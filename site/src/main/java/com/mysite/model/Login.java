package com.mysite.model;

import au.com.jcloud.jcframe.annotation.DisplayType;
import au.com.jcloud.jcframe.annotation.HiddenBean;
import au.com.jcloud.jcframe.annotation.TableName;
import au.com.jcloud.jcframe.model.BaseBean;

/**
 * Created by david on 22/05/16.
 */
@HiddenBean
@TableName("User")
public class Login extends BaseBean<Integer> {
    private String username;
    @DisplayType("password")
    private String password;

    @Override
    public String toString() {
        return super.toString()+ ", username=" + username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
