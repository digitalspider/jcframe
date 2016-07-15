package au.com.jcloud.jcframe.view;

public enum ViewType {
	EDIT, LIST, SHOW, INDEX;
	
	public String getPageName() {
		return this.name().toLowerCase()+".jsp";
	}
}
