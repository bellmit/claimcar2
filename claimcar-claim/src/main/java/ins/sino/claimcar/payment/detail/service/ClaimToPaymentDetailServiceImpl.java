/******************************************************************************
 * CREATETIME : 2016年7月16日 下午3:28:24
 ******************************************************************************/
package ins.sino.claimcar.payment.detail.service;

import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.service.facade.PingAnDictService;
import ins.platform.common.service.utils.ServiceUserUtils;
import ins.platform.utils.DataUtils;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.PayFlagType;
import ins.sino.claimcar.CodeConstants.PayReason;
import ins.sino.claimcar.bankaccount.service.BankAccountService;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.services.ClaimInvoiceService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.claim.vo.PrpsmsMessageVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.endcase.service.EndCaseService;
import ins.sino.claimcar.endcase.service.ReOpenCaseService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.endcase.vo.PrpLReCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.interf.vo.ClaimMainReturnVo;
import ins.sino.claimcar.interf.vo.ClaimMainVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.manager.vo.PrpdCheckBankMainVo;
import ins.sino.claimcar.manager.vo.PrpdIntermBankVo;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.manager.vo.PrpdcheckBankVo;
import ins.sino.claimcar.other.service.MsgModelService;
import ins.sino.claimcar.other.service.SendMsgService;
import ins.sino.claimcar.other.vo.PrpLAcheckMainVo;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLAssessorMainVo;
import ins.sino.claimcar.other.vo.PrpLCheckFeeVo;
import ins.sino.claimcar.other.vo.SendMsgParamVo;
import ins.sino.claimcar.other.vo.SysMsgModelVo;
import ins.sino.claimcar.pay.service.PadPayPubService;
import ins.sino.claimcar.payment.detail.vo.JFeeVo;
import ins.sino.claimcar.payment.detail.vo.JPlanMainVo;
import ins.sino.claimcar.payment.detail.vo.JPlanReturnVo;
import ins.sino.claimcar.payment.detail.vo.JlinkAccountVo;
import ins.sino.claimcar.payment.detail.vo.JplanFeeDetailVo;
import ins.sino.claimcar.payment.detail.vo.JplanFeeVo;
import ins.sino.claimcar.payment.detail.webservice.CallPaymentDetailWebService;
import ins.sino.claimcar.payment.service.ClaimToPaymentDetailService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.subrogation.service.PlatLockService;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;
import ins.sino.claimcar.subrogation.vo.PrpLRecoveryOrPayVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;
import com.sinosoft.sff.interf.ClaimBackPrepayPortBindingStub;
import com.sinosoft.sff.interf.ClaimBackPrepayServiceLocator;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 调用收付接口
 * @author ★XMSH
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("claimToPaymentDetailService")
public class ClaimToPaymentDetailServiceImpl implements ClaimToPaymentDetailService {

	private static Logger logger = LoggerFactory.getLogger(ClaimToPaymentDetailServiceImpl.class);

	@Autowired
	private CodeTranService codeTranService;
	@Autowired
	private CallPaymentDetailWebService callPaymentDetailWebService;
	@Autowired
	private ManagerService managerService;
	@Autowired
	private BankAccountService bankAccountService;
	@Autowired
	private CompensateService compensateService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private PlatLockService platLockService;
	@Autowired
	private LossCarService lossCarService;
	@Autowired
	private PropTaskService propTaskService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private ClaimTaskService claimTaskService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	ClaimInvoiceService claimInvoiceService;
	@Autowired
	SendMsgService sendMsgService;
	@Autowired
	PadPayPubService padPayPubService;
	@Autowired
	ClaimTaskService claimService;
	@Autowired
	private EndCasePubService endCasePubService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	ReOpenCaseService reOpenCaseService;
	@Autowired
	EndCaseService endCaseService;
	@Autowired
	ClaimInterfaceLogService logService;
	@Autowired
	MsgModelService msgModelService;
	@Autowired
	PingAnDictService pingAnDictService;
	
	@Override
	public void prePayToPayment(PrpLCompensateVo compensateVo) throws Exception {
//		compensateVo.setUnderwriteDate(new Date());
		BigDecimal sumAmtP =new BigDecimal(0);
		BigDecimal sumAmtF =new BigDecimal(0);
		// 分别获取预付赔款和费用列表
		List<PrpLPrePayVo> prePayPVos = compensateService.getPrePayVo(compensateVo.getCompensateNo(),"P");
		List<PrpLPrePayVo> prePayFVos = compensateService.getPrePayVo(compensateVo.getCompensateNo(),"F");
		
		if(prePayPVos!=null && prePayPVos.size()>0){
			for(PrpLPrePayVo prePayVo:prePayPVos){
				sumAmtP=sumAmtP.add(prePayVo.getPayAmt());
			}
		}
		if(prePayFVos!=null && prePayFVos.size()>0){
			for(PrpLPrePayVo prePayVo:prePayFVos){
				sumAmtF=sumAmtF.add(prePayVo.getPayAmt());
			}
		}
		
		PrpLCMainVo prplcMainVo = policyViewService.findPolicyInfoByPaltform(compensateVo.getRegistNo(), compensateVo.getPolicyNo());
		
		JPlanMainVo jPlanMainVo = new JPlanMainVo();
		List<JFeeVo> jfeeList = new ArrayList<JFeeVo>();
		
		jPlanMainVo.setCertiType("Y");
		jPlanMainVo.setCertiNo(compensateVo.getCompensateNo());
		jPlanMainVo.setPolicyNo(compensateVo.getPolicyNo());
		jPlanMainVo.setRegistNo(compensateVo.getRegistNo());
		jPlanMainVo.setClaimNo(compensateVo.getClaimNo());
		jPlanMainVo.setOperateCode(compensateVo.getCreateUser());
		jPlanMainVo.setOperateComCode(compensateVo.getComCode());
		jPlanMainVo.setPayComCode(compensateVo.getComCode());
		if("1101".equals(prplcMainVo.getRiskCode())) {
			if(StringUtils.isNotBlank(prplcMainVo.getIsTwentyFlag())) {
				jPlanMainVo.setIsTwentyFlag(prplcMainVo.getIsTwentyFlag()); // 2020条款交强险标志
			}else {
				jPlanMainVo.setIsTwentyFlag("0"); // 2020条款交强险标志
			}
		}

		if(BigDecimal.ZERO.compareTo(DataUtils.NullToZero(sumAmtP))==0){
		}else{
			if(prePayPVos!=null&&prePayPVos.size()>0){// 预付赔款
	
				Map<String,PrpLPayCustomVo> customMap = new HashMap<String,PrpLPayCustomVo>();
				for(PrpLPrePayVo prePayVo:prePayPVos){
	
					// 合并收款直付帐号，根据收款人和摘要合并
					PrpLPayCustomVo customVo = managerService.findPayCustomVoById(prePayVo.getPayeeId());
					String key = prePayVo.getPayeeId()+prePayVo.getSummary();
					if(customMap.containsKey(key)){
						PrpLPayCustomVo payCustomVo = customMap.get(key);
						payCustomVo.setSumAmt(prePayVo.getPayAmt().doubleValue()+payCustomVo.getSumAmt());
						payCustomVo.getPrpLPrePayVos().add(prePayVo);
						customMap.put(key,payCustomVo);
					}else{
						customVo.setOtherFlag(prePayVo.getOtherFlag());
						customVo.setOtherCause(prePayVo.getOtherCause());
						customVo.setSumAmt(prePayVo.getPayAmt().doubleValue());// 收款人赔款金额
						customVo.setSummary(prePayVo.getSummary());
						customVo.getPrpLPrePayVos().add(prePayVo);
						customMap.put(key,customVo);
					}
				}
				for(PrpLPayCustomVo payCustomVo:customMap.values()){
					JFeeVo planFeeVo = new JFeeVo();
					JplanFeeVo jplanFeeVo = new JplanFeeVo();
					jplanFeeVo.setPayRefReason("P50");
					jplanFeeVo.setCurrency("CNY");
					jplanFeeVo.setUnderWriteDate(compensateVo.getUnderwriteDate());
					jplanFeeVo.setAppliCode(prplcMainVo.getAppliCode());
					jplanFeeVo.setAppliName(prplcMainVo.getAppliName());
					jplanFeeVo.setPlanFee(payCustomVo.getSumAmt());
	
					// 收款明细
					List<JplanFeeDetailVo> detailVos = new ArrayList<JplanFeeDetailVo>();
					Map<String,Double> datailMap = new HashMap<String,Double>();
					// 合并同一收款人和险别
					for(PrpLPrePayVo prePayVo:payCustomVo.getPrpLPrePayVos()){
						// Fix By LiYi 发送报文的多个SerialNo可能不一样，不能随便取值，根据compensateno取对应保单的相应值
						if(prePayVo.getCompensateNo().equals(jPlanMainVo.getCertiNo())){
							jplanFeeVo.setSerialNo(Integer.parseInt(prePayVo.getSerialNo()));// 因为合并赔款人，序列号随便取一条
						}
						if(datailMap.containsKey(prePayVo.getKindCode())){
							Double sumPayAmt = prePayVo.getPayAmt().doubleValue()+datailMap.get(prePayVo.getKindCode());
							datailMap.put(prePayVo.getKindCode(),sumPayAmt);
						}else{
							datailMap.put(prePayVo.getKindCode(),prePayVo.getPayAmt().doubleValue());
						}
					}
					// 收付险别明细
					for(Map.Entry<String,Double> entry:datailMap.entrySet()){
						JplanFeeDetailVo detailVo = new JplanFeeDetailVo();
						detailVo.setKindCode(entry.getKey());
						detailVo.setPlanfee(entry.getValue());
						detailVos.add(detailVo);
					}
					jplanFeeVo.setJplanFeeDetailVos(detailVos);
					planFeeVo.setJplanFeeVo(jplanFeeVo);
					
	
					JlinkAccountVo jlinkAccountVo = new JlinkAccountVo();// 收款支付账号明细数据
					jlinkAccountVo.setPayRefReason("P50");
					jlinkAccountVo.setPlanFee(payCustomVo.getSumAmt());
					jlinkAccountVo.setAccountNo(payCustomVo.getAccountId());
					jlinkAccountVo.setPayReasonFlag(payCustomVo.getOtherFlag());
					jlinkAccountVo.setPayReason(payCustomVo.getOtherCause());
					jlinkAccountVo.setTelephone(payCustomVo.getPayeeMobile());
					// jlinkAccountVo.setPayObject(payObject);
					// jlinkAccountVo.setSubPayObject(subPayObject);
					// if("1".equals(payCustomVo.getPayObjectType())){// 收款人性质，1-个人，2-机构
	//					jlinkAccountVo.setCentiType("01");
	//				}else{
	//					jlinkAccountVo.setCentiType("71");
	//				}
					jlinkAccountVo.setCentiType(payCustomVo.getCertifyType());
					jlinkAccountVo.setCentiCode(payCustomVo.getCertifyNo());
					//  摘要待调整
					String privateFlag = payCustomVo.getPublicAndPrivate();
					jlinkAccountVo.setPublicPrivateFlag(privateFlag==null ? "0" : privateFlag);// 公私标志
					jlinkAccountVo.setIsAutoPay(compensateVo.getPrpLCompensateExt().getIsAutoPay());// 是否推送资金系统
					jlinkAccountVo.setIsExpress("0");// 是否加急（0：否1：是）
					jlinkAccountVo.setIsFastReparation(compensateVo.getPrpLCompensateExt().getIsFastReparation());// 是否快赔
					// 短信发送
					PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(compensateVo.getRegistNo());
	                List<PrpLCItemKindVo> prpCItemKinds = registQueryService.findCItemKindListByRegistNo(compensateVo.getRegistNo());
					SendMsgParamVo msgParamVo = new SendMsgParamVo();
					msgParamVo.setRegistNo(compensateVo.getRegistNo());
					msgParamVo.setLicenseNo(prpLRegistVo.getPrpLRegistExt().getLicenseNo());
					msgParamVo.setDamageTime(prpLRegistVo.getDamageTime());
					msgParamVo.setSumAmt(payCustomVo.getSumAmt().toString());
					// 主险金额
					msgParamVo.setPrpCItemKinds(prpCItemKinds);
					//linkerName
					msgParamVo.setLinkerName(payCustomVo.getPayeeName());
					SysMsgModelVo msgModelVo = null;
					msgModelVo = this.findSysMsgModelVo(prpLRegistVo,CodeConstants.CompensateFlag.prePay);
					if(msgModelVo != null){
						String message = sendMsgService.getMessage(msgModelVo.getContent(), msgParamVo);
						jlinkAccountVo.setMessageContent(message);// 短信
						}
					if(StringUtils.isNotBlank(payCustomVo.getSummary())){
						jlinkAccountVo.setAbstractContent(payCustomVo.getSummary());// 摘要
					}else{
						jlinkAccountVo.setAbstractContent("");
					}
					// jlinkAccountVo.setAbstractContent(prpLRegistVo.getPrpLRegistExt().getLicenseNo()+"赔款");//摘要
					if(StringUtils.isNotBlank(payCustomVo.getPurpose())){
						jlinkAccountVo.setUsage(payCustomVo.getPurpose());// 用途
					}else{
						jlinkAccountVo.setUsage("");// 用途
					}
					if(StringUtils.isNotBlank(payCustomVo.getPurpose())){
						jlinkAccountVo.setOtherRemark(payCustomVo.getRemark());// 备注
                    }else{
						jlinkAccountVo.setOtherRemark("");// 备注
                    }
					
				/*	String message = "待补充";
					jlinkAccountVo.setMessageContent(message);//短信内容
	*/				
					planFeeVo.setJlinkAccountVo(jlinkAccountVo);
					jfeeList.add(planFeeVo);
				}
	
				
			}
		}
		if(BigDecimal.ZERO.compareTo(DataUtils.NullToZero(sumAmtF))==0){
		}else{
			if(prePayFVos!=null&&prePayFVos.size()>0){// 预付费用
				for(PrpLPrePayVo prePayVo:prePayFVos){
					PrpLPayCustomVo customVo = managerService.findPayCustomVoById(prePayVo.getPayeeId());
					SysCodeDictVo dictVo = codeTranService.findTransCodeDictVo("ChargeCode",prePayVo.getChargeCode());
					
					JFeeVo jfeeVo = new JFeeVo();
					JplanFeeVo jPlanFeeVo = new JplanFeeVo();// 收付明细
					jPlanFeeVo.setSerialNo(Integer.parseInt(prePayVo.getSerialNo()));
					jPlanFeeVo.setPayRefReason(dictVo.getProperty1());// 预付使用property1
					jPlanFeeVo.setCurrency("CNY");
					jPlanFeeVo.setPlanFee(prePayVo.getPayAmt().doubleValue());
					jPlanFeeVo.setUnderWriteDate(compensateVo.getUnderwriteDate());
					jPlanFeeVo.setAppliCode(prplcMainVo.getAppliCode());
					jPlanFeeVo.setAppliName(prplcMainVo.getAppliName());
	//				jPlanFeeVo.setAppliCode(customVo.getCertifyNo());
	//				jPlanFeeVo.setAppliName(customVo.getPayeeName());
					// 收款明细--预付可以不拆分
					List<JplanFeeDetailVo> detailVos = new ArrayList<JplanFeeDetailVo>();
					JplanFeeDetailVo detailVo = new JplanFeeDetailVo();
					detailVo.setKindCode(prePayVo.getKindCode());
					detailVo.setPlanfee(prePayVo.getPayAmt().doubleValue());
					detailVos.add(detailVo);
					jPlanFeeVo.setJplanFeeDetailVos(detailVos);
					jfeeVo.setJplanFeeVo(jPlanFeeVo);
	
					JlinkAccountVo jlinkAccountVo = new JlinkAccountVo();// 收款支付账号明细数据
					jlinkAccountVo.setPayRefReason(dictVo.getProperty1());
					jlinkAccountVo.setPlanFee(prePayVo.getPayAmt().doubleValue());
					jlinkAccountVo.setAccountNo(customVo.getAccountId());
					jlinkAccountVo.setPayReasonFlag("0");
					jlinkAccountVo.setPayReason(prePayVo.getOtherCause());
					jlinkAccountVo.setTelephone(customVo.getPayeeMobile());
					// jlinkAccountVo.setPayObject(payObject);//支付对象
					// jlinkAccountVo.setSubPayObject(subPayObject);//支付子对象
					if("1".equals(customVo.getPayObjectType())){// 收款人性质，1-个人，2-机构
						jlinkAccountVo.setCentiType("01");
					}else{
						jlinkAccountVo.setCentiType("71");
					}
					jlinkAccountVo.setCentiCode(customVo.getCertifyNo());
					
                    String privateFlag = customVo.getPublicAndPrivate();
					jlinkAccountVo.setPublicPrivateFlag(privateFlag==null ? "0" : privateFlag);// 公私标志
					jlinkAccountVo.setAbstractContent(prePayVo.getSummary());// 摘要
					jlinkAccountVo.setIsAutoPay("0");// 资金二期修改赔款不推送资金系统
					jlinkAccountVo.setIsExpress("0");// 是否加急（0：否1：是）
					jlinkAccountVo.setIsFastReparation("0");// 资金二期修改赔款非快赔
                    
                    if(StringUtils.isNotBlank(customVo.getPurpose())){
						jlinkAccountVo.setUsage(customVo.getPurpose());// 用途
                    }else{
						jlinkAccountVo.setUsage("");// 用途
                    }
                    if(StringUtils.isNotBlank(customVo.getPurpose())){
						jlinkAccountVo.setOtherRemark(customVo.getRemark());// 备注
                    }else{
						jlinkAccountVo.setOtherRemark("");// 备注
                    }
					jlinkAccountVo.setMessageContent("");// 费用不发短信
					jfeeVo.setJlinkAccountVo(jlinkAccountVo);
					
					
					jfeeList.add(jfeeVo);
				}
			}
		}
		jPlanMainVo.setJFeeVos(jfeeList);
		
		// 送收付接口
		JPlanReturnVo returnVo = callPaymentDetailWebService.callPaymentForClient(jPlanMainVo,BusinessType.Payment_prePay,compensateVo.getCompensateNo(),"1");
		// 送收付成功后回写预付PAYSTATUS字段为2-表示送收付成功
		if(returnVo.isResponseCode()){
			List<PrpLPrePayVo> prePayVos = new ArrayList<PrpLPrePayVo>();
			if(prePayFVos!=null && prePayFVos.size()>0){
				for(PrpLPrePayVo prePayFVo:prePayFVos){
					if(BigDecimal.ZERO.compareTo(prePayFVo.getPayAmt())!=0){
						prePayFVo.setPayStatus("2");
						// 支付历史轨迹Y预付D垫付P赔款F费用
						this.savePrplPayHis(compensateVo.getClaimNo(),prePayFVo.getCompensateNo(), prePayFVo.getId(),"2","Y");
					}else{
						this.savePrplPayHis(compensateVo.getClaimNo(),prePayFVo.getCompensateNo(), prePayFVo.getId(),"0","Y");
					}
				}
				prePayVos.addAll(prePayFVos);
			}
			if(prePayPVos!=null && prePayPVos.size()>0){
				for(PrpLPrePayVo prePayPVo:prePayPVos){
					if(BigDecimal.ZERO.compareTo(prePayPVo.getPayAmt())==-1){
						prePayPVo.setPayStatus("2");
						// 支付历史轨迹Y预付D垫付P赔款F费用
						this.savePrplPayHis(compensateVo.getClaimNo(),prePayPVo.getCompensateNo(), prePayPVo.getId(),"2","Y");
					}else{
						this.savePrplPayHis(compensateVo.getClaimNo(),prePayPVo.getCompensateNo(), prePayPVo.getId(),"0","Y");
					}
				}
				prePayVos.addAll(prePayPVos);
			}
			
			compensateService.saveOrUpdatePrePay(prePayVos,compensateVo.getCompensateNo());
		}
	}

	@Override
	public void pingAnPrePayToPayment(PrpLCompensateVo compensateVo,List<PrpLPrePayVo> prePayVoPingAn) throws Exception {
//		compensateVo.setUnderwriteDate(new Date());
		BigDecimal sumAmtP =new BigDecimal(0);
		BigDecimal sumAmtF =new BigDecimal(0);
		List<PrpLPrePayVo> prePayPVos = new ArrayList<>();
		List<PrpLPrePayVo> prePayFVos = new ArrayList<>();
		if(prePayVoPingAn!=null && prePayVoPingAn.size()>0){
			for (PrpLPrePayVo prePayVo : prePayVoPingAn) {
				if(prePayVo.getFeeType() != null && "P".equals(prePayVo.getFeeType())){
					prePayPVos.add(prePayVo);
				}else if(prePayVo.getFeeType() != null && "F".equals(prePayVo.getFeeType())){
					prePayFVos.add(prePayVo);
				}
			}
		}
		if(prePayPVos!=null && prePayPVos.size()>0){
			for(PrpLPrePayVo prePayVo:prePayPVos){
				sumAmtP=sumAmtP.add(prePayVo.getPayAmt());
			}
		}
		if(prePayFVos!=null && prePayFVos.size()>0){
			for(PrpLPrePayVo prePayVo:prePayFVos){
				sumAmtF=sumAmtF.add(prePayVo.getPayAmt());
			}
		}

		PrpLCMainVo prplcMainVo = policyViewService.findPolicyInfoByPaltform(compensateVo.getRegistNo(), compensateVo.getPolicyNo());

		JPlanMainVo jPlanMainVo = new JPlanMainVo();
		List<JFeeVo> jfeeList = new ArrayList<JFeeVo>();

		jPlanMainVo.setCertiType("Y");
		jPlanMainVo.setCertiNo(compensateVo.getCompensateNo());
		jPlanMainVo.setPolicyNo(compensateVo.getPolicyNo());
		jPlanMainVo.setRegistNo(compensateVo.getRegistNo());
		jPlanMainVo.setClaimNo(compensateVo.getClaimNo());
		jPlanMainVo.setOperateCode(compensateVo.getCreateUser());
		jPlanMainVo.setOperateComCode(compensateVo.getComCode());
		jPlanMainVo.setPayComCode(compensateVo.getComCode());
		if(BigDecimal.ZERO.compareTo(DataUtils.NullToZero(sumAmtP))==0){
		}else{
			if(prePayPVos!=null&&prePayPVos.size()>0){// 预付赔款

				Map<String,PrpLPayCustomVo> customMap = new HashMap<String,PrpLPayCustomVo>();
				for(PrpLPrePayVo prePayVo:prePayPVos){

					// 合并收款直付帐号，根据收款人和摘要合并
					PrpLPayCustomVo customVo = managerService.findPayCustomVoById(prePayVo.getPayeeId());
					String key = prePayVo.getPayeeId()+prePayVo.getSummary();
					if(customMap.containsKey(key)){
						PrpLPayCustomVo payCustomVo = customMap.get(key);
						payCustomVo.setSumAmt(prePayVo.getPayAmt().doubleValue()+payCustomVo.getSumAmt());
						payCustomVo.getPrpLPrePayVos().add(prePayVo);
						customMap.put(key,payCustomVo);
					}else{
						customVo.setOtherFlag(prePayVo.getOtherFlag());
						customVo.setOtherCause(prePayVo.getOtherCause());
						customVo.setSumAmt(prePayVo.getPayAmt().doubleValue());// 收款人赔款金额
						customVo.setSummary(prePayVo.getSummary());
						customVo.getPrpLPrePayVos().add(prePayVo);
						customMap.put(key,customVo);
					}
				}
				for(PrpLPayCustomVo payCustomVo:customMap.values()){
					JFeeVo planFeeVo = new JFeeVo();
					JplanFeeVo jplanFeeVo = new JplanFeeVo();
					jplanFeeVo.setPayRefReason("P50");
					jplanFeeVo.setCurrency("CNY");
					jplanFeeVo.setUnderWriteDate(compensateVo.getUnderwriteDate());
					jplanFeeVo.setAppliCode(prplcMainVo.getAppliCode());
					jplanFeeVo.setAppliName(prplcMainVo.getAppliName());
					jplanFeeVo.setPlanFee(payCustomVo.getSumAmt());

					// 收款明细
					List<JplanFeeDetailVo> detailVos = new ArrayList<JplanFeeDetailVo>();
					Map<String,Double> datailMap = new HashMap<String,Double>();
					// 合并同一收款人和险别
					for(PrpLPrePayVo prePayVo:payCustomVo.getPrpLPrePayVos()){
						// Fix By LiYi 发送报文的多个SerialNo可能不一样，不能随便取值，根据compensateno取对应保单的相应值
						if(prePayVo.getCompensateNo().equals(jPlanMainVo.getCertiNo())){
							jplanFeeVo.setSerialNo(Integer.parseInt(prePayVo.getSerialNo()));// 因为合并赔款人，序列号随便取一条
						}
						if(datailMap.containsKey(prePayVo.getKindCode())){
							Double sumPayAmt = prePayVo.getPayAmt().doubleValue()+datailMap.get(prePayVo.getKindCode());
							datailMap.put(prePayVo.getKindCode(),sumPayAmt);
						}else{
							datailMap.put(prePayVo.getKindCode(),prePayVo.getPayAmt().doubleValue());
						}
					}
					// 收付险别明细
					for(Map.Entry<String,Double> entry:datailMap.entrySet()){
						JplanFeeDetailVo detailVo = new JplanFeeDetailVo();
						detailVo.setKindCode(entry.getKey());
						detailVo.setPlanfee(entry.getValue());
						detailVos.add(detailVo);
					}
					jplanFeeVo.setJplanFeeDetailVos(detailVos);
					planFeeVo.setJplanFeeVo(jplanFeeVo);


					JlinkAccountVo jlinkAccountVo = new JlinkAccountVo();// 收款支付账号明细数据
					jlinkAccountVo.setPayRefReason("P50");
					jlinkAccountVo.setPlanFee(payCustomVo.getSumAmt());
					jlinkAccountVo.setAccountNo(payCustomVo.getAccountId());
					jlinkAccountVo.setPayReasonFlag(payCustomVo.getOtherFlag());
					jlinkAccountVo.setPayReason(payCustomVo.getOtherCause());
					jlinkAccountVo.setTelephone(payCustomVo.getPayeeMobile());
					// jlinkAccountVo.setPayObject(payObject);
					// jlinkAccountVo.setSubPayObject(subPayObject);
					// if("1".equals(payCustomVo.getPayObjectType())){// 收款人性质，1-个人，2-机构
					//					jlinkAccountVo.setCentiType("01");
					//				}else{
					//					jlinkAccountVo.setCentiType("71");
					//				}
					jlinkAccountVo.setCentiType(payCustomVo.getCertifyType());
					jlinkAccountVo.setCentiCode(payCustomVo.getCertifyNo());
					//  摘要待调整
					String privateFlag = payCustomVo.getPublicAndPrivate();
					jlinkAccountVo.setPublicPrivateFlag(privateFlag==null ? "0" : privateFlag);// 公私标志
					jlinkAccountVo.setIsAutoPay(compensateVo.getPrpLCompensateExt().getIsAutoPay());// 是否推送资金系统
					jlinkAccountVo.setIsExpress("0");// 是否加急（0：否1：是）
					jlinkAccountVo.setIsFastReparation(compensateVo.getPrpLCompensateExt().getIsFastReparation());// 是否快赔
					// 短信发送
					PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(compensateVo.getRegistNo());
					List<PrpLCItemKindVo> prpCItemKinds = registQueryService.findCItemKindListByRegistNo(compensateVo.getRegistNo());
					SendMsgParamVo msgParamVo = new SendMsgParamVo();
					msgParamVo.setRegistNo(compensateVo.getRegistNo());
					msgParamVo.setLicenseNo(prpLRegistVo.getPrpLRegistExt().getLicenseNo());
					msgParamVo.setDamageTime(prpLRegistVo.getDamageTime());
					msgParamVo.setSumAmt(payCustomVo.getSumAmt().toString());
					// 主险金额
					msgParamVo.setPrpCItemKinds(prpCItemKinds);
					//linkerName
					msgParamVo.setLinkerName(payCustomVo.getPayeeName());
					SysMsgModelVo msgModelVo = null;
					msgModelVo = this.findSysMsgModelVo(prpLRegistVo,CodeConstants.CompensateFlag.prePay);
					if(msgModelVo != null){
						String message = sendMsgService.getMessage(msgModelVo.getContent(), msgParamVo);
						jlinkAccountVo.setMessageContent(message);// 短信
					}
					if(StringUtils.isNotBlank(payCustomVo.getSummary())){
						jlinkAccountVo.setAbstractContent(payCustomVo.getSummary());// 摘要
					}else{
						jlinkAccountVo.setAbstractContent("");
					}
					// jlinkAccountVo.setAbstractContent(prpLRegistVo.getPrpLRegistExt().getLicenseNo()+"赔款");//摘要
					if(StringUtils.isNotBlank(payCustomVo.getPurpose())){
						jlinkAccountVo.setUsage(payCustomVo.getPurpose());// 用途
					}else{
						jlinkAccountVo.setUsage("");// 用途
					}
					if(StringUtils.isNotBlank(payCustomVo.getPurpose())){
						jlinkAccountVo.setOtherRemark(payCustomVo.getRemark());// 备注
					}else{
						jlinkAccountVo.setOtherRemark("");// 备注
					}

				/*	String message = "待补充";
					jlinkAccountVo.setMessageContent(message);//短信内容
	*/
					planFeeVo.setJlinkAccountVo(jlinkAccountVo);
					jfeeList.add(planFeeVo);
				}


			}
		}
		if(BigDecimal.ZERO.compareTo(DataUtils.NullToZero(sumAmtF))==0){
		}else{
			if(prePayFVos!=null&&prePayFVos.size()>0){// 预付费用
				for(PrpLPrePayVo prePayVo:prePayFVos){
					PrpLPayCustomVo customVo = managerService.findPayCustomVoById(prePayVo.getPayeeId());
					SysCodeDictVo dictVo = codeTranService.findTransCodeDictVo("ChargeCode",prePayVo.getChargeCode());

					JFeeVo jfeeVo = new JFeeVo();
					JplanFeeVo jPlanFeeVo = new JplanFeeVo();// 收付明细
					jPlanFeeVo.setSerialNo(Integer.parseInt(prePayVo.getSerialNo()));
					jPlanFeeVo.setPayRefReason(dictVo.getProperty1());// 预付使用property1
					jPlanFeeVo.setCurrency("CNY");
					jPlanFeeVo.setPlanFee(prePayVo.getPayAmt().doubleValue());
					jPlanFeeVo.setUnderWriteDate(compensateVo.getUnderwriteDate());
					jPlanFeeVo.setAppliCode(prplcMainVo.getAppliCode());
					jPlanFeeVo.setAppliName(prplcMainVo.getAppliName());
					//				jPlanFeeVo.setAppliCode(customVo.getCertifyNo());
					//				jPlanFeeVo.setAppliName(customVo.getPayeeName());
					// 收款明细--预付可以不拆分
					List<JplanFeeDetailVo> detailVos = new ArrayList<JplanFeeDetailVo>();
					JplanFeeDetailVo detailVo = new JplanFeeDetailVo();
					detailVo.setKindCode(prePayVo.getKindCode());
					detailVo.setPlanfee(prePayVo.getPayAmt().doubleValue());
					detailVos.add(detailVo);
					jPlanFeeVo.setJplanFeeDetailVos(detailVos);
					jfeeVo.setJplanFeeVo(jPlanFeeVo);

					JlinkAccountVo jlinkAccountVo = new JlinkAccountVo();// 收款支付账号明细数据
					jlinkAccountVo.setPayRefReason(dictVo.getProperty1());
					jlinkAccountVo.setPlanFee(prePayVo.getPayAmt().doubleValue());
					jlinkAccountVo.setAccountNo(customVo.getAccountId());
					jlinkAccountVo.setPayReasonFlag("0");
					jlinkAccountVo.setPayReason(prePayVo.getOtherCause());
					jlinkAccountVo.setTelephone(customVo.getPayeeMobile());
					// jlinkAccountVo.setPayObject(payObject);//支付对象
					// jlinkAccountVo.setSubPayObject(subPayObject);//支付子对象
					if("1".equals(customVo.getPayObjectType())){// 收款人性质，1-个人，2-机构
						jlinkAccountVo.setCentiType("01");
					}else{
						jlinkAccountVo.setCentiType("71");
					}
					jlinkAccountVo.setCentiCode(customVo.getCertifyNo());

					String privateFlag = customVo.getPublicAndPrivate();
					jlinkAccountVo.setPublicPrivateFlag(privateFlag==null ? "0" : privateFlag);// 公私标志
					jlinkAccountVo.setAbstractContent(prePayVo.getSummary());// 摘要
					jlinkAccountVo.setIsAutoPay("0");// 资金二期修改赔款不推送资金系统
					jlinkAccountVo.setIsExpress("0");// 是否加急（0：否1：是）
					jlinkAccountVo.setIsFastReparation("0");// 资金二期修改赔款非快赔

					if(StringUtils.isNotBlank(customVo.getPurpose())){
						jlinkAccountVo.setUsage(customVo.getPurpose());// 用途
					}else{
						jlinkAccountVo.setUsage("");// 用途
					}
					if(StringUtils.isNotBlank(customVo.getPurpose())){
						jlinkAccountVo.setOtherRemark(customVo.getRemark());// 备注
					}else{
						jlinkAccountVo.setOtherRemark("");// 备注
					}
					jlinkAccountVo.setMessageContent("");// 费用不发短信
					jfeeVo.setJlinkAccountVo(jlinkAccountVo);


					jfeeList.add(jfeeVo);
				}
			}
		}
		jPlanMainVo.setJFeeVos(jfeeList);

		// 送收付接口
		JPlanReturnVo returnVo = callPaymentDetailWebService.callPaymentForClient(jPlanMainVo,BusinessType.Payment_prePay,compensateVo.getCompensateNo(),"1");
		// 送收付成功后回写预付PAYSTATUS字段为2-表示送收付成功
		if(returnVo.isResponseCode()){
			List<PrpLPrePayVo> prePayVos = new ArrayList<PrpLPrePayVo>();
			if(prePayFVos!=null && prePayFVos.size()>0){
				for(PrpLPrePayVo prePayFVo:prePayFVos){
					if(BigDecimal.ZERO.compareTo(prePayFVo.getPayAmt())!=0){
						prePayFVo.setPayStatus("2");
						// 支付历史轨迹Y预付D垫付P赔款F费用
						this.savePrplPayHis(compensateVo.getClaimNo(),prePayFVo.getCompensateNo(), prePayFVo.getId(),"2","Y");
					}else{
						this.savePrplPayHis(compensateVo.getClaimNo(),prePayFVo.getCompensateNo(), prePayFVo.getId(),"0","Y");
					}
				}
				prePayVos.addAll(prePayFVos);
			}
			if(prePayPVos!=null && prePayPVos.size()>0){
				for(PrpLPrePayVo prePayPVo:prePayPVos){
					if(BigDecimal.ZERO.compareTo(prePayPVo.getPayAmt())==-1){
						prePayPVo.setPayStatus("2");
						// 支付历史轨迹Y预付D垫付P赔款F费用
						this.savePrplPayHis(compensateVo.getClaimNo(),prePayPVo.getCompensateNo(), prePayPVo.getId(),"2","Y");
					}else{
						this.savePrplPayHis(compensateVo.getClaimNo(),prePayPVo.getCompensateNo(), prePayPVo.getId(),"0","Y");
					}
				}
				prePayVos.addAll(prePayPVos);
			}

			compensateService.saveOrUpdatePrePay(prePayVos,compensateVo.getCompensateNo());
		}
	}

	@Override
	public void padPayToPayment(PrpLPadPayMainVo padPayMainVo) throws Exception {
		BigDecimal costSum =new BigDecimal(0);
		if(padPayMainVo.getPrpLPadPayPersons()!=null && padPayMainVo.getPrpLPadPayPersons().size()>0){
			for(PrpLPadPayPersonVo padPayPersonVo:padPayMainVo.getPrpLPadPayPersons()){
				costSum=costSum.add(padPayPersonVo.getCostSum());
			}
		}
		if(BigDecimal.ZERO.compareTo(DataUtils.NullToZero(costSum))==0){// 垫付赔款 为0 不送收付
		}else{
			JPlanMainVo jPlanMainVo = new JPlanMainVo();
			List<JFeeVo> jfeeList = new ArrayList<JFeeVo>();
			
	//		String makComCode = policyViewService.findPolicyComCode(padPayMainVo.getRegistNo(),"11");
			PrpLCMainVo prplcMainVo = policyViewService.findPolicyInfoByPaltform(padPayMainVo.getRegistNo(), padPayMainVo.getPolicyNo());
			String makComCode = prplcMainVo.getComCode();
			
			jPlanMainVo.setCertiType("C");
			jPlanMainVo.setCertiNo(padPayMainVo.getCompensateNo());
			jPlanMainVo.setPolicyNo(padPayMainVo.getPolicyNo());
			jPlanMainVo.setRegistNo(padPayMainVo.getRegistNo());
			jPlanMainVo.setClaimNo(padPayMainVo.getClaimNo());
			jPlanMainVo.setOperateCode(padPayMainVo.getCreateUser());
			jPlanMainVo.setOperateComCode(padPayMainVo.getComCode());
			jPlanMainVo.setPayComCode(makComCode);
			if("1101".equals(prplcMainVo.getRiskCode())) {
				if(StringUtils.isNotBlank(prplcMainVo.getIsTwentyFlag())) {
					jPlanMainVo.setIsTwentyFlag(prplcMainVo.getIsTwentyFlag()); // 2020条款交强险标志
				}else {
					jPlanMainVo.setIsTwentyFlag("0"); // 2020条款交强险标志
				}
			}
	
			Map<String,PrpLPadPayPersonVo> map = new HashMap<String,PrpLPadPayPersonVo>();
			for(PrpLPadPayPersonVo padPayPersonVo:padPayMainVo.getPrpLPadPayPersons()){
				PrpLPayCustomVo customVo = managerService.findPayCustomVoById(padPayPersonVo.getPayeeId());
				String key = customVo.getAccountId()+padPayPersonVo.getSummary();
				if(map.containsKey(key)){
					PrpLPadPayPersonVo personVo = map.get(key);
					BigDecimal sumCostSum = personVo.getCostSum().add(padPayPersonVo.getCostSum());
					personVo.setCostSum(sumCostSum);
					map.put(key,personVo);
				}else{
					map.put(key,padPayPersonVo);
				}
			}
			
			// 合并相同的垫付收款人
			for(PrpLPadPayPersonVo padPayPersonVo:map.values()){
				PrpLPayCustomVo customVo = managerService.findPayCustomVoById(padPayPersonVo.getPayeeId());
				JplanFeeVo jPlanFeeVo = new JplanFeeVo();// 收付明细
				JFeeVo jfeeVo = new JFeeVo();
	
				jPlanFeeVo.setSerialNo(Integer.parseInt(padPayPersonVo.getSerialNo()));
				jPlanFeeVo.setPayRefReason("P6K");
				jPlanFeeVo.setCurrency("CNY");
				jPlanFeeVo.setPlanFee(padPayPersonVo.getCostSum().doubleValue());
				jPlanFeeVo.setUnderWriteDate(padPayMainVo.getUnderwriteDate());
				jPlanFeeVo.setAppliCode(prplcMainVo.getAppliCode());
				jPlanFeeVo.setAppliName(prplcMainVo.getAppliName());
	//			jPlanFeeVo.setAppliCode(customVo.getCertifyNo());
	//			jPlanFeeVo.setAppliName(customVo.getPayeeName());
	
				List<JplanFeeDetailVo> detailVos = new ArrayList<JplanFeeDetailVo>();
				JplanFeeDetailVo detailVo = new JplanFeeDetailVo();
				detailVo.setKindCode("BZ");// 垫付只垫付交强
				detailVo.setPlanfee(padPayPersonVo.getCostSum().doubleValue());
				detailVos.add(detailVo);
				jPlanFeeVo.setJplanFeeDetailVos(detailVos);
				jfeeVo.setJplanFeeVo(jPlanFeeVo);
				
				JlinkAccountVo jlinkAccountVo = new JlinkAccountVo();// 收款支付账号明细数据
				jlinkAccountVo.setPayRefReason("P6K");
				jlinkAccountVo.setPlanFee(padPayPersonVo.getCostSum().doubleValue());
				jlinkAccountVo.setAccountNo(customVo.getAccountId());
				jlinkAccountVo.setPayReasonFlag(padPayPersonVo.getOtherFlag());
				jlinkAccountVo.setPayReason(padPayPersonVo.getOtherCause());
				jlinkAccountVo.setTelephone(customVo.getPayeeMobile());
				// jlinkAccountVo.setPayObject(payObject);//支付对象
				// jlinkAccountVo.setSubPayObject(subPayObject);//支付子对象
				// if("1".equals(customVo.getPayObjectType())){// 收款人性质，1-个人，2-机构
	//				jlinkAccountVo.setCentiType("01");
	//			}else{
	//				jlinkAccountVo.setCentiType("71");
	//			}
				jlinkAccountVo.setCentiType(customVo.getCertifyType());
				jlinkAccountVo.setCentiCode(customVo.getCertifyNo());
				//  摘要待调整
				String privateFlag = customVo.getPublicAndPrivate();
				jlinkAccountVo.setPublicPrivateFlag(privateFlag==null ? "0" : privateFlag);// 公私标志
				
				jlinkAccountVo.setIsAutoPay(padPayMainVo.getIsAutoPay());// 是否推送资金系统
				jlinkAccountVo.setIsExpress("0");// 是否加急（0：否1：是）
				jlinkAccountVo.setIsFastReparation(padPayMainVo.getIsFastReparation());// 是否快赔
				// 短信发送
				PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(padPayMainVo.getRegistNo());
                List<PrpLCItemKindVo> prpCItemKinds = registQueryService.findCItemKindListByRegistNo(padPayMainVo.getRegistNo());
				SendMsgParamVo msgParamVo = new SendMsgParamVo();
				msgParamVo.setRegistNo(padPayMainVo.getRegistNo());
				msgParamVo.setLicenseNo(prpLRegistVo.getPrpLRegistExt().getLicenseNo());
				msgParamVo.setDamageTime(prpLRegistVo.getDamageTime());
				msgParamVo.setSumAmt(padPayPersonVo.getCostSum().toString());
				msgParamVo.setLinkerName(customVo.getPayeeName());
				// 主险金额
				msgParamVo.setPrpCItemKinds(prpCItemKinds);
				SysMsgModelVo msgModelVo = null;
				msgModelVo = this.findSysMsgModelVo(prpLRegistVo,CodeConstants.CompensateFlag.padPay);
				if(msgModelVo != null){
					String message = sendMsgService.getMessage(msgModelVo.getContent(), msgParamVo);
					jlinkAccountVo.setMessageContent(message);// 短信
					}
				jlinkAccountVo.setAbstractContent(padPayPersonVo.getSummary());
				// jlinkAccountVo.setAbstractContent(prpLRegistVo.getPrpLRegistExt().getLicenseNo()+"赔款");//摘要
				if(StringUtils.isNotBlank(customVo.getPurpose())){
					jlinkAccountVo.setUsage(customVo.getPurpose());// 用途
				}else{
					jlinkAccountVo.setUsage("");// 用途
				}
				if(StringUtils.isNotBlank(customVo.getRemark())){
					jlinkAccountVo.setOtherRemark(customVo.getRemark());// 备注
                }else{
					jlinkAccountVo.setOtherRemark("");// 备注
                }
				jfeeVo.setJlinkAccountVo(jlinkAccountVo);
			/*	String message = "待补充";
				jlinkAccountVo.setMessageContent(message);//短信内容
	*/			
				jfeeList.add(jfeeVo);
			}
			jPlanMainVo.setJFeeVos(jfeeList);
	
			JPlanReturnVo returnVo = callPaymentDetailWebService.callPaymentForClient(jPlanMainVo,BusinessType.Payment_padPay,padPayMainVo.getCompensateNo(),"1");
			// 送收付成功后回写垫付PAYSTATUS字段为2-表示送收付成功
			if(returnVo.isResponseCode()){
				if(padPayMainVo.getPrpLPadPayPersons()!=null&&padPayMainVo.getPrpLPadPayPersons().size()>0){
					for(PrpLPadPayPersonVo PadPayPersonVo:padPayMainVo.getPrpLPadPayPersons()){
						if(BigDecimal.ZERO.compareTo(PadPayPersonVo.getCostSum())!=0){
							PadPayPersonVo.setPayStatus("2");
							this.savePrplPayHis(padPayMainVo.getClaimNo(),padPayMainVo.getCompensateNo(), PadPayPersonVo.getId(),"2","D");
						}else{
							this.savePrplPayHis(padPayMainVo.getClaimNo(),padPayMainVo.getCompensateNo(), PadPayPersonVo.getId(),"0","D");
						}
					}
				}
				padPayPubService.updatePadPay(padPayMainVo);
			}
			}
	}

	
	@Override
	public void compensateToPayment(PrpLCompensateVo compensateVo) throws Exception {
	    
		logger.info("compensateNo:"+compensateVo.getCompensateNo());
//		compensateVo.setUnderwriteDate(new Date());
		if(BigDecimal.ZERO.compareTo(DataUtils.NullToZero(compensateVo.getSumPaidAmt()))==0 && 
		   BigDecimal.ZERO.compareTo(DataUtils.NullToZero(compensateVo.getSumPaidFee()))==0){// 实付金额和费用 为0 不送收付
		}else{
			// List<PrpLLossItemVo> lossItemP6BVos = compensateService.getLosItemList(compensateVo.getCompensateNo(),"1");// 代付赔款
			// List<PrpLLossItemVo> lossItemP6EVos = compensateService.getLosItemList(compensateVo.getCompensateNo(),"2");// 清付
			List<PrpLLossItemVo> lossItemVos = compensateService.getLosItemList(compensateVo.getCompensateNo(),"3,4");// 赔款
			String coinsCode = policyViewService.findCoinsCode(compensateVo.getPolicyNo());
			PrpLRegistVo registVo = registQueryService.findByRegistNo(compensateVo.getRegistNo());
			PrpLCMainVo prplcMainVo = policyViewService.findPolicyInfoByPaltform(compensateVo.getRegistNo(), compensateVo.getPolicyNo());

			// 收付主数据
			JPlanMainVo jPlanMainVo = new JPlanMainVo();
			
			jPlanMainVo.setCertiType("C");
			jPlanMainVo.setCertiNo(compensateVo.getCompensateNo());
			jPlanMainVo.setPolicyNo(compensateVo.getPolicyNo());
			jPlanMainVo.setRegistNo(compensateVo.getRegistNo());
			jPlanMainVo.setClaimNo(compensateVo.getClaimNo());
			jPlanMainVo.setOperateCode(compensateVo.getCreateUser());
			jPlanMainVo.setOperateComCode(compensateVo.getComCode());
			jPlanMainVo.setPayComCode(compensateVo.getComCode());
			if("1101".equals(prplcMainVo.getRiskCode())) {
				if(StringUtils.isNotBlank(prplcMainVo.getIsTwentyFlag())) {
					jPlanMainVo.setIsTwentyFlag(prplcMainVo.getIsTwentyFlag()); // 2020条款交强险标志
				}else {
					jPlanMainVo.setIsTwentyFlag("0"); // 2020条款交强险标志
				}
			}
			
			List<JFeeVo> jFeeList = new ArrayList<JFeeVo>();
			
			// 收款明细--细分险别
			Map<String,BigDecimal> kindPaidMap = new HashMap<String, BigDecimal>();
			BigDecimal sumKindPaid = BigDecimal.ZERO;
			Map<String,BigDecimal> dwPaidMap = new HashMap<String,BigDecimal>();// 1 追偿 2代位
			String firstKindCode ="";
			if(lossItemVos!=null&&lossItemVos.size()>0){// 车赔款险别明细
				for(PrpLLossItemVo itemVo:lossItemVos){
					if(BigDecimal.ZERO.compareTo(itemVo.getSumRealPay())==0){
						continue;// 赔款金额为0时不组织该数据
					}
					
					if(CodeConstants.PayFlagType.INSTEAD_PAY.equals(itemVo.getPayFlag()) 
							|| CodeConstants.PayFlagType.CLEAR_PAY.equals(itemVo.getPayFlag())){
						
						BigDecimal realPay = itemVo.getSumRealPay().subtract(DataUtils.NullToZero(itemVo.getOffPreAmt()));
						BigDecimal kindPaid = BigDecimal.ZERO;
						if(dwPaidMap.containsKey(itemVo.getPayFlag())){
							kindPaid = dwPaidMap.get(itemVo.getPayFlag());
						}
						dwPaidMap.put(itemVo.getPayFlag(), kindPaid.add(realPay));
					}else{
						if(StringUtils.isBlank(firstKindCode)){
							firstKindCode = itemVo.getKindCode();
						}
						BigDecimal realPay = itemVo.getSumRealPay().subtract(DataUtils.NullToZero(itemVo.getOffPreAmt()));
						BigDecimal kindPaid = BigDecimal.ZERO;
						if(kindPaidMap.containsKey(itemVo.getKindCode())) {
							kindPaid = kindPaidMap.get(itemVo.getKindCode());
						}
						kindPaidMap.put(itemVo.getKindCode(), kindPaid.add(realPay));
						sumKindPaid = sumKindPaid.add(realPay);
					}
				}
			}
			
			if(compensateVo.getPrpLLossProps()!=null&&compensateVo.getPrpLLossProps().size()>0){// 财产赔款险别明细
				for(PrpLLossPropVo lossPropVo:compensateVo.getPrpLLossProps()){
					if(BigDecimal.ZERO.compareTo(lossPropVo.getSumRealPay())==0){
						continue;// 赔款金额为0时不组织该数据
					}
					if(StringUtils.isBlank(firstKindCode)){
						firstKindCode = lossPropVo.getKindCode();
					}
					BigDecimal realPay = lossPropVo.getSumRealPay().subtract(DataUtils.NullToZero(lossPropVo.getOffPreAmt()));
					BigDecimal kindPaid = BigDecimal.ZERO;
					if(kindPaidMap.containsKey(lossPropVo.getKindCode())){
						kindPaid = kindPaidMap.get(lossPropVo.getKindCode());
					}
					kindPaidMap.put(lossPropVo.getKindCode(), kindPaid.add(realPay));
					sumKindPaid = sumKindPaid.add(realPay);
				}
			}
			if(compensateVo.getPrpLLossPersons()!=null&&compensateVo.getPrpLLossPersons().size()>0){// 人伤赔款险别明细
				for(PrpLLossPersonVo lossPersonVo:compensateVo.getPrpLLossPersons()){
					if(BigDecimal.ZERO.compareTo(lossPersonVo.getSumRealPay())==0){
						continue;// 赔款金额为0时不组织该数据
					}
					if(StringUtils.isBlank(firstKindCode)){
						firstKindCode = lossPersonVo.getKindCode();
					}
					BigDecimal realPay = lossPersonVo.getSumRealPay().subtract(DataUtils.NullToZero(lossPersonVo.getOffPreAmt()));
					BigDecimal kindPaid = BigDecimal.ZERO;
					if(kindPaidMap.containsKey(lossPersonVo.getKindCode())) {
						kindPaid = kindPaidMap.get(lossPersonVo.getKindCode());
					}
					kindPaidMap.put(lossPersonVo.getKindCode(), kindPaid.add(realPay));
					sumKindPaid = sumKindPaid.add(realPay);
				}
			}
			
			// 根据收款人组织数据
			List<PrpLPaymentVo> padPayMentList = new ArrayList<PrpLPaymentVo>();
			for(PrpLPaymentVo paymentVo:compensateVo.getPrpLPayments()){
				if(paymentVo.getSumRealPay().doubleValue()!=0d){
					padPayMentList.add(paymentVo);
				}
			}	
		
			for(PrpLPaymentVo paymentVo : padPayMentList){
				String voucherNo2 = "";
				JFeeVo feeVo = new JFeeVo();
				// 收付主数据
				JplanFeeVo jplanFeeVo = new JplanFeeVo();
				jplanFeeVo.setSerialNo(Integer.parseInt(paymentVo.getSerialNo()));
				if(PayFlagType.COMPENSATE_PAY.equals(paymentVo.getPayFlag())){// 自付
					jplanFeeVo.setPayRefReason("P60");
				}else if(PayFlagType.CLEAR_PAY.equals(paymentVo.getPayFlag())){// 清付
					jplanFeeVo.setPayRefReason("P6D");
					// 获取青笋结算码
					if (StringUtils.isNotBlank(paymentVo.getItemId())) {
						voucherNo2 = getClearRecoveryCode(Integer.parseInt(paymentVo.getItemId()),compensateVo);
					}
				}else if(PayFlagType.INSTEAD_PAY.equals(paymentVo.getPayFlag())){// 代付
					jplanFeeVo.setPayRefReason("P6B");
					// 组织平台结算码
					voucherNo2 = getPlatformRecoveryCode(compensateVo, PayFlagType.INSTEAD_PAY);
					if(StringUtils.isBlank(voucherNo2)){
						voucherNo2 = "无";// 非机动车代位时 无结算码 无锁定platLockVo 此时传无
					}
				}
				jplanFeeVo.setCurrency("CNY");
				jplanFeeVo.setUnderWriteDate(compensateVo.getUnderwriteDate());
				jplanFeeVo.setAppliCode(prplcMainVo.getAppliCode());
				jplanFeeVo.setAppliName(prplcMainVo.getAppliName());
				jplanFeeVo.setVoucherNo2(voucherNo2);
				jplanFeeVo.setPlanFee(DataUtils.NullToZero(paymentVo.getSumRealPay()).doubleValue());
				if("2".equals(prplcMainVo.getCoinsFlag()) || "4".equals(prplcMainVo.getCoinsFlag())){
					jplanFeeVo.setCoinsCode(coinsCode);
					jplanFeeVo.setCoinsType("1");//我方
				}
				List<JplanFeeDetailVo> feeDetailList = new ArrayList<JplanFeeDetailVo>();
				if(CodeConstants.PayFlagType.INSTEAD_PAY.equals(paymentVo.getPayFlag()) 
						|| CodeConstants.PayFlagType.CLEAR_PAY.equals(paymentVo.getPayFlag())){
					JplanFeeDetailVo feeDetailVo = new JplanFeeDetailVo();
					String kindCode = CodeConstants.KINDCODE.KINDCODE_A;// 代位默认是车损险
					if(CodeConstants.PayFlagType.CLEAR_PAY.equals(paymentVo.getPayFlag())){
						if(Risk.isDQZ(compensateVo.getRiskCode())){
							kindCode = CodeConstants.KINDCODE.KINDCODE_BZ;
						}else{
							kindCode = CodeConstants.KINDCODE.KINDCODE_B;
						}
					}
					
					feeDetailVo.setKindCode(kindCode);
					feeDetailVo.setPlanfee(paymentVo.getSumRealPay().doubleValue());
					feeDetailList.add(feeDetailVo);
				}else{
					BigDecimal kindAddPaid = BigDecimal.ZERO;
					for (Map.Entry<String, BigDecimal> entry : kindPaidMap.entrySet()) {
						String kindCode= (String)entry.getKey(); 
						if(!firstKindCode.equals(kindCode)){
							BigDecimal kindPaid = (BigDecimal)entry.getValue();
							BigDecimal thisKindPiad = paymentVo.getSumRealPay().multiply(kindPaid).divide(sumKindPaid, 2,BigDecimal.ROUND_HALF_UP);
							JplanFeeDetailVo feeDetailVo = new JplanFeeDetailVo();
							feeDetailVo.setKindCode(kindCode);
							feeDetailVo.setPlanfee(thisKindPiad.doubleValue());
							feeDetailList.add(feeDetailVo);
							
							// //加入到已组织的中
							kindAddPaid = kindAddPaid.add(thisKindPiad);
						}
					 }
					// 最后一次 用减法 防止尾差 或只有一个
					BigDecimal thisKindPiad = paymentVo.getSumRealPay().subtract(kindAddPaid);
					JplanFeeDetailVo feeDetailVo = new JplanFeeDetailVo();
					feeDetailVo.setKindCode(firstKindCode);
					feeDetailVo.setPlanfee(thisKindPiad.doubleValue());
					feeDetailList.add(feeDetailVo);
				}
				jplanFeeVo.setJplanFeeDetailVos(feeDetailList);
				feeVo.setJplanFeeVo(jplanFeeVo);
				
				// 收款直付帐号明细数据
				JlinkAccountVo jlinkAccountVo = new JlinkAccountVo();// 收款支付账号明细数据
				PrpLPayCustomVo customVo = managerService.findPayCustomVoById(paymentVo.getPayeeId());
				if(PayFlagType.COMPENSATE_PAY.equals(paymentVo.getPayFlag())){// 自付
					jlinkAccountVo.setPayRefReason("P60");
				}else if(PayFlagType.CLEAR_PAY.equals(paymentVo.getPayFlag())){// 清付
					jlinkAccountVo.setPayRefReason("P6D");
				}else if(PayFlagType.INSTEAD_PAY.equals(paymentVo.getPayFlag())){// 代付/追偿
					jlinkAccountVo.setPayRefReason("P6B");
				}
				
				jlinkAccountVo.setPlanFee(paymentVo.getSumRealPay().doubleValue());
				jlinkAccountVo.setAccountNo(customVo.getAccountId());
				jlinkAccountVo.setPayReasonFlag(paymentVo.getOtherFlag());
				jlinkAccountVo.setPayReason(paymentVo.getOtherCause());
				jlinkAccountVo.setTelephone(customVo.getPayeeMobile());
				// jlinkAccountVo.setPayObject(payObject);//支付对象
				// jlinkAccountVo.setSubPayObject(subPayObject);//支付子对象
				//  摘要待调整
				String privateFlag = customVo.getPublicAndPrivate();
				jlinkAccountVo.setPublicPrivateFlag(privateFlag==null ? "0" : privateFlag);// 公私标志
				
				//重开赔案不送资金，从共从联不送资金
		 		String isAutoPay = "1";
				PrpLClaimVo prpLClaimVo = claimService.findClaimVoByClaimNo(compensateVo.getClaimNo());
				if(prpLClaimVo != null){
					Map<String,String> queryMap = new HashMap<String,String>();
					queryMap.put("registNo", compensateVo.getRegistNo());
					queryMap.put("claimNo", prpLClaimVo.getClaimNo());
					queryMap.put("checkStatus","6");// 审核通过
					// 查找审核通过的重开赔案 立案号列表
					List<PrpLReCaseVo> prpLReCaseVoList = endCasePubService.findReCaseVoListByqueryMap(queryMap);
					if(prpLReCaseVoList!=null&&prpLReCaseVoList.size()>0){// 重开
						isAutoPay = "0";// 不送资金
					}
				}
				if("0".equals(isAutoPay) || "2".equals(prplcMainVo.getCoinsFlag()) || "4".equals(prplcMainVo.getCoinsFlag())){//不送
					jlinkAccountVo.setIsAutoPay("0");//是否推送资金系统  
					jlinkAccountVo.setIsFastReparation("0");//是否快赔
				}else{
					jlinkAccountVo.setIsAutoPay(compensateVo.getPrpLCompensateExt().getIsAutoPay());// 是否推送资金系统
					jlinkAccountVo.setIsFastReparation(compensateVo.getPrpLCompensateExt().getIsFastReparation());// 是否快赔
				}
				jlinkAccountVo.setIsExpress("0");// 是否加急（0：否1：是）
				// 资金二期修改，重开赔案不发送短信通知 start
				if("1".equals(isAutoPay)){
					// 短信发送
					PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(compensateVo.getRegistNo());
					List<PrpLCItemKindVo> prpCItemKinds = registQueryService.findCItemKindListByRegistNo(compensateVo.getRegistNo());
					SendMsgParamVo msgParamVo = new SendMsgParamVo();
					// 主险金额
					msgParamVo.setPrpCItemKinds(prpCItemKinds);
					msgParamVo.setRegistNo(compensateVo.getRegistNo());
					msgParamVo.setLicenseNo(prpLRegistVo.getPrpLRegistExt().getLicenseNo());
					msgParamVo.setDamageTime(prpLRegistVo.getDamageTime());
					msgParamVo.setSumAmt(paymentVo.getSumRealPay().toString());
					msgParamVo.setLinkerName(customVo.getPayeeName());
					SysMsgModelVo msgModelVo = null;
					msgModelVo = this.findSysMsgModelVo(prpLRegistVo,CodeConstants.CompensateFlag.compensate);
					if(msgModelVo != null){
						String message = sendMsgService.getMessage(msgModelVo.getContent(), msgParamVo);
						
/*****************************送收付之前，先将结案短信存入短信表Prpsmsmessage******************************************/
						Date sendTime_7 = sendMsgService.getSendTime(msgModelVo.getTimeType());
						Date trueSendTime = new Date();// 真实发送时间
						Date nowTime = new Date();
						Date sendTime1=null;
						if(sendTime_7!=null){
							sendTime1 = DateUtils.addMinutes(sendTime_7, -5);// 短信平台发送时间
						   if(nowTime.getTime()<sendTime1.getTime()){
							    trueSendTime=sendTime1;
							}
						}
						
						PrpsmsMessageVo prpsmsMessageVo=null;
						if(msgParamVo != null){
							prpsmsMessageVo=new PrpsmsMessageVo();
							prpsmsMessageVo.setBusinessNo(msgParamVo.getRegistNo());
							prpsmsMessageVo.setComCode(prpLRegistVo.getComCode());
							prpsmsMessageVo.setCreateTime(nowTime);
							prpsmsMessageVo.setPhoneCode(prpLRegistVo.getReportorPhone());
							prpsmsMessageVo.setSendNodecode(FlowNode.EndCas.toString());
							prpsmsMessageVo.setSendText(message);
							prpsmsMessageVo.setTruesendTime(trueSendTime);
							prpsmsMessageVo.setUserCode(prpLRegistVo.getCreateUser());
							prpsmsMessageVo.setBackTime(nowTime);
							prpsmsMessageVo.setStatus("0");  //这时候保存的短信全部为推送失败，然后在收付回写报文中，再调整状态为1
							msgModelService.saveorUpdatePrpSmsMessage(prpsmsMessageVo);
						}
/*****************************送收付之前，先将结案短信存入短信表Prpsmsmessage**********************************************/						

						jlinkAccountVo.setMessageContent(message);// 短信
						}
				}
				// 资金二期修改，重开赔案不发送短信通知 end
				/*if(compensateVo.getLawsuitFlag().equals("1")){
					jlinkAccountVo.setAbstractContent(customVo.getSummary());//摘要
				}else{
					jlinkAccountVo.setAbstractContent(prpLRegistVo.getPrpLRegistExt().getLicenseNo()+"赔款");//摘要
				}*/
				if(StringUtils.isNotBlank(paymentVo.getSummary())){
					jlinkAccountVo.setAbstractContent(paymentVo.getSummary());// 摘要
				}else{
					jlinkAccountVo.setAbstractContent("");// 摘要
				}
				
				jlinkAccountVo.setUsage(customVo.getPurpose());// 用途
				jlinkAccountVo.setOtherRemark(customVo.getRemark());// 备注
				/*String message = "待补充";
				jlinkAccountVo.setMessageContent(message);//短信内容
				*/				
				// if("1".equals(customVo.getPayObjectType())){// 收款人性质，1-个人，2-机构
//					jlinkAccountVo.setCentiType("01");
//				}else{
//					jlinkAccountVo.setCentiType("71");
//				}
				jlinkAccountVo.setCentiType(customVo.getCertifyType());
				jlinkAccountVo.setCentiCode(customVo.getCertifyNo());
				feeVo.setJlinkAccountVo(jlinkAccountVo);
				
				jFeeList.add(feeVo);
			}
			
			// 合并相同的费用和收款人
			Map<String,PrpLPayCustomVo> chargeMap = new HashMap<String,PrpLPayCustomVo>();
			Map<String,PrpLPayCustomVo> chargeMap1 = new HashMap<String,PrpLPayCustomVo>();
			String paymentFlags = "1";// 1送收付，0不送
			for(PrpLChargeVo chargeVo:compensateVo.getPrpLCharges()){
				String key = chargeVo.getPayeeId()+chargeVo.getChargeCode()+chargeVo.getSummary();
//				String key1 = chargeVo.getPayeeId()+chargeVo.getChargeCode();
				if(chargeMap.containsKey(key)){// 相同
					// 后续有修改待定
					// 分两种情况，摘要相同送，不同不送
					Double sumAmt = DataUtils.NullToZero(chargeVo.getFeeRealAmt()).doubleValue();
							//-DataUtils.NullToZero(chargeVo.getOffPreAmt()).doubleValue()
					PrpLPayCustomVo payCustomVo = chargeMap.get(key);
					payCustomVo.setSumAmt(sumAmt+payCustomVo.getSumAmt());
					payCustomVo.getPrpLChargeVos().add(chargeVo);
					Integer serialNo = Integer.parseInt(chargeVo.getSerialNo());
					Integer oldSerialNo = Integer.parseInt(payCustomVo.getSerialNo());
					if(oldSerialNo>serialNo){// 取最小的serialno 保持送收付和送发票的serialno一致
						payCustomVo.setSerialNo(chargeVo.getSerialNo());
					}
					
					chargeMap.put(key,payCustomVo);
//					chargeMap1.put(key1,payCustomVo);
				}
				// else if(chargeMap1.containsKey(key1)){//不同
//				    paymentFlags = "0";
				// //throw new IllegalArgumentException("费用和收款人相同，摘要不同不送收付");
//				}
				else{
					PrpLPayCustomVo customVo = managerService.findPayCustomVoById(chargeVo.getPayeeId());
					SysCodeDictVo dictVo = codeTranService.findTransCodeDictVo("ChargeCode",chargeVo.getChargeCode());
					
					Double sumAmt = DataUtils.NullToZero(chargeVo.getFeeRealAmt()).doubleValue();
					customVo.setSumAmt(sumAmt);
					customVo.setSerialNo(chargeVo.getSerialNo());
					customVo.setPayRefReason(dictVo.getProperty2());
					customVo.getPrpLChargeVos().add(chargeVo);
					chargeMap.put(key,customVo);
//					chargeMap1.put(key1,customVo);
				}
			}
			for(PrpLPayCustomVo customVo:chargeMap.values()){
				// 费用收款主数据
				if(customVo.getSumAmt()!=0d){
					JFeeVo feeVo = new JFeeVo();
					// 收付主数据
					JplanFeeVo jplanFeeChargeVo = new JplanFeeVo();
					
					jplanFeeChargeVo.setPayRefReason(customVo.getPayRefReason());// 实赔使用property2
					jplanFeeChargeVo.setCurrency("CNY");
					jplanFeeChargeVo.setPlanFee(customVo.getSumAmt());
					jplanFeeChargeVo.setUnderWriteDate(compensateVo.getUnderwriteDate());
					jplanFeeChargeVo.setAppliCode(prplcMainVo.getAppliCode());
					jplanFeeChargeVo.setAppliName(prplcMainVo.getAppliName());
					jplanFeeChargeVo.setSerialNo(Integer.parseInt(customVo.getSerialNo()));
					if("2".equals(prplcMainVo.getCoinsFlag()) || "4".equals(prplcMainVo.getCoinsFlag())){
						jplanFeeChargeVo.setCoinsCode(coinsCode);
						jplanFeeChargeVo.setCoinsType("1");//我方
					}
					
					// 收付明细
					List<JplanFeeDetailVo> detailChargeVos = new ArrayList<JplanFeeDetailVo>();
					for(PrpLChargeVo chargeVo:customVo.getPrpLChargeVos()){
						JplanFeeDetailVo detailVo = new JplanFeeDetailVo();
						detailVo.setKindCode(chargeVo.getKindCode());
						Double sumAmt = DataUtils.NullToZero(chargeVo.getFeeRealAmt()).doubleValue();
						detailVo.setPlanfee(sumAmt);
						detailChargeVos.add(detailVo);
					}
					jplanFeeChargeVo.setJplanFeeDetailVos(detailChargeVos);
					
					feeVo.setJplanFeeVo(jplanFeeChargeVo);
					
					// 收款支付账号明细数据
					JlinkAccountVo jlinkAccountVo = new JlinkAccountVo();
					jlinkAccountVo.setPayRefReason(customVo.getPayRefReason());
					jlinkAccountVo.setPlanFee(customVo.getSumAmt());
					jlinkAccountVo.setAccountNo(customVo.getAccountId());
					jlinkAccountVo.setPayReasonFlag("0");// 费用没有收付例外标志和原因
					if("1".equals(customVo.getPayObjectType())){// 收款人性质，1-个人，2-机构
						jlinkAccountVo.setCentiType("01");
					}else{
						jlinkAccountVo.setCentiType("71");
					}
					jlinkAccountVo.setCentiCode(customVo.getCertifyNo());
					jlinkAccountVo.setTelephone(customVo.getPayeeMobile());
					
					// 资金二期修改，费用不送资金start
					jlinkAccountVo.setIsAutoPay("0");// 是否推送资金系统
					jlinkAccountVo.setIsFastReparation("0");// 是否快赔
	                String privateFlag = customVo.getPublicAndPrivate();
					jlinkAccountVo.setPublicPrivateFlag(privateFlag==null ? "0" : privateFlag);// 公私标志
					// 待定
	                if(customVo.getPrpLChargeVos() != null && customVo.getPrpLChargeVos().size()>0){
						jlinkAccountVo.setAbstractContent(customVo.getPrpLChargeVos().get(0).getSummary());// 摘要
	                }
	                jlinkAccountVo.setIsExpress("0");
	                if(StringUtils.isNotBlank(customVo.getPurpose())){
						jlinkAccountVo.setUsage(customVo.getPurpose());// 用途
                    }else{
						jlinkAccountVo.setUsage("");// 用途
                    }
                    if(StringUtils.isNotBlank(customVo.getPurpose())){
						jlinkAccountVo.setOtherRemark(customVo.getRemark());// 备注
                    }else{
						jlinkAccountVo.setOtherRemark("");// 备注
                    }
	                jlinkAccountVo.setMessageContent("");
					feeVo.setJlinkAccountVo(jlinkAccountVo);
					
					jFeeList.add(feeVo);
				}
			}
			jPlanMainVo.setJFeeVos(jFeeList);
			
			JPlanReturnVo returnVo = callPaymentDetailWebService.callPaymentForClient(jPlanMainVo,BusinessType.Payment_compe,compensateVo.getCompensateNo(),paymentFlags);
			// 送收付成功后回写理算PAYSTATUS字段为2-表示送收付成功
			if(returnVo.isResponseCode() && "1".equals(paymentFlags)){
				if(compensateVo.getPrpLPayments()!=null&&compensateVo.getPrpLPayments().size()>0){
					for(PrpLPaymentVo paymentVo : compensateVo.getPrpLPayments()){
						if(BigDecimal.ZERO.compareTo(paymentVo.getSumRealPay())!=0){
							paymentVo.setPayStatus("2");
							compensateService.updatePrpLPaymentVo(paymentVo);
							this.savePrplPayHis(compensateVo.getClaimNo(),compensateVo.getCompensateNo(), paymentVo.getId(),"2","P");
						}else{
							this.savePrplPayHis(compensateVo.getClaimNo(),compensateVo.getCompensateNo(), paymentVo.getId(),"0","P");
						}
					}
				}
				if(compensateVo.getPrpLCharges()!=null&&compensateVo.getPrpLCharges().size()>0){
					for(PrpLChargeVo chargeVo:compensateVo.getPrpLCharges()){
						if(BigDecimal.ZERO.compareTo(chargeVo.getFeeRealAmt())==-1){
							chargeVo.setPayStatus("2");
							compensateService.updatePrpLCharges(chargeVo);
							this.savePrplPayHis(compensateVo.getClaimNo(),compensateVo.getCompensateNo(), chargeVo.getId(),"2","F");
						}else{
							this.savePrplPayHis(compensateVo.getClaimNo(),compensateVo.getCompensateNo(), chargeVo.getId(),"0","F");
						}
					}
				}
			}
		
		}
		
	}	
	
	@Override
	public void updatePrePayToPayment(PrpLCompensateVo compensateVo) throws Exception{
		// 判断有无预付任务
		FlowNode nodeCode ;
		if("1101".equals(compensateVo.getRiskCode())){
			nodeCode = FlowNode.PrePayCI;
		}else{
			nodeCode = FlowNode.PrePayBI;
		}
		Boolean existPrePay = wfTaskHandleService.existTaskByNodeCode(compensateVo.getRegistNo(), nodeCode, compensateVo.getClaimNo(), "1");
		// 判断是否重开结案
		List<PrpLReCaseVo> prpLReCaseVoList = reOpenCaseService.findReCaseByClaimNo(compensateVo.getClaimNo());
		Boolean existReCase = false;
		if(prpLReCaseVoList != null && prpLReCaseVoList.size() >0){
			existReCase = true;
		}
		// 有预付任务，且不是重开结案，调预付数据更新接口
		if(existPrePay && !existReCase){
			String SVR_URL = SpringProperties.getProperty("PAYMENT_SVR_URL");
			if(SVR_URL==null || SVR_URL.isEmpty()){
				logger.warn("未配置收付地址，不调用收付接口。");
				throw new Exception("未配置收付服务地址。");
			}
			SVR_URL = SVR_URL+"/service/claimBackPrepay";// 收付地址用同一个，调用不同的服务再拼接 改sys_config表的配置数据
//			SVR_URL="http://10.236.0.215:8080/payment/service/claimBackPrepay";
			// 组织报文
			ClaimMainVo claimMainVo = new ClaimMainVo();
			claimMainVo.setCompensateNo(compensateVo.getCompensateNo());
			claimMainVo.setClaimNo(compensateVo.getClaimNo());
			claimMainVo.setRegistNo(compensateVo.getRegistNo());
			claimMainVo.setPolicyNo(compensateVo.getPolicyNo());
			claimMainVo.setUnderWriteEndDate(compensateVo.getUnderwriteDate());
			claimMainVo.setRiskCode(compensateVo.getRiskCode());
			claimMainVo.setComCode(compensateVo.getComCode());
			claimMainVo.setOperatorCode(compensateVo.getUnderwriteUser());
			PrpLEndCaseVo prpLEndCaseVo = endCaseService.queryEndCaseVo(compensateVo.getRegistNo(), compensateVo.getClaimNo());
			if(prpLEndCaseVo != null && prpLEndCaseVo.getEndCaseDate() != null){
				claimMainVo.setEndCaseDate(prpLEndCaseVo.getEndCaseDate());
			}else{
				claimMainVo.setEndCaseDate(new Date());
			}
			
			// 发送报文
			ClaimBackPrepayServiceLocator backPrepayService = new ClaimBackPrepayServiceLocator();
			ClaimBackPrepayPortBindingStub stub = new ClaimBackPrepayPortBindingStub(new java.net.URL(SVR_URL), backPrepayService);
			XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
			stream.autodetectAnnotations(true);
			stream.setMode(XStream.NO_REFERENCES);
			stream.aliasSystemAttribute(null,"class");
			String requestXml = stream.toXML(claimMainVo);
			
			logger.info("预付数据更新接口的报文---->"+requestXml);
			String responseXml = stub.backPrepayXml(requestXml);
			stream.processAnnotations(ClaimMainReturnVo.class);
			
			logger.info("预付数据更新接口返回的报文---->"+responseXml);
			ClaimMainReturnVo claimMainReturnVo = (ClaimMainReturnVo)stream.fromXML(responseXml);
			
			// 保存日志
			ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
			logVo.setRegistNo(compensateVo.getRegistNo());
			logVo.setBusinessType(BusinessType.Payment_backPrePay.name());
			logVo.setBusinessName(BusinessType.Payment_backPrePay.getName());
			logVo.setOperateNode(FlowNode.EndCas.name());
			logVo.setComCode(compensateVo.getComCode());
			logVo.setClaimNo(compensateVo.getClaimNo());
			logVo.setCompensateNo(compensateVo.getCompensateNo());
			if(claimMainReturnVo.isResponseCode()){
				logVo.setStatus("1");
				logVo.setErrorCode("true");
			}else{
				logVo.setStatus("0");
				logVo.setErrorCode("false");
			}
			logVo.setRequestTime(new Date());
			logVo.setRequestUrl(SVR_URL);
			logVo.setRequestXml(requestXml);
			logVo.setResponseXml(responseXml);
			logVo.setErrorMessage(claimMainReturnVo.getErrorMessage());
			logVo.setCreateTime(new Date());
			logVo.setCreateUser(compensateVo.getUnderwriteUser());
			
			logService.save(logVo);
		}
	}
	
	public SysMsgModelVo findSysMsgModelVo(PrpLRegistVo prpLRegistVo,String compensateFlag){
		SysMsgModelVo msgModelVo = null;
		String systemNode = null;
		
		if(CodeConstants.CompensateFlag.prePay.equals(compensateFlag)){
			systemNode = CodeConstants.SystemNode.prePay;
		}else if(CodeConstants.CompensateFlag.padPay.equals(compensateFlag)){
			systemNode = CodeConstants.SystemNode.padPay;
		}else{
			systemNode = CodeConstants.SystemNode.endCase;
		}
		msgModelVo = sendMsgService.findmsgModelVo(CodeConstants.ModelType.payee,systemNode,prpLRegistVo.getComCode(),CodeConstants.CaseType.normal);
		return msgModelVo;
	}
	
	public void savePrplPayHis(String claimNo,String compensateNo,Long id,String flags,String hisType){
		Date inputTime = new Date();//送收付取当前时间
		compensateService.savePrplPayHis(claimNo,compensateNo, id,flags,hisType,inputTime);
	}
	
	// 组织平台结算码
	private String getPlatformRecoveryCode(PrpLCompensateVo compensateVo, String payFlagType) {
		String voucherNo2 = "";
		List<PrpLPlatLockVo> lockList = platLockService
				.findPrpLPlatLockVoList(compensateVo.getRegistNo(),compensateVo.getPolicyNo(),payFlagType);
		if (lockList != null && !lockList.isEmpty()) {
			for (PrpLPlatLockVo lock : lockList) {
				List<PrpLRecoveryOrPayVo> payVoList = lock.getPrpLRecoveryOrPays();
				if (payVoList != null && !payVoList.isEmpty()) {
					for (PrpLRecoveryOrPayVo payVo : payVoList) {
						if (payVo.getCompensateNo().equals(compensateVo.getCompensateNo())) {
							voucherNo2 = payVo.getRecoveryCode();
						}
					}
				}
			}
		}
		return voucherNo2;
	}
	
	// 获取清算结算码
	private String getClearRecoveryCode(Integer itemId,PrpLCompensateVo compensateVo) {
		String voucherNo2 = "";
		PrpLCheckDutyVo dutyVo = checkTaskService.findCheckDuty(compensateVo.getRegistNo(),itemId);
		if(dutyVo!=null){
			PrpLPlatLockVo lockVo = platLockService.findPrpLPlatLockVoByLicenseNo(compensateVo.getRegistNo(),
					compensateVo.getPolicyNo(),PayFlagType.CLEAR_PAY,dutyVo.getLicenseNo());
			if(lockVo!=null){
				voucherNo2 = lockVo.getRecoveryCode();
			}
		}
		return voucherNo2;
	}
	
	/**
	 * 公估费送收付
	 */
	@Override
	public void assessorToPayment(PrpLAssessorMainVo assessorMainVo,PrpLAssessorFeeVo assessorFeeVo) throws Exception {
		
			JPlanMainVo jPlanMainVo = new JPlanMainVo();
			logger.debug("registNo:"+assessorFeeVo.getRegistNo());
			String policyType = "BZ".equals(assessorFeeVo.getKindCode()) ? "11" : "12";
			String makComCode = policyViewService.findPolicyComCode(assessorFeeVo.getRegistNo(),policyType);
			PrpLCMainVo prplcMainVo = policyViewService.findPolicyInfoByPaltform(assessorFeeVo.getRegistNo(), assessorFeeVo.getPolicyNo());
			
			String comCode = assessorMainVo.getComCode();
			if(StringUtils.isBlank(comCode)){
				comCode = ServiceUserUtils.getComCode();
			}
			
			PrpdIntermMainVo intermMainVo =managerService.findIntermById(assessorMainVo.getIntermId());
			if(intermMainVo==null){
				intermMainVo=managerService.findIntermByCode(assessorMainVo.getIntermcode(), comCode);
			}else if(intermMainVo!=null && intermMainVo.getId()==null){
				intermMainVo=managerService.findIntermByCode(assessorMainVo.getIntermcode(), comCode);
			}else{
				
			}
			
			PrpdIntermBankVo bankVo = intermMainVo.getPrpdIntermBank();
			
			jPlanMainVo.setCertiType("C");
			jPlanMainVo.setCertiNo(assessorFeeVo.getCompensateNo());
			jPlanMainVo.setPolicyNo(assessorFeeVo.getPolicyNo());
			jPlanMainVo.setRegistNo(assessorFeeVo.getRegistNo());
			jPlanMainVo.setClaimNo(assessorFeeVo.getClaimNo());
			jPlanMainVo.setOperateCode(assessorMainVo.getUpdateUser());
			jPlanMainVo.setOperateComCode(makComCode);
			jPlanMainVo.setPayComCode(makComCode);
			if("1101".equals(prplcMainVo.getRiskCode())) {
				if(StringUtils.isNotBlank(prplcMainVo.getIsTwentyFlag())) {
					jPlanMainVo.setIsTwentyFlag(prplcMainVo.getIsTwentyFlag()); // 2020条款交强险标志
				}else {
					jPlanMainVo.setIsTwentyFlag("0"); // 2020条款交强险标志
				}
			}
			
			List<JFeeVo> jFeeVos=new ArrayList<JFeeVo>();
			//List<JplanFeeVo> jPlanFeeVos = new ArrayList<JplanFeeVo>();
			JFeeVo jFeeVo=new JFeeVo();
		JplanFeeVo jPlanFeeVo = new JplanFeeVo();// 收付明细
			
			jPlanFeeVo.setPayRefReason("P67");
			jPlanFeeVo.setCurrency("CNY");
			jPlanFeeVo.setPlanFee(assessorFeeVo.getAmount().doubleValue());
			jPlanFeeVo.setUnderWriteDate(new Date());
			jPlanFeeVo.setSerialNo(0);
			jPlanFeeVo.setAppliCode(assessorMainVo.getIntermcode());
			jPlanFeeVo.setAppliName(assessorMainVo.getIntermname());

			
			List<JplanFeeDetailVo> detailVos = new ArrayList<JplanFeeDetailVo>();
			JplanFeeDetailVo detailVo = new JplanFeeDetailVo();
			detailVo.setKindCode(assessorFeeVo.getKindCode());
			detailVo.setPlanfee(assessorFeeVo.getAmount().doubleValue());
			detailVos.add(detailVo);
			jPlanFeeVo.setJplanFeeDetailVos(detailVos);
			jFeeVo.setJplanFeeVo(jPlanFeeVo);
			
		// 收款直付帐号明细数据
		JlinkAccountVo jlinkAccountVo = new JlinkAccountVo();// 收款支付账号明细数据
			jlinkAccountVo.setPayRefReason("P67");
			jlinkAccountVo.setPlanFee(assessorFeeVo.getAmount().doubleValue());
			jlinkAccountVo.setAccountNo(bankVo.getAccountId());
			jlinkAccountVo.setPayReasonFlag("0");
			jlinkAccountVo.setCentiType("71");
			jlinkAccountVo.setCentiCode(bankVo.getCertifyNo());
			if(StringUtils.isNotBlank(bankVo.getPublicAndPrivate())){
				jlinkAccountVo.setPublicPrivateFlag(bankVo.getPublicAndPrivate());
			}else{
				jlinkAccountVo.setPublicPrivateFlag("0");
			}
			if(StringUtils.isNotBlank(assessorFeeVo.getRemark())){
				jlinkAccountVo.setAbstractContent(assessorFeeVo.getRemark());
			}else{
			jlinkAccountVo.setAbstractContent("公估费");
			}
			jlinkAccountVo.setIsAutoPay("0");
			jlinkAccountVo.setIsExpress("0");
			jlinkAccountVo.setIsFastReparation("0");
			jlinkAccountVo.setTelephone(bankVo.getMobile());
			
			jFeeVo.setJlinkAccountVo(jlinkAccountVo);
			jFeeVos.add(jFeeVo);
            jPlanMainVo.setJFeeVos(jFeeVos);
			callPaymentDetailWebService.callPaymentForClient(jPlanMainVo,BusinessType.Payment_assessor,assessorFeeVo.getCompensateNo(), "1");
			
	}
	
	@Override
	public void checkFeeToPayment(PrpLAcheckMainVo checkMainVo,PrpLCheckFeeVo checkFeeVo) throws Exception {

		JPlanMainVo jPlanMainVo = new JPlanMainVo();
		logger.debug("registNo:"+checkFeeVo.getRegistNo());
		String policyType = "BZ".equals(checkFeeVo.getKindCode()) ? "11" : "12";
		String makComCode = policyViewService.findPolicyComCode(checkFeeVo.getRegistNo(),policyType);
		PrpLCMainVo prplcMainVo = policyViewService.findPolicyInfoByPaltform(checkFeeVo.getRegistNo(), checkFeeVo.getPolicyNo());
		
		String comCode = checkMainVo.getComCode();
		if(StringUtils.isBlank(comCode)){
			comCode = ServiceUserUtils.getComCode();
		}
		
		PrpdCheckBankMainVo checkBankMainVo =managerService.findCheckById(checkMainVo.getCheckmId());
		if(checkBankMainVo==null){
			checkBankMainVo=managerService.findCheckByCode(checkMainVo.getCheckcode(), comCode);
		}else if(checkBankMainVo!=null && checkBankMainVo.getId()==null){
			checkBankMainVo=managerService.findCheckByCode(checkMainVo.getCheckcode(), comCode);
		}else{
			
		}
		
		PrpdcheckBankVo bankVo = checkBankMainVo.getPrpdcheckBank();
		
		jPlanMainVo.setCertiType("C");
		jPlanMainVo.setCertiNo(checkFeeVo.getCompensateNo());
		jPlanMainVo.setPolicyNo(checkFeeVo.getPolicyNo());
		jPlanMainVo.setRegistNo(checkFeeVo.getRegistNo());
		jPlanMainVo.setClaimNo(checkFeeVo.getClaimNo());
		jPlanMainVo.setOperateCode(checkMainVo.getUpdateUser());
		jPlanMainVo.setOperateComCode(makComCode);
		jPlanMainVo.setPayComCode(makComCode);
		jPlanMainVo.setIsTwentyFlag(prplcMainVo.getIsTwentyFlag()); // 2020条款交强险标志
		if("1101".equals(prplcMainVo.getRiskCode())) {
			if(StringUtils.isNotBlank(prplcMainVo.getIsTwentyFlag())) {
				jPlanMainVo.setIsTwentyFlag(prplcMainVo.getIsTwentyFlag()); // 2020条款交强险标志
			}else {
				jPlanMainVo.setIsTwentyFlag("0"); // 2020条款交强险标志
			}
		}
		
		List<JFeeVo> jFeeVos=new ArrayList<JFeeVo>();
		//List<JplanFeeVo> jPlanFeeVos = new ArrayList<JplanFeeVo>();
		JFeeVo jFeeVo=new JFeeVo();
	    JplanFeeVo jPlanFeeVo = new JplanFeeVo();// 收付明细
		
		jPlanFeeVo.setPayRefReason(PayReason.CHECKFEE_PAY_Res);
		jPlanFeeVo.setCurrency("CNY");
		jPlanFeeVo.setPlanFee(checkFeeVo.getAmount().doubleValue());
		jPlanFeeVo.setUnderWriteDate(new Date());
		jPlanFeeVo.setSerialNo(0);
		jPlanFeeVo.setAppliCode(checkMainVo.getCheckcode());
		jPlanFeeVo.setAppliName(checkMainVo.getCheckname());

		
		List<JplanFeeDetailVo> detailVos = new ArrayList<JplanFeeDetailVo>();
		JplanFeeDetailVo detailVo = new JplanFeeDetailVo();
		detailVo.setKindCode(checkFeeVo.getKindCode());
		detailVo.setPlanfee(checkFeeVo.getAmount().doubleValue());
		detailVos.add(detailVo);
		jPlanFeeVo.setJplanFeeDetailVos(detailVos);
		jFeeVo.setJplanFeeVo(jPlanFeeVo);
		
	// 收款直付帐号明细数据
	JlinkAccountVo jlinkAccountVo = new JlinkAccountVo();// 收款支付账号明细数据
		jlinkAccountVo.setPayRefReason(PayReason.CHECKFEE_PAY_Res);
		jlinkAccountVo.setPlanFee(checkFeeVo.getAmount().doubleValue());
		jlinkAccountVo.setAccountNo(bankVo.getAccountId());
		jlinkAccountVo.setPayReasonFlag("0");
		jlinkAccountVo.setCentiType("71");
		jlinkAccountVo.setCentiCode(bankVo.getCertifyNo());
		if(StringUtils.isNotBlank(bankVo.getPublicAndPrivate())){
			jlinkAccountVo.setPublicPrivateFlag(bankVo.getPublicAndPrivate());
		}else{
			jlinkAccountVo.setPublicPrivateFlag("0");
		}
		if(StringUtils.isNotBlank(checkFeeVo.getRemark())){
			jlinkAccountVo.setAbstractContent(checkFeeVo.getRemark());
		}else{
		jlinkAccountVo.setAbstractContent("查勘费");
		}
		jlinkAccountVo.setIsAutoPay("0");
		jlinkAccountVo.setIsExpress("0");
		jlinkAccountVo.setIsFastReparation("0");
		jlinkAccountVo.setTelephone(bankVo.getMobile());
		
		jFeeVo.setJlinkAccountVo(jlinkAccountVo);
		jFeeVos.add(jFeeVo);
        jPlanMainVo.setJFeeVos(jFeeVos);
		callPaymentDetailWebService.callPaymentForClient(jPlanMainVo,BusinessType.Payment_checkFee,checkFeeVo.getCompensateNo(), "1");
		
		
	}
	
	
}
