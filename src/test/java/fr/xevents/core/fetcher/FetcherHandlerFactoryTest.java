package fr.xevents.core.fetcher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import fr.xevents.core.EventRepository;
import fr.xevents.core.User;

public class FetcherHandlerFactoryTest {

    private FetcherHandlerFactory factory;
    private EventRepository eventRepository;
    private Fetcher<?> fetcher;

    @Before
    public void initBeforeTest() throws Exception {
        eventRepository = mock(EventRepository.class);
        List<Fetcher<?>> fetchers = new ArrayList<Fetcher<?>>();
        fetcher = mock(Fetcher.class);
        fetchers.add(fetcher);
        factory = new FetcherHandlerFactory(eventRepository, fetchers);

        when(fetcher.canFetch(any(User.class))).thenReturn(true);
    }

    @Test
    public void shouldCreateHandlerWithUser() throws Exception {

        User user = new User("user");

        List<FetcherHandler> handlers = factory.createHandlers(user);

        assertThat(handlers, notNullValue());
        assertThat(handlers.size(), equalTo(1));
    }

    @Test
    public void shouldCreateHandlersWithUser() throws Exception {
        User user = new User("user");
        Fetcher<?> fetcher2 = mock(Fetcher.class);
        when(fetcher2.canFetch(any(User.class))).thenReturn(true);
        List<Fetcher<?>> fetchers = Lists.newArrayList(fetcher, fetcher2);
        factory = new FetcherHandlerFactory(eventRepository, fetchers);

        List<FetcherHandler> handlers = factory.createHandlers(user);

        assertThat(handlers, notNullValue());
        assertThat(handlers.size(), equalTo(2));
    }

    @Test
    public void shouldIgnoreFetcherThatDoesNotSupportUser() throws Exception {
        User user = new User("user");

        when(fetcher.canFetch(user)).thenReturn(false);

        List<FetcherHandler> handlers = factory.createHandlers(user);

        assertThat(handlers.isEmpty(), is(true));
    }

}
