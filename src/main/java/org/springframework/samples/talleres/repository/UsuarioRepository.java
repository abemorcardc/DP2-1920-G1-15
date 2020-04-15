package org.springframework.samples.talleres.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.talleres.model.Usuario;


public interface UsuarioRepository extends  CrudRepository<Usuario, String>{
	
}
