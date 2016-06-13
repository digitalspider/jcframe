package au.com.javacloud.model;

import java.util.Date;

import au.com.javacloud.annotation.Exclude;
import au.com.javacloud.annotation.Hidden;
import au.com.javacloud.annotation.Secure;
import au.com.javacloud.annotation.TableName;

/**
 * Created by david on 22/05/16.
 */
@Hidden
@TableName(name = "User")
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
