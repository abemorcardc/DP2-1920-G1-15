
package org.springframework.samples.talleres.model;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.talleres.model.Persona;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * @author Michael Isvy Simple test to make sure that Bean Validation is working (useful
 *         when upgrading to a new version of Hibernate Validator/ Bean Validation)
 */
class PersonaTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void shouldNotValidateWhenEveryFieldBlank() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Persona persona = new Persona();
		persona.setNombre("");
		persona.setApellidos("");
		persona.setDireccion("");
		persona.setDni("");
		persona.setTelefono("");
		persona.setEmail("");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Persona>> constraintViolations = validator.validate(persona);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(6);
		ConstraintViolation<Persona> violation = constraintViolations.iterator().next();

		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be blank");
	}

}
