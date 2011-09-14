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

package fr.keemto.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/META-INF/spring/applicationContext.xml"})
public class JdbcUserRepositoryIT {

    @Inject
    private UserRepository repository;

    @Test
    public void shouldReturnAllUsers() throws Exception {

        List<User> users = repository.getAllUsers();

        assertThat(users, notNullValue());
        assertThat(users.size(), equalTo(1));
    }

    @Test
    public void shouldMapRowToUser() throws Exception {

        List<User> users = repository.getAllUsers();

        assertThat(users.size(), greaterThan(0));
        User user = users.get(0);
        assertThat(user.getUsername(), equalTo("stnevex"));
        assertThat(user.getFirstName(), equalTo("John"));
        assertThat(user.getLastName(), equalTo("Doe"));
        assertThat(user.getEmail(), equalTo("stnevex@gmail.com"));

    }

}
