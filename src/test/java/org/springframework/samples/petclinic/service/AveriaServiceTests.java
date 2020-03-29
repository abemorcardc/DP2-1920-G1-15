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
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Averia;
import org.springframework.stereotype.Service;

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
class AveriaServiceTests {

	@Autowired
	protected MecanicoService	mecanicoService;
  
	@Autowired
	protected AveriaService		averiaService;
  
	@Autowired
	protected CitaService		citaService;


	// HISTORIA 7
	/*
	 * Escenario positivo: comprobar que el nº es igual al que yo le este dando. El
	 * mecánico obtiene una lista de todas las averías de un vehículo con la cita
	 * correspondiente donde se detectó. Escenario negativo: Un mecánico intenta
	 * listar las averías de un vehículo del que se encarga otro mecánico.
	 */
	@ParameterizedTest
	@CsvSource({
		"1,1", " 2,2"
	})
	void shouldListAllFaultsByVeh(final Integer vehiculoId, final int nAveria) {
		// todas las averias de un vehiculo sea el esperado
		Collection<Averia> averias = this.averiaService.findAveriaByVehiculoId(vehiculoId);

		List<Averia> averiasAux = averias.stream().collect(Collectors.toList());

		Assert.assertEquals(averiasAux.size(), nAveria);

	}

	@ParameterizedTest
	@CsvSource({ "1,2", " 2,3", "3,1" })

	void shouldNotShowFaults(final Integer citaId, final Integer mecanicoId) {
		// si soy el mecanico 1 no puedo ver las averias del mecanico 2
		Collection<Averia> averias = this.averiaService.findAveriasByCitaId(citaId);

		List<Averia> averiasAux = averias.stream().collect(Collectors.toList());

		int cont = 0;
		while (cont < averiasAux.size()) { // para todas las averias de una cita
			Assert.assertNotEquals(averiasAux.get(cont).getMecanico().getId(), mecanicoId);
			cont++;
		}

	}

}
