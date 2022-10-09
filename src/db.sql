create table if not exists users
(
    id                   serial primary key,
    name                 varchar(255) not null,
    email                varchar(255) not null unique,
    password             varchar(255) not null,
    created_at           timestamp default current_timestamp,
    updated_at           timestamp default current_timestamp,
    role                 int       default 0,
    promo_id             int references promos (id),
    deleted_at           timestamp,
    last_brief_read_date timestamp
);


create table if not exists admins
(
    id       serial primary key,
    username varchar(255) not null unique,
    password varchar(255) not null
);

create table if not exists promos
(
    id         serial primary key,
    name       varchar(255) not null unique,
    created_at timestamp             default current_timestamp,
    updated_at timestamp             default current_timestamp,
    deleted_at timestamp,
    year       int          not null default 2020
);

create table if not exists briefs
(
    id           serial primary key,
    name         varchar(255) not null,
    description  text         not null,
    created_at   timestamp default current_timestamp,
    updated_at   timestamp default current_timestamp,
    deleted_at   timestamp,
    published_at timestamp,
    user_id      int references users (id),
    promo_id     int references promos (id)
);


