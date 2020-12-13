/******************************************************************************
* CREATETIME : 2016年4月5日 上午8:52:16
******************************************************************************/
package ins.sino.claimcar.carplatform.controller;

import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.util.StringUtil;
import ins.sino.claimcar.carplatform.util.XmlElementParser;
import ins.sino.claimcar.carplatform.vo.BiRequestHeadVo;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringProperties;

import java.util.HashMap;
import java.util.Map;


/**
 * 商业险平台
 * @author ★LiuPing
 * @CreateTime 2016年4月5日
 */
public class BIPlatformController extends PlatformController {

	private static Logger logger = LoggerFactory.getLogger(BIPlatformController.class);

	public BIPlatformController(String comCode,RequestType requestType){
		super(comCode,requestType);
	}

	@Override
	protected String getUrl(String comCode,RequestType requestType) {
		String url = SpringProperties.getProperty("BI_"+comCode.substring(0,2)+"_URL");
		if(url==null||url.trim().length()==0){
			logger.error("机构【"+comCode+"】平台URL不存在");
			throw new IllegalArgumentException("机构【"+comCode+"】平台URL不存在，请联系管理员配置");
		}

		return url;
	}

	@Override
	protected Object getHeadVo(RequestType requestType,String... params) {
		BiRequestHeadVo head = new BiRequestHeadVo();
		String comCode = params[0].substring(0,2);
		head.setPassword(SpringProperties.getProperty("BI_"+comCode+"_PWD"));
		head.setUser(SpringProperties.getProperty("BI_"+comCode+"_USER"));
		head.setRequestType(requestType.getCode());

		return head;
	}

	/* 
	 * 初始化平台日志vo，从bodyVo获取对应注解的值，赋值到CiClaimPlatformLogVo上
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
		xmlElmParser.findElementValue(bodyVo,"ClaimNotificationNo","ClaimRegistrationNo","ClaimSequenceNo","ConfirmSequenceNo","compeNo");
		String registNo = xmlElmParser.getString(1);// 报案号注解 ClaimNotificationNo
		String claimNo = xmlElmParser.getString(2);// 立案号 注解ClaimRegistrationNo
		String claimSeqNo = xmlElmParser.getString(3);// 理赔编号 注解 ClaimSequenceNo
		String confirmSequenceNo = xmlElmParser.getString(4);//投保确认码 注解ConfirmSequenceNo
		String compeNo = xmlElmParser.getString(5);//compeNo
		if(StringUtils.isNotBlank(claimNo)){// 有立案号优先使用立案号
			logVo.setBussNo(claimNo);
		}else{
			logVo.setBussNo(registNo);
		}
		if(RequestType.PaymentBI == requestType || RequestType.PaymentBI_SH == requestType){
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
		String url = SpringProperties.getProperty("BI_"+comCode.substring(0,2)+"_URL");
		String user = SpringProperties.getProperty("BI_"+comCode.substring(0,2)+"_USER");
		String password = SpringProperties.getProperty("BI_"+comCode.substring(0,2)+"_PWD");
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
	 * @param requestType
	 * @param bodyVo
	 * @return
	 */
	@Override
	protected CiClaimPlatformLogVo initPlatformLogVoForPingAn(RequestType requestType,Map<String,String> reqDataMap) {
		CiClaimPlatformLogVo logVo = new CiClaimPlatformLogVo();
		logVo.setRequestType(requestType.getCode());
		logVo.setRequestName(requestType.getName());

		String registNo = reqDataMap.get("ClaimNotificationNo");// 报案号注解 ClaimNotificationNo
		String claimNo = reqDataMap.get("ClaimRegistrationNo");// 立案号 注解ClaimRegistrationNo
		String claimSeqNo = reqDataMap.get("ClaimSequenceNo");// 理赔编号 注解 ClaimSequenceNo
		String confirmSequenceNo = reqDataMap.get("ConfirmSequenceNo");//投保确认码 注解ConfirmSequenceNo
		String compeNo = reqDataMap.get("compeNo");//compeNo
		if(StringUtils.isNotBlank(claimNo)){// 有立案号优先使用立案号
			logVo.setBussNo(claimNo);
		}else{
			logVo.setBussNo(registNo);
		}
		/*if(RequestType.PaymentBI == requestType || RequestType.PaymentBI_SH == requestType){
			logVo.setBussNo(compeNo);
		}*/
		logVo.setClaimSeqNo(claimSeqNo);
		logVo.setValidNo(confirmSequenceNo);
		return logVo;
	}
}
