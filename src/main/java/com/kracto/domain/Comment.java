package com.kracto.domain;

import java.util.Date;

import org.springframework.data.domain.Persistable;

public class Comment implements Persistable<Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2976857417417342873L;
	private Long id;
	private Long articleId;
	private String username;
	private String content;
	private String profilePictureUrl;
	private Boolean createdByAdmin;
	private String liker;
	private Date created;
	private Date updated;
	private Long parentId;
	private Boolean state;

	private transient boolean persisted;

	@Override
	public boolean isNew() {
		return !persisted;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setPersisted(boolean persisted) {
		this.persisted = persisted;
	}

	public Comment() {
	}

	public Comment(Long id, Long articleId, String username, String content, String profilePictureUrl,
			Boolean createdByAdmin, String liker, Date created, Date updated, Long parentId,
			Boolean state) {
		super();
		this.id = id;
		this.articleId = articleId;
		this.username = username;
		this.content = content;
		this.profilePictureUrl = profilePictureUrl;
		this.createdByAdmin = createdByAdmin;
		this.liker = liker;
		this.created = created;
		this.updated = updated;
		this.parentId = parentId;
		this.state = state;
	}

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getProfilePictureUrl() {
		return profilePictureUrl;
	}

	public void setProfilePictureUrl(String profilePictureUrl) {
		this.profilePictureUrl = profilePictureUrl;
	}

	public Boolean getCreatedByAdmin() {
		return createdByAdmin;
	}

	public void setCreatedByAdmin(Boolean createdByAdmin) {
		this.createdByAdmin = createdByAdmin;
	}

	public String getLiker() {
		return liker;
	}

	public void setLiker(String liker) {
		this.liker = liker;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

	public boolean isPersisted() {
		return persisted;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
