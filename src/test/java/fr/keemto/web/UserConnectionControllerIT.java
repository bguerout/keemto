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

import fr.keemto.util.JacksonConnection;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserConnectionControllerIT extends ControllerTestCase {

    private static final Logger log = LoggerFactory.getLogger(UserConnectionControllerIT.class);

    @Mock
    private ConnectionRepository repository;
    private UserConnectionController controller;

    @Before
    public void initBeforeTest() throws Exception {
        initMocks(this);

        controller = new UserConnectionController(repository);

        request.setMethod("GET");
        request.setRequestURI("/api/connections");
        request.addHeader("Accept", "application/json");
    }

    @Test
    public void showReturnAllConnections() throws Exception {

        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
        connections.add("twitter", new JacksonConnection<Object>());
        when(repository.findAllConnections()).thenReturn(connections);

        handlerAdapter.handle(request, response, controller);

        assertThat(response.getStatus(), equalTo(200));
        Map<String, String> eventsAsJSon = getJsonSingleResultAsMap(response);
        assertThat(eventsAsJSon.size(), equalTo(1));
        assertThat(eventsAsJSon.get("twitter"), notNullValue());
    }

}
