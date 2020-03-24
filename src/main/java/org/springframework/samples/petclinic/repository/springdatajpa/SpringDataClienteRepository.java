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


package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Vehiculo;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.repository.ClienteRepository;
import org.springframework.samples.petclinic.repository.OwnerRepository;

/**
 * Spring Data JPA specialization of the {@link OwnerRepository} interface
 *
 * @author Michael Isvy
 * @since 15.1.2013
 */

public interface SpringDataClienteRepository extends ClienteRepository, Repository<Cliente, Integer> {


	/*
	 * @Override
	 * 
	 * @Query("SELECT DISTINCT cliente FROM Cliente cliente WHERE cliente.apellidos LIKE :apellidos%"
	 * ) Collection<Cliente> findByApellidos(@Param("apellidos") String apellidos);
	 * 
	 * @Override
	 * 
	 * @Query("SELECT cliente FROM Cliente cliente WHERE cliente.id =:id") Cliente
	 * findById(@Param("id") int id);
	 */

	@Override
	@Query("SELECT id FROM Cliente cliente WHERE cliente.usuario.nombreUsuario LIKE :username%")
	Integer findIdByUsername(@Param("username") String username);
	
	@Override
  	@Query("SELECT vehiculo FROM Vehiculo vehiculo WHERE vehiculo.cliente.id=:clienteId")
  	Collection<Vehiculo> findVehiculoByClienteId(Integer clienteId);

	@Override
  	@Query("SELECT vehiculo FROM Vehiculo vehiculo WHERE vehiculo.id=:vehiculoId")
  	Vehiculo findVehiculoById(int vehiculoId);
	
	@Override
	@Query("SELECT vehiculo FROM Vehiculo vehiculo WHERE vehiculo.matricula=:matricula")
	Vehiculo findVehiculoByMatricula(String matricula);

}
