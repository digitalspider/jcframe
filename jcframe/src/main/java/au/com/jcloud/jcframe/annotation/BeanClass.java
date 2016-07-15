package au.com.jcloud.jcframe.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import au.com.jcloud.jcframe.model.BaseBean;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BeanClass {
	Class<? extends BaseBean> value();
}
