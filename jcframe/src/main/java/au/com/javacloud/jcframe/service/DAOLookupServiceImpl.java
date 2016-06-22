package au.com.javacloud.jcframe.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

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
    private Map<Class<? extends BaseBean>,List<BaseController<? extends BaseBean, ?>>> controllerMap = new HashMap<Class<? extends BaseBean>,List<BaseController<? extends BaseBean, ?>>>();
    protected Map<Class<? extends BaseBean>,List<BaseBean>> lookupMap = new HashMap<Class<? extends BaseBean>, List<BaseBean>>();

    @SuppressWarnings("unchecked")
	@Override
    public List<BaseBean> getLookupMap(Class<? extends BaseBean> beanClass) {
        List<BaseBean> lookupData = lookupMap.get(beanClass);
        if (lookupData==null) {
            LOG.info("Initialising lookupMap for class="+beanClass.getSimpleName());
            BaseDAO<? extends BaseBean> lookupDao = Statics.getDaoMap().get(beanClass);
            LOG.info("lookupDao="+lookupDao);
            try {
                lookupData = (List<BaseBean>) lookupDao.getLookup();
            } catch (Exception e) {
                LOG.error(e,e);
                lookupData = new ArrayList<BaseBean>();
            }
            LOG.info("lookupData.size()="+lookupData.size());
            lookupMap.put(beanClass, lookupData);
        }
        return lookupData;
    }

    @Override
    public void registerController(Class<? extends BaseBean> beanClass, BaseController<? extends BaseBean, ?> controller) {
        List<BaseController<? extends BaseBean, ?>> controllerList = getInitialisedControllerList(beanClass);
        controllerList.add(controller);
    }

    @Override
    public void unregisterController(Class<? extends BaseBean> beanClass, BaseController<? extends BaseBean, ?> controller) {
        List<BaseController<? extends BaseBean, ?>> controllerList = getInitialisedControllerList(beanClass);
        if (controllerList.contains(controller)) {
            controllerList.remove(controller);
        }
    }

    @Override
    public <T extends BaseBean> void fireDAOUpdate(DAOActionEvent<T> event) {
        Class<T> beanClass = event.getBeanClass();
        switch (event.getEventType()) {
            case INSERT:
                addToLookupMap(beanClass, event.getBean());
                break;
            case DELETE:
                deleteFromLookupMap(beanClass, event.getId());
                break;
        }
        for (BaseController<? extends BaseBean, ?> controller : (List<BaseController<? extends BaseBean, ?>>) getInitialisedControllerList(beanClass)) {
            controller.reloadLookupMap();
        }
    }

    @Override
    public void addToLookupMap(Class<? extends BaseBean> beanClass, BaseBean bean) {
        LOG.debug("beanClass="+beanClass.getName());
        try {
            List<BaseBean> beans = getLookupMap(beanClass);
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
            List<BaseBean> beans = getLookupMap(beanClass);
            LOG.info("Deleting bean "+beanClass.getSimpleName()+" [id="+id+"]");
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

	private List<BaseController<? extends BaseBean, ?>>  getInitialisedControllerList(Class<? extends BaseBean> beanClass) {
        List<BaseController<? extends BaseBean, ?>> controllerList = controllerMap.get(beanClass);
        if (controllerList==null) {
            controllerList = new ArrayList<BaseController<? extends BaseBean, ?>>();
            controllerMap.put(beanClass, controllerList);
        }
        return controllerList;
    }
}
