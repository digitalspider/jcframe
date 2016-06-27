package au.com.javacloud.jcframe.util;

import java.util.Date;

public class DisplayDate extends Date {
	private static final long serialVersionUID = -3149246853663416526L;

	public String toString() {
		return Statics.getServiceLoaderService().getDisplayDateFormat().format(this);
	}

}
