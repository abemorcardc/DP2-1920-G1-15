
package org.springframework.samples.talleres.integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
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
import org.springframework.samples.talleres.web.CitaController;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CitaControllerIntegracionTests {
	
	@Autowired
	private CitaController citaController;
	
	//Historia 1

	@WithMockUser(value = "manolo", roles = "cliente")
	@Test
	void testClienteCitaList() throws Exception {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		Map<String,Object> model= new HashMap<String, Object>();
		
		String view= this.citaController.showCliCitaList(principal, model);
		
		assertEquals(view,"citas/citaList");
	}

	// Historia 2

	@WithMockUser(value = "manolo", roles = "cliente")
	@Test
	void testClienteCitaShow() throws Exception {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		Map<String,Object> model= new HashMap<String, Object>();
		
		
		String view= this.citaController.showCliCitaDetalle(principal, 1, model);
		assertEquals(view,"citas/citaEnDetalle");
	}

	// Historia 3
	@WithMockUser(value = "manolo", roles="cliente")
	@Test
	void testClienteInitCitaCreation() throws Exception {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		Map<String,Object> model= new HashMap<String, Object>();
		
		Cliente cliente=new Cliente();
		
		cliente.setApellidos("Martín");
		cliente.setDireccion("C/Tarfia");
		cliente.setDni("77844576X");
		cliente.setEmail("Manolo72@gmail.com");
		cliente.setId(1);
		cliente.setNombre("Manolo");
		cliente.setTelefono("608555102");
		
		
		String view= this.citaController.initCitaCreationForm(principal, cliente, model);
		
		assertEquals(view,"citas/crearCita");
	}
	
	@WithMockUser(value = "manolo", roles="cliente")
	@Test
	void testClienteCitaCreation() throws Exception {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		Map<String,Object> model= new HashMap<String, Object>();
		
		Cita cita=new Cita();
		
		cita.setCoste(120.0);
		cita.setDescripcion("Problemas con el motor");
		cita.setEstadoCita(EstadoCita.pendiente);
		cita.setEsUrgente(true);
		cita.setFechaCita(LocalDateTime.of(2021,03,14, 12,00));
		cita.setTiempo(40);
		cita.setTipo(TipoCita.reparacion);
		
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		
		String view= this.citaController.citaCreation(principal, cita, result, 1, model);
		
		assertEquals(view,"redirect:/cliente/citas/");
	}
	
	@WithMockUser(value = "manolo", roles="cliente")
	@Test
	void testClienteCitaVehiculoCreation() throws Exception {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		Map<String,Object> model= new HashMap<String, Object>();
		
		Cliente cliente=new Cliente();
		
		cliente.setApellidos("Martín");
		cliente.setDireccion("C/Tarfia");
		cliente.setDni("77844576X");
		cliente.setEmail("Manolo72@gmail.com");
		cliente.setId(1);
		cliente.setNombre("Manolo");
		cliente.setTelefono("608555102");
				
		String view= this.citaController.CitaVehiculoCreationForm(principal, cliente, model);
		
		assertEquals(view,"citas/citaVehiculo");
	}
	
	
	//Historia 4
	@WithMockUser(value = "manolo", roles="cliente")
	@Test
	void testClienteCitaCancela() throws Exception {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		Map<String,Object> model= new HashMap<String, Object>();
		
				
		String view= this.citaController.cancelaCita(principal, 1, model);
		
		assertEquals(view,"/citas/citaCancelar");
	}
	
	@WithMockUser(value = "manolo", roles="cliente")
	@Test
	void testClientePostCitaCancela() throws Exception {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		Map<String,Object> model= new HashMap<String, Object>();
		
		Cita citaEditada=new Cita();
		
		citaEditada.setCoste(120.0);
		citaEditada.setDescripcion("Problemas con el motor");
		citaEditada.setEstadoCita(EstadoCita.pendiente);
		citaEditada.setEsUrgente(true);
		citaEditada.setFechaCita(LocalDateTime.of(2021,03,14, 12,00));
		citaEditada.setTiempo(40);
		citaEditada.setTipo(TipoCita.reparacion);
				
		
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		
		String view= this.citaController.cancelaPostCita(principal, citaEditada, result, 1, model);
		
		assertEquals(view,"redirect:/cliente/citas/");
	}
	

	

}
