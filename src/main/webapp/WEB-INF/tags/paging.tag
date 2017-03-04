<%@ tag language="java" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ attribute name="uri" type="java.lang.String" required="true"
	rtexprvalue="true" description="Uri page"%>
<%@ attribute name="currentPage" type="java.lang.Integer"
	required="true" rtexprvalue="true" description="Current page"%>
<%@ attribute name="totalPages" type="java.lang.Integer" required="true"
	rtexprvalue="true" description="Total pages"%>
<%@ attribute name="visiblePages" type="java.lang.Integer"
	rtexprvalue="true" description="Visible pages"%>
<%@ attribute name="nameLabel" type="java.lang.String"
	rtexprvalue="true" description="Name label"%>
<%@ attribute name="firstLabel" type="java.lang.String"
	rtexprvalue="true" description="First label"%>
<%@ attribute name="previousLabel" type="java.lang.String"
	rtexprvalue="true" description="Previous label"%>
<%@ attribute name="nextLabel" type="java.lang.String"
	rtexprvalue="true" description="Next label"%>
<%@ attribute name="lastLabel" type="java.lang.String"
	rtexprvalue="true" description="Last label"%>
<%@ attribute name="paginationClass" type="java.lang.String"
	rtexprvalue="true" description="Pagination class"%>
<%@ attribute name="activeClass" type="java.lang.String"
	rtexprvalue="true" description="Active class"%>
<%@ attribute name="disabledClass" type="java.lang.String"
	rtexprvalue="true" description="Disabled class"%>
<%@ attribute name="removeFirstAndLast" type="java.lang.Boolean"
	rtexprvalue="true" description="Disabled class"%>

<c:if test="${not empty totalPages and not empty currentPage and not empty uri and totalPages > 0}">
	
	<c:set var="currentPage" value="${currentPage + 1 }"></c:set>
	
	<!-- Set default values -->
	<c:set var="visiblePages"
		value="${empty visiblePages ? 5 : visiblePages}"></c:set>
	<c:set var="nameLabel" value="${empty nameLabel ? 'page' : nameLabel}"></c:set>
	<c:set var="firstLabel"
		value="${empty firstLabel ? '‹' : firstLabel}"></c:set>
	<c:set var="previousLabel"
		value="${empty previousLabel ? '«' : previousLabel}"></c:set>
	<c:set var="nextLabel" value="${empty nextLabel ? '»' : nextLabel}"></c:set>
	<c:set var="lastLabel" value="${empty lastLabel ? '›' : lastLabel}"></c:set>
	<c:set var="paginationClass"
		value="${empty paginationClass ? 'paging' : paginationClass}"></c:set>
	<c:set var="activeClass"
		value="${empty activeClass ? 'active' : activeClass}"></c:set>
	<c:set var="disabledClass"
		value="${empty disabledClass ? 'disabled' : disabledClass}"></c:set>
	
	<!-- Write URL -->
	<c:set var="queryString" value="${pageContext.request.queryString}"></c:set>
	<c:set var="left_url" value="${uri}/${nameLabel}/"></c:set>
	<c:set var="right_url" value=""></c:set>
	<c:if test="${queryString != null}">
		<c:set var="right_url" value="/?${queryString}"></c:set>
	</c:if>
	
	<!-- Draw -->
	<ul class="${paginationClass}">
	
		<!-- First page -->
		<c:if test="${empty removeFirstAndLast || !removeFirstAndLast}">
			<c:choose>
				<c:when test="${currentPage eq 1}">
					<li class="${disabledClass}"><a href="javascript:void(0)">${fn:escapeXml(firstLabel)}</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="${left_url }1${right_url }"
						title="${fn:escapeXml(firstLabel)}">${fn:escapeXml(firstLabel)}</a></li>
				</c:otherwise>
			</c:choose>
		</c:if>
	
		<!-- Previous page -->
		<c:choose>
			<c:when test="${currentPage gt 1}">
				<li><a href="${left_url }${currentPage - 1}${right_url }"
					title="${fn:escapeXml(previousLabel)}">${fn:escapeXml(previousLabel)}</a></li>
			</c:when>
			<c:otherwise>
				<li class="${disabledClass}"><a href="javascript:void(0)"
					title="${fn:escapeXml(previousLabel)}">${fn:escapeXml(previousLabel)}</a></li>
			</c:otherwise>
		</c:choose>
	
		<!-- All page -->
		<c:if test="${totalPages < visiblePages}">
			<c:set var="visiblePages" value="${totalPages}"></c:set>
		</c:if>
		<c:set var="half" value="${visiblePages/2 - ((visiblePages/2) % 1)}"></c:set>
		<c:set var="start" value="${currentPage - half + 1 - visiblePages % 2}"></c:set>
		<c:set var="end" value="${currentPage+half}"></c:set>
		<c:if test="${start <= 0}">
			<c:set var="start" value="1"></c:set>
			<c:set var="end" value="${visiblePages}"></c:set>
		</c:if>
		<c:if test="${end > totalPages}">
			<c:set var="start" value="${totalPages - visiblePages+1}"></c:set>
			<c:set var="end" value="${totalPages}"></c:set>
		</c:if>
		<c:forEach begin="${start}" end="${end}" step="1" var="p">
			<c:choose>
				<c:when test="${p eq currentPage}">
					<li class="${activeClass}"><a href="javascript:void(0)"
						title="${p}">${p}</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="${left_url }${p}${right_url }" title="${p}">${p}</a></li>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	
		<!-- Next page -->
		<c:choose>
			<c:when test="${currentPage lt totalPages}">
				<li><a href="${left_url }${currentPage + 1}${right_url }"
					title="${fn:escapeXml(nextLabel)}">${fn:escapeXml(nextLabel)}</a></li>
			</c:when>
			<c:otherwise>
				<li class="${disabledClass}"><a href="javascript:void(0)">${fn:escapeXml(nextLabel)}</a></li>
			</c:otherwise>
		</c:choose>
	
		<!-- Last page -->
		<c:if test="${empty removeFirstAndLast || !removeFirstAndLast}">
			<c:choose>
				<c:when test="${currentPage lt totalPages}">
					<li><a href="${left_url }${totalPages}${right_url }"
						title="${fn:escapeXml(lastLabel)}">${fn:escapeXml(lastLabel)}</a></li>
				</c:when>
				<c:otherwise>
					<li class="${disabledClass}"><a href="javascript:void(0)"
						title="${fn:escapeXml(lastLabel)}">${fn:escapeXml(lastLabel)}</a></li>
				</c:otherwise>
			</c:choose>
		</c:if>
	</ul>
	
</c:if>