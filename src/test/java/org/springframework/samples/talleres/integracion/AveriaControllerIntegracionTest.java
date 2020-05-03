
package org.springframework.samples.talleres.integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.talleres.model.Averia;
import org.springframework.samples.talleres.model.Cliente;
import org.springframework.samples.talleres.model.Complejidad;
import org.springframework.samples.talleres.model.Mecanico;
import org.springframework.samples.talleres.web.AveriaController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;



/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AveriaControllerIntegracionTest {

	@Autowired
	private AveriaController averiaController;
	
	//Historia 8
	@WithMockUser(value = "paco",roles="mecanico")
	@Test
	void testMecAveriasShow() throws Exception {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		Map<String,Object> model= new HashMap<String, Object>();
		
		
		String view=this.averiaController.showMecAverByVeh(principal, model, 1);
		
		assertEquals(view,"averias/MecanicoAveriaShow");
		
	}
	
	
	//Historia 10
	@WithMockUser(value = "paco",roles="mecanico")
	@Test
	void testMecAveriasInitCreation() throws Exception {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		Map<String,Object> model= new HashMap<String, Object>();
		
		Mecanico mecanico =new Mecanico();
		
		mecanico.setApellidos("Naranjo");
		mecanico.setAveriasArregladas(12);
		mecanico.setDireccion("C/Esperanza");
		mecanico.setDni("21154416G");
		mecanico.setEmail("PacoTalleres@gmail.com");
		mecanico.setExperiencia("ninguna");
		mecanico.setNombre("Paco");
		mecanico.setTelefono("666973647");
		mecanico.setTitulaciones("Fp de mecanico");
		
		String view=this.averiaController.initAveriaCreationForm(principal, mecanico, model, 1);
		
		assertEquals(view,"averias/crearAveria");
		
	}
	
	@WithMockUser(value = "paco",roles="mecanico")
	@Test
	void testMecAveriasCreation() throws Exception {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		Map<String,Object> model= new HashMap<String, Object>();
		
		Averia averia=new Averia();
		
		averia.setComplejidad(Complejidad.BAJA);
		averia.setCoste(50.0);
		averia.setDescripcion("cambio de bujia");
		averia.setEstaReparada(false);
		averia.setNombre("coche de manolo");
		averia.setPiezasNecesarias(1);
		averia.setTiempo(100);
		
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		
		
		String view=this.averiaController.AveriaCreation(principal, averia, result, 1, 1, model);
		
		assertEquals(view,"redirect:/mecanicos/{vehiculoId}/");
		
	}
	
	@WithMockUser(value = "paco",roles="mecanico")
	@Test
	void testMecAveriasVehiculoCreation() throws Exception {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		Map<String,Object> model= new HashMap<String, Object>();
		
		Cliente cliente=new Cliente();
		
		cliente.setApellidos("Martín");
		cliente.setDireccion("C/Tarfia");
		cliente.setDni("77844576X");
		cliente.setEmail("Manolo72@gmail.com");
		cliente.setId(1);
		cliente.setNombre("Manolo");
		cliente.setTelefono("608555102");
				
		
		String view=this.averiaController.CitaVehiculoCreationForm(principal, cliente, 1, model);
		
		assertEquals(view,"averias/citasDelVehiculo");
		
	}
	
	
		
}
