package au.com.javacloud.jcframe.dao;

import au.com.javacloud.jcframe.model.BaseBean;

/**
 * Created by david on 19/06/16.
 */
public class DAOActionEvent<T extends BaseBean> {
    private int id;
    Class<T> beanClass;
    private T bean;
    private DAOEventType eventType;

    public DAOActionEvent(int id, Class<T> beanClass, T bean, DAOEventType eventType) {
        this.id = id;
        this.beanClass = beanClass;
        this.bean = bean;
        this.eventType = eventType;
    }

    public int getId() {
        return id;
    }

    public Class<T> getBeanClass() {
        return beanClass;
    }

    public T getBean() {
        return bean;
    }

    public DAOEventType getEventType() {
        return eventType;
    }
}
