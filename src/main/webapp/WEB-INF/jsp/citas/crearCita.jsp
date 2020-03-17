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
            <petclinic:inputField label="Fecha Cita" name="fechaCita"/>
            <petclinic:inputField label="Descripcion" name="descripcion"/>
            <petclinic:inputField label="Es Urgente" name="esUrgente"/>
            <petclinic:inputField label="Tipo" name="tipo"/>
            <petclinic:inputField label="Vehiculo" name="vehiculo"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                   <button class="btn btn-default" type="submit">Pedir Cita</button>
            </div>
        </div>
    </form:form>
</petclinic:layout>
