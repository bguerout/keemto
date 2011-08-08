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

package fr.keemto.core.fetcher;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.keemto.core.EventRepository;
import fr.keemto.core.User;

@Component
public class TaskFactory {

    private static final Logger log = LoggerFactory.getLogger(TaskFactory.class);

    private final EventRepository eventRepository;

    private final FetcherResolver fetcherResolver;

    @Autowired
    public TaskFactory(EventRepository eventRepository, FetcherResolver fetcherResolver) {
        this.eventRepository = eventRepository;
        this.fetcherResolver = fetcherResolver;

    }

    private EventUpdateTask createTask(Fetcher fetcher, User user) {
        return new EventUpdateTask(fetcher, user, eventRepository);
    }

    public List<EventUpdateTask> createTasks(User user) {

        List<EventUpdateTask> tasks = new ArrayList<EventUpdateTask>();
        for (Fetcher fetcher : fetcherResolver.resolve(user)) {
            EventUpdateTask task = createTask(fetcher, user);
            tasks.add(task);
        }
        return tasks;
    }

}
