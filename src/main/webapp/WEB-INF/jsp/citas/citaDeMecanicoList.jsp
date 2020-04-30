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
				<th></th>
				<th>Fecha de la cita</th>
				<th>¿Es urgente?</th>
				<th>Descripción</th>
				<th>Tipo de cita</th>
				<th>Estado de la cita</th>
				<th>Vehículo dañado</th>
				<th></th>
				<th></th>
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
							<c:out value="Revisión" />
						</c:if> <c:if test="${cita.tipo == 'reparacion'}">
							<c:out value="Reparación" />
						</c:if> <c:if test="${cita.tipo == 'preparacion_itv'}">
							<c:out value="Preparación ITV" />
						</c:if> <c:if test="${cita.tipo == 'modificacion'}">
							<c:out value="Modificación" />
						</c:if>
					</td>


					<td><c:if test="${cita.estadoCita == 'pendiente'}">
							<c:out value="Pendiente" />
						</c:if> <c:if test="${cita.estadoCita == 'aceptada'}">
							<c:out value="Aceptada" />
						</c:if> <c:if test="${cita.estadoCita == 'cancelada'}">
							<c:out value="Cancelada" />
						</c:if> <c:if test="${cita.estadoCita == 'finalizada'}">
							<c:out value="Finalizada" />
						</c:if></td>

					<td><c:out value="${cita.vehiculo.modelo}: ${cita.vehiculo.matricula}" /></td>
					<td><spring:url value="/mecanicos/citas/{citaId}/edit" var="editUrl">
							<spring:param name="citaId" value="${cita.id}" />
						</spring:url> <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar Cita</a></td>
						
					<td><spring:url value="/mecanicos/vehiculos/{vehiculoId}/averia" var="averiasUrl">
						<spring:param name="vehiculoId" value="${cita.vehiculo.id}" />
					</spring:url> <a href="${fn:escapeXml(averiasUrl)}" class="btn btn-default">Listar Averias</a>
				</td>

				</tr>
			</c:forEach>
		</tbody>
	</table>


</petclinic:layout>
