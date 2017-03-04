<%@ tag language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="time" uri="/WEB-INF/tlds/time.tld"%>
<%@ attribute name="article" type="java.lang.Object" required="true"
	rtexprvalue="true" description="Article"%>

<c:set var="isVi"
	value="${pageContext.response.locale.language eq 'vi'}" />
<c:choose>
	<c:when test="${isVi }">
		<h1>${article.titleVi }</h1>
	</c:when>
	<c:otherwise>
		<h1>${article.titleEn }</h1>
	</c:otherwise>
</c:choose>
<div class="main-sigle">
	<div class="detail-info detail-margin-bottom">
		<spring:message code="user.single.info.post"></spring:message> <a href="#author">${article.username }</a> <spring:message code="user.single.info.on"></spring:message>
		<time>
			<time:prettytime date="${article.created }" locale="${pageContext.response.locale }" pattern="dd/MM/yyyy" />
		</time>
		<spring:message code="user.single.info.in"></spring:message>
		<c:forEach var="tag" items="${tags }" varStatus="loop">
			<a
				href="${pageContext.request.contextPath }/${ rootCate}/${tag }">${tag }</a>
			<c:if test="${!loop.last}">,</c:if>
		</c:forEach>
		<ul class="links_bottom pull-right">
			<li><i class="fa fa-eye" aria-hidden="true"></i><span
				class="icon_text">${article.viewer }</span></li>
			<li><i class="fa fa-comments-o" aria-hidden="true"></i><span
				class="icon_text">${article.comment }</span></li>
		</ul>
	</div>
	<div class="addthis_inline_share_toolbox"></div>
	<img src="${article.largeImage }"
		class="detail-image detail-margin-bottom">
	<c:choose>
		<c:when test="${isVi }">
			<h1>${article.descriptionVi }</h1>
		</c:when>
		<c:otherwise>
			<h1>${article.descriptionEn }</h1>
		</c:otherwise>
	</c:choose>
</div>

<script>
	$(document).ready(function(){
	 	$('body').append('<script type="text/javascript" src="//s7.addthis.com/js/300/addthis_widget.js#pubid=<spring:eval expression="@addThisId" />"><//script>');
	});
</script>