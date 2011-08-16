/*
 * Copyright (C) 2010 Benoit Guerout <bguerout at gmail dot com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.keemto.web;

import fr.keemto.util.NullConnection;
import org.codehaus.jackson.JsonNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserConnectionControllerIT extends ControllerTestCase {

    private static final Logger log = LoggerFactory.getLogger(UserConnectionControllerIT.class);

    @Mock
    private ConnectionRepository repository;
    private UserConnectionController controller;
    private ConnectionData data;

    @Before
    public void initBeforeTest() throws Exception {
        initMocks(this);

        controller = new UserConnectionController(repository);

        request.setMethod("GET");
        request.addHeader("Accept", "application/json");

        data = new ConnectionData("providerId", "providerUserId", "stnevex",
                "http://twitter.com/stnevex", "http://twitter.com/stnevex.jpg",
                "accessToken", "secret", "refreshToken", (long) 999);
    }

    @Test
    public void showReturnAllConnections() throws Exception {

        request.setRequestURI("/api/connections");

        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
        connections.add("twitter", new NullConnection<Object>(data));
        when(repository.findAllConnections()).thenReturn(connections);

        handlerAdapter.handle(request, response, controller);

        assertThat(response.getStatus(), equalTo(200));

        JsonNode node = toJsonNode(response.getContentAsString());
        JsonNode twitterConnx = node.get("twitter");
        assertThat(twitterConnx, notNullValue());
        assertThat(twitterConnx.findPath("displayName").getValueAsText(), equalTo("stnevex"));
        assertThat(twitterConnx.findPath("profileUrl").getValueAsText(), equalTo("http://twitter.com/stnevex"));
        assertThat(twitterConnx.findPath("imageUrl").getValueAsText(), equalTo("http://twitter.com/stnevex.jpg"));

    }

    @Test
    public void showReturnProviderConnections() throws Exception {

        request.setRequestURI("/api/connections/twitter");

        List<Connection<?>> connections = new ArrayList<Connection<?>>();
        connections.add(new NullConnection<Object>(data));
        when(repository.findConnections("twitter")).thenReturn(connections);

        handlerAdapter.handle(request, response, controller);

        assertThat(response.getStatus(), equalTo(200));

        JsonNode node = toJsonNode(response.getContentAsString());
        assertThat(node, notNullValue());
        assertThat(node.findPath("displayName").getValueAsText(), equalTo("stnevex"));
        assertThat(node.findPath("profileUrl").getValueAsText(), equalTo("http://twitter.com/stnevex"));
        assertThat(node.findPath("imageUrl").getValueAsText(), equalTo("http://twitter.com/stnevex.jpg"));

        verify(repository).findConnections("twitter");

    }

}
