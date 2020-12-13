package ins.sino.claimcar.commom.vo;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StringUtils extends org.apache.commons.lang.StringUtils {

	private static final Log logger = LogFactory.getLog(StringUtils.class);

	public static String newString(String value,int length) {
		StringBuffer buffer = new StringBuffer();
		if(value==null){
			return null;
		}
		for(int i = 0; i<length; ++i){
			buffer.append(value);
		}
		return buffer.toString();
	}

	public static String newString(char ch,int length) {
		return newString(String.valueOf(ch),length);
	}

	public static String copyString(String str,int copyTimes) {
		StringBuffer buffer = new StringBuffer();
		if(str==null){
			return null;
		}
		for(int i = 0; i<copyTimes; ++i){
			buffer.append(str);
		}
		return buffer.toString();
	}

	/** @deprecated */
	public static int getBytesLength(String str) {
		return getBytesLength(str,Charset.defaultCharset().name());
	}

	public static int getBytesLength(String str,String charsetName) {
		if(str==null) return -1;
		try{
			return str.getBytes(charsetName).length;
		}catch(UnsupportedEncodingException e){
			throw new IllegalArgumentException(e);
		}
	}

	public static int indexOf(String str,String subStr,int startIndex,int occurrenceTimes) {
		int foundCount = 0;
		int index = startIndex;
		int substrLength = subStr.length();
		if(occurrenceTimes<=0){
			return -1;
		}
		if(str.length()-1<startIndex){
			return -1;
		}
		if("".equals(subStr)){
			return 0;
		}
		while(foundCount<occurrenceTimes){
			index = str.indexOf(subStr,index);
			if(index== -1){
				return -1;
			}
			++foundCount;
			index += substrLength;
		}
		return ( index-substrLength );
	}

	public static int indexOf(String str,String subStr,int occurrenceTimes) {
		return indexOf(str,subStr,0,occurrenceTimes);
	}

	public static int indexOf(String str,String subStr,int fromIndex,boolean caseSensitive) {
		if( !( caseSensitive )){
			return str.toLowerCase(Locale.getDefault()).indexOf(subStr.toLowerCase(Locale.getDefault()),fromIndex);
		}
		return str.indexOf(subStr,fromIndex);
	}

	public static String replace(String str,String searchStr,String replaceStr,boolean caseSensitive) {
		int i = 0;
		int j = 0;
		if(str==null){
			return null;
		}
		if("".equals(str)){
			return "";
		}
		if(( searchStr==null )||( searchStr.equals("") )){
			return str;
		}
		String newReplaceStr = replaceStr;
		if(replaceStr==null){
			newReplaceStr = "";
		}
		StringBuffer buffer = new StringBuffer();
		while(( j = indexOf(str,searchStr,i,caseSensitive) )> -1){
			buffer.append(str.substring(i,j));
			buffer.append(newReplaceStr);
			i = j+searchStr.length();
		}
		buffer.append(str.substring(i,str.length()));
		return buffer.toString();
	}

	public static String replace(String str,String searchStr,String replaceStr) {
		return replace(str,searchStr,replaceStr,true);
	}

	public static String replace(String str,char searchChar,String replaceStr) {
		return replace(str,String.valueOf(searchChar),replaceStr,true);
	}

	public static String replace(String str,int beginIndex,String replaceStr) {
		if(str==null){
			return null;
		}
		String newReplaceStr = replaceStr;
		if(replaceStr==null){
			newReplaceStr = "";
		}
		StringBuffer buffer = new StringBuffer(str.substring(0,beginIndex));
		buffer.append(newReplaceStr);
		buffer.append(str.substring(beginIndex+newReplaceStr.length()));
		return buffer.toString();
	}

	/** @deprecated */
	public static String[] split(String originalString,int splitByteLength) {
		return split(originalString,splitByteLength,Charset.defaultCharset().name());
	}

	public static String[] split(String originalString,int splitByteLength,String charsetName) {
		if(originalString==null){
			return new String[0];
		}
		if("".equals(originalString)){
			return new String[0];
		}
		if("".equals(originalString.trim())){
			return new String[]{""};
		}
		if(splitByteLength<=1){
			return new String[]{originalString};
		}

		String strText = null;
		int intStartIndex = 0;
		int intEndIndex = 0;
		int index = 0;
		int fixCount = 0;
		String[] arrReturn = null;
		List strList = new ArrayList();
		int loopCount = 0;
		try{
			byte[] arrByte = originalString.getBytes(charsetName);

			intEndIndex = 0;
			while(true){
				++loopCount;
				if(loopCount>10000){
					logger.error("Can't split(\""+originalString+"\","+splitByteLength+"). default charset is "+System.getProperty("file.encoding"));

					throw new IllegalStateException("Can't split,loop count is "+loopCount);
				}

				intStartIndex = intEndIndex;
				intEndIndex = intStartIndex+splitByteLength;

				if(intStartIndex>=arrByte.length){
					break;
				}

				if(intEndIndex>arrByte.length){
					intEndIndex = arrByte.length;
					strText = new String(arrByte,intStartIndex,intEndIndex-intStartIndex,charsetName);
					strList.add(strText);
					break;
				}

				fixCount = 0;
				strText = new String(arrByte,intStartIndex,intEndIndex-intStartIndex,charsetName);
				byte[] bytes = strText.getBytes(charsetName);
				if(bytes.length<splitByteLength){
					intEndIndex = intStartIndex+bytes.length;
				}
				for(index = intEndIndex-1; index>=intStartIndex; --index){
					if(arrByte[index]==bytes[( index-intStartIndex )]){
						break;
					}

					++fixCount;
				}

				if(fixCount>0){
					if(fixCount>=intEndIndex){
						fixCount = 0;
						if(logger.isDebugEnabled()){
							logger.debug("split length "+splitByteLength+" is too small.");
						}
					}
					intEndIndex -= fixCount;

					strText = new String(arrByte,intStartIndex,intEndIndex-intStartIndex,charsetName);
				}

				if(intStartIndex==intEndIndex){
					logger.error("Can't split(\""+originalString+"\","+splitByteLength+"). default charset is "+System.getProperty("file.encoding"));

					throw new IllegalStateException(
							"Can't split(\""+originalString+"\","+splitByteLength+"). default charset is "+System.getProperty("file.encoding"));
				}

				strList.add(strText);
			}

			arrReturn = new String[strList.size()];
			strList.toArray(arrReturn);
		}catch(UnsupportedEncodingException e){
			throw new IllegalArgumentException(e);
		}
		return arrReturn;
	}

	public static String[] split(String originalString,String delimiterString) {
		int index = 0;
		String[] returnArray = null;
		int length = 0;

		if(( originalString==null )||( delimiterString==null )||( "".equals(originalString) )){
			return new String[0];
		}

		if(( "".equals(originalString) )||( "".equals(delimiterString) )||( originalString.length()<delimiterString.length() )){
			return new String[]{originalString};
		}

		String strTemp = originalString;
		while(( strTemp!=null )&&( !( strTemp.equals("") ) )){
			index = strTemp.indexOf(delimiterString);
			if(index== -1){
				break;
			}
			++length;
			strTemp = strTemp.substring(index+delimiterString.length());
		}
		returnArray = new String[ ++length];

		strTemp = originalString;
		for(int i = 0; i<length-1; ++i){
			index = strTemp.indexOf(delimiterString);
			returnArray[i] = strTemp.substring(0,index);
			strTemp = strTemp.substring(index+delimiterString.length());
		}
		returnArray[( length-1 )] = strTemp;
		return returnArray;
	}

	public static String rightTrim(String str) {
		if(str==null){
			return "";
		}
		int length = str.length();
		for(int i = length-1; i>=0; --i){
			if(str.charAt(i)!=' '){
				break;
			}
			--length;
		}
		return str.substring(0,length);
	}

	public static String leftTrim(String str) {
		if(str==null){
			return "";
		}
		int start = 0;
		int i = 0;
		for(int n = str.length(); i<n; ++i){
			if(str.charAt(i)!=' '){
				break;
			}
			++start;
		}
		return str.substring(start);
	}

	public static String absoluteTrim(String str) {
		return replace(str," ","");
	}

	public static String lowerCase(String str,int beginIndex,int endIndex) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(str.substring(0,beginIndex));
		buffer.append(str.substring(beginIndex,endIndex).toLowerCase());
		buffer.append(str.substring(endIndex));
		return buffer.toString();
	}

	public static String upperCase(String str,int beginIndex,int endIndex) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(str.substring(0,beginIndex));
		buffer.append(str.substring(beginIndex,endIndex).toUpperCase());
		buffer.append(str.substring(endIndex));
		return buffer.toString();
	}

	public static String lowerCaseFirstChar(String iString) {
		String newString = iString.substring(0,1).toLowerCase()+iString.substring(1);
		return newString;
	}

	public static String upperCaseFirstChar(String iString) {
		String newString = iString.substring(0,1).toUpperCase()+iString.substring(1);
		return newString;
	}

	public static int timesOf(String str,String subStr) {
		int foundCount = 0;
		if("".equals(subStr)){
			return 0;
		}
		int fromIndex = str.indexOf(subStr);
		while(fromIndex!= -1){
			++foundCount;
			fromIndex = str.indexOf(subStr,fromIndex+subStr.length());
		}
		return foundCount;
	}

	public static int timesOf(String str,char ch) {
		int foundCount = 0;
		int fromIndex = str.indexOf(ch);
		while(fromIndex!= -1){
			++foundCount;
			fromIndex = str.indexOf(ch,fromIndex+1);
		}
		return foundCount;
	}

	public static Map<String,String> toMap(String str,String splitString) {
		Map map = Collections.synchronizedMap(new HashMap());
		String[] values = split(str,splitString);
		for(int i = 0; i<values.length; ++i){
			String tempValue = values[i];
			int pos = tempValue.indexOf(61);
			String key = "";
			String value = "";
			if(pos> -1){
				key = tempValue.substring(0,pos);
				value = tempValue.substring(pos+splitString.length());
			}else{
				key = tempValue;
			}
			map.put(key,value);
		}
		return map;
	}

	public static String native2ascii(String str) {
		char[] ca = str.toCharArray();
		StringBuffer buffer = new StringBuffer(ca.length*6);
		for(int x = 0; x<ca.length; ++x){
			char a = ca[x];
			if(a>255)
				buffer.append("\\u").append(Integer.toHexString(a));
			else{
				buffer.append(a);
			}
		}
		return buffer.toString();
	}

//	public static Map<String,String> sortEnglishNumberWord(Map<String,String> map) {
//		Map resultMap = new LinkedHashMap(0);
//		Map tempMap = new LinkedHashMap(0);
//		Set keySet = map.keySet();
//		int s = 2147483647;
//		for(String key:keySet){
//			if(key.indexOf("One")> -1)
//				tempMap.put(Integer.valueOf(1),key);
//			else if(key.indexOf("Two")> -1)
//				tempMap.put(Integer.valueOf(2),key);
//			else if(key.indexOf("Three")> -1)
//				tempMap.put(Integer.valueOf(3),key);
//			else if(key.indexOf("Four")> -1)
//				tempMap.put(Integer.valueOf(4),key);
//			else if(key.indexOf("Five")> -1)
//				tempMap.put(Integer.valueOf(5),key);
//			else if(key.indexOf("Six")> -1)
//				tempMap.put(Integer.valueOf(6),key);
//			else if(key.indexOf("Seven")> -1)
//				tempMap.put(Integer.valueOf(7),key);
//			else if(key.indexOf("Eight")> -1)
//				tempMap.put(Integer.valueOf(8),key);
//			else if(key.indexOf("Nine")> -1)
//				tempMap.put(Integer.valueOf(9),key);
//			else{
//				tempMap.put(Integer.valueOf(s),key);
//			}
//			--s;
//		}
//		Set keyNum = tempMap.keySet();
//		Object[] num_obj = keyNum.toArray();
//		Integer[] nums = new Integer[num_obj.length];
//		Integer tempInt = Integer.valueOf(0);
//		for(int i = 0; i<num_obj.length; ++i){
//			nums[i] = Integer.valueOf(num_obj[i].toString());
//		}
//		for(int i = 0; i<nums.length; ++i){
//			for(int j = 0; j<nums.length-i-1; ++j){
//				if(nums[j].intValue()>nums[( j+1 )].intValue()){
//					tempInt = nums[j];
//					nums[j] = nums[( j+1 )];
//					nums[( j+1 )] = tempInt;
//				}
//			}
//		}
//		for(Integer num:nums){
//			resultMap.put(tempMap.get(num),map.get(tempMap.get(num)));
//		}
//		return resultMap;
//	}

	public static String concat(Object[] sources) {
		if(sources==null){
			return "";
		}
		if(sources.length==1){
			return String.valueOf(sources[0]);
		}
		StringBuilder sb = new StringBuilder();
		for(Object o:sources){
			sb.append(o);
		}
		return sb.toString();
	}
	
}
