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

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.talleres.model.Mecanico;
import org.springframework.samples.talleres.repository.MecanicoRepository;

/**
 * Spring Data JPA specialization of the {@link MecanicoRepository} interface
 *
 * @author Michael Isvy
 * @since 15.1.2013
 */

public interface SpringDataMecanicoRepository extends MecanicoRepository, Repository<Mecanico, Integer> {

	@Override
	@Query("SELECT id FROM Mecanico mecanico WHERE mecanico.usuario.nombreUsuario LIKE :username%")
	Integer findMecIdByUsername(@Param("username") String username);

	@Override
	@Query("SELECT mecanico FROM Mecanico mecanico WHERE mecanico.id =:mecanicoId")
	Mecanico findMecanicoById(@Param("mecanicoId") int mecanicoId);

}
