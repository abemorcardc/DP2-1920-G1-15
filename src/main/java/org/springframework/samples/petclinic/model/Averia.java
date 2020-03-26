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

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;



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
@Table(name = "averias")
public class Averia extends BaseEntity {

	@Column(name = "nombre")
	@NotBlank
	private String		nombre;

	@Column(name = "descripcion")
	@NotBlank
	private String		descripcion;

	@Column(name = "coste")
	@NotNull
	private Double		coste;

	@Column(name = "tiempo")
	//@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Integer		tiempo;

	@Column(name = "piezas_necesarias")
	@NotNull
	private Integer		piezasNecesarias;


	@Column(name = "complejidad")
	@NotNull
	@Enumerated(value = EnumType.STRING)
	private Complejidad	complejidad;

	@Column(name = "reparada")
	@NotNull
	private boolean		estaReparada;

	@ManyToOne
	@JoinColumn(name = "vehiculo_id")
	private Vehiculo	vehiculo;

	@ManyToOne
	@JoinColumn(name = "mecanico_id")
	private Mecanico	mecanico;

	@ManyToOne
	@JoinColumn(name = "cita_id")
	private Cita		cita;


}
