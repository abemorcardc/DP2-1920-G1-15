
package org.springframework.samples.talleres.web.e2e;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.talleres.model.TipoVehiculo;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-mysql.properties")
class VehiculoControllerE2ETest {

	private static final int	TEST_VEHICULO_ID	= 1;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(value = "manolo", authorities = {
		"cliente"
	})
	@Test
	void testListVehiculoByCliente() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/vehiculos")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("results"))
			.andExpect(MockMvcResultMatchers.view().name("vehiculos/vehiculoList"));
	}

	@WithMockUser(value = "manolo", authorities = {
		"cliente"
	})
	@Test
	void testShowVehiculo() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/vehiculos/{vehiculoId}", VehiculoControllerE2ETest.TEST_VEHICULO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("vehiculo", Matchers.hasProperty("tipoVehiculo", Matchers.is(TipoVehiculo.turismo))))
			.andExpect(MockMvcResultMatchers.model().attribute("vehiculo", Matchers.hasProperty("matricula", Matchers.is("2345FCL")))).andExpect(MockMvcResultMatchers.model().attribute("vehiculo", Matchers.hasProperty("modelo", Matchers.is("Mercedes A"))))
			.andExpect(MockMvcResultMatchers.model().attribute("vehiculo", Matchers.hasProperty("kilometraje", Matchers.is(10000)))).andExpect(MockMvcResultMatchers.view().name("vehiculos/vehiculoEnDetalle"));
	}

	@WithMockUser(value = "manolo", authorities = {
		"cliente"
	})
	@Test
	void testShowVehiculoUsuarioEquivocado() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/vehiculos/{vehiculoId}", 3)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "lolo", authorities = {
		"mecanico"
	})
	@Test
	void testShowVehiculoMecanico() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/vehiculos/{vehiculoId}", 2)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("vehiculo", Matchers.hasProperty("tipoVehiculo", Matchers.is(TipoVehiculo.turismo))))
			.andExpect(MockMvcResultMatchers.model().attribute("vehiculo", Matchers.hasProperty("matricula", Matchers.is("5125DRF"))))
			.andExpect(MockMvcResultMatchers.model().attribute("vehiculo", Matchers.hasProperty("modelo", Matchers.is("Peugeot 307")))).andExpect(MockMvcResultMatchers.model().attribute("vehiculo", Matchers.hasProperty("kilometraje", Matchers.is(15000))))
			.andExpect(MockMvcResultMatchers.view().name("vehiculos/vehiculoEnDetalle"));
	}

	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testShowVehiculoMecanicoEquivocado() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/mecanicos/vehiculos/{vehiculoId}", 2)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "manolo", authorities = {
		"cliente"
	})
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/vehiculos/crear")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("vehiculo"))
			.andExpect(MockMvcResultMatchers.view().name("vehiculos/crearVehiculo"));
	}

	@WithMockUser(value = "manolo", authorities = {
		"cliente"
	})
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/cliente/vehiculos/crear").with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaMatriculacion", "2000-12-12").param("tipoVehiculo", "turismo").param("matricula", "1234ZXC")
			.param("modelo", "a3234").param("kilometraje", "3000").param("activo", "true")).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(value = "manolo", authorities = {
		"cliente"
	})
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/cliente/vehiculos/crear").with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaMatriculacion", "2000-12-12").param("tipoVehiculo", "turismo").param("matricula", "1234ZXC").param("modelo", "a3234")
				.param("kilometraje", "-3000").param("activo", "true"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("vehiculo", "kilometraje")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("vehiculos/crearVehiculo"));
	}

	@WithMockUser(value = "manolo", authorities = {
		"cliente"
	})
	@Test
	void testInitUpdateForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/vehiculos/{vehiculoId}/edit", VehiculoControllerE2ETest.TEST_VEHICULO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("vehiculo")).andExpect(MockMvcResultMatchers.view().name("vehiculos/vehiculoUpdate"));
	}

	@WithMockUser(value = "manolo", authorities = {
		"cliente"
	})
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/cliente/vehiculos/{vehiculoId}/edit", VehiculoControllerE2ETest.TEST_VEHICULO_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaMatriculacion", "2000-12-12")
				.param("tipoVehiculo", "turismo").param("matricula", "1234ZXC").param("modelo", "a3234").param("kilometraje", "6000").param("activo", "true"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/cliente/vehiculos"));
	}

	@WithMockUser(value = "manolo", authorities = {
		"cliente"
	})
	@Test
	void testInitUpdateFormUsuarioEquivocado() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/vehiculos/{vehiculoId}/edit", 3)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "manolo", authorities = {
		"cliente"
	})
	@Test
	void testInitDisableForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/vehiculos/{vehiculoId}/disable", VehiculoControllerE2ETest.TEST_VEHICULO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("vehiculos/disableVehiculo"));
	}

	@WithMockUser(value = "manolo", authorities = {
		"cliente"
	})
	@Test
	void testProcessDisableFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/cliente/vehiculos/{vehiculoId}/disable", 4).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaMatriculacion", "2012-09-01").param("tipoVehiculo", "turismo")
			.param("matricula", "0345FCL").param("modelo", "Mercedes AX").param("kilometraje", "1000").param("activo", "true").param("id", "4")).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/cliente/vehiculos"));
	}

	@WithMockUser(value = "manolo", authorities = {
		"cliente"
	})
	@Test
	void testInitDisableFormUsuarioEquivocado() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/vehiculos/{vehiculoId}/disable", 3)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}
}
