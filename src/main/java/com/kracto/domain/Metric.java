package com.kracto.domain;

import java.util.Date;

import org.springframework.data.domain.Persistable;

public class Metric implements Persistable<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4978753145889380797L;
	private Long id;
	private String result;
	private Date date;
	
	private transient boolean persisted;

	public Metric() {
	}

	public Metric(Long id, String result, Date date) {
		super();
		this.id = id;
		this.result = result;
		this.date = date;
	}

	@Override
	public boolean isNew() {
		return !persisted;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
