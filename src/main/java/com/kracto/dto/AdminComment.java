package com.kracto.dto;

public class AdminComment {

	private Long articleId;
	private Integer nonView;
	private Integer total;

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	public Integer getNonView() {
		return nonView;
	}

	public void setNonView(Integer nonView) {
		this.nonView = nonView;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

}
