
package org.springframework.samples.talleres.model;

import javax.persistence.Table;

@Table(name = "tipo_cita")
public enum TipoCita {
	//REVISION, REPARACION, PREPARACION_ITV, MODIFICACION
	revision, reparacion, preparacion_itv, modificacion
}
