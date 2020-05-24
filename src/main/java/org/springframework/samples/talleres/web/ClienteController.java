
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
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.talleres.model.Cita;
import org.springframework.samples.talleres.model.Cliente;
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
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class ClienteController {

	private static final String		VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM	= "citas/crearCita";
	//private static final String VIEWS_CLIENTE__UPDATE_FORM = "citas/editarCita";
	private final ClienteService	clienteService;
	private final MecanicoService	mecanicoService;
	private final CitaService		citaService;
	private final VehiculoService	vehiculoService;


	@Autowired
	public ClienteController(final ClienteService clienteService, final CitaService citaService, final MecanicoService mecanicoService, final VehiculoService vehiculoService) {
		this.clienteService = clienteService;
		this.citaService = citaService;
		this.mecanicoService = mecanicoService;
		this.vehiculoService = vehiculoService;
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

	@GetMapping(value = "/clientes/{idCliente}/edit")
	public String UpdateCliente(@PathVariable("idCliente") final int clienteId, final Model model) {

		Cliente cliente = this.clienteService.findClienteById(clienteId);
		model.addAttribute(cliente);
		return ClienteController.VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/clientes/{idCliente}/edit")
	public String UpdateCliente(@Valid final Cliente cliente, final BindingResult result, @PathVariable("idCliente") final int clienteId) {
		if (result.hasErrors()) {
			return ClienteController.VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM;
		} else {
			cliente.setId(clienteId);
			this.clienteService.saveCliente(cliente);
			return "redirect:/clientes/{clienteId}";
		}
	}

	@GetMapping("/clientes/{clienteId}")
	public ModelAndView showCliente(@PathVariable("clienteId") final int clienteId) {
		ModelAndView mav = new ModelAndView("clientes/clienteDetails");
		mav.addObject(this.clienteService.findClienteById(clienteId));
		return mav;
	}
	// ---------------------------------------------------------
	// METODOS MECANICOS-CLIENTES

	/*
	 * Historia 16: Mecánico muestra Cliente
	 *
	 * Como mecánico quiero poder ver la información de un cliente para poder obtener más información acerca de éste.
	 */
	@GetMapping("/mecanicos/cliente/{clienteId}")
	public String mecShowCliente(final Principal principal, @PathVariable("clienteId") final int clienteId, final Map<String, Object> model) {
		//Buscamos el cliente y lo añadimos al modelo
		Cliente cliente = this.clienteService.findClienteById(clienteId);
		model.put("cliente", cliente);
		//Buscamos todos los vehiculos del cliente y lo añadimos al modelo
		List<Vehiculo> vehiculos = (List<Vehiculo>) this.vehiculoService.findVehiculosByClienteId(clienteId);
		model.put("vehiculos", vehiculos);

		/*
		 * Buscamos todas las citas del cliente y comprobamos que el mecanico con el que se accede
		 * tiene alguna cita con dicho cliente
		 */
		List<Cita> citas = (List<Cita>) this.citaService.findCitasByClienteId(clienteId);
		Integer mecanicoId = this.mecanicoService.findMecIdByUsername(principal.getName());
		Boolean pertenece = false;
		for (Cita cita : citas) {
			if (cita.getMecanico().getId() == mecanicoId) {
				pertenece = true;
				break;
			}
		}
		if (!pertenece) {
			return "exception";
		}

		return "clientes/clienteEnDetalle";
	}
}
