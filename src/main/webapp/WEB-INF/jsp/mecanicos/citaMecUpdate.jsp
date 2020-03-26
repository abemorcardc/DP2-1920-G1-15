<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<petclinic:layout pageName="citas">
	
	<jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#fechaCita").datepicker({dateFormat:"dd/mm/yy 'hh:mm'"});
            });
        </script>
    </jsp:attribute>
    
	<jsp:body>
        <h2>
           Editar Cita
        </h2>
        <form:form modelAttribute="cita" class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-2 control-label">Fecha de la cita</label>
                    <div class="col-sm-10">
                        <c:out value="${cita.fechaCita}" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">Cliente</label>
                    <div class="col-sm-10">
                        <c:out value="${cita.cliente.nombre} ${cita.cliente.apellidos}" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">Veh�culo da�ado</label>
                    <div class="col-sm-10">
                        <c:out value="${cita.vehiculo.modelo}: ${cita.vehiculo.matricula}" />
                    </div>
                </div>
            
	            <input type="hidden" name="id" value="${cita.id}" />
	            <input type="hidden" name="fechaCita" value="${cita.fechaCita}" />
	       
	          
	            <div class="form-group has-feedback">
	            	<petclinic:inputField label="Fecha cita" name="fechaCita"/>
	              <petclinic:inputField label="Descripci�n" name="descripcion" />
	              <petclinic:inputField label="Tiempo" name="tiempo" />
	              <petclinic:inputField label="Coste" name="coste" />
	              <select name="estadoCita">
	              	<c:if test="${cita.estadoCita == 'pendiente'}">
						<option value="pendiente" selected>Pendiente</option>
						<option value="aceptada">Aceptada</option>
						<option value="cancelada">Cancelada</option>
					</c:if> <c:if test="${cita.estadoCita == 'aceptada'}">
						<option value="pendiente">Pendiente</option>
						<option value="aceptada" selected>Aceptada</option>
						<option value="cancelada">Cancelada</option>
					</c:if> <c:if test="${cita.estadoCita == 'cancelada'}">
						<option value="pendiente">Pendiente</option>
						<option value="aceptada">Aceptada</option>
						<option value="cancelada" selected>Cancelada</option>
					</c:if>
				</select>
	              
	            </div>
            
            <button class="btn btn-default" type="submit">Update</button>
           
        </form:form>
        <c:if test="${!cita['new']}">
        </c:if>
       <br>
       <a class="btn btn-default" href='<spring:url value="/mecanicos/citas" htmlEscape="true"/>'>Volver</a>
            
    </jsp:body>
</petclinic:layout>
