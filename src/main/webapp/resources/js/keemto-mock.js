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
        url: 'api/events',
        responseTime: 750,
        contentType: 'text/json',
        type: 'GET',
        responseText:[
            {
                "timestamp": '1',
                "user": 'bguerout',
                "message": 'Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur.',
                "user": {
                  "username": 'stnevex'
                },
                "providerConnection": {
                  "providerId": 'mail'
                }
            },
            {
                "timestamp": '2',
                "user": 'stnevex',
                "message": 'Praesent blandit odio eu enim. Pellentesque sed dui ut augue blandit sodales.',
                "user": {
                  "username": 'stnevex'
                },
                "providerConnection": {
                 "providerId":"twitter",
                 "providerUserId":"@twitter",
                 "displayName":"stnevex",
                 "profileUrl":"http://twitter.com/stnevex",
                 "imageUrl":"http://a0.twimg.com/sticky/default_profile_images/default_profile_5_normal.png"
                }
            }
        ]

    });

    //event creation
    $.mockjax({
        url: 'api/events',
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
                providerConnection: {
                    providerId: 'mail'
                }

            };
        }

    });

    //connections list
    $.mockjax({
        url: 'api/connections',
        responseTime: 750,
        contentType: 'text/json',
        type: 'GET',
        responseText: [
            {
                "id":"twitter-1111",
                "displayName":"stnevex",
                "providerId":"twitter",
                "profileUrl":"http://twitter.com/stnevex",
                "imageUrl":"http://twitter.com/stnevex.jpg"
            },
            {
                "id":"yammer-9999",
                "displayName":"bguerout(error on revoke)",
                "providerId":"twitter",
                "profileUrl":"http://yammer.com/bguerout",
                "imageUrl":"http://yammer.com/bguerout.jpg"
            }
        ]
    });

    $.mockjax({
        url: 'api/connections',
        responseTime: 750,
        contentType: 'text/json',
        type: 'POST',
        status: 202,
        responseText:{
            "authorizeUrl": 'https://api.twitter.com/oauth/authorize'
        }
    });

    //Remove connection
    $.mockjax({
        url: 'api/connections/twitter-1111',
        responseTime: 750,
        contentType: 'text/json',
        type: 'DELETE',
        status: 204
    });
    $.mockjax({
        url: 'api/connections/yammer-9999',
        responseTime: 750,
        contentType: 'text/json',
        type: 'DELETE',
        status: 500
    });

    //login
    $.mockjax({
        url: 'api/login',
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

     Keemto.init();

});