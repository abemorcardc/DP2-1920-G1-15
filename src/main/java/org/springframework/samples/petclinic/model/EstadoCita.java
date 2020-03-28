package org.springframework.samples.petclinic.model;

import javax.persistence.Table;

@Table(name = "estado_cita")
public enum EstadoCita {

	pendiente, aceptada, cancelada, finalizada
}