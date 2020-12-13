package ins.sino.claimcar.losscar.web.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ins.sino.claimcar.check.vo.PrpLCheckDriverVo;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import freemarker.core.ParseException;
import ins.framework.dao.database.support.Page;
import ins.framework.service.CodeTranService;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.service.facade.CodeDictService;
import ins.platform.common.util.ConfigUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.platform.saa.vo.SaaUserPowerVo;
import ins.platform.utils.DataUtils;
import ins.platform.utils.DateUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.LossParty;
import ins.sino.claimcar.certify.service.CertifyIlogService;
import ins.sino.claimcar.certify.service.CertifyService;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrplFraudriskInfoVo;
import ins.sino.claimcar.claim.vo.PrplLaborInfoVo;
import ins.sino.claimcar.claim.vo.PrplOperationInfoVo;
import ins.sino.claimcar.claim.vo.PrplPartsInfoVo;
import ins.sino.claimcar.claim.vo.PrplRiskpointInfoVo;
import ins.sino.claimcar.claim.vo.PrplTestinfoMainVo;
import ins.sino.claimcar.claimcarYJ.service.ClaimcarYJComService;
import ins.sino.claimcar.claimcarYJ.service.ClaimcarYJService;
import ins.sino.claimcar.claimcarYJ.vo.PrpLDlhkMainVo;
import ins.sino.claimcar.claimcarYJ.vo.PrpLyjlosscarCompVo;
import ins.sino.claimcar.claimyj.service.YjInteractionService;
import ins.sino.claimcar.claimyj.service.YjPrpLDlhkMainService;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfFlowService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;
import ins.sino.claimcar.ilog.rule.service.IlogRuleService;
import ins.sino.claimcar.ilogFinalpowerInfo.vo.IlogFinalPowerInfoVo;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.ClaimFittingVo;
import ins.sino.claimcar.losscar.vo.DefCommonVo;
import ins.sino.claimcar.losscar.vo.DeflossActionVo;
import ins.sino.claimcar.losscar.vo.PrpLCaseComponentVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarRepairVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarSubRiskVo;
import ins.sino.claimcar.losscar.vo.SubmitNextVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersHospitalVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLRepairFactoryVo;
import ins.sino.claimcar.mobilecheck.service.SendMsgToMobileService;
import ins.sino.claimcar.other.service.AcheckService;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.vo.PrpLAcheckVo;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLAssessorMainVo;
import ins.sino.claimcar.platform.service.CertifyToPaltformService;
import ins.sino.claimcar.platform.service.LossToPlatformService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.service.RegistTmpService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.selfHelpClaimCar.service.SelfHelpClaimCarService;
import ins.sino.claimcar.selfHelpClaimCar.vo.PrpLAutocasestateInfoVo;
import ins.sino.claimcar.subrogation.service.SubrogationService;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationCarVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationPersonVo;
import ins.sino.claimcar.verifyclaim.service.VerifyClaimService;
import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 
 * @author yk
 *
 */
@Controller
@RequestMapping("/defloss")
public class DeflossAction {

	private static Logger logger = LoggerFactory.getLogger(DeflossAction.class);
	@Autowired
	ScheduleService scheduleService;
	
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	DeflossHandleService deflossHandleService;
	@Autowired
	CodeDictService codeDictService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	ManagerService managerService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	PersTraceService persTraceService;
	@Autowired
	SubrogationService subrogationService;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	ClaimTaskService claimTaskService;
	@Autowired
	LossToPlatformService lossToPlatformService;
	@Autowired
	PropTaskService propTaskService;
	@Autowired
	private LossCarService lossCarService;
	@Autowired
	private ClaimTextService claimTextService;
	@Autowired
	CheckHandleService checkHandleService;
	@Autowired
	RegistService registService;
	@Autowired
	PersTraceDubboService persTraceDubboService;
	@Autowired
    CompensateTaskService compensateTaskService;
	@Autowired
	RegistTmpService registTmpService;
	@Autowired
    private SendMsgToMobileService sendMsgToMobileService;
	@Autowired
	private AssessorService assessorService;
	@Autowired
	InterfaceAsyncService interfaceAsyncService;
	@Autowired 
	EndCasePubService endCasePubService;
    @Autowired
    PrpLCMainService prpLCMainService; 
	@Autowired
	private CertifyService certifyService;	
	@Autowired
	SaaUserPowerService saaUserPowerService;
    @Autowired
    CertifyIlogService certifyIlogService;
    @Autowired
    CertifyToPaltformService certifyToPaltformService;
    @Autowired
    VerifyClaimService verifyClaimService;
    @Autowired
    WfFlowService wfFlowService;
    @Autowired
    YjInteractionService yjInteractionService;
    @Autowired
    ClaimcarYJService claimcarYJService;
    @Autowired
    YjPrpLDlhkMainService yjPrpLDlhkMainService;
    @Autowired
	private SelfHelpClaimCarService selfHelpClaimCarService;
    @Autowired
    ClaimcarYJComService claimcarYJComService;
    @Autowired
	private AcheckService acheckService;
    @Autowired
    private IlogRuleService ilogRuleService;

    /**
     * 定损初始化
     * @param request
     * @param flowTaskId
     * @return
     */
	@RequestMapping(value = "/preAddDefloss.do")  
	public ModelAndView preAddDefloss(HttpServletRequest request,Double flowTaskId) throws Exception{
		SysUserVo sysUserVo=WebUserUtils.getUser();
		String sign=request.getParameter("sign");//判断请求是否来自定损页面同查勘按钮
		DeflossActionVo deflossVo = deflossHandleService.prepareAddDefLoss(flowTaskId,WebUserUtils.getUser(),sign);
		PrpLRegistVo registVo = deflossVo.getRegistVo();
		ModelAndView mv = new ModelAndView();
		//脱敏处理
		if(CodeConstants.WorkStatus.END.equals(deflossVo.getTaskVo().getWorkStatus())){
			//reportorPhone
			PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.STARFLAG);
			if(configValueVo!=null && "1".equals(configValueVo.getConfigValue())){//开关
				deflossVo.getCarInfoVo().setIdentifyNo(DataUtils.replacePrivacy(deflossVo.getCarInfoVo().getIdentifyNo()));
				deflossVo.getCarInfoVo().setDrivingLicenseNo(DataUtils.replacePrivacy(deflossVo.getCarInfoVo().getDrivingLicenseNo()));
			}
		}
		PrpLDlossCarMainVo prpLDlossCarMainVo=deflossVo.getLossCarMainVo();
		PrpLScheduleDefLossVo scheduleDefLossVo=new PrpLScheduleDefLossVo() ;
		if(prpLDlossCarMainVo!=null){
			 scheduleDefLossVo = scheduleService.findScheduleDefLossByPk(prpLDlossCarMainVo.getScheduleDeflossId());
		}
		String signIndex="";
		if(registVo!=null){ 
			if(!"1101".equals(registVo.getRiskCode())){
				if(StringUtils.isNotBlank(registVo.getPolicyNo())){
				   signIndex=search(deflossVo);
				}
			}
		}
		//页面公估费查看按钮是否亮显
		String assessSign="0";
		if(registVo!=null){
			List<PrpLAssessorFeeVo> listFeeVo=assessorService.findPrpLAssessorFeeVoByRegistNo(registVo.getRegistNo());
			if(listFeeVo!=null && listFeeVo.size()>0){
	     		for(PrpLAssessorFeeVo vo :listFeeVo){
					PrpLAssessorMainVo assessMainVo=assessorService.findAssessorMainVoById(vo.getAssessMainId());
					if(assessMainVo!=null && "3".equals(assessMainVo.getUnderWriteFlag())){
						assessSign="1";
						break;
					}
				}
	     	}
		}
		String isMobileCase = "";
		if("1".equals(deflossVo.getTaskVo().getIsMobileAccept())&&HandlerStatus.INIT.equals(deflossVo.getTaskVo().getHandlerStatus())){
			isMobileCase = "1";
		}else{
			isMobileCase = "0";
		}
		//三者车的是否线下处理取标的车的值
		if(!"1".equals(deflossVo.getLossCarMainVo().getDeflossCarType())){
			List<PrpLDlossCarMainVo> carMainVoList = lossCarService.findLossCarMainBySerialNo(prpLDlossCarMainVo.getRegistNo(), 1);
			if(carMainVoList!=null && carMainVoList.size()>0){
				deflossVo.getLossCarMainVo().setOffLineHanding(carMainVoList.get(0).getOffLineHanding());
			}
		}
		//标的车
		if("1".equals(deflossVo.getLossCarMainVo().getDeflossCarType())){
			PrpLCItemCarVo prpLCItemCarVo = registQueryService.findCItemCarByRegistNo(deflossVo.getLossCarMainVo().getRegistNo());
			mv.addObject("prpLCItemCarVo",prpLCItemCarVo);
		}
		//结过案不能选择快赔

		String existEndCase = "N";
		List<PrpLEndCaseVo> endCaseVoList = endCasePubService.queryAllByRegistNo(deflossVo.getLossCarMainVo().getRegistNo());
		if(endCaseVoList!=null && endCaseVoList.size()>0){
			existEndCase = "Y";
		}				
    	
		//是否为单商业案件，1-单商业，2-单交强，3-商业和交强同时报案
		String caseFlag="";
		List<PrpLClaimVo> claimList=claimTaskService.findClaimListByRegistNo(deflossVo.getLossCarMainVo().getRegistNo());
		if(claimList!=null && claimList.size()>0 ){
			if(claimList.size()==1){
				if("1101".equals(claimList.get(0).getRiskCode())){
					caseFlag="2";
				}else{
					caseFlag="1";
				}
			}else{
				caseFlag="3";
			}
		}
        
    	
	    //三者车交强承保机构赋值
		List<PrpLCheckCarInfoVo> prpLCheckCarInfoList = checkTaskService.findPrpLCheckCarInfoVoListByRegistNo(deflossVo.getLossCarMainVo().getRegistNo());//车辆信息列表;
		if("3".equals(deflossVo.getLossCarMainVo().getDeflossCarType()) && deflossVo.getCarInfoVo()!=null && StringUtils.isNotBlank(deflossVo.getCarInfoVo().getLicenseNo())){
			if( StringUtils.isBlank(deflossVo.getCarInfoVo().getBiInsureComCode()) && StringUtils.isBlank(deflossVo.getCarInfoVo().getBiInsurerArea()) && StringUtils.isBlank(deflossVo.getCarInfoVo().getBiPolicyNo()) && StringUtils.isBlank(deflossVo.getCarInfoVo().getBiRegistNo())
				&& StringUtils.isBlank(deflossVo.getCarInfoVo().getCiInsureComCode()) && StringUtils.isBlank(deflossVo.getCarInfoVo().getCiInsurerArea()) && StringUtils.isBlank(deflossVo.getCarInfoVo().getCiPolicyNo()) && StringUtils.isBlank(deflossVo.getCarInfoVo().getCiRegistNo())	)
			if(prpLCheckCarInfoList!=null && prpLCheckCarInfoList.size()>0){
				for(PrpLCheckCarInfoVo InfoVo:prpLCheckCarInfoList){
					if(deflossVo.getCarInfoVo().getLicenseNo().equals(InfoVo.getLicenseNo())){
						deflossVo.getCarInfoVo().setBiInsureComCode(InfoVo.getBiInsureComCode());
						deflossVo.getCarInfoVo().setBiInsurerArea(InfoVo.getBiInsurerArea());
						deflossVo.getCarInfoVo().setBiRegistNo(InfoVo.getBiRegistNo());
						deflossVo.getCarInfoVo().setBiPolicyNo(InfoVo.getBiPolicyNo());
						deflossVo.getCarInfoVo().setCiInsureComCode(InfoVo.getCiInsureComCode());
						deflossVo.getCarInfoVo().setCiInsurerArea(InfoVo.getCiInsurerArea());
						deflossVo.getCarInfoVo().setCiRegistNo(InfoVo.getCiRegistNo());
						deflossVo.getCarInfoVo().setCiPolicyNo(InfoVo.getCiPolicyNo());
						break;
					}
				}
			}
		}
		// 请求获取该报案号下险别对应保额
        List<PrpLCItemKindVo> cItemKindList = new ArrayList<PrpLCItemKindVo>();
        cItemKindList = registQueryService.findCItemKindListByRegistNo(prpLDlossCarMainVo.getRegistNo());
        mv.addObject("kindCodeX2","0");//没有承保
        for(PrpLCItemKindVo vo : cItemKindList){
            if("X2".equals(vo.getKindCode())){//发动机涉水损失险
                mv.addObject("kindCodeX2","1");//有承保
            }
        }	
		//山东预警信信息按钮是否显示，山东保单显示，其它不显示
		String policeInfoFlag="0";//显示标志
		String policyComCode = policyViewService.getPolicyComCode(prpLDlossCarMainVo.getRegistNo());
		if(policyComCode.startsWith("62")){
			policeInfoFlag = "1";
		     //山东影像对比
	        String claimSequenceNo = "";
	        List<PrpLCMainVo> prpLCMains = prpLCMainService.findPrpLCMainsByRegistNo(deflossVo.getRegistVo().getRegistNo());
	        if(prpLCMains != null && prpLCMains.size() > 1){//有商业取商业
	            for(PrpLCMainVo vo : prpLCMains){
	                if("12".equals(vo.getRiskCode().substring(0, 2))){
	                    if(StringUtils.isNotBlank(vo.getClaimSequenceNo())){
	                        claimSequenceNo = vo.getClaimSequenceNo();
	                        mv.addObject("claimSequenceNo",claimSequenceNo);
	                    }
	                }
	            }  
	        }else if(prpLCMains != null && prpLCMains.size() == 1){
	            PrpLCMainVo vo = prpLCMains.get(0);
	            if(StringUtils.isNotBlank(vo.getClaimSequenceNo())){
	                claimSequenceNo = vo.getClaimSequenceNo();
	                mv.addObject("claimSequenceNo",claimSequenceNo);
	            }
	        }
	        String carRiskUrl = SpringProperties.getProperty("CARRISK_URL");
            String carRiskUserName = SpringProperties.getProperty("SDWARN_YMUSER");
            String carRiskPassWord = SpringProperties.getProperty("SDWARN_YMPW");
	        mv.addObject("comparePicURL",carRiskUrl);
	        String claimPeriod = "04";
	        mv.addObject("claimPeriod",claimPeriod);
	        mv.addObject("carRiskUserName",carRiskUserName);
	        mv.addObject("carRiskPassWord",carRiskPassWord);
		}
		//阳杰查看展示标志0-不展示，1展示
		String yjAskFlag=CodeConstants.YN01.N;
		if(deflossVo.getLossCarMainVo()!=null && deflossVo.getLossCarMainVo().getPrpLDlossCarComps()!=null && deflossVo.getLossCarMainVo().getPrpLDlossCarComps().size()>0){
			for(PrpLDlossCarCompVo compVo:deflossVo.getLossCarMainVo().getPrpLDlossCarComps()){
				if("1".equals(compVo.getyJAskPrivceFlag())){
					yjAskFlag=CodeConstants.YN01.Y;
					break;
				}
			}
		}
		
		//阳杰复检信息按钮展示标志0-不展示，1-展示
		String yjReAskFlag=CodeConstants.YN01.N;
		if(deflossVo.getLossCarMainVo()!=null && deflossVo.getLossCarMainVo().getPrpLDlossCarComps()!=null && deflossVo.getLossCarMainVo().getPrpLDlossCarComps().size()>0){
			for(PrpLDlossCarCompVo compVo:deflossVo.getLossCarMainVo().getPrpLDlossCarComps()){
				if("1".equals(compVo.getIsYangJie())){
					yjReAskFlag=CodeConstants.YN01.Y;
					break;
				}
			}
		}
		
		/*//阳杰下单按钮展示标志0-不展示，1展示
		String yjSupllyFlag=CodeConstants.YN01.N;
		if(sysUserVo!=null && StringUtils.isNotBlank(sysUserVo.getUserCode()) && deflossVo.getLossCarMainVo()!=null){
			if(sysUserVo.getUserCode().equals(deflossVo.getLossCarMainVo().getHandlerCode()) && CodeConstants.YN01.Y.equals(deflossVo.getLossCarMainVo().getUnderWriteFlag())){
				yjSupllyFlag=CodeConstants.YN01.Y;
			}
		}
		mv.addObject("yjFlag",yjSupllyFlag);*/
		mv.addObject("yjReAskFlag",yjReAskFlag);
		mv.addObject("yjAskFlag",yjAskFlag);
		mv.addObject("policeInfoFlag",policeInfoFlag);   	

        String jy2Flag = "0";
		String JY_TimeStamp = SpringProperties.getProperty("JY_TIMESTAMP");
        Date timeStamp = DateUtils.strToDate(JY_TimeStamp, DateUtils.YToSec);
        if(registVo.getReportTime().getTime()>timeStamp.getTime()){
        	jy2Flag = "1";
        }
        String selfClaimFlag="0";//0表示非自助理赔案件，1表示自助理赔案件
        String selfDlossAmout="0";
        PrpLRegistVo prpLRegistVo=registService.findRegistByRegistNo(prpLDlossCarMainVo.getRegistNo());
        if(prpLRegistVo!=null && "1".equals(prpLRegistVo.getSelfClaimFlag())){
        	selfClaimFlag="1";
        	PrpLAutocasestateInfoVo prpLAutocasestateInfoVo= selfHelpClaimCarService.findPrpLAutocasestateInfoByRegistNoAndLicenseNo(prpLDlossCarMainVo.getRegistNo(), prpLDlossCarMainVo.getLicenseNo());
        	if(prpLAutocasestateInfoVo!=null && prpLAutocasestateInfoVo.getFeepayMoney()!=null){
        		selfDlossAmout=prpLAutocasestateInfoVo.getFeepayMoney().toString();
        	}
        }

//根据deflossVo.getLossCarMainVo()里面的报案号以及licenseNo去prplcheckcarinfo获取carid(如果不为空)。再根据carid去prplcheckdriver获得初次领证日期
if (deflossVo.getCarInfoVo().getAcceptLicenseDate()==null) {


	PrpLCheckDriverVo prpLCheckDriverVo = checkHandleService.findPrplcheckcarinfoByRegistNoAndLicenseNo(deflossVo.getLossCarMainVo().getRegistNo(), deflossVo.getLossCarMainVo().getLicenseNo());
	if (prpLCheckDriverVo != null) {
		//mv.addObject("acceptlicensedate",prpLCheckDriverVo.getAcceptLicenseDate());
		deflossVo.getCarInfoVo().setAcceptLicenseDate(prpLCheckDriverVo.getAcceptLicenseDate());
	} else {
		//mv.addObject("acceptlicensedate",null);
		deflossVo.getCarInfoVo().setAcceptLicenseDate(null);
	}
}
        mv.addObject("selfClaimFlag",selfClaimFlag);
		mv.addObject("selfDlossAmout",selfDlossAmout);
        mv.addObject("jy2Flag",jy2Flag);
		mv.addObject("caseFlag",caseFlag);
		mv.addObject("assessSign",assessSign);
		mv.addObject("signIndex",signIndex);
		mv.addObject("sign",sign);
		mv.addObject("scheduleDefLossVo", scheduleDefLossVo);
		mv.addObject("kindMap",deflossVo.getKindMap());
		mv.addObject("intermMap",deflossVo.getIntermMap());
		mv.addObject("lossCarMainVo",deflossVo.getLossCarMainVo());
		mv.addObject("claimTextVos",deflossVo.getClaimTextVos());
		mv.addObject("claimTextVo",deflossVo.getClaimTextVo());
		mv.addObject("lossChargeVos",deflossVo.getLossChargeVos());
		mv.addObject("carInfoVo",deflossVo.getCarInfoVo());
		mv.addObject("commonVo",deflossVo.getCommonVo());
		mv.addObject("registVo",deflossVo.getRegistVo());
		mv.addObject("subrogationMain", deflossVo.getSubrogationMainVo());
		mv.addObject("checkChargeVo",deflossVo.getCheckChargeVo());
		mv.addObject("taskVo",deflossVo.getTaskVo());
	    mv.addObject("deviceMap",deflossVo.getCommonVo().getDeviceMap());
		mv.addObject("lossPropMainVos",deflossVo.getCommonVo().getLossPropMainList());
		mv.addObject("isMobileCase",isMobileCase);
		mv.addObject("oldClaim",deflossVo.getRegistVo().getFlag());
		mv.addObject("existEndCase",existEndCase);
		mv.addObject("riskCode",deflossVo.getRegistVo().getRiskCode());

		mv.setViewName("lossCar/DeflossEdit");

		return mv;
	}
	
	//定损页面的定损详细信息按钮
	@RequestMapping(value = "/findsDefloss.do")
	public ModelAndView findsDefloss(String registNo){
		ModelAndView mv=new ModelAndView();
		List<PrpLDlossCarMainVo> lossCarMainList = lossCarService.findLossCarMainByRegistNo(registNo);
		 List<PrpLdlossPropMainVo> lossPropMainList = propTaskService.findPropMainListByRegistNo(registNo);
		 List<PrpLDlossPersTraceVo> traceList = persTraceService.findPersTraceVoByRegistNo(registNo);
		 //取跟踪次数y
		List<PrpLDlossPersTraceMainVo> lossPersMainList = persTraceDubboService.findPersTraceMainVoList(registNo);
		//人伤住院信息
		List<PrpLDlossPersHospitalVo> lists=new ArrayList<PrpLDlossPersHospitalVo>();
		if(lossPersMainList!=null && lossPersMainList.size()>0){
			if(lossPersMainList.get(0).getTraceTimes()!=null){
			if(traceList!=null && traceList.size()>0){
		       for(PrpLDlossPersTraceVo trace:traceList){
			      trace.setRemark(lossPersMainList.get(0).getTraceTimes().toString());
			      if(trace.getPrpLDlossPersInjured()!=null){
			    	  lists=trace.getPrpLDlossPersInjured().getPrpLDlossPersHospitals(); 
			    	  if(lists!=null && lists.size()>0){
				    	  for(PrpLDlossPersHospitalVo vo:lists){
				    		  //取人伤人员住院医院名称
				    		  trace.getPrpLDlossPersInjured().setHospitalCode(vo.getHospitalCode());
				    		  trace.getPrpLDlossPersInjured().setHospitalCity(vo.getHospitalCity());
				    	  }
				      }
			      }
			      
			   }
			}
			}
		}
		
	     mv.addObject("lossCarMainVo", lossCarMainList);
		 mv.addObject("propMainVos", lossPropMainList);
		 mv.addObject("prpLDlossPersTraceMainVos",traceList);
		 mv.setViewName("lossperson/common/loss/LossEdit");
	return mv;
		
	}
	
	//核价任务处理页面零配件更换次数链接
	@RequestMapping(value = "/linkCase.do")
	public ModelAndView linkCase(String compCode,String frameNo){
		ModelAndView mv =new ModelAndView();
		List<String> registNoList = new ArrayList<String>();
		List<PrpLCaseComponentVo> prpComVos= lossCarService.findCaseCompList(compCode, frameNo);
		if(prpComVos!=null&&prpComVos.size()>0){
			for(PrpLCaseComponentVo prpComVo:prpComVos){
				registNoList.add(prpComVo.getRegistNo());
			}
		}
	  /*HashSet<String> registNoSet=new HashSet<String>(registNoList);//去重，必要时放
     	mv.addObject("registNoList", registNoSet);*/
		mv.addObject("registNoList", registNoList);
		mv.setViewName("loss-common/ShowRegistNo");
		return mv;
	}
	
	
	@RequestMapping(value = "/acceptDefloss.do") 
	@ResponseBody
	public AjaxResult acceptDefloss(String flowTaskId,String registNo){
		String flag = "1";
		try{
		    PrpLDlossCarMainVo carMainVo = deflossHandleService.acceptDefloss(Double.parseDouble(flowTaskId),registNo,WebUserUtils.getUser());
			//移动端案件理赔处理要通知快赔 并写会理赔处理标识
            //写会标志
			 PrpLWfTaskVo prpLWfTaskVo = sendMsgToMobileService.isMobileCaseAcceptInClaim(registNo,new BigDecimal(flowTaskId));
	         if(prpLWfTaskVo != null){ //发送通知
	             prpLWfTaskVo.setMobileNo(carMainVo.getSerialNo().toString());
	             prpLWfTaskVo.setMobileName(carMainVo.getLicenseNo());
	             prpLWfTaskVo.setMobileOperateType(CodeConstants.MobileOperationType.CARLOSSACCEPT);
	             String url = SpringProperties.getProperty("MClaimPlatform_URL_IN");
	             interfaceAsyncService.packMsg(prpLWfTaskVo,url);
	         }
		}catch (Exception e){
			e.printStackTrace();
			flag = e.getMessage();
		}
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(HttpStatus.SC_OK);
		ajaxResult.setData(flag);
		
		return ajaxResult;
	}
	
	/**
	 * 提交下一个页面
	 * @param lossMainId
	 * @param auditStatus 提交状态
	 * @modified:
	 * ☆yangkun(2016年1月16日 下午4:53:24): <br>
	 */
	@RequestMapping(value = "/submitNextPage.do")//页面加入意见
	public ModelAndView submitNextPage(Long lossMainId,String flowTaskId,String auditStatus) throws Exception{
		ModelAndView mv = new ModelAndView();	
		SysUserVo userVo = WebUserUtils.getUser();

		//判断是否总公司审核过
		PrpLDlossCarMainVo  lossCarMainVo = lossCarService.findLossCarMainById(lossMainId);
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("registNo",lossCarMainVo.getRegistNo());
		if(flowTaskId == null){
			params.put("taskId",BigDecimal.ZERO.doubleValue());
		} else{
			params.put("taskId",Double.valueOf(flowTaskId));
		}
		String isSubmitHeadOffice = managerService.isSubmitHeadOffice(params);
			SubmitNextVo nextVo = deflossHandleService.organizeNextVo(lossMainId,flowTaskId,auditStatus,"N",userVo,isSubmitHeadOffice);
			//复勘和复检默认原处理人，并可以选择同机构有权限的人员
			TreeMap<String,String> userMap = new TreeMap<String,String>();
			if(CodeConstants.AuditStatus.RECHECK.equals(auditStatus)){
				userMap = deflossHandleService.organizeUserMap(nextVo, auditStatus);
				mv.addObject("userMap",userMap);
			}else if(CodeConstants.AuditStatus.RELOSS.equals(nextVo.getAuditStatus())){
				userMap = deflossHandleService.organizeUserMap(nextVo, nextVo.getAuditStatus());
				mv.addObject("userMap",userMap);
				auditStatus = nextVo.getAuditStatus();
			}
			nextVo.setTaskInUser(WebUserUtils.getUserCode());
//			//设置默认下一个处理人,TODO　之后改成轮询机制处理
//			if(StringUtils.isBlank(nextVo.getAssignUser())){
//				nextVo.setAssignCom(SecurityUtils.getComCode());
//				nextVo.setAssignUser(SecurityUtils.getUserCode());
//			}
			mv.addObject("auditStatus",auditStatus);
			mv.addObject("nextVo",nextVo);
			mv.addObject("nextNodeMap",nextVo.getNodeMap());
			mv.setViewName("loss-common/SubmitNextPage");
			return mv;
		
	}
	
	/**
	 * 提交下一个节点
	 * @modified:
	 * ☆yangkun(2016年1月16日 下午6:26:02): <br>
	 */
	@RequestMapping(value = "/submitNextNode.do")
	public ModelAndView submitNextNode(@FormModel("nextVo")SubmitNextVo nextVo) throws Exception{
		ModelAndView mv = new ModelAndView();
		String lossMainId = nextVo.getTaskInKey();
		SysUserVo userVo = WebUserUtils.getUser();
		nextVo.setUserVo(userVo);
		PrpLDlossCarMainVo lossCarMainVo = lossCarService.findLossCarMainById(Long.parseLong(lossMainId));
		String operationLink = "";
		String operationResults = "";

		String yJsendFlag="0";//配件是否有未询价的
		if(lossCarMainVo!=null && lossCarMainVo.getPrpLDlossCarComps()!=null && lossCarMainVo.getPrpLDlossCarComps().size()>0){
			for(PrpLDlossCarCompVo compVo:lossCarMainVo.getPrpLDlossCarComps()){
				if(compVo.getMaterialFee().intValue()>=1000 && !"1".equals(compVo.getyJAskPrivceFlag())){
					yJsendFlag="1";
					break;
				}
			}
		}
		
		
		//精友一代与精友二代时间分割标识
		String jYFlag=SpringProperties.getProperty("JY_TIMESTAMP");
		if(StringUtils.isNotBlank(jYFlag) && FlowNode.DLCar.name().equals(nextVo.getCurrentNode()) && lossCarMainVo!=null && !"2".equals(lossCarMainVo.getDeflossSourceFlag())){
			PrpLDlossCarInfoVo prpLDlossCarInfoVo=lossCarService.findDefCarInfoByPk(lossCarMainVo.getCarId());
			PrpLRegistVo prpLRegistVo=registService.findRegistByRegistNo(lossCarMainVo.getRegistNo());
			Long timeAounts=DateUtils.strToDate(jYFlag,DateUtils.YToSec)!=null?DateUtils.strToDate(jYFlag,DateUtils.YToSec).getTime():0L;
			Long reportTimes=prpLRegistVo.getReportTime()!=null?prpLRegistVo.getReportTime().getTime():0L;
			if(reportTimes.longValue()>timeAounts.longValue() && prpLDlossCarInfoVo!=null  && "1".equals(yJsendFlag)){
				if("1".equals(lossCarMainVo.getDeflossCarType())){
					List<String> kindCodes=carkindCodes("1");//含有此种标的车车辆类型
					if(kindCodes.contains(prpLDlossCarInfoVo.getCarKindCode())){
						interfaceAsyncService.claimcarYJAskPriceAdd(lossCarMainVo.getId(),userVo);
					}
				}else{
					List<String> kindCodes=carkindCodes("3");//含有此种三者车车辆类型
					if(kindCodes.contains(prpLDlossCarInfoVo.getPlatformCarKindCode())){
						interfaceAsyncService.claimcarYJAskPriceAdd(lossCarMainVo.getId(),userVo);
					}
				}
				
			}
		}
		
		if((CodeConstants.AuditStatus.AUDIT.equals(lossCarMainVo.getAuditStatus()))){
		   //判断核损提交下一级时，如果当前为分公司最高级时，大案审核未通过，不能提交到总公司
	     	int maxLevel=lossCarMainVo.getMaxLevel();//最高级
		    String subNodeCode="VLCar_LV"+maxLevel;//分公司能审核的最高级
		   PrpLWfTaskVo  prpLWfTaskInVo =wfTaskHandleService.findWftaskInByRegistnoAndSubnode(lossCarMainVo.getRegistNo(),subNodeCode);
		 if(prpLWfTaskInVo!=null){
		   if(wfTaskHandleService.existTaskByNodeCode(lossCarMainVo.getRegistNo(),FlowNode.ChkBig,null,"0")){
						throw new IllegalArgumentException("车物大案审核未提交，核损不能提交！");
					}
				}
		}	
		nextVo.setAuditStatus(lossCarMainVo.getAuditStatus());
		
		
		
		String jyUrl = SpringProperties.getProperty("JY_URL");
		nextVo.setJyUrl(jyUrl);
		//移动端案件理赔处理要通知快赔 并写会理赔处理标识
        //写会标志
		 String url = SpringProperties.getProperty("MClaimPlatform_URL_IN");
         PrpLWfTaskVo prpLWfTaskVo = sendMsgToMobileService.isMobileCaseAcceptInClaim(lossCarMainVo.getRegistNo(),new BigDecimal(nextVo.getFlowTaskId()));
         if(prpLWfTaskVo != null && (FlowNode.valueOf(nextVo.getCurrentNode()).getUpperNode().equals(FlowNode.DLoss.name()) ||
                 FlowNode.valueOf(nextVo.getCurrentNode()).getUpperNode().equals(FlowNode.DLCar.name()))){ //发送通知  定损提交
             prpLWfTaskVo.setHandlerStatus("3"); //已处理
             prpLWfTaskVo.setWorkStatus("3");  // 提交
             prpLWfTaskVo.setMobileNo(lossCarMainVo.getSerialNo().toString());
             prpLWfTaskVo.setMobileName(lossCarMainVo.getLicenseNo());
             prpLWfTaskVo.setMobileOperateType(CodeConstants.MobileOperationType.CARLOSSSUBMIT);
             interfaceAsyncService.packMsg(prpLWfTaskVo,url);
             interfaceAsyncService.sendCarLossForGenilex(lossCarMainVo.getRegistNo(),nextVo.getFlowTaskId(), userVo);
         }
         //PC端处理
         lossCarMainVo.setIsMobileCase("0");
		// 提交工作流, Lundy 2018-12-26初步怀疑是提交工作流出现了异常，之后上传平台的代码也被 中断，导致“车辆自动核损停留在未处理状态且未指定处理人（车辆定损修改-自动核损）”，加catch捕捉异常。
		List<PrpLWfTaskVo> taskVoList = null;
		
		try{
			Date currentDate = new Date();
			logger.info("registNo: "+lossCarMainVo.getRegistNo()+" ,deflossHandleService.submitNextNode start...");
			taskVoList = deflossHandleService.submitNextNode(lossCarMainVo,nextVo);
			logger.info("registNo: "+lossCarMainVo.getRegistNo()+" ,deflossHandleService.submitNextNode end, 耗时 "+( System.currentTimeMillis()-currentDate
					.getTime() )+"毫秒");
		}catch(Exception e1){
			logger.error("registNo: "+lossCarMainVo.getRegistNo()+" ,deflossHandleService.submitNextNode error: ",e1);
			throw e1;
		}
         if(prpLWfTaskVo != null && (FlowNode.valueOf(nextVo.getCurrentNode()).getUpperNode().equals(FlowNode.VLCar.name())||
        		 FlowNode.valueOf(nextVo.getCurrentNode()).getUpperNode().equals(FlowNode.VPCar.name())) && 
                 CodeConstants.AuditStatus.BACKLOSS.equals(lossCarMainVo.getAuditStatus())){ //核价核损退回定损
        	 //退回案件要传被退回定损的taskId
        	 PrpLWfTaskVo upWfTaskVo = wfTaskHandleService.queryTask(Double.valueOf(prpLWfTaskVo.getUpperTaskId().toString()));
        	 while(!FlowNode.DLoss.name().equals(upWfTaskVo.getNodeCode())){
        		 upWfTaskVo = wfTaskHandleService.queryTask(Double.valueOf(upWfTaskVo.getUpperTaskId().toString()));
        	 }
        	 //查询退回后的taskid
        	 List<PrpLWfTaskVo> nextWfTaskVoList = wfTaskHandleService.findInTask(prpLWfTaskVo.getRegistNo(), prpLWfTaskVo.getHandlerIdKey(), FlowNode.DLCar.name());
        	 if(nextWfTaskVoList!=null && nextWfTaskVoList.size()>0){
        		 prpLWfTaskVo.setTaskId(nextWfTaskVoList.get(0).getTaskId());
        	 }
        	 prpLWfTaskVo.setOriginalTaskId(upWfTaskVo.getTaskId());
             prpLWfTaskVo.setHandlerStatus("0"); //未处理
             prpLWfTaskVo.setWorkStatus("6");  // 退回
             prpLWfTaskVo.setMobileNo(lossCarMainVo.getSerialNo().toString());
             prpLWfTaskVo.setMobileName(lossCarMainVo.getLicenseNo());
             prpLWfTaskVo.setMobileOperateType(CodeConstants.MobileOperationType.CARVERILOSSBACK);
             interfaceAsyncService.packMsg(prpLWfTaskVo,url);
         }
				
		//核损通过刷未决 或者 自动核价,核损任务
		if(CodeConstants.AuditStatus.SUBMITVLOSS.equals(lossCarMainVo.getAuditStatus()) || "1".equals(nextVo.getAutoLossFlag())
				 || "1".equals(nextVo.getAutoPriceFlag())){
			// 车辆核损送平台
			try{
				interfaceAsyncService.sendLossToPlatform(lossCarMainVo.getRegistNo(),null);
				
				
			}catch(Exception e){
				logger.error("报案号registno=" + (lossCarMainVo == null ? null :lossCarMainVo.getRegistNo()) + "车辆核损送平台失败！" ,e);
			}
			//自动核价--德联易控
			if("1".equals(nextVo.getAutoPriceFlag())){
			   PrpLDlossCarMainVo carMainVo = lossCarService.findLossCarMainById(lossCarMainVo.getId());
			   String Qurl=SpringProperties.getProperty("YX_QUrl");
	           interfaceAsyncService.SendControlExpert(carMainVo.getRegistNo(), userVo, carMainVo.getLicenseNo(), String.valueOf(carMainVo.getId()), "15",Qurl);
			
			}
			//自动核损--德联易控
			if("1".equals(nextVo.getAutoLossFlag())){
				   PrpLDlossCarMainVo carMainVo = lossCarService.findLossCarMainById(lossCarMainVo.getId());
				   String Qurl=SpringProperties.getProperty("YX_QUrl");
		           interfaceAsyncService.SendControlExpert(carMainVo.getRegistNo(), userVo, carMainVo.getLicenseNo(), String.valueOf(carMainVo.getId()),"14",Qurl);
				
			}
			try{
				//调用影像系统“影像资料统计接口”，查询该工号在该任务中上传的影像数量并保存（异步执行）
				String imageUrl = SpringProperties.getProperty("YX_QUrl")+"?";
				interfaceAsyncService.getReqImageNum(userVo, CodeConstants.APPROLE, lossCarMainVo.getRegistNo(), "", imageUrl,CodeConstants.APPNAMECLAIM,CodeConstants.APPCODECLAIM);
				interfaceAsyncService.getReqCheckUserImageNum(userVo, CodeConstants.APPROLE, lossCarMainVo.getRegistNo(),imageUrl,CodeConstants.APPNAMECLAIM,CodeConstants.APPCODECLAIM);
			}catch(Exception e){
				logger.info("车辆核损调用影像系统影像资料统计接口报错=============", e);
			}
			
			
			if(lossCarMainVo!=null){//自助理赔
				PrpLRegistVo prpLRegistVo=registService.findRegistByRegistNo(lossCarMainVo.getRegistNo());
				if(prpLRegistVo!=null && "1".equals(prpLRegistVo.getSelfClaimFlag())){  
					Boolean flag=certifyIlogService.validAllVLossPass(lossCarMainVo.getRegistNo());
					if(flag){
						interfaceAsyncService.sendClaimResultToSelfClaim(lossCarMainVo.getRegistNo(), userVo,"5","2","");
					}
					
				}
			}
			//判断是否为最后一个核损，请求Ilog
			PrpLConfigValueVo configValueIlogVo = ConfigUtil.findConfigByCode(CodeConstants.ILOGVALIDFLAG,WebUserUtils.getComCode());
			PrpLConfigValueVo configValueIRuleVo = ConfigUtil.findConfigByCode(CodeConstants.RULEVALIDFLAG,WebUserUtils.getComCode());
			if("1".equals(configValueIlogVo.getConfigValue()) && "0".equals(configValueIRuleVo.getConfigValue())){
				Boolean flag=certifyIlogService.validAllVLossPass(lossCarMainVo.getRegistNo());
				if(flag){
					
					LIlogRuleResVo resVo=certifyIlogService.sendAutoCertifyRule(lossCarMainVo.getRegistNo(),userVo,new BigDecimal(nextVo.getFlowTaskId()),nextVo.getCurrentNode());
					if(resVo!=null && "1".equals(resVo.getUnderwriterflag()) && certifyService.isPassPlatform(lossCarMainVo.getRegistNo())){
						WfTaskSubmitVo submitVo=certifyIlogService.autoCertify(lossCarMainVo.getRegistNo(),userVo);//自动单证
						// 单证送平台
						 try{
							 certifyToPaltformService.certifyToPaltform(lossCarMainVo.getRegistNo(),null);
						 }catch(Exception e){
							 logger.error("报案号" +(lossCarMainVo == null ? null : lossCarMainVo.getRegistNo()) + "车损提交自动单证送平台异常信息-------------->",e);
						 }
						// 调用ilog查询是否可自动理算
						boolean NotExistObj = compensateTaskService.adjustNotExistObj(lossCarMainVo.getRegistNo());
						if("1".equals(configValueIlogVo.getConfigValue()) && StringUtils.isNotBlank(submitVo.getFlowId())&&!NotExistObj){
	                        //==============事务问题开始
                            String registNo = lossCarMainVo.getRegistNo();
                            LIlogRuleResVo ruleResVo = certifyIlogService.sendAutoCertifyRule(registNo,userVo,submitVo.getFlowTaskId(),submitVo.getCurrentNode().toString());
                            /** 兜底人员权限判断 start **/
                            String finalPowerFlag =  SpringProperties.getProperty("FINALPOWERFLAG");
            	        	boolean finalAutoPass = true;
            	        	if ("1".equals(finalPowerFlag)) {
            	        		IlogFinalPowerInfoVo powerInfoVo = ilogRuleService.findByUserCode(userVo.getUserCode());
            	        		if (powerInfoVo != null) {
            	        			BigDecimal gradePower = powerInfoVo.getGradeAmount();
            	        			if (gradePower != null) {
            	        				// 总定损金额
            	        				BigDecimal sumAmount = BigDecimal.ZERO;
            	        				// 定损车辆信息
            	        				List<PrpLDlossCarMainVo> losscarMainList = deflossHandleService.findLossCarMainByRegistNo(registNo);
            	        				// 人伤定损信息
            	        				List<PrpLDlossPersTraceMainVo> losspersTraceList = deflossHandleService.findlossPersTraceMainByRegistNo(registNo);
            	        				// 财产定损信息
            	        				List<PrpLdlossPropMainVo> propmianList = propTaskService.findPropMainListByRegistNo(registNo);
            	        				if (losscarMainList != null && losscarMainList.size() > 0) {
            	        					for (PrpLDlossCarMainVo vo : losscarMainList) {
            	        						sumAmount = sumAmount.add(new BigDecimal(vo.getSumVeriLossFee() == null ? "0" : vo.getSumVeriLossFee().toString()));
            	        					}
            	        				}
            	        				
            	        				if (losspersTraceList != null && losspersTraceList.size() > 0) {
            	        					for (PrpLDlossPersTraceMainVo vo : losspersTraceList) {
            	        						if (vo.getPrpLDlossPersTraces() != null && vo.getPrpLDlossPersTraces().size() > 0) {
            	        							for (PrpLDlossPersTraceVo traceVo : vo.getPrpLDlossPersTraces()) {
            	        								sumAmount = sumAmount.add(DataUtils.NullToZero(traceVo.getSumVeriDefloss()));
            	        							}
            	        						}
            	        					}
            	        				}
            	        				
            	        				if (propmianList != null && propmianList.size() > 0) {
            	        					for (PrpLdlossPropMainVo vo : propmianList) {
            	        						sumAmount = sumAmount.add(new BigDecimal(vo.getSumVeriLoss() == null ? "0" : vo.getSumVeriLoss().toString()));
            	        					}
            	        				}
            	        				
            	        				if (sumAmount.compareTo(gradePower) == 1) {
            	        					finalAutoPass = false;
            	        				}
            	        			}
            	        		} else {
            	        			finalAutoPass = false;
            	        		}
            	        	}
							/** 兜底人员权限判断  end  **/
                            
                            if("1".equals(ruleResVo.getUnderwriterflag()) && finalAutoPass){//自动理算通过
                                List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findInTaskByOther(registNo,null,FlowNode.Compe.toString());
                                if(prpLWfTaskVoList!=null&&prpLWfTaskVoList.size()>0){
                                    for(PrpLWfTaskVo taskVo1:prpLWfTaskVoList){
                                        PrpLCompensateVo compVo = compensateTaskService.autoCompTask(taskVo1,userVo);
                                        Boolean autoVerifyFlag = false;
                                        Map<String,Object> params = new HashMap<String,Object>();
                                        params.put("registNo",compVo.getRegistNo());
                                        if(taskVo1.getTaskId() == null){
                                        	 params.put("taskId",BigDecimal.ZERO.doubleValue());
                                        } else{
                                        	 params.put("taskId",taskVo1.getTaskId().doubleValue());
                                        }
                                        String isSubmitHeadOffice = managerService.isSubmitHeadOffice(params);
                                        WfTaskSubmitVo nextVo1 = compensateTaskService.getCompensateSubmitNextVo(compVo.getCompensateNo(),taskVo1.getTaskId().doubleValue(),taskVo1,userVo,autoVerifyFlag,isSubmitHeadOffice);
                                        if(nextVo1.getSubmitLevel()==0){
                                            autoVerifyFlag = true;
                                        }
                                        if(autoVerifyFlag){
                                            // 自动核赔标识为true，理算提交后执行自动核赔
                                            Long uwNotionMainId = verifyClaimService.autoVerifyClaimEndCase(userVo,compVo);
                                            //核赔提交结案
                                            verifyClaimService.autoVerifyClaimToFlowEndCase(userVo, compVo,uwNotionMainId);
                                            //核赔通过送收付、再保
                                            try{
                                                verifyClaimService.sendCompensateToPayment(uwNotionMainId);
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                        
                                    }
                                }
                            }
                            //==============事务问题结束
						}
					}
					
				}
			}
			
			try{
				claimTaskService.updateClaimFee(lossCarMainVo.getRegistNo(),nextVo.getTaskInUser(),FlowNode.VLCar);//刷新立案
			}catch (Exception e){
//				e.printStackTrace();
//				throw new IllegalStateException("刷新立案未决失败！");
				logger.error("报案号"+ (lossCarMainVo == null ?null :lossCarMainVo.getRegistNo())+"车辆核损通过刷未决 或者 自动核损任务刷新立案未决失败！" , e);
			}
		}
		//全部核损通过送河南快赔，结过案的不送
		try{
			List<PrpLEndCaseVo> endCaseVoList = endCasePubService.queryAllByRegistNo(lossCarMainVo.getRegistNo());
			if(endCaseVoList==null || endCaseVoList.size()==0){
				deflossHandleService.sendHNQC(lossCarMainVo.getRegistNo(),userVo);
			}
		}catch(Exception e){
			logger.error("报案号"+ (lossCarMainVo == null ?null :lossCarMainVo.getRegistNo())+"全部核损通过送河南快赔，结过案的不送失败！" , e);
		}
		
		String taskIds ="";
		for(PrpLWfTaskVo taskVo : taskVoList){
			if("".equals(taskIds)){
				taskIds= taskVo.getTaskId()+"";
			}else{
				taskIds = taskIds + ","+taskVo.getTaskId();
			}
		}
		String autoFlag = "";
		if("1".equals(nextVo.getAutoLossFlag()) && "1".equals(nextVo.getAutoPriceFlag())){
			autoFlag = "1";
		}
		if(StringUtils.isNotBlank(nextVo.getFinalNextNode()) && !"2".equals(lossCarMainVo.getDeflossSourceFlag())){
			if(FlowNode.valueOf(nextVo.getFinalNextNode()).equals(FlowNode.DLChk.name())){
				//阳杰发起复检startDLChk
				//定损车辆为客车、货车时（后续会提供车型代码），且存在【零部件更换费用清单】定损价格大于等于5000时，发起复检时，自动发起阳杰复检（只推送定损价格大于等于5000零配件）
				List<PrpLDlossCarCompVo> prpLDlossCarComps = lossCarMainVo.getPrpLDlossCarComps();
				if(prpLDlossCarComps != null && prpLDlossCarComps.size() > 0 ){
					String yjFlag="0";//0--表示不存在未询过价的超过5000的零部件，1--表示存在
					for(PrpLDlossCarCompVo compvo:prpLDlossCarComps){
						if(!"1".equals(compvo.getIsYangJie()) && compvo.getMaterialFee() != null && (compvo.getMaterialFee().compareTo(new BigDecimal(5000)) >= 0)){
							yjFlag="1";
							break;
						}
					}
					if("1".equals(yjFlag)){
						PrpLDlossCarMainVo carMainVo = lossCarService.findLossCarMainById(lossCarMainVo.getId());
						PrpLDlossCarInfoVo prpLDlossCarInfoVo=lossCarService.findDefCarInfoByPk(lossCarMainVo.getCarId());
						if("1".equals(lossCarMainVo.getDeflossCarType())){
							List<String> kindCodes=carkindCodes("1");//含有此种标的车车辆类型
							if(prpLDlossCarInfoVo!=null && kindCodes.contains(prpLDlossCarInfoVo.getCarKindCode())){
								yjInteractionService.sendDlChkInfoService(carMainVo,userVo);
							}
						}else{
							List<String> kindCodes=carkindCodes("3");//含有此种三者车车辆类型
							if(prpLDlossCarInfoVo!=null && kindCodes.contains(prpLDlossCarInfoVo.getPlatformCarKindCode())){
								yjInteractionService.sendDlChkInfoService(carMainVo,userVo);
							}
						}
					}
					
				}
				//阳杰发起复检end
			}
		}
		
		if(FlowNode.valueOf(nextVo.getCurrentNode()).getUpperNode().equals(FlowNode.VLCar.name()) &&
				CodeConstants.AuditStatus.SUBMITVLOSS.equals(lossCarMainVo.getAuditStatus()) && !"2".equals(lossCarMainVo.getDeflossSourceFlag())){
			//核损通过
			List<PrpLDlossCarCompVo> prpLDlossCarComps = lossCarMainVo.getPrpLDlossCarComps();
			if(prpLDlossCarComps != null && prpLDlossCarComps.size() > 0 ){
				String yjFlag="0";//0--表示零配件未询过价，1--表示存在询过价的零配件
				for(PrpLDlossCarCompVo compvo:prpLDlossCarComps){
					if("1".equals(compvo.getyJAskPrivceFlag())){
						yjFlag="1";
						break;
					}
				}
				if("1".equals(yjFlag)){
					yjInteractionService.sendVLossInfoService(lossCarMainVo, userVo);
				}
				
			}
		}
		
		PrpLRegistVo prpLRegistVo =registService.findRegistByRegistNo(lossCarMainVo.getRegistNo());
		if(FlowNode.valueOf(nextVo.getCurrentNode()).equals(FlowNode.DLChk.name())){//复检
			operationLink = "10";
			operationResults = "029";
		}else if(FlowNode.valueOf(nextVo.getCurrentNode()).getUpperNode().equals(FlowNode.DLoss.name()) ||
	    		FlowNode.valueOf(nextVo.getCurrentNode()).getUpperNode().equals(FlowNode.DLCar.name())){
	    	operationLink = "05";//定损
	    	if("1".equals(nextVo.getAutoPriceFlag())){//定损提交到核价,自动核价 
	    		operationResults = "012";//因为自动核价后就直接是核损传12
	    	}else{
		    	for(PrpLWfTaskVo taskVo : taskVoList){
		        	if(StringUtils.isNotBlank(taskVo.getNodeCode())){
		        		if(FlowNode.VPrice.equals(FlowNode.valueOf(taskVo.getNodeCode())) ||
			    				"1".equals(nextVo.getAutoPriceFlag())){//定损提交到核价
			    			operationResults = "011";
			    			break;
			    		}else if(FlowNode.VLoss.equals(FlowNode.valueOf(taskVo.getNodeCode())) ||
			    				"1".equals(nextVo.getAutoLossFlag())){//定损提交到核损
			    			operationResults = "012";
			    			break;
			    		}
		        	}else{
		        		taskVo = wfFlowService.findPrpLWfTaskQueryByTaskId(taskVo.getTaskId());
		        		if(FlowNode.VPrice.equals(FlowNode.valueOf(taskVo.getNodeCode())) ||
			    				"1".equals(nextVo.getAutoPriceFlag())){//定损提交到核价,自动核价 
			    			operationResults = "011";
			    			break;
			    		}else if(FlowNode.VLoss.equals(FlowNode.valueOf(taskVo.getNodeCode())) ||
			    				"1".equals(nextVo.getAutoLossFlag())){//定损提交到核损,自动核损
			    			operationResults = "012";
			    			break;
			    		}
		        	}
		    	}
	    	}

	    	boolean JyFlag = false;
	    	if("02".equals(lossCarMainVo.getCetainLossType())){//推定全损
    			if(deflossHandleService.isJyTwo(prpLRegistVo.getReportTime())){
    				interfaceAsyncService.sendCleanDataService(lossCarMainVo.getRegistNo(), lossCarMainVo.getId().toString(), userVo);
    			}
			}
			if("05".equals(lossCarMainVo.getCetainLossType())){//无损失
    			if(deflossHandleService.isJyTwo(prpLRegistVo.getReportTime())){
    				//定损零结通知接口
    				interfaceAsyncService.sendZeroNoticeService(lossCarMainVo.getRegistNo(), lossCarMainVo.getId().toString(), userVo);
	    		}
	    	}
	    }else if(FlowNode.valueOf(nextVo.getCurrentNode()).getUpperNode().equals(FlowNode.VPCar.name())){
	    	operationLink = "06";//核价
	    	if( CodeConstants.AuditStatus.BACKLOSS.equals(lossCarMainVo.getAuditStatus())){ //核价退回定损)
	    		operationResults = "013";
	    	}else if(CodeConstants.AuditStatus.AUDIT.equals(lossCarMainVo.getAuditStatus())){
	    		operationResults = "023";//核价上报核价
			}else if(CodeConstants.AuditStatus.BACKLOWER.equals(lossCarMainVo.getAuditStatus())){
	    		operationResults = "024";//核价上报退回
			}else{
				for(PrpLWfTaskVo taskVo : taskVoList){
					if(StringUtils.isNotBlank(taskVo.getNodeCode())){
						if(FlowNode.VLoss.equals(FlowNode.valueOf(taskVo.getNodeCode()))){//核价通过，提交到核损
			    			operationResults = "015";
			    			break;
			    		}
		        	}else{
		        		taskVo = wfFlowService.findPrpLWfTaskQueryByTaskId(taskVo.getTaskId());
		        		if(FlowNode.VLoss.equals(FlowNode.valueOf(taskVo.getNodeCode()))){//核价通过，提交到核损
			    			operationResults = "015";
			    			break;
			    		}
		        	}
		    		
		    	}
			}
	    }else if(FlowNode.valueOf(nextVo.getCurrentNode()).getUpperNode().equals(FlowNode.VLCar.name())){
	    	operationLink = "07";//核损
	    	if( CodeConstants.AuditStatus.BACKLOSS.equals(lossCarMainVo.getAuditStatus())){ //核损退回定损)
	    		operationResults = "014";
	    	}else if(CodeConstants.AuditStatus.SUBMITVLOSS.equals(lossCarMainVo.getAuditStatus())){
	    		operationResults = "016";//核损通过
			}else if(CodeConstants.AuditStatus.AUDIT.equals(lossCarMainVo.getAuditStatus())){
	    		operationResults = "025";//核损上报核损
			}else{
				for(PrpLWfTaskVo taskVo : taskVoList){
		    		if(FlowNode.VPrice.equals(FlowNode.valueOf(taskVo.getNodeCode()))){//核损退回到核价
		    			operationResults = "017";
		    			break;
		    		}else if(FlowNode.VLoss.equals(FlowNode.valueOf(taskVo.getNodeCode()))){
		    			operationResults = "026";//核损上报退回
		    			break;
		    		}else if(FlowNode.DLChk.equals(FlowNode.valueOf(taskVo.getSubNodeCode()))){//核损提交到复检
		    			operationResults = "028";
		    			break;
		    		}
		    	}
			}
	    }
		
		if(deflossHandleService.isJyTwo(prpLRegistVo.getReportTime())){
			interfaceAsyncService.sendTaskInfoService(lossCarMainVo.getRegistNo(), lossCarMainVo.getId().toString(),
					operationLink, operationResults,nextVo.getCurrentNode(), userVo);
		}
		//退回定损回写，
		if(FlowNode.DLCar.name().equals(nextVo.getFinalNextNode())){
			PrpLAcheckVo prpLAcheckVo =acheckService.findPrpLAcheckVo(lossCarMainVo.getRegistNo(),"1", "0", lossCarMainVo.getLicenseNo());
			if(prpLAcheckVo!=null){
				prpLAcheckVo.setUnderWriteFlag("7");//退回
				acheckService.updatePrpLAcheck(prpLAcheckVo);
			}
		}
		//请求德联易控的接口--定损
        //04-表示定损节点请求
		if(StringUtils.isNotBlank(nextVo.getCurrentNode()) && nextVo.getCurrentNode().contains(FlowNode.DLCar.name())){
			PrpLDlossCarMainVo carMainVo = lossCarService.findLossCarMainById(lossCarMainVo.getId());
			 String Qurl=SpringProperties.getProperty("YX_QUrl");
           interfaceAsyncService.SendControlExpert(carMainVo.getRegistNo(), userVo, carMainVo.getLicenseNo(), String.valueOf(carMainVo.getId()), "04",Qurl);
		}
		//请求德联易控的接口--核价
        // 15-表示定损节点请求
		if(StringUtils.isNotBlank(nextVo.getCurrentNode()) && nextVo.getCurrentNode().contains(FlowNode.VPCar.name()) && 
				StringUtils.isNotBlank(nextVo.getFinalNextNode()) && nextVo.getFinalNextNode().contains(FlowNode.VLCar.name())){
			PrpLDlossCarMainVo carMainVo = lossCarService.findLossCarMainById(lossCarMainVo.getId());
			 String Qurl=SpringProperties.getProperty("YX_QUrl");
           interfaceAsyncService.SendControlExpert(carMainVo.getRegistNo(), userVo, carMainVo.getLicenseNo(), String.valueOf(carMainVo.getId()), "15",Qurl);
		}
		//请求德联易控的接口--核损
        // 14-表示定损节点请求
		if(StringUtils.isNotBlank(nextVo.getCurrentNode()) && nextVo.getCurrentNode().contains(FlowNode.VLCar.name()) && "1".equals(nextVo.getEndFlag())){
			PrpLDlossCarMainVo carMainVo = lossCarService.findLossCarMainById(lossCarMainVo.getId());
			 String Qurl=SpringProperties.getProperty("YX_QUrl");
           interfaceAsyncService.SendControlExpert(carMainVo.getRegistNo(), userVo, carMainVo.getLicenseNo(), String.valueOf(carMainVo.getId()), "14",Qurl);
		}
		mv.setViewName("redirect:/defloss/nextTaskView.do");
		mv.addObject("registNo",lossCarMainVo.getRegistNo());
		mv.addObject("taskIds",taskIds);
		mv.addObject("autoFlag",autoFlag);
		return mv;
		
	}
	
	
	@RequestMapping(value = "/nextTaskView.do")
	public ModelAndView nextTaskView(String taskIds,String registNo,String autoFlag) throws Exception{
		ModelAndView mv = new ModelAndView();
		List<PrpLWfTaskVo> taskVoList = new ArrayList<PrpLWfTaskVo>(); 
		String userCode = WebUserUtils.getUserCode();
		if(taskIds!=null && !"".equals(taskIds)){
			String[] taskIdArray = taskIds.split(",");
			for(String taskId : taskIdArray){
				PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(Double.valueOf(taskId));
				if("1".equals(autoFlag)){
					taskVo.setTaskName("自动核价核损");
				}
//				wfTaskVo.handlerStatus ne '3'  && wfTaskVo.assignUser eq userCode}
				if(!"3".equals(taskVo.getHandlerStatus()) 
						&& userCode.equals(taskVo.getAssignUser())
						&& !taskVo.getSubNodeCode().startsWith("Compe")){
					
					taskVo.setQuickFlag("1");
				}else{
					taskVo.setQuickFlag("0");
				}
				
				taskVoList.add(taskVo);
			}
		}
		
		mv.setViewName("loss-common/NextTaskVeiw");
		mv.addObject("registNo",registNo);
		mv.addObject("userCode",WebUserUtils.getUserCode());//
		mv.addObject("wfTaskVoList",taskVoList);
		return mv;
		
	}
	
	/**
	 * 核价初始化
	 */
	@RequestMapping(value = "/preAddVerifyPrice.do")
	public ModelAndView preAddVerifyPrice(Double flowTaskId) {
		ModelAndView mv = new ModelAndView();
		DeflossActionVo deflossVo = deflossHandleService.prepareAddVerifyPrice(flowTaskId,WebUserUtils.getUser());
		String registNo="";//报案号
		String insuredName="";//被保险人
		String driverName="";//驾驶员
		String frameNo="";//标的车车架号
		Date registTime=null;//报案时间
		if(deflossVo.getLossCarMainVo()!=null){
    	    registNo=deflossVo.getLossCarMainVo().getRegistNo();
    	    List<PrpLCMainVo> prpLCMainVos= checkHandleService.getPolicyAllInfo(registNo);
    	    if(prpLCMainVos!=null && prpLCMainVos.size()>0){
    		    //获取被保险人，如果有商业就优先取商业，没有商业就取交强的被保险人
    		    for(PrpLCMainVo prpM:prpLCMainVos){
    			    if(!"1101".equals(prpM.getRiskCode())){
    				    insuredName=prpM.getInsuredName();
    				    break;
    				}else{  					
    					insuredName=prpM.getInsuredName(); 
    				}
    		   }
    	    }
    	    //获取驾驶员，报案时间
    	    PrpLRegistVo prpLRegistVo =registService.findRegistByRegistNo(registNo);
    	    if(prpLRegistVo!=null){
    		    driverName=prpLRegistVo.getDriverName();
    		    registTime=prpLRegistVo.getReportTime();
    		}
    	    //获取标的车的车架号
    	    List<PrpLCheckCarVo>  prpLCheckCarVos=checkTaskService.findCheckCarVo(registNo);
    	    if(prpLCheckCarVos!=null && prpLCheckCarVos.size()>0){
    		    for(PrpLCheckCarVo prpLCheckCarVo:prpLCheckCarVos){
    			    if(1==prpLCheckCarVo.getSerialNo()){
    				    PrpLCheckCarInfoVo carInfoVo=checkTaskService.findPrpLCheckCarInfoVoById(prpLCheckCarVo.getCarid());
    				    if(carInfoVo!=null){
    					    frameNo=carInfoVo.getFrameNo();
    				    }
    			    }
    		    }
    	    }
    	   
        }
		
	    //页面公估费查看按钮是否亮显
	    String assessSign="0";
	    if(deflossVo.getLossCarMainVo()!=null){
	    	List<PrpLAssessorFeeVo> listFeeVo=assessorService.findPrpLAssessorFeeVoByRegistNo(deflossVo.getLossCarMainVo().getRegistNo());
	     	if(listFeeVo!=null && listFeeVo.size()>0){
	     		for(PrpLAssessorFeeVo vo :listFeeVo){
					PrpLAssessorMainVo assessMainVo=assessorService.findAssessorMainVoById(vo.getAssessMainId());
					if(assessMainVo!=null && "3".equals(assessMainVo.getUnderWriteFlag())){
						assessSign="1";
						break;
					}
				}
	     	}
	    }
     	if(deflossVo.getLossCarMainVo().getDeflossCarType().equals("1")){
     		PrpLCItemCarVo cItemCarVo = registQueryService.findCItemCarByRegistNo(deflossVo.getLossCarMainVo().getRegistNo());
     		mv.addObject("prpLCItemCarVo",cItemCarVo);
     	}
     	mv.addObject("assessSign",assessSign);
        mv.addObject("insuredName",insuredName);
        mv.addObject("driverName",driverName);
        mv.addObject("frameNo",frameNo);
        mv.addObject("registTime",registTime);
		mv.addObject("lossCarMainVo",deflossVo.getLossCarMainVo());
		mv.addObject("claimTextVos",deflossVo.getClaimTextVos());
		mv.addObject("claimTextVo",deflossVo.getClaimTextVo());
		mv.addObject("carInfoVo",deflossVo.getCarInfoVo());
		mv.addObject("commonVo",deflossVo.getCommonVo());
		mv.addObject("taskVo",deflossVo.getTaskVo());
		
		//山东预警信信息按钮是否显示，山东保单显示，其它不显示
		String policeInfoFlag="0";//显示标志
		String policyComCode = policyViewService.getPolicyComCode(registNo);
		if(policyComCode.startsWith("62")){
			policeInfoFlag = "1";
		     //山东影像对比
	        String claimSequenceNo = "";
	        List<PrpLCMainVo> prpLCMains = prpLCMainService.findPrpLCMainsByRegistNo(registNo);
	        if(prpLCMains != null && prpLCMains.size() > 1){//有商业取商业
	            for(PrpLCMainVo vo : prpLCMains){
	                if("12".equals(vo.getRiskCode().substring(0, 2))){
	                    if(StringUtils.isNotBlank(vo.getClaimSequenceNo())){
	                        claimSequenceNo = vo.getClaimSequenceNo();
	                        mv.addObject("claimSequenceNo",claimSequenceNo);
	                    }
	                }
	            }  
	        }else if(prpLCMains != null && prpLCMains.size() == 1){
	            PrpLCMainVo vo = prpLCMains.get(0);
	            if(StringUtils.isNotBlank(vo.getClaimSequenceNo())){
	                claimSequenceNo = vo.getClaimSequenceNo();
	                mv.addObject("claimSequenceNo",claimSequenceNo);
	            }
	        }
	        String carRiskUrl = SpringProperties.getProperty("CARRISK_URL");
            String carRiskUserName = SpringProperties.getProperty("SDWARN_YMUSER");
            String carRiskPassWord = SpringProperties.getProperty("SDWARN_YMPW");
	        mv.addObject("comparePicURL",carRiskUrl);
	        String claimPeriod = "04";
	        mv.addObject("claimPeriod",claimPeriod);
	        mv.addObject("carRiskUserName",carRiskUserName);
	        mv.addObject("carRiskPassWord",carRiskPassWord);
		}
		mv.addObject("policeInfoFlag",policeInfoFlag);
        
		//阳杰查看展示标志0-不展示，1展示
		String yjAskFlag=CodeConstants.YN01.N;
		if(deflossVo.getLossCarMainVo()!=null && deflossVo.getLossCarMainVo().getPrpLDlossCarComps()!=null && deflossVo.getLossCarMainVo().getPrpLDlossCarComps().size()>0){
 			for(PrpLDlossCarCompVo compVo:deflossVo.getLossCarMainVo().getPrpLDlossCarComps()){
				if("1".equals(compVo.getyJAskPrivceFlag())){
					yjAskFlag=CodeConstants.YN01.Y;
					break;
				}
			}
 		}
		mv.addObject("yjAskFlag",yjAskFlag);
		
        //判断是否有ILOG规则信息查看权限
        String nodeCode=deflossVo.getTaskVo().getSubNodeCode();
        String roleFlag="0";
		SysUserVo userVo = WebUserUtils.getUser();
		String grades="";
		SaaUserPowerVo saaUserPowerVo=saaUserPowerService.findUserPower(userVo.getUserCode());		
		List<FlowNode> flowNodeList=FlowNode.valueOf(nodeCode).getNextLevelNodes();
		for(FlowNode flowNode:flowNodeList){
			grades=grades+flowNode.getRoleCode()+",";
		}
		
		if(saaUserPowerVo!=null&&saaUserPowerVo.getRoleList().size()>0){			
			for(String gradeId:saaUserPowerVo.getRoleList()){
				if(grades.indexOf(gradeId)>-1){
					roleFlag="1";
					break;
				}
			}
		}
		mv.addObject("roleFlag",roleFlag);
		String jy2Flag = "0";
		PrpLRegistVo prpLRegistVo =registService.findRegistByRegistNo(registNo);
		if(deflossHandleService.isJyTwo(prpLRegistVo.getReportTime())){
			jy2Flag = "1";
		}
		mv.addObject("jy2Flag",jy2Flag);
		mv.setViewName("verifyPrice/VerifyPriceEdit");
		return mv;
	}
	
	@RequestMapping(value = "/addVerifyPrice.do") 
	@ResponseBody
	public AjaxResult addVerifyPrice(@FormModel("lossCarMainVo") PrpLDlossCarMainVo lossCarMainVo,
	 	                             @FormModel("claimTextVo") PrpLClaimTextVo claimTextVo,
	 	                             @FormModel("taskVo") PrpLWfTaskVo taskVo,
	 	                             String saveType) {
		AjaxResult ajaxResult = new AjaxResult();
		try{
			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(taskVo.getTaskId().doubleValue());
			if(CodeConstants.HandlerStatus.END.equals(wfTaskVo.getHandlerStatus())){
				throw new IllegalArgumentException("该任务已处理完成，请刷新页面！");
			}
			
			SysUserVo userVo = WebUserUtils.getUser();
			deflossHandleService.saveVerifyPrice(lossCarMainVo,claimTextVo,taskVo,userVo);
	
			String returnStr = lossCarMainVo.getId()+","+taskVo.getTaskId();
			
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(returnStr);
		}catch(Exception e){
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		return ajaxResult;
		
	}
	/**
	 * 核损初始化
	 * ☆yangkun(2016年1月6日 上午9:17:41): <br>
	 */

	@RequestMapping(value = "/preAddVerifyLoss.do")
	public ModelAndView preAddVerifyLoss(Double flowTaskId) {
		ModelAndView mv = new ModelAndView();
		
		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(flowTaskId);
		DeflossActionVo deflossVo = deflossHandleService.prepareAddVerifyLoss(taskVo,WebUserUtils.getUser());
		deflossVo = deflossHandleService.isVerifyLossChanged(deflossVo); //核价 核损 颜色改变
		
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.ASSESSORFEE,WebUserUtils.getComCode());
		String configValue = "0";
		if(configValueVo!=null){
			configValue = configValueVo.getConfigValue();
		}
		String registNo="";//报案号
		String insuredName="";//被保险人
		String driverName="";//驾驶员
		String frameNo="";//标的车车架号
		Date registTime=null;//报案时间
       if(deflossVo.getLossCarMainVo()!=null){
    	   registNo=deflossVo.getLossCarMainVo().getRegistNo();
    	   List<PrpLCMainVo> prpLCMainVos= checkHandleService.getPolicyAllInfo(registNo);
    	   if(prpLCMainVos!=null && prpLCMainVos.size()>0){
    		   //获取被保险人，如果有商业就优先取商业，没有商业就取交强的被保险人
    		   for(PrpLCMainVo prpM:prpLCMainVos){
    			   if(!"1101".equals(prpM.getRiskCode())){
    				   insuredName=prpM.getInsuredName();
    				   break;
    				 }else{
    					 insuredName=prpM.getInsuredName(); 
    				 }
    		   }
    	   }
    	   //获取驾驶员，报案时间
    	   PrpLRegistVo prpLRegistVo =registService.findRegistByRegistNo(registNo);
    	   if(prpLRegistVo!=null){
    		   driverName=prpLRegistVo.getDriverName();
    		   registTime=prpLRegistVo.getReportTime();
    		  }
    	   //获取标的车的车架号
    	   List<PrpLCheckCarVo>  prpLCheckCarVos=checkTaskService.findCheckCarVo(registNo);
    	   if(prpLCheckCarVos!=null && prpLCheckCarVos.size()>0){
    		   for(PrpLCheckCarVo prpLCheckCarVo:prpLCheckCarVos){
    			   if(1==prpLCheckCarVo.getSerialNo()){
    				   PrpLCheckCarInfoVo carInfoVo=checkTaskService.findPrpLCheckCarInfoVoById(prpLCheckCarVo.getCarid());
    				   if(carInfoVo!=null){
    					   frameNo=carInfoVo.getFrameNo();
    				   }
    			   }
    		   }
    	   }
    	   
       }
       //阳杰查看展示标志0-不展示，1展示
 		String yjAskFlag=CodeConstants.YN01.N;
 		if(deflossVo.getLossCarMainVo()!=null && deflossVo.getLossCarMainVo().getPrpLDlossCarComps()!=null && deflossVo.getLossCarMainVo().getPrpLDlossCarComps().size()>0){
 			for(PrpLDlossCarCompVo compVo:deflossVo.getLossCarMainVo().getPrpLDlossCarComps()){
				if("1".equals(compVo.getyJAskPrivceFlag())){
					yjAskFlag=CodeConstants.YN01.Y;
					break;
				}
			}
 		}
     	mv.addObject("yjAskFlag",yjAskFlag);
       
     //理算计算书打印和赔款收据打印
      String compensateSign="0";//页面按钮是否置灰的标志
       List<PrpLCompensateVo> compensates=compensateTaskService.findCompensatevosByRegistNo(registNo);
    	   if(compensates!=null && compensates.size()>0){
    		   for(PrpLCompensateVo vo:compensates){
    	        if("N".equals(vo.getCompensateType())){
    	        	compensateSign="1";
    	        	break;
    	        }
    			      
    	         }
    	      
    	   }
    	     //页面公估费查看按钮是否亮显
    		String assessSign="0";
    		if(deflossVo.getLossCarMainVo()!=null){
    			List<PrpLAssessorFeeVo> listFeeVo=assessorService.findPrpLAssessorFeeVoByRegistNo(deflossVo.getLossCarMainVo().getRegistNo());
    			if(listFeeVo!=null && listFeeVo.size()>0){
    				for(PrpLAssessorFeeVo vo :listFeeVo){
    					PrpLAssessorMainVo assessMainVo=assessorService.findAssessorMainVoById(vo.getAssessMainId());
    					if(assessMainVo!=null && "3".equals(assessMainVo.getUnderWriteFlag())){
    						assessSign="1";
    						break;
    					}
    				}
    			}
    		}
    		
    	//结过案不能选择快赔
    	String existEndCase = "N";
    	List<PrpLEndCaseVo> endCaseVoList = endCasePubService.queryAllByRegistNo(deflossVo.getLossCarMainVo().getRegistNo());
    	if(endCaseVoList!=null && endCaseVoList.size()>0){
    		existEndCase = "Y";
    	}
    	
    	mv.addObject("assessSign",assessSign);
        mv.addObject("compensateSign",compensateSign);
        mv.addObject("insuredName",insuredName);
        mv.addObject("driverName",driverName);
        mv.addObject("frameNo",frameNo);
        mv.addObject("registTime",registTime);
		mv.addObject("configValue", configValue);  //公估费开关
		mv.addObject("lossCarMainVo",deflossVo.getLossCarMainVo());
		mv.addObject("claimTextVos",deflossVo.getClaimTextVos());
		mv.addObject("claimTextVo",deflossVo.getClaimTextVo());
		mv.addObject("carInfoVo",deflossVo.getCarInfoVo());
		mv.addObject("lossChargeVos",deflossVo.getLossChargeVos());
		mv.addObject("commonVo",deflossVo.getCommonVo());
		mv.addObject("checkChargeVo",deflossVo.getCheckChargeVo());
		mv.addObject("subrogationMain", deflossVo.getSubrogationMainVo());
		mv.addObject("taskVo",deflossVo.getTaskVo());
		mv.addObject("registVo",deflossVo.getRegistVo());
		mv.addObject("existEndCase",existEndCase);
	    //山东预警信信息按钮是否显示，山东保单显示，其它不显示
        String policeInfoFlag="0";//显示标志
        String policyComCode = policyViewService.getPolicyComCode(registNo);
        if(policyComCode.startsWith("62")){
            policeInfoFlag = "1";
            //山东影像对比
            String claimSequenceNo = "";
            List<PrpLCMainVo> prpLCMains = prpLCMainService.findPrpLCMainsByRegistNo(registNo);
            if(prpLCMains != null && prpLCMains.size() > 1){//有商业取商业
                for(PrpLCMainVo vo : prpLCMains){
                    if("12".equals(vo.getRiskCode().substring(0, 2))){
                        if(StringUtils.isNotBlank(vo.getClaimSequenceNo())){
                            claimSequenceNo = vo.getClaimSequenceNo();
                            mv.addObject("claimSequenceNo",claimSequenceNo);
                        }
                    }
                }  
            }else if(prpLCMains != null && prpLCMains.size() == 1){
                PrpLCMainVo vo = prpLCMains.get(0);
                if(StringUtils.isNotBlank(vo.getClaimSequenceNo())){
                    claimSequenceNo = vo.getClaimSequenceNo();
                    mv.addObject("claimSequenceNo",claimSequenceNo);
                }
            }
            String carRiskUrl = SpringProperties.getProperty("CARRISK_URL");
            String carRiskUserName = SpringProperties.getProperty("SDWARN_YMUSER");
            String carRiskPassWord = SpringProperties.getProperty("SDWARN_YMPW");
            mv.addObject("comparePicURL",carRiskUrl);
            String claimPeriod = "04";
            mv.addObject("claimPeriod",claimPeriod);
            mv.addObject("carRiskUserName",carRiskUserName);
            mv.addObject("carRiskPassWord",carRiskPassWord);
        }
        mv.addObject("policeInfoFlag",policeInfoFlag);
        //判断是否有ILOG规则信息查看权限
        String nodeCode=deflossVo.getTaskVo().getSubNodeCode();
        String roleFlag="0";
        SysUserVo userVo = WebUserUtils.getUser();
		String grades="";
		SaaUserPowerVo saaUserPowerVo=saaUserPowerService.findUserPower(userVo.getUserCode());		
		List<FlowNode> flowNodeList=FlowNode.valueOf(nodeCode).getNextLevelNodes();
		for(FlowNode flowNode:flowNodeList){
			grades=grades+flowNode.getRoleCode()+",";
		}
		
		if(saaUserPowerVo!=null&&saaUserPowerVo.getRoleList().size()>0){			
			for(String gradeId:saaUserPowerVo.getRoleList()){
				if(grades.indexOf(gradeId)>-1){
					roleFlag="1";
					break;
				}
			}
		}
	   	if(deflossVo.getLossCarMainVo().getDeflossCarType().equals("1")){
     		PrpLCItemCarVo cItemCarVo = registQueryService.findCItemCarByRegistNo(deflossVo.getLossCarMainVo().getRegistNo());
     		mv.addObject("prpLCItemCarVo",cItemCarVo);
     	}
		mv.addObject("roleFlag",roleFlag);
		String ruleNodeCode="VLCar";
		ruleNodeCode=FlowNode.valueOf(taskVo.getSubNodeCode()).getUpperNode();
        mv.addObject("ruleNodeCode",ruleNodeCode);
		mv.setViewName("verifyLoss/VerifyLossEdit");
		mv.addObject("lossPropMainVos",deflossVo.getLossPropMainVos());
		String jy2Flag = "0";
		PrpLRegistVo prpLRegistVo =registService.findRegistByRegistNo(registNo);
		if(deflossHandleService.isJyTwo(prpLRegistVo.getReportTime())){
			jy2Flag = "1";
		}
		mv.addObject("jy2Flag",jy2Flag);     
		String selfClaimFlag="0";//0表示非自助理赔案件，1表示自助理赔案件
        String selfDlossAmout="0";
        if(prpLRegistVo!=null && "1".equals(prpLRegistVo.getSelfClaimFlag())){
        	selfClaimFlag="1";
        	PrpLAutocasestateInfoVo prpLAutocasestateInfoVo= selfHelpClaimCarService.findPrpLAutocasestateInfoByRegistNoAndLicenseNo(deflossVo.getLossCarMainVo().getRegistNo(), deflossVo.getLossCarMainVo().getLicenseNo());
        	if(prpLAutocasestateInfoVo!=null && prpLAutocasestateInfoVo.getFeepayMoney()!=null){
        		selfDlossAmout=prpLAutocasestateInfoVo.getFeepayMoney().toString();
        	}
        }
        mv.addObject("selfClaimFlag",selfClaimFlag);
		mv.addObject("selfDlossAmout",selfDlossAmout);
		return mv;
	}
	
	@RequestMapping(value = "/addVerifyLoss.do") 
	@ResponseBody
	public AjaxResult addVerifyLoss(@FormModel("lossCarMainVo") PrpLDlossCarMainVo lossCarMainVo,
	 	                            @FormModel("claimTextVo") PrpLClaimTextVo claimTextVo,
	 	                            @FormModel("lossChargeVos") List<PrpLDlossChargeVo> lossChargeVos,
	 	                            @FormModel("prpLDlossCarRepairs")List<PrpLDlossCarRepairVo> outRepairList,
	 	                            @FormModel("taskVo") PrpLWfTaskVo taskVo) {
		AjaxResult ajaxResult = new AjaxResult();
		try{
			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(taskVo.getTaskId().doubleValue());
			if(CodeConstants.HandlerStatus.END.equals(wfTaskVo.getHandlerStatus())){
				throw new IllegalArgumentException("该任务已处理完成，请刷新页面！");
			}
			
			SysUserVo userVo = WebUserUtils.getUser();
			lossCarMainVo.setOutRepairList(outRepairList);
			deflossHandleService.saveVerifyLoss(lossCarMainVo,lossChargeVos,claimTextVo,taskVo,userVo);
			
			String returnStr = lossCarMainVo.getId()+","+taskVo.getTaskId();
			
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(returnStr);
		}catch(Exception e){
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		
		return ajaxResult;
	}
	
	@RequestMapping(value = "/amendDefloss.do")
	public String amendDefloss(String policyNo,Model model) {

		model.addAttribute("reportDate",new Date());
		return "lossCar/AmendDefloss";
	}

	
	@RequestMapping(value = "/modifyDefloss.do")
	public String modifyDefloss(String policyNo,Model model) {

		model.addAttribute("reportDate",new Date());
		return "lossCar/ModifyDefloss";
	}
	
	//暂存
	@RequestMapping(value = "/saveDefloss.do") 
	@ResponseBody
	public AjaxResult saveDefloss(@FormModel("lossCarMainVo") PrpLDlossCarMainVo lossCarMainVo,
	                              @FormModel("claimTextVo") PrpLClaimTextVo claimTextVo,
	                              @FormModel("lossChargeVos") List<PrpLDlossChargeVo> lossChargeVos,
	                              @FormModel("carInfoVo") PrpLDlossCarInfoVo lossCarInfoVo,
	                              @FormModel("subrogationMain")PrpLSubrogationMainVo subrogationMainVo,
	                              @FormModel("prpLDlossCarRepairs")List<PrpLDlossCarRepairVo> outRepairList,
	                              @FormModel("taskVo") PrpLWfTaskVo taskVo
			)throws ParseException{
		
		AjaxResult ajaxResult = new AjaxResult();
		try{
			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(taskVo.getTaskId().doubleValue());
			if(CodeConstants.HandlerStatus.END.equals(wfTaskVo.getHandlerStatus())){
				throw new IllegalArgumentException("该任务已处理完成，请刷新页面！");
			}
			
			SysUserVo userVo = WebUserUtils.getUser();
			lossCarMainVo.setLossCarInfoVo(lossCarInfoVo);
			lossCarMainVo.setOutRepairList(outRepairList);
			
			deflossHandleService.save(lossCarMainVo, lossChargeVos, claimTextVo, subrogationMainVo, wfTaskVo,userVo);
			//移动端案件理赔处理要通知快赔 并写会理赔处理标识
            //写会标志
             PrpLWfTaskVo prpLWfTaskVo = sendMsgToMobileService.isMobileCaseAcceptInClaim(lossCarMainVo.getRegistNo(),taskVo.getTaskId());
             if(prpLWfTaskVo != null){ //发送通知
                 prpLWfTaskVo.setHandlerStatus("2"); //未处理
                 prpLWfTaskVo.setWorkStatus("2");  // 接收案件
                 prpLWfTaskVo.setMobileNo(lossCarMainVo.getSerialNo().toString());
                 prpLWfTaskVo.setMobileName(lossCarMainVo.getLicenseNo());
                 prpLWfTaskVo.setMobileOperateType(CodeConstants.MobileOperationType.CARLOSSSAVE);
                 String url = SpringProperties.getProperty("MClaimPlatform_URL_IN");
                 interfaceAsyncService.packMsg(prpLWfTaskVo,url);
             }
			String returnStr =lossCarMainVo.getId()+","+taskVo.getTaskId();
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(returnStr);
			
		}catch(Exception e){
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		return ajaxResult;
	}
	
	/**
	 * 
	 * 增加附加险费用按钮选择险别
	 * @return
	 * @modified:
	 * ☆yangkun(2015年12月9日 下午7:27:42): <br>
	 */
	@RequestMapping(value="/initSubRisk.ajax")
	public ModelAndView initSubRisk(String registNo,String[] kindCodes) { 
		ModelAndView mv = new ModelAndView();
		List<PrpLCItemKindVo> itemKinds = deflossHandleService.initSubRisks(registNo,kindCodes);
		List<PrpLCItemKindVo> itemKindList=new ArrayList<PrpLCItemKindVo>();
		 if(itemKinds!=null && itemKinds.size()>0){//不展示法定节假日限额翻倍险 B2
	      	  for(PrpLCItemKindVo vo:itemKinds){
	      		  //不允许展示增值和医保外用和新险种的附加免赔系列
	      		  if(!"B2".equals(vo.getKindCode()) && !CodeConstants.KINDCODE.KINDCODE_BG.equals(vo.getKindCode())
	    				   && !CodeConstants.KINDCODE.KINDCODE_D12G.equals(vo.getKindCode()) && !CodeConstants.KINDCODE.KINDCODE_AG.equals(vo.getKindCode())
	    				   && !CodeConstants.KINDCODE.KINDCODE_D11G.equals(vo.getKindCode()) && !CodeConstants.KINDCODE.KINDCODE_GG.equals(vo.getKindCode())
	    				   && !CodeConstants.KINDCODE.KINDCODE_BP.equals(vo.getKindCode()) && !CodeConstants.KINDCODE.KINDCODE_D11P .equals(vo.getKindCode())
	    				   && !CodeConstants.KINDCODE.KINDCODE_D12P.equals(vo.getKindCode()) && !CodeConstants.KINDCODE.KINDCODE_DP.equals(vo.getKindCode())
	    				   && !CodeConstants.KINDCODE.KINDCODE_RS.equals(vo.getKindCode()) && !CodeConstants.KINDCODE.KINDCODE_VS.equals(vo.getKindCode())
	    				   && !CodeConstants.KINDCODE.KINDCODE_DS.equals(vo.getKindCode()) && !CodeConstants.KINDCODE.KINDCODE_DC.equals(vo.getKindCode())){
	      			itemKindList.add(vo);
	      		  }
	      	  }
	 
	      }
		mv.addObject("itemKinds",itemKindList);
		mv.setViewName("lossCar/SubRiskDialog");
		return mv; 
	}
	/**
	 * 增加一条数据
	 * <pre></pre>
	 * @param size
	 * @param kindCodes
	 * @return
	 * @modified:
	 * ☆yangkun(2015年12月9日 下午7:28:14): <br>
	 */
	@RequestMapping(value="/loadSubRiskTr.ajax")
	public ModelAndView loadSubRiskTr(int size, String registNo,String deviceStr,String[] kindCodes) { 
		ModelAndView mv = new ModelAndView();
		List<PrpLDlossCarSubRiskVo> subRiskVoList = deflossHandleService.loadSubRisk(kindCodes,registNo);
		PrpLDlossCarMainVo lossCarMainVo =new PrpLDlossCarMainVo();
		lossCarMainVo.setPrpLDlossCarSubRisks(subRiskVoList);
		
		for(String str : kindCodes){
			if(CodeConstants.KINDCODE.KINDCODE_X.equals(str)){
				if(StringUtils.isNotBlank(deviceStr)){
					Map<String,String> deviceMap = handleKindMap(deviceStr);
					mv.addObject("deviceMap", deviceMap);
				}
				break;
			}
		}
		
		mv.addObject("lossCarMainVo",lossCarMainVo);
		mv.addObject("size",size);
		mv.setViewName("lossCar/DeflossEdit_SubRisk_Tr");
		return mv; 
	}
	
	/**
	 * 外修的初始化
	 * 
	 */
	@RequestMapping(value="/loadOutRepairTr.ajax")
	public ModelAndView loadOutRepairTr(int size,String veriFeeFlag) { 
		ModelAndView mv = new ModelAndView();
		List<PrpLDlossCarRepairVo> outRepairList =  new ArrayList<PrpLDlossCarRepairVo>();
		PrpLDlossCarRepairVo outRepair = new PrpLDlossCarRepairVo();
		outRepair.setRepairFlag("1");
		
		outRepairList.add(outRepair);
		PrpLDlossCarMainVo lossCarMainVo =new PrpLDlossCarMainVo();
		lossCarMainVo.setOutRepairList(outRepairList);
		
		mv.addObject("lossCarMainVo",lossCarMainVo);
		mv.addObject("size",size);
		mv.addObject("veriFeeFlag",veriFeeFlag);
		mv.setViewName("lossCar/DeflossEdit_OutRepari_Tr");
		return mv; 
	}
	
	
	/**
	 *费用赔款信息初始化选择费用类型
	 * ☆yangkun(2016年1月20日 下午3:21:35): <br>
	 */
	@RequestMapping(value="/initChargeType.ajax")
	public ModelAndView initChargeType(String chargeCodes,String intermFlag) { 
		List<String> chargeCodeList = new ArrayList<String>();
		if(chargeCodes!=null && !"".equals(chargeCodes)){
			String[] chargeArray = chargeCodes.split(",");
			//chargeCodeList = Arrays.asList(chargeArray);
			chargeCodeList = new ArrayList<String>(Arrays.asList(chargeArray));  
		}
		//都不能选择公估费
		chargeCodeList.add("13");
		
		ModelAndView mv = new ModelAndView();
		List<SysCodeDictVo> sysCodes = codeDictService.findCodeListByQuery("ChargeCode",chargeCodeList);
		
		mv.addObject("sysCodes",sysCodes);
		mv.setViewName("loss-common/ChargeDialog");
		return mv; 
	}
	
	@RequestMapping(value="/loadChargeTr.ajax")
	public ModelAndView loadChargeTr(int size,String[] chargeTypes,String registNo,String intermCode,double feeStandard,String veriChargeFlag) { 
		ModelAndView mv = new ModelAndView();
		
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		String kindCode ="";
		Map<String,String> kindMap = new HashMap<String,String>(); 
		List<PrpLCItemKindVo> itemKinds = policyViewService.findItemKinds(registNo,null);
		for(PrpLCItemKindVo itemKind :itemKinds){
			if(!(itemKind.getKindCode().endsWith("M") || CodeConstants.NOSUBRISK_MAP.containsKey(itemKind.getKindCode()))){
				kindMap.put(itemKind.getKindCode(),itemKind.getKindName());
			}
		}
		//玻璃破碎、车身划痕、盗抢、自燃损失时，则对应默认为：玻璃破碎险、车身划痕险、盗抢险、自燃损失险（前提保单承保里有此险别）
		if("DM02".equals(registVo.getDamageCode()) && kindMap.containsKey("F")){//玻璃单独破碎
			kindCode ="F";
		}else if("DM03".equals(registVo.getDamageCode()) && kindMap.containsKey("L")){//车身划痕
			kindCode ="L";
		}else if("DM04".equals(registVo.getDamageCode()) && kindMap.containsKey("G")){//盗抢
			kindCode ="G";
		}else if("DM05".equals(registVo.getDamageCode()) && kindMap.containsKey("Z")){//自燃损失
			kindCode ="Z";
		}else if(kindMap.containsKey("A")){
			kindCode ="A";
		}else{
			kindCode ="";
		}
		
		List<PrpLDlossChargeVo> lossChargeVos = new ArrayList<PrpLDlossChargeVo>();
		if(chargeTypes!=null){
			for(String chargeCode : chargeTypes){
				PrpLDlossChargeVo lossChargeVo = new PrpLDlossChargeVo();
				lossChargeVo.setChargeCode(chargeCode);
				lossChargeVo.setRegistNo(registNo);
				lossChargeVo.setKindCode(kindCode);
				lossChargeVo.setBusinessType(FlowNode.DLCar.name());
				lossChargeVo.setChargeName(codeTranService.transCode("ChargeCode",chargeCode));
				
				lossChargeVos.add(lossChargeVo);
			}
		}
		
		mv.addObject("kindMap",kindMap);
		mv.addObject("lossChargeVos",lossChargeVos);
		mv.addObject("size",size);
		mv.addObject("veriChargeFlag",veriChargeFlag);
		mv.setViewName("loss-common/DeflossEdit_Charge_Tr");
		return mv; 
	}
	
	/**
	 * 进入精友组织claimFittingVo
	 * ☆yangkun(2016年2月19日 下午4:18:17): <br>
	 */
	@RequestMapping(value="/enterFittingSys.do")
	public ModelAndView enterFittingSys(String carMainId,String operateType,String repariFactoryType,String nodeCode) {
		ModelAndView mv = new ModelAndView();
		ClaimFittingVo claimFittingVo = new ClaimFittingVo();
		PrpLDlossCarMainVo carMainVo = lossCarService.findLossCarMainById(Long.valueOf(carMainId));
		
		claimFittingVo.setLossCarId(carMainId);
		claimFittingVo.setOperateType(operateType);
		claimFittingVo.setNodeCode(nodeCode);
		claimFittingVo.setSystemAreaCode("深圳分公司");//TODO 用户的机构
		claimFittingVo.setLocalAreaCode(WebUserUtils.getComCode());
		claimFittingVo.setRegistNo(carMainVo.getRegistNo());
		//claimFittingVo.setLocalAreaCode(lossCarMainVo.getComCode());// 本地价地区（填写当前用户所在的分公司代码）
//		if(!"certa".equals(operateType)){
//			claimFittingVo.setLocalAreaCode("00020000");// 本地价地区（填写当前用户所在的分公司代码）
//			claimFittingVo.setLocalAreaName("深圳分公司");// 本地价地区（填写当前用户所在的分公司代码）
//		}
		claimFittingVo.setOperatorCode(WebUserUtils.getUserCode());
		
		mv.addObject("fittingVo",claimFittingVo);
		mv.setViewName("lossCar/OpenFittingsSystemBefore");
		return mv;
	}
	
	/**
	 * 1 校验责任比例 本案件的所有车辆的责任比例之和不能超过100%
	 * 2 校验附加险（停驶险和代步险）的天数和定损金额
	 * ☆yangkun(2016年1月4日 下午2:44:25): <br>
	 */
	@RequestMapping(value="/validDefloss.do")
	@ResponseBody
	public AjaxResult validDefloss(@FormModel("lossCarMainVo") PrpLDlossCarMainVo lossCarMainVo,
	                               @FormModel("carInfoVo") PrpLDlossCarInfoVo carInfoVo,
	                               @FormModel("subrogationMain") PrpLSubrogationMainVo subrogationMain,
	                               @FormModel("taskVo") PrpLWfTaskVo taskVo){
		String retData = deflossHandleService.validDefloss(lossCarMainVo,carInfoVo, subrogationMain,taskVo);
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(HttpStatus.SC_OK);
		ajaxResult.setData(retData);
		
		return ajaxResult;
	} 
	/*
	 * 修理厂查询界面
	 */
	@RequestMapping(value="/findViewList.do")
	@ResponseBody
	public ModelAndView	findFactory(String comCode,String checkAddressCode){
		ModelAndView mv = new ModelAndView();
		mv.addObject("comCode",comCode);
		mv.addObject("checkAddressCode",checkAddressCode);
		mv.setViewName("lossCar/CheckFactoryList");
		return mv;
	} 
	/*
	 * 修理厂查询
	 * 
	 */
	
	@RequestMapping(value="/checkFactoryList.do")
	@ResponseBody
	public String search(
			@FormModel(value = "prplRepairFactoryVo") PrpLRepairFactoryVo repairfactoryVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length,
			String index
			) {
		//index地区下拉框第三级是否被选择地区的标志位
		Page<PrpLRepairFactoryVo> page = managerService.findRepariFactory(repairfactoryVo, start, length,index);
		//去除修理厂字符串中的字符串
		List<PrpLRepairFactoryVo> list=page.getResult();
		if(list!=null && list.size()>0){
		for(PrpLRepairFactoryVo prplfactoryvo:list){
			if(StringUtils.isNotEmpty(prplfactoryvo.getFactoryName())){
			  prplfactoryvo.setFactoryName(prplfactoryvo.getFactoryName().replaceAll(" ", ""));
			}
		}
		}
	    System.out.println(repairfactoryVo.getAreaCode());
		String jsonData = ResponseUtils.toDataTableJson(page, "id", "factoryCode", "factoryName", "address","factoryType","mobile");
		logger.debug(jsonData);
		return jsonData;
	}
	
	
	/**
	 * 定损轨迹查看 
	 * 
	 * ☆yangkun(2016年1月28日 上午10:33:02): <br>
	 */
	@RequestMapping(value="/deflossHisView.do")
	public ModelAndView deflossHisView(Long defLossMainId){
		@SuppressWarnings("rawtypes")
		Map deflossHisMap = deflossHandleService.showDeflossHis(defLossMainId);
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("deflossHisMap",deflossHisMap);
		mv.setViewName("lossCar/DeflossHisView");
		
		return mv;
	}
	//导出功能
	@RequestMapping(value = "/exportComp.do", method = RequestMethod.GET)
	@ResponseBody
	public void writeExcel(long id, HttpServletResponse response)
			throws IOException {
		WritableWorkbook workbook; 
		OutputStream os = response.getOutputStream();
		response.reset();
		response.setHeader("Content-Disposition", "attachment;filename="+new String("零配件导出".getBytes("gb2312"), "ISO8859-1")+".xls" ); 
		response.setContentType("application/vnd.ms-excel"); 
		workbook = Workbook.createWorkbook(os); 
		try {
			WritableSheet sheet = workbook.createSheet("零配件", 0);
			PrpLDlossCarMainVo lossMainVo = lossCarService.findLossCarMainById(id);
			List<PrpLDlossCarCompVo> compList = lossMainVo
					.getPrpLDlossCarComps();
			WritableFont wf_titles = new WritableFont(WritableFont.ARIAL, 11,  
					                    WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,  
					                 jxl.format.Colour.BLACK); // 定义格式 字体 下划线 斜体 粗体 颜色  
			WritableCellFormat wcftitle = new WritableCellFormat(wf_titles); 
			wcftitle.setAlignment(jxl.format.Alignment.CENTRE); // 设置对齐方式  
			//查询
			long carId = lossMainVo.getCarId();
			PrpLDlossCarInfoVo dlossCarInfoVo =lossCarService.findPrpLDlossCarInfoVoById(carId);
			dlossCarInfoVo.getModelName();//车型
			dlossCarInfoVo.getFrameNo();//车架号
			dlossCarInfoVo.getEngineNo();//发动机号
			
			sheet.mergeCells(0, 0, 3, 0);
			Label title=new Label(0,0,"车型"+dlossCarInfoVo.getModelName()+"   车架号"+dlossCarInfoVo.getFrameNo()+"   发动机号"+dlossCarInfoVo.getEngineNo(),wcftitle); 
			sheet.addCell(title); 
			sheet.addCell(new Label(0, 1, "序号"));
			sheet.addCell(new Label(1, 1, "配件编码"));
			sheet.addCell(new Label(2, 1, "配件名称"));
			sheet.addCell(new Label(3, 1, "定损价格"));
			for (int i = 0, j = 2,q=1; compList.size() > i; i++, j++) {
				sheet.addCell(new Label(0, j, "" + q));
				sheet.addCell(new Label(1, j, compList.get(i).getOriginalId()));
				sheet.addCell(new Label(2, j, compList.get(i).getCompName()));
				sheet.addCell(new Label(3, j, compList.get(i).getMaterialFee().toString()));
			}
			// 写入数据并关闭文件
			workbook.write();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	@RequestMapping(value="/validThirdCar.do")
	@ResponseBody
	public AjaxResult validThirdCar(String registNo){
		
		String retData ="ok";
		AjaxResult ajaxResult = new AjaxResult();
		
		List<PrpLCheckDutyVo> checkDutyList = checkTaskService.findCheckDutyByRegistNo(registNo);
		if(checkDutyList.size()==1){
			retData ="没有三者车的查勘或者定损任务，不能代位求偿";
		}
		ajaxResult.setStatus(HttpStatus.SC_OK);
		ajaxResult.setData(retData);
		
		return ajaxResult;
	}
	
	@RequestMapping(value="/loadSubrationCar.ajax")
	public ModelAndView loadSubrationCar(int size,String registNo){
		ModelAndView mv = new ModelAndView();
		PrpLSubrogationMainVo subrogationMain = new PrpLSubrogationMainVo();
		PrpLSubrogationCarVo carVo = new PrpLSubrogationCarVo();
		carVo.setRegistNo(registNo);
		
		List<PrpLSubrogationCarVo> carList = new ArrayList<PrpLSubrogationCarVo>();
		carList.add(carVo);
		
		List<PrpLCheckDutyVo> checkDutyList = checkTaskService.findCheckDutyByRegistNo(registNo);
		Map<String,String> thirdCarMap = new HashMap<String,String>();
		
		for(PrpLCheckDutyVo checkDuty : checkDutyList){
			if(checkDuty.getSerialNo()!=1){
				thirdCarMap.put(checkDuty.getSerialNo().toString(),checkDuty.getLicenseNo());
			}
		}
		DefCommonVo commonVo = new DefCommonVo();
		commonVo.setThirdCarMap(thirdCarMap);
		
		if(thirdCarMap.isEmpty()){
			thirdCarMap.put("","");
		}
		
		subrogationMain.setPrpLSubrogationCars(carList);
		mv.addObject("subrogationMain",subrogationMain);
		mv.addObject("size",size);
		mv.addObject("commonVo",commonVo);
		
		mv.setViewName("lossCar/DeflossEdit_Subrogation_CarTr");
		
		return mv; 
	}
	
	@RequestMapping(value="/loadSubrationPers.ajax")
	public ModelAndView loadSubrationPers(int size,String registNo){
		ModelAndView mv = new ModelAndView();
		PrpLSubrogationMainVo subrogationMain = new PrpLSubrogationMainVo();
		PrpLSubrogationPersonVo personVo = new PrpLSubrogationPersonVo();
		personVo.setRegistNo(registNo);
		List<PrpLSubrogationPersonVo> personList = new ArrayList<PrpLSubrogationPersonVo>();
		personList.add(personVo);
		
		subrogationMain.setPrpLSubrogationPersons(personList);
		mv.addObject("subrogationMain",subrogationMain);
		mv.addObject("size",size);
		mv.setViewName("lossCar/DeflossEdit_Subrogation_PerTr");
		
		return mv; 
	}
	
	/**
	 * 通过定损id 查看定损信息
	 * @param lossId
	 * @return
	 */
	@RequestMapping(value = "/deflossView.do")
	public ModelAndView deflossView(String lossId) {
//		this.setSecurity(deflossVo);
		DeflossActionVo deflossVo = deflossHandleService.deflossView(lossId);
		PrpLRegistVo registNo = registService.findRegistByRegistNo(deflossVo.getRegistNo());
		PrpLWfTaskVo taskVo = new PrpLWfTaskVo();
		taskVo.setHandlerStatus("3");
		String sindex="1";//是否置灰页面的标志
		ModelAndView mv = new ModelAndView();
		mv.addObject("kindMap",deflossVo.getKindMap());
		mv.addObject("intermMap",deflossVo.getIntermMap());
		mv.addObject("lossCarMainVo",deflossVo.getLossCarMainVo());
		mv.addObject("claimTextVos",deflossVo.getClaimTextVos());
		mv.addObject("claimTextVo",deflossVo.getClaimTextVo());
		mv.addObject("lossChargeVos",deflossVo.getLossChargeVos());
		mv.addObject("carInfoVo",deflossVo.getCarInfoVo());
		mv.addObject("commonVo",deflossVo.getCommonVo());
		mv.addObject("registVo",deflossVo.getRegistVo());
		mv.addObject("subrogationMain", deflossVo.getSubrogationMainVo());
		mv.addObject("checkChargeVo",deflossVo.getCheckChargeVo());
		mv.addObject("sindex",sindex);
		mv.addObject("taskVo",taskVo);
		mv.addObject("riskCode",registNo.getRiskCode());
	    //山东影像对比
        String claimSequenceNo = "";
        List<PrpLCMainVo> prpLCMains = prpLCMainService.findPrpLCMainsByRegistNo(deflossVo.getRegistNo());
        if(prpLCMains != null && prpLCMains.size() > 1){//有商业取商业
            for(PrpLCMainVo vo : prpLCMains){
                if("12".equals(vo.getRiskCode().substring(0, 2))){
                    if(StringUtils.isNotBlank(vo.getClaimSequenceNo())){
                        claimSequenceNo = vo.getClaimSequenceNo();
                        mv.addObject("claimSequenceNo",claimSequenceNo);
                    }
                }
            }  
        }else if(prpLCMains != null && prpLCMains.size() == 1){
            PrpLCMainVo vo = prpLCMains.get(0);
            if(StringUtils.isNotBlank(vo.getClaimSequenceNo())){
                claimSequenceNo = vo.getClaimSequenceNo();
                mv.addObject("claimSequenceNo",claimSequenceNo);
            }
        }
        String carRiskUrl = SpringProperties.getProperty("CARRISK_URL");
        String carRiskUserName = SpringProperties.getProperty("SDWARN_YMUSER");
        String carRiskPassWord = SpringProperties.getProperty("SDWARN_YMPW");
        mv.addObject("comparePicURL",carRiskUrl);
        String claimPeriod = "04";
        mv.addObject("claimPeriod",claimPeriod);
        mv.addObject("carRiskUserName",carRiskUserName);
        mv.addObject("carRiskPassWord",carRiskPassWord);
		String jy2Flag = "0";
		if(deflossVo.getLossCarMainVo() != null && StringUtils.isNotBlank(deflossVo.getLossCarMainVo().getRegistNo())){
			PrpLRegistVo prpLRegistVo =registService.findRegistByRegistNo(deflossVo.getLossCarMainVo().getRegistNo());
			if(deflossHandleService.isJyTwo(prpLRegistVo.getReportTime())){
				jy2Flag = "1";
			}
			mv.addObject("jy2Flag",jy2Flag);
		}
		mv.setViewName("lossCar/DeflossEdit");
        return mv;
        
        
        }
	
	/**
	 * 通过定损id 查看核损信息
	 * @param lossId
	 * @return
	 */
	@RequestMapping(value = "/VerifyLossView.do")
	public ModelAndView VerifyLossView(String lossId) {
		ModelAndView mv = new ModelAndView();
		
		PrpLDlossCarMainVo prpLDlossCarMainVo = lossCarService.findLossCarMainById(Long.valueOf(lossId));
		String registNo = "";
		if(prpLDlossCarMainVo != null){
			registNo = prpLDlossCarMainVo.getRegistNo();
		}
	//	deflossVo.setUserVo(SecurityUtils.getUser());
		//查找最后一个工作流数据
		PrpLWfTaskVo taskVo = wfTaskHandleService.findEndTask(registNo, lossId,FlowNode.VLCar).get(0);
		DeflossActionVo deflossVo = deflossHandleService.prepareAddVerifyLoss(taskVo,WebUserUtils.getUser());
		
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.ASSESSORFEE,WebUserUtils.getComCode());
		String configValue = "0";
		if(configValueVo!=null){
			configValue = configValueVo.getConfigValue();
		}
	   	if(deflossVo.getLossCarMainVo().getDeflossCarType().equals("1")){
     		PrpLCItemCarVo cItemCarVo = registQueryService.findCItemCarByRegistNo(deflossVo.getLossCarMainVo().getRegistNo());
     		mv.addObject("prpLCItemCarVo",cItemCarVo);
     	}
		mv.addObject("configValue", configValue);  //公估费开关
		
		mv.addObject("lossCarMainVo",deflossVo.getLossCarMainVo());
		mv.addObject("claimTextVos",deflossVo.getClaimTextVos());
		mv.addObject("claimTextVo",deflossVo.getClaimTextVo());
		mv.addObject("carInfoVo",deflossVo.getCarInfoVo());
		mv.addObject("lossChargeVos",deflossVo.getLossChargeVos());
		mv.addObject("commonVo",deflossVo.getCommonVo());
		mv.addObject("checkChargeVo",deflossVo.getCheckChargeVo());
		mv.addObject("subrogationMain", deflossVo.getSubrogationMainVo());
		mv.addObject("taskVo",deflossVo.getTaskVo());
		mv.setViewName("verifyLoss/VerifyLossEdit");
		String jy2Flag = "0";
		PrpLRegistVo prpLRegistVo =registService.findRegistByRegistNo(registNo);
		if(deflossHandleService.isJyTwo(prpLRegistVo.getReportTime())){
			jy2Flag = "1";
		}
		mv.addObject("jy2Flag",jy2Flag);
		return mv;
	}
	
	/**
	 * 异步刷新费用赔款 和附加险
	 * @param checkId
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆YangKun(2016年7月15日 下午7:37:37): <br>
	 */
	@RequestMapping("/refreshFee.ajax")
	@ResponseBody
	public ModelAndView refreshFee(Long lossMainId,String registNo,String operateType){
		ModelAndView mv = new ModelAndView();
		DeflossActionVo deflossVo = deflossHandleService.refreshFee(lossMainId,operateType);
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		Map<String,String> kindMap = new HashMap<String,String>(); 
		List<PrpLCItemKindVo> itemKinds = policyViewService.findItemKinds(registNo,null);
		for(PrpLCItemKindVo itemKind :itemKinds){
			if(!(itemKind.getKindCode().endsWith("M") || CodeConstants.NOSUBRISK_MAP.containsKey(itemKind.getKindCode()))){
				kindMap.put(itemKind.getKindCode(),itemKind.getKindName());
			}
		}
		
		PrpLDlossCarMainVo carMainVo = deflossVo.getLossCarMainVo();
		if(carMainVo.getSumVeriSubRiskFee()==null){
			carMainVo.setSumVeriSubRiskFee(BigDecimal.ZERO);
		}
		mv.addObject("refreshFlag","1");
		mv.addObject("kindMap",kindMap);
		mv.addObject("commonVo",deflossVo.getCommonVo());
		mv.addObject("lossCarMainVo",carMainVo);
		mv.addObject("lossChargeVos",deflossVo.getLossChargeVos());
		mv.addObject("subrogationMain", deflossVo.getSubrogationMainVo());
		mv.addObject("registVo", registVo);
		mv.addObject("riskCode", registVo.getRiskCode());

		if(!"certa".equals(operateType)){
			mv.addObject("offsetVeriRate",deflossVo.getCommonVo().getOffsetVeriRate());
			mv.addObject("offsetVeri",deflossVo.getCommonVo().getOffsetVeri());
		}
		
		if("certa".equals(operateType)){
			mv.addObject("deviceMap",deflossVo.getCommonVo().getDeviceMap());
			mv.setViewName("lossCar/DeflossEdit_AllLoss");
		}else if("verifyPrice".equals(operateType)){
			mv.setViewName("verifyPrice/VerifyPrice_jyLoss");
		}else{
			mv.setViewName("verifyLoss/VerifyLoss_allLoss");
		}
		
		return mv;
	}
	
	/**
	 * 异步定损信息
	 * @param lossMainId
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆LinYi(2016年7月15日 下午7:37:37): <br>
	 */
	@RequestMapping("/refreshLossInfo.ajax")
	@ResponseBody
	public ModelAndView refreshLossInfo(Long lossMainId,String registNo){
		ModelAndView mv = new ModelAndView();
		PrpLDlossCarMainVo lossMainVo = lossCarService.findLossCarMainById(lossMainId);
		List<PrpLCheckDutyVo> checkDutyList = checkTaskService.findCheckDutyByRegistNo(lossMainVo.getRegistNo());
		Map<String,String> thirdCarMap = new HashMap<String,String>();
		PrpLCheckDutyVo checkDutyVo = null ;
		if(checkDutyList!=null && !checkDutyList.isEmpty()){
			for(PrpLCheckDutyVo checkDuty : checkDutyList){
				if(checkDuty.getSerialNo()==lossMainVo.getSerialNo()){
					checkDutyVo = checkDuty;
				}
				
				if(checkDuty.getSerialNo()!=1){
					thirdCarMap.put(checkDuty.getSerialNo().toString(),checkDuty.getLicenseNo());
				}
				
			}
		}
		//事故责任比例
		if(checkDutyVo!=null){
			lossMainVo.setCiDutyFlag(checkDutyVo.getCiDutyFlag());
			lossMainVo.setIndemnityDuty(checkDutyVo.getIndemnityDuty());
			lossMainVo.setIndemnityDutyRate(checkDutyVo.getIndemnityDutyRate());
			lossMainVo.setIsClaimSelf(checkDutyVo.getIsClaimSelf());
		}else{
			lossMainVo.setCiDutyFlag("1");//默认有责
		}
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		DefCommonVo commonVo = new DefCommonVo();
		commonVo.setDamageDate(registVo.getDamageTime());		
		List<PrpLCItemKindVo> itemKindVoList = policyViewService.findItemKinds(registNo,null);
		//保单的车损险和盗抢险保额
		for(PrpLCItemKindVo itemKindVo : itemKindVoList){
			if(CodeConstants.LossParty.TARGET.equals(lossMainVo.getDeflossCarType())){
				if("A".equals(itemKindVo.getKindCode())||"A1".equals(itemKindVo.getKindCode())){//车损险保额
					commonVo.setCarAmount(itemKindVo.getAmount());
				}else if("G".equals(itemKindVo.getKindCode())){//盗抢险保额
					commonVo.setTheftAmount(itemKindVo.getAmount());
				}
			}
		}
		mv.addObject("commonVo",commonVo);
		mv.addObject("lossCarMainVo",lossMainVo);
		mv.addObject("carInfoVo",lossMainVo.getLossCarInfoVo());
		
		if(LossParty.TARGET.equals(lossMainVo.getDeflossCarType())){
			mv.setViewName("lossCar/DeflossEdit_Info_Main");
		}else {
			mv.setViewName("lossCar/DeflossEdit_Info_Third");
		}
		return mv;
	}
	   /**
		 * 车辆定损发起
		 * @param compensateNo
		 * @param flowTaskId
		 * @return
		 */
		@RequestMapping("/LossAddInit.do")
		@ResponseBody
		public ModelAndView LossAddInit(String registNo) {
			List<PrpLDlossCarMainVo> carVos = lossCarService.findLossCarMainByRegistNo(registNo);//车辆定损
			if(carVos!=null && carVos.size() > 0){
				for(PrpLDlossCarMainVo vo : carVos){
					if(vo.getSerialNo()>1){
						vo.setRemark("三者车");
					}else{
						String remark = codeTranService.transCode("DefLossItemType",vo.getSerialNo().toString());
						vo.setRemark(remark);
					}
				}
			}
		List<PrpLdlossPropMainVo> propVos = propTaskService.findPrpLdlossPropMainVoListByCondition(registNo, null, null, null);

			if(propVos!=null && propVos.size() > 0){
				for(PrpLdlossPropMainVo vo : propVos){
					if(vo.getSerialNo()>1){
						vo.setRemark("三者车");
					}else{
						String remark = codeTranService.transCode("DefLossItemType",vo.getSerialNo().toString());
						vo.setRemark(remark);
					}
				}
			}
			ModelAndView mv = new ModelAndView();	
			mv.addObject("carVos",carVos);
			mv.addObject("propVos",propVos);
			mv.setViewName("lossCar/LossAddInit");
			return mv;
		}
		
		//定损发起
		@RequestMapping(value = "/AddLossSubmit.do", method = RequestMethod.POST)
		@ResponseBody
		public AjaxResult AddLossSubmit(Long[] propVos,Long[] carVos,String remarks) {
			AjaxResult ajaxResult = new AjaxResult();
			System.out.println("意见"+remarks);
			String retStr="";
			try{
			//车辆定损发起
			for(int i=0;i<carVos.length;i++){
				SysUserVo userVo = WebUserUtils.getUser();
				String flags = lossCarService.carAdditionLaunch(carVos[i],userVo);
			
				//保存意见PrpLDlossCarMainVo findLossCarMainById
				PrpLDlossCarMainVo vo = lossCarService.findLossCarMainById(carVos[i]);
				if(!flags.equals("ok")){
					if(vo.getSerialNo()>1){
						retStr = retStr+"三者车";
					}else{
						String remark = codeTranService.transCode("DefLossItemType",vo.getSerialNo().toString());
						retStr = retStr+remark;
					}
					retStr = retStr + "(" +vo.getLicenseNo()+ ")" + flags;
				}
				PrpLClaimTextVo claimTextVo = new PrpLClaimTextVo();
				claimTextVo.setBussTaskId(carVos[i]);
				claimTextVo.setBigNode(FlowNode.DLCar.toString());
				claimTextVo.setNodeCode(FlowNode.DLCarMod.toString());
				claimTextVo.setBussNo(vo.getRegistNo());
				claimTextVo.setRegistNo(vo.getRegistNo());
				claimTextVo.setTextType("2");
				claimTextVo.setDescription(remarks);
				claimTextVo.setOperatorCode(WebUserUtils.getUserCode());
//				claimTextVo.setOperatorName(WebUserUtils.getComName());
				claimTextVo.setComCode(WebUserUtils.getComCode());
//				claimTextVo.setComName(WebUserUtils.getComName());
				claimTextService.saveOrUpdte(claimTextVo);
				}
			
			//财产定损修改发起
			for(int i=0;i<propVos.length;i++){
				SysUserVo sysUserVo = new SysUserVo();
				sysUserVo.setUserCode(WebUserUtils.getUserCode());
				sysUserVo.setUserName(WebUserUtils.getUserName());
				sysUserVo.setComCode(WebUserUtils.getComCode());
//				sysUserVo.setComName(WebUserUtils.getComName());
				//propTaskService.propModifyLaunch(propVos[i], sysUserVo);
				//保存意见
				PrpLdlossPropMainVo vo = propTaskService.findPropMainVoById(propVos[i]);
				SysUserVo userVo = WebUserUtils.getUser();
				String flags =  propTaskService.propAdditionLaunch(propVos[i],userVo, vo.getLicense(), remarks);
				if(!flags.equals("ok")){
					if(vo.getSerialNo()>1){
						retStr = retStr+"三者车";
					}else{
						String remark = codeTranService.transCode("DefLossItemType",vo.getSerialNo().toString());
						retStr = retStr+remark;
					}
					retStr = retStr + "(" +vo.getLicense()+ ")" + flags;
				}
				PrpLClaimTextVo claimTextVo = new PrpLClaimTextVo();
				claimTextVo.setBussTaskId(propVos[i]);
				claimTextVo.setBigNode(FlowNode.DLProp.toString());
				claimTextVo.setNodeCode(FlowNode.DLPropMod.toString());
				claimTextVo.setBussNo(vo.getRegistNo());
				claimTextVo.setRegistNo(vo.getRegistNo());
				claimTextVo.setTextType("2");
				claimTextVo.setDescription(remarks);
				claimTextVo.setOperatorCode(WebUserUtils.getUserCode());
//				claimTextVo.setOperatorName(WebUserUtils.getComName());
				claimTextVo.setComCode(WebUserUtils.getComCode());
//				claimTextVo.setComName(WebUserUtils.getComName());
				claimTextService.saveOrUpdte(claimTextVo);
				}
			}catch(Exception e){
				ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
				ajaxResult.setStatusText(e.getMessage());
				e.printStackTrace();
			}
	/*		mv.addObject("persInjuredVo",persInjuredVo);
			mv.setViewName("compensate/cancelInit");*/
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setStatusText(retStr);
			return ajaxResult;
		}
		
	private Map<String, String> handleKindMap(String kindStr) {
		Map<String, String> kindMap = new HashMap<String, String>();
		kindStr = kindStr.replace("{", "");
		kindStr = kindStr.replace("}", "");
		kindStr = kindStr.replace(" ", "");

		String[] kindArr = kindStr.split(",");
		if (kindArr.length > 0) {
			for (String temp : kindArr) {
				String[] tempArr = temp.split("=");
				kindMap.put(tempArr[0], tempArr[1]);
			}
		}

		return kindMap;
	}

	/**
	 * 异步校验定损是否可以发起重大赔案上报
	 * @param registNo
	 * @return
	 */
	@RequestMapping("/isMajorSelectValid.ajax")
	@ResponseBody
	public AjaxResult isMajorSelectValid(String registNo) {
		AjaxResult ajaxResult = new AjaxResult();
		boolean isExist = wfTaskHandleService.existTaskByNodeCode(registNo,FlowNode.ChkBig,null,"");
		ajaxResult.setData(isExist);
		return ajaxResult;
	}
	
	/**
	 * 判断是否有损失信息，包括（零部件更换费用清单、零部件辅料费用清单、零部件修理费用清单 ）
	 * @param lossMainId
	 * @return
	 */
	@RequestMapping("/existLoss.ajax")
	@ResponseBody
	public AjaxResult existLoss(String lossMainId) {
		AjaxResult ajaxResult = new AjaxResult();
		if(lossMainId.isEmpty()){
			ajaxResult.setData(false);
		}else{
			PrpLDlossCarMainVo lossCarMainVo = lossCarService.findLossCarMainById(Long.valueOf(lossMainId));
			if(lossCarMainVo!=null && (
					(lossCarMainVo.getPrpLDlossCarComps()!=null && lossCarMainVo.getPrpLDlossCarComps().size()>0) ||
					(lossCarMainVo.getPrpLDlossCarMaterials()!=null && lossCarMainVo.getPrpLDlossCarMaterials().size()>0) ||
					(lossCarMainVo.getPrpLDlossCarRepairs()!=null && lossCarMainVo.getPrpLDlossCarRepairs().size()>0))){
				ajaxResult.setData(true);
			}else{
				ajaxResult.setData(false);
			}
		}
		
		return ajaxResult;
	}
		
// 识别查勘是否完成
	@RequestMapping(value = "/checkComp.ajax")
	@ResponseBody
	public AjaxResult checkComp(String registNo,String serialNo) {
		AjaxResult ajaxResult = new AjaxResult();
		String flag = "0";	
		try {
			PrpLCheckCarVo checkCarVo = checkTaskService.findCheckCarBySerialNo(registNo,Integer.valueOf(serialNo));
			List<PrpLWfTaskVo> prpLwfs = wfFlowQueryService.findWfTaskVo(registNo,FlowNode.Chk, "1");
			
			if (prpLwfs != null && prpLwfs.size() > 0 && checkCarVo!=null) {
				flag = "1";
			}

			ajaxResult.setData(flag);
			ajaxResult.setStatus(HttpStatus.SC_OK);

		} catch (Exception e) {
			ajaxResult.setStatusText(e.getMessage());
		}
		return ajaxResult;
	}
	
	/**
	 * 若历次有过推定全损、盗抢折旧、赔付达到保额，则再次标的车定损时控制只能0定损，
	 * 并给出提示“此保单有赔付过全损、盗抢、达到保额情形之一，只能0定损.
	 * @param policyNo
	 * @return
	 */
	private String search(DeflossActionVo deflossVo){

		//排除互碰自赔
		if("1".equals(deflossVo.getLossCarMainVo().getIsClaimSelf())){
			return "";
		}
		String registNo = deflossVo.getRegistVo().getRegistNo();
		String policyNo = deflossVo.getRegistVo().getPolicyNo();
		String signIndex="";
		List<PrpLCompensateVo> compensateVos=compensateTaskService.findPrpCompensateBypolicyNo(policyNo);
		
	    double  oldSumMoney=compensateTaskService.findPrpllossBypolicyNo(policyNo);
		double sumMoney=0;
		double sumpaidAount=0;//总保额
		String flag = "";
		if(StringUtils.isNotBlank(policyNo)){
		  flag=lossCarService.checkCarLossHisByPolicyNo(policyNo);
		  if("02".equals(flag)){
			  return "此保单有赔付过推定全损，只能0定损!";
		  }else if("03".equals(flag)){
			  return "此保单有赔付过全车盗抢，只能0定损!";
		  }
		}
		
		//新理赔理算历次赔款金额之和
		if(compensateVos!=null && compensateVos.size()>0){
			for(PrpLCompensateVo vo:compensateVos){
				List<PrpLLossItemVo> prpLLossItems=vo.getPrpLLossItems();
				for(PrpLLossItemVo prpLLossItemVo:prpLLossItems){
					if("A".equals(prpLLossItemVo.getKindCode())){
						if("1".equals(vo.getUnderwriteFlag())){
							// prpLLossItem表中SumRealPay表示的是整个险别在历次出险理赔过程中每次赔付的总金额，其中包含各次预付金额（如果有）
							sumMoney=sumMoney+DataUtils.NullToZero(prpLLossItemVo.getSumRealPay()).doubleValue();//+DataUtils.NullToZero(prpLLossItemVo.getOffPreAmt()).doubleValue();
							 break;
						}
					}
				}
			}
		}
		
		//新旧理赔理算历次赔款之和
		 sumMoney=oldSumMoney+sumMoney;
		 
		//该保单的总保额
		PrpLCMainVo cmainVo=policyViewService.findPolicyInfoByPaltform(registNo,policyNo);
		Boolean bl = false;//是否承保A险
		if(cmainVo!=null ){
			if(cmainVo.getSumAmount()!=null){
				List<PrpLCItemKindVo> cItems=cmainVo.getPrpCItemKinds();
				for(PrpLCItemKindVo prpLCItemKindVo:cItems){
					if("A".equals(prpLCItemKindVo.getKindCode())){
						sumpaidAount=DataUtils.NullToZero(prpLCItemKindVo.getAmount()).doubleValue();
						bl = true;
					}
				}
			}
		}
		//当历次赔款总和大于保额
		if(bl && (sumMoney>=sumpaidAount)){
			signIndex = "此保单车损险赔付金额已达到保额，只能0定损!";
		}
		return signIndex;
		
	}
     /**
      * 德联易控信息
      * @param registNo
      * @param licenseNo
      * @return
     * @throws UnsupportedEncodingException 
     * 
      */
	@RequestMapping(value = "/controlExpert.do")
	public ModelAndView controlExpert(String registNo,Long lossCarMainId,HttpServletRequest request) throws UnsupportedEncodingException{
		String licenseNo=request.getParameter("licenseNo");
		licenseNo=URLDecoder.decode(licenseNo,"UTF-8");
		ModelAndView mv=new ModelAndView();
		PrplTestinfoMainVo prplTestinfoMainVo=claimTaskService.findPrplTestinfoMainByRegistNoAndLicenseNo(registNo,licenseNo,lossCarMainId,"3");//定损
		List<PrplFraudriskInfoVo> prplFraudriskInfoVos=new ArrayList<PrplFraudriskInfoVo>();
		List<PrplLaborInfoVo> prplLaborInfoVos=new ArrayList<PrplLaborInfoVo>();
		List<PrplOperationInfoVo> prplOperationInfoVos=new ArrayList<PrplOperationInfoVo>();
		List<PrplPartsInfoVo> prplPartsInfoVos=new ArrayList<PrplPartsInfoVo>();
		List<PrplRiskpointInfoVo> prplRiskpointInfoVos=new ArrayList<PrplRiskpointInfoVo>();
		if(prplTestinfoMainVo!=null && prplTestinfoMainVo.getId()!=null){
			if(StringUtils.isNotBlank(prplTestinfoMainVo.getLossType())){
				if("1".equals(prplTestinfoMainVo.getLossType())){
					prplTestinfoMainVo.setLossType("标的车");
				}else{
					prplTestinfoMainVo.setLossType("三者车");
				}
			}
			
			if(StringUtils.isNotBlank(prplTestinfoMainVo.getClaimResult())){
				if("1".equals(prplTestinfoMainVo.getClaimResult())){
					prplTestinfoMainVo.setClaimResult("正常");
				}else if("2".equals(prplTestinfoMainVo.getClaimResult())){
					prplTestinfoMainVo.setClaimResult("有减损无风险提示");
				}else if("3".equals(prplTestinfoMainVo.getClaimResult())){
					prplTestinfoMainVo.setClaimResult("无减损有风险提示");
				}else if("4".equals(prplTestinfoMainVo.getClaimResult())){
					prplTestinfoMainVo.setClaimResult("有减损有风险提示");
				}else{
					
				}
			}
			prplFraudriskInfoVos=prplTestinfoMainVo.getPrplFraudriskInfos();
			prplLaborInfoVos=prplTestinfoMainVo.getPrplLaborInfos();
			prplOperationInfoVos=prplTestinfoMainVo.getPrplOperationInfos();
			prplPartsInfoVos=prplTestinfoMainVo.getPrplPartsInfos();
			prplRiskpointInfoVos=prplTestinfoMainVo.getPrplRiskpointInfos();
		}
	   
		//报案
		PrplTestinfoMainVo prplTestinfoMainVo1=claimTaskService.findPrplTestinfoMainByRegistNoAndLicenseNo(registNo,licenseNo,lossCarMainId,"1");//报案
		List<PrplFraudriskInfoVo> prplFraudriskInfoVos1=new ArrayList<PrplFraudriskInfoVo>();
		List<PrplLaborInfoVo> prplLaborInfoVos1=new ArrayList<PrplLaborInfoVo>();
		List<PrplOperationInfoVo> prplOperationInfoVos1=new ArrayList<PrplOperationInfoVo>();
		List<PrplPartsInfoVo> prplPartsInfoVos1=new ArrayList<PrplPartsInfoVo>();
		List<PrplRiskpointInfoVo> prplRiskpointInfoVos1=new ArrayList<PrplRiskpointInfoVo>();
		if(prplTestinfoMainVo1!=null && prplTestinfoMainVo1.getId()!=null){
			if(StringUtils.isNotBlank(prplTestinfoMainVo1.getLossType())){
				if("1".equals(prplTestinfoMainVo1.getLossType())){
					prplTestinfoMainVo1.setLossType("标的车");
				}else{
					prplTestinfoMainVo1.setLossType("三者车");
				}
			}
			
			if(StringUtils.isNotBlank(prplTestinfoMainVo1.getClaimResult())){
				if("1".equals(prplTestinfoMainVo1.getClaimResult())){
					prplTestinfoMainVo1.setClaimResult("正常");
				}else if("2".equals(prplTestinfoMainVo1.getClaimResult())){
					prplTestinfoMainVo1.setClaimResult("有减损无风险提示");
				}else if("3".equals(prplTestinfoMainVo1.getClaimResult())){
					prplTestinfoMainVo1.setClaimResult("无减损有风险提示");
				}else if("4".equals(prplTestinfoMainVo1.getClaimResult())){
					prplTestinfoMainVo1.setClaimResult("有减损有风险提示");
				}else{
					
				}
			}
			
			prplFraudriskInfoVos1=prplTestinfoMainVo1.getPrplFraudriskInfos();
			prplLaborInfoVos1=prplTestinfoMainVo1.getPrplLaborInfos();
			prplOperationInfoVos1=prplTestinfoMainVo1.getPrplOperationInfos();
			prplPartsInfoVos1=prplTestinfoMainVo1.getPrplPartsInfos();
			prplRiskpointInfoVos1=prplTestinfoMainVo1.getPrplRiskpointInfos();
		}
		
		//查勘
		PrplTestinfoMainVo prplTestinfoMainVo2=claimTaskService.findPrplTestinfoMainByRegistNoAndLicenseNo(registNo,licenseNo,lossCarMainId,"2");
		List<PrplFraudriskInfoVo> prplFraudriskInfoVos2=new ArrayList<PrplFraudriskInfoVo>();
		List<PrplLaborInfoVo> prplLaborInfoVos2=new ArrayList<PrplLaborInfoVo>();
		List<PrplOperationInfoVo> prplOperationInfoVos2=new ArrayList<PrplOperationInfoVo>();
		List<PrplPartsInfoVo> prplPartsInfoVos2=new ArrayList<PrplPartsInfoVo>();
		List<PrplRiskpointInfoVo> prplRiskpointInfoVos2=new ArrayList<PrplRiskpointInfoVo>();
		if(prplTestinfoMainVo2!=null && prplTestinfoMainVo2.getId()!=null){
			if(StringUtils.isNotBlank(prplTestinfoMainVo2.getLossType())){
				if("1".equals(prplTestinfoMainVo2.getLossType())){
					prplTestinfoMainVo2.setLossType("标的车");
				}else{
					prplTestinfoMainVo2.setLossType("三者车");
				}
			}
			
			if(StringUtils.isNotBlank(prplTestinfoMainVo2.getClaimResult())){
				if("1".equals(prplTestinfoMainVo2.getClaimResult())){
					prplTestinfoMainVo2.setClaimResult("正常");
				}else if("2".equals(prplTestinfoMainVo2.getClaimResult())){
					prplTestinfoMainVo2.setClaimResult("有减损无风险提示");
				}else if("3".equals(prplTestinfoMainVo2.getClaimResult())){
					prplTestinfoMainVo2.setClaimResult("无减损有风险提示");
				}else if("4".equals(prplTestinfoMainVo2.getClaimResult())){
					prplTestinfoMainVo2.setClaimResult("有减损有风险提示");
				}else{
					
				}
			}
			
			prplFraudriskInfoVos2=prplTestinfoMainVo2.getPrplFraudriskInfos();
			prplLaborInfoVos2=prplTestinfoMainVo2.getPrplLaborInfos();
			prplOperationInfoVos2=prplTestinfoMainVo2.getPrplOperationInfos();
			prplPartsInfoVos2=prplTestinfoMainVo2.getPrplPartsInfos();
			prplRiskpointInfoVos2=prplTestinfoMainVo2.getPrplRiskpointInfos();
		}
		
		mv.addObject("prplTestinfoMainVo",prplTestinfoMainVo);
		mv.addObject("prplFraudriskInfoVos",prplFraudriskInfoVos);
		mv.addObject("prplLaborInfoVos",prplLaborInfoVos);
		mv.addObject("prplOperationInfoVos",prplOperationInfoVos);
		mv.addObject("prplPartsInfoVos",prplPartsInfoVos);
		mv.addObject("prplRiskpointInfoVos",prplRiskpointInfoVos);
		
		mv.addObject("prplTestinfoMainVo1",prplTestinfoMainVo1);
		mv.addObject("prplFraudriskInfoVos1",prplFraudriskInfoVos1);
		mv.addObject("prplLaborInfoVos1",prplLaborInfoVos1);
		mv.addObject("prplOperationInfoVos1",prplOperationInfoVos1);
		mv.addObject("prplPartsInfoVos1",prplPartsInfoVos1);
		mv.addObject("prplRiskpointInfoVos1",prplRiskpointInfoVos1);
		
		mv.addObject("prplTestinfoMainVo2",prplTestinfoMainVo2);
		mv.addObject("prplFraudriskInfoVos2",prplFraudriskInfoVos2);
		mv.addObject("prplLaborInfoVos2",prplLaborInfoVos2);
		mv.addObject("prplOperationInfoVos2",prplOperationInfoVos2);
		mv.addObject("prplPartsInfoVos2",prplPartsInfoVos2);
		mv.addObject("prplRiskpointInfoVos2",prplRiskpointInfoVos2);
		mv.setViewName("lossCar/ControlExpertView1");
		return mv;
	}
	
	@RequestMapping(value = "/controlExpertViewOfcheck.do")
	public ModelAndView controlExpertViewOfcheck(String registNo){
		ModelAndView mav=new ModelAndView();
		//查询该报案号下，报案返回的德联易控的信息，1-报案
		List<PrplTestinfoMainVo> prplTestinfoMainVoRs=claimTaskService.findPrplTestinfoMainByRegistNoAndNodeFlag(registNo,"1");
		//查询该报案号下，查勘返回的德联易控的信息，2-查勘
		List<PrplTestinfoMainVo> prplTestinfoMainVoCs=claimTaskService.findPrplTestinfoMainByRegistNoAndNodeFlag(registNo,"2");
		
		if(prplTestinfoMainVoRs!=null && prplTestinfoMainVoRs.size()>0){
			for(PrplTestinfoMainVo vo:prplTestinfoMainVoRs){
			    if(StringUtils.isNotBlank(vo.getClaimResult())){
					if("1".equals(vo.getClaimResult())){
						vo.setClaimResult("正常");
					}else if("2".equals(vo.getClaimResult())){
						vo.setClaimResult("有减损无风险提示");
					}else if("3".equals(vo.getClaimResult())){
						vo.setClaimResult("无减损有风险提示");
					}else if("4".equals(vo.getClaimResult())){
						vo.setClaimResult("有减损有风险提示");
					}else{
						
					}
				}
			}
		}
		
		if(prplTestinfoMainVoCs!=null && prplTestinfoMainVoCs.size()>0){
			for(PrplTestinfoMainVo vo:prplTestinfoMainVoCs){
				if(StringUtils.isNotBlank(vo.getClaimResult())){
					if("1".equals(vo.getClaimResult())){
						vo.setClaimResult("正常");
					}else if("2".equals(vo.getClaimResult())){
						vo.setClaimResult("有减损无风险提示");
					}else if("3".equals(vo.getClaimResult())){
						vo.setClaimResult("无减损有风险提示");
					}else if("4".equals(vo.getClaimResult())){
						vo.setClaimResult("有减损有风险提示");
					}else{
						
					}
				}
			}
		}
		mav.addObject("prplTestinfoMainVoRs",prplTestinfoMainVoRs);
		mav.addObject("prplTestinfoMainVoCs",prplTestinfoMainVoCs);
		mav.setViewName("lossCar/ControlExpertView");
		
		return mav;
	}

	@RequestMapping(value = "/openPhotoVerify.do")
	@ResponseBody
	public ModelAndView openPhotoVerify(HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		String registNo = request.getParameter("registNo");
		String mainId = request.getParameter("mainId");
		String offLineHanding = request.getParameter("offLineHanding");
		String nodeCode = request.getParameter("nodeCode");
		mv.addObject("registNo", registNo);
		mv.addObject("mainId", mainId);
		mv.addObject("nodeCode", nodeCode);
		mv.addObject("offLineHanding", offLineHanding);
		mv.setViewName("lossCar/PhotoVerify");
		return mv;
	}
	
	@RequestMapping(value = "/PhotoVerify.do") 
	@ResponseBody
	public AjaxResult photoVerify(@RequestParam("registNo") String registNo,
			@RequestParam("mainId") String lossMainId,
			@RequestParam("photoStatus") String photoStatus,
			@RequestParam("offLineHanding") String offLineHanding){
		
		AjaxResult ajaxResult = new AjaxResult();
		try{
			SysUserVo userVo = WebUserUtils.getUser();
			//定损照片审核不通过
			deflossHandleService.photoVerifyToHNQC(registNo, lossMainId, photoStatus, CodeConstants.RadioValue.RADIO_NO,offLineHanding,CodeConstants.HNQCDataType.PHOTOVERIFY,userVo);
			ajaxResult.setStatus(HttpStatus.SC_OK);
		}catch(Exception e){
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		
		return ajaxResult;
	}

    @RequestMapping(value = "/checkSubrogationFlag.do") 
    @ResponseBody
    public AjaxResult checkSubrogationFlag(String registNo){
        List<PrpLCMainVo> mainVoList = prpLCMainService.findPrpLCMainsByRegistNo(registNo);
        Boolean iskindA = false;
        for(PrpLCMainVo vo : mainVoList){
            if("12".equals(vo.getRiskCode().substring(0,2))){
                for(PrpLCItemKindVo itemKindVo : vo.getPrpCItemKinds()){
                    if("A".equals(itemKindVo.getKindCode()) || "A1".equals(itemKindVo.getKindCode())){
                        iskindA =true;
                        break;
                    }
                }
            }
        }
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setData("1");
        if(!iskindA){
            ajaxResult.setStatusText("本车无车损险，不能选择代位求偿！");
            ajaxResult.setData("0");
        }
        ajaxResult.setStatus(HttpStatus.SC_OK);
        
        return ajaxResult;
    } 

    
    @RequestMapping(value = "/checkKindCode.ajax") 
    @ResponseBody
    public AjaxResult checkKindCode(@RequestParam("lossMainId") String lossMainId){
        
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setStatusText("0");
        try{
            PrpLDlossCarMainVo lossMainVo = lossCarService.findLossCarMainById(Long.parseLong(lossMainId));
            List<PrpLDlossCarCompVo> vos = lossMainVo.getPrpLDlossCarComps();
            if(vos != null && vos.size() > 0 ){
                for(PrpLDlossCarCompVo vo : vos){
                    if("1".equals(vo.getWadFlag())){
                        ajaxResult.setStatusText("1");//配件勾选了涉水
                    }
                }
            }
            ajaxResult.setStatus(HttpStatus.SC_OK);
        }catch(Exception e){
            ajaxResult.setStatus(HttpStatus.SC_NOT_FOUND);
            ajaxResult.setStatusText(e.getMessage());
            e.printStackTrace();
        }
        return ajaxResult;
    }
    
	/**
	 * 进入精友组织claimFittingVo
	 * ☆yangkun(2016年2月19日 下午4:18:17): <br>
	 */
	@RequestMapping(value="/jyViewData.do")
	public ModelAndView jyViewData(String registNo,String id) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("registNo",registNo);
		mv.addObject("id",id);
		//jyViewDataService.sendViewDataService(registNo, dmgVhclId, userVo);
		mv.setViewName("lossCar/OpenViewLossData");
		return mv;
	}
	
	@RequestMapping(value = "/showDlhk.do")
	public ModelAndView showDlhk(String registNo){
		//阳杰复检信息查看
		ModelAndView mav = new ModelAndView();
		List<PrpLDlhkMainVo> prpLDlhkMainVos = yjPrpLDlhkMainService.findPrpLDlhkMains(registNo);
		mav.addObject("prpLDlhkMainVos", prpLDlhkMainVos);
		mav.setViewName("dlhk/DeflossEdit_DlhkInfo");
		return mav;

	}
	
	@RequestMapping(value = "/showDetailedDlhkInfo.do")
	@ResponseBody
	public ModelAndView showDetailedDlhkInfo(String topActualId){
		//阳杰复检信息查看
		ModelAndView mav = new ModelAndView();
		List<PrpLDlhkMainVo> prpLDlhkMainVos = yjPrpLDlhkMainService.findPrpLDlhkMainsBytopActualId(topActualId);
		mav.addObject("prpLDlhkMainVo", prpLDlhkMainVos.get(0));
		mav.addObject("prpLDlchkInfos", prpLDlhkMainVos.get(0).getPrpLDlchkInfos());
		mav.setViewName("dlhk/DeflossEdit_DetailedDlhkInfo");
		return mav;

	}
	/**
	 * 1--代表标的车车辆种类，3--代表三者车车辆种类
	 * @param carFlag
	 * @return
	 */
	private List<String> carkindCodes(String carFlag){
		List<String> carKinds=new ArrayList<String>();
		if("1".equals(carFlag)){
			carKinds.add("TA");
			carKinds.add("G0");
			carKinds.add("H0");
			carKinds.add("H1");
			carKinds.add("T21");
		}else if("3".equals(carFlag)){
			carKinds.add("21");
			carKinds.add("22");
			carKinds.add("23");
			carKinds.add("24");
			carKinds.add("25");
			carKinds.add("26");
			carKinds.add("27");
			carKinds.add("28");
			carKinds.add("30");
			carKinds.add("31");
			carKinds.add("40");
			carKinds.add("41");
			carKinds.add("50");
			carKinds.add("51");
			carKinds.add("60");
		}
		return carKinds;
	}
}
