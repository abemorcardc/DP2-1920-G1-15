
package org.springframework.samples.talleres.web;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.talleres.configuration.SecurityConfiguration;
import org.springframework.samples.talleres.web.MecanicoController;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;

/**
 * Test class for {@link MecanicoController}
 *
 * @author Colin But
 */

@WebMvcTest(controllers = MecanicoController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class MecanicoControllerTests {
	
}
