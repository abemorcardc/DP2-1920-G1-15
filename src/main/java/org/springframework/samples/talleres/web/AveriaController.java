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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.talleres.model.Averia;
import org.springframework.samples.talleres.model.Cita;
import org.springframework.samples.talleres.model.Cliente;
import org.springframework.samples.talleres.model.EstadoCita;
import org.springframework.samples.talleres.model.Mecanico;
import org.springframework.samples.talleres.model.Vehiculo;
import org.springframework.samples.talleres.service.AveriaService;
import org.springframework.samples.talleres.service.CitaService;
import org.springframework.samples.talleres.service.MecanicoService;
import org.springframework.samples.talleres.service.VehiculoService;
import org.springframework.samples.talleres.service.exceptions.FechaEnFuturoException;
import org.springframework.stereotype.Controller;
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
	private final VehiculoService 	vehiculoService;
	private final CitaService 		citaService;

	
	private static final String		VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM	= "averias/crearAveria";

	@Autowired
	public AveriaController(final AveriaService averiaService, final MecanicoService mecanicoService,
			final VehiculoService vehiculoService, final CitaService citaService) {
		this.averiaService = averiaService;
		this.mecanicoService = mecanicoService;
		this.vehiculoService=vehiculoService;
		this.citaService=citaService;
	}

	@GetMapping("/mecanicos/{vehiculoId}")
	public String showMecAverListByVeh(final Principal principal, final Map<String, Object> model, @PathVariable("vehiculoId") final int vehiculoId) {
		Integer mecanicoId = this.mecanicoService.findMecIdByUsername(principal.getName());
		Collection<Averia> results = this.averiaService.findAveriaByVehiculoId(mecanicoId);
		model.put("results", results);
		return "averias/averiasDeVehiculoList";
	}
	
	@GetMapping(value = "/mecanicos/{vehiculoId}/new")
	public String initAveriaCreationForm(final Principal principal, final Mecanico mecanico, final Map<String, Object> model,@PathVariable("vehiculoId") final int vehiculoId) {
		Averia averia=	new Averia();
		//Integer mecanicoId = this.mecanicoService.findMecIdByUsername(principal.getName());
		Collection<Cita> citas =this.citaService.findCitasByVehiculoId(vehiculoId);
		model.put("citas", citas);
		model.put("averia", averia);
		return "averias/crearAveria";
	}

	@PostMapping(value = "/mecanicos/{vehiculoId}/new")
	public String AveriaCreation(final Principal principal, @Valid final Averia averia, final BindingResult result,
			@PathVariable("vehiculoId") final int vehiculoId,@Param(value = "citaId") final Integer citaId, final Map<String, Object> model) {
		
		if (citaId == null) {
			return "redirect:/mecanicos/{vehiculoId}/citas";
		} else {
			averia.setCita(this.citaService.findCitaById(citaId));
		}
		
		
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());

			return AveriaController.VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM;

		} else {
			averia.setVehiculo(this.vehiculoService.findVehiculoById(vehiculoId));
			
			
			Integer idMecanico = this.mecanicoService.findMecIdByUsername(principal.getName());
			Collection<Averia> results = this.averiaService.findAveriaByVehiculoId(vehiculoId);
			averia.setMecanico(this.mecanicoService.findMecanicoById(idMecanico));

			results.add(averia);
			// this.clienteService.saveVehiculo(vehiculo);
			try {
				this.averiaService.saveAveria(averia);
			} catch (DataAccessException e) {
				e.printStackTrace();
			
			model.put("results", results);
			return "redirect:/mecanicos/{vehiculoId}/";
			}
		}
		return "redirect:/mecanicos/{vehiculoId}/";
	}
	
	@GetMapping(value = "/mecanicos/{vehiculoId}/citas")
	public String CitaVehiculoCreationForm(final Principal principal, final Cliente cliente, @PathVariable("vehiculoId") final int vehiculoId, final Map<String, Object> model) {

		//Integer mecanicoId = this.mecanicoService.findMecIdByUsername(principal.getName());
		Collection<Cita> citas = this.citaService.findCitasByVehiculoId(vehiculoId);

		model.put("results", citas);
		return "averias/citasDelVehiculo";
	}


}
