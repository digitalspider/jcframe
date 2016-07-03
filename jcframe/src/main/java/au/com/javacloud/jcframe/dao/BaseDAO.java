package au.com.javacloud.jcframe.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.sql.DataSource;

import au.com.javacloud.jcframe.model.BaseBean;
import au.com.javacloud.jcframe.service.DAOLookup;
import au.com.javacloud.jcframe.util.FieldMetaData;

/**
 * Created by david on 22/05/16.
 */
public interface BaseDAO<ID,T extends BaseBean<ID>> {

    public static final int MAX_LIMIT=1000;
    public static final int DEFAULT_LIMIT = 50;
    public static final int DEFAULT_DAOLOOKUP_LIMIT = 200;
	
    public DataSource getDataSource();
    public String getTableName();
    public Class<T> getBeanClass();
    public void populateBeanFromResultSet(T bean, ResultSet rs) throws Exception;
    public PreparedStatementWrapper prepareStatementForSave(Connection conn, T bean) throws Exception;
    public void executeM2MUpdate(Connection conn, T bean) throws Exception;
    public void executeM2MPopulate(T bean, FieldMetaData fieldMetaData) throws Exception;
    public void executeLinkFieldPopulate(T bean, FieldMetaData fieldMetaData) throws Exception;
    public List<String> getBeanFieldNames();
    
    public void saveOrUpdate(T bean) throws Exception;
    public int count() throws Exception;
    public int count(String field, String value) throws Exception;
    public List<T> getAll(int pageNo, boolean populateBean) throws Exception;
    public List<T> getLookupList() throws Exception;
    public T getLookup(ID id) throws Exception;
    public T get(ID id, boolean populateBean) throws Exception;
    public void delete(ID id) throws Exception;
    public List<T> find(String field, String value, int pageNo, boolean exact, boolean populateBean) throws Exception;

    public ID getIdFromResultSet(ResultSet resultSet) throws SQLException;

    public void init(Class<T> clazz) throws IOException;
    public void init(Class<T> clazz, DataSource dataSource, DAOLookup daoLookupService) throws IOException;
    public void initHttp(ServletConfig config);
    public Connection getConnection() throws SQLException;

    public void toggleOrderBy(String orderBy);
    public void setOrderBy(String orderBy, boolean isAsc);
    public String getOrderBy();

    public void setLimit(int limit);
    public int getLimit();

    public DateFormat getDateFormat();
    public void setDateFormat(DateFormat dateFormat);
}
