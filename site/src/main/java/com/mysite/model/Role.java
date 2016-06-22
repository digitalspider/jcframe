package com.mysite.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import au.com.javacloud.jcframe.annotation.DisplayHeader;
import au.com.javacloud.jcframe.annotation.DisplayOrder;
import au.com.javacloud.jcframe.annotation.DisplayType;
import au.com.javacloud.jcframe.annotation.DisplayValueColumn;
import au.com.javacloud.jcframe.annotation.ExcludeDBWrite;
import au.com.javacloud.jcframe.annotation.ExcludeView;
import au.com.javacloud.jcframe.annotation.TableName;
import au.com.javacloud.jcframe.model.BaseBean;

/**
 * Created by david on 22/05/16.
 */
@TableName("roles")
@DisplayValueColumn("rolename")
public class Role extends BaseBean {
    private String rolename;

//    @Override
//    public String toString() {
//        StringBuffer result = new StringBuffer(super.toString());
//        if (StringUtils.isNotBlank(rolename)) {
//            result.append(", rolename=" + rolename);
//        }
//        return result.toString();
//    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }
}
