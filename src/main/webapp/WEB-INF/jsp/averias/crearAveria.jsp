<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="averia">
    
    <jsp:body>
    <h2>
        Nueva Avería
    </h2>
  
    <form:form modelAttribute="averia" class="form-horizontal" id="add-cita-form">
        <div class="form-group has-feedback">
			 <a href="citas" class="btn btn-default">Primero elija la cita correspondiente</a>
			<br>
	        <petclinic:inputField label="Nombre" name="nombre"/>     
            <petclinic:inputField label="Descripción" name="descripcion"/>
            <petclinic:inputField label="Coste" name="coste"/>
            <petclinic:inputField label="Tiempo" name="tiempo"/>
            <petclinic:inputField label="Piezas Necesarias" name="piezasNecesarias"/>
            
			<input name="estaReparada" type="hidden" value="False"></input>
			
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<table class="error-title">
						<tr>
							<th>Complejidad</th>
							<td><select name="complejidad">
									<option value="BAJA">Baja</option>
									<option value="MEDIA">Media</option>
									<option value="ALTA">Alta</option>
									
							</select></td>
						</tr>
					</table>
				</div>
			</div>

			
			
        </div>
        <div class="form-group">
             <div class="col-sm-offset-2 col-sm-10"> 
                   <button class="btn btn-default" type="submit">Crear Avería</button>
            </div> 
        </div> 
    </form:form>
    <br>
    <a class="btn btn-default" href='<spring:url value="/mecanicos/${vehiculoId}" htmlEscape="true"/>'>Volver</a>
    </jsp:body>
</petclinic:layout>
