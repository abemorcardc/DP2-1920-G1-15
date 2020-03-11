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


import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Vehiculos;
import org.springframework.samples.petclinic.service.VehiculoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class VehiculoController {

	private final VehiculoService vehiculoService;

	@Autowired
	public VehiculoController(final VehiculoService vehiculoService) {
		this.vehiculoService = vehiculoService;
	}
	
//	@ModelAttribute("tipoVehiculo")
//	public Collection<TipoVehiculo> populateTipoVehiculo() {
//		return this.vehiculoService.findTipoVehiculo();
//	}

	@GetMapping(value = {"/vehiculos"})
	public String showVehiculoList(@PathVariable("clienteId") int clienteId,final Map<String, Object> model) {
		Vehiculos vehiculos = new Vehiculos();
		vehiculos.getVehiculoList().addAll(this.vehiculoService.findVehiculosById(clienteId));
		model.put("vehiculos", vehiculos);
		return "vehiculos/vehiculoList";
	}

	@GetMapping(value = {"/vehiculos.xml"})
	public @ResponseBody Vehiculos showResourcesVehiculoList() {
		Vehiculos vehiculos = new Vehiculos();
		vehiculos.getVehiculoList().addAll(this.vehiculoService.findVehiculos());
		return vehiculos;
	}

}
