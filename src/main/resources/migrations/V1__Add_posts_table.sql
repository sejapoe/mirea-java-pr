create table if not exists posts
(
    id            bigserial     not null primary key,
    text          varchar(1023) not null,
    creation_date timestamp default current_timestamp
)