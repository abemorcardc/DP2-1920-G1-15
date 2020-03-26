<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="cita">

	<h2>Detalles de la cita</h2>


	<table class="table table-striped">
		<tr>
			<th>Fecha de la Cita</th>
			<td><c:out value="${cita.fechaCita}"  /> </td>
		</tr>

		<tr>
			<th>Descripci�n</th>
			<td><c:out value="${cita.descripcion}" /></td>
		</tr>
		<tr>
			<th>Estado de la cita</th>
			<td><c:if test="${cita.estadoCita == 'pendiente'}">
					<c:out value="Pendiente" />
				</c:if> <c:if test="${cita.estadoCita == 'aceptada'}">
					<c:out value="Aceptada" />
				</c:if> <c:if test="${cita.estadoCita == 'cancelada'}">
					<c:out value="Cancelada" />
				</c:if></td>
		</tr>
		<tr>
			<th>Tipo de la cita</th>
			<td><c:if test="${cita.tipo == 'revision'}">
					<c:out value="Revisi�n" />
				</c:if> <c:if test="${cita.tipo == 'reparacion'}">
					<c:out value="Reparaci�n" />
				</c:if> <c:if test="${cita.tipo == 'preparacion_itv'}">
					<c:out value="Preparaci�n ITV" />
				</c:if> <c:if test="${cita.tipo == 'modificacion'}">
					<c:out value="Modificaci�n" />
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
			<th>�Es urgente?</th>
			<td><c:if test="${cita.esUrgente == 'TRUE'}">
					<c:out value="Si" />
				</c:if> <c:if test="${cita.esUrgente == 'FALSE'}">
					<c:out value="No" />
				</c:if>
			</td>
		</tr>
		<tr>
			<th>Veh�culo</th>
			<td>
			   <spring:url value="/mecanicos/{vehiculoId}" var="averiasUrl">
               		<spring:param name="vehiculoId" value="${cita.vehiculo.id}"/>
               </spring:url>
               <a href="${fn:escapeXml(averiasUrl)}"><c:out value="${cita.vehiculo.modelo}: ${cita.vehiculo.matricula}" /></a>
			<!-- <c:out value="${cita.vehiculo.modelo}: ${cita.vehiculo.matricula}" /> -->
			</td>
		</tr>
	</table>
	
<a class="btn btn-default" href='<spring:url value="/mecanicos/citas" htmlEscape="true"/>'>Volver</a>
    
</petclinic:layout>
