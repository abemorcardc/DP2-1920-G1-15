
package org.springframework.samples.talleres.web.integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.talleres.model.Cita;
import org.springframework.samples.talleres.model.Cliente;
import org.springframework.samples.talleres.model.EstadoCita;
import org.springframework.samples.talleres.model.Mecanico;
import org.springframework.samples.talleres.model.TipoCita;
import org.springframework.samples.talleres.model.Vehiculo;
import org.springframework.samples.talleres.service.ClienteService;
import org.springframework.samples.talleres.service.MecanicoService;
import org.springframework.samples.talleres.service.VehiculoService;
import org.springframework.samples.talleres.web.CitaController;
import org.springframework.samples.talleres.web.VisitController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.servlet.ModelAndView;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CitaControllerIntegracionTest {

	@Autowired
	private CitaController citaController;

	@Autowired
	private MecanicoService	mecanicoService;

	@Autowired
	private VehiculoService	vehiculoService;

	@Autowired
	private ClienteService	clienteService;




	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testshowMecCitaDetalle() throws Exception {
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		int citaId = 1;

		ModelAndView mav = this.citaController.showMecCitaDetalle(principal, citaId);
		Assertions.assertEquals(mav.getViewName(), "citas/citaEnDetalle");
	}

	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testShowMecCitaDetalleError() throws Exception {
		//Se comprueba que el mecánico no pueda acceder a citas que no sean suyas
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		int citaId = 3;

		ModelAndView mav = this.citaController.showMecCitaDetalle(principal, citaId);
		Assertions.assertEquals(mav.getViewName(), "exception");
	}

	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testShowMecCitaList() throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		Principal principal = SecurityContextHolder.getContext().getAuthentication();

		String view = this.citaController.showMecCitaList(principal, model);

		Assertions.assertEquals(view, "citas/citaDeMecanicoList");
	}

	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testInitUpdateMecForm() throws Exception {
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		int citaId = 1;
		ModelMap model = new ModelMap();

		String view = this.citaController.initUpdateMecForm(principal, citaId, model);
		Assertions.assertEquals(view, "citas/citaMecUpdate");
	}

	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testInitUpdateMecFormError() throws Exception {
		//Se comprueba que el mecánico no pueda acceder a citas que no sean suyas
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		int citaId = 3;
		ModelMap model = new ModelMap();

		String view = this.citaController.initUpdateMecForm(principal, citaId, model);
		Assertions.assertEquals(view, "exception");
	}

	@WithMockUser(value = "paco", authorities = {
		"mecanico"
	})
	@Test
	void testProcessUpdateMecForm() throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		int citaId = 1;
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		Cita citaEditada = new Cita();

		LocalDateTime fechaHora = LocalDateTime.of(2027, 04, 05, 10, 30);
		citaEditada.setFechaCita(fechaHora);

		citaEditada.setDescripcion("Averia del motor");
		citaEditada.setEsUrgente(true);
		citaEditada.setTipo(TipoCita.reparacion);
		citaEditada.setCoste(100.00);
		citaEditada.setTiempo(120);
		citaEditada.setEstadoCita(EstadoCita.aceptada);

		int mecanicoId = this.mecanicoService.findMecIdByUsername("paco");
		Mecanico mecanico = this.mecanicoService.findMecanicoById(mecanicoId);
		citaEditada.setMecanico(mecanico);

		Cliente cliente = this.clienteService.findClienteById(1);
		citaEditada.setCliente(cliente);

		Vehiculo vehiculo = this.vehiculoService.findVehiculosByClienteId(cliente.getId()).stream().findFirst().get();
		citaEditada.setVehiculo(vehiculo);

		String view = this.citaController.processUpdateMecForm(citaEditada, result, citaId, model);

		Assertions.assertEquals(view, "redirect:/mecanicos/citas/");
	}

	//Hay que ver como hacerlo para que funcione
	//	@WithMockUser(value = "paco", authorities = {
	//		"mecanico"
	//	})
	//	@Test
	//	void testProcessUpdateMecFormError() throws Exception {
	//		Map<String, Object> model = new HashMap<String, Object>();
	//		int citaId = 1;
	//		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
	//		Cita citaEditada = new Cita();
	//
	//		LocalDateTime fechaHora = LocalDateTime.of(2019, 04, 05, 10, 30);
	//		citaEditada.setFechaCita(fechaHora);
	//
	//		citaEditada.setDescripcion("Averia del motor");
	//		citaEditada.setEsUrgente(true);
	//		citaEditada.setTipo(TipoCita.reparacion);
	//		citaEditada.setCoste(100.00);
	//		citaEditada.setTiempo(120);
	//		citaEditada.setEstadoCita(EstadoCita.aceptada);
	//
	//		int mecanicoId = this.mecanicoService.findMecIdByUsername("paco");
	//		Mecanico mecanico = this.mecanicoService.findMecanicoById(mecanicoId);
	//		citaEditada.setMecanico(mecanico);
	//
	//		Cliente cliente = this.clienteService.findClienteById(1);
	//		citaEditada.setCliente(cliente);
	//
	//		Vehiculo vehiculo = this.vehiculoService.findVehiculosByClienteId(cliente.getId()).stream().findFirst().get();
	//		citaEditada.setVehiculo(vehiculo);
	//
	//		String view = this.citaController.processUpdateMecForm(citaEditada, result, citaId, model);
	//
	//		Assertions.assertEquals(view, "redirect:/mecanicos/citas/");
	//	}


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
