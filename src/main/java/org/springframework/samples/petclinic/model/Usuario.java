package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "usuario")
public class Usuario{
	@Id
	@Column(name="nombre_usuario")
	String nombreUsuario;
	
	String contraseña;
	
	boolean enabled;
}
