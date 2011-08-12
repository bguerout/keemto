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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Scope(value = "request")
public class UserConnectionController {

    private final ConnectionRepository connectionRepository;

    @Autowired
    public UserConnectionController(ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }


    @RequestMapping("/connections")
    public String getAllUserConnections(Model model) {
        model.addAttribute("connections", connectionRepository.findAllConnections());
        return "connections";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/connections")
    @ResponseBody
    public MultiValueMap getUserConnections(Model model) {
        return connectionRepository.findAllConnections();
    }
}
