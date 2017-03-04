<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class="page-title">
	<div class="title-left">
		<h3><spring:message code="admin.account.profile.title"></spring:message></h3>
	</div>
</div>
<div class="content-main clearfix">

	<div class="text-center photo-info">
		<c:set var="photo" value="/images/cd-photo.svg" />
		<c:if test="${not empty account.photo }">
			<c:set var="photo" value="/images/accounts/${account.photo }" />
		</c:if>
		<img src="<c:url value="${photo }"/>" class="avatar img-circle"
			style="height: 200px; width: 200px" alt="avatar" id="avatar">
		<div>${account.fullname }</div>
		<div>
			<fmt:formatDate value="${account.dob }" pattern="dd/MM/yyyy" />
		</div>
		<div>${account.gender }</div>
	</div>

	<div class="personal-info">
		<h3><spring:message code="admin.account.profile.title.sub.contact"></spring:message></h3>
		<div class="content-group">
			<div class="lable-text"><spring:message code="admin.account.profile.username"></spring:message></div>
			<div class="text-content">${account.username }</div>
		</div>
		<div class="content-group">
			<div class="lable-text"><spring:message code="admin.account.profile.email"></spring:message></div>
			<div class="text-content">${account.email }</div>
		</div>
		<div class="content-group">
			<div class="lable-text"><spring:message code="admin.account.profile.role"></spring:message></div>
			<div class="text-content">${account.role }</div>
		</div>
		<hr>
		<h3><spring:message code="admin.account.profile.title.sub.addition"></spring:message></h3>
		<div class="content-group">
			<div class="lable-text"><spring:message code="admin.account.profile.date.create"></spring:message></div>
			<div class="text-content">
				<fmt:formatDate value="${account.created }" pattern="dd/MM/yyyy" />
			</div>
		</div>
		<div class="content-group">
			<div class="lable-text"><spring:message code="admin.account.profile.date.update"></spring:message></div>
			<div class="text-content">
				<fmt:formatDate value="${account.updated }" pattern="dd/MM/yyyy" />
			</div>
		</div>
	</div>
</div>