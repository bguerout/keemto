package fr.xevents.core.fetcher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.xevents.core.Event;
import fr.xevents.core.User;
import fr.xevents.core.fetcher.TwitterFetcher;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/META-INF/spring/applicationContext.xml" })
public class FetchingTweetsIT {

    @Inject
    private TwitterFetcher fetcher;

    @Test
    public void fetchTweets() {
        // given
        User user = new User("stnevex");

        // when
        List<Event> events = fetcher.fetch(user, 0);

        // then
        assertThat(events.size(), greaterThan(0));
        for (Event event : events) {
            assertThat(event.getUser(), equalTo("stnevex"));
        }
    }

}
