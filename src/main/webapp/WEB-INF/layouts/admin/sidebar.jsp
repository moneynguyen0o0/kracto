<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<nav class="cd-side-nav">
	<ul>
		<li class="cd-label"><spring:message code="admin.sidebar.main"></spring:message></li>
		<li class="dashboard"><a
				href="<c:url value="${applicationScope.adminUrl }/dashboard" />"><spring:message code="admin.sidebar.dashboard"></spring:message></a></li>
		<li class="articles"><a
			href="<c:url value="${applicationScope.adminUrl }/articles" />"><spring:message code="admin.sidebar.articles"></spring:message></a></li>
		<li class="comments"><a
			href="<c:url value="${applicationScope.adminUrl }/comments" />"><spring:message code="admin.sidebar.comments"></spring:message>
			<c:if test="${countNewComments != 0 }">
				<span class="count count-orange">${countNewComments }</span>
			</c:if></a></li>
		<sec:authorize access="hasRole('SUPERADMIN')">
			<li class="users active"><a
				href="<c:url value="${applicationScope.adminUrl }/accounts" />"><spring:message code="admin.sidebar.accounts"></spring:message>
				<c:if test="${countNewAccounts != 0 }">	
					<span class="count count-blue" id="count-account">${countNewAccounts }</span>
				</c:if>	
				</a></li>
		</sec:authorize>
	</ul>

	<ul>
		<li class="cd-label"><spring:message code="admin.sidebar.secondary"></spring:message></li>
		<li class="images"><a
			href="<c:url value="${applicationScope.adminUrl }/image-folder" />"><spring:message code="admin.sidebar.images"></spring:message></a></li>
		<sec:authorize access="hasRole('SUPERADMIN')">
			<li class="categories"><a
				href="<c:url value="${applicationScope.adminUrl }/categories" />"><spring:message code="admin.sidebar.categories"></spring:message></a></li>
			<li class="has-children settings"><a href="javascript:void(0);"><spring:message code="admin.sidebar.settings"></spring:message></a>
				<ul>
					<li><a
						href="<c:url value="${applicationScope.adminUrl }/setting/menu" />"><spring:message code="admin.sidebar.menu"></spring:message></a></li>
					<li><a
						href="<c:url value="${applicationScope.adminUrl }/setting/banner" />"><spring:message code="admin.sidebar.banner"></spring:message></a></li>
					<li><a
						href="<c:url value="${applicationScope.adminUrl }/setting/utilpage" />"><spring:message code="admin.sidebar.utilpage"></spring:message></a></li>
					<li><a
						href="<c:url value="${applicationScope.adminUrl }/setting/config/database" />"><spring:message code="admin.sidebar.database"></spring:message></a></li>
					<li><a
						href="<c:url value="${applicationScope.adminUrl }/setting/config/email" />"><spring:message code="admin.sidebar.email"></spring:message></a></li>
					<li><a
						href="<c:url value="${applicationScope.adminUrl }/setting/config/social" />"><spring:message code="admin.sidebar.social"></spring:message></a></li>
					<li><a
						href="<c:url value="${applicationScope.adminUrl }/setting/i18n/message" />"><spring:message code="admin.sidebar.message"></spring:message></a></li>
					<li><a
						href="<c:url value="${applicationScope.adminUrl }/setting/i18n/validation" />"><spring:message code="admin.sidebar.validation"></spring:message></a></li>	
				</ul></li>
		</sec:authorize>
	</ul>
</nav>