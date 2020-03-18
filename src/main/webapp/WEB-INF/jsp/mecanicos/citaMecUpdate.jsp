<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<petclinic:layout pageName="citas">
	
	<jsp:body>
        <h2>
           Editar Cita
        </h2>
        
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
                    <label class="col-sm-2 control-label">Vehículo dañado</label>
                    <div class="col-sm-10">
                        <c:out value="${cita.vehiculo.modelo}: ${cita.vehiculo.matricula}" />
                    </div>
                </div>
            <form:form modelAttribute="cita" class="form-horizontal">
	            <input type="hidden" name="id" value="${cita.id}" />
	            <input type="hidden" name="fechaCita" value="${cita.fechaCita}" />
	       
	          
	            <div class="form-group has-feedback">
	               <petclinic:inputField label="Descripcion" name="descripcion" />
	              <petclinic:inputField label="Tiempo" name="tiempo" />
	              <petclinic:inputField label="Coste" name="coste" />
	              
	            </div>
            
            <button class="btn btn-default" type="submit">Update</button>
           
        </form:form>
        <c:if test="${!cita['new']}">
        </c:if>
    </jsp:body>
</petclinic:layout>
