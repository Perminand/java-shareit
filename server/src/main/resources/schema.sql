
drop table IF EXISTS request, comments,bookings,items, users;

create table if not exists users (
  id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

create table if not exists request (
	id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
	description VARCHAR NOT NULL,
	requester_id BIGINT NOT NULL,
	created_date TIMESTAMP WITHOUT TIME ZONE,
	CONSTRAINT pk_request PRIMARY KEY (id),
	CONSTRAINT fk_request_users FOREIGN KEY (requester_id) REFERENCES users
);

create table if not exists items (
  id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR NOT null,
  is_available BOOLEAN not null,
  owner_id BIGINT not null,
  request_id BIGINT,
  CONSTRAINT pk_item PRIMARY KEY  (id),
  CONSTRAINT fk_item_users FOREIGN KEY (owner_id) REFERENCES users,
  CONSTRAINT fk_item_requests FOREIGN KEY (request_id) REFERENCES request
  );

create table if not exists bookings (
    id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id BIGINT,
    booker_id BIGINT,
    status VARCHAR,
    CONSTRAINT pk_booking PRIMARY KEY  (id),
    CONSTRAINT fk_booking_users FOREIGN KEY (booker_id) REFERENCES users,
    CONSTRAINT fk_booking_items FOREIGN KEY (item_id) REFERENCES items
);

create table if not exists comments (
    id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text VARCHAR NOT NULL,
    item_id BIGINT,
    author_id BIGINT,
    created TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_comments PRIMARY KEY  (id),
    CONSTRAINT fk_comments_users FOREIGN KEY (author_id) REFERENCES users,
    CONSTRAINT fk_comments_items FOREIGN KEY (item_id) REFERENCES items
);