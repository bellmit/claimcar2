package ins.platform.common.util;

import org.apache.commons.lang.StringUtils;

public class StringOperationUtil {
	
	public static String cutOutString(String name,int nameLength){
		if(StringUtils.isNotBlank(name) && name.length() > nameLength){
			name = name.substring(0,nameLength);
		}
		return name;
	}
}
