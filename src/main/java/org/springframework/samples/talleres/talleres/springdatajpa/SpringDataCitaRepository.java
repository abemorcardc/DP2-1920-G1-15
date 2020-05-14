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

package org.springframework.samples.talleres.talleres.springdatajpa;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.talleres.model.Cita;
import org.springframework.samples.talleres.repository.CitaRepository;

public interface SpringDataCitaRepository extends CitaRepository, Repository<Cita, Integer> {

	/* METODOS CLIENTES */
	@Override
	@Query("SELECT cita FROM Cita cita WHERE cita.cliente.id =:idCliente")
	Collection<Cita> findCitasByClienteId(Integer idCliente);

	@Override
	@Query("SELECT cita FROM Cita cita WHERE cita.id =:citaId")
	Cita findCitaById(int citaId);

	@Override
	@Query("SELECT COUNT(cita) FROM Cita cita WHERE cita.cliente.id=:idCliente and cita.vehiculo.id=:idVehiculo and (cita.estadoCita='pendiente' or cita.estadoCita='aceptada')")
	Integer countCitasAceptadasYPendientesByClienteIdAndVehiculoId(Integer idCliente, Integer idVehiculo) throws DataAccessException;

	/* METODOS MECANICOS */
	@Override
	@Query("SELECT cita FROM Cita cita WHERE cita.mecanico.id =:mecanicoId")
	Collection<Cita> findCitasByMecanicoId(Integer mecanicoId);

	@Override
	@Query("SELECT cita FROM Cita cita WHERE cita.vehiculo.id =:vehiculoId")
	Collection<Cita> findCitasByVehiculoId(Integer vehiculoId);

	@Override
	@Query("SELECT cita FROM Cita cita WHERE cita.estadoCita='pendiente' and (cita.mecanico.id is null)")
	Collection<Cita> findCitasSinAsignar();
}
