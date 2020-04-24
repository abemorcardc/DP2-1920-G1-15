
package org.springframework.samples.talleres.web.e2e;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class AveriaControllerE2ETest {

	@Autowired
	private MockMvc mockMvc;

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