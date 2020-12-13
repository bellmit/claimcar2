package ins.platform.common.web.taglib;

import ins.platform.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class DateTag extends SimpleTagSupport {

	/**
	 * 日期计算公式:Y+1表示年加1，M-1表示月减1，D-max表示显示这个月的最后一天，多个条件直接用逗号“,”隔开， 例如：cal="Y+1,M+1,D-end"
	 */
	private String cal;//

	/**
	 * 日期格式，默认yyyy-MM-dd
	 */
	private String format;

	/**
	 * 日期时间，默认当前日期
	 */
	private Object date;

	private Date dateTime = null;
 
	@Override
	public void doTag() throws JspException {
		
		PageContext pageContext = (PageContext) this.getJspContext();
		JspWriter out = pageContext.getOut();
		String showDateTime = "";

		if (date != null) {// 界面上面指定了date
			if (date instanceof Date) {
				dateTime = (Date) date;
			} else if (date instanceof String) {
				try {
					dateTime = DateUtils.strToDate((String) date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		} else {//没有指定就用当前日期
			dateTime = new Date();
		}

		if (format == null) {
			format = DateUtils.YToDay;
		}

		try {
			
			if(dateTime==null){
				showDateTime = "";
			} else {

				SimpleDateFormat dateformat = new SimpleDateFormat(format);

		
				if (cal != null && !cal.equals("")) {
					GregorianCalendar gc = new GregorianCalendar();
					gc.setTime(dateTime);
					String calStrs[] = cal.split(",");
					String oneCal;
					String type;
					String value;
					String sign;
					for (int i = 0; i < calStrs.length; i++) {
						oneCal = calStrs[i];
						type = oneCal.substring(0, 1);
						sign = oneCal.substring(1, 2);
						value = oneCal.substring(2);
						if (value.equals("max")) {
							gc.set(GregorianCalendar.DATE, gc.getActualMaximum(GregorianCalendar.DATE));
						} else {
							gc = calDate(gc, type, sign, value);
						}
					}
					showDateTime = dateformat.format( gc.getTime());
				}else{
					showDateTime = dateformat.format(dateTime);
				}
				 

			}
			out.write(showDateTime);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspException(e.getMessage());
		}
	}

	private GregorianCalendar calDate(GregorianCalendar gc, String type, String sign, String value) {
		int valueInt = new Double(value).intValue();
		if (type.equals("Y")) {

			if (sign.equals("+")) {
				gc.add(GregorianCalendar.YEAR, +valueInt);
			} else if (sign.equals("-")) {
				gc.add(GregorianCalendar.YEAR, -valueInt);
			}

		} else if (type.equals("M")) {
			if (sign.equals("+")) {
				gc.add(GregorianCalendar.MONTH, +valueInt);
			} else if (sign.equals("-")) {
				gc.add(GregorianCalendar.MONTH, -valueInt);
			}
		} else if (type.equals("D")) {
			if (sign.equals("+")) {
				gc.add(GregorianCalendar.DATE, +valueInt);
			} else if (sign.equals("-")) {
				gc.add(GregorianCalendar.DATE, -valueInt);
			}
		}
		return gc;
	}

	public String getCal() {
		return cal;
	}

	public void setCal(String cal) {
		this.cal = cal;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setDate(Object date) {
		if (date == null) date = "";
		this.date = date;
	}

}
