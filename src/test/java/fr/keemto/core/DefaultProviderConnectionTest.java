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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class DefaultProviderConnectionTest {

    @Test
    public void shouldCreateIdWithProviderIds() throws Exception {

        DefaultProviderConnection dpc = new DefaultProviderConnection("yammer", "5555", null, null, null);

        assertThat(dpc.getId(), equalTo("yammer-5555"));
    }

    @Test
    public void shouldBeAnonymous() throws Exception {

        DefaultProviderConnection dpc = new DefaultProviderConnection("yammer");

        assertThat(dpc.isAnonymous(), equalTo(true));
    }

    @Test
    public void shouldNotBeAnonymous() throws Exception {

        DefaultProviderConnection dpc = new DefaultProviderConnection("yammer", "5555", null, null, null);

        assertThat(dpc.isAnonymous(), equalTo(false));
    }
}
