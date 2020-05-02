<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<petclinic:layout pageName="averias">
	<h2>Averías del vehículo</h2>

	<table id="citasMecanicoTable" class="table table-striped">
		<thead>
			<tr>
				<th>Nombre de la avería</th>
				<th>Descripción</th>
				<!-- <th>Coste</th>
				<th>Tiempo</th>
				<th>Piezas necesarias</th> -->
				<th>Complejidad</th>
				<th>¿Esta reparada?</th>
				<th>Vehículo</th>
				<!-- <th>Mecanico</th>-->
				<th>Cita correspondiente</th> 
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${results}" var="averia">
				<tr>
					<td>
                    <c:out value="${averia.nombre}"/>
                </td>
                
					<td><c:out value="${averia.descripcion}" /></td>
					
					<%-- <td><c:out value="${averia.coste} " /></td>

					<td><c:out value="${averia.tiempo}" /></td>

					<td><c:out value="${averia.piezasNecesarias}" /></td> --%>
					<td><c:out value="${averia.complejidad}" /></td>
					<td><c:if test="${averia.estaReparada == 'TRUE'}">
							<c:out value="Si" />
						</c:if> <c:if test="${averia.estaReparada == 'FALSE'}">
							<c:out value="No" />
						</c:if></td>
					<td><c:out value="${averia.vehiculo.modelo}: ${averia.vehiculo.matricula}" /></td>
					<%-- <td><c:out value="${averia.mecanico.nombre}" /></td>  --%>
					<td><c:out value="${averia.cita.descripcion}" /></td>
					<td><spring:url value="/mecanicos/vehiculos/{vehiculoId}/averia/{averiaId}/edit" var="editUrl">
					<spring:param name="vehiculoId" value="${averia.vehiculo.id}" />
					<spring:param name="averiaId" value="${averia.id}" />
					</spring:url> <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar Averia</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
<a class="btn btn-default" href='<spring:url value="/mecanicos/citas" htmlEscape="true"/>'>Volver a citas</a>

    	
</petclinic:layout>
