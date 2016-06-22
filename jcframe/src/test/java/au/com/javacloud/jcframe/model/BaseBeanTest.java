package au.com.javacloud.jcframe.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class BaseBeanTest {

	@Test
	public void testId() {
		BaseBean bean = new BaseBean();
		int result = bean.getId();
		assertNotNull(result);
		assertEquals(0,result);
		
		bean.setId(5);
		result = bean.getId();
		assertEquals(5,result);
	}
	
	@Test
	public void testDisplayValue() {
		BaseBean bean = new BaseBean();
		String result = bean.getDisplayValue();
		assertNotNull(result);
		assertEquals("",result);
		
		bean.setDisplayValue("value");
		result = bean.getDisplayValue();
		assertEquals("value",result);
	}
	
	@Test
	public void testToString() {
		BaseBean bean = new BaseBean();
		String result = bean.toString();
		assertNotNull(result);
		assertEquals("BaseBean[0] ",result);
		
		bean.setId(5);
		bean.setDisplayValue("value");
		result = bean.toString();
		assertEquals("BaseBean[5] value",result);
	}
}
