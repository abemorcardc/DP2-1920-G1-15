DROP TABLE vet_specialties IF EXISTS;
DROP TABLE vets IF EXISTS;
DROP TABLE specialties IF EXISTS;
DROP TABLE visits IF EXISTS;
DROP TABLE pets IF EXISTS;
DROP TABLE types IF EXISTS;
DROP TABLE owners IF EXISTS;
DROP TABLE users IF EXISTS;
DROP TABLE authorities IF EXISTS;
--
DROP TABLE persona IF EXISTS;
DROP TABLE cliente IF EXISTS;
DROP TABLE mecanico IF EXISTS;
DROP TABLE usuario IF EXISTS;
DROP TABLE averias IF EXISTS;

CREATE TABLE vets
(
   id INTEGER IDENTITY PRIMARY KEY,
   first_name VARCHAR (30),
   last_name VARCHAR (30)
);
CREATE INDEX vets_last_name ON vets (last_name);
CREATE TABLE specialties
(
   id INTEGER IDENTITY PRIMARY KEY,
   name VARCHAR (80)
);
CREATE INDEX specialties_name ON specialties (name);
CREATE TABLE vet_specialties
(
   vet_id INTEGER NOT NULL,
   specialty_id INTEGER NOT NULL
);
ALTER TABLE vet_specialties ADD CONSTRAINT fk_vet_specialties_vets FOREIGN KEY (vet_id) REFERENCES vets (id);
ALTER TABLE vet_specialties ADD CONSTRAINT fk_vet_specialties_specialties FOREIGN KEY (specialty_id) REFERENCES specialties (id);
CREATE TABLE types
(
   id INTEGER IDENTITY PRIMARY KEY,
   name VARCHAR (80)
);
CREATE INDEX types_name ON types (name);
CREATE TABLE owners
(
   id INTEGER IDENTITY PRIMARY KEY,
   first_name VARCHAR (30),
   last_name VARCHAR_IGNORECASE (30),
   address VARCHAR (255),
   city VARCHAR (80),
   telephone VARCHAR (20)
);
CREATE INDEX owners_last_name ON owners (last_name);
CREATE TABLE pets
(
   id INTEGER IDENTITY PRIMARY KEY,
   name VARCHAR (30),
   birth_date DATE,
   type_id INTEGER NOT NULL,
   owner_id INTEGER NOT NULL
);
ALTER TABLE pets ADD CONSTRAINT fk_pets_owners FOREIGN KEY (owner_id) REFERENCES owners (id);
ALTER TABLE pets ADD CONSTRAINT fk_pets_types FOREIGN KEY (type_id) REFERENCES types (id);
CREATE INDEX pets_name ON pets (name);
CREATE TABLE visits
(
   id INTEGER IDENTITY PRIMARY KEY,
   pet_id INTEGER NOT NULL,
   visit_date DATE,
   description VARCHAR (255)
);
ALTER TABLE visits ADD CONSTRAINT fk_visits_pets FOREIGN KEY (pet_id) REFERENCES pets (id);
CREATE INDEX visits_pet_id ON visits (pet_id);
CREATE TABLE users
(
   username varchar_ignorecase (255) NOT NULL PRIMARY KEY,
   password varchar_ignorecase (255) NOT NULL,
   enabled BOOLEAN NOT NULL
);
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
------------------
--CREATE TABLE averias
--(
  -- id INTEGER IDENTITY PRIMARY KEY,
   --nombre varchar (20),
   --descripcion varchar (50),
   --coste INTEGER NOT NULL,
   --tiempo INTEGER NOT NULL,
   --piezas_necesarias INTEGER NOT NULL,
   --complejidad varchar (50),
   --reparada BOOLEAN NOT NULL,
--);
--ALTER TABLE averias ADD CONSTRAINT fk_averias_vehiculos FOREIGN KEY (vehiculo_id) REFERENCES vehiculos (id);
--ALTER TABLE averias ADD CONSTRAINT fk_averias_mecanicos FOREIGN KEY (mecanico_id) REFERENCES mecanicos (id);
--ALTER TABLE averias ADD CONSTRAINT fk_averias_citas FOREIGN KEY (cita_id) REFERENCES citas (id);
-- no se... CREATE INDEX averias_pet_id ON averias (pet_id);
------------------