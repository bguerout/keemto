--DATA
insert into keemto_user (username, password, firstName, lastName, email)
 values ('stnevex', 'test', 'John', 'Doe','stnevex@gmail.com');

insert into events (ts,message,username,providerId,providerUserId)
 values ('1','hello this is a test','stnevex','twitter','293724331');

insert into events (ts,message,username,providerId,providerUserId)
 values ('1301464284370','First tweet','stnevex','twitter','293724331');

insert into events (ts,message,username,providerId,providerUserId)
 values ('1301464284371','Second tweet','stnevex','twitter','293724331');

insert into mail (id,sender,subject,body,ts,recipients)
 values('1','stnevex@gmail.com','subject','body',1322076312000,'to@xebia.fr,stnevex@xebia.fr');

insert into mail (id,sender,subject,body,ts,recipients)
 values('2','stnevex@gmail.com','subject2','body2',200,'to@xebia.fr,stnevex@xebia.fr');

insert into UserConnection (userId,providerId,providerUserId,rank,displayName,profileUrl,imageUrl,accessToken,secret,refreshToken,expireTime)
 values ('stnevex','twitter','293724331',1,'@stnevex','http://twitter.com/stnevex','http://www.gravatar.com/avatar/0a40a289089f2d262cc713c54cae7fa2.png','293724331-R8wyHN0CXBZ5BNDri3OrblT4ntYHXi976bLJ0IRm','zkSBVq9MV7Slp4ArUDn6lTTH4etvvbY1S7FOAxNoiGo',null,null);
