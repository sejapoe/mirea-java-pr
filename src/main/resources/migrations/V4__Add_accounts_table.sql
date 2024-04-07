create table if not exists accounts
(
    id       bigserial    not null primary key,
    username varchar(255) not null unique,
    password varchar(255) not null,
    role     varchar(32)  not null
)