
package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.repository.CitaRepository;
import org.springframework.stereotype.Service;

@Service
public class CitaService {

	private CitaRepository citaRepository;
	//	private AveriaRepository	averiaRepository;

	//Como las averias se listan al acceder desde una cita, el repositorio de averias se incluye en este servicio

	@Autowired
	public CitaService(final CitaRepository citaRepository) {//, final AveriaRepository averiaRepository) {
		this.citaRepository = citaRepository;
		//		this.averiaRepository = averiaRepository;
	}


	@Transactional(readOnly = true)
	public Collection<Cita> findCitas() throws DataAccessException {
		return this.citaRepository.findAll();	//todas. por si salia algo en la pag
	}

}
