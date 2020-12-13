package ins.sino.claimcar.reinsurance.webservice;

import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.reinsurance.vo.ClaimVo;
import ins.sino.claimcar.reinsurance.vo.CompensateVo;
import ins.sino.claimcar.reinsurance.vo.ReinsCaseStatusVO;
import ins.sino.claimcar.reinsurance.vo.ReinsReturnVo;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

@org.springframework.stereotype.Service("callReinsWebService")
public class CallReinsWebService{
	
	@Autowired
	ClaimInterfaceLogService logService;

	private static Logger logger = LoggerFactory.getLogger(CallReinsWebService.class);
	// private static final String SVR_URL = "http://10.0.47.101:7012/payment/service/claimTransSff";
	//private String reins_url = SpringProperties.getProperty("reins_url");
	/**
	 * 
	 * <pre></pre>
	 * @param conpVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆niuqiang(2016年8月11日 下午2:26:31): <br>
	 */
	public ReinsReturnVo callCompensateForClient(CompensateVo compensateVo,ClaimInterfaceLogVo logVo) throws Exception {
		String reins_url = SpringProperties.getProperty("reins_url");
		if(reins_url==null){
			logger.warn("未配置再保地址，不调用再保接口。");
			throw new Exception("未配置再保服务地址。");
		}
		
		//保存日志
		
		logVo.setRequestUrl(reins_url);
		//logVo.setComCode(SecurityUtils.getUserCode());
		
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		
		ReinsTransSffServiceLocator service = new ReinsTransSffServiceLocator();
		ReinsTransSffPortBindingStub reinsTransSff = new ReinsTransSffPortBindingStub(new java.net.URL(reins_url), service);
		
		logVo.setRequestTime(new Date());
		logVo.setRequestXml(stream.toXML(compensateVo));  //发送的日志
		logger.debug("再保CompensateVo接口请求：\n"+stream.toXML(compensateVo));
		String returnXml=reinsTransSff.transDataForcompensateXml(stream.toXML(compensateVo));
		logger.debug("再保CompensateVo接口返回：\n"+returnXml);
         logVo.setResponseXml(returnXml);   // 返回日志
		stream.processAnnotations(ReinsReturnVo.class);
		ReinsReturnVo returnVo = (ReinsReturnVo)stream.fromXML(returnXml);
		
		logVo.setErrorCode(returnVo.isResponseCode()+"");
		logVo.setErrorMessage(returnVo.getErrorMessage());
		if(returnVo.isResponseCode()){
			logVo.setStatus("1");
		}else{
			logVo.setStatus("0");
		}
		
		logService.save(logVo);
		logger.debug("再保CompensateVo接口返回数据输出：\n"+returnVo.getErrorMessage()+returnVo.isResponseCode());
//		if(!returnVo.isResponseCode()){
//			throw new Exception(returnVo.getErrorMessage());
//		}
		return returnVo;
	}
	
	/**
	 * 
	 * <pre></pre>
	 * @param claimVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆niuqiang(2016年8月11日 下午2:26:37): <br>
	 */
	public ReinsReturnVo callClaimForClient(ClaimVo claimVo,ClaimInterfaceLogVo logVo) throws Exception {
		String reins_url = SpringProperties.getProperty("reins_url");
		if(reins_url==null){
			logger.warn("未配置收付地址，不调再保付接口。");
			throw new Exception("未配置再保服务地址。");
		}
		
		// 保存日志
		logVo.setClaimNo(claimVo.getPrpLClaimVo().getClaimNo());
		logVo.setRegistNo(claimVo.getPrpLClaimVo().getRegistNo());
		logVo.setBusinessType(BusinessType.Reinsurance_claim.name());
		logVo.setBusinessName(BusinessType.Reinsurance_claim.getName());
		logVo.setRequestUrl(reins_url);
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		
		ReinsTransSffServiceLocator service = new ReinsTransSffServiceLocator();
		ReinsTransSffPortBindingStub claimTransSff = new ReinsTransSffPortBindingStub(new java.net.URL(reins_url), service);
		logger.debug("再保ClaimVo接口请求：\n"+stream.toXML(claimVo)); 

		logVo.setRequestXml(stream.toXML(claimVo));  //发送报文
		logVo.setRequestTime(new Date());
		String returnXml = "";
		try {
			returnXml = claimTransSff.transDataForClaimXml(stream.toXML(claimVo));
			logger.debug("再保ClaimVo接口返回：\n"+returnXml);
			
			logVo.setResponseXml(returnXml);    // 返回报文
			
			stream.processAnnotations(ReinsReturnVo.class);
			ReinsReturnVo returnVo = (ReinsReturnVo)stream.fromXML(returnXml);
			
			logVo.setErrorMessage(returnVo.getErrorMessage());
			logVo.setErrorCode(returnVo.isResponseCode()+"");
			if(returnVo.isResponseCode()){
				logVo.setStatus("1");
			}else{
				logVo.setStatus("0");
			}
			
			logger.debug("再保ClaimVo接口返回数据输出：\n"+returnVo.getErrorMessage()+returnVo.isResponseCode());
//			if(!returnVo.isResponseCode()){
//				throw new Exception(returnVo.getErrorMessage());
//			}
			
			logService.save(logVo);
			System.out.println("接口发送成功");
			
			return returnVo;
		} catch (Exception e) {
			logger.error("再保接口失败", e);
		}
		return null;
	}
	/**
	 * 
	 * <pre></pre>
	 * @param reinsCaseStatusVO
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆niuqiang(2016年8月12日 下午8:06:31): <br>
	 */
	
	public ReinsReturnVo callReinsCaseStatusForClient(ReinsCaseStatusVO reinsCaseStatusVO,ClaimInterfaceLogVo logVo) throws Exception {
		String reins_url = SpringProperties.getProperty("reins_url");
		if(reins_url==null){
			logger.warn("未配置收付地址，不调用再保接口。");
			throw new Exception("未配置再保服务地址。");
		}
		
		// 保存日志
		logVo.setClaimNo(reinsCaseStatusVO.getClaimNo());
		logVo.setRegistNo(logVo.getRegistNo());
		logVo.setBusinessType(BusinessType.Reinsurance_cancel.name());
		logVo.setBusinessName(BusinessType.Reinsurance_cancel.getName());
		logVo.setRequestUrl(reins_url);
				
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		
		ReinsTransSffServiceLocator service = new ReinsTransSffServiceLocator();
		ReinsTransSffPortBindingStub claimTransSff = new ReinsTransSffPortBindingStub(new java.net.URL(reins_url), service);
		
		logVo.setRequestXml(stream.toXML(reinsCaseStatusVO));
		logVo.setRequestTime(new Date());
		logger.info("再保ReinsCaseStatusVO接口请求：\n" + stream.toXML(reinsCaseStatusVO));
		String returnXml = claimTransSff.transDataForreinsCaseXml(stream.toXML(reinsCaseStatusVO));
		logger.info("再保ReinsCaseStatusVO接口返回：\n" + returnXml);
		
		logVo.setResponseXml(returnXml);
		
		stream.processAnnotations(ReinsReturnVo.class);
		ReinsReturnVo returnVo = (ReinsReturnVo)stream.fromXML(returnXml);
		logVo.setErrorCode(returnVo.isResponseCode()+"");
		logVo.setErrorMessage(returnVo.getErrorMessage());
		if (returnVo.isResponseCode()) {
			logVo.setStatus("1");
		} else {
			logVo.setStatus("0");
		}
		try {
			logService.save(logVo);
		} catch (Exception e) {
			logger.info("立案号：" + (logVo == null ? "" : logVo.getClaimNo()) + "，理赔送再保日志保存失败！", e);
		}
		return returnVo;
	}
}
