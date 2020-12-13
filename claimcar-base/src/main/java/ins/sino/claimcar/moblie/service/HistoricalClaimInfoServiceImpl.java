package ins.sino.claimcar.moblie.service;

import ins.framework.lang.Springs;
import ins.platform.utils.DataUtils;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.ClaimSummaryService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimSummaryVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrploldCompensateVo;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.mobile.vo.AccountVo;
import ins.sino.claimcar.mobile.vo.HisclaimInfoVo;
import ins.sino.claimcar.mobile.vo.HisclaimListVo;
import ins.sino.claimcar.mobile.vo.HistoricalClaimInfoResBodyVo;
import ins.sino.claimcar.mobile.vo.HistoricalClaimInfoResVo;
import ins.sino.claimcar.mobile.vo.PolicyInfoReqBodyVo;
import ins.sino.claimcar.mobile.vo.PolicyInfoReqVo;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckRequest;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;
import ins.sino.claimcar.moblie.logUtil.QuickClaimLogUtil;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sinosoft.api.service.ServiceInterface;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;


public class HistoricalClaimInfoServiceImpl implements
ServiceInterface {

	@Autowired
	private ClaimService claimService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private CompensateTaskService compensateTaskService;
	@Autowired
	private PayCustomService payCustomService;
	@Autowired
	private PrpLCMainService prpLCMainService;
	@Autowired
	RegistService registService;
	@Autowired
	ClaimSummaryService claimSummaryService;

	private static Logger logger = LoggerFactory.getLogger(HistoricalClaimInfoServiceImpl.class);

	

	private void init() {
		if(claimService==null){
			claimService = (ClaimService)Springs.getBean(ClaimService.class);
		}
		if(registQueryService==null){
			registQueryService = (RegistQueryService)Springs.getBean(RegistQueryService.class);
		}
		if(compensateTaskService==null){
			compensateTaskService = (CompensateTaskService)Springs.getBean(CompensateTaskService.class);
		}
		if(payCustomService==null){
			payCustomService = (PayCustomService)Springs.getBean(PayCustomService.class);
		}
		if(prpLCMainService==null){
			prpLCMainService = (PrpLCMainService)Springs.getBean(PrpLCMainService.class);
		}
		if(registService==null){
			registService = (RegistService)Springs.getBean(RegistService.class);
		}
		if(claimSummaryService==null){
			claimSummaryService = (ClaimSummaryService)Springs.getBean(ClaimSummaryService.class);
		}
	}

	public List<HisclaimListVo> setHisclaimList(
			List<HisclaimListVo> hisclaimList,
			PolicyInfoReqBodyVo policyInfoReqBodyVo,
			List<PrpLCMainVo> prpLCMainVoList) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		// 新理赔数据start
		List<PrpLClaimSummaryVo> prpLClaimSummaryVoList = new ArrayList<PrpLClaimSummaryVo>();

		List<PrpLRegistVo> oldPrpLRegistVoList = new ArrayList<PrpLRegistVo>();// 旧理赔
		List<PrploldCompensateVo> oldcompensateVos = new ArrayList<PrploldCompensateVo>();// 旧理赔收款人信息
		for (PrpLCMainVo vo : prpLCMainVoList) {
			List<PrpLClaimSummaryVo> claimSummaryVoList = claimSummaryService.findPrpLClaimSummaryVoList(vo.getPolicyNo(),vo.getRegistNo());
			if (claimSummaryVoList != null && claimSummaryVoList.size() > 0) {
				prpLClaimSummaryVoList.addAll(claimSummaryVoList);
			}

			List<PrpLRegistVo> registVoList = registQueryService.findOldRegistByPolicyNo(vo.getPolicyNo());
			if (registVoList != null && registVoList.size() > 0) {
				oldPrpLRegistVoList.addAll(registVoList);
			}
			List<PrploldCompensateVo> compensateVoList = compensateTaskService.findPrpoldCompensateBypolicyNo(vo.getPolicyNo());
			if (compensateVoList != null && compensateVoList.size() > 0) {
				oldcompensateVos.addAll(compensateVoList);
			}
		}
		Map<String, HisclaimListVo> paymentAmount = new HashMap<String, HisclaimListVo>();
		for (PrpLClaimSummaryVo vo : prpLClaimSummaryVoList) {
			BigDecimal newPaymentAmount = new BigDecimal(0);
			if (paymentAmount.containsKey(vo.getRegistNo())) {
				if (vo.getRealPay() != null) {
					newPaymentAmount = newPaymentAmount.add(vo.getRealPay());
				}
				HisclaimListVo hisclaimListVo = paymentAmount.get(vo.getRegistNo());
				if (hisclaimListVo.getCloseDate() != null && vo.getEndCaseTime() != null) {
					Date closeDate = formatter.parse(hisclaimListVo.getCloseDate());
					if (vo.getEndCaseTime().getTime() > closeDate.getTime()) {// 可能后面有改动
						PrpLCompensateVo prpLCompensateVo = compensateTaskService.searchCompByClaimNo(vo.getClaimNo());
						if (prpLCompensateVo != null) {
							/*
							 * if(prpLCompensateVo.getSumPaidAmt()!=null){
							 * hisclaim
							 * .setPaymentAmount(prpLCompensateVo.getSumPaidAmt
							 * ().toString()); }
							 */
							if ("3".equals(prpLCompensateVo.getCaseType())) {
								hisclaimListVo.setCaseType("代赔");
							} else {
								hisclaimListVo.setCaseType("自赔");
							}
						} else {
							hisclaimListVo.setCaseType("自赔");
						}
						if (vo.getEndCaseTime() != null) {
							String endCaseTime = formatter.format(vo.getEndCaseTime());
							hisclaimListVo.setCloseDate(endCaseTime);
						}
						if ("正常处理".equals(hisclaimListVo.getCaseState())) {
							hisclaimListVo.setCaseState("正常处理");
						} else if ("注销".equals(hisclaimListVo.getCaseState())) {
							if ("C".equals(vo.getCaseStatus())) {
								hisclaimListVo.setCaseState("注销");
							} else {
								hisclaimListVo.setCaseState("正常处理");
							}
						} else {
							if ("E".equals(vo.getCaseStatus())) {
								hisclaimListVo.setCaseState("完成");
							} else {
								hisclaimListVo.setCaseState("正常处理");
							}
						}
					}
				}
				if("12".equals(vo.getRiskCode().substring(0, 2))){
					hisclaimListVo.setBusipolicyNo(vo.getPolicyNo());
				}else{
					hisclaimListVo.setPolicyNo(vo.getPolicyNo());
				}
				hisclaimListVo.setPaymentAmount(newPaymentAmount.toString());
				paymentAmount.put(vo.getRegistNo(), hisclaimListVo);
			} else {
				HisclaimListVo hisclaim = new HisclaimListVo();
				hisclaim.setRegistNo(vo.getRegistNo());
				hisclaim.setLicenseNo(vo.getLicenseNo());
				hisclaim.setDamageCode(vo.getDamageCode());
				String damageTime = formatter.format(vo.getDamageTime());
				hisclaim.setDamageDate(damageTime);
				hisclaim.setDamageAddress(vo.getDamageAddress());
				String reportTime = formatter.format(vo.getReportTime());
				if (vo.getEndCaseTime() != null) {
					String endCaseTime = formatter.format(vo.getEndCaseTime());
					hisclaim.setCloseDate(endCaseTime);
				}
				hisclaim.setReportTime(reportTime);

				if (vo.getRealPay() != null) {
					hisclaim.setPaymentAmount(vo.getRealPay().toString());
					newPaymentAmount.add(vo.getRealPay());
				}

				if ("N".equals(vo.getCaseStatus())) {
					hisclaim.setCaseState("正常处理");
				} else if ("C".equals(vo.getCaseStatus())) {
					hisclaim.setCaseState("注销");
				} else if ("E".equals(vo.getCaseStatus())) {
					hisclaim.setCaseState("完成");
				}
				PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(policyInfoReqBodyVo.getRegistNo());
				PrpLCompensateVo prpLCompensateVo = compensateTaskService.searchCompByClaimNo(vo.getClaimNo());
				if (prpLCompensateVo != null) {
					/*
					 * if(prpLCompensateVo.getSumPaidAmt()!=null){
					 * hisclaim.setPaymentAmount
					 * (prpLCompensateVo.getSumPaidAmt().toString()); }
					 */
					if ("3".equals(prpLCompensateVo.getCaseType())) {
						hisclaim.setCaseType("代赔");
					} else {
						hisclaim.setCaseType("自赔");
					}
				} else {
					hisclaim.setCaseType("自赔");
				}
				hisclaim.setCollisionSite(prpLRegistVo.getPrpLRegistCarLosses().get(0).getLosspart());

				// 收款人信息
				List<PrpLCompensateVo> prpLCompensateListVo = compensateTaskService.findCompListByClaimNo(vo.getClaimNo(), "N");
				List<AccountVo> accountList = new ArrayList<AccountVo>();
				for (PrpLCompensateVo compensateVo : prpLCompensateListVo) {
					for (PrpLPaymentVo lPaymentVo : compensateVo.getPrpLPayments()) {
						PrpLPayCustomVo prpLPayCustomVo = payCustomService.findPayCustomVoById(lPaymentVo.getPayeeId());
						AccountVo account = new AccountVo();
						account.setPayeeType(prpLPayCustomVo.getPayObjectKind());
						account.setName(prpLPayCustomVo.getPayeeName());
						account.setIdentifyNumber(prpLPayCustomVo.getCertifyNo());
						account.setBankName(prpLPayCustomVo.getBankOutlets());
						account.setAccountName(prpLPayCustomVo.getPayeeName());
						account.setAccountNo(prpLPayCustomVo.getAccountNo());
						account.setPhone(prpLPayCustomVo.getPayeeMobile());
						accountList.add(account);
					}

					hisclaim.setAccountList(accountList);
				}

				if("12".equals(vo.getRiskCode().substring(0, 2))){
					hisclaim.setBusipolicyNo(vo.getPolicyNo());
				}else{
					hisclaim.setPolicyNo(vo.getPolicyNo());
				}
				/*
				 * HisclaimListVo hisclaimVo = new HisclaimListVo();
				 * hisclaim.setAccountList(accountList);
				 * hisclaimVo.setHisclaim(hisclaim);
				 */
				paymentAmount.put(vo.getRegistNo(), hisclaim);
				// hisclaimList.add(hisclaim);
			}
		}
		// 从map里循环取值
		for (HisclaimListVo vo : paymentAmount.values()) {
			hisclaimList.add(vo);
		}
		// 新理赔数据end

		// 旧理赔start

		for (PrpLRegistVo oldPrpLRegistVo : oldPrpLRegistVoList) {
			HisclaimListVo hisclaim = new HisclaimListVo();
			hisclaim.setRegistNo(oldPrpLRegistVo.getRegistNo());
			hisclaim.setLicenseNo(oldPrpLRegistVo.getLicense());
			hisclaim.setDamageCode(oldPrpLRegistVo.getDamageCode());
			String damageTime = formatter.format(oldPrpLRegistVo.getDamageTime());
			hisclaim.setDamageDate(damageTime);
			hisclaim.setDamageAddress(oldPrpLRegistVo.getDamageAddress());
			String reportTime = formatter.format(oldPrpLRegistVo.getReportTime());

			// 旧理赔
			List<PrploldCompensateVo> oldcompensateVoList = new ArrayList<PrploldCompensateVo>();
			List<PrpLRegistVo> oldPrpLRegistRPolicyList = registQueryService.findOldPrpLRegistRPolicy(oldPrpLRegistVo.getRegistNo());
			for (PrpLRegistVo prpLRegistRPolicy : oldPrpLRegistRPolicyList) {
				List<PrploldCompensateVo> compensateVoList = new ArrayList<PrploldCompensateVo>();
				compensateVoList = compensateTaskService.findPrpoldCompensateBypolicyNo(prpLRegistRPolicy.getPolicyNo());
				if (compensateVoList != null && compensateVoList.size() > 0) {
					oldcompensateVoList.addAll(compensateVoList);
				}
				
				//设置保单
				if("12".equals(prpLRegistRPolicy.getRiskCode().substring(0, 2))){
					hisclaim.setBusipolicyNo(prpLRegistRPolicy.getPolicyNo());
				}else{
					hisclaim.setPolicyNo(prpLRegistRPolicy.getPolicyNo());
				}
			}
			double oldPaymentAmount = 0;
			if (oldcompensateVoList != null && oldcompensateVoList.size() > 0) {
				for (PrploldCompensateVo oldVo : oldcompensateVoList) {
					if (oldVo.getEndcaseDate() != null) {
						String endCaseTime = formatter.format(oldVo.getEndcaseDate());
						hisclaim.setCloseDate(endCaseTime);
					}
					if (oldVo.getSumthisPaid() != null) {
						oldPaymentAmount = oldPaymentAmount
								+ DataUtils.NullToZero(oldVo.getSumthisPaid()).doubleValue();
					}
				}
			}
			hisclaim.setPaymentAmount(String.valueOf(oldPaymentAmount));
			List<PrpLClaimVo> prpLClaimVoList = registQueryService
					.findOldPrpLClaimVo(oldPrpLRegistVo.getRegistNo());
			if (prpLClaimVoList != null && prpLClaimVoList.size() > 0) {
				if (prpLClaimVoList.get(0).getEndCaseTime() != null) {
					String endCaseTime = formatter.format(prpLClaimVoList.get(0).getEndCaseTime());
					hisclaim.setCloseDate(endCaseTime);
				} else {
					hisclaim.setCloseDate("");
				}
				if (prpLClaimVoList.size() == 2) {
					if (prpLClaimVoList.get(0).getEndCaseTime() != null
							&& prpLClaimVoList.get(1).getEndCaseTime() != null) {
						hisclaim.setCaseState("完成");
					} else if (prpLClaimVoList.get(0).getCancelTime() != null
							&& prpLClaimVoList.get(1).getCancelTime() != null) {
						hisclaim.setCaseState("注销");
					} else {
						hisclaim.setCaseState("正常处理");
					}
				} else {
					if (prpLClaimVoList.get(0).getEndCaseTime() != null) {
						hisclaim.setCaseState("完成");
					} else if (prpLClaimVoList.get(0).getCancelTime() != null) {
						hisclaim.setCaseState("注销");
					} else {
						hisclaim.setCaseState("正常处理");
					}
				}

			} else {
				hisclaim.setCaseState("正常处理");
			}
			/*
			 * if(vo.getEndCaseTime()!=null){ String endCaseTime =
			 * formatter.format(vo.getEndCaseTime());
			 * hisclaim.setCloseDate(endCaseTime); }
			 */
			hisclaim.setReportTime(reportTime);
			if ("L".equals(oldPrpLRegistVo.getFlag())) {
				hisclaim.setCaseType("自赔");
			} else {
				hisclaim.setCaseType("代赔");
			}
			// hisclaim.setCaseType(oldPrpLRegistVo.getFlag());
			/*
			 * if(vo.getRealPay()!=null){
			 * hisclaim.setPaymentAmount(vo.getRealPay().toString()); }
			 * 
			 * if("N".equals(vo.getCaseStatus())){
			 * hisclaim.setCaseState("正常处理"); }else
			 * if("C".equals(vo.getCaseStatus())){ hisclaim.setCaseState("注销");
			 * }else if("E".equals(vo.getCaseStatus())){
			 * hisclaim.setCaseState("完成"); }
			 */

			// 碰撞部位
			List<PrpLRegistCarLossVo> registCarLossVoList = registQueryService.findOldPrpLthirdCarLoss(oldPrpLRegistVo.getRegistNo(), "1");
			if (registCarLossVoList != null && registCarLossVoList.size() > 0) {
				if (StringUtils.isNotBlank(registCarLossVoList.get(0).getLosspart())) {
					if (registCarLossVoList.get(0).getLosspart().length() > 1) {
						if (!"10".equals(registCarLossVoList.get(0).getLosspart())) {
							hisclaim.setCollisionSite(registCarLossVoList.get(0).getLosspart().substring(1, 2));
						} else {
							hisclaim.setCollisionSite(registCarLossVoList.get(0).getLosspart());
						}
					}
				}
			}
			// 收款人信息

			List<AccountVo> accountList = new ArrayList<AccountVo>();
			for (PrploldCompensateVo vo1 : oldcompensateVos) {
				List<PrpLPayCustomVo> payCustomVoList = registQueryService.findOldPrpjlinkaccountByCertiNo(vo1.getCompensateNo());
				for (PrpLPayCustomVo payCustomVo : payCustomVoList) {
					List<PrpLPayCustomVo> prpLPayCustomVoList = registQueryService.findOldAccountByAccountNo(payCustomVo.getAccountNo());
					for (PrpLPayCustomVo prpLPayCustomVo : prpLPayCustomVoList) {
						AccountVo account = new AccountVo();
						account.setPayeeType(prpLPayCustomVo.getPayObjectKind());
						account.setName(prpLPayCustomVo.getPayeeName());
						account.setIdentifyNumber(prpLPayCustomVo.getCertifyNo());
						account.setBankName(prpLPayCustomVo.getBankName());
						account.setAccountName(prpLPayCustomVo.getPayeeName());
						account.setAccountNo(prpLPayCustomVo.getAccountNo());
						account.setPhone(prpLPayCustomVo.getPayeeMobile());
						accountList.add(account);
					}
				}
			}
			hisclaim.setAccountList(accountList);
			hisclaimList.add(hisclaim);
		}
		// 旧理赔end

		/*
		 * PrpLClaimVo vo =
		 * claimService.findClaimVoByRegistNoAndPolicyNo(policyInfoReqBodyVo
		 * .getRegistNo(), policyNo); PrpLRegistVo prpLRegistVo =
		 * registQueryService.findByRegistNo(policyInfoReqBodyVo.getRegistNo());
		 * PrpLCompensateVo prpLCompensateVo =
		 * compensateTaskService.searchCompByClaimNo(vo.getClaimNo());
		 * HisclaimVo hisclaim = new HisclaimVo(); List<AccountListVo>
		 * accountList = new ArrayList<AccountListVo>();
		 * hisclaim.setRegistNo(policyInfoReqBodyVo.getRegistNo());
		 * hisclaim.setLicenseNo
		 * (prpLRegistVo.getPrpLRegistExt().getLicenseNo());
		 * hisclaim.setDamageCode(vo.getDamageCode()); String damageTime =
		 * formatter.format(vo.getDamageTime());
		 * hisclaim.setDamageDate(damageTime);
		 * hisclaim.setDamageAddress(prpLRegistVo.getDamageAddress()); String
		 * reportTime = formatter.format(vo.getReportTime());
		 * hisclaim.setReportTime(reportTime); if(vo.getEndCaseTime()!=null){
		 * String endCaseTime = formatter.format(vo.getEndCaseTime());
		 * hisclaim.setCloseDate(endCaseTime); } if(vo.getEndCaseTime()!=null){
		 * hisclaim
		 * .setSettledAmount(prpLCompensateVo.getSumPaidAmt().toString());
		 * hisclaim.setCaseState("结案"); }else{//没有结案
		 * hisclaim.setSettledAmount("0"); hisclaim.setCaseState("未结案"); }
		 * if(prpLCompensateVo != null){
		 * if(prpLCompensateVo.getSumPaidAmt()!=null){
		 * hisclaim.setPaymentAmount(
		 * prpLCompensateVo.getSumPaidAmt().toString()); }
		 * hisclaim.setCaseType(prpLCompensateVo.getCaseType()); }
		 * hisclaim.setCollisionSite
		 * (prpLRegistVo.getPrpLRegistCarLosses().get(0).getLosspart());
		 * 
		 * 
		 * //收款人信息 List<PrpLCompensateVo> prpLCompensateListVo =
		 * compensateTaskService.findCompListByClaimNo(vo.getClaimNo(), "N");
		 * for(PrpLCompensateVo compensateVo :prpLCompensateListVo){
		 * for(PrpLPaymentVo lPaymentVo:compensateVo.getPrpLPayments()){
		 * PrpLPayCustomVo prpLPayCustomVo =
		 * payCustomService.findPayCustomVoById(lPaymentVo.getPayeeId());
		 * AccountVo account = new AccountVo();
		 * account.setPayeeType(prpLPayCustomVo.getPayObjectKind());
		 * account.setName(prpLPayCustomVo.getPayeeName());
		 * account.setIdentifyNumber(prpLPayCustomVo.getCertifyNo());
		 * account.setBankName(prpLPayCustomVo.getBankName());
		 * account.setAccountName(prpLPayCustomVo.getPayeeName());
		 * account.setAccountNo(prpLPayCustomVo.getAccountNo());
		 * account.setPhone(prpLPayCustomVo.getPayeeMobile()); AccountListVo
		 * accountVo = new AccountListVo(); accountVo.setAccount(account);
		 * accountList.add(accountVo); } } HisclaimListVo hisclaimVo = new
		 * HisclaimListVo(); hisclaim.setAccountList(accountList);
		 * hisclaimVo.setHisclaim(hisclaim); hisclaimList.add(hisclaimVo);
		 */

		return hisclaimList;

	}

	public Map<String, String> registRiskInfo(String registNo)
			throws ParseException {

		Map<String, String> registRiskInfoMap = new HashMap<String, String>();
		if (!StringUtils.isEmpty(registNo)) {
			registRiskInfoMap = registService.findRegistRiskInfoByRegistNo(registNo);
		}
		return registRiskInfoMap;
	}
	/*
	 * 保单基本信息接口
	 * 
	 * zjd
	 */
	@Override
	public Object service(String arg0, Object arg1) {
		init();
		
		// 返回的vo
		HistoricalClaimInfoResVo resVo = new HistoricalClaimInfoResVo();
		HistoricalClaimInfoResBodyVo historicalClaimInfoResBodyVo = new HistoricalClaimInfoResBodyVo();
		HisclaimInfoVo hisclaimInfo = new HisclaimInfoVo();
		List<HisclaimListVo> hisclaimList = new ArrayList<HisclaimListVo>();

		MobileCheckResponseHead head = new MobileCheckResponseHead();
		MobileCheckHead mobileCheckHead = new MobileCheckHead();
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_", "_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null, "class");// 去掉 class属性
		String registNo = "";
		try {
			
			stream.processAnnotations(PolicyInfoReqVo.class);
			/*PolicyInfoReqVo policyInfoReqVo = (PolicyInfoReqVo) stream
					.fromXML(xml);*/
			PolicyInfoReqVo policyInfoReqVo = (PolicyInfoReqVo) arg1;
			stream.processAnnotations(MobileCheckRequest.class);
			String xml = stream.toXML(policyInfoReqVo);
			logger.info("移动查勘历史赔案接收报文: \n" + xml);
			PolicyInfoReqBodyVo policyInfoReqBodyVo = policyInfoReqVo.getBody();
			mobileCheckHead = policyInfoReqVo.getHead();
			if (!"003".equals(mobileCheckHead.getRequestType())|| !"claim_user".equals(mobileCheckHead.getUser())|| !"claim_psd".equals(mobileCheckHead.getPassWord())) {
				throw new IllegalArgumentException(" 请求头参数错误  ");
			}
			if (StringUtils.isBlank(xml)) {
				throw new IllegalArgumentException("报文为空");
			}
			if (!StringUtils.isNotBlank(mobileCheckHead.getRequestType())) {
				throw new IllegalArgumentException("请求类型不能为空");
			}
			if (!StringUtils.isNotBlank(mobileCheckHead.getRequestType())) {
				throw new IllegalArgumentException("用户名不能为空");
			}
			if (!StringUtils.isNotBlank(mobileCheckHead.getRequestType())) {
				throw new IllegalArgumentException("密码不能为空");
			}
			if (!StringUtils.isNotBlank(policyInfoReqBodyVo.getRegistNo())) {
				throw new IllegalArgumentException("报案号不能为空");
			}
			registNo = policyInfoReqBodyVo.getRegistNo();
			/*
			 * if((!StringUtils.isNotBlank(policyInfoReqBodyVo.getBusiPolicyNo())
			 * ) &&
			 * (!StringUtils.isNotBlank(policyInfoReqBodyVo.getPolicyNo()))){
			 * throw new IllegalArgumentException("交强险保单号 和商业强险保单号不能同时为空"); }
			 */
			// 返回的vo
			List<PrpLCMainVo> prpLCMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(policyInfoReqBodyVo.getRegistNo());
			double claimSum = 0;
			int claimTimes = 0;// 次数
			for (PrpLCMainVo vo : prpLCMainVoList) {
				if ("12".equals(vo.getRiskCode().substring(0, 2))) {
					hisclaimInfo.setBusipolicyNo(vo.getPolicyNo());
				} else {
					hisclaimInfo.setPolicyNo(vo.getPolicyNo());
				}

				// 新理赔
				List<PrpLCompensateVo> compensateNewVoList = compensateTaskService
						.queryCompensateByOther(null, "N", vo.getPolicyNo(),
								"1");
				if (compensateNewVoList != null
						&& compensateNewVoList.size() > 0) {
					for (PrpLCompensateVo compensateVo : compensateNewVoList) {
						if ("1".equals(compensateVo.getUnderwriteFlag())) {
							// claimSum =
							// claimSum.add(compensateVo.getSumPaidAmt());
							claimSum = claimSum + DataUtils.NullToZero(compensateVo.getSumPaidAmt()).doubleValue();
							claimTimes = claimTimes + 1;
						}
					}
				}

				// 旧理赔
				List<PrploldCompensateVo> oldcompensateVos = compensateTaskService
						.findPrpoldCompensateBypolicyNo(vo.getPolicyNo());
				if (oldcompensateVos != null && oldcompensateVos.size() > 0) {
					for (PrploldCompensateVo oldVo : oldcompensateVos) {
						if (oldVo.getEndcaseDate() != null) {
							claimSum = claimSum + DataUtils.NullToZero(oldVo.getSumPaid()).doubleValue();
							claimTimes = claimTimes + 1;
						}
					}
				}
			}
			hisclaimList = setHisclaimList(hisclaimList, policyInfoReqBodyVo,prpLCMainVoList);
			/*
			 * if(StringUtils.isNotBlank(policyInfoReqBodyVo.getBusiPolicyNo())){
			 * hisclaimInfo
			 * .setBusipolicyNo(policyInfoReqBodyVo.getBusiPolicyNo());
			 * hisclaimList = setHisclaimList(hisclaimList,
			 * policyInfoReqBodyVo); }
			 * if(StringUtils.isNotBlank(policyInfoReqBodyVo.getPolicyNo())){
			 * hisclaimInfo.setPolicyNo(policyInfoReqBodyVo.getPolicyNo());
			 * hisclaimList = setHisclaimList(hisclaimList,
			 * policyInfoReqBodyVo); }
			 */
			Map<String, String> registRiskInfoMap = registRiskInfo(policyInfoReqBodyVo.getRegistNo());
			int historicalAccident = 0;
			if (StringUtils.isNotEmpty(registRiskInfoMap.get("CI-No"))) {
				if (!StringUtils.isEmpty(registRiskInfoMap.get("CI-DangerNum"))) {
					historicalAccident = historicalAccident
							+ Integer.parseInt(registRiskInfoMap.get("CI-DangerNum"));
					for (PrpLCMainVo vo : prpLCMainVoList) {
						if("1101".equals(vo.getRiskCode())){
							historicalAccident = historicalAccident-1;
						}
					}
				}
			}

			if (StringUtils.isNotEmpty(registRiskInfoMap.get("BI-No"))) {
				if (!StringUtils.isEmpty(registRiskInfoMap.get("BI-DangerNum"))) {
					historicalAccident = historicalAccident
							+ Integer.parseInt(registRiskInfoMap.get("BI-DangerNum"));
					for (PrpLCMainVo vo : prpLCMainVoList) {
						if(!"1101".equals(vo.getRiskCode())){
							historicalAccident = historicalAccident-1;
						}
					}
				}
			}

			hisclaimInfo.setHistoricalAccident(String.valueOf(historicalAccident));
			hisclaimInfo.setHistoricalClaimTimes(String.valueOf(claimTimes));
			hisclaimInfo.setHistoricalClaimSum(String.valueOf(claimSum));
			hisclaimInfo.setHisclaimList(hisclaimList);
			historicalClaimInfoResBodyVo.setHisclaimInfo(hisclaimInfo);

			head.setResponseType(mobileCheckHead.getRequestType());
			head.setResponseCode("YES");
			head.setResponseMessage("Success");
			resVo.setHead(head);
			resVo.setBody(historicalClaimInfoResBodyVo);
		} catch (Exception e) {
			head.setResponseType(mobileCheckHead.getRequestType());
			head.setResponseCode("NO");
			head.setResponseMessage(e.getMessage());
			resVo.setHead(head);
			logger.info("移动查勘保单信息异常信息：\n");
			e.printStackTrace();
		}
		stream.processAnnotations(HistoricalClaimInfoResVo.class);
		logger.info("移动查勘历史赔案返回报文=========：\n" + stream.toXML(resVo));
		//return stream.toXML(resVo);
		return resVo;
	}
}
