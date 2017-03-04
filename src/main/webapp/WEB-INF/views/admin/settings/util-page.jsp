<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="page-title">
	<div class="title-left">
		<h3><spring:message code="admin.utilpage.title"></spring:message></h3>
	</div>
	<div class="title-right">
		<a
			href="<c:url value="${applicationScope.adminUrl }/setting/utilpage?lang=en" />"
			class="action-btn ${lang eq 'en' ? 'btn-info' : 'btn-default'}"><spring:message code="admin.utilpage.lang.en"></spring:message></a>
		<a
			href="<c:url value="${applicationScope.adminUrl }/setting/utilpage?lang=vi" />"
			class="action-btn ${lang eq 'vi' ? 'btn-info' : 'btn-default'}"><spring:message code="admin.utilpage.lang.vi"></spring:message></a>
	</div>
</div>
<div class="content-main">
	<form action="${pageContext.request.contextPath}${applicationScope.adminUrl }/setting/utilpage" method="post">	
		<c:forEach items="${messages }" var="entry">
			<fieldset class="form-group">
				<label>${entry.key }</label>
				<textarea class="ckeditor" name="${entry.key }">${entry.value }</textarea>
			</fieldset>
		</c:forEach>
		<input type="hidden" name="lang" value="${lang }">
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />
		<div class="page-action">
			<button class="action-btn btn-primary btn-right" id="btn-save-menu"><spring:message code="admin.utilpage.submit"></spring:message></button>
		</div>
	</form>
</div>

<!-- CKEditor -->
<script src="<c:url value="/ckeditor/ckeditor.js"/>"></script>