<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<petclinic:layout pageName="citas">
	<h2>Mis citas</h2>

	<table id="citasMecanicoTable" class="table table-striped">
		<thead>
			<tr>
				<th>Detalle de la cita</th>
				<th>Fecha de la cita</th>
				<th>Urgente</th>
				<th>Descripcion</th>
				<th>Tipo de cita</th>

				<th>Cliente</th>
				<th>Aceptado</th>
				<th>Tiempo</th>
				<th>Coste</th>

				<th>Veh�culo da�ado</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${results}" var="cita">
				<tr>
					<td><spring:url value="/mecanicos/citas/{citaId}" var="ownerUrl">
							<spring:param name="citaId" value="${cita.id}" />
						</spring:url> <a href="${fn:escapeXml(ownerUrl)}" class="btn btn-default">Ver Cita </a></td>

					<td><c:out value="${cita.fechaCita}" /></td>
					<td><c:if test="${cita.esUrgente == 'TRUE'}">
							<c:out value="Si" />
						</c:if> <c:if test="${cita.esUrgente == 'FALSE'}">
							<c:out value="No" />
						</c:if></td>
					<td><c:out value="${cita.descripcion} " /></td>

					<td>
						<!-- REVISION,REPARACION,PREPARACION_ITV,MODIFICACION --> <c:if test="${cita.tipo == 'revision'}">
							<c:out value="Revision" />
						</c:if> <c:if test="${cita.tipo == 'reparacion'}">
							<c:out value="Reparacion" />
						</c:if> <c:if test="${cita.tipo == 'preparacion_itv'}">
							<c:out value="Preparacion ITV" />
						</c:if> <c:if test="${cita.tipo == 'modificacion'}">
							<c:out value="Modificacion" />
						</c:if>
					</td>

					<td><c:out value="${cita.cliente.usuario.nombreUsuario}" /></td>
					<td><c:if test="${cita.esAceptado == 'TRUE'}">
							<c:out value="Si" />
						</c:if> <c:if test="${cita.esAceptado == 'FALSE'}">
							<c:out value="No" />
						</c:if></td>
					<td><c:out value="${cita.tiempo}" /></td>
					<td><c:out value="${cita.coste}" /></td>
					<td><c:out value="${cita.vehiculo.matricula}" /></td>
					<td><spring:url value="/mecanicos/citas/{citaId}/edit" var="editUrl">
							<spring:param name="citaId" value="${cita.id}" />
						</spring:url> <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar Cita</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<table class="table-buttons">
		<tr>
			<td><a href="<spring:url value="/citas.xml" htmlEscape="true" />">View as XML</a></td>
		</tr>
	</table>
</petclinic:layout>
