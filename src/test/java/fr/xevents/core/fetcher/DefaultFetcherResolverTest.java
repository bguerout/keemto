package fr.xevents.core.fetcher;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import fr.xevents.core.User;

public class DefaultFetcherResolverTest {

    private User user;
    private FetcherResolver resolver;

    @Before
    public void initBeforeTest() throws Exception {
        user = new User("username");
    }

    @Test
    public void shouldResolveANonNullFetcherList() throws Exception {

        List<Fetcher> fetchers = Lists.newArrayList();
        FetcherResolver resolver = new DefaultFetcherResolver(fetchers);

        List<Fetcher> result = resolver.resolve(user);

        assertThat(result, notNullValue());

    }

    @Test
    public void shouldResolveAllFetchers() throws Exception {

        List<Fetcher> fetchers = Lists.newArrayList();
        FetcherResolver resolver = new DefaultFetcherResolver(fetchers);

        List<Fetcher> result = resolver.resolveAll();

        assertThat(fetchers.size(), equalTo(result.size()));

    }

    @Test
    public void shouldResolveFetcherForUser() throws Exception {

        Fetcher fetcher = mock(Fetcher.class);
        List<Fetcher> fetchers = Lists.newArrayList(fetcher);
        resolver = new DefaultFetcherResolver(fetchers);
        when(fetcher.canFetch(user)).thenReturn(true);

        List<Fetcher> fetcherForUser = resolver.resolve(user);

        assertThat(fetcherForUser, notNullValue());
        assertThat(fetcherForUser.get(0), equalTo(fetcher));
        assertThat(fetcherForUser.get(0).canFetch(user), is(true));
    }

    @Test
    public void shouldRejectFetcherWhichCannotHandleUser() throws Exception {

        Fetcher invalidFetcher = mock(Fetcher.class);
        List<Fetcher> fetchers = Lists.newArrayList(invalidFetcher);
        resolver = new DefaultFetcherResolver(fetchers);
        when(invalidFetcher.canFetch(user)).thenReturn(false);

        List<Fetcher> fetcherForUser = resolver.resolve(user);

        assertThat(fetcherForUser.isEmpty(), is(true));
    }

    @Test
    public void shouldHandleManyFetchers() throws Exception {

        Fetcher fetcher = mock(Fetcher.class);
        Fetcher invalidFetcher = mock(Fetcher.class);
        List<Fetcher> fetchers = Lists.newArrayList(fetcher, invalidFetcher);
        resolver = new DefaultFetcherResolver(fetchers);
        when(fetcher.canFetch(user)).thenReturn(true);
        when(invalidFetcher.canFetch(user)).thenReturn(false);

        List<Fetcher> fetcherForUser = resolver.resolve(user);

        assertThat(fetcherForUser.size(), equalTo(1));

    }
}
