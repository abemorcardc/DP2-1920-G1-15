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
        <c:forEach items="${vehiculos.vehiculoList}" var="vehiculo">
            <tr>
                <td>
                    <c:out value="${vehiculo.tipoVehiculo}"/>
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
