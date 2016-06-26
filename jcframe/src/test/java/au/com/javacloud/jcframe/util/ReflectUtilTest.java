package au.com.javacloud.jcframe.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import test.model.TestBean;

public class ReflectUtilTest {

	private ReflectUtil testClass;
	
	@Before
	public void setup() {
		testClass = new ReflectUtil();
	}

	@Test
	public void testGetFieldData() {
		List<FieldMetaData> fieldMetaDataList = testClass.getFieldData(TestBean.class);
		assertEquals(20,fieldMetaDataList.size());

		for (FieldMetaData fieldMetaData : fieldMetaDataList) {
			String fieldName = fieldMetaData.getField().getName();
			if (fieldName.equals("noGetter")) {
				assertNull(fieldMetaData.getGetMethod());
				assertNotNull(fieldMetaData.getSetMethod());
			} else if (fieldName.equals("noSetter")) {
				assertEquals(String.class, fieldMetaData.getClassType());
				assertNotNull(fieldMetaData.getGetMethod());
				assertNull(fieldMetaData.getSetMethod());
			} else if (fieldName.equals("noGetterSetter")) {
				assertEquals(String.class, fieldMetaData.getClassType());
				assertNull(fieldMetaData.getGetMethod());
				assertNull(fieldMetaData.getSetMethod());
			} else if (fieldName.equals("noGetterSetter")) {
				assertEquals(String.class, fieldMetaData.getClassType());
				assertNull(fieldMetaData.getGetMethod());
				assertNull(fieldMetaData.getSetMethod());
			} else if (fieldName.equals("pInt")) {
				assertFalse(fieldMetaData.isCollection());
				assertFalse(fieldMetaData.isBean());
				assertEquals(int.class,fieldMetaData.getClassType());
				assertNull(fieldMetaData.getCollectionClass());
			} else if (fieldName.equals("oInt")) {
				assertFalse(fieldMetaData.isCollection());
				assertFalse(fieldMetaData.isBean());
				assertEquals(Integer.class,fieldMetaData.getClassType());
				assertNull(fieldMetaData.getCollectionClass());
			}
			if (fieldName.endsWith("List")) {
				assertTrue(fieldMetaData.isCollection());
			}
			if (fieldName.equals("TestClass")) {
				assertTrue(fieldMetaData.isBean());
				assertEquals(TestBean.class, fieldMetaData.getClassType());
			}
		}


	}
}
