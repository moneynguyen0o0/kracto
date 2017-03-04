<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<sec:authentication var="user" property="principal" />
<header class="cd-main-header">
	<a href="#0" class="cd-logo"><img
		src="<c:url value="/images/cd-logo.svg"/>" alt="Logo"></a>

	<div class="cd-search is-hidden">
		<form id="generic-search-form">
			<input type="search" placeholder="<spring:message code="admin.header.search.placeholder"></spring:message>">
		</form>
		<script type="text/javascript">
			$('#generic-search-form').submit(function() {
				alert("This feature is not yet complete!");
				return false;
			});
		</script>
	</div>
	<!-- cd-search -->

	<a href="#0" class="cd-nav-trigger"><span><spring:message code="admin.header.menu"></spring:message></span></a>

	<nav class="cd-nav">
		<ul class="cd-top-nav">
			<li class="has-children account"><a href="#0"> <c:set
						var="photo" value="/images/cd-photo.svg" /> <c:if
						test="${not empty user.photo }">
						<c:set var="photo"
							value="/images/accounts/${user.photo }" />
					</c:if> <img
					src="<c:url value="${photo }"/>"
					alt="avatar"> ${user.username }
			</a>
				<ul>

					<li><a
						href="<c:url value="${applicationScope.adminUrl }/account/profile" />"><spring:message code="admin.header.profile"></spring:message></a></li>
					<li><a
						href="<c:url value="${applicationScope.adminUrl }/account/security" />"><spring:message code="admin.header.security"></spring:message></a></li>
					<li><a
						href="<c:url value="${applicationScope.adminUrl }/account/setting" />"><spring:message code="admin.header.setting"></spring:message></a></li>
					<li>
						<a href="<c:url value="${applicationScope.adminUrl }/logout" />"><spring:message code="admin.header.logout"></spring:message></a>
					</li>
				</ul></li>
		</ul>
	</nav>
</header>