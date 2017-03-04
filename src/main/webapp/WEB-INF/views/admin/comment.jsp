<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<div class="page-title">
	<div class="title-left">
		<h3><spring:message code="admin.comment.title"></spring:message></h3>
	</div>
</div>
<div class="content-main">
	<c:choose>
		<c:when test="${fn:length(comments.content) gt 0}">
			<spring:message code="admin.comment.table.article_id" var="articleId"></spring:message>
			<spring:message code="admin.comment.table.non_view" var="nonView"></spring:message>
			<spring:message code="admin.comment.table.total" var="total"></spring:message>
			<table class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th><tags:table-header content="${articleId }"
								field="article_id" /></th>
						<th><tags:table-header content="${nonView }" field="non_view" /></th>
						<th><tags:table-header content="${total }" field="total" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${comments.content }" var="comment">
						<tr>
							<td><a href="${pageContext.request.contextPath }${applicationScope.adminUrl }/articles/${comment.articleId }">${comment.articleId }</a></td>
							<td>${comment.nonView }</td>
							<td>${comment.total }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:when>
		<c:otherwise>
			<h3 class="item-not-found"><spring:message code="admin.comment.empty"></spring:message></h3>
		</c:otherwise>
	</c:choose>
	<div class="page-action">
		<tags:paging uri="${pageContext.request.contextPath }${url }"
			currentPage="${comments.number }"
			totalPages="${comments.totalPages }" />
	</div>

</div>