package com.kracto.repository.generic;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link PagingAndSortingRepository} using
 * {@link JdbcTemplate}
 */
public abstract class JdbcRepositoryImpl<T extends Persistable<ID>, ID extends Serializable>
		implements JdbcRepository<T, ID>, InitializingBean, BeanFactoryAware {

	public static Object[] pk(Object... idValues) {
		return idValues;
	}

	private final TableDescription table;

	private final RowMapper<T> rowMapper;
	private final RowUnmapper<T> rowUnmapper;

	private SqlGenerator sqlGenerator = new SqlGenerator();
	private BeanFactory beanFactory;
	private JdbcTemplate jdbcTemplate;

	public JdbcRepositoryImpl(RowMapper<T> rowMapper, RowUnmapper<T> rowUnmapper, SqlGenerator sqlGenerator,
			TableDescription table) {
		Assert.notNull(rowMapper);
		Assert.notNull(rowUnmapper);
		Assert.notNull(table);

		this.rowUnmapper = rowUnmapper;
		this.rowMapper = rowMapper;
		this.sqlGenerator = sqlGenerator;
		this.table = table;
	}

	public JdbcRepositoryImpl(RowMapper<T> rowMapper, RowUnmapper<T> rowUnmapper, TableDescription table) {
		this(rowMapper, rowUnmapper, null, table);
	}

	public JdbcRepositoryImpl(RowMapper<T> rowMapper, RowUnmapper<T> rowUnmapper, String tableName, String idColumn) {
		this(rowMapper, rowUnmapper, null, new TableDescription(tableName, idColumn));
	}

	public JdbcRepositoryImpl(RowMapper<T> rowMapper, RowUnmapper<T> rowUnmapper, String tableName) {
		this(rowMapper, rowUnmapper, new TableDescription(tableName, "id"));
	}

	public JdbcRepositoryImpl(RowMapper<T> rowMapper, TableDescription table) {
		this(rowMapper, new MissingRowUnmapper<T>(), null, table);
	}

	public JdbcRepositoryImpl(RowMapper<T> rowMapper, String tableName, String idColumn) {
		this(rowMapper, new MissingRowUnmapper<T>(), null, new TableDescription(tableName, idColumn));
	}

	public JdbcRepositoryImpl(RowMapper<T> rowMapper, String tableName) {
		this(rowMapper, new MissingRowUnmapper<T>(), new TableDescription(tableName, "id"));
	}

	// InitializingBean is an interface which has afterPropertiesSet() method.
	// Our bean can use it to perform any task required after the bean
	// properties are set. BeanFactory invokes afterPropertiesSet() method once
	// the bean properties are initialized. We can use InitializingBean to
	// validate our properties value or to initializing any task.
	// http://www.concretepage.com/spring/example_initializing_bean_spring
	@Override
	public void afterPropertiesSet() throws Exception {
		obtainJdbcTemplate();
		if (sqlGenerator == null)
			obtainSqlGenerator();
	}

	public void setSqlGenerator(SqlGenerator sqlGenerator) {
		this.sqlGenerator = sqlGenerator;
	}

	public SqlGenerator getSqlGenerator() {
		return sqlGenerator;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	protected JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	protected TableDescription getTable() {
		return table;
	}

	private void obtainSqlGenerator() {
		try {
			sqlGenerator = beanFactory.getBean(SqlGenerator.class);
		} catch (NoSuchBeanDefinitionException e) {
			sqlGenerator = new SqlGenerator();
		}
	}

	private void obtainJdbcTemplate() {
		try {
			jdbcTemplate = beanFactory.getBean(JdbcTemplate.class);
		} catch (NoSuchBeanDefinitionException e) {
			final DataSource dataSource = beanFactory.getBean(DataSource.class);
			jdbcTemplate = new JdbcTemplate(dataSource);
		}
	}

	/**
	 * If a bean in spring implements BeanFactoryAware then that bean has to
	 * implement a method that is setBeanFactory. And when that bean is loaded
	 * in spring container, setBeanFactory is called. BeanFactoryAware belongs
	 * to the package org.springframework.beans.factory. BeanFactoryAware awares
	 * the bean for its BeanFactory.
	 * http://www.concretepage.com/spring/example_beanfactoryaware_spring
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public long count() {
		return jdbcTemplate.queryForObject(sqlGenerator.count(table), Long.class);
	}

	@Override
	public void delete(ID id) {
		jdbcTemplate.update(sqlGenerator.deleteById(table), idToObjectArray(id));
	}

	@Override
	public void delete(T entity) {
		jdbcTemplate.update(sqlGenerator.deleteById(table), idToObjectArray(entity.getId()));
	}

	@Override
	public void deleteAll() {
		jdbcTemplate.update(sqlGenerator.deleteAll(table));
	}

	@Override
	public boolean exists(ID id) {
		return jdbcTemplate.queryForObject(sqlGenerator.countById(table), Integer.class, idToObjectArray(id)) > 0;
	}

	@Override
	public List<T> findAll() {
		return jdbcTemplate.query(sqlGenerator.selectAll(table), rowMapper);
	}

	@Override
	public T findOne(ID id) {
		final Object[] idColumns = idToObjectArray(id);
		final List<T> entityOrEmpty = jdbcTemplate.query(sqlGenerator.selectById(table), idColumns, rowMapper);
		return entityOrEmpty.isEmpty() ? null : entityOrEmpty.get(0);
	}

	private static <ID> Object[] idToObjectArray(ID id) {
		if (id instanceof Object[])
			return (Object[]) id;
		else
			return new Object[] { id };
	}

	private static <ID> List<Object> idToObjectList(ID id) {
		if (id instanceof Object[])
			return Arrays.asList((Object[]) id);
		else
			return Collections.<Object> singletonList(id);
	}

	@Override
	public <S extends T> S save(S entity) {
		final Map<String, Object> columns = preCreate(columns(entity), entity);
		if (entity.getId() == null)
			return createWithAutoGeneratedKey(entity, columns);
		else
			return createWithManuallyAssignedKey(entity, columns);
	}

	@Override
	public void save(List<T> entities) {
		if (entities.size() != 0) {
			T entity = entities.get(0);
			final Map<String, Object> columns = preCreate(columns(entity), entity);
			if (entity.getId() == null)
				removeIdColumns(columns);
			final String createQuery = sqlGenerator.create(table, columns);
			jdbcTemplate.batchUpdate(createQuery, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					T entity = entities.get(i);
					final Map<String, Object> columns = preCreate(columns(entity), entity);
					if (entity.getId() == null)
						removeIdColumns(columns);
					setObjectToPs(columns.values().toArray(), ps);
				}

				@Override
				public int getBatchSize() {
					return entities.size();
				}

			});
		}
	}

	@Override
	public <S extends T> S update(S entity) {
		final Map<String, Object> columns = preUpdate(entity, columns(entity));
		final List<Object> idValues = removeIdColumns(columns);
		final String updateQuery = sqlGenerator.update(table, columns);
		putIdToColumn(columns, idValues);
		final Object[] queryParams = columns.values().toArray();
		jdbcTemplate.update(updateQuery, queryParams);
		return postUpdate(entity);
	}

	@Override
	public void update(List<T> entities) {
		if (entities.size() != 0) {
			T entity = entities.get(0);
			final Map<String, Object> columns = preUpdate(entity, columns(entity));
			removeIdColumns(columns);
			jdbcTemplate.batchUpdate(sqlGenerator.update(table, columns), new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					T entity = entities.get(i);
					final Map<String, Object> columns = preUpdate(entity, columns(entity));
					putIdToColumn(columns, removeIdColumns(columns));
					setObjectToPs(columns.values().toArray(), ps);
				}

				@Override
				public int getBatchSize() {
					return entities.size();
				}

			});
		}
	}

	protected Map<String, Object> preUpdate(T entity, Map<String, Object> columns) {
		return columns;
	}

	private <S extends T> S createWithManuallyAssignedKey(S entity, Map<String, Object> columns) {
		final String createQuery = sqlGenerator.create(table, columns);
		final Object[] queryParams = columns.values().toArray();
		jdbcTemplate.update(createQuery, queryParams);
		return postCreate(entity, null);
	}

	private <S extends T> S createWithAutoGeneratedKey(S entity, Map<String, Object> columns) {
		removeIdColumns(columns);
		final String createQuery = sqlGenerator.create(table, columns);
		final Object[] queryParams = columns.values().toArray();
		final GeneratedKeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				final String idColumnName = table.getIdColumns().get(0);
				final PreparedStatement ps = con.prepareStatement(createQuery, new String[] { idColumnName });
				setObjectToPs(queryParams, ps);
				return ps;
			}
		}, key);
		return postCreate(entity, key.getKey());
	}

	private List<Object> removeIdColumns(Map<String, Object> columns) {
		List<Object> idColumnsValues = new ArrayList<Object>(columns.size());
		for (String idColumn : table.getIdColumns())
			idColumnsValues.add(columns.remove(idColumn));
		return idColumnsValues;
	}

	private void putIdToColumn(final Map<String, Object> columns, final List<Object> idValues) {
		for (int i = 0; i < table.getIdColumns().size(); ++i)
			columns.put(table.getIdColumns().get(i), idValues.get(i));
	}

	private void setObjectToPs(final Object[] queryParams, final PreparedStatement ps) throws SQLException {
		for (int i = 0; i < queryParams.length; ++i)
			ps.setObject(i + 1, queryParams[i]);
	}

	protected Map<String, Object> preCreate(Map<String, Object> columns, T entity) {
		return columns;
	}

	public LinkedHashMap<String, Object> columns(T entity) {
		return new LinkedHashMap<String, Object>(rowUnmapper.mapColumns(entity));
	}

	protected <S extends T> S postUpdate(S entity) {
		return entity;
	}

	/**
	 * General purpose hook method that is called every time {@link #create} is
	 * called with a new entity.
	 * <p/>
	 * OVerride this method e.g. if you want to fetch auto-generated key from
	 * database
	 *
	 *
	 * @param entity
	 *            Entity that was passed to {@link #create}
	 * @param generatedId
	 *            ID generated during INSERT or NULL if not available/not
	 *            generated. todo: Type should be ID, not Number
	 * @return Either the same object as an argument or completely different one
	 */
	protected <S extends T> S postCreate(S entity, Number generatedId) {
		return entity;
	}

	@Override
	public List<T> findAll(List<ID> ids) {
		if (ids.isEmpty())
			return Collections.emptyList();
		final Object[] idColumnValues = flatten(ids);
		return jdbcTemplate.query(sqlGenerator.selectByIds(table, ids.size()), rowMapper, idColumnValues);
	}

	private static <ID> Object[] flatten(List<ID> ids) {
		final List<Object> result = new ArrayList<Object>();
		for (ID id : ids)
			result.addAll(idToObjectList(id));
		return result.toArray();
	}

	@Override
	public List<T> findAll(Sort sort) {
		return jdbcTemplate.query(sqlGenerator.selectAll(table, sort), rowMapper);
	}

	@Override
	public Page<T> findAll(Pageable page) {
		String query = sqlGenerator.selectAll(table, page);
		return new PageImpl<T>(jdbcTemplate.query(query, rowMapper), page, count());
	}

	protected List<T> getByFields(String filter, String[] params, RowMapper<T> rowMapper, String fields) {
		if (filter == null)
			filter = "";
		String query = "SELECT " + fields + " FROM " + this.table.getName() + filter;
		return getJdbcTemplate().query(query, params, rowMapper);
	}

	protected Page<T> getByFields(String filter, String[] params, Pageable pageable, RowMapper<T> rowMapper,
			String fields) {
		if (filter == null)
			filter = "";
		// String... fields
		// Stream.of(fields).map(Object::toString).collect(Collectors.joining(","))
		String query = "SELECT SQL_CALC_FOUND_ROWS " + fields + " FROM " + this.table.getName() + filter
				+ getSqlGenerator().sortingClauseIfRequired(pageable.getSort())
				+ getSqlGenerator().limitClause(pageable);
		return new PageImpl<T>(getJdbcTemplate().query(query, params, rowMapper), pageable,
				getJdbcTemplate().queryForObject("SELECT FOUND_ROWS()", Long.class));
	}

	protected long count(String query, Object[] params) {
		return getJdbcTemplate().queryForObject(query, params, Long.class);
	}

	protected boolean exists(String query, Object[] params) {
		return getJdbcTemplate().queryForObject(query, params, Boolean.class);
	}

	@SuppressWarnings("unchecked")
	protected Page<T> getByProcedure(String procedureName, Map<String, Object> inParam, String outField,
			RowMapper<T> rowMapper, Pageable pageable) {
		// http://docs.spring.io/spring/docs/current/spring-framework-reference/html/jdbc.html
		// http://www.tutorialspoint.com/spring/calling_stored_procedure.htm
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(getJdbcTemplate()).withProcedureName(procedureName)
				.returningResultSet("entities", rowMapper);
		Map<String, Object> out = simpleJdbcCall.execute(new MapSqlParameterSource(inParam));
		return new PageImpl<T>((List<T>) out.get("entities"), pageable, (Integer) out.get(outField));
	}

	@SuppressWarnings("unchecked")
	protected List<T> getByProcedure(String procedureName, Map<String, Object> inParam, RowMapper<T> rowMapper) {
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(getJdbcTemplate()).withProcedureName(procedureName)
				.returningResultSet("entities", rowMapper);
		Map<String, Object> out = simpleJdbcCall.execute(new MapSqlParameterSource(inParam));
		return (List<T>) out.get("entities");
	}

	@SuppressWarnings("unchecked")
	protected T findOneByProcedure(String procedureName, Map<String, Object> inParam, RowMapper<T> rowMapper) {
		// http://stackoverflow.com/questions/31296270/simplejdbccall-executeobject-usage-in-spring-framework
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(getJdbcTemplate()).withProcedureName(procedureName)
				.returningResultSet("entity", rowMapper);
		Map<String, Object> out = simpleJdbcCall.execute(new MapSqlParameterSource(inParam));
		List<T> list = (List<T>) out.get("entity");
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

}