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

package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.EstadoCita;
import org.springframework.samples.petclinic.model.Vehiculo;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.ClienteService;
import org.springframework.samples.petclinic.service.VehiculoService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class CitaController {

	private static final String VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM = "citas/crearCita";
	private static final String VIEWS_CLIENTE__UPDATE_FORM = "citas/editarCita";
	private final CitaService citaService;
	private final VehiculoService vehiculoService;
	private final ClienteService clienteService;

	@Autowired
	public CitaController(final CitaService clinicService, final VehiculoService vehiculoService,final ClienteService clienteService) {
		this.citaService = clinicService;
		this.vehiculoService = vehiculoService;
		this.clienteService=clienteService;
	}

	@GetMapping(value = "/cliente/citas")
	public String showCliCitaList(final Principal principal, final Map<String, Object> model) {
		Integer idCliente = this.clienteService.findIdByUsername(principal.getName());
		Collection<Cita> results = this.citaService.findCitasByClienteId(idCliente);
		model.put("results", results);
		
		return "citas/citaList";
	}

	@GetMapping("/cliente/citas/{citaId}")
	public String showCliCitaDetalle(final Principal principal,@PathVariable("citaId") final int citaId, final Map<String, Object> model) {
		// ModelAndView mav = new ModelAndView("citas/citaEnDetalle");
		Cita cita = this.citaService.findCitaById(citaId);
		Vehiculo vehiculo = cita.getVehiculo();
		model.put("cita", cita);
		model.put("vehiculo", vehiculo);
		if(cita.getCliente().getId()!=this.clienteService.findIdByUsername(principal.getName())) {
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
	public String cancelaCita(final Principal principal,@Param(value = "citaId") final Integer citaId, final Map<String, Object> model) {
		Cita cita = this.citaService.findCitaById(citaId);
		model.put("cita", cita);
		//if(cita.getCliente().getId()!=this.citaService.findIdByUsername(principal.getName())) {
			//return "redirect:/cliente/citas/cancelar";
		//}
		// Vehiculo vehiculo = cita.getVehiculo();
		// model.addAttribute(cita);
		// model.addAttribute(vehiculo);
		return "/citas/citaCancelar";
	}

	@PostMapping(value = "/cliente/citas/{citaId}/cancelar")
	public String cancelaPostCita(final Principal principal, final Cita citaEditada, final BindingResult result,
			@Param(value = "citaId") final int citaId, final Map<String, Object> model) {

		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "/citas/citaCancelar";
		} else {
			Cita citaCambiada = this.citaService.findCitaById(citaId);
			citaCambiada.setEstadoCita(EstadoCita.cancelada);
			model.put("cita", citaCambiada);
			/*
			 * Integer idCliente = this.citaService.findIdByUsername(principal.getName());
			 *
			 * Collection<Cita> results = this.citaService.findCitasByClienteId(idCliente);
			 * List<Cita> ls = new ArrayList(results); Cita c; Integer i; for (i = 0; i <
			 * ls.size(); i++) { Cita aux = ls.get(i); if (aux.getId().equals(cita.getId()))
			 * { c = aux; results.remove(i); results.add(cita); } }
			 */
			// results.remove(o)
			// this.vehiculoService.saveVehiculo(citaCambiada.getVehiculo());
			this.citaService.saveCita(citaCambiada);
			System.out.println("Elseeeeeeeee");
			System.out.println("Entraaaaaaaaa");

			// Vehiculo vehiculo = cita.getVehiculo();
			// model.addAttribute(cita);
			// model.addAttribute(vehiculo);
			return "redirect:/cliente/citas/";
		}
	}

	/*
	 * @PostMapping(value = "/cliente/citas/{vehiculoId}/editar") public String
	 * editCitaPost(final Principal principal, final Cita citaEditada,
	 *
	 * @Param(value = "citaId") final int citaId, final BindingResult result,
	 *
	 * @PathVariable(value = "vehiculoId") final int vehiculoId, final Map<String,
	 * Object> model) { if (result.hasErrors()) { model.put("cita", citaEditada);
	 * System.out.println(result.getAllErrors()); return
	 * CitaController.VIEWS_CLIENTE__UPDATE_FORM; } else { Cita citaAntigua =
	 * this.citaService.findCitaById(citaId);
	 *
	 * BeanUtils.copyProperties(citaEditada, citaAntigua, "id", "esAceptado",
	 * "esUrgente", "tipo", "mecanico", "cliente"); Vehiculo v =
	 * this.citaService.findVehiculoById(vehiculoId); citaAntigua.setVehiculo(v);
	 * model.put("cita", citaAntigua); this.citaService.saveCita(citaAntigua);
	 * return "redirect:/cliente/citas"; } }
	 *
	 * @GetMapping(value = "/cliente/citas/vehiculo-editar") public String
	 * CitaVehiculoEditForm(final Principal principal, @Param(value = "citaId")
	 * final int citaId, final Cliente cliente, final Map<String, Object> model) {
	 *
	 * Integer clienteId = this.citaService.findIdByUsername(principal.getName());
	 * System.out.println("Cliente ID: " + clienteId);
	 * System.out.println("Cita ID: " + citaId); Cita cita =
	 * this.citaService.findCitaById(citaId); Collection<Vehiculo> vehiculo =
	 * this.citaService.findVehiculoByClienteId(clienteId); model.put("vehiculo",
	 * vehiculo); model.put("cita", cita); return "citas/citaEditarVehiculo"; }
	 */

}
