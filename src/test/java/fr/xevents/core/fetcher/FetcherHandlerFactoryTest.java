package fr.xevents.core.fetcher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import fr.xevents.core.EventRepository;
import fr.xevents.core.User;
import fr.xevents.core.UserResolver;

public class FetcherHandlerFactoryTest {

    private FetcherHandlerFactory factory;
    private EventRepository eventRepository;
    private UserResolver userResolver;

    @Before
    public void initBeforeTest() throws Exception {
        eventRepository = mock(EventRepository.class);
        userResolver = mock(UserResolver.class);
        factory = new FetcherHandlerFactory(eventRepository, userResolver);
    }

    @Test
    public void shouldCreateANonNullFetcher() throws Exception {
        Fetcher<?> fetcher = mock(Fetcher.class);
        User user = new User("username");

        FetcherHandler handler = factory.createHandler(fetcher, user);

        assertThat(handler, notNullValue());
    }

    @Test
    public void shouldCreateHandler() throws Exception {
        Fetcher<?> fetcher = mock(Fetcher.class);
        User user = new User("username");

        FetcherHandler handler = factory.createHandler(fetcher, user);

        assertThat(handler, notNullValue());
    }

    @Test
    public void shouldCreateHandlers() throws Exception {
        Fetcher<?> fetcher = mock(Fetcher.class);
        Fetcher<?> fetcher2 = mock(Fetcher.class);
        when(userResolver.getAllUsers()).thenReturn(Lists.newArrayList(new User("user")));

        List<FetcherHandler> handlers = factory.createHandlers(Lists.newArrayList(fetcher, fetcher2));

        assertThat(handlers, notNullValue());
        assertThat(handlers.size(), equalTo(2));
    }

    @Test
    public void shouldResolveUserToCreateHandlers() throws Exception {
        Fetcher<?> fetcher = mock(Fetcher.class);
        Fetcher<?> fetcher2 = mock(Fetcher.class);
        when(userResolver.getAllUsers()).thenReturn(Lists.newArrayList(new User("user")));

        factory.createHandlers(Lists.newArrayList(fetcher, fetcher2));

        verify(userResolver).getAllUsers();
    }

}
