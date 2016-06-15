package au.com.javacloud.view;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import au.com.javacloud.annotation.DisplayHtml;
import au.com.javacloud.annotation.ExcludeDBWrite;
import au.com.javacloud.annotation.IndexPage;
import au.com.javacloud.model.BaseBean;
import au.com.javacloud.util.ReflectUtil;
import au.com.javacloud.util.Statics;

public class ViewGeneratorImpl implements ViewGenerator {

	private static final Logger LOG = Logger.getLogger(ViewGeneratorImpl.class);

	Map<ViewType,String> textFieldContentTemplates;
	Map<ViewType,String> beanFieldContentTemplates;

	@Override
	public void generatePages() throws Exception {
		LOG.info("generatePages() STARTED");
		Map<ViewType,String> pageContentTemplates = getContentTemplates(PATH_TEMPLATE_PAGE);
		LOG.debug("pageContentTemplates="+pageContentTemplates);
		textFieldContentTemplates = getContentTemplates(PATH_TEMPLATE_FIELD_TEXT);
		beanFieldContentTemplates = getContentTemplates(PATH_TEMPLATE_FIELD_BEAN);

		Map<String,Class<? extends BaseBean>> classMap = Statics.getSecureClassTypeMap();
		for (String beanName : classMap.keySet()) {
			if (StringUtils.isNotEmpty(beanName)) {
				String directory = PATH_JSP + beanName + "/";
				File destDir = new File(directory);
				LOG.info("destDir="+destDir.getAbsolutePath());
				Class<? extends BaseBean> classType = classMap.get(beanName);
				Map<Method,Class> methodMap = ReflectUtil.getPublicGetterMethods(classType, ExcludeDBWrite.class);

				for (ViewType viewType : ViewType.values()) {
					if (!viewType.equals(ViewType.INDEX) || (viewType.equals(ViewType.INDEX) && classType.isAnnotationPresent(IndexPage.class))) {
						String html = generateView(viewType, beanName, classType, methodMap);
						String pageContent = pageContentTemplates.get(viewType).replaceAll("\\$\\{beanName\\}", classType.getSimpleName());
						pageContent = pageContent.replace(PLACEHOLDER, html);
						File outputFile = new File(destDir,viewType.getPageName());
						FileUtils.writeStringToFile(outputFile, pageContent, "UTF-8");
					}
				}
			}
		}
		LOG.info("generatePages() DONE");
	}

	@Override
    public Map<ViewType,String> getContentTemplates(String templatePageDirectory) throws IOException {
    	Map<ViewType,String> pageContentTemplates = new HashMap<>();
		
        for (ViewType viewType : ViewType.values()) {
        	File templateFile = new File(templatePageDirectory+viewType.getPageName());
        	LOG.info("templatePageFile="+templateFile.getAbsolutePath());
        	final String pageContentTemplate = FileUtils.readFileToString(templateFile, "UTF-8");
        	pageContentTemplates.put(viewType, pageContentTemplate);
        }
		return pageContentTemplates;
    }
    
	@Override
	public String generateView(ViewType viewType, String beanName, Class<? extends BaseBean> classType, Map<Method,Class> methodMap) throws Exception {
		StringBuffer html = new StringBuffer();
		
		switch (viewType) {
		case SHOW:
		case EDIT:
			// Handle BaseBean id
			String fieldName = "id";
			String fieldHeader = classType.getSimpleName()+" ID";
			String type = "text";
			String other = null;
			boolean isHtml = false;
			boolean isBean = false;
			if (viewType == ViewType.EDIT) {
				other = "readonly=\"readonly\"";
			}
			String content = getTemplatedContent(viewType, fieldName, fieldHeader, type, other, isHtml, isBean);
			html.append(content);

			// Handle remaining fields
			for (Method method : methodMap.keySet()) {
				Class fieldClass = methodMap.get(method);
				fieldName = ReflectUtil.getFieldName(method);
				fieldHeader = ReflectUtil.getFieldHeader(classType, fieldName);
				type = ReflectUtil.getFieldDisplayType(classType, fieldName);
				other = null;
				isHtml = ReflectUtil.isAnnotationPresent(classType, fieldName,DisplayHtml.class);
				isBean = ReflectUtil.isBean(methodMap.get(method));
				content = getTemplatedContent(viewType, fieldName, fieldHeader, type, other, isHtml, isBean);
				html.append(content);
			}
			break;
		case LIST:
		case INDEX:
			html.append("<table>\n");
			
			// DisplayHeader
			html.append("<thead>\n");
			html.append("  <tr>\n");
			html.append("    <th><a href=\"${beanUrl}/config/order/id\">"+classType.getSimpleName()+" ID</a></th>\n");
			fieldName = "";
			fieldHeader = "";
			type = "text";
			other = null;
			for (Method method : methodMap.keySet()) {
				fieldName = ReflectUtil.getFieldName(method);
				fieldHeader = ReflectUtil.getFieldHeader(classType, fieldName);
				type = ReflectUtil.getFieldDisplayType(classType, fieldName);
				if (!type.equals("password")) {
					html.append("    <th><a href=\"${beanUrl}/config/order/" + fieldName + "\">" + fieldHeader + "</a></th>\n");
				}
			}
			html.append("    <th colspan=\"2\">Action</th>\n");
			html.append("  </tr>\n");
			html.append("</thead>\n");
			
			// Body
			html.append("<tbody>\n");
			html.append("  <c:forEach items=\"${beans}\" var=\"bean\">\n");
			html.append("    <tr>\n");

			// Handle BaseBean id
			fieldName = "id";
			fieldHeader = classType.getSimpleName()+" ID";
			type = "text";
			isHtml = false;
			isBean = false;
			content = getTemplatedContent(viewType, fieldName, fieldHeader, type, other, isHtml, isBean);
			html.append(content);
			
			// Handle other fields
			for (Method method : methodMap.keySet()) {
				Class fieldClass = methodMap.get(method);
				fieldName = ReflectUtil.getFieldName(method);
				fieldHeader = ReflectUtil.getFieldHeader(classType, fieldName);
				isHtml = ReflectUtil.isAnnotationPresent(classType, fieldName,DisplayHtml.class);
				isBean = ReflectUtil.isBean(methodMap.get(method));
				type = ReflectUtil.getFieldDisplayType(classType, fieldName);
				content = getTemplatedContent(viewType, fieldName, fieldHeader, type, other, isHtml, isBean);
				html.append(content);
			}
			
			html.append("      <td><a href=\"${beanUrl}/edit/<c:out value='${bean.id}'/>\">Update</a></td>\n");
			html.append("      <td><a href=\"${beanUrl}/delete/<c:out value='${bean.id}'/>\">Delete</a></td>\n");
			
			html.append("    </tr>");
			html.append("  </c:forEach>");
			html.append("</tbody>\n");
			html.append("</table>\n");				
			break;
		}
		return html.toString();
	}

	@Override
	public String getTemplatedContent(ViewType viewType, String fieldName, String fieldHeader, String type, String other, boolean isHtml, boolean isBean) {
		String template = getTemplate(viewType, isBean);
		return getTemplatedContent(viewType, template, fieldName, fieldHeader, type, other, isHtml, isBean);
	}

	@Override
	public String getTemplatedContent(ViewType viewType, String template, String fieldName, String fieldHeader, String type, String other, boolean isHtml, boolean isBean) {
		String result = "";
		if (type==null) {
			return result;
		}
		result = template.replaceAll("\\$\\{fieldName\\}", fieldName);
		result = result.replaceAll("\\$\\{fieldHeader\\}", fieldHeader);
		result = result.replaceAll("\\$\\{type\\}", type);
		if (other == null) {
			other = "";
		}
		result = result.replaceAll("\\$\\{other\\}", other);
		switch(viewType) {
		case SHOW:
			if (type.equals("password")) {
				result="";
			}
			break;
		case EDIT:
			break;
		case LIST:
		case INDEX:
			if (type.equals("password")) {
				result="";
			} else {
				if (isHtml) {
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
					result = result.replaceAll("\\$\\{linkPrefix\\}", "<a href=\"\\$\\{baseUrl\\}/" + fieldName + "/show/<c:out value='\\$\\{bean." + fieldName + ".id\\}'/>\">");
					result = result.replaceAll("\\$\\{linkSuffix\\}", "</a>");
				}
			}
		}
		return result; 
	}

	@Override
	public String getTemplate(ViewType viewType, boolean isBean) {
		if (isBean) {
			return beanFieldContentTemplates.get(viewType);
		} else {
			return textFieldContentTemplates.get(viewType);
		}
	}
}
