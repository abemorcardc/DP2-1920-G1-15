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

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Averia;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.MecanicoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
	private static final String		VIEWS_MEC_UPDATE_FORM	= "citas/citaMecUpdate";


	@Autowired
	public CitaController(final MecanicoService mecanicoService, final CitaService citaService) {
		this.mecanicoService = mecanicoService;
		this.citaService = citaService;
	}

	@GetMapping("/mecanicos/citas")
	public String showMecCitaList(final Principal principal, final Map<String, Object> model) {
		Integer mecanicoId = this.mecanicoService.findIdByUsername(principal.getName());
		Collection<Cita> results = this.mecanicoService.findCitasByMecanicoId(mecanicoId);
		model.put("results", results);
		return "citas/citaDeMecanicoList";
	}

	@GetMapping("/mecanicos/citas/{citaId}")
	public ModelAndView showMecCitaDetalle(@PathVariable("citaId") final int citaId) {
		ModelAndView mav = new ModelAndView("citas/citaEnDetalle");
		mav.addObject(this.mecanicoService.findCitaById(citaId));
		return mav;
	}

	@GetMapping(value = "/mecanicos/citas/{citaId}/edit")
	public String initUpdateMecForm(@PathVariable("citaId") final int citaId, final Model model) {
		Cita cita = this.citaService.findCitaById(citaId);
		model.addAttribute(cita);
		return CitaController.VIEWS_MEC_UPDATE_FORM;
	}

	@PostMapping(value = "/mecanicos/citas/{citaId}/edit")
	public String processUpdateMecForm(@Valid final Cita citaEditada, @PathVariable("citaId") final int citaId, final BindingResult result, final Map<String, Object> model) {
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
		if (result.hasErrors()) {
			model.put("cita", citaEditada);
			return CitaController.VIEWS_MEC_UPDATE_FORM;
		} else {
			Cita citaAntigua = this.citaService.findCitaById(citaId);

			BeanUtils.copyProperties(citaEditada, citaAntigua, "id", "esAceptado", "esUrgente", "tipo", "mecanico", "cliente", "vehiculo"); //coge los nuevos descripcion tiempo y coste

			this.citaService.saveCita(citaAntigua);

			return "redirect:/mecanicos/citas/";
		}
	}

	@GetMapping("/mecanicos/{vehiculoId}")
	public String showMecAverListByVeh(final Principal principal, final Map<String, Object> model, @PathVariable("vehiculoId") final int vehiculoId) {
		Integer mecanicoId = this.mecanicoService.findIdByUsername(principal.getName());
		Collection<Averia> results = this.mecanicoService.findAveriaByVehiculoId(mecanicoId);
		model.put("results", results);
		return "averias/averiasDeVehiculoList";
	}

}
