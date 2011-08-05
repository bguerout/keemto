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
                this.responseText = "Unable to authenticate user";
            } else {
                this.status = 200;
                this.responseText = {
                    login: 'stnevex'
                };
            }
        }
    });

});