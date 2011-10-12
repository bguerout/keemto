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
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FetchingTaskFactoryTest {

    private FetchingTaskFactory fetchingTaskFactory;
    private EventRepository eventRepository;
    private List<Account> accounts;
    private final User user = new User("user");

    @Before
    public void initBeforeTest() throws Exception {

        accounts = new ArrayList<Account>();
        accounts.add(mock(Account.class));
        AccountFactory accountFactory = mock(AccountFactory.class);
        when(accountFactory.getAccounts(user)).thenReturn(accounts);

        eventRepository = mock(EventRepository.class);
        fetchingTaskFactory = new FetchingTaskFactory(accountFactory, eventRepository);

    }

    @Test
    public void shouldCreateTaskWithUser() throws Exception {

        List<FetchingTask> tasks = fetchingTaskFactory.createTasks(user);

        assertThat(tasks, notNullValue());
        assertThat(tasks.size(), equalTo(1));
    }

    @Test
    public void shouldCreateTasksWithUser() throws Exception {

        Account acc2 = mock(Account.class);
        accounts.add(acc2);

        List<FetchingTask> tasks = fetchingTaskFactory.createTasks(user);

        assertThat(tasks, notNullValue());
        assertThat(tasks.size(), equalTo(2));
    }

}
