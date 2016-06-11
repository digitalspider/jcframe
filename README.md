# JavaCloud Framework

The JavaCloud Framework (jcframe) makes building Java web CRUD applications really simple.

This project contains 2 artefacts:
* A [jcframe](https://github.com/digitalspider/jcsite/tree/master/jcframe) java library for building CRUD applications
* A [demo website](https://github.com/digitalspider/jcsite/tree/master/website), which is a sample war file using the jcframe code.

The development process is:
* Create a new database table schema, or get the connection properties to existing database
* Use a tool to generate a series of *beans* against the database schema
 * e.g. [oracle jpa page](http://www.oracle.com/technetwork/developer-tools/eclipse/jpatutorial-2-092215.html) or [eclipse jpa page](http://help.eclipse.org/juno/index.jsp?topic=%2Forg.eclipse.jpt.doc.user%2Ftasks021.htm)
 * A **bean** is an object representing a table in the database, extends **BaseBean**
* Import this JCFrame jar file into your maven
 * Download the source code using <code> git clone https://github.com/digitalspider/jcsite.git </code>
 * Run <code>maven clean install</code>
* Create a new WebApplication project and include the maven depenedency in *pom.xml*
```xml
    <!-- JavaCloud Framework -->
    <dependency>
      <groupId>au.com.javacloud</groupId>
      <artifactId>jcframe</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>
```
* Controllers are automatically created for each *bean*, however you can create custom ones like this:
```java
import java.security.Principal;
import au.com.javacloud.annotation.BeanClass;
import au.com.javacloud.model.Page;

public class PageController extends BaseControllerImpl<Page,Principal> {
    @BeanClass(bean = Page.class)
    public PageController() {
    }
}
```
* Create some jsp pages for the *bean* "page"
 * src/main/webapp/jsp/page/list.jsp
 * src/main/webapp/jsp/page/show.jsp
 * src/main/webapp/jsp/page/edit.jsp
 * src/main/webapp/jsp/page/index.jsp (optional)
* Populate these pages using the variable **${bean}** or **${beans}**
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
<a href="${beanUrl}/insert">Add Page</a>
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

package.model.name=au.com.javacloud.model
#auth.class=au.com.javacloud.auth.BaseAuthServiceImpl
#ds.class=au.com.javacloud.dao.BaseDataSource
#ds.config.file=db.properties.sample
```
* Build your application
 * <code>maven package</code>
* Deploy your application
 * Place the war file into a tomcat "webapps" directory
