
package org.springframework.samples.talleres.web;

import java.util.Calendar;
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
import org.springframework.samples.talleres.configuration.SecurityConfiguration;
import org.springframework.samples.talleres.model.Averia;
import org.springframework.samples.talleres.model.Cita;
import org.springframework.samples.talleres.model.Cliente;
import org.springframework.samples.talleres.model.Complejidad;
import org.springframework.samples.talleres.model.Mecanico;
import org.springframework.samples.talleres.model.TipoVehiculo;
import org.springframework.samples.talleres.model.Vehiculo;
import org.springframework.samples.talleres.service.AveriaService;
import org.springframework.samples.talleres.service.CitaService;
import org.springframework.samples.talleres.service.ClienteService;
import org.springframework.samples.talleres.service.MecanicoService;
import org.springframework.samples.talleres.service.VehiculoService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */
@WebMvcTest(controllers = AveriaController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class AveriaControllerTests {

	private static final int	TEST_CLIENTE_ID		= 1;
	private static final int	TEST_VEHICULO_ID	= 1;
	private static final int	TEST_AVERIA_ID		= 1;
	//private static final int	TEST_VEHICULO_ID_404	= 3;

	@MockBean
	private CitaService			citaService;

	@MockBean
	private VehiculoService		vehiculoService;

	@MockBean
	private MecanicoService		mecanicoService;

	@MockBean
	private ClienteService		clienteService;

	@MockBean
	private AveriaService		averiaService;

	private Cita				cita1;

	private Mecanico			paco;

	private Vehiculo			mercedes;

	private Cliente				manolo;

	private Averia				av1;

	@Autowired
	private MockMvc				mockMvc;


	@BeforeEach
	void setup() {

		this.manolo = new Cliente();
		this.manolo.setId(AveriaControllerTests.TEST_CLIENTE_ID);
		this.manolo.setNombre("Paco");
		this.manolo.setApellidos("Ramirez");
		this.manolo.setDireccion("C/Esperanza");
		this.manolo.setDni("21154416G");
		this.manolo.setEmail("PacoTalleres@gmail.com");
		this.manolo.setTelefono("666973647");

		this.mercedes = new Vehiculo();
		this.mercedes.setId(AveriaControllerTests.TEST_VEHICULO_ID);
		this.mercedes.setActivo(true);
		this.mercedes.setKilometraje(10000);
		this.mercedes.setCliente(this.manolo);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2012);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date dateRepresentation = cal.getTime();

		this.mercedes.setFechaMatriculacion(dateRepresentation);
		this.mercedes.setMatricula("2345FCL");
		this.mercedes.setModelo("Mercedes A");
		this.mercedes.setTipoVehiculo(TipoVehiculo.turismo);

		this.av1 = new Averia();
		this.av1.setId(1);
		this.av1.setCita(this.cita1);
		this.av1.setComplejidad(Complejidad.BAJA);
		this.av1.setDescripcion("cambio de bujia");
		this.av1.setCoste(50.0);
		this.av1.setEstaReparada(false);
		this.av1.setTiempo(100);
		this.av1.setPiezasNecesarias(1);
		this.av1.setNombre("coche de manolo");
		this.av1.setVehiculo(this.mercedes);
		this.av1.setMecanico(this.paco);

		BDDMockito.given(this.clienteService.findIdByUsername("manolo")).willReturn(AveriaControllerTests.TEST_CLIENTE_ID);
		BDDMockito.given(this.vehiculoService.findVehiculoById(AveriaControllerTests.TEST_VEHICULO_ID)).willReturn(this.mercedes);
		BDDMockito.given(this.averiaService.findAveriasByVehiculoId(AveriaControllerTests.TEST_AVERIA_ID)).willReturn(Lists.newArrayList(this.av1, new Averia()));

	}

	//lista averias un mecanico:
	@WithMockUser(value = "spring")
	@Test
	void testShowAveriasList() throws Exception {

		// Compruebo que para la cita 1 me devuelve una lista de averias
		BDDMockito.given(this.averiaService.findAveriasByVehiculoId(this.mercedes.getId())).willReturn(Lists.newArrayList(this.av1, new Averia()));

		// Compruebo que al hacer un GET a /mecanicos/1 no da error y redirije bien
		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/{vehiculoId}", AveriaControllerTests.TEST_VEHICULO_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("averias/MecAveriasDeVehiculoList"));
	}

	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testShowAveriaListError() throws Exception {
	//
	//		// Compruebo que para la cita 1 me devuelve una lista de averias
	//
	//		//BDDMockito.given(this.averiaService.findAveriasByVehiculoId(this.mercedes.getId())).willReturn(Lists.newArrayList(this.av1, new Averia()));
	//
	//		// Compruebo que al hacer un GET a /averias/averiasDeVehiculoList no da error y redirije bien
	//		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/{vehiculoId}", AveriaControllerTests.TEST_VEHICULO_ID_404)).andExpect(MockMvcResultMatchers.status().is4xxClientError())
	//			.andExpect(MockMvcResultMatchers.view().name("averias/MecAveriasDeVehiculoList"));
	//
	//	}

	//lista averias un cliente:
	// Escenario positivo
	@WithMockUser(value = "manolo", roles = "cliente")
	@Test
	void testShowCliAveriaList() throws Exception {

		// Compruebo que para mi cliente paco me devuelve una lista que contiene la cita
		// cita1
		BDDMockito.given(this.averiaService.findAveriasByVehiculoId(this.mercedes.getId())).willReturn(Lists.newArrayList(this.av1, new Averia()));

		// Compruebo que al hacer un GET a /cliente/citas no da error y redirije a
		// citas/citaList
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/vehiculos/{vehiculoId}/averias", AveriaControllerTests.TEST_VEHICULO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("averias/CliAveriasDeVehiculoList"));

	}
}
