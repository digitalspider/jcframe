package au.com.javacloud.view;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import au.com.javacloud.model.BaseBean;

public interface ViewGenerator {

	public static final String PATH_JSP = "src/main/webapp/jsp/";
	public static final String PATH_TEMPLATE_PAGE = PATH_JSP+"jctemplate/";
	public static final String PATH_TEMPLATE_FIELD = PATH_TEMPLATE_PAGE+"field/";
	public static final String PATH_TEMPLATE_FIELD_TEXT = PATH_TEMPLATE_FIELD+"text/";
	public static final String PATH_TEMPLATE_FIELD_BEAN = PATH_TEMPLATE_FIELD+"bean/";
	public static final String PLACEHOLDER = "##FIELDS##";

	public void generatePages() throws Exception;

	public Map<ViewType,String> getContentTemplates(String templatePageDirectory) throws IOException;
	
	public Map<Method, Class> sortMethodMap(Class<? extends BaseBean> classType, Map<Method, Class> methodMap, String[] orderList);
	
	public boolean validForView(ViewType viewType, Class<? extends BaseBean> classType, String fieldName);
	
	public String generateView(ViewType viewType, String beanName, Class<? extends BaseBean> classType, Map<Method,Class> methodMap) throws Exception;

	public String getTemplatedContent(ViewType viewType, String fieldName, String fieldHeader, String type, String other, boolean isHtml, boolean isBean);

	public String getTemplatedContent(ViewType viewType, String template, String fieldName, String fieldHeader, String type, String other, boolean isHtml, boolean isBean);
	
	public String getTemplate(ViewType viewType, boolean isBean);
}
