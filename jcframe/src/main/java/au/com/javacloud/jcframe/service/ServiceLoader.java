package au.com.javacloud.jcframe.service;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Properties;

import javax.sql.DataSource;

import au.com.javacloud.jcframe.auth.AuthService;
import au.com.javacloud.jcframe.view.ViewGenerator;

/**
 * Created by david on 27/06/16.
 */
public interface ServiceLoader {

    public static final String DEFAULT_PACKAGE_NAME = "au.com.javacloud.jcframe";
    public static final String DEFAULT_DB_CONFIG_FILE = "db.properties";
    public static final String DEFAULT_AUTH_CLASS = DEFAULT_PACKAGE_NAME+".auth.BaseAuthServiceImpl";
    public static final String DEFAULT_DAOLOOKUP_CLASS = DEFAULT_PACKAGE_NAME+".service.DAOLookupImpl";
    public static final String DEFAULT_VIEWGEN_CLASS = DEFAULT_PACKAGE_NAME+".view.ViewGeneratorImpl";
    public static final String DEFAULT_DS_CLASS = DEFAULT_PACKAGE_NAME+".dao.BaseDataSource";
    public static final String DEFAULT_DATEFORMAT_DISPLAY = "dd/MM/yyyy HH:mm";
    public static final String DEFAULT_DATEFORMAT_DB = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DB_SCHEMA = "default";

    public static final String PROP_AUTH_CLASS = "auth.class";
    public static final String PROP_DAOLOOKUP_CLASS = "daolookup.class";
    public static final String PROP_VIEWGEN_CLASS = "viewgen.class";
    public static final String PROP_DS_CLASS = "ds.class";
    public static final String PROP_DS_CONFIG_FILE = "ds.config.file";
    public static final String PROP_DATEFORMAT_DISPLAY = "date.format.display";
    public static final String PROP_DATEFORMAT_DB = "date.format.db";

    public void init(Properties properties);

    public <T> T getService(String className, T defaultImpl);

    public AuthService getAuthService();

    public DAOLookup getDAOLookupService();

    public ViewGenerator getViewGeneratorService();

    public DataSource getDataSource() throws IOException;

    public DataSource getDataSource(String schema) throws IOException;

    public DateFormat getDatabaseDateFormat();

    public DateFormat getDisplayDateFormat();
}
