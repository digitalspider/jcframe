package au.com.javacloud.jcframe.view;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class ViewGeneratorImplTest {

	private ViewGenerator testClass;
	
	@Before
	public void setup() {
		testClass = new ViewGeneratorImpl();
	}
	
	@Test
	public void test() {
		assertNotNull(testClass);
	}
}
