/******************************************************************************
 * CREATETIME : 2016年7月15日 上午11:11:59
 ******************************************************************************/
package ins.sino.claimcar.payment.detail.webservice;

import ins.platform.common.util.ConfigUtil;
import ins.platform.vo.PrpLConfigValueVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.payment.detail.vo.JPlanMainVo;
import ins.sino.claimcar.payment.detail.vo.JPlanReturnVo;
import ins.sino.claimcar.payment.webservice.CallPaymentWebService;

import java.util.Date;

import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 理赔调用收付
 * @author ★XMSH
 */
@org.springframework.stereotype.Service("callPaymentDetailWebService")
public class CallPaymentDetailWebService {

	private static Logger logger = LoggerFactory.getLogger(CallPaymentWebService.class);
	
	@Autowired
	private ClaimInterfaceLogService logService;
	@Autowired
	private RegistQueryService registQueryService;
	//private static final String SVR_URL = SpringProperties.getProperty("PAYMENT_SVR_URL");

	public JPlanReturnVo callPaymentForClient(JPlanMainVo jPlanMainVo,BusinessType businessType,String bussNo,String paymentFlags) throws Exception {
		String SVR_URL = SpringProperties.getProperty("PAYMENT_SVR_URL");
//		System.out.println(SVR_URL);
//		//SVR_URL ="http://10.0.47.101:7022/payment";
//		SVR_URL ="http://10.236.0.205:8080/payment";
		if(SVR_URL==null){
			logger.warn("未配置收付地址，不调用收付接口。");
			throw new Exception("未配置收付服务地址。");
		}
		
		SVR_URL = SVR_URL +"/service/claimTransSffDetail";
		ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); //填写日志表
		claimInterfaceLogVo.setClaimNo(jPlanMainVo.getClaimNo());
		claimInterfaceLogVo.setRegistNo(jPlanMainVo.getRegistNo());
		claimInterfaceLogVo.setOperateNode(getOperateNode(businessType));
		claimInterfaceLogVo.setComCode(jPlanMainVo.getPayComCode());
		claimInterfaceLogVo.setCreateUser(jPlanMainVo.getOperateCode());
		claimInterfaceLogVo.setCreateTime(new Date());
		claimInterfaceLogVo.setBusinessType(businessType.name());
		claimInterfaceLogVo.setBusinessName(businessType.getName());
		claimInterfaceLogVo.setRequestUrl(SVR_URL);
		
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		
		ClaimTransSffService service = new ClaimTransSffServiceLocator();
		
		String requestXml = stream.toXML(jPlanMainVo);
		logger.info("收付接口请求：\n"+requestXml);
		claimInterfaceLogVo.setRequestXml(requestXml);  //发送报文
		claimInterfaceLogVo.setRequestTime(new Date());
		JPlanReturnVo returnVo = new JPlanReturnVo();
		String returnXml = "";
		//平安案件是否送收付
		if(this.checkPingAnCase(jPlanMainVo.getRegistNo())){
			PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.PINGANTOPALTFORMFLAG);
			if(configValueVo != null && configValueVo.getConfigValue() != null && "1".equals(configValueVo.getConfigValue())){
				//不送
				claimInterfaceLogVo.setResponseXml("");    // 返回报文

				claimInterfaceLogVo.setErrorMessage("平安送收付开关未打开");
				claimInterfaceLogVo.setErrorCode(false+"");
				claimInterfaceLogVo.setStatus("0");
				claimInterfaceLogVo.setCompensateNo(bussNo);
				logService.save(claimInterfaceLogVo);
				returnVo.setResponseCode(false);
				return returnVo;
			}
		}
		if("0".equals(paymentFlags)){
		    //不送
            claimInterfaceLogVo.setResponseXml("");    // 返回报文
            
            claimInterfaceLogVo.setErrorMessage("费用，收款人相同，摘要不同不送收付");
            claimInterfaceLogVo.setErrorCode(false+"");
            claimInterfaceLogVo.setStatus("0");
            claimInterfaceLogVo.setCompensateNo(bussNo);
            logService.save(claimInterfaceLogVo);
            returnVo.setResponseCode(false);
            return returnVo;
		} else {
			try{
				ClaimTransSff claimTransSff = new ClaimTransSffPortBindingStub(new java.net.URL(SVR_URL), service);
		        returnXml = claimTransSff.transDataForXml(requestXml);
		        logger.info("收付接口返回：\n"+returnXml);
		        claimInterfaceLogVo.setResponseXml(returnXml);    // 返回报文
		        stream.processAnnotations(JPlanReturnVo.class);
		        returnVo = (JPlanReturnVo)stream.fromXML(returnXml);
		        
		        claimInterfaceLogVo.setErrorMessage(returnVo.getErrorMessage());
		        claimInterfaceLogVo.setErrorCode(returnVo.isResponseCode()+"");
		        if(returnVo.isResponseCode()){
		            claimInterfaceLogVo.setStatus("1");
		        }else{
		            claimInterfaceLogVo.setStatus("0");
		        }
			}catch(Exception e){
				e.printStackTrace();
				claimInterfaceLogVo.setStatus("0");
				claimInterfaceLogVo.setErrorCode("false");
				claimInterfaceLogVo.setErrorMessage(e.getMessage());
			}
	        claimInterfaceLogVo.setCompensateNo(bussNo);
	        logService.save(claimInterfaceLogVo);
	        if(!returnVo.isResponseCode()){
//	          throw new Exception(returnVo.getErrorMessage());
	        }
	        return returnVo;
		}
		
		
	}
	
	private String getOperateNode(BusinessType businessType){
		String node = "";
		if(BusinessType.Payment_prePay.equals(businessType)){
			node = FlowNode.PrePay.name();
		}else if(BusinessType.Payment_compe.equals(businessType)){
			//理算完核赔通过后，才送收付，日志表里操作节点存成 核赔（VClaim）
			node = FlowNode.VClaim.name();
		}else if(BusinessType.Payment_padPay.equals(businessType)){
			node = FlowNode.PadPay.name();
		}else if(BusinessType.Payment_recLoss.equals(businessType)){
			node = FlowNode.RecLoss.name();
		}else if(BusinessType.Payment_recPay.equals(businessType)){
			node = FlowNode.RecPay.name();
		}else{
			node = "";
		}
		return node;
	}

	/**
	 * 判断是否平安案件
	 * @param registNo
	 * @return
	 */
	private boolean checkPingAnCase(String registNo){
		boolean resultFlag = false ;
		PrpLRegistVo registVO = registQueryService.findByRegistNo(registNo);
		if(registVO != null){
			if(registVO.getPaicReportNo() != null && registVO.getPaicReportNo() != ""){
				resultFlag = true;
			}
		}
		return resultFlag;
	}
}
