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
import fr.keemto.core.User;
import fr.keemto.core.UserRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

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
        initializer = new AutomaticTaskRegister(userRepository, fetchingTaskFactory, registrar);
    }

    @Test
    public void shouldRegisterTask() throws Exception {
        User user = new User("bguerout");

        ArrayList<FetchingTask> tasks = Lists.newArrayList(mock(FetchingTask.class));
        when(fetchingTaskFactory.createTasks(user)).thenReturn(tasks);
        when(userRepository.getAllUsers()).thenReturn(Lists.newArrayList(user));

        initializer.registerAllTasks();

        verify(registrar).registerTasks(tasks);

    }

    @Test
    public void shouldRegisterTaskForAllUsers() throws Exception {
        User bguerout = new User("bguerout");
        User stnevex = new User("stnevex");
        ArrayList<FetchingTask> bgueroutTasks = Lists.newArrayList(mock(FetchingTask.class));
        ArrayList<FetchingTask> stnevexTasks = Lists.newArrayList(mock(FetchingTask.class));
        when(userRepository.getAllUsers()).thenReturn(Lists.newArrayList(bguerout, stnevex));
        when(fetchingTaskFactory.createTasks(bguerout)).thenReturn(bgueroutTasks);
        when(fetchingTaskFactory.createTasks(stnevex)).thenReturn(stnevexTasks);

        initializer.registerAllTasks();

        verify(userRepository).getAllUsers();
        verify(registrar).registerTasks(bgueroutTasks);
        verify(registrar).registerTasks(stnevexTasks);

    }


}
