create table question
(
    id         bigint   not null,
    text       varchar(500) not null,
    epoch_time bigint      not null,
    constraint question_pk primary key (id)
);

create table answer_option
(
    id           bigint    not null,
    id_question  bigint    not null,
    text         varchar(500) not null,
    vote_count   int not null,
    constraint answer_option_pk primary key (id),
    constraint id_question_fk foreign key (id_question) references question (id)
    );


-- acl sample from spring acl template

create table acl_sid(
    id bigserial not null primary key,
    principal boolean not null,
    sid varchar(100) not null,
    constraint unique_uk_1 unique(sid,principal)
);

create table acl_class(
    id bigserial not null primary key,
    class varchar(100) not null,
    class_id_type varchar(100),
    constraint unique_uk_2 unique(class)
);

create table acl_object_identity(
    id bigserial primary key,
    object_id_class bigint not null,
    object_id_identity varchar(36) not null,
    parent_object bigint,
    owner_sid bigint,
    entries_inheriting boolean not null,
    constraint unique_uk_3 unique(object_id_class,object_id_identity),
    constraint foreign_fk_1 foreign key(parent_object)references acl_object_identity(id),
    constraint foreign_fk_2 foreign key(object_id_class)references acl_class(id),
    constraint foreign_fk_3 foreign key(owner_sid)references acl_sid(id)
);

create table acl_entry(
    id bigserial primary key,
    acl_object_identity bigint not null,
    ace_order int not null,
    sid bigint not null,
    mask integer not null,
    granting boolean not null,
    audit_success boolean not null,
    audit_failure boolean not null,
    constraint unique_uk_4 unique(acl_object_identity,ace_order),
    constraint foreign_fk_4 foreign key(acl_object_identity) references acl_object_identity(id),
    constraint foreign_fk_5 foreign key(sid) references acl_sid(id)
);

-- user related tables

create table user_account(

    id bigserial primary key,
    username varchar(200) unique not null,
    password varchar(200) not null,
    enabled boolean not null
);



create table user_role(

    id bigserial primary key,
    user_id bigint not null,
    role_type varchar(200) not null,
    constraint user_fk foreign key(user_id) references user_account(id)
);


-- sequences

create sequence if not exists hibernate_sequence start 10000;
create sequence if not exists user_id_seq start 100;
