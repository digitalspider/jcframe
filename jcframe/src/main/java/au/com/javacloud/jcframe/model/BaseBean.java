package au.com.javacloud.jcframe.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import au.com.javacloud.jcframe.annotation.DisplayType;
import au.com.javacloud.jcframe.annotation.ExcludeView;
import au.com.javacloud.jcframe.annotation.ExcludeDBWrite;
import au.com.javacloud.jcframe.util.FieldMetaData;
import au.com.javacloud.jcframe.util.ReflectUtil;

/**
 * Created by david on 22/05/16.
 */

public class BaseBean<ID> {

    @ExcludeDBWrite
    protected ID id;
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
                    boolean display = true;
                    if (field.isAnnotationPresent(DisplayType.class)) {
                        String displayType = field.getAnnotation(DisplayType.class).value();
                        if (displayType.equals("password")) {
                            display = false;
                        }
                    }
                    if (display) {
                        Object value = method.invoke(this);
                        if (value != null && StringUtils.isNotBlank(value.toString())) {
                            result.append(", " + field.getName() + "=" + value);
                        }
                    }
                }
            } catch (Exception e) {
                // ignore
            }
        }
        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseBean baseBean = (BaseBean) o;

        return getId() == baseBean.getId();

    }

    @Override
    public int hashCode() {
        return (""+getId()).hashCode();
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

}
