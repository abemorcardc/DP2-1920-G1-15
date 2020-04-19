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

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.talleres.model.Averia;
import org.springframework.samples.talleres.service.AveriaService;
import org.springframework.samples.talleres.service.MecanicoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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
public class AveriaController {

	private final AveriaService		averiaService;
	private final MecanicoService	mecanicoService;
	
	private boolean comprobarIdentidadMecanico(final Principal principal, final int averiaId) {
		Averia averia = this.averiaService.findAveriaById(averiaId);
		if (this.mecanicoService.findMecIdByUsername(principal.getName()).equals(averia.getMecanico().getId())) {
			return true;
		} else {
			return false;
		}
	}

	@Autowired
	public AveriaController(final AveriaService averiaService, final MecanicoService mecanicoService) {
		this.averiaService = averiaService;
		this.mecanicoService = mecanicoService;
	}

	@GetMapping("/mecanicos/vehiculos/{vehiculoId}/averia")
	public String showMecAverListByVeh(final Principal principal, final Map<String, Object> model, @PathVariable("vehiculoId") final int vehiculoId) {
		Integer mecanicoId = this.mecanicoService.findMecIdByUsername(principal.getName());
		Collection<Averia> results = this.averiaService.findAveriaByVehiculoId(mecanicoId);
		model.put("results", results);
		return "averias/averiasDeVehiculoList";
	}
	
	// Abel y Javi --------------------------------
	
	@GetMapping(value = "/mecanicos/vehiculos/{vehiculoId}/averia/{averiaId}/edit")
	public String updateAveria(@PathVariable("vehiculoId") final int vehiculoId, @PathVariable("averiaId") final int averiaId, final Principal principal,
			final Model model) {
		
		if (!this.comprobarIdentidadMecanico(principal, averiaId)) {
			return "exception";
		}

		Averia averia = this.averiaService.findAveriaById(averiaId);
		model.addAttribute(averia);
		
		return "averias/averiaUpdate";
	}

	@PostMapping(value = "/mecanicos/vehiculos/{vehiculoId}/averia/{averiaId}/edit")
	public String updateVehiculo(final Averia averiaEditada,@PathVariable("vehiculoId") final int vehiculoId, @PathVariable("averiaId") final int averiaId, final Principal principal, 
			final BindingResult result, final ModelMap model) throws DataAccessException {
		
		if (!this.comprobarIdentidadMecanico(principal, averiaId)) {
			return "exception";
		}
		
		if (result.hasErrors()) {
			return "averias/averiaUpdate";
		} else {
			
			Averia averiaAntigua = this.averiaService.findAveriaById(averiaId);

			BeanUtils.copyProperties(averiaEditada, averiaAntigua, "id", "vehiculo", "cita", "mecanico");

			this.averiaService.saveAveria(averiaAntigua);

			return "redirect:/mecanicos/vehiculos/{vehiculoId}/averia";
		}

	}

}
