package au.com.javacloud.jcframe.util;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class ReflectUtilTest {

	private ReflectUtil testClass;
	
	@Before
	public void setup() {
		testClass = new ReflectUtil();
	}
	
	@Test
	public void test() {
		assertNotNull(testClass);
	}
}
