package au.com.javacloud.jcframe.service;

import java.util.List;

import au.com.javacloud.jcframe.controller.BaseController;
import au.com.javacloud.jcframe.dao.DAOActionEvent;
import au.com.javacloud.jcframe.model.BaseBean;

/**
 * Created by david on 20/06/16.
 */
public interface DAOLookupService {
    public List<BaseBean> getLookupMap(Class<? extends BaseBean> beanClass);
    public void registerController(Class<? extends BaseBean> beanClass, BaseController controller);
    public void unregisterController(Class<? extends BaseBean> beanClass, BaseController controller);
    public void fireDAOUpdate(DAOActionEvent event);
    public void addToLookupMap(Class<? extends BaseBean> lookupClass, BaseBean bean);
    public void deleteFromLookupMap(Class<? extends BaseBean> lookupClass, int id);
}
