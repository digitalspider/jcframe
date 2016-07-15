package au.com.jcloud.jcframe.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class BaseBeanTest {

	@Test
	public void testId() {
		BaseBean<Integer> bean = new BaseBean<Integer>();
		Integer result = bean.getId();
		assertNull(result);
		bean.setId(0);
		assertEquals(0,(int)bean.getId());
		
		bean.setId(5);
		result = bean.getId();
		assertEquals(new Integer(5),result);

		BaseBean<String> stringBean = new BaseBean<String>();
		String id = stringBean.getId();
		assertNull(id);
		stringBean.setId("");
		assertEquals("",stringBean.getId());

		stringBean.setId("test");
		assertEquals("test",stringBean.getId());
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
		assertEquals("BaseBean[null] ",result);
		
		bean.setId(5);
		bean.setDisplayValue("value");
		result = bean.toString();
		assertEquals("BaseBean[5] value",result);

		BaseBean<String> stringBean = new BaseBean<String>();
		stringBean.setId("test");
		stringBean.setDisplayValue("value");
		assertEquals("BaseBean[test] value",stringBean.toString());
	}
}
