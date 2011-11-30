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

import fr.keemto.core.AccountKey;
import fr.keemto.core.User;
import fr.keemto.core.fetching.FetchingTask;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ScheduledFuture;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class TaskSchedulerTest {

    private static final int TASK_DELAY = 0;
    private TaskScheduler scheduler;
    private org.springframework.scheduling.TaskScheduler springScheduler;

    @Before
    public void initBeforeTest() throws Exception {
        springScheduler = mock(org.springframework.scheduling.TaskScheduler.class);
        this.scheduler = new TaskScheduler(springScheduler);

    }

    @Test
    public void shouldCheckIfTaskHasBeenRegistred() throws Exception {

        boolean isRegistered = this.scheduler.checkIfTaskHasAlreadyBeenScheduled("task-id");

        assertThat(isRegistered, is(false));
    }


    @Test
    public void shouldRegisterTaskToScheduler() throws Exception {
        FetchingTask task = new MockFetchingTask("task-id");

        this.scheduler.scheduleTask(task);

        verify(springScheduler).scheduleWithFixedDelay(task, task.getDelay());
        assertThat(this.scheduler.checkIfTaskHasAlreadyBeenScheduled("task-id"), is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenAccountKeyCannotBeCancelled() throws Exception {

        this.scheduler.cancelTask(new AccountKey("provider", "userId", new User("bguerout")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenRegisterTaskTwice() throws Exception {

        FetchingTask task = new MockFetchingTask("task-id");

        this.scheduler.scheduleTask(task);
        this.scheduler.scheduleTask(task);
    }

    @Test
    public void shouldCancelTask() throws Exception {
        FetchingTask task = new MockFetchingTask("task-id");
        ScheduledFuture future = mock(ScheduledFuture.class);
        when(springScheduler.scheduleWithFixedDelay(task, TASK_DELAY)).thenReturn(future);
        this.scheduler.scheduleTask(task);

        this.scheduler.cancelTask("task-id");

        verify(future).cancel(true);
    }

    @Test
    public void shouldSuccessWhenRegisterTaskTwiceAndCancel() throws Exception {

        FetchingTask task = new MockFetchingTask("task-id");
        ScheduledFuture future = mock(ScheduledFuture.class);
        when(springScheduler.scheduleWithFixedDelay(task, TASK_DELAY)).thenReturn(future);

        this.scheduler.scheduleTask(task);
        this.scheduler.cancelTask("task-id");
        this.scheduler.scheduleTask(task);

        verify(springScheduler, times(2)).scheduleWithFixedDelay(eq(task), anyLong());
    }

    private static class MockFetchingTask implements FetchingTask {

        private final String id;

        private MockFetchingTask(String id) {
            this.id = id;
        }

        @Override
        public long getDelay() {
            return TASK_DELAY;
        }

        @Override
        public Object getTaskId() {
            return id;
        }

        @Override
        public void run() {
        }
    }


}
