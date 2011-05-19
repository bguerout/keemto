package fr.xevents;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/META-INF/spring/applicationContext.xml" })
public class JdbcEventRepositoryTest {

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

}
