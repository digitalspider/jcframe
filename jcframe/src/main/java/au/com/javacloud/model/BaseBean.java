package au.com.javacloud.model;

import au.com.javacloud.annotation.ExcludeDBWrite;
import au.com.javacloud.annotation.ExcludeView;

/**
 * Created by david on 22/05/16.
 */

public class BaseBean {
	@ExcludeDBWrite
    protected int id;
	@ExcludeView
    @ExcludeDBWrite
    protected String displayValue;

    public final static String FIELD_ID = "id";
    public final static String FIELD_DISPLAYVALUE = "displayValue";

    @Override
    public String toString() {
        return getClass().getSimpleName()+"["+id+"] "+displayValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

}
