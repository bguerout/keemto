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
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

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

        registrar.registerTaskForScheduling(task);

        verify(scheduler).cancelTask("task-id");
        verify(scheduler).scheduleTask(task);
    }

    @Test
    public void shouldCancelTaskById() throws Exception {

        registrar.cancelTask("task-id");

        verify(scheduler).cancelTask("task-id");
    }



}
