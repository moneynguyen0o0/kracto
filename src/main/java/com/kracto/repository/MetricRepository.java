package com.kracto.repository;

import com.kracto.domain.Metric;
import com.kracto.repository.generic.JdbcRepository;

public interface MetricRepository extends JdbcRepository<Metric, Long> {

}
