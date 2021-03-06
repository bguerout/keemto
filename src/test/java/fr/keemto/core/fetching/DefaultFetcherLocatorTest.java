package fr.keemto.core.fetching;

import com.google.common.collect.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class DefaultFetcherLocatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Test
    public void whenFetcherCannotBeFoundShouldThrowEx() throws Exception {

        thrown.expect(FetcherConfigurationException.class);
        List<Fetcher> emptyFetcherList = new ArrayList<Fetcher>();
        FetcherLocator fetcherLocator = new DefaultFetcherLocator(emptyFetcherList);

        fetcherLocator.getFetcher("myProvider");
    }

    @Test
    public void shouldReturnFetcherForSpecificProvider() throws Exception {
        Fetcher fetcher = mock(Fetcher.class);
        FetcherLocator fetcherLocator = new DefaultFetcherLocator(Lists.newArrayList(fetcher));
        when(fetcher.getProviderId()).thenReturn("twitter");

        Fetcher twitterFetcher = fetcherLocator.getFetcher("twitter");

        assertThat(twitterFetcher, notNullValue());
        assertThat(twitterFetcher, equalTo(fetcher));

    }

    @Test
    public void canCheckIfProviderHasFetcher() throws Exception {
        Fetcher fetcher = mock(Fetcher.class);
        FetcherLocator fetcherLocator = new DefaultFetcherLocator(Lists.newArrayList(fetcher));
        when(fetcher.getProviderId()).thenReturn("twitter");

        assertThat(fetcherLocator.hasFetcherFor("twitter"), is(true));
        assertThat(fetcherLocator.hasFetcherFor("unknown"), is(false));

    }
}
