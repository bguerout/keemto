package fr.xevents;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class SocialFetcherTest {

    private StringFetcher fetcher;
    private ApiResolver<String> apiResolver;
    private User bguerout;
    private List<User> users;

    @Before
    public void initBeforeTest() throws Exception {
        initMocks(this);

        apiResolver = mock(ApiResolver.class);
        fetcher = new StringFetcher(apiResolver);
        bguerout = new User("bguerout");
        users = Lists.newArrayList(bguerout);

        when(apiResolver.getApis(eq(bguerout))).thenReturn(Lists.newArrayList("string-api"));
    }

    private class StringFetcher extends SocialFetcher<String> {

        public StringFetcher(ApiResolver<String> apiResolver) {
            super(apiResolver);
        }

        @Override
        protected List<Event> fetchApiEvents(String api) {
            return Lists.newArrayList(new Event(1, bguerout.getUsername(), api.toString()));
        }

    }

    @Test
    public void whenNoEventCanBeFoundThenANonNullListMustBeReturned() {
        // when
        List<Event> events = fetcher.fetch(users);

        // then
        assertThat(events, notNullValue());
    }

    @Test
    public void shouldConvertAllFetchedItemsToEvents() {

        // when
        List<Event> events = fetcher.fetch(users);

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
        when(apiResolver.getApis(eq(bguerout))).thenReturn(multipleApis);

        // when
        List<Event> events = fetcherWithManyApis.fetch(users);

        // then
        assertThat(events.size(), equalTo(2));
        assertThat(events.get(0).getMessage(), equalTo("string-api"));
        assertThat(events.get(1).getMessage(), equalTo("another-api"));
    }

    @Test
    public void shouldFetchEveryAllUsers() {
        // given
        User stnevex = new User("stnevex");
        List<User> manyUsers = Lists.newArrayList(bguerout, stnevex);

        // when
        fetcher.fetch(manyUsers);

        // then
        verify(apiResolver, times(1)).getApis(bguerout);
        verify(apiResolver, times(1)).getApis(stnevex);
    }

}
