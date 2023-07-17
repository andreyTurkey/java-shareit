--drop table IF EXISTS users CASCADE;
--drop table IF EXISTS items CASCADE;
--drop table IF EXISTS bookings CASCADE;
--drop table IF EXISTS comments CASCADE;
--drop table IF EXISTS requests CASCADE;
--drop table IF EXISTS answers CASCADE;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
    );

CREATE TABLE IF NOT EXISTS items (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(512) NOT NULL,
    owner BIGINT,
    available boolean,
    request_Id INTEGER,
    CONSTRAINT pk_items PRIMARY KEY (id),
    CONSTRAINT USER_ID_FK foreign key (owner) references users(id)
    );

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    item_id INTEGER NOT NULL,
    booking_state VARCHAR,
    user_id INTEGER,
    booking_start TIMESTAMP WITHOUT TIME ZONE,
    booking_end TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_booking PRIMARY KEY (id),
    constraint USER_ID_FK_BOOKINGS foreign key (user_id) references users(id),
    constraint ITEM_ID_FK_BOOKINGS foreign key (item_id) references items(id)
    );

CREATE TABLE IF NOT EXISTS comments (
                                        id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                        item_id INTEGER NOT NULL,
                                        text VARCHAR NOT NULL,
                                        user_id INTEGER NOT NULL,
                                        created TIMESTAMP WITHOUT TIME ZONE,
                                        CONSTRAINT pk_comments PRIMARY KEY (id),
    constraint USER_ID_FK_comments foreign key (user_id) references users(id),
    constraint ITEM_ID_FK_comments foreign key (item_id) references items(id)
    );

CREATE TABLE IF NOT EXISTS requests (
                                        id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                        description VARCHAR NOT NULL,
                                        user_id INTEGER NOT NULL,
                                        created TIMESTAMP WITHOUT TIME ZONE,
                                        CONSTRAINT pk_requests PRIMARY KEY (id),
    constraint USER_ID_FK_requests foreign key (user_id) references users(id)
    );

CREATE TABLE IF NOT EXISTS answers (
                                        id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                        item_Id INTEGER NOT NULL,
                                        item_Request_Id INTEGER,
                                        CONSTRAINT pk_answers PRIMARY KEY (id),
    constraint ITEM_ID_FK_answers foreign key (item_Id) references items(id),
    constraint itemRequest_FK_answers foreign key (item_Request_Id) references requests(id)
    );