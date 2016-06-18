package au.com.javacloud.model;

import java.util.Date;

import au.com.javacloud.annotation.DisplayHeader;
import au.com.javacloud.annotation.DisplayValueColumn;
import au.com.javacloud.annotation.ExcludeDBWrite;
import au.com.javacloud.annotation.ExcludeView;
import au.com.javacloud.annotation.Secure;
import au.com.javacloud.annotation.TableName;
import au.com.javacloud.annotation.DisplayHtml;
import au.com.javacloud.annotation.DisplayOrder;

/**
 * Created by david on 22/05/16.
 */

@Secure(roles = "admin")
@TableName("Page")
@DisplayValueColumn("title")
@DisplayOrder("title,url,description,content,tags,type,status,authorId,parentId")
public class Page extends BaseBean {
	@ExcludeView(pages="edit,list")
	@ExcludeDBWrite
	@DisplayHeader("Created Date")
    protected Date cdate = new Date();
	@ExcludeView(pages="edit,list")
	@ExcludeDBWrite
	@DisplayHeader("Last Modified Date")
	protected Date mdate = new Date();
    @DisplayHtml
    protected String description;
    protected String tags;
    protected String type;
    protected String status;
    @DisplayHeader("Author")
    protected User authorId;
    @DisplayHeader("Parent Page")
    protected Page parentId;
    @DisplayHtml
    private String title;
    @DisplayHtml
    private String content;
    private String url;

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

    public Page getParentId() {
        return parentId;
    }

    public void setParentId(Page parentId) {
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
