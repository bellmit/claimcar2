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
 * 车险车辆核价对外服务接口
 * <pre></pre>
 * @author ★zhujunde
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
        //vPriceReqVo.setCurrentNodeNo("");//当前审核级别
        vPriceReqVo.setOperateType(operateType);//操作类型
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
        	vPriceReqVo.setCoinsFlag("0".equals(prpLCMainVoList.get(0).getCoinsFlag()) ? "0":"1");//是否联共保案件
        }
        
        if(prpLCMainVoList.get(0).getPrpCItemCars().get(0).getEnrollDate() != null){
        	vPriceReqVo.setEnrollDate(format.format(prpLCMainVoList.get(0).getPrpCItemCars().get(0).getEnrollDate())); //初登日期 
        }
           
        List<PrpLDlossCarCompVo> dlossCarCompVoList = lossCarMainVo.getPrpLDlossCarComps();
        //标准
        int jyStandardFittingsNum = 0;
        BigDecimal jystandardSingleFittingsHighAmount = new BigDecimal(0);
        BigDecimal jystandardFittingsSumAmount = new BigDecimal(0);
        //非标准
        int jyNonstandardFittingsNum = 0;
        BigDecimal jyNonstandardSingleFittingsHighAmount = new BigDecimal(0);
        BigDecimal jyNonstandardFittingsSumAmount = new BigDecimal(0);
        //总
        BigDecimal jyFittingsSumAmount = new BigDecimal(0);
        int replaceNum = 0;//零部件更换次数合计
        List<Accessories> accessoriesList = new ArrayList<Accessories>();
        for(PrpLDlossCarCompVo vo : dlossCarCompVoList){
            Accessories accessories = new Accessories();
            accessories.setIsStandard("0");
            // 0-系统配件  1-自定义配件 2-标准配件
            if("2".equals(vo.getSelfConfigFlag())){
                jyStandardFittingsNum += vo.getQuantity();
                if(jystandardSingleFittingsHighAmount.compareTo(vo.getMaterialFee())<0){//取最高价格
                    jystandardSingleFittingsHighAmount = vo.getMaterialFee();
                }
                jystandardFittingsSumAmount = jystandardFittingsSumAmount.add(vo.getMaterialFee());//汇总
                accessories.setIsStandard("1");
            }else{
                jyNonstandardFittingsNum += vo.getQuantity();
                if(jyNonstandardSingleFittingsHighAmount.compareTo(vo.getMaterialFee())<0){//取最高价格
                    jyNonstandardSingleFittingsHighAmount = vo.getMaterialFee();
                }
                jyNonstandardFittingsSumAmount = jyNonstandardFittingsSumAmount.add(vo.getMaterialFee());//汇总
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
        //配件信息
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
        vPriceReqVo.setSubrogationFlag(subrogationFlag);//是否代位求偿  0非代位求偿  1代位求偿
        
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
                if("12".equals(vo.getRiskCode().substring(0,2))){//商业
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
                   /* <c:if test="${fn:substring(prpLCItemCarVo.otherNature, 4, 5)=='1' }">是</c:if>
                    <c:if test="${fn:substring(prpLCItemCarVo.otherNature, 4, 5)!='1' }">否</c:if>*/
                    if("1".equals(prpLCItemCarVo.getOtherNature().substring(4,5))){//过户
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
             if("1".equals(prpLCItemCarVo.getOtherNature().substring(4,5))){//过户
                 vPriceReqVo.setChgOwnerFlag("1");
             }else{
                 vPriceReqVo.setChgOwnerFlag("0");
             }
             vPriceReqVo.setUseKindCode(prpLCItemCarVo.getUseKindCode());
        }
        //待续
/*        List<PrpLCMainVo> lCMainVoList = policyViewService.getPolicyAllInfo(registNo);
        for(PrpLCMainVo prpLCMaintmp : prpLCMainVoList){
            //取得相关保单的车辆信息
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
        //出险次数待续
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
        // 处理人员类型 （0-司内人员 1-公估人员） 默认为司内人员
        vPriceReqVo.setHandlerType("0");
        List<SingleCarLossInfo> singleCarLossInfos = new ArrayList<SingleCarLossInfo>();
        for (PrpLDlossCarMainVo carmain : prpLDlossCarMainVoList) {
        	// 单车总损失统计（配件、辅料、工时、外修）
        	if (carmain.getSumLossFee() != null) {
        		SingleCarLossInfo singlecarlossinfo = new SingleCarLossInfo();
        		singlecarlossinfo.setSingleCarSumloss(carmain.getSumLossFee().toString());
        		singleCarLossInfos.add(singlecarlossinfo);
        	}
        	
        	// 如果有公估机构的人即视为公估机构处理
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
    						.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumChargeFee()))//费用
    		        		.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumSubRiskFee()))//附加险
    						.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumRescueFee()))//施救费
    						.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumOutFee()));//外修
    	        }
            }     
		}			
        
		if(CodeConstants.JyFlag.NOIN.equals(lossCarMainVo.getFlag())){//不走精友，同意核价提交核损 加上定损费用
			sumLossFee = sumLossFee
					.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))
					.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()));
		}else{
			if(lossCarMainVo.getSumVeripLoss()!=null 
					&& (CodeConstants.CetainLossType.DEFLOSS_REPAIR.equals(lossCarMainVo.getCetainLossType())
					|| CodeConstants.CetainLossType.DEFLOSS_AGREE.equals(lossCarMainVo.getCetainLossType()))){
				sumLossFee = sumLossFee
						.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))//费用
		        		.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()))//附加险
						.add(DataUtils.NullToZero(lossCarMainVo.getSumRescueFee()))//施救费
						.add(DataUtils.NullToZero(lossCarMainVo.getSumOutFee()));//外修
			}else{
				sumLossFee = sumLossFee
						.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))
						.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()));
			}
		}
		
            
        vPriceReqVo.setSumAmount(String.valueOf(sumLossFee));//总定损金额
                
        vPriceReqVo.setCertainSumAmount(certainSumAmount.toString());
        vPriceReqVo.setDamageCode(prpLRegistVo.getDamageCode());
        //是否授权,授权1，否0
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
        claimDeductVoList = registQueryService.findClaimDeductVoByRegistNo(registNo);// 获取该报案号下所有免赔条件
        if(claimDeductVoList != null && claimDeductVoList.size() > 0){
            for(PrpLClaimDeductVo vo : claimDeductVoList){
                if("1".equals(vo.getIsCheck())){
                    deductCondCode = deductCondCode + "," +vo.getDeductCondCode();
                }
            }
            if(deductCondCode.length()>0){
                deductCondCode = deductCondCode.substring(1,deductCondCode.length());//去除第一个逗号
            }
        }
        
        vPriceReqVo.setDeductCondCode(deductCondCode);
        vPriceReqVo.setEmployeeId(userVo.getUserCode());//员工工号
        String lossPartyName="";
        String licenseNo="";
        String handlerId=prpLWfTaskVo.getHandlerIdKey();
        PrpLDlossCarMainVo vo=lossCarService.findLossCarMainById(Long.valueOf(handlerId));
        lossPartyName=vo.getDeflossCarType();
        licenseNo=vo.getLicenseNo();//损失方的车牌号
        vPriceReqVo.setLossPartyName(Integer.parseInt(lossPartyName)>1?"2":lossPartyName);//损失方 0  地面/路人损失  1  标的车  2  三者车

        vPriceReqVo.setIsNuclearpricereturn("0");//是否核价退回案件
        PrpLWfTaskVo upperPrpLWfTaskVo=wfFlowService.findPrpLWfTaskQueryByTaskId(prpLWfTaskVo.getUpperTaskId());
        if(WorkStatus.BACK.equals(upperPrpLWfTaskVo.getWorkStatus())&&FlowNode.VPrice.name().equals(upperPrpLWfTaskVo.getNodeCode())){   
            vPriceReqVo.setIsNuclearpricereturn("1");//是否核价退回案件
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
        

        String urlStr = SpringProperties.getProperty("ILOG_SVR_URL");//获取ILOG规则服务地址
        urlStr = urlStr + CheckRuleForCarVerifyPriceServlet;//整合交互地址
        String requestXML = XstreamFactory.objToXml(vPriceReqVo);//请求报文转换xml
        logger.info("核价请求报文转换xml============" + requestXML);
        String returnXml = earlyWarnService.requestSDEW(requestXML,urlStr);//推送ILOG
        logger.info("核价返回报文转换xml============" + returnXml);
        lIlogRuleResVo = XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//返回报文转换vo
        
        IlogDataProcessingVo ilogDataProcessingVo =new IlogDataProcessingVo();
        ilogDataProcessingVo.setBusinessNo(vPriceReqVo.getRegistNo());//业务号 报案号
        ilogDataProcessingVo.setCompensateNo("");//计算书号   
        ilogDataProcessingVo.setComCode(vPriceReqVo.getComCode());//业务归属机构
        ilogDataProcessingVo.setRiskCode(vPriceReqVo.getRiskCode());//险种
        ilogDataProcessingVo.setOperateType(vPriceReqVo.getOperateType());//操作类型  1：自动  2：人工权限
        ilogDataProcessingVo.setRuleType(vPriceReqVo.getTaskType());//任务类型 0：车辆; 1：财产
        ilogDataProcessingVo.setRuleNode("VPrice");//任务节点
        ilogDataProcessingVo.setLossParty(vPriceReqVo.getLossPartyName());//损失方
        ilogDataProcessingVo.setLicenseNo(licenseNo);//损失方的车牌号
        ilogDataProcessingVo.setTriggerNode(triggerNode);//触发节点  提交到任务节点的前节点
        ilogDataProcessingVo.setTaskId(taskId);//触发节点对应的工作流ID 
        ilogDataProcessingVo.setOperatorCode(userVo.getUserCode());//操作人员
        
        ruleReturnDataSaveService.dealILogResReturnData(lIlogRuleResVo,ilogDataProcessingVo);//规则保存
        return lIlogRuleResVo;
    }
    
    
    
    public LIlogRuleResVo organizaVProperty(PrpLdlossPropMainVo lossPropMainVo,String operateType,SysUserVo userVo,BigDecimal taskId,String triggerNode,String isSubmitHeadOffice ) throws Exception{
        VPriceReqVo vPriceReqVo = new VPriceReqVo();
        LIlogRuleResVo lIlogRuleResVo = new LIlogRuleResVo();
        String registNo = lossPropMainVo.getRegistNo();
        PrpLRegistVo prpLRegistVo = registService.findRegistByRegistNo(registNo);
        List<PrpLCMainVo> prpLCMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(registNo);//保单信息主表
        vPriceReqVo.setRegistNo(registNo);//报案号
//        vPriceReqVo.setCurrentNodeNo(0);//当前审核级别  人工权限时传值
//        if("2".equals(operateType)){//人工权限时传值
//            vPriceReqVo.setCurrentNodeNo(currentNodeNo);//当前审核级别  人工权限时传值
//        }
        if(isSubmitHeadOffice != null){
        	vPriceReqVo.setExistHeadOffice(isSubmitHeadOffice);
        } else{
        	vPriceReqVo.setExistHeadOffice(CodeConstants.CommonConst.FALSE);
        }
        vPriceReqVo.setOperateType(operateType);//操作类型1：自动   2：人工权限
        vPriceReqVo.setRiskCode(lossPropMainVo.getRiskCode());
        vPriceReqVo.setComCode(lossPropMainVo.getMakeCom());
        vPriceReqVo.setTaskType("1");//0：车辆  1：财产
        vPriceReqVo.setRepairFactoryType("");//修理厂类型
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 

        String damageTime = format.format(prpLRegistVo.getDamageTime());
        vPriceReqVo.setDamageDate(damageTime);//出险时间
        damageTime = damageTime.substring(11,damageTime.length());
        String[] damageHourMinute = damageTime.split(":");
        String damageHour ="";
        String damageMinute="";
        if(damageHourMinute.length > 1){
            damageHour = damageHourMinute[0];
            damageMinute = damageHourMinute[1];
        }
        vPriceReqVo.setDamageHour(damageHour);//出险小时
        vPriceReqVo.setDamageMinute(damageMinute);//出险分钟
        
        if(prpLCMainVoList!=null && prpLCMainVoList.size()>0){
        	if(prpLCMainVoList.get(0).getPrpCItemCars().get(0).getEnrollDate() != null){
            	vPriceReqVo.setEnrollDate(format.format(prpLCMainVoList.get(0).getPrpCItemCars().get(0).getEnrollDate())); //初登日期 
            }
        	vPriceReqVo.setCoinsFlag("0".equals(prpLCMainVoList.get(0).getCoinsFlag()) ? "0":"1");//是否联共保案件
        }

//        List<PrpLDlossCarCompVo> dlossCarCompVoList = new ArrayList<PrpLDlossCarCompVo>();//换件项目清单 财产无精友数据
        //标准
        int jyStandardFittingsNum = 0;
        BigDecimal jystandardSingleFittingsHighAmount = new BigDecimal(0);
        BigDecimal jystandardFittingsSumAmount = new BigDecimal(0);
        //非标准
        int jyNonstandardFittingsNum = 0;
        BigDecimal jyNonstandardSingleFittingsHighAmount = new BigDecimal(0);
        BigDecimal jyNonstandardFittingsSumAmount = new BigDecimal(0);
        //总
        BigDecimal jyFittingsSumAmount = new BigDecimal(0);
        int replaceNum = 0;//零部件更换次数合计
        
        jyFittingsSumAmount = jystandardFittingsSumAmount.add(jyNonstandardFittingsSumAmount);
        //精友系统
        vPriceReqVo.setJyStandardFittingsNum(String.valueOf(jyStandardFittingsNum));//精友系统标准点选配件个数
        vPriceReqVo.setJystandardSingleFittingsHighAmount(String.valueOf(jystandardSingleFittingsHighAmount));//精友系统标准点选单个配件最高金额
        vPriceReqVo.setJyNonstandardFittingsNum(String.valueOf(jyNonstandardFittingsNum));//精友系统非标准点选配件个数
        vPriceReqVo.setJyNonstandardSingleFittingsHighAmount(String.valueOf(jyNonstandardSingleFittingsHighAmount));//精友系统非标准点选单个配件最高金额
        vPriceReqVo.setJystandardFittingsSumAmount(String.valueOf(jystandardFittingsSumAmount));//精友系统标准点选配件更换总金额
        vPriceReqVo.setJyNonstandardFittingsSumAmount(String.valueOf(jyNonstandardFittingsSumAmount));//精友系统非标准点选配件更换总金额
        vPriceReqVo.setJyFittingsSumAmount(String.valueOf(jyFittingsSumAmount)); //精友系统点选配件更换总金额
        
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
        vPriceReqVo.setSubrogationFlag(subrogationFlag);//是否代位求偿  0非代位求偿  1代位求偿
        
        String coverageFlag = "0";//是否承保机动车损失保险
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
        //财产定损金额
        vPriceReqVo.setMarkCarAmount("0");
        
        List<PrpLDlossCarMainVo> lossCarMainList = lossCarService.findLossCarMainByRegistNo(registNo);
        for(PrpLDlossCarMainVo  lossCarMainVo:lossCarMainList){
            if("1".equals(lossCarMainVo.getDeflossCarType())){
                if(lossCarMainVo.getSumLossFee() != null){
                    vPriceReqVo.setMarkCarAmount(String.valueOf(lossCarMainVo.getSumLossFee()));
                }
            }
        }
        
        vPriceReqVo.setCarAmount(carAmount);//车损险保额
        
        if(prpLCMainVoList != null && prpLCMainVoList.size() > 1 ){
            for(PrpLCMainVo vo : prpLCMainVoList){
                if(!Risk.isDQZ(vo.getRiskCode())){//商业
                    List<PrpLCMainVo> vos = registTmpService.findAreadictByPolicyNo(vo.getPolicyNo());
                    PrpLCMainVo prpLCMainVo = vos.get(0);
                    vPriceReqVo.setStartDate(format1.format(prpLCMainVo.getStartDate()));//起保日期
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
                    vPriceReqVo.setEndDate(format1.format(prpLCMainVo.getEndDate()));//终保日期
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
                    if("1".equals(prpLCItemCarVo.getOtherNature().substring(4,5))){//过户
                        vPriceReqVo.setChgOwnerFlag("1");//标的车是否过户车
                    }else{
                        vPriceReqVo.setChgOwnerFlag("0");
                    }
                    vPriceReqVo.setUseKindCode(prpLCItemCarVo.getUseKindCode());
                }
            }
        }else if(prpLCMainVoList != null){
            List<PrpLCMainVo> vos = registTmpService.findAreadictByPolicyNo(prpLCMainVoList.get(0).getPolicyNo());
            PrpLCMainVo prpLCMainVo = vos.get(0);
            vPriceReqVo.setStartDate(format1.format(prpLCMainVo.getStartDate()));//起保日期
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
            vPriceReqVo.setEndDate(format1.format(prpLCMainVo.getEndDate()));//终保日期
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
            if("1".equals(prpLCItemCarVo.getOtherNature().substring(4,5))){//过户
                vPriceReqVo.setChgOwnerFlag("1");//标的车是否过户车
             }else{
                 vPriceReqVo.setChgOwnerFlag("0");
             }
             vPriceReqVo.setUseKindCode(prpLCItemCarVo.getUseKindCode());
        }
        
        //报案日期   
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

        //出险次数待续
        Map<String, String> registRiskInfoMap = new HashMap<String, String>();
        if (!StringUtils.isEmpty(registNo)) {
            registRiskInfoMap = registService.findRegistRiskInfoByRegistNo(registNo);
        }
        String ciDangerNum = registRiskInfoMap.get("CI-DangerNum");//（交强）
        if(StringUtils.isNotBlank(ciDangerNum)){
            vPriceReqVo.setJqDamagTime(ciDangerNum);
        }else{
            vPriceReqVo.setJqDamagTime("");
        }
        String ciDangerInSum = registRiskInfoMap.get("CI-DangerInSum");//7天内出险次数（交强）
        if(StringUtils.isNotBlank(ciDangerInSum)){
            vPriceReqVo.setJqSevenDaysDamagTime(ciDangerInSum);
        }else{
            vPriceReqVo.setJqSevenDaysDamagTime("");
        }

        String biDangerNum = registRiskInfoMap.get("BI-DangerNum");//出险次数（商业）
        if(StringUtils.isNotBlank(biDangerNum)){
            vPriceReqVo.setSyDamagtime(biDangerNum);
        }else{
            vPriceReqVo.setSyDamagtime("");
        }
        String biDangerInSum = registRiskInfoMap.get("BI-DangerInSum");//7天内出险次数（商业）
        if(StringUtils.isNotBlank(biDangerInSum)){
            vPriceReqVo.setSySevenDaysDamagTime(biDangerInSum);
        }else{
            vPriceReqVo.setSySevenDaysDamagTime("");
        }
        
        vPriceReqVo.setFittingsChangeSumNum(String.valueOf(replaceNum));//零部件更换次数合计           
        
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
        vPriceReqVo.setThirdVehicleCertainAmount(String.valueOf(thirdVehicleCertainAmount));//三者车财定损金额之和
        vPriceReqVo.setCertainAmount(String.valueOf(sumDefLoss));//定损金额（不含施救费）
        vPriceReqVo.setCertainSumAmount(String.valueOf(lossPropMainVo.getSumDefloss().add(lossPropMainVo.getDefRescueFee())));//定损金额合计
        BigDecimal ModificaCertainAmount = new BigDecimal("0");
        PrpLdlossPropMainHisVo lossMainHisvo = propLossService.findPropHisByPropMainId(lossPropMainVo.getId());
        if(lossMainHisvo != null){
        	ModificaCertainAmount=lossMainHisvo.getSumDefLoss();
        }
        vPriceReqVo.setModificaCertainAmount(String.valueOf(ModificaCertainAmount));//修改前定损金额
        vPriceReqVo.setRescueFeeAmount(String.valueOf(lossPropMainVo.getDefRescueFee()));//施救费金额
        
        List<PrpLClaimDeductVo> claimDeductVoList = new ArrayList<PrpLClaimDeductVo>();
        String deductCondCode = "";
        claimDeductVoList = registQueryService.findClaimDeductVoByRegistNo(registNo);// 获取该报案号下所有免赔条件
        if(claimDeductVoList != null && claimDeductVoList.size() > 0){
            for(PrpLClaimDeductVo vo : claimDeductVoList){
                if("1".equals(vo.getIsCheck())){
                    deductCondCode = deductCondCode + "," +vo.getDeductCondCode();
                }
            }
            if(deductCondCode.length()>0){
                deductCondCode = deductCondCode.substring(1,deductCondCode.length());//去除第一个逗号
            }
        }
        vPriceReqVo.setDeductCondCode(deductCondCode);//免赔条件
        vPriceReqVo.setDamageCode(prpLRegistVo.getDamageCode());//出险原因
        
        String lossPartyName="";
        String licenseNo="";
        PrpLWfTaskVo prpLWfTaskVo=wfFlowService.findPrpLWfTaskQueryByTaskId(taskId);
        String handlerId=prpLWfTaskVo.getHandlerIdKey();
        PrpLdlossPropMainVo vo=propLossService.findPropMainVoById(Long.valueOf(handlerId));
        lossPartyName=vo.getLossType();
        
        licenseNo=lossPropMainVo.getLicense();//损失方的车牌号
        
    	vPriceReqVo.setLossPartyName(Integer.parseInt(lossPartyName)>1?"2":lossPartyName);//损失方 0  地面/路人损失  1  标的车  2  三者车
    	
    	PrpLAutoVerifyVo prpLAutoVerifyVo = new PrpLAutoVerifyVo();
    	prpLAutoVerifyVo.setUserCode(userVo.getUserCode());
        prpLAutoVerifyVo.setNode(triggerNode);
    	boolean isSysAuthorization = deflossHandleService.isAutoVerifyUser(prpLAutoVerifyVo);   
        vPriceReqVo.setSysAuthorizationFlag(isSysAuthorization?"1":"0");//是否系统授权
        
        vPriceReqVo.setEmployeeId(userVo.getUserCode());//员工工号
        
        
		if(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossPropMainVo.getDeflossSourceFlag())){
			List<PrpLdlossPropMainVo> prpLdlossPropMainVos = propLossService.findLossPropBySerialNo(registNo, lossPropMainVo.getSerialNo());
	    	for(PrpLdlossPropMainVo prpLdlossPropMainVo : prpLdlossPropMainVos){
            	if(UnderWriteFlag.MANAL_UNDERWRITE.equals(prpLdlossPropMainVo.getUnderWriteFlag())){
            		sumDefLoss = sumDefLoss
    	    				.add(DataUtils.NullToZero(prpLdlossPropMainVo.getSumDefloss()))
    	            		.add(DataUtils.NullToZero(prpLdlossPropMainVo.getDefRescueFee()))//施救费用
    	            		.add(DataUtils.NullToZero(prpLdlossPropMainVo.getSumLossFee()));//费用
            	}
			}
		}

	    sumDefLoss = sumDefLoss
	    		.add(DataUtils.NullToZero(lossPropMainVo.getDefRescueFee()))//施救费用
	    		.add(DataUtils.NullToZero(lossPropMainVo.getSumLossFee()));//费用
        
        vPriceReqVo.setSumAmount(String.valueOf(sumDefLoss));//总定损金额
        
        vPriceReqVo.setSurveyFlag("0");//是否调查案件
        vPriceReqVo.setIsFlagN("0");//是否可疑交易
        
        //是否车物减损
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
        vPriceReqVo.setIsWhethertheloss(isWhethertheloss);//是否减损
        
        vPriceReqVo.setIsNucleardamagereturn("0");//是否核损退回案件
        PrpLWfTaskVo upperPrpLWfTaskVo=wfFlowService.findPrpLWfTaskQueryByTaskId(prpLWfTaskVo.getUpperTaskId());
        if(WorkStatus.BACK.equals(upperPrpLWfTaskVo.getWorkStatus())){   
            vPriceReqVo.setIsNucleardamagereturn("1");//是否核损退回案件
        }
        
        List<Accessories> accessoriesList = new ArrayList<Accessories>();
        vPriceReqVo.setAccessoriesList(accessoriesList); ////配件信息
        
        String urlStr=SpringProperties.getProperty("ILOG_SVR_URL");//获取ILOG规则服务地址
        urlStr=urlStr+"CheckRuleForCarVerifyLossServlet";//整合交互地址
        String requestXML=XstreamFactory.objToXml(vPriceReqVo);//请求报文转换xml
        String returnXml=earlyWarnService.requestSDEW(requestXML,urlStr);//推送ILOG
        System.out.println("******ILOG请求报文*******");
        System.out.println(requestXML);
        System.out.println("******ILOG返回报文*******");
        System.out.println(returnXml);
        lIlogRuleResVo= XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//返回报文转换vo
        
        IlogDataProcessingVo ilogDataProcessingVo =new IlogDataProcessingVo();
        ilogDataProcessingVo.setBusinessNo(vPriceReqVo.getRegistNo());//业务号 报案号
        ilogDataProcessingVo.setCompensateNo("");//计算书号   
        ilogDataProcessingVo.setComCode(vPriceReqVo.getComCode());//业务归属机构
        ilogDataProcessingVo.setRiskCode(vPriceReqVo.getRiskCode());//险种
        ilogDataProcessingVo.setOperateType(vPriceReqVo.getOperateType());//操作类型  1：自动  2：人工权限
        ilogDataProcessingVo.setRuleType(vPriceReqVo.getTaskType());//任务类型 0：车辆; 1：财产
        ilogDataProcessingVo.setRuleNode("VLProp");//任务节点
        ilogDataProcessingVo.setLossParty(lossPartyName);//损失方
        ilogDataProcessingVo.setLicenseNo(licenseNo);//损失方的车牌号
        ilogDataProcessingVo.setTriggerNode(triggerNode);//触发节点  提交到任务节点的前节点
        ilogDataProcessingVo.setTaskId(taskId);//触发节点对应的工作流ID 
        ilogDataProcessingVo.setOperatorCode(userVo.getUserCode());//操作人员

        ruleReturnDataSaveService.dealILogResReturnData(lIlogRuleResVo,ilogDataProcessingVo);//规则保存
        
        return lIlogRuleResVo;
      }
    
    
    
    public LIlogRuleResVo organizaVehicleLoss(PrpLDlossCarMainVo lossCarMainVo,String operateType,SysUserVo userVo,BigDecimal taskId,String triggerNode, String isSubmitHeadOffice) throws Exception{
        VPriceReqVo vPriceReqVo = new VPriceReqVo();
        LIlogRuleResVo lIlogRuleResVo = new LIlogRuleResVo();
        String registNo = lossCarMainVo.getRegistNo();
        PrpLRegistVo prpLRegistVo = registService.findRegistByRegistNo(registNo);
        List<PrpLCMainVo> prpLCMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(registNo);//保单信息主表
        vPriceReqVo.setRegistNo(registNo);//报案号
        //vPriceReqVo.setCurrentNodeNo("");//当前审核级别  人工权限时传值
//        if("2".equals(operateType)){//人工权限时传值
//            vPriceReqVo.setCurrentNodeNo(currentNodeNo);//当前审核级别  人工权限时传值
//        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 
        
        vPriceReqVo.setOperateType(operateType);//操作类型1：自动   2：人工权限
        vPriceReqVo.setRiskCode(lossCarMainVo.getRiskCode());
        vPriceReqVo.setComCode(lossCarMainVo.getMakeCom());
        vPriceReqVo.setTaskType("0");//0：车辆  1：财产
        vPriceReqVo.setRepairFactoryType(lossCarMainVo.getRepairFactoryType());//修理厂类型
        if(isSubmitHeadOffice != null){
            vPriceReqVo.setExistHeadOffice(isSubmitHeadOffice);
        } else{
        	vPriceReqVo.setExistHeadOffice(CodeConstants.CommonConst.FALSE);
        }
        String damageTime = format.format(prpLRegistVo.getDamageTime());

        vPriceReqVo.setDamageDate(damageTime);//出险时间
        damageTime = damageTime.substring(11,damageTime.length());
        String[] damageHourMinute = damageTime.split(":");
        String damageHour ="";
        String damageMinute="";
        if(damageHourMinute.length > 1){
            damageHour = damageHourMinute[0];
            damageMinute = damageHourMinute[1];
        }
        vPriceReqVo.setDamageHour(damageHour);//出险小时
        vPriceReqVo.setDamageMinute(damageMinute);//出险分钟
        
        if(prpLCMainVoList!=null && prpLCMainVoList.size()>0){
        	if(prpLCMainVoList.get(0).getPrpCItemCars().get(0).getEnrollDate() != null){
            	vPriceReqVo.setEnrollDate(format1.format(prpLCMainVoList.get(0).getPrpCItemCars().get(0).getEnrollDate())); //初登日期 
            }
        	vPriceReqVo.setCoinsFlag("0".equals(prpLCMainVoList.get(0).getCoinsFlag()) ? "0":"1");//是否联共保案件
        }
        
        List<PrpLDlossCarCompVo> dlossCarCompVoList = lossCarMainVo.getPrpLDlossCarComps();//换件项目清单
        //标准
        int jyStandardFittingsNum = 0;
        BigDecimal jystandardSingleFittingsHighAmount = new BigDecimal(0);
        BigDecimal jystandardFittingsSumAmount = new BigDecimal(0);
        //非标准
        int jyNonstandardFittingsNum = 0;
        BigDecimal jyNonstandardSingleFittingsHighAmount = new BigDecimal(0);
        BigDecimal jyNonstandardFittingsSumAmount = new BigDecimal(0);
        //总
        BigDecimal jyFittingsSumAmount = new BigDecimal(0);
        int replaceNum = 0;//零部件更换次数合计
        List<Accessories> accessoriesList = new ArrayList<Accessories>();
        for(PrpLDlossCarCompVo vo : dlossCarCompVoList){
            Accessories accessories = new Accessories();
            // 0-系统配件  1-自定义配件 2-标准配件
            if("2".equals(vo.getSelfConfigFlag())){//标准
                jyStandardFittingsNum += vo.getQuantity();
                if(jystandardSingleFittingsHighAmount.compareTo(vo.getMaterialFee())<0){//取最高价格
                    jystandardSingleFittingsHighAmount = vo.getMaterialFee();
                }
                jystandardFittingsSumAmount = jystandardFittingsSumAmount.add(vo.getMaterialFee());//汇总
                accessories.setIsStandard("1");//是否标准点选
            }else{
                jyNonstandardFittingsNum += vo.getQuantity();
                if(jyNonstandardSingleFittingsHighAmount.compareTo(vo.getMaterialFee())<0){//取最高价格
                    jyNonstandardSingleFittingsHighAmount = vo.getMaterialFee();
                }
                jyNonstandardFittingsSumAmount = jyNonstandardFittingsSumAmount.add(vo.getMaterialFee());//汇总
                accessories.setIsStandard("0");//是否标准点选
            }
            if(vo.getReplaceNum()!=null){
                replaceNum += vo.getReplaceNum();
            }     
            accessories.setCompCode(vo.getCompCode());//配件代码
            accessories.setCompName(vo.getCompName());//配件名称
            if(vo.getChgRefPrice()!=null){
                accessories.setChgrefPrice(String.valueOf(vo.getChgRefPrice()));//系统价格
            }
            if(vo.getSumDefLoss()!=null){
                accessories.setSumdefLoss(String.valueOf(vo.getSumDefLoss()));//定损价格
            }
            if(vo.getChgLocPrice()!=null){
                accessories.setChglocPrice(String.valueOf(vo.getChgLocPrice()));//本地价格

            }
            if(vo.getQuantity()!=null){
                accessories.setQuantity(String.valueOf(vo.getQuantity()));//数量
            }
            accessoriesList.add(accessories);
        }
        
        vPriceReqVo.setAccessoriesList(accessoriesList); ////配件信息

        
        jyFittingsSumAmount = jystandardFittingsSumAmount.add(jyNonstandardFittingsSumAmount);
        //精友系统
        vPriceReqVo.setJyStandardFittingsNum(String.valueOf(jyStandardFittingsNum));//精友系统标准点选配件个数
        vPriceReqVo.setJystandardSingleFittingsHighAmount(String.valueOf(jystandardSingleFittingsHighAmount));//精友系统标准点选单个配件最高金额
        vPriceReqVo.setJyNonstandardFittingsNum(String.valueOf(jyNonstandardFittingsNum));//精友系统非标准点选配件个数
        vPriceReqVo.setJyNonstandardSingleFittingsHighAmount(String.valueOf(jyNonstandardSingleFittingsHighAmount));//精友系统非标准点选单个配件最高金额
        vPriceReqVo.setJystandardFittingsSumAmount(String.valueOf(jystandardFittingsSumAmount));//精友系统标准点选配件更换总金额
        vPriceReqVo.setJyNonstandardFittingsSumAmount(String.valueOf(jyNonstandardFittingsSumAmount));//精友系统非标准点选配件更换总金额
        vPriceReqVo.setJyFittingsSumAmount(String.valueOf(jyFittingsSumAmount)); //精友系统点选配件更换总金额
        
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
        vPriceReqVo.setSubrogationFlag(subrogationFlag);//是否代位求偿  0非代位求偿  1代位求偿
       
        String coverageFlag = "0";//是否承保机动车损失保险
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
        //标的车定损金额
        if("1".equals(lossCarMainVo.getDeflossCarType())){
            if(lossCarMainVo.getSumLossFee() != null){
                vPriceReqVo.setMarkCarAmount(String.valueOf(lossCarMainVo.getSumLossFee()));
            }
        }else{
            vPriceReqVo.setMarkCarAmount("");
        }
        vPriceReqVo.setCarAmount(carAmount);//车损险保额
        if(prpLCMainVoList != null && prpLCMainVoList.size() > 1 ){
            for(PrpLCMainVo vo : prpLCMainVoList){
                if(!Risk.isDQZ(vo.getRiskCode())){//商业
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
                    if("1".equals(prpLCItemCarVo.getOtherNature().substring(4,5))){//过户
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
             if("1".equals(prpLCItemCarVo.getOtherNature().substring(4,5))){//过户
                 vPriceReqVo.setChgOwnerFlag("1");
             }else{
                 vPriceReqVo.setChgOwnerFlag("0");
             }
             vPriceReqVo.setUseKindCode(prpLCItemCarVo.getUseKindCode());
        }
        
        //报案日期   
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

        //出险次数待续
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
        
        vPriceReqVo.setFittingsChangeSumNum(String.valueOf(replaceNum));//零部件更换次数合计           
        
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
                
                // 单车总损失统计（配件、辅料、工时、外修）
            	if (vo.getSumLossFee() != null) {
            		SingleCarLossInfo singlecarlossinfo = new SingleCarLossInfo();
            		singlecarlossinfo.setSingleCarSumloss(vo.getSumLossFee().toString());
            		singleCarLossInfos.add(singlecarlossinfo);
            	}
            	
            	// 如果有公估机构的人即视为公估机构处理
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
        vPriceReqVo.setThirdVehicleCertainAmount(String.valueOf(thirdVehicleCertainAmount));//三者车财定损金额之和
        vPriceReqVo.setCertainAmount(String.valueOf(sumLossFee));//定损金额（不含施救费）
        vPriceReqVo.setCertainSumAmount(String.valueOf(NullToZero(lossCarMainVo.getSumLossFee()).add(NullToZero(lossCarMainVo.getSumRescueFee()))));//定损金额合计
        BigDecimal modificaCertainAmount =new BigDecimal(0);
        List<PrpLDlossCarMainHisVo> lossMainHisList = deflossService.findDeflossHisByMainId(lossCarMainVo.getId());
        if(lossMainHisList != null && lossMainHisList.size() > 0){
            PrpLDlossCarMainHisVo lossCarMainHisVo = lossMainHisList.get(lossMainHisList.size()-1);
            modificaCertainAmount=lossCarMainHisVo.getSumLossFee();
        }
        vPriceReqVo.setModificaCertainAmount(String.valueOf(modificaCertainAmount));//修改前定损金额
        vPriceReqVo.setRescueFeeAmount(String.valueOf(lossCarMainVo.getSumRescueFee()));//施救费金额
        
        List<PrpLClaimDeductVo> claimDeductVoList = new ArrayList<PrpLClaimDeductVo>();
        String deductCondCode = "";
        claimDeductVoList = registQueryService.findClaimDeductVoByRegistNo(registNo);// 获取该报案号下所有免赔条件
        if(claimDeductVoList != null && claimDeductVoList.size() > 0){
            for(PrpLClaimDeductVo vo : claimDeductVoList){
                if("1".equals(vo.getIsCheck())){
                    deductCondCode = deductCondCode + "," +vo.getDeductCondCode();
                }
            }
            if(deductCondCode.length()>0){
                deductCondCode = deductCondCode.substring(1,deductCondCode.length());//去除第一个逗号
            }
        }
        
        vPriceReqVo.setDeductCondCode(deductCondCode);//免赔条件
        vPriceReqVo.setDamageCode(prpLRegistVo.getDamageCode());//出险原因
        
        String lossPartyName="";
        String licenseNo="";
        PrpLWfTaskVo prpLWfTaskVo=wfFlowService.findPrpLWfTaskQueryByTaskId(taskId);
        String handlerId=prpLWfTaskVo.getHandlerIdKey();
        PrpLDlossCarMainVo vo=lossCarService.findLossCarMainById(Long.valueOf(handlerId));
        lossPartyName=vo.getDeflossCarType();
        licenseNo=vo.getLicenseNo();//损失方的车牌号
        
        vPriceReqVo.setLossPartyName(Integer.parseInt(lossPartyName)>1?"2":lossPartyName);//损失方 0  地面/路人损失  1  标的车  2  三者车

        
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
        vPriceReqVo.setSysAuthorizationFlag(isSysAuthorization?"1":"0");//是否系统授权
        
        vPriceReqVo.setEmployeeId(userVo.getUserCode());//员工工号
        
		if(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossCarMainVo.getDeflossSourceFlag())){
	        List<PrpLDlossCarMainVo> prpLDlossCarMains = deflossService.findLossCarMainBySerialNo(lossCarMainVo.getRegistNo(),lossCarMainVo.getSerialNo());
        	for(PrpLDlossCarMainVo prpLDlossCarMainVo : prpLDlossCarMains){
            	if(UnderWriteFlag.MANAL_UNDERWRITE.equals(prpLDlossCarMainVo.getUnderWriteFlag())){
            		
            		sumLossFee = sumLossFee
            				.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumLossFee()))
    						.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumChargeFee()))//费用
    		        		.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumSubRiskFee()))//附加险
    						.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumRescueFee()))//施救费
    						.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumOutFee()));//外修
    	        }
            }     
		}			
        
		if(CodeConstants.JyFlag.NOIN.equals(lossCarMainVo.getFlag())){//不走精友，同意核价提交核损 加上定损费用
			sumLossFee = sumLossFee
					.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))
					.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()));
		}else{
			if(lossCarMainVo.getSumVeripLoss()!=null 
					&& (CodeConstants.CetainLossType.DEFLOSS_REPAIR.equals(lossCarMainVo.getCetainLossType())
					|| CodeConstants.CetainLossType.DEFLOSS_AGREE.equals(lossCarMainVo.getCetainLossType()))){
				sumLossFee = sumLossFee
						.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))//费用
		        		.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()))//附加险
						.add(DataUtils.NullToZero(lossCarMainVo.getSumRescueFee()))//施救费
						.add(DataUtils.NullToZero(lossCarMainVo.getSumOutFee()));//外修
			}else{
				sumLossFee = sumLossFee
						.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))
						.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()));
			}
		}		
        
        vPriceReqVo.setSumAmount(String.valueOf(sumLossFee));//总定损金额
        
        vPriceReqVo.setSurveyFlag("0");//是否调查案件
        vPriceReqVo.setIsFlagN("0");//是否可疑交易
        
        
        
        //是否车物减损
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
        vPriceReqVo.setIsWhethertheloss(isWhethertheloss);//是否减损
        

        vPriceReqVo.setIsNucleardamagereturn("0");//是否核损退回案件
        PrpLWfTaskVo upperPrpLWfTaskVo=wfFlowService.findPrpLWfTaskQueryByTaskId(prpLWfTaskVo.getUpperTaskId());
        if(WorkStatus.BACK.equals(upperPrpLWfTaskVo.getWorkStatus())){   
            vPriceReqVo.setIsNucleardamagereturn("1");//是否核损退回案件
        }
        
        
        String urlStr=SpringProperties.getProperty("ILOG_SVR_URL");//获取ILOG规则服务地址
        urlStr=urlStr+"CheckRuleForCarVerifyLossServlet";//整合交互地址
        String requestXML=XstreamFactory.objToXml(vPriceReqVo);//请求报文转换xml
        String returnXml=earlyWarnService.requestSDEW(requestXML,urlStr);//推送ILOG
        System.out.println("******ILOG请求报文*******");
        System.out.println(requestXML);
        System.out.println("******ILOG返回报文*******");
        System.out.println(returnXml);
        lIlogRuleResVo= XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//返回报文转换vo
        
        IlogDataProcessingVo ilogDataProcessingVo =new IlogDataProcessingVo();
        ilogDataProcessingVo.setBusinessNo(vPriceReqVo.getRegistNo());//业务号 报案号
        ilogDataProcessingVo.setCompensateNo("");//计算书号   
        ilogDataProcessingVo.setComCode(vPriceReqVo.getComCode());//业务归属机构
        ilogDataProcessingVo.setRiskCode(vPriceReqVo.getRiskCode());//险种
        ilogDataProcessingVo.setOperateType(vPriceReqVo.getOperateType());//操作类型  1：自动  2：人工权限
        ilogDataProcessingVo.setRuleType(vPriceReqVo.getTaskType());//任务类型 0：车辆; 1：财产
        ilogDataProcessingVo.setRuleNode("VLCar");//任务节点
        ilogDataProcessingVo.setLossParty(vPriceReqVo.getLossPartyName());//损失方
        ilogDataProcessingVo.setLicenseNo(licenseNo);//损失方的车牌号
        ilogDataProcessingVo.setTriggerNode(triggerNode);//触发节点  提交到任务节点的前节点
        ilogDataProcessingVo.setTaskId(taskId);//触发节点对应的工作流ID 
        ilogDataProcessingVo.setOperatorCode(userVo.getUserCode());//操作人员
        
        ruleReturnDataSaveService.dealILogResReturnData(lIlogRuleResVo,ilogDataProcessingVo);//规则保存
                
        return lIlogRuleResVo;
      }
    
    /**
     * 查询上一个节点，如果是定损节点就返回
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
        List<PrpLCMainVo> prpLCMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(registNo);//保单信息主表
        vPriceReqVo.setRegistNo(registNo);//报案号
        vPriceReqVo.setOperateType(operateType);//操作类型1：自动   2：人工权限
        vPriceReqVo.setRiskCode(lossCarMainVo.getRiskCode());
        vPriceReqVo.setComCode(lossCarMainVo.getMakeCom());
        vPriceReqVo.setTaskType("0");//0：车辆  1：财产
        
        
        List<PrpLDlossCarCompVo> dlossCarCompVoList = lossCarMainVo.getPrpLDlossCarComps();//换件项目清单
        //标准
        BigDecimal jystandardSingleFittingsHighAmount = new BigDecimal(0);
        BigDecimal jystandardFittingsSumAmount = new BigDecimal(0);
        //非标准
        BigDecimal jyNonstandardSingleFittingsHighAmount = new BigDecimal(0);
        BigDecimal jyNonstandardFittingsSumAmount = new BigDecimal(0);
        //总
        BigDecimal jyFittingsSumAmount = new BigDecimal(0);
        int replaceNum = 0;//零部件更换次数合计
        List<Accessories> accessoriesList = new ArrayList<Accessories>();
        for(PrpLDlossCarCompVo vo : dlossCarCompVoList){
            Accessories accessories = new Accessories();
            // 0-系统配件  1-自定义配件 2-标准配件
            if("2".equals(vo.getSelfConfigFlag())){//标准
                if(jystandardSingleFittingsHighAmount.compareTo(DataUtils.NullToZero(vo.getMaterialFee()))<0){//取最高价格
                    jystandardSingleFittingsHighAmount = DataUtils.NullToZero(vo.getMaterialFee());
                }
                jystandardFittingsSumAmount = jystandardFittingsSumAmount.add(DataUtils.NullToZero(vo.getMaterialFee()));//汇总
                accessories.setIsStandard("1");//是否标准点选
            }else{
                if(jyNonstandardSingleFittingsHighAmount.compareTo(DataUtils.NullToZero(vo.getMaterialFee()))<0){//取最高价格
                    jyNonstandardSingleFittingsHighAmount = DataUtils.NullToZero(vo.getMaterialFee());
                }
                jyNonstandardFittingsSumAmount = jyNonstandardFittingsSumAmount.add(DataUtils.NullToZero(vo.getMaterialFee()));//汇总
                accessories.setIsStandard("0");//是否标准点选
            }
            if(vo.getReplaceNum()!=null){
                replaceNum += vo.getReplaceNum();
            }     
            accessories.setCompCode(vo.getCompCode());//配件代码
            accessories.setCompName(vo.getCompName());//配件名称
            if(vo.getChgRefPrice()!=null){
                accessories.setChgrefPrice(String.valueOf(vo.getChgRefPrice()));//系统价格
            }
            if(vo.getSumDefLoss()!=null){
                accessories.setSumdefLoss(String.valueOf(vo.getSumDefLoss()));//定损价格
            }
            if(vo.getChgLocPrice()!=null){
                accessories.setChglocPrice(String.valueOf(vo.getChgLocPrice()));//本地价格

            }
            if(vo.getQuantity()!=null){
                accessories.setQuantity(String.valueOf(vo.getQuantity()));//数量
            }
            accessoriesList.add(accessories);
        }
        
        vPriceReqVo.setAccessoriesList(accessoriesList); ////配件信息

        
        jyFittingsSumAmount = jystandardFittingsSumAmount.add(jyNonstandardFittingsSumAmount);
        //精友系统
        vPriceReqVo.setJystandardSingleFittingsHighAmount(String.valueOf(jystandardSingleFittingsHighAmount));//精友系统标准点选单个配件最高金额
        vPriceReqVo.setJyNonstandardSingleFittingsHighAmount(String.valueOf(jyNonstandardSingleFittingsHighAmount));//精友系统非标准点选单个配件最高金额
        vPriceReqVo.setJystandardFittingsSumAmount(String.valueOf(jystandardFittingsSumAmount));//精友系统标准点选配件更换总金额
        vPriceReqVo.setJyNonstandardFittingsSumAmount(String.valueOf(jyNonstandardFittingsSumAmount));//精友系统非标准点选配件更换总金额
        vPriceReqVo.setJyFittingsSumAmount(String.valueOf(jyFittingsSumAmount)); //精友系统点选配件更换总金额
        
        String coverageFlag = "0";//是否承保机动车损失保险
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
        //标的车定损金额
        if("1".equals(lossCarMainVo.getDeflossCarType())){
            if(lossCarMainVo.getSumLossFee() != null){
                vPriceReqVo.setMarkCarAmount(String.valueOf(lossCarMainVo.getSumLossFee()));
            }
        }else{
            vPriceReqVo.setMarkCarAmount("");
        }
        vPriceReqVo.setCarAmount(carAmount);//车损险保额
        vPriceReqVo.setFittingsChangeSumNum(String.valueOf(replaceNum));//零部件更换次数合计 
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
        vPriceReqVo.setThirdVehicleCertainAmount(String.valueOf(thirdVehicleCertainAmount));//三者车财定损金额之和
        vPriceReqVo.setCertainAmount(String.valueOf(sumLossFee));//定损金额（不含施救费）
        vPriceReqVo.setCertainSumAmount(String.valueOf(NullToZero(lossCarMainVo.getSumLossFee()).add(NullToZero(lossCarMainVo.getSumRescueFee()))));//定损金额合计
        BigDecimal modificaCertainAmount =new BigDecimal(0);
        List<PrpLDlossCarMainHisVo> lossMainHisList = deflossService.findDeflossHisByMainId(lossCarMainVo.getId());
        if(lossMainHisList != null && lossMainHisList.size() > 0){
            PrpLDlossCarMainHisVo lossCarMainHisVo = lossMainHisList.get(lossMainHisList.size()-1);
            modificaCertainAmount=DataUtils.NullToZero(lossCarMainHisVo.getSumLossFee());
        }
        vPriceReqVo.setModificaCertainAmount(String.valueOf(modificaCertainAmount));//修改前定损金额
        vPriceReqVo.setRescueFeeAmount(String.valueOf(DataUtils.NullToZero(lossCarMainVo.getSumRescueFee())));//施救费金额
        
		if(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossCarMainVo.getDeflossSourceFlag())){
	        List<PrpLDlossCarMainVo> prpLDlossCarMains = deflossService.findLossCarMainBySerialNo(lossCarMainVo.getRegistNo(),lossCarMainVo.getSerialNo());
        	for(PrpLDlossCarMainVo prpLDlossCarMainVo : prpLDlossCarMains){
            	if(UnderWriteFlag.MANAL_UNDERWRITE.equals(prpLDlossCarMainVo.getUnderWriteFlag())){
            		
            		sumLossFee = sumLossFee
            				.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumLossFee()))
    						.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumChargeFee()))//费用
    		        		.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumSubRiskFee()))//附加险
    						.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumRescueFee()))//施救费
    						.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumOutFee()));//外修
    	        }
            }     
		}			
        
		if(CodeConstants.JyFlag.NOIN.equals(lossCarMainVo.getFlag())){//不走精友，同意核价提交核损 加上定损费用
			sumLossFee = sumLossFee
					.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))
					.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()));
		}else{
			if(lossCarMainVo.getSumVeripLoss()!=null 
					&& (CodeConstants.CetainLossType.DEFLOSS_REPAIR.equals(lossCarMainVo.getCetainLossType())
					|| CodeConstants.CetainLossType.DEFLOSS_AGREE.equals(lossCarMainVo.getCetainLossType()))){
				sumLossFee = sumLossFee
						.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))//费用
		        		.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()))//附加险
						.add(DataUtils.NullToZero(lossCarMainVo.getSumRescueFee()))//施救费
						.add(DataUtils.NullToZero(lossCarMainVo.getSumOutFee()));//外修
			}else{
				sumLossFee = sumLossFee
						.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))
						.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()));
			}
		}		
        
        vPriceReqVo.setSumAmount(String.valueOf(sumLossFee));//总定损金额
        
        String urlStr=SpringProperties.getProperty("ILOG_SVR_URL");//获取ILOG规则服务地址
        urlStr=urlStr+"CheckRuleForCarVerifyLossServlet";//整合交互地址
        String requestXML=XstreamFactory.objToXml(vPriceReqVo);//请求报文转换xml
        String returnXml=earlyWarnService.requestSDEW(requestXML,urlStr);//推送ILOG
        logger.info("******ILOG核损请求报文*******");
        logger.info(requestXML);
        logger.info("******ILOG核损返回报文*******");
        logger.info(returnXml);
        lIlogRuleResVo= XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//返回报文转换vo
        
        return lIlogRuleResVo;
      }



	@Override
	public LIlogRuleResVo organizaOldOrganizaVprice(PrpLDlossCarMainVo lossCarMainVo,String operateType) throws Exception {
        VPriceReqVo vPriceReqVo = new VPriceReqVo();
        LIlogRuleResVo lIlogRuleResVo = new LIlogRuleResVo();
        String registNo = lossCarMainVo.getRegistNo();
        List<PrpLCMainVo> prpLCMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(registNo);
        vPriceReqVo.setRegistNo(registNo);
        vPriceReqVo.setOperateType(operateType);//操作类型
        vPriceReqVo.setRiskCode(lossCarMainVo.getRiskCode());
        vPriceReqVo.setComCode(lossCarMainVo.getMakeCom());
        vPriceReqVo.setTaskType("0");
        List<PrpLDlossCarCompVo> dlossCarCompVoList = lossCarMainVo.getPrpLDlossCarComps();
        //标准
        BigDecimal jystandardSingleFittingsHighAmount = new BigDecimal(0);
        BigDecimal jystandardFittingsSumAmount = new BigDecimal(0);
        //非标准
        BigDecimal jyNonstandardSingleFittingsHighAmount = new BigDecimal(0);
        BigDecimal jyNonstandardFittingsSumAmount = new BigDecimal(0);
        //总
        BigDecimal jyFittingsSumAmount = new BigDecimal(0);
        int replaceNum = 0;//零部件更换次数合计
        List<Accessories> accessoriesList = new ArrayList<Accessories>();
        for(PrpLDlossCarCompVo vo : dlossCarCompVoList){
            Accessories accessories = new Accessories();
            accessories.setIsStandard("0");
            // 0-系统配件  1-自定义配件 2-标准配件
            if("2".equals(vo.getSelfConfigFlag())){//标准
                if(jystandardSingleFittingsHighAmount.compareTo(DataUtils.NullToZero(vo.getMaterialFee()))<0){//取最高价格
                    jystandardSingleFittingsHighAmount = DataUtils.NullToZero(vo.getMaterialFee());
                }
                jystandardFittingsSumAmount = jystandardFittingsSumAmount.add(DataUtils.NullToZero(vo.getMaterialFee()));//汇总
                accessories.setIsStandard("1");
            }else{
                if(jyNonstandardSingleFittingsHighAmount.compareTo(DataUtils.NullToZero(vo.getMaterialFee()))<0){//取最高价格
                    jyNonstandardSingleFittingsHighAmount = DataUtils.NullToZero(vo.getMaterialFee());
                }
                jyNonstandardFittingsSumAmount = jyNonstandardFittingsSumAmount.add(DataUtils.NullToZero(vo.getMaterialFee()));//汇总
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
        //配件信息
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
    						.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumChargeFee()))//费用
    		        		.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumSubRiskFee()))//附加险
    						.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumRescueFee()))//施救费
    						.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumOutFee()));//外修
    	        }
            }     
		}			
        
		if(CodeConstants.JyFlag.NOIN.equals(lossCarMainVo.getFlag())){//不走精友，同意核价提交核损 加上定损费用
			sumLossFee = sumLossFee
					.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))
					.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()));
		}else{
			if(lossCarMainVo.getSumVeripLoss()!=null 
					&& (CodeConstants.CetainLossType.DEFLOSS_REPAIR.equals(lossCarMainVo.getCetainLossType())
					|| CodeConstants.CetainLossType.DEFLOSS_AGREE.equals(lossCarMainVo.getCetainLossType()))){
				sumLossFee = sumLossFee
						.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))//费用
		        		.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()))//附加险
						.add(DataUtils.NullToZero(lossCarMainVo.getSumRescueFee()))//施救费
						.add(DataUtils.NullToZero(lossCarMainVo.getSumOutFee()));//外修
			}else{
				sumLossFee = sumLossFee
						.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))
						.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()));
			}
		}
        vPriceReqVo.setSumAmount(String.valueOf(sumLossFee));//总定损金额
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
        

        String urlStr = SpringProperties.getProperty("ILOG_SVR_URL");//获取ILOG规则服务地址
        urlStr = urlStr + CheckRuleForCarVerifyPriceServlet;//整合交互地址
        String requestXML = XstreamFactory.objToXml(vPriceReqVo);//请求报文转换xml
        logger.info("核价请求报文转换xml============" + requestXML);
        String returnXml = earlyWarnService.requestSDEW(requestXML,urlStr);//推送ILOG
        logger.info("核价返回报文转换xml============" + returnXml);
        lIlogRuleResVo = XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//返回报文转换vo

        return lIlogRuleResVo;
    }



	@Override
	public LIlogRuleResVo organizaOldVProperty(PrpLdlossPropMainVo lossPropMainVo,String operateType) throws Exception {
        VPriceReqVo vPriceReqVo = new VPriceReqVo();
        LIlogRuleResVo lIlogRuleResVo = new LIlogRuleResVo();
        String registNo = lossPropMainVo.getRegistNo();
        List<PrpLCMainVo> prpLCMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(registNo);//保单信息主表
        vPriceReqVo.setRegistNo(registNo);//报案号
        vPriceReqVo.setOperateType(operateType);//操作类型1：自动   2：人工权限
        vPriceReqVo.setRiskCode(lossPropMainVo.getRiskCode());
        vPriceReqVo.setComCode(lossPropMainVo.getMakeCom());
        vPriceReqVo.setTaskType("1");//0：车辆  1：财产
        vPriceReqVo.setRepairFactoryType("");//修理厂类型
        
        //标准
        int jyStandardFittingsNum = 0;
        BigDecimal jystandardSingleFittingsHighAmount = new BigDecimal(0);
        BigDecimal jystandardFittingsSumAmount = new BigDecimal(0);
        //非标准
        int jyNonstandardFittingsNum = 0;
        BigDecimal jyNonstandardSingleFittingsHighAmount = new BigDecimal(0);
        BigDecimal jyNonstandardFittingsSumAmount = new BigDecimal(0);
        //总
        BigDecimal jyFittingsSumAmount = new BigDecimal(0);
        
        jyFittingsSumAmount = jystandardFittingsSumAmount.add(jyNonstandardFittingsSumAmount);
        //精友系统
        vPriceReqVo.setJyStandardFittingsNum(String.valueOf(jyStandardFittingsNum));//精友系统标准点选配件个数
        vPriceReqVo.setJystandardSingleFittingsHighAmount(String.valueOf(jystandardSingleFittingsHighAmount));//精友系统标准点选单个配件最高金额
        vPriceReqVo.setJyNonstandardFittingsNum(String.valueOf(jyNonstandardFittingsNum));//精友系统非标准点选配件个数
        vPriceReqVo.setJyNonstandardSingleFittingsHighAmount(String.valueOf(jyNonstandardSingleFittingsHighAmount));//精友系统非标准点选单个配件最高金额
        vPriceReqVo.setJystandardFittingsSumAmount(String.valueOf(jystandardFittingsSumAmount));//精友系统标准点选配件更换总金额
        vPriceReqVo.setJyNonstandardFittingsSumAmount(String.valueOf(jyNonstandardFittingsSumAmount));//精友系统非标准点选配件更换总金额
        vPriceReqVo.setJyFittingsSumAmount(String.valueOf(jyFittingsSumAmount)); //精友系统点选配件更换总金额
        
        
        String coverageFlag = "0";//是否承保机动车损失保险
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
        //财产定损金额
        vPriceReqVo.setMarkCarAmount("0");
        
        List<PrpLDlossCarMainVo> lossCarMainList = lossCarService.findLossCarMainByRegistNo(registNo);
        for(PrpLDlossCarMainVo  lossCarMainVo:lossCarMainList){
            if("1".equals(lossCarMainVo.getDeflossCarType())){
                if(lossCarMainVo.getSumLossFee() != null){
                    vPriceReqVo.setMarkCarAmount(String.valueOf(lossCarMainVo.getSumLossFee()));
                }
            }
        }
        
        vPriceReqVo.setCarAmount(carAmount);//车损险保额
           
        
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
        vPriceReqVo.setThirdVehicleCertainAmount(String.valueOf(thirdVehicleCertainAmount));//三者车财定损金额之和
        vPriceReqVo.setCertainAmount(String.valueOf(sumDefLoss));//定损金额（不含施救费）
        vPriceReqVo.setCertainSumAmount(String.valueOf(DataUtils.NullToZero(lossPropMainVo.getSumDefloss()).add(DataUtils.NullToZero(lossPropMainVo.getDefRescueFee()))));//定损金额合计
        BigDecimal ModificaCertainAmount = new BigDecimal("0");
        PrpLdlossPropMainHisVo lossMainHisvo = propLossService.findPropHisByPropMainId(lossPropMainVo.getId());
        if(lossMainHisvo != null){
        	ModificaCertainAmount=lossMainHisvo.getSumDefLoss();
        }
        vPriceReqVo.setModificaCertainAmount(String.valueOf(ModificaCertainAmount));//修改前定损金额
        vPriceReqVo.setRescueFeeAmount(String.valueOf(DataUtils.NullToZero(lossPropMainVo.getDefRescueFee())));//施救费金额
        
		if(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossPropMainVo.getDeflossSourceFlag())){
			List<PrpLdlossPropMainVo> prpLdlossPropMainVos = propLossService.findLossPropBySerialNo(registNo, lossPropMainVo.getSerialNo());
	    	for(PrpLdlossPropMainVo prpLdlossPropMainVo : prpLdlossPropMainVos){
            	if(UnderWriteFlag.MANAL_UNDERWRITE.equals(prpLdlossPropMainVo.getUnderWriteFlag())){
            		sumDefLoss = sumDefLoss
    	    				.add(DataUtils.NullToZero(prpLdlossPropMainVo.getSumDefloss()))
    	            		.add(DataUtils.NullToZero(prpLdlossPropMainVo.getDefRescueFee()))//施救费用
    	            		.add(DataUtils.NullToZero(prpLdlossPropMainVo.getSumLossFee()));//费用
            	}
			}
		}

	    sumDefLoss = sumDefLoss
	    		.add(DataUtils.NullToZero(lossPropMainVo.getDefRescueFee()))//施救费用
	    		.add(DataUtils.NullToZero(lossPropMainVo.getSumLossFee()));//费用
        
        vPriceReqVo.setSumAmount(String.valueOf(sumDefLoss));//总定损金额
        
        String urlStr=SpringProperties.getProperty("ILOG_SVR_URL");//获取ILOG规则服务地址
        urlStr=urlStr+"CheckRuleForCarVerifyLossServlet";//整合交互地址
        String requestXML=XstreamFactory.objToXml(vPriceReqVo);//请求报文转换xml
        String returnXml=earlyWarnService.requestSDEW(requestXML,urlStr);//推送ILOG
        logger.info("******ILOG财核损请求报文*******");
        logger.info(requestXML);
        logger.info("******ILOG财核损返回报文*******");
        logger.info(returnXml);
        lIlogRuleResVo= XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//返回报文转换vo
        
        return lIlogRuleResVo;
      }
 
}
