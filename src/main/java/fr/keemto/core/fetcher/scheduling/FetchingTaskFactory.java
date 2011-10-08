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

import fr.keemto.core.Account;
import fr.keemto.core.AccountFactory;
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
public class FetchingTaskFactory {

    private static final Logger log = LoggerFactory.getLogger(FetchingTaskFactory.class);

    private final AccountFactory accountFactory;
    private final EventRepository eventRepository;


    @Autowired
    public FetchingTaskFactory(AccountFactory accountFactory, EventRepository eventRepository) {
        this.accountFactory = accountFactory;
        this.eventRepository = eventRepository;
    }


    public List<FetchingTask> createTasks(User user) {

        List<FetchingTask> tasks = new ArrayList<FetchingTask>();
        for (Account account : accountFactory.getAccounts(user)) {
            FetchingTask task = new FetchingTask(account, eventRepository);
            tasks.add(task);
        }
        return tasks;
    }

}
