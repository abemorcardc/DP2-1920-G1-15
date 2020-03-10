<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="citas">
	<h2>Mis citas</h2>

	<table id="citasMecanicoTable" class="table table-striped">
		<thead>
			<tr>
				<th>ID</th>
				<th>Fecha de la cita</th>
				<th>Urgente</th>
				<th>Descripcion</th>
				<th>Tipo de cita</th>

				<th>Cliente</th>
				<th>Aceptado</th>
				<th>Tiempo</th>
				<th>Coste</th>

				<th>Vehículo dañado</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${citas.citaList}" var="cita">
				<tr>
					<td><c:out value="${cita.id}" /></td>
					<td><c:out value="${cita.fechaCita}" /></td>
					<td><c:if test="${cita.esUrgente = TRUE}">
							<c:out value="Si" />
						</c:if> <c:if test="${cita.esUrgente = FALSE}">
							<c:out value="No" />
						</c:if></td>
					<td><c:out value="${cita.descripcion} " /></td>
					
					<td> <!-- REVISION,REPARACION,PREPARACION_ITV,MODIFICACION -->
					<c:if test="${cita.tipo = revision}"> 
					<c:out value="Revision" />
					</c:if>
					
					<c:if test="${cita.tipo = reparacion}"> 
					<c:out value="Reparacion" />
					</c:if>
					
					<c:if test="${cita.tipo = preparacion_itv}"> 
					<c:out value="Preparacion ITV" />
					</c:if>
					
					<c:if test="${cita.tipo = modificacion}"> 
					<c:out value="Modificacion" />
					</c:if>
					</td>
					
					<td><c:out value="${cita.cliente}" /></td>
					<td><c:if test="${cita.esAceptado = TRUE}">
							<c:out value="SI" />
						</c:if> <c:if test="${cita.esAceptado = FALSE}">
							<c:out value="NO" />
						</c:if></td>
					<td><c:out value="${cita.tiempo}" /></td>
					<td><c:out value="${cita.coste}" /></td>
					<td><c:out value="${cita.vehiculo}" /></td>
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
