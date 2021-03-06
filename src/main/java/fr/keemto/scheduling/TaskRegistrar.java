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

import java.util.List;
import java.util.Set;

public class TaskRegistrar {

    private static final Logger log = LoggerFactory.getLogger(TaskRegistrar.class);

    private final TaskScheduler taskScheduler;

    public TaskRegistrar(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public Set<ScheduledTask> getScheduledTasks() {
        return taskScheduler.getScheduledTasks();
    }

    public void registerTasks(List<? extends Task> tasks) {
        for (Task task : tasks) {
            registerTaskForScheduling(task);
        }
    }

    public void registerTaskForScheduling(Task task) {
        Object taskId = task.getTaskId();
        if (taskScheduler.checkIfTaskHasAlreadyBeenScheduled(taskId)) {
            taskScheduler.cancelTask(taskId);
        }
        taskScheduler.scheduleTask(task);
    }

    public void cancelTask(String taskId) {
        taskScheduler.cancelTask(taskId);
    }
}
