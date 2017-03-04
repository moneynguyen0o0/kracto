<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<header id="header">
	<div class="header-wrap">
		<div class="container">
			<div class="header-inner">
				<div class="row header-panel">
					<div class="header-left col-md-4 col-xs-3"><a href="<c:url value="/" />" class="logo"><spring:message code="user.header.logo"></spring:message></a></div>
					<div class="header-right col-md-8  col-xs-9">
						<form method="get" class="search"
							action="${pageContext.request.contextPath}/search">
							<button><i class="fa fa-search" aria-hidden="true"></i></button>
							<input type="search" name="keyword"
								placeholder="<spring:message code="user.header.search.placeholder"></spring:message>">
						</form>
						<sec:authorize access="isAnonymous()">
							<a href="<c:url value="/signup"/>" class="sign signup-btn">
								<span class="hidden-xs"><spring:message code="user.header.signup"></spring:message></span>
								<i class="fa fa-user-plus" aria-hidden="true"></i>
							</a>
							<a href="<c:url value="/signin"/>" class="sign signin-btn">
								<span class="hidden-xs"><spring:message code="user.header.signin"></spring:message></span>
								<i class="fa fa-user-circle" aria-hidden="true"></i>
							</a>
						</sec:authorize>
						<sec:authorize access="isAuthenticated()">
							<sec:authentication var="user" property="principal" />
							<div class="dropdown">
							  <div class="dropbtn">
							  	<c:set var="photo" value="/images/cd-photo.svg" /> 
							  	<c:if test="${not empty user.photo }">
									<c:set var="photo" value="/images/accounts/${user.photo }" />
								</c:if>
							  	<img src="<c:url value="${photo }"/>" class="img-circle" alt="avatar">
							  	<i class="fa fa-caret-down" aria-hidden="true"></i>
							  </div>
							  <div class="dropdown-content">
							    <a href="<c:url value="/account/profile"/>" ><spring:message code="user.header.profile"></spring:message></a>
							    <a href="<c:url value="/logout" />"><spring:message code="user.header.logout"></spring:message></a>
							  </div>
							</div>
						</sec:authorize>
					</div>
				</div>
				<div class="header-nav">
					<spring:message code="menu.user"></spring:message>
				</div>
			</div>
		</div>
	</div>
	<div class="header-banner">
		<div class="banner-inner">
			<div class="container">
				<h1 class="banner-title"><spring:message code="banner.title"></spring:message></h1>
				<h6 class="banner-description"><spring:message code="banner.description"></spring:message></h6>
			</div>
		</div>
	</div>
</header>