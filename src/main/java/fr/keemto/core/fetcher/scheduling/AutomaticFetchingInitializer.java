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

import fr.keemto.core.User;
import fr.keemto.core.UserResolver;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AutomaticFetchingInitializer implements InitializingBean {

    private final UserResolver userResolver;
    private final FetchingTaskFactory fetchingTaskFactory;
    private final TaskRegistrar registrar;

    @Autowired
    public AutomaticFetchingInitializer(UserResolver userResolver, FetchingTaskFactory fetchingTaskFactory, TaskRegistrar registrar) {
        this.userResolver = userResolver;
        this.fetchingTaskFactory = fetchingTaskFactory;
        this.registrar = registrar;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        registerAllTasks();
    }

    protected void registerAllTasks() {
        for (User user : userResolver.getAllUsers()) {
            List<FetchingTask> tasks = fetchingTaskFactory.createTasks(user);
            registrar.registerTasks(tasks);
        }
    }

}
