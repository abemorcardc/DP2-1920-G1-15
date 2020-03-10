
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cliente;

public interface ClienteRepository {

	Collection<Cliente> findByApellidos(String apellidos) throws DataAccessException;
	Cliente findById(int id) throws DataAccessException;
	void save(Cliente cliente) throws DataAccessException;

}
