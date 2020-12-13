package ins.sino.claimcar.linkage.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
//	private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

	public static final String PATTERN_ALL = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_DATE = "yyyy-MM-dd";
	public static final String PATTERN_DATE_SHORT = "yyyyMMdd";
	
	/**
	 * 鑾峰彇褰撳ぉ鏃堕棿
	 * 
	 * @param pattern
	 * @return
	 */
	public static Date getToDate(String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		String formatSource = formatter.format(new Date());
		Date date = stringToDate(formatSource, pattern);
		return date;
	}
	/**
	 * 鑾峰彇鏃ユ湡
	 * 
	 * @param time
	 * @return
	 */
	public static Date getToDate(Long time) {
		Date date = new Date();
		date.setTime(time);
		return date; 
	}
	
	/**
	 * 鑾峰彇褰撳ぉ鏃堕棿
	 * 
	 * @param pattern
	 * @return
	 */
	public static String getToDateString(String pattern) {
		return new SimpleDateFormat(pattern).format(new Date());
	}
	
	public static Date parse(Date source) {
		SimpleDateFormat formatter = new SimpleDateFormat(PATTERN_ALL);
		String formatSource = formatter.format(source);
		Date date = stringToDate(formatSource, PATTERN_ALL);
		return date;
	}
	
	public static Date parseByPattern(String pattern, Date source) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		String formatSource = formatter.format(source);
		Date date = stringToDate(formatSource, pattern);
		return date;
	}

	public static Date parseByPattern(String pattern, String source) {
		try {
			return new SimpleDateFormat(pattern).parse(source);
		} catch (ParseException e) {
//			logger.error(e.getMessage() + "[" + pattern + "][" + source + "]", e);
			return null;
		}
	}
	
	public static String toDateString(String pattern, Date date) {
		if (date == null) {
			return "";
		}
		return new SimpleDateFormat(pattern).format(date);
	}
		
	/**
	 * 瀛楃涓茶浆鏃堕棿
	 * @param date
	 * 鎴愬姛杩斿洖date锟�
	 * 杩樻槸杩斿洖null锟�
	 */
	public static Date stringToDate(String strdate,String pattern){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = simpleDateFormat.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static Date dateAddHour(Date date, int addHour) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int nowHour = cal.get(Calendar.HOUR);
		cal.set(Calendar.HOUR, nowHour + addHour);
		return cal.getTime();
	}
	
	public static Date dateAddMinute(Date date, int addMinute) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int nowMinute = cal.get(Calendar.MINUTE);
		cal.set(Calendar.MINUTE, nowMinute + addMinute);
		return cal.getTime();
	}
	
	public static Date dateAddSecond(Date date, int addSecond) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int nowSecond = cal.get(Calendar.SECOND);
		cal.set(Calendar.SECOND, nowSecond + addSecond);
		return cal.getTime();
	}
	
	public static Date dateAddDay(Date date, int addDay) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, addDay);
		return cal.getTime();
	}
	
	public static Date dateAddMonth(Date date, int months) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, months);
		return cal.getTime();
	}
	
	public static Date todayZero(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}
	
	public static Date todayZero(Date day){
		Calendar cal = Calendar.getInstance();
		cal.setTime(day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}
}
