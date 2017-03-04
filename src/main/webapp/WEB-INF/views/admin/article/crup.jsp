<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<spring:message code="admin.article.crup.placeholder.title.en" var="enTitlePlaceholer"></spring:message>
<spring:message code="admin.article.crup.placeholder.title.vi" var="viTitlePlaceholer"></spring:message>
<spring:message code="admin.article.crup.placeholder.image.small" var="smImgPlaceholer"></spring:message>
<spring:message code="admin.article.crup.placeholder.image.large" var="lgImgPlaceholer"></spring:message>
<div class="page-title">
	<div class="title-left">
		<h3><spring:message code="admin.article.crup.title"></spring:message></h3>
	</div>
</div>
<div class="content-main">
	<form:form method="post"
		action="${pageContext.request.contextPath}${applicationScope.adminUrl }/articles/save"
		modelAttribute="article" id="crup-form">
		<fieldset class="form-group">
			<label for="root-cate"><spring:message code="admin.article.crup.cate.root"></spring:message></label>
			<form:select path="rootCate" class="form-control" id="root-cate" required="required">
				<c:forEach var="category" items="${categories }">
					<c:if test="${category.parent == 0 }">
						<option value="${category.id }">${category.nameEn }</option>
					</c:if>
				</c:forEach>
			</form:select>
			<form:errors path="rootCate" cssClass="error" />
		</fieldset>
		<fieldset class="form-group">
			<label for="sub-cate"><spring:message code="admin.article.crup.cate.sub"></spring:message></label>
			<form:select path="subCate" multiple="true" class="form-control"
				id="sub-cate" required="required">
				<c:forEach var="category" items="${categories }">
					<c:if test="${category.parent != 0 }">
						<option value="${category.id }" data-id="${category.parent }">${category.nameEn }</option>
					</c:if>
				</c:forEach>
				<option id="none-cate"><spring:message code="admin.article.crup.cate.sub.none"></spring:message></option>
			</form:select>
			<form:errors path="subCate" cssClass="error" />
		</fieldset>
		<fieldset class="form-group">
			<label for="title-en"><spring:message code="admin.article.crup.title.en"></spring:message></label>
			<form:input path="titleEn" class="form-control" id="title-en"
				placeholder="${enTitlePlaceholer }" required="required" />
			<form:errors path="titleEn" cssClass="error" />
		</fieldset>
		<fieldset class="form-group">
			<label for="title-vi"><spring:message code="admin.article.crup.title.vi"></spring:message></label>
			<form:input path="titleVi" class="form-control" id="title-vi"
				placeholder="${viTitlePlaceholer }" required="required" />
			<form:errors path="titleVi" cssClass="error" />
		</fieldset>
		<fieldset class="form-group">
			<label for="small-image"><spring:message code="admin.article.crup.image.small"></spring:message></label>
			<form:input path="smallImage" class="form-control" id="small-image"
				placeholder="${smImgPlaceholer }" required="required" />
			<form:errors path="smallImage" cssClass="error" />
			<img src="#" id="sm-img" class="form-image hide">
		</fieldset>
		<fieldset class="form-group">
			<label for="large-image"><spring:message code="admin.article.crup.image.large"></spring:message></label>
			<form:input path="largeImage" class="form-control" id="large-image"
				placeholder="${lgImgPlaceholer }" required="required" />
			<form:errors path="largeImage" cssClass="error" />
			<img src="#" id="lg-img" class="form-image hide">
		</fieldset>
		<fieldset class="form-group">
			<label for="description-en"><spring:message code="admin.article.crup.description.en"></spring:message></label>
			<form:textarea path="descriptionEn" id="description-en"></form:textarea>
			<form:errors path="descriptionEn" cssClass="error" />
		</fieldset>
		<fieldset class="form-group">
			<label for="description-en"><spring:message code="admin.article.crup.description.vi"></spring:message></label>
			<form:textarea path="descriptionVi" id="description-vi"></form:textarea>
			<form:errors path="descriptionVi" cssClass="error" />
		</fieldset>
		<c:if test="${not empty article.id }">
			<form:hidden path="id" />
		</c:if>
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />
		<div class="page-action">
			<button type="submit" class="action-btn btn-info btn-right" id="btn-preview" formtarget="_blank"><spring:message code="admin.article.crup.preview"></spring:message></button>
			<button type="submit" class="action-btn btn-primary btn-right" id="btn-save"><spring:message code="admin.article.crup.submit"></spring:message></button>
		</div>
	</form:form>
</div>

<!-- CKEditor -->
<script src="<c:url value="/ckeditor/ckeditor.js"/>"></script>
<script>
	CKEDITOR.replace('description-en');
	CKEDITOR.replace('description-vi');
</script>

<script>
	function getCkEditorLength(name) {
		return CKEDITOR.instances[name].getData().replace(/<[^>]*>/gi, '').length;
	}
	
	function validateCkEditor(){
		var descriptionEnLength = getCkEditorLength('description-en'); 
		var descriptionViLength = getCkEditorLength('description-vi');
		var message;
		if(!descriptionViLength) {
        	message = '<spring:message code="admin.article.crup.validation.description.vi"></spring:message>';
        }
        if(!descriptionEnLength) {
        	message = '<spring:message code="admin.article.crup.validation.description.en"></spring:message>';
        }
        if(message){
        	modalMessage.find('h3').html(message);
			modalMessage.css('display', 'block');
		    return false;
        }
        return true;
	}
	
	$('#btn-save').click(function(){
		return validateCkEditor();
	});
	
	$('#btn-preview').click(function(){
		if(!validateCkEditor()){
			return false;
		}
		var input = $("<input>").attr("type", "hidden").attr("name", "preview").val(true);
		var crupForm = $('#crup-form');
		crupForm.attr('action', '${pageContext.request.contextPath}${applicationScope.adminUrl }/articles/preview');
		crupForm.append(input);
		return true;
	});
</script>

<script>
	// change sub cate with root cate
	$('#root-cate').change(function() {
		var selectedCate = $(this).children('option:selected');
		handleSelect(selectedCate.val(), selectedCate.text());
	});
	function handleSelect(id, text) {
		var count = 0;
		$('#sub-cate > option:selected').removeAttr('selected');
		$('#sub-cate > option').each(function() {
			if ($(this).attr('data-id') != id) {
				$(this).hide();
			} else {
				$(this).show();
				count++;
			}
		});
		$('#sub-cate').attr('size', count);
		if (count === 0) {
			var noneCate = $('#none-cate');
			noneCate.val(id);
			noneCate.text(text);
			noneCate.show();
		}
	}
	// Show image
	var smallImage = $('#small-image');
	var largeImage = $('#large-image');
	function showSmallImage() {
		var smImg = $('#sm-img');
		smImg.attr('src', smallImage.val());
		smImg.show();
	}
	function showLargeImage() {
		var lgImg = $('#lg-img');
		lgImg.attr('src', largeImage.val());
		lgImg.show();
	}
	if (smallImage.val() != null) {
		showSmallImage();
	}
	smallImage.blur(function() {
		showSmallImage();
	});
	if (largeImage.val() != null) {
		showLargeImage();
	}
	largeImage.blur(function() {
		showLargeImage();
	});
</script>

<c:choose>
	<c:when test="${empty article.id }">
		<script>
			var firstRootCate = $('#root-cate option:first');
			handleSelect(firstRootCate.val(), firstRootCate.text());
		</script>
	</c:when>
	<c:otherwise>
		<script>
			var selectedRootCate;
			var currentRoot = '${article.rootCate}';
			$('#root-cate > option').each(function() {
				if ($(this).text() === currentRoot) {
					$(this).attr('selected', 'selected');
					selectedRootCate = $(this);
				}
			});
			handleSelect(selectedRootCate.val(), selectedRootCate.text());
			var currentSub = '${article.subCate}';
			var arraySub = JSON.parse(currentSub);
			$('#sub-cate > option').each(function() {
				for (var i = 0; i < arraySub.length; i++)
					if ($(this).text() === arraySub[i] && $(this).attr('data-id') === selectedRootCate.val())
						$(this).attr('selected', 'selected');
			});
		</script>
	</c:otherwise>
</c:choose>