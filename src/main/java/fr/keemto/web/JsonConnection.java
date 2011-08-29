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


import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;

public class JsonConnection {

    private final Connection<?> connection;

    public JsonConnection(Connection<?> connection) {
        this.connection = connection;
    }

    public String getId() {
        ConnectionKey key = connection.getKey();
        return key.getProviderId() + "-" + key.getProviderUserId();
    }


    public String getProviderId() {
        return connection.getKey().getProviderId();
    }

    public String getDisplayName() {
        return connection.getDisplayName();
    }


    public String getProfileUrl() {
        return connection.getProfileUrl();
    }


    public String getImageUrl() {
        return connection.getImageUrl();
    }
}
