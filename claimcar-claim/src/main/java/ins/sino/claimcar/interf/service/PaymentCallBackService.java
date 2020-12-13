/******************************************************************************
 * CREATETIME : 2016年8月9日 下午4:41:09
 ******************************************************************************/
package ins.sino.claimcar.interf.service;


import ins.framework.lang.Springs;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.sino.claimcar.carplatform.util.StringUtil;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.claim.vo.PrpsmsMessageVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.interf.vo.BasePartVo;
import ins.sino.claimcar.interf.vo.PayDataVo;
import ins.sino.claimcar.interf.vo.PayReturnVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.other.service.AcheckService;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.service.MsgModelService;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLCheckFeeVo;
import ins.sino.claimcar.pay.service.PadPayService;
import ins.sino.claimcar.pinganUnion.service.PingAnPayCallBackService;
import ins.sino.claimcar.pinganunion.vo.payCallback.UnionPayCallbackRequestListDto;
import ins.sino.claimcar.pinganunion.vo.payCallback.UnionPayCallbackRequestParamDto;
import ins.sino.claimcar.platform.service.SendPaymentToPlatformService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.alibaba.dubbo.common.utils.Assert;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * @author ★XMSH
 */

@WebService(serviceName="paymentCallBack")
public class PaymentCallBackService extends SpringBeanAutowiringSupport {
//	@Autowired
	private CompensateTaskService compensateTaskService;
//	@Autowired
	private SendPaymentToPlatformService sendPaymentToPlatformService;
//	@Autowired
	private CompensateService compensateService;
//	@Autowired
	private PadPayService padPayService;
//	@Autowired
	private AssessorService assessorService;
	private ManagerService managerService;
	private CodeTranService codeTranService;
	
	private AcheckService acheckService;
	
//	@Autowired
	MsgModelService msgModelService;
	@Autowired
	private PingAnPayCallBackService pingAnPayCallBackService;
	
	private static Logger logger = LoggerFactory.getLogger(PaymentCallBackService.class);
	
	public String paymentWriteBackForXml(String xml) {
		this.init();
		logger.info("收付回写报文： "+xml);
		PayReturnVo payReturnVo = new PayReturnVo();
		payReturnVo.setErrorMessage("成功");
		payReturnVo.setResponseCode(true);
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		
		String registNo = "";
		try {
			//将结案短信的状态改为：1(推送成功)
			stream.processAnnotations(BasePartVo.class);
			BasePartVo basePartVo1 = (BasePartVo)stream.fromXML(xml);
			registNo = basePartVo1.getRegistNo();
			List<PrpsmsMessageVo> prpsmsMessageList = msgModelService.findPrpSmsMessageByBusinessNo(registNo);
			if(prpsmsMessageList != null && prpsmsMessageList.size() > 0){
				for(PrpsmsMessageVo vo : prpsmsMessageList){
					if(vo.getSendNodecode() != null && FlowNode.EndCas.name().equals(vo.getSendNodecode())){
						vo.setStatus("1");
						msgModelService.saveorUpdatePrpSmsMessage(vo);
					}
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
			logger.info("报案号:"+registNo+"和收付回写理赔系统更新结案短信状态：",e );
		}
		
		try{
			stream.processAnnotations(BasePartVo.class);
			BasePartVo basePartVo = (BasePartVo)stream.fromXML(xml);
			this.paymentWriteBack(basePartVo);
		}
		catch(Exception e){
			payReturnVo.setResponseCode(false);
			payReturnVo.setErrorMessage(e.getMessage());
			logger.info("收付回写报文异常!回写时间：" + new Date() + " 报文内容：\n" + xml, e);
		}
		stream.processAnnotations(PayReturnVo.class);
		return stream.toXML(payReturnVo);
	}

	private String adjustContent(String content){
		String reContent = null;
		if(content!=null&&!"".equals(content)&&!content.isEmpty()){
			reContent = content;
		}
		return reContent;
	}
	
	private void paymentWriteBack(BasePartVo basePartVo) throws Exception{
		Assert.notNull(adjustContent(basePartVo.getCertiType()),"基本信息业务类型为空");
		Assert.notNull(adjustContent(basePartVo.getCertiNo()),"基本信息业务号为空");
		Assert.notNull(adjustContent(basePartVo.getPolicyNo()),"基本信息保单号为空");
		Assert.notNull(adjustContent(basePartVo.getClaimNo()),"基本信息立案号为空");
		int no = 0;
		boolean isSendPlatform = false;
		if(basePartVo.getPayDataVo()!=null||basePartVo.getPayDataVo().size()>0){
			//prplpaycustom
			Map<String,Long> payIdMap = new HashMap<String, Long>();
			//平安支付结果回调列表
			List<UnionPayCallbackRequestListDto> callbackRequestListDtos = new ArrayList<UnionPayCallbackRequestListDto>();
			//平安支付结果回调Map
			Map<String, Object> callBackDataMap = new HashMap<String, Object>();
			List<PrpLPayCustomVo> customList = managerService.findPayCustomVoByRegistNo(basePartVo.getRegistNo());
			if(customList!=null && !customList.isEmpty()){
				for(PrpLPayCustomVo customVo : customList){
					payIdMap.put(customVo.getAccountId(), customVo.getId());
				}
			}
			
			String payFlag = null;//1 表示垫付 2 公估费  ，3 查勘费 审核
			for(PayDataVo payDataVo : basePartVo.getPayDataVo()){
				no++;
				Assert.notNull(adjustContent(payDataVo.getPayeeId()),"第"+no+"条收付明细信息收款人ID为空");
				Assert.notNull(adjustContent(payDataVo.getAccountNo()),"第"+no+"条收付明细信息收款人账户为空");
				Assert.notNull(adjustContent(payDataVo.getPayRefReason()),"第"+no+"条收付明细信息收付原因为空");
				Assert.notNull(adjustContent(payDataVo.getSerialNo()),"第"+no+"条收付明细信息序列号为空");
				Assert.notNull(payDataVo.getPayTime(),"第"+no+"条收付明细信息支付时间为空");
				if("P6K".equals(adjustContent(payDataVo.getPayRefReason()))){//垫付
					payFlag ="1";
				}else if("P67".equals(payDataVo.getPayRefReason())){//付公估费
					payFlag ="2";
				}else if("P62".equals(payDataVo.getPayRefReason())){//付查勘费
					payFlag ="3";
				}
			}


			if("Y".equals(basePartVo.getCertiType())){// 预付
				List<PrpLPrePayVo> prePayPVos = compensateService.getPrePayVo(basePartVo.getCertiNo(),"P");
				List<PrpLPrePayVo> prePayFVos = compensateService.getPrePayVo(basePartVo.getCertiNo(),"F");
				List<PrpLPrePayVo> writeBackList = new ArrayList<PrpLPrePayVo>();
				Map<String,SysCodeDictVo> dictTransMap = codeTranService.findCodeDictTransMap("ChargeCode","");
				if(basePartVo.getPayDataVo()!=null&& !basePartVo.getPayDataVo().isEmpty()){
					for(PayDataVo payDataVo:basePartVo.getPayDataVo()){// Property1是预付，Property2是理算
						if(prePayPVos!=null&& !prePayPVos.isEmpty()){
							for(PrpLPrePayVo prePayPVo:prePayPVos){// 赔款
								if(payDataVo.getSerialNo().equals(prePayPVo.getSerialNo())&&payDataVo.getPayRefReason().equals("P50")){
									prePayPVo.setPayStatus("1");
									prePayPVo.setPayTime(payDataVo.getPayTime());
									writeBackList.add(prePayPVo);
									// 支付轨迹
									try {
										logger.info("收付回写理赔预付赔款支付轨迹开始！计算书号：" + prePayPVo.getCompensateNo() + " 系统时间" + new Date());
										this.savePrplPayHis(basePartVo.getClaimNo(),prePayPVo.getCompensateNo(),prePayPVo.getId(),"1","Y",payDataVo.getPayTime());
										logger.info("收付回写理赔预付赔款支付轨迹结束！计算书号：" + prePayPVo.getCompensateNo() + " 系统时间" + new Date());
									} catch (Exception e) {
										logger.info("收付回写理赔预付赔款支付轨迹异常！计算书号：" + prePayPVo.getCompensateNo() + " 系统时间" + new Date(), e);
										throw e;
									}
									//获取平安支付主键
									String idClmPaymentResult = prePayPVo.getIdClmPaymentResult();
									if (StringUtils.isNotBlank(idClmPaymentResult)) {
										addPingAnCallbackData(callbackRequestListDtos, payDataVo, idClmPaymentResult);
									}
								}
							}
						}
						if(prePayFVos!=null&& !prePayFVos.isEmpty()){
							for(PrpLPrePayVo prePayFVo:prePayFVos){// 费用
								SysCodeDictVo sysCodeDictVo = dictTransMap.get(prePayFVo.getChargeCode());
								if(payDataVo.getSerialNo().equals(prePayFVo.getSerialNo())&&payDataVo.getPayRefReason().equals(
										sysCodeDictVo.getProperty1())){
									prePayFVo.setPayStatus("1");
									prePayFVo.setPayTime(payDataVo.getPayTime());
									writeBackList.add(prePayFVo);
									// 支付轨迹
									try {
										logger.info("收付回写理赔预付费用支付轨迹开始！计算书号：" + prePayFVo.getCompensateNo() + " 系统时间" + new Date());
										this.savePrplPayHis(basePartVo.getClaimNo(),prePayFVo.getCompensateNo(),prePayFVo.getId(),"1","Y",payDataVo.getPayTime());
										logger.info("收付回写理赔预付费用支付轨迹结束！计算书号：" + prePayFVo.getCompensateNo() + " 系统时间" + new Date());
									} catch (Exception e) {
										logger.info("收付回写理赔预付费用支付轨迹异常！计算书号：" + prePayFVo.getCompensateNo() + " 系统时间" + new Date(), e);
										throw e;
									}
									//获取平安支付主键
									String idClmPaymentResult = prePayFVo.getIdClmPaymentResult();
									if (StringUtils.isNotBlank(idClmPaymentResult)) {
										addPingAnCallbackData(callbackRequestListDtos, payDataVo, idClmPaymentResult);
									}
								}
							}
						}
					}
				}

				if (writeBackList.size() > 0) {
					try {
						logger.info("收付回写理赔预付主表开始！计算书号：" + basePartVo.getCertiNo() + " 系统时间" + new Date());
						compensateService.writeBackPay(writeBackList, basePartVo.getCertiNo());
						logger.info("收付回写理赔预付主表结束！计算书号：" + basePartVo.getCertiNo() + " 系统时间" + new Date());
					} catch (Exception e) {
						logger.info("收付回写理赔预付主表异常！计算书号：" + basePartVo.getCertiNo() + " 系统时间" + new Date(), e);
						throw e;
					}
				}

			}else if("C".equals(basePartVo.getCertiType())){//实赔
				if("1".equals(payFlag)){//垫付
					PrpLPadPayMainVo padMainVo = padPayService.findPadPayMainByComp(basePartVo.getRegistNo(),basePartVo.getCertiNo());
					List<PrpLPadPayPersonVo> prpLPadPayPersons = padMainVo.getPrpLPadPayPersons();
					for(PayDataVo payDataVo : basePartVo.getPayDataVo()){
						for(PrpLPadPayPersonVo personVo : prpLPadPayPersons){
							if(personVo.getSerialNo().equals(payDataVo.getSerialNo())){
								personVo.setPayStatus("1");
								personVo.setPayTime(payDataVo.getPayTime());
								//支付轨迹
								try {
									logger.info("收付回写理赔垫付支付轨迹开始！计算书号：" + padMainVo.getCompensateNo() + " 系统时间" + new Date());
									this.savePrplPayHis(padMainVo.getClaimNo(),padMainVo.getCompensateNo(), personVo.getId(),"1","D",payDataVo.getPayTime());
									logger.info("收付回写理赔垫付支付轨迹结束！计算书号：" + padMainVo.getCompensateNo() + " 系统时间" + new Date());
								} catch (Exception e) {
									logger.info("收付回写理赔垫付支付轨迹异常！计算书号：" + padMainVo.getCompensateNo() + " 系统时间" + new Date(), e);
									throw e;
								}
								//获取平安支付主键
								String idClmPaymentResult = personVo.getIdClmPaymentResult();
								if (StringUtils.isNotBlank(idClmPaymentResult)) {
									addPingAnCallbackData(callbackRequestListDtos, payDataVo, idClmPaymentResult);
								}
							}
						}
					}
					
					padPayService.save(padMainVo, null, null);
				}else if("2".equals(payFlag)){//付公估费
					PayDataVo payDataVo = basePartVo.getPayDataVo().get(0);
					PrpLAssessorFeeVo assessorFee = assessorService.findAssessorFeeVoByComp(basePartVo.getRegistNo(), basePartVo.getCertiNo());
					if(assessorFee!=null){
						assessorFee.setPayStatus("1");
						assessorFee.setPayTime(payDataVo.getPayTime());
						assessorFee.setPayAmount(assessorFee.getAmount());
						assessorFee.setTaskStatus("1");
						try {
							logger.info("收付回写理赔公估费开始！计算书号：" + assessorFee.getCompensateNo() + " 报案号：" + assessorFee.getRegistNo() + " 系统时间" + new Date());
							assessorService.updateAssessorFee(assessorFee);
							logger.info("收付回写理赔公估费开始！计算书号：" + assessorFee.getCompensateNo() + " 报案号：" + assessorFee.getRegistNo() + " 系统时间" + new Date());
						} catch (Exception e) {
							logger.info("收付回写理赔公估费异常！计算书号：" + assessorFee.getCompensateNo() + " 报案号：" + assessorFee.getRegistNo() + " 系统时间" + new Date(), e);
							throw e;
						}
					}
				}else if("3".equals(payFlag)){//付查勘费
					PayDataVo payDataVo = basePartVo.getPayDataVo().get(0);
					PrpLCheckFeeVo prpLCheckFeeVo=acheckService.findCheckFeeVoByComp(basePartVo.getRegistNo(), basePartVo.getCertiNo());
					if(prpLCheckFeeVo!=null){
						prpLCheckFeeVo.setPayStatus("1");
						prpLCheckFeeVo.setPayTime(payDataVo.getPayTime());
						prpLCheckFeeVo.setPayAmount(prpLCheckFeeVo.getAmount());
						prpLCheckFeeVo.setTaskStatus("1");
						try {
							logger.info("收付回写理赔查勘费开始！计算书号：" + prpLCheckFeeVo.getCompensateNo() + " 报案号：" + prpLCheckFeeVo.getRegistNo() + " 系统时间" + new Date());
							acheckService.updateCheckFee(prpLCheckFeeVo);
							logger.info("收付回写理赔查勘费开始！计算书号：" + prpLCheckFeeVo.getCompensateNo() + " 报案号：" + prpLCheckFeeVo.getRegistNo() + " 系统时间" + new Date());
						} catch (Exception e) {
							logger.info("收付回写理赔查勘费异常！计算书号：" + prpLCheckFeeVo.getCompensateNo() + " 报案号：" + prpLCheckFeeVo.getRegistNo() + " 系统时间" + new Date(), e);
							throw e;
						}
					}
				}else{//实付
					PrpLCompensateVo compensateVo = compensateTaskService.queryCompByPK(basePartVo.getCertiNo());
					for(PayDataVo payDataVo : basePartVo.getPayDataVo()){
						if("P60".equals(payDataVo.getPayRefReason())||"P6B".equals(payDataVo.getPayRefReason())
								||"P6D".equals(payDataVo.getPayRefReason())){//赔款
							
							isSendPlatform = true;
							//多个收款人时 收付同时
							for(PrpLPaymentVo paymentVo : compensateVo.getPrpLPayments()){
								//通过收付的账号id 判断是否支付
								Long payeeid = payIdMap.get(payDataVo.getAccountNo());
								if(paymentVo.getPayeeId().equals(payeeid) || paymentVo.getSerialNo().equals(payDataVo.getSerialNo())){
									paymentVo.setPayStatus("1");
									paymentVo.setPayTime(payDataVo.getPayTime());
									//支付轨迹
									try {
										logger.info("收付回写理赔赔款支付轨迹开始！计算书号：" + compensateVo.getCompensateNo() + " 系统时间" + new Date());
										this.savePrplPayHis(compensateVo.getClaimNo(),compensateVo.getCompensateNo(), paymentVo.getId(),"1","P",payDataVo.getPayTime());
										logger.info("收付回写理赔赔款支付轨迹结束！计算书号：" + compensateVo.getCompensateNo() + " 系统时间" + new Date());
									} catch (Exception e) {
										logger.info("收付回写理赔赔款支付轨迹异常！计算书号：" + compensateVo.getCompensateNo() + " 系统时间" + new Date(), e);
										throw e;
									}
									//获取平安支付主键
									String idClmPaymentResult = paymentVo.getIdClmPaymentResult();
									if (StringUtils.isNotBlank(idClmPaymentResult)) {
										addPingAnCallbackData(callbackRequestListDtos, payDataVo, idClmPaymentResult);
									}
								}
							}
						}else{//费用
							for(PrpLChargeVo chargeVo : compensateVo.getPrpLCharges()){
								//if(chargeVo.getSerialNo().equals(payDataVo.getSerialNo())){//这里不能用序号
							    Long payeeid = payIdMap.get(payDataVo.getAccountNo());
			                    Map<String,SysCodeDictVo> dictTransMap = codeTranService.findCodeDictTransMap("ChargeCode", "");
			                    SysCodeDictVo sysCodeDictVo = dictTransMap.get(chargeVo.getChargeCode());
			                    if(chargeVo.getPayeeId().equals(payeeid) && payDataVo.getPayRefReason().equals(sysCodeDictVo.getProperty2())){
									chargeVo.setPayStatus("1");
									chargeVo.setPayTime(payDataVo.getPayTime());
									//支付轨迹
									try {
										logger.info("收付回写理赔费用支付轨迹开始！计算书号：" + compensateVo.getCompensateNo() + " 系统时间" + new Date());
										this.savePrplPayHis(compensateVo.getClaimNo(),compensateVo.getCompensateNo(), chargeVo.getId(),"1","F",payDataVo.getPayTime());
										logger.info("收付回写理赔费用支付轨迹结束！计算书号：" + compensateVo.getCompensateNo() + " 系统时间" + new Date());
									} catch (Exception e) {
										logger.info("收付回写理赔费用支付轨迹异常！计算书号：" + compensateVo.getCompensateNo() + " 系统时间" + new Date(), e);
										throw e;
									}
									//获取平安支付主键
									String idClmPaymentResult = chargeVo.getIdClmPaymentResult();
									if (StringUtils.isNotBlank(idClmPaymentResult)) {
										addPingAnCallbackData(callbackRequestListDtos, payDataVo, idClmPaymentResult);
									}
								}
							}
						}
					}
					try {
						logger.info("收付回写理赔实付开始！计算书号：" + compensateVo.getCompensateNo() + " 系统时间" + new Date());
						compensateTaskService.paymentWriteBackCompVo(compensateVo);
						logger.info("收付回写理赔实付结束！计算书号：" + compensateVo.getCompensateNo() + " 系统时间" + new Date());
					}catch (Exception e) {
						logger.info("收付回写理赔实付异常！计算书号：" + compensateVo.getCompensateNo() + " 系统时间" + new Date(), e);
						throw e;
					}
					if(isSendPlatform){//发送平台
						try {
							logger.info("收付回写理赔赔款后送平台开始！计算书号：" + compensateVo.getCompensateNo() + " 系统时间" + new Date());
							sendPaymentToPlatformService.sendPlatform(compensateVo.getCompensateNo());
							logger.info("收付回写理赔赔款后送平台结束！计算书号：" + compensateVo.getCompensateNo() + " 系统时间" + new Date());
						}catch (Exception e) {
							logger.info("收付回写理赔赔款后送平台异常！计算书号：" + compensateVo.getCompensateNo() + " 系统时间" + new Date(), e);
							throw e;
						}
					}
				}
			}
			try {
				//平安支付结果回调
				if (!callbackRequestListDtos.isEmpty()) {
					UnionPayCallbackRequestParamDto unionPayCallbackRequestParamDto = new UnionPayCallbackRequestParamDto();
					unionPayCallbackRequestParamDto.setList(callbackRequestListDtos);
					callBackDataMap.put(basePartVo.getRegistNo(), unionPayCallbackRequestParamDto);
					logger.info("收付回写平安支付结果开始！报案号：" + basePartVo.getRegistNo() + " 系统时间" + new Date());
					pingAnPayCallBackService.payCallBackDataBuild(callBackDataMap);
					logger.info("收付回写平安支付结果结束！报案号：" + basePartVo.getRegistNo() + " 系统时间" + new Date());
				}
			} catch (Exception e) {
				logger.error("收付回写平安支付结果异常！报案号：" + basePartVo.getRegistNo() + " 系统时间" + new Date());
				throw e;
			}
		}else{
			logger.info("收付回写理赔，报文中支付明细信息为空！系统时间为：" + new Date());
			throw new Exception("支付明细信息为空");
		}
	}

	private void addPingAnCallbackData(List<UnionPayCallbackRequestListDto> callbackRequestListDtos, PayDataVo payDataVo, String payeeId) {
		UnionPayCallbackRequestListDto piccCallBakcData = new UnionPayCallbackRequestListDto();
		piccCallBakcData.setPayDate(DateUtils.dateToStr(payDataVo.getPayTime(), DateUtils.YToSec));
		piccCallBakcData.setIdClmPaymentResult(payeeId);
		piccCallBakcData.setNoticeStatus("00");
		callbackRequestListDtos.add(piccCallBakcData);
	}

	/**
	 * webserive 初始化 service需要初始化
	 */
	private void init() {
		if(compensateTaskService==null){
			compensateTaskService = (CompensateTaskService)Springs.getBean(CompensateTaskService.class);
		}
		
		if(sendPaymentToPlatformService==null){
			sendPaymentToPlatformService = (SendPaymentToPlatformService)Springs.getBean(SendPaymentToPlatformService.class);
		}
		
		if(compensateService==null){
			compensateService = (CompensateService)Springs.getBean(CompensateService.class);
		}
		
		if(padPayService==null){
			padPayService = (PadPayService)Springs.getBean(PadPayService.class);
		}
		
		if(assessorService==null){
			assessorService = (AssessorService)Springs.getBean(AssessorService.class);
		}
			
		if(managerService==null){
			managerService = (ManagerService)Springs.getBean(ManagerService.class);
		}
		if(codeTranService==null){
		    codeTranService = (CodeTranService)Springs.getBean(CodeTranService.class);
        }
		
		if(acheckService==null){
			acheckService=(AcheckService)Springs.getBean(AcheckService.class);
		}
		if(msgModelService==null){
			msgModelService=(MsgModelService)Springs.getBean(MsgModelService.class);
		}
		if (pingAnPayCallBackService == null) {
			pingAnPayCallBackService = (PingAnPayCallBackService) Springs.getBean(PingAnPayCallBackService.class);
		}
	}
	
	public void savePrplPayHis(String claimNo,String compensateNo,Long id,String flags,String hisType,Date payTime){
		compensateService.savePrplPayHis(claimNo, compensateNo, id, flags,hisType,payTime);
	}
}
