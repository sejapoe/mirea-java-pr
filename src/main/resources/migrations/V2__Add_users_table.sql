create table if not exists users
(
    id          bigserial    not null primary key,
    first_name  varchar(255) not null,
    last_name   varchar(255) not null,
    middle_name varchar(255) not null,
    birth_date  date         not null
);