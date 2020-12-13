package ins.platform.utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class IDCardUtil {

	public class IDInfo {

		// 出生日期
		private String birthdayStr;

		private String birthday;
		// 性别
		private String gender;
		// 为空则表示身份证号码验证正确，否则则验证失败。
		private String errMsg;

		public void setBirthdayStr(String birthdayStr) {
			this.birthdayStr = birthdayStr;
		}

		public String getBirthdayStr() {
			return birthdayStr;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		public String getGender() {
			return gender;
		}

		public void setErrMsg(String errMsg) {
			this.errMsg = errMsg;
		}

		public String getErrMsg() {
			return errMsg;
		}

		public void setBirthday(String birthday) {
			this.birthday = birthday;
		}

		public String getBirthday() {
			return birthday;
		}
	}

	private IDInfo idInfo = new IDInfo();
	/*********************************** 身份证验证开始 ****************************************/

	/**
	 * 身份证号码验证 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。 2、地址码(前六位数）表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。 6 3、出生日期码（第七位至十四位）
	 * 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。8 4、顺序码（第十五位至十七位） 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号， 顺序码的奇数分配给男性，偶数分配给女性。3 5、校验码（第十八位数） 1 （1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, ... , 16
	 * ，先对前17位数字的权求和 Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2
	 * 15位身份证号码与18位身份证号码的区别 1、15位身份证号码中，"出生年份"字段是2位，转换时需要补入"19"，表示20世纪。 2、15位身份证无最后一位校验码。18位身份证中，校验码根据根据前17位生成。
	 */
	// 校验码
	private static final String[] ValCodeArr = {"1","0","X","9","8","7","6","5","4","3","2"};
	// 加权因子
	private static final String[] Wi = {"7","9","10","5","8","4","2","1","6","3","7","9","10","5","8","4","2"};
	private static final int maxAge = 200;

	/**
	 * 身份证的有效验证
	 * @param IDStr 身份证号
	 * @return IDInfo
	 */
	public IDInfo IDCardValidate(String IDStr) {
		String Ai = "";
		// ================ 号码的长度 15位或18位 ================
		if(IDStr.length()!=15&&IDStr.length()!=18){
			idInfo.errMsg = "身份证号码长度应该为15位或18位!";
			return idInfo;
		}
		// =======================(end)========================

		// ================ 除最后一位以外都为数字 ================
		if(IDStr.length()==18){
			Ai = IDStr.substring(0,17);
		}else if(IDStr.length()==15){
			Ai = IDStr.substring(0,6)+"19"+IDStr.substring(6,15);
		}
		if(isNumeric(Ai)==false){
			idInfo.errMsg = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
			return idInfo;
		}
		// =======================(end)========================

		// ================ 出生年月是否有效 ================
		String strYear = Ai.substring(6,10);// 年份
		String strMonth = Ai.substring(10,12);// 月份
		String strDay = Ai.substring(12,14);// 日期
		Date brirthday = null;
		try{
			brirthday = DateUtils.strToDate(strYear+"-"+strMonth+"-"+strDay);
		}catch(ParseException pe){
			idInfo.errMsg = "身份证生日无效。";
			return idInfo;
		}
		GregorianCalendar gc = new GregorianCalendar();
		if(( gc.get(Calendar.YEAR)-Integer.parseInt(strYear) )>maxAge||( gc.getTime().getTime()-brirthday.getTime() )<0){
			idInfo.errMsg = "身份证生日不在有效范围。";
			return idInfo;
		}
		if(Integer.parseInt(strMonth)>12||Integer.parseInt(strMonth)==0){
			idInfo.errMsg = "身份证月份无效。";
			return idInfo;
		}
		if(Integer.parseInt(strDay)>31||Integer.parseInt(strDay)==0){
			idInfo.errMsg = "身份证日期无效。";
			return idInfo;
		}
		// =====================(end)=====================

		// ================ 地区码时候有效 ================
		Hashtable<String,String> h = GetAreaCode();
		if(h.get(Ai.substring(0,2))==null){
			idInfo.errMsg = "身份证地区编码错误。";
			return idInfo;
		}
		// ==============================================

		// ================ 判断最后一位的值 ================
		int TotalmulAiWi = 0;
		for(int i = 0; i<17; i++ ){
			TotalmulAiWi = TotalmulAiWi+Integer.parseInt(String.valueOf(Ai.charAt(i)))*Integer.parseInt(Wi[i]);
		}
		int modValue = TotalmulAiWi%11;
		String strVerifyCode = ValCodeArr[modValue];
		Ai = Ai+strVerifyCode;

		if(IDStr.length()==18){
			if(Ai.equalsIgnoreCase(IDStr)==false){
				idInfo.errMsg = "身份证无效，不是合法的身份证号码。";
				return idInfo;
			}
		}
		if(Integer.parseInt(Ai.substring(16,17))%2==0){
			idInfo.gender = "F";
		}else{
			idInfo.gender = "M";
		}
		idInfo.birthdayStr = strYear+strMonth+strDay;
		idInfo.birthday = strYear+"-"+strMonth+"-"+strDay;
		// =====================(end)=====================
		return idInfo;
	}

	/**
	 * 功能：设置地区编码
	 * @return Hashtable 对象
	 */
	private Hashtable<String,String> GetAreaCode() {
		Hashtable<String,String> hashtable = new Hashtable<String,String>();
		hashtable.put("11","北京");
		hashtable.put("12","天津");
		hashtable.put("13","河北");
		hashtable.put("14","山西");
		hashtable.put("15","内蒙古");
		hashtable.put("21","辽宁");
		hashtable.put("22","吉林");
		hashtable.put("23","黑龙江");
		hashtable.put("31","上海");
		hashtable.put("32","江苏");
		hashtable.put("33","浙江");
		hashtable.put("34","安徽");
		hashtable.put("35","福建");
		hashtable.put("36","江西");
		hashtable.put("37","山东");
		hashtable.put("41","河南");
		hashtable.put("42","湖北");
		hashtable.put("43","湖南");
		hashtable.put("44","广东");
		hashtable.put("45","广西");
		hashtable.put("46","海南");
		hashtable.put("50","重庆");
		hashtable.put("51","四川");
		hashtable.put("52","贵州");
		hashtable.put("53","云南");
		hashtable.put("54","西藏");
		hashtable.put("61","陕西");
		hashtable.put("62","甘肃");
		hashtable.put("63","青海");
		hashtable.put("64","宁夏");
		hashtable.put("65","新疆");
		hashtable.put("81","香港");
		hashtable.put("82","澳门");
		hashtable.put("83","台湾");
		// hashtable.put("91","国外");
		return hashtable;
	}

	/**
	 * 功能：判断字符串是否为数字
	 * @param str
	 * @return
	 */
	private boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if(isNum.matches()){
			return true;
		}else{
			return false;
		}
	}

	/*********************************** 身份证验证结束 ****************************************/

	public static void main(String[] args) throws ParseException {
		// 44011119350915242X 440104195912180426 440921197212023816 44122719731117512X
		String IDCardNum = "440921197212023816";
		IDCardUtil cc = new IDCardUtil();
		IDInfo iDInfo = cc.IDCardValidate(IDCardNum);
		System.out.println(iDInfo.birthdayStr);
		System.out.println(iDInfo.gender);
		System.out.println(iDInfo.errMsg);
	}
}
