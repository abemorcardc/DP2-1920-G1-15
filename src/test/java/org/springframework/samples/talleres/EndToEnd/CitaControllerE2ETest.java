package org.springframework.samples.talleres.EndToEnd;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class CitaControllerE2ETest {

	@Autowired
	private MockMvc mockMvc;

	private static final int TEST_CITA_ID = 1;
	private static final int TEST_VEHICULO_ID = 1;

	@BeforeEach
	void setup() {
	}

	// Escenario positivo de un show a una cita
	@WithMockUser(value = "manolo", authorities = { "cliente" })
	@Test
	void testShowCitaForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/citas/{citaId}", CitaControllerE2ETest.TEST_CITA_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("cita"))
				.andExpect(MockMvcResultMatchers.model().attribute("cita",
						Matchers.hasProperty("descripcion", Matchers.is("Problemas con el motor"))))
				.andExpect(MockMvcResultMatchers.model().attribute("cita",
						Matchers.hasProperty("esUrgente", Matchers.is(true))))
				.andExpect(MockMvcResultMatchers.model().attribute("cita",
						Matchers.hasProperty("tipo", Matchers.is(TipoCita.reparacion))))
				.andExpect(MockMvcResultMatchers.model().attribute("cita",
						Matchers.hasProperty("coste", Matchers.is(120.0))))
				.andExpect(MockMvcResultMatchers.model().attribute("cita",
						Matchers.hasProperty("tiempo", Matchers.is(40))))
				.andExpect(MockMvcResultMatchers.model().attribute("cita",
						Matchers.hasProperty("estadoCita", Matchers.is(EstadoCita.pendiente))))
				.andExpect(MockMvcResultMatchers.view().name("citas/citaEnDetalle"));
	}

	// Escenario negativo de un show a una cita
	@WithMockUser(value = "jose", authorities = { "cliente" })
	@Test
	void testShowCitaFormError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/citas/{citaId}", 1))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("cita"))
				.andExpect(MockMvcResultMatchers.view().name("redirect:/cliente/citas"));
	}

	// Escenario positivo
	@WithMockUser(value = "manolo", authorities = { "cliente" })
	@Test
	void testListCliCitaSuccess() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/citas", CitaControllerE2ETest.TEST_CITA_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("citas/citaList"));

	}

	@WithMockUser(value = "manolo", authorities = { "cliente" })
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/citas/pedir"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("cita"))
				.andExpect(MockMvcResultMatchers.view().name("citas/crearCita"));
	}

	// Esto comprueba que aunque rellenes todo el formulario sino has elegido un
	// coche te redirige hacia la elección de este
	@WithMockUser(value = "manolo", authorities = { "cliente" })
	@Test
	void testProcessCreationFormNoVehiculo() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/cliente/citas/pedir").param("estadoCita", "pendiente")
				.param("descripcion", "Problemas con el motor").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("fechaCita", "28/03/2020 10:01").param("coste", "0.0").param("tiempo", "0")
				.param("esUrgente", "TRUE").param("tipo", "revision"))
				.andExpect(MockMvcResultMatchers.view().name("redirect:/cliente/citas/vehiculo"));
	}

	// Escenario positivo si todos los parametros están bien te redirige
	// correctamente
	@WithMockUser(value = "manolo", authorities = { "cliente" })
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/cliente/citas/pedir", "1").param("estadoCita", "pendiente")
				.param("descripcion", "Problemas con el motor").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("fechaCita", "28/03/2021 10:01").param("coste", "0.0").param("tiempo", "0")
				.param("esUrgente", "TRUE").param("tipo", "revision").queryParam("vehiculoId", "1"))
				.andExpect(MockMvcResultMatchers.view().name("redirect:/cliente/citas/"));
	}

	// Comprobamos que si un parametro esta en blanco, en este caso descripción, se
	// redirige bien

	@WithMockUser(value = "manolo", authorities = { "cliente" })
	@Test
	void testProcessCreationFormUnoVacio() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/cliente/citas/pedir", "1").param("estadoCita", "pendiente")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaCita", "28/03/2021 10:01")
				.param("coste", "0.0").param("tiempo", "0").param("esUrgente", "TRUE").param("tipo", "revision")
				.queryParam("vehiculoId", "1")).andExpect(MockMvcResultMatchers.view().name("citas/crearCita"));
	}

	// Comprobamos que si el parametro fecha esta mal(le falta la hora) te redirige
	// correctamente
	@WithMockUser(value = "manolo", authorities = { "cliente" })
	@Test
	void testProcessCreationFormFechaMal() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/cliente/citas/pedir", "1").param("estadoCita", "pendiente")
				.param("descripcion", "Problemas con el motor").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("fechaCita", "28/03/2021").param("coste", "0.0").param("tiempo", "0").param("esUrgente", "TRUE")
				.param("tipo", "revision").queryParam("vehiculoId", "1"))
				.andExpect(MockMvcResultMatchers.view().name("citas/crearCita"));
	}

	// Escenario positivo .
	@WithMockUser(value = "manolo", authorities = { "cliente" })
	@Test
	void testCancelaCitaSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/cliente/citas/{citaId}/cancelar",
						CitaControllerE2ETest.TEST_CITA_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("cita"))
				.andExpect(MockMvcResultMatchers.view().name("/citas/citaCancelar"));
	}

	// Escenario negativo
	@WithMockUser(value = "jose", authorities = { "cliente" })
	@Test
	void testCancelaCitaError() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/cliente/citas/{citaId}/cancelar",
						CitaControllerE2ETest.TEST_CITA_ID))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("cita"))
				.andExpect(MockMvcResultMatchers.view().name("redirect:/cliente/citas"));
	}
}
