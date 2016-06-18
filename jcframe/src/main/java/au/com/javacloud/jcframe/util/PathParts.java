package au.com.javacloud.jcframe.util;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

public class PathParts extends HashMap<Integer,String> {

	private static final long serialVersionUID = 3137725756932412782L;

	public PathParts() {
	}

	public PathParts(String[] dataArray) {
		int i=0;
		for (String data : dataArray) {
			put(i++,data);
		}
	}
	
	public boolean isNumeric(int index) {
		if (size()>index && StringUtils.isNumeric(get(index))) {
			return true;
		}
		return false;
	}
	
	public int getInt(int index) throws Exception {
		if (isNumeric(index)) {
			try {
				return Integer.parseInt(get(index));
			} catch (NumberFormatException e) {
				// ignore
			}
		}
		return 0;
	}
}
