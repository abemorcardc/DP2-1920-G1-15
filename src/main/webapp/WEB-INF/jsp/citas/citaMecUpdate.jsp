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
        <form:form modelAttribute="cita" class="form-horizontal" id="update-visit-form">
        	<div class="form-group has-feedback">
               <%--  <div class="form-group">
                    <label class="col-sm-2 control-label">Fecha de la cita</label>
                    <div class="col-sm-10">
                        <c:out value="${cita.fechaCita}" />
                    </div>
                </div> --%>
                <div class="form-group">
                    <label class="col-sm-2 control-label">Cliente</label>
                    <div class="col-sm-10">
                        <c:out value="${cita.cliente.nombre} ${cita.cliente.apellidos}" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">Vehículo dañado</label>
                    <div class="col-sm-10">
                        <c:out value="${cita.vehiculo.modelo}: ${cita.vehiculo.matricula}" />
                    </div>
                </div>
            
	            <input type="hidden" name="id" value="${cita.id}" />
	        	<div class="form-group">
	              <petclinic:inputField label="Fecha cita" name="fechaCita" placeholder="dd/MM/yyyy HH:mm" 
	              pattern="^([0-2][0-9]|(3)[0-1])(\/)(((0)[0-9])|((1)[0-2]))(\/)\d{4}(\s)([0-2][0-9])(:)([0-5][0-9])$"
	               required="true"  title="Introduce una fecha en futuro: Ejemplo: 12/12/2021 10:30 "/>
	              <petclinic:inputField label="Descripción" name="descripcion" required="true" />
	              <petclinic:inputField label="Tiempo" name="tiempo" required="true" pattern="^\\d{1,6}$" placeholder="Solo numeros"  title="Ejemplo: 5"/>
	              <petclinic:inputField label="Coste" name="coste" required="true" pattern="\d{1,3}.\d{0,2}" placeholder="Solo numeros"  title="Ejemplo: 50.00" />
	              <div class="form-group">
	              <label class="col-sm-2 control-label">Estado cita</label>  
	             <div class="col-sm-10" >
	             <select name="estadoCita">
									<option value="pendiente">Pendiente</option>
									<option value="aceptada">Aceptada</option>
									<option value="cancelada">Cancelada</option>
									<option value="finalizada">Finalizada</option>
							</select>
							</div>
							</div>
	             </div>
	             
	             
	             
	          </div>
            
            <button class="btn btn-default" type="submit">Update</button>
           
        </form:form>
        <c:if test="${!cita['new']}">
        </c:if>
       <br>
       <a class="btn btn-default" href='<spring:url value="/mecanicos/citas" htmlEscape="true"/>'>Volver</a>
            
    </jsp:body>
</petclinic:layout>
