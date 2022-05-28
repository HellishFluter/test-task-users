create sequence hibernate_sequence start 1 increment 1;

create table USERS(
    id   bigserial    not null,
    NAME varchar(255) not null,
    AGE  int4         not null,
    EMAIL varchar(255) UNIQUE not null,
    primary key (id)

);

create table PROFILES
(
    id   bigserial    not null,
    CASH decimal(12,2) not null,
    MAX_CASH decimal(12,2) not null,
    USER_ID  int8         not null,
    primary key (id),
    constraint fk_user foreign key (USER_ID) references USERS (id)
);

create table PHONES
(
    id   bigserial    not null,
    VALUE varchar(20) UNIQUE not null,
    USER_ID  int8         not null,
    primary key (id),
    constraint fk_user foreign key (USER_ID) references USERS (id)
);