<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<petclinic:layout pageName="citas">
    <h2>Citas</h2>

    <table id="vehiculosTable" class="table table-striped">
        <thead>
        <tr>
            <th>Fecha Cita</th>
            <th>Descripción</th>
            <th>Elegir Cita</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${results}" var="cita">
            <tr>
                
                <td>
                    <c:out value="${cita.fechaCita}"/>
                </td>
                 <td>
                    <c:out value="${cita.descripcion}"/>
                </td>
               
                <td>
                	<spring:url value="/mecanicos/${vehiculoId }/new" var="delUrl">
                	<spring:param name="citaId" value="${cita.id}"/>
                	</spring:url>
                	<a href="${fn:escapeXml(delUrl)}" class="btn btn-default">Elegir Cita</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <table class="table-buttons">
        <tr>
            <td>
                <a href="<spring:url value="/citas.xml" htmlEscape="true" />">View as XML</a>
            </td>            
        </tr>
    </table>
</petclinic:layout>