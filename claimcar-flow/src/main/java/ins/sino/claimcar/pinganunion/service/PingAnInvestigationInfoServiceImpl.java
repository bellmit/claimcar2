package ins.sino.claimcar.pinganunion.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;

import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.pinganUnion.service.PingAnHandleService;
import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import ins.sino.claimcar.regist.service.RegistQueryService;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.google.gson.Gson;

import ins.platform.common.service.facade.PingAnDictService;
import ins.platform.common.util.ConfigUtil;
import ins.platform.vo.PiccCodeDictVo;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.pinganUnion.dto.PingAnCertifyDTO;
import ins.sino.claimcar.pinganUnion.enums.PingAnCodeTypeEnum;
import ins.sino.claimcar.pinganunion.vo.investigation.PingAnSurveyDTO;
import ins.sino.claimcar.recloss.service.PrpLSurveyService;
import ins.sino.claimcar.recloss.vo.PrpLSurveyVo;
import ins.sino.claimcar.regist.service.PolicyViewService;

import java.math.BigDecimal;

/**
 * 
 * @Description: 平安联盟-调查信息查询接口业务数据处理入口
 * @author: zhubin
 * @date: 2020年8月3日 上午9:34:04
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},group="pingAnInvestigationInfoService")
@Path("pingAnInvestigationInfoService")
public class PingAnInvestigationInfoServiceImpl implements PingAnHandleService {

	 private static Logger logger = LoggerFactory.getLogger(PingAnInvestigationInfoServiceImpl.class);

	    @Autowired
	    private RegistQueryService registQueryService;
	    
	    @Autowired
		private WfTaskHandleService wfTaskHandleService;
	    
	    @Autowired
		private PrpLSurveyService prpLSurveyService;
	    
	    @Autowired
		private PolicyViewService policyViewService;    
		
		@Autowired
		private WfFlowQueryService wfFlowQueryService;   
		
		@Autowired
		private PingAnDictService pingAnDictService; 

		@Override
		public ResultBean pingAnHandle(String registNo, PingAnDataNoticeVo pingAnDataNoticeVo,
				String respData) {
			logger.info("平安联盟-调查信息查询接口业务数据处理入口--respData={}", respData);

	        ResultBean resultBean = ResultBean.success();
	        try {
	            PingAnSurveyDTO surveyDTO = JSON.parseObject(respData, PingAnSurveyDTO.class);
	            
	            //基本校验
	            checkData(registNo,surveyDTO);
	            
	            //数据保存
	            saveInvestigationInfo(registNo,surveyDTO);
	            

	        }catch (Exception e){
	        	logger.error("平安联盟-调查信息查询接口业务数据处理报错：registNo={},error={}", registNo, ExceptionUtils.getStackTrace(e));
	            resultBean = resultBean.fail(e.getMessage());
	        }

	        return resultBean;
		}

	    /**
	     * 校验数据是否合法
	     */
	    private void checkData(String registNo,PingAnSurveyDTO surveyDTOList) {
	        if (surveyDTOList == null){
	            throw new IllegalArgumentException("调查信息surveyDTOList为空");
	        }
	        
	        if (StringUtils.isBlank(registNo)){
	            throw new IllegalArgumentException("报案号reportNo为空");
	        }
	        
	    }
	    
	    private void saveInvestigationInfo(String registNo,PingAnSurveyDTO surveyDTO){
        	SysUserVo sysUserVo = new SysUserVo();
        	sysUserVo.setUserCode("AUTO");
    		PrpLSurveyVo surveyVo = null;
    		//调查任务开关控制调整		
    		String policyNoComCode = policyViewService.getPolicyComCode(registNo);
    		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.SURVEY,policyNoComCode);
            if(CodeConstants.IsSingleAccident.YES.equals(configValueVo.getConfigValue())){
            	boolean isExist = wfTaskHandleService.existTaskInBySubNodeCode(registNo,FlowNode.Survey.toString());
        		if(isExist){
        			throw new IllegalArgumentException("已存在未完成调查任务！");
        		}else {
        			surveyVo = new PrpLSurveyVo();
    				
    				//根据当前节点查询对应taskin表的工作流
    				PiccCodeDictVo dictData = pingAnDictService.getDictData(PingAnCodeTypeEnum.TDLYHJ.getCodeType(), surveyDTO.getInvestigationFrom());
    				if(StringUtils.isEmpty(dictData.getDhCodeCode())){
    					throw new IllegalArgumentException("找不到对应发起调查流程结点！");
    				}else{
    					List<PrpLWfTaskVo> prpLWfTaskVos=wfFlowQueryService.findTaskVoForOutByNodeCode(registNo, dictData.getDhCodeCode());
        				BigDecimal flowTaskId = new BigDecimal(0);
        				if(prpLWfTaskVos!=null && prpLWfTaskVos.size()>0){
        					surveyVo.setFlowId(prpLWfTaskVos.get(0).getFlowId());
        					flowTaskId = prpLWfTaskVos.get(0).getTaskId();
        					//-------------------------------------------------
            				//surveyVo.setReasonDesc(surveyDTO.getRefuseReasonCode());
            				surveyVo.setNodeCode(dictData.getDhCodeCode());
            				//surveyVo.setFraudScoreId(1L);
            				
            				surveyVo.setRegistNo(registNo);
            				surveyVo.setCreateUser("AUTO");
            				surveyVo.setCreateTime(surveyDTO.getCreatedDate());
            				if(StringUtils.isNotEmpty(surveyDTO.getInvestigationType())){
            					if("1".equals(surveyDTO.getInvestigationType()) || "2".equals(surveyDTO.getInvestigationType())){
            						surveyVo.setIsInjuryCases("1");
            					}else{
            						surveyVo.setIsInjuryCases("0");
            					}
            				}else{
            					surveyVo.setIsInjuryCases("0");
            				}
            				surveyVo.setIsMinorCases("0");
            				
            				surveyVo.setHandlerUser("AUTO");
            				surveyVo.setHandlerTime(new Date());
            				if(StringUtils.isNotEmpty(surveyDTO.getInvestigationStatus())){
            					String status = surveyDTO.getInvestigationStatus();
            					if("1".equals(status)){
            						surveyVo.setHandlerStatus(HandlerStatus.INIT);
            					}else if("2".equals(status)){
            						surveyVo.setHandlerStatus(HandlerStatus.START);
            					}else if("3".equals(status) || "4".equals(status) || "5".equals(status)){
            						surveyVo.setHandlerStatus(HandlerStatus.DOING);
            					}else{
            						surveyVo.setHandlerStatus(HandlerStatus.END);
            					}
            				}else{
            					surveyVo.setHandlerStatus(HandlerStatus.END);
            				}
            				if("1".equals(surveyDTO.getInvestigationOpinion())){
            					surveyVo.setOpinionDesc("赔付");
            				}else if("2".equals(surveyDTO.getInvestigationOpinion())){
            					surveyVo.setOpinionDesc("扣减");
            				}else if("3".equals(surveyDTO.getInvestigationOpinion())){
            					surveyVo.setOpinionDesc("整案拒赔");
            				}else if("4".equals(surveyDTO.getInvestigationOpinion())){
            					surveyVo.setOpinionDesc("商业险拒赔");
            				}else{
            					surveyVo.setOpinionDesc("其他");
            				}
            				//surveyVo.setAmout(new BigDecimal(0));   
            				surveyVo.setIsFraud("0");
            				surveyVo.setImpairmentCase("0");
                			if("1".equals(surveyVo.getIsFraud())){
                				surveyVo.setFraudType(surveyVo.getFraudType());
                			}
                			surveyVo.setExternalSurvey("0");
            				surveyVo.setIsAutoTrigger("0");
            				
                			prpLSurveyService.pinganSaveSurvey(surveyVo,sysUserVo,flowTaskId);
                			
                			//调查处理后，提交结点
                			List<PrpLWfTaskVo> wfTaskVoList=wfFlowQueryService.findTaskVoForInByNodeCode(registNo, FlowNode.Survey.name());
                			if(wfTaskVoList!=null && wfTaskVoList.size()>0){
                				Long id = Long.parseLong(wfTaskVoList.get(0).getHandlerIdKey());
                        		WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
                        		taskVo.setRegistNo(registNo);
                        		taskVo.setHandlerIdKey(id.toString());
                        		
                        		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
                        		submitVo.setFlowId(wfTaskVoList.get(0).getFlowId());
                        		submitVo.setCurrentNode(FlowNode.Survey);
                        		submitVo.setAssignUser(wfTaskVoList.get(0).getHandlerUser());
                        		submitVo.setAssignCom(wfTaskVoList.get(0).getAssignCom());
                        		submitVo.setComCode(wfTaskVoList.get(0).getComCode());
                        		submitVo.setTaskInKey(wfTaskVoList.get(0).getTaskInKey());
                        		submitVo.setTaskInUser(sysUserVo.getUserCode());
                        		submitVo.setFlowTaskId(wfTaskVoList.get(0).getTaskId());
                        		submitVo.setNextNode(FlowNode.END);
                        		
                        		wfTaskHandleService.submitSimpleTask(taskVo,submitVo);
                			}
        				}else{
        					throw new IllegalArgumentException("找不到对应prplwftaskout表流程结点！");
        				}
    				}
        		}
            }else{
            	throw new IllegalArgumentException("发起调查任务功能已关闭！");
            }
	    }
    
}
