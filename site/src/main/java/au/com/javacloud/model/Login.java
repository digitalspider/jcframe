package au.com.javacloud.model;

import au.com.javacloud.annotation.HiddenBean;
import au.com.javacloud.annotation.TableName;

/**
 * Created by david on 22/05/16.
 */
@HiddenBean
@TableName("User")
public class Login extends BaseBean {
    private String username;
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
