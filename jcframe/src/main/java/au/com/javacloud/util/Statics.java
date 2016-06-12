package au.com.javacloud.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import au.com.javacloud.annotation.BeanClass;
import au.com.javacloud.auth.AuthService;
import au.com.javacloud.auth.BaseAuthServiceImpl;
import au.com.javacloud.controller.BaseController;
import au.com.javacloud.controller.BaseControllerImpl;
import au.com.javacloud.dao.BaseDAO;
import au.com.javacloud.dao.BaseDAOImpl;
import au.com.javacloud.dao.BaseDataSource;
import au.com.javacloud.model.BaseBean;

public class Statics {

	private static final Logger LOG = Logger.getLogger(Statics.class);

    private static final String DEFAULT_PACKAGE_NAME = "au.com.javacloud";
    private static final String DEFAULT_JC_CONFIG_FILE = "jc.properties";
    private static final String DEFAULT_DB_CONFIG_FILE = "db.properties";
    private static final String DEFAULT_AUTH_CLASS = "au.com.javacloud.auth.BaseAuthServiceImpl";
    private static final String DEFAULT_DS_CLASS = "au.com.javacloud.dao.BaseDataSource";
    
    private static final String PROP_PACKAGE_NAME = "package.name";
    private static final String PROP_AUTH_CLASS = "auth.class";
    private static final String PROP_DS_CLASS = "ds.class";
	private static final String PROP_DS_CONFIG_FILE = "ds.config.file";

    private static Map<Class<? extends BaseBean>,BaseDAO<? extends BaseBean>> daoMap = new HashMap<Class<? extends BaseBean>,BaseDAO<? extends BaseBean>>();
    private static Map<Class<? extends BaseBean>,BaseController<? extends BaseBean, ?>> controllerMap = new HashMap<Class<? extends BaseBean>,BaseController<? extends BaseBean,?>>();
	private static Map<String,Class<? extends BaseBean>> classTypeMap = new HashMap<String,Class<? extends BaseBean>>();
    private static AuthService authService;
    private static DataSource dataSource;
	private static String packageName;

    static {
		try {
			Properties properties = ResourceUtil.loadProperties(DEFAULT_JC_CONFIG_FILE);
			packageName = properties.getProperty(PROP_PACKAGE_NAME,DEFAULT_PACKAGE_NAME);
			String authClassName = properties.getProperty(PROP_AUTH_CLASS,DEFAULT_AUTH_CLASS);
			String dsClassName = properties.getProperty(PROP_DS_CLASS,DEFAULT_DS_CLASS);
			String dsPropertiesFilename = properties.getProperty(PROP_DS_CONFIG_FILE,DEFAULT_DB_CONFIG_FILE);

			Properties dbProperties = ResourceUtil.loadProperties(dsPropertiesFilename);

			// Register the authService
			try {
				LOG.info("authClassName="+authClassName);
				authService = (AuthService)Class.forName(authClassName).newInstance();
			} catch (Exception e) {
				LOG.error(e,e);
				authService = new BaseAuthServiceImpl();
			}
			LOG.info("authService="+authService);

			// Register the dataSource
			try {
				LOG.info("dsClassName="+dsClassName);
				dataSource = (DataSource)Class.forName(dsClassName).newInstance();
				if (dataSource instanceof BaseDataSource) {
					((BaseDataSource)dataSource).setProperties(dbProperties);
				}
			} catch (Exception e) {
				LOG.error(e,e);
				dataSource = new BaseDataSource();
				((BaseDataSource)dataSource).setProperties(dbProperties);
			}
			LOG.info("dataSource="+dataSource);

			// Find all the beanClassTypes
			try {
				LOG.info("packageName="+packageName);
				// Initialise default classTypeMap, daoMap and controllerMap
				List<Class> beanClassTypes = ReflectUtil.getClasses(packageName, BaseBean.class, true);
				for (Class classType : beanClassTypes) {
					classTypeMap.put(classType.getSimpleName().toLowerCase(),classType);
					BaseDAO dao = new BaseDAOImpl<>();
					dao.init(classType, dataSource);
					daoMap.put(classType, dao);
					BaseController controller = new BaseControllerImpl();
					controller.init(classType, authService);
					controllerMap.put(classType, controller);
				}
				LOG.info("classTypeMap="+classTypeMap);
				// Insert custom daos
				List<Class> classTypes = ReflectUtil.getClasses(packageName, BaseDAO.class, true);
				for (Class classType : classTypes) {
					Class beanClassType = getClassTypeFromBeanClassAnnotation(classType);
					if (beanClassType!=null) {
						BaseDAO dao = (BaseDAO) classType.newInstance();
						dao.init(beanClassType, dataSource);
						daoMap.put(beanClassType, dao);
					}
				}
				LOG.info("daoMap="+daoMap);
				// Insert custom controllers
				classTypes = ReflectUtil.getClasses(packageName, BaseController.class, true);
				for (Class classType : classTypes) {
					Class beanClassType = getClassTypeFromBeanClassAnnotation(classType);
					if (beanClassType!=null) {
						BaseController controller = (BaseController) classType.newInstance();
						controller.init(beanClassType, authService);
						controllerMap.put(beanClassType, controller);
					}
				}
				LOG.info("controllerMap="+controllerMap);
			} catch (Exception e) {
				LOG.error(e, e);
			}
			LOG.info("getClassTypeMap="+classTypeMap);
		} catch(Exception e) {
			LOG.error(e,e);
		}
    }

    private static <T extends BaseBean> Class<T> getClassTypeFromBeanClassAnnotation(Class controllerClassType) throws ClassNotFoundException, NoSuchMethodException {
		if (controllerClassType.isAnnotationPresent(BeanClass.class)) {
			BeanClass baseClassAnnotation = (BeanClass) controllerClassType.getAnnotation(BeanClass.class);
			if (baseClassAnnotation!=null) {
				Class<T> classType = (Class<T>) baseClassAnnotation.bean();
				return classType;
			}
		}
		return null;
	}
    
    public static BaseController<? extends BaseBean,?> getControllerForBeanName(String beanName) { 
	    if (!StringUtils.isBlank(beanName)) {
	        Class<? extends BaseBean> classType = classTypeMap.get(beanName);
	        if (classType!=null) {
		        BaseController<? extends BaseBean,?> baseController = controllerMap.get(classType);
		        return baseController;
	        }
	    }
	    return null;
    }

	public static AuthService getAuthService() {
		return authService;
	}

	public static DataSource getDataSource() {
		return dataSource;
	}
    
    public static Map<Class<? extends BaseBean>,BaseDAO<? extends BaseBean>> getDaoMap() {
    	return daoMap;
    }
    
    public static Map<Class<? extends BaseBean>,BaseController<? extends BaseBean,?>> getControllerMap() {
    	return controllerMap;
    }

	public static Map<String, Class<? extends BaseBean>> getClassTypeMap() {
		return classTypeMap;
	}
}
