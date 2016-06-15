package au.com.javacloud.util;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import au.com.javacloud.annotation.ExcludeDBWrite;
import au.com.javacloud.annotation.IndexPage;
import au.com.javacloud.model.BaseBean;
import au.com.javacloud.view.ViewGenerator;
import au.com.javacloud.view.ViewGeneratorImpl;
import au.com.javacloud.view.ViewType;

/**
 * Created by david on 26/05/16.
 */
public class Generator {
    public static final String PATH_JSP="src/main/webapp/jsp/";
    public static final String PATH_TEMPLATE=PATH_JSP+"jctemplate/";
    public static final String PLACEHOLDER="##FIELDS##";

    public static void main(String[] args) throws Exception {
        System.out.println("START");
		ViewGenerator viewGenerator = new ViewGeneratorImpl();

		Map<ViewType,String> pageContentTemplates = viewGenerator.getPageContentTemplates(PATH_TEMPLATE);

        Map<String,Class<? extends BaseBean>> classMap = Statics.getSecureClassTypeMap();
        for (String beanName : classMap.keySet()) {
            if (StringUtils.isNotEmpty(beanName)) {
                String directory = PATH_JSP + beanName + "/";
                File destDir = new File(directory);
                System.out.println("destDir="+destDir.getAbsolutePath());
                Class<? extends BaseBean> classType = classMap.get(beanName);
                Map<Method,Class> methodMap = ReflectUtil.getPublicGetterMethods(classType, ExcludeDBWrite.class);
                
                for (ViewType viewType : ViewType.values()) {
                	if (!viewType.equals(ViewType.INDEX) || (viewType.equals(ViewType.INDEX) && classType.isAnnotationPresent(IndexPage.class))) {
	                	String html = viewGenerator.generateView(viewType, beanName, classType, methodMap);
	                	String pageContent = pageContentTemplates.get(viewType).replaceAll("\\$\\{beanName\\}", classType.getSimpleName());
	                	pageContent = pageContent.replace(PLACEHOLDER, html);
	                	File outputFile = new File(destDir,viewType.getPageName());
	                	FileUtils.writeStringToFile(outputFile, pageContent, "UTF-8");
	                }
                }
            }
        }
        System.out.println("DONE");
    }
}
