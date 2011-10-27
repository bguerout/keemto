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

import fr.keemto.core.AccountKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Component
public class TaskRegistrar {

    private static final Logger log = LoggerFactory.getLogger(TaskRegistrar.class);

    private final TaskScheduler scheduler;

    private final List<ScheduledTask> scheduledTasks = new ArrayList<ScheduledTask>();

    @Autowired
    public TaskRegistrar(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void registerTasks(List<FetchingTask> tasks) {
        for (FetchingTask task : tasks) {
            registerTask(task);
        }
    }

    public void registerTask(FetchingTask task) {
        ScheduledFuture<?> future = scheduler.scheduleWithFixedDelay(task, task.getDelay());
        ScheduledTask scheduledTask = new ScheduledTask(future, task.getFetchedAccountKey());
        scheduledTasks.add(scheduledTask);
        log.info("A new fetching task has been registered: {}. This task is going to run every {}ms", task, task.getDelay());
    }

    public void findAndCancelTask(AccountKey key) {
        ScheduledTask task = findTask(key);
        task.cancel();
        scheduledTasks.remove(task);
        log.info("Task for account {} has been cancelled and removed from registry", key);
    }

    private ScheduledTask findTask(AccountKey key) {
        for (ScheduledTask scheduledTask : scheduledTasks) {
            if (scheduledTask.key.equals(key)) {
                return scheduledTask;
            }
        }
        throw new IllegalArgumentException("No task seems to be registered for account:" + key);
    }

    private static class ScheduledTask {
        private static final boolean INTERRUPT_IF_RUNNING = true;
        private final ScheduledFuture<?> future;
        private final AccountKey key;

        private ScheduledTask(ScheduledFuture<?> future, AccountKey key) {
            this.future = future;
            this.key = key;
        }

        public void cancel() {
            future.cancel(INTERRUPT_IF_RUNNING);
        }
    }


}
