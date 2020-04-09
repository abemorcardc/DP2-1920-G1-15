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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.talleres.model.Averia;
import org.springframework.samples.talleres.service.AveriaService;
import org.springframework.samples.talleres.service.MecanicoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class AveriaController {

	private final AveriaService averiaService;


	@Autowired
	public AveriaController(final AveriaService averiaService, final MecanicoService mecanicoService) {
		this.averiaService = averiaService;
	}

	@GetMapping("/mecanicos/{vehiculoId}")
	public String showMecAverListByVeh(final Principal principal, final Map<String, Object> model, @PathVariable("vehiculoId") final int vehiculoId) {
		Collection<Averia> results = this.averiaService.findAveriaByVehiculoId(vehiculoId);
		model.put("results", results);
		return "averias/MecAveriasDeVehiculoList";
	}

	@GetMapping("/cliente/vehiculos/{vehiculoId}/averias")
	public String showCliAverListByVeh(final Principal principal, final Map<String, Object> model, @PathVariable("vehiculoId") final int vehiculoId) {
		Collection<Averia> results = this.averiaService.findAveriaByVehiculoId(vehiculoId);
		model.put("results", results);
		return "averias/CliAveriasDeVehiculoList";
	}

}
