package com.kracto.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

public class UserComment {

	private Long id;
	private String username;
	@NotNull
	@NotEmpty
	@NotBlank
	private String content;
	private String profilePictureUrl;
	private Boolean createdByAdmin;
	private Integer upvoteCount;
	private Date created;
	private Date updated;
	private Long parent;
	private Boolean createdByCurrentUser;
	private Boolean userHasUpvoted;
	private Boolean state;

	public UserComment() {
	}

	public UserComment(Long id, String username, String content, String profilePictureUrl, Boolean createdByAdmin,
			Integer upvoteCount, Date created, Date updated, Long parent, Boolean createdByCurrentUser,
			Boolean userHasUpvoted, Boolean state) {
		super();
		this.id = id;
		this.username = username;
		this.content = content;
		this.profilePictureUrl = profilePictureUrl;
		this.createdByAdmin = createdByAdmin;
		this.upvoteCount = upvoteCount;
		this.created = created;
		this.updated = updated;
		this.parent = parent;
		this.createdByCurrentUser = createdByCurrentUser;
		this.userHasUpvoted = userHasUpvoted;
		this.state = state;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getUpvoteCount() {
		return upvoteCount;
	}

	public void setUpvoteCount(Integer upvoteCount) {
		this.upvoteCount = upvoteCount;
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

	public Long getParent() {
		return parent;
	}

	public void setParent(Long parent) {
		this.parent = parent;
	}

	public Boolean getCreatedByCurrentUser() {
		return createdByCurrentUser;
	}

	public void setCreatedByCurrentUser(Boolean createdByCurrentUser) {
		this.createdByCurrentUser = createdByCurrentUser;
	}

	public Boolean getUserHasUpvoted() {
		return userHasUpvoted;
	}

	public void setUserHasUpvoted(Boolean userHasUpvoted) {
		this.userHasUpvoted = userHasUpvoted;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

}