package au.com.jcloud.jcframe;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.BDDMockito;

import au.com.jcloud.jcframe.service.ServiceLoader;
import au.com.jcloud.jcframe.util.Statics;
import au.com.jcloud.jcframe.view.ViewGenerator;

public class GeneratorTest {

	@Test
	public void testConstructor() throws Exception {
		Generator generator = new Generator();
		assertNotNull(generator);
	}
	
	@Test
	public void testMainNoServiceLoader() throws Exception {
		// No params
		String[] args = new String[0];
		Generator.main(args);
		
		// One param
		args = new String[] {"TestBean"};
		Generator.main(args);
		
		// Two params
		args = new String[] {"TestBean", "TestBean2"};
		Generator.main(args);
		
		// One param comma separated
		args = new String[] {"TestBean,TestBean2,TestBean3"};
		Generator.main(args);
	}

	@Test
	public void testMainWithServiceLoader() throws Exception {
		ServiceLoader serviceLoaderService = BDDMockito.mock(ServiceLoader.class);
		Statics.setServiceLoader(serviceLoaderService);
		// Test
		String[] args = new String[0];
		Generator.main(args);
		
		ViewGenerator viewGenerator = BDDMockito.mock(ViewGenerator.class);
		BDDMockito.when(serviceLoaderService.getViewGeneratorService()).thenReturn(viewGenerator);
		
		// Test
		Generator.main(args);
		BDDMockito.verify(viewGenerator,BDDMockito.times(1)).generatePages(BDDMockito.anyListOf(String.class));
	}
}
