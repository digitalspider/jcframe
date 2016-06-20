# JavaCloud Framework

The JavaCloud Framework (jcframe) makes building Java web CRUD applications really simple.

This project contains 2 artefacts:
* A [jcframe](https://github.com/digitalspider/jcframe/tree/master/jcframe) java library for building CRUD applications
* A [demo website](https://github.com/digitalspider/jcframe/tree/master/site), which is a sample war file using the jcframe code.

The development process is:
* Create a new database table schema, or get the connection properties to existing database
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
import au.com.javacloud.model.Page;

@BeanClass(Page.class)
public class PageController extends BaseControllerImpl<Page,Principal> {
    @Override
    public void doAction(ServletAction action, PathParts pathParts, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doAction(action, pathParts, request, response);
    }
}
```
* Generate some jsp pages for the *bean* "page" by running "site/generate.sh page"
 * src/main/webapp/jsp/page/list.jsp
 * src/main/webapp/jsp/page/show.jsp
 * src/main/webapp/jsp/page/edit.jsp
 * src/main/webapp/jsp/page/index.jsp (optional)
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
<a href="${beanUrl}/insertStmt">Add Page</a>
```
* Create a new file for the database configuration in
 * src/main/resources/**db.properties**
```properties
# MySQL
driver=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/TestDB
username=test
password=test

# SQLite
#driver=org.sqlite.JDBC
#url=jdbc:sqlite:${REALPATH}database.db
#username=test
#password=test
```
* Create a new file for the javacloud configuration in
 * src/main/resources/**jc.properties**
```properties
# JavaCloud configuration file

package.name=com.mysite

#auth.class=au.com.javacloud.jcframe.auth.BaseAuthServiceImpl
#daolookup.class=au.com.javacloud.jcframe.service.DAOLookupServiceImpl
#viewgen.class=au.com.javacloud.jcframe.view.ViewGeneratorImpl
#ds.class=au.com.javacloud.jcframe.dao.BaseDataSource
#ds.config.file=db.properties
```
* Build your application
 * <code>mvn package</code>
* Deploy your application
 * Place the war file into a tomcat "webapps" directory
