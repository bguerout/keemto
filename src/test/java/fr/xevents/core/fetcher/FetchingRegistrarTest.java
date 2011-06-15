package fr.xevents.core.fetcher;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import fr.xevents.core.User;
import fr.xevents.core.UserResolver;

public class FetchingRegistrarTest {

    private FetchingRegistrar registrar;
    private FetcherHandlerFactory factory;
    private UserResolver userResolver;

    @Before
    public void initBeforeTest() throws Exception {
        factory = mock(FetcherHandlerFactory.class);
        userResolver = mock(UserResolver.class);
        registrar = new FetchingRegistrar(factory, userResolver);
    }

    @Test
    public void shouldCreateHandlersForUser() throws Exception {
        User user = new User("bguerout");
        FetcherHandler handler = mock(FetcherHandler.class);
        when(userResolver.getAllUsers()).thenReturn(Lists.newArrayList(user));
        when(factory.createHandlers(user)).thenReturn(Lists.newArrayList(handler));
        when(handler.getDelay()).thenReturn((long) 50);

        registrar.afterPropertiesSet();

        verify(factory).createHandlers(user);
    }

    @Test
    public void shouldAutomaticallyAddCreatedHandlerToTasksMap() throws Exception {
        User user = new User("bguerout");
        FetcherHandler handler = mock(FetcherHandler.class);
        when(userResolver.getAllUsers()).thenReturn(Lists.newArrayList(user));
        when(factory.createHandlers(user)).thenReturn(Lists.newArrayList(handler));
        when(handler.getDelay()).thenReturn((long) 50);

        registrar.afterPropertiesSet();

        Map<Runnable, Long> fixedDelayTasks = registrar.getFixedDelayTasks();
        assertThat(fixedDelayTasks.size(), equalTo(1));
        assertThat(fixedDelayTasks.get(handler), equalTo((long) 50));
    }

    @Test
    public void shouldStartRegistrarTaskCron() throws Exception {
        User user = new User("bguerout");
        CountDownLatch latch = new CountDownLatch(10);
        FetcherHandler handler = new CountDownHandler(latch);
        when(userResolver.getAllUsers()).thenReturn(Lists.newArrayList(user));
        when(factory.createHandlers(user)).thenReturn(Lists.newArrayList(handler));

        registrar.afterPropertiesSet();
        latch.await(2000, TimeUnit.MILLISECONDS);

        assertThat(latch.getCount(), equalTo((long) 0));
    }

    public class CountDownHandler extends FetcherHandler {

        private final CountDownLatch latch;

        public CountDownHandler(CountDownLatch latch) {
            super(null, null, null);
            this.latch = latch;
        }

        @Override
        public long getDelay() {
            return 100;
        }

        @Override
        public void run() {
            latch.countDown();
        }
    }

}
