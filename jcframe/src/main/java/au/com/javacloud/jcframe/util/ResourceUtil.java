package au.com.javacloud.jcframe.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ResourceUtil {

	private static final Logger LOG = Logger.getLogger(ResourceUtil.class);

	public static Properties loadProperties(String fileName) throws IOException {
		Properties properties = new Properties();
		InputStream inputStream = null;
		try {
			File file = new File(fileName);
			if (file.exists()) {
				inputStream = new FileInputStream(file);
				properties.load( inputStream );
			} else {
				inputStream = ResourceUtil.class.getClassLoader().getResourceAsStream( fileName );
		        properties.load( inputStream );
			}
		} catch (IOException e) {
			LOG.error(e,e);
		} finally {
			if (inputStream!=null) { inputStream.close(); }
		}
		return properties;
    } 
}
