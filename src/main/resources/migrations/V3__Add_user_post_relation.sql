alter table posts
    add column author_id bigint null references users (id);