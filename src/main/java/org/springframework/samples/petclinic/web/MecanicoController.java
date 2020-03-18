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
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.MecanicoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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
public class MecanicoController {

	private final MecanicoService	mecanicoService;
	private CitaService				citaService;
	private static final String		VIEWS_MEC_UPDATE_FORM	= "mecanicos/citaMecUpdate";


	@Autowired
	public MecanicoController(final MecanicoService mecanicoService) {
		this.mecanicoService = mecanicoService;
	}

	@GetMapping("/mecanicos/citas")
	public String showMecCitaList(final Principal principal, final Map<String, Object> model) {
		Integer mecanicoId = this.mecanicoService.findIdByUsername(principal.getName());
		Collection<Cita> results = this.mecanicoService.findCitasByMecanicoId(mecanicoId);
		model.put("results", results);
		return "mecanicos/citaDeMecanicoList";
	}

	@GetMapping("/mecanicos/citas/{citaId}")
	public ModelAndView showMecCitaDetalle(@PathVariable("citaId") final int citaId) {
		ModelAndView mav = new ModelAndView("mecanicos/citaEnDetalle");
		mav.addObject(this.mecanicoService.findCitaById(citaId));
		return mav;
	}

	@GetMapping(value = "/mecanicos/citas/{citaId}/edit")
	public String initUpdateMecForm(@PathVariable("citaId") final int citaId, final Model model) {
		Cita cita = this.mecanicoService.findCitaById(citaId);
		model.addAttribute(cita);
		return MecanicoController.VIEWS_MEC_UPDATE_FORM;
	}

	@PostMapping(value = "/mecanicos/citas/{citaId}/edit")
	public String processUpdateMecForm(@Valid final Cita citaEditada, @PathVariable("citaId") final int citaId, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("cita", citaEditada);
			return MecanicoController.VIEWS_MEC_UPDATE_FORM;
		} else {
			Cita citaAntigua = this.mecanicoService.findCitaById(citaId);

			//			BeanUtils.copyProperties(citaAntigua, citaEditada, "id", "fechaCita", "esAceptado", "esUrgente", "tipo", "mecanico", "cliente", "vehiculo"); //
			BeanUtils.copyProperties(citaEditada, citaAntigua, "id", "fechaCita", "esAceptado", "esUrgente", "tipo", "mecanico", "cliente", "vehiculo"); //coge los nuevos descripcion tiempo y coste

			citaAntigua.setDescripcion(citaEditada.getDescripcion());
			citaAntigua.setTiempo(citaEditada.getTiempo());
			citaAntigua.setCoste(citaEditada.getCoste());

			this.citaService.saveCita(citaAntigua);

			return "redirect:/mecanicos/citas";
		}
	}

	//	@GetMapping("/mecanicos/citasL")
	//	public ModelAndView showMecCitasList(final Principal principal, final Map<String, Object> model) {
	//		ModelAndView mav = new ModelAndView("mecanicos/citaDeMecanicoList");
	//
	//		Integer mecanicoId = this.mecanicoService.findIdByUsername(principal.getName());
	//		Collection<Cita> results = this.mecanicoService.findCitasByMecanicoId(mecanicoId);
	//		mav.addObject(results);
	//
	//		return mav;
	//	}
}
