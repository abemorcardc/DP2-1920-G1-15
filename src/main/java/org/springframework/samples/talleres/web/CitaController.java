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
import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.talleres.model.Cita;
import org.springframework.samples.talleres.model.Cliente;
import org.springframework.samples.talleres.model.EstadoCita;
import org.springframework.samples.talleres.model.Mecanico;
import org.springframework.samples.talleres.model.Vehiculo;
import org.springframework.samples.talleres.service.CitaService;
import org.springframework.samples.talleres.service.ClienteService;
import org.springframework.samples.talleres.service.MecanicoService;
import org.springframework.samples.talleres.service.VehiculoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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

	private final CitaService		citaService;
	private final MecanicoService	mecanicoService;
	private final VehiculoService	vehiculoService;
	private final ClienteService	clienteService;

	private static final String		VIEWS_MEC_UPDATE_FORM				= "citas/citaMecUpdate";
	private static final String		VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM	= "citas/crearCita";


	@Autowired
	public CitaController(final MecanicoService mecanicoService, final CitaService citaService, final VehiculoService vehiculoService, final ClienteService clienteService) {
		this.mecanicoService = mecanicoService;
		this.citaService = citaService;
		this.vehiculoService = vehiculoService;
		this.clienteService = clienteService;
	}

	@InitBinder("cita")
	public void initCitaBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new CitaValidator());
	}

	// ----------------------------METODOS MECANICOS-CITAS------------------------
	@GetMapping("/mecanicos/citas")			// lista de las citas del mecanico 
	public String showMecCitaList(final Principal principal, final Map<String, Object> model) {
		Integer mecanicoId = this.mecanicoService.findMecIdByUsername(principal.getName());
		Collection<Cita> results = this.citaService.findCitasByMecanicoId(mecanicoId);

		model.put("results", results);
		return "citas/citaDeMecanicoList";
	}

	@GetMapping("/mecanicos/citas/{citaId}")		// cita en detalle 
	public ModelAndView showMecCitaDetalle(final Principal principal, @PathVariable("citaId") final int citaId) {
		ModelAndView mav = new ModelAndView("citas/citaEnDetalle");
		mav.addObject(this.citaService.findCitaById(citaId));
		Cita cita = this.citaService.findCitaById(citaId);
		Integer mecanicoId = this.mecanicoService.findMecIdByUsername(principal.getName());

		if (cita.getMecanico().getId() != mecanicoId) {
			ModelAndView exception = new ModelAndView("exception");
			return exception;
		}
		return mav;
	}

	@GetMapping("/mecanicos/citasPendientes")		//lista de citas pendientes, las que no tienen mecanico asignado
	public String listMecCitasPendiente(final Principal principal, final Map<String, Object> model) {
		Collection<Cita> results = this.citaService.findCitasSinAsignar();

		model.put("results", results);
		return "citas/citasPendientesMecList";
	}
	@GetMapping("/mecanicos/citasP/{citaId}")		// el mecanico puede ver los detalles antes de aceptarla
	public ModelAndView showMecCitaDetalleP(final Principal principal, @PathVariable("citaId") final int citaId) {
		ModelAndView mav = new ModelAndView("citas/citaEnDetallePendiente");

		mav.addObject(this.citaService.findCitaById(citaId));
		return mav;
	}
	@GetMapping(value = "/mecanicos/citas/{citaId}/aceptar")		//el mecanico le da al boton de aceptar cita
	public String aceptaCita(final Principal principal, @PathVariable(value = "citaId") final Integer citaId, final Map<String, Object> model) {
		Cita cita = this.citaService.findCitaById(citaId);
		if(cita.getMecanico() != null || cita ==  null) {
			return "exception";
		}
		model.put("cita", cita);
		return "/citas/aceptarCita";
	}

	@PostMapping(value = "/mecanicos/citas/{citaId}/aceptar")		//el mecanico le da confirmar para aceptar cita
	public String aceptaPostCita(final Principal principal, final Cita citaEditada, final BindingResult result, @PathVariable(value = "citaId") final int citaId, final ModelMap model) {
		Cita citaOrigen = this.citaService.findCitaById(citaId);
		BeanUtils.copyProperties(citaOrigen, citaEditada, "mecanico");
		int idMec = this.mecanicoService.findMecIdByUsername(principal.getName());
		Mecanico mecanico = this.mecanicoService.findMecanicoById(idMec);
		if(citaOrigen.getMecanico() != null || citaOrigen == null) {
			return "exception";
		}

		citaOrigen.setEstadoCita(EstadoCita.aceptada);
		citaOrigen.setMecanico(mecanico);
		model.put("cita", citaOrigen);
		try {
			this.citaService.saveCita(citaOrigen);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}

		return "redirect:/mecanicos/citas/";
	}

	@GetMapping(value = "/mecanicos/citas/{citaId}/edit")		//el mecanico quiere actualizar cita
	public String initUpdateMecForm(final Principal principal, @PathVariable("citaId") final int citaId, final ModelMap model) {
		Cita cita = this.citaService.findCitaById(citaId);
		model.addAttribute(cita);
		Integer mecanicoId = this.mecanicoService.findMecIdByUsername(principal.getName());

		if (cita.getMecanico().getId() != mecanicoId) { //comprobar que no es otro mecanico que quiere acceder 
			return "exception";
		}
		return CitaController.VIEWS_MEC_UPDATE_FORM;
	}

	@PostMapping(value = "/mecanicos/citas/{citaId}/edit")		//el mecanico actualiza cita
	public String processUpdateMecForm(@Valid final Cita citaEditada, final BindingResult result, @PathVariable("citaId") final int citaId, final Map<String, Object> model) {
		Cita citaAntigua = this.citaService.findCitaById(citaId);
		BeanUtils.copyProperties(citaEditada, citaAntigua, "id", "esUrgente", "tipo", "mecanico", "cliente", "vehiculo"); // coge los nuevos
		this.comprobarAtributosCita(citaEditada, citaId);

		if (result.hasErrors()) {
			return CitaController.VIEWS_MEC_UPDATE_FORM;
		} else {
			this.citaService.saveCita(citaAntigua);
			return "redirect:/mecanicos/citas/";
		}

	}

	private void comprobarAtributosCita(final Cita citaEditada, final int citaId) {		//método para establecer estos atributos que no se copian 
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
	}

	// ---------------------------------------------------------------
	// ---------------------METODOS CLIENTES-CITAS---------------------

	@GetMapping(value = "/cliente/citas")		// lista de las citas del cliente
	public String showCliCitaList(final Principal principal, final Map<String, Object> model) {
		Integer idCliente = this.clienteService.findIdByUsername(principal.getName());
		Collection<Cita> results = this.citaService.findCitasByClienteId(idCliente);

		model.put("results", results);
		return "citas/citaList";
	}

	@GetMapping("/cliente/citas/{citaId}")		// cita en detalle
	public String showCliCitaDetalle(final Principal principal, @PathVariable("citaId") final int citaId, final Map<String, Object> model) {
		Cita cita = this.citaService.findCitaById(citaId);
		Vehiculo vehiculo = cita.getVehiculo();

		model.put("cita", cita);
		model.put("vehiculo", vehiculo);
		if (cita.getCliente().getId() != this.clienteService.findIdByUsername(principal.getName())) { //comprobar que no esta accediendo otro cliente
			return "redirect:/cliente/citas";
		}
		return "citas/citaEnDetalle";
	}

	@GetMapping(value = "/cliente/citas/new")		// ?
	public String citaCreation(final Principal principal, final Cliente cliente, final Map<String, Object> model) {
		Cita cita = new Cita();
		Integer idCliente = this.clienteService.findIdByUsername(principal.getName());
		Collection<Cita> results = this.citaService.findCitasByClienteId(idCliente);

		results.add(cita);
		model.put("results", results);
		return "citas/citaList";
	}

	@GetMapping(value = "/cliente/citas/pedir")		//el cliente quiere pedir una cita
	public String initCitaCreationForm(final Principal principal, final Cliente cliente, final Map<String, Object> model) {
		Cita cita = new Cita();
		Integer clienteId = this.clienteService.findIdByUsername(principal.getName());
		Collection<Vehiculo> vehiculo = this.vehiculoService.findVehiculosByClienteId(clienteId);

		model.put("vehiculo", vehiculo);
		model.put("cita", cita);
		return "citas/crearCita";
	}

	@PostMapping(value = "/cliente/citas/pedir")		//el cliente pide una cita
	public String citaCreation(final Principal principal, @Valid final Cita cita, final BindingResult result, @Param(value = "vehiculoId") final Integer vehiculoId, final Map<String, Object> model) {

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
			try {
				this.citaService.saveCita(cita);
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
			model.put("results", results);
			return "redirect:/cliente/citas/";

		}
	}

	@GetMapping(value = "/cliente/citas/vehiculo")		//el cliente escoje el vehiculo para crear una cita del vehiculo
	public String CitaVehiculoCreationForm(final Principal principal, final Cliente cliente, final Map<String, Object> model) {

		Integer clienteId = this.clienteService.findIdByUsername(principal.getName());
		Collection<Vehiculo> vehiculo = this.vehiculoService.findVehiculosByClienteId(clienteId);

		model.put("results", vehiculo);
		return "citas/citaVehiculo";
	}

	@GetMapping(value = "/cliente/citas/{citaId}/cancelar")		//el cliente quiere cancelar la cita
	public String cancelaCita(final Principal principal, @PathVariable(value = "citaId") final Integer citaId, final Map<String, Object> model) {
		Cita cita = this.citaService.findCitaById(citaId);
		if (cita.getCliente().getId() != this.clienteService.findIdByUsername(principal.getName())) {
			return "redirect:/cliente/citas";
		}
		model.put("cita", cita);
		return "/citas/citaCancelar";
	}

	@PostMapping(value = "/cliente/citas/{citaId}/cancelar")		//el cliente cancela la cita
	public String cancelaPostCita(final Principal principal, final Cita citaEditada, final BindingResult result, @PathVariable(value = "citaId") final int citaId, final Map<String, Object> model) {

		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "/citas/citaCancelar";
		} else {
			Cita citaCambiada = this.citaService.findCitaById(citaId);
			if (citaCambiada.getCliente().getId() != this.clienteService.findIdByUsername(principal.getName())) { //comprueba que no es otro cliente que quiere cancelar
				return "redirect:/cliente/citas";
			} else {
				citaCambiada.setEstadoCita(EstadoCita.cancelada);
				model.put("cita", citaCambiada);
				try {
					this.citaService.saveCita(citaCambiada);
				} catch (DataAccessException e) {
					e.printStackTrace();
				}
				return "redirect:/cliente/citas/";
			}
		}
	}

}
