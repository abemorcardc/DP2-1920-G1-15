/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.TipoVehiculo;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vehiculo;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.samples.petclinic.service.exceptions.FechaIncorrectaException;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test of the Service and the Repository layer.
 * <p>
 * ClinicServiceSpringDataJpaTests subclasses benefit from the following
 * services provided by the Spring TestContext Framework:
 * </p>
 * <ul>
 * <li><strong>Spring IoC container caching</strong> which spares us unnecessary
 * set up time between test execution.</li>
 * <li><strong>Dependency Injection</strong> of test fixture instances, meaning
 * that we don't need to perform application context lookups. See the use of
 * {@link Autowired @Autowired} on the <code>{@link
 * OwnerServiceTests#clinicService clinicService}</code> instance variable,
 * which uses autowiring <em>by type</em>.
 * <li><strong>Transaction management</strong>, meaning each test method is
 * executed in its own transaction, which is automatically rolled back by
 * default. Thus, even if tests insert or otherwise change database state, there
 * is no need for a teardown or cleanup script.
 * <li>An {@link org.springframework.context.ApplicationContext
 * ApplicationContext} is also inherited and can be used for explicit bean
 * lookup if necessary.</li>
 * </ul>
 *
 * @author Ken Krebs
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Dave Syer
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class VehiculoServiceTests {

	@Autowired
	protected VehiculoService vehiculoService;

	@Autowired
	protected ClienteService clienteService;

	@Test
	void findVehiculoById() {
		Vehiculo vehiculo = this.vehiculoService.findVehiculoById(1);
		assertThat(vehiculo.getMatricula().equals("2345FCL")).isTrue();

		Vehiculo vehiculo2 = this.vehiculoService.findVehiculoById(2);
		assertThat(vehiculo2.getMatricula().equals("2345FCL")).isFalse();
	}

	@Test
	void findVehiculosByClienteId() {
		Collection<Vehiculo> vehiculos = this.vehiculoService.findVehiculosByClienteId(1);
		assertThat(vehiculos.size()).isEqualTo(1);

		Vehiculo[] vehiculoArr = vehiculos.toArray(new Vehiculo[vehiculos.size()]);
		assertThat(vehiculoArr[0].getCliente()).isNotNull();
		assertThat(vehiculoArr[0].getMatricula()).isNotNull();
		assertThat(vehiculoArr[0].getCliente().getId()).isEqualTo(1);
	}

	@Test
	@Transactional
	public void shouldInsertVehiculo() throws ParseException, DataAccessException, FechaIncorrectaException {
		Collection<Vehiculo> vehiculos = this.vehiculoService.findVehiculosByClienteId(1);
		int found = vehiculos.size();

		Cliente cliente = this.clienteService.findClienteById(1);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String string = "2010-09-15";
		Date fecha = sdf.parse(string);

		Vehiculo vehiculo = new Vehiculo();
		vehiculo.setMatricula("1934HGF");
		vehiculo.setModelo("mercedes A3");
		vehiculo.setFechaMatriculacion(fecha);
		vehiculo.setKilometraje(1000);
		vehiculo.setActivo(true);
		vehiculo.setTipoVehiculo(TipoVehiculo.turismo);
		vehiculo.setCliente(cliente);

		this.vehiculoService.saveVehiculo(vehiculo);
		assertThat(vehiculo.getId().longValue()).isNotEqualTo(0);

		vehiculos = this.vehiculoService.findVehiculosByClienteId(1);
		assertThat(vehiculos.size()).isEqualTo(found + 1);
	}

	@Test
	@Transactional
	public void shouldUpdateVehiculo() throws Exception {
		Vehiculo vehiculo = this.vehiculoService.findVehiculoById(1);
		String oldModelo = vehiculo.getModelo();

		String newModelo = oldModelo + "X";
		vehiculo.setModelo(newModelo);
		this.vehiculoService.saveVehiculo(vehiculo);
		
		vehiculo = this.vehiculoService.findVehiculoById(1);
		assertThat(vehiculo.getModelo()).isEqualTo(newModelo);
	}
	
//	@Test
//	@Transactional
//	public void shouldThrowExceptionUpdatingPetsWithTheSameName() {
//		Owner owner6 = this.ownerService.findOwnerById(6);
//		Pet pet = new Pet();
//		pet.setName("wario");
//		Collection<PetType> types = this.petService.findPetTypes();
//		pet.setType(EntityUtils.getById(types, PetType.class, 2));
//		pet.setBirthDate(LocalDate.now());
//		owner6.addPet(pet);
//		
//		Pet anotherPet = new Pet();		
//		anotherPet.setName("waluigi");
//		anotherPet.setType(EntityUtils.getById(types, PetType.class, 1));
//		anotherPet.setBirthDate(LocalDate.now().minusWeeks(2));
//		owner6.addPet(anotherPet);
//		
//		try {
//			petService.savePet(pet);
//			petService.savePet(anotherPet);
//		} catch (DuplicatedPetNameException e) {
//			// The pets already exists!
//			e.printStackTrace();
//		}				
//			
//		Assertions.assertThrows(DuplicatedPetNameException.class, () ->{
//			anotherPet.setName("wario");
//			petService.savePet(anotherPet);
//		});		
//	}

}
