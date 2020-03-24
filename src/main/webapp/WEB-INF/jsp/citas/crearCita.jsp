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
                $("#fechaCita").datepicker({dateFormat:"dd/mm/yy 'hh:mm'"});
            });
        </script>
    </jsp:attribute>
    
    
    <jsp:body>
    <h2>
        Nueva Cita
    </h2>
  
    
    <form:form modelAttribute="cita" class="form-horizontal" id="add-cita-form">
        <div class="form-group has-feedback">
          <a href="vehiculo" class="btn btn-default">Primero escoge tu vehículo</a>
			<br>
			
			<input name="vehiculo" type="hidden"></input>
            <petclinic:inputField label="Fecha cita" name="fechaCita"/>
            <petclinic:inputField label="Descripción" name="descripcion"/>
<%--             <petclinic:inputField label="¿Es Urgente?" name="esUrgente"/> --%>
<%--             <petclinic:inputField label="Tipo de cita" name="tipo"/> --%>
            <input name="coste" type="hidden" value=0.0></input>
            <input name="tiempo" type="hidden" value=0></input>
            
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<table class="error-title">
						<tr>
							<th>¿Es urgente?</th>
							<td><select name="esUrgente">
									<option value="TRUE">Sí</option>
									<option value="FALSE">No</option>
							</select></td>
						</tr>
					</table>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<table class="error-title">
						<tr>
							<th>Tipo de cita</th>
							<td><select name="tipo">
									<option value="revision">Revisión</option>
									<option value="reparacion">Reparación</option>
									<option value="preparacion_itv">Preparación ITV	</option>
									<option value="modificacion">Modificación</option>
							</select></td>
						</tr>
					</table>
				</div>
			</div>
			
			
        </div>
        <div class="form-group">
             <div class="col-sm-offset-2 col-sm-10"> 
                   <button class="btn btn-default" type="submit">Pedir Cita</button>
            </div> 
        </div> 
    </form:form>
    <br>
    <a class="btn btn-default" href='<spring:url value="/cliente/citas" htmlEscape="true"/>'>Volver</a>
    </jsp:body>
</petclinic:layout>
