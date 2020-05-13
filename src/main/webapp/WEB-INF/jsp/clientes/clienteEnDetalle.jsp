<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<petclinic:layout pageName="cliente">

	<h2>Detalles del cliente </h2>


	<table class="table table-striped">
		<tr>
            <th>Nombre completo</th>
            <td><b><c:out value="${cliente.nombre} ${cliente.apellidos}"/></b></td>
        </tr>
        <tr>
            <th>Dirección</th>
            <td><c:out value="${cliente.direccion}"/></td>
        </tr>
        <tr>
            <th>DNI</th>
            <td><c:out value="${cliente.dni}"/></td>
        </tr>
        <tr>
            <th>Teléfono</th>
            <td><c:out value="${cliente.telefono}"/></td>
        </tr>
        <tr>
            <th>Email</th>
            <td><c:out value="${cliente.email}"/></td>
        </tr>
		
	</table>
	
	<h2>Vehículos del cliente </h2>
	
	<table class="table table-striped">
		<c:forEach items="${vehiculos}" var="vehiculo" >
			<tr>
            	<td><c:out value="${vehiculo.modelo}: ${vehiculo.matricula}"/></td>
        	</tr>
		</c:forEach>
	</table>
	
	<sec:authorize access="hasAnyAuthority('mecanico')">
		<a class="btn btn-default" href='<spring:url value="/mecanicos/citas" htmlEscape="true"/>'>Volver</a>
	</sec:authorize>


</petclinic:layout>
