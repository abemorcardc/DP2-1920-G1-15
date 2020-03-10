<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="citas">
    <h2>Citas</h2>

    <table id="citasTable" class="table table-striped">
        <thead>
        <tr>
            <th>Fecha cita</th>
            <th>Urgente</th>
            <th>Tipo cita</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${citas.citaList}" var="cita">
            <tr>
                <td>
                    <c:out value="${cita.fechaCita}"/>
                </td>
                <td>
                    <c:if test="${cita.esUrgente == TRUE}"><c:out value="SI"/></c:if>
                </td>
                 <td>
                    <c:out value="${cita.tipo}"/>
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
