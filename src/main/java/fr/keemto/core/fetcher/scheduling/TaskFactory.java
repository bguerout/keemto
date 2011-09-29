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

package fr.keemto.core.fetcher.scheduling;

import fr.keemto.core.EventRepository;
import fr.keemto.core.User;
import fr.keemto.core.fetcher.Fetcher;
import fr.keemto.core.fetcher.FetcherLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskFactory {

    private static final Logger log = LoggerFactory.getLogger(TaskFactory.class);

    private final EventRepository eventRepository;

    private final FetcherLocator fetcherLocator;

    @Autowired
    public TaskFactory(EventRepository eventRepository, FetcherLocator fetcherLocator) {
        this.eventRepository = eventRepository;
        this.fetcherLocator = fetcherLocator;

    }

    public List<FetchingTask> createTasks(User user) {

        List<FetchingTask> tasks = new ArrayList<FetchingTask>();
        for (Fetcher fetcher : fetcherLocator.getFetchersFor(user)) {
            FetchingTask task = new FetchingTask(fetcher, user, eventRepository);
            tasks.add(task);
        }
        return tasks;
    }

}
