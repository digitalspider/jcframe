package com.mysite.model;

import java.util.Date;
import java.util.List;

import au.com.javacloud.jcframe.annotation.DisplayHeader;
import au.com.javacloud.jcframe.annotation.DisplayOrder;
import au.com.javacloud.jcframe.annotation.DisplayType;
import au.com.javacloud.jcframe.annotation.DisplayValueColumn;
import au.com.javacloud.jcframe.annotation.ExcludeDBWrite;
import au.com.javacloud.jcframe.annotation.ExcludeView;
import au.com.javacloud.jcframe.annotation.LinkField;
import au.com.javacloud.jcframe.annotation.M2MTable;
import au.com.javacloud.jcframe.model.BaseBean;

/**
 * Created by david on 22/05/16.
 */
@DisplayValueColumn("email")
@DisplayOrder("username,email,firstname,lastname,type,description")
public class User extends BaseBean {
	@ExcludeView(pages="edit,list")
	@ExcludeDBWrite
	@DisplayHeader("Created Date")
    private Date cdate;
	@ExcludeView(pages="edit,list")
	@ExcludeDBWrite
	@DisplayHeader("Last Modified Date")
    private Date mdate;
    private String description;
    private List<String> tags;
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
    @M2MTable(table="userroles", column="userid", rcolumn="roleid")
    private List<Role> roles;
    @LinkField("author")
    private List<Page> pages;
    
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }
}
