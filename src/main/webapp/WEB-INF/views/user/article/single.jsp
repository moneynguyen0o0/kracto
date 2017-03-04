<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<sec:csrfMetaTags />
<div class="content-wrap">
	<div class="content-padding clearfix">
		<div id="single">
			<tags:article article="${article }"></tags:article>
			<h1><spring:message code="user.single.relatepost"></spring:message></h1>
			<div id="related-posts" class="clearfix"></div>
			<h1><spring:message code="user.single.comments"></spring:message></h1>
			<div id="comments-container"></div>
		</div>
	</div>
</div>
<div id="modal-message" class="modal">
	<div class="modal-content">
		<div class="modal-header">
			<span class="close-top">×</span>
			<h2><spring:message code="user.single.login.title"></spring:message></h2>
		</div>
		<div class="modal-body">
			<h3><spring:message code="user.single.login.message"></spring:message></h3>
		</div>
		<div class="modal-footer">
			<button class="btn close-bottom"><spring:message code="user.single.login.close"></spring:message></button>
		</div>
	</div>
</div>
<script>
	var modalMessage = $('#modal-message');
	$('.close-top, .close-bottom').click(function() {
		modalMessage.css('display', 'none');
	});
</script>
<script>
	var photo = 'user-icon.png',
		currentUserIsAdmin = false,
		sortKey = 'popularity',
		imgUrl = '${pageContext.servletContext.contextPath }/images/accounts/'; // jquery-comments.js will use this.
</script>
<sec:authorize access="isAuthenticated()">
	<sec:authentication var="user" property="principal" />
	<script>
		var username = '${user.username }';
		photo = '${user.photo }';
	</script>
	<sec:authorize access="hasRole('ADMIN')">
		<script>
			currentUserIsAdmin = true;
			sortKey = 'newest';
		</script>
	</sec:authorize>
</sec:authorize>
<script type="text/javascript" src="<c:url value="/js/jquery-comments.js" />"></script>
<script>

	var csrfParameter = $("meta[name='_csrf_parameter']").attr("content"),
		csrfHeader = $("meta[name='_csrf_header']").attr("content"),
		csrfToken = $("meta[name='_csrf']").attr("content");
	var rootUrl = '${pageContext.request.contextPath }/${article.rootCate }/${article.id }';
	var url = rootUrl + '/comments';
	var comments = [];

	/** 
	*	Load related posts
	*/
	
	var relatedPosts = $('#related-posts');

	function appendLoading() {
		relatedPosts
				.html('<div id="loading"><img src="${pageContext.request.contextPath }/images/ring.svg" alt="loading..."></div>');
	}

	function loadRelatedPosts() {
		appendLoading();
		var data = {};
		data = getCsrfToken(data);
		data['subCate'] = '${article.subCate }';
		$.post({
			url : rootUrl,
			data : data,
			success : function(response) {
				relatedPosts.html(response);
			}
		});
	}
	
	/** 
	*	Load comments
	*/
	
	function loadComments(){
		// load comment css
		var link = document.createElement('link');
		link.href = '${pageContext.request.contextPath }/css/jquery-comments.css';
		link.type = 'text/css';
		link.rel = 'stylesheet';
		link.media = 'all';
		document.getElementsByTagName('head')[0].appendChild(link);
		
		//
		var log = false;
		if (typeof username !== 'undefined')
			log = true;
	
		$('#comments-container').comments({
			currentUserIsAdmin : currentUserIsAdmin,
			enableDeleting : currentUserIsAdmin,
			enableDeletingCommentWithReplies : currentUserIsAdmin,
			profilePictureURL : photo,
			youText : log ? username : 'Unknown',
			textareaRows : 2,
			defaultNavigationSortKey: sortKey,
			fieldMappings : {
				modified : 'updated',
				fullname : 'username',
				profilePictureURL : 'profilePictureUrl',
				createdByAdmin : 'createdByAdmin',
				createdByCurrentUser : 'createdByCurrentUser',
				upvoteCount : 'upvoteCount',
				userHasUpvoted : 'userHasUpvoted'
			},
			getComments : function(success, error) {
				$.ajax({
					type : 'get',
					url : url,
					success : function(data) {
						comments = data;
						success(data);
					},
					error : error
				});
			},
			postComment : function(data, success, error) {
				if (!log) {
					login();
				} else {
					data['id'] = null;
					$.ajax({
						type : 'post',
						url : url,
						headers : getCsrfHeader(),
						data : JSON.stringify(getCsrfToken(data)),
						dataType : 'json',
						success : function(data) {
							success(data);
						},
						error : error
					});
				}
			},
			putComment : function(data, success, error) {
				$.ajax({
					type : 'put',
					url : url + '/' + data.id,
					headers : getCsrfHeader(),
					data : JSON.stringify(getCsrfToken(data)),
					dataType : 'json',
					success : success(data),
					error : error
				});
			},
			deleteComment : function(data, success, error) {
				$.ajax({
					type : 'delete',
					headers : getCsrfHeader(),
					url : url + '/' + data.id,
					success : success,
					error : error
				});
			},
			upvoteComment : function(data, success, error) {
				if (!log) {
					login();
					error();
				} else {
					$.ajax({
						type : 'get',
						url : url + '/' + data.id + '/upvotes',
						success : success(data),
						error : error
					});
				}
			}
		});
	}
	
	function login() {
		modalMessage.css('display', 'block');
	}

	function getCsrfToken(data) {
		data[csrfParameter] = csrfToken;
		return data;
	}
	function getCsrfHeader() {
		var header = {
			'Accept' : 'application/json',
			'Content-Type' : 'application/json'
		};
		header[csrfHeader] = csrfToken;
		return header;
	}
	
	function updateViewer() {
		$.post({
			url : rootUrl + '/update-viewer?${_csrf.parameterName}=${_csrf.token}'
		});
	}
	
	/**
 	*	Load where 0.75 corresponds to 75%
	*/
	var processing,
		handling;
	$(document).ready(function(){
	    $(document).scroll(function(e){
	    	if(handling && processing)
	    		return false;
	        var hT = $('#comments-container').offset().top,
				hH = $('#comments-container').outerHeight(),
				wH = $(window).height(),
				wS = $(this).scrollTop();
	    	if (wS > ( hT + hH - wH )){
			 	if(currentUserIsAdmin){
			 		var ids = []; 
		    		comments.forEach(function(item){
		    			if(!item.state){
		    				ids.push(item.id);
		    			}
		    		});
		    		if(ids.length !== 0){
						$.post({
							url : url + '/update-state?${_csrf.parameterName}=${_csrf.token}',
							data : {
								ids : ids
							},
							dataType : 'json'
						});
		    		}
				}
	    		handling = true;
	    	}
	        if (processing)
	            return false;
	        if ($(window).scrollTop() >= ($(document).height() - $(window).height()) * 0.75){
	    		loadRelatedPosts();
	    		loadComments();
	    		updateViewer();
	    		processing = true;
	        }
	    });
	});
</script> 