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
import fr.keemto.core.User;
import fr.keemto.core.UserRepository;
import fr.keemto.core.fetching.FetchingTask;
import fr.keemto.core.fetching.FetchingTaskFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AutomaticTaskRegister implements ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(AutomaticTaskRegister.class);

    private UserRepository userRepository;
    private FetchingTaskFactory fetchingTaskFactory;
    private TaskRegistrar registrar;
    private List<Task> tasks;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        registerFetchingTasksForAllUsers();
        registerTasks(tasks);
    }

    public void registerFetchingTasksForAllUsers() {
        for (User user : userRepository.getAllUsers()) {
            List<FetchingTask> tasks = fetchingTaskFactory.createTasks(user);
            registerTasks(tasks);
        }
    }

    private void registerTasks(List<? extends Task> tasks) {
        for (Task task : tasks) {
            registrar.registerTask(task);
        }
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setFetchingTaskFactory(FetchingTaskFactory fetchingTaskFactory) {
        this.fetchingTaskFactory = fetchingTaskFactory;
    }

    @Autowired
    public void setRegistrar(TaskRegistrar registrar) {
        this.registrar = registrar;
    }

    @Autowired
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
