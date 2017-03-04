package com.kracto.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.kracto.domain.Metric;
import com.kracto.repository.MetricRepository;
import com.kracto.repository.generic.JdbcRepositoryImpl;
import com.kracto.repository.generic.RowUnmapper;

@Repository("metricRepository")
public class MetricRepositoryImpl extends JdbcRepositoryImpl<Metric, Long> implements MetricRepository {

	public MetricRepositoryImpl() {
		super(ROW_MAPPER, ROW_UNMAPPER, "metric");
	}

	public static final RowMapper<Metric> ROW_MAPPER = new RowMapper<Metric>() {
		@Override
		public Metric mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Metric(rs.getLong("id"), rs.getString("result"), rs.getDate("date"));
		}
	};

	private static final RowUnmapper<Metric> ROW_UNMAPPER = new RowUnmapper<Metric>() {
		@Override
		public Map<String, Object> mapColumns(Metric metric) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("id", metric.getId());
			mapping.put("result", metric.getResult());
			mapping.put("date", metric.getDate());
			return mapping;
		}
	};

}