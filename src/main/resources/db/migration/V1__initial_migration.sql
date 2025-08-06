create table users
(
    id       bigint auto_increment
        primary key,
    username varchar(255) not null,
    email    varchar(255) not null,
    password varchar(255) null
);

create table profiles
(
    id            bigint auto_increment
        primary key,
    avatar_url    varchar(255) null,
    language_pref varchar(255) null,
    user_id       bigint       null,
    constraint profiles_users_id_fk
        foreign key (user_id) references users (id)
);

