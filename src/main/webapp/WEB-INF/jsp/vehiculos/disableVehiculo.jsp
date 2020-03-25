<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<petclinic:layout pageName="vehiculos">

	<jsp:body>
        <h2>
			¿Esta seguro de que quiere dar de baja su vehiculo?
        </h2>
      		
      		 <form:form modelAttribute="vehiculo" class="form-horizontal">
	            <input type="hidden" name="id" value="${vehiculo.id}" />
	            <input type="hidden" name="activo" value="${vehiculo.activo}" />
	            <input readonly name="matricula" value="${vehiculo.matricula}" />
	            <input type="hidden" name="fechaMatriculacion" value="${vehiculo.fechaMatriculacion}" />
	            <input type="hidden" name="modelo" value="${vehiculo.modelo}" />
	            <input type="hidden" name="tipoVehiculo" value="${vehiculo.tipoVehiculo}" />
	            <input type="hidden" name="kilometraje" value="${vehiculo.kilometraje}" />
        
        	<c:if test="${x == 'true'}">
            		<h3>No puedes dar de baja un vehículo con citas pendientes</h3></c:if>
            		
            <c:if test="${x == 'false'}">
            		<button class="btn btn-default" type="submit">Dar de baja</button></c:if>
        	</form:form>
        
        
        
        	
        
            <a class="btn btn-default"
			href='<spring:url value="/cliente/vehiculos" htmlEscape="true"/>'>Volver</a>
           

    </jsp:body>
</petclinic:layout>