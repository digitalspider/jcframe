package au.com.javacloud.jcframe.service;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.javacloud.jcframe.controller.BaseController;
import au.com.javacloud.jcframe.dao.BaseDAO;
import au.com.javacloud.jcframe.dao.DAOActionEvent;
import au.com.javacloud.jcframe.model.BaseBean;
import au.com.javacloud.jcframe.util.Statics;

/**
 * Created by david on 20/06/16.
 */
public class DAOLookupServiceImpl implements DAOLookupService {

    private final static Logger LOG = Logger.getLogger(DAOLookupServiceImpl.class);
    private Map<Class,List<BaseController>> controllerMap = new HashMap<Class,List<BaseController>>();
    protected Map<Class,List<BaseBean>> lookupMap = new HashMap<Class, List<BaseBean>>();

    public List<BaseBean> getLookupMap(Class<? extends BaseBean> beanClass) {
        List<BaseBean> lookupData = lookupMap.get(beanClass);
        if (lookupData==null) {
            BaseDAO lookupDao = Statics.getDaoMap().get(beanClass);
            try {
                lookupData = lookupDao.getLookup();
            } catch (Exception e) {
                LOG.error(e,e);
            }
            lookupMap.put(beanClass, lookupData);
        }
        return lookupData;
    }

    public void registerController(Class<? extends BaseBean> beanClass, BaseController controller) {
        List<BaseController> controllerList = controllerMap.get(beanClass);
        if (controllerList==null) {
            controllerList = new ArrayList<BaseController>();
            controllerMap.put(beanClass, controllerList);
        }
        controllerList.add(controller);
    }

    public void unregisterController(Class<? extends BaseBean> beanClass, BaseController controller) {
        List<BaseController> controllerList = controllerMap.get(beanClass);
        if (controllerList==null) {
            controllerList = new ArrayList<BaseController>();
            controllerMap.put(beanClass, controllerList);
        }
        if (controllerList.contains(controller)) {
            controllerList.remove(controller);
        }
    }

    public void fireDAOUpdate(DAOActionEvent event) {
        Class beanClass = event.getClass();
        switch (event.getEventType()) {
            case INSERT:
                addToLookupMap(beanClass, event.getBean());
                break;
            case DELETE:
                deleteFromLookupMap(beanClass, event.getId());
                break;
        }
        for (BaseController controller : controllerMap.get(beanClass)) {
            controller.reloadLookupMap();
        }
    }

    @Override
    public void addToLookupMap(Class<? extends BaseBean> beanClass, BaseBean bean) {
        LOG.debug("beanClass="+beanClass.getName());
        try {
            List<BaseBean> beans = lookupMap.get(beanClass);
            LOG.info("Adding bean "+bean);
            beans.add(bean);
        } catch (Exception e) {
            LOG.error(e,e);
        }
    }

    @Override
    public void deleteFromLookupMap(Class<? extends BaseBean> beanClass, int id) {
        LOG.debug("beanClass="+beanClass.getName());
        try {
            List<BaseBean> beans = lookupMap.get(beanClass);
            for (BaseBean bean : beans) {
                if (bean.getId() == id) {
                    beans.remove(bean);
                    break;
                }
            }
        } catch (Exception e) {
            LOG.error(e,e);
        }
    }
}
