
package org.springframework.samples.talleres.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
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

	private Averia				averia1;

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

		this.paco= new Mecanico();
		this.paco.setId(1);
		this.paco.setApellidos("Naranjo");
		this.paco.setAveriasArregladas(12);
		this.paco.setDireccion("C/Esperanza");
		this.paco.setDni("21154416G");
		this.paco.setEmail("PacoTalleres@gmail.com");
		this.paco.setExperiencia("ninguna");
		this.paco.setNombre("Paco");
		this.paco.setTelefono("666973647");
		this.paco.setTitulaciones("Fp de mecanico");
		
		this.averia1 = new Averia();
		this.averia1.setId(1);
		this.averia1.setNombre("coche de manolo");
		this.averia1.setCita(this.cita1);
		this.averia1.setComplejidad(Complejidad.BAJA);
		this.averia1.setDescripcion("cambio de bujia");
		this.averia1.setCoste(50.0);
		this.averia1.setEstaReparada(false);
		this.averia1.setTiempo(100);
		this.averia1.setPiezasNecesarias(1);
		this.averia1.setVehiculo(this.mercedes);
		this.averia1.setMecanico(this.paco);
		
		this.cita1= new Cita();
		this.cita1.setId(1);
		this.cita1.setCliente(this.manolo);
		this.cita1.setMecanico(this.paco);
		this.cita1.setVehiculo(this.mercedes);
		this.cita1.setCoste(120.0);
		this.cita1.setDescripcion("Problemas con el motor");
		this.cita1.setEstadoCita(EstadoCita.aceptada);
		this.cita1.setEsUrgente(true);
		this.cita1.setFechaCita(LocalDateTime.of(2021,03,14, 12,00));
		this.cita1.setTiempo(40);
		this.cita1.setTipo(TipoCita.reparacion);
		
		BDDMockito.given(this.clienteService.findIdByUsername("manolo")).willReturn(AveriaControllerTests.TEST_CLIENTE_ID);
		BDDMockito.given(this.vehiculoService.findVehiculoById(AveriaControllerTests.TEST_VEHICULO_ID)).willReturn(this.mercedes);
		BDDMockito.given(this.averiaService.findAveriasByVehiculoId(AveriaControllerTests.TEST_AVERIA_ID)).willReturn(Lists.newArrayList(this.averia1, new Averia()));
		
	}

	//lista averias del mecanico:
	@WithMockUser(value = "paco",roles="mecanico")
	@Test
	void testShowAveriasList() throws Exception {
		Collection<Cita> c=new ArrayList<Cita>();
		c.add(this.cita1);
		// Compruebo que para la cita 1 me devuelve una lista de averias
		
		BDDMockito.given(this.averiaService.findAveriasByVehiculoId(this.mercedes.getId())).willReturn(Lists.newArrayList(this.averia1, new Averia()));
		//Compruebo que para el usuario paco me devuelve su id
		BDDMockito.given(this.mecanicoService.findMecIdByUsername("paco")).willReturn(this.paco.getId());
		//Compruebo que devuelve las citas del mecanico
		BDDMockito.given(this.citaService.findCitasByMecanicoId(this.paco.getId())).willReturn(c);
		// Compruebo que al hacer un GET a /mecanicos/1 no da error y redirije bien
		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/{vehiculoId}", AveriaControllerTests.TEST_VEHICULO_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("averias/MecAveriasDeVehiculoList"));
	}
	
	//lista averias de otro mecanico:
		@WithMockUser(value = "paco",roles="mecanico")
		@Test
		void testShowAveriasListOtroMecanico() throws Exception {
			Collection<Cita> c=new ArrayList<Cita>();
			c.add(this.cita1);
			// Compruebo que para la cita 1 me devuelve una lista de averias
			BDDMockito.given(this.averiaService.findAveriasByVehiculoId(this.mercedes.getId())).willReturn(Lists.newArrayList(this.averia1, new Averia()));
			//Compruebo que devuelve las citas del mecanico
			BDDMockito.given(this.mecanicoService.findMecIdByUsername("paco")).willReturn(this.paco.getId());
			//Compruebo que devuelve las citas del mecanico
			BDDMockito.given(this.citaService.findCitasByMecanicoId(this.paco.getId())).willReturn(c);
			// Compruebo que al hacer un GET a /mecanicos/3 da error y redirije bien
			this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/{vehiculoId}", 3)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
		}
	//show averia del mecanico:
		@WithMockUser(value = "paco",roles="mecanico")
		@Test
		void testShowAverias() throws Exception {
			
			
			// Compruebo que para la cita 1 me devuelve una lista de averias
			BDDMockito.given(this.averiaService.findAveriaById(AveriaControllerTests.TEST_AVERIA_ID)).willReturn(this.averia1);
			//Compruebo que para el usuario paco me devuelve su id
			BDDMockito.given(this.mecanicoService.findMecIdByUsername("paco")).willReturn(this.paco.getId());
			// Compruebo que al hacer un GET a /mecanicos/averia/1 no da error y redirije bien
			this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/averia/{averiaId}", AveriaControllerTests.TEST_AVERIA_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("averias/MecanicoAveriaShow"));
		}		
		
		@WithMockUser(value = "paco",roles="mecanico")
		@Test
		void testShowAveriasOtroMecanico() throws Exception {
			
			
			// Compruebo que para la cita 1 me devuelve una lista de averias
			BDDMockito.given(this.averiaService.findAveriaById(AveriaControllerTests.TEST_AVERIA_ID)).willReturn(this.averia1);
			//Compruebo que para el usuario paco me devuelve su id
			BDDMockito.given(this.mecanicoService.findMecIdByUsername("paco")).willReturn(this.paco.getId());
			// Compruebo que al hacer un GET a /mecanicos/averia/1 no da error y redirije bien
			this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/averia/{averiaId}", 2)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
		}		
		
	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testShowAveriaListError() throws Exception {
	//
	//		// Compruebo que para la cita 1 me devuelve una lista de averias
	//
	//		//BDDMockito.given(this.averiaService.findAveriasByVehiculoId(this.mercedes.getId())).willReturn(Lists.newArrayList(this.averia1, new Averia()));
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
		BDDMockito.given(this.averiaService.findAveriasByVehiculoId(this.mercedes.getId())).willReturn(Lists.newArrayList(this.averia1, new Averia()));

		// Compruebo que al hacer un GET a /cliente/citas no da error y redirije a
		// citas/citaList
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/vehiculos/{vehiculoId}/averias", AveriaControllerTests.TEST_VEHICULO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("averias/CliAveriasDeVehiculoList"));

	}
	
	//Creacion de averia por parte del mecanico
	@WithMockUser(value = "paco",roles="mecanico")
	@Test
	void testInitCreationForm() throws Exception {
		Collection<Cita> c=new ArrayList<Cita>();
		c.add(this.cita1);
		// Compruebo que para la cita 1 me devuelve una lista de averias
		BDDMockito.given(this.averiaService.findAveriasByVehiculoId(this.mercedes.getId())).willReturn(Lists.newArrayList(this.averia1, new Averia()));
		//Compruebo que para el usuario paco me devuelve su id
		BDDMockito.given(this.mecanicoService.findMecIdByUsername("paco")).willReturn(this.paco.getId());
		//Compruebo que devuelve las citas del mecanico
		BDDMockito.given(this.citaService.findCitasByMecanicoId(this.paco.getId())).willReturn(c);
		
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/{vehiculoId}/new",1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("averia"))
			.andExpect(MockMvcResultMatchers.view().name("averias/crearAveria"));
	}
	
	//Creacion de averia por parte de otro mecanico
		@WithMockUser(value = "paco",roles="mecanico")
		@Test
		void testInitCreationFormOtroMecanico() throws Exception {
			Collection<Cita> c=new ArrayList<Cita>();
			c.add(this.cita1);
			// Compruebo que para la cita 1 me devuelve una lista de averias
			BDDMockito.given(this.averiaService.findAveriasByVehiculoId(this.mercedes.getId())).willReturn(Lists.newArrayList(this.averia1, new Averia()));
			//Compruebo que para el usuario paco me devuelve su id
			BDDMockito.given(this.mecanicoService.findMecIdByUsername("paco")).willReturn(this.paco.getId());
			//Compruebo que devuelve las citas del mecanico
			BDDMockito.given(this.citaService.findCitasByMecanicoId(this.paco.getId())).willReturn(c);
			
			
			this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/{vehiculoId}/new",3)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("averia"))
				.andExpect(MockMvcResultMatchers.view().name("exception"));
		}
	
	// Esto comprueba que aunque rellenes todo el formulario sino has elegido una
	// cita te redirige hacia la elección de esta
	@WithMockUser(value = "paco", roles = "mecanico")
	@Test
	void testProcessAverCreateFormNoCita() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/mecanicos/{vehiculoId}/new",AveriaControllerTests.TEST_VEHICULO_ID).param("nombre", "limpiaparabrisas").param("descripcion", "Fallo en el limpiaparabrisas, no funciona la opcion rapida, cambio de piezas").with(SecurityMockMvcRequestPostProcessors.csrf()).param("coste", "50.0")
				.param("tiempo", "20").param("piezasNecesarias", "5").param("complejidad", "BAJA")).andExpect(MockMvcResultMatchers.view().name("redirect:/mecanicos/{vehiculoId}/citas"));
		
	}
	
	//Escenario positivo de la creacion de la averia, todos los parametros están bien
	@WithMockUser(value = "paco", roles = "mecanico")
	@Test
	void testProcessAverCreateForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/mecanicos/{vehiculoId}/new",AveriaControllerTests.TEST_VEHICULO_ID,"1").param("nombre", "limpiaparabrisas").param("descripcion", "Fallo en el limpiaparabrisas, no funciona la opcion rapida, cambio de piezas").with(SecurityMockMvcRequestPostProcessors.csrf()).param("coste", "50.0")
				.param("tiempo", "20").param("piezasNecesarias", "5").param("complejidad", "BAJA").queryParam("citaId", "1")).andExpect(MockMvcResultMatchers.view().name("redirect:/mecanicos/{vehiculoId}/"));
		
	}
	
	//Si uno de los parámetros está vacío, por ejemplo el nombre, redirige bien a la pagnia excepcion
	@WithMockUser(value = "paco", roles = "mecanico")
	@Test
	void testProcessAverCreateFormEmpty() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/mecanicos/{vehiculoId}/new",AveriaControllerTests.TEST_VEHICULO_ID,"1").param("descripcion", "Fallo en el limpiaparabrisas, no funciona la opcion rapida, cambio de piezas").with(SecurityMockMvcRequestPostProcessors.csrf()).param("coste", "50.0")
				.param("tiempo", "20").param("piezasNecesarias", "5").param("complejidad", "BAJA").queryParam("citaId", "1")).andExpect(MockMvcResultMatchers.view().name("averias/crearAveria"));
		
	}
	
	//Si uno de los parámetros son negativos, por ejemplo el tiempo, redirige bien
	@WithMockUser(value = "paco", roles = "mecanico")
	@Test
	void testProcessAverCreateFormNegative() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/mecanicos/{vehiculoId}/new",AveriaControllerTests.TEST_VEHICULO_ID,"1").param("descripcion", "Fallo en el limpiaparabrisas, no funciona la opcion rapida, cambio de piezas").with(SecurityMockMvcRequestPostProcessors.csrf()).param("coste", "50.0")
				.param("tiempo", "-20").param("piezasNecesarias", "5").param("complejidad", "BAJA").queryParam("citaId", "1")).andExpect(MockMvcResultMatchers.view().name("averias/crearAveria"));
		
	}
	
	
		
}
