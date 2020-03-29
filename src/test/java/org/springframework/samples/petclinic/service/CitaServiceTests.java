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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.talleres.model.Cita;
import org.springframework.samples.talleres.model.Cliente;
import org.springframework.samples.talleres.model.Mecanico;
import org.springframework.samples.talleres.model.Vehiculo;
import org.springframework.samples.talleres.service.CitaService;
import org.springframework.stereotype.Service;
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
 * ClinicServiceTests#clinicService clinicService}</code> instance variable,
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

class CitaServiceTests {

	@Autowired
	private CitaService citaService;

	@Test
	void shouldFindCitaWithCorrectId() {
		Cita cita2 = this.citaService.findCitaById(2);
		Assertions.assertEquals(cita2.getDescripcion(), "luna rota");
		Assertions.assertEquals(cita2.getVehiculo().getMatricula(), "5125DRF");
	}

	@Test
	void shouldNotFindCitaWithIncorrectId() {
		Assertions.assertNull(this.citaService.findCitaById(4));

		// assertNotEquals(cita2.getDescripcion(), "luna rota");
	}

	@Test
	void shouldFindAllCitas() {
		Collection<Cita> citas = this.citaService.findCitas();

		Assertions.assertEquals(citas.size(), 3);
	}

	/*
	 * @Test public void shouldNotFindCitas() {
	 * assertNull(this.citaService.findCitas());
	 *
	 * }
	 */

	@Test
	void shouldFindCitasByClienteId() {
		Collection<Cita> citas = this.citaService.findCitasByClienteId(1);

		Assertions.assertEquals(citas.size(), 1);
		List<Cita> lista = new ArrayList<>(citas);

		Assertions.assertEquals(lista.get(0).getDescripcion(), "Problemas con el motor");
	}

	@Test
	void shouldNotFindCitasByClienteId() {
		Collection<Cita> citas = this.citaService.findCitasByClienteId(1);

		List<Cita> lista = new ArrayList<>(citas);

		Assertions.assertNotEquals(lista.get(0).getCliente().getId(), 2);
	}

	// HISTORIA 12
	/*
	 * Escenario positivo: El mecánico quiere ver todos los detalles de una cita y
	 * al mostrarla ve que está acordada la cita el lunes a las 12:30, que el
	 * vehículo del cliente no arranca, el tiempo estimado de la cita es de 5
	 * minutos lo cual tendrá un coste asociado y que es muy urgente. Escenario
	 * negativo: El mecánico intenta ver los detalles de una cita que no es suya y
	 * no puede porque no tiene acceso.
	 */
	
//	@ParameterizedTest
//	@ValueSource(ints = { 1, 2, 3 })
//
//	void shouldNotShowVisit(final Integer mecanicoId) {
//		// si soy el mecanico 1 no puedo ver las citas del mecanico 2
//		Cita cita = this.citaService.findCitaById(mecanicoId);
//
//		Integer mecanicoIdObtenido = cita.getId();
//
//		Integer idMecanicoAleatorio = (int) (Math.random() * 10) + 1;
//
//		Assert.assertNotEquals(mecanicoIdObtenido, idMecanicoAleatorio);
//	}

	@Test
	void shouldFindSingleVisit() {
		// la cita es la que esta el repositorio
		Cita cita = this.citaService.findCitaById(3);
		Assertions.assertTrue(cita.getDescripcion().startsWith("puerta"));
		Assertions.assertEquals(cita.getCoste(), 200.0);
		Assertions.assertNotNull(cita.getTiempo());
		Assertions.assertEquals(cita.getCliente().getClass(), Cliente.class);
		Assertions.assertEquals(cita.getVehiculo().getClass(), Vehiculo.class);
		Assertions.assertEquals(cita.getMecanico().getClass(), Mecanico.class);
		Assertions.assertTrue(cita.isEsUrgente());

	}

	// HISTORIA 11
	/*
	 * Escenario positivo: El mecánico quiere saber si tiene que atender una cita al
	 * día siguiente a una determinada hora y al listar, le sale todas las citas.
	 * Escenario negativo: El mecánico intenta listar las citas buscando de otro
	 * mecánico, pero no puede hacerlo.
	 */
	@ParameterizedTest
	@CsvSource({ "1,1", "2,1" })
	void shouldListVisits(final Integer mecanicoId, final Integer nCitas) {
		Collection<Cita> citas = this.citaService.findCitasByMecanicoId(mecanicoId);

		List<Cita> citasAux = citas.stream().collect(Collectors.toList());

		Assert.assertTrue(citasAux.size() == nCitas);

	}

	// HISTORIA 13
	/*
	 * Escenario positivo: Al mecánico le surge un imprevisto y no puede atender la
	 * cita, así que modifica la fecha de la cita. Escenario negativo: Un mecánico
	 * introduce una fecha pasada por lo que la cita no se actualiza.
	 */

	@Test
	@Transactional
	public void shouldUpdateVisitDate() throws Exception {
		Cita cita3 = this.citaService.findCitaById(3);

		LocalDateTime newDate = LocalDateTime.parse("2021-12-15T10:15:30");
		cita3.setFechaCita(newDate);
		this.citaService.saveCita(cita3);

		cita3 = this.citaService.findCitaById(3);
		Assert.assertTrue(cita3.getFechaCita().isEqual(newDate));
	}

	// @Test
	// @Transactional
	// public void shouldNotUpdateVisitDate() throws Exception {
	// Cita cita3 = this.citaService.findCitaById(3);
	//
	// LocalDateTime newDate = LocalDateTime.parse("2019-12-15T10:15:30");
	// cita3.setFechaCita(newDate);
	// this.citaService.saveCita(cita3);
	//
	// Assertions.assertThrows(DuplicatedPetNameException.class, () -> {
	// cita3.setFechaCita(newDate);
	// this.citaService.saveCita(cita3);
	// });
	// }
}
