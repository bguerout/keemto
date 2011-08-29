/*
 * Copyright (C) 2010 Benoit Guerout <bguerout at gmail dot com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package fr.keemto.web;

import org.junit.Test;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsonConnectionTest {


    @Test
    public void shouldCreateAnIdBasedOnConnectionKey() throws Exception {

        Connection<String> connx = mock(Connection.class);
        ConnectionKey key = new ConnectionKey("provider", "providerUserId");
        when(connx.getKey()).thenReturn(key);
        JsonConnection jconnection = new JsonConnection(connx);

        String id = jconnection.getId();

        assertThat(id, equalTo("provider-providerUserId"));
    }

    @Test
    public void shouldUseDataFromConnection() throws Exception {

        Connection<String> connx = mock(Connection.class);
        ConnectionKey key = new ConnectionKey("provider", "providerUserId");
        when(connx.getKey()).thenReturn(key);
        when(connx.getDisplayName()).thenReturn("display");
        when(connx.getImageUrl()).thenReturn("image");
        when(connx.getProfileUrl()).thenReturn("profile");
        JsonConnection jconnection = new JsonConnection(connx);

        assertThat(jconnection.getProviderId(), equalTo("provider"));
        assertThat(jconnection.getDisplayName(), equalTo("display"));
        assertThat(jconnection.getImageUrl(), equalTo("image"));
        assertThat(jconnection.getProfileUrl(), equalTo("profile"));
    }
}
