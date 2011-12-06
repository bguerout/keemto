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
import fr.keemto.core.Task;
import fr.keemto.core.User;
import fr.keemto.core.fetching.FetchingTask;
import fr.keemto.core.fetching.FetchingTaskFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;

public class TaskRegistrarTest {

    private TaskRegistrar registrar;
    private FetchingTaskFactory fetchingTaskFactory;
    private TaskScheduler scheduler;

    @Before
    public void initBeforeTest() throws Exception {
        fetchingTaskFactory = mock(FetchingTaskFactory.class);
        scheduler = mock(TaskScheduler.class);
        registrar = new TaskRegistrar(fetchingTaskFactory, scheduler);
    }

    @Test
    public void shouldRegisterTasks() throws Exception {
        Task task = mock(Task.class);
        when(scheduler.getScheduledTasks()).thenReturn(new HashSet<ScheduledTask>());

        registrar.registerTasks(Lists.newArrayList(task));

        verify(scheduler).scheduleTask(task);
    }

    @Test
    public void shouldCancelAlreadyRegisteredTask() throws Exception {
        Task task = mock(Task.class);
        when(task.getTaskId()).thenReturn("task-id");
        when(scheduler.checkIfTaskHasAlreadyBeenScheduled("task-id")).thenReturn(true);

        registrar.registerTasks(Lists.newArrayList(task));

        verify(scheduler).cancelTask("task-id");
        verify(scheduler).scheduleTask(task);
    }


    @Test
    public void shouldRegisterFetchingTaskUsingFactory() throws Exception {
        User user = new User("bguerout");

        FetchingTask task = mock(FetchingTask.class);
        List<FetchingTask> tasks = Lists.newArrayList(task);
        when(fetchingTaskFactory.createTasks(user)).thenReturn(tasks);

        registrar.registerFetchingTasksFor(Lists.newArrayList(user));

        verify(scheduler, times(1)).scheduleTask(task);
    }

    @Test
    public void shouldRegisterFetchingTaskForAllUsers() throws Exception {

        User bguerout = new User("bguerout");
        User stnevex = new User("stnevex");
        FetchingTask bgueroutTask = mock(FetchingTask.class);
        FetchingTask stnevexTask = mock(FetchingTask.class);
        when(fetchingTaskFactory.createTasks(bguerout)).thenReturn(Lists.newArrayList(bgueroutTask));
        when(fetchingTaskFactory.createTasks(stnevex)).thenReturn(Lists.newArrayList(stnevexTask));

        registrar.registerFetchingTasksFor(Lists.newArrayList(bguerout, stnevex));

        verify(scheduler).scheduleTask(bgueroutTask);
        verify(scheduler).scheduleTask(stnevexTask);
    }


}
