
/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Averia;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.Vehiculo;
import org.springframework.samples.petclinic.repository.AveriaRepository;
import org.springframework.samples.petclinic.repository.CitaRepository;
import org.springframework.samples.petclinic.repository.ClienteRepository;
import org.springframework.samples.petclinic.repository.VehiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class ClienteService {

	private ClienteRepository	clienteRepository;
	private VehiculoRepository	vehiculoRepository;
	private CitaRepository		citaRepository;
	private AveriaRepository	averiaRepository;

	@Autowired
	private UsuarioService		usuarioService;

	@Autowired
	private AuthoritiesService	authoritiesService;


	@Autowired

	public ClienteService(final ClienteRepository clienteRepository, final CitaRepository citaRepository, final VehiculoRepository vehiculoRepository) {
		this.clienteRepository = clienteRepository;
		this.citaRepository = citaRepository;
		this.vehiculoRepository = vehiculoRepository;
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

	@Transactional(readOnly = true)
	public Cita findCitaById(final int id) throws DataAccessException {
		return this.citaRepository.findCitaById(id);
	}

	@Transactional(readOnly = true)
	public Collection<Averia> findAveriaByVehiculoId(final int id) throws DataAccessException {
		return this.averiaRepository.findAveriasByVehiculoId(id);
	}

	@Transactional
	public void saveCliente(final Cliente cliente) throws DataAccessException {
		// creating owner
		this.clienteRepository.save(cliente);
		// creating user
		this.usuarioService.saveUsuario(cliente.getUsuario());
		// creating authorities
		this.authoritiesService.saveAuthorities(cliente.getUsuario().getNombreUsuario(), "cliente");
	}

	@Transactional(readOnly = true)
	public Integer findIdByUsername(final String username) throws DataAccessException {

		return this.clienteRepository.findIdByUsername(username);
	}

	@Transactional(readOnly = true)
	public Collection<Vehiculo> findVehiculosByClienteId(final Integer idCliente) throws DataAccessException {
		return this.vehiculoRepository.findByClienteId(idCliente);

	}
}
