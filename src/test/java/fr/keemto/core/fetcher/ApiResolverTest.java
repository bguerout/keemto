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
import org.mockito.Mock;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.twitter.api.Twitter;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ApiResolverTest {

    private ApiResolver<Twitter> resolver;

    @Mock
    private UsersConnectionRepository usersConnectionRepository;

    @Mock
    private Twitter api;

    @Mock
    private ConnectionRepository connRepository;

    @Before
    public void initBeforeTest() throws Exception {
        initMocks(this);

        resolver = new ApiResolver<Twitter>(Twitter.class, usersConnectionRepository);

        when(usersConnectionRepository.createConnectionRepository("bguerout")).thenReturn(connRepository);

    }

    @Test
    public void whenNoApiExistsThenShouldReturnAnEmptyList() throws Exception {

        List<Twitter> apis = resolver.getApis(new User("bguerout"));

        assertThat(apis, notNullValue());
    }

    @Test
    public void shouldUseSocialRepositoryToResolveApis() {

        Connection<Twitter> twitterConnection = mock(Connection.class);
        when(connRepository.findConnections(Twitter.class)).thenReturn(Lists.newArrayList(twitterConnection));
        when(twitterConnection.getApi()).thenReturn(api);

        List<Twitter> apis = resolver.getApis(new User("bguerout"));

        assertThat(apis, hasItem(api));

    }

}
