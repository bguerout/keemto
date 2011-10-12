--DDL
create table events (
    ts BIGINT,
    message varchar,
    username varchar(255),
    providerId varchar(255) not null,
	providerUserId varchar(255),
    primary key (ts)
);

create table keemto_user (
    username varchar unique,
    password varchar not null,
    firstName varchar not null,
    lastName varchar not null,
    email varchar,
    primary key (username)
 );


--DATA
insert into keemto_user (username, password, firstName, lastName, email) values ('stnevex', 'test', 'John', 'Doe','stnevex@gmail.com');

insert into events (ts,message,username,providerId,providerUserId) values ('1','hello this is a test','stnevex','mail','stnevex@gmail.com');
insert into events (ts,message,username,providerId,providerUserId) values ('1301464284370','First tweet','stnevex','twitter','293724331');
insert into events (ts,message,username,providerId,providerUserId) values ('1301464284371','Second tweet','stnevex','twitter','293724331');
insert into events (ts,message,username,providerId,providerUserId) values ('1301464284372','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer rutrum suscipit sem, eu blandit urna porta eu. Phasellus fringilla eleifend accumsan.','stnevex','mail','stnevex@gmail.com');
insert into events (ts,message,username,providerId,providerUserId) values ('1301464284373','Nullam posuere elit vel justo facilisis volutpat. Vestibulum id nisi at erat semper venenatis. Duis at ultrices velit. Nam mollis, ante id cursus accumsan','stnevex','mail','stnevex@gmail.com');
insert into events (ts,message,username,providerId,providerUserId) values ('1301464284374','Nullam ut blandit mauris. Mauris vitae porttitor tellus. Maecenas rutrum nunc bibendum arcu convallis tincidunt. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus eleifend blandit ultrices','stnevex','mail','stnevex@gmail.com');
insert into events (ts,message,username,providerId,providerUserId) values ('1301464284375','Proin consectetur justo ut nunc aliquet commodo. Nam suscipit risus sit amet mi pulvinar mollis. Praesent quis accumsan nisl. Suspendisse accumsan posuere tempor. Sed eget ligula et diam sollicitudin laoreet ut at quam. In tristique accumsan tempus. Nulla porta purus eu orci mollis tristique. Lorem ipsum dolor sit amet, consectetur adipiscing elit','stnevex','mail','stnevex@gmail.com');
insert into events (ts,message,username,providerId,providerUserId) values ('1301464284376','Mauris posuere molestie ante vel eleifend. Cras erat nulla, malesuada nec laoreet sit amet, lacinia et ante. Morbi dictum lacus ac lectus posuere ut egestas lacus euismod. Aliquam erat volutpat. Morbi laoreet, lectus eu vehicula vehicula, nisl nisi sollicitudin erat, ut iaculis est velit sed leo. Praesent ornare rhoncus mi, ac blandit ante commodo ac','stnevex','mail','stnevex@gmail.com');


insert into UserConnection (userId,providerId,providerUserId,rank,displayName,profileUrl,imageUrl,accessToken,secret,refreshToken,expireTime)
values ('stnevex','twitter','293724331',1,'@stnevex','http://twitter.com/stnevex','http://www.gravatar.com/avatar/0a40a289089f2d262cc713c54cae7fa2.png','293724331-R8wyHN0CXBZ5BNDri3OrblT4ntYHXi976bLJ0IRm','zkSBVq9MV7Slp4ArUDn6lTTH4etvvbY1S7FOAxNoiGo',null,null);
