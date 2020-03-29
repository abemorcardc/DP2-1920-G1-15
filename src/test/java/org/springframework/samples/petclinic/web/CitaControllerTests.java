package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
import java.util.Date;

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
import org.springframework.samples.talleres.configuration.SecurityConfiguration;
import org.springframework.samples.talleres.model.Cita;
import org.springframework.samples.talleres.model.Cliente;
import org.springframework.samples.talleres.model.EstadoCita;
import org.springframework.samples.talleres.model.Mecanico;
import org.springframework.samples.talleres.model.TipoCita;
import org.springframework.samples.talleres.model.TipoVehiculo;
import org.springframework.samples.talleres.model.Vehiculo;
import org.springframework.samples.talleres.service.CitaService;
import org.springframework.samples.talleres.service.ClienteService;
import org.springframework.samples.talleres.service.MecanicoService;
import org.springframework.samples.talleres.service.VehiculoService;
import org.springframework.samples.talleres.web.CitaController;
import org.springframework.samples.talleres.web.VisitController;
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

	@MockBean
	private CitaService citaService;

	@MockBean
	private VehiculoService vehiculoService;

	@MockBean
	private MecanicoService mecanicoService;

	@MockBean
	private ClienteService clienteService;

	@Autowired
	private MockMvc mockMvc;

	private Cita cita1;

	private Mecanico paco;

	private Mecanico error;

	private Vehiculo mercedes;

	private Cliente manolo;

	private Cliente jose;

	private LocalDateTime fecha = LocalDateTime.parse("2021-12-15T10:15:30");

	/*
	 * private LocalDate ld=new LocalDate(2021, 3, 14); private LocalDateTime
	 * fecha=new LocalDateTime(ld), new LocalTime());
	 */

	@SuppressWarnings("deprecation")
	@BeforeEach
	void setup() {
		this.paco = new Mecanico();
		this.paco.setId(CitaControllerTests.TEST_MECANICO_ID);
		this.paco.setNombre("Paco");
		this.paco.setApellidos("Ramirez");
		this.paco.setDireccion("C/Esperanza");
		this.paco.setDni("21154416G");
		this.paco.setEmail("PacoTalleres@gmail.com");
		this.paco.setTelefono("666973647");
		this.paco.setAveriasArregladas(12);
		this.paco.setExperiencia("ninguna");
		this.paco.setTitulaciones("Fp de mecanico");

		this.manolo = new Cliente();
		this.manolo.setId(CitaControllerTests.TEST_CLIENTE_ID);
		this.manolo.setNombre("Paco");
		this.manolo.setApellidos("Ramirez");
		this.manolo.setDireccion("C/Esperanza");
		this.manolo.setDni("21154416G");
		this.manolo.setEmail("PacoTalleres@gmail.com");
		this.manolo.setTelefono("666973647");

		this.jose = new Cliente();
		this.jose.setId(500);
		this.jose.setNombre("Paco");
		this.jose.setApellidos("Ramirez");
		this.jose.setDireccion("C/Esperanza");
		this.jose.setDni("21154416G");
		this.jose.setEmail("PacoTalleres@gmail.com");
		this.jose.setTelefono("666973647");

		this.mercedes = new Vehiculo();
		this.mercedes.setId(CitaControllerTests.TEST_VEHICULO_ID);
		this.mercedes.setActivo(true);
		this.mercedes.setKilometraje(10000);
		this.mercedes.setCliente(this.manolo);
		Date d = new Date(); // 2012-09-04
		d.setYear(2012);
		d.setMonth(9);
		d.setDate(4);
		this.mercedes.setFechaMatriculacion(d);
		this.mercedes.setMatricula("2345FCL");
		this.mercedes.setModelo("Mercedes A");
		this.mercedes.setTipoVehiculo(TipoVehiculo.turismo);

		this.error = new Mecanico();
		this.error.setId(2);
		this.error.setNombre("Error");
		this.error.setApellidos("Error");
		this.error.setDireccion("Error");
		this.error.setDni("Error");
		this.error.setEmail("Error");
		this.error.setTelefono("Error");
		this.error.setAveriasArregladas(0);
		this.error.setExperiencia("Error");
		this.error.setTitulaciones("Error");

		this.cita1 = new Cita();
		this.cita1.setId(CitaControllerTests.TEST_CITA_ID);
		this.cita1.setFechaCita(this.fecha);
		this.cita1.setCoste(120.0);
		this.cita1.setDescripcion("Problemas con el motor");
		this.cita1.setEstadoCita(EstadoCita.pendiente);
		this.cita1.setEsUrgente(true);
		this.cita1.setTiempo(40);
		this.cita1.setTipo(TipoCita.reparacion);
		this.cita1.setMecanico(this.paco);
		this.cita1.setVehiculo(this.mercedes);
		this.cita1.setCliente(this.manolo);
		BDDMockito.given(this.citaService.findCitaById(CitaControllerTests.TEST_CITA_ID)).willReturn(this.cita1);
		BDDMockito.given(this.clienteService.findIdByUsername("manolo"))
				.willReturn(CitaControllerTests.TEST_CLIENTE_ID);
		BDDMockito.given(this.vehiculoService.findVehiculoById(CitaControllerTests.TEST_VEHICULO_ID))
				.willReturn(this.mercedes);
	}

	// Pruebo que pediendo la cita con la id 1 me da todas las propiedades de esa
	// cita correctamente

	@WithMockUser(value = "manolo", roles = "cliente")
	@Test
	void testShowCitaForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/citas/{citaId}", 1))
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

	// Pruebo que pediendo la cita con la id 1 si entro con un cliente que no es el
	// que pidio esa cita
	// me redirije hacia las citas de ese cliente

	@WithMockUser(value = "jose", roles = "cliente")
	@Test
	void testShowCitaFormError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/citas/{citaId}", 1))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("cita"))

				// .andExpect(MockMvcResultMatchers.model().attribute("cita",
				// Matchers.emptyCollectionOf(Cita.class)))

				.andExpect(MockMvcResultMatchers.view().name("redirect:/cliente/citas"));
	}

	// Escenario positivo
	@WithMockUser(value = "spring")
	@Test
	void testShowCliCitaList() throws Exception {

		// Compruebo que para mi cliente paco me devuelve una lista que contiene la cita
		// cita1
		BDDMockito.given(this.citaService.findCitasByClienteId(this.error.getId()))
				.willReturn(Lists.newArrayList(this.cita1, new Cita()));

		// Compruebo que al hacer un GET a /cliente/citas no da error y redirije a
		// citas/citaList
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/citas", CitaControllerTests.TEST_CITA_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("citas/citaList"));

	}

//	// Escenario negativo
//	@WithMockUser(value = "jose", roles = "cliente")
//	@Test
//	void testShowCliCitaListError() throws Exception {
//
//		// Compruebo que para un cliente que no existe me devuelve una lista vacia
//		BDDMockito.given(this.citaService.findCitasByClienteId(100)).willReturn(Lists.emptyList());
//
//		// Compruebo que al hacer un GET a /cliente/citas no da error y redirije a
//		// citas/citaList
//		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/citas", CitaControllerTests.TEST_CITA_ID))
//				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
//				.andExpect(MockMvcResultMatchers.view().name("citas/citaList"));
//
//	}

	// Comprobamos que si se intenta consultar una cita que no existe da error
	@WithMockUser(value = "spring")
	@Test
	void testShowCitaErrorForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/cliente/citas/{citaId}",
						CitaControllerTests.TEST_CITA_ID_INEXISTENTE))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("cita"));
		// .andExpect(MockMvcResultMatchers.model().attributeHasErrors("cita"))
		// .andExpect(MockMvcResultMatchers.view().name("citas/citaEnDetalle"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/citas/pedir"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("cita"))
				.andExpect(MockMvcResultMatchers.view().name("citas/crearCita"));
	}

	// Esto comprueba que aunque rellenes todo el formulario sino has elegido un
	// coche te redirige hacia la elección de este
	@WithMockUser(value = "spring")
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
	@WithMockUser(value = "spring")
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

	@WithMockUser(value = "spring")

	@Test
	void testProcessCreationFormUnoVacio() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/cliente/citas/pedir", "1").param("estadoCita", "pendiente")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaCita", "28/03/2021 10:01")
				.param("coste", "0.0").param("tiempo", "0").param("esUrgente", "TRUE").param("tipo", "revision")
				.queryParam("vehiculoId", "1")).andExpect(MockMvcResultMatchers.view().name("citas/crearCita"));
	}

	// Comprobamos que si el parametro fecha esta mal(le falta la hora) te redirige
	// correctamente

	@WithMockUser(value = "spring")

	@Test
	void testProcessCreationFormFechaMal() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/cliente/citas/pedir", "1").param("estadoCita", "pendiente")
				.param("descripcion", "Problemas con el motor").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("fechaCita", "28/03/2021").param("coste", "0.0").param("tiempo", "0").param("esUrgente", "TRUE")
				.param("tipo", "revision").queryParam("vehiculoId", "1"))
				.andExpect(MockMvcResultMatchers.view().name("citas/crearCita"));
	}

	// Escenario positivo del get.
	@WithMockUser(value = "manolo", roles = "cliente")
	@Test
	void testCancelaCitaForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/cliente/citas/{citaId}/cancelar",
						CitaControllerTests.TEST_CITA_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("cita"))
				.andExpect(MockMvcResultMatchers.view().name("/citas/citaCancelar"));
	}

	// Escenario negativo
	@WithMockUser(value = "jose", roles = "cliente")
	@Test
	void testCancelaCitaFormError() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/cliente/citas/{citaId}/cancelar",
						CitaControllerTests.TEST_CITA_ID))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("cita"))
				.andExpect(MockMvcResultMatchers.view().name("redirect:/cliente/citas"));
	}

	// Escenario positivo del post
	@WithMockUser(value = "manolo", roles = "cliente")
	@Test
	void testCancelaCitaFormSucess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/cliente/citas/{citaId}/cancelar", CitaControllerTests.TEST_CITA_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.view().name("redirect:/cliente/citas/"));
	}

}