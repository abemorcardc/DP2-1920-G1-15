
package org.springframework.samples.talleres.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

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
import org.springframework.samples.talleres.web.OwnerController;
import org.springframework.samples.talleres.web.VehiculoController;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

/**
 * Test class for {@link OwnerController}
 *
 * @author Colin But
 */

@WebMvcTest(controllers = VehiculoController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class VehiculoControllerTests {

	private static final int TEST_VEHICULO_ID = 1;

	private static final int TEST_CLIENTE_ID = 1;

	@MockBean
	private VehiculoService vehiculoService;

	@MockBean
	private ClienteService clienteService;
	
	@MockBean
	private MecanicoService mecanicoService;

	@MockBean
	private CitaService citaService;

	@Autowired
	private MockMvc mockMvc;

	private Vehiculo mercedes;

	private Cliente pepe;
	
	private Mecanico paco;
	
	private Cita cita1;

	private Usuario pepe1, paco1;
	
	private LocalDateTime fecha2 = LocalDateTime.parse("2021-12-15T10:15:30");

	@BeforeEach
	void setup() throws ParseException {

		pepe1 = new Usuario();
		pepe1.setNombreUsuario("pepe1");
		pepe1.setContra("pepe1");
		pepe1.setEnabled(true);
		
		paco1 = new Usuario();
		paco1.setNombreUsuario("paco1");
		paco1.setContra("paco1");
		paco1.setEnabled(true);

		pepe = new Cliente();
		pepe.setNombre("pepe");
		pepe.setApellidos("Ramirez");
		pepe.setDireccion("C/Esperanza");
		pepe.setDni("21154416G");
		pepe.setEmail("pepeTalleres@gmail.com");
		pepe.setTelefono("666973647");
		pepe.setUsuario(pepe1);
		pepe.setId(TEST_CLIENTE_ID);
		
		paco = new Mecanico();
		paco.setId(1);
		paco.setNombre("Paco");
		paco.setApellidos("Ramirez");
		paco.setDireccion("C/Esperanza");
		paco.setDni("21154416G");
		paco.setEmail("PacoTalleres@gmail.com");
		paco.setTelefono("666973647");
		paco.setAveriasArregladas(12);
		paco.setExperiencia("ninguna");
		paco.setTitulaciones("Fp de mecanico");
		paco.setUsuario(paco1);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String string = "2010-09-15";
		Date fecha = sdf.parse(string);
		
		cita1 = new Cita();
		cita1.setId(1);
		cita1.setFechaCita(fecha2);
		cita1.setCoste(120.0);
		cita1.setDescripcion("Problemas con el motor");
		cita1.setEstadoCita(EstadoCita.pendiente);
		cita1.setEsUrgente(true);
		cita1.setTiempo(40);
		cita1.setTipo(TipoCita.reparacion);
		cita1.setMecanico(paco);
		cita1.setVehiculo(mercedes);
		cita1.setCliente(pepe);

		mercedes = new Vehiculo();
		mercedes.setMatricula("1234HGF");
		mercedes.setModelo("mercedes A3");
		mercedes.setFechaMatriculacion(fecha);
		mercedes.setKilometraje(1000);
		mercedes.setActivo(true);
		mercedes.setTipoVehiculo(TipoVehiculo.turismo);
		mercedes.setCliente(pepe);
		mercedes.setId(TEST_VEHICULO_ID);

		given(this.vehiculoService.findVehiculoById(TEST_VEHICULO_ID)).willReturn(mercedes);
		given(this.clienteService.findIdByUsername("pepe1")).willReturn(TEST_CLIENTE_ID);
		given(this.citaService.findCitasByVehiculoId(1)).willReturn(Lists.newArrayList(this.cita1, new Cita()));
		given(this.mecanicoService.findMecIdByUsername("paco1")).willReturn(1);

	}

	@WithMockUser(value = "spring")
	@Test
	void testListVehiculoByCliente() throws Exception {
		given(this.vehiculoService.findVehiculosByClienteId(this.pepe.getId()))
				.willReturn(Lists.newArrayList(this.mercedes));

		mockMvc.perform(get("/cliente/vehiculos")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(model().attributeExists("results")).andExpect(view().name("vehiculos/vehiculoList"));
	}

	@WithMockUser(value = "pepe1", roles = "cliente")
	@Test
	void testShowVehiculo() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String string = "2010-09-15";
		Date fecha = sdf.parse(string);

		mockMvc.perform(get("/cliente/vehiculos/{vehiculoId}", TEST_VEHICULO_ID)).andExpect(status().isOk())
				.andExpect(model().attribute("vehiculo", hasProperty("fechaMatriculacion", is(fecha))))
				.andExpect(model().attribute("vehiculo", hasProperty("tipoVehiculo", is(TipoVehiculo.turismo))))
				.andExpect(model().attribute("vehiculo", hasProperty("matricula", is("1234HGF"))))
				.andExpect(model().attribute("vehiculo", hasProperty("modelo", is("mercedes A3"))))
				.andExpect(model().attribute("vehiculo", hasProperty("kilometraje", is(1000))))
				.andExpect(view().name("vehiculos/vehiculoEnDetalle"));
	}

	@WithMockUser(value = "manolo", roles = "cliente")
	@Test
	void testShowVehiculoUsuarioEquivocado() throws Exception {
		mockMvc.perform(get("/cliente/vehiculos/{vehiculoId}", TEST_VEHICULO_ID)).andExpect(status().isOk())
				.andExpect(view().name("exception"));
	}
	
	@WithMockUser(value = "paco1", roles = "mecanico")
	@Test
	void testShowVehiculoMecanico() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String string = "2010-09-15";
		Date fecha = sdf.parse(string);

		mockMvc.perform(get("/mecanicos/vehiculos/{vehiculoId}", TEST_VEHICULO_ID)).andExpect(status().isOk())
				.andExpect(model().attribute("vehiculo", hasProperty("fechaMatriculacion", is(fecha))))
				.andExpect(model().attribute("vehiculo", hasProperty("tipoVehiculo", is(TipoVehiculo.turismo))))
				.andExpect(model().attribute("vehiculo", hasProperty("matricula", is("1234HGF"))))
				.andExpect(model().attribute("vehiculo", hasProperty("modelo", is("mercedes A3"))))
				.andExpect(model().attribute("vehiculo", hasProperty("kilometraje", is(1000))))
				.andExpect(view().name("vehiculos/vehiculoEnDetalle"));
	}

	@WithMockUser(value = "manolo", roles = "mecanico")
	@Test
	void testShowVehiculoMecanicoEquivocado() throws Exception {
		mockMvc.perform(get("/mecanicos/vehiculos/{vehiculoId}", TEST_VEHICULO_ID)).andExpect(status().isOk())
				.andExpect(view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cliente/vehiculos/crear"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("vehiculo"))
				.andExpect(MockMvcResultMatchers.view().name("vehiculos/crearVehiculo"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/cliente/vehiculos/crear")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaMatriculacion", "2000-12-12")
				.param("tipoVehiculo", "turismo").param("matricula", "1234ZXC").param("modelo", "a3234")
				.param("kilometraje", "3000").param("activo", "true"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/cliente/vehiculos/crear").with(csrf()).param("fechaMatriculacion", "2000-12-12")
				.param("tipoVehiculo", "turismo").param("matricula", "1234ZXC").param("modelo", "a3234")
				.param("kilometraje", "-3000").param("activo", "true"))
				.andExpect(model().attributeHasFieldErrors("vehiculo", "kilometraje")).andExpect(status().isOk())
				.andExpect(view().name("vehiculos/crearVehiculo"));
	}

	@WithMockUser(value = "pepe1", roles = "cliente")
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/cliente/vehiculos/{vehiculoId}/edit", TEST_VEHICULO_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("vehiculo")).andExpect(view().name("vehiculos/vehiculoUpdate"));
	}

	@WithMockUser(value = "pepe1", roles = "cliente")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		mockMvc.perform(post("/cliente/vehiculos/{vehiculoId}/edit", TEST_VEHICULO_ID).with(csrf())
				.param("fechaMatriculacion", "2000-12-12").param("tipoVehiculo", "turismo")
				.param("matricula", "1234ZXC").param("modelo", "a3234").param("kilometraje", "6000")
				.param("activo", "true")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/cliente/vehiculos"));
	}

	@WithMockUser(value = "manolo", roles = "cliente")
	@Test
	void testInitUpdateFormUsuarioEquivocado() throws Exception {
		mockMvc.perform(get("/cliente/vehiculos/{vehiculoId}/edit", TEST_VEHICULO_ID)).andExpect(status().isOk())
				.andExpect(view().name("exception"));
	}

	@WithMockUser(value = "pepe1", roles = "cliente")
	@Test
	void testProcessUpdateFormSuccessHasErrors() throws Exception {
		mockMvc.perform(post("/cliente/vehiculos/{vehiculoId}/edit", TEST_VEHICULO_ID).with(csrf())
				.param("fechaMatriculacion", "2000-12-12").param("tipoVehiculo", "turismo")
				.param("matricula", "1234ZXC").param("modelo", "a3234").param("kilometraje", "-6000")
				.param("activo", "true")).andExpect(model().attributeHasFieldErrors("vehiculo", "kilometraje"))
				.andExpect(status().isOk()).andExpect(view().name("vehiculos/vehiculoUpdate"));
	}

	@WithMockUser(value = "pepe1", roles = "cliente")
	@Test
	void testInitDisableForm() throws Exception {
		mockMvc.perform(get("/cliente/vehiculos/{vehiculoId}/disable", TEST_VEHICULO_ID)).andExpect(status().isOk())
				.andExpect(view().name("vehiculos/disableVehiculo"));
	}

	@WithMockUser(value = "pepe1", roles = "cliente")
	@Test
	void testProcessDisableFormSuccess() throws Exception {
		mockMvc.perform(post("/cliente/vehiculos/{vehiculoId}/disable", TEST_VEHICULO_ID).with(csrf())
				.param("fechaMatriculacion", "2010-09-15").param("tipoVehiculo", "turismo")
				.param("matricula", "1234HGF").param("modelo", "mercedes A3").param("kilometraje", "1000")
				.param("activo", "true").param("id", "900")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/cliente/vehiculos"));
	}

	@WithMockUser(value = "manolo", roles = "cliente")
	@Test
	void testInitDisableFormUsuarioEquivocado() throws Exception {
		mockMvc.perform(get("/cliente/vehiculos/{vehiculoId}/disable", TEST_VEHICULO_ID)).andExpect(status().isOk())
				.andExpect(view().name("exception"));
	}
}