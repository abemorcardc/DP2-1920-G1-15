
package org.springframework.samples.talleres.web.integracion;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.talleres.model.Cliente;
import org.springframework.samples.talleres.service.CitaService;
import org.springframework.samples.talleres.service.ClienteService;
import org.springframework.samples.talleres.service.VehiculoService;
import org.springframework.samples.talleres.web.VehiculoController;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;


/**
 * Test class for the {@link PetController}
 *
 * @author Colin But
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VehiculoControllerIntegracionTests {

	private static final int TEST_VEHICULO_ID = 1;

	private static final int TEST_CLIENTE_ID = 1;

	@Autowired
	private VehiculoController vehiculoController;

	@Autowired
	private VehiculoService vehiculoService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private CitaService citaService;

	@Test
	void testInitCreationVehiculoForm() throws Exception {

		Cliente cliente = clienteService.findClienteById(1);

		ModelMap model = new ModelMap();

		String view = vehiculoController.vehiculoCreation(cliente, model);

		assertEquals(view, "vehiculos/crearVehiculo");
		assertNotNull(model.get("vehiculo"));
	}

}