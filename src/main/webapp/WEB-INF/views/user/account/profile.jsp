<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<spring:message code="user.profile.placeholder.fullname" var="fullnamePlaceholder"></spring:message>
<spring:message code="user.profile.placeholder.dob" var="dobPlaceholder"></spring:message>
<spring:message code="user.profile.password.placeholder.current" var="currentPasswordPlacholder"></spring:message>
<spring:message code="user.profile.password.placeholder.new" var="newPasswordPlaceholder"></spring:message>
<spring:message code="user.profile.password.placeholder.confirm" var="confirmPasswordPlaceholder"></spring:message>
<script>
	var link = document.createElement('link');
	link.href = '${pageContext.request.contextPath }/css/datetimepicker.css';
	link.type = 'text/css';
	link.rel = 'stylesheet';
	link.media = 'all';
	document.getElementsByTagName('head')[0].appendChild( link);
</script>
<div class="content-wrap">
	<div class="content-padding">
		<div id="user-profile">
			<h1 class="content-title"><spring:message code="user.profile.title"></spring:message></h1>
			<div class="profile-section">
				<form:form method="post"
					action="${pageContext.request.contextPath}/account/profile"
					modelAttribute="account" enctype="multipart/form-data">
					<div class="photo-info">
						<c:set var="photo" value="/images/cd-photo.svg" />
						<c:if test="${not empty account.photo }">
							<c:set var="photo"
								value="/images/accounts/${account.photo }" />
						</c:if>
						<img src="<c:url value="${photo }"/>" class="avatar img-circle"
							alt="avatar" id="avatar">
						<h6><spring:message code="user.profile.upload.info"></spring:message></h6>
						<div class="file-upload btn">
						    <span><spring:message code="user.profile.upload.button"></spring:message></span>
						    <input type="file" class="upload" id="photo"
							name="photoImage" accept="image/jpeg">
						</div>
					</div>					
					<div class="personal-info">
						<div class="form-group">
							<label for="fullname"><spring:message code="user.profile.fullname"></spring:message></label>
							<form:input path="fullname" id="fullname"
								placeholder="${fullnamePlaceholder }" required="required" />
						</div>
						<div class="form-group">
							<label for="dob"><spring:message code="user.profile.dob"></spring:message></label>
							<form:input path="dob" id="dob"
								placeholder="${dobPlaceholder }" required="required" />
							<form:errors path="dob" cssClass="error" />
						</div>
						<div class="form-group">
							<label for="gender"><spring:message code="user.profile.gender"></spring:message></label>
							<form:select path="gender" id="gender">
								<c:forEach var="gender" items="${genders }">
									<option value="${gender }"
										${account.gender eq gender ? 'selected="selected"' : '' }>${gender }</option>
								</c:forEach>
							</form:select>
							<form:errors path="gender" cssClass="error" />
						</div>
						<form:hidden path="photo" />
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />
						<div class="action-section">
							<button class="btn bnt-save"><spring:message code="user.profile.update"></spring:message></button>
						</div>
					</div>
				</form:form>
			</div>
			<div class="password-section">
				<h3 class="title-section"><spring:message code="user.profile.password.title"></spring:message></h3>
				<form:form method="post"
					action="${pageContext.request.contextPath}/account/change-password"
					modelAttribute="newPassword">
					<div class="password-changes">
						<div class="form-group">
							<label for="current-password"><spring:message code="user.profile.password.current"></spring:message></label>
							<form:password path="currentPassword" id="current-password" placeholder="${currentPasswordPlacholder }" required="required" />
							<form:errors path="currentPassword" cssClass="error" />
						</div>
						<div class="form-group">
							<label for="new-password"><spring:message code="user.profile.password.new"></spring:message></label>
							<form:password path="newPassword" id="new-password" placeholder="${newPasswordPlaceholder }" required="required" />
							<form:errors path="newPassword" cssClass="error" />
						</div>
						<div class="form-group">
							<label for="confirm-password"><spring:message code="user.profile.password.confirm"></spring:message></label>
							<form:password path="confirmPassword" id="confirm-password" placeholder="${confirmPasswordPlaceholder }" required="required" />
							<form:errors path="confirmPassword" cssClass="error" />
						</div>
						<input type="hidden" name="${_csrf.parameterName}"
								value="${_csrf.token}" />
						<div class="action-section">
							<button class="btn bnt-save"><spring:message code="user.profile.password.change"></spring:message></button>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>
<script src="<c:url value="/js/jquery.datetimepicker.full.min.js"/>"></script>
<script src="<c:url value="/js/profile.js"/>"></script>