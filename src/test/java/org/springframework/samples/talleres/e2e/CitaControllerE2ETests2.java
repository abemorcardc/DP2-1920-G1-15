
package org.springframework.samples.talleres.e2e;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.talleres.model.TipoCita;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class CitaControllerE2ETests2 {

	private static final int	TEST_CITA_ID		= 1;
	private static final int	TEST_ERROR_CITA_ID	= 2;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testshowMecCitaDetalle() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/citas/{citaId}", CitaControllerE2ETests2.TEST_CITA_ID)).andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("fechaCita", Matchers.is("2021-03-14T12:00"))))
			.andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("descripcion", Matchers.is("Problemas con el motor"))))
			.andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("tipo", Matchers.is(TipoCita.reparacion)))).andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("coste", Matchers.is(120))))
			.andExpect(MockMvcResultMatchers.view().name("citas/citaEnDetalle"));
	}

	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testShowMecCitaDetalleError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/citas/{citaId}", CitaControllerE2ETests2.TEST_ERROR_CITA_ID)).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testShowMecCitaList() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/citas")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("results"))
			.andExpect(MockMvcResultMatchers.view().name("citas/citaDeMecanicoList"));
	}

	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testInitUpdateMecForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/citas/{citaId}/edit", CitaControllerE2ETests2.TEST_CITA_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("cita"))
			.andExpect(MockMvcResultMatchers.view().name("citas/citaMecUpdate"));
	}

	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testInitUpdateMecFormError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/citas/{citaId}/edit", CitaControllerE2ETests2.TEST_ERROR_CITA_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testProcessUpdateMecForm() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/mecanicos/citas/{citaId}/edit", CitaControllerE2ETests2.TEST_CITA_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaCita", "2021-03-14 12:00:00")
				.param("descripcion", "Problemas con el motor").param("esUrgente", "true").param("tipo", "reparacion").param("coste", "200").param("tiempo", "50").param("id", "1").param("estadoCita", "pendiente"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/mecanicos/citas/"));
	}

	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testProcessUpdateMecFormError() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/mecanicos/citas/{citaId}/edit", CitaControllerE2ETests2.TEST_CITA_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaCita", "2021-03-14 12:00:00")
				.param("descripcion", "Problemas con el motor").param("esUrgente", "true").param("tipo", "reparacion").param("coste", "-200").param("tiempo", "-50").param("id", "1").param("estadoCita", "pendiente"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("citas/citaMecUpdate"));
	}

}
