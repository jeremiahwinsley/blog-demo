-- should be done with liquibase or flyway, but for demo purposes a script suffices
create sequence hibernate_sequence start 1 increment 1;

    create table author (
       id uuid not null,
        created_by uuid,
        created_date timestamp,
        last_modified_by uuid,
        last_modified_date timestamp,
        display_name varchar(255),
        role int4 not null,
        username varchar(255),
        primary key (id)
    );

    create table post (
       id int8 not null,
        created_by uuid,
        created_date timestamp,
        last_modified_by uuid,
        last_modified_date timestamp,
        body oid,
        excerpt varchar(255),
        publish_date timestamp not null,
        title varchar(255),
        author_id uuid not null,
        primary key (id)
    );

    alter table if exists post
       add constraint FK5l759v7apba3lqguc7bp8h456
       foreign key (author_id)
       references author;
