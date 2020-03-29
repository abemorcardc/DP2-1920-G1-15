
package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.talleres.model.Cita;
import org.springframework.samples.talleres.model.Cliente;
import org.springframework.samples.talleres.model.EstadoCita;
import org.springframework.samples.talleres.model.Mecanico;
import org.springframework.samples.talleres.model.TipoCita;
import org.springframework.samples.talleres.model.Vehiculo;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * @author Michael Isvy Simple test to make sure that Bean Validation is working (useful
 *         when upgrading to a new version of Hibernate Validator/ Bean Validation)
 */
class CitaTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	@Test
	void shouldValidateWhenFieldsAreCorrect() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Cita cita = new Cita();

		cita.setCoste(120.0);; 			
		cita.setDescripcion("Problemas con el motor");
		cita.setEstadoCita(EstadoCita.pendiente);
		cita.setEsUrgente(true);
		cita.setFechaCita(LocalDateTime.of(2021,03,14, 12,00));
		cita.setTiempo(40);
		cita.setTipo(TipoCita.reparacion);
		
		cita.setCliente(new Cliente());
		cita.setVehiculo(new Vehiculo());
		cita.setMecanico(new Mecanico());
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Cita>> constraintViolations = validator.validate(cita);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	
		

	}
	
	@Test
	void shouldNotValidateWhenHisFieldsBlankOrNull() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Cita cita = new Cita();

		cita.setCoste(null);; 			
		cita.setDescripcion("");
		cita.setEstadoCita(EstadoCita.pendiente);
		cita.setEsUrgente(true);
		cita.setFechaCita(null);
		cita.setTiempo(40);
		cita.setTipo(TipoCita.reparacion);
		
		cita.setCliente(new Cliente());
		cita.setVehiculo(new Vehiculo());
		cita.setMecanico(new Mecanico());
		
		
		
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Cita>> constraintViolations = validator.validate(cita);
		
		Assertions.assertThat(constraintViolations.size()).isEqualTo(3);
				
	}
	
	@Test
	void shouldNotValidateWhenHisFechaCitaIsPast() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Cita cita = new Cita();

		
		cita.setCoste(null); 			
		cita.setDescripcion("");
		cita.setEstadoCita(EstadoCita.pendiente);
		cita.setEsUrgente(true);
		cita.setFechaCita(LocalDateTime.of(2011,03,14, 12,00));
		cita.setTiempo(40);
		cita.setTipo(TipoCita.reparacion);
		
		cita.setCliente(new Cliente());
		cita.setVehiculo(new Vehiculo());
		cita.setMecanico(new Mecanico());
		
		
		
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Cita>> constraintViolations = validator.validate(cita);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(3);

	}

}
