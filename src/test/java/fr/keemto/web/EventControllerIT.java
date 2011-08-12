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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.ModelAndViewAssert.*;

public class EventControllerIT extends ControllerTestCase {

    @Mock
    private EventRepository eventRepository;

    private EventController controller;

    @Before
    public void initBeforeTest() throws Exception {
        controller = new EventController(eventRepository);
        request.setMethod("GET");
        request.setRequestURI("/");
    }

    @Test
    public void shouldReturnHomeViewWithEvents() throws Exception {
        ArrayList<Event> events = Lists.newArrayList(new Event(1, "user", "message", "provider"));
        when(eventRepository.getAllEvents()).thenReturn(events);

        final ModelAndView mav = handlerAdapter.handle(request, response, controller);
        assertViewName(mav, "home");
        assertModelAttributeAvailable(mav, "events");
        assertModelAttributeValue(mav, "events", events);
    }

    @Test
    public void shouldReturnAllEvents() throws Exception {

        request.setRequestURI("/api/events.json");
        request.addHeader("Accept", "application/json");

        ArrayList<Event> events = Lists.newArrayList(new Event(1, "user", "message", "provider"));
        when(eventRepository.getAllEvents()).thenReturn(events);

        final ModelAndView mav = handlerAdapter.handle(request, response, controller);

    }
}
