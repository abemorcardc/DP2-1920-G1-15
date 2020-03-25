<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="cita">

	<h2>¿Está seguro de cancelar la cita?</h2>
	<table class="table table-striped">
		<tr>
			<th>Fecha de la Cita</th>
			<td><c:out value="${cita.fechaCita}"  /> </td>
		</tr>
		<tr>
			<th>DescripciÃ³n</th>
			<td><c:out value="${cita.descripcion}" /></td>
		</tr>
		
		</table>
	<form:form>
	 <div class="form-group">
             <div class="col-sm-offset-2 col-sm-10"> 
                   <button class="btn btn-default" type="submit">Cancelar Cita</button>
            </div> 
        </div> 
     </form:form>
	<a class="btn btn-default" href='<spring:url value="/cliente/citas" htmlEscape="true"/>'>No Cancelar</a>

	 
	
</petclinic:layout>
