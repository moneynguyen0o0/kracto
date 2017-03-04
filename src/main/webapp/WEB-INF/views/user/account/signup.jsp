<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<spring:message code="user.signup.placeholder.username" var="usernamePlaceholder"></spring:message>
<spring:message code="user.signup.placeholder.password" var="passwordPlaceholder"></spring:message>
<spring:message code="user.signup.placeholder.email" var="emailPlaceholer"></spring:message>
<div class="content-wrap">
	<div class="content-padding">
		<div id="register">
			<h1 class="content-title"><spring:message code="user.signup.title"></spring:message></h1>
			<div class="register-section">
				<form:form method="post"
					action="${pageContext.request.contextPath}/account/save"
					modelAttribute="account">
					<div class="register-top">
						<h3><spring:message code="user.signup.subtitle"></spring:message></h3>
						<div class="register-middle">
							<form:input path="username" id="username"
								placeholder="${usernamePlaceholder }" required="required" />
							<form:errors path="username" cssClass="error" />
							<form:password path="password" id="password"
								placeholder="${passwordPlaceholder }" required="required" />
							<form:errors path="password" cssClass="error" />
							<form:input path="email" id="email"
								placeholder="${emailPlaceholer }" required="required" />
							<form:errors path="email" cssClass="error" />
						</div>
					</div>
					<div class="register-bottom">
						<div class="register-left">
							<p><spring:message code="user.signup.signin.question"></spring:message></p>
							<a href="<c:url value="/signin"/>"><spring:message code="user.signup.signin"></spring:message></a>
						</div>
						<div class="register-right">
							<input type="submit" class="btn" value="<spring:message code="user.signup.submit"></spring:message>">
						</div>
					</div>
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
				</form:form>
			</div>
		</div>
	</div>
</div>