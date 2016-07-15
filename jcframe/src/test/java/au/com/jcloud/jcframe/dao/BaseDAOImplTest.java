package au.com.jcloud.jcframe.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;

import au.com.jcloud.jcframe.annotation.TableName;
import au.com.jcloud.jcframe.model.BaseBean;
import au.com.jcloud.jcframe.service.DAOLookup;
import au.com.jcloud.jcframe.service.ServiceLoader;
import au.com.jcloud.jcframe.util.Statics;

public class BaseDAOImplTest {

	private BaseDAO<Integer,BaseBean<Integer>> testClass;
	private ServiceLoader serviceLoaderService;
	private DataSource dataSource;
	private DAOLookup daoLookupService;
	
	@TableName("try:this")
	private class TestSchemaBean extends BaseBean<Integer> {
	}
	
	@Before
	public void setup() {
		serviceLoaderService = BDDMockito.mock(ServiceLoader.class);
		Statics.setServiceLoader(serviceLoaderService);
		dataSource = BDDMockito.mock(DataSource.class);
		daoLookupService = BDDMockito.mock(DAOLookup.class);
		testClass = new BaseDAOImpl<Integer,BaseBean<Integer>>();
	}
	
	@Test
	public void testConstructor() {
		assertNotNull(testClass);
	}
	
	@Test
	public void testInit() throws IOException {
		Class clazz = BaseBean.class;
		testClass.init(clazz);

		// test with params
		testClass.init(clazz, dataSource, daoLookupService);
		assertEquals(dataSource,testClass.getDataSource());
	}
	
	@Test
	public void testInitWithSchema() throws IOException {
		DataSource tryDataSource = BDDMockito.mock(DataSource.class);
		BDDMockito.when(serviceLoaderService.getDataSource("try")).thenReturn(tryDataSource);
		
		BaseDAO<Integer,TestSchemaBean> dao = new BaseDAOImpl<>();
		dao.init(TestSchemaBean.class);
		assertEquals("this",dao.getTableName());
		assertEquals(tryDataSource,dao.getDataSource());
	}
	
	@Test
	public void testGetConnection() throws IOException, SQLException {
		Class clazz = BaseBean.class;
		testClass.init(clazz, dataSource, daoLookupService);
		assertEquals(dataSource,testClass.getDataSource());
		
		Connection conn = BDDMockito.mock(Connection.class);
		BDDMockito.when(dataSource.getConnection()).thenReturn(conn);
		
		Connection result = testClass.getConnection();
		assertEquals(conn,result);
		BDDMockito.verify(dataSource,BDDMockito.times(1)).getConnection();
		
		// Test second time, for cached result
		result = testClass.getConnection();
		assertEquals(conn,result);
		BDDMockito.verify(dataSource,BDDMockito.times(1)).getConnection();
		
		// Test with closed connection
		BDDMockito.when(conn.isClosed()).thenReturn(true);
		result = testClass.getConnection();
		assertEquals(conn,result);
		BDDMockito.verify(dataSource,BDDMockito.times(2)).getConnection();
	}
	
	@Test
	public void testGetBeanClass() throws IOException {
		// Test null
		Class<BaseBean<Integer>> classType = testClass.getBeanClass();
		assertEquals(null,classType);

		// Test BaseBean
		Class clazz = BaseBean.class;
		testClass.init(clazz);
		classType = testClass.getBeanClass();
		assertEquals(BaseBean.class,classType);
		
		// Test CustomBean
		BaseDAO<Integer,TestSchemaBean> dao = new BaseDAOImpl<>();
		dao.init(TestSchemaBean.class);
		Class<TestSchemaBean> classType2 = dao.getBeanClass();
		assertEquals(TestSchemaBean.class,classType2);
	}
}
