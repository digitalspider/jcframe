package au.com.javacloud.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import au.com.javacloud.annotation.BeanClass;
import au.com.javacloud.annotation.HiddenBean;
import au.com.javacloud.annotation.Secure;
import au.com.javacloud.auth.AuthService;
import au.com.javacloud.auth.BaseAuthServiceImpl;
import au.com.javacloud.controller.BaseController;
import au.com.javacloud.controller.BaseControllerImpl;
import au.com.javacloud.dao.BaseDAO;
import au.com.javacloud.dao.BaseDAOImpl;
import au.com.javacloud.dao.BaseDataSource;
import au.com.javacloud.model.BaseBean;
import au.com.javacloud.view.ViewGenerator;
import au.com.javacloud.view.ViewGeneratorImpl;

public class Statics {

	private static final Logger LOG = Logger.getLogger(Statics.class);

    private static final String DEFAULT_PACKAGE_NAME = "au.com.javacloud";
    private static final String DEFAULT_JC_CONFIG_FILE = "jc.properties";
    private static final String DEFAULT_DB_CONFIG_FILE = "db.properties";
    private static final String DEFAULT_AUTH_CLASS = "au.com.javacloud.auth.BaseAuthServiceImpl";
	private static final String DEFAULT_VIEWGEN_CLASS = "au.com.javacloud.view.ViewGeneratorImpl";
    private static final String DEFAULT_DS_CLASS = "au.com.javacloud.dao.BaseDataSource";
    
    private static final String PROP_PACKAGE_NAME = "package.name";
    private static final String PROP_AUTH_CLASS = "auth.class";
	private static final String PROP_VIEWGEN_CLASS = "viewgen.class";
    private static final String PROP_DS_CLASS = "ds.class";
	private static final String PROP_DS_CONFIG_FILE = "ds.config.file";

    private static Map<Class<? extends BaseBean>,BaseDAO<? extends BaseBean>> daoMap = new HashMap<Class<? extends BaseBean>,BaseDAO<? extends BaseBean>>();
    private static Map<Class<? extends BaseBean>,BaseController<? extends BaseBean, ?>> controllerMap = new HashMap<Class<? extends BaseBean>,BaseController<? extends BaseBean,?>>();
	private static Map<String,Class<? extends BaseBean>> classTypeMap = new HashMap<String,Class<? extends BaseBean>>();
	private static Map<String,Class<? extends BaseBean>> secureClassTypeMap = new HashMap<String,Class<? extends BaseBean>>();
	private static Map<String,Class<? extends BaseBean>> hiddenClassTypeMap = new HashMap<String,Class<? extends BaseBean>>();
	private static Map<String,Class<? extends BaseBean>> hiddenSecureClassTypeMap = new HashMap<String,Class<? extends BaseBean>>();

	private static String packageName;
	private static AuthService authService;
	private static ViewGenerator viewGenerator;
    private static DataSource dataSource;

    static {
		try {
			Properties properties = ResourceUtil.loadProperties(DEFAULT_JC_CONFIG_FILE);
			packageName = properties.getProperty(PROP_PACKAGE_NAME,DEFAULT_PACKAGE_NAME);
			String authClassName = properties.getProperty(PROP_AUTH_CLASS,DEFAULT_AUTH_CLASS);
			String viewGeneratorClassName = properties.getProperty(PROP_VIEWGEN_CLASS,DEFAULT_VIEWGEN_CLASS);
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

			// Register the viewGenerator
			try {
				LOG.info("viewGeneratorClassName="+viewGeneratorClassName);
				viewGenerator = (ViewGenerator) Class.forName(viewGeneratorClassName).newInstance();
			} catch (Exception e) {
				LOG.error(e,e);
				viewGenerator = new ViewGeneratorImpl();
			}
			LOG.info("viewGenerator="+viewGenerator);

			// Find all the beanClassTypes
			try {
				LOG.info("packageName="+packageName);
				// Initialise default classTypeMap, daoMap and controllerMap
				List<Class> beanClassTypes = ReflectUtil.getClasses(packageName, BaseBean.class, true);
				for (Class classType : beanClassTypes) {
					if (!classType.isAnnotationPresent(HiddenBean.class)) {
						if (!classType.isAnnotationPresent(Secure.class)) {
							classTypeMap.put(classType.getSimpleName().toLowerCase(), classType);
						}
						secureClassTypeMap.put(classType.getSimpleName().toLowerCase(), classType);
					} else {
						if (!classType.isAnnotationPresent(Secure.class)) {
							hiddenClassTypeMap.put(classType.getSimpleName().toLowerCase(), classType);
						}
						hiddenSecureClassTypeMap.put(classType.getSimpleName().toLowerCase(), classType);
					}
					BaseDAO<? extends BaseBean> dao = new BaseDAOImpl<>();
					dao.init(classType, dataSource);
					daoMap.put(classType, dao);
					BaseController controller = new BaseControllerImpl();
					controller.init(classType, authService);
					controllerMap.put(classType, controller);
				}
				LOG.info("classTypeMap="+classTypeMap);
				
				// Find custom daos
				List<Class> classTypes = ReflectUtil.getClasses(packageName, BaseDAO.class, true);
				// Allow for a complete BaseDAO override!
				for (Class classType : classTypes) {
					Class beanClassType = getClassTypeFromBeanClassAnnotation(classType);
					if (beanClassType!=null && beanClassType.equals(BaseBean.class)) {
						BaseDAO overrideDao = (BaseDAO) classType.newInstance();
						overrideDao.init(beanClassType, dataSource);
						for (Class key : daoMap.keySet()) {
							daoMap.put(key, overrideDao);
						}
						break;
					}
				}
				// Insert custom daos
				for (Class classType : classTypes) {
					Class beanClassType = getClassTypeFromBeanClassAnnotation(classType);
					if (beanClassType!=null) {
						BaseDAO dao = (BaseDAO) classType.newInstance();
						dao.init(beanClassType, dataSource);
						daoMap.put(beanClassType, dao);
					}
				}
				LOG.info("daoMap="+daoMap);
				
				// Find custom controllers
				classTypes = ReflectUtil.getClasses(packageName, BaseController.class, true);
				// Allow for a complete BaseController override!
				for (Class classType : classTypes) {
					Class beanClassType = getClassTypeFromBeanClassAnnotation(classType);
					if (beanClassType!=null && beanClassType.equals(BaseBean.class)) {
						BaseController overrideController = (BaseController) classType.newInstance();
						overrideController.init(beanClassType, authService);
						for (Class key : controllerMap.keySet()) {
							controllerMap.put(key, overrideController);
						}
						break;
					}
				}
				// Insert custom controllers
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
    
    public static BaseController<? extends BaseBean,?> getControllerForBeanName(String beanName, HttpServletRequest request) {
	    if (!StringUtils.isBlank(beanName)) {
	        Class<? extends BaseBean> classType = getClassTypeMap(request).get(beanName);
			if (classType==null) {
				classType = getHiddenClassTypeMap(request).get(beanName);
			}
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

	public static ViewGenerator getViewGenerator() {
		return viewGenerator;
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

	public static Map<String, Class<? extends BaseBean>> getClassTypeMap(HttpServletRequest request) {
		if (authService.isAuthenticated(request)) {
			return secureClassTypeMap;
		}
		return classTypeMap;
	}

	public static Map<String, Class<? extends BaseBean>> getSecureClassTypeMap() {
		return secureClassTypeMap;
	}

	private static Map<String, Class<? extends BaseBean>> getHiddenClassTypeMap(HttpServletRequest request) {
		if (authService.isAuthenticated(request)) {
			return hiddenSecureClassTypeMap;
		}
		return hiddenClassTypeMap;
	}

}
