<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<petclinic:layout pageName="vehiculos">
	
	<jsp:body>
        <h2>
           Editar Vehiculo
        </h2>
        
            <form:form modelAttribute="vehiculo" class="form-horizontal">
	            <input type="hidden" name="id" value="${vehiculo.id}" />
	            <input type="hidden" name="activo" value="${vehiculo.activo}" />
	       
	          
	            <div class="form-group has-feedback">
	              
	              <petclinic:inputField label="Matricula" placeholder="9999XXX" name="matricula" pattern="^\\d{4}\\w{3}$" title="Una matrícula está compuesta por 4 números y 3 letras"/>
			      <petclinic:inputField label="Fecha de matriculacion" name="fechaMatriculacion" />
			      <petclinic:inputField label="Modelo" name="modelo" />
			      <petclinic:inputField label="Kilometraje" name="kilometraje" />
	              
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