
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

package org.springframework.samples.talleres.service;

import java.util.Collection;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.talleres.model.Cliente;
import org.springframework.samples.talleres.repository.ClienteRepository;
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

	private ClienteRepository clienteRepository;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private AuthoritiesService authoritiesService;

	@Autowired
	public ClienteService(final ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	
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
	public Integer findIdByUsername(final String username) throws DataAccessException {
		return this.clienteRepository.findIdByUsername(username);
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

	
}
