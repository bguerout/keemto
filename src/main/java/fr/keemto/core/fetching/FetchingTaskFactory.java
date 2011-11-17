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

package fr.keemto.core.fetching;

import fr.keemto.core.*;

import java.util.ArrayList;
import java.util.List;

public class FetchingTaskFactory {

    private final AccountFactory accountFactory;
    private final EventRepository eventRepository;


    public FetchingTaskFactory(AccountFactory accountFactory, EventRepository eventRepository) {
        this.accountFactory = accountFactory;
        this.eventRepository = eventRepository;
    }


    public List<FetchingTask> createTasks(User user) {

        List<FetchingTask> tasks = new ArrayList<FetchingTask>();
        for (Account account : accountFactory.getAccounts(user)) {
            FetchingTask task = new IncrementalFetchingTask(account, eventRepository);
            tasks.add(task);
        }
        return tasks;
    }

    public IncrementalFetchingTask createIncrementalTask(AccountKey key) {
        Account account = accountFactory.getAccount(key);
        return new IncrementalFetchingTask(account, eventRepository);
    }
}