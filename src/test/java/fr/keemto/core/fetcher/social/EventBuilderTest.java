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

import fr.keemto.core.DefaultProviderConnection;
import fr.keemto.core.Event;
import fr.keemto.core.User;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class EventBuilderTest {
    @Test
    public void shouldCreateEventWithEmptyMessageAndCurrentTime() throws Exception {

        DefaultProviderConnection providerConnection = new DefaultProviderConnection("provider");
        User user = new User("user");

        Event event = new EventBuilder(user, providerConnection).build();

        assertThat(event.getMessage(), equalTo(""));
        assertThat(event.getTimestamp(), lessThanOrEqualTo(System.currentTimeMillis()));
    }
}
