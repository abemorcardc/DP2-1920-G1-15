
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Averia;
import org.springframework.samples.petclinic.repository.AveriaRepository;
import org.springframework.stereotype.Service;

@Service
public class AveriaService {

	private AveriaRepository averiaRepository;


	@Autowired
	public AveriaService(final AveriaRepository averiaRepository) {
		this.averiaRepository = averiaRepository;
	}

	public Collection<Averia> findAveriaByVehiculoId(final int id) throws DataAccessException {
		return this.averiaRepository.findAveriasByVehiculoId(id);
	}
	public Collection<Averia> findAveriasByCitaId(final Integer citaId) {
		return this.averiaRepository.findAveriasByCitaId(citaId);
	}
}
