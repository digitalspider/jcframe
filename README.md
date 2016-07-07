# JavaCloud Framework

The JavaCloud Framework (jcframe) makes building Java web CRUD applications really simple.

This project contains 2 artefacts:
* A [jcframe](https://github.com/digitalspider/jcframe/tree/master/jcframe) java library for building CRUD applications
* A [demo website](https://github.com/digitalspider/jcframe/tree/master/site), which is a sample war file using the jcframe code.

The development process is:
* Create a new database table schema, or get the connection properties to existing database
* Create a new database configuration file
 * src/main/resources/**db.properties**
 * Note: Different databases can be accessed through the annotation <code>@TableName("myschema:Table")</code>
```properties
# MySQL
myschema.driver=com.mysql.jdbc.Driver
myschema.url=jdbc:mysql://localhost:3306/TestDB
myschema.username=test
myschema.password=test

# SQLite
default.driver=org.sqlite.JDBC
default.url=jdbc:sqlite:${REALPATH}database.db
default.username=test
default.password=test
```
* Create a javacloud configuration file
 * src/main/resources/**jc.properties**
```properties
# JavaCloud configuration file

package.name=com.mysite

#serviceloader.class=au.com.javacloud.jcframe.service.ServiceLoaderImpl
#auth.class=au.com.javacloud.jcframe.auth.BaseAuthServiceImpl
#daolookup.class=au.com.javacloud.jcframe.service.DAOLookupImpl
#viewgen.class=au.com.javacloud.jcframe.view.ViewGeneratorImpl
#ds.class=au.com.javacloud.jcframe.dao.BaseDataSource
#ds.config.file=db.properties
```
* Use a tool to generate a series of *beans* against the database schema
 * A **bean** is an object representing a table in the database, extends **BaseBean**
 * e.g. [oracle jpa page](http://www.oracle.com/technetwork/developer-tools/eclipse/jpatutorial-2-092215.html) or [eclipse jpa page](http://help.eclipse.org/juno/index.jsp?topic=%2Forg.eclipse.jpt.doc.user%2Ftasks021.htm)
* Import this JCFrame jar file into your maven
 * Download the source code using <code> git clone https://github.com/digitalspider/jcsite.git </code>
 * Run <code>mvn clean install</code>
* Create a new WebApplication project and include the maven depenedency in *pom.xml*
```xml
    <!-- JavaCloud Framework -->
    <dependency>
      <groupId>au.com.javacloud</groupId>
      <artifactId>jcframe</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>
```
* DAO and Controllers are automatically created for each *bean*, however you can create custom ones like this:
 * For more details see [jcframe](https://github.com/digitalspider/jcframe/tree/master/jcframe)
 * For complete override of all Controllers set **@BeanClass(BaseBean.class)**
```java
import java.security.Principal;
import au.com.javacloud.annotation.BeanClass;
import au.com.javacloud.model.MyData;

@BeanClass(MyData.class)
public class MyDataController extends BaseControllerImpl<MyData,Principal> {
    @Override
    public void doAction(ServletAction action, PathParts pathParts, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doAction(action, pathParts, request, response);
    }
}
```
* Generate some jsp pages for the *bean* "MyData" by running "site/generate.sh mydata"
 * src/main/webapp/jsp/mydata/list.jsp
 * src/main/webapp/jsp/mydata/show.jsp
 * src/main/webapp/jsp/mydata/edit.jsp
 * src/main/webapp/jsp/mydata/index.jsp (optional, based on @IndexPage)
 * **Note:** Create your own custom templates in:
  * src/main/webapp/jsp/jctemplate/<list/index/edit/show>.jsp (for page templates)
  * src/main/webapp/jsp/jctemplate/<view>/<list/index/edit/show>.jsp (for field templates)
* These pages use the variables **${bean}** or **${beans}**
 * e.g. content for *list.jsp*
```html
<c:forEach items="${beans}" var="bean">
    <tr>
        <td><a href="${beanUrl}/show/<c:out value='${bean.id}'/>"><c:out value="${bean.id}" /></a></td>
        <td><c:out value="${bean.title}"/></td>
        <td><c:out value="${bean.description}" escapeXml="false"/></td>
        <td><a href="${beanUrl}/edit/<c:out value='${bean.id}'/>">Update</a></td>
        <td><a href="${beanUrl}/delete/<c:out value='${bean.id}'/>">Delete</a></td>
    </tr>
</c:forEach>
<a href="${beanUrl}/insertStmt">Add MyData</a>
```
* Build your application
 * <code>mvn package</code>
* Deploy your application
 * Place the war file into a tomcat "webapps" directory

# TODO
* Implement AttachmentService = File Upload / display
* Implement bean generator from DB metadata
* Implement AJAX mechanism
* Move DBDateFormat to new DataSourceWrapper
* Implement custom @DBColumnName attribute
* Implement default templates in jcframe.jar
* Add Tests for ViewGeneratorImpl
* Add Tests for BaseControllerImpl
* Add Tests for BaseDAOImpl
* Add Tests for FrontControllerServlet
* Add ITTests for Database Testing
* Implement custom ID types = :white_check_mark: 06/07/2016
* Implement M2MService = Many-to-Many i.e. List<BaseBean> :white_check_mark: 04/07/2016
* Implement ServiceLoaderService = abstract away some of statics :white_check_mark: 27/06/2016
* Implement namespaces in db.properties files, and in @TableName("schema:Table") :white_check_mark: 27/06/2016
* Refactor ReflectUtil to create a centralised FieldMetaData with setMethod, getMethod, Field, ClassType :white_check_mark: 26/06/2016

