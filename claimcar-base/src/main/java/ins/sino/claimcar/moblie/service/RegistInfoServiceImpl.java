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
	 * ??????????????????
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

		// ?????????????????????????????????
		String reconcileFlag = null;
		// ???????????????????????????
		String index = "0";
		// ????????????????????????
		String recoveryFlag1 = "0";
		// ????????????????????????
		String recoveryFlag2 = "0";
		/*
		 * //?????????????????? String cIPolicyNo=CIPolicyNo; //?????????????????? String
		 * bIPolicyNo=BIPolicyNo;
		 */
		/*
		 * if(!StringUtils.isEmpty(flowNodeCode)&&flowNodeCode.equals("PLFirst")&&
		 * !StringUtils.isEmpty(registNo)){ PrpLCheckVo prplcheckvo =
		 * checkTaskService.findCheckVoByRegistNo(registNo);
		 * if(prplcheckvo!=null){ reconcileFlag =prplcheckvo.getReconcileFlag();
		 * } }
		 */
		// ?????????
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

		// TODO PrpLRegistVo registVo?????????????????????????????????registno
		PrpLRegistVo registVo = new PrpLRegistVo();
		if (!StringUtils.isEmpty(registNo)) {
			registVo = registService.findRegistByRegistNo(registNo);
		}
		Map<String, String> registRiskInfoMap = new HashMap<String, String>();
		if (!StringUtils.isEmpty(registNo)) {
			registRiskInfoMap = registService.findRegistRiskInfoByRegistNo(registNo);
		}

		// ?????????????????????????????????????????????
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
			riskWarning = riskWarning + "??????????????????"
					+ registRiskInfoMap.get("CI-No");
			if (StringUtils.isEmpty(registRiskInfoMap.get("CI-DangerNum"))) {
				riskWarning = riskWarning + "??????????????????????????????0";
			} else {
				riskWarning = riskWarning + "??????????????????????????????"
						+ registRiskInfoMap.get("CI-DangerNum");
			}
			if (StringUtils.isEmpty(registRiskInfoMap.get("CI-DangerInSum"))) {
				riskWarning = riskWarning + "???7??????????????????:0";
			} else {
				riskWarning = riskWarning + "???7??????????????????:"
						+ registRiskInfoMap.get("CI-DangerInSum");
			}
			if ("1".equals(registRiskInfoMap.get("CIRepeatReport"))) {
				riskWarning = riskWarning + "????????????48?????????????????????,??????????????????";
			}
			if ("1".equals(registRiskInfoMap.get("CIPolicy-A7"))) {
				riskWarning = riskWarning + "????????????????????????7????????????";
			}
			if ("1".equals(registRiskInfoMap.get("CIPolicy-B7"))) {
				riskWarning = riskWarning + "????????????????????????7????????????";
			}
			if ("1".equals(recoveryFlag2)) {
				riskWarning = riskWarning + "?????????????????????????????????";
			}
		}

		if (StringUtils.isNotEmpty(registRiskInfoMap.get("BI-No"))) {
			if (StringUtils.isNotEmpty(riskWarning)) {
				riskWarning = riskWarning + "?????????????????????"+ registRiskInfoMap.get("BI-No");
			} else {
				riskWarning = riskWarning + "??????????????????"+ registRiskInfoMap.get("BI-No");
			}

			if (StringUtils.isEmpty(registRiskInfoMap.get("BI-DangerNum"))) {
				riskWarning = riskWarning + "??????????????????????????????0";
			} else {
				riskWarning = riskWarning + "??????????????????????????????"+ registRiskInfoMap.get("BI-DangerNum");
			}
			if (StringUtils.isEmpty(registRiskInfoMap.get("BI-DangerInSum"))) {
				riskWarning = riskWarning + "???7??????????????????:0";
			} else {
				riskWarning = riskWarning + "???7??????????????????:" + registRiskInfoMap.get("BI-DangerInSum");
			}
			if ("1".equals(registRiskInfoMap.get("BIRepeatReport"))) {
				riskWarning = riskWarning + "????????????48?????????????????????,??????????????????";
			}
			if (StringUtils.isEmpty(registRiskInfoMap.get("BI-CSHH"))) {
				riskWarning = riskWarning + "???????????????????????????:0";
			} else {
				riskWarning = riskWarning + "???????????????????????????:" + registRiskInfoMap.get("BI-CSHH");
			}

			if ("1".equals(registRiskInfoMap.get("BIPolicy-A7"))) {
				riskWarning = riskWarning + "????????????????????????7????????????";
			}
			if ("1".equals(registRiskInfoMap.get("BIPolicy-B7"))) {
				riskWarning = riskWarning + "????????????????????????7????????????";
			}
			if ("1".equals(recoveryFlag1)) {
				riskWarning = riskWarning + "?????????????????????????????????";
			}
		}
		if ("1".equals(reconcileFlag)) {
			riskWarning = riskWarning + "?????????????????????????????????";
		}
		if ("1".equals(index)) {
			riskWarning = riskWarning + "???????????????????????????";
		}
		if ("1".equals(registRiskInfoMap.get("YJCX"))) {
			riskWarning = riskWarning + "?????????????????????:???";
		} else {
			riskWarning = riskWarning + "?????????????????????:???";
		}
		if ("1".equals(registRiskInfoMap.get("BA-D48"))) {
			riskWarning = riskWarning + "?????????????????????????????????48??????";
		}
		if ("1".equals(registRiskInfoMap.get("DWQC"))) {
			riskWarning = riskWarning + "?????????????????????????????????????????????????????????";
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
		// logger.info("????????????????????????????????????: \n"+xml);
		// ?????????vo
		RegistInfoResVo resVo = new RegistInfoResVo();
		MobileCheckResponseHead head = new MobileCheckResponseHead();
		MobileCheckHead mobileCheckHead = null;
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_", "_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null, "class");// ?????? class??????
		String registNo = "";
		try {
			MobileCheckRequest mobileCheckRequest = (MobileCheckRequest) arg1;
			stream.processAnnotations(MobileCheckRequest.class);
			String xml = stream.toXML(mobileCheckRequest);
			logger.info("???????????????????????????????????????\n" + xml);
			if (StringUtils.isBlank(xml)) {
				throw new IllegalArgumentException("????????????");
			}
			stream.processAnnotations(MobileCheckRequest.class);

			// MobileCheckRequest mobileCheckRequest =
			// (MobileCheckRequest)stream.fromXML(xml);
			MobileCheckBody mobileCheckBody = mobileCheckRequest.getBody();
			mobileCheckHead = mobileCheckRequest.getHead();
			if (!"001".equals(mobileCheckHead.getRequestType())|| !"claim_user".equals(mobileCheckHead.getUser())|| !"claim_psd".equals(mobileCheckHead.getPassWord())) {
				throw new IllegalArgumentException(" ?????????????????????  ");
			}
			if (!StringUtils.isNotBlank(mobileCheckHead.getRequestType())) {
				throw new IllegalArgumentException("????????????????????????");
			}
			if (!StringUtils.isNotBlank(mobileCheckHead.getRequestType())) {
				throw new IllegalArgumentException("?????????????????????");
			}
			if (!StringUtils.isNotBlank(mobileCheckHead.getRequestType())) {
				throw new IllegalArgumentException("??????????????????");
			}
			if (!StringUtils.isNotBlank(mobileCheckBody.getRegistNo())) {
				throw new IllegalArgumentException("?????????????????????");
			}
			registNo = mobileCheckBody.getRegistNo();
			// ?????????vo
			PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(mobileCheckBody.getRegistNo());
			if (prpLRegistVo != null) {
				PrpLCMainVo prpLCMainVo = prpLCMainService.findPrpLCMain(prpLRegistVo.getRegistNo(), prpLRegistVo.getPolicyNo());
				RegistInfoResBodyVo registInfoResBodyVo = new RegistInfoResBodyVo();
				registInfoResBodyVo.setRegistNo(prpLRegistVo.getRegistNo());
				if (prpLCMainVo != null) {
					registInfoResBodyVo.setInuredName(prpLCMainVo.getInsuredName());// ????????????
				}
				registInfoResBodyVo.setIsVip("0");
				registInfoResBodyVo.setDriverName(prpLRegistVo.getDriverName());
				registInfoResBodyVo.setLicenseNo(prpLRegistVo.getPrpLRegistExt().getLicenseNo());
				registInfoResBodyVo.setBrandName(prpLRegistVo.getPrpLRegistCarLosses().get(0).getBrandName());
				registInfoResBodyVo.setReportorName(prpLRegistVo.getReportorName());
				registInfoResBodyVo.setReportPhoneNumber(prpLRegistVo.getReportorPhone());
				registInfoResBodyVo.setLinkerName(prpLRegistVo.getLinkerName());
				registInfoResBodyVo.setLinkerPhoneNumber(prpLRegistVo.getLinkerMobile());//??????????????????1
				registInfoResBodyVo.setExigenCegree(prpLRegistVo.getMercyFlag());
				// ????????????
				for (PrpLRegistCarLossVo vo : prpLRegistVo.getPrpLRegistCarLosses()) {
					if ("1".equals(vo.getLossparty())) {
						registInfoResBodyVo.setCollisionSite(vo.getLosspart());
					}
				}
				// ????????????
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
				if(CodeConstants.YN01.Y.equals(prpLRegistVo.getPrpLRegistExt().getIsOnSitReport())){//???
					registInfoResBodyVo.setIsCurrentReport(CodeConstants.YN01.Y);
				}else{//???
					registInfoResBodyVo.setIsCurrentReport(CodeConstants.YN01.N);
				}
				registInfoResBodyVo.setInSuranceCompanyCode(prpLRegistVo.getComCode());
				String  inSuranceCompanyName = CodeTranUtil.transCode("ComCodeFull", prpLRegistVo.getComCode());
				registInfoResBodyVo.setInSuranceCompanyName(inSuranceCompanyName);
				if(CodeConstants.YN01.Y.equals(prpLRegistVo.getPrpLRegistExt().getIsCarLoss())){//???
					registInfoResBodyVo.setCarLossFlag(CodeConstants.YN01.Y);
				}else{//???
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
			logger.info("???????????????????????????????????????\n");
			e.printStackTrace();
		}
		stream.processAnnotations(RegistInfoResVo.class);
		logger.info("???????????????????????????????????????\n" + stream.toXML(resVo));
		//return stream.toXML(resVo);
		return resVo;
	}
		
}
