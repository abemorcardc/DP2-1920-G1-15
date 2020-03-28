
package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.TipoVehiculo;
import org.springframework.samples.petclinic.model.Usuario;
import org.springframework.samples.petclinic.model.Vehiculo;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.ClienteService;
import org.springframework.samples.petclinic.service.VehiculoService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for {@link OwnerController}
 *
 * @author Colin But
 */

@WebMvcTest(controllers = VehiculoController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class VehiculoControllerTests {

	private static final int	TEST_VEHICULO_ID	= 1;

	private static final int	TEST_CLIENTE_ID		= 1;

	@Autowired
	private VehiculoController	vehiculoController;

	@MockBean
	private VehiculoService		vehiculoService;

	@MockBean
	private ClienteService		clienteService;

	@MockBean
	private CitaService			citaService;

	@Autowired
	private MockMvc				mockMvc;

	private Vehiculo			mercedes;

	private Cliente				pepe;

	private Usuario				pepe1;


	@BeforeEach
	void setup() throws ParseException {

		this.pepe1 = new Usuario();
		this.pepe1.setNombreUsuario("pepe1");
		this.pepe1.setContra("pepe1");
		this.pepe1.setEnabled(true);

		this.pepe = new Cliente();
		this.pepe.setNombre("pepe");
		this.pepe.setApellidos("Ramirez");
		this.pepe.setDireccion("C/Esperanza");
		this.pepe.setDni("21154416G");
		this.pepe.setEmail("pepeTalleres@gmail.com");
		this.pepe.setTelefono("666973647");
		this.pepe.setId(1);
		this.pepe.setUsuario(this.pepe1);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String string = "2010-09-15";
		Date fecha = sdf.parse(string);

		this.mercedes = new Vehiculo();
		this.mercedes.setId(VehiculoControllerTests.TEST_VEHICULO_ID);
		this.mercedes.setMatricula("1234HGF");
		this.mercedes.setModelo("mercedes A3");
		this.mercedes.setFechaMatriculacion(fecha);
		this.mercedes.setKilometraje(1000);
		this.mercedes.setActivo(true);
		this.mercedes.setTipoVehiculo(TipoVehiculo.turismo);
		this.mercedes.setCliente(this.pepe);

		BDDMockito.given(this.vehiculoService.findVehiculoById(VehiculoControllerTests.TEST_VEHICULO_ID)).willReturn(this.mercedes);

	}

	@WithMockUser(value = "spring")
	@Test
	void testListVehiculoByCliente() throws Exception {
		BDDMockito.given(this.vehiculoService.findVehiculosByClienteId(this.pepe.getId())).willReturn(Lists.newArrayList(this.mercedes));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/vehiculos")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("results"))
			.andExpect(MockMvcResultMatchers.view().name("vehiculos/vehiculoList"));
	}

	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testShowVehiculo() throws Exception {
	//		mockMvc.perform(get("/cliente/vehiculos/{vehiculoId}", TEST_VEHICULO_ID)).andExpect(status().isOk())
	//				.andExpect(model().attribute("vehiculo", hasProperty("fechaMatriculacion", is("2010-09-15"))))
	//				.andExpect(model().attribute("vehiculo", hasProperty("tipoVehiculo", is("turismo"))))
	//				.andExpect(model().attribute("vehiculo", hasProperty("matricula", is("1234HGF"))))
	//				.andExpect(model().attribute("vehiculo", hasProperty("modelo", is("mercedes A3"))))
	//				.andExpect(model().attribute("vehiculo", hasProperty("kilometraje", is("1000"))))
	//				.andExpect(view().name("vehiculos/vehiculoEnDetalle"));
	//	}

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
		this.mockMvc.perform(MockMvcRequestBuilders.post("/cliente/vehiculos/crear").with(SecurityMockMvcRequestPostProcessors.csrf()).param("tipoVehiculo", "turismo").param("matricula", "1234ZXC"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("vehiculo")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("vehiculos/crearVehiculo"));
	}

	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testInitUpdateForm() throws Exception {	
	//		mockMvc.perform(get("/cliente/vehiculos/{vehiculoId}/edit", TEST_VEHICULO_ID)).andExpect(status().isOk())
	//				.andExpect(model().attributeExists("vehiculo")).andExpect(view().name("vehiculos/vehiculoUpdate"));
	//	}

	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testProcessUpdateFormSuccess() throws Exception {
	//		mockMvc.perform(post("/cliente/vehiculos/{vehiculoId}/edit", TEST_VEHICULO_ID).with(csrf())
	//				.param("fechaMatriculacion", "2000-12-12").param("tipoVehiculo", "turismo")
	//				.param("matricula", "1234ZXC").param("modelo", "a3234").param("kilometraje", "3000")
	//				.param("activo", "true")).andExpect(status().is3xxRedirection())
	//				.andExpect(view().name("vehiculos/vehiculoList"));
	//	}
	//
	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testProcessUpdateFormHasErrors() throws Exception {
	//		mockMvc.perform(post("/cliente/vehiculos/{vehiculoId}/edit", TEST_VEHICULO_ID).with(csrf())
	//				.param("fechaMatriculacion", "2000-12-12").param("tipoVehiculo", "turismo"))
	//				.andExpect(model().attributeHasErrors("vehiculo"))
	//				.andExpect(status().isOk()).andExpect(view().name("vehiculos/vehiculoUpdate"));
	//	}

	//	@WithMockUser(value = "pepe")
	//	@Test
	//	void testVehiculoByVehiculoID() throws Exception {
	//		BDDMockito.given(this.vehiculoService.findVehiculoById(this.mercedes.getId())).willReturn(mercedes);
	//
	//		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/vehiculos/" + this.mercedes.getId())).andExpect(MockMvcResultMatchers.status().isOk())
	//			.andExpect(MockMvcResultMatchers.model().attributeExists("results"))
	//			.andExpect(MockMvcResultMatchers.view().name("vehiculos/vehiculoEnDetalle"));
	//	}

}
