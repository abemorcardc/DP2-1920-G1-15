
package org.springframework.samples.talleres.web.integration;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.talleres.service.ClienteService;
import org.springframework.samples.talleres.service.VehiculoService;
import org.springframework.samples.talleres.web.AveriaController;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AveriaControllerIntegrationTests {

	@Autowired
	private AveriaController	averiaController;

	@Autowired
	private VehiculoService		vehiculoService;

	@Autowired
	private ClienteService		clienteService;


	@WithMockUser(value = "manolo", authorities = {
		"cliente"
	})
	@Test
	void testShowCliAverListByVeh() throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		int vehiculoId = 1;
		Principal principal = SecurityContextHolder.getContext().getAuthentication();

		String view = this.averiaController.showCliAverListByVeh(principal, model, vehiculoId);

		Assertions.assertEquals(view, "averias/CliAveriasDeVehiculoList");
	}

	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testShowMecAverListByVeh() throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		int vehiculoId = 1;
		Principal principal = SecurityContextHolder.getContext().getAuthentication();

		String view = this.averiaController.showMecAverListByVeh(principal, model, vehiculoId);

		Assertions.assertEquals(view, "averias/MecAveriasDeVehiculoList");
	}

}
