/******************************************************************************
* CREATETIME : 2019年1月29日 下午5:06:37
******************************************************************************/
package ins.platform.utils;

import org.springframework.core.SpringProperties;


/**
 * <pre></pre>
 * @author ★XHY
 */
public class ReadConfigUtils {
	/**
	 * 获取查询结果最大条数
	 * <pre></pre>
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年1月29日 下午5:35:21): <br>
	 */
	public static int getResultCount(String propertyName){
		String realCount = SpringProperties.getProperty(propertyName);
		if(realCount != null){
			return Integer.valueOf(realCount);
		} 
		return 100;//如果为空，直接返回条数为100
	}

}
