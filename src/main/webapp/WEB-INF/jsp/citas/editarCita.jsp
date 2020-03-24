<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="cita">
    <jsp:attribute name="customScript">
        		<script>
            	$(function () {
                $("#fechaCita").datepicker({dateFormat:"yy-mm-dd'Thh:mm'"});
           		 });
       		 </script>
    		 </jsp:attribute>
    <jsp:body>
        <h2>
           Editar Cita
        </h2>
        <form:form modelAttribute="cita" class="form-horizontal">
              
            
                <div class="form-group">
                    <label class="col-sm-2 control-label">Mecanico</label>
                    <div class="col-sm-10">
                        <c:out value="${cita.mecanico.nombre} ${cita.mecanico.apellidos}" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">Vehículo dañado</label>
                    <div class="col-sm-10">
                        <c:out value="${cita.vehiculo.modelo}: ${cita.vehiculo.matricula}" />
                    </div>
                </div>
                 <div class="form-group">
                    <label class="col-sm-2 control-label">Coste</label>
                    <div class="col-sm-10">
                        <c:out value="${cita.coste}" />
                    </div>
                </div>
            
	            <input type="hidden" name="id" value="${cita.id}" />
	            <input type="hidden" name="coste" value="${cita.coste}" />
	            <input type="hidden" name="tipo" value="${cita.tipo}" />
	       
	          
	            <div class="form-group has-feedback">
	              <petclinic:inputField label="Fecha cita" name="fechaCita" />
	              <petclinic:inputField label="Descripción" name="descripcion" />
	              <petclinic:inputField label="Tiempo" name="tiempo" />
	            </div>
            	
            	<td>
             		<spring:url value="/cliente/citas/vehiculo-editar" var="delUrl">
             		<spring:param name="citaId" value="${cita.id}"/>
             		</spring:url>
             		<a href="${fn:escapeXml(delUrl)}" class="btn btn-default">Elegir Vehiculo</a>
        		</td>
            <button class="btn btn-default" type="submit">Editar</button>
           
        </form:form>
        <c:if test="${!cita['new']}">
        </c:if>
       <br>
       <a class="btn btn-default" href='<spring:url value="/cliente/citas" htmlEscape="true"/>'>Volver</a>
            
    </jsp:body>
</petclinic:layout>