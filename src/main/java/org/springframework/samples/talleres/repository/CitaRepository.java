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

package org.springframework.samples.talleres.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.talleres.model.Cita;

/**
 * Repository class for <code>Cita</code> domain objects All method names are
 * compliant with Spring Data naming conventions so this interface can easily be
 * extended for Spring Data See here:
 * http://static.springsource.org/spring-data/jpa/docs/current/reference/html/jpa.repositories.html#jpa.query-methods.query-creation
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 */
public interface CitaRepository {

	void save(Cita cita);//throws FechaEnFuturoException;
	Cita findCitaById(int citaId);

	//metodos clientes-citas

	Collection<Cita> findCitasByClienteId(Integer idCliente) throws DataAccessException;

	Collection<Cita> findAll() throws DataAccessException;

	Integer countCitasAceptadasYPendientesByClienteIdAndVehiculoId(Integer idCliente, Integer idVehiculo) throws DataAccessException;

	//metodos mecanicos-citas
	Collection<Cita> findCitasByMecanicoId(Integer mecanicoId) throws DataAccessException;
	Collection<Cita> findCitasByVehiculoId(Integer vehiculoId) throws DataAccessException;
	Collection<Cita> findCitasSinAsignar() throws DataAccessException;

}
