package au.com.javacloud.jcframe.util;

import java.util.Date;

import au.com.javacloud.jcframe.service.ServiceLoaderService;

public class DisplayDate extends Date {
	private static final long serialVersionUID = -3149246853663416526L;

	public String toString() {
        ServiceLoaderService serviceLoader = Statics.getServiceLoaderService();
        if (serviceLoader!=null) {
        	if (serviceLoader.getDisplayDateFormat()!=null) {
        		return serviceLoader.getDisplayDateFormat().format(this);
        	}
        }
        return super.toString();
	}

}
