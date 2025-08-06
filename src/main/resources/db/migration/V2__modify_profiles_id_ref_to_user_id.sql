alter table profiles
    modify id bigint not null;

alter table profiles
    add constraint profiles_users_id__fk
        foreign key (id) references users (id);

alter table profiles
drop foreign key profiles_users_id_fk;

alter table profiles
drop column user_id;