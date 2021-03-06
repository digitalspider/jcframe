package au.com.jcloud.jcframe.view;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import au.com.jcloud.jcframe.annotation.DisplayHeader;
import au.com.jcloud.jcframe.annotation.DisplayOrder;
import au.com.jcloud.jcframe.annotation.DisplayType;
import au.com.jcloud.jcframe.annotation.ExcludeView;
import au.com.jcloud.jcframe.annotation.HiddenBean;
import au.com.jcloud.jcframe.annotation.IndexPage;
import au.com.jcloud.jcframe.annotation.LinkField;
import au.com.jcloud.jcframe.annotation.M2MTable;
import au.com.jcloud.jcframe.model.BaseBean;
import au.com.jcloud.jcframe.util.FieldMetaData;
import au.com.jcloud.jcframe.util.FieldMetaDataComparator;
import au.com.jcloud.jcframe.util.ReflectUtil;
import au.com.jcloud.jcframe.util.Statics;

public class ViewGeneratorImpl implements ViewGenerator {

	private static final Logger LOG = Logger.getLogger(ViewGeneratorImpl.class);
	private static final String DELIM_HTML_TEMPLATE = "@@@";

	Map<String,Map<ViewType,String>> fieldContentTemplateMap = new HashMap<String,Map<ViewType,String>>();

	@Override
	public void generatePages(List<String> beans) throws Exception {
		generatePages(beans, null);
	}

	@Override
	public void generatePages(List<String> beans, String layout) throws Exception {
		LOG.info("generatePages() STARTED");
		String templatePath = PATH_TEMPLATE;
		LOG.info("templatePath="+templatePath);
		Map<ViewType,String> pageTemplates = getTemplates(layout, null);
		LOG.debug("pageTemplates="+pageTemplates);

		if (!pageTemplates.isEmpty()) {
			Map<String, Class<? extends BaseBean>> classMap = Statics.getSecureClassTypeMap();
			for (String beanName : classMap.keySet()) {
				if (StringUtils.isNotEmpty(beanName)) {
					if (beans.isEmpty() || beans.contains(beanName)) {
						String directory = PATH_JSP + beanName + "/";
						File destDir = new File(directory);
						LOG.info("destDir=" + destDir.getAbsolutePath());
						Class<? extends BaseBean> classType = classMap.get(beanName);
						if (!classType.isAnnotationPresent(HiddenBean.class)) {
							List<FieldMetaData> fieldMetaDataList = ReflectUtil.getFieldData(classType);

							for (ViewType viewType : ViewType.values()) {
								if (!viewType.equals(ViewType.INDEX) || (viewType.equals(ViewType.INDEX) && classType.isAnnotationPresent(IndexPage.class))) {
									String html = generateView(viewType, layout, classType, fieldMetaDataList);
									String pageContent = pageTemplates.get(viewType).replaceAll("\\$\\{beanName\\}", classType.getSimpleName());
									String[] htmlParts = html.split(DELIM_HTML_TEMPLATE); // split on delimiter
									if (htmlParts.length == 2) {
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
			}
		}
		LOG.info("generatePages() DONE");
	}

	@Override
	public String getTemplate(ViewType viewType, String layout, String type) throws IOException {
		Map<ViewType,String> contentTemplates = getTemplates(layout, type);
		if (contentTemplates!=null) {
			return contentTemplates.get(viewType);
		}
		return null;
	}

	@Override
    public Map<ViewType,String> getTemplates(String layout, String fieldName) throws IOException {
		Map<ViewType,String> templates = new HashMap<ViewType,String>();
		String templatePath = PATH_TEMPLATE;
		String jcTemplatePath = PATH_JCTEMPLATE;
		if (StringUtils.isNotBlank(layout)) {
			templatePath += layout+"/";
			jcTemplatePath += layout+"/";
		} else {
			templatePath += PATH_DEFAULT;
			jcTemplatePath += PATH_DEFAULT;
		}
		if (StringUtils.isNotBlank(fieldName)) {
			templates = fieldContentTemplateMap.get(fieldName);
			if (templates==null) {
				templates = new HashMap<ViewType,String>();
				fieldContentTemplateMap.put(fieldName,templates);
			} else {
				return templates;
			}
			templatePath+=fieldName+"/";
			jcTemplatePath+=fieldName+"/";
		}
		boolean displayLog = true;

		for (ViewType viewType : ViewType.values()) {
			String viewTemplateFilePath = templatePath + viewType.getPageName();
			String template;
			File templateFile = new File(viewTemplateFilePath);
			if (templateFile.exists()) {
				if (displayLog) {
					LOG.info("templateFile=" + templateFile.getAbsolutePath());
					displayLog = false;
				}
				template = FileUtils.readFileToString(templateFile);
			} else {
				viewTemplateFilePath = jcTemplatePath + viewType.getPageName();
				if (displayLog) {
					LOG.info("viewTemplateFilePath=" + viewTemplateFilePath);
					displayLog = false;
				}
				InputStream templateInputStream = getClass().getClassLoader().getResourceAsStream(viewTemplateFilePath);
				File temporaryFile = new File(System.getProperty("java.io.tmpdir"), viewType.getPageName());
				temporaryFile.createNewFile();
				FileUtils.copyInputStreamToFile(templateInputStream, temporaryFile);
				template = FileUtils.readFileToString(temporaryFile, "UTF-8");
			}
			templates.put(viewType, template);
		}
		return templates;
    }
    
	@SuppressWarnings("rawtypes")
	@Override
	public String generateView(ViewType viewType, String layout, Class<? extends BaseBean> classType, List<FieldMetaData> fieldMetaDataList) throws Exception {
		StringBuffer html = new StringBuffer();

		String[] orderList = new String[0];
		if (classType.isAnnotationPresent(DisplayOrder.class)) {
			orderList = classType.getAnnotation(DisplayOrder.class).value().split(",");
		}
		List<FieldMetaData> sortedFieldMetaDataList = sortFieldData(fieldMetaDataList, orderList);
		
		String fieldName;
		String content;
		
		switch (viewType) {
		case SHOW:
		case EDIT:
			// Handle fields
			for (FieldMetaData fieldMetaData : sortedFieldMetaDataList) {
				Field field = fieldMetaData.getField();
				Method method = fieldMetaData.getSetMethod();
				fieldName = field.getName();
				if (validForView(viewType, field)) {
					content = getTemplatedContent(viewType, layout, fieldMetaData, classType);
					html.append(content);
				}
			}
			break;
		case LIST:
		case INDEX:
			// Display headers
			fieldName = "";
			for (FieldMetaData fieldMetaData : sortedFieldMetaDataList) {
				Field field = fieldMetaData.getField();
				Method method = fieldMetaData.getSetMethod();
				fieldName = field.getName();
				if (validForView(viewType, field)) {
					String type = getDisplayType(field);
					String fieldHeader = getDisplayHeader(field);
					if (fieldName.equals(BaseBean.FIELD_ID)) {
						fieldHeader = classType.getSimpleName() + " ID";
					}
					if (!type.equals("password")) {
						html.append("    <th><a href=\"${beanUrl}/config/order/" + fieldName + "\">" + fieldHeader + "</a></th>\n");
					}
				}
			}

			html.append(DELIM_HTML_TEMPLATE); // DELIMITER for HEADERS

			// Display fields
			for (FieldMetaData fieldMetaData : sortedFieldMetaDataList) {
				Field field = fieldMetaData.getField();
				Method method = fieldMetaData.getSetMethod();
				fieldName = field.getName();
				if (validForView(viewType, field)) {
					content = getTemplatedContent(viewType, layout, fieldMetaData, classType);
					html.append(content);
				}
			}
			break;
		}
		return html.toString();
	}

	@Override
	public boolean validForView(ViewType viewType, Field field) {
		boolean displayForView = true;
		if (field.isAnnotationPresent(ExcludeView.class)) {
			String value = field.getAnnotation(ExcludeView.class).pages();
			if (value.equals("all")) {
				displayForView = false;
			} else if (value.contains(viewType.name().toLowerCase())) {
				displayForView = false;
			}
		}
		return displayForView;
	}

	@Override
	public List<FieldMetaData> sortFieldData(@SuppressWarnings("rawtypes") final List<FieldMetaData> fieldMetaDataList, final String[] orderList) {
		List<FieldMetaData> fieldMetaDataListCopy = new ArrayList<FieldMetaData>(fieldMetaDataList);
		List<FieldMetaData> sortedFieldMetaDataList = new ArrayList<FieldMetaData>();
		// Insert id field first
		for (FieldMetaData fieldMetaData : fieldMetaDataListCopy) {
			String fieldName = fieldMetaData.getField().getName();
			if (fieldName.equals(BaseBean.FIELD_ID)) {
				sortedFieldMetaDataList.add(fieldMetaData);
				fieldMetaDataListCopy.remove(fieldMetaData);
				break;
			}
		}
		// Insert methods in the orderList
		for (String orderedField : orderList) {
			for (FieldMetaData fieldMetaData : fieldMetaDataListCopy) {
				String fieldName = fieldMetaData.getField().getName();
				if (fieldName.equals(orderedField)) {
					sortedFieldMetaDataList.add(fieldMetaData);
					fieldMetaDataListCopy.remove(fieldMetaData);
					break;
				}
			}
		}
		// Add remaining values
		Collections.sort(fieldMetaDataListCopy,new FieldMetaDataComparator());
		for (FieldMetaData fieldMetaData : fieldMetaDataListCopy) {
			sortedFieldMetaDataList.add(fieldMetaData);
		}
		return sortedFieldMetaDataList;
	}

	@Override
	public String getTemplatedContent(ViewType viewType, String layout, FieldMetaData fieldMetaData, Class<? extends BaseBean> classType) throws Exception {
		Class fieldClass = fieldMetaData.getClassType(); // TODO: Implement field specific stuff
		boolean isBean = fieldMetaData.isBean();
		boolean isCollection = fieldMetaData.isCollection();
		Field field = fieldMetaData.getField();
		String fieldName = field.getName();
		String type = getDisplayType(field);
		if (StringUtils.isBlank(type)) {
			return "";
		}
		if (isBean && type.equals(FIELD_TYPE_TEXT)) {
			type = FIELD_TYPE_BEAN;
		}
		String other = "";
		M2MTable m2MTable = field.getAnnotation(M2MTable.class);
		if (m2MTable !=null) {
			type = FIELD_TYPE_BEANLIST;
		}
		LinkField linkField = field.getAnnotation(LinkField.class);
		if (linkField!=null) {
			type = FIELD_TYPE_TEXT;
			if (viewType == ViewType.EDIT) {
				other = "readonly=\"readonly\"";
			}
		}
		String fieldHeader = getDisplayHeader(field);
		if (fieldName.equals(BaseBean.FIELD_ID)) {
			fieldHeader = classType.getSimpleName() + " ID";
			if (viewType == ViewType.EDIT) {
				other = "readonly=\"readonly\"";
			}
		}

		String template = getTemplate(viewType, layout, getTypeToUseForTemplate(type));
		if (template!=null) {
			LOG.debug("viewType="+viewType+", fieldMetaData="+fieldMetaData+", fieldHeader="+fieldHeader+", classType="+classType+", fieldClass="+fieldClass+", type="+type+", isBean="+isBean);
			return getTemplatedContent(viewType, template, fieldMetaData, fieldHeader, classType, fieldClass, type, other, isBean);
		}
		return "";
	}

	@Override
	public String getTemplatedContent(ViewType viewType, String template, FieldMetaData fieldMetaData, String fieldHeader, Class<? extends BaseBean> classType, @SuppressWarnings("rawtypes") Class fieldClass, String type, String other, boolean isBean) throws Exception {
		String result = "";
		if (type==null) {
			return result;
		}
		if (other==null) {
			other = "";
		}
		Field field = fieldMetaData.getField();
		String fieldName = field.getName();

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
			if (type.equalsIgnoreCase(FIELD_TYPE_PASSWORD)) {
				result="";
			} else {
				if (type.equalsIgnoreCase(FIELD_TYPE_HTML)) {
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
					LinkField linkField = field.getAnnotation(LinkField.class);
					M2MTable m2MTable = field.getAnnotation(M2MTable.class);
					if (linkField!=null) {
						String linkFieldName = linkField.value();
						result = result.replaceAll("\\$\\{linkPrefix\\}", "<a href=\"\\$\\{baseUrl\\}/" + fieldClass.getSimpleName().toLowerCase() + "/find/"+linkFieldName+"/=<c:out value='\\$\\{bean.id\\}'/>\">"+fieldClass.getSimpleName()+"s ");
					} else if (m2MTable !=null) {
						result = result.replaceAll("\\$\\{linkPrefix\\}", "<a href=\"\\$\\{baseUrl\\}/" + fieldClass.getSimpleName().toLowerCase() + "/show/<c:out value='\\$\\{fieldBean.id\\}'/>\">");
					} else {
						result = result.replaceAll("\\$\\{linkPrefix\\}", "<a href=\"\\$\\{baseUrl\\}/" + fieldClass.getSimpleName().toLowerCase() + "/show/<c:out value='\\$\\{bean." + fieldName + ".id\\}'/>\">");
					}
					result = result.replaceAll("\\$\\{linkSuffix\\}", "</a>");
				}
			}
		}
		return result; 
	}

	public String getDisplayHeader(Field field) throws NoSuchFieldException {
		if (field.isAnnotationPresent(DisplayHeader.class)) {
			return field.getAnnotation(DisplayHeader.class).value();
		}
		return ReflectUtil.getFirstLetterUpperCase(field.getName()); // TODO: Put spaces in between each uppercase letter
	}

	public String getDisplayType(Field field) throws NoSuchFieldException {
		if (field.isAnnotationPresent(DisplayType.class)) {
			return field.getAnnotation(DisplayType.class).value();
		}
		return ViewGenerator.FIELD_TYPE_TEXT;
	}

	@Override
	public String getTypeToUseForTemplate(String type) {
		String typeToUse = type;
		if (type.equalsIgnoreCase(FIELD_TYPE_HTML)) {
			typeToUse = FIELD_TYPE_TEXT;
		} else if (type.equalsIgnoreCase(FIELD_TYPE_PASSWORD)) {
			typeToUse = FIELD_TYPE_TEXT;
		}
		return typeToUse;
	}

	@Override
	public String getTypeToUseForJSP(String type) {
		String typeToUse = type;
		if (type.equalsIgnoreCase(FIELD_TYPE_HTML)) {
			typeToUse = FIELD_TYPE_TEXT;
		}
		return typeToUse;
	}
}
