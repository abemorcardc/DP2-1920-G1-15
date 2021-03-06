
package org.springframework.samples.talleres.web;

import java.time.LocalDateTime;

import org.springframework.samples.talleres.model.Cita;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CitaValidator implements Validator {

	@Override
	public void validate(final Object obj, final Errors errors) {
		Cita cita = (Cita) obj;
		String descripcion = cita.getDescripcion();
		Integer tiempo = cita.getTiempo();
		Double coste = cita.getCoste();
		LocalDateTime fechaYHora = null;
		fechaYHora = cita.getFechaCita();
		if (fechaYHora == null) {
			errors.rejectValue("fechaCita", "Introduce una fecha futura", "Introduce una fecha futura");
		}
		if (fechaYHora.isBefore(LocalDateTime.now())) {
			errors.rejectValue("fechaCita", "La fecha debe debe ser futura", "La fecha debe debe ser futura");
		}

		if (!StringUtils.hasLength(descripcion)) {
			errors.rejectValue("descripcion", "La descripcion no puede estar vacía", "La descripcion no puede estar vacía");
		}

		if (tiempo == null || tiempo < 0) {
			errors.rejectValue("tiempo", "El tiempo debe ser positivo", "El tiempo debe ser positivo");
		}

		if (coste == null || coste < 0) {
			errors.rejectValue("coste", "El coste debe ser positivo", "El coste debe ser positivo");
		}

	}

	/**
	 * This Validator validates *just* Cita instances
	 */
	@Override
	public boolean supports(final Class<?> clazz) {
		return Cita.class.isAssignableFrom(clazz);
	}

}
