package ins.sino.claimcar.claim.services.handle;

import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BillNoService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.UnderWriteFlag;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.ConfigService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.PrpLClaimKindVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateExtVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.claim.vo.SubmitNextVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.AMLVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.other.vo.SendMsgParamVo;
import ins.sino.claimcar.pay.service.PadPayPubService;
import ins.sino.claimcar.pay.service.PrePayHandleService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCInsuredVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.rule.service.ClaimRuleApiService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;
import com.sinosoft.insaml.apiclient.FxqCrmRiskService;
import com.sinosoft.insaml.povo.vo.CrmRiskInfoVo;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("prePayHandleService")
public class PrePayHandleServiceImpl implements PrePayHandleService {
	@Autowired
	private ManagerService managerService;
	@Autowired
	CompensateTaskService  compensateTaskService;
	@Autowired
	ClaimService claimService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	@Autowired
	CompensateService compensateService;
	@Autowired
	BillNoService billNoService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	ClaimRuleApiService claimRuleApiService;
	@Autowired
	LossCarService lossCarService;
	@Autowired
	PropTaskService propTaskService;
	@Autowired
	PersTraceDubboService persTraceDubboService;
	@Autowired
	AssignService assignService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	PadPayPubService padPayPubService;
	@Autowired
    PrpLCMainService prpLCMainService;
	@Autowired
	private ConfigService configService;
	
	private static Logger logger = LoggerFactory.getLogger(PrePayHandleServiceImpl.class);
	/**
	 * 预付任务发起
	 * @param claimNoArr
	 * @throws Exception 
	 */
	@Override
	public void prePayTaskApply(String[] claimNoArr,SysUserVo userVo) throws Exception {
		List<PrpLWfTaskVo> wfTaskVoList = new ArrayList<PrpLWfTaskVo>();

		for(String claimNo:claimNoArr){
			PrpLClaimVo claimVo = claimService.findByClaimNo(claimNo);
			if("0".equals(claimVo.getValidFlag())){
				throw new Exception("该立案："+claimNo+"已注销，不能发起预付任务！");
			}

			WfTaskSubmitVo submitVo = new WfTaskSubmitVo();

			String prePayNo = "";
			if("1101".equals(claimVo.getRiskCode())){
				submitVo.setCurrentNode(FlowNode.ClaimCI);
				submitVo.setNextNode(FlowNode.PrePayCI);
				prePayNo = billNoService.getPrePayNo(policyViewService.getPolicyComCode(claimVo.getRegistNo()),claimVo.getRiskCode());
			}else{
				submitVo.setCurrentNode(FlowNode.ClaimBI);
				submitVo.setNextNode(FlowNode.PrePayBI);
				prePayNo = billNoService.getPrePayNo(policyViewService.getPolicyComCode(claimVo.getRegistNo()),claimVo.getRiskCode());
			}

			// 查询立案已处理
			List<PrpLWfTaskVo> wfTaskVos = wfTaskHandleService.findEndTask(claimVo.getRegistNo(),claimNo,
					submitVo.getCurrentNode());
			if(wfTaskVos!=null&&wfTaskVos.size()>0){

				submitVo.setFlowId(wfTaskVos.get(0).getFlowId());
				submitVo.setFlowTaskId(wfTaskVos.get(0).getTaskId());
			}else{
				throw new Exception("没有查询到立案节点！");
			}

			// 查询查勘已处理
			List<PrpLWfTaskVo> wfCheckTaskVos = wfTaskHandleService.findEndTask(claimVo.getRegistNo(),null,
					FlowNode.Chk);
			if(wfCheckTaskVos==null||wfCheckTaskVos.size()==0){
				throw new Exception("查勘未提交，不能发起预付任务！");
			}

			// 存在未核赔通过的预付不能发起预付
			List<PrpLCompensateVo> prePayCompVos = compensateService.queryCompListByClaimNo(claimVo.getClaimNo(),
					"Y");
			if(prePayCompVos!=null&&prePayCompVos.size()>0){
				for(PrpLCompensateVo compVo:prePayCompVos){
					if( !"1".equals(compVo.getUnderwriteFlag())&& !"7".equals(compVo.getUnderwriteFlag())){
						throw new Exception("该立案："+claimNo+"存在未核赔通过的预付任务，不能发起预付任务！");
					}
				}
			}
			// 存在非注销的理算任务不能发起预付
			FlowNode node = FlowNode.Compe;
			if("1101".equals(claimVo.getRiskCode())){
				node = FlowNode.CompeCI;
			}else{
				node = FlowNode.CompeBI;
			}
			boolean existCompensateTask = wfTaskHandleService.existTaskByNodeCodeYJ(claimVo.getRegistNo(),
					node,claimVo.getRegistNo(),"");
			String compeCancelFlag = compensateTaskService.isCompeCancelByClaimNo(claimNo);
			if(existCompensateTask && !"1".equals(compeCancelFlag)){
				throw new Exception("该立案："+claimNo+"存在理算任务,不能发起预付任务！");
			}
			System.out.println("================================可以发起预付");

			// 存在垫付任务不能发起预付
			boolean existPadPayTask = wfTaskHandleService.existTaskByNodeCode(claimVo.getRegistNo(),
					FlowNode.PadPay,claimVo.getClaimNo(),"");
			if(existPadPayTask){
				//根据报案号查询垫付信息，如果垫付信息存在且状态为注销，也可以发起预付
				PrpLPadPayMainVo padMainVo = padPayPubService.findPadPay(claimVo.getRegistNo());
				if(padMainVo!=null){
					throw new Exception("该立案："+claimNo+"存在垫付任务,不能发起预付任务！");
				}
			}
			
			// 存在已经发起的预付任务不能发起预付     牛强改
			boolean existPrePayTaskBI = wfTaskHandleService.existTaskByNodeCode(claimVo.getRegistNo(), 
					FlowNode.PrePayBI,claimVo.getClaimNo(), "0");
			if (existPrePayTaskBI) {
				throw new Exception("该立案：" + claimNo + "存在商业预付任务,不能发起商业预付任务！");
			}
			boolean existPrePayTaskCI = wfTaskHandleService.existTaskByNodeCode(claimVo.getRegistNo(), 
					FlowNode.PrePayCI,claimVo.getClaimNo(), "0");
			if (existPrePayTaskCI) {
				throw new Exception("该立案：" + claimNo + "存在交强预付任务,不能发起交强预付任务！");
			}

			submitVo.setComCode(policyViewService.getPolicyComCode(claimVo.getRegistNo()));
			submitVo.setTaskInUser(userVo.getUserCode());
			submitVo.setTaskInKey(claimNo);
			submitVo.setHandleIdKey(prePayNo);
			// 指定原出来人
			submitVo.setAssignCom(policyViewService.getPolicyComCode(claimVo.getRegistNo()));
			submitVo.setAssignUser(null);  //牛强改  不指定处理人

			// PrpLWfTaskVo wfTaskVo = wfTaskHandleService.addSimpleTask(taskVo,submitVo);
			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.addPrePayTask(claimVo,submitVo);
			wfTaskVoList.add(wfTaskVo);
		}
	}
	
	/**
	 * 预付提交
	 * @param compensateVo
	 * @param prePayPVos
	 * @param prePayFVos
	 * @param submitNextVo
	 * @return
	 * @throws Exception
	 */
	public String saveOrSubmit(PrpLCompensateVo compensateVo,List<PrpLPrePayVo> prePayPVos,List<PrpLPrePayVo> prePayFVos,SubmitNextVo submitNextVo,
			SysUserVo userVo) throws Exception{

			

		// 当前任务

		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(Double.parseDouble(submitNextVo.getFlowTaskId()));

		String auditStatus = "";// 提交动作
		if(submitNextVo.getAuditStatus()!=null&& !"".equals(submitNextVo.getAuditStatus())){// 提交
			auditStatus = submitNextVo.getAuditStatus();
		}else{// 暂存
			auditStatus = CodeConstants.AuditStatus.SAVE;
		}

		// 该立案号下的历史预付信息
		List<PrpLCompensateVo> compensateVos = compensateService.findCompListByClaimNo(compensateVo.getClaimNo(),"Y");
		
		//自动支付
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(compensateVo.getRegistNo());
		String selfClaimFlags = prpLRegistVo.getFlag();
		List<PrpLPaymentVo> paymentVo = new ArrayList<PrpLPaymentVo>();
		BigDecimal sumAmt =new BigDecimal(0);
		// List排空
		prePayPVos.removeAll(Collections.singleton(null));
		if(prePayPVos.size()>0){
			for(PrpLPrePayVo vo : prePayPVos){
				PrpLPaymentVo prpLPaymentVo = new PrpLPaymentVo();
				prpLPaymentVo.setSumRealPay(vo.getPayAmt());
				prpLPaymentVo.setPayeeId(vo.getPayeeId());
				paymentVo.add(prpLPaymentVo);
				sumAmt=sumAmt.add(vo.getPayAmt());
			}
			
		}
		PrpLCompensateExtVo compensateExtVo = new PrpLCompensateExtVo();
		//自动支付
		if(!"save".equals(auditStatus)){// 提交
			//2017-12-13 如立案号下存在预付冲销任务（不管是否核赔通过），预付送收付的自动支付标志为否。
			boolean isExistWriteOffFlag = compensateTaskService.isExistWriteOff(compensateVo.getClaimNo(),"Y");
			if(paymentVo!=null && paymentVo.size()>0&&!isExistWriteOffFlag){
				List<SendMsgParamVo> msgParamVoList = compensateTaskService.getsendMsgParamVo(prpLRegistVo, paymentVo);
				if(msgParamVoList!=null && msgParamVoList.size() > 0){
					compensateExtVo.setIsFastReparation("1");
				}else{
					compensateExtVo.setIsFastReparation("0");
				}
			}else{
				compensateExtVo.setIsFastReparation("0");
			}
		}
		if(sumAmt.compareTo(BigDecimal.ZERO)==1){//小于0
			compensateExtVo.setIsAutoPay("1");
		}else{
			compensateExtVo.setIsAutoPay("0");
		}		
		prePayPVos.addAll(prePayFVos);
		compensateVo.setHandler1Code(userVo.getUserCode());
		compensateVo.setAllLossFlag("0");
		compensateVo.setTimes(1);
		compensateVo.setCurrency("CNY");
		compensateVo.setDeductType("1");
		compensateVo.setLastModifyUser(userVo.getUserCode());
		compensateVo.setUnderwriteFlag("0");// 核赔状态初始为0

		compensateExtVo.setRegistNo(compensateVo.getRegistNo());
		compensateExtVo.setIsCompDeduct("0");// 是否理算扣减
		compensateExtVo.setWriteOffFlag(CodeConstants.WRITEOFFFLAG.NORMAL);
		
		//自动支付
/*		if(!"save".equals(auditStatus)){// 提交
			if(paymentVo!=null && paymentVo.size()>0){
				List<SendMsgParamVo> msgParamVoList = compensateTaskService.getsendMsgParamVo(prpLRegistVo, paymentVo);
				if(msgParamVoList!=null && msgParamVoList.size() > 0){
					compensateExtVo.setIsFastReparation("1");
				}else{
					compensateExtVo.setIsFastReparation("0");
				}
			}else{
				compensateExtVo.setIsFastReparation("0");
			}
		}
		
		
		if(compensateVo.getSumAmt().compareTo(BigDecimal.ZERO)==-1){//小于0
			compensateExtVo.setIsAutoPay("0");
		}else{
			compensateExtVo.setIsAutoPay("1");
		}*/
		
		compensateVo.setPrpLCompensateExt(compensateExtVo);

		Boolean isBKindCode = false;//本次预付是否存在三者险别
		Map<String,BigDecimal> kindMap = new HashMap<String,BigDecimal>();//险别总金额
		Map<String,BigDecimal> lossMap = new HashMap<String,BigDecimal>();//损失项核损总金额
		Map<String,BigDecimal> payMap = new HashMap<String,BigDecimal>();//损失项总金额
		List<PrpLCItemKindVo> ItemKindVos = policyViewService.findItemKinds(compensateVo.getRegistNo(),"Y");
		for(PrpLCItemKindVo itemKindVo:ItemKindVos){
			kindMap.put(itemKindVo.getKindCode(),BigDecimal.ZERO);
		}
		for(PrpLPrePayVo prePayVo:prePayPVos){
			prePayVo.setCompensateNo(compensateVo.getCompensateNo());
			prePayVo.setRiskCode(compensateVo.getRiskCode());
			prePayVo.setCurrency("CNY");

			if("P".equals(prePayVo.getFeeType())&&StringUtils.isNotBlank(prePayVo.getLossType())){// 预付赔款

				String[] lossTypeArr = prePayVo.getLossType().split("_");
				//赋值险别
				if("1101".equals(compensateVo.getRiskCode())){// 交强预付
					if("1".equals(lossTypeArr[1])){// 标的车
						throw new Exception("交强预付赔款的预付损失类型不能选择"+prePayVo.getLossName());
					}
					prePayVo.setKindCode("BZ");// 机动车交通事故责任强制险
				}else{
					if("car".equals(lossTypeArr[0])){// 车辆
						if("1".equals(lossTypeArr[1])){// 标的
							if( !kindMap.containsKey("A")&& !kindMap.containsKey("A1")){
								throw new Exception("该保单没有投保车损险，预付损失类型不能选择"+prePayVo.getLossName());
							}
							if(kindMap.containsKey("A")){
								prePayVo.setKindCode("A");// 车损险
							}else if(kindMap.containsKey("A1")){
								prePayVo.setKindCode("A1");//全面型
							}
						}else{// 三者或地面
							prePayVo.setKindCode("B");
							isBKindCode = true;
						}
					}else if("prop".equals(lossTypeArr[0])){// 财产
						if(compensateVo.getRiskCode().compareTo("1205")>0){
							if("1".equals(lossTypeArr[1])){// 标的
								if( !kindMap.containsKey("D2")){
									throw new Exception("该保单没有投保车上货物责任险，预付损失类型不能选择"+prePayVo.getLossName());
								}
								prePayVo.setKindCode("D2");
							}else{// 三者或地面
								prePayVo.setKindCode("B");
								isBKindCode = true;
							}
						}else{
							if("1".equals(lossTypeArr[1])){// 标的
								if( !kindMap.containsKey("NZ")|| !kindMap.containsKey("D2")){
									throw new Exception("该保单没有投保随车行李损失险，预付损失类型不能选择"+prePayVo.getLossName());
								}
								if(kindMap.containsKey("NZ")){
									prePayVo.setKindCode("NZ");
								}else if(kindMap.containsKey("D2")){
									prePayVo.setKindCode("D2");
								}
							}else{// 三者或地面
								prePayVo.setKindCode("B");
								isBKindCode = true;
							}
						}
					}else if("pers".equals(lossTypeArr[0])){// 人伤
						if("1".equals(lossTypeArr[1])){// 标的
							PrpLDlossPersTraceVo persTraceVo = persTraceDubboService.findPersTraceByPK(Long.valueOf(lossTypeArr[2]));
							if("1".equals(persTraceVo.getPrpLDlossPersInjured().getLossItemType())){// 三者车
								prePayVo.setKindCode("B");
							}else if("2".equals(persTraceVo.getPrpLDlossPersInjured().getLossItemType())){// 本车乘客
								if( !kindMap.containsKey("D12")){
									throw new Exception("该保单没有投保车上人员责任险（乘客），预付损失类型不能选择"+prePayVo.getLossName());
								}
								prePayVo.setKindCode("D12");
							}else if("3".equals(persTraceVo.getPrpLDlossPersInjured().getLossItemType())){// 本车司机
								if( !kindMap.containsKey("D11")){
									throw new Exception("该保单没有投保车上人员责任险（司机），预付损失类型不能选择"+prePayVo.getLossName());
								}
								prePayVo.setKindCode("D11");
							}
						}else{// 三者或地面
							prePayVo.setKindCode("B");
							isBKindCode = true;
						}
					}
					BigDecimal sumKindAmount = kindMap.get(prePayVo.getKindCode()).add(prePayVo.getPayAmt());
					kindMap.put(prePayVo.getKindCode(),sumKindAmount);
				}
				
				//该案件提交时提示预付金额高于核损金额，这个控制请修改为控制整个交强险的核损总金额和限额
				if("car".equals(lossTypeArr[0])){// 车辆
					if(!lossMap.containsKey(prePayVo.getLossName())){
						PrpLDlossCarMainVo lossCarMainVo = lossCarService.findLossCarMainById(Long.valueOf(lossTypeArr[2]));
						//车辆核损总金额=核损总费用+核损总施救费
						BigDecimal sumLossCarFee = lossCarMainVo.getSumVeriLossFee().add(lossCarMainVo.getSumVeriRescueFee());
						lossMap.put(prePayVo.getLossName(),sumLossCarFee);
					}
				}else if("prop".equals(lossTypeArr[0])){// 财产
					if(!lossMap.containsKey(prePayVo.getLossName())){
						PrpLdlossPropMainVo lossPropMainVo = propTaskService.findPropMainVoById(Long.valueOf(lossTypeArr[2]));
						//财产核损总金额=核损总费用+核损总施救费
						BigDecimal sumLossPropFee = lossPropMainVo.getSumVeriLoss().add(lossPropMainVo.getVerirescueFee());
						lossMap.put(prePayVo.getLossName(),sumLossPropFee);
					}
				}else if("pers".equals(lossTypeArr[0])){// 人伤
					if(!lossMap.containsKey(prePayVo.getLossName())){
						PrpLDlossPersTraceVo persTraceVo = persTraceDubboService.findPersTraceByPK(Long.valueOf(lossTypeArr[2]));
						BigDecimal sumLoss = BigDecimal.ZERO;
						if(persTraceVo.getSumVeriDefloss()==null){
							sumLoss = persTraceVo.getSumVeriReportFee();
						}else{
							sumLoss = persTraceVo.getSumVeriDefloss();
						}
						//人伤费用总金额
						//lossMap.put(prePayVo.getLossName(),sumLoss.doubleValue());  // 人伤不管控是否超过核损金额
					}
				}
				//各损失项总预付赔款
				if(payMap.containsKey(prePayVo.getLossName())){
					BigDecimal sumPayAmt = payMap.get(prePayVo.getLossName()).add(prePayVo.getPayAmt());
					payMap.put(prePayVo.getLossName(),sumPayAmt);
				}else{
					payMap.put(prePayVo.getLossName(),prePayVo.getPayAmt());
				}
			}
		}
		
		//该案件提交时提示预付金额高于核损金额
		if(!CodeConstants.oldClaimType.oldClaim.equals(selfClaimFlags)){//旧理赔不用管控分项
			for(Map.Entry<String, BigDecimal> entry : lossMap.entrySet()){
				if(payMap.get(entry.getKey()).compareTo(entry.getValue())==1){
					throw new Exception("该损失项"+entry.getKey()+"的预付总金额（"+payMap.get(entry.getKey())+"）大于核损总金额（"+entry.getValue()+"）");
				}
			}
		}
		
       //该案件提交时提示预付金额不能超过立案估计赔款
		PrpLClaimVo claimVo = claimService.findByClaimNo(compensateVo.getClaimNo());
		
		BigDecimal propPrePay = BigDecimal.ZERO;// 财产预付赔款
		BigDecimal deathPrePay = BigDecimal.ZERO;// 死亡伤残预付赔款
		BigDecimal medicalPrePay = BigDecimal.ZERO;// 医疗费用预付赔款
		BigDecimal oldPrePayAll = BigDecimal.ZERO;// 预付总赔款（旧理赔案子用）
		if(compensateVos!=null&&compensateVos.size()>0){
			for(PrpLCompensateVo compVo:compensateVos){
				List<PrpLPrePayVo> prePayVos = compensateService.getPrePayVo(compVo.getCompensateNo(),"P");
				if(prePayPVos!=null&&prePayPVos.size()>0&&prePayVos!=null&&prePayVos.size()>0){
					prePayPVos.addAll(prePayVos);
				}
			}
		}
		if(CodeConstants.oldClaimType.oldClaim.equals(selfClaimFlags)){//旧理赔不用管控分项
			if("1101".equals(compensateVo.getRiskCode())){// 交强预付
				if(prePayPVos!=null&&prePayPVos.size()>0){
					for(PrpLPrePayVo prePayVo:prePayPVos){
						if("02".equals(prePayVo.getChargeCode())){// 死亡伤残
							deathPrePay = deathPrePay.add(prePayVo.getPayAmt());
						}
						if("01".equals(prePayVo.getChargeCode())){// 财产
							propPrePay = propPrePay.add(prePayVo.getPayAmt());
						}
						if("03".equals(prePayVo.getChargeCode())){// 医疗费用
							medicalPrePay = medicalPrePay.add(prePayVo.getPayAmt());
						}
					}
					oldPrePayAll = propPrePay.add(medicalPrePay).add(deathPrePay);
					if(oldPrePayAll.compareTo(claimVo.getSumClaim())==1){
						throw new Exception("预付赔款总额不能超过立案估计赔款（"+claimVo.getSumClaim()+"）"); 
					}
				}
			}else{//商业
				if(compensateVos!=null&&compensateVos.size()>0){// 历次预付金额
					for(PrpLCompensateVo compVo:compensateVos){
						List<PrpLPrePayVo> prePayVos = compensateService.getPrePayVo(compVo.getCompensateNo(),"P");
						if(prePayVos!=null&&prePayVos.size()>0){
							for(PrpLPrePayVo prePayVo:prePayVos){
								BigDecimal sumKindAmount = kindMap.get(prePayVo.getKindCode()).add(prePayVo.getPayAmt());
								kindMap.put(prePayVo.getKindCode(),sumKindAmount);
							}
						}
					}
				}
				for(Map.Entry<String, BigDecimal> entry : kindMap.entrySet()){
					oldPrePayAll = oldPrePayAll.add(entry.getValue());
				}
				if(oldPrePayAll.compareTo(claimVo.getSumClaim())==1){
					throw new Exception("预付赔款总额不能超过立案估计赔款（"+claimVo.getSumClaim()+"）"); 
				}
			}
		}else {
			if(prePayPVos!=null&&prePayPVos.size()>0){
				for(PrpLPrePayVo prePayVo:prePayPVos){
					if("02".equals(prePayVo.getChargeCode())){// 死亡伤残
						deathPrePay = deathPrePay.add(prePayVo.getPayAmt());
					}else if("03".equals(prePayVo.getChargeCode())){// 医疗费用
						medicalPrePay = medicalPrePay.add(prePayVo.getPayAmt());
					}
				}
				
				PrpLCheckDutyVo dutuVo = checkTaskService.findCheckDuty(claimVo.getRegistNo(), 1);  //责任比例表
				//预付总金额和估计赔款比较
				List<PrpLClaimKindVo> claimKindVos = claimVo.getPrpLClaimKinds();
				for(PrpLClaimKindVo claimKindVo:claimKindVos){// 取立案估损金额
					if(CodeConstants.FeeTypeCode.PROPLOSS.equals(claimKindVo.getLossItemNo())){
						if(propPrePay.doubleValue()>claimKindVo.getClaimLoss().doubleValue()){
							//throw new Exception("财产损失的预付赔款总额不能超过立案估计赔案（"+claimKindVo.getClaimLoss()+"）");
						}
					}else if(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES.equals(claimKindVo.getLossItemNo())){
						if(medicalPrePay.compareTo(claimKindVo.getClaimLoss())==1&&(!"1101".equals(compensateVo.getRiskCode()))){
							throw new Exception("医疗费用的的预付赔款总额不能超过立案估计赔款（"+claimKindVo.getClaimLoss()+"）"); //交强放开医疗费用不超过立案估计赔款
						}
						/*if("1101".equals(compensateVo.getRiskCode())&&"1".equals(dutuVo.getCiDutyFlag())&&medicalPrePay.compareTo(new BigDecimal(10000))==1){
							throw new Exception("医疗费用的的预付赔款交强有责不超过10000，本次医疗预付费用为（"+medicalPrePay+"）");
						}
						if("1101".equals(compensateVo.getRiskCode())&&"0".equals(dutuVo.getCiDutyFlag())&&medicalPrePay.compareTo(new BigDecimal(1000))==1){
							throw new Exception("医疗费用的的预付赔款交强无责不超过1000，本次预付医疗费用为（"+medicalPrePay+"）");
						}*/
						//医疗费预付限额判断调整为读取配置表的限额
						if ("1101".equals(compensateVo.getRiskCode())){
							double unitAmountTemp = configService.calBzAmount(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES,"1".equals(dutuVo.getCiDutyFlag()),compensateVo.getRegistNo());
							if (medicalPrePay.compareTo(new BigDecimal(unitAmountTemp))==1){
								throw new Exception("医疗费用的的预付赔款交强有责不超过"+ unitAmountTemp +"，本次医疗预付费用为（"+medicalPrePay+"）");
							}
						}
					}else if(CodeConstants.FeeTypeCode.PERSONLOSS.equals(claimKindVo.getLossItemNo())){
						if(deathPrePay.compareTo(claimKindVo.getClaimLoss())==1){
							throw new Exception("死亡伤残的预付赔款总额不能超过立案估计赔款（"+claimKindVo.getClaimLoss()+"）");
						}
					}
				}
			}
			
			if("1101".equals(compensateVo.getRiskCode())){// 交强预付
				
				if(prePayPVos!=null&&prePayPVos.size()>0){
					for(PrpLPrePayVo prePayVo:prePayPVos){
						if("01".equals(prePayVo.getChargeCode())){// 财产
							propPrePay = propPrePay.add(prePayVo.getPayAmt());
						}
					}
					//预付总金额和估计赔款比较
					List<PrpLClaimKindVo> claimKindVos = claimVo.getPrpLClaimKinds();
					for(PrpLClaimKindVo claimKindVo:claimKindVos){// 取立案估损金额
						if(CodeConstants.FeeTypeCode.PROPLOSS.equals(claimKindVo.getLossItemNo())){
							if(propPrePay.doubleValue()>claimKindVo.getClaimLoss().doubleValue()){
								throw new Exception("财产损失的预付赔款总额不能超过立案估计赔款（"+claimKindVo.getClaimLoss()+"）");
							}
						}
					}
				}
				
			}else{// 商业预付
				if(compensateVos!=null&&compensateVos.size()>0){// 历次预付金额
					for(PrpLCompensateVo compVo:compensateVos){
						List<PrpLPrePayVo> prePayVos = compensateService.getPrePayVo(compVo.getCompensateNo(),"P");
						if(prePayVos!=null&&prePayVos.size()>0){
							for(PrpLPrePayVo prePayVo:prePayVos){
								BigDecimal sumKindAmount = kindMap.get(prePayVo.getKindCode()).add(prePayVo.getPayAmt());
								kindMap.put(prePayVo.getKindCode(),sumKindAmount);
							}
						}
					}
				}
				List<PrpLClaimKindVo> claimKindVos = claimService.findClaimKindVoListByRegistNo(compensateVo
						.getRegistNo());
				BigDecimal sumBClaimLoss = BigDecimal.ZERO;
				for(PrpLClaimKindVo claimKindVo:claimKindVos){// 取立案估损金额
					if(!"B".equals(claimKindVo.getKindCode())&&kindMap.containsKey(claimKindVo.getKindCode())&&kindMap.get(claimKindVo.getKindCode()).compareTo(BigDecimal.ZERO)==1){// 保了该险别且该险别预付赔款大于0
						if(kindMap.get(claimKindVo.getKindCode()).compareTo(claimKindVo.getClaimLoss())==1){// 该预付损失类型对应的预付金额不能超过该险别的立案的估计赔款。
							throw new Exception(
									claimKindVo.getKindName()+"的预付总金额不能超过该险别的立案估计赔款（"+claimKindVo.getClaimLoss()+"）");
						}
					}else if("B".equals(claimKindVo.getKindCode())){//三者险别拆开了计算总计的
						sumBClaimLoss = sumBClaimLoss.add(claimKindVo.getClaimLoss());
					}
				}
				if(isBKindCode&&kindMap.get("B").compareTo(sumBClaimLoss)== 1){//本次预付包含三者险的预付
					throw new Exception("三者险的预付总金额（"+kindMap.get("B")+"）不能超过该险别的立案估计赔款（"+sumBClaimLoss+"）");
				}
			}
	    }
		
		compensateService.saveOrUpdateCompensateVo(compensateVo,userVo);
		compensateService.saveOrUpdatePrePay(prePayPVos,compensateVo.getCompensateNo());

		if("save".equals(auditStatus)){// 暂存
			wfTaskHandleService.tempSaveTask(wfTaskVo.getTaskId().doubleValue(),compensateVo.getCompensateNo(),
					userVo.getUserCode(),userVo.getComCode());
		}else{// 提交
			
			//预付提交之前提醒反洗钱人的姓名
			//feeType--预付子表的费用类型
			String registNo=compensateVo.getRegistNo();
			String policyNo=compensateVo.getPolicyNo();
			/*String str=vaxInfor(policyNo,registNo,userVo);
			if(StringUtils.isNotBlank(str)){
				throw new Exception(str+"已被冻结,案件不能提交!");
			}*/
			/*String feeType1="P";//费用赔款类型
			String feeType2="F";
			String payList1="";//payeeId组成字符串
			String payList2="";
			for(PrpLPrePayVo prpLPrePay:prePayPVos){
				payList1+=prpLPrePay.getPayeeId()+",";
			}
			
			if(payList1 !=null && !"".equals(payList1) ){
			  List<String> nameList1=nameback(payList1,registNo,feeType1);
			  String str1="";
		       if(nameList1 !=null && nameList1.size()>0){
				  for(int i=0;i<nameList1.size();i++){
					str1+=nameList1.get(i)+",";
					
					}
				  str1=str1.substring(0,str1.length()-1);//去掉最后一个逗号
		        throw new Exception("请补录收款人[ "+str1+" ]的反洗钱信息");
				}
		  }
			
			for(PrpLPrePayVo prpLPrePay:prePayFVos){
				payList2+=prpLPrePay.getPayeeId()+",";
			}
			if(payList2!=null && !"".equals(payList2)){
		       List<String> nameList2=nameback(payList2,registNo,feeType2);
			     String str2="";
		          if(nameList2 !=null && nameList2.size()>0){
				    for(int i=0;i<nameList2.size();i++){
				      	str2+=nameList2.get(i)+",";
				     	}
				    str2=str2.substring(0,str2.length()-1);//去掉最后一个逗号
		           throw new Exception("请补录收款人[ "+str2+" ]的反洗钱信息");
				}
			}*/
			
			WfTaskSubmitVo submitVo = new WfTaskSubmitVo();

			submitVo.setFlowTaskId(wfTaskVo.getTaskId());
			submitVo.setTaskInKey(compensateVo.getCompensateNo());
			submitVo.setTaskInUser(userVo.getUserCode());
			submitVo.setFlowId(wfTaskVo.getFlowId());
			submitVo.setFlowTaskId(wfTaskVo.getTaskId());
			// 预付提交指定到处理人
			PrpLWfTaskVo oldTaskVo = findTaskIn(registNo, compensateVo.getCompensateNo(),FlowNode.valueOf(submitNextVo.getNextNode()));
			if(oldTaskVo != null && wfTaskVo.getSubNodeCode().equals(oldTaskVo.getYwTaskType())){
				submitVo.setAssignCom(oldTaskVo.getHandlerCom());
				submitVo.setAssignUser(oldTaskVo.getHandlerUser());
			}else{
				submitVo.setAssignUser(submitNextVo.getAssignUser());
				submitVo.setHandleruser(submitNextVo.getAssignUser());
			}
			//submitVo.setAssignCom(wfTaskVo.getAssignCom());
			
			submitVo.setCurrentNode(FlowNode.valueOf(wfTaskVo.getSubNodeCode()));
			submitVo.setComCode(policyViewService.getPolicyComCode(registNo));
			submitVo.setNextNode(FlowNode.valueOf(submitNextVo.getNextNode()));
			
			wfTaskHandleService.submitPrepay(compensateVo,submitVo);
		}
		
		return auditStatus;
	}
	@Override
	public PrpLWfTaskVo findTaskIn(String registNo,String handleIdKey,FlowNode nextNode) throws Exception{
		PrpLWfTaskVo oldTaskVo = null;
		List<PrpLWfTaskVo> endTaskVoList = wfTaskHandleService.findEndTask(registNo,handleIdKey,nextNode);
		if(endTaskVoList == null || endTaskVoList.size() == 0){
			endTaskVoList = wfTaskHandleService.findEndTask(registNo,handleIdKey,FlowNode.VClaim_CI_LV1);
		}
		if(endTaskVoList != null && endTaskVoList.size() > 0){
			oldTaskVo = endTaskVoList.get(0);
		}
		return oldTaskVo;
	}

	/**
	 * 预付冲销
	 */
	@Override
	public void submitPrePayWriteOff(PrpLCompensateVo compVo,SubmitNextVo submitNextVo,SysUserVo userVo,List<PrpLPrePayVo> prePayPVos,
												List<PrpLPrePayVo> prePayFVos) throws Exception{
		boolean existCompensateTask = wfTaskHandleService.existTaskByNodeCodeYJ(compVo.getRegistNo(),FlowNode.Compe,
				compVo.getRegistNo(),"");
		if(existCompensateTask){
			throw new Exception("该立案："+compVo.getClaimNo()+"存在理算任务,不能发起预付冲销任务！");
		}
		PrpLCompensateVo oldCompensateVo = compensateService.findCompByPK(compVo.getCompensateNo());
		//判断是否存在预付冲销任务
		List<PrpLCompensateVo>  compensateVos = compensateService.findValidCompensate(compVo.getRegistNo(), "Y",compVo.getCompensateNo());
		if(compensateVos != null && compensateVos.size()>0){
			BigDecimal sumAmt = BigDecimal.ZERO;
			compensateVos.removeAll(Collections.singleton(null));
			boolean isUnderwrite = false;
			for(PrpLCompensateVo compensateVo: compensateVos){
				if(UnderWriteFlag.BACK_UNDERWRITE.equals(compensateVo.getUnderwriteFlag())
						|| UnderWriteFlag.NORMAL.equals(compensateVo.getUnderwriteFlag())
						|| UnderWriteFlag.WAIT_UNDERWRITE.equals(compensateVo.getUnderwriteFlag())
						){
					isUnderwrite = true;
					break;
				} else{
					sumAmt = compensateVo.getSumAmt()== null ? sumAmt : sumAmt.add(compensateVo.getSumAmt());
				}
			}
			//判断预付冲销的总金额是否等于预付的金额
			if(isUnderwrite ||sumAmt != null && compVo.getSumAmt() != null && oldCompensateVo.getSumAmt().compareTo(sumAmt) == 0){
				throw new Exception("该立案："+compVo.getClaimNo()+"存在预付冲销任务,不能发起预付冲销任务！");
			}
		}
		BigDecimal sumAmt = BigDecimal.ZERO;
		String newCompNo = billNoService.getPrePayNo(policyViewService.getPolicyComCode(compVo.getRegistNo()),compVo.getRiskCode());// 预付冲销预付号
		List<PrpLPrePayVo> newPrePayVos = new ArrayList<PrpLPrePayVo>();
		prePayPVos.removeAll(Collections.singleton(null));
		prePayFVos.removeAll(Collections.singleton(null));
		if(prePayPVos != null && prePayPVos.size()>0){
			for(PrpLPrePayVo prePayVo:prePayPVos){
				if(prePayVo.getPayAmt().compareTo(BigDecimal.ZERO) != 0){
					BigDecimal newPayAmt = prePayVo.getPayAmt().multiply(new BigDecimal( -1));
					sumAmt = sumAmt.add(newPayAmt);
					prePayVo.setId(null);
					prePayVo.setPayAmt(newPayAmt);
					prePayVo.setCompensateNo(newCompNo);
					newPrePayVos.add(prePayVo);
				}
			}
		}
		if(prePayFVos != null && prePayFVos.size()>0){
			for(PrpLPrePayVo prePayVo:prePayFVos){
				if(prePayVo.getPayAmt().compareTo(BigDecimal.ZERO) != 0){
					BigDecimal newPayAmt = prePayVo.getPayAmt().multiply(new BigDecimal( -1));
					prePayVo.setPayAmt(newPayAmt);
					prePayVo.setId(null);
					sumAmt = sumAmt.add(newPayAmt);
					prePayVo.setCompensateNo(newCompNo);
					newPrePayVos.add(prePayVo);
				}
			}
		}
		compVo.setSumAmt(sumAmt.multiply(new BigDecimal( -1)));
		// PrpLCompensateExtVo comExtVo = compensateService.findCompExtByCompNo(compVo.getCompensateNo());
		PrpLCompensateVo oldCompVo = compensateService.findCompByPK(compVo.getCompensateNo());
		List<PrpLPrePayVo> prePayVos = compensateService.getPrePayVo(compVo.getCompensateNo(),null);


		// comExtVo.setOppoCompensateNo(newCompNo);// 对冲计算书号
		// compensateService.saveOrUpdateCompensateExtVo(comExtVo,compVo);

		// 保存一份预付冲销数据
		PrpLCompensateVo newCompVo = new PrpLCompensateVo();
		newCompVo = Beans.copyDepth().from(oldCompVo).to(PrpLCompensateVo.class);
		newCompVo.setCompensateNo(newCompNo);
		newCompVo.setSumAmt(compVo.getSumAmt().multiply(new BigDecimal( -1)));// 本次预付冲销金额变成负数
		newCompVo.setUnderwriteDate(null);
		newCompVo.setUnderwriteUser(null);
		newCompVo.setHandler1Code(userVo.getUserCode());
		newCompVo.setAllLossFlag("0");
		newCompVo.setTimes(1);
		newCompVo.setCurrency("CNY");
		newCompVo.setDeductType("1");
		newCompVo.setLastModifyUser(userVo.getUserCode());
		newCompVo.setUnderwriteFlag("0");// 核赔状态初始为0
		PrpLCompensateExtVo extVo = newCompVo.getPrpLCompensateExt();
		if(extVo!=null){
			extVo.setWriteOffFlag(CodeConstants.WRITEOFFFLAG.ALLOFF);
			extVo.setOppoCompensateNo(compVo.getCompensateNo());//
			//不送资金系统
			extVo.setIsAutoPay("0");
			extVo.setIsFastReparation("0");
		}

		compensateService.saveOrUpdateCompensateVo(newCompVo,userVo);

	
		if(newPrePayVos != null && newPrePayVos.size() >0){
			List<PrpLPrePayVo> oldPrePayVos = Beans.copyDepth().from(prePayVos).toList(PrpLPrePayVo.class);
		    compensateService.saveOrUpdatePrePay(oldPrePayVos, compVo.getCompensateNo()); //paytime paystatus 置为空
			compensateService.saveOrUpdatePrePay(newPrePayVos,newCompNo);
		}
		// 保存预付对冲计算书号
		//	oldCompVo.getPrpLCompensateExt().setOppoCompensateNo(newCompVo.getCompensateNo());
			oldCompVo.getPrpLCompensateExt().setPayBackState("1");
			compensateService.saveOrUpdateCompensateVo(oldCompVo,userVo);

			// 发起一个预付冲销任务
			WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
			WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
			submitVo.setCurrentNode(FlowNode.VClaim);
			taskVo.setRegistNo(newCompVo.getRegistNo());
			taskVo.setHandlerIdKey(newCompNo);// 生成预付号
			if("1101".equals(newCompVo.getRiskCode())){
				submitVo.setNextNode(FlowNode.PrePayWfCI);
			}else{
				submitVo.setNextNode(FlowNode.PrePayWfBI);
			}

			// 查询立案已处理
			List<PrpLWfTaskVo> wfTaskVos = wfTaskHandleService.findEndTask(newCompVo.getRegistNo(),
					compVo.getCompensateNo(),submitVo.getCurrentNode());
			if(wfTaskVos!=null&&wfTaskVos.size()>0){
				taskVo.setRegistNo(wfTaskVos.get(0).getRegistNo());
				taskVo.setHandlerIdKey(newCompNo);//
				taskVo.setItemName(wfTaskVos.get(0).getItemName());
				taskVo.setBussTag(wfTaskVos.get(0).getBussTag());
				taskVo.setShowInfoXml(wfTaskVos.get(0).getShowInfoXML());

				submitVo.setFlowId(wfTaskVos.get(0).getFlowId());
				submitVo.setFlowTaskId(wfTaskVos.get(0).getTaskId());
			}

			submitVo.setComCode(policyViewService.getPolicyComCode(wfTaskVos.get(0).getRegistNo()));
			submitVo.setTaskInUser(userVo.getUserCode());
			submitVo.setTaskInKey(compVo.getCompensateNo());
			// 指定原出来人
			submitVo.setAssignCom(userVo.getComCode());
			submitVo.setAssignUser(userVo.getUserCode());

			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.addSimpleTask(taskVo,submitVo);

			// 提交预付冲销到核赔
			WfTaskSubmitVo submiVClaimVo = new WfTaskSubmitVo();

			submiVClaimVo.setFlowTaskId(wfTaskVo.getTaskId());
			submiVClaimVo.setTaskInKey(wfTaskVo.getTaskInKey());
			submiVClaimVo.setTaskInUser(userVo.getUserCode());
			submiVClaimVo.setFlowId(wfTaskVo.getFlowId());
			// submiVClaimVo.setCurrentNode(FlowNode.VClaim);
			submiVClaimVo.setComCode(policyViewService.getPolicyComCode(newCompVo.getRegistNo()));
			if("1101".equals(newCompVo.getRiskCode())){
				submiVClaimVo.setCurrentNode(FlowNode.PrePayWfCI);
			}else{
				submiVClaimVo.setCurrentNode(FlowNode.PrePayWfBI);
			}
			submiVClaimVo.setNextNode(FlowNode.valueOf(submitNextVo.getNextNode()));
			
			SysUserVo assignUserVo = assignService.execute(submiVClaimVo.getNextNode(),userVo.getComCode(),userVo, "");

			if(assignUserVo == null){  
			throw new IllegalArgumentException(FlowNode.valueOf(submitNextVo.getNextNode()).getName()+"未配置人员");
			}
			submiVClaimVo.setAssignUser(assignUserVo.getUserCode());
			submiVClaimVo.setAssignCom(assignUserVo.getComCode());

			wfTaskHandleService.submitPrepay(newCompVo,submiVClaimVo);
	/*	if(prePayVos!=null&&prePayVos.size()>0){
			List<PrpLPrePayVo> oldPrePayVos = Beans.copyDepth().from(prePayVos).toList(PrpLPrePayVo.class);
			for(PrpLPrePayVo prePayVo:oldPrePayVos){
				prePayVo.setPayTime(null);
				prePayVo.setPayStatus(null);
				PrpLPrePayVo newPrePayVo = new PrpLPrePayVo();

				Beans.copy().from(prePayVo).to(newPrePayVo);
				newPrePayVo.setId(null);
				newPrePayVo.setCompensateNo(newCompNo);
				newPrePayVo.setPayAmt(prePayVo.getPayAmt().multiply(new BigDecimal( -1)));// 把费用变成负数
				newPrePayVos.add(newPrePayVo);
			}
	    compensateService.saveOrUpdatePrePay(oldPrePayVos, compVo.getCompensateNo()); //paytime paystatus 置为空
		compensateService.saveOrUpdatePrePay(newPrePayVos,newCompNo);
		}*/


	}
   /**
    * 预付提交验证
    */
	@Override
	public List<String> nameback(String payList, String registNo, String feeType)
			throws Exception {
		double sum=0.00;
		List<Long> payIdlist = new ArrayList<Long>();
		String [] payArray =payList.split(",");
		List<String> keylist = new ArrayList<String>();
		List<PrpLCompensateVo> compVos2=new ArrayList<PrpLCompensateVo>();
	    List<PrpLCompensateVo> compVos1 = compensateTaskService.findCompensatevosByRegistNo(registNo);
		 //去掉已注销的
	    if(payArray!=null && payArray.length>0){
	    	for(int i=0;i<payArray.length;i++){
	    		payIdlist.add(Long.valueOf(payArray[i]));
	    	}
	    }
	    if(compVos1!=null && compVos1.size()>0 ){
	    	 for(PrpLCompensateVo prpLCompensateVo :compVos1 ){
				if(!(prpLCompensateVo.getUnderwriteFlag().equals("7"))){
					compVos2.add(prpLCompensateVo);
					}
			 }
		 }
       if(payIdlist!=null && payIdlist.size()>0){
    	 //相同收款人去重
			Set<Long> payIdSet= new HashSet<Long>(payIdlist);
	        for(Long payId : payIdSet){
	           PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(payId);
	            //if(!(payCustomVo.getFlag().equals("1")) && !"7".equals(payCustomVo.getPayObjectKind())){
                if (!(payCustomVo.getFlag().equals("1")) && (!"7".equals(payCustomVo.getPayObjectKind())
                        && !"4".equals(payCustomVo.getPayObjectKind()) && !"8".equals(payCustomVo.getPayObjectKind()))) { //4公估机构,7医院（法院）律师所
	            	if(compVos2!=null && compVos2.size()>0){
	            	   for(PrpLCompensateVo prpLCompensateVo: compVos2){
	            		      String compasate =prpLCompensateVo.getCompensateNo();
	            		       List<PrpLPrePayVo> PrpLPrePays = compensateTaskService.queryPrePay(compasate);
	    		          for(PrpLPrePayVo prpLPrePayVo:PrpLPrePays){
	    		    	     if(prpLPrePayVo.getFeeType().equals(feeType)){
	    			             if(prpLPrePayVo.getPayeeId().longValue()==payId.longValue()){
	    			    	
	    			    	        sum=sum+(prpLPrePayVo.getPayAmt().doubleValue());
	    			    	
	    			              }
	    			    
	    		    	      }
	    			   
	    		          }
	    	          }
	            	
	          }  
	            	
	    	//金额大于等于10000时
			if(sum>= 10000){
				
				 keylist.add(payCustomVo.getPayeeName());
			
	      }
			
			sum=0.00;
	      }
	     
	   }
	    	
     }
     
		return keylist;
	} 
	
	private String vaxInfor(String policyNo,String registNo,SysUserVo userVo){
		AMLVo amlVo = new AMLVo();
        List<PrpLCMainVo> cmainVos= prpLCMainService.findPrpLCMainsByRegistNo(registNo);
        if(cmainVos!=null && cmainVos.size()>0){
        	for(PrpLCMainVo vo:cmainVos){
        		if(StringUtils.isNotBlank(policyNo) && policyNo.equals(vo.getPolicyNo())){
        			 List<PrpLCInsuredVo> PrpLCInsuredList = vo.getPrpCInsureds();
        		            for(PrpLCInsuredVo cInsured:PrpLCInsuredList){
        		                if("1".equals(cInsured.getInsuredFlag())){
        		                	
        		                    amlVo.setInsuredName(cInsured.getInsuredName());
        		                    amlVo.setIdentifyNumber(cInsured.getIdentifyNumber());
        		                    
        		                }
        		               
        		            }
        		        }
        	}
        }

        
        String amlUrl = SpringProperties.getProperty("dhic.aml.saveurl");
        
       
           String nameList="";
       	   FxqCrmRiskService crmRiskService = new FxqCrmRiskService(amlUrl);
       	if(StringUtils.isNotBlank(amlVo.getIdentifyNumber())){
				
				try {
					CrmRiskInfoVo crmRiskInfoVo = crmRiskService.getCustRiskInfo(amlVo.getIdentifyNumber(),amlVo.getInsuredName(), amlVo.getIdentifyNumber(),"",userVo.getUserCode());
					if(crmRiskInfoVo!=null && "1".equals(crmRiskInfoVo.getIsFreeze())){
						nameList="被保险人[ "+crmRiskInfoVo.getCustName()+" ],";
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		     }
		
		
		return nameList;
	}

	@Override
	public boolean saveBeforeCheck(List<PrpLPrePayVo> prpPrePayVos) {
		for(PrpLPrePayVo prpPrePayVo :prpPrePayVos){
			if(!"2".equals(prpPrePayVo.getPayObjectKind())){
				if(StringUtils.isBlank(prpPrePayVo.getOtherCause())){
					return true;
				}
			}
		}
		return false;
	}
	
	/* 
	 * @see ins.sino.claimcar.claim.service.CompensateTaskService#updatePrePayWf(ins.sino.claimcar.claim.vo.PrpLCompensateVo, java.util.List, java.util.List)
	 * @param oldCompVo
	 * @param prePayFVos
	 * @param prePayPVos
	 * @return
	 */
	@Override
	public PrpLCompensateVo updatePrePayWf(PrpLCompensateVo oldCompVo,List<PrpLPrePayVo> prePayFVos,List<PrpLPrePayVo> prePayPVos,SysUserVo userVo){
		prePayPVos.removeAll(Collections.singleton(null));
		prePayFVos.removeAll(Collections.singleton(null));
		List<PrpLPrePayVo> newPrePayVos = new ArrayList<PrpLPrePayVo>();
		BigDecimal sumAmt = BigDecimal.ZERO;
		if(prePayPVos != null && prePayPVos.size()>0){
			for(PrpLPrePayVo prePayVo:prePayPVos){
				if(prePayVo.getPayAmt().compareTo(BigDecimal.ZERO) != 0){
					BigDecimal newPayAmt = prePayVo.getPayAmt().multiply(new BigDecimal( -1));
					sumAmt = sumAmt.add(newPayAmt);
					prePayVo.setPayAmt(newPayAmt);
					newPrePayVos.add(prePayVo);
				}
			}
		}
		if(prePayFVos != null && prePayFVos.size()>0){
			for(PrpLPrePayVo prePayVo:prePayFVos){
				if(prePayVo.getPayAmt().compareTo(BigDecimal.ZERO) != 0){
					BigDecimal newPayAmt = prePayVo.getPayAmt().multiply(new BigDecimal( -1));
					prePayVo.setPayAmt(newPayAmt);
					sumAmt = sumAmt.add(newPayAmt);;
					newPrePayVos.add(prePayVo);
				}
			}
		}
		if(newPrePayVos != null && !newPrePayVos.isEmpty()){
			compensateService.saveOrUpdatePrePay(newPrePayVos,oldCompVo.getCompensateNo());
		}
		oldCompVo.setSumAmt(sumAmt);
		compensateService.saveOrUpdateCompensateVo(oldCompVo,userVo);
		return oldCompVo;
	}

}
