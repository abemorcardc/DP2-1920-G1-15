
package org.springframework.samples.talleres.web.integration;

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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.servlet.ModelAndView;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CitaControllerIntegrationTests {

	@Autowired
	private CitaController	citaController;

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

}
