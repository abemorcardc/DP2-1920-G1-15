
package org.springframework.samples.talleres.web;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.talleres.model.Cita;
import org.springframework.samples.talleres.model.Cliente;
import org.springframework.samples.talleres.model.Vehiculo;
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
public class VehiculoController {

	private final VehiculoService	vehiculoService;

	private final ClienteService	clienteService;


	private final CitaService citaService;
	
	private final MecanicoService mecanicoService;


	private static final String		VIEWS_CLIENTE_VEHICULO_CREATE_OR_UPDATE_FORM	= "vehiculos/crearVehiculo";

	private static final String		VIEWS_CLI_UPDATE_FORM							= "vehiculos/vehiculoUpdate";


	@InitBinder("vehiculo")
	public void initVehiculoBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new VehiculoValidator());
	}

	private boolean comprobarIdentidad(final Principal principal, final int vehiculoId) {
		Vehiculo vehiculo = this.vehiculoService.findVehiculoById(vehiculoId);
		if (this.clienteService.findIdByUsername(principal.getName()).equals(vehiculo.getCliente().getId())) {
			return true;
		} else {
			return false;
		}
	}

	private boolean tieneCitasAceptadasYPendientes(final int vehiculoId) {
		Vehiculo vehiculo = this.vehiculoService.findVehiculoById(vehiculoId);
		Integer res = this.citaService.countCitasAceptadasYPendientesByClienteIdAndVehiculoId(vehiculo.getCliente().getId(), vehiculoId);
		if (res != 0) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean comprobarIdentidadMecanico(final Principal principal, final int vehiculoId) {
		Collection<Cita> lista = this.citaService.findCitasByVehiculoId(vehiculoId);
		Integer idMecanico = this.mecanicoService.findMecIdByUsername(principal.getName());
		Boolean res = false;
		for(Cita c: lista) {
			if(c.getMecanico().getId().equals(idMecanico)) {
				res = true;
				break;
			}
		}
		return res;
		}

	@Autowired

	public VehiculoController(final VehiculoService vehiculoService, final ClienteService clienteService,
			final CitaService citaService, final MecanicoService mecanicoService) {

		this.vehiculoService = vehiculoService;
		this.clienteService = clienteService;
		this.citaService = citaService;
		this.mecanicoService = mecanicoService;
	}

	@GetMapping(value = "/cliente/vehiculos")		//el cliente lista sus vehiculos
	public String showVehiculoList(final Principal principal, final Map<String, Object> model) {
		Integer idCliente = this.clienteService.findIdByUsername(principal.getName());
		Collection<Vehiculo> results = this.vehiculoService.findVehiculosByClienteId(idCliente);
		model.put("results", results);
		return "vehiculos/vehiculoList";
	}

	@GetMapping("/cliente/vehiculos/{vehiculoId}")	//el cliente ve en detalle su vehiculo
	public String showVehiculoDetalle(@PathVariable("vehiculoId") final int vehiculoId, final Principal principal, final Map<String, Object> model) {

		if (!this.comprobarIdentidad(principal, vehiculoId)) {
			return "exception";
		}

		Vehiculo vehiculo = this.vehiculoService.findVehiculoById(vehiculoId);

		model.put("vehiculo", vehiculo);

		return "vehiculos/vehiculoEnDetalle";
	}
	
	@GetMapping("/mecanicos/vehiculos/{vehiculoId}")
	public String showVehiculoMecanicoDetalle(@PathVariable("vehiculoId") final int vehiculoId, final Principal principal,
			final Map<String, Object> model) {

		if (!this.comprobarIdentidadMecanico(principal, vehiculoId)) {
			return "exception";
		}

		Vehiculo vehiculo = this.vehiculoService.findVehiculoById(vehiculoId);

		model.put("vehiculo", vehiculo);

		return "vehiculos/vehiculoEnDetalle";
	}

	@GetMapping(value = "/cliente/vehiculos/crear")		//el cliente quiere crear un vehiculo
	public String vehiculoCreation(final Cliente cliente, final Map<String, Object> model) {
		Vehiculo vehiculo = new Vehiculo();
		model.put("vehiculo", vehiculo);
		return "vehiculos/crearVehiculo";
	}

	@PostMapping(value = "/cliente/vehiculos/crear")	//el cliente crea el vehiculo
	public String vehiculoCreation(final Principal principal, @Valid final Vehiculo vehiculo, final BindingResult result, final Map<String, Object> model) throws DataAccessException {

		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());

			return VehiculoController.VIEWS_CLIENTE_VEHICULO_CREATE_OR_UPDATE_FORM;

		} else {
			Integer idCliente = this.clienteService.findIdByUsername(principal.getName());
			Collection<Vehiculo> results = this.vehiculoService.findVehiculosByClienteId(idCliente);
			vehiculo.setCliente(this.clienteService.findClienteById(idCliente));
			vehiculo.setActivo(true);

			results.add(vehiculo);
			this.vehiculoService.saveVehiculo(vehiculo);

			model.put("results", results);
			return "redirect:/cliente/vehiculos";
		}
	}

	@GetMapping(value = "/cliente/vehiculos/{vehiculoId}/edit")		//el cliente quiere actualizar el vehiculo
	public String updateVehiculo(@PathVariable("vehiculoId") final int vehiculoId, final Principal principal, final ModelMap model) {

		if (!this.comprobarIdentidad(principal, vehiculoId)) {
			return "exception";
		}

		Vehiculo vehiculo = this.vehiculoService.findVehiculoById(vehiculoId);
		model.put("vehiculo", vehiculo);
		return VehiculoController.VIEWS_CLI_UPDATE_FORM;
	}

	@PostMapping(value = "/cliente/vehiculos/{vehiculoId}/edit")	//el cliente actualiza el vehiculo
	public String updateVehiculo(@Valid final Vehiculo vehiculoEditado, final BindingResult result, @PathVariable("vehiculoId") final int vehiculoId, final Principal principal, final ModelMap model) throws DataAccessException {

		if (!this.comprobarIdentidad(principal, vehiculoId)) {
			return "exception";
		}

		if (result.hasErrors()) {
			model.put("vehiculo", vehiculoEditado);
			return VehiculoController.VIEWS_CLI_UPDATE_FORM;

		} else {

			Vehiculo vehiculoAntiguo = this.vehiculoService.findVehiculoById(vehiculoId);

			BeanUtils.copyProperties(vehiculoEditado, vehiculoAntiguo, "id", "activo", "cliente");

			this.vehiculoService.saveVehiculo(vehiculoAntiguo);

			return "redirect:/cliente/vehiculos";
		}

	}

	@GetMapping(value = "/cliente/vehiculos/{vehiculoId}/disable")		//el cliente quiere dar de baja el vehiculo
	public String deshabilitarVehiculo(@PathVariable("vehiculoId") final int vehiculoId, final Principal principal, final ModelMap model) {

		if (!this.comprobarIdentidad(principal, vehiculoId)) {
			return "exception";
		}

		if (this.tieneCitasAceptadasYPendientes(vehiculoId)) {
			model.addAttribute("x", true);

		} else {
			model.addAttribute("x", false);
		}

		Vehiculo vehiculo = this.vehiculoService.findVehiculoById(vehiculoId);
		model.addAttribute(vehiculo);
		return "vehiculos/disableVehiculo";
	}

	@PostMapping(value = "/cliente/vehiculos/{vehiculoId}/disable")	//el cliente da de baja el vehiculo
	public String deshabilitarVehiculoForm(@PathVariable("vehiculoId") final int vehiculoId, final Principal principal, final ModelMap model) throws DataAccessException {

		if (!this.comprobarIdentidad(principal, vehiculoId)) {
			return "exception";
		}

		if (this.tieneCitasAceptadasYPendientes(vehiculoId)) {
			return "redirect:/cliente/vehiculos/{vehiculoId}/disable";

		} else {
			Vehiculo vehiculo = this.vehiculoService.findVehiculoById(vehiculoId);

			vehiculo.setActivo(false);

			this.vehiculoService.saveVehiculo(vehiculo);

			return "redirect:/cliente/vehiculos";
		}
	}

}
