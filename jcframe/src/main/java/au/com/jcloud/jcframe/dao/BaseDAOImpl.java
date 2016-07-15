package au.com.jcloud.jcframe.dao;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import au.com.jcloud.jcframe.annotation.DisplayValueColumn;
import au.com.jcloud.jcframe.annotation.ExcludeDBRead;
import au.com.jcloud.jcframe.annotation.ExcludeDBWrite;
import au.com.jcloud.jcframe.annotation.IdColumnName;
import au.com.jcloud.jcframe.annotation.LinkField;
import au.com.jcloud.jcframe.annotation.M2MTable;
import au.com.jcloud.jcframe.annotation.TableName;
import au.com.jcloud.jcframe.model.BaseBean;
import au.com.jcloud.jcframe.service.DAOLookup;
import au.com.jcloud.jcframe.util.FieldMetaData;
import au.com.jcloud.jcframe.util.ReflectUtil;
import au.com.jcloud.jcframe.util.Statics;

/**
 * Created by david on 22/05/16.
 */
public class BaseDAOImpl<ID, Bean extends BaseBean<ID>> implements BaseDAO<ID, Bean> {

	private static final Logger LOG = Logger.getLogger(BaseDAOImpl.class);

	protected DataSource dataSource;
	protected String tableName;
	protected Class<Bean> clazz;
	protected String orderBy;
	protected boolean orderAsc = true;
	protected int limit = DEFAULT_LIMIT;
	protected DateFormat dateFormat;
	private Connection conn;
	private DAOLookup daoLookup;

	@Override
	public void init(Class<Bean> clazz) throws IOException {
		init(clazz, Statics.getServiceLoader().getDataSource(), Statics.getServiceLoader().getDAOLookupService());
	}

	@Override
	public void init(Class<Bean> clazz, DataSource dataSource, DAOLookup daoLookup) throws IOException {
		this.clazz = clazz;
		this.tableName = getTableName();
		if (tableName.contains(":") && tableName.split(":").length==2) {
			String schema = tableName.split(":")[0];
			tableName = tableName.split(":")[1];
			this.dataSource = Statics.getServiceLoader().getDataSource(schema);
		} else {
			this.dataSource = dataSource;
		}

		this.daoLookup = daoLookup;
		this.dateFormat = Statics.getServiceLoader().getDatabaseDateFormat();
	}

	@Override
	public void initHttp(ServletConfig config) {
		if (dataSource instanceof BaseDataSource) {
			((BaseDataSource)dataSource).setRealPath(config.getServletContext().getRealPath("/"));
		}
	}

	@Override
	public Connection getConnection() throws SQLException {
		try {
			if (conn != null && !conn.isClosed()) {
				return conn;
			}
		} catch (Exception e) {
			// ignore
		}
		conn = dataSource.getConnection();
		return conn;
	}

	@Override
	public void saveOrUpdate( Bean bean ) throws Exception {
		PreparedStatementWrapper statement = null;
		Connection conn = getConnection();
		try {
			statement = prepareStatementForSave(conn, bean);
			int affectedRows = statement.getPreparedStatement().executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creating bean failed, no rows affected. bean="+bean);
			}
			if (statement.isInsertStmt()) {
				ResultSet generatedKeys = statement.getPreparedStatement().getGeneratedKeys();
				if (generatedKeys.next()) {
					int newId = generatedKeys.getInt(1);
					if (newId>0) {
						bean.setId((ID)(Integer)newId);
					}
					bean.setDisplayValue(ReflectUtil.getDisplayValueFromBean(bean));
				} else {
					throw new SQLException("Creating bean failed, no ID affected. bean="+bean);
				}
				daoLookup.fireDAOUpdate(new DAOActionEvent<ID, Bean>(bean.getId(), clazz, bean, DAOEventType.INSERT));
			}
			executeM2MUpdate(conn, bean);
		} finally {
			if (statement!=null) statement.getPreparedStatement().close();
		}
	}

	@Override
	public void saveOrUpdate( List<Bean> beanList ) throws Exception {
		// TODO: Make transactional
		for (Bean bean : beanList) {
			saveOrUpdate(bean);
		}
	}

	@Override
	public int count() throws SQLException {
		String query = "select count(1) from "+tableName;
		Connection conn = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = conn.prepareStatement(query);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
		} finally {
			if (resultSet!=null) resultSet.close();
			if (statement!=null) statement.close();
		}
		return 0;
	}

	@Override
	public int count(String field, String value, boolean exact) throws SQLException {
		String query = "select count(1) from "+tableName+" where "+field+" like ?";
		if (exact) {
			query = "select count(1) from "+tableName+" where "+field+" = ?";
		}
		Connection conn = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = conn.prepareStatement(query);
			if (exact) {
				statement.setString(1, value);
			} else {
				statement.setString(1, "%" + value + "%");
			}
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
		} finally {
			if (resultSet!=null) resultSet.close();
			if (statement!=null) statement.close();
		}
		return 0;
	}

	@Override
	public void delete( ID id ) throws SQLException {
		if (id!=null && StringUtils.isNotBlank(id.toString())) {
			String idColumnName = ReflectUtil.getAnnotationValue(clazz, IdColumnName.class, BaseBean.FIELD_ID);
			String query = "delete from " + tableName + " where " + idColumnName + "=?";
			Connection conn = getConnection();
			PreparedStatement statement = getStatementWithId(conn, query, id);
			statement.executeUpdate();
			statement.close();
			daoLookup.fireDAOUpdate(new DAOActionEvent<ID, Bean>(id, clazz, null, DAOEventType.DELETE));
		}
	}

	@Override
	public void delete( List<ID> idList ) throws SQLException {
		if (idList==null || idList.isEmpty()) {
			return;
		}
		String idColumnName = ReflectUtil.getAnnotationValue(clazz,IdColumnName.class, BaseBean.FIELD_ID);
		String query = "delete from "+tableName+" where "+idColumnName+" in "+getInClause(idList);
		Connection conn = getConnection();
		PreparedStatement statement = conn.prepareStatement(query);
		statement.executeUpdate();
		statement.close();
		for (ID id : idList) {
			daoLookup.fireDAOUpdate(new DAOActionEvent<ID, Bean>(id, clazz, null, DAOEventType.DELETE));
		}
	}

	@Override
	public List<Bean> getAll(int pageNo, boolean populateBean) throws Exception {
		List<Bean> beans = new ArrayList<Bean>();
		Connection conn = getConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Bean bean = ReflectUtil.getNewBean(clazz);
			String dvColumnName = ReflectUtil.getAnnotationValue(clazz,DisplayValueColumn.class, BaseBean.FIELD_ID);
			if (pageNo<1) { pageNo=1; }
			if (pageNo>MAX_LIMIT) { pageNo=MAX_LIMIT; }
			statement = conn.createStatement();
			String query = "select * from "+tableName;
			if (!StringUtils.isBlank(orderBy)) {
				query += " order by "+orderBy;
			}
			query += " limit "+limit+" offset "+((pageNo-1)*limit);
			resultSet = statement.executeQuery( query );
			while( resultSet.next() ) {
				bean = ReflectUtil.getNewBean(clazz);
				bean.setDisplayValue( resultSet.getString( dvColumnName ) );
				if (populateBean) {
					populateBeanFromResultSet(bean, resultSet);
				}
				beans.add(bean);
			}
		} finally {
			if (resultSet!=null) resultSet.close();
			if (statement!=null) statement.close();
		}
		return beans;
	}

	@Override
	public List<Bean> getLookupList() throws SQLException, IOException {
		List<Bean> beans = new ArrayList<Bean>();
		Connection conn = getConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Bean bean = ReflectUtil.getNewBean(clazz);
			String dvColumnName = ReflectUtil.getAnnotationValue(clazz,DisplayValueColumn.class, BaseBean.FIELD_ID);
			String idColumnName = ReflectUtil.getAnnotationValue(clazz,IdColumnName.class, BaseBean.FIELD_ID);
			statement = conn.createStatement();
			String query = "select "+idColumnName+","+dvColumnName+" from "+tableName+" order by "+dvColumnName+" limit "+DEFAULT_DAOLOOKUP_LIMIT;
			resultSet = statement.executeQuery( query );
			while( resultSet.next() ) {
				bean = ReflectUtil.getNewBean(clazz);
				bean.setId( getIdFromResultSet(resultSet, idColumnName) );
				bean.setDisplayValue( resultSet.getString( dvColumnName ) );
				beans.add(bean);
			}
		} finally {
			if (resultSet!=null) resultSet.close();
			if (statement!=null) statement.close();
		}
		return beans;
	}

	@Override
	public List<Bean> get(List<ID> idList, boolean populateBean) throws Exception {
		List<Bean> result = new ArrayList<Bean>();
		if (idList==null || idList.isEmpty()) {
			return result;
		}
		Connection conn = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		Bean bean = ReflectUtil.getNewBean(clazz);
		String dvColumnName = ReflectUtil.getAnnotationValue(clazz,DisplayValueColumn.class, BaseBean.FIELD_ID);
		String idColumnName = ReflectUtil.getAnnotationValue(clazz,IdColumnName.class, BaseBean.FIELD_ID);
		try {
			for (ID id : idList) {
				String query = "select * from " + tableName + " where "+idColumnName+" in " + getInClause(idList);
				statement = getStatementWithId(conn, query, id);
				resultSet = statement.executeQuery();
				while (resultSet.next()) {
					bean.setId(getIdFromResultSet(resultSet, idColumnName));
					bean.setDisplayValue(resultSet.getString(dvColumnName));
					if (populateBean) {
						populateBeanFromResultSet(bean, resultSet);
					}
					result.add(bean);
				}
			}
		} finally {
			if (resultSet != null) resultSet.close();
			if (statement != null) statement.close();
		}
		return result;
	}

	@Override
	public Bean get(ID id, boolean populateBean) throws Exception {
		Bean bean = ReflectUtil.getNewBean(clazz);
		Connection conn = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			String dvColumnName = ReflectUtil.getAnnotationValue(clazz,DisplayValueColumn.class, BaseBean.FIELD_ID);
			String idColumnName = ReflectUtil.getAnnotationValue(clazz,IdColumnName.class, BaseBean.FIELD_ID);
			String query = "select * from "+tableName+" where "+idColumnName+"=?";
			statement = getStatementWithId(conn, query, id);
			resultSet = statement.executeQuery();
			if( resultSet.next() ) {
				bean.setId( getIdFromResultSet(resultSet, idColumnName) );
				bean.setDisplayValue( resultSet.getString( dvColumnName ) );
				if (populateBean) {
					populateBeanFromResultSet(bean, resultSet);
				}
			}
		} finally {
			if (resultSet!=null) resultSet.close();
			if (statement!=null) statement.close();
		}
		return bean;
	}

	@Override
	public List<Bean> find(String field, String value, int pageNo, boolean exact, boolean populateBean) throws Exception {
		List<Bean> results = new ArrayList<Bean>();
		Connection conn = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			Bean bean = ReflectUtil.getNewBean(clazz);
			String dvColumnName = ReflectUtil.getAnnotationValue(clazz,DisplayValueColumn.class, BaseBean.FIELD_ID);
			String idColumnName = ReflectUtil.getAnnotationValue(clazz,IdColumnName.class, BaseBean.FIELD_ID);
			if (pageNo<1) { pageNo=1; }
			if (pageNo>MAX_LIMIT) { pageNo=MAX_LIMIT; }
			String query = "select * from "+tableName+" where "+field+" like ?";
			if (exact) {
				query = "select * from "+tableName+" where "+field+" = ?";
			}
			if (!StringUtils.isBlank(orderBy)) {
				query += " order by "+orderBy;
			}
			query += " limit "+limit+" offset "+((pageNo-1)*limit);
			statement = conn.prepareStatement( query );
			if (exact) {
				statement.setString(1, value);
			} else {
				statement.setString(1, "%" + value + "%");
			}
			resultSet = statement.executeQuery();
			while( resultSet.next() ) {
				bean = ReflectUtil.getNewBean(clazz);
				bean.setId( getIdFromResultSet(resultSet, idColumnName) );
				bean.setDisplayValue( resultSet.getString( dvColumnName ) );
				if (populateBean) {
					populateBeanFromResultSet(bean, resultSet);
				}
				results.add(bean);
			}
		} finally {
			if (resultSet!=null) resultSet.close();
			if (statement!=null) statement.close();
		}
		return results;
	}

	@Override
	public String getTableName() {
		if (tableName!=null) {
			return tableName;
		}
		return ReflectUtil.getAnnotationValue(clazz,TableName.class,clazz.getSimpleName());
	}
	
	@Override
	public Class<Bean> getBeanClass() {
		return clazz;
	}

	@Override
	public List<String> getBeanFieldNames() {
		return ReflectUtil.getBeanFieldNames(clazz, null);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void populateBeanFromResultSet(Bean bean, ResultSet rs) throws Exception {
		List<FieldMetaData> fieldMetaDataList = ReflectUtil.getFieldData(clazz, ExcludeDBRead.class);
		String dvColumnName = ReflectUtil.getAnnotationValue(clazz,DisplayValueColumn.class, BaseBean.FIELD_ID);
		for (FieldMetaData fieldMetaData : fieldMetaDataList) {
			Field field = fieldMetaData.getField();
			String fieldName = field.getName();
			Method method = fieldMetaData.getSetMethod();
			Class classType = fieldMetaData.getClassType();
            LOG.debug("method="+method.getName()+" paramClass.getSimpleName()="+classType.getSimpleName());

			if (field.isAnnotationPresent(M2MTable.class) && fieldMetaData.isBean() && fieldMetaData.isCollection()) {
				executeM2MPopulate(bean, fieldMetaData);
			} else if (field.isAnnotationPresent(LinkField.class) && fieldMetaData.isBean()) {
				executeLinkFieldPopulate(bean, fieldMetaData);
			}
			if (!field.isAnnotationPresent(LinkField.class) && !field.isAnnotationPresent(M2MTable.class)) {
				// populate the display value
				if (fieldName.equals(dvColumnName)) {
					bean.setDisplayValue(rs.getString(fieldName));
				}

				if (fieldMetaData.isBean()) {
					// Handle BaseBeans
					int id = rs.getInt(fieldName);
					ReflectUtil.invokeSetterMethodForBeanType(bean, method, classType, id);
				} else if (fieldMetaData.isCollection()) {
					// Handle Collections
					String value = rs.getString(fieldName);
					String[] values = new String[0];
					if (StringUtils.isNotBlank(value)) {
						values = value.split(",");
					}
					ReflectUtil.invokeSetterMethodForCollection(bean, method, classType, fieldMetaData.getCollectionClass(), values);
				} else {
					// Handle primitives
					try {
						if (classType.equals(String.class)) {
							method.invoke(bean, rs.getString(fieldName));
						} else if (classType.equals(int.class) || classType.equals(Integer.class)) {
							method.invoke(bean, rs.getInt(fieldName));
						} else if (classType.equals(boolean.class) || classType.equals(Boolean.class)) {
							method.invoke(bean, rs.getBoolean(fieldName));
						} else if (classType.equals(Date.class)) {
							method.invoke(bean, dateFormat.parse(rs.getString(fieldName)));
						} else if (classType.equals(long.class) || classType.equals(Long.class)) {
							method.invoke(bean, rs.getLong(fieldName));
						} else if (classType.equals(short.class) || classType.equals(Short.class)) {
							method.invoke(bean, rs.getShort(fieldName));
						} else if (classType.equals(float.class) || classType.equals(Float.class)) {
							method.invoke(bean, rs.getFloat(fieldName));
						} else if (classType.equals(double.class) || classType.equals(Double.class)) {
							method.invoke(bean, rs.getDouble(fieldName));
						} else if (classType.equals(BigDecimal.class)) {
							method.invoke(bean, rs.getBigDecimal(fieldName));
						} else if (classType.equals(Blob.class)) {
							method.invoke(bean, rs.getBlob(fieldName));
						} else if (classType.equals(Clob.class)) {
							method.invoke(bean, rs.getClob(fieldName));
						} else if (classType.equals(Object.class)) {
							Object result = rs.getObject(fieldName);
							if (result!=null) {
								if (StringUtils.isNumeric(result.toString())) {
									method.invoke(bean, Integer.parseInt(result.toString()));
								} else {
									method.invoke(bean, result.toString());
								}
							} else {
								method.invoke(bean, null);
							}
						}
					} catch (SQLException e) {
						if (!fieldName.equals(BaseBean.FIELD_DISPLAYVALUE)) { // ignore "displayvalue", as this is custom to BaseBean
							throw e;
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public PreparedStatementWrapper prepareStatementForSave(Connection conn, Bean bean) throws Exception {
		boolean updateStmt = false;
		List<FieldMetaData> fieldMetaDataList = ReflectUtil.getFieldData(clazz, ExcludeDBWrite.class);

		List<String> columns = new ArrayList<String>();
		for (FieldMetaData fieldMetaData : fieldMetaDataList) {
			Field field = fieldMetaData.getField();
			if (!field.isAnnotationPresent(LinkField.class) && !field.isAnnotationPresent(M2MTable.class)) {
				columns.add(field.getName());
			}
		}
		String query = "insert into "+tableName+" "+ getInsertIntoColumnsSQL(columns);
		if (bean.getId()!=null && StringUtils.isNotBlank(bean.getId().toString())) {
			updateStmt = true;
			query = "update "+tableName+" set "+ getUpdateColumnsSQL(columns)+" where id=?";
		}
        LOG.debug("query="+query+" columns="+columns);
		PreparedStatement preparedStatement;
		if (updateStmt) {
			preparedStatement = conn.prepareStatement(query);
		} else {
			preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		}
		PreparedStatementWrapper preparedStatementWrapper = new PreparedStatementWrapper(preparedStatement);
		preparedStatementWrapper.setInsertStmt(!updateStmt);
		preparedStatementWrapper.setSql(query);

		int index = 0;
		for (FieldMetaData fieldMetaData : fieldMetaDataList) {
			Field field = fieldMetaData.getField();
			Method method = fieldMetaData.getGetMethod();
			if (!field.isAnnotationPresent(LinkField.class) && !field.isAnnotationPresent(M2MTable.class)) {
				Class classType = fieldMetaData.getClassType();
				Object result = method.invoke(bean);
				LOG.debug("classType=" + classType.getSimpleName() +" method=" + method.getName() +  " result=" + result);
				if (fieldMetaData.isBean()) {
					LOG.debug("classType=" + classType.getSimpleName() +" method=" + method.getName() +  " result=" + result);
					// Handle BaseBeans
					if (result == null) {
						preparedStatement.setInt(++index, 0);
					} else if (result instanceof BaseBean) {
						preparedStatement.setObject( ++index, ((BaseBean<ID>) result).getId());
					}
				} else if (fieldMetaData.isCollection()) {
					// Handle Collections
					if (result == null) {
						preparedStatement.setObject(++index, null);
					} else {
						Collection c = (Collection) result;
						String resString = "";
						for (Object o : c) {
							if (resString.length() > 0) {
								resString += ",";
							}
							if (o instanceof BaseBean) {
								resString += ((BaseBean) o).getId();
							} else {
								resString += o.toString().trim();
							}
						}
						preparedStatement.setString(++index, resString);
					}
				} else {
					// Handle primitives
					if (result == null) {
						preparedStatement.setObject(++index, null);
					} else {
						if (classType.equals(String.class)) {
							preparedStatement.setString(++index, (String) result);
						} else if (classType.equals(int.class) || classType.equals(Integer.class)) {
							preparedStatement.setInt(++index, (Integer) result);
						} else if (classType.equals(boolean.class) || classType.equals(Boolean.class)) {
							preparedStatement.setBoolean(++index, (Boolean) result);
						} else if (classType.equals(Date.class)) {
							preparedStatement.setString(++index, dateFormat.format(result));
						} else if (classType.equals(long.class) || classType.equals(Long.class)) {
							preparedStatement.setLong(++index, (Long) result);
						} else if (classType.equals(short.class) || classType.equals(Short.class)) {
							preparedStatement.setShort(++index, (Short) result);
						} else if (classType.equals(float.class) || classType.equals(Float.class)) {
							preparedStatement.setFloat(++index, (Float) result);
						} else if (classType.equals(double.class) || classType.equals(Double.class)) {
							preparedStatement.setDouble(++index, (Double) result);
						} else if (classType.equals(BigDecimal.class)) {
							preparedStatement.setBigDecimal(++index, (BigDecimal) result);
						} else if (classType.equals(Blob.class)) {
							preparedStatement.setBlob(++index, (Blob) result);
						} else if (classType.equals(Clob.class)) {
							preparedStatement.setClob(++index, (Clob) result);
						} else if (classType.equals(Object.class)) {
							preparedStatement.setObject(++index, result);
						}
					}
				}
			}
		}
		if (updateStmt) {
			preparedStatement.setObject(++index, bean.getId());
		}
		return preparedStatementWrapper;
	}

	@Override
	public void executeM2MUpdate(Connection conn, Bean bean) throws Exception {
		List<FieldMetaData> fieldMetaDataList = ReflectUtil.getFieldData(clazz, ExcludeDBWrite.class);

		for (FieldMetaData fieldMetaData : fieldMetaDataList) {
			Field field = fieldMetaData.getField();
			if (field.isAnnotationPresent(M2MTable.class)) {
				if (fieldMetaData.isCollection()) {
					Method method = fieldMetaData.getGetMethod();
					Object result = method.invoke(bean);

					String m2mTable = field.getAnnotation(M2MTable.class).table();
					String column = field.getAnnotation(M2MTable.class).column();
					String rcolumn = field.getAnnotation(M2MTable.class).rcolumn();
					String deleteSQL = "delete from " + m2mTable + " where " + column + "=?";
					PreparedStatement deleteStatement = getStatementWithId(conn, deleteSQL, bean.getId());
					if (result!=null) {
						Class<? extends Collection<?>> collectionClass = fieldMetaData.getCollectionClass();
						Collection<?> resultList = collectionClass.cast(result);
						String insertSQL = "insert into " + m2mTable + " (" + column + "," + rcolumn + ") VALUES (?,?)";
						PreparedStatement insertStatement = conn.prepareStatement(insertSQL);
						for (Object resultObject : resultList) {
							if (!(resultObject instanceof BaseBean)) {
								throw new Exception("@M2MTable annotation requires the Collection to be BaseBean objects");
							}
							BaseBean resultBean = (BaseBean) resultObject;
							insertStatement.setObject(1, bean.getId());
							insertStatement.setObject(2, ReflectUtil.getValueObject(resultBean.getId(),dateFormat));
							insertStatement.addBatch();
						}
						deleteStatement.executeUpdate();
						insertStatement.executeBatch();
					} else {
						deleteStatement.executeUpdate();
						}
				} else {
					throw new Exception("@M2MTable annotation has to be on a 'Collection' field, e.g. List, Set, etc");
				}
			}
		}
	}

	@Override
	public void executeM2MPopulate(Bean bean, FieldMetaData fieldMetaData) throws Exception {

		Field field = fieldMetaData.getField();
		if (field.isAnnotationPresent(M2MTable.class)) {
			if (fieldMetaData.isCollection()) {
				Class<? extends Collection<?>> collectionClass = fieldMetaData.getCollectionClass();

				String m2mTable = field.getAnnotation(M2MTable.class).table();
				String column = field.getAnnotation(M2MTable.class).column();
				String rcolumn = field.getAnnotation(M2MTable.class).rcolumn();
				String query = "select " + column + "," + rcolumn + " from " + m2mTable + " where " + column + "=?";
				PreparedStatement statement = getStatementWithId(getConnection(), query, bean.getId());
				ResultSet rs = statement.executeQuery();
				List<BaseBean> m2mList = new ArrayList<BaseBean>();
				BaseDAO fieldDao = Statics.getDaoMap().get(fieldMetaData.getClassType());
				while (rs.next()) {
					int linkId = rs.getInt(2);
					BaseBean fieldBean = fieldDao.get(linkId,false);
					if (fieldBean!=null) {
						m2mList.add(fieldBean);
					}
				}
				Method method = fieldMetaData.getSetMethod();
				method.invoke(bean,m2mList);
			} else {
				throw new Exception("@M2MTable annotation has to be on a 'Collection' field, e.g. List, Set, etc");
			}
		}
		LOG.debug("bean after executeM2MPopulate="+bean);
	}

	@Override
	public void executeLinkFieldPopulate(Bean bean, FieldMetaData fieldMetaData) throws Exception {

		Field field = fieldMetaData.getField();
		if (field.isAnnotationPresent(LinkField.class)) {
			String fieldName = field.getAnnotation(LinkField.class).value();
			BaseDAO fieldDao = Statics.getDaoMap().get(fieldMetaData.getClassType());
			List<BaseBean> resultList = fieldDao.find(fieldName,""+bean.getId(),0,true,false);
			if (fieldMetaData.isCollection()) {
				Class<? extends Collection<?>> collectionClass = fieldMetaData.getCollectionClass();
				Method method = fieldMetaData.getSetMethod();
				method.invoke(bean,resultList);
			} else {
				if (!resultList.isEmpty()) {
					BaseBean resultBean = resultList.get(0);
					Method method = fieldMetaData.getSetMethod();
					method.invoke(bean, resultBean);
				}
			}
		}
		LOG.debug("bean after executeDaoLookupPopulate="+bean);
	}


	/**
	 * Creates the insert part of the SQL. e.g. (name,email,date) values (?,?,?)
	 */
	public static String getInsertIntoColumnsSQL(List<String> columns) {
		String names = "";
		String params = "";
		for (String column : columns) {
			if (names.length()>0) {
				names +=", ";
				params +=", ";
			}
			names += column;
			params += "?";
		}
		return "("+names+") values ("+params+")";
	}

	/**
	 * Create the update part of the SQL. e.g. name=?, email=?, date=?
	 */
	public static String getUpdateColumnsSQL(List<String> columns) {
		String sql = "";
		for (String column : columns) {
			if (sql.length()>0) {
				sql +=", ";
			}
			sql += column+"=?";
		}
		return sql;
	}

	@Override
	public String getOrderBy() {
		return orderBy;
	}

	@Override
	public void toggleOrderBy(String orderBy) {
		orderAsc = !orderAsc;
		setOrderBy(orderBy,orderAsc);
	}

	@Override
	public void setOrderBy(String orderBy, boolean isAsc) {
		orderAsc = isAsc;
		if (StringUtils.isEmpty(orderBy)) {
			orderBy = "";
		}
		if (isAsc) {
			this.orderBy = orderBy +" ASC";
		} else {
			this.orderBy = orderBy +" DESC";
		}
	}

	@Override
	public int getLimit() {
		return limit;
	}

	@Override
	public void setLimit(int limit) {
		if (limit<=0) {
			limit=DEFAULT_LIMIT;
		}
		if (limit>MAX_LIMIT) {
			limit=MAX_LIMIT;
		}
		this.limit = limit;
	}

	@Override
	public DateFormat getDateFormat() {
		return dateFormat;
	}

	@Override
	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String toString() {
		return getClass().getSimpleName()+"["+clazz.getSimpleName().toLowerCase()+"] tableName="+tableName;
	}

	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	@Override
	public ID getIdFromResultSet(ResultSet resultSet, String idColumnName) throws SQLException {
		Object id = resultSet.getObject( idColumnName );
		if (id!=null) {
			try {
				return ReflectUtil.getValueObject(id, dateFormat);
			} catch (ParseException e) {
				throw new SQLException(e);
			}
		}
		return null;
	}

	public String getInClause(List<ID> idList) {
		StringBuffer buf = new StringBuffer("(");
		for (ID id : idList) {
			if (id!=null) {
				if (buf.length()>1) {
					buf.append(",");
				}
				if (String.class.isAssignableFrom(id.getClass())) {
					buf.append("'" + id + "'");
				} else {
					buf.append(id);
				}
			}
		}
		buf.append(")");
		return buf.toString();
	}

	protected PreparedStatement getStatementWithId(Connection conn, String query, ID id) throws SQLException {
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setObject(1, id);
		return statement;
	}
}
