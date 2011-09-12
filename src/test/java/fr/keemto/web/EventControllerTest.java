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

import com.google.common.collect.Lists;
import fr.keemto.core.Event;
import fr.keemto.core.EventRepository;
import fr.keemto.core.User;
import org.codehaus.jackson.JsonNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

public class EventControllerTest extends ControllerTestCase {

    @Mock
    private EventRepository eventRepository;

    private EventController controller;

    @Before
    public void prepare() throws Exception {
        controller = new EventController(eventRepository);
        request.addHeader("Accept", "application/json");
        request.setMethod("GET");
        request.setRequestURI("/api/events");
    }

    @Test
    public void shouldReturnAllEvents() throws Exception {

        User user1 = new User("user1");
        ArrayList<Event> events = Lists.newArrayList(new Event(1, "message", user1, "provider"));
        when(eventRepository.getAllEvents()).thenReturn(events);

        handlerAdapter.handle(request, response, controller);

        assertThat(response.getStatus(), equalTo(200));
        JsonNode eventsAsJson = toJsonNode(response.getContentAsString());
        assertThat(eventsAsJson.isArray(), is(true));
        assertThat(eventsAsJson.has(0), is(true));
        JsonNode eventNode = eventsAsJson.get(0);
        assertThat(eventNode.get("timestamp").getValueAsText(), equalTo("1"));
        assertThat(eventNode.get("message").getValueAsText(), equalTo("message"));
        assertThat(eventNode.get("providerId").getValueAsText(), equalTo("provider"));
        JsonNode userNode = eventNode.get("user");
        assertThat(userNode.get("username").getValueAsText(), equalTo("user1"));

    }

    @Test
    public void whenNoEventHasBeenFetchedShouldReturnAnEmptyJson() throws Exception {

        when(eventRepository.getAllEvents()).thenReturn(new ArrayList<Event>());

        handlerAdapter.handle(request, response, controller);

        assertThat(response.getStatus(), equalTo(200));
        JsonNode eventsAsJson = toJsonNode(response.getContentAsString());
        assertThat(eventsAsJson, notNullValue());
        assertThat(eventsAsJson.size(), equalTo(0));
    }

}
