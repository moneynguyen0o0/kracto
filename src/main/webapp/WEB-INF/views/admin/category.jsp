<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<div class="page-title">
	<div class="title-left">
		<h3><spring:message code="admin.category.title"></spring:message></h3>
	</div>
	<div class="title-right">
		<form action="${pageContext.request.contextPath}${applicationScope.adminUrl }/categories"
			method="get">
			<select class="form-control" onchange="this.form.submit()"
				name="rootId">
				<option value="0"><spring:message code="admin.category.filter.root"></spring:message></option>
				<c:forEach items="${categories }" var="category">
					<c:if test="${category.parent == 0 }">
						<option value="${category.id }"
							${ category.id == rootId ? 'selected="selected"' : '' }>${category.nameEn }</option>
					</c:if>
				</c:forEach>
			</select>
		</form>
		<a class="action-btn btn-primary btn-add">+</a>
	</div>
</div>
<div class="content-main">
	<c:choose>
		<c:when test="${fn:length(categories) gt 0}">
			<table class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th><spring:message code="admin.category.table.code"></spring:message></th>
						<th><spring:message code="admin.category.table.name.en"></spring:message></th>
						<th><spring:message code="admin.category.table.name.vi"></spring:message></th>
						<th></th>
					</tr>
				</thead>
				<tbody>

					<c:choose>
						<c:when test="${rootId == 0 }">
							<c:forEach items="${categories }" var="category">
								<tr>
									<td class="id">${category.id }</td>
									<td class="name-en">${category.nameEn }</td>
									<td class="name-vi">${category.nameVi }</td>
									<td><a class="btn-edit"></a><a
										href="${pageContext.request.contextPath }${applicationScope.adminUrl }/categories/delete?id=${category.id }&rootId=${rootId }"
										class="btn-remove"></a></td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<c:forEach items="${categories }" var="category">
								<c:if test="${category.parent != 0 }">
									<tr>
										<td class="id">${category.id }</td>
										<td class="name-en">${category.nameEn }</td>
										<td class="name-vi">${category.nameVi }</td>
										<td><a class="btn-edit"></a><a
											href="${pageContext.request.contextPath }${applicationScope.adminUrl }/categories/delete?id=${category.id }&rootId=${rootId }"
											class="btn-remove"></a></td>
									</tr>
								</c:if>
							</c:forEach>
						</c:otherwise>
					</c:choose>
			</table>
		</c:when>
		<c:otherwise>
			<h3 class="item-not-found"><spring:message code="admin.category.empty"></spring:message></h3>
		</c:otherwise>
	</c:choose>
</div>

<!-- The Modal -->
<div id="modal-add" class="modal">
	<!-- Modal content -->
	<form:form id="cate-form"
		action="${pageContext.request.contextPath}${applicationScope.adminUrl }/categories/save"
		method="post" modelAttribute="category">
		<div class="modal-content">
			<div class="modal-header">
				<span class="close cancel">×</span>
				<h2><spring:message code="admin.category.modal.add.title"></spring:message></h2>
			</div>
			
			<spring:message code="admin.category.modal.add.placeholder.id" var="idPlaceholder"></spring:message>
			<spring:message code="admin.category.modal.add.placeholder.name.en" var="enNamePlaceholder"></spring:message>
			<spring:message code="admin.category.modal.add.placeholder.name.vi" var="viNamePlaceholder"></spring:message>
			
			<div class="modal-body">
				<fieldset class="form-group" id="id-group">
					<label for="id"><spring:message code="admin.category.modal.add.id"></spring:message></label>
					<form:input path="id" type="number" class="form-control" id="id"
						placeholder="${idPlaceholder }" required="required" />
					<form:errors path="id" cssClass="error" />
				</fieldset>
				<fieldset class="form-group">
					<label for="name-en"><spring:message code="admin.category.modal.add.name.en"></spring:message></label>
					<form:input path="nameEn" class="form-control" id="name-en"
						placeholder="${enNamePlaceholder }" required="required" />
					<form:errors path="nameEn" cssClass="error" />
				</fieldset>
				<fieldset class="form-group">
					<label for="name-vi"><spring:message code="admin.category.modal.add.name.vi"></spring:message></label>
					<form:input path="nameVi" class="form-control" id="name-vi"
						placeholder="${viNamePlaceholder }" required="required" />
					<form:errors path="nameVi" cssClass="error" />
				</fieldset>
				<form:hidden path="parent" value="${rootId }" />
				<input type="hidden" name="action" id="action" /> <input
					type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			</div>
			<div class="modal-footer">
				<button class="action-btn btn-success"><spring:message code="admin.category.modal.add.submit"></spring:message></button>
			</div>
		</div>
	</form:form>
</div>
<tags:confirm-dialog />
<tags:stacktable />
<c:if test="${not empty message }">
	<script>
		modalMessage.find('h3').html('${message}');
		modalMessage.css('display', 'block');
	</script>
</c:if>

<c:if test="${not empty action }">
	<script>
		var action = '${action }';
		$('#action').val(action);
		$('#modal-add').css('display', 'block');
	</script>
</c:if>

<script>
	var modalAdd = $('#modal-add');
	$('.btn-add').click(function() {
		$('#id-group').show();
		passData(null, null, null, 'create');
	});
	$('.cancel').click(function() {
		modalAdd.css('display', 'none');
	});
	$('.btn-edit').click(
			function() {
				$('#id-group').hide();
				var tr = $(this).closest('tr');
				passData(tr.find(".id").text(), tr.find('.name-en').text(), tr
						.find('.name-vi').text(), 'update');
			});
	function passData(id, nameEn, nameVi, action) {
		$('#id').val(id);
		$('#name-en').val(nameEn);
		$('#name-vi').val(nameVi);
		$('#action').val(action);
		$('.error').empty();
		modalAdd.css('display', 'block');
	}
</script>