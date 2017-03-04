<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>	
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<div class="page-title">
	<div class="title-left">
		<h3 class="capitalize">${name }</h3>
	</div>
	<div class="title-right">
		<a
			href="<c:url value="${applicationScope.adminUrl }/setting/i18n/${name }?lang=en" />"
			class="action-btn ${lang eq 'en' ? 'btn-info' : 'btn-default'}"><spring:message code="admin.config.lang.en"></spring:message></a>
		<a
			href="<c:url value="${applicationScope.adminUrl }/setting/i18n/${name }?lang=vi" />"
			class="action-btn ${lang eq 'vi' ? 'btn-info' : 'btn-default'}"><spring:message code="admin.config.lang.vi"></spring:message></a>
	</div>
</div>
<div class="content-main">
	<form action="${pageContext.request.contextPath}${applicationScope.adminUrl }/setting/i18n/${name }/save" method="post">
		<tags:load-messages messages="${messages }"></tags:load-messages>
		<input type="hidden" name="lang" value="${lang }">
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />
		<div class="page-action">
			<button class="action-btn btn-primary btn-right" id="btn-save-menu"><spring:message code="admin.config.submit"></spring:message></button>
		</div>
	</form>
</div>