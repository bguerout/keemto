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

package fr.keemto.scheduling;

import com.google.common.collect.Lists;
import fr.keemto.core.AccountKey;
import fr.keemto.core.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.scheduling.TaskScheduler;

import java.util.concurrent.ScheduledFuture;

import static org.mockito.Mockito.*;

public class TaskRegistrarTest {

    private TaskRegistrar registrar;
    private TaskScheduler scheduler;

    @Before
    public void initBeforeTest() throws Exception {
        scheduler = mock(TaskScheduler.class);
        registrar = new TaskRegistrar(scheduler);

    }

    @Test
    public void shouldAutomaticallyRegisterTaskToScheduler() throws Exception {
        FetchingTask task = mock(FetchingTask.class);

        registrar.registerTask(task);

        verify(scheduler).scheduleWithFixedDelay(task, task.getDelay());
    }

    @Test
    public void shouldRegisterAllTasks() throws Exception {
        FetchingTask task = mock(FetchingTask.class);
        FetchingTask anotherTask = mock(FetchingTask.class);

        registrar.registerTasks(Lists.newArrayList(task, anotherTask));

        verify(scheduler, timeout(2)).scheduleWithFixedDelay(task, task.getDelay());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenAccountKeyCannotBeCancelled() throws Exception {

        registrar.findAndCancelTask(new AccountKey("provider", "userId", new User("bguerout")));
    }

    @Test
    public void shouldCancelTask() throws Exception {
        AccountKey key = new AccountKey("provider", "userId", new User("bguerout"));
        FetchingTask task = mock(FetchingTask.class);
        ScheduledFuture future = mock(ScheduledFuture.class);
        when(task.getFetchedAccountKey()).thenReturn(key);
        when(scheduler.scheduleWithFixedDelay(task, task.getDelay())).thenReturn(future);
        registrar.registerTask(task);

        registrar.findAndCancelTask(key);

        verify(future).cancel(true);
    }


}
