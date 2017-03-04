<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<spring:message code="user.changepassword.placeholder.new" var="newPasswordPlaceholder"></spring:message>
<spring:message code="user.changepassword.placeholder.confirm" var="confirmPasswordPlaceholder"></spring:message>
<div class="content-wrap">
	<div class="content-padding">
		<div id="password-change">
			<h1 class="content-title"><spring:message code="user.signup.title"></spring:message></h1>
			<div class="password-change-section">
				<form:form method="post"
					action="${pageContext.request.contextPath }/change-password/${token }"
					modelAttribute="password">
					<div class="password-change-top">
						<h3><spring:message code="user.changepassword.title"></spring:message></h3>
						<form:password path="newPassword" class="form-control"
							placeholder="${newPasswordPlaceholder }" required="required" />
						<form:errors path="newPassword" cssClass="error" />
						<form:password path="confirmPassword" class="form-control"
							placeholder="${confirmPasswordPlaceholder }" required="required" />
						<form:errors path="confirmPassword" cssClass="error" />
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />
					</div>
					<div class="password-change-bottom">
						<input type="submit" class="btn" value="<spring:message code="user.changepassword.submit"></spring:message>">
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>