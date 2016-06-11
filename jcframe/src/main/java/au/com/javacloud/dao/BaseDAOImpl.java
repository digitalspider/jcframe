package au.com.javacloud.dao;

import java.io.IOException;
import java.lang.annotation.Annotation;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import au.com.javacloud.annotation.TableName;
import au.com.javacloud.model.BaseBean;
import au.com.javacloud.util.ReflectUtil;

/**
 * Created by david on 22/05/16.
 */
public class BaseDAOImpl<T extends BaseBean> implements BaseDAO<T> {

	private static final Logger LOG = Logger.getLogger(BaseDAOImpl.class);
	public static final int DEFAULT_LIMIT = 50;

	protected DataSource dataSource;
	protected String tableName;
	protected Class<T> clazz;
	protected String orderBy;
	protected boolean orderAsc = true;
	protected int limit = DEFAULT_LIMIT;
	protected DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Connection conn;

	@Override
	public void init(Class<T> clazz, DataSource dataSource) {
		this.clazz = clazz;
		this.dataSource = dataSource;
		this.tableName= getTableName();
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
	public void saveOrUpdate( T bean ) throws Exception {
		PreparedStatement statement = null;
		Connection conn = getConnection();
		try {
			statement = prepareStatementForSave(conn, bean);
			statement.executeUpdate();
		} finally {
			if (statement!=null) statement.close();
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
	public int count(String field, String value) throws SQLException {
		String query = "select count(1) from "+tableName+" where "+field+" like ?";
		Connection conn = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = conn.prepareStatement(query);
			statement.setString(1, "%"+value+"%");
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
	public void delete( int id ) throws SQLException {
		String query = "delete from "+tableName+" where id=?";
		Connection conn = getConnection();
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setInt(1, id);
		statement.executeUpdate();
		statement.close();
	}

	@Override
	public List<T> getAll(int pageNo) throws Exception {
		List<T> beans = new ArrayList<T>();
		Connection conn = getConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			if (pageNo<1) { pageNo=1; };
			if (pageNo>MAX_LIMIT) { pageNo=MAX_LIMIT; };
			statement = conn.createStatement();
			String query = "select * from "+tableName;
			if (!StringUtils.isBlank(orderBy)) {
				query += " order by "+orderBy;
			}
			query += " limit "+limit+" offset "+((pageNo-1)*limit);
			resultSet = statement.executeQuery( query );
			while( resultSet.next() ) {
				T bean = ReflectUtil.getNewBean(clazz);
				populateBeanFromResultSet(bean, resultSet);
				beans.add(bean);
			}
		} finally {
			if (resultSet!=null) resultSet.close();
			if (statement!=null) statement.close();
		}
		return beans;
	}

	public List<T> getLookup() throws SQLException, IOException {
		List<T> beans = new ArrayList<T>();
		Connection conn = getConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			T bean = ReflectUtil.getNewBean(clazz);
			String columnName = bean.getNameColumn();
			statement = conn.createStatement();
			String query = "select id,"+columnName+" from "+tableName;
			if (!StringUtils.isBlank(orderBy)) {
				query += " order by "+orderBy;
			}
			query += " limit "+limit;
			resultSet = statement.executeQuery( query );
			while( resultSet.next() ) {
				bean = ReflectUtil.getNewBean(clazz);
				bean.setId( resultSet.getInt( BaseBean.FIELD_ID ) );
				bean.setDisplayValue( resultSet.getString( columnName ) );
				beans.add(bean);
			}
		} finally {
			if (resultSet!=null) resultSet.close();
			if (statement!=null) statement.close();
		}
		return beans;
	}

	@Override
	public T getLookup(int id) throws Exception {
		T bean = ReflectUtil.getNewBean(clazz);
		Connection conn = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			String columnName = bean.getNameColumn();
			String query = "select id,"+columnName+" from "+tableName+" where id=?";
			statement = conn.prepareStatement( query );
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			if( resultSet.next() ) {
				bean.setId( resultSet.getInt( BaseBean.FIELD_ID ) );
				bean.setDisplayValue( resultSet.getString( columnName ) );
			}
		} finally {
			if (resultSet!=null) resultSet.close();
			if (statement!=null) statement.close();
		}
		return bean;
	}

	@Override
	public T get(int id) throws Exception {
		T bean = ReflectUtil.getNewBean(clazz);
		Connection conn = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			String query = "select * from "+tableName+" where id=?";
			if (!StringUtils.isBlank(orderBy)) {
				query += " order by "+orderBy;
			}
			statement = conn.prepareStatement( query );
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			if( resultSet.next() ) {
				bean.setId( resultSet.getInt( BaseBean.FIELD_ID ) );
				populateBeanFromResultSet(bean, resultSet);
			}
		} finally {
			if (resultSet!=null) resultSet.close();
			if (statement!=null) statement.close();
		}
		return bean;
	}

	@Override
	public List<T> find(String field, String value, int pageNo) throws Exception {
		List<T> results = new ArrayList<T>();
		Connection conn = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			if (pageNo<1) { pageNo=1; };
			if (pageNo>MAX_LIMIT) { pageNo=MAX_LIMIT; };
			String query = "select * from "+tableName+" where "+field+" like ?";
			if (!StringUtils.isBlank(orderBy)) {
				query += " order by "+orderBy;
			}
			query += " limit "+limit+" offset "+((pageNo-1)*limit);
			statement = conn.prepareStatement( query );
			statement.setString(1, "%"+value+"%");
			resultSet = statement.executeQuery();
			while( resultSet.next() ) {
				T bean = ReflectUtil.getNewBean(clazz);
				bean.setId( resultSet.getInt( BaseBean.FIELD_ID ) );
				populateBeanFromResultSet(bean, resultSet);
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
		String tableName = null;
		try {
			TableName annotation = (TableName) clazz.getConstructor(null).getAnnotation(TableName.class);
			if (annotation!=null) {
				tableName = annotation.name();
			}
		} catch (Exception e) {
			// ignore
		}
		if (StringUtils.isEmpty(tableName)) {
			tableName = clazz.getSimpleName().toLowerCase();
		}
		return tableName;
	}

	@Override
	public Class<T> getBeanClass() {
		return clazz;
	}

	@Override
	public List<String> getBeanFieldNames() {
		return ReflectUtil.getBeanFieldNames(clazz);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void populateBeanFromResultSet(T bean, ResultSet rs) throws Exception {
		Map<Method,Class> methods = ReflectUtil.getPublicSetterMethods(clazz);
		String columnName = bean.getNameColumn();
		for (Method method : methods.keySet()) {
			Class classType = methods.get(method);
            LOG.debug("method="+method.getName()+" paramClass.getSimpleName()="+classType.getSimpleName());
			String fieldName = ReflectUtil.getFieldName(method);

			// populate the display value
			if (fieldName.equals(columnName)) {
				bean.setDisplayValue(rs.getString(fieldName));
			}

			if (ReflectUtil.isBean(classType)) {
				// Handle BaseBeans
				int id = rs.getInt(fieldName);
				ReflectUtil.invokeSetterMethodForBeanType(bean, method, classType, id);
			} else if (ReflectUtil.isCollection(classType)) {
				// Handle Collections
				String value = rs.getString(fieldName);
				ReflectUtil.invokeSetterMethodForCollection(bean, method, classType, value);
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
					}
				} catch (SQLException e) {
					if (!fieldName.equals(BaseBean.FIELD_DISPLAYVALUE)) { // ignore "displayvalue", as this is custom to BaseBean
						throw e;
					}
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public PreparedStatement prepareStatementForSave(Connection conn, T bean) throws Exception {
		boolean updateStmt = false;
		List<String> columns = new ArrayList<String>();
		Map<Method,Class> methods = ReflectUtil.getPublicGetterMethods(clazz);
		for (Method method : methods.keySet()) {
			String fieldName = ReflectUtil.getFieldName(method);
			columns.add(fieldName);
		}
		String query = "insert into "+tableName+" "+ getInsertIntoColumnsSQL(columns);
		if (bean.getId()>0) {
			updateStmt = true;
			query = "update "+tableName+" set "+ getUpdateColumnsSQL(columns)+" where id=?";
		}
        LOG.debug("query="+query+" columns="+columns);
		PreparedStatement preparedStatement = conn.prepareStatement( query );

		int index = 0;
		for (Method method : methods.keySet()) {
			String fieldName = ReflectUtil.getFieldName(method);
			Class classType = methods.get(method);
			Object result = method.invoke(bean);
			LOG.debug("m="+method.getName() + " classType="+classType +" result="+result);
			if (ReflectUtil.isBean(classType)) {
				// Handle BaseBeans
				if (result==null) {
					LOG.debug("result.class=null");
					preparedStatement.setInt(++index, 0);
				} else if (result instanceof BaseBean) {
					LOG.debug("result.class="+result.getClass().getSimpleName());
					preparedStatement.setInt(++index, ((BaseBean)result).getId());
				}
			} else if (ReflectUtil.isCollection(classType)) {
				// Handle Collections
				if (result==null) {
					LOG.debug("result.class=null");
					preparedStatement.setObject(++index, null);
				} else {
					LOG.debug("result.class="+result.getClass().getSimpleName());
					Collection c = (Collection) result;
					String resString = "";
					for (Object o : c) {
						if (resString.length()>0) {
							resString+=",";
						}
						if (o instanceof BaseBean) {
							resString += ((BaseBean)o).getId();
						} else {
							resString += o.toString();
						}
					}
					preparedStatement.setInt(++index, ((BaseBean)result).getId());
				}
			} else {
				// Handle primitives
				if (result==null) {
					LOG.debug("result.class=null");
					preparedStatement.setObject(++index, null);
				} else {
					LOG.debug("result.class="+result.getClass().getSimpleName());
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
					}
				}
			}
		}
		if (updateStmt) {
			preparedStatement.setInt(++index, bean.getId());
		}
		return preparedStatement;
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
}
