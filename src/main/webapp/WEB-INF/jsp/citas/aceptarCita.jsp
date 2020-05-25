<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<petclinic:layout pageName="aceptarCita">

	<jsp:body>
        <h2>
			¿Quiere aceptar esta cita?
        </h2>
      		
      		 <form:form modelAttribute="cita" class="form-horizontal">
	            <input type="hidden" name="id" value="${cita.id}" />
<%-- 	            <input type="hidden" name="activo" value="${cita.activo}" /> --%>
<%-- 	            <input readonly name="matricula" value="${cita.matricula}" /> --%>
<%-- 	            <input type="hidden" name="fechaMatriculacion" value="${cita.fechaMatriculacion}" /> --%>
<%-- 	            <input type="hidden" name="modelo" value="${cita.modelo}" /> --%>
<%-- 	            <input type="hidden" name="tipoVehiculo" value="${cita.tipoVehiculo}" /> --%>
<%-- 	            <input type="hidden" name="kilometraje" value="${cita.kilometraje}" /> --%>
            		
<%--             <c:if test="${x == 'false'}"> --%>
            		<button class="btn btn-default" type="submit">Aceptar cita</button>
<%-- 			</c:if> --%>
        	</form:form>
        
            <a class="btn btn-default"
			href='<spring:url value="/mecanicos/citas" htmlEscape="true"/>'>Volver</a>
           

    </jsp:body>
</petclinic:layout>