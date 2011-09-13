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

package fr.keemto.core.fetcher.social;

import com.google.common.collect.Lists;
import fr.keemto.core.Event;
import fr.keemto.core.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class SocialFetcherTest {

    private StringFetcher fetcher;
    private ProviderResolver<String> providerResolver;
    private User user;
    private long since;

    @Before
    public void initBeforeTest() throws Exception {
        initMocks(this);

        providerResolver = mock(ProviderResolver.class);
        fetcher = new StringFetcher(providerResolver);
        user = new User("bguerout");
        since = System.currentTimeMillis();
    }

    @Test
    public void whenNoEventCanBeFoundThenANonNullListMustBeReturned() {

        when(providerResolver.getConnectionsFor(eq(user))).thenReturn(new ArrayList<Connection<String>>());

        List<Event> events = fetcher.fetch(user, since);

        assertThat(events, notNullValue());
    }

    @Test
    public void shouldConvertAllFetchedItemsToEvents() {

        List<Connection<String>> connections = new ArrayList<Connection<String>>();
        connections.add(mockConnectionWithApi("string-api"));
        when(providerResolver.getConnectionsFor(eq(user))).thenReturn(connections);

        // when
        List<Event> events = fetcher.fetch(user, since);

        // then
        assertThat(events.size(), greaterThan(0));
        Event event = events.get(0);
        assertThat(event.getUser(), equalTo(user));
        assertThat(event.getMessage(), equalTo("string-api"));
    }

    @Test
    public void shouldFetchAllUserApis() {

        // given
        StringFetcher fetcherWithManyApis = new StringFetcher(providerResolver);
        List<Connection<String>> multipleConnections = new ArrayList<Connection<String>>();
        multipleConnections.add(mockConnectionWithApi("string-api"));
        multipleConnections.add(mockConnectionWithApi("another-api"));
        when(providerResolver.getConnectionsFor(eq(user))).thenReturn(multipleConnections);

        // when
        List<Event> events = fetcherWithManyApis.fetch(user, since);

        // then
        assertThat(events.size(), equalTo(2));
        assertThat(events.get(0).getMessage(), equalTo("string-api"));
        assertThat(events.get(1).getMessage(), equalTo("another-api"));
    }

    @Test
    public void shouldCheckIfUserCanBeFetched() throws Exception {

        List<Connection<String>> connections = mock(List.class);
        when(connections.isEmpty()).thenReturn(false);
        when(providerResolver.getConnectionsFor(eq(user))).thenReturn(connections);

        boolean canFetch = fetcher.canFetch(user);

        assertThat(canFetch, is(true));
        verify(providerResolver).getConnectionsFor(user);
    }

    private Connection mockConnectionWithApi(String api) {
        Connection connection = mock(Connection.class);
        when(connection.getApi()).thenReturn(api);
        ConnectionKey key = new ConnectionKey("providerId", "providerUserIdd");
        when(connection.getKey()).thenReturn(key);
        return connection;
    }


    private class StringFetcher extends SocialFetcher<String, String> {

        public StringFetcher(ProviderResolver<String> providerResolver) {
            super(providerResolver, 60000);
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
        protected List<String> fetchApi(String api, long lastFetchedEventTime) {
            return Lists.newArrayList(api.toString());
        }

        @Override
        protected Event convertDataToEvent(String data, EventBuilder builder) {
            return builder.message(data).build();
        }
    }

}
