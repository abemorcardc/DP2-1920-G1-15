/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

/**
 * Simple JavaBean domain object representing an person.
 *
 * @author Ken Krebs
 */
@Entity
@Getter
@Setter
@Table(name = "persona")
public class Persona extends BaseEntity {

	@Column(name = "nombre")
	@NotBlank
	private String nombre;

	@Column(name = "apellidos")
	@NotBlank
	private String apellidos;
	
	@Column(name = "dni")
	@NotBlank
	private String dni;
	
	@Column(name = "direccion")
	@NotBlank
	private String direccion;
	
	@Column(name="telefono")
	@NotBlank
	@Pattern(regexp = "([+][^0][\\d]{0,2})?[ ]?([(][\\d]{0,4}[)])?[ ]?([\\d]{6,10})$")
	private String				telefono;
	
	@Email
	@NotBlank
	private String email;
	

	
}
