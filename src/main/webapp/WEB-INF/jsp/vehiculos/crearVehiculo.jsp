<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="vehiculo">
	<h2>Nuevo vehiculo</h2>
	<form:form modelAttribute="vehiculo" class="form-horizontal" id="add-vehiculo-form">
		<div class="form-group has-feedback">
			<petclinic:inputField label="Tipo de vehiculo" name="tipoVehiculo" />
			<petclinic:inputField label="Matricula" name="matricula" />
			<petclinic:inputField label="Fecha de matriculacion" name="fechaMatriculacion" />
			<petclinic:inputField label="Modelo" name="modelo" />
			<petclinic:inputField label="Kilometraje" name="kilometraje" />
			<input name="activo" type="hidden" value=true></input>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<button class="btn btn-default" type="submit">Crear</button>
			</div>
		</div>
	</form:form>
</petclinic:layout>
