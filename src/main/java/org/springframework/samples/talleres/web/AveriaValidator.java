
package org.springframework.samples.talleres.web;

import org.springframework.samples.talleres.model.Averia;
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
public class AveriaValidator implements Validator {

	@Override
	public void validate(Object obj, Errors errors) {
		Averia averia = (Averia) obj;
		
		String nombre = averia.getNombre();
		String descripcion = averia.getDescripcion();
		Double coste = averia.getCoste();
		Integer tiempo = averia.getTiempo();
		Integer piezasNecesarias = averia.getPiezasNecesarias();
		

		if (!StringUtils.hasLength(nombre)) {
			errors.rejectValue("nombre", "El nombre no puede estar vacío", "El nombre no puede estar vacío");
		}
		
		if (!StringUtils.hasLength(descripcion)) {
			errors.rejectValue("descripcion", "La descripcion no puede estar vacío", "La descripcion no puede estar vacío");
		}


		if (coste==null || coste<0.0) {
			errors.rejectValue("coste", "El coste debe ser positivo", "El coste debe ser positivo");
		}
		
		if (tiempo==null || tiempo<0) {
			errors.rejectValue("tiempo", "El tiempo debe ser positivo", "El tiempo debe ser positivo");
		}
		
		if (piezasNecesarias==null || piezasNecesarias<0) {
			errors.rejectValue("piezasNecesarias", "Las piezas necesarias debe ser positivas", "Las piezas necesarias debe ser positivas");
		}

	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Averia.class.isAssignableFrom(clazz);
	}

}
