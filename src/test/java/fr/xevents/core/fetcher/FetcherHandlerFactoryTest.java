package fr.xevents.core.fetcher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import fr.xevents.core.EventRepository;
import fr.xevents.core.User;

public class FetcherHandlerFactoryTest {

    private FetcherHandlerFactory factory;
    private EventRepository eventRepository;

    @Before
    public void initBeforeTest() throws Exception {
        eventRepository = mock(EventRepository.class);
        factory = new FetcherHandlerFactory(eventRepository);
    }

    @Test
    public void shouldCreateANonNullFetcher() throws Exception {
        Fetcher<?> fetcher = mock(Fetcher.class);
        User user = new User("username");

        FetcherHandler handler = factory.createHandler(fetcher, user);

        assertThat(handler, notNullValue());
    }

    @Test
    public void shouldCreateFetcher() throws Exception {
        Fetcher<?> fetcher = mock(Fetcher.class);
        User user = new User("username");

        FetcherHandler handler = factory.createHandler(fetcher, user);

        assertThat(handler, notNullValue());
    }

}
