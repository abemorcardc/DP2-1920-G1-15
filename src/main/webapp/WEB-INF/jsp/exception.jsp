<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<petclinic:layout pageName="error">

    <spring:url value="/resources/images/car-home.png" var="petsImage"/>
    <img src="${petsImage}"/>

    <h2>Something happened...</h2>

    <p>${exception.message}</p>
    
    <spring:url value="{clienteId}/vehiculos" var="vehiculoUrl">
                                    <spring:param name="clienteId" value="<sec:authentication property="name" />"/>
                                </spring:url>
                                <a href="${fn:escapeXml(vehiculoUrl)}">Vehiculos</a>

</petclinic:layout>
