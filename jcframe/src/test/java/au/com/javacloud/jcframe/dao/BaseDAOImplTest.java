package au.com.javacloud.jcframe.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;

import au.com.javacloud.jcframe.annotation.TableName;
import au.com.javacloud.jcframe.model.BaseBean;
import au.com.javacloud.jcframe.service.DAOLookupService;
import au.com.javacloud.jcframe.service.ServiceLoaderService;
import au.com.javacloud.jcframe.util.Statics;

public class BaseDAOImplTest {

	private BaseDAO<BaseBean> testClass;
	private ServiceLoaderService serviceLoaderService;
	private DataSource dataSource;
	private DAOLookupService daoLookupService;
	
	@TableName("try:this")
	private class TestSchemaBean extends BaseBean {
	}
	
	@Before
	public void setup() {
		serviceLoaderService = BDDMockito.mock(ServiceLoaderService.class);
		Statics.setServiceLoaderService(serviceLoaderService);
		dataSource = BDDMockito.mock(DataSource.class);
		daoLookupService = BDDMockito.mock(DAOLookupService.class);
		testClass = new BaseDAOImpl<BaseBean>();
	}
	
	@Test
	public void testConstructor() {
		assertNotNull(testClass);
	}
	
	@Test
	public void testInit() throws IOException {
		testClass.init(BaseBean.class);
		
		// test with params
		testClass.init(BaseBean.class, dataSource, daoLookupService);
		assertEquals(dataSource,testClass.getDataSource());
	}
	
	@Test
	public void testInitWithSchema() throws IOException {
		DataSource tryDataSource = BDDMockito.mock(DataSource.class);
		BDDMockito.when(serviceLoaderService.getDataSource("try")).thenReturn(tryDataSource);
		
		BaseDAO<TestSchemaBean> dao = new BaseDAOImpl<>();
		dao.init(TestSchemaBean.class);
		assertEquals("this",dao.getTableName());
		assertEquals(tryDataSource,dao.getDataSource());
	}
	
	@Test
	public void testGetConnection() throws IOException, SQLException {
		testClass.init(BaseBean.class, dataSource, daoLookupService);
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
		Class classType = testClass.getBeanClass();
		assertEquals(null,classType);

		// Test BaseBean
		testClass.init(BaseBean.class);
		classType = testClass.getBeanClass();
		assertEquals(BaseBean.class,classType);
		
		// Test CustomBean
		BaseDAO<TestSchemaBean> dao = new BaseDAOImpl<>();
		dao.init(TestSchemaBean.class);
		classType = dao.getBeanClass();
		assertEquals(TestSchemaBean.class,classType);
	}
}
