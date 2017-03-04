<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script>
	var link = document.createElement('link');
	link.href = '${pageContext.request.contextPath }/css/megamenu.css';
	link.type = 'text/css';
	link.rel = 'stylesheet';
	link.media = 'all';
	document.getElementsByTagName('head')[0].appendChild(link);
</script>
<div class="page-title">
	<div class="title-left">
		<h3><spring:message code="admin.menu.title"></spring:message></h3>
	</div>
	<div class="title-right">
		<a
			href="<c:url value="${applicationScope.adminUrl }/setting/menu?lang=en" />"
			class="action-btn ${lang eq 'en' ? 'btn-info' : 'btn-default'}"><spring:message code="admin.menu.lang.en"></spring:message></a>
		<a
			href="<c:url value="${applicationScope.adminUrl }/setting/menu?lang=vi" />"
			class="action-btn ${lang eq 'vi' ? 'btn-info' : 'btn-default'}"><spring:message code="admin.menu.lang.vi"></spring:message></a>
	</div>
</div>
<div class="content-main" style="padding: 10px">
	<div class="page-input clearfix">
		<div class="input-left">
			<input type="text" class="form-control" id="link"
				placeholder="<spring:message code="admin.menu.input.key"></spring:message>" required="required">
		</div>
		<div class="input-right">
			<input type="text" class="form-control" id="value"
				placeholder="<spring:message code="admin.menu.input.value"></spring:message>" required="required">
		</div>
	</div>
	<div id="menu">${menu }</div>
	<div class="page-action">
		<a class="action-btn btn-primary btn-right" id="btn-save-menu"><spring:message code="admin.menu.input.submit"></spring:message></a>
	</div>
</div>
<form action="${pageContext.request.contextPath}${applicationScope.adminUrl }/setting/menu"
	method="post" id="form-menu">
	<input type="hidden" name="menu-user" id="menu-user"> <input
		type="hidden" name="menu-admin" id="menu-admin"> <input
		type="hidden" name="lang" value="${lang }"> <input
		type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>
<script src="<c:url value="/js/jquery-ui.min.js"/>"></script>
<script>
	// sortable
	function sortMenu() {
		$('.menu > ul').sortable({
			items : '> li:not(:last)'
		});
		$('.vertical-menu').sortable({
			cancel : '.blocking'
		});
		$('.menu > ul > li > ul > li > ul').sortable({
			connectWith : '.menu > ul > li > ul > li > ul',
			beforeStop : function(ev, ui) {
				if ($(ui.item).hasClass('blocking')) {
					$(this).sortable('cancel');
				}
			}
		});
	}
	// run sort menu at first
	sortMenu();
	// view level-1 menu
	function hideSubMenu() {
		$('.menu > ul > li > ul').hide();
	}
	$(document).click(function() {
		if (this.id != 'menu')
			hideSubMenu();
	});
	$(document).on('click', '.menu > ul > li', function(e) {
		var child = $(this).children('ul');
		if (!child.is(':visible')) {
			hideSubMenu();
			child.fadeToggle(150);
		}
		// e.preventDefault();
		return false;
	});
	// add class normal-sub
	function appendSub() {
		$('.menu > ul > li > ul:not(:has(ul))').addClass('normal-sub');
	}
	// check if string empty or whitespace
	function isNullOrWhitespace(input) {
		if (input == null)
			return true;
		return input.replace(/\s/gi, '').length < 1;
	}
	function checkLength(input, length) {
		if (input.length > length)
			return true;
		else
			return false;
	}
	// prepare menu
	var btnAdd = '<li class="blocking"><button class="add-menu">+</button></li>';
	var btnRemove = '<button class="remove-menu">-</button>';
	function getValueMenu() {
		var value = $('#value').val();
		if (!isNullOrWhitespace(value) && !checkLength(value, 10)) {
			var link = '${pageContext.request.contextPath }/'
					+ $('#link').val().trim().replace(/\s\s+/g, ' ');
			return '<a href="' + link + '">' + value.trim() + '</a>';
		} else {
			modalMessage.find('h3').html(
					'<spring:message code="admin.menu.input.validation"></spring:message>');
			modalMessage.css('display', 'block');
			return null;
		}
	}
	// add menu root
	$('.add-root-menu').click(
			function() {
				var li = getValueMenu();
				if (li != null) {
					li = '<li>' + li + btnRemove
							+ '<ul class="vertical-menu level-1">' + btnAdd
							+ '</ul>' + '</li>';
					$(this).closest('ul').find('li').last().before(li);
					appendSub();
				}
			});
	// add menu child
	$(document).on('click', '.add-menu', function() {
		var thisObj = $(this);
		var li = getValueMenu();
		if (li != null) {
			var li = '<li>' + li + btnRemove + '</li>';
			if (thisObj.parents('.level-1').find('li').length === 8) {
				var lis = '';
				var parent = thisObj.parents('.level-1');
				for (var i = 0; i < parent.find('li').length; i++) {
					if (i != 0 && i % 2 == 0)
						lis += btnAdd + '</ul></li>';
					if (i === 0 || i % 2 == 0)
						lis += '<li><ul class="level-2">';
					if (i === parent.find('li').length - 1)
						break;
					lis += parent.find('li')[i].outerHTML;
				}
				lis += li + btnAdd + '</ul></li>';
				parent.removeClass('vertical-menu normal-sub');
				parent.html(lis);
			} else {
				thisObj.closest('ul').find('li').last().before(li);
				if (!thisObj.parents('.level-2').length)
					appendSub();
			}
		}
		sortMenu();
	});
	// Remove menu
	$(document).on('click', '.remove-menu', function() {
		var thisObj = $(this);
		var parent = thisObj.parents('.level-1');
		thisObj.closest('li').remove();
		if ($('.level-2').length) {
			var el = parent.children().children().children();
			if (el.length < 12) {
				var lis = '';
				for (var i = 0; i < el.length; i++) {
					var val = el[i].outerHTML;
					if (val.indexOf('remove-menu') != -1)
						lis += val;
				}
				lis += btnAdd;
				parent.addClass('vertical-menu normal-sub');
				parent.html(lis);
			}
		}
		sortMenu();
	});
	// save menu
	$('#btn-save-menu').click(function() {
		hideSubMenu();
		$('#menu-admin').val($('#menu').html());
		$('.add-root-menu').parent().remove();
		$('.add-menu').parent().remove();
		$('.level-1').not(':has(li)').remove();
		$('.remove-menu').remove();
		$('.ui-sortable-handle').removeClass('ui-sortable-handle');
		$('.ui-sortable').removeClass('ui-sortable');
		$('*[class=""]').removeAttr('class');
		$('#menu-user').val($('#menu').html());
		$('#form-menu').submit();
	});
</script>