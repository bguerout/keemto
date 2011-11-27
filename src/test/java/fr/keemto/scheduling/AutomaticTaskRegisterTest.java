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
import fr.keemto.core.UserRepository;
import fr.keemto.core.fetching.FetchingTask;
import fr.keemto.core.fetching.FetchingTaskFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class AutomaticTaskRegisterTest {

    private AutomaticTaskRegister initializer;
    private UserRepository userRepository;
    private FetchingTaskFactory fetchingTaskFactory;
    private TaskRegistrar registrar;

    @Before
    public void initBeforeTest() throws Exception {
        initMocks(this);
        userRepository = mock(UserRepository.class);
        fetchingTaskFactory = mock(FetchingTaskFactory.class);
        registrar = mock(TaskRegistrar.class);

        initializer = new AutomaticTaskRegister();
        initializer.setUserRepository(userRepository);
        initializer.setFetchingTaskFactory(fetchingTaskFactory);
        initializer.setTaskRegistrar(registrar);
    }

    @Test
    public void shouldRegisterTask() throws Exception {
        Task task = mock(Task.class);
        initializer.setTasks(Lists.newArrayList(task));

        initializer.setApplicationContext(null);

        verify(registrar).registerTask(task);
    }

    @Test
    public void shouldRegisterFetchingTaskUsingFactory() throws Exception {
        User user = new User("bguerout");

        FetchingTask task = mock(FetchingTask.class);
        List<FetchingTask> tasks = Lists.newArrayList(task);
        when(fetchingTaskFactory.createTasks(user)).thenReturn(tasks);
        when(userRepository.getAllUsers()).thenReturn(Lists.newArrayList(user));

        initializer.registerFetchingTasksForAllUsers();

        verify(registrar, times(1)).registerTask(task);

    }

    @Test
    public void shouldRegisterFetchingTaskForAllUsers() throws Exception {

        User bguerout = new User("bguerout");
        User stnevex = new User("stnevex");
        FetchingTask bgueroutTask = mock(FetchingTask.class);
        FetchingTask stnevexTask = mock(FetchingTask.class);
        when(userRepository.getAllUsers()).thenReturn(Lists.newArrayList(bguerout, stnevex));
        when(fetchingTaskFactory.createTasks(bguerout)).thenReturn(Lists.newArrayList(bgueroutTask));
        when(fetchingTaskFactory.createTasks(stnevex)).thenReturn(Lists.newArrayList(stnevexTask));

        initializer.registerFetchingTasksForAllUsers();

        verify(userRepository).getAllUsers();
        verify(registrar).registerTask(bgueroutTask);
        verify(registrar).registerTask(stnevexTask);
    }


}
