<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page session="false"%>

<c:set var="adminUrl" value="<%= com.kracto.web.constant.URL.ADMIN %>" scope="application"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<tiles:importAttribute name="title"></tiles:importAttribute>
<title><spring:message code="${title}"></spring:message></title>

<link rel="shortcut icon" href="<c:url value="/images/favicon.ico"/>" />

<link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet">
<link href="<c:url value="/css/login.css"/>" rel="stylesheet">
</head>
<body>
	<div id="warp">
		<form action="${pageContext.request.contextPath}${adminUrl }/login"
			method="post" id="formu">
			<div class="admin">
				<div class="rota">
					<h1><spring:message code="admin.login.title.left"></spring:message></h1>
					<input id="username" type="text" name="username"
						placeholder="<spring:message code="admin.login.placeholder.username"></spring:message>" required="required" /><br /> <input
						id="password" type="password" name="password" value=""
						placeholder="<spring:message code="admin.login.placeholder.password"></spring:message>" /> <input type="hidden"
						name="${_csrf.parameterName}" value="${_csrf.token}"
						required="required" />
				</div>
			</div>
			<div class="cms">
				<div class="roti">
					<h1><spring:message code="admin.login.title.right"></spring:message></h1>
					<c:if test="${isValid}">
						<span class="error">
							<spring:message code="admin.login.invalid"></spring:message>
						</span>
					</c:if>
					<button id="valid" type="submit" name="valid"><spring:message code="admin.login.submit"></spring:message></button>
				</div>
			</div>
		</form>
	</div>
</body>
</html>