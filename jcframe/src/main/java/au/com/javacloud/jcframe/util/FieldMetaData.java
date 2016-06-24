package au.com.javacloud.jcframe.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by david on 24/06/16.
 */
public class FieldMetaData {
    private Field field;
    private Method setMethod;
    private Method getMethod;
    private Class classType;
    private Class collectionClass;

    public String toString() {
        if (field!=null) {
            return field.getName()+" class="+classType+" collectionClass="+collectionClass;
        }
        return null;
    }

    public Class getCollectionClass() {
        return collectionClass;
    }

    public void setCollectionClass(Class collectionClass) {
        this.collectionClass = collectionClass;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Method getSetMethod() {
        return setMethod;
    }

    public void setSetMethod(Method setMethod) {
        this.setMethod = setMethod;
    }

    public Method getGetMethod() {
        return getMethod;
    }

    public void setGetMethod(Method getMethod) {
        this.getMethod = getMethod;
    }

    public Class getClassType() {
        return classType;
    }

    public void setClassType(Class classType) {
        this.classType = classType;
    }
}
