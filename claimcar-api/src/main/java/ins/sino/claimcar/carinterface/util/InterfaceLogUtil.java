/******************************************************************************
 * CREATETIME : 2016年9月21日 上午10:33:07
 ******************************************************************************/
package ins.sino.claimcar.carinterface.util;

import ins.framework.lang.Springs;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre></pre>
 * @author ★Luwei
 */
public class InterfaceLogUtil {

	private static Logger logger = LoggerFactory.getLogger(InterfaceLogUtil.class);

	public static ClaimInterfaceLogVo saveLog(ClaimInterfaceLogVo logVo) {
		try{
			// 去掉 <PACKET 之前的部分
			String requestXml = subPacket(logVo.getRequestXml());
			logVo.setRequestXml(requestXml);
			String responseXml = subPacket(logVo.getResponseXml());
			logVo.setResponseXml(responseXml);

			ClaimInterfaceLogService logService = Springs.getBean(ClaimInterfaceLogService.class);
			logService.save(logVo);
		}
		catch(Exception e){
			logger.error("保存日志异常",e);
		}
		return logVo;
	}
	
	/**
	 * <pre>根据报案号查询理赔接口日志表</pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆Luwei(2016年9月22日 上午11:37:53): <br>
	 */
	public static List<ClaimInterfaceLogVo> findLogByRegistNo(String registNo) {
		List<ClaimInterfaceLogVo> logVoList = null;
		try{
			ClaimInterfaceLogService logService = Springs.getBean(ClaimInterfaceLogService.class);
			logVoList = logService.findLogByRegistNo(registNo);
		}
		catch(Exception e){
			logger.error("查询ClaimInterfaceLogVo异常！",e);
		}
		return logVoList;
	}

	private static String subPacket(String xmlStr) {
		// 去掉 <PACKET 之前的部分
		int idx = 0;
		if(xmlStr!=null){
			idx = xmlStr.indexOf("<PACKET");
			if(idx>0){
				xmlStr = xmlStr.substring(idx);
			}
//			if(xmlStr.length()>3000){
//				xmlStr = xmlStr.substring(0,3000);
//			}
		}
		return xmlStr;
	}
}
