/******************************************************************************
* CREATETIME : 2016年4月5日 上午8:50:41
******************************************************************************/
package ins.sino.claimcar.carplatform.controller;

import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.util.StringUtil;
import ins.sino.claimcar.carplatform.util.XmlElementParser;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.carplatform.vo.CiRequestHeadVo;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringProperties;

import java.util.HashMap;
import java.util.Map;


/**
 * 交强险平台
 * @author ★LiuPing
 * @CreateTime 2016年4月5日
 */
public class CIPlatformController extends PlatformController {
	private static Logger logger = LoggerFactory.getLogger(CIPlatformController.class);

	public CIPlatformController(String comCode,RequestType requestType){
		super(comCode,requestType);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getUrl(String comCode,RequestType requestType) {
		String url = SpringProperties.getProperty("CI_"+comCode.substring(0,2)+"_URL");

		if(url==null||url.trim().length()==0){
			logger.error("机构【"+comCode+"】平台URL不存在");
			throw new IllegalArgumentException("机构【"+comCode+"】平台URL不存在，请联系管理员配置");
		}

		return url;
	}

	@Override
	protected Object getHeadVo(RequestType requestType,String... params) {
		CiRequestHeadVo head = new CiRequestHeadVo();

		String comCode = params[0];
		String comCodeSub = comCode.substring(0,2);
		String user = SpringProperties.getProperty("CI_"+comCodeSub+"_USER");
		String passws = SpringProperties.getProperty("CI_"+comCodeSub+"_PWD");
		if(StringUtils.isBlank(user)||StringUtils.isBlank(passws)){
			throw new IllegalArgumentException("机构【"+comCode+"】未找到配置的用户名和密码，请联系管理员配置");
		}
		head.setPassword(passws);
		head.setUser(user);
		head.setRequestType(requestType.getCode());

		return head;
	}

	/* 
	 * 初始化平台日志vo，从bodyVo获取对应注解的值，赋值到CiClaimPlatformLogVo上
	 * @see ins.sino.claimcar.carplatform.controller.PlatformController#initPlatformLogVo(ins.sino.claimcar.carplatform.constant.RequestType, java.lang.Object)
	 * @param requestType
	 * @param bodyVo
	 * @return
	 */
	@Override
	protected CiClaimPlatformLogVo initPlatformLogVo(RequestType requestType,Object bodyVo) {
		CiClaimPlatformLogVo logVo = new CiClaimPlatformLogVo();
		logVo.setRequestType(requestType.getCode());
		logVo.setRequestName(requestType.getName());

		XmlElementParser xmlElmParser = new XmlElementParser();
		// TODO XuMS 需要检查这个平台中是不是通过如下三个注解获取值的
		xmlElmParser.findElementValue(bodyVo,"REPORT_NO","REGISTRATION_NO","CLAIM_CODE","CONFIRM_SEQUENCE_NO","COMPE_NO");
		String registNo = xmlElmParser.getString(1);// 报案号注解 REPORT_NO
		String claimNo = xmlElmParser.getString(2);// 立案号 注解REGISTRATION_NO
		String claimSeqNo = xmlElmParser.getString(3);// 理赔编号 注解 CLAIM_CODE
		String confirmSequenceNo = xmlElmParser.getString(4);//投保确认码 注解CONFIRM_SEQUENCE_NO
		String compeNo = xmlElmParser.getString(5);
		if(StringUtils.isNotBlank(claimNo)){// 有立案号优先使用立案号
			logVo.setBussNo(claimNo);
		}else{
			logVo.setBussNo(registNo);
		}
		if(requestType.toString().startsWith("Payment")){//赔款支付
			logVo.setBussNo(compeNo);
		}
		logVo.setClaimSeqNo(claimSeqNo);
		logVo.setValidNo(confirmSequenceNo);
		return logVo;
	}


	/**
	 * 【【平安联盟对接上传平台方法】】
	 * @param comCode
	 * @return
	 */
	@Override
	protected Map<String,String> getUrlAndUserAndPassword(String comCode) {
		Map<String,String> map = new HashMap<String, String>();
		String url = SpringProperties.getProperty("CI_"+comCode.substring(0,2)+"_URL");
		String user = SpringProperties.getProperty("CI_"+comCode.substring(0,2)+"_USER");
		String password = SpringProperties.getProperty("CI_"+comCode.substring(0,2)+"_PWD");
		if(url==null||url.trim().length()==0){
			logger.error("机构【"+comCode+"】平台URL不存在");
			throw new IllegalArgumentException("机构【"+comCode+"】平台URL不存在，请联系管理员配置");
		}
		if(StringUtils.isBlank(user)||StringUtils.isBlank(password)){
			throw new IllegalArgumentException("机构【"+comCode+"】未找到配置的用户名和密码，请联系管理员配置");
		}

		map.put("url", url);
		map.put("user", user);
		map.put("password", password);
		return map;
	}

	/*
	 *【【平安联盟对接上传平台方法】】
	 * 初始化平台日志vo，从bodyVo获取对应注解的值，赋值到CiClaimPlatformLogVo上
	 * @see ins.sino.claimcar.carplatform.controller.PlatformController#initPlatformLogVo(ins.sino.claimcar.carplatform.constant.RequestType, java.lang.Object)
	 * @param requestType
	 * @param bodyVo
	 * @return
	 */
	@Override
	protected CiClaimPlatformLogVo initPlatformLogVoForPingAn(RequestType requestType,Map<String,String> reqDataMap) {
		CiClaimPlatformLogVo logVo = new CiClaimPlatformLogVo();
		logVo.setRequestType(requestType.getCode());
		logVo.setRequestName(requestType.getName());

		String registNo = reqDataMap.get("REPORT_NO");// 报案号注解 REPORT_NO
		String claimNo = reqDataMap.get("REGISTRATION_NO");// 立案号 注解REGISTRATION_NO
		String claimSeqNo = reqDataMap.get("CLAIM_CODE");// 理赔编号 注解 CLAIM_CODE
		String confirmSequenceNo = reqDataMap.get("CONFIRM_SEQUENCE_NO");//投保确认码 注解CONFIRM_SEQUENCE_NO
		String compeNo = reqDataMap.get("COMPE_NO");
		if(StringUtils.isNotBlank(claimNo)){// 有立案号优先使用立案号
			logVo.setBussNo(claimNo);
		}else{
			logVo.setBussNo(registNo);
		}
		/*if(requestType.toString().startsWith("Payment")){//赔款支付
			logVo.setBussNo(compeNo);
		}*/
		logVo.setClaimSeqNo(claimSeqNo);
		logVo.setValidNo(confirmSequenceNo);
		return logVo;
	}
}
