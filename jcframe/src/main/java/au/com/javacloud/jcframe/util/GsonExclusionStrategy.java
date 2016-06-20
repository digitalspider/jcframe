package au.com.javacloud.jcframe.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import au.com.javacloud.jcframe.annotation.ExcludeGSon;

public class GsonExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(ExcludeGSon.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
