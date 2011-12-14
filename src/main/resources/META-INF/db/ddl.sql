--DDL
drop table userconnection;

drop table events;
create table events (
    ts BIGINT,
    message varchar,
    username varchar(255),
    providerId varchar(255) not null,
	providerUserId varchar(255),
    primary key (ts)
);

drop table keemto_user;
create table keemto_user (
    username varchar unique,
    password varchar not null,
    firstName varchar not null,
    lastName varchar not null,
    role varchar not null,
    email varchar,
    primary key (username)
 );

drop table mail;
create table mail (id varchar unique,
    sender varchar,
    subject varchar,
    body varchar,
    ts BIGINT,
    recipients varchar,
    primary key (id));
