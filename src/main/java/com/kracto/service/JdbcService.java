package com.kracto.service;

import java.io.Serializable;

import com.kracto.repository.generic.JdbcRepository;

public interface JdbcService<T, ID extends Serializable> extends JdbcRepository<T, ID> {

}
