package au.com.javacloud.jcframe.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class DAOLookupServiceImplTest {

	private DAOLookup testClass;
	
	@Before
	public void setup() {
		testClass = new DAOLookupImpl();
	}
	
	@Test
	public void test() {
		assertNotNull(testClass);
	}

}
