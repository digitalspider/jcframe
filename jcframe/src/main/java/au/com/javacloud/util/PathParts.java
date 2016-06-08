package au.com.javacloud.util;

import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;

public class PathParts extends LinkedList<String> {

	private static final long serialVersionUID = 3137725756932412782L;

	public PathParts() {
		
	}

	public PathParts(String[] dataArray) {
		for (String data : dataArray) {
			add(data);
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
