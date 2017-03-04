<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="page-title">
	<div class="title-left">
		<h3><spring:message code="admin.imagefolder.title"></spring:message></h3>
	</div>
	<div class="title-right">
		<c:if test="${subUrl != '' }">
			<button class="action-btn btn-primary" id="btn-back">&larr;</button>
		</c:if>
		<button class="action-btn btn-primary" id="btn-add">+</button>
	</div>
</div>
<div class="content-main clearfix">
	<div
		${flag ? 'style="zoom: 0.4; -ms-zoom: 0.4; -webkit-zoom: 0.4; -moz-transform: scale(0.4, 0.4); -moz-transform-origin: left center"' : ''}>
		<c:forEach var="folderName" items="${folderNames }">
			<div class="img-wrap folders">
				<span class="close" style="bottom: 30px; right: 14px;">&times;</span>
				<img src="<c:url value="/images/cd-folder.png" />"
					alt="${folderName }" data-name="${folderName }"> <span
					class="text">${folderName }</span>
			</div>
		</c:forEach>
		<c:if test="${flag }">
			<div id="img-wrap">
			<c:forEach var="imageName" items="${imageNames }">
				<div class="img-wrap images">
					<span class="close"
						style="top: 2px; right: 2px; zoom: 3; -ms-zoom: 3; -webkit-zoom: 3; -moz-transform: scale(3, 3); -moz-transform-origin: left center;">&times;</span>
					<img
						src="${pageContext.request.contextPath }/images${subUrl}/${imageName}"
						alt="" data-name="${imageName }" class="copy">
				</div>
			</c:forEach>
			</div>
		</c:if>
	</div>
</div>
<!-- The Modal -->
<div id="modal-add" class="modal">
	<!-- Modal content -->
	<c:choose>
		<c:when test="${flag }">
			<form
				action="${pageContext.request.contextPath}${applicationScope.adminUrl }/image-folder/add-image"
				method="post" id="add-image-form">
				<div class="modal-content">
					<div class="modal-header">
						<span class="close cancel">×</span>
						<h2><spring:message code="admin.imagefolder.modal.upload.image.title"></spring:message></h2>
					</div>
					<div class="modal-body">
						<div class="action-btn btn-primary file-upload">
							<span><spring:message code="admin.imagefolder.modal.upload.image.lable"></spring:message></span> <input type="file" name=images class="upload"
								multiple="multiple" />
						</div>
						<input id="input-upload" class="form-control"
							placeholder="<spring:message code="admin.imagefolder.modal.upload.image.placeholder"></spring:message>" readonly="readonly" /> <input
							type="hidden" class="curent-folder" name="curentFolder">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />
					</div>
					<div class="modal-footer">
						<button class="action-btn btn-success" id="btn-save"><spring:message code="admin.imagefolder.modal.upload.image.submit"></spring:message></button>
					</div>
				</div>
			</form>
		</c:when>
		<c:otherwise>
			<form
				action="${pageContext.request.contextPath}${applicationScope.adminUrl }/image-folder/add-folder"
				method="post" id="add-folder-form">
				<div class="modal-content">
					<div class="modal-header">
						<span class="close cancel">×</span>
						<h2><spring:message code="admin.imagefolder.modal.upload.folder.title"></spring:message></h2>
					</div>
					<div class="modal-body">
						<fieldset class="form-group">
							<label for="input-folder"><spring:message code="admin.imagefolder.modal.upload.folder.lable"></spring:message></label> <input type="text"
								name="inputFolder" id="input-folder" class="form-control"
								placeholder="<spring:message code="admin.imagefolder.modal.upload.folder.placeholder"></spring:message>" required="required">
							<input type="hidden" class="curent-folder" name="curentFolder">
						</fieldset>
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />
					</div>
					<div class="modal-footer">
						<button class="action-btn btn-success" id="btn-save"><spring:message code="admin.imagefolder.modal.upload.folder.submit"></spring:message></button>
					</div>
				</div>
			</form>
		</c:otherwise>
	</c:choose>
</div>
<!-- The Confirm Modal -->
<div id="modal-confirm" class="modal">
	<!-- Modal content -->
	<div class="modal-content">
		<div class="modal-header">
			<span class="close cancel">×</span>
			<h2><spring:message code="admin.imagefolder.modal.delete.title"></spring:message></h2>
		</div>
		<div class="modal-body">
			<h3>${flag ? 'image' : 'folder' } <spring:message code="admin.imagefolder.modal.delete.content"></spring:message></h3>
		</div>
		<div class="modal-footer">
			<button class="action-btn btn-default cancel"><spring:message code="admin.imagefolder.modal.delete.cancel"></spring:message></button>
			<button class="action-btn btn-danger" id="accept"><spring:message code="admin.imagefolder.modal.delete.submit"></spring:message></button>
		</div>
	</div>
</div>
<c:if test="${not empty message }">
	<script>
		modalMessage.find('h3').html('${message}');
		modalMessage.css('display', 'block');
	</script>
</c:if>
<script>
	var subUrl = '${subUrl}';
</script>