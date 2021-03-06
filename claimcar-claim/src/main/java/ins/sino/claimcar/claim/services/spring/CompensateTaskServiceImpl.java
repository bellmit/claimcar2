package ins.sino.claimcar.claim.services.spring;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import ins.sino.claimcar.claim.service.*;
import ins.sino.claimcar.claim.vo.*;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;
import com.sinosoft.insaml.apiclient.FxqCrmRiskService;
import com.sinosoft.insaml.povo.vo.BussRiskInfoReturnVo;
import com.sinosoft.insaml.povo.vo.BussRiskInfoVo;
import com.sinosoft.insaml.povo.vo.SusInfoVo;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.util.ConfigUtil;
import ins.platform.utils.DataUtils;
import ins.platform.utils.DateUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.CompensateType;
import ins.sino.claimcar.CodeConstants.PayFlagType;
import ins.sino.claimcar.CodeConstants.PayReason;
import ins.sino.claimcar.CodeConstants.PolicyType;
import ins.sino.claimcar.bankaccount.service.BankAccountService;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.UnderWriteFlag;
import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.certify.service.CertifyIlogService;
import ins.sino.claimcar.certify.service.CertifyPubService;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.calculator.CalculatorCI;
import ins.sino.claimcar.claim.po.PrpLCharge;
import ins.sino.claimcar.claim.po.PrpLCoins;
import ins.sino.claimcar.claim.po.PrpLCompensate;
import ins.sino.claimcar.claim.po.PrpLCompensateExt;
import ins.sino.claimcar.claim.po.PrpLEndor;
import ins.sino.claimcar.claim.po.PrpLLossItem;
import ins.sino.claimcar.claim.po.PrpLPayCustom;
import ins.sino.claimcar.claim.po.PrpLPayHis;
import ins.sino.claimcar.claim.po.PrpLPayment;
import ins.sino.claimcar.claim.po.PrpLPrePay;
import ins.sino.claimcar.claim.po.PrploldCompensate;
import ins.sino.claimcar.claim.services.CompensateAutoService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;
import ins.sino.claimcar.ilog.rule.service.IlogRuleService;
import ins.sino.claimcar.ilogFinalpowerInfo.vo.IlogFinalPowerInfoVo;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.other.vo.PrpLAutoVerifyVo;
import ins.sino.claimcar.other.vo.PrpLPayBankVo;
import ins.sino.claimcar.other.vo.SendMsgParamVo;
import ins.sino.claimcar.padpay.po.PrpLPadPayMain;
import ins.sino.claimcar.pay.service.PadPayService;
import ins.sino.claimcar.pay.service.RecPayService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.rule.vo.VerifyClaimRuleVo;
import ins.sino.claimcar.verifyclaim.service.VerifyClaimService;

import java.util.HashMap;

@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" }, timeout = 600*1000)
@Path("compensateTaskService")
public class CompensateTaskServiceImpl implements CompensateTaskService {
	@Autowired
	private CompensateService compensateService;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private BaseDaoService baseDaoService;
	@Autowired
	ManagerService managerService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	private CalculatorCI calculatorCI;
	@Autowired
	private WfFlowQueryService wfFlowQueryService;
	@Autowired
	ConfigService configService;
	@Autowired
	private CompensateAutoService compensateAutoService;
	@Autowired
	private DeflossHandleService deflossHandleService;
	@Autowired
	private PersTraceDubboService persTraceDubboService;
    @Autowired
    CompensateHandleServiceIlogService compensateHandleServiceIlogService;
	@Autowired
	private AssignService assignService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private VerifyClaimService verifyClaimService;
	@Autowired
	private CompensateHandleService compHandleService;
	@Autowired
	private CertifyIlogService certifyIlogService;
	@Autowired
	private CertifyPubService certifyPubService;
	@Autowired
	PadPayService padPayService;
	
	@Autowired
	RecPayService recPayService;
	
	@Autowired
	BankAccountService bankAccountService;
	@Autowired
	PayCustomService payCustomService;
	@Autowired
    LossCarService lossCarService;
	@Autowired
    private IlogRuleService ilogRuleService;
	@Autowired
	private ClaimService claimService;

	
	private static Logger logger = LoggerFactory.getLogger(CompensateTaskServiceImpl.class);

	
	@Override
	public List<PrpLCompensateVo> findCompByRegistNo(String registNo) {

		return compensateService.findCompByRegistNo(registNo);
	}

	@Override
	public PrpLCompensateVo findCompByClaimNo(String ClaimNo){

		return compensateService.findCompByClaimNo(ClaimNo,"N");
	}
	
	@Override
	public PrpLCompensateVo searchCompByClaimNo(String ClaimNo){
		return compensateService.searchCompByClaimNo(ClaimNo,"N");
	}

	/**
	 * ????????????????????????????????????
	 * <pre></pre>
	 * @param id
	 * @return
	 * @modified:
	 * ???ZhouYanBin(2016???4???12??? ??????3:10:17): <br>
	 */
	@Override
	public PrpLCompensateVo findPrpLCompensateVoByPK(String id) {
		PrpLCompensateVo prpLCompensateVo = compensateService.findCompByPK(id);
//		PrpLCompensate prpLCompensate = databaseDao.findByPK(PrpLCompensate.class,id);
//		if(prpLCompensate != null){
//		prpLCompensate.getPrpLPayments().size();
//			
//			prpLCompensateVo = Beans.copyDepth().from(prpLCompensate).to(PrpLCompensateVo.class);
//		}
		return prpLCompensateVo;
	}

	@Override
	public List<PrpLPrePayVo> getPrePayVo(String compeNo,String type) {
		return compensateService.getPrePayVo(compeNo,type);
	}
	
	
	/** ?????????????????? **/
	@Override
	public void writeBackCompesate(PrpLCompensateVo compeVo) {
		PrpLCompensate compePo = databaseDao.findByPK(PrpLCompensate.class,compeVo.getCompensateNo());
		Beans.copy().from(compeVo).excludeNull().to(compePo);
		databaseDao.update(PrpLCompensate.class,compePo);
	}

	@Override
	public List<PrpLCompensateVo> findCompListByClaimNo(String claimNo,String compensateType) {
		return compensateService.findCompListByClaimNo(claimNo,compensateType);
	}
	
	@Override
	public PrpLCompensateVo queryCompByPK(String compeNo) {
		return compensateService.findCompByPK(compeNo);
	}

	public void main(String[] args) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo","4000000201612020000068");
		List<PrpLCompensateExt> s = databaseDao.findAll(PrpLCompensateExt.class,queryRule);
		for(PrpLCompensateExt ss : s){
			System.err.println(ss.getCompensateNo());
		}
	}
	
	@Override
	public List<PrpLCompensateVo> queryCompensate(String registNo,String type) {
		return compensateService.findCompensate(registNo,type);
	}
	
	@Override
	public List<PrpLCompensateVo> findCompensatevosByRegistNo(String registNo) {
		return compensateService.findCompensatevosByRegistNo(registNo);
	}
	
	@Override
    public List<PrpLCompensateVo> findNotCancellCompensatevosByRegistNo(String registNo) {
        return compensateService.findNotCancellCompensatevosByRegistNo(registNo);
    }

	@Override
	public List<PrpLPrePayVo> queryPrePay(String compeNo) {
		return compensateService.queryPrePay(compeNo);
	}

	/**
	 * ?????????????????????
	 * @author lanlei
	 */
	@Override
	public void savePrpLEndor(String compensateNo,SysUserVo userVo ) {
		compensateService.savePrpLEndor(compensateNo,userVo);
	}


	@Override
	public BigDecimal queryEmotorKindLossfee(String policyNo,String kindCode) {
		return compensateService.queryEmotorKindLossfee(policyNo,kindCode);
	}


	@Override
	public List<PrpLCompensateVo> simpleQueryCompensate(String registNo,
			String type) {
		List<PrpLCompensateVo> compeVos = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("compensateType",type);
		List<PrpLCompensate> compePo = databaseDao.findAll(PrpLCompensate.class,queryRule);
		if(compePo!=null&&compePo.size()>0){
			compeVos = new ArrayList<PrpLCompensateVo>();
			for(PrpLCompensate prpLCompensate:compePo){
				PrpLCompensateVo prpLCompensateVo = new PrpLCompensateVo();
				Beans.copy().from(prpLCompensate).to(prpLCompensateVo);
				compeVos.add(prpLCompensateVo);
			}
		}
		return compeVos;
	}

	@Override
	public ResultPage<PrpLCompensateVo> findCompensatePage(String registNo,String claimNo,String compensateNo,
															int start,int length) {
		// TODO Auto-generated method stub
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" FROM PrpLCompensate compensate WHERE 1=1 ");
		//????????????
		sqlUtil.append(" AND compensate.compensateType = ?");
		sqlUtil.addParamValue("N");
		//????????????
		sqlUtil.append(" AND compensate.underwriteFlag = ?");
		sqlUtil.addParamValue("1");
		if(StringUtils.isNotBlank(registNo)){
			sqlUtil.append(" AND compensate.registNo = ?");
			sqlUtil.addParamValue(registNo);
		}
		
		if(StringUtils.isNotBlank(claimNo)){
			sqlUtil.append(" AND compensate.claimNo = ?");
			sqlUtil.addParamValue(claimNo);
		}
		
		if(StringUtils.isNotBlank(compensateNo)){
			sqlUtil.append(" AND compensate.compensateNo = ?");
			sqlUtil.addParamValue(compensateNo);
		}
		
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		
		List<PrpLCompensateVo> resultVoList=new ArrayList<PrpLCompensateVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){
			PrpLCompensateVo compensateVo = new PrpLCompensateVo();
			List<Object[]> obj = page.getResult();

			PrpLCompensate compensate = (PrpLCompensate)obj.toArray()[i];
			
			Beans.copy().from(compensate).to(compensateVo);
			resultVoList.add(compensateVo);
		}
		ResultPage<PrpLCompensateVo> resultPage = new ResultPage<PrpLCompensateVo> (start, length, page.getTotalCount(), resultVoList);
		
		return resultPage;
	}
	
	//????????????PrpLPayment??????
	@Override
	public ResultPage<PrpLPaymentVo> findPrpLPaymentPage(String compensateNo,int start,int length) {
		
		
		List<PrpLPaymentVo> paymentList=null;
		 PrpLCompensateVo compensateVo=null;
		if(StringUtils.isNotBlank(compensateNo)){
			 compensateVo=compensateService.findCompByPK(compensateNo);
			 }
		

			 if(compensateVo!=null){
				
				 if("N".equals(compensateVo.getCompensateType())&&"1".equals(compensateVo.getUnderwriteFlag())){
				 paymentList=compensateVo.getPrpLPayments();
				if(paymentList!=null&& paymentList.size()>0){
					for(PrpLPaymentVo prpLPaymentVo :paymentList){
						if("0".equals(prpLPaymentVo.getOtherFlag())){
							prpLPaymentVo.setOtherFlag("???");
						}else if("1".equals(prpLPaymentVo.getOtherFlag())){
							prpLPaymentVo.setOtherFlag("???");
						}else{
							
						}
						PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(prpLPaymentVo.getPayeeId());
						if(payCustomVo!=null){
						  prpLPaymentVo.setAccountNo(payCustomVo.getAccountNo());
						  SysCodeDictVo sys = codeTranService.findTransCodeDictVo("BankCode",payCustomVo.getBankName());
						  if(sys!=null){
							  prpLPaymentVo.setBankName(sys.getCodeName());
						  }
						  prpLPaymentVo.setClaimNo(compensateVo.getClaimNo());
						  prpLPaymentVo.setCompensateNo(compensateVo.getCompensateNo());
						  prpLPaymentVo.setPolicyNo(compensateVo.getPolicyNo());
						  prpLPaymentVo.setPayeeName(payCustomVo.getPayeeName());
						  
						}
					}
				}
				 }
		   }
	     ResultPage<PrpLPaymentVo> resultPage=null;
		if(paymentList!=null && paymentList.size()>0){
		resultPage = new ResultPage<PrpLPaymentVo> (start, length, paymentList.size(), paymentList);
		}else{
			
		   throw new IllegalArgumentException("???????????????");
		 
		}
		return resultPage;
	}


	
	@Override
	public Map<String,BigDecimal> queryEmotorMap(String policyNo) {
		return compensateService.queryEmotorMap(policyNo);
	}

	@Override
	public Map<String,BigDecimal> querySumRealPay(String claimNo,String compensateNo) {
		// TODO Auto-generated method stub
		return compensateService.querySumRealPay(claimNo,compensateNo);
	}
	
	@Override
	public List<PrpLPrePayVo> findPrePayList(String compensateNo){
		return compensateService.getPrePayVo(compensateNo,null) ;
	}

	/* 
	 * @see ins.sino.claimcar.claim.service.CompensateTaskService#paymentWriteBackPrePay(java.lang.String, java.lang.String, java.util.Date)
	 * @param compensateNo
	 * @param serialNo
	 * @param payTime
	 */
	@Override
	public void paymentWriteBackPrePay(String compensateNo,String serialNo,Date payTime) throws Exception {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("compensateNo",compensateNo);
		queryRule.addEqual("serialNo",serialNo);
		List<PrpLPrePay> prePays = databaseDao.findAll(PrpLPrePay.class,queryRule);
		PrpLPrePay prePay = null;
		if(prePays!=null&&prePays.size()>0){
			prePay = prePays.get(0);
			prePay.setPayStatus("1");
			prePay.setPayTime(payTime);
		}else{
			throw new Exception("???????????????????????????????????????");
		}
		databaseDao.save(PrpLPrePay.class,prePay);
	}
	
	@Override
	public void paymentWriteBackCompVo(PrpLCompensateVo compensateVo) throws Exception {
		PrpLCompensate prpLCompensate = databaseDao.findByPK(PrpLCompensate.class,compensateVo.getCompensateNo());

		if(prpLCompensate==null){// ??????
			throw new Exception("??????????????????");
		}else{// ??????
			// ????????????????????????????????????
			compensateService.mergeList(prpLCompensate,compensateVo.getPrpLPayments(),prpLCompensate.getPrpLPayments(),"id",PrpLPayment.class,"setPrpLCompensate");
			compensateService.mergeList(prpLCompensate,compensateVo.getPrpLCharges(),prpLCompensate.getPrpLCharges(),"id",PrpLCharge.class,"setPrpLCompensate");
		}
		databaseDao.save(PrpLCompensate.class,prpLCompensate);
	}

	/**
	 * ????????????????????????????????????
	 * @param registNo
	 */
	@Override
	public List<PrpLCompensateVo> queryCompensateByRegistNo(String registNo) {
		
		return compensateService.queryCompensateByRegistNo(registNo);
		
		}

	@Override
	public CompensateListVo calCulator(PrpLCompensateVo prpLCompensateVo,List<PrpLLossItemVo> prpLLossItemVoList,
										List<PrpLLossPropVo> prpLLossPropVoList,List<PrpLLossPersonVo> prpLLossPersonVoList,String compensateType) {
		return calculatorCI.calCulator(prpLCompensateVo,prpLLossItemVoList,prpLLossPropVoList,prpLLossPersonVoList,compensateType);
	}

	@Override
	public List<ThirdPartyLossInfo> getThirdPartyLossInfolistBz(CompensateListVo returnCompensateList,String calculateType) {
		return calculatorCI.getThirdPartyLossInfolistBz(returnCompensateList,calculateType);
	}

	@Override
	public List<PrpLClaimDeductVo> findDeductCond(String registNo,String riskCode) {
		return compensateService.findDeductCond(registNo,riskCode);
	}
	
	@Override
	public Map<String,List<PrpLPrePayVo>> getPrePayMap(String ClaimNo,String bcFlag,String prePaidFlag) throws Exception {
		return compensateService.getPrePayMap(ClaimNo,bcFlag,prePaidFlag);
	}
	
	@Override
    public PrpLPadPayMainVo findPadPayMain(String registNo) {
	   PrpLPadPayMainVo prpLPadPayMainVo = new PrpLPadPayMainVo();
       List<PrpLPadPayMainVo> prpLPadPayMainVoList = compensateService.findPadPayMainByRegistNo(registNo);
       if(prpLPadPayMainVoList != null&&prpLPadPayMainVoList.size() > 0){
           prpLPadPayMainVo = prpLPadPayMainVoList.get(0);
       }
        return prpLPadPayMainVo;
    }
	
	@Override
	public List<PrpLPadPayPersonVo> getPadPayPersons(String registNo,String prePaidFlag) {
		return compensateService.getPadPayPersons(registNo,prePaidFlag);
	}
	
	@Override
	public List<PrpLCItemKindVo> getDeductOffKindList(String registNo) {
		return compensateService.getDeductOffKindList(registNo);
	}


	@Override
	public List<PrpLDlossPersTraceMainVo> getValidLossPersTraceMain(String registNo,String bcFlag) {
		return compensateService.getValidLossPersTraceMain(registNo,bcFlag);
	}

	@Override
	public boolean checkLossState(String lossState,String bcFlag) {
		return compensateService.checkLossState(lossState,bcFlag);
	}

	@Override
	public void cancelCompensates(PrpLCompensateVo compVo,BigDecimal flowTaskId,SysUserVo userVo) {
		compensateService.cancelCompensates(compVo,flowTaskId,userVo);
		
	}
	@Override
	public void cancelCompensates(BigDecimal flowTaskId,SysUserVo userVo) {
		compensateService.cancelCompensate(flowTaskId,userVo);
		
	}
	
	@Override
	public void prePayCancel(String taskId, SysUserVo user) {
		 compensateService.prePayCancel(taskId, user);
		
	}

	@Override
	public VerifyClaimRuleVo organizeRuleVo(String compensateId,SysUserVo userVo) {
		return compensateService.organizeRuleVo(compensateId,userVo);
	}
	@Override
	public PrpLCompensateVo findCompByPK(String compensateNo) {
		return compensateService.findCompByPK(compensateNo);
	}
	@Override
	public List<PrpLCompensateVo> findCompensateBySettleNo(String settleNo,String flag){
		return compensateService.findCompensateBySettleNo(settleNo, flag);
	}

	@Override
	public Map<String, BigDecimal> getCompKindAmtMap(List<PrpLLossItemVo> prpLLossItemVoList,List<PrpLLossPropVo> prpLLossPropVoList,
			List<PrpLLossPersonVo> prpLLossPersonVoList,List<PrpLChargeVo> prpLChargeVoList) {
		
		return compensateService.getCompKindAmtMap(prpLLossItemVoList, prpLLossPropVoList,
				prpLLossPersonVoList, prpLChargeVoList);
	}

	@Override
	public List<PrpLCheckDutyVo> calcCheckDutyList(Map<String,Double> leftAmountMap,List<PrpLCheckDutyVo> checkDutyList,String calType) {
		logger.info("?????????" + ((checkDutyList == null || checkDutyList.size() <=0 ) ? "checkDutyList =null" :checkDutyList.get(0).getRegistNo()) + ",??????????????????????????????"); 
		for(PrpLCheckDutyVo checkDutyVo:checkDutyList){
			BigDecimal carLeftAmount = BigDecimal.ZERO;
			BigDecimal medLeftAmount = BigDecimal.ZERO;
			BigDecimal dehLeftAmount = BigDecimal.ZERO;

			boolean ciDuty = false;
			if("1".equals(checkDutyVo.getCiDutyFlag())){
				ciDuty = true;
			}
			if(leftAmountMap.get("car-"+checkDutyVo.getSerialNo())!=null
					&&!leftAmountMap.get("car-"+checkDutyVo.getSerialNo()).isNaN()
					&&!leftAmountMap.get("car-"+checkDutyVo.getSerialNo()).isInfinite()){
				carLeftAmount = new BigDecimal(leftAmountMap.get("car-"+checkDutyVo.getSerialNo()));
			}else{
				//carLeftAmount = new BigDecimal(CalculatorCI.calBzAmount(CodeConstants.FeeTypeCode.PROPLOSS,ciDuty));
				carLeftAmount = new BigDecimal(configService.calBzAmount(CodeConstants.FeeTypeCode.PROPLOSS,ciDuty,checkDutyVo.getRegistNo()));
			}

			if(leftAmountMap.get("med-"+checkDutyVo.getSerialNo())!=null
					&&!leftAmountMap.get("med-"+checkDutyVo.getSerialNo()).isNaN()
					&&!leftAmountMap.get("med-"+checkDutyVo.getSerialNo()).isInfinite()){
				medLeftAmount = new BigDecimal(leftAmountMap.get("med-"+checkDutyVo.getSerialNo()));
			}else{
				//medLeftAmount = new BigDecimal(CalculatorCI.calBzAmount(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES,ciDuty));
				medLeftAmount = new BigDecimal(configService.calBzAmount(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES,ciDuty,checkDutyVo.getRegistNo()));
			}

			if(leftAmountMap.get("deh-"+checkDutyVo.getSerialNo())!=null
					&&!leftAmountMap.get("deh-"+checkDutyVo.getSerialNo()).isNaN()
					&&!leftAmountMap.get("deh-"+checkDutyVo.getSerialNo()).isInfinite()){
				dehLeftAmount = new BigDecimal(leftAmountMap.get("deh-"+checkDutyVo.getSerialNo()));
			}else{
				//dehLeftAmount = new BigDecimal(CalculatorCI.calBzAmount(CodeConstants.FeeTypeCode.PERSONLOSS,ciDuty));
				dehLeftAmount = new BigDecimal(configService.calBzAmount(CodeConstants.FeeTypeCode.PERSONLOSS,ciDuty,checkDutyVo.getRegistNo()));
			}
			if(CompensateType.COMP_CI.equals(calType)){// ?????????
				logger.info("?????????" + (checkDutyList == null ? null :checkDutyVo.getRegistNo() + "??????????????????" + "CiTCarLeftAmount.getCiTCarLeftAmount()="
			+ carLeftAmount + ",checkDutyVo.getCiTMedLeftAmount()=" + medLeftAmount +",checkDutyVo.getCiTDehLeftAmount()=" + dehLeftAmount ));
				checkDutyVo.setCiTCarLeftAmount(carLeftAmount);
				checkDutyVo.setCiTMedLeftAmount(medLeftAmount);
				checkDutyVo.setCiTDehLeftAmount(dehLeftAmount);
			}else{
				logger.info("?????????" + (checkDutyList == null ? null :checkDutyVo.getRegistNo() + "??????????????????" + "CiTCarLeftAmount.getBiTCarLeftAmount()="
						+ carLeftAmount + ",checkDutyVo.getBiTMedLeftAmount()=" + medLeftAmount +",checkDutyVo.getBiTDehLeftAmount()=" + dehLeftAmount ));
				checkDutyVo.setBiTCarLeftAmount(carLeftAmount);
				checkDutyVo.setBiTMedLeftAmount(medLeftAmount);
				checkDutyVo.setBiTDehLeftAmount(dehLeftAmount);
			}
		}
		logger.info("?????????" + ((checkDutyList == null || checkDutyList.size() <= 0) ? "checkDutyList =null" :checkDutyList.get(0).getRegistNo()) + ",??????????????????????????????"); 

		return checkDutyList;
	}

	@Override
	public boolean isPrepayAllPassed(String registNo) {
		boolean prepay = true;
		List<PrpLCompensateVo> prpLCompensateVoList = simpleQueryCompensate(registNo, "Y");
		if (prpLCompensateVoList != null && prpLCompensateVoList.size() > 0) {
			for (PrpLCompensateVo prpLCompensateVo : prpLCompensateVoList) {
				if (!"1".equals(prpLCompensateVo.getUnderwriteFlag())
						&& !"7".equals(prpLCompensateVo.getUnderwriteFlag())) {// ????????????????????????????????????
					prepay = false;
				}
			}
		}
		if (prepay) {// ???????????????????????????
			List<PrpLWfTaskVo> prpLWfTaskVoList = wfFlowQueryService
					.findUnAcceptTask(registNo, FlowNode.PrePay.name(),
							FlowNode.PrePayWf.name());
			if (prpLWfTaskVoList != null && prpLWfTaskVoList.size() > 0) {
				prepay = false;
			}
		}
		return prepay;
	}

	@Override
	public boolean isCompepayAllPassed(String registNo) {
		boolean compepay = true;
		List<PrpLCompensateVo> prpLCompensateVoList = simpleQueryCompensate(registNo, "N");
		if (prpLCompensateVoList != null && prpLCompensateVoList.size() > 0) {
			for (PrpLCompensateVo prpLCompensateVo : prpLCompensateVoList) {
				if (!"1".equals(prpLCompensateVo.getUnderwriteFlag())
						&& !"7".equals(prpLCompensateVo.getUnderwriteFlag())) {// ??????????????????????????????????????????????????????
					compepay = false;
				}
			}
		}
		if (compepay) {// ?????????????????????????????????????????????
			List<PrpLWfTaskVo> prpLWfTaskVoList = wfFlowQueryService
					.findUnAcceptTask(registNo, FlowNode.Compe.name(),
							FlowNode.CompeWf.name());
			if (prpLWfTaskVoList != null && prpLWfTaskVoList.size() > 0) {
				compepay = false;
			}
		}
		return compepay;
	}

	

	@Override
	public PrpDAccidentDeductVo finPrpDAccidentDeductVo(String riskCode, String kindCode,String  clauseType,String indemnityDuty) {
		// TODO Auto-generated method stub
		return configService.findAccidentDeduct(riskCode,kindCode,clauseType,indemnityDuty);
	}

	

	
/**
 * ????????????????????????????????????
 */
	@Override
	public List<PrpLEndorVo> findPrpEndorListByPolicyNo(String policyNo) {
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("policyNo",policyNo);
		List<PrpLEndor> prpLEndors=databaseDao.findAll(PrpLEndor.class,queryRule);
		List<PrpLEndorVo> prpLEndorVos=new ArrayList<PrpLEndorVo>();
		if(prpLEndors!=null && prpLEndors.size()>0){
			for(PrpLEndor prpLEndor:prpLEndors){
				PrpLEndorVo prpLEndorVo=new PrpLEndorVo();
				Beans.copy().from(prpLEndor).to(prpLEndorVo);
				prpLEndorVos.add(prpLEndorVo);
			}
		}
		return prpLEndorVos;
	}

	@Override
	public List<SendMsgParamVo> getsendMsgParamVo(PrpLRegistVo prpLRegistVo,
			List<PrpLPaymentVo> paymentVo) {
		SendMsgParamVo msgParamVo = new  SendMsgParamVo();
		msgParamVo.setRegistNo(prpLRegistVo.getRegistNo());
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
		if(prpLRegistVo.getPrpLRegistCarLosses().size()>=2){//??????????????????
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
		if(prpLRegistVo.getReportorPhone() != null && !"".equals(prpLRegistVo.getReportorPhone())){//???????????????
			msgParamVo.setReportoMobile(prpLRegistVo.getReportorPhone());
		}
		if(prpLRegistVo.getPrpLRegistExt()!=null&&prpLRegistVo.getPrpLRegistExt().getFrameNo()!=null
				&&!"".equals(prpLRegistVo.getPrpLRegistExt())){
			msgParamVo.setFrameNo(prpLRegistVo.getPrpLRegistExt().getFrameNo());
		}
		if(prpLRegistVo.getDamageTime() != null){
			msgParamVo.setDamageTime(prpLRegistVo.getDamageTime());
		}
		if(prpLRegistVo.getDamageAddress() != null){
			msgParamVo.setDamageAddress(prpLRegistVo.getDamageAddress());
		}
		if(prpLRegistVo.getPrpLRegistExt()!=null && prpLRegistVo.getPrpLRegistExt().getLicenseNo()!=null){
			msgParamVo.setLicenseNo(prpLRegistVo.getPrpLRegistExt().getLicenseNo());
		}
		
		List<SendMsgParamVo> msgParamVoList = new ArrayList<SendMsgParamVo>();
		for(PrpLPaymentVo vo:paymentVo){
			SendMsgParamVo msgParam = new  SendMsgParamVo();
			Beans.copy().from(msgParamVo).to(msgParam);
			//????????????
			if(vo.getSumRealPay().compareTo(BigDecimal.ZERO)==-1){
				return null;
			}
			msgParam.setSumAmt(vo.getSumRealPay().toString());
			//???????????????
			PrpLPayCustomVo paycustomVo = managerService.findPayCustomVoById(vo.getPayeeId());
			//????????????
			String accountNo = paycustomVo.getAccountNo();
			//???????????????
			msgParam.setPayeeName(paycustomVo.getPayeeName());
			//???????????????????????????????????????????????????
			msgParam.setDamageReason(paycustomVo.getCertifyNo());
			//??????id???usercode?????????paycustomVo
			//msgParam.setUseCode(vo.getPayeeId().toString());
			//??????
			//msgParam.setSummary(paycustomVo.getSummary());
			//???????????????????????????????????????????????????
			if(paycustomVo.getPayeeMobile()!=null && !"".equals(paycustomVo.getPayeeMobile())){
				msgParam.setPhoneNo(paycustomVo.getPayeeMobile());
				msgParamVoList.add(msgParam);
			}
		}
		//???????????????????????????????????????????????????
		if(msgParamVoList!=null && msgParamVoList.size() > 1){
			BigDecimal sumAmt = new BigDecimal(0);
			for(SendMsgParamVo vo : msgParamVoList){
				if((!msgParamVoList.get(0).getDamageReason().equals(vo.getDamageReason())) &&
						(!msgParamVoList.get(0).getPayeeName().equals(vo.getPayeeName()))){
					return null;
				}
				//??????????????????????????????5????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
				//??????
				sumAmt=sumAmt.add(new BigDecimal(vo.getSumAmt()));
			}
			//??????????????????????????????5????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
			if(sumAmt.compareTo(new BigDecimal(50000))==1){
				return null;
			}
		}else if(msgParamVoList!=null && msgParamVoList.size() ==1){
			BigDecimal sumAmt = new BigDecimal(0);
			//??????????????????????????????5????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
			//??????
			sumAmt=new BigDecimal(msgParamVoList.get(0).getSumAmt());
			//??????????????????????????????5????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
			if(sumAmt.compareTo(new BigDecimal(50000))==1){
				return null;
			}
		}
		return msgParamVoList;
	}
	

	/**
	 * ??????????????? ??????????????????
	 * @param registNo
	 * @param compensateNo
	 * @return
	 */
	public PrpLPadPayMainVo findPadPayMainByComp(String registNo,String compensateNo){
		PrpLPadPayMainVo padPayMainVo = null;
		
		QueryRule pfqr = QueryRule.getInstance();
		pfqr.addEqual("registNo",registNo);
		pfqr.addEqual("compensateNo",compensateNo);
		List<PrpLPadPayMain> padPayMains = databaseDao.findAll(PrpLPadPayMain.class,pfqr);
		if(padPayMains!=null && padPayMains.size()>0){
			PrpLPadPayMain padPayMain = padPayMains.get(0);
			padPayMainVo = Beans.copyDepth().from(padPayMain).to(PrpLPadPayMainVo.class);
		}
		
		return padPayMainVo;
	}

	
	/*
	 * ??????payeeId??????
	 */
	@Override
	public List<PrpLPaymentVo> findPrpLPayment(PrpLPayCustomVo prpLPayCustomVo,
			String compensateNo) {
		List<PrpLPaymentVo> prpLPaymentListVo = new ArrayList<PrpLPaymentVo>();;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("payeeId",prpLPayCustomVo.getId());
		queryRule.addEqual("prpLCompensate.compensateNo",compensateNo);
		List<PrpLPayment> prpLPaymentList = databaseDao.findAll(PrpLPayment.class,queryRule);
		if(prpLPaymentList != null && prpLPaymentList.size() > 0){
			for(PrpLPayment prpLPayment:prpLPaymentList){
				PrpLPaymentVo prpLPaymentVo=new PrpLPaymentVo();
				Beans.copy().from(prpLPayment).to(prpLPaymentVo);
				prpLPaymentListVo.add(prpLPaymentVo);
				
			}
		}
		return prpLPaymentListVo;
	}

	/*@Override
	public AjaxResult updateDeductCondForComp(List<PrpLClaimDeductVo> claimDeductSendList,String registNo) {
		return compensateService.updateDeductCondForComp(claimDeductSendList,registNo);
	}

	@Override
	public CompensateVo executeRulesDeductRate(CompensateVo compensateVo,PrpLClaimVo prpLClaimVo) {
		return compensateService.executeRulesDeductRate(compensateVo,prpLClaimVo);
	}*/
	public List<PrpLPayHisVo> showPayHis(Long otherId,String hisType){
		List<PrpLPayHisVo> PrpLPayHisVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("otherId",otherId);
		queryRule.addEqual("hisType",hisType);
		queryRule.addAscOrder("inputTime");
		List<PrpLPayHis> PrpLPayHisList = databaseDao.findAll(PrpLPayHis.class,queryRule);
		if(PrpLPayHisList!=null&& !PrpLPayHisList.isEmpty()){
			PrpLPayHisVoList = Beans.copyDepth().from(PrpLPayHisList).toList(PrpLPayHisVo.class);
		}
		return PrpLPayHisVoList;
	}

	@Override
	public void modAccountWritePayeeId(PrpLPayBankVo payBankVo,Long oldPayeeId,Long payeeId,SysUserVo userVo){
		Date date = new Date();
		if(payBankVo.getChargeCode()!=null&&!"".equals(payBankVo.getChargeCode())&&
				!"null".equals(payBankVo.getChargeCode())){
			QueryRule qr_charge = QueryRule.getInstance();
			qr_charge.addEqual("prpLCompensate.compensateNo", payBankVo.getCompensateNo());
			qr_charge.addEqual("payeeId",oldPayeeId);
			qr_charge.addEqual("chargeCode",payBankVo.getChargeCode());
			List<PrpLCharge> prpLChargeList = databaseDao.findAll(PrpLCharge.class, qr_charge);
			if(prpLChargeList!=null && prpLChargeList.size()>0){
				for(PrpLCharge prpLCharge:prpLChargeList){
					prpLCharge.setPayeeId(payeeId);
					prpLCharge.setUpdateTime(date);
					prpLCharge.setSummary(payBankVo.getSummary());
					PrpLChargeVo prpLChargeVo = new PrpLChargeVo();
					Beans.copy().from(prpLCharge).to(prpLChargeVo);
					compensateService.updatePrpLCharges(prpLChargeVo);
				}
			}
		}else{
			QueryRule qr = QueryRule.getInstance();
			qr.addEqual("prpLCompensate.compensateNo", payBankVo.getCompensateNo());
			qr.addEqual("payeeId",oldPayeeId);
			List<PrpLPayment> prpLPaymentList = databaseDao.findAll(PrpLPayment.class, qr);
			if(prpLPaymentList!=null && prpLPaymentList.size()>0){
				for(PrpLPayment prpLPayment:prpLPaymentList){
					prpLPayment.setPayeeId(payeeId);
					prpLPayment.setUpdateTime(date);
					prpLPayment.setSummary(payBankVo.getSummary());
					PrpLPaymentVo prpLPaymentVo = new PrpLPaymentVo();
					Beans.copy().from(prpLPayment).to(prpLPaymentVo);
					compensateService.updatePrpLPaymentVo(prpLPaymentVo);
				}
			}
		}
	}

	@Override
	public ResultPage<PrpLCompensateVo> findPreOrPadPayPage(String registNo,String claimNo,String compensateNo,String compeType,
															int start,int length) {
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		if("1".equals(compeType)){
			// ?????????????????????
			sqlUtil.append("select a.registNo , a.claimNo , a.compensateNo FROM PrpLPadPayMain a WHERE 1=1 ");
			sqlUtil.append(" AND (a.underwriteFlag != ? or a.underwriteFlag is null) ");
            sqlUtil.addParamValue("7");
		}else{
			// ????????????????????? 
			sqlUtil.append("select a.registNo , a.claimNo , a.compensateNo FROM PrpLCompensate a WHERE 1=1 ");
			sqlUtil.append(" AND a.compensateType = ?");			
			sqlUtil.addParamValue("Y");
	        sqlUtil.append(" AND (a.underwriteFlag != ? or a.underwriteFlag is null) ");
	        sqlUtil.addParamValue("7");
		}
		
//		//????????????
//		sqlUtil.append(" AND a.underwriteFlag = ?");
//		sqlUtil.addParamValue("1");
		if(StringUtils.isNotBlank(registNo)){
			sqlUtil.append(" AND a.registNo = ?");
			sqlUtil.addParamValue(registNo);
		}
		
		if(StringUtils.isNotBlank(claimNo)){
			sqlUtil.append(" AND a.claimNo = ?");
			sqlUtil.addParamValue(claimNo);
		}
		
		if(StringUtils.isNotBlank(compensateNo)){
			sqlUtil.append(" AND a.compensateNo = ?");
			sqlUtil.addParamValue(compensateNo);
		}
		sqlUtil.append(" ORDER BY createTime DESC ");
		
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		
		Page<Object[]> page = new Page<Object[]>();
		try{
			page = baseDaoService.pagedSQLQuery(sql,start,length,values);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		List<PrpLCompensateVo> resultVoList=new ArrayList<PrpLCompensateVo>();
		
		if(page!=null&&page.getTotalCount()>0){
			for(Object[] obj : page.getResult()){
				PrpLCompensateVo compeVo = new PrpLCompensateVo();
				compeVo.setCompensateNo(obj[2].toString());
				compeVo.setClaimNo(obj[1].toString());
				compeVo.setRegistNo(obj[0].toString());
				resultVoList.add(compeVo);
			}
		}

		// ????????????ResultPage
		ResultPage<PrpLCompensateVo> resultPage = new ResultPage<PrpLCompensateVo>(start,length,page.getTotalCount(),resultVoList);
		
		return resultPage;
	}

	@Override
	public List<PrpLCompensateVo> queryCompensateByOther(String registNo,
			String type, String policyNo,String underwriteFlag) {
		return compensateService.findCompensateByOther(registNo,type,policyNo,underwriteFlag);
	}

	@Override
	public List<PrploldCompensateVo> findPrpoldCompensateBypolicyNo(
			String policyNo) {
		QueryRule queryRule= QueryRule.getInstance();
		queryRule.addEqual("policyNo", policyNo);
	List<PrploldCompensate>	 poLists=databaseDao.findAll(PrploldCompensate.class,queryRule);
	List<PrploldCompensateVo> voLists=new ArrayList<PrploldCompensateVo>();
	if(poLists!=null && poLists.size()>0){
		for(PrploldCompensate po:poLists){
			PrploldCompensateVo vo=new PrploldCompensateVo();	
			Beans.copy().from(po).to(vo);
			voLists.add(vo);
		}
	}
		return voLists;
	}

	@Override
	public List<PrpLCompensateVo> findPrpCompensateBypolicyNo(String policyNo) {
		// TODO Auto-generated method stub
		
	List<PrpLCompensateVo> voLists=new ArrayList<PrpLCompensateVo>();
	voLists=compensateService.findPrpCompensateBypolicyNo(policyNo);
	return voLists;
}

@Override
	public Double findPrpllossBypolicyNo(String policyNo) {
		String sumrealpay="";
		Double sumAoumt=0.0;
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append("SELECT * FROM ywuser.prpLloss WHERE 1=1 ");
		sqlUtil.append(" AND policyNo = ? ");
		sqlUtil.addParamValue(policyNo);
		sqlUtil.append(" AND kindCode = ? ");
		sqlUtil.addParamValue("A");
		
		
		// ????????????
		String sql = sqlUtil.getSql();
		System.out.print("++==================="+sql);
		Object[] values = sqlUtil.getParamValues();
		//????????????
		List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
		// ????????????
		for(int i = 0; i<objects.size(); i++ ){
			Object[] obj = objects.get(i);
			sumrealpay= obj[32]==null ? "Y" : obj[32].toString();
			if(!"Y".equals(sumrealpay)){
				sumAoumt=sumAoumt+Double.valueOf(sumrealpay);
			}
			
		}
		return sumAoumt;
	}


	@Override
	public String isCompeCancelByClaimNo(String claimNo){
		String flag = "";
		
		//?????????????????????????????????????????????????????????
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("claimNo", claimNo);
		queryRule.addEqual("compensateType", "N");
		queryRule.addDescOrder("createTime");
		List<PrpLCompensate> compensateList = databaseDao.findAll(PrpLCompensate.class,queryRule);
		
		if(compensateList != null && compensateList.size() > 0){
			if("7".equals(compensateList.get(0).getUnderwriteFlag())){
				flag = "1";
			}else{
				flag = "2";
			}
		}else{
			flag = "0";
		}
		
		return flag;
	}
	
	@Override
	public void saveOrUpdateSettleNo(String settleNo,String accountNo,String operateType,
			String compensateNo,String serialNo,String payRefReason){
		if(compensateNo.startsWith("Y")){
			this.updateSettleNoForPrePay(settleNo, accountNo, operateType, compensateNo, serialNo, payRefReason);
		}else if("P60".equals(payRefReason)){
			this.updateSettleNoForPayment(settleNo, accountNo, operateType, compensateNo, serialNo);
		}else{
			this.updateSettleNoForCharge(settleNo, accountNo, operateType, compensateNo, serialNo, payRefReason);
		}
	}

	/**
	 * ????????????????????????
	 *
	 * @param settleNo     ????????????
	 * @param accountNo    ????????????
	 * @param operateType  ????????????
	 * @param compensateNo ????????????
	 * @param serialNo     ??????
	 * @param payRefReason ????????????
	 * @return ???????????????????????????
	 */
	@Override
	public int updatePrePaySettleNo(String settleNo, String accountNo, String operateType, String compensateNo, String serialNo, String payRefReason) {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" from PrpLPrePay prePay where 1=1 ");
		sqlUtil.append(" and prePay.serialNo=? ");
		sqlUtil.addParamValue(serialNo);
		sqlUtil.append(" and prePay.compensateNo=? ");
		sqlUtil.addParamValue(compensateNo);
		sqlUtil.append(" and exists(select 1 from PrpLPayCustom custom where custom.id=prePay.payeeId) ");

		String chargeCode = CodeConstants.TransPayTypeMap.get(payRefReason);
		if (StringUtils.isNotBlank(chargeCode)) {
			sqlUtil.append(" and prePay.chargeCode=? ");
			sqlUtil.addParamValue(chargeCode);
		}

		//operateType==0?????????????????????
		if ("0".equals(operateType)) {
			sqlUtil.append(" and prePay.settleNo=? ");
			sqlUtil.addParamValue(settleNo);
		}
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();

		List<PrpLPrePay> prePayList = databaseDao.findAllByHql(PrpLPrePay.class, sql, values);
		int count = 0;
		if (prePayList != null && prePayList.size() > 0) {
			if ("1".equals(operateType)) {
				for (PrpLPrePay prePay : prePayList) {
					count++;
					prePay.setSettleNo(settleNo);
				}
			} else if ("0".equals(operateType)) {
				for (PrpLPrePay prePay : prePayList) {
					count++;
					prePay.setSettleNo(null);
				}
			}
		}
		return count;
	}

	/**
	 * ??????????????????????????????
	 *
	 * @param settleNo     ????????????
	 * @param accountNo    ????????????
	 * @param operateType  ????????????
	 * @param compensateNo ????????????
	 * @param serialNo     ??????
	 * @param payRefReason ????????????
	 * @return ???????????????????????????
	 */
	@Override
	public int updateCompensateSettleNo(String settleNo, String accountNo, String operateType, String compensateNo, String serialNo, String payRefReason) {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" from PrpLPayment payment where 1=1 ");
		sqlUtil.append(" and payment.serialNo=? ");
		sqlUtil.addParamValue(serialNo);
		sqlUtil.append(" and payment.prpLCompensate.compensateNo=? ");
		sqlUtil.addParamValue(compensateNo);
		sqlUtil.append(" and exists(select 1 from PrpLPayCustom custom where custom.id=payment.payeeId) ");
		//operateType==0?????????????????????
		if ("0".equals(operateType)) {
			sqlUtil.append(" and payment.settleNo=? ");
			sqlUtil.addParamValue(settleNo);
		}
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();

		List<PrpLPayment> paymentList = databaseDao.findAllByHql(PrpLPayment.class, sql, values);
		int count = 0;
		if (paymentList != null && paymentList.size() > 0) {
			if ("1".equals(operateType)) {
				for (PrpLPayment payment : paymentList) {
					count++;
					payment.setSettleNo(settleNo);
				}
			} else if ("0".equals(operateType)) {
				for (PrpLPayment payment : paymentList) {
					count++;
					payment.setSettleNo(null);
				}
			}
		}
		return count;
	}

	/**
	 * ??????????????????????????????
	 *
	 * @param settleNo     ????????????
	 * @param accountNo    ????????????
	 * @param operateType  ????????????
	 * @param compensateNo ????????????
	 * @param serialNo     ??????
	 * @param payRefReason ????????????
	 * @return ???????????????????????????
	 */
	@Override
	public int updateCompensateChargeSettleNo(String settleNo, String accountNo, String operateType, String compensateNo, String serialNo, String payRefReason) {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" from PrpLCharge charge where 1=1 ");
		sqlUtil.append(" and charge.serialNo=? ");
		sqlUtil.addParamValue(serialNo);
		sqlUtil.append(" and charge.prpLCompensate.compensateNo=? ");
		sqlUtil.addParamValue(compensateNo);
		sqlUtil.append(" and exists(select 1 from PrpLPayCustom custom where custom.id=charge.payeeId) ");

		String chargeCode = CodeConstants.TransPayTypeMap.get(payRefReason);
		sqlUtil.append(" and charge.chargeCode=? ");
		sqlUtil.addParamValue(chargeCode);
		//operateType==0?????????????????????
		if ("0".equals(operateType)) {
			sqlUtil.append(" and charge.settleNo=? ");
			sqlUtil.addParamValue(settleNo);
		}
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		List<PrpLCharge> chargeList = databaseDao.findAllByHql(PrpLCharge.class, sql, values);
		int count = 0;
		if (chargeList != null && chargeList.size() > 0) {
			if ("1".equals(operateType)) {
				for (PrpLCharge charge : chargeList) {
					count++;
					charge.setSettleNo(settleNo);
				}
			} else if ("0".equals(operateType)) {
				for (PrpLCharge charge : chargeList) {
					count++;
					charge.setSettleNo(null);
				}
			}
		}
		return count;
	}

	/*
	 * ???????????????????????????
	 */
	public void updateSettleNoForPrePay(String settleNo,String accountNo,String operateType,
			String compensateNo,String serialNo,String payRefReason){
		
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" from PrpLPrePay prePay where 1=1 ");
		sqlUtil.append(" and prePay.serialNo=? ");
		sqlUtil.addParamValue(serialNo);
		sqlUtil.append(" and prePay.compensateNo=? ");
		sqlUtil.addParamValue(compensateNo);
		sqlUtil.append(" and exists(select 1 from PrpLPayCustom custom where custom.id=prePay.payeeId and custom.accountNo=?) ");
		sqlUtil.addParamValue(accountNo);
		
		String chargeCode = CodeConstants.TransPayTypeMap.get(payRefReason);
		if(StringUtils.isNotBlank(chargeCode)){
			sqlUtil.append(" and prePay.chargeCode=? ");
			sqlUtil.addParamValue(chargeCode);
		}
		
		//operateType==0?????????????????????
		if("0".equals(operateType)){
			sqlUtil.append(" and prePay.settleNo=? ");
			sqlUtil.addParamValue(settleNo);
		}
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		
		List<PrpLPrePay> prePayList = databaseDao.findAllByHql(PrpLPrePay.class, sql, values);
		if(prePayList!=null && prePayList.size()>0){
			if("1".equals(operateType)){
				for(PrpLPrePay prePay:prePayList){
					prePay.setSettleNo(settleNo);
				}
			}else if("0".equals(operateType)){
				for(PrpLPrePay prePay:prePayList){
					prePay.setSettleNo(null);
				}
			}
		}
	}
	/*
	 * ?????????????????????????????????
	 */
	public void updateSettleNoForPayment(String settleNo,String accountNo,String operateType,
			String compensateNo,String serialNo){
		
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" from PrpLPayment payment where 1=1 ");
		sqlUtil.append(" and payment.serialNo=? ");
		sqlUtil.addParamValue(serialNo);
		sqlUtil.append(" and payment.prpLCompensate.compensateNo=? ");
		sqlUtil.addParamValue(compensateNo);
		sqlUtil.append(" and exists(select 1 from PrpLPayCustom custom where custom.id=payment.payeeId and custom.accountNo=?) ");
		sqlUtil.addParamValue(accountNo);
		//operateType==0?????????????????????
		if("0".equals(operateType)){
			sqlUtil.append(" and payment.settleNo=? ");
			sqlUtil.addParamValue(settleNo);
		}
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		
		List<PrpLPayment> paymentList = databaseDao.findAllByHql(PrpLPayment.class, sql, values);
		
		if(paymentList!=null && paymentList.size()>0){
			if("1".equals(operateType)){
				for(PrpLPayment payment:paymentList){
					payment.setSettleNo(settleNo);
				}
			}else if("0".equals(operateType)){
				for(PrpLPayment payment:paymentList){
					payment.setSettleNo(null);
				}
			}
		}
		
	}
	
	/*
	 * ?????????????????????????????????
	 */
	public void updateSettleNoForCharge(String settleNo,String accountNo,String operateType,
			String compensateNo,String serialNo,String payRefReason){

		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" from PrpLCharge charge where 1=1 ");
		sqlUtil.append(" and charge.serialNo=? ");
		sqlUtil.addParamValue(serialNo);
		sqlUtil.append(" and charge.prpLCompensate.compensateNo=? ");
		sqlUtil.addParamValue(compensateNo);
		sqlUtil.append(" and exists(select 1 from PrpLPayCustom custom where custom.id=charge.payeeId and custom.accountNo=?) ");
		sqlUtil.addParamValue(accountNo);
		
		String chargeCode = CodeConstants.TransPayTypeMap.get(payRefReason);
		sqlUtil.append(" and charge.chargeCode=? ");
		sqlUtil.addParamValue(chargeCode);
		//operateType==0?????????????????????
		if("0".equals(operateType)){
			sqlUtil.append(" and charge.settleNo=? ");
			sqlUtil.addParamValue(settleNo);
		}
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		List<PrpLCharge> chargeList = databaseDao.findAllByHql(PrpLCharge.class, sql, values);
		
		if(chargeList!=null && chargeList.size()>0){
			if("1".equals(operateType)){
				for(PrpLCharge charge:chargeList){
					charge.setSettleNo(settleNo);
				}
			}else if("0".equals(operateType)){
				for(PrpLCharge charge:chargeList){
					charge.setSettleNo(null);
				}
			}
		}
	}

    @Override
    public String findInsuredPhone(String registNo) {
        String insuredPhone = "";
        List<PrpLCompensateVo> compList =  this.findCompByRegistNo(registNo);
        if(compList != null && compList.size() > 0){
            for(PrpLCompensateVo comp : compList ){
                if(!"".equals(comp.getinsuredPhone())){
                    return comp.getinsuredPhone();
                }
            }
        }
        List<PrpLPayCustomVo> payCustomList = managerService.findPayCustomVoByRegistNo(registNo);
        if(payCustomList != null && payCustomList.size() > 0){
            for(PrpLPayCustomVo payCustom : payCustomList){
                if("2".equals(payCustom.getPayObjectKind()) && !"".equals(payCustom.getPayeeMobile())){
                    return payCustom.getPayeeMobile();
                }
            }
        }
        return insuredPhone;
    }

    
    @Override
    public List<PrpLPrePayVo> findPrpLPrePayVoBySettleNo(String settleNo) {
        List<PrpLPrePayVo> prpLPrePayVoList = new ArrayList<PrpLPrePayVo>();
        QueryRule pfqr = QueryRule.getInstance();
        pfqr.addEqual("settleNo",settleNo);
        List<PrpLPrePay> prpLPrePays = databaseDao.findAll(PrpLPrePay.class,pfqr);
        if(prpLPrePays!=null && prpLPrePays.size()>0){
            prpLPrePayVoList = Beans.copyDepth().from(prpLPrePays).toList(PrpLPrePayVo.class);
        }
        return prpLPrePayVoList;
    
    }

    @Override
    public List<PrpLPaymentVo> findPrpLPaymentVoBySettleNo(String settleNo) {
        List<PrpLPaymentVo> prpLPaymentVoList = new ArrayList<PrpLPaymentVo>();
        QueryRule pfqr = QueryRule.getInstance();
        pfqr.addEqual("settleNo",settleNo);
        List<PrpLPayment> prpLPayments = databaseDao.findAll(PrpLPayment.class,pfqr);
        if(prpLPayments!=null && prpLPayments.size()>0){
            prpLPaymentVoList = Beans.copyDepth().from(prpLPayments).toList(PrpLPaymentVo.class);
        }
        return prpLPaymentVoList;
    
    }

    @Override
    public List<PrpLChargeVo> findPrpLChargeVoBySettleNo(String settleNo) {
        List<PrpLChargeVo> prpLChargeVoList = new ArrayList<PrpLChargeVo>();
        QueryRule pfqr = QueryRule.getInstance();
        pfqr.addEqual("settleNo",settleNo);
        List<PrpLCharge> prpLCharges = databaseDao.findAll(PrpLCharge.class,pfqr);
        if(prpLCharges!=null && prpLCharges.size()>0){
            prpLChargeVoList = Beans.copyDepth().from(prpLCharges).toList(PrpLChargeVo.class);
        }
        return prpLChargeVoList;
    
    }
    
    @Override
    public List<PrpLChargeVo> findChargeVoByChargeCode(String compensateNo,String chargeCode){
    	List<PrpLChargeVo> prpLChargeVoList = new ArrayList<PrpLChargeVo>();
    	QueryRule qr = QueryRule.getInstance();
    	qr.addEqual("compensateNo",compensateNo);
    	qr.addEqual("chargeCode",chargeCode);
    	List<PrpLCharge> prpLCharges = databaseDao.findAll(PrpLCharge.class,qr);
    	if(prpLCharges!=null && prpLCharges.size()>0){
            prpLChargeVoList = Beans.copyDepth().from(prpLCharges).toList(PrpLChargeVo.class);
        }
        return prpLChargeVoList;
    }

    @Override
    public List<SendMsgParamVo> getSendMsgParamVoByRefund(PrpLRegistVo prpLRegistVo,List<PrpLPaymentVo> paymentVo,PrpLPayCustomVo payCustomVo) {
        SendMsgParamVo msgParamVo = new  SendMsgParamVo();
        msgParamVo.setRegistNo(prpLRegistVo.getRegistNo());
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
        if(prpLRegistVo.getPrpLRegistCarLosses().size()>=2){//??????????????????
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
        if(prpLRegistVo.getReportorPhone() != null && !"".equals(prpLRegistVo.getReportorPhone())){//???????????????
            msgParamVo.setReportoMobile(prpLRegistVo.getReportorPhone());
        }
        if(prpLRegistVo.getPrpLRegistExt()!=null&&prpLRegistVo.getPrpLRegistExt().getFrameNo()!=null
                &&!"".equals(prpLRegistVo.getPrpLRegistExt())){
            msgParamVo.setFrameNo(prpLRegistVo.getPrpLRegistExt().getFrameNo());
        }
        if(prpLRegistVo.getDamageTime() != null){
            msgParamVo.setDamageTime(prpLRegistVo.getDamageTime());
        }
        if(prpLRegistVo.getDamageAddress() != null){
            msgParamVo.setDamageAddress(prpLRegistVo.getDamageAddress());
        }
        if(prpLRegistVo.getPrpLRegistExt()!=null && prpLRegistVo.getPrpLRegistExt().getLicenseNo()!=null){
            msgParamVo.setLicenseNo(prpLRegistVo.getPrpLRegistExt().getLicenseNo());
        }
        
        List<SendMsgParamVo> msgParamVoList = new ArrayList<SendMsgParamVo>();
        for(PrpLPaymentVo vo:paymentVo){
            SendMsgParamVo msgParam = new  SendMsgParamVo();
            Beans.copy().from(msgParamVo).to(msgParam);
            //????????????
            if(vo.getSumRealPay().compareTo(BigDecimal.ZERO)==-1){
                return null;
            }
            msgParam.setSumAmt(vo.getSumRealPay().toString());
            //???????????????
            //PrpLPayCustomVo paycustomVo = managerService.findPayCustomVoById(vo.getPayeeId());
            PrpLPayCustomVo paycustomVo = payCustomVo;
            //????????????
            String accountNo = paycustomVo.getAccountNo();
            //???????????????
            msgParam.setPayeeName(paycustomVo.getPayeeName());
            //???????????????????????????????????????????????????
            msgParam.setDamageReason(paycustomVo.getCertifyNo());
            //??????id???usercode?????????paycustomVo
            //msgParam.setUseCode(vo.getPayeeId().toString());
            //??????
            //msgParam.setSummary(paycustomVo.getSummary());
            //???????????????????????????????????????????????????
            if(paycustomVo.getPayeeMobile()!=null && !"".equals(paycustomVo.getPayeeMobile())){
                msgParam.setPhoneNo(paycustomVo.getPayeeMobile());
                msgParamVoList.add(msgParam);
            }
        }
        //???????????????????????????????????????????????????
        if(msgParamVoList!=null && msgParamVoList.size() > 1){
            BigDecimal sumAmt = new BigDecimal(0);
            for(SendMsgParamVo vo : msgParamVoList){
                if((!msgParamVoList.get(0).getDamageReason().equals(vo.getDamageReason())) &&
                        (!msgParamVoList.get(0).getPayeeName().equals(vo.getPayeeName()))){
                    return null;
                }
                //??????????????????????????????1????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                //??????
                sumAmt=sumAmt.add(new BigDecimal(vo.getSumAmt()));
            }
            //??????????????????????????????1????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            if(sumAmt.compareTo(new BigDecimal(50000))==1){
                return null;
            }
        }else if(msgParamVoList!=null && msgParamVoList.size() ==1){
            BigDecimal sumAmt = new BigDecimal(0);
            //??????????????????????????????5????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            //??????
            sumAmt=new BigDecimal(msgParamVoList.get(0).getSumAmt());
            //??????????????????????????????5????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            if(sumAmt.compareTo(new BigDecimal(50000))==1){
                return null;
            }
        }
        return msgParamVoList;
    }

	@Override
	public PrpLPaymentVo findByPayeeNameAndAccountNo(String payeeName,String accountNo,Long paycustmId) {
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		PrpLPaymentVo mentVo=new PrpLPaymentVo();
		sqlUtil.append(" from PrpLPayCustom a where 1=1 ");
		if(StringUtils.isNotBlank(accountNo)){
			sqlUtil.append("AND a.accountNo=? ");
			sqlUtil.addParamValue(accountNo);
		}else{
			return mentVo;
		}
		sqlUtil.append(" AND (exists(select 1 from PrpLPayment b where b.payeeId=a.id and b.payStatus=? ) or exists(select 1 from PrpLPrePay c where c.payeeId=a.id and c.payStatus=? ) or exists(select 1 from PrpLPadPayPerson d where d.payeeId=a.id and d.payStatus=? ) or exists(select 1 from PrpLCharge e where e.payeeId=a.id and e.payStatus=? ))");
		sqlUtil.addParamValue("1");
		sqlUtil.addParamValue("1");
		sqlUtil.addParamValue("1");
		sqlUtil.addParamValue("1");
		sqlUtil.append(" order By a.createTime desc ");
		
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		List<PrpLPayCustom> payCustomList = databaseDao.findAllByHql(PrpLPayCustom.class, sql, values);
		//????????????????????????????????????????????????????????????????????????,paycustmId????????????????????????Id
		List<PrpLPayCustom> payCustompoList =new ArrayList<PrpLPayCustom>();
		if(payCustomList!=null && payCustomList.size()>0){
			for(PrpLPayCustom po:payCustomList){
				if(!po.getId().equals(paycustmId)){
					payCustompoList.add(po);
				}
			}
		}
		if(payCustompoList!=null && payCustompoList.size()>0){
			if(!payeeName.equals(payCustomList.get(0).getPayeeName())){
				mentVo.setPayeeName(payCustomList.get(0).getPayeeName());
				mentVo.setAccountNo(payCustomList.get(0).getAccountNo());
				mentVo.setRegistNo(payCustomList.get(0).getRegistNo());
			}
			
		}
		return mentVo;
	}
	
	 /**
		 * ???????????????????????????????????????
		 * 
		 * @param registNo
		 * @return
		 */
		public List<PrpLLossItemVo> findLossItemsByRegistNo(String registNo) {
			List<PrpLLossItemVo> lossItems = new ArrayList<PrpLLossItemVo>();			
			if (!StringUtils.isBlank(registNo)) {
				QueryRule qr = QueryRule.getInstance();
				qr.addEqual("registNo", registNo);
				List<PrpLLossItem> lossItemList = databaseDao.findAll(PrpLLossItem.class, qr);
				if (lossItemList!=null && lossItemList.size() > 0) {
					for (PrpLLossItem lossItem : lossItemList) {
						PrpLLossItemVo lossItemVo = new PrpLLossItemVo();
						Beans.copy().from(lossItem).to(lossItemVo);
						lossItems.add(lossItemVo);
					}
				}
			}

			return lossItems;
		}
		
	@Override
	public List<PrpLCompensateVo> findCompensatevosByclaimNo(String claimNo) {
		
		return compensateService.findCompensatevosByclaimNo(claimNo);
	}
	
    @Override
	public boolean isExistWriteOff(String claimNo,String compeType){
    	return compensateService.isExistWriteOff(claimNo,compeType);
    }
    
	@Override
	public PrpLCompensateVo autoCompensate(Double flowTaskId,SysUserVo user) throws Exception{
		PrpLCompensateVo compVo = compensateAutoService.autoCompensate(flowTaskId,user);
		return compVo;
	}
	@Override
	public PrpLWfTaskVo submitAutoCompTask(PrpLWfTaskVo wfTaskVo,PrpLCompensateVo compVo,SysUserVo user) throws Exception{
		
		//????????????????????????????????????????????????????????????ilog????????? //TODO ????????????sumamtNow???????????????
//		Boolean autoVerifyFlag = this.getAutoVerifyFlag(wfTaskVo,compVo.getRegistNo(),compVo.getClaimNo(),compVo.getSumPaidAmt(),user);
		Boolean autoVerifyFlag = false;
		
		//??????????????????????????????
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("registNo",wfTaskVo.getRegistNo());
		if(wfTaskVo.getTaskId() == null){
			params.put("taskId",BigDecimal.ZERO.doubleValue());	
		} else{
			params.put("taskId",wfTaskVo.getTaskId().doubleValue());
		}
		String isSubmitHeadOffice = managerService.isSubmitHeadOffice(params);
		WfTaskSubmitVo nextVo = this.getCompensateSubmitNextVo(compVo.getCompensateNo(),wfTaskVo.getTaskId().doubleValue(),wfTaskVo,user,autoVerifyFlag,isSubmitHeadOffice);
		
		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(nextVo.getFlowTaskId().doubleValue());
		PrpLWfTaskVo upTaskVo = wfTaskHandleService.queryTask(taskVo.getUpperTaskId().doubleValue());
		if(nextVo.getSubmitLevel()==0){
			autoVerifyFlag = true;
		}
		PrpLWfTaskVo vclaimTaskVo = this.submitCompeWfTaskVo(compVo,upTaskVo,nextVo,autoVerifyFlag.toString(),user);
		return vclaimTaskVo;
	}
	
	@Override
	public boolean getAutoVerifyFlag(PrpLWfTaskVo wfTaskVo,PrpLCompensateVo compeVo,SysUserVo userVo){
		boolean autoVerifyFlag = false;
		String registNo = compeVo.getRegistNo();
		String claimNo = compeVo.getClaimNo();
		BigDecimal sumAmtNow = compeVo.getSumAmt();
		PrpLCertifyMainVo certifyMainVo = certifyPubService.findPrpLCertifyMainVoByRegistNo(registNo);
		// ????????????????????????????????????  ??????????????????????????????????????????  ???????????????????????????
		//????????????????????????
		if(!CodeConstants.WorkStatus.BYBACK.equals(wfTaskVo.getWorkStatus())
				&&!CodeConstants.WorkStatus.BACK.equals(wfTaskVo.getWorkStatus())){
			PrpLAutoVerifyVo prpLAutoVerifyVo = new PrpLAutoVerifyVo();
			//????????????????????????????????????2000
			//??????????????????????????????????????????????????????????????????????????????????????????????????????2000
			List<PrpLCompensateVo> compList = this.findCompListByClaimNo(claimNo,"N");//???????????????????????????
			BigDecimal sumAmt = sumAmtNow;
			if(compList!=null&&compList.size()>0){
				for(PrpLCompensateVo compVo : compList){
					sumAmt = sumAmt.add(compVo.getSumPaidAmt());
				}
			}
			prpLAutoVerifyVo.setUserCode(userVo.getUserCode());
			prpLAutoVerifyVo.setComCode(userVo.getComCode());
			prpLAutoVerifyVo.setNode(wfTaskVo.getSubNodeCode());// ????????????
			prpLAutoVerifyVo.setMoney(sumAmt);
			if(sumAmt.compareTo(BigDecimal.ZERO)!=-1){//???????????????????????????
				autoVerifyFlag = deflossHandleService.isAutoVerifyUser(prpLAutoVerifyVo);
			}
			
			if(autoVerifyFlag&&BigDecimal.ZERO.compareTo(sumAmtNow)==-1){
				// ??????????????????????????????0???????????????????????????		
				List<PrpLDlossPersTraceMainVo> dlossPersTraceMainVoList = persTraceDubboService.findPersTraceMainVoList(registNo);
				if(dlossPersTraceMainVoList!=null&&dlossPersTraceMainVoList.size()>0){
					for(PrpLDlossPersTraceMainVo traceMainVo : dlossPersTraceMainVoList){
						if(!"10".equals(traceMainVo.getCaseProcessType())
								&&traceMainVo.getPrpLDlossPersTraces()!=null
								&&traceMainVo.getPrpLDlossPersTraces().size()>0){
								for(PrpLDlossPersTraceVo traceVo : traceMainVo.getPrpLDlossPersTraces()){
									if(BigDecimal.ZERO.compareTo(DataUtils.NullToZero(traceVo.getSumVeriDefloss()))==-1){
										autoVerifyFlag = false;
										break;
									}
								}
						}
						if(!autoVerifyFlag){
							logger.info("??????????????????????????????0????????????????????????	");
							break;
						}
					}
				}
			}
			//????????????????????????0?????????????????????	
			if(autoVerifyFlag && compeVo.getCisderoAmout()!=null && 
					compeVo.getCisderoAmout().compareTo(BigDecimal.ZERO)==1){
				autoVerifyFlag = false;
			}
			//????????????????????????????????????
			if(autoVerifyFlag){
				if("1101".equals(compeVo.getRiskCode())){
					if("1".equals(certifyMainVo.getIsJQFraud())){
						autoVerifyFlag = false;
					}
				}else{
					if("1".equals(certifyMainVo.getIsSYFraud())){
						autoVerifyFlag = false;
					}
				}
			}
		}
		return autoVerifyFlag;
	}
    
    /**
     *
     * @param compensateId ????????????
     * @param flowTaskId ???????????????????????????taskid
     * @param wfTaskVo ??????????????????Vo
     * @param userVo ????????????
     * @param autoVerifyFlag  ????????????????????????  true-???   false-???
     * @return
     */
	@Override
	public WfTaskSubmitVo getCompensateSubmitNextVo(String compensateId, Double flowTaskId,PrpLWfTaskVo wfTaskVo,
	                                                SysUserVo userVo,boolean autoVerifyFlag,String isSubmitHeadOffice){
		logger.info(wfTaskVo.getRegistNo() + "??????????????????getCompensateSubmitNextVo????????? compensateId= " + compensateId +", flowTaskId="+flowTaskId+", autoVerifyFlag="+autoVerifyFlag +", wfTaskVo.getSubNodeCode() = "+wfTaskVo.getSubNodeCode());
		WfTaskSubmitVo nextVo = new WfTaskSubmitVo();
		String nextNode = null;
		PrpLCompensateVo compeVo = compensateService.findCompByPK(compensateId);
		if (!autoVerifyFlag) {
			PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.ILOGVALIDFLAG, userVo.getComCode());
			if ("1".equals(configValueVo.getConfigValue())) {//??????ilog
				//ilog????????????
				LIlogRuleResVo vPriceResVo = null;
				try {
					vPriceResVo = compensateHandleServiceIlogService.organizaForCompensate(compensateId, "1", "00", BigDecimal.valueOf(flowTaskId), FlowNode.Compe.name(), userVo, wfTaskVo,isSubmitHeadOffice);
					if (vPriceResVo != null) {
						logger.info(wfTaskVo.getRegistNo() + "??????????????????ILOG???????????? vPriceResVo.getUnderwriterflag() = " + vPriceResVo.getUnderwriterflag());
					}
				} catch (Exception e) {
					logger.error("?????????" + (wfTaskVo == null ? null: wfTaskVo.getRegistNo())+"??????????????????ILOG????????????",e);
				}

				/*	???????????????????????? start  */
				String finalPowerFlag = SpringProperties.getProperty("FINALPOWERFLAG");
				if ("1".equals(finalPowerFlag)) {
					IlogFinalPowerInfoVo powerInfoVo = ilogRuleService.findByUserCode(userVo.getUserCode());
					if (powerInfoVo != null) {
						BigDecimal gradePower = powerInfoVo.getGradeAmount();
						if (gradePower != null) {
							BigDecimal sumAmount = BigDecimal.ZERO;//?????????????????????
							PrpLCompensateVo compensateVo = compensateService.findCompByPK(compensateId);
							// callNode: 00-???????????? 01-?????? 02-?????? 03-???????????? 04-???????????? 05-?????????????????? 99-??????
							List<PrpLCompensateVo> prpLCompensateVoList = compensateService.findCompensatevosByclaimNo(compensateVo.getClaimNo());
							for (PrpLCompensateVo lCompensateVo : prpLCompensateVoList) {
								if (!"7".equals(lCompensateVo.getUnderwriteFlag())) {
									if (lCompensateVo.getSumAmt() != null) {
										sumAmount = sumAmount.add(lCompensateVo.getSumAmt().abs());
									}
									if (lCompensateVo.getSumFee() != null) {
										sumAmount = sumAmount.add(lCompensateVo.getSumFee().abs());
									}
								}
							}

							if (sumAmount.compareTo(gradePower) == 1) {
								autoVerifyFlag = false;
							} else {
                                autoVerifyFlag = true;
                            }
						} else {
							autoVerifyFlag = false;
						}
					} else {
						autoVerifyFlag = false;
					}
					logger.info(wfTaskVo.getRegistNo() + "?????????????????????????????????autoVerifyFlag=" + autoVerifyFlag);
				}
				/*	????????????????????????  end   */

				if (!"1".equals(vPriceResVo.getUnderwriterflag()) || ("1".equals(finalPowerFlag) && !autoVerifyFlag)) {//?????????
					autoVerifyFlag = false;
					try {
						vPriceResVo = compensateHandleServiceIlogService.organizaForCompensate(compensateId, "2", "00", BigDecimal.valueOf(flowTaskId), FlowNode.Compe.name(), userVo, wfTaskVo,isSubmitHeadOffice);
						if (vPriceResVo != null) {
							logger.info(wfTaskVo.getRegistNo() + "??????????????????ILOG???????????? vPriceResVo.getUnderwriterflag() = " + vPriceResVo.getUnderwriterflag() +", vPriceResVo.getMinUndwrtNode()=" + vPriceResVo.getMinUndwrtNode() + ", vPriceResVo.getMaxUndwrtNode()=" + vPriceResVo.getMaxUndwrtNode());
						}
					} catch (Exception e) {
						logger.error("?????????" + (wfTaskVo == null ? null: wfTaskVo.getRegistNo())+ "????????????????????????????????????ILOG????????????",e);
					}
					if (Integer.parseInt(vPriceResVo.getMinUndwrtNode()) > Integer.parseInt(vPriceResVo.getMaxUndwrtNode())) {
						nextVo.setSubmitLevel(Integer.parseInt(vPriceResVo.getMaxUndwrtNode()));
					} else {
						nextVo.setSubmitLevel(Integer.parseInt(vPriceResVo.getMinUndwrtNode()));
					}
					if ("CompeCI".equals(wfTaskVo.getSubNodeCode())) {
						nextNode = "VClaim_CI_LV" + nextVo.getSubmitLevel();
					} else {
						nextNode = "VClaim_BI_LV" + nextVo.getSubmitLevel();
					}
					int maxLevel = Integer.parseInt(vPriceResVo.getMaxUndwrtNode());
					boolean haveUser = false;
					for (int i = nextVo.getSubmitLevel(); i < maxLevel; i++) {
						haveUser = assignService.existsGradeUser(FlowNode.valueOf(nextNode), userVo.getComCode());
						if (!haveUser) {
							if ("CompeCI".equals(wfTaskVo.getSubNodeCode())) {
								nextNode = "VClaim_CI_LV" + (i + 1);
							} else {
								nextNode = "VClaim_BI_LV" + (i + 1);
							}
						} else {
							break;
						}
					}
				} else {
					autoVerifyFlag = true;
				}
			}
			//??????????????????
			PrpLConfigValueVo configRuleValueVo = ConfigUtil.findConfigByCode(CodeConstants.RULEVALIDFLAG, userVo.getComCode());
			if ("1".equals(configRuleValueVo.getConfigValue())) {
				//??????ilog???????????????????????????
				autoVerifyFlag = this.getAutoVerifyFlag(wfTaskVo, compeVo, userVo);
				VerifyClaimRuleVo ruleVo = new VerifyClaimRuleVo();
				ruleVo = organizeRuleVo(compensateId, userVo);
				if (ruleVo.getBackLevel() > ruleVo.getMaxLevel()) {
					nextVo.setSubmitLevel(ruleVo.getMaxLevel());
				} else {
					nextVo.setSubmitLevel(ruleVo.getBackLevel());
				}
				if ("CompeCI".equals(wfTaskVo.getSubNodeCode())) {
					nextNode = "VClaim_CI_LV" + nextVo.getSubmitLevel();
				} else {
					nextNode = "VClaim_BI_LV" + nextVo.getSubmitLevel();
				}
				boolean haveUser = false;
				for (int i = nextVo.getSubmitLevel(); i < ruleVo.getMaxLevel(); i++) {
					haveUser = assignService.existsGradeUser(FlowNode.valueOf(nextNode), userVo.getComCode());
					if (!haveUser) {
						if ("CompeCI".equals(wfTaskVo.getSubNodeCode())) {
							nextNode = "VClaim_CI_LV" + (i + 1);
						} else {
							nextNode = "VClaim_BI_LV" + (i + 1);
						}
					} else {
						break;
					}
				}
			}
		}
		
		nextVo.setTaskInUser(userVo.getUserCode());
		if (autoVerifyFlag) {
			nextVo.setSubmitLevel(0);
			if ("CompeCI".equals(wfTaskVo.getSubNodeCode())) {
				nextNode = FlowNode.VClaim_CI_LV0.toString();
			} else {
				nextNode = FlowNode.VClaim_BI_LV0.toString();
			}
				
		}
		logger.info(wfTaskVo.getRegistNo()+"??????????????????getCompensateSubmitNextVo(), nextNode = "+nextNode);
		nextVo.setFlowTaskId(BigDecimal.valueOf(flowTaskId));
		// nextVo.setSubmitType(submitType);
		nextVo.setComCode(userVo.getComCode());
		nextVo.setFlowId(wfTaskVo.getFlowId());
		nextVo.setTaskInUser(userVo.getUserCode());
		nextVo.setCurrentNode(FlowNode.valueOf(wfTaskVo.getSubNodeCode()));
		nextVo.setNextNode(FlowNode.valueOf(nextNode));
		nextVo.setTaskInKey(compensateId);
		return nextVo;
	}
	
	@Override
	public PrpLWfTaskVo submitCompeWfTaskVo(PrpLCompensateVo prpLCompensateVo,PrpLWfTaskVo upTaskVo,
	                                        WfTaskSubmitVo nextVo,String autoVerifyFlag,SysUserVo userVo){
		logger.info("?????????registno="+(prpLCompensateVo == null ? null: prpLCompensateVo.getRegistNo())+ ",????????????=" 
			+ (prpLCompensateVo == null ? null: prpLCompensateVo.getCompensateNo()) +	"???????????????????????????????????????????????? ");
		PrpLWfTaskVo wfTaskVo = new PrpLWfTaskVo();
		if (upTaskVo.getSubNodeCode().startsWith("VClaim_") && nextVo.getNextNode().equals(upTaskVo.getSubNodeCode())) {//???????????????????????????????????????????????????
			boolean isHoliday = assignService.validUserCode(upTaskVo.getHandlerUser());
			if (isHoliday) {
				nextVo.setAssignUser(upTaskVo.getHandlerUser());
				nextVo.setAssignCom(upTaskVo.getHandlerCom());
			} else {
				// ????????????
				PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.ISSPECIFIEDUSER, prpLCompensateVo.getComCode());
				if ("1".equals(configValueVo.getConfigValue())) {
					assignUser(prpLCompensateVo, nextVo, userVo, prpLCompensateVo.getComCode());
				} else {
					setAssignUser(nextVo, userVo, prpLCompensateVo.getComCode());
				}
			}
		} else {
			if (!"true".equals(autoVerifyFlag.trim())) {
				// ????????????
				PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.ISSPECIFIEDUSER, prpLCompensateVo.getComCode());
				if ("1".equals(configValueVo.getConfigValue())) {
					assignUser(prpLCompensateVo, nextVo, userVo, prpLCompensateVo.getComCode());
				} else {
					setAssignUser(nextVo, userVo, prpLCompensateVo.getComCode());
				}
			}
		}
		logger.info("?????????registno="+(prpLCompensateVo == null ? null: prpLCompensateVo.getRegistNo())+ ",????????????=" 
				+ (prpLCompensateVo == null ? null: prpLCompensateVo.getCompensateNo()) +	",???????????????=" + (nextVo== null ? null : nextVo.getNextNode())
				+  "????????????=" + (nextVo== null ? null : nextVo.getAssignCom()) + "??????????????????=" + (nextVo== null ? null :nextVo.getAssignCom()));
			
		String policyCom = policyViewService.getPolicyComCode(prpLCompensateVo.getRegistNo());
		nextVo.setComCode(policyCom);
		if ("true".equals(autoVerifyFlag.trim())) {
			nextVo.setAssignUser("AUTO");
			nextVo.setAssignCom(policyCom);
		}
		
		wfTaskVo = wfTaskHandleService.submitCompe(prpLCompensateVo, nextVo);
		//??????????????????????????????????????????????????????
		fxInfoType(prpLCompensateVo.getCompensateNo(),prpLCompensateVo.getPolicyNo(),prpLCompensateVo.getRiskCode(),userVo);
		//????????????????????????????????????????????????????????????
		fxqRiskValid(prpLCompensateVo.getCompensateNo());
		logger.info("?????????registno="+(prpLCompensateVo == null ? null: prpLCompensateVo.getRegistNo())+ ",????????????=" 
				+ (prpLCompensateVo == null ? null: prpLCompensateVo.getCompensateNo()) +	"???????????????????????????????????????????????? ");
		return wfTaskVo;
	}
	
	//????????????
	private void setAssignUser( WfTaskSubmitVo nextVo,SysUserVo userVo,String comCode) {
		if(StringUtils.isNotBlank(comCode)){
			userVo.setComCode(comCode);
		}
		SysUserVo assUserVo = assignService.execute(nextVo.getNextNode(),userVo.getComCode(),userVo, "");
		if (assUserVo == null) {
			throw new IllegalArgumentException(nextVo.getNextNode().getName() + "???????????????");
		}
		nextVo.setAssignUser(assUserVo.getUserCode());
		nextVo.setAssignCom(assUserVo.getComCode());
	}

	/**
	 * ??????????????????????????????  ??????1706-??????????????????????????????????????????
	 * @param prpLCompensateVo ???????????????
	 * @param nextVo ???????????????????????????
	 * @param userVo ?????????????????????
	 * @param comCode ??????????????????
	 * @author mfn 2019-12-4 15:42:51
	 */
	private void assignUser(PrpLCompensateVo prpLCompensateVo, WfTaskSubmitVo nextVo,SysUserVo userVo,String comCode) {
		try {
			if (StringUtils.isNotBlank(comCode)) {
				userVo.setComCode(comCode);
			}
			// ????????????????????????
			String currentCompensateNo = prpLCompensateVo.getCompensateNo();
			// ????????????????????????
			String relatedCompensateNo = "";
			// ??????????????????????????? ????????????????????????
			List<PrpLCompensateVo> compensateVos = compensateService.findCompByRegistNo(prpLCompensateVo.getRegistNo());
			// ??????????????????????????????????????????????????????
			if (compensateVos.size() <= 1) {
				setAssignUser(nextVo, userVo, prpLCompensateVo.getComCode());
			} else {
				// ??????????????????????????????????????????1706????????????????????????
				// ????????????????????????????????????
				for (PrpLCompensateVo compensateVo : compensateVos) {
					if (compensateVo.getCompensateNo() != null && !(compensateVo.getCompensateNo().equals(currentCompensateNo))) {
						relatedCompensateNo = compensateVo.getCompensateNo();
						break;
					}
				}

				// ??????out???????????????????????????
				List<PrpLWfTaskVo> vClaimTaskVos = wfTaskHandleService.findEndTask(prpLCompensateVo.getRegistNo(), relatedCompensateNo, FlowNode.VClaim);
				// ??????out???????????????????????????????????????????????????
				PrpLWfTaskVo earliestTaskVo = new PrpLWfTaskVo();
				if (vClaimTaskVos != null && vClaimTaskVos.size() > 0) {
					earliestTaskVo = vClaimTaskVos.get(vClaimTaskVos.size()-1);
				} else {
					// ??????out???????????????????????????in?????????????????????
					vClaimTaskVos = wfTaskHandleService.findInTaskByOther(prpLCompensateVo.getRegistNo(), relatedCompensateNo, FlowNode.VClaim.name());
					if (vClaimTaskVos != null && vClaimTaskVos.size() > 0) {
						earliestTaskVo = vClaimTaskVos.get(0);
					} else {
						// ??????in???out??????????????????????????????????????????
						setAssignUser(nextVo, userVo, prpLCompensateVo.getComCode());
					}
				}

				// ????????????????????????????????????????????????????????????????????????????????????
				if (StringUtils.isNotBlank(earliestTaskVo.getAssignUser())) {
					String usercode = earliestTaskVo.getAssignUser();
					boolean isUserCanHandle = assignService.isUserCouldHandle(nextVo.getNextNode(), userVo.getComCode(), usercode);
					if (isUserCanHandle) {
						// ??????????????????????????????
						nextVo.setAssignUser(usercode);
						nextVo.setAssignCom(earliestTaskVo.getComCode());
					} else {
						// ???????????????????????????????????????????????????
						setAssignUser(nextVo, userVo, prpLCompensateVo.getComCode());
					}
				} else {
					setAssignUser(nextVo, userVo, prpLCompensateVo.getComCode());
				}
			}
		} catch (Exception e) {
			logger.info("??????????????? " + (prpLCompensateVo == null ? "" : prpLCompensateVo.getCompensateNo()) + " ???????????????????????????????????????", e);
			// ???????????????????????????????????????????????????
			setAssignUser(nextVo, userVo, prpLCompensateVo.getComCode());
		}
	}
	
    /**
     * ????????????????????????????????????????????????
     * @param compensateNo
     * @param policyNo
     * @param riskCode
     * @param userVo
     */
    private void fxInfoType(String compensateNo,String policyNo,String riskCode,SysUserVo userVo){
    	PrplcomcontextVo contextVo=compHandleService.findPrplcomcontextByCompensateNo(compensateNo,"C");
    	List<String> codes=new ArrayList<String>();
    	
    	String amlUrl = SpringProperties.getProperty("dhic.aml.url");
        logger.info("amlUrl============================"+amlUrl);
        FxqCrmRiskService crmRiskService = new FxqCrmRiskService(amlUrl);
        BussRiskInfoVo  bussriskinfovo=new BussRiskInfoVo();
        bussriskinfovo.setBussNo(compensateNo);
        bussriskinfovo.setBussType("C");
        bussriskinfovo.setComCode(userVo.getComCode());
        bussriskinfovo.setCustCode(userVo.getUserCode());
        bussriskinfovo.setRiskCode(riskCode);
        bussriskinfovo.setSystem("claimcar");
        bussriskinfovo.setRefBussNo(policyNo);
        
        if(contextVo!=null){
        	if(StringUtils.isNotBlank(contextVo.getFlagContext())){
        		String [] codeArray=contextVo.getFlagContext().split(",");
        		if(codeArray!=null && codeArray.length>0){
        			for(int i=0;i<codeArray.length;i++){
        				codes.add(codeArray[i]);
        			}
        		}
        		bussriskinfovo.setSusCodes(codes);
        		bussriskinfovo.setInvestigationInfo(contextVo.getCauses());
        	}
        }
        crmRiskService.saveSusRiskInfo(bussriskinfovo);
    }
    
    /**
     * ??????????????????????????????????????????????????????
     * @param compensateNo
     */
    private void fxqRiskValid(String compensateNo){
    	
    	PrplcomcontextVo contextVo=compHandleService.findPrplcomcontextByCompensateNo(compensateNo,"C");
    	String amlUrl = SpringProperties.getProperty("dhic.aml.url");
    	String strCode="";
    	String strCode1="";
        logger.info("amlUrl============================"+amlUrl);
        FxqCrmRiskService crmRiskService = new FxqCrmRiskService(amlUrl);
        BussRiskInfoReturnVo bussriskinforeturnvo=crmRiskService.validBussSusRiskInfo(compensateNo,false);
        if(bussriskinforeturnvo!=null && contextVo!=null){
        	if(StringUtils.isNotBlank(contextVo.getFlagContext())){
        		List<SusInfoVo> susinfovos=bussriskinforeturnvo.getBussRiskInfoVos();
        		if(susinfovos!=null && susinfovos.size()>0){
        			for(SusInfoVo vo:susinfovos){
        				strCode=strCode+vo.getSusCode()+",";
        			}
        		}
        		//????????????????????????
        		if(StringUtils.isNotBlank(strCode)){
        			strCode1=strCode.substring(0,strCode.length()-1);
        		}
        		if(StringUtils.isNotBlank(strCode1)){
        			contextVo.setFlagContext(contextVo.getFlagContext()+","+strCode1);
        		}
        		
        	}
        }
        try {
			compHandleService.saveOrUpdatePrplcomcontext(contextVo);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
    }
    @Override
    public PrpLCompensateVo autoCompTask(PrpLWfTaskVo taskVo,SysUserVo userVo){
/*   	LIlogRuleResVo ruleResVo = certifyIlogService.sendAutoCertifyRule(registNo,userVo,taskId,codeNode);
		if("1".equals(ruleResVo.getUnderwriterflag())){//??????????????????
			List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findInTaskByOther(registNo,null,FlowNode.Compe.toString());
			if(prpLWfTaskVoList!=null&&prpLWfTaskVoList.size()>0){
				for(PrpLWfTaskVo taskVo:prpLWfTaskVoList){
					
				}
			}
		}*/
        PrpLCompensateVo compVo = null;
		try{
			compVo = this.autoCompensate(taskVo.getTaskId().doubleValue(),userVo);
	           //????????????????????????????????????????????????????????????ilog????????? //TODO ????????????sumamtNow???????????????
	        Boolean autoVerifyFlag = false;
	        
	        String registNo = taskVo.getRegistNo();
	        BigDecimal taskId = taskVo.getTaskId(); 
	        Map<String,Object> params = new HashMap<String,Object>();
	        if(taskId == null){
	        	params.put("taskId",BigDecimal.ZERO.doubleValue());
	        } else{
		        params.put("taskId",taskId.doubleValue());
	        }
	        params.put("registNo",registNo);
	        String isSubmitHeadOffice = managerService.isSubmitHeadOffice(params);
	        WfTaskSubmitVo nextVo = this.getCompensateSubmitNextVo(compVo.getCompensateNo(),taskVo.getTaskId().doubleValue(),taskVo,userVo,autoVerifyFlag,isSubmitHeadOffice);
	        PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(nextVo.getFlowTaskId().doubleValue());
	        PrpLWfTaskVo upTaskVo = wfTaskHandleService.queryTask(wfTaskVo.getUpperTaskId().doubleValue());
	        if(nextVo.getSubmitLevel()==0){
	            autoVerifyFlag = true;
	        }
	        this.submitCompeWfTaskVo(compVo,upTaskVo,nextVo,autoVerifyFlag.toString(),userVo);
	        return compVo;
		}catch(Exception e){
			logger.error("??????????????????????????????,taskid=" + (taskVo == null ? null:taskVo.getFlowId()),e);
		}
		return compVo;
    }
    
    @Override
    public boolean adjustNotExistObj(String registNo){
    	return compensateAutoService.adjustNotExistObj(registNo);
    }
    
    @Override
    public List<PrpLCoinsVo> findPrpLCoins(String policyNo){
    	List<PrpLCoinsVo> PrpLCoinsVoList = new ArrayList<PrpLCoinsVo>();
    	SqlJoinUtils sqlUtil=new SqlJoinUtils();
    	sqlUtil.append("SELECT  A.POLICYNO,A.COINSTYPE,A.CHIEFFLAG,A.COINSCODE,A.COINSNAME,A.COINSRATE,B.CURRENCY,A.PROPORTIONFLAG   FROM YWUSER.PRPCCOINS A,YWUSER.PRPCCOINSDETAIL B WHERE 1=1  ");
    	sqlUtil.append(" AND A.POLICYNO=B.POLICYNO AND A.SERIALNO=B.SERIALNO AND A.POLICYNO= ? ");
    	sqlUtil.addParamValue(policyNo);
    	String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
		
		for(int i = 0; i<objects.size(); i++ ){
			Object[] obj = objects.get(i);
			PrpLCoinsVo coinsVo = new PrpLCoinsVo();
			coinsVo.setPolicyNo(obj[0]!=null ? obj[0].toString():null);
			coinsVo.setCoinsType(obj[1]!=null ? obj[1].toString():null);
			coinsVo.setChiefFlag(obj[2]!=null ? obj[2].toString():null);
			coinsVo.setCoinsCode(obj[3]!=null ? obj[3].toString():null);
			coinsVo.setCoinsName(obj[4]!=null ? obj[4].toString():null);
			coinsVo.setCoinsRate(obj[5]!=null ? new BigDecimal(obj[5].toString()):null);
			coinsVo.setCurrency(obj[6]!=null ? obj[6].toString():null);
			String PROPORTIONFLAG = obj[7]!=null ? obj[7].toString():null;
			if(PROPORTIONFLAG != null && PROPORTIONFLAG.length()==2){
				coinsVo.setCalculateType(PROPORTIONFLAG.substring(0,1));
			}
			PrpLCoinsVoList.add(coinsVo);
		}
    	
    	return PrpLCoinsVoList;
    }
    
    @Override
    public List<PrpLCoinsVo> findPrpLCoinsByCompensateNo(String compensateNo){
    	List<PrpLCoinsVo> PrpLCoinsVoList = new ArrayList<PrpLCoinsVo>();
    	QueryRule queryRule = QueryRule.getInstance();
    	queryRule.addEqual("compensateNo", compensateNo);
    	
    	List<PrpLCoins> prpLCoinsList = databaseDao.findAll(PrpLCoins.class, queryRule);
    	
    	if(prpLCoinsList != null && prpLCoinsList.size()>0){
    		PrpLCoinsVoList = Beans.copyDepth().from(prpLCoinsList).toList(PrpLCoinsVo.class);
    	}
    	
    	return PrpLCoinsVoList;
    }
    
    public void savePrpLCoins(List<PrpLCoinsVo> prpLCoinsVoList){
    	compensateService.savePrpLCoins(prpLCoinsVoList);
    }
	
	@Override
	public List<PrpLCompensateVo> findCompensateVoList(String claimNo,String riskCode,String compensateType) {
		List<PrpLCompensateVo> list = new ArrayList<PrpLCompensateVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("claimNo",claimNo);
		queryRule.addEqual("compensateType","N");
		queryRule.addIn("underwriteFlag",CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE,CodeConstants.UnderWriteFlag.AUTO_UNDERWRITE);
		List<PrpLCompensate> compePo = databaseDao.findAll(PrpLCompensate.class,queryRule);
		List<PrpLCompensateVo> compeVos = null;
		if(compePo!=null&&compePo.size()>0){
			compeVos = new ArrayList<PrpLCompensateVo>();
			for(PrpLCompensate prpLCompensate:compePo){
				PrpLCompensateVo prpLCompensateVo = new PrpLCompensateVo();
				Beans.copy().from(prpLCompensate).to(prpLCompensateVo);
				String compensateNo = prpLCompensateVo.getCompensateNo();
				List<PrpLLossItemVo> prpLLossItemVos = compensateService.findPrpLLossByCompNo(compensateNo);
				prpLCompensateVo.setPrpLLossItems(prpLLossItemVos);
				List<PrpLLossPropVo> prpLLossProps = compensateService.findLossPropByCompNo(compensateNo);
				prpLCompensateVo.setPrpLLossProps(prpLLossProps);
				List<PrpLLossPersonVo> prpLLossPersonVos = compensateService.findLossPersonByCompNo(compensateNo);
				prpLCompensateVo.setPrpLLossPersons(prpLLossPersonVos);
				compeVos.add(prpLCompensateVo);
			}
		}

		if(compeVos != null && !compeVos.isEmpty()){
			for(PrpLCompensateVo t : compeVos){
				if(StringUtils.isNotBlank(riskCode) && riskCode.equals(t.getRiskCode()) ){
					list.add(t);
				}
			}
		}
		return list;
	}


	@Override
	public List<PrpLCompensateVo> findPrplCompensateByRegistNoAndPolicyNo(String registNo, String policyNo) {
		QueryRule qurey=QueryRule.getInstance();
		qurey.addEqual("registNo",registNo);
		qurey.addEqual("policyNo",policyNo);
		qurey.addEqual("compensateType","N");
		List<PrpLCompensate> compos=databaseDao.findAll(PrpLCompensate.class,qurey);
		List<PrpLCompensateVo> comVos=new ArrayList<PrpLCompensateVo>();
		if(compos!=null && compos.size()>0){
			comVos=Beans.copyDepth().from(compos).toList(PrpLCompensateVo.class);
		}
		return comVos;
	}

	@Override
	public List<PrpLCompensateVo> queryValidCompensate(String registNo,
			String type) {
		return compensateService.findValidCompensate(registNo,type,null);
	}

	@Override
	public boolean existPayCustom(String accountNo, String compensateNo,Long payeeId,String payReason) {
		Map<String,SysCodeDictVo> dictTransMap = codeTranService.findCodeDictTransMap("ChargeCode","");
		//?????????????????????????????????????????????????????????????????????
		for(SysCodeDictVo sysCodeDictVo : dictTransMap.values()){
			String reason = sysCodeDictVo.getProperty2();
			if(reason.equals(payReason)){
				return false;
			}
		}
		List<Long> ids = null;
		if(compensateNo.startsWith("Y")){//??????
			List<PrpLPrePayVo> prpLPrePayVos = queryPrePay(compensateNo); 
			if(prpLPrePayVos != null && prpLPrePayVos.size()>0){
				ids = new ArrayList<Long>();
				for(PrpLPrePayVo prpLPrePay: prpLPrePayVos){
					if(prpLPrePay.getPayeeId() != null && !payeeId.equals(prpLPrePay.getPayeeId()) &&"P".equals(prpLPrePay.getFeeType())){//???????????????????????????
						ids.add(prpLPrePay.getPayeeId());
					}
				}
			}
		} else if(compensateNo.startsWith("D")){//??????
			PrpLPadPayMainVo prpLPadPayMainVo = padPayService.findPadPayMainByCompNo(compensateNo);
			if(prpLPadPayMainVo != null){
				List<PrpLPadPayPersonVo> prpLPadPayPersons = prpLPadPayMainVo.getPrpLPadPayPersons();
				if(prpLPadPayPersons != null && prpLPadPayPersons.size()>0){
					ids = new ArrayList<Long>();
					for(PrpLPadPayPersonVo prpLPadPayPerson: prpLPadPayPersons){
						if(prpLPadPayPerson.getPayeeId() != null && !payeeId.equals(prpLPadPayPerson.getPayeeId())){//???????????????????????????
							ids.add(prpLPadPayPerson.getPayeeId());
						}
					}
				}
			}
		} else if(compensateNo.startsWith("7")){//??????
			QueryRule qurey = QueryRule.getInstance();
			qurey.addEqual("prpLCompensate.compensateNo",compensateNo);
			List<PrpLPayment> payments = databaseDao.findAll(PrpLPayment.class,qurey);  
			if(payments != null && payments.size()>0){
				ids = new ArrayList<Long>();
				for(PrpLPayment prplPayment: payments){
					if(prplPayment.getPayeeId() != null && !payeeId.equals(prplPayment.getPayeeId())){//???????????????????????????
						if(PayReason.COMPENSATE_PAY_Res.equals(payReason) && 
								(PayFlagType.COMPENSATE_PAY.equals(prplPayment.getPayFlag()))){//?????????????????????
							ids.add(prplPayment.getPayeeId());
						} else if(PayReason.CLEAR_PAY_Res.equals(payReason) && PayFlagType.CLEAR_PAY.equals(prplPayment.getPayFlag())){//?????????????????????
							ids.add(prplPayment.getPayeeId());
						} else if(PayReason.INSTEAD_PAY_Res.equals(payReason) && PayFlagType.INSTEAD_PAY.equals(prplPayment.getPayFlag())){//?????????????????????
							ids.add(prplPayment.getPayeeId());
						}
					}
				}
			}
		}
		if(ids != null && ids.size()>0){
			QueryRule idsQuery = QueryRule.getInstance();
			idsQuery.addIn("id", ids);
			List<PrpLPayCustom> payCustoms = databaseDao.findAll(PrpLPayCustom.class,idsQuery);  
			if(payCustoms!= null && payCustoms.size()>0){
				for(PrpLPayCustom payCustom:payCustoms){
					if(StringUtils.isNotBlank(payCustom.getAccountNo()) 
							&& payCustom.getAccountNo().equals(accountNo)){
						return true;
					}
				}
			}	
		}
		return false;
	}
	/**
	 * ???????????????????????? ?????????????????????????????????????????????????????????????????????????????????????????????= ??????+??????+??????????????????????????????
	 */
    public String validateAmoutIsSame(PrpLCompensateVo compVo){
    	if(!compVo.getComCode().startsWith(CodeConstants.PolicyNoFromt_SH)){//??????????????????????????????
    		if(compVo.getRiskCode().startsWith(PolicyType.POLICY_DZA)){//??????????????????????????????
    			List<PrpLCompensateVo> compensateVos = this.findCompensateVoList(compVo.getClaimNo(),compVo.getRiskCode(),compVo.getRiskCode());
				BigDecimal sumAmt = BigDecimal.ZERO;
				BigDecimal lossTotalAmt = BigDecimal.ZERO;
				if(!compensateVos.isEmpty() && compensateVos.size()>0){
					for(PrpLCompensateVo compensateVo : compensateVos){
						sumAmt = sumAmt.add(DataUtils.NullToZero(compensateVo.getSumAmt()));
						List<PrpLLossItemVo> lossItemVoList = compensateVo.getPrpLLossItems();
						if(lossItemVoList != null && !lossItemVoList.isEmpty()){
							lossItemVoList.removeAll(Collections.singleton(null));
							for(PrpLLossItemVo itemVo : lossItemVoList){// ???
								lossTotalAmt = lossTotalAmt.add(DataUtils.NullToZero(itemVo.getSumRealPay()));
							}
						}
						List<PrpLLossPropVo> lossPropVoList = compensateVo.getPrpLLossProps();
						if(lossPropVoList != null && !lossPropVoList.isEmpty()){
							lossPropVoList.removeAll(Collections.singleton(null));
							for(PrpLLossPropVo propVo : lossPropVoList){// ????????????
								lossTotalAmt = lossTotalAmt.add(DataUtils.NullToZero(propVo.getSumRealPay()));
							}
						}
						//???
						List<PrpLLossPersonVo> lossPersoVoList = compensateVo.getPrpLLossPersons();
						if(lossPersoVoList != null && !lossPersoVoList.isEmpty()){
							lossPersoVoList.removeAll(Collections.singleton(null));
							for(PrpLLossPersonVo personVo : lossPersoVoList){// ???
								for(PrpLLossPersonFeeVo feeVo:personVo.getPrpLLossPersonFees()){
									lossTotalAmt = lossTotalAmt.add(DataUtils.NullToZero(feeVo.getFeeRealPay()));
								}
							}
						}
					}
				}
				if(sumAmt.compareTo(lossTotalAmt) != 0){
					return "????????????????????????????????????????????????????????????";
				}
    		}
    	}
    	return null;
    }
    
    @Override
    public Map<String,String> getCompUnderWriteFlagByRegNo(String registNo){
    	return compensateService.getCompUnderWriteFlagByRegNo(registNo);
    }


	/* 
	 * @see ins.sino.claimcar.claim.service.CompensateTaskService#validAccountIsSame(ins.sino.claimcar.claim.vo.PrpLCompensateVo)
	 * @param compeVo
	 * @return
	 */
	@Override
	public String validAccountIsSame(PrpLCompensateVo compeVo,SysUserVo userVo) throws Exception {
		QueryRule qurey = QueryRule.getInstance();
		qurey.addEqual("prpLCompensate.compensateNo",compeVo.getCompensateNo());

		List<Long> ids = new ArrayList<Long>();
		List<PrpLPayment> payments = databaseDao.findAll(PrpLPayment.class,qurey);  
		List<PrpLCharge> prpLCharges = databaseDao.findAll(PrpLCharge.class, qurey);
		if(payments != null && payments.size()>0){
			for(PrpLPayment prplPayment: payments){
				ids.add(prplPayment.getPayeeId());
			}
		}
		if(prpLCharges != null && prpLCharges.size()>0){
			for(PrpLCharge prpLCharge: prpLCharges){
				ids.add(prpLCharge.getPayeeId());
			}
		}
		if(ids != null && ids.size()>0){
			QueryRule idsQuery = QueryRule.getInstance();
			idsQuery.addIn("id", ids);
			List<PrpLPayCustom> payCustoms = databaseDao.findAll(PrpLPayCustom.class,idsQuery);  
			if(payCustoms!= null && payCustoms.size()>0){
				for(PrpLPayCustom payCustom:payCustoms){
					if(StringUtils.isNotBlank(payCustom.getAccountNo())){
						String oldName = payCustomService.findByPayeeNameAndAccountNo(payCustom.getPayeeName(),payCustom.getAccountNo(),userVo);
						if(StringUtils.isNotBlank(oldName)){
							return "{"+ payCustom.getPayeeName() + "}-{" + payCustom.getAccountNo() 
									+ "} ??????????????????????????????????????????" + oldName + "??????????????????????????????????????????????????????";
						}
					}
				}
			}	
		}
		return null;
	}

	


	/* 
	 * @see ins.sino.claimcar.claim.service.CompensateTaskService#getPrePayWfHis(java.lang.String)
	 * @param prePayNo
	 * @return
	 */
	@Override
	public List<Map<String,Object>> getPrePayWfHis(String prePayNo) throws Exception {
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" with t1 as( "
				+ " select pay.chargecode,pay.compensateno,pay.payamt, PAYEENAME || '-' ||(substr(ACCOUNTNO,length(ACCOUNTNO)-4,4)) "
				+ " ,compensate.createuser,compensate.createtime,pay.FEETYPE"
				+ "   from claimuser.prplcompensateext ext,claimuser.PrpLPrePay pay,prplcompensate compensate"
				+ "  where ext.oppocompensateno = ? ");
		sqlUtil.addParamValue(prePayNo);
		sqlUtil.append("  and  pay.compensateno = ext.compensateno and compensate.compensateno = ext.compensateno"
				+ " and compensate.underwriteflag='1'), " //???????????????
				+ "t2 as ("
				+ " select p.underwriteuser,p.underwritedate from claimuser.prplcompensate p where compensateno= ? "
				+ " ) select * from t1,t2 ");
		sqlUtil.addParamValue(prePayNo);
		String sql = sqlUtil.getSql();
		logger.info("????????????????????????sql=" + sql);
		Object[] values = sqlUtil.getParamValues();
		List<Object[]> result = baseDaoService.findListBySql(sql,values);
		List<Map<String, Object>> mapList = null;
		if(result != null && result.size() > 0){
			Map<String,Object>  map = null;
			mapList = new ArrayList<Map<String,Object>>();
			for(int i = 0 ;i < result.size(); i++){
				map = new HashMap<String,Object>();
				Object[] obj = (Object[])result.get(i);
				map.put("chargeCode",(String)obj[0]);
				map.put("compensateNo",(String)obj[1]);
				map.put("payamt",(String.valueOf(obj[2])));
				map.put("payeeInfo",(String) obj[3]);
				map.put("createUser",(String) obj[4]);
				map.put("createDate",(Date) obj[5]);
				map.put("feeType",(String) obj[6]);
				map.put("underwriteUser",(String) obj[7]);
				map.put("underwriteDate",(Date) obj[8]);
				mapList.add(map);
			}
		}
		return mapList;
	}

	/* 
	 * @see ins.sino.claimcar.claim.service.CompensateTaskService#getPrePayWriteVo(java.util.List)
	 * @param PrePayVos
	 * @return
	 */
	@Override
	public List<PrpLPrePayVo> getPrePayWriteVo(String prePayNo,String feeType) throws Exception {
		List<PrpLPrePayVo> prePayVos = getPrePayVo(prePayNo,feeType);
		if(prePayVos != null && prePayVos.size() >0){
			PrpLPrePayVo prePayVo = prePayVos.get(0);
			SqlJoinUtils sqlUtil=new SqlJoinUtils();
			sqlUtil.append(" select  AccountNo,ChargeCode,PayeeName ,PayAmt from prplprepay prepay where compensateno in ( "
					+ " select compensate.compensateno from prplcompensate compensate "
					+ "  inner join claimuser.prplcompensateext ext"
					+ "   on  ext.compensateno = compensate.compensateno "
					+ "  where ext.oppocompensateno= ? and compensate.underwriteflag != ?  and compensate.underwriteflag != ? ) and  prepay.FEETYPE =?");
			sqlUtil.addParamValue(prePayVo.getCompensateNo());
			sqlUtil.addParamValue(UnderWriteFlag.CANCEL_UNDERWRITE);
			sqlUtil.addParamValue(UnderWriteFlag.CANCELFLAG);
			sqlUtil.addParamValue(prePayVo.getFeeType());
			String sql = sqlUtil.getSql();
			logger.info("sql=" + sql);
			Object[] values = sqlUtil.getParamValues();
			List<Object[]> prepPayOpps = baseDaoService.findListBySql(sql,values);
			if(prepPayOpps!= null && prepPayOpps.size() >0){
				for(int i = 0;i<prepPayOpps.size(); i++){
					for(int j = 0;j<prePayVos.size();j++){
						if(prePayVos.get(j).getAccountNo().equals(prepPayOpps.get(i)[0])
								&& prepPayOpps.get(i)[1].equals(prePayVos.get(j).getChargeCode())
								&& prepPayOpps.get(i)[2].equals(prePayVos.get(j).getPayeeName())){
							prePayVos.get(j).setPayAmt(prePayVos.get(j).getPayAmt().add((BigDecimal)prepPayOpps.get(i)[3]));
						}
					}
				}
				
			}
		}
	
		return prePayVos;
	}

	/* 
	 * @see ins.sino.claimcar.claim.service.CompensateTaskService#findOppCompensates(java.lang.String, char)
	 * @param compensateNo
	 * @param c
	 * @return
	 */
	@Override
	public List<PrpLCompensateVo> findOppCompensates(String compensateNo,String compensateType) {
		return compensateService.findByOpp(compensateNo,compensateType);
	}
	/* 
	 * @see ins.sino.claimcar.claim.service.CompensateTaskService#getBackPrePayWriteVo(java.lang.String, java.lang.String)
	 * @param prePayNo
	 * @param compensateType
	 * @return
	 */
	@Override
	public List<PrpLPrePayVo> getRemainPrePayWriteVo(String prePayNo,String compensateType) throws Exception {
		List<PrpLPrePayVo> prePayVos = getPrePayVo(prePayNo,compensateType);
		if(prePayVos != null && prePayVos.size() >0){
			PrpLPrePayVo prePayVo = prePayVos.get(0);
			SqlJoinUtils sqlUtil=new SqlJoinUtils();
			sqlUtil.append(" select  AccountNo,ChargeCode,PayeeName ,PayAmt from prplprepay prepay where compensateno in ( "
					+ " select compensate.compensateno from prplcompensate compensate "
					+ "  inner join claimuser.prplcompensateext ext"
					+ "   on  ext.compensateno = compensate.compensateno "
					+ "  where ext.oppocompensateno= ?  and compensate.underwriteflag != ? "
					+ "and compensate.underwriteflag != ?"
					+ ") and  prepay.FEETYPE =?"
					+ "and compensateno not in (select taskinkey from prplwftaskin taskin "
					+ " where taskin.taskinkey = prepay.compensateno and taskin.workstatus = ? and taskin.HANDLERSTATUS = ?)");
			sqlUtil.addParamValue(prePayVo.getCompensateNo());
			sqlUtil.addParamValue(UnderWriteFlag.CANCEL_UNDERWRITE);
			sqlUtil.addParamValue(UnderWriteFlag.CANCELFLAG);
			sqlUtil.addParamValue(prePayVo.getFeeType());
			sqlUtil.addParamValue(WorkStatus.BYBACK);
			sqlUtil.addParamValue(HandlerStatus.INIT);
			String sql = sqlUtil.getSql();
			logger.info("sql=" + sql);
			Object[] values = sqlUtil.getParamValues();
			List<Object[]> prepPayOpps = baseDaoService.findListBySql(sql,values);
			if(prepPayOpps!= null && prepPayOpps.size() >0){
				for(int i = 0;i<prepPayOpps.size(); i++){
					for(int j = 0;j<prePayVos.size();j++){
						if(prePayVos.get(j).getAccountNo().equals(prepPayOpps.get(i)[0])
								&& prepPayOpps.get(i)[1].equals(prePayVos.get(j).getChargeCode())
								&& prepPayOpps.get(i)[2].equals(prePayVos.get(j).getPayeeName())){
							prePayVos.get(j).setPayAmt(prePayVos.get(j).getPayAmt().add((BigDecimal)prepPayOpps.get(i)[3]));
						}
					}
				}
				
			}
		}
	
		return prePayVos;
	}

	/**
	 * ??????????????????????????????????????????????????????
	 * @param registNo
	 * @param policyNo
	 * @param times
	 * @return
	 */
	@Override
	public List<PrpLCompensateVo> findPrplCompensateByRegistNoAndPolicyNo(String registNo, String policyNo,Integer times) {
		return compensateService.findPrplCompensateByRegistNoAndPolicyNo(registNo,policyNo,times);
	}
	@Override
	public List<PrpLCompensateVo> queryCompensateByPolicyNo(String policyNo) {
		return compensateService.findCompByPolicyNo(policyNo);
	}
	
	@Override
	public List<PrpLLossItemVo> findLossItemsByRegistNoAndpolicyNo(
			String registNo, String policyNo) {
		List<PrpLLossItemVo> lossItems = new ArrayList<PrpLLossItemVo>();			
		if (!StringUtils.isBlank(registNo)) {
			QueryRule qr = QueryRule.getInstance();
			qr.addEqual("registNo", registNo);
			qr.addEqual("policyNo", policyNo);
			List<PrpLLossItem> lossItemList = databaseDao.findAll(PrpLLossItem.class, qr);
			if (lossItemList!=null && lossItemList.size() > 0) {
				for (PrpLLossItem lossItem : lossItemList) {
					PrpLLossItemVo lossItemVo = new PrpLLossItemVo();
					Beans.copy().from(lossItem).to(lossItemVo);
					lossItems.add(lossItemVo);
				}
			}
		}

		return lossItems;
	}

	@Override
	public boolean findsingcompay(String compensateNo, Date datesign) {
		long sumTask=0L;
		boolean flag=false;
		if(StringUtils.isBlank(compensateNo) || datesign==null) {
			return false;
		}
		//????????????(???????????????)
		SqlJoinUtils sqlUtils1=new SqlJoinUtils();
		sqlUtils1.append("select count(*) from PrpLPayment a where 1=1 "
				+ " and not exists(select 1 from PrpLCompensate c where a.compensateNo=c.compensateNo and c.underwriteDate <= ? and a.regsumAmount is null and c.underwriteDate is not null ) "
				+ " and a.compensateNo = ? ");
				
		sqlUtils1.addParamValue(datesign);//????????????
		sqlUtils1.addParamValue(compensateNo);//????????????
		String hqlS=sqlUtils1.getSql();
		logger.info("????????????(???????????????)??????sql:"+hqlS);
	    Object[] paramS=sqlUtils1.getParamValues();
	    Long suma=baseDaoService.getCountBySql(hqlS, paramS);
	    //????????????
		SqlJoinUtils sqlUtils2=new SqlJoinUtils();
		sqlUtils2.append("select count(*) from PrpLCharge a where 1=1 "
				+ " and not exists(select 1 from PrpLCompensate c where a.compensateNo=c.compensateNo and c.underwriteDate <= ? and a.regsumAmount is null and c.underwriteDate is not null ) "
				+ " and a.compensateNo = ? ");
		sqlUtils2.addParamValue(datesign);//????????????
		sqlUtils2.addParamValue(compensateNo);//????????????
		String hqlf=sqlUtils2.getSql();
		logger.info("??????????????????sql:"+hqlf);
	    Object[] paramf=sqlUtils2.getParamValues();
	    Long sumf=baseDaoService.getCountBySql(hqlf, paramf);
	    //??????
		SqlJoinUtils sqlUtils3=new SqlJoinUtils();
		sqlUtils3.append("select count(*) from PrpLPrePay a where 1=1 "
				+ " and not exists(select 1 from PrpLCompensate c where a.compensateNo=c.compensateNo and c.underwriteDate <= ? and a.regsumAmount is null and c.underwriteDate is not null ) "
				+ " and a.compensateNo = ? ");
		sqlUtils3.addParamValue(datesign);//????????????
		sqlUtils3.addParamValue(compensateNo);//????????????
		String hqly=sqlUtils3.getSql();
		logger.info("????????????sql:"+hqly);
	    Object[] paramy=sqlUtils3.getParamValues();
	    Long sumy=baseDaoService.getCountBySql(hqly, paramy);
	  
	    if(suma!=null){
	    	sumTask=sumTask+suma.intValue();
	    }
	    if(sumf!=null){
	    	sumTask=sumTask+sumf.intValue();
	    }
	    if(sumy != null){
	    	sumTask=sumTask+sumy.intValue(); 
	    }
	    if(sumTask>0){
	    	flag=true;
	    }
		return flag;
	}

	@Override
	public List<PrpLCompensateVo> findPrplCompensateByRegistNoAndClaimNo(
			String registNo, String claimNo) {
		return compensateService.findPrplCompensateByRegistNoAndClaimNo(registNo,claimNo);
	}
}
