
package org.springframework.samples.talleres.web.integracion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.talleres.model.Cliente;
import org.springframework.samples.talleres.model.TipoVehiculo;
import org.springframework.samples.talleres.model.Vehiculo;
import org.springframework.samples.talleres.service.ClienteService;
import org.springframework.samples.talleres.web.PetController;
import org.springframework.samples.talleres.web.VehiculoController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

/**
 * Test class for the {@link PetController}
 *
 * @author Colin But
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VehiculoControllerIntegracionTest {

	@Autowired
	private VehiculoController vehiculoController;

	@Autowired
	private ClienteService clienteService;

	@Test
	@WithMockUser(value = "manolo", authorities = { "cliente" })
	void testShowVehiculoList() throws Exception {

		Map<String, Object> model = new ModelMap();

		Authentication principal = SecurityContextHolder.getContext().getAuthentication();

		String res = vehiculoController.showVehiculoList(principal, model);

		assertEquals(res, "vehiculos/vehiculoList");

	}

	@Test
	@WithMockUser(value = "manolo", authorities = { "cliente" })
	void testShowVehiculoDetalle() throws Exception {

		Authentication principal = SecurityContextHolder.getContext().getAuthentication();

		Map<String, Object> model = new ModelMap();

		String res = vehiculoController.showVehiculoDetalle(1, principal, model);

		assertEquals(res, "vehiculos/vehiculoEnDetalle");

	}

	@Test
	@WithMockUser(value = "manoli", authorities = { "cliente" })
	void testShowVehiculoDetalleNegativo() throws Exception {

		Authentication principal = SecurityContextHolder.getContext().getAuthentication();

		Map<String, Object> model = new ModelMap();

		String res = vehiculoController.showVehiculoDetalle(1, principal, model);

		assertEquals(res, "exception");

	}
	
	@Test
	@WithMockUser(value = "lolo", authorities = { "mecanico" })
	void testShowVehiculoMecanicoDetalle() throws Exception {

		Authentication principal = SecurityContextHolder.getContext().getAuthentication();

		Map<String, Object> model = new ModelMap();

		String res = vehiculoController.showVehiculoMecanicoDetalle(2, principal, model);

		assertEquals(res, "vehiculos/vehiculoEnDetalle");

	}

	@Test
	@WithMockUser(value = "paco", authorities = { "mecanico" })
	void testShowVehiculoMecanicoDetalleNegativo() throws Exception {

		Authentication principal = SecurityContextHolder.getContext().getAuthentication();

		Map<String, Object> model = new ModelMap();

		String res = vehiculoController.showVehiculoMecanicoDetalle(2, principal, model);

		assertEquals(res, "exception");

	}

	@Test
	void testInitCreationVehiculoForm() throws Exception {

		Cliente cliente = this.clienteService.findClienteById(1);

		ModelMap model = new ModelMap();

		String view = this.vehiculoController.vehiculoCreation(cliente, model);

		assertEquals(view, "vehiculos/crearVehiculo");
		assertNotNull(model.get("vehiculo"));
	}

	@Test
	@WithMockUser(value = "manolo", authorities = { "cliente" })
	void testCreationVehiculoForm() throws Exception {

		Authentication principal = SecurityContextHolder.getContext().getAuthentication();

		Cliente cliente = this.clienteService.findClienteById(1);

		Vehiculo vehiculo = new Vehiculo();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String string = "2010-09-15";
		Date fecha = sdf.parse(string);

		vehiculo.setMatricula("1234HGF");
		vehiculo.setModelo("mercedes A3");
		vehiculo.setFechaMatriculacion(fecha);
		vehiculo.setKilometraje(1000);
		vehiculo.setActivo(true);
		vehiculo.setTipoVehiculo(TipoVehiculo.turismo);
		vehiculo.setCliente(cliente);
		vehiculo.setId(20);

		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");

		Map<String, Object> model = new ModelMap();

		String view = this.vehiculoController.vehiculoCreation(principal, vehiculo, bindingResult, model);

		assertEquals(view, "redirect:/cliente/vehiculos");
	}

	@Test
	@WithMockUser(value = "manolo", authorities = { "cliente" })
	void testCreationVehiculoNegativoForm() throws Exception {

		Authentication principal = SecurityContextHolder.getContext().getAuthentication();

		Cliente cliente = this.clienteService.findClienteById(1);

		Vehiculo vehiculo = new Vehiculo();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String string = "2010-09-15";
		Date fecha = sdf.parse(string);

		vehiculo.setMatricula("1234HGF");
		vehiculo.setModelo("mercedes A3");
		vehiculo.setFechaMatriculacion(fecha);
		vehiculo.setKilometraje(-1000);
		vehiculo.setActivo(true);
		vehiculo.setTipoVehiculo(TipoVehiculo.turismo);
		vehiculo.setCliente(cliente);
		vehiculo.setId(20);

		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
		bindingResult.reject("kilometraje", "Es negativo");

		Map<String, Object> model = new ModelMap();

		String view = this.vehiculoController.vehiculoCreation(principal, vehiculo, bindingResult, model);

		assertEquals(view, "vehiculos/crearVehiculo");
	}

	@Test
	@WithMockUser(value = "manolo", authorities = { "cliente" })
	void testInitUpdateVehiculo() throws Exception {

		Authentication principal = SecurityContextHolder.getContext().getAuthentication();

		ModelMap model = new ModelMap();

		String view = this.vehiculoController.updateVehiculo(1, principal, model);

		assertEquals(view, "vehiculos/vehiculoUpdate");

	}

	@Test
	@WithMockUser(value = "manoli", authorities = { "cliente" })
	void testInitUpdateVehiculoNegativo() throws Exception {

		Authentication principal = SecurityContextHolder.getContext().getAuthentication();

		ModelMap model = new ModelMap();

		String view = this.vehiculoController.updateVehiculo(1, principal, model);

		assertEquals(view, "exception");

	}

	@Test
	@WithMockUser(value = "manolo", authorities = { "cliente" })
	void testUpdateVehiculoForm() throws Exception {

		Authentication principal = SecurityContextHolder.getContext().getAuthentication();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String string = "2010-09-15";
		Date fecha = sdf.parse(string);

		Vehiculo vehiculo = new Vehiculo();

		vehiculo.setMatricula("1234HGF");
		vehiculo.setModelo("mercedes A3");
		vehiculo.setFechaMatriculacion(fecha);
		vehiculo.setKilometraje(1000);
		vehiculo.setTipoVehiculo(TipoVehiculo.turismo);

		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");

		ModelMap model = new ModelMap();

		String view = this.vehiculoController.updateVehiculo(vehiculo, bindingResult, 1, principal, model);

		assertEquals(view, "redirect:/cliente/vehiculos");
	}

	@Test
	@WithMockUser(value = "manolo", authorities = { "cliente" })
	void testUpdateVehiculoNegativoForm() throws Exception {

		Authentication principal = SecurityContextHolder.getContext().getAuthentication();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String string = "2010-09-15";
		Date fecha = sdf.parse(string);

		Vehiculo vehiculo = new Vehiculo();

		vehiculo.setMatricula("1234HGF");
		vehiculo.setModelo("mercedes A3");
		vehiculo.setFechaMatriculacion(fecha);
		vehiculo.setKilometraje(-1000);
		vehiculo.setTipoVehiculo(TipoVehiculo.turismo);

		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
		bindingResult.reject("kilometraje", "Es negativo");

		ModelMap model = new ModelMap();

		String view = this.vehiculoController.updateVehiculo(vehiculo, bindingResult, 1, principal, model);

		assertEquals(view, "vehiculos/vehiculoUpdate");
	}

	@Test
	@WithMockUser(value = "manolo", authorities = { "cliente" })
	void testInitDisableVehiculo() throws Exception {

		Authentication principal = SecurityContextHolder.getContext().getAuthentication();

		ModelMap model = new ModelMap();

		String view = this.vehiculoController.deshabilitarVehiculo(4, principal, model);

		assertEquals(view, "vehiculos/disableVehiculo");

	}

	@Test
	@WithMockUser(value = "manoli", authorities = { "cliente" })
	void testInitDisableVehiculoNegativo() throws Exception {

		Authentication principal = SecurityContextHolder.getContext().getAuthentication();

		ModelMap model = new ModelMap();

		String view = this.vehiculoController.deshabilitarVehiculo(4, principal, model);

		assertEquals(view, "exception");

	}

	@Test
	@WithMockUser(value = "manolo", authorities = { "cliente" })
	void testDisableVehiculoForm() throws Exception {

		Authentication principal = SecurityContextHolder.getContext().getAuthentication();

		ModelMap model = new ModelMap();

		String view = this.vehiculoController.deshabilitarVehiculoForm(4, principal, model);

		assertEquals(view, "redirect:/cliente/vehiculos");
	}

	@Test
	@WithMockUser(value = "manoli", authorities = { "cliente" })
	void testDisableVehiculoFormNegativo() throws Exception {

		Authentication principal = SecurityContextHolder.getContext().getAuthentication();

		ModelMap model = new ModelMap();

		String view = this.vehiculoController.deshabilitarVehiculoForm(4, principal, model);

		assertEquals(view, "exception");
	}
}