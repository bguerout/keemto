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

import fr.keemto.core.User;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;

import java.util.ArrayList;
import java.util.List;

class ConnectionResolver<T> {

    private final UsersConnectionRepository usersConnectionRepository;

    private final Class<T> apiClass;

    ConnectionResolver(Class<T> clazz, UsersConnectionRepository usersConnectionRepository) {
        this.usersConnectionRepository = usersConnectionRepository;
        this.apiClass = clazz;
    }

    public List<Connection<T>> getConnectionsFor(User user) {
                ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(user.getUsername());
        return connectionRepository.findConnections(apiClass);
    }

    Class<T> getApiClass() {
        return apiClass;
    }

}
