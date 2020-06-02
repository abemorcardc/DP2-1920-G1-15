
package org.springframework.samples.talleres.web.integracion;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.talleres.web.ClienteController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Test class for {@link ClienteController}
 *
 * @author Colin But
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClienteControllerIntegracionTest {

	@Autowired
	private ClienteController	clienteController;

	private static final int	TEST_CLIENTE_ID				= 1;

	private static final int	TEST_CLIENTE_ID_2			= 2;

	private static final int	TEST_CLIENTE_ID_INEXISTENTE	= 100;

	// ---------------------------------------------------------
	//TESTS METODOS MECANICOS-CLIENTES


	/*
	 * Historia 16: Mecánico muestra Cliente
	 *
	 * Como mecánico quiero poder ver la información de un cliente para poder obtener más información acerca de éste.
	 */
	//ESCENARIO POSITIVO
	@WithMockUser(value = "paco", roles = "mecanico")
	@Test
	void testMecShowCliente() throws Exception {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> model = new HashMap<String, Object>();

		String view = this.clienteController.mecShowCliente(principal, ClienteControllerIntegracionTest.TEST_CLIENTE_ID, model);
		Assertions.assertEquals(view, "clientes/clienteEnDetalle");
	}

	/*
	 * ESCENARIO NEGATIVO
	 *
	 * Se intenta acceder a un cliente que no existe
	 */
	@WithMockUser(value = "paco", roles = "mecanico")
	@Test
	void testMecShowClienteNoExiste() throws Exception {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> model = new HashMap<String, Object>();

		String view = this.clienteController.mecShowCliente(principal, ClienteControllerIntegracionTest.TEST_CLIENTE_ID_INEXISTENTE, model);
		Assertions.assertEquals(view, "exception");
	}

	/*
	 * ESCENARIO NEGATIVO
	 *
	 * Se intenta acceder a un cliente con el que el mecanico no tiene ninguna cita
	 */
	@WithMockUser(value = "paco", roles = "mecanico")
	@Test
	void testMecNoAutorizadoShowCliente() throws Exception {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> model = new HashMap<String, Object>();

		String view = this.clienteController.mecShowCliente(principal, ClienteControllerIntegracionTest.TEST_CLIENTE_ID_2, model);
		Assertions.assertEquals(view, "exception");
	}
}
