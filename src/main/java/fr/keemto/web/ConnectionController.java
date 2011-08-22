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
import org.springframework.social.connect.web.ConnectController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletResponse;
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
    public List<Connection<?>> getUserConnections() {
        List<Connection<?>> userConnections = new ArrayList<Connection<?>>();
        MultiValueMap<String, Connection<?>> connectionsByProviderMap = connectionRepository.findAllConnections();
        for (List<Connection<?>> connections : connectionsByProviderMap.values()){
            userConnections.addAll(connections);
        }
        return userConnections;
    }

    @RequestMapping(value = "/{providerId}", method = RequestMethod.GET)
    @ResponseBody
    public List<Connection<?>> getUserConnections(@PathVariable String providerId) {
        return connectionRepository.findConnections(providerId);
    }

    @RequestMapping(value = "/{providerId}/{providerUserId}", method = RequestMethod.DELETE)
    @ResponseStatus(value= HttpStatus.NO_CONTENT)
    @ResponseBody
    public void removeConnection(@PathVariable String providerId, @PathVariable String providerUserId) {
        connectionRepository.removeConnection(new ConnectionKey(providerId, providerUserId));
    }
}
