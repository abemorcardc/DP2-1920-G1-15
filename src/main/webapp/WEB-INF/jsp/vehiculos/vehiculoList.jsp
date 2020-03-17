<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="vehiculos">
    <h2>Vehiculos</h2>

    <table id="vehiculosTable" class="table table-striped">
        <thead>
        <tr>
            <th>Tipo de Vehiculo</th>
            <th>Modelo</th>
            <th>Matricula</th>
            <th>Fecha de matriculacion</th>
            <th>Kilometraje</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${results}" var="vehiculo">
            <tr>
                <td> <!-- REVISION,REPARACION,PREPARACION_ITV,MODIFICACION -->
					<c:if test="${vehiculo.tipoVehiculo == 'turismo'}"> 
					<c:out value="Turismo" />
					</c:if>
					
					<c:if test="${vehiculo.tipoVehiculo == deportivo}"> 
					<c:out value="Deportivo" />
					</c:if>
					
					<c:if test="${vehiculo.tipoVehiculo == todoterreno}"> 
					<c:out value="Todoterreno" />
					</c:if>
					
					<c:if test="${vehiculo.tipoVehiculo == camion}"> 
					<c:out value="Camión" />
					</c:if>
					
					<c:if test="${vehiculo.tipoVehiculo == furgoneta}"> 
					<c:out value="Furgoneta" />
					</c:if>
					</td>
                <td>
                    <c:out value="${vehiculo.modelo}"/>
                </td>
                 <td>
                    <c:out value="${vehiculo.matricula}"/>
                </td>
                <td>
                    <c:out value="${vehiculo.fechaMatriculacion}"/>
                </td>
                 <td>
                    <c:out value="${vehiculo.kilometraje}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <table class="table-buttons">
        <tr>
            <td>
                <a href="<spring:url value="/vehiculos.xml" htmlEscape="true" />">View as XML</a>
            </td>            
        </tr>
    </table>
</petclinic:layout>
