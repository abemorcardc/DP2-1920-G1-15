package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.Vehiculo;
import org.springframework.samples.petclinic.service.ClienteService;
import org.springframework.samples.petclinic.service.VehiculoService;
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
public class VehiculoController {

	private final VehiculoService vehiculoService;
	
	private final ClienteService clienteService;
	
	private static final String VIEWS_CLIENTE_VEHICULO_CREATE_OR_UPDATE_FORM = "vehiculos/crearVehiculo";

	private static final String VIEWS_CLI_UPDATE_FORM = "vehiculos/vehiculoUpdate";


	@Autowired
	public VehiculoController(final VehiculoService vehiculoService, final ClienteService clienteService) {
		this.vehiculoService = vehiculoService;
		this.clienteService = clienteService;
	}
	
	@GetMapping(value = "/cliente/vehiculos")
	public String showCliVehiculoList(final Principal principal, final Map<String, Object> model) {
		Integer idCliente = this.clienteService.findIdByUsername(principal.getName());
		Collection<Vehiculo> results = this.vehiculoService.findVehiculosByClienteId(idCliente);
		model.put("results", results);
		return "vehiculos/vehiculoList";
	}

	@GetMapping("/cliente/vehiculos/{vehiculoId}")
	public ModelAndView showCliVehiculoDetalle(@PathVariable("vehiculoId") final int vehiculoId) {
		ModelAndView mav = new ModelAndView("vehiculos/vehiculoEnDetalle");
		mav.addObject(this.vehiculoService.findVehiculoById(vehiculoId));
		return mav;
	}
	
	@GetMapping(value = "/cliente/vehiculos/crear")
	public String initVehiculoCreationForm(final Principal principal, final Cliente cliente,
			final Map<String, Object> model) {
		Vehiculo vehiculo = new Vehiculo();
		model.put("vehiculo", vehiculo);
		return "vehiculos/crearVehiculo";
	}

	@PostMapping(value = "/cliente/vehiculos/crear")
	public String vehiculoCreation(final Principal principal, @Valid final Vehiculo vehiculo,
			final BindingResult result, final Map<String, Object> model) {

		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());

			return VehiculoController.VIEWS_CLIENTE_VEHICULO_CREATE_OR_UPDATE_FORM;

		} else {
			Integer idCliente = this.clienteService.findIdByUsername(principal.getName());
			Collection<Vehiculo> results = this.vehiculoService.findVehiculosByClienteId(idCliente);
			vehiculo.setCliente(this.clienteService.findClienteById(idCliente));
			vehiculo.setActivo(true);

			results.add(vehiculo);
			this.vehiculoService.saveVehiculo(vehiculo);;
			model.put("results", results);
			return "redirect:/cliente/vehiculos/";
		}
	}

	@GetMapping(value = "/cliente/vehiculos/{vehiculoId}/edit")
	public String updateVehiculo(@PathVariable("vehiculoId") final int vehiculoId, final Model model) {
		Vehiculo vehiculo = this.vehiculoService.findVehiculoById(vehiculoId);
		model.addAttribute(vehiculo);
		return VehiculoController.VIEWS_CLI_UPDATE_FORM;
	}

	@PostMapping(value = "/cliente/vehiculos/{vehiculoId}/edit")
	public String updateVehiculo(@Valid final Vehiculo vehiculoEditado,
			@PathVariable("vehiculoId") final int vehiculoId, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return VehiculoController.VIEWS_CLI_UPDATE_FORM;
		} else {
			Vehiculo vehiculoAntiguo = this.vehiculoService.findVehiculoById(vehiculoId);

			BeanUtils.copyProperties(vehiculoEditado, vehiculoAntiguo, "id", "activo", "cliente");

			this.vehiculoService.saveVehiculo(vehiculoAntiguo);

			return "redirect:/cliente/vehiculos/";
		}
	}
	
	@GetMapping(value = "/cliente/vehiculos/{vehiculoId}/disable")
	public String deshabilitarVehiculo(@PathVariable("vehiculoId") final int vehiculoId, final Model model) {
		Vehiculo vehiculo = this.vehiculoService.findVehiculoById(vehiculoId);
		model.addAttribute(vehiculo);
		return "vehiculos/disableVehiculo";
	}
	
	@PostMapping(value = "/cliente/vehiculos/{vehiculoId}/disable")
	public String deshabilitarVehiculo(@Valid final Vehiculo vehiculoEditado, @PathVariable("vehiculoId") final int vehiculoId, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "redirect:/cliente/vehiculos/";
		} else {
			Vehiculo vehiculo = this.vehiculoService.findVehiculoById(vehiculoId);

			vehiculo.setActivo(false);

			this.vehiculoService.saveVehiculo(vehiculo);

			return "redirect:/cliente/vehiculos/";
		}
	}


}
