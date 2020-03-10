package org.springframework.samples.petclinic.model;

import javax.persistence.Table;

@Table(name = "tipo_cita")
public enum TipoCita {
	
	REVISION,REPARACION,PREPARACION_ITV,MODIFICACION

}
