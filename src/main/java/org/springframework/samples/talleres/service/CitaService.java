
package org.springframework.samples.talleres.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.talleres.model.Cita;
import org.springframework.samples.talleres.repository.CitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CitaService {

	private CitaRepository citaRepository;


	/* METODOS DE CLIENTES-CITAS */
	@Autowired
	public CitaService(final CitaRepository citaRepository) {
		this.citaRepository = citaRepository;
	}

	@Transactional(readOnly = true)
	public Collection<Cita> findCitas() throws DataAccessException {
		return this.citaRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Collection<Cita> findCitasByClienteId(final Integer idCliente) throws DataAccessException {
		Collection<Cita> res = this.citaRepository.findCitasByClienteId(idCliente);
		return res;
	}

	@Transactional(readOnly = true)
	public Cita findCitaById(final int id) throws DataAccessException {
		return this.citaRepository.findCitaById(id);
	}

	public void saveCita(final Cita cita) throws DataAccessException {
		this.citaRepository.save(cita);
	}

	public Integer countCitasAceptadasYPendientesByClienteIdAndVehiculoId(final Integer idCliente, final Integer idVehiculo) throws DataAccessException {
		return this.citaRepository.countCitasAceptadasYPendientesByClienteIdAndVehiculoId(idCliente, idVehiculo);
	}

	/*------------ METODOS DE MECANICOS-CITAS----------- */
	@Transactional(readOnly = true)
	public Collection<Cita> findCitasByMecanicoId(final Integer mecanicoId) throws DataAccessException {
		return this.citaRepository.findCitasByMecanicoId(mecanicoId);
	}

	public Collection<Cita> findCitasByVehiculoId(final int vehiculoId) {
		return this.citaRepository.findCitasByVehiculoId(vehiculoId);
	}

	public Collection<Cita> findCitasSinAsignar() throws DataAccessException {
		return this.citaRepository.findCitasSinAsignar();
	}

}
