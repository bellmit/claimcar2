/******************************************************************************
 * CREATETIME : 2016年5月26日 下午3:52:58
 ******************************************************************************/
package ins.sino.claimcar.platform.service.sd;

import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.DataUtils;
import ins.platform.utils.HttpClientHander;
import ins.sino.claimcar.CodeConstants.EWLossFeeType;
import ins.sino.claimcar.CodeConstants.FeeTypeCode;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonFeeVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.endcase.vo.PrpLuwNotionMainVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.platform.service.KindCodeTranService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.trafficplatform.service.EarlyWarnService;
import ins.sino.claimcar.trafficplatform.service.SendVClaimToEWPlatformService;
import ins.sino.claimcar.trafficplatform.vo.EWReqHead;
import ins.sino.claimcar.trafficplatform.vo.EWResponse;
import ins.sino.claimcar.trafficplatform.vo.EWVClaimAdjustmentDataVo;
import ins.sino.claimcar.trafficplatform.vo.EWVClaimBasePartVo;
import ins.sino.claimcar.trafficplatform.vo.EWVClaimClaimCoverDataVo;
import ins.sino.claimcar.trafficplatform.vo.EWVClaimRequest;
import ins.sino.claimcar.trafficplatform.vo.EWVClaimRequestBody;
import ins.sino.claimcar.verifyclaim.service.VerifyClaimService;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * @author ★WLL
 */
@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"} , timeout = 1000*60*10)
@Path("sendVClaimToEWPlatformService")
public class SendVClaimToEWPlatformServiceImpl implements SendVClaimToEWPlatformService {

	private Logger logger = LoggerFactory.getLogger(SendVClaimToEWPlatformServiceImpl.class);

	@Autowired
	private CompensateService compensateService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private VerifyClaimService verifyClaimService;
	@Autowired
	private EarlyWarnService earlyWarnService;
	@Autowired
	ClaimInterfaceLogService claimInterfaceLogService;
	@Autowired
	KindCodeTranService kindCodeTranService;
	@Autowired
	CiClaimPlatformLogService platformLogService;
	
	@Override
	public void SendVClaimToEWPlatform(PrpLCompensateVo compensateVo) {

		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");
		
		String requestXML = "";
		String responseXML = "";
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		try{
			if(compensateVo!=null){
				PrpLCMainVo cMainVo = policyViewService.getPolicyInfo(compensateVo.getRegistNo(),compensateVo.getPolicyNo());
				if(cMainVo.getComCode().startsWith("62")){// 山东机构
					String urlStr = SpringProperties.getProperty("SDWARN_URL");// 获取请求地址
					try{
				requestXML = getRequestXML(compensateVo);
				logger.info("=================理算核赔送山东预警请求报文"+requestXML);
				responseXML = earlyWarnService.requestSDEW(requestXML, urlStr);
				logger.info("=================理算核赔送山东预警返回报文"+responseXML);
				if(responseXML!=null && !"".equals(responseXML)){
					EWResponse responseVo = ClaimBaseCoder.xmlToObj(responseXML, EWResponse.class);
					if("1".equals(responseVo.getHead().getResponseCode())){
						logVo.setStatus("1");
					}else{
						logVo.setStatus("0");
					}
					logVo.setErrorCode(responseVo.getHead().getErrorCode());
					logVo.setErrorMessage(responseVo.getHead().getErrorMessage());
				}else{
					logVo.setStatus("0");
					logVo.setErrorMessage("没有返回信息");
				}

		}catch(Exception e){
			e.printStackTrace();
			logger.info("计算书号："+compensateVo.getCompensateNo()+"==========理算核赔送山东预警报错=========="+responseXML);
			logVo.setStatus("0");
            logVo.setErrorMessage(e.getMessage());
		}finally{
			if(compensateVo !=null){
				logVo.setRequestUrl(urlStr);
				logVo.setRequestXml(requestXML);
				logVo.setResponseXml(responseXML);
				logVo.setRegistNo(compensateVo.getRegistNo());
				logVo.setBusinessType(BusinessType.SDEW_vClaim.toString());
	            logVo.setBusinessName(BusinessType.SDEW_vClaim.getName());
	            logVo.setOperateNode(FlowNode.VClaim.name());
	            logVo.setRequestTime(new Date());
	            logVo.setCreateTime(new Date());
	            logVo.setCreateUser(compensateVo.getCreateUser());
	            logVo.setClaimNo(compensateVo.getClaimNo());
	            logVo.setCompensateNo(compensateVo.getCompensateNo());
	            claimInterfaceLogService.save(logVo);
			}
		}
				}else if(cMainVo.getComCode().startsWith("10")){// 广东平台
					String urlStr = SpringProperties.getProperty("GDWARN_URL");// 获取请求地址
					try{
						requestXML = getRequestXML(compensateVo);
						logger.info("=================理算核赔送广东预警请求报文"+requestXML);
						responseXML = earlyWarnService.requestSDEW(requestXML,urlStr);
						logger.info("=================理算核赔送广东预警返回报文"+responseXML);
						if(responseXML!=null&& !"".equals(responseXML)){
							EWResponse responseVo = ClaimBaseCoder.xmlToObj(responseXML,EWResponse.class);
							if("1".equals(responseVo.getHead().getResponseCode())){
								logVo.setStatus("1");
							}else{
								logVo.setStatus("0");
							}
							logVo.setErrorCode(responseVo.getHead().getErrorCode());
							logVo.setErrorMessage(responseVo.getHead().getErrorMessage());
						}else{
							logVo.setStatus("0");
							logVo.setErrorMessage("没有返回信息");
						}

					}catch(Exception e){
						e.printStackTrace();
						logger.info("计算书号："+compensateVo.getCompensateNo()+"==========理算核赔送广东预警报错=========="+responseXML);
						logVo.setStatus("0");
						logVo.setErrorMessage(e.getMessage());
					}
					finally{
						if(compensateVo!=null){
							logVo.setRequestUrl(urlStr);
							logVo.setRequestXml(requestXML);
							logVo.setResponseXml(responseXML);
							logVo.setRegistNo(compensateVo.getRegistNo());
							logVo.setBusinessType(BusinessType.GDEW_vClaim.toString());
							logVo.setBusinessName(BusinessType.GDEW_vClaim.getName());
							logVo.setOperateNode(FlowNode.VClaim.name());
							logVo.setRequestTime(new Date());
							logVo.setCreateTime(new Date());
							logVo.setCreateUser(compensateVo.getCreateUser());
							logVo.setClaimNo(compensateVo.getClaimNo());
							logVo.setCompensateNo(compensateVo.getCompensateNo());
							claimInterfaceLogService.save(logVo);
						}
					}
				}else if(cMainVo.getComCode().startsWith("50")){// 河南
					String urlStr = SpringProperties.getProperty("HNWARN_URL");// 获取请求地址
					try{
						requestXML = getRequestXML(compensateVo);
						logger.info("=================理算核赔送河南预警请求报文"+requestXML);
						responseXML = earlyWarnService.requestSDEW(requestXML,urlStr);
						logger.info("=================理算核赔送河南预警返回报文"+responseXML);
						if(responseXML!=null&& !"".equals(responseXML)){
							EWResponse responseVo = ClaimBaseCoder.xmlToObj(responseXML,EWResponse.class);
							if("1".equals(responseVo.getHead().getResponseCode())){
								logVo.setStatus("1");
							}else{
								logVo.setStatus("0");
							}
							logVo.setErrorCode(responseVo.getHead().getErrorCode());
							logVo.setErrorMessage(responseVo.getHead().getErrorMessage());
						}else{
							logVo.setStatus("0");
							logVo.setErrorMessage("没有返回信息");
						}

					}catch(Exception e){
						e.printStackTrace();
						logger.info("计算书号："+compensateVo.getCompensateNo()+"==========理算核赔送河南平台报错=========="+responseXML);
						logVo.setStatus("0");
						logVo.setErrorMessage(e.getMessage());
					}
					finally{
						if(compensateVo!=null){
							logVo.setRequestUrl(urlStr);
							logVo.setRequestXml(requestXML);
							logVo.setResponseXml(responseXML);
							logVo.setRegistNo(compensateVo.getRegistNo());
							logVo.setBusinessType(BusinessType.HNEW_vClaim.toString());
							logVo.setBusinessName(BusinessType.HNEW_vClaim.getName());
							logVo.setOperateNode(FlowNode.VClaim.name());
							logVo.setRequestTime(new Date());
							logVo.setCreateTime(new Date());
							logVo.setCreateUser(compensateVo.getCreateUser());
							logVo.setClaimNo(compensateVo.getClaimNo());
							logVo.setCompensateNo(compensateVo.getCompensateNo());
							claimInterfaceLogService.save(logVo);
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("理算核赔流程出错！");
		}
	}
	
	/**
	 * 组织报文
	 * @param cMainVo
	 * @param claimVo
	 * @return
	 */
	public String getRequestXML(PrpLCompensateVo compensateVo){
		String requestXML = "";
		EWVClaimRequest request = new EWVClaimRequest();
		EWReqHead head = new EWReqHead();
		EWVClaimRequestBody body = new EWVClaimRequestBody();
		EWVClaimBasePartVo basePart = new EWVClaimBasePartVo();
		EWVClaimAdjustmentDataVo adjustmentData = new EWVClaimAdjustmentDataVo();
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");
		PrpLCMainVo cMainVo = policyViewService.getPolicyInfo(compensateVo.getRegistNo(),compensateVo.getPolicyNo());
		if(cMainVo.getComCode().startsWith("62")){// 山东
			String user = SpringProperties.getProperty("SDWARN_USER");// 获取数据库配置用户名
			String passWord = SpringProperties.getProperty("SDWARN_PW");

			head.setRequestType(BusinessType.SDEW_vClaim.getCode());
			head.setUser(user);
			head.setPassWord(passWord);
		}else if(cMainVo.getComCode().startsWith("10")){// 广东
			String user = SpringProperties.getProperty("GDWARN_USER");// 获取数据库配置用户名
			String passWord = SpringProperties.getProperty("GDWARN_PW");

			head.setRequestType(BusinessType.GDEW_vClaim.getCode());
			head.setUser(user);
			head.setPassWord(passWord);
		}else if(cMainVo.getComCode().startsWith("50")){// 河南
			String user = SpringProperties.getProperty("HNWARN_USER");// 获取数据库配置用户名
			String passWord = SpringProperties.getProperty("HNWARN_PW");

			head.setRequestType(BusinessType.HNEW_vClaim.getCode());
			head.setUser(user);
			head.setPassWord(passWord);
		}
		
		if(!StringUtils.isBlank(cMainVo.getClaimSequenceNo())){
			basePart.setClaimSequenceNo(cMainVo.getClaimSequenceNo());//理赔编码
		}else{
			String requestCode = "";
			if(compensateVo.getComCode().startsWith("22")){
				requestCode = Risk.DQZ.equals(compensateVo.getRiskCode()) ? 
						RequestType.RegistInfoCI_SH.getCode() : RequestType.RegistInfoBI_SH.getCode();
			}else{
				requestCode = Risk.DQZ.equals(compensateVo.getRiskCode()) ? 
						RequestType.RegistInfoCI.getCode() : RequestType.RegistInfoBI.getCode();
			}
			CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo(requestCode,compensateVo.getRegistNo(),compensateVo.getComCode());
			basePart.setClaimSequenceNo(logVo.getClaimSeqNo());
		}
		basePart.setClaimNotificationNo(compensateVo.getRegistNo());//报案号
		basePart.setConfirmSequenceNo(cMainVo.getValidNo());//投保确认码
		
		String taskType = "1101".equals(compensateVo.getRiskCode()) ? "1" : "2";
		PrpLuwNotionMainVo uwNotionMainVo 
				= verifyClaimService.findUwNotion(compensateVo.getRegistNo(),compensateVo.getCompensateNo(),taskType);
		adjustmentData.setAdjustmentCode(compensateVo.getCompensateNo());//理算编码-计算书号
		adjustmentData.setUnderWriteDesc(uwNotionMainVo.getRemark());//核赔意见
		adjustmentData.setClaimAmount(DataUtils.NullToZero(compensateVo.getSumAmt()));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm"); //核赔时间精确到分
		String underWriteEndTime = null;
		if(compensateVo.getUnderwriteDate()!=null){
			underWriteEndTime = sdf.format(compensateVo.getUnderwriteDate()).trim();
		}else{
			underWriteEndTime = sdf.format(new Date()).trim();
		}
		underWriteEndTime = underWriteEndTime.replaceAll("-","");
		underWriteEndTime = underWriteEndTime.replaceAll(" ","");
		underWriteEndTime = underWriteEndTime.replaceAll(":","");
		adjustmentData.setUnderWriteEndTime(underWriteEndTime);
		List<EWVClaimClaimCoverDataVo> claimCoverDataVoList = new ArrayList<EWVClaimClaimCoverDataVo>();
		
		/**无损失0结案及冲销0结案案件由于claimCoverData必传，做特殊处理，手工生成标的车0损失记录或交强0损失记录**/
		if(BigDecimal.ZERO.compareTo(compensateVo.getSumPaidAmt()!=null?compensateVo.getSumPaidAmt():BigDecimal.ZERO)==0 
				&& (compensateVo.getPrpLLossItems()==null||compensateVo.getPrpLLossItems().size()==0)
				&& (compensateVo.getPrpLLossProps()==null||compensateVo.getPrpLLossProps().size()==0)
				&& (compensateVo.getPrpLLossPersons()==null||compensateVo.getPrpLLossPersons().size()==0)){
			EWVClaimClaimCoverDataVo claimCoverData = new EWVClaimClaimCoverDataVo();
			if(Risk.DQZ.equals(compensateVo.getRiskCode()) ){
				claimCoverData.setCoverageCode("100");//交强险出险时，“赔偿险种代码”必须传送“100-机动车强制责任保险”
			}else{
				//山东预警系统编码与全国车险平台1201险种编码一致
				String kindCode = "A";
				if(cMainVo.getPrpCItemKinds()!=null && cMainVo.getPrpCItemKinds().size() > 0){
					kindCode = cMainVo.getPrpCItemKinds().get(0).getKindCode();
				}
				claimCoverData.setCoverageCode(kindCodeTranService.findTransCiCode("00", Risk.DAA, "CovergeCode", kindCode));
			}
			claimCoverData.setLossFeeType(EWLossFeeType.CARLOSS);
			claimCoverData.setLiabilityRate(compensateVo.getIndemnityDutyRate().divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));//TODO 确认 数值（3,2）是什么格式
			claimCoverData.setClaimAmount(BigDecimal.ZERO);
			claimCoverData.setSalvageFee(BigDecimal.ZERO);
			claimCoverData.setLossAmount(BigDecimal.ZERO);
			if("0201200".equals(claimCoverData.getCoverageCode())){
				claimCoverData.setIsDeviceItem("0");
			}
			claimCoverDataVoList.add(claimCoverData);
			
		}
		
		/** 车辆损失 **/
		if(compensateVo.getPrpLLossItems() != null && compensateVo.getPrpLLossItems().size() > 0){
			for(PrpLLossItemVo itemVo : compensateVo.getPrpLLossItems()){
				EWVClaimClaimCoverDataVo claimCoverData = new EWVClaimClaimCoverDataVo();
				if("1101".equals(compensateVo.getRiskCode()) ){
					claimCoverData.setCoverageCode("100");//交强险出险时，“赔偿险种代码”必须传送“100-机动车强制责任保险”
				}else{
					//山东预警系统编码与全国车险平台1201险种编码一致
					claimCoverData.setCoverageCode(kindCodeTranService.findTransCiCode("00", "1201", "CovergeCode", itemVo.getKindCode()));
				}
				claimCoverData.setLossFeeType(EWLossFeeType.CARLOSS);
				claimCoverData.setLiabilityRate(compensateVo.getIndemnityDutyRate().divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));//TODO 确认 数值（3,2）是什么格式
				claimCoverData.setClaimAmount(DataUtils.NullToZero(itemVo.getSumRealPay()));
				claimCoverData.setSalvageFee(DataUtils.NullToZero(itemVo.getRescueFee()));
				claimCoverData.setLossAmount(DataUtils.NullToZero(itemVo.getSumLoss()));
				if("A1".equals(itemVo.getKindCode())){
	            	String sign="0";//sign附加险是否有新增设备险，0无，1有
	            	if(compensateVo.getPrpLLossProps()!=null && compensateVo.getPrpLLossProps().size()>0){
	            		for(PrpLLossPropVo propVo : compensateVo.getPrpLLossProps()){
	            			if("9".equals(propVo.getPropType())){
	            				if("X3".equals(propVo.getKindCode())){
	            					sign="1";
	            					break;
	            				}
	            			}
	            		}
	            	}
	            	if("0".equals(sign)){
	            		claimCoverData.setIsDeviceItem("0");
	            	}else{
	            		claimCoverData.setIsDeviceItem("1");
	            	}
	            }
				
				claimCoverDataVoList.add(claimCoverData);
			}
		}
		/** 财产损失 **/
		if(compensateVo.getPrpLLossProps() != null && compensateVo.getPrpLLossProps().size() > 0){
			for(PrpLLossPropVo itemVo : compensateVo.getPrpLLossProps()){
				EWVClaimClaimCoverDataVo claimCoverData = new EWVClaimClaimCoverDataVo();
				if("1101".equals(compensateVo.getRiskCode()) ){
					claimCoverData.setCoverageCode("100");//交强险出险时，“赔偿险种代码”必须传送“100-机动车强制责任保险”
				}else{
					//山东预警系统编码与全国车险平台1201险种编码一致
					claimCoverData.setCoverageCode(kindCodeTranService.findTransCiCode("00", "1201", "CovergeCode", itemVo.getKindCode()));
				}
				claimCoverData.setLossFeeType(EWLossFeeType.PROPLOSS);
				claimCoverData.setLiabilityRate(compensateVo.getIndemnityDutyRate().divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));//TODO 确认 数值（3,2）是什么格式
				claimCoverData.setClaimAmount(DataUtils.NullToZero(itemVo.getSumRealPay()));
				claimCoverData.setSalvageFee(DataUtils.NullToZero(itemVo.getRescueFee()));
				claimCoverData.setLossAmount(DataUtils.NullToZero(itemVo.getSumLoss()));
				if("X3".equals(itemVo.getKindCode())){
					claimCoverData.setIsDeviceItem("1");
		        }
				
				claimCoverDataVoList.add(claimCoverData);
			}
		}	
		
		/** 人伤损失 **/
		if(compensateVo.getPrpLLossPersons() != null && compensateVo.getPrpLLossPersons().size() > 0){
			for(PrpLLossPersonVo personVo : compensateVo.getPrpLLossPersons()){
				for(PrpLLossPersonFeeVo feeVo : personVo.getPrpLLossPersonFees()){
					EWVClaimClaimCoverDataVo claimCoverData = new EWVClaimClaimCoverDataVo();
					if("1101".equals(compensateVo.getRiskCode()) ){
						claimCoverData.setCoverageCode("100");//交强险出险时，“赔偿险种代码”必须传送“100-机动车强制责任保险”
					}else{
						//山东预警系统编码与全国车险平台1201险种编码一致
						claimCoverData.setCoverageCode(kindCodeTranService.findTransCiCode("00", compensateVo.getRiskCode(), "CovergeCode", personVo.getKindCode()));
					}
					claimCoverData.setLossFeeType(FeeTypeCode.PERSONLOSS.equals(feeVo.getLossItemNo()) ? EWLossFeeType.DEATHLOSS : EWLossFeeType.MEDILOSS);
					claimCoverData.setLiabilityRate(compensateVo.getIndemnityDutyRate().divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));//TODO 确认 数值（3,2）是什么格式
					claimCoverData.setClaimAmount(DataUtils.NullToZero(feeVo.getFeeRealPay()));
					claimCoverData.setSalvageFee(BigDecimal.ZERO);
					claimCoverData.setLossAmount(DataUtils.NullToZero(feeVo.getFeeLoss()));
					
					claimCoverDataVoList.add(claimCoverData);
				}
			}
		}
		
		adjustmentData.setClaimCoverData(claimCoverDataVoList);
		
		body.setBasePart(basePart);
		body.setAdjustmentData(adjustmentData);
		
		request.setHead(head);
		request.setBody(body);
		requestXML = "<?xml version=\"1.0\" encoding=\"GBK\"?>"+stream.toXML(request);
		return requestXML;
	}
	


}
