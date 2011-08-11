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

import fr.keemto.web.security.LoginStatus;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class LoginWebIT {

    //TODO externalize me
    public static final String URL = "http://127.0.0.1:8080/keemto/api/login.json";
    public static final String USERNAME_PARAM = "j_username";
    public static final String PASSWORD_PARAM = "j_password";
    public static final String VALID_USERNAME = "stnevex";
    private RestTemplate template;


    @Before
    public void prepare() throws Exception {
         template = new RestTemplate();
    }

    @Test
    public void shouldRejectInvalidUsername() {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add(USERNAME_PARAM, "invalid-login");
        params.add(PASSWORD_PARAM, "fake");


        LoginStatus status = template.postForObject(URL, params, LoginStatus.class);

        assertThat(status.isLoggedIn(), is(false));
        assertThat(status.getUsername(), equalTo("invalid-login"));

    }

    @Test
    public void shouldRejectValidUserWithWrongCredentials() {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add(USERNAME_PARAM, VALID_USERNAME);
        params.add(PASSWORD_PARAM, "invalid");

        LoginStatus status = template.postForObject(URL, params, LoginStatus.class);

        assertThat(status.isLoggedIn(), is(false));
        assertThat(status.getUsername(), equalTo(VALID_USERNAME));

    }

    @Test
    public void shouldAuthenticateUserWithValidCredentials() {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add(USERNAME_PARAM, VALID_USERNAME);
        params.add(PASSWORD_PARAM, "test");

        LoginStatus status = template.postForObject(URL, params, LoginStatus.class);

        assertThat(status.isLoggedIn(), is(true));
        assertThat(status.getUsername(), equalTo(VALID_USERNAME));
    }


}

