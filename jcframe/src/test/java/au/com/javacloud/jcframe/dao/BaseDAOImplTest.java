package au.com.javacloud.jcframe.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import au.com.javacloud.jcframe.model.BaseBean;

public class BaseDAOImplTest {

	private BaseDAO<? extends BaseBean> testClass;
	
	@Before
	public void setup() {
		testClass = new BaseDAOImpl<>();
	}
	
	@Test
	public void test() {
		assertNotNull(testClass);
	}
}
