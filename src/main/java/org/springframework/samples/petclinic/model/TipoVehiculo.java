package org.springframework.samples.petclinic.model;

import javax.persistence.Table;

@Table(name = "tipo_vehiculo")
public enum TipoVehiculo {
	TURISMO,DEPORTIVO,TODOTERRENO,CAMION,FURGONETA

}
