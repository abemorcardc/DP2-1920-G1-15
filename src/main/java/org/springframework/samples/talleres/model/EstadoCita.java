package org.springframework.samples.talleres.model;

import javax.persistence.Table;

@Table(name = "estado_cita")
public enum EstadoCita {

	pendiente, aceptada, cancelada, finalizada
}