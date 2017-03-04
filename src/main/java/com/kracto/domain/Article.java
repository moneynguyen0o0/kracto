package com.kracto.domain;

import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.domain.Persistable;

public class Article implements Persistable<Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -433385695676018679L;
	private Long id;
	@NotEmpty(message = "{errors.required}")
	private String titleEn;
	@NotEmpty(message = "{errors.required}")
	private String titleVi;
	@NotEmpty(message = "{errors.required}")
	private String descriptionEn;
	@NotEmpty(message = "{errors.required}")
	private String descriptionVi;
	@NotEmpty(message = "{errors.required}")
	private String smallImage;
	@NotEmpty(message = "{errors.required}")
	private String largeImage;
	private Integer viewer;
	private Integer comment;
	private String username;
	@NotEmpty(message = "{errors.required}")
	private String rootCate;
	@NotEmpty(message = "{errors.required}")
	private String subCate;
	private Date created;
	private Date updated;

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

	public Article() {
	}

	public Article(Long id, String titleEn, String titleVi, String descriptionEn, String descriptionVi,
			String smallImage, String largeImage, Integer viewer, Integer comment, String username,
			String rootCate, String subCate, Date created, Date updated) {
		super();
		this.id = id;
		this.titleEn = titleEn;
		this.titleVi = titleVi;
		this.descriptionEn = descriptionEn;
		this.descriptionVi = descriptionVi;
		this.smallImage = smallImage;
		this.largeImage = largeImage;
		this.viewer = viewer;
		this.comment = comment;
		this.username = username;
		this.rootCate = rootCate;
		this.subCate = subCate;
		this.created = created;
		this.updated = updated;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitleEn() {
		return titleEn;
	}

	public void setTitleEn(String titleEn) {
		this.titleEn = titleEn;
	}

	public String getTitleVi() {
		return titleVi;
	}

	public void setTitleVi(String titleVi) {
		this.titleVi = titleVi;
	}

	public String getDescriptionEn() {
		return descriptionEn;
	}

	public void setDescriptionEn(String descriptionEn) {
		this.descriptionEn = descriptionEn;
	}

	public String getDescriptionVi() {
		return descriptionVi;
	}

	public void setDescriptionVi(String descriptionVi) {
		this.descriptionVi = descriptionVi;
	}

	public String getSmallImage() {
		return smallImage;
	}

	public void setSmallImage(String smallImage) {
		this.smallImage = smallImage;
	}

	public String getLargeImage() {
		return largeImage;
	}

	public void setLargeImage(String largeImage) {
		this.largeImage = largeImage;
	}

	public Integer getViewer() {
		return viewer;
	}

	public void setViewer(Integer viewer) {
		this.viewer = viewer;
	}

	public Integer getComment() {
		return comment;
	}

	public void setComment(Integer comment) {
		this.comment = comment;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRootCate() {
		return rootCate;
	}

	public void setRootCate(String rootCate) {
		this.rootCate = rootCate;
	}

	public String getSubCate() {
		return subCate;
	}

	public void setSubCate(String subCate) {
		this.subCate = subCate;
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
}