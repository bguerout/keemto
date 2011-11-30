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

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import fr.keemto.core.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

class TaskScheduler {

    private static final Logger log = LoggerFactory.getLogger(TaskScheduler.class);

    private final org.springframework.scheduling.TaskScheduler scheduler;

    private final Set<ScheduledTask> scheduledTasks = new HashSet<ScheduledTask>();

    public TaskScheduler(org.springframework.scheduling.TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void scheduleTask(Task task) {

        if (checkIfTaskHasAlreadyBeenScheduled(task.getTaskId())) {
            throw new IllegalArgumentException("Task" + task + " has already been registered. Cancel it before register it again.");
        }
        ScheduledFuture<?> future = scheduler.scheduleWithFixedDelay(task, task.getDelay());
        ScheduledTask scheduledTask = new ScheduledTask(task, future);
        scheduledTasks.add(scheduledTask);
        log.info("Task has been registered: {}. This task is going to run every {}ms", task, task.getDelay());
    }


    public boolean checkIfTaskHasAlreadyBeenScheduled(Object taskId) {
        return Sets.filter(scheduledTasks, new WithTaskId(taskId)).size() > 0;
    }


    public void cancelTask(Object taskId) {
        ScheduledTask task = findTask(taskId);
        task.cancel();
        scheduledTasks.remove(task);
        log.info("Task {} has been cancelled and removed from registry", taskId);
    }

    public Set<ScheduledTask> getScheduledTasks() {
        return Collections.unmodifiableSet(scheduledTasks);
    }

    private ScheduledTask findTask(Object taskId) {
        for (ScheduledTask scheduledTask : scheduledTasks) {
            if (scheduledTask.getTaskId().equals(taskId)) {
                return scheduledTask;
            }
        }
        throw new IllegalArgumentException("No task seems to be registered with id:" + taskId);
    }


    private static class WithTaskId implements Predicate<ScheduledTask> {
        private final Object taskId;

        public WithTaskId(Object taskId) {
            this.taskId = taskId;
        }

        @Override
        public boolean apply(ScheduledTask task) {
            return task.getTaskId().equals(taskId);
        }
    }
}
