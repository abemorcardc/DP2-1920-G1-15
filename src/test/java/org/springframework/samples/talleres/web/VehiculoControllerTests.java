
package org.springframework.samples.talleres.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.samples.talleres.model.Usuario;
import org.springframework.samples.talleres.model.Vehiculo;
import org.springframework.samples.talleres.service.CitaService;
import org.springframework.samples.talleres.service.ClienteService;
import org.springframework.samples.talleres.service.MecanicoService;
import org.springframework.samples.talleres.service.VehiculoService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for {@link OwnerController}
 *
 * @author Colin But
 */

@WebMvcTest(controllers = VehiculoController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
@TestPropertySource(locations = "classpath:application-mysql.properties")
class VehiculoControllerTests {

	private static final int	TEST_VEHICULO_ID	= 1;

	private static final int	TEST_CLIENTE_ID		= 1;

	@MockBean
	private VehiculoService		vehiculoService;

	@MockBean
	private ClienteService		clienteService;

	@MockBean
	private MecanicoService		mecanicoService;

	@MockBean
	private CitaService			citaService;

	@Autowired
	private MockMvc				mockMvc;

	private Vehiculo			mercedes;

	private Cliente				pepe;

	private Mecanico			paco;

	private Cita				cita1;

	private Usuario				pepe1, paco1;

	private LocalDateTime		fecha2				= LocalDateTime.parse("2021-12-15T10:15:30");


	@BeforeEach
	void setup() throws ParseException {

		this.pepe1 = new Usuario();
		this.pepe1.setNombreUsuario("pepe1");
		this.pepe1.setContra("pepe1");
		this.pepe1.setEnabled(true);

		this.paco1 = new Usuario();
		this.paco1.setNombreUsuario("paco1");
		this.paco1.setContra("paco1");
		this.paco1.setEnabled(true);

		this.pepe = new Cliente();
		this.pepe.setNombre("pepe");
		this.pepe.setApellidos("Ramirez");
		this.pepe.setDireccion("C/Esperanza");
		this.pepe.setDni("21154416G");
		this.pepe.setEmail("pepeTalleres@gmail.com");
		this.pepe.setTelefono("666973647");
		this.pepe.setUsuario(this.pepe1);
		this.pepe.setId(VehiculoControllerTests.TEST_CLIENTE_ID);

		this.paco = new Mecanico();
		this.paco.setId(1);
		this.paco.setNombre("Paco");
		this.paco.setApellidos("Ramirez");
		this.paco.setDireccion("C/Esperanza");
		this.paco.setDni("21154416G");
		this.paco.setEmail("PacoTalleres@gmail.com");
		this.paco.setTelefono("666973647");
		this.paco.setAveriasArregladas(12);
		this.paco.setExperiencia("ninguna");
		this.paco.setTitulaciones("Fp de mecanico");
		this.paco.setUsuario(this.paco1);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String string = "2010-09-15";
		Date fecha = sdf.parse(string);

		this.cita1 = new Cita();
		this.cita1.setId(1);
		this.cita1.setFechaCita(this.fecha2);
		this.cita1.setCoste(120.0);
		this.cita1.setDescripcion("Problemas con el motor");
		this.cita1.setEstadoCita(EstadoCita.pendiente);
		this.cita1.setEsUrgente(true);
		this.cita1.setTiempo(40);
		this.cita1.setTipo(TipoCita.reparacion);
		this.cita1.setMecanico(this.paco);
		this.cita1.setVehiculo(this.mercedes);
		this.cita1.setCliente(this.pepe);

		this.mercedes = new Vehiculo();
		this.mercedes.setMatricula("1234HGF");
		this.mercedes.setModelo("mercedes A3");
		this.mercedes.setFechaMatriculacion(fecha);
		this.mercedes.setKilometraje(1000);
		this.mercedes.setActivo(true);
		this.mercedes.setTipoVehiculo(TipoVehiculo.turismo);
		this.mercedes.setCliente(this.pepe);
		this.mercedes.setId(VehiculoControllerTests.TEST_VEHICULO_ID);

		BDDMockito.given(this.vehiculoService.findVehiculoById(VehiculoControllerTests.TEST_VEHICULO_ID)).willReturn(this.mercedes);
		BDDMockito.given(this.clienteService.findIdByUsername("pepe1")).willReturn(VehiculoControllerTests.TEST_CLIENTE_ID);
		BDDMockito.given(this.citaService.findCitasByVehiculoId(1)).willReturn(Lists.newArrayList(this.cita1, new Cita()));
		BDDMockito.given(this.mecanicoService.findMecIdByUsername("paco1")).willReturn(1);

	}

	@WithMockUser(value = "spring")
	@Test
	void testListVehiculoByCliente() throws Exception {
		BDDMockito.given(this.vehiculoService.findVehiculosByClienteId(this.pepe.getId())).willReturn(Lists.newArrayList(this.mercedes));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/vehiculos")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("results"))
			.andExpect(MockMvcResultMatchers.view().name("vehiculos/vehiculoList"));
	}

	@WithMockUser(value = "pepe1", roles = "cliente")
	@Test
	void testShowVehiculo() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String string = "2010-09-15";
		Date fecha = sdf.parse(string);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/vehiculos/{vehiculoId}", VehiculoControllerTests.TEST_VEHICULO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("vehiculo", Matchers.hasProperty("fechaMatriculacion", Matchers.is(fecha))))
			.andExpect(MockMvcResultMatchers.model().attribute("vehiculo", Matchers.hasProperty("tipoVehiculo", Matchers.is(TipoVehiculo.turismo))))
			.andExpect(MockMvcResultMatchers.model().attribute("vehiculo", Matchers.hasProperty("matricula", Matchers.is("1234HGF"))))
			.andExpect(MockMvcResultMatchers.model().attribute("vehiculo", Matchers.hasProperty("modelo", Matchers.is("mercedes A3")))).andExpect(MockMvcResultMatchers.model().attribute("vehiculo", Matchers.hasProperty("kilometraje", Matchers.is(1000))))
			.andExpect(MockMvcResultMatchers.view().name("vehiculos/vehiculoEnDetalle"));
	}

	@WithMockUser(value = "manolo", roles = "cliente")
	@Test
	void testShowVehiculoUsuarioEquivocado() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/vehiculos/{vehiculoId}", VehiculoControllerTests.TEST_VEHICULO_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "paco1", roles = "mecanico")
	@Test
	void testShowVehiculoMecanico() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String string = "2010-09-15";
		Date fecha = sdf.parse(string);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/vehiculos/{vehiculoId}", VehiculoControllerTests.TEST_VEHICULO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("vehiculo", Matchers.hasProperty("fechaMatriculacion", Matchers.is(fecha))))
			.andExpect(MockMvcResultMatchers.model().attribute("vehiculo", Matchers.hasProperty("tipoVehiculo", Matchers.is(TipoVehiculo.turismo))))
			.andExpect(MockMvcResultMatchers.model().attribute("vehiculo", Matchers.hasProperty("matricula", Matchers.is("1234HGF"))))
			.andExpect(MockMvcResultMatchers.model().attribute("vehiculo", Matchers.hasProperty("modelo", Matchers.is("mercedes A3")))).andExpect(MockMvcResultMatchers.model().attribute("vehiculo", Matchers.hasProperty("kilometraje", Matchers.is(1000))))
			.andExpect(MockMvcResultMatchers.view().name("vehiculos/vehiculoEnDetalle"));
	}

	@WithMockUser(value = "manolo", roles = "mecanico")
	@Test
	void testShowVehiculoMecanicoEquivocado() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/vehiculos/{vehiculoId}", VehiculoControllerTests.TEST_VEHICULO_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/vehiculos/crear")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("vehiculo"))
			.andExpect(MockMvcResultMatchers.view().name("vehiculos/crearVehiculo"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/cliente/vehiculos/crear").with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaMatriculacion", "2000-12-12").param("tipoVehiculo", "turismo").param("matricula", "1234ZXC")
			.param("modelo", "a3234").param("kilometraje", "3000").param("activo", "true")).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/cliente/vehiculos/crear").with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaMatriculacion", "2000-12-12").param("tipoVehiculo", "turismo").param("matricula", "1234ZXC").param("modelo", "a3234")
				.param("kilometraje", "-3000").param("activo", "true"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("vehiculo", "kilometraje")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("vehiculos/crearVehiculo"));
	}

	@WithMockUser(value = "pepe1", roles = "cliente")
	@Test
	void testInitUpdateForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/vehiculos/{vehiculoId}/edit", VehiculoControllerTests.TEST_VEHICULO_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("vehiculo"))
			.andExpect(MockMvcResultMatchers.view().name("vehiculos/vehiculoUpdate"));
	}

	@WithMockUser(value = "pepe1", roles = "cliente")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/cliente/vehiculos/{vehiculoId}/edit", VehiculoControllerTests.TEST_VEHICULO_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaMatriculacion", "2000-12-12").param("tipoVehiculo", "turismo")
				.param("matricula", "1234ZXC").param("modelo", "a3234").param("kilometraje", "6000").param("activo", "true"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/cliente/vehiculos"));
	}

	@WithMockUser(value = "manolo", roles = "cliente")
	@Test
	void testInitUpdateFormUsuarioEquivocado() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/vehiculos/{vehiculoId}/edit", VehiculoControllerTests.TEST_VEHICULO_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "pepe1", roles = "cliente")
	@Test
	void testProcessUpdateFormSuccessHasErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/cliente/vehiculos/{vehiculoId}/edit", VehiculoControllerTests.TEST_VEHICULO_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaMatriculacion", "2000-12-12").param("tipoVehiculo", "turismo")
				.param("matricula", "1234ZXC").param("modelo", "a3234").param("kilometraje", "-6000").param("activo", "true"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("vehiculo", "kilometraje")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("vehiculos/vehiculoUpdate"));
	}

	@WithMockUser(value = "pepe1", roles = "cliente")
	@Test
	void testInitDisableForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/vehiculos/{vehiculoId}/disable", VehiculoControllerTests.TEST_VEHICULO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("vehiculos/disableVehiculo"));
	}

	@WithMockUser(value = "pepe1", roles = "cliente")
	@Test
	void testProcessDisableFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/cliente/vehiculos/{vehiculoId}/disable", VehiculoControllerTests.TEST_VEHICULO_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaMatriculacion", "2010-09-15")
				.param("tipoVehiculo", "turismo").param("matricula", "1234HGF").param("modelo", "mercedes A3").param("kilometraje", "1000").param("activo", "true").param("id", "900"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/cliente/vehiculos"));
	}

	@WithMockUser(value = "manolo", roles = "cliente")
	@Test
	void testInitDisableFormUsuarioEquivocado() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/vehiculos/{vehiculoId}/disable", VehiculoControllerTests.TEST_VEHICULO_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}
}
