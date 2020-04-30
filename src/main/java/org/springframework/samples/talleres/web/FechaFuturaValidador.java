
package org.springframework.samples.talleres.web;

import java.time.LocalDateTime;

import org.springframework.samples.talleres.model.Cita;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class FechaFuturaValidador implements Validator {

	private static final String PAST_DATE = "pastDate";


	@Override
	public boolean supports(final Class<?> clazz) {
		return Cita.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		Cita cita = (Cita) target;

		if (cita.getFechaCita().isBefore(LocalDateTime.now())) {
			errors.rejectValue("fechaCita", FechaFuturaValidador.PAST_DATE, FechaFuturaValidador.PAST_DATE);
		}

	}
}
