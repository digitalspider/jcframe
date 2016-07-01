package au.com.javacloud.jcframe.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import au.com.javacloud.jcframe.annotation.BeanClass;
import au.com.javacloud.jcframe.annotation.HiddenBean;
import au.com.javacloud.jcframe.annotation.Secure;
import au.com.javacloud.jcframe.auth.AuthService;
import au.com.javacloud.jcframe.controller.BaseController;
import au.com.javacloud.jcframe.controller.BaseControllerImpl;
import au.com.javacloud.jcframe.dao.BaseDAO;
import au.com.javacloud.jcframe.dao.BaseDAOImpl;
import au.com.javacloud.jcframe.model.BaseBean;
import au.com.javacloud.jcframe.service.ServiceLoader;
import au.com.javacloud.jcframe.service.ServiceLoaderImpl;

public class Statics {

	private static final Logger LOG = Logger.getLogger(Statics.class);

    private static final String DEFAULT_PACKAGE_NAME = "au.com.javacloud.jcframe";
    private static final String DEFAULT_JC_CONFIG_FILE = "jc.properties";
	private static final String DEFAULT_SERVICELOADER_CLASS = DEFAULT_PACKAGE_NAME+".service.ServiceLoaderImpl";

    private static final String PROP_PACKAGE_NAME = "package.name";
	private static final String PROP_SERVICELOADER_CLASS = "serviceloader.class";

    private static Map<Class<? extends BaseBean>,BaseDAO<? extends BaseBean>> daoMap = new HashMap<Class<? extends BaseBean>,BaseDAO<? extends BaseBean>>();
    private static Map<Class<? extends BaseBean>,BaseController<? extends BaseBean, ?>> controllerMap = new HashMap<Class<? extends BaseBean>,BaseController<? extends BaseBean,?>>();
	private static Map<String,Class<? extends BaseBean>> classTypeMap = new HashMap<String,Class<? extends BaseBean>>();
	private static Map<String,Class<? extends BaseBean>> secureClassTypeMap = new HashMap<String,Class<? extends BaseBean>>();
	private static Map<String,Class<? extends BaseBean>> hiddenClassTypeMap = new HashMap<String,Class<? extends BaseBean>>();
	private static Map<String,Class<? extends BaseBean>> hiddenSecureClassTypeMap = new HashMap<String,Class<? extends BaseBean>>();

	private static ServiceLoader serviceLoader;

    static {
		try {
			Properties properties = ResourceUtil.loadProperties(DEFAULT_JC_CONFIG_FILE);
			String packageName = properties.getProperty(PROP_PACKAGE_NAME,DEFAULT_PACKAGE_NAME);
			String serviceLoaderClassName = properties.getProperty(PROP_SERVICELOADER_CLASS,DEFAULT_SERVICELOADER_CLASS);

			try {
				LOG.info("serviceLoaderClassName=" + serviceLoaderClassName);
				serviceLoader = (ServiceLoader) Class.forName(serviceLoaderClassName).newInstance();
				serviceLoader.init(properties);
			} catch (Exception e) {
				LOG.error(e, e);
				serviceLoader = new ServiceLoaderImpl();
				serviceLoader.init(properties);
			}
			LOG.info("serviceLoader=" + serviceLoader);

			// Find all the beanClassTypes
			try {
				LOG.info("packageName="+packageName);
				// Initialise default classTypeMap, daoMap and controllerMap
				List<Class> beanClassTypes = ReflectUtil.getClasses(DEFAULT_PACKAGE_NAME, BaseBean.class, true);
				if (!packageName.equals(DEFAULT_PACKAGE_NAME)) {
					beanClassTypes.addAll(ReflectUtil.getClasses(packageName, BaseBean.class, true));
				}
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
					dao.init(classType);
					daoMap.put(classType, dao);
					BaseController controller = new BaseControllerImpl();
					controller.init(classType);
					controllerMap.put(classType, controller);
				}
				LOG.info("classTypeMap="+classTypeMap);
				
				// Find custom daos
				List<Class> classTypes = ReflectUtil.getClasses(DEFAULT_PACKAGE_NAME, BaseDAO.class, true);
				if (!packageName.equals(DEFAULT_PACKAGE_NAME)) {
					classTypes.addAll(ReflectUtil.getClasses(packageName, BaseDAO.class, true));
				}
				// Allow for a complete BaseDAO override!
				for (Class classType : classTypes) {
					Class beanClassType = getClassTypeFromBeanClassAnnotation(classType);
					if (beanClassType!=null && beanClassType.equals(BaseBean.class)) {
						BaseDAO overrideDao = (BaseDAO) classType.newInstance();
						overrideDao.init(beanClassType);
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
						dao.init(beanClassType);
						daoMap.put(beanClassType, dao);
					}
				}
				LOG.info("daoMap="+daoMap);
				
				// Find custom controllers
				classTypes = ReflectUtil.getClasses(DEFAULT_PACKAGE_NAME, BaseController.class, true);
				if (!packageName.equals(DEFAULT_PACKAGE_NAME)) {
					classTypes.addAll(ReflectUtil.getClasses(packageName, BaseController.class, true));
				}
				// Allow for a complete BaseController override!
				for (Class classType : classTypes) {
					Class beanClassType = getClassTypeFromBeanClassAnnotation(classType);
					if (beanClassType!=null && beanClassType.equals(BaseBean.class)) {
						BaseController overrideController = (BaseController) classType.newInstance();
						overrideController.init(beanClassType);
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
						controller.init(beanClassType);
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
				Class<T> classType = (Class<T>) baseClassAnnotation.value();
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

    public static Map<Class<? extends BaseBean>,BaseDAO<? extends BaseBean>> getDaoMap() {
    	return daoMap;
    }
    
    public static Map<Class<? extends BaseBean>,BaseController<? extends BaseBean,?>> getControllerMap() {
    	return controllerMap;
    }

	public static Map<String, Class<? extends BaseBean>> getClassTypeMap(HttpServletRequest request) {
		AuthService authService = Statics.getServiceLoader().getAuthService();
		if (authService.isAuthenticated(request)) {
			return secureClassTypeMap;
		}
		return classTypeMap;
	}

	public static Map<String, Class<? extends BaseBean>> getSecureClassTypeMap() {
		return secureClassTypeMap;
	}

	private static Map<String, Class<? extends BaseBean>> getHiddenClassTypeMap(HttpServletRequest request) {
		AuthService authService = Statics.getServiceLoader().getAuthService();
		if (authService.isAuthenticated(request)) {
			return hiddenSecureClassTypeMap;
		}
		return hiddenClassTypeMap;
	}

	public static ServiceLoader getServiceLoader() {
		return serviceLoader;
	}

	public static void setServiceLoader(ServiceLoader serviceLoader) {
		Statics.serviceLoader = serviceLoader;
	}

}
