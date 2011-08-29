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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@Scope(value = "request")
@RequestMapping(value = "/api/connections")
public class ConnectionController {

    private final ConnectionRepository connectionRepository;

    @Autowired
    public ConnectionController(ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<JsonConnection> getUserConnections() {

        MultiValueMap<String, Connection<?>> connectionsByProviderMap = connectionRepository.findAllConnections();

        return convertUserConnectionsToJsonConnections(connectionsByProviderMap);
    }

    private List<JsonConnection> convertUserConnectionsToJsonConnections(MultiValueMap<String, Connection<?>> connectionsByProviderMap) {
        List<JsonConnection> userConnections = new ArrayList<JsonConnection>();
        for (List<Connection<?>> connections : connectionsByProviderMap.values()) {
            for (Connection<?> connx : connections) {
                userConnections.add(new JsonConnection(connx));
            }
        }
        return userConnections;
    }

    @RequestMapping(value = "/{providerId}-{providerUserId}", method = RequestMethod.GET)
    @ResponseBody
    public JsonConnection getUserConnections(@PathVariable String providerId, @PathVariable String providerUserId) {
        //TODO we should use a ConnectionKeyBuilder to convert id to provider*Id
        Connection<?> connection = connectionRepository.getConnection(new ConnectionKey(providerId, providerUserId));
        return new JsonConnection(connection);
    }

    @RequestMapping(value = {"/{providerId}-{providerUserId}"}, method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ResponseBody
    public void removeConnection(@PathVariable String providerId, @PathVariable String providerUserId) {
        connectionRepository.removeConnection(new ConnectionKey(providerId, providerUserId));
    }
}
