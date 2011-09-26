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
import fr.keemto.core.*;
import org.codehaus.jackson.JsonNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
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
    public void apiExposesAllEvents() throws Exception {


        handlerAdapter.handle(request, response, controller);

        verify(eventRepository).getAllEvents();

    }

        @Test
    public void apiCanFilterEventsByDate() throws Exception {

        request.addParameter("newerThan","1");

        handlerAdapter.handle(request, response, controller);

        verify(eventRepository).getEvents(1);

    }

    @Test
    public void shouldReturnEventsAsJson() throws Exception {

        request.addParameter("newerThan","1");
        DefaultProviderConnection yammerConnection = new DefaultProviderConnection("yammer", "4444", "stnevex", "http://profileUrl", "http://imageUrl");
        User user = new User("stnevex","John","Doe","stnevex@gmail.com");
        List<Event> events = Lists.newArrayList(new Event(1, "message", user, yammerConnection));
        when(eventRepository.getEvents(1)).thenReturn(events);

        handlerAdapter.handle(request, response, controller);

        assertThat(response.getStatus(), equalTo(200));
        JsonNode eventsAsJson = toJsonNode(response.getContentAsString());
        assertThat(eventsAsJson.isArray(), is(true));
        assertThat(eventsAsJson.has(0), is(true));
        JsonNode eventNode = eventsAsJson.get(0);
        assertThat(eventNode.get("timestamp").getValueAsText(), equalTo("1"));
        assertThat(eventNode.get("message").getValueAsText(), equalTo("message"));

        JsonNode providerConnxNode = eventNode.get("providerConnection");
        assertThat(providerConnxNode.get("providerId").getValueAsText(), equalTo("yammer"));
        assertThat(providerConnxNode.get("providerUserId").getValueAsText(), equalTo("4444"));
        assertThat(providerConnxNode.get("displayName").getValueAsText(), equalTo("stnevex"));
        assertThat(providerConnxNode.get("profileUrl").getValueAsText(), equalTo("http://profileUrl"));
        assertThat(providerConnxNode.get("imageUrl").getValueAsText(), equalTo("http://imageUrl"));
        assertThat(providerConnxNode.get("anonymous").getValueAsText(), equalTo("false"));

        JsonNode userNode = eventNode.get("user");
        assertThat(userNode.get("username").getValueAsText(), equalTo("stnevex"));
        assertThat(userNode.get("firstName").getValueAsText(), equalTo("John"));
        assertThat(userNode.get("lastName").getValueAsText(), equalTo("Doe"));
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
