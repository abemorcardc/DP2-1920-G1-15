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
package org.springframework.samples.talleres.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.talleres.model.Cliente;
import org.springframework.samples.talleres.model.TipoVehiculo;
import org.springframework.samples.talleres.model.Vehiculo;
import org.springframework.samples.talleres.service.ClienteService;
import org.springframework.samples.talleres.service.VehiculoService;
import org.springframework.samples.talleres.service.exceptions.FechaIncorrectaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
		assertThat(vehiculos.size()).isEqualTo(2);

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
}