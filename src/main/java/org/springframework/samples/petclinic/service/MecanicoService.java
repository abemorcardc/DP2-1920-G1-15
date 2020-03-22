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
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Mecanico;
import org.springframework.samples.petclinic.repository.CitaRepository;
import org.springframework.samples.petclinic.repository.MecanicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class MecanicoService {

	private MecanicoRepository	mecanicoRepository;
	private CitaRepository		citaRepository;

	@Autowired
	private UsuarioService		usuarioService;

	@Autowired
	private AuthoritiesService	authoritiesService;


	@Autowired
	public MecanicoService(final CitaRepository citaRepository, final MecanicoRepository mecanicoRepository) {
		this.citaRepository = citaRepository;
		this.mecanicoRepository = mecanicoRepository;
	}

	@Transactional(readOnly = true)
	public Collection<Cita> findCitasByMecanicoId(final Integer mecanicoId) throws DataAccessException {
		return this.citaRepository.findByMecanicoId(mecanicoId);
	}

	@Transactional(readOnly = true)
	public Integer findIdByUsername(final String username) throws DataAccessException {
		return this.mecanicoRepository.findIdByUsername(username);
	}

	@Transactional(readOnly = true)
	public Collection<Cita> findAll() {
		return this.citaRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Cita findCitaById(final int id) throws DataAccessException {
		return this.citaRepository.findCitaById(id);
	}

	@Transactional(readOnly = true)
	public Mecanico findMecanicoById(final int mecanicoId) {
		return this.mecanicoRepository.findMecanicoById(mecanicoId);
	}

	@Transactional
	public void saveMecanico(final Mecanico mec) throws DataAccessException {
		this.mecanicoRepository.save(mec);
		this.usuarioService.saveUsuario(mec.getUsuario());
		this.authoritiesService.saveAuthorities(mec.getUsuario().getNombreUsuario(), "mecanico");
	}

	public void saveCita(final Cita citaAntigua) {
		this.citaRepository.save(citaAntigua);
	}

}
