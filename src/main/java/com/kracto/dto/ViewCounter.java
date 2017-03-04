package com.kracto.dto;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ViewCounter {

	private Set<Long> visitors;

	public ViewCounter() {
		visitors = new CopyOnWriteArraySet<Long>();
	}

	public Set<Long> getVisitors() {
		return visitors;
	}

	public void add(Long id) {
		visitors.add(id);
	}

	public boolean isExist(Long id) {
		return visitors.stream().anyMatch(i -> i.equals(id));
	}

}
