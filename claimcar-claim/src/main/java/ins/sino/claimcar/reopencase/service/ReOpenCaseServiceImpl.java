package ins.sino.claimcar.reopencase.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.certify.service.CertifyPubService;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.endcase.po.PrpLEndCase;
import ins.sino.claimcar.endcase.service.ReOpenCaseService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.endcase.vo.PrpLReCaseTextVo;
import ins.sino.claimcar.endcase.vo.PrpLReCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.constant.SubmitType;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.reopencase.po.PrpLReCase;
import ins.sino.claimcar.reopencase.po.PrpLReCaseText;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, timeout = 1000000)
@Path("reOpenCaseService")
public class ReOpenCaseServiceImpl implements ReOpenCaseService {
	
	private static Logger logger = LoggerFactory.getLogger(ReOpenCaseServiceImpl.class);

	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	private ClaimTaskService claimTaskService;
	@Autowired
	private CompensateTaskService compensateTaskService;
	@Autowired
	AssignService assignService;
	@Autowired
	CertifyPubService certifyPubService;
	@Autowired
	private PolicyViewService policyViewService;
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.reopencase.service.ReOpenCaseService#findEndCaseByRegistNo(java.lang.String)
	 */
/*	public List<PrpLClaimVo> findClaimVoByRegistNo(String registNo){
		Date date=new Date();
		List<PrpLClaimVo> claimVos1 = claimTaskService.findClaimListByRegistNo(registNo);
		List<PrpLClaimVo> claimVos2 = new ArrayList<PrpLClaimVo>();
		for(PrpLClaimVo claimVo:claimVos1){
			if(claimVo.getEndCaserCode()!=null&&claimVo.getCaseNo()!=null&&
					claimVo.getEndCaseTime()!=null&&claimVo.getEndCaseTime().compareTo(date)<0){
				claimVos2.add(claimVo);
			}
		}
		return claimVos2;
	}*/
	@Override
	public List<PrpLEndCaseVo> findEndCaseByRegistNo(String registNo){
		QueryRule queryRule1 = QueryRule.getInstance();
		QueryRule queryRule2 = QueryRule.getInstance();
		List<PrpLEndCaseVo> endCaseList=new ArrayList<PrpLEndCaseVo>();
		List<PrpLEndCase> endCompelCase=new ArrayList<PrpLEndCase>();
		List<PrpLEndCase> endCase=new ArrayList<PrpLEndCase>();
		List<PrpLEndCase> endBusinessCase=new ArrayList<PrpLEndCase>();
		Date date=new Date();
		queryRule1.addEqual("registNo", registNo);
		queryRule1.addLessThan("endCaseDate", date);
		queryRule1.addEqual("riskCode", "1101");
		endCompelCase=databaseDao.findAll(PrpLEndCase.class, queryRule1);
		for(int i=1;i<endCompelCase.size();i++){
			if(endCompelCase.get(0).getEndCaseDate().compareTo(endCompelCase.get(i).getEndCaseDate())<0){
				endCompelCase.set(0, endCompelCase.get(i));
			}
		}
		queryRule2.addEqual("registNo", registNo);
		queryRule2.addLessThan("endCaseDate", date);
		queryRule2.addNotEqual("riskCode", "1101");
		endBusinessCase=databaseDao.findAll(PrpLEndCase.class, queryRule2);
		for(int i=1;i<endBusinessCase.size();i++){
			if(endBusinessCase.get(0).getEndCaseDate().compareTo(endBusinessCase.get(i).getEndCaseDate())<0){
				endBusinessCase.set(0, endBusinessCase.get(i));
			}
		}
		if(endCompelCase.size()>0&&endBusinessCase.size()>0){
			endCase.add(endCompelCase.get(0));
			endCase.add(endBusinessCase.get(0));
		}else if(endCompelCase.size()>0&&endBusinessCase.size()<=0){
			endCase.add(endCompelCase.get(0));
		}else if(endCompelCase.size()<=0&&endBusinessCase.size()>0){
			endCase.add(endBusinessCase.get(0));
		}
		endCaseList=Beans.copyDepth().from(endCase).toList(PrpLEndCaseVo.class);
		return endCaseList;
	}
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.reopencase.service.ReOpenCaseService#findReCaseVoByEndCaseNo(java.lang.String)
	 */
	@Override
	public PrpLReCaseVo findReCaseVoByEndCaseNo(String endCaseNo){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("endCaseNo", endCaseNo);
		queryRule.addDescOrder("createTime");
		List<PrpLReCase> reCase=databaseDao.findAll(PrpLReCase.class, queryRule);
		PrpLReCaseVo reCaseVo=null;
		if(reCase.size()>0){
			reCaseVo = new PrpLReCaseVo();
			reCaseVo=Beans.copyDepth().from(reCase.get(0)).to(PrpLReCaseVo.class);
		}
		return reCaseVo;
	}
	@Override
	public PrpLReCaseVo findReCaseVoByEndCaseNoA(String endCaseNo,Integer reOpenCount){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("endCaseNo", endCaseNo);
		queryRule.addDescOrder("id");
		List<PrpLReCase> reCase=databaseDao.findAll(PrpLReCase.class, queryRule);
		PrpLReCaseVo reCaseVo=null;
		if(reCase.size()>0&&reCase.size()>=(reOpenCount+1)){
			reCaseVo = new PrpLReCaseVo();
			reCaseVo=Beans.copyDepth().from(reCase.get(reOpenCount)).to(PrpLReCaseVo.class);
		}
		return reCaseVo;
	}
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.reopencase.service.ReOpenCaseService#countReCaseByClaimNo(java.lang.String)
	 */
	@Override
	public Long countReCaseByClaimNo(String claimNo){
		QueryRule queryRule = QueryRule.getInstance();
		List<PrpLReCaseVo> reCaseVoList=new ArrayList<PrpLReCaseVo>();
		List<PrpLReCase> reCaseList=new ArrayList<PrpLReCase>();
		queryRule.addEqual("claimNo", claimNo);
		reCaseList=databaseDao.findAll(PrpLReCase.class, queryRule);
		reCaseVoList=Beans.copyDepth().from(reCaseList).toList(PrpLReCaseVo.class);
		return Long.valueOf(reCaseVoList.size());
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.reopencase.service.ReOpenCaseService#saveOrUpdateReCase(ins.sino.claimcar.endcase.vo.PrpLReCaseVo)
	 */
	@Override
	public Long saveOrUpdateReCase(PrpLReCaseVo prpLReCaseVo){
		PrpLReCase prpLReCase=Beans.copyDepth().from(prpLReCaseVo).to(PrpLReCase.class);
		List<PrpLReCaseText> textList = prpLReCase.getPrpLReCaseTexts();
		if(textList!=null && !textList.isEmpty()){
			for(PrpLReCaseText caseText :textList){
				caseText.setPrpLReCase(prpLReCase);
			}
		}
		
		if(prpLReCase.getId()!=null){
			databaseDao.update(PrpLReCase.class, prpLReCase);
		}else{
			databaseDao.save(PrpLReCase.class, prpLReCase);
		}
		return prpLReCase.getId();
	}
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.reopencase.service.ReOpenCaseService#findEndCaseVoByEndCaseNo(java.lang.String)
	 */
	@Override
	public PrpLEndCaseVo findEndCaseVoByEndCaseNo(String endCaseNo){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("endCaseNo", endCaseNo);
		List<PrpLEndCase> endCase=databaseDao.findAll(PrpLEndCase.class, queryRule);
		PrpLEndCaseVo endCaseVo=new PrpLEndCaseVo();
		if(endCase.size()>0){
			endCaseVo=Beans.copyDepth().from(endCase.get(0)).to(PrpLEndCaseVo.class);
		}
		return endCaseVo;
	}
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.reopencase.service.ReOpenCaseService#findEndCaseVoByClaimNo(java.lang.String)
	 */
	@Override
	public PrpLEndCaseVo findEndCaseVoByClaimNo(String claimNo){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("claimNo", claimNo);
		queryRule.addDescOrder("endCaseDate");
		List<PrpLEndCase> endCase=databaseDao.findAll(PrpLEndCase.class, queryRule);
		PrpLEndCaseVo endCaseVo=Beans.copyDepth().from(endCase.get(0)).to(PrpLEndCaseVo.class);
		return endCaseVo;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.reopencase.service.ReOpenCaseService#superior(ins.sino.claimcar.endcase.vo.PrpLReCaseVo, java.lang.Double, java.lang.String, ins.platform.vo.SysUserVo)
	 */
	@Override
	public String superior(PrpLReCaseVo prpLReCaseVo,Double flowTaskId,String nextNode,SysUserVo userVo){
		try{
			Date date = new Date();
			WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
			WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
			// //轮询
			// SysUserVo assUserVo = assignService.execute(FlowNode.valueOf(nextNode),"00010000");
			// if(assUserVo == null){
			// throw new IllegalArgumentException(FlowNode.valueOf(nextNode)+"未配置人员 ！");
			// }
			// 重开赔案审核到岗
			submitVo.setAssignCom(CodeConstants.TOPCOM);
			submitVo.setAssignUser(null);
			submitVo.setFlowId(wfTaskVo.getFlowId());
			submitVo.setFlowTaskId(wfTaskVo.getTaskId());
			submitVo.setHandleIdKey(wfTaskVo.getHandlerIdKey());
			submitVo.setTaskInKey(prpLReCaseVo.getEndCaseNo());
			submitVo.setComCode(wfTaskVo.getComCode());// 提交到总公司
			submitVo.setTaskInUser(userVo.getUserCode());
			submitVo.setCurrentNode(FlowNode.ReOpenApp);
			submitVo.setNextNode(FlowNode.valueOf(nextNode));
			submitVo.setSubmitType(SubmitType.U);
			submitVo.setHandlertime(date);
			submitVo.setHandleruser(userVo.getUserCode());

			taskVo.setRegistNo(wfTaskVo.getRegistNo());
			taskVo.setItemName(wfTaskVo.getItemName());
			taskVo.setBussTag(wfTaskVo.getBussTag());
			taskVo.setShowInfoXml(wfTaskVo.getShowInfoXML());
			taskVo.setHandlerIdKey(wfTaskVo.getHandlerIdKey());
			taskVo.setRiskCode(wfTaskVo.getRiskCode());
			taskVo.setClaimNo(wfTaskVo.getClaimNo());
			PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.submitSimpleTask(taskVo,submitVo);
			prpLReCaseVo.setCheckStatus("5");
			prpLReCaseVo.setPrpLReCaseTexts(getReCaseTextList("5",prpLReCaseVo.getCheckOpinion(),userVo));
			saveOrUpdateReCase(prpLReCaseVo);
		}catch(Exception e){
			logger.error(prpLReCaseVo.getClaimNo()+"重开赔案提交上级报错："+e);
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.reopencase.service.ReOpenCaseService#save(ins.sino.claimcar.endcase.vo.PrpLReCaseVo, java.lang.Double)
	 */
	@Override
	public Long save(PrpLReCaseVo prpLReCaseVo,Double flowTaskId,SysUserVo userVo){
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
		wfTaskHandleService.tempSaveTask(flowTaskId, wfTaskVo.getHandlerIdKey(), userVo.getUserCode(), userVo.getComCode());
		prpLReCaseVo.setCheckStatus("9");
		Long reOpenId=saveOrUpdateReCase(prpLReCaseVo);
		return reOpenId;
	}
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.reopencase.service.ReOpenCaseService#submit(java.lang.Double, java.lang.String, ins.sino.claimcar.endcase.vo.PrpLReCaseVo, ins.platform.vo.SysUserVo)
	 */
	@Override
	public void submit(Double flowTaskId,String auditStatus,PrpLReCaseVo prpLReCaseVo,SysUserVo userVo){
		try{
			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
			PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(prpLReCaseVo.getRegistNo());
			PrpLCertifyMainVo certifyMainVo = certifyPubService.findPrpLCertifyMainVoByRegistNo(prpLReCaseVo.getRegistNo());

			// List<PrpLWfTaskVo> wfTaskVoList=wfTaskHandleService.findEndTask(prpLReCaseVo.getRegistNo(), String.valueOf(checkVo.getId()), FlowNode.Certi);
			Boolean bl = wfTaskHandleService.existTaskByNodeCode(prpLReCaseVo.getRegistNo(),FlowNode.Certi,prpLReCaseVo.getRegistNo(),"1");

			WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
			WfSimpleTaskVo taskVo = new WfSimpleTaskVo();

			taskVo.setRegistNo(wfTaskVo.getRegistNo());
			taskVo.setItemName(wfTaskVo.getItemName());
			taskVo.setBussTag(wfTaskVo.getBussTag());
			taskVo.setShowInfoXml(wfTaskVo.getShowInfoXML());
			taskVo.setRiskCode(wfTaskVo.getRiskCode());
			taskVo.setClaimNo(wfTaskVo.getClaimNo());
			submitVo.setFlowId(wfTaskVo.getFlowId());
			submitVo.setFlowTaskId(wfTaskVo.getTaskId());
			submitVo.setHandleIdKey(wfTaskVo.getHandlerIdKey());
			submitVo.setTaskInKey(wfTaskVo.getTaskInKey());
			submitVo.setComCode(wfTaskVo.getComCode());
			submitVo.setTaskInUser(userVo.getUserCode());
			submitVo.setCurrentNode(FlowNode.ReOpenVrf);
			submitVo.setAssignCom(userVo.getComCode());
			submitVo.setAssignUser(userVo.getUserCode());
			submitVo.setHandlertime(prpLReCaseVo.getUpdateTime());
			submitVo.setHandleruser(prpLReCaseVo.getUpdateUser());

			if(auditStatus.equals("pass")&&bl){
				BigDecimal FlowTaskId = this.findTaskByCertify(prpLReCaseVo.getRegistNo(),String.valueOf(checkVo.getId()),
						String.valueOf(certifyMainVo.getId()),wfTaskVo.getClaimNo());
				wfTaskHandleService.submitReOpenTask(FlowTaskId,taskVo,submitVo);
				prpLReCaseVo.setCheckStatus("6");
				prpLReCaseVo.setPrpLReCaseTexts(getReCaseTextList("6",prpLReCaseVo.getCheckOpinion(),userVo));
				saveOrUpdateReCase(prpLReCaseVo);
				claimTaskService.reOpenClaimWirteBack(prpLReCaseVo.getClaimNo(),FlowNode.ReOpenVrf,"");
				// 重开赔案刷新立案轨迹表
				claimTaskService.calcClaimKindHisByReOpen(prpLReCaseVo.getClaimNo());
			}else if(auditStatus.equals("pass")&& !bl){// 该报案号下已有赔案重开
				submitVo.setNextNode(FlowNode.END);
				wfTaskHandleService.submitSimpleTask(taskVo,submitVo);
				prpLReCaseVo.setCheckStatus("6");
				prpLReCaseVo.setPrpLReCaseTexts(getReCaseTextList("6",prpLReCaseVo.getCheckOpinion(),userVo));
				saveOrUpdateReCase(prpLReCaseVo);
				claimTaskService.reOpenClaimWirteBack(prpLReCaseVo.getClaimNo(),FlowNode.ReOpenVrf,"");
				// 重开赔案刷新立案轨迹表
				claimTaskService.calcClaimKindHisByReOpen(prpLReCaseVo.getClaimNo());
			}else{
				submitVo.setNextNode(FlowNode.END);
				wfTaskHandleService.submitSimpleTask(taskVo,submitVo);
				prpLReCaseVo.setCheckStatus("7");
				prpLReCaseVo.setPrpLReCaseTexts(getReCaseTextList("7",prpLReCaseVo.getCheckOpinion(),userVo));
				saveOrUpdateReCase(prpLReCaseVo);
				PrpLEndCaseVo endCaseVo = this.findEndCaseVoByEndCaseNo(prpLReCaseVo.getEndCaseNo());
				claimTaskService.reOpenClaimWirteBack(prpLReCaseVo.getClaimNo(),FlowNode.ReOpenVrf,endCaseVo.getCreateUser());
			}
		}catch(Exception e){
			logger.error(prpLReCaseVo.getClaimNo()+"重开赔案提交失败："+e);
		}
		
	}
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.reopencase.service.ReOpenCaseService#returnAndModify(java.lang.Double, ins.sino.claimcar.endcase.vo.PrpLReCaseVo)
	 */
	@Override
	public void returnAndModify(Double flowTaskId,PrpLReCaseVo prpLReCaseVo,SysUserVo userVo){
		WfSimpleTaskVo taskVo=new WfSimpleTaskVo();
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
		List<PrpLWfTaskVo> wfTaskVoList=wfTaskHandleService.findEndTask(prpLReCaseVo.getRegistNo(), prpLReCaseVo.getEndCaseNo(), FlowNode.ReOpen);
		submitVo.setFlowId(wfTaskVo.getFlowId());
		submitVo.setFlowTaskId(wfTaskVo.getTaskId());
		submitVo.setHandleIdKey(wfTaskVo.getHandlerIdKey());
		submitVo.setTaskInKey(wfTaskVo.getTaskInKey());
		submitVo.setComCode(wfTaskVo.getComCode());
		submitVo.setTaskInUser(wfTaskVo.getTaskInUser());
		submitVo.setCurrentNode(FlowNode.ReOpenVrf);
		submitVo.setNextNode(FlowNode.ReOpenApp);
		submitVo.setAssignCom(wfTaskVo.getAssignCom());
		submitVo.setAssignUser(wfTaskVo.getAssignUser());
		submitVo.setSubmitType(SubmitType.B);
		
		taskVo.setRegistNo(wfTaskVo.getRegistNo());
		taskVo.setItemName(wfTaskVo.getItemName());
		taskVo.setBussTag(wfTaskVo.getBussTag());
		taskVo.setShowInfoXml(wfTaskVo.getShowInfoXML());
		taskVo.setHandlerIdKey(wfTaskVo.getHandlerIdKey());
		wfTaskHandleService.submitSimpleTask(taskVo, submitVo);
		prpLReCaseVo.setCheckStatus("8");
		prpLReCaseVo.setPrpLReCaseTexts(getReCaseTextList("8",prpLReCaseVo.getCheckOpinion(),userVo));
		saveOrUpdateReCase(prpLReCaseVo);
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.reopencase.service.ReOpenCaseService#findReCaseByClaimNo(java.lang.String)
	 */
	@Override
	public List<PrpLReCaseVo> findReCaseByClaimNo(String claimNo){
		List<PrpLReCaseVo> reCaseVoList=new ArrayList<PrpLReCaseVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("claimNo", claimNo);
		queryRule.addAscOrder("seriesNo");
		List<PrpLReCase> reCaseList=databaseDao.findAll(PrpLReCase.class, queryRule);
		if(reCaseList!=null&&reCaseList.size()>0){
			reCaseVoList=Beans.copyDepth().from(reCaseList).toList(PrpLReCaseVo.class);
		}
		return reCaseVoList;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.reopencase.service.ReOpenCaseService#findReCase(java.lang.String, java.lang.String)
	 */
	@Override
	public List<PrpLReCaseVo> findReCase(String registNo,String claimNo){
		List<PrpLReCaseVo> reCaseVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("claimNo", claimNo);
		queryRule.addEqual("registNo", registNo);
		List<PrpLReCase> reCaseList=databaseDao.findAll(PrpLReCase.class, queryRule);
		if(reCaseList!=null&&reCaseList.size()>0){
			reCaseVo = Beans.copyDepth().from(reCaseList).toList(PrpLReCaseVo.class);
		}
		return reCaseVo;
	}

	// 根据重开赔案原因代码返回重开原因
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.reopencase.service.ReOpenCaseService#getOpinion(int)
	 */
	@Override
	public String getOpinion(int reasonCode){
		String opinion="";
		
		switch (reasonCode) {
		case 1:
			opinion = "诉讼";
			break;
		case 2:
			opinion = "后续治疗";
			break;
		case 3:
			opinion = "客户追加索赔";
			break;
		case 4:
			opinion = "客户要求退赔";
			break;
		case 5:
			opinion = "追偿";
			break;
		case 6:
			opinion = "反欺诈";
			break;
		case 7:
			opinion = "追加直接理赔费用";
			break;
		default:
			opinion = "其他";
		}
		return opinion;
	}

	// 设置意见表
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.reopencase.service.ReOpenCaseService#getReCaseTextList(java.lang.String, java.lang.String)
	 */
	@Override
	public List<PrpLReCaseTextVo> getReCaseTextList(String checkStatus,String checkOpinion,SysUserVo userVo){
		Date date = new Date();
		List<PrpLReCaseTextVo> prpLReCaseTexts=new ArrayList<PrpLReCaseTextVo>();
		PrpLReCaseTextVo prpLReCaseTextVo=new PrpLReCaseTextVo();
		prpLReCaseTextVo.setOperatorName(userVo.getUserName());
		prpLReCaseTextVo.setComName(userVo.getComCode());
		prpLReCaseTextVo.setInputTime(date);
		prpLReCaseTextVo.setCheckStatus(checkStatus);
		prpLReCaseTextVo.setCheckOpinion(checkOpinion);
		prpLReCaseTexts.add(prpLReCaseTextVo);
		return prpLReCaseTexts;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.reopencase.service.ReOpenCaseService#setWfSimpleTaskVo(ins.sino.claimcar.flow.vo.PrpLWfTaskVo, java.lang.String, java.lang.String)
	 */
	@Override
	public WfSimpleTaskVo setWfSimpleTaskVo(PrpLWfTaskVo wfTaskVo,String handlerIdKey,String registNo){
		WfSimpleTaskVo taskVo=new WfSimpleTaskVo();
		taskVo.setRegistNo(wfTaskVo.getRegistNo());
		taskVo.setItemName(wfTaskVo.getItemName());
		taskVo.setBussTag(wfTaskVo.getBussTag());
		taskVo.setShowInfoXml(wfTaskVo.getShowInfoXML());
		taskVo.setHandlerIdKey(handlerIdKey);
		taskVo.setRegistNo(registNo);
		return taskVo;
	}
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.reopencase.service.ReOpenCaseService#setWfTaskSubmitVo(ins.sino.claimcar.flow.vo.PrpLWfTaskVo, java.lang.String, java.lang.String)
	 */
	@Override
	public WfTaskSubmitVo setWfTaskSubmitVo(PrpLWfTaskVo wfTaskVo,String handleIdKey,String taskInKey,SysUserVo userVo){
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setFlowId(wfTaskVo.getFlowId());
		submitVo.setFlowTaskId(wfTaskVo.getTaskId());// 设置重开赔案登记taskId
		submitVo.setHandleIdKey(handleIdKey);
		submitVo.setTaskInKey(taskInKey);
		submitVo.setComCode(userVo.getComCode());
		submitVo.setTaskInUser(userVo.getUserCode());
		submitVo.setCurrentNode(FlowNode.ReOpenApp);
		submitVo.setNextNode(FlowNode.ReOpenVrf);
		submitVo.setAssignCom(userVo.getComCode());
		submitVo.setAssignUser(userVo.getUserCode());
		return submitVo;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.reopencase.service.ReOpenCaseService#findReCaseByPk(java.lang.Long)
	 */
	@Override
	public PrpLReCaseVo findReCaseByPk(Long reOpenId){
		if(reOpenId==null){
			return null;
		}
		PrpLReCase reCase = databaseDao.findByPK(PrpLReCase.class, reOpenId);
		PrpLReCaseVo reCaseVo = Beans.copyDepth().from(reCase).to(PrpLReCaseVo.class);
		return reCaseVo;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.reopencase.service.ReOpenCaseService#getTaskName(java.lang.String, java.lang.String)
	 */
	@Override
	public String getTaskName(String saveType,String nextNode){
		String taskName = "";
		if("pass".equals(saveType)){
			taskName = "审核通过";
		}else if("failed".equals(saveType)){
			taskName = "审核不通过";
		}else if("superior".equals(saveType)){
			taskName = "提交"+FlowNode.valueOf(nextNode).getName();
		}
		return taskName;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.reopencase.service.ReOpenCaseService#reOpenValid(java.lang.String)
	 */
	@Override
	public String reOpenValid(String registNo){
		String retData = "ok";
		List<PrpLClaimVo> claimVoList = claimTaskService.findClaimListByRegistNo(registNo);
		if(claimVoList.size()>=2){
			for(PrpLClaimVo claimVo:claimVoList){
				PrpLCompensateVo compensateVo = compensateTaskService.searchCompByClaimNo(claimVo.getClaimNo());
				if(compensateVo.getCompensateNo()==null || !wfTaskHandleService.existTaskByNodeCode(registNo,FlowNode.VClaim,compensateVo.getCompensateNo(),"1")){
					if(claimVo.getRiskCode().equals("1101")){
						retData = "该案件的交强险核赔未处理";
						return retData;
					}else{
						retData = "该案件的商业险核赔未处理";
						return retData;
					}
				}
			}
		}
		return retData;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.reopencase.service.ReOpenCaseService#findTaskByCertify(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public BigDecimal findTaskByCertify(String registNo,String checkId,String certifyId,String claimNo){
		List<PrpLWfTaskVo> wfTaskVoList = wfTaskHandleService.findEndTask(registNo, checkId, FlowNode.Certi);
		if(wfTaskVoList.size()>0){
			return wfTaskVoList.get(0).getTaskId();
		}else{
			List<PrpLWfTaskVo> wfTaskList = wfTaskHandleService.findEndTask(registNo, certifyId, FlowNode.Certi);
			if(wfTaskList.size()>0){
				return wfTaskList.get(0).getTaskId();
			}else{
				// TODO 待完成，handleridkey是立案号，交强商业取交强,或者直接查单证工作流
				return wfTaskVoList.get(0).getTaskId(); 
			}
		}
	}

	// 判断该案件是否重开过但审核不通过
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.reopencase.service.ReOpenCaseService#isFail(java.lang.String)
	 */
	@Override
	public Boolean isFail(String endCaseNo){
		PrpLReCaseVo reCaseVo = this.findReCaseVoByEndCaseNo(endCaseNo);
		if(reCaseVo == null){
			return false;
		}
		if(reCaseVo.getCheckStatus().equals("7")){
			return true;
		}else{
			return false;
		}
	}
	
	public List<PrpLReCaseTextVo> findReCaseTextByReCaseId(Long reCaseId){
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("prpLReCase.id",reCaseId);
		qr.addDescOrder("inputTime");
		List<PrpLReCaseText> reCaseList=databaseDao.findAll(PrpLReCaseText.class, qr);
		List<PrpLReCaseTextVo> reCaseTextVoList = Beans.copyDepth().from(reCaseList).toList(PrpLReCaseTextVo.class);
		return reCaseTextVoList;
	}

	@Override
	public void appSubmit(String registNo,String[] claimNoArr,String handleStatus,PrpLReCaseVo prpLReCaseVo,SysUserVo userVo) throws Exception{
		Date date = new Date();
		List<PrpLEndCaseVo> endCaseList=this.findEndCaseByRegistNo(registNo);
		if(handleStatus.equals("6")){
			PrpLReCaseVo reCase=this.findReCaseVoByEndCaseNo(prpLReCaseVo.getEndCaseNo());
			List<PrpLWfTaskVo> wfTaskVoList=wfTaskHandleService.findEndTask(reCase.getRegistNo(), reCase.getEndCaseNo(), FlowNode.ReOpen);
			WfSimpleTaskVo taskVo=this.setWfSimpleTaskVo(wfTaskVoList.get(0), reCase.getEndCaseNo(), reCase.getRegistNo());
			WfTaskSubmitVo submitVo = this.setWfTaskSubmitVo(wfTaskVoList.get(0), reCase.getEndCaseNo(), reCase.getRegistNo(),userVo);
			PrpLWfTaskVo WfTaskVo=wfTaskHandleService.submitReOpenApp(taskVo, submitVo);
			reCase.setCheckStatus("4");
			this.saveOrUpdateReCase(reCase);
		}else{
		for(String claimNo:claimNoArr){
			PrpLEndCaseVo endCase=this.findEndCaseVoByClaimNo(claimNo);
			WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
			WfSimpleTaskVo taskVo=new WfSimpleTaskVo();
			taskVo.setClaimNo(claimNo);
			for(int i=0;i<endCaseList.size();i++){
				if(endCaseList.get(i).getClaimNo().equals(claimNo)){
					Boolean existReOpen = wfTaskHandleService.existTaskByNodeCode(registNo, FlowNode.ReOpen, endCase.getEndCaseNo(), "0");
					if(existReOpen){
						if("1101".equals(endCase.getRiskCode())){
								throw new Exception("已重开交强赔案！");
						}else{
								throw new Exception("已重开商业赔案！");
						}
					}
						// 拼装taskVo和submitVo
					PrpLWfTaskVo wfEndCaseTaskVo = new PrpLWfTaskVo();
					List<PrpLWfTaskVo> wfTaskVos = wfTaskHandleService.findEndTask(registNo,endCaseList.get(i).getCompensateNo(),FlowNode.EndCas);
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
							throw new Exception("没有查询到结案节点！");
					}
					
					taskVo.setHandlerIdKey(endCaseList.get(i).getEndCaseNo());
					taskVo.setRegistNo(endCaseList.get(i).getRegistNo());
					taskVo.setRiskCode(endCaseList.get(i).getRiskCode());
					
						submitVo.setHandleIdKey(endCaseList.get(i).getClaimNo());// 立案号赋值给HandleIdKey
						submitVo.setTaskInKey(endCaseList.get(i).getEndCaseNo());// 结案号赋值给taskInKey
					submitVo.setComCode(wfEndCaseTaskVo.getComCode());
					submitVo.setTaskInUser(userVo.getUserCode());
					submitVo.setCurrentNode(FlowNode.EndCas);
					submitVo.setNextNode(FlowNode.ReOpenApp);
					submitVo.setAssignCom(userVo.getComCode());
					submitVo.setAssignUser(userVo.getUserCode());
					submitVo.setHandlertime(date);
					submitVo.setHandleruser(userVo.getUserCode());
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
						// //轮询
//					    SysUserVo assUserVo = assignService.execute(FlowNode.ReOpenVrf_LV1,userVo.getComCode());
//						if(assUserVo == null){
						// throw new IllegalArgumentException(FlowNode.ReOpenVrf_LV1+"未配置人员 ！");
//						}
//						submitVo.setAssignCom(assUserVo.getComCode());
//						submitVo.setAssignUser(assUserVo.getUserCode());
						submitVo.setAssignCom(wfEndCaseTaskVo.getComCode());// 重开赔案审核到岗
					    submitVo.setAssignUser(null);
					    submitVo.setHandleruser(null);
					    submitVo.setHandlertime(null);
					PrpLWfTaskVo WfTaskVo=wfTaskHandleService.submitReOpenApp(taskVo, submitVo);
						// 回写立案表的结案信息
					claimTaskService.reOpenClaimWirteBack(claimNo, FlowNode.ReOpenApp,"");
					
					prpLReCaseVo.setEndCaseDate(endCaseList.get(i).getEndCaseDate());
					prpLReCaseVo.setEndCaseNo(endCaseList.get(i).getEndCaseNo());
					prpLReCaseVo.setCompensateNo(endCaseList.get(i).getCompensateNo());
					prpLReCaseVo.setRegistNo(endCaseList.get(i).getRegistNo());
					prpLReCaseVo.setEndCaseUserCode(endCaseList.get(i).getCreateUser());
					prpLReCaseVo.setSeriesNo(this.countReCaseByClaimNo(endCaseList.get(i).getClaimNo()));
					prpLReCaseVo.setClaimNo(endCaseList.get(i).getClaimNo());
					prpLReCaseVo.setOpenCaseUserCode(userVo.getUserCode());
					prpLReCaseVo.setOpenCaseUserName(userVo.getUserName());
					prpLReCaseVo.setOpenCaseDate(date);
					prpLReCaseVo.setCreateTime(date);
					prpLReCaseVo.setCheckStatus("4");
					prpLReCaseVo.setFlag(endCaseList.get(i).getRiskCode());
					
					PrpLCMainVo prpLCMainVo=policyViewService.getPrpLCMainByRegistNoAndPolicyNo(endCaseList.get(i).getRegistNo(), endCaseList.get(i).getPolicyNo());
					PrpLClaimVo prpLClaimVo=claimTaskService.findClaimVoByClaimNo(endCaseList.get(i).getClaimNo());
					prpLReCaseVo.setInsuredName(prpLCMainVo.getInsuredName());
					prpLReCaseVo.setMercyFlag(prpLClaimVo.getMercyFlag());
					prpLReCaseVo.setRemark(endCaseList.get(i).getPolicyNo());
						// 设置意见表属性
					List<PrpLReCaseTextVo> prpLReCaseTexts=new ArrayList<PrpLReCaseTextVo>();
					PrpLReCaseTextVo prpLReCaseTextVo=new PrpLReCaseTextVo();
					prpLReCaseTextVo.setOperatorName(userVo.getUserName());
					prpLReCaseTextVo.setComName(userVo.getComCode());
					prpLReCaseTextVo.setInputTime(date);
					prpLReCaseTextVo.setCheckStatus("4");
					prpLReCaseTextVo.setOpenReasonDetail(prpLReCaseVo.getOpenReasonDetail());
					int n=Integer.valueOf(prpLReCaseVo.getOpenReasonCode());
					prpLReCaseTextVo.setCheckOpinion(this.getOpinion(n));
					prpLReCaseTexts.add(prpLReCaseTextVo);
					prpLReCaseVo.setPrpLReCaseTexts(prpLReCaseTexts);
					this.saveOrUpdateReCase(prpLReCaseVo);
				}
			}
		}
		}
	}
	@Override
	public void pinganSubmit(Double flowTaskId, String auditStatus,
			PrpLReCaseVo prpLReCaseVo, SysUserVo userVo) {
		try{
			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
			PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(prpLReCaseVo.getRegistNo());
			PrpLCertifyMainVo certifyMainVo = certifyPubService.findPrpLCertifyMainVoByRegistNo(prpLReCaseVo.getRegistNo());

			// List<PrpLWfTaskVo> wfTaskVoList=wfTaskHandleService.findEndTask(prpLReCaseVo.getRegistNo(), String.valueOf(checkVo.getId()), FlowNode.Certi);
			Boolean bl = wfTaskHandleService.existTaskByNodeCode(prpLReCaseVo.getRegistNo(),FlowNode.Certi,prpLReCaseVo.getRegistNo(),"1");

			WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
			WfSimpleTaskVo taskVo = new WfSimpleTaskVo();

			taskVo.setRegistNo(wfTaskVo.getRegistNo());
			taskVo.setItemName(wfTaskVo.getItemName());
			taskVo.setBussTag(wfTaskVo.getBussTag());
			taskVo.setShowInfoXml(wfTaskVo.getShowInfoXML());
			taskVo.setRiskCode(wfTaskVo.getRiskCode());
			taskVo.setClaimNo(wfTaskVo.getClaimNo());
			submitVo.setFlowId(wfTaskVo.getFlowId());
			submitVo.setFlowTaskId(wfTaskVo.getTaskId());
			submitVo.setHandleIdKey(wfTaskVo.getHandlerIdKey());
			submitVo.setTaskInKey(wfTaskVo.getTaskInKey());
			submitVo.setComCode(wfTaskVo.getComCode());
			submitVo.setTaskInUser(userVo.getUserCode());
			submitVo.setCurrentNode(FlowNode.ReOpenVrf);
			submitVo.setAssignCom(userVo.getComCode());
			submitVo.setAssignUser(userVo.getUserCode());
			submitVo.setHandlertime(prpLReCaseVo.getUpdateTime());
			submitVo.setHandleruser(prpLReCaseVo.getUpdateUser());

			if(auditStatus.equals("pass")&&bl){
				BigDecimal FlowTaskId = this.findTaskByCertify(prpLReCaseVo.getRegistNo(),String.valueOf(checkVo.getId()),
						String.valueOf(certifyMainVo.getId()),wfTaskVo.getClaimNo());
				wfTaskHandleService.submitReOpenTask(FlowTaskId,taskVo,submitVo);
				/*prpLReCaseVo.setCheckStatus("6");
				prpLReCaseVo.setPrpLReCaseTexts(getReCaseTextList("6",prpLReCaseVo.getCheckOpinion(),userVo));
				saveOrUpdateReCase(prpLReCaseVo);*/
				claimTaskService.reOpenClaimWirteBack(prpLReCaseVo.getClaimNo(),FlowNode.ReOpenVrf,"");
				// 重开赔案刷新立案轨迹表
				claimTaskService.calcClaimKindHisByReOpen(prpLReCaseVo.getClaimNo());
			}else if(auditStatus.equals("pass")&& !bl){// 该报案号下已有赔案重开
				submitVo.setNextNode(FlowNode.END);
				wfTaskHandleService.submitSimpleTask(taskVo,submitVo);
				/*prpLReCaseVo.setCheckStatus("6");
				prpLReCaseVo.setPrpLReCaseTexts(getReCaseTextList("6",prpLReCaseVo.getCheckOpinion(),userVo));
				saveOrUpdateReCase(prpLReCaseVo);*/
				claimTaskService.reOpenClaimWirteBack(prpLReCaseVo.getClaimNo(),FlowNode.ReOpenVrf,"");
				// 重开赔案刷新立案轨迹表
				claimTaskService.calcClaimKindHisByReOpen(prpLReCaseVo.getClaimNo());
			}else{
				submitVo.setNextNode(FlowNode.END);
				wfTaskHandleService.submitSimpleTask(taskVo,submitVo);
				/*prpLReCaseVo.setCheckStatus("7");
				prpLReCaseVo.setPrpLReCaseTexts(getReCaseTextList("7",prpLReCaseVo.getCheckOpinion(),userVo));
				saveOrUpdateReCase(prpLReCaseVo);*/
				PrpLEndCaseVo endCaseVo = this.findEndCaseVoByEndCaseNo(prpLReCaseVo.getEndCaseNo());
				claimTaskService.reOpenClaimWirteBack(prpLReCaseVo.getClaimNo(),FlowNode.ReOpenVrf,endCaseVo.getCreateUser());
			}
		}catch(Exception e){
			logger.error(prpLReCaseVo.getClaimNo()+"重开赔案提交失败："+e);
		}
	}
}
