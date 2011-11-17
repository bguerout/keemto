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

package fr.keemto.provider.twitter;

import fr.keemto.core.User;
import fr.keemto.provider.social.EventData;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/META-INF/spring/core-config.xml"})
public class FetchingTweetsIT {

    @Inject
    private TwitterFetcher fetcher;

    @Inject
    UsersConnectionRepository usersConnectionRepository;

    @Test(timeout = 3000)
    @Ignore
    public void fetchTweets() {
        User user = new User("stnevex");

        Connection<Twitter> connx = usersConnectionRepository.createConnectionRepository("stnevex").findPrimaryConnection(Twitter.class);

        List<EventData> datas = fetcher.fetch(connx, 0);

        assertThat(datas.size(), greaterThan(0));

    }

}
