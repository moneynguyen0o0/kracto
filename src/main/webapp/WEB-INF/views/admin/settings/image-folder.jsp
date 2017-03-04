<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<sec:csrfMetaTags />
<div id="sub-view"></div>
<script>
	var csrfParameter = $("meta[name='_csrf_parameter']").attr("content");
	var csrfHeader = $("meta[name='_csrf_header']").attr("content");
	var csrfToken = $("meta[name='_csrf']").attr("content");
	// append response to sub-view function
	function appendResponse(response) {
		$("#sub-view").html(response);
	}
	// append loading to sub-view function
	function appendLoading() {
		$("#sub-view")
				.html(
						'<div id="loading"><img src="${pageContext.request.contextPath }/images/ring.svg" alt="Loading..."></div>');
	}
	// function loads image folder with ajax
	function loadImageFolder(folderName) {
		appendLoading();
		var data = {};
		data[csrfParameter] = csrfToken;
		if (folderName != null)
			data['folderName'] = folderName;
		$.post({
			url : '${pageContext.request.contextPath }${applicationScope.adminUrl }/image-folder',
			data : data,
			success : function(response) {
				appendResponse(response);
			}
		});
	}
	// go into folder
	$(document).on('click', '.folders img', function() {
		loadImageFolder(subUrl + '/' + $(this).attr('data-name'));
	});
	// back
	$(document).on('click', '#btn-back', function() {
		loadImageFolder(subUrl.substr(0, subUrl.lastIndexOf('/')));
	});
	// show and hide modal
	function getModalAdd() {
		return $('#modal-add');
	}
	$(document).on('click', '.cancel', function() {
		getModalAdd().css('display', 'none');
	});
	$(document).on('click', '#btn-add', function() {
		getModalAdd().css('display', 'block');
	});
	// set value to input upload
	$(document).on('change', '.upload', function() {
		var names = '';
		for (var i = 0; i < this.files.length; i++)
			names += this.files[i].name + ', ';
		if (names.lenght !== '')
			$('#input-upload').val(names.substring(0, names.length - 2));
	});
	// save folder
	$(document).on('submit', '#add-folder-form', function(e) {
		e.preventDefault();
		appendLoading();
		$(this).find('input[name=curentFolder]').val(subUrl);
		$.post({
			url : $(this).attr('action'),
			data : $(this).serialize(),
			success : function(response) {
				appendResponse(response);
			}
		});
	});
	// save image
	$(document).on('submit', '#add-image-form', function(e) {
		e.preventDefault();
		appendLoading();
		$(this).find('input[name=curentFolder]').val(subUrl);
		$.post({
			url : $(this).attr('action'),
			data : new FormData($(this)[0]),
			enctype : 'multipart/form-data',
			processData : false,
			contentType : false,
			success : function(response) {
				appendResponse(response);
			}
		});
	});
	// delete folder or image
	function getModalConfirm() {
		return $('#modal-confirm');
	}
	var accept = null;
	$(document).on('click', '.img-wrap .close', function() {
		accept = $(this);
		getModalConfirm().css('display', 'block');
	});
	$(document).on('click', '.cancel', function() {
		getModalConfirm().css('display', 'none');
	});
	$(document)
			.on(
					'click',
					'#accept',
					function(e) {
						appendLoading();
						e.preventDefault();
						var data = {};
						data[csrfParameter] = csrfToken;
						if (accept == null)
							return false;
						data['fileName'] = accept.closest('.img-wrap').find(
								'img').data('name');
						data['curentFolder'] = subUrl;
						$
								.post({
									url : '${pageContext.request.contextPath }${applicationScope.adminUrl }/image-folder/delete',
									data : data,
									success : function(response) {
										appendResponse(response);
										accept = null;
									}
								});
					});
	// http://stackoverflow.com/questions/22581345/click-button-copy-to-clipboard-using-jquery
	function copyToClipboard(value) {
		// Create a "hidden" input
		var aux = document.createElement("input");
		// Assign it the value of the specified element
		aux.setAttribute("value", value);
		// Append it to the body
		document.body.appendChild(aux);
		// Highlight its content
		aux.select();
		// Copy the highlighted text
		document.execCommand("copy");
		// Remove it from the body
		document.body.removeChild(aux);
	}
	$(document).on('click', '.copy', function() {
		copyToClipboard($(this).attr("src"));
		modalMessage.find('h3').html('COPIED');
		modalMessage.css('display', 'block');
		setTimeout(function() {
			modalMessage.fadeOut('fast');
		}, 500);
	});
	// load when finished
	window.onload = function() {
		loadImageFolder(null);
	};
</script>