package au.com.javacloud.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import au.com.javacloud.annotation.Exclude;
import au.com.javacloud.dao.BaseDAO;
import au.com.javacloud.model.BaseBean;

@SuppressWarnings("rawtypes")
public class ReflectUtil {

	private static final Logger LOG = Logger.getLogger(ReflectUtil.class);

	public static boolean isBean(Class clazz) {
        if (BaseBean.class.isAssignableFrom(clazz)) {
        	return true;
        }
        return false;
    }

	public static boolean isCollection(Class clazz) {
		if (Collection.class.isAssignableFrom(clazz)) {
			return true;
		}
		return false;
	}

    public static Map<Method,Class> getPublicSetterMethods(Class<?> objectClass) {
    	Method[] allMethods = objectClass.getDeclaredMethods();
    	Map<Method,Class> setterMethods = new HashMap<Method,Class>();
    	LOG.debug("set objectClass="+objectClass+" super="+objectClass.getSuperclass());
        if (objectClass.getSuperclass() != null && ReflectUtil.isBean(objectClass.getSuperclass())) {
            Class<?> superClass = objectClass.getSuperclass();
            Map<Method,Class> superClassMethods = getPublicSetterMethods(superClass);
            setterMethods.putAll(superClassMethods);
        }
    	for (Method method : allMethods) {
    	    if (Modifier.isPublic(method.getModifiers()) && !Modifier.isAbstract(method.getModifiers())) {
    	        if (method.getName().startsWith("set")) {
					if (!method.isAnnotationPresent(Exclude.class)) {
						Class[] params = method.getParameterTypes();
						if (params.length == 1) {
							setterMethods.put(method, params[0]);
						}
					}
    	        }
    	    }
    	}
    	LOG.debug("setters="+setterMethods);
    	return setterMethods;
    }

    public static Map<Method,Class> getPublicGetterMethods(Class<?> objectClass) {
    	Method[] allMethods = objectClass.getDeclaredMethods();
    	Map<Method,Class> getterMethods = new HashMap<Method,Class>();
    	LOG.debug("get objectClass="+objectClass+" super="+objectClass.getSuperclass());
        if (objectClass.getSuperclass() != null && ReflectUtil.isBean(objectClass.getSuperclass())) {
            Class<?> superClass = objectClass.getSuperclass();
            Map<Method,Class> superClassMethods = getPublicGetterMethods(superClass);
            getterMethods.putAll(superClassMethods);
        }
    	for (Method method : allMethods) {
    	    if (Modifier.isPublic(method.getModifiers()) && !Modifier.isAbstract(method.getModifiers())) {
    	        if (method.getName().startsWith("get") || method.getName().startsWith("is")) {
					if (!method.isAnnotationPresent(Exclude.class)) {
						Class returnClass = method.getReturnType();
						getterMethods.put(method, returnClass);
					}
    	        }
    	    }
    	}
    	LOG.debug("getters="+getterMethods);
    	return getterMethods;
    }

    public static <U extends BaseBean> U getNewBean(Class<U> clazz) {
    	try {
    		return (U)clazz.newInstance();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }

	public static <U extends BaseBean> List<String> getBeanFieldNames(Class<U> clazz) {
		Set<Method> methods = ReflectUtil.getPublicGetterMethods(clazz).keySet();
		List<String> beanFieldNames = new ArrayList<String>();
		for (Method method : methods) {
            String fieldName = ReflectUtil.getFieldName(method);
			beanFieldNames.add(fieldName);
		}
		return beanFieldNames;
	}


    public static String getFieldName(String methodName) {
    	String methodString = methodName.substring(3);
    	return methodString.substring(0,1).toLowerCase()+methodString.substring(1);
    }

    public static String getFieldName(Method method) {
		return getFieldName(method.getName());
    }

	/**
	 * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
	 *
	 * @param packageName The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static List<Class> getClasses(String packageName, Class filterClassType, boolean excludeSelf) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		List<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName, filterClassType, excludeSelf));
		}
		return classes;
	}

	/**
	 * Recursive method used to find all classes in a given directory and subdirs.
	 *
	 * @param directory   The base directory
	 * @param packageName The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public static List<Class> findClasses(File directory, String packageName, Class filterClassType, boolean excludeSelf) throws ClassNotFoundException {

		List<Class> classes = new ArrayList<Class>();

		if (!directory.exists()) {
			return classes;
		}

		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName(), filterClassType, excludeSelf));
			} else if (file.getName().endsWith(".class")) {
				Class classType = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
				if (filterClassType == null || (filterClassType!=null && filterClassType.isAssignableFrom(classType))) {
					if (!(excludeSelf && classType.equals(filterClassType))) {
						classes.add(classType);
					}
				}
			}
		}
		return classes;
	}

	public static <T extends BaseBean> void invokeSetterMethodForBeanType(T bean, Method method, Class classType, int id) throws Exception {
		if (ReflectUtil.isBean(classType)) {
			BaseDAO fieldDao = Statics.getDaoMap().get(classType);
			if (fieldDao != null) {
				BaseBean valueBean = fieldDao.getLookup(id);
				if (valueBean != null) {
					method.invoke(bean, valueBean);
				}
			}
		}
	}


	public static <T extends BaseBean> void invokeSetterMethodForCollection(T bean, Method method, Class classType, String value) throws Exception {
		if (ReflectUtil.isCollection(classType)) {
			String[] valueArray = value.split(",");
			BaseDAO fieldDao = Statics.getDaoMap().get(classType);
			if (fieldDao!=null && valueArray.length>0 && StringUtils.isNumeric(valueArray[0])) {
				List<BaseBean> beans = new ArrayList<BaseBean>();
				for (String valueString : valueArray) {
					if (!StringUtils.isBlank(valueString)) {
						BaseBean valueBean = fieldDao.getLookup(Integer.parseInt(valueString));
						if (valueBean != null) {
							beans.add(valueBean);
						}
					}
				}
				method.invoke(bean, beans);
			} else {
				List<String> strings = new ArrayList<String>(Arrays.asList(valueArray));
				method.invoke(bean, strings);
			}
		}
	}

	public static <T extends BaseBean> void invokeSetterMethodForPrimitive(T bean, Method method, Class classType, String value, DateFormat dateFormat) throws Exception {
		if (!StringUtils.isBlank(value)) {
			if (classType.equals(String.class)) {
				method.invoke(bean, value);
			} else if (classType.equals(int.class) || classType.equals(Integer.class)) {
				method.invoke(bean, Integer.parseInt(value));
			} else if (classType.equals(boolean.class) || classType.equals(Boolean.class)) {
				method.invoke(bean, Boolean.parseBoolean(value));
			} else if (classType.equals(Date.class)) {
				method.invoke(bean, dateFormat.parse(value));
			} else if (classType.equals(long.class) || classType.equals(Long.class)) {
				method.invoke(bean, Long.parseLong(value));
			} else if (classType.equals(short.class) || classType.equals(Short.class)) {
				method.invoke(bean, Short.parseShort(value));
			} else if (classType.equals(float.class) || classType.equals(Float.class)) {
				method.invoke(bean, Float.parseFloat(value));
			} else if (classType.equals(double.class) || classType.equals(Double.class)) {
				method.invoke(bean, Double.parseDouble(value));
			} else if (classType.equals(BigDecimal.class)) {
				method.invoke(bean, new BigDecimal(value));
			} else if (classType.equals(File.class)) {
				if (value.lastIndexOf("\\") >= 0) {
					value = value.substring(value.lastIndexOf("\\"));
				} else {
					value = value.substring(value.lastIndexOf("\\") + 1);
				}
				method.invoke(bean, new File(value));
			}
		}
	}
}
