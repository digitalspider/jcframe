package au.com.jcloud.jcframe.util;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import au.com.jcloud.jcframe.annotation.DisplayValueColumn;
import au.com.jcloud.jcframe.dao.BaseDAO;
import au.com.jcloud.jcframe.model.BaseBean;

@SuppressWarnings("rawtypes")
public class ReflectUtil {

	private static final Logger LOG = Logger.getLogger(ReflectUtil.class);

	public static Map<Class<? extends BaseBean>,List<FieldMetaData>> fieldMetaDataCache = new HashMap<Class<? extends BaseBean>,List<FieldMetaData>>();

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

	public static List<FieldMetaData> getFieldData(Class<? extends BaseBean> objectClass, Class<? extends Annotation> excludeAnnotationClass) {
		return getFieldData(objectClass, asList(excludeAnnotationClass));
	}

	/**
	 * Call {@link #getFieldData(Class)} and match {@link @isRelevantMethod}.
	 */
	public static List<FieldMetaData> getFieldData(Class<? extends BaseBean> objectClass, List<Class<? extends Annotation>> excludeAnnotationClasses) {
		List<FieldMetaData> filteredFieldMetaData = new ArrayList<FieldMetaData>();
		for (FieldMetaData fieldMetaData : getFieldData(objectClass)) {
			if (isRelevant(fieldMetaData, excludeAnnotationClasses)) {
				filteredFieldMetaData.add(fieldMetaData);
			}
		}
		LOG.debug("filteredFieldMetaData=" + filteredFieldMetaData);
		return filteredFieldMetaData;
	}

	public static List<FieldMetaData> getFieldData(Class<? extends BaseBean> objectClass) {
		List<FieldMetaData> result = fieldMetaDataCache.get(objectClass);
		if (result!=null && !result.isEmpty()) {
			return result;
		}
		Field[] allFields = objectClass.getDeclaredFields();
		List<FieldMetaData> fieldMetaDataList = new ArrayList<FieldMetaData>();
		LOG.debug("get objectClass=" + objectClass + " super=" + objectClass.getSuperclass());
		if (objectClass.getSuperclass() != null && ReflectUtil.isBean(objectClass.getSuperclass())) {
			@SuppressWarnings("unchecked")
			Class<? extends BaseBean> superClass = (Class<? extends BaseBean>) objectClass.getSuperclass();
			List<FieldMetaData> superFieldData = getFieldData(superClass);
			fieldMetaDataList.addAll(superFieldData);
		}
		for (Field field : allFields) {
			if (!Modifier.isAbstract(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) {
				FieldMetaData fieldData = new FieldMetaData();
				fieldData.setField(field);
				Class classType = field.getType();
				Class<? extends Collection<?>> collectionClass = null;
				if (isCollection(classType)) {
					collectionClass = classType;
					classType = getCollectionGenericClass(field);
				}
				fieldData.setClassType(classType);
				fieldData.setCollectionClass(collectionClass);
				Method setMethod = getSetMethod(objectClass, field);
				fieldData.setSetMethod(setMethod);
				Method getMethod = getGetMethod(objectClass, field);
				fieldData.setGetMethod(getMethod);
				fieldMetaDataList.add(fieldData);
			}
		}
		fieldMetaDataCache.put(objectClass,fieldMetaDataList);
		LOG.debug("fieldMetaDataList=" + fieldMetaDataList);
		return fieldMetaDataList;
	}

	public static Method getGetMethod(Class<? extends BaseBean> objectClass, Field field) {
		for (Method method : objectClass.getDeclaredMethods()) {
			String fieldName = getFirstLetterUpperCase(field.getName());
			if (method.getName().equals("get"+fieldName) ||
					method.getName().equals("is"+fieldName)) {
				if ((Modifier.isPublic(method.getModifiers()) || Modifier.isProtected(method.getModifiers())) && !Modifier.isAbstract(method.getModifiers())) {
					return method;
				}
			}
		}
		return null;
	}

	public static Method getSetMethod(Class<? extends BaseBean> objectClass, Field field) {
		for (Method method : objectClass.getDeclaredMethods()) {
			String fieldName = getFirstLetterUpperCase(field.getName());
			if (method.getName().equals("set"+fieldName)) {
				if ((Modifier.isPublic(method.getModifiers()) || Modifier.isProtected(method.getModifiers())) && !Modifier.isAbstract(method.getModifiers())) {
					return method;
				}
			}
		}
		return null;
	}

	/**
	 * Return true if the method has an associated field name which does not have the excludeAnnotationClass present.
	 */
	public static boolean isRelevant(FieldMetaData fieldMetaData, List<Class<? extends Annotation>> excludeAnnotationClasses) {
		boolean relevant = true;
		Field field = fieldMetaData.getField();
		if (excludeAnnotationClasses != null && !excludeAnnotationClasses.isEmpty()) {
			for (Class<? extends Annotation> excludeAnnotationClass : excludeAnnotationClasses) {
				if (field.isAnnotationPresent(excludeAnnotationClass)) {
					relevant = false;
					break;
				}
			}
		}
		return relevant;
	}

	/**
	 * Only return methods from the method map where the associated field does not contains the excludeAnnotationClass
	 */
	public static Map<Method, FieldMetaData> getFilteredMethodMap(Map<Method, FieldMetaData> methodMap, List<Class<? extends Annotation>> excludedAnnotationClasses) {
		Map<Method, FieldMetaData> filteredMethodMap = new HashMap<Method, FieldMetaData>();
		for (Method method : methodMap.keySet()) {
			if (isRelevant(methodMap.get(method), excludedAnnotationClasses)) {
				filteredMethodMap.put(method, methodMap.get(method));
			}
		}
		return filteredMethodMap;
	}

	public static <Bean extends BaseBean> Bean getNewBean(Class<Bean> clazz) {
		try {
			return (Bean) clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <Bean extends BaseBean> List<String> getBeanFieldNames(Class<Bean> clazz, Class<? extends Annotation> excludeAnnnotationClass) {
		Collection<FieldMetaData> fieldMetaDatas = getFieldData(clazz, excludeAnnnotationClass);
		List<String> beanFieldNames = new ArrayList<String>();
		for (FieldMetaData fieldMetaData : fieldMetaDatas) {
			String fieldName = fieldMetaData.getField().getName();
			beanFieldNames.add(fieldName);
		}
		return beanFieldNames;
	}

	public static Class getCollectionGenericClass(Field field) {
		ParameterizedType collectionClassType = (ParameterizedType) field.getGenericType();
		Class<?> collectionClass = (Class<?>) collectionClassType.getActualTypeArguments()[0];
		LOG.debug("field="+field.getName()+" class="+collectionClass);
		return collectionClass;
	}

	public static <T extends BaseBean> String getDisplayValueFromBean(T bean) {
		String displayValue = "" + bean.getId();
		String columnName = BaseBean.FIELD_ID;
		if (bean.getClass().isAnnotationPresent(DisplayValueColumn.class)) {
			columnName = bean.getClass().getAnnotation(DisplayValueColumn.class).value();
		}
		try {
			Method method = bean.getClass().getDeclaredMethod("get" + ReflectUtil.getFirstLetterUpperCase(columnName));
			displayValue = "" + method.invoke(bean);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			try {
				Method method = bean.getClass().getDeclaredMethod("is" + ReflectUtil.getFirstLetterUpperCase(columnName));
				displayValue = "" + method.invoke(bean);
			} catch (Exception e2) {
				LOG.error(e2.getMessage());
				displayValue = "" + bean.getId();
			}
		}
		return displayValue;
	}

	/** Reflective way to get an annotation value */
	public static String getAnnotationValue(Class<?> classType, Class<? extends Annotation> annotationClass, String defaultValue) {
		if (classType.isAnnotationPresent(annotationClass)) {
			return getAnnotationValue(classType, annotationClass, defaultValue, "value");
		}
		return defaultValue;
	}

	public static String getAnnotationValue(Class<?> classType, Class<? extends Annotation> annotationType, String defaultValue, String attributeName) {
		String value = defaultValue;
		Annotation annotation = classType.getAnnotation(annotationType);
		if (annotation != null) {
			try {
				value = (String) annotation.annotationType().getMethod(attributeName).invoke(annotation);
			} catch (Exception e) {
				LOG.error(e,e);
			}
		}
		return value;
	}

	public static String getFirstLetterUpperCase(String name) {
		return name.substring(0,1).toUpperCase()+name.substring(1);
	}

	public static String getFirstLetterLowerCase(String name) {
		return name.substring(0,1).toLowerCase()+name.substring(1);
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
	 * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
	 *
	 * @param packageName The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static List<Class> getClasses(String packageName, Class<? extends Annotation> annotationClass) throws ClassNotFoundException, IOException {
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
			classes.addAll(findClasses(directory, packageName, annotationClass));
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

	/**
	 * Recursive method used to find all classes in a given directory and subdirs.
	 *
	 * @param directory   The base directory
	 * @param packageName The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public static List<Class> findClasses(File directory, String packageName, Class<? extends Annotation> annotationClass) throws ClassNotFoundException {

		List<Class> classes = new ArrayList<Class>();

		if (!directory.exists()) {
			return classes;
		}

		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName(), annotationClass));
			} else if (file.getName().endsWith(".class")) {
				Class classType = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
				if (classType.isAnnotationPresent(annotationClass)) {
					classes.add(classType);
				}
			}
		}
		return classes;
	}

	public static <Bean extends BaseBean> void invokeSetterMethodForBeanType(Bean bean, Method method, Class classType, int id) throws Exception {
		if (ReflectUtil.isBean(classType)) {
			BaseDAO fieldDao = Statics.getDaoMap().get(classType);
			if (fieldDao != null) {
				BaseBean valueBean = fieldDao.get(id,false);
				LOG.debug("classType=" + classType.getSimpleName() +" method=" + method.getName() +  " valueBean=" + valueBean);
				if (valueBean != null) {
					method.invoke(bean, valueBean);
				}
			}
		}
	}


	public static <Bean extends BaseBean> void invokeSetterMethodForCollection(Bean bean, Method method, Class fieldClass, Class collectionClass, String[] valueArray) throws Exception {
		if (ReflectUtil.isCollection(collectionClass) && valueArray!=null) {
			BaseDAO fieldDao = Statics.getDaoMap().get(fieldClass);
			LOG.info("fieldDao="+fieldDao);
			LOG.info("valueArray="+valueArray);
			Collection values = null;
			if (List.class.isAssignableFrom(collectionClass)) {
				values = new ArrayList();
			} else if (Set.class.isAssignableFrom(collectionClass)) {
				values = new HashSet();
			} else {
				try {
					values = (Collection)collectionClass.newInstance();
				} catch (Exception e) {
					LOG.error(e,e);
				}
				throw new Exception("Unknown collection class: "+collectionClass);
			}
			if (fieldDao!=null && valueArray.length>0 && StringUtils.isNumeric(valueArray[0])) {
				for (String valueString : valueArray) {
					if (!StringUtils.isBlank(valueString)) {
						BaseBean valueBean = fieldDao.get(Integer.parseInt(valueString),false);
						LOG.info("valueBean="+valueBean);
						if (valueBean != null) {
							values.add(valueBean);
						}
					}
				}
			} else {
				values.addAll(Arrays.asList(valueArray));
			}
			method.invoke(bean, values);
		}
	}

	public static <Bean extends BaseBean> void invokeSetterMethodForPrimitive(Bean bean, Method method, Class classType, String value) throws Exception {
		DateFormat dateFormat = Statics.getServiceLoader().getDisplayDateFormat();
		invokeSetterMethodForPrimitive(bean, method, classType, value, dateFormat);
	}

	public static <Bean extends BaseBean> void invokeSetterMethodForPrimitive(Bean bean, Method method, Class classType, String value, DateFormat dateFormat) throws Exception {
		if (!StringUtils.isBlank(value)) {
			method.invoke(bean, getValueObject(value, dateFormat));
		}
	}

	public static <T extends Object> T getValueObject(Object object, DateFormat dateFormat) throws ParseException {
		if (object==null) {
			return null;
		} else {
			String value = object.toString();
			Class classType = object.getClass();
			if (classType.equals(String.class)) {
				return (T)value;
			} else if (classType.equals(int.class) || classType.equals(Integer.class)) {
				return (T)(Integer)Integer.parseInt(value);
			} else if (classType.equals(boolean.class) || classType.equals(Boolean.class)) {
				return (T)(Boolean)Boolean.parseBoolean(value);
			} else if (classType.equals(Date.class)) {
				return (T) dateFormat.parse(value);
			} else if (classType.equals(long.class) || classType.equals(Long.class)) {
				return (T)(Long)Long.parseLong(value);
			} else if (classType.equals(short.class) || classType.equals(Short.class)) {
				return (T)(Short)Short.parseShort(value);
			} else if (classType.equals(float.class) || classType.equals(Float.class)) {
				return (T)(Float)Float.parseFloat(value);
			} else if (classType.equals(double.class) || classType.equals(Double.class)) {
				return (T)(Double)Double.parseDouble(value);
			} else if (classType.equals(BigDecimal.class)) {
				return (T)new BigDecimal(value);
			} else if (classType.equals(File.class)) {
				if (value.lastIndexOf("\\") >= 0) {
					value = value.substring(value.lastIndexOf("\\"));
				} else {
					value = value.substring(value.lastIndexOf("\\") + 1);
				}
				return (T)new File(value);
			}
		}
		return null;
	}

	public static List<Class<? extends Annotation>> asList(Class<? extends Annotation> singleClass) {
		List<Class<? extends Annotation>> classes = new ArrayList<Class<? extends Annotation>>();
		if (singleClass!=null) {
			classes.add(singleClass);
		}
		return classes;
	}
}
