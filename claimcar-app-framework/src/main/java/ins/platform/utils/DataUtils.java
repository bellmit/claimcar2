/******************************************************************************
 * CREATETIME : 2014年6月25日 下午7:41:23
 * FILE       : ins.prpcar.utils.DataUtil
 ******************************************************************************/
package ins.platform.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * String工具类,方法都是返回String类型
 * 
 * @author ★<a href="mailto:huangyi@sinosoft.com.cn">HuangYi</a>
 * @Company www.sinosoft.com.cn
 * @Copyright Copyright (c) 2014-7-3
 * @since (2014-7-3 上午10:29:18): <br>
 */
public class DataUtils {

	/**
	 * 如果为空返回默认值
	 * 
	 * @param value
	 * @param defValue
	 * @return
	 * @modified: ☆LiuPing(2014年7月7日 上午10:05:25): <br>
	 */
	public static String nullToDefault(String value, String defValue) {
		if (StringUtils.isBlank(value)) return defValue;
		return value;
	}

	/**
	 * 如果为空返回默认值
	 * 
	 * @param value
	 * @param defValue
	 * @return
	 * @modified: ☆LiuPing(2014年7月7日 上午10:05:25): <br>
	 */
	public static String nullToDefault(Double value,String defValue) {
		return value==null ? defValue : toString(value);
	}

	/**
	 * 如果为空返回默认值
	 * 
	 * @param value
	 * @param defValue
	 * @return
	 * @modified: ☆LiuPing(2014年7月7日 上午10:05:25): <br>
	 */
	public static String nullToDefault(Float value,String defValue) {
		return value==null ? defValue : toString(value);
	}

	/**
	 * 如果为空返回默认值
	 * 
	 * @param value
	 * @param defValue
	 * @return
	 * @modified: ☆LiuPing(2014年7月7日 上午10:05:25): <br>
	 */
	public static String nullToDefault(Number value,String defValue) {
		return value==null ? defValue : toString(value);
	}

	/**
	 * 如果入参是Null则返回空
	 * 
	 * @param value
	 * @return
	 * @author ☆HuangYi(2014-7-3 上午10:28:55): <br>
	 */
	public static String nullToEmpty(String value) {
		return value==null ? "" : value;
	}

	/**
	 * 如果入参是Null则返回空
	 * 
	 * @param value
	 * @return
	 * @author ☆HuangYi(2014-7-3 上午10:28:55): <br>
	 */
	public static String nullToEmpty(Double value) {
		return nullToEmpty(toString(value));
	}

	/**
	 * 如果入参是Null则返回空
	 * 
	 * @param value
	 * @return
	 * @author ☆HuangYi(2014-7-3 上午10:28:55): <br>
	 */
	public static String nullToEmpty(Number value) {
		return nullToEmpty(toString(value));
	}

	/**
	 * 如果入参是Null则返回空
	 * 
	 * @param value
	 * @return
	 * @author ☆HuangYi(2014-7-3 上午10:28:55): <br>
	 */
	public static String nullToEmpty(Float value) {
		return nullToEmpty(toString(value));
	}

	/**
	 * Double类型转成String 如果入参是Null则返回Null
	 * 
	 * @param value
	 * @return
	 * @author ☆HuangYi(2014-6-27 下午06:44:29): <br>
	 */
	public static String toString(Double value) {
		return value==null ? null : BigDecimal.valueOf(value).toString();
	}

	/**
	 * Number类型转成String 如果入参是Null则返回Null
	 * 
	 * @param value
	 * @return
	 * @author ☆HuangYi(2014-6-27 下午06:44:29): <br>
	 */
	public static String toString(Number value) {
		return value==null ? null : value.toString();
	}

	/**
	 * Float类型转成String 如果入参是Null则返回Null
	 * 
	 * @param value
	 * @return
	 * @author ☆HuangYi(2014-6-27 下午06:44:29): <br>
	 */
	public static String toString(Float value) {
		return value==null ? null : BigDecimal.valueOf(Double.valueOf(value)).toString();
	}

	/**
	 * Integer类型封装成String
	 * 
	 * @param value
	 * @return
	 * @modified: ☆qianxin(2014年7月16日 下午6:33:37): <br>
	 */
	public static String toString(Integer value){
		return value == null? null :value.toString();
	}

	/**
	 * Long类型封装成String
	 * 
	 * @param value
	 * @return
	 * @modified: ☆qianxin(2014年7月16日 下午6:33:37): <br>
	 */
	public static String toString(Long value){
		return value == null? null :value.toString();
	}

	/**
	 * <pre>
	 * 排除数组中为空的元素
	 * </pre>
	 * 
	 * @param values
	 * @return
	 * @modified: ☆LianLG(2014年7月14日 下午6:23:18): <br>
	 */
	public static String[] excludeEmpty(String[] values) {
		List<String> strList = new ArrayList<String>();
		for(String arg:values){
			if( !ObjectUtils.isEmpty(arg)){
				strList.add(arg);
			}
		}
		return (String[])strList.toArray(new String[strList.size()]);
	}

	/**
	 * 检查字符串是否为空
	 * 
	 * @param str
	 * @return
	 * @modified: ☆qianxin(2014年7月29日 上午11:41:56): <br>
	 */
	public static boolean isEmpty(String str){
		boolean flag = false;
		if(str==null){
			return true;
		}else if("null".equals(str)){
			return true;
		}else if("".equals(str)){
			return true;
		}
		return flag;
	}


	/**
	 * 将指定的字符串按给定的长度进行分割，返回分割后的字符串数组.如果最后一个字节是中文的半个字符，则该字节进入数组的下一条。 <br>
	 * <br>
	 * <b>示例 </b> <br>
	 * StringUtils.split(&quot;a123bcd12345&quot;,5) 返回 new String[]{&quot;a123b&quot;,&quot;cd123&quot;,&quot;45&quot;} StringUtils.split(&quot;中国人民保险公司两千年特别条款&quot;,8) 返回 new
	 * String[]{&quot;中国人民&quot;,&quot;保险公司&quot;,&quot;两千年特&quot;,&quot;别条款&quot;} 特别说明：如果遇到截取到半个汉字的情况,工具会采取如下例所示的原则 StringUtils.split(&quot;机动车险A类费率&quot;,7) 返回 new
	 * String[]{&quot;机动车&quot;,&quot;险A类费&quot;,&quot;率&quot;}
	 * 
	 * @param originalString 指定的字符串,字符串的值不能为null
	 * @param splitByteLength 给定字节的长度
	 * @return 返回按照给定长度分割后的字符串数组
	 * @throws UnsupportedEncodingException 
	 */
	public static String[] split(String originalString,int splitByteLength) throws Exception {
		// 定义变量
		String charset = "GBK";//编码
		ArrayList<String> vector = new ArrayList<String>(); // 存放截后的字符串
		String strText = ""; // 临时存放字符串
		byte[] arrByte = null; // 被拆分的字符串生成的Byte数组
		int intStartIndex = 0; // 游标起始位置
		int intEndIndex = 0; // 游标终止位置
		String[] arrReturn = null; // 返回
		// 特殊值处理（长度<1、空、空字符串）
		if(originalString==null){
			return new String[0]; // 空数组
		}
		if(originalString.equals("")){
			return new String[0]; // 空数组
		}
		if(originalString.trim().equals("")){
			return new String[]{""}; // 空字符串
		}
		if(splitByteLength<=1){
			return new String[]{originalString};
		}
		arrByte = originalString.getBytes(charset);
		if(arrByte.length<=splitByteLength){
			return new String[]{originalString};
		}
		byte[] strTextByte=null;

		// 正常处理
		intEndIndex = 0; // 设置最初值
		while(true){
			// 初步设置游标位置
			intStartIndex = intEndIndex;
			intEndIndex = intStartIndex+splitByteLength;
			// 起始位置已经超过数组长度
			if(intStartIndex>=arrByte.length){
				break;
			}
			// 终止位置已经超过数组长度
			if(intEndIndex>arrByte.length){
				intEndIndex = arrByte.length;
				strText = new String(arrByte,intStartIndex,intEndIndex-intStartIndex,charset);
				vector.add(strText);
				break;
			}
			// 检查末尾的半个汉字问题
            strText =  new String(arrByte,intStartIndex,intEndIndex-intStartIndex,charset);
			strText.substring(strText.length()-1);
			strTextByte=strText.substring(strText.length()-1).getBytes(charset);
			//System.out.println("strText111=="+strText+"==="+strTextByte[strTextByte.length-1]);
			if(strTextByte.length<=1){// 半个汉字或者英文字符都退一格
				intEndIndex=intEndIndex-1;
				strText =  new String(arrByte,intStartIndex,intEndIndex-intStartIndex,charset);
			}
			vector.add(strText);
		}// end while
		// 转成字符串数组
		arrReturn = new String[vector.size()];
		vector.toArray(arrReturn);
		// 返回
		return arrReturn;
	}
	
	/**
	 * 特殊字符处理
	 * @param strInValue
	 * @return
	 * @modified:
	 * ☆qianxin(2015年6月6日 下午1:05:23): <br>
	 */
	public static String encode(String strInValue) {
		String strOutValue = "";
		for (int i = 0; i < strInValue.length(); ++i) {
			char c = strInValue.charAt(i);
			switch (c) {
			case ':':
				strOutValue = strOutValue + "：";
				break;
			case '|':
				strOutValue = strOutValue + "┃";
				break;
			case '\n':
				strOutValue = strOutValue + "\\n";
				break;
			case '\r':
				strOutValue = strOutValue + "\\r";
				break;
			case '"':
				strOutValue = strOutValue + "\\\"";
				break;
			case '\'':
				strOutValue = strOutValue + "\\'";
				break;
			case '\b':
				strOutValue = strOutValue + "\\b";
				break;
			case '\t':
				strOutValue = strOutValue + "\\t";
				break;
			case '\f':
				strOutValue = strOutValue + "\\f";
				break;
			case '\\':
				strOutValue = strOutValue + "\\\\";
				break;
			case '<':
				strOutValue = strOutValue + "\\<";
				break;
			case '>':
				strOutValue = strOutValue + "\\>";
				break;
			default:
				strOutValue = strOutValue + c;
			}
		}
		return strOutValue;
	}
	
	/**
	 * 将字符转换成HTML语言
	 * @param strInValue
	 * @return
	 * @modified:
	 * ☆qianxin(2015年7月1日 下午5:37:56): <br>
	 */
	public static String toHTMLFormat(String strInValue) {
		String strOutValue = "";
		for (int i = 0; i < strInValue.length(); ++i) {
			char c = strInValue.charAt(i);
			switch (c) {
			case '<':
				strOutValue = strOutValue + "&lt;";
				break;
			case '>':
				strOutValue = strOutValue + "&gt;";
				break;
			case '\n':
				strOutValue = strOutValue + "<br>";
				break;
			case ' ':
				strOutValue = strOutValue + "&nbsp;";
				break;
			default:
				strOutValue = strOutValue + c;
			case '\r':
			}
		}
		return strOutValue;
	}
	
	/**
	 * 计算字符串里某个字符出现的次数
	 * @param str
	 * @param c
	 * @return
	 * @modified:
	 * ☆qianxin(2015年7月10日 上午10:38:20): <br>
	 */
	public static int countCharactor(String str,char c){
		if(str == null){
			return 0;
		}
		int count = 0;
		for(int i = 0;i < str.length();i++){
			if(c == str.charAt(i)){
				count ++;
			}
		}
		return count;
	}
	
	 public static double round(double value, int scale){
		BigDecimal obj = new BigDecimal(Double.toString(value));
		return obj.divide(BigDecimal.ONE, scale, 4).doubleValue();
	}
	 
	 
	public static BigDecimal NullToZero(BigDecimal strNum) {
		if(strNum==null){
			return new BigDecimal("0");
		}
		return strNum;
	}
	
	public static Integer NullToZeroInt(Integer strNum) {
		return strNum==null ? 0 : strNum;
	}
	
	/**
	 * 替换隐私字符串
	 * @param str
	 * @return
	 * @modified: ☆LiuPing(2016年11月28日 ): <br>
	 */
	public static String replacePrivacy(String str) {
		if(str==null) return null;
		int head = 1;
		int tail = 0;
		int length = str.length();
		if(length<=3){
			head = 1;
			tail = 0;
		}else if(length<=5){
			head = 1;
			tail = 1;
		}else if(length<=7){
			head = 2;
			tail = 2;
		}else if(length<=12){
			head = 3;
			tail = 3;
		}else{
			head = 4;
			tail = 4;
		}
		return replacePrivacy(str,head,tail);
	}

	/**
	 * 替换隐私字符串
	 * @param str
	 * @param head 前面显示的位数
	 * @param tail 后面显示的位数
	 * @return
	 * @modified: ☆LiuPing(2016年11月28日 ): <br>
	 */
	public static String replacePrivacy(String str,int head,int tail) {
		if(str==null) return null;
		StringBuffer buffer = new StringBuffer(str);
		for(int i = head; i<buffer.length()-tail; i++ ){
			buffer.setCharAt(i,'*');
		}
		return buffer.toString();
	}
	
	/**
	 * @param args
	 * @modified: ☆LiuPing(2016年11月28日 ): <br>
	 */
	public static void main(String[] args) {
		StringBuffer buffer = new StringBuffer();
		String str;
		for(int i = 1; i<20; i++ ){
			buffer.append(i%10);
			str = buffer.toString();
			System.out.println(i+"=="+str+"--replacePrivacy=="+DataUtils.replacePrivacy(str));
		}

	}
}
