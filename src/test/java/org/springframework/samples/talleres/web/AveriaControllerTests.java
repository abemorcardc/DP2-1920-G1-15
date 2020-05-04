
package org.springframework.samples.talleres.web;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
import org.springframework.samples.talleres.model.Usuario;
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


	private static final int TEST_CITA_ID = 1;
	private static final int TEST_CITA_ID_INEXISTENTE = 100;
	private static final int TEST_CLIENTE_ID = 3;
	private static final int TEST_VEHICULO_ID = 1;
	private static final int TEST_MECANICO_ID = 1;
	private static final int TEST_AVERIA_ID = 1;


	@MockBean
	private CitaService citaService;

	@MockBean
	private VehiculoService vehiculoService;

	@MockBean
	private MecanicoService mecanicoService;

	@MockBean
	private ClienteService clienteService;

	@MockBean
	private AveriaService averiaService;


	@Autowired
	private MockMvc mockMvc;

	private Cita cita1;


	private Mecanico paco;


	private Mecanico error;


	private Vehiculo mercedes;


	private Averia				averia1;



	private Usuario paco1, manolo1;


	private Cliente manolo;

	private LocalDateTime fecha = LocalDateTime.parse("2021-12-15T10:15:30");


	private Averia av1;
	private Averia av2;

	@BeforeEach
	void setup() throws ParseException {

		this.paco1 = new Usuario();
		this.paco1.setNombreUsuario("paco1");
		this.paco1.setContra("paco1");
		this.paco1.setEnabled(true);

		this.manolo1 = new Usuario();
		this.manolo1.setNombreUsuario("manolo1");
		this.manolo1.setContra("manolo1");
		this.manolo1.setEnabled(true);

		this.paco = new Mecanico();
		this.paco.setId(AveriaControllerTests.TEST_MECANICO_ID);
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


		this.manolo = new Cliente();
		this.manolo.setId(AveriaControllerTests.TEST_CLIENTE_ID);
		this.manolo.setNombre("Manolo");
		this.manolo.setApellidos("Ramirez");
		this.manolo.setDireccion("C/Esperanza");
		this.manolo.setDni("21154416G");
		this.manolo.setEmail("PacoTalleres@gmail.com");
		this.manolo.setTelefono("666973647");
		this.manolo.setUsuario(this.manolo1);

		this.mercedes = new Vehiculo();
		this.mercedes.setId(AveriaControllerTests.TEST_VEHICULO_ID);
		this.mercedes.setActivo(true);
		this.mercedes.setKilometraje(10000);
		this.mercedes.setCliente(this.manolo);


		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String string = "2010-09-15";
		Date fecha = sdf.parse(string);

		Date d = new Date(); // 2012-09-04
		d.setYear(2012);
		d.setMonth(9);
		d.setDate(4);
		this.mercedes.setFechaMatriculacion(fecha);
		this.mercedes.setMatricula("2345FCL");
		this.mercedes.setModelo("Mercedes A");
		this.mercedes.setTipoVehiculo(TipoVehiculo.turismo);

		// this.error = new Mecanico();
		// this.error.setId(2);
		// this.error.setNombre("Error");
		// this.error.setApellidos("Error");
		// this.error.setDireccion("Error");
		// this.error.setDni("Error");
		// this.error.setEmail("Error");
		// this.error.setTelefono("Error");
		// this.error.setAveriasArregladas(0);
		// this.error.setExperiencia("Error");
		// this.error.setTitulaciones("Error");

		this.cita1 = new Cita();
		this.cita1.setId(AveriaControllerTests.TEST_CITA_ID);
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

		this.av2 = new Averia();
		this.av2.setId(2);
		this.av2.setCita(this.cita1);
		this.av2.setComplejidad(Complejidad.BAJA);
		this.av2.setDescripcion("cambio de bujia");
		this.av2.setCoste(50.0);
		this.av2.setEstaReparada(false);
		this.av2.setTiempo(100);
		this.av2.setPiezasNecesarias(1);
		this.av2.setNombre("coche de manolo");
		this.av2.setVehiculo(this.mercedes);
		this.av2.setMecanico(this.paco);

		BDDMockito.given(this.mecanicoService.findMecIdByUsername("paco1")).willReturn(1);
		BDDMockito.given(this.averiaService.findAveriaById(1)).willReturn(this.av1);
		BDDMockito.given(this.citaService.findCitaById(AveriaControllerTests.TEST_CITA_ID)).willReturn(this.cita1);
		BDDMockito.given(this.citaService.findCitasByMecanicoId(1)).willReturn(Lists.newArrayList(this.cita1));
		BDDMockito.given(this.clienteService.findIdByUsername("manolo"))
		.willReturn(AveriaControllerTests.TEST_CLIENTE_ID);
		BDDMockito.given(this.vehiculoService.findVehiculoById(AveriaControllerTests.TEST_VEHICULO_ID))
		.willReturn(this.mercedes);
		BDDMockito.given(this.averiaService.findAveriasByCitaId(AveriaControllerTests.TEST_AVERIA_ID))
		.willReturn(Lists.newArrayList(this.av1, this.av2, new Averia()));

	}

	// lista averias:
	@WithMockUser(value = "paco", roles = "mecanico")
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
		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/vehiculos/{vehiculoId}/averia", AveriaControllerTests.TEST_VEHICULO_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("averias/MecAveriasDeVehiculoList"));

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
			this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/vehiculos/{vehiculoId}/averia", 3)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
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

	// Tests Historia 9 (Abel y Javi) ------------------

	@WithMockUser(value = "paco1", roles = "mecanico")
	@Test
	void testInitUpdateForm() throws Exception {
		this.mockMvc
		.perform(get("/mecanicos/vehiculos/{vehiculoId}/averia/{averiaId}/edit", 1, 1))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("averia"))
		.andExpect(view().name("averias/averiaUpdate"));
	}

	@WithMockUser(value = "paco1", roles = "mecanico")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		mockMvc.perform(post("/mecanicos/vehiculos/{vehiculoId}/averia/{averiaId}/edit", 1, 1).with(csrf())
			.param("nombre", "Luna rota").param("descripcion", "la luna se ha roto")
			.param("coste", "100.0").param("tiempo", "2").param("piezasNecesarias", "1"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/mecanicos/vehiculos/{vehiculoId}/averia"));
	}

	@WithMockUser(value = "manolo", roles = "mecanico")
	@Test
	void testInitUpdateFormUsuarioEquivocado() throws Exception {
		mockMvc.perform(get("/mecanicos/vehiculos/{vehiculoId}/averia/{averiaId}/edit", 1,1)).andExpect(status().isOk())
		.andExpect(view().name("exception"));
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
				.param("tiempo", "20").param("piezasNecesarias", "5").param("complejidad", "BAJA").queryParam("citaId", "1")).andExpect(MockMvcResultMatchers.view().name("redirect:/mecanicos/vehiculos/{vehiculoId}/averia"));
		
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
