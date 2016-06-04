package au.com.javacloud.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ResourceUtil {

	private static final Logger LOG = Logger.getLogger(ResourceUtil.class);

	public static Properties loadProperties(String fileName) {
		try {
		    InputStream inputStream = ResourceUtil.class.getClassLoader().getResourceAsStream( fileName );
		    Properties properties = new Properties();
	        properties.load( inputStream );
	        return properties;
		} catch (IOException e) {
			LOG.error(e,e);
			return new Properties();
		}
    } 
}
