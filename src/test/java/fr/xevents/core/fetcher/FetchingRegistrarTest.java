package fr.xevents.core.fetcher;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ScheduledFuture;

import org.junit.Before;
import org.junit.Test;
import org.springframework.scheduling.TaskScheduler;

import com.google.common.collect.Lists;

import fr.xevents.core.User;

public class FetchingRegistrarTest {

    private FetchingRegistrar registrar;
    private TaskScheduler scheduler;
    private User user;

    @Before
    public void initBeforeTest() throws Exception {
        user = new User("bguerout");
        scheduler = mock(TaskScheduler.class);
        registrar = new FetchingRegistrar(scheduler);

    }

    @Test
    public void shouldAutomaticallyRegisterHandlerToScheduler() throws Exception {
        FetcherHandler handler = mock(FetcherHandler.class);
        when(handler.getUser()).thenReturn(user);

        registrar.registerHandler(handler);

        verify(scheduler).scheduleWithFixedDelay(handler, handler.getDelay());
    }

    @Test
    public void shouldRegisterAllHandlers() throws Exception {
        FetcherHandler handler = mock(FetcherHandler.class);
        when(handler.getUser()).thenReturn(user);
        FetcherHandler anotherHandler = mock(FetcherHandler.class);
        when(anotherHandler.getUser()).thenReturn(user);

        registrar.registerHandlers(Lists.newArrayList(handler, anotherHandler));

        verify(scheduler, timeout(2)).scheduleWithFixedDelay(handler, handler.getDelay());
    }

    @Test
    public void shouldCancelHandlersForUser() throws Exception {
        FetcherHandler handler = mock(FetcherHandler.class);
        when(handler.getUser()).thenReturn(user);
        ScheduledFuture<?> future = mock(ScheduledFuture.class);
        when(scheduler.scheduleWithFixedDelay(handler, handler.getDelay())).thenReturn(future);

        registrar.registerHandler(handler);
        registrar.cancelHandlers(user);

        verify(future).cancel(false);

    }

}
