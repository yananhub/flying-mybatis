create table book
(
    id bigint identity
        constraint BOOK_PK
        primary key,
    name varchar(100),
    publish_date date
);