package fr.xevents.core;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataRetrievalFailureException;

import com.google.common.collect.Lists;

public class EventSynchronizerTest {

    private EventSynchronizer eventSynchronizer;
    private List<Fetcher<?>> fetchers;
    private Fetcher<?> fetcher;
    private String providerId;
    private EventRepository eventRepository;
    private Event mostRecentEvent;

    @Before
    public void initBeforeTest() throws Exception {
        fetcher = mock(Fetcher.class);
        eventRepository = mock(EventRepository.class);

        fetchers = new ArrayList<Fetcher<?>>();
        fetchers.add(fetcher);
        providerId = "valid-provider-id";
        mostRecentEvent = new Event(9999, "owner", "message");
        eventSynchronizer = new EventSynchronizer(fetchers, eventRepository);

        when(fetcher.getProviderId()).thenReturn(providerId);
        when(eventRepository.getMostRecentEvent(any(User.class))).thenReturn(mostRecentEvent);
    }

    @Test
    public void shouldFetchEvents() throws Exception {

        User user = new User("bguerout");
        List<User> users = Lists.newArrayList(user);

        eventSynchronizer.updateEvents(users, providerId);

        verify(fetcher).fetch(eq(user), anyLong());
    }

    @Test
    public void shouldIgnoreFetchersWithInvalidProviderId() throws Exception {

        Fetcher<?> invalidFetcher = mock(Fetcher.class);
        fetchers.add(invalidFetcher);
        User user = new User("bguerout");
        List<User> users = Lists.newArrayList(user);
        when(invalidFetcher.getProviderId()).thenReturn("invalid-provider-id");

        eventSynchronizer.updateEvents(users, "valid-provider-id");

        verify(invalidFetcher, never()).fetch(eq(user), anyLong());
    }

    @Test
    public void shouldFetchEventsForAllUsers() throws Exception {
        User user = new User("bguerout");
        User user2 = new User("username");
        List<User> users = Lists.newArrayList(user, user2);

        eventSynchronizer.updateEvents(users, providerId);

        verify(fetcher).fetch(eq(user), anyLong());
        verify(fetcher).fetch(eq(user2), anyLong());
    }

    @Test
    public void shouldFetchAllFetchers() throws Exception {
        Fetcher<?> anotherFetcher = mock(Fetcher.class);
        fetchers.add(anotherFetcher);
        User user = new User("bguerout");
        List<User> users = Lists.newArrayList(user);
        when(anotherFetcher.getProviderId()).thenReturn(providerId);

        eventSynchronizer.updateEvents(users, providerId);

        verify(fetcher).fetch(eq(user), anyLong());
        verify(anotherFetcher).fetch(eq(user), anyLong());
    }

    @Test
    public void shouldUseEventRepositoryToObtainLastFetchedEventTime() {
        User user = new User("bguerout");
        List<User> users = Lists.newArrayList(user);

        eventSynchronizer.updateEvents(users, providerId);

        verify(eventRepository).getMostRecentEvent(user);
        verify(fetcher).fetch(eq(user), eq(mostRecentEvent.getTimestamp()));
    }

    @Test
    public void shouldPersitFetchedEvents() {
        User user = new User("bguerout");
        List<User> users = Lists.newArrayList(user);
        Event fetchedEvent = new Event(System.currentTimeMillis(), "bguerout", "message");
        ArrayList<Event> events = Lists.newArrayList(fetchedEvent);
        when(fetcher.fetch(eq(user), anyLong())).thenReturn(events);

        eventSynchronizer.updateEvents(users, providerId);

        verify(eventRepository).persist(events);
    }

    @Test
    public void whenFetcherThrowsAnExceptionShouldHandleIt() throws Exception {
        User user = new User("bguerout");
        List<User> users = Lists.newArrayList(user);
        when(fetcher.fetch(eq(user), anyLong())).thenThrow(new FetchingException());

        try {
            eventSynchronizer.updateEvents(users, providerId);
        } catch (Exception e) {
            fail("Synchronizer must be able to handle a fetcher in error.");
        }
    }

    @Test
    public void whenEventRepositoryThrowsAnExceptionShouldHandleIt() throws Exception {
        User user = new User("bguerout");
        List<User> users = Lists.newArrayList(user);
        doThrow(new DataRetrievalFailureException("")).when(eventRepository).persist(anyList());

        try {
            eventSynchronizer.updateEvents(users, providerId);
        } catch (Exception e) {
            fail("Synchronizer must be able to handle a fetcher in error.");
        }
    }
}
