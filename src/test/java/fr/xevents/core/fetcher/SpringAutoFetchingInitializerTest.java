package fr.xevents.core.fetcher;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import fr.xevents.core.User;
import fr.xevents.core.UserResolver;

public class SpringAutoFetchingInitializerTest {

    private SpringAutoFetchingInitializer initializer;
    private UserResolver userResolver;
    private FetcherHandlerFactory handlerFactory;
    private FetchingRegistrar registrar;

    @Before
    public void initBeforeTest() throws Exception {
        initMocks(this);
        userResolver = mock(UserResolver.class);
        handlerFactory = mock(FetcherHandlerFactory.class);
        registrar = mock(FetchingRegistrar.class);
        initializer = new SpringAutoFetchingInitializer(userResolver, handlerFactory, registrar);
    }

    @Test
    public void shouldRegisterHandler() throws Exception {
        User user = new User("bguerout");
        ArrayList<FetcherHandler> handlers = Lists.newArrayList(mock(FetcherHandler.class));
        when(handlerFactory.createHandlers(user)).thenReturn(handlers);
        when(userResolver.getAllUsers()).thenReturn(Lists.newArrayList(user));

        initializer.afterPropertiesSet();

        verify(registrar).registerHandlers(handlers);

    }

    @Test
    public void shouldRegisterHandlerForAllUsers() throws Exception {
        User bguerout = new User("bguerout");
        User stnevex = new User("stnevex");
        when(userResolver.getAllUsers()).thenReturn(Lists.newArrayList(bguerout, stnevex));
        ArrayList<FetcherHandler> bgueroutHandlers = Lists.newArrayList(mock(FetcherHandler.class));
        when(handlerFactory.createHandlers(bguerout)).thenReturn(bgueroutHandlers);
        ArrayList<FetcherHandler> stnevexHandlers = Lists.newArrayList(mock(FetcherHandler.class));
        when(handlerFactory.createHandlers(stnevex)).thenReturn(stnevexHandlers);

        initializer.afterPropertiesSet();

        verify(registrar).registerHandlers(bgueroutHandlers);
        verify(registrar).registerHandlers(stnevexHandlers);

    }

}
