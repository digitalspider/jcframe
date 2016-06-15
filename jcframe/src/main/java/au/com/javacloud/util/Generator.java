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

    public static void main(String[] args) throws Exception {
        System.out.println("START");
		ViewGenerator viewGenerator = Statics.getViewGenerator();
        viewGenerator.generatePages();
        System.out.println("DONE");
    }
}
