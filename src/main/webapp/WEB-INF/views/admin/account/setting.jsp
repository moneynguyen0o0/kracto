<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<script>
	var link = document.createElement('link');
	link.href = '${pageContext.request.contextPath }/css/datetimepicker.css';
	link.type = 'text/css';
	link.rel = 'stylesheet';
	link.media = 'all';
	document.getElementsByTagName('head')[0].appendChild(link);
</script>
<div class="page-title">
	<div class="title-left">
		<h3><spring:message code="admin.account.setting.title"></spring:message></h3>
	</div>
</div>
<div class="content-main clearfix">
	<form:form method="post"
		action="${pageContext.request.contextPath}${applicationScope.adminUrl }/account/setting"
		modelAttribute="account" enctype="multipart/form-data">
		
		<div class="text-center photo-info">
			<c:set var="photo" value="/images/cd-photo.svg" />
			<c:if test="${not empty account.photo }">
				<c:set var="photo" value="/images/accounts/${account.photo }" />
			</c:if>
			<img src="<c:url value="${photo }"/>" class="avatar img-circle"
				alt="avatar" id="avatar">
			<h6><spring:message code="admin.account.setting.upload"></spring:message></h6>
			<input type="file" class="form-control" id="photo" name="photoImage" accept="image/jpeg">
		</div>
		
		<spring:message code="admin.account.setting.placeholder.fullname" var="fullnamePlaceholder"></spring:message>
		<spring:message code="admin.account.setting.placeholder.dob" var="dobPlaceholder"></spring:message>

		<div class="personal-info">
			<fieldset class="form-group">
				<label for="fullname"><spring:message code="admin.account.setting.fullname"></spring:message></label>
				<form:input path="fullname" class="form-control" id="fullname"
					placeholder="${fullnamePlaceholder }" required="required" />
			</fieldset>
			<fieldset class="form-group">
				<label for="dob"><spring:message code="admin.account.setting.dob"></spring:message></label>
				<form:input path="dob" class="form-control" id="dob"
					placeholder="${dobPlaceholder }" required="required" />
				<form:errors path="dob" cssClass="error" />
			</fieldset>
			<fieldset class="form-group">
				<label for="gender"><spring:message code="admin.account.setting.gender"></spring:message></label>
				<form:select path="gender" class="form-control" id="gender">
					<c:forEach var="gender" items="${genders }">
						<option value="${gender }"
							${account.gender eq gender ? 'selected="selected"' : '' }>${gender }</option>
					</c:forEach>
				</form:select>
				<form:errors path="gender" cssClass="error" />
			</fieldset>
			<form:hidden path="photo" />
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
			<div class="page-action">
				<button class="action-btn btn-primary btn-right" id="btn-save-menu"><spring:message code="admin.account.setting.submit"></spring:message></button>
			</div>
		</div>
	</form:form>
</div>
<script src="<c:url value="/js/jquery.datetimepicker.full.min.js"/>"></script>
<script src="<c:url value="/js/profile.js"/>"></script>