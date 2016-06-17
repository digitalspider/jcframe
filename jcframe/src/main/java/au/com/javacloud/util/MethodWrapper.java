package au.com.javacloud.util;

import java.lang.reflect.Method;

public class MethodWrapper implements Comparable<MethodWrapper> {

	private final static MethodComparator comparator = new MethodComparator();
	private Method method;
	
	public MethodWrapper(Method method) {
		this.method = method;
	}
	
	@Override
	public int compareTo(MethodWrapper o) {
		return comparator.compare(this.method, o.method);
	}

	public Method getMethod() {
		return method;
	}

}
