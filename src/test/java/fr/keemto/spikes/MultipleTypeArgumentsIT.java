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

package fr.keemto.spikes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.core.GenericTypeResolver;
import org.springframework.social.connect.Connection;
import org.springframework.social.twitter.api.Twitter;

public class MultipleTypeArgumentsIT {

    @Test
    public void genericType() throws Exception {
        ClassWithTypeArguments typor = new ClassWithTypeArguments();

        Class<?>[] args = GenericTypeResolver.resolveTypeArguments(typor.getClass(), InterfaceWithTypeArguments.class);

        assertThat(args.length, equalTo(2));
        assertTrue(args[0].equals(Twitter.class));
        assertTrue(args[1].equals(Connection.class));
    }

    public final class ClassWithTypeArguments implements InterfaceWithTypeArguments<Twitter, Connection<Twitter>> {

        @Override
        public void test(Connection<Twitter> connection) {
        }
    }
}
