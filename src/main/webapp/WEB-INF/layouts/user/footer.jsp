<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<footer id="footer">
	<div class="container">
		<div class="row footer-inner">
			<div class="copyright col-sm-8 col-xs-12">
				<p>
					<spring:message code="user.footer.copyright"></spring:message> <a href="mailto:<spring:eval expression="@email" />"><spring:message code="user.footer.author"></spring:message></a>
				</p>
			</div>
			<div class="social col-sm-4 col-xs-12">
				<ul>
					<li><a href="<spring:eval expression="@facebook" />" class="social-icon facebook"><i
							class="fa fa-facebook" aria-hidden="true"></i></a></li>
					<li><a href="<spring:eval expression="@google" />" class="social-icon google"><i
							class="fa fa-google-plus" aria-hidden="true"></i></a></li>
					<li><a href="<spring:eval expression="@twitter" />" class="social-icon twitter"><i
							class="fa fa-twitter" aria-hidden="true"></i></a></li>
				</ul>
			</div>
		</div>
	</div>
</footer>