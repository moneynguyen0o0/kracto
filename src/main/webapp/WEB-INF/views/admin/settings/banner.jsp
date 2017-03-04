<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="page-title">
	<div class="title-left">
		<h3><spring:message code="admin.banner.title"></spring:message></h3>
	</div>
	<div class="title-right">
		<a
			href="<c:url value="${applicationScope.adminUrl }/setting/banner?lang=en" />"
			class="action-btn ${lang eq 'en' ? 'btn-info' : 'btn-default'}"><spring:message code="admin.banner.lang.en"></spring:message></a>
		<a
			href="<c:url value="${applicationScope.adminUrl }/setting/banner?lang=vi" />"
			class="action-btn ${lang eq 'vi' ? 'btn-info' : 'btn-default'}"><spring:message code="admin.banner.lang.vi"></spring:message></a>
	</div>
</div>
<div class="content-main">
	<form action="${pageContext.request.contextPath}${applicationScope.adminUrl }/setting/banner" method="post" enctype="multipart/form-data">	
		<fieldset class="form-group">
			<label for="title"><spring:message code="admin.banner.lable.title"></spring:message></label>
			<input name="title" class="form-control" id="title" value="${title }" placeholder="<spring:message code="admin.banner.input.title"></spring:message>" required="required" />
		</fieldset>
		<fieldset class="form-group">
			<label for="description"><spring:message code="admin.banner.lable.description"></spring:message></label>
			<input name="description" class="form-control" id="description" value="${description }" placeholder="<spring:message code="admin.banner.input.description"></spring:message>" required="required" />
		</fieldset>
		<fieldset class="form-group">
			<label for="banner-input"><spring:message code="admin.banner.lable.image"></spring:message></label>
			<input type="file" name="bannerImage" id="banner-input" accept="image/jpeg"/>
		</fieldset>
		<img src="<c:url value="/images/bg.jpg" />" class="form-image" id="banner-image">
		<input type="hidden" name="lang" value="${lang }">
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />
		<div class="page-action">
			<button class="action-btn btn-primary btn-right" id="btn-save-menu"><spring:message code="admin.banner.submit"></spring:message></button>
		</div>
	</form>
</div>

<script>
function readURL(input) {
	if (input.files && input.files[0]) {
		var reader = new FileReader();
		reader.onload = function(e) {
			$('#banner-image').attr('src', e.target.result);
		}
		reader.readAsDataURL(input.files[0]);
	}
}
$("#banner-input").change(function() {
	readURL(this);
});
</script>