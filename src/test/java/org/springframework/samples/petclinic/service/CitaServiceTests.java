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

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.Mecanico;
import org.springframework.samples.petclinic.model.TipoCita;
import org.springframework.samples.petclinic.model.Vehiculo;
import org.springframework.stereotype.Service;

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
class CitaServiceTests {

	@Autowired
	protected CitaService citaService;


	@ParameterizedTest
	@ValueSource(ints = {
		1, 2, 3
	})
	@Order(2)
	void shouldShowVisit(final Integer mecanicoId) {
		Assertions.assertTrue(mecanicoId > 0 && mecanicoId < 4);

		Cita cita = this.citaService.findCitaById(mecanicoId);

		Assert.assertTrue(cita.getDescripcion().contains(" "));
		Assert.assertTrue(cita.getId().equals(mecanicoId));
		Assert.assertTrue(cita.getCoste() > 0);
		Assert.assertTrue(cita.getTiempo() > 0);
		Assert.assertEquals(cita.getTipo().getClass(), TipoCita.class);
		Assert.assertEquals(cita.getFechaCita().getClass(), LocalDateTime.class);
		Assert.assertTrue(cita.isEsUrgente());
		Assert.assertTrue(cita.isEsAceptado());
		Assert.assertEquals(cita.getCliente().getNombre().getClass(), String.class);
		Assert.assertEquals(cita.getMecanico().getNombre().getClass(), String.class);
		Assert.assertEquals(cita.getVehiculo().getMatricula().getClass(), String.class);

	}

	@Test
	@Order(1)
	void shouldFindSingleVisit() {
		Cita cita = this.citaService.findCitaById(4);
		Assertions.assertTrue(cita.getDescripcion().startsWith("parachoques"));
		Assertions.assertEquals(cita.getCoste(), 200.0);
		Assertions.assertNotNull(cita.getTiempo());
		Assertions.assertEquals(cita.getCliente().getClass(), Cliente.class);
		Assertions.assertEquals(cita.getVehiculo().getClass(), Vehiculo.class);
		Assertions.assertEquals(cita.getMecanico().getClass(), Mecanico.class);
		Assert.assertFalse(cita.isEsUrgente());
		Assert.assertTrue(cita.isEsAceptado());

	}

}
