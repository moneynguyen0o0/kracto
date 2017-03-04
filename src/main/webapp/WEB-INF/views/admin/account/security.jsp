<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<spring:message code="admin.account.security.placeholder.security.password.current" var="currentPasswordPlaceholder"></spring:message>
<spring:message code="admin.account.security.placeholder.security.password.new" var="newPasswordPlaceholder"></spring:message>
<spring:message code="admin.account.security.placeholder.security.password.confirm" var="confirmPasswordPlaceholder"></spring:message>
<div class="page-title">
	<div class="title-left">
		<h3><spring:message code="admin.account.security.title"></spring:message></h3>
	</div>
</div>
<div class="content-main">
	<form:form method="post"
		action="${pageContext.request.contextPath}${applicationScope.adminUrl }/account/security"
		modelAttribute="newPassword">
		<fieldset class="form-group">
			<label for="current-password"><spring:message code="admin.account.security.password.current"></spring:message></label>
			<form:password path="currentPassword" class="form-control" id="current-password"
				placeholder="${currentPasswordPlaceholder }" required="required" />
			<form:errors path="currentPassword" cssClass="error" />
		</fieldset>
		<fieldset class="form-group">
			<label for="new-password"><spring:message code="admin.account.security.password.new"></spring:message></label>
			<form:password path="newPassword" class="form-control" id="new-password"
				placeholder="${newPasswordPlaceholder }" required="required" />
			<form:errors path="newPassword" cssClass="error" />
		</fieldset>
		<fieldset class="form-group">
			<label for="confirm-password"><spring:message code="admin.account.security.password.confirm"></spring:message></label>
			<form:password path="confirmPassword" class="form-control" id="confirm-password"
				placeholder="${confirmPasswordPlaceholder }" required="required" />
			<form:errors path="confirmPassword" cssClass="error" />
		</fieldset>
		<div class="form-note"><span><spring:message code="admin.account.security.note.title"></spring:message> </span><spring:message code="admin.account.security.note.content"></spring:message></div>
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />
		<div class="page-action">
			<button class="action-btn btn-primary btn-right" id="btn-save-menu"><spring:message code="admin.account.security.submit"></spring:message></button>
		</div>
	</form:form>
</div>