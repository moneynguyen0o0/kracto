<%@ tag language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="messages" type="java.util.Map" required="true"
	rtexprvalue="true" description="Messages"%>

<c:forEach items="${messages }" var="entry">
	<fieldset class="form-group">
		<label>${entry.key}</label>
		<input class="form-control" value="${entry.value }" name="${entry.key }"
			required="required" />
	</fieldset>
</c:forEach>