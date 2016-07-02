package com.mysite.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import au.com.javacloud.jcframe.annotation.DisplayHeader;
import au.com.javacloud.jcframe.annotation.DisplayOrder;
import au.com.javacloud.jcframe.annotation.DisplayType;
import au.com.javacloud.jcframe.annotation.DisplayValueColumn;
import au.com.javacloud.jcframe.annotation.ExcludeDBWrite;
import au.com.javacloud.jcframe.annotation.ExcludeView;
import au.com.javacloud.jcframe.annotation.Secure;
import au.com.javacloud.jcframe.annotation.TableName;
import au.com.javacloud.jcframe.model.BaseBean;
import au.com.javacloud.jcframe.util.DisplayDate;

/**
 * Created by david on 22/05/16.
 */

@Secure(roles = "admin")
@TableName("Page")
@DisplayValueColumn("title")
@DisplayOrder("title,url,description,content,tags,type,status,authorId,parentId")
public class Page extends BaseBean<Integer> {
	@ExcludeView(pages="edit,list")
	@ExcludeDBWrite
	@DisplayHeader("Created Date")
    protected Date cdate;
	@ExcludeView(pages="edit,list")
	@ExcludeDBWrite
	@DisplayHeader("Last Modified Date")
	protected Date mdate;
    @DisplayType("html")
    protected String description;
    protected String tags;
    protected String type;
    protected String status;
    @DisplayHeader("Author")
    protected User author;
    @DisplayHeader("Parent Page")
    protected Page parent;
    @DisplayType("html")
    private String title;
    @DisplayType("html")
    private String content;
    private String url;

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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Page getParent() {
        return parent;
    }

    public void setParent(Page parent) {
        this.parent = parent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
