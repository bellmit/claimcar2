package ins.sino.claimcar.claim.services;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.lang.Datas;
import ins.framework.service.CodeTranService;
import ins.framework.utils.Beans;
import ins.framework.web.AjaxResult;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.service.facade.BillNoService;
import ins.platform.utils.DataUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.CommonConst;
import ins.sino.claimcar.CodeConstants.CompensateFlag;
import ins.sino.claimcar.CodeConstants.KINDCODE;
import ins.sino.claimcar.CodeConstants.UnderWriteFlag;
import ins.sino.claimcar.CodeConstants.WRITEOFFFLAG;
import ins.sino.claimcar.certify.service.CertifyPubService;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.calculator.CalculatorCI;
import ins.sino.claimcar.claim.calculator.kindCalculator.CalculatorFactory;
import ins.sino.claimcar.claim.calculator.kindCalculator.KindCalculator;
import ins.sino.claimcar.claim.calculator.kindCalculator.KindCalculatorBz;
import ins.sino.claimcar.claim.calculator.kindCalculator.MoneyFormator;
import ins.sino.claimcar.claim.po.ClaimCalLog;
import ins.sino.claimcar.claim.po.PrpLCharge;
import ins.sino.claimcar.claim.po.PrpLClaimSummary;
import ins.sino.claimcar.claim.po.PrpLCoins;
import ins.sino.claimcar.claim.po.PrpLCompensate;
import ins.sino.claimcar.claim.po.PrpLCompensateDef;
import ins.sino.claimcar.claim.po.PrpLCompensateExt;
import ins.sino.claimcar.claim.po.PrpLEndor;
import ins.sino.claimcar.claim.po.PrpLKindAmtSummary;
import ins.sino.claimcar.claim.po.PrpLLossItem;
import ins.sino.claimcar.claim.po.PrpLLossItem_QUERY;
import ins.sino.claimcar.claim.po.PrpLLossPerson;
import ins.sino.claimcar.claim.po.PrpLLossPersonFee;
import ins.sino.claimcar.claim.po.PrpLLossPersonFeeDetail;
import ins.sino.claimcar.claim.po.PrpLLossProp;
import ins.sino.claimcar.claim.po.PrpLPayHis;
import ins.sino.claimcar.claim.po.PrpLPayment;
import ins.sino.claimcar.claim.po.PrpLPrePay;
import ins.sino.claimcar.claim.service.CalculatorService;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.ConfigService;
import ins.sino.claimcar.claim.vo.CompensateExp;
import ins.sino.claimcar.claim.vo.CompensateKindPay;
import ins.sino.claimcar.claim.vo.CompensateListVo;
import ins.sino.claimcar.claim.vo.CompensateVo;
import ins.sino.claimcar.claim.vo.DefLossInfoOfA;
import ins.sino.claimcar.claim.vo.MItemKindVo;
import ins.sino.claimcar.claim.vo.PrpDDeprecateRateVo;
import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLClaimKindVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCoinsVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateDefVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateExtVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLEndorVo;
import ins.sino.claimcar.claim.vo.PrpLKindAmtSummaryVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonFeeDetailVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonFeeVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.claim.vo.ThirdPartyCompInfo;
import ins.sino.claimcar.claim.vo.ThirdPartyDepreRate;
import ins.sino.claimcar.claim.vo.ThirdPartyInfo;
import ins.sino.claimcar.claim.vo.ThirdPartyLossInfo;
import ins.sino.claimcar.claim.vo.ThirdPartyRecoveryInfo;
import ins.sino.claimcar.commom.vo.Refs;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMaterialVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarRepairVo;
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
import ins.sino.claimcar.other.vo.SendMsgParamVo;
import ins.sino.claimcar.padpay.po.PrpLPadPayMain;
import ins.sino.claimcar.padpay.po.PrpLPadPayPerson;
import ins.sino.claimcar.pay.service.PadPayService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.regist.vo.PrpLcCarDeviceVo;
import ins.sino.claimcar.regist.vo.PrplOldClaimRiskInfoVo;
import ins.sino.claimcar.reopencase.po.PrpLReCase;
import ins.sino.claimcar.rule.service.ClaimRuleApiService;
import ins.sino.claimcar.rule.vo.VerifyClaimRuleVo;
import ins.sino.claimcar.subrogation.service.SubrogationService;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;
import ins.sino.claimcar.verifyclaim.po.PrpLuwNotion;
import ins.sino.claimcar.verifyclaim.po.PrpLuwNotionMain;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author lanlei
 */
@Service("compensateService")
public class CompensateService {

	private static final Logger logger = LoggerFactory.getLogger(CompensateService.class);
	@Autowired
	private DatabaseDao databaseDao;
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
	private PropTaskService propTaskService;
	@Autowired
	private PersTraceDubboService persTraceDubboService;
	@Autowired
	private ManagerService managerService;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	private CertifyPubService certifyPubService;
	@Autowired
	private WfTaskHandleService taskHandleService;
	@Autowired
	private LossCarService lossCarService;
	@Autowired
	private SubrogationService subrogationService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	private CalculatorService calculatorService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	private ConfigService configService;
	@Autowired
	ClaimRuleApiService claimRuleApiService;
	@Autowired
	private RegistService registService;
	@Autowired
	private CompensateTaskService compensateTaskService;
	@Autowired
	private BaseDaoService baseDaoService;
	@Autowired
	PrpLCMainService prpLCMainService;

	/** 诉讼案件 */
	public static final String LAWFLAG_YES = "1";
	/** 非诉讼案件 */
	public static final String LAWFLAG_NO = "0";
	/** 交强险种前缀 */
	public static final String PREFIX_CI = "11";
	/** 商业险种前缀 */
	public static final String PREFIX_BI = "12";

	/**
	 * 根据报案号查询计算书信息 正常计算书
	 * @param registNo
	 * @return
	 */
	public List<PrpLCompensateVo> findCompByRegistNo(String number) {
		List<PrpLCompensateVo> compList = new ArrayList<PrpLCompensateVo>();
		if( !StringUtils.isBlank(number)){
			QueryRule qr = QueryRule.getInstance();
			qr.addEqual("registNo",number);
			qr.addEqual("compensateType","N");// CompensateType为N表示正常理算书
			List<PrpLCompensate> compPoList = databaseDao.findAll(PrpLCompensate.class,qr);
			// PrpLCompensateVo comVo = new PrpLCompensateVo();
			// for (PrpLCompensate comPo : compPoList) {
			// Beans.copy().from(comPo).to(comVo);
			// compList.add(comVo);
			// }

			compList = Beans.copyDepth().from(compPoList).toList(PrpLCompensateVo.class);
		}

		return compList;
	}

	public List<PrpLCompensateVo> findCompensate(String registNo,String type) {
		List<PrpLCompensateVo> compeVos = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("compensateType",type);
		// queryRule.addEqual("underwriteFlag","1");
		List<PrpLCompensate> compePo = databaseDao.findAll(PrpLCompensate.class,queryRule);
		if(compePo!=null&&compePo.size()>0){
			compeVos = Beans.copyDepth().from(compePo).toList(PrpLCompensateVo.class);
		}
		return compeVos;
	}

	public List<PrpLPrePayVo> queryPrePay(String compeNo) {
		List<PrpLPrePayVo> prePayVos = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("compensateNo",compeNo);
		List<PrpLPrePay> prePayPos = databaseDao.findAll(PrpLPrePay.class,queryRule);
		if(prePayPos!=null&&prePayPos.size()>0){
			prePayVos = new ArrayList<PrpLPrePayVo>();
			prePayVos = Beans.copyDepth().from(prePayPos).toList(PrpLPrePayVo.class);
		}
		return prePayVos;
	}
	
	
	/**
	 * 获取车损相关的预付信息
	 * @param registNo
	 * @param riskCode
	 * @param lossId
	 * @return
	 */
	public List<PrpLPrePayVo> queryCarLossPrePay(String registNo, String riskCode,String lossId) {
		List<PrpLPrePayVo> prePayVos = null;
		List<PrpLCompensateVo> compeVos = this.findUnderwriteCompensatevosByRegistNoAndRiskCode(registNo,
						riskCode);

		if (compeVos != null && !compeVos.isEmpty()) {

			List<String> compensateNoList = new ArrayList<String>();
			for (PrpLCompensateVo compensateVo : compeVos) {
				compensateNoList.add(compensateVo.getCompensateNo());
			}

			QueryRule queryRule = QueryRule.getInstance();
			queryRule.addIn("compensateNo", compensateNoList);
			queryRule.addLike("lossType", "car"+"%"+lossId);
			List<PrpLPrePay> prePayPos = databaseDao.findAll(PrpLPrePay.class,
					queryRule);
			if (prePayPos != null && prePayPos.size() > 0) {
				prePayVos = new ArrayList<PrpLPrePayVo>();
				prePayVos = Beans.copyDepth().from(prePayPos)
						.toList(PrpLPrePayVo.class);
			}
		}
		return prePayVos;
	}
	
	/**
	 * 获取预付信息
	 * @param registNo
	 * @param riskCode
	 * @return
	 */
	public List<PrpLPrePayVo> queryLossPrePay(String registNo, String riskCode) {
		List<PrpLPrePayVo> prePayVos = null;
		List<PrpLCompensateVo> compeVos = this.findUnderwriteCompensatevosByRegistNoAndRiskCode(registNo,
						riskCode);

		if (compeVos != null && !compeVos.isEmpty()) {

			List<String> compensateNoList = new ArrayList<String>();
			for (PrpLCompensateVo compensateVo : compeVos) {
				compensateNoList.add(compensateVo.getCompensateNo());
			}

			QueryRule queryRule = QueryRule.getInstance();
			queryRule.addIn("compensateNo", compensateNoList);
			List<PrpLPrePay> prePayPos = databaseDao.findAll(PrpLPrePay.class,
					queryRule);
			if (prePayPos != null && prePayPos.size() > 0) {
				prePayVos = new ArrayList<PrpLPrePayVo>();
				prePayVos = Beans.copyDepth().from(prePayPos)
						.toList(PrpLPrePayVo.class);
			}
		}
		return prePayVos;
	}

	/**
	 * 根据报案号和险种查询已审核通过的计算书
	 * @param registNo
	 * @param riskCode
	 * @return
	 */
	public List<PrpLCompensateVo> findUnderwriteCompensatevosByRegistNoAndRiskCode(String registNo,String riskCode) {
		List<PrpLCompensateVo> compeVos = new ArrayList<PrpLCompensateVo>();;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("riskCode", riskCode);
		queryRule.addEqual("underwriteFlag", "1");
		List<PrpLCompensate> compePo = databaseDao.findAll(PrpLCompensate.class,queryRule);
		if(compePo!=null&&compePo.size()>0){
			compeVos = Beans.copyDepth().from(compePo).toList(PrpLCompensateVo.class);
		}
		return compeVos;
	}

	/**
	 * 查询报案下所有的计算书 预付和赔案
	 * @param registNo
	 * @return
	 */
	public List<PrpLCompensateVo> findCompensatevosByRegistNo(String registNo) {
		List<PrpLCompensateVo> compeVos = new ArrayList<PrpLCompensateVo>();;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLCompensate> compePo = databaseDao.findAll(PrpLCompensate.class,queryRule);
		if(compePo!=null&&compePo.size()>0){
			compeVos = Beans.copyDepth().from(compePo).toList(PrpLCompensateVo.class);
		}
		return compeVos;
	}

	public List<PrpLCompensateVo> findCompensatevosByclaimNo(String claimNo) {
		List<PrpLCompensateVo> compeVos = new ArrayList<PrpLCompensateVo>();
		List<PrpLCompensateVo> compeVos1 = new ArrayList<PrpLCompensateVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("claimNo",claimNo);
		List<PrpLCompensate> compePo = databaseDao.findAll(PrpLCompensate.class,queryRule);
		if(compePo!=null&&compePo.size()>0){
			compeVos = Beans.copyDepth().from(compePo).toList(PrpLCompensateVo.class);
			if(compeVos!=null&&compeVos.size()>0){
				for(PrpLCompensateVo vo:compeVos){
					if( !vo.getCompensateNo().startsWith("Y")){
						compeVos1.add(vo);
					}
				}
			}
		}
		return compeVos1;
	}

	public List<PrpLCompensateVo> queryCompensateByRegistNo(String registNo) {

		// 一个立案号对应一个计算书号
		List<PrpLCompensateVo> comVo = new ArrayList<PrpLCompensateVo>();
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("registNo",registNo);
		// qr.addEqual("underwriteFlag",CodeConstants.UnderWriteFlag.CANCELFLAG);
		qr.addEqual("compensateType","N");
		List<PrpLCompensate> compPo = databaseDao.findAll(PrpLCompensate.class,qr);
		if(compPo!=null&&compPo.size()>0){
			comVo = Beans.copyDepth().from(compPo).toList(PrpLCompensateVo.class);
		}
		return comVo;
	}

	public List<PrpLPadPayMainVo> findPadPayMainByRegistNo(String registNo) {
		List<PrpLPadPayMainVo> padPayMainVo = new ArrayList<PrpLPadPayMainVo>();
		if(StringUtils.isNotBlank(registNo)){
			QueryRule pfqr = QueryRule.getInstance();
			pfqr.addEqual("registNo",registNo);
			List<PrpLPadPayMain> padPayMain = databaseDao.findAll(PrpLPadPayMain.class,pfqr);
			if(padPayMain!=null&&padPayMain.size()>0){
				padPayMainVo = Beans.copyDepth().from(padPayMain).toList(PrpLPadPayMainVo.class);
			}
		}
		return padPayMainVo;
	}

	public List<PrpLCompensateVo> findCompensateByClaimno(String claimNo,String compensateType,String compensateNo,Boolean containFlag) {
		QueryRule queryRule = QueryRule.getInstance();

		queryRule.addEqual("claimNo",claimNo);
		queryRule.addEqual("compensateType","N");
		queryRule.addIn("underwriteFlag",CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE,CodeConstants.UnderWriteFlag.AUTO_UNDERWRITE);
		// 不从数据库中统计本计算书，本计算书的数据统计当前数据。
		if( !containFlag){
			queryRule.addNotEqual("compensateNo",compensateNo);
		}

		List<PrpLCompensate> compensatList = databaseDao.findAll(PrpLCompensate.class,queryRule);
		List<PrpLCompensateVo> compensateVoList = Beans.copyDepth().from(compensatList).toList(PrpLCompensateVo.class);

		return compensateVoList;
	}

	public List<PrpLCompensateVo> findCompensateByClaimno(String claimNo,String compensateType) {
		QueryRule queryRule = QueryRule.getInstance();

		queryRule.addEqual("claimNo",claimNo);
		queryRule.addEqual("compensateType","N");
		queryRule.addIn("underwriteFlag",CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE,CodeConstants.UnderWriteFlag.AUTO_UNDERWRITE);

		List<PrpLCompensate> compensatList = databaseDao.findAll(PrpLCompensate.class,queryRule);
		List<PrpLCompensateVo> compensateVoList = Beans.copyDepth().from(compensatList).toList(PrpLCompensateVo.class);

		return compensateVoList;
	}

	/**
	 * 根据立案号查询计算书信息
	 * @param ClaimNo
	 * @param compensateType --N-正常理算,Y-预付
	 */
	public PrpLCompensateVo findCompByClaimNo(String ClaimNo,String compensateType) {// 一个立案号对应一个计算书号
		PrpLCompensateVo comVo = null;
		if( !StringUtils.isBlank(ClaimNo)){
			QueryRule qr = QueryRule.getInstance();
			qr.addEqual("claimNo",ClaimNo);
			qr.addEqual("compensateType",compensateType);// CompensateType为N表示正常理算书
			qr.addNotEqual("underwriteFlag",CodeConstants.UnderWriteFlag.CANCELFLAG);// 排除掉注销计算书
			List<PrpLCompensate> compPoList = databaseDao.findAll(PrpLCompensate.class,qr);
			if(compPoList.size()>0){
				comVo = Beans.copyDepth().from(compPoList.get(0)).to(PrpLCompensateVo.class);
			}
		}

		return comVo;
	}

	public PrpLCompensateVo searchCompByClaimNo(String ClaimNo,String compensateType) {
		PrpLCompensateVo comVo = null;
		if( !StringUtils.isBlank(ClaimNo)){
			QueryRule qr = QueryRule.getInstance();
			qr.addEqual("claimNo",ClaimNo);
			qr.addEqual("compensateType",compensateType);// CompensateType为N表示正常理算书
			qr.addDescOrder("createTime");
			List<PrpLCompensate> compPoList = databaseDao.findAll(PrpLCompensate.class,qr);
			if(compPoList.size()>0){
				comVo = Beans.copyDepth().from(compPoList.get(0)).to(PrpLCompensateVo.class);
			}
		}

		return comVo;
	}

	/**
	 * 查询历史预付信息
	 * 
	 * <pre></pre>
	 * @param ClaimNo
	 * @param compensateType
	 * @return
	 * @modified: ☆XMSH(2016年4月16日 下午5:12:12): <br>
	 */
	public List<PrpLCompensateVo> findCompListByClaimNo(String ClaimNo,String compensateType) {// 一个立案号对应一个计算书号
		List<PrpLCompensateVo> comListVo = new ArrayList<PrpLCompensateVo>();
		if( !StringUtils.isBlank(ClaimNo)){
			QueryRule qr = QueryRule.getInstance();
			qr.addEqual("claimNo",ClaimNo);
			qr.addEqual("underwriteFlag","1");// 核赔通过
			qr.addEqual("compensateType",compensateType);// CompensateType为N表示正常理算书
			List<PrpLCompensate> compPoList = databaseDao.findAll(PrpLCompensate.class,qr);
			if(compPoList.size()>0){
				for(PrpLCompensate compPo:compPoList){
					PrpLCompensateVo compVo = new PrpLCompensateVo();
					compVo = Beans.copyDepth().from(compPo).to(PrpLCompensateVo.class);
					comListVo.add(compVo);
				}
			}
		}
		return comListVo;
	}

	/**
	 * 根据charge来源的定损主表判断该费用是否已经理算扣减
	 * 
	 * <pre></pre>
	 * @param chargeVo
	 * @param bcFlag
	 * @return
	 * @modified: ☆WLL(2016年8月17日 下午7:46:48): <br>
	 */
	public boolean adjustIsCompDeductForCharge(PrpLDlossChargeVo chargeVo,String bcFlag) {
		String bussType = chargeVo.getBusinessType();
		if("DLProp".equals(bussType)){
			PrpLdlossPropMainVo propMainVo = propTaskService.findPropMainVoById(chargeVo.getBusinessId());
			if(PREFIX_CI.equals(bcFlag)){
				if(propMainVo.getLossState().startsWith("1")){
					return true;
				}
			}else{
				if(propMainVo.getLossState().endsWith("1")){
					return true;
				}
			}
		}
		if("DLCar".equals(bussType)){
			PrpLDlossCarMainVo carMainVo = lossCarService.findLossCarMainById(chargeVo.getBusinessId());
			if(PREFIX_CI.equals(bcFlag)){
				if(carMainVo.getLossState().startsWith("1")){
					return true;
				}
			}else{
				if(carMainVo.getLossState().endsWith("1")){
					return true;
				}
			}
		}
		if("PLoss".equals(bussType)){
			PrpLDlossPersTraceMainVo persMainVo = persTraceDubboService.findPersTraceMainByPk(chargeVo.getBusinessId());
			if(PREFIX_CI.equals(bcFlag)){
				if(persMainVo.getLossState().startsWith("1")){
					return true;
				}
			}else{
				if(persMainVo.getLossState().endsWith("1")){
					return true;
				}
			}
		}
		if ("Check".equals(bussType)) {
			QueryRule queryRule = QueryRule.getInstance();
			queryRule.addEqual("registNo", chargeVo.getRegistNo());
			//重开核赔通过
			queryRule.addEqual("checkStatus", "6");
			List<PrpLReCase> reCaseList = databaseDao.findAll(PrpLReCase.class, queryRule);
			if (null != reCaseList && !reCaseList.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param ClaimNo
	 * @param compensateType
	 * @return
	 */
	public List<PrpLCompensateVo> findNormalCompListByClaimNo(String ClaimNo,String compensateType,String isCompdeduct) {
		List<PrpLCompensateVo> comListVo = new ArrayList<PrpLCompensateVo>();
		if( !StringUtils.isBlank(ClaimNo)){
			SqlJoinUtils sqlUtil = new SqlJoinUtils();
			sqlUtil.append(" FROM PrpLCompensate comp ");
			// 查询核损完成，未理算的数据
			sqlUtil.append("Where comp.claimNo = ?");
			sqlUtil.append(" And comp.underwriteFlag = ? ");
			sqlUtil.append("And  comp.compensateType=? ");
			sqlUtil.append("And ( comp.prpLCompensateExt.oppoCompensateNo is null"
					+ " or "
					+ " exists (select 1 from PrpLCompensateExt t, PrpLCompensate b where t.oppoCompensateNo = comp.compensateNo"
					+ " and t.compensateNo = b.compensateNo and b.underwriteFlag= ? group by t.oppoCompensateNo having sum(abs(b.sumAmt)) < comp.sumAmt)) ");
			sqlUtil.append("And comp.prpLCompensateExt.isCompDeduct =? ");
			sqlUtil.addParamValue(ClaimNo);
			sqlUtil.addParamValue("1");
			sqlUtil.addParamValue(compensateType);
			sqlUtil.addParamValue(UnderWriteFlag.MANAL_UNDERWRITE);
			sqlUtil.addParamValue(isCompdeduct);

			String sql = sqlUtil.getSql();
			Object[] values = sqlUtil.getParamValues();
			logger.debug("taskQrySql="+sql);
			logger.debug("ParamValues="+ArrayUtils.toString(values));

			List<PrpLCompensate> compPoList = databaseDao.findAllByHql(PrpLCompensate.class,sql,values);

			if(compPoList.size()>0){
				for(PrpLCompensate compPo:compPoList){
					PrpLCompensateVo compVo = new PrpLCompensateVo();
					compVo = Beans.copyDepth().from(compPo).to(PrpLCompensateVo.class);
					comListVo.add(compVo);
				}
			}
		}
		return comListVo;
	}

	public List<PrpLCompensateVo> queryCompListByClaimNo(String ClaimNo,String compensateType) {// 一个立案号对应一个计算书号
		List<PrpLCompensateVo> comListVo = null;
		if( !StringUtils.isBlank(ClaimNo)){
			QueryRule qr = QueryRule.getInstance();
			qr.addEqual("claimNo",ClaimNo);
			if(StringUtils.isNotBlank(compensateType)){
				qr.addEqual("compensateType",compensateType);// CompensateType为N表示正常理算书
			}
			List<PrpLCompensate> compPoList = databaseDao.findAll(PrpLCompensate.class,qr);
			if(compPoList.size()>0){
				comListVo = Beans.copyDepth().from(compPoList).toList(PrpLCompensateVo.class);
			}
		}
		return comListVo;
	}

	/**
	 * 判断立案号下是否存在未注销的冲销计算书（包含未核赔计算书）
	 * 
	 * <pre></pre>
	 * @param claimNo 立案号
	 * @param compeType 计算书类型
	 * @return
	 * @modified: ☆WLL(2017年12月13日 上午11:14:40): <br>
	 */
	public boolean isExistWriteOff(String claimNo,String compeType) {
		List<PrpLCompensateVo> compVoList = this.queryCompListByClaimNo(claimNo,compeType);
		if(compVoList!=null&&compVoList.size()>0){
			for(PrpLCompensateVo compeVo:compVoList){
				// 计算书未被注销且是否冲销标志位为是
				if( !UnderWriteFlag.CANCELFLAG.equals(compeVo.getUnderwriteFlag())&&"1".equals(compeVo.getPrpLCompensateExt().getWriteOffFlag())){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 根据计算书号查询计算书VO
	 * @param compensateNo
	 * @return
	 */
	public PrpLCompensateVo findCompByPK(String compensateNo) {
		PrpLCompensateVo compVo = null;
		PrpLCompensate compPo = databaseDao.findByPK(PrpLCompensate.class,compensateNo);
		if(compPo!=null){
			compVo = Beans.copyDepth().from(compPo).to(PrpLCompensateVo.class);
		}

		return compVo;
	}
	/**
	 * 根据对冲计算书号更新原计算书
	 * @param compensateNo
	 * @return
	 */
	public void updateCompByOppCompensateVo(String compensateNo) {
		 
		PrpLCompensateExt compensateExt = databaseDao.findByPK(PrpLCompensateExt.class, compensateNo);
		if(compensateExt != null){
			compensateExt.setOppoCompensateNo("");
			databaseDao.update(PrpLCompensateExt.class, compensateExt);
		} 
	}
	public List<PrpLLossPropVo> findLossPropByCompNo(String compensateNo) {
		List<PrpLLossPropVo> lossPropList = null;
		QueryRule rule = QueryRule.getInstance();
		rule.addEqual("prpLCompensate.compensateNo",compensateNo);

		List<PrpLLossProp> lossPropPoList = databaseDao.findAll(PrpLLossProp.class,rule);
		if(lossPropPoList!=null){
			lossPropList = Beans.copyDepth().from(lossPropPoList).toList(PrpLLossPropVo.class);
		}
		return lossPropList;
	}
	public List<PrpLLossPersonVo> findLossPersonByCompNo(String compensateNo) {
		List<PrpLLossPersonVo> lossPersonList = null;
		QueryRule rule = QueryRule.getInstance();
		rule.addEqual("prpLCompensate.compensateNo",compensateNo);

		List<PrpLLossPerson> lossPersons = databaseDao.findAll(PrpLLossPerson.class,rule);
		if(lossPersons!=null){
			lossPersonList = Beans.copyDepth().from(lossPersons).toList(PrpLLossPersonVo.class);
		}
		return lossPersonList;
	}
	public PrpLCompensateExtVo findCompExtByCompNo(String compensateNo) {
		PrpLCompensateExtVo compExtVo = null;
		QueryRule rule = QueryRule.getInstance();
		rule.addEqual("compensateNo",compensateNo);

		PrpLCompensateExt compExtPo = databaseDao.findAll(PrpLCompensateExt.class,rule).get(0);
		if(compExtPo!=null){
			compExtVo = Beans.copyDepth().from(compExtPo).to(PrpLCompensateExtVo.class);
		}

		return compExtVo;
	}

	/**
	 * 根据报案号查询车辆损失信息
	 * @param registNo
	 * @return
	 */
	public List<PrpLLossItemVo> findLossItemsByRegistNo(String registNo) {
		List<PrpLLossItemVo> lossItems = new ArrayList<PrpLLossItemVo>();
		PrpLLossItemVo lossItemVo = new PrpLLossItemVo();
		if( !StringUtils.isBlank(registNo)){
			QueryRule qr = QueryRule.getInstance();
			qr.addEqual("registNo",registNo);
			List<PrpLLossItem> lossItemList = databaseDao.findAll(PrpLLossItem.class,qr);
			if(lossItemList.size()>0){
				for(PrpLLossItem lossItem:lossItemList){
					Beans.copy().from(lossItem).to(lossItemVo);
					lossItems.add(lossItemVo);
				}
			}
		}

		return lossItems;
	}

	/**
	 * 根据险别汇总金额
	 * @param prpLLossItemVoList
	 * @param prpLLossPropVoList
	 * @param prpLLossPersonVoList
	 * @param prpLChargeVoList
	 * @return
	 */
	public Map<String,BigDecimal> getCompKindAmtMap(List<PrpLLossItemVo> prpLLossItemVoList,List<PrpLLossPropVo> prpLLossPropVoList,
													List<PrpLLossPersonVo> prpLLossPersonVoList,List<PrpLChargeVo> prpLChargeVoList) {
		Map<String,BigDecimal> kindAmtMap = new HashMap<String,BigDecimal>();
		for(PrpLLossItemVo item:prpLLossItemVoList){
			if(item!=null){
				if(kindAmtMap==null||kindAmtMap.size()==0){
					kindAmtMap.put(item.getKindCode(),item.getSumRealPay());
				}else{
					if(kindAmtMap.containsKey(item.getKindCode())){
						BigDecimal temp = BigDecimal.ZERO;
						if(kindAmtMap.get(item.getKindCode())!=null){
							temp = kindAmtMap.get(item.getKindCode()).add(item.getSumRealPay());
						}else{
							temp = item.getSumRealPay();
						}
						kindAmtMap.put(item.getKindCode(),temp);
					}else{
						kindAmtMap.put(item.getKindCode(),item.getSumRealPay());
					}
				}
			}
		}
		for(PrpLLossPropVo prop:prpLLossPropVoList){
			if(prop!=null){
				if(kindAmtMap==null||kindAmtMap.size()==0){
					kindAmtMap.put(prop.getKindCode(),prop.getSumRealPay());
				}else{
					if(kindAmtMap.containsKey(prop.getKindCode())){
						BigDecimal temp = BigDecimal.ZERO;
						if(kindAmtMap.get(prop.getKindCode())!=null){
							temp = kindAmtMap.get(prop.getKindCode()).add(prop.getSumRealPay());
						}else{
							temp = prop.getSumRealPay();
						}
						kindAmtMap.put(prop.getKindCode(),temp);
					}else{
						kindAmtMap.put(prop.getKindCode(),prop.getSumRealPay());
					}
				}
			}
		}
		for(PrpLLossPersonVo personVo:prpLLossPersonVoList){
			if(kindAmtMap==null||kindAmtMap.size()==0){
				kindAmtMap.put(personVo.getKindCode(),personVo.getSumRealPay());
			}else{
				if(kindAmtMap.containsKey(personVo.getKindCode())){
					BigDecimal temp = BigDecimal.ZERO;
					if(kindAmtMap.get(personVo.getKindCode())!=null){
						temp = kindAmtMap.get(personVo.getKindCode()).add(personVo.getSumRealPay());
					}else{
						temp = personVo.getSumRealPay();
					}
					kindAmtMap.put(personVo.getKindCode(),temp);
				}else{
					kindAmtMap.put(personVo.getKindCode(),personVo.getSumRealPay());
				}
			}
		}
		for(PrpLChargeVo chargeVo:prpLChargeVoList){
			if(kindAmtMap==null||kindAmtMap.size()==0){
				kindAmtMap.put(chargeVo.getKindCode(),chargeVo.getFeeAmt());
			}else{
				if(kindAmtMap.containsKey(chargeVo.getKindCode())){
					BigDecimal temp = BigDecimal.ZERO;
					if(kindAmtMap.get(chargeVo.getKindCode())!=null){
						temp = kindAmtMap.get(chargeVo.getKindCode()).add(chargeVo.getFeeAmt());
					}else{
						temp = chargeVo.getFeeAmt();
					}
					kindAmtMap.put(chargeVo.getKindCode(),temp);
				}else{
					kindAmtMap.put(chargeVo.getKindCode(),chargeVo.getFeeAmt());
				}
			}
		}

		return kindAmtMap;
	}

	/**
	 * 理算保存
	 * @param compensateVo
	 * @param lossItemVos
	 * @param lossPropVos
	 * @param lossPersons
	 * @param lossPersonFees
	 * @param charges
	 */
	public String saveCompensates(PrpLCompensateVo compensateVo,List<PrpLLossItemVo> lossItemVos,List<PrpLLossPropVo> lossPropVos,
									List<PrpLLossPersonVo> lossPersons,List<PrpLChargeVo> charges,List<PrpLPaymentVo> payments,SysUserVo user) {

		String compensateNo = compensateVo.getCompensateNo();

		// 在新增前确认该立案号下只能同时存在一张未核赔通过的计算书，如果已存在，执行更新操作
		PrpLCompensate compensatePo = this.adjustCompUpdateOrSave(compensateVo.getClaimNo(),compensateVo.getCompensateNo());
		if(compensatePo!=null&& !compensatePo.getCompensateNo().equals(compensateVo.getCompensateNo())){
			throw new IllegalArgumentException("计算书保存失败！存在未核赔通过的冲销计算书，请先核赔通过！<br/>");
		}
		if(StringUtils.isBlank(compensateNo)&&compensatePo==null){
			// 生成计算书号
			compensateNo = billNoService.getCompensateNo(compensateVo.getComCode(),compensateVo.getRiskCode());
		}
		if(compensatePo==null){

			Date date = new Date();// 创建时间

			compensatePo = new PrpLCompensate();
			compensateVo.setCompensateNo(compensateNo);
			compensateVo.setCreateTime(date);
			compensateVo.setCreateUser(user.getUserCode());
			compensateVo.setUnderwriteFlag(CodeConstants.UnderWriteFlag.NORMAL);
			// 计算书类型字段赋值
			if(lossItemVos==null&&lossPersons==null&&lossPropVos==null&&charges!=null){
				compensateVo.setCompensateKind(CodeConstants.CompensateKind.BI_COMPENSATE_CHARGE);
				if(compensateVo.getRiskCode().equals("1101")){
					compensateVo.setCompensateKind(CodeConstants.CompensateKind.CI_COMPENSATE_CHARGE);
				}
			}else{
				compensateVo.setCompensateKind(CodeConstants.CompensateKind.BI_COMPENSATE);
				if(compensateVo.getRiskCode().equals("1101")){
					compensateVo.setCompensateKind(CodeConstants.CompensateKind.CI_COMPENSATE);
				}
			}
			// 次数字段赋值
			List<PrpLCompensateVo> underCompVoList = this.findCompensateByClaimno(compensateVo.getClaimNo(),"N");
			int times = underCompVoList.size();
			compensateVo.setTimes(times+1);

			Beans.copy().from(compensateVo).to(compensatePo);

			// 险别汇总金额表PrpLKindAmtSummary
			Map<String,PrpLKindAmtSummary> prpLKindAmtSummaryVoMap = new HashMap<String,PrpLKindAmtSummary>();

			// 给子表的compensateNo 报案号 立案号 保单号等字段赋值
			// 车辆损失
			List<PrpLLossItem> lossItemPoList = new ArrayList<PrpLLossItem>();
			if(lossItemVos!=null&& !lossItemVos.isEmpty()){
				for(PrpLLossItemVo lossItem:lossItemVos){
					PrpLLossItem lossItemPo = new PrpLLossItem();
					lossItem.setRiskCode(compensateVo.getRiskCode());
					lossItem.setPolicyNo(compensateVo.getPolicyNo());
					lossItem.setRegistNo(compensateVo.getRegistNo());
					lossItem.setCreateTime(date);
					Beans.copy().from(lossItem).to(lossItemPo);
					lossItemPo.setPrpLCompensate(compensatePo);
					lossItemPoList.add(lossItemPo);
					// databaseDao.save(PrpLLossItem.class, lossItemPo);
					if( !"B".equals(lossItem.getKindCode())){
						PrpLKindAmtSummary prpLKindAmtSummary = new PrpLKindAmtSummary();
						prpLKindAmtSummary.setPrpLCompensate(compensatePo);
						prpLKindAmtSummary.setKindCode(lossItem.getKindCode());
						BigDecimal kindAmtSummarySumLoss = new BigDecimal(0);
						BigDecimal kindAmtSummarySumRealPay = new BigDecimal(0);
						if(prpLKindAmtSummaryVoMap.containsKey(lossItem.getKindCode())){// 金额累加
							kindAmtSummarySumLoss = kindAmtSummarySumLoss.add(prpLKindAmtSummaryVoMap.get(lossItem.getKindCode()).getSumLoss());
							kindAmtSummarySumRealPay = kindAmtSummarySumRealPay.add(prpLKindAmtSummaryVoMap.get(lossItem.getKindCode())
									.getSumRealPay());
						}
						if(lossItem.getSumLoss()!=null){
							kindAmtSummarySumLoss = kindAmtSummarySumLoss.add(lossItem.getSumLoss());
							prpLKindAmtSummary.setSumLoss(kindAmtSummarySumLoss);
						}
						if(lossItem.getSumRealPay()!=null){
							kindAmtSummarySumRealPay = kindAmtSummarySumRealPay.add(lossItem.getSumRealPay());
							prpLKindAmtSummary.setSumRealPay(kindAmtSummarySumRealPay);
						}

						prpLKindAmtSummary.setCreateTime(new Date());
						prpLKindAmtSummary.setUpdateTime(new Date());
						prpLKindAmtSummaryVoMap.put(lossItem.getKindCode(),prpLKindAmtSummary);
					}else{// 区分车财人
						PrpLKindAmtSummary prpLKindAmtSummary = new PrpLKindAmtSummary();
						prpLKindAmtSummary.setPrpLCompensate(compensatePo);
						prpLKindAmtSummary.setKindCode(lossItem.getKindCode());
						prpLKindAmtSummary.setLossKind("1");
						BigDecimal kindAmtSummarySumLoss = new BigDecimal(0);
						BigDecimal kindAmtSummarySumRealPay = new BigDecimal(0);
						if(prpLKindAmtSummaryVoMap.containsKey(lossItem.getKindCode())){// 金额累加
							kindAmtSummarySumLoss = kindAmtSummarySumLoss.add(prpLKindAmtSummaryVoMap.get(lossItem.getKindCode()).getSumLoss());
							kindAmtSummarySumRealPay = kindAmtSummarySumRealPay.add(prpLKindAmtSummaryVoMap.get(lossItem.getKindCode())
									.getSumRealPay());
						}
						if(lossItem.getSumLoss()!=null){
							kindAmtSummarySumLoss = kindAmtSummarySumLoss.add(lossItem.getSumLoss());
							prpLKindAmtSummary.setSumLoss(kindAmtSummarySumLoss);
						}
						if(lossItem.getSumRealPay()!=null){
							kindAmtSummarySumRealPay = kindAmtSummarySumRealPay.add(lossItem.getSumRealPay());
							prpLKindAmtSummary.setSumRealPay(kindAmtSummarySumRealPay);
						}

						prpLKindAmtSummary.setCreateTime(new Date());
						prpLKindAmtSummary.setUpdateTime(new Date());
						prpLKindAmtSummaryVoMap.put(lossItem.getKindCode()+"lossItem",prpLKindAmtSummary);
					}
				}
			}
			// 财产损失
			List<PrpLLossProp> lossPropPoLsit = new ArrayList<PrpLLossProp>();
			if(lossPropVos!=null&& !lossPropVos.isEmpty()){
				for(PrpLLossPropVo lossProp:lossPropVos){
					PrpLLossProp lossPropPo = new PrpLLossProp();
					lossProp.setRiskCode(compensateVo.getRiskCode());
					lossProp.setPolicyNo(compensateVo.getPolicyNo());
					lossProp.setCreateTime(date);
					lossProp.setUpdateTime(date);
					Beans.copy().from(lossProp).to(lossPropPo);
					lossPropPo.setPrpLCompensate(compensatePo);
					lossPropPoLsit.add(lossPropPo);
					// databaseDao.save(PrpLLossProp.class, lossPropPo);
					if( !"B".equals(lossProp.getKindCode())){
						PrpLKindAmtSummary prpLKindAmtSummary = new PrpLKindAmtSummary();
						prpLKindAmtSummary.setPrpLCompensate(compensatePo);
						prpLKindAmtSummary.setKindCode(lossProp.getKindCode());
						BigDecimal kindAmtSummarySumLoss = new BigDecimal(0);
						BigDecimal kindAmtSummarySumRealPay = new BigDecimal(0);
						if(prpLKindAmtSummaryVoMap.containsKey(lossProp.getKindCode())){// 金额累加
							kindAmtSummarySumLoss = kindAmtSummarySumLoss.add(prpLKindAmtSummaryVoMap.get(lossProp.getKindCode()).getSumLoss());
							kindAmtSummarySumRealPay = kindAmtSummarySumRealPay.add(prpLKindAmtSummaryVoMap.get(lossProp.getKindCode())
									.getSumRealPay());
						}
						if(lossProp.getSumLoss()!=null){
							kindAmtSummarySumLoss = kindAmtSummarySumLoss.add(lossProp.getSumLoss());
							prpLKindAmtSummary.setSumLoss(kindAmtSummarySumLoss);
						}
						if(lossProp.getSumRealPay()!=null){
							kindAmtSummarySumRealPay = kindAmtSummarySumRealPay.add(lossProp.getSumRealPay());
							prpLKindAmtSummary.setSumRealPay(kindAmtSummarySumRealPay);
						}

						prpLKindAmtSummary.setCreateTime(new Date());
						prpLKindAmtSummary.setUpdateTime(new Date());
						prpLKindAmtSummaryVoMap.put(lossProp.getKindCode(),prpLKindAmtSummary);
					}else{// 区分车财人
						PrpLKindAmtSummary prpLKindAmtSummary = new PrpLKindAmtSummary();
						prpLKindAmtSummary.setPrpLCompensate(compensatePo);
						prpLKindAmtSummary.setKindCode(lossProp.getKindCode());
						prpLKindAmtSummary.setLossKind("2");
						BigDecimal kindAmtSummarySumLoss = new BigDecimal(0);
						BigDecimal kindAmtSummarySumRealPay = new BigDecimal(0);
						if(prpLKindAmtSummaryVoMap.containsKey(lossProp.getKindCode())){// 金额累加
							kindAmtSummarySumLoss = kindAmtSummarySumLoss.add(prpLKindAmtSummaryVoMap.get(lossProp.getKindCode()).getSumLoss());
							kindAmtSummarySumRealPay = kindAmtSummarySumRealPay.add(prpLKindAmtSummaryVoMap.get(lossProp.getKindCode())
									.getSumRealPay());
						}
						if(lossProp.getSumLoss()!=null){
							kindAmtSummarySumLoss = kindAmtSummarySumLoss.add(lossProp.getSumLoss());
							prpLKindAmtSummary.setSumLoss(kindAmtSummarySumLoss);
						}
						if(lossProp.getSumRealPay()!=null){
							kindAmtSummarySumRealPay = kindAmtSummarySumRealPay.add(lossProp.getSumRealPay());
							prpLKindAmtSummary.setSumRealPay(kindAmtSummarySumRealPay);
						}

						prpLKindAmtSummary.setCreateTime(new Date());
						prpLKindAmtSummary.setUpdateTime(new Date());
						prpLKindAmtSummaryVoMap.put(lossProp.getKindCode()+"Prop",prpLKindAmtSummary);
					}
				}
			}

			// 人员损失
			// 统计伤残亡人数
			BigDecimal hurtNum = BigDecimal.ZERO;
			BigDecimal deathNum = BigDecimal.ZERO;
			BigDecimal injureNum = BigDecimal.ZERO;
			List<PrpLLossPerson> persPoList = new ArrayList<PrpLLossPerson>();
			if(lossPersons!=null){
				for(PrpLLossPersonVo lossPerson:lossPersons){
					PrpLLossPerson lossPersonPo = new PrpLLossPerson();
					lossPerson.setPolicyNo(compensateVo.getPolicyNo());
					lossPerson.setRiskCode(compensateVo.getRiskCode());
					lossPerson.setRegistNo(compensateVo.getRegistNo());
					lossPerson.setCreateTime(date);
					lossPerson.setUpdateTime(date);
					if(lossPerson.equals("A")){
						hurtNum.add(BigDecimal.ONE);// 伤
					}else if(lossPerson.equals("B")){
						injureNum.add(BigDecimal.ONE);// 残
					}else if(lossPerson.equals("C")){
						deathNum.add(BigDecimal.ONE);// 亡
					}
					Beans.copy().from(lossPerson).to(lossPersonPo);
					// lossPersonPo.setPrpLLossPersonFees(persFeePoList);
					lossPersonPo.setPrpLCompensate(compensatePo);
					List<PrpLLossPersonFee> persFeePoList = new ArrayList<PrpLLossPersonFee>();
					for(PrpLLossPersonFeeVo lossPersonFee:lossPerson.getPrpLLossPersonFees()){
						PrpLLossPersonFee lossPersonFeePo = new PrpLLossPersonFee();
						lossPersonFee.setCompensateNo(compensateNo);
						lossPersonFee.setPolicyNo(compensateVo.getPolicyNo());
						// lossPersonFee.setPrpLLossPerson(lossPerson);
						Beans.copy().from(lossPersonFee).to(lossPersonFeePo);
						// PrpLLossPersonFeeDetail维护主子表关系
						List<PrpLLossPersonFeeDetail> feeDetailPoList = new ArrayList<PrpLLossPersonFeeDetail>();
						if(lossPersonFee.getPrpLLossPersonFeeDetails()!=null&&lossPersonFee.getPrpLLossPersonFeeDetails().size()>0){
							for(PrpLLossPersonFeeDetailVo feeDetailVo:lossPersonFee.getPrpLLossPersonFeeDetails()){
								PrpLLossPersonFeeDetail feeDetailPo = new PrpLLossPersonFeeDetail();
								Beans.copy().from(feeDetailVo).to(feeDetailPo);
								feeDetailPo.setPrpLLossPersonFee(lossPersonFeePo);
								feeDetailPoList.add(feeDetailPo);
							}
						}
						lossPersonFeePo.setPrpLLossPersonFeeDetails(feeDetailPoList);
						lossPersonFeePo.setPrpLLossPerson(lossPersonPo);
						persFeePoList.add(lossPersonFeePo);
						// databaseDao.save(PrpLLossPersonFee.class, lossPersonFeePo);

					}

					// Beans.copy().from(lossPerson).to(lossPersonPo);
					lossPersonPo.setPrpLLossPersonFees(persFeePoList);
					// lossPersonPo.setPrpLCompensate(compensatePo);
					persPoList.add(lossPersonPo);
					// databaseDao.save(PrpLLossPerson.class, lossPersonPo);
					if( !"B".equals(lossPerson.getKindCode())){
						PrpLKindAmtSummary prpLKindAmtSummary = new PrpLKindAmtSummary();
						prpLKindAmtSummary.setPrpLCompensate(compensatePo);
						prpLKindAmtSummary.setKindCode(lossPerson.getKindCode());
						BigDecimal kindAmtSummarySumLoss = new BigDecimal(0);
						BigDecimal kindAmtSummarySumRealPay = new BigDecimal(0);
						if(prpLKindAmtSummaryVoMap.containsKey(lossPerson.getKindCode())){// 金额累加
							kindAmtSummarySumLoss = kindAmtSummarySumLoss.add(prpLKindAmtSummaryVoMap.get(lossPerson.getKindCode()).getSumLoss());
							kindAmtSummarySumRealPay = kindAmtSummarySumRealPay.add(prpLKindAmtSummaryVoMap.get(lossPerson.getKindCode())
									.getSumRealPay());
						}
						if(lossPerson.getSumLoss()!=null){
							kindAmtSummarySumLoss = kindAmtSummarySumLoss.add(lossPerson.getSumLoss());
							prpLKindAmtSummary.setSumLoss(kindAmtSummarySumLoss);
						}
						if(lossPerson.getSumRealPay()!=null){
							kindAmtSummarySumRealPay = kindAmtSummarySumRealPay.add(lossPerson.getSumRealPay());
							prpLKindAmtSummary.setSumRealPay(kindAmtSummarySumRealPay);
						}

						prpLKindAmtSummary.setCreateTime(new Date());
						prpLKindAmtSummary.setUpdateTime(new Date());
						prpLKindAmtSummaryVoMap.put(lossPerson.getRiskCode(),prpLKindAmtSummary);
					}else{// 区分车财人
						PrpLKindAmtSummary prpLKindAmtSummary = new PrpLKindAmtSummary();
						prpLKindAmtSummary.setPrpLCompensate(compensatePo);
						prpLKindAmtSummary.setKindCode(lossPerson.getKindCode());
						prpLKindAmtSummary.setLossKind("3");
						BigDecimal kindAmtSummarySumLoss = new BigDecimal(0);
						BigDecimal kindAmtSummarySumRealPay = new BigDecimal(0);
						if(prpLKindAmtSummaryVoMap.containsKey(lossPerson.getKindCode())){// 金额累加
							kindAmtSummarySumLoss = kindAmtSummarySumLoss.add(prpLKindAmtSummaryVoMap.get(lossPerson.getKindCode()).getSumLoss());
							kindAmtSummarySumRealPay = kindAmtSummarySumRealPay.add(prpLKindAmtSummaryVoMap.get(lossPerson.getKindCode())
									.getSumRealPay());
						}
						if(lossPerson.getSumLoss()!=null){
							kindAmtSummarySumLoss = kindAmtSummarySumLoss.add(lossPerson.getSumLoss());
							prpLKindAmtSummary.setSumLoss(kindAmtSummarySumLoss);
						}
						if(lossPerson.getSumRealPay()!=null){
							kindAmtSummarySumRealPay = kindAmtSummarySumRealPay.add(lossPerson.getSumRealPay());
							prpLKindAmtSummary.setSumRealPay(kindAmtSummarySumRealPay);
						}

						prpLKindAmtSummary.setCreateTime(new Date());
						prpLKindAmtSummary.setUpdateTime(new Date());
						prpLKindAmtSummaryVoMap.put(lossPerson.getKindCode()+"P",prpLKindAmtSummary);
					}
				}
			}
			// 费用赔款
			List<PrpLCharge> chargePoList = new ArrayList<PrpLCharge>();
			if(charges!=null&& !charges.isEmpty()){
				for(PrpLChargeVo charge:charges){
					PrpLCharge chargePo = new PrpLCharge();
					if(StringUtils.isBlank(charge.getRiskCode())){
						charge.setRiskCode(compensateVo.getRiskCode());
					}
					charge.setPolicyNo(compensateVo.getPolicyNo());
					charge.setCreateTime(date);
					charge.setUpdateTime(date);
					Beans.copy().from(charge).to(chargePo);
					chargePo.setPrpLCompensate(compensatePo);
					chargePoList.add(chargePo);

					// databaseDao.save(PrpLCharge.class, chargePo);
				}
			}
			// 收款人信息
			List<PrpLPayment> payMentPoList = new ArrayList<PrpLPayment>();
			if(payments!=null){
				for(PrpLPaymentVo payment:payments){
					if(StringUtils.isBlank(payment.getPayFlag())){
						throw new IllegalArgumentException("计算书保存失败！收款人赔付类型不能为空！<br/>");
					}
					PrpLPayment paymentsPo = new PrpLPayment();
					payment.setCreateTime(date);
					payment.setUpdateTime(date);
					Beans.copy().from(payment).to(paymentsPo);
					paymentsPo.setPrpLCompensate(compensatePo);
					payMentPoList.add(paymentsPo);
					// databaseDao.save(PrpLPayment.class, paymentsPo);
				}
			}

			// 理算扩展表维护
			PrpLCompensateExtVo compExt = new PrpLCompensateExtVo();
			compExt.setRegistNo(compensateVo.getRegistNo());
			// compExt.setPrpLCompensate(compensateVo);
			List<PrpLClaimDeductVo> deductVos = findDeductCond(compensateVo.getRegistNo(),compensateVo.getRiskCode());
			if(deductVos!=null||deductVos.size()>0){
				// 有免赔条件
				compExt.setCheckDeductFlag("1");
			}else{
				// 无免赔条件
				compExt.setCheckDeductFlag("0");
			}
			compExt.setHurtNum(hurtNum);
			compExt.setDeathNum(deathNum);
			compExt.setInjureNum(injureNum);
			if(compensateVo.getOppoCompensateNo()!=null){// 冲销案件
				compExt.setOppoCompensateNo(compensateVo.getOppoCompensateNo());
				compExt.setWriteOffFlag(CodeConstants.WRITEOFFFLAG.ALLOFF);// TODO
			}else{
				compExt.setWriteOffFlag(CodeConstants.WRITEOFFFLAG.NORMAL);
			}
			compExt.setCreateTime(date);
			if (compensateVo.getPrpLCompensateExt() != null) {
				compExt.setDisastersFlag(compensateVo.getPrpLCompensateExt().getDisastersFlag());
			}
			PrpLCompensateExt compExtPo = new PrpLCompensateExt();
			Beans.copy().from(compExt).to(compExtPo);
			compExtPo.setPrpLCompensate(compensatePo);

			// 自动支付
			PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(compensateVo.getRegistNo());
			List<SendMsgParamVo> msgParamVoList = compensateTaskService.getsendMsgParamVo(prpLRegistVo,payments);
			if(msgParamVoList!=null&&msgParamVoList.size()>0){
				/*for(PrpLPayment po : payMentPoList){
					po.setIsAutoPay("1");
					po.setIsFastReparation("1");
				}*/

				compExtPo.setIsFastReparation("1");
			}else{
				compExtPo.setIsFastReparation("0");
			}

			if(compensateVo.getSumAmt().compareTo(BigDecimal.ZERO)==1){// 大于0
				compExtPo.setIsAutoPay("1");
			}else{
				compExtPo.setIsAutoPay("0");
			}

			compensatePo.setPrpLCompensateExt(compExtPo);
			// databaseDao.save(PrpLCompensateExt.class,compExtPo);

			// 理算关联定损表
			List<PrpLCompensateDefVo> compDefVoList = processCompensateDef(compensateVo,lossItemVos,lossPropVos,lossPersons);
			List<PrpLCompensateDef> compDefPoList = new ArrayList<PrpLCompensateDef>();
			for(PrpLCompensateDefVo compDefVo:compDefVoList){
				PrpLCompensateDef compDefPo = new PrpLCompensateDef();
				Beans.copy().from(compDefVo).to(compDefPo);
				compDefPo.setPrpLCompensate(compensatePo);
				compDefPoList.add(compDefPo);
				// databaseDao.save(PrpLCompensateDef.class, compDefPo);
			}
			// 主子表关系
			compensatePo.setPrpLLossItems(lossItemPoList);
			compensatePo.setPrpLLossPersons(persPoList);
			compensatePo.setPrpLLossProps(lossPropPoLsit);
			compensatePo.setPrpLPayments(payMentPoList);
			compensatePo.setPrpLCharges(chargePoList);
			// 循环去PrpLKindAmtSummary
			List<PrpLKindAmtSummary> prpLKindAmtSummaryList = new ArrayList<PrpLKindAmtSummary>();
			if(prpLKindAmtSummaryVoMap!=null&&prpLKindAmtSummaryVoMap.size()>0){
				for(PrpLKindAmtSummary prpLKindAmtSummary:prpLKindAmtSummaryVoMap.values()){
					prpLKindAmtSummaryList.add(prpLKindAmtSummary);
				}
				compensatePo.setPrpLKindAmtSummaries(prpLKindAmtSummaryList);
			}
		}else{
			Date date = new Date();// 创建时间
			// // 次数字段赋值 在理算更新操作时不增加
			// List<PrpLCompensateVo> underCompVoList = this.findCompensateByClaimno(compensateVo.getClaimNo(),"N");
			// int times = underCompVoList.size();
			// compensateVo.setTimes(times + 1);
			compensateVo.setUpdateTime(date);
			compensateVo.setLastModifyUser(user.getUserCode());

			Beans.copy().from(compensateVo).excludeNull().to(compensatePo);
			if (compensatePo.getPrpLCompensateExt() != null && compensateVo.getPrpLCompensateExt() != null) {
				compensatePo.getPrpLCompensateExt().setDisastersFlag(compensateVo.getPrpLCompensateExt().getDisastersFlag());
			}

			// 给子表的compensateNo 报案号 立案号 保单号等字段赋值
			// 车辆损失
			List<PrpLLossItem> lossItemPoList = compensatePo.getPrpLLossItems();
			List<PrpLLossProp> lossPropPoList = compensatePo.getPrpLLossProps();
			List<PrpLLossPerson> persPoList = compensatePo.getPrpLLossPersons();
			List<PrpLKindAmtSummary> prpLKindAmtSummaryPoList = compensatePo.getPrpLKindAmtSummaries();
			// 险别汇总金额表PrpLKindAmtSummary
			Map<String,PrpLKindAmtSummaryVo> prpLKindAmtSummaryVoMap = new HashMap<String,PrpLKindAmtSummaryVo>();

			if(lossItemVos!=null&& !lossItemVos.isEmpty()){
				for(PrpLLossItemVo lossItem:lossItemVos){
					if(lossItem.getId()==null){
						lossItem.setRiskCode(compensateVo.getRiskCode());
						lossItem.setPolicyNo(compensateVo.getPolicyNo());
						lossItem.setRegistNo(compensateVo.getRegistNo());
						lossItem.setCreateTime(date);
						lossItem.setUpdateTime(date);
					}

					if( !"B".equals(lossItem.getKindCode())){
						PrpLKindAmtSummaryVo prpLKindAmtSummaryVo = new PrpLKindAmtSummaryVo();
						prpLKindAmtSummaryVo.setKindCode(lossItem.getKindCode());
						BigDecimal kindAmtSummarySumLoss = new BigDecimal(0);
						BigDecimal kindAmtSummarySumRealPay = new BigDecimal(0);
						if(prpLKindAmtSummaryVoMap.containsKey(lossItem.getKindCode())){// 金额累加
							kindAmtSummarySumLoss = kindAmtSummarySumLoss.add(prpLKindAmtSummaryVoMap.get(lossItem.getKindCode()).getSumLoss());
							kindAmtSummarySumRealPay = kindAmtSummarySumRealPay.add(prpLKindAmtSummaryVoMap.get(lossItem.getKindCode())
									.getSumRealPay());
						}
						if(lossItem.getSumLoss()!=null){
							kindAmtSummarySumLoss = kindAmtSummarySumLoss.add(lossItem.getSumLoss());
							prpLKindAmtSummaryVo.setSumLoss(kindAmtSummarySumLoss);
						}
						if(lossItem.getSumRealPay()!=null){
							kindAmtSummarySumRealPay = kindAmtSummarySumRealPay.add(lossItem.getSumRealPay());
							prpLKindAmtSummaryVo.setSumRealPay(kindAmtSummarySumRealPay);
						}

						prpLKindAmtSummaryVo.setCreateTime(date);
						prpLKindAmtSummaryVo.setUpdateTime(date);
						prpLKindAmtSummaryVoMap.put(lossItem.getKindCode(),prpLKindAmtSummaryVo);
					}else{// 区分车财人
						PrpLKindAmtSummaryVo prpLKindAmtSummaryVo = new PrpLKindAmtSummaryVo();
						prpLKindAmtSummaryVo.setKindCode(lossItem.getKindCode());
						prpLKindAmtSummaryVo.setLossKind("1");
						BigDecimal kindAmtSummarySumLoss = new BigDecimal(0);
						BigDecimal kindAmtSummarySumRealPay = new BigDecimal(0);
						if(prpLKindAmtSummaryVoMap.containsKey(lossItem.getKindCode())){// 金额累加
							kindAmtSummarySumLoss = kindAmtSummarySumLoss.add(prpLKindAmtSummaryVoMap.get(lossItem.getKindCode()).getSumLoss());
							kindAmtSummarySumRealPay = kindAmtSummarySumRealPay.add(prpLKindAmtSummaryVoMap.get(lossItem.getKindCode())
									.getSumRealPay());
						}
						if(lossItem.getSumLoss()!=null){
							kindAmtSummarySumLoss = kindAmtSummarySumLoss.add(lossItem.getSumLoss());
							prpLKindAmtSummaryVo.setSumLoss(kindAmtSummarySumLoss);
						}
						if(lossItem.getSumRealPay()!=null){
							kindAmtSummarySumRealPay = kindAmtSummarySumRealPay.add(lossItem.getSumRealPay());
							prpLKindAmtSummaryVo.setSumRealPay(kindAmtSummarySumRealPay);
						}

						prpLKindAmtSummaryVo.setCreateTime(date);
						prpLKindAmtSummaryVo.setUpdateTime(date);
						prpLKindAmtSummaryVoMap.put(lossItem.getKindCode()+"lossItem",prpLKindAmtSummaryVo);
					}

				}
			}
			if(lossItemPoList!=null&& !lossItemPoList.isEmpty()){
				mergeList(compensatePo,lossItemVos,lossItemPoList,"id",PrpLLossItem.class,"setPrpLCompensate");

			}

			if(lossPropVos!=null&& !lossPropVos.isEmpty()){
				for(PrpLLossPropVo lossProp:lossPropVos){
					if(lossProp.getId()==null){
						lossProp.setRiskCode(compensateVo.getRiskCode());
						lossProp.setPolicyNo(compensateVo.getPolicyNo());
						lossProp.setCreateTime(date);
						lossProp.setUpdateTime(date);
					}
					if( !"B".equals(lossProp.getKindCode())){
						PrpLKindAmtSummaryVo prpLKindAmtSummaryVo = new PrpLKindAmtSummaryVo();
						prpLKindAmtSummaryVo.setKindCode(lossProp.getKindCode());
						BigDecimal kindAmtSummarySumLoss = new BigDecimal(0);
						BigDecimal kindAmtSummarySumRealPay = new BigDecimal(0);
						if(prpLKindAmtSummaryVoMap.containsKey(lossProp.getKindCode())){// 金额累加
							kindAmtSummarySumLoss = kindAmtSummarySumLoss.add(prpLKindAmtSummaryVoMap.get(lossProp.getKindCode()).getSumLoss());
							kindAmtSummarySumRealPay = kindAmtSummarySumRealPay.add(prpLKindAmtSummaryVoMap.get(lossProp.getKindCode())
									.getSumRealPay());
						}
						if(lossProp.getSumLoss()!=null){
							kindAmtSummarySumLoss = kindAmtSummarySumLoss.add(lossProp.getSumLoss());
							prpLKindAmtSummaryVo.setSumLoss(kindAmtSummarySumLoss);
						}
						if(lossProp.getSumRealPay()!=null){
							kindAmtSummarySumRealPay = kindAmtSummarySumRealPay.add(lossProp.getSumRealPay());
							prpLKindAmtSummaryVo.setSumRealPay(kindAmtSummarySumRealPay);
						}

						prpLKindAmtSummaryVo.setCreateTime(new Date());
						prpLKindAmtSummaryVo.setUpdateTime(new Date());
						prpLKindAmtSummaryVoMap.put(lossProp.getKindCode(),prpLKindAmtSummaryVo);
					}else{// 区分车财人
						PrpLKindAmtSummaryVo prpLKindAmtSummaryVo = new PrpLKindAmtSummaryVo();
						prpLKindAmtSummaryVo.setKindCode(lossProp.getKindCode());
						prpLKindAmtSummaryVo.setLossKind("2");
						BigDecimal kindAmtSummarySumLoss = new BigDecimal(0);
						BigDecimal kindAmtSummarySumRealPay = new BigDecimal(0);
						if(prpLKindAmtSummaryVoMap.containsKey(lossProp.getKindCode())){// 金额累加
							kindAmtSummarySumLoss = kindAmtSummarySumLoss.add(prpLKindAmtSummaryVoMap.get(lossProp.getKindCode()).getSumLoss());
							kindAmtSummarySumRealPay = kindAmtSummarySumRealPay.add(prpLKindAmtSummaryVoMap.get(lossProp.getKindCode())
									.getSumRealPay());
						}
						if(lossProp.getSumLoss()!=null){
							kindAmtSummarySumLoss = kindAmtSummarySumLoss.add(lossProp.getSumLoss());
							prpLKindAmtSummaryVo.setSumLoss(kindAmtSummarySumLoss);
						}
						if(lossProp.getSumRealPay()!=null){
							kindAmtSummarySumRealPay = kindAmtSummarySumRealPay.add(lossProp.getSumRealPay());
							prpLKindAmtSummaryVo.setSumRealPay(kindAmtSummarySumRealPay);
						}

						prpLKindAmtSummaryVo.setCreateTime(new Date());
						prpLKindAmtSummaryVo.setUpdateTime(new Date());
						prpLKindAmtSummaryVoMap.put(lossProp.getKindCode()+"Prop",prpLKindAmtSummaryVo);
					}
				}
			}

			mergeList(compensatePo,lossPropVos,lossPropPoList,"id",PrpLLossProp.class,"setPrpLCompensate");
			// mergeList(compensatePo,lossPersons,persPoList,"id",PrpLLossPerson.class,"setPrpLCompensate");

			if(lossPersons!=null&& !lossPersons.isEmpty()){
				for(PrpLLossPerson lossPerson:persPoList){
					for(PrpLLossPersonVo lossPersonVo:lossPersons){
						if(lossPersonVo.getId().equals(lossPerson.getId())){
							Beans.copy().from(lossPersonVo).excludeNull().to(lossPerson);

							List<PrpLLossPersonFee> perFeeList = lossPerson.getPrpLLossPersonFees();
							for(PrpLLossPersonFee perFee:perFeeList){
								for(PrpLLossPersonFeeVo perFeeVo:lossPersonVo.getPrpLLossPersonFees()){
									if(perFee.getId().equals(perFeeVo.getId())){
										Beans.copy().from(perFeeVo).excludeNull().to(perFee);
									}
									if(perFee.getPrpLLossPersonFeeDetails()!=null&&perFee.getPrpLLossPersonFeeDetails().size()>0){
										for(PrpLLossPersonFeeDetail feeDetail:perFee.getPrpLLossPersonFeeDetails()){
											if(perFeeVo.getPrpLLossPersonFeeDetails()!=null&&perFeeVo.getPrpLLossPersonFeeDetails().size()>0){
												for(PrpLLossPersonFeeDetailVo feeDetailVo:perFeeVo.getPrpLLossPersonFeeDetails()){
													if(feeDetail.getId().equals(feeDetailVo.getId())){
														Beans.copy().from(feeDetailVo).excludeNull().to(feeDetail);
													}
												}
											}
										}
									}
								}
							}

						}
					}
				}
				for(PrpLLossPersonVo lossPersonVo:lossPersons){
					if( !"B".equals(lossPersonVo.getKindCode())){
						PrpLKindAmtSummaryVo prpLKindAmtSummaryVo = new PrpLKindAmtSummaryVo();
						prpLKindAmtSummaryVo.setKindCode(lossPersonVo.getKindCode());
						BigDecimal kindAmtSummarySumLoss = new BigDecimal(0);
						BigDecimal kindAmtSummarySumRealPay = new BigDecimal(0);
						if(prpLKindAmtSummaryVoMap.containsKey(lossPersonVo.getKindCode())){// 金额累加
							kindAmtSummarySumLoss = kindAmtSummarySumLoss.add(prpLKindAmtSummaryVoMap.get(lossPersonVo.getKindCode()).getSumLoss());
							kindAmtSummarySumRealPay = kindAmtSummarySumRealPay.add(prpLKindAmtSummaryVoMap.get(lossPersonVo.getKindCode())
									.getSumRealPay());
						}
						if(lossPersonVo.getSumLoss()!=null){
							kindAmtSummarySumLoss = kindAmtSummarySumLoss.add(lossPersonVo.getSumLoss());
							prpLKindAmtSummaryVo.setSumLoss(kindAmtSummarySumLoss);
						}
						if(lossPersonVo.getSumRealPay()!=null){
							kindAmtSummarySumRealPay = kindAmtSummarySumRealPay.add(lossPersonVo.getSumRealPay());
							prpLKindAmtSummaryVo.setSumRealPay(kindAmtSummarySumRealPay);
						}

						prpLKindAmtSummaryVo.setCreateTime(new Date());
						prpLKindAmtSummaryVo.setUpdateTime(new Date());
						prpLKindAmtSummaryVoMap.put(lossPersonVo.getRiskCode(),prpLKindAmtSummaryVo);
					}else{// 区分车财人
						PrpLKindAmtSummaryVo prpLKindAmtSummaryVo = new PrpLKindAmtSummaryVo();
						prpLKindAmtSummaryVo.setKindCode(lossPersonVo.getKindCode());
						prpLKindAmtSummaryVo.setLossKind("3");
						BigDecimal kindAmtSummarySumLoss = new BigDecimal(0);
						BigDecimal kindAmtSummarySumRealPay = new BigDecimal(0);
						if(prpLKindAmtSummaryVoMap.containsKey(lossPersonVo.getKindCode())){// 金额累加
							kindAmtSummarySumLoss = kindAmtSummarySumLoss.add(prpLKindAmtSummaryVoMap.get(lossPersonVo.getKindCode()).getSumLoss());
							kindAmtSummarySumRealPay = kindAmtSummarySumRealPay.add(prpLKindAmtSummaryVoMap.get(lossPersonVo.getKindCode())
									.getSumRealPay());
						}
						if(lossPersonVo.getSumLoss()!=null){
							kindAmtSummarySumLoss = kindAmtSummarySumLoss.add(lossPersonVo.getSumLoss());
							prpLKindAmtSummaryVo.setSumLoss(kindAmtSummarySumLoss);
						}
						if(lossPersonVo.getSumRealPay()!=null){
							kindAmtSummarySumRealPay = kindAmtSummarySumRealPay.add(lossPersonVo.getSumRealPay());
							prpLKindAmtSummaryVo.setSumRealPay(kindAmtSummarySumRealPay);
						}

						prpLKindAmtSummaryVo.setCreateTime(new Date());
						prpLKindAmtSummaryVo.setUpdateTime(new Date());
						prpLKindAmtSummaryVoMap.put(lossPersonVo.getKindCode()+"P",prpLKindAmtSummaryVo);
					}
				}

			}

			// // 费用赔款
			List<PrpLCharge> chargePoList = compensatePo.getPrpLCharges();
			if(charges!=null&& !charges.isEmpty()){
				for(PrpLChargeVo chargeVo:charges){
					if(chargeVo.getId()==null){
						if(StringUtils.isBlank(chargeVo.getRiskCode())){
							chargeVo.setRiskCode(compensateVo.getRiskCode());
						}
						chargeVo.setPolicyNo(compensateVo.getPolicyNo());
						chargeVo.setCreateTime(date);
						chargeVo.setUpdateTime(date);
					}
				}
			}

			mergeList(compensatePo,charges,chargePoList,"id",PrpLCharge.class,"setPrpLCompensate");

			// 收款人信息
			List<PrpLPayment> prpLPayment = compensatePo.getPrpLPayments();

			// 自动支付
			PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(compensateVo.getRegistNo());
			List<SendMsgParamVo> msgParamVoList = compensateTaskService.getsendMsgParamVo(prpLRegistVo,payments);
			PrpLCompensateExt lCompensateExt = compensatePo.getPrpLCompensateExt();
			if(msgParamVoList!=null&&msgParamVoList.size()>0){
				lCompensateExt.setIsFastReparation("1");

			}else{
				lCompensateExt.setIsFastReparation("0");
			}
			if(compensateVo.getSumAmt().compareTo(BigDecimal.ZERO)==1){// 大于0
				lCompensateExt.setIsAutoPay("1");
			}else{
				lCompensateExt.setIsAutoPay("0");
			}
			compensatePo.setPrpLCompensateExt(lCompensateExt);
			mergeList(compensatePo,payments,prpLPayment,"id",PrpLPayment.class,"setPrpLCompensate");

			// 循环去PrpLKindAmtSummary
			List<PrpLKindAmtSummaryVo> prpLKindAmtSummaryVoList = new ArrayList<PrpLKindAmtSummaryVo>();
			if(prpLKindAmtSummaryVoMap!=null&&prpLKindAmtSummaryVoMap.size()>0){
				for(PrpLKindAmtSummaryVo prpLKindAmtSummaryVo:prpLKindAmtSummaryVoMap.values()){
					for(PrpLKindAmtSummary prpLKindAmtSummary:prpLKindAmtSummaryPoList){
						if("B".equals(prpLKindAmtSummary.getKindCode())&&prpLKindAmtSummary.getLossKind().equals(prpLKindAmtSummaryVo.getLossKind())){
							prpLKindAmtSummaryVo.setId(prpLKindAmtSummary.getId());
						}else if( !"B".equals(prpLKindAmtSummary.getKindCode())&&prpLKindAmtSummary.getKindCode().equals(
								prpLKindAmtSummaryVo.getKindCode())){
							prpLKindAmtSummaryVo.setId(prpLKindAmtSummary.getId());
						}
					}
					prpLKindAmtSummaryVoList.add(prpLKindAmtSummaryVo);
				}
			}

			mergeList(compensatePo,prpLKindAmtSummaryVoList,prpLKindAmtSummaryPoList,"id",PrpLKindAmtSummary.class,"setPrpLCompensate");

			// 主子表关系
			compensatePo.setPrpLLossItems(lossItemPoList);
			compensatePo.setPrpLLossPersons(persPoList);
			compensatePo.setPrpLLossProps(lossPropPoList);
			compensatePo.setPrpLPayments(prpLPayment);
			compensatePo.setPrpLCharges(chargePoList);
			compensatePo.setPrpLKindAmtSummaries(prpLKindAmtSummaryPoList);
		}

		// 管控 如果理算主表赔款金额不为0，收款人子表不可以为空
		if(( BigDecimal.ZERO.compareTo(compensatePo.getSumPaidAmt())== -1&&compensatePo.getPrpLPayments().size()==0 )||( BigDecimal.ZERO
				.compareTo(compensatePo.getSumPaidFee())== -1&&compensatePo.getPrpLCharges().size()==0 )){
			throw new IllegalArgumentException("计算书保存失败！收款人信息不能为空！<br/>");
		}
		// 费用在后台累加
		BigDecimal sumChargeRealFee = BigDecimal.ZERO;// 本次实赔
		BigDecimal sumChargeFee = BigDecimal.ZERO;// 总赔付
		if(compensatePo.getPrpLCharges().size()>0){
			// 费用在后台累加
			for(PrpLCharge charge:compensatePo.getPrpLCharges()){
				sumChargeRealFee = sumChargeRealFee.add(charge.getFeeRealAmt());
				sumChargeFee = sumChargeFee.add(charge.getFeeAmt()).subtract(charge.getOffAmt());
			}
		}
		// 添加管控，如果银行账号相同的数据在系统中已存在，且户名不同，提示账户已存在户名不同，且不允许保存 硬管控
		if(compensatePo.getPrpLPayments()!=null&&compensatePo.getPrpLPayments().size()>0){
			for(PrpLPayment payment:compensatePo.getPrpLPayments()){
				PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(payment.getPayeeId());
				PrpLPayCustomVo existPayCus = managerService.adjustExistSamePayCusDifName(payCustomVo);
				if(existPayCus!=null&&existPayCus.getPayeeName()!=null){
					throw new IllegalArgumentException("保存失败！该账号已存于案件"+existPayCus.getRegistNo()+"，且户名为"+existPayCus.getPayeeName()+"！");
				}
			}
		}

		compensatePo.setSumPaidFee(sumChargeRealFee);
		compensatePo.setSumFee(sumChargeFee);
		databaseDao.save(PrpLCompensate.class,compensatePo);
		return compensatePo.getCompensateNo();
	}

	/**
	 * 判断该立案号下只能存在 一张 核赔未通过的 计算书 如果存在 返回compensatePo进行更新操作 如果不存在 compensatePo为空 执行新增操作
	 * 
	 * <pre></pre>
	 * @param claimNo
	 * @return
	 * @modified: ☆WLL(2016年10月14日 下午6:18:37): <br>
	 */
	private PrpLCompensate adjustCompUpdateOrSave(String claimNo,String compensateNo) {
		// 先查询该计算书号在数据库是否有数据
		PrpLCompensate compensatePo = null;
		if(compensateNo!=null){
			compensatePo = databaseDao.findByPK(PrpLCompensate.class,compensateNo);
		}

		if(compensatePo==null){
			// 再查询该立案号下是否已存在一张未核赔通过的计算书
			QueryRule queryRule = QueryRule.getInstance();
			queryRule.addEqual("claimNo",claimNo);
			queryRule.addEqual("underwriteFlag","0");
			List<PrpLCompensate> compPoList = databaseDao.findAll(PrpLCompensate.class,queryRule);
			if(compPoList!=null&&compPoList.size()>0){
				compensatePo = compPoList.get(0);
			}
		}
		return compensatePo;
	}

	/**
	 * 处理理算关联定损表的数据
	 * @param compensateVo
	 * @param lossItemVos
	 * @param lossPropVos
	 * @param lossPersons
	 */
	public List<PrpLCompensateDefVo> processCompensateDef(PrpLCompensateVo compensateVo,List<PrpLLossItemVo> lossItemVos,
															List<PrpLLossPropVo> lossPropVos,List<PrpLLossPersonVo> lossPersons) {
		List<PrpLCompensateDefVo> compDefList = new ArrayList<PrpLCompensateDefVo>();
		Date date = new Date();
		for(PrpLLossItemVo lossItem:lossItemVos){
			if(lossItem!=null){
				PrpLCompensateDefVo compDef = new PrpLCompensateDefVo();
				// compDef.setPrpLCompensate(compensateVo);
				compDef.setBusinessId(lossItem.getDlossId());
				compDef.setBusinessType("DLCar");// 定损类型-车辆损失
				compDef.setCreateTime(date);
				compDef.setUpdateTime(date);
				compDefList.add(compDef);
			}
		}
		for(PrpLLossPropVo propItem:lossPropVos){
			PrpLCompensateDefVo compDef = new PrpLCompensateDefVo();
			// compDef.setPrpLCompensate(compensateVo);
			compDef.setBusinessId(propItem.getDlossId());
			compDef.setBusinessType("DLProp");// 定损类型-财产损失
			compDef.setCreateTime(date);
			compDef.setUpdateTime(date);
			compDefList.add(compDef);
		}
		for(PrpLLossPersonVo persItem:lossPersons){
			PrpLCompensateDefVo compDef = new PrpLCompensateDefVo();
			// compDef.setPrpLCompensate(compensateVo);
			compDef.setBusinessId(persItem.getDlossId());
			compDef.setBusinessType("PLoss");// 定损类型-人伤损失
			compDef.setCreateTime(date);
			compDef.setUpdateTime(date);
			compDefList.add(compDef);
		}
		return compDefList;
	}

	/**
	 * 注销理算
	 * @param compensateNo
	 */

	public void cancelCompensates(PrpLCompensateVo compVo,BigDecimal flowTaskId,SysUserVo userVo) {
		logger.info("计算书号为compensateno="+ compVo.getCompensateNo() + "进入理算注销回写标志位UnderwriteFlag方法");
		compVo.setUnderwriteFlag("7");// 修改计算书的核赔状态字段
		PrpLCompensate compPo = new PrpLCompensate();
		Beans.copy().from(compVo).to(compPo);
		databaseDao.update(PrpLCompensate.class,compPo);
		// 调用工作流注销方法
		wfTaskHandleService.cancelTask(userVo.getUserCode(),flowTaskId);
		logger.info("计算书号为compensateno=" + compVo.getCompensateNo() + ",结束理算注销回写标志位UnderwriteFlag方法");
	}

	/**
	 * 注销理算
	 * @param compensateNo
	 */

	public void cancelCompensate(BigDecimal flowTaskId,SysUserVo userVo) {
		// 调用工作流注销方法
		wfTaskHandleService.cancelTask(userVo.getUserCode(),flowTaskId);
	}

	/**
	 * 根据计算书号查询赔款标的信息表PrpLLossItemVo
	 * @param compNo
	 * @return
	 */
	public List<PrpLLossItemVo> findPrpLLossByCompNo(String compNo) {
		List<PrpLLossItemVo> PrpLLossItemVos = new ArrayList<PrpLLossItemVo>();
		PrpLLossItemVo lossVo = new PrpLLossItemVo();
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("prpLCompensate.compensateNo",compNo);
		List<PrpLLossItem> PrpLLossPos = databaseDao.findAll(PrpLLossItem.class,qr);
		if(PrpLLossPos!=null&&PrpLLossPos.size()>0){
			for(PrpLLossItem lossPo:PrpLLossPos){
				lossVo = Beans.copyDepth().from(lossPo).to(PrpLLossItemVo.class);
				PrpLLossItemVos.add(lossVo);
			}
		}
		return PrpLLossItemVos;
	}

	/**
	 * <pre></pre>
	 * @param compensateNo
	 * @param feeType P-赔款,F-费用,null-all
	 * @return
	 * @modified: ☆XMSH(2016年4月19日 下午4:51:40): <br>
	 */
	public List<PrpLPrePayVo> getPrePayVo(String compensateNo,String feeType) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("compensateNo",compensateNo);
		queryRule.addEqualIfExist("feeType",feeType);
		List<PrpLPrePayVo> PrpLPrePayVoList = null;
		List<PrpLPrePay> PrpLPrePayList = databaseDao.findAll(PrpLPrePay.class,queryRule);
		if(PrpLPrePayList!=null&& !PrpLPrePayList.isEmpty()){
			PrpLPrePayVoList = new ArrayList<PrpLPrePayVo>();
			for(PrpLPrePay prePay:PrpLPrePayList){
				PrpLPrePayVo prePayVo = new PrpLPrePayVo();
				String serialNo = prePay.getSerialNo();
				logger.info("计算书号: " + compensateNo +" prepay.id: " + prePay.getId() + " serialNo: " + serialNo);
				Beans.copy().from(prePay).to(prePayVo);
				prePayVo.setSerialNo(serialNo);
				PrpLPayCustomVo customVo = managerService.findPayCustomVoById(prePay.getPayeeId());// 获取收款人信息
				prePayVo.setAccountNo(customVo.getAccountNo());
				prePayVo.setBankName(customVo.getBankName());
				prePayVo.setPayObjectKind(customVo.getPayObjectKind());
				String lastNo = customVo.getAccountNo().length()>4 ? customVo.getAccountNo().substring(customVo.getAccountNo().length()-4) : customVo
						.getAccountNo();
				prePayVo.setPayeeName(customVo.getPayeeName()+"-"+lastNo);
				logger.info("计算书号: " + compensateNo + " prepayVo.id: " + prePayVo.getId() + " serialNo: " + prePayVo.getSerialNo());
				PrpLPrePayVoList.add(prePayVo);
			}
		}
		if(PrpLPrePayVoList!=null){
			return PrpLPrePayVoList;
		}else{
			return null;
		}

	}

	/**
	 * 保存或更新理算主表
	 * @param compensateVo
	 * @modified: ☆XMSH(2016年4月5日 下午5:38:33): <br>
	 */
	public String saveOrUpdateCompensateVo(PrpLCompensateVo compensateVo,SysUserVo userVo) {
		PrpLCompensate prpLCompensate = databaseDao.findByPK(PrpLCompensate.class,compensateVo.getCompensateNo());

		if(prpLCompensate==null){// 新增
			prpLCompensate = new PrpLCompensate();
			prpLCompensate = Beans.copyDepth().from(compensateVo).to(PrpLCompensate.class);
			prpLCompensate.setCreateTime(new Date());
			prpLCompensate.setUpdateTime(new Date());
			prpLCompensate.setCreateUser(userVo.getUserCode());
			// 设置主子表关系
			PrpLCompensateExt ext = prpLCompensate.getPrpLCompensateExt();
			ext.setCreateTime(new Date());
			ext.setUpdateTime(new Date());
			ext.setPrpLCompensate(prpLCompensate);
		}else{// 更新
			Beans.copy().from(compensateVo).excludeNull().to(prpLCompensate);
			prpLCompensate.setUpdateTime(new Date());
			// 设置主子表关系
			PrpLCompensateExt ext = prpLCompensate.getPrpLCompensateExt();
			Beans.copy().from(compensateVo.getPrpLCompensateExt()).excludeNull().to(ext);
			ext.setPayBackState(compensateVo.getPrpLCompensateExt().getPayBackState());
			ext.setIsCompDeduct(compensateVo.getPrpLCompensateExt().getIsCompDeduct());
			if(ext!=null){
				ext.setUpdateTime(new Date());
				ext.setPrpLCompensate(prpLCompensate);
			}

		}
		databaseDao.save(PrpLCompensate.class,prpLCompensate);
		return prpLCompensate.getCompensateNo();
	}

	/**
	 * 保存理算扩展表 TODO 有问题
	 * @param compensateExtVo
	 */
	public void saveOrUpdateCompExtVo(PrpLCompensateExtVo compensateExtVo,PrpLCompensateVo compOriginVo) {
		PrpLCompensateExt compExt = new PrpLCompensateExt();
		PrpLCompensateVo compVo = compOriginVo;
		PrpLCompensate compPo = new PrpLCompensate();
		Beans.copy().from(compVo).to(compPo);

		if(compensateExtVo.getId()==null){
			compensateExtVo.setCreateTime(new Date());
			compensateExtVo.setUpdateTime(new Date());
			Beans.copy().from(compensateExtVo).to(compExt);
			compExt.setPrpLCompensate(compPo);
			compPo.setPrpLCompensateExt(compExt);
			// databaseDao.update(PrpLCompensateExt.class, compExt);
		}else{
			compensateExtVo.setUpdateTime(new Date());
			Beans.copy().from(compensateExtVo).to(compExt);
			compExt.setPrpLCompensate(compPo);
			compPo.setPrpLCompensateExt(compExt);
			// databaseDao.save(PrpLCompensateExt.class, compExt);
		}
		databaseDao.update(PrpLCompensate.class,compPo);
	}

	/**
	 * 保存预付费用信息
	 * @param prePayVos
	 * @param compensateNo
	 * @modified: ☆XMSH(2016年4月5日 下午5:57:57): <br>
	 */
	public void saveOrUpdatePrePay(List<PrpLPrePayVo> prePayVos,String compensateNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("compensateNo",compensateNo);
		List<PrpLPrePay> prePays = databaseDao.findAll(PrpLPrePay.class,queryRule);

		if(prePays!=null&&prePays.size()>0){
			mergeList(prePayVos,prePays,"id",PrpLPrePay.class);
		}else{
			prePays = Beans.copyDepth().from(prePayVos).toList(PrpLPrePay.class);
		}
		// int i = 0;
		/*for(PrpLPrePay prePay:prePays){// 赋值序号，
			// Fix By LiYi
			if(prePay.getSerialNo()==null||"".equals(prePay.getSerialNo())){
				if(compensateNo.equals(prePay.getCompensateNo())){
					prePay.setSerialNo(( prePays.size()-1 )+"");
				}
			}
			// prePay.setSerialNo(Integer.toString(i));
			// i++;
		}*/
		if(prePays!=null&&prePays.size()>0){
			for(int i=0;i<prePays.size();i++){
				prePays.get(i).setSerialNo(Integer.toString(i));
			}
		}
		// 添加管控，如果银行账号相同的数据在系统中已存在，且户名不同，提示账户已存在户名不同，且不允许保存 硬管控
		if(prePays!=null&&prePays.size()>0){
			for(PrpLPrePay payment:prePays){
				PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(payment.getPayeeId());
				PrpLPayCustomVo existPayCus = managerService.adjustExistSamePayCusDifName(payCustomVo);
				if(existPayCus!=null&&existPayCus.getPayeeName()!=null){
					throw new IllegalArgumentException("保存失败！该账号已存于案件"+existPayCus.getRegistNo()+"，且户名为"+existPayCus.getPayeeName()+"！");
				}
			}
		}
		databaseDao.saveAll(PrpLPrePay.class,prePays);
	}

	public void writeBackPay(List<PrpLPrePayVo> prePayVos,String compensateNo) throws Exception {
		/*QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("compensateNo",compensateNo);
		List<PrpLPrePay> prePays = databaseDao.findAll(PrpLPrePay.class,queryRule);
		
		if(prePays !=null &&prePays.size()>0){
			mergeList(prePayVos,prePays,"id",PrpLPrePay.class);
		}else{
			prePays = Beans.copyDepth().from(prePayVos).toList(PrpLPrePay.class);
		}*/
		for(PrpLPrePayVo prePayVo:prePayVos){
			PrpLPrePay prePayPo = databaseDao.findByPK(PrpLPrePay.class,prePayVo.getId());
			Beans.copy().excludeNull().from(prePayVo).to(prePayPo);
			databaseDao.update(PrpLPrePay.class,prePayPo);
		}
	}

	public void mergeList(List voList,List poList,String idName,Class paramClass) {
		Map<Object,Object> map = new HashMap<Object,Object>();
		Map<Integer,Object> keyMap = new HashMap<Integer,Object>();
		Map<Object,Object> poMap = new HashMap<Object,Object>();

		for(int i = 0,count = voList.size(); i<count; i++ ){
			Object element = voList.get(i);
			if(element==null){
				continue;
			}
			Object key;
			try{
				key = PropertyUtils.getProperty(element,idName);
				map.put(key,element);
				keyMap.put(i,key);
			}catch(Exception e){
				throw new IllegalArgumentException(e);
			}
		}

		for(Iterator it = poList.iterator(); it.hasNext();){
			Object element = (Object)it.next();
			try{
				Object key = PropertyUtils.getProperty(element,idName);
				poMap.put(key,null);
				if( !map.containsKey(key)){
					// delete(element);
					databaseDao.deleteByObject(paramClass,element);
					it.remove();
				}else{
					Beans.copy().from(map.get(key)).excludeNull().to(element);
				}
			}catch(Exception e){
				throw new IllegalArgumentException(e);
			}
		}
		for(int i = 0,count = voList.size(); i<count; i++ ){
			Object element = voList.get(i);
			if(element==null){
				continue;
			}
			try{
				Object poElement = paramClass.newInstance();
				Object key = keyMap.get(i);
				if(key==null|| !poMap.containsKey(key)){
					Method setMethod;
					Beans.copy().from(element).to(poElement);
					poList.add(poElement);
				}
			}catch(Exception e){
				throw new IllegalArgumentException(e);
			}
		}
	}

	/**
	 * 获取免赔条件
	 * @param registNo
	 * @param riskCode
	 * @return
	 */
	public List<PrpLClaimDeductVo> findDeductCond(String registNo,String riskCode) {
		List<PrpLClaimDeductVo> claimDeductVoList = new ArrayList<PrpLClaimDeductVo>();
		claimDeductVoList = registQueryService.findClaimDeductVoByRegistNo(registNo);// 获取该报案号下所有免赔条件
		List<PrpLClaimDeductVo> tempList = new ArrayList<PrpLClaimDeductVo>();
		List<PrpLClaimDeductVo> reClaimDeductVoList = new ArrayList<PrpLClaimDeductVo>();

		List<String> deductCondList = new ArrayList<String>();
		// 遍历生成免赔条件List并去除重复
		if(claimDeductVoList!=null&&claimDeductVoList.size()>0){
			// 根据险种筛选数据
			for(PrpLClaimDeductVo temp:claimDeductVoList){
				if(temp.getRiskCode().equals(riskCode)){
					deductCondList.add(temp.getDeductCondCode());
					tempList.add(temp);
				}
			}
			Set<String> deductCondSet = new HashSet<String>();
			deductCondSet.addAll(deductCondList); // 将list中的值加入set,并去掉重复的条件代码
			for(PrpLClaimDeductVo tempB:tempList){
				int reflag = 0;
				String remCond = "";
				for(String deductCond:deductCondSet){
					if(tempB.getDeductCondCode().equals(deductCond)){
						reflag = 1;
						remCond = deductCond;
					}
				}
				if(reflag==1){
					reClaimDeductVoList.add(tempB);
					deductCondSet.remove(remCond);
				}
			}
		}
		return reClaimDeductVoList;
	}

	/**
	 * 理算环节勾选免赔条件后回写免赔条件表
	 * @return
	 * @author lanlei
	 */
	public AjaxResult updateDeductCondForComp(List<PrpLClaimDeductVo> claimDeductSendList,String registNo) {
		AjaxResult ajaxResult = new AjaxResult();
		List<PrpLClaimDeductVo> claimDeductQueryList = registQueryService.findClaimDeductVoByRegistNo(registNo);
		// 获取该报案号下所有免赔条件数据
		String riskCode = claimDeductSendList.get(0).getRiskCode();
		List<PrpLClaimDeductVo> tempList = new ArrayList<PrpLClaimDeductVo>();
		// 根据险种筛选数据
		for(PrpLClaimDeductVo temp:claimDeductQueryList){
			if(temp.getRiskCode().equals(riskCode)){
				tempList.add(temp);
			}
		}
		// 根据提交过来的List更新免赔条件的isCheck字段
		for(PrpLClaimDeductVo claimDeductQ:tempList){
			for(PrpLClaimDeductVo claimDeductS:claimDeductSendList){
				if(claimDeductQ.getDeductCondCode().equals(claimDeductS.getDeductCondCode())){
					claimDeductQ.setIsCheck(claimDeductS.getIsCheck());
					break;
				}
			}
		}
		registQueryService.updateClaimDeduct(tempList);
		return ajaxResult;
	}

	/**
	 * 根据免赔条件的勾选计算险别对应免赔率
	 * @param registNo
	 * @param riskCode
	 * @return
	 */
	public Map<String,BigDecimal> getAddKindRateMapByDeductCond(String registNo,String riskCode) {
		Map<String,BigDecimal> kindRateMap = new HashMap<String,BigDecimal>();
		List<PrpLClaimDeductVo> claimDeductVoList = registQueryService.findClaimDeductVoByRegistNo(registNo);// 获取该报案号下所有免赔条件
		List<PrpLClaimDeductVo> tempList = new ArrayList<PrpLClaimDeductVo>();
		if(claimDeductVoList!=null&&claimDeductVoList.size()>0){
			// 根据险种和选中状态筛选数据
			List<String> kindList = new ArrayList<String>();
			for(PrpLClaimDeductVo temp:claimDeductVoList){
				if(temp.getRiskCode().equals(riskCode)){
					kindList.add(temp.getKindCode());
					if(temp.getIsCheck().equals("1")){
						tempList.add(temp);
					}
				}
			}
			Set<String> kindSet = new HashSet<String>();
			kindSet.addAll(kindList); // 将list中的值加入set,并去掉重复的条件代码
			BigDecimal rate = new BigDecimal(0);
			// 把不重复的险别放到Map中
			for(String kindCode:kindSet){
				kindRateMap.put(kindCode,rate);
			}

			for(String kindCode:kindSet){
				for(PrpLClaimDeductVo temp:tempList){
					if(temp.getKindCode().equals(kindCode)){
						BigDecimal rateChange = kindRateMap.get(kindCode);
						rateChange = rateChange.add(temp.getDeductRate());
						kindRateMap.put(kindCode,rateChange);
					}
				}
			}
		}
		return kindRateMap;
	}

	/**
	 * 根据事故责任类型决定事故责任免赔率
	 * @param dutyType
	 * @return
	 */
	public Map<String,BigDecimal> getDutyRateByDutyType(String registNo,String dutyType,String riskCode) {
		Map<String,BigDecimal> kindRateMap = new HashMap<String,BigDecimal>();
		List<PrpLClaimKindVo> kindVoList = claimService.findClaimKindVoListByRegistNo(registNo);
		BigDecimal dutyRate = new BigDecimal(0);
		boolean cprc = this.isCprcCase(registNo);
		if(cprc){
			for(PrpLClaimKindVo kindVo:kindVoList){
				if(kindVo.getKindCode().equals(KINDCODE.KINDCODE_A)||kindVo.getKindCode().equals(KINDCODE.KINDCODE_B)||kindVo.getKindCode().equals(
						KINDCODE.KINDCODE_D11)||kindVo.getKindCode().equals(KINDCODE.KINDCODE_D12)||kindVo.getKindCode().equals(KINDCODE.KINDCODE_X)){
					if(dutyType.equals("0")){
						dutyRate = new BigDecimal(20);
					}else if(dutyType.equals("1")){
						dutyRate = new BigDecimal(15);
					}else if(dutyType.equals("2")){
						dutyRate = new BigDecimal(10);
					}else if(dutyType.equals("3")){
						dutyRate = new BigDecimal(5);
					}else if(dutyType.equals("4")){
						dutyRate = new BigDecimal(0);
					}

					kindRateMap.put(kindVo.getKindCode(),dutyRate);
				}
			}
		}else{
			for(PrpLClaimKindVo kindVo:kindVoList){// 15 10 8 5 0
				if(kindVo.getKindCode().equals(KINDCODE.KINDCODE_A)||kindVo.getKindCode().equals(KINDCODE.KINDCODE_D11)||kindVo.getKindCode().equals(
						KINDCODE.KINDCODE_D12)||kindVo.getKindCode().equals(KINDCODE.KINDCODE_X)){
					if(dutyType.equals("0")){
						dutyRate = new BigDecimal(15);
					}else if(dutyType.equals("1")){
						dutyRate = new BigDecimal(10);
					}else if(dutyType.equals("2")){
						dutyRate = new BigDecimal(8);
					}else if(dutyType.equals("3")){
						dutyRate = new BigDecimal(5);
					}else if(dutyType.equals("4")){
						dutyRate = new BigDecimal(0);
					}

					kindRateMap.put(kindVo.getKindCode(),dutyRate);
				}else if(kindVo.getKindCode().equals(KINDCODE.KINDCODE_B)){// 20 15 10 5 0
					if(dutyType.equals("0")){
						dutyRate = new BigDecimal(20);
					}else if(dutyType.equals("1")){
						dutyRate = new BigDecimal(15);
					}else if(dutyType.equals("2")){
						dutyRate = new BigDecimal(10);
					}else if(dutyType.equals("3")){
						dutyRate = new BigDecimal(5);
					}else if(dutyType.equals("4")){
						dutyRate = new BigDecimal(0);
					}
					kindRateMap.put(kindVo.getKindCode(),dutyRate);
				}
			}
		}
		return kindRateMap;
	}

	/**
	 * 根据报案号获取立案险别金额表中险别对应的绝对免赔
	 * @param registNo
	 * @return
	 */
	public Map<String,BigDecimal> getAbsKindRateMapByRegistNo(String registNo) {
		Map<String,BigDecimal> kindRateMap = new HashMap<String,BigDecimal>();
		List<PrpLClaimKindVo> kindVoList = claimService.findClaimKindVoListByRegistNo(registNo);
		for(PrpLClaimKindVo kindVo:kindVoList){
			kindRateMap.put(kindVo.getKindCode(),kindVo.getDeductibleRate());
		}
		return kindRateMap;
	}

	/**
	 * 根据立案号查询所有有效计算书
	 * 
	 * <pre></pre>
	 * @param claimNo
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月14日 上午11:33:56): <br>
	 */
	public List<PrpLCompensateVo> findPrpLCompensateVoListByClaimNo(String claimNo) {
		List<PrpLCompensateVo> prpLCompensateVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("claimNo",claimNo);
		queryRule.addNotEqual("underwriteFlag",CodeConstants.UnderWriteFlag.CANCELFLAG);
		List<PrpLCompensate> prpLCompensateList = databaseDao.findAll(PrpLCompensate.class,queryRule);
		if(prpLCompensateList==null||prpLCompensateList.isEmpty()){
			return null;
		}

		prpLCompensateVoList = Beans.copyDepth().from(prpLCompensateList).toList(PrpLCompensateVo.class);
		return prpLCompensateVoList;
	}

	/**
	 * 根据报案号获取不计免赔附加险
	 * @param registNo
	 * @return
	 */
	public List<PrpLCItemKindVo> getDeductOffKindList(String registNo) {
		List<PrpLCItemKindVo> cItemKindList = registQueryService.findCItemKindListByRegistNo(registNo);
		// 不计免赔险
		List<PrpLCItemKindVo> claimKindMList = new ArrayList<PrpLCItemKindVo>();
		for(PrpLCItemKindVo cItemKindVo:cItemKindList){
			// 旧条款-是否不计免赔标志位第五位为1
			// 新条款-kindCode以M结尾的都是不计免赔险
			if( !"1101".equals(cItemKindVo.getRiskCode())&& !CodeConstants.ISNEWCLAUSECODE_MAP.get(cItemKindVo.getRiskCode())&&cItemKindVo.getFlag()
					.length()>4&&String.valueOf(cItemKindVo.getFlag().charAt(4)).equals("1")){
				claimKindMList.add(cItemKindVo);
			}else{
				if(StringUtils.isNotBlank(cItemKindVo.getKindCode())&&cItemKindVo.getKindCode()
						.substring(cItemKindVo.getKindCode().length()-1,cItemKindVo.getKindCode().length()).equals("M")){
					claimKindMList.add(cItemKindVo);
				}
			}
		}
		return claimKindMList;
	}

	/**
	 * 判断数据表的理算状态
	 * @param lossState
	 * @param bcFlag
	 * @return
	 */
	public boolean checkLossState(String lossState,String bcFlag) {
		// 第一位-交强 第二位-商业 0-未理算 1-已理算
		if(StringUtils.isNotBlank(lossState)){
			if(bcFlag.equals("11")){
				if(lossState.equals("00")||lossState.equals("01")||lossState.equals("0")){
					return true;// 交强未理算
				}
			}
			if(bcFlag.equals("12")){
				if(lossState.equals("00")||lossState.equals("10")||lossState.equals("0")){
					return true;
				}
			}
		}
		return false;// 已理算
	}

	/**
	 * 根据报案号查询交强理算的无责代赔总额
	 * @param registNo
	 * @return
	 */
	public BigDecimal getNoDutyAmt(String registNo) {
		BigDecimal noDutyAmt = new BigDecimal(0);
		List<PrpLLossItemVo> itemList = findLossItemsByRegistNo(registNo);
		for(PrpLLossItemVo lossItem:itemList){
			if(CodeConstants.PayFlagType.NODUTY_INSTEAD_PAY.equals(lossItem.getPayFlag().trim())){
				noDutyAmt.add(lossItem.getSumRealPay());
			}
		}
		return noDutyAmt;
	}

	/**
	 * 根据报案号获得车损数据
	 * @param registNo 报案号
	 * @param reLossCarMainVos 车辆损失任务主表List
	 * @param subRogationFlag 代位求偿标志
	 * @param bcFlag 商业/交强理算标志
	 * @return
	 */
	public List<PrpLLossItemVo> getBILossItemVoList(String registNo,List<PrpLDlossCarMainVo> reLossCarMainVos,String subRogationFlag,String bcFlag,
													List<PrpLCItemKindVo> itemKindVoList) {

		/**
		 * 组织车损数据(后续专门提取成一个方法)
		 */
		List<PrpLLossItemVo> rePrpLLossItemVo = new ArrayList<PrpLLossItemVo>();
		Map<String,BigDecimal> kindASumMap = new HashMap<String,BigDecimal>();// 险别-损失金额Map
		if(reLossCarMainVos==null||reLossCarMainVos.isEmpty()){
			return rePrpLLossItemVo;
		}

		for(PrpLDlossCarMainVo prpLDlossCarMainVo:reLossCarMainVos){
			// 换件项目清单
			List<PrpLDlossCarCompVo> prpLDlossCarCompVoList = prpLDlossCarMainVo.getPrpLDlossCarComps();
			// 零部件辅料信息
			List<PrpLDlossCarMaterialVo> prpLDlossCarMaterialVoList = prpLDlossCarMainVo.getPrpLDlossCarMaterials();
			// 零部件修理费用清单表
			List<PrpLDlossCarRepairVo> prpLDlossCarRepairVoList = prpLDlossCarMainVo.getPrpLDlossCarRepairs();

			if(prpLDlossCarCompVoList!=null&& !prpLDlossCarCompVoList.isEmpty()){
				for(PrpLDlossCarCompVo prpLDlossCarCompVo:prpLDlossCarCompVoList){
					if(kindASumMap.containsKey(prpLDlossCarCompVo.getKindCode().trim())){
						kindASumMap.put(prpLDlossCarCompVo.getKindCode().trim(),
								kindASumMap.get(prpLDlossCarCompVo.getKindCode().trim()).add(prpLDlossCarCompVo.getSumVeriLoss()));
					}else{
						kindASumMap.put(prpLDlossCarCompVo.getKindCode().trim(),prpLDlossCarCompVo.getSumVeriLoss());
					}
				}
			}

			if(prpLDlossCarMaterialVoList!=null&& !prpLDlossCarMaterialVoList.isEmpty()){
				for(PrpLDlossCarMaterialVo prpLDlossCarMaterialVo:prpLDlossCarMaterialVoList){
					if(kindASumMap.containsKey(prpLDlossCarMaterialVo.getKindCode().trim())){
						kindASumMap.put(prpLDlossCarMaterialVo.getKindCode().trim(),
								kindASumMap.get(prpLDlossCarMaterialVo.getKindCode().trim()).add(prpLDlossCarMaterialVo.getAuditMaterialFee()));
					}else{
						kindASumMap.put(prpLDlossCarMaterialVo.getKindCode().trim(),prpLDlossCarMaterialVo.getAuditMaterialFee());
					}
				}
			}

			if(prpLDlossCarRepairVoList!=null&& !prpLDlossCarRepairVoList.isEmpty()){
				for(PrpLDlossCarRepairVo prpLDlossCarRepairVo:prpLDlossCarRepairVoList){
					if(kindASumMap.containsKey(prpLDlossCarRepairVo.getKindCode().trim())){
						kindASumMap.put(prpLDlossCarRepairVo.getKindCode().trim(),
								kindASumMap.get(prpLDlossCarRepairVo.getKindCode().trim()).add(prpLDlossCarRepairVo.getSumVeriLoss()));
					}else{
						kindASumMap.put(prpLDlossCarRepairVo.getKindCode().trim(),prpLDlossCarRepairVo.getSumVeriLoss());
					}
				}
			}

			// 此标志用于判断是否有主险，存在主险的施救费挂在任意主险下，无主险挂在任意附加险下
			boolean flag = false;
			for(Map.Entry<String,BigDecimal> entry:kindASumMap.entrySet()){
				PrpLLossItemVo prpLLossItemVo = new PrpLLossItemVo();
				if( !flag){
					if(entry.getKey().equals(CodeConstants.KINDCODE.KINDCODE_A)||entry.getKey().equals(CodeConstants.KINDCODE.KINDCODE_B)||entry
							.getKey().equals(CodeConstants.KINDCODE.KINDCODE_G)||entry.getKey().equals(CodeConstants.KINDCODE.KINDCODE_D11)||entry
							.getKey().equals(CodeConstants.KINDCODE.KINDCODE_D12)){
						prpLLossItemVo.setRescueFee(prpLDlossCarMainVo.getSumVeriRescueFee());
						flag = true;
					}
				}else{
					prpLLossItemVo.setRescueFee(BigDecimal.ZERO);
				}

				prpLLossItemVo.setItemId(prpLDlossCarMainVo.getSerialNo().toString());
				prpLLossItemVo.setLossType(prpLDlossCarMainVo.getDeflossCarType());
				prpLLossItemVo.setItemName(prpLDlossCarMainVo.getLicenseNo());
				prpLLossItemVo.setDlossId(prpLDlossCarMainVo.getId());
				prpLLossItemVo.setKindCode(entry.getKey());
				prpLLossItemVo.setRiskCode(prpLDlossCarMainVo.getRiskCode());
				prpLLossItemVo.setSumLoss(entry.getValue());
				if(subRogationFlag.equals(CodeConstants.subRogationFlagType.SUBROGA_CASE)){
					prpLLossItemVo.setPayFlag(CodeConstants.PayFlagType.INSTEAD_PAY);
				}else{
					prpLLossItemVo.setPayFlag(CodeConstants.PayFlagType.COMPENSATE_PAY);
				}
				if(itemKindVoList!=null&& !itemKindVoList.isEmpty()){
					for(PrpLCItemKindVo itemKindVo:itemKindVoList){
						if(itemKindVo.getKindCode().equals(entry.getKey())){
							prpLLossItemVo.setItemAmount(itemKindVo.getAmount());
							break;
						}
					}
				}
				// 标的车展示 实际价值
				if(prpLDlossCarMainVo.getSerialNo()==1){
					ThirdPartyDepreRate carDepreRate = this.queryThirdPartyDepreRate(registNo);
					prpLLossItemVo.setDepreMonthRate(new BigDecimal(carDepreRate.getDepreRate()));
					prpLLossItemVo.setStandardPrice(new BigDecimal(carDepreRate.getPurchasePrice()));
					prpLLossItemVo.setDepreMonths(new BigDecimal(carDepreRate.getUseMonths()));
				}

				rePrpLLossItemVo.add(prpLLossItemVo);
			}

			// 损失不涉及主险，此时施救费要挂在附加险下
			if( !flag){
				for(PrpLLossItemVo prpLLossItemVo:rePrpLLossItemVo){
					prpLLossItemVo.setRescueFee(prpLDlossCarMainVo.getSumVeriRescueFee());
				}
			}
		}

		return rePrpLLossItemVo;
	}

	/**
	 * 根据立案号、报案号查询计算书信息
	 * @param ClaimNo
	 * @param registNo
	 */
	public PrpLCompensateVo queryCompensate(String registNo,String claimNo) {
		// 一个立案号对应一个计算书号
		PrpLCompensateVo comVo = null;
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("registNo",registNo);
		qr.addEqual("claimNo",claimNo);// CompensateType为N表示正常理算书
		PrpLCompensate compPo = databaseDao.findUnique(PrpLCompensate.class,qr);
		if(compPo!=null){
			comVo = new PrpLCompensateVo();
			Beans.copyDepth().from(compPo).to(PrpLCompensateVo.class);
		}
		return comVo;
	}

	/**
	 * 获取车损险保额
	 * @param registNo
	 * @return
	 */
	public BigDecimal getAkindAmtByRegistNo(String registNo) {
		BigDecimal AkindAmt = new BigDecimal(0);
		List<PrpLCItemKindVo> cItemKindList = registQueryService.findCItemKindListByRegistNo(registNo);
		// TODO 旧条款取折旧价-需要计算公式
		// 新条款取保额
		for(PrpLCItemKindVo cItemKind:cItemKindList){
			if(cItemKind.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_A)||cItemKind.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_A1)){
				AkindAmt = cItemKind.getAmount();
				break;
			}else if(cItemKind.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_A1)){
				AkindAmt = cItemKind.getAmount();
				break;
			}
		}

		return AkindAmt;
	}

	/**
	 * 获取有效的人伤主表List
	 * @param registNo
	 * @param bcFlag
	 * @return
	 */
	public List<PrpLDlossPersTraceMainVo> getValidLossPersTraceMain(String registNo,String bcFlag) {
		List<PrpLDlossPersTraceMainVo> lossPersTraceMainVos = null;
		lossPersTraceMainVos = persTraceDubboService.findPersTraceMainVoList(registNo);
		// 获取有效的人伤跟踪任务主表PrpLDlossPersTraceMain-主表下有多条人伤跟踪任务PrpLDlossPersTrace
		List<PrpLDlossPersTraceMainVo> reLossPersTraceMainVos = new ArrayList<PrpLDlossPersTraceMainVo>();
		if(lossPersTraceMainVos!=null){
			for(PrpLDlossPersTraceMainVo lossPersTraceMainVo:lossPersTraceMainVos){
				if(checkLossState(lossPersTraceMainVo.getLossState(),bcFlag)){
					if(StringUtils.isNotBlank(lossPersTraceMainVo.getAuditStatus())&&lossPersTraceMainVo.getAuditStatus().equals(
							CodeConstants.AuditStatus.SUBMITCHARGE)){
						reLossPersTraceMainVos.add(lossPersTraceMainVo);
					}
				}
			}
		}
		return lossPersTraceMainVos;
	}

	/**
	 * 查询案件是否诉讼
	 * @param registNo
	 * @param bcFlag
	 * @return
	 */
	public String checkLawsuitStatus(String registNo,String bcFlag) {
		List<PrpLDlossPersTraceMainVo> lossPersTraceMainList = null;
		lossPersTraceMainList = getValidLossPersTraceMain(registNo,bcFlag);
		if(lossPersTraceMainList!=null){
			for(PrpLDlossPersTraceMainVo persTrace:lossPersTraceMainList){
				if(LAWFLAG_YES.equals(persTrace.getCaseProcessType())){
					return LAWFLAG_YES;
				}
			}
		}

		// 根据报案号查询单证表的是否诉讼状态 等待系统更新
		PrpLCertifyMainVo CertifyMain = certifyPubService.findPrpLCertifyMainVoByRegistNo(registNo);
		if(CertifyMain!=null){
			if(LAWFLAG_YES.equals(CertifyMain.getLawsuitFlag())){
				return LAWFLAG_YES;
			}
		}
		return LAWFLAG_NO;
	}

	/**
	 * 根据报案号查询并区分出预付的赔款和费用表 prePaidFlag 1 表示已核赔通过或注销的计算书，存在预付金额 TODO 冲销计算书默认是1，之后得调整
	 * @param registNo
	 * @return
	 * @throws Exception 
	 */
	public Map<String,List<PrpLPrePayVo>> getPrePayMap(String ClaimNo,String bcFlag,String prePaidFlag) throws Exception {
		Map<String,List<PrpLPrePayVo>> PrePayMap = new HashMap<String,List<PrpLPrePayVo>>();
		List<PrpLCompensateVo> prePayMainVos = findCompListByClaimNo(ClaimNo,"Y");
		List<PrpLCompensateVo> reprePayMainVos = new ArrayList<PrpLCompensateVo>();
		List<PrpLPrePayVo> rePprePayVos = new ArrayList<PrpLPrePayVo>();
		List<PrpLPrePayVo> reFprePayVos = new ArrayList<PrpLPrePayVo>();
		if(prePayMainVos!=null){
			// 剔除冲销的计算书
			for(PrpLCompensateVo vo:prePayMainVos){
				PrpLCompensateExtVo extVo = vo.getPrpLCompensateExt();
				if(CodeConstants.WRITEOFFFLAG.NORMAL.equals(extVo.getWriteOffFlag())
						&& !UnderWriteFlag.CANCELFLAG.equals(vo.getUnderwriteFlag())){
					List<PrpLCompensateVo> oppList = findByOpp(vo.getCompensateNo(),CompensateFlag.prePay);
					BigDecimal sumAmt =  BigDecimal.ZERO;
					if(oppList != null && !oppList.isEmpty()){

						for(PrpLCompensateVo compensateVo:oppList){
							sumAmt = sumAmt.add(compensateVo.getSumAmt());
						}
						sumAmt = sumAmt.multiply(new BigDecimal(-1));
					} 
					if(BigDecimal.ZERO.compareTo(vo.getSumAmt()) !=0 && sumAmt.compareTo(vo.getSumAmt()) == -1){
						if("1".equals(prePaidFlag)){
							reprePayMainVos.add(vo);
						}else{
							if( !"1".equals(vo.getPrpLCompensateExt().getIsCompDeduct())){
								reprePayMainVos.add(vo);
							}
						}
					}
				}
			
			}
		}

		// 根据计算书号查询出预付子表 区分预付子表是赔款、费用
		for(PrpLCompensateVo prePayMainVo:reprePayMainVos){
			List<PrpLPrePayVo> PprePays = new ArrayList<PrpLPrePayVo>();
			PprePays = compensateTaskService.getPrePayWriteVo(prePayMainVo.getCompensateNo(),"P");
			if(PprePays!=null && !PprePays.isEmpty()){
				for(PrpLPrePayVo tempPre:PprePays){
					rePprePayVos.add(tempPre);
				}
			}

			List<PrpLPrePayVo> FprePays = new ArrayList<PrpLPrePayVo>();
			FprePays = compensateTaskService.getPrePayWriteVo(prePayMainVo.getCompensateNo(),"F");
			if(FprePays!=null && !FprePays.isEmpty()){
				for(PrpLPrePayVo tempPre:FprePays){
					reFprePayVos.add(tempPre);
				}
			}
		}
		PrePayMap.put("prePay",rePprePayVos);
		PrePayMap.put("preFee",reFprePayVos);

		return PrePayMap;
	}

	/**
	 * <pre></pre>
	 * @param compensateNo
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年4月12日 下午2:39:15): <br>
	 */
	public  List<PrpLCompensateVo> findByOpp(String compensateNo,String compensateType) {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append("from PrpLCompensate com where 1=1 ");
		if(StringUtils.isNotBlank(compensateNo)){
			sqlUtil.append(" and com.prpLCompensateExt.oppoCompensateNo = ?  and com.prpLCompensateExt.writeOffFlag = ? ");
			sqlUtil.addParamValue(compensateNo);
			sqlUtil.addParamValue(WRITEOFFFLAG.ALLOFF);
		}
		if(StringUtils.isNotBlank(compensateType)){
			sqlUtil.append(" and com.compensateType = ?");
			sqlUtil.addParamValue(compensateType);
		}

		sqlUtil.append(" and ( com.underwriteFlag = ? ");
		sqlUtil.addParamValue(CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE);
		sqlUtil.append(" or  com.underwriteFlag = ? )");
		sqlUtil.addParamValue(CodeConstants.UnderWriteFlag.AUTO_UNDERWRITE);
	

		String sql = sqlUtil.getSql();
		logger.info("查询计算书号下的所有对冲计算书sql=" + sql);
		Object[] values = sqlUtil.getParamValues();
		List<PrpLCompensate> compensateList = databaseDao.findAllByHql(PrpLCompensate.class,sql,values);
		List<PrpLCompensateVo> compensateVoList = new ArrayList<PrpLCompensateVo>();
		if(compensateList!=null&&compensateList.size()>0){
			for(PrpLCompensate compensate:compensateList){
				PrpLCompensateVo compensateVo = new PrpLCompensateVo();
				compensateVo = Beans.copyDepth().from(compensate).to(PrpLCompensateVo.class);
				// Beans.copy().from(compensate).to(compensateVo);
				compensateVoList.add(compensateVo);
			}
		}
		return compensateVoList;
	}

	/**
	 * 根据报案号获取垫付表信息 prePaidFlag 1 表示已核赔通过或注销的计算书，存在预付金额 TODO 冲销计算书默认是1，之后得调整
	 * @param registNo
	 * @author willingRan
	 * @return
	 */
	public List<PrpLPadPayPersonVo> getPadPayPersons(String registNo,String prePaidFlag) {
		List<PrpLPadPayPersonVo> padPayPersons = new ArrayList<PrpLPadPayPersonVo>();
		List<PrpLPadPayMainVo> padPayMainVos = padPayService.findPadPayMainByRegistNo(registNo);
		List<PrpLPadPayMainVo> tempPadPayMainVos = new ArrayList<PrpLPadPayMainVo>();

		for(PrpLPadPayMainVo padPayMainVo:padPayMainVos){
			if(CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE.equals(padPayMainVo.getUnderwriteFlag())){// 核赔通过
				if("1".equals(prePaidFlag)){// 核赔通过的计算数据，如果存在垫付或预付则展示
					tempPadPayMainVos.add(padPayMainVo);
				}else{
					if( !"1".equals(padPayMainVo.getIsCompdeDuct())){// 未理算扣减
						tempPadPayMainVos.add(padPayMainVo);
					}
				}

			}
		}
		if(tempPadPayMainVos.size()>0){
			for(PrpLPadPayMainVo padPayMainVo:tempPadPayMainVos){
				for(PrpLPadPayPersonVo padPayPerson:padPayMainVo.getPrpLPadPayPersons()){
					padPayPersons.add(padPayPerson);
				}
			}
		}
		return padPayPersons;
	}

	/**
	 * <pre></pre>
	 * @param taskId 流程id
	 * @param claimNo 立案号
	 * @param compensateType 计算书类型
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月25日 上午11:38:45): <br>
	 */
	public PrpLCompensateVo rapidCompensate4Auto(Long taskId,String claimNo,String compensateKind) {
		PrpLCompensateVo prpLCompensateVo = null;
		PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(claimNo);
		logger.info("-->rapidCompensate4Auto - "+prpLClaimVo.getRegistNo());

		// *查询工作流。
		logger.info("-->查询工作流");
		PrpLWfTaskVo prpLWfTaskVo = taskHandleService.queryTask(Double.valueOf(taskId));
		// PrpLbpmMain prpLbpmMain = (PrpLbpmMain)Beans.deepClone(this.flowService.prpLbpmMain(taskId));
		// prpLbpmMain.setBusinessNo(prpLClaimVo.getClaimNo());
		// prpLbpmMain.setBusinessType(businessType);
		// prpLbpmMain.setMainNo(prpLClaimVo.getRegistNo());
		Assert.notNull(prpLWfTaskVo);
		logger.info("<--查询工作流");
		//
		// // *生成PrpLcompensate和CompensateVo。
		// logger.info("生成PrpLcompensate和CompensateVo。");
		// prpLCompensateVo = this.loadMockCompensate(prpLWfTaskVo,compensateKind);
		//
		// // 快速理赔需要对费用一并理算，但只理算公估费。
		// logger.info("快速理赔需要对费用一并理算，但只理算公估费。");
		// prpLCompensateVo.setPrpLCharges(this.queryReadyPrpLcharge(prpLCompensateVo.getRegistNo()));

		logger.info("-->数据转换");
		CompensateVo compensateVo = this.convertPrpLcompensate2CompensateVo(prpLCompensateVo,prpLWfTaskVo);

		if( !( CodeConstants.CompensateKind.BI_COMPENSATE_CHARGE.equals(compensateKind) )&& !( CodeConstants.CompensateKind.CI_COMPENSATE_CHARGE
				.equals(compensateKind) )){
			// *获取免赔率。
			logger.info("获取免赔率");
			compensateVo = this.executeRulesDeductRate(compensateVo,prpLClaimVo);

			// *商业险试算。
			logger.info("商业险试算");
			compensateVo = this.orgnizeBicompensateData(compensateVo,prpLClaimVo);
		}

		// *默认取第一个人作为赔款的领款人。快速理赔案件中由领款人模块控制只有一个领款人。
		logger.info("默认取第一个人作为赔款的领款人。快速理赔案件中由领款人模块控制只有一个领款人。");
		// PrpLPaymentVo prpLPaymentVo = new PrpLPaymentVo();
		// prpLpayeePersonQuery.setRegistNo(prpLWfTaskVo.getRegistNo());
		// prpLpayeePersonQuery.setValidFlag("1");

		// *金额拆分。只针对实赔理算项进行拆分，公估费在理算保存的时候自动拆分。
		// logger.info("金额拆分。只针对实赔理算项进行拆分，公估费在理算保存的时候自动拆分。");
		// if(CodeConstants.CompensateKind.BI_COMPENSATE_CHARGE.equals(compensateKind) ||
		// CodeConstants.CompensateKind.CI_COMPENSATE_CHARGE.equals(compensateKind)){
		// prpLpayeePersonQuery.setPayeetypecode(CodeConstants.PayeeType.SUP03.value);
		// List<PrpLpayeePerson> prpLpayPersonList = this.payeePersonService.queryByExample(prpLpayeePersonQuery);
		// logger.info("prpLpayPersonList.size() = "+prpLpayPersonList.size());
		// for(PrpLpayeePerson payeePerson:prpLpayPersonList){
		// payeePersonService.save4RapidCompensate(prpLCompensateVo,payeePerson);
		// }
		// }else{
		// prpLpayeePersonQuery.setPayeetypecode(CodeConstants.PayeeType.SUP99.value);
		// List<PrpLpayeePerson> prpLpayPersonList = this.payeePersonService.queryByExample(prpLpayeePersonQuery);
		// PrpLpayeePerson prpLpayeePerson = prpLpayPersonList.get(0);
		// payeePersonService.save4RapidCompensate(prpLCompensateVo,prpLpayeePerson);
		// }

		logger.info("-->rapidCompensate4Auto - "+prpLClaimVo.getRegistNo());
		return prpLCompensateVo;
	}

	/**
	 * 根据工作流表初始化PrpLCompensateVo信息
	 * 
	 * <pre></pre>
	 * @param prpLWfTaskVo
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月25日 上午11:47:57): <br>
	 */
	public PrpLCompensateVo loadMockCompensate(PrpLWfTaskVo prpLWfTaskVo,String compensateType) {

		// 在生成理算业务数据前PrpLbpmMain.businessNo是立案号。
		PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLWfTaskVo.getClaimNo());
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(prpLClaimVo.getRegistNo());

		PrpLCompensateVo prpLCompensateVo = new PrpLCompensateVo();
		// 从立案复制同名属性数据到理算
		Datas.copySimpleObjectToTargetFromSource(prpLCompensateVo,prpLClaimVo,true);
		// 理算类型取自工作流中业务类型的转换值。
		prpLCompensateVo.setCompensateType(compensateType);

		// 取保单的classcode、riskcode作为计算书的classcode、riskcode。
		// PrpLcMain prpLcMain = this.opusService.queryPrpLcMainByRegistNoAndPolicyNo(prpLclaim.getRegistNo(),prpLclaim.getPolicyNo());
		prpLCompensateVo.setRiskCode(prpLWfTaskVo.getRiskCode());
		prpLCompensateVo.setMakeCom(prpLWfTaskVo.getComCode());
		// 对计算书中的商业险责任比例赋值。
		this.fillIndemnityDuty(prpLCompensateVo);

		// 默认非通融赔付。
		prpLCompensateVo.setAllowFlag("0");
		// 追偿需要程序判断计算书的类型。
		prpLCompensateVo.setRecoveryFlag(this.isCompulsoryReplevy(prpLCompensateVo) ? "1" : "0");
		// 赔案类别取自报案赔案类别。
		// prpLCompensateVo.setClaimType(prpLRegistVo.getClaimType());
		// 理算诉讼案标志取自立案。
		// prpLCompensateVo.setIsSuitFlag(prpLClaimVo.getLawsuitflag());
		// 币种取保单的币种。
		prpLCompensateVo.setCurrency("CNY");
		// 计算书的交强险已赔付总金额默认为0。界面中点获取交强赔付金额时获取。
		prpLCompensateVo.setSumBzPaid(BigDecimal.ZERO);
		// 计算书的协议金额默认为0。
		prpLCompensateVo.setAgreeAmt(BigDecimal.ZERO);
		// 计算书的合计费用默认为0。
		prpLCompensateVo.setSumFee(BigDecimal.ZERO);
		// 计算书的责任赔款合计默认为0。
		prpLCompensateVo.setSumAmt(BigDecimal.ZERO);
		// 计算书的标的损失金额默认为0。
		prpLCompensateVo.setSumLoss(BigDecimal.ZERO);
		// 计算书的合计费用默认为0。
		// prpLCompensateVo.setSumNoDutyFee(BigDecimal.ZERO);
		// 计算书的不计免赔金额总计默认为0。
		// prpLCompensateVo.setSumNoDeductFee(BigDecimal.ZERO);
		// 计算书的本次赔付金额默认为0。
		prpLCompensateVo.setSumPaidAmt(BigDecimal.ZERO);
		// 计算书的损余金额默认为0。
		// prpLCompensateVo.setSumremnantfee(BigDecimal.ZERO);
		// 计算书的赔款初始为0
		prpLCompensateVo.setSumRealPay(BigDecimal.ZERO);
		// 预付金额初始化为0
		prpLCompensateVo.setSumPreAmt(BigDecimal.ZERO);
		// 预付费用初始化为0
		prpLCompensateVo.setSumPreFee(BigDecimal.ZERO);
		// 实赔计算书下才整理车财人的理算项目
		if(this.isActualOrExpenseCompensate(prpLCompensateVo.getCompensateKind())){

			// 整理待理算车辆信息。
			prpLCompensateVo.setPrpLLossItems(this.queryReadyPrpLLossItemVoList(prpLCompensateVo));
			// 整理待理算财产信息。
			prpLCompensateVo.setPrpLLossProps(this.queryReadyPrpLpropMains(prpLCompensateVo));
			// 整理待理算人伤信息。
			prpLCompensateVo.setPrpLLossPersons(this.queryReadyPrpLpersonItems(prpLCompensateVo));

			this.fill4CompulsoryPrpLcompensate(prpLCompensateVo);
		}else{
			// 费用计算书需要整理未理算的费用数据。
			prpLCompensateVo.setPrpLCharges(this.queryReadyPrpLcharge(prpLCompensateVo.getRegistNo()));
		}

		// 计算预赔金额。
		this.fillPrePaid(prpLCompensateVo);

		return prpLCompensateVo;
	}

	/**
	 * 获取计算书的责任比例。 如果已有同类型计算书，从同类型计算书获取，否则从查勘获取。
	 * @param prpLcompensate
	 */
	private void fillIndemnityDuty(PrpLCompensateVo prpLCompensateVo) {
		if(this.isActualOrExpenseCompensate(prpLCompensateVo.getCompensateKind())){
			List<PrpLCompensateVo> prpLCompensateVoList = this.queryMatchedTypeCompensate(prpLCompensateVo.getRegistNo(),
					prpLCompensateVo.getCompensateKind());

			if(prpLCompensateVoList.isEmpty()){
				// 从主车承保信息中初始化理算的责任和责任比例。
				List<PrpLDlossCarMainVo> prpLDlossCarMainList = lossCarService.findLossCarMainByRegistNo(prpLCompensateVo.getRegistNo());
				PrpLDlossCarMainVo mainCar = null;
				for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainList){
					if(prpLDlossCarMainVo.getSerialNo()==1){
						mainCar = prpLDlossCarMainVo;
					}
				}
				prpLCompensateVo.setIndemnityDuty(mainCar.getIndemnityDuty());
				prpLCompensateVo.setIndemnityDutyRate(mainCar.getIndemnityDutyRate());
			}else{
				PrpLCompensateVo existedprpLCompensateVo = prpLCompensateVoList.get(0);
				prpLCompensateVo.setIndemnityDuty(existedprpLCompensateVo.getIndemnityDuty());
				prpLCompensateVo.setIndemnityDutyRate(existedprpLCompensateVo.getIndemnityDutyRate());
			}
		}
	}

	/**
	 * 判断计算书是实赔计算书还是费用计算书。true表示实赔计算书，false表示费用计算书。
	 * @param compensateType
	 * @return
	 */
	public boolean isActualOrExpenseCompensate(String compensateType) {
		Assert.notNull(compensateType);

		boolean isActualOrExpense;
		if(CodeConstants.CompensateKind.BI_COMPENSATE.equals(compensateType)||CodeConstants.CompensateKind.CI_COMPENSATE.equals(compensateType)||CodeConstants.CompensateKind.ADVANCE
				.equals(compensateType)){
			isActualOrExpense = true;
			return isActualOrExpense;
		}

		if(CodeConstants.CompensateKind.BI_COMPENSATE_CHARGE.equals(compensateType)){
			isActualOrExpense = false;
			return isActualOrExpense;
		}

		Assert.isTrue(false);
		return true;
	}

	/**
	 * 查找计算书类型匹配的值
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @param compensateType
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月25日 下午2:54:04): <br>
	 */
	public List<PrpLCompensateVo> queryMatchedTypeCompensate(String registNo,String compensateType) {
		List<PrpLCompensateVo> prpLCompensateVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("compensateType",compensateType);
		queryRule.addIn("underwriteFlag",CodeConstants.UnderWriteFlag.NORMAL,CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE,
				CodeConstants.UnderWriteFlag.BACK_UNDERWRITE,CodeConstants.UnderWriteFlag.AUTO_UNDERWRITE,
				CodeConstants.UnderWriteFlag.WAIT_UNDERWRITE);

		queryRule.addAscOrder("createTime");

		List<PrpLCompensate> prpLCompensateList = databaseDao.findAll(PrpLCompensate.class,queryRule);
		if(prpLCompensateList!=null&& !prpLCompensateList.isEmpty()){
			prpLCompensateVoList = Beans.copyDepth().from(prpLCompensateList).toList(PrpLCompensateVo.class);
		}
		return prpLCompensateVoList;
	}

	/**
	 * 判断是商业或交强计算书。商业计算书返回true，交强计算书返回false。
	 * @param compensateType
	 * @return
	 */
	public boolean isCommercialOrCompulsory(String compensateType) {

		Assert.notNull(compensateType);

		boolean isCommercialOrCompulsory;

		if(CodeConstants.CompensateKind.BI_COMPENSATE.equals(compensateType)||CodeConstants.CompensateKind.BI_COMPENSATE_CHARGE
				.equals(compensateType)){
			isCommercialOrCompulsory = true;
			return isCommercialOrCompulsory;
		}
		if(CodeConstants.CompensateKind.CI_COMPENSATE.equals(compensateType)||CodeConstants.CompensateKind.ADVANCE.equals(compensateType)){
			isCommercialOrCompulsory = false;
			return isCommercialOrCompulsory;
		}

		Assert.isTrue(false);
		return true;
	}

	/**
	 * 是否强制追偿。 1.代位求偿的商业险，第一张赔款计算书，默认为追偿。
	 * @param prpLcompensate
	 * @return
	 */
	public boolean isCompulsoryReplevy(PrpLCompensateVo prpLCompensateVo) {
		// 判断本计算书是否第一个有效商业险赔款计算书。
		boolean isFirstCommercialActualPayment = true;
		List<PrpLCompensateVo> prpLCompensateVoList = this.queryMatchedTypeCompensate(prpLCompensateVo.getRegistNo(),
				CodeConstants.CompensateKind.BI_COMPENSATE);
		for(PrpLCompensateVo existedPrpLcompensate:prpLCompensateVoList){
			if( !existedPrpLcompensate.getCompensateNo().equals(prpLCompensateVo.getCompensateNo())){
				isFirstCommercialActualPayment = false;
				break;
			}
		}

		// 是否代位求偿。
		boolean isSubstituteCase = false;
		PrpLSubrogationMainVo prpLSubrogationMainVo = subrogationService.find(prpLCompensateVo.getRegistNo());
		if(prpLSubrogationMainVo!=null&&prpLSubrogationMainVo.getSubrogationFlag()!=null){
			if("1".equals(prpLSubrogationMainVo.getSubrogationFlag())){
				isSubstituteCase = true;
			}
		}

		return isSubstituteCase&&isFirstCommercialActualPayment&&this.isActualOrExpenseCompensate(prpLCompensateVo.getCompensateKind())&&this
				.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind());
	}

	/**
	 * 查询给定的计算书在当前可以理算的所有车辆信息
	 * 
	 * <pre></pre>
	 * @param prpLCompensateVo
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月25日 下午3:57:14): <br>
	 */
	private List<PrpLLossItemVo> queryReadyPrpLLossItemVoList(PrpLCompensateVo prpLCompensateVo) {
		List<PrpLLossItemVo> prpLLossItemVo = null;

		// 车辆定损中的理算状态条件。
		List<String> lossStateCondition = this.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind()) ? Arrays.asList(
				CodeConstants.LossState.UN_COMPENSATE,CodeConstants.LossState.CI_COMPENSATE) : Arrays.asList(CodeConstants.LossState.UN_COMPENSATE,
				CodeConstants.LossState.CI_COMPENSATE,CodeConstants.LossState.BI_COMPENSATE);

		// 车辆定损中的核损状态条件。
		List<String> underWriteFlagCondition = Arrays.asList(CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE,
				CodeConstants.UnderWriteFlag.AUTO_UNDERWRITE);

		// 当前所有可理算车辆。
		String serialNo = null;
		// 交强险。
		if( !this.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind())){
			// 交强险互碰自赔案件，带出主车车损。
			if(prpLCompensateVo.getCaseType().equals(CodeConstants.ClaimType.SPAY_CASE)){
				serialNo = "1";
			}
		}

		List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findPrpLDlossCarMainVoListByCondition(prpLCompensateVo.getRegistNo(),
				lossStateCondition,underWriteFlagCondition,serialNo);
		// 返回的车辆理算结果。
		List<PrpLLossItemVo> prpLLossItemVoList = new ArrayList<PrpLLossItemVo>();
		// 是否代位求偿。
		boolean isSubstituteCase = false;
		PrpLSubrogationMainVo prpLSubrogationMainVo = subrogationService.find(prpLCompensateVo.getRegistNo());
		if(prpLSubrogationMainVo!=null&&prpLSubrogationMainVo.getSubrogationFlag()!=null){
			if("1".equals(prpLSubrogationMainVo.getSubrogationFlag())){
				isSubstituteCase = true;
			}
		}
		// 本计算书是否代位求偿下第一张商业险计算书。
		boolean isZcCompensate = this.isZcCompensate(prpLCompensateVo);

		// 交强、商业非代位求偿。
		if( !isSubstituteCase||isZcCompensate){
			for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){
				List<PrpLLossItemVo> prpLlossList4PrpLdefLossMainVo = new ArrayList<PrpLLossItemVo>();
				// 推定全损定损下，理算金额由车辆定损确定，险别由车辆定损中的损失类型确定。
				String cetainLossType = prpLDlossCarMainVo.getCetainLossType();
				if(cetainLossType.equals(CodeConstants.CetainLossType.DEFLOSS_ALL)){
					// 推定全损默认是车损险
					String kindCode = CodeConstants.KINDCODE.KINDCODE_A;
					PrpLCItemKindVo prpLCItemKindVo = this.queryPrpLcItemKind4KindCode(kindCode,prpLCompensateVo);
					PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(prpLCompensateVo.getRegistNo(),prpLCompensateVo.getPolicyNo());
					// 查询损失项对应的险别信息。
					boolean flag = policyViewService.isInsuredKindCode(prpLCMainVo,kindCode);
					// 如果没有对应的险别信息，该损失项不带入理算。
					if(flag){
						PrpLPlatLockVo prpLPlatLockVo = null;
						// 在一般案件下，有可能对方保险公司做了代位求偿，我方需要对三者车做清付处理。
						if(prpLDlossCarMainVo.getSerialNo()!=1){
							String policyType = null;
							if(this.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind())){
								policyType = CodeConstants.PolicyType.POLICY_DAA;
							}else{
								policyType = CodeConstants.PolicyType.POLICY_DZA;
							}
							List<PrpLPlatLockVo> prpLPlatLockVoList = subrogationService.findByPrpLPlatLockVo("2",prpLDlossCarMainVo.getRegistNo(),
									prpLDlossCarMainVo.getLicenseNo(),policyType);
							if(prpLPlatLockVoList!=null){
								prpLPlatLockVo = prpLPlatLockVoList.get(0);
							}
						}

						PrpLLossItemVo lossItem = this.makePrpLlossByDefLoss(prpLDlossCarMainVo,prpLCItemKindVo,null,prpLPlatLockVo);
						prpLlossList4PrpLdefLossMainVo.add(lossItem);
					}
				}else{
					// 修改定损下。
					Map<String,BigDecimal> kindCodeFee = this.sumKindCodeFee(prpLDlossCarMainVo,prpLCompensateVo);

					// 主车，按损失项分项汇总。
					if(prpLDlossCarMainVo.getSerialNo()==1){
						prpLlossList4PrpLdefLossMainVo.addAll(this.makePrpLloss4KindCode(kindCodeFee,prpLCompensateVo,prpLDlossCarMainVo,null,
								new ArrayList<String>()));
					}else{
						// 三者车，如果有保三者险，全部归入三者险，否则不带出理算项。
						// 在一般案件下，有可能对方保险公司做了代位求偿，我方需要对三者车做清付处理。
						String policyType = null;
						if(this.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind())){
							policyType = CodeConstants.PolicyType.POLICY_DAA;
						}else{
							policyType = CodeConstants.PolicyType.POLICY_DZA;
						}
						List<PrpLPlatLockVo> prpLPlatLockVoList = subrogationService.findByPrpLPlatLockVo("2",prpLDlossCarMainVo.getRegistNo(),
								prpLDlossCarMainVo.getLicenseNo(),policyType);
						PrpLPlatLockVo prpLPlatLockVo = null;
						if(prpLPlatLockVoList!=null){
							prpLPlatLockVo = prpLPlatLockVoList.get(0);
						}
						prpLlossList4PrpLdefLossMainVo.addAll(this.makePrpLloss4KindCode(kindCodeFee,prpLCompensateVo,prpLDlossCarMainVo,
								prpLPlatLockVo,new ArrayList<String>()));
					}
				}

				this.fillRescueFee4Car(prpLCompensateVo,prpLlossList4PrpLdefLossMainVo,prpLDlossCarMainVo);
				prpLLossItemVoList.addAll(prpLlossList4PrpLdefLossMainVo);
			}
		}

		// 代位求偿案件（有代位求偿标志）。
		// 只在第一张商业险赔款计算书才做代位求偿，后续商业险赔款计算书按一般商业险赔款计算书处理。
		if(isZcCompensate&&this.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind())){
			logger.info("只在第一张商业险赔款计算书才做代位求偿，后续商业险赔款计算书按一般商业险赔款计算书处理");
			// 主车定损任务。
			List<PrpLDlossCarMainVo> mainCarDefLossList = new ArrayList<PrpLDlossCarMainVo>();
			// 三者车定损任务。
			List<PrpLDlossCarMainVo> thirdCarDefLossList = new ArrayList<PrpLDlossCarMainVo>();
			// 分类主车、三者车定损任务。
			for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){

				if(prpLDlossCarMainVo.getSerialNo()==1){
					mainCarDefLossList.add(prpLDlossCarMainVo);
				}else{
					thirdCarDefLossList.add(prpLDlossCarMainVo);
				}
			}

			// 自付：主车理算记录=主车定损记录。
			for(PrpLDlossCarMainVo prpLdefLossMainVo:mainCarDefLossList){
				List<PrpLLossItemVo> prpLlossList4PrpLdefLossMain = new ArrayList<PrpLLossItemVo>();

				// 自付部分没有锁定记录。
				PrpLPlatLockVo prpLPlatLockVo = null;

				// 推定全损定损下，理算金额由车辆定损确定，险别默认为车损险
				String cetainLossType = prpLdefLossMainVo.getCetainLossType();
				if(CodeConstants.CetainLossType.DEFLOSS_ALL.equals(cetainLossType)){
					// 查询损失项对应的险别信息。
					String kindCode = CodeConstants.KINDCODE.KINDCODE_A;
					PrpLCItemKindVo prpLCItemKindVo = this.queryPrpLcItemKind4KindCode(kindCode,prpLCompensateVo);
					// 如果没有对应的险别信息，该损失项不带入理算。
					if(prpLCItemKindVo!=null){
						PrpLLossItemVo lossItemVo = this.makePrpLlossByDefLoss(prpLdefLossMainVo,prpLCItemKindVo,null,prpLPlatLockVo);
						prpLlossList4PrpLdefLossMain.add(lossItemVo);
					}
				}else{
					// 修改定损下，险别、理算金额由各损失项按换件、修复、辅料记录汇总确定。
					Map<String,BigDecimal> kindCodeFee = this.sumKindCodeFee(prpLdefLossMainVo,prpLCompensateVo);
					prpLlossList4PrpLdefLossMain.addAll(this.makePrpLloss4KindCode(kindCodeFee,prpLCompensateVo,prpLdefLossMainVo,prpLPlatLockVo,
							new ArrayList<String>()));
				}

				this.fillRescueFee4Car(prpLCompensateVo,prpLlossList4PrpLdefLossMain,prpLdefLossMainVo);
				prpLLossItemVoList.addAll(prpLlossList4PrpLdefLossMain);
			}

			// 代付：主车理算代位求偿记录=主车定损记录*LOCK_L的数量。
			List<PrpLPlatLockVo> prpLPlatLockVoList = subrogationService.findPrpLPlatLockVoByRegistNo(prpLCompensateVo.getRegistNo(),
					CodeConstants.PayFlagType.INSTEAD_PAY);
			for(PrpLDlossCarMainVo prpLdefLossMainVo:mainCarDefLossList){
				for(PrpLPlatLockVo prpLPlatLockVo:prpLPlatLockVoList){
					List<PrpLLossItemVo> prpLlossList4PrpLdefLossMain = new ArrayList<PrpLLossItemVo>();

					// 推定全损定损下，理算金额由车辆定损确定，险别默认为车损险。
					String cetainLossType = prpLdefLossMainVo.getCetainLossType();
					if(cetainLossType.equals(CodeConstants.CetainLossType.DEFLOSS_ALL)){
						// 查询损失项对应的险别信息。
						String kindCode = CodeConstants.KINDCODE.KINDCODE_A;
						PrpLCItemKindVo prpLCItemKindVo = this.queryPrpLcItemKind4KindCode(kindCode,prpLCompensateVo);
						// 代位求偿情况下，只带入车损险损失项。
						if(prpLCItemKindVo!=null){
							PrpLLossItemVo lossItem = this.makePrpLlossByDefLoss(prpLdefLossMainVo,prpLCItemKindVo,null,prpLPlatLockVo);
							prpLlossList4PrpLdefLossMain.add(lossItem);
						}
					}else{
						// 修改定损下，险别、理算金额由各损失项按换件、修复、辅料记录汇总确定。
						Map<String,BigDecimal> kindCodeFee = this.sumKindCodeFee(prpLdefLossMainVo,prpLCompensateVo);
						prpLlossList4PrpLdefLossMain.addAll(this.makePrpLloss4KindCode(kindCodeFee,prpLCompensateVo,prpLdefLossMainVo,prpLPlatLockVo,
								Arrays.asList(new String[]{CodeConstants.KINDCODE.KINDCODE_A})));
					}
					this.fillRescueFee4Car(prpLCompensateVo,prpLlossList4PrpLdefLossMain,prpLdefLossMainVo);
					prpLLossItemVoList.addAll(prpLlossList4PrpLdefLossMain);
				}
			}

			// 清付：三者车理算代位求偿记录=三者车定损记录的数量。
			for(PrpLDlossCarMainVo prpLdefLossMainVo:thirdCarDefLossList){
				List<PrpLLossItemVo> prpLlossList4PrpLdefLossMain = new ArrayList<PrpLLossItemVo>();

				// 商业代位求偿案件，三者车的损失项只能是清付，所以查询三者车的被锁记录。
				PrpLPlatLockVo prpLPlatLockVo = null;
				List<PrpLPlatLockVo> prpLPlatLockList = subrogationService.findByPrpLPlatLockVo("2",prpLdefLossMainVo.getRegistNo(),
						prpLdefLossMainVo.getLicenseNo(),CodeConstants.PolicyType.POLICY_DAA);

				if(prpLPlatLockList!=null){
					prpLPlatLockVo = prpLPlatLockList.get(0);
				}

				// 推定全损定损下，理算金额由车辆定损确定，险别默认为车损险。
				final String cetainLossType = prpLdefLossMainVo.getCetainLossType();
				if(cetainLossType.equals(CodeConstants.CetainLossType.DEFLOSS_ALL)){
					// 查询损失项对应的险别信息。
					String kindCode = CodeConstants.KINDCODE.KINDCODE_A;
					PrpLCItemKindVo prpLCItemKindVo = this.queryPrpLcItemKind4KindCode(kindCode,prpLCompensateVo);

					if(prpLCItemKindVo!=null){
						PrpLLossItemVo lossItem = this.makePrpLlossByDefLoss(prpLdefLossMainVo,prpLCItemKindVo,null,prpLPlatLockVo);
						prpLlossList4PrpLdefLossMain.add(lossItem);
					}
				}else{
					// 修改定损下，险别、理算金额由各损失项按换件、修复、辅料记录汇总确定。
					Map<String,BigDecimal> kindCodeFee = this.sumKindCodeFee(prpLdefLossMainVo,prpLCompensateVo);
					prpLlossList4PrpLdefLossMain.addAll(this.makePrpLloss4KindCode(kindCodeFee,prpLCompensateVo,prpLdefLossMainVo,prpLPlatLockVo,
							new ArrayList<String>()));
				}
				this.fillRescueFee4Car(prpLCompensateVo,prpLlossList4PrpLdefLossMain,prpLdefLossMainVo);
				prpLLossItemVoList.addAll(prpLlossList4PrpLdefLossMain);
			}
		}

		// 删除损失+施救费等于0的理算项。
		// zhongyuhai 20121121
		// Iterator<PrpLLossItemVo> it = prpLLossItemVoList.iterator();
		// while(it.hasNext()){
		// PrpLLossItemVo prpLossItemVo = it.next();
		// if(prpLossItemVo.getSumLoss().add(prpLossItemVo.getRescueFee()).compareTo(BigDecimal.ZERO) == 0){
		// // it.remove();/恢复o理算项的损失项，如用于：用于冲减残值回收的赔款 2013-3-28
		// }
		// }
		return prpLLossItemVo;
	}

	/**
	 * 查询给定的计算书在当前可以理算的所有财产信息。
	 * @param prpLcompensate
	 * @author zhongyuhai
	 * @return
	 */
	private List<PrpLLossPropVo> queryReadyPrpLpropMains(PrpLCompensateVo prpLCompensateVo) {

		// 财产定损中的理算状态条件。
		List<String> lossStateCondition = this.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind()) ? Arrays.asList(
				CodeConstants.LossState.UN_COMPENSATE,CodeConstants.LossState.CI_COMPENSATE) : Arrays.asList(CodeConstants.LossState.UN_COMPENSATE,
				CodeConstants.LossState.CI_COMPENSATE,CodeConstants.LossState.BI_COMPENSATE);

		// 物损定损中的核损状态条件。
		List<String> underWriteFlagCondition = Arrays.asList(CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE,
				CodeConstants.UnderWriteFlag.AUTO_UNDERWRITE);

		String serialNo = null;
		// 交强险。
		if( !this.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind())){
			// 交强险互碰自赔案件，带出主车车损。
			if(prpLCompensateVo.getCaseType().equals(CodeConstants.ClaimType.SPAY_CASE)){
				serialNo = "1";
			}
		}
		// 当前所有可理算财产。
		List<PrpLdlossPropMainVo> prpLdlossPropMainVoList = propTaskService.findPrpLdlossPropMainVoListByCondition(prpLCompensateVo.getRegistNo(),
				lossStateCondition,underWriteFlagCondition,serialNo);

		List<PrpLLossPropVo> prpLLossPropVoList = new ArrayList<PrpLLossPropVo>();

		for(PrpLdlossPropMainVo prpLdlossPropMainVo:prpLdlossPropMainVoList){
			List<PrpLLossPropVo> prpLproplossList4PrpLpropMain = new ArrayList<PrpLLossPropVo>();
			// 分险别汇总费用。
			Map<String,BigDecimal> kindCodeFee = this.sumKindCodeFee(prpLdlossPropMainVo,prpLCompensateVo);

			Set<String> kindCodeSet = kindCodeFee.keySet();
			for(String kindCode:kindCodeSet){
				// 查询损失项对应的险别信息。
				PrpLCItemKindVo prpLcItemKindVo = this.queryPrpLcItemKind4KindCode(kindCode,prpLCompensateVo);
				if(prpLcItemKindVo!=null){
					PrpLLossPropVo prpLLossPropVo = new PrpLLossPropVo();

					// 从定损财物复制同名属性数据到理算财物中。
					Datas.copySimpleObjectToTargetFromSource(prpLLossPropVo,prpLdlossPropMainVo,true);
					// 财损id。
					prpLLossPropVo.setDlossId(prpLdlossPropMainVo.getId());
					// 施救费默认为0，由后续方法填充。
					prpLLossPropVo.setRescueFee(BigDecimal.ZERO);
					// 财产损失金额去核损金额。
					prpLLossPropVo.setSumLoss(kindCodeFee.get(kindCode));
					prpLLossPropVo.setId(null);

					// 币别从保单获取。
					prpLLossPropVo.setCurrency("CNY");
					// 险别序号从险别信息中获取。
					// prpLLossPropVo.setItemKindNo(prpLcItemKindVo.getItemKindNo());
					// 险别代码从险别信息中获取。
					prpLLossPropVo.setKindCode(prpLcItemKindVo.getKindCode());
					// 免赔率从险别记录中获取。
					prpLLossPropVo.setDeductAbsRate(prpLcItemKindVo.getDeductibleRate());
					// 免赔额从险别记录中获取。
					prpLLossPropVo.setDeductAbsAmt(prpLcItemKindVo.getDeductible());

					// 施救费交强赔付金额默认为0，在界面中通过获取交强险赔款来获取。
					prpLLossPropVo.setBzPaidRescueFee(BigDecimal.ZERO);
					// 交强险已赔付金额默认为0，在界面中通过获取交强险赔款来获取。
					prpLLossPropVo.setBzPaidLoss(BigDecimal.ZERO);
					// 可选免赔率/其它免赔率/绝对免赔率默认为0。
					prpLLossPropVo.setDeductAddRate(BigDecimal.ZERO);
					// 计入赔款金额默认为0。
					prpLLossPropVo.setSumRealPay(BigDecimal.ZERO);
					// 理算项预赔金额默认为0。
					prpLLossPropVo.setOffPreAmt(BigDecimal.ZERO);
					// 其他扣除金额默认为0。
					// prpLLossPropVo.setOtherDeductFee(BigDecimal.ZERO);
					// 财损没有标的价值。
					prpLLossPropVo.setItemValue(null);
					// 不计免赔率默认为0。
					prpLLossPropVo.setDeductOffRate(BigDecimal.ZERO);
					// 不计免赔金额默认为0。
					prpLLossPropVo.setDeductOffAmt(BigDecimal.ZERO);
					// 赔付比例默认为0。
					prpLLossPropVo.setClaimRate(BigDecimal.ZERO);
					// 残值费用从财损核损残值取。。
					// prpLLossPropVo.setRemnantfee(prpLpropMain.getSumveriremnantfee());

					// 设置车辆信息。判断非空是财损有可能是路面财损。
					prpLLossPropVo.setItemId(prpLdlossPropMainVo.getSerialNo().toString());
					List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(prpLCompensateVo.getRegistNo());
					String licenseNo = null;
					if(prpLDlossCarMainVoList!=null&& !prpLDlossCarMainVoList.isEmpty()){
						for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){
							if(prpLdlossPropMainVo.getSerialNo().equals(prpLDlossCarMainVo.getSerialNo())){
								licenseNo = prpLDlossCarMainVo.getLicenseNo();
								break;
							}
						}
					}
					if(prpLdlossPropMainVo.getSerialNo()==0){
						prpLLossPropVo.setItemName("地面财产");
					}else{
						prpLLossPropVo.setItemName(licenseNo);
					}

					prpLproplossList4PrpLpropMain.add(prpLLossPropVo);
				}
			}
			this.fillRescueFee4Prop(prpLCompensateVo,prpLproplossList4PrpLpropMain,prpLdlossPropMainVo);
			prpLLossPropVoList.addAll(prpLproplossList4PrpLpropMain);
		}

		// 删除损失+施救费等于0的理算项。
		// zhongyuhai 20121121
		Iterator<PrpLLossPropVo> it = prpLLossPropVoList.iterator();
		while(it.hasNext()){
			PrpLLossPropVo prpLpropLoss = it.next();
			if(prpLpropLoss.getSumLoss().add(prpLpropLoss.getRescueFee()).compareTo(BigDecimal.ZERO)==0){
				it.remove();
			}
		}

		return prpLLossPropVoList;
	}

	/**
	 * 查询给定的计算书在当前可以理算的所有人伤信息。
	 * @param prpLcompensate
	 * @author zhongyuhai
	 * @return
	 */
	private List<PrpLLossPersonVo> queryReadyPrpLpersonItems(PrpLCompensateVo prpLCompensateVo) {

		// 人伤定损中的理算状态条件。
		List<String> lossStateCondition = this.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind()) ? Arrays.asList(
				CodeConstants.LossState.UN_COMPENSATE,CodeConstants.LossState.CI_COMPENSATE) : Arrays.asList(CodeConstants.LossState.UN_COMPENSATE,
				CodeConstants.LossState.CI_COMPENSATE,CodeConstants.LossState.BI_COMPENSATE);

		PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo = null;
		List<PrpLDlossPersTraceVo> prpLDlossPersTraceVoList = null;
		// 交强险。
		if(this.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind())){
			prpLDlossPersTraceMainVo = persTraceDubboService.findPersTraceMainVoByCondition(prpLCompensateVo.getRegistNo(),lossStateCondition,"1");
		}

		if(prpLDlossPersTraceMainVo!=null&&prpLDlossPersTraceMainVo.getPrpLDlossPersTraces()!=null){
			prpLDlossPersTraceVoList = prpLDlossPersTraceMainVo.getPrpLDlossPersTraces();
		}
		List<PrpLLossPersonVo> prpLLossPersonVoList = new ArrayList<PrpLLossPersonVo>();

		for(PrpLDlossPersTraceVo prpLDlossPersTraceVo:prpLDlossPersTraceVoList){
			PrpLLossPersonVo prpLLossPersonVo = new PrpLLossPersonVo();
			// 从定损人伤复制同名属性数据到理算人伤中。
			Datas.copySimpleObjectToTargetFromSource(prpLLossPersonVo,prpLDlossPersTraceVo,true);
			prpLLossPersonVo.setId(null);
			prpLLossPersonVo.setDlossId(prpLDlossPersTraceMainVo.getId());
			prpLLossPersonVo.setPersonId(prpLDlossPersTraceVo.getId());
			PrpLDlossPersInjuredVo prpLDlossPersInjuredVo = prpLDlossPersTraceVo.getPrpLDlossPersInjured();

			// 年龄取自人伤。
			prpLLossPersonVo.setPersonAge(prpLDlossPersInjuredVo.getPersonAge().intValue());
			// 伤残等级从人伤中取。
			// prpLLossPersonVo.setInjureGrade(prpLDlossPersInjuredVo.getWoundCode());
			// 损失项目类型从人伤中取。
			// prpLLossPersonVo.setLossItemType(prpLDlossPersInjuredVo.getLossItemType());
			// 伤者姓名从人伤中取。
			prpLLossPersonVo.setPersonName(prpLDlossPersInjuredVo.getPersonName());

			// 总受损费用取人伤总核损费用。
			prpLLossPersonVo.setSumLoss(prpLDlossPersTraceVo.getSumVeriDefloss());
			// 总剔除费用取人伤总剔除费用。
			// prpLLossPersonVo.setSumReject(prpLDlossPersTraceVo.getSumverirejectfee());
			// prpLLossPersonVo.setKindCode(kindCode);
			/****************** 以下赋值之前人保、安联都是放在人员费用表里，此次设计拿到主表中 ******************/
			prpLLossPersonVo.setSumLoss(BigDecimal.ZERO);
			prpLLossPersonVo.setSumRealPay(BigDecimal.ZERO);
			prpLLossPersonVo.setSumOffLoss(BigDecimal.ZERO);
			prpLLossPersonVo.setOffPreAmt(BigDecimal.ZERO);
			prpLLossPersonVo.setBzPaidLoss(BigDecimal.ZERO);
			prpLLossPersonVo.setDutyRate(BigDecimal.ZERO);
			prpLLossPersonVo.setClaimRate(BigDecimal.ZERO);
			prpLLossPersonVo.setDeductDutyRate(BigDecimal.ZERO);
			prpLLossPersonVo.setDeductDutyAmt(BigDecimal.ZERO);
			// prpLLossPersonVo.setDeductAbsRate(deductAbsRate);
			// prpLLossPersonVo.setDeductAbsAmt(deductAbsRate);
			// prpLLossPersonVo.setDeductAddRate(deductAbsRate);
			// prpLLossPersonVo.setDeductAddAmt(deductAbsRate);

			// 从定损人伤明细复制同名属性数据到理算人伤明细中。
			List<PrpLDlossPersTraceFeeVo> prpLDlossPersTraceFeeVoList = prpLDlossPersTraceVo.getPrpLDlossPersTraceFees();
			for(PrpLDlossPersTraceFeeVo prpLDlossPersTraceFeeVo:prpLDlossPersTraceFeeVoList){
				String compensateKindCode = this.queryCompensateKindCode(prpLDlossPersTraceFeeVo.getKindCode(),prpLCompensateVo,
						prpLDlossPersTraceVo.getLossFeeType());

				PrpLCItemKindVo prpLcItemKindVo = this.queryPrpLcItemKind4KindCode(compensateKindCode,prpLCompensateVo);

				String compolusyKindCode = null;
				// this.kindService.translateCompolusyRisk(prpLDlossPersTraceFeeVo.getFeeTypeCode());

				// 有承保相关险别并且人伤费用类型能被交强理算，才组织理算项。
				// zhongyuhai 20121212
				if(prpLcItemKindVo!=null&&( this.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind())||( !this
						.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind()) )&&compolusyKindCode!=null )){
					PrpLLossPersonFeeVo prpLLossPersonFeeVo = new PrpLLossPersonFeeVo();
					Datas.copySimpleObjectToTargetFromSource(prpLLossPersonFeeVo,prpLDlossPersTraceFeeVo,true);
					// prpLLossPersonFeeVo.setPrpLLossPerson(prpLLossPersonVo);
					prpLLossPersonFeeVo.setId(null);

					// 人伤费用ID。
					// prpLLossPersonFeeVo.setPrpLpersonTraceFeeId(new BigDecimal(prpLpersonTraceFee.getId()));
					// 币别从保单获取。
					// prpLLossPersonFeeVo.setCurrency("CNY");
					// 费用代码默认取人伤跟踪估计费用明细的费用代码。
					prpLLossPersonFeeVo.setLossItemNo(prpLDlossPersTraceFeeVo.getFeeTypeCode());
					// 费用名称
					// prpLLossPersonFeeVo.setChargeName(prpLDlossPersTraceFeeVo.getFeeTypeName());
					// 金额类型默认取人伤跟踪的损失代码。
					// prpLLossPersonFeeVo.setLossFeeType(prpLpersonTrace.getLossFeeType());
					// 损失金额取人伤的核损金额。
					prpLLossPersonFeeVo.setFeeLoss(prpLDlossPersTraceFeeVo.getVeriDefloss());
					// 计入赔款金额取默认为0。
					prpLLossPersonFeeVo.setFeeRealPay(BigDecimal.ZERO);

					// itemKindNo、kindCode取险别。
					// prpLLossPersonFeeVo.setItemKindNo(prpLcItemKindVo.getItemKindNo());
					// prpLLossPersonFeeVo.setKindCode(prpLcItemKindVo.getKindCode());
					// prpLLossPersonFeeVo.setUnitAmount(prpLcItemKindVo.getUnitAmount());

					// 交强险已赔付金额默认为0。
					prpLLossPersonFeeVo.setBzPaidLoss(BigDecimal.ZERO);
					// 因为车险只有人民币一个币种所以兑换率都是1。
					// prpLLossPersonFeeVo.setExchangeRate(new BigDecimal(1));
					// 可选免赔率/其它免赔率/绝对免赔率默认为0。
					// prpLLossPersonFeeVo.setSelectDeductibleRate(BigDecimal.ZERO);
					// 其他扣除默认为0。
					// prpLLossPersonFeeVo.setOtherDeductFee(BigDecimal.ZERO);
					// 理算项预赔金额默认为0。
					// prpLLossPersonFeeVo.setSumPrePaid(BigDecimal.ZERO);
					// 人伤没有标的价值。
					// prpLLossPersonFeeVo.setItemValue(null);
					// 不计免赔率默认为0。
					// prpLLossPersonFeeVo.setExceptDeductibleRate(BigDecimal.ZERO);
					// 不计免赔金额默认为0。
					// prpLLossPersonFeeVo.setExceptDeductible(BigDecimal.ZERO);
					// 赔付比例默认为0。
					// prpLLossPersonFeeVo.setClaimRate(BigDecimal.ZERO);
					// 人伤没有残值。
					// prpLLossPersonFeeVo.setRemnantfee(BigDecimal.ZERO);
					// 事故责任免赔率默认为0。
					// prpLLossPersonFeeVo.setDutyDeductibleRate(BigDecimal.ZERO);
					// 免赔额默认为0。
					// prpLLossPersonFeeVo.setDeductible(BigDecimal.ZERO);
					// 赔付系数/比例默认为0。在理算计算完成后计算人伤费用赔付比例（超限额情况下，各种费用的赔付比例）。
					// prpLLossPersonFeeVo.setUnitRate(BigDecimal.ZERO);
					// 单位限额默认为0。
					// prpLLossPersonFeeVo.setUnitAmount(BigDecimal.ZERO);
					// 目前系统不需要公式类型，不保存。
					// prpLLossPersonFeeVo.setFormularType(null);
					// prpLLossPersonFeeVo.setParamDec(null);
					// 无用字段。
					// prpLLossPersonFeeVo.setExchangesum(BigDecimal.ZERO);

					prpLLossPersonVo.getPrpLLossPersonFees().add(prpLLossPersonFeeVo);
				}
			}

			// 当人员下有费用项才将人员理算项加入到计算书，避免没承保的理算项进入计算书。
			if( !prpLLossPersonVo.getPrpLLossPersonFees().isEmpty()){
				prpLLossPersonVoList.add(prpLLossPersonVo);
			}
		}
		return prpLLossPersonVoList;
	}

	public boolean isZcCompensate(PrpLCompensateVo prpLCompensateVo) {
		boolean isZcCompensate = false;

		boolean isSubstituteCase = false;
		PrpLSubrogationMainVo prpLSubrogationMainVo = subrogationService.find(prpLCompensateVo.getRegistNo());
		if(prpLSubrogationMainVo!=null&&prpLSubrogationMainVo.getSubrogationFlag()!=null){
			if("1".equals(prpLSubrogationMainVo.getSubrogationFlag())){
				isSubstituteCase = true;
			}
		}
		// 代位案件下的商业险赔款计算书。
		if(isSubstituteCase&&this.isActualOrExpenseCompensate(prpLCompensateVo.getCompensateKind())&&this.isCommercialOrCompulsory(prpLCompensateVo
				.getCompensateKind())){
			// 判断是否第一张商业险赔款计算书。
			List<PrpLCompensateVo> compensateVoList = new ArrayList<PrpLCompensateVo>();
			List<PrpLCompensateVo> compensateVoListTmp = this.queryMatchedTypeCompensate(prpLCompensateVo.getRegistNo(),
					prpLCompensateVo.getCompensateKind());
			if(compensateVoListTmp!=null&& !compensateVoListTmp.isEmpty()){
				for(PrpLCompensateVo prpLCompensate:compensateVoListTmp){
					if(CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE.equals(prpLCompensateVo.getUnderwriteFlag())||CodeConstants.UnderWriteFlag.AUTO_UNDERWRITE
							.equals(prpLCompensateVo.getUnderwriteFlag())){
						compensateVoList.add(prpLCompensate);
					}
				}
			}

			// 如果系统中没有有效商业险赔款计算书，目标计算书是代位求偿商业险计算书。
			if(compensateVoList.isEmpty()){
				isZcCompensate = true;
			}else{
				// 如果第一张有效商业赔款计算书的计算书号与目标计算书的计算书号一致，所以目标计算书号是代位求偿商业险计算书。
				PrpLCompensateVo firstZcCompensate = compensateVoList.get(0);
				if(firstZcCompensate.getCompensateNo().equals(prpLCompensateVo.getCompensateNo())){
					isZcCompensate = true;
				}
			}
		}

		return isZcCompensate;
	}

	private Map<String,BigDecimal> sumFee4Compulsory(Map<String,BigDecimal> kindCodeFee,PrpLCompensateVo prpLCompensateVo,String lossFeeType) {
		BigDecimal fee4Compulsory = kindCodeFee.get(CodeConstants.KINDCODE.KINDCODE_BZ);
		if(fee4Compulsory==null){
			fee4Compulsory = BigDecimal.ZERO;
		}
		kindCodeFee.remove(CodeConstants.KINDCODE.KINDCODE_BZ);

		Set<String> kindCodeSet = kindCodeFee.keySet();
		for(String kindCode:kindCodeSet){
			String compensateKindCode = this.queryCompensateKindCode(kindCode,prpLCompensateVo,lossFeeType);
			if(CodeConstants.KINDCODE.KINDCODE_BZ.equals(compensateKindCode)){
				fee4Compulsory = fee4Compulsory.add(kindCodeFee.get(kindCode));
			}
		}

		kindCodeFee = new HashMap<String,BigDecimal>();
		kindCodeFee.put(CodeConstants.KINDCODE.KINDCODE_BZ,fee4Compulsory);
		return kindCodeFee;
	}

	/**
	 * 由于交强险在定损时有可能定损为商业险， 所以如果是商业险计算书，直接返回定损的险别。如果是交强计算书，险别是交强险，直接返回，险别是商业险，需要转为交强险再返回。
	 * @param kindCode
	 * @param prpLCompensateVo
	 * @param lossFeeType
	 */
	private String queryCompensateKindCode(String kindCode,PrpLCompensateVo prpLCompensateVo,String lossFeeType) {
		Assert.notNull(kindCode);
		Assert.notNull(prpLCompensateVo);
		Assert.notNull(lossFeeType);

		String compensateKindCode = null;

		if(this.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind())){
			if( !CodeConstants.KINDCODE.KINDCODE_BZ.equals(kindCode)){
				compensateKindCode = kindCode;
			}
		}else{
			final String claimType = prpLCompensateVo.getCaseType();

			// 对于主车，交强险负责赔付的险别。
			// zhongyuhai 20121012
			List<String> mainCarLossKindCode = new ArrayList<String>();
			mainCarLossKindCode.add(CodeConstants.KINDCODE.KINDCODE_BZ);// 交强险。
			mainCarLossKindCode.add(CodeConstants.KINDCODE.KINDCODE_A);// 车损险，赔偿车辆损失。
			mainCarLossKindCode.add(CodeConstants.KINDCODE.KINDCODE_X);// 新增设备险，赔偿财产损失。
			mainCarLossKindCode.add(CodeConstants.KINDCODE.KINDCODE_D2);// 车上货物险，赔偿财产损失。
			mainCarLossKindCode.add(CodeConstants.KINDCODE.KINDCODE_D12);// 车上人员险，赔偿人伤。
			mainCarLossKindCode.add(CodeConstants.KINDCODE.KINDCODE_D11);// 车上司机险，赔偿人伤。

			// 交强险都带出本车损失。
			if(( lossFeeType.equals(CodeConstants.LossFeeType.THIS_CAR_LOSS)||lossFeeType.equals(CodeConstants.LossFeeType.THIS_PERSON_LOSS)||lossFeeType
					.equals(CodeConstants.LossFeeType.THIS_PERSON_DRIVER_LOSS)||lossFeeType.equals(CodeConstants.LossFeeType.THIS_CAR_PROP) )&&mainCarLossKindCode
					.contains(kindCode)){
				compensateKindCode = CodeConstants.KINDCODE.KINDCODE_BZ;
			}

			// 对于三者车，交强险负责赔付的险别。
			// zhongyuhai 20121012
			List<String> thirdPartyLossKindCode = new ArrayList<String>();
			thirdPartyLossKindCode.add(CodeConstants.KINDCODE.KINDCODE_BZ);// 交强险。
			thirdPartyLossKindCode.add(CodeConstants.KINDCODE.KINDCODE_B);// 商业三者险，赔偿车辆、财产、人伤损失。

			// 一般赔案、代位求偿取三者损失。
			if(( lossFeeType.equals(CodeConstants.LossFeeType.THIRDPARTY_CAR_LOSS)||lossFeeType
					.equals(CodeConstants.LossFeeType.THIRDPARTY_PERSON_LOSS)||lossFeeType.equals(CodeConstants.LossFeeType.THIRDPARTY_CAR_PROP)||lossFeeType
					.equals(CodeConstants.LossFeeType.THIRDPARTY_OTH_PERSON)||lossFeeType.equals(CodeConstants.LossFeeType.THIRDPARTY_OTH_PROP) )&&thirdPartyLossKindCode
					.contains(kindCode)&&( claimType.equals(CodeConstants.ClaimType.NORMAL)||claimType
					.equals(CodeConstants.ClaimType.EACHHIT_SELFLOSS_CICASE_SUB) )){
				compensateKindCode = CodeConstants.KINDCODE.KINDCODE_BZ;
			}
		}

		return compensateKindCode;
	}

	/**
	 * 通过车辆定损信息建立车辆理算信息。
	 * @param prpLdefLossMain
	 * @param prpLcItemKind
	 * @param fee 险别汇总金额。
	 * @param prplplatrecoveryinfo
	 * @return
	 */
	private PrpLLossItemVo makePrpLlossByDefLoss(PrpLDlossCarMainVo prpLDlossCarMainVo,PrpLCItemKindVo prpLcItemKindVo,BigDecimal fee,
													PrpLPlatLockVo prpLPlatLockVo) {
		boolean isMainCar = false;
		if(prpLDlossCarMainVo.getSerialNo()==1){
			isMainCar = true;
		}

		PrpLLossItemVo lossItem = new PrpLLossItemVo();

		// 车辆金额类型取自车辆定损。
		// lossItem.setLossFeeType(prpLDlossCarMainVo.getLossFeeType());
		// 车辆定损id。
		lossItem.setDlossId(prpLDlossCarMainVo.getId());

		final String cetainLossType = prpLDlossCarMainVo.getCetainLossType();
		// 推定全损定损下。
		if(CodeConstants.CetainLossType.DEFLOSS_ALL.equals(cetainLossType)){
			// 车损险的车辆损失金额取自核损的总金额。这种情况出现在推定全损的情况下。
			lossItem.setSumLoss(prpLDlossCarMainVo.getSumVeriLossFee());
			// 车辆残值金额取自核损的残值。这种情况出现在推定全损的情况下。
			// lossItem.setRemnantfee(prpLDlossCarMainVo.getSumveriremnantfee());
		}else{
			lossItem.setSumLoss(fee);
			// 残值费用由同险别的换件中的残值费用汇总。
			// BigDecimal remnantfee = BigDecimal.ZERO;
			// List<PrpLcomponent> prpLcomponentList = prpLDlossCarMainVo.getPrpLcomponents();
			// for(PrpLcomponent prpLcomponent:prpLcomponentList){
			// if(prpLcomponent.getKindCode().equals(prpLcItemKind.getKindCode())){
			// remnantfee = remnantfee.add(prpLcomponent.getRemnantfee());
			// }
			// }
			// lossItem.setRemnantfee(remnantfee);
		}

		// 项目类别从险别信息中获取。
		// lossItem.setItemCode(prpLcItemKindVo.getItemCode());
		// 险别序号从险别信息中获取。
		// lossItem.setItemKindNo(prpLcItemKindVo.getItemKindNo());
		// 免赔率从险别记录中获取。
		lossItem.setDeductAbsRate(prpLcItemKindVo.getDeductibleRate());
		// 免赔额从险别记录中获取。
		lossItem.setDeductAbsAmt(prpLcItemKindVo.getDeductible());
		// 施救费默认为0，由后续方法填充。
		lossItem.setRescueFee(BigDecimal.ZERO);
		// 币别从险别记录中获取。
		lossItem.setCurrency("CNY");
		// 险别代码从险别信息中获取。
		lossItem.setKindCode(prpLcItemKindVo.getKindCode());

		// 设置代位求偿字段。
		// 在车辆有锁定记录并且是主车或者险别是交强险或者商业三者险才设置代付或清付字段。
		// 因为三者车只有交强险或者商业三者险的情况下才做清付处理。
		// zhongyuhai 20121011
		if(prpLPlatLockVo!=null&&( isMainCar||lossItem.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_B)||lossItem.getKindCode().equals(
				CodeConstants.KINDCODE.KINDCODE_BZ) )){
			// lossItem.setOpponentCoverageType(prpLPlatLockVo.getOpponentcoveragetype);
			lossItem.setPayFlag(prpLPlatLockVo.getRecoveryOrPayFlag());
			// lossItem.setSubrogationId(prpLPlatLockVo.getId());
			// 代付下车险损失项都归入车损险。
			if(prpLPlatLockVo.getRecoveryOrPayFlag().equals(CodeConstants.PayFlagType.INSTEAD_PAY)){
				// 代位求偿中险别归入车损险。
				List<PrpLCItemKindVo> kindVoList = policyViewService.findItemKindVos(null,prpLDlossCarMainVo.getRegistNo(),
						CodeConstants.KINDCODE.KINDCODE_A1);
				if(kindVoList!=null&&kindVoList.size()>0){
					lossItem.setKindCode(CodeConstants.KINDCODE.KINDCODE_A1);
				}else{
					lossItem.setKindCode(CodeConstants.KINDCODE.KINDCODE_A);
				}

				// lossItem.setInsteadprplthirdpartyid(prplplatrecoveryinfo.getCertiid());
			}
		}else{
			// lossItem.setOpponentCoverageType(CodeConstants.Opponentcoveragetype.SF700.value);
			lossItem.setPayFlag(CodeConstants.PayFlagType.COMPENSATE_PAY);
			// lossItem.setPrpLplatRecoveryInfoID(null);
		}

		// 设置车辆信息。
		// 如果代位，PrpLloss车牌取三者车车牌。
		// if(prpLPlatLockVo != null && prpLPlatLockVo.getRecoveryOrPayFlag().equals(CodeConstants.PayFlagType.INSTEAD_PAY)){
		// PrpLthirdParty prpLthirdParty = this.thirdPartyService.findPrpLthirdPartyByPK(prplplatrecoveryinfo.getCertiid());
		// lossItem.setLicenseNo(prpLthirdParty.getLicenseNo());
		// }else{
		// PrpLthirdParty prpLthirdParty = this.thirdPartyService.findPrpLthirdPartyByPK(prpLdefLossMain.getPrpLthirdPartId());
		// lossItem.setLicenseNo(prpLthirdParty.getLicenseNo());
		// }
		// PrpLthirdParty prpLthirdParty = this.thirdPartyService.findPrpLthirdPartyByPK(prpLdefLossMain.getPrpLthirdPartId());
		// lossItem.setPrpLthirdPartyId(prpLthirdParty.getId());
		// lossItem.setLossName(prpLthirdParty.getLicenseNo());

		// 标的价值币别从险别记录中获取。
		// lossItem.setCurrency1(prpLdefLossMain.getCurrency());
		// 受损金额币别从车辆定损中获取。
		// lossItem.setCurrency2(prpLdefLossMain.getCurrency());

		// 交强险已赔付金额默认为0，在界面中通过获取交强险赔款来获取。
		lossItem.setBzPaidLoss(BigDecimal.ZERO);
		// 冲减标志默认为不冲减。
		lossItem.setDeductFlag("0");
		// 理算项预赔金额默认为0。
		lossItem.setOffPreAmt(BigDecimal.ZERO);
		// 其他扣除金额默认为0。
		// lossItem.setOtherDeductFee(BigDecimal.ZERO);
		// 计入赔款金额默认为0。
		lossItem.setSumRealPay(BigDecimal.ZERO);
		// 施救费交强赔付金额默认为0，在界面中通过获取交强险赔款来获取。
		lossItem.setBzPaidRescueFee(BigDecimal.ZERO);
		// 可选免赔率/其它免赔率/绝对免赔率默认为0。
		lossItem.setDeductAddAmt(BigDecimal.ZERO);
		// 不计免赔率默认为0。
		lossItem.setDeductOffRate(BigDecimal.ZERO);
		// 不计免赔金额默认为0。
		lossItem.setDeductOffAmt(BigDecimal.ZERO);
		// 赔付比例默认为0。
		lossItem.setClaimRate(BigDecimal.ZERO);

		return lossItem;
	}

	/**
	 * 根据计算书和险别去查询PrpLCItemKindVo
	 * 
	 * <pre></pre>
	 * @param kindCode
	 * @param prpLCompensateVo
	 * @return
	 * @modified: ☆ZhouYanBin(2016年5月3日 上午10:03:09): <br>
	 */
	public PrpLCItemKindVo queryPrpLcItemKind4KindCode(String kindCode,PrpLCompensateVo prpLCompensateVo) {

		PrpLCItemKindVo matchedPrpLcItemKind = null;

		// 查询计算书涉及的保单。
		PrpLCMainVo prpLcMainVo = null;
		// 如果查询不到交强险保单，由系统自动生成，以方便模拟交强险计算书来做商业险获取交强的操作。
		// zhongyuhai 20121022
		if(prpLCompensateVo.getPolicyNo()==null&&CodeConstants.KINDCODE.KINDCODE_BZ.equals(kindCode)){
			prpLcMainVo = this.makeMockCompulsoryPolicy();
		}else{
			prpLcMainVo = policyViewService.getPolicyInfo(prpLCompensateVo.getRegistNo(),prpLCompensateVo.getPolicyNo());
		}

		List<PrpLCItemKindVo> prpLcItemKindVoList = prpLcMainVo.getPrpCItemKinds();
		for(PrpLCItemKindVo prpLcItemKindVo:prpLcItemKindVoList){
			// 如果有效承保险别和损失类型项匹配的险别一致，返回承保险别记录。
			if(prpLcItemKindVo.getKindCode().equals(kindCode)){
				matchedPrpLcItemKind = prpLcItemKindVo;
				break;
			}
		}

		return matchedPrpLcItemKind;
	}

	/**
	 * 模拟交强险保单。
	 * @return
	 */
	private PrpLCMainVo makeMockCompulsoryPolicy() {
		PrpLCMainVo prpLcMainVo = new PrpLCMainVo();
		prpLcMainVo.setCurrency("CNY");

		PrpLCItemKindVo prpLCItemKindVo = new PrpLCItemKindVo();
		prpLCItemKindVo.setKindCode(CodeConstants.KINDCODE.KINDCODE_BZ);

		prpLcMainVo.getPrpCItemKinds().add(prpLCItemKindVo);
		return prpLcMainVo;
	}

	/**
	 * 将车辆定损任务下的所有定损项汇总为险别-费用Map。
	 * @param prpLdefLossMain
	 * @param prpLcompensate
	 * @return
	 */
	private Map<String,BigDecimal> sumKindCodeFee(PrpLDlossCarMainVo prpLDlossCarMainVo,PrpLCompensateVo prpLCompensateVo) {
		// 分险别汇总费用。
		Map<String,BigDecimal> kindCodeFee = this.calKindCodeFee(prpLDlossCarMainVo.getPrpLDlossCarRepairs(),null);
		kindCodeFee = this.calKindCodeFee(prpLDlossCarMainVo.getPrpLDlossCarMaterials(),kindCodeFee);
		kindCodeFee = this.calKindCodeFee(prpLDlossCarMainVo.getPrpLDlossCarComps(),kindCodeFee);
		// 补没有明细定损项目时，录入施救费得情况。
		if(kindCodeFee.isEmpty()&&BigDecimal.ZERO.compareTo(prpLDlossCarMainVo.getSumVeriRescueFee())<0){
			if(CodeConstants.CetainLossType.DEFLOSS_ALL.equals(prpLDlossCarMainVo.getCetainLossType())){
				kindCodeFee.put(CodeConstants.KINDCODE.KINDCODE_A,BigDecimal.ZERO);
			}else if(CodeConstants.CetainLossType.DEFLOSS_ROBBERY.equals(prpLDlossCarMainVo.getCetainLossType())){
				kindCodeFee.put(CodeConstants.KINDCODE.KINDCODE_G,BigDecimal.ZERO);
			}
		}

		String lossItemType = null;
		if("1".equals(prpLDlossCarMainVo.getSerialNo().toString().trim())){
			lossItemType = CodeConstants.LossFeeType.THIS_CAR_LOSS;
		}else{
			lossItemType = CodeConstants.LossFeeType.THIRDPARTY_CAR_LOSS;
		}
		return this.sumKindCodeFee(kindCodeFee,prpLCompensateVo,lossItemType);
	}

	/**
	 * 将财产定损任务下的所有定损项汇总为险别-费用Map。
	 * @param prpLpropMain
	 * @param prpLcompensate
	 * @return
	 */
	private Map<String,BigDecimal> sumKindCodeFee(PrpLdlossPropMainVo prpLdlossPropMainVo,PrpLCompensateVo prpLCompensateVo) {
		// 分险别汇总费用。
		Map<String,BigDecimal> kindCodeFee = this.calKindCodeFee(prpLdlossPropMainVo.getPrpLdlossPropFees(),null);
		String lossItemType = null;
		if("1".equals(prpLdlossPropMainVo.getSerialNo().toString().trim())){
			lossItemType = CodeConstants.LossFeeType.THIS_CAR_PROP;
		}else if("0".equals(prpLdlossPropMainVo.getSerialNo().toString().trim())){
			lossItemType = CodeConstants.LossFeeType.THIS_CAR_PROP;
		}else{
			lossItemType = CodeConstants.LossFeeType.THIRDPARTY_OTH_PROP;
		}
		return this.sumKindCodeFee(kindCodeFee,prpLCompensateVo,lossItemType);
	}

	/**
	 * 将定损项汇总为险别-金额Map按计算书类型汇总金额。
	 * @param kindCodeFee
	 * @param prpLcompensate
	 * @param lossFeeType
	 * @return
	 */
	private Map<String,BigDecimal> sumKindCodeFee(Map<String,BigDecimal> kindCodeFee,PrpLCompensateVo prpLCompensateVo,String lossFeeType) {
		Set<String> kindCodeSet = kindCodeFee.keySet();

		if(this.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind())){
			for(String kindCode:kindCodeSet){
				if(CodeConstants.KINDCODE.KINDCODE_BZ.equals(kindCode)){
					kindCodeFee.remove(kindCode);
				}
			}
		}else{
			kindCodeFee = this.sumFee4Compulsory(kindCodeFee,prpLCompensateVo,lossFeeType);
		}

		return kindCodeFee;
	}

	/**
	 * 汇总换件、维修、辅料的险别费用。
	 * @param feeList
	 * @param kindCodeFee
	 * @return
	 */
	private Map<String,BigDecimal> calKindCodeFee(List feeList,Map<String,BigDecimal> kindCodeFee) {
		kindCodeFee = kindCodeFee!=null ? kindCodeFee : new HashMap<String,BigDecimal>();

		for(Object feeRecord:feeList){
			final String kindCode = (String)Refs.get(feeRecord,"kindCode");
			String feeField = null;
			if(feeRecord instanceof PrpLDlossCarCompVo){
				feeField = "vericompfee";
			}
			if(feeRecord instanceof PrpLDlossCarMaterialVo){
				feeField = "veriMaterialFee";
			}
			if(feeRecord instanceof PrpLDlossCarRepairVo){
				feeField = "verirepairfee";
			}
			if(feeRecord instanceof PrpLdlossPropFeeVo){
				feeField = "verilossfee";
			}

			final BigDecimal fee = (BigDecimal)Refs.get(feeRecord,feeField);

			BigDecimal totalFee;
			if(kindCodeFee.containsKey(kindCode)){
				totalFee = kindCodeFee.get(kindCode).add(fee);
			}else{
				totalFee = fee;
			}
			kindCodeFee.put(kindCode,totalFee);
		}

		return kindCodeFee;
	}

	/**
	 * 通过以下参数组装PrpLloss。
	 * @param kindCodeFee
	 * @param prpLcompensate
	 * @param prpLdefLossMain
	 * @param prplplatrecoveryinfo
	 * @param kindCodesNeeded 需要返回的那些险别的PrpLloss。如果kindCodesNeeded没有元素，返回全部。
	 * @return
	 */
	private List<PrpLLossItemVo> makePrpLloss4KindCode(Map<String,BigDecimal> kindCodeFee,PrpLCompensateVo prpLCompensateVo,
														PrpLDlossCarMainVo prpLdDlossCarMainVo,PrpLPlatLockVo prpLPlatLockVo,
														List<String> kindCodesNeeded) {
		List<PrpLLossItemVo> prpLLossItemVoList = new ArrayList<PrpLLossItemVo>();
		Set<String> kindCodeSet = kindCodeFee.keySet();
		for(String kindCode:kindCodeSet){
			if(kindCodesNeeded.isEmpty()||kindCodesNeeded.contains(kindCode)){
				// 查询损失项对应的险别信息。
				PrpLCItemKindVo prpLcItemKindVo = this.queryPrpLcItemKind4KindCode(kindCode,prpLCompensateVo);
				if(prpLcItemKindVo!=null){
					PrpLLossItemVo prpLLossItemVo = this.makePrpLlossByDefLoss(prpLdDlossCarMainVo,prpLcItemKindVo,kindCodeFee.get(kindCode),
							prpLPlatLockVo);
					prpLLossItemVoList.add(prpLLossItemVo);
				}
			}
		}
		return prpLLossItemVoList;
	}

	/**
	 * 填充车辆理算项中的施救费。
	 * @param prpLcompensate
	 * @param prpLlossList
	 * @param prpLdefLossMain
	 */
	private void fillRescueFee4Car(PrpLCompensateVo prpLCompensateVo,List<PrpLLossItemVo> prpLLossItemVoList,PrpLDlossCarMainVo prpLDlossCarMainVo) {
		// 当有车辆理算项的时候，才处理施救费。有可能存在定损在本计算书中不能理算的情况。比如定损为盗抢险，但发起交强险计算书的情况。
		// zhongyuhai 20121121
		if( !prpLLossItemVoList.isEmpty()){
			// PrpLthirdParty mainCar = this.thirdPartyService.findInsuredCarByRegistNo(prpLCompensateVo.getRegistNo());
			BigDecimal rescueFee = prpLDlossCarMainVo.getSumVeriRescueFee();
			logger.info("rescueFee = "+rescueFee);
			// 定损的施救费挂在任意主险下，无主险挂在任意附加险下
			if(this.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind())){
				// 此标志用于判断是否有主险
				boolean flag = false;
				for(PrpLLossItemVo prpLLossItemVo:prpLLossItemVoList){
					if(CodeConstants.KINDCODE.KINDCODE_A.equals(prpLLossItemVo.getKindCode())||CodeConstants.KINDCODE.KINDCODE_B
							.equals(prpLLossItemVo.getKindCode())||CodeConstants.KINDCODE.KINDCODE_G.equals(prpLLossItemVo.getKindCode())||CodeConstants.KINDCODE.KINDCODE_D11
							.equals(prpLLossItemVo.getKindCode())||CodeConstants.KINDCODE.KINDCODE_D12.equals(prpLLossItemVo.getKindCode())){
						prpLLossItemVo.setRescueFee(rescueFee);
						flag = true;
						break;
					}
				}

				if( !flag){
					for(PrpLLossItemVo prpLLossItemVo:prpLLossItemVoList){
						if( !CodeConstants.KINDCODE.KINDCODE_BZ.equals(prpLLossItemVo.getKindCode())){
							prpLLossItemVo.setRescueFee(rescueFee);
							break;
						}
					}
				}
			}else{
				prpLLossItemVoList.get(0).setRescueFee(rescueFee);
			}
		}
	}

	/**
	 * 填充财产理算项中的施救费。
	 * @param prpLcompensate
	 * @param prpLpropLossList
	 * @param prpLpropMain
	 */
	private void fillRescueFee4Prop(PrpLCompensateVo prpLCompensateVo,List<PrpLLossPropVo> prpLLossPropVoList,PrpLdlossPropMainVo prpLdlossPropMainVo) {
		// 当有车辆理算项的时候，才处理施救费。有可能存在定损在本计算书中不能理算的情况。比如定损为盗抢险，但发起交强险计算书的情况。
		// zhongyuhai 20121121
		if( !prpLLossPropVoList.isEmpty()){
			BigDecimal rescueFee = prpLdlossPropMainVo.getSumVeriLoss();

			// 三者车定损的施救费只能加入到主车的商业三者险中。如果是交强险，都加入施救费。
			if(this.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind())){
				boolean flag = false;
				for(PrpLLossPropVo prpLLossPropVo:prpLLossPropVoList){
					if(CodeConstants.KINDCODE.KINDCODE_A.equals(prpLLossPropVo.getKindCode())||CodeConstants.KINDCODE.KINDCODE_B
							.equals(prpLLossPropVo.getKindCode())||CodeConstants.KINDCODE.KINDCODE_G.equals(prpLLossPropVo.getKindCode())||CodeConstants.KINDCODE.KINDCODE_D11
							.equals(prpLLossPropVo.getKindCode())||CodeConstants.KINDCODE.KINDCODE_D12.equals(prpLLossPropVo.getKindCode())){
						prpLLossPropVo.setRescueFee(rescueFee);
						flag = true;
						break;
					}
				}

				if( !flag){
					for(PrpLLossPropVo prpLLossPropVo:prpLLossPropVoList){
						if( !CodeConstants.KINDCODE.KINDCODE_BZ.equals(prpLLossPropVo.getKindCode())){
							prpLLossPropVo.setRescueFee(rescueFee);
							break;
						}
					}
				}
			}else{
				prpLLossPropVoList.get(0).setRescueFee(rescueFee);
			}
		}
	}

	/**
	 * 对交强计算书的数据进行过滤。过滤内容看方法内注释。
	 * @param prpLcompensate
	 */
	private void fill4CompulsoryPrpLcompensate(PrpLCompensateVo prpLCompensateVo) {

		if( !this.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind())){
			// 生成交强险代赔。
			this.fillInsteadLoss(prpLCompensateVo);
			// 删除计算书中非代赔数据。
			// this.removeMainCarLoss4Cal(prpLCompensateVo);
			// 对非第一张交强险计算书中数据进行过滤。
			this.fill4NotFirstCompulsoryPrpLcompensate(prpLCompensateVo);
		}
	}

	/**
	 * 生成交强赔款计算书中的无责代赔理算对象（车、财、人）。
	 * @param prpLcompensate
	 * @return
	 */
	private void fillInsteadLoss(PrpLCompensateVo prpLCompensateVo) {

		// 如果是交强险一般、代位案件的无责代赔。
		if(this.isCompulsory4Normal_Replatement(prpLCompensateVo)){

			List<PrpLLossItemVo> mainCarCiPrpLlossList = new ArrayList<PrpLLossItemVo>();
			Iterator<PrpLLossItemVo> it4PrpLlosses = prpLCompensateVo.getPrpLLossItems().iterator();
			while(it4PrpLlosses.hasNext()){
				PrpLLossItemVo prpLlossItemVo = it4PrpLlosses.next();
				if("1".equals(prpLlossItemVo.getItemId())){
					it4PrpLlosses.remove();
					mainCarCiPrpLlossList.add(prpLlossItemVo);
				}
			}

			List<PrpLLossPropVo> mainCarCiPrpLpropLossList = new ArrayList<PrpLLossPropVo>();
			Iterator<PrpLLossPropVo> it4PrpLpropLosses = prpLCompensateVo.getPrpLLossProps().iterator();
			while(it4PrpLpropLosses.hasNext()){
				PrpLLossPropVo prpLLossPropVo = it4PrpLpropLosses.next();
				if("1".equals(prpLLossPropVo.getItemId())){
					it4PrpLpropLosses.remove();
					mainCarCiPrpLpropLossList.add(prpLLossPropVo);
				}
			}

			List<PrpLLossPersonVo> mainCarCiPrpLpersonItemList = new ArrayList<PrpLLossPersonVo>();
			Iterator<PrpLLossPersonVo> it4PrpLpersonItem = prpLCompensateVo.getPrpLLossPersons().iterator();
			while(it4PrpLpersonItem.hasNext()){
				PrpLLossPersonVo prpLLossPersonVo = it4PrpLpersonItem.next();
				if("1".equals(prpLLossPersonVo.getItemId())){
					it4PrpLpersonItem.remove();
					mainCarCiPrpLpersonItemList.add(prpLLossPersonVo);
				}
			}

			List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(prpLCompensateVo.getRegistNo());

			for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){
				// 无责、无保代赔中只考虑三者车(鼎和没有无保代赔，此处只考虑无责代赔)。
				if(prpLDlossCarMainVo.getSerialNo()!=1){
					// 是否主车对三者车进行无责代赔。
					boolean isNodutyPaid = this.isNodutyPaid(prpLCompensateVo.getRegistNo(),prpLDlossCarMainVo.getSerialNo());

					String payFlag = null;
					if(isNodutyPaid){
						payFlag = CodeConstants.PayFlagType.NODUTY_INSTEAD_PAY;
					}

					if(payFlag!=null){
						for(PrpLLossItemVo mainCarPrpLloss:mainCarCiPrpLlossList){
							// 将主车PrpLloss复制一条，作为代赔数据。
							PrpLLossItemVo insteadPrpLloss = new PrpLLossItemVo();
							Datas.copySimpleObjectToTargetFromSource(insteadPrpLloss,mainCarPrpLloss,true);
							insteadPrpLloss.setItemId(prpLDlossCarMainVo.getSerialNo().toString());
							insteadPrpLloss.setItemName(prpLDlossCarMainVo.getLicenseNo());
							insteadPrpLloss.setPayFlag(payFlag);
							prpLCompensateVo.getPrpLLossItems().add(insteadPrpLloss);
						}
					}
				}
			}
		}
	}

	/**
	 * 删除交强计算书中的主车损失项 (此方法可以用fillInsteadLoss代替)。
	 * @param prpLcompensate
	 */
	// private void removeMainCarLoss4Cal(PrpLCompensateVo prpLCompensateVo){
	//
	// if(this.isCompulsory4Normal_Replatement(prpLCompensateVo)){
	//
	// Iterator<PrpLLossItemVo> it4PrpLloss = prpLCompensateVo.getPrpLLossItems().iterator();
	// while(it4PrpLloss.hasNext()){
	// PrpLLossItemVo prpLLossItemVo = it4PrpLloss.next();
	// if("1".equals(prpLLossItemVo.getItemId()) && !CodeConstants.PayFlagType.NODUTY_INSTEAD_PAY.equals(prpLLossItemVo.getPayFlag())){
	// it4PrpLloss.remove();
	// }
	// }
	//
	// Iterator<PrpLLossPropVo> it4PrpLpropLoss = prpLCompensateVo.getPrpLLossProps().iterator();
	// while(it4PrpLpropLoss.hasNext()){
	// PrpLLossPropVo prpLLossPropVo = it4PrpLpropLoss.next();
	// if("1".equals(prpLLossPropVo.getItemId()) && !CodeConstants.PayFlagType.NODUTY_INSTEAD_PAY.equals(prpLLossPropVo.getPayFlag())){
	// it4PrpLpropLoss.remove();
	// }
	// }
	//
	// Iterator<PrpLLossPersonVo> it4PrpLpersonItem = prpLCompensateVo.getPrpLLossPersons().iterator();
	// while(it4PrpLpersonItem.hasNext()){
	// PrpLLossPersonVo prpLLossPersonVo = it4PrpLpersonItem.next();
	// if("1".equals(prpLLossPersonVo.getItemId()) && !CodeConstants.PayFlagType.NODUTY_INSTEAD_PAY.equals(prpLLossPersonVo.getPayFlag())){
	// it4PrpLpersonItem.remove();
	// }
	// }
	// }
	// }

	/**
	 * 对第一张后的交强计算书进行过滤。
	 * @param prpLcompensate
	 */
	private void fill4NotFirstCompulsoryPrpLcompensate(PrpLCompensateVo prpLCompensateVo) {

		if( !this.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind())){
			List<PrpLCompensateVo> pass4ClaimList = this.findPassCompulsoryCompensate(prpLCompensateVo.getClaimNo());
			if( !pass4ClaimList.isEmpty()){
				this.removeCompensated(prpLCompensateVo,prpLCompensateVo.getPrpLLossItems());
				this.removeCompensated(prpLCompensateVo,prpLCompensateVo.getPrpLLossProps());
				this.removeCompensated(prpLCompensateVo,prpLCompensateVo.getPrpLLossPersons());
			}
		}
	}

	/**
	 * 查询核赔通过的交强险赔款计算书(包括垫付计算书)。
	 * @param claimNo
	 * @return
	 */
	private List<PrpLCompensateVo> findPassCompulsoryCompensate(String claimNo) {
		List<PrpLCompensateVo> prpLCompensateVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("claimNo",claimNo);

		queryRule.addIn("underwriteFlag",CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE,CodeConstants.UnderWriteFlag.AUTO_UNDERWRITE);
		queryRule.addIn("compensateType",CodeConstants.CompensateKind.CI_COMPENSATE,CodeConstants.CompensateKind.ADVANCE);

		List<PrpLCompensate> prpLCompensateList = databaseDao.findAll(PrpLCompensate.class,queryRule);
		if(prpLCompensateList!=null&& !prpLCompensateList.isEmpty()){
			prpLCompensateVoList = Beans.copyDepth().from(prpLCompensateList).toList(PrpLCompensateVo.class);
		}
		return prpLCompensateVoList;
	}

	/**
	 * 交强险一般和代位计算书，需要考虑删除主车损失，不做显示。
	 * @param prpLcompensate
	 * @return
	 */
	private boolean isCompulsory4Normal_Replatement(PrpLCompensateVo prpLCompensateVo) {
		String claimType = prpLCompensateVo.getCaseType();

		return !this.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind())&&( CodeConstants.ClaimType.NORMAL.equals(claimType)||CodeConstants.ClaimType.EACHHIT_SELFLOSS_CICASE_SUB
				.equals(claimType) );
	}

	/**
	 * 根据报案号查询该三者车是否属于无责代赔
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月27日 上午11:43:06): <br>
	 */
	private boolean isNodutyPaid(String registNo,Integer ThirdCarSeralNo) {
		List<PrpLCheckDutyVo> prpLCheckDutyVoList = checkTaskService.findCheckDutyByRegistNo(registNo);
		boolean mainCiDutyFlag = false;
		boolean thirdCiNoDutyFlag = false;
		if(prpLCheckDutyVoList!=null&& !prpLCheckDutyVoList.isEmpty()){
			// 判断主车有责
			for(PrpLCheckDutyVo prpLCheckDutyVo:prpLCheckDutyVoList){
				if(prpLCheckDutyVo.getSerialNo()==1&&"1".equals(prpLCheckDutyVo.getCiDutyFlag())){
					mainCiDutyFlag = true;
					break;
				}
			}
			// 判断三者车无责
			for(PrpLCheckDutyVo prpLCheckDutyVo:prpLCheckDutyVoList){
				if(ThirdCarSeralNo.equals(prpLCheckDutyVo.getSerialNo())&& !"1".equals(prpLCheckDutyVo.getCiDutyFlag())){
					thirdCiNoDutyFlag = true;
					break;
				}
			}
		}
		return ( mainCiDutyFlag&&thirdCiNoDutyFlag );
	}

	/**
	 * 删除已做定损理算的理算项。目前只考虑车、财、人。
	 * @param prpLcompensate
	 * @param lossItemList
	 * @return
	 */
	private void removeCompensated(PrpLCompensateVo prpLCompensateVo,List lossItemList) {
		String lossState = this.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind()) ? CodeConstants.LossState.BI_COMPENSATE : CodeConstants.LossState.CI_COMPENSATE;
		Iterator lossItemsIt = lossItemList.iterator();
		// 查找对应的定损类、理算中的定损类ID、定损记录。
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",prpLCompensateVo.getRegistNo());
		queryRule.addEqual("lossState",lossState);
		while(lossItemsIt.hasNext()){
			Object lossItem = lossItemsIt.next();
			if(lossItem.getClass().equals(PrpLLossItemVo.class)){
				List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findPrpLDlossCarMainVoListByRule(queryRule);
				if(prpLDlossCarMainVoList!=null&& !prpLDlossCarMainVoList.isEmpty()){
					lossItemsIt.remove();
				}
			}else if(lossItem.getClass().equals(PrpLLossPropVo.class)){
				List<PrpLdlossPropMainVo> prpLdlossPropMainVoList = propTaskService.findPrpLdlossPropMainVoListByRule(queryRule);
				if(prpLdlossPropMainVoList!=null&& !prpLdlossPropMainVoList.isEmpty()){
					lossItemsIt.remove();
				}
			}else if(lossItem.getClass().equals(PrpLLossPersonVo.class)){
				List<PrpLDlossPersTraceVo> prpLDlossPersTraceVoList = persTraceDubboService.findPrpLDlossPersTraceVoListByRule(queryRule);
				if(prpLDlossPersTraceVoList!=null&& !prpLDlossPersTraceVoList.isEmpty()){
					lossItemsIt.remove();
				}
			}
		}
	}

	/**
	 * 查询给定的计算书在当前可以理算的所有费用信息。
	 * @param prpLcompensate
	 * @author zhongyuhai
	 * @return
	 */
	public List<PrpLChargeVo> queryReadyPrpLcharge(String registNo) {
		List<PrpLChargeVo> prpLChargeVoList = null;
		// 费用中的理算状态条件。
		String lossStateCondition = CodeConstants.LossState.UN_COMPENSATE;
		// 查询被保单下某张保单的未理算费用。
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLCharge> prpLChargeList = databaseDao.findAll(PrpLCharge.class,queryRule);
		if(prpLChargeList!=null&& !prpLChargeList.isEmpty()){
			prpLChargeVoList = Beans.copyDepth().from(prpLChargeList).toList(PrpLChargeVo.class);
		}
		return prpLChargeVoList;
	}

	/**
	 * 填充计算书中的预赔金额。
	 * @param prpLcompensate
	 */
	private void fillPrePaid(PrpLCompensateVo prpLCompensateVo) {
		logger.info("-->fillPrePaid");
		prpLCompensateVo.setSumPreAmt(BigDecimal.ZERO);
		// 赔款计算书需要考虑预赔金额初始化。
		if(this.isActualOrExpenseCompensate(prpLCompensateVo.getCompensateKind())){
			// 当前商业险实赔计算书数量。
			int actualPaymentCount = this.queryNumByClaimNo(prpLCompensateVo.getClaimNo(),prpLCompensateVo.getCompensateKind());
			// 在本计算书初始化前没有其他商业实赔计算书，本次计算书的预赔金额等于当前预赔金额和。
			if(actualPaymentCount==0){
				prpLCompensateVo.setSumPreAmt(this.querySumPrePaid(prpLCompensateVo.getClaimNo()));
				// this.fillPrePaidDetail(prpLCompensateVo);
			}else{
				// 在本计算书初始化前已有赔款计算书，本次计算书的预赔金额等于0。
				prpLCompensateVo.setSumPreAmt(BigDecimal.ZERO);
			}
		}
		logger.info("<--fillPrePaid");
	}

	/**
	 * 根据立案号获得有效计算书记录个数
	 * @param claimNo
	 * @return
	 */
	private int queryNumByClaimNo(String claimNo,String compensateKind) {
		List<String> underwriteFlag = new ArrayList<String>();
		underwriteFlag.add(CodeConstants.UnderWriteFlag.NORMAL);
		underwriteFlag.add(CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE);
		underwriteFlag.add(CodeConstants.UnderWriteFlag.BACK_UNDERWRITE);
		underwriteFlag.add(CodeConstants.UnderWriteFlag.AUTO_UNDERWRITE);
		underwriteFlag.add(CodeConstants.UnderWriteFlag.WAIT_UNDERWRITE);
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("claimNo",claimNo);
		queryRule.addEqual("compensateKind",compensateKind);
		queryRule.addEqual("underwriteFlag",underwriteFlag);

		List<PrpLCompensate> prpLCompensateList = databaseDao.findAll(PrpLCompensate.class,queryRule);
		if(prpLCompensateList!=null){
			return prpLCompensateList.size();
		}else{
			return 0;
		}
	}

	/**
	 * 查询本立案下的预赔金额总和。
	 * @param claimNo
	 * @return
	 */
	public BigDecimal querySumPrePaid(String registNo) {
		BigDecimal prePay = BigDecimal.ZERO;
		List<String> underwriteFlag = new ArrayList<String>();
		underwriteFlag.add(CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE);
		underwriteFlag.add(CodeConstants.UnderWriteFlag.AUTO_UNDERWRITE);

		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("compensateKind",CodeConstants.CompensateKind.PRY_PAY);
		queryRule.addEqual("underwriteFlag",underwriteFlag);
		List<PrpLCompensate> prpLCompensateList = databaseDao.findAll(PrpLCompensate.class,queryRule);
		if(prpLCompensateList!=null&& !prpLCompensateList.isEmpty()){
			for(PrpLCompensate prpLCompensate:prpLCompensateList){
				prePay = prePay.add(prpLCompensate.getSumAmt());
			}
		}
		return prePay;
	}

	/**
	 * 将PrpLcompensate转换为CompensateVo。
	 * @param prpLcompensate
	 * @return
	 */
	public CompensateVo convertPrpLcompensate2CompensateVo(PrpLCompensateVo prpLCompensateVo,PrpLWfTaskVo prpLWfTaskVo) {
		String registNo = prpLCompensateVo.getRegistNo();
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);

		CompensateVo compensateVo = new CompensateVo();

		compensateVo.setPrpLCompensateVo(prpLCompensateVo);
		compensateVo.setPrpLLossItemVoList(prpLCompensateVo.getPrpLLossItems());
		compensateVo.setPrpLChargeVoList(prpLCompensateVo.getPrpLCharges());
		compensateVo.setPrpLLossPropVoList(prpLCompensateVo.getPrpLLossProps());
		compensateVo.setPrpLLossPersonVoList(prpLCompensateVo.getPrpLLossPersons());
		compensateVo.setTaskId(prpLWfTaskVo.getTaskId().toString());
		// 在诉讼案的情况下，协议金额=手工录入的分项赔款和-原理算计算金额。需要在理算完成后计算。
		compensateVo.setExgratiaFee(null);

		// 交强险主车车损理算记录，由于一辆车可能有多个定损任务，所以主车可能有多条理算记录。
		List<PrpLLossItemVo> mainCarCiPrpLlossList = new ArrayList<PrpLLossItemVo>();

		List<PrpLLossItemVo> prpLLossItemVoList = prpLCompensateVo.getPrpLLossItems();
		for(PrpLLossItemVo prpLLossItemVo:prpLLossItemVoList){
			if("1".equals(prpLLossItemVo.getItemId())){
				DefLossInfoOfA defLossInfoOfA = new DefLossInfoOfA();
				defLossInfoOfA.setPrpLthirdPartyId(prpLLossItemVo.getDlossId().toString());
				defLossInfoOfA.setSumLoss(prpLLossItemVo.getSumLoss());
				defLossInfoOfA.setRescueFee(prpLLossItemVo.getRescueFee());
				compensateVo.getCarDefLossInfoOfAList().add(defLossInfoOfA);
				// 如果是交强险。
				if( !this.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind())){
					mainCarCiPrpLlossList.add(prpLLossItemVo);
				}
			}
		}

		// 交强险主车财损理算记录，可能有多条理算记录。
		List<PrpLLossPropVo> mainCarCiPrpLpropLossList = new ArrayList<PrpLLossPropVo>();

		List<PrpLLossPropVo> prpLLossPropVoList = prpLCompensateVo.getPrpLLossProps();
		for(PrpLLossPropVo prpLLossPropVo:prpLLossPropVoList){
			if("1".equals(prpLLossPropVo.getItemId())){
				DefLossInfoOfA defLossInfoOfA = new DefLossInfoOfA();
				defLossInfoOfA.setPrpLthirdPartyId(prpLLossPropVo.getDlossId().toString());
				defLossInfoOfA.setSumLoss(prpLLossPropVo.getSumLoss());
				defLossInfoOfA.setRescueFee(prpLLossPropVo.getRescueFee());
				compensateVo.getPropDefLossInfoOfAList().add(defLossInfoOfA);
				// 如果是交强险。
				if( !this.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind())){
					mainCarCiPrpLpropLossList.add(prpLLossPropVo);
				}
			}
		}

		// 交强险主车人伤理算记录，可能有多条理算记录。
		List<PrpLLossPersonVo> mainCarCiPrpLpersonItemList = new ArrayList<PrpLLossPersonVo>();

		List<PrpLLossPersonVo> prpLLossPersonVoList = prpLCompensateVo.getPrpLLossPersons();
		for(PrpLLossPersonVo prpLLossPersonVo:prpLLossPersonVoList){
			if("1".equals(prpLLossPersonVo.getItemId())){
				DefLossInfoOfA defLossInfoOfA = new DefLossInfoOfA();
				defLossInfoOfA.setPrpLthirdPartyId(prpLLossPersonVo.getDlossId().toString());
				defLossInfoOfA.setSumLoss(prpLLossPersonVo.getSumLoss());
				// defLossInfoOfA.setRescueFee(prpLLossPersonVo.getRescueFee());
				compensateVo.getPersonDefLossInfoOfAList().add(defLossInfoOfA);
				mainCarCiPrpLpersonItemList.add(prpLLossPersonVo);
			}
		}

		// 如果是交强险，归类无责代赔对象。
		if( !this.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind())){
			for(PrpLLossItemVo prpLLossItemVo:prpLLossItemVoList){
				if(CodeConstants.PayFlagType.NODUTY_INSTEAD_PAY.equals(prpLLossItemVo.getPayFlag())){
					compensateVo.getDutyInsteadPayCarInfos().add(prpLLossItemVo);
				}
			}
			// 鼎和没有无责代赔财产信息，此处删除
			// for(PrpLpropLoss prpLpropLoss:prpLCompensateVo.getPrpLpropLosses()){
			// if(CodeConstants.NODUTY.equals(prpLpropLoss.getFeeTypeCode())){
			// compensateVo.getDutyInsteadPayPropInfos().add(prpLpropLoss);
			// }
			// }
		}

		// 查找本次立案的所有有效领款人
		// PrpLpayeePerson prpLpayeePersonQuery = new PrpLpayeePerson();
		// prpLpayeePersonQuery.setRegistNo(registNo);
		// prpLpayeePersonQuery.setValidFlag(CodeConstants.ValidFlag.Yes.value);
		// compensateVo.setPrpLpayeeInfos(this.payeePersonService.queryByExample(prpLpayeePersonQuery));

		PrpLCMainVo prpLcMainVo = policyViewService.getPrpLCMainByRegistNoAndPolicyNo(registNo,prpLCompensateVo.getPolicyNo());
		compensateVo.setPrpLCMainVo(prpLcMainVo);

		List<PrpLCItemKindVo> prpLCItemKindVoList = prpLcMainVo.getPrpCItemKinds();
		compensateVo.setPrpLCItemKindVoList(prpLCItemKindVoList);
		// 商业车损险的绝对免赔额。
		compensateVo.setDeductibleValue(prpLCItemKindVoList.get(0).getDeductible());

		compensateVo.setOperateDate(prpLcMainVo.getOperateDate());

		// compensateVo.setPrpldeductinfoList(this.deductInfoService.queryByRegistNo(registNo));

		for(PrpLCItemKindVo prpLcItemKindVo:prpLCItemKindVoList){
			if(this.isMKindCode(prpLcItemKindVo)){
				compensateVo.getIsMitemKindList().add(prpLcItemKindVo);
				MItemKindVo mItemKindVo = new MItemKindVo();
				mItemKindVo.setKindCode(prpLcItemKindVo.getKindCode());
				compensateVo.getmExceptKinds().add(mItemKindVo);
			}
		}
		// 初始化其中的金额，因为核赔环节需要显示不计免赔信息。
		List<MItemKindVo> mExceptKinds = compensateVo.getmExceptKinds();
		for(MItemKindVo vo:mExceptKinds){
			List<PrpLLossItemVo> prpLlosses = prpLCompensateVo.getPrpLLossItems();
			for(PrpLLossItemVo lossItem:prpLlosses){
				if(vo.getKindCode().equals(lossItem.getKindCode())){
					vo.setExceptKindDeductibleRate(lossItem.getDeductOffRate().doubleValue());
					vo.setExceptKindDeductible(MoneyFormator.format(
							new BigDecimal(lossItem.getDeductOffAmt().doubleValue()+vo.getExceptKindDeductible())).doubleValue());
				}
			}
			List<PrpLLossPropVo> prpLpropLosses = prpLCompensateVo.getPrpLLossProps();
			for(PrpLLossPropVo lossItem:prpLpropLosses){
				if(vo.getKindCode().equals(lossItem.getKindCode())){
					vo.setExceptKindDeductibleRate(lossItem.getDeductOffRate().doubleValue());
					vo.setExceptKindDeductible(lossItem.getDeductOffAmt().doubleValue()+vo.getExceptKindDeductible());
				}
			}
			List<PrpLLossPersonVo> prpLpersonItems = prpLCompensateVo.getPrpLLossPersons();
			for(PrpLLossPersonVo prpLpersonItem:prpLpersonItems){
				if(vo.getKindCode().equals(prpLpersonItem.getKindCode())){
					vo.setExceptKindDeductibleRate(prpLpersonItem.getDeductOffRate().doubleValue());
					vo.setExceptKindDeductible(prpLpersonItem.getDeductOffAmt().doubleValue()+vo.getExceptKindDeductible());
				}
				// List<PrpLLossPersonFeeVo> prpLpersonLosses = prpLpersonItem.getPrpLLossPersonFees();
				// for(PrpLLossPersonVo lossItem:prpLpersonLosses){
				// if(vo.getKindCode().equals(lossItem.getKindCode())){
				// vo.setExceptKindDeductibleRate(lossItem.getDeductOffRate().doubleValue());
				// vo.setExceptKindDeductible(lossItem.getDeductOffAmt().doubleValue()+vo.getExceptKindDeductible());
				// }
				// }
			}
		}

		// 查询主车实际价值。
		ThirdPartyDepreRate thirdPartyDepreRate = this.queryThirdPartyDepreRate(registNo);
		compensateVo.setThirdPartyDepreRate(thirdPartyDepreRate);

		for(PrpLLossItemVo prpLLossItemVo:prpLLossItemVoList){
			String payFlag = prpLLossItemVo.getPayFlag();
			if(payFlag.equals(CodeConstants.PayFlagType.COMPENSATE_PAY)){
				compensateVo.getPrpLLossItemVoZFList().add(prpLLossItemVo);
			}
			if(payFlag.equals(CodeConstants.PayFlagType.INSTEAD_PAY)){
				compensateVo.getPrpLLossItemVoZCList().add(prpLLossItemVo);
			}
			if(payFlag.equals(CodeConstants.PayFlagType.CLEAR_PAY)){
				compensateVo.getPrpLLossItemVoQFList().add(prpLLossItemVo);
			}
		}

		// 代付记录需要生成ThirdPartyRecoveryInfo。

		PrpLPlatLockVo prpLPlatLockVo = null;
		List<PrpLPlatLockVo> prpLPlatLockList = subrogationService.findByPrpLPlatLockVo("1",prpLCompensateVo.getRegistNo(),prpLRegistVo
				.getPrpLRegistExt().getLicenseNo(),CodeConstants.PolicyType.POLICY_DZA);

		if(prpLPlatLockList!=null&& !prpLPlatLockList.isEmpty()){
			PrpLDlossCarMainVo mainCar = null;
			PrpLDlossCarMainVo thirdCar = null;
			List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(prpLCompensateVo.getRegistNo());
			if(prpLDlossCarMainVoList!=null&& !prpLDlossCarMainVoList.isEmpty()){
				for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){
					if(prpLDlossCarMainVo.getSerialNo()==1){
						mainCar = prpLDlossCarMainVo;
						break;
					}
				}
			}
			for(PrpLPlatLockVo prpLPlatLock:prpLPlatLockList){
				// 责任对方车牌号
				String thirdCarLiceseNo = prpLPlatLock.getOppoentLicensePlateNo();
				for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){
					if(prpLDlossCarMainVo.getSerialNo()!=1&&thirdCarLiceseNo.equals(prpLDlossCarMainVo.getLicenseNo())){
						thirdCar = prpLDlossCarMainVo;
						break;
					}
				}
				ThirdPartyRecoveryInfo info = new ThirdPartyRecoveryInfo();
				info.setMainCarLicenseNo(mainCar.getId().toString());
				info.setMainCarLossName(mainCar.getLicenseNo());
				info.setThirdCarlicenseNo(thirdCar.getId().toString());
				info.setRecoverySumRealPay(BigDecimal.ZERO);

				compensateVo.getThirdPartyRecoveryInfoList().add(info);
			}
		}

		return compensateVo;
	}

	private boolean isMKindCode(PrpLCItemKindVo prpLCItemKindVo) {
		// 是否是新条款险别
		boolean flag = false;
		boolean isNewClauseCode = CodeConstants.ISNEWCLAUSECODE_MAP.get(prpLCItemKindVo.getRiskCode());
		if(isNewClauseCode){
			if(prpLCItemKindVo.getKindCode().trim().endsWith("M")){
				flag = true;
			}
		}else{
			if("1".equals(prpLCItemKindVo.getFlag().charAt(4))){
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 获取免赔率
	 * 
	 * <pre></pre>
	 * @param compensateVo
	 * @param prpLclaim
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月28日 下午4:49:38): <br>
	 */
	public CompensateVo executeRulesDeductRate(CompensateVo compensateVo,PrpLClaimVo prpLClaimVo) {
		this.calLossItem(compensateVo,compensateVo.getPrpLLossItemVoList());

		this.calLossItem(compensateVo,compensateVo.getPrpLLossPropVoList());

		// this.calLossItem(compensateVo,compensateVo.getPrpLLossPersonVoList());

		List<PrpLLossPersonVo> prpLLossPersonVoList = compensateVo.getPrpLLossPersonVoList();
		for(PrpLLossPersonVo prpLLossPersonVo:prpLLossPersonVoList){
			// prpLLossPersonVo.setIndentityDutyRate(compensateVo.getPrpLCompensateVo().getIndemnityDutyRate());
			List<PrpLLossPersonFeeVo> prpLpersonFees = prpLLossPersonVo.getPrpLLossPersonFees();
			for(PrpLLossPersonFeeVo perFeeVo:prpLpersonFees){
				perFeeVo.setDutyRate(prpLLossPersonVo.getDutyRate());
			}

			this.calLossItem(compensateVo,prpLpersonFees);

			prpLLossPersonVo.setDeductDutyRate(prpLpersonFees.get(0).getDeductDutyRate());
			prpLLossPersonVo.setDeductibleRate(prpLpersonFees.get(0).getDeductibleRate());
			prpLLossPersonVo.setDeductAddRate(prpLpersonFees.get(0).getDeductAddRate());
			// prpLLossPersonVo.setDeductAbsRate(prpLpersonFees.get(0).getDeduct);
			prpLLossPersonVo.setDeductOffRate(prpLpersonFees.get(0).getDeductOffRate());
			prpLLossPersonVo.setQuantity(prpLpersonFees.get(0).getQuantity());
			prpLLossPersonVo.setItemAmount(prpLpersonFees.get(0).getItemAmount());
		}

		// this.calLossItem(compensateVo,compensateVo.getPrpLCompensateVo().getPrplotherlosses());

		return compensateVo;
	}

	/**
	 * 通过险别计算器，对损失项进行计算。
	 */
	private void calLossItem(CompensateVo compensateVo,List lossItemList) {

		for(Object lossItem:lossItemList){

			Long start = System.currentTimeMillis();
			KindCalculator kc = CalculatorFactory.makeCalculator(compensateVo,lossItem,this);

			kc.calItemValue(null);
			kc.calAmount(null);
			// 商业险责任比例
			kc.calIndemnityDutyRate((BigDecimal)Refs.get(lossItem,"dutyRate"));
			// 事故责任免赔率 或绝对免赔率
			kc.calDutyDeductibleRate(null);
			// 可选免赔率
			kc.calSelectDeductibleRate(null);
			kc.calExceptDeductRate(null);
			// kc.calDepreRate(null);
			kc.calDeductibleRate(null);

			String kindCode = kc.getKindCode();
			logger.info("kindCode = "+kindCode);
			boolean ishasKindCode = false;
			for(MItemKindVo mitenKindVo:compensateVo.getmExceptKinds()){
				if(mitenKindVo.getKindCode().equals(kindCode)){
					ishasKindCode = true;
					break;
				}
			}

			if( !ishasKindCode){
				MItemKindVo vo = new MItemKindVo();
				vo.setKindCode(kindCode);
				vo.setExceptKindDeductibleRate(kc.calExceptDeductRate(null).doubleValue());
				compensateVo.getmExceptKinds().add(vo);
			}

			// compensateVo.getKindCalculatorList().add(kc);

			Long end = System.currentTimeMillis();
			System.out.println(( end-start )/1000);
		}
	}

	/**
	 * 计算车辆实际价值
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @param prpLCMainVo
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月28日 下午3:53:36): <br>
	 */
	public ThirdPartyDepreRate queryThirdPartyDepreRate(String registNo) {
		ThirdPartyDepreRate thirdPartyDepreRate = new ThirdPartyDepreRate();
		double purchasePrice = 0;// 新车购置价
		double depreMonthRate = 0;// 月折旧率
		int depreMon = 0;// 已使用月数
		double actualValue = 0;// 出险时实际价值

		// standardPrice基准价
		// PrpLCItemCar表 PurchasePrice 新件购置价 = 基准价
		// PrpLCItemCarVo cItemCar = registQueryService.findCItemCarByRegistNo(registNo);
		List<PrpLCMainVo> prpLcMainList = policyViewService.findPrpLCMainVoListByRegistNo(registNo);
		PrpLCMainVo prpLCMainVo = null;
		if(prpLcMainList!=null&& !prpLcMainList.isEmpty()){
			if(prpLcMainList.size()==1){
				prpLCMainVo = prpLcMainList.get(0);
			}else{
				for(PrpLCMainVo cMainVo:prpLcMainList){
					if( !Risk.isDQZ(cMainVo.getRiskCode())){
						prpLCMainVo = cMainVo;
						break;
					}
				}
			}
		}

		PrpLCItemCarVo cItemCar = prpLCMainVo.getPrpCItemCars().get(0);

		String carKindCodeCal = "";
		String carKindCode = cItemCar.getCarKindCode();
		String useNatureCode = cItemCar.getUseNatureCode();// 车辆性质
		String riskCode = cItemCar.getRiskCode();
		Long seatCountShow = ( cItemCar.getSeatCount()!=null ? cItemCar.getSeatCount().longValue() : 0 );
		Double tonCountShow = ( cItemCar.getTonCount()!=null ? cItemCar.getTonCount().doubleValue() : 0 );

		if(Risk.isDAA(riskCode)||Risk.isDBA(riskCode)||Risk.isDAF(riskCode)){
			if("8A".equals(useNatureCode)){
				if(seatCountShow<10){
					carKindCodeCal = "01";// 家庭用车 9座以下
				}else{
					carKindCodeCal = "04";// 家庭用车 10座以上
				}
			}else if("8B".equals(useNatureCode)||"8C".equals(useNatureCode)||"8D".equals(useNatureCode)){
				if("A0".equals(carKindCode)&&seatCountShow<10){
					carKindCodeCal = "02";// 9座以下客车
				}else if("H1".equals(carKindCode)){
					carKindCodeCal = "03";// 低速载货汽车
				}else{
					carKindCodeCal = "04";// 其他
				}
			}else if("9A".equals(useNatureCode)){// 租赁
				if("A0".equals(carKindCode)){
					carKindCodeCal = "05";// 客车
				}else if("H0".equals(carKindCode)&&tonCountShow<3.5*1000){
					carKindCodeCal = "06";// 微型载货汽车
				}
				/*
				 * 带拖挂的载货汽车 是指那种带牵引头的 它是有动力装置可以跑的,货车挂车 不带牵引头 是挂在带牵引头的后面 自己没动力装置
				 * 带拖挂的载货汽车 深圳机构的是要按特4出单  其它机构的按核定载质对应的吨位或直接按10吨以上出
				 * 货车挂车不会出这种带牵引的
				 */
				// else if("G0".equals(carKindCode)){
				// carKindCodeCal = "07";// 拖挂汽车
				// }
				else if("H1".equals(carKindCode)){
					carKindCodeCal = "08";// 低速载货汽车
				}else{
					carKindCodeCal = "09";// 其他
				}
			}else if("9B".equals(useNatureCode)||"9C".equals(useNatureCode)||"9D".equals(useNatureCode)){
				if("A0".equals(carKindCode)){
					carKindCodeCal = "10";// 客车
				}else if("H0".equals(carKindCode)&&tonCountShow<3.5*1000){
					carKindCodeCal = "11";// 微型载货汽车
				}
				/*
				 * 带拖挂的载货汽车 是指那种带牵引头的 它是有动力装置可以跑的,货车挂车 不带牵引头 是挂在带牵引头的后面 自己没动力装置
				 * 带拖挂的载货汽车 深圳机构的是要按特4出单  其它机构的按核定载质对应的吨位或直接按10吨以上出
				 * 货车挂车不会出这种带牵引的
				 */
				// else if("G0".equals(carKindCode)){
				// carKindCodeCal = "12";// 拖挂汽车
				// }
				else if("H1".equals(carKindCode)){
					carKindCodeCal = "13";// 低速载货汽车
				}else{
					carKindCodeCal = "14";// 其他
				}
			}else{
				carKindCodeCal = "99";// 其他
			}
		}else if(Risk.isDAE(riskCode)||Risk.isDBE(riskCode)){
			carKindCodeCal = "15";
		}else if(Risk.isDAC(riskCode)||Risk.isDBC(riskCode)){
			if("9A".equals(useNatureCode)){
				carKindCodeCal = "62";
			}else{
				carKindCodeCal = "60";
			}
		}

		thirdPartyDepreRate.setLicenseNo(cItemCar.getLicenseNo());
		purchasePrice = cItemCar.getPurchasePrice().doubleValue();
		// depreMonthRate月折旧 获取 = 月折旧率
		PrpDDeprecateRateVo deprecateRateVo = configService.findDeprecateRate(carKindCodeCal,cItemCar.getRiskCode(),cItemCar.getClauseType());
		if(deprecateRateVo!=null&&deprecateRateVo.getPerMonthRate()!=null){
			depreMonthRate = deprecateRateVo.getPerMonthRate().doubleValue();
		}

		// depreMonths折旧时间-提交后赋值
		// 出险时间（PrpLRegist表DamageTime）-保险起期(PrpLCMain表StartDate DATE 起保时间) = 折旧时间
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);

		depreMon = calMonthDiv(registVo.getDamageTime(),prpLCMainVo.getStartDate());
		// 被保险机动车出险时的实际价值＝ 出险时新车购置价×（1－已使用月数×月折旧率）；
		actualValue = purchasePrice*( 1-depreMon*depreMonthRate );

		thirdPartyDepreRate.setPurchasePrice(purchasePrice);
		thirdPartyDepreRate.setActualValue(actualValue);
		thirdPartyDepreRate.setDepreRate(depreMonthRate);
		thirdPartyDepreRate.setUseMonths(depreMon);
		thirdPartyDepreRate.setCarKindCode(cItemCar.getCarKindCode());
		thirdPartyDepreRate.setClauseType(cItemCar.getClauseType());
		thirdPartyDepreRate.setSearCount(cItemCar.getSeatCount());

		if( !Risk.isDQZ(prpLCMainVo.getRiskCode())&& !CodeConstants.ISNEWCLAUSECODE_MAP.get(prpLCMainVo.getRiskCode())){
			// 新增设备MAP key deviceId,value 0 deviceName,1 新设备购置价 2 出险时新设备实际价值
			Map<Long,String[]> deviceMap = new HashMap<Long,String[]>();
			List<PrpLcCarDeviceVo> deviceVoList = prpLCMainVo.getPrpLcCarDevices();
			if(deviceVoList!=null&& !deviceVoList.isEmpty()){
				for(PrpLcCarDeviceVo deviceVo:deviceVoList){
					String[] array = new String[3];

					array[0] = deviceVo.getDeviceName();
					array[1] = deviceVo.getPurChasePrice().toString();
					double deviceActualValue = deviceVo.getPurChasePrice().doubleValue()*( 1-depreMon*depreMonthRate );
					array[2] = deviceActualValue+"";

					deviceMap.put(deviceVo.getDeviceId(),array);
				}
			}
			thirdPartyDepreRate.setDeviceMap(deviceMap);
		}

		return thirdPartyDepreRate;
	}

	/**
	 * 获取两个时间区间的月份
	 * @param start
	 * @param end
	 * @return
	 */
	private int calMonthDiv(Date start,Date end) {
		if(start.after(end)){
			Date t = start;
			start = end;
			end = t;
		}
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(start);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(end);
		Calendar temp = Calendar.getInstance();
		temp.setTime(end);
		temp.add(Calendar.DATE,1);

		int year = endCalendar.get(Calendar.YEAR)-startCalendar.get(Calendar.YEAR);
		int month = endCalendar.get(Calendar.MONTH)-startCalendar.get(Calendar.MONTH);
		// PNCCAR,2010-07-30,LIFUBO,ADD,BEGIN
		int day = endCalendar.get(Calendar.DATE)-startCalendar.get(Calendar.DATE);
		// PNCCAR,2010-07-30,LIFUBO,ADD,END

		if(( startCalendar.get(Calendar.DATE)==1 )&&( temp.get(Calendar.DATE)==1 )){
			return year*12+month+1;
		}else if(( startCalendar.get(Calendar.DATE)!=1 )&&( temp.get(Calendar.DATE)==1 )){
			return year*12+month;
		}else if(( startCalendar.get(Calendar.DATE)==1 )&&( temp.get(Calendar.DATE)!=1 )){
			return year*12+month;
		}else{
			// PNCCAR,2010-07-30,LIFUBO,ADD,BEGIN
			if(day<0){
				return ( year*12+month-1 )<0 ? 0 : ( year*12+month-1 );
			}
			// PNCCAR,2010-07-30,LIFUBO,ADD,END
			return ( year*12+month-1 )<0 ? 0 : ( year*12+month );
		}
	}

	/**
	 * 商业险试算
	 */
	public CompensateVo calCulator(PrpLCompensateVo prpLCompensateVo,List<PrpLLossItemVo> prpLLossItemVoList,List<PrpLLossPropVo> prpLLossPropVoList,
									List<PrpLLossPersonVo> prpLLossPersonVoList) {

		PrpLClaimVo prpLClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(prpLCompensateVo.getRegistNo(),prpLCompensateVo.getPolicyNo());
		CompensateVo compensateVo = calculatorService.orgnizeCompensateData(prpLCompensateVo,prpLLossItemVoList,prpLLossPropVoList,
				prpLLossPersonVoList,prpLClaimVo,CodeConstants.CompensateKind.BI_COMPENSATE);
		CompensateVo returnCompensateList = this.orgnizeBicompensateData(compensateVo,prpLClaimVo);

		return returnCompensateList;
	}

	// 对应人保方法compensateBIRuleData。商业险试算
	public CompensateVo orgnizeBicompensateData(CompensateVo compensateVo,PrpLClaimVo prpLClaimVo) {
		Boolean cprcCase = isCprcCase(prpLClaimVo.getRegistNo());
		compensateVo.setIsMitemKindList(this.getIsMitemKinds(compensateVo.getPrpLCItemKindVoList(),cprcCase));
		// 2.获取免赔率,并将免赔率结果组织到Loss中,并放入CompensateVo返回,以供商业试算.
		this.executeRulesDeductRate(compensateVo,prpLClaimVo);

		List<CompensateExp> compensateExpAry = this.organizeCompensateExp(compensateVo,prpLClaimVo,cprcCase);
		List compensateResult = this.executeRulesCompensate(compensateExpAry,cprcCase);
		compensateVo = this.rebuildBiCompensateVo(compensateVo,compensateResult);
		return compensateVo;
	}

	// 组织CompensateExp数据
	public List<CompensateExp> organizeCompensateExp(CompensateVo compensateVo,PrpLClaimVo prpLClaimVo,boolean cprcCase) {
		String registNo = prpLClaimVo.getRegistNo();
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
		List<CompensateExp> compensateExpAry = new ArrayList<CompensateExp>(0);
		PrpLCompensateVo prpLCompensateVo = compensateVo.getPrpLCompensateVo();
		List<PrpLLossItemVo> prpLLossItemVoList = prpLCompensateVo.getPrpLLossItems(); // 所有理算车辆损失项信息
		List<PrpLLossPropVo> prpLLossPropVoList = prpLCompensateVo.getPrpLLossProps(); // 财产损失项信息
		List<PrpLLossPersonVo> prpLLossPersonVoList = prpLCompensateVo.getPrpLLossPersons();
		ThirdPartyDepreRate thirdPartyDepreRate = compensateVo.getThirdPartyDepreRate();
		boolean isZcCompensate = this.isZcCompensate(prpLCompensateVo);

		int index = 0;
		boolean deductibleKindAFlag = false; // 车损险是否扣减了免赔额
		// 1.车辆,List<CompensateExp>对象组织前的准备：获取到所有损失项的KindCode，一个险别对应一个CompensateExp对象
		for(PrpLLossItemVo prpLLossItemVo:prpLLossItemVoList){
			CompensateExp compensateExp = new CompensateExp();
			compensateExp.setRiskCode(prpLClaimVo.getRiskCode());
			compensateExp.setKindCode(prpLLossItemVo.getKindCode());
			if(CodeConstants.KINDCODE.KINDCODE_G.equals(prpLLossItemVo.getKindCode())){
				compensateExp.setLossFeeType(prpLLossItemVo.getLossFeeType());
			}

			compensateExp.setLicenseNo(prpLLossItemVo.getItemName()); // 车牌号码
			// compensateExp.setLossName(prpLLossItemVo.getLossName()); // 损失项名称
			compensateExp.setRecoveryOrPayFlag(prpLLossItemVo.getPayFlag()); // 损失项代位类型：1代位,2清付,3自付
			compensateExp.setLossQuantity(1); // 损失项数量，如果非手工增加的其他损失，则默认为1
			compensateExp.setExpType(CodeConstants.ExpType.CAR); // 损失类型：车/财/人/其他
			if("3".equals(prpLCompensateVo.getCaseType())){
				compensateExp.setSubrogationFlag("1");
			}

			// 安联计算书不分代位类型，与周文确认如果第一张商业计算书（只有第一张商业计算书有代位理算项）置为“1”，其余“0”。
			if(isZcCompensate){
				compensateExp.setPayType("1");
			}else{
				compensateExp.setPayType("0");
			}
			if(prpLLossItemVo.getPayFlag().equals(CodeConstants.PayFlagType.CLEAR_PAY)||prpLLossItemVo.getPayFlag().equals(
					CodeConstants.PayFlagType.INSTEAD_PAY)){
				// 第三者已赔付金额 在代位计算追偿金额处扣减
				compensateExp.setThirdPaidAmt(DataUtils.NullToZero(prpLLossItemVo.getThirdPaidAmt()).doubleValue());
			}
			if(prpLLossItemVo.getPayFlag().equals(CodeConstants.PayFlagType.CLEAR_PAY)){
				// 清付金额=核损费用 + 施救费 - 残值 - 其他扣除(鼎和的核损已经扣除残值了)。
				compensateExp.setRecoveryPay(prpLLossItemVo.getSumLoss().doubleValue()+prpLLossItemVo.getRescueFee().doubleValue());
				// compensateExp.setRecoveryPay(prpLLossItemVo.getSumLoss().doubleValue() +
				// prpLLossItemVo.getRescueFee().doubleValue() -
				// prpLLossItemVo.getRemnantfee().doubleValue() -
				// prpLLossItemVo.getOtherDeductFee().doubleValue());
			}

			// compensateExp.setCetainLossType(prpLDlossCarMainVo.getCetainLossType());
			if("1".equals(prpLLossItemVo.getOutRiskType())){
				compensateExp.setOppoentCoverageType(CodeConstants.PolicyType.POLICY_DZA); // 代位类型
			}else{
				compensateExp.setOppoentCoverageType(CodeConstants.PolicyType.POLICY_DZA); // 代位类型
			}
			// compensateExp.setSumRest(prpLLossItemVo.getRemnantfee().doubleValue()); // 残值
			compensateExp.setOtherDeductFee(prpLLossItemVo.getOtherDeductAmt().doubleValue());// 其他扣除
			if(prpLLossItemVo.getOffPreAmt()!=null){
				compensateExp.setSumPrePay(prpLLossItemVo.getOffPreAmt().doubleValue());
			}else{
				compensateExp.setSumPrePay(0.00);
			}
			compensateExp.setRescueFee(DataUtils.NullToZero(prpLLossItemVo.getRescueFee()).doubleValue()); // 施救费
			if(prpLLossItemVo.getBzPaidLoss()!=null){
				compensateExp.setSumLossBZPaid(prpLLossItemVo.getBzPaidLoss().doubleValue()); // 定损交强已赔付
			}else{
				compensateExp.setSumLossBZPaid(0.00);
			}

			if(prpLLossItemVo.getBzPaidRescueFee()!=null){
				compensateExp.setRescueFeeBZPaid(prpLLossItemVo.getBzPaidRescueFee().doubleValue()); // 施救费交强已赔付
			}else{
				compensateExp.setRescueFeeBZPaid(0.00);
			}
			compensateExp.setbZPaid(compensateExp.getSumLossBZPaid()+compensateExp.getRescueFeeBZPaid()); // 总交强已赔付
			// 实际价值减去 已赔付金额
			Map<String,BigDecimal> kindPaidMap = compensateVo.getKindPaidMap();
			BigDecimal sumRealPay4Kind = BigDecimal.ZERO;
			if(kindPaidMap!=null&& !kindPaidMap.isEmpty()){
				sumRealPay4Kind = kindPaidMap.get(CodeConstants.KINDCODE.KINDCODE_A);
			}
			compensateExp.setDamageItemRealCost(thirdPartyDepreRate.getActualValue()-DataUtils.NullToZero(sumRealPay4Kind).doubleValue()); // 车辆实际价值
			compensateExp.setEntryItemCarCost(thirdPartyDepreRate.getPurchasePrice()); // 保险车辆新车购置价
			compensateExp.setDeviceActualValue(thirdPartyDepreRate.getDeviceActualValue()); // 设备实际价值
			compensateExp.setDevicePurchasePrice(thirdPartyDepreRate.getDevicePurchasePrice()); // 新设备购置价
			compensateExp.setAbsolvePayAmt(DataUtils.NullToZero(prpLLossItemVo.getAbsolvePayAmt()).doubleValue());
			compensateExp.setUseMonths(thirdPartyDepreRate.getUseMonths()); // 使用月数
			compensateExp.setDepreRate(thirdPartyDepreRate.getDepreRate()); // 折旧率
			compensateExp.setDamageDate(prpLRegistVo.getDamageTime()); // 出险日期
			compensateExp.setOperateDate(compensateVo.getOperateDate()); // 承保日期,prpLcMain.operateDate
			compensateExp.setCompensateType(Integer.parseInt(CodeConstants.CompensateKind.BI_COMPENSATE)); // 计算书类型,商业
			// compensateExp.setIsSuitFlag(this.isSuitCase(prpLCompensateVo));

			compensateExp.setDeductFee(DataUtils.NullToZero(compensateVo.getDeductibleValue()).doubleValue()); // 商业车损险免赔额
			if(CodeConstants.KINDCODE.KINDCODE_A.equals(prpLLossItemVo.getKindCode())){
				if(compensateVo.isDeductibleFlag()){
					compensateExp.setDeductFee(0d);
				}else{
					deductibleKindAFlag = true;
				}
			}

			// compensateExp.setClaimType(prpLCompensateVo.getClaimType()); // 赔案类别
			compensateExp.setIndemnityDutyRate(prpLLossItemVo.getDutyRate().doubleValue()/100); // 险别事故责任比例
			if(prpLLossItemVo.getDeductDutyRate().doubleValue()>0){
				compensateExp.setDutyDeductibleRate(prpLLossItemVo.getDeductDutyRate().doubleValue()/100); // 事故责任免赔率
			}else{
				compensateExp.setDutyDeductibleRate(prpLLossItemVo.getDeductAbsRate().doubleValue()/100); // 绝对免赔率
			}

			compensateExp.setSelectDeductibleRate(prpLLossItemVo.getDeductAddRate().doubleValue()/100); // 可选免赔率
			compensateExp.setDeductibleRate(prpLLossItemVo.getDeductibleRate().doubleValue()/100);
			// double deductOffRate = this.deductOffRate(registNo,prpLLossItemVo.getKindCode(),prpLLossItemVo.getDeductDutyRate(),prpLLossItemVo.getDeductAddRate());
			// compensateExp.setExceptDeductRate(deductOffRate); // 不计免赔率
			compensateExp.setExceptDeductRate(prpLLossItemVo.getDeductOffRate().doubleValue()/100);
			// compensateExp.setThirdCarDutyRate(prpLLossItemVo.getDutyRate().doubleValue()); //如果是代位案件，PrpLloss.indemnityDutyRate就是对方比例。
			compensateExp.setIndex(index++ ); // 对象索引
			compensateExp.setLossItem(prpLLossItemVo);
			// 代位案件需要从车辆中设置对方商业险责任比例。
			if(prpLLossItemVo.getPayFlag().equals(CodeConstants.PayFlagType.INSTEAD_PAY)){
				compensateExp.setThirdCarDutyRate(prpLLossItemVo.getDutyRate().doubleValue()/100);
			}else{
				compensateExp.setThirdCarDutyRate(0d);
			}
			// 无法找到第三方 特殊处理
			if(CodeConstants.KINDCODE.KINDCODE_A.equals(prpLLossItemVo.getKindCode())||CodeConstants.KINDCODE.KINDCODE_X.equals(prpLLossItemVo
					.getKindCode())){

				compensateExp.setNoFindThirdFlag("0");
				compensateExp.setKindCodeNTFlag("0");;
				List<PrpLClaimDeductVo> claimDeductList = compensateVo.getClaimDeductList();
				if(claimDeductList!=null&& !claimDeductList.isEmpty()){
					for(PrpLClaimDeductVo claimDeductVo:claimDeductList){
						if("120".equals(claimDeductVo.getDeductCondCode())||"320".equals(claimDeductVo.getDeductCondCode())){
							compensateExp.setNoFindThirdFlag("1");
							break;
						}
					}
				}
				if(cprcCase){
					for(PrpLCItemKindVo itemKindVo:compensateVo.getPrpLCItemKindVoList()){
						if(CodeConstants.KINDCODE.KINDCODE_NT.equals(itemKindVo.getKindCode())){
							compensateExp.setKindCodeNTFlag("1");
							break;
						}
					}
				}
			}

			// 与周文确认，不计免赔都纳入赔款。
			compensateExp.setFlagOfM("1"); // 是否将不计免赔率纳入赔款 1,是;0,否
			compensateExp.setFeeTypeName(codeTranService.transCode("LossTypeCar",prpLLossItemVo.getLossType()));

			compensateExp.setCheckPolicyM("0");
			List<PrpLCItemKindVo> isMitemKinds = compensateVo.getIsMitemKindList();
			if(isMitemKinds!=null&& !isMitemKinds.isEmpty()){
				for(PrpLCItemKindVo prpLcItemKindVo:isMitemKinds){
					if(prpLcItemKindVo.getKindCode().equals(prpLLossItemVo.getKindCode())){
						compensateExp.setCheckPolicyM("1");
						break;
					}
				}
			}

			if("1".equals(compensateExp.getIsSuitFlag())){
				List<MItemKindVo> mExceptKinds = compensateVo.getmExceptKinds();
				for(MItemKindVo mItemKindVo:mExceptKinds){
					if(compensateExp.getKindCode().equals(mItemKindVo.getKindCode())){
						compensateExp.setSumRealPayJud(mItemKindVo.getExceptKindDeductible());
					}
				}
			}

			compensateExp.setAmount(prpLLossItemVo.getItemAmount().doubleValue());
			compensateExp.setDamageAmount(prpLLossItemVo.getItemAmount().doubleValue());
			compensateExp.setUnitAmount(0d);
			compensateExp.setQuantity(0d);

			// 商业车损险保额取保单保额，用于后续计算是否足额投保。
			// PrpLCItemKindVo prpLcItemKindVo = this.queryPrpLcItemKind4KindCode(compensateExp.getKindCode(),prpLCompensateVo);
			// double amt = prpLcItemKindVo.getAmount().doubleValue();
			// if(CodeConstants.KINDCODE.KINDCODE_A.equals(compensateExp.getKindCode())){
			// compensateExp.setAmount(amt);
			// }

			if(CodeConstants.PayFlagType.INSTEAD_PAY.equals(compensateExp.getRecoveryOrPayFlag())&&CodeConstants.PolicyType.POLICY_DZA
					.equals(compensateExp.getOppoentCoverageType())){
				List<ThirdPartyRecoveryInfo> thirdPartyRecoveryInfoList = compensateVo.getThirdPartyRecoveryInfoList();
				for(ThirdPartyRecoveryInfo recoveryInfo:thirdPartyRecoveryInfoList){
					// 被代位车辆ID。
					Long thirdCarId = Long.valueOf(recoveryInfo.getThirdCarlicenseNo());
					// PrpLthirdParty thirdPartyCar = this.thirdPartyService.findPrpLthirdPartyByPK(thirdCarId);
					PrpLDlossCarMainVo thirdPartyCar = lossCarService.findLossCarMainById(thirdCarId);
					if(thirdPartyCar.getLicenseNo().trim().equals(compensateExp.getLicenseNo().trim())){
						compensateExp.setLockedBzRealPay(recoveryInfo.getRecoverySumRealPay().doubleValue());

						// TODO start。待修改，目前交强规则还没有完成，所以在代付交强险损失项时，可以将这个字段存入总的交强赔付金额。
						compensateExp.setLockedBzRealPay(prpLLossItemVo.getBzPaidLoss().doubleValue()+prpLLossItemVo.getBzPaidRescueFee()
								.doubleValue());
						recoveryInfo.setRecoverySumRealPay(new BigDecimal(prpLLossItemVo.getBzPaidLoss().doubleValue()+prpLLossItemVo
								.getBzPaidRescueFee().doubleValue()));
						// TODO end。待修改，目前交强规则还没有完成，所以在代付交强险损失项时，可以将这个字段存入总的交强赔付金额。

						break;
					}
				}
			}else{
				compensateExp.setLockedBzRealPay(0d);
			}

			PrpLDlossCarMainVo prpLDlossCarMainVo = lossCarService.findLossCarMainById(prpLLossItemVo.getDlossId());
			if(CodeConstants.CetainLossType.DEFLOSS_ALL.equals(prpLDlossCarMainVo.getCetainLossType())){
				// 此处原始代码已经注销 AJ
				// 主车车损险全损，理算时的损失金额取保额。
				// if(ClaimConst.ClaimSF.SF700.value.equals(compensateExp.getKindCode())){
				// compensateExp.setSumLoss(this.queryPrpLcItemKind4KindCode(ClaimConst.ClaimSF.SF700.value , prpLcompensate).getAmount().doubleValue()); // 承保保额
				// }
				// else{
				// compensateExp.setSumLoss(prpLloss.getLossFee().doubleValue()); // 核定损失
				// }
				// 逻辑修改为：推定全损，核损金额小于保额时取核损金额；核损金额大于等于保额时取保额。
				if(CodeConstants.KINDCODE.KINDCODE_A.equals(compensateExp.getKindCode())||CodeConstants.KINDCODE.KINDCODE_G.equals(compensateExp
						.getKindCode())||CodeConstants.KINDCODE.KINDCODE_Z.equals(compensateExp.getKindCode())){
					BigDecimal amount = prpLLossItemVo.getItemAmount();
					if(prpLLossItemVo.getSumLoss().compareTo(amount)<=0){
						compensateExp.setSumLoss(prpLLossItemVo.getSumLoss().doubleValue());
					}else{
						compensateExp.setSumLoss(amount.doubleValue());
					}
				}else{// 三者车
					compensateExp.setSumLoss(prpLLossItemVo.getSumLoss().doubleValue()); // 核定损失
				}
			}else{
				compensateExp.setSumLoss(prpLLossItemVo.getSumLoss().doubleValue()); // 核定损失
			}
			compensateExp.setRegistNo(registNo);

			compensateExpAry.add(compensateExp);
		}

		// 2.财产,List<CompensateExp>对象组织前的准备：获取到所有损失项的KindCode，一个险别对应一个CopensateExp对象
		for(PrpLLossPropVo prpLLossPropVo:prpLLossPropVoList){
			if("1".equals(prpLLossPropVo.getPropType())){
				CompensateExp compensateExp = new CompensateExp();
				compensateExp.setRiskCode(prpLClaimVo.getRiskCode());
				compensateExp.setKindCode(prpLLossPropVo.getKindCode().trim());
				// compensateExp.setFeeTypeCode(prpLLossPropVo.getFeeTypeCode()); // 损失类型代码
				int serialNo = propTaskService.findPropMainVoById(prpLLossPropVo.getDlossId()).getSerialNo();
				List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(prpLCompensateVo.getRegistNo());
				if(prpLDlossCarMainVoList!=null&& !prpLDlossCarMainVoList.isEmpty()){
					for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){
						if(prpLDlossCarMainVo.getSerialNo()==serialNo){
							compensateExp.setLicenseNo(prpLDlossCarMainVo.getLicenseNo()); // 车牌号码
							break;
						}
					}
				}
				compensateExp.setRecoveryOrPayFlag(CodeConstants.PayFlagType.COMPENSATE_PAY); // 损失项代位类型：1代位,2清付,3自付
				compensateExp.setLossQuantity(1); // 损失项数量，如果非手工增加的其他损失，则默认为1
				compensateExp.setExpType(CodeConstants.ExpType.PROP); // 损失类型：车/财/人/其他

				// 财产不代位，所以都是自付损失项。
				compensateExp.setPayType(CodeConstants.PayFlagType.COMPENSATE_PAY);
				// 财产不代位，所以责任对方追偿金额都是0。
				compensateExp.setRecoveryPay(0d);

				// compensateExp.setSumRest(prpLLossPropVo.getRemnantfee().doubleValue()); // 残值
				compensateExp.setOtherDeductFee(prpLLossPropVo.getOtherDeductAmt().doubleValue());// 其他扣除
				compensateExp.setSumLoss(prpLLossPropVo.getSumLoss().doubleValue()); // 核定损失
				if(prpLLossPropVo.getOffPreAmt()==null){
					prpLLossPropVo.setOffPreAmt(BigDecimal.ZERO);
				}
				compensateExp.setSumPrePay(prpLLossPropVo.getOffPreAmt().doubleValue());
				compensateExp.setRescueFee(prpLLossPropVo.getRescueFee().doubleValue()); // 施救费
				compensateExp.setSumLossBZPaid(prpLLossPropVo.getBzPaidLoss().doubleValue()); // 定损交强已赔付
				compensateExp.setRescueFeeBZPaid(prpLLossPropVo.getBzPaidRescueFee().doubleValue()); // 施救费交强已赔付
				compensateExp.setbZPaid(compensateExp.getSumLossBZPaid()+compensateExp.getRescueFeeBZPaid()); // 总交强已赔付
				// 实际价值减去 已赔付金额
				Map<String,BigDecimal> kindPaidMap = compensateVo.getKindPaidMap();
				BigDecimal sumRealPay4Kind = BigDecimal.ZERO;
				if(kindPaidMap!=null&& !kindPaidMap.isEmpty()){
					sumRealPay4Kind = kindPaidMap.get(CodeConstants.KINDCODE.KINDCODE_A);
				}
				compensateExp.setDamageItemRealCost(thirdPartyDepreRate.getActualValue()-DataUtils.NullToZero(sumRealPay4Kind).doubleValue()); // 车辆实际价值
				compensateExp.setEntryItemCarCost(thirdPartyDepreRate.getPurchasePrice()); // 保险车辆新车购置价
				compensateExp.setDeviceActualValue(thirdPartyDepreRate.getDeviceActualValue()); // 设备实际价值
				compensateExp.setDevicePurchasePrice(thirdPartyDepreRate.getDevicePurchasePrice()); // 新设备购置价
				compensateExp.setUseMonths(thirdPartyDepreRate.getUseMonths()); // 使用月数
				compensateExp.setDepreRate(thirdPartyDepreRate.getDepreRate()); // 折旧率
				compensateExp.setDamageDate(prpLRegistVo.getDamageTime()); // 出险日期
				compensateExp.setOperateDate(compensateVo.getOperateDate()); // 承保日期,prpLcMain.operateDate
				compensateExp.setCompensateType(Integer.parseInt(CodeConstants.CompensateKind.BI_COMPENSATE)); // 计算书类型,商业
				// compensateExp.setIsSuitFlag(this.isSuitCase(prpLCompensateVo));
				if(compensateVo.getDeductibleValue()==null){
					compensateVo.setDeductibleValue(BigDecimal.ZERO);
				}
				compensateExp.setDeductFee(compensateVo.getDeductibleValue().doubleValue()); // 商业车损险免赔额
				// compensateExp.setClaimType(prpLCompensateVo.getClaimType()); // 赔案类别
				compensateExp.setIndemnityDutyRate(prpLLossPropVo.getDutyRate().doubleValue()/100); // 险别事故责任比例
				if(prpLLossPropVo.getDeductDutyRate().doubleValue()>0){
					compensateExp.setDutyDeductibleRate(prpLLossPropVo.getDeductDutyRate().doubleValue()/100); // 事故责任免赔率
				}else{
					compensateExp.setDutyDeductibleRate(prpLLossPropVo.getDeductAbsRate().doubleValue()/100); // 绝对免赔率
				}
				compensateExp.setSelectDeductibleRate(prpLLossPropVo.getDeductAddRate().doubleValue()/100); // 可选免赔率
				compensateExp.setDeductibleRate(prpLLossPropVo.getDeductibleRate().doubleValue()/100);
				// double deductOffRate = this.deductOffRate(registNo,prpLLossPropVo.getKindCode(),prpLLossPropVo.getDeductDutyRate(),prpLLossPropVo.getDeductAddRate());
				// compensateExp.setExceptDeductRate(deductOffRate); // 不计免赔率
				compensateExp.setExceptDeductRate(prpLLossPropVo.getDeductOffRate().doubleValue()/100);
				compensateExp.setIndex(index++ ); // 对象索引
				// 与周文确认，不计免赔都纳入赔款。
				compensateExp.setFlagOfM("1"); // 是否将不计免赔率纳入赔款 1,是;0,否
				compensateExp.setLossItem(prpLLossPropVo);
				compensateExp.setLossName("地面损失");
				compensateExp.setFeeTypeName(codeTranService.transCode("LossTypeProp",prpLLossPropVo.getLossType()));

				compensateExp.setCheckPolicyM("0");
				List<PrpLCItemKindVo> isMitemKinds = compensateVo.getIsMitemKindList();
				if(isMitemKinds!=null&& !isMitemKinds.isEmpty()){
					for(PrpLCItemKindVo prpLcItemKind:isMitemKinds){
						if(prpLcItemKind.getKindCode().equals(prpLLossPropVo.getKindCode())){
							compensateExp.setCheckPolicyM("1");
							break;
						}
					}
				}

				if("1".equals(compensateExp.getIsSuitFlag())){
					List<MItemKindVo> mExceptKinds = compensateVo.getmExceptKinds();
					for(MItemKindVo mItemKindVo:mExceptKinds){
						if(compensateExp.getKindCode().equals(mItemKindVo.getKindCode())){
							compensateExp.setSumRealPayJud(mItemKindVo.getExceptKindDeductible());
						}
					}
				}

				compensateExp.setAmount(prpLLossPropVo.getItemAmount().doubleValue());
				compensateExp.setDamageAmount(prpLLossPropVo.getItemAmount().doubleValue());
				compensateExp.setUnitAmount(0d);
				compensateExp.setQuantity(0d);
				compensateExp.setRegistNo(registNo);

				compensateExpAry.add(compensateExp);
			}
		}

		// 3.人伤,List<CompensateExp>对象组织前的准备：获取到所有损失项的KindCode，一个险别对应一个CopensateExp对象
		int prpLpersonItemIndex = -1;
		for(PrpLLossPersonVo prpLLossPersonVo:prpLLossPersonVoList){
			if(CodeConstants.KINDCODE.KINDCODE_D11.equals(prpLLossPersonVo.getKindCode())||CodeConstants.KINDCODE.KINDCODE_D12
					.equals(prpLLossPersonVo.getKindCode())){
				prpLpersonItemIndex++ ;
			}

			int prpLpersonLossIndex = 0;

			double sumLoss = 0d;
			double otherDeductFee = 0d;
			double sumLossBZPaid = 0d;
			for(PrpLLossPersonFeeVo prpLLossPersonFeeVo:prpLLossPersonVo.getPrpLLossPersonFees()){
				sumLoss = sumLoss+prpLLossPersonFeeVo.getFeeLoss().doubleValue();
				otherDeductFee = otherDeductFee+prpLLossPersonFeeVo.getFeeOffLoss().doubleValue();
				sumLossBZPaid = sumLossBZPaid+prpLLossPersonFeeVo.getBzPaidLoss().doubleValue();
			}

			CompensateExp compensateExp = new CompensateExp();
			compensateExp.setRiskCode(prpLClaimVo.getRiskCode());
			prpLLossPersonVo.setPersonId(prpLLossPersonVo.getPersonId());
			if(CodeConstants.KINDCODE.KINDCODE_D11.equals(prpLLossPersonVo.getKindCode())||CodeConstants.KINDCODE.KINDCODE_D12
					.equals(prpLLossPersonVo.getKindCode())){
				compensateExp.setIntLossIndex(prpLpersonItemIndex);// 归属第几个人
				compensateExp.setLossNumPerPerson(prpLpersonLossIndex++ );// 某个人费用序号 (0或者1)
			}
			// BigDecimal loss = prpLLossPersonFeeVo.getFeeLoss().subtract(prpLLossPersonFeeVo.getFeeOffLoss());

			// if(CodeConstants.KINDCODE.KINDCODE_B.equals(prpLLossPersonVo.getKindCode().trim())){
			// compensateExp.setRescueFee(loss.doubleValue());
			// compensateExp.setSumLoss(0d);
			// }

			compensateExp.setbZPaid(compensateExp.getSumLossBZPaid());
			// compensateExp.setIsSuitFlag(this.isSuitCase(prpLCompensateVo));
			compensateExp.setKindCode(prpLLossPersonVo.getKindCode().trim());
			compensateExp.setFeeTypeName(codeTranService.transCode("LossType",prpLLossPersonVo.getLossType()));
			// compensateExp.setFeeTypeCode(prpLLossPersonVo.getLossFeeType()); // 损失类型代码
			compensateExp.setLossItem(prpLLossPersonVo);
			compensateExp.setPersonChargeName("人伤费用");
			// if("02".equals(prpLLossPersonFeeVo.getLossItemNo())){//02-死亡伤残 03-医疗费用
			// compensateExp.setPersonChargeName("死亡伤残");
			// }else{
			// compensateExp.setPersonChargeName("医疗费用");
			// }

			PrpLDlossPersTraceVo prpLDlossPersTraceVo = persTraceDubboService.findPersTraceByPK(prpLLossPersonVo.getPersonId());
			if(prpLDlossPersTraceVo!=null){
				compensateExp.setLicenseNo(prpLDlossPersTraceVo.getPrpLDlossPersInjured().getLicenseNo());
				compensateExp.setLossName(prpLDlossPersTraceVo.getPrpLDlossPersInjured().getPersonName());
			}

			if("1".equals(compensateExp.getIsSuitFlag())){
				List<MItemKindVo> mExceptKinds = compensateVo.getmExceptKinds();
				for(MItemKindVo mItemKindVo:mExceptKinds){
					if(compensateExp.getKindCode().equals(mItemKindVo.getKindCode())){
						compensateExp.setSumRealPayJud(mItemKindVo.getExceptKindDeductible());
					}
				}
			}

			compensateExp.setRecoveryOrPayFlag(CodeConstants.PayFlagType.COMPENSATE_PAY); // 损失项代位类型：1代位,2清付,3自付
			compensateExp.setLossQuantity(1); // 损失项数量，如果非手工增加的其他损失，则默认为1
			compensateExp.setExpType(CodeConstants.ExpType.PERSON); // 损失类型：车/财/人/其他

			// 人伤不代位，所以都是自付损失项。
			compensateExp.setPayType(CodeConstants.PayFlagType.COMPENSATE_PAY);
			// 人伤不代位，所以责任对方追偿金额都是0。
			compensateExp.setRecoveryPay(0d);

			compensateExp.setOtherDeductFee(otherDeductFee);// 其他扣除
			// compensateExp.setSumRest(prpLpersonLoss.getRemnantfee().doubleValue()); // 残值
			compensateExp.setSumLoss(sumLoss); // 核定损失
			// if(prpLLossPersonFeeVo.getOffPreAmt()!=null){
			// compensateExp.setSumPrePay(prpLLossPersonFeeVo.getOffPreAmt().doubleValue());
			// }else{
			// compensateExp.setSumPrePay(0.00D);
			// }
			compensateExp.setSumLossBZPaid(sumLossBZPaid); // 定损交强已赔付
			compensateExp.setRescueFee(0d); // 施救费
			compensateExp.setRescueFeeBZPaid(0d); // 施救费交强已赔付
			compensateExp.setbZPaid(compensateExp.getSumLossBZPaid()+compensateExp.getRescueFeeBZPaid()); // 总交强已赔付
			// compensateExp.setDamageItemRealCost(thirdPartyDepreRate.getActualValue()); // 车辆实际价值
			// compensateExp.setEntryItemCarCost(thirdPartyDepreRate.getPurchasePrice()); // 保险车辆新车购置价
			// compensateExp.setDeviceActualValue(thirdPartyDepreRate.getDeviceActualValue()); // 设备实际价值
			// compensateExp.setDevicePurchasePrice(thirdPartyDepreRate.getDevicePurchasePrice()); // 新设备购置价
			// compensateExp.setUseMonths(thirdPartyDepreRate.getUseMonths()); // 使用月数
			// compensateExp.setDepreRate(thirdPartyDepreRate.getDepreRate()); // 折旧率
			compensateExp.setDamageDate(prpLRegistVo.getDamageTime()); // 出险日期
			compensateExp.setOperateDate(compensateVo.getOperateDate()); // 承保日期,prpLcMain.operateDate
			compensateExp.setCompensateType(Integer.parseInt(CodeConstants.CompensateKind.BI_COMPENSATE)); // 计算书类型,商业
			// compensateExp.setIsSuitFlag(this.isSuitCase(prpLCompensateVo));
			compensateExp.setDeductFee(compensateVo.getDeductibleValue() == null ? 0D: compensateVo.getDeductibleValue().doubleValue()); // 商业车损险免赔额
			// compensateExp.setClaimType(prpLCompensateVo.getClaimType()); // 赔案类别
			compensateExp.setIndemnityDutyRate(prpLLossPersonVo.getDutyRate().doubleValue()/100); // 险别事故责任比例
			compensateExp.setDutyDeductibleRate(prpLLossPersonVo.getDeductDutyRate().doubleValue()/100); // 事故责任免赔率

			compensateExp.setSelectDeductibleRate(prpLLossPersonVo.getDeductAddRate().doubleValue()/100); // 可选免赔率
			compensateExp.setDeductibleRate(prpLLossPersonVo.getDeductibleRate().doubleValue()/100);
			compensateExp.setExceptDeductRate(prpLLossPersonVo.getDeductOffRate().doubleValue()/100);
			// double deductOffRate = this.deductOffRate(registNo,prpLLossPersonVo.getKindCode(),prpLLossPersonFeeVo.getDeductDutyRate(),prpLLossPersonFeeVo.getDeductAddRate());
			// compensateExp.setExceptDeductRate(deductOffRate); // 不计免赔率
			compensateExp.setIndex(index++ ); // 对象索引
			// 与周文确认，不计免赔都纳入赔款。
			compensateExp.setFlagOfM("1"); // 是否将不计免赔率纳入赔款 1,是;0,否

			compensateExp.setCheckPolicyM("0");
			List<PrpLCItemKindVo> isMitemKinds = compensateVo.getIsMitemKindList();
			for(PrpLCItemKindVo prpLcItemKind:isMitemKinds){
				if(prpLcItemKind.getKindCode().equals(prpLLossPersonVo.getKindCode())){
					compensateExp.setCheckPolicyM("1");
					break;
				}
			}

			compensateExp.setAmount(prpLLossPersonVo.getItemAmount().doubleValue());
			compensateExp.setDamageAmount(prpLLossPersonVo.getItemAmount().doubleValue());
			compensateExp.setUnitAmount(prpLLossPersonVo.getItemAmount().doubleValue());
			if(prpLLossPersonVo.getQuantity()==null){
				compensateExp.setQuantity(0d);
			}else{
				compensateExp.setQuantity(prpLLossPersonVo.getQuantity().doubleValue());
			}

			List<PrpLCItemKindVo> prpLcItemKinds = compensateVo.getPrpLCItemKindVoList();
			// for(PrpLCItemKindVo prpLcItemKind:prpLcItemKinds){
			// if(prpLcItemKind.getKindCode().trim().equals(prpLLossPersonVo.getKindCode().trim())){
			// compensateExp.setAmount(prpLcItemKind.getAmount().doubleValue());
			// compensateExp.setDamageAmount(prpLcItemKind.getAmount().doubleValue());
			// compensateExp.setUnitAmount(prpLcItemKind.getUnitAmount().doubleValue());
			// if(prpLcItemKind.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_D11)){
			// compensateExp.setUnitAmount(prpLcItemKind.getAmount().doubleValue());
			// }
			// if(prpLcItemKind.getQuantity()==null){
			// compensateExp.setQuantity(0d);
			// }else{
			// compensateExp.setQuantity(prpLcItemKind.getQuantity().doubleValue());
			// }
			// break;
			// }
			//
			// }
			compensateExp.setRegistNo(registNo);
			compensateExpAry.add(compensateExp);
			// }
		}

		// 4.其他,List<CompensateExp>对象组织前的准备：获取到所有损失项的KindCode，一个险别对应一个CopensateExp对象
		for(PrpLLossPropVo prpLLossPropVo:prpLLossPropVoList){
			if("9".equals(prpLLossPropVo.getPropType())){
				CompensateExp compensateExp = new CompensateExp();
				compensateExp.setRiskCode(prpLClaimVo.getRiskCode());
				compensateExp.setKindCode(prpLLossPropVo.getKindCode().trim());
				// compensateExp.setFeeTypeCode(prpLLossPropVo.getFeeTypeCode()); // 损失类型代码
				List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(prpLCompensateVo.getRegistNo());
				if(prpLDlossCarMainVoList!=null&& !prpLDlossCarMainVoList.isEmpty()){
					for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){
						if(prpLDlossCarMainVo.getSerialNo()==1){
							compensateExp.setLicenseNo(prpLDlossCarMainVo.getLicenseNo()); // 车牌号码
							break;
						}
					}
				}
				compensateExp.setRecoveryOrPayFlag(CodeConstants.PayFlagType.COMPENSATE_PAY); // 损失项代位类型：1代位,2清付,3自付
				if(prpLLossPropVo.getLossQuantity()==null){
					compensateExp.setLossQuantity(1);
				}else{
					compensateExp.setLossQuantity(prpLLossPropVo.getLossQuantity().intValue());
				}
				compensateExp.setExpType(CodeConstants.ExpType.OTHER); // 损失类型：车/财/人/其他

				// 财产不代位，所以都是自付损失项。
				compensateExp.setPayType(CodeConstants.PayFlagType.COMPENSATE_PAY);
				// 财产不代位，所以责任对方追偿金额都是0。
				compensateExp.setRecoveryPay(0d);
				// if(prpLLossPropVo.getSumRest()==null){
				// compensateExp.setSumRest(0d); // 残值
				// }else{
				// compensateExp.setSumRest(prpLLossPropVo.getSumRest().doubleValue()); // 残值
				// }
				compensateExp.setOtherDeductFee(DataUtils.NullToZero(prpLLossPropVo.getOtherDeductAmt()).doubleValue());// 其他扣除
				compensateExp.setSumLoss(DataUtils.NullToZero(prpLLossPropVo.getSumLoss()).doubleValue()); // 核定损失
				// compensateExp.setSumPrePay(prpLLossPropVo.getOffPreAmt().doubleValue());
				// compensateExp.setRescueFee(prpLLossPropVo.getRescueFee().doubleValue()); // 施救费
				// compensateExp.setSumLossBZPaid(prpLLossPropVo.getBzPaidLoss().doubleValue()); // 定损交强已赔付
				// compensateExp.setRescueFeeBZPaid(prpLLossPropVo.getBzPaidRescueFee().doubleValue()); // 施救费交强已赔付
				// compensateExp.setbZPaid(compensateExp.getSumLossBZPaid()+compensateExp.getRescueFeeBZPaid()); // 总交强已赔付
				// 实际价值减去 已赔付金额
				Map<String,BigDecimal> kindPaidMap = compensateVo.getKindPaidMap();
				BigDecimal sumRealPay4Kind = BigDecimal.ZERO;
				if(kindPaidMap!=null&& !kindPaidMap.isEmpty()){
					sumRealPay4Kind = kindPaidMap.get(CodeConstants.KINDCODE.KINDCODE_A);
				}
				compensateExp.setDamageItemRealCost(thirdPartyDepreRate.getActualValue()-DataUtils.NullToZero(sumRealPay4Kind).doubleValue()); // 车辆实际价值
				compensateExp.setEntryItemCarCost(thirdPartyDepreRate.getPurchasePrice()); // 保险车辆新车购置价
				if(CodeConstants.KINDCODE.KINDCODE_X.equals(compensateExp.getKindCode())&& !cprcCase){
					Map<Long,String[]> deviceMap = thirdPartyDepreRate.getDeviceMap();
					// 新增设备MAP key deviceId,value 0 deviceName,1 新设备购置价 2 出险时新设备实际价值
					String[] array = deviceMap.get(Long.parseLong(prpLLossPropVo.getItemName()));
					compensateExp.setDeviceActualValue(Double.parseDouble(array[2])); // 设备实际价值
					compensateExp.setDevicePurchasePrice(Double.parseDouble(array[1])); // 新设备购置价
				}

				compensateExp.setUseMonths(thirdPartyDepreRate.getUseMonths()); // 使用月数
				compensateExp.setDepreRate(thirdPartyDepreRate.getDepreRate()); // 折旧率
				compensateExp.setDamageDate(prpLRegistVo.getDamageTime()); // 出险日期
				compensateExp.setOperateDate(compensateVo.getOperateDate()); // 承保日期,prpLcMain.operateDate
				compensateExp.setCompensateType(Integer.parseInt(CodeConstants.CompensateKind.BI_COMPENSATE)); // 计算书类型,商业
				// compensateExp.setIsSuitFlag(this.isSuitCase(prpLCompensateVo));
				compensateExp.setDeductFee(compensateVo.getDeductibleValue() == null ? 0D : compensateVo.getDeductibleValue().doubleValue()); // 商业车损险免赔额
				// compensateExp.setClaimType(prpLCompensateVo.getClaimType()); // 赔案类别
				compensateExp.setIndemnityDutyRate(prpLLossPropVo.getDutyRate().doubleValue()/100); // 险别事故责任比例
				if(prpLLossPropVo.getDeductDutyRate().doubleValue()>0){
					compensateExp.setDutyDeductibleRate(prpLLossPropVo.getDeductDutyRate().doubleValue()/100); // 事故责任免赔率
				}else{
					compensateExp.setDutyDeductibleRate(prpLLossPropVo.getDeductAbsRate().doubleValue()/100); // 绝对免赔率
				}
				compensateExp.setSelectDeductibleRate(prpLLossPropVo.getDeductAddRate().doubleValue()/100); // 可选免赔率
				compensateExp.setDeductibleRate(prpLLossPropVo.getDeductibleRate().doubleValue()/100);
				// double deductOffRate = this.deductOffRate(registNo,prpLLossPropVo.getKindCode(),prpLLossPropVo.getDeductDutyRate(),prpLLossPropVo.getDeductAddRate());
				compensateExp.setExceptDeductRate(prpLLossPropVo.getDeductOffRate().doubleValue()/100); // 不计免赔率
				compensateExp.setIndex(index++ ); // 对象索引
				// 与周文确认，不计免赔都纳入赔款。
				compensateExp.setFlagOfM("1"); // 是否将不计免赔率纳入赔款 1,是;0,否
				compensateExp.setLossItem(prpLLossPropVo);
				compensateExp.setFeeTypeName("附加险");

				compensateExp.setCheckPolicyM("0");
				List<PrpLCItemKindVo> isMitemKinds = compensateVo.getIsMitemKindList();
				for(PrpLCItemKindVo prpLcItemKind:isMitemKinds){
					if(prpLcItemKind.getKindCode().equals(prpLLossPropVo.getKindCode())){
						compensateExp.setCheckPolicyM("1");
						break;
					}
				}

				// List<PrpLCItemKindVo> itemKindList = compensateVo.getPrpLCItemKindVoList();
				compensateExp.setDamageAmount(prpLLossPropVo.getItemAmount().doubleValue());
				compensateExp.setQuantity(DataUtils.NullToZero(prpLLossPropVo.getMaxQuantity()).doubleValue());
				compensateExp.setUnitAmount(DataUtils.NullToZero(prpLLossPropVo.getUnitAmount()).doubleValue());
				// for(PrpLCItemKindVo prpLcItemKind:itemKindList){
				// if(prpLcItemKind.getKindCode().equals(prpLLossPropVo.getKindCode())){
				// if(prpLLossPropVo.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_C)
				// ||prpLLossPropVo.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_T)
				// ||prpLLossPropVo.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_RF)){
				//
				// compensateExp.setQuantity(prpLcItemKind.getQuantity().doubleValue());
				// }
				// break;
				// }
				// }

				// 无法找到第三方 特殊处理
				if(CodeConstants.KINDCODE.KINDCODE_X.equals(prpLLossPropVo.getKindCode())){
					// 有车损险 或者 已扣减过免赔额
					if(deductibleKindAFlag||compensateVo.isDeductibleFlag()){
						compensateExp.setDeductFee(0d);
					}

					compensateExp.setNoFindThirdFlag("0");
					compensateExp.setKindCodeNTFlag("0");;
					List<PrpLClaimDeductVo> claimDeductList = compensateVo.getClaimDeductList();
					if(claimDeductList!=null&& !claimDeductList.isEmpty()){
						for(PrpLClaimDeductVo claimDeductVo:claimDeductList){
							if(( "120".equals(claimDeductVo.getDeductCondCode())||"320".equals(claimDeductVo.getDeductCondCode()) )){
								compensateExp.setNoFindThirdFlag("1");
								break;
							}
						}
					}
					if(cprcCase){
						for(PrpLCItemKindVo itemKindVo:compensateVo.getPrpLCItemKindVoList()){
							if(CodeConstants.KINDCODE.KINDCODE_NT.equals(itemKindVo.getKindCode())){
								compensateExp.setKindCodeNTFlag("1");
								break;
							}
						}
					}
				}

				if("1".equals(compensateExp.getIsSuitFlag())){
					List<MItemKindVo> mExceptKinds = compensateVo.getmExceptKinds();
					for(MItemKindVo mItemKindVo:mExceptKinds){
						if(compensateExp.getKindCode().equals(mItemKindVo.getKindCode())){
							compensateExp.setSumRealPayJud(mItemKindVo.getExceptKindDeductible());
						}
					}
				}
				if(CodeConstants.KINDCODE.KINDCODE_R.equals(compensateExp.getKindCode())||CodeConstants.KINDCODE.KINDCODE_SS.equals(compensateExp
						.getKindCode())){

					compensateExp.setUnitAmount(prpLLossPropVo.getItemAmount().doubleValue());
				}

				compensateExp.setAmount(prpLLossPropVo.getItemAmount().doubleValue());

				// if(prpLLossPropVo.getLossQuantity()!=null){
				// compensateExp.setQuantity(prpLLossPropVo.getLossQuantity().doubleValue());
				// }else{
				// compensateExp.setQuantity(0d);
				// }
				compensateExp.setRegistNo(registNo);
				compensateExpAry.add(compensateExp);
			}
		}
		return compensateExpAry;
	}

	/**
	 * 返回计算书是否按诉讼案处理，诉讼案、通融赔付、交强垫付计算书、交强重开都按诉讼案处理。
	 * @param prpLcompensate
	 * @return
	 */
	public String isSuitCase(PrpLCompensateVo prpLCompensateVo) {

		boolean isNotFirst4CompulsoryCompensate = this.isNotFirst4CompulsoryCompensate(prpLCompensateVo);

		boolean isSuitCase = "1".equals(prpLCompensateVo.getLawsuitFlag())||"1".equals(prpLCompensateVo.getAllowFlag())||CodeConstants.CompensateKind.ADVANCE
				.equals(prpLCompensateVo.getCompensateKind())||isNotFirst4CompulsoryCompensate;
		return isSuitCase ? "1" : "0";
	}

	public boolean isNotFirst4CompulsoryCompensate(PrpLCompensateVo prpLCompensateVo) {
		boolean isNotFirst4CompulsoryCompensate = false;
		if( !this.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind())){
			List<PrpLCompensateVo> passCompensateList = this.findPassCompulsoryCompensate(prpLCompensateVo.getClaimNo());
			if(passCompensateList.size()==1&& !passCompensateList.get(0).getCompensateNo().equals(prpLCompensateVo.getCompensateNo())){
				isNotFirst4CompulsoryCompensate = true;
			}
			if(passCompensateList.size()>=2){
				isNotFirst4CompulsoryCompensate = true;
			}
		}

		return isNotFirst4CompulsoryCompensate;
	}

	/**
	 * 判断商车费改保单案件
	 * @param registNo
	 * @return
	 */
	public boolean isCprcCase(String registNo) {
		return policyViewService.isNewClauseCode(registNo);
	}

	public List executeRulesCompensate(List<CompensateExp> compensteExpList,Boolean cprcCase) {
		List<CompensateExp> _compensateExpList = new ArrayList<CompensateExp>(0);
		CompensateListVo compensateListVo = new CompensateListVo();

		List<ThirdPartyInfo> thirdPartyInfosTemp;
		List<ThirdPartyLossInfo> thirdPartyLossInfosTemp;
		List<ThirdPartyRecoveryInfo> thirdPartyRecoveryInfoTemp;

		List<ThirdPartyInfo> thirdPartyInfoAryTemp;
		List<ThirdPartyLossInfo> thirdPartyLossInfoAryTemp;
		List<ThirdPartyRecoveryInfo> thirdPartyRecoveryInfoAryTemp;

		// 是否存在交强有责车辆。
		String bzDutyCar = "0";
		// 是否交强险理算。
		boolean isBZPay = false;

		if(compensteExpList!=null&&compensteExpList.size()>0){
			compensateListVo.setRegistNo(compensteExpList.get(0).getRegistNo());
		}

		// 为了取得每个人员下面的具体险别损失项数目。
		final Map<Long,Integer> map = new HashMap<Long,Integer>();

		for(CompensateExp exp:compensteExpList){
			String kindCode = exp.getKindCode();
			// 如果是 车上人员责任险(司机)、车上人员责任险(乘客)。
			if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D12)||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D11)){
				// CompensateExp.intLossIndex是人保画面中这个人员损失项PrpLpersonLoss对应的PrpLpersonItem在HTML Table上的序号，由0开始。
				PrpLLossPersonVo prpLLossPersonVo = (PrpLLossPersonVo)exp.getLossItem();
				Long personId = prpLLossPersonVo.getPersonId();
				if(map.containsKey(personId)){
					int count = map.get(personId);
					map.put(personId,count+1);
				}else{
					map.put(personId,1);
				}
			}
		}

		// 功能：如果理算大对象中有代位理算，将理算清单设置为代位理算。
		for(CompensateExp exp:compensteExpList){
			if(CodeConstants.PayFlagType.INSTEAD_PAY.equals(exp.getPayType())){
				if(CodeConstants.PayFlagType.INSTEAD_PAY.equals(exp.getRecoveryOrPayFlag())){
					String kindCode = exp.getKindCode();
					if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_A)||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_A1)){
						compensateListVo.setCompensateType(exp.getCompensateType());
						// 代位类型：1:追偿,2:清付,3:自付(上代位后的新案件)；“”:普通理算
						compensateListVo.setPayType(exp.getPayType());
						compensateListVo.setIsSuitFlag(exp.getIsSuitFlag());
						compensateListVo.setBzDutyCar(bzDutyCar);
						if(exp.getOppoentCoverageType()==null){
							exp.setOppoentCoverageType("");
						}
						_compensateExpList.add(exp);
					}
				}
			}
		}

		// 处理非代位CompensateExp。
		for(CompensateExp exp:compensteExpList){
			if(CodeConstants.PayFlagType.INSTEAD_PAY.equals(exp.getPayType())&&CodeConstants.PayFlagType.INSTEAD_PAY.equals(exp
					.getRecoveryOrPayFlag())&&( exp.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_A)||exp.getKindCode().equals(
					CodeConstants.KINDCODE.KINDCODE_A1) )){
				continue;
			}
			// TODO　看不懂
			if(exp.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_BZ)){
				isBZPay = true;
				thirdPartyInfosTemp = new ArrayList<ThirdPartyInfo>(0);
				thirdPartyLossInfosTemp = new ArrayList<ThirdPartyLossInfo>(0);
				thirdPartyRecoveryInfoTemp = new ArrayList<ThirdPartyRecoveryInfo>(0);

				thirdPartyInfoAryTemp = Arrays.asList(exp.getThirdPartyInfoAry());
				thirdPartyLossInfoAryTemp = Arrays.asList(exp.getThirdPartyLossInfoAry());
				thirdPartyRecoveryInfoAryTemp = Arrays.asList(exp.getThirdPartyRecoveryInfoAry());

				for(ThirdPartyLossInfo lossInfo:thirdPartyLossInfoAryTemp){
					lossInfo.setInsteadFlag("1");
				}

				// 由于人保代码存在数组转List的过程，但安联不需要，所以直接将List赋值到List就可以了。
				exp.setThirdPartyInfos(thirdPartyInfoAryTemp);
				exp.setThirdPartyLossInfos(thirdPartyLossInfoAryTemp);
				exp.setThirdPartyRecoveryInfos(thirdPartyRecoveryInfoAryTemp);

				for(ThirdPartyInfo thirdPartyInfo:thirdPartyInfoAryTemp){
					if("1".equals(thirdPartyInfo.isBzDutyType())){
						bzDutyCar = "1";
						break;
					}
				}

				compensateListVo.setThirdPartyRecoveryInfoAry(exp.getThirdPartyRecoveryInfos());
			}

			// 如果是 车上人员责任险(司机)、车上人员责任险(乘客)、交通事故精神损害赔偿险。//TODO 需要调整
			if(exp.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_D12)||exp.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_D11)){
				PrpLLossPersonVo prpLLossPersonVo = (PrpLLossPersonVo)exp.getLossItem();
				// for(PrpLLossPersonVo personVo : lossPersonList){
				//
				// }
				//
				// PrpLLossPersonVo prpLLossPersonVo = prpLLossPersonFeeVo.getPrpLLossPerson();
				exp.setLossAmountPerPerson(map.get(prpLLossPersonVo.getPersonId()));
			}

			compensateListVo.setCompensateType(exp.getCompensateType());
			// 代位类型：1:追偿,2:清付,3:自付(上代位后的新案件)；””:普通理算
			compensateListVo.setPayType(exp.getPayType());
			if(exp.getOppoentCoverageType()==null){
				exp.setOppoentCoverageType("");
			}
			compensateListVo.setIsSuitFlag(exp.getIsSuitFlag());
			compensateListVo.setBzDutyCar(bzDutyCar);
			_compensateExpList.add(exp);
		}
		
		compensateListVo.setCompensateList(_compensateExpList);
		// 设置compensateExpList中的代位理算大对象数量。
		compensateListVo.setListAmount(_compensateExpList.size());
		// 人伤数量。
		compensateListVo.setLossPersonsNum(map.size());
       
		// 设置是否扣减过绝对免赔 TODO

		// 调用商业计算过程
		CompensateListVo returnCompensateList = this.calculate(compensateListVo,cprcCase);

		this.handleOneMoneny(returnCompensateList);

		// 如果是交强险
		logger.info("是否交强险 = "+isBZPay);
		if(isBZPay){
			List<CompensateExp> returnCompensateExpList = returnCompensateList.getCompensateList();
			// 代交强赔付信息
			List<ThirdPartyRecoveryInfo> thirdPartyRecoveryInfoList = returnCompensateList.getThirdPartyRecoveryInfoAry();

			List<ThirdPartyLossInfo> thirdPartyLossInfolist = new ArrayList<ThirdPartyLossInfo>(0);
			// 主车车牌。
			String policyLicenseNo = null;
			for(CompensateExp exp:returnCompensateExpList){
				List<ThirdPartyLossInfo> thirdPartyLossInfos = exp.getThirdPartyLossInfos();
				List<ThirdPartyInfo> thirdPartyInfos = exp.getThirdPartyInfos();
				// 查找主车车牌。
				for(ThirdPartyInfo carInfo:thirdPartyInfos){
					if(carInfo.getSerialNo()==1){
						policyLicenseNo = carInfo.getLicenseNo();
					}
				}

				for(ThirdPartyLossInfo lossInfo:thirdPartyLossInfos){

					lossInfo.setDamageType(exp.getDamageType());
					lossInfo.setExgratiaFee(returnCompensateList.getExgratiaFee());
					lossInfo.setThirdPartyRecoveryInfoAry(thirdPartyRecoveryInfoList);

					String claimType = exp.getClaimType();

					// 如果是互碰自赔并且是主车损失，才加入损失项，否则不加入。
					if(claimType.equals(CodeConstants.ClaimType.SPAY_CASE)){
						if(policyLicenseNo.equals(lossInfo.getLicenseNo())){
							thirdPartyLossInfolist.add(lossInfo);
						}
					}else{
						thirdPartyLossInfolist.add(lossInfo);
					}
				}
			}
			logger.info("<--executeRulesCompensate thirdPartyLossInfolist.size()"+thirdPartyLossInfolist.size());
			return thirdPartyLossInfolist;
		}else{
			List<CompensateExp> returnCompensateExpList = returnCompensateList.getReturnCompensateList();
			for(CompensateExp returnCompensateExp:returnCompensateExpList){
				returnCompensateExp.setExgratiaFee(returnCompensateList.getExgratiaFee());
			}
			// 组织理算计算文字信息
			List<CompensateExp> list = this.setupCompensateExpByCompensateExp(returnCompensateExpList);
			logger.info("<--executeRulesCompensate list.size()"+list.size());
			return list;
		}
	}

	/***
	 * 处理一分钱 尾差 可以合并处理的险别需要做尾差处理 三者险（B） 和 车上乘客 （D12）
	 * @param returnCompensateList
	 */
	private void handleOneMoneny(CompensateListVo returnCompensateList) {
		List<CompensateExp> compExpList = returnCompensateList.getReturnCompensateList();
		handleOneKind(compExpList,CodeConstants.KINDCODE.KINDCODE_B);
		handleOneKind(compExpList,CodeConstants.KINDCODE.KINDCODE_D11);

	}

	private void handleOneKind(List<CompensateExp> compExpList,String kindCode) {
		Double allDiffer = 0.00;// 保留四位小数相加后的结果四舍五入取小数点后两位-保留两位小数相加之和
		Double allSumRealPayFour = 0.0000;// 该理算大对象的所有损失项实赔金额之和（保留四位小数相加）
		Double allSumRealPayTwo = 0.00;// 该理算大对象的所有损失项实赔金额之和(保留两位小数相加)
		Double allSumRealPaySix = 0.000000;// 防止超限额后再对此理算对象进行一分钱处理
		for(CompensateExp returnCompensateExp:compExpList){
			if(kindCode.equals(returnCompensateExp.getKindCode())){

				Double sumRealPaySixTemp = 0.000000;
				Double sumRealPayFourTemp = 0.0000;
				Double sumRealPayTwoTemp = 0.00;
				Double differ = 0.00;
				if(returnCompensateExp.getSumRealPay()>0){
					if( !Double.isInfinite(returnCompensateExp.getSumRealPay())&& !Double.isNaN(returnCompensateExp.getSumRealPay())){
						// sumRealPay可能为NaN
						Double sumRealPaySix = DataUtils.round(returnCompensateExp.getSumRealPay(),6);
						Double sumRealPayFour = DataUtils.round(returnCompensateExp.getSumRealPay(),4);
						Double sumRealPayTwo = new BigDecimal(returnCompensateExp.getSumRealPay()).setScale(2,4).doubleValue();
						allSumRealPaySix = allSumRealPaySix+sumRealPaySix;
						allSumRealPayFour = allSumRealPayFour+sumRealPayFour;
						allSumRealPayTwo = allSumRealPayTwo+sumRealPayTwo;

						sumRealPaySixTemp = sumRealPaySixTemp+sumRealPaySix;
						sumRealPayFourTemp = sumRealPayFourTemp+sumRealPayFour;
						sumRealPayTwoTemp = sumRealPayTwoTemp+sumRealPayTwo;
						if(sumRealPayTwo>returnCompensateExp.getSumRealPay()){// 进行了进入
							returnCompensateExp.setRoundFlag("2");
						}else if(sumRealPayTwo<returnCompensateExp.getSumRealPay()){// 进行了四舍
							returnCompensateExp.setRoundFlag("1");
						}else{
							returnCompensateExp.setRoundFlag("0");
						}
					}
				}
				if(returnCompensateExp.getMsumRealPay()>0){
					if( !Double.isInfinite(returnCompensateExp.getMsumRealPay())&& !Double.isNaN(returnCompensateExp.getMsumRealPay())){
						// sumRealPay可能为NaN
						Double mSumRealPaySix = DataUtils.round(returnCompensateExp.getMsumRealPay(),6);
						Double mSumRealPayFour = DataUtils.round(returnCompensateExp.getMsumRealPay(),4);;
						Double mSumRealPayTwo = new BigDecimal(returnCompensateExp.getMsumRealPay()).setScale(2,4).doubleValue();
						allSumRealPaySix = allSumRealPaySix+mSumRealPaySix;
						allSumRealPayFour = allSumRealPayFour+mSumRealPayFour;
						allSumRealPayTwo = allSumRealPayTwo+mSumRealPayTwo;

						sumRealPaySixTemp = sumRealPaySixTemp+mSumRealPaySix;
						sumRealPayFourTemp = sumRealPayFourTemp+mSumRealPayFour;
						sumRealPayTwoTemp = sumRealPayTwoTemp+mSumRealPayTwo;
						if(mSumRealPayTwo>returnCompensateExp.getMsumRealPay()){// 进行了进入
							returnCompensateExp.setRoundFlagM("2");
						}else if(mSumRealPayTwo<returnCompensateExp.getMsumRealPay()){// 进行了舍去
							returnCompensateExp.setRoundFlagM("1");
						}else{
							returnCompensateExp.setRoundFlagM("0");
						}
					}
				}
				sumRealPaySixTemp = DataUtils.round(sumRealPaySixTemp,4);
				sumRealPayFourTemp = DataUtils.round(sumRealPayFourTemp,2);
				if(sumRealPaySixTemp>sumRealPayFourTemp){// 该损失项进行了舍去
					returnCompensateExp.setRoundFlagExp("1");
				}else if(sumRealPaySixTemp<sumRealPayFourTemp){// 该损失项进行了进入
					returnCompensateExp.setRoundFlagExp("2");
				}else{
					returnCompensateExp.setRoundFlagExp("0");
				}
			}
		}

		allSumRealPayFour = DataUtils.round(allSumRealPaySix,2);
		allDiffer = allSumRealPayTwo-allSumRealPayFour;
		allDiffer = DataUtils.round(allDiffer,2);
		if(allDiffer!=0){
			for(CompensateExp returnCompensateExp:compExpList){
				if(kindCode.equals(returnCompensateExp.getKindCode())){
					if(allDiffer>0&&returnCompensateExp.getRoundFlagExp().equals("2")&&( returnCompensateExp.getRoundFlag().equals("2")||returnCompensateExp
							.getRoundFlagM().equals("2") )){
						if( !Double.isInfinite(returnCompensateExp.getSumRealPay())&& !Double.isNaN(returnCompensateExp.getSumRealPay())){
							// sumRealPay可能为NaN
							Double sumRealPay = new BigDecimal(returnCompensateExp.getSumRealPay()).setScale(2,4).doubleValue();
							returnCompensateExp.setSumRealPay(sumRealPay-0.01);
							allDiffer = allDiffer-0.01;
							returnCompensateExp.setRoundFlagExp("0");
						}
					}
					if(allDiffer<0&&returnCompensateExp.getRoundFlagExp().equals("1")&&( returnCompensateExp.getRoundFlag().equals("1")||returnCompensateExp
							.getRoundFlagM().equals("1") )){
						if( !Double.isInfinite(returnCompensateExp.getSumRealPay())&& !Double.isNaN(returnCompensateExp.getSumRealPay())){
							// sumRealPay可能为NaN
							Double sumRealPay = new BigDecimal(returnCompensateExp.getSumRealPay()).setScale(2,4).doubleValue();
							returnCompensateExp.setSumRealPay(sumRealPay+0.01);
							allDiffer = allDiffer+0.01;
							returnCompensateExp.setRoundFlagExp("0");
						}
					}
					if(allDiffer==0){
						break;
					}
				}
			}
		}
		// 返回进行一分钱调整 end
		for(CompensateExp returnCompensateExp:compExpList){
			// returnCompensateExp.setExgratiaFee(returnCompensateList.getExgratiaFee());
			if(kindCode.equals(returnCompensateExp.getKindCode())){
				if(returnCompensateExp.getSumRealPay()>0){
					if( !Double.isInfinite(returnCompensateExp.getSumRealPay())&& !Double.isNaN(returnCompensateExp.getSumRealPay())){
						// sumRealPay可能为NaN
						Double sumRealPay = new BigDecimal(returnCompensateExp.getSumRealPay()).setScale(2,4).doubleValue();
						returnCompensateExp.setSumRealPay(sumRealPay);
						returnCompensateExp.setSumDefLoss(sumRealPay);
					}
				}
				if(returnCompensateExp.getMsumRealPay()>0){
					if( !Double.isInfinite(returnCompensateExp.getMsumRealPay())&& !Double.isNaN(returnCompensateExp.getMsumRealPay())){
						// sumRealPay可能为NaN
						Double mSumRealPay = new BigDecimal(returnCompensateExp.getMsumRealPay()).setScale(2,4).doubleValue();
						returnCompensateExp.setMsumRealPay(mSumRealPay);
					}
				}
			}
		}

	}

	private List<CompensateExp> setupCompensateExpByCompensateExp(List<CompensateExp> compensateExpList) {
		List<CompensateExp> list = new ArrayList<CompensateExp>(0);
		int lastIndex = 0;
		String kindName = null;

		Map<String,Double> kindSumRealPayMap = new TreeMap<String,Double>();
		Map<String,Double> exceptKindDeductMap = new TreeMap<String,Double>();

		double sumPaidAZF = 0d;// 我方赔付金额

		for(CompensateExp compensateExpForA:compensateExpList){
			if(( compensateExpForA.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_A)||compensateExpForA.getKindCode().equals(
					CodeConstants.KINDCODE.KINDCODE_A1) )&& !compensateExpForA.getRecoveryOrPayFlag().equals(CodeConstants.PayFlagType.INSTEAD_PAY)){
				// 代位公式中需要展示我方赔付金额，即正常车损险计算公式结果，从非代位的车损险exp中获取
				sumPaidAZF = compensateExpForA.getSumRealLoss()+compensateExpForA.getRescueRealFee();
			}
		}
		for(CompensateExp compensateExp:compensateExpList){

			// 分险别累加不记免赔金额
			String keyStrTemp = null;
			// 如果是不计免赔。 不计免赔不单独展示公式 ，注释掉
			// if(compensateExp.getCheckPolicyM().equals("1")){
			// double mSumRealPay = 0;
			// if( !"1".equals(compensateExp.getIsSuitFlag())){
			// mSumRealPay = compensateExp.getMsumRealPay();
			// }
			// keyStrTemp = compensateExp.getKindCode();
			// makeItemSummary(keyStrTemp,mSumRealPay,exceptKindDeductMap);
			// }
			// 地面损失没有车牌
			if(compensateExp.getLicenseNo()==null||"".equals(compensateExp.getLicenseNo())){
				compensateExp.setLicenseNo(( compensateExp.getLossName()==null ) ? "" : compensateExp.getLossName());
			}
			if(compensateExp.getPersonChargeName()==null||"".equals(compensateExp.getPersonChargeName())){
				compensateExp.setPersonChargeName("["+compensateExp.getPersonChargeName()+"]");
			}else{
				compensateExp.setPersonChargeName("");
			}

			String feeTypeName = compensateExp.getFeeTypeName();
			if(compensateExp.getRecoveryOrPayFlag().equals(CodeConstants.PayFlagType.INSTEAD_PAY)){
				feeTypeName = "代位实际赔款";
			}else{
				if("1".equals(compensateExp.getSubrogationFlag())&&( compensateExp.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_A)||compensateExp
						.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_A1) )){

					feeTypeName = "我方应承担赔款";
				}
			}
			String exp = feeTypeName+":"+this.space(CodeConstants.SpaceCount.one)+compensateExp.getLicenseNo()+"\r\n";

			String extraExp = "";
			String cal = "";

			// 车险赔款 如果损失金额等于0 就不展示公式了。
			// 代交强没有定损金额和施救费赔款。
			final String kindCode = compensateExp.getKindCode();
			if(( kindCode.equals(CodeConstants.KINDCODE.KINDCODE_A)||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_X)
			//||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_X3)
					||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_X1)||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_A1) )&&compensateExp
					.getSumLoss()!=0d&& !CodeConstants.PayFlagType.INSTEAD_PAY.equals(compensateExp.getRecoveryOrPayFlag())){
				exp += this.format4Space(this.space(CodeConstants.SpaceCount.shorter)+compensateExp.getSumRealLossText()+"\r\n"+"="+MoneyFormator
						.format4Output(compensateExp.getSumRealLoss())+"\r\n");
			}
			// 施救费。
			// 代交强没有定损金额和施救费赔款。
			if(( kindCode.equals(CodeConstants.KINDCODE.KINDCODE_A)||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_A1) )&&compensateExp
					.getRescueFee()!=0d&& !CodeConstants.PayFlagType.INSTEAD_PAY.equals(compensateExp.getRecoveryOrPayFlag())){
				exp += this.format4Space(this.space(CodeConstants.SpaceCount.shorter)+compensateExp.getRescueFeeText()+"\r\n"+"="+MoneyFormator
						.format4Output(compensateExp.getRescueRealFee())+"\r\n");
			}

			boolean showTolal = true;
			// 险别赔付信息 如果损失金额等于0 就不展示公式了
			if(( compensateExp.getSumLoss()!=0d||compensateExp.getRescueFee()!=0d||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_G)||kindCode
					.equals(CodeConstants.KINDCODE.KINDCODE_Z) )){

				this.fillMsumRealPayText(compensateExp);

				// 计算书内无需国际化。
				if (CodeConstants.KINDCODE.KINDCODE_BP.equals(kindCode) || CodeConstants.KINDCODE.KINDCODE_D11P.equals(kindCode) || CodeConstants.KINDCODE.KINDCODE_D12P.equals(kindCode)
				||CodeConstants.KINDCODE.KINDCODE_BS.equals(kindCode) || CodeConstants.KINDCODE.KINDCODE_D11S.equals(kindCode) || CodeConstants.KINDCODE.KINDCODE_D12S.equals(kindCode)){
					kindName = CodeConstants.KINDCODE_MAP.get(compensateExp.getRiskCode()+kindCode);
				}else {
					kindName = CodeConstants.KINDCODE_MAP.get(kindCode);
				}
				cal += this.space(CodeConstants.SpaceCount.shorter)+kindName+compensateExp.getSumRealPayText()+"\r\n";

				// 超限额文字说明,赔付比例不能超过1
				if( !compensateExp.getKindCode().equals("B")&&compensateExp.getClaimRate()>=0&&compensateExp.getClaimRate()<1){
					if(kindSumRealPayMap.get(kindCode)==null){
						extraExp += kindName+CodeConstants.CompensateText.text1.value+":"+"\r\n";
					}
					extraExp += compensateExp.getFeeTypeName()+":"+compensateExp.getLicenseNo()+"\r\n"+space(CodeConstants.SpaceCount._short)+compensateExp
							.getPersonChargeName()+String.format(CodeConstants.CompensateText.text2.value,
							MoneyFormator.format4Output(compensateExp.getSumRealPay()),MoneyFormator.format4Output(compensateExp.getMsumRealPay()));
				}else if(compensateExp.getClaimRate()==1){
					if("1".equals(compensateExp.getOverFlag())){
						extraExp += compensateExp.getFeeTypeName()+":"+compensateExp.getLicenseNo()+"\r\n"+space(CodeConstants.SpaceCount._short)+compensateExp
								.getPersonChargeName()+String.format(CodeConstants.CompensateText.text2.value,
								MoneyFormator.format4Output(compensateExp.getSumRealPay()),
								MoneyFormator.format4Output(compensateExp.getMsumRealPay()));
					}
				}
				cal += this.space(CodeConstants.SpaceCount._long)+"="+this.space(CodeConstants.SpaceCount.one)+MoneyFormator
						.format4Output(compensateExp.getSumDefLoss())+"\r\n";
				exp += this.format4Space(cal)+compensateExp.getmSumRealPayText();
			}else{
				exp = "";
				showTolal = false;
			}
			exp += "\r\n";

			if(CodeConstants.PayFlagType.INSTEAD_PAY.equals(compensateExp.getRecoveryOrPayFlag())){
				// compensateExp.setSubrogationBRealPay(recoveryPay);
				if(compensateExp.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_A1)){
					String dwPayText = String.format(CodeConstants.CompensateText.sumRealPayText_dwFinall_A1.value,
							MoneyFormator.format4Output(compensateExp.getSumRealPay()+compensateExp.getMsumRealPay()),
							MoneyFormator.format4Output(sumPaidAZF));
					compensateExp.setDwPayText(dwPayText);
					logger.debug(dwPayText);// sumRealPayText
				}else{
					String dwPayText = String.format(CodeConstants.CompensateText.sumRealPayText_dwFinall_A.value,
							MoneyFormator.format4Output(compensateExp.getSumRealPay()+compensateExp.getMsumRealPay()),
							MoneyFormator.format4Output(sumPaidAZF),MoneyFormator.format4Output(compensateExp.getThirdPaidAmt()));
					compensateExp.setDwPayText(dwPayText);
					logger.debug(dwPayText);// sumRealPayText
				}
			}
			DecimalFormat df = new DecimalFormat("#.00");// 四舍五入保留后两位，解决加法相差一分钱问题
			if(showTolal){
				String totalPayText = "";
				if(compensateExp.getMsumRealPay()>0){
					Double sumFinalAmt = Double.parseDouble(df.format(compensateExp.getSumRealPay()))+Double.parseDouble(df.format(compensateExp
							.getMsumRealPay()));
					totalPayText = String.format(CodeConstants.CompensateText.totalPayText.value,
							MoneyFormator.format4Output(compensateExp.getSumRealPay()),MoneyFormator.format4Output(compensateExp.getMsumRealPay()),
							MoneyFormator.format4Output(sumFinalAmt));
				}

				if(CodeConstants.PayFlagType.INSTEAD_PAY.equals(compensateExp.getRecoveryOrPayFlag())){
					double recoveryPay = compensateExp.getSumRealPay()+compensateExp.getMsumRealPay()-sumPaidAZF-compensateExp.getThirdPaidAmt();// 追偿金额
					compensateExp.setSumRealPay(recoveryPay);// 此处更改了sumrealpay为追偿金额
					totalPayText += "\r\n"+this
							.format4Space(this.space(CodeConstants.SpaceCount.shorter)+compensateExp.getDwPayText()+"\r\n"+"="+MoneyFormator
									.format4Output(compensateExp.getSumRealPay())+"\r\n");
				}

				compensateExp.setTotalPayText(this.format4Space(totalPayText));

			}

			// 分险别累加核定（理算）金额 加上不计免赔金额-追偿时不加（已经包含在sumrealpay中）
			double sumRealPayForKind = 0;
			if(CodeConstants.PayFlagType.INSTEAD_PAY.equals(compensateExp.getRecoveryOrPayFlag())){
				sumRealPayForKind = compensateExp.getSumRealPay();
			}else{
				sumRealPayForKind = Double.parseDouble(df.format(compensateExp.getSumRealPay()))+Double.parseDouble(df.format(compensateExp
						.getMsumRealPay()));
			}
			makeItemSummary(compensateExp.getKindCode(),sumRealPayForKind,kindSumRealPayMap);

			String otherExp = "";

			// 最后的汇总公式
			String exceptDeductibleExp = "";
			if( ++lastIndex==compensateExpList.size()){
				// 第三行，放总金额。
				double kindMSumRealPay = 0;
				// 为M险组织展现公式
				if("1".equals(compensateExp.getFlagOfM())){
					if(exceptKindDeductMap.size()>0){
						exceptDeductibleExp += CodeConstants.CompensateText.text4.value+"=";
						String mkindStr = ""; // 第一行，放险别代码
						String mSumRealPayStr = ""; // 第二行，放对应的金额
						for(Iterator iter = exceptKindDeductMap.keySet().iterator(); iter.hasNext();){
							// 取出险别。
							String keyValue = (String)iter.next();
							// 计算书内无需国际化。
							kindName = CodeConstants.KINDCODE_MAP.get(keyValue);
							mkindStr += kindName;
							// 取出对应的mSumRealPay。
							Double mSumRealPay = exceptKindDeductMap.get(keyValue);
							mSumRealPayStr += MoneyFormator.format4Output(mSumRealPay);
							kindMSumRealPay += mSumRealPay;
							if(iter.hasNext()){
								mkindStr += "+";
								mSumRealPayStr += "+";
							}
						}
						if(exceptKindDeductMap.size()==1){
							exceptDeductibleExp += mkindStr+"\r\n"+space(CodeConstants.SpaceCount.normal)+"="+mSumRealPayStr+"\r\n"+"\r\n";
						}else{
							exceptDeductibleExp += mkindStr+"\r\n"+space(CodeConstants.SpaceCount.normal)+"="+mSumRealPayStr+"\r\n"+space(CodeConstants.SpaceCount.longer)+"="+MoneyFormator
									.format4Output(kindMSumRealPay)+"\r\n";
						}
					}
				}
				exceptDeductibleExp = this.format4Space(exceptDeductibleExp);

				String sumThisPayExp = CodeConstants.CompensateText.text5.value+"=";
				// 第一行，放险别代码。
				String kindStr = "";
				// 第二行，放对应的金额。
				String sumRealPayStr = "=";
				// 第三行，放总金额。
				double kindSumRealPay = 0;

				Iterator<String> it = kindSumRealPayMap.keySet().iterator();
				while(it.hasNext()){
					// 取出险别
					String keyValue = it.next();
					// 取出对应的sumRealPay。
					Double sumRealPay = kindSumRealPayMap.get(keyValue);
					sumRealPayStr += MoneyFormator.format4Output(sumRealPay)+"+";

					// 计算书内无需国际化。
					if (CodeConstants.KINDCODE.KINDCODE_BP.equals(keyValue) || CodeConstants.KINDCODE.KINDCODE_D11P.equals(keyValue) || CodeConstants.KINDCODE.KINDCODE_D12P.equals(keyValue)
							||CodeConstants.KINDCODE.KINDCODE_BS.equals(keyValue) || CodeConstants.KINDCODE.KINDCODE_D11S.equals(keyValue) || CodeConstants.KINDCODE.KINDCODE_D12S.equals(keyValue)){
						kindName = CodeConstants.KINDCODE_MAP.get(compensateExp.getRiskCode()+keyValue);
					}else {
						kindName = CodeConstants.KINDCODE_MAP.get(keyValue);
					}
					kindStr += kindName+"+";

					kindSumRealPay += sumRealPay;
				}
				// 删除最后一个空格。
				sumRealPayStr = sumRealPayStr.substring(0,sumRealPayStr.length()-1);
				kindStr = kindStr.substring(0,kindStr.length()-1);

				// 险别赔付合计需要加上M险的
				if(exceptKindDeductMap.size()>0&&"1".equals(compensateExp.getFlagOfM())){
					kindStr += "+"+CodeConstants.CompensateText.text4.value;
					sumRealPayStr += "+"+MoneyFormator.format4Output(kindMSumRealPay);
					kindSumRealPay += kindMSumRealPay;
				}
				if(compensateExpList.size()==1&&exceptKindDeductMap.size()<1){
					sumThisPayExp += kindStr+"\r\n"+space(CodeConstants.SpaceCount.normal)+sumRealPayStr;
				}else{
					sumThisPayExp += kindStr+"\r\n"+space(CodeConstants.SpaceCount.normal)+sumRealPayStr+"\r\n"+space(CodeConstants.SpaceCount.longer)+"="+MoneyFormator
							.format4Output(kindSumRealPay);
				}

				sumThisPayExp = this.format4Space(sumThisPayExp);

				otherExp = exceptDeductibleExp+sumThisPayExp;
			}

			compensateExp.setCompensateExp(exp);
			compensateExp.setExtraCompensateExp(extraExp);
			compensateExp.setOtherExp(otherExp);
			list.add(compensateExp);
		}
		return list;
	}

	public CompensateVo rebuildBiCompensateVo(CompensateVo compensateVo,List<CompensateExp> compensateExpList) {

		PrpLCompensateVo prpLCompensateVo = compensateVo.getPrpLCompensateVo();
		List<PrpLCItemKindVo> exceptKindCodeList = compensateVo.getIsMitemKindList(); // 承保了不计免赔的险别
		List<MItemKindVo> mExceptKinds = compensateVo.getmExceptKinds(); // M险赔付金额对象

		StringBuffer strCompensateExp = new StringBuffer(""); // 理算分損失項计算公式
		StringBuffer compensateKindExp = new StringBuffer(""); // 理算分險別计算公式
		StringBuffer newCompensateExp = new StringBuffer(""); // 新格式理算报告
		// String isSuitFlagValue = this.isSuitCase(prpLCompensateVo);
		String isSuitFlagValue = "0";

		Map exceptDeductMap = new HashMap(); // 不计免赔Map
		Map kindExpMap = new HashMap(); // 险别理算公式Map
		Map extraExpMap = new HashMap(); // 多险别超限额理算公式Map
		Map kindNameMap = new HashMap();

		for(CompensateExp objCompensateExp:compensateExpList){
			final String wholeExp = objCompensateExp.getCompensateExp()+objCompensateExp.getExtraCompensateExp()+objCompensateExp.getTotalPayText()+objCompensateExp
					.getOtherExp();

			final String expType = objCompensateExp.getExpType();

			if(CodeConstants.ExpType.CAR.equals(expType)){
				PrpLLossItemVo prpLLossItemVo = (PrpLLossItemVo)objCompensateExp.getLossItem();
				String kindCode = prpLLossItemVo.getKindCode();
				if(kindCode.equals(objCompensateExp.getKindCode())){
					// 同一险别的不记免赔金额做累加。
					if("1".equals(objCompensateExp.getCheckPolicyM())&& !"1".equals(isSuitFlagValue)){
						makeItemSummary(kindCode,objCompensateExp.getMsumRealPay(),exceptDeductMap);
					}

					strCompensateExp.append(wholeExp);
					// 同一險別的理算公式整理
					if(objCompensateExp.getCompensateExp()!=null){
						makeItemSummary(kindCode,objCompensateExp.getCompensateExp(),kindExpMap);
					}
					if(objCompensateExp.getClaimRate()>=0&&objCompensateExp.getClaimRate()<1){
						makeItemSummary(kindCode,objCompensateExp.getExtraCompensateExp(),extraExpMap);
					}else{
						if("1".equals(objCompensateExp.getOverFlag())){
							makeItemSummary(kindCode,objCompensateExp.getExtraCompensateExp(),extraExpMap);
						}
					}
					if( !"1".equals(objCompensateExp.getIsSuitFlag())){
						prpLLossItemVo.setSumRealPay(MoneyFormator.format(new BigDecimal(objCompensateExp.getSumRealPay())));
						prpLLossItemVo.setClaimRate(MoneyFormator.format(new BigDecimal(objCompensateExp.getClaimRate())));
					}
					// 往对应的carLoss,personLoss,propLoss,损失项下存放对应的免赔金额.
					if( !"1".equals(isSuitFlagValue)){
						prpLLossItemVo.setDeductOffAmt(MoneyFormator.format(new BigDecimal(objCompensateExp.getMsumRealPay())));
					}else{
						prpLLossItemVo.setDeductOffAmt(MoneyFormator.format(BigDecimal.ZERO));
					}
				}
			}else if(CodeConstants.ExpType.PROP.equals(expType)){
				PrpLLossPropVo prpLLossPropVo = (PrpLLossPropVo)objCompensateExp.getLossItem();
				String kindCode = prpLLossPropVo.getKindCode();
				if(kindCode.equals(objCompensateExp.getKindCode())){
					// 同一险别的不记免赔金额做累加
					if("1".equals(objCompensateExp.getCheckPolicyM())&& !"1".equals(isSuitFlagValue)){
						makeItemSummary(kindCode,objCompensateExp.getMsumRealPay(),exceptDeductMap);
					}
					strCompensateExp.append(wholeExp);
					// 同一險別的理算公式整理
					if(objCompensateExp.getCompensateExp()!=null){
						makeItemSummary(kindCode,objCompensateExp.getCompensateExp(),kindExpMap);
					}
					if(objCompensateExp.getClaimRate()>=0&&objCompensateExp.getClaimRate()<1){
						makeItemSummary(kindCode,objCompensateExp.getExtraCompensateExp(),extraExpMap);
					}else{
						if("1".equals(objCompensateExp.getOverFlag())){
							makeItemSummary(kindCode,objCompensateExp.getExtraCompensateExp(),extraExpMap);
						}
					}
					if( !"1".equals(objCompensateExp.getIsSuitFlag())){
						prpLLossPropVo.setSumRealPay(MoneyFormator.format(new BigDecimal(objCompensateExp.getSumRealPay())));
						prpLLossPropVo.setClaimRate(MoneyFormator.format(new BigDecimal(objCompensateExp.getClaimRate())));
					}
					// 往对应的carLoss,personLoss,propLoss,损失项下存放对应的免赔金额.
					if( !"1".equals(isSuitFlagValue)){
						prpLLossPropVo.setDeductOffAmt(MoneyFormator.format(new BigDecimal(objCompensateExp.getMsumRealPay())));
					}else{
						prpLLossPropVo.setDeductOffAmt(BigDecimal.ZERO);
					}
				}
			}else if(CodeConstants.ExpType.PERSON.equals(expType)){

				PrpLLossPersonVo personFeeVo = (PrpLLossPersonVo)objCompensateExp.getLossItem();

				String kindCode = objCompensateExp.getKindCode();
				// 同一险别的不记免赔金额做累加
				if("1".equals(objCompensateExp.getCheckPolicyM())&& !"1".equals(isSuitFlagValue)){
					makeItemSummary(kindCode,objCompensateExp.getMsumRealPay(),exceptDeductMap);
				}
				strCompensateExp.append(wholeExp);
				// 同一險別的理算公式整理
				if(objCompensateExp.getCompensateExp()!=null){
					makeItemSummary(kindCode,objCompensateExp.getCompensateExp(),kindExpMap);
				}
				if(objCompensateExp.getClaimRate()>=0&&objCompensateExp.getClaimRate()<1){
					makeItemSummary(kindCode,objCompensateExp.getExtraCompensateExp(),extraExpMap);
				}else{
					if("1".equals(objCompensateExp.getOverFlag())){
						makeItemSummary(kindCode,objCompensateExp.getExtraCompensateExp(),extraExpMap);
					}
				}
				if( !"1".equals(objCompensateExp.getIsSuitFlag())){
					personFeeVo.setSumRealPay(MoneyFormator.format(new BigDecimal(objCompensateExp.getSumRealPay())));
					// prpLLossPersonVo.setClaimRate(MoneyFormator.format(new BigDecimal(objCompensateExp.getClaimRate())));
				}
				// 往对应的carLoss,personLoss,propLoss,损失项下存放对应的免赔金额.
				if( !"1".equals(isSuitFlagValue)){
					personFeeVo.setDeductOffAmt(MoneyFormator.format(new BigDecimal(objCompensateExp.getMsumRealPay())));
				}else{
					personFeeVo.setDeductOffAmt(BigDecimal.ZERO);
				}
			}else if(CodeConstants.ExpType.OTHER.equals(expType)){
				PrpLLossPropVo prpLLossPropVo = (PrpLLossPropVo)objCompensateExp.getLossItem();
				String kindCode = prpLLossPropVo.getKindCode();
				if(kindCode.equals(objCompensateExp.getKindCode())){
					// 同一险别的不记免赔金额做累加
					if("1".equals(objCompensateExp.getCheckPolicyM())&& !"1".equals(isSuitFlagValue)){
						makeItemSummary(kindCode,objCompensateExp.getMsumRealPay(),exceptDeductMap);
					}
					strCompensateExp.append(wholeExp);
					// 同一險別的理算公式整理
					if(objCompensateExp.getCompensateExp()!=null){
						makeItemSummary(kindCode,objCompensateExp.getCompensateExp(),kindExpMap);
					}
					if(objCompensateExp.getClaimRate()>=0&&objCompensateExp.getClaimRate()<1){
						makeItemSummary(kindCode,objCompensateExp.getExtraCompensateExp(),extraExpMap);
					}else{
						if("1".equals(objCompensateExp.getOverFlag())){
							makeItemSummary(kindCode,objCompensateExp.getExtraCompensateExp(),extraExpMap);
						}
					}
					if( !"1".equals(objCompensateExp.getIsSuitFlag())){
						prpLLossPropVo.setSumRealPay(MoneyFormator.format(new BigDecimal(objCompensateExp.getSumRealPay())));
						prpLLossPropVo.setClaimRate(MoneyFormator.format(new BigDecimal(objCompensateExp.getClaimRate())));
					}
					// 往对应的carLoss,personLoss,propLoss,损失项下存放对应的免赔金额.
					if( !"1".equals(isSuitFlagValue)){
						prpLLossPropVo.setDeductOffAmt(MoneyFormator.format(new BigDecimal(objCompensateExp.getMsumRealPay())));
					}else{
						prpLLossPropVo.setDeductOffAmt(BigDecimal.ZERO);
					}
				}
			}

			Object lossItem = objCompensateExp.getLossItem();
			String kindCode = (String)Refs.get(lossItem,"kindCode");
			for(MItemKindVo vo:mExceptKinds){
				if(vo.getKindCode().equals(kindCode)){
					double exceptKindDeductibleRate = 0;
					if( !"1".equals(objCompensateExp.getIsSuitFlag())){
						exceptKindDeductibleRate = ( (BigDecimal)Refs.get(lossItem,"deductOffRate") ).doubleValue();
					}
					vo.setExceptKindDeductibleRate(exceptKindDeductibleRate);
				}
			}
		}

		for(PrpLCItemKindVo prpLcItemKindVo:exceptKindCodeList){
			String kindCode = prpLcItemKindVo.getKindCode();
			double exceptKindDeductible = 0.0d;
			if(exceptDeductMap.get(kindCode)!=null){
				exceptKindDeductible = ( Double.parseDouble(exceptDeductMap.get(kindCode).toString()) );
			}
			for(MItemKindVo vo:mExceptKinds){
				if(vo.getKindCode().equals(kindCode)){
					vo.setExceptKindDeductible(exceptKindDeductible);
				}
			}
		}

		// 組織出按險別顯示的理算公式
		List<PrpLCItemKindVo> prpLcItemKinds = compensateVo.getPrpLCItemKindVoList();
		for(PrpLCItemKindVo prpLcItemKind:prpLcItemKinds){
			kindNameMap.put(prpLcItemKind.getKindCode(),prpLcItemKind.getKindName());
		}

		if(compensateExpList.size()>0){
			compensateKindExp.append(compensateExpList.get(0).getOtherExp());
		}

		this.fillSumBzPaid_SumPaid_SumThisPaid_SumNoDeductFee4Payment(prpLCompensateVo);

		newCompensateExp = strCompensateExp;

		// 诉讼案需要追加诉讼案计算书。 讼案的计算书不管
		// if(this.isSuitCase(prpLCompensateVo).equals("1")){
		// this.fillSuitCaseText(prpLCompensateVo);
		// }else{
		// prpLCompensateVo.setLcText(newCompensateExp.toString());
		// }
		prpLCompensateVo.setLcText(newCompensateExp.toString());

		// logger.debug(prpLCompensateVo.getLcText());
		return compensateVo;
	}

	/**
	 * 计算。用于代替人保的规则引擎方法/Compensate_getSumRealPayApp/Compensate_getSumRealPayRule。
	 * @param compensateList
	 */
	private CompensateListVo calculate(CompensateListVo compensateListVo,Boolean cprcCase) {
		List<CompensateExp> compensateAddList =compensateListVo.getCompensateList();
		if(compensateAddList!=null && compensateAddList.size()>0){
			for(CompensateExp exp:compensateAddList){
				if(CodeConstants.KINDCODE.KINDCODE_A.equals(exp.getKindCode())){
					List<PrpLCItemKindVo> prpLCItemKindVos= registQueryService.findItemKindVo(compensateListVo.getRegistNo(), CodeConstants.KINDCODE.KINDCODE_AG);
					if(prpLCItemKindVos!=null && prpLCItemKindVos.size()>0){
						    exp.setBugFlag(true);
						    exp.setAbsrate(prpLCItemKindVos.get(0).getValue()!=null?prpLCItemKindVos.get(0).getValue().doubleValue():0);
					}
				}else if(CodeConstants.KINDCODE.KINDCODE_B.equals(exp.getKindCode())){
					List<PrpLCItemKindVo> prpLCItemKindVos= registQueryService.findItemKindVo(compensateListVo.getRegistNo(), CodeConstants.KINDCODE.KINDCODE_BG);
					if(prpLCItemKindVos!=null && prpLCItemKindVos.size()>0){
						    exp.setBugFlag(true);
						    exp.setAbsrate(prpLCItemKindVos.get(0).getValue()!=null?prpLCItemKindVos.get(0).getValue().doubleValue():0);
					}
				}else if(CodeConstants.KINDCODE.KINDCODE_D11.equals(exp.getKindCode())){
					List<PrpLCItemKindVo> prpLCItemKindVos= registQueryService.findItemKindVo(compensateListVo.getRegistNo(), CodeConstants.KINDCODE.KINDCODE_D11G);
					if(prpLCItemKindVos!=null && prpLCItemKindVos.size()>0){
						    exp.setBugFlag(true);
						    exp.setAbsrate(prpLCItemKindVos.get(0).getValue()!=null?prpLCItemKindVos.get(0).getValue().doubleValue():0);
					}
				}else if(CodeConstants.KINDCODE.KINDCODE_D12.equals(exp.getKindCode())){
					List<PrpLCItemKindVo> prpLCItemKindVos= registQueryService.findItemKindVo(compensateListVo.getRegistNo(), CodeConstants.KINDCODE.KINDCODE_D12G);
					if(prpLCItemKindVos!=null && prpLCItemKindVos.size()>0){
						    exp.setBugFlag(true);
						    exp.setAbsrate(prpLCItemKindVos.get(0).getValue()!=null?prpLCItemKindVos.get(0).getValue().doubleValue():0);
					}
				}else if(CodeConstants.KINDCODE.KINDCODE_G.equals(exp.getKindCode())){
					List<PrpLCItemKindVo> prpLCItemKindVos= registQueryService.findItemKindVo(compensateListVo.getRegistNo(), CodeConstants.KINDCODE.KINDCODE_GG);
					if(prpLCItemKindVos!=null && prpLCItemKindVos.size()>0){
						    exp.setBugFlag(true);
						    exp.setAbsrate(prpLCItemKindVos.get(0).getValue()!=null?prpLCItemKindVos.get(0).getValue().doubleValue():0);
					}
				}
			}
		}
		// 超限额控制.超限额调整获取。
		this.mutualCalculate(compensateListVo);

		List<CompensateExp> compensateListCompensateList = compensateListVo.getCompensateList();
		for(CompensateExp compensateExp:compensateListCompensateList){

			// 交强计算书。
			// 只要险别是交强险都按交强险计算，因为在商业险理算有可能做获取交强理算金额的操作。
			// zhongyuhai 20120926
			if(CodeConstants.KINDCODE.KINDCODE_BZ.equals(compensateExp.getKindCode())){
				this.calculateCi(compensateListVo,compensateExp);

			}else{
				// 商业计算书。
				if(this.isCommercialOrCompulsory(compensateExp.getCompensateType()+"")){
					this.calculateBi(compensateListVo,compensateExp,cprcCase);
				}
			}

			// 人保规则：设置返回的理算大对象List。
			{
				compensateListVo.setCompensateExpInfo(compensateExp);
			}
		}

		return compensateListVo;
	}

	/**
	 * 用于代替人保的规则引擎方法/Compensate_getSumRealPayApp/Compensate_getSumRealPayRule中交强商业的共同部分。
	 * @param compensateList
	 */
	private CompensateListVo mutualCalculate(CompensateListVo compensateListVo) {
		// 人保规则：初始化协议金额。
		// 如果诉讼案，协议金额为0。
		if("1".equals(compensateListVo.getIsSuitFlag())){
			compensateListVo.setExgratiaFee(0d);
		}

		// 人保规则：超限额调整获取赔付比例。
		// 1.人保规则：组织分险别赔付信息Map。
		// 作用：将 险别赔付分项信息 汇总为 险别赔付信息。
		{
			
			List<CompensateExp> compensateListCompensateList = compensateListVo.getCompensateList();
			for(CompensateExp compensateExp:compensateListCompensateList){
				compensateListVo.setKindPayInfoMap(compensateExp);
			}
		}

		// 2.人保规则：组织分险别赔付信息List。
		// 作用：将原有以Map格式存放的 险别赔付信息 ， 转换为一个List格式存放。
		{
			compensateListVo.copyMapToList();
		}

		// 3.人保规则：获取赔付比例。
		{
			List<CompensateKindPay> kindPayList = compensateListVo.getReturnKindPayList();
			for(CompensateKindPay compensateKindPay:kindPayList){
				final String kindCode = compensateKindPay.getKindCode();
				List<CompensateExp> compensateExpList = compensateKindPay.getCompensateExpList();
				for(CompensateExp compensateExp:compensateExpList){
					// 险别总责任比例金额大于险别限额，并且系险别损失项多余1个，并且不是车上人员险。
					if(compensateKindPay.getAdjKindSum()>compensateKindPay.getDamageAmount()&&compensateKindPay.getDamageAmount()>0d&&compensateKindPay
							.getKindLossNum()!=1&& !kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D11)&& !kindCode
							.equals(CodeConstants.KINDCODE.KINDCODE_D12)
					// && !kindCode.equals(CodeConstants.KINDCODE.KINDCODE_R)
					){
						compensateExp.setClaimRate(compensateKindPay.getDamageAmount()/compensateKindPay.getKindSum());
					}else{
						compensateExp.setClaimRate(1d);
					}
				}
			}
		}

		// 4.获取赔付比例$_$车辆实际价值控制。
		{
			List<CompensateKindPay> kindPayList = compensateListVo.getReturnKindPayList();
			for(CompensateKindPay compensateKindPay:kindPayList){
				final String kindCode = compensateKindPay.getKindCode();
				boolean isKindCode = kindCode.equals(CodeConstants.KINDCODE.KINDCODE_A)||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_X1)||kindCode
						.equals(CodeConstants.KINDCODE.KINDCODE_G)||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_F);
				List<CompensateExp> compensateExpList = compensateKindPay.getCompensateExpList();
				for(CompensateExp compensateExp:compensateExpList){
					boolean condition1 = compensateKindPay.getAdjKindSum()>compensateKindPay.getDamageAmount()&&compensateKindPay.getKindLossNum()!=1&&compensateExp
							.getDamageItemRealCost()<=compensateExp.getDamageAmount()&&isKindCode;
					boolean condition2 = compensateKindPay.getAdjKindSum()>compensateKindPay.getDamageItemRealCost()&&compensateKindPay
							.getDamageAmount()==0d&&isKindCode;
					if(condition1||condition2){
						compensateExp.setClaimRate(compensateKindPay.getDamageAmount()/compensateKindPay.getKindSum());
					}
				}
			}
		}

		// B 险按超过保额则获取比例
		{
			List<CompensateKindPay> kindPayList = compensateListVo.getReturnKindPayList();
			double sumKindB = 0d;
			List<CompensateExp> compensateExpList = new ArrayList<CompensateExp>();
			CompensateKindPay compensateKindPay = null;
			for(CompensateKindPay kindPay:kindPayList){
				if(kindPay.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_B)){
					sumKindB = kindPay.getKindSum();
					compensateExpList = kindPay.getCompensateExpList();
					compensateKindPay = kindPay;
					break;
				}
			}

			double sumRate = 0d;
			for(int i = 0; i<compensateExpList.size(); i++ ){
				CompensateExp compensateExp = compensateExpList.get(i);
				if(sumKindB>compensateExp.getAmount()){
					compensateExp.setOverFlag("1");
					double sumLoss = compensateExp.getSumLoss()+compensateExp.getRescueFee()-compensateExp.getbZPaid()-compensateExp
							.getOtherDeductFee();
					double rate = 0d;

					if(i==compensateExpList.size()-1){
						rate = 1-sumRate;
					}else{
						rate = sumLoss*compensateExp.getIndemnityDutyRate()*(1-compensateExp.getAbsrate()/100)/sumKindB;
						sumRate = sumRate+rate;
					}

					compensateExp.setClaimRate(rate);
				}else{
					compensateExp.setOverFlag("0");
					compensateExp.setClaimRate(1d);
				}
			}

		}

		// 5.组织D1险人员损失数据。
		{
			List<CompensateKindPay> kindPayList = compensateListVo.getReturnKindPayList();
			for(CompensateKindPay compensateKindPay:kindPayList){
				final String kindCode = compensateKindPay.getKindCode();
				boolean isKindCode = kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D11)||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D12);
				if(isKindCode){
					List<CompensateExp> compensateExpList = compensateKindPay.getCompensateExpList();
					for(CompensateExp compensateExp:compensateExpList){
						if(compensateKindPay.getKindLossNum()>0){
							compensateListVo.organizePersonData(compensateExp);
						}
					}
				}
			}
		}

		// 6.获取D1险赔付比例。 车上人员责任险（乘客）
		{
			List<CompensateKindPay> kindPayList = compensateListVo.getReturnKindPayList();
			for(CompensateKindPay compensateKindPay:kindPayList){
				final String kindCode = compensateKindPay.getKindCode();
				boolean isKindCode = kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D12);
				if(isKindCode){
					List<CompensateExp> compensateExpList = compensateKindPay.getCompensateExpList();
					for(CompensateExp compensateExp:compensateExpList){
						if(compensateKindPay.getKindLossNum()>0){
							compensateExp.setClaimRate(compensateListVo.judgeQuota(compensateExp,compensateExp.getUnitAmount()));
						}
					}
				}
			}
		}

		// 7.获取D1险车上司机赔付比例。
		{
			List<CompensateKindPay> kindPayList = compensateListVo.getReturnKindPayList();
			for(CompensateKindPay compensateKindPay:kindPayList){
				final String kindCode = compensateKindPay.getKindCode();
				boolean isKindCode = kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D11);
				if(isKindCode){
					List<CompensateExp> compensateExpList = compensateKindPay.getCompensateExpList();
					for(CompensateExp compensateExp:compensateExpList){
						if(compensateKindPay.getKindLossNum()>0){
							compensateExp.setClaimRate(compensateListVo.judgeQuota(compensateExp,compensateExp.getAmount()));
						}
					}

				}
			}
		}

		// 8.获取D1险超限额标志位。
		{
			List<CompensateKindPay> kindPayList = compensateListVo.getReturnKindPayList();
			for(CompensateKindPay compensateKindPay:kindPayList){
				final String kindCode = compensateKindPay.getKindCode();
				boolean isKindCode = kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D12);
				if(isKindCode){

					List<CompensateExp> compensateExpList = compensateKindPay.getCompensateExpList();
					for(CompensateExp compensateExp:compensateExpList){
						if(compensateKindPay.getKindLossNum()>0){
							compensateExp.setOverFlag(compensateListVo.judgeOverFlag(compensateExp,compensateExp.getUnitAmount()));
						}
					}
				}
			}
		}

		// 9.获取D1险车上司机超限额标志位。
		{
			List<CompensateKindPay> kindPayList = compensateListVo.getReturnKindPayList();
			for(CompensateKindPay compensateKindPay:kindPayList){
				final String kindCode = compensateKindPay.getKindCode();
				boolean isKindCode = kindCode.equals(CodeConstants.KINDCODE.KINDCODE_R)||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D12);
				if(isKindCode){
					List<CompensateExp> compensateExpList = compensateKindPay.getCompensateExpList();
					for(CompensateExp compensateExp:compensateExpList){
						if(compensateKindPay.getKindLossNum()>0){
							compensateExp.setOverFlag(compensateListVo.judgeOverFlag(compensateExp,compensateExp.getAmount()));
						}
					}
				}
			}
		}

		// 对R和SS(精神损害赔偿责任)的赔付比例和超限额标志位设置。
		{
			List<CompensateKindPay> kindPayList = compensateListVo.getReturnKindPayList();
			double sum4SF714 = 0d;
			List<CompensateExp> compensateExpList = new ArrayList<CompensateExp>();
			CompensateKindPay compensateKindPay = null;
			for(CompensateKindPay kindPay:kindPayList){
				if(kindPay.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_R)||kindPay.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_SS) || kindPay.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_BS) || kindPay.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_D11S) || kindPay.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_D12S)){
					sum4SF714 = kindPay.getKindSum();
					compensateExpList = kindPay.getCompensateExpList();
					compensateKindPay = kindPay;
					break;
				}
			}
			for(CompensateExp compensateExp:compensateExpList){
				if(compensateExp.getSumLoss()>compensateExp.getUnitAmount()||sum4SF714>compensateExp.getUnitAmount()){
					compensateExp.setOverFlag("1");
					compensateExp.setClaimRate(compensateExp.getSumLoss()/sum4SF714);
				}else{
					compensateExp.setOverFlag("0");
					compensateExp.setClaimRate(1d);
				}
			}
		}

		// 10.获取赔付比例$40$施救费$41$。
		{
			List<CompensateKindPay> kindPayList = compensateListVo.getReturnKindPayList();
			for(CompensateKindPay compensateKindPay:kindPayList){
				final String kindCode = compensateKindPay.getKindCode();
				boolean kindCodeA = kindCode.equals(CodeConstants.KINDCODE.KINDCODE_A);
				boolean kindCodeB = kindCode.equals(CodeConstants.KINDCODE.KINDCODE_B);
				boolean kindCodeZ = kindCode.equals(CodeConstants.KINDCODE.KINDCODE_Z);
				List<CompensateExp> compensateExpList = compensateKindPay.getCompensateExpList();
				for(CompensateExp compensateExp:compensateExpList){
					if(compensateKindPay.getKindRescueSum()>compensateKindPay.getDamageAmount()&&compensateKindPay.getDamageAmount()>0d&&compensateKindPay
							.getKindLossNum()!=1&&( kindCodeA||kindCodeB||kindCodeZ )){
						compensateExp.setClaimRescueRate(compensateKindPay.getDamageAmount()/compensateKindPay.getKindRescueSum());
					}else{
						compensateExp.setClaimRescueRate(1d);
					}
				}
			}
		}

		// 11.赔付损失项加入赔付比例。
		{
			List<CompensateExp> compensateListCompensateList = compensateListVo.getCompensateList();
			for(CompensateExp exp:compensateListCompensateList){
				List<CompensateKindPay> returnKindPayList = compensateListVo.getReturnKindPayList();
				for(CompensateKindPay kindPay:returnKindPayList){
					List<CompensateExp> returnKindPayListCompensateList = kindPay.getCompensateExpList();
					for(CompensateExp expInternal:returnKindPayListCompensateList){
						if(exp.getIndex()==expInternal.getIndex()){
							exp.setClaimRate(expInternal.getClaimRate());
							exp.setClaimRescueRate(expInternal.getClaimRescueRate());
						}
					}
				}
			}
		}
		return compensateListVo;
	}

	/**
	 * 计算。用于代替人保的规则引擎方法/Compensate_getSumRealPayApp/Compensate_getSumRealPayRule的交强险部分。
	 * @param compensateList
	 * @param compensateExp
	 * @return
	 */
	public CompensateExp calculateCi(CompensateListVo compensateListVo,CompensateExp compensateExp) {

		// 人保规则：获取交强赔付限额。
		if("1".equals(compensateListVo.getReCaseFlag())){
			this.setLeftAmount(compensateListVo,compensateExp);
		}else{
			List<ThirdPartyInfo> thirdPartyInfos = compensateExp.getThirdPartyInfos();
			for(ThirdPartyInfo thirdPartyInfo:thirdPartyInfos){
				//thirdPartyInfo.setDutyAmount(KindCalculatorBz.calBzAmount(compensateExp.getDamageType(),thirdPartyInfo.isBzDutyType()));
				thirdPartyInfo.setDutyAmount(configService.calBzAmount(compensateExp.getDamageType(),thirdPartyInfo.isBzDutyType(),compensateListVo.getRegistNo()));
			}
		}

		if(compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)){
			if(CodeConstants.ClaimType.SPAY_CASE.equals(compensateExp.getClaimType())){
				this.calculateCi4Self(compensateListVo,compensateExp);
			}else{
				this.calculateCi4Common(compensateListVo,compensateExp);
			}
		}else{
			this.calculateCi4CommonPerson(compensateListVo,compensateExp);
		}

		return compensateExp;
	}

	/**
	 * 计算。用于代替人保的规则引擎方法/Compensate_getSumRealPayApp/Compensate_getSumRealPayRule的交强险互陪自赔部分。
	 * @param compensateList
	 * @param compensateExp
	 * @return
	 */
	private CompensateExp calculateCi4Self(CompensateListVo compensateListVo,CompensateExp compensateExp) {
		// 人保规则：交强试算$_$20080201$_$财产$_$互碰自赔。

		// 1.初始化损失项车牌号。
		{
			List<ThirdPartyLossInfo> thirdPartyLossInfos = compensateExp.getThirdPartyLossInfos();
			for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
				// 互碰自赔默认为主车
				thirdPartyLossInfo.setLossItemType("1");
				thirdPartyLossInfo.setBzDutyType("0");
			}
		}

		// 2.初始化本车损失项。
		// 功能：将本车车损各损失项
		{
			List<ThirdPartyLossInfo> thirdPartyLossInfos = compensateExp.getThirdPartyLossInfos();
			List<ThirdPartyInfo> infos = compensateExp.getThirdPartyInfos();
			for(ThirdPartyInfo thirdPartyInfo:infos){
				for(ThirdPartyLossInfo lossInfo:thirdPartyLossInfos){
					// 代码中使用了licenseNo字段来存放PrpLthirdParty.id字段。
					if(thirdPartyInfo.getSerialNo().equals(lossInfo.getSerialNo())){

						String bzCompensateText = String.format(CodeConstants.CompensateText.textComplex1.value,lossInfo.getLossName(),
								lossInfo.getLossFeeName(),MoneyFormator.format4Output(lossInfo.getSumLoss()));

						thirdPartyInfo.addThirdPartyPayAmount(lossInfo.getSumLoss(),bzCompensateText,lossInfo);
						logger.debug(bzCompensateText);
					}

				}
			}
		}

		// 3.本车赔偿金额合计。
		{
			List<ThirdPartyInfo> infos = compensateExp.getThirdPartyInfos();
			for(ThirdPartyInfo thirdPartyInfo:infos){
				List<ThirdPartyCompInfo> compInfos = thirdPartyInfo.getThirdPartyCompInfos();
				for(ThirdPartyCompInfo lossInfo:compInfos){
					thirdPartyInfo.setPayAmount(thirdPartyInfo.getPayAmount()+lossInfo.getPayAmount());
				}
			}
		}

		// 4.设置超限额标志位。
		{
			List<ThirdPartyInfo> infos = compensateExp.getThirdPartyInfos();
			List<ThirdPartyLossInfo> thirdPartyLossInfos = compensateExp.getThirdPartyLossInfos();
			for(ThirdPartyInfo thirdPartyInfo:infos){
				if(thirdPartyInfo.getPayAmount()>thirdPartyInfo.getDutyAmount()){
					List<ThirdPartyCompInfo> compInfos = thirdPartyInfo.getThirdPartyCompInfos();
					for(ThirdPartyCompInfo lossInfo:compInfos){
						for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
							if(lossInfo.getObjectIndex().equals(thirdPartyLossInfo.getObjectIndex())){
								thirdPartyLossInfo.setOverFlag("1");
							}
						}
					}
				}
			}
		}

		// 5.超限额调整。
		// 人保规则：rule $54$累计赔付金额
		{
			List<ThirdPartyInfo> infos = compensateExp.getThirdPartyInfos();
			for(ThirdPartyInfo thirdPartyInfo:infos){
				List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
				for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
					if(thirdPartyInfo.getDutyAmount()<thirdPartyInfo.getPayAmount()){
						thirdPartyCompInfo.setSumRealPay(thirdPartyCompInfo.getPayAmount()*thirdPartyInfo.getDutyAmount()/thirdPartyInfo
								.getPayAmount());

						String bzCompensateText = String.format(CodeConstants.CompensateText.textComplex2.value,
								thirdPartyCompInfo.getBzCompensateText(),thirdPartyCompInfo.getPayAmount(),
								MoneyFormator.format4Output(thirdPartyInfo.getDutyAmount()),
								MoneyFormator.format4Output(thirdPartyInfo.getPayAmount()),
								MoneyFormator.format4Output(thirdPartyCompInfo.getSumRealPay()));
						thirdPartyCompInfo.setBzCompensateText(thirdPartyCompInfo.getBzCompensateText()+bzCompensateText);
						logger.debug(bzCompensateText);
					}else{
						thirdPartyCompInfo.setSumRealPay(thirdPartyCompInfo.getPayAmount());
					}
				}
			}
		}

		// 6.按损失项顺序返回实赔。
		// 人保规则：
		{
			List<ThirdPartyLossInfo> thirdPartyLossInfos = compensateExp.getThirdPartyLossInfos();
			for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
				List<ThirdPartyInfo> thirdPartyInfos = compensateExp.getThirdPartyInfos();
				for(ThirdPartyInfo thirdPartyInfo:thirdPartyInfos){
					List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
					for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
						if(thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())){
							thirdPartyLossInfo.setSumRealPay(thirdPartyCompInfo.getSumRealPay()+thirdPartyLossInfo.getSumRealPay());
							thirdPartyLossInfo.setBzCompensateText(thirdPartyCompInfo.getBzCompensateText());
							thirdPartyLossInfo.setSumDefLoss(thirdPartyCompInfo.getPayAmount());
							thirdPartyLossInfo.setAllAmount(thirdPartyInfo.getPayAmount());
							thirdPartyLossInfo.setPayCarAmout(thirdPartyCompInfo.getPayCarAmount());
							thirdPartyLossInfo.setDamageType(compensateExp.getDamageType());
						}
					}
				}
			}
		}

		// 扣减交强支付金额。
		this.compulsorySubPrePay(compensateExp.getThirdPartyLossInfos());

		// 7.清空赔付损失车辆信息。
		{
			List<ThirdPartyInfo> infos = compensateExp.getThirdPartyInfos();
			for(ThirdPartyInfo thirdPartyInfo:infos){
				thirdPartyInfo.setLeftAmount(0d);
				thirdPartyInfo.setLeftPayAmount(0d);
				thirdPartyInfo.setAdjPayAmount(0d);
				thirdPartyInfo.setDutyCompPay(0d);
				thirdPartyInfo.setSumNoenoughPay(0d);
				thirdPartyInfo.setPayAmount(0d);
				thirdPartyInfo.setSumRealPay(0d);
				thirdPartyInfo.setCarPaySum(0d);
				thirdPartyInfo.setCarLossNum(0d);
				thirdPartyInfo.setCarLossSum(0d);
				thirdPartyInfo.deleteThirdPartyPayAmount();
			}
		}

		return compensateExp;
	}

	/**
	 * 计算。用于代替人保的规则引擎方法/Compensate_getSumRealPayApp/Compensate_getSumRealPayRule的交强险非互陪自赔部分理算。
	 * @param compensateList
	 * @param compensateExp
	 * @return
	 */
	private CompensateExp calculateCi4Common(CompensateListVo compensateListVo,CompensateExp compensateExp) {

		boolean bzFlag = false; // 本车交强责任类型
		double allDamageAmount = 0.00d; // 所有限额
		int noDutyCarNum = 0; // 无责方车辆个数
		int dutyCarNum = 0; // 有责方车辆个数
		int dutyCarNumWithLoss = 0;// 有损失的有责方车辆数量。
		String compensateType = compensateListVo.getCompensateType()+"";

		List<ThirdPartyInfo> infos = compensateExp.getThirdPartyInfos();
		List<ThirdPartyLossInfo> thirdPartyLossInfos = compensateExp.getThirdPartyLossInfos();

		ThirdPartyInfo mainCar = null;
		for(ThirdPartyInfo thirdPartyInfo:infos){
			if(thirdPartyInfo.getSerialNo()==1){ // 标的车
				bzFlag = thirdPartyInfo.isBzDutyType(); // 2.3初始化本车交强责任类型
				mainCar = thirdPartyInfo;
				break;
			}
		}

		// 2.4累计赔付限额计算
		for(ThirdPartyInfo thirdPartyInfo:infos){
			allDamageAmount += thirdPartyInfo.getDutyAmount();
		}

		// 2.5获取损失项归属车辆限额
		for(ThirdPartyInfo thirdPartyInfo:infos){
			for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
				if(thirdPartyInfo.getSerialNo().equals(thirdPartyLossInfo.getSerialNo())){ // 车牌号相同
					thirdPartyLossInfo.setDamageAmount(thirdPartyInfo.getDutyAmount()); // 归属车辆赔限额
					thirdPartyLossInfo.setLossItemType(thirdPartyInfo.getLossItemType()); // 归属车辆类型
					if(thirdPartyInfo.isBzDutyType()){
						thirdPartyLossInfo.setBzDutyType("1"); // 归属车辆交强责任类型
					}else{
						thirdPartyLossInfo.setBzDutyType("0"); // 归属车辆交强责任类型
					}
				}
			}
		}

		// 2.6计算赔付第三方金额
		// 计算第三方为本损失可赔付的比例。
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			logger.debug("\r\n 车辆 "+thirdPartyInfo.getSerialNo()+" 赔付其他");
			for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
				if( !thirdPartyLossInfo.getSerialNo().equals(thirdPartyInfo.getSerialNo())){
					// 无责车不赔付路面损失。
					// zhongyuhai 20121023
					if( !thirdPartyInfo.isBzDutyType()&&( thirdPartyLossInfo.getSerialNo()==0|| !thirdPartyLossInfo.isBzDutyType() )){
						continue;
					}else{
						double payAmount = 0d;
						if(allDamageAmount-thirdPartyLossInfo.getDamageAmount()>0){
							payAmount = thirdPartyLossInfo.getSumLoss()*thirdPartyInfo.getDutyAmount()/( allDamageAmount-thirdPartyLossInfo
									.getDamageAmount() );
						}

						String bzCompensateText = String.format(CodeConstants.CompensateText.payAmountText.value,thirdPartyLossInfo.getLossName(),
								thirdPartyLossInfo.getLossFeeName(),MoneyFormator.format4Output(thirdPartyLossInfo.getSumLoss()),
								MoneyFormator.format4Output(thirdPartyInfo.getDutyAmount()),
								MoneyFormator.format4Output(allDamageAmount-thirdPartyLossInfo.getDamageAmount()),
								MoneyFormator.format4Output(payAmount));

						bzCompensateText = "";
						thirdPartyInfo.addThirdPartyPayAmount(payAmount,bzCompensateText,thirdPartyLossInfo);
						logger.debug(bzCompensateText);
					}
				}
			}
		}

		// 初始化代赔标志位。
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
			for(ThirdPartyCompInfo carThirdPartyCompInfo:carThirdPartyCompInfos){ // 车辆下赔付损失分项
				carThirdPartyCompInfo.setInsteadFlag(CodeConstants.InsteadFlag.NO_INSTEAD); // 非代赔
			}
		}
		// if( !this.isCommercialOrCompulsory(compensateType)){
		// 2.23设置代赔标志位
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车

			String insteadFlag = CodeConstants.InsteadFlag.THIRD2THIRD_INSTEAD;
			if(thirdPartyInfo.getSerialNo()!=1){
				// 三者无责、主车有责。
				if( !thirdPartyInfo.isBzDutyType()&&mainCar.isBzDutyType()){
					insteadFlag = CodeConstants.InsteadFlag.NODUTY_INSTEAD;
				}
			}

			List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
			for(ThirdPartyCompInfo carThirdPartyCompInfo:carThirdPartyCompInfos){ // 车辆下赔付损失分项
				if(carThirdPartyCompInfo.getSerialNo()==1){
					carThirdPartyCompInfo.setInsteadFlag(insteadFlag);
				}else{
					if(thirdPartyInfo.getSerialNo()==1){
						carThirdPartyCompInfo.setInsteadFlag(CodeConstants.InsteadFlag.NO_INSTEAD); // 三者代赔三者
					}else{
						carThirdPartyCompInfo.setInsteadFlag(CodeConstants.InsteadFlag.THIRD2THIRD_INSTEAD); // 三者代赔三者
						if( !thirdPartyInfo.isBzDutyType()&&carThirdPartyCompInfo.isBzDutyType()){
							carThirdPartyCompInfo.setInsteadFlag(CodeConstants.InsteadFlag.NODUTY_INSTEAD); // 三者代赔三者
						}
					}
				}
			}
		}
		// }

		// 交强试算_20080201_财产_代赔
		if("1".equals(compensateListVo.getBzDutyCar())){ // 存在有责车辆
			for(ThirdPartyInfo thirdPartyInfo:infos){
				// 2.7.1无责方车辆个数
				if( !thirdPartyInfo.isBzDutyType()&&thirdPartyInfo.getDutyAmount()==100d&&"1".equals(thirdPartyInfo.getNodutyPayFlag())&&compensateExp
						.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)){ // 无责,车财损失
					noDutyCarNum++ ;
				}
				// 2.7.2有责方车辆个数
				if(thirdPartyInfo.isBzDutyType()&&compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)){ // 有责,车财损失
					dutyCarNum++ ;
				}
			}

			// 2.7.3有车辆的损失个数。
			// 统计车辆的损失项数量。
			for(ThirdPartyInfo thirdPartyInfo:infos){
				for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
					if(compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)&&thirdPartyInfo.getSerialNo().equals(
							thirdPartyLossInfo.getSerialNo())&&thirdPartyLossInfo.getSumLoss()!=0.00){ // 车财损失
						thirdPartyInfo.setCarLossNum(thirdPartyInfo.getCarLossNum()+1);
					}
				}
			}

			// 计算有损有责车辆数量。dutyCarNumWithLoss 暂时不用
			// zhongyuhai 20121017
			// for(ThirdPartyInfo thirdPartyInfo:infos){
			// if(thirdPartyInfo.getCarLossNum()>0&&thirdPartyInfo.isBzDutyType()){
			// dutyCarNumWithLoss++ ;
			// }
			// }

			// 2.7.5无责方赔付有责方
			// 人保规则：rule 新财产计算$40$1_a无责方赔付有责方$41$
			for(ThirdPartyInfo thirdPartyInfo:infos){
				if( !thirdPartyInfo.isBzDutyType()&&thirdPartyInfo.getDutyAmount()==100d&&"1".equals(thirdPartyInfo.getNodutyPayFlag())){// 赔付损失车辆无责
					List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
					for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
						if(compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)&& // 车财损失
						thirdPartyCompInfo.isBzDutyType()){ // 归属车辆交强有责

							thirdPartyCompInfo.setSumRealPay(thirdPartyCompInfo.getSumLoss()/noDutyCarNum); // 损失金额 / 无责方车辆个数

							String bzCompensateText = String.format(CodeConstants.CompensateText.noDutyPayText.value,
									thirdPartyCompInfo.getLossName(),MoneyFormator.format4Output(thirdPartyCompInfo.getSumLoss()),noDutyCarNum,
									MoneyFormator.format4Output(thirdPartyCompInfo.getSumLoss()/noDutyCarNum));

							thirdPartyCompInfo.setBzCompensateText(thirdPartyCompInfo.getBzCompensateText()+bzCompensateText);
							logger.debug(bzCompensateText);
						}
					}
				}
			}

			// 2.7.6无责方赔付有责方_本车
			// 人保规则：rule 新财产计算$40$1_a1无责方赔付有责方$_$本车$41$
			for(ThirdPartyInfo thirdPartyInfo:infos){
				if( !thirdPartyInfo.isBzDutyType()&&thirdPartyInfo.getSerialNo()==1){// 赔付损失车辆无责 // 涉案车辆为标的车
					List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
					for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
						if(compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)&& // 车财损失
						thirdPartyCompInfo.isBzDutyType()){ // 归属车辆交强有责

							thirdPartyCompInfo.setSumRealPay(thirdPartyCompInfo.getSumLoss()/noDutyCarNum); // 损失金额 / 无责方车辆个数

							String bzCompensateText = String.format(CodeConstants.CompensateText.mainCarNoDutyPayText.value,
									thirdPartyCompInfo.getLossName(),thirdPartyCompInfo.getLossFeeName(),
									MoneyFormator.format4Output(thirdPartyCompInfo.getSumLoss()),noDutyCarNum,
									MoneyFormator.format4Output(thirdPartyCompInfo.getSumLoss()/noDutyCarNum));
							thirdPartyCompInfo.setBzCompensateText(thirdPartyCompInfo.getBzCompensateText()+bzCompensateText);
							logger.debug(bzCompensateText);
						}
					}
				}
			}

			// 2.7.7赔付损失项赋值。
			// 人保规则：rule 新财产计算$40$1_b1赔付损失项赋值$41$。
			// 无责方赔付有责方损失，该损失在无责方能取得的限额应该是无责方责任限额除以有责方数量以做平分。
			for(ThirdPartyInfo thirdPartyInfo:infos){
				List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
				if( !thirdPartyInfo.isBzDutyType()&&thirdPartyInfo.getDutyAmount()==100d&&"1".equals(thirdPartyInfo.getNodutyPayFlag())){
					for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
						if(thirdPartyCompInfo.isBzDutyType()&&compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)){
							thirdPartyCompInfo.setPayAmount(thirdPartyInfo.getDutyAmount()/dutyCarNum);// dutyCarNumWithLoss
						}
					}
				}
			}

			// 2.7.8无责代赔限额控制
			for(ThirdPartyInfo thirdPartyInfo:infos){
				List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
				if( !thirdPartyInfo.isBzDutyType()){
					for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
						if(thirdPartyCompInfo.isBzDutyType()&&compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)&&thirdPartyCompInfo
								.getSumRealPay()>thirdPartyCompInfo.getPayAmount()){ // 实赔金额 > 损失车辆 的 责任限额 / 有责方车辆个数
							// 无责方赔付有责方损失，该损失在无责方能取得的限额应该是无责方责任限额除以有责方数量以做平分。
							thirdPartyCompInfo.setSumRealPay(thirdPartyCompInfo.getPayAmount());

							String bzCompensateText = String.format(CodeConstants.CompensateText.noDutyPayOverAmountText.value,
									MoneyFormator.format4Output(thirdPartyInfo.getDutyAmount()),dutyCarNum,// dutyCarNumWithLoss
									MoneyFormator.format4Output(thirdPartyCompInfo.getPayAmount()));

							thirdPartyCompInfo.setBzCompensateText(thirdPartyCompInfo.getBzCompensateText()+bzCompensateText);
							logger.debug(bzCompensateText);
						}
					}
				}
			}

			// 2.7.9车辆的损失金额合计（1/2/3）
			for(ThirdPartyInfo thirdPartyInfo:infos){
				for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
					if(thirdPartyInfo.getSerialNo().equals(thirdPartyLossInfo.getSerialNo())&&compensateExp.getDamageType().equals(
							CodeConstants.FeeTypeCode.PROPLOSS)){

						// 赔付损失车辆 的车辆的总损失金额为 赔付损失车辆 的 车辆的总损失金额 + 赔付损失项 的 损失金额
						thirdPartyInfo.setCarLossSum(thirdPartyInfo.getCarLossSum()+thirdPartyLossInfo.getSumLoss());
					}
				}
			}
			for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
				// 路面理算项。
				if(thirdPartyLossInfo.getSerialNo()==0){
					thirdPartyLossInfo.setCarLossSum(thirdPartyLossInfo.getSumLoss());
				}else{
					for(ThirdPartyInfo thirdPartyInfo:infos){
						if(thirdPartyLossInfo.getSerialNo().equals(thirdPartyInfo.getSerialNo())){
							thirdPartyLossInfo.setCarLossSum(thirdPartyInfo.getCarLossSum());
							break;
						}
					}
				}
			}

			for(ThirdPartyInfo thirdPartyInfo:infos){
				for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
					List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
					for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
						if(compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)&&thirdPartyLossInfo.getObjectIndex().equals(
								thirdPartyCompInfo.getObjectIndex())){

							thirdPartyCompInfo.setCarLossSum(thirdPartyLossInfo.getCarLossSum()); // 赔付损失分项 的归属车辆的总损失金额为 赔付损失项 的 归属车辆的总损失金额
						}
					}
				}
			}

			// 2.7.10车辆的赔付金额合计
			for(ThirdPartyInfo thirdPartyInfo:infos){
				if(compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)&& !thirdPartyInfo.isBzDutyType()){
					List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
					for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
						thirdPartyInfo.setCarPaySum(thirdPartyInfo.getCarPaySum()+thirdPartyCompInfo.getSumRealPay()); // 赔付损失车辆 的总赔付金额为 赔付损失车辆 的 总赔付金额 + 赔付损失分项 的 实赔金额
					}
				}
			}

			// 2.7.11财产损失超限额调整--无责
			for(ThirdPartyInfo thirdPartyInfo:infos){
				if(compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)&& !thirdPartyInfo.isBzDutyType()&&thirdPartyInfo
						.getCarPaySum()>=thirdPartyInfo.getDutyAmount()&&thirdPartyInfo.getDutyAmount()==100d&&"1".equals(thirdPartyInfo
						.getNodutyPayFlag())){// 大于等于就需要分摊

					List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
					for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
						// 赔付损失分项 的实赔金额为 赔付损失分项 的 赔付车辆限额 * ( 赔付损失分项 的 损失金额 / 无责方车辆个数 ) / ( 赔付损失分项 的 归属车辆的总损失金额 / 无责方车辆个数 )
						thirdPartyCompInfo
								.setSumRealPay(thirdPartyCompInfo.getPayAmount()*( thirdPartyCompInfo.getSumLoss()/noDutyCarNum )/( thirdPartyCompInfo
										.getCarLossSum()/noDutyCarNum ));

						String bzCompensateText = String.format(CodeConstants.CompensateText.noDutyPayOverAmountAdjust4PropText.value,
								MoneyFormator.format4Output(thirdPartyCompInfo.getPayAmount()),
								MoneyFormator.format4Output(thirdPartyCompInfo.getSumLoss()/noDutyCarNum),
								MoneyFormator.format4Output(thirdPartyCompInfo.getCarLossSum()/noDutyCarNum),
								MoneyFormator.format4Output(thirdPartyCompInfo.getSumRealPay()));

						thirdPartyCompInfo.setBzCompensateText(thirdPartyCompInfo.getBzCompensateText()+bzCompensateText);
						logger.debug(bzCompensateText);
					}
				}
			}

			// 2.7.12无责方赔付金额初始化
			for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
				thirdPartyLossInfo.setNoDutyCarPay(0d);
			}

			// 2.7.13无责方赔付有责方金额合计
			for(ThirdPartyInfo thirdPartyInfo:infos){
				if( !thirdPartyInfo.isBzDutyType()){ // 无责
					List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
					for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
						if(thirdPartyCompInfo.isBzDutyType()){ // 有责
							for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
								if(compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)&&thirdPartyLossInfo.getObjectIndex()
										.equals(thirdPartyCompInfo.getObjectIndex())){

									// 赔付损失项 的无责方赔付金额为 赔付损失项 的 无责方赔付金额 + 赔付损失分项 的 实赔金额
									thirdPartyLossInfo.setNoDutyCarPay(thirdPartyLossInfo.getNoDutyCarPay()+thirdPartyCompInfo.getSumRealPay());
								}
							}
						}
					}
				}
			}

			// 2.7.14无责方赔付有责方金额调整
			for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
				if(compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)&&thirdPartyLossInfo.getNoDutyCarPay()>thirdPartyLossInfo
						.getSumLoss()){
					thirdPartyLossInfo.setNoDutyCarPay(thirdPartyLossInfo.getSumLoss());
				}
			}

			// 2.7.15有责方赔付无责方
			// 2.7.16有责方赔付路面财产
			// 赔付无责方与赔付路财的理算公式一致
			for(ThirdPartyInfo thirdPartyInfo:infos){
				if(compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)&&thirdPartyInfo.isBzDutyType()){

					logger.debug(thirdPartyInfo.getSerialNo()+" 赔付");
					List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
					for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
						if( !thirdPartyCompInfo.isBzDutyType()){
							thirdPartyCompInfo.setPayAmount(thirdPartyCompInfo.getSumLoss()/dutyCarNum);
							String bzCompensateText = String.format(CodeConstants.CompensateText.dutyPayNoDuty4PropText.value,
									thirdPartyCompInfo.getLossName(),thirdPartyCompInfo.getLossFeeName(),
									MoneyFormator.format4Output(thirdPartyCompInfo.getSumLoss()),dutyCarNum,
									MoneyFormator.format4Output(thirdPartyCompInfo.getPayAmount()));
							thirdPartyCompInfo.setBzCompensateText(thirdPartyCompInfo.getBzCompensateText()+bzCompensateText);
							logger.debug(bzCompensateText);
						}else{
							// 2.7.17有责方赔付有责方
							for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
								if(thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())){

									thirdPartyCompInfo
											.setPayAmount(( thirdPartyCompInfo.getSumLoss()-thirdPartyLossInfo.getNoDutyCarPay() )/( dutyCarNum-1 ));

									String bzCompensateText = String.format(CodeConstants.CompensateText.dutyPayDuty4PropText.value,
											thirdPartyCompInfo.getLossName(),thirdPartyCompInfo.getLossFeeName(),
											MoneyFormator.format4Output(thirdPartyCompInfo.getSumLoss()),
											MoneyFormator.format4Output(thirdPartyLossInfo.getNoDutyCarPay()),dutyCarNum,
											MoneyFormator.format4Output(thirdPartyCompInfo.getPayAmount()));

									thirdPartyCompInfo.setBzCompensateText(bzCompensateText);
									logger.debug(bzCompensateText);
								}
							}
						}
					}
				}
			}

			// 2.7.17有责方赔付有责方
			// for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			// if(compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)&&thirdPartyInfo.isBzDutyType()){
			//
			// List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
			// for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
			// if(thirdPartyCompInfo.isBzDutyType()){
			//
			// for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
			// if(thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())){
			//
			// thirdPartyCompInfo.setPayAmount(( thirdPartyCompInfo.getSumLoss()-thirdPartyLossInfo.getNoDutyCarPay())/( dutyCarNum-1 ));
			//
			// String bzCompensateText = String.format(CodeConstants.CompensateText.dutyPayDuty4PropText.value,
			// thirdPartyCompInfo.getLossName(),thirdPartyCompInfo.getLossFeeName(),
			// MoneyFormator.format4Output(thirdPartyCompInfo.getSumLoss()),
			// MoneyFormator.format4Output(thirdPartyLossInfo.getNoDutyCarPay()),dutyCarNum,
			// MoneyFormator.format4Output(thirdPartyCompInfo.getPayAmount()));
			//
			// thirdPartyCompInfo.setBzCompensateText(bzCompensateText);
			// logger.debug(bzCompensateText);
			// }
			// }
			// }
			// }
			// }
			// }
		}

		// 2.8累计赔付第三方的金额
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			if(thirdPartyInfo.isBzDutyType()){
				List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
				for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
					// 赔付损失车辆 的限额比例赔偿金额为 赔付损失分项 的 限额比例赔付金额 + 赔付损失车辆 的 限额比例赔偿金额
					thirdPartyInfo.setPayAmount(thirdPartyInfo.getPayAmount()+thirdPartyCompInfo.getPayAmount());
				}
			}
		}

		// 2.9超限额调整
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			logger.debug(thirdPartyInfo.getSerialNo()+" 赔付");
			if(thirdPartyInfo.isBzDutyType()){
				List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
				// 赔付损失车辆 的 责任限额 is less than 赔付损失车辆 的 限额比例赔偿金额
				if(thirdPartyInfo.getDutyAmount()<thirdPartyInfo.getPayAmount()){
					for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
						thirdPartyCompInfo.setAdjPayAmount(thirdPartyInfo.getDutyAmount()*thirdPartyCompInfo.getPayAmount()/thirdPartyInfo
								.getPayAmount());

						String bzCompensateText = String.format(CodeConstants.CompensateText.overPayAdjust4PropText.value,
								MoneyFormator.format4Output(thirdPartyInfo.getDutyAmount()),
								MoneyFormator.format4Output(thirdPartyCompInfo.getPayAmount()),
								MoneyFormator.format4Output(thirdPartyInfo.getPayAmount()),
								MoneyFormator.format4Output(thirdPartyCompInfo.getAdjPayAmount()));

						thirdPartyCompInfo.setBzCompensateText(thirdPartyCompInfo.getBzCompensateText()+bzCompensateText);
						logger.debug(bzCompensateText);
					}
				}else{
					for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
						// 赔付损失分项 的超限额赔付调整金额为 赔付损失分项 的 限额比例赔付金额
						thirdPartyCompInfo.setAdjPayAmount(thirdPartyCompInfo.getPayAmount());
					}
				}
			}
		}

		// 2.10累计赔付金额
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			if(thirdPartyInfo.isBzDutyType()){
				List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项

				for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
					// 赔付损失车辆 的累计赔付金额为 赔付损失分项 的 超限额赔付调整金额 + 赔付损失车辆 的 累计赔付金额
					thirdPartyInfo.setAdjPayAmount(thirdPartyInfo.getAdjPayAmount()+thirdPartyCompInfo.getAdjPayAmount());
				}
			}
		}

		// 2.11累计未赔付金额
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			if(thirdPartyInfo.isBzDutyType()){
				// 损失赔付车辆 的 责任限额 is more than 损失赔付车辆 的 累计赔付金额
				if(thirdPartyInfo.getDutyAmount()>thirdPartyInfo.getAdjPayAmount()){
					// 损失赔付车辆 的剩余限额为 损失赔付车辆 的 责任限额 - 损失赔付车辆 的 累计赔付金额
					thirdPartyInfo.setLeftAmount(thirdPartyInfo.getDutyAmount()-thirdPartyInfo.getAdjPayAmount());
				}
			}
		}

		// 2.12车辆项下分项损失转换为损失项下分项损失
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			if(thirdPartyInfo.isBzDutyType()){
				for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){

					List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
					for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
						if(thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())){

							thirdPartyLossInfo.addThirdPartyPayAmount(thirdPartyLossInfo.getLicenseNo(),thirdPartyCompInfo.getObjectIndex(),
									thirdPartyCompInfo.getAdjPayAmount(),thirdPartyCompInfo.getBzCompensateText(),
									thirdPartyCompInfo.getInsteadFlag(),thirdPartyLossInfo.getSerialNo());
						}
					}
				}
			}
		}

		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			if( !thirdPartyInfo.isBzDutyType()){
				for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
					List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
					for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
						if(thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())){
							// 赔付损失项 .增加赔付分项损失(赔付损失项 的 车牌号码 , 车辆赔付损失分项 的 损失项序号 , 车辆赔付损失分项 的 实赔金额 , 车辆赔付损失分项 的 交强赔付公式 )
							thirdPartyLossInfo.addThirdPartyPayAmount(thirdPartyLossInfo.getLicenseNo(),thirdPartyCompInfo.getObjectIndex(),
									thirdPartyCompInfo.getSumRealPay(),thirdPartyCompInfo.getBzCompensateText(),thirdPartyCompInfo.getInsteadFlag(),
									thirdPartyLossInfo.getSerialNo());
						}
					}
				}
			}
		}

		// 2.13合计损失项已赔付金额
		for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
			List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyLossInfo.getThirdPartyCompInfos(); // 赔付损失项 的 赔付损失分项
			for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
				// 赔付损失项 涉案车辆限额赔付金额为 赔付损失项 的 涉案车辆限额赔付金 + 赔付损失分项 的 超限额赔付调整金额
				thirdPartyLossInfo.setThirdPartyLimitPay(thirdPartyLossInfo.getThirdPartyLimitPay()+thirdPartyCompInfo.getAdjPayAmount());
			}
		}

		// 2.14分项损失未赔足金额
		for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
			List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyLossInfo.getThirdPartyCompInfos(); // 赔付损失项 的 赔付损失分项
			for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
				// 赔付损失分项 的未赔足金额为 赔付损失项 的 损失金额 - 赔付损失项 的 涉案车辆限额赔付金
				thirdPartyCompInfo.setLeftPay(thirdPartyLossInfo.getSumLoss()-thirdPartyLossInfo.getThirdPartyLimitPay());
			}
		}

		// 2.15损失项下分项损失转换为车辆项下分项损失 ----车辆信息的分项赔付获取未赔足金额
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			if(thirdPartyInfo.isBzDutyType()){
				List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
				for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
					List<ThirdPartyCompInfo> lossThirdPartyCompInfos = thirdPartyLossInfo.getThirdPartyCompInfos(); // 损失项下赔付损失分项
					for(ThirdPartyCompInfo carThirdPartyCompInfo:carThirdPartyCompInfos){ // 车辆下赔付损失分项
						for(ThirdPartyCompInfo lossThirdPartyCompInfo:lossThirdPartyCompInfos){ // 损失项下赔付损失分项
							if(carThirdPartyCompInfo.getObjectIndex().equals(lossThirdPartyCompInfo.getObjectIndex())){
								// 车辆赔付损失分项 的未赔足金额为 损失赔付损失分项 的 未赔足金额
								carThirdPartyCompInfo.setLeftPay(lossThirdPartyCompInfo.getLeftPay());
							}
						}
					}
				}
			}
		}

		// 2.16合计未赔足金额
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			if(thirdPartyInfo.isBzDutyType()){
				List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
				for(ThirdPartyCompInfo carThirdPartyCompInfo:carThirdPartyCompInfos){ // 车辆下赔付损失分项
					// 赔付损失车辆 的赔付损失项未赔足金额为 赔付损失车辆 的 赔付损失项未赔足金额 + 赔付损失分项 的 未赔足金额
					thirdPartyInfo.setSumNoenoughPay(thirdPartyInfo.getSumNoenoughPay()+carThirdPartyCompInfo.getLeftPay());
				}
			}
		}

		// 2.17剩余限额调整金额计算
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			if(thirdPartyInfo.isBzDutyType()){
				List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
				for(ThirdPartyCompInfo carThirdPartyCompInfo:carThirdPartyCompInfos){ // 车辆下赔付损失分项
					if(( thirdPartyInfo.getDutyAmount()>( thirdPartyInfo.getAdjPayAmount()+0.005 ) )&&carThirdPartyCompInfo.getLeftPay()>0.005){
						// 赔付损失分项 的剩余限额赔付金额为 赔付损失车辆 的 剩余限额 * 赔付损失分项 的 未赔足金额 / 赔付损失车辆 的 赔付损失项未赔足金额
						carThirdPartyCompInfo.setLeftPayAmount(thirdPartyInfo.getLeftAmount()*carThirdPartyCompInfo.getLeftPay()/thirdPartyInfo
								.getSumNoenoughPay());

						String bzCompensateText = String.format(CodeConstants.CompensateText.leftPayAmountText.value,
								MoneyFormator.format4Output(thirdPartyInfo.getLeftAmount()),
								MoneyFormator.format4Output(carThirdPartyCompInfo.getLeftPay()),
								MoneyFormator.format4Output(thirdPartyInfo.getSumNoenoughPay()),
								MoneyFormator.format4Output(carThirdPartyCompInfo.getLeftPayAmount()));

						carThirdPartyCompInfo.setBzCompensateText(carThirdPartyCompInfo.getBzCompensateText()+bzCompensateText);
						logger.debug(bzCompensateText);
					}
				}
			}
		}

		// 2.19超损失调整
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			if(thirdPartyInfo.isBzDutyType()){
				List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
				for(ThirdPartyCompInfo carThirdPartyCompInfo:carThirdPartyCompInfos){ // 车辆下赔付损失分项
					if(( carThirdPartyCompInfo.getLeftPayAmount()>carThirdPartyCompInfo.getLeftPay() )&&( carThirdPartyCompInfo.getLeftPay()>0.000001 )){

						carThirdPartyCompInfo
								.setLeftPayAmount(MoneyFormator.format(new BigDecimal(carThirdPartyCompInfo.getLeftPay())).doubleValue());

						String bzCompensateText = String.format(CodeConstants.CompensateText.overLossAdjust4PropText.value,
								MoneyFormator.format4Output(carThirdPartyCompInfo.getLeftPay()));
						carThirdPartyCompInfo.setBzCompensateText(carThirdPartyCompInfo.getBzCompensateText()+bzCompensateText);
						logger.debug(bzCompensateText);
					}
				}
			}
		}

		Map<String,Double> leftAmountMap = compensateListVo.getLeftAmountMap();
		// 2.20实际赔付金额计算
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			double sumPay = 0d;
			if(thirdPartyInfo.isBzDutyType()){
				List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
				for(ThirdPartyCompInfo carThirdPartyCompInfo:carThirdPartyCompInfos){ // 车辆下赔付损失分项
					if(carThirdPartyCompInfo.getAdjPayAmount()>=0){// 等于0 是考虑重开赔案，剩余额度为0 by yk 20160704
						// 赔付损失分项 的实赔金额为 赔付损失分项 的 超限额赔付调整金额 + 赔付损失分项 的 剩余限额赔付金额
						carThirdPartyCompInfo.setSumRealPay(carThirdPartyCompInfo.getAdjPayAmount()+carThirdPartyCompInfo.getLeftPayAmount());
						if(CodeConstants.PayFlagType.CLEAR_PAY.equals(compensateListVo.getPayType())){
							carThirdPartyCompInfo.setNoRecoveryPay(carThirdPartyCompInfo.getSumRealPay());
						}
					}else{// 该步骤基本不走，当小于保额时，会用PayAmount赋值给AdjPayAmount by yk 20160704
							// 赔付损失分项 的实赔金额为 赔付损失分项 的 限额比例赔付金额 + 赔付损失分项 的 剩余限额赔付金额
						carThirdPartyCompInfo.setSumRealPay(carThirdPartyCompInfo.getPayAmount()+carThirdPartyCompInfo.getLeftPayAmount());
						if(CodeConstants.PayFlagType.CLEAR_PAY.equals(compensateListVo.getPayType())){
							carThirdPartyCompInfo.setNoRecoveryPay(carThirdPartyCompInfo.getSumRealPay());
						}
					}

					sumPay = sumPay+carThirdPartyCompInfo.getSumRealPay();
				}
			}else{
				sumPay = 100d; // 无责直接扣除100
			}

			double leftAmount = thirdPartyInfo.getDutyAmount()-sumPay;
			if(leftAmount<0d){
				leftAmount = 0d;
			}
			leftAmountMap.put("car-"+thirdPartyInfo.getSerialNo(),leftAmount);

		}

		// 2.21组织返回信息<交强>
		// 商业计算书。
		// if(this.isCommercialOrCompulsory(compensateType)){
		if("1".equals(compensateListVo.getIsBiPCi())){
			for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
				List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
				for(ThirdPartyCompInfo carThirdPartyCompInfo:carThirdPartyCompInfos){ // 车辆下赔付损失分项
					compensateListVo.setreturnBZCompensateList(carThirdPartyCompInfo,thirdPartyInfo,"交强试算_20080201_财产_交强_14组织返回信息(本车损失)");
					logger.debug("车辆 "+thirdPartyInfo.getSerialNo()+"赔付：车辆序号 "+carThirdPartyCompInfo.getSerialNo()+" "+carThirdPartyCompInfo
							.getObjectIndex()+" "+carThirdPartyCompInfo.getInsteadFlag()+" "+carThirdPartyCompInfo.getSumRealPay());
				}
			}
		}

		// 交强计算书。
		// if( !this.isCommercialOrCompulsory(compensateType)){
		if("0".equals(compensateListVo.getIsBiPCi())){
			for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
				if(thirdPartyInfo.getSerialNo()==1){
					List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
					for(ThirdPartyCompInfo carThirdPartyCompInfo:carThirdPartyCompInfos){ // 车辆下赔付损失分项
						compensateListVo.setreturnBZCompensateList(carThirdPartyCompInfo,thirdPartyInfo,"交强试算_20080201_财产_交强_14组织返回信息");
						logger.debug(carThirdPartyCompInfo.getObjectIndex()+" "+carThirdPartyCompInfo.getSerialNo()+" "+carThirdPartyCompInfo
								.getInsteadFlag()+" "+carThirdPartyCompInfo.getSumRealPay());
					}
				}
			}

			// 2.24组织返回信息
			for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
				if(compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)&&thirdPartyInfo.getSerialNo()!=1){
					List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
					for(ThirdPartyCompInfo carThirdPartyCompInfo:carThirdPartyCompInfos){ // 车辆下赔付损失分项
						if(carThirdPartyCompInfo.getSerialNo()==1&&( CodeConstants.InsteadFlag.NODUTY_INSTEAD.equals(carThirdPartyCompInfo
								.getInsteadFlag()) )){
							carThirdPartyCompInfo.setObjectSerialNo(thirdPartyInfo.getSerialNo());
							carThirdPartyCompInfo.setObjectLicenseNo(thirdPartyInfo.getLicenseNo());
							compensateListVo.setreturnBZCompensateList(carThirdPartyCompInfo,thirdPartyInfo,"交强试算_20080201_财产_交强_16组织返回信息");
							logger.debug(carThirdPartyCompInfo.getObjectIndex()+" "+carThirdPartyCompInfo.getSerialNo()+" "+carThirdPartyCompInfo
									.getInsteadFlag()+" "+carThirdPartyCompInfo.getSumRealPay());
						}
					}
				}
			}
		}

		// 2.25按损失项顺序返回实赔 ThirdPartyLossInfos
		List<ThirdPartyLossInfo> nodutyLossInfos = new ArrayList<ThirdPartyLossInfo>();
		for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
			logger.debug(thirdPartyLossInfo.getObjectIndex()+" "+thirdPartyLossInfo.getSerialNo());
			List<ThirdPartyCompInfo> thirdPartyCompInfos = compensateListVo.getReturnBZCompensateList();
			for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){ // 损失项清单 的 返回损失分项
				if(thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())
				// && (CodeConstants.InsteadFlag.NO_INSTEAD.equals(thirdPartyCompInfo.getInsteadFlag())
				// || CodeConstants.InsteadFlag.NODUTY_INSTEAD.equals(thirdPartyCompInfo.getInsteadFlag()))
				){

					thirdPartyLossInfo.setSumRealPay(thirdPartyLossInfo.getSumRealPay()+thirdPartyCompInfo.getSumRealPay());
					if( !CodeConstants.InsteadFlag.NODUTY_INSTEAD.equals(thirdPartyCompInfo.getInsteadFlag())){
						thirdPartyLossInfo.setBzCompensateText(thirdPartyLossInfo.getBzCompensateText()+"\r\n"+thirdPartyCompInfo
								.getBzCompensateText());
					}
					// 商业险 如果有无责代赔，就不覆盖了
					if( !"1".equals(thirdPartyLossInfo.getInsteadFlag())){
						thirdPartyLossInfo.setInsteadFlag(thirdPartyCompInfo.getInsteadFlag());
					}
					thirdPartyLossInfo.setSumDefLoss(thirdPartyCompInfo.getPayAmount()); // 赔付损失项 的理算金额为 返回损失分项 的 限额比例赔付金额
					thirdPartyLossInfo.setAllAmount(allDamageAmount);
					thirdPartyLossInfo.setPayCarAmout(thirdPartyCompInfo.getPayCarAmount());
					compensateListVo.setDutyCarNum(dutyCarNum);
					thirdPartyLossInfo.setDamageType(compensateExp.getDamageType());
				}

			}
		}

		// 2.26设置赔付损失项的超限额标志位
		for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
			List<ThirdPartyCompInfo> thirdPartyCompInfos = compensateListVo.getReturnBZCompensateList();
			for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){ // 损失项清单 的 返回损失分项
				if(thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())&&thirdPartyCompInfo.getPayAmount()>thirdPartyCompInfo
						.getAdjPayAmount()){
					// 超限额
					thirdPartyLossInfo.setOverFlag("1");
				}
			}
		}

		// 2.27设置赔付损失项的剩余限额赔付标志位
		for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
			List<ThirdPartyCompInfo> thirdPartyCompInfos = compensateListVo.getReturnBZCompensateList();
			for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){ // 损失项清单 的 返回损失分项
				if(thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())&&thirdPartyCompInfo.getLeftPayAmount()>0){
					// 无责代赔
					thirdPartyLossInfo.setOverFlag("2");
				}
			}
		}

		// 2.29设置交强险无保代赔的所有限额
		for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
			List<ThirdPartyCompInfo> thirdPartyCompInfos = compensateListVo.getReturnBZCompensateList();
			for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){ // 损失项清单 的 返回损失分项
				if(thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())){
					thirdPartyLossInfo.setAllAmount(allDamageAmount);
				}
			}
		}

		// 2.30屏蔽本车损失以及空的理算公式
		if(CodeConstants.PayFlagType.CLEAR_PAY.equals(compensateListVo.getPayType())){ // 清付

			for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
				List<ThirdPartyCompInfo> thirdPartyCompInfos = compensateListVo.getReturnBZCompensateList();
				for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){ // 损失项清单 的 返回损失分项
					if(thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())){
						if(( CodeConstants.InsteadFlag.NO_INSTEAD.equals(thirdPartyCompInfo.getInsteadFlag()) )&&thirdPartyLossInfo
								.getBzCompensateText()!=null){
							String bzCompensateText = String.format(CodeConstants.CompensateText.sumRealPayQf4PropText.value,
									MoneyFormator.format4Output(thirdPartyLossInfo.getSumRealPay()));
							thirdPartyLossInfo.setBzCompensateText(thirdPartyLossInfo.getBzCompensateText()+bzCompensateText);
						}
					}
				}
			}
		}else{
			for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
				if(thirdPartyLossInfo.getBzCompensateText()!=null&&( CodeConstants.InsteadFlag.NO_INSTEAD.equals(thirdPartyLossInfo.getInsteadFlag()) )&&bzFlag){
					if(StringUtils.isNotBlank(thirdPartyLossInfo.getBzCompensateText())){
						String bzCompensateText = String.format(CodeConstants.CompensateText.sumRealPayZfText.value,
								MoneyFormator.format4Output(thirdPartyLossInfo.getSumRealPay()));
						thirdPartyLossInfo.setBzCompensateText(thirdPartyLossInfo.getBzCompensateText()+bzCompensateText);
						logger.debug(bzCompensateText);
					}
				}
			}
		}

		// 17屏蔽本车损失(屏蔽空的理算公式)
		for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
			if(thirdPartyLossInfo.getBzCompensateText()==null){
				thirdPartyLossInfo.setBzCompensateText("");
			}
		}

		// 扣减交强支付金额。
		// this.compulsorySubPrePay(compensateExp.getThirdPartyLossInfos());

		// this.calculateCi4CommonReCaseAdjust(thirdPartyLossInfos,compensateExp);

		if( !"claim".equals(compensateListVo.getCalculateType())){
			compensateExp.setNoDutyLossInfos(nodutyLossInfos);
			// thirdPartyLossInfos.addAll(nodutyLossInfos);
		}

		// 2.31清空赔付损失车辆信息
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			thirdPartyInfo.setLeftAmount(0); // 剩余限额
			thirdPartyInfo.setLeftPayAmount(0); // 剩余限额赔偿金额
			thirdPartyInfo.setAdjPayAmount(0); // 累计赔付金额
			thirdPartyInfo.setDutyCompPay(0); // 责任限额计算金额
			thirdPartyInfo.setSumNoenoughPay(0); // 赔付损失项未赔足金额
			thirdPartyInfo.setPayAmount(0); // 限额比例赔偿金额
			thirdPartyInfo.deleteThirdPartyPayAmount(); // 赔付损失车辆.清空赔付损失分项
			thirdPartyInfo.setSumRealPay(0); // 车辆合计赔付金额
			thirdPartyInfo.setCarPaySum(0); // 总赔付金额
			thirdPartyInfo.setCarLossNum(0); // 车辆损失个数
			thirdPartyInfo.setCarLossSum(0); // 车辆的总损失金额
		}

		// 2.32清空赔付损失项
		compensateListVo.deleteReturnBZCompensateList();

		return compensateExp;
	}

	/**
	 * 计算。用于代替人保的规则引擎方法/Compensate_getSumRealPayApp/Compensate_getSumRealPayRule的交强险非互陪自赔部分人伤理算。
	 * @param compensateList
	 * @param compensateExp
	 * @return
	 */
	private CompensateExp calculateCi4CommonPerson(CompensateListVo compensateListVo,CompensateExp compensateExp) {

		double allDamageAmount = 0.00d; // 所有限额
		String compensateType = compensateListVo.getCompensateType()+"";
		List<ThirdPartyInfo> infos = compensateExp.getThirdPartyInfos();
		List<ThirdPartyLossInfo> thirdPartyLossInfos = compensateExp.getThirdPartyLossInfos();

		// 1累计赔付限额计算
		// 人保规则：rule $49$累计赔付限额计算。
		for(ThirdPartyInfo thirdPartyInfo:infos){
			allDamageAmount += thirdPartyInfo.getDutyAmount();
		}

		// 2获取损失项归属车辆限额
		// 人保规则：rule $50$获取损失项归属车辆限额
		for(ThirdPartyInfo thirdPartyInfo:infos){
			for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
				if(thirdPartyInfo.getSerialNo().equals(thirdPartyLossInfo.getSerialNo())){ // 车牌号相同
					thirdPartyLossInfo.setDamageAmount(thirdPartyInfo.getDutyAmount()); // 归属车辆赔限额
					thirdPartyLossInfo.setLossItemType(thirdPartyInfo.getLossItemType()); // 归属车辆类型
					if(thirdPartyInfo.isBzDutyType()){
						thirdPartyLossInfo.setBzDutyType("1"); // 归属车辆交强责任类型
					}else{
						thirdPartyLossInfo.setBzDutyType("0"); // 归属车辆交强责任类型
					}
				}
			}
		}

		// 3计算赔付第三方金额
		// 人保规则：rule $51$计算赔付第三方金额
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
				if( !thirdPartyLossInfo.getSerialNo().equals(thirdPartyInfo.getSerialNo())){
					double payAmount = 0d;
					if(allDamageAmount>0&&allDamageAmount-thirdPartyLossInfo.getDamageAmount()>0d){
						payAmount = thirdPartyLossInfo.getSumLoss()*thirdPartyInfo.getDutyAmount()/( allDamageAmount-thirdPartyLossInfo
								.getDamageAmount() );
					}
					thirdPartyInfo.addThirdPartyPayAmount(payAmount,"",thirdPartyLossInfo);
				}
			}
		}

		for(ThirdPartyInfo thirdPartyInfo:infos){
			List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
			for(ThirdPartyCompInfo carThirdPartyCompInfo:carThirdPartyCompInfos){ // 车辆下赔付损失分项
				carThirdPartyCompInfo.setInsteadFlag(CodeConstants.InsteadFlag.NO_INSTEAD);
			}
		}
		// 交强判断
		if( !this.isCommercialOrCompulsory(compensateType)){
			// 2.23设置代赔标志位
			for(ThirdPartyInfo thirdPartyInfo:infos){ // 车

				String insteadFlag = CodeConstants.InsteadFlag.THIRD2THIRD_INSTEAD;
				// if(!"1".equals(thirdPartyInfo.getLossItemType())){
				// //无保且有责才是无保代赔。
				// if(thirdPartyInfo.getInsuredFlag().equals(CodeConstants.InsuredFlag.no.value) &&
				// thirdPartyInfo.isBzDutyType()){
				// insteadFlag = CodeConstants.InsteadFlag.noPolicyInstead.value;
				// }
				// }

				List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
				for(ThirdPartyCompInfo carThirdPartyCompInfo:carThirdPartyCompInfos){ // 车辆下赔付损失分项
					if(carThirdPartyCompInfo.getSerialNo()==1){
						carThirdPartyCompInfo.setInsteadFlag(insteadFlag);
					}else{
						if(thirdPartyInfo.getSerialNo()==1){
							carThirdPartyCompInfo.setInsteadFlag(CodeConstants.InsteadFlag.NO_INSTEAD); // 三者代赔三者
						}else{
							carThirdPartyCompInfo.setInsteadFlag(CodeConstants.InsteadFlag.THIRD2THIRD_INSTEAD); // 三者代赔三者
						}
					}
				}
			}
		}

		// 4累计赔付第三方金额
		// 人保规则：rule $52$累计赔付第三方金额
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			logger.debug(thirdPartyInfo.getSerialNo()+" 赔付");
			List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
			for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
				// 赔付损失车辆 的限额比例赔偿金额为 赔付损失分项 的 限额比例赔付金额 + 赔付损失车辆 的 限额比例赔偿金额
				thirdPartyInfo.setPayAmount(thirdPartyInfo.getPayAmount()+thirdPartyCompInfo.getPayAmount());
				String bzCompensateText = String.format(CodeConstants.CompensateText.payAmountText.value,thirdPartyCompInfo.getLossName(),
						thirdPartyCompInfo.getLossFeeName(),MoneyFormator.format4Output(thirdPartyCompInfo.getSumLoss()),
						MoneyFormator.format4Output(thirdPartyInfo.getDutyAmount()),
						MoneyFormator.format4Output(allDamageAmount-thirdPartyCompInfo.getThirdPartyLossInfo().getDamageAmount()),
						MoneyFormator.format4Output(thirdPartyCompInfo.getPayAmount()));

				// 赔付损失分项 的交强赔付公式为 赔付损失分项 的 交强赔付公式 + "                   ＝" + 理算大对象 . 数据格式化 ( 赔付损失分项 的 限额比例赔付金额 )
				if(thirdPartyCompInfo.getSumLoss()>0){
					if(allDamageAmount>0){
						thirdPartyCompInfo.setBzCompensateText(thirdPartyCompInfo.getBzCompensateText()+bzCompensateText);
					}else{
						bzCompensateText = String.format(CodeConstants.CompensateText.payAmountText_no.value,thirdPartyCompInfo.getLossName(),
								thirdPartyCompInfo.getLossFeeName());
						thirdPartyCompInfo.setBzCompensateText(thirdPartyCompInfo.getBzCompensateText()+bzCompensateText);
					}
				}
				logger.debug(bzCompensateText);
			}
		}

		// 5超限额调整
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车

			List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
			// 赔付损失车辆 的 责任限额 is less than 赔付损失车辆 的 限额比例赔偿金额
			if(thirdPartyInfo.getDutyAmount()<thirdPartyInfo.getPayAmount()){
				logger.debug(thirdPartyInfo.getSerialNo()+" 超限额赔付调整");
				for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
					// 赔付损失分项 的超限额赔付调整金额为 赔付损失车辆 的 责任限额 * 赔付损失分项 的 限额比例赔付金额 / 赔付损失车辆 的 限额比例赔偿金额
					thirdPartyCompInfo
							.setAdjPayAmount(thirdPartyInfo.getDutyAmount()*thirdPartyCompInfo.getPayAmount()/thirdPartyInfo.getPayAmount());

					String bzCompensateText = String.format(CodeConstants.CompensateText.overPayAdjust4PropText.value,
							MoneyFormator.format4Output(thirdPartyInfo.getDutyAmount()),
							MoneyFormator.format4Output(thirdPartyCompInfo.getPayAmount()),
							MoneyFormator.format4Output(thirdPartyInfo.getPayAmount()),
							MoneyFormator.format4Output(thirdPartyCompInfo.getAdjPayAmount()));

					thirdPartyCompInfo.setBzCompensateText(thirdPartyCompInfo.getBzCompensateText()+bzCompensateText);
					logger.debug(bzCompensateText);
				}
			}else{
				for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
					// 赔付损失分项 的超限额赔付调整金额为 赔付损失分项 的 限额比例赔付金额
					thirdPartyCompInfo.setAdjPayAmount(thirdPartyCompInfo.getPayAmount());
				}
			}
		}

		// 6累计赔付金额
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
			// 赔付损失车辆 的 责任限额 is less than 赔付损失车辆 的 责任限额计算金额 不知所云
			if(thirdPartyInfo.getDutyAmount()<thirdPartyInfo.getDutyCompPay()){
				for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
					// 赔付损失车辆 的累计赔付金额为 赔付损失分项 的 超限额赔付调整金额 + 赔付损失车辆 的 累计赔付金额
					thirdPartyInfo.setAdjPayAmount(thirdPartyInfo.getAdjPayAmount()+thirdPartyCompInfo.getAdjPayAmount());
					StringBuffer bzCompensateText = new StringBuffer();
					// "\r\n" + "                   ＝" + 理算大对象 .数据格式化( 赔付损失分项 的 超限额赔付调整金额 )
					bzCompensateText.append(thirdPartyCompInfo.getBzCompensateText()+"\r\n"+this.space(CodeConstants.SpaceCount.longer)+MoneyFormator
							.format4Output(thirdPartyCompInfo.getAdjPayAmount()));
					thirdPartyCompInfo.setBzCompensateText(thirdPartyCompInfo.getBzCompensateText()+bzCompensateText.toString());
					logger.info(bzCompensateText.toString());
				}
			}else{
				for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
					// 赔付损失车辆 的累计赔付金额为 赔付损失分项 的 超限额赔付调整金额 + 赔付损失车辆 的 累计赔付金额
					thirdPartyInfo.setAdjPayAmount(thirdPartyInfo.getAdjPayAmount()+thirdPartyCompInfo.getAdjPayAmount());
				}
			}
		}

		// 7累计未赔付金额
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			// 损失赔付车辆 的 责任限额 is more than 损失赔付车辆 的 累计赔付金额
			if(thirdPartyInfo.getDutyAmount()>thirdPartyInfo.getAdjPayAmount()){
				// 损失赔付车辆 的剩余限额为 损失赔付车辆 的 责任限额 - 损失赔付车辆 的 累计赔付金额
				thirdPartyInfo.setLeftAmount(thirdPartyInfo.getDutyAmount()-thirdPartyInfo.getAdjPayAmount());
			}
		}

		// 8车辆项下分项损失转换为损失项下分项损失
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
			for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
				for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
					if(thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())){
						// 赔付损失项 .增加赔付分项损失(赔付损失项 的 车牌号码 , 车辆赔付损失分项 的 损失项序号 , 车辆赔付损失分项 的 超限额赔付调整金额 , 车辆赔付损失分项 的 交强赔付公式 )
						thirdPartyLossInfo.addThirdPartyPayAmount(thirdPartyLossInfo.getLicenseNo(),thirdPartyCompInfo.getObjectIndex(),
								thirdPartyCompInfo.getAdjPayAmount(),thirdPartyCompInfo.getBzCompensateText(),thirdPartyCompInfo.getInsteadFlag(),
								thirdPartyLossInfo.getSerialNo());
					}
				}
			}
		}

		// 9合计损失项已赔付金额
		for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
			List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyLossInfo.getThirdPartyCompInfos(); // 赔付损失项 的 赔付损失分项
			for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
				// 赔付损失项 涉案车辆限额赔付金额为 赔付损失项 的 涉案车辆限额赔付金 + 赔付损失分项 的 超限额赔付调整金额
				thirdPartyLossInfo.setThirdPartyLimitPay(thirdPartyLossInfo.getThirdPartyLimitPay()+thirdPartyCompInfo.getAdjPayAmount());
			}
		}

		// 10分项损失未赔足金额
		for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
			List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyLossInfo.getThirdPartyCompInfos(); // 赔付损失项 的 赔付损失分项
			for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){
				// 赔付损失分项 的未赔足金额为 赔付损失项 的 损失金额 - 赔付损失项 的 涉案车辆限额赔付金
				thirdPartyCompInfo.setLeftPay(thirdPartyLossInfo.getSumLoss()-thirdPartyLossInfo.getThirdPartyLimitPay());
			}
		}

		// 11损失项下分项损失转换为车辆项下分项损失
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
			for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
				List<ThirdPartyCompInfo> lossThirdPartyCompInfos = thirdPartyLossInfo.getThirdPartyCompInfos(); // 损失项下赔付损失分项
				for(ThirdPartyCompInfo carThirdPartyCompInfo:carThirdPartyCompInfos){ // 车辆下赔付损失分项
					for(ThirdPartyCompInfo lossThirdPartyCompInfo:lossThirdPartyCompInfos){ // 损失项下赔付损失分项
						if(carThirdPartyCompInfo.getObjectIndex().equals(lossThirdPartyCompInfo.getObjectIndex())){
							// 车辆赔付损失分项 的未赔足金额为 损失赔付损失分项 的 未赔足金额
							carThirdPartyCompInfo.setLeftPay(lossThirdPartyCompInfo.getLeftPay());
						}
					}
				}
			}
		}

		// 12合计未赔足金额
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
			for(ThirdPartyCompInfo carThirdPartyCompInfo:carThirdPartyCompInfos){ // 车辆下赔付损失分项
				// 赔付损失车辆 的赔付损失项未赔足金额为 赔付损失车辆 的 赔付损失项未赔足金额 + 赔付损失分项 的 未赔足金额
				thirdPartyInfo.setSumNoenoughPay(thirdPartyInfo.getSumNoenoughPay()+carThirdPartyCompInfo.getLeftPay());
			}
		}

		// 13剩余限额调整金额计算
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			logger.debug(thirdPartyInfo.getSerialNo()+" 剩余限额调整金额");
			List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
			for(ThirdPartyCompInfo carThirdPartyCompInfo:carThirdPartyCompInfos){ // 车辆下赔付损失分项
				if(( thirdPartyInfo.getDutyAmount()>( thirdPartyInfo.getAdjPayAmount()+0.005 ) )&&carThirdPartyCompInfo.getLeftPay()>0.005){

					// 赔付损失分项 的剩余限额赔付金额为 赔付损失车辆 的 剩余限额 * 赔付损失分项 的 未赔足金额 / 赔付损失车辆 的 赔付损失项未赔足金额
					carThirdPartyCompInfo.setLeftPayAmount(thirdPartyInfo.getLeftAmount()*carThirdPartyCompInfo.getLeftPay()/thirdPartyInfo
							.getSumNoenoughPay());
					String bzCompensateText = String.format(CodeConstants.CompensateText.leftPayAmountText.value,
							MoneyFormator.format4Output(thirdPartyInfo.getLeftAmount()),
							MoneyFormator.format4Output(carThirdPartyCompInfo.getLeftPay()),
							MoneyFormator.format4Output(thirdPartyInfo.getSumNoenoughPay()),
							MoneyFormator.format4Output(carThirdPartyCompInfo.getLeftPayAmount()));
					carThirdPartyCompInfo.setBzCompensateText(carThirdPartyCompInfo.getBzCompensateText()+bzCompensateText.toString());
					logger.debug(bzCompensateText);
				}
			}
		}

		// 13剩余限额金额展示
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
			for(ThirdPartyCompInfo carThirdPartyCompInfo:carThirdPartyCompInfos){ // 车辆下赔付损失分项
				if(carThirdPartyCompInfo.getLeftPayAmount()>0.005){
					String bzCompensateText = carThirdPartyCompInfo.getBzCompensateText()+"\r\n"+"="+MoneyFormator
							.format4Output(carThirdPartyCompInfo.getLeftPayAmount());
					carThirdPartyCompInfo.setBzCompensateText(carThirdPartyCompInfo.getBzCompensateText()+bzCompensateText.toString());
					// logger.debug(bzCompensateText);
				}
			}
		}

		// 13超损失调整
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
			for(ThirdPartyCompInfo carThirdPartyCompInfo:carThirdPartyCompInfos){ // 车辆下赔付损失分项
				if(carThirdPartyCompInfo.getLeftPayAmount()>( carThirdPartyCompInfo.getLeftPay()+0.005 )){
					carThirdPartyCompInfo.setLeftPayAmount(carThirdPartyCompInfo.getLeftPay());
					String bzCompensateText = String.format(CodeConstants.CompensateText.overLossAdjust4PropText.value,
							MoneyFormator.format4Output(carThirdPartyCompInfo.getLeftPay()));
					carThirdPartyCompInfo.setBzCompensateText(carThirdPartyCompInfo.getBzCompensateText()+bzCompensateText);
					logger.debug(bzCompensateText);
				}
			}
		}

		// 14组织返回信息
		// 商业
		// if(this.isCommercialOrCompulsory(compensateType)){
		if("1".equals(compensateListVo.getIsBiPCi())){
			for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
				List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
				for(ThirdPartyCompInfo carThirdPartyCompInfo:carThirdPartyCompInfos){ // 车辆下赔付损失分项
					compensateListVo.setreturnBZCompensateList(carThirdPartyCompInfo,thirdPartyInfo,"交强试算_20080201_其它_14组织返回信息(本车损失)");
				}
			}
		}
		// 交强
		// if( !this.isCommercialOrCompulsory(compensateType)){
		if("0".equals(compensateListVo.getIsBiPCi())){
			for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
				if( !compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)&&thirdPartyInfo.getSerialNo()==1){
					List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
					for(ThirdPartyCompInfo carThirdPartyCompInfo:carThirdPartyCompInfos){ // 车辆下赔付损失分项
						compensateListVo.setreturnBZCompensateList(carThirdPartyCompInfo,thirdPartyInfo,"交强试算_20080201_其它_14组织返回信息");
					}
				}
			}

			// for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
			// List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
			// for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
			// if(CodeConstants.InsteadFlag.noPolicyInstead.value.equals(carThirdPartyCompInfo.getInsteadFlag())){
			// compensateList.setreturnBZCompensateList(carThirdPartyCompInfo, thirdPartyInfo,
			// "交强试算_20080201_其它_14组织返回信息");
			// }
			// }
			// }
		}

		Map<String,Double> leftAmountMap = compensateListVo.getLeftAmountMap();
		// 15实际赔付金额计算
		for(ThirdPartyInfo thirdPartyInfo:infos){
			double sumPay = 0d;
			List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
			for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){ // 损失项清单 的 返回损失分项
				if(thirdPartyCompInfo.getAdjPayAmount()>=0){// 等于0 是考虑重开赔案，剩余额度为0 by yk 20160704
					// 赔付损失分项 的实赔金额为 赔付损失分项 的 超限额赔付调整金额 + 赔付损失分项 的 剩余限额赔付金额
					thirdPartyCompInfo.setSumRealPay(thirdPartyCompInfo.getAdjPayAmount()+thirdPartyCompInfo.getLeftPayAmount());
				}else{// 该步骤基本不走，当小于保额时，会用PayAmount赋值给AdjPayAmount by yk 20160704
						// 赔付损失分项 的实赔金额为 赔付损失分项 的 限额比例赔付金额 + 赔付损失分项 的 剩余限额赔付金额
					thirdPartyCompInfo.setSumRealPay(thirdPartyCompInfo.getPayAmount()+thirdPartyCompInfo.getLeftPayAmount());
				}
				// thirdPartyCompInfo.setSumRealPay(MoneyFormator.format(new BigDecimal(thirdPartyCompInfo.getSumRealPay())).doubleValue());
				// thirdPartyCompInfo.setSumRealPay(thirdPartyCompInfo.getSumRealPay());
				sumPay = sumPay+thirdPartyCompInfo.getSumRealPay();
			}

			String key = "";
			if(compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES)){
				key = "med-"+thirdPartyInfo.getSerialNo();
			}else{
				key = "deh-"+thirdPartyInfo.getSerialNo();
			}

			double leftAmount = thirdPartyInfo.getDutyAmount()-sumPay;
			if(leftAmount<0d){
				leftAmount = 0d;
			}
			leftAmountMap.put(key,leftAmount);
		}

		// 16按损失项顺序返回实赔
		for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
			List<ThirdPartyCompInfo> thirdPartyCompInfos = compensateListVo.getReturnBZCompensateList();
			for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){ // 损失项清单 的 返回损失分项
				if(thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())){

					thirdPartyLossInfo.setSumRealPay(thirdPartyLossInfo.getSumRealPay()+thirdPartyCompInfo.getSumRealPay());
					thirdPartyLossInfo.setBzCompensateText(thirdPartyLossInfo.getBzCompensateText()+thirdPartyCompInfo.getBzCompensateText());
					thirdPartyLossInfo.setInsteadFlag(thirdPartyCompInfo.getInsteadFlag());
					thirdPartyLossInfo.setSumDefLoss(thirdPartyCompInfo.getPayAmount()); // 赔付损失项 的理算金额为 返回损失分项 的 限额比例赔付金额
					thirdPartyLossInfo.setAllAmount(allDamageAmount);
					thirdPartyLossInfo.setPayCarAmout(thirdPartyCompInfo.getPayCarAmount());
					thirdPartyLossInfo.setDamageType(compensateExp.getDamageType());

					// if(CodeConstants.InsteadFlag.noPolicyInstead.value.equals(thirdPartyCompInfo.getInsteadFlag())){
					// thirdPartyLossInfo.setNoPolicySumRealPaid(thirdPartyLossInfo.getNoPolicySumRealPaid() + thirdPartyCompInfo.getSumRealPay());
					// thirdPartyLossInfo.addNoPolicyInsteadThirdPartyCompInfos(thirdPartyCompInfo);
					// }
				}
			}
		}

		// 16设置赔付损失项的超限额标志位
		for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
			List<ThirdPartyCompInfo> thirdPartyCompInfos = compensateListVo.getReturnBZCompensateList();
			for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){ // 损失项清单 的 返回损失分项
				if(thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())&&thirdPartyCompInfo.getPayAmount()>thirdPartyCompInfo
						.getAdjPayAmount()){
					thirdPartyLossInfo.setOverFlag("1");
				}
			}
		}

		// 16设置赔付损失项的剩余限额赔付标志位
		for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
			List<ThirdPartyCompInfo> thirdPartyCompInfos = compensateListVo.getReturnBZCompensateList();
			for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){ // 损失项清单 的 返回损失分项
				if(thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())&&thirdPartyCompInfo.getLeftPayAmount()>0){
					thirdPartyLossInfo.setOverFlag("2");
				}
			}
		}

		// 16设置交强险无保代赔的所有限额
		for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
			List<ThirdPartyCompInfo> thirdPartyCompInfos = compensateListVo.getReturnBZCompensateList();
			for(ThirdPartyCompInfo thirdPartyCompInfo:thirdPartyCompInfos){ // 损失项清单 的 返回损失分项
				if(thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())){
					thirdPartyLossInfo.setAllAmount(allDamageAmount);
				}
			}
		}

		// 17屏蔽本车损失
		for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){

			if(thirdPartyLossInfo.getBzCompensateText()==null&&"".equals(thirdPartyLossInfo.getBzCompensateText())&&compensateExp.getDamageType()
					.equals(CodeConstants.FeeTypeCode.PROPLOSS)){
				thirdPartyLossInfo.setBzCompensateText(thirdPartyLossInfo.getBzCompensateText());
			}else{
				String bzCompensateText = String.format(CodeConstants.CompensateText.sumRealPayZfText.value,
						MoneyFormator.format4Output(thirdPartyLossInfo.getSumRealPay()));
				if(thirdPartyLossInfo.getSumRealPay()>0){
					thirdPartyLossInfo.setBzCompensateText(thirdPartyLossInfo.getBzCompensateText()+bzCompensateText);
				}
				if( !this.isCommercialOrCompulsory(compensateType)){
					logger.debug("交强赔付  "+thirdPartyLossInfo.getLossName()+thirdPartyLossInfo.getLossFeeName());

				}
				logger.debug(bzCompensateText);
			}
		}

		// 17屏蔽本车损失(屏蔽空的理算公式)
		for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
			if(thirdPartyLossInfo.getBzCompensateText()==null&&"".equals(thirdPartyLossInfo.getBzCompensateText())){
				thirdPartyLossInfo.setBzCompensateText("");
			}
		}

		// 扣减交强预赔金额。
		// this.compulsorySubPrePay(compensateExp.getThirdPartyLossInfos());
		// TODO 没看懂
		// this.calculateCi4CommonReCaseAdjust(thirdPartyLossInfos,compensateExp);

		// 2.31清空赔付损失车辆信息
		for(ThirdPartyInfo thirdPartyInfo:infos){ // 车
			thirdPartyInfo.setLeftAmount(0); // 剩余限额
			thirdPartyInfo.setLeftPayAmount(0); // 剩余限额赔偿金额
			thirdPartyInfo.setAdjPayAmount(0); // 累计赔付金额
			thirdPartyInfo.setDutyCompPay(0); // 责任限额计算金额
			thirdPartyInfo.setSumNoenoughPay(0); // 赔付损失项未赔足金额
			thirdPartyInfo.setPayAmount(0); // 限额比例赔偿金额
			thirdPartyInfo.deleteThirdPartyPayAmount(); // 赔付损失车辆.清空赔付损失分项
			thirdPartyInfo.setSumRealPay(0); // 车辆合计赔付金额
			thirdPartyInfo.setCarPaySum(0); // 总赔付金额
			thirdPartyInfo.setCarLossNum(0); // 车辆损失个数
			thirdPartyInfo.setCarLossSum(0); // 车辆的总损失金额
		}

		// 2.32清空赔付损失项
		compensateListVo.deleteReturnBZCompensateList();

		return compensateExp;
	}

	/**
	 * 计算。用于代替人保的规则引擎方法/Compensate_getSumRealPayApp/Compensate_getSumRealPayRule的商业险部分。
	 * @param compensateList
	 * @param compensateExp
	 * @return
	 */
	private CompensateExp calculateBi(CompensateListVo compensateListVo,CompensateExp compensateExp,Boolean cprcCase) {

		String kindCode = compensateExp.getKindCode();
		// 人保规则：保存裁定金额。
		{
			if("1".equals(compensateExp.getIsSuitFlag())){
				compensateExp.setSumRealPayJud(compensateExp.getSumRealPay());
			}
		}

		logger.info("cprcCase = "+cprcCase);
		logger.info("kindCode = "+kindCode);
		logger.info("compensateExp.getSumLoss() = "+compensateExp.getSumLoss());
		logger.info("compensateExp.getSumRealLoss() = "+compensateExp.getSumRealLoss());

		logger.info("compensateExp.getAmount() = "+compensateExp.getAmount());
		logger.info("compensateExp.getDamageAmount() = "+compensateExp.getDamageAmount());

		logger.info("compensateExp.getEntryItemCarCost() = "+compensateExp.getEntryItemCarCost());
		logger.info("compensateExp.getDamageItemRealCost() = "+compensateExp.getDamageItemRealCost());
		logger.info("compensateExp.getSumLossBZPaid() = "+compensateExp.getSumLossBZPaid());
		logger.info("compensateExp.getSumPrePay() = "+compensateExp.getSumPrePay());
		logger.info("compensateExp.getDeductibleRate() = "+compensateExp.getDeductibleRate());

		logger.info("compensateExp.getLockedBzRealPay() = "+compensateExp.getLockedBzRealPay());

		// 人保规则：获取免赔率。
		// 其中对deductibleRate的计算移入KindCalculator，在获取免赔率方法中计算，在组织商业险数据的时候，从理算项获取设置到CompensateExp中。
		if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode()) != null &&
				!CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode())){
			{
				String deductibleRateText = null;

				if(CodeConstants.KINDCODE.KINDCODE_A.equals(kindCode)||CodeConstants.KINDCODE.KINDCODE_B.equals(kindCode)||CodeConstants.KINDCODE.KINDCODE_G
						.equals(kindCode)||CodeConstants.KINDCODE.KINDCODE_D12.equals(kindCode)||CodeConstants.KINDCODE.KINDCODE_D11.equals(kindCode)||CodeConstants.KINDCODE.KINDCODE_X
						.equals(kindCode)){
					deductibleRateText = CodeConstants.CompensateText.text6.value;
				}

				if(CodeConstants.KINDCODE.KINDCODE_F.equals(kindCode)||CodeConstants.KINDCODE.KINDCODE_L.equals(kindCode)||CodeConstants.KINDCODE.KINDCODE_Z
						.equals(kindCode)||CodeConstants.KINDCODE.KINDCODE_X1.equals(kindCode)||CodeConstants.KINDCODE.KINDCODE_D2.equals(kindCode)||CodeConstants.KINDCODE.KINDCODE_R
						.equals(kindCode)){
					deductibleRateText = CodeConstants.CompensateText.text8.value;
				}

				compensateExp.setDeductibleRateText(deductibleRateText);
			}
		}
		

		// 判断是否代位损失项。
		String recoveryOrPayFlag = compensateExp.getRecoveryOrPayFlag();

		// 代位损失项必须有代位标志并且是车损险。
		if(recoveryOrPayFlag.equals(CodeConstants.PayFlagType.INSTEAD_PAY)&&CodeConstants.KINDCODE.KINDCODE_A.equals(kindCode)){
			logger.info("代且是位损失项必须有代位标志并车损险");
			// 人保规则：获取实际损失和施救费(代位)。
			// 功能：计算实际损失和施救费，考虑了车辆的实际价值和责任对方事故责任比例。
			this.dwCalculatKindCodeA(compensateListVo,compensateExp,cprcCase);

		}else if(recoveryOrPayFlag.equals(CodeConstants.PayFlagType.INSTEAD_PAY)&&CodeConstants.KINDCODE.KINDCODE_A1.equals(kindCode)){
			this.dwCalculatKindCodeA1(compensateListVo,compensateExp,cprcCase);

		}else{ // 非代位。
				// 人保规则：获取实际损失和施救费。
			final String payType = compensateExp.getPayType();
			logger.info("非代位:"+payType);
			if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_A)){
				if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode()) != null &&
						CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode())){
					this.calculatKindCode2020A(compensateListVo,compensateExp);
				}else{
					this.calculatKindCodeA(compensateListVo,compensateExp,cprcCase);
				}
				
			}
			// 机动车损失保险（IACJQL0001）
			if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_A1)){
				this.calculatKindCodeA1(compensateListVo,compensateExp);
			}
			if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_W1)){
				if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode()) != null &&
						CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode())){
					this.calculatKindCode2020W1(compensateListVo,compensateExp);
				}else{
					this.calculatKindCodeW1(compensateListVo,compensateExp);
				}
				
			}

			// 新增设备险（计算受损金额）。
			if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_X)){
				if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode()) != null &&
						CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode())){
					this.calculatKindCode2020X(compensateListVo,compensateExp);
				}else{
					this.calculatKindCodeX(compensateListVo,compensateExp,cprcCase);
				}
				
			}
			//附加条款
			if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_RS) || kindCode.equals(CodeConstants.KINDCODE.KINDCODE_VS) ||
					kindCode.equals(CodeConstants.KINDCODE.KINDCODE_DS) ||	kindCode.equals(CodeConstants.KINDCODE.KINDCODE_DC)){
				if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode()) != null &&
						CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode())){
					this.calculatKindCode2020_RS_VS_DS_DC(compensateListVo,compensateExp);
				}
			}
			
			if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_X3)){
				this.calculatKindCodeX3(compensateListVo,compensateExp);
			}

			// 发动机险（计算受损金额）。
			System.out.println(kindCode);
			if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_X1)||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_X2)){

				this.calculatKindCodeX2(compensateListVo,compensateExp,cprcCase);
			}

			// 商业三者险。
			if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_B)){
				if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode()) != null &&
						CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode())){
					this.calculatKindCode2020B(compensateListVo,compensateExp);
				}else{
					this.calculatKindCodeB(compensateListVo,compensateExp,cprcCase);
				}
				
			}
			// 盗抢险
			if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_G)){
				if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode()) != null &&
						CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode())){
					this.calculatKindCode2020G(compensateListVo,compensateExp);
				}else{
					this.calculatKindCodeG(compensateListVo,compensateExp,cprcCase);
				}
				
			}
			// 车上人员
			if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D12)||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D11)){
				if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode()) != null &&
						CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode())){
					if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D12)){
						this.calculatKindCode2020D12(compensateListVo,compensateExp);
					}else{
						this.calculatKindCode2020D11(compensateListVo,compensateExp);
					}
				}else{
					this.calculatKindCodeD11(compensateListVo,compensateExp,cprcCase);
				}
				
			}
			
			if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode()) != null &&
					CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode())){
				if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_BP)){
					this.calculatKindCode2020BP(compensateListVo,compensateExp);
				}
				if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D11P)){
					this.calculatKindCode2020D11P(compensateListVo,compensateExp);
				}
				if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D12P)){
					this.calculatKindCode2020D12P(compensateListVo,compensateExp);
				}
				if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_BS)){
					this.calculatKindCode2020_BS(compensateListVo,compensateExp);
				}
				if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D11S)){
					this.calculatKindCode2020_D11S(compensateListVo,compensateExp);
				}
				if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D12S)){
					this.calculatKindCode2020_D12S(compensateListVo,compensateExp);
				}
			}
			
			
			if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_F)){
				this.calculatKindCodeF(compensateListVo,compensateExp,cprcCase);
			}
			
			if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_L)){
				if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode()) != null &&
						CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode())){
					this.calculatKindCode2020L(compensateListVo,compensateExp);
			
				}else{
					this.calculatKindCodeL(compensateListVo,compensateExp,cprcCase);
				}
			}
				
			
			

			if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_Z)||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_E)){
				this.calculatKindCodeZ(compensateListVo,compensateExp,cprcCase);
			}

			if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_C)||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_RF)||kindCode
					.equals(CodeConstants.KINDCODE.KINDCODE_T)){
				if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode()) != null &&
						CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode())){
					if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_RF)){
						this.calculatKindCode2020RF(compensateListVo,compensateExp);
					}
					
				}else{
					this.calculatKindCodeC(compensateListVo,compensateExp,cprcCase);
				}
				
			}

			if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D2)){
				if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode()) != null &&
						CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode())){
					this.calculatKindCode2020D2(compensateListVo,compensateExp);
				}else{
					this.calculatKindCodeD2(compensateListVo,compensateExp,cprcCase);
				}
				
			}

			if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_R)||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_SS)){

				this.calculatKindCodeR(compensateListVo,compensateExp,cprcCase);
			}

			if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_NT)){
				this.calculatKindCodeNT(compensateListVo,compensateExp,cprcCase);
			}
			// K1 属于特约条款 不单独赔付 李雄军确认
			// kindCode.equals(CodeConstants.KINDCODE.KINDCODE_K1)
			if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode()) != null &&
					!CodeConstants.ISNEWCLAUSECODE2020_MAP.get(compensateExp.getRiskCode())){
				if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_K2)){
					this.calculatKindCodeK(compensateListVo,compensateExp,cprcCase);
				}
			}
			

			// 租车人人车失踪险
			if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_Z1)){
				this.calculatKindCodeZ1(compensateListVo,compensateExp,cprcCase);
			}

			if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_Z2)){
				this.calculatKindCodeZ2(compensateListVo,compensateExp,cprcCase);
			}

			if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_V1)){
				this.calculatKindCodeV1(compensateListVo,compensateExp,cprcCase);
			}

			if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_NZ)){
				this.calculatKindCodeNZ(compensateListVo,compensateExp,cprcCase);
			}
		}

		// 人保规则：解决0赔付数据异常问题。
		{
			if(compensateExp.getSumRealPay()<0.0001d){
				compensateExp.setSumRealPay(0);
				compensateExp.setMsumRealPay(0);
			}
		}

		// 人保规则：调整M险1分钱误差。
		{
			compensateExp.adjMsumRealPay();
		}

		// 人保规则：获取单项实赔金额。
		{
			if(compensateExp.getItemFlag().equals("1")){
				compensateExp.setItemSumRealPay(compensateExp.getSumRealPay());
			}
		}

		// 人保规则：获取赔付金额。
		{
			compensateExp.setSumDefLoss(compensateExp.getSumRealPay());
		}

		// 人保规则：X险超限额控制。
		{
			// 人保规则：获取赔付比例$40$新设备购置价大于险别保险金额$41$。
			{
				List<CompensateKindPay> returnKindPayList = compensateListVo.getReturnKindPayList();

				for(CompensateKindPay kindPay:returnKindPayList){
					List<CompensateExp> returnKindPayListCompensateList = kindPay.getCompensateExpList();

					for(CompensateExp exp:returnKindPayListCompensateList){
						if(kindPay.getAdjKindSum()>compensateExp.getDeviceActualValue()&&kindPay.getDamageAmount()>compensateExp
								.getDeviceActualValue()&&exp.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_X)&&kindPay.getDamageAmount()>0&&kindPay
								.getKindLossNum()!=1){
							exp.setClaimRate(compensateExp.getDeviceActualValue()/kindPay.getAdjKindSum());
						}
					}
				}
			}

			// 人保规则：获取赔付比例$40$险别保险金额大于新设备购置价$41$。
			{
				List<CompensateKindPay> returnKindPayList = compensateListVo.getReturnKindPayList();

				for(CompensateKindPay kindPay:returnKindPayList){
					List<CompensateExp> returnKindPayListCompensateList = kindPay.getCompensateExpList();

					for(CompensateExp exp:returnKindPayListCompensateList){
						if(kindPay.getAdjKindSum()>kindPay.getDamageAmount()&&kindPay.getDamageAmount()<compensateExp.getDeviceActualValue()&&exp
								.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_X)&&kindPay.getDamageAmount()>0&&kindPay.getKindLossNum()!=1){
							exp.setClaimRate(kindPay.getDamageAmount()/kindPay.getAdjKindSum());
						}
					}
				}
			}

			// 人保规则：超限额赔付调整。
			{
				// 保存超限额调整前的不计免赔金额，用于生成不计免赔计算书公式。
				// zhongyuhai 20121101
				compensateExp.setMsumRealPayBeforeJud(compensateExp.getMsumRealPay());

				// if(compensateExp.getClaimRate()<1){ // 理算大对象的赔付比例 < 1
				// if(compensateExp.getClaimRescueRate()<1){ // 理算大对象的施救费赔付比例 < 1
				// if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_A)){ // 车损险
				// compensateExp
				// .setSumRealPay(( compensateExp.getSumLoss()*compensateExp.getLossQuantity()-compensateExp.getSumRest() )*compensateExp
				// .getClaimRate()*compensateExp.getIndemnityDutyRate()*compensateExp.getDeductibleRate()+compensateExp
				// .getRescueFee()*compensateExp.getClaimRescueRate()*compensateExp.getIndemnityDutyRate()*compensateExp
				// .getDeductibleRate()-compensateExp.getSumPrePay());
				// compensateExp
				// .setMsumRealPay(( compensateExp.getSumLoss()*compensateExp.getLossQuantity()+compensateExp.getRescueFee()-compensateExp
				// .getSumRest() )*compensateExp.getClaimRate()*compensateExp.getIndemnityDutyRate()*compensateExp
				// .getExceptDeductRate());
				//
				// }else if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D12)||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D11)||kindCode
				// .equals(CodeConstants.KINDCODE.KINDCODE_C)||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_R)
				// || kindCode.equals(CodeConstants.KINDCODE.KINDCODE_B)){ // 车上乘客责任险 或 车上司机险 或 代步车险 或 精神险 不做调整
				// ;
				// }else if(
				// kindCode.equals(CodeConstants.KINDCODE.KINDCODE_G)|| // 盗抢险
				// kindCode.equals(CodeConstants.KINDCODE.KINDCODE_L)|| // 车身划痕损失险
				// kindCode.equals(CodeConstants.KINDCODE.KINDCODE_Z)|| // 自燃损失险
				// kindCode.equals(CodeConstants.KINDCODE.KINDCODE_X1)|| // 发动机特别损失险
				// kindCode.equals(CodeConstants.KINDCODE.KINDCODE_X)|| // 新增设备损失险
				// kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D2)){ // 车上货物责任险
				// compensateExp
				// .setSumRealPay(( compensateExp.getSumLoss()*compensateExp.getLossQuantity()+compensateExp.getRescueFee()-compensateExp
				// .getSumRest() )*compensateExp.getDeductibleRate()*compensateExp.getClaimRate()-compensateExp
				// .getSumPrePay());
				// compensateExp
				// .setMsumRealPay(( compensateExp.getSumLoss()*compensateExp.getLossQuantity()+compensateExp.getRescueFee()-compensateExp
				// .getSumRest() )*compensateExp.getExceptDeductRate()*compensateExp.getClaimRate());
				//
				// if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_B)){
				// compensateExp.setSumRealPay(( compensateExp.getSumRealPay()+compensateExp.getSumPrePay() )*compensateExp
				// .getIndemnityDutyRate()-compensateExp.getSumPrePay());
				// compensateExp.setMsumRealPay(compensateExp.getMsumRealPay()*compensateExp.getIndemnityDutyRate());
				// }
				//
				// }else{ // 其他险别
				// compensateExp
				// .setSumRealPay(( compensateExp.getSumLoss()*compensateExp.getLossQuantity()+compensateExp.getRescueFee() )*compensateExp
				// .getDeductibleRate()*compensateExp.getClaimRate()-compensateExp.getSumPrePay());
				// compensateExp
				// .setMsumRealPay(( compensateExp.getSumLoss()*compensateExp.getLossQuantity()+compensateExp.getRescueFee() )*compensateExp
				// .getExceptDeductRate()*compensateExp.getClaimRate());
				//
				// }
				// }else{
				// if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_A)){ // 车损险
				// compensateExp
				// .setSumRealPay(( compensateExp.getSumLoss()*compensateExp.getLossQuantity()-compensateExp.getSumRest() )*compensateExp
				// .getClaimRate()*compensateExp.getIndemnityDutyRate()*compensateExp.getDeductibleRate()+compensateExp
				// .getSumRealPayRescue()-compensateExp.getSumPrePay());
				// compensateExp
				// .setMsumRealPay(( compensateExp.getSumLoss()*compensateExp.getLossQuantity()-compensateExp.getSumRest() )*compensateExp
				// .getClaimRate()*compensateExp.getIndemnityDutyRate()*compensateExp.getExceptDeductRate()+compensateExp
				// .getMsumRealPayRescue());
				//
				// }else if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D12)||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D11)||kindCode
				// .equals(CodeConstants.KINDCODE.KINDCODE_C)||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_R)
				// ||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_B)){ // 车上乘客责任险 或 车上司机险 或 代步车险 或 精神险 不做调整
				// }else if(
				// kindCode.equals(CodeConstants.KINDCODE.KINDCODE_G)|| // 盗抢险
				// kindCode.equals(CodeConstants.KINDCODE.KINDCODE_L)|| // 车身划痕损失险
				// kindCode.equals(CodeConstants.KINDCODE.KINDCODE_Z)|| // 自燃损失险
				// kindCode.equals(CodeConstants.KINDCODE.KINDCODE_X1)|| // 发动机特别损失险
				// kindCode.equals(CodeConstants.KINDCODE.KINDCODE_X)|| // 新增设备损失险
				// kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D2)){ // 车上货物掉落责任险
				// compensateExp
				// .setSumRealPay(( compensateExp.getSumLoss()*compensateExp.getLossQuantity()+compensateExp.getRescueFee()-compensateExp
				// .getSumRest() )*compensateExp.getDeductibleRate()*compensateExp.getClaimRate()-compensateExp
				// .getSumPrePay());
				// compensateExp
				// .setMsumRealPay(( compensateExp.getSumLoss()*compensateExp.getLossQuantity()+compensateExp.getRescueFee()-compensateExp
				// .getSumRest() )*compensateExp.getExceptDeductRate()*compensateExp.getClaimRate());
				//
				// if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_B)){
				// compensateExp.setSumRealPay(( compensateExp.getSumRealPay()+compensateExp.getSumPrePay() )*compensateExp
				// .getIndemnityDutyRate()-compensateExp.getSumPrePay());
				// compensateExp.setMsumRealPay(compensateExp.getMsumRealPay()*compensateExp.getIndemnityDutyRate());
				// }
				// }else{ // 其他险别
				// compensateExp
				// .setSumRealPay(( compensateExp.getSumLoss()*compensateExp.getLossQuantity()+compensateExp.getRescueFee() )*compensateExp
				// .getDeductibleRate()*compensateExp.getIndemnityDutyRate()*compensateExp.getClaimRate()-compensateExp
				// .getSumPrePay());
				// compensateExp
				// .setMsumRealPay(( compensateExp.getSumLoss()*compensateExp.getLossQuantity()+compensateExp.getRescueFee() )*compensateExp
				// .getExceptDeductRate()*compensateExp.getIndemnityDutyRate()*compensateExp.getClaimRate());
				// }
				// }
				// }else{ // 理算大对象的赔付比例 !< 1
				// if(compensateExp.getClaimRescueRate()<1){
				// if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_A)){
				// compensateExp.setSumRealPay(compensateExp.getRescueFee()*compensateExp.getClaimRescueRate()*compensateExp
				// .getIndemnityDutyRate()*compensateExp.getDeductibleRate()+compensateExp.getSumRealPayLoss()-compensateExp
				// .getSumPrePay());
				// compensateExp.setMsumRealPay(compensateExp.getRescueFee()*compensateExp.getClaimRate()*compensateExp
				// .getIndemnityDutyRate()*compensateExp.getExceptDeductRate()+compensateExp.getMsumRealPayLoss());
				//
				// }else if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D12)||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D11)||kindCode
				// .equals(CodeConstants.KINDCODE.KINDCODE_C)||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_R)){ // 车上乘客责任险 或 车上司机险 或 代步车险 或 精神险 不做调整
				// ;
				// }else if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_B)|| // 商业三者险
				// kindCode.equals(CodeConstants.KINDCODE.KINDCODE_G)|| // 盗抢险
				// kindCode.equals(CodeConstants.KINDCODE.KINDCODE_L)|| // 车身划痕损失险
				// kindCode.equals(CodeConstants.KINDCODE.KINDCODE_Z)|| // 自燃损失险
				// kindCode.equals(CodeConstants.KINDCODE.KINDCODE_X1)|| // 发动机特别损失险
				// kindCode.equals(CodeConstants.KINDCODE.KINDCODE_X)|| // 新增设备损失险
				// kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D2)){ // 车上货物掉落责任险
				// compensateExp
				// .setSumRealPay(( compensateExp.getSumLoss()*compensateExp.getLossQuantity()+compensateExp.getRescueFee()-compensateExp
				// .getSumRest() )*compensateExp.getDeductibleRate()*compensateExp.getClaimRate()-compensateExp
				// .getSumPrePay());
				// compensateExp
				// .setMsumRealPay(( compensateExp.getSumLoss()*compensateExp.getLossQuantity()+compensateExp.getRescueFee()-compensateExp
				// .getSumRest() )*compensateExp.getExceptDeductRate());
				//
				// if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_B)){
				// compensateExp.setSumRealPay(( compensateExp.getSumRealPay()+compensateExp.getSumPrePay() )*compensateExp
				// .getIndemnityDutyRate()-compensateExp.getSumPrePay());
				// compensateExp.setMsumRealPay(compensateExp.getMsumRealPay()*compensateExp.getIndemnityDutyRate());
				// }
				// }else{ // 其他险别
				// compensateExp
				// .setSumRealPay(( compensateExp.getSumLoss()*compensateExp.getLossQuantity()+compensateExp.getRescueFee() )*compensateExp
				// .getDeductibleRate()*compensateExp.getIndemnityDutyRate()*compensateExp.getClaimRate()-compensateExp
				// .getSumPrePay());
				// compensateExp
				// .setMsumRealPay(( compensateExp.getSumLoss()*compensateExp.getLossQuantity()+compensateExp.getRescueFee() )*compensateExp
				// .getExceptDeductRate()*compensateExp.getIndemnityDutyRate()*compensateExp.getClaimRate());
				// }
				// }
				// }
			}

			// 人保规则：计算协议金额。
			{
				if("1".equals(compensateListVo.getIsSuitFlag())){
					compensateListVo.setExgratiaFee(compensateListVo.getExgratiaFee()+compensateExp.getSumRealPayJud()-compensateExp.getSumRealPay());
				}
			}
		}

		return compensateExp;
	}

	/**
	 * 代位求偿计算 目前只考虑新条款 车损赔款＝（min（保险金额，定损金额）） 施救费赔款=（min（保险金额，核定施救费）） 代位实际赔款＝车损险赔款+车损不计免赔赔款 = max【（车损赔款+施救费赔款）×（1－事故责任免赔率）×（1－可选免赔率之和）－绝对免赔额）,0】+ 【（车损赔款+施救费赔款）×（1－可选免赔率之和）×事故责任免赔率+
	 * min（0，（（车损赔款+施救费赔款）×（1－事故责任免赔率）×（1－可选免赔率之和）－绝对免赔额）】
	 * @param compensateListVo
	 * @param compensateExp
	 * @param cprcCase
	 * @modified: ☆YangKun(2016年5月26日 下午7:19:28): <br>
	 */
	private void dwCalculatKindCodeA(CompensateListVo compensateListVo,CompensateExp compensateExp,Boolean cprcCase) {
		double sumRealLoss = compensateExp.getSumLoss();
		double rescueRealFee = compensateExp.getRescueFee();
		if(compensateExp.getSumLoss()>compensateExp.getDamageAmount()){
			sumRealLoss = compensateExp.getDamageAmount();
		}

		if(compensateExp.getRescueFee()>compensateExp.getDamageAmount()){
			rescueRealFee = compensateExp.getDamageAmount();
		}

		// 计算免赔额。
		double deductibleAmount = ( sumRealLoss+rescueRealFee )*compensateExp.getDeductibleRate();
		if(deductibleAmount>compensateExp.getDeductFee()){// （车损赔款+施救费赔款）×（1－事故责任免赔率）×（1－可选免赔率之和） 大于免赔额
			compensateExp.setSumRealPay(deductibleAmount-compensateExp.getDeductFee());
			compensateExp.setMsumRealPay(( sumRealLoss+rescueRealFee )*compensateExp.getExceptDeductRate());

			// 赔款 = (车损赔款 + 施救费赔款) × (1 - 事故责任免赔率) × (1 - 可选免赔率) - 免赔额
			String dwPayText = String.format(CodeConstants.CompensateText.sumRealPayText_dw.value,sumRealLoss,rescueRealFee,
					MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
					MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100),
					MoneyFormator.format4Output(compensateExp.getDeductFee()));
			compensateExp.setSumRealPayText(dwPayText);// sumRealPayText
			logger.debug(dwPayText);// sumRealPayText

		}else{
			compensateExp.setSubrogationBRealPay(0);
			compensateExp
					.setMsumSubrogation(( sumRealLoss+rescueRealFee )*compensateExp.getExceptDeductRate()+( ( sumRealLoss+rescueRealFee )-compensateExp
							.getDeductFee() ));

		}

	}

	private void dwCalculatKindCodeA1(CompensateListVo compensateListVo,CompensateExp compensateExp,Boolean cprcCase) {
		double sumRealLoss = compensateExp.getSumLoss();
		double rescueRealFee = compensateExp.getRescueFee();
		if(compensateExp.getSumLoss()>compensateExp.getDamageAmount()){
			sumRealLoss = compensateExp.getDamageAmount();
		}

		if(compensateExp.getRescueFee()>compensateExp.getDamageAmount()){
			rescueRealFee = compensateExp.getDamageAmount();
		}
		// 施救费
		compensateExp.setRescueRealFee(rescueRealFee-compensateExp.getRescueFeeBZPaid());
		String rescueFeeText = String.format(CodeConstants.CompensateText.rescueFeeText_replace_A1.value,MoneyFormator.format4Output(rescueRealFee),
				MoneyFormator.format4Output(compensateExp.getRescueFeeBZPaid()));
		logger.info("rescueFeeText = "+rescueFeeText);
		compensateExp.setRescueFeeText(rescueFeeText);

		// 计算约定绝对免赔率
		double dutyDeductibleRate = 0;
		List<PrpLCItemKindVo> itemKindVoList = registQueryService.findItemKindVo(compensateListVo.getRegistNo(),CodeConstants.KINDCODE.KINDCODE_M3);
		if(itemKindVoList!=null&&itemKindVoList.size()>0){
			for(PrpLCItemKindVo itemKindVo:itemKindVoList){
				if(itemKindVo.getValue()!=null){
					dutyDeductibleRate = Double.valueOf(itemKindVo.getValue().divide(new BigDecimal(100),2,RoundingMode.HALF_UP).toString());
				}
			}
		}

		// 车损赔款
		compensateExp.setSumRealLoss(sumRealLoss-compensateExp.getRescueFeeBZPaid());
		String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossTex_replace_A1.value,MoneyFormator.format4Output(sumRealLoss),
				MoneyFormator.format4Output(compensateExp.getRescueFeeBZPaid()));
		logger.info("sumRealLossText = "+sumRealLossText);
		compensateExp.setSumRealLossText(sumRealLossText);

		compensateExp
				.setSumRealPay(( rescueRealFee+sumRealLoss-compensateExp.getAbsolvePayAmt() )*compensateExp.getIndemnityDutyRate()*( 1-dutyDeductibleRate ));
		String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText24.value,MoneyFormator.format4Output(sumRealLoss),
				MoneyFormator.format4RateOutput(rescueRealFee),MoneyFormator.format4RateOutput(compensateExp.getAbsolvePayAmt()),
				MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100),MoneyFormator.format4RateOutput(dutyDeductibleRate*100));
		logger.info("sumRealPayText = "+sumRealPayText);
		compensateExp.setSumRealPayText(sumRealPayText);

	}

	/**
	 * 车损险计算 1获取实际损失和施救费。 获取理算金额
	 * @param compensateListVo TODO 重开赔案 车损险保额 需要和已赔付的定损金额-已扣除 + 本车赔付的定损金额比较 施救费 已赔付的施救费金额 + 本车赔付的施救费金额比较
	 * @param compensateExp
	 * @modified: ☆YZY(2020年9月2日 下午16:13:51): <br>
	 */
	private void calculatKindCode2020A(CompensateListVo compensateListVo,CompensateExp compensateExp) {
            //施救费计算,险别保额大于或等于施救费赔款则施救费赔款取施救费赔款否则取险别保额
		    if((compensateExp.getRescueFee()-compensateExp.getRescueFeeBZPaid() )*compensateExp.getIndemnityDutyRate()<=compensateExp.getDamageAmount()) {
		    	compensateExp.setRescueRealFee(( compensateExp.getRescueFee()-compensateExp.getRescueFeeBZPaid() )*compensateExp.getIndemnityDutyRate());
		    	String rescueFeeText = String.format(CodeConstants.CompensateText.rescueFeeText2020.value,
		    			MoneyFormator.format4Output(compensateExp.getDamageAmount()),
						MoneyFormator.format4Output(compensateExp.getRescueFee()),
						MoneyFormator.format4Output(compensateExp.getRescueFeeBZPaid()),
						MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100));
				logger.info("rescueFeeText = "+rescueFeeText);
				compensateExp.setRescueFeeText(rescueFeeText);
		    }else {
		    	compensateExp.setRescueRealFee(compensateExp.getDamageAmount());
		    	String rescueFeeText = String.format(CodeConstants.CompensateText.rescueFeeText2020.value,
		    			MoneyFormator.format4Output(compensateExp.getDamageAmount()),
						MoneyFormator.format4Output(compensateExp.getRescueFee()),
						MoneyFormator.format4Output(compensateExp.getRescueFeeBZPaid()),
						MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100));
				logger.info("rescueFeeText = "+rescueFeeText);
				compensateExp.setRescueFeeText(rescueFeeText);
		    }
		    //车损赔款计算,险别保额大于或等于车损赔款时，车损赔款取车损赔款否则取险别保额
		    if(( compensateExp.getSumLoss()-compensateExp.getSumLossBZPaid()-compensateExp.getAbsolvePayAmt()-compensateExp.getOtherDeductFee() )*compensateExp.getIndemnityDutyRate()<=compensateExp.getDamageAmount()) {
		    	compensateExp.setSumRealLoss(( compensateExp.getSumLoss()-compensateExp.getSumLossBZPaid()-compensateExp.getAbsolvePayAmt() - compensateExp.getOtherDeductFee() )*compensateExp.getIndemnityDutyRate());
		    	String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossTextBelowDamageItemRealCost4ZF2020_CPRC.value,
		    			MoneyFormator.format4Output(compensateExp.getDamageAmount()),
						MoneyFormator.format4Output(compensateExp.getSumLoss()),
						MoneyFormator.format4Output(compensateExp.getSumLossBZPaid()),
						MoneyFormator.format4Output(compensateExp.getAbsolvePayAmt()),
						MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
						MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100));
				logger.info("sumRealLossText = "+sumRealLossText);
				compensateExp.setSumRealLossText(sumRealLossText);
		    }else {
		    	compensateExp.setSumRealLoss(compensateExp.getDamageAmount());
		    	String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossTextBelowDamageItemRealCost4ZF2020_CPRC.value,
		    			MoneyFormator.format4Output(compensateExp.getDamageAmount()),
						MoneyFormator.format4Output(compensateExp.getSumLoss()),
						MoneyFormator.format4Output(compensateExp.getSumLossBZPaid()),
						MoneyFormator.format4Output(compensateExp.getAbsolvePayAmt()),
						MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
						MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100));
				logger.info("sumRealLossText = "+sumRealLossText);
				compensateExp.setSumRealLossText(sumRealLossText);
		    }
			
	        //机动车损失保险赔款计算
		    if(compensateExp.getBugFlag()){//买了附加绝对免赔条款
				compensateExp.setSumRealPay(( compensateExp.getSumRealLoss()+compensateExp.getRescueRealFee() ));
				double deductFee = this.makeDeductFee(compensateExp);
				if(compensateExp.getSumRealPay()<=deductFee){//车损赔款小于免赔额
					compensateExp.setSumRealPay(0);//真实赔付
					String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_FJ7.value,
							MoneyFormator.format4Output(compensateExp.getSumRealLoss()),
							MoneyFormator.format4Output(compensateExp.getRescueRealFee()),
							MoneyFormator.format4Output(deductFee),
							MoneyFormator.format4Output(compensateExp.getAbsrate()));
							
					logger.info("sumRealPayText = "+sumRealPayText);
					compensateExp.setSumRealPayText(sumRealPayText);
				}else{
						compensateExp.setSumRealPay(((compensateExp.getSumRealLoss()+compensateExp.getRescueRealFee() -deductFee)*(1-(compensateExp.getAbsrate()/100))));//真实赔付
						String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_FJ_A.value,
								MoneyFormator.format4Output(compensateExp.getSumRealLoss()),
								MoneyFormator.format4Output(compensateExp.getRescueRealFee()),
								MoneyFormator.format4Output(deductFee),
								MoneyFormator.format4Output(compensateExp.getAbsrate()));
						logger.info("sumRealPayText = "+sumRealPayText);
						compensateExp.setSumRealPayText(sumRealPayText);
					
				}
				
            }else{
            	//未买附加绝对免赔条款
				compensateExp.setSumRealPay(compensateExp.getSumRealLoss()+compensateExp.getRescueRealFee());
				double deductFee = this.makeDeductFee(compensateExp);
				if(compensateExp.getSumRealPay()<=deductFee){//车损赔款小于免赔额
					compensateExp.setSumRealPay(0);//真实赔付
					String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_7.value,
							MoneyFormator.format4Output(compensateExp.getSumRealLoss()),
							MoneyFormator.format4Output(compensateExp.getRescueRealFee()),
							MoneyFormator.format4Output(deductFee));
					logger.info("sumRealPayText = "+sumRealPayText);
					compensateExp.setSumRealPayText(sumRealPayText);
				}else{
						compensateExp.setSumRealPay(compensateExp.getSumRealLoss()+compensateExp.getRescueRealFee()-deductFee);//真实赔付
						String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_6.value,
								MoneyFormator.format4Output(compensateExp.getSumRealLoss()),
								MoneyFormator.format4Output(compensateExp.getRescueRealFee()),
								MoneyFormator.format4Output(deductFee));
						logger.info("sumRealPayText = "+sumRealPayText);
						compensateExp.setSumRealPayText(sumRealPayText);
					
				}
            }
			

	}

	/**
	 * 车损险计算 1获取实际损失和施救费。 获取理算金额
	 * @param compensateListVo TODO 重开赔案 车损险保额 需要和已赔付的定损金额-已扣除 + 本车赔付的定损金额比较 施救费 已赔付的施救费金额 + 本车赔付的施救费金额比较
	 * @param compensateExp
	 * @param cprcCase
	 * @modified: ☆YangKun(2016年5月26日 下午7:20:51): <br>
	 */
	private void calculatKindCodeA(CompensateListVo compensateListVo,CompensateExp compensateExp,Boolean cprcCase) {
		// 车损险（实际损失金额和施救费金额）。
		// 车损险非代赔案件按正常流程理算。
		logger.info("车损险非代赔案件按正常流程理算");

		if("1".equals(compensateExp.getNoFindThirdFlag())){
			calculatNoFondThirdKindCodeA(compensateListVo,compensateExp,cprcCase);

		}else{
			// if( !CodeConstants.PayFlagType.INSTEAD_PAY.equals(compensateExp.getPayType())){
			// 非代位判断开始 施救费
			if(cprcCase){
				if(compensateExp.getRescueFee()<compensateExp.getAmount()){
					compensateExp.setRescueRealFee(( compensateExp.getRescueFee()-compensateExp.getRescueFeeBZPaid() )*compensateExp
							.getIndemnityDutyRate());
					String rescueFeeText = String.format(CodeConstants.CompensateText.rescueFeeText.value,
							MoneyFormator.format4Output(compensateExp.getRescueFee()),
							MoneyFormator.format4Output(compensateExp.getRescueFeeBZPaid()),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100));
					logger.info("rescueFeeText = "+rescueFeeText);
					compensateExp.setRescueFeeText(rescueFeeText);
				}else{
					compensateExp.setRescueRealFee(( compensateExp.getAmount()-compensateExp.getRescueFeeBZPaid() )*compensateExp
							.getIndemnityDutyRate());
					String rescueFeeText = String.format(CodeConstants.CompensateText.rescueFeeText_over.value,
							MoneyFormator.format4Output(compensateExp.getAmount()),MoneyFormator.format4Output(compensateExp.getRescueFeeBZPaid()),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100));
					logger.info("rescueFeeText = "+rescueFeeText);
					compensateExp.setRescueFeeText(rescueFeeText);
				}
			}else{
				// 非代位施救费原始代码开始
				if(compensateExp.getDamageAmount()<compensateExp.getEntryItemCarCost()){
					compensateExp
							.setRescueRealFee(( compensateExp.getRescueFee()-compensateExp.getRescueFeeBZPaid() )*( compensateExp.getAmount()/compensateExp
									.getEntryItemCarCost() )*compensateExp.getIndemnityDutyRate());
					String rescueFeeText = String.format(CodeConstants.CompensateText.rescueFeeText_unfull.value,
							MoneyFormator.format4Output(compensateExp.getRescueFee()),
							MoneyFormator.format4Output(compensateExp.getRescueFeeBZPaid()),MoneyFormator.format4Output(compensateExp.getAmount()),
							MoneyFormator.format4Output(compensateExp.getEntryItemCarCost()),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100));
					logger.info("rescueFeeText = "+rescueFeeText);
					compensateExp.setRescueFeeText(rescueFeeText);
				}else{
					compensateExp.setRescueRealFee(( compensateExp.getRescueFee()-compensateExp.getRescueFeeBZPaid() )*compensateExp
							.getIndemnityDutyRate());
					String rescueFeeText = String.format(CodeConstants.CompensateText.rescueFeeText.value,
							MoneyFormator.format4Output(compensateExp.getRescueFee()),
							MoneyFormator.format4Output(compensateExp.getRescueFeeBZPaid()),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100));
					logger.info("rescueFeeText = "+rescueFeeText);
					compensateExp.setRescueFeeText(rescueFeeText);
				}
				// 非代位施救费原始代码结束
			}
			// 赔款
			if(cprcCase){
				double sumLoss = compensateExp.getSumLoss()-compensateExp.getOtherDeductFee();
				if(sumLoss<compensateExp.getAmount()){ // 损失金额 < 险别保额
					logger.info("损失金额 < 险别保额");
					compensateExp.setSumRealLoss(( sumLoss-compensateExp.getSumLossBZPaid()-compensateExp.getAbsolvePayAmt() )*compensateExp
							.getIndemnityDutyRate());
					String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossTextBelowDamageItemRealCost4ZF_CPRC.value,
							MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getSumLossBZPaid()),
							MoneyFormator.format4Output(compensateExp.getAbsolvePayAmt()),
							MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100));
					logger.info("sumRealLossText = "+sumRealLossText);
					compensateExp.setSumRealLossText(sumRealLossText);
				}else{ // 损失金额 => 险别保额
					logger.info("损失金额 => 险别保额");
					compensateExp
							.setSumRealLoss(( compensateExp.getAmount()-compensateExp.getSumLossBZPaid()-compensateExp.getAbsolvePayAmt() )*compensateExp
									.getIndemnityDutyRate());
					String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossTextBelowDamageItemRealCostOVER4ZF_CPRC.value,
							MoneyFormator.format4Output(compensateExp.getAmount()),MoneyFormator.format4Output(compensateExp.getSumLossBZPaid()),
							MoneyFormator.format4Output(compensateExp.getAbsolvePayAmt()),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100));
					logger.info("sumRealLossText = "+sumRealLossText);
					compensateExp.setSumRealLossText(sumRealLossText);
				}
			}else{
				// 损失金额 < 实际价值。按损失金额赔付。
				// 车损险理算原始代码开始
				double sumLoss = compensateExp.getSumLoss()-compensateExp.getOtherDeductFee();
				if(sumLoss<compensateExp.getDamageItemRealCost()){
					logger.info("损失金额 < 实际价值。按损失金额赔付");
					compensateExp
							.setSumRealLoss(( compensateExp.getSumLoss()-compensateExp.getSumLossBZPaid()-compensateExp.getAbsolvePayAmt()-compensateExp
									.getOtherDeductFee() )*compensateExp.getIndemnityDutyRate()*( compensateExp.getAmount()/compensateExp
									.getEntryItemCarCost() ));

					String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossTextBelowDamageItemRealCost4ZF.value,
							MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getSumLossBZPaid()),
							MoneyFormator.format4Output(compensateExp.getAbsolvePayAmt()),
							MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100),
							MoneyFormator.format4Output(compensateExp.getAmount()),MoneyFormator.format4Output(compensateExp.getEntryItemCarCost()));
					logger.info("sumRealLossText = "+sumRealLossText);
					compensateExp.setSumRealLossText(sumRealLossText);

				}else{// 按实际价值赔付。
					logger.info("按实际价值赔付。");

					compensateExp.setSumRealLoss(( compensateExp.getDamageItemRealCost()-compensateExp.getSumLossBZPaid()-compensateExp
							.getAbsolvePayAmt() )*compensateExp.getIndemnityDutyRate());

					String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossTextOverRealCost4ZF.value,
							MoneyFormator.format4Output(compensateExp.getDamageItemRealCost()),
							MoneyFormator.format4Output(compensateExp.getSumLossBZPaid()),
							MoneyFormator.format4Output(compensateExp.getAbsolvePayAmt()),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100));
					logger.info("sumRealLossText = "+sumRealLossText);
					compensateExp.setSumRealLossText(sumRealLossText);

				}
				// 车损险理算原始代码结束
			}
			// } // 非代位判断结束
			// 车损险代赔案件需要扣减追偿金额。
			// else{
			// calculatKindCodeADW(compensateListVo,compensateExp,cprcCase);
			// }

			// 人保规则：获取理算金额。
			if( !compensateExp.getRecoveryOrPayFlag().equals(CodeConstants.PayFlagType.INSTEAD_PAY)){
				// 判断施救费。
				if(compensateExp.getRescueRealFee()<=compensateExp.getDamageAmount()){

					if(compensateExp.getSumRealLoss()<=compensateExp.getDamageAmount()){
						// 计算免赔额。
						double deductibleAmount = ( compensateExp.getSumRealLoss()+compensateExp.getRescueRealFee() )*compensateExp
								.getDeductibleRate();
						if(deductibleAmount>compensateExp.getDeductFee()){// （车损赔款+施救费赔款）×（1－事故责任免赔率）×（1－可选免赔率之和） 大于免赔额
							// 规则12。

							compensateExp.setSumRealPay(( compensateExp.getSumRealLoss()+compensateExp.getRescueRealFee() )*compensateExp
									.getDeductibleRate());
							double deductFee = this.makeDeductFee(compensateExp);
							compensateExp.setSumRealPay(compensateExp.getSumRealPay()-deductFee);
							compensateExp.setMsumRealPay(( compensateExp.getSumRealLoss()+compensateExp.getRescueRealFee() )*compensateExp
									.getExceptDeductRate());

							compensateExp.setSumRealPayLoss(compensateExp.getSumRealLoss()*compensateExp.getDeductibleRate()-deductFee);
							compensateExp.setSumRealPayRescue(compensateExp.getRescueRealFee()*compensateExp.getDeductibleRate());
							compensateExp.setMsumRealPayLoss(compensateExp.getSumRealLoss()*compensateExp.getExceptDeductRate());
							compensateExp.setMsumRealPayRescue(compensateExp.getRescueRealFee()*compensateExp.getExceptDeductRate());

							String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText6.value,
									MoneyFormator.format4Output(compensateExp.getSumRealLoss()),
									MoneyFormator.format4Output(compensateExp.getRescueRealFee()),
									MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
									MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100),
									MoneyFormator.format4Output(deductFee));
							logger.info("sumRealPayText = "+sumRealPayText);
							compensateExp.setSumRealPayText(sumRealPayText);
						}else{// （车损赔款+施救费赔款）×（1－事故责任免赔率）×（1－可选免赔率之和） 小于免赔额
								// 规则14。
							compensateExp.setSumRealPay(0d);
							double deductFee = this.makeDeductFee(compensateExp);
							compensateExp.setSumRealPay(compensateExp.getSumRealPay()-deductFee);
							compensateExp.setMsumRealPay(0d);
							// （车损赔款+施救费赔款）×（1－事故责任免赔率）×（1－可选免赔率之和） 小于绝对免赔额，
							// 不计免赔金额+（车损赔款+施救费赔款）×（1－事故责任免赔率）×（1－可选免赔率之和）-绝对免赔额
							double msumRealPay = ( compensateExp.getSumRealLoss()+compensateExp.getRescueRealFee() )*compensateExp
									.getExceptDeductRate()+( deductibleAmount-compensateExp.getDeductFee() );
							if(msumRealPay>0){
								compensateExp.setMsumRealPay(msumRealPay);
							}

							compensateExp.setSumRealPayLoss(compensateExp.getSumRealLoss()*compensateExp.getDeductibleRate()-deductFee);
							compensateExp.setSumRealPayRescue(compensateExp.getRescueRealFee()*compensateExp.getDeductibleRate());
							compensateExp.setMsumRealPayLoss(compensateExp.getSumRealLoss()*compensateExp.getExceptDeductRate());
							compensateExp.setMsumRealPayRescue(compensateExp.getRescueRealFee()*compensateExp.getExceptDeductRate());
							String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText7.value,
									MoneyFormator.format4Output(compensateExp.getSumRealLoss()),
									MoneyFormator.format4Output(compensateExp.getRescueRealFee()),
									MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
									MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100),
									MoneyFormator.format4Output(deductFee));
							compensateExp.setSumRealPayText(sumRealPayText);
						}
					}else{
						// 规则15。

						compensateExp.setSumRealPay(( compensateExp.getDamageAmount()+compensateExp.getRescueRealFee() )*compensateExp
								.getDeductibleRate());
						double deductFee = this.makeDeductFee(compensateExp);
						compensateExp.setSumRealPay(compensateExp.getSumRealPay()-deductFee);
						compensateExp.setMsumRealPay(( compensateExp.getDamageAmount()+compensateExp.getRescueRealFee() )*compensateExp
								.getExceptDeductRate());
						compensateExp.setSumRealPayLoss(compensateExp.getDamageAmount()*compensateExp.getDeductibleRate()-deductFee);
						compensateExp.setSumRealPayRescue(compensateExp.getRescueRealFee()*compensateExp.getDeductibleRate());
						compensateExp.setMsumRealPayLoss(compensateExp.getDamageAmount()*compensateExp.getExceptDeductRate());
						compensateExp.setMsumRealPayRescue(compensateExp.getRescueRealFee()*compensateExp.getExceptDeductRate());
						String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText8.value,
								MoneyFormator.format4Output(compensateExp.getDamageAmount()),
								MoneyFormator.format4Output(compensateExp.getRescueRealFee()),
								MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
								MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100),MoneyFormator.format4Output(deductFee));
						logger.info("sumRealPayText = "+sumRealPayText);
						compensateExp.setSumRealPayText(sumRealPayText);
					}
				}else{
					if(compensateExp.getSumRealLoss()<=compensateExp.getDamageAmount()){
						// 规则16。

						compensateExp.setSumRealPay(( compensateExp.getSumRealLoss()+compensateExp.getDamageAmount() )*compensateExp
								.getDeductibleRate());
						double deductFee = this.makeDeductFee(compensateExp);
						compensateExp.setSumRealPay(compensateExp.getSumRealPay()-deductFee);
						compensateExp.setMsumRealPay(( compensateExp.getSumRealLoss()+compensateExp.getDamageAmount() )*compensateExp
								.getExceptDeductRate());
						compensateExp.setSumRealPayLoss(compensateExp.getSumRealLoss()*compensateExp.getDeductibleRate()-deductFee);
						compensateExp.setSumRealPayRescue(compensateExp.getDamageAmount()*compensateExp.getDeductibleRate());
						compensateExp.setMsumRealPayLoss(compensateExp.getSumRealLoss()*compensateExp.getExceptDeductRate());
						compensateExp.setMsumRealPayRescue(compensateExp.getDamageAmount()*compensateExp.getExceptDeductRate());

						String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText9.value,
								MoneyFormator.format4Output(compensateExp.getSumRealLoss()),
								MoneyFormator.format4Output(compensateExp.getDamageAmount()),
								MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
								MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100),MoneyFormator.format4Output(deductFee));
						logger.info("sumRealPayText = "+sumRealPayText);
						compensateExp.setSumRealPayText(sumRealPayText);
					}else{
						// 规则17。

						compensateExp.setSumRealPay(( compensateExp.getDamageAmount()+compensateExp.getDamageAmount() )*compensateExp
								.getDeductibleRate());
						double deductFee = this.makeDeductFee(compensateExp);
						compensateExp.setSumRealPay(compensateExp.getSumRealPay()-deductFee);
						compensateExp.setMsumRealPay(( compensateExp.getDamageAmount()+compensateExp.getDamageAmount() )*compensateExp
								.getExceptDeductRate());
						compensateExp.setSumRealPayLoss(compensateExp.getDamageAmount()*compensateExp.getDeductibleRate()-deductFee);
						compensateExp.setSumRealPayRescue(compensateExp.getDamageAmount()*compensateExp.getDeductibleRate());
						compensateExp.setMsumRealPayLoss(compensateExp.getDamageAmount()*compensateExp.getExceptDeductRate());
						compensateExp.setMsumRealPayRescue(compensateExp.getDamageAmount()*compensateExp.getExceptDeductRate());
						String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText10.value,
								MoneyFormator.format4Output(compensateExp.getDamageAmount()),
								MoneyFormator.format4Output(compensateExp.getDamageAmount()),
								MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
								MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100),MoneyFormator.format4Output(deductFee));
						logger.info("sumRealPayText = "+sumRealPayText);
						compensateExp.setSumRealPayText(sumRealPayText);
					}
				}
			}
		}

	}

	
	private void calculatKindCodeA1(CompensateListVo compensateListVo,CompensateExp compensateExp) {

		// if("1".equals(compensateExp.getNoFindThirdFlag())){
		// // calculatNoFondThirdKindCodeA(compensateListVo,compensateExp,cprcCase);
		//
		// }else{
		// 非代位判断开始 施救费
		if(compensateExp.getRescueFee()<compensateExp.getAmount()){
			compensateExp.setRescueRealFee(compensateExp.getRescueFee()-compensateExp.getRescueFeeBZPaid());
			String rescueFeeText = String.format(CodeConstants.CompensateText.rescueFeeText_A1.value,
					MoneyFormator.format4Output(compensateExp.getRescueFee()),MoneyFormator.format4Output(compensateExp.getRescueFeeBZPaid()));
			logger.info("rescueFeeText = "+rescueFeeText);
			compensateExp.setRescueFeeText(rescueFeeText);
		}else{
			compensateExp.setRescueRealFee(compensateExp.getAmount()-compensateExp.getRescueFeeBZPaid());
			String rescueFeeText = String.format(CodeConstants.CompensateText.rescueFeeText_A1_over.value,
					MoneyFormator.format4Output(compensateExp.getAmount()),MoneyFormator.format4Output(compensateExp.getRescueFeeBZPaid()));
			logger.info("rescueFeeText = "+rescueFeeText);
			compensateExp.setRescueFeeText(rescueFeeText);
		}

		// 赔款
		// double sumLoss = compensateExp.getSumLoss() - compensateExp.getOtherDeductFee();
		if(compensateExp.getSumLoss()<compensateExp.getAmount()){ // 损失金额 < 险别保额
			logger.info("损失金额 < 险别保额");
			compensateExp.setSumRealLoss(compensateExp.getSumLoss()-compensateExp.getSumLossBZPaid());
			String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossTextBelowDamageItemRealCost4ZF_CPRC_A1.value,
					MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getSumLossBZPaid()));
			logger.info("sumRealLossText = "+sumRealLossText);
			compensateExp.setSumRealLossText(sumRealLossText);
		}else{ // 损失金额 => 险别保额
			logger.info("损失金额 => 险别保额");
			compensateExp.setSumRealLoss(compensateExp.getAmount()-compensateExp.getSumLossBZPaid());
			String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossTextBelowDamageItemRealCostOVER4ZF_CPRC_A1.value,
					MoneyFormator.format4Output(compensateExp.getAmount()),MoneyFormator.format4Output(compensateExp.getSumLossBZPaid()));
			logger.info("sumRealLossText = "+sumRealLossText);
			compensateExp.setSumRealLossText(sumRealLossText);
		}
		// } // 非代位判断结束
		// 车损险代赔案件需要扣减追偿金额。
		// else{
		// calculatKindCodeADW(compensateListVo,compensateExp,cprcCase);
		// }

		// 计算约定绝对免赔率
		double dutyDeductibleRate = 0;
		List<PrpLCItemKindVo> itemKindVoList = registQueryService.findItemKindVo(compensateListVo.getRegistNo(),CodeConstants.KINDCODE.KINDCODE_M3);
		if(itemKindVoList!=null&&itemKindVoList.size()>0){
			for(PrpLCItemKindVo itemKindVo:itemKindVoList){
				if(itemKindVo.getValue()!=null){
					dutyDeductibleRate = Double.valueOf(itemKindVo.getValue().divide(new BigDecimal(100),2,RoundingMode.HALF_UP).toString());
				}
			}
		}

		// 获取理算金额。
		compensateExp
				.setSumRealPay(( compensateExp.getSumRealLoss()+compensateExp.getRescueRealFee()-compensateExp.getAbsolvePayAmt() )*compensateExp
						.getIndemnityDutyRate()*( 1-dutyDeductibleRate ));

		compensateExp.setMsumRealPay(( compensateExp.getSumRealLoss()+compensateExp.getRescueRealFee() )*compensateExp.getExceptDeductRate());
		compensateExp.setSumRealPayLoss(compensateExp.getSumRealLoss()*( 1-dutyDeductibleRate ));
		compensateExp.setSumRealPayRescue(compensateExp.getRescueRealFee()*( 1-dutyDeductibleRate ));
		compensateExp.setMsumRealPayLoss(compensateExp.getSumRealLoss()*compensateExp.getExceptDeductRate());
		compensateExp.setMsumRealPayRescue(compensateExp.getRescueRealFee()*compensateExp.getExceptDeductRate());

		String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText24.value,
				MoneyFormator.format4Output(compensateExp.getSumRealLoss()),MoneyFormator.format4RateOutput(compensateExp.getRescueRealFee()),
				MoneyFormator.format4RateOutput(compensateExp.getAbsolvePayAmt()),
				MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100),MoneyFormator.format4RateOutput(dutyDeductibleRate*100));
		logger.info("sumRealPayText = "+sumRealPayText);
		compensateExp.setSumRealPayText(sumRealPayText);
		// }
	}

	private void calculatKindCodeW1(CompensateListVo compensateListVo,CompensateExp compensateExp) {
		// W1险默认核定金额是0
		compensateExp.setSumRealPay(0);
	}

	/**
	 * 无法找到第三方 车损险公式 无法找到第三方车损险计算公式=min（定损金额，保险金额）*（1-无法找到第三方的免赔率-加扣免赔率之和） 如果保了无法找到第三方特约=无法找到第三方车损险计算公式=min（定损金额，保险金额）*无法找到第三方免赔率
	 * @param compensateListVo
	 * @param compensateExp
	 * @param cprcCase
	 * @modified: ☆YangKun(2016年6月13日 下午5:42:24): <br>
	 */
	private void calculatNoFondThirdKindCodeA(CompensateListVo compensateListVo,CompensateExp compensateExp,Boolean cprcCase) {
		if(cprcCase){
			// 施救费
			if(compensateExp.getRescueFee()<compensateExp.getAmount()){
				compensateExp.setRescueRealFee(( compensateExp.getRescueFee()-compensateExp.getRescueFeeBZPaid() ));
				String rescueFeeText = String.format(CodeConstants.CompensateText.rescueFeeText_NT.value,
						MoneyFormator.format4Output(compensateExp.getRescueFee()),MoneyFormator.format4Output(compensateExp.getRescueFeeBZPaid()));
				logger.info("rescueFeeText = "+rescueFeeText);
				compensateExp.setRescueFeeText(rescueFeeText);
			}else{
				compensateExp.setRescueRealFee(( compensateExp.getAmount()-compensateExp.getRescueFeeBZPaid() ));
				String rescueFeeText = String.format(CodeConstants.CompensateText.rescueFeeText_overNT.value,
						MoneyFormator.format4Output(compensateExp.getAmount()),MoneyFormator.format4Output(compensateExp.getRescueFeeBZPaid()));
				logger.info("rescueFeeText = "+rescueFeeText);
				compensateExp.setRescueFeeText(rescueFeeText);
			}

			// 赔款
			if(compensateExp.getSumLoss()-compensateExp.getOtherDeductFee()<compensateExp.getAmount()){
				logger.info("损失金额 < 险别保额");
				compensateExp.setSumRealLoss(( compensateExp.getSumLoss()-compensateExp.getSumLossBZPaid()-compensateExp.getOtherDeductFee() ));

				String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossTextZF_NTCPRC.value,
						MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getSumLossBZPaid()),
						MoneyFormator.format4Output(compensateExp.getOtherDeductFee()));
				logger.info("sumRealLossText = "+sumRealLossText);
				compensateExp.setSumRealLossText(sumRealLossText);
			}else{ // 损失金额 => 险别保额
				logger.info("损失金额 => 险别保额");
				compensateExp.setSumRealLoss(( compensateExp.getAmount()-compensateExp.getSumLossBZPaid() ));
				String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossOverTextZF_NTCPRC.value,
						MoneyFormator.format4Output(compensateExp.getAmount()),MoneyFormator.format4Output(compensateExp.getSumLossBZPaid()));
				logger.info("sumRealLossText = "+sumRealLossText);
				compensateExp.setSumRealLossText(sumRealLossText);
			}
		}else{
			if(compensateExp.getDamageAmount()<compensateExp.getEntryItemCarCost()){
				compensateExp
						.setRescueRealFee(( compensateExp.getRescueFee()-compensateExp.getRescueFeeBZPaid() )*( compensateExp.getAmount()/compensateExp
								.getEntryItemCarCost() ));
				String rescueFeeText = String.format(CodeConstants.CompensateText.rescueFeeText_unfull_NT.value,
						MoneyFormator.format4Output(compensateExp.getRescueFee()),MoneyFormator.format4Output(compensateExp.getRescueFeeBZPaid()),
						MoneyFormator.format4Output(compensateExp.getAmount()),MoneyFormator.format4Output(compensateExp.getEntryItemCarCost()));
				logger.info("rescueFeeText = "+rescueFeeText);
				compensateExp.setRescueFeeText(rescueFeeText);
			}else{
				compensateExp.setRescueRealFee(( compensateExp.getRescueFee()-compensateExp.getRescueFeeBZPaid() ));
				String rescueFeeText = String.format(CodeConstants.CompensateText.rescueFeeText_NT.value,
						MoneyFormator.format4Output(compensateExp.getRescueFee()),MoneyFormator.format4Output(compensateExp.getRescueFeeBZPaid()));
				logger.info("rescueFeeText = "+rescueFeeText);
				compensateExp.setRescueFeeText(rescueFeeText);
			}

			// 赔款
			if(compensateExp.getSumLoss()-compensateExp.getOtherDeductFee()<compensateExp.getDamageItemRealCost()){
				logger.info("损失金额 < 实际价值。按损失金额赔付");
				compensateExp
						.setSumRealLoss(( compensateExp.getSumLoss()-compensateExp.getSumLossBZPaid()-compensateExp.getOtherDeductFee() )*( compensateExp
								.getAmount()/compensateExp.getEntryItemCarCost() ));
				String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossTextBelowDamageItemRealCost4ZF_NT.value,
						MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getSumLossBZPaid()),
						MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),MoneyFormator.format4Output(compensateExp.getAmount()),
						MoneyFormator.format4Output(compensateExp.getEntryItemCarCost()));
				logger.info("sumRealLossText = "+sumRealLossText);
				compensateExp.setSumRealLossText(sumRealLossText);
			}else{
				logger.info("按实际价值赔付。");

				compensateExp.setSumRealLoss(( compensateExp.getDamageItemRealCost()-compensateExp.getSumLossBZPaid() ));
				String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossTextOverCost4ZF_NT.value,
						MoneyFormator.format4Output(compensateExp.getDamageItemRealCost()),
						MoneyFormator.format4Output(compensateExp.getSumLossBZPaid()));
				logger.info("sumRealLossText = "+sumRealLossText);
				compensateExp.setSumRealLossText(sumRealLossText);
			}

		}

		// if("1".equals(compensateExp.getKindCodeNTFlag())){//承保了 无法找到第三方特约
		// 判断施救费。
		if(compensateExp.getRescueRealFee()<=compensateExp.getDamageAmount()){

			if(compensateExp.getSumRealLoss()<=compensateExp.getDamageAmount()){// 新条款没承保 无法第三方特约和旧条款都小于保额 公式一样
				compensateExp.setSumRealPay(( compensateExp.getSumRealLoss()+compensateExp.getRescueRealFee() )*( 1-compensateExp
						.getSelectDeductibleRate() )-compensateExp.getDeductFee());
				compensateExp.setMsumRealPay(0);
				compensateExp.setSumRealPayLoss(compensateExp.getSumRealLoss()*( 1-compensateExp.getSelectDeductibleRate() ));
				compensateExp.setSumRealPayRescue(compensateExp.getRescueRealFee()*( 1-compensateExp.getSelectDeductibleRate() ));
				compensateExp.setMsumRealPayLoss(0);
				compensateExp.setMsumRealPayRescue(0);
				String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayTextA_NT.value,
						MoneyFormator.format4Output(compensateExp.getSumRealLoss()),MoneyFormator.format4Output(compensateExp.getRescueRealFee()),
						MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100),
						MoneyFormator.format4Output(compensateExp.getDeductFee()));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);

			}else{
				compensateExp.setSumRealPay(( compensateExp.getDamageAmount()+compensateExp.getRescueRealFee() )*( 1-compensateExp
						.getSelectDeductibleRate() )-compensateExp.getDeductFee());
				compensateExp.setMsumRealPay(0);
				compensateExp.setSumRealPayLoss(compensateExp.getDamageAmount()*( 1-compensateExp.getSelectDeductibleRate() ));
				compensateExp.setSumRealPayRescue(compensateExp.getRescueRealFee()*( 1-compensateExp.getSelectDeductibleRate() ));
				compensateExp.setMsumRealPayLoss(0);
				compensateExp.setMsumRealPayRescue(0);
				String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayOverTextA_NT1.value,
						MoneyFormator.format4Output(compensateExp.getDamageAmount()),MoneyFormator.format4Output(compensateExp.getRescueRealFee()),
						MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100),
						MoneyFormator.format4Output(compensateExp.getDeductFee()));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);
			}
		}else{
			if(compensateExp.getSumRealLoss()<=compensateExp.getDamageAmount()){
				compensateExp.setSumRealPay(( compensateExp.getSumRealLoss()+compensateExp.getDamageAmount() )*( 1-compensateExp
						.getSelectDeductibleRate() )-compensateExp.getDeductFee());
				compensateExp.setMsumRealPay(0);
				compensateExp.setSumRealPayLoss(compensateExp.getSumRealLoss()*( 1-compensateExp.getSelectDeductibleRate() ));
				compensateExp.setSumRealPayRescue(compensateExp.getDamageAmount()*( 1-compensateExp.getSelectDeductibleRate() ));
				compensateExp.setMsumRealPayLoss(0);
				compensateExp.setMsumRealPayRescue(0);
				String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayOverTextA_NT2.value,
						MoneyFormator.format4Output(compensateExp.getSumRealLoss()),MoneyFormator.format4Output(compensateExp.getDamageAmount()),
						MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100),
						MoneyFormator.format4Output(compensateExp.getDeductFee()));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);
			}else{
				compensateExp.setSumRealPay(( compensateExp.getDamageAmount()+compensateExp.getDamageAmount() )*( 1-compensateExp
						.getSelectDeductibleRate() )-compensateExp.getDeductFee());
				compensateExp.setMsumRealPay(0);
				compensateExp.setSumRealPayLoss(compensateExp.getDamageAmount()*( 1-compensateExp.getSelectDeductibleRate() ));
				compensateExp.setSumRealPayRescue(compensateExp.getDamageAmount()*( 1-compensateExp.getSelectDeductibleRate() ));
				compensateExp.setMsumRealPayLoss(0);
				compensateExp.setMsumRealPayRescue(0);
				String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayOverTextA_NT3.value,
						MoneyFormator.format4Output(compensateExp.getDamageAmount()),MoneyFormator.format4Output(compensateExp.getDamageAmount()),
						MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100),
						MoneyFormator.format4Output(compensateExp.getDeductFee()));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);
			}
		}
	}

	private void calculatKindCodeADW(CompensateListVo compensateListVo,CompensateExp compensateExp,Boolean cprcCase) {

		if( !false){
			if(compensateExp.getSumLoss()<compensateExp.getDamageItemRealCost()){ // 损失金额 < 实际价值

				// 追交强金额之和[按定损施救占比分配]。
				double part4SumRealLoss = ( compensateListVo.getSubrogationBzRealPay()*( compensateExp.getSumLoss()-compensateExp.getSumRest() )/( compensateExp
						.getSumLoss()-compensateExp.getSumRest()+compensateExp.getRescueFee() ) );

				double part4RescueRealFee = ( compensateListVo.getSubrogationBzRealPay()*compensateExp.getRescueFee()/( compensateExp.getSumLoss()-compensateExp
						.getSumRest()+compensateExp.getRescueFee() ) );

				/*
				   情况            是否减追交强金额之和      是否减交强已赔付金额
				   全责有交强理算           否                   是
				   全责无交强理算           是                   否
				   无责有交强理算           是                   是
				   无责无交强理算           否                   否
				 */

				String registNo = ( (PrpLLossItemVo)compensateExp.getLossItem() ).getRegistNo();
				List<PrpLCompensateVo> prpLCompensateVoList = this.findPrpLcompensateByRegistNo(registNo);

				boolean isExistCTPL = false;
				boolean isFullDuty = false;

				for(PrpLCompensateVo prpLCompensateVo:prpLCompensateVoList){
					if("2".equalsIgnoreCase(prpLCompensateVo.getCompensateKind())&&"1".equalsIgnoreCase(prpLCompensateVo.getUnderwriteFlag()))
						isExistCTPL = true;
				}

				PrpLCheckDutyVo prpLCheckDutyVo = checkTaskService.findCheckDuty(registNo,1);
				if("1".equals(prpLCheckDutyVo.getCiDutyFlag())&&new BigDecimal(100).equals(prpLCheckDutyVo.getIndemnityDutyRate())){
					isFullDuty = true;
				}

				if(isFullDuty&& !isExistCTPL)
					compensateExp.setSumRealLoss(( compensateExp.getSumLoss()-compensateExp.getSumRest()-part4SumRealLoss-compensateExp
							.getOtherDeductFee() )*compensateExp.getIndemnityDutyRate());
				else if(isFullDuty&&isExistCTPL)
					compensateExp.setSumRealLoss(( compensateExp.getSumLoss()-compensateExp.getSumRest()-compensateExp.getbZPaid()-compensateExp
							.getOtherDeductFee() )*compensateExp.getIndemnityDutyRate());
				else if( !isFullDuty&& !isExistCTPL)
					compensateExp
							.setSumRealLoss(( compensateExp.getSumLoss()-compensateExp.getSumRest()-compensateExp.getOtherDeductFee() )*compensateExp
									.getIndemnityDutyRate());
				else
					compensateExp
							.setSumRealLoss(( compensateExp.getSumLoss()-compensateExp.getSumRest()-part4SumRealLoss-compensateExp.getbZPaid()-compensateExp
									.getOtherDeductFee() )*compensateExp.getIndemnityDutyRate());

				String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealPayText4_CPRC.value,MoneyFormator
						.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getSumRest()),MoneyFormator
						.format4Output(( ( isFullDuty&&isExistCTPL )||( !isFullDuty&& !isExistCTPL ) ) ? 0 : part4SumRealLoss),MoneyFormator
						.format4Output(( ( isFullDuty&& !isExistCTPL )||( !isFullDuty&& !isExistCTPL ) ) ? 0 : compensateExp.getbZPaid()),
						MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),MoneyFormator.format4RateOutput(compensateExp
								.getIndemnityDutyRate()));
				logger.info("sumRealLossText = "+sumRealLossText);
				compensateExp.setSumRealLossText(sumRealLossText);

				if(compensateExp.getDamageAmount()<compensateExp.getEntryItemCarCost()){
					compensateExp
							.setRescueRealFee(( compensateExp.getRescueFee()-part4RescueRealFee )*( compensateExp.getDamageAmount()/compensateExp
									.getEntryItemCarCost() )*compensateExp.getIndemnityDutyRate());

					String rescueFeeText = String.format(CodeConstants.CompensateText.rescueFeeText1_unfull_CPRC.value,
							MoneyFormator.format4Output(compensateExp.getRescueFee()),MoneyFormator.format4Output(part4RescueRealFee),
							MoneyFormator.format4Output(compensateExp.getDamageAmount()),
							MoneyFormator.format4Output(compensateExp.getEntryItemCarCost()),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()));
					logger.info("rescueFeeText = "+rescueFeeText);
					compensateExp.setRescueFeeText(rescueFeeText);
				}else{
					compensateExp.setRescueRealFee(( compensateExp.getRescueFee()-part4RescueRealFee )*compensateExp.getIndemnityDutyRate());

					String rescueFeeText = String.format(CodeConstants.CompensateText.rescueFeeText1.value,
							MoneyFormator.format4Output(compensateExp.getRescueFee()),MoneyFormator.format4Output(part4RescueRealFee),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()));
					logger.info("rescueFeeText = "+rescueFeeText);
					compensateExp.setRescueFeeText(rescueFeeText);
				}

			}else{

				// 追交强金额之和[按定损施救占比分配]。
				double part4SumRealLoss = ( compensateListVo.getSubrogationBzRealPay()*( compensateExp.getDamageItemRealCost()-compensateExp
						.getSumRest() )/( compensateExp.getDamageItemRealCost()-compensateExp.getSumRest()+compensateExp.getRescueFee() ) );
				double part4RescueRealFee = ( compensateListVo.getSubrogationBzRealPay()*compensateExp.getRescueFee()/( compensateExp
						.getDamageItemRealCost()-compensateExp.getSumRest()+compensateExp.getRescueFee() ) );

				compensateExp.setSumRealLoss(( compensateExp.getDamageItemRealCost()-compensateExp.getSumRest()-part4SumRealLoss-compensateExp
						.getOtherDeductFee() )*compensateExp.getIndemnityDutyRate());

				String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealPayText5.value,
						MoneyFormator.format4Output(compensateExp.getDamageItemRealCost()),MoneyFormator.format4Output(compensateExp.getSumRest()),
						MoneyFormator.format4Output(part4SumRealLoss),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
						MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()));

				if(compensateExp.getDamageAmount()<compensateExp.getEntryItemCarCost()){
					compensateExp
							.setRescueRealFee(( compensateExp.getRescueFee()-part4RescueRealFee )*compensateExp.getIndemnityDutyRate()*( compensateExp
									.getDamageAmount()/compensateExp.getEntryItemCarCost() ));
					String rescueFeeText = String.format(CodeConstants.CompensateText.rescueFeeText1_unfull_CPRC.value,
							MoneyFormator.format4Output(compensateExp.getRescueFee()),MoneyFormator.format4Output(part4RescueRealFee),
							MoneyFormator.format4Output(compensateExp.getDamageAmount()),
							MoneyFormator.format4Output(compensateExp.getEntryItemCarCost()),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()));

					logger.info("rescueFeeText = "+rescueFeeText);
					compensateExp.setRescueFeeText(rescueFeeText);

					logger.info("sumRealLossText = "+sumRealLossText);
					compensateExp.setSumRealLossText(sumRealLossText);
				}else{
					compensateExp
							.setRescueRealFee(( compensateExp.getRescueFee()-part4RescueRealFee )*compensateExp.getIndemnityDutyRate()*compensateExp
									.getDamageItemRealCost()/compensateExp.getDamageItemRealCost());
					String rescueFeeText = String.format(CodeConstants.CompensateText.rescueFeeText1.value,
							MoneyFormator.format4Output(compensateExp.getRescueFee()),MoneyFormator.format4Output(part4RescueRealFee),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()),
							MoneyFormator.format4Output(compensateExp.getDamageItemRealCost()),
							MoneyFormator.format4Output(compensateExp.getDamageItemRealCost()));
					logger.info("rescueFeeText = "+rescueFeeText);
					compensateExp.setRescueFeeText(rescueFeeText);

					logger.info("sumRealLossText = "+sumRealLossText);
					compensateExp.setSumRealLossText(sumRealLossText);
				}
			}
		}else{ // 原代位理算代码开始
			if(compensateExp.getSumLoss()<compensateExp.getDamageItemRealCost()){ // 损失金额 < 实际价值

				// 追交强金额之和[按定损施救占比分配]。
				double part4SumRealLoss = ( compensateListVo.getSubrogationBzRealPay()*( compensateExp.getSumLoss()-compensateExp.getSumRest() )/( compensateExp
						.getSumLoss()-compensateExp.getSumRest()+compensateExp.getRescueFee() ) );

				double part4RescueRealFee = ( compensateListVo.getSubrogationBzRealPay()*compensateExp.getRescueFee()/( compensateExp.getSumLoss()-compensateExp
						.getSumRest()+compensateExp.getRescueFee() ) );

				compensateExp
						.setSumRealLoss(( compensateExp.getSumLoss()-compensateExp.getSumRest()-part4SumRealLoss-compensateExp.getOtherDeductFee() )*compensateExp
								.getIndemnityDutyRate()*( compensateExp.getAmount()/compensateExp.getEntryItemCarCost() ));

				String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealPayText4.value,
						MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getSumRest()),
						MoneyFormator.format4Output(part4SumRealLoss),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
						MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()),MoneyFormator.format4Output(compensateExp.getAmount()),
						MoneyFormator.format4Output(compensateExp.getEntryItemCarCost()));
				logger.info("sumRealLossText = "+sumRealLossText);
				compensateExp.setSumRealLossText(sumRealLossText);

				if(compensateExp.getDamageAmount()<compensateExp.getEntryItemCarCost()){
					compensateExp
							.setRescueRealFee(( compensateExp.getRescueFee()-part4RescueRealFee )*( compensateExp.getDamageAmount()/compensateExp
									.getEntryItemCarCost() )*compensateExp.getIndemnityDutyRate()*( compensateExp.getDamageItemRealCost()/compensateExp
									.getDamageItemRealCost() ));

					String rescueFeeText = String.format(CodeConstants.CompensateText.rescueFeeText1_unfull.value,
							MoneyFormator.format4Output(compensateExp.getRescueFee()),MoneyFormator.format4Output(part4RescueRealFee),
							MoneyFormator.format4Output(compensateExp.getDamageAmount()),
							MoneyFormator.format4Output(compensateExp.getEntryItemCarCost()),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()),
							MoneyFormator.format4Output(compensateExp.getDamageItemRealCost()),
							MoneyFormator.format4Output(compensateExp.getDamageItemRealCost()));
					logger.info("rescueFeeText = "+rescueFeeText);
					compensateExp.setRescueFeeText(rescueFeeText);
				}else{
					compensateExp.setRescueRealFee(( compensateExp.getRescueFee()-part4RescueRealFee )*compensateExp.getIndemnityDutyRate());

					String rescueFeeText = String.format(CodeConstants.CompensateText.rescueFeeText1.value,
							MoneyFormator.format4Output(compensateExp.getRescueFee()),MoneyFormator.format4Output(part4RescueRealFee),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()));
					logger.info("rescueFeeText = "+rescueFeeText);
					compensateExp.setRescueFeeText(rescueFeeText);
				}

			}else{

				// 追交强金额之和[按定损施救占比分配]。
				double part4SumRealLoss = ( compensateListVo.getSubrogationBzRealPay()*( compensateExp.getDamageItemRealCost()-compensateExp
						.getSumRest() )/( compensateExp.getDamageItemRealCost()-compensateExp.getSumRest()+compensateExp.getRescueFee() ) );
				double part4RescueRealFee = ( compensateListVo.getSubrogationBzRealPay()*compensateExp.getRescueFee()/( compensateExp
						.getDamageItemRealCost()-compensateExp.getSumRest()+compensateExp.getRescueFee() ) );

				compensateExp.setSumRealLoss(( compensateExp.getDamageItemRealCost()-compensateExp.getSumRest()-part4SumRealLoss-compensateExp
						.getOtherDeductFee() )*compensateExp.getIndemnityDutyRate());

				String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealPayText5.value,
						MoneyFormator.format4Output(compensateExp.getDamageItemRealCost()),MoneyFormator.format4Output(compensateExp.getSumRest()),
						MoneyFormator.format4Output(part4SumRealLoss),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
						MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()));

				if(compensateExp.getDamageAmount()<compensateExp.getEntryItemCarCost()){
					compensateExp
							.setRescueRealFee(( compensateExp.getRescueFee()-part4RescueRealFee )*compensateExp.getIndemnityDutyRate()*( compensateExp
									.getDamageAmount()/compensateExp.getEntryItemCarCost() )*compensateExp.getDamageItemRealCost()/compensateExp
									.getDamageItemRealCost());
					String rescueFeeText = String.format(CodeConstants.CompensateText.rescueFeeText1_unfull.value,
							MoneyFormator.format4Output(compensateExp.getRescueFee()),MoneyFormator.format4Output(part4RescueRealFee),
							MoneyFormator.format4Output(compensateExp.getDamageAmount()),
							MoneyFormator.format4Output(compensateExp.getEntryItemCarCost()),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()),
							MoneyFormator.format4Output(compensateExp.getDamageItemRealCost()),
							MoneyFormator.format4Output(compensateExp.getDamageItemRealCost()));

					logger.info("rescueFeeText = "+rescueFeeText);
					compensateExp.setRescueFeeText(rescueFeeText);

					logger.info("sumRealLossText = "+sumRealLossText);
					compensateExp.setSumRealLossText(sumRealLossText);
				}else{
					compensateExp
							.setRescueRealFee(( compensateExp.getRescueFee()-part4RescueRealFee )*compensateExp.getIndemnityDutyRate()*compensateExp
									.getDamageItemRealCost()/compensateExp.getDamageItemRealCost());
					String rescueFeeText = String.format(CodeConstants.CompensateText.rescueFeeText1.value,
							MoneyFormator.format4Output(compensateExp.getRescueFee()),MoneyFormator.format4Output(part4RescueRealFee),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()),
							MoneyFormator.format4Output(compensateExp.getDamageItemRealCost()),
							MoneyFormator.format4Output(compensateExp.getDamageItemRealCost()));

					logger.info("rescueFeeText = "+rescueFeeText);
					compensateExp.setRescueFeeText(rescueFeeText);

					logger.info("sumRealLossText = "+sumRealLossText);
					compensateExp.setSumRealLossText(sumRealLossText);
				}
			}
		} // 原代位理算结束

		// 判断施救费。
		if(compensateExp.getRescueRealFee()<=compensateExp.getDamageAmount()){
			if(compensateExp.getSumRealLoss()>compensateExp.getDamageAmount()){
				// 规则21。
				compensateExp.setSumRealPay(( compensateExp.getDamageAmount()+compensateExp.getRescueRealFee() )*compensateExp.getDeductibleRate());
				double deductFee = this.makeDeductFee(compensateExp);
				compensateExp.setSumRealPay(compensateExp.getSumRealPay()-deductFee-compensateExp.getSumPrePay());
				compensateExp
						.setMsumRealPay(( compensateExp.getDamageAmount()+compensateExp.getRescueRealFee() )*compensateExp.getExceptDeductRate());
				compensateExp.setSumRealPayLoss(compensateExp.getDamageAmount()*compensateExp.getDeductibleRate()-deductFee-compensateExp
						.getSumPrePay());
				compensateExp.setSumRealPayRescue(compensateExp.getRescueRealFee()*compensateExp.getDeductibleRate());
				compensateExp.setMsumRealPayLoss(compensateExp.getDamageAmount()*compensateExp.getExceptDeductRate());
				compensateExp.setMsumRealPayRescue(compensateExp.getRescueRealFee()*compensateExp.getExceptDeductRate());
				String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText8.value,
						compensateExp.getDeductibleRateText()+MoneyFormator.format4Output(compensateExp.getDamageAmount()),
						MoneyFormator.format4Output(compensateExp.getRescueRealFee()),MoneyFormator.format4Output(deductFee),
						MoneyFormator.format4Output(compensateExp.getSumPrePay()));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);

			}else{
				// 计算免赔额。
				double deductibleAmount = ( compensateExp.getSumRealLoss()+compensateExp.getRescueRealFee() )*compensateExp.getDeductibleRate();
				if(deductibleAmount>compensateExp.getDeductFee()){
					// 规则18。
					compensateExp
							.setSumRealPay(( compensateExp.getSumRealLoss()+compensateExp.getRescueRealFee() )*compensateExp.getDeductibleRate());
					double deductFee = this.makeDeductFee(compensateExp);
					compensateExp
							.setSumRealPay(compensateExp.getSumRealPay()-deductFee-compensateExp.getSumPrePay()-compensateExp.getAbsolvePayAmt());

					compensateExp.setMsumRealPay(( compensateExp.getSumRealLoss()+compensateExp.getRescueRealFee() )*compensateExp
							.getExceptDeductRate());

					compensateExp.setSumRealPayLoss(compensateExp.getSumRealLoss()*compensateExp.getDeductibleRate()-deductFee-compensateExp
							.getSumPrePay()-compensateExp.getAbsolvePayAmt());
					compensateExp.setSumRealPayRescue(compensateExp.getRescueRealFee()*compensateExp.getDeductibleRate());
					compensateExp.setMsumRealPayLoss(compensateExp.getSumRealLoss()*compensateExp.getExceptDeductRate());
					compensateExp.setMsumRealPayRescue(compensateExp.getRescueRealFee()*compensateExp.getExceptDeductRate());
					String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText6.value,
							MoneyFormator.format4Output(compensateExp.getSumRealLoss()),
							MoneyFormator.format4Output(compensateExp.getRescueRealFee()),
							MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
							MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100),MoneyFormator.format4Output(deductFee),
							MoneyFormator.format4Output(compensateExp.getSumPrePay()),MoneyFormator.format4Output(compensateExp.getAbsolvePayAmt()));
					logger.info("sumRealPayText = "+sumRealPayText);
					compensateExp.setSumRealPayText(sumRealPayText);

				}else{
					if(deductibleAmount<0d){

						// 规则19。
						compensateExp.setSumRealPay(( compensateExp.getSumRealLoss()+compensateExp.getRescueRealFee() )*compensateExp
								.getDeductibleRate());
						double deductFee = this.makeDeductFee(compensateExp);
						compensateExp.setSumRealPay(compensateExp.getSumRealPay()-deductFee-compensateExp.getSumPrePay()-compensateExp
								.getAbsolvePayAmt());

						compensateExp.setMsumRealPay(( compensateExp.getSumRealLoss()+compensateExp.getRescueRealFee() )*compensateExp
								.getExceptDeductRate());
						compensateExp.setSumRealPayLoss(compensateExp.getSumRealLoss()*compensateExp.getDeductibleRate()-deductFee-compensateExp
								.getSumPrePay()-compensateExp.getAbsolvePayAmt());
						compensateExp.setSumRealPayRescue(compensateExp.getRescueRealFee()*compensateExp.getDeductibleRate());
						compensateExp.setMsumRealPayLoss(compensateExp.getSumRealLoss()*compensateExp.getExceptDeductRate());
						compensateExp.setMsumRealPayRescue(compensateExp.getRescueRealFee()*compensateExp.getExceptDeductRate());

						String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText6.value,
								MoneyFormator.format4Output(compensateExp.getSumRealLoss()),
								MoneyFormator.format4Output(compensateExp.getRescueRealFee()),
								MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
								MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100),MoneyFormator.format4Output(deductFee),
								MoneyFormator.format4Output(compensateExp.getSumPrePay()),
								MoneyFormator.format4Output(compensateExp.getAbsolvePayAmt()));
						logger.info("sumRealPayText = "+sumRealPayText);
						compensateExp.setSumRealPayText(sumRealPayText);
					}else{
						// 规则20。

						compensateExp.setSumRealPay(0d);
						double deductFee = this.makeDeductFee(compensateExp);
						compensateExp.setSumRealPay(compensateExp.getSumRealPay()-deductFee);

						compensateExp.setMsumRealPay(0d);
						compensateExp.setSumRealPayLoss(compensateExp.getSumRealLoss()*compensateExp.getDeductibleRate()-deductFee);
						compensateExp.setSumRealPayRescue(compensateExp.getRescueRealFee()*compensateExp.getDeductibleRate());
						compensateExp.setMsumRealPayLoss(compensateExp.getSumRealLoss()*compensateExp.getExceptDeductRate());
						compensateExp.setMsumRealPayRescue(compensateExp.getRescueRealFee()*compensateExp.getExceptDeductRate());
						String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText7.value,
								compensateExp.getDeductibleRateText(),MoneyFormator.format4Output(compensateExp.getSumRealLoss()),
								MoneyFormator.format4Output(compensateExp.getRescueRealFee()),MoneyFormator.format4Output(deductFee));
						logger.info("sumRealPayText = "+sumRealPayText);
						compensateExp.setSumRealPayText(sumRealPayText);

					}
				}
			}
		}else{
			if(compensateExp.getSumRealLoss()<=compensateExp.getDamageAmount()){
				// 规则22。

				compensateExp.setSumRealPay(( compensateExp.getSumRealLoss()+compensateExp.getDamageAmount() )*compensateExp.getDeductibleRate());
				double deductFee = this.makeDeductFee(compensateExp);
				compensateExp.setSumRealPay(compensateExp.getSumRealPay()-deductFee-compensateExp.getSumPrePay());
				compensateExp.setMsumRealPay(( compensateExp.getSumRealLoss()+compensateExp.getDamageAmount() )*compensateExp.getExceptDeductRate());
				compensateExp.setSumRealPayLoss(compensateExp.getSumRealLoss()*compensateExp.getDeductibleRate()-deductFee-compensateExp
						.getSumPrePay());
				compensateExp.setSumRealPayRescue(compensateExp.getDamageAmount()*compensateExp.getDeductibleRate());
				compensateExp.setMsumRealPayLoss(compensateExp.getSumRealLoss()*compensateExp.getExceptDeductRate());
				compensateExp.setMsumRealPayRescue(compensateExp.getDamageAmount()*compensateExp.getExceptDeductRate());

				String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText9.value,
						compensateExp.getDeductibleRateText()+MoneyFormator.format4Output(compensateExp.getSumRealLoss()),
						MoneyFormator.format4Output(compensateExp.getDamageAmount()),MoneyFormator.format4Output(compensateExp.getDeductibleRate()),
						MoneyFormator.format4Output(deductFee),MoneyFormator.format4Output(compensateExp.getSumPrePay()));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);
			}else{
				// 规则23。

				compensateExp.setSumRealPay(( compensateExp.getDamageAmount()+compensateExp.getDamageAmount() )*compensateExp.getDeductibleRate());
				double deductFee = this.makeDeductFee(compensateExp);
				compensateExp.setSumRealPay(compensateExp.getSumRealPay()-deductFee-compensateExp.getSumPrePay());
				compensateExp.setMsumRealPay(( compensateExp.getDamageAmount()+compensateExp.getDamageAmount() )*compensateExp.getExceptDeductRate());
				compensateExp.setSumRealPayLoss(compensateExp.getDamageAmount()*compensateExp.getDeductibleRate()-deductFee-compensateExp
						.getSumPrePay());
				compensateExp.setSumRealPayRescue(compensateExp.getDamageAmount()*compensateExp.getDeductibleRate());
				compensateExp.setMsumRealPayLoss(compensateExp.getDamageAmount()*compensateExp.getExceptDeductRate());
				compensateExp.setMsumRealPayRescue(compensateExp.getDamageAmount()*compensateExp.getExceptDeductRate());
				String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText10.value,
						compensateExp.getDeductibleRateText()+MoneyFormator.format4Output(compensateExp.getDamageAmount()),
						MoneyFormator.format4Output(compensateExp.getDamageAmount()),MoneyFormator.format4Output(deductFee),
						MoneyFormator.format4Output(compensateExp.getSumPrePay()));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);
			}
		}

	}

	/**
	 * 新增设备险计算
	 * @param compensateListVo
	 * @param compensateExp
	 * @param cprcCase
	 * @modified: ☆YangKun(2016年5月26日 下午7:35:13): <br>
	 */
	private void calculatKindCodeX(CompensateListVo compensateListVo,CompensateExp compensateExp,Boolean cprcCase) {

		if("1".equals(compensateExp.getNoFindThirdFlag())){
			calculatNoFondThirdKindCodeX(compensateListVo,compensateExp,cprcCase);

		}else{
			String payHeadText = "";
			if(cprcCase){
				// 损失金额 <= 保额。
				if(compensateExp.getSumLoss()-compensateExp.getOtherDeductFee()<=compensateExp.getDamageAmount()){
					compensateExp
							.setSumRealLoss(( compensateExp.getSumLoss()-compensateExp.getSumLossBZPaid()-compensateExp.getOtherDeductFee() )*compensateExp
									.getIndemnityDutyRate());

					String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossTextBelowDamageItemRealCost4ZF_X.value,
							MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100));
					logger.info("sumRealLossText = "+sumRealLossText);
					compensateExp.setSumRealLossText(sumRealLossText);
					payHeadText = "赔付金额";

				}else{
					compensateExp.setSumRealLoss(( compensateExp.getDamageAmount() )*compensateExp.getIndemnityDutyRate());
					String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossTextOverRealCost4ZF_X_CPRC.value,
							MoneyFormator.format4Output(compensateExp.getDamageAmount()),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100));
					logger.info("sumRealLossText = "+sumRealLossText);
					compensateExp.setSumRealLossText(sumRealLossText);
					compensateExp.setItemFlag("1");
					payHeadText = "保险金额";
				}
				// 规则45。
				// 新增设备险实赔金额 = max（0，（新增设备赔款 × （1 - 事故责任免赔率）×（1 - 可选免赔率之和） - 免赔额）【可能为负数】）
				// 不计免赔　＝　新增设备险赔款　＊（１ - 可选免赔率之和）× 事故责任免赔率 +min（0，（新增设备险赔款 × （1 - 事故免赔率）×（1 - 可选免赔率之和） - 免赔额）
				double sumRealPay = compensateExp.getSumRealLoss()*compensateExp.getDeductibleRate();
				double realPaySubDeduct = sumRealPay-compensateExp.getDeductFee();// 实赔 - 免赔额 差额
				double msumRealPay = compensateExp.getSumRealLoss()*compensateExp.getExceptDeductRate();
				if(realPaySubDeduct>=0){ // 赔款大于免赔额
					sumRealPay = realPaySubDeduct;
					compensateExp.setSumRealPay(sumRealPay);
					compensateExp.setMsumRealPay(msumRealPay);

					String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText_X_CPRC.value,payHeadText,
							MoneyFormator.format4Output(compensateExp.getSumRealLoss()),
							MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
							MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100),
							MoneyFormator.format4Output(compensateExp.getDeductFee()));
					logger.info("sumRealPayText = "+sumRealPayText);
					compensateExp.setSumRealPayText(sumRealPayText);

				}else{
					sumRealPay = 0;
					msumRealPay = msumRealPay+realPaySubDeduct;
					compensateExp.setSumRealPay(sumRealPay);
					compensateExp.setMsumRealPay(msumRealPay);

					compensateExp.setSumRealPayText("\r\n    实赔小于免赔额，故 赔款  ");

				}
			}else{
				// double indemnityDutyLoss = ( compensateExp.getSumLoss()-compensateExp.getSumRest()-compensateExp.getSumLossBZPaid()-compensateExp
				// .getOtherDeductFee() )*compensateExp.getIndemnityDutyRate();
				// 损失金额 <= 实际价值。按损失金额赔付。
				if(compensateExp.getSumLoss()-compensateExp.getOtherDeductFee()<=compensateExp.getDeviceActualValue()){
					compensateExp
							.setSumRealLoss(( compensateExp.getSumLoss()-compensateExp.getSumLossBZPaid()-compensateExp.getOtherDeductFee() )*compensateExp
									.getIndemnityDutyRate());
					String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossTextBelowDamageItemRealCost4ZF_X.value,
							MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100));
					logger.info("sumRealLossText = "+sumRealLossText);
					compensateExp.setSumRealLossText(sumRealLossText);

				}
				// 按实际价值赔付。
				else{
					compensateExp.setSumRealLoss(( compensateExp.getDeviceActualValue()-compensateExp.getSumLossBZPaid() )*compensateExp
							.getIndemnityDutyRate());

					String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossTextBelowDamageItemRealCost4ZF_X.value,
							MoneyFormator.format4Output(compensateExp.getDeviceActualValue()),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100));
					logger.info("sumRealLossText = "+sumRealLossText);
					compensateExp.setSumRealLossText(sumRealLossText);
					compensateExp.setItemFlag("1");
				}

				String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText_X.value,
						MoneyFormator.format4Output(compensateExp.getSumRealLoss()),
						MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
						MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);
			}
		}

	}
    
	/**
	 * 2020附加车轮单独损失险
	 * @param compensateListVo
	 * @param compensateExp
	 * @modified: ☆YZY(2020年9月4日 上午9:35:13): <br>
	 */
	private void calculatKindCode2020W1(CompensateListVo compensateListVo,CompensateExp compensateExp) {
		    //剩余保额
		    BigDecimal restAmount=new BigDecimal(0);
		    //已减保额
		    BigDecimal ductAmount=new BigDecimal(0);
		    //险别保额
		    BigDecimal kindAmount=new BigDecimal(0);
		    List<PrpLCMainVo> prpLCMainVos=prpLCMainService.findPrpLCMainsByRegistNo(compensateListVo.getRegistNo());
		    if(prpLCMainVos!=null && prpLCMainVos.size()>0){
		    	for(PrpLCMainVo cmainVo:prpLCMainVos){
		    		if(!"1101".equals(cmainVo.getRiskCode())){
		    			List<PrpLCItemKindVo> prpLCItemKindVos=cmainVo.getPrpCItemKinds();
		    			if(prpLCItemKindVos!=null && prpLCItemKindVos.size()>0) {
		    				for(PrpLCItemKindVo citemKindVo:prpLCItemKindVos) {
		    					if(CodeConstants.KINDCODE.KINDCODE_W1.equals(citemKindVo.getKindCode())) {
		    						kindAmount=DataUtils.NullToZero(citemKindVo.getAmount());
		    						break;
		    					}
		    				}
		    			}
		    			List<PrpLEndorVo> prpLEndorVos=compensateTaskService.findPrpEndorListByPolicyNo(cmainVo.getPolicyNo());
		    			if(prpLEndorVos!=null && prpLEndorVos.size()>0){
		    				for(PrpLEndorVo prpLEndorVo:prpLEndorVos) {
		    					if(CodeConstants.KINDCODE.KINDCODE_W1.equals(prpLEndorVo.getKindCode())) {
		    						ductAmount=ductAmount.add(DataUtils.NullToZero(prpLEndorVo.getEndorAmount()));
		    					}
		    					
		    				}
		    				
		    			}
		    		}
		    	}
		    }
		    if(ductAmount.compareTo(kindAmount)>=0) {
		    	restAmount=new BigDecimal(0);
		    }else {
		    	restAmount=kindAmount.subtract(ductAmount);
		    }
		    
			String payHeadText = "";
				// 损失金额 <= 保额。
				if((compensateExp.getSumLoss()-compensateExp.getSumLossBZPaid()-compensateExp.getOtherDeductFee())*compensateExp.getIndemnityDutyRate()<=restAmount.doubleValue()){
					compensateExp.setSumRealLoss(( compensateExp.getSumLoss()-compensateExp.getSumLossBZPaid()-compensateExp.getOtherDeductFee() )*compensateExp.getIndemnityDutyRate());
					compensateExp.setSumRealPay(( compensateExp.getSumLoss()-compensateExp.getSumLossBZPaid()-compensateExp.getOtherDeductFee() )*compensateExp.getIndemnityDutyRate());//真实赔付
					compensateExp.setMsumRealPay(0);
					String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_W1.value,
							MoneyFormator.format4Output(restAmount.doubleValue()),
							MoneyFormator.format4Output(compensateExp.getSumLoss()),
							MoneyFormator.format4Output(compensateExp.getSumLossBZPaid()),
							MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100));
					logger.info("sumRealPayText = "+sumRealPayText);
					compensateExp.setSumRealPayText(sumRealPayText);
					payHeadText = "赔付金额";

				}else{
					compensateExp.setSumRealLoss(( compensateExp.getSumLoss()-compensateExp.getSumLossBZPaid()-compensateExp.getOtherDeductFee() )*compensateExp.getIndemnityDutyRate());
					compensateExp.setSumRealPay(restAmount.doubleValue());//真实赔付
					compensateExp.setMsumRealPay(0);
					String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_W1.value,
							MoneyFormator.format4Output(restAmount.doubleValue()),
							MoneyFormator.format4Output(compensateExp.getSumLoss()),
							MoneyFormator.format4Output(compensateExp.getSumLossBZPaid()),
							MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100));
					logger.info("sumRealPayText = "+sumRealPayText);
					compensateExp.setSumRealPayText(sumRealPayText);
					payHeadText = "保险剩余金额";
				}
				
		

	}
    
	/**
	 * 2020-BP-附加医保外医疗费用责任险（特种车第三者责任保险）
	 * @param compensateListVo
	 * @param compensateExp
	 * @modified: ☆YZY(2020年9月4日 上午9:35:13): <br>
	 */
	private void calculatKindCode2020BP(CompensateListVo compensateListVo,CompensateExp compensateExp) {
		compensateExp.setClaimRate(1);
		compensateExp.setOverFlag("0");
			String payHeadText = "";
				// 损失金额 <= 保额。
				if(compensateExp.getSumLoss()*compensateExp.getIndemnityDutyRate()<=compensateExp.getDamageAmount()){
					compensateExp.setSumRealPay(compensateExp.getSumLoss()*compensateExp.getIndemnityDutyRate());

					String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossText2020_BP.value,
							MoneyFormator.format4Output(compensateExp.getDamageAmount()),
							MoneyFormator.format4Output(compensateExp.getSumLoss()),
							MoneyFormator.format4Output(compensateExp.getIndemnityDutyRate()*100),
							MoneyFormator.format4RateOutput(compensateExp.getSumLoss()*compensateExp.getIndemnityDutyRate()));
					logger.info("sumRealLossText = "+sumRealLossText);
					compensateExp.setSumRealPayText(sumRealLossText);
					payHeadText = "赔付金额";

				}else{
					compensateExp.setSumRealPay(compensateExp.getDamageAmount());

					String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossText2020_BP.value,
							MoneyFormator.format4Output(compensateExp.getDamageAmount()),
							MoneyFormator.format4Output(compensateExp.getSumLoss()),
							MoneyFormator.format4Output(compensateExp.getIndemnityDutyRate()*100),
							MoneyFormator.format4RateOutput(compensateExp.getDamageAmount()));
					logger.info("sumRealLossText = "+sumRealLossText);
					compensateExp.setSumRealPayText(sumRealLossText);
					payHeadText = "赔付金额";
				}
				
		

	}
	
	/**
	 * 2020-D11P-附加医保外医疗费用责任险（特种车车上人员责任保险（司机））
	 * @param compensateListVo
	 * @param compensateExp
	 * @modified: ☆YZY(2020年9月4日 上午9:35:13): <br>
	 */
	private void calculatKindCode2020D11P(CompensateListVo compensateListVo,CompensateExp compensateExp) {
		compensateExp.setClaimRate(1);
		compensateExp.setOverFlag("0");
			String payHeadText = "";
				// 损失金额 <= 保额。
				if(compensateExp.getSumLoss()*compensateExp.getIndemnityDutyRate()<=compensateExp.getDamageAmount()){
					compensateExp.setSumRealPay(compensateExp.getSumLoss()*compensateExp.getIndemnityDutyRate());

					String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossText2020_D11P.value,
							MoneyFormator.format4Output(compensateExp.getDamageAmount()),
							MoneyFormator.format4Output(compensateExp.getSumLoss()),
							MoneyFormator.format4Output(compensateExp.getIndemnityDutyRate()*100),
							MoneyFormator.format4RateOutput(compensateExp.getSumLoss()*compensateExp.getIndemnityDutyRate()));
					logger.info("sumRealLossText = "+sumRealLossText);
					compensateExp.setSumRealPayText(sumRealLossText);
					payHeadText = "赔付金额";

				}else{
					compensateExp.setSumRealPay(compensateExp.getDamageAmount());

					String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossText2020_D11P.value,
							MoneyFormator.format4Output(compensateExp.getDamageAmount()),
							MoneyFormator.format4Output(compensateExp.getSumLoss()),
							MoneyFormator.format4Output(compensateExp.getIndemnityDutyRate()*100),
							MoneyFormator.format4RateOutput(compensateExp.getDamageAmount()));
					logger.info("sumRealLossText = "+sumRealLossText);
					compensateExp.setSumRealPayText(sumRealLossText);
					payHeadText = "赔付金额";
				}
				
		

	}
	/**
	 * 2020-D12P-附加医保外医疗费用责任险（特种车车上人员责任保险（乘客））
	 * @param compensateListVo
	 * @param compensateExp
	 * @modified: ☆YZY(2020年9月4日 上午9:35:13): <br>
	 */
	private void calculatKindCode2020D12P(CompensateListVo compensateListVo,CompensateExp compensateExp) {
		compensateExp.setClaimRate(1);
		compensateExp.setOverFlag("0");
			String payHeadText = "";
				// 损失金额 <= 保额。
				if(compensateExp.getSumLoss()*compensateExp.getIndemnityDutyRate()<=compensateExp.getDamageAmount()){
					compensateExp.setSumRealPay(compensateExp.getSumLoss()*compensateExp.getIndemnityDutyRate());

					String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossText2020_D12P.value,
							MoneyFormator.format4Output(compensateExp.getDamageAmount()),
							MoneyFormator.format4Output(compensateExp.getSumLoss()),
							MoneyFormator.format4Output(compensateExp.getIndemnityDutyRate()*100),
							MoneyFormator.format4RateOutput(compensateExp.getSumLoss()*compensateExp.getIndemnityDutyRate()));
					logger.info("sumRealLossText = "+sumRealLossText);
					compensateExp.setSumRealPayText(sumRealLossText);
					payHeadText = "赔付金额";

				}else{
					compensateExp.setSumRealPay(compensateExp.getDamageAmount());

					String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossText2020_D12P.value,
							MoneyFormator.format4Output(compensateExp.getDamageAmount()),
							MoneyFormator.format4Output(compensateExp.getSumLoss()),
							MoneyFormator.format4Output(compensateExp.getIndemnityDutyRate()*100),
							MoneyFormator.format4RateOutput(compensateExp.getDamageAmount()));
					logger.info("sumRealLossText = "+sumRealLossText);
					compensateExp.setSumRealPayText(sumRealLossText);
					payHeadText = "赔付金额";
				}
				
		

	}
	/**
	 * 2020附加新增加设备损失险
	 * @param compensateListVo
	 * @param compensateExp
	 * @modified: ☆YZY(2020年9月4日 上午9:35:13): <br>
	 */
	private void calculatKindCode2020X(CompensateListVo compensateListVo,CompensateExp compensateExp) {
		    compensateExp.setClaimRate(1);
		    compensateExp.setOverFlag("0");
		    compensateExp.setSumRealLoss((compensateExp.getSumLoss()-compensateExp.getSumLossBZPaid()-compensateExp.getOtherDeductFee())*compensateExp.getIndemnityDutyRate());
		    String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossTextBelowDamageItemRealCost4ZFS2020_X.value,
					MoneyFormator.format4Output(compensateExp.getSumLoss()-compensateExp.getOtherDeductFee()),
					MoneyFormator.format4Output(compensateExp.getSumLossBZPaid()),
					MoneyFormator.format4Output(compensateExp.getIndemnityDutyRate()*100));
		    logger.info("sumRealLossText = "+sumRealLossText);
		    compensateExp.setSumRealLossText(sumRealLossText);
		    
		    
			String payHeadText = "";
				// 损失金额 <= 保额。
				if(compensateExp.getSumLoss()-compensateExp.getOtherDeductFee()-compensateExp.getSumLossBZPaid()<=compensateExp.getDamageAmount()){
					compensateExp.setSumRealPay((compensateExp.getSumLoss()-compensateExp.getOtherDeductFee()-compensateExp.getSumLossBZPaid())*compensateExp.getIndemnityDutyRate());
					String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealLossTextBelowDamageItemRealCost4ZF2020_X.value,
							MoneyFormator.format4Output(compensateExp.getDamageAmount()),
							MoneyFormator.format4Output(compensateExp.getSumLoss()-compensateExp.getOtherDeductFee()),
							MoneyFormator.format4Output(compensateExp.getSumLossBZPaid()),
							MoneyFormator.format4Output(compensateExp.getIndemnityDutyRate()*100));
					logger.info("sumRealPayText = "+sumRealPayText);
					compensateExp.setSumRealPayText(sumRealPayText);
					payHeadText = "赔付金额";

				}else{
					compensateExp.setSumRealPay(compensateExp.getDamageAmount());
					String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealLossTextBelowDamageItemRealCost4ZF2020_X.value,
							MoneyFormator.format4Output(compensateExp.getDamageAmount()),
							MoneyFormator.format4Output(compensateExp.getSumLoss()-compensateExp.getOtherDeductFee()),
							MoneyFormator.format4Output(compensateExp.getSumLossBZPaid()),
							MoneyFormator.format4Output(compensateExp.getIndemnityDutyRate()*100));
					logger.info("sumRealPayText = "+sumRealPayText);
					compensateExp.setSumRealPayText(sumRealPayText);
					payHeadText = "保险金额";
				}
				
	}

	/**
	 * 2020道路救援服务特约条款
	 * @param compensateListVo
	 * @param compensateExp
	 * @modified: ☆YZY(2020年9月4日 上午9:35:13): <br>
	 */
	private void calculatKindCode2020_RS_VS_DS_DC(CompensateListVo compensateListVo,CompensateExp compensateExp) {
		    compensateExp.setClaimRate(1);
		    compensateExp.setOverFlag("0");
			// 损失金额 <= 保额。
			compensateExp.setSumRealPay(compensateExp.getSumLoss());
			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealLossTextBelowDamageItemRealCost4ZF2020_RS_VS_DS_DC.value,
					MoneyFormator.format4Output(compensateExp.getSumLoss()));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
    }
	
	
	private void calculatKindCodeX3(CompensateListVo compensateListVo,CompensateExp compensateExp) {
		// 获取约定免赔率
		double dutyDeductibleRate = 0;
		List<PrpLCItemKindVo> itemKindVoList = registQueryService.findItemKindVo(compensateListVo.getRegistNo(),CodeConstants.KINDCODE.KINDCODE_M3);
		if(itemKindVoList!=null&&itemKindVoList.size()>0){
			for(PrpLCItemKindVo itemKindVo:itemKindVoList){
				if(itemKindVo.getValue()!=null){
					dutyDeductibleRate = Double.valueOf(itemKindVo.getValue().divide(new BigDecimal(100),2,RoundingMode.HALF_UP).toString());
				}
			}
		}
		// 损失金额 <= 保额。
		if(compensateExp.getSumLoss()-compensateExp.getOtherDeductFee()<=compensateExp.getDamageAmount()){
			compensateExp.setSumRealLoss(compensateExp.getSumLoss());
			compensateExp
					.setSumRealPay(( compensateExp.getSumLoss()-compensateExp.getSumLossBZPaid()-compensateExp.getOtherDeductFee() )*( 1-dutyDeductibleRate ));
			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText_X3.value,
					MoneyFormator.format4Output(compensateExp.getSumLoss()),
					MoneyFormator.format4Output(compensateExp.getSumLossBZPaid()+compensateExp.getOtherDeductFee()),
					MoneyFormator.format4RateOutput(dutyDeductibleRate*100));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);

		}else{
			compensateExp.setSumRealLoss(compensateExp.getDamageAmount());
			compensateExp
					.setSumRealPay(( compensateExp.getDamageAmount()-compensateExp.getSumLossBZPaid()-compensateExp.getOtherDeductFee() )*( 1-dutyDeductibleRate ));
			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText_X3_CPRC.value,
					MoneyFormator.format4Output(compensateExp.getDamageAmount()),
					MoneyFormator.format4Output(compensateExp.getSumLossBZPaid()+compensateExp.getOtherDeductFee()),
					MoneyFormator.format4RateOutput(dutyDeductibleRate*100));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
			compensateExp.setItemFlag("1");
		}
	}

	/**
	 * 无法找到第三方的赔付
	 * @param compensateListVo
	 * @param compensateExp
	 * @param cprcCase
	 * @modified: ☆YangKun(2016年6月14日 上午11:09:21): <br>
	 */
	private void calculatNoFondThirdKindCodeX(CompensateListVo compensateListVo,CompensateExp compensateExp,Boolean cprcCase) {

		if(cprcCase){
			// 损失金额 <= 保额。
			if(compensateExp.getSumLoss()-compensateExp.getOtherDeductFee()<=compensateExp.getDamageAmount()){
				compensateExp.setSumRealLoss(( compensateExp.getSumLoss()-compensateExp.getSumLossBZPaid()-compensateExp.getOtherDeductFee() ));

				String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossText_X_NT.value,"定损金额",
						MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()));
				logger.info("sumRealLossText = "+sumRealLossText);
				compensateExp.setSumRealLossText(sumRealLossText);

			}else{
				compensateExp.setSumRealLoss(( compensateExp.getDamageAmount() ));
				String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossText_X_NT.value,"险别保额",
						MoneyFormator.format4Output(compensateExp.getDamageAmount()));
				logger.info("sumRealLossText = "+sumRealLossText);
				compensateExp.setSumRealLossText(sumRealLossText);
				compensateExp.setItemFlag("1");
			}

			compensateExp.setSumRealPay(compensateExp.getSumRealLoss()*( 1-compensateExp.getSelectDeductibleRate() ));
			compensateExp.setMsumRealPay(0);

			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayOverTextX_NT.value,
					MoneyFormator.format4Output(compensateExp.getSumRealLoss()),
					MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);

		}else{// 旧条款
				// 损失金额 <= 实际价值。按损失金额赔付。
			if(compensateExp.getSumLoss()-compensateExp.getOtherDeductFee()<=compensateExp.getDeviceActualValue()){
				compensateExp.setSumRealLoss(( compensateExp.getSumLoss()-compensateExp.getSumLossBZPaid()-compensateExp.getOtherDeductFee() ));
				String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossText_X_NT.value,"定损金额",
						MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()));
				logger.info("sumRealLossText = "+sumRealLossText);
				compensateExp.setSumRealLossText(sumRealLossText);

			}
			// 按实际价值赔付。
			else{
				compensateExp.setSumRealLoss(( compensateExp.getDeviceActualValue()-compensateExp.getSumLossBZPaid() ));
				String compensateText = CodeConstants.CompensateText.sumRealLossText_X_NT.value;
				String sumRealLossText = String.format(compensateText,"出险时实际价值",MoneyFormator.format4Output(compensateExp.getDeviceActualValue()));
				logger.info("sumRealLossText = "+sumRealLossText);
				compensateExp.setSumRealLossText(sumRealLossText);
				compensateExp.setItemFlag("1");
			}

			compensateExp.setSumRealPay(compensateExp.getSumRealLoss()*( 1-compensateExp.getSelectDeductibleRate() ));
			compensateExp.setMsumRealPay(0);

			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayOverTextX_NT.value,
					MoneyFormator.format4Output(compensateExp.getSumRealLoss()),
					MoneyFormator.format4RateOutput(compensateExp.getDeductibleRate()*100));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}

	}

	/**
	 * 发动机险（计算受损金额）。
	 * @param compensateListVo
	 * @param compensateExp
	 * @param cprcCase
	 * @modified:
	 */
	private void calculatKindCodeX2(CompensateListVo compensateListVo,CompensateExp compensateExp,Boolean cprcCase) {

		if(cprcCase){
			if(compensateExp.getSumLoss()-compensateExp.getOtherDeductFee()<=compensateExp.getAmount()){
				compensateExp.setSumRealLoss(( compensateExp.getSumLoss()-compensateExp.getOtherDeductFee() ));

				String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossTextBelowAmount_X1_CPRC.value,
						MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()));
				logger.info("sumRealLossText = "+sumRealLossText);
				compensateExp.setSumRealLossText(sumRealLossText);
			}else{
				compensateExp.setSumRealLoss(( compensateExp.getAmount()-compensateExp.getOtherDeductFee() ));
				String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossTextOverAmount_X1_CPRC.value,
						MoneyFormator.format4Output(compensateExp.getAmount()));
				logger.info("sumRealLossText = "+sumRealLossText);
				compensateExp.setSumRealLossText(sumRealLossText);
			}
		}else{
			if(compensateExp.getSumLoss()-compensateExp.getOtherDeductFee()<=compensateExp.getDamageItemRealCost()){
				compensateExp.setSumRealLoss(( compensateExp.getSumLoss()-compensateExp.getOtherDeductFee() )*compensateExp.getAmount()/compensateExp
						.getEntryItemCarCost());

				String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossTextBelowAmount_X1.value,
						MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
						MoneyFormator.format4Output(compensateExp.getAmount()),MoneyFormator.format4Output(compensateExp.getEntryItemCarCost()));
				logger.info("sumRealLossText = "+sumRealLossText);
				compensateExp.setSumRealLossText(sumRealLossText);
			}else{
				compensateExp.setSumRealLoss(( compensateExp.getDamageItemRealCost() )*compensateExp.getAmount()/compensateExp.getEntryItemCarCost());

				String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealLossTextOverAmount_X1.value,
						MoneyFormator.format4Output(compensateExp.getDamageItemRealCost()),MoneyFormator.format4Output(compensateExp.getAmount()),
						MoneyFormator.format4Output(compensateExp.getEntryItemCarCost()));
				logger.info("sumRealLossText = "+sumRealLossText);
				compensateExp.setSumRealLossText(sumRealLossText);
			}
		}

		// 似乎不需要比较
		// if(compensateExp.getSumRealLoss()>=compensateExp.getDamageAmount()){
		// // 规则64。
		// compensateExp.setItemFlag("1");
		// compensateExp.setSumRealPay(compensateExp.getDamageAmount()*compensateExp.getDeductibleRate()-compensateExp.getSumPrePay());
		// compensateExp.setMsumRealPay(compensateExp.getDamageAmount()*compensateExp.getExceptDeductRate());
		// compensateExp.setItemSumRealPay(compensateExp.getDamageAmount()*compensateExp.getDeductibleRate()-compensateExp.getSumPrePay());
		// String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText28.value,
		// MoneyFormator.format4Output(compensateExp.getDamageAmount()),
		// MoneyFormator.format4Output(compensateExp.getDeductibleRate()),MoneyFormator.format4Output(compensateExp.getSumPrePay()));
		// logger.info("sumRealPayText = "+sumRealPayText);
		// compensateExp.setSumRealPayText(sumRealPayText);
		// }else{
		// 规则66。
		compensateExp.setSumRealPay(compensateExp.getSumRealLoss()*compensateExp.getDeductibleRate()-compensateExp.getSumPrePay());
		compensateExp.setMsumRealPay(compensateExp.getSumRealLoss()*compensateExp.getExceptDeductRate());
		compensateExp.setItemSumRealPay(compensateExp.getSumRealLoss()*compensateExp.getDeductibleRate()-compensateExp.getSumPrePay());
		compensateExp.setItemFlag("1");
		String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText_X1.value,
				MoneyFormator.format4Output(compensateExp.getSumRealLoss()),
				MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100));
		logger.info("sumRealPayText = "+sumRealPayText);
		compensateExp.setSumRealPayText(sumRealPayText);
		// }

	}

	/**
	 * 商业三者险
	 * @param compensateListVo
	 * @param compensateExp
	 * @param cprcCase
	 * @modified:
	 */
	private void calculatKindCodeB(CompensateListVo compensateListVo,CompensateExp compensateExp,Boolean cprcCase) {

		final double loss = compensateExp.getSumLoss()+compensateExp.getRescueFee()-compensateExp.getbZPaid()-compensateExp.getOtherDeductFee();
		final double indemnityDutyLoss = loss*compensateExp.getIndemnityDutyRate();
		if( !compensateExp.getRecoveryOrPayFlag().equals(CodeConstants.PayFlagType.CLEAR_PAY)){
			if("1".equals(compensateExp.getOverFlag())){// 超过比例

				// 规则4。
				compensateExp.setItemFlag("1");
				compensateExp.setSumRealPay(compensateExp.getDamageAmount()*compensateExp.getClaimRate()*compensateExp.getDeductibleRate());
				compensateExp.setMsumRealPay(compensateExp.getDamageAmount()*compensateExp.getClaimRate()*compensateExp.getExceptDeductRate());
				compensateExp.setItemSumRealPay(loss*compensateExp.getIndemnityDutyRate()*compensateExp.getDeductibleRate());

				String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealOverPayText_B.value,
						MoneyFormator.format4Output(compensateExp.getDamageAmount()),
						MoneyFormator.format4RateOutput(compensateExp.getClaimRate()*100),
						MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
						MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100));

				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);

			}else{
				if(compensateExp.getPersonChargeName()!=null){
					// 规则0+规则1。以规则0开发。因为残值最小为0。
					compensateExp.setSumRealPay(loss*compensateExp.getIndemnityDutyRate()*compensateExp.getDeductibleRate());
					compensateExp.setMsumRealPay(loss*compensateExp.getIndemnityDutyRate()*compensateExp.getExceptDeductRate());

					String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText_B_personName.value,
							compensateExp.getPersonChargeName(),MoneyFormator.format4Output(compensateExp.getSumLoss()+compensateExp.getRescueFee()),
							MoneyFormator.format4Output(compensateExp.getbZPaid()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100),
							MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
							MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100));
					logger.info("sumRealPayText = "+sumRealPayText);
					compensateExp.setSumRealPayText(sumRealPayText);
				}else{
					// 规则2+规则3。以规则2开发。因为残值最小为0。
					compensateExp.setSumRealPay(loss*compensateExp.getIndemnityDutyRate()*compensateExp.getDeductibleRate());
					compensateExp.setMsumRealPay(loss*compensateExp.getIndemnityDutyRate()*compensateExp.getExceptDeductRate());

					String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText_B.value,
							MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getRescueFee()),
							MoneyFormator.format4Output(compensateExp.getbZPaid()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100),
							MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
							MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100));
					logger.info("sumRealPayText = "+sumRealPayText);
					compensateExp.setSumRealPayText(sumRealPayText);
				}
			}

			// if(indemnityDutyLoss<=compensateExp.getDamageAmount()){
			// if(compensateExp.getPersonChargeName()!=null){
			// // 规则0+规则1。以规则0开发。因为残值最小为0。
			// compensateExp.setSumRealPay(loss*compensateExp.getIndemnityDutyRate()*compensateExp.getDeductibleRate());
			// compensateExp.setMsumRealPay(loss*compensateExp.getIndemnityDutyRate()*compensateExp.getExceptDeductRate());
			//
			// String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText_B_personName.value,
			// compensateExp.getPersonChargeName(),
			// MoneyFormator.format4Output(compensateExp.getSumLoss()+compensateExp.getRescueFee()),
			// MoneyFormator.format4Output(compensateExp.getbZPaid()),
			// MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
			// MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100),
			// MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
			// MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100));
			// logger.info("sumRealPayText = "+sumRealPayText);
			// compensateExp.setSumRealPayText(sumRealPayText);
			// }else{
			// // 规则2+规则3。以规则2开发。因为残值最小为0。
			// compensateExp.setSumRealPay(loss*compensateExp.getIndemnityDutyRate()*compensateExp.getDeductibleRate());
			// compensateExp.setMsumRealPay(loss*compensateExp.getIndemnityDutyRate()*compensateExp.getExceptDeductRate());
			//
			// String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText_B.value,
			// MoneyFormator.format4Output(compensateExp.getSumLoss()),
			// MoneyFormator.format4Output(compensateExp.getRescueFee()),
			// MoneyFormator.format4Output(compensateExp.getbZPaid()),
			// MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
			// MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100),
			// MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
			// MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100));
			// logger.info("sumRealPayText = "+sumRealPayText);
			// compensateExp.setSumRealPayText(sumRealPayText);
			// }
			// }else{
			// if(compensateExp.getPersonChargeName()!=null){
			// // 规则4。
			// compensateExp.setItemFlag("1");
			// compensateExp.setSumRealPay(compensateExp.getDamageAmount()*compensateExp.getDeductibleRate());
			// compensateExp.setMsumRealPay(compensateExp.getDamageAmount()*compensateExp.getExceptDeductRate());
			// compensateExp.setItemSumRealPay(loss*compensateExp.getIndemnityDutyRate()*compensateExp.getDeductibleRate());
			//
			// String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealOverPayText_B_personName.value,
			// compensateExp.getPersonChargeName(),
			// MoneyFormator.format4Output(compensateExp.getDamageAmount()),
			// MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
			// MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100));
			// logger.info("sumRealPayText = "+sumRealPayText);
			// compensateExp.setSumRealPayText(sumRealPayText);
			// }else{
			//
			// // 规则5。
			// compensateExp.setItemFlag("1");
			// compensateExp.setSumRealPay(compensateExp.getDamageAmount()*compensateExp.getDeductibleRate());
			// compensateExp.setMsumRealPay(compensateExp.getDamageAmount()*compensateExp.getExceptDeductRate());
			// compensateExp.setItemSumRealPay(loss*compensateExp.getIndemnityDutyRate()*compensateExp.getDeductibleRate());
			//
			// String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealOverPayText_B.value,
			// MoneyFormator.format4Output(compensateExp.getDamageAmount()),
			// MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
			// MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100));
			//
			// logger.info("sumRealPayText = "+sumRealPayText);
			// compensateExp.setSumRealPayText(sumRealPayText);
			// }
			// }
		}else{// 清付
			if(indemnityDutyLoss<=compensateExp.getDamageAmount()){
				if(compensateExp.getPersonChargeName()!=null){
					// 规则6+规则7。以规则6开发。因为残值最小为6。
					compensateExp.setSumRealPay(loss*compensateExp.getIndemnityDutyRate()*compensateExp.getDeductibleRate());
					compensateExp.setMsumRealPay(loss*compensateExp.getExceptDeductRate()*compensateExp.getDeductibleRate());

					String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText_B_personName.value,
							compensateExp.getPersonChargeName(),MoneyFormator.format4Output(compensateExp.getSumLoss()+compensateExp.getRescueFee()),
							MoneyFormator.format4Output(compensateExp.getbZPaid()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()),
							MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
							MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100));
					logger.info("sumRealPayText = "+sumRealPayText);
					compensateExp.setSumRealPayText(sumRealPayText);
				}else{
					// 规则8+规则9。以规则8开发。因为残值最小为0。

					compensateExp.setSumRealPay(( compensateExp.getSumLoss()+compensateExp.getRescueFee()-compensateExp.getSumRest()-compensateExp
							.getbZPaid()-compensateExp.getOtherDeductFee() )*compensateExp.getIndemnityDutyRate()*compensateExp.getDeductibleRate());

					compensateExp
							.setMsumRealPay(( compensateExp.getSumLoss()+compensateExp.getRescueFee()-compensateExp.getSumRest()-compensateExp
									.getbZPaid()-compensateExp.getOtherDeductFee() )*compensateExp.getExceptDeductRate()*compensateExp
									.getIndemnityDutyRate());

					String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText_B.value,
							MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getRescueFee()),
							MoneyFormator.format4Output(compensateExp.getbZPaid()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
							MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100),
							MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
							MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100));
					logger.info("sumRealPayText = "+sumRealPayText);
					compensateExp.setSumRealPayText(sumRealPayText);
				}
			}else{
				if(compensateExp.getPersonChargeName()!=null){
					// 规则10。
					compensateExp.setItemFlag("1");
					compensateExp.setSumRealPay(compensateExp.getDamageAmount()*compensateExp.getDeductibleRate());
					compensateExp.setMsumRealPay(compensateExp.getDamageAmount()*compensateExp.getExceptDeductRate());
					compensateExp.setItemSumRealPay(loss*compensateExp.getIndemnityDutyRate()*compensateExp.getDeductibleRate());

					String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealOverPayText_B_personName.value,
							compensateExp.getPersonChargeName(),MoneyFormator.format4Output(compensateExp.getDamageAmount()),
							MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
							MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100));
					logger.info("sumRealPayText = "+sumRealPayText);
					compensateExp.setSumRealPayText(sumRealPayText);
				}else{

					// 规则11。
					compensateExp.setItemFlag("1");
					compensateExp.setSumRealPay(compensateExp.getDamageAmount()*compensateExp.getDeductibleRate());
					compensateExp.setMsumRealPay(compensateExp.getDamageAmount()*compensateExp.getExceptDeductRate());
					compensateExp.setItemSumRealPay(loss*compensateExp.getIndemnityDutyRate()*compensateExp.getDeductibleRate()-compensateExp
							.getRecoveryPay());;

					String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealOverPayText_B.value,
							MoneyFormator.format4Output(compensateExp.getDamageAmount()),
							MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
							MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100));
					logger.info("sumRealPayText = "+sumRealPayText);
					compensateExp.setSumRealPayText(sumRealPayText);
				}
			}
		}

	}
    
	/**
	 * 2020商业三者险
	 * @param compensateListVo
	 * @param compensateExp
	 * @modified:
	 */
	private void calculatKindCode2020B(CompensateListVo compensateListVo,CompensateExp compensateExp) {
		final double loss = compensateExp.getSumLoss()+compensateExp.getRescueFee()-compensateExp.getbZPaid()-compensateExp.getOtherDeductFee();
		final double indemnityDutyLoss = loss*compensateExp.getIndemnityDutyRate();
		if( !compensateExp.getRecoveryOrPayFlag().equals(CodeConstants.PayFlagType.CLEAR_PAY)){
			if("1".equals(compensateExp.getOverFlag())){// 超过比例

				// 规则4。
				compensateExp.setItemFlag("1");
				compensateExp.setSumRealPay(compensateExp.getDamageAmount()*compensateExp.getClaimRate());
				compensateExp.setMsumRealPay(0);
				compensateExp.setItemSumRealPay(loss*compensateExp.getIndemnityDutyRate()*(1-(compensateExp.getAbsrate()/100)));
                	String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealOverPayText2020_FJB.value,
    						MoneyFormator.format4Output(compensateExp.getDamageAmount()),
    						MoneyFormator.format4RateOutput(compensateExp.getClaimRate()*100));
    				logger.info("sumRealPayText = "+sumRealPayText);
    				compensateExp.setSumRealPayText(sumRealPayText);
				
			}else{
				if(compensateExp.getPersonChargeName()!=null){
					// 规则0+规则1。以规则0开发。因为残值最小为0。
					compensateExp.setSumRealPay(loss*compensateExp.getIndemnityDutyRate()*(1-(compensateExp.getAbsrate()/100)));
					compensateExp.setMsumRealPay(0);
					if(compensateExp.getBugFlag()){//投保了附加险绝对免赔条款
						String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_FJB_personName.value,
								compensateExp.getPersonChargeName(),MoneyFormator.format4Output(compensateExp.getSumLoss()+compensateExp.getRescueFee()),
								MoneyFormator.format4Output(compensateExp.getbZPaid()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
								MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100),
								MoneyFormator.format4RateOutput(compensateExp.getAbsrate()));
						logger.info("sumRealPayText = "+sumRealPayText);
						compensateExp.setSumRealPayText(sumRealPayText);
					}else{
						String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_B_personName.value,
								compensateExp.getPersonChargeName(),MoneyFormator.format4Output(compensateExp.getSumLoss()+compensateExp.getRescueFee()),
								MoneyFormator.format4Output(compensateExp.getbZPaid()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
								MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100));
						logger.info("sumRealPayText = "+sumRealPayText);
						compensateExp.setSumRealPayText(sumRealPayText);
					}
					
				}else{
					// 规则2+规则3。以规则2开发。因为残值最小为0。
					compensateExp.setSumRealPay(loss*compensateExp.getIndemnityDutyRate()*(1-(compensateExp.getAbsrate()/100)));
					compensateExp.setMsumRealPay(0);
					if(compensateExp.getBugFlag()){//投保了附加险绝对免赔条款
						String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_FJB.value,
								MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getRescueFee()),
								MoneyFormator.format4Output(compensateExp.getbZPaid()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
								MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100),
								MoneyFormator.format4RateOutput(compensateExp.getAbsrate()));
						logger.info("sumRealPayText = "+sumRealPayText);
						compensateExp.setSumRealPayText(sumRealPayText);
					}else{
						String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_B.value,
								MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getRescueFee()),
								MoneyFormator.format4Output(compensateExp.getbZPaid()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
								MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100));
						logger.info("sumRealPayText = "+sumRealPayText);
						compensateExp.setSumRealPayText(sumRealPayText);
					}
					
				}
			}

		}else{// 清付
			if(indemnityDutyLoss<=compensateExp.getDamageAmount()){
				if(compensateExp.getPersonChargeName()!=null){
					// 规则6+规则7。以规则6开发。因为残值最小为6。
					compensateExp.setSumRealPay(loss*compensateExp.getIndemnityDutyRate()*(1-(compensateExp.getAbsrate()/100)));
					compensateExp.setMsumRealPay(0);
					if(compensateExp.getBugFlag()){//投保了附加险绝对免赔条款
						String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_FJB_personName.value,
								compensateExp.getPersonChargeName(),MoneyFormator.format4Output(compensateExp.getSumLoss()+compensateExp.getRescueFee()),
								MoneyFormator.format4Output(compensateExp.getbZPaid()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
								MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()),
								MoneyFormator.format4RateOutput(compensateExp.getAbsrate()));
						logger.info("sumRealPayText = "+sumRealPayText);
						compensateExp.setSumRealPayText(sumRealPayText);
					}else{
						String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_B_personName.value,
								compensateExp.getPersonChargeName(),MoneyFormator.format4Output(compensateExp.getSumLoss()+compensateExp.getRescueFee()),
								MoneyFormator.format4Output(compensateExp.getbZPaid()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
								MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()));
						logger.info("sumRealPayText = "+sumRealPayText);
						compensateExp.setSumRealPayText(sumRealPayText);
					}
					
				}else{
					// 规则8+规则9。以规则8开发。因为残值最小为0。

					compensateExp.setSumRealPay(( compensateExp.getSumLoss()+compensateExp.getRescueFee()-compensateExp.getSumRest()-compensateExp
							.getbZPaid()-compensateExp.getOtherDeductFee() )*compensateExp.getIndemnityDutyRate()*(1-(compensateExp.getAbsrate()/100)));

					compensateExp.setMsumRealPay(0);
					if(compensateExp.getBugFlag()){//投保了附加险绝对免赔条款
						String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_FJB.value,
								MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getRescueFee()),
								MoneyFormator.format4Output(compensateExp.getbZPaid()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
								MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100),
								MoneyFormator.format4RateOutput(compensateExp.getAbsrate()));
						logger.info("sumRealPayText = "+sumRealPayText);
						compensateExp.setSumRealPayText(sumRealPayText);
					}else{
						String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_B.value,
								MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getRescueFee()),
								MoneyFormator.format4Output(compensateExp.getbZPaid()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
								MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100));
						logger.info("sumRealPayText = "+sumRealPayText);
						compensateExp.setSumRealPayText(sumRealPayText);
					}
					
				}
			}else{
				if(compensateExp.getPersonChargeName()!=null){
					// 规则10。
					compensateExp.setItemFlag("1");
					compensateExp.setSumRealPay(compensateExp.getDamageAmount());
					compensateExp.setMsumRealPay(0);
					compensateExp.setItemSumRealPay(loss*compensateExp.getIndemnityDutyRate()*(1-(compensateExp.getAbsrate()/100)));
						String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealOverPayText2020_FJB_personName.value,
								compensateExp.getPersonChargeName(),MoneyFormator.format4Output(compensateExp.getDamageAmount()));
						logger.info("sumRealPayText = "+sumRealPayText);
						compensateExp.setSumRealPayText(sumRealPayText);
				}else{

					// 规则11。
					compensateExp.setItemFlag("1");
					compensateExp.setSumRealPay(compensateExp.getDamageAmount()*compensateExp.getClaimRate());
					compensateExp.setMsumRealPay(0);
					compensateExp.setItemSumRealPay(loss*compensateExp.getIndemnityDutyRate()*(1-(compensateExp.getAbsrate()/100))-compensateExp
							.getRecoveryPay());;
						String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealOverPayText2020_FJB.value,
								MoneyFormator.format4Output(compensateExp.getDamageAmount()),
								MoneyFormator.format4RateOutput(compensateExp.getClaimRate()*100));
						logger.info("sumRealPayText = "+sumRealPayText);
						compensateExp.setSumRealPayText(sumRealPayText);
					
				}
			}
		}

	}

	
	/**
	 * 盗抢险计算 和李雄军确认 是损失金额+施救费 和 盗抢险保额比较 大于则为全损 扣30%免赔
	 * @param compensateListVo
	 * @param compensateExp
	 * @param cprcCase
	 * @modified:
	 */
	private void calculatKindCodeG(CompensateListVo compensateListVo,CompensateExp compensateExp,Boolean cprcCase) {
		boolean allLossFlag = false;
		double allLoss = compensateExp.getSumLoss()+compensateExp.getRescueFee();

		// double deductibleRate = (1d - 0.2d - compensateExp.getSelectDeductibleRate());//赔率
		// double dutyDeductibleRate = 20d;
		// double exceptDeductRate = deductibleRate* (1 - compensateExp.getSelectDeductibleRate());
		if(cprcCase){ // 商车改
			if(allLoss>=compensateExp.getAmount()){
				// 规则24。
				allLossFlag = true;
				compensateExp.setSumRealPay(( compensateExp.getAmount()-compensateExp.getOtherDeductFee() )*compensateExp.getDeductibleRate());
				compensateExp.setMsumRealPay(( compensateExp.getAmount()-compensateExp.getOtherDeductFee() )*compensateExp.getExceptDeductRate());

				String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayTextG.value,
						MoneyFormator.format4Output(compensateExp.getAmount()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
						MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
						MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);
			}
		}else{
			double amount = compensateExp.getDamageAmount();
			String amountText = "保险金额";
			if(compensateExp.getDamageItemRealCost()<amount){
				amount = compensateExp.getDamageItemRealCost();
				amountText = "出险时保险车辆的实际价值";
			}

			if(allLoss>=amount){
				allLossFlag = true;
				compensateExp.setSumRealPay(amount*compensateExp.getDeductibleRate());
				compensateExp.setMsumRealPay(amount*compensateExp.getExceptDeductRate());

				String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayTextCostG.value,amountText,
						MoneyFormator.format4Output(amount),MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
						MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);
			}
		}

		if( !allLossFlag){// 非全损
			compensateExp.setSumRealPay(( allLoss-compensateExp.getOtherDeductFee() )*( 1-compensateExp.getSelectDeductibleRate() ));
			compensateExp.setMsumRealPay(( allLoss-compensateExp.getOtherDeductFee() )*compensateExp.getExceptDeductRate());

			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayTextBPartG.value,
					MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getRescueFee()),
					MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
					MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}
	}
    
	/**
	 * 2020盗抢险计算 和李雄军确认 是损失金额+施救费 和 盗抢险保额比较
	 * @param compensateListVo
	 * @param compensateExp
	 * @modified:
	 */
	private void calculatKindCode2020G(CompensateListVo compensateListVo,CompensateExp compensateExp) {
		double allLoss = (compensateExp.getSumLoss()+compensateExp.getRescueFee()-compensateExp.getOtherDeductFee())*(1-(compensateExp.getAbsrate()/100));
		if(allLoss>=compensateExp.getAmount()){
			// 规则24。
			compensateExp.setSumRealPay(compensateExp.getAmount());
			compensateExp.setMsumRealPay(0);
            if(compensateExp.getBugFlag()){//买了附加险绝对免赔条款
            	String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_FJ_G.value,
						MoneyFormator.format4Output(compensateExp.getAmount()),
						MoneyFormator.format4Output(compensateExp.getSumLoss()),
						MoneyFormator.format4Output(compensateExp.getRescueFee()),
						MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
						MoneyFormator.format4RateOutput(compensateExp.getAbsrate()));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);
            }else{
            	String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayTextBPart2020_G.value,
						MoneyFormator.format4Output(compensateExp.getAmount()),
						MoneyFormator.format4Output(compensateExp.getSumLoss()),
						MoneyFormator.format4Output(compensateExp.getRescueFee()),
						MoneyFormator.format4Output(compensateExp.getOtherDeductFee()));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);
            }
			
		}else{
			// 规则24。
			compensateExp.setSumRealPay(allLoss);
			compensateExp.setMsumRealPay(0);
            if(compensateExp.getBugFlag()){//买了附加险绝对免赔条款
            	String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_FJ_G.value,
						MoneyFormator.format4Output(compensateExp.getAmount()),
						MoneyFormator.format4Output(compensateExp.getSumLoss()),
						MoneyFormator.format4Output(compensateExp.getRescueFee()),
						MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
						MoneyFormator.format4RateOutput(compensateExp.getAbsrate()));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);
            }else{
            	String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayTextBPart2020_G.value,
						MoneyFormator.format4Output(compensateExp.getAmount()),
						MoneyFormator.format4Output(compensateExp.getSumLoss()),
						MoneyFormator.format4Output(compensateExp.getRescueFee()),
						MoneyFormator.format4Output(compensateExp.getOtherDeductFee()));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);
            }
			
		}

	}
	
	/**
	 * 车上司机 和车上乘客险计算
	 * @param compensateListVo
	 * @param compensateExp
	 * @param cprcCase
	 * @modified:
	 */
	private void calculatKindCodeD11(CompensateListVo compensateListVo,CompensateExp compensateExp,Boolean cprcCase) {

		if(compensateExp.getTempMoney()<=compensateExp.getUnitAmount()){
			// 规则30。
			compensateExp.setSumRealPay(( compensateExp.getSumLoss()-compensateExp.getbZPaid()-compensateExp.getOtherDeductFee() )*compensateExp
					.getIndemnityDutyRate()*compensateExp.getDeductibleRate());

			compensateExp.setMsumRealPay(( compensateExp.getSumLoss()-compensateExp.getbZPaid()-compensateExp.getOtherDeductFee() )*compensateExp
					.getExceptDeductRate()*compensateExp.getIndemnityDutyRate());

			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText_D11.value,compensateExp.getPersonChargeName(),
					MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getbZPaid()),
					MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
					MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100),
					MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
					MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);

		}else{
			// 规则31。
			compensateExp.setItemFlag("1");
			compensateExp.setItemSumRealPay(( compensateExp.getSumLoss()-compensateExp.getbZPaid()-compensateExp.getOtherDeductFee() )*compensateExp
					.getIndemnityDutyRate()*compensateExp.getDeductibleRate());
			compensateExp.setSumRealPay(compensateExp.getUnitAmount()*compensateExp.getClaimRate()*compensateExp.getDeductibleRate());
			compensateExp.setMsumRealPay(compensateExp.getUnitAmount()*compensateExp.getClaimRate()*compensateExp.getExceptDeductRate());

			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayOverAmountText_D11.value,
					compensateExp.getPersonChargeName(),MoneyFormator.format4Output(compensateExp.getUnitAmount()),
					MoneyFormator.format4RateOutput(compensateExp.getClaimRate()*100),
					MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
					MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}

	}
    
	
	
	
	/**
	 * 2020新条款车上司机
	 * @param compensateListVo
	 * @param compensateExp
	 * @modified:
	 */
	private void calculatKindCode2020D11(CompensateListVo compensateListVo,CompensateExp compensateExp) {
		compensateExp.setSumRealPay(( compensateExp.getSumLoss()-compensateExp.getbZPaid()-compensateExp.getOtherDeductFee() )*compensateExp.getIndemnityDutyRate()*(1-(compensateExp.getAbsrate()/100)));
		if(compensateExp.getSumRealPay()<=compensateExp.getUnitAmount()){
			// 规则30。
			compensateExp.setMsumRealPay(0);
            if(compensateExp.getBugFlag()){//承保了附加险绝对免赔条款
            	String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_FJ_D11.value,compensateExp.getPersonChargeName(),
    					MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getbZPaid()),
    					MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
    					MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100),
    					MoneyFormator.format4RateOutput(compensateExp.getAbsrate()));
    			logger.info("sumRealPayText = "+sumRealPayText);
    			compensateExp.setSumRealPayText(sumRealPayText);

            }else{
            	String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_D11.value,compensateExp.getPersonChargeName(),
    					MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getbZPaid()),
    					MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
    					MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100));
    			logger.info("sumRealPayText = "+sumRealPayText);
    			compensateExp.setSumRealPayText(sumRealPayText);

            }
			
		}else{
			// 规则31。
			compensateExp.setItemFlag("1");
			compensateExp.setItemSumRealPay(( compensateExp.getSumLoss()-compensateExp.getbZPaid()-compensateExp.getOtherDeductFee() )*compensateExp
					.getIndemnityDutyRate()*(1-(compensateExp.getAbsrate()/100)));
			compensateExp.setSumRealPay(compensateExp.getUnitAmount()*compensateExp.getClaimRate());
			compensateExp.setMsumRealPay(0);
			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayOverAmountText2020_FJ_D11.value,
					compensateExp.getPersonChargeName(),MoneyFormator.format4Output(compensateExp.getUnitAmount()),
					MoneyFormator.format4RateOutput(compensateExp.getClaimRate()*100));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
			
		}

	}
	/**
	 * 2020新条款车上乘客险计算
	 * @param compensateListVo
	 * @param compensateExp
	 * @modified:
	 */
	private void calculatKindCode2020D12(CompensateListVo compensateListVo,CompensateExp compensateExp) {
		if(compensateExp.getTempMoney()<=compensateExp.getUnitAmount()){
			// 规则30。
			compensateExp.setSumRealPay(( compensateExp.getSumLoss()-compensateExp.getbZPaid()-compensateExp.getOtherDeductFee() )*compensateExp
					.getIndemnityDutyRate()*(1-(compensateExp.getAbsrate()/100)));

			compensateExp.setMsumRealPay(0);
            if(compensateExp.getBugFlag()){//承保了附加险绝对免赔条款
            	String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_FJ_D12.value,compensateExp.getPersonChargeName(),
    					MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getbZPaid()),
    					MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
    					MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100),
    					MoneyFormator.format4RateOutput(compensateExp.getAbsrate()));
    			logger.info("sumRealPayText = "+sumRealPayText);
    			compensateExp.setSumRealPayText(sumRealPayText);

            }else{
            	String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_D12.value,compensateExp.getPersonChargeName(),
    					MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getbZPaid()),
    					MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
    					MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100));
    			logger.info("sumRealPayText = "+sumRealPayText);
    			compensateExp.setSumRealPayText(sumRealPayText);

            }
			
		}else{
			// 规则31。
			compensateExp.setItemFlag("1");
			compensateExp.setItemSumRealPay(( compensateExp.getSumLoss()-compensateExp.getbZPaid()-compensateExp.getOtherDeductFee() )*compensateExp
					.getIndemnityDutyRate()*(1-(compensateExp.getAbsrate()/100)));
			compensateExp.setSumRealPay(compensateExp.getUnitAmount()*compensateExp.getClaimRate());
			compensateExp.setMsumRealPay(0);
				String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayOverAmountText2020_FJ_D12.value,
						compensateExp.getPersonChargeName(),MoneyFormator.format4Output(compensateExp.getUnitAmount()),
						MoneyFormator.format4RateOutput(compensateExp.getClaimRate()*100));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);
			
		}

	}

	
	/**
	 * 玻璃单独破碎险计算
	 * @param compensateListVo
	 * @param compensateExp
	 * @param cprcCase
	 * @modified:
	 */
	private void calculatKindCodeF(CompensateListVo compensateListVo,CompensateExp compensateExp,Boolean cprcCase) {

		double sumLoss = compensateExp.getSumLoss()+compensateExp.getRescueFee()-compensateExp.getOtherDeductFee();
		if(cprcCase){
			double amount = compensateExp.getAmount();
			if(amount<=sumLoss){
				compensateExp.setItemFlag("1");
				compensateExp.setItemSumRealPay(sumLoss);
				compensateExp.setSumRealPay(amount);
				String compensateText = CodeConstants.CompensateText.sumRealOverPayText_F_CPRC.value;

				String sumRealPayText = String.format(compensateText,MoneyFormator.format4Output(amount));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);
			}else{
				compensateExp.setSumRealPay(sumLoss);
				String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText_F_CPRC.value,
						MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getRescueFee()),
						MoneyFormator.format4Output(compensateExp.getOtherDeductFee()));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);
			}
		}else{
			double damageItemRealCost = compensateExp.getDamageItemRealCost();
			if(damageItemRealCost<=sumLoss){
				// 规则78。
				compensateExp.setItemFlag("1");
				compensateExp.setItemSumRealPay(compensateExp.getSumLoss());

				compensateExp.setSumRealPay(compensateExp.getDamageItemRealCost());
				String compensateText = CodeConstants.CompensateText.sumRealOverPayText_F.value;

				String sumRealPayText = String.format(compensateText,MoneyFormator.format4Output(compensateExp.getDamageItemRealCost()));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);

			}else{
				// 规则79。
				compensateExp.setSumRealPay(sumLoss);
				String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText_F.value,
						MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getRescueFee()),
						MoneyFormator.format4Output(compensateExp.getOtherDeductFee()));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);
			}
		}

	}

	/**
	 * 车身划痕损失险计算
	 * @param compensateListVo
	 * @param compensateExp
	 * @param cprcCase
	 * @modified:
	 */
	private void calculatKindCodeL(CompensateListVo compensateListVo,CompensateExp compensateExp,Boolean cprcCase) {

		double sumPaid = compensateExp.getSumLoss()+compensateExp.getRescueFee()-compensateExp.getOtherDeductFee();
		if(sumPaid>=compensateExp.getDamageAmount()){
			// 规则41。
			compensateExp.setItemFlag("1");
			compensateExp.setSumRealPay(compensateExp.getDamageAmount()*( 1-compensateExp.getDutyDeductibleRate() ));
			compensateExp.setItemSumRealPay(sumPaid*( 1-compensateExp.getDutyDeductibleRate() ));
			compensateExp.setMsumRealPay(compensateExp.getDamageAmount()*compensateExp.getExceptDeductRate());
			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayOverAmoutText_L.value,
					MoneyFormator.format4Output(compensateExp.getDamageAmount()),
					MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}else{
			// 规则43。
			compensateExp.setSumRealPay(sumPaid*( 1-compensateExp.getDutyDeductibleRate() ));
			compensateExp.setMsumRealPay(sumPaid*compensateExp.getExceptDeductRate());
			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText_L.value,
					MoneyFormator.format4Output(compensateExp.getSumLoss()+compensateExp.getRescueFee()),
					MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
					MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}

	}
    
	/**
	 * 车身划痕损失险计算2020L
	 * @param compensateListVo
	 * @param compensateExp
	 * @param cprcCase
	 * @modified:
	 */
	private void calculatKindCode2020L(CompensateListVo compensateListVo,CompensateExp compensateExp) {

		double sumPaid = compensateExp.getSumLoss()+compensateExp.getRescueFee()-compensateExp.getOtherDeductFee();
		if(sumPaid>=compensateExp.getDamageAmount()){
			// 规则41。
			compensateExp.setItemFlag("1");
			compensateExp.setSumRealPay(compensateExp.getDamageAmount());
			compensateExp.setItemSumRealPay(sumPaid);
			compensateExp.setMsumRealPay(0);
			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayOverAmoutText2020_L.value,
					MoneyFormator.format4Output(compensateExp.getDamageAmount()));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}else{
			// 规则43。
			compensateExp.setSumRealPay(sumPaid);
			compensateExp.setMsumRealPay(0);
			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_L.value,
					MoneyFormator.format4Output(compensateExp.getSumLoss()+compensateExp.getRescueFee()),
					MoneyFormator.format4Output(compensateExp.getOtherDeductFee()));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}

	}
	/**
	 * 自燃损失险险计算
	 * @param compensateListVo
	 * @param compensateExp
	 * @param cprcCase
	 * @modified:
	 */
	private void calculatKindCodeZ(CompensateListVo compensateListVo,CompensateExp compensateExp,Boolean cprcCase) {

		double sumPaid = compensateExp.getSumLoss()-compensateExp.getOtherDeductFee();
		double sumLoss = 0d;// 实际损失
		double rescueFee = 0d; // 施救费
		String amountText = "保险金额";
		double amount = compensateExp.getDamageAmount();

		// 施救费不能大于保险金额
		if(compensateExp.getRescueFee()<=compensateExp.getDamageAmount()){
			rescueFee = compensateExp.getRescueFee();
		}else{
			rescueFee = compensateExp.getDamageAmount();
		}

		if( !cprcCase){// 旧条款需要和实际价值比较
			if(compensateExp.getDamageItemRealCost()<amount){
				amount = compensateExp.getDamageItemRealCost();
				amountText = "实际价值";
			}
		}

		if(sumPaid<=amount){
			amountText = "定损金额";
			sumLoss = sumPaid+rescueFee;
			compensateExp.setSumRealPay(sumLoss*( 1-compensateExp.getDutyDeductibleRate() ));
			compensateExp.setMsumRealPay(sumLoss*compensateExp.getExceptDeductRate());
			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText_Z.value,amountText,
					MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(rescueFee),
					MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
					MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);

		}else{
			compensateExp.setItemFlag("1");
			compensateExp.setItemSumRealPay(sumPaid*compensateExp.getDeductibleRate());

			sumLoss = amount+rescueFee;
			compensateExp.setSumRealPay(sumLoss*( 1-compensateExp.getDutyDeductibleRate() ));
			compensateExp.setMsumRealPay(sumLoss*compensateExp.getExceptDeductRate());

			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealOverPayText_Z.value,amountText,
					MoneyFormator.format4Output(amount),MoneyFormator.format4Output(rescueFee),
					MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}

	}

	/**
	 * 代步车费用特约条款计算
	 * @param compensateListVo
	 * @param compensateExp
	 * @param cprcCase
	 * @modified:
	 */
	private void calculatKindCodeC(CompensateListVo compensateListVo,CompensateExp compensateExp,Boolean cprcCase) {
		if(compensateExp.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_T)){
			if(compensateExp.getLossQuantity()>compensateExp.getQuantity()){// Quantity 最高赔付天数
				// 规则53。
				compensateExp.setItemFlag("1");
				compensateExp.setItemSumRealPay(compensateExp.getLossQuantity()*compensateExp.getUnitAmount()-compensateExp.getOtherDeductFee());
				compensateExp.setSumRealPay(( compensateExp.getQuantity() )*compensateExp.getUnitAmount()-compensateExp.getOtherDeductFee());
				String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealOverPayText_T.value,
						MoneyFormator.format4Output(compensateExp.getQuantity()),MoneyFormator.format4Output(compensateExp.getUnitAmount()),
						MoneyFormator.format4Output(compensateExp.getOtherDeductFee()));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);
			}else{
				// 规则54。
				compensateExp.setSumRealPay(( compensateExp.getLossQuantity() )*compensateExp.getUnitAmount()-compensateExp.getOtherDeductFee());
				String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayTextT.value,
						MoneyFormator.format4Output(compensateExp.getLossQuantity()),MoneyFormator.format4Output(compensateExp.getUnitAmount()),
						MoneyFormator.format4Output(compensateExp.getOtherDeductFee()));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);
			}
		}else{
			// 小于或等于3天的 代步险 免赔
			if(compensateExp.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_C)){
				if(compensateExp.getLossQuantity()<=3){
					compensateExp.setLossQuantity(1);
				}
			}

			if(compensateExp.getLossQuantity()>compensateExp.getQuantity()){
				// 规则53。
				compensateExp.setItemFlag("1");
				compensateExp.setItemSumRealPay(compensateExp.getLossQuantity()*compensateExp.getUnitAmount()-compensateExp.getOtherDeductFee());
				compensateExp.setSumRealPay(( compensateExp.getQuantity()-1 )*compensateExp.getUnitAmount());
				String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealOverPayText_C.value,
						MoneyFormator.format4Output(compensateExp.getQuantity()),"1",MoneyFormator.format4Output(compensateExp.getUnitAmount()),
						MoneyFormator.format4Output(compensateExp.getOtherDeductFee()));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);
			}else{
				// 规则54。
				compensateExp.setSumRealPay(( compensateExp.getLossQuantity()-1 )*compensateExp.getUnitAmount()-compensateExp.getOtherDeductFee());
				String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText_C.value,
						MoneyFormator.format4Output(compensateExp.getLossQuantity()),"1",MoneyFormator.format4Output(compensateExp.getUnitAmount()),
						MoneyFormator.format4Output(compensateExp.getOtherDeductFee()));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);
			}
		}

	}
    
	/**
	 * RF-附加修理期间费用补偿险
	 * @param compensateListVo
	 * @param compensateExp
	 * @modified:
	 */
	private void calculatKindCode2020RF(CompensateListVo compensateListVo,CompensateExp compensateExp) {

			if(compensateExp.getLossQuantity()>compensateExp.getQuantity()){
				// 规则53。
				compensateExp.setItemFlag("1");
				compensateExp.setItemSumRealPay(compensateExp.getLossQuantity()*compensateExp.getUnitAmount()-compensateExp.getOtherDeductFee());
				compensateExp.setSumRealPay(( compensateExp.getQuantity())*compensateExp.getUnitAmount());
				String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealOverPayText2020_C.value,
						MoneyFormator.format4Output(compensateExp.getQuantity()),MoneyFormator.format4Output(compensateExp.getUnitAmount()),
						MoneyFormator.format4Output(compensateExp.getOtherDeductFee()));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);
			}else{
				// 规则54。
				compensateExp.setSumRealPay(( compensateExp.getLossQuantity())*compensateExp.getUnitAmount()-compensateExp.getOtherDeductFee());
				String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_C.value,
						MoneyFormator.format4Output(compensateExp.getLossQuantity()),MoneyFormator.format4Output(compensateExp.getUnitAmount()),
						MoneyFormator.format4Output(compensateExp.getOtherDeductFee()));
				logger.info("sumRealPayText = "+sumRealPayText);
				compensateExp.setSumRealPayText(sumRealPayText);
			}

	}
	
	/**
	 * 车上货物险计算
	 * @param compensateListVo
	 * @param compensateExp
	 * @param cprcCase
	 * @modified:
	 */
	private void calculatKindCodeD2(CompensateListVo compensateListVo,CompensateExp compensateExp,Boolean cprcCase) {
		double sumPay = ( compensateExp.getSumLoss()+compensateExp.getRescueFee()-compensateExp.getbZPaid()-compensateExp.getOtherDeductFee() )*compensateExp
				.getIndemnityDutyRate();

		if(sumPay<=compensateExp.getDamageAmount()){
			// 规则32。
			compensateExp.setSumRealPay(sumPay*( 1-compensateExp.getDutyDeductibleRate() ));

			compensateExp.setMsumRealPay(sumPay*compensateExp.getExceptDeductRate());
			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText_D2.value,
					MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getRescueFee()),
					MoneyFormator.format4Output(compensateExp.getbZPaid()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
					MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100),
					MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}else{

			// 规则33。
			compensateExp.setSumRealPay(compensateExp.getDamageAmount()*( 1-compensateExp.getDutyDeductibleRate() ));
			compensateExp.setItemFlag("1");
			compensateExp.setItemSumRealPay(sumPay*compensateExp.getIndemnityDutyRate()*( 1-compensateExp.getDutyDeductibleRate() ));
			compensateExp.setMsumRealPay(compensateExp.getDamageAmount()*compensateExp.getExceptDeductRate());

			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayOverAmountText_D2.value,
					MoneyFormator.format4Output(compensateExp.getDamageAmount()),
					MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}
	}
    
	/**
	 * 车上货物险计算
	 * @param compensateListVo
	 * @param compensateExp
	 * @modified:
	 */
	private void calculatKindCode2020D2(CompensateListVo compensateListVo,CompensateExp compensateExp) {
		double sumPay = ( compensateExp.getSumLoss()+compensateExp.getRescueFee()-compensateExp.getbZPaid()-compensateExp.getOtherDeductFee() )*compensateExp.getIndemnityDutyRate();

		if(sumPay<=compensateExp.getDamageAmount()){
			// 规则32。
			compensateExp.setSumRealPay(sumPay);

			compensateExp.setMsumRealPay(0);
			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_D2.value,
					MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getRescueFee()),
					MoneyFormator.format4Output(compensateExp.getbZPaid()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
					MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}else{

			// 规则33。
			compensateExp.setSumRealPay(compensateExp.getDamageAmount());
			compensateExp.setItemFlag("1");
			compensateExp.setItemSumRealPay(sumPay*compensateExp.getIndemnityDutyRate());
			compensateExp.setMsumRealPay(0);

			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayOverAmountText2020_D2.value,
					MoneyFormator.format4Output(compensateExp.getDamageAmount()));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}
	}
    
	
	/**
	 * 附加交通事故精神损害赔偿责任保险
	 * @param compensateListVo
	 * @param compensateExp
	 * @param cprcCase
	 * @modified:
	 */
	private void calculatKindCodeR(CompensateListVo compensateListVo,CompensateExp compensateExp,Boolean cprcCase) {

		if(compensateExp.getOverFlag().equals("0")){
			// 规则37。
			compensateExp.setSumRealPay(( compensateExp.getSumLoss()-compensateExp.getbZPaid()-compensateExp.getOtherDeductFee() )*compensateExp
					.getDeductibleRate());
			compensateExp.setMsumRealPay(( compensateExp.getSumLoss()-compensateExp.getbZPaid()-compensateExp.getOtherDeductFee() )*compensateExp
					.getExceptDeductRate());
			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText_R.value,
					MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getbZPaid()),
					MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
					MoneyFormator.format4RateOutput(compensateExp.getDeductibleRate()*100));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}else{
			// 规则38。
			compensateExp.setItemFlag("1");
			compensateExp.setSumRealPay(( compensateExp.getUnitAmount()-compensateExp.getbZPaid() )*compensateExp.getClaimRate()*compensateExp
					.getDeductibleRate());
			compensateExp.setMsumRealPay(compensateExp.getUnitAmount()*compensateExp.getClaimRate()*compensateExp.getExceptDeductRate());
			compensateExp.setItemSumRealPay(( compensateExp.getSumLoss()-compensateExp.getbZPaid() )*compensateExp.getDeductibleRate());
			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealOverPayText_R.value,compensateExp.getPersonChargeName(),
					MoneyFormator.format4Output(compensateExp.getUnitAmount()),MoneyFormator.format4Output(compensateExp.getbZPaid()),
					MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),MoneyFormator.format4RateOutput(compensateExp.getClaimRate()),
					MoneyFormator.format4RateOutput(compensateExp.getDeductibleRate()*100));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}

	}
    
	/**
	 * 附加精神损害抚慰金责任险(第三者责任险)
	 * @param compensateListVo
	 * @param compensateExp
	 * @modified:
	 */
	private void calculatKindCode2020_BS(CompensateListVo compensateListVo,CompensateExp compensateExp) {
		compensateExp.setClaimRate(1);
		compensateExp.setOverFlag("0");
		if(( compensateExp.getSumLoss()-compensateExp.getbZPaid()-compensateExp.getOtherDeductFee() )<=compensateExp.getDamageAmount()){
			// 规则37。
			compensateExp.setSumRealPay(( compensateExp.getSumLoss()-compensateExp.getbZPaid()-compensateExp.getOtherDeductFee() ));
			compensateExp.setMsumRealPay(0);
			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_R.value,
					MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getbZPaid()),
					MoneyFormator.format4Output(compensateExp.getOtherDeductFee()));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}else{
			// 规则38。
			compensateExp.setSumRealPay(compensateExp.getDamageAmount());
			compensateExp.setMsumRealPay(0);
			compensateExp.setItemSumRealPay(compensateExp.getDamageAmount());
			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealOverPayText2020_R.value,
					MoneyFormator.format4Output(compensateExp.getDamageAmount()));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}

	}
    
	/**
	 * D11S-附加精神损害抚慰金责任险（机动车车上人员责任保险（司机））
	 * @param compensateListVo
	 * @param compensateExp
	 * @modified:
	 */
	private void calculatKindCode2020_D11S(CompensateListVo compensateListVo,CompensateExp compensateExp) {
		compensateExp.setClaimRate(1);
		compensateExp.setOverFlag("0");
		if((compensateExp.getSumLoss()-compensateExp.getbZPaid()-compensateExp.getOtherDeductFee())<=compensateExp.getDamageAmount()){
			// 规则37。
			compensateExp.setSumRealPay(( compensateExp.getSumLoss()-compensateExp.getbZPaid()-compensateExp.getOtherDeductFee() ));
			compensateExp.setMsumRealPay(0);
			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_R.value,
					MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getbZPaid()),
					MoneyFormator.format4Output(compensateExp.getOtherDeductFee()));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}else{
			// 规则38。
			compensateExp.setSumRealPay(compensateExp.getDamageAmount());
			compensateExp.setMsumRealPay(0);
			compensateExp.setItemSumRealPay(compensateExp.getDamageAmount());
			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealOverPayText2020_R.value,
					MoneyFormator.format4Output(compensateExp.getDamageAmount()));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}

	}
	
	/**
	 * D12S-附加精神损害抚慰金责任险（机动车车上人员责任保险（乘客））
	 * @param compensateListVo
	 * @param compensateExp
	 * @modified:
	 */
	private void calculatKindCode2020_D12S(CompensateListVo compensateListVo,CompensateExp compensateExp) {
		compensateExp.setClaimRate(1);
		compensateExp.setOverFlag("0");
		if(( compensateExp.getSumLoss()-compensateExp.getbZPaid()-compensateExp.getOtherDeductFee() )<=compensateExp.getDamageAmount()){
			// 规则37。
			compensateExp.setSumRealPay(( compensateExp.getSumLoss()-compensateExp.getbZPaid()-compensateExp.getOtherDeductFee() ));
			compensateExp.setMsumRealPay(0);
			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText2020_R.value,
					MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getbZPaid()),
					MoneyFormator.format4Output(compensateExp.getOtherDeductFee()));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}else{
			// 规则38。
			compensateExp.setSumRealPay(compensateExp.getDamageAmount());
			compensateExp.setMsumRealPay(0);
			compensateExp.setItemSumRealPay(compensateExp.getDamageAmount());
			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealOverPayText2020_R.value,
					MoneyFormator.format4Output(compensateExp.getDamageAmount()));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}

	}
	
	/**
	 * 机动车损失无法找到第三方特约险
	 * @param compensateListVo
	 * @param compensateExp
	 * @param cprcCase
	 * @modified:
	 */
	private void calculatKindCodeNT(CompensateListVo compensateListVo,CompensateExp compensateExp,Boolean cprcCase) {

		// String text="定损金额";
		double rate = 0.3;// 无法找到第三方加扣免赔率
		double sumLoss = compensateExp.getSumLoss()-compensateExp.getOtherDeductFee();
		if(sumLoss>compensateExp.getDamageAmount()*2){
			sumLoss = compensateExp.getDamageAmount()*2;

			compensateExp.setSumRealPay(compensateExp.getDamageAmount()*2*rate);
			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealOverPayText_NT.value,
					MoneyFormator.format4Output(compensateExp.getDamageAmount()),MoneyFormator.format4Output(compensateExp.getDamageAmount()),
					MoneyFormator.format4RateOutput(rate*100));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);

		}else{
			compensateExp.setSumRealPay(( compensateExp.getSumLoss()-compensateExp.getOtherDeductFee() )*rate);
			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText_NT.value,
					MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
					MoneyFormator.format4RateOutput(rate*100));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);

		}
		// if(compensateExp.getSumLoss()>compensateExp.getDamageAmount()){
		// sumLoss = compensateExp.getSumLoss();
		// }
		// if(sumLoss>compensateExp.getAmount()){
		// compensateExp.setItemFlag("1");
		// text ="车损险保险金额";
		// compensateExp.setSumRealPay(sumLoss * compensateExp.getSelectDeductibleRate());
		// }else{
		// text ="车辆损失金额";
		// sumLoss = compensateExp.getAmount();
		// compensateExp.setSumRealPay(sumLoss * compensateExp.getSelectDeductibleRate());
		// }

	}

	/**
	 * 固定设备、仪器损坏扩展条款
	 * @param compensateListVo
	 * @param compensateExp
	 * @param cprcCase
	 * @modified:
	 */
	private void calculatKindCodeK(CompensateListVo compensateListVo,CompensateExp compensateExp,Boolean cprcCase) {

		double sumLoss = compensateExp.getSumLoss()-compensateExp.getOtherDeductFee();
		if(sumLoss<compensateExp.getDamageItemRealCost()){
			logger.info("损失金额 < 实际价值。按损失金额赔付");
			double realLoss = ( compensateExp.getSumLoss()-compensateExp.getOtherDeductFee() )*compensateExp.getIndemnityDutyRate()*( compensateExp
					.getAmount()/compensateExp.getEntryItemCarCost() );

			compensateExp.setSumRealLoss(realLoss*compensateExp.getDeductibleRate());
			compensateExp.setMsumRealPay(realLoss*compensateExp.getExceptDeductible());
			// (1 - 事故责任免赔率) × (1 - 可选免赔率)
			String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealPayText_K.value,
					MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
					MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100),MoneyFormator.format4Output(compensateExp.getAmount()),
					MoneyFormator.format4Output(compensateExp.getEntryItemCarCost()),
					MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
					MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100));
			logger.info("sumRealLossText = "+sumRealLossText);
			compensateExp.setSumRealPayText(sumRealLossText);

		}else{// 按实际价值赔付。
			logger.info("按实际价值赔付。");

			double realLoss = compensateExp.getDamageItemRealCost()*compensateExp.getIndemnityDutyRate();
			compensateExp.setSumRealLoss(realLoss*compensateExp.getDeductibleRate());
			compensateExp.setMsumRealPay(realLoss*compensateExp.getExceptDeductible());

			String sumRealLossText = String.format(CodeConstants.CompensateText.sumRealOverPayText_K.value,
					MoneyFormator.format4Output(compensateExp.getDamageItemRealCost()),
					MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100),
					MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
					MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100));
			logger.info("sumRealLossText = "+sumRealLossText);
			compensateExp.setSumRealPayText(sumRealLossText);

		}

	}

	/**
	 * 租车人人车失踪险 TODO 是否需要和保额比较
	 * @param compensateListVo
	 * @param compensateExp
	 * @param cprcCase
	 * @modified:
	 */
	private void calculatKindCodeZ1(CompensateListVo compensateListVo,CompensateExp compensateExp,Boolean cprcCase) {

		double sumLoss = compensateExp.getSumLoss()-compensateExp.getOtherDeductFee();
		if(sumLoss<compensateExp.getAmount()){
			compensateExp.setSumRealPay(sumLoss*compensateExp.getDeductibleRate());
			compensateExp.setMsumRealPay(sumLoss*compensateExp.getExceptDeductRate());

			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText_Z1.value,
					MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
					MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
					MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100));

			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}else{
			compensateExp.setItemFlag("1");
			compensateExp.setSumRealPay(compensateExp.getAmount()*compensateExp.getDeductibleRate());
			compensateExp.setMsumRealPay(compensateExp.getAmount()*compensateExp.getExceptDeductRate());

			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayOverAmountText_Z1.value,
					MoneyFormator.format4Output(compensateExp.getAmount()),
					MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
					MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100));

			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}

	}

	/**
	 * 约定区域通行费用特约条款 TODO 是否需要和保额比较
	 * @param compensateListVo
	 * @param compensateExp
	 * @param cprcCase
	 * @modified:
	 */
	private void calculatKindCodeZ2(CompensateListVo compensateListVo,CompensateExp compensateExp,Boolean cprcCase) {

		double sumLoss = ( compensateExp.getSumLoss()-compensateExp.getOtherDeductFee() )*compensateExp.getIndemnityDutyRate();

		if(sumLoss<=compensateExp.getAmount()){
			compensateExp.setSumRealPay(sumLoss*compensateExp.getIndemnityDutyRate()*compensateExp.getDeductibleRate());
			compensateExp.setSumRealPay(sumLoss*compensateExp.getExceptDeductible());

			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayTextZ2.value,
					MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
					MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100),
					MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
					MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}else{
			compensateExp.setSumRealPay(compensateExp.getAmount()*compensateExp.getIndemnityDutyRate()*compensateExp.getDeductibleRate());
			compensateExp.setSumRealPay(compensateExp.getAmount()*compensateExp.getExceptDeductible());

			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealOverPayTextZ2.value,
					MoneyFormator.format4Output(compensateExp.getAmount()),MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()),
					MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()),
					MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}

	}

	/**
	 * 附加油污污染责任保险
	 * @param compensateListVo
	 * @param compensateExp
	 * @param cprcCase
	 * @modified: ☆YangKun(2016年6月3日 上午8:55:53): <br>
	 */
	private void calculatKindCodeV1(CompensateListVo compensateListVo,CompensateExp compensateExp,Boolean cprcCase) {

		double payLoss = ( compensateExp.getSumLoss()-compensateExp.getOtherDeductFee() )*compensateExp.getIndemnityDutyRate();
		if(payLoss<=compensateExp.getAmount()){
			compensateExp.setSumRealPay(payLoss*( 1-compensateExp.getDutyDeductibleRate() ));
			compensateExp.setMsumRealPay(payLoss*compensateExp.getExceptDeductRate());

			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayText_V1.value,
					MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
					MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100),
					MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}else{
			compensateExp.setItemFlag("1");
			compensateExp.setSumRealPay(compensateExp.getAmount()*( 1-compensateExp.getDutyDeductibleRate() ));
			compensateExp.setMsumRealPay(compensateExp.getAmount()*compensateExp.getExceptDeductRate());

			String sumRealPayText = String
					.format(CodeConstants.CompensateText.sumRealOverPayText_V1.value,MoneyFormator.format4Output(compensateExp.getAmount()),
							MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}

	}

	/**
	 * 随车行李物品损失保险
	 * @param compensateListVo
	 * @param compensateExp
	 * @param cprcCase
	 * @modified: ☆YangKun(2016年6月3日 上午8:58:54): <br>
	 */
	private void calculatKindCodeNZ(CompensateListVo compensateListVo,CompensateExp compensateExp,Boolean cprcCase) {

		double sumLoss = ( compensateExp.getSumLoss()-compensateExp.getOtherDeductFee() )*compensateExp.getIndemnityDutyRate();
		if(sumLoss<=compensateExp.getAmount()){
			compensateExp.setSumRealPay(sumLoss*compensateExp.getDeductibleRate());
			compensateExp.setMsumRealPay(sumLoss*compensateExp.getExceptDeductRate());

			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealPayTextNZ.value,
					MoneyFormator.format4Output(compensateExp.getSumLoss()),MoneyFormator.format4Output(compensateExp.getOtherDeductFee()),
					MoneyFormator.format4RateOutput(compensateExp.getIndemnityDutyRate()*100),
					MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
					MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}else{
			compensateExp.setSumRealPay(compensateExp.getAmount()*compensateExp.getDeductibleRate());
			compensateExp.setMsumRealPay(compensateExp.getAmount()*compensateExp.getExceptDeductRate());

			String sumRealPayText = String.format(CodeConstants.CompensateText.sumRealOverPayTextNZ.value,
					MoneyFormator.format4Output(compensateExp.getAmount()),
					MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
					MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100));
			logger.info("sumRealPayText = "+sumRealPayText);
			compensateExp.setSumRealPayText(sumRealPayText);
		}

	}

	/**
	 * 交强赔款扣减预赔金额。
	 * @param thirdPartyLossInfos
	 */
	private void compulsorySubPrePay(List<ThirdPartyLossInfo> thirdPartyLossInfos) {

		for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
			if(thirdPartyLossInfo.getBzCompensateText()==null||"".equals(thirdPartyLossInfo.getBzCompensateText())){
				double sumRealPay = thirdPartyLossInfo.getSumRealPay()-thirdPartyLossInfo.getSumPrePaid();
				sumRealPay = sumRealPay==0d ? 0 : sumRealPay;
				String bzCompensateText = String.format(CodeConstants.CompensateText.compulsorySubPrePayText.value,
						MoneyFormator.format4Output(thirdPartyLossInfo.getSumRealPay()),
						MoneyFormator.format4Output(thirdPartyLossInfo.getSumPrePaid()),MoneyFormator.format4Output(sumRealPay));
				thirdPartyLossInfo.setSumRealPay(sumRealPay);
				thirdPartyLossInfo.setBzCompensateText(thirdPartyLossInfo.getBzCompensateText()+bzCompensateText);
				logger.debug(thirdPartyLossInfo.getLossName()+thirdPartyLossInfo.getLossFeeName());
				logger.debug(bzCompensateText);
			}
		}
	}

	/**
	 * 交强计算书重开的超限额调整。
	 * @param thirdPartyLossInfos
	 * @param compensateExp
	 */
	private void calculateCi4CommonReCaseAdjust(List<ThirdPartyLossInfo> thirdPartyLossInfos,CompensateExp compensateExp) {
		// 对重开赔案的交强理算行进行超限额处理。
		// zhongyuhai 20121113
		if(compensateExp.isIsNotFirst4CompulsoryCompensate()){
			double restAmount = 0;
			List<ThirdPartyLossInfo> thirdPartyLossInfo4ReCase = new ArrayList<ThirdPartyLossInfo>();
			for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos){
				if(thirdPartyLossInfo.isIsSameCompensate()){
					thirdPartyLossInfo4ReCase.add(thirdPartyLossInfo);
					restAmount = thirdPartyLossInfo.getBzRestAmount();
				}
			}
			double totalSumRealPayBeforeReCase = 0;
			for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfo4ReCase){
				totalSumRealPayBeforeReCase += thirdPartyLossInfo.getSumRealPay();
			}
			// 超限额。交强险重开赔款计算书超限额调整 = 交强剩余限额 × 原实赔金额 / 重开总赔付金额
			if(restAmount<totalSumRealPayBeforeReCase){
				for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfo4ReCase){
					double sumRealPay4ReCase = restAmount*thirdPartyLossInfo.getSumRealPay()/totalSumRealPayBeforeReCase;
					String bzCompensateText = String.format(CodeConstants.CompensateText.reCaseSumRealPayText.value,
							MoneyFormator.format4Output(restAmount),MoneyFormator.format4Output(thirdPartyLossInfo.getSumRealPay()),
							MoneyFormator.format4Output(totalSumRealPayBeforeReCase),MoneyFormator.format4Output(sumRealPay4ReCase));
					thirdPartyLossInfo.setSumRealPay(MoneyFormator.format(new BigDecimal(sumRealPay4ReCase)).doubleValue());
					thirdPartyLossInfo.setBzCompensateText(thirdPartyLossInfo.getBzCompensateText()+"\r\n"+bzCompensateText);
					logger.debug(bzCompensateText);

				}
			}
		}
	}

	/**
	 * 生成不计免赔的计算公式。
	 * @param compensateExp
	 */
	private void fillMsumRealPayText(CompensateExp compensateExp) {
		if("K2".equals(compensateExp.getKindCode())){
			System.out.println();
		}
		String sumRealPayText = compensateExp.getSumRealPayText();
		logger.debug(compensateExp.getKindCode()+"    "+sumRealPayText);
		List<Integer> locations = new ArrayList<Integer>();

		int index = -1;
		do{
			index = sumRealPayText.indexOf("@", ++index);
			if(index== -1){
				break;
			}else{
				locations.add(index);
			}
		}while(true);

		String mSumRealPayText = "";
		if(locations.size()==4){
			String text = sumRealPayText.substring(locations.get(0)+1,locations.get(1));
			String value = sumRealPayText.substring(locations.get(2)+1,locations.get(3));
			if(compensateExp.getMsumRealPayBeforeJud()>0&&"1".equals(compensateExp.getCheckPolicyM())){// 不计免赔为0 则不展示不计免赔公式
				mSumRealPayText = String.format(CodeConstants.CompensateText.mSumRealPayText.value,text,value,
						MoneyFormator.format4RateOutput(compensateExp.getDutyDeductibleRate()*100),
						MoneyFormator.format4RateOutput(compensateExp.getSelectDeductibleRate()*100),
						MoneyFormator.format4Output(compensateExp.getMsumRealPayBeforeJud()));
			}

			compensateExp.setSumRealPayText(sumRealPayText.replaceAll("@",""));
		}
		logger.info("mSumRealPayText = "+mSumRealPayText);
		compensateExp.setmSumRealPayText(this.format4Space(mSumRealPayText));
	}

	/**
	 * 生成诉讼案计算书。
	 * @param prpLcompensate
	 */
	private void fillSuitCaseText(PrpLCompensateVo prpLCompensateVo) {
		// 诉讼案需要追加诉讼案计算书。
		if(this.isSuitCase(prpLCompensateVo).equals("1")){
			StringBuffer suitCompensateExp = new StringBuffer(
					"\r\n"+"\r\n"+CodeConstants.CompensateText.text14.value+this.space(CodeConstants.SpaceCount.one)+"="+this
							.space(CodeConstants.SpaceCount.one)); // 诉讼案格式理算报告
			suitCompensateExp.append(MoneyFormator.format(prpLCompensateVo.getSumPaidAmt()));
			prpLCompensateVo.setLcText(suitCompensateExp.toString());
		}
	}

	/**
	 * 计算计算书中的交强已赔付金额、本次赔付金额、总赔付金额、不计免赔金额。 适用于所有计算书。
	 * @param prpLcompensate
	 */
	private void fillSumBzPaid_SumPaid_SumThisPaid_SumNoDeductFee4Payment(PrpLCompensateVo prpLCompensateVo) {
		BigDecimal sumThisPaid = this.calSumThisPaid(prpLCompensateVo);
		prpLCompensateVo.setSumAmt(sumThisPaid);

		BigDecimal sumBzPaid4Payment = BigDecimal.ZERO;
		BigDecimal sumPaid4Payment = BigDecimal.ZERO;
		BigDecimal sumNoDeductFee4Payment = BigDecimal.ZERO;

		List<PrpLCompensateVo> compensateList = findCompensateByClaimno(prpLCompensateVo.getClaimNo(),"N",prpLCompensateVo.getCompensateNo(),false);
		for(PrpLCompensateVo compensate:compensateList){
			sumBzPaid4Payment = sumBzPaid4Payment.add(compensate.getSumBzPaid());
			sumPaid4Payment = sumPaid4Payment.add(DataUtils.NullToZero(compensate.getSumAmt())).add(DataUtils.NullToZero(compensate.getSumPreAmt()));
		}

		List<PrpLLossItemVo> prpLLossItemVoList = prpLCompensateVo.getPrpLLossItems();
		for(PrpLLossItemVo lossItem:prpLLossItemVoList){
			// 只考虑自付、清付类型金额。因为代付的交强金额是自付类型金额的复制。
			// zhongyuhai 20121012
			if(CodeConstants.PayFlagType.COMPENSATE_PAY.equals(lossItem.getPayFlag())||CodeConstants.PayFlagType.CLEAR_PAY.equals(lossItem
					.getPayFlag())){

				sumBzPaid4Payment = sumBzPaid4Payment.add(DataUtils.NullToZero(lossItem.getBzPaidLoss())).add(
						DataUtils.NullToZero(lossItem.getBzPaidRescueFee()));
			}
			sumPaid4Payment = sumPaid4Payment.add(lossItem.getSumRealPay()).add(lossItem.getDeductOffAmt());
			sumNoDeductFee4Payment = sumNoDeductFee4Payment.add(lossItem.getDeductOffAmt());
		}

		List<PrpLLossPropVo> prpLLossPropVoList = prpLCompensateVo.getPrpLLossProps();
		List<PrpLLossPropVo> lossPropList = new ArrayList<PrpLLossPropVo>();
		List<PrpLLossPropVo> lossOtherList = new ArrayList<PrpLLossPropVo>();

		for(PrpLLossPropVo prpLLossPropVo:prpLLossPropVoList){
			if("1".equals(prpLLossPropVo.getPropType())){
				lossPropList.add(prpLLossPropVo);
			}else if("9".equals(prpLLossPropVo.getPropType())){
				lossOtherList.add(prpLLossPropVo);
			}
		}
		for(PrpLLossPropVo lossItem:lossPropList){
			sumBzPaid4Payment = sumBzPaid4Payment.add(lossItem.getBzPaidLoss()).add(DataUtils.NullToZero(lossItem.getBzPaidRescueFee()));
			sumPaid4Payment = sumPaid4Payment.add(lossItem.getSumRealPay()).add(lossItem.getDeductOffAmt());
			sumNoDeductFee4Payment = sumNoDeductFee4Payment.add(lossItem.getDeductOffAmt());
		}

		List<PrpLLossPersonVo> prpLLossPersonVoList = prpLCompensateVo.getPrpLLossPersons();
		for(PrpLLossPersonVo prpLLossPersonVo:prpLLossPersonVoList){
			// List<PrpLLossPersonFeeVo> personFeeList = prpLLossPersonVo.getPrpLLossPersonFees();
			// for(PrpLLossPersonFeeVo personFeeVo : personFeeList){
			// sumBzPaid4Payment = sumBzPaid4Payment.add(personFeeVo.getBzPaidLoss());
			// sumPaid4Payment = sumPaid4Payment.add(personFeeVo.getFeeRealPay()).add(personFeeVo.getDeductOffAmt());
			// sumNoDeductFee4Payment = sumNoDeductFee4Payment.add(personFeeVo.getDeductOffAmt());
			// }

			sumBzPaid4Payment = sumBzPaid4Payment.add(prpLLossPersonVo.getBzPaidLoss());
			sumPaid4Payment = sumPaid4Payment.add(prpLLossPersonVo.getSumRealPay()).add(prpLLossPersonVo.getDeductOffAmt());
			sumNoDeductFee4Payment = sumNoDeductFee4Payment.add(prpLLossPersonVo.getDeductOffAmt());

			// 分摊到医疗和死亡
			this.sharePersonFee(prpLLossPersonVo);
		}

		for(PrpLLossPropVo lossItem:lossOtherList){
			// sumBzPaid4Payment = sumBzPaid4Payment.add(lossItem.getBzPaidLoss()).add(lossItem.getBzPaidRescueFee());
			sumBzPaid4Payment = BigDecimal.ZERO;
			sumPaid4Payment = sumPaid4Payment.add(lossItem.getSumRealPay()).add(lossItem.getDeductOffAmt());
			sumNoDeductFee4Payment = sumNoDeductFee4Payment.add(lossItem.getDeductOffAmt());
		}

		// 费用只归入费用计算书，对于快速理赔的费用，不归入赔款计算书。
		// zhongyuhai 20121112
		if( !this.isActualOrExpenseCompensate(prpLCompensateVo.getCompensateKind())){
			List<PrpLChargeVo> prpLchargees = prpLCompensateVo.getPrpLCharges();
			for(PrpLChargeVo lossItem:prpLchargees){
				sumPaid4Payment = sumPaid4Payment.add(lossItem.getFeeRealAmt());
			}
		}

		prpLCompensateVo.setSumAmt(sumPaid4Payment.add(DataUtils.NullToZero(prpLCompensateVo.getSumPreAmt())));
		prpLCompensateVo.setSumBzPaid(sumBzPaid4Payment);
		// 不计免赔金额
		prpLCompensateVo.setSumNoDeductFee(sumNoDeductFee4Payment);
	}

	/**
	 * 分摊金额数据到医疗和死亡
	 * @param prpLLossPersonVo
	 */
	private void sharePersonFee(PrpLLossPersonVo prpLLossPersonVo) {
		BigDecimal sumLoss = BigDecimal.ZERO;// 总损失 - 其他扣除-扣交强
		List<PrpLLossPersonFeeVo> prpLLossPersonFeeVos = prpLLossPersonVo.getPrpLLossPersonFees();
		for(PrpLLossPersonFeeVo personFeeVo:prpLLossPersonFeeVos){
			sumLoss = sumLoss.add(personFeeVo.getFeeLoss()).subtract(personFeeVo.getFeeOffLoss()).subtract(personFeeVo.getBzPaidLoss());
		}

		BigDecimal medRealLoss = BigDecimal.ZERO;
		BigDecimal medRealAmt = BigDecimal.ZERO;
		if(sumLoss.compareTo(BigDecimal.ZERO)==1){
			for(PrpLLossPersonFeeVo personFeeVo:prpLLossPersonFeeVos){
				if(personFeeVo.getLossItemNo().equals("03")){// 医疗
					BigDecimal perFeeLoss = personFeeVo.getFeeLoss().subtract(personFeeVo.getFeeOffLoss()).subtract(personFeeVo.getBzPaidLoss());
					BigDecimal rate = perFeeLoss.divide(sumLoss,8,BigDecimal.ROUND_HALF_UP); // 2,BigDecimal.ROUND_HALF_UP
					medRealLoss = prpLLossPersonVo.getSumRealPay().multiply(rate).setScale(2,BigDecimal.ROUND_HALF_UP);
					medRealAmt = prpLLossPersonVo.getDeductOffAmt().multiply(rate).setScale(2,BigDecimal.ROUND_HALF_UP);

					personFeeVo.setFeeRealPay(medRealLoss);
					personFeeVo.setDeductOffAmt(medRealAmt);

					break;
				}
			}

			for(PrpLLossPersonFeeVo personFeeVo:prpLLossPersonFeeVos){
				if(personFeeVo.getLossItemNo().equals("02")){// 死亡
					personFeeVo.setFeeRealPay(prpLLossPersonVo.getSumRealPay().subtract(medRealLoss));
					personFeeVo.setDeductOffAmt(prpLLossPersonVo.getDeductOffAmt().subtract(medRealAmt));

					break;
				}
			}

		}else{
			for(PrpLLossPersonFeeVo personFeeVo:prpLLossPersonFeeVos){
				personFeeVo.setFeeRealPay(BigDecimal.ZERO);
				personFeeVo.setDeductOffAmt(BigDecimal.ZERO);
			}
		}
	}

	/**
	 * 计算计算书的本次赔付金额。
	 * @param lossList
	 * @return
	 */
	private BigDecimal calSumThisPaid(PrpLCompensateVo prpLCompensateVo) {

		List lossItemList = new ArrayList();
		// Hibernate.initialize(prpLcompensate);
		// PrpLcompensate cloneCompensate = (PrpLcompensate)Beans.deepClone(prpLcompensate);
		lossItemList.addAll(prpLCompensateVo.getPrpLLossItems());
		List<PrpLLossPropVo> prpLLossPropVoList = prpLCompensateVo.getPrpLLossProps();
		List<PrpLLossPropVo> lossPropList = new ArrayList<PrpLLossPropVo>();
		List<PrpLLossPropVo> lossOtherList = new ArrayList<PrpLLossPropVo>();

		for(PrpLLossPropVo prpLLossPropVo:prpLLossPropVoList){
			if("1".equals(prpLLossPropVo.getPropType())){
				lossPropList.add(prpLLossPropVo);
			}else if("9".equals(prpLLossPropVo.getPropType())){
				lossOtherList.add(prpLLossPropVo);
			}
		}
		lossItemList.addAll(lossPropList);
		List<PrpLLossPersonVo> items = prpLCompensateVo.getPrpLLossPersons();
		for(PrpLLossPersonVo item:items){
			lossItemList.addAll(item.getPrpLLossPersonFees());
		}
		lossItemList.addAll(lossOtherList);

		BigDecimal sumThisPaid = BigDecimal.ZERO;
		BigDecimal sumRealPay = BigDecimal.ZERO;
		BigDecimal deductOffAmt = BigDecimal.ZERO;
		for(Object loss:lossItemList){
			if(loss instanceof PrpLLossItemVo){
				sumRealPay = NullToZero(( (PrpLLossItemVo)loss ).getSumRealPay());
				deductOffAmt = NullToZero(( (PrpLLossItemVo)loss ).getDeductOffAmt());
			}else if(loss instanceof PrpLLossPropVo){
				sumRealPay = NullToZero(( (PrpLLossPropVo)loss ).getSumRealPay());
				deductOffAmt = NullToZero(( (PrpLLossPropVo)loss ).getDeductOffAmt());
			}else if(loss instanceof PrpLLossPersonVo){
				sumRealPay = NullToZero(( (PrpLLossPersonVo)loss ).getSumRealPay());
				deductOffAmt = NullToZero(( (PrpLLossPersonVo)loss ).getDeductOffAmt());
			}
			sumThisPaid = sumThisPaid.add(sumRealPay).add(deductOffAmt);
		}

		for(PrpLChargeVo prpLcharge:prpLCompensateVo.getPrpLCharges()){
			sumThisPaid = sumThisPaid.add(prpLcharge.getFeeRealAmt());
		}
		return sumThisPaid;
	}

	/**
	 * 对code对于的分项累加discount。
	 * @param code
	 * @param itemValue
	 * @param summaryMap
	 */
	private void makeItemSummary(String code,Object itemValue,Map summaryMap) {
		if(itemValue instanceof String){
			if(summaryMap.containsKey(code)){
				summaryMap.put(code,summaryMap.get(code).toString()+itemValue);
			}else{
				summaryMap.put(code,itemValue);
			}
		}else{
			if(summaryMap.containsKey(code)){
				summaryMap.put(code,MoneyFormator.format(new BigDecimal((Double)summaryMap.get(code)+(Double)itemValue)).doubleValue());
			}else{
				summaryMap.put(code,MoneyFormator.format(new BigDecimal((Double)itemValue)).doubleValue());
			}
		}
	}

	/**
	 * 返回填充n个空格的字符串。
	 * @param n
	 * @return
	 */
	private String space(int n) {
		StringBuffer sb = new StringBuffer("");
		for(int i = 0; i<n; i++ ){
			sb.append(" ");
		}
		return sb.toString();
	}

	public String format4Space(String text) {
		final String reg = " *"+"="+" *";
		String returnString = new String(text);
		int firstEqualIndex = text.indexOf("=");
		if(firstEqualIndex!= -1){

			String beforeFirstEqual = returnString.substring(0,firstEqualIndex+1);

			char[] beforeFirstEqualChars = beforeFirstEqual.toCharArray();
			int spaceCount = 0;
			for(int i = 0,j = beforeFirstEqualChars.length-1; i<j; i++ ){
				if(beforeFirstEqualChars[i]>255){
					spaceCount += 2;
				}else{
					spaceCount++ ;
				}
			}

			String replacement = this.space(spaceCount)+"="+this.space(CodeConstants.SpaceCount.one);

			String afterFirstEqual = returnString.substring(firstEqualIndex+1);
			String newAfterFirstEqual = afterFirstEqual.replaceAll(reg,replacement);
			returnString = beforeFirstEqual+newAfterFirstEqual;
		}

		return returnString;
	}

	private boolean isAllCetainLossType(Object lossItem) {
		boolean flag = false;
		if(lossItem instanceof PrpLLossItemVo){
			PrpLLossItemVo prpLLossItemVo = (PrpLLossItemVo)lossItem;
			PrpLDlossCarMainVo prpLDlossCarMainVo = lossCarService.findLossCarMainById(prpLLossItemVo.getDlossId());
			if(prpLDlossCarMainVo!=null&&CodeConstants.CetainLossType.DEFLOSS_ALL.equals(prpLDlossCarMainVo.getCetainLossType())){
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 如果实际赔付金额之和等于0，返回0的免赔额，否则返回实际免赔额，避免0损失但扣减免赔额的情况。
	 * @param compensateExp
	 * @return
	 */
	private double makeDeductFee(CompensateExp compensateExp) {
		Assert.notNull(compensateExp);

		BigDecimal sumRealPay = MoneyFormator.format(new BigDecimal(compensateExp.getSumRealPay()));
		double deductFee = 0d;
		if(sumRealPay.compareTo(BigDecimal.ZERO)!=0){
			deductFee = compensateExp.getDeductFee();
		}

		return deductFee;
	}

	public List<PrpLCompensateVo> findPrpLcompensateByRegistNo(String registNo) {
		List<PrpLCompensateVo> prpLCompensateVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addNotEqual("underwriteFlag",CodeConstants.UnderWriteFlag.CANCELFLAG);
		List<PrpLCompensate> prpLCompensateList = databaseDao.findAll(PrpLCompensate.class,queryRule);
		if(prpLCompensateList!=null&& !prpLCompensateList.isEmpty()){
			prpLCompensateVo = Beans.copyDepth().from(prpLCompensateList).toList(PrpLCompensateVo.class);
		}
		return prpLCompensateVo;
	}

	/**
	 * 计算赔案中该险别的已赔付金额。
	 * @param registNo
	 * @param kindCode
	 * @return
	 */
	public Map<String,BigDecimal> querySumRealPay(String claimNo,String compensateNo) {
		Map<String,BigDecimal> kindPaidMap = new HashMap<String,BigDecimal>();

		BigDecimal sumRealPay4Kind = BigDecimal.ZERO;
		PrpLCompensateVo query = new PrpLCompensateVo();
		List<PrpLCompensateVo> prpLCompensateVoList = this.findCompensateByClaimno(claimNo,"N");
		if(prpLCompensateVoList==null&&prpLCompensateVoList.isEmpty()){
			return kindPaidMap;
		}
		for(PrpLCompensateVo prpLCompensateVo:prpLCompensateVoList){
			if(StringUtils.isNotBlank(compensateNo)&&prpLCompensateVo.getCompensateNo().equals(compensateNo)){
				continue;
			}

			// 车辆计算
			List<PrpLLossItemVo> prpLLossItemVoList = prpLCompensateVo.getPrpLLossItems();
			for(PrpLLossItemVo prpLLossItemVo:prpLLossItemVoList){
				String key = prpLLossItemVo.getKindCode();
				if(kindPaidMap.containsKey(key)){
					kindPaidMap.put(key,kindPaidMap.get(key).add(prpLLossItemVo.getSumRealPay()));
				}else{
					kindPaidMap.put(key,prpLLossItemVo.getSumRealPay());
				}
			}
			List<PrpLLossPropVo> prpLLossPropVoList = prpLCompensateVo.getPrpLLossProps();
			for(PrpLLossPropVo prpLLossPropVo:prpLLossPropVoList){
				String key = prpLLossPropVo.getKindCode();
				if(kindPaidMap.containsKey(key)){
					kindPaidMap.put(key,kindPaidMap.get(key).add(prpLLossPropVo.getSumRealPay()));
				}else{
					kindPaidMap.put(key,prpLLossPropVo.getSumRealPay());
				}
			}
			// 人伤计算
			List<PrpLLossPersonVo> prpLLossPersonVoList = prpLCompensateVo.getPrpLLossPersons();
			for(PrpLLossPersonVo prpLLossPersonVo:prpLLossPersonVoList){
				String key = prpLLossPersonVo.getKindCode();
				if(kindPaidMap.containsKey(key)){
					kindPaidMap.put(key,kindPaidMap.get(key).add(prpLLossPersonVo.getSumRealPay()));
				}else{
					kindPaidMap.put(key,prpLLossPersonVo.getSumRealPay());
				}
			}
		}
		return kindPaidMap;
	}

	/**
	 * 该人员在本案已经核赔费用 （因为PrpLLossPerson表没有存涉案人员的injuerdId,所以只能通过personTraceId去对应，这样涉案人员的id = personTrace.getInjuerdId）
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @param kindCode
	 * @param personTraceId
	 * @return
	 * @modified: ☆ZhouYanBin(2016年5月3日 下午3:14:22): <br>
	 */
	public BigDecimal querySumRealPay4KindAndPerson(String registNo,String kindCode,Long personTraceId) {
		BigDecimal sumRealPay4Kind = BigDecimal.ZERO;
		PrpLCompensateVo query = new PrpLCompensateVo();
		query.setRegistNo(registNo);
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("compensateType","N");
		queryRule.addIn("underwriteFlag",CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE,CodeConstants.UnderWriteFlag.AUTO_UNDERWRITE);
		List<PrpLCompensate> prpLCompensateList = databaseDao.findAll(PrpLCompensate.class,queryRule);
		List<PrpLCompensateVo> prpLCompensateVoList = null;
		if(prpLCompensateList==null||prpLCompensateList.isEmpty()){
			return sumRealPay4Kind;
		}else{
			prpLCompensateVoList = Beans.copyDepth().from(prpLCompensateList).toList(PrpLCompensateVo.class);
		}

		for(PrpLCompensateVo prpLCompensateVo:prpLCompensateVoList){
			List<PrpLLossPersonVo> prpLLossPersonVoList = prpLCompensateVo.getPrpLLossPersons();
			for(PrpLLossPersonVo prpLLossPersonVo:prpLLossPersonVoList){
				if(prpLLossPersonVo.getKindCode().equals(kindCode)&&personTraceId.equals(prpLLossPersonVo.getPersonId())){
					sumRealPay4Kind = sumRealPay4Kind.add(prpLLossPersonVo.getSumRealPay()).add(
							prpLLossPersonVo.getDeductOffAmt().add(prpLLossPersonVo.getOffPreAmt()));
				}
			}
		}

		return sumRealPay4Kind;
	}

	/**
	 * 计算 赔案中该险别的已赔付金额。
	 * @param registNo
	 * @param kindCode
	 * @return
	 */
	public BigDecimal querySumRealPayX(String registNo,String kindCode,String deviceName) {
		BigDecimal sumRealPay4Kind = BigDecimal.ZERO;
		PrpLCompensateVo query = new PrpLCompensateVo();
		query.setRegistNo(registNo);
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addIn("underwriteFlag",CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE,CodeConstants.UnderWriteFlag.AUTO_UNDERWRITE);
		List<PrpLCompensate> prpLCompensateList = databaseDao.findAll(PrpLCompensate.class,queryRule);
		List<PrpLCompensateVo> prpLCompensateVoList = null;
		if(prpLCompensateList==null||prpLCompensateList.isEmpty()){
			return sumRealPay4Kind;
		}else{
			prpLCompensateVoList = Beans.copyDepth().from(prpLCompensateList).toList(PrpLCompensateVo.class);
		}

		for(PrpLCompensateVo prpLCompensateVo:prpLCompensateVoList){
			List<PrpLLossPropVo> prpLLossPropVoList = prpLCompensateVo.getPrpLLossProps();
			for(PrpLLossPropVo prpLLossPropVo:prpLLossPropVoList){
				if("9".equals(prpLLossPropVo.getPropType())){
					if(prpLLossPropVo.getKindCode().equals(kindCode)&&prpLLossPropVo.getLossName().trim().equals(deviceName.trim())){
						// 财产计算
						sumRealPay4Kind = sumRealPay4Kind.add(prpLLossPropVo.getSumRealPay()).add(
								prpLLossPropVo.getDeductOffAmt().add(prpLLossPropVo.getOffPreAmt()));
					}
				}
			}
		}
		return sumRealPay4Kind;
	}

	/**
	 * 理算交强险一分钱调整
	 * @param returnCompensateList
	 * @param calculateType
	 * @return
	 */
	public List<ThirdPartyLossInfo> getThirdPartyLossInfolistBz(CompensateListVo returnCompensateList,String calculateType) {// type compensate 表示理算 ； claim 表示立案
		List<CompensateExp> returnCompensateExpList = returnCompensateList.getCompensateList();
		List<ThirdPartyRecoveryInfo> thirdPartyRecoveryInfoList = returnCompensateList.getThirdPartyRecoveryInfoAry();// 代交强赔付信息
		List<ThirdPartyLossInfo> thirdPartyLossInfos;
		List<ThirdPartyInfo> thirdPartyInfos;
		List<ThirdPartyLossInfo> thirdPartyLossInfolist = new ArrayList<ThirdPartyLossInfo>(0);
		String policyLicenseNo = "";
		// 理算一分钱误差处理 add by chenrong 2013-09-22 begin 比较总的赔付金额存在的误差
		Double thirdPartyLossSumRealPayFour = 0.0000;// 用每个理算大对象的实赔金额之和保留四位小数的进行相加
		Double thirdPartyLossSumRealPayTwo = 0.00;// 用每个理算大对象的实赔金额之和保留两位小数的进行相加
		Double allDiffer = 0.00;// 本次赔付总金额相差金额
		// 理算一分钱误差处理 add by chenrong 2013-09-22 end 比较总的赔付金额存在的误差
		for(CompensateExp exp:returnCompensateExpList){
			thirdPartyLossInfos = exp.getThirdPartyLossInfos();
			thirdPartyInfos = exp.getThirdPartyInfos();
			if( !"claim".equals(calculateType)){
				for(ThirdPartyInfo carInfo:thirdPartyInfos){
					if(carInfo.getSerialNo()==1){
						policyLicenseNo = carInfo.getLicenseNo();
					}
				}
			}
			// 理算一分钱误差处理 add by chenrong 2013-09-22 begin 比较每个理算大对象存在的误差
			Double differ = 0.00;// 保留四位小数相加后的结果四舍五入取小数点后两位-保留两位小数相加之和
			Double thirdPartyLossInfoAllSumRealPayFour = 0.0000;// 该理算大对象的所有损失项实赔金额之和（保留四位小数相加）
			Double thirdPartyLossInfoAllSumRealPayTwo = 0.00;// 该理算大对象的所有损失项实赔金额之和(保留两位小数相加)
			Double thirdPartyLossInfoAllSumRealPay = 0.000;// 防止超限额后再对此理算对象进行一分钱处理
			if(thirdPartyLossInfos!=null&&thirdPartyLossInfos.size()>0){
				for(ThirdPartyLossInfo lossInfo:thirdPartyLossInfos){
					if( !Double.isInfinite(lossInfo.getSumRealPay())&& !Double.isNaN(lossInfo.getSumRealPay())){
						// sumRealPay可能为NaN
						thirdPartyLossInfoAllSumRealPay = thirdPartyLossInfoAllSumRealPay+Datas.round(lossInfo.getSumRealPay(),6);
						thirdPartyLossInfoAllSumRealPayFour = thirdPartyLossInfoAllSumRealPayFour+Datas.round(lossInfo.getSumRealPay(),4);
						Double sumRealPayTemp = new BigDecimal(lossInfo.getSumRealPay()).setScale(2,4).doubleValue();
						thirdPartyLossInfoAllSumRealPayTwo = thirdPartyLossInfoAllSumRealPayTwo+sumRealPayTemp;
						if(sumRealPayTemp>lossInfo.getSumRealPay()){// 五入
							lossInfo.setRoundFlag("2");
						}else if(sumRealPayTemp<lossInfo.getSumRealPay()){// 四舍
							lossInfo.setRoundFlag("1");
						}else{
							lossInfo.setRoundFlag("0");
						}
					}
				}
			}
			thirdPartyLossSumRealPayFour = thirdPartyLossSumRealPayFour+Datas.round(thirdPartyLossInfoAllSumRealPayFour,4);
			thirdPartyLossInfoAllSumRealPayFour = Datas.round(thirdPartyLossInfoAllSumRealPayFour,2);
			thirdPartyLossSumRealPayTwo = thirdPartyLossSumRealPayTwo+thirdPartyLossInfoAllSumRealPayFour;
			thirdPartyLossInfoAllSumRealPayTwo = Datas.round(thirdPartyLossInfoAllSumRealPayTwo,2);
			differ = thirdPartyLossInfoAllSumRealPayFour-thirdPartyLossInfoAllSumRealPayTwo;
			differ = Datas.round(differ,2);
			allDiffer = thirdPartyLossSumRealPayFour-thirdPartyLossSumRealPayTwo;
			allDiffer = Datas.round(allDiffer,2);
			thirdPartyLossInfoAllSumRealPay = Datas.round(thirdPartyLossInfoAllSumRealPay,4);
			if(thirdPartyLossInfoAllSumRealPay>thirdPartyLossInfoAllSumRealPayFour){// 该理算大对象进行了四舍
				if(thirdPartyLossInfos!=null&&thirdPartyLossInfos.size()>0){
					for(ThirdPartyLossInfo lossInfo:thirdPartyLossInfos){
						lossInfo.setRoundFlagExp("1");
					}
				}
			}else if(thirdPartyLossInfoAllSumRealPay<thirdPartyLossInfoAllSumRealPayFour){// 该理算大对象进行了五入
				if(thirdPartyLossInfos!=null&&thirdPartyLossInfos.size()>0){
					for(ThirdPartyLossInfo lossInfo:thirdPartyLossInfos){
						lossInfo.setRoundFlagExp("2");
					}
				}
			}else{
				if(thirdPartyLossInfos!=null&&thirdPartyLossInfos.size()>0){
					for(ThirdPartyLossInfo lossInfo:thirdPartyLossInfos){
						lossInfo.setRoundFlagExp("0");
					}
				}
			}
			// 理算一分钱误差处理 add by chenrong 2013-09-22 end 比较每个理算大对象存在的误差
			for(ThirdPartyLossInfo lossInfo:thirdPartyLossInfos){
				if( !"claim".equals(calculateType)){
					if(CodeConstants.CompensateKind.ADVANCE.equals(String.valueOf(exp.getCompensateType()))){
						lossInfo.setInsteadFlag("0");
					}
				}
				lossInfo.setDamageType(exp.getDamageType());
				if( !Double.isInfinite(lossInfo.getSumRealPay())&& !Double.isNaN(lossInfo.getSumRealPay())){
					// sumRealPay可能为NaN
					Double sumRealPay = new BigDecimal(lossInfo.getSumRealPay()).setScale(2,4).doubleValue();
					// 理算一分钱误差处理 add by chenrong 2013-09-22 begin 比较每个理算大对象存在的误差
					// lossInfo.setSumRealPay(sumRealPay);
					if(differ>0&&lossInfo.getRoundFlag().trim().equals("1")){// 舍得多，将进行舍得加一
						lossInfo.setSumRealPay(sumRealPay+0.01);
						lossInfo.setRoundFlag("0");
						differ = differ-0.01;
					}else if(differ<0&&lossInfo.getRoundFlag().trim().equals("2")){// 进的多，将进行进位的减一
						lossInfo.setSumRealPay(sumRealPay-0.01);
						lossInfo.setRoundFlag("0");
						differ = differ+0.01;
					}else{
						lossInfo.setSumRealPay(sumRealPay);
					}
					// 理算一分钱误差处理 add by chenrong 2013-09-22 end 比较每个理算大对象存在的误差
				}
				lossInfo.setExgratiaFee(returnCompensateList.getExgratiaFee());
				if( !"claim".equals(calculateType)){
					lossInfo.setThirdPartyRecoveryInfoAry(thirdPartyRecoveryInfoList);
					thirdPartyLossInfolist.add(lossInfo);
				}else{
					thirdPartyLossInfolist.add(lossInfo);
				}
			}
		}
		// 理算一分钱误差处理 add by chenrong 2013-09-22 begin 比较总的赔付金额存在的误差
		if(allDiffer!=0){
			for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfolist){
				if(allDiffer>0&&"1".equals(thirdPartyLossInfo.getRoundFlagExp())&&"1".equals(thirdPartyLossInfo.getRoundFlag())){// 舍得多，将进行舍得加一
					thirdPartyLossInfo.setSumRealPay(thirdPartyLossInfo.getSumRealPay()+0.01);
					thirdPartyLossInfo.setRoundFlag("0");
					allDiffer = allDiffer-0.01;
				}else if(allDiffer<0&&"2".equals(thirdPartyLossInfo.getRoundFlagExp())&&"2".equals(thirdPartyLossInfo.getRoundFlag())){// 进得多，将进行进的减一
					thirdPartyLossInfo.setSumRealPay(thirdPartyLossInfo.getSumRealPay()-0.01);
					thirdPartyLossInfo.setRoundFlag("0");
					allDiffer = allDiffer+0.01;
				}
				if(allDiffer==0){
					break;
				}
			}
		}
		// 理算一分钱误差处理 add by chenrong 2013-09-22 end 比较总的赔付金额存在的误差
		return thirdPartyLossInfolist;
	}

	// public PrpLCompensateVo savePrpLcompensate(PrpLCompensateVo prpLCompensateVo,Long taskId,List<PrpLClaimDeductVo> prpLClaimDeductVoList, boolean saveOrSubmit){
	//
	// String registNo = prpLCompensateVo.getRegistNo();
	// logger.info("-->savePrpLcompensate " + registNo);
	//
	// // this.makeCheckSubrogationInfo(registNo);
	// logger.info("完成检查理赔系统中的结算码是否与平台一致");
	//
	// // checkCi4Self(registNo , prpLCompensateVo.getCompensateType());
	// logger.info("完成理算内容");
	//
	// int totalLossCount = 0;
	// if (this.isActualOrExpenseCompensate(prpLCompensateVo.getCompensateKind())) {
	// totalLossCount =
	// prpLCompensateVo.getPrpLLossItems().size() +
	// prpLCompensateVo.getPrpLLossProps().size() +
	// prpLCompensateVo.getPrpLLossPersons().size();
	// } else {
	// totalLossCount = prpLCompensateVo.getPrpLCharges().size();
	// }
	// //保存计算书时，需要有理算项。
	// Assert.isTrue(totalLossCount > 0 , "error.compensate.noneitem");
	// //目前其他理算项中只能录入代步车险。
	// // Assert.isTrue(prpLCompensateVo.getPrplotherlosses().size() <= 1 , "error.compensate.onlyoneotherloss");
	//
	// //判断计算书的预赔金额和理算项和是否一致，不一致，异常提示。
	// this.checkPrePaidSummary(prpLCompensateVo);
	// logger.info("完成判断计算书的预赔金额和理算项和是否一致");
	// //处理。
	// //将当前操作人设置为理算操作人。
	// // PrpDuser currentPrpDuser = Users.getCurrentPrpDuser();
	// prpLCompensateVo.setCreateUser(SecurityUtils.getUserCode());
	//
	// //无计算书号情况下生成计算书号和业务ID。
	// if (prpLCompensateVo.getCompensateNo() == null || "".equals(prpLCompensateVo.getCompensateNo())) {
	// PrpLCMainVo prpLcMainVo = policyViewService.getPolicyInfo(registNo,prpLCompensateVo.getPolicyNo());
	// String compensateNo = billNoService.getCompensateNo(prpLcMainVo.getComCode(),prpLcMainVo.getRiskCode());
	// prpLCompensateVo.setCompensateNo(compensateNo);
	// // prpLCompensateVo.setBusinessId(new Long(super.makeSequence(ClaimConst.SeqName.prpLCompensateVo.value)));
	//
	// List<PrpLLossPersonVo> prpLLossPersonVoList = prpLCompensateVo.getPrpLLossPersons();
	// for (PrpLLossPersonVo prpLLossPersonVo : prpLLossPersonVoList) {
	// List<PrpLLossPersonFeeVo> prpLLossPersonFeeVoList = prpLLossPersonVo.getPrpLLossPersonFees();
	// for (PrpLLossPersonFeeVo prpLLossPersonFeeVo : prpLLossPersonFeeVoList) {
	// prpLLossPersonFeeVo.setCompensateNo(prpLCompensateVo.getCompensateNo());
	// }
	// }
	// logger.info("无计算书号情况下生成计算书号和业务ID");
	// }
	//
	// String compensateKind= prpLCompensateVo.getCompensateKind();
	// List<PrpLLossItemVo> prpLLossItemVoList = prpLCompensateVo.getPrpLLossItems();
	// for (PrpLLossItemVo prpLLossItemVo : prpLLossItemVoList) {
	// PrpLDlossCarMainVo prpLDlossCarMainVo = lossCarService.findLossCarMainById(prpLLossItemVo.getDlossId());
	// prpLDlossCarMainVo.setLossState(this.makeLossStateCompensated(prpLDlossCarMainVo, compensateKind));
	// lossCarService.updateDlossCarMain(prpLDlossCarMainVo);
	//
	// }
	// logger.info("Save -- prpLlosses");
	//
	// List<PrpLLossPropVo> lossPropList = new ArrayList<PrpLLossPropVo>();
	// List<PrpLLossPropVo> otherLossList = new ArrayList<PrpLLossPropVo>();
	// List<PrpLLossPropVo> prpLpropLosses = prpLCompensateVo.getPrpLLossProps();
	// for (PrpLLossPropVo prpLLossPropVo : prpLpropLosses) {
	// if("1".equals(prpLLossPropVo.getPropType())){
	// PrpLdlossPropMainVo prpLdlossPropMainVo = propTaskService.findPropMainVoById(prpLLossPropVo.getDlossId());
	// prpLdlossPropMainVo.setLossState(this.makeLossStateCompensated(prpLdlossPropMainVo, compensateKind));
	// propTaskService.updateDLossProp(prpLdlossPropMainVo);
	// }else if("9".equals(prpLLossPropVo.getPropType())){
	//
	// }
	// }
	// logger.info("Save -- prpLpropLosses");
	//
	// List<PrpLLossPersonVo> prpLLossPersonVoList = prpLCompensateVo.getPrpLLossPersons();
	// for (PrpLLossPersonVo prpLLossPersonVo : prpLLossPersonVoList) {
	// PrpLDlossPersTraceVo prpLDlossPersTraceVo = persTraceDubboService.findPersTraceByPK(prpLLossPersonVo.getDlossId());
	// prpLDlossPersTraceVo.setLossState(this.makeLossStateCompensated(prpLDlossPersTraceVo, compensateKind));
	// persTraceDubboService.update(prpLDlossPersTraceVo);
	// }
	// logger.info("Save -- prpLpersonItems");
	//
	// // List<PrpLChargeVo> prpLChargeVoList = prpLCompensateVo.getPrpLCharges();
	// // this.reCalChargeFee(prpLChargeVoList);
	// logger.info("Save -- prpLcharges");
	//
	// //商业险赔款计算书才有可选免赔选项。
	// //zhongyuhai 20121011
	// // if(this.isCommercialOrCompulsory(prpLCompensateVo.getCompensateKind()) &&
	// // this.isActualOrExpenseCompensate(prpLCompensateVo.getCompensateKind()) &&
	// // prpLClaimDeductVoList != null){
	// // super.saveAll(prpLdeductinfoList);
	// // logger.info("商业险赔款计算书才有可选免赔选项");
	// // }
	//
	// fillSumBzPaid_SumPaid_SumThisPaid_SumNoDeductFee4Payment(prpLCompensateVo);
	// logger.info("完成计算计算书中的交强已赔付金额、本次赔付金额、总赔付金额、不计免赔金额");
	//
	// this.savePrpLCompensate(prpLCompensateVo);
	// logger.info("save - prpLCompensateVo");
	//
	// //自动生成公估费金额拆分记录。
	// // splitMoneyService.savePrpLpayeeFeeOfSF32(prpLCompensateVo);
	// logger.info("save - 自动生成公估费金额拆分记录");
	//
	// saveBpmInfo(prpLCompensateVo, taskId);
	// logger.info("完成保存工作流");
	//
	// //由于金额拆分需要依赖理算附表的数据，所以需要刷新理算附表数据保证其id已经生成，才能调用金额拆分的相关方法。
	// //zhongyuhai 20120814
	// super.flush();
	// logger.info("由于金额拆分需要依赖理算附表的数据，所以需要刷新理算附表数据保证其id已经生成，才能调用金额拆分的相关方法");
	//
	// //提交时需要先进行金额拆分，否则无法送收付费。
	// this.splitMoneyService.updatePrpLpayeeFeeOfSumRealPay(prpLCompensateVo);
	// logger.info("提交时需要先进行金额拆分，否则无法送收付费");
	//
	// if(!saveOrSubmit){
	// //校验是否所有金额拆分记录都选择了归属人。
	// Assert.isTrue(this.splitMoneyService.isByAllSplit(prpLCompensateVo), "claim.biz.label.checksplit");
	// logger.info("完成校验是否所有金额拆分记录都选择了归属人");
	// //商业险实赔计算书。
	// if (this.isCommercialOrCompulsory(compensateType) && this.isActualOrExpenseCompensate(compensateType)) {
	// List<PrpLclaim> prpLclaimTempList = claimService.findClaimByRegistNo(registNo);
	// Boolean tempFlag = false; //标志位，记录该商业实赔计算书是否有一起报案的交强立案，而且该交强立案是否被拒赔
	// if (prpLclaimTempList.size() > 1) { //存在两个立案，就是交强和商业关联报案
	// for (PrpLclaim prpLclaimTemp : prpLclaimTempList) {
	// String policyNO = prpLclaimTemp.getPolicyNo();
	// String policyType =
	// registService.findPrpLregistSummaryByRegistNo(prpLclaimTemp.getRegistNo(), policyNO).getPolicyType();
	// if (ClaimConst.PolicyType.compulsory.typeFactor.equals(policyType)) { //如果是交强立案
	// if (ClaimConst.CaseType.Refuse.value.equals(prpLclaimTemp.getCaseType())) { //并且是拒赔案件
	// tempFlag = true;
	// }
	// }
	// }
	// }
	// logger.info("tempFlag = " + tempFlag);
	// if (this.isCompulsoryAddCommercial(registNo) && !tempFlag) {
	// //商业险实赔计算书需要在已经存在已核赔交强计算书的条件下才能提交核赔。交强核赔工作流必须全部核赔通过。
	// //zhongyuhai 20121211
	// logger.info("商业险实赔计算书需要在已经存在已核赔交强计算书的条件下才能提交核赔。交强核赔工作流必须全部核赔通过");
	// List<PrpLbpmMain> compensateBpmUndwrt =
	// bpmMainService.findPrpLbpmMainsByMainNoAndNodeId(registNo, BpmConst.BpmNodeType.Undwrt.nodeType);
	// List<PrpLbpmMain> compensateBpmUndwrtOne =
	// bpmMainService.findPrpLbpmMainsByMainNoAndNodeId(registNo,
	// BpmConst.BpmNodeType.UndwrtOne.nodeType);
	// List<PrpLbpmMain> compensateBpmUndwrtTwo =
	// bpmMainService.findPrpLbpmMainsByMainNoAndNodeId(registNo,
	// BpmConst.BpmNodeType.UndwrtTwo.nodeType);
	// List<PrpLbpmMain> compensateBpmUndwrtThree =
	// bpmMainService.findPrpLbpmMainsByMainNoAndNodeId(registNo,
	// BpmConst.BpmNodeType.UndwrtThree.nodeType);
	// List<PrpLbpmMain> compensateBpmUndwrtFour =
	// bpmMainService.findPrpLbpmMainsByMainNoAndNodeId(registNo,
	// BpmConst.BpmNodeType.UndwrtFour.nodeType);
	// List<PrpLbpmMain> compensateBpmUndwrtFive =
	// bpmMainService.findPrpLbpmMainsByMainNoAndNodeId(registNo,
	// BpmConst.BpmNodeType.UndwrtFive.nodeType);
	//
	// List<PrpLbpmMain> undwrtBpmList = new ArrayList<PrpLbpmMain>();
	// undwrtBpmList.addAll(compensateBpmUndwrt);
	// undwrtBpmList.addAll(compensateBpmUndwrtOne);
	// undwrtBpmList.addAll(compensateBpmUndwrtTwo);
	// undwrtBpmList.addAll(compensateBpmUndwrtThree);
	// undwrtBpmList.addAll(compensateBpmUndwrtFour);
	// undwrtBpmList.addAll(compensateBpmUndwrtFive);
	//
	// boolean isAllCompulsoryFinished = true;
	// List<PrpLbpmMain> undwrtBpmList4Compulsory = new ArrayList<PrpLbpmMain>();
	// for (PrpLbpmMain main : undwrtBpmList) {
	// if (BpmConst.BusinessType.C.value.equals(main.getBusinessType()) ||
	// BpmConst.BusinessType.D.value.equals(main.getBusinessType())) {
	// undwrtBpmList4Compulsory.add(main);
	//
	// if (!BpmConst.TaskState.Handlered.value.equals(main.getState())) {
	// isAllCompulsoryFinished = false;
	// break;
	// }
	// }
	// }
	// logger.info("isAllCompulsoryFinished = " + isAllCompulsoryFinished);
	// //存在核赔工作流，而且全部核赔通过。
	// //zhongyuhai 20121226
	// Assert.isTrue(isAllCompulsoryFinished && !undwrtBpmList4Compulsory.isEmpty(),
	// "error.compensate.underwriteCompulsoryAcutalPayment");
	// }
	// }
	// }
	// logger.info("<--savePrpLcompensate " + registNo);
	// return prpLcompensate;
	//
	// }

	/**
	 * 将定损对象的lossState属性根据compensateType改为已理算状态。
	 * @param defLossItem
	 * @param compensateType
	 * @return
	 */
	private String makeLossStateCompensated(Object defLossItem,String compensateKind) {
		Assert.notNull(defLossItem);
		Assert.notNull(compensateKind);
		String lossState = (String)Refs.get(defLossItem,"lossState");
		Assert.notNull(lossState);
		// 正常情况下lossState字段长度等于2。
		Assert.isTrue(lossState.length()==2);

		PrpLCompensateVo prpLCompensateVo = new PrpLCompensateVo();
		prpLCompensateVo.setCompensateKind(compensateKind);
		boolean isCommercialOrCompulsory = this.isCommercialOrCompulsory(compensateKind);

		String newLossState = null;
		final String ci = lossState.substring(0,1);
		final String bi = lossState.substring(1,2);
		if(isCommercialOrCompulsory){
			newLossState = ci+"1";
		}else{
			newLossState = "1"+bi;
		}

		return newLossState;
	}

	/**
	 * 保存数据
	 * 
	 * <pre></pre>
	 * @param prpLCompensateVo
	 * @modified: ☆ZhouYanBin(2016年5月3日 下午5:06:10): <br>
	 */
	public void savePrpLCompensate(PrpLCompensateVo prpLCompensateVo) {
		PrpLCompensate prpLCompensate = databaseDao.findByPK(PrpLCompensate.class,prpLCompensateVo.getCompensateNo());
		if(prpLCompensate!=null){
			Beans.copy().from(prpLCompensateVo).excludeNull().to(prpLCompensate);
			databaseDao.update(PrpLCompensate.class,prpLCompensate);
		}else{
			prpLCompensate = Beans.copyDepth().from(prpLCompensateVo).to(PrpLCompensate.class);
			databaseDao.save(PrpLCompensate.class,prpLCompensate);
		}

	}

	/**
	 * 查询已经冲减的保额
	 * 
	 * <pre></pre>
	 * @param policyNo
	 * @return
	 * @modified: ☆ZhouYanBin(2016年5月4日 上午10:42:16): <br>
	 */
	public BigDecimal queryEmotorKindLossfee(String policyNo,String kindCode) {
		BigDecimal endorAmount = BigDecimal.ZERO;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("policyNo",policyNo);
		List<PrpLEndor> prpLEndorList = databaseDao.findAll(PrpLEndor.class,queryRule);
		if(prpLEndorList!=null&& !prpLEndorList.isEmpty()){
			for(PrpLEndor prpLEndor:prpLEndorList){
				if(kindCode.equals(prpLEndor.getKindCode())){
					endorAmount = endorAmount.add(prpLEndor.getEndorAmount());
				}
			}
		}
		return endorAmount;
	}

	/**
	 * 保单冲销的金额
	 * @param policyNo
	 * @return
	 * @modified: ☆YangKun(2016年7月8日 下午2:19:48): <br>
	 */
	public List<PrpLEndor> queryEmotorLoss(String policyNo) {
		BigDecimal endorAmount = BigDecimal.ZERO;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("policyNo",policyNo);
		List<PrpLEndor> prpLEndorList = databaseDao.findAll(PrpLEndor.class,queryRule);

		return prpLEndorList;
	}

	public Map<String,BigDecimal> queryEmotorMap(String policyNo) {
		Map<String,BigDecimal> eMotorMap = new HashMap<String,BigDecimal>();
		// 查询累加新理赔系统的险别赔付金额
		List<PrpLEndor> endorList = this.queryEmotorLoss(policyNo);
		if(endorList!=null&& !endorList.isEmpty()){
			for(PrpLEndor endorVo:endorList){
				String key = endorVo.getKindCode();
				if(eMotorMap.containsKey(key)){
					eMotorMap.put(key,eMotorMap.get(key).add(endorVo.getEndorAmount()));
				}else{
					eMotorMap.put(key,endorVo.getEndorAmount());
				}
			}
		}
		// 查询累加旧理赔系统的险别赔付金额
		List<PrplOldClaimRiskInfoVo> oldClaimRiskInfoList = registService.findPrploldclaimriskinfoByPolicyNo(policyNo);
		if(oldClaimRiskInfoList!=null&& !oldClaimRiskInfoList.isEmpty()){
			for(PrplOldClaimRiskInfoVo oldClaimRiskInfoVo:oldClaimRiskInfoList){
				if(eMotorMap.containsKey(CodeConstants.KINDCODE.KINDCODE_C)){
					eMotorMap.put(CodeConstants.KINDCODE.KINDCODE_C,
							eMotorMap.get(CodeConstants.KINDCODE.KINDCODE_C).add(DataUtils.NullToZero(oldClaimRiskInfoVo.getKindCPayment())));
				}
				if(eMotorMap.containsKey(CodeConstants.KINDCODE.KINDCODE_L)){
					eMotorMap.put(CodeConstants.KINDCODE.KINDCODE_L,
							eMotorMap.get(CodeConstants.KINDCODE.KINDCODE_L).add(DataUtils.NullToZero(oldClaimRiskInfoVo.getKindLPayment())));
				}
				if(eMotorMap.containsKey(CodeConstants.KINDCODE.KINDCODE_RF)){
					eMotorMap.put(CodeConstants.KINDCODE.KINDCODE_RF,
							eMotorMap.get(CodeConstants.KINDCODE.KINDCODE_RF).add(DataUtils.NullToZero(oldClaimRiskInfoVo.getKindRFPayment())));
				}
				if(eMotorMap.containsKey(CodeConstants.KINDCODE.KINDCODE_T)){
					eMotorMap.put(CodeConstants.KINDCODE.KINDCODE_T,
							eMotorMap.get(CodeConstants.KINDCODE.KINDCODE_T).add(DataUtils.NullToZero(oldClaimRiskInfoVo.getKindTPayment())));
				}
				if(eMotorMap.containsKey(CodeConstants.KINDCODE.KINDCODE_Z2)){
					eMotorMap.put(CodeConstants.KINDCODE.KINDCODE_Z2,
							eMotorMap.get(CodeConstants.KINDCODE.KINDCODE_Z2).add(DataUtils.NullToZero(oldClaimRiskInfoVo.getKindZPayment())));
				}
			}
		}

		return eMotorMap;
	}

	/**
	 * 写入冲减保额表
	 * @param
	 * @author lanlei
	 */
	public void savePrpLEndor(String compensateNo,SysUserVo userVo) {
		PrpLCompensateVo comp = findCompByPK(compensateNo);
		String wfFlag="0";//是否冲销
		if(DataUtils.NullToZero(comp.getSumAmt()).compareTo(new BigDecimal(0))<0) {
			if(comp!=null && comp.getPrpLCompensateExt()!=null) {
				if(StringUtils.isNotBlank(comp.getPrpLCompensateExt().getOppoCompensateNo())) {
					comp = findCompByPK(comp.getPrpLCompensateExt().getOppoCompensateNo());
					wfFlag="1";//是冲销
				}
				
			}
		}
		// 查询理算其他损失表的数据，根据险别写入冲减保额表中
		List<PrpLLossPropVo> lossPropList = new ArrayList<PrpLLossPropVo>();
		lossPropList = comp.getPrpLLossProps();
		List<PrpLLossItemVo> lossItemList = comp.getPrpLLossItems();
		String user = userVo.getUserCode();// 获取用户
		Date date = new Date();
		List<PrpLEndorVo> endorVoList = new ArrayList<PrpLEndorVo>();
		if(lossPropList.size()>0){
			for(PrpLLossPropVo lossProp:lossPropList){
				if(lossProp.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_L)||lossProp.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_C)||lossProp
						.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_RF) || (lossProp.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_W1) && CodeConstants.ISNEWCLAUSECODE2020_MAP.get(lossProp.getRiskCode()))){
					PrpLEndorVo endorVo = new PrpLEndorVo();
					endorVo.setClaimNo(comp.getClaimNo());
					endorVo.setCompensateNo(comp.getCompensateNo());
					endorVo.setPolicyNo(comp.getPolicyNo());
					endorVo.setKindCode(lossProp.getKindCode());
					if("1".equals(wfFlag)) {
						endorVo.setEndorAmount(DataUtils.NullToZero(lossProp.getSumRealPay()).multiply(new BigDecimal(-1)));
					}else {
						endorVo.setEndorAmount(lossProp.getSumRealPay());
					}
					endorVo.setInputTime(date);
					endorVo.setValidFlag(CodeConstants.ValidFlag.Y);
					endorVo.setCreateTime(date);
					endorVo.setCreateUser(user);
					endorVo.setUpdateTime(date);
					endorVo.setUpdateUser(user);

					endorVoList.add(endorVo);
				}
			}
		}
		if(lossItemList!=null&& !lossItemList.isEmpty()){
			for(PrpLLossItemVo itemVo:lossItemList){
				if(itemVo.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_L)
						|| (itemVo.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_W1) && CodeConstants.ISNEWCLAUSECODE2020_MAP.get(itemVo.getRiskCode()))){
					PrpLEndorVo endorVo = new PrpLEndorVo();
					endorVo.setClaimNo(comp.getClaimNo());
					endorVo.setCompensateNo(comp.getCompensateNo());
					endorVo.setPolicyNo(comp.getPolicyNo());
					endorVo.setKindCode(itemVo.getKindCode());
					if("1".equals(wfFlag)) {
						endorVo.setEndorAmount(DataUtils.NullToZero(itemVo.getSumRealPay()).multiply(new BigDecimal(-1)));
					}else{
						endorVo.setEndorAmount(itemVo.getSumRealPay());
					}
					endorVo.setInputTime(date);
					endorVo.setValidFlag(CodeConstants.ValidFlag.Y);
					endorVo.setCreateTime(date);
					endorVo.setCreateUser(user);
					endorVo.setUpdateTime(date);
					endorVo.setUpdateUser(user);

					endorVoList.add(endorVo);

				}
			}
		}
		if(endorVoList!=null&& !endorVoList.isEmpty()){
			List<PrpLEndor> endorList = Beans.copyDepth().from(endorVoList).toList(PrpLEndor.class);
			databaseDao.saveAll(PrpLEndor.class,endorList);
		}

	}

	//
	/**
	 * 1-(1-事故责任免赔率)*(1-可选免赔率)-可选免赔率
	 * @param registNo
	 * @param kindCode
	 * @param DeductDutyRate 事故责任免赔率
	 * @param deductAddRate 可选免赔率
	 * @return
	 * @modified: ☆YangKun(2016年5月12日 下午5:24:36): <br>
	 */
	private double deductOffRate(String registNo,String kindCode,BigDecimal deductDutyRate,BigDecimal deductAddRate) {
		boolean isMitemKind = policyViewService.isMKindCode(registNo,kindCode);
		if(deductDutyRate==null){
			deductDutyRate = BigDecimal.ZERO;
		}
		if(deductAddRate==null){
			deductAddRate = BigDecimal.ZERO;
		}
		double deductOffRate = 0d;
		if(isMitemKind){
			if(CodeConstants.KINDCODE.KINDCODE_G.equals(kindCode)){
				deductOffRate = deductDutyRate.doubleValue();
			}else{
				deductOffRate = 100-( 100-deductDutyRate.doubleValue() )*( 100-deductAddRate.doubleValue() )/100-deductAddRate.doubleValue();
			}
		}
		deductOffRate = deductOffRate/100;

		return deductOffRate;
	}

	/**
	 * 获取承保不计免赔险别信息
	 * @param prpLcitemKinds 承保险别信息
	 * @return 已承保不计免赔险别信息
	 */
	public List<PrpLCItemKindVo> getIsMitemKinds(List<PrpLCItemKindVo> prpLcitemKinds,Boolean newRegist) {
		List<PrpLCItemKindVo> isMitemkinds = new ArrayList<PrpLCItemKindVo>(0);
		// 是否承保M险
		boolean isM = false;

		Map<String,PrpLCItemKindVo> itemMap = new HashMap<String,PrpLCItemKindVo>();
		for(PrpLCItemKindVo prpLcItemKind:prpLcitemKinds){
			itemMap.put(prpLcItemKind.getKindCode(),prpLcItemKind);
		}

		for(PrpLCItemKindVo itemKindVo:prpLcitemKinds){
			if(newRegist){
				String kindCodeM = itemKindVo.getKindCode()+"M";
				if(itemMap.containsKey(kindCodeM)){
					isMitemkinds.add(itemKindVo);
				}
			}else{
				// 截取FLAG标志位第5位,如果为1则已投保不计算免赔,flag不能为空,为空的问题数据需要兼容.
				if(itemKindVo.getFlag()!=null&&"1".equals(itemKindVo.getFlag().substring(CodeConstants.ISM_FLAG_BEGIN,CodeConstants.ISM_FLAG_END))){
					isMitemkinds.add(itemKindVo);
				}
			}
			// 判断是否投保了M险
			if("M".equals(itemKindVo.getKindCode())){
				isM = true;
			}
		}
		if( !newRegist&& !isM){
			return new ArrayList<PrpLCItemKindVo>(0);
		}
		return isMitemkinds;
	}

	public void mergeList(PrpLCompensate compensate,List voList,List poList,String idName,Class paramClass,String method) {
		Map<Object,Object> map = new HashMap<Object,Object>();
		Map<Integer,Object> keyMap = new HashMap<Integer,Object>();
		Map<Object,Object> poMap = new HashMap<Object,Object>();

		for(int i = 0,count = voList.size(); i<count; i++ ){
			Object element = voList.get(i);
			if(element==null){
				continue;
			}
			Object key;
			try{
				key = PropertyUtils.getProperty(element,idName);
				map.put(key,element);
				keyMap.put(i,key);
			}catch(Exception e){
				throw new IllegalArgumentException(e);
			}
		}

		for(Iterator it = poList.iterator(); it.hasNext();){
			Object element = (Object)it.next();
			try{
				Object key = PropertyUtils.getProperty(element,idName);
				poMap.put(key,null);
				if( !map.containsKey(key)){
					// delete(element);
					databaseDao.deleteByObject(paramClass,element);
					it.remove();
				}else{
					Beans.copy().from(map.get(key)).excludeNull().to(element);
				}
			}catch(Exception e){
				throw new IllegalArgumentException(e);
			}
		}
		for(int i = 0,count = voList.size(); i<count; i++ ){
			Object element = voList.get(i);
			if(element==null){
				continue;
			}
			try{
				Object poElement = paramClass.newInstance();
				Object key = keyMap.get(i);
				if(key==null|| !poMap.containsKey(key)){
					Method setMethod;
					Beans.copy().from(element).to(poElement);
					setMethod = paramClass.getDeclaredMethod(method,compensate.getClass());
					setMethod.invoke(poElement,compensate);

					poList.add(poElement);
				}
			}catch(Exception e){
				throw new IllegalArgumentException(e);
			}
		}
	}

	/**
	 * 预付注销
	 * @param taskId
	 */
	public void prePayCancel(String taskId,SysUserVo user) {
		BigDecimal bd = new BigDecimal(taskId);
		Date date = new Date();
		wfTaskHandleService.cancelTask(user.getUserCode(),bd);
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(Double.valueOf(taskId));
		PrpLCompensate prpLCompensate = new PrpLCompensate();
		if(wfTaskVo!=null){
			PrpLCompensateVo compensateVo = this.findCompByPK(wfTaskVo.getCompensateNo());
			if(compensateVo==null){
				prpLCompensate = this.setPrpLCompensate(prpLCompensate,wfTaskVo,user);
				databaseDao.save(PrpLCompensate.class,prpLCompensate);
			}else{
				PrpLCompensate comPo = databaseDao.findByPK(PrpLCompensate.class,wfTaskVo.getCompensateNo());
				comPo.setUnderwriteFlag("7");
				comPo.setUnderwriteDate(date); 
				comPo.setUnderwriteUser(user.getUserCode());
				if(StringUtils.isNotBlank(wfTaskVo.getCompensateNo())){
					this.updateCompByOppCompensateVo(wfTaskVo.getCompensateNo());
				}
				// databaseDao.update(PrpLCompensate.class, prpLCompensate);
			}
		}
	}

	public PrpLCompensate setPrpLCompensate(PrpLCompensate prpLCompensate,PrpLWfTaskVo wfTaskVo,SysUserVo user) {
		Date date = new Date();
		PrpLCompensateVo compensateVo = this.findCompByClaimNo(wfTaskVo.getClaimNo(),"Y");
		prpLCompensate.setCompensateNo(wfTaskVo.getCompensateNo());
		prpLCompensate.setClaimNo(wfTaskVo.getClaimNo());
		prpLCompensate.setRegistNo(wfTaskVo.getRegistNo());
		prpLCompensate.setComCode(user.getComCode());
		prpLCompensate.setRiskCode(wfTaskVo.getRiskCode());
		prpLCompensate.setUnderwriteFlag("7");
		prpLCompensate.setCreateTime(wfTaskVo.getTaskInTime());
		prpLCompensate.setCreateUser(wfTaskVo.getTaskInUser());
		prpLCompensate.setUpdateTime(date);
		prpLCompensate.setUnderwriteDate(date);
		prpLCompensate.setUnderwriteUser(user.getUserCode());
		prpLCompensate.setCompensateType("Y");

		if(compensateVo!=null&&compensateVo.getRegistNo()!=null){
			prpLCompensate.setPolicyNo(compensateVo.getPolicyNo());
			prpLCompensate.setCurrency(compensateVo.getCurrency());
			prpLCompensate.setCaseType(compensateVo.getCaseType());
			prpLCompensate.setIndemnityDuty(compensateVo.getIndemnityDuty());
			prpLCompensate.setIndemnityDutyRate(compensateVo.getIndemnityDutyRate());
			prpLCompensate.setTimes(compensateVo.getTimes()+1);
			prpLCompensate.setDeductType(compensateVo.getDeductType());
			prpLCompensate.setMakeCom(compensateVo.getComCode());
		}else{
			PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(wfTaskVo.getClaimNo());
			// 获取保单信息
			PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(prpLClaimVo.getRegistNo(),prpLClaimVo.getPolicyNo());
			// 获取报案信息
			PrpLRegistVo prpLregistVo = registQueryService.findByRegistNo(prpLClaimVo.getRegistNo());
			PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(prpLClaimVo.getRegistNo());
			PrpLCheckDutyVo prpLCheckDutyVo = checkTaskService.findCheckDuty(prpLClaimVo.getRegistNo(),1);
			List<PrpLCompensateVo> comList = this.findCompListByClaimNo(wfTaskVo.getClaimNo(),"Y");

			String caseType = "1";
			if("1".equals(checkVo.getIsClaimSelf())){
				caseType = "2";
			}else if("1".equals(checkVo.getIsSubRogation())){
				caseType = "3";
			}

			prpLCompensate.setCurrency("1");
			prpLCompensate.setPolicyNo(prpLClaimVo.getPolicyNo());
			prpLCompensate.setMakeCom(prpLCMainVo.getComCode());
			prpLCompensate.setCaseType(caseType);
			prpLCompensate.setDeductType("1");
			if("1101".equals(prpLClaimVo.getRiskCode())){
				if("0".equals(prpLCheckDutyVo.getCiDutyFlag())){
					prpLCompensate.setIndemnityDuty("0");
					prpLCompensate.setIndemnityDutyRate(new BigDecimal(0));
				}else{
					prpLCompensate.setIndemnityDuty("1");
					prpLCompensate.setIndemnityDutyRate(new BigDecimal(100));
				}
			}else{
				prpLCompensate.setIndemnityDuty(prpLCheckDutyVo.getIndemnityDuty());
				prpLCompensate.setIndemnityDutyRate(prpLCheckDutyVo.getIndemnityDutyRate());
			}
			if(comList!=null&&comList.size()>0){
				prpLCompensate.setTimes(comList.size()+1);
			}else{
				prpLCompensate.setTimes(1);
			}
		}
		return prpLCompensate;
	}

	public void setLeftAmount(CompensateListVo compensateListVo,CompensateExp compensateExp) {
		List<PrpLCheckDutyVo> checkDutyList = compensateListVo.getCheckDutyList();
		Map<String,Double> leftAmountMap = new HashMap<String,Double>();
		double carLeftAmount = 0d;
		double medLeftAmount = 0d;
		double dehLeftAmount = 0d;
		for(PrpLCheckDutyVo checkDutyVo:checkDutyList){
			String serialNo = checkDutyVo.getSerialNo()+"";
			boolean ciDuty = false;
			if("1".equals(checkDutyVo.getCiDutyFlag())){
				ciDuty = true;
			}

			if("0".equals(compensateListVo.getIsBiPCi())){// 交强
				if(checkDutyVo.getCiCarLeftAmount()!=null){
					carLeftAmount = checkDutyVo.getCiCarLeftAmount().doubleValue();
				}else{
					//carLeftAmount = CalculatorCI.calBzAmount(CodeConstants.FeeTypeCode.PROPLOSS,ciDuty);
					carLeftAmount = configService.calBzAmount(CodeConstants.FeeTypeCode.PROPLOSS,ciDuty,checkDutyVo.getRegistNo());
				}

				if(checkDutyVo.getCiMedLeftAmount()!=null){
					medLeftAmount = checkDutyVo.getCiMedLeftAmount().doubleValue();
				}else{
					//medLeftAmount = CalculatorCI.calBzAmount(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES,ciDuty);
					medLeftAmount = configService.calBzAmount(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES,ciDuty,checkDutyVo.getRegistNo());
				}

				if(checkDutyVo.getCiDehLeftAmount()!=null){
					dehLeftAmount = checkDutyVo.getCiDehLeftAmount().doubleValue();
				}else{
					//dehLeftAmount = CalculatorCI.calBzAmount(CodeConstants.FeeTypeCode.PERSONLOSS,ciDuty);
					dehLeftAmount = configService.calBzAmount(CodeConstants.FeeTypeCode.PERSONLOSS,ciDuty,checkDutyVo.getRegistNo());
				}
			}else{
				if(checkDutyVo.getBiCarLeftAmount()!=null){
					carLeftAmount = checkDutyVo.getBiCarLeftAmount().doubleValue();
				}else{
					//carLeftAmount = CalculatorCI.calBzAmount(CodeConstants.FeeTypeCode.PROPLOSS,ciDuty);
					carLeftAmount = configService.calBzAmount(CodeConstants.FeeTypeCode.PROPLOSS,ciDuty,checkDutyVo.getRegistNo());
				}

				if(checkDutyVo.getBiMedLeftAmount()!=null){
					medLeftAmount = checkDutyVo.getBiMedLeftAmount().doubleValue();
				}else{
					//medLeftAmount = CalculatorCI.calBzAmount(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES,ciDuty);
					medLeftAmount = configService.calBzAmount(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES,ciDuty,checkDutyVo.getRegistNo());
				}

				if(checkDutyVo.getBiDehLeftAmount()!=null){
					dehLeftAmount = checkDutyVo.getBiDehLeftAmount().doubleValue();
				}else{
					//dehLeftAmount = CalculatorCI.calBzAmount(CodeConstants.FeeTypeCode.PERSONLOSS,ciDuty);
					dehLeftAmount = configService.calBzAmount(CodeConstants.FeeTypeCode.PERSONLOSS,ciDuty,checkDutyVo.getRegistNo());
				}
			}
			leftAmountMap.put("car-"+serialNo,carLeftAmount);
			leftAmountMap.put("med-"+serialNo,medLeftAmount);
			leftAmountMap.put("deh-"+serialNo,dehLeftAmount);
		}

		List<ThirdPartyInfo> thirdPartyInfos = compensateExp.getThirdPartyInfos();
		for(ThirdPartyInfo thirdPartyInfo:thirdPartyInfos){
			if(compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)){
				thirdPartyInfo.setDutyAmount(leftAmountMap.get("car-"+thirdPartyInfo.getSerialNo()));

			}else if(compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES)){
				thirdPartyInfo.setDutyAmount(leftAmountMap.get("med-"+thirdPartyInfo.getSerialNo()));

			}else if(compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PERSONLOSS)){
				thirdPartyInfo.setDutyAmount(leftAmountMap.get("deh-"+thirdPartyInfo.getSerialNo()));
			}

		}
	}

	public VerifyClaimRuleVo organizeRuleVo(String compensateId,SysUserVo userVo) {
		VerifyClaimRuleVo ruleVo = new VerifyClaimRuleVo();
		PrpLCompensateVo compensateVo = this.findCompByPK(compensateId);
		Double sumAmt = Math.abs(compensateVo.getSumPaidAmt().add(compensateVo.getSumPaidFee()).doubleValue());
		ruleVo.setSumAmt(sumAmt);
		ruleVo.setComCode(userVo.getComCode());
		if("1101".equals(compensateVo.getRiskCode())){
			ruleVo.setClassCode("11");
		}else{
			ruleVo.setClassCode("12");
		}
		ruleVo = claimRuleApiService.compToVClaim(ruleVo);
		// 保存核赔审核需要的级别到理算扩展表
		if(ruleVo.getBackLevel()>ruleVo.getMaxLevel()){
			compensateVo.getPrpLCompensateExt().setSubLevel(ruleVo.getMaxLevel());
		}else{
			compensateVo.getPrpLCompensateExt().setSubLevel(ruleVo.getBackLevel());
		}
		this.saveOrUpdateCompensateVo(compensateVo,userVo);
		// if(ruleVo.getBackLevel()>8){
		// ruleVo.setBackLevel(8);
		// }
		return ruleVo;
	}

	public void saveClaimCalLog(String registNo,String claimNo,String lcText) {
		logger.info("报案号={},进入保存ClaimCalLog方法",registNo);
		ClaimCalLog log = new ClaimCalLog();
		log.setClaimNo(claimNo);
		log.setRegistNo(registNo);
		log.setLctext(lcText);
		log.setCreateTime(new Date());
		databaseDao.save(ClaimCalLog.class,log);
		logger.info("报案号={},结束保存ClaimCalLog方法",registNo);
	}

	/**
	 * 获取车辆的险别
	 * @param compensateNo
	 * @param payFlag
	 * @return
	 * @modified: ☆XMSH(2016年9月12日 下午4:59:25): <br>
	 */
	public List<PrpLLossItemVo> getLosItemList(String compensateNo,String payFlag) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("compensateNo",compensateNo);
		if(payFlag.indexOf(",")> -1){
			String[] payFlagArr = payFlag.split(",");
			qr.addIn("payFlag",payFlagArr);
		}else{
			qr.addEqual("payFlag",payFlag);
		}

		List<PrpLLossItem_QUERY> lossItemPos = databaseDao.findAll(PrpLLossItem_QUERY.class,qr);
		List<PrpLLossItemVo> lossItemVos = null;
		if(lossItemPos!=null&&lossItemPos.size()>0){
			lossItemVos = Beans.copyDepth().from(lossItemPos).toList(PrpLLossItemVo.class);
		}

		return lossItemVos;
	}

	/* 营改增数据写会
	 * update prplCharge
	 * @牛强
	 */
	public void updatePrpLCharges(PrpLChargeVo prplCharge) {
		PrpLCharge prplChargePo = databaseDao.findByPK(PrpLCharge.class,prplCharge.getId());
		prplCharge.setUpdateTime(new Date());
		Beans.copy().from(prplCharge).excludeEmpty().to(prplChargePo);
		databaseDao.update(PrpLCharge.class,prplChargePo);
	}


	/* 查询
	 */
	public PrpLPaymentVo findPrpLPaymentVo(Long id) {
		PrpLPayment PrpLPaymentPo = databaseDao.findByPK(PrpLPayment.class,id);
		PrpLPaymentVo prpLPaymentVo = Beans.copyDepth().from(PrpLPaymentPo).to(PrpLPaymentVo.class);
		return prpLPaymentVo;
	}

	public void updatePrpLPaymentVo(PrpLPaymentVo paymentVo) {
		PrpLPayment payMentPo = databaseDao.findByPK(PrpLPayment.class,paymentVo.getId());
		paymentVo.setUpdateTime(new Date());
		Beans.copy().from(paymentVo).excludeEmpty().to(payMentPo);
		databaseDao.update(PrpLPayment.class,payMentPo);
	}
	
	public void savePrplPayHis(String claimNo,String compensateNo,Long id,String flags,String hisType,Date inputTime) {
		PrpLPayHis prpLPayHis = new PrpLPayHis();
		prpLPayHis.setClaimNo(claimNo);
		prpLPayHis.setCompensateNo(compensateNo);
		prpLPayHis.setCreateTime(new Date());
		prpLPayHis.setInputTime(inputTime);
		prpLPayHis.setOtherId(id);
		prpLPayHis.setPayStatus(flags);
		prpLPayHis.setUpdateTime(new Date());
		prpLPayHis.setHisType(hisType);
		databaseDao.save(PrpLPayHis.class,prpLPayHis);
	}

	public List<PrpLCompensateVo> findCompensateByOther(String registNo,String type,String policyNo,String underwriteFlag) {
		List<PrpLCompensateVo> compeVos = new ArrayList<PrpLCompensateVo>();
		QueryRule queryRule = QueryRule.getInstance();
		if(StringUtils.isNotBlank(registNo)){
			queryRule.addEqual("registNo",registNo);
		}
		if(StringUtils.isNotBlank(type)){
			queryRule.addEqual("compensateType",type);
		}
		if(StringUtils.isNotBlank(policyNo)){
			queryRule.addEqual("policyNo",policyNo);
		}
		if(StringUtils.isNotBlank(underwriteFlag)){
			queryRule.addEqual("underwriteFlag",underwriteFlag);
		}
		List<PrpLCompensate> compePo = databaseDao.findAll(PrpLCompensate.class,queryRule);
		if(compePo!=null&&compePo.size()>0){
			compeVos = Beans.copyDepth().from(compePo).toList(PrpLCompensateVo.class);
		}
		return compeVos;
	}

	/**
	 * 通过保单号查询理算表
	 * @param policyNo
	 * @return
	 */
	public List<PrpLCompensateVo> findPrpCompensateBypolicyNo(String policyNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("policyNo",policyNo);
		List<PrpLCompensate> poLists = databaseDao.findAll(PrpLCompensate.class,queryRule);
		List<PrpLCompensateVo> voLists = new ArrayList<PrpLCompensateVo>();
		if(poLists!=null&&poLists.size()>0){
			for(PrpLCompensate po:poLists){
				PrpLCompensateVo vo = new PrpLCompensateVo();
				vo = Beans.copyDepth().from(po).to(PrpLCompensateVo.class);
				voLists.add(vo);
			}
		}
		return voLists;
	}

	public List<PrpLCompensateVo> findCompensateBySettleNo(String settleNo,String flag) {

		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		if("P".equals(flag)){
			sqlUtil.append(" from PrpLCompensate com where 1=1 ");
			sqlUtil.append(" and exists(select 1 from PrpLPayment payment where ");
			sqlUtil.append(" com.compensateNo=payment.prpLCompensate.compensateNo and payment.settleNo=? ) ");
			sqlUtil.addParamValue(settleNo);
		}else if("F".equals(flag)){
			sqlUtil.append(" from PrpLCompensate com where 1=1 ");
			sqlUtil.append(" and exists(select 1 from PrpLCharge charge where ");
			sqlUtil.append(" com.compensateNo=charge.prpLCompensate.compensateNo and charge.settleNo=? ) ");
			sqlUtil.addParamValue(settleNo);
		}
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		List<PrpLCompensate> compensateList = databaseDao.findAllByHql(PrpLCompensate.class,sql,values);
		List<PrpLCompensateVo> compensateVoList = new ArrayList<PrpLCompensateVo>();
		if(compensateList!=null&&compensateList.size()>0){
			for(PrpLCompensate compensate:compensateList){
				PrpLCompensateVo compensateVo = new PrpLCompensateVo();
				compensateVo = Beans.copyDepth().from(compensate).to(PrpLCompensateVo.class);
				// Beans.copy().from(compensate).to(compensateVo);
				compensateVoList.add(compensateVo);
			}
		}
		return compensateVoList;
	}

	public void updatePrpLPrePay(PrpLPrePayVo vo,String payStatus) {
		PrpLPrePay prpLPrePay = databaseDao.findByPK(PrpLPrePay.class,vo.getId());
		prpLPrePay.setPayStatus(payStatus);
		databaseDao.update(PrpLPrePay.class,prpLPrePay);
	}

	public void updatePrpLPrePay(PrpLPrePayVo vo) {
		PrpLPrePay prpLPrePay = databaseDao.findByPK(PrpLPrePay.class,vo.getId());
		Beans.copy().from(vo).excludeEmpty().to(prpLPrePay);
		databaseDao.update(PrpLPrePay.class,prpLPrePay);
	}

	/**
	 * 通过payeeId查询PrpLPrePay表
	 * @param payeeId
	 * @return
	 */
	public List<PrpLPrePayVo> findPayeeIdByPrpLPrePay(Long payeeId) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("payeeId",payeeId);
		List<PrpLPrePay> poLists = databaseDao.findAll(PrpLPrePay.class,queryRule);
		List<PrpLPrePayVo> voLists = new ArrayList<PrpLPrePayVo>();
		if(poLists!=null&&poLists.size()>0){
			voLists = Beans.copyDepth().from(poLists).toList(PrpLPrePayVo.class);
		}
		return voLists;
	}

	/**
	 * 通过payeeId查询PrpLCharge表
	 * @param payeeId
	 * @return
	 */
	public List<PrpLChargeVo> findPayeeIdByPrpLCharge(Long payeeId) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("payeeId",payeeId);
		List<PrpLCharge> poLists = databaseDao.findAll(PrpLCharge.class,queryRule);
		List<PrpLChargeVo> voLists = new ArrayList<PrpLChargeVo>();
		if(poLists!=null&&poLists.size()>0){
			voLists = Beans.copyDepth().from(poLists).toList(PrpLChargeVo.class);
		}
		return voLists;
	}

	/**
	 * 通过payeeId查询PrpLPadPayPerson表
	 * @param payeeId
	 * @return
	 */
	public List<PrpLPadPayPersonVo> findPayeeIdByPrpLPadPayPerson(Long payeeId) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("payeeId",payeeId);
		List<PrpLPadPayPerson> poLists = databaseDao.findAll(PrpLPadPayPerson.class,queryRule);
		List<PrpLPadPayPersonVo> voLists = new ArrayList<PrpLPadPayPersonVo>();
		if(poLists!=null&&poLists.size()>0){
			voLists = Beans.copyDepth().from(poLists).toList(PrpLPadPayPersonVo.class);
		}
		return voLists;
	}

	/**
	 * 通过payeeId查询PrpLPayment表
	 * @param payeeId
	 * @return
	 */
	public List<PrpLPaymentVo> findPayeeIdByPrpLPayment(Long payeeId) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("payeeId",payeeId);
		List<PrpLPayment> poLists = databaseDao.findAll(PrpLPayment.class,queryRule);
		List<PrpLPaymentVo> voLists = new ArrayList<PrpLPaymentVo>();
		if(poLists!=null&&poLists.size()>0){
			voLists = Beans.copyDepth().from(poLists).toList(PrpLPaymentVo.class);
		}
		return voLists;
	}

	public List<PrpLCompensateVo> findNotCancellCompensatevosByRegistNo(String registNo) {
		List<PrpLCompensateVo> compeVoList = new ArrayList<PrpLCompensateVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addNotEqual("underwriteFlag","7");
		List<PrpLCompensate> compePo = databaseDao.findAll(PrpLCompensate.class,queryRule);
		if(compePo!=null&&compePo.size()>0){
			compeVoList = Beans.copyDepth().from(compePo).toList(PrpLCompensateVo.class);
		}
		return compeVoList;
	}

	/**
	 * 根据计算书更新计算书赔款和费用记录支付时间、支付状态
	 * 
	 * <pre></pre>
	 * @param compeVo 计算书数据实体,需包含子表数据
	 * @param payTime 支付时间
	 * @param payStatus 支付状态
	 * @modified: ☆WLL(2018年2月27日 上午10:52:59): <br>
	 */
	public void updateCompPayTime(PrpLCompensateVo compeVo,Date payTime,String payStatus) {
		if(compeVo.getPrpLPayments()!=null&&compeVo.getPrpLPayments().size()>0){
			for(PrpLPaymentVo payMentVo:compeVo.getPrpLPayments()){
				payMentVo.setPayTime(payTime);
				payMentVo.setPayStatus(payStatus);
				this.updatePrpLPaymentVo(payMentVo);
			}
		}
		if(compeVo.getPrpLCharges()!=null&&compeVo.getPrpLCharges().size()>0){
			for(PrpLChargeVo chargeVo:compeVo.getPrpLCharges()){
				chargeVo.setPayTime(payTime);
				chargeVo.setPayStatus(payStatus);
				this.updatePrpLCharges(chargeVo);
			}
		}
	}

	/** 判断有没有承保这个险别 */
	public boolean existKind(CompensateListVo compensateListVo,String kindCode) {
		boolean bl = false;
		List<CompensateExp> compensateListCompensateList = compensateListVo.getCompensateList();
		for(CompensateExp compensateExp:compensateListCompensateList){
			if(kindCode.equals(compensateExp.getKindCode())){
				bl = true;
			}
		}
		return bl;
	}

	public double min(double data_A,double data_B) {
		if(data_A<=data_B){
			return data_A;
		}else{
			return data_B;
		}
	}

	private static BigDecimal NullToZero(BigDecimal strNum) {
		if(strNum==null){
			return new BigDecimal("0");
		}
		return strNum;
	}

	public Long saveNotionMain(PrpLuwNotionMain mainPo) {
		databaseDao.save(PrpLuwNotionMain.class,mainPo);
		return mainPo.getId();
	}
	public Long saveorUpdateNotionMain(PrpLuwNotionMain mainPo) {
		if(mainPo.getId()==null){
			databaseDao.save(PrpLuwNotionMain.class,mainPo);
		}else{
			databaseDao.update(PrpLuwNotionMain.class,mainPo);
		}
		
		return mainPo.getId();
	}
	

	public void saveNotion(PrpLuwNotion LuwNotion) {
		databaseDao.save(PrpLuwNotion.class,LuwNotion);
	}
	 
	 public void savePrpLCoins(List<PrpLCoinsVo> prpLCoinsVoList){
		 if(prpLCoinsVoList!=null && prpLCoinsVoList.size()>0){
			 //保存前先删除原分摊信息
			 String sql = "DELETE FROM PRPLCOINS WHERE COMPENSATENO='"+prpLCoinsVoList.get(0).getCompensateNo()+"'";
			 baseDaoService.executeSQL(sql);
			 List<PrpLCoins> prpLCoins = Beans.copyDepth().from(prpLCoinsVoList).toList(PrpLCoins.class);
			 databaseDao.saveAll(PrpLCoins.class,prpLCoins);
		 }
	 }


	public List<PrpLCompensateVo> findValidCompensate(String registNo,
			String type,String CompNo ) {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" from PrpLCompensate com where 1=1 ");
		if(StringUtils.isNotBlank(CompNo)){
			sqlUtil.append(" and com.prpLCompensateExt.oppoCompensateNo = ?  and com.prpLCompensateExt.writeOffFlag = ? ");
			sqlUtil.addParamValue(CompNo);
			sqlUtil.addParamValue(WRITEOFFFLAG.ALLOFF);
		}
		if(StringUtils.isNotBlank(registNo)){
			sqlUtil.append(" and com.registNo = ?");
			sqlUtil.addParamValue(registNo);
		}
		sqlUtil.append(" and com.compensateType = ?");
		if(StringUtils.isNotBlank(type)){
			sqlUtil.addParamValue(type);
		}
		sqlUtil.append(" and ( com.underwriteFlag != ? ");
		sqlUtil.addParamValue(CodeConstants.UnderWriteFlag.CANCEL_UNDERWRITE);
		sqlUtil.append(" and  com.underwriteFlag != ? ");
		sqlUtil.addParamValue(CodeConstants.UnderWriteFlag.CANCELFLAG);
		sqlUtil.append(" or com.underwriteFlag is null )");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		List<PrpLCompensate> compensateList = databaseDao.findAllByHql(PrpLCompensate.class,sql,values);
		List<PrpLCompensateVo> compensateVoList = new ArrayList<PrpLCompensateVo>();
		if(compensateList!=null&&compensateList.size()>0){
			compensateVoList = Beans.copyDepth().from(compensateList).toList(PrpLCompensateVo.class);
		}
		return compensateVoList;
	}
	
	/**
	 * 查询报案号下计算书的核赔状态
	 * @param registNo
	 * @return
	 */
	public Map<String,String> getCompUnderWriteFlagByRegNo(String registNo){
		Map<String,String> CompUnderWriteFlagMap = new HashMap<String, String>();
		List<PrpLCompensateVo> compeList = this.findCompByRegistNo(registNo);
		if(compeList!=null && compeList.size() > 0){
			for(PrpLCompensateVo compeVo:compeList){
				CompUnderWriteFlagMap.put(compeVo.getCompensateNo(), compeVo.getUnderwriteFlag());
			}
		}
		return CompUnderWriteFlagMap;
	}

	/**
	 * 根据报案号、保单号、赔付次数查询理算
	 * @param registNo
	 * @param policyNo
	 * @param times
	 * @return
	 */
	public List<PrpLCompensateVo> findPrplCompensateByRegistNoAndPolicyNo(String registNo, String policyNo,Integer times) {
		QueryRule qurey=QueryRule.getInstance();
		qurey.addEqual("registNo",registNo);
		if (StringUtils.isNotBlank(policyNo)){
			qurey.addEqual("policyNo",policyNo);
		}
		qurey.addEqual("times",times);
		qurey.addEqual("compensateType","N");
		List<PrpLCompensate> compos=databaseDao.findAll(PrpLCompensate.class,qurey);
		List<PrpLCompensateVo> comVos=new ArrayList<PrpLCompensateVo>();
		if(compos!=null && compos.size()>0){
			comVos=Beans.copyDepth().from(compos).toList(PrpLCompensateVo.class);
		}
		return comVos;
	}
	/**
	 * 根据保单号查出所有underwriteflag为1和3的理算数据
	 */
	public List<PrpLCompensateVo> findCompByPolicyNo(String policyNo) {
		List<PrpLCompensateVo> compList = new ArrayList<PrpLCompensateVo>();
		if( !StringUtils.isBlank(policyNo)){
			QueryRule qr = QueryRule.getInstance();
			qr.addEqual("policyNo",policyNo);
			qr.addSql(" (underwriteFlag = '1' or underwriteFlag = '3') ");
			List<PrpLCompensate> compPoList = databaseDao.findAll(PrpLCompensate.class,qr);
			compList = Beans.copyDepth().from(compPoList).toList(PrpLCompensateVo.class);
		}
		return compList;
	}
	
	/**
	 * 根据报案号、立案号查询理算
	 * @param registNo
	 * @param policyNo
	 * @param times
	 * @return
	 */
	public List<PrpLCompensateVo> findPrplCompensateByRegistNoAndClaimNo(String registNo, String claimNo) {
		QueryRule qurey=QueryRule.getInstance();
		qurey.addEqual("registNo",registNo);
		qurey.addEqual("claimNo",claimNo);
		//qurey.addDescOrder("TIMES");
		List<PrpLCompensate> compos=databaseDao.findAll(PrpLCompensate.class,qurey);
		List<PrpLCompensateVo> comVos=new ArrayList<PrpLCompensateVo>();
		if(compos!=null && compos.size()>0){
			comVos=Beans.copyDepth().from(compos).toList(PrpLCompensateVo.class);
		}
		return comVos;
	}

	/**
	 * 该方法用于查询并且更新
	 * @param registNo
	 * @param caseTimes
	 * @return
	 */
	public List<PrpLPrePay> findPrePayByRegistNoAndCaseTimes(String compensateNo , Integer caseTimes) {
		List<PrpLPrePayVo> prePayVos = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("compensateNo",compensateNo);
		queryRule.addEqual("caseTimes",caseTimes);
		List<PrpLPrePay> prePayPos = databaseDao.findAll(PrpLPrePay.class,queryRule);
		return prePayPos;
	}

	/**
	 * 该方法用于查询并且更新
	 * @param registNo
	 * @return
	 */
	public List<PrpLPadPayMain> findPadPayMainByRegistNoPA(String registNo) {
		List<PrpLPadPayMain> padPayMain = new ArrayList<PrpLPadPayMain>();
		if(StringUtils.isNotBlank(registNo)){
			QueryRule pfqr = QueryRule.getInstance();
			pfqr.addEqual("registNo",registNo);
			padPayMain = databaseDao.findAll(PrpLPadPayMain.class,pfqr);
		}
		return padPayMain;
	}
}
