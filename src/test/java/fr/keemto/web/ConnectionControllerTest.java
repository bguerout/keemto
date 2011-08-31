/*
 * Copyright (C) 2010 Benoit Guerout <bguerout at gmail dot com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package fr.keemto.web;

import fr.keemto.util.NullConnection;
import org.codehaus.jackson.JsonNode;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.*;
import org.springframework.social.connect.support.OAuth1ConnectionFactory;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.connect.web.ConnectSupport;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ConnectionControllerTest extends ControllerTestCase {

    private static final Logger log = LoggerFactory.getLogger(ConnectionControllerTest.class);

    @Mock
    private ConnectionRepository repository;
    @Mock
    private ConnectionFactoryLocator connectionFactoryLocator;

    @Mock
    private ConnectSupport webSupport;

    private ConnectionController controller;
    private ConnectionData data;

    @Before
    public void initBeforeTest() throws Exception {
        initMocks(this);

        controller = new ConnectionController(repository, connectionFactoryLocator, webSupport);

        request.addHeader("Accept", "application/json");

        String providerUserId = "1111";
        String providerId = "twitter";
        String profileUrl = "http://twitter.com/stnevex";
        String imageUrl = "http://twitter.com/stnevex.jpg";
        String displayName = "stnevex";
        data = new ConnectionData(providerId, providerUserId, displayName,
                profileUrl, imageUrl, "accessToken", "secret", "refreshToken", (long) 999);
    }

    @Test
    public void showReturnAllConnections() throws Exception {

        request.setMethod("GET");
        request.setRequestURI("/api/connections");

        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
        connections.add("twitter", new NullConnection<Object>(data));
        when(repository.findAllConnections()).thenReturn(connections);

        handlerAdapter.handle(request, response, controller);

        assertThat(response.getStatus(), equalTo(200));
        JsonNode jsonNode = toJsonNode(response.getContentAsString());
        assertThat(jsonNode.isArray(), is(true));
        assertThat(jsonNode.has(0), is(true));
        JsonNode connx = jsonNode.get(0);
        assertThat(connx.get("id").getValueAsText(), equalTo("twitter-1111"));
        assertThat(connx.get("providerId").getValueAsText(), equalTo("twitter"));
        assertThat(connx.get("displayName").getValueAsText(), equalTo("stnevex"));
        assertThat(connx.get("profileUrl").getValueAsText(), equalTo("http://twitter.com/stnevex"));
        assertThat(connx.get("imageUrl").getValueAsText(), equalTo("http://twitter.com/stnevex.jpg"));


    }

    @Test
    public void whenUserHasNoConnectionShouldReturnEmptyJson() throws Exception {

        request.setMethod("GET");
        request.setRequestURI("/api/connections");

        when(repository.findAllConnections()).thenReturn(new LinkedMultiValueMap<String, Connection<?>>());

        handlerAdapter.handle(request, response, controller);

        assertThat(response.getStatus(), equalTo(200));
        JsonNode node = toJsonNode(response.getContentAsString());
        assertThat(node.size(), equalTo(0));
    }

    @Test
    public void showReturnConnectionById() throws Exception {

        request.setMethod("GET");
        request.setRequestURI("/api/connections/twitter-1111");

        when(repository.getConnection(new ConnectionKey("twitter", "1111"))).thenReturn(new NullConnection<Object>(data));

        handlerAdapter.handle(request, response, controller);

        assertThat(response.getStatus(), equalTo(200));
        JsonNode connx = toJsonNode(response.getContentAsString());
        assertThat(connx.get("id").getTextValue(), equalTo("twitter-1111"));
        assertThat(connx.get("providerId").getTextValue(), equalTo("twitter"));
        assertThat(connx.get("displayName").getTextValue(), equalTo("stnevex"));
        assertThat(connx.get("profileUrl").getTextValue(), equalTo("http://twitter.com/stnevex"));
        assertThat(connx.get("imageUrl").getTextValue(), equalTo("http://twitter.com/stnevex.jpg"));

    }

    @Test
    public void shouldDeleteConnection() throws Exception {

        request.setMethod("DELETE");
        request.setRequestURI("/api/connections/twitter-9999");

        handlerAdapter.handle(request, response, controller);

        assertThat(response.getStatus(), equalTo(204));
        verify(repository).removeConnection(new ConnectionKey("twitter", "9999"));
    }


    @Test
    public void shouldDeleteConnectionBySplittingKeyWithLastIndexOfMinus() throws Exception {

        request.setMethod("DELETE");
        request.setRequestURI("/api/connections/linked-in-9999");

        handlerAdapter.handle(request, response, controller);

        assertThat(response.getStatus(), equalTo(204));
        verify(repository).removeConnection(new ConnectionKey("linked-in", "9999"));
    }

    @Test
    public void shouldBeginConnectionCreationByRedirectUserToProviderUrl() throws Exception {

        when(webSupport.buildOAuthUrl(any(ConnectionFactory.class), any(NativeWebRequest.class))).thenReturn("redirectUrl");

        RedirectView redirectView = controller.connect("twitter", mock(NativeWebRequest.class));

        assertThat(redirectView.getUrl(), equalTo("redirectUrl"));
    }

    @Test
    public void providerShouldCalledBackWithOAuth1() throws Exception {

        request.setMethod("GET");
        request.setRequestURI("/api/connections/twitter");
        request.setParameter("oauth_token", "xxx");
        Connection newConnectionCreated = mock(Connection.class);
        when(webSupport.completeConnection(any(OAuth1ConnectionFactory.class), any(NativeWebRequest.class))).thenReturn(newConnectionCreated);

        ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);

        verify(repository).addConnection(newConnectionCreated);
        assertThat(modelAndView.getView(), Matchers.<Object>instanceOf(RedirectView.class));
        RedirectView view = (RedirectView)modelAndView.getView();
        assertThat(view.getUrl(),equalTo("/#connections"));
    }

    @Test
    public void providerShouldCalledBackWithOAuth2() throws Exception {

        request.setMethod("GET");
        request.setRequestURI("/api/connections/yammer");
        request.setParameter("code", "xxx");
        Connection newConnectionCreated = mock(Connection.class);
        when(webSupport.completeConnection(any(OAuth2ConnectionFactory.class), any(NativeWebRequest.class))).thenReturn(newConnectionCreated);

        ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);

        verify(repository).addConnection(newConnectionCreated);
        assertThat(modelAndView.getView(), Matchers.<Object>instanceOf(RedirectView.class));
        RedirectView view = (RedirectView)modelAndView.getView();
        assertThat(view.getUrl(),equalTo("/#connections"));
    }


}
