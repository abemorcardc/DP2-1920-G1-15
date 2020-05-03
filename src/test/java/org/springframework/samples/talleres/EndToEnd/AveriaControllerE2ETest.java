package org.springframework.samples.talleres.EndToEnd;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.talleres.model.Complejidad;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class AveriaControllerE2ETest {

	@Autowired
	private MockMvc mockMvc;

	private static final int TEST_VEHICULO_ID = 1;
	private static final int TEST_AVERIA_ID = 1;

	@BeforeEach
	void setup() {
	}

	@WithMockUser(value = "paco", authorities = { "mecanico" })
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/mecanicos/{vehiculoId}/new",
						AveriaControllerE2ETest.TEST_VEHICULO_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("averia"))
				.andExpect(MockMvcResultMatchers.view().name("averias/crearAveria"));
	}

	// Escenario positivo de la creacion de la averia, todos los parametros están
	// bien
	@WithMockUser(username = "paco", authorities = { "mecanico" })
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/mecanicos/{vehiculoId}/new", AveriaControllerE2ETest.TEST_VEHICULO_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("nombre", "limpiaparabrisas")
						.param("descripcion",
								"Fallo en el limpiaparabrisas, no funciona la opcion rapida, cambio de piezas")
						.param("coste", "50.0").param("tiempo", "20").param("piezasNecesarias", "5")
						.param("complejidad", "BAJA").queryParam("citaId", "1"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/mecanicos/{vehiculoId}/"));
	}

	// Escenario negativo, con uno de los parámetros vacío, por ejemplo el
	// nombre
	@WithMockUser(username = "paco", authorities = { "mecanico" })
	@Test
	void testProcessCreationFormError() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/mecanicos/{vehiculoId}/new", AveriaControllerE2ETest.TEST_VEHICULO_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("descripcion",
								"Fallo en el limpiaparabrisas, no funciona la opcion rapida, cambio de piezas")
						.param("coste", "50.0").param("tiempo", "20").param("piezasNecesarias", "5")
						.param("complejidad", "BAJA").queryParam("citaId", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("averias/crearAveria"));
	}

	// Escenario positivo de un Show a una averia
	@WithMockUser(username = "paco", authorities = { "mecanico" })
	@Test
	void testShowAveriaSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/mecanicos/averia/{averiaId}",
						AveriaControllerE2ETest.TEST_AVERIA_ID))
				.andExpect(MockMvcResultMatchers.model().attributeExists("averia"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("averia",
						Matchers.hasProperty("nombre", Matchers.is("coche de manolo"))))
				.andExpect(MockMvcResultMatchers.model().attribute("averia",
						Matchers.hasProperty("descripcion", Matchers.is("cambio de bujia"))))
				.andExpect(MockMvcResultMatchers.model().attribute("averia",
						Matchers.hasProperty("complejidad", Matchers.is(Complejidad.BAJA))))
				.andExpect(MockMvcResultMatchers.model().attribute("averia",
						Matchers.hasProperty("tiempo", Matchers.is(100))))
				.andExpect(MockMvcResultMatchers.model().attribute("averia",
						Matchers.hasProperty("coste", Matchers.is(50.0))))
				.andExpect(MockMvcResultMatchers.model().attribute("averia",
						Matchers.hasProperty("piezasNecesarias", Matchers.is(1))))
				.andExpect(MockMvcResultMatchers.model().attribute("averia",
						Matchers.hasProperty("estaReparada", Matchers.is(false))))
				.andExpect(MockMvcResultMatchers.view().name("averias/MecanicoAveriaShow"));
	}

	// Escenario negativo de un Show a una averia
	@WithMockUser(username = "paco", authorities = { "mecanico" })
	@Test
	void testShowAveriaError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/averia/{averiaId}", 3))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

}
