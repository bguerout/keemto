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
package fr.keemto.web;

import java.security.Principal;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;

@Configuration
public class ConnectionRepositoryConfig {

    @Inject
    protected UsersConnectionRepository usersConnectionRepository;

    @Bean
    @Scope(value = "request")
    public ConnectionRepository connectionRepository(@Value("#{request.userPrincipal}") Principal principal) {
        if (principal == null) {
            throw new IllegalStateException(
                    "Unable to get a ConnectionRepository because no principal is attached to this request "
                            + " or this method has been called outside a web container.");
        }
        return usersConnectionRepository.createConnectionRepository(principal.getName());
    }

}
