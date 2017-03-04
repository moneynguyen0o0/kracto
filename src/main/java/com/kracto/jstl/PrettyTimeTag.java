package com.kracto.jstl;

import org.apache.taglibs.standard.tag.common.fmt.SetLocaleSupport;
import org.ocpsoft.prettytime.PrettyTime;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Custom tag to pretty print {@link java.util.Date} objects using
 * <a href="http://ocpsoft.org/prettytime/">prettytime</a>.
 */
public class PrettyTimeTag extends SimpleTagSupport {

	/**
	 * The pretty time object.
	 */
	private PrettyTime prettyTime;

	/**
	 * The date to pretty print.
	 */
	private Date date;

	/**
	 * The locale used to localize the message.
	 */
	private String locale;

	/**
	 * The pattern used to format.
	 */
	private String pattern;

	public PrettyTimeTag() {
		prettyTime = new PrettyTime();
	}

	@Override
	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();
		String time = null;
		if (date.compareTo(new Date(System.currentTimeMillis() - (3 * 1000 * 60 * 60 * 24))) >= 0) {
			if (locale != null) {
				Locale locale = SetLocaleSupport.parseLocale(this.locale);
				prettyTime.setLocale(locale);
			}
			time = prettyTime.format(date);
		} else {
			time = (new SimpleDateFormat(pattern)).format(date);
		}
		out.print(time);
	}

	/*
	 * setters for tag attributes
	 */

	public void setDate(Date date) {
		this.date = date;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

}
