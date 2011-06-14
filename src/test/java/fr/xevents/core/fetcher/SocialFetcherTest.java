package fr.xevents.core.fetcher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import fr.xevents.core.Event;
import fr.xevents.core.User;

public class SocialFetcherTest {

    private StringFetcher fetcher;
    private ApiResolver<String> apiResolver;
    private User user;
    private long since;

    @Before
    public void initBeforeTest() throws Exception {
        initMocks(this);

        apiResolver = mock(ApiResolver.class);
        fetcher = new StringFetcher(apiResolver);
        user = new User("bguerout");
        since = System.currentTimeMillis();

        when(apiResolver.getApis(eq(user))).thenReturn(Lists.newArrayList("string-api"));
    }

    private class StringFetcher extends SocialFetcher<String> {

        public StringFetcher(ApiResolver<String> apiResolver) {
            super(apiResolver);
        }

        @Override
        public String getProviderId() {
            return "provider";
        }

        @Override
        public long getDelay() {
            return 0;
        }

        @Override
        protected List<Event> fetchApiEvents(String api, long lastFetchedEventTime) {
            return Lists.newArrayList(new Event(1, user.getUsername(), api.toString()));
        }

    }

    @Test
    public void whenNoEventCanBeFoundThenANonNullListMustBeReturned() {
        // when
        List<Event> events = fetcher.fetch(user, since);

        // then
        assertThat(events, notNullValue());
    }

    @Test
    public void shouldConvertAllFetchedItemsToEvents() {

        // when
        List<Event> events = fetcher.fetch(user, since);

        // then
        assertThat(events.size(), greaterThan(0));
        Event event = events.get(0);
        assertThat(event.getUser(), equalTo("bguerout"));
        assertThat(event.getMessage(), equalTo("string-api"));
    }

    @Test
    public void shouldFetchAllUserApis() {

        // given
        StringFetcher fetcherWithManyApis = new StringFetcher(apiResolver);
        ArrayList<String> multipleApis = Lists.newArrayList("string-api", "another-api");
        when(apiResolver.getApis(eq(user))).thenReturn(multipleApis);

        // when
        List<Event> events = fetcherWithManyApis.fetch(user, since);

        // then
        assertThat(events.size(), equalTo(2));
        assertThat(events.get(0).getMessage(), equalTo("string-api"));
        assertThat(events.get(1).getMessage(), equalTo("another-api"));
    }

}
