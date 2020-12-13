/******************************************************************************
 * CREATETIME : 2016年7月15日 上午11:11:59
 ******************************************************************************/
package ins.sino.claimcar.payment.webservice;

import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.payment.vo.JPlanMainVo;
import ins.sino.claimcar.payment.vo.JPlanReturnVo;

import java.util.Date;

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
@org.springframework.stereotype.Service("callPaymentWebService")
public class CallPaymentWebService {

	private static Logger logger = LoggerFactory.getLogger(CallPaymentWebService.class);
	
	@Autowired
	private ClaimInterfaceLogService logService;

	//private static final String SVR_URL = SpringProperties.getProperty("PAYMENT_SVR_URL");
	
	public JPlanReturnVo callPaymentForClient(JPlanMainVo jPlanMainVo,BusinessType businessType,String bussNo) throws Exception {
		String SVR_URL = SpringProperties.getProperty("PAYMENT_SVR_URL");
		if(SVR_URL==null){
			logger.warn("未配置收付地址，不调用收付接口。");
			throw new Exception("未配置收付服务地址。");
		}
		
		SVR_URL = SVR_URL +"/service/claimTransSff";
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
		ClaimTransSff claimTransSff = new ClaimTransSffPortBindingStub(new java.net.URL(SVR_URL), service);
		String requestXml = stream.toXML(jPlanMainVo);
		logger.info("收付接口请求：\n"+requestXml);
		claimInterfaceLogVo.setRequestXml(requestXml);  //发送报文
		claimInterfaceLogVo.setRequestTime(new Date());
		
		String returnXml = claimTransSff.transDataForXml(requestXml);
		logger.info("收付接口返回：\n"+returnXml);
		claimInterfaceLogVo.setResponseXml(returnXml);    // 返回报文
		stream.processAnnotations(JPlanReturnVo.class);
		JPlanReturnVo returnVo = (JPlanReturnVo)stream.fromXML(returnXml);
		
		claimInterfaceLogVo.setErrorMessage(returnVo.getErrorMessage());
		claimInterfaceLogVo.setErrorCode(returnVo.isResponseCode()+"");
		if(returnVo.isResponseCode()){
			claimInterfaceLogVo.setStatus("1");
		}else{
			claimInterfaceLogVo.setStatus("0");
		}
		claimInterfaceLogVo.setCompensateNo(bussNo);
		logService.save(claimInterfaceLogVo);
		if(!returnVo.isResponseCode()){
//			throw new Exception(returnVo.getErrorMessage());
		}
		return returnVo;
	}
	
	private String getOperateNode(BusinessType businessType){
		String node = "";
		if(BusinessType.Payment_prePay.equals(businessType)){
			node = FlowNode.PrePay.name();
		}else if(BusinessType.Payment_compe.equals(businessType)){
			node = FlowNode.Compe.name();
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
}
