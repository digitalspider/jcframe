package au.com.javacloud.jcframe.view;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import au.com.javacloud.jcframe.annotation.DisplayOrder;
import au.com.javacloud.jcframe.annotation.ExcludeView;
import au.com.javacloud.jcframe.annotation.IndexPage;
import au.com.javacloud.jcframe.annotation.LinkField;
import au.com.javacloud.jcframe.model.BaseBean;
import au.com.javacloud.jcframe.util.MethodComparator;
import au.com.javacloud.jcframe.util.ReflectUtil;
import au.com.javacloud.jcframe.util.Statics;

public class ViewGeneratorImpl implements ViewGenerator {

	private static final Logger LOG = Logger.getLogger(ViewGeneratorImpl.class);

	Map<String,Map<ViewType,String>> fieldContentTemplateMap = new HashMap<String,Map<ViewType,String>>();

	@Override
	public void generatePages(List<String> beans) throws Exception {
		LOG.info("generatePages() STARTED");
		Map<ViewType,String> pageTemplates = getTemplates(PATH_TEMPLATE, null);
		LOG.debug("pageTemplates="+pageTemplates);

		Map<String,Class<? extends BaseBean>> classMap = Statics.getSecureClassTypeMap();
		for (String beanName : classMap.keySet()) {
			if (StringUtils.isNotEmpty(beanName)) {
				if (beans.isEmpty() || beans.contains(beanName)) {
					String directory = PATH_JSP + beanName + "/";
					File destDir = new File(directory);
					LOG.info("destDir=" + destDir.getAbsolutePath());
					Class<? extends BaseBean> classType = classMap.get(beanName);
					List<Class<? extends Annotation>> excludedAnnotationClasses = new ArrayList<Class<? extends Annotation>>();
					Map<Method, Class> methodMap = ReflectUtil.getPublicGetterMethods(classType, excludedAnnotationClasses);

					for (ViewType viewType : ViewType.values()) {
						if (!viewType.equals(ViewType.INDEX) || (viewType.equals(ViewType.INDEX) && classType.isAnnotationPresent(IndexPage.class))) {
							String html = generateView(viewType, beanName, classType, methodMap);
							String pageContent = pageTemplates.get(viewType).replaceAll("\\$\\{beanName\\}", classType.getSimpleName());
							String[] htmlParts = html.split("@@@"); // split on delimiter
							if (htmlParts.length==2) {
								pageContent = pageContent.replace(PLACEHOLDER_FIELDHEADERS, htmlParts[0]);
								pageContent = pageContent.replace(PLACEHOLDER_FIELDS, htmlParts[1]);
							} else {
								pageContent = pageContent.replace(PLACEHOLDER_FIELDS, html);
							}
							File outputFile = new File(destDir, viewType.getPageName());
							FileUtils.writeStringToFile(outputFile, pageContent, "UTF-8");
						}
					}
				}
			}
		}
		LOG.info("generatePages() DONE");
	}

	@Override
	public String getTemplate(ViewType viewType, String type) throws IOException {
		Map<ViewType,String> contentTemplates = getTemplates(PATH_TEMPLATE, type);
		if (contentTemplates!=null) {
			return contentTemplates.get(viewType);
		}
		return null;
	}

	@Override
    public Map<ViewType,String> getTemplates(String templateDirectory, String fieldName) throws IOException {
		Map<ViewType,String> templates = new HashMap<ViewType,String>();
		if (StringUtils.isNotBlank(fieldName)) {
			templates = fieldContentTemplateMap.get(fieldName);
			if (templates==null) {
				templates = new HashMap<ViewType,String>();
				fieldContentTemplateMap.put(fieldName,templates);
			} else {
				return templates;
			}
			templateDirectory+=fieldName+"/";
		}
		boolean displayLog = true;
        for (ViewType viewType : ViewType.values()) {
        	File templateFile = new File(templateDirectory+viewType.getPageName());
			if (displayLog) {
				LOG.info("templateFile=" + templateFile.getAbsolutePath());
				displayLog = false;
			}
        	final String template = FileUtils.readFileToString(templateFile, "UTF-8");
        	templates.put(viewType, template);
        }
		return templates;
    }
    
	@SuppressWarnings("rawtypes")
	@Override
	public String generateView(ViewType viewType, String beanName, Class<? extends BaseBean> classType, Map<Method,Class> methodMap) throws Exception {
		StringBuffer html = new StringBuffer();

		String[] orderList = new String[0];
		if (classType.isAnnotationPresent(DisplayOrder.class)) {
			orderList = classType.getAnnotation(DisplayOrder.class).value().split(",");
		}
		List<Method> sortedMethodMap = sortMethodMap(methodMap, orderList);
		
		String fieldName;
		String content;
		
		switch (viewType) {
		case SHOW:
		case EDIT:
			// Handle fields
			for (Method method : sortedMethodMap) {
				fieldName = ReflectUtil.getFieldName(method);
				if (validForView(viewType, classType, fieldName)) {
					Class fieldClass = methodMap.get(method); // TODO: Implement field specific stuff
					content = getTemplatedContent(viewType, fieldName, classType, fieldClass);
					html.append(content);
				}
			}
			break;
		case LIST:
		case INDEX:
			// Display headers
			fieldName = "";
			for (Method method : sortedMethodMap) {
				fieldName = ReflectUtil.getFieldName(method);
				if (validForView(viewType, classType, fieldName)) {
					String type = ReflectUtil.getFieldDisplayType(classType, fieldName);
					String fieldHeader = ReflectUtil.getFieldHeader(classType, fieldName);
					if (fieldName.equals(BaseBean.FIELD_ID)) {
						fieldHeader = classType.getSimpleName() + " ID";
					}
					if (!type.equals("password")) {
						html.append("    <th><a href=\"${beanUrl}/config/order/" + fieldName + "\">" + fieldHeader + "</a></th>\n");
					}
				}
			}

			html.append("@@@"); // DELIMITER for HEADERS

			// Display fields
			for (Method method : sortedMethodMap) {
				Class fieldClass = methodMap.get(method); // TODO: Implement field specific stuff
				fieldName = ReflectUtil.getFieldName(method);
				if (validForView(viewType, classType, fieldName)) {
					content = getTemplatedContent(viewType, fieldName, classType, fieldClass);
					html.append(content);
				}
			}
			break;
		}
		return html.toString();
	}

	@Override
	public boolean validForView(ViewType viewType, Class<? extends BaseBean> classType, String fieldName) {
		boolean displayForView = true;
		if (ReflectUtil.isAnnotationPresent(classType, fieldName, ExcludeView.class)) {
			try {
				String value = ReflectUtil.getAnnotation(classType, fieldName, ExcludeView.class).pages();
				if (value.equals("all")) {
					displayForView = false;
				} else if (value.contains(viewType.name().toLowerCase())) {
					displayForView = false;
				}
			} catch (NoSuchFieldException e) {
				LOG.error(e,e);
				return false;
			}
		}
		return displayForView;
	}

	@Override
	public List<Method> sortMethodMap(@SuppressWarnings("rawtypes") final Map<Method, Class> methodMap, final String[] orderList) {
		List<Method> methods = new ArrayList<Method>(methodMap.keySet());
		List<Method> sortedMethodList = new ArrayList<Method>();
		// Insert first id field
		for (Method method : methods) {
			String fieldName = ReflectUtil.getFieldName(method);
			if (fieldName.equals("id")) {
				sortedMethodList.add(method);
				methods.remove(method);
				break;
			}
		}
		// Insert methods in the orderList
		for (String orderedField : orderList) {
			for (Method method : methods) {
				String fieldName = ReflectUtil.getFieldName(method);
				if (fieldName.equals(orderedField)) {
					sortedMethodList.add(method);
					methods.remove(method);
					break;
				}
			}
		}
		// Add remaining values
		Collections.sort(methods,new MethodComparator());
		for (Method method : methods) {
			sortedMethodList.add(method);
		}
		return sortedMethodList;
	}

	@Override
	public String getTemplatedContent(ViewType viewType, String fieldName, Class<? extends BaseBean> classType, @SuppressWarnings("rawtypes") Class fieldClass) throws Exception {
		boolean isBean = ReflectUtil.isBean(fieldClass);
		String type = ReflectUtil.getFieldDisplayType(classType, fieldName);
		if (StringUtils.isBlank(type) && isBean) {
			type = "bean";
		}
		boolean isCollection = false;
		if (ReflectUtil.isCollection(fieldClass)) {
			isCollection = true;
			fieldClass = ReflectUtil.getCollectionGenericClass(classType, fieldName);
			isBean = ReflectUtil.isBean(fieldClass);
			if (StringUtils.isBlank(type) && isBean) {
				type = "beanlist";
			}
		}
		if (type==null) {
			return "";
		}
		String other = "";
		String fieldHeader = ReflectUtil.getFieldHeader(classType, fieldName);
		if (fieldName.equals(BaseBean.FIELD_ID)) {
			fieldHeader = classType.getSimpleName() + " ID";
			if (viewType == ViewType.EDIT) {
				other = "readonly=\"readonly\"";
			}
		}
		String template = getTemplate(viewType, getTypeToUseForTemplate(type));
		if (template!=null) {
			return getTemplatedContent(viewType, template, fieldName, fieldHeader, classType, fieldClass, type, other, isBean);
		}
		return "";
	}

	@Override
	public String getTemplatedContent(ViewType viewType, String template, String fieldName, String fieldHeader, Class<? extends BaseBean> classType, @SuppressWarnings("rawtypes") Class fieldClass, String type, String other, boolean isBean) throws Exception {
		String result = "";
		if (type==null) {
			return result;
		}
		if (other==null) {
			other = "";
		}

		result = template.replaceAll("\\$\\{fieldName\\}", fieldName);
		result = result.replaceAll("\\$\\{fieldHeader\\}", fieldHeader);
		result = result.replaceAll("\\$\\{type\\}", getTypeToUseForJSP(type));
		result = result.replaceAll("\\$\\{other\\}", other);
		switch(viewType) {
		case EDIT:
			break;
		case SHOW:
		case LIST:
		case INDEX:
			if (type.equalsIgnoreCase("password")) {
				result="";
			} else {
				if (type.equalsIgnoreCase("html")) {
					result = result.replaceAll("\\$\\{isHtml\\}", "escapeXml=\"false\"");
				} else {
					result = result.replaceAll("\\$\\{isHtml\\}", "");
				}
				if (!isBean) {
					if (fieldName.equals(BaseBean.FIELD_ID)) {
						result = result.replaceAll("\\$\\{linkPrefix\\}", "<a href=\"\\$\\{beanUrl\\}/show/<c:out value='\\$\\{bean.id\\}'/>\">");
						result = result.replaceAll("\\$\\{linkSuffix\\}", "</a>");
					} else {
						result = result.replaceAll("\\$\\{linkPrefix\\}", "");
						result = result.replaceAll("\\$\\{linkSuffix\\}", "");
					}
				} else {
					LinkField linkField = ReflectUtil.getAnnotation(classType, fieldName, LinkField.class);
					if (linkField!=null) {
						String linkFieldName = linkField.value();
						result = result.replaceAll("\\$\\{linkPrefix\\}", "<a href=\"\\$\\{baseUrl\\}/" + fieldClass.getSimpleName().toLowerCase() + "/find/"+linkFieldName+"/=<c:out value='\\$\\{bean.id\\}'/>\">"+fieldClass.getSimpleName()+"s ");
					} else {
						result = result.replaceAll("\\$\\{linkPrefix\\}", "<a href=\"\\$\\{baseUrl\\}/" + fieldClass.getSimpleName().toLowerCase() + "/show/<c:out value='\\$\\{bean." + fieldName + ".id\\}'/>\">");
					}
					result = result.replaceAll("\\$\\{linkSuffix\\}", "</a>");
				}
			}
		}
		return result; 
	}

	@Override
	public String getTypeToUseForTemplate(String type) {
		String typeToUse = type;
		if (type.equalsIgnoreCase("html")) {
			typeToUse = "text";
		} else if (type.equalsIgnoreCase("password")) {
			typeToUse = "text";
		}
		return typeToUse;
	}

	@Override
	public String getTypeToUseForJSP(String type) {
		String typeToUse = type;
		if (type.equalsIgnoreCase("html")) {
			typeToUse = "text";
		}
		return typeToUse;
	}
}
