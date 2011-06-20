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
    private FetcherHandlerFactory handlerFactory;
    private FetcherRegistrar registrar;
    private List<Fetcher> fetchers;

    @Before
    public void initBeforeTest() throws Exception {
        initMocks(this);
        userResolver = mock(UserResolver.class);
        handlerFactory = mock(FetcherHandlerFactory.class);
        registrar = mock(FetcherRegistrar.class);
        initializer = new AutomaticFetchingInitializer();
        initializer.setRegistrar(registrar);
        initializer.setHandlerFactory(handlerFactory);
        initializer.setUserResolver(userResolver);

        fetchers = new ArrayList<Fetcher>();
        fetchers.add(mock(Fetcher.class));
    }

    @Test
    public void shouldRegisterHandler() throws Exception {
        User user = new User("bguerout");

        ArrayList<FetcherHandler> handlers = Lists.newArrayList(mock(FetcherHandler.class));
        when(handlerFactory.createHandlers(user)).thenReturn(handlers);
        when(userResolver.getAllUsers()).thenReturn(Lists.newArrayList(user));

        initializer.registerAllHandlers(fetchers);

        verify(registrar).registerHandlers(handlers);

    }

    @Test
    public void shouldRegisterHandlerForAllUsers() throws Exception {
        User bguerout = new User("bguerout");
        User stnevex = new User("stnevex");
        ArrayList<FetcherHandler> bgueroutHandlers = Lists.newArrayList(mock(FetcherHandler.class));
        ArrayList<FetcherHandler> stnevexHandlers = Lists.newArrayList(mock(FetcherHandler.class));
        when(userResolver.getAllUsers()).thenReturn(Lists.newArrayList(bguerout, stnevex));
        when(handlerFactory.createHandlers(bguerout)).thenReturn(bgueroutHandlers);
        when(handlerFactory.createHandlers(stnevex)).thenReturn(stnevexHandlers);

        initializer.registerAllHandlers(fetchers);

        verify(userResolver).getAllUsers();
        verify(registrar).registerHandlers(bgueroutHandlers);
        verify(registrar).registerHandlers(stnevexHandlers);

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
