package ins.sino.claimcar.common.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.common.po.PrpLSurvey;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.recloss.service.PrpLSurveyService;
import ins.sino.claimcar.recloss.vo.PrpLSurveyVo;
import ins.sino.claimcar.regist.service.PolicyViewService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("prpLSurveyService")
public class PrpLSurveyServiceImpl implements PrpLSurveyService {
	
	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	PolicyViewService policyViewService;
	
	@Override
	public List<PrpLSurveyVo> findSurveyByRegistNo(String registNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		List<PrpLSurveyVo> prpLSurveyVos = new ArrayList<PrpLSurveyVo>();
		List<PrpLSurvey> prpLSurveys = databaseDao.findAll(PrpLSurvey.class,queryRule);
		if(prpLSurveys!=null && !prpLSurveys.isEmpty()){
			prpLSurveyVos = Beans.copyDepth().from(prpLSurveys).toList(PrpLSurveyVo.class);
		}
		return prpLSurveyVos;
	}
	
	@Override
	public PrpLSurveyVo findSurveyVo(Long id) {
		PrpLSurveyVo prpLSurveyVo = null;
		PrpLSurvey prpLSurvey = databaseDao.findByPK(PrpLSurvey.class,id);
		if(prpLSurvey!=null){
			prpLSurveyVo = new PrpLSurveyVo();
			Beans.copy().from(prpLSurvey).to(prpLSurveyVo);
		}		
		return prpLSurveyVo;	
	}
	
	
	@Override
	public void saveSurvey(PrpLSurveyVo prpLSurveyVo,SysUserVo sysUserVo,BigDecimal flowTaskId) {
		prpLSurveyVo.setCreateTime(new Date());
		prpLSurveyVo.setCreateUser(sysUserVo.getUserCode());
		prpLSurveyVo.setHandlerStatus(HandlerStatus.INIT);
		PrpLSurvey prpLSurvey = new PrpLSurvey();
		Beans.copy().from(prpLSurveyVo).excludeNull().to(prpLSurvey);
		databaseDao.save(PrpLSurvey.class, prpLSurvey);
		
		WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
		taskVo.setRegistNo(prpLSurveyVo.getRegistNo());
		taskVo.setHandlerIdKey(prpLSurvey.getId().toString());

		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setFlowId(prpLSurveyVo.getFlowId());
		submitVo.setCurrentNode(FlowNode.valueOf(prpLSurveyVo.getNodeCode()));
		submitVo.setNextNode(FlowNode.Survey);
		String comCode = policyViewService.getPolicyComCode(prpLSurveyVo.getRegistNo());
		submitVo.setAssignCom(comCode);
		submitVo.setComCode(comCode);
		submitVo.setTaskInKey(prpLSurveyVo.getRegistNo());
		submitVo.setTaskInUser(sysUserVo.getUserCode());
		submitVo.setFlowTaskId(flowTaskId);

		wfTaskHandleService.addSimpleTask(taskVo,submitVo);
	}

	
	@Override
	public void updateSurvey(PrpLSurveyVo prpLSurveyVo) {
		PrpLSurvey prpLSurvey = Beans.copyDepth().from(prpLSurveyVo).to(PrpLSurvey.class);
		databaseDao.update(PrpLSurvey.class,prpLSurvey);

	}

	@Override
	public void pinganSaveSurvey(PrpLSurveyVo prpLSurveyVo,
			SysUserVo sysUserVo, BigDecimal flowTaskId) {
		//prpLSurveyVo.setCreateTime(new Date());
		prpLSurveyVo.setCreateUser(sysUserVo.getUserCode());
		PrpLSurvey prpLSurvey = new PrpLSurvey();
		Beans.copy().from(prpLSurveyVo).excludeNull().to(prpLSurvey);
		databaseDao.save(PrpLSurvey.class, prpLSurvey);
		
		WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
		taskVo.setRegistNo(prpLSurveyVo.getRegistNo());
		taskVo.setHandlerIdKey(prpLSurvey.getId().toString());

		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setFlowId(prpLSurveyVo.getFlowId());
		submitVo.setCurrentNode(FlowNode.valueOf(prpLSurveyVo.getNodeCode()));
		submitVo.setNextNode(FlowNode.Survey);
		String comCode = policyViewService.getPolicyComCode(prpLSurveyVo.getRegistNo());
		submitVo.setAssignCom(comCode);
		submitVo.setComCode(comCode);
		submitVo.setTaskInKey(prpLSurveyVo.getRegistNo());
		submitVo.setTaskInUser(sysUserVo.getUserCode());
		submitVo.setFlowTaskId(flowTaskId);

		wfTaskHandleService.addSimpleTask(taskVo,submitVo);
	}
}
