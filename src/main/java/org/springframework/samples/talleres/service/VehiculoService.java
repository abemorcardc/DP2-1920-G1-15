
package org.springframework.samples.talleres.service;

import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.talleres.model.Vehiculo;
import org.springframework.samples.talleres.repository.VehiculoRepository;
import org.springframework.samples.talleres.service.exceptions.FechaIncorrectaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehiculoService {

	private VehiculoRepository vehiculoRepository;

	@Autowired
	public VehiculoService(final VehiculoRepository vehiculoRepository) {
		this.vehiculoRepository = vehiculoRepository;
	}

	@Transactional(readOnly = true)
	public Vehiculo findVehiculoById(Integer id) throws DataAccessException {
		return this.vehiculoRepository.findVehiculoById(id);
	}

	@Transactional(readOnly = true)
	public Collection<Vehiculo> findVehiculosByClienteId(final Integer idCliente) throws DataAccessException {
		return this.vehiculoRepository.findVehiculoByClienteId(idCliente);
	}
	
	@Transactional(rollbackFor = FechaIncorrectaException.class)
	public void saveVehiculo(@Valid final Vehiculo vehiculo) throws DataAccessException, FechaIncorrectaException {
		if (vehiculo.getFechaMatriculacion().after(new Date())) {
			throw new FechaIncorrectaException();	
		}
		else {
			this.vehiculoRepository.save(vehiculo);
		}
	}
}
