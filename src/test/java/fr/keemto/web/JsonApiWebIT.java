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

import fr.keemto.core.Event;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;


public class JsonApiWebIT {

    //TODO externalize me
    public static final String URL = "http://127.0.0.1:8080/keemto/api";

    private RestTemplate template;


    @Before
    public void prepare() throws Exception {
        template = new RestTemplate();
    }

    @Test
    public void shouldReturnEventsList() {

        Event[] events = template.getForObject(buildUrl("events"), Event[].class);

        assertThat(events, notNullValue());
        assertThat(events.length, equalTo(6));
    }


    private String buildUrl(String path) {
        return URL + "/" + path;
    }


}

