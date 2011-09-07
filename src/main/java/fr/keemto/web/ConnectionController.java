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
import org.springframework.social.connect.*;
import org.springframework.social.connect.support.OAuth1ConnectionFactory;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.connect.web.ConnectSupport;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@Controller
@Scope(value = "request")
@RequestMapping(value = "/api/connections")
public class ConnectionController {

    private final ConnectionFactoryLocator connectionFactoryLocator;
    private final ConnectionRepository connectionRepository;
    private final ConnectSupport webSupport;

    @Autowired
    public ConnectionController(ConnectionRepository connectionRepository, ConnectionFactoryLocator connectionFactoryLocator) {
        this(connectionRepository, connectionFactoryLocator, new ConnectSupport());
    }

    public ConnectionController(ConnectionRepository connectionRepository, ConnectionFactoryLocator connectionFactoryLocator, ConnectSupport webSupport) {
        this.connectionRepository = connectionRepository;
        this.connectionFactoryLocator = connectionFactoryLocator;
        this.webSupport = webSupport;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<ConnectionViewBean> getUserConnections() {
        MultiValueMap<String, Connection<?>> connectionsByProviderMap = connectionRepository.findAllConnections();
        return convertUserConnectionsToConnectionViewBean(connectionsByProviderMap);
    }

    @RequestMapping(value = "/{providerId}-{providerUserId}", method = RequestMethod.GET)
    @ResponseBody
    public ConnectionViewBean getUserConnections(@PathVariable String providerId, @PathVariable String providerUserId) {
        //TODO we should use a ConnectionKeyBuilder to convert id to provider*Id
        Connection<?> connection = connectionRepository.getConnection(new ConnectionKey(providerId, providerUserId));
        return new ConnectionViewBean(connection);
    }

    @RequestMapping(value = {"/{providerId}-{providerUserId}"}, method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ResponseBody
    public void removeConnection(@PathVariable String providerId, @PathVariable String providerUserId) {
        connectionRepository.removeConnection(new ConnectionKey(providerId, providerUserId));
    }

    /**
     * @see org.springframework.social.connect.web.ConnectController
     */
    @RequestMapping(value = "/{providerId}", method = RequestMethod.POST)
    public RedirectView connect(@PathVariable String providerId, NativeWebRequest request) {
        ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(providerId);
        return new RedirectView(webSupport.buildOAuthUrl(connectionFactory, request));
    }

    @RequestMapping(value = "/{providerId}", method = RequestMethod.POST, params = "oauth_verifier")
    public RedirectView oauth1ManualCallback(@PathVariable String providerId, NativeWebRequest request) {
        return oauth1Callback(providerId, request);
    }

    /**
     * Allows service provider to complete OAuth1 connection creation by call back application
     *
     * @see org.springframework.social.connect.web.ConnectController
     */
    @RequestMapping(value = "/{providerId}", method = RequestMethod.GET, params = "oauth_token")
    public RedirectView oauth1Callback(@PathVariable String providerId, NativeWebRequest request) {
        OAuth1ConnectionFactory<?> connectionFactory = (OAuth1ConnectionFactory<?>) connectionFactoryLocator.getConnectionFactory(providerId);
        Connection<?> connection = webSupport.completeConnection(connectionFactory, request);
        connectionRepository.addConnection(connection);
        return createRedirectView();
    }

    /**
     * Allows service provider to complete OAuth2 connection creation by call back application
     *
     * @see org.springframework.social.connect.web.ConnectController
     */
    @RequestMapping(value = "/{providerId}", method = RequestMethod.GET, params = "code")
    public RedirectView oauth2Callback(@PathVariable String providerId, NativeWebRequest request) {
        OAuth2ConnectionFactory<?> connectionFactory = (OAuth2ConnectionFactory<?>) connectionFactoryLocator.getConnectionFactory(providerId);
        Connection<?> connection = webSupport.completeConnection(connectionFactory, request);
        connectionRepository.addConnection(connection);
        return createRedirectView();
    }

    private RedirectView createRedirectView() {
        return new RedirectView("/#connections", true);
    }

    private List<ConnectionViewBean> convertUserConnectionsToConnectionViewBean(MultiValueMap<String, Connection<?>> connectionsByProviderMap) {
        List<ConnectionViewBean> userConnections = new ArrayList<ConnectionViewBean>();
        for (List<Connection<?>> connections : connectionsByProviderMap.values()) {
            for (Connection<?> connx : connections) {
                userConnections.add(new ConnectionViewBean(connx));
            }
        }
        return userConnections;
    }


}
