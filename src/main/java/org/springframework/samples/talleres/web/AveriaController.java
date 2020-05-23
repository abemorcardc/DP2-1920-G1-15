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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.talleres.model.Averia;
import org.springframework.samples.talleres.model.Cita;
import org.springframework.samples.talleres.model.Cliente;
import org.springframework.samples.talleres.model.Mecanico;
import org.springframework.samples.talleres.model.Vehiculo;
import org.springframework.samples.talleres.service.AveriaService;
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

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class AveriaController {

	private final AveriaService averiaService;
	private final ClienteService clienteService;
	private final VehiculoService vehiculoService;
	private final MecanicoService mecanicoService;
	private final CitaService citaService;

	private static final String VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM = "averias/crearAveria";

	@InitBinder("averia")
	public void initAveriaBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new AveriaValidator());
	}

	private boolean comprobarIdentidadMecanico(final Principal principal, final int averiaId) {
		Averia averia = this.averiaService.findAveriaById(averiaId);
		if (this.mecanicoService.findMecIdByUsername(principal.getName()).equals(averia.getMecanico().getId())) {
			return true;
		} else {
			return false;
		}
	}

	private boolean comprobarIdentidadCliente(final Principal principal, final int vehiculoId) {
		Vehiculo vehiculo = this.vehiculoService.findVehiculoById(vehiculoId);
		if (this.clienteService.findIdByUsername(principal.getName()).equals(vehiculo.getCliente().getId())) {
			return true;
		} else {
			return false;
		}
	}

	private boolean comprobarVehiculosMecanico(final Principal principal, final int vehiculoId) {
		Integer mecanicoId = this.mecanicoService.findMecIdByUsername(principal.getName());

		List<Integer> idVehiculosMecanico = new ArrayList<>();
		Collection<Cita> cita = this.citaService.findCitasByMecanicoId(mecanicoId);
		for (Cita c : cita) {
			Integer vehiculoId2 = c.getVehiculo().getId();
			if (!idVehiculosMecanico.contains(vehiculoId2)) {
				idVehiculosMecanico.add(vehiculoId2);
			}

		}

		if (idVehiculosMecanico.contains(vehiculoId)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean comprobarIdentidadMecanico(final Principal principal, final Averia averia) {
		Integer mecanicoId = this.mecanicoService.findMecIdByUsername(principal.getName());

		if (mecanicoId == averia.getMecanico().getId()) {
			return true;
		} else {
			return false;
		}
	}

	@Autowired
	public AveriaController(final AveriaService averiaService, final VehiculoService vehiculoService,
			final ClienteService clienteService, final MecanicoService mecanicoService, final CitaService citaService) {
		this.averiaService = averiaService;
		this.vehiculoService = vehiculoService;
		this.clienteService = clienteService;
		this.mecanicoService = mecanicoService;
		this.citaService = citaService;
	}

	@GetMapping("/mecanicos/vehiculos/{vehiculoId}/averia")
	public String showMecAverListByVeh(final Principal principal, final Map<String, Object> model,
			@PathVariable("vehiculoId") final int vehiculoId) {
		Collection<Averia> results = this.averiaService.findAveriasByVehiculoId(vehiculoId);
		model.put("results", results);

		if (!this.comprobarVehiculosMecanico(principal, vehiculoId)) {
			return "exception";
		}

		return "averias/MecAveriasDeVehiculoList";
	}

	@GetMapping("/cliente/vehiculos/{vehiculoId}/averias")
	public String showCliAverListByVeh(final Principal principal, final Map<String, Object> model,
			@PathVariable("vehiculoId") final int vehiculoId) {

		if (!this.comprobarIdentidadCliente(principal, vehiculoId)) {
			return "exception";
		}

		Collection<Averia> results = this.averiaService.findAveriasByVehiculoId(vehiculoId);
		model.put("results", results);
		return "averias/CliAveriasDeVehiculoList";
	}

	@GetMapping(value = "/mecanicos/{vehiculoId}/new")
	public String initAveriaCreationForm(final Principal principal, final Mecanico mecanico,
			final Map<String, Object> model, @PathVariable("vehiculoId") final int vehiculoId) {
		Averia averia = new Averia();

		// Integer mecanicoId =
		// this.mecanicoService.findMecIdByUsername(principal.getName());
		Collection<Cita> citas = this.citaService.findCitasByVehiculoId(vehiculoId);
		model.put("citas", citas);
		model.put("averia", averia);

		if (!this.comprobarVehiculosMecanico(principal, vehiculoId)) {
			return "exception";
		}
		return "averias/crearAveria";
	}

	@PostMapping(value = "/mecanicos/{vehiculoId}/new")
	public String AveriaCreation(final Principal principal, @Valid final Averia averia, final BindingResult result,
			@PathVariable("vehiculoId") final int vehiculoId, @Param(value = "citaId") final Integer citaId,
			final Map<String, Object> model) {

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
			Collection<Averia> results = this.averiaService.findAveriasByVehiculoId(vehiculoId);
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
		return "redirect:/mecanicos/vehiculos/{vehiculoId}/averia";
	}

	@GetMapping(value = "/mecanicos/{vehiculoId}/citas")
	public String CitaVehiculoCreationForm(final Principal principal, final Cliente cliente,
			@PathVariable("vehiculoId") final int vehiculoId, final Map<String, Object> model) {

		// Integer mecanicoId =
		// this.mecanicoService.findMecIdByUsername(principal.getName());
		if (this.comprobarVehiculosMecanico(principal, vehiculoId)) {
			Collection<Cita> citas = this.citaService.findCitasByVehiculoId(vehiculoId);

			model.put("results", citas);
			return "averias/citasDelVehiculo";
		} else {
			return "exception";
		}

	}

	@GetMapping("/mecanicos/averia/{averiaId}")
	public String showMecAverByVeh(final Principal principal, final Map<String, Object> model,
			@PathVariable("averiaId") final int averiaId) {
		Averia averia = this.averiaService.findAveriaById(averiaId);
		if (!this.comprobarIdentidadMecanico(principal, averia)) {
			return "exception";
		}
		model.put("averia", averia);
		return "averias/MecanicoAveriaShow";

	}

	@GetMapping("/cliente/averia/{averiaId}")
	public String showCliAverByVeh(final Principal principal, final Map<String, Object> model,
			@PathVariable("averiaId") final int averiaId) {
		Averia averia = this.averiaService.findAveriaById(averiaId);
		if (!this.comprobarIdentidadCliente(principal, averiaId)) {
			return "exception";
		}
		model.put("averia", averia);
		return "averias/ClienteAveriaShow";

	}

	// Abel y Javi --------------------------------

	@GetMapping(value = "/mecanicos/vehiculos/{vehiculoId}/averia/{averiaId}/edit")
	public String updateAveria(@PathVariable("vehiculoId") final int vehiculoId,
			@PathVariable("averiaId") final int averiaId, final Principal principal, final ModelMap model) {

		if (!this.comprobarIdentidadMecanico(principal, averiaId)) {
			return "exception";
		}

		Averia averia = this.averiaService.findAveriaById(averiaId);
		model.addAttribute(averia);

		return "averias/averiaUpdate";
	}

	@PostMapping(value = "/mecanicos/vehiculos/{vehiculoId}/averia/{averiaId}/edit")
	public String updateVehiculo(@Valid final Averia averiaEditada, final BindingResult result,
			@PathVariable("vehiculoId") final int vehiculoId, @PathVariable("averiaId") final int averiaId,
			final Principal principal, final ModelMap model) throws DataAccessException {

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
