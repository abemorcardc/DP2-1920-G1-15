<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="vehiculo">

	<h2>Detalles de su vehiculo</h2>


	<table class="table table-striped">
		<tr>
			<th>Tipo de vehiculo</th>
			<td>
				<!-- REVISION,REPARACION,PREPARACION_ITV,MODIFICACION --> <c:if
					test="${vehiculo.tipoVehiculo == 'turismo'}">
					<c:out value="Turismo" />
				</c:if> <c:if test="${vehiculo.tipoVehiculo == 'deportivo'}">
					<c:out value="Deportivo" />
				</c:if> <c:if test="${vehiculo.tipoVehiculo == 'todoterreno'}">
					<c:out value="Todoterreno" />
				</c:if> <c:if test="${vehiculo.tipoVehiculo == 'camion'}">
					<c:out value="Camión" />
				</c:if> <c:if test="${vehiculo.tipoVehiculo == 'furgoneta'}">
					<c:out value="Furgoneta" />
				</c:if>
			</td>
		</tr>
		<tr>
			<th>Modelo</th>
			<td><c:out value="${vehiculo.modelo}" /></td>
		</tr>
		<tr>
			<th>Matricula</th>
			<td><c:out value="${vehiculo.matricula}" /></td>
		</tr>
		<tr>
			<th>Fecha de matriculacion</th>
			<td><c:out value="${vehiculo.fechaMatriculacion}" /></td>
		</tr>
		<tr>
			<th>Kilometraje</th>
			<td><c:out value="${vehiculo.kilometraje}" /></td>
		</tr>
	</table>

</petclinic:layout>
