package ins.sino.claimcar.moblie.service;

import ins.framework.lang.Springs;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.vo.PrpLLawSuitVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.mobile.vo.RegistInfoResBodyVo;
import ins.sino.claimcar.mobile.vo.RegistInfoResVo;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckBody;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckRequest;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;
import ins.sino.claimcar.moblie.logUtil.QuickClaimLogUtil;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistHandlerService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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


public class RegistInfoServiceImpl implements ServiceInterface {

	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private PrpLCMainService prpLCMainService;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	ClaimTaskService claimTaskService;
	@Autowired
	RegistService registService;
	@Autowired
	RegistHandlerService registHandlerService;
	@Autowired
	CompensateTaskService compensateTaskService;

	private static Logger logger = LoggerFactory.getLogger(RegistInfoServiceImpl.class);

	/*
	 * 报案基本信息
	 * 
	 * zjd
	 */

	private void init() {
		if(registQueryService==null){
			registQueryService = (RegistQueryService)Springs.getBean(RegistQueryService.class);
		}
		if(prpLCMainService==null){
			prpLCMainService = (PrpLCMainService)Springs.getBean(PrpLCMainService.class);
		}
		if(checkTaskService==null){
			checkTaskService = (CheckTaskService)Springs.getBean(CheckTaskService.class);
		}
		if(claimTaskService==null){
			claimTaskService = (ClaimTaskService)Springs.getBean(ClaimTaskService.class);
		}
		if(registService==null){
			registService = (RegistService)Springs.getBean(RegistService.class);
		}
		if(registHandlerService==null){
			registHandlerService = (RegistHandlerService)Springs.getBean(RegistHandlerService.class);
		}
		if(compensateTaskService==null){
			compensateTaskService = (CompensateTaskService)Springs.getBean(CompensateTaskService.class);
		}
		
	}

	public String registRiskInfo(String registNo) throws ParseException {

		// 是否为现场调解案件标志
		String reconcileFlag = null;
		// 是否为诉讼案件标志
		String index = "0";
		// 商业是否发起追偿
		String recoveryFlag1 = "0";
		// 交强是否发起追偿
		String recoveryFlag2 = "0";
		/*
		 * //交强险保单号 String cIPolicyNo=CIPolicyNo; //商业险保单号 String
		 * bIPolicyNo=BIPolicyNo;
		 */
		/*
		 * if(!StringUtils.isEmpty(flowNodeCode)&&flowNodeCode.equals("PLFirst")&&
		 * !StringUtils.isEmpty(registNo)){ PrpLCheckVo prplcheckvo =
		 * checkTaskService.findCheckVoByRegistNo(registNo);
		 * if(prplcheckvo!=null){ reconcileFlag =prplcheckvo.getReconcileFlag();
		 * } }
		 */
		// 待确认
		PrpLCheckVo prplcheckvo = checkTaskService.findCheckVoByRegistNo(registNo);
		if (prplcheckvo != null) {
			reconcileFlag = prplcheckvo.getReconcileFlag();
		}
		if (!StringUtils.isEmpty(registNo)) {
			List<PrpLLawSuitVo> prpLLawSuitVos = claimTaskService.findPrpLLawSuitVoByRegistNo(registNo);
			if (prpLLawSuitVos != null && prpLLawSuitVos.size() > 0) {
				index = "1";
			}
		}

		// TODO PrpLRegistVo registVo到时再界面获取或者查询registno
		PrpLRegistVo registVo = new PrpLRegistVo();
		if (!StringUtils.isEmpty(registNo)) {
			registVo = registService.findRegistByRegistNo(registNo);
		}
		Map<String, String> registRiskInfoMap = new HashMap<String, String>();
		if (!StringUtils.isEmpty(registNo)) {
			registRiskInfoMap = registService.findRegistRiskInfoByRegistNo(registNo);
		}

		// 判断商业和交强核陪是否发起追偿
		if (!StringUtils.isEmpty(registNo)) {
			List<PrpLCompensateVo> compList = compensateTaskService
					.findCompByRegistNo(registNo);
			if (compList != null && compList.size() > 0) {
				for (PrpLCompensateVo prpLCompensateVo : compList) {
					if (prpLCompensateVo.getRiskCode().equals("1101")) {
						if (prpLCompensateVo.getRecoveryFlag().equals("1")
								&& recoveryFlag2.equals("0")) {
							recoveryFlag2 = "1";
						}
					} else {
						if (prpLCompensateVo.getRecoveryFlag().equals("1")
								&& recoveryFlag1.equals("0")) {
							recoveryFlag1 = "1";
						}
					}
				}
			}
		}
		String riskWarning = "";
		if (StringUtils.isNotEmpty(registRiskInfoMap.get("CI-No"))) {
			riskWarning = riskWarning + "交强险保单："
					+ registRiskInfoMap.get("CI-No");
			if (StringUtils.isEmpty(registRiskInfoMap.get("CI-DangerNum"))) {
				riskWarning = riskWarning + "。有效期内出险次数：0";
			} else {
				riskWarning = riskWarning + "。有效期内出险次数："
						+ registRiskInfoMap.get("CI-DangerNum");
			}
			if (StringUtils.isEmpty(registRiskInfoMap.get("CI-DangerInSum"))) {
				riskWarning = riskWarning + "。7天内出险次数:0";
			} else {
				riskWarning = riskWarning + "。7天内出险次数:"
						+ registRiskInfoMap.get("CI-DangerInSum");
			}
			if ("1".equals(registRiskInfoMap.get("CIRepeatReport"))) {
				riskWarning = riskWarning + "。该保单48小时内已报过案,疑似重复报案";
			}
			if ("1".equals(registRiskInfoMap.get("CIPolicy-A7"))) {
				riskWarning = riskWarning + "。交强保单生效后7天内出险";
			}
			if ("1".equals(registRiskInfoMap.get("CIPolicy-B7"))) {
				riskWarning = riskWarning + "。交强保单失效前7天内出险";
			}
			if ("1".equals(recoveryFlag2)) {
				riskWarning = riskWarning + "。当前核赔涉及追偿处理";
			}
		}

		if (StringUtils.isNotEmpty(registRiskInfoMap.get("BI-No"))) {
			if (StringUtils.isNotEmpty(riskWarning)) {
				riskWarning = riskWarning + "。商业险保单："+ registRiskInfoMap.get("BI-No");
			} else {
				riskWarning = riskWarning + "商业险保单："+ registRiskInfoMap.get("BI-No");
			}

			if (StringUtils.isEmpty(registRiskInfoMap.get("BI-DangerNum"))) {
				riskWarning = riskWarning + "。有效期内出险次数：0";
			} else {
				riskWarning = riskWarning + "。有效期内出险次数："+ registRiskInfoMap.get("BI-DangerNum");
			}
			if (StringUtils.isEmpty(registRiskInfoMap.get("BI-DangerInSum"))) {
				riskWarning = riskWarning + "。7天内出险次数:0";
			} else {
				riskWarning = riskWarning + "。7天内出险次数:" + registRiskInfoMap.get("BI-DangerInSum");
			}
			if ("1".equals(registRiskInfoMap.get("BIRepeatReport"))) {
				riskWarning = riskWarning + "。该保单48小时内已报过案,疑似重复报案";
			}
			if (StringUtils.isEmpty(registRiskInfoMap.get("BI-CSHH"))) {
				riskWarning = riskWarning + "。车身划痕出险次数:0";
			} else {
				riskWarning = riskWarning + "。车身划痕出险次数:" + registRiskInfoMap.get("BI-CSHH");
			}

			if ("1".equals(registRiskInfoMap.get("BIPolicy-A7"))) {
				riskWarning = riskWarning + "。商业保单生效后7天内出险";
			}
			if ("1".equals(registRiskInfoMap.get("BIPolicy-B7"))) {
				riskWarning = riskWarning + "。商业保单失效前7天内出险";
			}
			if ("1".equals(recoveryFlag1)) {
				riskWarning = riskWarning + "。当前核赔涉及追偿处理";
			}
		}
		if ("1".equals(reconcileFlag)) {
			riskWarning = riskWarning + "。该案件为现场调解案件";
		}
		if ("1".equals(index)) {
			riskWarning = riskWarning + "。该案件为诉讼案件";
		}
		if ("1".equals(registRiskInfoMap.get("YJCX"))) {
			riskWarning = riskWarning + "。是否夜间出险:是";
		} else {
			riskWarning = riskWarning + "。是否夜间出险:否";
		}
		if ("1".equals(registRiskInfoMap.get("BA-D48"))) {
			riskWarning = riskWarning + "。报案时间超过出险时间48小时";
		}
		if ("1".equals(registRiskInfoMap.get("DWQC"))) {
			riskWarning = riskWarning + "。该案件为代位求偿案件，可能涉及追偿。";
		}
		/*
		 * model.addAttribute("recoveryFlag1", recoveryFlag1);
		 * model.addAttribute("recoveryFlag2", recoveryFlag2);
		 * model.addAttribute("index", index);
		 * model.addAttribute("registNo",registNo);
		 * model.addAttribute("prpLRegistRiskInfoMap",registRiskInfoMap);
		 * model.addAttribute("reconcileFlag", reconcileFlag);
		 */
		return riskWarning;
	}

	@Override
	public Object service(String arg0, Object arg1) {
		init();
		// logger.info("移动查勘报案信息接收报文: \n"+xml);
		// 返回的vo
		RegistInfoResVo resVo = new RegistInfoResVo();
		MobileCheckResponseHead head = new MobileCheckResponseHead();
		MobileCheckHead mobileCheckHead = null;
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_", "_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null, "class");// 去掉 class属性
		String registNo = "";
		try {
			MobileCheckRequest mobileCheckRequest = (MobileCheckRequest) arg1;
			stream.processAnnotations(MobileCheckRequest.class);
			String xml = stream.toXML(mobileCheckRequest);
			logger.info("移动查勘报案信息接收报文：\n" + xml);
			if (StringUtils.isBlank(xml)) {
				throw new IllegalArgumentException("报文为空");
			}
			stream.processAnnotations(MobileCheckRequest.class);

			// MobileCheckRequest mobileCheckRequest =
			// (MobileCheckRequest)stream.fromXML(xml);
			MobileCheckBody mobileCheckBody = mobileCheckRequest.getBody();
			mobileCheckHead = mobileCheckRequest.getHead();
			if (!"001".equals(mobileCheckHead.getRequestType())|| !"claim_user".equals(mobileCheckHead.getUser())|| !"claim_psd".equals(mobileCheckHead.getPassWord())) {
				throw new IllegalArgumentException(" 请求头参数错误  ");
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
			if (!StringUtils.isNotBlank(mobileCheckBody.getRegistNo())) {
				throw new IllegalArgumentException("报案号不能为空");
			}
			registNo = mobileCheckBody.getRegistNo();
			// 返回的vo
			PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(mobileCheckBody.getRegistNo());
			if (prpLRegistVo != null) {
				PrpLCMainVo prpLCMainVo = prpLCMainService.findPrpLCMain(prpLRegistVo.getRegistNo(), prpLRegistVo.getPolicyNo());
				RegistInfoResBodyVo registInfoResBodyVo = new RegistInfoResBodyVo();
				registInfoResBodyVo.setRegistNo(prpLRegistVo.getRegistNo());
				if (prpLCMainVo != null) {
					registInfoResBodyVo.setInuredName(prpLCMainVo.getInsuredName());// 被保险人
				}
				registInfoResBodyVo.setIsVip("0");
				registInfoResBodyVo.setDriverName(prpLRegistVo.getDriverName());
				registInfoResBodyVo.setLicenseNo(prpLRegistVo.getPrpLRegistExt().getLicenseNo());
				registInfoResBodyVo.setBrandName(prpLRegistVo.getPrpLRegistCarLosses().get(0).getBrandName());
				registInfoResBodyVo.setReportorName(prpLRegistVo.getReportorName());
				registInfoResBodyVo.setReportPhoneNumber(prpLRegistVo.getReportorPhone());
				registInfoResBodyVo.setLinkerName(prpLRegistVo.getLinkerName());
				registInfoResBodyVo.setLinkerPhoneNumber(prpLRegistVo.getLinkerMobile());//取联系人电话1
				registInfoResBodyVo.setExigenCegree(prpLRegistVo.getMercyFlag());
				// 取标的车
				for (PrpLRegistCarLossVo vo : prpLRegistVo.getPrpLRegistCarLosses()) {
					if ("1".equals(vo.getLossparty())) {
						registInfoResBodyVo.setCollisionSite(vo.getLosspart());
					}
				}
				// 风险提示
				registInfoResBodyVo.setRiskWarning(registRiskInfo(prpLRegistVo.getRegistNo()));
				//System.out.println(registRiskInfo(prpLRegistVo.getRegistNo()));
				registInfoResBodyVo.setRelations(prpLRegistVo.getReportorRelation());
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String reportTime = formatter.format(prpLRegistVo.getReportTime());
				String damageTime = formatter.format(prpLRegistVo.getDamageTime());
				registInfoResBodyVo.setReportDate(reportTime);
				registInfoResBodyVo.setDamageDate(damageTime);
				registInfoResBodyVo.setDamageCode(prpLRegistVo.getDamageCode());
				registInfoResBodyVo.setDamageAddress(prpLRegistVo.getDamageAddress());
				registInfoResBodyVo.setAccidentDesc(prpLRegistVo.getPrpLRegistExt().getDangerRemark());
				if(CodeConstants.YN01.Y.equals(prpLRegistVo.getPrpLRegistExt().getIsOnSitReport())){//是
					registInfoResBodyVo.setIsCurrentReport(CodeConstants.YN01.Y);
				}else{//否
					registInfoResBodyVo.setIsCurrentReport(CodeConstants.YN01.N);
				}
				registInfoResBodyVo.setInSuranceCompanyCode(prpLRegistVo.getComCode());
				String  inSuranceCompanyName = CodeTranUtil.transCode("ComCodeFull", prpLRegistVo.getComCode());
				registInfoResBodyVo.setInSuranceCompanyName(inSuranceCompanyName);
				if(CodeConstants.YN01.Y.equals(prpLRegistVo.getPrpLRegistExt().getIsCarLoss())){//是
					registInfoResBodyVo.setCarLossFlag(CodeConstants.YN01.Y);
				}else{//否
					registInfoResBodyVo.setCarLossFlag(CodeConstants.YN01.N);
				}
				head.setResponseType(mobileCheckHead.getRequestType());
				head.setResponseCode("YES");
				head.setResponseMessage("Success");
				resVo.setBody(registInfoResBodyVo);
				resVo.setHead(head);
			}
		} catch (Exception e) {
			head.setResponseType(mobileCheckHead.getRequestType());
			head.setResponseCode("NO");
			head.setResponseMessage(e.getMessage());
			resVo.setHead(head);
			logger.info("移动查勘报案信息异常信息：\n");
			e.printStackTrace();
		}
		stream.processAnnotations(RegistInfoResVo.class);
		logger.info("移动查勘报案信息返回报文：\n" + stream.toXML(resVo));
		//return stream.toXML(resVo);
		return resVo;
	}
		
}
