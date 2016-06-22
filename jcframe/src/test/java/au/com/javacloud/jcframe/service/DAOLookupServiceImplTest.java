package au.com.javacloud.jcframe.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class DAOLookupServiceImplTest {

	private DAOLookupService testClass;
	
	@Before
	public void setup() {
		testClass = new DAOLookupServiceImpl();
	}
	
	@Test
	public void test() {
		assertNotNull(testClass);
	}

}
