package fr.xevents.core.fetcher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fr.xevents.core.EventRepository;
import fr.xevents.core.User;

public class TaskFactoryTest {

    private TaskFactory factory;
    private EventRepository eventRepository;
    private Fetcher fetcher;
    private List<Fetcher> fetchers;
    private final User user = new User("user");

    @Before
    public void initBeforeTest() throws Exception {

        fetchers = new ArrayList<Fetcher>();
        fetcher = mock(Fetcher.class);
        fetchers.add(fetcher);
        FetcherResolver fetcherResolver = mock(FetcherResolver.class);
        when(fetcherResolver.resolve(user)).thenReturn(fetchers);

        eventRepository = mock(EventRepository.class);
        factory = new TaskFactory(eventRepository, fetcherResolver);

    }

    @Test
    public void shouldCreateTaskWithUser() throws Exception {

        List<EventUpdateTask> tasks = factory.createTasks(user);

        assertThat(tasks, notNullValue());
        assertThat(tasks.size(), equalTo(1));
    }

    @Test
    public void shouldCreateTasksWithUser() throws Exception {

        Fetcher fetcher2 = mock(Fetcher.class);
        when(fetcher2.canFetch(any(User.class))).thenReturn(true);
        fetchers.add(fetcher2);

        List<EventUpdateTask> tasks = factory.createTasks(user);

        assertThat(tasks, notNullValue());
        assertThat(tasks.size(), equalTo(2));
    }

}
