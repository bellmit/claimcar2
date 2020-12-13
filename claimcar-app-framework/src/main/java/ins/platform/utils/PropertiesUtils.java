package ins.platform.utils;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtils {
	
	/**
	 * 取得配置文件中的属性值
	 * @param key
	 * @return
	 */
	public static String getProperties(String key) {
		Properties properties = new Properties();
		try {
			properties.load(PropertiesUtils.class.getResourceAsStream("/config/app.properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String value = properties.getProperty(key);
		
		return value;
	}

}
