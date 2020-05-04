
package org.springframework.samples.talleres.web.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class AveriaControllerE2ETests {

	private static final int	TEST_VEHICULO_ID		= 1;
	private static final int	TEST_ERROR_VEHICULO_ID	= 2;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(value = "manolo", authorities = {
		"cliente"
	})
	@Test
	void testShowCliAverListByVeh() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/vehiculos/{vehiculoId}/averias", AveriaControllerE2ETests.TEST_VEHICULO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("results")).andExpect(MockMvcResultMatchers.view().name("averias/CliAveriasDeVehiculoList"));
	}

	@WithMockUser(value = "manolo", authorities = {
		"cliente"
	})
	@Test
	void testShowCliAverListByVehError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/vehiculos/{vehiculoId}/averias", AveriaControllerE2ETests.TEST_ERROR_VEHICULO_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testShowMecAverListByVeh() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/{vehiculoId}", AveriaControllerE2ETests.TEST_VEHICULO_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("results"))
			.andExpect(MockMvcResultMatchers.view().name("averias/MecAveriasDeVehiculoList"));
	}

	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testShowMecAverListByVehError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/{vehiculoId}", AveriaControllerE2ETests.TEST_ERROR_VEHICULO_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

}
