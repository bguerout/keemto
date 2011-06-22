package fr.xevents.core.fetcher;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import fr.xevents.core.User;
import fr.xevents.core.UserResolver;

public class AutomaticFetchingInitializerTest {

    private AutomaticFetchingInitializer initializer;
    private UserResolver userResolver;
    private TaskFactory taskFactory;
    private FetchingRegistrar registrar;
    private List<Fetcher> fetchers;

    @Before
    public void initBeforeTest() throws Exception {
        initMocks(this);
        userResolver = mock(UserResolver.class);
        taskFactory = mock(TaskFactory.class);
        registrar = mock(FetchingRegistrar.class);
        initializer = new AutomaticFetchingInitializer();
        initializer.setRegistrar(registrar);
        initializer.setEventTaskFactory(taskFactory);
        initializer.setUserResolver(userResolver);

        fetchers = new ArrayList<Fetcher>();
        fetchers.add(mock(Fetcher.class));
    }

    @Test
    public void shouldRegisterTask() throws Exception {
        User user = new User("bguerout");

        ArrayList<EventTask> tasks = Lists.newArrayList(mock(EventTask.class));
        when(taskFactory.createTasks(user)).thenReturn(tasks);
        when(userResolver.getAllUsers()).thenReturn(Lists.newArrayList(user));

        initializer.registerAllTasks(fetchers);

        verify(registrar).registerTasks(tasks);

    }

    @Test
    public void shouldRegisterTaskForAllUsers() throws Exception {
        User bguerout = new User("bguerout");
        User stnevex = new User("stnevex");
        ArrayList<EventTask> bgueroutTasks = Lists.newArrayList(mock(EventTask.class));
        ArrayList<EventTask> stnevexTasks = Lists.newArrayList(mock(EventTask.class));
        when(userResolver.getAllUsers()).thenReturn(Lists.newArrayList(bguerout, stnevex));
        when(taskFactory.createTasks(bguerout)).thenReturn(bgueroutTasks);
        when(taskFactory.createTasks(stnevex)).thenReturn(stnevexTasks);

        initializer.registerAllTasks(fetchers);

        verify(userResolver).getAllUsers();
        verify(registrar).registerTasks(bgueroutTasks);
        verify(registrar).registerTasks(stnevexTasks);

    }

    @Test
    public void shouldResolveFetcherWithResolver() throws Exception {

        FetcherResolver resolver = mock(FetcherResolver.class);
        initializer.setFetcherResolver(resolver);
        when(resolver.resolveAll()).thenReturn(fetchers);

        initializer.afterPropertiesSet();

        verify(resolver).resolveAll();
    }

}
