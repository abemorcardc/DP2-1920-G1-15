package org.springframework.samples.talleres.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id
	@Column(name = "nombre_usuario")
	@NotBlank
	String	nombreUsuario;

	@Column(name = "contra")
	@NotBlank
	String	contra;

	boolean	enabled;

}