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

import com.google.common.collect.Lists;
import fr.keemto.core.User;
import fr.keemto.core.UserResolver;
import fr.keemto.core.fetcher.Fetcher;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class AutomaticFetchingInitializerTest {

    private AutomaticFetchingInitializer initializer;
    private UserResolver userResolver;
    private FetchingTaskFactory fetchingTaskFactory;
    private TaskRegistrar registrar;
    private List<Fetcher> fetchers;

    @Before
    public void initBeforeTest() throws Exception {
        initMocks(this);
        userResolver = mock(UserResolver.class);
        fetchingTaskFactory = mock(FetchingTaskFactory.class);
        registrar = mock(TaskRegistrar.class);
        initializer = new AutomaticFetchingInitializer();
        initializer.setRegistrar(registrar);
        initializer.setEventTaskFactory(fetchingTaskFactory);
        initializer.setUserResolver(userResolver);
    }

    @Test
    public void shouldRegisterTask() throws Exception {
        User user = new User("bguerout");

        ArrayList<FetchingTask> tasks = Lists.newArrayList(mock(FetchingTask.class));
        when(fetchingTaskFactory.createTasks(user)).thenReturn(tasks);
        when(userResolver.getAllUsers()).thenReturn(Lists.newArrayList(user));

        initializer.registerAllTasks();

        verify(registrar).registerTasks(tasks);

    }

    @Test
    public void shouldRegisterTaskForAllUsers() throws Exception {
        User bguerout = new User("bguerout");
        User stnevex = new User("stnevex");
        ArrayList<FetchingTask> bgueroutTasks = Lists.newArrayList(mock(FetchingTask.class));
        ArrayList<FetchingTask> stnevexTasks = Lists.newArrayList(mock(FetchingTask.class));
        when(userResolver.getAllUsers()).thenReturn(Lists.newArrayList(bguerout, stnevex));
        when(fetchingTaskFactory.createTasks(bguerout)).thenReturn(bgueroutTasks);
        when(fetchingTaskFactory.createTasks(stnevex)).thenReturn(stnevexTasks);

        initializer.registerAllTasks();

        verify(userResolver).getAllUsers();
        verify(registrar).registerTasks(bgueroutTasks);
        verify(registrar).registerTasks(stnevexTasks);

    }


}
