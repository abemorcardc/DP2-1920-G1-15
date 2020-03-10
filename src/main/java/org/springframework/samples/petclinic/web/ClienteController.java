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

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cliente;
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

	private static final String		VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM	= "clientes/createOrUpdateClienteForm";

	private final ClienteService	clienteService;


	@Autowired
	public ClienteController(ClienteService clienteService, UsuarioService usuarioService, AuthoritiesService authoritiesService) {
		this.clienteService = clienteService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
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
			//creating owner, user and authorities
			clienteService.saveCliente(cliente);

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
		Collection<Cliente> results = clienteService.findClienteByApellidos(cliente.getApellidos());
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

	@GetMapping(value = "/clientes/{clienteId}/edit")
	public String initUpdateClienteForm(@PathVariable("clienteId") final int clienteId, final Model model) {
		Cliente cliente = clienteService.findClienteById(clienteId);
		model.addAttribute(cliente);
		return ClienteController.VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/clientes/{clienteId}/edit")
	public String processUpdateClienteForm(@Valid final Cliente cliente, final BindingResult result, @PathVariable("clienteId") final int clienteId) {
		if (result.hasErrors()) {
			return ClienteController.VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM;
		} else {
			cliente.setId(clienteId);
			clienteService.saveCliente(cliente);
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
		mav.addObject(clienteService.findClienteById(clienteId));
		return mav;
	}

}
