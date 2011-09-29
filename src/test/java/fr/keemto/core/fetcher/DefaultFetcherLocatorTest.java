package fr.keemto.core.fetcher;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class DefaultFetcherLocatorTest {


    @Test
    public void whenFetcherCannotBeFoundShouldThrowEx() throws Exception {

        List<Fetcher> emptyFetcherList = new ArrayList<Fetcher>();
        FetcherLocator fetcherLocator = new DefaultFetcherLocator(emptyFetcherList);

        try {
            fetcherLocator.getFetcher("myProvider");
            Assert.fail();
        } catch (FetcherConfigurationException e) {
            //success
        }
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
}
