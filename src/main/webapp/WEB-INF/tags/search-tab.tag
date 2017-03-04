<%@ tag language="java" pageEncoding="utf-8"%>
<%@ attribute name="text" type="java.lang.String" required="true"
	rtexprvalue="true" description="Text"%>
<%@ attribute name="link" type="java.lang.String" required="true"
	rtexprvalue="true" description="Link"%>
<a
	href="${pageContext.request.contextPath }${url }?type=${link }&keyword=${keyword }"><li
	class="${type eq link ? 'resp-tab-active' : '' }">${text }</li></a>