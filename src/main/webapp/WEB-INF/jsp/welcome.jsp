<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->

<petclinic:layout pageName="home">
	<h2>
		<fmt:message key="welcome" />
	</h2>
	<div class="row">
		<div class="col-md-12">
			<spring:url value="/resources/images/car-home.png" htmlEscape="true" var="petsImage" />
			<img style="float:left" class="img-responsive" src="${petsImage}" />
			<h3>Somos un taller en Sevilla con más de 50 años de experiencia en el sector. Nos dedicamos a prestar diferentes servicios
				de reparación de automóviles, mecánica en general, neumáticos y chapa y pintura. Somos especialistas en revisiones de coche y
				arreglos de todo tipo.</h3>
		</div>
	</div>
</petclinic:layout>
