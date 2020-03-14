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
        <form:form modelAttribute="cita" class="form-horizontal">
            <input type="hidden" name="id" value="${cita.id}" />
            <div class="form-group has-feedback">
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
                <petclinic:inputField label="Descipcion" name="descripcion" />
              <petclinic:inputField label="Tiempo" name="tiempo" />
              <petclinic:inputField label="Coste" name="coste" />
              
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                <!--  con este parrafo lleva bien pero sin haber actualizado los datos -->
                 <spring:url value="/mecanicos/citas" var="ownerUrl">
							<spring:param name="citaId" value="${cita.id}" />
						</spring:url> <a href="${fn:escapeXml(ownerUrl)}" class="btn btn-default">Actualizar Cita </a>
            	</div>
            	<!--  otra forma seria pero no funciona...
            	<c:choose>
                        <c:when test="${pet['new']}">
                            <button class="btn btn-default" type="submit">Add Pet</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-default" type="submit">Update Pet</button>
                            <a class="btn btn-default" href="/owners/${owner.id}/pets/${pet.id}/delete">Delete Pet</a>
                        </c:otherwise>
                    </c:choose>
            	
            	 -->
            </div>
        </form:form>
        <c:if test="${!cita['new']}">
        </c:if>
    </jsp:body>
</petclinic:layout>
