package au.com.javacloud.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import au.com.javacloud.model.BaseBean;

/**
 * Created by david on 26/05/16.
 */
public class Generator {
    public static final String PATH_WEBAPP="src/main/webapp/";
    public static final String PATH_TEMPLATE=PATH_WEBAPP+"template/";
    public static final String PAGE_EDIT="edit.jsp";
    public static final String PAGE_LIST="list.jsp";
    public static final String PAGE_SHOW="show.jsp";
    public static final String PAGE_INDEX="index.jsp";
    public static final String PLACEHOLDER="##FIELDS##";

    public static void main(String[] args) throws IOException {
        System.out.println("START");
        File editFile = new File(PATH_TEMPLATE+PAGE_EDIT);
        System.out.println("editFile="+editFile.getAbsolutePath());
        File listFile = new File(PATH_TEMPLATE+PAGE_LIST);
        System.out.println("listFile="+listFile.getAbsolutePath());
        File showFile = new File(PATH_TEMPLATE+PAGE_SHOW);
        System.out.println("showFile="+showFile.getAbsolutePath());
        File indexFile = new File(PATH_TEMPLATE+PAGE_INDEX);
        System.out.println("indexFile="+indexFile.getAbsolutePath());

        String editContent = FileUtils.readFileToString(editFile, "UTF-8");
        String listContent = FileUtils.readFileToString(listFile, "UTF-8");
        String showContent = FileUtils.readFileToString(showFile, "UTF-8");
        String indexContent = FileUtils.readFileToString(indexFile, "UTF-8");

        Map<String,Class<? extends BaseBean>> classMap = Statics.getSecureClassTypeMap();
        for (String beanName : classMap.keySet()) {
            if (StringUtils.isNotEmpty(beanName)) {
                String directory = PATH_WEBAPP + beanName + "/jsp/";
                File destDir = new File(directory);
                System.out.println("destDir="+destDir.getAbsolutePath());
                Class<? extends BaseBean> classType = classMap.get(beanName);
                Map<Method,Class> methodMap = ReflectUtil.getPublicGetterMethods(classType);
                String editHtml = generateEditView(beanName, classType, methodMap);
                String listHtml = generateListView(beanName, classType, methodMap);
                String showHtml = generateShowView(beanName, classType, methodMap);
                String indexHtml = generateIndexView(beanName, classType, methodMap);

                editContent = editContent.replace(PLACEHOLDER, editHtml);
                showContent = showContent.replace(PLACEHOLDER, showHtml);
                listContent = listContent.replace(PLACEHOLDER, listHtml);
                indexContent = indexContent.replace(PLACEHOLDER, indexHtml);

				File beanEditFile = new File(destDir,PAGE_EDIT);
				File beanListFile = new File(destDir,PAGE_LIST);
				File beanShowFile = new File(destDir,PAGE_SHOW);
				File beanIndexFile = new File(destDir,PAGE_INDEX);

                FileUtils.writeStringToFile(beanEditFile, editContent, "UTF-8");
                FileUtils.writeStringToFile(beanListFile, listContent, "UTF-8");
                FileUtils.writeStringToFile(beanShowFile, showContent, "UTF-8");
                FileUtils.writeStringToFile(beanIndexFile, indexContent, "UTF-8");
            }
        }
        System.out.println("DONE");
    }

	public static String generateShowView(String beanName, Class<? extends BaseBean> classType, Map<Method,Class> methodMap) {
		StringBuffer html = new StringBuffer();
		
		// Handle BaseBean id
		String template = getEditTemplate();
		String fieldName = "id";
		String fieldHeader = getFirstLetterUpper(beanName)+" ID";
		String type = "text";
		String content = getTemplatedContent(template, fieldName, fieldHeader, type, null);
		html.append(content);
		
		for (Method method : methodMap.keySet()) {
			Class fieldClass = methodMap.get(method);
			fieldName = ReflectUtil.getFieldName(method);
			fieldHeader = ReflectUtil.getFieldHeader(method);
			content = getTemplatedContent(template, fieldName, fieldHeader, type, null);
			html.append(content);
		}
		return html.toString();
	}

	public static String generateEditView(String beanName, Class<? extends BaseBean> classType, Map<Method,Class> methodMap) {
		StringBuffer html = new StringBuffer();

		// Handle BaseBean id
		String template = getEditTemplate();
		String fieldName = "id";
		String fieldHeader = getFirstLetterUpper(beanName)+" ID";
		String type = "text";
		String other = "readonly=\"readonly\"/> ";
		String content = getTemplatedContent(template, fieldName, fieldHeader, type, other);
		html.append(content);
		
		for (Method method : methodMap.keySet()) {
			Class fieldClass = methodMap.get(method);
			fieldName = ReflectUtil.getFieldName(method);
			fieldHeader = ReflectUtil.getFieldHeader(method);
			content = getTemplatedContent(template, fieldName, fieldHeader, type, null);
			html.append(content);
		}
		return html.toString();
	}

	public static String generateListView(String beanName, Class<? extends BaseBean> classType, Map<Method,Class> methodMap) {
		StringBuffer html = new StringBuffer();

		html.append("<table>\n");
		
		// Header
		html.append("<thead><tr>\n");
		html.append("  <th>Page ID</th>\n");
		String fieldHeader = "";
		for (Method method : methodMap.keySet()) {
			fieldHeader = ReflectUtil.getFieldHeader(method);
			html.append("  <th>"+fieldHeader+"</th>\n");			
		}
		html.append("  <th colspan=\"2\">Action</th>\n");
		html.append("</tr></thead>\n");
		
		// Body
		html.append("<tbody><c:forEach items=\"${beans}\" var=\"bean\"><tr>\n");

		// Handle BaseBean id
		String template = getEditTemplate();
		String fieldName = "id";
		fieldHeader = getFirstLetterUpper(beanName)+" ID";
		String type = "text";
		boolean isHtml = false;
		boolean isLink = true;
		String content = getListTemplatedContent(template, fieldName, fieldHeader, type, isHtml, isLink);
		html.append(content);
		
		// Handle other fields
		for (Method method : methodMap.keySet()) {
			Class fieldClass = methodMap.get(method);
			fieldName = ReflectUtil.getFieldName(method);
			fieldHeader = ReflectUtil.getFieldHeader(method);
			content = getListTemplatedContent(template, fieldName, fieldHeader, type, isHtml, isLink);
			html.append(content);
		}
		
		html.append("<td><a href=\"${beanUrl}/edit/<c:out value='${bean.id}'/>\">Update</a></td>\n");
		html.append("<td><a href=\"${beanUrl}/delete/<c:out value='${bean.id}'/>\">Delete</a></td>\n");
		
		html.append("</tr></c:forEach></tbody>\n");
		html.append("</table>\n");


		

		return html.toString();
	}

	private static String getFirstLetterUpper(String beanName) {
		String fieldHeader = beanName.substring(0,1).toUpperCase()+beanName.substring(1);
		return fieldHeader;
	}

	public static String generateIndexView(String beanName, Class<? extends BaseBean> classType, Map<Method,Class> methodMap) {
		return generateListView(beanName, classType, methodMap);
	}
	
	public static String getTemplatedContent(String template, String fieldName, String fieldHeader, String type, String other) {
		String result = template.replaceAll("${fieldName}", fieldName);
		result = result.replaceAll("${fieldHeader}", fieldHeader);
		result = result.replaceAll("${type}", type);
		if (other==null) {
			other = "";
		}
		result = result.replaceAll("${other}", other);
		return result;
	}
	
	public static String getListTemplatedContent(String template, String fieldName, String fieldHeader, String type, boolean isHtml, boolean isLink) {
		String result = template.replaceAll("${fieldName}", fieldName);
		result = result.replaceAll("${fieldHeader}", fieldHeader);
		result = result.replaceAll("${type}", type);
		if (isHtml) {
			result = result.replaceAll("${isHtml}", "escapeXml=\"false\"");
		}
		if (!isLink) {
			result = result.replaceAll("${linkPrefix}", "");
			result = result.replaceAll("${linkSuffix}", "");
		} else {
			if (fieldName.equals(BaseBean.FIELD_ID)) {
				result = result.replaceAll("${linkSuffix}", "<a href=\"${beanUrl}/show/<c:out value='${bean.id}'/>\">");
			} else {
				result = result.replaceAll("${linkSuffix}", "<a href=\"${baseUrl}/"+fieldName+"/show/<c:out value='${bean."+fieldName+".id}'/>\">");
			}
			result = result.replaceAll("${linkSuffix}", "</a>");
		}
		return result;
	}
	
	public static String getEditTemplate() {
		StringBuffer html = new StringBuffer();
		html.append("<div class=\"fieldrow\" id=\"fieldrow_${fieldName}\" name=\"fieldrow_${fieldName}\">\n");
		html.append("  <label for=\"${fieldName}\">${fieldHeader}</label>\n");
		html.append("  <input type=\"${type}\" name=\"${fieldName}\" id=\"${fieldName}\" value=\"<c:out value=\"${bean.${fieldName}}\" />\" placeholder=\"${fieldHeader}\" ${other}\n");
		html.append("/>\n");
		html.append("</div>\n");
		return html.toString();
	}
	
	public static String getShowTemplate() {
		StringBuffer html = new StringBuffer();
		html.append("<div class=\"fieldrow\" id=\"fieldrow_${fieldName}\" name=\"fieldrow_${fieldName}\">\n");
		html.append("  <label for=\"${fieldName}\">${fieldHeader}</label>\n");
		html.append("  <div class=\"field\" id=\"field\"><c:out value=\"${bean.${fieldName}}\" /></div>\n");
		html.append("/>\n");
		return html.toString();
	}
	
	public static String getListTemplate() {
		StringBuffer html = new StringBuffer();
		html.append("<td>${linkPrefix}<c:out value=\"${bean.${fieldName}}\" ${isHtml}/>${linkSuffix}</td>\n");
		return html.toString();
	}
	
	public static String getIndexTemplate() {
		return getListTemplate();
	}
}
