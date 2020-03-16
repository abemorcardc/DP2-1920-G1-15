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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Averia;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.service.MecanicoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class MecanicoController {

	private final MecanicoService mecanicoService;
	//private CitaService				citaService;


	@Autowired
	public MecanicoController(final MecanicoService mecanicoService) {
		this.mecanicoService = mecanicoService;
	}

	/*
	 * @GetMapping(value = {
	 * "/mecanicos/{mecanicoId}/citas"
	 * })
	 * public String citasList(@PathVariable final int mecanicoId, final Map<String, Object> model) {
	 * Citas citas = new Citas();
	 * citas.getCitaList().addAll(this.citaService.findCitas());
	 * model.put("citas", this.mecanicoService.findAll()); //cambiado por si salia algo
	 * return "mecanicos/citaDeMecanicoList";
	 * }
	 *
	 *
	 *
	 *
	 */
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

	@GetMapping("/mecanicos/{vehiculoId}")
	public String showMecAverListByVeh(final Principal principal, final Map<String, Object> model, @PathVariable("vehiculoId") final int vehiculoId) {
		Integer mecanicoId = this.mecanicoService.findIdByUsername(principal.getName());
		Collection<Averia> results = this.mecanicoService.findAveriaByVehiculoId(mecanicoId);
		model.put("results", results);
		return "mecanicos/averiasDeVehiculoList";
	}
}
