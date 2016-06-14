package au.com.javacloud.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Ref;
import java.util.Map;
import org.apache.commons.io.FileUtils;

import au.com.javacloud.annotation.IndexPage;
import au.com.javacloud.annotation.DisplayHtml;
import au.com.javacloud.model.BaseBean;

/**
 * Created by david on 26/05/16.
 */
public class Generator {
    public static final String PATH_JSP="src/main/webapp/jsp/";
    public static final String PATH_TEMPLATE=PATH_JSP+"jctemplate/";
    public static final String PAGE_EDIT="edit.jsp";
    public static final String PAGE_LIST="list.jsp";
    public static final String PAGE_SHOW="show.jsp";
    public static final String PAGE_INDEX="index.jsp";
    public static final String PLACEHOLDER="##FIELDS##";

    public static void main(String[] args) throws Exception {
        System.out.println("START");
        File editFile = new File(PATH_TEMPLATE+PAGE_EDIT);
        System.out.println("editFile="+editFile.getAbsolutePath());
        File listFile = new File(PATH_TEMPLATE+PAGE_LIST);
        System.out.println("listFile="+listFile.getAbsolutePath());
        File showFile = new File(PATH_TEMPLATE+PAGE_SHOW);
        System.out.println("showFile="+showFile.getAbsolutePath());
        File indexFile = new File(PATH_TEMPLATE+PAGE_INDEX);
        System.out.println("indexFile="+indexFile.getAbsolutePath());

        final String editContentTemplate = FileUtils.readFileToString(editFile, "UTF-8");
		final String listContentTemplate = FileUtils.readFileToString(listFile, "UTF-8");
		final String showContentTemplate = FileUtils.readFileToString(showFile, "UTF-8");
		final String indexContentTemplate = FileUtils.readFileToString(indexFile, "UTF-8");

        Map<String,Class<? extends BaseBean>> classMap = Statics.getSecureClassTypeMap();
        for (String beanName : classMap.keySet()) {
            if (StringUtils.isNotEmpty(beanName)) {
                String directory = PATH_JSP + beanName + "/";
                File destDir = new File(directory);
                System.out.println("destDir="+destDir.getAbsolutePath());
                Class<? extends BaseBean> classType = classMap.get(beanName);
                Map<Method,Class> methodMap = ReflectUtil.getPublicGetterMethods(classType);
                String editHtml = generateEditView(beanName, classType, methodMap);
                String listHtml = generateListView(beanName, classType, methodMap);
                String showHtml = generateShowView(beanName, classType, methodMap);

				String editContent = editContentTemplate.replaceAll("\\$\\{beanName\\}", classType.getSimpleName());
				String listContent = listContentTemplate.replaceAll("\\$\\{beanName\\}", classType.getSimpleName());
				String showContent = showContentTemplate.replaceAll("\\$\\{beanName\\}", classType.getSimpleName());
                editContent = editContent.replace(PLACEHOLDER, editHtml);
				showContent = showContent.replace(PLACEHOLDER, showHtml);
				listContent = listContent.replace(PLACEHOLDER, listHtml);

				File beanEditFile = new File(destDir,PAGE_EDIT);
				File beanListFile = new File(destDir,PAGE_LIST);
				File beanShowFile = new File(destDir,PAGE_SHOW);

                FileUtils.writeStringToFile(beanEditFile, editContent, "UTF-8");
                FileUtils.writeStringToFile(beanListFile, listContent, "UTF-8");
                FileUtils.writeStringToFile(beanShowFile, showContent, "UTF-8");

				if (classType.isAnnotationPresent(IndexPage.class)) {
					String indexHtml = generateIndexView(beanName, classType, methodMap);
					String indexContent = indexContentTemplate.replace(PLACEHOLDER, indexHtml);
					File beanIndexFile = new File(destDir,PAGE_INDEX);
					FileUtils.writeStringToFile(beanIndexFile, indexContent, "UTF-8");
				}
            }
        }
        System.out.println("DONE");
    }

	public static String generateShowView(String beanName, Class<? extends BaseBean> classType, Map<Method,Class> methodMap) throws Exception {
		StringBuffer html = new StringBuffer();
		
		// Handle BaseBean id
		String template = getShowTemplate();
		String fieldName = "id";
		String fieldHeader = ReflectUtil.getFirstLetterUpperCase(beanName)+" ID";
		String type = "text";
		String content = getTemplatedContent(template, fieldName, fieldHeader, type, null);
		html.append(content);
		
		for (Method method : methodMap.keySet()) {
			Class fieldClass = methodMap.get(method);
			fieldName = ReflectUtil.getFieldName(method);
			fieldHeader = ReflectUtil.getFieldHeader(classType, fieldName);
			content = getTemplatedContent(template, fieldName, fieldHeader, type, null);
			html.append(content);
		}
		return html.toString();
	}

	public static String generateEditView(String beanName, Class<? extends BaseBean> classType, Map<Method,Class> methodMap) throws Exception {
		StringBuffer html = new StringBuffer();

		// Handle BaseBean id
		String template = getEditTemplate();
		String fieldName = "id";
		String fieldHeader = ReflectUtil.getFirstLetterUpperCase(beanName)+" ID";
		String type = "text";
		String other = "readonly=\"readonly\"/> ";
		String content = getTemplatedContent(template, fieldName, fieldHeader, type, other);
		html.append(content);
		
		for (Method method : methodMap.keySet()) {
			Class fieldClass = methodMap.get(method);
			fieldName = ReflectUtil.getFieldName(method);
			fieldHeader = ReflectUtil.getFieldHeader(classType, fieldName);
			content = getTemplatedContent(template, fieldName, fieldHeader, type, null);
			html.append(content);
		}
		return html.toString();
	}

	public static String generateListView(String beanName, Class<? extends BaseBean> classType, Map<Method,Class> methodMap) throws Exception {
		StringBuffer html = new StringBuffer();

		html.append("<table>\n");
		
		// DisplayHeader
		html.append("<thead>\n");
		html.append("  <tr>\n");
		html.append("    <th>Page ID</th>\n");
		String fieldHeader = "";
		for (Method method : methodMap.keySet()) {
			fieldHeader = ReflectUtil.getFieldHeader(classType, method);
			html.append("    <th>"+fieldHeader+"</th>\n");
		}
		html.append("    <th colspan=\"2\">Action</th>\n");
		html.append("  </tr>\n");
		html.append("</thead>\n");
		
		// Body
		html.append("<tbody>\n");
		html.append("  <c:forEach items=\"${beans}\" var=\"bean\">\n");
		html.append("    <tr>\n");

		// Handle BaseBean id
		String template = getListTemplate();
		String fieldName = "id";
		fieldHeader = ReflectUtil.getFirstLetterUpperCase(beanName)+" ID";
		String type = "text";
		boolean isHtml = false;
		boolean isLink = true;
		String content = getListTemplatedContent(template, fieldName, fieldHeader, type, isHtml, isLink);
		html.append(content);
		
		// Handle other fields
		for (Method method : methodMap.keySet()) {
			isHtml = ReflectUtil.isAnnotationPresent(classType, method,DisplayHtml.class);
			isLink = ReflectUtil.isBean(methodMap.get(method));
			Class fieldClass = methodMap.get(method);
			fieldName = ReflectUtil.getFieldName(method);
			fieldHeader = ReflectUtil.getFieldHeader(classType, method);
			content = getListTemplatedContent(template, fieldName, fieldHeader, type, isHtml, isLink);
			html.append(content);
		}
		
		html.append("      <td><a href=\"${beanUrl}/edit/<c:out value='${bean.id}'/>\">Update</a></td>\n");
		html.append("      <td><a href=\"${beanUrl}/delete/<c:out value='${bean.id}'/>\">Delete</a></td>\n");
		
		html.append("    </tr>");
		html.append("  </c:forEach>");
		html.append("</tbody>\n");
		html.append("</table>\n");


		

		return html.toString();
	}

	public static String generateIndexView(String beanName, Class<? extends BaseBean> classType, Map<Method,Class> methodMap) throws Exception {
		return generateListView(beanName, classType, methodMap);
	}
	
	public static String getTemplatedContent(String template, String fieldName, String fieldHeader, String type, String other) {
		String result = template.replaceAll("\\$\\{fieldName\\}", fieldName);
		result = result.replaceAll("\\$\\{fieldHeader\\}", fieldHeader);
		result = result.replaceAll("\\$\\{type\\}", type);
		if (other==null) {
			other = "";
		}
		result = result.replaceAll("\\$\\{other\\}", other);
		return result;
	}
	
	public static String getListTemplatedContent(String template, String fieldName, String fieldHeader, String type, boolean isHtml, boolean isLink) {
		String result = template.replaceAll("\\$\\{fieldName\\}", fieldName);
		result = result.replaceAll("\\$\\{fieldHeader\\}", fieldHeader);
		result = result.replaceAll("\\$\\{type\\}", type);
		if (isHtml) {
			result = result.replaceAll("\\$\\{isHtml\\}", "escapeXml=\"false\"");
		} else {
			result = result.replaceAll("\\$\\{isHtml\\}", "");
		}
		if (!isLink) {
			result = result.replaceAll("\\$\\{linkPrefix\\}", "");
			result = result.replaceAll("\\$\\{linkSuffix\\}", "");
		} else {
			if (fieldName.equals(BaseBean.FIELD_ID)) {
				result = result.replaceAll("\\$\\{linkPrefix\\}", "<a href=\"\\$\\{beanUrl\\}/show/<c:out value='\\$\\{bean.id\\}'/>\">");
			} else {
				result = result.replaceAll("\\$\\{linkPrefix\\}", "<a href=\"\\$\\{baseUrl\\}/"+fieldName+"/show/<c:out value='\\$\\{bean."+fieldName+".id\\}'/>\">");
			}
			result = result.replaceAll("\\$\\{linkSuffix\\}", "</a>");
		}
		return result;
	}
	
	public static String getEditTemplate() {
		StringBuffer html = new StringBuffer();
		html.append("<div class=\"fieldrow\" id=\"fieldrow_${fieldName}\" name=\"fieldrow_${fieldName}\">\n");
		html.append("  <label for=\"${fieldName}\">${fieldHeader}</label>\n");
		html.append("  <input type=\"${type}\" id=\"${fieldName}\" name=\"${fieldName}\" value='<c:out value=\"${bean.${fieldName}}\" />' placeholder=\"${fieldHeader}\" ${other} />\n");
		html.append("</div>\n");
		return html.toString();
	}
	
	public static String getShowTemplate() {
		StringBuffer html = new StringBuffer();
		html.append("<div class=\"fieldrow\" id=\"fieldrow_${fieldName}\" name=\"fieldrow_${fieldName}\">\n");
		html.append("  <label for=\"${fieldName}\">${fieldHeader}</label>\n");
		html.append("  <div class=\"field\" id=\"${fieldName}\" name=\"${fieldName}\"><c:out value=\"${bean.${fieldName}}\" /></div>\n");
		html.append("</div>\n");
		return html.toString();
	}
	
	public static String getListTemplate() {
		StringBuffer html = new StringBuffer();
		html.append("      <td>${linkPrefix}<c:out value=\"${bean.${fieldName}}\" ${isHtml}/>${linkSuffix}</td>\n");
		return html.toString();
	}
	
	public static String getIndexTemplate() {
		return getListTemplate();
	}
}
