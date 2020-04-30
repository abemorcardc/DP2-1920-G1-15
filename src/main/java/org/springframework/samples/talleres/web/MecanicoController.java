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

import org.springframework.stereotype.Controller;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class MecanicoController {
	/*
	 * private final MecanicoService mecanicoService;
	 * private CitaService citaService;
	 * private static final String VIEWS_MEC_UPDATE_FORM = "mecanicos/citaMecUpdate";
	 *
	 *
	 * @Autowired
	 * public MecanicoController(final MecanicoService mecanicoService, final CitaService citaService) {
	 * this.mecanicoService = mecanicoService;
	 * this.citaService = citaService;
	 * }
	 *
	 * @GetMapping("/mecanicos/citas")
	 * public String showMecCitaList(final Principal principal, final Map<String, Object> model) {
	 * Integer mecanicoId = this.mecanicoService.findIdByUsername(principal.getName());
	 * Collection<Cita> results = this.mecanicoService.findCitasByMecanicoId(mecanicoId);
	 * model.put("results", results);
	 * return "mecanicos/citaDeMecanicoList";
	 * }
	 *
	 * @GetMapping("/mecanicos/citas/{citaId}")
	 * public ModelAndView showMecCitaDetalle(@PathVariable("citaId") final int citaId) {
	 * ModelAndView mav = new ModelAndView("mecanicos/citaEnDetalle");
	 * mav.addObject(this.mecanicoService.findCitaById(citaId));
	 * return mav;
	 * }
	 *
	 * @GetMapping(value = "/mecanicos/citas/{citaId}/edit")
	 * public String initUpdateMecForm(@PathVariable("citaId") final int citaId, final Model model) {
	 * Cita cita = this.citaService.findCitaById(citaId);
	 * model.addAttribute(cita);
	 * return MecanicoController.VIEWS_MEC_UPDATE_FORM;
	 * }
	 *
	 * @PostMapping(value = "/mecanicos/citas/{citaId}/edit")
	 * public String processUpdateMecForm(@Valid final Cita citaEditada, @PathVariable("citaId") final int citaId, final BindingResult result, final ModelMap model) {
	 * if (citaEditada.getCliente() == null) {
	 * citaEditada.setCliente(this.citaService.findCitaById(citaId).getCliente());
	 * }
	 * if (citaEditada.getMecanico() == null) {
	 * citaEditada.setMecanico(this.citaService.findCitaById(citaId).getMecanico());
	 * }
	 * if (citaEditada.getTipo() == null) {
	 * citaEditada.setTipo(this.citaService.findCitaById(citaId).getTipo());
	 * }
	 * if (citaEditada.getVehiculo() == null) {
	 * citaEditada.setVehiculo(this.citaService.findCitaById(citaId).getVehiculo());
	 * }
	 * if (result.hasErrors()) {
	 * model.put("cita", citaEditada);
	 * return MecanicoController.VIEWS_MEC_UPDATE_FORM;
	 * } else {
	 * Cita citaAntigua = this.citaService.findCitaById(citaId);
	 *
	 * // BeanUtils.copyProperties(citaAntigua, citaEditada, "id", "fechaCita", "esAceptado", "esUrgente", "tipo", "mecanico", "cliente", "vehiculo"); //
	 * BeanUtils.copyProperties(citaEditada, citaAntigua, "id", "fechaCita", "esAceptado", "esUrgente", "tipo", "mecanico", "cliente", "vehiculo"); //coge los nuevos descripcion tiempo y coste
	 *
	 * // citaAntigua.setDescripcion(citaEditada.getDescripcion());
	 * // citaAntigua.setTiempo(citaEditada.getTiempo());
	 * // citaAntigua.setCoste(citaEditada.getCoste());
	 *
	 * this.citaService.saveCita(citaAntigua);
	 *
	 * return "redirect:/mecanicos/citas/";
	 * }
	 * }
	 *
	 * @GetMapping("/mecanicos/{vehiculoId}")
	 * public String showMecAverListByVeh(final Principal principal, final Map<String, Object> model, @PathVariable("vehiculoId") final int vehiculoId) {
	 * Integer mecanicoId = this.mecanicoService.findIdByUsername(principal.getName());
	 * Collection<Averia> results = this.mecanicoService.findAveriaByVehiculoId(mecanicoId);
	 * model.put("results", results);
	 * return "mecanicos/averiasDeVehiculoList";
	 * }
	 */
}
