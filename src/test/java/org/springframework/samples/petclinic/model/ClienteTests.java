
package org.springframework.samples.petclinic.model;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * @author Michael Isvy Simple test to make sure that Bean Validation is working (useful
 *         when upgrading to a new version of Hibernate Validator/ Bean Validation)
 */
class ClienteTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	
	@Test
	void shouldValidateWhenFieldsAreCorrect() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Cliente cliente = new Cliente();

		cliente.setApellidos("Martín"); 			
		cliente.setDireccion("C/Tarfia");
		
		
		cliente.setDni("77844576X");
		cliente.setEmail("manolo@gmail.com");
		cliente.setNombre("manolo");
		cliente.setTelefono("608555102");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Cliente>> constraintViolations = validator.validate(cliente);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
		

	}
	
	@Test
	void shouldNotValidateWhenHisFieldsBlank() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Cliente cliente = new Cliente();

		cliente.setApellidos(""); 			
		cliente.setDireccion("");
		
		
		cliente.setDni("11122233P");
		cliente.setEmail("paco@gmail.com");
		cliente.setNombre("paco");
		cliente.setTelefono("678548394");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Cliente>> constraintViolations = validator.validate(cliente);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(2);
		ConstraintViolation<Cliente> violation1 = constraintViolations.iterator().next();

		Assertions.assertThat(violation1.getMessage()).isEqualTo("must not be blank");

	}

	

}
