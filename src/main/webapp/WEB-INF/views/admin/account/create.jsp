<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="page-title">
	<div class="title-left">
		<h3><spring:message code="admin.account.create.title"></spring:message></h3>
	</div>
</div>

<spring:message code="admin.account.create.placehoder.username" var="usernamePlaceholder"></spring:message>
<spring:message code="admin.account.create.placehoder.password" var="passwordPlaceholer"></spring:message>
<spring:message code="admin.account.create.placehoder.email" var="emailPlaceholder"></spring:message>

<div class="content-main">
	<form:form method="post"
		action="${pageContext.request.contextPath}${applicationScope.adminUrl }/accounts/save"
		modelAttribute="account">
		<fieldset class="form-group">
			<label for="username"><spring:message code="admin.account.create.username"></spring:message></label>
			<form:input path="username" class="form-control" id="username"
				placeholder="${usernamePlaceholder }" required="required" />
			<form:errors path="username" cssClass="error" />
		</fieldset>
		<fieldset class="form-group">
			<label for="password"><spring:message code="admin.account.create.password"></spring:message></label>
			<form:password path="password" class="form-control" id="password"
				placeholder="${passwordPlaceholer }" required="required" />
			<form:errors path="password" cssClass="error" />
		</fieldset>
		<fieldset class="form-group">
			<label for="email"><spring:message code="admin.account.create.email"></spring:message></label>
			<form:input path="email" type="email" class="form-control" id="email"
				placeholder="${emailPlaceholder }" required="required" />
			<form:errors path="email" cssClass="error" />
		</fieldset>
		<fieldset class="form-group">
			<label for="role"><spring:message code="admin.account.create.role"></spring:message></label>
			<form:select path="role" multiple="true" class="form-control" required="required"
				id="role">
				<c:forEach var="role" items="${roles }">
					<option value="${role }">${role }</option>
				</c:forEach>
			</form:select>
			<form:errors path="role" cssClass="error" />
		</fieldset>
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />
		<div class="page-action">
			<button class="action-btn btn-primary btn-right" id="btn-save-menu"><spring:message code="admin.account.create.submit"></spring:message></button>
		</div>
	</form:form>
</div>