package au.com.javacloud;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import au.com.javacloud.annotation.ExcludeDBWrite;
import au.com.javacloud.annotation.IndexPage;
import au.com.javacloud.model.BaseBean;
import au.com.javacloud.util.Statics;
import au.com.javacloud.view.ViewGenerator;
import au.com.javacloud.view.ViewGeneratorImpl;
import au.com.javacloud.view.ViewType;

/**
 * Created by david on 26/05/16.
 */
public class Generator {

    public static void main(String[] args) throws Exception {
        System.out.println("START");
        List<String> beans = new ArrayList<String>();
        if (args.length>0) {
            System.out.println("args.length="+args.length);
            beans = Arrays.asList(args[0].split(","));
        }
        System.out.println("beans="+beans);
        ViewGenerator viewGenerator = Statics.getViewGenerator();
        viewGenerator.generatePages(beans);
        System.out.println("DONE");
    }
}
