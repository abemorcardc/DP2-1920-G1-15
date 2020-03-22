<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="cita">
    <h2>
        Nueva Cita
    </h2>
    <form:form modelAttribute="cita" class="form-horizontal" id="add-cita-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Fecha cita yyyy-MM-ddTHH:mm" name="fechaCita"/>
            <petclinic:inputField label="Descripción" name="descripcion"/>
            <petclinic:inputField label="¿Es Urgente?" name="esUrgente"/>
            <petclinic:inputField label="Tipo de cita" name="tipo"/>
            <input name="coste" type="hidden" value=0.0></input>
            <input name="tiempo" type="hidden" value=0></input>
            
            <a href="vehiculo">Elegir Vehículo</a>
			
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                   <button class="btn btn-default" type="submit">Pedir Cita</button>
            </div>
        </div>
    </form:form>
    
    <a class="btn btn-default" href='<spring:url value="/cliente/citas" htmlEscape="true"/>'>Volver</a>
    
</petclinic:layout>
