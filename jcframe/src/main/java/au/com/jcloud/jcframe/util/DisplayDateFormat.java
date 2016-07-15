package au.com.jcloud.jcframe.util;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DisplayDateFormat extends SimpleDateFormat {
	private static final long serialVersionUID = -8013603088822570190L;

	public DisplayDateFormat() {
		super();
	}

	public DisplayDateFormat(String format) {
		super(format);
	}

	public DisplayDateFormat(String format, Locale locale) {
		super(format, locale);
	}

	public DisplayDateFormat(String format, DateFormatSymbols formatSymbols) {
		super(format, formatSymbols);
	}

	@Override
	public Date parse(String source) throws ParseException {
		Date baseDate = super.parse(source);
		DisplayDate date = new DisplayDate();
		date.setTime(baseDate.getTime());
		baseDate = null;
		return date;
	}
}
