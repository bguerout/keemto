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
import fr.keemto.core.Event;
import fr.keemto.core.User;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

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
            super(apiResolver, 60000);
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
        protected List<Event> fetchApiEvents(String api, long lastFetchedEventTime, User user) {
            return Lists.newArrayList(new Event(1, user.getUsername(), api.toString(), "social"));
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

    @Test
    public void shouldCheckIfUserCanBeFetched() throws Exception {

        boolean canFetch = fetcher.canFetch(user);

        assertThat(canFetch, is(true));
        verify(apiResolver).getApis(user);
    }

}
