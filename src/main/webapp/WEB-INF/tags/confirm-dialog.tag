<%@ tag language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!-- The Confirm Modal -->
<div id="modal-confirm" class="modal">
	<!-- Modal content -->
	<div class="modal-content">
		<div class="modal-header">
			<span class="close cancel">×</span>
			<h2><spring:message code="tags.dialog.confirm.title"></spring:message></h2>
		</div>
		<div class="modal-body">
			<h3><spring:message code="tags.dialog.confirm.content"></spring:message></h3>
		</div>
		<div class="modal-footer">
			<button class="action-btn btn-default cancel"><spring:message code="tags.dialog.confirm.cancel"></spring:message></button>
			<button class="action-btn btn-danger" id="accept"><spring:message code="tags.dialog.confirm.delete"></spring:message></button>
		</div>
	</div>
</div>
<script>
	var modalConfirm = $('#modal-confirm');
	$('.btn-remove').click(function() {
		$('#accept').attr('data-href', $(this).attr('href'));
		modalConfirm.css('display', 'block');
		return false;
	});
	$('.cancel').click(function() {
		modalConfirm.css('display', 'none');
	});
	$('#accept').click(function() {
		window.location.href = $(this).attr('data-href');
	});
</script>
<c:if test="${not empty message }">
	<script>
		modalMessage.find('h3').html('${message}');
		modalMessage.css('display', 'block');
	</script>
</c:if>