package com.kracto.dto;

public class RootCate implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8012997997935683932L;
	private String nameEn;
	private String nameVi;
	private Integer index;

	public RootCate() {
	}

	public RootCate(String nameEn, String nameVi, Integer index) {
		super();
		this.nameEn = nameEn;
		this.nameVi = nameVi;
		this.index = index;
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

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

}
