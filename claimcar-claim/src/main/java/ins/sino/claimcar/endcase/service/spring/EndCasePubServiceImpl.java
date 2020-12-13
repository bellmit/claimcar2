/******************************************************************************
 * CREATETIME : 2016年4月5日 上午10:49:04
 ******************************************************************************/
package ins.sino.claimcar.endcase.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.service.CodeTranService;
import ins.framework.utils.Beans;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carinterface.service.CaseLeapHNService;
import ins.sino.claimcar.carinterface.service.CaseLeapService;
import ins.sino.claimcar.claim.service.ClaimSummaryService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.service.OtherInterfaceAsyncService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrpsmsMessageVo;
import ins.sino.claimcar.endcase.po.PrpLEndCase;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.endcase.service.EndCaseService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.endcase.vo.PrpLReCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.genilex.service.ClaimToGenilexService;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.other.service.AcheckService;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.service.MsgModelService;
import ins.sino.claimcar.other.service.SendMsgService;
import ins.sino.claimcar.other.vo.SendMsgParamVo;
import ins.sino.claimcar.other.vo.SysMsgModelVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.reopencase.po.PrpLReCase;
import ins.sino.claimcar.sms.service.SmsService;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 结案接口实现类
 * 
 * @author ★Luwei
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("endCasePubService")
public class EndCasePubServiceImpl implements EndCasePubService {

    private static Logger log = LoggerFactory.getLogger(EndCasePubServiceImpl.class);
	@Autowired
	DatabaseDao databaseDao;

	@Autowired
	EndCaseService endCaseService;

	@Autowired
	ClaimTaskService claimTaskService;

	@Autowired
	WfTaskHandleService wfTaskHandleService;

	@Autowired
	ClaimSummaryService claimSummaryService;
	@Autowired
	AssessorService assessorService;
	
	@Autowired
	CaseLeapHNService caseLeapHNService;
	
	@Autowired
	CaseLeapService caseLeapService;
	
	@Autowired
	RegistQueryService registQueryService;
	
	@Autowired
	PolicyViewService policyViewService;
	
	@Autowired
	CodeTranService codeTranService;
	
	@Autowired
	RegistService registService;
	
	@Autowired
	CompensateTaskService compensateTaskService;
	
	@Autowired
	ManagerService managerService;
	
	@Autowired
	SendMsgService sendMsgService;
	
	@Autowired
	SmsService smsService;
	
	@Autowired
	MsgModelService msgModelService;
	
	@Autowired
	OtherInterfaceAsyncService otherInterfaceAsyncService;
	@Autowired
	AcheckService acheckService;
	@Autowired
	CompensateService compensateService;
	

	@Override
	public List<PrpLEndCaseVo> queryAllByRegistNo(String registNo) {
		List<PrpLEndCaseVo> endCaseVos = null;
		QueryRule queryRule = QueryRule.getInstance();
		Assert.notNull(registNo,"registNo must have value.");
		queryRule.addEqual("registNo",registNo);
		queryRule.addDescOrder("endCaseDate");
		List<PrpLEndCase> endCasePos = databaseDao.findAll(PrpLEndCase.class,queryRule);
		if(endCasePos!=null&&endCasePos.size()>0){
			endCaseVos = Beans.copyDepth().from(endCasePos).toList(PrpLEndCaseVo.class);
		}
		return endCaseVos;
	}

	@Override
	public PrpLEndCaseVo findEndCaseByPK(Long endCaseId,String endCaseNo) {
		PrpLEndCaseVo endCaseVo = null;
		PrpLEndCase endCasePo = new PrpLEndCase();
		if(StringUtils.isNotEmpty(endCaseNo)){
			QueryRule queryRule = QueryRule.getInstance();
			queryRule.addEqual("endCaseNo",endCaseNo);
			endCasePo = databaseDao.findUnique(PrpLEndCase.class,queryRule);
		}else{
			endCasePo = databaseDao.findByPK(PrpLEndCase.class,endCaseId);
		}
		endCaseVo = Beans.copyDepth().from(endCasePo).to(PrpLEndCaseVo.class);
		return endCaseVo;
	}

	/**
	 * autoEndCase-自动结案 1，回写 立案..（结案号） 2，生成归档号.....
	 * @throws ParseException 
	 */
	@Override
	public PrpLEndCaseVo autoEndCase(PrpLWfTaskVo wfTaskVo,PrpLCompensateVo compeVo) {
		String userCode = wfTaskVo.getTaskInUser();
		SysUserVo userVo = new SysUserVo();
		userVo.setUserCode(userCode);
		String registNo = wfTaskVo.getRegistNo();
		String calimNo = compeVo.getClaimNo();
		String compeNo = compeVo.getCompensateNo();

		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		// queryRule.addEqual("claimNo",claimNo);
		queryRule.addEqual("compensateNo",compeNo);

		Long endId = endCaseService.saveEndCase(compeVo,userCode);
		// 生成归档号
		PrpLEndCase endCase = null;
		List<PrpLEndCase> endCasePos = databaseDao.findAll(PrpLEndCase.class,queryRule);
		if(endCasePos!=null&&endCasePos.size()>0&&!"CompeWfZero".equals(compeNo)){
			endCase = endCasePos.get(0);
		}else{
			endCase = databaseDao.findByPK(PrpLEndCase.class,endId);
		}
		
		//回写理算表结案号y
		if(!"CompeWfZero".equals(endCase.getCompensateNo())){
			PrpLCompensateVo prplcompensateVo=compensateTaskService.findCompByPK(endCase.getCompensateNo());
			if(prplcompensateVo!=null){
				prplcompensateVo.setCaseNo(endCase.getEndCaseNo());
				compensateTaskService.writeBackCompesate(prplcompensateVo);
			}
		}
		// 回写立案
		PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(calimNo);
		Date date = new Date();
		claimVo.setAutoEndCaseFlag("1");
		claimVo.setCaseNo(endCase.getEndCaseNo());
		claimVo.setEndCaseTime(date);
		claimVo.setEndCaserCode(userCode);
		claimVo.setUpdateTime(date);
		claimVo.setUpdateUser(userCode);
		claimTaskService.claimWirteBack(claimVo);
		//结刷新立案轨迹表
		claimTaskService.calcClaimKindHisByEndCase(calimNo);
		//claimSummary表更新
		claimSummaryService.updateByEndCase(compeVo);
		
		//回写公估任务表
		assessorService.saveEndCaseTimeToAssessor(registNo, date);
		//回写查勘任务表
		acheckService.saveEndCaseTimeToAcheck(registNo, date);
		PrpLEndCaseVo endCaseVo = new PrpLEndCaseVo(); 
		Beans.copy().from(endCase).to(endCaseVo);
		
		//结案送中保信
		otherInterfaceAsyncService.reqByEndCase(endCaseVo, userVo);
		
		/*try {*/
			//结案送河南销保
//			caseLeapHNService.endCaseToHN(endCaseVo, userCode);
			otherInterfaceAsyncService.endCaseToHN(endCaseVo, userCode);
			//结案送贵州消保
//			caseLeapService.endCaseToGZ(endCaseVo, userCode);
			otherInterfaceAsyncService.endCaseToGZ(endCaseVo, userCode);
			
/*		} catch (Exception e) {
		   // Logger
		    //new IllegalArgumentException(paramString)
			throw  new RuntimeException(e);
		}*/
		
			
		//送精励联讯
		otherInterfaceAsyncService.endCaseToGenilex(endCaseVo,compeVo,wfTaskVo.getTaskId().toString());

		
		return endCaseVo;
	}

	/**
	 * 【【平安联盟对接使用】】
	 * autoEndCase-自动结案 1，回写 立案..（结案号） 2，生成归档号.....
	 * @throws ParseException
	 */
	@Override
	public PrpLEndCaseVo autoEndCaseForPingAn(PrpLWfTaskVo wfTaskVo,PrpLCompensateVo compeVo, Date endCaseDate) {
		String userCode = wfTaskVo.getTaskInUser();
		SysUserVo userVo = new SysUserVo();
		userVo.setUserCode(userCode);
		String registNo = wfTaskVo.getRegistNo();
		String calimNo = compeVo.getClaimNo();
		String compeNo = compeVo.getCompensateNo();

		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("compensateNo",compeNo);

		Long endId = endCaseService.saveEndCaseForPingAn(compeVo,userCode, endCaseDate);
		// 生成归档号
		PrpLEndCase endCase = null;
		List<PrpLEndCase> endCasePos = databaseDao.findAll(PrpLEndCase.class,queryRule);
		if(endCasePos!=null&&endCasePos.size()>0){
			endCase = endCasePos.get(0);
		}else{
			endCase = databaseDao.findByPK(PrpLEndCase.class,endId);
		}

		//回写理算表结案号y
		PrpLCompensateVo prplcompensateVo=compensateTaskService.findCompByPK(endCase.getCompensateNo());
		if(prplcompensateVo!=null){
			prplcompensateVo.setCaseNo(endCase.getEndCaseNo());
			compensateTaskService.writeBackCompesate(prplcompensateVo);
		}
		// 零赔付计算书（本次赔款+本次费用 = 0）的支付时间回写为结案时间
		BigDecimal sumPay = prplcompensateVo.getSumPaidAmt().add(prplcompensateVo.getSumPaidFee());
		if(sumPay.compareTo(BigDecimal.ZERO)==0){
			compensateService.updateCompPayTime(compeVo,endCase.getEndCaseDate(), CodeConstants.PayStatus.PAID);
		}

		// 回写立案
		PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(calimNo);
		Date date = new Date();
		claimVo.setAutoEndCaseFlag("1");
		claimVo.setCaseNo(endCase.getEndCaseNo());
		claimVo.setEndCaseTime(date);
		claimVo.setEndCaserCode(userCode);
		claimVo.setUpdateTime(date);
		claimVo.setUpdateUser(userCode);
		claimTaskService.claimWirteBack(claimVo);
		//结刷新立案轨迹表
		claimTaskService.calcClaimKindHisByEndCase(calimNo);
		//claimSummary表更新
		claimSummaryService.updateByEndCase(compeVo);

		//回写查勘任务表
		acheckService.saveEndCaseTimeToAcheck(registNo, date);
		PrpLEndCaseVo endCaseVo = new PrpLEndCaseVo();
		Beans.copy().from(endCase).to(endCaseVo);

		/*//结案送中保信
		otherInterfaceAsyncService.reqByEndCase(endCaseVo, userVo);

		//结案送河南销保
		otherInterfaceAsyncService.endCaseToHN(endCaseVo, userCode);
		//结案送贵州消保
		otherInterfaceAsyncService.endCaseToGZ(endCaseVo, userCode);
		//送精励联讯
		otherInterfaceAsyncService.endCaseToGenilex(endCaseVo,compeVo,wfTaskVo.getTaskId().toString());*/

		return endCaseVo;
	}

	public PrpLEndCaseVo findEndCaseByClaimNo(String claimNo) {
		PrpLEndCaseVo endCaseVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("claimNo", claimNo);
		queryRule.addDescOrder("endCaseDate");
		List<PrpLEndCase> endCasePo = databaseDao.findAll(PrpLEndCase.class,
				queryRule);
		if (endCasePo != null && endCasePo.size() > 0) {
			endCaseVo = Beans.copyDepth().from(endCasePo.get(0))
					.to(PrpLEndCaseVo.class);
		}
		return endCaseVo;
	}

	@Override
	public List<String> findPrpLReCaseVoList(Map<String, String> queryMap) {
		List<String> claimNoList = new ArrayList<String>();
		QueryRule queryRule = QueryRule.getInstance();
		for(String key:queryMap.keySet()){
			String value = queryMap.get(key);
			if(StringUtils.isNotBlank(value)){
				queryRule.addEqual(key,value);
			}
		}
		List<PrpLReCase> prpLReCaseList = databaseDao.findAll(PrpLReCase.class,queryRule);
		if(prpLReCaseList!=null&&prpLReCaseList.size()>0){
			for(PrpLReCase prpLReCase:prpLReCaseList){
				claimNoList.add(prpLReCase.getClaimNo());
			}
		}
		return claimNoList;
	}

	@Override
	public List<PrpLReCaseVo> findReCaseListByClaimNo(String claimNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("claimNo",claimNo);
		List<PrpLReCase> reCaseList = databaseDao.findAll(PrpLReCase.class,queryRule);
		
		List<PrpLReCaseVo> reCaseVoList = Beans.copyDepth().from(reCaseList).toList(PrpLReCaseVo.class);
		
		return reCaseVoList;
	}

	@Override
	public PrpLEndCaseVo findEndCaseByType(String registNo,String endCaseType) {
		PrpLEndCaseVo endCaseVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("endcaseType",endCaseType);
		queryRule.addDescOrder("endCaseDate");
		List<PrpLEndCase> poList = databaseDao.findAll(PrpLEndCase.class,queryRule);
		if(poList != null && !poList.isEmpty()){
			endCaseVo = Beans.copyDepth().from(poList.get(0)).to(PrpLEndCaseVo.class);
		}
		return endCaseVo;
	}
	/**
	 * 发送短信
	 * @param prpLEndCaseVo
	 */
	public void sendMsg(PrpLEndCaseVo prpLEndCaseVo,String userCode){
		PrpLCompensateVo compensateVo=compensateTaskService.findPrpLCompensateVoByPK(prpLEndCaseVo.getCompensateNo());
		BigDecimal zero = new BigDecimal(0);
		//理算冲销和0结不发短信
		if(compensateVo!=null&&"0".equals(compensateVo.getPrpLCompensateExt().getWriteOffFlag()) && 
				compensateVo.getSumPaidAmt() != null && compensateVo.getSumPaidAmt().compareTo(zero)==1){
			PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(prpLEndCaseVo.getRegistNo());
			PrpLCMainVo prpLCMain = policyViewService.getPolicyInfo(prpLRegistVo.getRegistNo(), 
					prpLRegistVo.getPolicyNo());
			List<SendMsgParamVo> msgParamVoList = this.getsendMsgParamVo(prpLEndCaseVo, 
					prpLRegistVo, prpLCMain, userCode, compensateVo);
			SysMsgModelVo msgModelVo = sendMsgService.findmsgModelVo(CodeConstants.ModelType.payee, CodeConstants.SystemNode.endCase,prpLCMain.getComCode(),CodeConstants.CaseType.normal);
			if(msgModelVo != null){
				Date sendTime = sendMsgService.getSendTime(msgModelVo.getTimeType());
				for(SendMsgParamVo msgParamVo:msgParamVoList){
					String message = sendMsgService.getMessage(msgModelVo.getContent(), msgParamVo);
					String status="";
					boolean	 index=false;
					index=smsService.sendSMSContent(msgParamVo.getRegistNo(), msgParamVo.getPhoneNo(), message, msgParamVo.getUseCode(), msgParamVo.getComCode(),sendTime);
					if(index){
						status="1";//推送短信平台成功
					}else{
						status="0";//推送短信平台失败
					}
					if(StringUtils.isNotBlank(msgParamVo.getPhoneNo())){
					putSmsmessage(msgParamVo,msgParamVo.getPhoneNo(),sendTime,message,status);
					}
				}
			} else {
				String content = prpLEndCaseVo.getRegistNo() +
						" 结案发送短信模板为空!" +
						" ModelTYpe:" + CodeConstants.ModelType.payee +
						" SystemNode:" + CodeConstants.SystemNode.endCase +
						"CaseType:" + CodeConstants.CaseType.normal;
				log.info(content);
			}
		}
		
	}

	public List<SendMsgParamVo> getsendMsgParamVo(PrpLEndCaseVo prpLEndCaseVo,PrpLRegistVo prpLRegistVo,
			PrpLCMainVo prpLCMainVo,String userCode,PrpLCompensateVo compensateVo){
		SendMsgParamVo msgParamVo = new  SendMsgParamVo();
		msgParamVo.setUseCode(userCode);
		msgParamVo.setComCode(prpLCMainVo.getComCode());
		msgParamVo.setRegistNo(prpLEndCaseVo.getRegistNo());
		msgParamVo.setReportTime(DateUtils.dateToStr(prpLRegistVo.getReportTime(), DateUtils.YToSec));
		if(prpLRegistVo.getPolicyNo() != null && !"".equals(prpLRegistVo.getPolicyNo())){
			msgParamVo.setPolicyNo(prpLRegistVo.getPolicyNo());
		}
		if(prpLRegistVo.getPrpLRegistExt() != null && prpLRegistVo.getPrpLRegistExt().getInsuredName()!=null){
			msgParamVo.setInsuredName(prpLRegistVo.getPrpLRegistExt().getInsuredName());
		}
		if(prpLRegistVo.getInsuredPhone() != null){
			msgParamVo.setMobile(prpLRegistVo.getInsuredPhone());
		}
		if(prpLRegistVo.getPrpLRegistExt()!=null && prpLRegistVo.getPrpLRegistExt().getDangerRemark()!=null){
			msgParamVo.setDangerRemark(prpLRegistVo.getPrpLRegistExt().getDangerRemark());
		}
		if(prpLRegistVo.getPrpLRegistCarLosses().size()>=2){//取三者车牌号
			for(PrpLRegistCarLossVo registCarLossVo:prpLRegistVo.getPrpLRegistCarLosses()){
				if("3".equals(registCarLossVo.getLossparty())&&registCarLossVo.getLicenseNo()!=null){
					msgParamVo.setOtherLicenseNo(registCarLossVo.getLicenseNo());
					break;
				}
			}
		}
		if(prpLRegistVo.getReportorName() != null){
			msgParamVo.setReportorName(prpLRegistVo.getReportorName());
		}
		if(prpLRegistVo.getReportorPhone() != null && !"".equals(prpLRegistVo.getReportorPhone())){//报案人电话
			msgParamVo.setReportoMobile(prpLRegistVo.getReportorPhone());
		}
		if(prpLRegistVo.getPrpLRegistExt()!=null&&prpLRegistVo.getPrpLRegistExt().getFrameNo()!=null
				&&!"".equals(prpLRegistVo.getPrpLRegistExt())){
			msgParamVo.setFrameNo(prpLRegistVo.getPrpLRegistExt().getFrameNo());
		}
		if(prpLRegistVo.getDamageCode() != null){
	    	msgParamVo.setDamageReason(codeTranService.transCode("DamageCode", prpLRegistVo.getDamageCode()));
	    }
		if(prpLRegistVo.getDamageTime() != null){
			msgParamVo.setDamageTime(prpLRegistVo.getDamageTime());
		}
		if(prpLRegistVo.getDamageAddress() != null){
			msgParamVo.setDamageAddress(prpLRegistVo.getDamageAddress());
		}
		if(prpLCMainVo.getPrpCItemCars() != null && prpLCMainVo.getPrpCItemCars().size()>0){
			msgParamVo.setBrandName(prpLCMainVo.getPrpCItemCars().get(0).getBrandName());
		}
		if(prpLRegistVo.getPrpLRegistExt()!=null && prpLRegistVo.getPrpLRegistExt().getLicenseNo()!=null){
			msgParamVo.setLicenseNo(prpLRegistVo.getPrpLRegistExt().getLicenseNo());
		}
		if(prpLCMainVo.getPrpCItemKinds()!=null && prpLCMainVo.getPrpCItemKinds().size()>0){
            msgParamVo.setPrpCItemKinds(prpLCMainVo.getPrpCItemKinds());
        }
		
		List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(prpLRegistVo.getRegistNo());
		String insuredDate = this.getInsuredDate(cMainVoList);
		//保险期间
		msgParamVo.setInsuredDate(insuredDate);
		
		if(prpLRegistVo.getPrpLRegistCarLosses() != null && prpLRegistVo.getPrpLRegistCarLosses().get(0).getLosspart() != null){
			String lossPart = sendMsgService.getLossPart(prpLRegistVo.getPrpLRegistCarLosses().get(0).getLosspart());
			msgParamVo.setLosspart(lossPart);//损失项
		}
		
		if("1101".equals(prpLEndCaseVo.getRiskCode())){
			msgParamVo.setRiskType("交强险");
		}else if(prpLEndCaseVo.getRiskCode()!=null&&!"".equals(prpLEndCaseVo.getRiskCode())){
			msgParamVo.setRiskType("商业险");
		}
		
		//承保险种
		String kindCode = sendMsgService.getKindName(prpLRegistVo.getRegistNo());
		msgParamVo.setKindCode(kindCode);
		
		List<SendMsgParamVo> msgParamVoList = new ArrayList<SendMsgParamVo>();
		for(PrpLPaymentVo paymentVo:compensateVo.getPrpLPayments()){
			SendMsgParamVo msgParam = new  SendMsgParamVo();
			Beans.copy().from(msgParamVo).to(msgParam);
			//赔款金额
			msgParam.setSumAmt(paymentVo.getSumRealPay().toString());
			//收款人信息
			PrpLPayCustomVo paycustomVo = managerService.findPayCustomVoById(paymentVo.getPayeeId());
			//银行账户
			String accountNo = paycustomVo.getAccountNo();
			accountNo = accountNo.substring(accountNo.length()-4, accountNo.length());
			msgParam.setAccountNo(accountNo);
			//收款人名称
			msgParam.setPayeeName(paycustomVo.getPayeeName());
			//如果收款人手机号码是空，则不发短信
			if(paycustomVo.getPayeeMobile()!=null && !"".equals(paycustomVo.getPayeeMobile())){
				msgParam.setPhoneNo(paycustomVo.getPayeeMobile());
				msgParamVoList.add(msgParam);
			}
		}
		
		return msgParamVoList;
	}
	
	/**
	 * 返回保险期间
	 * @param cMainList
	 * @return
	 */
	public String getInsuredDate(List<PrpLCMainVo> cMainList){
		String insuredDate = "";
		for(PrpLCMainVo prpLCMain:cMainList){
			if("1101".equals(prpLCMain.getRiskCode())){
				insuredDate = insuredDate + "(交强)" + DateUtils.dateToStr(prpLCMain.getStartDate(), "yyyy-MM-dd") + 
						"至" + DateUtils.dateToStr(prpLCMain.getEndDate(), "yyyy-MM-dd");
			}else{
				insuredDate = insuredDate + "(商业)" + DateUtils.dateToStr(prpLCMain.getStartDate(), "yyyy-MM-dd") + 
						"至" + DateUtils.dateToStr(prpLCMain.getEndDate(), "yyyy-MM-dd");
			}
		}
		return insuredDate;
	}
	
	
	@Override
	public List<PrpLReCaseVo> findReCaseVoListByqueryMap(
			Map<String, String> queryMap) {
		List<PrpLReCaseVo> reCaseVoList = new ArrayList<PrpLReCaseVo>();
		QueryRule queryRule = QueryRule.getInstance();
		for(String key:queryMap.keySet()){
			String value = queryMap.get(key);
			if(StringUtils.isNotBlank(value)){
				queryRule.addEqual(key,value);
			}
		}
		List<PrpLReCase> prpLReCaseList = databaseDao.findAll(PrpLReCase.class,queryRule);
		if(prpLReCaseList != null && prpLReCaseList.size() > 0){
            for(PrpLReCase po : prpLReCaseList){
                PrpLReCaseVo vo = new PrpLReCaseVo();
                Beans.copy().from(po).to(vo);
                reCaseVoList.add(vo);
            }
        }
		return reCaseVoList;
	}
	
	//保存短信内容yzy 
	private void putSmsmessage(SendMsgParamVo msgParamVo,String moble,Date sendTime0,String smsContext,String status){
			//DateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date trueSendTime=new Date();//真实发送时间
			Date sendTime1=null;
			Date nowTime=new Date();
			if(sendTime0!=null){
				sendTime1=DateUtils.addMinutes(sendTime0, -5);//短信平台发送时间
			   if(nowTime.getTime()<sendTime1.getTime()){
				    trueSendTime=sendTime1;
				}
			}
			PrpsmsMessageVo prpsmsMessageVo=new PrpsmsMessageVo();
			if(msgParamVo!=null){
				prpsmsMessageVo.setBusinessNo(msgParamVo.getRegistNo());
				prpsmsMessageVo.setComCode(msgParamVo.getComCode());
				prpsmsMessageVo.setCreateTime(nowTime);
				prpsmsMessageVo.setPhoneCode(moble);
				prpsmsMessageVo.setSendNodecode(FlowNode.EndCas.toString());
				prpsmsMessageVo.setSendText(smsContext);
				prpsmsMessageVo.setTruesendTime(trueSendTime);
				prpsmsMessageVo.setUserCode(msgParamVo.getUseCode());
				prpsmsMessageVo.setBackTime(nowTime);
				prpsmsMessageVo.setStatus(status);
			}
			msgModelService.saveorUpdatePrpSmsMessage(prpsmsMessageVo);
		}
}
