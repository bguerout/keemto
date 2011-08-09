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

$(document).ready(function () {

    //events init list
    $.mockjax({
        url: '/events',
        responseTime: 750,
        contentType: 'text/json',
        type: 'GET',
        responseText:[
            {
            id: '1',
            timestamp: '1',
            user: 'bguerout',
            message: 'Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur.',
            providerId: 'mail'
            },
            {
            id: '2',
            timestamp: '2',
            user: 'stnevex',
            message: 'Praesent blandit odio eu enim. Pellentesque sed dui ut augue blandit sodales.',
            providerId: 'twitter'
            }
         ]

    });

    //event creation
    $.mockjax({
        url: '/events',
        responseTime: 750,
        status: 201,
        contentType: 'text/json',
        type: 'POST',
        response: function (settings) {
            var timestamp = new Date().getTime();
            this.responseText = {
                id: timestamp,
                timestamp: '100',
                user: 'bguerout',
                message: timestamp + ' / Ex vix aliquip euismod. Per verear tacimates persequeris ad. Recusabo expetendis ei vix..',
                providerId: 'mail'
            };
        }

    });

    //connections list
    $.mockjax({
        url: '/connections',
        responseTime: 750,
        contentType: 'text/json',
        type: 'GET',
        responseText: [
            {displayName: '@stnevex',
            profileUrl:'http://twitter.com/stnevex',
            providerId: 'twitter'}
            ,
             {displayName: '@bguerout',
            profileUrl:'http://twitter.com/bguerout',
            providerId: 'twitter'}
        ]
    });

    //login
    $.mockjax({
        url: '/api/login',
        responseTime: 750,
        contentType: 'text/json',
        type: 'POST',
        response: function (settings) {

            if (settings.data.login == '') {
                this.status = 401;
                this.responseText = {"username":null,"loggedIn":false};
            } else {
                this.status = 200;
                this.responseText = {"username":"stnevex","loggedIn":true};
            }
        }
    });

});