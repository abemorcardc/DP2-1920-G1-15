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



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.Mecanico;
import org.springframework.samples.petclinic.model.TipoCita;
import org.springframework.samples.petclinic.model.Vehiculo;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Integration test of the Service and the Repository layer.
 * <p>
 * ClinicServiceSpringDataJpaTests subclasses benefit from the following services provided
 * by the Spring TestContext Framework:
 * </p>
 * <ul>
 * <li><strong>Spring IoC container caching</strong> which spares us unnecessary set up
 * time between test execution.</li>
 * <li><strong>Dependency Injection</strong> of test fixture instances, meaning that we
 * don't need to perform application context lookups. See the use of
 * {@link Autowired @Autowired} on the <code>{@link
 * ClinicServiceTests#clinicService clinicService}</code> instance variable, which uses
 * autowiring <em>by type</em>.
 * <li><strong>Transaction management</strong>, meaning each test method is executed in
 * its own transaction, which is automatically rolled back by default. Thus, even if tests
 * insert or otherwise change database state, there is no need for a teardown or cleanup
 * script.
 * <li>An {@link org.springframework.context.ApplicationContext ApplicationContext} is
 * also inherited and can be used for explicit bean lookup if necessary.</li>
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
class CitaServiceTests{

	@Autowired
	private CitaService	citaService;

	
	@Test
	void shouldFindCitaWithCorrectId() {
		Cita cita2=this.citaService.findCitaById(2);
		assertEquals(cita2.getDescripcion(), "luna rota");
		assertEquals(cita2.getVehiculo().getMatricula(),"5125DRF");
	}
	

	@Test
	void shouldNotFindCitaWithIncorrectId() {
		assertNull(this.citaService.findCitaById(4));
		
		//assertNotEquals(cita2.getDescripcion(), "luna rota");
	}
	
	
	@Test
	void shouldFindAllCitas() {
		Collection<Cita> citas=this.citaService.findCitas();
		
		assertEquals(citas.size(), 3);
	}
	
	/*@Test
	public void shouldNotFindCitas() {
		assertNull(this.citaService.findCitas());
		
	}
	*/
	

	@Test
	void shouldFindCitasByClienteId() {
		Collection<Cita> citas=this.citaService.findCitasByClienteId(1);
		
		assertEquals(citas.size(),1);
		List<Cita> lista =new ArrayList<>(citas);
		
		assertEquals(lista.get(0).getDescripcion(),"Problemas con el motor");
	}
	
	@Test
	void shouldNotFindCitasByClienteId() {
		Collection<Cita> citas=this.citaService.findCitasByClienteId(1);
		
		List<Cita> lista =new ArrayList<>(citas);
		
		assertNotEquals(lista.get(0).getCliente().getId(),2);
	}

	@ParameterizedTest
	@ValueSource(ints = {
		1, 2, 3
	})
	@Order(2)
	void shouldShowVisit(final Integer mecanicoId) {
		Assertions.assertTrue(mecanicoId > 0 && mecanicoId < 4);

		Cita cita = this.citaService.findCitaById(mecanicoId);

		assertTrue(cita.getDescripcion().contains(" "));
		assertTrue(cita.getId().equals(mecanicoId));
		assertTrue(cita.getCoste() > 0);
		assertTrue(cita.getTiempo() > 0);
		assertEquals(cita.getTipo().getClass(), TipoCita.class);
		assertEquals(cita.getFechaCita().getClass(), LocalDateTime.class);
		assertTrue(cita.isEsUrgente());
		assertEquals(cita.getCliente().getNombre().getClass(), String.class);
		assertEquals(cita.getMecanico().getNombre().getClass(), String.class);
		assertEquals(cita.getVehiculo().getMatricula().getClass(), String.class);

	}

	@Test
	@Order(1)
	void shouldFindSingleVisit() {
		Cita cita = this.citaService.findCitaById(3);
		Assertions.assertTrue(cita.getDescripcion().startsWith("puerta"));
		Assertions.assertEquals(cita.getCoste(), 200.0);
		Assertions.assertNotNull(cita.getTiempo());
		Assertions.assertEquals(cita.getCliente().getClass(), Cliente.class);
		Assertions.assertEquals(cita.getVehiculo().getClass(), Vehiculo.class);
		Assertions.assertEquals(cita.getMecanico().getClass(), Mecanico.class);
		assertTrue(cita.isEsUrgente());
		

	}

	//	@Test
	//	void shouldFindPetWithCorrectId() {
	//		Pet pet7 = this.petService.findPetById(7);
	//		assertThat(pet7.getName()).startsWith("Samantha");
	//		assertThat(pet7.getOwner().getFirstName()).isEqualTo("Jean");
	//
	//	}
	//
	//	@Test
	//	void shouldFindAllPetTypes() {
	//		Collection<PetType> petTypes = this.petService.findPetTypes();
	//
	//		PetType petType1 = EntityUtils.getById(petTypes, PetType.class, 1);
	//		assertThat(petType1.getName()).isEqualTo("cat");
	//		PetType petType4 = EntityUtils.getById(petTypes, PetType.class, 4);
	//		assertThat(petType4.getName()).isEqualTo("snake");
	//	}
	//
	//	@Test
	//	@Transactional
	//	public void shouldInsertPetIntoDatabaseAndGenerateId() {
	//		Owner owner6 = this.ownerService.findOwnerById(6);
	//		int found = owner6.getPets().size();
	//
	//		Pet pet = new Pet();
	//		pet.setName("bowser");
	//		Collection<PetType> types = this.petService.findPetTypes();
	//		pet.setType(EntityUtils.getById(types, PetType.class, 2));
	//		pet.setBirthDate(LocalDate.now());
	//		owner6.addPet(pet);
	//		assertThat(owner6.getPets().size()).isEqualTo(found + 1);
	//
	//            try {
	//                this.petService.savePet(pet);
	//            } catch (DuplicatedPetNameException ex) {
	//                Logger.getLogger(PetServiceTests.class.getName()).log(Level.SEVERE, null, ex);
	//            }
	//		this.ownerService.saveOwner(owner6);
	//
	//		owner6 = this.ownerService.findOwnerById(6);
	//		assertThat(owner6.getPets().size()).isEqualTo(found + 1);
	//		// checks that id has been generated
	//		assertThat(pet.getId()).isNotNull();
	//	}
	//	
	//	@Test
	//	@Transactional
	//	public void shouldThrowExceptionInsertingPetsWithTheSameName() {
	//		Owner owner6 = this.ownerService.findOwnerById(6);
	//		Pet pet = new Pet();
	//		pet.setName("wario");
	//		Collection<PetType> types = this.petService.findPetTypes();
	//		pet.setType(EntityUtils.getById(types, PetType.class, 2));
	//		pet.setBirthDate(LocalDate.now());
	//		owner6.addPet(pet);
	//		try {
	//			petService.savePet(pet);		
	//		} catch (DuplicatedPetNameException e) {
	//			// The pet already exists!
	//			e.printStackTrace();
	//		}
	//		
	//		Pet anotherPetWithTheSameName = new Pet();		
	//		anotherPetWithTheSameName.setName("wario");
	//		anotherPetWithTheSameName.setType(EntityUtils.getById(types, PetType.class, 1));
	//		anotherPetWithTheSameName.setBirthDate(LocalDate.now().minusWeeks(2));
	//		Assertions.assertThrows(DuplicatedPetNameException.class, () ->{
	//			owner6.addPet(anotherPetWithTheSameName);
	//			petService.savePet(anotherPetWithTheSameName);
	//		});		
	//	}
	//
	//	@Test
	//	@Transactional
	//	public void shouldUpdatePetName() throws Exception {
	//		Pet pet7 = this.petService.findPetById(7);
	//		String oldName = pet7.getName();
	//
	//		String newName = oldName + "X";
	//		pet7.setName(newName);
	//		this.petService.savePet(pet7);
	//
	//		pet7 = this.petService.findPetById(7);
	//		assertThat(pet7.getName()).isEqualTo(newName);
	//	}
	//	
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
	//
	//	@Test
	//	@Transactional
	//	public void shouldAddNewVisitForPet() {
	//		Pet pet7 = this.petService.findPetById(7);
	//		int found = pet7.getVisits().size();
	//		Visit visit = new Visit();
	//		pet7.addVisit(visit);
	//		visit.setDescription("test");
	//		this.petService.saveVisit(visit);
	//            try {
	//                this.petService.savePet(pet7);
	//            } catch (DuplicatedPetNameException ex) {
	//                Logger.getLogger(PetServiceTests.class.getName()).log(Level.SEVERE, null, ex);
	//            }
	//
	//		pet7 = this.petService.findPetById(7);
	//		assertThat(pet7.getVisits().size()).isEqualTo(found + 1);
	//		assertThat(visit.getId()).isNotNull();
	//	}
	//
	//	@Test
	//	void shouldFindVisitsByPetId() throws Exception {
	//		Collection<Visit> visits = this.petService.findVisitsByPetId(7);
	//		assertThat(visits.size()).isEqualTo(2);
	//		Visit[] visitArr = visits.toArray(new Visit[visits.size()]);
	//		assertThat(visitArr[0].getPet()).isNotNull();
	//		assertThat(visitArr[0].getDate()).isNotNull();
	//		assertThat(visitArr[0].getPet().getId()).isEqualTo(7);
	//	}

}
