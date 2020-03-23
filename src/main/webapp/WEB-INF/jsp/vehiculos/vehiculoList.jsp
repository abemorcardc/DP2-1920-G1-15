<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<petclinic:layout pageName="vehiculos">
	<h2>Vehículos</h2>

	<table id="vehiculosTable" class="table table-striped">
		<thead>
			<tr>
				<th>Detalles del vehículo</th>
				<th>Modelo</th>
				<th>Matrícula</th>
				<th>Fecha de matriculación</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${results}" var="vehiculo">
				<tr>
					<td><spring:url value="/cliente/vehiculos/{vehiculoId}" var="vehiculoUrl">
							<spring:param name="vehiculoId" value="${vehiculo.id}" />
						</spring:url> <a href="${fn:escapeXml(vehiculoUrl)}" class="btn btn-default">Ver vehículo</a></td>
					<td><c:out value="${vehiculo.modelo}" /></td>
					<td><c:out value="${vehiculo.matricula}" /></td>
					<td><c:out value="${vehiculo.fechaMatriculacion}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<a class="btn btn-default" href='<spring:url value="/cliente/vehiculos/crear" htmlEscape="true"/>'>Crear</a>
</petclinic:layout>
