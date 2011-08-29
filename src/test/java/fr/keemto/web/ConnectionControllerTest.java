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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionKey;
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

public class ConnectionControllerTest extends ControllerTestCase {

    private static final Logger log = LoggerFactory.getLogger(ConnectionControllerTest.class);

    @Mock
    private ConnectionRepository repository;
    private ConnectionController controller;
    private ConnectionData data;

    @Before
    public void initBeforeTest() throws Exception {
        initMocks(this);

        controller = new ConnectionController(repository);

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
        String expectedJson = getJsonFileAsString("connections.json");
        String jsonResponse = response.getContentAsString();
        assertThat(jsonResponse, equalTo(expectedJson));

    }

    @Test
    public void whenUserHasnotConnectionShouldReturnEmptyJson() throws Exception {

        request.setMethod("GET");
        request.setRequestURI("/api/connections");

        when(repository.findAllConnections()).thenReturn(new LinkedMultiValueMap<String, Connection<?>>());

        handlerAdapter.handle(request, response, controller);

        assertThat(response.getStatus(), equalTo(200));
        JsonNode node = toJsonNode(response.getContentAsString());
        assertThat(node.size(), equalTo(0));
    }

    @Test
    public void showReturnUserConnectionById() throws Exception {

        request.setMethod("GET");
        request.setRequestURI("/api/connections/twitter-1111");

        when(repository.getConnection(new ConnectionKey("twitter", "1111"))).thenReturn(new NullConnection<Object>(data));

        handlerAdapter.handle(request, response, controller);

        assertThat(response.getStatus(), equalTo(200));
        String expectedJson = getJsonFileAsString("connection.json");
        String jsonResponse = response.getContentAsString();
        assertThat(jsonResponse, equalTo(expectedJson));

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

}
