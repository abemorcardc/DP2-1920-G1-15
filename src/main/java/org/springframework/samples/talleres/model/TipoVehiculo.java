
package org.springframework.samples.talleres.model;

import javax.persistence.Table;


@Table(name = "tipo_vehiculo")
public enum TipoVehiculo {
	turismo, deportivo, todoterreno, camion, furgoneta

}
