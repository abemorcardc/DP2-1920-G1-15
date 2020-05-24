
package org.springframework.samples.talleres.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.talleres.model.Averia;

public interface AveriaRepository {

	List<Averia> findAll() throws DataAccessException;

	Averia findById(int id) throws DataAccessException;

	void save(Averia averia) throws DataAccessException;

	Collection<Averia> findAveriasByVehiculoId(int vehiculoId) throws DataAccessException;

	Collection<Averia> findAveriasByCitaId(Integer citaId) throws DataAccessException;

}
