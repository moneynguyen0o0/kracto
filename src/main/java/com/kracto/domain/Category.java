package com.kracto.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.domain.Persistable;

public class Category implements Persistable<Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -54237860425038028L;
	@NotNull(message = "{errors.required}")
	@Min(value = 1, message = "{errors.id.range.min}")
	private Integer id;
	@NotNull(message = "{errors.required}")
	@NotEmpty(message = "{errors.required}")
	private String nameEn;
	@NotNull(message = "{errors.required}")
	@NotEmpty(message = "{errors.required}")
	private String nameVi;
	private Integer parent;

	private transient boolean persisted;

	@Override
	public boolean isNew() {
		return !persisted;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setPersisted(boolean persisted) {
		this.persisted = persisted;
	}

	public Category() {
	}

	public Category(Integer id, String nameEn, String nameVi, Integer parent) {
		this.id = id;
		this.nameEn = nameEn;
		this.nameVi = nameVi;
		this.parent = parent;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getNameVi() {
		return nameVi;
	}

	public void setNameVi(String nameVi) {
		this.nameVi = nameVi;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

}
