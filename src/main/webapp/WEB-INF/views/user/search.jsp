<%@page import="com.kracto.web.constant.Keyword"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<div class="content-wrap">
	<div id="content-tab" class="content-padding clearfix">
		<ul class="resp-tabs-list">
			<spring:message code="user.search.keyword.all" var="searchAll"></spring:message>
			<c:set value="<%= Keyword.ALL %>" var="all"/>
			<tags:search-tab text="${searchAll }" link="${all }"></tags:search-tab>
			<c:choose>
				<c:when test="${pageContext.response.locale.language eq 'vi' }">
					<c:forEach var="entry" items="${categories}">
						<tags:search-tab text="${entry.value.nameVi}" link="${entry.value.nameEn}"></tags:search-tab>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<c:forEach var="entry" items="${categories}">
						<tags:search-tab text="${entry.value.nameEn}" link="${entry.value.nameEn}"></tags:search-tab>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</ul>
		<div id="list">
			<c:choose>
				<c:when test="${fn:length(articles.content) gt 0}">
					<tags:articles articles="${articles.content }" />
				</c:when>
				<c:otherwise>
					<h3 class="item-not-found"><spring:message code="user.search.noresult"></spring:message></h3>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<div class="content-footer">
		<tags:paging uri="${pageContext.request.contextPath }${url }"
			currentPage="${articles.number }"
			totalPages="${articles.totalPages }" />
	</div>
</div>