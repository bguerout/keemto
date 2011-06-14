package fr.xevents.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/META-INF/spring/applicationContext.xml" })
public class JdbcEventRepositoryIT {

    @Autowired
    private EventRepository repository;

    @Test
    public void shouldReturnANonNullEventsList() throws Exception {
        List<Event> events = repository.getAllEvents();

        assertThat(events, notNullValue());
    }

    @Test
    public void whenEventsExistShouldReturnAllEvents() throws Exception {

        List<Event> events = repository.getAllEvents();
        assertThat(events, hasItem(new Event(1, "tester", "eventTest")));
    }

    @Test
    public void shouldReturnMostRecentEventForUser() {
        Event mostRecentEvent = repository.getMostRecentEvent(new User("stnevex"));

        assertThat(mostRecentEvent, notNullValue());
        assertThat(mostRecentEvent.getUser(), equalTo("stnevex"));
        assertThat(mostRecentEvent.getTimestamp(), equalTo(new Long(1301464284376L)));
    }

    @Test
    public void whenUserHasntEventShouldReturnAnInitEvent() {
        Event mostRecentEvent = repository.getMostRecentEvent(new User("userWithoutEvents"));

        assertThat(mostRecentEvent, notNullValue());
        assertThat(mostRecentEvent.getUser(), equalTo("userWithoutEvents"));
        assertThat(mostRecentEvent.getTimestamp(), equalTo((long) 0));
    }

    @Test
    public void shouldPersitEvents() throws Exception {
        Event event = new Event(System.currentTimeMillis(), "owner", "message");
        Event event2 = new Event(System.currentTimeMillis() + 100, "owner", "message");
        repository.persist(Lists.newArrayList(event, event2));

        List<Event> allEvents = repository.getAllEvents();

        assertThat(allEvents, hasItem(event));
        assertThat(allEvents, hasItem(event2));
    }
}
