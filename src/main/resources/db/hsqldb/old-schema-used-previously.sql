DROP TABLE authorities IF EXISTS;

DROP TABLE persona IF EXISTS;
DROP TABLE cliente IF EXISTS;
DROP TABLE mecanico IF EXISTS;
DROP TABLE usuario IF EXISTS;
DROP TABLE averias IF EXISTS;


CREATE TABLE authorities
(
   username varchar_ignorecase (50) NOT NULL,
   authority varchar_ignorecase (50) NOT NULL,
);
ALTER TABLE authorities ADD CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users (username);
CREATE UNIQUE INDEX ix_auth_username ON authorities
(
   username,
   authority
);
