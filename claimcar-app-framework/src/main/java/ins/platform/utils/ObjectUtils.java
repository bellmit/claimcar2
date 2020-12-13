/******************************************************************************
* CREATETIME : 2014-6-27 上午9:09:31
* FILE       : ins.platform.utils.ObjectUtil
******************************************************************************/
package ins.platform.utils;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.support.Page;

import java.util.Collection;
import java.util.Map;




/**
 * <pre></pre>
 * @author ★LiaoJingdong
 */
public class ObjectUtils {

	/**
	 * 判断对象是否为空
	 * 
	 * @return
	 * @modified: ☆LianLG(2013-7-17 下午05:17:30): <br>
	 */
	public static boolean isEmpty(Object o) {
		boolean flag = false;
		if(o==null){
			return true;
		}else if("null".equals(o)){
			return true;
		}else if("".equals(o)){
			return true;
		}
		return flag;
	}
	
	/**
	 * 判断集合是否为空
	 * @param list
	 * @return
	 * @modified:
	 * ☆qianxin(2014年7月7日 上午11:24:35): <br>
	 */
	public static boolean isEmpty(Collection<?> list){
		boolean flag = false;
		if(list == null || list.isEmpty()){
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 判断map是否为空
	 * @param map
	 * @return
	 * @modified:
	 * ☆qianxin(2014年7月18日 下午2:43:28): <br>
	 */
	public static boolean isEmpty(Map<?,?> map){
		boolean flag = false;
		if(map == null || map.isEmpty()){
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 判断数组是否为空
	 * @param objs
	 * @return
	 * @modified:
	 * ☆qianxin(2014年7月18日 下午2:44:32): <br>
	 */
	public static boolean isEmpty(Object[] objs){
		boolean flag = false;
		if(objs == null || objs.length == 0){
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 将 Page<T> 转换为ResultPage<T>
	 * @param page
	 * @return
	 * @modified: ☆LiuPing(2016年1月7日 上午9:57:56): <br>
	 */
	public static <T> ResultPage<T> toResultPage(Page<T> page) {
		ResultPage<T> resultPage = new ResultPage<T>(page.getStart(),page.getPageSize(),page.getTotalCount(),page.getResult());
		return resultPage;
	}
}
