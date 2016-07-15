package au.com.jcloud.jcframe.dao;

import au.com.jcloud.jcframe.model.BaseBean;

/**
 * Created by david on 19/06/16.
 */
public class DAOActionEvent<ID, Bean extends BaseBean<ID>> {
    private ID id;
    Class<Bean> beanClass;
    private Bean bean;
    private DAOEventType eventType;

    public DAOActionEvent(ID id, Class<Bean> beanClass, Bean bean, DAOEventType eventType) {
        this.id = id;
        this.beanClass = beanClass;
        this.bean = bean;
        this.eventType = eventType;
    }

    public ID getId() {
        return id;
    }

    public Class<Bean> getBeanClass() {
        return beanClass;
    }

    public Bean getBean() {
        return bean;
    }

    public DAOEventType getEventType() {
        return eventType;
    }
}
