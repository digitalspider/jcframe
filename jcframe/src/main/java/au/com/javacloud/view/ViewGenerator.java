package au.com.javacloud.view;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import au.com.javacloud.model.BaseBean;

public interface ViewGenerator {

	public Map<ViewType,String> getPageContentTemplates(String templateDirectory) throws IOException;
	
	public String generateView(ViewType viewType, String beanName, Class<? extends BaseBean> classType, Map<Method,Class> methodMap) throws Exception;

	public String getTemplatedContent(ViewType viewType, String fieldName, String fieldHeader, String type, String other, boolean isHtml, boolean isBean);

	public String getTemplatedContent(ViewType viewType, String template, String fieldName, String fieldHeader, String type, String other, boolean isHtml, boolean isBean);
	
	public String getTemplate(ViewType viewType, boolean isBean);
}
