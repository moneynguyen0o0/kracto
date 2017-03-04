package com.kracto.service;

import com.kracto.domain.Metric;

public interface MetricService extends JdbcService<Metric, Long> {

	void increaseCount(String request, int status);

	void autoSave();

}
