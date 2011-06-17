package fr.xevents.core.fetcher;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataRetrievalFailureException;

import com.google.common.collect.Lists;

import fr.xevents.core.Event;
import fr.xevents.core.EventRepository;
import fr.xevents.core.User;

public class FetcherHandlerTest {

    private FetcherHandler handler;
    private EventRepository eventRepository;
    private Fetcher<?> fetcher;
    private User user;
    private final Event mostRecentEvent = new Event(9999, "user", "message");

    @Before
    public void initBeforeTest() throws Exception {
        fetcher = mock(Fetcher.class);
        eventRepository = mock(EventRepository.class);
        user = new User("user");
        handler = new FetcherHandler(fetcher, user, eventRepository);

        when(eventRepository.getMostRecentEvent(any(User.class))).thenReturn(mostRecentEvent);
    }

    @Test
    public void shouldUseEventRepositoryToObtainLastFetchedEventTime() {

        handler.run();

        verify(eventRepository).getMostRecentEvent(user);
        verify(fetcher).fetch(eq(user), eq(mostRecentEvent.getTimestamp()));
    }

    @Test
    public void shouldPersitFetchedEvents() {
        Event fetchedEvent = new Event(System.currentTimeMillis(), "bguerout", "message");
        ArrayList<Event> events = Lists.newArrayList(fetchedEvent);
        when(fetcher.fetch(eq(user), anyLong())).thenReturn(events);

        handler.run();

        verify(eventRepository).persist(events);
    }

    @Test(expected = FetchingException.class)
    public void whenFetcherFailsShouldThrowException() throws Exception {

        when(fetcher.fetch(eq(user), anyLong())).thenThrow(new RuntimeException());

        handler.run();

    }

    @Test(expected = FetchingException.class)
    public void whenEventRepositoryFailsShouldThrowException() throws Exception {

        doThrow(new DataRetrievalFailureException("")).when(eventRepository).persist(anyList());

        handler.run();
    }

}