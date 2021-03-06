package ins.sino.claimcar.claim.services.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.lang.Springs;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BillNoService;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.DateUtils;
import ins.platform.utils.HttpClientHander;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.PrpLLawSuitVo;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.ClaimFlag;
import ins.sino.claimcar.CodeConstants.FlowStatus;
import ins.sino.claimcar.carplatform.po.CiClaimPlatformLog;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.carplatform.po.CiClaimPlatformLog;
import ins.sino.claimcar.carplatform.util.CodeConvertTool;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDriverVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckTaskVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.po.PrpLClaim;
import ins.sino.claimcar.claim.po.PrpLClaimKind;
import ins.sino.claimcar.claim.po.PrpLClaimKindFee;
import ins.sino.claimcar.claim.po.PrplFraudriskInfo;
import ins.sino.claimcar.claim.po.PrplLaborInfo;
import ins.sino.claimcar.claim.po.PrplOperationInfo;
import ins.sino.claimcar.claim.po.PrplPartsInfo;
import ins.sino.claimcar.claim.po.PrplRiskpointInfo;
import ins.sino.claimcar.claim.po.PrplTestinfoMain;
import ins.sino.claimcar.claim.service.ClaimKindHisService;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.ClaimTaskExtService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.ConfigService;
import ins.sino.claimcar.claim.vo.EstimatedLossAmountInfoVo;
import ins.sino.claimcar.claim.vo.PrpLClaimKindFeeVo;
import ins.sino.claimcar.claim.vo.PrpLClaimKindHisVo;
import ins.sino.claimcar.claim.vo.PrpLClaimKindVo;
import ins.sino.claimcar.claim.vo.PrpLClaimSummaryVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrplFraudriskInfoVo;
import ins.sino.claimcar.claim.vo.PrplLaborInfoVo;
import ins.sino.claimcar.claim.vo.PrplOperationInfoVo;
import ins.sino.claimcar.claim.vo.PrplPartsInfoVo;
import ins.sino.claimcar.claim.vo.PrplRiskpointInfoVo;
import ins.sino.claimcar.claim.vo.PrplTestinfoMainVo;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.RepairFactoryService;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.interf.vo.CECheckResultVo;
import ins.sino.claimcar.interf.vo.CheckVo;
import ins.sino.claimcar.interf.vo.ClaimVo;
import ins.sino.claimcar.interf.vo.CommercialInsuranceVo;
import ins.sino.claimcar.interf.vo.CompulsoryInsuranceVo;
import ins.sino.claimcar.interf.vo.ConfirmLossDiscussionVo;
import ins.sino.claimcar.interf.vo.ConfirmLossVo;
import ins.sino.claimcar.interf.vo.DataVo;
import ins.sino.claimcar.interf.vo.DriverVo;
import ins.sino.claimcar.interf.vo.FraudRiskVo;
import ins.sino.claimcar.interf.vo.HistoricalClaimVo;
import ins.sino.claimcar.interf.vo.InsuranceItemVo;
import ins.sino.claimcar.interf.vo.InsuranceModificationVo;
import ins.sino.claimcar.interf.vo.LaborResultVo;
import ins.sino.claimcar.interf.vo.LaborVo;
import ins.sino.claimcar.interf.vo.NonStandardOperationVo;
import ins.sino.claimcar.interf.vo.PartVo;
import ins.sino.claimcar.interf.vo.PriceSummaryVo;
import ins.sino.claimcar.interf.vo.RelatingVehicleVo;
import ins.sino.claimcar.interf.vo.ReportVo;
import ins.sino.claimcar.interf.vo.RiskPointVo;
import ins.sino.claimcar.interf.vo.SmallSparepartVo;
import ins.sino.claimcar.interf.vo.SparepartVo;
import ins.sino.claimcar.interf.vo.SpecialAgreementVo;
import ins.sino.claimcar.interf.vo.TransitClaimDocumentBaseVo;
import ins.sino.claimcar.interf.vo.VehicleVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMaterialVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarRepairVo;
import ins.sino.claimcar.manager.vo.PrpLRepairFactoryVo;
import ins.sino.claimcar.platform.service.SendClaimToPlatformService;

/*import ins.sino.claimcar.platform.service.SendEstimatedToPlatformService;*/

import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLCengageVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.regist.vo.PrppheadVo;
import ins.sino.claimcar.regist.vo.PrpptextVo;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.util.Assert;

import com.alibaba.dubbo.config.annotation.Service;



@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"} , timeout = 1000*60*10)
@Path("claimTaskService")
public class ClaimTaskServiceImpl implements ClaimTaskService {

	private static Logger logger = LoggerFactory.getLogger(ClaimTaskServiceImpl.class);
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	private ConfigService prpLDLimitService;
	@Autowired
	private ClaimService claimService;
	@Autowired
	private BillNoService billNoService;
	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	private ClaimTaskExtService claimTaskExtService;
	@Autowired
	private ClaimKindHisService claimKindHisService;
	@Autowired
    SendClaimToPlatformService sendClaimToAll;
	/*@Autowired
	private SendEstimatedToPlatformService estimatedToPlatform;*/
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	@Autowired
    ClaimInterfaceLogService interfaceLogService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	LossCarService lossCarService;
	@Autowired
	ClaimTextService claimTextService;
	@Autowired
	ScheduleService scheduleService;
	@Autowired
	RepairFactoryService repairFactoryService;

	@Override
	public List<PrpLClaimVo> findClaimListByRegistNo(String registNo) {
		return claimService.findClaimListByRegistNo(registNo);
	}

	/**
	 * ????????????????????????
	 * <pre></pre>
	 * @param registNo
	 * @param policyNo
	 * @param validFlag ????????????
	 * @return
	 * @modified:
	 * ???ZhouYanBin(2016???5???13??? ??????2:30:09): <br>
	 */
	@Override
	public List<PrpLClaimVo> findprpLClaimVoListByRegistAndPolicyNo(String registNo,String policyNo,String validFlag){
		return claimService.findprpLClaimVoListByRegistAndPolicyNo(registNo,policyNo,validFlag);
	}

	@Override
	public PrpLClaimVo findClaimVoByClaimNo(String claimNo) {
		return claimService.findByClaimNo(claimNo);
	}

	@Override
	public void submitClaim(String registNo,String flowId) throws ParseException {
		List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(registNo);
		if(cMainVoList != null && !cMainVoList.isEmpty()){
			for(PrpLCMainVo cMainVo : cMainVoList){
				claimTaskExtService.submitClaim(registNo,cMainVo.getPolicyNo(),flowId,ClaimFlag.AUTOCLAIM);
			}
		}
//		for(PrpLCMainVo prpLCMainVo:prpLCMainVoList){
//			String policyNo = prpLCMainVo.getPolicyNo();
//			PrpLClaimVo claimVo = claimTaskExtService.submitClaim(registNo,policyNo,flowId,ClaimFlag.AUTOCLAIM);
//			//???????????????
//			if(claimVo != null){
//				sendClaimToAll.sendClaimToPlatform(claimVo);
//			}
//		}
	}

	@Override
	public void claimForceJob(String registNo,String policyNo,String flowId) throws ParseException {
		PrpLClaimVo claimVo = claimTaskExtService.submitClaim(registNo,policyNo,flowId,ClaimFlag.CLAIMFORCE);
		//???????????????
		if(claimVo != null){
			sendClaimToPlatform(claimVo);
		}
	}
	
	// ???????????????
	@Override
	public void sendClaimToPlatform(PrpLClaimVo claimVo) {
		if(claimVo!=null){
			sendClaimToAll.sendClaimToPlatform(claimVo);
		}
	}

	
	// ??????????????????????????????????????????
	@Override
	public void updateClaimFee(String registNo,String userCode,FlowNode node) throws Exception {
		claimTaskExtService.updateClaimFee(registNo,userCode,node);
	}

	/**
	 * ?????????????????????????????????????????????
	 * @param registNo
	 * @param userCode
	 * @param node
	 * @modified: ???weilanlei(2016???6???22??? ): <br>
	 */
	@Override
	public void updateClaimAfterCompe(String compensateNo,String userCode,FlowNode node) {
		claimTaskExtService.updateClaimAfterCompe(compensateNo,userCode,node);
	}
	/**
	 * ???????????????????????????0?????????????????????????????????
	 * @param registNo
	 * @param userCode
	 * @param node
	 * @modified: ???weilanlei(2016???12???1??? ): <br>
	 */
	@Override
	public void updateClaimAfterCompeWfZero(String compensateNo,String userCode,FlowNode node){
		claimTaskExtService.updateClaimAfterCompeWfZero(compensateNo,userCode,node);
	}
	//
	@Override
	public void claimWirteBack(PrpLClaimVo claimVo) {
		claimTaskExtService.claimWirteBack(claimVo);
	}

	@Override
	public void reOpenClaimWirteBack(String claimNo,FlowNode node,String endCaserCode) {
		claimTaskExtService.reOpenClaimWirteBack(claimNo,node,endCaserCode);
	}

	@Override
	public List<PrpLClaimKindHisVo> findPrpLClaimKindHisByRegistNo(
			String registNo) {
		List<PrpLClaimKindHisVo> claimKindHisVos = claimKindHisService.findPrpLClaimKindHisByRegistNo(registNo);
		return claimKindHisVos;
	}

	@Override
	public List<PrpLClaimSummaryVo> findPrpLClaimSummaryByRegistNo(String registNo) {

		List<PrpLClaimSummaryVo> claimSummaryList = claimService.findPrpLClaimSummaryByRegistNo(registNo);
		return claimSummaryList;
	}
	@Override
	public PrpLClaimSummaryVo findPrpLClaimSummaryVoByRegistNo(String claimNo) {

		PrpLClaimSummaryVo prpLClaimSummaryVo = claimService.findPrpLClaimSummaryVoByRegistNo(claimNo);
		return prpLClaimSummaryVo;
	}
	
	@Override
	public List<PrpLClaimVo> findClaimListByRegistNo(String registNo,
			String validFlag) {
		return claimService.findClaimListByRegistNo(registNo,validFlag);
	}
	
	@Override
	public PrpLClaimVo getClaimVo(String registNo,String riskCode) {
		//String claimNo = "";
		Assert.notNull(registNo,"registNo must have value.");
		Assert.notNull(riskCode,"riskCode must have value.");
		
		QueryRule rule = QueryRule.getInstance();
		rule.addEqual("registNo",registNo);
		if("11".equals(riskCode)){
			rule.addEqual("riskCode","1101");
		}else{
			rule.addNotEqual("riskCode","1101");
		}
		
		List<PrpLClaim> poList = databaseDao.findAll(PrpLClaim.class,rule);
		PrpLClaimVo vo =null;
		if(poList != null && poList.size() > 0){
			vo=new PrpLClaimVo();
			Beans.copy().from(poList.get(0)).to(vo);
//			vo = Beans.copyDepth().from(poList.get(0)).to(PrpLClaimVo.class);
		}
		return vo;
	}

	/**
	 * ??????????????????
	 * @param claimNo
	 * @return
	 * @modified: ???LiuPing(2016???6???3??? ): <br>
	 */
	public void calcClaimKindHisByEndCase(String claimNo){
		claimKindHisService.calcClaimKindHisByEndCase(claimNo);
	}

	/**
	 * ????????????????????????
	 * @param claimNo
	 * @return
	 * @modified: ???LiuPing(2016???6???3??? ): <br>
	 */
	public void calcClaimKindHisByReOpen(String claimNo){
		claimKindHisService.calcClaimKindHisByReOpen(claimNo);
	}

	@Override
	public List<PrpLLawSuitVo> findPrpLLawSuitVoByRegistNo(String RegistNo) {
		
			
		return claimTaskExtService.findPrpLLawSuitVoByRegistNo(RegistNo);
		
		
	
	}


	@Override
	public void updateClaimFeeForFifteen(PrpLRegistVo registVo) {
		claimTaskExtService.updateClaimFeeForFifteen(registVo);
		
	}
	@Override
	public boolean adjustPropLossState(String registNo) {
		
		return claimTaskExtService.adjustPropLossState(registNo);
	}

	@Override
	public boolean adjustPersLossState(String registNo) {
		return claimTaskExtService.adjustPersLossState(registNo);
	}
	/**
	 * 30??????????????? ??????????????????
	 * <pre></pre>
	 * @param registVo
	 * @modified:
	 * ???WLL(2016???8???17??? ??????5:46:37): <br>
	 */
	public void updateClaimFeeForThirty(PrpLRegistVo registVo){
		claimTaskExtService.updateClaimFeeForThirty(registVo);
	}

	
	@Override
	public void wirteBackClaimKindAndFee(List<PrpLClaimKindVo> kindVo,List<PrpLClaimKindFeeVo> feeVo) {
		logger.debug("size:"+kindVo.size());
		if(kindVo!=null&&kindVo.size()>0){
			for(PrpLClaimKindVo kind:kindVo){
				PrpLClaimKind kindPo = databaseDao.findByPK(PrpLClaimKind.class,kind.getId());
				Beans.copy().from(kind).includeEmpty().to(kindPo);
				kindPo.setUpdateTime(new Date());
				databaseDao.update(PrpLClaimKind.class,kindPo);
			}
		}
		if(feeVo!=null&&feeVo.size()>0){
			for(PrpLClaimKindFeeVo fee:feeVo){
				PrpLClaimKindFee feePo = databaseDao.findByPK(PrpLClaimKindFee.class,fee.getId());
				Beans.copy().from(fee).includeEmpty().to(feePo);
				feePo.setUpdateTime(new Date());
				databaseDao.update(PrpLClaimKind.class,feePo);
			}
		}

	}

	@Override
	public void cancleClaim(String claimNo,String validFlag,BigDecimal flowTaskId,SysUserVo userVo,String registNo,WfTaskSubmitVo submitVo) {
		List<PrpLClaimVo> prpLClaimVoList = claimService.findClaimListByRegistNo(registNo);
		if(prpLClaimVoList != null && prpLClaimVoList.size() > 1){
			for(PrpLClaimVo prpLClaimVo:prpLClaimVoList){
				if(!claimNo.equals(prpLClaimVo.getClaimNo())){//?????????????????????????????????????????????
					if("0".equals(prpLClaimVo.getCancelCode()) || "2".equals(prpLClaimVo.getCancelCode())){
						//List<PrpLWfTaskVo> PrpLWfTaskListVo = wfFlowQueryService.findPrpWfTaskVo(registNo, FlowNode.Cancel.toString());
						//if(PrpLWfTaskListVo==null || PrpLWfTaskListVo.size()==0){
							submitVo.setFlowStatus(FlowStatus.CANCEL);
						//}
						
					}
				}
			}
		}else if(prpLClaimVoList != null && prpLClaimVoList.size() == 1){
			submitVo.setFlowStatus(FlowStatus.CANCEL);
		}
		
		claimTaskExtService.cancleClaim(claimNo,validFlag,flowTaskId,userVo,submitVo);
		
	}

	@Override
	public void cancleClaim(BigDecimal flowTaskId,String userCode,SysUserVo userVo) {
		claimTaskExtService.cancleClaim(flowTaskId,userCode,userVo);
		
	}

	/* 
	 * @see ins.sino.claimcar.claim.service.ClaimTaskService#getClaimVoByEstimatedLossAmount()
	 * @return
	 */
	@Override
	public List<EstimatedLossAmountInfoVo> getClaimVoByEstimatedLossAmount(String comCode) {
		// TODO Auto-generated method stub
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
		
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append("from PrpLClaim claim,CiClaimPlatformLog log where 1=1 ");
		sqlUtil.append(" claim.claimNo = log.bussNo");
		sqlUtil.addIn("claim","riskCode","1101","1201","1206","1207");
		sqlUtil.append(" claim.caseNo<>? and claim.endCaseTime is null");//??????????????????
		sqlUtil.addParamValue("0");
		sqlUtil.append(" and claim.comcode like ?");
		sqlUtil.addParamValue(comCode+"%");
		sqlUtil.append(" claim.updateTime<? and claim.updateTime>?");
		sqlUtil.addParamValue(sdf.format(cal.getTime()));
		cal.add(Calendar.DATE, -2);// ??????????????????
		sqlUtil.addParamValue(sdf.format(cal.getTime()));
		
		String hql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		
		List<Object[]> objects = databaseDao.findAllByHql(hql,values);
		List<EstimatedLossAmountInfoVo> infoList = new ArrayList<EstimatedLossAmountInfoVo>();
		for(Object[] object : objects){
			
			PrpLClaim claimPo = (PrpLClaim)object[0];
			CiClaimPlatformLog logPo = (CiClaimPlatformLog)object[1];
			
			EstimatedLossAmountInfoVo info = new EstimatedLossAmountInfoVo();
			
			String validNo = "";
			if("1101".equals(claimPo.getRiskCode())){
				validNo = policyViewService.findValidNo(claimPo.getRegistNo(),"11");
			}else{
				validNo = policyViewService.findValidNo(claimPo.getRegistNo(),"12");
			}
			
			info.setClaimsequenceno(logPo.getClaimSeqNo());
			info.setConfirmsequenceno(validNo);
			info.setClaimnotificationno(claimPo.getRegistNo());
			info.setEstimatedlossamount(claimPo.getSumDefLoss().doubleValue());
			infoList.add(info);
		}
		
		return infoList;
	}

	
	/**
	 * ?????????????????????????????????
	 * ????????????????????????????????????[???????????????????????????????????????????????????????????????????????????20???00?????????6:00??????????????????????????????????????????]
	 * ??????????????????????????????????????????
	 * @modified:
	 * ???Luwei(2016???12???21??? ??????11:24:58): <br>
	 */
	/*public void sendEstimatedByTimer(){
		List<PrpLClaimVo> claimVoList = findClaimVoByEstimated();
		if(claimVoList != null && !claimVoList.isEmpty()){
			for(PrpLClaimVo claimVo : claimVoList){
				estimatedToPlatform.sendEstimatedToPlatform(claimVo);
			}
		}
	}*/
	

	@Override
	public void cancleClaimForOther(String registNo, String userCode) {
		// ???????????????????????????
		wfTaskHandleService.cancelTaskForOther(registNo, userCode);
	}
	
	@Override
	public void recoverClaimForOther(String registNo, String userCode,BigDecimal flowTaskId) {
		// ???????????????????????????
		wfTaskHandleService.recoverClaimForOther(registNo, userCode,flowTaskId);
	}

	@Override
	public PrplTestinfoMainVo findPrplTestinfoMainByRegistNoAndLicenseNo(String registNo, String licenseNo,Long lossCarMainId,String nodeflag) {
				QueryRule rule=QueryRule.getInstance();
				rule.addEqual("registNo",registNo);
				if("3".equals(nodeflag)){
					rule.addEqual("lossCarMainId",String.valueOf(lossCarMainId));
				}
				rule.addEqual("vehicleRegistionNumber",licenseNo);
				rule.addEqual("nodeFlag",nodeflag);
				rule.addDescOrder("createTime");
				PrplTestinfoMainVo vo=new PrplTestinfoMainVo();
				List<PrplTestinfoMain> PrplTestinfoMains=databaseDao.findAll(PrplTestinfoMain.class,rule);
				PrplTestinfoMain testInfoMainPo=null;
				if(PrplTestinfoMains!=null && PrplTestinfoMains.size()>0){
					testInfoMainPo=PrplTestinfoMains.get(0);
				}
				QueryRule rule1=QueryRule.getInstance();
				if(testInfoMainPo!=null){
					rule1.addEqual("prplTestinfoMain.id",testInfoMainPo.getId());
				
				List<PrplPartsInfo> partspoList=databaseDao.findAll(PrplPartsInfo.class,rule1);
				List<PrplLaborInfo> laborspoList=databaseDao.findAll(PrplLaborInfo.class,rule1);
				List<PrplFraudriskInfo> fraudRiskpoList=databaseDao.findAll(PrplFraudriskInfo.class,rule1);
				List<PrplRiskpointInfo> riskPointpoList=databaseDao.findAll(PrplRiskpointInfo.class,rule1);
				List<PrplOperationInfo> operationpoList=databaseDao.findAll(PrplOperationInfo.class,rule1);
				testInfoMainPo.setPrplFraudriskInfos(fraudRiskpoList);
				testInfoMainPo.setPrplLaborInfos(laborspoList);
				testInfoMainPo.setPrplOperationInfos(operationpoList);
				testInfoMainPo.setPrplPartsInfos(partspoList);
				testInfoMainPo.setPrplRiskpointInfos(riskPointpoList);
				
				if(testInfoMainPo!=null){
					vo=Beans.copyDepth().from(testInfoMainPo).to(PrplTestinfoMainVo.class);
				}
				}
		return vo;
	}

	@Override
	public void SendControlExpertInterface(String registNo, String licenseNo,SysUserVo userVo,String lossCarMainId,String nodeFlag) throws Exception {
		TransitClaimDocumentBaseVo baseVo=new TransitClaimDocumentBaseVo();
		//??????????????????
		baseVo=setParamsForTransitClaimDocumentBaseVo(registNo,licenseNo,lossCarMainId,nodeFlag);
		
		CECheckResultVo resultVo=new CECheckResultVo();
		 String xmlToSend = ClaimBaseCoder.objToXmlUtf(baseVo);
		 logger.info("????????????????????????send---------------------------"+xmlToSend);//ControlExpert_URL
		 String url= SpringProperties.getProperty("ControlExpert_URL");
	        try{
	        	String xmlReturn=this.requestControlExpert(xmlToSend, url);
	            logger.info("???????????????????????????return---------------------------"+xmlReturn);
	            resultVo =ClaimBaseCoder.xmlToObj(xmlReturn,CECheckResultVo.class);
	            if(resultVo!=null && "00".equals(resultVo.getErrCode())){
	            	saveOrUpdateForPrplTestinfoMain(resultVo,userVo,lossCarMainId,nodeFlag);
	            }
	        }
	        catch(Exception e){
	            e.printStackTrace();
	        }finally{
	          
	            ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
	            logVo.setRegistNo(registNo);
	            if("1".equals(nodeFlag)){
	            	logVo.setOperateNode("??????"); //????????????????????????
	            	logVo.setFlag("1");
	            }else if("2".equals(nodeFlag)){
	            	logVo.setOperateNode("??????"); //????????????????????????
	            	logVo.setFlag("2");
	            }else{
	            	logVo.setOperateNode("????????????"); //????????????????????????
	            	logVo.setFlag("3");
	            }
	            
	            logVo.setClaimNo(lossCarMainId);   //?????????????????????Id
	            logVo.setCompensateNo(licenseNo);  //?????????????????????????????????
	            logVo.setServiceType(resultVo.getExtractionCode()); //?????????
	            logVo.setCreateUser(userVo.getUserCode());
	            logVo.setComCode(userVo.getComCode());
	            logVo.setRequestUrl(url);
	            this.logUtil(baseVo,resultVo,logVo,resultVo.getErrCode(),resultVo.getErrMsg());
	        }
		
	}
	
	/**
	 * ???TransitClaimDocumentBaseVo????????????
	 * @param registNo
	 * @param licenseNo
	 * @return
	 * @throws Exception 
	 */
	private TransitClaimDocumentBaseVo setParamsForTransitClaimDocumentBaseVo(String registNo, String licenseNo,String lossCarMainId,String nodeFlag) throws Exception{
		TransitClaimDocumentBaseVo baseVo=new TransitClaimDocumentBaseVo();
		PrpLDlossCarMainVo carMainVo=new PrpLDlossCarMainVo();//??????????????????
		if(StringUtils.isNotBlank(lossCarMainId)){
			carMainVo=lossCarService.findLossCarMainById(Long.valueOf(lossCarMainId));
		}
		
		List<PrpLDlossCarInfoVo> lossCarInfoList=lossCarService.findPrpLDlossCarInfoVoListByRegistNo(registNo);//????????????????????????
		PrpLDlossCarInfoVo lossCarInfoVo=new PrpLDlossCarInfoVo();//???????????????
		if(lossCarInfoList!=null && lossCarInfoList.size()>0){
			for(PrpLDlossCarInfoVo vo:lossCarInfoList){
				if(licenseNo.equals(vo.getLicenseNo())){
					lossCarInfoVo=vo;
				}
			}
		}
		List<ConfirmLossDiscussionVo> DiscussionVos=new ArrayList<ConfirmLossDiscussionVo>();//??????????????????????????????
		List<LaborVo> laborVos=new ArrayList<LaborVo>();//????????????
		List<SmallSparepartVo> sparepartVos=new ArrayList<SmallSparepartVo>();//????????????
		List<SparepartVo> repartVos=new ArrayList<SparepartVo>();//????????????
		List<RelatingVehicleVo> relatingVehicles=new ArrayList<RelatingVehicleVo>();//???????????????
		
		PrpLRegistVo prpLRegistVo=new PrpLRegistVo();
		PrpLRegistVo prpLRegistVo1=registQueryService.findByRegistNo(registNo);//???????????????
		if(prpLRegistVo1!=null){
			prpLRegistVo=prpLRegistVo1;
		}
		List<PrpLRegistCarLossVo> prpLRegistCarLosses = new ArrayList<PrpLRegistCarLossVo>();
		prpLRegistCarLosses=prpLRegistVo.getPrpLRegistCarLosses();//????????????????????????
		PrpLRegistCarLossVo prpLRegistCarLossVo=new PrpLRegistCarLossVo();//???????????????????????????
		if(prpLRegistCarLosses!=null && prpLRegistCarLosses.size()>0){
			for(PrpLRegistCarLossVo vo:prpLRegistCarLosses){
				if(licenseNo.equals(vo.getLicenseNo())){
					prpLRegistCarLossVo=vo;
				}
			}
		}
		PrpLRegistExtVo registExtVo=new PrpLRegistExtVo();
		PrpLRegistExtVo registExtVo1= prpLRegistVo.getPrpLRegistExt();
		if(registExtVo1!=null){
			registExtVo=registExtVo1;
		}
		List<SpecialAgreementVo> bIAgreementVos=new ArrayList<SpecialAgreementVo>();//???????????????????????????
		List<SpecialAgreementVo> cIAgreementVos=new ArrayList<SpecialAgreementVo>();//???????????????????????????
		List<InsuranceModificationVo> bImodificationVos=new ArrayList<InsuranceModificationVo>();//????????????????????????
		List<InsuranceModificationVo> cImodificationVos=new ArrayList<InsuranceModificationVo>();//????????????????????????
		List<InsuranceItemVo> biItemVos=new ArrayList<InsuranceItemVo>();//?????????????????????
		List<InsuranceItemVo> ciItemVos=new ArrayList<InsuranceItemVo>();//?????????????????????
		List<HistoricalClaimVo> bIhisClaimVos=new ArrayList<HistoricalClaimVo>();//??????????????????????????????
		List<HistoricalClaimVo> cIhisClaimVos=new ArrayList<HistoricalClaimVo>();//??????????????????????????????
		PrpLCheckVo prpLCheckVo=new PrpLCheckVo();
		PrpLCheckVo prpLCheckVo1=checkTaskService.findCheckVoByRegistNo(registNo);//????????????
		if(prpLCheckVo1!=null){
			prpLCheckVo=prpLCheckVo1;
		}
		PrpLCheckTaskVo prpLCheckTaskVo=new PrpLCheckTaskVo();
		PrpLCheckTaskVo prpLCheckTaskVo1=prpLCheckVo.getPrpLCheckTask();//???????????????
		if(prpLCheckTaskVo1!=null){
			prpLCheckTaskVo=prpLCheckTaskVo1;
		}
		List<PrpLCheckCarInfoVo> carInfoList=checkTaskService.findPrpLCheckCarInfoVoListByRegistNo(registNo);//??????????????????
		PrpLCheckDriverVo prpLCheckDriverVo=new PrpLCheckDriverVo();//??????????????????
		PrpLCheckCarVo carVo=new PrpLCheckCarVo();//???????????????
		PrpLCheckDutyVo prplcheckdutyVo=new PrpLCheckDutyVo();//???????????????
		List<PrpLCMainVo> prplCmainVos=policyViewService.findPrpLCMainVoListByRegistNo(registNo);//???????????????
		PrpLCMainVo biVo=new PrpLCMainVo();//?????????????????????
		PrpLCMainVo ciVo=new PrpLCMainVo();//?????????????????????
		if(prplCmainVos!=null && prplCmainVos.size()>0){
			for(PrpLCMainVo vo:prplCmainVos){
				if("1101".equals(vo.getRiskCode())){
					ciVo=vo;
				}else{
					biVo=vo;
				}
			}
		}
		//????????????????????????
		if(biVo!=null &&  biVo.getId()!=null){
			List<PrpLClaimVo> claimVos=claimService.findPrpLClaimVosByPolicyNo(biVo.getPolicyNo());
			if(claimVos!=null && claimVos.size()>0){
				for(PrpLClaimVo vo:claimVos){
					PrpLRegistVo registVo=registQueryService.findByRegistNo(vo.getRegistNo());
					HistoricalClaimVo hisclaimVo=new HistoricalClaimVo();
					if(vo.getDamageTime()!=null){
						hisclaimVo.setClaimDate(this.DateFormatString(vo.getDamageTime()));//????????????
					}
					//hisclaimVo.setClaimEndDate(this.DateFormatString(vo.getEndCaseTime()));//????????????
					hisclaimVo.setClaimNumber(vo.getRegistNo());//?????????
					hisclaimVo.setDriverName(registVo.getDriverName());//?????????
					hisclaimVo.setEventAddress(registVo.getDamageAddress());//????????????
					hisclaimVo.setInsuranceCategory("12");//??????
					
					
					hisclaimVo.setInsuranceNumber(vo.getPolicyNo());//????????????
					//hisclaimVo.setPaidAmount(vo.getSumPaid()+"");//??????????????????
					//hisclaimVo.setPaidTimes("");//????????????
					if(vo.getReportTime()!=null){
						hisclaimVo.setReportDate(this.DateFormatString(vo.getReportTime()));//????????????
					}
					hisclaimVo.setReporter(registVo.getReportorName());//?????????
					hisclaimVo.setReporterTel(registVo.getReportorPhone());//???????????????
					hisclaimVo.setStatus("");//????????????
					bIhisClaimVos.add(hisclaimVo);
				}
				
			}
		}
		//??????????????????
		if(biVo.getPrpCItemKinds()!=null && biVo.getPrpCItemKinds().size()>0){
			for(PrpLCItemKindVo vo:biVo.getPrpCItemKinds()){
				InsuranceItemVo itemVo=new InsuranceItemVo();
				if(vo.getAdjustRate()!=null){
					itemVo.setAdjustmentRatio(vo.getAdjustRate()+"");//????????????
				}
				if(vo.getDeductibleRate()!=null){
					itemVo.setDeductibleRate(vo.getDeductibleRate()+"");//?????????
				}
				if(vo.getDiscount()!=null){
					itemVo.setDiscount(vo.getDiscount()+"");//?????????
				}
				if(vo.getAmount()!=null){
					itemVo.setInsuranceAmount(vo.getAmount()+"");//????????????
				}
				if(vo.getBenchMarkPremium()!=null){
					itemVo.setInsuranceFee(vo.getBenchMarkPremium()+"");//????????????
				}
				if(StringUtils.isNotBlank(carMainVo.getLossFeeType())){
					if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(carMainVo.getRiskCode()) != null &&
							CodeConstants.ISNEWCLAUSECODE2020_MAP.get(carMainVo.getRiskCode())){
						itemVo.setInsuranceItemCode(CodeConstants.LossFee2020Kind_Map.get(carMainVo.getRiskCode()+carMainVo.getLossFeeType()));//????????????
					}else{
						itemVo.setInsuranceItemCode(CodeConstants.LossFeeKind_Map.get(carMainVo.getLossFeeType()));//????????????
					}
					
				}else{
					itemVo.setInsuranceItemCode("A");
				}
				
				SysCodeDictVo sysVo1 = codeTranService.findTransCodeDictVo("CarRiskCode",vo.getRiskCode());
				itemVo.setName(sysVo1.getCodeName());//????????????
				
				
				if("1".equals(vo.getNoDutyFlag())){//??????????????????
					itemVo.setNonDeductible("true");
				}else if("0".equals(vo.getNoDutyFlag())){
					itemVo.setNonDeductible("false");
				}else{
					itemVo.setNonDeductible("false");
				}
				if(vo.getQuantity()!=null){
					itemVo.setQty(vo.getQuantity()+"");//??????
				}
				
				itemVo.setRemark("");//??????
				if(vo.getUnitAmount()!=null){
					itemVo.setSingleInsuranceAmount(vo.getUnitAmount()+"");//??????????????????
				}
				
				biItemVos.add(itemVo);
			}
		}
		
		//??????????????????
		if(ciVo.getPrpCItemKinds()!=null && ciVo.getPrpCItemKinds().size()>0){
			for(PrpLCItemKindVo vo:ciVo.getPrpCItemKinds()){
				InsuranceItemVo itemVo=new InsuranceItemVo();
				if(vo.getAdjustRate()!=null){
					itemVo.setAdjustmentRatio(vo.getAdjustRate()+"");//????????????
				}
				if(vo.getDeductibleRate()!=null){
					itemVo.setDeductibleRate(vo.getDeductibleRate()+"");//?????????
				}
				if(vo.getDiscount()!=null){
					itemVo.setDiscount(vo.getDiscount()+"");//?????????*/
				}
				
				if(vo.getAmount()!=null){
			      itemVo.setInsuranceAmount(vo.getAmount()+"");//????????????
				}
			   if(vo.getBenchMarkPremium()!=null){
				   itemVo.setInsuranceFee(vo.getBenchMarkPremium()+"");//???????????? 
			   }
				itemVo.setInsuranceItemCode("BZ");//????????????
				SysCodeDictVo sysVo1 = codeTranService.findTransCodeDictVo("CarRiskCode",vo.getRiskCode());
				
					itemVo.setName(sysVo1.getCodeName());//????????????
				
				
				if("1".equals(vo.getNoDutyFlag())){//??????????????????
					itemVo.setNonDeductible("true");
				}else if("0".equals(vo.getNoDutyFlag())){
					itemVo.setNonDeductible("false");
				}else{
					itemVo.setNonDeductible("false");
				}
				if(vo.getQuantity()!=null){
					itemVo.setQty(vo.getQuantity()+"");//??????
				}
				
				itemVo.setRemark("");//??????
				if(vo.getUnitAmount()!=null){
					itemVo.setSingleInsuranceAmount(vo.getUnitAmount()+"");//??????????????????
				}
				
				ciItemVos.add(itemVo);
			}
		}
		
		//????????????????????????
		if(biVo!=null &&  biVo.getId()!=null){
		   List<PrpLClaimVo> claimVos=claimService.findPrpLClaimVosByPolicyNo(biVo.getPolicyNo());
				if(claimVos!=null && claimVos.size()>0){
					for(PrpLClaimVo vo:claimVos){
							PrpLRegistVo registVo=registQueryService.findByRegistNo(vo.getRegistNo());
							HistoricalClaimVo hisclaimVo=new HistoricalClaimVo();
							if(vo.getDamageTime()!=null){
								hisclaimVo.setClaimDate(this.DateFormatString(vo.getDamageTime()));//????????????
							}
							//hisclaimVo.setClaimEndDate(this.DateFormatString(vo.getEndCaseTime()));//????????????
							hisclaimVo.setClaimNumber(vo.getRegistNo());//?????????
							hisclaimVo.setDriverName(registVo.getDriverName());//?????????
							hisclaimVo.setEventAddress(registVo.getDamageAddress());//????????????
							hisclaimVo.setInsuranceCategory("11");//??????
							hisclaimVo.setInsuranceNumber(vo.getPolicyNo());//????????????
							//hisclaimVo.setPaidAmount(vo.getSumPaid()+"");//??????????????????
							//hisclaimVo.setPaidTimes("");//????????????
							if(vo.getReportTime()!=null){
								hisclaimVo.setReportDate(this.DateFormatString(vo.getReportTime()));//????????????
							}
							hisclaimVo.setReporter(registVo.getReportorName());//?????????
							hisclaimVo.setReporterTel(registVo.getReportorPhone());//???????????????
							hisclaimVo.setStatus("");//????????????
							cIhisClaimVos.add(hisclaimVo);
						}
						
					}
				}
		
		//?????????????????????
		List<PrppheadVo> vos = new ArrayList<PrppheadVo>();
		vos = registQueryService.findByPolicyNo(biVo.getPolicyNo());
		int i=0;
		for(PrppheadVo vo:vos){
			i++;
			InsuranceModificationVo cationVo=new InsuranceModificationVo();
			List<PrpptextVo> prppTextVoList =registQueryService.findPrppTextByPolicyNo(vo.getEndorseNo());
			String endorContent = "";
			for(PrpptextVo prpptextVo :prppTextVoList){
				if(StringUtils.isNotBlank(prpptextVo.getEndorseText())){
					endorContent = endorContent+prpptextVo.getEndorseText();
				}
			}
			vo.setEndNoContext(endorContent);//????????????
			if(StringUtils.isNotBlank(endorContent)){
				cationVo.setContent(vo.getEndNoContext());//????????????
			}else{
				cationVo.setContent("????????????");//????????????
			}
			cationVo.setInsuranceNumber(vo.getPolicyNo());//?????????
			cationVo.setModificationBillNo(vo.getEndorseNo());//?????????
			if(vo.getEndorDate()!=null){
				cationVo.setMofificationDate(this.DateFormatString(vo.getEndorDate()));//????????????
			}
			
			cationVo.setOrder(i+"");//??????
			bImodificationVos.add(cationVo);
			
		}
		
		//?????????????????????
		List<PrppheadVo> vo1s = new ArrayList<PrppheadVo>();
		vo1s = registQueryService.findByPolicyNo(ciVo.getPolicyNo());
		int j=0;
		for(PrppheadVo vo:vo1s){
	       j++;
		   InsuranceModificationVo cationVo=new InsuranceModificationVo();
		   List<PrpptextVo> prppTextVoList =registQueryService.findPrppTextByPolicyNo(vo.getEndorseNo());
		   String endorContent = "";
					for(PrpptextVo prpptextVo :prppTextVoList){
						if(StringUtils.isNotBlank(prpptextVo.getEndorseText())){
							endorContent = endorContent+prpptextVo.getEndorseText();
						}
					}
					vo.setEndNoContext(endorContent);//????????????
					if(StringUtils.isNotBlank(endorContent)){
						cationVo.setContent(vo.getEndNoContext());//????????????
					}else{
						cationVo.setContent("????????????");//????????????
					}
					cationVo.setInsuranceNumber(vo.getPolicyNo());//?????????
					cationVo.setModificationBillNo(vo.getEndorseNo());//?????????
					if(vo.getEndorDate()!=null){
						cationVo.setMofificationDate(this.DateFormatString(vo.getEndorDate()));//????????????
					}
					
					cationVo.setOrder(j+"");//??????
					cImodificationVos.add(cationVo);
					
				}
		
		//???????????????????????????
		String  promiseStr="";
		 List<PrpLCengageVo> prpCengageVos=biVo.getPrpCengages();
		 if(prpCengageVos!=null && prpCengageVos.size()>0){
			 for(PrpLCengageVo vo:prpCengageVos){
				 if(!"1101".equals(vo.getRiskCode())){
					 promiseStr=promiseStr+vo.getClauses();
				 }
			 }
			 if(StringUtils.isNotEmpty(promiseStr)){
		    	 promiseStr=promiseStr+"(????????????);";
		     }
			SpecialAgreementVo agreementVo=new SpecialAgreementVo();
			agreementVo.setContent(promiseStr);//????????????????????????
			agreementVo.setOrder("1");
			bIAgreementVos.add(agreementVo);
			
		 }
		 
		//???????????????????????????
		String  promiseStr1="";
			 List<PrpLCengageVo> prpCengageVo1s=ciVo.getPrpCengages();
			 if(prpCengageVo1s!=null && prpCengageVo1s.size()>0){
				 for(PrpLCengageVo vo:prpCengageVo1s){
					 if("1101".equals(vo.getRiskCode())){
						 promiseStr1=promiseStr1+vo.getClauses();
					 }
				 }
				 if(StringUtils.isNotEmpty(promiseStr)){
			    	 promiseStr1=promiseStr1+"(????????????);";
			     }
				SpecialAgreementVo agreementVo=new SpecialAgreementVo();
				agreementVo.setContent(promiseStr1);//????????????????????????
				agreementVo.setOrder("1");
				cIAgreementVos.add(agreementVo);
				
			 }
		//(car Draiver carInfo) ?????????Id ???
	    PrpLCheckCarInfoVo chekCarInfoVo=new PrpLCheckCarInfoVo();
		if(carInfoList!=null && carInfoList.size()>0){
			for(PrpLCheckCarInfoVo vo:carInfoList){
				if(vo.getLicenseNo().equals(licenseNo)){
					chekCarInfoVo=vo;
					prpLCheckDriverVo=checkTaskService.findPrpLCheckDriverVoById(vo.getCarid());
					carVo=checkTaskService.findPrpLCheckCarVoById(vo.getCarid());
					prplcheckdutyVo=checkTaskService.findCheckDuty(registNo,carVo.getSerialNo());
					break;
				}
			}
		}
		
		//?????????????????????
		CheckVo checkVo=new CheckVo();
		checkVo.setCheckAddress(prpLCheckTaskVo.getCheckAddress());//????????????
		if(prpLCheckTaskVo.getCheckDate()!=null){
			checkVo.setCheckDate(this.DateFormatString(prpLCheckTaskVo.getCheckDate()));//????????????
		}
		
		checkVo.setCheckEmployee1Code(prpLCheckTaskVo.getCheckerCode());//???????????????1
		checkVo.setCheckEmployee1Name(prpLCheckTaskVo.getChecker());//?????????1
		checkVo.setCheckEmployee2Code("");//???????????????2
		checkVo.setCheckEmployee2Name("");//?????????2
		checkVo.setCheckopinion(prpLCheckTaskVo.getContexts());//????????????
		checkVo.setCheckType(prpLCheckVo.getCheckType());//????????????
		checkVo.setClaimType("0");//????????????
		checkVo.setContactPerson(prpLCheckTaskVo.getLinkerName());//?????????
		checkVo.setContactTel(prpLCheckTaskVo.getLinkerMobile());//????????????
		checkVo.setDisposeType(prpLCheckVo.getManageType());//??????????????????
		DriverVo driverVo1=new DriverVo();//???????????????
		driverVo1.setAddress(prpLCheckDriverVo.getUnitAddress());//??????????????????????????????
		if(prpLCheckDriverVo.getDriverAge()!=null){
			driverVo1.setDriverAge(prpLCheckDriverVo.getDriverAge().toString());//??????
		}
		driverVo1.setDriverAllowedVehicleType(prpLCheckDriverVo.getIdentifyType());//????????????
		driverVo1.setDriverEducation("");//????????????
		driverVo1.setDriverGender(prpLCheckDriverVo.getDriverSex());//???????????????
		driverVo1.setDriverLicenseInstitution("");//????????????
		driverVo1.setDriverTel(prpLCheckDriverVo.getLinkPhoneNumber());//????????????
		if(prpLCheckDriverVo.getAcceptLicenseDate()!=null){
			driverVo1.setDrivingLicenseDate(this.DateFormatString(prpLCheckDriverVo.getAcceptLicenseDate()));//??????????????????
		}
		
		driverVo1.setDrivingLicenseNum(prpLCheckDriverVo.getDrivingLicenseNo());//???????????????
		
		
		driverVo1.setName(prpLCheckDriverVo.getDriverName());//???????????????	
		
		
		checkVo.setDriverVo(driverVo1);
		checkVo.setEventAddressType("1");//????????????
		checkVo.setEventReason(prpLCheckVo.getDamageCode());//????????????
		if(StringUtils.isNotBlank(prpLCheckVo.getAccidentReason())){
			checkVo.setEventReasonType(prpLCheckVo.getAccidentReason());//????????????
		}else{
			checkVo.setEventReasonType("01");//??????
		}
		
		checkVo.setEventResponsibility(prplcheckdutyVo.getIndemnityDuty());//????????????
		checkVo.setEventType(prpLCheckVo.getDamageTypeCode());//??????????????????
		checkVo.setEventCategory("");//????????????
		checkVo.setIsFirstScene("true");//??????????????????
		checkVo.setLossType(prpLCheckVo.getLossType());//????????????
		
		//????????????????????????
		CommercialInsuranceVo bIInfoVo=new CommercialInsuranceVo();
		bIInfoVo.setAgreeDriverFlag("true");//?????????????????????
		if(biVo.getUnderWriteEndDate()!=null){
		 bIInfoVo.setBuyInsuranceDate(this.DateFormatString(biVo.getUnderWriteEndDate()));//????????????
		}
		
		bIInfoVo.setHistoricalClaims(bIhisClaimVos);//???????????????????????????
		if(biVo.getSumAmount()!=null){
			bIInfoVo.setInsuranceAmount(biVo.getSumAmount()+"");//?????????	
		}
		
		bIInfoVo.setInsuranceCategory("12");//??????	
		
		
		if(biVo.getEndDate()!=null){
			bIInfoVo.setInsuranceEndDate(this.DateFormatString(biVo.getEndDate()));//????????????
		}
		if(biVo.getSumPremium()!=null){
			bIInfoVo.setInsuranceFee(biVo.getSumPremium()+"");//?????????
		}
		
		bIInfoVo.setInsuranceItems(biItemVos);//????????????
		bIInfoVo.setInsuranceModifications(bImodificationVos);//??????????????????
		bIInfoVo.setInsuranceNumber(biVo.getPolicyNo());//?????????
		if(biVo.getStartDate()!=null){
			bIInfoVo.setInsuranceStartDate(this.DateFormatString(biVo.getStartDate()));//????????????
		}
		if(biVo.getOperateDate()!=null){
			bIInfoVo.setIssueDate(this.DateFormatString(biVo.getOperateDate()));//????????????
		}
		
		bIInfoVo.setPolicyHolder(biVo.getAppliName());//?????????
		bIInfoVo.setSignAddress(biVo.getOperateSite());//????????????
		if(biVo.getSignDate()!=null){
			bIInfoVo.setSignDate(this.DateFormatString(biVo.getSignDate()));//????????????	
		}
		
		bIInfoVo.setSpecialAgreements(bIAgreementVos);//????????????????????????
		bIInfoVo.setTheInsuredName(biVo.getInsuredName());//????????????
		
		//????????????????????????
		CompulsoryInsuranceVo cIInfoVo=new CompulsoryInsuranceVo();
		cIInfoVo.setAgreeDriverFlag("true");//?????????????????????
		if(ciVo.getUnderWriteEndDate()!=null){
			cIInfoVo.setBuyInsuranceDate(this.DateFormatString(ciVo.getUnderWriteEndDate()));//????????????
		}
		
		cIInfoVo.setHistoricalClaims(cIhisClaimVos);//???????????????????????????
		if(ciVo.getSumAmount()!=null){
			cIInfoVo.setInsuranceAmount(ciVo.getSumAmount()+"");//?????????
		}
		
		cIInfoVo.setInsuranceCategory("11");//??????
		if(ciVo.getEndDate()!=null){
			cIInfoVo.setInsuranceEndDate(this.DateFormatString(ciVo.getEndDate()));//????????????
		}
		if(ciVo.getSumPremium()!=null){
			cIInfoVo.setInsuranceFee(ciVo.getSumPremium()+"");//?????????	
		}
		
		cIInfoVo.setInsuranceItems(ciItemVos);//????????????
		cIInfoVo.setInsuranceModifications(cImodificationVos);//??????????????????
		cIInfoVo.setInsuranceNumber(ciVo.getPolicyNo());//?????????
		if(ciVo.getStartDate()!=null){
			cIInfoVo.setInsuranceStartDate(this.DateFormatString(ciVo.getStartDate()));//????????????	
		}
		if(ciVo.getOperateDate()!=null){
			cIInfoVo.setIssueDate(this.DateFormatString(ciVo.getOperateDate()));//????????????
		}
		
		cIInfoVo.setPolicyHolder(ciVo.getAppliName());//?????????
		cIInfoVo.setSignAddress(ciVo.getOperateSite());//????????????
		if(ciVo.getSignDate()!=null){
			cIInfoVo.setSignDate(this.DateFormatString(ciVo.getSignDate()));//????????????
		}
		
		cIInfoVo.setSpecialAgreements(cIAgreementVos);//????????????????????????
		cIInfoVo.setTheInsuredName(ciVo.getInsuredName());//????????????
		
		//??????????????????
		PrpLClaimTextVo textVo=new PrpLClaimTextVo();
		PrpLClaimTextVo  textVo1=claimTextService.findClaimTextByLossCarMainIdAndNodeCode(carMainVo.getId(),"DLCar");
		if(textVo1!=null){
			textVo=textVo1;
		}
		ConfirmLossDiscussionVo cussionVo=new ConfirmLossDiscussionVo();//??????????????????
		if(textVo!=null && textVo.getId()!=null){
			if(textVo.getSumLossFee()!=null){
				cussionVo.setAmount(textVo.getSumLossFee()+"");//?????????
			}
			
			cussionVo.setComment(textVo.getDescription());//????????????
			cussionVo.setCompanyname(codeTranService.transCode("ComCode",textVo.getComCode()));//???????????????
			if(textVo.getInputTime()!=null){
				cussionVo.setDate(this.DateFormatString(textVo.getInputTime()));//??????????????????
			}
			
			cussionVo.setPersonName(textVo.getOperatorName());//???????????????
			cussionVo.setStatus("");//????????????
		}else{
			//cussionVo.setAmount("");//?????????
		    cussionVo.setComment("????????????");//????????????
			cussionVo.setCompanyname(codeTranService.transCode("ComCode",carMainVo.getComCode()));//???????????????
			if(carMainVo.getDeflossDate()!=null){
			 cussionVo.setDate(this.DateFormatString(carMainVo.getDeflossDate()));//??????????????????
			}
			
			cussionVo.setPersonName(carMainVo.getHandlerName());//???????????????
			cussionVo.setStatus("");//????????????
		}
		DiscussionVos.add(cussionVo);
		
		//?????????????????????
		List<PrpLDlossCarRepairVo> carRepairVos=carMainVo.getPrpLDlossCarRepairs();
		if(carRepairVos!=null && carRepairVos.size()>0){
			for(PrpLDlossCarRepairVo vo:carRepairVos){
				LaborVo laborVo=new LaborVo();
				laborVo.setCode(vo.getRepairId());//??????????????????????????????
				if(vo.getManHourUnitPrice()!=null){
					laborVo.setPrice(vo.getManHourUnitPrice()+"");//??????	
				}
				if(vo.getManHour()!=null){
					laborVo.setCount(vo.getManHour()+"");//??????
				}
				
				laborVo.setDescription(vo.getRepairName());//????????????
				laborVo.setLaborType(vo.getRepairCode());//????????????
				laborVo.setRemark("");//??????
				//laborVo.setHandAddFlag("");//??????????????????
				if(StringUtils.isNotBlank(carMainVo.getLossFeeType())){
					if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(carMainVo.getRiskCode()) != null &&
							CodeConstants.ISNEWCLAUSECODE2020_MAP.get(carMainVo.getRiskCode())){
						laborVo.setInsuranceItem(CodeConstants.LossFee2020Kind_Map.get(carMainVo.getRiskCode()+carMainVo.getLossFeeType()));//????????????
					}else{
						laborVo.setInsuranceItem(CodeConstants.LossFeeKind_Map.get(carMainVo.getLossFeeType()));//????????????
					}
					
				}else{
					laborVo.setInsuranceItem("A");
				}
				
				laborVo.setPartName(vo.getCompName());//?????????????????????
				laborVos.add(laborVo);
				}
		}
		
		//?????????????????????
		List<PrpLDlossCarMaterialVo> carMaterialVos= carMainVo.getPrpLDlossCarMaterials();
		if(carMaterialVos!=null && carMaterialVos.size()>0){
			for(PrpLDlossCarMaterialVo vo:carMaterialVos){
				SmallSparepartVo sparepartVo=new SmallSparepartVo();
				sparepartVo.setCode(vo.getAssistId());//??????????????????????????????
				if(vo.getAssisCount()!=null){
					sparepartVo.setCount(vo.getAssisCount()+"");//??????
				}
				
				sparepartVo.setDescription(vo.getMaterialName());//??????
				//sparepartVo.setHandAddFlag("");//??????????????????
				if(StringUtils.isNotBlank(carMainVo.getLossFeeType())){
					if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(carMainVo.getRiskCode()) != null &&
							CodeConstants.ISNEWCLAUSECODE2020_MAP.get(carMainVo.getRiskCode())){
						sparepartVo.setInsuranceItem(CodeConstants.LossFee2020Kind_Map.get(carMainVo.getRiskCode()+carMainVo.getLossFeeType()));//??????????????????
					}else{
						sparepartVo.setInsuranceItem(CodeConstants.LossFeeKind_Map.get(carMainVo.getLossFeeType()));//??????????????????
					}
					
				}else{
					sparepartVo.setInsuranceItem("A");
				}
				
				if(vo.getUnitPrice()!=null){
					sparepartVo.setPrice(vo.getUnitPrice()+"");//??????	
				}
				
				sparepartVo.setRemark("");//??????
				sparepartVos.add(sparepartVo);
			}
		}
		
		//?????????????????????
		List<PrpLDlossCarCompVo> carCompVos=carMainVo.getPrpLDlossCarComps();
		
		if(carCompVos!=null && carCompVos.size()>0){
			for(PrpLDlossCarCompVo vo:carCompVos){
				SparepartVo repartVo=new SparepartVo(); 
				repartVo.setCode(vo.getIndId());//??????????????????????????????
				if(vo.getQuantity()!=null){
					repartVo.setCount(vo.getQuantity()+"");//??????
				}
				if(vo.getMaterialFee()!=null){
					repartVo.setPrice(vo.getMaterialFee()+"");//??????
				}
				
				repartVo.setDescription(vo.getCompName());//??????
				//repartVo.setDiscount("");//??????
				//repartVo.setHandAddFlag("");//??????????????????
				if(StringUtils.isNotBlank(carMainVo.getLossFeeType())){
					if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(carMainVo.getRiskCode()) != null &&
							CodeConstants.ISNEWCLAUSECODE2020_MAP.get(carMainVo.getRiskCode())){
						repartVo.setInsuranceItem(CodeConstants.LossFee2020Kind_Map.get(carMainVo.getRiskCode()+carMainVo.getLossFeeType()));//????????????
					}else{
						repartVo.setInsuranceItem(CodeConstants.LossFeeKind_Map.get(carMainVo.getLossFeeType()));//????????????
					}
					
				}else{
					repartVo.setInsuranceItem("A");
				}
				
				//repartVo.setManagementFee("");//?????????
				repartVo.setOriginalPriceType("");//??????????????????
				repartVo.setRemark("");//??????
				if(vo.getRestFee()!=null){
					repartVo.setRestValue(vo.getRestFee()+"");//??????
				}
				
				repartVo.setOem(vo.getOriginalId());//?????????????????????OEM??????
				repartVos.add(repartVo);
			}
		}
		
		//?????????????????????
		ConfirmLossVo confirmLossVo=new ConfirmLossVo();
		confirmLossVo.setConfirmLossAddress(prpLCheckTaskVo.getCheckAddress());//????????????
		confirmLossVo.setConfirmLossCompany(codeTranService.transCode("ComCode",carMainVo.getComCode()));//??????????????????
		confirmLossVo.setConfirmLossCompanyCode(carMainVo.getComCode());//??????????????????
		if(carMainVo.getDeflossDate()!=null){
			confirmLossVo.setConfirmLossDate(this.DateFormatString(new Date()));//????????????
		}
		
		confirmLossVo.setConfirmLossDiscussions(DiscussionVos);//???????????????
		confirmLossVo.setConfirmLossEmployee(carMainVo.getHandlerName());//?????????
		confirmLossVo.setConfirmLossEmployeeCode(carMainVo.getHandlerCode());//???????????????
		if("1".equals(carMainVo.getDeflossCarType())){
			if(StringUtils.isNotBlank(carMainVo.getLossFeeType())){
				confirmLossVo.setConfirmLossInsuranceItem(CodeConstants.LossFeeKind_Map.get(carMainVo.getLossFeeType()));//????????????
			}else{
				confirmLossVo.setConfirmLossInsuranceItem("A");
			}
		}else{
			    confirmLossVo.setConfirmLossInsuranceItem("B");
		}
		
		
		confirmLossVo.setConfirmOpinion(textVo.getDescription());//????????????
		confirmLossVo.setEvaluationType(carMainVo.getCetainLossType());//????????????
		//confirmLossVo.setIsPayBackForOthers("");//??????????????????
		confirmLossVo.setLabors(laborVos);//???????????? 
		confirmLossVo.setLossType(carMainVo.getIsTotalLoss());//????????????
		PrpLRepairFactoryVo repairVo=null;
		if(carMainVo!=null && StringUtils.isNotBlank(carMainVo.getRepairFactoryCode())){
			 repairVo=repairFactoryService.findFactoryById(carMainVo.getRepairFactoryCode());
			if(repairVo==null){//????????????????????????
			 repairVo=repairFactoryService.findFactoryByCode(carMainVo.getRepairFactoryCode(), carMainVo.getRepairFactoryType());
			}
		   
		}
		if(repairVo!=null){
			confirmLossVo.setRepairFactoryCode(repairVo.getFactoryCode());//???????????????
		}else{
			confirmLossVo.setRepairFactoryCode(carMainVo.getRepairFactoryCode());
		}
		confirmLossVo.setRepairFactoryName(carMainVo.getRepairFactoryName());//???????????????
		confirmLossVo.setRepairFactoryType(carMainVo.getRepairFactoryType());//???????????????
		confirmLossVo.setSmallSpareparts(sparepartVos);//????????????
		confirmLossVo.setSpareparts(repartVos);//????????????
		if(carMainVo.getSumRescueFee()!=null){
			confirmLossVo.setRescueFee(carMainVo.getSumRescueFee()+"");//?????????
		}
		
		
		//?????????????????????
		ReportVo reportVo=new ReportVo();//????????????
		List<DataVo> dataVos=new ArrayList<DataVo>();//???????????????
		if(prpLRegistVo!=null && StringUtils.isNotBlank(prpLRegistVo.getRegistNo())){
			reportVo.setContactPerson(prpLRegistVo.getLinkerName());//?????????
			reportVo.setContactTel(prpLRegistVo.getLinkerMobile());//???????????????
			DriverVo driverVo2=new DriverVo();//???????????????????????????
			driverVo2.setAddress(prpLCheckDriverVo.getUnitAddress());//?????????????????????
		    if(prpLCheckDriverVo.getDriverAge()!=null){
		    	driverVo2.setDriverAge(prpLCheckDriverVo.getDriverAge().toString());//??????
			}
		    driverVo2.setDriverAllowedVehicleType(prpLCheckDriverVo.getIdentifyType());//????????????
		    driverVo2.setDriverEducation("");//????????????
		    driverVo2.setDriverGender(prpLCheckDriverVo.getDriverSex());//???????????????
		    driverVo2.setDriverLicenseInstitution("");//????????????
		    driverVo2.setDriverTel(prpLCheckDriverVo.getLinkPhoneNumber());//????????????
		    if(prpLCheckDriverVo.getAcceptLicenseDate()!=null){
		    	 driverVo2.setDrivingLicenseDate(this.DateFormatString(prpLCheckDriverVo.getAcceptLicenseDate()));//??????????????????
		    }
		
			   driverVo2.setDrivingLicenseNum(prpLCheckDriverVo.getDrivingLicenseNo());//??????????????? 
		  
		 
			   driverVo2.setName(prpLCheckDriverVo.getDriverName());//???????????????
		 
		   
		    reportVo.setDriverVo(driverVo2);
		    reportVo.setEventAddress(prpLRegistVo.getDamageAddress());//????????????
		    reportVo.setEventAddressType("1");//????????????
		    if(prpLRegistVo.getDamageTime()!=null){
		    	 reportVo.setEventDate(this.DateFormatString(prpLRegistVo.getDamageTime()));//????????????
		    }
		   
		    reportVo.setEventDescription(registExtVo.getDangerRemark());//????????????
		    reportVo.setEventReason(prpLRegistVo.getDamageCode());//??????????????????
		    if(StringUtils.isNotBlank(prpLRegistVo.getAccidentReason())){
		    	 reportVo.setEventReasonType(prpLRegistVo.getAccidentReason());//????????????
		    }else{
		    	 reportVo.setEventReasonType("01");//??????
		    }
		    reportVo.setEventResponsibility(prplcheckdutyVo.getIndemnityDuty());//????????????
		    reportVo.setEventType(prpLCheckVo.getDamageTypeCode());//????????????
		    reportVo.setInformant(prpLRegistVo.getReportorName());//?????????
		    //reportVo.setInformantIsDriver("");//????????????????????????
		   // reportVo.setInformantIsInsured("");//???????????????????????????
		    reportVo.setInformantTel(prpLRegistVo.getReportorPhone());//???????????????
		    reportVo.setIsClosed("false");//????????????
		    if("1".equals(registExtVo.getIsClaimSelf())){
		    	reportVo.setIsMutualCollisionSelfCompensation("true");//??????????????????
		    }else{
		    	reportVo.setIsMutualCollisionSelfCompensation("false");//??????????????????
		    }
		    if("1".equals(registExtVo.getIsOnSitReport())){
		    	reportVo.setIsScene("true");//??????????????????
		    }else{
		    	reportVo.setIsScene("false");
		    }
		    //reportVo.setIsSimpleClaim("");//??????????????????
		    reportVo.setMercyFlag(prpLRegistVo.getMercyFlag());//??????????????????
		    if(prpLRegistVo.getReportTime()!=null){
		    	 reportVo.setReportDate(this.DateFormatString(prpLRegistVo.getReportTime()));//????????????	
		    }
		    //????????????????????????????????????????????????1-?????????2-?????????3-??????
		    DataVo dataVo=new DataVo();
		    dataVo.setKey("Stage");
		    dataVo.setValue(nodeFlag);
		    dataVos.add(dataVo);
		    reportVo.setDatas(dataVos);
		   
		}
		
		//???????????????????????????
		PrpLCItemCarVo prpLCItemCarVo1=new PrpLCItemCarVo();
		VehicleVo vehicleVo1=new VehicleVo();
		
		if(ciVo!=null && ciVo.getId()!=null){
			List<PrpLCItemCarVo> itemCarVos=ciVo.getPrpCItemCars();
			if(itemCarVos!=null && itemCarVos.size()>0){
				for(PrpLCItemCarVo vo:itemCarVos){		
					if(licenseNo.equals(vo.getLicenseNo())){
						prpLCItemCarVo1=vo;
						break;
					}
				}
			}
				
		}
		if(prpLCItemCarVo1.getItemCarId()==null){
			if(biVo!=null && biVo.getId()!=null){
				List<PrpLCItemCarVo> itemCarVos=biVo.getPrpCItemCars();
				if(itemCarVos!=null && itemCarVos.size()>0){
					for(PrpLCItemCarVo vo:itemCarVos){		
						if(licenseNo.equals(vo.getLicenseNo())){
							prpLCItemCarVo1=vo;
							break;
						}
					}
				}
			}
		}
		//vehicleVo1.setApprovedCapacity("");//???????????????
		//vehicleVo1.setApprovedPassenger("");//????????????
		vehicleVo1.setBrand("");//????????????
		///vehicleVo1.setCarAge("");//??????
		//vehicleVo1.setDisplacement("");//??????
		vehicleVo1.setDrivingArea("");//????????????
		vehicleVo1.setEngineNumber(prpLCItemCarVo1.getEngineNo());//????????????
		if(prpLCItemCarVo1.getEnrollDate()!=null){
			vehicleVo1.setFirstRegistrationDate(this.DateFormatString(prpLCItemCarVo1.getEnrollDate()));//????????????
		}
		
		vehicleVo1.setGasType("");//????????????
		vehicleVo1.setInsuredFlag("");//????????????
		vehicleVo1.setInsurer(prpLCItemCarVo1.getInsurerCode());//????????????
		//vehicleVo1.setIsImported("");//???????????????
	    vehicleVo1.setIsMainCar("true");//???????????????
		vehicleVo1.setManufacturer("");//???????????????
		//vehicleVo1.setMileage("");//???????????????
		vehicleVo1.setModel(prpLCItemCarVo1.getBrandName());//????????????
		if(prpLCItemCarVo1.getPurchasePrice()!=null){
			vehicleVo1.setNewCarAmount(prpLCItemCarVo1.getPurchasePrice()+"");//???????????????
		}
		
		vehicleVo1.setOwner(prpLCItemCarVo1.getCarOwner());//??????
		
		vehicleVo1.setRegistrationNumber(prpLCItemCarVo1.getLicenseNo());//?????????
		
		
		//vehicleVo1.setSeatCount("");//?????????
		vehicleVo1.setTypeOfUsage(prpLCItemCarVo1.getUseKindCode());//????????????
		vehicleVo1.setVehicleID(prpLCItemCarVo1.getItemCarId()+"");//??????Id
		if(prpLCItemCarVo1.getActualValue()!=null){
			vehicleVo1.setVehiclePrice(prpLCItemCarVo1.getActualValue()+"");//??????????????????
		}
		if(StringUtils.isNotBlank(lossCarMainId)){
			if(carMainVo.getDeflossCarType().equals("1")){
				PrpLCItemCarVo ciItemCarVo = registQueryService.findCItemCarByRegistNo(registNo);
				String platformCarKind = null;
				if("011".equals(ciItemCarVo.getCarType()) || "016".equals(ciItemCarVo.getCarType())){
					platformCarKind = CodeConvertTool.getVehicleCategory(ciItemCarVo.getCarType(),
							ciItemCarVo.getExhaustScale(),ciItemCarVo.getTonCount());
					if(StringUtils.isBlank(platformCarKind)){
						platformCarKind = CodeTranUtil.findTransCodeDictVo("VehicleTypeShow",ciItemCarVo.getCarType()).getProperty1();
					}
					vehicleVo1.setVehicleType(platformCarKind);
				} else{
					vehicleVo1.setVehicleType(CodeTranUtil.findTransCodeDictVo("VehicleTypeShow",ciItemCarVo.getCarType()).getProperty1());
				}
			} else{
				vehicleVo1.setVehicleType(lossCarInfoVo.getPlatformCarKindCode());//????????????
			}
		}
		vehicleVo1.setVin(prpLCItemCarVo1.getFrameNo());//vin???
		vehicleVo1.setNotificationNumber(lossCarInfoVo.getModelCode());
		//vehicleVo1.setYearOfProduction(this.DateFormatString(prpLCItemCarVo1.getMakeDate()));//????????????
		
		
		//????????????????????????
		RelatingVehicleVo relatingVehicleVo=new RelatingVehicleVo();
		VehicleVo vehicleVo3=new VehicleVo();//????????????
		//vehicleVo3.setApprovedCapacity("");//???????????????
		//vehicleVo3.setApprovedPassenger("");//????????????
		vehicleVo3.setBrand("");//????????????
		//vehicleVo3.setCarAge("");//??????
		//vehicleVo3.setDisplacement("");//??????
		vehicleVo3.setDrivingArea("");//????????????
		vehicleVo3.setEngineNumber(lossCarInfoVo.getEngineNo());//????????????
		if(lossCarInfoVo.getEnrollDate()!=null){
			vehicleVo3.setFirstRegistrationDate(this.DateFormatString(lossCarInfoVo.getEnrollDate()));//????????????
		}
		
		vehicleVo3.setGasType("");//????????????
		vehicleVo3.setInsuredFlag("");//????????????
		vehicleVo3.setInsurer(lossCarInfoVo.getInsureComName());//????????????
		//vehicleVo3.setIsImported("");//???????????????
		vehicleVo3.setIsMainCar("false");//???????????????
		vehicleVo3.setManufacturer("");//???????????????
		//vehicleVo3.setMileage("");//???????????????
       if("3".equals(nodeFlag)){
    	   vehicleVo3.setModel(lossCarInfoVo.getBrandName());//????????????
		}else{
			vehicleVo3.setModel(prpLRegistCarLossVo.getBrandName());//????????????
		}
		
		//vehicleVo3.setNewCarAmount("");//???????????????
		vehicleVo3.setOwner(lossCarInfoVo.getCarOwner());//??????
		if("3".equals(nodeFlag)){
			vehicleVo3.setRegistrationNumber(lossCarInfoVo.getLicenseNo());//?????????
		}else{
			vehicleVo3.setRegistrationNumber(prpLRegistCarLossVo.getLicenseNo());//?????????
		}
		
		
		
		//vehicleVo3.setSeatCount("");//?????????
		vehicleVo3.setTypeOfUsage("");//????????????
		vehicleVo3.setVehicleID(lossCarInfoVo.getId()+"");//??????Id
		//vehicleVo3.setVehiclePrice("");//??????????????????
		if("2".equals(nodeFlag)){
			vehicleVo3.setVehicleType(chekCarInfoVo.getPlatformCarKindCode());//????????????
		}else{
			if(StringUtils.isNotBlank(lossCarMainId)){
				if(carMainVo.getDeflossCarType().equals("1")){
					PrpLCItemCarVo ciItemCarVo = registQueryService.findCItemCarByRegistNo(registNo);
					String platformCarKind = null;
					if("011".equals(ciItemCarVo.getCarType()) || "016".equals(ciItemCarVo.getCarType())){
						platformCarKind = CodeConvertTool.getVehicleCategory(ciItemCarVo.getCarType(),
								ciItemCarVo.getExhaustScale(),ciItemCarVo.getTonCount());
						if(StringUtils.isBlank(platformCarKind)){
							platformCarKind = CodeTranUtil.findTransCodeDictVo("VehicleTypeShow",ciItemCarVo.getCarType()).getProperty1();
						}
						vehicleVo3.setVehicleType(platformCarKind);
					} else{
						vehicleVo3.setVehicleType(CodeTranUtil.findTransCodeDictVo("VehicleTypeShow",ciItemCarVo.getCarType()).getProperty1());
					}
				} else{
					vehicleVo3.setVehicleType(lossCarInfoVo.getPlatformCarKindCode());//????????????
				}
			}
		}
		if("2".equals(nodeFlag)){
			vehicleVo3.setVin(chekCarInfoVo.getVinNo());//vin???
		}else{
			vehicleVo3.setVin(lossCarInfoVo.getVinNo());//vin???
		}
		if("2".equals(nodeFlag)){
			vehicleVo3.setNotificationNumber(chekCarInfoVo.getModelCode());
		}else{
			vehicleVo3.setNotificationNumber(lossCarInfoVo.getModelCode());
		}
		
		//vehicleVo3.setYearOfProduction("");//????????????
		
		relatingVehicleVo.setConfirmLossVo(confirmLossVo);//?????????????????????
		relatingVehicleVo.setVehicleVo(vehicleVo3);//?????????????????????
		relatingVehicles.add(relatingVehicleVo);
		
		String lossCarType="1";
		lossCarType=prpLRegistCarLossVo.getLossparty();
		List<PrpLScheduleDefLossVo> scheduleLists=scheduleService.findPrpLScheduleCarLossList(registNo);
		PrpLScheduleDefLossVo scheduleVo=new PrpLScheduleDefLossVo();
		if(StringUtils.isBlank(lossCarType) && scheduleLists!=null && scheduleLists.size()>0){
			for(PrpLScheduleDefLossVo vo:scheduleLists){
				if(licenseNo.equals(vo.getLicenseNo())){
					scheduleVo=vo;
					if(scheduleVo.getSerialNo()!=null){
						lossCarType=scheduleVo.getSerialNo().toString();
					};
				}
			}
		}
			baseVo.setBranchCompanyCode(carMainVo.getComCode());
			baseVo.setBranchCompanyname(codeTranService.transCode("ComCode",carMainVo.getComCode()));
			if(!"1".equals(nodeFlag)){
				baseVo.setCheckVo(checkVo);//????????????
			}
			baseVo.setClaimNumber(registNo);//?????????
			if(biVo!=null && biVo.getId()!=null){//???????????????
			baseVo.setCommercialInsuranceVo(bIInfoVo);	
			}
			if(ciVo!=null && ciVo.getId()!=null){//???????????????
			baseVo.setCompulsoryInsuranceVo(cIInfoVo);	
			}
			if("1".equals(lossCarType) && "3".equals(nodeFlag)){
			baseVo.setConfirmLossVo(confirmLossVo);//???????????????????????????
			}
			baseVo.setInsuranceCompanyID("403");//?????????ID???????????????
			//baseVo.setIsPayBackForOthers("");//??????????????????
		
			baseVo.setRegistrationNumber(licenseNo);//?????????
			baseVo.setReportVo(reportVo);//????????????
			if("1".equals(lossCarType)){
			baseVo.setVehicleVo(vehicleVo1);//????????????
			}
			if("3".equals(lossCarType)){
			baseVo.setRelatingVehicles(relatingVehicles);//???????????????
			}
			
			
			
		
		
		
		return baseVo;
	}
	/**
	 * ??????????????????
	 * @param requestXML
	 * @param urlStr
	 * @param seconds
	 * @return
	 * @throws Exception
	 */
	private String requestControlExpert(String requestXML,String urlStr) throws Exception {
		long t1 = System.currentTimeMillis();
		String responseXml="";
		  StringBuffer buffer = new StringBuffer();    
		//??????????????????
		  String seconds = SpringProperties.getProperty("HTTP_TIMEOUT");
			if(StringUtils.isBlank(seconds)){
				seconds = "20";
			}
	        try {    
	         
	            URL url = new URL(urlStr);
	            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
	            httpUrlConn.setDoOutput(true);    
	            httpUrlConn.setDoInput(true);    
	            httpUrlConn.setUseCaches(false);    
	            // ?????????????????????GET/POST???    
	            httpUrlConn.setRequestMethod("POST"); 
	            httpUrlConn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
	            httpUrlConn.setConnectTimeout(Integer.valueOf(seconds) * 1000);
	            httpUrlConn.setReadTimeout(Integer.valueOf(seconds) * 1000);
		        
	            httpUrlConn.connect();    
	    
	            String outputStr =requestXML;
	            			
	            OutputStream outputStream = httpUrlConn.getOutputStream();  		        
	            // ???????????????????????????    
	            if (null != outputStr) {    
	                // ???????????????????????????????????????    outputStream.write
	                outputStream.write(outputStr.getBytes("utf-8"));    
	            }    
	    
	            // ???????????????????????????????????????    
	            InputStream inputStream = httpUrlConn.getInputStream();    
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");    
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);    
	    
	            String str = null;    
	            while ((str = bufferedReader.readLine()) != null) {
	            	
	                buffer.append(str);    
	            } 
	            if (buffer.length() < 1) {
					throw new Exception("??????????????????????????????");
				}
	            bufferedReader.close();    
	            inputStreamReader.close();    
	            // ????????????  
	            outputStream.flush();
	            outputStream.close();
	            inputStream.close();    
	            inputStream = null;    
	            httpUrlConn.disconnect(); 
	            responseXml=buffer.toString();
	            logger.info("????????????--------???"+responseXml);
	        } catch (ConnectException ce) {
	        	throw new Exception("?????????????????????????????????????????????", ce);
	        } catch (Exception e) {
	        	logger.error("????????????=========???",e);
	        	throw new Exception("????????????????????????????????????", e);
	        	
	        } finally {
				logger.warn("??????({})????????????{}ms", urlStr, System.currentTimeMillis() - t1);
			}    
	        return responseXml;
	}

	/**
	 * ????????????????????????
	 * @param reqObj
	 * @param resObj
	 * @param logVo
	 * @param flag
	 * @param errorMsg
	 */
	private  void logUtil(Object reqObj,Object resObj,ClaimInterfaceLogVo logVo,String flag,String errorMsg) {
	        try{
	            String reqXml = ClaimBaseCoder.objToXmlUtf(reqObj);
	            String resXml = ClaimBaseCoder.objToXmlUtf(resObj);
	            logger.info("===============????????????===========");
	            logger.info(reqXml);
	            logger.info(resXml);
	            logVo.setBusinessType(BusinessType.ControlExpert.name());
	            logVo.setBusinessName(BusinessType.ControlExpert.getName());
	            logVo.setRequestXml(reqXml);
	            logVo.setResponseXml(resXml);
	            logVo.setCreateTime(new Date());
	            logVo.setRequestTime(new Date());
	            if("00".equals(flag)){
	                logVo.setStatus("1");
	            }else{
	                logVo.setStatus("0");
	                logVo.setErrorCode(flag);
	                logVo.setErrorMessage(errorMsg);
	                
	            }
	            

	        }catch(Exception e1){
	            e1.printStackTrace();
	        }finally{
	            if(interfaceLogService == null){
	                interfaceLogService = (ClaimInterfaceLogService)Springs.getBean(ClaimInterfaceLogService.class);
	            }
	            interfaceLogService.save(logVo);
	        }
	 }
	/*
	 * ?????????????????????????????????   lossCarMainId--????????????????????????????????????????????????????????????????????????
	 */
	public void saveOrUpdateForPrplTestinfoMain(CECheckResultVo resultVo,SysUserVo userVo,String lossCarMainId,String nodeFlag) throws ParseException{
		//????????????????????????
		PrplTestinfoMainVo mainVo=new PrplTestinfoMainVo();
		//????????????
		List<PrplPartsInfoVo> partsList=new ArrayList<PrplPartsInfoVo>();
		//????????????
		List<PrplLaborInfoVo> laborsList=new ArrayList<PrplLaborInfoVo>();
		//??????????????????
		List<PrplFraudriskInfoVo> fraudRiskList=new ArrayList<PrplFraudriskInfoVo>();
		//???????????????
		List<PrplRiskpointInfoVo> riskPointList=new ArrayList<PrplRiskpointInfoVo>();
		//?????????????????????
		List<PrplOperationInfoVo> operationList=new ArrayList<PrplOperationInfoVo>();
		//????????????????????????????????????
		ClaimVo claimVo=new ClaimVo();
		//????????????Vo
		PriceSummaryVo summaryVo=new PriceSummaryVo();
		if(resultVo.getClaims()!=null && resultVo.getClaims().size()>0){
			 claimVo=resultVo.getClaims().get(0);
			 summaryVo=claimVo.getPriceSummaryVo();
		}
		
		
        //?????????????????????????????????
		mainVo.setRegistNo(resultVo.getClaimNumber());//?????????
		mainVo.setVehicleRegistionNumber(claimVo.getVehicleRegistionNumber());//?????????
		mainVo.setVehicleBrandName(claimVo.getVehicleBrandName());//????????????
		mainVo.setVehicleModelName(claimVo.getVehicleModelName());//????????????
		mainVo.setVin(claimVo.getVin());//VIN???
		mainVo.setCheckEmployee(claimVo.getCheckEmployee());//?????????
		mainVo.setConfirmlossEmployee(claimVo.getConfirmLossEmployee());//?????????
		mainVo.setRepairFactoryName(claimVo.getRepairFactoryName());//???????????????
		mainVo.setEventDate(this.StringFormatDate(claimVo.getEventDate()));//????????????
		mainVo.setConfirmlossDate(this.StringFormatDate(claimVo.getConfirmLossDate()));//????????????confirmLossDate
		mainVo.setCheckTime(this.StringFormatDate(claimVo.getCheckTime()));//??????????????????
		mainVo.setLossType(claimVo.getIsMainCar());//????????????1-?????????0-??????
		mainVo.setPartlossAmount(summaryVo.getPartTotalPrice());//?????????????????????
		mainVo.setPartceAmount(summaryVo.getcEPartTotalPrice());//?????????????????????
		mainVo.setPartsavingAmount(summaryVo.getSavingPartTotalPrice());//?????????????????????
		mainVo.setRiskSavingAmount(summaryVo.getFraudRiskSavingPrice());//????????????????????????
		mainVo.setLaborlossAmount(summaryVo.getLaborTotalPrice());//?????????????????????
		mainVo.setLaborceAmount(summaryVo.getcELaborTotalPrice());//?????????????????????
		mainVo.setLaborsavingAmount(summaryVo.getSavingLaborTotalPrice());//?????????????????????
		mainVo.setTotalPrice(summaryVo.getTotalPrice());//'??????????????????????????????????????????+?????????????????????';
		mainVo.setCetotalPrice(summaryVo.getcETotalPrice());//'CE?????????????????????CE?????????????????????+CE?????????????????????-????????????????????????'; 
		mainVo.setSavingTotalPrice(summaryVo.getSavingTotalPrice());//'??????????????????????????????????????????????????????+???????????????????????????+????????????????????????'; 
		mainVo.setLossCarMainId(lossCarMainId);//????????????Id
		mainVo.setComCode(userVo.getComCode());//????????????
		mainVo.setCreateTime(new Date());//???????????? 
		mainVo.setCreateUser(userVo.getUserCode());//????????????
		mainVo.setClaimResult(claimVo.getClaimResult());//????????????
		mainVo.setNodeFlag(nodeFlag);//???????????????1-?????????2-?????????3-??????
		//?????????????????????
		if(claimVo.getParts()!=null && claimVo.getParts().size()>0){
			for(PartVo vo:claimVo.getParts()){
				PrplPartsInfoVo infoVo=new PrplPartsInfoVo();
				infoVo.setPriceType(vo.getPriceType());//??????????????????
				infoVo.setPartsName(vo.getName());//????????????
				infoVo.setPartsPrice(vo.getPrice());//??????????????????
				infoVo.setPartsCount(vo.getCount());//??????????????????
				infoVo.setPartstotalPrice(vo.getTotalPrice());//??????????????????
				infoVo.setCePrice(vo.getcEPrice());//CE????????????
				infoVo.setCeCount(vo.getCount());//CE????????????
				infoVo.setCetotalPrice(vo.getcETotalPrice());//CE????????????
				infoVo.setSavingPrice(vo.getSavingPrice());//????????????
				if(vo.getSavingDescsVo()!=null){
					infoVo.setSavingDescs(vo.getSavingDescsVo().getSavingDesc());//??????????????????
				}
				infoVo.setCreateTime(new Date());//????????????
				infoVo.setCreateUser(userVo.getUserCode());//????????????
				partsList.add(infoVo);
				
			}
		}
		
		//?????????????????????
		if(claimVo.getLabors()!=null && claimVo.getLabors().size()>0){
			for(LaborResultVo vo:claimVo.getLabors()){
				PrplLaborInfoVo infoVo=new PrplLaborInfoVo();
				infoVo.setLaborItem(vo.getLaborItem());//????????????
				infoVo.setLaborName(vo.getName());//????????????
				infoVo.setLaborPrice(vo.getPrice());//????????????
				infoVo.setLaborCount(vo.getCount());//????????????
				infoVo.setLabortotalPrice(vo.getTotalPrice());//????????????
				infoVo.setCePrice(vo.getcEPrice());//CE????????????
				infoVo.setCeCount(vo.getCECount());//CE????????????
				infoVo.setCetotalPrice(vo.getcETotalPrice());//CE????????????
				if(vo.getSavingDescsVo()!=null){
					infoVo.setSavingDescs(vo.getSavingDescsVo().getSavingDesc());//??????????????????
				}
				infoVo.setCreateTime(new Date());//????????????
				infoVo.setCreateUser(userVo.getUserCode());//????????????
				laborsList.add(infoVo);
			}
		}
		//???????????????????????????
		if(claimVo.getFraudRisks()!=null && claimVo.getFraudRisks().size()>0){
			for(FraudRiskVo vo:claimVo.getFraudRisks()){
				PrplFraudriskInfoVo infoVo=new PrplFraudriskInfoVo();
				infoVo.setFactCode(vo.getFactCode());//????????????
				infoVo.setFactName(vo.getFactName());//????????????
				infoVo.setRiskDesc(vo.getRiskDesc());//????????????
				infoVo.setSuggest(vo.getSuggest());//????????????
				infoVo.setAccuracyRate(vo.getAccuracyRate());
				infoVo.setCreateTime(new Date());//????????????
				infoVo.setCreateUser(userVo.getUserCode());//????????????
				fraudRiskList.add(infoVo);
			}
		}
		
		//?????????????????????
		if(claimVo.getRiskPoints()!=null && claimVo.getRiskPoints().size()>0 ){
			for(RiskPointVo vo:claimVo.getRiskPoints()){
				PrplRiskpointInfoVo infoVo=new PrplRiskpointInfoVo();
				infoVo.setFactCode(vo.getFactCode());//????????????
				infoVo.setFactName(vo.getFactName());//????????????
				infoVo.setRiskDesc(vo.getRiskDesc());//????????????
				infoVo.setSuggest(vo.getSuggest());//????????????
				infoVo.setCreateTime(new Date());//????????????
				infoVo.setCreateUser(userVo.getUserCode());//????????????
				riskPointList.add(infoVo);
			}
		}
		//?????????????????????
		if(claimVo.getNonStandardOperations()!=null && claimVo.getNonStandardOperations().size()>0){
			for(NonStandardOperationVo vo:claimVo.getNonStandardOperations()){
				PrplOperationInfoVo infoVo=new PrplOperationInfoVo();
				infoVo.setFactCode(vo.getFactCode());//????????????
				infoVo.setFactName(vo.getFactName());//????????????
				infoVo.setRiskDesc(vo.getRiskDesc());//????????????
				infoVo.setSuggest(vo.getSuggest());//????????????
				infoVo.setCreateTime(new Date());//????????????
				infoVo.setCreateUser(userVo.getUserCode());//????????????
				operationList.add(infoVo);
			}
		}
		if(fraudRiskList!=null && fraudRiskList.size()>0){
			mainVo.setPrplFraudriskInfos(fraudRiskList);
		}
		if(laborsList!=null && laborsList.size()>0){
			mainVo.setPrplLaborInfos(laborsList);	
		}
		if(partsList!=null && partsList.size()>0){
			mainVo.setPrplPartsInfos(partsList);
		}
		if(riskPointList!=null && riskPointList.size()>0){
			mainVo.setPrplRiskpointInfos(riskPointList);
		}
		if(operationList!=null && operationList.size()>0){
			mainVo.setPrplOperationInfos(operationList);
		}
		
		
		    ///??????
			PrplTestinfoMain prplTestinfoMain=new PrplTestinfoMain();
			Beans.copy().from(mainVo).to(prplTestinfoMain);
			List<PrplPartsInfo> partspoList=new ArrayList<PrplPartsInfo>();
			List<PrplLaborInfo> laborspoList=new ArrayList<PrplLaborInfo>();
			List<PrplFraudriskInfo> fraudRiskpoList=new ArrayList<PrplFraudriskInfo>();
			List<PrplRiskpointInfo> riskPointpoList=new ArrayList<PrplRiskpointInfo>();
			List<PrplOperationInfo> operationpoList=new ArrayList<PrplOperationInfo>();
			partspoList=Beans.copyDepth().from(partsList).toList(PrplPartsInfo.class);
			laborspoList=Beans.copyDepth().from(laborsList).toList(PrplLaborInfo.class);
			fraudRiskpoList=Beans.copyDepth().from(fraudRiskList).toList(PrplFraudriskInfo.class);
			riskPointpoList=Beans.copyDepth().from(riskPointList).toList(PrplRiskpointInfo.class);
			operationpoList=Beans.copyDepth().from(operationList).toList(PrplOperationInfo.class);
			if(partspoList!=null && partspoList.size()>0){
				for(PrplPartsInfo po:partspoList){
					po.setPrplTestinfoMain(prplTestinfoMain);
				}	
			}
			if(laborspoList!=null && laborspoList.size()>0){
				for(PrplLaborInfo po:laborspoList){
					po.setPrplTestinfoMain(prplTestinfoMain);
				}
			}
			if(fraudRiskpoList!=null && fraudRiskpoList.size()>0){
				for(PrplFraudriskInfo po:fraudRiskpoList){
					po.setPrplTestinfoMain(prplTestinfoMain);
				}	
			}
			if(riskPointpoList!=null && riskPointpoList.size()>0){
				for(PrplRiskpointInfo po:riskPointpoList){
					po.setPrplTestinfoMain(prplTestinfoMain);
				}
			}
			if(operationpoList!=null && operationpoList.size()>0){
				for(PrplOperationInfo po:operationpoList){
					po.setPrplTestinfoMain(prplTestinfoMain);
				}	
			}
			if(fraudRiskpoList!=null && fraudRiskpoList.size()>0){
				prplTestinfoMain.setPrplFraudriskInfos(fraudRiskpoList);
			}
			if(laborspoList!=null && laborspoList.size()>0){
				prplTestinfoMain.setPrplLaborInfos(laborspoList);
			}
			if(operationpoList!=null && operationpoList.size()>0){
				prplTestinfoMain.setPrplOperationInfos(operationpoList);
			}
			if(partspoList!=null && partspoList.size()>0){
				prplTestinfoMain.setPrplPartsInfos(partspoList);
			}
			if(riskPointpoList!=null && riskPointpoList.size()>0){
				prplTestinfoMain.setPrplRiskpointInfos(riskPointpoList);
			}
			PrplTestinfoMainVo prplTestinfoMainVo=Beans.copyDepth().from(prplTestinfoMain).to(PrplTestinfoMainVo.class);
			claimTaskExtService.saveToPrplTestinfoMain(prplTestinfoMainVo);
		
	}
	/**
	 * ??????????????????
	 * String ???????????? Date??????
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	private Date StringFormatDate(String strDate) throws ParseException{
		Date date=null;
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		  if(StringUtils.isNotBlank(strDate)){
			date=format.parse(strDate);
		}
		return date;
	}
	
	/**
	 * ??????????????????
	 * Date ???????????? String??????
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	private String DateFormatString(Date strDate) throws ParseException{
		String str="";
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		  if(strDate!=null){
			  str=format.format(strDate);
		}
		return str;
	}

	@Override
	public List<PrplTestinfoMainVo> findPrplTestinfoMainByRegistNoAndNodeFlag(String registNo, String nodeFlag) {
		QueryRule rule=QueryRule.getInstance();
		rule.addEqual("registNo",registNo);
		rule.addEqual("nodeFlag",nodeFlag);
		List<PrplTestinfoMain> lists=databaseDao.findAll(PrplTestinfoMain.class, rule);
		QueryRule rule1=QueryRule.getInstance();
		if(lists!=null && lists.size()>0){
			for(PrplTestinfoMain po:lists){
				rule1.addEqual("prplTestinfoMain.id",po.getId());
				List<PrplPartsInfo> partspoList=databaseDao.findAll(PrplPartsInfo.class,rule1);
				List<PrplLaborInfo> laborspoList=databaseDao.findAll(PrplLaborInfo.class,rule1);
				List<PrplFraudriskInfo> fraudRiskpoList=databaseDao.findAll(PrplFraudriskInfo.class,rule1);
				List<PrplRiskpointInfo> riskPointpoList=databaseDao.findAll(PrplRiskpointInfo.class,rule1);
				List<PrplOperationInfo> operationpoList=databaseDao.findAll(PrplOperationInfo.class,rule1);
				po.setPrplFraudriskInfos(fraudRiskpoList);
				po.setPrplLaborInfos(laborspoList);
				po.setPrplOperationInfos(operationpoList);
				po.setPrplPartsInfos(partspoList);
				po.setPrplRiskpointInfos(riskPointpoList);
			}
			}
		List<PrplTestinfoMainVo> listVo=new ArrayList<PrplTestinfoMainVo>();
		if(lists!=null && lists.size()>0){
			listVo=Beans.copyDepth().from(lists).toList(PrplTestinfoMainVo.class);
		}
			
		return listVo;
	}
}
