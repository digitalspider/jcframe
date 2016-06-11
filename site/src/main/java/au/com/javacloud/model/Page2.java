package au.com.javacloud.model;

import java.util.Date;

import au.com.javacloud.annotation.Exclude;
import au.com.javacloud.annotation.TableName;

/**
 * Created by david on 22/05/16.
 */

public class Page2 extends BaseBean {
    protected Date cdate = new Date();
    protected Date mdate = new Date();
    protected String description;
    protected String tags;
    protected String type;
    protected String status;
    protected User authorId;
    protected Page2 parentId;
    private String title;
    private String content;
    private String url;

    @TableName(name="Page")
    public Page2() {

    }
    @Exclude
    public String getNameColumn() {
        return "title";
    }

    @Override
    public String toString() {
        return super.toString()+
        		", cdate=" + cdate +
                ", mdate=" + mdate +
                ", description=" + description +
                ", tags=" + tags +
                ", type=" + type +
                ", status=" + status +
                ", authorId=" + authorId +
                ", parentId=" + parentId +
                ", title=" + title +
                ", content=" + content +
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

    public User getAuthorId() {
        return authorId;
    }

    public void setAuthorId(User authorId) {
        this.authorId = authorId;
    }

    public Page2 getParentId() {
        return parentId;
    }

    public void setParentId(Page2 parentId) {
        this.parentId = parentId;
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
