package au.com.javacloud.jcframe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import au.com.javacloud.jcframe.service.ServiceLoaderService;
import au.com.javacloud.jcframe.util.Statics;
import au.com.javacloud.jcframe.view.ViewGenerator;

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
        ServiceLoaderService serviceLoader = Statics.getServiceLoaderService();
        if (serviceLoader!=null) {
	        ViewGenerator viewGenerator = serviceLoader.getViewGeneratorService();
	        if (viewGenerator!=null) {
	        	viewGenerator.generatePages(beans);
	        }
        }
        System.out.println("DONE");
    }
}
