package au.com.javacloud.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import au.com.javacloud.annotation.GSonExclude;

public class GsonExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(GSonExclude.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
