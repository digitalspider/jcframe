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
```

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
public void saveOrUpdate(Bean bean);
public void saveOrUpdate(List<Bean> beanList);
public int count();
public int count(String field, String value, boolean exact);
public List<Bean> getAll(int pageNo, boolean populateBean);
public List<Bean> getLookupList();
public Bean get(ID id, boolean populateBean);
public List<Bean> get(List<ID> idList, boolean populateBeans);
public void delete(ID id);
public void delete(List<ID> idList);
public List<Bean> find(String field, String value, int pageNo, boolean exact, boolean populateBean);
```

The **AuthService** interface provides:
```java
public Bean getUser(HttpServletRequest request);
public boolean isAuthenticated(HttpServletRequest request);
public <Bean extends BaseBean> boolean checkACL(Bean user, Class<Bean> classType, Action action);
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

