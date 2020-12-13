/******************************************************************************
* CREATETIME : 2016年4月5日 上午8:47:26
******************************************************************************/
package ins.sino.claimcar.carplatform.util;

import ins.sino.claimcar.carplatform.constant.PlatfromType;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.controller.BIPlatformController;
import ins.sino.claimcar.carplatform.controller.CAPlatformController;
import ins.sino.claimcar.carplatform.controller.CIPlatformController;
import ins.sino.claimcar.carplatform.controller.PlatformController;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年4月5日
 */
public class PlatformFactory {

	private static Logger logger = LoggerFactory.getLogger(PlatformController.class);

	/** 一个机构+一个接口，对应一个实例 **/
	private static Map<String,PlatformController> instanceMap = new HashMap<String,PlatformController>();

	/**
	 * 一个URL一个实例
	 * @param url
	 * @return
	 * @modified: ☆LiuPing(2016年4月4日 ): <br>
	 */
	public static PlatformController getInstance(String comCode,RequestType requestType) {
		String key=comCode+requestType.hashCode();
		
		PlatformController instance = instanceMap.get(key);
		if(instance==null){
			PlatfromType platfromType = requestType.getPlatformType();
			if(platfromType==PlatfromType.CI){
				instance = new CIPlatformController(comCode,requestType);
			}else if(platfromType==PlatfromType.BI){
				instance = new BIPlatformController(comCode,requestType);
			}
			logger.debug(key+","+instance.getClass().toString());
			instanceMap.put(key,instance);
		}
		return instance;
	}
	
	public static PlatformController getInstance(String comCode,String areaCode,RequestType requestType) {
		String key=comCode+areaCode+requestType.hashCode();
		
		PlatformController instance = instanceMap.get(key);
		if(instance==null){
			PlatfromType platfromType = requestType.getPlatformType();
			if(platfromType==PlatfromType.CA || platfromType==PlatfromType.HS){
				instance = new CAPlatformController(comCode,areaCode,requestType);
			}	
			logger.debug(key+","+instance.getClass().toString());
			instanceMap.put(key,instance);
		}
		return instance;
	}
}
