
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Vehiculo;
import org.springframework.samples.petclinic.repository.VehiculoRepository;
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
	public Collection<Vehiculo> findVehiculos() throws DataAccessException {
		return this.vehiculoRepository.findAll();
	}

	//	@Transactional(readOnly = true)
	//	public Collection<TipoVehiculo> findTipoVehiculo() throws DataAccessException {
	//		return vehiculoRepository.findTiposVehiculo();
	//	}

}
