/******************************************************************************
 * CREATETIME : 2016年7月16日 下午3:28:24
 ******************************************************************************/
package ins.sino.claimcar.payment.service;

import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.service.utils.ServiceUserUtils;
import ins.platform.utils.DataUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants.PayFlagType;
import ins.sino.claimcar.CodeConstants.PayReason;
import ins.sino.claimcar.bankaccount.service.BankAccountService;
import ins.sino.claimcar.carinterface.util.BusinessType;
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
import ins.sino.claimcar.claim.vo.PrplReplevyDetailVo;
import ins.sino.claimcar.claim.vo.PrplReplevyMainVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.manager.vo.PrpdCheckBankMainVo;
import ins.sino.claimcar.manager.vo.PrpdIntermBankVo;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.manager.vo.PrpdcheckBankVo;
import ins.sino.claimcar.other.vo.PrpLAcheckMainVo;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLAssessorMainVo;
import ins.sino.claimcar.other.vo.PrpLCheckFeeVo;
import ins.sino.claimcar.pay.service.PadPayPubService;
import ins.sino.claimcar.payment.detail.vo.JFeeVo;
import ins.sino.claimcar.payment.detail.webservice.CallPaymentDetailWebService;
import ins.sino.claimcar.payment.vo.JPlanMainVo;
import ins.sino.claimcar.payment.vo.JPlanReturnVo;
import ins.sino.claimcar.payment.vo.JlinkAccountVo;
import ins.sino.claimcar.payment.vo.JplanFeeDetailVo;
import ins.sino.claimcar.payment.vo.JplanFeeVo;
import ins.sino.claimcar.payment.webservice.CallPaymentWebService;
import ins.sino.claimcar.recloss.vo.PrpLRecLossDetailVo;
import ins.sino.claimcar.recloss.vo.PrpLRecLossVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
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
import org.springframework.scheduling.annotation.Async;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 调用收付接口
 * @author ★XMSH
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("padPayService")
public class ClaimToPaymentServiceImpl implements ClaimToPaymentService {

	private static Logger logger = LoggerFactory.getLogger(ClaimToPaymentServiceImpl.class);

	@Autowired
	private CodeTranService codeTranService;
	@Autowired
	private CallPaymentWebService callPaymentWebService;
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
	ClaimInvoiceService claimInvoiceService;
	@Autowired 
	PadPayPubService padPayPubService;

	/* 
	 * @see ins.sino.claimcar.payment.service.ClaimToPaymentService#prePayToPayment(ins.sino.claimcar.claim.vo.PrpLCompensateVo)
	 * @param compensateVo
	 * @throws Exception
	 */
	@Override
	public void prePayToPayment(PrpLCompensateVo compensateVo) throws Exception {
		// 分别获取预付赔款和费用列表
		List<PrpLPrePayVo> prePayPVos = compensateService.getPrePayVo(compensateVo.getCompensateNo(),"P");
		List<PrpLPrePayVo> prePayFVos = compensateService.getPrePayVo(compensateVo.getCompensateNo(),"F");
		PrpLCMainVo prplcMainVo = policyViewService.findPolicyInfoByPaltform(compensateVo.getRegistNo(), compensateVo.getPolicyNo());
		
		JPlanMainVo jPlanMainVo = new JPlanMainVo();
		jPlanMainVo.setCertiType("Y");
		jPlanMainVo.setCertiNo(compensateVo.getCompensateNo());
		jPlanMainVo.setPolicyNo(compensateVo.getPolicyNo());
		jPlanMainVo.setRegistNo(compensateVo.getRegistNo());
		jPlanMainVo.setClaimNo(compensateVo.getClaimNo());
		jPlanMainVo.setOperateCode(compensateVo.getCreateUser());
		jPlanMainVo.setOperateComCode(compensateVo.getComCode());
		jPlanMainVo.setPayComCode(compensateVo.getComCode());

		List<JplanFeeVo> jPlanFeeVos = new ArrayList<JplanFeeVo>();// 收付主数据
		List<JlinkAccountVo> jlinkAccountVos = new ArrayList<JlinkAccountVo>();// 收款直付帐号明细数据

		if(prePayPVos!=null&&prePayPVos.size()>0){// 预付赔款

			Map<Long,PrpLPayCustomVo> customMap = new HashMap<Long,PrpLPayCustomVo>();
			for(PrpLPrePayVo prePayVo:prePayPVos){

				// 合并收款直付帐号
				PrpLPayCustomVo customVo = managerService.findPayCustomVoById(prePayVo.getPayeeId());
				if(customMap.containsKey(customVo.getId())){
					PrpLPayCustomVo payCustomVo = customMap.get(customVo.getId());
					payCustomVo.setSumAmt(prePayVo.getPayAmt().doubleValue()+payCustomVo.getSumAmt());
					payCustomVo.getPrpLPrePayVos().add(prePayVo);
					customMap.put(customVo.getId(),payCustomVo);
				}else{
					customVo.setOtherFlag(prePayVo.getOtherFlag());
					customVo.setOtherCause(prePayVo.getOtherCause());
					customVo.setSumAmt(prePayVo.getPayAmt().doubleValue());// 收款人赔款金额
					customVo.getPrpLPrePayVos().add(prePayVo);
					customMap.put(customVo.getId(),customVo);
				}
			}
			for(PrpLPayCustomVo payCustomVo:customMap.values()){
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
					jplanFeeVo.setSerialNo(Integer.parseInt(prePayVo.getSerialNo()));// 因为合并赔款人，序列号随便取一条
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
				jPlanFeeVos.add(jplanFeeVo);

				JlinkAccountVo jlinkAccountVo = new JlinkAccountVo();// 收款支付账号明细数据
				jlinkAccountVo.setPayRefReason("P50");
				jlinkAccountVo.setPlanFee(payCustomVo.getSumAmt());
				jlinkAccountVo.setAccountNo(payCustomVo.getAccountId());
				jlinkAccountVo.setPayReasonFlag(payCustomVo.getOtherFlag());
				jlinkAccountVo.setPayReason(payCustomVo.getOtherCause());
				// jlinkAccountVo.setPayObject(payObject);
				// jlinkAccountVo.setSubPayObject(subPayObject);
//				if("1".equals(payCustomVo.getPayObjectType())){// 收款人性质，1-个人，2-机构
//					jlinkAccountVo.setCentiType("01");
//				}else{
//					jlinkAccountVo.setCentiType("71");
//				}
				jlinkAccountVo.setCentiType(payCustomVo.getCertifyType());
				jlinkAccountVo.setCentiCode(payCustomVo.getCertifyNo());
				jlinkAccountVos.add(jlinkAccountVo);
			}

		}

		if(prePayFVos!=null&&prePayFVos.size()>0){// 预付费用
			for(PrpLPrePayVo prePayVo:prePayFVos){
				PrpLPayCustomVo customVo = managerService.findPayCustomVoById(prePayVo.getPayeeId());
				SysCodeDictVo dictVo = codeTranService.findTransCodeDictVo("ChargeCode",prePayVo.getChargeCode());

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
				jPlanFeeVos.add(jPlanFeeVo);

				JlinkAccountVo jlinkAccountVo = new JlinkAccountVo();// 收款支付账号明细数据
				jlinkAccountVo.setPayRefReason(dictVo.getProperty1());
				jlinkAccountVo.setPlanFee(prePayVo.getPayAmt().doubleValue());
				jlinkAccountVo.setAccountNo(customVo.getAccountId());
				jlinkAccountVo.setPayReasonFlag("0");
				jlinkAccountVo.setPayReason(prePayVo.getOtherCause());
				// jlinkAccountVo.setPayObject(payObject);//支付对象
				// jlinkAccountVo.setSubPayObject(subPayObject);//支付子对象
				if("1".equals(customVo.getPayObjectType())){// 收款人性质，1-个人，2-机构
					jlinkAccountVo.setCentiType("01");
				}else{
					jlinkAccountVo.setCentiType("71");
				}
				jlinkAccountVo.setCentiCode(customVo.getCertifyNo());
				jlinkAccountVos.add(jlinkAccountVo);
			}
		}
		jPlanMainVo.setJplanFeeVos(jPlanFeeVos);
		jPlanMainVo.setJlinkAccountVos(jlinkAccountVos);
		// 送收付接口
		JPlanReturnVo returnVo = callPaymentWebService.callPaymentForClient(jPlanMainVo,BusinessType.Payment_prePay,compensateVo.getCompensateNo());
		// 送收付成功后回写预付PAYSTATUS字段为2-表示送收付成功
		if(returnVo.isResponseCode()){
			List<PrpLPrePayVo> prePayVos = new ArrayList<PrpLPrePayVo>();
			if(prePayFVos!=null && prePayFVos.size()>0){
				for(PrpLPrePayVo prePayFVo:prePayFVos){
					if(BigDecimal.ZERO.compareTo(prePayFVo.getPayAmt())==-1){
						prePayFVo.setPayStatus("2");
					}
				}
				prePayVos.addAll(prePayFVos);
			}
			if(prePayPVos!=null && prePayPVos.size()>0){
				for(PrpLPrePayVo prePayPVo:prePayPVos){
					if(BigDecimal.ZERO.compareTo(prePayPVo.getPayAmt())==-1){
						prePayPVo.setPayStatus("2");
					}
				}
				prePayVos.addAll(prePayPVos);
			}
			
			compensateService.saveOrUpdatePrePay(prePayVos,compensateVo.getCompensateNo());
		}
	}

	/* 
	 * @see ins.sino.claimcar.payment.service.ClaimToPaymentService#padPayToPayment(ins.sino.claimcar.claim.vo.PrpLPadPayMainVo)
	 * @param padPayMainVo
	 * @throws Exception
	 */
	@Override
	public void padPayToPayment(PrpLPadPayMainVo padPayMainVo) throws Exception {
		JPlanMainVo jPlanMainVo = new JPlanMainVo();

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

		List<JplanFeeVo> jPlanFeeVos = new ArrayList<JplanFeeVo>();
		List<JlinkAccountVo> jlinkAccountVos = new ArrayList<JlinkAccountVo>();
		Map<String,PrpLPadPayPersonVo> map = new HashMap<String,PrpLPadPayPersonVo>();
		for(PrpLPadPayPersonVo padPayPersonVo:padPayMainVo.getPrpLPadPayPersons()){
			PrpLPayCustomVo customVo = managerService.findPayCustomVoById(padPayPersonVo.getPayeeId());
			JplanFeeVo jPlanFeeVo = new JplanFeeVo();// 收付明细

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
			jPlanFeeVos.add(jPlanFeeVo);
			
			if(map.containsKey(customVo.getAccountId())){
				PrpLPadPayPersonVo personVo = map.get(customVo.getAccountId());
				BigDecimal sumCostSum = personVo.getCostSum().add(padPayPersonVo.getCostSum());
				personVo.setCostSum(sumCostSum);
				map.put(customVo.getAccountId(),personVo);
			}else{
				map.put(customVo.getAccountId(),padPayPersonVo);
			}
		}
		
		//合并相同的垫付收款人
		for(PrpLPadPayPersonVo padPayPersonVo:map.values()){
			PrpLPayCustomVo customVo = managerService.findPayCustomVoById(padPayPersonVo.getPayeeId());
			JlinkAccountVo jlinkAccountVo = new JlinkAccountVo();// 收款支付账号明细数据
			jlinkAccountVo.setPayRefReason("P6K");
			jlinkAccountVo.setPlanFee(padPayPersonVo.getCostSum().doubleValue());
			jlinkAccountVo.setAccountNo(customVo.getAccountId());
			jlinkAccountVo.setPayReasonFlag(padPayPersonVo.getOtherFlag());
			jlinkAccountVo.setPayReason(padPayPersonVo.getOtherCause());
			// jlinkAccountVo.setPayObject(payObject);//支付对象
			// jlinkAccountVo.setSubPayObject(subPayObject);//支付子对象
//			if("1".equals(customVo.getPayObjectType())){// 收款人性质，1-个人，2-机构
//				jlinkAccountVo.setCentiType("01");
//			}else{
//				jlinkAccountVo.setCentiType("71");
//			}
			jlinkAccountVo.setCentiType(customVo.getCertifyType());
			jlinkAccountVo.setCentiCode(customVo.getCertifyNo());
			jlinkAccountVos.add(jlinkAccountVo);
		}

		jPlanMainVo.setJplanFeeVos(jPlanFeeVos);
		jPlanMainVo.setJlinkAccountVos(jlinkAccountVos);

		JPlanReturnVo returnVo = callPaymentWebService.callPaymentForClient(jPlanMainVo,BusinessType.Payment_padPay,padPayMainVo.getCompensateNo());
		// 送收付成功后回写垫付PAYSTATUS字段为2-表示送收付成功
		if(returnVo.isResponseCode()){
			if(padPayMainVo.getPrpLPadPayPersons()!=null&&padPayMainVo.getPrpLPadPayPersons().size()>0){
				for(PrpLPadPayPersonVo PadPayPersonVo:padPayMainVo.getPrpLPadPayPersons()){
					if(BigDecimal.ZERO.compareTo(PadPayPersonVo.getCostSum())==-1){
						PadPayPersonVo.setPayStatus("2");
					}
				}
			}
			padPayPubService.updatePadPay(padPayMainVo);
		}
	}

	/* 
	 * @see ins.sino.claimcar.payment.service.ClaimToPaymentService#compensateToPayment(ins.sino.claimcar.claim.vo.PrpLCompensateVo)
	 * @param compensateVo
	 * @throws Exception
	 */
	@Override
	public void compensateToPayment(PrpLCompensateVo compensateVo) throws Exception {
//		compensateVo.setUnderwriteDate(new Date());
		if(BigDecimal.ZERO.compareTo(DataUtils.NullToZero(compensateVo.getSumPaidAmt()))==0 
				&& BigDecimal.ZERO.compareTo(DataUtils.NullToZero(compensateVo.getSumPaidFee()))==0){//实付金额和费用 为0 不送收付
		}else{
			List<PrpLLossItemVo> lossItemP6BVos = compensateService.getLosItemList(compensateVo.getCompensateNo(),"1");// 代付赔款
			List<PrpLLossItemVo> lossItemP6EVos = compensateService.getLosItemList(compensateVo.getCompensateNo(),"2");// 清付
			List<PrpLLossItemVo> lossItemVos = compensateService.getLosItemList(compensateVo.getCompensateNo(),"3,4");// 赔款
			
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
			
			List<JplanFeeVo> jPlanFeeVos = new ArrayList<JplanFeeVo>();// 收付主数据
			List<JlinkAccountVo> jlinkAccountVos = new ArrayList<JlinkAccountVo>();// 收款人
			PrpLCMainVo prplcMainVo = policyViewService.findPolicyInfoByPaltform(compensateVo.getRegistNo(), compensateVo.getPolicyNo());
			
			// 代付赔款
			int i=0;
			if(lossItemP6BVos!=null&&lossItemP6BVos.size()>0){
				for(PrpLLossItemVo lossItemVo:lossItemP6BVos){
//					PrpLPlatLockVo platLockVo = platLockService.findPrpLPlatLockVoByLicenseNo(compensateVo.getRegistNo(),
//							compensateVo.getPolicyNo(),PayFlagType.INSTEAD_PAY,lossItemVo.getItemName());
					
					List<PrpLPlatLockVo> platLockVos = platLockService.findPrpLPlatLockVoList(compensateVo.getRegistNo(),compensateVo.getPolicyNo(),PayFlagType.INSTEAD_PAY);
					
					for(PrpLPlatLockVo platLockVo : platLockVos){
						// 赔款主数据
						JplanFeeVo jPlanFeeP6BVo = new JplanFeeVo();// 收付明细
						jPlanFeeP6BVo.setPayRefReason("P6B");
						jPlanFeeP6BVo.setCurrency("CNY");
						
						jPlanFeeP6BVo.setUnderWriteDate(compensateVo.getUnderwriteDate());
						jPlanFeeP6BVo.setAppliCode(prplcMainVo.getAppliCode());
						jPlanFeeP6BVo.setAppliName(prplcMainVo.getAppliName());
						// jPlanFeeVo.setAppliCode(customVo.getCertifyNo());
						// jPlanFeeVo.setAppliName(customVo.getPayeeName());
						jPlanFeeP6BVo.setSerialNo(i);
						i++;
						
						String voucherNo2 = "无";// 非机动车代位时 无结算码 无锁定platLockVo 此时传无
						Double amt = 0d;
						for(PrpLRecoveryOrPayVo recoveryOrPayVo : platLockVo.getPrpLRecoveryOrPays()){
							if(recoveryOrPayVo.getCompensateNo().equals(compensateVo.getCompensateNo())){
								voucherNo2 = recoveryOrPayVo.getRecoveryCode();
								amt = recoveryOrPayVo.getRecoveryOrPayAmount().doubleValue();
							}
						}
						jPlanFeeP6BVo.setVoucherNo2(voucherNo2);
						
						List<JplanFeeDetailVo> detailVos = new ArrayList<JplanFeeDetailVo>();
						JplanFeeDetailVo detailVo = new JplanFeeDetailVo();
						detailVo.setKindCode("A");
						detailVo.setPlanfee(amt);
//						sumPlanFee += detailVo.getPlanfee();
						detailVos.add(detailVo);
						jPlanFeeP6BVo.setPlanFee(amt);
						jPlanFeeP6BVo.setJplanFeeDetailVos(detailVos);
						jPlanFeeVos.add(jPlanFeeP6BVo);
					}
				}
			}
			
			// 清付赔款
			if(lossItemP6EVos!=null&&lossItemP6EVos.size()>0){
				for(PrpLLossItemVo lossItemVo:lossItemP6EVos){
					PrpLPlatLockVo platLockVo = platLockService.findPrpLPlatLockVoByLicenseNo(compensateVo.getRegistNo(),
							compensateVo.getPolicyNo(),PayFlagType.CLEAR_PAY,lossItemVo.getItemName());
					// 赔款主数据
					JplanFeeVo jPlanFeeP6EVo = new JplanFeeVo();// 收付明细
					jPlanFeeP6EVo.setPayRefReason("P6D");
					jPlanFeeP6EVo.setCurrency("CNY");
					jPlanFeeP6EVo.setPlanFee(compensateVo.getSumPaidAmt().doubleValue());
					jPlanFeeP6EVo.setUnderWriteDate(compensateVo.getUnderwriteDate());
					jPlanFeeP6EVo.setAppliCode(prplcMainVo.getAppliCode());
					jPlanFeeP6EVo.setAppliName(prplcMainVo.getAppliName());
					// jPlanFeeVo.setAppliCode(customVo.getCertifyNo());
					// jPlanFeeVo.setAppliName(customVo.getPayeeName());
					jPlanFeeP6EVo.setSerialNo(0);
					String voucherNo2 = "";
					for(PrpLRecoveryOrPayVo recoveryOrPayVo : platLockVo.getPrpLRecoveryOrPays()){
						if(recoveryOrPayVo.getCompensateNo().equals(compensateVo.getCompensateNo())){
							voucherNo2 = recoveryOrPayVo.getRecoveryCode();
						}
					}
					jPlanFeeP6EVo.setVoucherNo2(voucherNo2);
					
					List<JplanFeeDetailVo> detailVos = new ArrayList<JplanFeeDetailVo>();
					JplanFeeDetailVo detailVo = new JplanFeeDetailVo();
					detailVo.setKindCode(lossItemVo.getKindCode());
					detailVo.setPlanfee(lossItemVo.getSumRealPay().doubleValue());
					detailVos.add(detailVo);
					
					jPlanFeeP6EVo.setPlanFee(detailVo.getPlanfee());
					jPlanFeeP6EVo.setJplanFeeDetailVos(detailVos);
					jPlanFeeVos.add(jPlanFeeP6EVo);
				}
			}
			
			// 收款明细--细分险别
			double sumRealPay = 0d;
			List<JplanFeeDetailVo> detailVos = new ArrayList<JplanFeeDetailVo>();
			if(lossItemVos!=null&&lossItemVos.size()>0){// 车赔款险别明细
				for(PrpLLossItemVo itemVo:lossItemVos){
					if(BigDecimal.ZERO.compareTo(itemVo.getSumRealPay())==0){
						continue;// 赔款金额为0时不组织该数据
					}
					JplanFeeDetailVo detailVo = new JplanFeeDetailVo();
					detailVo.setKindCode(itemVo.getKindCode());
					detailVo.setPlanfee(itemVo.getSumRealPay().doubleValue() - DataUtils.NullToZero(itemVo.getOffPreAmt()).doubleValue());
					sumRealPay += detailVo.getPlanfee();
					detailVos.add(detailVo);
				}
			}
			if(compensateVo.getPrpLLossProps()!=null&&compensateVo.getPrpLLossProps().size()>0){// 财产赔款险别明细
				for(PrpLLossPropVo lossPropVo:compensateVo.getPrpLLossProps()){
					if(BigDecimal.ZERO.compareTo(lossPropVo.getSumRealPay())==0){
						continue;// 赔款金额为0时不组织该数据
					}
					JplanFeeDetailVo detailVo = new JplanFeeDetailVo();
					detailVo.setKindCode(lossPropVo.getKindCode());
					detailVo.setPlanfee(lossPropVo.getSumRealPay().doubleValue() - DataUtils.NullToZero(lossPropVo.getOffPreAmt()).doubleValue());
					sumRealPay += detailVo.getPlanfee();
					detailVos.add(detailVo);
				}
			}
			if(compensateVo.getPrpLLossPersons()!=null&&compensateVo.getPrpLLossPersons().size()>0){// 人伤赔款险别明细
				for(PrpLLossPersonVo lossPersonVo:compensateVo.getPrpLLossPersons()){
					if(BigDecimal.ZERO.compareTo(lossPersonVo.getSumRealPay())==0){
						continue;// 赔款金额为0时不组织该数据
					}
					JplanFeeDetailVo detailVo = new JplanFeeDetailVo();
					detailVo.setKindCode(lossPersonVo.getKindCode());
					detailVo.setPlanfee(lossPersonVo.getSumRealPay().doubleValue() - DataUtils.NullToZero(lossPersonVo.getOffPreAmt()).doubleValue());
					sumRealPay += detailVo.getPlanfee();
					detailVos.add(detailVo);
				}
			}
			// 正常赔款
			if(detailVos!=null&&detailVos.size()>0&&sumRealPay !=0){
//				PrpLPayCustomVo customVo = managerService.findPayCustomVoById(paymentVo.getPayeeId());
				// 赔款主数据
				JplanFeeVo jPlanFeeVo = new JplanFeeVo();// 收付明细
				jPlanFeeVo.setPayRefReason("P60");
				jPlanFeeVo.setCurrency("CNY");
				jPlanFeeVo.setUnderWriteDate(compensateVo.getUnderwriteDate());
				jPlanFeeVo.setAppliCode(prplcMainVo.getAppliCode());
				jPlanFeeVo.setAppliName(prplcMainVo.getAppliName());
//				 jPlanFeeVo.setAppliCode(customVo.getCertifyNo());
//				 jPlanFeeVo.setAppliName(customVo.getPayeeName());
				jPlanFeeVo.setSerialNo(0);
//				Double sumFee = compensateVo.getSumPaidAmt().doubleValue()-sumPlanFee;
				jPlanFeeVo.setPlanFee(sumRealPay);
				jPlanFeeVo.setJplanFeeDetailVos(detailVos);
				jPlanFeeVos.add(jPlanFeeVo);
			}
			
			// 合并相同的费用和收款人
			Map<String,PrpLPayCustomVo> chargeMap = new HashMap<String,PrpLPayCustomVo>();
			for(PrpLChargeVo chargeVo:compensateVo.getPrpLCharges()){
				String key = chargeVo.getPayeeId()+chargeVo.getChargeCode();
				if(chargeMap.containsKey(key)){
//					Double sumAmt = chargeVo.getFeeRealAmt().doubleValue()-chargeVo.getOffPreAmt().doubleValue();
					Double sumAmt = DataUtils.NullToZero(chargeVo.getFeeRealAmt()).doubleValue();
							//-DataUtils.NullToZero(chargeVo.getOffPreAmt()).doubleValue()
					PrpLPayCustomVo payCustomVo = chargeMap.get(key);
					payCustomVo.setSumAmt(sumAmt+payCustomVo.getSumAmt());
					Integer oldSerialNo = Integer.parseInt(payCustomVo.getSerialNo()) ;
					Integer serialNo = Integer.parseInt(chargeVo.getSerialNo()) ;
					if(oldSerialNo > serialNo){
						payCustomVo.setSerialNo(chargeVo.getSerialNo());
					}
					payCustomVo.getPrpLChargeVos().add(chargeVo);
					chargeMap.put(key,payCustomVo);
				}else{
					PrpLPayCustomVo customVo = managerService.findPayCustomVoById(chargeVo.getPayeeId());
					SysCodeDictVo dictVo = codeTranService.findTransCodeDictVo("ChargeCode",chargeVo.getChargeCode());
					
					// Double sumAmt = chargeVo.getFeeRealAmt().doubleValue()-DataUtils.NullToZero(chargeVo.getOffPreAmt())
					// .doubleValue();
					
					Double sumAmt = DataUtils.NullToZero(chargeVo.getFeeRealAmt()).doubleValue();
							//-DataUtils.NullToZero(chargeVo.getOffPreAmt()).doubleValue();
					customVo.setSumAmt(sumAmt);
					customVo.setSerialNo(chargeVo.getSerialNo());
					customVo.setPayRefReason(dictVo.getProperty2());
					customVo.getPrpLChargeVos().add(chargeVo);
					chargeMap.put(key,customVo);
				}
			}
			
			for(PrpLPayCustomVo customVo:chargeMap.values()){
				// 费用收款主数据
				if(customVo.getSumAmt()!=0d){
					JplanFeeVo jplanFeeChargeVo = new JplanFeeVo();
					jplanFeeChargeVo.setPayRefReason(customVo.getPayRefReason());// 实赔使用property2
					jplanFeeChargeVo.setCurrency("CNY");
					jplanFeeChargeVo.setPlanFee(customVo.getSumAmt());
					jplanFeeChargeVo.setUnderWriteDate(compensateVo.getUnderwriteDate());
					jplanFeeChargeVo.setAppliCode(prplcMainVo.getAppliCode());
					jplanFeeChargeVo.setAppliName(prplcMainVo.getAppliName());
//					jplanFeeChargeVo.setAppliCode(customVo.getCertifyNo());
//					jplanFeeChargeVo.setAppliName(customVo.getPayeeName());
					jplanFeeChargeVo.setSerialNo(Integer.parseInt(customVo.getSerialNo()));
					
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
					jPlanFeeVos.add(jplanFeeChargeVo);
					
					// 收款支付账号明细数据
					JlinkAccountVo jlinkAccountVo = new JlinkAccountVo();
					jlinkAccountVo.setPayRefReason(customVo.getPayRefReason());
					jlinkAccountVo.setPlanFee(customVo.getSumAmt());
					jlinkAccountVo.setAccountNo(customVo.getAccountId());
					jlinkAccountVo.setPayReasonFlag("0");// 费用没有收付例外标志和原因
					// jlinkAccountVo.setPayObject(payObject);//支付对象
					// jlinkAccountVo.setSubPayObject(subPayObject);//支付子对象
//					if("1".equals(customVo.getPayObjectType())){// 收款人性质，1-个人，2-机构
//						jlinkAccountVo.setCentiType("01");
//					}else{
//						jlinkAccountVo.setCentiType("71");
//					}
					jlinkAccountVo.setCentiType(customVo.getCertifyType());
					jlinkAccountVo.setCentiCode(customVo.getCertifyNo());
					jlinkAccountVos.add(jlinkAccountVo);
				}
			}
			
			// 赔款
			for(PrpLPaymentVo paymentVo:compensateVo.getPrpLPayments()){
				if(paymentVo.getSumRealPay().doubleValue()!=0d){
					PrpLPayCustomVo customVo = managerService.findPayCustomVoById(paymentVo.getPayeeId());
					// 收款直付帐号明细数据
					JlinkAccountVo jlinkAccountVo = new JlinkAccountVo();// 收款支付账号明细数据
					if(PayFlagType.COMPENSATE_PAY.equals(paymentVo.getPayFlag())){//自付
						jlinkAccountVo.setPayRefReason("P60");
					}else if(PayFlagType.CLEAR_PAY.equals(paymentVo.getPayFlag())){//清付
						jlinkAccountVo.setPayRefReason("P6D");
					}else if(PayFlagType.INSTEAD_PAY.equals(paymentVo.getPayFlag())){//代付/追偿
						jlinkAccountVo.setPayRefReason("P6B");
					}
					
					jlinkAccountVo.setPlanFee(paymentVo.getSumRealPay().doubleValue());
					jlinkAccountVo.setAccountNo(customVo.getAccountId());
					jlinkAccountVo.setPayReasonFlag(paymentVo.getOtherFlag());
					jlinkAccountVo.setPayReason(paymentVo.getOtherCause());
					// jlinkAccountVo.setPayObject(payObject);//支付对象
					// jlinkAccountVo.setSubPayObject(subPayObject);//支付子对象
					if("1".equals(customVo.getPayObjectType())){// 收款人性质，1-个人，2-机构
						jlinkAccountVo.setCentiType("01");
					}else{
						jlinkAccountVo.setCentiType("71");
					}
					jlinkAccountVo.setCentiCode(customVo.getCertifyNo());
					jlinkAccountVos.add(jlinkAccountVo);
				}
			}
			jPlanMainVo.setJplanFeeVos(jPlanFeeVos);
			jPlanMainVo.setJlinkAccountVos(jlinkAccountVos);
			
			JPlanReturnVo returnVo = callPaymentWebService.callPaymentForClient(jPlanMainVo,BusinessType.Payment_compe,compensateVo.getCompensateNo());
			// 送收付成功后回写理算PAYSTATUS字段为2-表示送收付成功
			if(returnVo.isResponseCode()){
				if(compensateVo.getPrpLPayments()!=null&&compensateVo.getPrpLPayments().size()>0){
					for(PrpLPaymentVo paymentVo : compensateVo.getPrpLPayments()){
						if(BigDecimal.ZERO.compareTo(paymentVo.getSumRealPay())==-1){
							paymentVo.setPayStatus("2");
							compensateService.updatePrpLPaymentVo(paymentVo);
						}
					}
				}
				if(compensateVo.getPrpLCharges()!=null&&compensateVo.getPrpLCharges().size()>0){
					for(PrpLChargeVo chargeVo:compensateVo.getPrpLCharges()){
						if(BigDecimal.ZERO.compareTo(chargeVo.getFeeRealAmt())==-1){
							chargeVo.setPayStatus("2");
							compensateService.updatePrpLCharges(chargeVo);
						}
					}
				}
			}
		}
			
		}
			

	//组织平台结算码
		private String getPlatformRecoveryCode(PrpLCompensateVo compensateVo, String payFlagType) {
			String voucherNo2 = "";
			List<PrpLPlatLockVo> lockList = platLockService.findPrpLPlatLockVoList(compensateVo.getRegistNo(),compensateVo.getPolicyNo(),payFlagType);
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
	/* 
	 * @see ins.sino.claimcar.payment.service.ClaimToPaymentService#recPayToPayment(ins.sino.claimcar.claim.vo.PrplReplevyMainVo)
	 * @param prplReplevyMainVo
	 * @throws Exception
	 */
	@Override
	public void recPayToPayment(PrplReplevyMainVo prplReplevyMainVo) throws Exception {
								
		PrpLCMainVo prplcMainVo = policyViewService.findPolicyInfoByPaltform(prplReplevyMainVo.getRegistNo(), prplReplevyMainVo.getPolicyNo());
		if(prplReplevyMainVo.getSumRealReplevy().doubleValue()>0d||prplReplevyMainVo.getSumReplevyFee().doubleValue()>0d){
			JPlanMainVo jPlanMainVo = new JPlanMainVo();
			
			jPlanMainVo.setCertiType("Z");
			jPlanMainVo.setCertiNo(prplReplevyMainVo.getCompensateNo());
			jPlanMainVo.setPolicyNo(prplReplevyMainVo.getPolicyNo());
			jPlanMainVo.setRegistNo(prplReplevyMainVo.getRegistNo());
			jPlanMainVo.setClaimNo(prplReplevyMainVo.getClaimNo());
			jPlanMainVo.setOperateCode(prplReplevyMainVo.getCreateUser());
			jPlanMainVo.setOperateComCode(prplReplevyMainVo.getComCode());
			jPlanMainVo.setPayComCode(prplReplevyMainVo.getComCode());

			List<JplanFeeVo> jPlanFeeVos = new ArrayList<JplanFeeVo>();
			//追偿赔款
			if(prplReplevyMainVo.getSumRealReplevy() != null && !(BigDecimal.ZERO.compareTo(prplReplevyMainVo.getSumRealReplevy())==0)){
				JplanFeeVo jPlanFeeVo = new JplanFeeVo();// 收付明细
				jPlanFeeVo.setPayRefReason("P6C");//2017-03-16陆韦修改 费用和赔款的 搞反了
				jPlanFeeVo.setCurrency("CNY");
				jPlanFeeVo.setPlanFee(prplReplevyMainVo.getSumRealReplevy().doubleValue());
				jPlanFeeVo.setUnderWriteDate(new Date());
				jPlanFeeVo.setSerialNo(0);
				jPlanFeeVo.setAppliCode(prplcMainVo.getAppliCode());
				jPlanFeeVo.setAppliName(prplcMainVo.getAppliName());
				PrpLCompensateVo compensateVo = compensateService.findCompByPK(prplReplevyMainVo.getCompensateNo());
				//组织平台结算码
//				String voucherNo2 = getPlatformRecoveryCode(compensateVo, PayFlagType.INSTEAD_PAY);
//				if(StringUtils.isBlank(voucherNo2)){
					String voucherNo2 = "无";// 非机动车代位时 无结算码 无锁定platLockVo 此时传无
//				}
				jPlanFeeVo.setVoucherNo2(voucherNo2);
				// jPlanFeeVo.setAppliCode(customVo.getCertifyNo());
				// jPlanFeeVo.setAppliName(customVo.getPayeeName());
				
				List<JplanFeeDetailVo> detailVos = new ArrayList<JplanFeeDetailVo>();
				for(PrplReplevyDetailVo replevyDetailVo:prplReplevyMainVo.getPrplReplevyDetails()){
				    if("P".equals(replevyDetailVo.getLossCategory())){//赔款
				        JplanFeeDetailVo detailVo = new JplanFeeDetailVo();
	                    detailVo.setKindCode(replevyDetailVo.getKindCode());
	                    detailVo.setPlanfee(replevyDetailVo.getRealReplevy().doubleValue());
	                    detailVos.add(detailVo);
				    }
				}
				jPlanFeeVo.setJplanFeeDetailVos(detailVos);
				jPlanFeeVos.add(jPlanFeeVo);
			}
			//追偿费用
			if(prplReplevyMainVo.getSumReplevyFee()!=null && !(BigDecimal.ZERO.compareTo(prplReplevyMainVo.getSumReplevyFee())==0)){
				JplanFeeVo jPlanFeeVo_Fee = new JplanFeeVo();
				jPlanFeeVo_Fee.setPayRefReason("P6H");//2017-03-16陆韦修改 费用和赔款的 搞反了
				jPlanFeeVo_Fee.setCurrency("CNY");
				jPlanFeeVo_Fee.setPlanFee(prplReplevyMainVo.getSumReplevyFee().doubleValue());
				jPlanFeeVo_Fee.setUnderWriteDate(new Date());
				jPlanFeeVo_Fee.setSerialNo(1);
				jPlanFeeVo_Fee.setAppliCode(prplcMainVo.getAppliCode());
				jPlanFeeVo_Fee.setAppliName(prplcMainVo.getAppliName());
				
				List<JplanFeeDetailVo> detailVos_Fee = new ArrayList<JplanFeeDetailVo>();
				for(PrplReplevyDetailVo replevyDetailVo:prplReplevyMainVo.getPrplReplevyDetails()){
				    if("F".equals(replevyDetailVo.getLossCategory())){//费用
				        JplanFeeDetailVo detailVo_Fee = new JplanFeeDetailVo();
	                    detailVo_Fee.setKindCode(replevyDetailVo.getKindCode());
	                    detailVo_Fee.setPlanfee(replevyDetailVo.getRealReplevy().doubleValue());
	                    detailVos_Fee.add(detailVo_Fee);
				    }
				}
				jPlanFeeVo_Fee.setJplanFeeDetailVos(detailVos_Fee);
				jPlanFeeVos.add(jPlanFeeVo_Fee);
			}
			
			jPlanMainVo.setJplanFeeVos(jPlanFeeVos);

			callPaymentWebService.callPaymentForClient(jPlanMainVo,BusinessType.Payment_recPay,prplReplevyMainVo.getId().toString());
		}

	}

	/* 
	 * @see ins.sino.claimcar.payment.service.ClaimToPaymentService#recLossToPayment(ins.sino.claimcar.recloss.vo.PrpLRecLossVo)
	 * @param recLossVo
	 * @throws Exception
	 */
	@Override
	public void recLossToPayment(PrpLRecLossVo recLossVo) throws Exception {
		
		JPlanMainVo jPlanMainVo = new JPlanMainVo();
		
		List<PrpLClaimVo> claimVos = claimTaskService.findClaimListByRegistNo(recLossVo.getRegistNo());
		
		String makComCode = "";
		String claimNo = "";
		if(claimVos!=null&&claimVos.size()>0){
			for(PrpLClaimVo claimVo : claimVos){
				if(claimVos.size()==2&&!"1101".equals(claimVo.getRiskCode())){
					claimNo = claimVo.getClaimNo();
					makComCode = policyViewService.findPolicyComCode(recLossVo.getRegistNo(),"12");
				}else if(claimVos.size()!=2){
					claimNo = claimVo.getClaimNo();
					if("1101".equals(claimVo.getRiskCode())){
						makComCode = policyViewService.findPolicyComCode(recLossVo.getRegistNo(),"11");
					}else{
						makComCode = policyViewService.findPolicyComCode(recLossVo.getRegistNo(),"12");
					}
				}
			}
		}
		//损余回收 为0 不送收付
		if(DataUtils.NullToZero(recLossVo.getRecLossFee()).compareTo(BigDecimal.ZERO)==1){
			//报案信息
			PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(recLossVo.getRegistNo());
			PrpLCMainVo prplcMainVo = policyViewService.findPolicyInfoByPaltform(prpLRegistVo.getRegistNo(), prpLRegistVo.getPolicyNo());
			
			jPlanMainVo.setCertiType("C");
			jPlanMainVo.setCertiNo(recLossVo.getPrpLRecLossId());
			jPlanMainVo.setPolicyNo(prpLRegistVo.getPolicyNo());
			jPlanMainVo.setRegistNo(prpLRegistVo.getRegistNo());
			jPlanMainVo.setClaimNo(claimNo);
			jPlanMainVo.setOperateCode(recLossVo.getCreateUser());
//			jPlanMainVo.setOperateComCode(userVo.getComCode());
			jPlanMainVo.setOperateComCode(makComCode);
			jPlanMainVo.setPayComCode(makComCode);

			List<JplanFeeVo> jPlanFeeVos = new ArrayList<JplanFeeVo>();
			JplanFeeVo jPlanFeeVo = new JplanFeeVo();// 收付明细
			
			jPlanFeeVo.setPayRefReason("P6J");
			jPlanFeeVo.setCurrency("CNY");
			jPlanFeeVo.setPlanFee(recLossVo.getRecLossFee().doubleValue());
			jPlanFeeVo.setUnderWriteDate(new Date());
			jPlanFeeVo.setSerialNo(0);
			jPlanFeeVo.setAppliCode(prplcMainVo.getAppliCode());
			jPlanFeeVo.setAppliName(prplcMainVo.getAppliName());
//			jPlanFeeVo.setAppliCode(customVo.getCertifyNo());
//			jPlanFeeVo.setAppliName(customVo.getPayeeName());
			
			List<JplanFeeDetailVo> detailVos = new ArrayList<JplanFeeDetailVo>();
			for(PrpLRecLossDetailVo lossDetailVo : recLossVo.getPrpLRecLossDetails()){
				JplanFeeDetailVo detailVo = new JplanFeeDetailVo();
				detailVo.setKindCode(lossDetailVo.getKindCode());
				detailVo.setPlanfee(lossDetailVo.getRecLossFee().doubleValue());
				detailVos.add(detailVo);
			}
			
			jPlanFeeVo.setJplanFeeDetailVos(detailVos);
			jPlanFeeVos.add(jPlanFeeVo);
			jPlanMainVo.setJplanFeeVos(jPlanFeeVos);
			
			callPaymentWebService.callPaymentForClient(jPlanMainVo,BusinessType.Payment_recLoss,recLossVo.getPrpLRecLossId());
		}
	}
	@Override
	public void assessorToPayment(PrpLAssessorMainVo assessorMainVo,PrpLAssessorFeeVo assessorFeeVo) throws Exception {
		
			JPlanMainVo jPlanMainVo = new JPlanMainVo();
			logger.debug("registNo:"+assessorFeeVo.getRegistNo());
			String policyType = "BZ".equals(assessorFeeVo.getKindCode()) ? "11" : "12";
			String makComCode = policyViewService.findPolicyComCode(assessorFeeVo.getRegistNo(),policyType);
			
			jPlanMainVo.setCertiType("C");
			jPlanMainVo.setCertiNo(assessorFeeVo.getCompensateNo());
			jPlanMainVo.setPolicyNo(assessorFeeVo.getPolicyNo());
			jPlanMainVo.setRegistNo(assessorFeeVo.getRegistNo());
			jPlanMainVo.setClaimNo(assessorFeeVo.getClaimNo());
			jPlanMainVo.setOperateCode(assessorMainVo.getUpdateUser());
			jPlanMainVo.setOperateComCode(makComCode);
			jPlanMainVo.setPayComCode(makComCode);
			
			List<JplanFeeVo> jPlanFeeVos = new ArrayList<JplanFeeVo>();
			JplanFeeVo jPlanFeeVo = new JplanFeeVo();// 收付明细
			
			jPlanFeeVo.setPayRefReason("P67");
			jPlanFeeVo.setCurrency("CNY");
			jPlanFeeVo.setPlanFee(assessorFeeVo.getAmount().doubleValue());
			jPlanFeeVo.setUnderWriteDate(new Date());
			jPlanFeeVo.setSerialNo(0);
			jPlanFeeVo.setAppliCode(assessorMainVo.getIntermcode());
			jPlanFeeVo.setAppliName(assessorMainVo.getIntermname());
//			jPlanFeeVo.setAppliCode(customVo.getCertifyNo());
//			jPlanFeeVo.setAppliName(customVo.getPayeeName());
			
			List<JplanFeeDetailVo> detailVos = new ArrayList<JplanFeeDetailVo>();
			JplanFeeDetailVo detailVo = new JplanFeeDetailVo();
			detailVo.setKindCode(assessorFeeVo.getKindCode());
			detailVo.setPlanfee(assessorFeeVo.getAmount().doubleValue());
			detailVos.add(detailVo);
			jPlanFeeVo.setJplanFeeDetailVos(detailVos);
			jPlanFeeVos.add(jPlanFeeVo);
			jPlanMainVo.setJplanFeeVos(jPlanFeeVos);
			
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
			
			// 收款直付帐号明细数据
			List<JlinkAccountVo> jlinkAccountVos = new ArrayList<JlinkAccountVo>();// 收款人
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
			jlinkAccountVo.setAccountCode(bankVo.getAccountNo());
			jlinkAccountVo.setAccountName(bankVo.getAccountNo());
			
			jlinkAccountVos.add(jlinkAccountVo);
			jPlanMainVo.setJlinkAccountVos(jlinkAccountVos);
			callPaymentWebService.callPaymentForClient(jPlanMainVo,BusinessType.Payment_assessor,assessorFeeVo.getCompensateNo());
			//callPaymentDetailWebService.callPaymentForClient(jPlanMainVo,BusinessType.Payment_compe,assessorFeeVo.getCompensateNo(), "1");
			//callPaymentDetailWebService.callPaymentForClient(jPlanMainVo,BusinessType.Payment_assessor,assessorFeeVo.getCompensateNo(),"1");
	}

	
	@Override
	public boolean pushCharge(String compensateNo,String serialNoSend) throws Exception {
			return claimInvoiceService.pushCharge(compensateNo,serialNoSend);
		 
	}
	
	@Override
	public boolean pushPreCharge(String compensateNo,String serialNoSend) throws Exception {
			return claimInvoiceService.pushPreCharge(compensateNo,serialNoSend);
		 
	}
	
	@Override
	public boolean pushAssessorFee(PrpLAssessorFeeVo assessorVo, SysUserVo userVo)
			throws Exception {
		return claimInvoiceService.pushAssessorFee(assessorVo, userVo);
	}

	@Override
	public Boolean pushPadPay(PrpLPadPayMainVo padPayMainVo,String serialNoSend) {
		return claimInvoiceService.pushPadPay(padPayMainVo,serialNoSend);
	}

	

	@Override
	public boolean pushCheckFee(PrpLCheckFeeVo checkFeeVo, SysUserVo userVo)throws Exception {
		return claimInvoiceService.pushCheckFee(checkFeeVo, userVo);
	}


}
