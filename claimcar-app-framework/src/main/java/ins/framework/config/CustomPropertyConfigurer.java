/******************************************************************************
* CREATETIME : 2014年6月24日 下午3:27:00
******************************************************************************/
package ins.framework.config;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.SpringProperties;

/**
 * 系统配置文件初始化，可以使用本类的getProperty方法读取Spring的配置文件
 * @author ★LiuPing
 */
public class CustomPropertyConfigurer  extends PropertyPlaceholderConfigurer {

	// private static Map<String, String> ctxPropertiesMap;

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
			throws BeansException {
		// SpringProperties.getProperty(arg0)
		super.processProperties(beanFactory, props);
		// load properties to ctxPropertiesMap
		// ctxPropertiesMap = new HashMap<String, String>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			String value = props.getProperty(keyStr);
			SpringProperties.setProperty(keyStr,value);
			// ctxPropertiesMap.put(keyStr, value);
		}
		// 解决 sitemesh2.4在Weblogic10.3下中文乱码，这样控制台不会乱码
		Properties prop = System.getProperties();
		prop.put("file.encoding","utf-8");
	}

}
