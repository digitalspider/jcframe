# JavaCloud Framework

The JavaCloud Framework (jcframe) makes building Java web CRUD applications really simple.

The details of the framewok is that is uses reflection to generate the necessary code to handle:
* DataAccessObjects (DAO)s - in interface **BaseDAO.java** and class **BaseDAOImpl.java**
* HttpServlets - in interface **BaseController.java** and class **BaseControllerImpl.java**

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
