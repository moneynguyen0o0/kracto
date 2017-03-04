<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<div class="page-title">
	<div class="title-left">
		<h3><spring:message code="admin.article.list.title"></spring:message></h3>
	</div>
	<div class="title-right">
		<form
			action="${pageContext.request.contextPath}${applicationScope.adminUrl }/articles/search"
			method="get">
			<input type="text" class="form-control" name="keyword"
				placeholder="<spring:message code="admin.article.list.search.placeholder"></spring:message>" required="required"> <input
				type="submit" value="">
		</form>
		<a href="<c:url value="${applicationScope.adminUrl }/articles/create" />"
			class="action-btn btn-primary btn-add">+</a>
	</div>
</div>
<div class="content-main">
	<c:choose>
		<c:when test="${fn:length(articles.content) gt 0}">
			<spring:message code="admin.article.list.table.id" var="id"></spring:message>
			<spring:message code="admin.article.list.table.title" var="title"></spring:message>
			<spring:message code="admin.article.list.table.category" var="category"></spring:message>
			<spring:message code="admin.article.list.table.viewer" var="viewer"></spring:message>
			<spring:message code="admin.article.list.table.comment" var="comment"></spring:message>
			<spring:message code="admin.article.list.table.created" var="created"></spring:message>
			<spring:message code="admin.article.list.table.updated" var="updated"></spring:message>
			<spring:message code="admin.article.list.table.username" var="username"></spring:message>
			<table class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th><tags:table-header content="${id }" field="id" /></th>
						<th><tags:table-header content="${title }" field="title_en" /></th>
						<th><tags:table-header content="${category }" field="root_cate" /></th>
						<th><tags:table-header content="${viewer }" field="viewer" /></th>
						<th><tags:table-header content="${comment }" field="comment" /></th>
						<th><tags:table-header content="${created }" field="created" /></th>
						<th><tags:table-header content="${updated }" field="updated" /></th>
						<th><tags:table-header content="${username }" field="Username" /></th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${articles.content }" var="article">
						<tr>
							<td>${article.id }</td>
							<td>${article.titleEn }</td>
							<td>${article.rootCate }</td>
							<td>${article.viewer }</td>
							<td>${article.comment }</td>
							<td><fmt:formatDate pattern="dd/MM/yy" value="${article.created }" /></td>
							<td><fmt:formatDate pattern="dd/MM/yy" value="${article.updated }" /></td>
							<td>${article.username }</td>
							<td><a
								href="${pageContext.request.contextPath }${applicationScope.adminUrl }/articles/edit/${article.id }"
								class="btn-edit"></a><a
								href="${pageContext.request.contextPath }${applicationScope.adminUrl }/articles/delete/${article.id }"
								class="btn-remove"></a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:when>
		<c:otherwise>
			<h3 class="item-not-found"><spring:message code="admin.article.list.empty"></spring:message></h3>
		</c:otherwise>
	</c:choose>
	<div class="page-action">
		<tags:paging uri="${pageContext.request.contextPath }${url }"
			currentPage="${articles.number }"
			totalPages="${articles.totalPages }" />
	</div>
</div>
<tags:confirm-dialog />
<tags:stacktable />