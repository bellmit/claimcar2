package ins.sino.claimcar.claim.services;
import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BillNoService;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.po.PrpLClaim;
import ins.sino.claimcar.claim.po.PrpLCompensate;
import ins.sino.claimcar.claim.po.PrpLCompensateExt;
import ins.sino.claimcar.claim.service.*;
import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLClaimKindVo;
import ins.sino.claimcar.claim.vo.PrpLClaimResultVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCoinsVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLKindAmtSummaryVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonFeeDetailVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonFeeVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.SubmitNextVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.po.PrpLWfTaskQuery;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.pay.service.PadPayService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.subrogation.service.PlatLockService;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;
import ins.sino.claimcar.subrogation.vo.PrpLRecoveryOrPayVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("compensateLaunchService")
public class CompensateLaunchServiceImpl implements CompensateLaunchService {
	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private CompensateService compensateService;
	@Autowired
	private BillNoService billNoService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private ClaimService claimService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private PadPayService padPayService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	private LossCarService lossCarService;
	@Autowired
	private PropTaskService propTaskService;
	@Autowired
	private PersTraceDubboService persTraceDubboService;
	@Autowired
	private ManagerService managerService;
	@Autowired
	private CompensateHandleService compHandleService;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	private WfFlowQueryService wfFlowQueryService;
	@Autowired
	private PlatLockService platLockService;
	@Autowired
	private CompensateTaskService compensateTaskService;
	@Autowired
	private ConfigService configService;

	/* 
	 * @see ins.sino.claimcar.claim.services.CompensateLaunchService#validCheckComp(java.lang.String, java.lang.String)
	 * @param registNo
	 * @param chkFlag
	 * @return
	 */
	@Override
	public String validCheckComp(String registNo, String chkFlag) {// 先验证registNo不为空再调用
		String reMsg = "OK";
		String checkFlag = chkFlag;// 验证标志位 BI-发起商业理算 CI-发起交强理算 BC-同时发起理算
		List<PrpLCompensateVo> compList = new ArrayList<PrpLCompensateVo>();// 报案号下计算书
		List<PrpLCMainVo> cMainList = policyViewService.getPolicyAllInfo(registNo);// 报案号下保单信息表
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);// 报案信息表
		if (!StringUtils.isBlank(registNo)) {
			compList = compensateService.findCompByRegistNo(registNo);
		}
		// 查询报案号下预付表核赔状态 预算主表同为PrpLCompensateVo-CompensateType为Y
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("registNo", registNo);
		qr.addEqual("compensateType", "Y");
		qr.addEqual("underwriteFlag", "0");
		String prePayCIFlag = "N";
		String prePayBIFlag = "N";
		List<PrpLCompensate> prePayList = databaseDao.findAll(PrpLCompensate.class, qr);
		for (PrpLCompensate prePay : prePayList) {
			if (prePay.getRiskCode().substring(0, 2).equals(PREFIX_CI)) {
					prePayCIFlag = "Y";
			}
			if (prePay.getRiskCode().substring(0, 2).equals(PREFIX_BI)) {
					prePayBIFlag = "Y";
			}
		}
		if (checkFlag.equals("CI") || checkFlag.equals("BC")) {
			if (prePayCIFlag.equals("Y")) {
				return "存在未核赔通过的交强预付任务，不能发起交强计算书。";
			}

		}
		if (checkFlag.equals("BI") || checkFlag.equals("BC")) {
			if (prePayBIFlag.equals("Y")) {
				return "存在未核赔通过的商业预付任务，不能发起商业计算书。";
			}
		}
		
		// 计算书未核赔通过
		List<PrpLCompensateVo> compCIList = new ArrayList<PrpLCompensateVo>();
		List<PrpLCompensateVo> compBIList = new ArrayList<PrpLCompensateVo>();
		String CIUnderwriteFlag = "N";
		String BIUnderwriteFlag = "N";
		String compNo = "";
		BigDecimal deathDisSum = BigDecimal.ZERO;
		BigDecimal medicalSum = BigDecimal.ZERO;
		BigDecimal propSum = BigDecimal.ZERO;

		String payBIFlag = "N";
		for (PrpLCompensateVo compVo : compList) {
			if (compVo.getRiskCode().substring(0, 2).equals(PREFIX_CI)) {// 交强险计算书
				compCIList.add(compVo);
				if (compVo.getUnderwriteFlag().equals(CodeConstants.UnderWriteFlag.NORMAL)) {
					CIUnderwriteFlag = "Y";
				} else {
					if (compVo.getUnderwriteFlag().equals(CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE)) {
						compNo = compVo.getCompensateNo();
						// 如果核赔通过，查询交强险该计算书下的医疗、死亡伤残、财产三个限额的已赔付金额并累加
						// 只需要查询该报案号下面历史赔付信息 立案的保额字段是实时更新的
						List<PrpLLossPersonVo> personList = compVo.getPrpLLossPersons();
						if(personList!=null && !personList.isEmpty()){
							for(PrpLLossPersonVo personVo : personList){
								List<PrpLLossPersonFeeVo> personFeeList = personVo.getPrpLLossPersonFees();
								for (PrpLLossPersonFeeVo personFee : personFeeList) {
									if (personFee.getLossItemNo().equals(CodeConstants.LossTypePersComp.PERSON_LOSS_DEATHDIS)) {
										deathDisSum.add(personFee.getFeeRealPay());
									}
									if (personFee.getLossItemNo().equals(CodeConstants.LossTypePersComp.PERSON_LOSS_MEDICAL)) {
										medicalSum.add(personFee.getFeeRealPay());
									}
								}
							}
							
						}
						
						List<PrpLLossPropVo> propList = compVo.getPrpLLossProps();
						if(propList!=null && !propList.isEmpty()){
							for(PrpLLossPropVo propVo : propList){
								propSum.add(propVo.getSumRealPay());
							}
						}	
							
						List<PrpLLossItemVo> itemList = compVo.getPrpLLossItems();
						for (PrpLLossItemVo itemVo : itemList) {
							propSum.add(itemVo.getSumRealPay());
						}
					}
				}
			}
			if (compVo.getRiskCode().substring(0, 2).equals(PREFIX_BI)) {// 商业险计算书
				compBIList.add(compVo);
				if (compVo.getUnderwriteFlag().equals(CodeConstants.UnderWriteFlag.NORMAL)) {
					BIUnderwriteFlag = "Y";
				} else {
					if (compVo.getUnderwriteFlag().equals(CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE)) {
						compNo = compVo.getCompensateNo();
						// 核赔通过，查询商业计算书险别对应的赔付限额，查询立案险别金额表PrpLCItemKind
						List<PrpLClaimKindVo> claimKinds = claimService.findClaimKindVoListByRegistNo(registNo);
						Map<String, BigDecimal> kindAmtMap = compensateService.getCompKindAmtMap(compVo.getPrpLLossItems(), 
								compVo.getPrpLLossProps(), compVo.getPrpLLossPersons(), compVo.getPrpLCharges());
						for (PrpLClaimKindVo claimKind : claimKinds) {
							//判断险别赔款是否大于该险别保额
							if(kindAmtMap.containsKey(claimKind.getKindCode())){
								if (kindAmtMap.get(claimKind.getKindCode()).compareTo(claimKind.getAmount()) == 1) {
									payBIFlag = "Y";
									break;
								}
							}
						}
					}
				}
			}
		}

		if (checkFlag.equals("CI") || checkFlag.equals("BC")) {
			if (CIUnderwriteFlag.equals("Y")) {
				return "存在未核赔通过的交强计算书，不能发起交强计算书。";
			}
		}
		if (checkFlag.equals("BI") || checkFlag.equals("BC")) {
			if (BIUnderwriteFlag.equals("Y")) {
				return "存在未核赔通过的商业计算书，不能发起商业计算书。";
			}
		}
		// 互碰自赔案件
		if (checkFlag.equals("BI") || checkFlag.equals("BC")) {
			List<PrpLClaimVo> claimVos = claimService.findClaimListByRegistNo(registNo);
			for(PrpLClaimVo claimVo:claimVos){
				if(claimVo.getCaseFlag()!=null){
					if (claimVo.getRiskCode().startsWith(PREFIX_BI)
							&&claimVo.getCaseFlag().equals("1")
					) {
						return "互碰自赔案件不能发起商业计算书。";
					}
				}
			}
		}

		BigDecimal ddLimit = BigDecimal.ZERO;//死亡伤残限额
		BigDecimal meLimit = BigDecimal.ZERO;//医疗费用限额
		BigDecimal prLimit = BigDecimal.ZERO;//车财损失限额

		// 判断有责无责来设定不同交强限额
		Boolean ciIndemDuty = false;
		List<PrpLCheckDutyVo> checkDutyList = checkTaskService.findCheckDutyByRegistNo(registNo);
		for (PrpLCheckDutyVo checkDutyVo : checkDutyList) {
			if (checkDutyVo.getSerialNo().equals("1")) {
				if (checkDutyVo.getIndemnityDutyRate().compareTo(
						BigDecimal.ZERO) == 1) {// 有责
					ciIndemDuty = true;
				}
			}
		}
		ddLimit = new BigDecimal(configService.calBzAmount(CodeConstants.FeeTypeCode.PERSONLOSS,ciIndemDuty,registNo));
		meLimit = new BigDecimal(configService.calBzAmount(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES,ciIndemDuty,registNo));
		prLimit = new BigDecimal(configService.calBzAmount(CodeConstants.FeeTypeCode.PROPLOSS,ciIndemDuty,registNo));

		// 超过限额
		if (checkFlag.equals("CI") || checkFlag.equals("BC")) {
			if (deathDisSum.compareTo(ddLimit) == 1
					|| medicalSum.compareTo(meLimit) == 1
					|| propSum.compareTo(prLimit) == 1) {
				return "已超过交强赔付限额，不能发起交强计算书。";
			}
		}
		if (checkFlag.equals("BI") || checkFlag.equals("BC")) {
			if (payBIFlag.equals("Y")) {
				return "已超过商业险赔付限额，不能发起商业计算书。";
			}
		}

		// 垫付任务核赔通过or未通过
		if (checkFlag.equals("CI") || checkFlag.equals("BC")) {
			List<PrpLPadPayMainVo> padPays = padPayService
					.findPadPayMainByRegistNo(registNo);
			for (PrpLPadPayMainVo padPay : padPays) {
				if (padPay.getFlag().equals(CodeConstants.UnderWriteFlag.NORMAL)) {
					return "存在未核赔通过的垫付任务，不能发起交强计算书。";
				} else {
					// 回传字符，表示需要将发起的理算任务-是否诉讼-设置为是-只读
					return "padPayFlag";
				}
			}
		}

		//核损未通过
		 List<PrpLdlossPropMainVo> prpLdlossPropMainVoList = propTaskService.findPropMainListByRegistNo(registNo);
		   List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(registNo);
		   List<PrpLDlossPersTraceMainVo> prpLDlossPersTraceMainVoList = persTraceDubboService.findPersTraceMainVoList(registNo);
		   //List<String> idList = new ArrayList<String>();
		   boolean vLoss = true;
		   if(prpLdlossPropMainVoList != null){
			   for(PrpLdlossPropMainVo prpLdlossPropMainVo:prpLdlossPropMainVoList){
				   if(!CodeConstants.VeriFlag.PASS.equals(prpLdlossPropMainVo.getUnderWriteFlag()) &&
				    		!CodeConstants.VeriFlag.CANCEL.equals(prpLdlossPropMainVo.getUnderWriteFlag())){
				 	   vLoss = false;
				    }
			   } 
		   }
		   if(prpLDlossCarMainVoList != null){
			   for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){
				   if(!CodeConstants.VeriFlag.PASS.equals(prpLDlossCarMainVo.getUnderWriteFlag()) &&
				    		!CodeConstants.VeriFlag.CANCEL.equals(prpLDlossCarMainVo.getUnderWriteFlag())){
					   vLoss = false;
				    }
			   }
		   }
		  
		   //List<String> personIdList = new ArrayList<String>();
		   boolean pLoss = true;
		   if(prpLDlossPersTraceMainVoList != null){
			   for(PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo:prpLDlossPersTraceMainVoList){
				  // personIdList.add(prpLDlossPersTraceMainVo.getId().toString());
				   if(prpLDlossPersTraceMainVo.getAuditStatus()!=null){
				   if(!prpLDlossPersTraceMainVo.getAuditStatus().equals(CodeConstants.AuditStatus.SUBMITCHARGE)){
					   pLoss = false;
				   }
				   }
			   } 
		   }
			   
		   if(!vLoss){
			   return "核损未全部完成，单证不能提交";
		   }
		   if(!pLoss){
			   return "人伤费用审核未完成，单证不能提交";
		   }

		return reMsg;
	}

	/* 
	 * @see ins.sino.claimcar.claim.services.CompensateLaunchService#validCheckCompAuto(java.lang.String)
	 * @param registNo
	 * @return
	 */
	@Override
	public String validCheckCompAuto(String registNo) {
		String reFlag = "Y";
		BigDecimal dlossSum = BigDecimal.ZERO;
		BigDecimal cmp = new BigDecimal(10000);
		List<PrpLDlossCarMainVo> dlossCarMains = lossCarService.findLossCarMainByRegistNo(registNo);
		// 定核损总金额不超过10000元
		for (PrpLDlossCarMainVo dlossCarMain : dlossCarMains) {
			if (dlossCarMain.getUnderWriteFlag().equals(CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE)) {// 核损通过
				if(dlossCarMain.getSumVeriChargeFee()!=null){
				dlossSum.add(dlossCarMain.getSumVeriChargeFee());
				}
				dlossSum.add(dlossCarMain.getSumVeriLossFee());
				dlossSum.add(dlossCarMain.getSumVeriRescueFee());

			} else {
				reFlag = "N";
			}
		}
		int res = dlossSum.compareTo(cmp);
		if (res == 1) {
			return "N";
		}
		// 涉案车辆小于两辆
		if (dlossCarMains.size() > 2) {
			return "N";
		}
		// 只有车辆定损任务，没有财产和人伤

		List<PrpLdlossPropMainVo> lossProps = new ArrayList<PrpLdlossPropMainVo>();
		lossProps = propTaskService.findPropMainListByRegistNo(registNo);
		if (lossProps != null) {
			if (lossProps.size() > 0) {
				return "N";
			}
		}

		List<PrpLDlossPersTraceMainVo> lossPersTrace = new ArrayList<PrpLDlossPersTraceMainVo>();
		lossPersTrace = persTraceDubboService.findPersTraceMainVoList(registNo);
		if (lossPersTrace != null) {
			if (lossPersTrace.size() > 0) {
				return "N";
			}
		}

		// 险别只有车损险、三者险、交强险
		List<PrpLClaimKindVo> claimKinds = claimService.findClaimKindVoListByRegistNo(registNo);
		for(PrpLClaimKindVo claimKind:claimKinds){
			if (!claimKind.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_A)
					&& !claimKind.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_B)
					&& !claimKind.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_BZ)) {
				return "N";
			}
		}
		// 收款人只能为被保险人
		List<PrpLCMainVo> cMainList = policyViewService.getPolicyAllInfo(registNo);// 报案保单信息表
		String insuredName = "";
		if (cMainList.size() > 0) {
			insuredName = cMainList.get(0).getInsuredName();
		}
		List<PrpLPayCustomVo> payCusts = managerService
				.findPayCustomVoByRegistNo(registNo);
		if (payCusts.size() > 0) {
			if (payCusts.size() > 1
					|| !payCusts.get(0).getPayeeName().equals(insuredName)) {
				return "N";
			}
		}

		// 代位求偿案件
		List<PrpLClaimVo> claimList = claimService
				.findClaimListByRegistNo(registNo);
		for (PrpLClaimVo claim : claimList) {
			if(claim.getIsSubRogation()!=null){
				if (claim.getIsSubRogation().equals("1")) {
					return "N";
				}
			}
		}

		return reFlag;
	}
	/* 
	 * @see ins.sino.claimcar.claim.services.CompensateLaunchService#findCompensateLaunchByHql(ins.sino.claimcar.claim.vo.PrpLClaimVo, int, int)
	 * @param prpLClaimVo
	 * @param start
	 * @param length
	 * @return
	 */
	@Override
	public Page<PrpLClaimResultVo> findCompensateLaunchByHql(
			PrpLClaimVo prpLClaimVo, int start, int length) {
		//定义参数list，ps：执行查询时需要转换成object数组
		List<Object> paramValues = new ArrayList<Object>();
		
		//hql查询语句
		StringBuffer queryString = new StringBuffer("from PrpLClaim pm, PrpLWfTaskQuery pq"
				+ " WHERE pm.registNo = pq.registNo");
		if (!prpLClaimVo.getPolicyNo().isEmpty()) {
			if(prpLClaimVo.getPolicyNo().length() <= 7 && prpLClaimVo.getPolicyNo().length() >= 4){
				queryString.append(" AND pm.policyNo LIKE ?");
				paramValues.add("%" + prpLClaimVo.getPolicyNo());
			} else {
				queryString.append(" AND pm.policyNo = ?");
				paramValues.add(prpLClaimVo.getPolicyNo());
			}
		}
		if (!prpLClaimVo.getRegistNo().isEmpty()) {
			if(prpLClaimVo.getRegistNo().length() <= 7 && prpLClaimVo.getRegistNo().length() >= 4){
				queryString.append(" AND pm.registNo LIKE ?");
				paramValues.add("%" + prpLClaimVo.getRegistNo());
			} else {
				queryString.append(" AND pm.registNo = ?");
				paramValues.add(prpLClaimVo.getRegistNo());
			}
		}
		if (!prpLClaimVo.getClaimNo().isEmpty()) {
			if(prpLClaimVo.getClaimNo().length() <= 21 && prpLClaimVo.getClaimNo().length() >= 4){
				queryString.append(" AND pm.claimNo LIKE ?");
				paramValues.add("%" + prpLClaimVo.getClaimNo());
			} else {
				queryString.append(" AND pm.claimNo = ?");
				paramValues.add(prpLClaimVo.getClaimNo());
			}
		}
		if (!prpLClaimVo.getInsuredName().isEmpty()) {
			queryString.append(" AND pq.insuredName LIKE ?");
			paramValues.add("%" + prpLClaimVo.getInsuredName() + "%");
		}
		
		if (!prpLClaimVo.getRiskCode().isEmpty()) {
			queryString.append(" AND pm.riskCode = ?");
			paramValues.add(prpLClaimVo.getRiskCode());
		}
		
		if (prpLClaimVo.getComCode()!=null) {
			queryString.append(" AND pm.comCode = ?");
			paramValues.add(prpLClaimVo.getComCode());
		}
		if (!prpLClaimVo.getMercyFlag().isEmpty()) {
			queryString.append(" AND pm.mercyFlag = ?");
			paramValues.add(prpLClaimVo.getMercyFlag());
		}
		//执行查询
		Page page = databaseDao.findPageByHql(queryString.toString(),( start/length+1 ),length,paramValues.toArray());
		PrpLClaimVo policyInfoVo = new PrpLClaimVo();		
				//对查询结果进行数据处理
		Page pageReturn = assemblyPolicyInfo(page,policyInfoVo);
		return pageReturn;
	}
private Page assemblyPolicyInfo(Page page,PrpLClaimVo policyInfoVo) {
		
	List<PrpLClaimResultVo> resultVoList=new ArrayList<PrpLClaimResultVo>();
		//循环查询结果集合
		for(int i = 0; i<page.getResult().size(); i++ ){

			// 获取当前序号下的保单相关对象数组
			Object[] obj = (Object[])page.getResult().get(i);

			if(obj[0]!=null&&obj[1]!=null){

				PrpLClaimResultVo plyVo = new PrpLClaimResultVo();

				// 将获取的承保主表信息，复制到PolicyInfoVo对象中
				PrpLClaim pm = (PrpLClaim)obj[0];
				Beans.copy().from(pm).to(plyVo);

				// 将获取的标的车表信息，复制到PolicyInfoVo对象中
				PrpLWfTaskQuery pc = (PrpLWfTaskQuery)obj[1];
				Beans.copy().from(pc).to(plyVo);

				// 对象转换
				page.getResult().set(i,plyVo);
			}
		}
		return page;
	}
	/* 
	 * @see ins.sino.claimcar.claim.services.CompensateLaunchService#findAllCompForWriteOff(ins.sino.claimcar.claim.vo.PrpLCompensateVo, int, int)
	 * @param compVo
	 * @param start
	 * @param length
	 * @return
	 */
	@Override
	public ResultPage<PrpLCompensateVo> findAllCompForWriteOff(PrpLCompensateVo compVo,int start,int length,SysUserVo userVo) {
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" from PrpLCompensate compensate,PrpLCompensateExt compensateExt,PrpLClaim claim ");
		sqlUtil.append(" where compensate.compensateNo=compensateExt.compensateNo and claim.claimNo=compensate.claimNo ");
		sqlUtil.append(" and compensateExt.oppoCompensateNo is null and claim.caseNo is null ");
		// 理算冲销发起时，如果该立案号下存在未核赔通过的理算业务数据，不允许发起该立案号的理算冲销
		sqlUtil.append(" and not exists (select 1 from PrpLCompensate where underwriteFlag not in(?, ?, ?) and claimNo = compensate.claimNo)");
		sqlUtil.addParamValue("1");
		sqlUtil.addParamValue("3");
		sqlUtil.addParamValue("7");
		sqlUtil.append(" and  compensate.compensateType = ? ");
		sqlUtil.addParamValue("N");
		sqlUtil.append(" and  compensateExt.writeOffFlag = ? ");
		sqlUtil.addParamValue("0");
		sqlUtil.append(" and  compensate.underwriteFlag in (?,?) ");
		sqlUtil.addParamValue("1");
		sqlUtil.addParamValue("3");
		sqlUtil.andTopComSql("compensate","comCode",userVo.getComCode());
		
		
		if(StringUtils.isNotBlank(compVo.getCompensateNo())){
			sqlUtil.append(" and  compensate.compensateNo = ? ");
			sqlUtil.addParamValue(compVo.getCompensateNo().trim()  );
		}
		if(StringUtils.isNotBlank(compVo.getClaimNo())){
			sqlUtil.append(" and  compensate.claimNo = ? ");
			sqlUtil.addParamValue(compVo.getClaimNo().trim()  );
		}
		if(StringUtils.isNotBlank(compVo.getRegistNo())){
			sqlUtil.append(" and compensate.registNo = ? ");
			sqlUtil.addParamValue(compVo.getRegistNo().trim() );
		}
		if(StringUtils.isNotBlank(compVo.getCompensateKind())){
			sqlUtil.append(" and compensate.compensateKind = ? ");
			sqlUtil.addParamValue(compVo.getCompensateKind() );
		}
		// 2016-10-26 理算冲销发起查询的机构只查询当前人归属机构的
		/*if(StringUtils.isNotBlank(compVo.getComCode())){
			sqlUtil.append(" and compensate.comCode like ? ");
			sqlUtil.addParamValue("%"+compVo.getComCode() );
		}*/

		sqlUtil.append(" ORDER BY underwriteDate DESC ");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		System.out.println(sql);
		System.out.println(values);
		Page page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		List<PrpLCompensateVo> compList = new ArrayList<PrpLCompensateVo>();
		for(int i=0;i<page.getResult().size();i++){
			PrpLCompensateVo compTempVo = new PrpLCompensateVo();
			Object[] obj = (Object[])page.getResult().get(i);
			compTempVo = Beans.copyDepth().from(obj[0]).to(PrpLCompensateVo.class);
			compList.add(compTempVo);
		}
		ResultPage<PrpLCompensateVo> resultPage = new ResultPage<PrpLCompensateVo>(start, length, page.getTotalCount(), compList);

		return resultPage;
		
	}

	/* 
	 * @see ins.sino.claimcar.claim.services.CompensateLaunchService#findWriteOffCompensateVoByOriCompNo(java.lang.String)
	 * @param compNo
	 * @return
	 */
	@Override
	public PrpLCompensateVo findWriteOffCompensateVoByOriCompNo(String compNo){
		PrpLCompensateVo writeOffCompensateVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("writeOffFlag","1");//冲销标志位 0- 正常案件 1 -全部冲销 2 - 部分冲销
		queryRule.addEqual("oppoCompensateNo",compNo);//冲销计算书的对冲计算书号为原计算书号
		List<PrpLCompensateExt> compExtPoList = databaseDao.findAll(PrpLCompensateExt.class,queryRule);
		String writeOffCompNo = null;
		if(compExtPoList!=null&&!compExtPoList.isEmpty()){
			writeOffCompNo = compExtPoList.get(0).getCompensateNo();
		}
		if(writeOffCompNo!=null){
			writeOffCompensateVo = compensateService.findCompByPK(writeOffCompNo);
		}
		return writeOffCompensateVo;
	}
	
	
	public String submitPrePayWriteOff(PrpLCompensateVo compensateVo,List<PrpLLossItemVo> lossItemVos,List<PrpLLossPropVo> lossPropVos,
			List<PrpLLossPersonVo> lossPersons,List<PrpLChargeVo> charges,List<PrpLPaymentVo> payments,
			List<PrpLKindAmtSummaryVo> prpLKindAmtSummaryVoList,SubmitNextVo submitNextVo,SysUserVo userVo) throws Exception{
		
		String newCompNo = null;
		PrpLCompensateVo newCompVo = null;
		PrpLWfTaskVo wfTaskVo = null;
		String oldCompensate  = compensateVo.getCompensateNo();
		PrpLCompensateVo compVo = compensateService.findCompByPK(oldCompensate);
		if(compVo!=null&&compVo.getPrpLCompensateExt()!=null&&"1".equals(compVo.getPrpLCompensateExt().getWriteOffFlag())){
			//提交的是退回的已存在业务表的冲销计算书，此时执行更新而非新增
			newCompNo = compVo.getCompensateNo();
			newCompVo = compVo;
			String subNodeCode = FlowNode.CompeWfBI.name();
			if("1101".equals(compVo.getRiskCode())){
				subNodeCode = FlowNode.CompeWfCI.name();
			}
			BigDecimal taskID = wfTaskHandleService.findTaskId(compVo.getClaimNo(),subNodeCode);
			wfTaskVo = wfTaskHandleService.findTaskIn(taskID.doubleValue());
		}else{
			// List排空
			lossItemVos.removeAll(Collections.singleton(null));
			lossPropVos.removeAll(Collections.singleton(null));
			lossPersons.removeAll(Collections.singleton(null));
			//清空子表ID
			for(PrpLLossItemVo lossItem:lossItemVos){
				lossItem.setId(null);
			}
			for(PrpLLossPropVo lossProp:lossPropVos){
				lossProp.setId(null);
			}
			for(PrpLLossPersonVo lossPers:lossPersons){
				lossPers.setId(null);
				for(PrpLLossPersonFeeVo persFee:lossPers.getPrpLLossPersonFees()){
					persFee.setId(null);
					if(persFee.getPrpLLossPersonFeeDetails() != null && persFee.getPrpLLossPersonFeeDetails().size() > 0){
						for(PrpLLossPersonFeeDetailVo feeDetailVo : persFee.getPrpLLossPersonFeeDetails()){
							feeDetailVo.setId(null);
						}
					}
				}
			}
			for(PrpLChargeVo lossCharge:charges){
				lossCharge.setId(null);
			}
			for(PrpLPaymentVo payCus:payments){
				payCus.setId(null);
			}
			for(PrpLKindAmtSummaryVo summary:prpLKindAmtSummaryVoList){
				summary.setId(null);
			}
			compensateVo.setCompensateNo(null);
			compensateVo.setPrpLLossItems(lossItemVos);
			compensateVo.setPrpLLossProps(lossPropVos);
			compensateVo.setPrpLLossPersons(lossPersons);
			compensateVo.setPrpLCharges(charges);
			compensateVo.setPrpLPayments(payments);
			compensateVo.setOppoCompensateNo(oldCompensate);
			
			newCompNo = compensateService.saveCompensates(compensateVo, lossItemVos, lossPropVos, lossPersons, charges, payments,userVo);
			/*if(comExtVo.getId()==null){
			//comExtVo.setPrpLCompensate(compOriginVo);
				comExtVo.setRegistNo(compOriginVo.getRegistNo());
			}
			comExtVo.setOppoCompensateNo(newCompNo);// 原计算书写入对冲计算书号
			compensatateService.saveOrUpdateCompExtVo(comExtVo,compOriginVo);*/
			newCompVo = compensateService.findCompByPK(newCompNo);
			
			//如果是联共保单，需要保存分摊信息
			this.saveCoinsByWriteOff(oldCompensate, newCompNo);

			// 发起一个理算冲销任务
			WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
			WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
			taskVo.setRegistNo(compensateVo.getRegistNo());
			taskVo.setHandlerIdKey(newCompNo);// 计算书号
			if("1101".equals(compensateVo.getRiskCode())){
				submitVo.setCurrentNode(FlowNode.CompeCI);
				submitVo.setNextNode(FlowNode.CompeWfCI);
			}else{
				submitVo.setCurrentNode(FlowNode.CompeBI);
				submitVo.setNextNode(FlowNode.CompeWfBI);
			}

			// 查询已处理的将被冲销的理算对应的核赔任务
			//List<PrpLWfTaskVo> wfTaskVos = wfTaskHandleService.findEndTask(compensateVo.getRegistNo(),compensateVo.getCompensateNo(),submitVo.getCurrentNode());
			List<PrpLWfTaskVo> wfTaskVos = wfTaskHandleService.findEndTask(compensateVo.getRegistNo(),oldCompensate,FlowNode.Compe);
			if(wfTaskVos!=null&&wfTaskVos.size()>0){
				taskVo.setRegistNo(wfTaskVos.get(0).getRegistNo());
				taskVo.setHandlerIdKey(newCompNo);//冲销计算书号
				taskVo.setItemName(wfTaskVos.get(0).getItemName());
				taskVo.setBussTag(wfTaskVos.get(0).getBussTag());
				taskVo.setShowInfoXml(wfTaskVos.get(0).getShowInfoXML());

				submitVo.setFlowId(wfTaskVos.get(0).getFlowId());
				submitVo.setFlowTaskId(wfTaskVos.get(0).getTaskId());
			}

			submitVo.setComCode(submitNextVo.getComCode());
			submitVo.setTaskInUser(userVo.getUserCode());
			submitVo.setTaskInKey(compensateVo.getCompensateNo());
			// 指定原出来人
			submitVo.setAssignCom(submitNextVo.getAssignCom());
			submitVo.setAssignUser(submitNextVo.getAssignUser());
			wfTaskVo = wfTaskHandleService.addPrePayWriteOffTask(newCompVo,submitVo,taskVo);
			//PrpLWfTaskVo wfTaskVo = wfTaskHandleService.addSimpleTask(taskVo,submitVo);
		}
		// 提交理算冲销到核赔
		WfTaskSubmitVo submiVClaimVo = new WfTaskSubmitVo();

		submiVClaimVo.setFlowTaskId(wfTaskVo.getTaskId());
		submiVClaimVo.setTaskInKey(wfTaskVo.getTaskInKey());
		submiVClaimVo.setTaskInUser(userVo.getUserCode());
		submiVClaimVo.setFlowId(wfTaskVo.getFlowId());
		//submiVClaimVo.setCurrentNode(FlowNode.VClaim);
		submiVClaimVo.setComCode(policyViewService.getPolicyComCode(compVo.getRegistNo()));
		if("1101".equals(compensateVo.getRiskCode())){
			submiVClaimVo.setCurrentNode(FlowNode.CompeWfCI);
		}else{
			submiVClaimVo.setCurrentNode(FlowNode.CompeWfBI);
		}
		submiVClaimVo.setNextNode(FlowNode.valueOf(submitNextVo.getNextNode()));
		submiVClaimVo.setAssignUser(submitNextVo.getAssignUser());
		submiVClaimVo.setAssignCom(submitNextVo.getAssignCom());

		wfTaskHandleService.submitCompe(newCompVo,submiVClaimVo);
		
		return newCompNo;
	}

	/**
	 * 自动发起理算方法 之后得重写 by yk
	 */
	@Override
	public void getSubmitCompe(List<PrpLCompensateVo> prpLCompensateVos,
			String bcFlag, String handlerUser,SysUserVo userVo) {

		List<PrpLCMainVo> cMainList = new ArrayList<PrpLCMainVo>();
		String compNo = "";
		cMainList = policyViewService.getPolicyAllInfo(prpLCompensateVos.get(0).getRegistNo());// 获取当前报案号下的所有保单信息
		// 商业交强保单号
		String CIPolicyNo = null;
		String BIPolicyNo = null;
		for (PrpLCMainVo cMain : cMainList) {
			if (cMain.getRiskCode().startsWith(PREFIX_CI)) {
				CIPolicyNo = cMain.getPolicyNo();
			}
			if (cMain.getRiskCode().startsWith(PREFIX_BI)) {
				BIPolicyNo = cMain.getPolicyNo();
			}
		}
		if (bcFlag.equals("BI") || bcFlag.equals("CI")) {
			String registNo = prpLCompensateVos.get(0).getRegistNo();
			if (bcFlag.equals("CI")) {
				prpLCompensateVos.get(0).setCreateUser(handlerUser);
				// TODO 理算计算书生成，返回计算书号，添加到Map,key=CI
				PrpLClaimVo prpLClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(registNo, CIPolicyNo);
				compNo = billNoService.getCompensateNo(userVo.getComCode(),prpLClaimVo.getRiskCode());
				
				//调用工作流
				PrpLWfTaskVo wfTaskVo = getSubmit(prpLClaimVo,compNo,FlowNode.CompeCI,FlowNode.ClaimCI,userVo);
				//理算计算start
				
				
				//end
				//理算提交核赔start
				//PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
				WfTaskSubmitVo nextVo = new WfTaskSubmitVo();
				nextVo.setTaskInUser(userVo.getUserCode());
				// 设置默认下一个处理人,TODO　之后改成轮询机制处理
				nextVo.setFlowTaskId(wfTaskVo.getTaskId());
				// nextVo.setSubmitType(submitType);
				nextVo.setAssignUser(userVo.getUserCode());
				nextVo.setComCode(policyViewService.getPolicyComCode(registNo));
				nextVo.setFlowId(wfTaskVo.getFlowId());
				nextVo.setTaskInUser(userVo.getUserCode());
				nextVo.setCurrentNode(FlowNode.valueOf(wfTaskVo.getSubNodeCode()));
				nextVo.setNextNode(FlowNode.VClaim);
				nextVo.setTaskInKey(compNo);
				PrpLCompensateVo prpLCompensateVo = compensateService.findCompByPK(nextVo.getTaskInKey());
				wfTaskHandleService.submitCompe(prpLCompensateVo, nextVo);
				
				//end
				
			}
			if (bcFlag.equals("BI")) {
				prpLCompensateVos.get(1).setCreateUser(handlerUser);
				// TODO 理算计算书生成，返回计算书号，添加到Map,key=BI
				PrpLClaimVo prpLClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(prpLCompensateVos.get(0).getRegistNo(), BIPolicyNo);
				compNo = billNoService.getCompensateNo(userVo.getComCode(),prpLClaimVo.getRiskCode());
				
				//调用工作流
				PrpLWfTaskVo wfTaskVo = getSubmit(prpLClaimVo,compNo,FlowNode.CompeBI,FlowNode.ClaimBI,userVo);
				
			
				//理算计算start
				
				
				//end
				//理算提交核赔start
				//PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
				WfTaskSubmitVo nextVo = new WfTaskSubmitVo();
				nextVo.setTaskInUser(userVo.getUserCode());
				// 设置默认下一个处理人,TODO　之后改成轮询机制处理
				nextVo.setFlowTaskId(wfTaskVo.getTaskId());
				// nextVo.setSubmitType(submitType);
				nextVo.setAssignUser(userVo.getUserCode());
				nextVo.setComCode(policyViewService.getPolicyComCode(registNo));
				nextVo.setFlowId(wfTaskVo.getFlowId());
				nextVo.setTaskInUser(userVo.getUserCode());
				nextVo.setCurrentNode(FlowNode.valueOf(wfTaskVo.getSubNodeCode()));
				nextVo.setNextNode(FlowNode.VClaim);
				nextVo.setTaskInKey(compNo);
				PrpLCompensateVo prpLCompensateVo = compensateService.findCompByPK(nextVo.getTaskInKey());
				wfTaskHandleService.submitCompe(prpLCompensateVo, nextVo);
				
				//end
			}
		}
		if (bcFlag.equals("BC")) {
			// 调用两次计算书生成，返回计算书号
			PrpLClaimVo prpLClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(prpLCompensateVos.get(0).getRegistNo(), CIPolicyNo);
			compNo = billNoService.getCompensateNo(userVo.getComCode(),prpLClaimVo.getRiskCode());
			
			PrpLWfTaskVo wfTaskVo = getSubmit(prpLClaimVo,compNo,FlowNode.CompeCI,FlowNode.ClaimCI,userVo);
			//理算计算start
			
			
			//end
			WfTaskSubmitVo nextVo = new WfTaskSubmitVo();
			nextVo.setTaskInUser(userVo.getUserCode());
			// 设置默认下一个处理人,TODO　之后改成轮询机制处理
			nextVo.setFlowTaskId(wfTaskVo.getTaskId());
			// nextVo.setSubmitType(submitType);
			nextVo.setAssignUser(userVo.getUserCode());
			nextVo.setComCode(userVo.getComCode());
			nextVo.setFlowId(wfTaskVo.getFlowId());
			nextVo.setTaskInUser(userVo.getUserCode());
			nextVo.setCurrentNode(FlowNode.valueOf(wfTaskVo.getSubNodeCode()));
			nextVo.setNextNode(FlowNode.VClaim);
			nextVo.setTaskInKey(compNo);
			PrpLCompensateVo prpLCompensateVo = compensateService.findCompByPK(nextVo.getTaskInKey());
			wfTaskHandleService.submitCompe(prpLCompensateVo, nextVo);
			prpLClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(prpLCompensateVos.get(0).getRegistNo(), BIPolicyNo);
			compNo = billNoService.getCompensateNo(userVo.getComCode(),prpLClaimVo.getRiskCode());
			
			//调用工作流
			wfTaskVo = getSubmit(prpLClaimVo,compNo,FlowNode.CompeBI,FlowNode.ClaimBI,userVo);
			//理算计算start
			
			
			//end
			//理算提交核赔start
			//PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
			//WfTaskSubmitVo nextVo = new WfTaskSubmitVo();
			nextVo.setTaskInUser(userVo.getUserCode());
			// 设置默认下一个处理人,TODO　之后改成轮询机制处理
			nextVo.setFlowTaskId(wfTaskVo.getTaskId());
			// nextVo.setSubmitType(submitType);
			nextVo.setAssignUser(userVo.getUserCode());
			nextVo.setComCode(userVo.getComCode());
			nextVo.setFlowId(wfTaskVo.getFlowId());
			nextVo.setTaskInUser(userVo.getUserCode());
			nextVo.setCurrentNode(FlowNode.valueOf(wfTaskVo.getSubNodeCode()));
			nextVo.setNextNode(FlowNode.VClaim);
			nextVo.setTaskInKey(compNo);
			prpLCompensateVo = compensateService.findCompByPK(nextVo.getTaskInKey());
			wfTaskHandleService.submitCompe(prpLCompensateVo, nextVo);
			
			//end
		}
	}

	//提交工作流
	public PrpLWfTaskVo getSubmit(PrpLClaimVo prpLClaimVo,String compNo,FlowNode nextNode,FlowNode currentNode,SysUserVo userVo){
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setCurrentNode(currentNode);
		submitVo.setNextNode(nextNode);
		System.out.println("kdddddk="+prpLClaimVo.getRegistNo()+"hh"+prpLClaimVo.getClaimNo()+"ff"+submitVo.getCurrentNode());
		//List<PrpLWfTaskVo> wfTaskVos = wfTaskHandleService.findEndTask(prpLClaimVo.getRegistNo(),prpLClaimVo.getClaimNo(),submitVo.getCurrentNode());
		List<PrpLWfTaskVo> wfTaskVos = wfFlowQueryService.findCertiByRegistNo(prpLClaimVo.getRegistNo());
		if(wfTaskVos!=null&&wfTaskVos.size()>0){

			submitVo.setFlowId(wfTaskVos.get(0).getFlowId());
			submitVo.setFlowTaskId(wfTaskVos.get(0).getTaskId());
		}else{
			try {
				throw new Exception("没有查询到立案节点！");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		submitVo.setComCode(userVo.getComCode());
		submitVo.setTaskInUser(userVo.getUserCode());
		submitVo.setTaskInKey(prpLClaimVo.getClaimNo());
		submitVo.setHandleIdKey(compNo);
		// 指定原出来人
		submitVo.setAssignCom(userVo.getComCode());
		submitVo.setAssignUser(userVo.getUserCode());

//			PrpLWfTaskVo wfTaskVo = WfTaskHandleService.addSimpleTask(taskVo,submitVo);
		//PrpLWfTaskVo wfTaskVo = wfTaskHandleService.addPrePayTask(prpLClaimVo,submitVo);
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.addCompenTask(prpLClaimVo,submitVo);
		return wfTaskVo;
	}
	
	public List<PrpLPlatLockVo> organizeRecoveryList(PrpLCompensateVo compVo){
		List<PrpLPlatLockVo> platLockList = new ArrayList<PrpLPlatLockVo>();
		
		platLockList = platLockService.findPrpLPlatLockVoList(compVo.getRegistNo(), compVo.getPolicyNo(), null);
		if(platLockList!=null && !platLockList.isEmpty()){
			for(PrpLPlatLockVo platLockVo : platLockList){
				List<PrpLRecoveryOrPayVo> recoveryList = platLockVo.getPrpLRecoveryOrPays();
				if(compVo!=null){
					for(PrpLRecoveryOrPayVo recoveryVo : recoveryList){
						if(compVo.getCompensateNo().equals(compVo.getCompensateNo())){
							platLockVo.setThisPaid(recoveryVo.getRecoveryOrPayAmount());
							break;
						}
					}
				}
			}
		}
		
		return platLockList;
	}
	
	public void saveCoinsByWriteOff(String oldCompensateNo,String newCompensateNo){
		List<PrpLCoinsVo> prpLCoinsList = compensateTaskService.findPrpLCoinsByCompensateNo(oldCompensateNo);
		if(prpLCoinsList!=null && prpLCoinsList.size()>0){
			for(PrpLCoinsVo coinsVo:prpLCoinsList){
				//理算冲销的分摊金额变成负数
				coinsVo.setShareAmt(BigDecimal.ZERO.subtract(coinsVo.getShareAmt()!=null ? coinsVo.getShareAmt():BigDecimal.ZERO));
				coinsVo.setCompensateNo(newCompensateNo);
				coinsVo.setId(null);
			}
			compensateTaskService.savePrpLCoins(prpLCoinsList);
		}
	}
}
