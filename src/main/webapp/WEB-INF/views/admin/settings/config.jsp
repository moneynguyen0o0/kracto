<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<div class="page-title">
	<div class="title-left">
		<h3 class="capitalize">${name }</h3>
	</div>
</div>
<div class="content-main">
	<form action="${pageContext.request.contextPath}${applicationScope.adminUrl }/setting/config/${name }/save" method="post">
		<tags:load-messages messages="${messages }"></tags:load-messages>
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />
		<div class="page-action">
			<button class="action-btn btn-primary btn-right" id="btn-save-menu"><spring:message code="admin.config.submit"></spring:message></button>
		</div>
	</form>
</div>