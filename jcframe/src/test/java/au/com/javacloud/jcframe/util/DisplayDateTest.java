package au.com.javacloud.jcframe.util;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;

import au.com.javacloud.jcframe.service.ServiceLoaderService;

public class DisplayDateTest {

	private DisplayDate testClass;
	private Date actualDate;
	
	@Before
	public void setup() {
		testClass = new DisplayDate();
		actualDate = new Date();
	}

	@Test
	public void testToString() {
		String result = testClass.toString();
		assertEquals(actualDate.toString(),result);
		
		ServiceLoaderService serviceLoaderService = BDDMockito.mock(ServiceLoaderService.class);
		Statics.setServiceLoaderService(serviceLoaderService);
		
		result = testClass.toString();
		assertEquals(actualDate.toString(),result);
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh");
		BDDMockito.when(serviceLoaderService.getDisplayDateFormat()).thenReturn(df);
		
		result = testClass.toString();
		assertEquals(df.format(actualDate),result);
	}
}
