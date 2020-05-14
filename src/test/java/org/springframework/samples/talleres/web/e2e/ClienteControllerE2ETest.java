
package org.springframework.samples.talleres.web.e2e;

import org.hamcrest.Matchers;
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
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class ClienteControllerE2ETest {

	@Autowired
	private MockMvc				mockMvc;

	private static final int	TEST_CLIENTE_ID_INEXISTENTE	= 100;
	private static final int	TEST_CLIENTE_ID				= 1;
	private static final int	TEST_CLIENTE2_ID			= 2;

	// ---------------------------------------------------------
	//TESTS METODOS MECANICOS-CLIENTES


	/*
	 * Historia 16: Mecánico muestra Cliente
	 *
	 * Como mecánico quiero poder ver la información de un cliente para poder obtener más información acerca de éste.
	 */
	//ESCENARIO POSITIVO
	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testMecShowCliente() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/cliente/{clienteId}", ClienteControllerE2ETest.TEST_CLIENTE_ID)).andExpect(MockMvcResultMatchers.status().isOk())

			.andExpect(MockMvcResultMatchers.model().attribute("cliente", Matchers.hasProperty("nombre", Matchers.is("Manolo"))))

			.andExpect(MockMvcResultMatchers.model().attribute("cliente", Matchers.hasProperty("apellidos", Matchers.is("Martin"))))

			.andExpect(MockMvcResultMatchers.model().attribute("cliente", Matchers.hasProperty("dni", Matchers.is("77844576X"))))

			.andExpect(MockMvcResultMatchers.model().attribute("cliente", Matchers.hasProperty("direccion", Matchers.is("C/Tarfia"))))

			.andExpect(MockMvcResultMatchers.model().attribute("cliente", Matchers.hasProperty("telefono", Matchers.is("608555102"))))

			.andExpect(MockMvcResultMatchers.model().attribute("cliente", Matchers.hasProperty("email", Matchers.is("Manolo72@gmail.com"))))

			.andExpect(MockMvcResultMatchers.view().name("clientes/clienteEnDetalle"));
	}

	/*
	 * ESCENARIO NEGATIVO
	 *
	 * Se intenta acceder a un cliente que no existe
	 */
	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testMecShowClienteNoExiste() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/cliente/{clienteId}", ClienteControllerE2ETest.TEST_CLIENTE_ID_INEXISTENTE)).andExpect(MockMvcResultMatchers.status().isOk())

			.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	/*
	 * ESCENARIO NEGATIVO
	 *
	 * Se intenta acceder a un cliente con el que el mecanico no tiene ninguna cita
	 */
	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testMecNoAutorizadoShowCliente() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/cliente/{clienteId}", ClienteControllerE2ETest.TEST_CLIENTE2_ID)).andExpect(MockMvcResultMatchers.status().isOk())

			.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

}
