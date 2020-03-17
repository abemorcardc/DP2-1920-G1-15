
/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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
import org.springframework.samples.petclinic.model.Vehiculo;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ClienteService;
import org.springframework.samples.petclinic.service.UsuarioService;
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
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class ClienteController {

	private static final String VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM = "citas/crearCita";

	private final ClienteService clienteService;

	@Autowired

	public ClienteController(final ClienteService clienteService, final UsuarioService usuarioService,
			final AuthoritiesService authoritiesService) {
		this.clienteService = clienteService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("idCliente");
	}

	@GetMapping(value = "/clientes/new")
	public String initCreationForm(final Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		return ClienteController.VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/clientes/new")
	public String processCreationForm(@Valid final Cliente cliente, final BindingResult result) {
		if (result.hasErrors()) {
			return ClienteController.VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM;
		} else {

			// creating owner, user and authorities
			this.clienteService.saveCliente(cliente);

			return "redirect:/clientes/" + cliente.getId();
		}
	}

	@GetMapping(value = "/clientes/find")
	public String initFindForm(final Map<String, Object> model) {
		model.put("cliente", new Cliente());
		return "clientes/findClientes";
	}


	@GetMapping(value = "/clientes")
	public String processFindForm(Cliente cliente, final BindingResult result, final Map<String, Object> model) {

		// allow parameterless GET request for /owners to return all records
		if (cliente.getApellidos() == null) {
			cliente.setApellidos(""); // empty string signifies broadest possible search
		}

		// find owners by last name
		Collection<Cliente> results = this.clienteService.findClienteByApellidos(cliente.getApellidos());
		if (results.isEmpty()) {
			// no owners found
			result.rejectValue("apellidos", "notFound", "not found");
			return "clientes/findClientes";
		} else if (results.size() == 1) {
			// 1 owner found
			cliente = results.iterator().next();
			return "redirect:/clientes/" + cliente.getId();
		} else {
			// multiple owners found
			model.put("selections", results);
			return "clientes/clientesList";
		}
	}



	@GetMapping(value = "/clientes/{idCliente}/edit")
	public String initUpdateOwnerForm(@PathVariable("idCliente") final int clienteId, final Model model) {

		Cliente cliente = this.clienteService.findClienteById(clienteId);
		model.addAttribute(cliente);
		return ClienteController.VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/clientes/{idCliente}/edit")
	public String processUpdateOwnerForm(@Valid final Cliente cliente, final BindingResult result,
			@PathVariable("idCliente") final int clienteId) {
		if (result.hasErrors()) {
			return ClienteController.VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM;
		} else {
			cliente.setId(clienteId);
			this.clienteService.saveCliente(cliente);
			return "redirect:/clientes/{clienteId}";
		}
	}


	/**
	 * Custom handler for displaying an owner.
	 *
	 * @param ownerId
	 *            the ID of the owner to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@GetMapping("/clientes/{clienteId}")
	public ModelAndView showCliente(@PathVariable("clienteId") final int clienteId) {
		ModelAndView mav = new ModelAndView("clientes/clienteDetails");
		mav.addObject(this.clienteService.findClienteById(clienteId));
		return mav;
	}

	@GetMapping(value = "/cliente/vehiculos")
	public String showCliVehiculoList(final Principal principal, final Map<String, Object> model) {
		Integer idCliente = this.clienteService.findIdByUsername(principal.getName());
		Collection<Vehiculo> results = this.clienteService.findVehiculosByClienteId(idCliente);
		model.put("results", results);
		return "vehiculos/vehiculoList";
	}

	@GetMapping("/cliente/vehiculos/{vehiculoId}")
	public ModelAndView showCliVehiculoDetalle(@PathVariable("vehiculoId") final int vehiculoId) {
		ModelAndView mav = new ModelAndView("vehiculos/vehiculoEnDetalle");
		mav.addObject(this.clienteService.findVehiculoById(vehiculoId));
		return mav;
	}

	@GetMapping(value = "/cliente/citas")
	public String showCliCitaList(final Principal principal, final Map<String, Object> model) {
		Integer idCliente = this.clienteService.findIdByUsername(principal.getName());
		Collection<Cita> results = this.clienteService.findCitasByClienteId(idCliente);
		model.put("results", results);
		return "citas/citaList";
	}

	@GetMapping("/cliente/citas/{citaId}")
	public ModelAndView showCliCitaDetalle(@PathVariable("citaId") final int citaId) {
		ModelAndView mav = new ModelAndView("citas/citaEnDetalle");
		mav.addObject(this.clienteService.findCitaById(citaId));
		return mav;
	}

	@GetMapping(value = "/cliente/citas/new")
	public String citaCreation(final Principal principal, final Cliente cliente, final Map<String, Object> model) {
		Cita cita = new Cita();
		Integer idCliente = this.clienteService.findIdByUsername(principal.getName());
		Collection<Cita> results = this.clienteService.findCitasByClienteId(idCliente);
		results.add(cita);
		model.put("results", results);
		return "citas/citaList";
	}

	@GetMapping(value = "/cliente/citas/pedir")
	public String initCitaCreationForm(final Principal principal, final Cliente cliente,
			final Map<String, Object> model) {
		Cita cita = new Cita();
		model.put("cita", cita);
		return "citas/crearCita";
	}

	@PostMapping(value = "/cliente/citas/pedir")
	public String citaCreation(final Principal principal, @Valid final Cita cita, final BindingResult result,@Param(value="vehiculoId") final int vehiculoId,
			final Map<String, Object> model) {
		
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			
			return ClienteController.VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM;
			
		} else {
			Integer idCliente = this.clienteService.findIdByUsername(principal.getName());
			Collection<Cita> results = this.clienteService.findCitasByClienteId(idCliente);
			cita.setCliente(this.clienteService.findClienteById(idCliente));
			cita.setEsAceptado(false);
		
			cita.setVehiculo(this.clienteService.findVehiculoById(vehiculoId));
			results.add(cita);
			this.clienteService.saveCita(cita);
			model.put("results", results);
			return "citas/citaList";
		}
	}
	
	@GetMapping(value = "/cliente/citas/vehiculo")
	public String CitaVehiculoCreationForm(final Principal principal, final Cliente cliente,
			final Map<String, Object> model) {
		
		Integer clienteId= this.clienteService.findIdByUsername(principal.getName());
		Collection<Vehiculo> vehiculo= this.clienteService.findVehiculosByClienteId(clienteId);
		
		model.put("results",vehiculo);
		return "citas/citaVehiculo";
	}
	
	

}
