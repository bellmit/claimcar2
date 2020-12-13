package ins.sino.claimcar.claim.services;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.framework.web.bind.annotation.FormModel;
import ins.platform.utils.DataUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.PayFlagType;
import ins.sino.claimcar.CodeConstants.PolicyType;
import ins.sino.claimcar.CodeConstants.RadioValue;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.certify.service.CertifyPubService;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.po.PrpLCompensate;
import ins.sino.claimcar.claim.po.Prplcomcontext;
import ins.sino.claimcar.claim.service.CalculatorService;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.CompensateHandleService;
import ins.sino.claimcar.claim.vo.CompensateActionVo;
import ins.sino.claimcar.claim.vo.CompensateVo;
import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonFeeDetailVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonFeeVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.claim.vo.PrplcomcontextVo;
import ins.sino.claimcar.claim.vo.ThirdPartyDepreRate;
import ins.sino.claimcar.common.losscharge.service.LossChargeService;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.endcase.vo.PrpLReCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarSubRiskVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersInjuredVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceFeeVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropFeeVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.platform.util.SendPlatformUtil;
import ins.sino.claimcar.regist.service.PolicyQueryService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.regist.vo.PrpLcCarDeviceVo;
import ins.sino.claimcar.subrogation.platform.service.SubrogationToPlatService;
import ins.sino.claimcar.subrogation.po.PrpLPlatLock;
import ins.sino.claimcar.subrogation.service.PlatLockService;
import ins.sino.claimcar.subrogation.service.SubrogationService;
import ins.sino.claimcar.subrogation.sh.service.SubrogationSHHandleService;
import ins.sino.claimcar.subrogation.sh.vo.CopyInformationResultVo;
import ins.sino.claimcar.subrogation.sh.vo.CopyInformationSubrogationViewVo;
import ins.sino.claimcar.subrogation.sh.vo.SubrogationSHQueryVo;
import ins.sino.claimcar.subrogation.vo.BeSubrogationVo;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;
import ins.sino.claimcar.subrogation.vo.PrpLRecoveryOrPayVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationCarVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationPersonVo;
import ins.sino.claimcar.subrogation.vo.SubrogationQueryVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" })
@Path("compensateHandleService")
public class CompensateHandleServiceImpl implements CompensateHandleService {

	/** 交强理算标志位 */
	public static final String COMP_CI = "1";
	/** 商业理算标志位 */
	public static final String COMP_BI = "2";
	/** 交强险种前缀 */
	public static final String PREFIX_CI = "11";
	/** 商业险种前缀 */
	public static final String PREFIX_BI = "12";
	@Autowired
	private SubrogationService subrogationService;
	@Autowired
	private SubrogationToPlatService subrogationToPlatService;
	@Autowired
	private SubrogationSHHandleService subrogationSHHandleService;
	@Autowired
	private CiClaimPlatformLogService platformLogService;
	@Autowired
	private EndCasePubService endCasePubService;
	@Autowired
	private PlatLockService platLockService;
	@Autowired
	private CompensateService compensatateService;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	private CheckTaskService checkTaskService;

	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private ClaimService claimService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private ManagerService managerService;
	@Autowired
	private LossCarService lossCarService;

	@Autowired
	private PropTaskService propTaskService;
	@Autowired
	private PersTraceDubboService persTraceDubboService;
	@Autowired
	private LossChargeService lossChargeService;
	@Autowired
	private CalculatorService calculatorService;
	
	@Autowired
	private CertifyPubService certifyPubService;
	
	@Autowired
	private DatabaseDao databaseDao;
	
	@Autowired
	PolicyQueryService policyQueryService;

	/*
	 * @see
	 * ins.sino.claimcar.claim.services.CompensateHandleService#saveCompensateEdit
	 * (java.lang.Double, ins.sino.claimcar.claim.vo.PrpLCompensateVo,
	 * java.util.List, java.util.List, java.util.List, java.util.List,
	 * java.util.List, java.util.List, java.util.List, java.util.List,
	 * ins.platform.vo.SysUserVo)
	 * 
	 * @param flowTaskId
	 * 
	 * @param prpLCompensate
	 * 
	 * @param prpLLossItemVoList
	 * 
	 * @param prpLLossPropVoList
	 * 
	 * @param otherLossList
	 * 
	 * @param prpLLossPersonVoList
	 * 
	 * @param prpLChargeVoList
	 * 
	 * @param prpLPaymentVoList
	 * 
	 * @param claimDeductVoList
	 * 
	 * @param platLockVoList
	 * 
	 * @param userVo
	 */
	@Override
	public PrpLCompensateVo saveCompensateEdit(Double flowTaskId,
			PrpLCompensateVo prpLCompensate,
			List<PrpLLossItemVo> prpLLossItemVoList,
			List<PrpLLossPropVo> prpLLossPropVoList,
			List<PrpLLossPropVo> otherLossList,
			List<PrpLLossPersonVo> prpLLossPersonVoList,
			List<PrpLChargeVo> prpLChargeVoList,
			List<PrpLPaymentVo> prpLPaymentVoList,
			List<PrpLClaimDeductVo> claimDeductVoList,
			List<PrpLPlatLockVo> platLockVoList, SysUserVo userVo,
			List<PrpLCheckDutyVo> prpLCheckDutyVoList) {

		// 根据计算书查询是否已经存在
		// 存在就更新，不存在就保存
		prpLLossItemVoList.removeAll(Collections.singleton(null));
		prpLLossPropVoList.addAll(otherLossList);
		
		//排空
		prpLPaymentVoList.removeAll(Collections.singleton(null));
		prpLChargeVoList.removeAll(Collections.singleton(null));

		prpLCompensate.setPrpLLossItems(prpLLossItemVoList);
		prpLCompensate.setPrpLLossProps(prpLLossPropVoList);
		prpLCompensate.setPrpLLossPersons(prpLLossPersonVoList);
		int serialNo = 0;// serialNo 每次都用顺序的数据
		if (prpLPaymentVoList != null && !prpLPaymentVoList.isEmpty()) {
			for (PrpLPaymentVo paymentVo : prpLPaymentVoList) {
				paymentVo.setSerialNo(serialNo + "");
				serialNo++;
			}

		}

		if (prpLChargeVoList != null && !prpLChargeVoList.isEmpty()) {
			for (PrpLChargeVo chargeVo : prpLChargeVoList) {
				chargeVo.setSerialNo(serialNo + "");
				serialNo++;
			}

		}
		prpLCompensate.setPrpLCharges(prpLChargeVoList);
		prpLCompensate.setPrpLPayments(prpLPaymentVoList);

		// String compensateNo =
		// compensatateService.saveCompensates(prpLCompensate);
		String compensateNo = compensatateService.saveCompensates(
				prpLCompensate, prpLLossItemVoList, prpLLossPropVoList,
				prpLLossPersonVoList, prpLChargeVoList, prpLPaymentVoList,
				userVo);
		prpLCompensate.setCompensateNo(compensateNo);
		// 回写免赔率信息表PrpLClaimDeduct
		if (claimDeductVoList != null && claimDeductVoList.size() > 0) {
			compensatateService.updateDeductCondForComp(claimDeductVoList,
					prpLCompensate.getRegistNo());
		}
		// 回写checkDuty表
		/*
		 * if(!"1101".equals(prpLCompensate.getRiskCode())){ PrpLCheckDutyVo
		 * checkDutyVo =
		 * checkTaskService.findCheckDuty(prpLCompensate.getRegistNo
		 * (),1);//查询出标的车checkDutyVo
		 * checkDutyVo.setIndemnityDuty(prpLCompensate.getIndemnityDuty());
		 * checkDutyVo
		 * .setIndemnityDutyRate(prpLCompensate.getIndemnityDutyRate());
		 * checkTaskService.saveCheckDuty(checkDutyVo); }
		 */
		if(prpLCheckDutyVoList==null||prpLCheckDutyVoList.size()==0){
		    prpLCheckDutyVoList = checkTaskService.findCheckDutyByRegistNo(prpLCompensate.getRegistNo());
		    for(PrpLCheckDutyVo prpLCheckDutyVo : prpLCheckDutyVoList){
	            if(1==prpLCheckDutyVo.getSerialNo()){
	                prpLCheckDutyVo.setIndemnityDuty(prpLCompensate.getIndemnityDuty());
	                prpLCheckDutyVo.setIndemnityDutyRate(prpLCompensate.getIndemnityDutyRate());
	            }
	        }
		}
		
		checkTaskService.saveOrUpdateCheckDutyList(prpLCheckDutyVoList);
		checkTaskService.saveCheckDutyHis(prpLCompensate.getRegistNo(),
				FlowNode.Compe + prpLCompensate.getCompensateNo());

		// 代位金额保存
		platLockService.savePlatLockVoList(prpLCompensate, platLockVoList);

		return prpLCompensate;
	}

	/**
	 * 判断该保单是否承保了某个险别
	 * 
	 * <pre></pre>
	 * 
	 * @param kindCode
	 *            险别
	 * @param cMainVo
	 *            保单信息主表
	 * @return
	 * @modified: ☆Weilanlei(2016年7月15日 下午2:35:03): <br>
	 */
	private boolean HadBuyTheKind(String kindCode, PrpLCMainVo cMainVo) {
		boolean isHad = false;
		if (kindCode == null) {
			return isHad;
		} else {
			if (cMainVo.getPrpCItemKinds() != null
					&& cMainVo.getPrpCItemKinds().size() > 0) {
				for (PrpLCItemKindVo cItemKind : cMainVo.getPrpCItemKinds()) {
					if (kindCode.equals(cItemKind.getKindCode())) {
						isHad = true;
						break;
					}
				}
			}
		}

		return isHad;
	}

	/*
	 * @see
	 * ins.sino.claimcar.claim.services.CompensateHandleService#organizeRecoveryList
	 * (ins.sino.claimcar.regist.vo.PrpLRegistVo,
	 * ins.sino.claimcar.regist.vo.PrpLCMainVo,
	 * ins.sino.claimcar.claim.vo.PrpLClaimVo,
	 * ins.sino.claimcar.claim.vo.PrpLCompensateVo)
	 * 
	 * @param registVo
	 * 
	 * @param prpLcMainVo
	 * 
	 * @param claimVo
	 * 
	 * @param compVo
	 * 
	 * @return
	 */
	@Override
	public List<PrpLPlatLockVo> organizeRecoveryList(PrpLRegistVo registVo,
			PrpLCMainVo prpLcMainVo, PrpLClaimVo claimVo,
			PrpLCompensateVo compVo, SysUserVo userVo) {//yzy
		List<PrpLPlatLockVo> platLockList = new ArrayList<PrpLPlatLockVo>();

	
		PrpLEndCaseVo endCaseVo = endCasePubService.findEndCaseByClaimNo(claimVo.getClaimNo());
        if(!"6".equals(prpLcMainVo.getServices()) && !"5".equals(prpLcMainVo.getServices())){
		  if (endCaseVo != null || compVo != null){// 重开的情况 或者 已暂存
			  platLockList = platLockService.findPrpLPlatLockVoList(

					claimVo.getRegistNo(), claimVo.getPolicyNo(), null);
			if (platLockList != null && !platLockList.isEmpty()) {
				for (PrpLPlatLockVo platLockVo : platLockList) {
					List<PrpLRecoveryOrPayVo> recoveryList = platLockVo.getPrpLRecoveryOrPays();
					if (compVo != null) {
						for (PrpLRecoveryOrPayVo recoveryVo : recoveryList) {
							//yzy
							if (compVo.getCompensateNo().equals(recoveryVo.getCompensateNo())) {
								platLockVo.setThisPaid(recoveryVo.getRecoveryOrPayAmount());
								break;
							}
						}

					}
				}
			}

			return platLockList;
		 }
        }
//		PrpLSubrogationMainVo subrogationMainVo = subrogationService
//				.find(claimVo.getRegistNo());
//		List<PrpLSubrogationCarVo> subrogtaionCarList = subrogationMainVo
//				.getPrpLSubrogationCars();
//		
//		//平台进行锁定确认
//		if ( "1".equals(subrogationMainVo.getSubrogationFlag()) && !prpLcMainVo.getComCode().startsWith("22")
//				&& subrogtaionCarList != null && !subrogtaionCarList.isEmpty()) {
//			
//			List<PrpLPlatLockVo> platLockVo = subrogationService.findPrpLPlatLockVoByRegistNo(claimVo.getRegistNo(), null);
//			if (platLockVo == null) {
//				throw new IllegalArgumentException("代位求偿案件请先完成锁定确认!");
//			}
//		}

		String requestType = RequestType.RegistInfoBI.getCode();
		if (Risk.isDQZ(claimVo.getRiskCode())) {// 交强险
			if (prpLcMainVo.getComCode().startsWith("22")) {
				requestType = RequestType.RegistInfoCI_SH.getCode();
			} else {
				requestType = RequestType.RegistInfoCI.getCode();
			}
		} else {
			if (prpLcMainVo.getComCode().startsWith("22")) {
				requestType = RequestType.RegistInfoBI_SH.getCode();
			}
		}
		boolean isSendToPlatform = true;
		isSendToPlatform=SendPlatformUtil.isMor(prpLcMainVo);
		if( !CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())&& !CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())
				&& isSendToPlatform){

			CiClaimPlatformLogVo formLogVo = platformLogService.findLogByBussNo(requestType,claimVo.getRegistNo(),prpLcMainVo.getComCode());
			if(formLogVo==null||StringUtils.isEmpty(formLogVo.getClaimSeqNo())){
				throw new IllegalArgumentException("该案件平台理赔编码为空,请核查");
			}

			if(prpLcMainVo.getComCode().startsWith("22")){// 上海平台，代位
				SubrogationSHQueryVo queryVo = new SubrogationSHQueryVo();
				queryVo.setComCode(prpLcMainVo.getComCode());
				queryVo.setRegistNo(claimVo.getRegistNo());//
				queryVo.setClaimSeqNo(formLogVo.getClaimSeqNo());
				// 调用上海平台的 -- 代位求偿信息抄回接口信息查询yzy
				List<CopyInformationResultVo> resultVo = null;
				try{
					resultVo = subrogationSHHandleService.sendCopyInformationToSubrogationSH(queryVo);
					// if(resultVo!=null && resultVo.size()>0){
					//
					// for(CopyInformationResultVo vo:resultVo){
					// if("0".equals(vo.getResponseCode())){
					// if("1".equals(subrogationMainVo.getSubrogationFlag()) && !"1101".equals(claimVo.getRiskCode())){
					//
					// throw new IllegalArgumentException("代位求偿案件请先完成锁定确认!");
					// }
					// }else if("1".equals(vo.getResponseCode())){
					// String sign="0";//我方是追偿方，看是否锁定--0未锁定，1已锁定
					// if("1".equals(subrogationMainVo.getSubrogationFlag()) && !"1101".equals(claimVo.getRiskCode())){
					// for(CopyInformationResultVo cvo:resultVo){
					// if(cvo.getSubrogationViewVo().getInsurerCode().startsWith("EDHI")){
					// sign="1";
					// }
					// }
					// if("0".equals(sign)){
					// throw new IllegalArgumentException("代位求偿案件请先完成锁定确认!");
					// }
					// }
					// }else{
					//
					// }
					// }
					// }
				}catch(IllegalArgumentException e){
					throw new IllegalArgumentException(e.getMessage());
				}catch(Exception e){
					throw new IllegalArgumentException("调用上海平台代位求偿信息抄回接口信息查询异常！");
				}
				// 此时存锁定表信息
				platLockList = getBeSubrogationListSH(formLogVo.getClaimSeqNo(),claimVo,registVo,resultVo,userVo);
			}else{

				SubrogationQueryVo queryVo = new SubrogationQueryVo();
				queryVo.setComCode(prpLcMainVo.getComCode());
				queryVo.setRegistNo(claimVo.getRegistNo());
				queryVo.setClaimCode(formLogVo.getClaimSeqNo());
				queryVo.setConfirmSequenceNo(prpLcMainVo.getValidNo());// 投保确认码
				try{
					if(Risk.isDQZ(claimVo.getRiskCode())){// 交强险
						List<BeSubrogationVo> subrogationRiskList = subrogationToPlatService.sendBeSubrogationQueryCI(queryVo);
						if(subrogationRiskList!=null&& !subrogationRiskList.isEmpty()){
							platLockList = getBeSubrogationList(formLogVo.getClaimSeqNo(),claimVo,registVo,subrogationRiskList,userVo);
						}

					}else{
						List<BeSubrogationVo> subrogationRiskList = subrogationToPlatService.sendBeSubrogationQueryBI(queryVo);
						if(subrogationRiskList!=null&& !subrogationRiskList.isEmpty()){
							platLockList = getBeSubrogationList(formLogVo.getClaimSeqNo(),claimVo,registVo,subrogationRiskList,userVo);
						}
						// 查询追偿的数据
						List<PrpLPlatLockVo> recoveryLockList = platLockService.findPrpLPlatLockVoList(claimVo.getRegistNo(),claimVo.getPolicyNo(),
								"1");
						if(recoveryLockList!=null&& !recoveryLockList.isEmpty()){
							platLockList.addAll(recoveryLockList);
						}
					}

				}catch(Exception e){
					e.printStackTrace();
					throw new IllegalArgumentException("调用平台接口异常！");
				}
			}
		}
		return platLockList;
	}

	/**
	 * 转换上海平台的代位抄回信息
	 * ☆Luwei(2017年3月7日 下午8:18:47): <br>
	 */
	private List<BeSubrogationVo> transformationCopyInfo(List<CopyInformationResultVo> resultVo) {
		List<BeSubrogationVo> beSubrogationVo = new ArrayList<BeSubrogationVo>();
		if (resultVo != null && !resultVo.isEmpty()) {
			for (CopyInformationResultVo result : resultVo) {
				//代位求偿信息列表（多条）
				CopyInformationSubrogationViewVo subrogationListVo = result.getSubrogationViewVo();
				if (!RadioValue.RADIO_YES.equals(result) && subrogationListVo != null) {//返回成功的数据
					
				/*if (!"9".equals(subrogationListVo.getRecoveryCodeStatus())) {//排除清算码已失效的
*/						BeSubrogationVo subrogationVo = new BeSubrogationVo();
						Beans.copy().from(subrogationListVo).to(subrogationVo);//拷贝
						//转换标志字段。。。
						beSubrogationVo.add(subrogationVo);
					//}
				}
			}
		}
		return beSubrogationVo;
	}
	
	private List<PrpLPlatLockVo> getBeSubrogationList(String claimSequenceNo,
			PrpLClaimVo claimVo, PrpLRegistVo registVo,
			List<BeSubrogationVo> queryResultDataList, SysUserVo userVo) {

		ArrayList<PrpLPlatLockVo> platLockList = new ArrayList<PrpLPlatLockVo>();
		if (queryResultDataList != null && queryResultDataList.size() > 0) {
			int index = 1;
			for (BeSubrogationVo queryResultData : queryResultDataList) {
				PrpLPlatLockVo platLockVo = new PrpLPlatLockVo();

				platLockVo.setRecoveryCode(queryResultData.getRecoveryCode());
				platLockVo.setClaimSequenceNo(claimSequenceNo);
				platLockVo.setRegistNo(claimVo.getRegistNo());
				platLockVo.setPolicyNo(claimVo.getPolicyNo());
				platLockVo.setRiskCode(claimVo.getRiskCode());
				platLockVo.setLicenseNo(registVo.getPrpLRegistExt()
						.getLicenseNo());
				platLockVo.setInsureName(registVo.getPrpLRegistExt()
						.getInsuredName());
				// RecoveryOrPayFlag
				platLockVo.setOppoentRegistNo(queryResultData
						.getClaimNotificationNo());
				platLockVo.setOppoentPolicyNo(queryResultData.getPolicyNo());
				platLockVo.setOppoentInsurerCode(queryResultData
						.getInsurerCode());
				platLockVo.setOppoentInsurerArea(queryResultData
						.getInsurerArea());
				platLockVo.setCoverageType(queryResultData.getCoverageType());
				if (StringUtils.isEmpty(queryResultData.getLicensePlateNo())) {
					List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService.findLossCarMainByRegistNo(claimVo.getRegistNo());
					for (PrpLDlossCarMainVo carMainVo : lossCarMainVos) {
						if (carMainVo.getSerialNo() > 1) {
							PrpLDlossCarInfoVo carInfoVo = carMainVo.getLossCarInfoVo();
							if (queryResultData.getVin() != null) {
								if (queryResultData.getVin().equals(
										carInfoVo.getVinNo())) {
									platLockVo.setOppoentLicensePlateNo(carInfoVo.getLicenseNo());
								} else if (queryResultData.getEngineNo().equals(carInfoVo.getEngineNo())) {
									platLockVo.setOppoentLicensePlateNo(carInfoVo.getLicenseNo());
								}
							}
						}
					}
				} else {
					platLockVo.setOppoentLicensePlateNo(queryResultData.getLicensePlateNo());
				}
				platLockVo.setOppoentLicensePlateType(queryResultData.getLicensePlateType());
				platLockVo.setOppoentEngineNo(queryResultData.getEngineNo());
				platLockVo.setOppoentVin(queryResultData.getVin());
				platLockVo.setLossTime(registVo.getDamageTime());
				platLockVo.setLossArea("");
				platLockVo.setLossDesc("");
				platLockVo.setRecoveryOrPayFlag("2");
				if (Risk.isDQZ(claimVo.getRiskCode())) {
					platLockVo.setRecoveryOrPayType(CodeConstants.CoverageType.CI_DQZ);//
				} else {
					platLockVo.setRecoveryOrPayType(CodeConstants.CoverageType.BI_B);
				}
				platLockVo.setRecoveryCodeStatus(queryResultData
						.getRecoveryCodeStatus());
				platLockVo.setLockTime(queryResultData.getLockedTime());
				platLockVo.setComCode(registVo.getComCode());
				// platLockVo.setLockedOperateCode(user.getUserCode());
				// platLockVo.setLockedOperateName(user.getUserName());

				List<PrpLRecoveryOrPayVo> recoveryVoList = new ArrayList<PrpLRecoveryOrPayVo>();
				PrpLRecoveryOrPayVo recoveryVo = new PrpLRecoveryOrPayVo();
				recoveryVo.setRecoveryCode(queryResultData.getRecoveryCode());
				recoveryVo.setRegistNo(registVo.getRegistNo());
				recoveryVo.setSerialNo(index);
				recoveryVo.setValidFlag("1");
				recoveryVo.setComCode(registVo.getComCode());
				recoveryVo.setCreateTime(new Date());
				recoveryVo.setCreateUser(userVo.getUserCode());
				
				List<PrpLReCaseVo> reCaseList = endCasePubService.findReCaseListByClaimNo(claimVo.getClaimNo());
				if (reCaseList != null && !reCaseList.isEmpty()) {
					recoveryVo.setTimes(reCaseList.size() + 1);
				} else {
					recoveryVo.setTimes(1);
				}

				// List<PrpLrecoveryOrPayDto> list =
				// (List<PrpLrecoveryOrPayDto>)blPrpLrecoveryOrPayFacade.findByConditions(condition);
				// for(int j = 0; j<list.size(); j++ ){
				// PrpLrecoveryOrPayDto prpLrecoveryOrPayDto1 = new
				// PrpLrecoveryOrPayDto();
				// prpLrecoveryOrPayDto1 = list.get(j);
				// // 如果是互审中状态，进行互审查询，更新互审状态 20120823 by lianlg start
				// if(queryResultData.getRecoveryCodeStatus().equals("3")){
				// BLCheckAccountsInterfaceFacade blCheckAccountsInterfaceFacade
				// = new BLCheckAccountsInterfaceFacade();
				// CheckEachQueryBody checkEachQueryBody = new
				// CheckEachQueryBody();
				// CheckEachQueryBasePart checkEachQueryBasePart = new
				// CheckEachQueryBasePart();
				// checkEachQueryBasePart.setAccountsNo(queryResultData.getRecoveryCode());//
				// 清算码
				// checkEachQueryBasePart.setCheckOwnType("2");// 审核方类型:责任对方
				// // checkEachQueryBasePart.setCheckStats(CheckStats);
				// System.out.println("****************开始进行互审查询****************");
				// try{
				// List<CheckEachQueryBasePartRes> checkEachQueryList =
				// blCheckAccountsInterfaceFacade.getCheckEachQuery(checkEachQueryBasePart,
				// prpLregistDto.getComCode());
				// if(checkEachQueryList!=null&&checkEachQueryList.size()>0){
				// prpLrecoveryOrPayDto1.setQsStatus(checkEachQueryList.get(0).getCheckStats());//
				// 更新互审状态
				// }
				// }catch(Exception e){
				// System.out.println("********"+e.getMessage()+"********");
				// }
				// System.out.println("****************互审查询结束****************");
				//
				// }
				// // 如果是互审中状态，进行互审查询，更新互审状态 20120823 by lianlg end
				// prpLrecoveryOrPayDto1.setRecoveryCodeStatus(queryResultData.getRecoveryCodeStatus());
				// prpLrecoveryOrPayDto1.setRecoveryCodeStatusName(uiCodeAction.translateCodeCode("DWRecoveryCodeStatus",queryResultData.getRecoveryCodeStatus(),true));
				// blPrpLrecoveryOrPayFacade.update(prpLrecoveryOrPayDto1,
				// " RecoveryCode = '"+prpLrecoveryOrPayDto1.getRecoveryCode()+"' And Times = '"+prpLrecoveryOrPayDto1.getTimes()+"'");
				// }
				recoveryVoList.add(recoveryVo);
	            platLockVo.setPrpLRecoveryOrPays(recoveryVoList);
               try{
            	   platLockService.firstSavePlatLock(platLockVo);
               }catch(Exception e){
            	   e.printStackTrace();
               }
				

				platLockList.add(platLockVo);
			}
		}
		return platLockList;
	}
	
	
	private List<PrpLPlatLockVo> getBeSubrogationListSH(String claimSequenceNo,PrpLClaimVo claimVo, PrpLRegistVo registVo,List<CopyInformationResultVo> resultVos, SysUserVo userVo) {

		ArrayList<PrpLPlatLockVo> platLockList = new ArrayList<PrpLPlatLockVo>();
		if (resultVos != null && resultVos.size() > 0) {
			for (CopyInformationResultVo result : resultVos) {
				CopyInformationSubrogationViewVo subrogationListVo = result.getSubrogationViewVo();
				if(subrogationListVo == null) continue;
				int index = 1;
				PrpLPlatLockVo platLockVo = new PrpLPlatLockVo();
				platLockVo.setClaimSequenceNo(claimSequenceNo);
				platLockVo.setRegistNo(claimVo.getRegistNo());
				platLockVo.setPolicyNo(claimVo.getPolicyNo());
				platLockVo.setRiskCode(claimVo.getRiskCode());
				platLockVo.setLicenseNo(registVo.getPrpLRegistExt().getLicenseNo());
				platLockVo.setInsureName(registVo.getPrpLRegistExt().getInsuredName());
				platLockVo.setRecoveryCode(subrogationListVo.getRecoveryCode());
				platLockVo.setLossTime(registVo.getDamageTime());
				platLockVo.setLossArea("");
				platLockVo.setLossDesc("");
				if (Risk.isDQZ(claimVo.getRiskCode())) {
					platLockVo.setRecoveryOrPayType(CodeConstants.CoverageType.CI_DQZ);//
				} else {
					platLockVo.setRecoveryOrPayType(CodeConstants.CoverageType.BI_B);
				}
				platLockVo.setRecoveryCodeStatus(subrogationListVo.getRecoveryCodeStatus());
				platLockVo.setLockTime(subrogationListVo.getLockedTime());
				platLockVo.setComCode(registVo.getComCode());
				
				if(subrogationListVo.getInsurerCode().startsWith("EDHI")){//我方为追偿方
					platLockVo.setRecoveryOrPayFlag("1");
					//存对方责任方信息
					platLockVo.setOppoentRegistNo(subrogationListVo.getBerecoveryClaimNotificationNo());
					platLockVo.setOppoentPolicyNo(subrogationListVo.getBerecoveryPolicyNo());
					platLockVo.setOppoentInsurerCode(subrogationListVo.getBerecoveryInsurerCode());
					platLockVo.setOppoentInsurerArea(subrogationListVo.getBerecoveryInsurerArea());
					platLockVo.setCoverageType(subrogationListVo.getBerecoveryCoverageType());
					platLockVo.setOppoentLicensePlateNo(subrogationListVo.getBerecoveryLicensePlateNo());
					platLockVo.setOppoentLicensePlateType(subrogationListVo.getBerecoveryLicensePlateType());
					platLockVo.setOppoentEngineNo(subrogationListVo.getBerecoveryEngineNo());
					platLockVo.setOppoentVin(subrogationListVo.getBerecoveryVin());
				}else{//我方为责任方
					platLockVo.setRecoveryOrPayFlag("2");
					//存对方追偿方信息
					platLockVo.setOppoentRegistNo(subrogationListVo.getClaimNotificationNo());
					platLockVo.setOppoentPolicyNo(subrogationListVo.getPolicyNo());
					platLockVo.setOppoentInsurerCode(subrogationListVo.getInsurerCode());
					platLockVo.setOppoentInsurerArea(subrogationListVo.getInsurerArea());
					platLockVo.setCoverageType(subrogationListVo.getCoverageType());
					platLockVo.setOppoentLicensePlateNo(subrogationListVo.getLicensePlateNo());
					platLockVo.setOppoentLicensePlateType(subrogationListVo.getLicensePlateType());
					platLockVo.setOppoentEngineNo(subrogationListVo.getEngineNo());
					platLockVo.setOppoentVin(subrogationListVo.getVin());
				}
				
				List<PrpLRecoveryOrPayVo> recoveryVoList = new ArrayList<PrpLRecoveryOrPayVo>();
				PrpLRecoveryOrPayVo recoveryVo = new PrpLRecoveryOrPayVo();
				recoveryVo.setRecoveryCode(subrogationListVo.getRecoveryCode());
				recoveryVo.setRegistNo(registVo.getRegistNo());
				recoveryVo.setSerialNo(index++);
				recoveryVo.setValidFlag("1");
				recoveryVo.setComCode(registVo.getComCode());
				recoveryVo.setCreateTime(new Date());
				recoveryVo.setCreateUser(userVo.getUserCode());

				List<PrpLReCaseVo> reCaseList = endCasePubService.findReCaseListByClaimNo(claimVo.getClaimNo());
				if (reCaseList != null && !reCaseList.isEmpty()) {
					recoveryVo.setTimes(reCaseList.size() + 1);
				} else {
					recoveryVo.setTimes(1);
				}
				recoveryVoList.add(recoveryVo);
				platLockVo.setPrpLRecoveryOrPays(recoveryVoList);
				try {
					platLockService.firstSavePlatLock(platLockVo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				platLockList.add(platLockVo);
				index++;
			}
		}
		return platLockList;
	}

	public CompensateActionVo compensateEdit(Double flowTaskId, SysUserVo userVo) throws Exception {

		CompensateActionVo actionVo = new CompensateActionVo();

		String flag = COMP_BI;// 判断是商业还是交强计算书
		String bcFlag = PREFIX_BI;// 11-交强 12-商业
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
		if (wfTaskVo.getSubNodeCode().equals("CompeCI")) {
			flag = COMP_CI;
			bcFlag = PREFIX_CI;
		}

		String compensateNo = "";
		String registNo = wfTaskVo.getRegistNo();
		if (wfTaskVo.getCompensateNo() != null) {
			compensateNo = wfTaskVo.getCompensateNo();
		} else {
			compensateNo = wfTaskVo.getHandlerIdKey();
		}

		// 是否代位求偿 1-是 0-否
		String subRogationFlag = "0";
		// 是否互碰自赔 1-是 0-否
		String caseFlag = "0";
		List<PrpLClaimVo> claimVos = claimService.findClaimListByRegistNo(registNo);
		PrpLClaimVo reClaimVo = new PrpLClaimVo();
		for (PrpLClaimVo claimVo : claimVos) {
			if (claimVo.getRiskCode().substring(0, 2).equals(bcFlag)) {
				reClaimVo = claimVo;// 获取立案主表
				if (StringUtils.isNotEmpty(reClaimVo.getCaseFlag())) {
					if (reClaimVo.getCaseFlag().equals("1")) {
						caseFlag = "1";
					}
				}
				if (StringUtils.isNotEmpty(reClaimVo.getIsSubRogation())) {
					if (reClaimVo.getIsSubRogation().equals("1")) {
						subRogationFlag = "1";
					}
				}
			}
		}
		actionVo.setCompFlag(flag);
		actionVo.setBcFlag(bcFlag);
		actionVo.setCaseFlag(caseFlag);
		actionVo.setFlowTaskId(flowTaskId);
		actionVo.setSubRogationFlag(subRogationFlag);
		actionVo.setWfTaskVo(wfTaskVo);
		actionVo.setHandlerStatus(wfTaskVo.getHandlerStatus());

		// 报案信息主表
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		actionVo.setRegistVo(registVo);
		PrpLCMainVo cMainVo = policyViewService.getPrpLCMainByRegistNoAndPolicyNo(registNo,reClaimVo.getPolicyNo());
		List<PrpLCItemKindVo> itemKindVoList = cMainVo.getPrpCItemKinds();
		List<PrpLCheckDutyVo> checkDutyList = checkTaskService.findCheckDutyByRegistNo(registNo);

		PrpLCompensateVo compVo = null;
		if (compensateNo != null) {
			compVo = compensatateService.findCompByPK(compensateNo);
		}
		if (wfTaskVo.getCompensateNo() != null && compVo == null) {
			// 工作流计算书号不为空，但业务表不存在对应业务数据时，抛出异常不初始化
			throw new IllegalArgumentException("数据异常，请联系管理员");
		}

		Map<String, String> carMap = new HashMap<String, String>();// 车辆信息

		for (PrpLCheckDutyVo checkDutyVo : checkDutyList) {
			carMap.put(checkDutyVo.getLicenseNo(), checkDutyVo.getSerialNo().toString());
		}
		actionVo.setCarMap(carMap);

		// 是否为新条款 重新赋值保额
		boolean cprcCase = false;
		if (!"1101".equals(cMainVo.getRiskCode())) {
			cprcCase = CodeConstants.ISNEWCLAUSECODE_MAP.get(cMainVo.getRiskCode());
		}
		BigDecimal amountKindA = BigDecimal.ZERO;
		if (cprcCase) {
			if (itemKindVoList != null && !itemKindVoList.isEmpty()) {
				for (PrpLCItemKindVo itemKindVo : itemKindVoList) {
					if (CodeConstants.KINDCODE.KINDCODE_A.equals(itemKindVo.getKindCode().trim())) {
						amountKindA = itemKindVo.getAmount();
					}
				}
				for (PrpLCItemKindVo itemKindVo : itemKindVoList) {
					if (CodeConstants.KINDCODE.KINDCODE_X1.equals(itemKindVo.getKindCode().trim())
							|| CodeConstants.KINDCODE.KINDCODE_F.equals(itemKindVo.getKindCode().trim())
							|| CodeConstants.KINDCODE.KINDCODE_NT.equals(itemKindVo.getKindCode().trim())) {
						itemKindVo.setAmount(amountKindA);
					}
				}
			}
		}
		actionVo.setAmountKindA(amountKindA);
		if (flag.equals(COMP_BI)) {
			boolean noFindThirdCond = false;
			// 获取不计免赔附加险
			List<PrpLCItemKindVo> claimKindMList = compensatateService.getDeductOffKindList(registNo);
			actionVo.setClaimKindMList(claimKindMList);
			actionVo.setCprcCase(cprcCase);
			// modelAndView.addObject("claimKindMList", claimKindMList);
			// modelAndView.addObject("cprcCase", cprcCase);

			// 获取免赔条件
			List<PrpLClaimDeductVo> claimDeductVos = compensatateService.findDeductCond(registNo, registVo.getRiskCode());
			actionVo.setClaimDeductVos(claimDeductVos);

			// modelAndView.addObject("claimDeductVos", claimDeductVos);
			if (compVo == null && claimDeductVos != null
					&& !claimDeductVos.isEmpty()) {
				for (PrpLClaimDeductVo claimDeductVo : claimDeductVos) {
					if ("1".equals(claimDeductVo.getIsCheck())
							&& ("120".equals(claimDeductVo.getDeductCondCode()) 
									|| "320".equals(claimDeductVo.getDeductCondCode()))) {
						noFindThirdCond = true;
					}
				}
			}
			actionVo.setNoFindThirdCond(noFindThirdCond);

			List<PrpLcCarDeviceVo> carDeviceList = cMainVo.getPrpLcCarDevices();
			if (carDeviceList != null && !carDeviceList.isEmpty()) {
				Map<String, String> deviceMap = new HashMap<String, String>();
				for (PrpLcCarDeviceVo carDeviceVo : carDeviceList) {
					deviceMap.put(carDeviceVo.getDeviceId().toString(),carDeviceVo.getDeviceName());
					actionVo.setDeviceMap(deviceMap);
					// modelAndView.addObject("deviceMap", deviceMap);
				}
			}
		}

		// 理算中如果有车上乘客险的 要校验车上人员数据小于低于承保的人数
		BigDecimal personCount = new BigDecimal(0);
		for (PrpLCItemKindVo itemKind : cMainVo.getPrpCItemKinds()) {
			if (itemKind.getKindCode().equals("D12")) {
				personCount = itemKind.getQuantity();
			}
			/*
			 * itemKind.getKindCode() sum=sum.add(itemKind.getQuantity());
			 */
		}
		actionVo.setPersonCount(personCount);
		//
		BigDecimal unitamount = new BigDecimal(0);
		for (PrpLCItemKindVo itemKind : cMainVo.getPrpCItemKinds()) {
			if (itemKind.getKindCode().equals("RF")
					|| itemKind.getKindCode().equals("T")
					|| itemKind.getKindCode().equals("C")) {
				unitamount = itemKind.getUnitAmount();
			}
		}
		actionVo.setUnitamount(unitamount);
		// modelAndView.addObject("unitamount", unitamount);

		// 垫付信息表
		String prePaidFlag = "0";
		if (compVo != null) {
			BigDecimal sumPre = DataUtils.NullToZero(compVo.getSumPreAmt())
					.add(DataUtils.NullToZero(compVo.getSumPreFee()));
			String handlerStatus = wfTaskVo.getHandlerStatus();
			if (sumPre.compareTo(BigDecimal.ZERO) == 1
					&& (CodeConstants.WorkStatus.END.equals(handlerStatus) 
							|| CodeConstants.WorkStatus.CANCEL.equals(handlerStatus))) {// 已赔付或者注销的情况
				prePaidFlag = "1";
			}

		}
		List<PrpLPadPayPersonVo> padPayPersons = new ArrayList<PrpLPadPayPersonVo>();
		if (flag.equals(COMP_CI)) {// 交强垫付
			padPayPersons = compensatateService.getPadPayPersons(registNo,prePaidFlag);
			actionVo.setPadPayPersons(padPayPersons);
			// modelAndView.addObject("prpLPadPayPersons", padPayPersons);
		}

		// 获取预付赔款和费用表
		Map<String, List<PrpLPrePayVo>> prePayMap = compensatateService.getPrePayMap(reClaimVo.getClaimNo(), bcFlag, prePaidFlag);
		// modelAndView.addObject("prpLPrePayP", PrePayMap.get("prePay"));
		// modelAndView.addObject("prpLPrePayF", PrePayMap.get("preFee"));
		actionVo.setPrpLPrePayF(prePayMap.get("preFee"));
		actionVo.setPrpLPrePayP(prePayMap.get("prePay"));

		// 锁定校验 代位求偿案件请先完成锁定确认 TODO 代位接口未调通 暂时不放开
		List<PrpLPlatLockVo> platLockList = new ArrayList<PrpLPlatLockVo>();

		PrpLCItemCarVo itemCarVo = cMainVo.getPrpCItemCars().get(0);
		String carKindCode = itemCarVo.getCarKindCode();
		// 摩托车 拖拉机不送平台
		//非上海机构 1203 1208 1209 不传平台 及非上海机构的摩托车拖拉机的交强险1101也不送平台
		boolean dwPersFlag = false;
		PrpLSubrogationMainVo PrpLSubrogationMainVo = subrogationService.find(registNo);
		List<PrpLSubrogationPersonVo> subrogationPersonVoList = PrpLSubrogationMainVo.getPrpLSubrogationPersons();
		if(CodeConstants.CommonConst.TRUE.equals(PrpLSubrogationMainVo.getSubrogationFlag()) &&
				subrogationPersonVoList!=null&&subrogationPersonVoList.size()>0){
			dwPersFlag = true;
		}
		if(!dwPersFlag&&cMainVo.getComCode().startsWith("22")){
			cMainVo.setServices(wfTaskVo.getWorkStatus());//为了判定是不是核赔退回理算
			platLockList = this.organizeRecoveryList(registVo, cMainVo,reClaimVo, compVo, userVo);

		}else{
			if (!dwPersFlag && !(Risk.isDBC(cMainVo.getRiskCode()) ) && (!Risk.isDBT(cMainVo.getRiskCode())) && (!Risk.isDAC(cMainVo.getRiskCode()))
					&&( StringUtils.isBlank(carKindCode) ||  "J,M".indexOf(carKindCode.substring(0, 1)) == -1 )){
                    platLockList = this.organizeRecoveryList(registVo, cMainVo,reClaimVo, compVo, userVo);
				}
		
		}

		if (compVo != null) {
			this.getCompensate(actionVo, wfTaskVo, compVo, platLockList,itemKindVoList);
		} else {
			this.initCompensate(actionVo, wfTaskVo, compVo, reClaimVo,
					platLockList, itemKindVoList, checkDutyList, prePayMap,
					padPayPersons, cMainVo);
		}
		//判断是否实收保费
		String[] plyNoArr = {cMainVo.getPolicyNo()};
		Set<Map<String,String>> sets = policyQueryService.findPrpjpayrefrecBySQL(plyNoArr);
		if(sets !=null && !sets.isEmpty()){
			for(Map<String,String> map3:sets){
				actionVo.setPayrefFlag(map3.get("payrefflag"));
			}
		}else{
			actionVo.setPayrefFlag("0");
		}
		
		return actionVo;
	}

	private void initCompensate(CompensateActionVo actionVo,
			PrpLWfTaskVo wfTaskVo, PrpLCompensateVo compVo,
			PrpLClaimVo reClaimVo, List<PrpLPlatLockVo> platLockList,
			List<PrpLCItemKindVo> itemKindVoList,
			List<PrpLCheckDutyVo> checkDutyList,
			Map<String, List<PrpLPrePayVo>> prePayMap,
			List<PrpLPadPayPersonVo> padPayPersons, PrpLCMainVo cMainVo) {

 		String bcFlag = actionVo.getBcFlag();
		String caseFlag = actionVo.getCaseFlag();
		boolean haveKindNT = false;// 是否包含无法找到第三方条款
		/* 初始化时带入保存的被保险人数据 */
		String registNo = wfTaskVo.getRegistNo();
		String flag = actionVo.getCompFlag();
		PrpLPayCustomVo prpLPayCustomVo = null;
		List<PrpLPayCustomVo> payList = managerService
				.findPayCustomVoByRegistNo(wfTaskVo.getRegistNo());
		if (payList != null && payList.size() > 0) {
			for (PrpLPayCustomVo customVo : payList) {
				if (customVo.getPayObjectKind().equals("2")) {
					prpLPayCustomVo = customVo;
					break;
				}
			}
		}

		List<PrpLPaymentVo> prpLPaymentVoList = null;
		if (prpLPayCustomVo != null) {
			String AccountNo1 = prpLPayCustomVo.getAccountNo();
			String AccountNo2 = null;
			if (!AccountNo1.isEmpty() && AccountNo1.length() >= 4) {
				AccountNo2 = AccountNo1.substring(AccountNo1.length() - 4,
						AccountNo1.length());
			} else {
				AccountNo2 = AccountNo1;
			}
			PrpLPaymentVo prpLPaymentVo = new PrpLPaymentVo();
			prpLPaymentVoList = new ArrayList<PrpLPaymentVo>();
			prpLPaymentVo.setPayeeName(prpLPayCustomVo.getPayeeName() + "-" + AccountNo2);
			prpLPaymentVo.setPayeeId(prpLPayCustomVo.getId());
			prpLPaymentVo.setAccountNo(prpLPayCustomVo.getAccountNo());
			prpLPaymentVo.setBankName(prpLPayCustomVo.getBankOutlets());
			prpLPaymentVo.setOtherFlag("0");
			prpLPaymentVo.setPayObjectKind(prpLPayCustomVo.getPayObjectKind());
			prpLPaymentVoList.add(prpLPaymentVo);

			actionVo.setPrpLId(prpLPayCustomVo.getId());
			// modelAndView.addObject("prpLId", prpLPayCustomVo.getId());

		}

		/**
		 * 车辆损失信息
		 */
		List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService
				.findLossCarMainByRegistNo(registNo);
		PrpLDlossCarMainVo mainLossCarVo = null;
		List<PrpLDlossCarMainVo> reLossCarMainVos = new ArrayList<PrpLDlossCarMainVo>();
		if (lossCarMainVos != null) {
			for (PrpLDlossCarMainVo lossCarMainVo : lossCarMainVos) {
				if (compensatateService.checkLossState(
						lossCarMainVo.getLossState(), bcFlag)
						&& lossCarMainVo.getUnderWriteFlag().equals("1")) {//未理算

					if (lossCarMainVo.getSerialNo() == 1) {//标的车
						mainLossCarVo = lossCarMainVo;
					}
					// 三者车无损失 无责 展示不了无责代赔 --先不排除无损失的三者车，等到无责代赔车辆数据组织完毕之后再排除
					if (flag.equals(COMP_CI)) {//交强理算标志位1
						if (caseFlag.equals("1")) {
							// 交强-是互碰自赔取本车定损，不是互碰自赔取三者车
							if (lossCarMainVo.getDeflossCarType().equals("1")) {
								reLossCarMainVos.add(lossCarMainVo);
							}
						} else {
							int i = Integer.parseInt(lossCarMainVo
									.getDeflossCarType());
							if (i > 1) {
								if (i == 2) {
									lossCarMainVo.setDeflossCarType("3");
								}
								reLossCarMainVos.add(lossCarMainVo);
							}
						}
					} else {
						if (lossCarMainVo.getDeflossCarType().equals("2")) {
							lossCarMainVo.setDeflossCarType("3");
						}
						reLossCarMainVos.add(lossCarMainVo);
					}
				}
			}
		}
		List<PrpLLossItemVo> rePrpLLossItemVoList = new ArrayList<PrpLLossItemVo>();
		// 交强理算，把车损数据放到LossItemVo中方便页面展示
		if (flag.equals(COMP_CI)) {
			for (PrpLDlossCarMainVo carMainVo : reLossCarMainVos) {
				PrpLLossItemVo lossItemVo = processLossCarVoList(carMainVo,null, "BZ", flag);
				rePrpLLossItemVoList.add(lossItemVo);
			}
		}
		// 商业计算书需要重新组织车损数据
		if (flag.equals(COMP_BI) && !"1".equals(caseFlag)) {
			rePrpLLossItemVoList = new ArrayList<PrpLLossItemVo>();
			List<PrpLLossItemVo> tempPrpLLossItemList = new ArrayList<PrpLLossItemVo>();
			for (PrpLDlossCarMainVo carMainVo : reLossCarMainVos) {
				if (carMainVo.getCetainLossType().equals("05")) {
					// 定损方式cetainLossType为05（无损失），该车不参与理算
					// rePrpLLossItemVoList = null;
					continue;
				} else {
					if (carMainVo.getDeflossCarType().equals("1")) {
						// 查询计算无责代赔金额，放在标的车损失记录下
						BigDecimal noDutyAmt = compensatateService
								.getNoDutyAmt(registNo);
						// 标的车定损方式cetainLossType为02（推定全损）时赋值kindcode为A险，车损险
						if (carMainVo.getLossFeeType() == null) {// 没有可选的损失类别
							continue;
						}
						// 考虑涉水险金额
						BigDecimal sumWadFee = BigDecimal.ZERO;// 涉水核损金额
						String wadKindCode = null;
						List<PrpLDlossCarCompVo> compList = carMainVo.getPrpLDlossCarComps();
						if (compList != null && !compList.isEmpty()) {
							for (PrpLDlossCarCompVo lossCompVo : compList) {
								if ("1".equals(lossCompVo.getWadFlag())) {
									wadKindCode = lossCompVo.getKindCode();
									sumWadFee = sumWadFee.add(DataUtils.NullToZero(lossCompVo.getSumVeriLoss())).subtract(DataUtils.NullToZero(lossCompVo.getVeriRestFee()));
								}
							}
						}
						// 涉水险组织数据
						if (DataUtils.NullToZero(sumWadFee).compareTo(BigDecimal.ZERO) == 1) {
							PrpLDlossCarMainVo wadCarMainVo = Beans.copyDepth().from(carMainVo).to(PrpLDlossCarMainVo.class);
							wadCarMainVo.setSumVeriLossFee(sumWadFee);
							wadCarMainVo.setSumRescueFee(BigDecimal.ZERO);
							tempPrpLLossItemList.add(processLossCarVoList(wadCarMainVo, itemKindVoList, wadKindCode,flag));

						}
						BigDecimal leftFee = carMainVo.getSumVeriLossFee().subtract(sumWadFee);//就是carMainVo.getSumVeriLossFee()减去sumWadFee的值
						if (DataUtils.NullToZero(leftFee).compareTo(BigDecimal.ZERO) == 1
								|| DataUtils.NullToZero(carMainVo.getSumRescueFee()).compareTo(BigDecimal.ZERO) == 1) {
							carMainVo.setSumVeriLossFee(leftFee);// 核损金额 减去
																	// 涉水险金额
							String kindCode ="";
							if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(carMainVo.getRiskCode()) != null && CodeConstants.ISNEWCLAUSECODE2020_MAP.get(carMainVo.getRiskCode())){
								kindCode = CodeConstants.LossFee2020Kind_Map.get(carMainVo.getRiskCode()+carMainVo.getLossFeeType());
							}else{
								kindCode = CodeConstants.LossFeeKind_Map.get(carMainVo.getLossFeeType());
							}
							
							tempPrpLLossItemList.add(processLossCarVoList(carMainVo, itemKindVoList, kindCode, flag));
						}

						for (PrpLLossItemVo tempItem : tempPrpLLossItemList) {
							if (!rePrpLLossItemVoList.contains(tempItem)) {
								tempItem.setAbsolvePayAmt(noDutyAmt);
								rePrpLLossItemVoList.add(tempItem);
							}
						}

					} else {
						// 若为商业险理算，则赋值三者车为B险，第三者责任险
						PrpLLossItemVo thirdLossItem = processLossCarVoList(
								carMainVo, itemKindVoList, "B", flag);
						rePrpLLossItemVoList.add(thirdLossItem);
					}
				}
			}
		}
		/**
		 * 2016-10-18 防止重开赔案-理算冲销-追加定损 后车财人损失一个损失项有多条损失主表 超保额管控出错
		 * 故在组织完损失项后遍历损失项List按照serialNo汇总一次金额（施救费和定损金额）,DLossIdExt字段存多张主表的ID
		 */
		if (rePrpLLossItemVoList != null && rePrpLLossItemVoList.size() > 0) {
			Map<String, PrpLLossItemVo> seriNoItemVoMap = new HashMap<String, PrpLLossItemVo>();
			// 损失项序号和损失项VoMap
			for (PrpLLossItemVo itemVo : rePrpLLossItemVoList) {
				String NoKindKey = itemVo.getItemId() + itemVo.getKindCode();
				if (seriNoItemVoMap == null || seriNoItemVoMap.size() == 0) {
					seriNoItemVoMap.put(NoKindKey, itemVo);
				} else {
					if (seriNoItemVoMap.containsKey(NoKindKey)) {
						seriNoItemVoMap.get(NoKindKey).setRescueFee(
								DataUtils.NullToZero(seriNoItemVoMap.get(NoKindKey).getRescueFee())
								.add(DataUtils.NullToZero(itemVo.getRescueFee())));// 施救费
						seriNoItemVoMap.get(NoKindKey).setSumLoss(seriNoItemVoMap.get(NoKindKey).getSumLoss()
										.add(itemVo.getSumLoss()));// 定损金额
						seriNoItemVoMap.get(NoKindKey).setOriginLossFee(seriNoItemVoMap.get(NoKindKey).getOriginLossFee()
								.add(itemVo.getOriginLossFee()));// 原定损金额
						String DlossId = null;
						if (seriNoItemVoMap.get(NoKindKey).getDlossIdExt() == null) {
							DlossId = itemVo.getDlossId().toString();
						} else {
							DlossId = seriNoItemVoMap.get(NoKindKey).getDlossIdExt()
									+ ","
									+ itemVo.getDlossId();// 定损主表扩展ID
						}
						seriNoItemVoMap.get(NoKindKey).setDlossIdExt(DlossId);
					} else {
						seriNoItemVoMap.put(NoKindKey, itemVo);
					}
				}
			}
			rePrpLLossItemVoList = new ArrayList<PrpLLossItemVo>();
			// 重新添加Vo
			for (PrpLLossItemVo vo : seriNoItemVoMap.values()) {
				// 保单未承保该险别则初始化不显示该损失项
				if (HadBuyTheKind(vo.getKindCode(), cMainVo)) {
					rePrpLLossItemVoList.add(vo);
				}
			}
		}

		/**
		 * 查询无责代赔车辆
		 */
		List<String> noDutyLicenseNo = new ArrayList<String>();// 获取无责代赔车辆车牌号
		PrpLCheckDutyVo checkDuty = new PrpLCheckDutyVo();// 标的车责任类型
		String itemDutyFlag = "";
		if (checkDutyList != null) {
			for (PrpLCheckDutyVo checkDutyVo : checkDutyList) {
				if (checkDutyVo.getSerialNo() == 1) {
					checkDuty = checkDutyVo;
					// 先判断标的有责，再判断三者车无责
					if (DataUtils.NullToZero(checkDutyVo.getIndemnityDutyRate()).compareTo(BigDecimal.ZERO) == 1) {
						itemDutyFlag = CodeConstants.DutyType.CIINDEMDUTY_Y;
					}
				} else {
					if (checkDutyVo.getCiDutyFlag() != null
							&& checkDutyVo.getCiDutyFlag().equals(CodeConstants.DutyType.CIINDEMDUTY_N)) {
						noDutyLicenseNo.add(checkDutyVo.getLicenseNo());
					}
				}
			}
			if (flag.equals(COMP_CI)) {
				if (!itemDutyFlag.equals(CodeConstants.DutyType.CIINDEMDUTY_Y)) {
					// 如果标的车无责，则所有车损数据不显示
					rePrpLLossItemVoList = null;
					noDutyLicenseNo = null;
				} else if (noDutyLicenseNo != null) {
					// 如果为无责代赔，改写无责代赔三者车的支付类型payFlag为无责代赔
					List<PrpLLossItemVo> noDutyLossItemVoList = new ArrayList<PrpLLossItemVo>();
					for (PrpLLossItemVo lossItem : rePrpLLossItemVoList) {
						if (noDutyLicenseNo.contains(lossItem.getItemName())) {
							PrpLLossItemVo noDutyLossItem = Beans.copyDepth().from(lossItem).to(PrpLLossItemVo.class);
							noDutyLossItem.setPayFlag("4");
							if (mainLossCarVo != null) {
								noDutyLossItem.setSumLoss(mainLossCarVo.getSumVeriLossFee() == null ? BigDecimal.ZERO : mainLossCarVo.getSumVeriLossFee());
								noDutyLossItem
										.setRescueFee(mainLossCarVo.getSumVeriRescueFee() == null ? BigDecimal.ZERO : mainLossCarVo.getSumVeriRescueFee());
							} else {
								noDutyLossItem.setSumLoss(BigDecimal.ZERO);
								noDutyLossItem.setRescueFee(BigDecimal.ZERO);
							}
							// 单证保存的数据
							for (PrpLCheckDutyVo checkDutyVo : checkDutyList) {
								if (lossItem.getItemId().equals(checkDutyVo.getSerialNo().toString())) {
									noDutyLossItem.setInsuredComCode(checkDutyVo.getCiInsureComCode());
									noDutyLossItem.setNoDutyPayFlag(checkDutyVo.getNoDutyPayFlag());
									noDutyLossItem.setCiPolicyNo(checkDutyVo.getCiPolicyNo());
									break;
								}

							}

							noDutyLossItemVoList.add(noDutyLossItem);

						}
					}

					rePrpLLossItemVoList.addAll(noDutyLossItemVoList);
				}
			}
		}

		// 排除掉损失为无且payflag!=4（不是无责代赔）的车辆信息
		List<PrpLLossItemVo> removeCarVoList = new ArrayList<PrpLLossItemVo>();
		if (rePrpLLossItemVoList != null && rePrpLLossItemVoList.size() > 0) {
			for (PrpLLossItemVo lossItem : rePrpLLossItemVoList) {
				if (DataUtils.NullToZero(lossItem.getSumLoss()).compareTo(BigDecimal.ZERO) == 0
						&& DataUtils.NullToZero(lossItem.getRescueFee()).compareTo(BigDecimal.ZERO) == 0
						&& !"4".equals(lossItem.getPayFlag())) {
					removeCarVoList.add(lossItem);
				}
			}
		}

		if (removeCarVoList != null && removeCarVoList.size() > 0) {
			rePrpLLossItemVoList.removeAll(removeCarVoList);
		}

		/**
		 * 财产损失信息
		 */
		// lossPropMainVo.getPrpLdlossPropFees()财产种类
		List<PrpLdlossPropMainVo> lossPropMainVos = propTaskService.findPropMainListByRegistNo(registNo);
		List<PrpLdlossPropMainVo> reLossPropMainVos = new ArrayList<PrpLdlossPropMainVo>();
		if (lossPropMainVos != null) {
			for (PrpLdlossPropMainVo lossPropMainVo : lossPropMainVos) {
				if (compensatateService.checkLossState(
						lossPropMainVo.getLossState(), bcFlag)
						&& lossPropMainVo.getUnderWriteFlag().equals("1")) {
					// 无损失的不展示
					if (DataUtils.NullToZero(lossPropMainVo.getSumVeriLoss()).compareTo(BigDecimal.ZERO) == 0
							&& DataUtils.NullToZero(lossPropMainVo.getVerirescueFee()).compareTo(BigDecimal.ZERO) == 0) {
						continue;
					}

					if (flag.equals(COMP_CI)) {
						if ("1".equals(caseFlag)) {// 互碰自赔  财产损失只展示我方的损失
							if (lossPropMainVo.getSerialNo() == 1) {
								reLossPropMainVos.add(lossPropMainVo);
							}
						} else {
							if (!lossPropMainVo.getLossType().equals("1")) {// 交强
								// 当losstype为3是赋值为2来来显示
								if (lossPropMainVo.getLossType().equals("3")) {
									lossPropMainVo.setLossType("2");
								}
								reLossPropMainVos.add(lossPropMainVo);
							}
						}
					} else {
						if (!"1".equals(caseFlag)) {// 商业险 互碰自赔案件 不展示财产损失
							// 当losstype为3是赋值为2来来显示
							if (lossPropMainVo.getLossType().equals("3")) {
								lossPropMainVo.setLossType("2");
							}
							reLossPropMainVos.add(lossPropMainVo);
						}

					}
				}
			}
		}
		// 处理财产损失数据，放到prplLossPropVo中
		List<PrpLLossPropVo> reLossPropList = processLossPropVoList(reLossPropMainVos, itemKindVoList, flag);
		/**
		 * 2016-10-18 防止重开赔案-理算冲销-追加定损 后车财人损失一个损失项有多条损失主表 超保额管控出错
		 * 故在组织完损失项后遍历损失项List按照serialNo汇总一次金额（施救费和定损金额）,DLossIdExt字段存多张主表的ID
		 */
		if (reLossPropList != null && reLossPropList.size() > 0) {
			Map<String, PrpLLossPropVo> seriNoPropVoMap = new HashMap<String, PrpLLossPropVo>();
			// 损失项序号和损失项VoMap
			for (PrpLLossPropVo itemVo : reLossPropList) {
				String NoKindKey = itemVo.getItemId() + itemVo.getKindCode();
				if (seriNoPropVoMap == null || seriNoPropVoMap.size() == 0) {
					seriNoPropVoMap.put(NoKindKey, itemVo);
				} else {
					if (seriNoPropVoMap.containsKey(NoKindKey)) {
						seriNoPropVoMap.get(NoKindKey).setRescueFee(
								DataUtils.NullToZero(seriNoPropVoMap.get(NoKindKey).getRescueFee())
								.add(DataUtils.NullToZero(itemVo.getRescueFee())));// 施救费
						seriNoPropVoMap.get(NoKindKey).setSumLoss(seriNoPropVoMap.get(NoKindKey).getSumLoss()
										.add(itemVo.getSumLoss()));// 定损金额
						seriNoPropVoMap.get(NoKindKey).setOriginLossFee(seriNoPropVoMap.get(NoKindKey).getOriginLossFee()
								.add(itemVo.getOriginLossFee()));// 原定损金额
						String DlossId = null;
						if (seriNoPropVoMap.get(NoKindKey).getDlossIdExt() == null) {
							DlossId = itemVo.getDlossId().toString();
						} else {
							DlossId = seriNoPropVoMap.get(NoKindKey).getDlossIdExt()
									+ ","
									+ itemVo.getDlossId();// 定损主表扩展ID
						}
						seriNoPropVoMap.get(NoKindKey).setDlossIdExt(DlossId);
					} else {
						seriNoPropVoMap.put(NoKindKey, itemVo);
					}
				}
			}
			reLossPropList = new ArrayList<PrpLLossPropVo>();
			// 重新添加Vo
			for (PrpLLossPropVo vo : seriNoPropVoMap.values()) {
				// 保单未承保该险别则初始化不显示该损失项
				if (HadBuyTheKind(vo.getKindCode(), cMainVo)) {
					reLossPropList.add(vo);
				}
			}
		}

		/**
		 * 其他损失及 险别List
		 */
		List<PrpLLossPropVo> prpLLossPropOthVos = new ArrayList<PrpLLossPropVo>();
		for (PrpLDlossCarMainVo DlossCarMain : reLossCarMainVos) {

			for (PrpLDlossCarSubRiskVo vo : DlossCarMain.getPrpLDlossCarSubRisks()) {
				PrpLLossPropVo prpLLossPropOth = new PrpLLossPropVo();
				prpLLossPropOth.setPropType("9");
				prpLLossPropOth.setDlossId(vo.getId());
				prpLLossPropOth.setKindCode(vo.getKindCode());
				prpLLossPropOth.setRiskCode(reClaimVo.getRiskCode());
				prpLLossPropOth.setItemName(vo.getItemName());
				prpLLossPropOth.setLossQuantity(vo.getCount());
				prpLLossPropOth.setSumLoss(vo.getVeriSubRiskFee());
				// prpLLossPropOth.setSumRealPay(vo.getVeriSubRiskFee());
				prpLLossPropOthVos.add(prpLLossPropOth);
				// reLossPropList.add(prpLLossPropOth);//
				// 把其他财产损失也放到财产List中，显示的时候根据PropType判断是否显示
			}
		}

		/**
		 * 2016-10-18 防止重开赔案-理算冲销-追加定损 后车财人损失一个损失项有多条损失主表 超保额管控出错
		 * 故在组织完损失项后遍历损失项List按照serialNo汇总一次金额（施救费和定损金额）,DLossIdExt字段存多张主表的ID
		 */
		if (prpLLossPropOthVos != null && prpLLossPropOthVos.size() > 0) {
			Map<String, PrpLLossPropVo> seriNoOthVoMap = new HashMap<String, PrpLLossPropVo>();
			// 损失项序号和损失项VoMap
			for (PrpLLossPropVo itemVo : prpLLossPropOthVos) {
				String NoKindKey = itemVo.getItemId() + itemVo.getKindCode();
				if (seriNoOthVoMap == null || seriNoOthVoMap.size() == 0) {
					seriNoOthVoMap.put(NoKindKey, itemVo);
				} else {
					if (seriNoOthVoMap.containsKey(NoKindKey)) {
						seriNoOthVoMap.get(NoKindKey).setRescueFee(
								DataUtils.NullToZero(seriNoOthVoMap.get(NoKindKey).getRescueFee())
										.add(DataUtils.NullToZero(itemVo.getRescueFee())));// 施救费
						seriNoOthVoMap.get(NoKindKey).setSumLoss(seriNoOthVoMap.get(NoKindKey).getSumLoss()
										.add(itemVo.getSumLoss()));// 定损金额
						seriNoOthVoMap.get(NoKindKey).setOriginLossFee(seriNoOthVoMap.get(NoKindKey).getOriginLossFee()
								.add(itemVo.getOriginLossFee()));// 原定损金额
						// 当附加险为 修理补偿 代步 停驶时需要累加天数
						if (CodeConstants.KINDCODE.KINDCODE_C.equals(itemVo.getKindCode())
								|| CodeConstants.KINDCODE.KINDCODE_T.equals(itemVo.getKindCode())
								|| CodeConstants.KINDCODE.KINDCODE_RF.equals(itemVo.getKindCode())) {
							seriNoOthVoMap.get(NoKindKey).setLossQuantity(seriNoOthVoMap.get(NoKindKey)
											.getLossQuantity().add(itemVo.getLossQuantity()));
						}
						String DlossId = null;
						if (seriNoOthVoMap.get(NoKindKey).getDlossIdExt() == null) {
							DlossId = itemVo.getDlossId().toString();
						} else {
							DlossId = seriNoOthVoMap.get(NoKindKey).getDlossIdExt()+ ","
									+ itemVo.getDlossId();// 定损主表扩展ID
						}
						seriNoOthVoMap.get(NoKindKey).setDlossIdExt(DlossId);
					} else {
						seriNoOthVoMap.put(NoKindKey, itemVo);
					}
				}
			}
			prpLLossPropOthVos = new ArrayList<PrpLLossPropVo>();
			// 重新添加Vo
			for (PrpLLossPropVo vo : seriNoOthVoMap.values()) {
				prpLLossPropOthVos.add(vo);
			}
		}

		/**
		 * 判断施救费是否为null
		 */
		for (PrpLLossPropVo reLossProp : reLossPropList) {
			if (reLossProp.getRescueFee() == null) {
				reLossProp.setRescueFee(new BigDecimal(0));
			}
		}
		/**
		 * 人伤损失信息
		 */
		List<PrpLDlossPersTraceMainVo> reLossPersTraceMainVos = compensatateService.getValidLossPersTraceMain(registNo, bcFlag);
		List<PrpLDlossPersTraceVo> lossPersTraceList = new ArrayList<PrpLDlossPersTraceVo>();
		if (reLossPersTraceMainVos != null) {
			// 遍历获取有效的人伤跟踪任务子表PrpLDlossPersTrace
			for (PrpLDlossPersTraceMainVo lossPersTraceMain : reLossPersTraceMainVos) {
				if (!compensatateService.checkLossState(
						lossPersTraceMain.getLossState(), bcFlag)) {
					continue;
				}
				if ("7".equals(lossPersTraceMain.getUnderwriteFlag())) {
                    continue;
                }
				if (lossPersTraceMain.getPrpLDlossPersTraces() != null
						&& lossPersTraceMain.getPrpLDlossPersTraces().size() > 0) {
					for (PrpLDlossPersTraceVo lossPersTrace : lossPersTraceMain
							.getPrpLDlossPersTraces()) {
						// 无损失的不展示 或者无效的不展示
						if ("0".equals(lossPersTrace.getValidFlag())
								|| BigDecimal.ZERO.compareTo(
										DataUtils.NullToZero(lossPersTrace.getSumVeriDefloss())) == 0) {
							continue;
						}

						if (flag.equals(COMP_CI)) {
							/*
							 * if
							 * (lossPersTrace.getPrpLDlossPersInjured().getSerialNo
							 * ()>1 && lossPersTrace.getValidFlag().equals("1"))
							 * {
							 */
							if (caseFlag == "0") {
								if (lossPersTrace.getPrpLDlossPersInjured().getSerialNo() != 1
										&& lossPersTrace.getValidFlag().equals("1")) {
									lossPersTraceList.add(lossPersTrace);
								}
							} else {
								if (lossPersTrace.getPrpLDlossPersInjured().getSerialNo() == 1
										&& lossPersTrace.getValidFlag().equals("1")) {
									lossPersTraceList.add(lossPersTrace);
								}
							}

						} else {
							if (lossPersTrace.getValidFlag().equals("1")) {// 人伤记录有效
								lossPersTraceList.add(lossPersTrace);
							}
						}
					}
				}
			}
		}

		Map<String, String> kindForChaMap = new HashMap<String, String>();// 费用赔款信息表险别Map
		Map<String, String> kindForOthMap = new HashMap<String, String>();// 其他损失表险别Map
		// 处理人伤损失数据
		List<PrpLLossPersonVo> reLossPersonList = processLossPersonVoList(lossPersTraceList, flag);
		List<PrpLLossPersonVo> reLossPersonListTemp = new ArrayList<PrpLLossPersonVo>();
		// 临时List用于排除未承保险别的损失项数据
		for (PrpLLossPersonVo vo : reLossPersonList) {
			if (HadBuyTheKind(vo.getKindCode(), cMainVo)) {
				// 保单未承保该险别则初始化不显示该损失项
				reLossPersonListTemp.add(vo);
			}
		}
		reLossPersonList = reLossPersonListTemp;

		if (flag.equals(COMP_BI)) {
			PrpLCItemKindVo itemKindNtVo = null;
			for (PrpLCItemKindVo itemKindVo : itemKindVoList) {
				if (!CodeConstants.NOSUBRISK_MAP.containsKey(itemKindVo.getKindCode())
						&& !itemKindVo.getKindCode().endsWith("M")) {
					kindForChaMap.put(itemKindVo.getKindCode(),itemKindVo.getKindName());

					if ("N".equals(itemKindVo.getCalculateFlag())) {
						int tempFlag = 0;
						for (PrpLLossPropVo vo : prpLLossPropOthVos) {// 去掉遍历出的附加险
							if (itemKindVo.getKindCode().equals(vo.getKindCode())
									&& !CodeConstants.KINDCODE.KINDCODE_X.equals(vo.getKindCode())) {
								tempFlag++;
							}
						}
						for (PrpLLossItemVo vo : rePrpLLossItemVoList) {// 去掉遍历出的附加险
							if (itemKindVo.getKindCode().equals(vo.getKindCode())) {
								tempFlag++;
							}
						}
						if (tempFlag == 0) {
							kindForOthMap.put(itemKindVo.getKindCode().trim(),
									itemKindVo.getKindName());
						}
					}
				}
				if (CodeConstants.KINDCODE.KINDCODE_NT.equals(itemKindVo.getKindCode())) {
					haveKindNT = true;
					itemKindNtVo = itemKindVo;
				}
			}
			if (haveKindNT && !"4".equals(checkDuty.getIndemnityDuty())) {
				kindForOthMap.put(itemKindNtVo.getKindCode(),itemKindNtVo.getKindName());
			}

			actionVo.setHaveKindNT(haveKindNT);
			// modelAndView.addObject("haveKindNT", haveKindNT);

		}

		if (mainLossCarVo != null) {
			BigDecimal amountKindA = actionVo.getAmountKindA();
			BigDecimal sumLoss = DataUtils.NullToZero(mainLossCarVo.getSumVeriLossFee());// 主车定损金额+施救费
			BigDecimal rescueFee = DataUtils.NullToZero(mainLossCarVo.getSumVeriRescueFee());
			// 赔付金额大于0,承保了无法找到第三方，并且勾选了无法找到第三方免赔条件
			if (sumLoss.add(rescueFee).compareTo(BigDecimal.ZERO) == 1
					&& haveKindNT && actionVo.isNoFindThirdCond()) {
				PrpLLossPropVo prpLLossPropOth = new PrpLLossPropVo();
				prpLLossPropOth.setPropType("9");
				prpLLossPropOth.setKindCode(CodeConstants.KINDCODE.KINDCODE_NT);
				prpLLossPropOth.setRiskCode(reClaimVo.getRiskCode());
				prpLLossPropOth.setItemName(mainLossCarVo.getLicenseNo());
				if (mainLossCarVo.getSumVeriLossFee().compareTo(amountKindA) == 1) {
					sumLoss = amountKindA;
				}
				if (mainLossCarVo.getSumVeriRescueFee().compareTo(amountKindA) == 1) {
					rescueFee = amountKindA;
				}
				prpLLossPropOth.setSumLoss(sumLoss.add(rescueFee));

				prpLLossPropOthVos.add(prpLLossPropOth);
			}
		}
		//如果保了医保外用药 则初始化人伤减损金额等信息到 其他损失项中
		List<String> kindCodeList = null;
		if(CodeConstants.New_RISKCODE.NEW_RISK.indexOf(cMainVo.getRiskCode()) != -1){
			kindCodeList = new ArrayList<String>();
			if(kindForOthMap.containsKey(CodeConstants.KINDCODE.KINDCODE_DP)){
				kindCodeList.add(CodeConstants.KINDCODE.KINDCODE_DP);
			}
			if(kindForOthMap.containsKey(CodeConstants.KINDCODE.KINDCODE_BP)){
				kindCodeList.add(CodeConstants.KINDCODE.KINDCODE_BP);
			}
			if(kindForOthMap.containsKey(CodeConstants.KINDCODE.KINDCODE_D11P)){
				kindCodeList.add(CodeConstants.KINDCODE.KINDCODE_D11P);
			}
			if(kindForOthMap.containsKey(CodeConstants.KINDCODE.KINDCODE_D12P)){
				kindCodeList.add(CodeConstants.KINDCODE.KINDCODE_D12P);
			}
		}
		
		if(kindCodeList != null && kindCodeList.size() > 0){
			List<PrpLDlossPersTraceMainVo> findPersTraceMainVo = persTraceDubboService.findPersTraceMainVoList(registNo);
			for (String kindCode : kindCodeList) {
				PrpLLossPropVo prpLLossPropVo = new PrpLLossPropVo();
				BigDecimal sum = new BigDecimal(0);
				String name = "";
				String riskCode = "";
				if(findPersTraceMainVo != null && findPersTraceMainVo.size() > 0){
					for (PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo : findPersTraceMainVo) {
						List<PrpLDlossPersTraceVo> prpLDlossPersTraces = prpLDlossPersTraceMainVo.getPrpLDlossPersTraces();
						riskCode = prpLDlossPersTraceMainVo.getRiskCode();
						if(prpLDlossPersTraces != null && prpLDlossPersTraces.size() > 0){
							for (PrpLDlossPersTraceVo prpLDlossPersTraceVo : prpLDlossPersTraces) {
								boolean flags = false;
								PrpLDlossPersInjuredVo prpLDlossPersInjured = prpLDlossPersTraceVo.getPrpLDlossPersInjured();
								List<PrpLDlossPersTraceFeeVo> prpLDlossPersTraceFees = prpLDlossPersTraceVo.getPrpLDlossPersTraceFees();
								if(prpLDlossPersTraceFees != null && prpLDlossPersTraceFees.size() > 0){
									if(CodeConstants.KINDCODE.KINDCODE_BP.equals(kindCode) && "1".equals(prpLDlossPersInjured.getLossItemType())){
										for (PrpLDlossPersTraceFeeVo prpLDlossPersTraceFeeVo : prpLDlossPersTraceFees) {
											//"1".equals(prpLDlossPersTraceFeeVo.getFeeTypeCode())  只需要拿医疗费这条数据
											if("1".equals(prpLDlossPersTraceFeeVo.getFeeTypeCode()) && prpLDlossPersTraceFeeVo.getDetractionfee().compareTo(new BigDecimal(0)) == 1){
												//如果人伤审核的时候修改了定损金额，导致减损金额变化，则取审核后减损金额
												if(prpLDlossPersTraceFeeVo.getVeriDetractionFee() != null){
													sum = sum.add(prpLDlossPersTraceFeeVo.getVeriDetractionFee());
												}else{
													sum = sum.add(prpLDlossPersTraceFeeVo.getDetractionfee());
												}
												flags = true;
											}
										}
									}else if(CodeConstants.KINDCODE.KINDCODE_D11P.equals(kindCode) && "3".equals(prpLDlossPersInjured.getLossItemType())){
										for (PrpLDlossPersTraceFeeVo prpLDlossPersTraceFeeVo : prpLDlossPersTraceFees) {
											if("1".equals(prpLDlossPersTraceFeeVo.getFeeTypeCode()) && prpLDlossPersTraceFeeVo.getDetractionfee().compareTo(new BigDecimal(0)) == 1){
												//如果人伤审核的时候修改了定损金额，导致减损金额变化，则取审核后减损金额
												if(prpLDlossPersTraceFeeVo.getVeriDetractionFee() != null){
													sum = sum.add(prpLDlossPersTraceFeeVo.getVeriDetractionFee());
												}else{
													sum = sum.add(prpLDlossPersTraceFeeVo.getDetractionfee());
												}
												flags = true;
											}
										}
									}else if(CodeConstants.KINDCODE.KINDCODE_D12P.equals(kindCode) && "2".equals(prpLDlossPersInjured.getLossItemType())){
										for (PrpLDlossPersTraceFeeVo prpLDlossPersTraceFeeVo : prpLDlossPersTraceFees) {
											if("1".equals(prpLDlossPersTraceFeeVo.getFeeTypeCode()) && prpLDlossPersTraceFeeVo.getDetractionfee().compareTo(new BigDecimal(0)) == 1){
												//如果人伤审核的时候修改了定损金额，导致减损金额变化，则取审核后减损金额
												if(prpLDlossPersTraceFeeVo.getVeriDetractionFee() != null){
													sum = sum.add(prpLDlossPersTraceFeeVo.getVeriDetractionFee());
												}else{
													sum = sum.add(prpLDlossPersTraceFeeVo.getDetractionfee());
												}
												flags = true;
											}
										}
									}
								}
								if(flags){
									name += prpLDlossPersInjured.getPersonName()+",";
								}
							}
						}
					}
					if(sum.compareTo(new BigDecimal(0)) == 1){
						String[] split = name.split(",");
						String a= "";
						for(int i = 0;i<split.length;i++){
							if(i == split.length-1){
								a+=split[i];
							}else{
								a+=split[i]+",";
							}
						}
						prpLLossPropVo.setItemName(a);
						prpLLossPropVo.setSumLoss(sum);
						prpLLossPropVo.setOriginLossMaxFee(sum);
						prpLLossPropVo.setKindCode(kindCode);
						prpLLossPropVo.setRiskCode(riskCode);
						prpLLossPropOthVos.add(prpLLossPropVo);
					}
				}
			}
		}
		
		/**
		 * 费用赔款信息
		 */
		List<PrpLDlossChargeVo> dlossChargeVos = null;
		if (StringUtils.isNotBlank(registNo)) {
			dlossChargeVos = lossChargeService
					.findLossChargeVoByRegistNo(registNo);
		}
		List<PrpLChargeVo> prpLChargeVos = new ArrayList<PrpLChargeVo>();

		for (PrpLDlossChargeVo dlossChargeVo : dlossChargeVos) {
			boolean isCompDeductFlag = compensatateService
					.adjustIsCompDeductForCharge(dlossChargeVo, bcFlag);
			// 判断该条费用是否已经理算扣减 扣减则不组织
			if (isCompDeductFlag) {
				continue;
			}
			//判断核损通过，排除定损注销
			if("DLCar".equals(dlossChargeVo.getBusinessType())){
				PrpLDlossCarMainVo carVo = lossCarService.findLossCarMainById(Long.valueOf(dlossChargeVo.getBusinessId()));
				if(!"1".equals(carVo.getUnderWriteFlag())){
					continue;
				}
			}
			if("DLProp".equals(dlossChargeVo.getBusinessType())){
				PrpLdlossPropMainVo propVo = propTaskService.findPropMainVoById(Long.valueOf(dlossChargeVo.getBusinessId()));
				if(!"1".equals(propVo.getUnderWriteFlag())){
					continue;
				}
			}
			PrpLChargeVo prpLChargeVo = new PrpLChargeVo();
			prpLChargeVo.setKindCode(dlossChargeVo.getKindCode());
			// 来源环节字段设置
			if (dlossChargeVo.getBusinessType().equals("Check")) {
				prpLChargeVo
						.setBusinessType(CodeConstants.chargeNodeCode.CHARGE_CHECK);
			} else if (dlossChargeVo.getBusinessType().equals("PLoss")) {
				prpLChargeVo
						.setBusinessType(CodeConstants.chargeNodeCode.CHARGE_PLOSS);
			} else {
				prpLChargeVo
						.setBusinessType(CodeConstants.chargeNodeCode.CHARGE_LOSS);
			}
			prpLChargeVo.setChargeCode(dlossChargeVo.getChargeCode());
			// 判断
			if ("Check".equals(dlossChargeVo.getBusinessType())) {
				prpLChargeVo.setFeeAmt(DataUtils.NullToZero(dlossChargeVo
						.getChargeFee()));
			} else {
				prpLChargeVo.setFeeAmt(DataUtils.NullToZero(dlossChargeVo
						.getVeriChargeFee()));
			}
			prpLChargeVo.setBusinessId(dlossChargeVo.getBusinessId());// 关联损失ID
			PrpLPayCustomVo payCustomVo = managerService
					.findPayCustomVoById(dlossChargeVo.getReceiverId());
			// 需要在voCustom增加相关字段
			prpLChargeVo.setPayeeId(payCustomVo.getId());
			prpLChargeVo.setPayeeName(payCustomVo.getPayeeName());
			prpLChargeVo.setAccountNo(payCustomVo.getAccountNo());
			prpLChargeVo.setBankName(payCustomVo.getBankName());
			prpLChargeVo.setPayeeIdfNo(payCustomVo.getCertifyNo());

			prpLChargeVos.add(prpLChargeVo);
		}
		// 判断扣除类型
		String deductType = CodeConstants.DeductType.DEDUCT_NONE;
		if (prePayMap.get("prePay") != null
				&& prePayMap.get("prePay").size() > 0
				|| prePayMap.get("preFee") != null
				&& prePayMap.get("preFee").size() > 0) {
			deductType = CodeConstants.DeductType.DEDUCT_PRE;
		} else if (padPayPersons != null && padPayPersons.size() > 0
				&& flag.equals(COMP_CI)) {
			deductType = CodeConstants.DeductType.DEDUCT_PAD;
		}

		// 交强显示BZ
		// 商业显示非bz
		List<PrpLChargeVo> prpLChargees = new ArrayList<PrpLChargeVo>();

		for (PrpLChargeVo PrpLChargeVo : prpLChargeVos) {
			if (flag.equals(COMP_CI)) {
				if ("BZ".equals(PrpLChargeVo.getKindCode())) {
					prpLChargees.add(PrpLChargeVo);
				}
			} else {
				if (!"BZ".equals((PrpLChargeVo.getKindCode()))) {
					prpLChargees.add(PrpLChargeVo);
				}
			}
		}
		// 初始化时拆分应扣预付金额到损失项
		if (prePayMap.get("prePay") != null
				&& prePayMap.get("prePay").size() > 0) {
			addPrePayAmtForLoss(prePayMap.get("prePay"), rePrpLLossItemVoList,
					reLossPropList, reLossPersonList);

		}

		// 增加代位数据
		Map<String, String> qfLicenseMap = new HashMap<String, String>();// 清付车牌号
		String dwFlag = this.organizeDwILossItem(platLockList,rePrpLLossItemVoList, qfLicenseMap, actionVo.getCarMap());
		String dwPersFlag = "0";
		if("0".equals(dwFlag)){
			// 非车代位数据组织
			dwFlag = this.organizeDwILossItemPers(platLockList,rePrpLLossItemVoList,qfLicenseMap, actionVo.getCarMap(),cMainVo.getRegistNo());
			dwPersFlag = dwFlag;
		}
		actionVo.setDwFlag(dwFlag);
		// 理算主表数据处理
		PrpLCompensateVo compensateVo = processCompensateMain(registNo,
				reClaimVo, cMainVo, deductType, checkDuty, bcFlag);
		// 互碰自赔未取最新环节的值checkTaskService
		/*
		 * List<PrpLCheckDutyVo> prpLCheckDutyVos =
		 * checkTaskService.findCheckDutyByRegistNo(registNo);
		 * for(PrpLCheckDutyVo vo:prpLCheckDutyVos ){
		 * if(!"1".equals(vo.getCarType())){
		 * if("1".equals(vo.getIsClaimSelf())){ CompensateVo.setCaseType("2"); }
		 * } }
		 */

		actionVo.setCompensateVo(compensateVo);
		actionVo.setPaymentVoList(prpLPaymentVoList);
		actionVo.setLossItemVoList(rePrpLLossItemVoList);
		actionVo.setLossPropList(reLossPropList);
		actionVo.setLossPersonList(reLossPersonList);
		actionVo.setOtherLossProps(prpLLossPropOthVos);
		actionVo.setPrpLChargees(prpLChargees);
		actionVo.setKindForChaMap(kindForChaMap);
		actionVo.setKindForOthMap(kindForOthMap);
		actionVo.setQfLicenseMap(qfLicenseMap);
		actionVo.setDwPersFlag(dwPersFlag);

	}

	/**
	 * 如果状态为已处理则读取理算表的数据初始化显示
	 * 
	 * @param compVo
	 */
	private void getCompensate(CompensateActionVo actionVo,
			PrpLWfTaskVo wfTaskVo, PrpLCompensateVo compVo,
			List<PrpLPlatLockVo> platLockList,
			List<PrpLCItemKindVo> itemKindVoList) {
		boolean haveKindNT = false;// 是否包含无法找到第三方条款
		String compFlag = actionVo.getCompFlag();
		Map<String, String> qfLicenseMap = new HashMap<String, String>();// 清付车牌号
		List<PrpLPaymentVo> prpLPayments = compVo.getPrpLPayments();
		List<PrpLLossItemVo> prpLLossItems = compVo.getPrpLLossItems();
		List<PrpLLossPropVo> prpLLossProps = compVo.getPrpLLossProps();
		List<PrpLLossPersonVo> prpLLossPersons = compVo.getPrpLLossPersons();
		// 增加代位数据
		String dwFlag = this.organizeDwILossItem(platLockList, prpLLossItems,qfLicenseMap, actionVo.getCarMap());
		String dwPersFlag = "0";
		if("0".equals(dwFlag)){
			// 非车代位数据组织
			dwFlag = this.organizeDwILossItemPers(platLockList,prpLLossItems,qfLicenseMap, actionVo.getCarMap(),compVo.getRegistNo());
			dwPersFlag = dwFlag;
		}
		// List<PrpLKindAmtSummaryVo> prpLKindAmtSummaries =
		// compVo.getPrpLKindAmtSummaries();
		List<PrpLChargeVo> prpLCharges = compVo.getPrpLCharges();
		for (PrpLChargeVo prpLChargeVo : prpLCharges) {
			PrpLPayCustomVo payCustomVo = managerService
					.findPayCustomVoById(prpLChargeVo.getPayeeId());
			// 需要在voCustom增加相关字段
			prpLChargeVo.setPayeeName(payCustomVo.getPayeeName());
			prpLChargeVo.setAccountNo(payCustomVo.getAccountNo());
			prpLChargeVo.setBankName(payCustomVo.getBankName());
			prpLChargeVo.setPayeeIdfNo(payCustomVo.getCertifyNo());
		}
		for (PrpLPaymentVo payMentVo : prpLPayments) {
			PrpLPayCustomVo payCustomVo = managerService
					.findPayCustomVoById(payMentVo.getPayeeId());
			// 需要在voCustom增加相关字段
			payMentVo.setPayeeName(payCustomVo.getPayeeName());
			payMentVo.setAccountNo(payCustomVo.getAccountNo());
			payMentVo.setBankName(payCustomVo.getBankOutlets());
			payMentVo.setPayObjectKind(payCustomVo.getPayObjectKind());
		}
		// 交强显示BZ
		// 商业显示非bz
		List<PrpLChargeVo> prpLChargees = new ArrayList<PrpLChargeVo>();
		for (PrpLChargeVo PrpLChargeVo : prpLCharges) {
			if (compFlag.equals(COMP_CI)) {
				if (PrpLChargeVo.getKindCode().equals("BZ")) {
					prpLChargees.add(PrpLChargeVo);
				}
			} else {
				if (!(PrpLChargeVo.getKindCode().equals("BZ"))) {
					prpLChargees.add(PrpLChargeVo);
				}
			}
		}
		List<PrpLLossPropVo> prpLLossPropOthVos = new ArrayList<PrpLLossPropVo>();
		if (prpLLossProps != null && !prpLLossProps.isEmpty()) {
			Iterator<PrpLLossPropVo> propIterator = prpLLossProps.iterator();
			while (propIterator.hasNext()) {
				PrpLLossPropVo propVo = propIterator.next();
				if ("9".equals(propVo.getPropType())) {
					prpLLossPropOthVos.add(propVo);
					propIterator.remove();
				}
			}

		}

		Map<String, String> kindForChaMap = new HashMap<String, String>();// 费用赔款信息表险别Map
		Map<String, String> kindForOthMap = new HashMap<String, String>();// 其他损失表险别Map
		if (compFlag.equals(COMP_BI)) {
			PrpLCItemKindVo itemKindNtVo = null;
			for (PrpLCItemKindVo itemKindVo : itemKindVoList) {
				if (!CodeConstants.NOSUBRISK_MAP.containsKey(itemKindVo
						.getKindCode())
						&& !itemKindVo.getKindCode().endsWith("M")) {

					kindForChaMap.put(itemKindVo.getKindCode(),
							itemKindVo.getKindName());

					if ("N".equals(itemKindVo.getCalculateFlag())) {// 无责把显示无法找到第三方
						int tempFlag = 0;
						for (PrpLLossPropVo vo : prpLLossPropOthVos) {// 去掉遍历出的附加险
							if (vo.getDlossId() != null
									&& itemKindVo.getKindCode().equals(
											vo.getKindCode())
									&& !CodeConstants.KINDCODE.KINDCODE_X
											.equals(vo.getKindCode())) {
								tempFlag++;
							}
						}

						for (PrpLLossItemVo vo : prpLLossItems) {// 去掉遍历出的附加险
							if (itemKindVo.getKindCode().equals(
									vo.getKindCode())) {
								tempFlag++;
							}
						}
						if (tempFlag == 0) {
							kindForOthMap.put(itemKindVo.getKindCode().trim(),
									itemKindVo.getKindName());
						}
					}
				}

				if (CodeConstants.KINDCODE.KINDCODE_NT.equals(itemKindVo
						.getKindCode())) {
					haveKindNT = true;
					itemKindNtVo = itemKindVo;
				}
			}
			if (haveKindNT && !"4".equals(compVo.getIndemnityDuty())) {
				kindForOthMap.put(itemKindNtVo.getKindCode(),
						itemKindNtVo.getKindName());
			}
			
			if(HandlerStatus.INIT.equals(wfTaskVo.getHandlerStatus())||HandlerStatus.START.equals(wfTaskVo.getHandlerStatus())||HandlerStatus.DOING.equals(wfTaskVo.getHandlerStatus())){
			    List<PrpLCheckDutyVo> checkDutyList = checkTaskService.findCheckDutyByRegistNo(compVo.getRegistNo());
	            for(PrpLCheckDutyVo prpLCheckDutyVo:checkDutyList){
	                if(1==prpLCheckDutyVo.getSerialNo()){
	                    compVo.setIndemnityDuty(prpLCheckDutyVo.getIndemnityDuty());
	                    compVo.setIndemnityDutyRate(prpLCheckDutyVo.getIndemnityDutyRate());
	                }
	            } 
			}
			
			actionVo.setHaveKindNT(haveKindNT);
		}

		actionVo.setDwFlag(dwFlag);

		actionVo.setCompensateVo(compVo);
		actionVo.setPaymentVoList(prpLPayments);
		actionVo.setLossItemVoList(prpLLossItems);
		actionVo.setLossPropList(prpLLossProps);
		actionVo.setLossPersonList(prpLLossPersons);
		actionVo.setOtherLossProps(prpLLossPropOthVos);
		actionVo.setPrpLChargees(prpLChargees);
		actionVo.setKindForChaMap(kindForChaMap);
		actionVo.setKindForOthMap(kindForOthMap);
		actionVo.setQfLicenseMap(qfLicenseMap);
		// TODO why
		actionVo.setBuJiState("1");
		actionVo.setDwPersFlag(dwPersFlag);

		// modelAndView.addObject("prpLKindAmtSummaries", prpLKindAmtSummaries);
	}

	/**
	 * 组织代位信息
	 * 
	 * @param platLockList
	 * @param lossItemVoList
	 *            dwFlag 0 非代位，1 追偿 2 清付 3 追偿和清付
	 */
	private String organizeDwILossItem(List<PrpLPlatLockVo> platLockList,
			List<PrpLLossItemVo> lossItemVoList,
			Map<String, String> qfLicenseMap, Map<String, String> carMap) {
		String dwFlag = "0";
		List<PrpLPlatLockVo> recoveryLockList = new ArrayList<PrpLPlatLockVo>();
		boolean dfPayFlag = false; // 代付标识
		boolean qfPayFlag = false;// 清付标识
		
		if (platLockList != null && !platLockList.isEmpty()) {
			for (PrpLPlatLockVo platLockVo : platLockList) {
				if (PayFlagType.CLEAR_PAY.equals(platLockVo
						.getRecoveryOrPayFlag())) {// 清付
					for (PrpLLossItemVo lossItem : lossItemVoList) {
						if (lossItem.getItemName().equals(
								platLockVo.getOppoentLicensePlateNo())
								&& !CodeConstants.PayFlagType.NODUTY_INSTEAD_PAY
										.equals(lossItem.getPayFlag())) {// 锁定表取对方车牌号和三者车是否相同
							lossItem.setPayFlag(PayFlagType.CLEAR_PAY);
							List<PrpLPlatLockVo> itemPlatLockList = new ArrayList<PrpLPlatLockVo>();
							itemPlatLockList.add(platLockVo);
							lossItem.setPlatLockList(itemPlatLockList);
							if (!qfLicenseMap.containsKey(lossItem.getItemId())) {
								qfLicenseMap.put(lossItem.getItemId(),
										lossItem.getItemName());
							}
						}
					}
					qfPayFlag = true;
				} else {// 追偿
					recoveryLockList.add(platLockVo);
					dfPayFlag = true;
					String licenseNo = platLockVo.getOppoentLicensePlateNo();
					if (carMap.containsKey(licenseNo)) {
						String itemId = carMap.get(licenseNo);
						if (!qfLicenseMap.containsKey(itemId)) {
							qfLicenseMap.put(itemId, licenseNo);
						}
					}

					for (PrpLLossItemVo lossItem : lossItemVoList) {
						if (lossItem.getItemName().equals(
								platLockVo.getOppoentLicensePlateNo())) {
							if (!qfLicenseMap.containsKey(lossItem.getItemId())) {
								qfLicenseMap.put(lossItem.getItemId(),
										lossItem.getItemName());
							}
						}
					}
				}
			}
			// 有追偿
			if (recoveryLockList != null && !recoveryLockList.isEmpty()) {
				PrpLLossItemVo lossItemAVo = null;
				boolean recovyFlag = false;// 是否已存在追偿数据
				for (PrpLLossItemVo lossItem : lossItemVoList) {
					if ((CodeConstants.KINDCODE.KINDCODE_A.equals(lossItem
							.getKindCode())||CodeConstants.KINDCODE.KINDCODE_A1.equals(lossItem
									.getKindCode()))
							&& !PayFlagType.NODUTY_INSTEAD_PAY.equals(lossItem
									.getPayFlag())) {
						if (PayFlagType.INSTEAD_PAY.equals(lossItem
								.getPayFlag())) {
							lossItem.setPlatLockList(recoveryLockList);
							recovyFlag = true;
						} else {
							lossItemAVo = Beans.copyDepth().from(lossItem)
									.to(PrpLLossItemVo.class);
						}
					}
				}

				if (!recovyFlag) {
					lossItemAVo.setPayFlag(PayFlagType.INSTEAD_PAY);
					lossItemAVo.setPlatLockList(recoveryLockList);
					lossItemVoList.add(lossItemAVo);
				}
			}
			if(!dfPayFlag){
				// 平台取的锁定信息中没有代付  检查车损PrplLossItem是否存在代付损失记录 存在则移除
				PrpLLossItemVo itemVoRemove = null;
				for (PrpLLossItemVo lossItem : lossItemVoList){
					if (CodeConstants.KINDCODE.KINDCODE_A.equals(lossItem.getKindCode())
							&& PayFlagType.INSTEAD_PAY.equals(lossItem.getPayFlag())) {
						itemVoRemove = lossItem;
						break;
					}
				}
				if(itemVoRemove!=null){
					lossItemVoList.remove(itemVoRemove);
				}
			}
		}
		
		if (dfPayFlag && qfPayFlag) {
			dwFlag = "3";// 清付 追偿
		} else if (dfPayFlag) {
			dwFlag = "1";// 追偿
		} else if (qfPayFlag) {
			dwFlag = "2";// 清付
		} else {
			dwFlag = "0";// 非代位
		}

		return dwFlag;
	}
	/**
	 * 非机动车代位 没有平台锁定环节，但是需要组织和展示追偿信息  不存入数据到锁定表和recovery表 线下发起追偿
	 * <pre></pre>
	 * @param platLockList
	 * @param lossItemVoList
	 * @param qfLicenseMap
	 * @param carMap
	 * @return
	 * @modified:
	 * ☆WLL(2017年4月28日 下午8:50:54): <br>
	 */
	public String organizeDwILossItemPers(List<PrpLPlatLockVo> platLockList,List<PrpLLossItemVo> lossItemVoList,
	                                      Map<String, String> qfLicenseMap, Map<String, String> carMap,String registNo){
		String dwFlag = "0";// 非代位
		if(lossItemVoList!=null && lossItemVoList.size()>0){
			PrpLSubrogationMainVo PrpLSubrogationMainVo = subrogationService.find(registNo);
			List<PrpLSubrogationPersonVo> subrogationPersonVoList = PrpLSubrogationMainVo.getPrpLSubrogationPersons();
			if(CodeConstants.CommonConst.TRUE.equals(PrpLSubrogationMainVo.getSubrogationFlag()) &&
					subrogationPersonVoList!=null&&subrogationPersonVoList.size()>0){
				dwFlag = "1";// 追偿
				for(PrpLSubrogationPersonVo persVo : subrogationPersonVoList){
					//组织清付方下拉列表
					qfLicenseMap.put(persVo.getId().toString(),persVo.getName());
				}
				// 非机动车代位 追偿的第三方不是车辆，所以只有标的车两条车损数据 自付和代付
				PrpLLossItemVo lossItemAVo = null;
				boolean recovyFlag = false;
				for (PrpLLossItemVo lossItem : lossItemVoList) {
					if ((CodeConstants.KINDCODE.KINDCODE_A.equals(lossItem.getKindCode())
							||CodeConstants.KINDCODE.KINDCODE_A.equals(lossItem.getKindCode()))
							&& !PayFlagType.NODUTY_INSTEAD_PAY.equals(lossItem.getPayFlag())) {
						if (PayFlagType.INSTEAD_PAY.equals(lossItem.getPayFlag())) {
							lossItem.setSubrogationPersonVoList(subrogationPersonVoList);
							recovyFlag = true;
						} else {
							lossItemAVo = Beans.copyDepth().from(lossItem).to(PrpLLossItemVo.class);
						}
					}
				}
				if (!recovyFlag&&lossItemAVo!=null) {
					lossItemAVo.setPayFlag(PayFlagType.INSTEAD_PAY);
					lossItemAVo.setSubrogationPersonVoList(subrogationPersonVoList);
					lossItemVoList.add(lossItemAVo);
				}
			}
		}
		
		return dwFlag;
	}


	/**
	 * 理算页面损失金额取 定损金额发票金额 较小的值
	 * 
	 * <pre></pre>
	 * 
	 * @param delLoss
	 *            定损金额
	 * @param invoiceFee
	 *            发票金额
	 * @param registNo
	 *            报案号
	 * @param flag
	 *            交强商业标识
	 * @return 欺诈 拒赔 核损金额置为0 。发票金额不为空，核损金额，发票金额取小值。
	 * @modified: *牛强(2017年2月16日 下午3:03:46): <br>
	 */
	private BigDecimal minFees(BigDecimal delLoss, BigDecimal invoiceFee,
			String registNo, String flag) {
		PrpLCertifyMainVo certifyMainVo = certifyPubService.findPrpLCertifyMainVoByRegistNo(registNo);
		if (certifyMainVo != null && flag.equals(COMP_CI)
				&& "1".equals(certifyMainVo.getIsJQFraud())) {
			return BigDecimal.ZERO;
		}
		if (certifyMainVo != null && flag.equals(COMP_BI)
				&& "1".equals(certifyMainVo.getIsSYFraud())) {
			return BigDecimal.ZERO;
		}
		if ("1".equals(certifyMainVo.getIsFraud())
				&& ("01".equals(certifyMainVo.getFraudLogo()) || "02"
						.equals(certifyMainVo.getFraudLogo()))) {
			return BigDecimal.ZERO;
		}
		if (delLoss == null) {
			return BigDecimal.ZERO;
		}
		if (invoiceFee == null) {
			return delLoss == null ? BigDecimal.ZERO : delLoss;
		}
		return delLoss.compareTo(invoiceFee)>0?invoiceFee:delLoss;

	}

	/**
	 * 
	 * <pre></pre>
	 * 
	 * @param rescueFee
	 * @param registNo
	 * @param flag
	 * @return 欺诈拒赔 施救费置为0
	 * @modified: *牛强(2017年5月23日 上午11:01:22): <br>
	 */
	private BigDecimal calculateRescueFee(BigDecimal rescueFee,
			String registNo, String flag) {
		PrpLCertifyMainVo certifyMainVo = certifyPubService
				.findPrpLCertifyMainVoByRegistNo(registNo);
		if (certifyMainVo != null && flag.equals(COMP_CI)
				&& "1".equals(certifyMainVo.getIsJQFraud())) {
			return BigDecimal.ZERO;
		}
		if (certifyMainVo != null && flag.equals(COMP_BI)
				&& "1".equals(certifyMainVo.getIsSYFraud())) {
			return BigDecimal.ZERO;
		}
		if ("1".equals(certifyMainVo.getIsFraud())
				&& ("01".equals(certifyMainVo.getFraudLogo()) || "02"
						.equals(certifyMainVo.getFraudLogo()))) {
			return BigDecimal.ZERO;
		}
		return rescueFee;
	}
	/**
	 * 处理车损数据
	 * 
	 * @param reLossCarMainVos
	 * @param caseFlag
	 * @return
	 */
	public PrpLLossItemVo processLossCarVoList(PrpLDlossCarMainVo carMainVo,
			List<PrpLCItemKindVo> itemKindVoList, String kindCode, String flag) {
		//A险和A1险是互斥的，定损选择车损险可能对应A险或者A1险
		if(itemKindVoList!=null && itemKindVoList.size()>0){
			for(PrpLCItemKindVo itemKindVo:itemKindVoList){
				if(CodeConstants.KINDCODE.KINDCODE_A.equals(kindCode) && 
						CodeConstants.KINDCODE.KINDCODE_A1.equals(itemKindVo.getKindCode())){
					kindCode = CodeConstants.KINDCODE.KINDCODE_A1;
					break;
				}
			}
		}
		PrpLLossItemVo lossItemVo = new PrpLLossItemVo();
		lossItemVo.setDlossId(carMainVo.getId());
		lossItemVo.setOtherDeductAmt(BigDecimal.ZERO);
		if(CodeConstants.KINDCODE.KINDCODE_BZ.equals(kindCode)){
			lossItemVo.setRiskCode("1101");
		}else{
			lossItemVo.setRiskCode(carMainVo.getRiskCode());
		}
		lossItemVo.setPayFlag(CodeConstants.PayFlagType.COMPENSATE_PAY);// 自付
		lossItemVo.setKindCode(kindCode);
		lossItemVo.setItemId(carMainVo.getSerialNo().toString());
		lossItemVo.setLossType(carMainVo.getDeflossCarType());
		lossItemVo.setItemName(carMainVo.getLicenseNo());
		lossItemVo.setSumLoss(minFees(carMainVo.getSumVeriLossFee(),carMainVo.getInvoiceFee(), carMainVo.getRegistNo(), flag));
		lossItemVo.setOriginLossFee(minFees(carMainVo.getSumVeriLossFee(),carMainVo.getInvoiceFee(), carMainVo.getRegistNo(), flag));
		lossItemVo.setRescueFee(calculateRescueFee(carMainVo.getSumVeriRescueFee(),carMainVo.getRegistNo(), flag));
		// 标的车需要赋值折旧率信息字段
		if (carMainVo.getSerialNo() == 1) {
			ThirdPartyDepreRate carDepreRate = compensatateService.queryThirdPartyDepreRate(carMainVo.getRegistNo());
			lossItemVo.setDepreMonthRate(new BigDecimal(carDepreRate.getDepreRate()));
			lossItemVo.setStandardPrice(new BigDecimal(carDepreRate.getPurchasePrice()));
			lossItemVo.setDepreMonths(new BigDecimal(carDepreRate.getUseMonths()));
		}
		if (itemKindVoList != null && !itemKindVoList.isEmpty()) {
			for (PrpLCItemKindVo itemKindVo : itemKindVoList) {
				if (itemKindVo.getKindCode().equals(kindCode)) {
					lossItemVo.setItemAmount(itemKindVo.getAmount());
					break;
				}
			}
		}

		return lossItemVo;
	}

	/**
	 * 处理财产损失数据
	 * 
	 * @param reLossPropMainVos
	 * @param flag
	 * @return
	 */
	public List<PrpLLossPropVo> processLossPropVoList(
			List<PrpLdlossPropMainVo> reLossPropMainVos,
			List<PrpLCItemKindVo> itemKindVoList, String flag) {
		List<PrpLLossPropVo> reLossPropList = new ArrayList<PrpLLossPropVo>();
		for (PrpLdlossPropMainVo propMainVo : reLossPropMainVos) {
			PrpLLossPropVo lossPropVo = new PrpLLossPropVo();
			lossPropVo.setPropType("1");
			lossPropVo.setDlossId(propMainVo.getId());
			lossPropVo.setRiskCode(propMainVo.getRiskCode());
			lossPropVo.setOtherDeductAmt(BigDecimal.ZERO);
			if (flag.equals(COMP_CI)) {
				lossPropVo.setKindCode("BZ");
				lossPropVo.setRiskCode("1101");
			} else {
				lossPropVo.setKindCode("B");
				if (propMainVo.getLossType().equals("1")) {// lossType
					lossPropVo.setKindCode("D2");
				}
			}
			lossPropVo.setLossType(propMainVo.getLossType());
			String propName = "";
			for (PrpLdlossPropFeeVo feeVo : propMainVo.getPrpLdlossPropFees()) {
				propName += feeVo.getLossItemName() + " ";
			}
			lossPropVo.setLossName(propName);
			lossPropVo.setItemName(propMainVo.getLicense());
			lossPropVo.setItemId(propMainVo.getSerialNo().toString());

			lossPropVo.setSumLoss(
					minFees(propMainVo.getSumVeriLoss(),
							propMainVo.getInvoiceFee(),
							propMainVo.getRegistNo(), flag));
			lossPropVo.setOriginLossFee(
					minFees(propMainVo.getSumVeriLoss(),
							propMainVo.getInvoiceFee(),
							propMainVo.getRegistNo(), flag));
			lossPropVo.setRescueFee(calculateRescueFee(propMainVo.getVerirescueFee(),propMainVo.getRegistNo(), flag));
			if (itemKindVoList != null && !itemKindVoList.isEmpty()) {
				for (PrpLCItemKindVo itemKindVo : itemKindVoList) {
					if (itemKindVo.getKindCode().equals(
							lossPropVo.getKindCode())) {
						lossPropVo.setItemAmount(itemKindVo.getAmount());
						break;
					}
				}
			}

			reLossPropList.add(lossPropVo);
		}
		return reLossPropList;
	}

	/**
	 * 处理人伤损失数据
	 * 
	 * @param lossPersTraceList
	 * @param flag
	 * @return
	 */
	public List<PrpLLossPersonVo> processLossPersonVoList(
			List<PrpLDlossPersTraceVo> lossPersTraceList, String flag) {
		// 每一条人伤跟踪任务子表下有多条费用子表PrpLDlossPersTraceFee,需要对子表的费用进行分类-医疗/死亡伤残并合计金额
		boolean SHFeeFlag = false;
		if(lossPersTraceList!=null&&lossPersTraceList.size()>0){
			// 上海机构的费用所用字典不同-判断人伤是否是上海机构人伤
			PrpLRegistVo registVo = registQueryService.findByRegistNo(lossPersTraceList.get(0).getRegistNo());
			if(registVo!=null&&"22".equals(registVo.getComCode().substring(0,2))){
				SHFeeFlag = true;
			}
		}
		for (PrpLDlossPersTraceVo PersTrace : lossPersTraceList) {
			List<PrpLDlossPersTraceFeeVo> persFeeList = new ArrayList<PrpLDlossPersTraceFeeVo>();
			PrpLDlossPersTraceFeeVo medFee = new PrpLDlossPersTraceFeeVo();
			PrpLDlossPersTraceFeeVo dehFee = new PrpLDlossPersTraceFeeVo();
			List<PrpLLossPersonFeeDetailVo> medList = new ArrayList<PrpLLossPersonFeeDetailVo>();
			List<PrpLLossPersonFeeDetailVo> dehList = new ArrayList<PrpLLossPersonFeeDetailVo>();
			medFee.setFeeTypeCode(CodeConstants.LossTypePersComp.PERSON_LOSS_MEDICAL);// 设置费用类型为医疗费用
			dehFee.setFeeTypeCode(CodeConstants.LossTypePersComp.PERSON_LOSS_DEATHDIS);// 设置费用类型为死亡伤残
			medFee.setVeriDefloss(BigDecimal.ZERO);// 在下一条人伤记录循环开始前清零
			dehFee.setVeriDefloss(BigDecimal.ZERO);
			for (PrpLDlossPersTraceFeeVo personFee : PersTrace.getPrpLDlossPersTraceFees()) {
				if (personFee.getVeriDefloss() == null) {
					continue;
				}
				PrpLLossPersonFeeDetailVo feeDetailVo = new PrpLLossPersonFeeDetailVo();
				feeDetailVo.setFeeTypeCode(personFee.getFeeTypeCode());
				feeDetailVo.setFeeTypeName(personFee.getFeeTypeName());
				feeDetailVo.setLossFee(personFee.getVeriDefloss());
				if(SHFeeFlag){
					if (CodeConstants.MedicalFee_Map_SH.containsKey(personFee.getFeeTypeCode())) {
						medFee.setVeriDefloss(medFee.getVeriDefloss().add(personFee.getVeriDefloss()));
						feeDetailVo.setLossItemNo("03");
						medList.add(feeDetailVo);
					} 
					if(CodeConstants.DisabilityFee_Map_SH.containsKey(personFee.getFeeTypeCode())) {
						dehFee.setVeriDefloss(dehFee.getVeriDefloss().add(personFee.getVeriDefloss()));
						feeDetailVo.setLossItemNo("02");
						dehList.add(feeDetailVo);
					}
				}else{
					if (CodeConstants.MedicalFee_Map.containsKey(personFee.getFeeTypeCode())) {
						medFee.setVeriDefloss(medFee.getVeriDefloss().add(personFee.getVeriDefloss()));
						feeDetailVo.setLossItemNo("03");
						medList.add(feeDetailVo);
					}
					if(CodeConstants.DisabilityFee_Map.containsKey(personFee.getFeeTypeCode())){
						dehFee.setVeriDefloss(dehFee.getVeriDefloss().add(personFee.getVeriDefloss()));
						feeDetailVo.setLossItemNo("02");
						dehList.add(feeDetailVo);
					}
				}
				
			}
			// 损失金额 取定损金额发票金额较小值  如果取到发票金额 按比例划分 医疗费和死亡伤残费
			BigDecimal minFee = minFees(PersTrace.getSumVeriDefloss(),
					PersTrace.getInvoiceFee(), PersTrace.getRegistNo(), flag);
			if(minFee.compareTo(BigDecimal.ZERO) == 0){
				medFee.setVeriDefloss(BigDecimal.ZERO);
				dehFee.setVeriDefloss(BigDecimal.ZERO);
			}else if(minFee.compareTo(PersTrace.getSumVeriDefloss() == null ? BigDecimal.ZERO : PersTrace.getSumVeriDefloss()) < 0){
					BigDecimal yiliao = medFee.getVeriDefloss();
					BigDecimal sican = dehFee.getVeriDefloss();
					BigDecimal scale = yiliao.divide(yiliao.add(sican), 2, BigDecimal.ROUND_HALF_DOWN);
					BigDecimal medSubtrahend = BigDecimal.ZERO;
					BigDecimal dehSubtrahend = BigDecimal.ZERO;
					medFee.setVeriDefloss(PersTrace.getInvoiceFee().multiply(scale));
					dehFee.setVeriDefloss(PersTrace.getInvoiceFee().subtract(PersTrace.getInvoiceFee().multiply(scale)));
					for(int i=0;i<medList.size();i++){
						if(i+1 == medList.size()){
							medList.get(i).setLossFee(medFee.getVeriDefloss().subtract(medSubtrahend));
						}else{
							BigDecimal proportion = medList.get(i).getLossFee().divide(yiliao, 2, BigDecimal.ROUND_HALF_DOWN);
							medList.get(i).setLossFee(medFee.getVeriDefloss().multiply(proportion));
							medSubtrahend = medSubtrahend.add(medFee.getVeriDefloss().multiply(proportion));
						}
					}
					for(int i=0;i<dehList.size();i++){
						if(i+1 == dehList.size()){
							dehList.get(i).setLossFee(dehFee.getVeriDefloss().subtract(dehSubtrahend));
						}else{
							BigDecimal proportion = dehList.get(i).getLossFee().divide(sican, 2, BigDecimal.ROUND_HALF_DOWN);
							dehList.get(i).setLossFee(dehFee.getVeriDefloss().multiply(proportion));
							dehSubtrahend = dehSubtrahend.add(dehFee.getVeriDefloss().multiply(proportion));
						}
					}
					
			}
			
			medFee.setPrpLLossPersonFeeDetails(medList);
			dehFee.setPrpLLossPersonFeeDetails(dehList);
			// 将医疗和伤残两个子表添加到List然后set为persTrace的子表List
			persFeeList.add(medFee);
			persFeeList.add(dehFee);
			PersTrace.setPrpLDlossPersTraceFees(persFeeList);
		}

		List<PrpLLossPersonVo> reLossPersonList = new ArrayList<PrpLLossPersonVo>();
		for (PrpLDlossPersTraceVo persTraceVo : lossPersTraceList) {
			PrpLLossPersonVo personVo = new PrpLLossPersonVo();
			personVo.setDlossId(persTraceVo.getId());
			personVo.setPersonId(persTraceVo.getId());
			PrpLDlossPersInjuredVo injuredVo = persTraceVo
					.getPrpLDlossPersInjured();
			if (flag.equals(COMP_CI)) {
				personVo.setKindCode("BZ");
			} else if (injuredVo.getLossItemType().equals("1")) {
				personVo.setKindCode("B");
			} else if (injuredVo.getLossItemType().equals("2")) {
				personVo.setKindCode("D12");
			} else if (injuredVo.getLossItemType().equals("3")) {
				personVo.setKindCode("D11");
			}
			personVo.setLossType(persTraceVo.getLossFeeType());
			personVo.setPersonName(persTraceVo.getPrpLDlossPersInjured()
					.getPersonName());
			int age = persTraceVo.getPrpLDlossPersInjured().getPersonAge()
					.intValue();
			personVo.setPersonAge(age);
			personVo.setItemName(persTraceVo.getPrpLDlossPersInjured()
					.getLicenseNo());
			// 伤情类型
			personVo.setInjuryType(injuredVo.getWoundCode());
			if (persTraceVo.getPrpLDlossPersInjured().getSerialNo() != null) {
				personVo.setItemId(persTraceVo.getPrpLDlossPersInjured()
						.getSerialNo().toString());
			}

			// 子表数据处理
			List<PrpLLossPersonFeeVo> personFeeList = new ArrayList<PrpLLossPersonFeeVo>();
			for (PrpLDlossPersTraceFeeVo perTraceFeeVo : persTraceVo
					.getPrpLDlossPersTraceFees()) {
				PrpLLossPersonFeeVo persFeeVo = new PrpLLossPersonFeeVo();
				persFeeVo.setLossItemNo(perTraceFeeVo.getFeeTypeCode());
				persFeeVo.setFeeLoss(perTraceFeeVo.getVeriDefloss());
				persFeeVo.setFeeOffLoss(BigDecimal.ZERO);
				persFeeVo.setPrpLLossPersonFeeDetails(perTraceFeeVo.getPrpLLossPersonFeeDetails());
				personFeeList.add(persFeeVo);
			}
			personVo.setPrpLLossPersonFees(personFeeList);
			personVo.setOriginLossFee(minFees(persTraceVo.getSumVeriDefloss(),
					persTraceVo.getInvoiceFee(), persTraceVo.getRegistNo(),
					flag));
			reLossPersonList.add(personVo);
		}
		return reLossPersonList;
	}

	@Override
	public CompensateVo compensateGetAllRate(PrpLCompensateVo prpLCompensate,
			List<PrpLClaimDeductVo> claimDeductVoList,
			List<PrpLLossItemVo> prpLLossItemVoList,
			List<PrpLLossPropVo> prpLLossPropVoList,
			List<PrpLLossPersonVo> prpLLossPersonVoList) {

		String registNo = prpLCompensate.getRegistNo();
		// 回写加扣免赔率信息表PrpLClaimDeduct
		if (claimDeductVoList != null && claimDeductVoList.size() > 0) {
			compensatateService.updateDeductCondForComp(claimDeductVoList,
					registNo);
		}

		for (PrpLLossItemVo lossItemVo : prpLLossItemVoList) {
			if (CodeConstants.KINDCODE.KINDCODE_G.equals(lossItemVo.getKindCode())) {
				List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService.findLossCarMainBySerialNo(registNo, 1);
				lossItemVo.setLossFeeType(lossCarMainVos.get(0).getCetainLossType());
				break;
			}
		}

		PrpLClaimVo prpLClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(registNo,prpLCompensate.getPolicyNo());
		CompensateVo compensateVo = calculatorService.orgnizeCompensateData(
				prpLCompensate, prpLLossItemVoList, prpLLossPropVoList,
				prpLLossPersonVoList, prpLClaimVo,
				CodeConstants.CompensateKind.BI_COMPENSATE);
		compensatateService.executeRulesDeductRate(compensateVo, prpLClaimVo);

		return compensateVo;
	}

	/**
	 * 处理理算主表vo
	 * 
	 * @param registNo
	 * @param reClaimVo
	 * @param cMainVo
	 * @param deductType
	 * @param checkDuty
	 * @param bcFlag
	 * @return
	 */
	private PrpLCompensateVo processCompensateMain(String registNo,
			PrpLClaimVo reClaimVo, PrpLCMainVo cMainVo, String deductType,
			PrpLCheckDutyVo checkDuty, String bcFlag) {
		PrpLCompensateVo CompensateVo = new PrpLCompensateVo();
		// 获取车损险保额
		BigDecimal AkindAmt = compensatateService
				.getAkindAmtByRegistNo(registNo);
		// 判断案件诉讼状态
		String lawFlag = compensatateService.checkLawsuitStatus(registNo,
				bcFlag);

		CompensateVo.setLawsuitFlag(lawFlag);
		CompensateVo.setCarLossAmt(AkindAmt);
		CompensateVo.setMakeCom(cMainVo.getMakeCom());
		CompensateVo.setComCode(cMainVo.getComCode());
		CompensateVo.setRiskCode(reClaimVo.getRiskCode());
		CompensateVo.setClaimNo(reClaimVo.getClaimNo());
		CompensateVo.setPolicyNo(reClaimVo.getPolicyNo());
		CompensateVo.setHandler1Code(cMainVo.getHandler1Code());
		CompensateVo.setCurrency("CNY");
		CompensateVo.setDeductType(deductType);
		CompensateVo.setIndemnityDutyRate(checkDuty.getIndemnityDutyRate());
		CompensateVo.setIndemnityDuty(checkDuty.getIndemnityDuty());
		// 是否全损-取值查勘
		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
		CompensateVo.setAllLossFlag(checkVo.getLossType());
		/*if (reClaimVo.getIsTotalLoss() == null) {
			CompensateVo.setAllLossFlag("0");
		} else {
			CompensateVo.setAllLossFlag("1");
		}*/
		CompensateVo.setCompensateType("N");
		CompensateVo.setRegistNo(registNo);
		CompensateVo.setClaimNo(reClaimVo.getClaimNo());
		// 案件类型
		if (reClaimVo.getCaseFlag() != null
				&& reClaimVo.getCaseFlag().equals("1")) {
			CompensateVo.setCaseType("2");
		} else {
			CompensateVo.setCaseType("1");
			if (reClaimVo.getIsSubRogation() != null
					&& reClaimVo.getIsSubRogation().equals("1")) {
				CompensateVo.setCaseType("3");
			}
		}
		return CompensateVo;

	}

	/**
	 * 根据险别和定损主表ID拆分应扣预付金额到每一个损失项
	 * 
	 * <pre></pre>
	 * 
	 * @param prePayList
	 *            预付赔款表
	 * @param lossItemVoList
	 *            理算车损表
	 * @param lossPropVoList
	 *            理算财产表
	 * @param lossPersList
	 *            理算人伤表
	 * @return
	 * @modified: ☆Weilanlei(2016年7月14日 下午4:58:14): <br>
	 */
	private void addPrePayAmtForLoss(List<PrpLPrePayVo> prePayList,
			List<PrpLLossItemVo> lossItemVoList,
			List<PrpLLossPropVo> lossPropVoList,
			List<PrpLLossPersonVo> lossPersList) {

		for (PrpLPrePayVo prePayVo : prePayList) {
			if (prePayVo.getLossType() != null) {
				String[] lossTypeArray = prePayVo.getLossType().split("_");

				if (lossTypeArray != null) {
					if ("car".equals(lossTypeArray[0])) {
						for (PrpLLossItemVo lossItemVo : lossItemVoList) {
							// 如果险别和定损主表ID都相同，则赋值应扣预付金额字段
							if (prePayVo.getKindCode().equals(
									lossItemVo.getKindCode())
									&& lossTypeArray[2].equals(lossItemVo
											.getDlossId().toString()) && !"4".equals(lossItemVo.getPayFlag())) {
								lossItemVo.setOffPreAmt(DataUtils.NullToZero(
										lossItemVo.getOffPreAmt()).add(
										prePayVo.getPayAmt()));
							}
						}
					}
					if ("prop".equals(lossTypeArray[0])) {
						for (PrpLLossPropVo lossPropVo : lossPropVoList) {
							// 如果险别和定损主表ID都相同，则赋值应扣预付金额字段
							if (prePayVo.getKindCode().equals(
									lossPropVo.getKindCode())
									&& lossTypeArray[2].equals(lossPropVo
											.getDlossId().toString())) {
								lossPropVo.setOffPreAmt(DataUtils.NullToZero(
										lossPropVo.getOffPreAmt()).add(
										prePayVo.getPayAmt()));
							}
						}
					}
					if ("pers".equals(lossTypeArray[0])) {
						for (PrpLLossPersonVo lossPersVo : lossPersList) {
							// 如果险别和定损主表ID都相同，则赋值应扣预付金额字段
							if (prePayVo.getKindCode().equals(lossPersVo.getKindCode())
									&& lossTypeArray[2].equals(lossPersVo.getDlossId().toString())) {
								for (PrpLLossPersonFeeVo persFeeVo : lossPersVo.getPrpLLossPersonFees()) {
									if (persFeeVo.getLossItemNo().equals(prePayVo.getChargeCode())) {
										persFeeVo.setOffPreAmt(DataUtils.NullToZero(persFeeVo.getOffPreAmt()).add(prePayVo.getPayAmt()));
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public void calculateCI(PrpLCompensateVo prpLCompensate,
			List<PrpLLossItemVo> prpLLossItemVoList,
			List<PrpLLossPropVo> prpLLossPropVoList,
			List<PrpLLossPersonVo> prpLLossPersonVoList) {

	}

	public CompensateVo calculateBI(PrpLCompensateVo prpLCompensate,
			List<PrpLClaimDeductVo> claimDeductVoList,
			List<PrpLLossItemVo> prpLLossItemVoList,
			List<PrpLLossPropVo> prpLLossPropVoList,
			List<PrpLLossPropVo> otherLossList,
			List<PrpLLossPersonVo> prpLLossPersonVoList) {

		for (PrpLLossItemVo itemVo : prpLLossItemVoList) {
			if (CodeConstants.KINDCODE.KINDCODE_G.equals(itemVo.getKindCode())) {
				List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService
						.findLossCarMainBySerialNo(
								prpLCompensate.getRegistNo(), 1);
				itemVo.setLossFeeType(lossCarMainVos.get(0).getCetainLossType());
				break;
			}
		}

		// 回写加扣免赔率信息表PrpLClaimDeduct
		if (claimDeductVoList != null && claimDeductVoList.size() > 0) {
			compensatateService.updateDeductCondForComp(claimDeductVoList,
					prpLCompensate.getRegistNo());
		}

		CompensateVo compensateResult = compensatateService.calCulator(
				prpLCompensate, prpLLossItemVoList, prpLLossPropVoList,
				prpLLossPersonVoList);

		return compensateResult;
	}

	@Override
	public List<PrpLLossItemVo> getCarLossInfo(PrpLCompensateVo compensateVo,
			List<PrpLCheckDutyVo> checkDutyList, SysUserVo userVo) {
		List<PrpLLossItemVo> prpLLossItemVoList = new ArrayList<PrpLLossItemVo>();

		String flag = COMP_CI;
		String bcFlag = "11";
		String caseFlag = "0";
		PrpLClaimVo claimVo = claimService.findByClaimNo(compensateVo
				.getClaimNo());
		if (claimVo != null) {
			caseFlag = claimVo.getCaseFlag();
		}
		if (!"1101".equals(compensateVo.getRiskCode())) {
			flag = COMP_BI;
			bcFlag = "12";
		}
		PrpLCMainVo cMainVo = policyViewService
				.getPrpLCMainByRegistNoAndPolicyNo(compensateVo.getRegistNo(),
						compensateVo.getPolicyNo());
		PrpLRegistVo registVo = registQueryService.findByRegistNo(compensateVo
				.getRegistNo());
		PrpLClaimVo reClaimVo = claimService.findByClaimNo(compensateVo
				.getClaimNo());
		// 锁定校验 代位求偿案件请先完成锁定确认 TODO 代位接口未调通 暂时不放开
		List<PrpLPlatLockVo> platLockList = new ArrayList<PrpLPlatLockVo>();
		PrpLCItemCarVo itemCarVo = cMainVo.getPrpCItemCars().get(0);
		String carKindCode = itemCarVo.getCarKindCode();
		// 摩托车 拖拉机 1208 1209 1203不送平台 
		//非上海机构 1203 1208 1209 不传平台 及非上海机构的摩托车拖拉机的交强险1101也不送平台
		boolean dwPersFlag = false;
		PrpLSubrogationMainVo PrpLSubrogationMainVo = subrogationService.find(compensateVo.getRegistNo());
		List<PrpLSubrogationPersonVo> subrogationPersonVoList = PrpLSubrogationMainVo.getPrpLSubrogationPersons();
		if(CodeConstants.CommonConst.TRUE.equals(PrpLSubrogationMainVo.getSubrogationFlag()) &&
				subrogationPersonVoList!=null&&subrogationPersonVoList.size()>0){
			dwPersFlag = true;
		}
		if (!dwPersFlag&&cMainVo.getComCode().startsWith("22")) {
				platLockList = this.organizeRecoveryList(registVo, cMainVo,
						reClaimVo, compensateVo, userVo);
		} else {
			if (!dwPersFlag&&!(Risk.isDBC(cMainVo.getRiskCode()))
					&& (!Risk.isDBT(cMainVo.getRiskCode()))
					&& (!Risk.isDAC(cMainVo.getRiskCode()))
					&& (StringUtils.isBlank(carKindCode) || "J,M"
							.indexOf(carKindCode.substring(0, 1)) == -1)) {
				platLockList = this.organizeRecoveryList(registVo, cMainVo,
						reClaimVo, compensateVo, userVo);
			}
		}
		// 初始化车辆定损信息
		prpLLossItemVoList = this.initCompensateCarLossInfo(
				compensateVo.getRegistNo(), bcFlag, flag, caseFlag, cMainVo,
				checkDutyList);

		// 增加代位数据
		Map<String, String> carMap = new HashMap<String, String>();// 车辆信息
		for (PrpLCheckDutyVo checkDutyVo : checkDutyList) {
			carMap.put(checkDutyVo.getLicenseNo(), checkDutyVo.getSerialNo()
					.toString());
		}
		Map<String, String> qfLicenseMap = new HashMap<String, String>();// 清付车牌号
		String dwFlag = this.organizeDwILossItem(platLockList,
				prpLLossItemVoList, qfLicenseMap, carMap);
		return prpLLossItemVoList;
	}

	public List<PrpLLossItemVo> initCompensateCarLossInfo(String registNo,
			String bcFlag, String flag, String caseFlag, PrpLCMainVo cMainVo,
			List<PrpLCheckDutyVo> checkDutyList) {
		/**
		 * 车辆损失信息
		 */
		List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService
				.findLossCarMainByRegistNo(registNo);
		PrpLDlossCarMainVo mainLossCarVo = null;
		List<PrpLDlossCarMainVo> reLossCarMainVos = new ArrayList<PrpLDlossCarMainVo>();
		if (lossCarMainVos != null) {
			for (PrpLDlossCarMainVo lossCarMainVo : lossCarMainVos) {
				if (compensatateService.checkLossState(
						lossCarMainVo.getLossState(), bcFlag)
						&& lossCarMainVo.getUnderWriteFlag().equals("1")) {

					if (lossCarMainVo.getSerialNo() == 1) {
						mainLossCarVo = lossCarMainVo;
					}
					// 三者车无损失 无责 展示不了无责代赔 --先不排除无损失的三者车，等到无责代赔车辆数据组织完毕之后再排除
					if (flag.equals(COMP_CI)) {
						if (caseFlag.equals("1")) {
							// 交强-是互碰自赔取本车定损，不是互碰自赔取三者车
							if (lossCarMainVo.getDeflossCarType().equals("1")) {
								reLossCarMainVos.add(lossCarMainVo);
							}
						} else {
							int i = Integer.parseInt(lossCarMainVo
									.getDeflossCarType());
							if (i > 1) {
								if (i == 2) {
									lossCarMainVo.setDeflossCarType("3");
								}
								reLossCarMainVos.add(lossCarMainVo);
							}
						}
					} else {
						if (lossCarMainVo.getDeflossCarType().equals("2")) {
							lossCarMainVo.setDeflossCarType("3");
						}
						reLossCarMainVos.add(lossCarMainVo);
					}
				}
			}
		}
		List<PrpLLossItemVo> rePrpLLossItemVoList = new ArrayList<PrpLLossItemVo>();
		// 交强理算，把车损数据放到LossItemVo中方便页面展示
		if (flag.equals(COMP_CI)) {
			for (PrpLDlossCarMainVo carMainVo : reLossCarMainVos) {
				PrpLLossItemVo lossItemVo = processLossCarVoList(carMainVo,
						null, "BZ", flag);
				rePrpLLossItemVoList.add(lossItemVo);
			}
		}
		// 商业计算书需要重新组织车损数据
		if (flag.equals(COMP_BI) && !"1".equals(caseFlag)) {// 商业险互碰自赔不展示车损失
			rePrpLLossItemVoList = new ArrayList<PrpLLossItemVo>();
			List<PrpLLossItemVo> tempPrpLLossItemList = new ArrayList<PrpLLossItemVo>();
			for (PrpLDlossCarMainVo carMainVo : reLossCarMainVos) {
				if (carMainVo.getCetainLossType().equals("05")) {
					// 定损方式cetainLossType为05（无损失），该车不参与理算
					// rePrpLLossItemVoList = null;
					continue;
				} else {
					if (carMainVo.getDeflossCarType().equals("1")) {
						// 查询计算无责代赔金额，放在标的车损失记录下
						BigDecimal noDutyAmt = compensatateService
								.getNoDutyAmt(registNo);
						// 标的车定损方式cetainLossType为02（推定全损）时赋值kindcode为A险，车损险
						if (carMainVo.getLossFeeType() == null) {// 没有可选的损失类别
							continue;
						}
						// 考虑涉水险金额
						BigDecimal sumWadFee = BigDecimal.ZERO;// 涉水核损金额
						String wadKindCode = null;
						List<PrpLDlossCarCompVo> compList = carMainVo
								.getPrpLDlossCarComps();
						if (compList != null && !compList.isEmpty()) {
							for (PrpLDlossCarCompVo lossCompVo : compList) {
								if ("1".equals(lossCompVo.getWadFlag())) {
									wadKindCode = lossCompVo.getKindCode();
									sumWadFee = sumWadFee
											.add(DataUtils
													.NullToZero(lossCompVo
															.getSumVeriLoss()))
											.subtract(
													DataUtils
															.NullToZero(lossCompVo
																	.getVeriRestFee()));
								}
							}
						}
						// 涉水险组织数据
						if (sumWadFee.compareTo(BigDecimal.ZERO) == 1) {
							PrpLDlossCarMainVo wadCarMainVo = Beans.copyDepth()
									.from(carMainVo)
									.to(PrpLDlossCarMainVo.class);
							wadCarMainVo.setSumVeriLossFee(sumWadFee);
							wadCarMainVo.setSumRescueFee(BigDecimal.ZERO);

							tempPrpLLossItemList.add(processLossCarVoList(
									wadCarMainVo, cMainVo.getPrpCItemKinds(),
									wadKindCode, flag));
						}
						BigDecimal leftFee = carMainVo.getSumVeriLossFee()
								.subtract(sumWadFee);
						if (leftFee.compareTo(BigDecimal.ZERO) == 1
								|| carMainVo.getSumRescueFee().compareTo(
										BigDecimal.ZERO) == 1) {
							carMainVo.setSumVeriLossFee(leftFee);// 核损金额 减去
																	// 涉水险金额
							String kindCode = "";
							if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(carMainVo.getRiskCode()) != null &&
									CodeConstants.ISNEWCLAUSECODE2020_MAP.get(carMainVo.getRiskCode())){
								kindCode = CodeConstants.LossFee2020Kind_Map.get(carMainVo.getRiskCode()+carMainVo.getLossFeeType());
							}else{
								kindCode = CodeConstants.LossFeeKind_Map.get(carMainVo.getLossFeeType());
							}
							
							tempPrpLLossItemList.add(processLossCarVoList(
									carMainVo, cMainVo.getPrpCItemKinds(),
									kindCode, flag));
						}

						for (PrpLLossItemVo tempItem : tempPrpLLossItemList) {
							if (!rePrpLLossItemVoList.contains(tempItem)) {
								tempItem.setAbsolvePayAmt(noDutyAmt);
								rePrpLLossItemVoList.add(tempItem);
							}
						}

					} else {
						// 若为商业险理算，则赋值三者车为B险，第三者责任险
						PrpLLossItemVo thirdLossItem = processLossCarVoList(
								carMainVo, cMainVo.getPrpCItemKinds(), "B",
								flag);
						rePrpLLossItemVoList.add(thirdLossItem);
					}
				}
			}
		}
		/**
		 * 2016-10-18 防止重开赔案-理算冲销-追加定损 后车财人损失一个损失项有多条损失主表 超保额管控出错
		 * 故在组织完损失项后遍历损失项List按照serialNo汇总一次金额（施救费和定损金额）,DLossIdExt字段存多张主表的ID
		 */
		if (rePrpLLossItemVoList != null && rePrpLLossItemVoList.size() > 0) {
			Map<String, PrpLLossItemVo> seriNoItemVoMap = new HashMap<String, PrpLLossItemVo>();// 损失项序号
																								// 和
																								// 损失项Vo
																								// Map
			for (PrpLLossItemVo itemVo : rePrpLLossItemVoList) {
				String NoKindKey = itemVo.getItemId() + itemVo.getKindCode();
				if (seriNoItemVoMap == null || seriNoItemVoMap.size() == 0) {
					seriNoItemVoMap.put(NoKindKey, itemVo);
				} else {
					if (seriNoItemVoMap.containsKey(NoKindKey)) {
						seriNoItemVoMap.get(NoKindKey).setRescueFee(
								DataUtils.NullToZero(
										seriNoItemVoMap.get(NoKindKey)
												.getRescueFee()).add(
										DataUtils.NullToZero(itemVo
												.getRescueFee())));// 施救费
						seriNoItemVoMap.get(NoKindKey).setSumLoss(
								seriNoItemVoMap.get(NoKindKey).getSumLoss()
										.add(itemVo.getSumLoss()));// 定损金额
						String DlossId = null;
						if (seriNoItemVoMap.get(NoKindKey).getDlossIdExt() == null) {
							DlossId = itemVo.getDlossId().toString();
						} else {
							DlossId = seriNoItemVoMap.get(NoKindKey)
									.getDlossIdExt()
									+ ","
									+ itemVo.getDlossId();// 定损主表扩展ID
						}
						seriNoItemVoMap.get(NoKindKey).setDlossIdExt(DlossId);
					} else {
						seriNoItemVoMap.put(NoKindKey, itemVo);
					}
				}
			}
			rePrpLLossItemVoList = new ArrayList<PrpLLossItemVo>();
			// 重新添加Vo
			for (PrpLLossItemVo vo : seriNoItemVoMap.values()) {
				// 保单未承保该险别则初始化不显示该损失项
				if (HadBuyTheKind(vo.getKindCode(), cMainVo)) {
					rePrpLLossItemVoList.add(vo);
				}
			}
		}

		/**
		 * 查询无责代赔车辆
		 */
		List<String> noDutyLicenseNo = new ArrayList<String>();// 获取无责代赔车辆车牌号
		PrpLCheckDutyVo checkDuty = new PrpLCheckDutyVo();// 标的车责任类型
		String itemDutyFlag = "";
		if (checkDutyList != null) {
			for (PrpLCheckDutyVo checkDutyVo : checkDutyList) {
				if (checkDutyVo.getSerialNo() == 1) {
					checkDuty = checkDutyVo;
					// 先判断标的有责，再判断三者车无责
					if (checkDutyVo.getIndemnityDutyRate().compareTo(
							BigDecimal.ZERO) == 1) {
						itemDutyFlag = ins.sino.claimcar.CodeConstants.DutyType.CIINDEMDUTY_Y;
					}
				} else {
					if (checkDutyVo.getCiDutyFlag() != null
							&& checkDutyVo
									.getCiDutyFlag()
									.equals(ins.sino.claimcar.CodeConstants.DutyType.CIINDEMDUTY_N)) {
						noDutyLicenseNo.add(checkDutyVo.getLicenseNo());
					}
				}
			}
			if (flag.equals(COMP_CI)) {
				if (!itemDutyFlag
						.equals(ins.sino.claimcar.CodeConstants.DutyType.CIINDEMDUTY_Y)) {
					// 如果标的车无责，则所有车损数据不显示
					rePrpLLossItemVoList = null;
					noDutyLicenseNo = null;
				} else if (noDutyLicenseNo != null) {
					// 如果为无责代赔，改写无责代赔三者车的支付类型payFlag为无责代赔
					List<PrpLLossItemVo> noDutyLossItemVoList = new ArrayList<PrpLLossItemVo>();
					for (PrpLLossItemVo lossItem : rePrpLLossItemVoList) {
						if (noDutyLicenseNo.contains(lossItem.getItemName())) {
							PrpLLossItemVo noDutyLossItem = Beans.copyDepth()
									.from(lossItem).to(PrpLLossItemVo.class);
							noDutyLossItem.setPayFlag("4");
							if (mainLossCarVo != null) {
								noDutyLossItem.setSumLoss(mainLossCarVo
										.getSumVeriLossFee());
								noDutyLossItem.setRescueFee(mainLossCarVo
										.getSumVeriRescueFee());
							} else {
								noDutyLossItem.setSumLoss(BigDecimal.ZERO);
								noDutyLossItem.setRescueFee(BigDecimal.ZERO);
							}
							// 单证保存的数据
							for (PrpLCheckDutyVo checkDutyVo : checkDutyList) {
								if (lossItem.getItemId().equals(
										checkDutyVo.getSerialNo().toString())) {
									noDutyLossItem
											.setInsuredComCode(checkDutyVo
													.getCiInsureComCode());
									noDutyLossItem.setNoDutyPayFlag(checkDutyVo
											.getNoDutyPayFlag());
									noDutyLossItem.setCiPolicyNo(checkDutyVo
											.getCiPolicyNo());
									break;
								}

							}

							noDutyLossItemVoList.add(noDutyLossItem);

						}
					}

					rePrpLLossItemVoList.addAll(noDutyLossItemVoList);
				}
			}
		}

		// 排除掉损失为无且payflag!=4（不是无责代赔）的车辆信息
		List<PrpLLossItemVo> removeCarVoList = new ArrayList<PrpLLossItemVo>();
		if (rePrpLLossItemVoList != null && rePrpLLossItemVoList.size() > 0) {
			for (PrpLLossItemVo lossItem : rePrpLLossItemVoList) {
				if (lossItem.getSumLoss().compareTo(BigDecimal.ZERO) == 0
						&& lossItem.getRescueFee().compareTo(BigDecimal.ZERO) == 0
						&& !"4".equals(lossItem.getPayFlag())) {
					removeCarVoList.add(lossItem);
				}
			}
		}

		if (removeCarVoList != null && removeCarVoList.size() > 0) {
			rePrpLLossItemVoList.removeAll(removeCarVoList);
		}
		return rePrpLLossItemVoList;
	}
	
	@Override
	public boolean subrogationIsLock(PrpLSubrogationMainVo subrogationMainVo,String policyNo) throws Exception{
        boolean isLock = true;
        String registNo = subrogationMainVo.getRegistNo();
        PrpLClaimVo claimVo = claimService.findClaimVoByRegistNoAndPolicyNo(registNo,policyNo);
        PrpLCMainVo prpLcMainVo = policyViewService.getPrpLCMainByRegistNoAndPolicyNo(registNo,policyNo);
        
        if(!prpLcMainVo.getComCode().startsWith("22")){
            List<PrpLPlatLockVo> recoveryLockList = platLockService.findPrpLPlatLockVoList(claimVo.getRegistNo(),null, "1");
            if (recoveryLockList == null) {
                isLock = false;
            }
        }else {
            String requestType = RequestType.RegistInfoBI.getCode();
            if (Risk.isDQZ(claimVo.getRiskCode())) {// 交强险
                if (prpLcMainVo.getComCode().startsWith("22")) {
                    requestType = RequestType.RegistInfoCI_SH.getCode();
                } else {
                    requestType = RequestType.RegistInfoCI.getCode();
                }
            } else {
                if (prpLcMainVo.getComCode().startsWith("22")) {
                    requestType = RequestType.RegistInfoBI_SH.getCode();
                }
            }
			PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
			//联共保案件，从共，从联不送平台，没有理赔编码
			if( !CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())&& !CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){

				CiClaimPlatformLogVo formLogVo = platformLogService.findLogByBussNo(requestType,claimVo.getRegistNo(),prpLcMainVo.getComCode());
				if(formLogVo==null||StringUtils.isEmpty(formLogVo.getClaimSeqNo())){
					throw new IllegalArgumentException("该案件平台理赔编码为空,请核查");
				}

				if(prpLcMainVo.getComCode().startsWith("22")){// 上海平台，代位
					SubrogationSHQueryVo queryVo = new SubrogationSHQueryVo();
					queryVo.setComCode(prpLcMainVo.getComCode());
					queryVo.setRegistNo(registNo);//
					queryVo.setClaimSeqNo(formLogVo.getClaimSeqNo());
					// 调用上海平台的 -- 代位求偿信息抄回接口信息查询yzy
					List<CopyInformationResultVo> resultVo = subrogationSHHandleService.sendCopyInformationToSubrogationSH(queryVo);
					if(resultVo!=null&&resultVo.size()>0){
						for(CopyInformationResultVo vo:resultVo){
							if("0".equals(vo.getResponseCode())){
								if("1".equals(subrogationMainVo.getSubrogationFlag())&& !"1101".equals(claimVo.getRiskCode())){
									isLock = false;
								}
							}else if("1".equals(vo.getResponseCode())){
								String sign = "0";// 我方是追偿方，看是否锁定--0未锁定，1已锁定
								if("1".equals(subrogationMainVo.getSubrogationFlag())&& !"1101".equals(claimVo.getRiskCode())){
									for(CopyInformationResultVo cvo:resultVo){
										if(cvo.getSubrogationViewVo().getInsurerCode().startsWith("EDHI")){
											sign = "1";
										}
									}
									if("0".equals(sign)){
										isLock = false;
									}
								}
							}
						}
					}
				}
			}
		}
        return isLock;
    }
	

	@Override
	public List<PrpLPlatLockVo> organizeRecoveryListByCerti(
			PrpLRegistVo registVo, PrpLCMainVo prpLcMainVo,
			PrpLClaimVo claimVo, SysUserVo userVo) {
		List<PrpLPlatLockVo> platLockList = new ArrayList<PrpLPlatLockVo>();

		PrpLSubrogationMainVo subrogationMainVo = subrogationService
				.find(claimVo.getRegistNo());
		List<PrpLSubrogationCarVo> subrogtaionCarList = subrogationMainVo
				.getPrpLSubrogationCars();
		if ("1".equals(subrogationMainVo.getSubrogationFlag())
				&& subrogtaionCarList != null && !subrogtaionCarList.isEmpty()) {

			List<PrpLPlatLockVo> platLockVo = subrogationService
					.findPrpLPlatLockVoByRegistNo(claimVo.getRegistNo(), null);
			if (platLockVo == null) {
				return platLockList;
			}
		}

		if (prpLcMainVo.getComCode().startsWith("22")) {// 上海平台

		} else {

			SubrogationQueryVo queryVo = new SubrogationQueryVo();
			String requestType = RequestType.RegistInfoBI.getCode();
			if (Risk.isDQZ(claimVo.getRiskCode())) {// 交强险
				requestType = RequestType.RegistInfoCI.getCode();
			}
			if( !CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())&& !CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){

				CiClaimPlatformLogVo formLogVo = platformLogService.findLogByBussNo(requestType,claimVo.getRegistNo(),prpLcMainVo.getComCode());
				if(formLogVo==null||formLogVo.getClaimSeqNo()==null){
					throw new IllegalArgumentException("该案件平台理赔编码为空,请核查");
				}
				queryVo.setComCode(prpLcMainVo.getComCode());
				queryVo.setRegistNo(claimVo.getRegistNo());
				queryVo.setClaimCode(formLogVo.getClaimSeqNo());
				queryVo.setConfirmSequenceNo(prpLcMainVo.getValidNo());// 投保确认码
				try{
					if(Risk.isDQZ(claimVo.getRiskCode())){// 交强险
						List<BeSubrogationVo> subrogationRiskList = subrogationToPlatService.sendBeSubrogationQueryCI(queryVo);
						if(subrogationRiskList!=null&& !subrogationRiskList.isEmpty()){
							platLockList = getBeSubrogationList(formLogVo.getClaimSeqNo(),claimVo,registVo,subrogationRiskList,userVo);
						}

					}else{
						List<BeSubrogationVo> subrogationRiskList = subrogationToPlatService.sendBeSubrogationQueryBI(queryVo);
						if(subrogationRiskList!=null&& !subrogationRiskList.isEmpty()){
							platLockList = getBeSubrogationList(formLogVo.getClaimSeqNo(),claimVo,registVo,subrogationRiskList,userVo);
						}
						// 查询追偿的数据
						List<PrpLPlatLockVo> recoveryLockList = platLockService.findPrpLPlatLockVoList(claimVo.getRegistNo(),claimVo.getPolicyNo(),
								"1");
						if(recoveryLockList!=null&& !recoveryLockList.isEmpty()){
							platLockList.addAll(recoveryLockList);
						}
					}

				}catch(Exception e){
					e.printStackTrace();
					throw new IllegalArgumentException("调用平台接口异常！");
				}
			}
		}
		return platLockList;
	}
	
	//更新锁定表结算码状态yzy
	private List<PrpLPlatLockVo> updateRecoveryCodeStatusOfPrpLPlatLock(PrpLRegistVo registVo,PrpLCMainVo prpLcMainVo, PrpLClaimVo claimVo,
			PrpLCompensateVo compVo, SysUserVo userVo){
		List<PrpLPlatLockVo>  prpLPlatLockVos=subrogationService.findPlatLockVoByPayFlag(registVo.getRegistNo());
		List<PrpLPlatLockVo>  prpLPlatLockLists=new ArrayList<PrpLPlatLockVo>();
		if(prpLPlatLockVos!=null && prpLPlatLockVos.size()>0){
			for(PrpLPlatLockVo vos:prpLPlatLockVos){
				if(claimVo!=null){
					if("1101".equals(claimVo.getRiskCode())){
						if("1101".equals(vos.getRiskCode())){
							prpLPlatLockLists.add(vos);
						}
					}else{
						if(!"1101".equals(vos.getRiskCode())){
							prpLPlatLockLists.add(vos);
						}
					}
				}
				
			}
		}
		String requestType = RequestType.RegistInfoBI.getCode();
		if (Risk.isDQZ(claimVo.getRiskCode())) {// 交强险
			  if (prpLcMainVo.getComCode().startsWith("22")) {
				requestType = RequestType.RegistInfoCI_SH.getCode();
			   } 
		} else {
			 if (prpLcMainVo.getComCode().startsWith("22")) {
				requestType = RequestType.RegistInfoBI_SH.getCode();
			  }
		   }
		if( !CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())&& !CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			CiClaimPlatformLogVo formLogVo = platformLogService.findLogByBussNo(requestType,claimVo.getRegistNo(),prpLcMainVo.getComCode());
			if(formLogVo==null||StringUtils.isEmpty(formLogVo.getClaimSeqNo())){
				throw new IllegalArgumentException("该案件平台理赔编码为空,请核查");
			}

			if(prpLcMainVo.getComCode().startsWith("22")){// 上海平台，代位
				SubrogationSHQueryVo queryVo = new SubrogationSHQueryVo();
				queryVo.setComCode(prpLcMainVo.getComCode());
				queryVo.setRegistNo(claimVo.getRegistNo());
				queryVo.setClaimSeqNo(formLogVo.getClaimSeqNo());
				// 调用上海平台的 -- 代位求偿信息抄回接口信息查询
				List<CopyInformationResultVo> resultVo;
				try{
					resultVo = subrogationSHHandleService.sendCopyInformationToSubrogationSH(queryVo);

					if(resultVo!=null&&resultVo.size()>0){
						for(CopyInformationResultVo vo:resultVo){
							if(vo.getSubrogationViewVo()!=null){
								if(StringUtils.isNotBlank(vo.getSubrogationViewVo().getRecoveryCodeStatus())){
									if(prpLPlatLockLists!=null&&prpLPlatLockLists.size()>0){
										for(PrpLPlatLockVo prplplatLock:prpLPlatLockLists){
											if(vo.getSubrogationViewVo().getRecoveryCode().equals(prplplatLock.getRecoveryCode())){
												PrpLPlatLock po = new PrpLPlatLock();
												Beans.copy().from(prplplatLock).to(po);
												po.setRecoveryCodeStatus(vo.getSubrogationViewVo().getRecoveryCodeStatus());
												databaseDao.update(PrpLPlatLock.class,po);
											}
										}
									}
								}
							}
						}
					}

				}catch(Exception e){
					// TODO Auto-generated catch block
					// e.printStackTrace();
					throw new IllegalArgumentException("调用上海平台代位求偿信息抄回接口信息查询异常！");
				}

			}
		}
		return prpLPlatLockLists;
	}

    @Override
    public void syncInsuredPhone(String registNo,String insuredPhone) {
        if(StringUtils.isNotBlank(registNo) && StringUtils.isNotBlank(insuredPhone)){
            QueryRule qr = QueryRule.getInstance();
            qr.addEqual("registNo", registNo);
            List<PrpLCompensate> compPoList = databaseDao.findAll(PrpLCompensate.class, qr);
            if(compPoList!=null&&compPoList.size()>0){
                for(PrpLCompensate compPo:compPoList){
                    compPo.setinsuredPhone(insuredPhone);
                    databaseDao.update(PrpLCompensate.class,compPo);
                }
            }
        }
        
    }

	@Override
	public void saveOrUpdatePrplcomcontext(PrplcomcontextVo vo)throws Exception{
		Prplcomcontext po=new Prplcomcontext();
		if(vo!=null){
			if(vo.getId()!=null){
				Beans.copy().from(vo).to(po);
				databaseDao.update(Prplcomcontext.class,po);
			}else{
				Beans.copy().from(vo).to(po);
				databaseDao.save(Prplcomcontext.class,po);
			}
		}
		
	}

	@Override
	public PrplcomcontextVo findPrplcomcontextByCompensateNo(String compensateNo,String type) {
		PrplcomcontextVo vo=null;
		QueryRule rule=QueryRule.getInstance();
		rule.addEqual("compensateNo",compensateNo);
		rule.addEqual("nodeSign",type);
		List<Prplcomcontext>  listpos=databaseDao.findAll(Prplcomcontext.class,rule);
		if(listpos!=null && listpos.size()>0){
			vo=new PrplcomcontextVo();
			Beans.copy().from(listpos.get(0)).to(vo);
		}
		return vo;
	}

	@Override
	public PrplcomcontextVo findPrplcomcontextById(Long id) {
		PrplcomcontextVo vo=null;
		QueryRule rule=QueryRule.getInstance();
		rule.addEqual("id",id);
		Prplcomcontext po=databaseDao.findUnique(Prplcomcontext.class, rule);
		if(po!=null){
			vo=new PrplcomcontextVo();
			Beans.copy().from(po).to(vo);
		}
		return vo;
		
	}

	@Override
	public boolean saveBeforeCheck(List<PrpLPaymentVo> prpLPaymentVoList) {
		for(PrpLPaymentVo prplPaymentVo :prpLPaymentVoList){
			if(!"2".equals(prplPaymentVo.getPayObjectKind())){
				if(StringUtils.isBlank(prplPaymentVo.getOtherCause())){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isAmontEqual(List<PrpLPaymentVo> prpLPaymentVoList,
			PrpLCompensateVo prpLCompensate) {
		prpLPaymentVoList.removeAll(Collections.singleton(null));
		BigDecimal sumAmount = BigDecimal.ZERO;
		if(prpLPaymentVoList != null && prpLPaymentVoList.size()>0){
			for(PrpLPaymentVo prpLPaymentVo:prpLPaymentVoList){
				sumAmount = sumAmount.add(prpLPaymentVo.getSumRealPay());
			}
		}
		if(prpLCompensate != null){
			if(sumAmount.compareTo(prpLCompensate.getSumPaidAmt()) == 0){
				return false;
			}
		}
		return true;
	}
	/**
	 * 校验本次计算书的“赔款总金额”与“损失赔偿情况列表(人伤+财损+车损)”中的“赔偿金额”之和一致；
	 * <pre></pre>
	 * @param prpLCompensate
	 * @param prpLLossPropVoList
	 * @param prpLLossPersonVoList
	 * @param prpLLossItemVoList
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年1月26日 下午2:53:17): <br>
	 */
	private boolean validTotalTermEqualSubItem(PrpLCompensateVo prpLCompensate,List<PrpLLossPropVo> prpLLossPropVoList,
	                            			List<PrpLLossPersonVo> prpLLossPersonVoList,
	                                          List<PrpLLossItemVo> prpLLossItemVoList){
		String policyComCode = prpLCompensate.getComCode();
		String classType = Risk.DQZ.equals(prpLCompensate.getRiskCode()) ? PolicyType.POLICY_DZA : PolicyType.POLICY_DAA;
		if(!policyComCode.startsWith(CodeConstants.PolicyNoFromt_SH)){//全国平台
			if(classType.equals(PolicyType.POLICY_DZA)){//交强险校验理算信息列表中的“赔偿金额”之和与“损失赔偿情况列表”中的“赔偿金额”之和一致
				BigDecimal sumAmt = prpLCompensate.getSumAmt();
				BigDecimal lossSumAmt = new BigDecimal("0");
				if(prpLLossPropVoList!=null && prpLLossPropVoList.size()>0){//财损
					prpLLossPropVoList.removeAll(Collections.singleton(null));
					for(PrpLLossPropVo prpLLossPropVo : prpLLossPropVoList){
						BigDecimal tempSumRealPay = prpLLossPropVo.getSumRealPay();
						lossSumAmt = lossSumAmt.add(tempSumRealPay);
					}
				}
				if(prpLLossItemVoList!=null && prpLLossItemVoList.size()>0){//车损
					prpLLossItemVoList.removeAll(Collections.singleton(null));
					for(PrpLLossItemVo prpLLossItemVo : prpLLossItemVoList){
						if(prpLLossItemVo != null){
							BigDecimal tempSumRealPay = prpLLossItemVo.getSumRealPay();
							lossSumAmt = lossSumAmt.add(tempSumRealPay);
						}
					}
				}	
				if(prpLLossPersonVoList!=null && prpLLossPersonVoList.size()>0){//人伤
					prpLLossPersonVoList.removeAll(Collections.singleton(null));
					for(PrpLLossPersonVo prpLLossPersonVo : prpLLossPersonVoList){
						List<PrpLLossPersonFeeVo> prpLLossPersonFeeVos = prpLLossPersonVo.getPrpLLossPersonFees();
						if(prpLLossPersonFeeVos!=null && prpLLossPersonFeeVos.size()>0){
							for(PrpLLossPersonFeeVo prpLLossPersonFeeVo : prpLLossPersonFeeVos){
								BigDecimal tempSumRealPay = prpLLossPersonFeeVo.getFeeRealPay();
								lossSumAmt = lossSumAmt.add(tempSumRealPay);
							}
						}
					}
				}	
				if(lossSumAmt.compareTo(sumAmt) != 0){
					return true;
				}	
			}
		} 
		return false;
	}
	public void  validData(Map<String,Object> itemMap) throws Exception{
		PrpLCompensateVo prpLCompensate = (PrpLCompensateVo)itemMap.get("prpLCompensate");
		List<PrpLLossItemVo> prpLLossItemVoList = (List<PrpLLossItemVo>)itemMap.get("prpLLossItemVoList");
		List<PrpLLossPersonVo> prpLLossPersonVoList = (List<PrpLLossPersonVo>)itemMap.get("prpLLossPersonVoList");
		List<PrpLLossPropVo> prpLLossPropVoList = (List<PrpLLossPropVo>)itemMap.get("prpLLossPropVoList");
		if(validTotalTermEqualSubItem(prpLCompensate,prpLLossPropVoList,prpLLossPersonVoList,prpLLossItemVoList)){
			throw new IllegalArgumentException("总赔款金额与损失赔偿情况列表总金额不一致");
		}
	}

	@Override
	public void updatePrpLCompensate(PrpLCompensateVo vo) {
		PrpLCompensate po = new PrpLCompensate();
		Beans.copy().from(vo).to(po);
		databaseDao.update(PrpLCompensate.class,po);
	}
}
