/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.talleres.web;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.talleres.model.Cita;
import org.springframework.samples.talleres.model.Cliente;
import org.springframework.samples.talleres.model.EstadoCita;
import org.springframework.samples.talleres.model.Vehiculo;
import org.springframework.samples.talleres.service.CitaService;
import org.springframework.samples.talleres.service.ClienteService;
import org.springframework.samples.talleres.service.MecanicoService;
import org.springframework.samples.talleres.service.VehiculoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class CitaController {

	private final CitaService citaService;
	private final MecanicoService mecanicoService;
	private final VehiculoService vehiculoService;
	private final ClienteService clienteService;

	private static final String VIEWS_MEC_UPDATE_FORM = "citas/citaMecUpdate";
	private static final String VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM = "citas/crearCita";
	// private static final String VIEWS_CLIENTE__UPDATE_FORM = "citas/editarCita";

	@Autowired
	public CitaController(final MecanicoService mecanicoService, final CitaService citaService,
			final VehiculoService vehiculoService, final ClienteService clienteService) {
		this.mecanicoService = mecanicoService;
		this.citaService = citaService;
		this.vehiculoService = vehiculoService;
		this.clienteService = clienteService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("citaId");

	}

	// METODOS MECANICOS-CITAS
	@GetMapping("/mecanicos/citas")
	public String showMecCitaList(final Principal principal, final Map<String, Object> model) {
		Integer mecanicoId = this.mecanicoService.findMecIdByUsername(principal.getName());
		Collection<Cita> results = this.citaService.findCitasByMecanicoId(mecanicoId);
		model.put("results", results);
		return "citas/citaDeMecanicoList";
	}

	@GetMapping("/mecanicos/citas/{citaId}")
	public ModelAndView showMecCitaDetalle(@PathVariable("citaId") final int citaId) {
		ModelAndView mav = new ModelAndView("citas/citaEnDetalle");
		mav.addObject(this.citaService.findCitaById(citaId));
		return mav;
	}

	@GetMapping(value = "/mecanicos/citas/{citaId}/edit")
	public String initUpdateMecForm(@PathVariable("citaId") final int citaId, final Model model) {
		Cita cita = this.citaService.findCitaById(citaId);
		model.addAttribute(cita);
		return CitaController.VIEWS_MEC_UPDATE_FORM;
	}

	@PostMapping(value = "/mecanicos/citas/{citaId}/edit")
	public String processUpdateMecForm(@Valid final Cita citaEditada, @PathVariable("citaId") final int citaId,
			final BindingResult result, final Map<String, Object> model) {

		if (citaEditada.getCliente() == null) {
			citaEditada.setCliente(this.citaService.findCitaById(citaId).getCliente());
		}
		if (citaEditada.getMecanico() == null) {
			citaEditada.setMecanico(this.citaService.findCitaById(citaId).getMecanico());
		}
		if (citaEditada.getTipo() == null) {
			citaEditada.setTipo(this.citaService.findCitaById(citaId).getTipo());
		}
		if (citaEditada.getVehiculo() == null) {
			citaEditada.setVehiculo(this.citaService.findCitaById(citaId).getVehiculo());
		}
		if (citaEditada.getFechaCita().isBefore(LocalDateTime.now())) {
			// model.put("cita", citaEditada);
			return CitaController.VIEWS_MEC_UPDATE_FORM;
		}
		if (result.hasErrors()) {
			model.put("cita", citaEditada);
			return CitaController.VIEWS_MEC_UPDATE_FORM;
		} else {
			Cita citaAntigua = this.citaService.findCitaById(citaId);

			BeanUtils.copyProperties(citaEditada, citaAntigua, "id", "esUrgente", "tipo", "mecanico", "cliente",
					"vehiculo"); // coge los nuevos

			this.citaService.saveCita(citaAntigua);

			return "redirect:/mecanicos/citas/";
		}
	}

	// ---------------------------------------------------------
	// METODOS CLIENTES-CITAS

	@GetMapping(value = "/cliente/citas")
	public String showCliCitaList(final Principal principal, final Map<String, Object> model) {
		Integer idCliente = this.clienteService.findIdByUsername(principal.getName());
		Collection<Cita> results = this.citaService.findCitasByClienteId(idCliente);
		model.put("results", results);
		return "citas/citaList";
	}

	@GetMapping("/cliente/citas/{citaId}")
	public String showCliCitaDetalle(final Principal principal, @PathVariable("citaId") final int citaId,
			final Map<String, Object> model) {
		// ModelAndView mav = new ModelAndView("citas/citaEnDetalle");
		Cita cita = this.citaService.findCitaById(citaId);
		Vehiculo vehiculo = cita.getVehiculo();
		model.put("cita", cita);
		model.put("vehiculo", vehiculo);
		if (cita.getCliente().getId() != this.clienteService.findIdByUsername(principal.getName())) {
			return "redirect:/cliente/citas";
		}

		// mav.addObject(this.citaService.findCitaById(citaId));
		return "citas/citaEnDetalle";
	}

	@GetMapping(value = "/cliente/citas/new")
	public String citaCreation(final Principal principal, final Cliente cliente, final Map<String, Object> model) {
		Cita cita = new Cita();
		Integer idCliente = this.clienteService.findIdByUsername(principal.getName());
		Collection<Cita> results = this.citaService.findCitasByClienteId(idCliente);
		results.add(cita);
		model.put("results", results);
		return "citas/citaList";
	}

	@GetMapping(value = "/cliente/citas/pedir")
	public String initCitaCreationForm(final Principal principal, final Cliente cliente,
			final Map<String, Object> model) {
		Cita cita = new Cita();
		Integer clienteId = this.clienteService.findIdByUsername(principal.getName());
		Collection<Vehiculo> vehiculo = this.vehiculoService.findVehiculosByClienteId(clienteId);

		model.put("vehiculo", vehiculo);
		model.put("cita", cita);
		return "citas/crearCita";
	}

	@PostMapping(value = "/cliente/citas/pedir")
	public String citaCreation(final Principal principal, @Valid final Cita cita, final BindingResult result,
			@Param(value = "vehiculoId") final Integer vehiculoId, final Map<String, Object> model) {

		if (vehiculoId == null) {
			return "redirect:/cliente/citas/vehiculo";
		} else {
			cita.setVehiculo(this.vehiculoService.findVehiculoById(vehiculoId));
		}

		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());

			return CitaController.VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM;

		} else {
			Integer idCliente = this.clienteService.findIdByUsername(principal.getName());
			Collection<Cita> results = this.citaService.findCitasByClienteId(idCliente);
			cita.setCliente(this.clienteService.findClienteById(idCliente));
			cita.setEstadoCita(EstadoCita.pendiente);

			results.add(cita);
			// this.clienteService.saveVehiculo(vehiculo);
			this.citaService.saveCita(cita);
			model.put("results", results);
			return "redirect:/cliente/citas/";

		}
	}

	@GetMapping(value = "/cliente/citas/vehiculo")
	public String CitaVehiculoCreationForm(final Principal principal, final Cliente cliente,
			final Map<String, Object> model) {

		Integer clienteId = this.clienteService.findIdByUsername(principal.getName());
		Collection<Vehiculo> vehiculo = this.vehiculoService.findVehiculosByClienteId(clienteId);

		model.put("results", vehiculo);
		return "citas/citaVehiculo";
	}

	@GetMapping(value = "/cliente/citas/{citaId}/cancelar")
	public String cancelaCita(final Principal principal, @PathVariable(value = "citaId") final Integer citaId,
			final Map<String, Object> model) {
		Cita cita = this.citaService.findCitaById(citaId);
		if (cita.getCliente().getId() != this.clienteService.findIdByUsername(principal.getName())) {
			return "redirect:/cliente/citas";
		}
		model.put("cita", cita);
		return "/citas/citaCancelar";
	}

	@PostMapping(value = "/cliente/citas/{citaId}/cancelar")
	public String cancelaPostCita(final Principal principal, final Cita citaEditada, final BindingResult result,
			@PathVariable(value = "citaId") final int citaId, final Map<String, Object> model) {

		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "/citas/citaCancelar";
		} else {
			Cita citaCambiada = this.citaService.findCitaById(citaId);
			if (citaCambiada.getCliente().getId() != this.clienteService.findIdByUsername(principal.getName())) {
				return "redirect:/cliente/citas";
			} else {
				citaCambiada.setEstadoCita(EstadoCita.cancelada);
				model.put("cita", citaCambiada);
				this.citaService.saveCita(citaCambiada);
				return "redirect:/cliente/citas/";
			}
		}
	}

}
