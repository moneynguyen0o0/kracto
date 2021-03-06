<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page session="false"%>

<c:if test="${adminUrl == null }">
	<c:set var="adminUrl" value="<%= com.kracto.web.constant.URL.ADMIN %>" scope="application"/>
</c:if>

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

<link
	href='https://fonts.googleapis.com/css?family=Open+Sans:300,400,700'
	rel='stylesheet' type='text/css'>
	
<link href="<c:url value="/css/font-awesome.min.css"/>" rel="stylesheet"
type="text/css" />

<link href="<c:url value="/css/reset.css"/>" rel="stylesheet">
<link href="<c:url value="/css/form.css"/>" rel="stylesheet">
<link href="<c:url value="/css/stacktable.css"/>" rel="stylesheet">
<!-- Custom Theme Style -->
<link href="<c:url value="/css/custom.css"/>" rel="stylesheet">

<!-- jQuery -->
<script src="<c:url value="/js/jquery.min.js"/>"></script>

</head>
<body>
	<tiles:insertAttribute name="header" />
	<main class="cd-main-content"> 
		<tiles:insertAttribute name="sidebar" />
		<!-- The Message Modal -->
		<div id="modal-message" class="modal">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-header">
					<span class="close cancel">×</span>
					<h2><spring:message code="admin.page.model.title"></spring:message></h2>
				</div>
				<div class="modal-body">
					<h3></h3>
				</div>
				<div class="modal-footer">
					<button class="action-btn btn-primary cancel"><spring:message code="admin.page.model.close"></spring:message></button>
				</div>
			</div>
		</div>
		<script>
			// Modal message diablog
			var modalMessage = $('#modal-message');
			$('.cancel').click(function() {
				modalMessage.css('display', 'none');
			});
		</script>
		<div class="content-wrapper">
			<tiles:insertAttribute name="body" />
		</div>
	</main>

	<script src="<c:url value="/js/jquery.menu-aim.js"/>"></script>
	<!-- Custom Theme Scripts -->
	<script src="<c:url value="/js/custom.js"/>"></script>
</body>
</html>