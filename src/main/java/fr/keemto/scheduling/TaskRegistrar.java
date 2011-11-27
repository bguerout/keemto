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

import fr.keemto.core.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

public class TaskRegistrar {

    private static final Logger log = LoggerFactory.getLogger(TaskRegistrar.class);

    private final TaskScheduler scheduler;

    private final List<ScheduledTask> scheduledTasks = new ArrayList<ScheduledTask>();

    public TaskRegistrar(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void registerTask(Task task) {
        ScheduledFuture<?> future = scheduler.scheduleWithFixedDelay(task, task.getDelay());
        ScheduledTask scheduledTask = new ScheduledTask(task.getTaskId(), future);
        scheduledTasks.add(scheduledTask);
        log.info("A new task has been registered: {}. This task is going to run every {}ms", task, task.getDelay());
    }

    public void cancelTask(Object taskId) {
        ScheduledTask task = findTask(taskId);
        task.cancel();
        scheduledTasks.remove(task);
        log.info("Task {} has been cancelled and removed from registry", taskId);
    }

    private ScheduledTask findTask(Object taskId) {
        for (ScheduledTask scheduledTask : scheduledTasks) {
            if (scheduledTask.id.equals(taskId)) {
                return scheduledTask;
            }
        }
        throw new IllegalArgumentException("No task seems to be registered with id:" + taskId);
    }

    private static class ScheduledTask {
        private static final boolean INTERRUPT_IF_RUNNING = true;
        private final Object id;
        private final ScheduledFuture<?> future;

        private ScheduledTask(Object id, ScheduledFuture<?> future) {
            this.id = id;
            this.future = future;
        }

        public void cancel() {
            future.cancel(INTERRUPT_IF_RUNNING);
        }
    }


}
