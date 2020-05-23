
package org.springframework.samples.talleres.web.integracion;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.talleres.model.Averia;
import org.springframework.samples.talleres.model.Cliente;
import org.springframework.samples.talleres.model.Complejidad;
import org.springframework.samples.talleres.model.Mecanico;
import org.springframework.samples.talleres.web.AveriaController;
import org.springframework.samples.talleres.web.PetController;
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
class AveriaControllerIntegracionTest {

	@Autowired
	private AveriaController averiaController;

	@WithMockUser(value = "manolo", authorities = { "cliente" })
	@Test
	void testShowCliAverListByVeh() throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		int vehiculoId = 1;
		Principal principal = SecurityContextHolder.getContext().getAuthentication();

		String view = this.averiaController.showCliAverListByVeh(principal, model, vehiculoId);

		Assertions.assertEquals(view, "averias/CliAveriasDeVehiculoList");
	}

	@WithMockUser(value = "manolo", authorities = { "cliente" })
	@Test
	void testShowCliAverListByVehError() throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		int vehiculoId = 3;
		Principal principal = SecurityContextHolder.getContext().getAuthentication();

		String view = this.averiaController.showCliAverListByVeh(principal, model, vehiculoId);

		Assertions.assertEquals(view, "exception");
	}

	@WithMockUser(value = "paco", authorities = { "mecanico" })
	@Test
	void testShowMecAverListByVeh() throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		int vehiculoId = 1;
		Principal principal = SecurityContextHolder.getContext().getAuthentication();

		String view = this.averiaController.showMecAverListByVeh(principal, model, vehiculoId);

		Assertions.assertEquals(view, "averias/MecAveriasDeVehiculoList");
	}

	@WithMockUser(value = "paco", authorities = { "mecanico" })
	@Test
	void testShowMecAverListByVehError() throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		int vehiculoId = 3;
		Principal principal = SecurityContextHolder.getContext().getAuthentication();

		String view = this.averiaController.showMecAverListByVeh(principal, model, vehiculoId);

		Assertions.assertEquals(view, "exception");
	}

	// Tests Historia 9 (Abel y Javi) ------------------

	@Test
	@WithMockUser(value = "paco", authorities = { "mecanico" })
	void testInitUpdateAveria() throws Exception {

		Authentication principal = SecurityContextHolder.getContext().getAuthentication();

		ModelMap model = new ModelMap();

		String view = this.averiaController.updateAveria(1, 1, principal, model);

		Assertions.assertEquals(view, "averias/averiaUpdate");

	}

	@Test
	@WithMockUser(value = "lolo", authorities = { "mecanico" })
	void testInitUpdateAveriaNegativo() throws Exception {

		Authentication principal = SecurityContextHolder.getContext().getAuthentication();

		ModelMap model = new ModelMap();

		String view = this.averiaController.updateAveria(1, 1, principal, model);

		Assertions.assertEquals(view, "exception");

	}

	@Test
	@WithMockUser(value = "paco", authorities = { "mecanico" })
	void testUpdateAveriaForm() throws Exception {

		Authentication principal = SecurityContextHolder.getContext().getAuthentication();

		Averia averia = new Averia();

		averia.setNombre("Rueda pocha");
		averia.setDescripcion("La rueda esta pocha");
		averia.setCoste(100.0);
		averia.setTiempo(20);
		averia.setPiezasNecesarias(10);
		averia.setEstaReparada(false);
		averia.setComplejidad(Complejidad.ALTA);

		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");

		ModelMap model = new ModelMap();

		String view = this.averiaController.updateVehiculo(averia, bindingResult, 1, 1, principal, model);

		Assertions.assertEquals(view, "redirect:/mecanicos/vehiculos/{vehiculoId}/averia");
	}

	@Test
	@WithMockUser(value = "paco", authorities = { "mecanico" })
	void testUpdateAveriaFormNegativo() throws Exception {

		Authentication principal = SecurityContextHolder.getContext().getAuthentication();

		Averia averia = new Averia();

		averia.setNombre("Rueda pocha");
		averia.setDescripcion("La rueda esta pocha");
		averia.setCoste(-100.0);
		averia.setTiempo(20);
		averia.setPiezasNecesarias(10);
		averia.setEstaReparada(false);
		averia.setComplejidad(Complejidad.ALTA);

		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
		bindingResult.reject("coste", "Es negativo");

		ModelMap model = new ModelMap();

		String view = this.averiaController.updateVehiculo(averia, bindingResult, 1, 1, principal, model);

		Assertions.assertEquals(view, "averias/averiaUpdate");
	}

	// Historia 8
	@WithMockUser(value = "paco", roles = "mecanico")
	@Test
	void testMecAveriasShow() throws Exception {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> model = new HashMap<String, Object>();

		String view = this.averiaController.showMecAverByVeh(principal, model, 1);

		Assertions.assertEquals(view, "averias/MecanicoAveriaShow");

	}

	// Historia 8
	@WithMockUser(value = "paco", roles = "mecanico")
	@Test
	void testMecAveriasShowError() throws Exception {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> model = new HashMap<String, Object>();
		int averiaId = 3;

		String view = this.averiaController.showMecAverByVeh(principal, model, averiaId);

		Assertions.assertEquals(view, "exception");

	}

	// Historia 6
	@WithMockUser(value = "manolo", roles = "cliente")
	@Test
	void testCliAveriasShow() throws Exception {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> model = new HashMap<String, Object>();

		String view = this.averiaController.showCliAverByVeh(principal, model, 1);

		Assertions.assertEquals(view, "averias/ClienteAveriaShow");

	}

	// Historia 6
	@WithMockUser(value = "manolo", roles = "cliente")
	@Test
	void testCliAveriasShowError() throws Exception {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> model = new HashMap<String, Object>();

		String view = this.averiaController.showCliAverByVeh(principal, model, 2);

		Assertions.assertEquals(view, "exception");

	}

	// Historia 10
	@WithMockUser(value = "paco", roles = "mecanico")
	@Test
	void testMecAveriasInitCreation() throws Exception {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> model = new HashMap<String, Object>();

		Mecanico mecanico = new Mecanico();

		mecanico.setApellidos("Naranjo");
		mecanico.setAveriasArregladas(12);
		mecanico.setDireccion("C/Esperanza");
		mecanico.setDni("21154416G");
		mecanico.setEmail("PacoTalleres@gmail.com");
		mecanico.setExperiencia("ninguna");
		mecanico.setNombre("Paco");
		mecanico.setTelefono("666973647");
		mecanico.setTitulaciones("Fp de mecanico");

		String view = this.averiaController.initAveriaCreationForm(principal, mecanico, model, 1);

		Assertions.assertEquals(view, "averias/crearAveria");

	}

	@WithMockUser(value = "paco", roles = "mecanico")
	@Test
	void testMecAveriasCreation() throws Exception {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> model = new HashMap<String, Object>();

		Averia averia = new Averia();

		averia.setComplejidad(Complejidad.BAJA);
		averia.setCoste(50.0);
		averia.setDescripcion("cambio de bujia");
		averia.setEstaReparada(false);
		averia.setNombre("coche de manolo");
		averia.setPiezasNecesarias(1);
		averia.setTiempo(100);

		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");

		String view = this.averiaController.AveriaCreation(principal, averia, result, 1, 1, model);

		Assertions.assertEquals(view, "redirect:/mecanicos/vehiculos/{vehiculoId}/averia");

	}

	@WithMockUser(value = "paco", roles = "mecanico")
	@Test
	void testMecAveriasVehiculoCreation() throws Exception {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> model = new HashMap<String, Object>();

		Cliente cliente = new Cliente();

		cliente.setApellidos("Martín");
		cliente.setDireccion("C/Tarfia");
		cliente.setDni("77844576X");
		cliente.setEmail("Manolo72@gmail.com");
		cliente.setId(1);
		cliente.setNombre("Manolo");
		cliente.setTelefono("608555102");

		String view = this.averiaController.CitaVehiculoCreationForm(principal, cliente, 1, model);

		Assertions.assertEquals(view, "averias/citasDelVehiculo");

	}

	@WithMockUser(value = "paco", roles = "mecanico")
	@Test
	void testMecAveriasInitCreationError() throws Exception {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> model = new HashMap<String, Object>();
		int vehiculoId = 3;

		Mecanico mecanico = new Mecanico();

		mecanico.setApellidos("Naranjo");
		mecanico.setAveriasArregladas(12);
		mecanico.setDireccion("C/Esperanza");
		mecanico.setDni("21154416G");
		mecanico.setEmail("PacoTalleres@gmail.com");
		mecanico.setExperiencia("ninguna");
		mecanico.setNombre("Paco");
		mecanico.setTelefono("666973647");
		mecanico.setTitulaciones("Fp de mecanico");

		String view = this.averiaController.initAveriaCreationForm(principal, mecanico, model, vehiculoId);

		Assertions.assertEquals(view, "exception");

	}

	@WithMockUser(value = "paco", roles = "mecanico")
	@Test
	void testMecAveriasCreationNegativo() throws Exception {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> model = new HashMap<String, Object>();

		Averia averia = new Averia();

		averia.setComplejidad(Complejidad.BAJA);
		averia.setCoste(-50.0);
		averia.setDescripcion("cambio de bujia");
		averia.setEstaReparada(false);
		averia.setNombre("coche de manolo");
		averia.setPiezasNecesarias(1);
		averia.setTiempo(100);

		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		result.reject("coste", "Es negativo");

		String view = this.averiaController.AveriaCreation(principal, averia, result, 1, 1, model);

		Assertions.assertEquals(view, "averias/crearAveria");

	}

	@WithMockUser(value = "paco", roles = "mecanico")
	@Test
	void testMecAveriasVehiculoCreationError() throws Exception {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> model = new HashMap<String, Object>();
		int vehiculoId = 3;
		Cliente cliente = new Cliente();

		cliente.setApellidos("Martín");
		cliente.setDireccion("C/Tarfia");
		cliente.setDni("77844576X");
		cliente.setEmail("Manolo72@gmail.com");
		cliente.setId(1);
		cliente.setNombre("Manolo");
		cliente.setTelefono("608555102");

		String view = this.averiaController.CitaVehiculoCreationForm(principal, cliente, vehiculoId, model);

		Assertions.assertEquals(view, "exception");

	}

}