package au.com.javacloud.jcframe.service;

import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import au.com.javacloud.jcframe.auth.AuthService;
import au.com.javacloud.jcframe.auth.BaseAuthServiceImpl;
import au.com.javacloud.jcframe.dao.BaseDataSource;
import au.com.javacloud.jcframe.util.DisplayDateFormat;
import au.com.javacloud.jcframe.util.ResourceUtil;
import au.com.javacloud.jcframe.view.ViewGenerator;
import au.com.javacloud.jcframe.view.ViewGeneratorImpl;

/**
 * Created by david on 27/06/16.
 */
public class ServiceLoaderServiceImpl implements ServiceLoaderService {

    private static final Logger LOG = Logger.getLogger(ServiceLoaderServiceImpl.class);

    public static String DATEFORMATDISPLAY;
    public static String DATEFORMATDB;
    protected static DateFormat displayDateFormat;
    protected static DateFormat dbDateFormat;

    protected static Map<String, Object> serviceMap = new HashMap<String, Object>();
    protected static AuthService authService;
    protected static ViewGenerator viewGenerator;
    protected static DataSource dataSource;
    protected static DAOLookupService daoLookupService;

    protected Properties properties;

    @Override
    public void init(Properties properties) {
        this.properties = properties;
    }

    @Override
    public <T> T getService(String className, T defaultServiceImpl) {
        if (className!=null && properties!=null) {
            T service = (T) serviceMap.get(className);
            if (service!=null) {
                return service;
            }
            try {
                LOG.info("className=" + className);
                service = (T) Class.forName(className).newInstance();
            } catch (Exception e) {
                LOG.error(e, e);
                service = defaultServiceImpl;
            }
            serviceMap.put(className,service);
            LOG.info("service=" + service);
        }
        return null;
    }

    public DateFormat getDatabaseDateFormat() {
        if (dbDateFormat!=null) {
            return dbDateFormat;
        }
        DATEFORMATDB = properties.getProperty(PROP_DATEFORMAT_DB,DEFAULT_DATEFORMAT_DB);
        // Get the dateFormats
        LOG.info("DATEFORMATDB="+DATEFORMATDB);
        try {
            dbDateFormat = new DisplayDateFormat(DATEFORMATDB);
        } catch (Exception e) {
            LOG.error(e,e);
            dbDateFormat = new DisplayDateFormat(DEFAULT_DATEFORMAT_DB);
        }
        LOG.info("dbDateFormat="+dbDateFormat);
        return dbDateFormat;
    }

    public DateFormat getDisplayDateFormat() {
        if (displayDateFormat!=null) {
            return displayDateFormat;
        }
        DATEFORMATDISPLAY = properties.getProperty(PROP_DATEFORMAT_DISPLAY,DEFAULT_DATEFORMAT_DISPLAY);
        LOG.info("DATEFORMATDISPLAY="+DATEFORMATDISPLAY);
        try {
            displayDateFormat = new DisplayDateFormat(DATEFORMATDISPLAY);
        } catch (Exception e) {
            LOG.error(e,e);
            displayDateFormat = new DisplayDateFormat(DEFAULT_DATEFORMAT_DISPLAY);
        }
        LOG.info("displayDateFormat="+displayDateFormat);
        return displayDateFormat;
    }

    public AuthService getAuthService() {
        if (authService!=null) {
            return authService;
        }
        String className = properties.getProperty(PROP_AUTH_CLASS,DEFAULT_AUTH_CLASS);
        authService = getService(className, new BaseAuthServiceImpl());
        return authService;
    }

    public DAOLookupService getDAOLookupService() {
        if (daoLookupService!=null) {
            return daoLookupService;
        }
        String className = properties.getProperty(PROP_DAOLOOKUP_CLASS,DEFAULT_DAOLOOKUP_CLASS);
        daoLookupService = getService(className, new DAOLookupServiceImpl());
        return daoLookupService;
    }

    public ViewGenerator getViewGeneratorService() {
        if (viewGenerator!=null) {
            return viewGenerator;
        }
        String className = properties.getProperty(PROP_VIEWGEN_CLASS,DEFAULT_VIEWGEN_CLASS);
        viewGenerator = getService(className, new ViewGeneratorImpl());
        return viewGenerator;
    }

    public DataSource getDataSource() {
        if (dataSource!=null) {
            return dataSource;
        }
        String className = properties.getProperty(PROP_DS_CLASS,DEFAULT_DS_CLASS);
        dataSource = getService(className, new BaseDataSource());
        if (dataSource instanceof BaseDataSource) {
            BaseDataSource baseDataSource = (BaseDataSource)dataSource;
            if (baseDataSource.getDriver()==null) {
                String dsPropertiesFilename = properties.getProperty(PROP_DS_CONFIG_FILE, DEFAULT_DB_CONFIG_FILE);
                Properties dbProperties = ResourceUtil.loadProperties(dsPropertiesFilename);
                baseDataSource.setProperties(dbProperties);
            }
        }
        return dataSource;
    }
}
