package com.kracto.service.impl;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kracto.domain.Metric;
import com.kracto.repository.MetricRepository;
import com.kracto.service.MetricService;

@Service("metricService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MetricServiceImpl extends JdbcServiceImpl<Metric, Long> implements MetricService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MetricServiceImpl.class);

	private ConcurrentMap<String, ConcurrentHashMap<Integer, Integer>> metricMap;

	@Autowired
	public MetricServiceImpl(MetricRepository metricRepository) {
		super(metricRepository);
		metricMap = new ConcurrentHashMap<String, ConcurrentHashMap<Integer, Integer>>();
	}

	@Override
	public void increaseCount(String request, int status) {
		ConcurrentHashMap<Integer, Integer> statusMap = metricMap.get(request);
		if (statusMap == null)
			statusMap = new ConcurrentHashMap<Integer, Integer>();
		Integer count = statusMap.get(status);
		count = count == null ? 1 : count + 1;
		statusMap.put(status, count);
		metricMap.put(request, statusMap);
	}

	@Override
	@Scheduled(cron = "0 0 0 * * ?")
	public void autoSave() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Metric metric = new Metric();
			metric.setResult(mapper.writeValueAsString(metricMap));
			metric.setDate(new Date());
			this.save(metric);

			metricMap.clear();

			LOGGER.info("Save METRIC at {}", new Date());
		} catch (JsonProcessingException e) {
			LOGGER.error("Can not convert metricMap to json - {}", metricMap);

			e.printStackTrace();
		}
	}

}
