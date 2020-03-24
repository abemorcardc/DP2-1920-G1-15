
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.Vehiculo;
import org.springframework.samples.petclinic.repository.AveriaRepository;
import org.springframework.samples.petclinic.repository.CitaRepository;
import org.springframework.samples.petclinic.repository.ClienteRepository;
import org.springframework.samples.petclinic.repository.VehiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CitaService {

	private ClienteRepository clienteRepository;
	private CitaRepository citaRepository;
	// private AveriaRepository averiaRepository;


	// Como las averias se listan al acceder desde una cita, el repositorio de
	// averias se incluye en este servicio


	@Autowired
	public CitaService(final ClienteRepository clienteRepository, final CitaRepository citaRepository) {// , final AveriaRepository averiaRepository) {
		this.clienteRepository = clienteRepository;
		this.citaRepository = citaRepository;
	}

	@Transactional(readOnly = true)
	public Collection<Cita> findCitas() throws DataAccessException {
		return this.citaRepository.findAll(); // todas. por si salia algo en la pag
	}

	@Transactional(readOnly = true)
	public Collection<Cita> findCitasByClienteId(final Integer idCliente) throws DataAccessException {
		Collection<Cita> res = this.citaRepository.findCitasByClienteId(idCliente);
		return res;

	}

	@Transactional(readOnly = true)
	public Cliente findClienteById(final int id) throws DataAccessException {
		return this.clienteRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Collection<Cliente> findClienteByApellidos(final String apellidos) throws DataAccessException {
		return this.clienteRepository.findByApellidos(apellidos);

	}

	@Transactional(readOnly=true)
	public Cita findCitaById(Integer id) throws DataAccessException{
		return this.citaRepository.findCitaById(id);
	}


	@Transactional(readOnly = true)
	public Integer findIdByUsername(final String username) throws DataAccessException {

		return this.clienteRepository.findIdByUsername(username);
	}

	@Transactional(readOnly = true)
	public Collection<Vehiculo> findVehiculoByClienteId(final int id) throws DataAccessException {
		return this.clienteRepository.findVehiculoByClienteId(id);
	}

	@Transactional(readOnly = true)
	public Vehiculo findVehiculoById(final int id) throws DataAccessException {
		return this.clienteRepository.findVehiculoById(id);
	}

	public void saveCita(@Valid final Cita cita) throws DataAccessException {
		this.citaRepository.save(cita);
	}
	
	public Integer countCitasAceptadasYPendientesByClienteIdAndVehiculoId(Integer idCliente, Integer idVehiculo) throws DataAccessException {
		return this.citaRepository.countCitasAceptadasYPendientesByClienteIdAndVehiculoId(idCliente, idVehiculo);
	}


}
