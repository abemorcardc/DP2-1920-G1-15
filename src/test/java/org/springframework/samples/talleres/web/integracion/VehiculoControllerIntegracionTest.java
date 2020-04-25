
package org.springframework.samples.talleres.web.integracion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.Principal;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.talleres.model.Cliente;
import org.springframework.samples.talleres.service.CitaService;
import org.springframework.samples.talleres.service.ClienteService;
import org.springframework.samples.talleres.service.VehiculoService;
import org.springframework.samples.talleres.web.PetController;
import org.springframework.samples.talleres.web.VehiculoController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;

/**
 * Test class for the {@link PetController}
 *
 * @author Colin But
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VehiculoControllerIntegracionTest {

	@Autowired
	private VehiculoController	vehiculoController;

	@Autowired
	private VehiculoService		vehiculoService;

	@Autowired
	private ClienteService		clienteService;

	@Autowired
	private CitaService			citaService;


	@Test
	void testInitCreationVehiculoForm() throws Exception {

		Cliente cliente = this.clienteService.findClienteById(1);

		ModelMap model = new ModelMap();

		String view = this.vehiculoController.vehiculoCreation(cliente, model);

		assertEquals(view, "vehiculos/crearVehiculo");
		assertNotNull(model.get("vehiculo"));
	}
	
	@Test
	@WithMockUser(value = "manolo", authorities= {"cliente"})
	void testShowVehiculoList() throws Exception {
		
		Map<String, Object> model = new ModelMap();

		//String res = vehiculoController.showVehiculoList(p, model);	
		
	}
	
	@Test
	void testShowVehiculoDetalle() throws Exception {
		
	}
	
}
