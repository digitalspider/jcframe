package au.com.javacloud.model;

import au.com.javacloud.annotation.Exclude;
import au.com.javacloud.annotation.GSonExclude;

/**
 * Created by david on 22/05/16.
 */

public class BaseBean {
    protected int id;
    protected String displayValue;

    public final static String FIELD_ID = "id";
    public final static String FIELD_DISPLAYVALUE = "displayValue";

    @Override
    public String toString() {
        return getClass().getSimpleName()+"["+id+"] displayValue="+displayValue;
    }

    @Exclude
    public String getNameColumn() {
    	return FIELD_ID;
    }

    @Exclude
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Exclude
    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

}
