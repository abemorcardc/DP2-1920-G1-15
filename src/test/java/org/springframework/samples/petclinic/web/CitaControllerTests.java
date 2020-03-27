package org.springframework.samples.petclinic.web;

import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.EstadoCita;
import org.springframework.samples.petclinic.model.Mecanico;
import org.springframework.samples.petclinic.model.TipoCita;
import org.springframework.samples.petclinic.model.Vehiculo;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.VehiculoService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */
@WebMvcTest(controllers = CitaController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class CitaControllerTests {

	private static final int TEST_CITA_ID = 1;
	private static final int TEST_CITA_ID_INEXISTENTE = 100;
	private static final int TEST_CLIENTE_ID = 1;
	private static final int TEST_VEHICULO_ID = 1;
	private static final int TEST_MECANICO_ID = 1;

	@Autowired
	private CitaController citaController;

	@MockBean
	private CitaService citaService;

	@MockBean
	private VehiculoService vehiculoService;

	@Autowired
	private MockMvc mockMvc;

	private Cita cita1;

	private Mecanico paco;

	private Mecanico error;

	private Vehiculo mercedes;

	private Cliente manolo;

	/*
	 * private LocalDate ld=new LocalDate(2021, 3, 14); private LocalDateTime
	 * fecha=new LocalDateTime(ld), new LocalTime());
	 */

//	@BeforeEach
//	void setup() {
//		this.paco = new Mecanico();
//		this.paco.setId(CitaControllerTests.TEST_MECANICO_ID);
//		this.paco.setNombre("Paco");
//		this.paco.setApellidos("Ramirez");
//		this.paco.setDireccion("C/Esperanza");
//		this.paco.setDni("21154416G");
//		this.paco.setEmail("PacoTalleres@gmail.com");
//		this.paco.setTelefono("666973647");
//		this.paco.setAveriasArregladas(12);
//		this.paco.setExperiencia("ninguna");
//		this.paco.setTitulaciones("Fp de mecanico");
//
//		this.error = new Mecanico();
//		this.error.setId(2);
//		this.error.setNombre("Error");
//		this.error.setApellidos("Error");
//		this.error.setDireccion("Error");
//		this.error.setDni("Error");
//		this.error.setEmail("Error");
//		this.error.setTelefono("Error");
//		this.error.setAveriasArregladas(0);
//		this.error.setExperiencia("Error");
//		this.error.setTitulaciones("Error");
//
//		this.cita1 = new Cita();
//		this.cita1.setId(CitaControllerTests.TEST_CITA_ID);
//		// this.cita1.setFechaCita("2021-03-14 12:00:00");
//		this.cita1.setCoste(120.0);
//		this.cita1.setDescripcion("Problemas con el motor");
//		this.cita1.setEstadoCita(EstadoCita.pendiente);
//		this.cita1.setEsUrgente(true);
//		this.cita1.setTiempo(40);
//		this.cita1.setTipo(TipoCita.reparacion);
//		this.cita1.setMecanico(this.paco);
//		BDDMockito.given(this.citaService.findCitaById(CitaControllerTests.TEST_CITA_ID)).willReturn(this.cita1);
//	}

//	// Escenario positivo
//	@WithMockUser(value = "spring")
//	@Test
//	void testShowCliCitaList() throws Exception {
//
//		// Compruebo que para mi cliente paco me devuelve una lista que contiene la cita
//		// cita1
//		BDDMockito.given(this.citaService.findCitasByClienteId(this.error.getId()))
//				.willReturn(Lists.newArrayList(this.cita1, new Cita()));
//
//		// Compruebo que al hacer un GET a /cliente/citas no da error y redirije a
//		// citas/citaList
//		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/citas", CitaControllerTests.TEST_CITA_ID))
//				.andExpect(MockMvcResultMatchers.status().isOk())
//				.andExpect(MockMvcResultMatchers.view().name("citas/citaList"));
//
//	}
//
//	// Escenario negativo
//	// Hay que implementarlo
//	// primero-------------------------------------------------------
//	@WithMockUser(value = "spring")
//	@Test
//	void testShowCliCitaListError() throws Exception {
//
//		// Compruebo que para mi cliente paco me devuelve una lista que contiene la cita
//		// cita1
//		BDDMockito.given(this.citaService.findCitasByClienteId(this.paco.getId()))
//				.willReturn(Lists.newArrayList(this.cita1, new Cita()));
//
//		// Compruebo que al hacer un GET a /cliente/citas no da error y redirije a
//		// citas/citaList
//		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/citas", CitaControllerTests.TEST_CITA_ID))
//				.andExpect(MockMvcResultMatchers.status().isOk())
//				.andExpect(MockMvcResultMatchers.view().name("citas/citaList"));
//
//	}
//
//	// Pruebo que pediendo la cita con la id 1 me da todas las propiedades de esa
//	// cita correctamente
//	@WithMockUser(value = "spring")
//	@Test
//	void testShowCitaForm() throws Exception {
//		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/citas/{citaId}", CitaControllerTests.TEST_CITA_ID))
//				.andExpect(MockMvcResultMatchers.status().isOk())
//				.andExpect(MockMvcResultMatchers.model().attributeExists("cita"))
//				/*
//				 * .andExpect(MockMvcResultMatchers.model().attribute("cita",
//				 * Matchers.hasProperty("fechaCita", Matchers.is("2021-03-14 12:00:00"))))
//				 */
//				.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("cita"))
//				.andExpect(MockMvcResultMatchers.model().attribute("cita",
//						Matchers.hasProperty("descripcion", Matchers.is("Problemas con el motor"))))
//				.andExpect(MockMvcResultMatchers.model().attribute("cita",
//						Matchers.hasProperty("esUrgente", Matchers.is(true))))
//				.andExpect(MockMvcResultMatchers.model().attribute("cita",
//						Matchers.hasProperty("tipo", Matchers.is(TipoCita.reparacion))))
//				.andExpect(MockMvcResultMatchers.model().attribute("cita",
//						Matchers.hasProperty("coste", Matchers.is(120.0))))
//				.andExpect(MockMvcResultMatchers.model().attribute("cita",
//						Matchers.hasProperty("tiempo", Matchers.is(40))))
//				.andExpect(MockMvcResultMatchers.model().attribute("cita",
//						Matchers.hasProperty("estadoCita", Matchers.is(EstadoCita.pendiente))))
//				.andExpect(MockMvcResultMatchers.view().name("citas/citaEnDetalle"));
//	}
//
//	// Comprobamos que si se intenta consultar una cita que no existe da error
//	@WithMockUser(value = "spring")
//	@Test
//	void testShowCitaErrorForm() throws Exception {
//		this.mockMvc
//				.perform(MockMvcRequestBuilders.get("/cliente/citas/{citaId}",
//						CitaControllerTests.TEST_CITA_ID_INEXISTENTE))
//				.andExpect(MockMvcResultMatchers.status().isOk())
//				.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("cita"));
//		// .andExpect(MockMvcResultMatchers.model().attributeHasErrors("cita"))
//		// .andExpect(MockMvcResultMatchers.view().name("citas/citaEnDetalle"));
//	}
//
//	@WithMockUser(value = "spring")
//	@Test
//	void testInitCreationForm() throws Exception {
//		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/citas/pedir"))
//				.andExpect(MockMvcResultMatchers.status().isOk())
//				.andExpect(MockMvcResultMatchers.model().attributeExists("cita"))
//				.andExpect(MockMvcResultMatchers.view().name("citas/crearCita"));
//	}
//
//	// Esto comprueba que aunque rellenes todo el formulario sino has elegido un
//	// coche te redirige hacia la elección de este
//	@WithMockUser(value = "spring")
//	@Test
//	void testProcessCreationFormNoVehiculo() throws Exception {
//		this.mockMvc.perform(MockMvcRequestBuilders.post("/cliente/citas/pedir").param("estadoCita", "pendiente")
//				.param("descripcion", "Problemas con el motor").with(SecurityMockMvcRequestPostProcessors.csrf())
//				.param("fechaCita", "28/03/2020 10:01").param("coste", "0.0").param("tiempo", "0")
//				.param("esUrgente", "TRUE").param("tipo", "revision"))
//				.andExpect(MockMvcResultMatchers.view().name("redirect:/cliente/citas/vehiculo"));
//	}
//
//	// Escenario positivo si todos los parametros están bien te redirige
//	// correctamente
//	@WithMockUser(value = "spring")
//	@Test
//	void testProcessCreationFormSuccess() throws Exception {
//		this.mockMvc.perform(MockMvcRequestBuilders.post("/cliente/citas/pedir", "1").param("estadoCita", "pendiente")
//				.param("descripcion", "Problemas con el motor").with(SecurityMockMvcRequestPostProcessors.csrf())
//				.param("fechaCita", "28/03/2021 10:01").param("coste", "0.0").param("tiempo", "0")
//				.param("esUrgente", "TRUE").param("tipo", "revision").queryParam("vehiculoId", "1"))
//				.andExpect(MockMvcResultMatchers.view().name("redirect:/cliente/citas/"));
//	}
//
//	// Comprobamos que si un parametro esta en blanco, en este caso descripción, se
//	// redirige bien
//	/*
//	 * @WithMockUser(value = "spring")
//	 *
//	 * @Test void testProcessCreationFormUnoVacio() throws Exception {
//	 * this.mockMvc.perform(MockMvcRequestBuilders.post("/cliente/citas/pedir",
//	 * "1").param("estadoCita", "pendiente")
//	 * .with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaCita",
//	 * "28/03/2021 10:01") .param("coste", "0.0").param("tiempo",
//	 * "0").param("esUrgente", "TRUE").param("tipo", "revision")
//	 * .queryParam("vehiculoId",
//	 * "1")).andExpect(MockMvcResultMatchers.view().name("citas/crearCita")); }
//	 *
//	 * // Comprobamos que si el parametro fecha esta mal(le falta la hora) te
//	 * redirige // correctamente
//	 *
//	 * @WithMockUser(value = "spring")
//	 *
//	 * @Test void testProcessCreationFormFechaMal() throws Exception {
//	 * this.mockMvc.perform(MockMvcRequestBuilders.post("/cliente/citas/pedir",
//	 * "1").param("estadoCita", "pendiente") .param("descripcion",
//	 * "Problemas con el motor").with(SecurityMockMvcRequestPostProcessors.csrf())
//	 * .param("fechaCita", "28/03/2021").param("coste", "0.0").param("tiempo",
//	 * "0").param("esUrgente", "TRUE") .param("tipo",
//	 * "revision").queryParam("vehiculoId", "1"))
//	 * .andExpect(MockMvcResultMatchers.view().name("citas/crearCita")); }
//	 */
//
//	@WithMockUser(value = "spring")
//	@Test
//	void testCancelaCitaForm() throws Exception {
//		this.mockMvc
//				.perform(MockMvcRequestBuilders.get("/cliente/citas/{citaId}/cancelar",
//						CitaControllerTests.TEST_CITA_ID))
//				.andExpect(MockMvcResultMatchers.status().isOk())
//				.andExpect(MockMvcResultMatchers.model().attributeExists("cita"))
//				.andExpect(MockMvcResultMatchers.view().name("/citas/citaCancelar"));
//	}
//
//	@WithMockUser(value = "spring")
//	@Test
//	void testCancelaCitaFormSucess() throws Exception {
//		this.mockMvc
//				.perform(MockMvcRequestBuilders
//						.post("/cliente/citas/{citaId}/cancelar", CitaControllerTests.TEST_CITA_ID)
//						.with(SecurityMockMvcRequestPostProcessors.csrf()))
//				// .param("estadoCita", "cancelada")
//				// .param("fechaCita", "28/03/2021
//				// 10:01").with(SecurityMockMvcRequestPostProcessors.csrf())
//				// .param("descripcion", "Problemas con el motor"))
//				// .andExpect(MockMvcResultMatchers.status().isOk())
//				// .andExpect(MockMvcResultMatchers.model().attributeExists("cita"))
//
//				.andExpect(MockMvcResultMatchers.view().name("redirect:/cliente/citas/"));
//		// .andExpect(MockMvcResultMatchers.model().attributeExists("cita"))
//		// .andExpect(MockMvcResultMatchers.view().name("/citas/citaCancelar")));*/
//	}
//
}