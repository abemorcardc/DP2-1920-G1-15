
package org.springframework.samples.talleres.web.e2e;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.talleres.model.TipoVehiculo;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
//@TestPropertySource(locations = "classpath:application-mysql.properties")
class VehiculoControllerE2ETest {

	private static final int TEST_VEHICULO_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(value = "manolo", authorities= {"cliente"})
	@Test
	void testListVehiculoByCliente() throws Exception {
		mockMvc.perform(get("/cliente/vehiculos"))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("results"))
		.andExpect(view().name("vehiculos/vehiculoList"));
	}

	@WithMockUser(value = "manolo", authorities= {"cliente"})
	@Test
	void testShowVehiculo() throws Exception {
		mockMvc.perform(get("/cliente/vehiculos/{vehiculoId}", TEST_VEHICULO_ID)).andExpect(status().isOk())
		.andExpect(model().attribute("vehiculo", hasProperty("tipoVehiculo", is(TipoVehiculo.turismo))))
		.andExpect(model().attribute("vehiculo", hasProperty("matricula", is("2345FCL"))))
		.andExpect(model().attribute("vehiculo", hasProperty("modelo", is("Mercedes A"))))
		.andExpect(model().attribute("vehiculo", hasProperty("kilometraje", is(10000))))
		.andExpect(view().name("vehiculos/vehiculoEnDetalle"));
	}

	@WithMockUser(value = "manolo", authorities= {"cliente"})
	@Test
	void testShowVehiculoUsuarioEquivocado() throws Exception {
		mockMvc.perform(get("/cliente/vehiculos/{vehiculoId}", 3)).andExpect(status().isOk())
		.andExpect(view().name("exception"));
	}

	@WithMockUser(value = "lolo", authorities= {"mecanico"})
	@Test
	void testShowVehiculoMecanico() throws Exception {
		mockMvc.perform(get("/mecanicos/vehiculos/{vehiculoId}", 2)).andExpect(status().isOk())
		.andExpect(model().attribute("vehiculo", hasProperty("tipoVehiculo", is(TipoVehiculo.turismo))))
		.andExpect(model().attribute("vehiculo", hasProperty("matricula", is("5125DRF"))))
		.andExpect(model().attribute("vehiculo", hasProperty("modelo", is("Peugeot 307"))))
		.andExpect(model().attribute("vehiculo", hasProperty("kilometraje", is(15000))))
		.andExpect(view().name("vehiculos/vehiculoEnDetalle"));
	}

	@WithMockUser(value = "paco", authorities= {"mecanico"})
	@Test
	void testShowVehiculoMecanicoEquivocado() throws Exception {
		mockMvc.perform(get("/mecanicos/vehiculos/{vehiculoId}", 2)).andExpect(status().isOk())
		.andExpect(view().name("exception"));
	}

	@WithMockUser(value = "manolo", authorities= {"cliente"})
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cliente/vehiculos/crear"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("vehiculo"))
		.andExpect(MockMvcResultMatchers.view().name("vehiculos/crearVehiculo"));
	}

	@WithMockUser(value = "manolo", authorities= {"cliente"})
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/cliente/vehiculos/crear")
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaMatriculacion", "2000-12-12")
			.param("tipoVehiculo", "turismo").param("matricula", "1234ZXC").param("modelo", "a3234")
			.param("kilometraje", "3000").param("activo", "true"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(value = "manolo", authorities= {"cliente"})
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/cliente/vehiculos/crear").with(csrf()).param("fechaMatriculacion", "2000-12-12")
			.param("tipoVehiculo", "turismo").param("matricula", "1234ZXC").param("modelo", "a3234")
			.param("kilometraje", "-3000").param("activo", "true"))
		.andExpect(model().attributeHasFieldErrors("vehiculo", "kilometraje")).andExpect(status().isOk())
		.andExpect(view().name("vehiculos/crearVehiculo"));
	}

	@WithMockUser(value = "manolo", authorities= {"cliente"})
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/cliente/vehiculos/{vehiculoId}/edit", TEST_VEHICULO_ID)).andExpect(status().isOk())
		.andExpect(model().attributeExists("vehiculo")).andExpect(view().name("vehiculos/vehiculoUpdate"));
	}

	@WithMockUser(value = "manolo", authorities= {"cliente"})
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		mockMvc.perform(post("/cliente/vehiculos/{vehiculoId}/edit", TEST_VEHICULO_ID).with(csrf())
			.param("fechaMatriculacion", "2000-12-12").param("tipoVehiculo", "turismo")
			.param("matricula", "1234ZXC").param("modelo", "a3234").param("kilometraje", "6000")
			.param("activo", "true")).andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/cliente/vehiculos"));
	}

	@WithMockUser(value = "manolo", authorities= {"cliente"})
	@Test
	void testInitUpdateFormUsuarioEquivocado() throws Exception {
		mockMvc.perform(get("/cliente/vehiculos/{vehiculoId}/edit", 3)).andExpect(status().isOk())
		.andExpect(view().name("exception"));
	}

	@WithMockUser(value = "manolo", authorities= {"cliente"})
	@Test
	void testInitDisableForm() throws Exception {
		mockMvc.perform(get("/cliente/vehiculos/{vehiculoId}/disable", TEST_VEHICULO_ID)).andExpect(status().isOk())
		.andExpect(view().name("vehiculos/disableVehiculo"));
	}

	@WithMockUser(value = "manolo", authorities= {"cliente"})
	@Test
	void testProcessDisableFormSuccess() throws Exception {
		mockMvc.perform(post("/cliente/vehiculos/{vehiculoId}/disable", 4).with(csrf())
			.param("fechaMatriculacion", "2012-09-01").param("tipoVehiculo", "turismo")
			.param("matricula", "0345FCL").param("modelo", "Mercedes AX").param("kilometraje", "1000")
			.param("activo", "true").param("id", "4")).andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/cliente/vehiculos"));
	}

	@WithMockUser(value = "manolo", authorities= {"cliente"})
	@Test
	void testInitDisableFormUsuarioEquivocado() throws Exception {
		mockMvc.perform(get("/cliente/vehiculos/{vehiculoId}/disable", 3)).andExpect(status().isOk())
		.andExpect(view().name("exception"));
	}
}