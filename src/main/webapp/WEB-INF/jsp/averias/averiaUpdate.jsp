<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<petclinic:layout pageName="averia">
        <h2>
           Editar Averia
        </h2>
        
            <form:form modelAttribute="averia" class="form-horizontal">
	            <input type="hidden" name="id" value="${averia.id}" />
	          
	            <div class="form-group has-feedback">
	              
	              <petclinic:inputField label="Nombre" name="nombre" required="true"/>
			      <petclinic:inputField label="Descripcion" name="descripcion" required="true"/>
			      <petclinic:inputField label="Coste" name="coste" required="true" pattern="^\\d{1,6}\\.?\\d{1,2}?$" title="Debe ser un numero positivo"/>
			      <petclinic:inputField label="Tiempo" name="tiempo" required="true" pattern="^\\d{1,6}$" title="Debe ser un numero positivo"/>
			      <petclinic:inputField label="Piezas necesarias" name="piezasNecesarias" required="true" pattern="^\\d{1,6}$" title="Debe ser un numero positivo"/>

	              <table class="error-title">
				<tr>
					<th>Esta reparada</th>
					<td><select name="estaReparada" >
							<option value="true">Si</option>
							<option value="false">No</option>
					</select></td>
				</tr>
				<tr>
					<th>Tipo de averia</th>
					<td><select name="complejidad" >
							<option value="BAJA">Baja</option>
							<option value="MEDIA">Media</option>
							<option value="ALTA">Alta</option>
					</select></td>
				</tr>
			</table>
	            </div>
            
            <button class="btn btn-default" type="submit">Actualizar</button>
            <spring:url value="/mecanicos/vehiculos/{vehiculoId}/averia" var="listaAveriaURL">
			<spring:param name="vehiculoId" value="${averia.vehiculo.id}" />
			</spring:url> <a href="${fn:escapeXml(listaAveriaURL)}" class="btn btn-default">Volver</a>
           
        </form:form>
</petclinic:layout>