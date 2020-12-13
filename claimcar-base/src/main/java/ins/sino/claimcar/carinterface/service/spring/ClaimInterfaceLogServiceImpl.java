 
/******************************************************************************
 * CREATETIME : 2016年9月21日 上午10:33:33
 ******************************************************************************/
package ins.sino.claimcar.carinterface.service.spring;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.platform.saa.vo.SaaFactorPowerVo;
import ins.platform.saa.vo.SaaUserPowerVo;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SqlParamVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carchild.service.CarchildService;
import ins.sino.claimcar.carchild.vo.PrplcarchildregistcancleVo;
import ins.sino.claimcar.carchild.vo.RevokeBodyVo;
import ins.sino.claimcar.carchild.vo.RevokeInfoReqVo;
import ins.sino.claimcar.carchild.vo.RevokeTaskInfoVo;
import ins.sino.claimcar.carinterface.po.ClaimInterfaceLog;
import ins.sino.claimcar.carinterface.service.CaseLeapHNService;
import ins.sino.claimcar.carinterface.service.CaseLeapService;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.ciitc.service.AccidentService;
import ins.sino.claimcar.ciitc.service.CompeInterfaceService;
import ins.sino.claimcar.claim.service.ClaimKindHisService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.ClaimToReinsuranceService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.DlclaimTaskService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.vo.PrpLClaimCancelVo;
import ins.sino.claimcar.claim.vo.PrpLClaimKindHisVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrplReplevyMainVo;
import ins.sino.claimcar.claimcarYJ.service.ClaimcarYJService;
import ins.sino.claimcar.claimjy.service.PrpLRegistToLossService;
import ins.sino.claimcar.claimyj.service.YjInteractionService;
import ins.sino.claimcar.endcase.service.EndCaseService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.genilex.dlossService.ClaimJxService;
import ins.sino.claimcar.genilex.service.ClaimToGenilexService;
import ins.sino.claimcar.hnbxrest.service.ReceiveResultService;
import ins.sino.claimcar.hnbxrest.vo.ReceiveauditingresultVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.middlestagequery.service.ClaimToMiddleStageOfCaseService;
import ins.sino.claimcar.mobilecheck.service.SendMsgToMobileService;
import ins.sino.claimcar.moblie.logUtil.MobileRequestType;
import ins.sino.claimcar.other.service.AccountInfoService;
import ins.sino.claimcar.other.service.AcheckService;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.vo.PrpLAcheckMainVo;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLAssessorMainVo;
import ins.sino.claimcar.other.vo.PrpLCheckFeeVo;
import ins.sino.claimcar.pay.service.PadPayPubService;
import ins.sino.claimcar.pay.service.PadPayService;
import ins.sino.claimcar.pay.service.RecPayService;
import ins.sino.claimcar.newpayment.service.ClaimToNewPaymentService;
import ins.sino.claimcar.payment.service.ClaimToPaymentDetailService;
import ins.sino.claimcar.payment.service.ClaimToPaymentService;
import ins.sino.claimcar.recloss.service.RecLossService;
import ins.sino.claimcar.recloss.vo.PrpLRecLossVo;
import ins.sino.claimcar.regist.service.FounderCustomService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;
import ins.sino.claimcar.selfHelpClaimCar.service.SelfHelpClaimResultService;
import ins.sino.claimcar.subrogation.service.PlatLockDubboService;
import ins.sino.claimcar.subrogation.service.SubrogationHandleService;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;
import ins.sino.claimcar.trafficplatform.service.CheckToWarnService;
import ins.sino.claimcar.trafficplatform.service.ClaimToWarnService;
import ins.sino.claimcar.trafficplatform.service.RegistToCarRiskPaltformService;
import ins.sino.claimcar.trafficplatform.service.SdpoliceCaseService;
import ins.sino.claimcar.trafficplatform.service.SdpoliceService;
import ins.sino.claimcar.trafficplatform.service.SendEndCaseToEWPlatformService;
import ins.sino.claimcar.trafficplatform.service.SendVClaimToEWPlatformService;
import ins.sino.claimcar.trafficplatform.service.SzpoliceClaimInfoService;
import ins.sino.claimcar.trafficplatform.service.SzpoliceRegistService;
import ins.sino.claimcar.trafficplatform.vo.CarRiskRegistBasePartReqVo;
import ins.sino.claimcar.trafficplatform.vo.CarRiskRegistReqVo;
import ins.sino.claimcar.verifyclaim.service.VerifyClaimService;

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
import com.alibaba.fastjson.JSONObject;

/**
 * <pre></pre>
 * @author ★Luwei
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("claimInterfaceLogService")
public class ClaimInterfaceLogServiceImpl implements ClaimInterfaceLogService {

	private Logger logger = LoggerFactory.getLogger(ClaimInterfaceLogServiceImpl.class);

	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private BaseDaoService baseDaoService;
	@Autowired
	ScheduleService scheduleService;
	@Autowired
	ClaimTaskService claimTaskService;
	@Autowired
	PadPayPubService padPayService;
	@Autowired
	RecPayService recPayService;
	@Autowired
	RegistQueryService registService;
	@Autowired
	PlatLockDubboService platLockService;
	@Autowired
	private RecLossService recLossService;
	@Autowired
	private AssessorService assessorService;
	@Autowired
	CompensateTaskService compesateService;
	@Autowired
	FounderCustomService founderService;
	@Autowired
	ClaimToReinsuranceService reinsuranceService;
	@Autowired
	ClaimToPaymentService claimToPaymentService;
	@Autowired
	ClaimToPaymentDetailService claimToPaymentDetailService;
	@Autowired
	ClaimToNewPaymentService claimToNewPaymentService;
	@Autowired
	SubrogationHandleService subrogationService;
	@Autowired
	private ClaimKindHisService claimKindHisService;
	@Autowired
	CaseLeapService caseLeapService;
	@Autowired
	CaseLeapHNService caseLeapHNService;
	@Autowired
	EndCaseService endCaseService;
	@Autowired
	AccountInfoService accountInfoService;
	@Autowired
	private SendMsgToMobileService sendMsgToMobileService;
	@Autowired
	ReceiveResultService  receiveResultService;
	@Autowired
	CarchildService carchildService;
	@Autowired
	SdpoliceService sdpoliceService;
	@Autowired
	SdpoliceCaseService sdpoliceCaseService;

	@Autowired
	InterfaceAsyncService interfaceAsyncService;
	@Autowired
	ClaimInterfaceLogSaveService claimInterfaceLogSaveService;
	@Autowired
	CheckToWarnService checkToWarnService;
	@Autowired
	ClaimToWarnService claimToWarnService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
    ClaimToGenilexService claimToGenilexService;	
	@Autowired
	CheckTaskService checkTaskService;	
    @Autowired
    ClaimJxService claimJxService;
	@Autowired
	SendVClaimToEWPlatformService sendVClaimToEWPlatformService;
	@Autowired
	SendEndCaseToEWPlatformService sendEndCaseToEWPlatformService;
    @Autowired
    RegistToCarRiskPaltformService registToCarRiskPaltformService;
	@Autowired
	SzpoliceRegistService szpoliceRegistService;
	@Autowired
	SzpoliceClaimInfoService szpoliceClaimInfoService;
	@Autowired
	PrpLRegistToLossService prpLRegistToLossService;
	@Autowired
	YjInteractionService yjInteractionService;
	@Autowired
	LossCarService lossCarService;
    @Autowired
    AccidentService accidentService;
    
    @Autowired
    CompeInterfaceService compeInterfaceService;
    
	@Autowired
	SelfHelpClaimResultService selfHelpClaimResultService;
	@Autowired
	ClaimcarYJService claimcarYJService;
	
	@Autowired
	PadPayService padPayToVatService;
	@Autowired
	AcheckService acheckService;
	@Autowired
	DlclaimTaskService dlclaimTaskService;
	@Autowired
	VerifyClaimService verifyClaimService;
	@Autowired
	private ClaimToMiddleStageOfCaseService claimToMiddle;

	@Autowired
	private SaaUserPowerService saaUserPowerService;
	
	
	@Override
	public ClaimInterfaceLogVo save(ClaimInterfaceLogVo logVo) {
		return claimInterfaceLogSaveService.commitSave(logVo);
	}
	
	@Override
	public ClaimInterfaceLogVo updateLog(ClaimInterfaceLogVo logVo) {
		return claimInterfaceLogSaveService.updateLog(logVo);
	}
	
	@Override
	public void saveAll(List<ClaimInterfaceLogVo> logVoList) {
		
		claimInterfaceLogSaveService.commitSaveAll(logVoList);
	}

	@Override
	public List<ClaimInterfaceLogVo> findLogByRegistNo(String registNo){
		List<ClaimInterfaceLogVo> logVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		List<ClaimInterfaceLog> logPoList = 
				databaseDao.findAll(ClaimInterfaceLog.class,queryRule);
		if(logPoList!=null&&logPoList.size()>0){
			logVoList = Beans.copyDepth().from(logPoList).toList(ClaimInterfaceLogVo.class);
		}
		return logVoList;
	}
	
	@Override
	public Map<String,List<ClaimInterfaceLogVo>> searchByRegistNoAndType(String registNo,List<String> businessType){
		Map<String,List<ClaimInterfaceLogVo>> map = new HashMap<String, List<ClaimInterfaceLogVo>>();
		List<ClaimInterfaceLogVo> logVoList = new ArrayList<ClaimInterfaceLogVo>();
		List<ClaimInterfaceLogVo> registList = new ArrayList<ClaimInterfaceLogVo>();
		List<ClaimInterfaceLogVo> checkList = new ArrayList<ClaimInterfaceLogVo>();
		List<ClaimInterfaceLogVo> lossList = new ArrayList<ClaimInterfaceLogVo>();
		List<ClaimInterfaceLogVo> endCaseList = new ArrayList<ClaimInterfaceLogVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		queryRule.addIn("businessType", businessType);
		queryRule.addEqual("status", "0");
		List<ClaimInterfaceLog> logPoList = databaseDao.findAll(ClaimInterfaceLog.class,queryRule);
		if(logPoList!=null&&logPoList.size()>0){
			for(ClaimInterfaceLog logPo:logPoList){
				ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
				Beans.copy().from(logPo).to(logVo);
				if(BusinessType.GENILEX_Regist.name().equals(logVo.getBusinessType())){
					registList.add(logVo);
				}else if(BusinessType.GENILEX_Check.name().equals(logVo.getBusinessType())){
					checkList.add(logVo);
				}else if(BusinessType.GENILEX_Dloss.name().equals(logVo.getBusinessType())){
					lossList.add(logVo);
				}else if(BusinessType.GENILEX_EndCase.name().equals(logVo.getBusinessType())){
					endCaseList.add(logVo);
				}
			}
			if(registList.size()>0){
				map.put(BusinessType.GENILEX_Regist.name(), registList);
			}
			if(checkList.size()>0){
				map.put(BusinessType.GENILEX_Check.name(), checkList);
			}
			if(lossList.size()>0){
				map.put(BusinessType.GENILEX_Dloss.name(), lossList);
			}
			if(endCaseList.size()>0){
				map.put(BusinessType.GENILEX_EndCase.name(), endCaseList);
			}
		}
		return map;
	}

	@Override
	public ResultPage<ClaimInterfaceLogVo> findLogForPage(ClaimInterfaceLogVo queryVo,Integer start,Integer length) throws Exception {
		long total = 0;
		List<ClaimInterfaceLogVo> logVoList = new ArrayList<ClaimInterfaceLogVo>();
		//输入完整的报案号或者立案号才进行数据查询，否则直接返回空值
		if(StringUtils.isNotBlank(queryVo.getRegistNo())|| StringUtils.isNotBlank(queryVo.getClaimNo())) {
			// 定义参数list，ps：执行查询时需要转换成object数组
			// hql查询语句
			SqlJoinUtils sqlUtil=new SqlJoinUtils();
			sqlUtil.append(" FROM ClaimInterfaceLog log where 1=1");
			// 报案号
			if(StringUtils.isNotBlank(queryVo.getRegistNo())){
				sqlUtil.andEquals(queryVo,"log","registNo");
			}
			if(StringUtils.isNotBlank(queryVo.getClaimNo())){
				sqlUtil.andEquals(queryVo,"log","claimNo");
			}
			// 机构
			if(StringUtils.isNotBlank(queryVo.getComCode())){
				sqlUtil.andEquals(queryVo,"log","comCode");
			}
			// 业务名称
			if(StringUtils.isNotBlank(queryVo.getBusinessName())){
				sqlUtil.andEquals(queryVo,"log","businessName");
			}

		if(StringUtils.isNotBlank(queryVo.getCompensateNo())){
			sqlUtil.andEquals(queryVo,"log","compensateNo");
//			queryString.append(" AND log.compensateNo like ? ");
//			paramValues.add("%"+queryVo.getCompensateNo()+"%");
		}

			// 状态
			if(StringUtils.isNotBlank(queryVo.getStatus())){
				sqlUtil.andEquals(queryVo,"log","status");
			}
			//报案号和立案号全部录入，则忽略操作日期
			if (StringUtils.isBlank(queryVo.getRegistNo())
					|| StringUtils.isBlank(queryVo.getClaimNo())) {
				//操作日期
				sqlUtil.andDate(queryVo,"log","requestTime");
			}

			//29962 理赔默认菜单问题 接口交互记录 查询的内容按照机构权限过滤，如果没有配置机构权限，应该查不到内容
			String userCode = queryVo.getUserCode();
			SaaUserPowerVo userPowerVo = saaUserPowerService.findUserPower(userCode);
			Map<String, List<SaaFactorPowerVo>> factorPowerMap = userPowerVo.getPermitFactorMap();
			if (!"0000000000".equals(userCode)) {
				if (null != factorPowerMap && !factorPowerMap.isEmpty()) {
					List<SaaFactorPowerVo> factorPowerVoList = factorPowerMap.get("FF_COMCODE");
					if(factorPowerVoList == null || factorPowerVoList.size() == 0){
						return new ResultPage<ClaimInterfaceLogVo>(start,length,total,logVoList);
					}else{
						logger.info("接口交互记录查询权限判断=========");
						boolean isMainCom = false;
						for(SaaFactorPowerVo factorPowerVo:factorPowerVoList){
							String comCode = factorPowerVo.getDataValue();
							if("0001%".equals(comCode)){//排除总公司
								isMainCom = true;
								break;
							}
						}
						if(!isMainCom){
							sqlUtil.append(" AND (");
							for(int i = 0; i < factorPowerVoList.size(); i++){
								SaaFactorPowerVo factorPowerVo = factorPowerVoList.get(i);
								String comCode = factorPowerVo.getDataValue();
								if(i > 0){
									sqlUtil.append("  OR  ");
								}
								sqlUtil.append("EXISTS(select 1 from  PrpLRegist r where log.registNo=r.registNo and  ");
								sqlUtil.append(this.genSubPowerHql(factorPowerVo,"comCode","r"));// 得到alias. fieldCode = ?);
								sqlUtil.addParamValue(comCode);
								sqlUtil.append(" )");
							}
							sqlUtil.append(" )");
						}
					}
				} else {
					return new ResultPage<ClaimInterfaceLogVo>(start,length,total,logVoList);
				}
			}

		sqlUtil.append(" Order By log.createTime desc ");

		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
			logger.info("===========SQL:==================" + sql);
		// 执行查询
		Page<ClaimInterfaceLog> page = databaseDao.findPageByHql(ClaimInterfaceLog.class, sql, ( start/length+1 ), length, values);

			// 对象转换
			List<ClaimInterfaceLog> result = page.getResult();
			if(result!=null&&result.size()>0){
				logVoList = Beans.copyDepth().from(result).toList(ClaimInterfaceLogVo.class);
				for(ClaimInterfaceLogVo vo : logVoList){
					if(!StringUtils.isBlank(vo.getErrorMessage())){
						vo.setErrorMessage(vo.getErrorMessage().replaceAll("\""," "));
					}
				}
			}	
			total = page.getTotalCount();
		} else{
			throw new RuntimeException("请输入报案号或者立案号");
		}
	
		ResultPage<ClaimInterfaceLogVo> resultPage = new ResultPage<ClaimInterfaceLogVo>(start,length,total,logVoList);
		return resultPage;
	}

	@Override
	public ClaimInterfaceLogVo findLogByPK(Long id) {
		ClaimInterfaceLogVo logVo = null;
		ClaimInterfaceLog logPo = databaseDao.findByPK(ClaimInterfaceLog.class,id);
		if(logPo != null){
			logVo = Beans.copyDepth().from(logPo).to(ClaimInterfaceLogVo.class);
		}
		return logVo;
	}
	
	@Override
	public void changeInterfaceLog(Long id){
		claimInterfaceLogSaveService.commitUpdate(id);
	}
	
	@Override
	public String interfaceLogReupload(Long logId,SysUserVo userVo) throws Exception{
		String returnMsg = "补传成功！";
		ClaimInterfaceLogVo logVo = findLogByPK(logId);
		if(logVo == null){
			throw new IllegalArgumentException("未找到该条记录！");
		}
		String businessType = logVo.getBusinessType();
		String bussNo = logVo.getCompensateNo();// 业务号
		if(BusinessType.Founder_carRegist.toString().equals(businessType)){// 方正(车险报案)
			founderService.carRegistToFounder(bussNo);
		}else if(BusinessType.Founder_scheduleInfo.toString().equals(businessType)){// 方正(调度信息)
			PrpLScheduleTaskVo taskVo = 
					scheduleService.findScheduleTaskVoByPK(Long.parseLong(bussNo));
			founderService.scheduleInfoToFounder(taskVo,null);
		}else if(BusinessType.Founder_PolicyRelation.toString().equals(businessType)){// 方正(保单关联)
//			founderService.PolicyRelationToFounder(registNo,conPolicyNo,isConnect);
		}else if(BusinessType.Founder_registCancel.toString().equals(businessType)){// 方正(报案注销)
			founderService.registCancelToFounder(logVo.getRegistNo());
		}else if(BusinessType.Founder_cancelDfloss.toString().equals(businessType)){// 方正(定损注销)
			PrpLScheduleTaskVo taskVo = 
					scheduleService.findScheduleTaskVoByPK(Long.parseLong(bussNo));
			founderService.cancelDflossTaskToFounder(taskVo);
		}else if(BusinessType.Reinsurance_verify.toString().equals(businessType)){// 再保（核赔）
			PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(logVo.getClaimNo());
			PrpLCompensateVo compeVo = compesateService.findCompByPK(bussNo);
			reinsuranceService.TransDataForCompensateVo(claimVo,compeVo);
		}else if(BusinessType.Reinsurance_vatBack.toString().equals(businessType)){// vat回写送再保
			PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(logVo.getClaimNo());
			verifyClaimService.sendVatBackSumAmountNTToReins(claimVo.getRegistNo(), claimVo.getRiskCode());
		}else if(BusinessType.Reinsurance_claim.toString().equals(businessType)){// 再保（立案）
			PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(logVo.getClaimNo());
			List<PrpLClaimKindHisVo> kindHisVoList = 
					claimKindHisService.findKindHisVoList(claimVo.getClaimNo(), "1");
			reinsuranceService.TransDataForClaimVo(claimVo,kindHisVoList,logVo);
		}else if(BusinessType.Reinsurance_cancel.toString().equals(businessType)){// 再保（结案）
			PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(logVo.getClaimNo());
			reinsuranceService.TransDataForReinsCaseVo(logVo.getFlag(),claimVo,logVo);
		}else if(BusinessType.Reinsurance_cancel.toString().equals(businessType)){// 再保（结案）
			PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(logVo.getClaimNo());
			reinsuranceService.TransDataForReinsCaseVo(logVo.getFlag(),claimVo,logVo);
		}else if(BusinessType.Reinsurance_assessor.toString().equals(businessType)){// 公估费送再保
			PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(logVo.getClaimNo());
			PrpLCompensateVo prpLCompensateVo = compesateService.findCompByClaimNo(logVo.getClaimNo());
			PrpLAssessorFeeVo assFeeVo = assessorService.findAssessorFeeVoByComp(logVo.getRegistNo(), logVo.getCompensateNo());
			reinsuranceService.asseFeeToReinsOut(claimVo, prpLCompensateVo, assFeeVo);
		}else if(BusinessType.Payment_padPay.toString().equals(businessType)){// 垫付送收付
			claimToNewPaymentService.padPayToNewPayment(padPayService.queryPadPay(logVo.getRegistNo(),bussNo));
		}else if(BusinessType.Payment_compe.toString().equals(businessType)){// 理算送收付
			// claimToPaymentDetailService.compensateToPayment(compesateService.queryCompByPK(bussNo));
			claimToNewPaymentService.compensateToNewPayment(compesateService.queryCompByPK(bussNo));
		}else if(BusinessType.Payment_prePay.toString().equals(businessType)){// 预付送收付
			claimToNewPaymentService.prePayToNewPayment(compesateService.queryCompByPK(bussNo));
		}else if(BusinessType.Payment_recPay.toString().equals(businessType)){// 追偿送收付
			PrplReplevyMainVo replevyMainVo = 
					recPayService.findPrplReplevyMainVoByPK(Long.parseLong(bussNo));
			// claimToPaymentService.recPayToPayment(replevyMainVo);
			claimToNewPaymentService.recPayToNewPayment(replevyMainVo);
		}else if(BusinessType.Payment_recLoss.toString().equals(businessType)){// 损余回收送收付
			PrpLRecLossVo recLossVo = recLossService.findRecLossByRecLossId(bussNo);
			// claimToPaymentService.recLossToPayment(recLossVo);
			claimToNewPaymentService.recLossToNewPayment(recLossVo);
		}else if(BusinessType.Payment_assessor.toString().equals(businessType)){// 公估费送收付
			//PrpLAssessorFeeVo feeVo = assessorService.findAssessorFeeVoByComp(bussNo);
			PrpLAssessorMainVo assessorMainVo = assessorService.findAsseMainVoByCompNo(bussNo);
               List<PrpLAssessorFeeVo>  feeVoList = assessorMainVo.getPrpLAssessorFees();
			assToPaymentAll(assessorMainVo,feeVoList,logVo);

		}else if(BusinessType.Payment_checkFee.toString().equals(businessType)){// 查勘费送收付
			PrpLAcheckMainVo prpLAcheckMainVo=acheckService.findAcheckMainVoByCompNo(bussNo);
               List<PrpLCheckFeeVo> feeVoList=prpLAcheckMainVo.getPrpLCheckFees();
			checkFeeToPaymentAll(prpLAcheckMainVo,feeVoList,logVo);
        }else if(BusinessType.HNQC_endCas.toString().equals(businessType)){// 理赔结果通知河南快赔系统
			receiveResultService.receivecpsresult(logVo.getRegistNo(), logVo.getFlag(), userVo);
			this.changeInterfaceLog(logVo.getId());// 将原来这条日志状态改为已补传
		}else if(BusinessType.HNQC_PhotoVerify.toString().equals(businessType)){// 定损照片审核结果通知河南快赔系统
			String requestJson = logVo.getRequestXml();
			JSONObject jsonObject = JSONObject.parseObject(requestJson);
			ReceiveauditingresultVo vo = new ReceiveauditingresultVo();
	    	vo.setCasenumber(jsonObject.getString("casenumber"));
	    	vo.setInscaseno(jsonObject.getString("inscaseno"));
	    	vo.setDatatype(jsonObject.getString("datatype"));
	    	vo.setIsqualify(jsonObject.getString("isqualify"));
	    	vo.setImagetype(jsonObject.getString("imagetype"));
	    	vo.setIsass(jsonObject.getString("isass"));
	    	vo.setOperateuser(userVo.getUserCode());
			receiveResultService.receiveauditingresult(vo);
			this.changeInterfaceLog(logVo.getId());// 将原来这条日志状态改为已补传
		}else if(BusinessType.HNQC_DLossResult.toString().equals(businessType)){// 定损结果通知河南快赔系统
			receiveResultService.receivegusunresult(logVo.getRegistNo(), userVo);
			this.changeInterfaceLog(logVo.getId());// 将原来这条日志状态改为已补传
		}else if(BusinessType.CT_caseCancleNoPass.toString().equals(businessType)){// 车童网注销申请不通过接口补传
			String sign=carchildService.sendCaseInfoToCarchild(logVo.getRegistNo(),"CT", userVo);
			PrplcarchildregistcancleVo cancleVo=carchildService.findPrplcarchildregistcancleVoByRegistNo(logVo.getRegistNo());
			if(sign.equals("1")){
				cancleVo.setHandleDate(new Date());
				cancleVo.setHandleUser(userVo.getUserName());
				cancleVo.setExamineRusult("0");
				cancleVo.setStatus("1");
				carchildService.updatePrplcarchildregistcancle(cancleVo);
			}
		}else if(BusinessType.MTA_caseCancleNoPass.toString().equals(businessType)){// 民太安注销申请不通过接口补传
			String sign=carchildService.sendCaseInfoToCarchild(logVo.getRegistNo(),"MTA", userVo);
			PrplcarchildregistcancleVo cancleVo=carchildService.findPrplcarchildregistcancleVoByRegistNo(logVo.getRegistNo());
			if(sign.equals("1")){
				cancleVo.setHandleDate(new Date());
				cancleVo.setHandleUser(userVo.getUserName());
				cancleVo.setExamineRusult("0");
				cancleVo.setStatus("1");
				carchildService.updatePrplcarchildregistcancle(cancleVo);
			}
		}else if(BusinessType.Payment_dw.toString().equals(businessType)){// 追偿确认送收付
			PrpLPlatLockVo platLockVo = platLockService.findPlatLockVo(logVo.getRegistNo(),bussNo);
			PrpLRegistVo registVo = registService.findByRegistNo(logVo.getRegistNo());
			subrogationService.reCoveryPayToPayment(platLockVo,registVo,userVo);
		}else if(BusinessType.Payment_qf.toString().equals(businessType)){// 清付确认送收付
			PrpLPlatLockVo platLockVo = platLockService.findPlatLockVo(logVo.getRegistNo(),bussNo);
			subrogationService.qsConfirmToPayment(platLockVo,userVo);
		}else if("Invoice".equals(businessType)){ // 营改增 推送发票
			boolean result = false;
			if(bussNo.startsWith("Y")){
				result = claimToPaymentService.pushPreCharge(bussNo,logVo.getFlag());// 补送时参数为 计算书号 和 费用序列号
			}else if(bussNo.startsWith("D")){
				PrpLPadPayMainVo padPayMainVo = padPayToVatService.findPadPayMainByCompNo(bussNo);
				result = claimToPaymentService.pushPadPay(padPayMainVo,logVo.getFlag());
			}else{
				result = claimToPaymentService.pushCharge(bussNo,logVo.getFlag());
			}
		}else if(BusinessType.AssessorFee_Invoice.toString().equals(businessType)){ // 公估费 送发票
			PrpLAssessorFeeVo assFeeVo = assessorService.findAssessorFeeVoByComp(logVo.getRegistNo(), logVo.getCompensateNo());
			// 补送时，公估费机构应传输为审核人机构(原日志机构即可)
			if(logVo != null){
				userVo.setComCode(logVo.getComCode());
				userVo.setUserCode(logVo.getCreateUser());
			}
			boolean result = claimToPaymentService.pushAssessorFee(assFeeVo, userVo);
			//claimToPaymentService.pushCharge(bussNo);
		}else if(BusinessType.CheckFee_Invoice.toString().equals(businessType)){ // 查勘费 送发票
			PrpLCheckFeeVo prpLCheckFeeVo=acheckService.findCheckFeeVoByComp(logVo.getRegistNo(), logVo.getCompensateNo());
			// 补送时，查勘费机构应传输为审核人机构(原日志机构即可)
			if(logVo != null){
				userVo.setComCode(logVo.getComCode());
				userVo.setUserCode(logVo.getCreateUser());
			}
			boolean result = claimToPaymentService.pushCheckFee(prpLCheckFeeVo, userVo);
			//claimToPaymentService.pushCharge(bussNo);
		}else if(BusinessType.GZIS_claim.name().equals(businessType)){ // 立案送贵州消保
			PrpLClaimVo prpLClaimVo = claimTaskService.findClaimVoByClaimNo(logVo.getClaimNo());
			caseLeapService.claimToGZ(prpLClaimVo, userVo);
		}else if(BusinessType.HNIS_claim.name().equals(businessType)){ // 立案送河南消保
			PrpLClaimVo prpLClaimVo = claimTaskService.findClaimVoByClaimNo(logVo.getClaimNo());
			caseLeapHNService.claimToHN(prpLClaimVo,userVo);
		}else if(BusinessType.DL_CarInfo.name().equals(businessType)){//新德联易控
			String Qurl=SpringProperties.getProperty("YX_CeQUrl");
			dlclaimTaskService.SendControlExpert(logVo.getRegistNo(),userVo,logVo.getPolicyNo(),logVo.getServiceType(), logVo.getOperateNode(),Qurl);
		}else if(BusinessType.GZIS_endCase.name().equals(businessType)){ // 结案送贵州消保
			PrpLEndCaseVo prpLEndCaseVo = endCaseService.queryEndCaseVo(logVo.getRegistNo(),logVo.getClaimNo());
			caseLeapService.endCaseToGZ(prpLEndCaseVo,userVo.getUserCode());
		}else if(BusinessType.HNIS_endCase.name().equals(businessType)){ // 结案送河南消保
			PrpLEndCaseVo prpLEndCaseVo = endCaseService.queryEndCaseVo(logVo.getRegistNo(),logVo.getClaimNo());
			caseLeapHNService.endCaseToHN(prpLEndCaseVo,userVo.getUserCode());
		}else if(BusinessType.ModAccount.name().equals(businessType)){ // 账号信息修改送收付
			//returnMsg=accountInfoService.reUpload(logVo.getCompensateNo(), logVo.getRequestXml(), userVo);
			returnMsg=accountInfoService.reUploadNewPayment(logVo.getCompensateNo(), logVo.getRequestXml(),userVo,logVo.getRemark());
		}else if(BusinessType.AssessorFee_BackTicket.name().equals(businessType)){// 公估费退票修改送收付
			returnMsg=accountInfoService.reUploadOfAssessorFeeBackTickit(logVo.getCompensateNo(),logVo.getRegistNo(), userVo);
		}else if(BusinessType.CheckFee_BackTicket.name().equals(businessType)){// 查勘费退票修改送收付
			returnMsg=accountInfoService.reUploadOfCheckFeeBackTickit(logVo.getCompensateNo(),logVo.getRegistNo(), userVo);
		}else if(BusinessType.Payment_backPrePay.name().equals(businessType)){ // 预付更新送收付
			PrpLCompensateVo compeVo = compesateService.findCompByPK(logVo.getCompensateNo());
			claimToPaymentDetailService.updatePrePayToPayment(compeVo);
		}else if(MobileRequestType.sendMsgToMobile_Mobile.name().equals(businessType)){ // 理赔通知移动端 接口补送
			String url = SpringProperties.getProperty("MClaimPlatform_URL_IN");
		    sendMsgToMobileService.sendMsgToMobile(logVo,url);
        }else if(BusinessType.MTA_claimCancelRestore.name().equals(businessType) ||
BusinessType.CT_claimCancelRestore.name().equals(businessType)){// 理赔送民太安(立案注销恢复推)，理赔送车童网(立案注销恢复推)
            interfaceAsyncService.sendClaimCancelRestoreCTorMTA(logVo.getRegistNo(),userVo);
        }else if(BusinessType.MTA_claimCancel.name().equals(businessType)||
BusinessType.CT_claimCancel.name().equals(businessType)){// 理赔送民太安(立案注销推),理赔送车童网(立案注销推)
            RevokeInfoReqVo reqVo = ClaimBaseCoder.xmlToObj(logVo.getRequestXml(), RevokeInfoReqVo.class);
            RevokeBodyVo revokeBodyVo = reqVo.getBody();
            List<RevokeTaskInfoVo> revokeTaskInfoVos = revokeBodyVo.getRevokeTaskInfos();
            RevokeTaskInfoVo revokeTaskInfoVo = revokeTaskInfoVos.get(0);
            PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
            pClaimCancelVo.setApplyReason(revokeTaskInfoVo.getReason());
            PrpLWfTaskVo wfTaskVo = new PrpLWfTaskVo();
            wfTaskVo.setRegistNo(logVo.getRegistNo());
            interfaceAsyncService.organizationAndSendCTorMTA(pClaimCancelVo,wfTaskVo,userVo);
        }else if(BusinessType.CT_registCancel.name().equals(businessType)||
BusinessType.MTA_registCancel.name().equals(businessType)){// 理赔送车童网(报案注销推)
            carchildService.registCancel(logVo,userVo);
        }else if(BusinessType.CT_cancelDfloss.name().equals(businessType)||
BusinessType.MTA_cancelDfloss.name().equals(businessType)){// 理赔送车童网(定损注销定损)
            carchildService.scheduleDefLossCancel(logVo,userVo);
        }else if(BusinessType.CT_handOver.name().equals(businessType)||
BusinessType.MTA_handOver.name().equals(businessType)){// 平级移交
            carchildService.handOverCancelSendCtOrMta(logVo,userVo);
        }else if(BusinessType.CT_scheduleChange.name().equals(businessType)||
                BusinessType.MTA_scheduleChange.name().equals(businessType)){
            carchildService.scheduleChangeSendCtOrMta(logVo,userVo);
        }else if(BusinessType.CT_Regist.name().equals(businessType)||
BusinessType.MTA_Regist.name().equals(businessType)){// 车童报案接口
            carchildService.registSendCtOrMta(logVo,userVo);
		}else if(BusinessType.GENILEX_Regist.name().equals(businessType)){// 精励联讯接口
			List<PrpLCMainVo> prpLCMainVoList = new ArrayList<PrpLCMainVo>();
	        prpLCMainVoList = policyViewService.getPolicyAllInfo(logVo.getRegistNo());
	        PrpLRegistVo registVo = registService.findByRegistNo(logVo.getRegistNo());
	        if(prpLCMainVoList != null && prpLCMainVoList.size() > 0){
	        	try{
	        		interfaceAsyncService.sendRegistForGenilex(registVo, userVo, prpLCMainVoList.get(0));
	        	} catch(Exception e) {
					logger.info("报案送精励联讯接口失败："+e.getMessage());
	        	}
	        }
		}else if(BusinessType.GENILEX_Check.name().equals(businessType)){// 查勘送精励联讯
        	PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(logVo.getRegistNo());
        	claimToGenilexService.checkToGenilex(checkVo, Long.valueOf(logVo.getCompensateNo()));
		}else if(BusinessType.GENILEX_EndCase.name().equals(businessType)){// 结案送精励联讯
        	PrpLEndCaseVo endCaseVo = endCaseService.queryEndCaseVo(logVo.getRegistNo(), logVo.getClaimNo());
        	PrpLCompensateVo compensateVo = compesateService.findCompByPK(logVo.getCompensateNo());
        	claimToGenilexService.endCaseToGenilex(endCaseVo, compensateVo,logVo.getRemark());
		}else if(BusinessType.GENILEX_Dloss.name().equals(businessType)){// 精励联讯(定损接口推送)
        	claimJxService.sendDlossInfor(logVo.getRegistNo(),logVo.getFlag(), userVo);
		}else if(BusinessType.SDEW_vLoss.name().equals(businessType)){// 山东预警（定核损）
        	PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(logVo.getRegistNo(), logVo.getPolicyNo());
        	sdpoliceService.sendDlossRegister(prpLCMainVo, userVo);
		}else if(BusinessType.SDEW_claimCancel.name().equals(businessType)){// 山东预警（案件注销）
        	PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(logVo.getRegistNo(), logVo.getPolicyNo());
        	sdpoliceCaseService.sendCaseCancleRegister(prpLCMainVo, logVo.getServiceType(),logVo.getCompensateNo(), userVo);
		}else if(BusinessType.SDEW_reOpen.name().equals(businessType)){// 山东预警（案件重开）
        	sdpoliceCaseService.sendReopenCaseRegister(logVo.getRegistNo(), logVo.getPolicyNo(),userVo);
        }else if(BusinessType.SDEW_check_CI.name().equals(businessType) ||
BusinessType.SDEW_check_BI.name().equals(businessType)){// 山东预警（查勘登记）
        	checkToWarnService.checkToWarn(Long.valueOf(logVo.getClaimNo()), Long.valueOf(logVo.getCompensateNo())
        			,logVo.getPolicyNo());
        }else if(BusinessType.SDEW_claim_CI.name().equals(businessType) ||
BusinessType.SDEW_claim_BI.name().equals(businessType)){// 山东预警（立案登记）
        	claimToWarnService.claimToWarn(logVo.getRegistNo(),logVo.getPolicyNo());
        }else if(BusinessType.SDEW_riskImg_CI.name().equals(businessType) ||
BusinessType.SDEW_riskImg_BI.name().equals(businessType)){// 山东预警（重复/虚假案件标记）
        	if("1".equals(logVo.getFlag())){
        		claimToWarnService.sendFalseCaseToEWByRegist(null, logVo.getRegistNo(), logVo.getPolicyNo());
        	}else{
        		claimToWarnService.sendFalseCaseToEWByCancel(logVo.getCompensateNo());
        	}
		}else if(BusinessType.SDEW_vClaim.name().equals(businessType)){// 山东风险预警系统(理算核赔登记)
        	PrpLCompensateVo compensateVo = compesateService.findCompByPK(logVo.getCompensateNo());
        	sendVClaimToEWPlatformService.SendVClaimToEWPlatform(compensateVo);
		}else if(BusinessType.SDEW_endCase.name().equals(businessType)){// 山东风险预警系统(结案登记)
        	PrpLEndCaseVo endVo = endCaseService.queryEndCaseVo(logVo.getRegistNo(),logVo.getClaimNo());
        	sendEndCaseToEWPlatformService.SendEndCaseToEWPlatform(endVo);
		}else if(BusinessType.SDEW_regist.name().equals(businessType)){// 山东风险预警系统(报案登记)
            CarRiskRegistReqVo reqVo = ClaimBaseCoder.xmlToObj(logVo.getRequestXml(), CarRiskRegistReqVo.class);
            CarRiskRegistBasePartReqVo basePartReqVoreqVo = reqVo.getBody().getCarRiskRegistBasePartReqVo();
            registToCarRiskPaltformService.sendRegistToCarRiskPlatformRe(logVo.getRegistNo(),basePartReqVoreqVo.getPolicyNo(),userVo);
		}else if(BusinessType.SDEW_imgUpload.name().equals(businessType)){// 山东风险预警系统(图片上传)
            interfaceAsyncService.carRiskImagesUpdate(logVo.getRegistNo(),userVo,logVo.getRemark(),logVo.getCompensateNo());
		}else if(BusinessType.SZReg_CI.name().equals(businessType)){// 深圳警保交强报案信息上传
			szpoliceRegistService.reportCaseUpload(logVo.getId().toString());
		}else if(BusinessType.SZReg_BI.name().equals(businessType)){// 深圳警保商业报案信息上传
			szpoliceRegistService.reportCaseUpload(logVo.getId().toString());
		}else if(BusinessType.SZClaim_CI.name().equals(businessType)){// 深圳警保交强理赔信息上传
			szpoliceClaimInfoService.settleClaimReUpload(logVo.getClaimNo(),"2",null);
		}else if(BusinessType.SZClaim_BI.name().equals(businessType)){// 深圳警保商业理赔信息上传
			szpoliceClaimInfoService.settleClaimReUpload(logVo.getClaimNo(),"2",null);
        }else if(BusinessType.GDEW_regist.name().equals(businessType)){// 广东车辆数据服务平台数据采集（理赔报案登记）
			CarRiskRegistReqVo reqVo = ClaimBaseCoder.xmlToObj(logVo.getRequestXml(),CarRiskRegistReqVo.class);
			CarRiskRegistBasePartReqVo basePartReqVoreqVo = reqVo.getBody().getCarRiskRegistBasePartReqVo();
			registToCarRiskPaltformService.sendRegistToCarRiskPlatformRe(logVo.getRegistNo(),basePartReqVoreqVo.getPolicyNo(),userVo);
		}else if(BusinessType.GDEW_claim_CI.name().equals(businessType)||BusinessType.GDEW_claim_BI.name().equals(businessType)){// 广东车辆数据服务平台数据采集（理赔立案登记）
			claimToWarnService.claimToWarn(logVo.getRegistNo(),logVo.getPolicyNo());
		}else if(BusinessType.GDEW_vClaim.name().equals(businessType)){// 广东车辆数据服务平台数据采集(理算核赔登记)
			PrpLCompensateVo compensateVo = compesateService.findCompByPK(logVo.getCompensateNo());
			sendVClaimToEWPlatformService.SendVClaimToEWPlatform(compensateVo);
		}else if(BusinessType.GDEW_check_CI.name().equals(businessType)||BusinessType.GDEW_check_BI.name().equals(businessType)){// 广东车辆数据服务平台数据采集（查勘登记）
			checkToWarnService.checkToWarn(Long.valueOf(logVo.getClaimNo()),Long.valueOf(logVo.getCompensateNo()),logVo.getPolicyNo());
		}else if(BusinessType.GDEW_vLoss.name().equals(businessType)){// 广东车辆数据服务平台数据采集（定核损）
			PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(logVo.getRegistNo(),logVo.getPolicyNo());
			sdpoliceService.sendDlossRegister(prpLCMainVo,userVo);
		}else if(BusinessType.GDEW_endCase.name().equals(businessType)){// 广东车辆数据服务平台数据采集(结案登记)
			PrpLEndCaseVo endVo = endCaseService.queryEndCaseVo(logVo.getRegistNo(),logVo.getClaimNo());
			sendEndCaseToEWPlatformService.SendEndCaseToEWPlatform(endVo);
		}else if(BusinessType.GDEW_claimCancel.name().equals(businessType)){// 广东车辆数据服务平台数据采集（案件注销）
			PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(logVo.getRegistNo(),logVo.getPolicyNo());
			sdpoliceCaseService.sendCaseCancleRegister(prpLCMainVo,logVo.getServiceType(),logVo.getCompensateNo(),userVo);
		}else if(BusinessType.GDEW_reOpen.name().equals(businessType)){// 广东车辆数据服务平台数据采集（案件重开）
			sdpoliceCaseService.sendReopenCaseRegister(logVo.getRegistNo(),logVo.getPolicyNo(),userVo);
		}else if(BusinessType.HNEW_regist.name().equals(businessType)){// 河南平台（报案登记）
			CarRiskRegistReqVo reqVo = ClaimBaseCoder.xmlToObj(logVo.getRequestXml(),CarRiskRegistReqVo.class);
			CarRiskRegistBasePartReqVo basePartReqVoreqVo = reqVo.getBody().getCarRiskRegistBasePartReqVo();
			registToCarRiskPaltformService.sendRegistToCarRiskPlatformRe(logVo.getRegistNo(),basePartReqVoreqVo.getPolicyNo(),userVo);
		}else if(BusinessType.HNEW_claim_CI.name().equals(businessType)||BusinessType.HNEW_claim_BI.name().equals(businessType)){// 河南平台（立案）
			claimToWarnService.claimToWarn(logVo.getRegistNo(),logVo.getPolicyNo());
		}else if(BusinessType.HNEW_check_CI.name().equals(businessType)||BusinessType.HNEW_check_BI.name().equals(businessType)){// 河南（查勘登记）
			checkToWarnService.checkToWarn(Long.valueOf(logVo.getClaimNo()),Long.valueOf(logVo.getCompensateNo()),logVo.getPolicyNo());
		}else if(BusinessType.HNEW_vLoss.name().equals(businessType)){// 河南（定核损）
			PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(logVo.getRegistNo(),logVo.getPolicyNo());
			sdpoliceService.sendDlossRegister(prpLCMainVo,userVo);
		}else if(BusinessType.HNEW_vClaim.name().equals(businessType)){// 河南平台(理算核赔登记)
			PrpLCompensateVo compensateVo = compesateService.findCompByPK(logVo.getCompensateNo());
			sendVClaimToEWPlatformService.SendVClaimToEWPlatform(compensateVo);
		}else if(BusinessType.HNEW_endCase.name().equals(businessType)){// 河南平台(结案登记)
			PrpLEndCaseVo endVo = endCaseService.queryEndCaseVo(logVo.getRegistNo(),logVo.getClaimNo());
			sendEndCaseToEWPlatformService.SendEndCaseToEWPlatform(endVo);
		}else if(BusinessType.HNEW_claimCancel.name().equals(businessType)){// 河南平台（案件注销）
			PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(logVo.getRegistNo(),logVo.getPolicyNo());
			sdpoliceCaseService.sendCaseCancleRegister(prpLCMainVo,logVo.getServiceType(),logVo.getCompensateNo(),userVo);
		}else if(BusinessType.HNEW_reOpen.name().equals(businessType)){// 河南平台（案件重开）
			sdpoliceCaseService.sendReopenCaseRegister(logVo.getRegistNo(),logVo.getPolicyNo(),userVo);
		}else if(BusinessType.JY_Regist.name().equals(businessType)){
			// 报案接口补送
			prpLRegistToLossService.sendRegistXmlData(logVo.getRegistNo());
		}else if(BusinessType.CIITC_Regist.name().equals(businessType)){//事故信息查询接口
        	accidentService.findAccidentInfoByRegistNo(logVo.getRegistNo(), userVo);
        }else if(BusinessType.CIITC_Compe.name().equals(businessType)){
        	if("01".equals(logVo.getFlag()) || "03".equals(logVo.getFlag())){
        		PrpLRegistVo registVo = registService.findByRegistNo(logVo.getRegistNo());
        		compeInterfaceService.reqByRegist(registVo, userVo, logVo.getFlag());
        	}else if("02".equals(logVo.getFlag()) || "02".equals(logVo.getFlag())){
        		PrpLEndCaseVo endCaseVo = endCaseService.queryEndCaseVo(logVo.getRegistNo(), logVo.getClaimNo());
        		compeInterfaceService.reqByEndCase(endCaseVo, userVo);
        	}else if("05".equals(logVo.getFlag())){
        		PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(logVo.getClaimNo());
        		compeInterfaceService.reqByCancel(claimVo, userVo);
        	}
        }else if(BusinessType.SELFCLAIM_CORE_002.name().equals(businessType)){//自助理赔（理赔结果通知接口）
        	selfHelpClaimResultService.sendSelfHelpClaimResult(logVo.getRegistNo(),userVo,logVo.getFlag(),logVo.getOperateNode(),logVo.getPolicyNo());
        }else if(BusinessType.YJ_DLCHK.name().equals(businessType)){
			//阳杰汽配复勘新增接口补送
			PrpLDlossCarMainVo lossCarMainVo = lossCarService.findLossCarMainById(Long.parseLong(logVo.getCompensateNo()));
			userVo.setUserCode(logVo.getCreateUser());
			userVo.setPhone(logVo.getRemark());
			userVo.setMobile(logVo.getRemark());
			yjInteractionService.sendDlChkInfoService(lossCarMainVo, userVo);
		}else if(BusinessType.YJ_VLoss.name().equals(businessType)){
			//阳杰汽配核损接口补送
			PrpLDlossCarMainVo lossCarMainVo = lossCarService.findLossCarMainById(Long.parseLong(logVo.getCompensateNo()));
			userVo.setUserCode(logVo.getCreateUser());
			userVo.setPhone(logVo.getRemark());
			userVo.setMobile(logVo.getRemark());
			yjInteractionService.sendVLossInfoService(lossCarMainVo, userVo);
		}else if(BusinessType.YJ_claimAdd.name().equals(businessType)){
			//阳杰询价新增接口补送
			if(StringUtils.isNotBlank(logVo.getFlag())){
				claimcarYJService.claimcarYJAskPriceAdd(Long.valueOf(logVo.getFlag()),userVo);
			}		
		}else if("MiddleStageMessage".equals(businessType)){
			if(StringUtils.isNotBlank(logVo.getOperateNode()) && StringUtils.isNotBlank(logVo.getRegistNo())){
				claimToMiddle.middleStageQuery(logVo.getRegistNo(), logVo.getOperateNode());
			}
		}else{
			throw new IllegalArgumentException("未找到对应的任务类型！businessType："+businessType);
		}
		return returnMsg;
	}

	/*公估费送收付  失败案件 按计算书号批量补送 并把错误日志更新为已补送 */
	private void assToPaymentAll(PrpLAssessorMainVo mainVo,List<PrpLAssessorFeeVo>  feeVoList,ClaimInterfaceLogVo logVo) throws Exception{
	    String compensateNo = "";
	    String comCode = logVo.getComCode();
	    String businessType = logVo.getBusinessType();
	    ClaimInterfaceLogVo logVoNew =null;
	    for(PrpLAssessorFeeVo feeVo : feeVoList){
	        compensateNo = feeVo.getCompensateNo();
	        if(StringUtils.isNotBlank(compensateNo)){
	            logVoNew = findLogVoByCompeAndNode(businessType,comCode,compensateNo);
				if(logVoNew==null){
					// 无日志案件 或者多条日志案件
	                // claimToPaymentDetailService.assessorToPayment(mainVo,feeVo);
	                claimToNewPaymentService.assessorToNewPayment(mainVo, feeVo);
	            }else{
					if("0".equals(logVoNew.getStatus())){
						// 失败案件
	                	// claimToPaymentDetailService.assessorToPayment(mainVo,feeVo);
						claimToNewPaymentService.assessorToNewPayment(mainVo, feeVo);
	                }
	            }
	        }
	    }
	}
	
	/** 查勘费送收付  失败案件 按计算书号批量补送 并把错误日志更新为已补送 */
	private void checkFeeToPaymentAll(PrpLAcheckMainVo mainVo,List<PrpLCheckFeeVo>  feeVoList,ClaimInterfaceLogVo logVo) throws Exception{
	    String compensateNo = "";
	    String comCode = logVo.getComCode();
	    String businessType = logVo.getBusinessType();
	    ClaimInterfaceLogVo logVoNew =null;
	    for(PrpLCheckFeeVo feeVo : feeVoList){
	        compensateNo = feeVo.getCompensateNo();
	        if(StringUtils.isNotBlank(compensateNo)){
	            logVoNew = findLogVoByCompeAndNode(businessType,comCode,compensateNo);
				if(logVoNew==null){
					// 无日志案件 或者多条日志案件
	                // claimToPaymentDetailService.checkFeeToPayment(mainVo, feeVo);
					claimToNewPaymentService.checkFeeToNewPayment(mainVo, feeVo);
	            }else{
					if("0".equals(logVoNew.getStatus())){
						// 失败案件
	                	// claimToPaymentDetailService.checkFeeToPayment(mainVo,feeVo);
						claimToNewPaymentService.checkFeeToNewPayment(mainVo, feeVo);
	                }
	            }
	        }
	    }
	}
	
	private ClaimInterfaceLogVo findLogVoByCompeAndNode(String bussineType ,String comCode ,String compensateNo){
	    ClaimInterfaceLogVo logVo = null;
	    QueryRule queryRule = QueryRule.getInstance();
	    queryRule.addEqual("businessType",bussineType);
	   // queryRule.addEqual("comCode",comCode);
	    queryRule.addEqual("compensateNo",compensateNo);
	    List<ClaimInterfaceLog> logVoList = databaseDao.findAll(ClaimInterfaceLog.class,queryRule);
	    if(logVoList != null && logVoList.size() == 1){
	        logVo = new ClaimInterfaceLogVo();
	        Beans.copy().from(logVoList.get(0)).to(logVo);
	    }
	   QueryRule rule= QueryRule.getInstance();
	    return logVo;
	    
	}

	
	public List<ClaimInterfaceLogVo> findLogByRequestTime(Date requestTime,String businessType,String status,String errorCode){
		List<ClaimInterfaceLogVo> logVoList = new ArrayList<ClaimInterfaceLogVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addBetween("requestTime", requestTime,new Date());
		queryRule.addEqual("businessType", businessType);
		queryRule.addEqual("status", status);
		if(errorCode!=null){
			queryRule.addNotEqual("errorCode", errorCode);
		}
		List<ClaimInterfaceLog> logPoList = databaseDao.findAll(ClaimInterfaceLog.class,queryRule);
		if(logPoList!=null && logPoList.size()>0){
			logVoList = Beans.copyDepth().from(logPoList).toList(ClaimInterfaceLogVo.class);
		}
		return logVoList;
	}

	/**
	 * 获取指定业务类型接口成功请求次数
	 * @param registNo
	 * @param businessType
	 * @param compensateNo
	 * @return
	 */
	@Override
	public int getBusinessTypeInterfaceRequestTimes(String registNo,String businessType,String compensateNo) throws Exception{
		List<Object> paramValues = new ArrayList<Object>();
		StringBuffer queryString = new StringBuffer(" select count(1) from claimuser.claiminterfacelog l WHERE l.status = '1'  ");
		if(!registNo.isEmpty()){
			queryString.append(" and l.registno = ?");
			paramValues.add(registNo.trim());
		}
		if(!businessType.isEmpty()){
			queryString.append(" and l.businesstype = ?");
			paramValues.add(businessType.trim());
		}
		if(!compensateNo.isEmpty()){
			queryString.append(" and l.compensateno = ?");
			paramValues.add(compensateNo.trim());
		}
	
		Object singleObj = baseDaoService.findListBySql(queryString.toString(),paramValues.toArray()).get(0);
		BigDecimal countValue= (BigDecimal)singleObj;
		return Integer.parseInt(countValue.toString()) ;
		
	}
	
	
	/**
	 * 获取已决送再保接口成功请求次数
	 * @param registNo
	 * @param compensateNo
	 * @return
	 */
	public int getReinsuranceInterfaceRequestTimes(String registNo,String compensateNo) throws Exception{
		List<Object> paramValues = new ArrayList<Object>();
		StringBuffer queryString = new StringBuffer(" select count(1) from claimuser.claiminterfacelog l WHERE l.status = '1' and l.businesstype in ('Reinsurance_verify','Reinsurance_vatBack','Reinsurance_washTransaction') ");
		if(!registNo.isEmpty()){
			queryString.append(" and l.registno = ?");
			paramValues.add(registNo.trim());
		}
		if(!compensateNo.isEmpty()){
			queryString.append(" and l.compensateno = ?");
			paramValues.add(compensateNo.trim());
		}
	
		Object singleObj = baseDaoService.findListBySql(queryString.toString(),paramValues.toArray()).get(0);
		BigDecimal countValue= (BigDecimal)singleObj;
		return Integer.parseInt(countValue.toString()) ;
	}

	private String genSubPowerHql(SaaFactorPowerVo factorPowerVo,String fieldCode,String alias) {

		SqlParamVo subPowerSqlVo = new SqlParamVo();
		String dataOper = factorPowerVo.getDataOper().toLowerCase();
		if("=".equals(dataOper)||"<".equals(dataOper)||">".equals(dataOper)||"<=".equals(dataOper)||">=".equals(dataOper)||"like".equals(dataOper)){// 不同操作符不同处理
			subPowerSqlVo.getSql().append(alias).append(".").append(fieldCode).append(" ").append(dataOper).append(" ? ");
		}
		return subPowerSqlVo.getSql().toString();

	}

}
