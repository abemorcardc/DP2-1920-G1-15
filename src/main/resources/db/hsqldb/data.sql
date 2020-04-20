-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n', TRUE );

INSERT INTO authorities VALUES ('admin1', 'admin');
-- One owner user, named owner1 with passwor 0wn3r
INSERT INTO users (username, password,enabled) VALUES ('owner1','0wn3r', TRUE );
INSERT INTO authorities VALUES ('owner1','owner');
-- One vet user, named vet1 with passwor v3t
INSERT INTO users (username, password,enabled) VALUES ('vet1','v3t', TRUE );
INSERT INTO authorities VALUES ('vet1', 'veterinarian');
INSERT INTO vets VALUES (1,'James', 'Carter');
INSERT INTO vets VALUES (2,'Helen','Leary');
INSERT INTO vets VALUES (3,'Linda','Douglas');
INSERT INTO vets VALUES (4,'Rafael','Ortega');
INSERT INTO vets VALUES (5,'Henry','Stevens');
INSERT INTO vets VALUES (6,'Sharon','Jenkins');
INSERT INTO specialties VALUES (1,'radiology');
INSERT INTO specialties VALUES (2,'surgery');
INSERT INTO specialties VALUES (3,'dentistry');
INSERT INTO vet_specialties VALUES (2,1);
INSERT INTO vet_specialties VALUES (3,2);
INSERT INTO vet_specialties VALUES (3,3);
INSERT INTO vet_specialties VALUES (4,2);
INSERT INTO vet_specialties VALUES (5,1);
INSERT INTO types VALUES (1,'cat');
INSERT INTO types VALUES (2,'dog');
INSERT INTO types VALUES (3,'lizard');
INSERT INTO types VALUES (4,'snake');
INSERT INTO types VALUES (5,'bird');
INSERT INTO types VALUES (6,'hamster');
INSERT INTO owners VALUES (1,'George','Franklin','110 W. Liberty St.','Madison','6085551023','owner1');

INSERT INTO owners VALUES (2,'Betty','Davis','638 Cardinal Ave.','Sun Prairie','6085551749','owner1');
INSERT INTO owners VALUES (3,'Eduardo','Rodriquez','2693 Commerce St.','McFarland','6085558763','owner1');
INSERT INTO owners VALUES (4,'Harold','Davis','563 Friendly St.','Windsor','6085553198','owner1');
INSERT INTO owners VALUES (5,'Peter','McTavish','2387 S. Fair Way','Madison','6085552765', 'owner1');
INSERT INTO owners VALUES (6,'Jean','Coleman','105 N. Lake St.','Monona','6085552654','owner1');
INSERT INTO owners VALUES (7,'Jeff','Black','1450 Oak Blvd.','Monona', '6085555387', 'owner1');
INSERT INTO owners VALUES (8, 'Maria', 'Escobito','345 Maple St.', 'Madison', '6085557683', 'owner1');
INSERT INTO owners VALUES (9,'David','Schroeder', '2749 Blackhawk Trail', 'Madison','6085559435','owner1');
INSERT INTO owners VALUES (10,'Carlos', 'Estaban','2335 Independence La.','Waunakee','6085555487','owner1');
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (1,'Leo','2010-09-07', 1, 1);
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (2,'Basil','2012-08-06',6,2);
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (3,'Rosy','2011-04-17',2,3);
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (4,'Jewel','2010-03-07',2,3);
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (5,'Iggy','2010-11-30',3, 4);
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (6,'George','2010-01-20',4,5);
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (7,'Samantha','2012-09-04',1,6);
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (8,'Max','2012-09-04',1,6);
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (9,'Lucky','2011-08-06',5,7);
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (10,'Mulligan','2007-02-24', 2,8);
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (11,'Freddy','2010-03-09', 5,9);
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (12,'Lucky','2010-06-24', 2,10);
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (13,'Sly','2012-06-08',1,10);
INSERT INTO visits (id,pet_id,visit_date,description) VALUES (1,7,'2013-01-01','rabies shot');
INSERT INTO visits (id,pet_id,visit_date,description) VALUES (2,8,'2013-01-02','rabies shot');
INSERT INTO visits (id,pet_id,visit_date,description) VALUES (3,8,'2013-01-03','neutered');
INSERT INTO visits (id,pet_id,visit_date,description) VALUES (4,7,'2013-01-04','spayed');
-- Talleres Paco -----------------------------
INSERT INTO usuarios (nombre_usuario,contra,enabled) VALUES ('manolo','manolo', TRUE );
INSERT INTO authorities VALUES ('manolo','cliente');
INSERT INTO usuarios (nombre_usuario,contra,enabled) VALUES ('manoli','manoli', TRUE );
INSERT INTO authorities VALUES ('manoli','cliente');
INSERT INTO usuarios (nombre_usuario,contra,enabled) VALUES ('david','david', TRUE );
INSERT INTO authorities VALUES ('david','cliente');
INSERT INTO usuarios (nombre_usuario,contra,enabled) VALUES ('paco','paco', TRUE );
INSERT INTO authorities VALUES ('paco','mecanico');
INSERT INTO usuarios (nombre_usuario,contra,enabled) VALUES ('lolo','lolo', TRUE );
INSERT INTO authorities VALUES ('lolo','mecanico');
INSERT INTO usuarios (nombre_usuario,contra,enabled) VALUES ('pepe','pepe', TRUE );
INSERT INTO authorities VALUES ('pepe','mecanico');
--Clientes ------ ID, Apellidos, Direccion, DNI, Email, Nombre, Telefono, Nombre_usuario
INSERT INTO clientes VALUES (1,'Martin','C/Tarfia','77844576X','Manolo72@gmail.com','Manolo','608555102','manolo');
INSERT INTO clientes VALUES (2,'Naranjo','C/Betis','91367576D','manoli@gmail.com','Manoli','608726190','manoli');
INSERT INTO clientes VALUES (3,'Fernandez','C/Sevilla','94567589R','david@gmail.com','David','608726190','david');

--Mecanicos ------ ID, Apellidos, Direccion, DNI, Email, Nombre, Telefono, Averias_reparadas, experiencia, titulacion ,Nombre_usuario
INSERT INTO mecanicos VALUES (1, 'Naranjo', 'C/Esperanza', '21154416G', 'PacoTalleres@gmail.com', 'Paco', '666973647', 12, 'ninguna', 'Fp de mecanico','paco');
INSERT INTO mecanicos VALUES (2, 'Lopez', 'C/Macarena', '25486596L', 'LoloTalleres@gmail.com', 'Lolo', '690670547', 5, 'heredada', 'mecanico hijo','lolo');
INSERT INTO mecanicos VALUES (3, 'Lopez', 'C/Macarena', '12456776V', 'PepeTalleres@gmail.com', 'Pepe', '690670547', 5, 'heredada', 'grado en mecanica','pepe');

--Vehículos ------ ID, Fecha Matriculacion, Kilometraje, Matricula, Modelo, Tipo, Cliente_ID

INSERT INTO vehiculos VALUES(1,True,'2012-09-04',10000,'2345FCL','Mercedes A','turismo',1);
INSERT INTO vehiculos VALUES(2,True,'2010-05-12',15000,'5125DRF','Peugeot 307','turismo',2);
INSERT INTO vehiculos VALUES(3,True,'2013-09-04',10200,'7634LDM','Seat Leon','turismo',3);
INSERT INTO vehiculos VALUES(4,True,'2012-09-01',1000,'0345FCL','Mercedes AX','turismo',1);



--Citas ----- ID, Coste, Description, EsAceptado, Urgente, Estado Cita, Fecha Cita, Tiempo, Tipo Cita, Cliente_ID, Mecanico_ID, Vehiculo_ID

INSERT INTO citas VALUES (1, 120, 'Problemas con el motor', TRUE, 'pendiente', '2021-03-14 12:00:00', 40, 'reparacion', 1, 1, 1);
INSERT INTO citas VALUES (2, 100, 'luna rota', TRUE , 'finalizada','2022-01-01 12:30:00', 100,'revision', 2, 2, 2);
INSERT INTO citas VALUES (3, 200, 'puerta mal', TRUE , 'cancelada','2020-01-01 13:00:00',150,'revision', 3,3, 3);
-- INSERT INTO citas VALUES (4, 120, 'Ruedas delanteras', TRUE, 'aceptada', '2021-03-14 12:00:00', 40, 'preparacion_itv', 1, 1, 1);
-- INSERT INTO citas VALUES (5, 100, 'luces delanteras', TRUE , 'aceptada','2020-01-01 12:30:00', 100,'modificacion', 2, 2, 2);
-- INSERT INTO citas VALUES (6, 200, 'freno de mano', TRUE , 'aceptada','2021-01-01 13:00:00',150,'revision', 3,3, 3);

--Averías ----- ID, Complejidad, Coste, Descripción, EsReparada, Nombre, Piezas Necesarias, Tiempo, Cita_ID, Mecanico_ID, Vehiculo_ID
INSERT INTO averias VALUES (  1,'BAJA',50.0,'cambio de bujia', FALSE ,'coche de manolo',1, 100, 1,1,1);
INSERT INTO averias VALUES (  2,'BAJA',50.0,'cambio de luna', FALSE ,'coche de manoli',1, 20, 2,2,2);
INSERT INTO averias VALUES (  3,'BAJA',50.0,'cambio de puerta', FALSE ,'coche de david',1, 30, 3,3,3);
INSERT INTO averias VALUES (  4,'BAJA',50.0,'paragolpes', FALSE ,'coche de manoli',1, 20, 2,2,2);

