package ins.sino.claimcar.losscar.service;

import ins.platform.utils.DataUtils;
import ins.platform.utils.XstreamFactory;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.UnderWriteFlag;
import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.ilog.defloss.vo.Accessories;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;
import ins.sino.claimcar.ilog.defloss.vo.VPriceReqVo;
import ins.sino.claimcar.ilog.rule.service.RuleReturnDataSaveService;
import ins.sino.claimcar.ilog.rule.vo.IlogDataProcessingVo;
import ins.sino.claimcar.ilog.vclaim.vo.SingleCarLossInfo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainHisVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMaterialVo;
import ins.sino.claimcar.losscar.vo.SubmitNextVo;
import ins.sino.claimcar.lossprop.service.PropLossService;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainHisVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.other.vo.PrpLAutoVerifyVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.service.RegistTmpService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.subrogation.service.SubrogationService;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;
import ins.sino.claimcar.trafficplatform.service.EarlyWarnService;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

/**
 * ????????????????????????????????????
 * <pre></pre>
 * @author ???zhujunde
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("deflossHandleIlogService")
public class DeflossHandleServiceIlogImpl implements DeflossHandleIlogService {
    
    private static Logger logger = LoggerFactory.getLogger(DeflossHandleServiceIlogImpl.class);
    
    private static final String CheckRuleForCarVerifyPriceServlet = "CheckRuleForCarVerifyPriceServlet";
    @Autowired
    RegistService registService;
    @Autowired
    DeflossService deflossService;
    @Autowired
    SubrogationService subrogationService;
    @Autowired
    PrpLCMainService prpLCMainService;
    @Autowired
    RegistQueryService registQueryService;
    @Autowired
    PolicyViewService policyViewService;
    @Autowired
    LossCarService lossCarService;
    @Autowired
    PropTaskService propTaskService;
    @Autowired
    RegistTmpService registTmpService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	private EarlyWarnService earlyWarnService;
	@Autowired
	private RuleReturnDataSaveService ruleReturnDataSaveService;
	@Autowired
	private WfFlowService wfFlowService;
	@Autowired
	private PropLossService propLossService;
	@Autowired
	private DeflossHandleService deflossHandleService;
	
    @Override
    public LIlogRuleResVo organizaVprice(PrpLDlossCarMainVo lossCarMainVo,String operateType,SysUserVo userVo,BigDecimal taskId,String triggerNode,SubmitNextVo nextVo,String isSubmitHeadOffice) throws Exception {
        VPriceReqVo vPriceReqVo = new VPriceReqVo();
        LIlogRuleResVo lIlogRuleResVo = new LIlogRuleResVo();
        String registNo = lossCarMainVo.getRegistNo();
        PrpLRegistVo prpLRegistVo = registService.findRegistByRegistNo(registNo);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        PrpLWfTaskVo prpLWfTaskVo=wfFlowService.findPrpLWfTaskQueryByTaskId(taskId);
        List<PrpLCMainVo> prpLCMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(registNo);
        vPriceReqVo.setRegistNo(registNo);
        //vPriceReqVo.setCurrentNodeNo("");//??????????????????
        vPriceReqVo.setOperateType(operateType);//????????????
        vPriceReqVo.setRiskCode(lossCarMainVo.getRiskCode());
        vPriceReqVo.setComCode(lossCarMainVo.getMakeCom());
        vPriceReqVo.setTaskType("0");
        vPriceReqVo.setRepairFactoryType(lossCarMainVo.getRepairFactoryType());
        if(isSubmitHeadOffice != null){
        	vPriceReqVo.setExistHeadOffice(isSubmitHeadOffice);
        }  else{
        	vPriceReqVo.setExistHeadOffice(CodeConstants.CommonConst.FALSE);
        }
        String damageTime = format.format(prpLRegistVo.getDamageTime());
        vPriceReqVo.setDamageDate(damageTime);
        damageTime = damageTime.substring(11,damageTime.length());
        String[] damageHourMinute = damageTime.split(":");
        if(damageHourMinute.length > 1){
            String damageHour = damageHourMinute[0];
            vPriceReqVo.setDamageHour(damageHour);
            String damageMinute = damageHourMinute[1];
            vPriceReqVo.setDamageMinute(damageMinute);
        }
        
        if(prpLCMainVoList!=null && prpLCMainVoList.size()>0){
        	vPriceReqVo.setCoinsFlag("0".equals(prpLCMainVoList.get(0).getCoinsFlag()) ? "0":"1");//?????????????????????
        }
        
        if(prpLCMainVoList.get(0).getPrpCItemCars().get(0).getEnrollDate() != null){
        	vPriceReqVo.setEnrollDate(format.format(prpLCMainVoList.get(0).getPrpCItemCars().get(0).getEnrollDate())); //???????????? 
        }
           
        List<PrpLDlossCarCompVo> dlossCarCompVoList = lossCarMainVo.getPrpLDlossCarComps();
        //??????
        int jyStandardFittingsNum = 0;
        BigDecimal jystandardSingleFittingsHighAmount = new BigDecimal(0);
        BigDecimal jystandardFittingsSumAmount = new BigDecimal(0);
        //?????????
        int jyNonstandardFittingsNum = 0;
        BigDecimal jyNonstandardSingleFittingsHighAmount = new BigDecimal(0);
        BigDecimal jyNonstandardFittingsSumAmount = new BigDecimal(0);
        //???
        BigDecimal jyFittingsSumAmount = new BigDecimal(0);
        int replaceNum = 0;//???????????????????????????
        List<Accessories> accessoriesList = new ArrayList<Accessories>();
        for(PrpLDlossCarCompVo vo : dlossCarCompVoList){
            Accessories accessories = new Accessories();
            accessories.setIsStandard("0");
            // 0-????????????  1-??????????????? 2-????????????
            if("2".equals(vo.getSelfConfigFlag())){
                jyStandardFittingsNum += vo.getQuantity();
                if(jystandardSingleFittingsHighAmount.compareTo(vo.getMaterialFee())<0){//???????????????
                    jystandardSingleFittingsHighAmount = vo.getMaterialFee();
                }
                jystandardFittingsSumAmount = jystandardFittingsSumAmount.add(vo.getMaterialFee());//??????
                accessories.setIsStandard("1");
            }else{
                jyNonstandardFittingsNum += vo.getQuantity();
                if(jyNonstandardSingleFittingsHighAmount.compareTo(vo.getMaterialFee())<0){//???????????????
                    jyNonstandardSingleFittingsHighAmount = vo.getMaterialFee();
                }
                jyNonstandardFittingsSumAmount = jyNonstandardFittingsSumAmount.add(vo.getMaterialFee());//??????
            }
            replaceNum += vo.getReplaceNum()==null ? 0:vo.getReplaceNum();
            
            accessories.setCompCode(vo.getCompCode());
            accessories.setCompName(vo.getCompName());
            if(vo.getChgRefPrice() != null){
                accessories.setChgrefPrice(vo.getChgRefPrice().toString());
            }
            if(vo.getSumDefLoss() != null){
                accessories.setSumdefLoss(vo.getSumDefLoss().toString());
            }
            if(vo.getChgLocPrice() != null){
                accessories.setChglocPrice(vo.getChgLocPrice().toString());
            }
            
            if(vo.getQuantity() != null){
                accessories.setQuantity(vo.getQuantity().toString());
            }
            accessoriesList.add(accessories);
        }
        //????????????
        vPriceReqVo.setAccessoriesList(accessoriesList);
        
        vPriceReqVo.setFittingsChangeSumNum(String.valueOf(replaceNum));
        jyFittingsSumAmount = jystandardFittingsSumAmount.add(jyNonstandardFittingsSumAmount);
        vPriceReqVo.setJyStandardFittingsNum(String.valueOf(jyStandardFittingsNum));
        vPriceReqVo.setJystandardSingleFittingsHighAmount(jystandardSingleFittingsHighAmount.toString());
        vPriceReqVo.setJystandardFittingsSumAmount(jystandardFittingsSumAmount.toString());
        
        vPriceReqVo.setJyNonstandardFittingsNum(String.valueOf(jyNonstandardFittingsNum));
        vPriceReqVo.setJyNonstandardSingleFittingsHighAmount(jyNonstandardSingleFittingsHighAmount.toString());
        vPriceReqVo.setJyNonstandardFittingsSumAmount(jyNonstandardFittingsSumAmount.toString());
        
        vPriceReqVo.setJyFittingsSumAmount(jyFittingsSumAmount.toString());

        PrpLSubrogationMainVo prpLSubrogationMainVo = subrogationService.find(registNo);
        String subrogationFlag = "";
        if(prpLSubrogationMainVo!=null){
            subrogationFlag = prpLSubrogationMainVo.getSubrogationFlag();
        }else {
            PrpLCheckVo prpLCheckVo = checkTaskService.findCheckVoByRegistNo(registNo);
            if(prpLCheckVo!=null){
                subrogationFlag = prpLCheckVo.getIsSubRogation();
            }
        }
        vPriceReqVo.setSubrogationFlag(subrogationFlag);//??????????????????  0???????????????  1????????????
        
        String coverageFlag = "0";
        BigDecimal carAmount = new BigDecimal(0);
        for(PrpLCMainVo vo : prpLCMainVoList){
            if("1".equals(coverageFlag)){
                break;
            }else{
                List<PrpLCItemKindVo> prpLCItemKindVoList = vo.getPrpCItemKinds();
                if(prpLCItemKindVoList != null && prpLCItemKindVoList.size() > 0){
                    for(PrpLCItemKindVo itemKindVo : prpLCItemKindVoList){
                        if("A".equals(itemKindVo.getKindCode())||"A1".equals(itemKindVo.getKindCode())){
                            coverageFlag = "1";
                            carAmount = itemKindVo.getAmount();
                            break;
                        }
                    }
                } 
            }
        }
        vPriceReqVo.setCoverageFlag(coverageFlag);
        if("1".equals(lossCarMainVo.getDeflossCarType())){
            if(lossCarMainVo.getSumLossFee() != null){
                vPriceReqVo.setMarkCarAmount(lossCarMainVo.getSumLossFee().toString());
            }
        }else{
            vPriceReqVo.setMarkCarAmount("0");
        }
        vPriceReqVo.setCarAmount(carAmount.toString());
        if(prpLCMainVoList != null && prpLCMainVoList.size() > 1 ){
            for(PrpLCMainVo vo : prpLCMainVoList){
                if("12".equals(vo.getRiskCode().substring(0,2))){//??????
                    List<PrpLCMainVo> vos = registTmpService.findAreadictByPolicyNo(vo.getPolicyNo());
                    //PrpLCMainVo prpLCMainVo = registQueryService.findPrpCmainByPolicyNo(vo.getPolicyNo());
                    PrpLCMainVo prpLCMainVo = vos.get(0);
                    if(prpLCMainVo.getStartDate() != null){
                        vPriceReqVo.setStartDate(format.format(prpLCMainVo.getStartDate()));
                    }
                    if(prpLCMainVo.getStartHour()!=null){
                    	vPriceReqVo.setStartHour(prpLCMainVo.getStartHour().toString());
                    }else{
                    	vPriceReqVo.setStartHour("0");
                    }
                    if(prpLCMainVo.getStartMinute()!=null && !"".equals(prpLCMainVo.getStartMinute())){
                    	vPriceReqVo.setStartMinute(prpLCMainVo.getStartMinute().toString());
                    }else{
                    	vPriceReqVo.setStartMinute("0");
                    }
                    if(prpLCMainVo.getEndDate() != null){
                        vPriceReqVo.setEndDate(format.format(prpLCMainVo.getEndDate()));
                    }
                    if(prpLCMainVo.getEndHour() != null){
                    	vPriceReqVo.setEndHour(prpLCMainVo.getEndHour().toString());
                    }else{
                    	vPriceReqVo.setEndHour("0");
                    }
                    if(prpLCMainVo.getEndMinute() != null){
                    	vPriceReqVo.setEndMinute(prpLCMainVo.getEndMinute().toString());
                    }else{
                    	vPriceReqVo.setEndMinute("0");
                    }
                    PrpLCItemCarVo prpLCItemCarVo = prpLCMainVo.getPrpCItemCars().get(0);
                   /* <c:if test="${fn:substring(prpLCItemCarVo.otherNature, 4, 5)=='1' }">???</c:if>
                    <c:if test="${fn:substring(prpLCItemCarVo.otherNature, 4, 5)!='1' }">???</c:if>*/
                    if("1".equals(prpLCItemCarVo.getOtherNature().substring(4,5))){//??????
                        vPriceReqVo.setChgOwnerFlag("1");
                    }else{
                        vPriceReqVo.setChgOwnerFlag("0");
                    }
                    vPriceReqVo.setUseKindCode(prpLCItemCarVo.getUseKindCode());
                }
            }
        }else if(prpLCMainVoList != null){
            List<PrpLCMainVo> vos = registTmpService.findAreadictByPolicyNo(prpLCMainVoList.get(0).getPolicyNo());
            PrpLCMainVo prpLCMainVo = vos.get(0);
            //PrpLCMainVo prpLCMainVo = registQueryService.findPrpCmainByPolicyNo(prpLCMainVoList.get(0).getPolicyNo());
            if(prpLCMainVo.getStartDate() != null){
                vPriceReqVo.setStartDate(format.format(prpLCMainVo.getStartDate()));
            }
            if(prpLCMainVo.getStartHour()!=null){
            	vPriceReqVo.setStartHour(prpLCMainVo.getStartHour().toString());
            }else{
            	vPriceReqVo.setStartHour("0");
            }
            if(prpLCMainVo.getStartMinute()!=null && !"".equals(prpLCMainVo.getStartMinute())){
            	vPriceReqVo.setStartMinute(prpLCMainVo.getStartMinute().toString());
            }else{
            	vPriceReqVo.setStartMinute("0");
            }
            if(prpLCMainVo.getEndDate() != null){
                vPriceReqVo.setEndDate(format.format(prpLCMainVo.getEndDate()));
            }
            if(prpLCMainVo.getEndHour() != null){
            	vPriceReqVo.setEndHour(prpLCMainVo.getEndHour().toString());
            }else{
            	vPriceReqVo.setEndHour("0");
            }
            if(prpLCMainVo.getEndMinute() != null){
            	vPriceReqVo.setEndMinute(prpLCMainVo.getEndMinute().toString());
            }else{
            	vPriceReqVo.setEndMinute("0");
            }
            PrpLCItemCarVo prpLCItemCarVo = prpLCMainVo.getPrpCItemCars().get(0);
             if("1".equals(prpLCItemCarVo.getOtherNature().substring(4,5))){//??????
                 vPriceReqVo.setChgOwnerFlag("1");
             }else{
                 vPriceReqVo.setChgOwnerFlag("0");
             }
             vPriceReqVo.setUseKindCode(prpLCItemCarVo.getUseKindCode());
        }
        //??????
/*        List<PrpLCMainVo> lCMainVoList = policyViewService.getPolicyAllInfo(registNo);
        for(PrpLCMainVo prpLCMaintmp : prpLCMainVoList){
            //?????????????????????????????????
            if(prpLCMaintmp.getPrpCItemCars().size() > 0){
                prpLCItemCarVo = prpLCMaintmp.getPrpCItemCars().get(0);
            };
        };*/
        vPriceReqVo.setReportDate(format.format(prpLRegistVo.getReportTime()));
        String reportTime = format.format(prpLRegistVo.getReportTime());
        reportTime = reportTime.substring(11,reportTime.length());
        String[] hourMinute = reportTime.split(":");
        if(hourMinute.length > 1){
            String reportHour = hourMinute[0];
            vPriceReqVo.setReportHour(reportHour);
            String reportMinute = hourMinute[1];
            vPriceReqVo.setReportMinute(reportMinute);
        }
        //??????????????????
        Map<String, String> registRiskInfoMap = new HashMap<String, String>();
        if (!StringUtils.isEmpty(registNo)) {
            registRiskInfoMap = registService.findRegistRiskInfoByRegistNo(registNo);
        }//CI-DangerNum
        String ciDangerNum = registRiskInfoMap.get("CI-DangerNum");
        if(StringUtils.isNotBlank(ciDangerNum)){
            vPriceReqVo.setJqDamagTime(ciDangerNum);
        }else{
            vPriceReqVo.setJqDamagTime("");
        }
        String ciDangerInSum = registRiskInfoMap.get("CI-DangerInSum");
        if(StringUtils.isNotBlank(ciDangerInSum)){
            vPriceReqVo.setJqSevenDaysDamagTime(ciDangerInSum);
        }else{
            vPriceReqVo.setJqSevenDaysDamagTime("0");
        }

        String biDangerNum = registRiskInfoMap.get("BI-DangerNum");
        if(StringUtils.isNotBlank(biDangerNum)){
            vPriceReqVo.setSyDamagtime(biDangerNum);
        }else{
            vPriceReqVo.setSyDamagtime("0");
        }
        String biDangerInSum = registRiskInfoMap.get("BI-DangerInSum");
        if(StringUtils.isNotBlank(biDangerInSum)){
            vPriceReqVo.setSySevenDaysDamagTime(biDangerInSum);
        }else{
            vPriceReqVo.setSySevenDaysDamagTime("0");
        }
        List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(registNo);
        // ?????????????????? ???0-???????????? 1-??????????????? ?????????????????????
        vPriceReqVo.setHandlerType("0");
        List<SingleCarLossInfo> singleCarLossInfos = new ArrayList<SingleCarLossInfo>();
        for (PrpLDlossCarMainVo carmain : prpLDlossCarMainVoList) {
        	// ????????????????????????????????????????????????????????????
        	if (carmain.getSumLossFee() != null) {
        		SingleCarLossInfo singlecarlossinfo = new SingleCarLossInfo();
        		singlecarlossinfo.setSingleCarSumloss(carmain.getSumLossFee().toString());
        		singleCarLossInfos.add(singlecarlossinfo);
        	}
        	
        	// ??????????????????????????????????????????????????????
        	if (carmain.getIntermFlag() != null && "1".equals(carmain.getIntermFlag()) && "0".equals(vPriceReqVo.getHandlerType())) {
        		vPriceReqVo.setHandlerType("1");
        	}
        }
        vPriceReqVo.setSingleCarlossCarInfoList(singleCarLossInfos);
        
        List<PrpLdlossPropMainVo> prpLdlossPropMainVoList = propTaskService.findPropMainListByRegistNo(registNo);
        BigDecimal thirdVehicleCertainAmount = new BigDecimal(0);
        if(prpLDlossCarMainVoList!=null && prpLDlossCarMainVoList.size()>0){
	        for(PrpLDlossCarMainVo vo : prpLDlossCarMainVoList){
	            if(vo.getSumLossFee() != null){
	                thirdVehicleCertainAmount = thirdVehicleCertainAmount.add(vo.getSumLossFee());
	            }
	        }
        }
        if(prpLdlossPropMainVoList!=null && prpLdlossPropMainVoList.size()>0){
	        for(PrpLdlossPropMainVo vo : prpLdlossPropMainVoList){
	            if(vo.getSumDefloss() != null){
	                thirdVehicleCertainAmount = thirdVehicleCertainAmount.add(vo.getSumDefloss());
	            }
	        }
        }
        vPriceReqVo.setThirdVehicleCertainAmount(thirdVehicleCertainAmount.toString());
        BigDecimal certainAmount = new BigDecimal(0);
        if(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossCarMainVo.getDeflossSourceFlag())){
            List<PrpLDlossCarMainVo> carMains = lossCarService.findLossCarMainBySerialNo(registNo,lossCarMainVo.getSerialNo());            
            for(PrpLDlossCarMainVo carMain : carMains){
                if(carMain.getSumMatFee() != null){
                    certainAmount = certainAmount.add(carMain.getSumMatFee());
                }
                if(carMain.getSumCompFee() != null){
                    certainAmount = certainAmount.add(carMain.getSumCompFee());
                }
            }
        }else{
            if(lossCarMainVo.getSumMatFee() != null){
                certainAmount = certainAmount.add(lossCarMainVo.getSumMatFee());
            }
            if(lossCarMainVo.getSumCompFee() != null){
                certainAmount = certainAmount.add(lossCarMainVo.getSumCompFee());
            }
        }
        vPriceReqVo.setCertainAmount(certainAmount.toString());
        //modificaCertainAmount
        BigDecimal modificamCertainAmount = new BigDecimal(0);
        List<PrpLDlossCarMainHisVo> lossMainHisList = deflossService.findDeflossHisByMainId(lossCarMainVo.getId());
        if(lossMainHisList != null && lossMainHisList.size() > 0){
            PrpLDlossCarMainHisVo lossCarMainHisVo = lossMainHisList.get(lossMainHisList.size()-1);
            if(lossCarMainHisVo.getSumLossFee() != null){
                modificamCertainAmount = modificamCertainAmount.add(lossCarMainHisVo.getSumLossFee());
            }
            if(lossCarMainHisVo.getSumRescueFee() != null){
                modificamCertainAmount = modificamCertainAmount.add(lossCarMainHisVo.getSumLossFee());
            }
            vPriceReqVo.setModificaCertainAmount(modificamCertainAmount.toString());
        }
        BigDecimal certainSumAmount = new BigDecimal(0);
        if(lossCarMainVo.getSumRescueFee() != null){
            certainSumAmount = certainSumAmount.add(lossCarMainVo.getSumRescueFee());
            vPriceReqVo.setRescueFeeAmount(lossCarMainVo.getSumRescueFee().toString());
        }
        BigDecimal sumLossFee = new BigDecimal("0");
        sumLossFee = lossCarMainVo.getSumLossFee();
        certainSumAmount = certainSumAmount.add(sumLossFee);
        
        if(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossCarMainVo.getDeflossSourceFlag())){
	        List<PrpLDlossCarMainVo> prpLDlossCarMains = deflossService.findLossCarMainBySerialNo(lossCarMainVo.getRegistNo(),lossCarMainVo.getSerialNo());
        	for(PrpLDlossCarMainVo prpLDlossCarMainVo : prpLDlossCarMains){
            	if(UnderWriteFlag.MANAL_UNDERWRITE.equals(prpLDlossCarMainVo.getUnderWriteFlag())){
            		sumLossFee = sumLossFee
            				.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumLossFee()))
    						.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumChargeFee()))//??????
    		        		.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumSubRiskFee()))//?????????
    						.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumRescueFee()))//?????????
    						.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumOutFee()));//??????
    	        }
            }     
		}			
        
		if(CodeConstants.JyFlag.NOIN.equals(lossCarMainVo.getFlag())){//??????????????????????????????????????? ??????????????????
			sumLossFee = sumLossFee
					.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))
					.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()));
		}else{
			if(lossCarMainVo.getSumVeripLoss()!=null 
					&& (CodeConstants.CetainLossType.DEFLOSS_REPAIR.equals(lossCarMainVo.getCetainLossType())
					|| CodeConstants.CetainLossType.DEFLOSS_AGREE.equals(lossCarMainVo.getCetainLossType()))){
				sumLossFee = sumLossFee
						.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))//??????
		        		.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()))//?????????
						.add(DataUtils.NullToZero(lossCarMainVo.getSumRescueFee()))//?????????
						.add(DataUtils.NullToZero(lossCarMainVo.getSumOutFee()));//??????
			}else{
				sumLossFee = sumLossFee
						.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))
						.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()));
			}
		}
		
            
        vPriceReqVo.setSumAmount(String.valueOf(sumLossFee));//???????????????
                
        vPriceReqVo.setCertainSumAmount(certainSumAmount.toString());
        vPriceReqVo.setDamageCode(prpLRegistVo.getDamageCode());
        //????????????,??????1??????0
        PrpLAutoVerifyVo prpLAutoVerifyVo =  new PrpLAutoVerifyVo();
        if(FlowNode.DLCar.name().equals(nextVo.getCurrentNode())){
    		prpLAutoVerifyVo.setUserCode(userVo.getUserCode());
            prpLAutoVerifyVo.setNode(triggerNode);
    	}else{
    		PrpLWfTaskVo dLossTaskVo = findDLossTaskVo(prpLWfTaskVo);
    		prpLAutoVerifyVo.setUserCode(dLossTaskVo.getHandlerUser());
            prpLAutoVerifyVo.setNode(dLossTaskVo.getSubNodeCode());
    	}
        Boolean isAutoVerifyUser = deflossHandleService.isAutoVerifyUser(prpLAutoVerifyVo);
        if(isAutoVerifyUser){
            vPriceReqVo.setSysAuthorizationFlag("1");
        }else{
            vPriceReqVo.setSysAuthorizationFlag("0");
        }
        
        List<PrpLClaimDeductVo> claimDeductVoList = new ArrayList<PrpLClaimDeductVo>();
        String deductCondCode = "";
        claimDeductVoList = registQueryService.findClaimDeductVoByRegistNo(registNo);// ???????????????????????????????????????
        if(claimDeductVoList != null && claimDeductVoList.size() > 0){
            for(PrpLClaimDeductVo vo : claimDeductVoList){
                if("1".equals(vo.getIsCheck())){
                    deductCondCode = deductCondCode + "," +vo.getDeductCondCode();
                }
            }
            if(deductCondCode.length()>0){
                deductCondCode = deductCondCode.substring(1,deductCondCode.length());//?????????????????????
            }
        }
        
        vPriceReqVo.setDeductCondCode(deductCondCode);
        vPriceReqVo.setEmployeeId(userVo.getUserCode());//????????????
        String lossPartyName="";
        String licenseNo="";
        String handlerId=prpLWfTaskVo.getHandlerIdKey();
        PrpLDlossCarMainVo vo=lossCarService.findLossCarMainById(Long.valueOf(handlerId));
        lossPartyName=vo.getDeflossCarType();
        licenseNo=vo.getLicenseNo();//?????????????????????
        vPriceReqVo.setLossPartyName(Integer.parseInt(lossPartyName)>1?"2":lossPartyName);//????????? 0  ??????/????????????  1  ?????????  2  ?????????

        vPriceReqVo.setIsNuclearpricereturn("0");//????????????????????????
        PrpLWfTaskVo upperPrpLWfTaskVo=wfFlowService.findPrpLWfTaskQueryByTaskId(prpLWfTaskVo.getUpperTaskId());
        if(WorkStatus.BACK.equals(upperPrpLWfTaskVo.getWorkStatus())&&FlowNode.VPrice.name().equals(upperPrpLWfTaskVo.getNodeCode())){   
            vPriceReqVo.setIsNuclearpricereturn("1");//????????????????????????
        }
        
        BigDecimal materialFee = new BigDecimal(0);
        if(lossCarMainVo.getPrpLDlossCarMaterials() != null && lossCarMainVo.getPrpLDlossCarMaterials().size() > 0){
            for(PrpLDlossCarMaterialVo carMaterialVo : lossCarMainVo.getPrpLDlossCarMaterials()){
                if(carMaterialVo.getMaterialFee() != null){
                    materialFee = materialFee.add(carMaterialVo.getMaterialFee());
                }
            }
        }
        vPriceReqVo.setJyingredientsSumAmount(materialFee.toString());
        

        String urlStr = SpringProperties.getProperty("ILOG_SVR_URL");//??????ILOG??????????????????
        urlStr = urlStr + CheckRuleForCarVerifyPriceServlet;//??????????????????
        String requestXML = XstreamFactory.objToXml(vPriceReqVo);//??????????????????xml
        logger.info("????????????????????????xml============" + requestXML);
        String returnXml = earlyWarnService.requestSDEW(requestXML,urlStr);//??????ILOG
        logger.info("????????????????????????xml============" + returnXml);
        lIlogRuleResVo = XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//??????????????????vo
        
        IlogDataProcessingVo ilogDataProcessingVo =new IlogDataProcessingVo();
        ilogDataProcessingVo.setBusinessNo(vPriceReqVo.getRegistNo());//????????? ?????????
        ilogDataProcessingVo.setCompensateNo("");//????????????   
        ilogDataProcessingVo.setComCode(vPriceReqVo.getComCode());//??????????????????
        ilogDataProcessingVo.setRiskCode(vPriceReqVo.getRiskCode());//??????
        ilogDataProcessingVo.setOperateType(vPriceReqVo.getOperateType());//????????????  1?????????  2???????????????
        ilogDataProcessingVo.setRuleType(vPriceReqVo.getTaskType());//???????????? 0?????????; 1?????????
        ilogDataProcessingVo.setRuleNode("VPrice");//????????????
        ilogDataProcessingVo.setLossParty(vPriceReqVo.getLossPartyName());//?????????
        ilogDataProcessingVo.setLicenseNo(licenseNo);//?????????????????????
        ilogDataProcessingVo.setTriggerNode(triggerNode);//????????????  ?????????????????????????????????
        ilogDataProcessingVo.setTaskId(taskId);//??????????????????????????????ID 
        ilogDataProcessingVo.setOperatorCode(userVo.getUserCode());//????????????
        
        ruleReturnDataSaveService.dealILogResReturnData(lIlogRuleResVo,ilogDataProcessingVo);//????????????
        return lIlogRuleResVo;
    }
    
    
    
    public LIlogRuleResVo organizaVProperty(PrpLdlossPropMainVo lossPropMainVo,String operateType,SysUserVo userVo,BigDecimal taskId,String triggerNode,String isSubmitHeadOffice ) throws Exception{
        VPriceReqVo vPriceReqVo = new VPriceReqVo();
        LIlogRuleResVo lIlogRuleResVo = new LIlogRuleResVo();
        String registNo = lossPropMainVo.getRegistNo();
        PrpLRegistVo prpLRegistVo = registService.findRegistByRegistNo(registNo);
        List<PrpLCMainVo> prpLCMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(registNo);//??????????????????
        vPriceReqVo.setRegistNo(registNo);//?????????
//        vPriceReqVo.setCurrentNodeNo(0);//??????????????????  ?????????????????????
//        if("2".equals(operateType)){//?????????????????????
//            vPriceReqVo.setCurrentNodeNo(currentNodeNo);//??????????????????  ?????????????????????
//        }
        if(isSubmitHeadOffice != null){
        	vPriceReqVo.setExistHeadOffice(isSubmitHeadOffice);
        } else{
        	vPriceReqVo.setExistHeadOffice(CodeConstants.CommonConst.FALSE);
        }
        vPriceReqVo.setOperateType(operateType);//????????????1?????????   2???????????????
        vPriceReqVo.setRiskCode(lossPropMainVo.getRiskCode());
        vPriceReqVo.setComCode(lossPropMainVo.getMakeCom());
        vPriceReqVo.setTaskType("1");//0?????????  1?????????
        vPriceReqVo.setRepairFactoryType("");//???????????????
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 

        String damageTime = format.format(prpLRegistVo.getDamageTime());
        vPriceReqVo.setDamageDate(damageTime);//????????????
        damageTime = damageTime.substring(11,damageTime.length());
        String[] damageHourMinute = damageTime.split(":");
        String damageHour ="";
        String damageMinute="";
        if(damageHourMinute.length > 1){
            damageHour = damageHourMinute[0];
            damageMinute = damageHourMinute[1];
        }
        vPriceReqVo.setDamageHour(damageHour);//????????????
        vPriceReqVo.setDamageMinute(damageMinute);//????????????
        
        if(prpLCMainVoList!=null && prpLCMainVoList.size()>0){
        	if(prpLCMainVoList.get(0).getPrpCItemCars().get(0).getEnrollDate() != null){
            	vPriceReqVo.setEnrollDate(format.format(prpLCMainVoList.get(0).getPrpCItemCars().get(0).getEnrollDate())); //???????????? 
            }
        	vPriceReqVo.setCoinsFlag("0".equals(prpLCMainVoList.get(0).getCoinsFlag()) ? "0":"1");//?????????????????????
        }

//        List<PrpLDlossCarCompVo> dlossCarCompVoList = new ArrayList<PrpLDlossCarCompVo>();//?????????????????? ?????????????????????
        //??????
        int jyStandardFittingsNum = 0;
        BigDecimal jystandardSingleFittingsHighAmount = new BigDecimal(0);
        BigDecimal jystandardFittingsSumAmount = new BigDecimal(0);
        //?????????
        int jyNonstandardFittingsNum = 0;
        BigDecimal jyNonstandardSingleFittingsHighAmount = new BigDecimal(0);
        BigDecimal jyNonstandardFittingsSumAmount = new BigDecimal(0);
        //???
        BigDecimal jyFittingsSumAmount = new BigDecimal(0);
        int replaceNum = 0;//???????????????????????????
        
        jyFittingsSumAmount = jystandardFittingsSumAmount.add(jyNonstandardFittingsSumAmount);
        //????????????
        vPriceReqVo.setJyStandardFittingsNum(String.valueOf(jyStandardFittingsNum));//????????????????????????????????????
        vPriceReqVo.setJystandardSingleFittingsHighAmount(String.valueOf(jystandardSingleFittingsHighAmount));//????????????????????????????????????????????????
        vPriceReqVo.setJyNonstandardFittingsNum(String.valueOf(jyNonstandardFittingsNum));//???????????????????????????????????????
        vPriceReqVo.setJyNonstandardSingleFittingsHighAmount(String.valueOf(jyNonstandardSingleFittingsHighAmount));//???????????????????????????????????????????????????
        vPriceReqVo.setJystandardFittingsSumAmount(String.valueOf(jystandardFittingsSumAmount));//?????????????????????????????????????????????
        vPriceReqVo.setJyNonstandardFittingsSumAmount(String.valueOf(jyNonstandardFittingsSumAmount));//????????????????????????????????????????????????
        vPriceReqVo.setJyFittingsSumAmount(String.valueOf(jyFittingsSumAmount)); //???????????????????????????????????????
        
        PrpLSubrogationMainVo prpLSubrogationMainVo = subrogationService.find(registNo);
        String subrogationFlag = "";
        if(prpLSubrogationMainVo!=null){
            subrogationFlag = prpLSubrogationMainVo.getSubrogationFlag();
        }else {
            PrpLCheckVo prpLCheckVo = checkTaskService.findCheckVoByRegistNo(registNo);
            if(prpLCheckVo!=null){
                subrogationFlag = prpLCheckVo.getIsSubRogation();
            }
        }
        vPriceReqVo.setSubrogationFlag(subrogationFlag);//??????????????????  0???????????????  1????????????
        
        String coverageFlag = "0";//?????????????????????????????????
        String carAmount = "";
        for(PrpLCMainVo vo : prpLCMainVoList){
            if("1".equals(coverageFlag)){
                break;
            }else{
                List<PrpLCItemKindVo> prpLCItemKindVoList = vo.getPrpCItemKinds();
                if(prpLCItemKindVoList != null && prpLCItemKindVoList.size() > 0){
                    for(PrpLCItemKindVo itemKindVo : prpLCItemKindVoList){
                        if("A".equals(itemKindVo.getKindCode())||"A1".equals(itemKindVo.getKindCode())){
                            coverageFlag = "1";
                            carAmount = String.valueOf(itemKindVo.getAmount());
                            break;
                        }
                    }
                } 
            }
        }
        vPriceReqVo.setCoverageFlag(coverageFlag);
        //??????????????????
        vPriceReqVo.setMarkCarAmount("0");
        
        List<PrpLDlossCarMainVo> lossCarMainList = lossCarService.findLossCarMainByRegistNo(registNo);
        for(PrpLDlossCarMainVo  lossCarMainVo:lossCarMainList){
            if("1".equals(lossCarMainVo.getDeflossCarType())){
                if(lossCarMainVo.getSumLossFee() != null){
                    vPriceReqVo.setMarkCarAmount(String.valueOf(lossCarMainVo.getSumLossFee()));
                }
            }
        }
        
        vPriceReqVo.setCarAmount(carAmount);//???????????????
        
        if(prpLCMainVoList != null && prpLCMainVoList.size() > 1 ){
            for(PrpLCMainVo vo : prpLCMainVoList){
                if(!Risk.isDQZ(vo.getRiskCode())){//??????
                    List<PrpLCMainVo> vos = registTmpService.findAreadictByPolicyNo(vo.getPolicyNo());
                    PrpLCMainVo prpLCMainVo = vos.get(0);
                    vPriceReqVo.setStartDate(format1.format(prpLCMainVo.getStartDate()));//????????????
                    if(prpLCMainVo.getStartHour()!=null){
                    	vPriceReqVo.setStartHour(prpLCMainVo.getStartHour().toString());
                    }else{
                    	vPriceReqVo.setStartHour("0");
                    }
                    if(prpLCMainVo.getStartMinute()!=null && !"".equals(prpLCMainVo.getStartMinute())){
                    	vPriceReqVo.setStartMinute(prpLCMainVo.getStartMinute().toString());
                    }else{
                    	vPriceReqVo.setStartMinute("0");
                    }
                    vPriceReqVo.setEndDate(format1.format(prpLCMainVo.getEndDate()));//????????????
                    if(prpLCMainVo.getEndHour() != null){
                    	vPriceReqVo.setEndHour(prpLCMainVo.getEndHour().toString());
                    }else{
                    	vPriceReqVo.setEndHour("0");
                    }
                    if(prpLCMainVo.getEndMinute() != null){
                    	vPriceReqVo.setEndMinute(prpLCMainVo.getEndMinute().toString());
                    }else{
                    	vPriceReqVo.setEndMinute("0");
                    }
                    PrpLCItemCarVo prpLCItemCarVo = prpLCMainVo.getPrpCItemCars().get(0);
                    if("1".equals(prpLCItemCarVo.getOtherNature().substring(4,5))){//??????
                        vPriceReqVo.setChgOwnerFlag("1");//????????????????????????
                    }else{
                        vPriceReqVo.setChgOwnerFlag("0");
                    }
                    vPriceReqVo.setUseKindCode(prpLCItemCarVo.getUseKindCode());
                }
            }
        }else if(prpLCMainVoList != null){
            List<PrpLCMainVo> vos = registTmpService.findAreadictByPolicyNo(prpLCMainVoList.get(0).getPolicyNo());
            PrpLCMainVo prpLCMainVo = vos.get(0);
            vPriceReqVo.setStartDate(format1.format(prpLCMainVo.getStartDate()));//????????????
            if(prpLCMainVo.getStartHour()!=null ){
            	vPriceReqVo.setStartHour(prpLCMainVo.getStartHour().toString());
            }else{
            	vPriceReqVo.setStartHour("0");
            }
            if(prpLCMainVo.getStartMinute()!=null && !"".equals(prpLCMainVo.getStartMinute())){
            	vPriceReqVo.setStartMinute(prpLCMainVo.getStartMinute().toString());
            }else{
            	vPriceReqVo.setStartMinute("0");
            }
            vPriceReqVo.setEndDate(format1.format(prpLCMainVo.getEndDate()));//????????????
            if(prpLCMainVo.getEndHour() != null){
            	vPriceReqVo.setEndHour(prpLCMainVo.getEndHour().toString());
            }else{
            	vPriceReqVo.setEndHour("0");
            }
            if(prpLCMainVo.getEndMinute() != null){
            	vPriceReqVo.setEndMinute(prpLCMainVo.getEndMinute().toString());
            }else{
            	vPriceReqVo.setEndMinute("0");
            }
            PrpLCItemCarVo prpLCItemCarVo = prpLCMainVo.getPrpCItemCars().get(0);
            if("1".equals(prpLCItemCarVo.getOtherNature().substring(4,5))){//??????
                vPriceReqVo.setChgOwnerFlag("1");//????????????????????????
             }else{
                 vPriceReqVo.setChgOwnerFlag("0");
             }
             vPriceReqVo.setUseKindCode(prpLCItemCarVo.getUseKindCode());
        }
        
        //????????????   
        String reportDate = format.format(prpLRegistVo.getReportTime());
        vPriceReqVo.setReportDate(reportDate);
        reportDate = reportDate.substring(11,reportDate.length());
        String[] reportHourMinute = reportDate.split(":");
        String reportHour ="";
        String reportMinute="";
        if(damageHourMinute.length > 1){
        	reportHour = reportHourMinute[0];
            reportMinute = reportHourMinute[1];
        }
        
        
        vPriceReqVo.setReportHour(reportHour);
        vPriceReqVo.setReportMinute(reportMinute);

        //??????????????????
        Map<String, String> registRiskInfoMap = new HashMap<String, String>();
        if (!StringUtils.isEmpty(registNo)) {
            registRiskInfoMap = registService.findRegistRiskInfoByRegistNo(registNo);
        }
        String ciDangerNum = registRiskInfoMap.get("CI-DangerNum");//????????????
        if(StringUtils.isNotBlank(ciDangerNum)){
            vPriceReqVo.setJqDamagTime(ciDangerNum);
        }else{
            vPriceReqVo.setJqDamagTime("");
        }
        String ciDangerInSum = registRiskInfoMap.get("CI-DangerInSum");//7??????????????????????????????
        if(StringUtils.isNotBlank(ciDangerInSum)){
            vPriceReqVo.setJqSevenDaysDamagTime(ciDangerInSum);
        }else{
            vPriceReqVo.setJqSevenDaysDamagTime("");
        }

        String biDangerNum = registRiskInfoMap.get("BI-DangerNum");//????????????????????????
        if(StringUtils.isNotBlank(biDangerNum)){
            vPriceReqVo.setSyDamagtime(biDangerNum);
        }else{
            vPriceReqVo.setSyDamagtime("");
        }
        String biDangerInSum = registRiskInfoMap.get("BI-DangerInSum");//7??????????????????????????????
        if(StringUtils.isNotBlank(biDangerInSum)){
            vPriceReqVo.setSySevenDaysDamagTime(biDangerInSum);
        }else{
            vPriceReqVo.setSySevenDaysDamagTime("");
        }
        
        vPriceReqVo.setFittingsChangeSumNum(String.valueOf(replaceNum));//???????????????????????????           
        
        BigDecimal thirdVehicleCertainAmount = new BigDecimal(0);
        for(PrpLDlossCarMainVo vo : lossCarMainList){
            if(vo.getSumLossFee() != null){
                thirdVehicleCertainAmount = thirdVehicleCertainAmount.add(vo.getSumLossFee());
            }
        }
        BigDecimal sumDefLoss = new BigDecimal("0");
		if(lossPropMainVo.getSumDefloss()!=null){
			sumDefLoss = lossPropMainVo.getSumDefloss();
			thirdVehicleCertainAmount = thirdVehicleCertainAmount.add(sumDefLoss);
		}
        vPriceReqVo.setThirdVehicleCertainAmount(String.valueOf(thirdVehicleCertainAmount));//??????????????????????????????
        vPriceReqVo.setCertainAmount(String.valueOf(sumDefLoss));//?????????????????????????????????
        vPriceReqVo.setCertainSumAmount(String.valueOf(lossPropMainVo.getSumDefloss().add(lossPropMainVo.getDefRescueFee())));//??????????????????
        BigDecimal ModificaCertainAmount = new BigDecimal("0");
        PrpLdlossPropMainHisVo lossMainHisvo = propLossService.findPropHisByPropMainId(lossPropMainVo.getId());
        if(lossMainHisvo != null){
        	ModificaCertainAmount=lossMainHisvo.getSumDefLoss();
        }
        vPriceReqVo.setModificaCertainAmount(String.valueOf(ModificaCertainAmount));//?????????????????????
        vPriceReqVo.setRescueFeeAmount(String.valueOf(lossPropMainVo.getDefRescueFee()));//???????????????
        
        List<PrpLClaimDeductVo> claimDeductVoList = new ArrayList<PrpLClaimDeductVo>();
        String deductCondCode = "";
        claimDeductVoList = registQueryService.findClaimDeductVoByRegistNo(registNo);// ???????????????????????????????????????
        if(claimDeductVoList != null && claimDeductVoList.size() > 0){
            for(PrpLClaimDeductVo vo : claimDeductVoList){
                if("1".equals(vo.getIsCheck())){
                    deductCondCode = deductCondCode + "," +vo.getDeductCondCode();
                }
            }
            if(deductCondCode.length()>0){
                deductCondCode = deductCondCode.substring(1,deductCondCode.length());//?????????????????????
            }
        }
        vPriceReqVo.setDeductCondCode(deductCondCode);//????????????
        vPriceReqVo.setDamageCode(prpLRegistVo.getDamageCode());//????????????
        
        String lossPartyName="";
        String licenseNo="";
        PrpLWfTaskVo prpLWfTaskVo=wfFlowService.findPrpLWfTaskQueryByTaskId(taskId);
        String handlerId=prpLWfTaskVo.getHandlerIdKey();
        PrpLdlossPropMainVo vo=propLossService.findPropMainVoById(Long.valueOf(handlerId));
        lossPartyName=vo.getLossType();
        
        licenseNo=lossPropMainVo.getLicense();//?????????????????????
        
    	vPriceReqVo.setLossPartyName(Integer.parseInt(lossPartyName)>1?"2":lossPartyName);//????????? 0  ??????/????????????  1  ?????????  2  ?????????
    	
    	PrpLAutoVerifyVo prpLAutoVerifyVo = new PrpLAutoVerifyVo();
    	prpLAutoVerifyVo.setUserCode(userVo.getUserCode());
        prpLAutoVerifyVo.setNode(triggerNode);
    	boolean isSysAuthorization = deflossHandleService.isAutoVerifyUser(prpLAutoVerifyVo);   
        vPriceReqVo.setSysAuthorizationFlag(isSysAuthorization?"1":"0");//??????????????????
        
        vPriceReqVo.setEmployeeId(userVo.getUserCode());//????????????
        
        
		if(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossPropMainVo.getDeflossSourceFlag())){
			List<PrpLdlossPropMainVo> prpLdlossPropMainVos = propLossService.findLossPropBySerialNo(registNo, lossPropMainVo.getSerialNo());
	    	for(PrpLdlossPropMainVo prpLdlossPropMainVo : prpLdlossPropMainVos){
            	if(UnderWriteFlag.MANAL_UNDERWRITE.equals(prpLdlossPropMainVo.getUnderWriteFlag())){
            		sumDefLoss = sumDefLoss
    	    				.add(DataUtils.NullToZero(prpLdlossPropMainVo.getSumDefloss()))
    	            		.add(DataUtils.NullToZero(prpLdlossPropMainVo.getDefRescueFee()))//????????????
    	            		.add(DataUtils.NullToZero(prpLdlossPropMainVo.getSumLossFee()));//??????
            	}
			}
		}

	    sumDefLoss = sumDefLoss
	    		.add(DataUtils.NullToZero(lossPropMainVo.getDefRescueFee()))//????????????
	    		.add(DataUtils.NullToZero(lossPropMainVo.getSumLossFee()));//??????
        
        vPriceReqVo.setSumAmount(String.valueOf(sumDefLoss));//???????????????
        
        vPriceReqVo.setSurveyFlag("0");//??????????????????
        vPriceReqVo.setIsFlagN("0");//??????????????????
        
        //??????????????????
        String isWhethertheloss = "0";
        List<PrpLDlossCarMainVo> prpLDlossCarMainVos = lossCarService.findLossCarMainByRegistNo(registNo); 
        if(prpLDlossCarMainVos!=null){
            for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVos){
                if("1".equals(prpLDlossCarMainVo.getIsWhethertheloss())){
                    isWhethertheloss = "1";
                    break;
                }
            }
        }
        if(!"1".equals(isWhethertheloss)){
            List<PrpLdlossPropMainVo> prpLdlossPropMainVos = propLossService.findPropMainByRegistNo(registNo);
            if(prpLdlossPropMainVos!=null){
                for(PrpLdlossPropMainVo prpLdlossPropMainVo:prpLdlossPropMainVos){
                    if("1".equals(prpLdlossPropMainVo.getIsWhethertheloss())){
                        isWhethertheloss = "1";
                        break;
                    }
                }
            }   
        }
        vPriceReqVo.setIsWhethertheloss(isWhethertheloss);//????????????
        
        vPriceReqVo.setIsNucleardamagereturn("0");//????????????????????????
        PrpLWfTaskVo upperPrpLWfTaskVo=wfFlowService.findPrpLWfTaskQueryByTaskId(prpLWfTaskVo.getUpperTaskId());
        if(WorkStatus.BACK.equals(upperPrpLWfTaskVo.getWorkStatus())){   
            vPriceReqVo.setIsNucleardamagereturn("1");//????????????????????????
        }
        
        List<Accessories> accessoriesList = new ArrayList<Accessories>();
        vPriceReqVo.setAccessoriesList(accessoriesList); ////????????????
        
        String urlStr=SpringProperties.getProperty("ILOG_SVR_URL");//??????ILOG??????????????????
        urlStr=urlStr+"CheckRuleForCarVerifyLossServlet";//??????????????????
        String requestXML=XstreamFactory.objToXml(vPriceReqVo);//??????????????????xml
        String returnXml=earlyWarnService.requestSDEW(requestXML,urlStr);//??????ILOG
        System.out.println("******ILOG????????????*******");
        System.out.println(requestXML);
        System.out.println("******ILOG????????????*******");
        System.out.println(returnXml);
        lIlogRuleResVo= XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//??????????????????vo
        
        IlogDataProcessingVo ilogDataProcessingVo =new IlogDataProcessingVo();
        ilogDataProcessingVo.setBusinessNo(vPriceReqVo.getRegistNo());//????????? ?????????
        ilogDataProcessingVo.setCompensateNo("");//????????????   
        ilogDataProcessingVo.setComCode(vPriceReqVo.getComCode());//??????????????????
        ilogDataProcessingVo.setRiskCode(vPriceReqVo.getRiskCode());//??????
        ilogDataProcessingVo.setOperateType(vPriceReqVo.getOperateType());//????????????  1?????????  2???????????????
        ilogDataProcessingVo.setRuleType(vPriceReqVo.getTaskType());//???????????? 0?????????; 1?????????
        ilogDataProcessingVo.setRuleNode("VLProp");//????????????
        ilogDataProcessingVo.setLossParty(lossPartyName);//?????????
        ilogDataProcessingVo.setLicenseNo(licenseNo);//?????????????????????
        ilogDataProcessingVo.setTriggerNode(triggerNode);//????????????  ?????????????????????????????????
        ilogDataProcessingVo.setTaskId(taskId);//??????????????????????????????ID 
        ilogDataProcessingVo.setOperatorCode(userVo.getUserCode());//????????????

        ruleReturnDataSaveService.dealILogResReturnData(lIlogRuleResVo,ilogDataProcessingVo);//????????????
        
        return lIlogRuleResVo;
      }
    
    
    
    public LIlogRuleResVo organizaVehicleLoss(PrpLDlossCarMainVo lossCarMainVo,String operateType,SysUserVo userVo,BigDecimal taskId,String triggerNode, String isSubmitHeadOffice) throws Exception{
        VPriceReqVo vPriceReqVo = new VPriceReqVo();
        LIlogRuleResVo lIlogRuleResVo = new LIlogRuleResVo();
        String registNo = lossCarMainVo.getRegistNo();
        PrpLRegistVo prpLRegistVo = registService.findRegistByRegistNo(registNo);
        List<PrpLCMainVo> prpLCMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(registNo);//??????????????????
        vPriceReqVo.setRegistNo(registNo);//?????????
        //vPriceReqVo.setCurrentNodeNo("");//??????????????????  ?????????????????????
//        if("2".equals(operateType)){//?????????????????????
//            vPriceReqVo.setCurrentNodeNo(currentNodeNo);//??????????????????  ?????????????????????
//        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 
        
        vPriceReqVo.setOperateType(operateType);//????????????1?????????   2???????????????
        vPriceReqVo.setRiskCode(lossCarMainVo.getRiskCode());
        vPriceReqVo.setComCode(lossCarMainVo.getMakeCom());
        vPriceReqVo.setTaskType("0");//0?????????  1?????????
        vPriceReqVo.setRepairFactoryType(lossCarMainVo.getRepairFactoryType());//???????????????
        if(isSubmitHeadOffice != null){
            vPriceReqVo.setExistHeadOffice(isSubmitHeadOffice);
        } else{
        	vPriceReqVo.setExistHeadOffice(CodeConstants.CommonConst.FALSE);
        }
        String damageTime = format.format(prpLRegistVo.getDamageTime());

        vPriceReqVo.setDamageDate(damageTime);//????????????
        damageTime = damageTime.substring(11,damageTime.length());
        String[] damageHourMinute = damageTime.split(":");
        String damageHour ="";
        String damageMinute="";
        if(damageHourMinute.length > 1){
            damageHour = damageHourMinute[0];
            damageMinute = damageHourMinute[1];
        }
        vPriceReqVo.setDamageHour(damageHour);//????????????
        vPriceReqVo.setDamageMinute(damageMinute);//????????????
        
        if(prpLCMainVoList!=null && prpLCMainVoList.size()>0){
        	if(prpLCMainVoList.get(0).getPrpCItemCars().get(0).getEnrollDate() != null){
            	vPriceReqVo.setEnrollDate(format1.format(prpLCMainVoList.get(0).getPrpCItemCars().get(0).getEnrollDate())); //???????????? 
            }
        	vPriceReqVo.setCoinsFlag("0".equals(prpLCMainVoList.get(0).getCoinsFlag()) ? "0":"1");//?????????????????????
        }
        
        List<PrpLDlossCarCompVo> dlossCarCompVoList = lossCarMainVo.getPrpLDlossCarComps();//??????????????????
        //??????
        int jyStandardFittingsNum = 0;
        BigDecimal jystandardSingleFittingsHighAmount = new BigDecimal(0);
        BigDecimal jystandardFittingsSumAmount = new BigDecimal(0);
        //?????????
        int jyNonstandardFittingsNum = 0;
        BigDecimal jyNonstandardSingleFittingsHighAmount = new BigDecimal(0);
        BigDecimal jyNonstandardFittingsSumAmount = new BigDecimal(0);
        //???
        BigDecimal jyFittingsSumAmount = new BigDecimal(0);
        int replaceNum = 0;//???????????????????????????
        List<Accessories> accessoriesList = new ArrayList<Accessories>();
        for(PrpLDlossCarCompVo vo : dlossCarCompVoList){
            Accessories accessories = new Accessories();
            // 0-????????????  1-??????????????? 2-????????????
            if("2".equals(vo.getSelfConfigFlag())){//??????
                jyStandardFittingsNum += vo.getQuantity();
                if(jystandardSingleFittingsHighAmount.compareTo(vo.getMaterialFee())<0){//???????????????
                    jystandardSingleFittingsHighAmount = vo.getMaterialFee();
                }
                jystandardFittingsSumAmount = jystandardFittingsSumAmount.add(vo.getMaterialFee());//??????
                accessories.setIsStandard("1");//??????????????????
            }else{
                jyNonstandardFittingsNum += vo.getQuantity();
                if(jyNonstandardSingleFittingsHighAmount.compareTo(vo.getMaterialFee())<0){//???????????????
                    jyNonstandardSingleFittingsHighAmount = vo.getMaterialFee();
                }
                jyNonstandardFittingsSumAmount = jyNonstandardFittingsSumAmount.add(vo.getMaterialFee());//??????
                accessories.setIsStandard("0");//??????????????????
            }
            if(vo.getReplaceNum()!=null){
                replaceNum += vo.getReplaceNum();
            }     
            accessories.setCompCode(vo.getCompCode());//????????????
            accessories.setCompName(vo.getCompName());//????????????
            if(vo.getChgRefPrice()!=null){
                accessories.setChgrefPrice(String.valueOf(vo.getChgRefPrice()));//????????????
            }
            if(vo.getSumDefLoss()!=null){
                accessories.setSumdefLoss(String.valueOf(vo.getSumDefLoss()));//????????????
            }
            if(vo.getChgLocPrice()!=null){
                accessories.setChglocPrice(String.valueOf(vo.getChgLocPrice()));//????????????

            }
            if(vo.getQuantity()!=null){
                accessories.setQuantity(String.valueOf(vo.getQuantity()));//??????
            }
            accessoriesList.add(accessories);
        }
        
        vPriceReqVo.setAccessoriesList(accessoriesList); ////????????????

        
        jyFittingsSumAmount = jystandardFittingsSumAmount.add(jyNonstandardFittingsSumAmount);
        //????????????
        vPriceReqVo.setJyStandardFittingsNum(String.valueOf(jyStandardFittingsNum));//????????????????????????????????????
        vPriceReqVo.setJystandardSingleFittingsHighAmount(String.valueOf(jystandardSingleFittingsHighAmount));//????????????????????????????????????????????????
        vPriceReqVo.setJyNonstandardFittingsNum(String.valueOf(jyNonstandardFittingsNum));//???????????????????????????????????????
        vPriceReqVo.setJyNonstandardSingleFittingsHighAmount(String.valueOf(jyNonstandardSingleFittingsHighAmount));//???????????????????????????????????????????????????
        vPriceReqVo.setJystandardFittingsSumAmount(String.valueOf(jystandardFittingsSumAmount));//?????????????????????????????????????????????
        vPriceReqVo.setJyNonstandardFittingsSumAmount(String.valueOf(jyNonstandardFittingsSumAmount));//????????????????????????????????????????????????
        vPriceReqVo.setJyFittingsSumAmount(String.valueOf(jyFittingsSumAmount)); //???????????????????????????????????????
        
        PrpLSubrogationMainVo prpLSubrogationMainVo = subrogationService.find(registNo);
        String subrogationFlag = "";
        if(prpLSubrogationMainVo!=null){
            subrogationFlag = prpLSubrogationMainVo.getSubrogationFlag();
        }else {
            PrpLCheckVo prpLCheckVo = checkTaskService.findCheckVoByRegistNo(registNo);
            if(prpLCheckVo!=null){
                subrogationFlag = prpLCheckVo.getIsSubRogation();
            }
        }
        vPriceReqVo.setSubrogationFlag(subrogationFlag);//??????????????????  0???????????????  1????????????
       
        String coverageFlag = "0";//?????????????????????????????????
        String carAmount = "";
        for(PrpLCMainVo vo : prpLCMainVoList){
            if("1".equals(coverageFlag)){
                break;
            }else{
                List<PrpLCItemKindVo> prpLCItemKindVoList = vo.getPrpCItemKinds();
                if(prpLCItemKindVoList != null && prpLCItemKindVoList.size() > 0){
                    for(PrpLCItemKindVo itemKindVo : prpLCItemKindVoList){
                        if("A".equals(itemKindVo.getKindCode())||"A1".equals(itemKindVo.getKindCode())){
                            coverageFlag = "1";
                            carAmount = String.valueOf(itemKindVo.getAmount());
                            break;
                        }
                    }
                } 
            }
        }
        vPriceReqVo.setCoverageFlag(coverageFlag);
        //?????????????????????
        if("1".equals(lossCarMainVo.getDeflossCarType())){
            if(lossCarMainVo.getSumLossFee() != null){
                vPriceReqVo.setMarkCarAmount(String.valueOf(lossCarMainVo.getSumLossFee()));
            }
        }else{
            vPriceReqVo.setMarkCarAmount("");
        }
        vPriceReqVo.setCarAmount(carAmount);//???????????????
        if(prpLCMainVoList != null && prpLCMainVoList.size() > 1 ){
            for(PrpLCMainVo vo : prpLCMainVoList){
                if(!Risk.isDQZ(vo.getRiskCode())){//??????
                    List<PrpLCMainVo> vos = registTmpService.findAreadictByPolicyNo(vo.getPolicyNo());
                    PrpLCMainVo prpLCMainVo = vos.get(0);
                    vPriceReqVo.setStartDate(format1.format(prpLCMainVo.getStartDate()));
                    if(prpLCMainVo.getStartHour()!=null){
                    	vPriceReqVo.setStartHour(prpLCMainVo.getStartHour().toString());
                    }else{
                    	vPriceReqVo.setStartHour("0");
                    }
                    if(prpLCMainVo.getStartMinute()!=null && !"".equals(prpLCMainVo.getStartMinute())){
                    	vPriceReqVo.setStartMinute(prpLCMainVo.getStartMinute().toString());
                    }else{
                    	vPriceReqVo.setStartMinute("0");
                    }
                    vPriceReqVo.setEndDate(format1.format(prpLCMainVo.getEndDate()));
                    if(prpLCMainVo.getEndHour() != null){
                    	vPriceReqVo.setEndHour(prpLCMainVo.getEndHour().toString());
                    }else{
                    	vPriceReqVo.setEndHour("0");
                    }
                    if(prpLCMainVo.getEndMinute() != null){
                    	vPriceReqVo.setEndMinute(prpLCMainVo.getEndMinute().toString());
                    }else{
                    	vPriceReqVo.setEndMinute("0");
                    }
                    PrpLCItemCarVo prpLCItemCarVo = prpLCMainVo.getPrpCItemCars().get(0);
                    if("1".equals(prpLCItemCarVo.getOtherNature().substring(4,5))){//??????
                        vPriceReqVo.setChgOwnerFlag("1");
                    }else{
                        vPriceReqVo.setChgOwnerFlag("0");
                    }
                    vPriceReqVo.setUseKindCode(prpLCItemCarVo.getUseKindCode());
                }
            }
        }else if(prpLCMainVoList != null){
            List<PrpLCMainVo> vos = registTmpService.findAreadictByPolicyNo(prpLCMainVoList.get(0).getPolicyNo());
            PrpLCMainVo prpLCMainVo = vos.get(0);
            vPriceReqVo.setStartDate(format1.format(prpLCMainVo.getStartDate()));
            if(prpLCMainVo.getStartHour()!=null){
            	vPriceReqVo.setStartHour(prpLCMainVo.getStartHour().toString());
            }else{
            	vPriceReqVo.setStartHour("0");
            }
            if(prpLCMainVo.getStartMinute()!=null && !"".equals(prpLCMainVo.getStartMinute())){
            	vPriceReqVo.setStartMinute(prpLCMainVo.getStartMinute().toString());
            }else{
            	vPriceReqVo.setStartMinute("0");
            }
            vPriceReqVo.setEndDate(format1.format(prpLCMainVo.getEndDate()));
            if(prpLCMainVo.getEndHour() != null){
            	vPriceReqVo.setEndHour(prpLCMainVo.getEndHour().toString());
            }else{
            	vPriceReqVo.setEndHour("0");
            }
            if(prpLCMainVo.getEndMinute() != null){
            	vPriceReqVo.setEndMinute(prpLCMainVo.getEndMinute().toString());
            }else{
            	vPriceReqVo.setEndMinute("0");
            }
            PrpLCItemCarVo prpLCItemCarVo = prpLCMainVo.getPrpCItemCars().get(0);
             if("1".equals(prpLCItemCarVo.getOtherNature().substring(4,5))){//??????
                 vPriceReqVo.setChgOwnerFlag("1");
             }else{
                 vPriceReqVo.setChgOwnerFlag("0");
             }
             vPriceReqVo.setUseKindCode(prpLCItemCarVo.getUseKindCode());
        }
        
        //????????????   
        String reportTime = format.format(prpLRegistVo.getDamageTime());
        vPriceReqVo.setReportDate(reportTime);
        reportTime=reportTime.substring(11,reportTime.length());
        String[] reportHourMinute=reportTime.split(":");
        String reportHour="";
        String reportMinute="";
        if(reportHourMinute.length>1){
        	reportHour=reportHourMinute[0];
        	reportMinute=reportHourMinute[1];
        }
        vPriceReqVo.setReportHour(reportHour);
        vPriceReqVo.setReportMinute(reportMinute);

        //??????????????????
        Map<String, String> registRiskInfoMap = new HashMap<String, String>();
        if (!StringUtils.isEmpty(registNo)) {
            registRiskInfoMap = registService.findRegistRiskInfoByRegistNo(registNo);
        }
        String ciDangerNum = registRiskInfoMap.get("CI-DangerNum");
        if(StringUtils.isNotBlank(ciDangerNum)){
            vPriceReqVo.setJqDamagTime(ciDangerNum);
        }else{
            vPriceReqVo.setJqDamagTime("");
        }
        String ciDangerInSum = registRiskInfoMap.get("CI-DangerInSum");
        if(StringUtils.isNotBlank(ciDangerInSum)){
            vPriceReqVo.setJqSevenDaysDamagTime(ciDangerInSum);
        }else{
            vPriceReqVo.setJqSevenDaysDamagTime("");
        }

        String biDangerNum = registRiskInfoMap.get("BI-DangerNum");
        if(StringUtils.isNotBlank(biDangerNum)){
            vPriceReqVo.setSyDamagtime(biDangerNum);
        }else{
            vPriceReqVo.setSyDamagtime("");
        }
        String biDangerInSum = registRiskInfoMap.get("BI-DangerInSum");
        if(StringUtils.isNotBlank(biDangerInSum)){
            vPriceReqVo.setSySevenDaysDamagTime(biDangerInSum);
        }else{
            vPriceReqVo.setSySevenDaysDamagTime("");
        }
        
        vPriceReqVo.setFittingsChangeSumNum(String.valueOf(replaceNum));//???????????????????????????           
        
        List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(registNo);
        List<PrpLdlossPropMainVo> prpLdlossPropMainVoList = propTaskService.findPropMainListByRegistNo(registNo);
        BigDecimal thirdVehicleCertainAmount = new BigDecimal(0);
        List<SingleCarLossInfo> singleCarLossInfos = new ArrayList<SingleCarLossInfo>();
        vPriceReqVo.setHandlerType("0");
        if(prpLDlossCarMainVoList!=null && prpLDlossCarMainVoList.size()>0){
            for(PrpLDlossCarMainVo vo : prpLDlossCarMainVoList){
                if(vo.getSumLossFee() != null){
                    thirdVehicleCertainAmount = thirdVehicleCertainAmount.add(vo.getSumLossFee());
                }
                
                // ????????????????????????????????????????????????????????????
            	if (vo.getSumLossFee() != null) {
            		SingleCarLossInfo singlecarlossinfo = new SingleCarLossInfo();
            		singlecarlossinfo.setSingleCarSumloss(vo.getSumLossFee().toString());
            		singleCarLossInfos.add(singlecarlossinfo);
            	}
            	
            	// ??????????????????????????????????????????????????????
            	if (vo.getIntermFlag() != null && "1".equals(vo.getIntermFlag()) && "0".equals(vPriceReqVo.getHandlerType())) {
            		vPriceReqVo.setHandlerType("1");
            	}
            }
        }
        vPriceReqVo.setSingleCarlossCarInfoList(singleCarLossInfos);

        if(prpLdlossPropMainVoList!=null && prpLdlossPropMainVoList.size()>0){
            for(PrpLdlossPropMainVo vo : prpLdlossPropMainVoList){
                if(vo.getSumDefloss() != null){
                    thirdVehicleCertainAmount = thirdVehicleCertainAmount.add(vo.getSumDefloss());
                }
            } 
        }
        BigDecimal sumLossFee = new BigDecimal("0");
        sumLossFee = NullToZero(lossCarMainVo.getSumLossFee());
        vPriceReqVo.setThirdVehicleCertainAmount(String.valueOf(thirdVehicleCertainAmount));//??????????????????????????????
        vPriceReqVo.setCertainAmount(String.valueOf(sumLossFee));//?????????????????????????????????
        vPriceReqVo.setCertainSumAmount(String.valueOf(NullToZero(lossCarMainVo.getSumLossFee()).add(NullToZero(lossCarMainVo.getSumRescueFee()))));//??????????????????
        BigDecimal modificaCertainAmount =new BigDecimal(0);
        List<PrpLDlossCarMainHisVo> lossMainHisList = deflossService.findDeflossHisByMainId(lossCarMainVo.getId());
        if(lossMainHisList != null && lossMainHisList.size() > 0){
            PrpLDlossCarMainHisVo lossCarMainHisVo = lossMainHisList.get(lossMainHisList.size()-1);
            modificaCertainAmount=lossCarMainHisVo.getSumLossFee();
        }
        vPriceReqVo.setModificaCertainAmount(String.valueOf(modificaCertainAmount));//?????????????????????
        vPriceReqVo.setRescueFeeAmount(String.valueOf(lossCarMainVo.getSumRescueFee()));//???????????????
        
        List<PrpLClaimDeductVo> claimDeductVoList = new ArrayList<PrpLClaimDeductVo>();
        String deductCondCode = "";
        claimDeductVoList = registQueryService.findClaimDeductVoByRegistNo(registNo);// ???????????????????????????????????????
        if(claimDeductVoList != null && claimDeductVoList.size() > 0){
            for(PrpLClaimDeductVo vo : claimDeductVoList){
                if("1".equals(vo.getIsCheck())){
                    deductCondCode = deductCondCode + "," +vo.getDeductCondCode();
                }
            }
            if(deductCondCode.length()>0){
                deductCondCode = deductCondCode.substring(1,deductCondCode.length());//?????????????????????
            }
        }
        
        vPriceReqVo.setDeductCondCode(deductCondCode);//????????????
        vPriceReqVo.setDamageCode(prpLRegistVo.getDamageCode());//????????????
        
        String lossPartyName="";
        String licenseNo="";
        PrpLWfTaskVo prpLWfTaskVo=wfFlowService.findPrpLWfTaskQueryByTaskId(taskId);
        String handlerId=prpLWfTaskVo.getHandlerIdKey();
        PrpLDlossCarMainVo vo=lossCarService.findLossCarMainById(Long.valueOf(handlerId));
        lossPartyName=vo.getDeflossCarType();
        licenseNo=vo.getLicenseNo();//?????????????????????
        
        vPriceReqVo.setLossPartyName(Integer.parseInt(lossPartyName)>1?"2":lossPartyName);//????????? 0  ??????/????????????  1  ?????????  2  ?????????

        
        PrpLAutoVerifyVo prpLAutoVerifyVo = new PrpLAutoVerifyVo();
        if(FlowNode.DLCar.name().equals(triggerNode)){
    		prpLAutoVerifyVo.setUserCode(userVo.getUserCode());
            prpLAutoVerifyVo.setNode(triggerNode);
    	}else{
    		PrpLWfTaskVo dLossTaskVo = findDLossTaskVo(prpLWfTaskVo);
    		prpLAutoVerifyVo.setUserCode(dLossTaskVo.getHandlerUser());
            prpLAutoVerifyVo.setNode(dLossTaskVo.getSubNodeCode());
    	}
        boolean isSysAuthorization = deflossHandleService.isAutoVerifyUser(prpLAutoVerifyVo);   
        vPriceReqVo.setSysAuthorizationFlag(isSysAuthorization?"1":"0");//??????????????????
        
        vPriceReqVo.setEmployeeId(userVo.getUserCode());//????????????
        
		if(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossCarMainVo.getDeflossSourceFlag())){
	        List<PrpLDlossCarMainVo> prpLDlossCarMains = deflossService.findLossCarMainBySerialNo(lossCarMainVo.getRegistNo(),lossCarMainVo.getSerialNo());
        	for(PrpLDlossCarMainVo prpLDlossCarMainVo : prpLDlossCarMains){
            	if(UnderWriteFlag.MANAL_UNDERWRITE.equals(prpLDlossCarMainVo.getUnderWriteFlag())){
            		
            		sumLossFee = sumLossFee
            				.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumLossFee()))
    						.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumChargeFee()))//??????
    		        		.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumSubRiskFee()))//?????????
    						.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumRescueFee()))//?????????
    						.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumOutFee()));//??????
    	        }
            }     
		}			
        
		if(CodeConstants.JyFlag.NOIN.equals(lossCarMainVo.getFlag())){//??????????????????????????????????????? ??????????????????
			sumLossFee = sumLossFee
					.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))
					.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()));
		}else{
			if(lossCarMainVo.getSumVeripLoss()!=null 
					&& (CodeConstants.CetainLossType.DEFLOSS_REPAIR.equals(lossCarMainVo.getCetainLossType())
					|| CodeConstants.CetainLossType.DEFLOSS_AGREE.equals(lossCarMainVo.getCetainLossType()))){
				sumLossFee = sumLossFee
						.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))//??????
		        		.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()))//?????????
						.add(DataUtils.NullToZero(lossCarMainVo.getSumRescueFee()))//?????????
						.add(DataUtils.NullToZero(lossCarMainVo.getSumOutFee()));//??????
			}else{
				sumLossFee = sumLossFee
						.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))
						.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()));
			}
		}		
        
        vPriceReqVo.setSumAmount(String.valueOf(sumLossFee));//???????????????
        
        vPriceReqVo.setSurveyFlag("0");//??????????????????
        vPriceReqVo.setIsFlagN("0");//??????????????????
        
        
        
        //??????????????????
        String isWhethertheloss = "0";
        List<PrpLDlossCarMainVo> prpLDlossCarMainVos = lossCarService.findLossCarMainByRegistNo(registNo);
        if(prpLDlossCarMainVos!=null){
            for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVos){
                if("1".equals(prpLDlossCarMainVo.getIsWhethertheloss())){
                    isWhethertheloss = "1";
                    break;
                }
            }
        }
        if(!"1".equals(isWhethertheloss)){
            List<PrpLdlossPropMainVo> prpLdlossPropMainVos = propLossService.findPropMainByRegistNo(registNo);
            if(prpLdlossPropMainVos!=null){
                for(PrpLdlossPropMainVo prpLdlossPropMainVo:prpLdlossPropMainVos){
                    if("1".equals(prpLdlossPropMainVo.getIsWhethertheloss())){
                        isWhethertheloss = "1";
                        break;
                    }
                }
            }  
        }        
        vPriceReqVo.setIsWhethertheloss(isWhethertheloss);//????????????
        

        vPriceReqVo.setIsNucleardamagereturn("0");//????????????????????????
        PrpLWfTaskVo upperPrpLWfTaskVo=wfFlowService.findPrpLWfTaskQueryByTaskId(prpLWfTaskVo.getUpperTaskId());
        if(WorkStatus.BACK.equals(upperPrpLWfTaskVo.getWorkStatus())){   
            vPriceReqVo.setIsNucleardamagereturn("1");//????????????????????????
        }
        
        
        String urlStr=SpringProperties.getProperty("ILOG_SVR_URL");//??????ILOG??????????????????
        urlStr=urlStr+"CheckRuleForCarVerifyLossServlet";//??????????????????
        String requestXML=XstreamFactory.objToXml(vPriceReqVo);//??????????????????xml
        String returnXml=earlyWarnService.requestSDEW(requestXML,urlStr);//??????ILOG
        System.out.println("******ILOG????????????*******");
        System.out.println(requestXML);
        System.out.println("******ILOG????????????*******");
        System.out.println(returnXml);
        lIlogRuleResVo= XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//??????????????????vo
        
        IlogDataProcessingVo ilogDataProcessingVo =new IlogDataProcessingVo();
        ilogDataProcessingVo.setBusinessNo(vPriceReqVo.getRegistNo());//????????? ?????????
        ilogDataProcessingVo.setCompensateNo("");//????????????   
        ilogDataProcessingVo.setComCode(vPriceReqVo.getComCode());//??????????????????
        ilogDataProcessingVo.setRiskCode(vPriceReqVo.getRiskCode());//??????
        ilogDataProcessingVo.setOperateType(vPriceReqVo.getOperateType());//????????????  1?????????  2???????????????
        ilogDataProcessingVo.setRuleType(vPriceReqVo.getTaskType());//???????????? 0?????????; 1?????????
        ilogDataProcessingVo.setRuleNode("VLCar");//????????????
        ilogDataProcessingVo.setLossParty(vPriceReqVo.getLossPartyName());//?????????
        ilogDataProcessingVo.setLicenseNo(licenseNo);//?????????????????????
        ilogDataProcessingVo.setTriggerNode(triggerNode);//????????????  ?????????????????????????????????
        ilogDataProcessingVo.setTaskId(taskId);//??????????????????????????????ID 
        ilogDataProcessingVo.setOperatorCode(userVo.getUserCode());//????????????
        
        ruleReturnDataSaveService.dealILogResReturnData(lIlogRuleResVo,ilogDataProcessingVo);//????????????
                
        return lIlogRuleResVo;
      }
    
    /**
     * ??????????????????????????????????????????????????????
     * @param taskVo
     * @return
     */
    public PrpLWfTaskVo findDLossTaskVo(PrpLWfTaskVo taskVo){
    	PrpLWfTaskVo resultTaskVo=wfFlowService.findPrpLWfTaskQueryByTaskId(taskVo.getUpperTaskId());
    	if(!FlowNode.DLCar.name().equals(resultTaskVo.getSubNodeCode()) &&
    			!FlowNode.DLCarMod.name().equals(resultTaskVo.getSubNodeCode()) &&
    			!FlowNode.DLCarAdd.name().equals(resultTaskVo.getSubNodeCode()) &&
    			!FlowNode.DLProp.name().equals(resultTaskVo.getSubNodeCode()) &&
    			!FlowNode.DLPropMod.name().equals(resultTaskVo.getSubNodeCode()) &&
    			!FlowNode.DLPropAdd.name().equals(resultTaskVo.getSubNodeCode())){
    		resultTaskVo = findDLossTaskVo(resultTaskVo);
    	}
    	return resultTaskVo;
    }
    
    private static BigDecimal NullToZero(BigDecimal strNum) {
		if(strNum==null){
			return BigDecimal.ZERO;
		}
		return strNum;
	}



	@Override
	public LIlogRuleResVo organizaOldVehicleLoss(PrpLDlossCarMainVo lossCarMainVo,String operateType) throws Exception {
        VPriceReqVo vPriceReqVo = new VPriceReqVo();
        LIlogRuleResVo lIlogRuleResVo = new LIlogRuleResVo();
        String registNo = lossCarMainVo.getRegistNo();
        List<PrpLCMainVo> prpLCMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(registNo);//??????????????????
        vPriceReqVo.setRegistNo(registNo);//?????????
        vPriceReqVo.setOperateType(operateType);//????????????1?????????   2???????????????
        vPriceReqVo.setRiskCode(lossCarMainVo.getRiskCode());
        vPriceReqVo.setComCode(lossCarMainVo.getMakeCom());
        vPriceReqVo.setTaskType("0");//0?????????  1?????????
        
        
        List<PrpLDlossCarCompVo> dlossCarCompVoList = lossCarMainVo.getPrpLDlossCarComps();//??????????????????
        //??????
        BigDecimal jystandardSingleFittingsHighAmount = new BigDecimal(0);
        BigDecimal jystandardFittingsSumAmount = new BigDecimal(0);
        //?????????
        BigDecimal jyNonstandardSingleFittingsHighAmount = new BigDecimal(0);
        BigDecimal jyNonstandardFittingsSumAmount = new BigDecimal(0);
        //???
        BigDecimal jyFittingsSumAmount = new BigDecimal(0);
        int replaceNum = 0;//???????????????????????????
        List<Accessories> accessoriesList = new ArrayList<Accessories>();
        for(PrpLDlossCarCompVo vo : dlossCarCompVoList){
            Accessories accessories = new Accessories();
            // 0-????????????  1-??????????????? 2-????????????
            if("2".equals(vo.getSelfConfigFlag())){//??????
                if(jystandardSingleFittingsHighAmount.compareTo(DataUtils.NullToZero(vo.getMaterialFee()))<0){//???????????????
                    jystandardSingleFittingsHighAmount = DataUtils.NullToZero(vo.getMaterialFee());
                }
                jystandardFittingsSumAmount = jystandardFittingsSumAmount.add(DataUtils.NullToZero(vo.getMaterialFee()));//??????
                accessories.setIsStandard("1");//??????????????????
            }else{
                if(jyNonstandardSingleFittingsHighAmount.compareTo(DataUtils.NullToZero(vo.getMaterialFee()))<0){//???????????????
                    jyNonstandardSingleFittingsHighAmount = DataUtils.NullToZero(vo.getMaterialFee());
                }
                jyNonstandardFittingsSumAmount = jyNonstandardFittingsSumAmount.add(DataUtils.NullToZero(vo.getMaterialFee()));//??????
                accessories.setIsStandard("0");//??????????????????
            }
            if(vo.getReplaceNum()!=null){
                replaceNum += vo.getReplaceNum();
            }     
            accessories.setCompCode(vo.getCompCode());//????????????
            accessories.setCompName(vo.getCompName());//????????????
            if(vo.getChgRefPrice()!=null){
                accessories.setChgrefPrice(String.valueOf(vo.getChgRefPrice()));//????????????
            }
            if(vo.getSumDefLoss()!=null){
                accessories.setSumdefLoss(String.valueOf(vo.getSumDefLoss()));//????????????
            }
            if(vo.getChgLocPrice()!=null){
                accessories.setChglocPrice(String.valueOf(vo.getChgLocPrice()));//????????????

            }
            if(vo.getQuantity()!=null){
                accessories.setQuantity(String.valueOf(vo.getQuantity()));//??????
            }
            accessoriesList.add(accessories);
        }
        
        vPriceReqVo.setAccessoriesList(accessoriesList); ////????????????

        
        jyFittingsSumAmount = jystandardFittingsSumAmount.add(jyNonstandardFittingsSumAmount);
        //????????????
        vPriceReqVo.setJystandardSingleFittingsHighAmount(String.valueOf(jystandardSingleFittingsHighAmount));//????????????????????????????????????????????????
        vPriceReqVo.setJyNonstandardSingleFittingsHighAmount(String.valueOf(jyNonstandardSingleFittingsHighAmount));//???????????????????????????????????????????????????
        vPriceReqVo.setJystandardFittingsSumAmount(String.valueOf(jystandardFittingsSumAmount));//?????????????????????????????????????????????
        vPriceReqVo.setJyNonstandardFittingsSumAmount(String.valueOf(jyNonstandardFittingsSumAmount));//????????????????????????????????????????????????
        vPriceReqVo.setJyFittingsSumAmount(String.valueOf(jyFittingsSumAmount)); //???????????????????????????????????????
        
        String coverageFlag = "0";//?????????????????????????????????
        String carAmount = "";
        for(PrpLCMainVo vo : prpLCMainVoList){
            if("1".equals(coverageFlag)){
                break;
            }else{
                List<PrpLCItemKindVo> prpLCItemKindVoList = vo.getPrpCItemKinds();
                if(prpLCItemKindVoList != null && prpLCItemKindVoList.size() > 0){
                    for(PrpLCItemKindVo itemKindVo : prpLCItemKindVoList){
                        if("A".equals(itemKindVo.getKindCode())||"A1".equals(itemKindVo.getKindCode())){
                            coverageFlag = "1";
                            carAmount = String.valueOf(itemKindVo.getAmount());
                            break;
                        }
                    }
                } 
            }
        }
        vPriceReqVo.setCoverageFlag(coverageFlag);
        //?????????????????????
        if("1".equals(lossCarMainVo.getDeflossCarType())){
            if(lossCarMainVo.getSumLossFee() != null){
                vPriceReqVo.setMarkCarAmount(String.valueOf(lossCarMainVo.getSumLossFee()));
            }
        }else{
            vPriceReqVo.setMarkCarAmount("");
        }
        vPriceReqVo.setCarAmount(carAmount);//???????????????
        vPriceReqVo.setFittingsChangeSumNum(String.valueOf(replaceNum));//??????????????????????????? 
        List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(registNo);
        List<PrpLdlossPropMainVo> prpLdlossPropMainVoList = propTaskService.findPropMainListByRegistNo(registNo);
        BigDecimal thirdVehicleCertainAmount = new BigDecimal(0);
        if(prpLDlossCarMainVoList!=null && prpLDlossCarMainVoList.size()>0){
            for(PrpLDlossCarMainVo vo : prpLDlossCarMainVoList){
                if(vo.getSumLossFee() != null){
                    thirdVehicleCertainAmount = thirdVehicleCertainAmount.add(vo.getSumLossFee());
                }
            }
        }

        if(prpLdlossPropMainVoList!=null && prpLdlossPropMainVoList.size()>0){
            for(PrpLdlossPropMainVo vo : prpLdlossPropMainVoList){
                if(vo.getSumDefloss() != null){
                    thirdVehicleCertainAmount = thirdVehicleCertainAmount.add(vo.getSumDefloss());
                }
            } 
        }
        BigDecimal sumLossFee = new BigDecimal("0");
        sumLossFee = NullToZero(lossCarMainVo.getSumLossFee());
        vPriceReqVo.setThirdVehicleCertainAmount(String.valueOf(thirdVehicleCertainAmount));//??????????????????????????????
        vPriceReqVo.setCertainAmount(String.valueOf(sumLossFee));//?????????????????????????????????
        vPriceReqVo.setCertainSumAmount(String.valueOf(NullToZero(lossCarMainVo.getSumLossFee()).add(NullToZero(lossCarMainVo.getSumRescueFee()))));//??????????????????
        BigDecimal modificaCertainAmount =new BigDecimal(0);
        List<PrpLDlossCarMainHisVo> lossMainHisList = deflossService.findDeflossHisByMainId(lossCarMainVo.getId());
        if(lossMainHisList != null && lossMainHisList.size() > 0){
            PrpLDlossCarMainHisVo lossCarMainHisVo = lossMainHisList.get(lossMainHisList.size()-1);
            modificaCertainAmount=DataUtils.NullToZero(lossCarMainHisVo.getSumLossFee());
        }
        vPriceReqVo.setModificaCertainAmount(String.valueOf(modificaCertainAmount));//?????????????????????
        vPriceReqVo.setRescueFeeAmount(String.valueOf(DataUtils.NullToZero(lossCarMainVo.getSumRescueFee())));//???????????????
        
		if(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossCarMainVo.getDeflossSourceFlag())){
	        List<PrpLDlossCarMainVo> prpLDlossCarMains = deflossService.findLossCarMainBySerialNo(lossCarMainVo.getRegistNo(),lossCarMainVo.getSerialNo());
        	for(PrpLDlossCarMainVo prpLDlossCarMainVo : prpLDlossCarMains){
            	if(UnderWriteFlag.MANAL_UNDERWRITE.equals(prpLDlossCarMainVo.getUnderWriteFlag())){
            		
            		sumLossFee = sumLossFee
            				.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumLossFee()))
    						.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumChargeFee()))//??????
    		        		.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumSubRiskFee()))//?????????
    						.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumRescueFee()))//?????????
    						.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumOutFee()));//??????
    	        }
            }     
		}			
        
		if(CodeConstants.JyFlag.NOIN.equals(lossCarMainVo.getFlag())){//??????????????????????????????????????? ??????????????????
			sumLossFee = sumLossFee
					.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))
					.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()));
		}else{
			if(lossCarMainVo.getSumVeripLoss()!=null 
					&& (CodeConstants.CetainLossType.DEFLOSS_REPAIR.equals(lossCarMainVo.getCetainLossType())
					|| CodeConstants.CetainLossType.DEFLOSS_AGREE.equals(lossCarMainVo.getCetainLossType()))){
				sumLossFee = sumLossFee
						.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))//??????
		        		.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()))//?????????
						.add(DataUtils.NullToZero(lossCarMainVo.getSumRescueFee()))//?????????
						.add(DataUtils.NullToZero(lossCarMainVo.getSumOutFee()));//??????
			}else{
				sumLossFee = sumLossFee
						.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))
						.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()));
			}
		}		
        
        vPriceReqVo.setSumAmount(String.valueOf(sumLossFee));//???????????????
        
        String urlStr=SpringProperties.getProperty("ILOG_SVR_URL");//??????ILOG??????????????????
        urlStr=urlStr+"CheckRuleForCarVerifyLossServlet";//??????????????????
        String requestXML=XstreamFactory.objToXml(vPriceReqVo);//??????????????????xml
        String returnXml=earlyWarnService.requestSDEW(requestXML,urlStr);//??????ILOG
        logger.info("******ILOG??????????????????*******");
        logger.info(requestXML);
        logger.info("******ILOG??????????????????*******");
        logger.info(returnXml);
        lIlogRuleResVo= XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//??????????????????vo
        
        return lIlogRuleResVo;
      }



	@Override
	public LIlogRuleResVo organizaOldOrganizaVprice(PrpLDlossCarMainVo lossCarMainVo,String operateType) throws Exception {
        VPriceReqVo vPriceReqVo = new VPriceReqVo();
        LIlogRuleResVo lIlogRuleResVo = new LIlogRuleResVo();
        String registNo = lossCarMainVo.getRegistNo();
        List<PrpLCMainVo> prpLCMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(registNo);
        vPriceReqVo.setRegistNo(registNo);
        vPriceReqVo.setOperateType(operateType);//????????????
        vPriceReqVo.setRiskCode(lossCarMainVo.getRiskCode());
        vPriceReqVo.setComCode(lossCarMainVo.getMakeCom());
        vPriceReqVo.setTaskType("0");
        List<PrpLDlossCarCompVo> dlossCarCompVoList = lossCarMainVo.getPrpLDlossCarComps();
        //??????
        BigDecimal jystandardSingleFittingsHighAmount = new BigDecimal(0);
        BigDecimal jystandardFittingsSumAmount = new BigDecimal(0);
        //?????????
        BigDecimal jyNonstandardSingleFittingsHighAmount = new BigDecimal(0);
        BigDecimal jyNonstandardFittingsSumAmount = new BigDecimal(0);
        //???
        BigDecimal jyFittingsSumAmount = new BigDecimal(0);
        int replaceNum = 0;//???????????????????????????
        List<Accessories> accessoriesList = new ArrayList<Accessories>();
        for(PrpLDlossCarCompVo vo : dlossCarCompVoList){
            Accessories accessories = new Accessories();
            accessories.setIsStandard("0");
            // 0-????????????  1-??????????????? 2-????????????
            if("2".equals(vo.getSelfConfigFlag())){//??????
                if(jystandardSingleFittingsHighAmount.compareTo(DataUtils.NullToZero(vo.getMaterialFee()))<0){//???????????????
                    jystandardSingleFittingsHighAmount = DataUtils.NullToZero(vo.getMaterialFee());
                }
                jystandardFittingsSumAmount = jystandardFittingsSumAmount.add(DataUtils.NullToZero(vo.getMaterialFee()));//??????
                accessories.setIsStandard("1");
            }else{
                if(jyNonstandardSingleFittingsHighAmount.compareTo(DataUtils.NullToZero(vo.getMaterialFee()))<0){//???????????????
                    jyNonstandardSingleFittingsHighAmount = DataUtils.NullToZero(vo.getMaterialFee());
                }
                jyNonstandardFittingsSumAmount = jyNonstandardFittingsSumAmount.add(DataUtils.NullToZero(vo.getMaterialFee()));//??????
            }
            replaceNum += vo.getReplaceNum()==null ? 0:vo.getReplaceNum();
            
            accessories.setCompCode(vo.getCompCode());
            accessories.setCompName(vo.getCompName());
            if(vo.getChgRefPrice() != null){
                accessories.setChgrefPrice(vo.getChgRefPrice().toString());
            }
            if(vo.getSumDefLoss() != null){
                accessories.setSumdefLoss(vo.getSumDefLoss().toString());
            }
            if(vo.getChgLocPrice() != null){
                accessories.setChglocPrice(vo.getChgLocPrice().toString());
            }
            
            if(vo.getQuantity() != null){
                accessories.setQuantity(vo.getQuantity().toString());
            }
            accessoriesList.add(accessories);
        }
        //????????????
        vPriceReqVo.setAccessoriesList(accessoriesList);
        
        vPriceReqVo.setFittingsChangeSumNum(String.valueOf(replaceNum));
        jyFittingsSumAmount = jystandardFittingsSumAmount.add(jyNonstandardFittingsSumAmount);
        vPriceReqVo.setJystandardSingleFittingsHighAmount(jystandardSingleFittingsHighAmount.toString());
        vPriceReqVo.setJystandardFittingsSumAmount(jystandardFittingsSumAmount.toString());
        vPriceReqVo.setJyNonstandardSingleFittingsHighAmount(jyNonstandardSingleFittingsHighAmount.toString());
        vPriceReqVo.setJyNonstandardFittingsSumAmount(jyNonstandardFittingsSumAmount.toString());
        vPriceReqVo.setJyFittingsSumAmount(jyFittingsSumAmount.toString());

        
        String coverageFlag = "0";
        BigDecimal carAmount = new BigDecimal(0);
        for(PrpLCMainVo vo : prpLCMainVoList){
            if("1".equals(coverageFlag)){
                break;
            }else{
                List<PrpLCItemKindVo> prpLCItemKindVoList = vo.getPrpCItemKinds();
                if(prpLCItemKindVoList != null && prpLCItemKindVoList.size() > 0){
                    for(PrpLCItemKindVo itemKindVo : prpLCItemKindVoList){
                        if("A".equals(itemKindVo.getKindCode())||"A1".equals(itemKindVo.getKindCode())){
                            coverageFlag = "1";
                            carAmount = DataUtils.NullToZero(itemKindVo.getAmount());
                            break;
                        }
                    }
                } 
            }
        }
        vPriceReqVo.setCoverageFlag(coverageFlag);
        if("1".equals(lossCarMainVo.getDeflossCarType())){
            if(lossCarMainVo.getSumLossFee() != null){
                vPriceReqVo.setMarkCarAmount(lossCarMainVo.getSumLossFee().toString());
            }
        }else{
            vPriceReqVo.setMarkCarAmount("0");
        }
        vPriceReqVo.setCarAmount(carAmount.toString());
        
    
      
        List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(registNo);
        List<PrpLdlossPropMainVo> prpLdlossPropMainVoList = propTaskService.findPropMainListByRegistNo(registNo);
        BigDecimal thirdVehicleCertainAmount = new BigDecimal(0);
        if(prpLDlossCarMainVoList!=null && prpLDlossCarMainVoList.size()>0){
	        for(PrpLDlossCarMainVo vo : prpLDlossCarMainVoList){
	            if(vo.getSumLossFee() != null){
	                thirdVehicleCertainAmount = thirdVehicleCertainAmount.add(vo.getSumLossFee());
	            }
	        }
        }
        if(prpLdlossPropMainVoList!=null && prpLdlossPropMainVoList.size()>0){
	        for(PrpLdlossPropMainVo vo : prpLdlossPropMainVoList){
	            if(vo.getSumDefloss() != null){
	                thirdVehicleCertainAmount = thirdVehicleCertainAmount.add(vo.getSumDefloss());
	            }
	        }
        }
        vPriceReqVo.setThirdVehicleCertainAmount(thirdVehicleCertainAmount.toString());
        BigDecimal certainAmount = new BigDecimal(0);
        if(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossCarMainVo.getDeflossSourceFlag())){
            List<PrpLDlossCarMainVo> carMains = lossCarService.findLossCarMainBySerialNo(registNo,lossCarMainVo.getSerialNo());            
            for(PrpLDlossCarMainVo carMain : carMains){
                if(carMain.getSumMatFee() != null){
                    certainAmount = certainAmount.add(carMain.getSumMatFee());
                }
                if(carMain.getSumCompFee() != null){
                    certainAmount = certainAmount.add(carMain.getSumCompFee());
                }
            }
        }else{
            if(lossCarMainVo.getSumMatFee() != null){
                certainAmount = certainAmount.add(lossCarMainVo.getSumMatFee());
            }
            if(lossCarMainVo.getSumCompFee() != null){
                certainAmount = certainAmount.add(lossCarMainVo.getSumCompFee());
            }
        }
        vPriceReqVo.setCertainAmount(certainAmount.toString());
        BigDecimal modificamCertainAmount = new BigDecimal(0);
        List<PrpLDlossCarMainHisVo> lossMainHisList = deflossService.findDeflossHisByMainId(lossCarMainVo.getId());
        if(lossMainHisList != null && lossMainHisList.size() > 0){
            PrpLDlossCarMainHisVo lossCarMainHisVo = lossMainHisList.get(lossMainHisList.size()-1);
            if(lossCarMainHisVo.getSumLossFee() != null){
                modificamCertainAmount = modificamCertainAmount.add(lossCarMainHisVo.getSumLossFee());
            }
            if(lossCarMainHisVo.getSumRescueFee() != null){
                modificamCertainAmount = modificamCertainAmount.add(lossCarMainHisVo.getSumLossFee());
            }
            vPriceReqVo.setModificaCertainAmount(modificamCertainAmount.toString());
        }
        BigDecimal certainSumAmount = new BigDecimal(0);
        if(lossCarMainVo.getSumRescueFee() != null){
            certainSumAmount = certainSumAmount.add(lossCarMainVo.getSumRescueFee());
            vPriceReqVo.setRescueFeeAmount(lossCarMainVo.getSumRescueFee().toString());
        }
        BigDecimal sumLossFee = new BigDecimal("0");
        sumLossFee = DataUtils.NullToZero(lossCarMainVo.getSumLossFee());
        certainSumAmount = certainSumAmount.add(sumLossFee);
        
        if(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossCarMainVo.getDeflossSourceFlag())){
	        List<PrpLDlossCarMainVo> prpLDlossCarMains = deflossService.findLossCarMainBySerialNo(lossCarMainVo.getRegistNo(),lossCarMainVo.getSerialNo());
        	for(PrpLDlossCarMainVo prpLDlossCarMainVo : prpLDlossCarMains){
            	if(UnderWriteFlag.MANAL_UNDERWRITE.equals(prpLDlossCarMainVo.getUnderWriteFlag())){
            		sumLossFee = sumLossFee
            				.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumLossFee()))
    						.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumChargeFee()))//??????
    		        		.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumSubRiskFee()))//?????????
    						.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumRescueFee()))//?????????
    						.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumOutFee()));//??????
    	        }
            }     
		}			
        
		if(CodeConstants.JyFlag.NOIN.equals(lossCarMainVo.getFlag())){//??????????????????????????????????????? ??????????????????
			sumLossFee = sumLossFee
					.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))
					.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()));
		}else{
			if(lossCarMainVo.getSumVeripLoss()!=null 
					&& (CodeConstants.CetainLossType.DEFLOSS_REPAIR.equals(lossCarMainVo.getCetainLossType())
					|| CodeConstants.CetainLossType.DEFLOSS_AGREE.equals(lossCarMainVo.getCetainLossType()))){
				sumLossFee = sumLossFee
						.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))//??????
		        		.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()))//?????????
						.add(DataUtils.NullToZero(lossCarMainVo.getSumRescueFee()))//?????????
						.add(DataUtils.NullToZero(lossCarMainVo.getSumOutFee()));//??????
			}else{
				sumLossFee = sumLossFee
						.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))
						.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()));
			}
		}
        vPriceReqVo.setSumAmount(String.valueOf(sumLossFee));//???????????????
        vPriceReqVo.setCertainSumAmount(certainSumAmount.toString());
        
        BigDecimal materialFee = new BigDecimal(0);
        if(lossCarMainVo.getPrpLDlossCarMaterials() != null && lossCarMainVo.getPrpLDlossCarMaterials().size() > 0){
            for(PrpLDlossCarMaterialVo carMaterialVo : lossCarMainVo.getPrpLDlossCarMaterials()){
                if(carMaterialVo.getMaterialFee() != null){
                    materialFee = materialFee.add(carMaterialVo.getMaterialFee());
                }
            }
        }
        vPriceReqVo.setJyingredientsSumAmount(materialFee.toString());
        

        String urlStr = SpringProperties.getProperty("ILOG_SVR_URL");//??????ILOG??????????????????
        urlStr = urlStr + CheckRuleForCarVerifyPriceServlet;//??????????????????
        String requestXML = XstreamFactory.objToXml(vPriceReqVo);//??????????????????xml
        logger.info("????????????????????????xml============" + requestXML);
        String returnXml = earlyWarnService.requestSDEW(requestXML,urlStr);//??????ILOG
        logger.info("????????????????????????xml============" + returnXml);
        lIlogRuleResVo = XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//??????????????????vo

        return lIlogRuleResVo;
    }



	@Override
	public LIlogRuleResVo organizaOldVProperty(PrpLdlossPropMainVo lossPropMainVo,String operateType) throws Exception {
        VPriceReqVo vPriceReqVo = new VPriceReqVo();
        LIlogRuleResVo lIlogRuleResVo = new LIlogRuleResVo();
        String registNo = lossPropMainVo.getRegistNo();
        List<PrpLCMainVo> prpLCMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(registNo);//??????????????????
        vPriceReqVo.setRegistNo(registNo);//?????????
        vPriceReqVo.setOperateType(operateType);//????????????1?????????   2???????????????
        vPriceReqVo.setRiskCode(lossPropMainVo.getRiskCode());
        vPriceReqVo.setComCode(lossPropMainVo.getMakeCom());
        vPriceReqVo.setTaskType("1");//0?????????  1?????????
        vPriceReqVo.setRepairFactoryType("");//???????????????
        
        //??????
        int jyStandardFittingsNum = 0;
        BigDecimal jystandardSingleFittingsHighAmount = new BigDecimal(0);
        BigDecimal jystandardFittingsSumAmount = new BigDecimal(0);
        //?????????
        int jyNonstandardFittingsNum = 0;
        BigDecimal jyNonstandardSingleFittingsHighAmount = new BigDecimal(0);
        BigDecimal jyNonstandardFittingsSumAmount = new BigDecimal(0);
        //???
        BigDecimal jyFittingsSumAmount = new BigDecimal(0);
        
        jyFittingsSumAmount = jystandardFittingsSumAmount.add(jyNonstandardFittingsSumAmount);
        //????????????
        vPriceReqVo.setJyStandardFittingsNum(String.valueOf(jyStandardFittingsNum));//????????????????????????????????????
        vPriceReqVo.setJystandardSingleFittingsHighAmount(String.valueOf(jystandardSingleFittingsHighAmount));//????????????????????????????????????????????????
        vPriceReqVo.setJyNonstandardFittingsNum(String.valueOf(jyNonstandardFittingsNum));//???????????????????????????????????????
        vPriceReqVo.setJyNonstandardSingleFittingsHighAmount(String.valueOf(jyNonstandardSingleFittingsHighAmount));//???????????????????????????????????????????????????
        vPriceReqVo.setJystandardFittingsSumAmount(String.valueOf(jystandardFittingsSumAmount));//?????????????????????????????????????????????
        vPriceReqVo.setJyNonstandardFittingsSumAmount(String.valueOf(jyNonstandardFittingsSumAmount));//????????????????????????????????????????????????
        vPriceReqVo.setJyFittingsSumAmount(String.valueOf(jyFittingsSumAmount)); //???????????????????????????????????????
        
        
        String coverageFlag = "0";//?????????????????????????????????
        String carAmount = "";
        for(PrpLCMainVo vo : prpLCMainVoList){
            if("1".equals(coverageFlag)){
                break;
            }else{
                List<PrpLCItemKindVo> prpLCItemKindVoList = vo.getPrpCItemKinds();
                if(prpLCItemKindVoList != null && prpLCItemKindVoList.size() > 0){
                    for(PrpLCItemKindVo itemKindVo : prpLCItemKindVoList){
                        if("A".equals(itemKindVo.getKindCode())||"A1".equals(itemKindVo.getKindCode())){
                            coverageFlag = "1";
                            carAmount = String.valueOf(itemKindVo.getAmount());
                            break;
                        }
                    }
                } 
            }
        }
        vPriceReqVo.setCoverageFlag(coverageFlag);
        //??????????????????
        vPriceReqVo.setMarkCarAmount("0");
        
        List<PrpLDlossCarMainVo> lossCarMainList = lossCarService.findLossCarMainByRegistNo(registNo);
        for(PrpLDlossCarMainVo  lossCarMainVo:lossCarMainList){
            if("1".equals(lossCarMainVo.getDeflossCarType())){
                if(lossCarMainVo.getSumLossFee() != null){
                    vPriceReqVo.setMarkCarAmount(String.valueOf(lossCarMainVo.getSumLossFee()));
                }
            }
        }
        
        vPriceReqVo.setCarAmount(carAmount);//???????????????
           
        
        BigDecimal thirdVehicleCertainAmount = new BigDecimal(0);
        for(PrpLDlossCarMainVo vo : lossCarMainList){
            if(vo.getSumLossFee() != null){
                thirdVehicleCertainAmount = thirdVehicleCertainAmount.add(vo.getSumLossFee());
            }
        }
        BigDecimal sumDefLoss = new BigDecimal("0");
		if(lossPropMainVo.getSumDefloss()!=null){
			sumDefLoss = lossPropMainVo.getSumDefloss();
			thirdVehicleCertainAmount = thirdVehicleCertainAmount.add(sumDefLoss);
		}
        vPriceReqVo.setThirdVehicleCertainAmount(String.valueOf(thirdVehicleCertainAmount));//??????????????????????????????
        vPriceReqVo.setCertainAmount(String.valueOf(sumDefLoss));//?????????????????????????????????
        vPriceReqVo.setCertainSumAmount(String.valueOf(DataUtils.NullToZero(lossPropMainVo.getSumDefloss()).add(DataUtils.NullToZero(lossPropMainVo.getDefRescueFee()))));//??????????????????
        BigDecimal ModificaCertainAmount = new BigDecimal("0");
        PrpLdlossPropMainHisVo lossMainHisvo = propLossService.findPropHisByPropMainId(lossPropMainVo.getId());
        if(lossMainHisvo != null){
        	ModificaCertainAmount=lossMainHisvo.getSumDefLoss();
        }
        vPriceReqVo.setModificaCertainAmount(String.valueOf(ModificaCertainAmount));//?????????????????????
        vPriceReqVo.setRescueFeeAmount(String.valueOf(DataUtils.NullToZero(lossPropMainVo.getDefRescueFee())));//???????????????
        
		if(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossPropMainVo.getDeflossSourceFlag())){
			List<PrpLdlossPropMainVo> prpLdlossPropMainVos = propLossService.findLossPropBySerialNo(registNo, lossPropMainVo.getSerialNo());
	    	for(PrpLdlossPropMainVo prpLdlossPropMainVo : prpLdlossPropMainVos){
            	if(UnderWriteFlag.MANAL_UNDERWRITE.equals(prpLdlossPropMainVo.getUnderWriteFlag())){
            		sumDefLoss = sumDefLoss
    	    				.add(DataUtils.NullToZero(prpLdlossPropMainVo.getSumDefloss()))
    	            		.add(DataUtils.NullToZero(prpLdlossPropMainVo.getDefRescueFee()))//????????????
    	            		.add(DataUtils.NullToZero(prpLdlossPropMainVo.getSumLossFee()));//??????
            	}
			}
		}

	    sumDefLoss = sumDefLoss
	    		.add(DataUtils.NullToZero(lossPropMainVo.getDefRescueFee()))//????????????
	    		.add(DataUtils.NullToZero(lossPropMainVo.getSumLossFee()));//??????
        
        vPriceReqVo.setSumAmount(String.valueOf(sumDefLoss));//???????????????
        
        String urlStr=SpringProperties.getProperty("ILOG_SVR_URL");//??????ILOG??????????????????
        urlStr=urlStr+"CheckRuleForCarVerifyLossServlet";//??????????????????
        String requestXML=XstreamFactory.objToXml(vPriceReqVo);//??????????????????xml
        String returnXml=earlyWarnService.requestSDEW(requestXML,urlStr);//??????ILOG
        logger.info("******ILOG?????????????????????*******");
        logger.info(requestXML);
        logger.info("******ILOG?????????????????????*******");
        logger.info(returnXml);
        lIlogRuleResVo= XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//??????????????????vo
        
        return lIlogRuleResVo;
      }
 
}
