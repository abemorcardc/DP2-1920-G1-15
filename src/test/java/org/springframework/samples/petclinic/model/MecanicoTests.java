
package org.springframework.samples.petclinic.model;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.talleres.model.Mecanico;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * @author Michael Isvy Simple test to make sure that Bean Validation is working (useful
 *         when upgrading to a new version of Hibernate Validator/ Bean Validation)
 */
class MecanicoTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void shouldNotValidateWhenHisFieldsBlank() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Mecanico mecanico = new Mecanico();

		mecanico.setExperiencia("");
		mecanico.setTitulaciones("");

		mecanico.setAveriasArregladas(2);
		mecanico.setApellidos("perez"); 			//heredadas
		mecanico.setDireccion("calle betis");
		mecanico.setDni("11122233P");
		mecanico.setEmail("paco@gmail.com");
		mecanico.setNombre("paco");
		mecanico.setTelefono("678548394");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Mecanico>> constraintViolations = validator.validate(mecanico);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(2);
		ConstraintViolation<Mecanico> violation1 = constraintViolations.iterator().next();

		Assertions.assertThat(violation1.getMessage()).isEqualTo("must not be blank");

	}

	@Test
	void shouldNotValidateWhenAveriaNull() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Mecanico mecanico = new Mecanico();

		mecanico.setAveriasArregladas(null);

		mecanico.setExperiencia("10 años");
		mecanico.setTitulaciones("eso");

		mecanico.setApellidos("perez");				//heredadas
		mecanico.setDireccion("calle betis");
		mecanico.setDni("11122233P");
		mecanico.setEmail("paco@gmail.com");
		mecanico.setNombre("paco");
		mecanico.setTelefono("678548394");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Mecanico>> constraintViolations = validator.validate(mecanico);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Mecanico> violation1 = constraintViolations.iterator().next();

		Assertions.assertThat(violation1.getMessage()).isEqualTo("must not be null");

	}

}
