<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="content-wrap">
	<div class="content-padding">
		<div id="password-reset">
			<h1 class="content-title"><spring:message code="user.forgotpassword.title"></spring:message></h1>
			<div class="password-reset-section">
			<form action="${pageContext.request.contextPath}/reset-password"
						method="post">
					<div class="password-reset-top">
						<h3><spring:message code="user.resetpassword.title"></spring:message></h3>
							<input type="email" class="form-control" placeholder="<spring:message code="user.resetpassword.placeholder.email"></spring:message>" name="email" required="required">
							<input type="hidden"
								name="${_csrf.parameterName}" value="${_csrf.token}" />
						<c:if test="${isNotFound}">
							<span class="error-block">
								<spring:message code="user.resetpassword.email.not"></spring:message>
							</span>
						</c:if>
					</div>
					<div class="password-reset-bottom">
						<input type="submit" class="btn" value="<spring:message code="user.resetpassword.submit"></spring:message>">
					</div>
				</form>
			</div>
		</div>
	</div>
</div>