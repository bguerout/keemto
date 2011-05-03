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

insert into events (ts,message,username) values ('1301464284372','message1','bguerout');
insert into events (ts,message,username) values ('1301464284373','message2','bguerout');
insert into events (ts,message,username) values ('1301464284374','message3','bguerout');
insert into events (ts,message,username) values ('1301464284375','message4','bguerout');
insert into events (ts,message,username) values ('1301464284376','message5','bguerout');
insert into events (ts,message,username) values ('1301464284377','message6','bguerout');
insert into events (ts,message,username) values ('1301464284378','message7','bguerout');
insert into events (ts,message,username) values ('1301464214372','message8','bguerout');
insert into events (ts,message,username) values ('1301464184372','message9','bguerout');
insert into events (ts,message,username) values ('1301461284372','message10','bguerout');
insert into events (ts,message,username) values ('1301414284372','message11','bguerout');
insert into events (ts,message,username) values ('1301164284372','message12','bguerout');
insert into events (ts,message,username) values ('1201464284372','message13','bguerout');
insert into events (ts,message,username) values ('1301454284352','message14','bguerout');
insert into events (ts,message,username) values ('1301414004372','message15','bguerout');
insert into events (ts,message,username) values ('1301464282382','message16','bguerout');
insert into events (ts,message,username) values ('1301464454372','message17','bguerout');
insert into events (ts,message,username) values ('1301364204372','message18','bguerout');
