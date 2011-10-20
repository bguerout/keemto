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

import fr.keemto.core.Event;
import fr.keemto.core.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class EventController {

    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    private final EventRepository eventRepository;

    @Autowired
    public EventController(EventRepository repository) {
        this.eventRepository = repository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/events")
    @ResponseBody
    public List<Event> getAllEvents() {
        return eventRepository.getAllEvents();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/events", params = "newerThan")
    @ResponseBody
    public List<Event> getEvents(@RequestParam("newerThan") long newerThan) {
        log.debug("A client has requested events newerThan: " + newerThan);
        return eventRepository.getEvents(newerThan);
    }

}
