<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="content-wrap">
	<div class="content-padding">
		<div id="login">
			<h1 class="content-title"><spring:message code="user.signin.title"></spring:message></h1>
			<form action="${pageContext.request.contextPath}/signin"
				method="post">
				<div class="login-section">
					<div class="login-middle">
						<h3><spring:message code="user.signin.subtitle"></spring:message></h3>
						<c:if test="${isValid}">
							<span class="error-block">
								<spring:message code="user.signin.invalid"></spring:message>
							</span>
						</c:if>
						<input type="text" placeholder="<spring:message code="user.signin.placeholder.username"></spring:message>" name="username">
						<input type="password" placeholder="<spring:message code="user.signin.placeholder.password"></spring:message>" name="password">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />
						<div class="login-extra">
							<div class="remember-me">
								<input type="checkbox" name="remember-me" checked="checked"/> <spring:message code="user.signin.rememberme"></spring:message>
							</div>
							<div class="forget-password">
								<a href="<c:url value="/reset-password"/>"><spring:message code="user.signin.forget"></spring:message></a>
							</div>
						</div>

					</div>
					<div class="login-bottom">
						<div class="login-left">
							<p><spring:message code="user.signin.signup.question"></spring:message></p>
							<a href="<c:url value="/signup"/>"><spring:message code="user.signin.signup"></spring:message></a>
						</div>
						<div class="login-right">
							<input type="submit" class="btn" value="<spring:message code="user.signin.submit"></spring:message>">
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>