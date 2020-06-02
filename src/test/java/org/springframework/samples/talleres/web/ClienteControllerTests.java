
package org.springframework.samples.talleres.web;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
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
 * Test class for {@link ClienteController}
 *
 * @author Colin But
 */
@WebMvcTest(controllers = CitaController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class ClienteControllerTests {

	private static final int	TEST_CITA_ID				= 1;
	private static final int	TEST_CITA_ID_INEXISTENTE	= 100;
	private static final int	TEST_CLIENTE_ID				= 1;
	private static final int	TEST_VEHICULO_ID			= 1;
	private static final int	TEST_MECANICO_ID			= 1;

	@MockBean
	private VehiculoService		vehiculoService;

	@MockBean
	private MecanicoService		mecanicoService;

	@MockBean
	private ClienteService		clienteService;

	@MockBean
	private AveriaService		averiaService;

	@MockBean
	private CitaService			citaService;

	@Autowired
	private MockMvc				mockMvc;

	private Cita				cita1;

	private Mecanico			paco;

	private Mecanico			error;

	private Vehiculo			mercedes;

	private Cliente				manolo;

	private LocalDateTime		fecha						= LocalDateTime.parse("2021-12-15T10:15:30");

	private Cliente				jose;


	@BeforeEach
	void setup() {
		this.paco = new Mecanico();
		this.paco.setId(ClienteControllerTests.TEST_MECANICO_ID);
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
		this.manolo.setId(ClienteControllerTests.TEST_CLIENTE_ID);
		this.manolo.setNombre("Manolo");
		this.manolo.setApellidos("Rodriguez");
		this.manolo.setDireccion("C/Esperanza");
		this.manolo.setDni("21154416G");
		this.manolo.setEmail("PacoTalleres@gmail.com");
		this.manolo.setTelefono("666973647");

		this.jose = new Cliente();
		this.jose.setId(500);
		this.jose.setNombre("Jose");
		this.jose.setApellidos("Garcia");
		this.jose.setDireccion("C/Esperanza");
		this.jose.setDni("21154416G");
		this.jose.setEmail("PacoTalleres@gmail.com");
		this.jose.setTelefono("666973647");

		this.mercedes = new Vehiculo();
		this.mercedes.setId(ClienteControllerTests.TEST_VEHICULO_ID);
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
		this.cita1.setId(ClienteControllerTests.TEST_CITA_ID);
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

		BDDMockito.given(this.citaService.findCitaById(ClienteControllerTests.TEST_CITA_ID)).willReturn(this.cita1);
		BDDMockito.given(this.citaService.findCitasByClienteId(this.manolo.getId())).willReturn(Lists.newArrayList(this.cita1));

		BDDMockito.given(this.clienteService.findIdByUsername("manolo")).willReturn(ClienteControllerTests.TEST_CLIENTE_ID);
		BDDMockito.given(this.clienteService.findClienteById(ClienteControllerTests.TEST_CLIENTE_ID)).willReturn(this.manolo);

		BDDMockito.given(this.mecanicoService.findMecIdByUsername("paco")).willReturn(ClienteControllerTests.TEST_MECANICO_ID);
		BDDMockito.given(this.mecanicoService.findMecanicoById(ClienteControllerTests.TEST_MECANICO_ID)).willReturn(this.paco);

		BDDMockito.given(this.vehiculoService.findVehiculoById(ClienteControllerTests.TEST_VEHICULO_ID)).willReturn(this.mercedes);
		BDDMockito.given(this.vehiculoService.findVehiculosByClienteId(this.manolo.getId())).willReturn(Lists.newArrayList(this.mercedes));

	}

	@WithMockUser(value = "paco", roles = "mecanico")
	//@Test
	void testMecShowCliente() throws Exception {

		//Da error 404
		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/cliente/{clienteId}", ClienteControllerTests.TEST_CLIENTE_ID))

			.andExpect(MockMvcResultMatchers.status().isOk())

		//			.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("cliente"))
		//
		//			.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("vehiculos"))
		//
		//			.andExpect(MockMvcResultMatchers.model().attribute("cliente", Matchers.hasProperty("nombre", Matchers.is("Manolo"))))
		//
		//			.andExpect(MockMvcResultMatchers.model().attribute("cliente", Matchers.hasProperty("apellidos", Matchers.is("Garcia"))))
		//
		//			.andExpect(MockMvcResultMatchers.model().attribute("cliente", Matchers.hasProperty("dni", Matchers.is("21154416G"))))
		//
		//			.andExpect(MockMvcResultMatchers.model().attribute("cliente", Matchers.hasProperty("direccion", Matchers.is("C/Esperanza"))))
		//
		//			.andExpect(MockMvcResultMatchers.model().attribute("cliente", Matchers.hasProperty("telefono", Matchers.is("666973647"))))
		//
		//			.andExpect(MockMvcResultMatchers.model().attribute("cliente", Matchers.hasProperty("email", Matchers.is("PacoTalleres@gmail.com"))))
		//
		//			.andExpect(MockMvcResultMatchers.view().name("clientes/clienteEnDetalle"))
		;
	}

}
