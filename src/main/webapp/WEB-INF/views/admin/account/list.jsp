<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<sec:csrfMetaTags />
<div class="page-title">
	<div class="title-left">
		<h3><spring:message code="admin.account.list.title"></spring:message></h3>
	</div>
	<div class="title-right">
		<form
			action="${pageContext.request.contextPath}${applicationScope.adminUrl }/accounts/search"
			method="get">
			<input type="text" class="form-control" name="keyword"
				placeholder="<spring:message code="admin.account.list.placeholder.search"></spring:message>" required="required"> <input
				type="submit" value="">
		</form>
		<a href="<c:url value="${applicationScope.adminUrl }/accounts/create" />"
			class="action-btn btn-primary btn-add">+</a>
	</div>
</div>
<div class="content-main">
	<c:choose>
		<c:when test="${fn:length(accounts.content) gt 0}">
			<spring:message code="admin.account.list.table.username" var="username"></spring:message>
			<spring:message code="admin.account.list.table.email" var="email"></spring:message>
			<spring:message code="admin.account.list.table.enabled" var="enabled"></spring:message>
			<spring:message code="admin.account.list.table.created" var="created"></spring:message>
			<spring:message code="admin.account.list.table.state" var="state"></spring:message>
			<table class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th><tags:table-header content="${username }" field="username" /></th>
						<th><tags:table-header content="${email }" field="email" /></th>
						<th><tags:table-header content="${enabled }" field="enabled" /></th>
						<th><tags:table-header content="${created }" field="created" /></th>
						<th><tags:table-header content="${state }" field="state" /></th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${accounts.content }" var="account">
						<tr>
							<td>${account.username }</td>
							<td>${account.email }</td>
							<td>
								<c:choose>
									<c:when test="${account.enabled }">
										<i class="fa fa-check" aria-hidden="true"></i>
									</c:when>
									<c:otherwise>
										<i class="fa fa-times" aria-hidden="true"></i>
									</c:otherwise>
								</c:choose>
							</td>
							<td><fmt:formatDate pattern="dd/MM/yy" value="${account.created }" /></td>
							<c:choose>
								<c:when test="${account.state }">
									<td></td>
								</c:when>
								<c:otherwise>
									<td class="id-acc" data-id="${account.username }"><i class="fa fa-eye-slash" aria-hidden="true"></i></td>
								</c:otherwise>
							</c:choose>
							<td><a
								href="${pageContext.request.contextPath }${applicationScope.adminUrl }/accounts/${account.username }/profile"
								class="btn-edit"></a><a
								href="${pageContext.request.contextPath }${applicationScope.adminUrl }/accounts/delete/${account.username }"
								class="btn-remove"></a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:when>
		<c:otherwise>
			<h3 class="item-not-found"><spring:message code="admin.account.list.empty"></spring:message></h3>
		</c:otherwise>
	</c:choose>
	<div class="page-action">
		<tags:paging uri="${pageContext.request.contextPath }${url }"
			currentPage="${accounts.number }"
			totalPages="${accounts.totalPages }" />
	</div>
</div>
<tags:confirm-dialog />
<tags:stacktable />
<script>
	window.onload = function() {
		var ids = $('.id-acc').map(function() {
			return $(this).data('id');
		}).get();
		if (ids.length != 0) {
			var countAccount = $('#count-account');
			var count = '${countNewAccounts }';
			if (count > ids.length)
				countAccount.html(count - ids.length);
			else
				countAccount.html(0);
			$
					.post({
						dataType : 'json',
						url : '${pageContext.request.contextPath }${applicationScope.adminUrl }/accounts/update-state?${_csrf.parameterName}=${_csrf.token}',
						data : {
							usernames : ids
						}
					});
		}
	};
</script>