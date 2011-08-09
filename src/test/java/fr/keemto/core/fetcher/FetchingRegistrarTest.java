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

import com.google.common.collect.Lists;
import fr.keemto.core.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.scheduling.TaskScheduler;

import java.util.concurrent.ScheduledFuture;

import static org.mockito.Mockito.*;

public class FetchingRegistrarTest {

    private FetchingRegistrar registrar;
    private TaskScheduler scheduler;
    private User user;

    @Before
    public void initBeforeTest() throws Exception {
        user = new User("bguerout");
        scheduler = mock(TaskScheduler.class);
        registrar = new FetchingRegistrar(scheduler);

    }

    @Test
    public void shouldAutomaticallyRegisterTaskToScheduler() throws Exception {
        EventUpdateTask task = mock(EventUpdateTask.class);
        when(task.getUser()).thenReturn(user);

        registrar.registerTask(task);

        verify(scheduler).scheduleWithFixedDelay(task, task.getDelay());
    }

    @Test
    public void shouldRegisterAllTasks() throws Exception {
        EventUpdateTask task = mock(EventUpdateTask.class);
        when(task.getUser()).thenReturn(user);
        EventUpdateTask anotherTask = mock(EventUpdateTask.class);
        when(anotherTask.getUser()).thenReturn(user);

        registrar.registerTasks(Lists.newArrayList(task, anotherTask));

        verify(scheduler, timeout(2)).scheduleWithFixedDelay(task, task.getDelay());
    }

    @Test
    public void shouldCancelTAsksForUser() throws Exception {
        EventUpdateTask task = mock(EventUpdateTask.class);
        when(task.getUser()).thenReturn(user);
        ScheduledFuture<?> future = mock(ScheduledFuture.class);
        when(scheduler.scheduleWithFixedDelay(task, task.getDelay())).thenReturn(future);

        registrar.registerTask(task);
        registrar.cancelTasks(user);

        verify(future).cancel(false);

    }

}
