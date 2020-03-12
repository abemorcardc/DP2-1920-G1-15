
package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.repository.CitaRepository;
import org.springframework.stereotype.Service;

@Service
public class CitaService {

	private CitaRepository citaRepository;

	@Autowired
	public CitaService(final CitaRepository citaRepository) {
		this.citaRepository = citaRepository;
	}

	/*
	 * @Transactional(readOnly = true) public Collection<Cita> findCitas() throws
	 * DataAccessException { int idCli = 0; return
	 * this.citaRepository.findAll(idCli); }
	 */

}
