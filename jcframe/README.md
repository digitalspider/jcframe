# JavaCloud Framework

The JavaCloud Framework (jcframe) makes building Java web CRUD applications really simple.

The details of the framewok is that is uses reflection to generate the necessary code to handle:
* DataAccessObjects (DAO)s - interface=**BaseDAO.java**, class=**BaseDAOImpl.java**
* HttpServlets - **FrontControllerServlet** delegates to interface=**BaseController.java**, class=**BaseControllerImpl.java**
* Security layer - interface **AuthService.java**, class=**BaseAuthServiceImpl.java**
* ViewGeneration - interface **ViewGenerator.java**, class=**ViewGeneratorImpl.java**

These handle bean objects that extend **BaseBean.java**

The reflection is mostly done in the class **ReflectUtil.java** and **Statics.java**

You can extend any of these classes to add your own functionality.

Special **annotation** can be added to BaseBeans.
```java
@Secure(roles = "admin")
@TableName("Page")
@DisplayValueColumn("title")
@DisplayOrder("title,url,description,content,tags,type,status,authorId,parentId")
public class Page extends BaseBean {
}

The **BaseController** interface provides:
```java
public void list();
public void create();
public void read();
public void update(String id);
public void delete();
public void find();
public void config();
```

The **BaseDAO** interface provides:
```java
public void saveOrUpdate(T bean);
public List<T> getAll(int pageNo);
public List<T> getLookup();
public T getLookup(int id);
public T get(int id);
public void delete(int beanId);
public List<T> find(String field, String value, int pageNo);
```

The **AuthService** interface provides:
```java
public U getUser(HttpServletRequest request);
public boolean isAuthenticated(HttpServletRequest request);
public <T extends BaseBean> boolean checkACL(U user, Class<T> classType, Action action);
```

The **ViewGenerator** interface provides:
```java
public void generatePages(List<String> beans);
public Map<ViewType,String> getContentTemplates(String templatePageDirectory);
public List<Method> sortMethodMap(final Map<Method, Class> methodMap, final String[] orderList);
public boolean validForView(ViewType viewType, Class classType, String fieldName);
public String generateView(ViewType viewType, String beanName, Class classType, Map<Method,Class> methodMap);
public String getTemplatedContent(ViewType viewType, String template, String fieldName, String fieldHeader, String type, String other, boolean isHtml, boolean isBean);
public String getTemplate(ViewType viewType, boolean isBean);
```

