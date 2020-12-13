/******************************************************************************
* CREATETIME : 2016年3月31日 下午9:10:32
******************************************************************************/
package ins.sino.claimcar.subrogation.service;

import ins.platform.utils.DataUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.payment.vo.JPlanMainVo;
import ins.sino.claimcar.payment.vo.JplanFeeDetailVo;
import ins.sino.claimcar.payment.vo.JplanFeeVo;
import ins.sino.claimcar.payment.webservice.CallPaymentWebService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.subrogation.platform.service.SubrogationAddService;
import ins.sino.claimcar.subrogation.platform.service.SubrogationToPlatService;
import ins.sino.claimcar.subrogation.platform.vo.AccountsInfoVo;
import ins.sino.claimcar.subrogation.sh.service.SubrogationSHHandleService;
import ins.sino.claimcar.subrogation.sh.vo.CopyInformationResultVo;
import ins.sino.claimcar.subrogation.sh.vo.CopyInformationSubrogationViewVo;
import ins.sino.claimcar.subrogation.sh.vo.SubrogationSHQueryVo;
import ins.sino.claimcar.subrogation.vo.PrpLPlatCheckVo;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;
import ins.sino.claimcar.subrogation.vo.PrpLRecoveryOrPayVo;
import ins.sino.claimcar.subrogation.vo.SubrogationCheckVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;


/**
 * @author ★YangKun
 * @CreateTime 2016年3月31日
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("subrogationHandleService")
public class SubrogationHandleServiceImpl implements SubrogationHandleService {
	@Autowired
	private PlatLockDubboService platLockDubboService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	SubrogationToPlatService subrogationToPlatService;
	@Autowired
	private CiClaimPlatformLogService platformLogService;
	@Autowired
	private CallPaymentWebService callPaymentWebService;
	@Autowired 
	private ClaimService claimService;
	@Autowired
	private SubrogationAddService subrogationAddService;
	
	@Autowired
	private SubrogationSHHandleService handleService;
	
	/**
	 * 展示追偿信息
	 * 1 调用结算接口，获取最新的结算金额,更新prplplatlock的sumRecoveryAmount
	 * 2 查询PrpLrecoveryOrPay 表获得追偿金额
	 * @param registNo
	 * @param recoveryCode
	 * @modified:
	 * ☆YangKun(2016年3月31日 下午9:24:21): <br>
	 */
	@Override
	public PrpLPlatLockVo sendBeforeRevoeryData(String registNo,String recoveryCode)throws Exception {
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		String comCode = registVo.getComCode();
		if(comCode.startsWith("22")){//上海机构单独处理	
//			String claimNo="";
//			ArrayList<PrpLrecoveryOrPayDto> list = (ArrayList<PrpLrecoveryOrPayDto>)blPrpLrecoveryOrPayFacade.findByConditions
//			(" recoveryCode = '"+recoveryCodeHref+"' and registNo = '"+registHref+"'");
//			if( list!=null && list.size()>0){
//				claimNo=list.get(0).getClaimNo();
//			}
//			
//			BLCIClaimDemandFacade claimdemandFacade=new BLCIClaimDemandFacade();
//			List<CIClaimDemandDto> demandList=(List)claimdemandFacade.findByConditions("registNo='"+registNo+"' and claimno='"+claimNo+"'");
//		
//			String claimCode="";
//			if( demandList!=null && demandList.size()>0){
//				claimCode=demandList.get(0).getClaimCode();
//				BLSubrogationInterfaceSHFacade shFacade=new BLSubrogationInterfaceSHFacade();
//				SubrogationCopyBackPacketRes pacRes=shFacade.getSubrogationCopyBack(registNo,claimCode,comCode);
//				prpLLockDataDto=this.convertSHSubrogationLockData(pacRes.getSubrogationCopyBackBodyRes(),registNo,recoveryCodeHref,prpLregistDto);
//				prpLrecoveryOrPayDto=this.convertSHSubrogationRecoveryData(pacRes.getSubrogationCopyBackBodyRes(),registNo,recoveryCodeHref,prpLregistDto);
//			}
			
		}
		else{//全国平台
			PrpLPlatLockVo platLockVo = platLockDubboService.findPlatLockVo(registNo,recoveryCode);
			
			List<AccountsInfoVo> accountsInfoList = subrogationToPlatService.sendAccountQuery(comCode,recoveryCode);
			if(accountsInfoList !=null && accountsInfoList.size()>0){
				double realReOrPayAmountRes = 0d;
				
				realReOrPayAmountRes = accountsInfoList.get(0).getCompensateAmount();
				//更新最新的清付金额 SumRecoveryAmount
				platLockVo.setSumRecoveryAmount(new BigDecimal(realReOrPayAmountRes));
				platLockDubboService.savePlatLock(platLockVo);
			}
			
			if(accountsInfoList ==null || accountsInfoList.isEmpty()){
				platLockVo.setAccountInfoFlag("1");
			}
			
			return platLockVo;
		}
		
		return null;
		
	}

	/**
	 * TODO  追回金额是否可手动修改
	 * @param registNo
	 * @param recoveryCode
	 * @param realReOrPayAmount
	 * @throws Exception
	 * @modified:
	 * ☆YangKun(2016年4月9日 下午4:55:16): <br>
	 */
	@Override
	public void recoveryConfirm(String registNo,String recoveryCode,Double realReOrPayAmount,SysUserVo userVo)throws Exception {
		PrpLPlatLockVo platLockVo = platLockDubboService.findPlatLockVo(registNo,recoveryCode);
		List<CopyInformationResultVo> resultVoList = new ArrayList<CopyInformationResultVo>();
		CopyInformationSubrogationViewVo copyInformationSubrogationViewVo=null;
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		String comCode = registVo.getComCode();
		CiClaimPlatformLogVo biciLog=null;
		BigDecimal recoverAmount=null;
//		String requestType = RequestType.RegistInfoBI.getCode();
//		CiClaimPlatformLogVo formLogVo = platformLogService.findLogByBussNo(requestType, registVo.getRegistNo(),registVo.getComCode());
		//上海工号走这yzy
		if(StringUtils.isNotBlank(comCode) && comCode.startsWith("22")){
			
			SubrogationSHQueryVo queryVo = new SubrogationSHQueryVo();
			if(platLockVo!=null){
				if("1101".equals(platLockVo.getRiskCode())){
					//查找理赔编码
					//交强
					biciLog = platformLogService.findLogByBussNo(RequestType.RegistInfoCI_SH.getCode(),platLockVo.getRegistNo(),comCode);
				}else{
					//商业
					biciLog = platformLogService.findLogByBussNo(RequestType.RegistInfoBI_SH.getCode(),platLockVo.getRegistNo(),comCode);
				}
				if(biciLog!=null){
					queryVo.setRegistNo(platLockVo.getRegistNo());
					queryVo.setClaimSeqNo(biciLog.getClaimSeqNo());
					queryVo.setComCode(comCode);
					
						resultVoList = handleService.sendCopyInformationToSubrogationSH(queryVo);
						if(resultVoList!=null && resultVoList.size()>0){
							for(CopyInformationResultVo vo:resultVoList){
								if(StringUtils.isNotBlank(vo.getSubrogationViewVo().getRecoveryCode())){
									if(vo.getSubrogationViewVo().getRecoveryCode().equals(platLockVo.getRecoveryCode())){
										copyInformationSubrogationViewVo=vo.getSubrogationViewVo();
										
									}
								}
							}
						}
		      }
				
	   }
			//送收付取被追偿方清付给我们的清付金额
		if(StringUtils.isNotBlank(copyInformationSubrogationViewVo.getCompensateAmount())){
		   recoverAmount=new BigDecimal(copyInformationSubrogationViewVo.getCompensateAmount());
		}else{
			recoverAmount=new BigDecimal(0);
		}
		
		
}
		
		String claimSequenceNo = platLockVo.getClaimSequenceNo();

		
		if(comCode.startsWith("22")){
			platLockVo.setSumRealAmount(recoverAmount);
		}else{
			recoverAmount = platLockVo.getSumRealAmount();// 追回金额
			if(platLockVo.getSumRecoveryAmount()==null){
				  platLockVo.setSumRecoveryAmount(new BigDecimal(0));
				}
				if (recoverAmount.compareTo(platLockVo.getSumRecoveryAmount())==1) {
					//追偿回款金额不得大于追偿金额
					recoverAmount = platLockVo.getSumRecoveryAmount();
				}

		}
		
		
		//TODO 不知道为啥要修改
		//blPrpLrecoveryOrPayFacade.updateRealReorpayamount(realReOrPayAmount,recoveryCode);

		if("3".equals(platLockVo.getRecoverOrPayStatus())){
			
			throw new IllegalArgumentException("该案件已经做过追偿回款确认");
			
		}else{
			//1	清算中  2	已结算  3	已到款
			platLockVo.setRecoverOrPayStatus("3");
			platLockDubboService.savePlatLock(platLockVo);
			
			//发送平台与收付解耦和
			if(comCode.startsWith("22")){
				
			}else{
				subrogationToPlatService.recoveryReturnConfirm(comCode,platLockVo,claimSequenceNo,recoverAmount);
			}
			
			//追偿回款确认送收付接口，未送收付的会送收付
			//prpTransSff.transRecoveryOrPayReturn(dbManager,prpLrecoveryOrPayDto.getRecoveryCode(),prpLrecoveryOrPayDto.getCompensateNo());
			reCoveryPayToPayment(platLockVo,registVo,userVo);
		}
	}

	
	
	/**
	 * 清算确认功能
	 * 更新platLock 清算状态字段 
	 * 发送收付接口
	 * @param registNo
	 * @param recoveryCode
	 * @param realAmount
	 * @throws Exception 
	 * @modified:
	 * ☆YangKun(2016年4月11日 下午4:01:22): <br>
	 */
	@Override
	public void qsConfirm(String recoveryCode,Double realAmount,SysUserVo userVo) throws Exception {
		PrpLPlatLockVo platLockVo = platLockDubboService.findPlatLockByRecoveryCode(recoveryCode);
		
		if(platLockVo == null){
			throw new IllegalArgumentException("结算码："+recoveryCode+"对应的锁定记录不存在");
		}
		PrpLRegistVo registVo = registQueryService.findByRegistNo(platLockVo.getRegistNo());
		
		//platLockVo.setSumRealAmount(new BigDecimal(realAmount));
		// 1 清算中 清算金额已确认
		// 2 已结算 案件已经清算
		// 3 已到款
		platLockVo.setRecoverOrPayStatus("2");
		platLockDubboService.savePlatLock(platLockVo);
		
		//transSff.transRecoveryOrPay(dbpool,iRecoveryCode,iCertiNo);
		//送收付
		qsConfirmToPayment(platLockVo,userVo);
	}
	
	/**
	 * 追偿送收付  P6C	追偿赔款
	 * @param platLockVo
	 * @throws Exception
	 */
	@Override
	public void reCoveryPayToPayment(PrpLPlatLockVo platLockVo,PrpLRegistVo registVo,SysUserVo userVo) throws Exception {
		JPlanMainVo jPlanMainVo = new JPlanMainVo();
		
		PrpLClaimVo claimVo = claimService.findClaimVoByRegistNoAndPolicyNo(platLockVo.getRegistNo(), platLockVo.getPolicyNo());
		List<PrpLRecoveryOrPayVo> recoveryList = platLockVo.getPrpLRecoveryOrPays();
		//TODO 重开赔案 赋值方式 待后续解决
		String compensateNo = recoveryList.get(0).getCompensateNo();
		
		jPlanMainVo.setCertiType("Z");
		jPlanMainVo.setCertiNo(compensateNo);
		jPlanMainVo.setPolicyNo(platLockVo.getPolicyNo());
		jPlanMainVo.setRegistNo(platLockVo.getRegistNo());
		jPlanMainVo.setClaimNo(claimVo.getClaimNo());
		jPlanMainVo.setOperateCode(userVo.getUserCode());
		jPlanMainVo.setOperateComCode(userVo.getComCode());
		jPlanMainVo.setPayComCode(registVo.getComCode());
		
		List<JplanFeeVo> jPlanFeeVos = new ArrayList<JplanFeeVo>();
		List<JplanFeeDetailVo> feeDetailVos = new ArrayList<JplanFeeDetailVo>();
		
		JplanFeeVo jPlanFeeVo = new JplanFeeVo();// 收付明细
		Integer serialNo = platLockVo.getPrpLRecoveryOrPays().get(0).getSerialNo();
		if (serialNo == null) {
			serialNo = 1;
		}
		jPlanFeeVo.setSerialNo(serialNo);
		jPlanFeeVo.setPayRefReason("P6C");
		jPlanFeeVo.setCurrency("CNY");
		jPlanFeeVo.setPlanFee(platLockVo.getSumRealAmount().doubleValue());
		jPlanFeeVo.setVoucherNo2(platLockVo.getRecoveryCode());
		jPlanFeeVo.setUnderWriteDate(claimVo.getEndCaseTime());
		
		JplanFeeDetailVo feeDetailVo = new JplanFeeDetailVo();
		feeDetailVo.setKindCode(CodeConstants.KINDCODE.KINDCODE_A);
		feeDetailVo.setPlanfee(platLockVo.getSumRealAmount().doubleValue());
		feeDetailVos.add(feeDetailVo);
		jPlanFeeVo.setJplanFeeDetailVos(feeDetailVos);
		
		jPlanFeeVos.add(jPlanFeeVo);
		jPlanMainVo.setJplanFeeVos(jPlanFeeVos);
		
		

		callPaymentWebService.callPaymentForClient(jPlanMainVo,BusinessType.Payment_dw,platLockVo.getRecoveryCode());
	}
	
	/**
	 * 清算 P6E
	 * @param platLockVo
	 * @throws Exception
	 */
	@Override
	public void qsConfirmToPayment(PrpLPlatLockVo platLockVo,SysUserVo userVo) throws Exception {
		JPlanMainVo jPlanMainVo = new JPlanMainVo();
		
		PrpLClaimVo claimVo = claimService.findClaimVoByRegistNoAndPolicyNo(platLockVo.getRegistNo(), platLockVo.getPolicyNo());
		List<PrpLRecoveryOrPayVo> recoveryList = platLockVo.getPrpLRecoveryOrPays();
		//TODO 重开赔案 赋值方式 待后续解决
		String compensateNo = recoveryList.get(0).getCompensateNo();
		
		jPlanMainVo.setCertiType("C");
		jPlanMainVo.setCertiNo(compensateNo);
		jPlanMainVo.setPolicyNo(platLockVo.getPolicyNo());
		jPlanMainVo.setRegistNo(platLockVo.getRegistNo());
		jPlanMainVo.setClaimNo(claimVo.getClaimNo());
		jPlanMainVo.setOperateCode(userVo.getUserCode());
		jPlanMainVo.setOperateComCode(userVo.getComCode());
		jPlanMainVo.setPayComCode(claimVo.getComCode());
		
		List<JplanFeeVo> jPlanFeeVos = new ArrayList<JplanFeeVo>();
		List<JplanFeeDetailVo> feeDetailVos = new ArrayList<JplanFeeDetailVo>();
		
		JplanFeeVo jPlanFeeVo = new JplanFeeVo();// 收付明细
		Integer serialNo = platLockVo.getPrpLRecoveryOrPays().get(0).getSerialNo();
		if (serialNo == null) {
			serialNo = 1;
		}
		jPlanFeeVo.setSerialNo(serialNo);
		jPlanFeeVo.setPayRefReason("P6E");
		jPlanFeeVo.setCurrency("CNY");
		jPlanFeeVo.setPlanFee(DataUtils.NullToZero(platLockVo.getSumRealAmount()).doubleValue());
		jPlanFeeVo.setVoucherNo2(platLockVo.getRecoveryCode());
		jPlanFeeVo.setUnderWriteDate(claimVo.getEndCaseTime());
		
		JplanFeeDetailVo feeDetailVo = new JplanFeeDetailVo();
		if(Risk.isDQZ(claimVo.getRiskCode())){
			feeDetailVo.setKindCode(CodeConstants.KINDCODE.KINDCODE_BZ);
		}else{
			feeDetailVo.setKindCode(CodeConstants.KINDCODE.KINDCODE_B);
		}
		feeDetailVo.setPlanfee(DataUtils.NullToZero(platLockVo.getSumRealAmount()).doubleValue());
		feeDetailVos.add(feeDetailVo);
		jPlanFeeVo.setJplanFeeDetailVos(feeDetailVos);
		
		jPlanFeeVos.add(jPlanFeeVo);
		jPlanMainVo.setJplanFeeVos(jPlanFeeVos);
		
	

		callPaymentWebService.callPaymentForClient(jPlanMainVo,BusinessType.Payment_qf,platLockVo.getRecoveryCode());
	}

	@Override
	public List<PrpLPlatCheckVo> findByOther(SubrogationCheckVo subrogationQuery) {
		return subrogationAddService.findByOther(subrogationQuery);
	}

	
}
