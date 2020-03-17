/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import lombok.Getter;
import lombok.Setter;

/**
 * Simple business object representing a pet.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 */
@Entity
@Getter
@Setter
@Table(name = "vehiculos")
public class Vehiculo extends BaseEntity {

	@Column(name = "fecha_matriculacion")
	@Temporal(TemporalType.TIMESTAMP)
	@Past
	@NotNull
	private Date	fechaMatriculacion;

	@Column(name = "tipo_vehiculo")
	@NotNull
	@Enumerated(value = EnumType.STRING)
	private TipoVehiculo	tipoVehiculo;

	@Column(name = "matricula")
	@NotBlank
	private String	matricula;

	@Column(name = "modelo")
	@NotNull
	private String	modelo;

	@Column(name = "kilometraje")
	@NotNull
	private Double	kilometraje;

	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private Cliente	cliente;
}
