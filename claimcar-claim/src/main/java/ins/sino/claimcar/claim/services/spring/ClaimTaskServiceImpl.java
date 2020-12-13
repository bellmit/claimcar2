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
	 * 根据条件查询立案
	 * <pre></pre>
	 * @param registNo
	 * @param policyNo
	 * @param validFlag 是否有效
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年5月13日 下午2:30:09): <br>
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
//			//立案送平台
//			if(claimVo != null){
//				sendClaimToAll.sendClaimToPlatform(claimVo);
//			}
//		}
	}

	@Override
	public void claimForceJob(String registNo,String policyNo,String flowId) throws ParseException {
		PrpLClaimVo claimVo = claimTaskExtService.submitClaim(registNo,policyNo,flowId,ClaimFlag.CLAIMFORCE);
		//立案送平台
		if(claimVo != null){
			sendClaimToPlatform(claimVo);
		}
	}
	
	// 立案送平台
	@Override
	public void sendClaimToPlatform(PrpLClaimVo claimVo) {
		if(claimVo!=null){
			sendClaimToAll.sendClaimToPlatform(claimVo);
		}
	}

	
	// 其他环节用来刷新立案估损金额
	@Override
	public void updateClaimFee(String registNo,String userCode,FlowNode node) throws Exception {
		claimTaskExtService.updateClaimFee(registNo,userCode,node);
	}

	/**
	 * 理算核赔通过后刷新立案估损金额
	 * @param registNo
	 * @param userCode
	 * @param node
	 * @modified: ☆weilanlei(2016年6月22日 ): <br>
	 */
	@Override
	public void updateClaimAfterCompe(String compensateNo,String userCode,FlowNode node) {
		claimTaskExtService.updateClaimAfterCompe(compensateNo,userCode,node);
	}
	/**
	 * 理算冲销核赔通过后0结案，刷新立案估损金额
	 * @param registNo
	 * @param userCode
	 * @param node
	 * @modified: ☆weilanlei(2016年12月1日 ): <br>
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
	 * 结案刷新轨迹
	 * @param claimNo
	 * @return
	 * @modified: ☆LiuPing(2016年6月3日 ): <br>
	 */
	public void calcClaimKindHisByEndCase(String claimNo){
		claimKindHisService.calcClaimKindHisByEndCase(claimNo);
	}

	/**
	 * 重开赔案刷新轨迹
	 * @param claimNo
	 * @return
	 * @modified: ☆LiuPing(2016年6月3日 ): <br>
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
	 * 30天案均上浮 人伤估损翻倍
	 * <pre></pre>
	 * @param registVo
	 * @modified:
	 * ☆WLL(2016年8月17日 下午5:46:37): <br>
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
				if(!claimNo.equals(prpLClaimVo.getClaimNo())){//另一个立案是否已经注销或者拒赔
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
		sqlUtil.append(" claim.caseNo<>? and claim.endCaseTime is null");//未结案未注销
		sqlUtil.addParamValue("0");
		sqlUtil.append(" and claim.comcode like ?");
		sqlUtil.addParamValue(comCode+"%");
		sqlUtil.append(" claim.updateTime<? and claim.updateTime>?");
		sqlUtil.addParamValue(sdf.format(cal.getTime()));
		cal.add(Calendar.DATE, -2);// 获取前天日期
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
	 * 提供定时服务调用的方法
	 * 未决案件最新估损金额上传[为了不影响保险公司出单业务及系统的稳定运行，除每日20：00至次日6:00外，其他时间该接口停止使用。]
	 * 查询出当天需要上传的立案信息
	 * @modified:
	 * ☆Luwei(2016年12月21日 上午11:24:58): <br>
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
		// 调用工作流注销方法
		wfTaskHandleService.cancelTaskForOther(registNo, userCode);
	}
	
	@Override
	public void recoverClaimForOther(String registNo, String userCode,BigDecimal flowTaskId) {
		// 调用工作流注销方法
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
		//组织请求数据
		baseVo=setParamsForTransitClaimDocumentBaseVo(registNo,licenseNo,lossCarMainId,nodeFlag);
		
		CECheckResultVo resultVo=new CECheckResultVo();
		 String xmlToSend = ClaimBaseCoder.objToXmlUtf(baseVo);
		 logger.info("德联易控发送信息send---------------------------"+xmlToSend);//ControlExpert_URL
		 String url= SpringProperties.getProperty("ControlExpert_URL");
	        try{
	        	String xmlReturn=this.requestControlExpert(xmlToSend, url);
	            logger.info("德联易控返回送信息return---------------------------"+xmlReturn);
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
	            	logVo.setOperateNode("报案"); //德联易控操作类型
	            	logVo.setFlag("1");
	            }else if("2".equals(nodeFlag)){
	            	logVo.setOperateNode("查勘"); //德联易控操作类型
	            	logVo.setFlag("2");
	            }else{
	            	logVo.setOperateNode("车辆定损"); //德联易控操作类型
	            	logVo.setFlag("3");
	            }
	            
	            logVo.setClaimNo(lossCarMainId);   //区分是定损主表Id
	            logVo.setCompensateNo(licenseNo);  //德联易控相关车辆车牌号
	            logVo.setServiceType(resultVo.getExtractionCode()); //提取码
	            logVo.setCreateUser(userVo.getUserCode());
	            logVo.setComCode(userVo.getComCode());
	            logVo.setRequestUrl(url);
	            this.logUtil(baseVo,resultVo,logVo,resultVo.getErrCode(),resultVo.getErrMsg());
	        }
		
	}
	
	/**
	 * 给TransitClaimDocumentBaseVo组装数据
	 * @param registNo
	 * @param licenseNo
	 * @return
	 * @throws Exception 
	 */
	private TransitClaimDocumentBaseVo setParamsForTransitClaimDocumentBaseVo(String registNo, String licenseNo,String lossCarMainId,String nodeFlag) throws Exception{
		TransitClaimDocumentBaseVo baseVo=new TransitClaimDocumentBaseVo();
		PrpLDlossCarMainVo carMainVo=new PrpLDlossCarMainVo();//车辆定损主表
		if(StringUtils.isNotBlank(lossCarMainId)){
			carMainVo=lossCarService.findLossCarMainById(Long.valueOf(lossCarMainId));
		}
		
		List<PrpLDlossCarInfoVo> lossCarInfoList=lossCarService.findPrpLDlossCarInfoVoListByRegistNo(registNo);//定损车辆信息列表
		PrpLDlossCarInfoVo lossCarInfoVo=new PrpLDlossCarInfoVo();//定损信息表
		if(lossCarInfoList!=null && lossCarInfoList.size()>0){
			for(PrpLDlossCarInfoVo vo:lossCarInfoList){
				if(licenseNo.equals(vo.getLicenseNo())){
					lossCarInfoVo=vo;
				}
			}
		}
		List<ConfirmLossDiscussionVo> DiscussionVos=new ArrayList<ConfirmLossDiscussionVo>();//商业险定核损意见列表
		List<LaborVo> laborVos=new ArrayList<LaborVo>();//工时列表
		List<SmallSparepartVo> sparepartVos=new ArrayList<SmallSparepartVo>();//辅料列表
		List<SparepartVo> repartVos=new ArrayList<SparepartVo>();//配件列表
		List<RelatingVehicleVo> relatingVehicles=new ArrayList<RelatingVehicleVo>();//三者车信息
		
		PrpLRegistVo prpLRegistVo=new PrpLRegistVo();
		PrpLRegistVo prpLRegistVo1=registQueryService.findByRegistNo(registNo);//报案表信息
		if(prpLRegistVo1!=null){
			prpLRegistVo=prpLRegistVo1;
		}
		List<PrpLRegistCarLossVo> prpLRegistCarLosses = new ArrayList<PrpLRegistCarLossVo>();
		prpLRegistCarLosses=prpLRegistVo.getPrpLRegistCarLosses();//报案车辆列表信息
		PrpLRegistCarLossVo prpLRegistCarLossVo=new PrpLRegistCarLossVo();//该车的报案车辆信息
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
		List<SpecialAgreementVo> bIAgreementVos=new ArrayList<SpecialAgreementVo>();//商业险特别约定列表
		List<SpecialAgreementVo> cIAgreementVos=new ArrayList<SpecialAgreementVo>();//交强险特别约定列表
		List<InsuranceModificationVo> bImodificationVos=new ArrayList<InsuranceModificationVo>();//商业批单信息列表
		List<InsuranceModificationVo> cImodificationVos=new ArrayList<InsuranceModificationVo>();//交强批单信息列表
		List<InsuranceItemVo> biItemVos=new ArrayList<InsuranceItemVo>();//商业险险种列表
		List<InsuranceItemVo> ciItemVos=new ArrayList<InsuranceItemVo>();//交强险险种列表
		List<HistoricalClaimVo> bIhisClaimVos=new ArrayList<HistoricalClaimVo>();//商业保单出险记录列表
		List<HistoricalClaimVo> cIhisClaimVos=new ArrayList<HistoricalClaimVo>();//交强保单出险记录列表
		PrpLCheckVo prpLCheckVo=new PrpLCheckVo();
		PrpLCheckVo prpLCheckVo1=checkTaskService.findCheckVoByRegistNo(registNo);//查勘主表
		if(prpLCheckVo1!=null){
			prpLCheckVo=prpLCheckVo1;
		}
		PrpLCheckTaskVo prpLCheckTaskVo=new PrpLCheckTaskVo();
		PrpLCheckTaskVo prpLCheckTaskVo1=prpLCheckVo.getPrpLCheckTask();//查勘任务表
		if(prpLCheckTaskVo1!=null){
			prpLCheckTaskVo=prpLCheckTaskVo1;
		}
		List<PrpLCheckCarInfoVo> carInfoList=checkTaskService.findPrpLCheckCarInfoVoListByRegistNo(registNo);//车辆信息列表
		PrpLCheckDriverVo prpLCheckDriverVo=new PrpLCheckDriverVo();//车辆驾驶员表
		PrpLCheckCarVo carVo=new PrpLCheckCarVo();//车辆信息表
		PrpLCheckDutyVo prplcheckdutyVo=new PrpLCheckDutyVo();//车辆责任表
		List<PrpLCMainVo> prplCmainVos=policyViewService.findPrpLCMainVoListByRegistNo(registNo);//保单信息表
		PrpLCMainVo biVo=new PrpLCMainVo();//商业险保单信息
		PrpLCMainVo ciVo=new PrpLCMainVo();//交强险保单信息
		if(prplCmainVos!=null && prplCmainVos.size()>0){
			for(PrpLCMainVo vo:prplCmainVos){
				if("1101".equals(vo.getRiskCode())){
					ciVo=vo;
				}else{
					biVo=vo;
				}
			}
		}
		//商业保单出险记录
		if(biVo!=null &&  biVo.getId()!=null){
			List<PrpLClaimVo> claimVos=claimService.findPrpLClaimVosByPolicyNo(biVo.getPolicyNo());
			if(claimVos!=null && claimVos.size()>0){
				for(PrpLClaimVo vo:claimVos){
					PrpLRegistVo registVo=registQueryService.findByRegistNo(vo.getRegistNo());
					HistoricalClaimVo hisclaimVo=new HistoricalClaimVo();
					if(vo.getDamageTime()!=null){
						hisclaimVo.setClaimDate(this.DateFormatString(vo.getDamageTime()));//出险时间
					}
					//hisclaimVo.setClaimEndDate(this.DateFormatString(vo.getEndCaseTime()));//结案时间
					hisclaimVo.setClaimNumber(vo.getRegistNo());//报案号
					hisclaimVo.setDriverName(registVo.getDriverName());//驾驶员
					hisclaimVo.setEventAddress(registVo.getDamageAddress());//出险地点
					hisclaimVo.setInsuranceCategory("12");//险别
					
					
					hisclaimVo.setInsuranceNumber(vo.getPolicyNo());//出险保单
					//hisclaimVo.setPaidAmount(vo.getSumPaid()+"");//保单结案金额
					//hisclaimVo.setPaidTimes("");//赔付次数
					if(vo.getReportTime()!=null){
						hisclaimVo.setReportDate(this.DateFormatString(vo.getReportTime()));//报案时间
					}
					hisclaimVo.setReporter(registVo.getReportorName());//报案人
					hisclaimVo.setReporterTel(registVo.getReportorPhone());//报案人电话
					hisclaimVo.setStatus("");//案件状态
					bIhisClaimVos.add(hisclaimVo);
				}
				
			}
		}
		//商业险种列表
		if(biVo.getPrpCItemKinds()!=null && biVo.getPrpCItemKinds().size()>0){
			for(PrpLCItemKindVo vo:biVo.getPrpCItemKinds()){
				InsuranceItemVo itemVo=new InsuranceItemVo();
				if(vo.getAdjustRate()!=null){
					itemVo.setAdjustmentRatio(vo.getAdjustRate()+"");//调整比率
				}
				if(vo.getDeductibleRate()!=null){
					itemVo.setDeductibleRate(vo.getDeductibleRate()+"");//免赔率
				}
				if(vo.getDiscount()!=null){
					itemVo.setDiscount(vo.getDiscount()+"");//折扣率
				}
				if(vo.getAmount()!=null){
					itemVo.setInsuranceAmount(vo.getAmount()+"");//保险限额
				}
				if(vo.getBenchMarkPremium()!=null){
					itemVo.setInsuranceFee(vo.getBenchMarkPremium()+"");//标准保费
				}
				if(StringUtils.isNotBlank(carMainVo.getLossFeeType())){
					if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(carMainVo.getRiskCode()) != null &&
							CodeConstants.ISNEWCLAUSECODE2020_MAP.get(carMainVo.getRiskCode())){
						itemVo.setInsuranceItemCode(CodeConstants.LossFee2020Kind_Map.get(carMainVo.getRiskCode()+carMainVo.getLossFeeType()));//险种代码
					}else{
						itemVo.setInsuranceItemCode(CodeConstants.LossFeeKind_Map.get(carMainVo.getLossFeeType()));//险种代码
					}
					
				}else{
					itemVo.setInsuranceItemCode("A");
				}
				
				SysCodeDictVo sysVo1 = codeTranService.findTransCodeDictVo("CarRiskCode",vo.getRiskCode());
				itemVo.setName(sysVo1.getCodeName());//险种名称
				
				
				if("1".equals(vo.getNoDutyFlag())){//承保不计免费
					itemVo.setNonDeductible("true");
				}else if("0".equals(vo.getNoDutyFlag())){
					itemVo.setNonDeductible("false");
				}else{
					itemVo.setNonDeductible("false");
				}
				if(vo.getQuantity()!=null){
					itemVo.setQty(vo.getQuantity()+"");//数量
				}
				
				itemVo.setRemark("");//备注
				if(vo.getUnitAmount()!=null){
					itemVo.setSingleInsuranceAmount(vo.getUnitAmount()+"");//单次赔款限额
				}
				
				biItemVos.add(itemVo);
			}
		}
		
		//交强险种列表
		if(ciVo.getPrpCItemKinds()!=null && ciVo.getPrpCItemKinds().size()>0){
			for(PrpLCItemKindVo vo:ciVo.getPrpCItemKinds()){
				InsuranceItemVo itemVo=new InsuranceItemVo();
				if(vo.getAdjustRate()!=null){
					itemVo.setAdjustmentRatio(vo.getAdjustRate()+"");//调整比率
				}
				if(vo.getDeductibleRate()!=null){
					itemVo.setDeductibleRate(vo.getDeductibleRate()+"");//免赔率
				}
				if(vo.getDiscount()!=null){
					itemVo.setDiscount(vo.getDiscount()+"");//折扣率*/
				}
				
				if(vo.getAmount()!=null){
			      itemVo.setInsuranceAmount(vo.getAmount()+"");//保险限额
				}
			   if(vo.getBenchMarkPremium()!=null){
				   itemVo.setInsuranceFee(vo.getBenchMarkPremium()+"");//标准保费 
			   }
				itemVo.setInsuranceItemCode("BZ");//险种代码
				SysCodeDictVo sysVo1 = codeTranService.findTransCodeDictVo("CarRiskCode",vo.getRiskCode());
				
					itemVo.setName(sysVo1.getCodeName());//险种名称
				
				
				if("1".equals(vo.getNoDutyFlag())){//承保不计免费
					itemVo.setNonDeductible("true");
				}else if("0".equals(vo.getNoDutyFlag())){
					itemVo.setNonDeductible("false");
				}else{
					itemVo.setNonDeductible("false");
				}
				if(vo.getQuantity()!=null){
					itemVo.setQty(vo.getQuantity()+"");//数量
				}
				
				itemVo.setRemark("");//备注
				if(vo.getUnitAmount()!=null){
					itemVo.setSingleInsuranceAmount(vo.getUnitAmount()+"");//单次赔款限额
				}
				
				ciItemVos.add(itemVo);
			}
		}
		
		//交强保单出险记录
		if(biVo!=null &&  biVo.getId()!=null){
		   List<PrpLClaimVo> claimVos=claimService.findPrpLClaimVosByPolicyNo(biVo.getPolicyNo());
				if(claimVos!=null && claimVos.size()>0){
					for(PrpLClaimVo vo:claimVos){
							PrpLRegistVo registVo=registQueryService.findByRegistNo(vo.getRegistNo());
							HistoricalClaimVo hisclaimVo=new HistoricalClaimVo();
							if(vo.getDamageTime()!=null){
								hisclaimVo.setClaimDate(this.DateFormatString(vo.getDamageTime()));//出险时间
							}
							//hisclaimVo.setClaimEndDate(this.DateFormatString(vo.getEndCaseTime()));//结案时间
							hisclaimVo.setClaimNumber(vo.getRegistNo());//报案号
							hisclaimVo.setDriverName(registVo.getDriverName());//驾驶员
							hisclaimVo.setEventAddress(registVo.getDamageAddress());//出险地点
							hisclaimVo.setInsuranceCategory("11");//险别
							hisclaimVo.setInsuranceNumber(vo.getPolicyNo());//出险保单
							//hisclaimVo.setPaidAmount(vo.getSumPaid()+"");//保单结案金额
							//hisclaimVo.setPaidTimes("");//赔付次数
							if(vo.getReportTime()!=null){
								hisclaimVo.setReportDate(this.DateFormatString(vo.getReportTime()));//报案时间
							}
							hisclaimVo.setReporter(registVo.getReportorName());//报案人
							hisclaimVo.setReporterTel(registVo.getReportorPhone());//报案人电话
							hisclaimVo.setStatus("");//案件状态
							cIhisClaimVos.add(hisclaimVo);
						}
						
					}
				}
		
		//商业险批单信息
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
			vo.setEndNoContext(endorContent);//批改内容
			if(StringUtils.isNotBlank(endorContent)){
				cationVo.setContent(vo.getEndNoContext());//批单内容
			}else{
				cationVo.setContent("批单内容");//批单内容
			}
			cationVo.setInsuranceNumber(vo.getPolicyNo());//保单号
			cationVo.setModificationBillNo(vo.getEndorseNo());//批单号
			if(vo.getEndorDate()!=null){
				cationVo.setMofificationDate(this.DateFormatString(vo.getEndorDate()));//批改日期
			}
			
			cationVo.setOrder(i+"");//序号
			bImodificationVos.add(cationVo);
			
		}
		
		//交强险批单信息
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
					vo.setEndNoContext(endorContent);//批改内容
					if(StringUtils.isNotBlank(endorContent)){
						cationVo.setContent(vo.getEndNoContext());//批单内容
					}else{
						cationVo.setContent("批单内容");//批单内容
					}
					cationVo.setInsuranceNumber(vo.getPolicyNo());//保单号
					cationVo.setModificationBillNo(vo.getEndorseNo());//批单号
					if(vo.getEndorDate()!=null){
						cationVo.setMofificationDate(this.DateFormatString(vo.getEndorDate()));//批改日期
					}
					
					cationVo.setOrder(j+"");//序号
					cImodificationVos.add(cationVo);
					
				}
		
		//商业险特别约定列表
		String  promiseStr="";
		 List<PrpLCengageVo> prpCengageVos=biVo.getPrpCengages();
		 if(prpCengageVos!=null && prpCengageVos.size()>0){
			 for(PrpLCengageVo vo:prpCengageVos){
				 if(!"1101".equals(vo.getRiskCode())){
					 promiseStr=promiseStr+vo.getClauses();
				 }
			 }
			 if(StringUtils.isNotEmpty(promiseStr)){
		    	 promiseStr=promiseStr+"(商业约定);";
		     }
			SpecialAgreementVo agreementVo=new SpecialAgreementVo();
			agreementVo.setContent(promiseStr);//商业特别约定内容
			agreementVo.setOrder("1");
			bIAgreementVos.add(agreementVo);
			
		 }
		 
		//交强险特别约定列表
		String  promiseStr1="";
			 List<PrpLCengageVo> prpCengageVo1s=ciVo.getPrpCengages();
			 if(prpCengageVo1s!=null && prpCengageVo1s.size()>0){
				 for(PrpLCengageVo vo:prpCengageVo1s){
					 if("1101".equals(vo.getRiskCode())){
						 promiseStr1=promiseStr1+vo.getClauses();
					 }
				 }
				 if(StringUtils.isNotEmpty(promiseStr)){
			    	 promiseStr1=promiseStr1+"(交强约定);";
			     }
				SpecialAgreementVo agreementVo=new SpecialAgreementVo();
				agreementVo.setContent(promiseStr1);//交强特别约定内容
				agreementVo.setOrder("1");
				cIAgreementVos.add(agreementVo);
				
			 }
		//(car Draiver carInfo) 同一个Id 的
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
		
		//给查勘信息赋值
		CheckVo checkVo=new CheckVo();
		checkVo.setCheckAddress(prpLCheckTaskVo.getCheckAddress());//查勘地址
		if(prpLCheckTaskVo.getCheckDate()!=null){
			checkVo.setCheckDate(this.DateFormatString(prpLCheckTaskVo.getCheckDate()));//查勘时间
		}
		
		checkVo.setCheckEmployee1Code(prpLCheckTaskVo.getCheckerCode());//查勘员代码1
		checkVo.setCheckEmployee1Name(prpLCheckTaskVo.getChecker());//查勘员1
		checkVo.setCheckEmployee2Code("");//查勘员代码2
		checkVo.setCheckEmployee2Name("");//查勘员2
		checkVo.setCheckopinion(prpLCheckTaskVo.getContexts());//查勘报告
		checkVo.setCheckType(prpLCheckVo.getCheckType());//查勘类型
		checkVo.setClaimType("0");//赔案类型
		checkVo.setContactPerson(prpLCheckTaskVo.getLinkerName());//联系人
		checkVo.setContactTel(prpLCheckTaskVo.getLinkerMobile());//联系电话
		checkVo.setDisposeType(prpLCheckVo.getManageType());//事故处理方式
		DriverVo driverVo1=new DriverVo();//驾驶员信息
		driverVo1.setAddress(prpLCheckDriverVo.getUnitAddress());//驾驶员单位或家庭地址
		if(prpLCheckDriverVo.getDriverAge()!=null){
			driverVo1.setDriverAge(prpLCheckDriverVo.getDriverAge().toString());//年龄
		}
		driverVo1.setDriverAllowedVehicleType(prpLCheckDriverVo.getIdentifyType());//证件类型
		driverVo1.setDriverEducation("");//教育程度
		driverVo1.setDriverGender(prpLCheckDriverVo.getDriverSex());//驾驶员性别
		driverVo1.setDriverLicenseInstitution("");//颁证机关
		driverVo1.setDriverTel(prpLCheckDriverVo.getLinkPhoneNumber());//联系电话
		if(prpLCheckDriverVo.getAcceptLicenseDate()!=null){
			driverVo1.setDrivingLicenseDate(this.DateFormatString(prpLCheckDriverVo.getAcceptLicenseDate()));//初次领证日期
		}
		
		driverVo1.setDrivingLicenseNum(prpLCheckDriverVo.getDrivingLicenseNo());//驾驶证号码
		
		
		driverVo1.setName(prpLCheckDriverVo.getDriverName());//驾驶员姓名	
		
		
		checkVo.setDriverVo(driverVo1);
		checkVo.setEventAddressType("1");//道路类型
		checkVo.setEventReason(prpLCheckVo.getDamageCode());//出险原因
		if(StringUtils.isNotBlank(prpLCheckVo.getAccidentReason())){
			checkVo.setEventReasonType(prpLCheckVo.getAccidentReason());//事故原因
		}else{
			checkVo.setEventReasonType("01");//追尾
		}
		
		checkVo.setEventResponsibility(prplcheckdutyVo.getIndemnityDuty());//责任类型
		checkVo.setEventType(prpLCheckVo.getDamageTypeCode());//事故类型代码
		checkVo.setEventCategory("");//事故类别
		checkVo.setIsFirstScene("true");//是否第一查勘
		checkVo.setLossType(prpLCheckVo.getLossType());//损失类别
		
		//给商业险信息赋值
		CommercialInsuranceVo bIInfoVo=new CommercialInsuranceVo();
		bIInfoVo.setAgreeDriverFlag("true");//是否约定驾驶员
		if(biVo.getUnderWriteEndDate()!=null){
		 bIInfoVo.setBuyInsuranceDate(this.DateFormatString(biVo.getUnderWriteEndDate()));//投保日期
		}
		
		bIInfoVo.setHistoricalClaims(bIhisClaimVos);//商业险保单出险记录
		if(biVo.getSumAmount()!=null){
			bIInfoVo.setInsuranceAmount(biVo.getSumAmount()+"");//总保额	
		}
		
		bIInfoVo.setInsuranceCategory("12");//险别	
		
		
		if(biVo.getEndDate()!=null){
			bIInfoVo.setInsuranceEndDate(this.DateFormatString(biVo.getEndDate()));//终保日期
		}
		if(biVo.getSumPremium()!=null){
			bIInfoVo.setInsuranceFee(biVo.getSumPremium()+"");//总保费
		}
		
		bIInfoVo.setInsuranceItems(biItemVos);//险种列表
		bIInfoVo.setInsuranceModifications(bImodificationVos);//批单批改列表
		bIInfoVo.setInsuranceNumber(biVo.getPolicyNo());//保单号
		if(biVo.getStartDate()!=null){
			bIInfoVo.setInsuranceStartDate(this.DateFormatString(biVo.getStartDate()));//起保日期
		}
		if(biVo.getOperateDate()!=null){
			bIInfoVo.setIssueDate(this.DateFormatString(biVo.getOperateDate()));//出单日期
		}
		
		bIInfoVo.setPolicyHolder(biVo.getAppliName());//投保人
		bIInfoVo.setSignAddress(biVo.getOperateSite());//签单地点
		if(biVo.getSignDate()!=null){
			bIInfoVo.setSignDate(this.DateFormatString(biVo.getSignDate()));//签单日期	
		}
		
		bIInfoVo.setSpecialAgreements(bIAgreementVos);//保单特别约定列表
		bIInfoVo.setTheInsuredName(biVo.getInsuredName());//被保险人
		
		//给交强险信息赋值
		CompulsoryInsuranceVo cIInfoVo=new CompulsoryInsuranceVo();
		cIInfoVo.setAgreeDriverFlag("true");//是否约定驾驶员
		if(ciVo.getUnderWriteEndDate()!=null){
			cIInfoVo.setBuyInsuranceDate(this.DateFormatString(ciVo.getUnderWriteEndDate()));//投保日期
		}
		
		cIInfoVo.setHistoricalClaims(cIhisClaimVos);//商业险保单出险记录
		if(ciVo.getSumAmount()!=null){
			cIInfoVo.setInsuranceAmount(ciVo.getSumAmount()+"");//总保额
		}
		
		cIInfoVo.setInsuranceCategory("11");//险别
		if(ciVo.getEndDate()!=null){
			cIInfoVo.setInsuranceEndDate(this.DateFormatString(ciVo.getEndDate()));//终保日期
		}
		if(ciVo.getSumPremium()!=null){
			cIInfoVo.setInsuranceFee(ciVo.getSumPremium()+"");//总保费	
		}
		
		cIInfoVo.setInsuranceItems(ciItemVos);//险种列表
		cIInfoVo.setInsuranceModifications(cImodificationVos);//批单批改列表
		cIInfoVo.setInsuranceNumber(ciVo.getPolicyNo());//保单号
		if(ciVo.getStartDate()!=null){
			cIInfoVo.setInsuranceStartDate(this.DateFormatString(ciVo.getStartDate()));//起保日期	
		}
		if(ciVo.getOperateDate()!=null){
			cIInfoVo.setIssueDate(this.DateFormatString(ciVo.getOperateDate()));//出单日期
		}
		
		cIInfoVo.setPolicyHolder(ciVo.getAppliName());//投保人
		cIInfoVo.setSignAddress(ciVo.getOperateSite());//签单地点
		if(ciVo.getSignDate()!=null){
			cIInfoVo.setSignDate(this.DateFormatString(ciVo.getSignDate()));//签单日期
		}
		
		cIInfoVo.setSpecialAgreements(cIAgreementVos);//保单特别约定列表
		cIInfoVo.setTheInsuredName(ciVo.getInsuredName());//被保险人
		
		//定损意见赋值
		PrpLClaimTextVo textVo=new PrpLClaimTextVo();
		PrpLClaimTextVo  textVo1=claimTextService.findClaimTextByLossCarMainIdAndNodeCode(carMainVo.getId(),"DLCar");
		if(textVo1!=null){
			textVo=textVo1;
		}
		ConfirmLossDiscussionVo cussionVo=new ConfirmLossDiscussionVo();//商业核损意见
		if(textVo!=null && textVo.getId()!=null){
			if(textVo.getSumLossFee()!=null){
				cussionVo.setAmount(textVo.getSumLossFee()+"");//总金额
			}
			
			cussionVo.setComment(textVo.getDescription());//定损意见
			cussionVo.setCompanyname(codeTranService.transCode("ComCode",textVo.getComCode()));//分公司名称
			if(textVo.getInputTime()!=null){
				cussionVo.setDate(this.DateFormatString(textVo.getInputTime()));//意见发表时间
			}
			
			cussionVo.setPersonName(textVo.getOperatorName());//意见提出人
			cussionVo.setStatus("");//意见状态
		}else{
			//cussionVo.setAmount("");//总金额
		    cussionVo.setComment("定损意见");//定损意见
			cussionVo.setCompanyname(codeTranService.transCode("ComCode",carMainVo.getComCode()));//分公司名称
			if(carMainVo.getDeflossDate()!=null){
			 cussionVo.setDate(this.DateFormatString(carMainVo.getDeflossDate()));//意见发表时间
			}
			
			cussionVo.setPersonName(carMainVo.getHandlerName());//意见提出人
			cussionVo.setStatus("");//意见状态
		}
		DiscussionVos.add(cussionVo);
		
		//给工时列表赋值
		List<PrpLDlossCarRepairVo> carRepairVos=carMainVo.getPrpLDlossCarRepairs();
		if(carRepairVos!=null && carRepairVos.size()>0){
			for(PrpLDlossCarRepairVo vo:carRepairVos){
				LaborVo laborVo=new LaborVo();
				laborVo.setCode(vo.getRepairId());//保险公司定损项目编码
				if(vo.getManHourUnitPrice()!=null){
					laborVo.setPrice(vo.getManHourUnitPrice()+"");//单价	
				}
				if(vo.getManHour()!=null){
					laborVo.setCount(vo.getManHour()+"");//数量
				}
				
				laborVo.setDescription(vo.getRepairName());//工时名称
				laborVo.setLaborType(vo.getRepairCode());//工时类型
				laborVo.setRemark("");//备注
				//laborVo.setHandAddFlag("");//是否手动输入
				if(StringUtils.isNotBlank(carMainVo.getLossFeeType())){
					if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(carMainVo.getRiskCode()) != null &&
							CodeConstants.ISNEWCLAUSECODE2020_MAP.get(carMainVo.getRiskCode())){
						laborVo.setInsuranceItem(CodeConstants.LossFee2020Kind_Map.get(carMainVo.getRiskCode()+carMainVo.getLossFeeType()));//定损险种
					}else{
						laborVo.setInsuranceItem(CodeConstants.LossFeeKind_Map.get(carMainVo.getLossFeeType()));//定损险种
					}
					
				}else{
					laborVo.setInsuranceItem("A");
				}
				
				laborVo.setPartName(vo.getCompName());//工时所涉配件名
				laborVos.add(laborVo);
				}
		}
		
		//给辅料列表赋值
		List<PrpLDlossCarMaterialVo> carMaterialVos= carMainVo.getPrpLDlossCarMaterials();
		if(carMaterialVos!=null && carMaterialVos.size()>0){
			for(PrpLDlossCarMaterialVo vo:carMaterialVos){
				SmallSparepartVo sparepartVo=new SmallSparepartVo();
				sparepartVo.setCode(vo.getAssistId());//保险公司定损项目编码
				if(vo.getAssisCount()!=null){
					sparepartVo.setCount(vo.getAssisCount()+"");//数量
				}
				
				sparepartVo.setDescription(vo.getMaterialName());//名称
				//sparepartVo.setHandAddFlag("");//是否手动录入
				if(StringUtils.isNotBlank(carMainVo.getLossFeeType())){
					if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(carMainVo.getRiskCode()) != null &&
							CodeConstants.ISNEWCLAUSECODE2020_MAP.get(carMainVo.getRiskCode())){
						sparepartVo.setInsuranceItem(CodeConstants.LossFee2020Kind_Map.get(carMainVo.getRiskCode()+carMainVo.getLossFeeType()));//定损险种类型
					}else{
						sparepartVo.setInsuranceItem(CodeConstants.LossFeeKind_Map.get(carMainVo.getLossFeeType()));//定损险种类型
					}
					
				}else{
					sparepartVo.setInsuranceItem("A");
				}
				
				if(vo.getUnitPrice()!=null){
					sparepartVo.setPrice(vo.getUnitPrice()+"");//单价	
				}
				
				sparepartVo.setRemark("");//备注
				sparepartVos.add(sparepartVo);
			}
		}
		
		//给配件列表赋值
		List<PrpLDlossCarCompVo> carCompVos=carMainVo.getPrpLDlossCarComps();
		
		if(carCompVos!=null && carCompVos.size()>0){
			for(PrpLDlossCarCompVo vo:carCompVos){
				SparepartVo repartVo=new SparepartVo(); 
				repartVo.setCode(vo.getIndId());//保险公司定损项目编码
				if(vo.getQuantity()!=null){
					repartVo.setCount(vo.getQuantity()+"");//数量
				}
				if(vo.getMaterialFee()!=null){
					repartVo.setPrice(vo.getMaterialFee()+"");//单价
				}
				
				repartVo.setDescription(vo.getCompName());//名称
				//repartVo.setDiscount("");//折扣
				//repartVo.setHandAddFlag("");//是否手动录入
				if(StringUtils.isNotBlank(carMainVo.getLossFeeType())){
					if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(carMainVo.getRiskCode()) != null &&
							CodeConstants.ISNEWCLAUSECODE2020_MAP.get(carMainVo.getRiskCode())){
						repartVo.setInsuranceItem(CodeConstants.LossFee2020Kind_Map.get(carMainVo.getRiskCode()+carMainVo.getLossFeeType()));//定损险种
					}else{
						repartVo.setInsuranceItem(CodeConstants.LossFeeKind_Map.get(carMainVo.getLossFeeType()));//定损险种
					}
					
				}else{
					repartVo.setInsuranceItem("A");
				}
				
				//repartVo.setManagementFee("");//管理费
				repartVo.setOriginalPriceType("");//换件价格方案
				repartVo.setRemark("");//备注
				if(vo.getRestFee()!=null){
					repartVo.setRestValue(vo.getRestFee()+"");//残值
				}
				
				repartVo.setOem(vo.getOriginalId());//配件原厂编码（OEM码）
				repartVos.add(repartVo);
			}
		}
		
		//给定损信息赋值
		ConfirmLossVo confirmLossVo=new ConfirmLossVo();
		confirmLossVo.setConfirmLossAddress(prpLCheckTaskVo.getCheckAddress());//查勘地址
		confirmLossVo.setConfirmLossCompany(codeTranService.transCode("ComCode",carMainVo.getComCode()));//定损机构名称
		confirmLossVo.setConfirmLossCompanyCode(carMainVo.getComCode());//定损机构代码
		if(carMainVo.getDeflossDate()!=null){
			confirmLossVo.setConfirmLossDate(this.DateFormatString(new Date()));//定损日期
		}
		
		confirmLossVo.setConfirmLossDiscussions(DiscussionVos);//核定损意见
		confirmLossVo.setConfirmLossEmployee(carMainVo.getHandlerName());//定损员
		confirmLossVo.setConfirmLossEmployeeCode(carMainVo.getHandlerCode());//定损员代码
		if("1".equals(carMainVo.getDeflossCarType())){
			if(StringUtils.isNotBlank(carMainVo.getLossFeeType())){
				confirmLossVo.setConfirmLossInsuranceItem(CodeConstants.LossFeeKind_Map.get(carMainVo.getLossFeeType()));//定损险种
			}else{
				confirmLossVo.setConfirmLossInsuranceItem("A");
			}
		}else{
			    confirmLossVo.setConfirmLossInsuranceItem("B");
		}
		
		
		confirmLossVo.setConfirmOpinion(textVo.getDescription());//定损意见
		confirmLossVo.setEvaluationType(carMainVo.getCetainLossType());//定损类型
		//confirmLossVo.setIsPayBackForOthers("");//是否无责代赔
		confirmLossVo.setLabors(laborVos);//工时列表 
		confirmLossVo.setLossType(carMainVo.getIsTotalLoss());//损失类型
		PrpLRepairFactoryVo repairVo=null;
		if(carMainVo!=null && StringUtils.isNotBlank(carMainVo.getRepairFactoryCode())){
			 repairVo=repairFactoryService.findFactoryById(carMainVo.getRepairFactoryCode());
			if(repairVo==null){//解决历史数据问题
			 repairVo=repairFactoryService.findFactoryByCode(carMainVo.getRepairFactoryCode(), carMainVo.getRepairFactoryType());
			}
		   
		}
		if(repairVo!=null){
			confirmLossVo.setRepairFactoryCode(repairVo.getFactoryCode());//修理厂代码
		}else{
			confirmLossVo.setRepairFactoryCode(carMainVo.getRepairFactoryCode());
		}
		confirmLossVo.setRepairFactoryName(carMainVo.getRepairFactoryName());//修理厂名称
		confirmLossVo.setRepairFactoryType(carMainVo.getRepairFactoryType());//修理厂类型
		confirmLossVo.setSmallSpareparts(sparepartVos);//辅料列表
		confirmLossVo.setSpareparts(repartVos);//配件列表
		if(carMainVo.getSumRescueFee()!=null){
			confirmLossVo.setRescueFee(carMainVo.getSumRescueFee()+"");//施救费
		}
		
		
		//给报案信息赋值
		ReportVo reportVo=new ReportVo();//报案信息
		List<DataVo> dataVos=new ArrayList<DataVo>();//自定义数据
		if(prpLRegistVo!=null && StringUtils.isNotBlank(prpLRegistVo.getRegistNo())){
			reportVo.setContactPerson(prpLRegistVo.getLinkerName());//联系人
			reportVo.setContactTel(prpLRegistVo.getLinkerMobile());//联系人电话
			DriverVo driverVo2=new DriverVo();//报案阶段驾驶员信息
			driverVo2.setAddress(prpLCheckDriverVo.getUnitAddress());//驾驶员家庭住址
		    if(prpLCheckDriverVo.getDriverAge()!=null){
		    	driverVo2.setDriverAge(prpLCheckDriverVo.getDriverAge().toString());//年龄
			}
		    driverVo2.setDriverAllowedVehicleType(prpLCheckDriverVo.getIdentifyType());//证件类型
		    driverVo2.setDriverEducation("");//教育程度
		    driverVo2.setDriverGender(prpLCheckDriverVo.getDriverSex());//驾驶员性别
		    driverVo2.setDriverLicenseInstitution("");//颁证机关
		    driverVo2.setDriverTel(prpLCheckDriverVo.getLinkPhoneNumber());//联系电话
		    if(prpLCheckDriverVo.getAcceptLicenseDate()!=null){
		    	 driverVo2.setDrivingLicenseDate(this.DateFormatString(prpLCheckDriverVo.getAcceptLicenseDate()));//初次领证日期
		    }
		
			   driverVo2.setDrivingLicenseNum(prpLCheckDriverVo.getDrivingLicenseNo());//驾驶证号码 
		  
		 
			   driverVo2.setName(prpLCheckDriverVo.getDriverName());//驾驶员姓名
		 
		   
		    reportVo.setDriverVo(driverVo2);
		    reportVo.setEventAddress(prpLRegistVo.getDamageAddress());//出险地址
		    reportVo.setEventAddressType("1");//道路类型
		    if(prpLRegistVo.getDamageTime()!=null){
		    	 reportVo.setEventDate(this.DateFormatString(prpLRegistVo.getDamageTime()));//出险时间
		    }
		   
		    reportVo.setEventDescription(registExtVo.getDangerRemark());//出险经过
		    reportVo.setEventReason(prpLRegistVo.getDamageCode());//出险原因代码
		    if(StringUtils.isNotBlank(prpLRegistVo.getAccidentReason())){
		    	 reportVo.setEventReasonType(prpLRegistVo.getAccidentReason());//事故原因
		    }else{
		    	 reportVo.setEventReasonType("01");//追尾
		    }
		    reportVo.setEventResponsibility(prplcheckdutyVo.getIndemnityDuty());//事故责任
		    reportVo.setEventType(prpLCheckVo.getDamageTypeCode());//事故类型
		    reportVo.setInformant(prpLRegistVo.getReportorName());//报案人
		    //reportVo.setInformantIsDriver("");//报案人是否驾驶员
		   // reportVo.setInformantIsInsured("");//报案人否为被保险人
		    reportVo.setInformantTel(prpLRegistVo.getReportorPhone());//报案人电话
		    reportVo.setIsClosed("false");//是否结案
		    if("1".equals(registExtVo.getIsClaimSelf())){
		    	reportVo.setIsMutualCollisionSelfCompensation("true");//是否互碰自赔
		    }else{
		    	reportVo.setIsMutualCollisionSelfCompensation("false");//是否互碰自赔
		    }
		    if("1".equals(registExtVo.getIsOnSitReport())){
		    	reportVo.setIsScene("true");//是否现场报案
		    }else{
		    	reportVo.setIsScene("false");
		    }
		    //reportVo.setIsSimpleClaim("");//简易案件标识
		    reportVo.setMercyFlag(prpLRegistVo.getMercyFlag());//案件紧急程度
		    if(prpLRegistVo.getReportTime()!=null){
		    	 reportVo.setReportDate(this.DateFormatString(prpLRegistVo.getReportTime()));//报案日期	
		    }
		    //自定义说明此请求是来自哪个节点。1-报案，2-查勘，3-定损
		    DataVo dataVo=new DataVo();
		    dataVo.setKey("Stage");
		    dataVo.setValue(nodeFlag);
		    dataVos.add(dataVo);
		    reportVo.setDatas(dataVos);
		   
		}
		
		//给标的车辆信息赋值
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
		//vehicleVo1.setApprovedCapacity("");//核定载质量
		//vehicleVo1.setApprovedPassenger("");//核定载客
		vehicleVo1.setBrand("");//车辆品牌
		///vehicleVo1.setCarAge("");//车龄
		//vehicleVo1.setDisplacement("");//排量
		vehicleVo1.setDrivingArea("");//行驶区域
		vehicleVo1.setEngineNumber(prpLCItemCarVo1.getEngineNo());//发动机号
		if(prpLCItemCarVo1.getEnrollDate()!=null){
			vehicleVo1.setFirstRegistrationDate(this.DateFormatString(prpLCItemCarVo1.getEnrollDate()));//初登日期
		}
		
		vehicleVo1.setGasType("");//燃料类型
		vehicleVo1.setInsuredFlag("");//保险类型
		vehicleVo1.setInsurer(prpLCItemCarVo1.getInsurerCode());//承保公司
		//vehicleVo1.setIsImported("");//是否进口车
	    vehicleVo1.setIsMainCar("true");//是否标的车
		vehicleVo1.setManufacturer("");//制造商名称
		//vehicleVo1.setMileage("");//行驶公里数
		vehicleVo1.setModel(prpLCItemCarVo1.getBrandName());//车辆型号
		if(prpLCItemCarVo1.getPurchasePrice()!=null){
			vehicleVo1.setNewCarAmount(prpLCItemCarVo1.getPurchasePrice()+"");//新车购置价
		}
		
		vehicleVo1.setOwner(prpLCItemCarVo1.getCarOwner());//车主
		
		vehicleVo1.setRegistrationNumber(prpLCItemCarVo1.getLicenseNo());//车牌号
		
		
		//vehicleVo1.setSeatCount("");//座位数
		vehicleVo1.setTypeOfUsage(prpLCItemCarVo1.getUseKindCode());//使用性质
		vehicleVo1.setVehicleID(prpLCItemCarVo1.getItemCarId()+"");//车辆Id
		if(prpLCItemCarVo1.getActualValue()!=null){
			vehicleVo1.setVehiclePrice(prpLCItemCarVo1.getActualValue()+"");//车辆实际价值
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
				vehicleVo1.setVehicleType(lossCarInfoVo.getPlatformCarKindCode());//车辆种类
			}
		}
		vehicleVo1.setVin(prpLCItemCarVo1.getFrameNo());//vin码
		vehicleVo1.setNotificationNumber(lossCarInfoVo.getModelCode());
		//vehicleVo1.setYearOfProduction(this.DateFormatString(prpLCItemCarVo1.getMakeDate()));//生产年份
		
		
		//给三者车信息赋值
		RelatingVehicleVo relatingVehicleVo=new RelatingVehicleVo();
		VehicleVo vehicleVo3=new VehicleVo();//车辆信息
		//vehicleVo3.setApprovedCapacity("");//核定载质量
		//vehicleVo3.setApprovedPassenger("");//核定载客
		vehicleVo3.setBrand("");//车辆品牌
		//vehicleVo3.setCarAge("");//车龄
		//vehicleVo3.setDisplacement("");//排量
		vehicleVo3.setDrivingArea("");//行驶区域
		vehicleVo3.setEngineNumber(lossCarInfoVo.getEngineNo());//发动机号
		if(lossCarInfoVo.getEnrollDate()!=null){
			vehicleVo3.setFirstRegistrationDate(this.DateFormatString(lossCarInfoVo.getEnrollDate()));//初登日期
		}
		
		vehicleVo3.setGasType("");//燃料类型
		vehicleVo3.setInsuredFlag("");//保险类型
		vehicleVo3.setInsurer(lossCarInfoVo.getInsureComName());//承保公司
		//vehicleVo3.setIsImported("");//是否进口车
		vehicleVo3.setIsMainCar("false");//是否标的车
		vehicleVo3.setManufacturer("");//制造商名称
		//vehicleVo3.setMileage("");//行驶公里数
       if("3".equals(nodeFlag)){
    	   vehicleVo3.setModel(lossCarInfoVo.getBrandName());//车辆型号
		}else{
			vehicleVo3.setModel(prpLRegistCarLossVo.getBrandName());//车辆型号
		}
		
		//vehicleVo3.setNewCarAmount("");//新车购置价
		vehicleVo3.setOwner(lossCarInfoVo.getCarOwner());//车主
		if("3".equals(nodeFlag)){
			vehicleVo3.setRegistrationNumber(lossCarInfoVo.getLicenseNo());//车牌号
		}else{
			vehicleVo3.setRegistrationNumber(prpLRegistCarLossVo.getLicenseNo());//车牌号
		}
		
		
		
		//vehicleVo3.setSeatCount("");//座位数
		vehicleVo3.setTypeOfUsage("");//使用性质
		vehicleVo3.setVehicleID(lossCarInfoVo.getId()+"");//车辆Id
		//vehicleVo3.setVehiclePrice("");//车辆实际价值
		if("2".equals(nodeFlag)){
			vehicleVo3.setVehicleType(chekCarInfoVo.getPlatformCarKindCode());//车辆种类
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
					vehicleVo3.setVehicleType(lossCarInfoVo.getPlatformCarKindCode());//车辆种类
				}
			}
		}
		if("2".equals(nodeFlag)){
			vehicleVo3.setVin(chekCarInfoVo.getVinNo());//vin码
		}else{
			vehicleVo3.setVin(lossCarInfoVo.getVinNo());//vin码
		}
		if("2".equals(nodeFlag)){
			vehicleVo3.setNotificationNumber(chekCarInfoVo.getModelCode());
		}else{
			vehicleVo3.setNotificationNumber(lossCarInfoVo.getModelCode());
		}
		
		//vehicleVo3.setYearOfProduction("");//生产年份
		
		relatingVehicleVo.setConfirmLossVo(confirmLossVo);//三者车定损信息
		relatingVehicleVo.setVehicleVo(vehicleVo3);//三者车车辆信息
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
				baseVo.setCheckVo(checkVo);//查勘信息
			}
			baseVo.setClaimNumber(registNo);//报案号
			if(biVo!=null && biVo.getId()!=null){//商业险信息
			baseVo.setCommercialInsuranceVo(bIInfoVo);	
			}
			if(ciVo!=null && ciVo.getId()!=null){//交强险信息
			baseVo.setCompulsoryInsuranceVo(cIInfoVo);	
			}
			if("1".equals(lossCarType) && "3".equals(nodeFlag)){
			baseVo.setConfirmLossVo(confirmLossVo);//标的车车辆定损信息
			}
			baseVo.setInsuranceCompanyID("403");//贵公司ID由德联分配
			//baseVo.setIsPayBackForOthers("");//是否无责代赔
		
			baseVo.setRegistrationNumber(licenseNo);//车牌号
			baseVo.setReportVo(reportVo);//报案信息
			if("1".equals(lossCarType)){
			baseVo.setVehicleVo(vehicleVo1);//车辆信息
			}
			if("3".equals(lossCarType)){
			baseVo.setRelatingVehicles(relatingVehicles);//三者车信息
			}
			
			
			
		
		
		
		return baseVo;
	}
	/**
	 * 接口组装数据
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
		//获取超时时间
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
	            // 设置请求方式（GET/POST）    
	            httpUrlConn.setRequestMethod("POST"); 
	            httpUrlConn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
	            httpUrlConn.setConnectTimeout(Integer.valueOf(seconds) * 1000);
	            httpUrlConn.setReadTimeout(Integer.valueOf(seconds) * 1000);
		        
	            httpUrlConn.connect();    
	    
	            String outputStr =requestXML;
	            			
	            OutputStream outputStream = httpUrlConn.getOutputStream();  		        
	            // 当有数据需要提交时    
	            if (null != outputStr) {    
	                // 注意编码格式，防止中文乱码    outputStream.write
	                outputStream.write(outputStr.getBytes("utf-8"));    
	            }    
	    
	            // 将返回的输入流转换成字符串    
	            InputStream inputStream = httpUrlConn.getInputStream();    
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");    
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);    
	    
	            String str = null;    
	            while ((str = bufferedReader.readLine()) != null) {
	            	
	                buffer.append(str);    
	            } 
	            if (buffer.length() < 1) {
					throw new Exception("德联易控返回数据失败");
				}
	            bufferedReader.close();    
	            inputStreamReader.close();    
	            // 释放资源  
	            outputStream.flush();
	            outputStream.close();
	            inputStream.close();    
	            inputStream = null;    
	            httpUrlConn.disconnect(); 
	            responseXml=buffer.toString();
	            logger.info("返回报文--------》"+responseXml);
	        } catch (ConnectException ce) {
	        	throw new Exception("与德联易控连接失败，请稍候再试", ce);
	        } catch (Exception e) {
	        	logger.error("错误信息=========》",e);
	        	throw new Exception("读取德联易控返回数据失败", e);
	        	
	        } finally {
				logger.warn("接口({})调用耗时{}ms", urlStr, System.currentTimeMillis() - t1);
			}    
	        return responseXml;
	}

	/**
	 * 得联易控日志保存
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
	            logger.info("===============德联易控===========");
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
	 * 保存或更新德联易控信息   lossCarMainId--当时定损修改时，方便找到是在那条数据上进行的修改
	 */
	public void saveOrUpdateForPrplTestinfoMain(CECheckResultVo resultVo,SysUserVo userVo,String lossCarMainId,String nodeFlag) throws ParseException{
		//德联易控信息主表
		PrplTestinfoMainVo mainVo=new PrplTestinfoMainVo();
		//配件列表
		List<PrplPartsInfoVo> partsList=new ArrayList<PrplPartsInfoVo>();
		//工时列表
		List<PrplLaborInfoVo> laborsList=new ArrayList<PrplLaborInfoVo>();
		//欺诈风险列表
		List<PrplFraudriskInfoVo> fraudRiskList=new ArrayList<PrplFraudriskInfoVo>();
		//风险点列表
		List<PrplRiskpointInfoVo> riskPointList=new ArrayList<PrplRiskpointInfoVo>();
		//操作不规范列表
		List<PrplOperationInfoVo> operationList=new ArrayList<PrplOperationInfoVo>();
		//德联易控返回的车辆信息表
		ClaimVo claimVo=new ClaimVo();
		//价格合计Vo
		PriceSummaryVo summaryVo=new PriceSummaryVo();
		if(resultVo.getClaims()!=null && resultVo.getClaims().size()>0){
			 claimVo=resultVo.getClaims().get(0);
			 summaryVo=claimVo.getPriceSummaryVo();
		}
		
		
        //给德联易控信息主表赋值
		mainVo.setRegistNo(resultVo.getClaimNumber());//报案号
		mainVo.setVehicleRegistionNumber(claimVo.getVehicleRegistionNumber());//车牌号
		mainVo.setVehicleBrandName(claimVo.getVehicleBrandName());//品牌名称
		mainVo.setVehicleModelName(claimVo.getVehicleModelName());//车型名称
		mainVo.setVin(claimVo.getVin());//VIN码
		mainVo.setCheckEmployee(claimVo.getCheckEmployee());//查勘员
		mainVo.setConfirmlossEmployee(claimVo.getConfirmLossEmployee());//定损员
		mainVo.setRepairFactoryName(claimVo.getRepairFactoryName());//修理厂名称
		mainVo.setEventDate(this.StringFormatDate(claimVo.getEventDate()));//出险时间
		mainVo.setConfirmlossDate(this.StringFormatDate(claimVo.getConfirmLossDate()));//定损时间confirmLossDate
		mainVo.setCheckTime(this.StringFormatDate(claimVo.getCheckTime()));//案件检测时间
		mainVo.setLossType(claimVo.getIsMainCar());//损失方，1-标的，0-三者
		mainVo.setPartlossAmount(summaryVo.getPartTotalPrice());//配件定损总金额
		mainVo.setPartceAmount(summaryVo.getcEPartTotalPrice());//配件建议总金额
		mainVo.setPartsavingAmount(summaryVo.getSavingPartTotalPrice());//配件节省总金额
		mainVo.setRiskSavingAmount(summaryVo.getFraudRiskSavingPrice());//欺诈风险减损金额
		mainVo.setLaborlossAmount(summaryVo.getLaborTotalPrice());//工时定损总金额
		mainVo.setLaborceAmount(summaryVo.getcELaborTotalPrice());//工时建议总金额
		mainVo.setLaborsavingAmount(summaryVo.getSavingLaborTotalPrice());//工时节省总金额
		mainVo.setTotalPrice(summaryVo.getTotalPrice());//'定损金额总计：配件总定损金额+工时总定损金额';
		mainVo.setCetotalPrice(summaryVo.getcETotalPrice());//'CE建议金额总计：CE建议配件总金额+CE建议工时总金额-欺诈风险减损金额'; 
		mainVo.setSavingTotalPrice(summaryVo.getSavingTotalPrice());//'建议减损金额总计：建议配件减损总金额+建议工时减损总金额+欺诈风险减损金额'; 
		mainVo.setLossCarMainId(lossCarMainId);//定损主表Id
		mainVo.setComCode(userVo.getComCode());//机构代码
		mainVo.setCreateTime(new Date());//创建时间 
		mainVo.setCreateUser(userVo.getUserCode());//创建人员
		mainVo.setClaimResult(claimVo.getClaimResult());//检测结果
		mainVo.setNodeFlag(nodeFlag);//保存节点，1-报案，2-查勘，3-定损
		//给配件信息赋值
		if(claimVo.getParts()!=null && claimVo.getParts().size()>0){
			for(PartVo vo:claimVo.getParts()){
				PrplPartsInfoVo infoVo=new PrplPartsInfoVo();
				infoVo.setPriceType(vo.getPriceType());//修理厂类别名
				infoVo.setPartsName(vo.getName());//配件名称
				infoVo.setPartsPrice(vo.getPrice());//配件定损单价
				infoVo.setPartsCount(vo.getCount());//配件定损数量
				infoVo.setPartstotalPrice(vo.getTotalPrice());//配件定损总价
				infoVo.setCePrice(vo.getcEPrice());//CE建议单价
				infoVo.setCeCount(vo.getCount());//CE建议数量
				infoVo.setCetotalPrice(vo.getcETotalPrice());//CE建议总价
				infoVo.setSavingPrice(vo.getSavingPrice());//建议减损
				if(vo.getSavingDescsVo()!=null){
					infoVo.setSavingDescs(vo.getSavingDescsVo().getSavingDesc());//建议减损描述
				}
				infoVo.setCreateTime(new Date());//创建时间
				infoVo.setCreateUser(userVo.getUserCode());//创建人员
				partsList.add(infoVo);
				
			}
		}
		
		//给工时信息赋值
		if(claimVo.getLabors()!=null && claimVo.getLabors().size()>0){
			for(LaborResultVo vo:claimVo.getLabors()){
				PrplLaborInfoVo infoVo=new PrplLaborInfoVo();
				infoVo.setLaborItem(vo.getLaborItem());//项目名称
				infoVo.setLaborName(vo.getName());//配件名称
				infoVo.setLaborPrice(vo.getPrice());//定损单价
				infoVo.setLaborCount(vo.getCount());//定损工时
				infoVo.setLabortotalPrice(vo.getTotalPrice());//定损总价
				infoVo.setCePrice(vo.getcEPrice());//CE建议单价
				infoVo.setCeCount(vo.getCECount());//CE建议工时
				infoVo.setCetotalPrice(vo.getcETotalPrice());//CE建议总价
				if(vo.getSavingDescsVo()!=null){
					infoVo.setSavingDescs(vo.getSavingDescsVo().getSavingDesc());//建议减损描述
				}
				infoVo.setCreateTime(new Date());//创建时间
				infoVo.setCreateUser(userVo.getUserCode());//创建人员
				laborsList.add(infoVo);
			}
		}
		//给欺诈风险信息赋值
		if(claimVo.getFraudRisks()!=null && claimVo.getFraudRisks().size()>0){
			for(FraudRiskVo vo:claimVo.getFraudRisks()){
				PrplFraudriskInfoVo infoVo=new PrplFraudriskInfoVo();
				infoVo.setFactCode(vo.getFactCode());//规则编码
				infoVo.setFactName(vo.getFactName());//规则名称
				infoVo.setRiskDesc(vo.getRiskDesc());//风险描述
				infoVo.setSuggest(vo.getSuggest());//检测建议
				infoVo.setAccuracyRate(vo.getAccuracyRate());
				infoVo.setCreateTime(new Date());//创建时间
				infoVo.setCreateUser(userVo.getUserCode());//创建人员
				fraudRiskList.add(infoVo);
			}
		}
		
		//给风险信息赋值
		if(claimVo.getRiskPoints()!=null && claimVo.getRiskPoints().size()>0 ){
			for(RiskPointVo vo:claimVo.getRiskPoints()){
				PrplRiskpointInfoVo infoVo=new PrplRiskpointInfoVo();
				infoVo.setFactCode(vo.getFactCode());//规则编码
				infoVo.setFactName(vo.getFactName());//规则名称
				infoVo.setRiskDesc(vo.getRiskDesc());//风险描述
				infoVo.setSuggest(vo.getSuggest());//检测建议
				infoVo.setCreateTime(new Date());//创建时间
				infoVo.setCreateUser(userVo.getUserCode());//创建人员
				riskPointList.add(infoVo);
			}
		}
		//给操作信息赋值
		if(claimVo.getNonStandardOperations()!=null && claimVo.getNonStandardOperations().size()>0){
			for(NonStandardOperationVo vo:claimVo.getNonStandardOperations()){
				PrplOperationInfoVo infoVo=new PrplOperationInfoVo();
				infoVo.setFactCode(vo.getFactCode());//规则编码
				infoVo.setFactName(vo.getFactName());//规则名称
				infoVo.setRiskDesc(vo.getRiskDesc());//风险描述
				infoVo.setSuggest(vo.getSuggest());//检测建议
				infoVo.setCreateTime(new Date());//创建时间
				infoVo.setCreateUser(userVo.getUserCode());//创建人员
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
		
		
		    ///保存
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
	 * 时间转换方法
	 * String 类型转换 Date类型
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
	 * 时间转换方法
	 * Date 类型转换 String类型
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
