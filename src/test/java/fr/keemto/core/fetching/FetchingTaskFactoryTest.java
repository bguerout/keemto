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
    private AccountRegistry accountRegistry;

    @Before
    public void initBeforeTest() throws Exception {

        accounts = new ArrayList<Account>();
        accounts.add(mock(Account.class));
        accountRegistry = mock(AccountRegistry.class);

        eventRepository = mock(EventRepository.class);
        fetchingTaskFactory = new FetchingTaskFactory(accountRegistry, eventRepository);

    }

    @Test
    public void shouldCreateTaskWithUser() throws Exception {

        when(accountRegistry.findAccounts(user)).thenReturn(accounts);

        List<FetchingTask> tasks = fetchingTaskFactory.createTasks(user);

        assertThat(tasks, notNullValue());
        assertThat(tasks.size(), equalTo(1));
    }

    @Test
    public void shouldCreateTasksWithUser() throws Exception {

        when(accountRegistry.findAccounts(user)).thenReturn(accounts);

        Account acc2 = mock(Account.class);
        accounts.add(acc2);

        List<FetchingTask> tasks = fetchingTaskFactory.createTasks(user);

        assertThat(tasks, notNullValue());
        assertThat(tasks.size(), equalTo(2));
    }


    @Test
    public void shouldCreateTaskBasedOnKey() throws Exception {

        AccountKey key = new AccountKey("provider", "userId", new User("bguerout"));
        Account account = mock(Account.class);
        when(accountRegistry.findAccount(key)).thenReturn(account);
        when(account.getKey()).thenReturn(key);

        IncrementalFetchingTask task = fetchingTaskFactory.createIncrementalTask(key);

        assertThat(task, notNullValue());
        assertThat(task.getTaskId(), equalTo(""+key.hashCode()));
    }

}
