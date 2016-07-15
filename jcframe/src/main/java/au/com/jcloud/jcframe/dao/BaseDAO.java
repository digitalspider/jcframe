package au.com.jcloud.jcframe.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.sql.DataSource;

import au.com.jcloud.jcframe.model.BaseBean;
import au.com.jcloud.jcframe.service.DAOLookup;
import au.com.jcloud.jcframe.util.FieldMetaData;

/**
 * Created by david on 22/05/16.
 */
public interface BaseDAO<ID, Bean extends BaseBean<ID>> {

    public static final int MAX_LIMIT=1000;
    public static final int DEFAULT_LIMIT = 50;
    public static final int DEFAULT_DAOLOOKUP_LIMIT = 200;
	
    public DataSource getDataSource();
    public String getTableName();
    public Class<Bean> getBeanClass();
    public void populateBeanFromResultSet(Bean bean, ResultSet rs) throws Exception;
    public PreparedStatementWrapper prepareStatementForSave(Connection conn, Bean bean) throws Exception;
    public void executeM2MUpdate(Connection conn, Bean bean) throws Exception;
    public void executeM2MPopulate(Bean bean, FieldMetaData fieldMetaData) throws Exception;
    public void executeLinkFieldPopulate(Bean bean, FieldMetaData fieldMetaData) throws Exception;
    public List<String> getBeanFieldNames();
    
    public void saveOrUpdate(Bean bean) throws Exception;
    public void saveOrUpdate(List<Bean> beanList) throws Exception;
    public int count() throws Exception;
    public int count(String field, String value, boolean exact) throws Exception;
    public List<Bean> getAll(int pageNo, boolean populateBean) throws Exception;
    public List<Bean> getLookupList() throws Exception;
    public Bean get(ID id, boolean populateBean) throws Exception;
    public List<Bean> get(List<ID> idList, boolean populateBeans) throws Exception;
    public void delete(ID id) throws Exception;
    public void delete(List<ID> idList) throws Exception;
    public List<Bean> find(String field, String value, int pageNo, boolean exact, boolean populateBean) throws Exception;

    public ID getIdFromResultSet(ResultSet resultSet, String idColumnName) throws SQLException;

    public void init(Class<Bean> clazz) throws IOException;
    public void init(Class<Bean> clazz, DataSource dataSource, DAOLookup daoLookupService) throws IOException;
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
