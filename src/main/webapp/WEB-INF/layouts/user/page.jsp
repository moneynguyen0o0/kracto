<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="x-ua-compatible" content="ie=edge">

<tiles:importAttribute name="title" ignore="true"></tiles:importAttribute>
<title>
	<c:choose>
		<c:when test="${not empty title}">
			<spring:message code="${title}"></spring:message>	
		</c:when>
		<c:when test="${not empty dynamicTitle}">
			${dynamicTitle }	
		</c:when>
		<c:otherwise>
			<spring:message code="tilte.home"></spring:message>
		</c:otherwise>
	</c:choose>
</title>

<meta name="description" content="">
<meta name="keywords" content="" />
<meta name="author" content="$money$">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="shortcut icon" href="<c:url value="/images/favicon.ico"/>" />

<link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet"
	type="text/css" />
<link href="<c:url value="/css/font-awesome.min.css"/>" rel="stylesheet"
	type="text/css" />

<!-- Custom Theme Style -->
<link href="<c:url value="/css/style.css"/>" rel="stylesheet"
	type="text/css" />

<!-- jQuery -->
<script src="<c:url value="/js/jquery.min.js"/>"></script>

</head>
<body>

	<tiles:insertAttribute name="header" />
	<main id="content">
		<div class="container">
			<tiles:insertAttribute name="body" />
		</div>
	</main>
	<tiles:insertAttribute name="footer" />

	<script src="<c:url value="/js/megamenu.js"/>"></script>

</body>
</html>