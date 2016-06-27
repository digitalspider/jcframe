package au.com.javacloud.jcframe.dao;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;

public class BaseDataSource implements DataSource {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(BaseDataSource.class);
    
    public static final String PROP_DRIVER = "driver";
    public static final String PROP_URL = "url";
    public static final String PROP_USER = "username";
    public static final String PROP_PASSWORD = "password";

	private String realPath;
	private String url;
	private String driver;

	private Properties properties;
	private static Connection conn;

	public void setProperties(Properties properties) {
		this.properties = properties;
		this.driver = properties.getProperty(PROP_DRIVER);
		this.url = properties.getProperty(PROP_URL);
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Connection getConnection() throws SQLException {
    	try {
	        if( conn != null && !conn.isClosed()) {
	            return conn;
	        }
    	} catch ( Exception e ) {
    		// ignore
    	}

        String username = properties.getProperty( PROP_USER );
        String password = properties.getProperty( PROP_PASSWORD );
        return getConnection(username,password);
	}            


	@Override
	public Connection getConnection(String username, String password) throws SQLException {
    	try {
	        if( conn != null && !conn.isClosed()) {
	            return conn;
	        }
    	} catch ( Exception e ) {
    		// ignore
    	}

    	try {
	        driver = properties.getProperty( PROP_DRIVER );
	        url = injectRealPath(properties.getProperty( PROP_URL ));
			LOG.debug("url="+url);
	        Class.forName( driver );
	        conn = DriverManager.getConnection( url, username, password );
        } catch (Exception e) {
            LOG.error(e,e);
        }

        return conn;
	}

	public String getDriver() {
		return driver;
	}

	public String getUrl() {
		return url;
	}

	public String getRealPath() {
		return realPath;
	}

	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

	public String injectRealPath(String url) {
		if (url.contains("${REALPATH}") && StringUtils.isNoneBlank(realPath)) {
			url = url.replace("${REALPATH}",realPath);
		}
		return url;
	}

	public String toString() {
		return getClass().getSimpleName()+" driver="+driver+" url="+url;
	}
}
