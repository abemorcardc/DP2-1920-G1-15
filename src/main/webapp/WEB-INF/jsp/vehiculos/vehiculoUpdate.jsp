<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<petclinic:layout pageName="vehiculos">
	
	
 	<jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#fechaMatriculacion").datepicker({dateFormat:"yy-mm-dd"});
            });
        </script>
    </jsp:attribute>
	
	<jsp:body>
        <h2>
           Editar Vehiculo
        </h2>
        
            <form:form modelAttribute="vehiculo" class="form-horizontal">
	            <input type="hidden" name="id" value="${vehiculo.id}" />
	            <input type="hidden" name="activo" value="${vehiculo.activo}" />
	       
	          
	            <div class="form-group has-feedback">
	              
	              <petclinic:inputField label="Matricula" placeholder="9999XXX" name="matricula" required="true" pattern="^\\d{4}\\p{Upper}{3}$" title="Una matricula esta compuesta por 4 numeros y 3 letras"/>
			      <petclinic:inputField label="Fecha de matriculacion" name="fechaMatriculacion" required="true" placeholder="yyyy-mm-dd" pattern="^\\d{4}[-]\\d{2}[-]\\d{2}$" title="El formato de fecha es: yyyy-mm-dd"/>
			      <petclinic:inputField label="Modelo" name="modelo" required="true"/>
			      <petclinic:inputField label="Kilometraje" required="true" pattern="^\\d{1,6}$" name="kilometraje" title="Debe ser un numero positivo" />

	              <table class="error-title">
				<tr>
					<th>Tipo de vehiculo</th>
					<td><select name="tipoVehiculo" >
							<option value="turismo">Turismo</option>
							<option value="deportivo">Deportivo</option>
							<option value="todoterreno">TodoTerreno</option>
							<option value="camion">Camion</option>
							<option value="furgoneta">Furgoneta</option>
					</select></td>
				</tr>
			</table>
	              
	            </div>
            
            <button class="btn btn-default" type="submit">Update</button>
           
        </form:form>
    </jsp:body>
</petclinic:layout>