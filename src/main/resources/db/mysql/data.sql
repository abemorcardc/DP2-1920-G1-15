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
INSERT INTO vehiculos VALUES(5,True,'2000-05-12',2000,'1789JNB','Peugeot 200','turismo',2);



--Citas ----- ID, Coste, Description, EsAceptado, Urgente, Estado Cita, Fecha Cita, Tiempo, Tipo Cita, Cliente_ID, Mecanico_ID, Vehiculo_ID

INSERT INTO citas VALUES (1, 120, 'Problemas con el motor', TRUE, 'pendiente', '2021-03-14 12:00:00', 40, 'reparacion', 1, 1, 1);
INSERT INTO citas VALUES (2, 100, 'luna rota', TRUE , 'finalizada','2022-01-01 12:30:00', 100,'revision', 2, 2, 2);
INSERT INTO citas VALUES (3, 200, 'puerta mal', TRUE , 'cancelada','2020-01-01 13:00:00',150,'revision', 3,3, 3);
INSERT INTO citas VALUES (4, 120, 'Ruedas delanteras', TRUE, 'pendiente', '2021-03-14 12:00:00', 40, 'preparacion_itv', 1, null, 2);
-- INSERT INTO citas VALUES (5, 100, 'luces delanteras', TRUE , 'aceptada','2020-01-01 12:30:00', 100,'modificacion', 2, 2, 2);
-- INSERT INTO citas VALUES (6, 200, 'freno de mano', TRUE , 'aceptada','2021-01-01 13:00:00',150,'revision', 3,3, 3);

--Averías ----- ID, Complejidad, Coste, Descripción, EsReparada, Nombre, Piezas Necesarias, Tiempo, Cita_ID, Mecanico_ID, Vehiculo_ID
INSERT INTO averias VALUES (  1,'BAJA',50.0,'cambio de bujia', FALSE ,'coche de manolo',1, 100, 1,1,1);
INSERT INTO averias VALUES (  2,'BAJA',50.0,'cambio de luna', FALSE ,'coche de manoli',1, 20, 2,2,2);
INSERT INTO averias VALUES (  3,'BAJA',50.0,'cambio de puerta', FALSE ,'coche de david',1, 30, 3,3,3);
INSERT INTO averias VALUES (  4,'BAJA',50.0,'paragolpes', FALSE ,'coche de manoli',1, 20, 2,2,2);

