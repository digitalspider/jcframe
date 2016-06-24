package au.com.javacloud.jcframe.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import au.com.javacloud.jcframe.annotation.ExcludeView;
import au.com.javacloud.jcframe.annotation.ExcludeDBWrite;
import au.com.javacloud.jcframe.util.FieldMetaData;
import au.com.javacloud.jcframe.util.ReflectUtil;

/**
 * Created by david on 22/05/16.
 */

public class BaseBean {
	@ExcludeDBWrite
    protected int id;
	@ExcludeView
    @ExcludeDBWrite
    protected String displayValue = "";

    public final static String FIELD_ID = "id";
    public final static String FIELD_DISPLAYVALUE = "displayValue";

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer(getClass().getSimpleName()+"["+id+"] "+displayValue);
        List<FieldMetaData> fieldMetaDataList = ReflectUtil.getFieldData(getClass());
        for (FieldMetaData fieldMetaData : fieldMetaDataList) {
            try {
                Field field = fieldMetaData.getField();
                Method method = fieldMetaData.getGetMethod();
                if (field!=null && !field.getName().equals(FIELD_ID) && !field.getName().equals(FIELD_DISPLAYVALUE)
                        && method!=null) {
                    Object value = method.invoke(this);
                    if (value != null && StringUtils.isNotBlank(value.toString())) {
                        result.append(", " + field.getName() + "=" + value);
                    }
                }
            } catch (Exception e) {
                // ignore
            }
        }
        return result.toString();
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
