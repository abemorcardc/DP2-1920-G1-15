
package org.springframework.samples.talleres.web;

import java.util.Date;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.samples.talleres.model.Vehiculo;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * <code>Validator</code> for <code>Pet</code> forms.
 * <p>
 * We're not using Bean Validation annotations here because it is easier to define such
 * validation rule in Java.
 * </p>
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 */
public class VehiculoValidator implements Validator {

	private static final String REQUIRED = "required";


	@Override
	public void validate(Object obj, Errors errors) {
		Vehiculo vehiculo = (Vehiculo) obj;
		String modelo = vehiculo.getModelo();
		String matricula = vehiculo.getMatricula();
		Integer kilometraje = vehiculo.getKilometraje();
		Date fecha=null;
		try {
			fecha = vehiculo.getFechaMatriculacion();
			if (fecha==null || fecha.after(new Date())) {
				errors.rejectValue("fechaMatriculacion", "La fecha debe pertenecer al pasado", "La fecha debe pertenecer al pasado");
			}

		} catch (ConversionFailedException ex) {
			errors.rejectValue("fechaMatriculacion", "Formato de fecha incorrecta", "Formato de fecha incorrecta");

		}


		if (!StringUtils.hasLength(modelo)) {
			errors.rejectValue("modelo", "El modelo no puede estar vacío", "El modelo no puede estar vacío");
		}

		if (!StringUtils.hasLength(matricula) || !matricula.matches("^\\d{4}\\w{3}$")) {
			errors.rejectValue("matricula", "La matrícula debe estar compuesta por 4 dígitos seguidos de tres letras", "La matrícula debe estar compuesta por 4 dígitos seguidos de tres letras");
		}



		if (kilometraje==null || kilometraje<0) {
			errors.rejectValue("kilometraje", "El kilometraje debe ser positivo", "El kilometraje debe ser positivo");
		}


	}

	/**
	 * This Validator validates *just* Pet instances
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return Vehiculo.class.isAssignableFrom(clazz);
	}

}
