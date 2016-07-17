package au.com.jcloud.jcframe.view;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import au.com.jcloud.jcframe.model.BaseBean;
import au.com.jcloud.jcframe.util.FieldMetaData;

public interface ViewGenerator {

	public static final String PATH_JSP = "src/main/webapp/jsp/";
	public static final String PATH_TEMPLATE = "src/main/resources/jctemplate/";
	public static final String PATH_TEMPLATE_DEFAULT = PATH_TEMPLATE+"default/";
	public static final String FIELD_TYPE_TEXT = "text";
	public static final String FIELD_TYPE_HTML = "html";
	public static final String FIELD_TYPE_BEAN = "bean";
	public static final String FIELD_TYPE_BEANLIST = "beanlist";
	public static final String FIELD_TYPE_PASSWORD = "password";
	public static final String PLACEHOLDER_FIELDS = "##FIELDS##";
	public static final String PLACEHOLDER_FIELDHEADERS = "##FIELDHEADERS##";

	public void generatePages(List<String> beans) throws Exception;

	public void generatePages(List<String> beans, String layout) throws Exception;

	public String getTemplate(ViewType viewType, String type) throws IOException;

	public Map<ViewType,String> getTemplates(String templateDirectory, String fieldName) throws IOException;

	public List<FieldMetaData> sortFieldData(final List<FieldMetaData> fieldMetaData, final String[] orderList);

	public boolean validForView(ViewType viewType, Field field);

	public String generateView(ViewType viewType, Class<? extends BaseBean> classType, List<FieldMetaData> fieldMetaDataList) throws Exception;

	public String getTemplatedContent(ViewType viewType, FieldMetaData fieldMetaData, Class<? extends BaseBean> classType) throws Exception;

	public String getTemplatedContent(ViewType viewType, String template, FieldMetaData fieldMetaData, String fieldHeader, Class<? extends BaseBean> classType, Class fieldClass, String type, String other, boolean isBean) throws Exception;

	public String getTypeToUseForTemplate(String type);

	public String getTypeToUseForJSP(String type);
}
