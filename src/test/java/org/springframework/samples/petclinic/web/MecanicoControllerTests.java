
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
import org.springframework.samples.petclinic.model.Mecanico;
import org.springframework.samples.petclinic.model.TipoCita;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.MecanicoService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for {@link MecanicoController}
 *
 * @author Colin But
 */

@WebMvcTest(controllers = MecanicoController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class MecanicoControllerTests {

	private static final int	TEST_MECANICO_ID	= 1;
	private static final int	TEST_CITA_ID		= 1;

	@Autowired
	private MecanicoController	mecanicoController;

	@MockBean
	private MecanicoService		mecanicoService;

	@MockBean
	private UserService			userService;

	@MockBean
	private AuthoritiesService	authoritiesService;

	@Autowired
	private MockMvc				mockMvc;

	private Mecanico			paco;

	private Cita				luna;


	@BeforeEach
	void setup() {

		this.paco = new Mecanico();
		this.paco.setId(MecanicoControllerTests.TEST_MECANICO_ID);
		this.paco.setNombre("Paco");
		this.paco.setApellidos("Ramirez");
		this.paco.setDireccion("C/Esperanza");
		this.paco.setDni("21154416G");
		this.paco.setEmail("PacoTalleres@gmail.com");
		this.paco.setTelefono("666973647");
		this.paco.setAveriasArregladas(12);
		this.paco.setExperiencia("ninguna");
		this.paco.setTitulaciones("Fp de mecanico");
		BDDMockito.given(this.mecanicoService.findById(MecanicoControllerTests.TEST_MECANICO_ID)).willReturn(this.paco);

		this.luna = new Cita();
		this.luna.setId(MecanicoControllerTests.TEST_CITA_ID);
		this.luna.setCoste(100.0);
		this.luna.setDescripcion("luna rota");
		this.luna.setEsUrgente(true);
		this.luna.setTiempo(100);
		this.luna.setTipo(TipoCita.reparacion);
		this.luna.setMecanico(this.paco);
		BDDMockito.given(this.mecanicoService.findCitaById(MecanicoControllerTests.TEST_CITA_ID)).willReturn(this.luna);

	}

	@WithMockUser(value = "spring")
	@Test
	void testListCitasByMecanico() throws Exception {
		BDDMockito.given(this.mecanicoService.findCitasByMecanicoId(this.paco.getId())).willReturn(Lists.newArrayList(this.luna));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/citas")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("results"))
			.andExpect(MockMvcResultMatchers.view().name("mecanicos/citaDeMecanicoList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateCitaForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/citas/{citaId}/edit", MecanicoControllerTests.TEST_CITA_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("cita"))
			.andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("coste", Matchers.is(100.0)))).andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("descripcion", Matchers.is("luna rota"))))
			.andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("esAceptado", Matchers.is(true)))).andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("esUrgente", Matchers.is(true))))
			.andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("tiempo", Matchers.is(100)))).andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("tipo", Matchers.is(TipoCita.reparacion))))
			.andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("mecanico", Matchers.is(this.paco)))).andExpect(MockMvcResultMatchers.view().name("mecanicos/citaMecUpdate"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateCitaFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/mecanicos/citas/{citaId}/edit", MecanicoControllerTests.TEST_CITA_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("coste", "115.0").param("descripcion", "luna destruida")
			.param("esAceptado", "true").param("esUrgente", "false").param("tiempo", "180")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/mecanicos/citas/"));
	}
	//
	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testProcessUpdateOwnerFormHasErrors() throws Exception {
	//		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/edit", TEST_OWNER_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", "Joe").param("lastName", "Bloggs").param("city", "London"))
	//			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("owner")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "address"))
	//			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "telephone")).andExpect(MockMvcResultMatchers.view().name("owners/createOrUpdateOwnerForm"));
	//	}

	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testShowOwner() throws Exception {
	//		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}", TEST_OWNER_ID)).andExpect(MockMvcResultMatchers.status().isOk())
	//			.andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("lastName", Matchers.is("Franklin")))).andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("firstName", Matchers.is("George"))))
	//			.andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("address", Matchers.is("110 W. Liberty St.")))).andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("city", Matchers.is("Madison"))))
	//			.andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("telephone", Matchers.is("6085551023")))).andExpect(MockMvcResultMatchers.view().name("owners/ownerDetails"));
	//	}

}
