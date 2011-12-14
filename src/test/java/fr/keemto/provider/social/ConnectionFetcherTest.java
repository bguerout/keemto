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

package fr.keemto.provider.social;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.springframework.social.connect.Connection;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.Mockito.*;

public class ConnectionFetcherTest {

    private long since;

    @Before
    public void initBeforeTest() throws Exception {
        since = System.currentTimeMillis();
    }

    @Test
    public void shouldDelegateFetchingToImpl() {

        ConnectionFetcher fetcher = spy(new TestConnectionFetcher());
        Connection<String> connection = mockConnectionWithApi("string-api");

        List<EventData> events = fetcher.fetch(connection, since);

        verify(fetcher).fetchApi("string-api", since);
    }

    @Test
    public void shouldDelegateEventDataCreationToImpl() {

        ConnectionFetcher fetcher = spy(new TestConnectionFetcher());
        Connection connection = mockConnectionWithApi("string-api");

        // when
        List<EventData> events = fetcher.fetch(connection, since);

        // then
        assertThat(events.size(), greaterThan(0));
        verify(fetcher, times(1)).convertDataToEvent("data");
        verify(fetcher, times(1)).convertDataToEvent("data1");
    }


    private Connection mockConnectionWithApi(String api) {
        Connection connection = mock(Connection.class);
        when(connection.getApi()).thenReturn(api);
        return connection;
    }

    private static class TestConnectionFetcher extends ConnectionFetcher<String, String> {

        private TestConnectionFetcher() {
            super(1);
        }

        @Override
        protected List<String> fetchApi(String api, long lastFetchedEventTime) {
            return Lists.newArrayList("data", "data1");
        }

        @Override
        protected EventData convertDataToEvent(String data) {
            return mock(EventData.class);
        }

        @Override
        public String getProviderId() {
            return null;
        }
    }


}
