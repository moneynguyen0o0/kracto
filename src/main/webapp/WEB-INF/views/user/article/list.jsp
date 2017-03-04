<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<div class="content-wrap">
	<div class="content-padding clearfix">
		<div id="list">
			<tags:articles articles="${articles.content }" />
		</div>
	</div>
	<div class="content-footer">
		<tags:paging uri="${pageContext.request.contextPath }${url }"
			currentPage="${articles.number }"
			totalPages="${articles.totalPages }" />
	</div>
</div>