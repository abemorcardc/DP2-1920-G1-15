
package org.springframework.samples.talleres.web.e2e;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.hamcrest.Matchers;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class AveriaControllerE2ETest {

	@Autowired
	private MockMvc mockMvc;

	
	private static final int TEST_VEHICULO_ID = 1;
	private static final int TEST_AVERIA_ID = 1;
	
	// Tests Historia 10 (Abraham y David) ------------------
	
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
						.andExpect(MockMvcResultMatchers.view().name("redirect:/mecanicos/vehiculos/{vehiculoId}/averia"));
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

			//Test historia 8 (Abraham y David)
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
								Matchers.hasProperty("nombre", Matchers.is("Luna rota"))))
						.andExpect(MockMvcResultMatchers.model().attribute("averia",
								Matchers.hasProperty("descripcion", Matchers.is("la luna se ha roto"))))
						.andExpect(MockMvcResultMatchers.model().attribute("averia",
								Matchers.hasProperty("complejidad", Matchers.is(Complejidad.BAJA))))
						.andExpect(MockMvcResultMatchers.model().attribute("averia",
								Matchers.hasProperty("tiempo", Matchers.is(2))))
						.andExpect(MockMvcResultMatchers.model().attribute("averia",
								Matchers.hasProperty("coste", Matchers.is(100.0))))
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
			
			
			

	// Tests Historia 9 (Abel y Javi) ------------------

		@WithMockUser(value = "paco", authorities= {"mecanico"})
		@Test
		void testInitUpdateForm() throws Exception {
			this.mockMvc
					.perform(get("/mecanicos/vehiculos/{vehiculoId}/averia/{averiaId}/edit", 1, 1))
					.andExpect(status().isOk())
					.andExpect(model().attributeExists("averia"))
					.andExpect(view().name("averias/averiaUpdate"));
		}

		@WithMockUser(value = "paco", authorities= {"mecanico"})
		@Test
		void testProcessUpdateFormSuccess() throws Exception {
			mockMvc.perform(post("/mecanicos/vehiculos/{vehiculoId}/averia/{averiaId}/edit", 1, 1).with(csrf())
					.param("nombre", "Luna rota").param("descripcion", "la luna se ha roto")
					.param("coste", "100.0").param("tiempo", "2").param("piezasNecesarias", "1")
					.param("complejidad", "BAJA").param("estaReparada", "false"))
					.andExpect(status().is3xxRedirection())
					.andExpect(view().name("redirect:/mecanicos/vehiculos/{vehiculoId}/averia"));
		}

		@WithMockUser(value = "pepe", authorities= {"mecanico"})
		@Test
		void testInitUpdateFormUsuarioEquivocado() throws Exception {
			mockMvc.perform(get("/mecanicos/vehiculos/{vehiculoId}/averia/{averiaId}/edit", 1,1)).andExpect(status().isOk())
					.andExpect(view().name("exception"));
		}
		
		

}