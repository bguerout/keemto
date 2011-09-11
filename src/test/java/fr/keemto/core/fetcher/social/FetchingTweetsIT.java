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

import fr.keemto.core.Event;
import fr.keemto.core.User;
import fr.keemto.core.fetcher.social.TwitterFetcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/META-INF/spring/applicationContext.xml"})
public class FetchingTweetsIT {

    @Inject
    private TwitterFetcher fetcher;

    @Test(timeout = 3000)
    public void fetchTweets() {
        User user = new User("stnevex");

        List<Event> events = fetcher.fetch(user, 0);

        assertThat(events.size(), greaterThan(0));
        for (Event event : events) {
            assertThat(event.getUser(), equalTo("stnevex"));
        }
    }

}
