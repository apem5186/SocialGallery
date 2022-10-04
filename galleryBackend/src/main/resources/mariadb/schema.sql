use socialgallery;

drop table if exists comments CASCADE;

drop table if exists file CASCADE;

drop table if exists post CASCADE;

drop table if exists refresh_token CASCADE;

drop table if exists users CASCADE;

drop table if exists users_roles CASCADE;

create table comments (
                          cid bigint auto_increment,
                          mod_date timestamp,
                          reg_date timestamp,
                          comment TEXT not null,
                          post_id bigint,
                          users_id bigint,
                          primary key (cid)
);

create table file (
                      file_id bigint auto_increment,
                      mod_date timestamp,
                      reg_date timestamp,
                      file_path varchar(255) not null,
                      file_size bigint,
                      origin_file_name varchar(255) not null,
                      post_id bigint,
                      primary key (file_id)
);

create table post (
                      pid bigint auto_increment,
                      mod_date timestamp,
                      reg_date timestamp,
                      category varchar(255) not null,
                      content varchar(255),
                      hits integer not null,
                      like_cnt integer not null,
                      review_cnt integer not null,
                      title varchar(255) not null,
                      users_id bigint,
                      primary key (pid)
);

create table refresh_token (
                               id bigint auto_increment,
                               mod_date timestamp,
                               reg_date timestamp,
                               key_ bigint not null,
                               token varchar(255) not null,
                               primary key (id)
);

create table users (
                       id bigint auto_increment,
                       mod_date timestamp,
                       reg_date timestamp,
                       auth_provider varchar(255) not null,
                       email varchar(100) not null,
                       from_social boolean not null,
                       password varchar(100) not null,
                       phone varchar(255),
                       picture varchar(255),
                       provider_id varchar(255),
                       username varchar(255) not null,
                       primary key (id)
);

create table users_roles (
                             users_id bigint not null,
                             roles varchar(255)
);

alter table users
    add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);

alter table users
    add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username);

alter table comments
    add constraint FKbqnvawwwv4gtlctsi3o7vs131
        foreign key (post_id)
            references post(pid);

alter table comments
    add constraint FKt55y2infwbbw3o942o2mhm0v4
        foreign key (users_id)
            references users(id);

alter table file
    add constraint FKnm59rbv6qbowpdacbalxrud1e
        foreign key (post_id)
            references post(pid);

alter table post
    add constraint FK654j7e05utx6icpfaoqxrdds2
        foreign key (users_id)
            references users(id);

alter table users_roles
    add constraint FKml90kef4w2jy7oxyqv742tsfc
        foreign key (users_id)
            references users(id);