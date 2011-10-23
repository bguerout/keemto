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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Component
public class TaskRegistrar {

    private static final Logger log = LoggerFactory.getLogger(TaskRegistrar.class);

    private final TaskScheduler scheduler;

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
        log.info("A new fetching task has been registered: {}. This task will run every {}ms", task, task.getDelay());
    }


}
