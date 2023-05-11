CREATE TABLE transport (
  id varchar(255) NOT NULL,
  available_seats int NOT NULL,
  date date NOT NULL,
  destination_place varchar(255) NOT NULL,
  source_place varchar(255) NOT NULL,
  PRIMARY KEY (id)
)