package au.com.javacloud.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Map;

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

    public static void main(String[] args) {
        System.out.println("START");
        File templateEdit = new File(PATH_TEMPLATE+PAGE_EDIT);
        System.out.println("edit="+templateEdit.getAbsolutePath());
        File templateList = new File(PATH_TEMPLATE+PAGE_LIST);
        System.out.println("list="+templateList.getAbsolutePath());
        File templateShow = new File(PATH_TEMPLATE+PAGE_SHOW);
        System.out.println("show="+templateShow.getAbsolutePath());

        Map<String,Class<? extends BaseBean>> classMap = Statics.getSecureClassTypeMap();
        for (String beanName : classMap.keySet()) {
            if (StringUtils.isNotEmpty(beanName)) {
                String directory = PATH_WEBAPP + beanName + "/";
                File edit = new File(directory+PAGE_EDIT);
                File list = new File(directory+PAGE_LIST);
                File show = new File(directory+PAGE_SHOW);
                System.out.println("show="+show.getAbsolutePath());
            }
        }
        System.out.println("DONE");
    }
}
