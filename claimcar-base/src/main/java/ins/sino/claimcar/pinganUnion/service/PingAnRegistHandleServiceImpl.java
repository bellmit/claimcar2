package ins.sino.claimcar.pinganUnion.service;

import com.alibaba.fastjson.JSON;
import ins.framework.service.CodeTranService;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.service.facade.PingAnDictService;
import ins.platform.vo.PiccCodeDictVo;
import ins.platform.vo.SysAreaDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.flow.constant.SubmitType;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.pinganUnion.dto.*;
import ins.sino.claimcar.pinganUnion.enums.PingAnCodeTypeEnum;
import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import ins.sino.claimcar.regist.service.*;
import ins.sino.claimcar.regist.vo.*;
import ins.sino.claimcar.schedule.vo.PrpLScheduleItemsVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * @Description 平安联盟-案件基本信息查询接口业务数据处理入口
 * @Author liuys
 * @Date 2020/7/21 8:52
 */
@Service("pingAnRegistHandleService")
public class PingAnRegistHandleServiceImpl implements PingAnHandleService {
    private static Logger logger = LoggerFactory.getLogger(PingAnRegistHandleServiceImpl.class);

    @Autowired
    private PrpLCMainService prpLCMainService;
    @Autowired
    private CodeTranService codeTranService;
    @Autowired
    private WfTaskHandleService wfTaskHandleService;
    @Autowired
    private RegistService registService;
    @Autowired
    private ScheduleTaskService scheduleTaskService;
    @Autowired
    private PolicyViewService policyViewService;
    @Autowired
    private ScheduleHandlerService scheduleHandlerService;
    @Autowired
    private RegistHandlerService registHandlerService;
    @Autowired
    private PingAnDictService pingAnDictService;
    @Autowired
    private AreaDictService areaDictService;

    @Override
    public ResultBean pingAnHandle(String registNo, PingAnDataNoticeVo pingAnDataNoticeVo, String respData) {
        logger.info("平安联盟-案件基本信息查询接口业务数据处理入口--registNo={},respData={}", registNo,respData);

        ResultBean resultBean = ResultBean.success();
        try {
            //解析json字符串
            ReportRespData reportRespData = JSON.parseObject(respData,ReportRespData.class);
            //基本校验
            checkData(reportRespData);

            //查询报案号是否已存在,已报案成功不再重复报案
            PrpLRegistVo prpLRegistVo = registService.findRegistByPaicReportNo(reportRespData.getReportInfoDTO().getReportNo());
            if (prpLRegistVo == null){
                //自动报案操作
                autoRegist(reportRespData);
            }else {
                resultBean.success("已报案成功不要重复报案操作",null);
            }
        }catch (Exception e){
            logger.error("平安联盟-案件基本信息查询接口业务数据处理报错：registNo={},error={}", registNo, ExceptionUtils.getStackTrace(e));
            resultBean = resultBean.fail(e.getMessage());
        }

        return resultBean;
    }

    /**
     * 自动报案操作
     */
    private void autoRegist(ReportRespData reportRespData) throws ParseException {
        ReportInfoDTO reportInfoDTO = reportRespData.getReportInfoDTO();
        ReportAccidentDTO reportAccidentDTO = reportRespData.getReportAccidentDTO();
        WholeCaseBaseDTO wholeCaseBaseDTO = reportRespData.getWholeCaseBaseDTO();
        List<CaseBaseDTO> caseBaseDTOList = reportRespData.getCaseBaseDTOList();
        List<PolicyInfoDTO> policyInfoDTOList = reportRespData.getPolicyInfoDTOList();

        //创建默认用户
        SysUserVo userVo = new SysUserVo();
        userVo.setUserCode("AUTO");
        userVo.setUserName("AUTO");
        //对象定义
        String BIPolicyNo = null;
        String CIPolicyNo = null;
        List<PrpLCMainVo> prpLCMains = null;

        //从承保获取保单数据
        prpLCMains = convertPolicyInfos(reportInfoDTO, caseBaseDTOList, policyInfoDTOList);
        //获取交强、商品保单号
        if(prpLCMains!=null&&prpLCMains.size()>0){
            for(PrpLCMainVo prpLCMainVo:prpLCMains){
                if(StringUtils.isNotBlank(prpLCMainVo.getRiskCode())){
                    if("1101".equals(prpLCMainVo.getRiskCode())){
                        CIPolicyNo = prpLCMainVo.getPolicyNo();
                    } else{
                        BIPolicyNo = prpLCMainVo.getPolicyNo();
                    }
                }
            }
        }else {
            throw new IllegalArgumentException("查询保单数据不存在");
        }

        //转换报案数据
        PrpLRegistVo prpLRegistVo = convertRegistInfos(reportInfoDTO,reportAccidentDTO,wholeCaseBaseDTO,policyInfoDTOList,prpLCMains);
        //给报案信息表赋值保单号，主表优先保存商业保单号，如果没有商业险保单则存交强险保单号，关联报案时，报案扩展表存储交强保单，否则不存
        if (!StringUtils.isEmpty(BIPolicyNo)) {
            prpLRegistVo.setPolicyNo(BIPolicyNo);
            if (!StringUtils.isEmpty(CIPolicyNo)) {
                prpLRegistVo.getPrpLRegistExt().setPolicyNoLink(CIPolicyNo);
            }
        } else {
            prpLRegistVo.setPolicyNo(CIPolicyNo);
        }
        prpLRegistVo = registHandlerService.save(prpLRegistVo,prpLCMains,true,BIPolicyNo,CIPolicyNo);
        //提交工作流
        submitWf(prpLRegistVo,userVo);
    }

    /**
     * 校验数据是否合法
     */
    private void checkData(ReportRespData reportRespData) {
        if (reportRespData == null){
            throw new IllegalArgumentException("解析返回报文内容为空");
        }
        if (reportRespData.getReportInfoDTO() == null){
            throw new IllegalArgumentException("案件基本信息reportInfoDTO为空");
        }

        if (reportRespData.getReportAccidentDTO() == null){
            throw new IllegalArgumentException("案件事故信息reportAccidentDTO为空");
        }
        if (reportRespData.getWholeCaseBaseDTO() == null){
            throw new IllegalArgumentException("整案基本信息wholeCaseBaseDTO为空");
        }

        if (CollectionUtils.isEmpty(reportRespData.getCaseBaseDTOList())){
            throw new IllegalArgumentException("保单案件信息caseBaseDTOList为空");
        }

        if (CollectionUtils.isEmpty(reportRespData.getPolicyInfoDTOList())){
            throw new IllegalArgumentException("保单信息policyInfoDTOList为空");
        }

        //reportInfoDTO对象字段校验
        if (StringUtils.isBlank(reportRespData.getReportInfoDTO().getReportNo())){
            throw new IllegalArgumentException("案件号为空");
        }
        if (reportRespData.getReportInfoDTO().getReportDate() == null){
            throw new IllegalArgumentException("报案时间为空");
        }
    }

    /**
     * 转换保单数据
     * @param reportInfoDTO
     * @param caseBaseDTOList
     * @param policyInfoDTOList
     * @return
     */
    private List<PrpLCMainVo> convertPolicyInfos(ReportInfoDTO reportInfoDTO,
                                                 List<CaseBaseDTO> caseBaseDTOList,
                                                 List<PolicyInfoDTO> policyInfoDTOList) throws ParseException {
        List<PrpLCMainVo> prpLCMainVoList = new ArrayList<PrpLCMainVo>();

       for (CaseBaseDTO dto : caseBaseDTOList){
           // 调用存储过程
           if(StringUtils.isNotBlank(dto.getPolicyNo())){
               String tempPolicyNo = CodeConstants.PINGAN_UNION.POLICY_PREFIX + dto.getPolicyNo();//承保出单规则：投保+T  保单+B  批单+P
               PrpLCMainVo prpLCMainVo = prpLCMainService.findRegistPolicy(tempPolicyNo,reportInfoDTO.getReportDate());
               if(prpLCMainVo!=null){
                   // 业务分类
                   business(prpLCMainVo);
                   prpLCMainVo.setClaimNo(dto.getCaseNo());//赔案号
                   prpLCMainVoList.add(prpLCMainVo);
               }else{
            	   throw new IllegalArgumentException("保单"+dto.getPolicyNo()+"数据在承保系统不存在！");
               }
           }
       }

        return prpLCMainVoList;
    }

    /**
     * 在报案、调度页面保单信息增加显示业务板块、 业务分类，若业务板块为会员业务，业务分类为“ 30-南网员工、31-员工亲属、34-浦发员工、35-农行员工、 36-深圳海关员工”这5类客户，则业务板块和业务分类显示的值标红
     * @param prpLCMainVo
     * @author huanggusheng
     */
    public void business(PrpLCMainVo prpLCMainVo) {
        Map<String,String> businessClassCheckMsg = codeTranService.findCodeNameMap("BusinessClassCheckMsg");
        if(businessClassCheckMsg.containsKey(prpLCMainVo.getBusinessClass())){
            prpLCMainVo.setMemberFlag("1");
        }
    }

    /**
     * 转换报案信息
     * @param reportInfoDTO
     * @param reportAccidentDTO
     * @param policyInfoDTOList
     * @return
     */
    private PrpLRegistVo convertRegistInfos(ReportInfoDTO reportInfoDTO,
                                            ReportAccidentDTO reportAccidentDTO,
                                            WholeCaseBaseDTO wholeCaseBaseDTO,
                                            List<PolicyInfoDTO> policyInfoDTOList,
                                            List<PrpLCMainVo> prpLCMains
                                            ) {
        PrpLRegistVo prpLRegistVo = new PrpLRegistVo();
        PrpLRegistExtVo prpLRegistExt = new PrpLRegistExtVo();
        PrpLCMainVo prpLCMainVo = prpLCMains.get(0);
        PrpLCItemCarVo prpLCItemCarVo = prpLCMainVo.getPrpCItemCars().get(0);
        //====================【主表信息设置】==============================
        //prpLRegistVo.setRegistNo(reportInfoDTO.getReportNo());//报案号
        prpLRegistVo.setPaicReportNo(reportInfoDTO.getReportNo());//平安联盟案件号
        prpLRegistVo.setReportorName(reportInfoDTO.getReporterName());// 报案人姓名
        prpLRegistVo.setReportTime(reportInfoDTO.getReportDate());//报案时间
        prpLRegistVo.setReportorPhone(reportInfoDTO.getReporterCallNo());//报案人电话
        prpLRegistVo.setLinkerName(reportInfoDTO.getReporterName());//联系人默认报案人
        prpLRegistVo.setLinkerMobile(reportInfoDTO.getReporterCallNo());//联系人手机默认报案人电话
        prpLRegistVo.setLinkerPhone(reportInfoDTO.getReporterRegisterTel());//联系人手机默认报案人登记号码
        prpLRegistVo.setDriverName(reportInfoDTO.getDriverName());//驾驶员
        prpLRegistVo.setRemark(reportInfoDTO.getRemark());//备注
        prpLRegistVo.setDriverSex(convertSex(reportInfoDTO.getDriverSex()));//驾驶员性别 需编码转换
        prpLRegistVo.setDriverIdfNo(reportInfoDTO.getDriveCardId());//驾驶员身份证号码
        prpLRegistVo.setSubmitSource(reportInfoDTO.getReportMode());//报案来源  1:95511电话报案 8:好车主APP报案
        String damageCode = convertDHCode(PingAnCodeTypeEnum.CLAIMREASON,reportAccidentDTO.getAccidentCauseLevel1());
        prpLRegistVo.setDamageCode(damageCode);
        if (StringUtils.isBlank(damageCode)) {
            prpLRegistVo.setDamageCode("DM99");//出险原因代码 需编码转换 TODO 映射不上默认DM99-其他 还有两个字段没用到 accidentCauseLevel2、accidentCauseLevel3
            prpLRegistVo.setDamageOtherCode("DM70");//DM70-其他意外事故
        }
        prpLRegistVo.setDamageAreaCode(reportAccidentDTO.getAccidentCityCode());//出险区域编码
        prpLRegistVo.setDamageAddress(reportAccidentDTO.getAccidentPlace());//出险地点
        prpLRegistVo.setDamageTime(reportAccidentDTO.getAccidentDate());//出险时间
        if (StringUtils.isNotBlank(reportAccidentDTO.getDamagePlaceGpsX()) || StringUtils.isNotBlank(reportAccidentDTO.getDamagePlaceGpsY())) {
            prpLRegistVo.setDamageMapCode(reportAccidentDTO.getDamagePlaceGpsX() + "," + reportAccidentDTO.getDamagePlaceGpsY());//出险地图编码
        }
        //设置机构及险种代码
        if(prpLCMains!=null&&prpLCMains.size()>0){
            for(PrpLCMainVo prpLCMain:prpLCMains){
                prpLRegistVo.setComCode(prpLCMain.getComCode());
                prpLRegistVo.setRiskCode(prpLCMain.getRiskCode());
                if( !"1101".equals(prpLCMain.getRiskCode())){
                    break;
                }
            }
        }
        //设置报案类型
        String reportType = null;
        if (prpLCMains.size()>1){
            reportType = "3";
        }else {
            if( "1101".equals(prpLCMains.get(0).getRiskCode())){
                reportType = "2";
            }else {
                reportType = "1";
            }
        }
        prpLRegistVo.setReportType(reportType);// 报案类型 1-单商业，2-单交强，3-关联报案
        prpLRegistVo.setIsMajorCase("Y".equals(wholeCaseBaseDTO.getIsImportantCase()) ? "1" : "0");//是否重大案件
        //默认赋值 TODO 需确认
        /*prpLRegistVo.setFirstRegUserCode(userVo.getUserCode());//初登人员代码
        prpLRegistVo.setFirstRegUserName(userVo.getUserCode());//初登座席姓名
        prpLRegistVo.setFirstRegComCode(prpLRegistVo.getComCode());//初登座席归属机构*/
        prpLRegistVo.setIsQuickCase("0");//是否快赔案件 默认“否”
        prpLRegistVo.setSelfClaimFlag("0");//是否自助理赔案件 默认“否”
        prpLRegistVo.setSelfRegistFlag("1");//设置是否自助报案案件为1，表示自助报案案件
        prpLRegistVo.setTempRegistFlag("0");//是否临时报案标志 默认“否”
        prpLRegistVo.setMercyFlag("0");//是否紧急案件 默认“一般”
        prpLRegistVo.setIsPeopleflag("0");//调度是否带出人伤标志 默认"否"

        //==================【扩展表信息】===========================
        //prpLRegistExt.setRegistNo(reportInfoDTO.getReportNo());//报案号
        prpLRegistExt.setIsPropLoss("Y".equals(reportInfoDTO.getIsCargoLoss()) ? "1" : "0");//是否有物损
        prpLRegistExt.setIsCarLoss("Y".equals(reportInfoDTO.getIsCarLoss()) ? "1" : "0");//是否有车损
        prpLRegistExt.setIsPersonLoss("Y".equals(reportInfoDTO.getIsInjured()) ? "1" : "0");//是否有人伤
        prpLRegistExt.setIsOnSitReport("Y".equals(reportInfoDTO.getReportOnPort()) ? "1" : "0");//是否现场报案
        prpLRegistExt.setIsSubRogation("Y".equals(reportInfoDTO.getIsAgentCase()) ? "1" : "0");//是否代位案件
        prpLRegistExt.setDangerRemark(reportAccidentDTO.getAccidentDetail());//出险经过
        prpLRegistExt.setAccidentTypes(convertDHCode(PingAnCodeTypeEnum.SGLX,reportAccidentDTO.getAccidentType()));//事故类型  需编码转换
        String obliGation = convertDHCode(PingAnCodeTypeEnum.SGZRRD,reportAccidentDTO.getAccidentResponsibility());
        prpLRegistExt.setObliGation(StringUtils.isNotBlank(obliGation) ? obliGation:"0");//事故责任划分 需编码转换 TODO 如果映射不上默认0-全责
        prpLRegistExt.setCheckAddressCode(prpLRegistVo.getDamageAreaCode());// 查勘区域编码 取出险区域编码
        prpLRegistExt.setCheckAddress(reportAccidentDTO.getAccidentPlace());// 查勘地址
        prpLRegistExt.setCheckAddressMapCode(reportAccidentDTO.getDamagePlaceGpsX()+","+reportAccidentDTO.getDamagePlaceGpsY());// 查勘地区地图编号
        prpLRegistExt.setRegistRemark(reportInfoDTO.getRemark());//报案备注
        prpLRegistExt.setRemark(reportInfoDTO.getRemark());//备注
        String licenseNo = prpLCItemCarVo.getLicenseNo();
        if (StringUtils.isBlank(licenseNo)){
            licenseNo = policyInfoDTOList.get(0).getCarMark();
        }
        prpLRegistExt.setLicenseNo(licenseNo);//车牌号
        prpLRegistExt.setFrameNo(prpLCItemCarVo.getFrameNo());//车架号
        prpLRegistExt.setInsuredName(prpLCMainVo.getInsuredName());//被保险人姓名
        prpLRegistExt.setInsuredCode(prpLCMainVo.getInsuredCode());//被保险人代码
        //默认赋值 TODO 需确认
        prpLRegistExt.setManageType("1");//事故处理类型 默认“保险公司处理”
        prpLRegistExt.setIsClaimSelf("0");//是否互碰自赔 默认“否”
        prpLRegistExt.setIsCheckSelf("0");//是否自助查勘 默认“否”
        prpLRegistExt.setIsAlarm("1");//是否报警 默认“是”
        prpLRegistExt.setIsCantravel("1");//车辆是否可行驶 默认“是”
        prpLRegistExt.setIsRescue("0");//是否进行相关施救 默认“否”
        prpLRegistExt.setCheckType("3");//查勘类型 默认“现场查勘”

        //================【车辆信息必须设置标的车】============================
        List<PrpLRegistCarLossVo> prpLRegistCarLosses = new ArrayList<PrpLRegistCarLossVo>();
        PrpLRegistCarLossVo prpLRegistCarLossVo = new PrpLRegistCarLossVo();
        prpLRegistCarLossVo.setLicenseNo(licenseNo);//车牌号
        prpLRegistCarLossVo.setFrameNo(prpLCItemCarVo.getFrameNo());//车架号
        prpLRegistCarLossVo.setBrandName(prpLCItemCarVo.getBrandName());//厂牌型号
        prpLRegistCarLossVo.setLicenseType(prpLCItemCarVo.getCarType());//号牌种类
        prpLRegistCarLossVo.setRepairCode(reportInfoDTO.getRepaireFactoryId());//推荐修理厂代码 TODO 需编码转换
        prpLRegistCarLossVo.setRepairName(reportInfoDTO.getRepairFactoryName());//推荐修理厂名称
        //默认赋值 TODO 需确认
        prpLRegistCarLossVo.setLossparty("1");//损失方 TODO 默认标的车
        prpLRegistCarLossVo.setLosspart("13");//损失部位 TODO 默认不详
        prpLRegistCarLosses.add(prpLRegistCarLossVo);

        //=====================【人伤设置人伤信息】==========================
        List<PrpLRegistPersonLossVo> prpLRegistPersonLosses = new ArrayList<PrpLRegistPersonLossVo>();
        if("Y".equals(reportInfoDTO.getIsInjured())){
            //标的车人伤
            PrpLRegistPersonLossVo insuredPersonLossVo = new PrpLRegistPersonLossVo();
            insuredPersonLossVo.setDeathcount(reportAccidentDTO.getDeatToll());//死亡人数
            insuredPersonLossVo.setInjuredcount(reportAccidentDTO.getInjuredNumber());//受伤人数
            //默认赋值 TODO 需确认
            insuredPersonLossVo.setLossparty("1");//损失方 TODO 暂时默认标的车
            //三者车人伤 TODO 默认赋值 需确认
            PrpLRegistPersonLossVo thirdPersonLossVo = new PrpLRegistPersonLossVo();
            thirdPersonLossVo.setDeathcount(0);//死亡人数
            thirdPersonLossVo.setInjuredcount(0);//受伤人数
            thirdPersonLossVo.setLossparty("3");//损失方
            prpLRegistPersonLosses.add(insuredPersonLossVo);
            prpLRegistPersonLosses.add(thirdPersonLossVo);
        }

        //=====================【设置报案主信息】=============================
        prpLRegistVo.setPrpLRegistPersonLosses(prpLRegistPersonLosses);
        prpLRegistVo.setPrpLRegistCarLosses(prpLRegistCarLosses);
        prpLRegistVo.setPrpLRegistExt(prpLRegistExt);

        return prpLRegistVo;
    }

    /**
     * 提交工作流
     * @param prpLRegistVo
     * @param userVo
     */
    private void submitWf(PrpLRegistVo prpLRegistVo, SysUserVo userVo){
        //提交报案工作流
        WfTaskSubmitVo submitVo1 = new WfTaskSubmitVo();
        submitVo1.setFlowId(prpLRegistVo.getFlowId());
        submitVo1.setTaskInKey(prpLRegistVo.getRegistNo());
        submitVo1.setFlowTaskId(BigDecimal.ZERO);
        submitVo1.setComCode(prpLRegistVo.getComCode());
        submitVo1.setAssignUser("ANYONE");//报案案件查询特殊处理,taskIn表assignUser和handlerUser赋值为ANYONE,允许全部报案权限工号查询
        submitVo1.setTaskInUser(userVo.getUserCode());
        submitVo1.setAssignCom(prpLRegistVo.getComCode());
        submitVo1.setSubmitType(SubmitType.TMP);//修改报案工作流信息
        wfTaskHandleService.submitRegist(prpLRegistVo,submitVo1);

        WfTaskSubmitVo submitVo2 = new WfTaskSubmitVo();
        PrpLRegistVo registVo = registService.findRegistByRegistNo(prpLRegistVo.getRegistNo());
        //将报案状态设为已提交
        registVo.setRegistTaskFlag(CodeConstants.RegistTaskFlag.SUBMIT);
        BigDecimal flowTaskId = BigDecimal.ZERO;
        submitVo2.setFlowId(registVo.getFlowId());
        submitVo2.setTaskInKey(registVo.getRegistNo());
        submitVo2.setFlowTaskId(flowTaskId);
        submitVo2.setTaskInUser(userVo.getUserCode());
        submitVo2.setComCode(prpLRegistVo.getComCode());
        submitVo2.setAssignUser(userVo.getUserCode());
        submitVo2.setAssignCom(prpLRegistVo.getComCode());
        submitVo2.setSubmitType(SubmitType.N);

        PrpLScheduleTaskVo scheduleTaskVo1 = new PrpLScheduleTaskVo();
        scheduleTaskVo1.setPosition("00010095");
        scheduleTaskVo1.setCreateUser(userVo.getUserCode());
        scheduleTaskVo1.setCreateTime(new Date());
        scheduleTaskVo1.setUpdateUser(userVo.getUserCode());
        scheduleTaskVo1.setUpdateTime(new Date());
        String url = null;/*SpringProperties.getProperty("MClaimPlatform_URL_IN")+AUTOSCHEDULE_URL_METHOD;*/
        try{
            registService.submitSchedule(registVo, submitVo2, scheduleTaskVo1,url);
        }
        catch(Exception e){
            logger.debug("报案提交调度失败"+e.getMessage());
            e.printStackTrace();
        }

        //提交报案工作流
        PrpLWfTaskVo taskVo = wfTaskHandleService.submitRegist(registVo,submitVo2);

        //报案提交调用方正客服系统(车险报案接口)
        /*try{
            //scheduleTaskVo.setRelateHandlerMobile(item.getRelateHandlerMobile());
            interfaceAsyncService.carRegistToFounder(prpLRegistVo.getRegistNo());
        }catch(Exception e){
            logger.debug("报案提交调用方正客服系统失败："+e.getMessage());
        }

        //发送短信
        registService.sendMsg(prpLRegistVo);*/

        //提交调度到查勘
        WfTaskSubmitVo submitVo3 = new WfTaskSubmitVo();
        PrpLScheduleTaskVo scheduleTaskVo2 = scheduleTaskService.findScheduleTaskByRegistNo(prpLRegistVo.getRegistNo());
        List<PrpLScheduleItemsVo> prpLScheduleItemses = scheduleTaskVo2.getPrpLScheduleItemses();
        submitVo3.setFlowId(taskVo.getFlowId());
        submitVo3.setFlowTaskId(taskVo.getTaskId());
        submitVo3.setTaskInKey(prpLRegistVo.getRegistNo());
        submitVo3.setComCode(prpLRegistVo.getComCode());
        submitVo3.setTaskInUser(userVo.getUserCode());
        submitVo3.setAssignCom(prpLRegistVo.getComCode());
        submitVo3.setAssignUser(userVo.getUserCode());
        submitVo3.setSubmitType(SubmitType.N);

        String lngXlatY = registVo.getDamageMapCode();
        String checkAreaCode = registVo.getDamageAreaCode();
        String code = scheduleTaskVo2.getScheduledComcode();//调度查勘员的机构
        //调度地区
        List<PrpLCMainVo> prpLCMainVoList = new ArrayList<PrpLCMainVo>();
        prpLCMainVoList = policyViewService.getPolicyAllInfo(scheduleTaskVo2.getRegistNo());
        if(prpLCMainVoList.size() > 0 && prpLCMainVoList!=null){
            //承保地区
            String comCode = "";
            if(prpLCMainVoList.size()==2){
                for(PrpLCMainVo vo:prpLCMainVoList){
                    if(("12").equals(vo.getRiskCode().substring(0, 2))){
                        comCode = vo.getComCode();
                    }
                }
            }else{
                comCode = prpLCMainVoList.get(0).getComCode();
            }
            if(code != null && comCode!=""){
                if("0002".equals(code.substring(0, 4))){//深圳
                    if(!code.substring(0, 4).equals(comCode.substring(0, 4))){

                        registVo.setTpFlag("1");
                        registVo.setIsoffSite("1");
                        registService.saveOrUpdate(registVo);
                    }

                }else{
                    if(!code.substring(0, 2).equals(comCode.substring(0, 2))){
                        registVo.setTpFlag("1");
                        registVo.setIsoffSite("1");
                        registService.saveOrUpdate(registVo);
                    }
                }
            }
        }
        scheduleTaskVo2.setTypes("1");
        scheduleTaskVo2.setScheduledTime(new Date());
        scheduleHandlerService.saveScheduleItemTask(scheduleTaskVo2, prpLScheduleItemses, submitVo3);
    }

    /**
     * 转换性别
     * @param driverSex
     * @return
     */
    private String convertSex(String driverSex) {
        if ("M".equals(driverSex)){
            return "1";
        }else if ("F".equals(driverSex)){
            return "2";
        }
        return "9";
    }

    /**
     * 转换鼎和编码
     * @param pingAnCodeTypeEnum
     * @param pingAnCode
     * @return
     */
    private String convertDHCode(PingAnCodeTypeEnum pingAnCodeTypeEnum, String pingAnCode) {
        PiccCodeDictVo piccCodeDictVo = pingAnDictService.getDictData(pingAnCodeTypeEnum.getCodeType(), pingAnCode);
        if (piccCodeDictVo == null){
            return "";
        }
        return piccCodeDictVo.getDhCodeCode();
    }

    /**
     * 获取鼎和地区编码
     */
    private String getAreaCodeByAccidentPlace(String accidentPlace) {
        List<SysAreaDictVo> areaDictVoList = null;
        if (StringUtils.isNotBlank(accidentPlace)){
            String array[] = accidentPlace.split("-");

            //先取末级
            if (array.length > 3){
                array = Arrays.copyOf(array, 3);
                areaDictVoList = areaDictService.findAreaCodeByFullName(StringUtils.join(array, "-"));
            }else {
                areaDictVoList = areaDictService.findAreaCodeByFullName(StringUtils.join(array, "-"));
            }

            //如果末级取不到则循环向上取
            if (CollectionUtils.isEmpty(areaDictVoList)){
                for (int i=array.length-1;i>0;i--){
                    areaDictVoList = areaDictService.findAreaCodeByFullName(StringUtils.join(Arrays.copyOf(array, i), "-"));
                    if (CollectionUtils.isNotEmpty(areaDictVoList)){
                        break;
                    }
                }
            }
        }

        if (CollectionUtils.isNotEmpty(areaDictVoList)) {
            return areaDictVoList.get(0).getAreaCode();
        }
        return null;
    }
}
