/******************************************************************************
* CREATETIME : 2016年6月24日 上午11:21:20
******************************************************************************/
package ins.sino.claimcar.carplatform.util;

import ins.framework.lang.Springs;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformTaskService;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformTaskVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年6月24日
 */

public class PlatformLogUtil {

	private static Logger logger = LoggerFactory.getLogger(PlatformLogUtil.class);

	public static void saveLog(CiClaimPlatformLogVo logVo) {
		try{
			// 去掉 <PACKET 之前的部分
			String requestXml = subPacket(logVo.getRequestXml());
			logVo.setRequestXml(requestXml);
			String responseXml = subPacket(logVo.getResponseXml());
			logVo.setResponseXml(responseXml);

		}catch(Exception e){
			logger.error("保存平台交互日志异常",e);
		}finally{
			CiClaimPlatformLogService platformLogService = Springs.getBean(CiClaimPlatformLogService.class);
			platformLogService.save(logVo);
		}
	}
	
	private static String subPacket(String xmlStr) {
		// 去掉 <PACKET 之前的部分

		int idx = 0;
		if(xmlStr!=null){
			idx = xmlStr.indexOf("<PACKET");
			if(idx>0){
				xmlStr = xmlStr.substring(idx);
			}
			if(xmlStr.length()>3000){
//				xmlStr = xmlStr.substring(0,3000);
			}
		}
		return xmlStr;
	}
	
	public static void updatePlatformTask(CiClaimPlatformTaskVo platformTaskVo){
		CiClaimPlatformTaskService platformTaskService = Springs.getBean(CiClaimPlatformTaskService.class);
		platformTaskService.updatePlatformTask(platformTaskVo);
	}

}
