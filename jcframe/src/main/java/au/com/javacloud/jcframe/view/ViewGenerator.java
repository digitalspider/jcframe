package au.com.javacloud.jcframe.view;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import au.com.javacloud.jcframe.model.BaseBean;

public interface ViewGenerator {

	public static final String PATH_JSP = "src/main/webapp/jsp/";
	public static final String PATH_TEMPLATE = PATH_JSP+"jctemplate/";
	public static final String PLACEHOLDER_FIELDS = "##FIELDS##";
	public static final String PLACEHOLDER_FIELDHEADERS = "##FIELDHEADERS##";

	public void generatePages(List<String> beans) throws Exception;

	public String getTemplate(ViewType viewType, String type) throws IOException;

	public Map<ViewType,String> getTemplates(String templateDirectory, String fieldName) throws IOException;

	public List<Method> sortMethodMap(final Map<Method, Class> methodMap, final String[] orderList);

	public boolean validForView(ViewType viewType, Class<? extends BaseBean> classType, String fieldName);

	public String generateView(ViewType viewType, String beanName, Class<? extends BaseBean> classType, Map<Method,Class> methodMap) throws Exception;

	public String getTemplatedContent(ViewType viewType, String fieldName, Class<? extends BaseBean> classType, Class fieldClass) throws Exception;

	public String getTemplatedContent(ViewType viewType, String template, String fieldName, String fieldHeader, Class<? extends BaseBean> classType, Class fieldClass, String type, String other, boolean isBean) throws Exception;

	public String getTypeToUseForTemplate(String type);

	public String getTypeToUseForJSP(String type);
}
