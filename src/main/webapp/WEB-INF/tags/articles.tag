<%@ tag language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="time" uri="/WEB-INF/tlds/time.tld"%>
<%@ attribute name="articles" type="java.util.List" required="true"
	rtexprvalue="true" description="Articles"%>

<c:choose>
	<c:when test="${pageContext.response.locale.language eq 'vi' }">
		<c:forEach items="${articles }" var="article">
			<div class="col-lg-3 col-md-4 col-sm-6 col-xs-12 item">
				<div class="item-wrap">
					<img src="${article.smallImage }" alt="${article.titleVi }"
						class="img-responsive">
					<div class="cate">
						<c:set var="rootCate" value="${categories[article.rootCate] }"/>
						<div class="trap trap-bg-${rootCate.index }">${rootCate.nameVi }</div>
					</div>
					<div class="item-content">
						<div class="item-content-wrap">
							<div class="cetaloge">
								<span class="bg-white"> ${article.viewer } <i
									class="fa fa-eye" aria-hidden="true"></i> ${article.comment }
									<i class="fa fa-comments-o" aria-hidden="true"></i></span>
							</div>
							<div class="title">
								<h3>
									<span class="bg-white">${article.titleVi }</span>
								</h3>
							</div>
							<div class="release-date">
								<span class="bg-white">
									<time:prettytime date="${article.created }" locale="${pageContext.response.locale }" pattern="dd/MM/yyyy" />
								</span>
							</div>
							<div class="more">
								<a href="<c:url value="/${article.rootCate }/${article.id }" />" class="btn"><spring:message code="tags.article.view"></spring:message></a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</c:forEach>
	</c:when>
	<c:otherwise>
		<c:forEach items="${articles }" var="article">
			<div class="col-lg-3 col-md-4 col-sm-6 col-xs-12 item">
				<div class="item-wrap">
					<img src="${article.smallImage }" alt="${article.titleEn }"
						class="img-responsive">
					<div class="cate">
						<div class="trap trap-bg-${categories[article.rootCate].index }">${article.rootCate }</div>
					</div>
					<div class="item-content">
						<div class="item-content-wrap">
							<div class="cetaloge">
								<ul class="links_bottom bg-white">
									<li><i class="fa fa-eye" aria-hidden="true"></i><span
										class="icon_text">${article.viewer }</span></li>
									<li><i class="fa fa-comments-o" aria-hidden="true"></i><span
										class="icon_text">${article.comment }</span></li>
								</ul>
							</div>
							<div class="title">
								<h3>
									<span class="bg-white">${article.titleEn }</span>
								</h3>
							</div>
							<div class="release-date">
								<span class="bg-white"><time:prettytime date="${article.created }" locale="${pageContext.response.locale }" pattern="dd/MM/yyyy" /></span>
							</div>
							<div class="more">
								<a href="<c:url value="/${article.rootCate }/${article.id }" />" class="btn"><spring:message code="tags.article.view"></spring:message></a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</c:forEach>
	</c:otherwise>
</c:choose>