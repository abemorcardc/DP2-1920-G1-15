<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="cita">

	<h2>Detalles de la cita</h2>


	<table class="table table-striped">
		<tr>
			<th>Fecha de la Cita</th>
			<td><c:out value="${cita.fechaCita}"  /> </td>
		</tr>

		<tr>
			<th>Descripción</th>
			<td><c:out value="${cita.descripcion}" /></td>
		</tr>
		<tr>
			<th>Tipo de la cita</th>
			<td><c:if test="${cita.tipo == 'revision'}">
					<c:out value="Revisión" />
				</c:if> <c:if test="${cita.tipo == 'reparacion'}">
					<c:out value="Reparación" />
				</c:if> <c:if test="${cita.tipo == 'preparacion_itv'}">
					<c:out value="Preparación ITV" />
				</c:if> <c:if test="${cita.tipo == 'modificacion'}">
					<c:out value="Modificación" />
				</c:if></td>
		</tr>
		<tr>
			<th>Tiempo</th>
			<td><c:out value="${cita.tiempo}" /></td>
		</tr>
		<tr>
			<th>Coste</th>
			<td><c:out value="${cita.coste}" /></td>
		</tr>
		<tr>
			<th>¿Es urgente?</th>
			<td><c:if test="${cita.esUrgente == 'TRUE'}">
					<c:out value="Si" />
				</c:if> <c:if test="${cita.esUrgente == 'FALSE'}">
					<c:out value="No" />
				</c:if>
			</td>
		</tr>
		
	</table>
	 <a class="btn btn-default" href='<spring:url value="/cliente/citas" htmlEscape="true"/>'>Volver</a>
	 
	
</petclinic:layout>
