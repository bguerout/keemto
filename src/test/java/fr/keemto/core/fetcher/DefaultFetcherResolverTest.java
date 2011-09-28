/*
 * Copyright (C) 2010 Benoit Guerout <bguerout at gmail dot com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.keemto.core.fetcher;

import com.google.common.collect.Lists;
import fr.keemto.core.User;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
