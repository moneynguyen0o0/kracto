<%@ tag language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="field" type="java.lang.String" required="true"
	rtexprvalue="true" description="Field"%>
<%@ attribute name="content" type="java.lang.String" required="true"
	rtexprvalue="true" description="Content"%>
<a
	href="${pageContext.request.contextPath }${url }?sort=${field }&type=${type }">${content }<c:if
		test="${sort eq field }">
		<i>${type eq 'asc' ? ' &#9660;' : ' &#9650;' }</i>
	</c:if>
</a>