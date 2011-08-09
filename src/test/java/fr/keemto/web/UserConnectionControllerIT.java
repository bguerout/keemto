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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.ModelAndView;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.ModelAndViewAssert.*;

public class UserConnectionControllerIT extends ControllerTestCase {

    @Mock
    private ConnectionRepository repository;
    private UserConnectionController controller;

    @Before
    public void initBeforeTest() throws Exception {
        initMocks(this);

        controller = new UserConnectionController(repository);

        request.setMethod("GET");
        request.setRequestURI("/connections");
    }

    @Test
    public void showReturnConnectionsView() throws Exception {

        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
        connections.add("twitter", mock(Connection.class));
        when(repository.findAllConnections()).thenReturn(connections);

        ModelAndView mav = handlerAdapter.handle(request, response, controller);

        assertViewName(mav, "connections");
    }

    @Test
    public void showReturnAllUserConnections() throws Exception {

        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
        connections.add("twitter", mock(Connection.class));
        when(repository.findAllConnections()).thenReturn(connections);

        ModelAndView mav = handlerAdapter.handle(request, response, controller);

        assertModelAttributeAvailable(mav, "connections");
        assertModelAttributeValue(mav, "connections", connections);
    }

}
