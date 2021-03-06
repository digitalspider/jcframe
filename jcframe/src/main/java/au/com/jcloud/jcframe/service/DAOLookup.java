package au.com.jcloud.jcframe.service;

import java.util.List;

import au.com.jcloud.jcframe.controller.BaseController;
import au.com.jcloud.jcframe.dao.DAOActionEvent;
import au.com.jcloud.jcframe.model.BaseBean;

/**
 * Created by david on 20/06/16.
 */
public interface DAOLookup {
    public List<BaseBean> getLookupList(Class<? extends BaseBean> beanClass);
    public void registerController(Class<? extends BaseBean> beanClass, BaseController<?,? extends BaseBean, ?> controller);
    public void unregisterController(Class<? extends BaseBean> beanClass, BaseController<?,? extends BaseBean, ?> controller);
    public <ID, Bean extends BaseBean<ID>> void fireDAOUpdate(DAOActionEvent<ID, Bean> event);
    public void addToLookupMap(Class<? extends BaseBean> lookupClass, BaseBean bean);
    public <ID> void deleteFromLookupMap(Class<? extends BaseBean> lookupClass, ID id);
}
