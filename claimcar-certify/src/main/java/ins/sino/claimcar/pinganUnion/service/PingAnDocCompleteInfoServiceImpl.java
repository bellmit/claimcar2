package ins.sino.claimcar.pinganUnion.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import ins.platform.vo.PrpLLawSuitVo;
import ins.sino.claimcar.certify.service.CertifyService;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.LawSiutService;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.pay.service.PadPayPubService;
import ins.sino.claimcar.pinganUnion.dto.PingAnCertifyDTO;
import ins.sino.claimcar.pinganUnion.dto.WholeCaseBaseDTO;
import ins.sino.claimcar.pinganUnion.service.PingAnHandleService;
import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

/**
 * 
 * @Description: 平安联盟-单证完成查询接口业务数据处理入口
 * @author: zhubin
 * @date: 2020年8月3日 上午9:34:04
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},group="pingAnDocCompleteInfoService")
@Path("pingAnDocCompleteInfoService")
public class PingAnDocCompleteInfoServiceImpl implements PingAnHandleService {
    private static Logger logger = LoggerFactory.getLogger(PingAnDocCompleteInfoServiceImpl.class);
    
    
	@Autowired
	private CertifyService certifyService;
	
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	
	@Autowired
	private RegistQueryService registQueryService;
	
	@Autowired
	private CheckTaskService checkTaskService;
	
	@Autowired
	private LawSiutService lawsiutService;
	
	@Autowired
	private DeflossHandleService deflossHandleService;
	
	@Autowired
	private PropTaskService propTaskService;
	
	@Autowired
	private PersTraceDubboService persTraceDubboService;
	
	@Autowired
	private CompensateTaskService compensateTaskService;
	
	@Autowired
	private PadPayPubService padPayPubService;
	
	@Autowired
	private WfFlowQueryService wfFlowQueryService;    
	
    @Override
    public ResultBean pingAnHandle(String registNo, PingAnDataNoticeVo pingAnDataNoticeVo, String respData) {
        logger.info("平安联盟-单证完成查询接口业务数据处理入口--respData={}", respData);

        ResultBean resultBean = ResultBean.success();
        try {
            
            //转换成DTO对象
            PingAnCertifyDTO certifyDTO = JSON.parseObject(respData, PingAnCertifyDTO.class);
            
            //基本校验
            checkData(registNo,certifyDTO);
            
            //提交校验
            String result = this.validateCerti(registNo);
			if(result != null){
				throw new IllegalArgumentException("单证提交失败："+result);
			}
            
            //数据保存
			saveCertifyInfo(certifyDTO,registNo);
			
        }catch (Exception e){
           logger.error("平安联盟-单证完成查询接口业务数据处理报错：registNo={},error={}", registNo, ExceptionUtils.getStackTrace(e));
           resultBean.fail(e.getMessage());
        }
        return resultBean;
    }

    /**
     * 校验数据是否合法
     */
    private void checkData(String registNo,PingAnCertifyDTO certifyDTO) {
        if (certifyDTO == null){
            throw new IllegalArgumentException("单证信息certifyDTO为空");
        }
        
        if (StringUtils.isBlank(registNo)){
            throw new IllegalArgumentException("报案号reportNo为空");
        }
        
    }
    
    /**
	 * 单证提交校验
	 * @param directFlag
	 * @param registNo
	 * @param subrogationMainVo
	 * @param prpLCheckDutyVo 标的车的责任信息
	 * @param prpLCheckDutyVoList 三者车责任信息集合
	 * @return
	 */
	private String validateCerti(String registNo) {
		
		if(!deflossHandleService.existTargetCar(registNo)){
			return "未生成标的车定损任务，请联系系统运维人员！";
		}

		if (!propTaskService.isDLossAllPassed(registNo)) {
			return "核损未全部完成，单证不能提交";
		}
		
		// 互碰自赔校验
		/*if ("1".equals(prpLCheckDutyVo.getIsClaimSelf())) {
			prpLCheckDutyVoList.add(prpLCheckDutyVo);// 加入标的车信息
			String returnInfo = isClaimSelf(registNo,prpLCheckDutyVoList);
			if(!StringUtils.isBlank(returnInfo)){
				return returnInfo;
			}
		}*/

		if (!persTraceDubboService.isDlossPersonAllPassed(registNo)) {
			return "人伤费用审核未完成，单证不能提交";
		}

		// 校验预付任务
		if (!compensateTaskService.isPrepayAllPassed(registNo)) {
			return "该案件存在未核赔通过的预付任务或者预付冲销任务，单证不能提交";
		}

		// 判断垫付任务
		if (!padPayPubService.isPadPayAllPassed(registNo)) {
			return "该案件存在未核赔通过的垫付任务，单证不能提交";
		}
		
		// 判断重开后理赔冲销是否核损通过 或者理算已注销
		/*if (!compensateTaskService.isCompepayAllPassed(registNo)){
			return "该案件存在未核赔通过的理算任务或者理算冲销任务，单证不能提交";
		}*/
		
		// 是否存在立案注销或者立案注销恢复任务未完结
		List<PrpLWfTaskVo> cancleTaskList = wfFlowQueryService.findPrpWfTaskVo(registNo, FlowNode.Cancel.name());
		if(cancleTaskList != null && !cancleTaskList.isEmpty()){
			return "该案件存在立案注销或者立案注销恢复任务未完结，单证不能提交";
		}
		
		return null;
	}
    
    private void saveCertifyInfo(PingAnCertifyDTO certifyDTO,String registNo){
    	boolean directFlag = true;// 是否收集齐全
		Date nowDate = new Date();
		String userCode = "AUTO";
		String saveType = "submit";
		boolean flag = true;   //主表PrpLCertifyMainVo有update(),无save()
		
		String registNO = registNo;
		//如果表里PrpLCertifyMainVo有，直接查出来，没有从报案拿
		PrpLCertifyMainVo prpLCertifyMainVo = certifyService.findPrpLCertifyMainVo(registNO);
		if(prpLCertifyMainVo == null){
			flag = false;
			prpLCertifyMainVo = new PrpLCertifyMainVo();
			PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNO);
			if(prpLRegistVo != null){
				String policyNo = prpLRegistVo.getPolicyNo(); 
    			String policyNoLink = prpLRegistVo.getPrpLRegistExt().getPolicyNoLink();
    			prpLCertifyMainVo.setPolicyNo(policyNo);
    			prpLCertifyMainVo.setPolicyNoLink(policyNoLink);
    			prpLCertifyMainVo.setRiskCode(prpLRegistVo.getRiskCode());
			}
			
			prpLCertifyMainVo.setRegistNo(registNO);
			prpLCertifyMainVo.setStartTime(nowDate);
			prpLCertifyMainVo.setValidFlag("1");// 有效
			prpLCertifyMainVo.setCreateUser(userCode);
			prpLCertifyMainVo.setCreateTime(nowDate);
		}
		
		// 当单证可以提交的时候必须是搜集齐全状态
		prpLCertifyMainVo.setCollectFlag("1");
		
		List<PrpLLawSuitVo> lawSuitVos = lawsiutService.findByRegistNo(registNO);// 案件诉讼信息表
		if(lawSuitVos!=null && lawSuitVos.size()>0){
			prpLCertifyMainVo.setLawsuitFlag("1");// 案件诉讼信息不为空时，默认为是
		}else{
			prpLCertifyMainVo.setLawsuitFlag("0");// 默认为否
		}
		//prpLCertifyMainVo.setLawsuitFlag("0");// 默认为否
		prpLCertifyMainVo.setIsFraud("0");
		prpLCertifyMainVo.setSurveyFlag("0");
		prpLCertifyMainVo.setFraudLogo("01");
		prpLCertifyMainVo.setFraudType("01");
		prpLCertifyMainVo.setIsSYFraud("0");
		prpLCertifyMainVo.setIsJQFraud("0");
		prpLCertifyMainVo.setCustomClaimTime(nowDate);
		prpLCertifyMainVo.setAddNotifyTime(nowDate);
		prpLCertifyMainVo.setDriveLicenceFlag("0");
		prpLCertifyMainVo.setCarLicenceFlag("0");
		   		
		if(directFlag&&"submit".equals(saveType)){// 单证收集齐全时间 提交时设置时间
			prpLCertifyMainVo.setClaimEndTime(nowDate);
		}
		prpLCertifyMainVo.setOperatorCode(userCode);
		prpLCertifyMainVo.setOperatorDate(nowDate);
		prpLCertifyMainVo.setUpdateUser(userCode);
		prpLCertifyMainVo.setUpdateTime(nowDate);
		
		if(flag){
			certifyService.updatePrpLCertifyMainVo(prpLCertifyMainVo);
		}else{
			prpLCertifyMainVo = certifyService.submitCertify(prpLCertifyMainVo);
		}
		
		//去流程表prplwftaskin取
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		List<PrpLWfTaskVo> prpLWfTaskVos=wfFlowQueryService.findTaskVoForInByNodeCode(registNO, FlowNode.Certi.name());
		if(prpLWfTaskVos!=null && prpLWfTaskVos.size()>0){
			submitVo.setFlowId(prpLWfTaskVos.get(0).getFlowId());
			submitVo.setComCode(prpLWfTaskVos.get(0).getComCode());
			submitVo.setFlowTaskId(prpLWfTaskVos.get(0).getTaskId());
			submitVo.setCurrentNode(FlowNode.Certi);
			submitVo.setTaskInUser(userCode);
			submitVo.setTaskInKey(registNO);
			submitVo.setAssignCom(submitVo.getComCode());
			submitVo.setAssignUser("AUTO");
			wfTaskHandleService.submitCertify(prpLCertifyMainVo, submitVo);
			checkTaskService.saveCheckDutyHis(registNO,"单证提交");
		}else{
			List<PrpLWfTaskVo> outTaskVos=wfFlowQueryService.findTaskVoForOutByNodeCode(registNO, FlowNode.Certi.name());
			//单证提交时taskin表无数据分两种情况，如果第一次提交对应out表没有单证流程数据，则是新增，抛出异常，如果存在已经生成的单证流程，则更新数据，不抛出异常
			if(outTaskVos == null || outTaskVos.size() == 0){
				throw new IllegalArgumentException("平安联盟-单证结点不存在！");
			}
		}
    }
}
