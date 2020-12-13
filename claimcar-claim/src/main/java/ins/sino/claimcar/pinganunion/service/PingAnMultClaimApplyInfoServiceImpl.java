package ins.sino.claimcar.pinganunion.service;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import ins.platform.common.web.util.WebUserUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.endcase.service.ReOpenCaseService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.endcase.vo.PrpLReCaseTextVo;
import ins.sino.claimcar.endcase.vo.PrpLReCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.constant.SubmitType;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.pinganUnion.dto.*;
import ins.sino.claimcar.pinganUnion.service.PingAnHandleService;
import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import ins.sino.claimcar.pinganunion.vo.multclaimapply.MultClaimApplyDTO;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

/**
 * 
 * @Description: 平安联盟-案件重开查询接口业务数据处理入口
 * @author: zhubin
 * @date: 2020年8月3日 上午9:34:04
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},group="pingAnMultClaimApplyInfoService")
@Path("pingAnMultClaimApplyInfoService")
public class PingAnMultClaimApplyInfoServiceImpl implements PingAnHandleService {
    private static Logger logger = LoggerFactory.getLogger(PingAnMultClaimApplyInfoServiceImpl.class);

    @Autowired
    private RegistQueryService registQueryService;
    
    @Autowired
	private WfTaskHandleService wfTaskHandleService;
    
    @Autowired
	private ReOpenCaseService reOpenCaseService;
    
    @Autowired
	private ClaimTaskService claimTaskService;
    
    @Autowired
	private PolicyViewService policyViewService;
    
    @Autowired
	private WfFlowQueryService wfFlowQueryService;    
	
	@Autowired
	private ClaimService claimService;    
	
	@Autowired
	private InterfaceAsyncService interfaceAsyncService;
    
    @Override
    public ResultBean pingAnHandle(String registNo,PingAnDataNoticeVo pingAnDataNoticeVo, String respData) {
        logger.info("平安联盟-案件重开查询接口业务数据处理入口--respData={}", respData);

        ResultBean resultBean = ResultBean.success();
        try {
            //解析json字符串
            JSONObject jsonObject = JSON.parseObject(respData);
            String multClaimApplyInfoString = jsonObject.getString("multClaimApplyDTO");//重开信息
            String wholeCaseBaseString = jsonObject.getString("wholeCaseBaseDTO");//整案基本信息

            //转换成DTO对象
            MultClaimApplyDTO multClaimApplyDTO = JSON.parseObject(multClaimApplyInfoString, MultClaimApplyDTO.class);
            WholeCaseBaseDTO wholeCaseBaseDTO = JSON.parseObject(wholeCaseBaseString, WholeCaseBaseDTO.class);
            
            //基本校验
            checkData(registNo,multClaimApplyDTO,wholeCaseBaseDTO);
            
            //数据保存
            saveMultClaimApplyInfo(registNo,multClaimApplyDTO,wholeCaseBaseDTO);
            

        }catch (Exception e){
        	logger.error("平安联盟-案件重开查询接口业务数据处理报错：registNo={},error={}", registNo, ExceptionUtils.getStackTrace(e));
            resultBean = resultBean.fail(e.getMessage());
        }

        return resultBean;
    }

    /**
     * 校验数据是否合法
     */
    private void checkData(String registNo,MultClaimApplyDTO multClaimApplyDTO,WholeCaseBaseDTO wholeCaseBaseDTO) {
        if (multClaimApplyDTO == null){
            throw new IllegalArgumentException("重开信息multClaimApplyDTO为空");
        }
        if (wholeCaseBaseDTO == null){
            throw new IllegalArgumentException("整案基本信息wholeCaseBaseDTO为空");
        }
        
        if (StringUtils.isBlank(registNo)){
            throw new IllegalArgumentException("报案号reportNo为空");
        }
        
    }
    
    private void saveMultClaimApplyInfo(String registNo,MultClaimApplyDTO multClaimApplyDTO,WholeCaseBaseDTO wholeCaseBaseDTO){
    	Date date = new Date();
    	// 旧理赔
		int length = registNo.length();
		if(length == 21){
			// 生成旧理赔流程节点
			wfTaskHandleService.generateFlow(registNo);
			wfTaskHandleService.generateFlowLevel(registNo);
		}
		
		String saveTypeFlag = "";
		if("1".equals(multClaimApplyDTO.getVerifyOptions())){
			saveTypeFlag = "pass";
		}
		
		//1.第一次存prplrecase,并生成重开赔案登记结点，开启重开赔案审核一级结点
		List<PrpLEndCaseVo> endCaseList=reOpenCaseService.findEndCaseByRegistNo(registNo);
		if(endCaseList != null && endCaseList.size() > 0){
			PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
			for(PrpLEndCaseVo endCase:endCaseList){
				WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
    			WfSimpleTaskVo taskVo=new WfSimpleTaskVo();
    			String claimNo = endCase.getClaimNo();
    			taskVo.setClaimNo(claimNo);
    			String adjustCaseHadEnd = claimService.adjustCaseHadEnd(claimNo);
    			if("noEnd".equals(adjustCaseHadEnd)){
    				throw new IllegalArgumentException("已重开赔案！");
    			}
    			boolean flag = true;
    			Boolean bl = reOpenCaseService.isFail(endCase.getEndCaseNo());
    			Boolean existReOpen = wfTaskHandleService.existTaskByNodeCode(registNo, FlowNode.ReOpen, endCase.getEndCaseNo(), "");
				if(!existReOpen || bl){
					flag = false;
				}
				if(flag){
					if("1101".equals(endCase.getRiskCode())){
						throw new IllegalArgumentException("已重开交强赔案！");
					}else{
						throw new IllegalArgumentException("已重开商业赔案！");
					}
				}
					// 拼装taskVo和submitVo
				PrpLWfTaskVo wfEndCaseTaskVo = new PrpLWfTaskVo();
				List<PrpLWfTaskVo> wfTaskVos = wfTaskHandleService.findEndTask(registNo,endCase.getCompensateNo(),FlowNode.EndCas);
				if(wfTaskVos!=null&&wfTaskVos.size()>0){
					for(PrpLWfTaskVo vo : wfTaskVos){
						if(claimNo.equals(vo.getClaimNo())){
								// 防止冲销0结案商业交强handleidkey都是CompWfZero的情况
							wfEndCaseTaskVo = vo;
							break;
						}
					}
					taskVo.setRegistNo(wfEndCaseTaskVo.getRegistNo());
					taskVo.setItemName(wfEndCaseTaskVo.getItemName());
					taskVo.setBussTag(wfEndCaseTaskVo.getBussTag());
					taskVo.setShowInfoXml(wfEndCaseTaskVo.getShowInfoXML());
					submitVo.setFlowId(wfEndCaseTaskVo.getFlowId());
					submitVo.setFlowTaskId(wfEndCaseTaskVo.getTaskId());// 设置重开赔案登记taskId
				}else{
					throw new IllegalArgumentException("没有查询到结案节点！");
				}
				
				taskVo.setHandlerIdKey(endCase.getEndCaseNo());
				taskVo.setRegistNo(endCase.getRegistNo());
				taskVo.setRiskCode(endCase.getRiskCode());
				
				submitVo.setHandleIdKey(endCase.getClaimNo());// 立案号赋值给HandleIdKey
				submitVo.setTaskInKey(endCase.getEndCaseNo());// 结案号赋值给taskInKey
				submitVo.setComCode(wfEndCaseTaskVo.getComCode());
				submitVo.setTaskInUser("AUTO");
				submitVo.setCurrentNode(FlowNode.EndCas);
				submitVo.setNextNode(FlowNode.ReOpenApp);
				
				submitVo.setAssignCom(prpLRegistVo.getComCode());
				
				submitVo.setAssignUser("AUTO");
				submitVo.setHandlertime(date);
				submitVo.setHandleruser("AUTO");
					// 新增重开赔案登记节点
				PrpLWfTaskVo wfTaskVo = wfTaskHandleService.addReOpenAppTask(taskVo,submitVo);
				
				taskVo.setRegistNo(wfTaskVo.getRegistNo());
				taskVo.setItemName(wfTaskVo.getItemName());
				taskVo.setBussTag(wfTaskVo.getBussTag());
				taskVo.setShowInfoXml(wfTaskVo.getShowInfoXML());
				submitVo.setFlowId(wfTaskVo.getFlowId());
			    submitVo.setCurrentNode(FlowNode.ReOpenApp);
			    submitVo.setNextNode(FlowNode.ReOpenVrf_LV1);
			    submitVo.setComCode(wfTaskVo.getComCode());
			    submitVo.setTaskInKey(wfTaskVo.getTaskInKey());
			    submitVo.setTaskInUser(wfTaskVo.getTaskInUser());
			    submitVo.setFlowTaskId(wfTaskVo.getTaskId());
				submitVo.setAssignCom(wfEndCaseTaskVo.getComCode());// 重开赔案审核到岗
			    submitVo.setAssignUser(null);
			    submitVo.setHandleruser(null);
			    submitVo.setHandlertime(null);
				PrpLWfTaskVo WfTaskVo=wfTaskHandleService.submitReOpenApp(taskVo, submitVo);
					// 回写立案表的结案信息
				claimTaskService.reOpenClaimWirteBack(claimNo, FlowNode.ReOpenApp,"");
				
				PrpLReCaseVo prpLReCaseVo = new PrpLReCaseVo();
				
				if("Y".equals(multClaimApplyDTO.getAddFee())&& !"Y".equals(multClaimApplyDTO.getAddOther())){
					prpLReCaseVo.setOpenReasonCode("7");
				}else{
					prpLReCaseVo.setOpenReasonCode("8");
				}
				prpLReCaseVo.setOpenReasonDetail(multClaimApplyDTO.getApplyReason());
				
				prpLReCaseVo.setEndCaseDate(endCase.getEndCaseDate());
				prpLReCaseVo.setEndCaseNo(endCase.getEndCaseNo());
				prpLReCaseVo.setCompensateNo(endCase.getCompensateNo());
				prpLReCaseVo.setRegistNo(endCase.getRegistNo());
				prpLReCaseVo.setEndCaseUserCode(endCase.getCreateUser());
				prpLReCaseVo.setSeriesNo(reOpenCaseService.countReCaseByClaimNo(endCase.getClaimNo()));
				prpLReCaseVo.setClaimNo(endCase.getClaimNo());
				
				prpLReCaseVo.setOpenCaseUserCode("AUTO");
				prpLReCaseVo.setOpenCaseUserName("AUTO");
				prpLReCaseVo.setOpenCaseDate(date);
				prpLReCaseVo.setCreateTime(date);
				
				//直接将状态设置为9，将第一次重开赔案登记时保存peplrecase,和重开赔案审核一级保存合并一起，将原来状态4改为9
				if("pass".equals(saveTypeFlag)){
					prpLReCaseVo.setCheckStatus("6");
				}else{
					prpLReCaseVo.setCheckStatus("7");
				}
				
				prpLReCaseVo.setFlag(endCase.getRiskCode());
				PrpLCMainVo prpLCMainVo=policyViewService.getPrpLCMainByRegistNoAndPolicyNo(endCase.getRegistNo(), endCase.getPolicyNo());
				PrpLClaimVo prpLClaimVo=claimTaskService.findClaimVoByClaimNo(endCase.getClaimNo());
				prpLReCaseVo.setInsuredName(prpLCMainVo.getInsuredName());
				prpLReCaseVo.setMercyFlag(prpLClaimVo.getMercyFlag());
				prpLReCaseVo.setRemark(endCase.getPolicyNo());
				
				//重开赔案审核一级特有赋值
				prpLReCaseVo.setCheckCaseUserCode("AUTO");
				prpLReCaseVo.setCheckCaseUserName("");
				prpLReCaseVo.setDealCaseDate(date);
				prpLReCaseVo.setUpdateTime(date);
				prpLReCaseVo.setUpdateUser("AUTO");
				prpLReCaseVo.setCheckOpinion(multClaimApplyDTO.getVerifyRemark());
				
					// 设置意见表属性
				List<PrpLReCaseTextVo> prpLReCaseTexts=new ArrayList<PrpLReCaseTextVo>();
				//重开赔案登记
				PrpLReCaseTextVo prpLReCaseTextVo=new PrpLReCaseTextVo();
				prpLReCaseTextVo.setOperatorName("AUTO");
				prpLReCaseTextVo.setComName(prpLRegistVo.getComCode());
				prpLReCaseTextVo.setInputTime(date);
				prpLReCaseTextVo.setCheckStatus("4");
				prpLReCaseTextVo.setOpenReasonDetail(prpLReCaseVo.getOpenReasonDetail());
				int n=Integer.valueOf(prpLReCaseVo.getOpenReasonCode());
				prpLReCaseTextVo.setCheckOpinion(reOpenCaseService.getOpinion(n));
				prpLReCaseTexts.add(prpLReCaseTextVo);
				
				//重开赔案审核-一级
				PrpLReCaseTextVo prpLReCaseTextVo2=new PrpLReCaseTextVo();
				prpLReCaseTextVo2.setOperatorName("AUTO");
				prpLReCaseTextVo2.setComName(prpLRegistVo.getComCode());
				prpLReCaseTextVo2.setInputTime(date);
				prpLReCaseTextVo2.setCheckStatus("5");
				prpLReCaseTextVo2.setOpenReasonDetail(prpLReCaseVo.getOpenReasonDetail());
				int n1=Integer.valueOf(prpLReCaseVo.getOpenReasonCode());
				prpLReCaseTextVo.setCheckOpinion(reOpenCaseService.getOpinion(n1));
				prpLReCaseTexts.add(prpLReCaseTextVo2);
				
				//重开赔案审核-二级
				PrpLReCaseTextVo prpLReCaseTextVo3=new PrpLReCaseTextVo();
				prpLReCaseTextVo3.setOperatorName("AUTO");
				prpLReCaseTextVo3.setComName(prpLRegistVo.getComCode());
				prpLReCaseTextVo3.setInputTime(date);
				if("pass".equals(saveTypeFlag)){
					prpLReCaseTextVo3.setCheckStatus("6");
				}else{
					prpLReCaseTextVo3.setCheckStatus("7");
				}
				prpLReCaseTextVo3.setOpenReasonDetail(prpLReCaseVo.getOpenReasonDetail());
				int n2=Integer.valueOf(prpLReCaseVo.getOpenReasonCode());
				prpLReCaseTextVo3.setCheckOpinion(reOpenCaseService.getOpinion(n2));
				prpLReCaseTexts.add(prpLReCaseTextVo3);
				
				prpLReCaseVo.setPrpLReCaseTexts(prpLReCaseTexts);
				reOpenCaseService.saveOrUpdateReCase(prpLReCaseVo);
			}
    		
    		//2.分别提交重开赔案审核一级结点，开启重开赔案审核二级结点
    		List<PrpLWfTaskVo> taskVoForInVos = wfFlowQueryService.findPrpWfTaskVoForIn(registNo,FlowNode.ReOpen.name(),FlowNode.ReOpenVrf_LV1.name());
    		if(taskVoForInVos != null && taskVoForInVos.size() > 0){
    			for (PrpLWfTaskVo wfTaskVo : taskVoForInVos) {
        			wfTaskHandleService.tempSaveTask(wfTaskVo.getTaskId().doubleValue(), wfTaskVo.getHandlerIdKey(), "AUTO", prpLRegistVo.getComCode());
        			
        			Integer reOpenCount=wfTaskHandleService.findTaskReOpenCount(wfTaskVo);
        			PrpLReCaseVo reCaseVo = reOpenCaseService.findReCaseVoByEndCaseNoA(wfTaskVo.getTaskInKey(),reOpenCount);
        			WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
        			WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
        			
        			// 重开赔案审核到岗
        			submitVo.setAssignCom(CodeConstants.TOPCOM);
        			submitVo.setAssignUser(null);
        			submitVo.setFlowId(wfTaskVo.getFlowId());
        			submitVo.setFlowTaskId(wfTaskVo.getTaskId());
        			submitVo.setHandleIdKey(wfTaskVo.getHandlerIdKey());
        			submitVo.setTaskInKey(reCaseVo.getEndCaseNo());
        			submitVo.setComCode(wfTaskVo.getComCode());// 提交到总公司
        			submitVo.setTaskInUser("AUTO");
        			submitVo.setCurrentNode(FlowNode.ReOpenApp);
        			submitVo.setNextNode(FlowNode.valueOf("ReOpenVrf_LV2"));
        			submitVo.setSubmitType(SubmitType.U);
        			submitVo.setHandlertime(date);
        			submitVo.setHandleruser("AUTO");

        			taskVo.setRegistNo(wfTaskVo.getRegistNo());
        			taskVo.setItemName(wfTaskVo.getItemName());
        			taskVo.setBussTag(wfTaskVo.getBussTag());
        			taskVo.setShowInfoXml(wfTaskVo.getShowInfoXML());
        			taskVo.setHandlerIdKey(wfTaskVo.getHandlerIdKey());
        			taskVo.setRiskCode(wfTaskVo.getRiskCode());
        			taskVo.setClaimNo(wfTaskVo.getClaimNo());
        			PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.submitSimpleTask(taskVo,submitVo);
    			}
    		}else{
    			throw new IllegalArgumentException("平安联盟-案赔审核一级结点找不到！");
    		}
    		
    		//3.分别提交重开赔案审核二级结点，如果审核意见通过，开启下一个节点，不通过，不开启
    		List<PrpLWfTaskVo> taskVoForInVos1 = wfFlowQueryService.findPrpWfTaskVoForIn(registNo,FlowNode.ReOpen.name(),FlowNode.ReOpenVrf_LV2.name());
    		if(taskVoForInVos1 != null && taskVoForInVos1.size() > 0){
    			for (PrpLWfTaskVo wfTaskVo : taskVoForInVos1) {
        			wfTaskHandleService.tempSaveTask(wfTaskVo.getTaskId().doubleValue(), wfTaskVo.getHandlerIdKey(), "AUTO", prpLRegistVo.getComCode());
        			Integer reOpenCount=wfTaskHandleService.findTaskReOpenCount(wfTaskVo);
        			
        			PrpLReCaseVo reCaseVo = reOpenCaseService.findReCaseVoByEndCaseNoA(wfTaskVo.getTaskInKey(),reOpenCount);
        			SysUserVo userVo = new SysUserVo();
        			userVo.setUserCode("AUTO");
        			userVo.setUserName("");
        			userVo.setComCode(prpLRegistVo.getComCode());
        			reOpenCaseService.pinganSubmit(wfTaskVo.getTaskId().doubleValue(),saveTypeFlag,reCaseVo,userVo);
        			// 再保处理重开赔案业务 niuqiang businessType=3
        			try{
        				String claimNo = reCaseVo.getClaimNo();
        				PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
        				ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); // 填写日志表
        				claimInterfaceLogVo.setClaimNo(claimNo);
        				claimInterfaceLogVo.setRegistNo(reCaseVo.getRegistNo());
        				claimInterfaceLogVo.setCompensateNo(reCaseVo.getCompensateNo());
        				claimInterfaceLogVo.setComCode(userVo.getComCode());
        				claimInterfaceLogVo.setCreateUser("AUTO");
        				claimInterfaceLogVo.setCreateTime(new Date());
        				claimInterfaceLogVo.setOperateNode(FlowNode.ReOpen.getName());
        				interfaceAsyncService.TransDataForReinsCaseVo("3", claimVo,claimInterfaceLogVo);
        			}catch(Exception e){
        				logger.error(reCaseVo.getClaimNo()+"重开赔案送再保报错："+e);
        			}
        		}
    		}else{
    			throw new IllegalArgumentException("平安联盟-赔案审核二级结点找不到！");
    		}
		}else{
			throw new IllegalArgumentException("该案件没有结案，结案表没有数据！");
		}
    }
    
}
