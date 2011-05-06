--DDL

create table xevents_user (
    id identity,
    username varchar unique,
    primary key (id)
);

create table events (
    ts long,
    message varchar,
    username varchar,
    primary key (ts)
);


--DATA

insert into xevents_user (username) values ('bguerout');

insert into events (ts,message,username) values ('1','eventTest','tester');
insert into events (ts,message,username) values ('1301464284372','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer rutrum suscipit sem, eu blandit urna porta eu. Phasellus fringilla eleifend accumsan.','bguerout');
insert into events (ts,message,username) values ('1301464284373','Nullam posuere elit vel justo facilisis volutpat. Vestibulum id nisi at erat semper venenatis. Duis at ultrices velit. Nam mollis, ante id cursus accumsan','bguerout');
insert into events (ts,message,username) values ('1301464284374','Nullam ut blandit mauris. Mauris vitae porttitor tellus. Maecenas rutrum nunc bibendum arcu convallis tincidunt. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus eleifend blandit ultrices','bguerout');
insert into events (ts,message,username) values ('1301464284375','Proin consectetur justo ut nunc aliquet commodo. Nam suscipit risus sit amet mi pulvinar mollis. Praesent quis accumsan nisl. Suspendisse accumsan posuere tempor. Sed eget ligula et diam sollicitudin laoreet ut at quam. In tristique accumsan tempus. Nulla porta purus eu orci mollis tristique. Lorem ipsum dolor sit amet, consectetur adipiscing elit','bguerout');
insert into events (ts,message,username) values ('1301464284376','Mauris posuere molestie ante vel eleifend. Cras erat nulla, malesuada nec laoreet sit amet, lacinia et ante. Morbi dictum lacus ac lectus posuere ut egestas lacus euismod. Aliquam erat volutpat. Morbi laoreet, lectus eu vehicula vehicula, nisl nisi sollicitudin erat, ut iaculis est velit sed leo. Praesent ornare rhoncus mi, ac blandit ante commodo ac','bguerout');
