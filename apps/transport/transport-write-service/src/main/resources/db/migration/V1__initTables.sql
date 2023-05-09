CREATE TABLE transport_event (
    id varchar(36) not null,
    event_json TEXT not null,
    transport_id varchar(36) not null,
    type varchar(255) not null,
    primary key(id)
);