
package org.springframework.samples.talleres.web.integracion;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.talleres.model.Averia;
import org.springframework.samples.talleres.model.Complejidad;
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
	
	// Tests Historia 9 (Abel y Javi) ------------------

	@Test
	@WithMockUser(value = "paco", authorities = { "mecanico" })
	void testInitUpdateAveria() throws Exception {

		Authentication principal = SecurityContextHolder.getContext().getAuthentication();

		ModelMap model = new ModelMap();

		String view = this.averiaController.updateAveria(1, 1, principal, model);

		assertEquals(view, "averias/averiaUpdate");

	}

	@Test
	@WithMockUser(value = "lolo", authorities = { "mecanico" })
	void testInitUpdateVehiculoNegativo() throws Exception {

		Authentication principal = SecurityContextHolder.getContext().getAuthentication();

		ModelMap model = new ModelMap();

		String view = this.averiaController.updateAveria(1, 1, principal, model);

		assertEquals(view, "exception");

	}

	@Test
	@WithMockUser(value = "paco", authorities = { "mecanico" })
	void testUpdateVehiculoForm() throws Exception {

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

		assertEquals(view, "redirect:/mecanicos/vehiculos/{vehiculoId}/averia");
	}
	
	@Test
	@WithMockUser(value = "paco", authorities = { "mecanico" })
	void testUpdateVehiculoFormNegativo() throws Exception {

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

		assertEquals(view, "averias/averiaUpdate");
	}

}