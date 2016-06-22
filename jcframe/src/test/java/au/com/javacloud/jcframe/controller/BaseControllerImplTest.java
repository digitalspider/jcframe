package au.com.javacloud.jcframe.controller;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import au.com.javacloud.jcframe.model.BaseBean;

public class BaseControllerImplTest {

	private BaseController<? extends BaseBean, ?> testClass;
	
	@Before
	public void setup() {
		testClass = new BaseControllerImpl<>();
	}
	
	@Test
	public void test() {
		assertNotNull(testClass);
	}
}
