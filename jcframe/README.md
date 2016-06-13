# JavaCloud Framework

The JavaCloud Framework (jcframe) makes building Java web CRUD applications really simple.

The details of the framewok is that is uses reflection to generate the necessary code to handle:
* DataAccessObjects (DAO)s - interface=**BaseDAO.java**, class=**BaseDAOImpl.java**
* HttpServlets - **FrontControllerServlet** delegates to interface=**BaseController.java**, class=**BaseControllerImpl.java**
* Security layer - interface **AuthService,java**, class=**BaseAuthServiceImpl.java**

These both handle bean objects called **BaseBean.java**

The reflection is mostly done in the class **ReflectUtil.java** and **Statics.java**

You can extend any of these classes to add your own functionality.

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
