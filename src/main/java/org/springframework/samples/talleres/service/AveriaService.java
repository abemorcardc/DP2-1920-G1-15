
package org.springframework.samples.talleres.service;

import java.util.Collection;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.talleres.model.Averia;
import org.springframework.samples.talleres.repository.AveriaRepository;
import org.springframework.stereotype.Service;


@Service
public class AveriaService {

	private AveriaRepository averiaRepository;


	@Autowired
	public AveriaService(final AveriaRepository averiaRepository) {
		this.averiaRepository = averiaRepository;
	}
	
	public Averia findAveriaById(final int id) {
		return this.averiaRepository.findById(id);
	}

	public Collection<Averia> findAveriaByVehiculoId(final int id) throws DataAccessException {
		return this.averiaRepository.findAveriasByVehiculoId(id);
	}
	public Collection<Averia> findAveriasByCitaId(final Integer citaId) {
		return this.averiaRepository.findAveriasByCitaId(citaId);
	}
	
	public void saveAveria(@Valid final Averia averia) throws DataAccessException {
			this.averiaRepository.save(averia);
	}
}

