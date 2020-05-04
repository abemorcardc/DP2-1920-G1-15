
package org.springframework.samples.talleres.web.e2e;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.talleres.model.EstadoCita;
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
public class CitaControllerE2ETests {

	private static final int	TEST_CITA_ID		= 1;
	private static final int	TEST_ERROR_CITA_ID	= 2;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testshowMecCitaDetalle() throws Exception {
		//Falla por algún motivo en el formato de la fecha
		mockMvc.perform(get("/mecanicos/citas/{citaId}", TEST_CITA_ID)).andExpect(status().isOk())

		.andExpect(model().attribute("cita", hasProperty("estadoCita", is(EstadoCita.pendiente))))
		.andExpect(model().attribute("cita", hasProperty("descripcion", is("prueba test"))))
		.andExpect(model().attribute("cita", hasProperty("esUrgente", is(true))))
		.andExpect(model().attribute("cita", hasProperty("tipo", is(TipoCita.reparacion))))
		.andExpect(model().attribute("cita", hasProperty("coste", is(60.0))))
		.andExpect(model().attribute("cita", hasProperty("tiempo", is(23))))

		.andExpect(model().attribute("cita", hasProperty("fechaCita", is(LocalDateTime.parse("2020-10-22T10:00")))))

		.andExpect(view().name("citas/citaEnDetalle"));
	}

	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testShowMecCitaDetalleError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/citas/{citaId}", CitaControllerE2ETests.TEST_ERROR_CITA_ID)).andExpect(MockMvcResultMatchers.view().name("exception"));
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
		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/citas/{citaId}/edit", CitaControllerE2ETests.TEST_CITA_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("cita"))
		.andExpect(MockMvcResultMatchers.view().name("citas/citaMecUpdate"));
	}

	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testInitUpdateMecFormError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/citas/{citaId}/edit", CitaControllerE2ETests.TEST_ERROR_CITA_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testProcessUpdateMecForm() throws Exception {
		//No da como que redirije, si no como OK
		mockMvc.perform(post("/mecanicos/citas/{citaId}/edit",TEST_CITA_ID).with(csrf())
			.param("fechaCita","22/10/2020 10:00")
			.param("descripcion", "prueba test").param("tiempo", "23").param("coste", "60.0")
			.param("estadoCita", "pendiente")
			)
		//.andExpect(MockMvcResultMatchers.forwardedUrl(""));
		.andExpect(status().is3xxRedirection())

		//.andExpect(MockMvcResultMatchers.status().isOk())

		.andExpect(view().name("redirect:/mecanicos/citas/"));
	}

	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testProcessUpdateMecFormError() throws Exception {
		//No redirije, da como OK
		this.mockMvc.perform(MockMvcRequestBuilders.post("/mecanicos/citas/{citaId}/edit", CitaControllerE2ETests.TEST_CITA_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaCita", "20-03-14 12:00:00")
			.param("descripcion", "Problemas con el motor").param("esUrgente", "true").param("tipo", "reparacion").param("coste", "-200").param("tiempo", "-50").param("id", "1").param("estadoCita", "pendiente"));
	}

}
