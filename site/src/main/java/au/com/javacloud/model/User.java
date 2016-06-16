package au.com.javacloud.model;

import java.util.Date;

import au.com.javacloud.annotation.DisplayHeader;
import au.com.javacloud.annotation.DisplayOrder;
import au.com.javacloud.annotation.DisplayType;
import au.com.javacloud.annotation.DisplayValueColumn;
import au.com.javacloud.annotation.ExcludeDBWrite;
import au.com.javacloud.annotation.ExcludeView;

/**
 * Created by david on 22/05/16.
 */
@DisplayValueColumn("email")
@DisplayOrder("username,email,firstname,lastname,type,description")
public class User extends BaseBean {
	@ExcludeView(pages="edit,list")
	@ExcludeDBWrite
	@DisplayHeader("Created Date")
    private Date cdate = new Date();
	@ExcludeView(pages="edit,list")
	@ExcludeDBWrite
	@DisplayHeader("Last Modified Date")
    private Date mdate = new Date();
    private String description;
    private String tags;
    private String type;
    private String status;
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    @DisplayType("password")
    private String password;
    private String url;
    private String mobile;
    private String token;
    private String image;

    @Override
    public String toString() {
        return super.toString()+
        		", cdate=" + cdate +
                ", mdate=" + mdate +
                ", description=" + description +
                ", tags=" + tags +
                ", type=" + type +
                ", status=" + status +
                ", firstname=" + firstname +
                ", lastname=" + lastname +
                ", email=" + email +
                ", username=" + username +
                ", url=" + url;
    }
    
    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }

    public Date getMdate() {
        return mdate;
    }

    public void setMdate(Date mdate) {
        this.mdate = mdate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
