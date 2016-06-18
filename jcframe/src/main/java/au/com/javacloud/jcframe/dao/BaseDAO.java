package au.com.javacloud.jcframe.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
/**
 * Created by david on 22/05/16.
 */
import java.util.List;

import javax.servlet.ServletConfig;
import javax.sql.DataSource;

import au.com.javacloud.jcframe.controller.BaseController;
import au.com.javacloud.jcframe.model.BaseBean;

public interface BaseDAO<T extends BaseBean> {

    public static final int MAX_LIMIT=1000;
    public static final int DEFAULT_LIMIT = 50;
	
    public String getTableName();
    public Class<T> getBeanClass();
    public void populateBeanFromResultSet(T bean, ResultSet rs) throws Exception;
    public PreparedStatementWrapper prepareStatementForSave(Connection conn, T bean) throws Exception;
    public List<String> getBeanFieldNames();
    
    public void saveOrUpdate(T bean) throws Exception;
    public int count() throws Exception;
    public int count(String field, String value) throws Exception;
    public List<T> getAll(int pageNo) throws Exception;
    public List<T> getLookup() throws Exception;
    public T getLookup(int id) throws Exception;
    public T get(int id) throws Exception;
    public void delete(int beanId) throws Exception;
    public List<T> find(String field, String value, int pageNo) throws Exception;

    public void init(Class<T> clazz, DataSource dataSource);
    public void initHttp(ServletConfig config);
    public void registerController(BaseController controller);
    public Connection getConnection() throws SQLException;

    public void toggleOrderBy(String orderBy);
    public void setOrderBy(String orderBy, boolean isAsc);
    public String getOrderBy();

    public void setLimit(int limit);
    public int getLimit();

    public DateFormat getDateFormat();
    public void setDateFormat(DateFormat dateFormat);
}
