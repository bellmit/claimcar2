/******************************************************************************
* CREATETIME : 2016年4月5日 上午8:52:16
******************************************************************************/
package ins.sino.claimcar.carplatform.controller;

import ins.sino.claimcar.carplatform.constant.PlatfromType;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.util.XmlElementParser;
import ins.sino.claimcar.carplatform.vo.AccountRequestHeadVo;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringProperties;

import java.util.Map;


/**
 * 结算平台
 * @author ★YangKun
 * @CreateTime 2016年4月8日
 */
public class CAPlatformController extends PlatformController {

	private static Logger logger = LoggerFactory.getLogger(CAPlatformController.class);

	public CAPlatformController(String comCode,String areaCode,RequestType requestType){
		super(comCode,areaCode,requestType);
	}

	@Override
	protected String getUrl(String comCode,RequestType requestType) {
		PlatfromType platfromType = requestType.getPlatformType();
		String url = "";
		if(platfromType==PlatfromType.CA){
			url = SpringProperties.getProperty("BI_"+comCode.substring(0,2)+"_URL_QS");	
		}else{
			url = SpringProperties.getProperty("BI_"+comCode.substring(0,2)+"_URL_HS");	
		}
		 
		if(url==null||url.trim().length()==0){
			logger.error("机构【"+comCode+"】平台URL不存在");
			throw new IllegalArgumentException("机构【"+comCode+"】平台URL不存在，请联系管理员配置");
		}

		return url;
	}

	@Override
	protected Object getHeadVo(RequestType requestType,String... params) {
		AccountRequestHeadVo head = new AccountRequestHeadVo();
		String comCode = params[0];
		String areaCode = params[1];
		comCode = comCode.substring(0,2);
		
		head.setPassword(SpringProperties.getProperty("BI_"+comCode+"_PWD"));
		head.setUser(SpringProperties.getProperty("BI_"+comCode+"_USER"));
		head.setRequestType(requestType.getCode());
		head.setAreaCode(areaCode);

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
		xmlElmParser.findElementValue(bodyVo,"AccountsNo");
		String accountsNo = xmlElmParser.getString(1);// 结算码注解 AccountsNo
		
		logVo.setBussNo(accountsNo);
		return logVo;
	}

	@Override
	protected Map<String, String> getUrlAndUserAndPassword(String comCode) {
		return null;
	}

	@Override
	protected CiClaimPlatformLogVo initPlatformLogVoForPingAn(RequestType requestType, Map<String, String> reqDataMap) {
		return null;
	}

}
