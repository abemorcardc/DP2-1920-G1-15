<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<petclinic:layout pageName="cita">

	<h2>Detalles de la averia</h2>


	<table class="table table-striped">
		<tr>
			<th>Nombre</th>
			<td><c:out value="${averia.nombre}" /></td>
		</tr>
		
		<tr>
			<th>Descripcion</th>
			<td><c:out value="${averia.descripcion}" /></td>
		</tr>

		<tr>
			<th>Complejidad</th>
			<td><c:if test="${averia.complejidad == 'BAJA'}">
					<c:out value="Baja" />
				</c:if> <c:if test="${averia.complejidad == 'MEDIA'}">
					<c:out value="Media" />
				</c:if> <c:if test="${averia.complejidad == 'ALTA'}">
					<c:out value="Alta" />
				</c:if></td>
		</tr>
		<tr>
			<th>Tiempo</th>
			<td><c:out value="${averia.tiempo}" /></td>
		</tr>
		<tr>
			<th>Coste</th>
			<td><c:out value="${averia.coste}" /></td>
		</tr>
		<tr>
			<th>Número de piezas necesarias</th>
			<td><c:out value="${averia.piezasNecesarias}" /></td>
		</tr>
		<tr>
			<th>¿Está reparada?</th>
			<td><c:if test="${averia.estaReparada == 'TRUE'}">
					<c:out value="Si" />
				</c:if> <c:if test="${averia.estaReparada == 'FALSE'}">
					<c:out value="No" />
				</c:if></td>
		</tr>
		<sec:authorize access="hasAnyAuthority('mecanico')">
			<tr>
				<th>Vehiculo</th>
				<td><c:out value="${averia.vehiculo.modelo}" /></td>
			</tr>
		</sec:authorize>
		<tr>
				<th>Cita</th>
				<td><c:out value="${averia.cita.descripcion}" /></td>
			</tr>
	</table>
	
	<spring:url value="/mecanicos/vehiculos/{vehiculoId}/averia" var="vehiculoId">
			<spring:param name="vehiculoId" value="${averia.vehiculo.id}" />
			</spring:url> <a href="${fn:escapeXml(vehiculoId)}" class="btn btn-default">Volver</a>
</petclinic:layout>
