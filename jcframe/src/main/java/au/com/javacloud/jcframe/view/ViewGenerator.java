package au.com.javacloud.jcframe.view;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import au.com.javacloud.jcframe.model.BaseBean;

public interface ViewGenerator {

	public static final String PATH_JSP = "src/main/webapp/jsp/";
	public static final String PATH_TEMPLATE_PAGE = PATH_JSP+"jctemplate/";
	public static final String PATH_TEMPLATE_FIELD = PATH_TEMPLATE_PAGE+"field/";
	public static final String PATH_TEMPLATE_FIELD_TEXT = PATH_TEMPLATE_FIELD+"text/";
	public static final String PATH_TEMPLATE_FIELD_BEAN = PATH_TEMPLATE_FIELD+"bean/";
	public static final String PLACEHOLDER_FIELDS = "##FIELDS##";
	public static final String PLACEHOLDER_FIELDHEADERS = "##FIELDHEADERS##";

	public void generatePages(List<String> beans) throws Exception;

	public Map<ViewType,String> getContentTemplates(String templatePageDirectory) throws IOException;

	public List<Method> sortMethodMap(final Map<Method, Class> methodMap, final String[] orderList);

	public boolean validForView(ViewType viewType, Class<? extends BaseBean> classType, String fieldName);

	public String generateView(ViewType viewType, String beanName, Class<? extends BaseBean> classType, Map<Method,Class> methodMap) throws Exception;

	public String getTemplatedContent(ViewType viewType, String fieldName, String fieldHeader, Class fieldClass, String type, String other, boolean isHtml, boolean isBean);

	public String getTemplatedContent(ViewType viewType, String template, String fieldName, String fieldHeader, Class fieldClass, String type, String other, boolean isHtml, boolean isBean);

	public String getTemplate(ViewType viewType, boolean isBean);
}
