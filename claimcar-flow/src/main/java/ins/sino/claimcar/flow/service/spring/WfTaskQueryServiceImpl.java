package ins.sino.claimcar.flow.service.spring;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.service.utils.ServiceUserUtils;
import ins.platform.saa.schema.SaaUserPermitCompany;
import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.platform.saa.vo.SaaUserPowerVo;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.utils.DataUtils;
import ins.platform.utils.DateUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysAreaDictVo;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.VeriFlag;
import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.po.PrpLAcheckMain;
import ins.sino.claimcar.flow.po.PrpLWfTaskIn;
import ins.sino.claimcar.flow.po.PrpLWfTaskOut;
import ins.sino.claimcar.flow.po.PrpLWfTaskQuery;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.service.WfTaskService;
import ins.sino.claimcar.flow.util.TaskExtMapUtils;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskQueryResultVo;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceFeeVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.other.po.PrpLAssessorMain;
import ins.sino.claimcar.pay.service.PadPayService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;
import ins.sino.claimcar.utils.SaaPowerUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * <pre>???????????????????????????</pre>
 * @author ???LiuPing
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path(value = "wfTaskQueryService")
public class WfTaskQueryServiceImpl implements WfTaskQueryService {

	private static Logger logger = LoggerFactory.getLogger(WfTaskQueryServiceImpl.class);

	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private SaaUserPowerService saaUserPowerService;
	
	@Autowired
	private CompensateTaskService compensateTaskService;
	
	@Autowired
	private PadPayService padPayService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	LossCarService lossCarService;
	@Autowired
    DeflossHandleService deflossHandleService;
    @Autowired
    PropTaskService propTaskService;
    @Autowired
    BaseDaoService baseDaoService;
    @Autowired
    AreaDictService areaDictService;
    @Autowired
	ManagerService managerService;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    private WfTaskService wfTaskService;
	/* 
	 * @see ins.sino.claimcar.flow.service.WfTaskQueryService#save(ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo)
	 * @param taskQueryVo
	 */
	@Override
	public void save(PrpLWfTaskQueryVo taskQueryVo) {
		PrpLWfTaskQuery po = new PrpLWfTaskQuery();
		Beans.copy().from(taskQueryVo).to(po);
		databaseDao.save(PrpLWfTaskQuery.class,po);
		logger.debug("WfTaskQueryService.po.id="+po.getFlowId());
	}
	
	/* 
	 * @see ins.sino.claimcar.flow.service.WfTaskQueryService#update(ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo)
	 * @param taskQueryVo
	 */
	@Override
	public void update(PrpLWfTaskQueryVo taskQueryVo,String flags) {
		PrpLWfTaskQuery po = databaseDao.findByPK(PrpLWfTaskQuery.class,taskQueryVo.getFlowId());
		// ???????????????????????????
		if(!StringUtils.isBlank(po.getLossPersonInfo()) && !StringUtils.isBlank(taskQueryVo.getLossPersonInfo())){
			StringBuffer lossPersonInfo = new StringBuffer();
			lossPersonInfo.append(taskQueryVo.getLossPersonInfo());
			taskQueryVo.setLossPersonInfo(lossPersonInfo.toString());
		}
		Beans.copy().from(taskQueryVo).excludeNull().excludeEmpty().to(po);
		if("1".equals(flags)){
		    if(taskQueryVo.getPolicyNoLink()!=null){
	            po.setPolicyNoLink(taskQueryVo.getPolicyNoLink());
	        }else if("".equals(taskQueryVo.getPolicyNoLink()) || taskQueryVo.getPolicyNoLink() == null){
	            po.setPolicyNoLink(null);
	        }
		}else{
		    if(taskQueryVo.getPolicyNoLink()!=null){
                po.setPolicyNoLink(taskQueryVo.getPolicyNoLink());
            }
		}
		
		databaseDao.update(PrpLWfTaskQuery.class,po);
	}
	
	@Override
	public ResultPage<WfTaskQueryResultVo> findTaskForWorkBench(PrpLWfTaskQueryVo taskQueryVo) throws Exception {
		
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		String userCode = taskQueryVo.getUserCode();
		String handleStatus = taskQueryVo.getHandleStatus();
		sqlUtil.append(" FROM PrpLWfTaskIn task, PrpLWfTaskQuery qry");
		sqlUtil.append(" WHERE task.flowId= qry.flowId");
		
		//?????????
		if("0".equals(handleStatus)){
			sqlUtil.append(" AND task.handlerStatus =? and  task.workStatus =? ");
			sqlUtil.addParamValue(HandlerStatus.INIT);
			sqlUtil.addParamValue(HandlerStatus.INIT);
			
			sqlUtil.append(" and task.nodeCode in(?,?,?,?,?,?,?) ");
			sqlUtil.addParamValue(FlowNode.Sched.name());
			sqlUtil.addParamValue(FlowNode.Check.name());
			sqlUtil.addParamValue(FlowNode.ChkRe.name());
			sqlUtil.addParamValue(FlowNode.DLoss.name());
			sqlUtil.addParamValue(FlowNode.VLoss.name());
			sqlUtil.addParamValue(FlowNode.VPrice.name());
			sqlUtil.addParamValue(FlowNode.VClaim.name());
			//???????????????
			sqlUtil.append(" and task.assignUser =? ");
			sqlUtil.addParamValue(userCode);
		//?????????
		}else if("1".equals(handleStatus)){
			sqlUtil.append(" AND task.handlerStatus =? and  task.workStatus =? ");
			sqlUtil.addParamValue(HandlerStatus.INIT);
			sqlUtil.addParamValue(HandlerStatus.INIT);
			
			String nodeStr = "";
			String subNodeStr = "";
			//??????/????????????
			if(SaaPowerUtils.hasTask(userCode,"claim.claim.cancel")){
				subNodeStr = subNodeStr+"?,?,?,";
			}
			//??????/????????????
			if(SaaPowerUtils.hasTask(userCode,"claim.claim.cancel.audit")){
				subNodeStr = subNodeStr+"?,?,?,?,";
			}
			//??????????????????
			if(SaaPowerUtils.hasTask(userCode,"claim.recase.audit")){
				subNodeStr = subNodeStr+"?,?,";
			}
			//????????????
			if(SaaPowerUtils.hasTask(userCode,"claim.bigcase.auditing")){
				nodeStr = nodeStr+"?,";
			}
			//?????????????????????
			if(SaaPowerUtils.hasTask(userCode,"claim.verifycarloss") ||
					SaaPowerUtils.hasTask(userCode,"claim.verifyloss")){
				nodeStr = nodeStr+"?,";
			}
			//????????????
			if(SaaPowerUtils.hasTask(userCode,"claim.reclaim")){
				nodeStr = nodeStr+"?,";
			}
			//?????????????????????
			if(SaaPowerUtils.hasTask(userCode,"claim.verifyprice")){
				nodeStr = nodeStr+"?,";
			}
			//????????????
			if(SaaPowerUtils.hasTask(userCode,"claim.certify")){
				nodeStr = nodeStr+"?,";
			}
			//??????
			if(SaaPowerUtils.hasTask(userCode,"claim.compensate")){
				nodeStr = nodeStr+"?,";
			}
			//?????????????????????
			if(SaaPowerUtils.hasTask(userCode,"claim.undwrt")){
				nodeStr = nodeStr+"?,";
			}
			//??????
			if(SaaPowerUtils.hasTask(userCode,"claim.prepay")){
				nodeStr = nodeStr+"?,";
			}
			//??????
			if(SaaPowerUtils.hasTask(userCode,"claim.payment")){
				nodeStr = nodeStr+"?,";
			}
			//??????
			if(SaaPowerUtils.hasTask(userCode,"claim.replevy")){
				nodeStr = nodeStr+"?,";
			}
			
			if(!"".equals(nodeStr)){
				nodeStr = nodeStr.substring(0, nodeStr.length()-1);
				sqlUtil.append(" and (task.nodeCode in("+nodeStr+") ");
				//????????????
				if(SaaPowerUtils.hasTask(userCode,"claim.bigcase.auditing")){
					sqlUtil.addParamValue(FlowNode.ChkBig.name());
				}
				//?????????????????????
				if(SaaPowerUtils.hasTask(userCode,"claim.verifycarloss") ||
						SaaPowerUtils.hasTask(userCode,"claim.verifyloss")){
					sqlUtil.addParamValue(FlowNode.VLoss.name());
				}
				//????????????
				if(SaaPowerUtils.hasTask(userCode,"claim.reclaim")){
					sqlUtil.addParamValue(FlowNode.RecLoss.name());
				}
				//?????????????????????
				if(SaaPowerUtils.hasTask(userCode,"claim.verifyprice")){
					sqlUtil.addParamValue(FlowNode.VPrice.name());
				}
				//????????????
				if(SaaPowerUtils.hasTask(userCode,"claim.certify")){
					sqlUtil.addParamValue(FlowNode.Certi.name());
				}
				//??????
				if(SaaPowerUtils.hasTask(userCode,"claim.compensate")){
					sqlUtil.addParamValue(FlowNode.Compe.name());
				}
				//?????????????????????
				if(SaaPowerUtils.hasTask(userCode,"claim.undwrt")){
					sqlUtil.addParamValue(FlowNode.VClaim.name());
				}
				//??????
				if(SaaPowerUtils.hasTask(userCode,"claim.prepay")){
					sqlUtil.addParamValue(FlowNode.PrePay.name());
				}
				//??????
				if(SaaPowerUtils.hasTask(userCode,"claim.payment")){
					sqlUtil.addParamValue(FlowNode.PadPay.name());
				}
				//??????
				if(SaaPowerUtils.hasTask(userCode,"claim.replevy")){
					sqlUtil.addParamValue(FlowNode.RecPay.name());
				}
			}
			
			if(!"".equals(subNodeStr)){
				subNodeStr = subNodeStr.substring(0, subNodeStr.length()-1);
				if(!"".equals(nodeStr)){
					sqlUtil.append(" or task.subNodeCode in("+subNodeStr+"))  ");
				}else{
					sqlUtil.append(" and task.subNodeCode in("+subNodeStr+")  ");
				}
				//??????/????????????
				if(SaaPowerUtils.hasTask(userCode,"claim.claim.cancel")){
					sqlUtil.addParamValue(FlowNode.ReCanApp.name());
					sqlUtil.addParamValue(FlowNode.CancelApp.name());
					sqlUtil.addParamValue(FlowNode.CancelAppJuPei.name());
				}
				//??????/????????????
				if(SaaPowerUtils.hasTask(userCode,"claim.claim.cancel.audit")){
					sqlUtil.addParamValue(FlowNode.ReCanVrf_LV1.name());
					sqlUtil.addParamValue(FlowNode.CancelVrf_LV1.name());
					sqlUtil.addParamValue(FlowNode.CancelLVrf_LV1.name());
					sqlUtil.addParamValue(FlowNode.ReCanLVrf_LV11.name());
				}
				//??????????????????
				if(SaaPowerUtils.hasTask(userCode,"claim.recase.audit")){
					sqlUtil.addParamValue(FlowNode.ReOpenVrf_LV1.name());
					sqlUtil.addParamValue(FlowNode.ReOpenVrf_LV2.name());
				}
			}else{
				sqlUtil.append(" ) ");
			}
			//?????????????????????????????????????????????null
			if("".equals(subNodeStr) && "".equals(nodeStr)){
				return null;
			}
			
			sqlUtil.append(" and task.assignUser is null and task.assignCom like ? ");
			if(taskQueryVo.getComCode().startsWith("0002")){
				sqlUtil.addParamValue(taskQueryVo.getComCode().substring(0, 4)+"%");
			}else{
				sqlUtil.addParamValue(taskQueryVo.getComCode().substring(0, 2)+"%");
			}
		//?????????????????????
		}else {
			sqlUtil.append(" AND task.handlerStatus =? and  task.workStatus =? ");
			if("2".equals(handleStatus)){
				sqlUtil.addParamValue(HandlerStatus.DOING);
				sqlUtil.addParamValue(HandlerStatus.DOING);
				
				sqlUtil.append(" and task.handlerUser =? ");
				sqlUtil.addParamValue(userCode);
			}else{
				sqlUtil.addParamValue(HandlerStatus.INIT);
				sqlUtil.addParamValue(WorkStatus.BYBACK);
				
				sqlUtil.append(" and task.assignUser =? ");
				sqlUtil.addParamValue(userCode);
			}
			
			sqlUtil.append(" and (task.nodeCode in(?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
			sqlUtil.addParamValue(FlowNode.Sched.name());
			sqlUtil.addParamValue(FlowNode.Check.name());
			sqlUtil.addParamValue(FlowNode.ChkRe.name());
			sqlUtil.addParamValue(FlowNode.DLoss.name());
			sqlUtil.addParamValue(FlowNode.VLoss.name());
			sqlUtil.addParamValue(FlowNode.VPrice.name());
			sqlUtil.addParamValue(FlowNode.VClaim.name());
			sqlUtil.addParamValue(FlowNode.PrePay.name());
			sqlUtil.addParamValue(FlowNode.RecPay.name());
			sqlUtil.addParamValue(FlowNode.PadPay.name());
			sqlUtil.addParamValue(FlowNode.Certi.name());
			sqlUtil.addParamValue(FlowNode.ChkBig.name());
			sqlUtil.addParamValue(FlowNode.RecLoss.name());
			sqlUtil.addParamValue(FlowNode.Compe.name());
			
			sqlUtil.append(" or task.subNodeCode in(?,?,?,?,?,?,?,?,?)) ");
			sqlUtil.addParamValue(FlowNode.ReCanApp.name());
			sqlUtil.addParamValue(FlowNode.CancelApp.name());
			sqlUtil.addParamValue(FlowNode.CancelAppJuPei.name());
			sqlUtil.addParamValue(FlowNode.ReCanVrf_LV1.name());
			sqlUtil.addParamValue(FlowNode.CancelVrf_LV1.name());
			sqlUtil.addParamValue(FlowNode.CancelLVrf_LV1.name());
			sqlUtil.addParamValue(FlowNode.ReCanLVrf_LV11.name());
			sqlUtil.addParamValue(FlowNode.ReOpenVrf_LV1.name());
			sqlUtil.addParamValue(FlowNode.ReOpenVrf_LV2.name());
		}
		sqlUtil.append(" order by task.taskInTime desc ");
		
		int start = taskQueryVo.getStart();
		int length = taskQueryVo.getLength();
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		List<WfTaskQueryResultVo> resultVoList=new ArrayList<WfTaskQueryResultVo>();
		for(int i=0;i<page.getResult().size();i++){
			WfTaskQueryResultVo resultVo = new WfTaskQueryResultVo();
			Object[] obj = page.getResult().get(i);
			PrpLWfTaskQuery wfTaskQuery = (PrpLWfTaskQuery)obj[1];
			PrpLWfTaskIn wfTaskIn = (PrpLWfTaskIn)obj[0];
			Beans.copy().from(wfTaskQuery).to(resultVo);
			resultVo.setComCode(wfTaskIn.getComCode());
			resultVo.setSubNodeCode(wfTaskIn.getSubNodeCode());
			resultVo.setNodeCode(wfTaskIn.getNodeCode());
			resultVo.setItemName(wfTaskIn.getItemName());
			resultVo.setInsuredName(wfTaskQuery.getInsuredName());
			resultVo.setShowInfoXML(wfTaskIn.getShowInfoXML());
			resultVo.setHandlerIdKey(wfTaskIn.getHandlerIdKey());
			resultVo.setHandlerStatus(wfTaskIn.getHandlerStatus());
			resultVo.setTaskId(wfTaskIn.getTaskId());
			//?????????
			if(FlowNode.DLoss.name().equals(wfTaskIn.getNodeCode()) || FlowNode.VLoss.name().equals(wfTaskIn.getNodeCode())
					|| FlowNode.VPrice.name().equals(wfTaskIn.getNodeCode())){
				resultVo.setDeflossCarType(wfTaskIn.getItemName());
			}else{
				resultVo.setDeflossCarType(wfTaskQuery.getLicenseNo());
			}
			resultVo.setTaskInTime(wfTaskIn.getTaskInTime());
			resultVo.setTaskInUser(wfTaskIn.getTaskInUser());
			resultVo.setTaskName(FlowNode.valueOf(wfTaskIn.getSubNodeCode()).getName());
			resultVo.setBussTag(wfTaskIn.getBussTag());
			resultVo.setClaimNo(wfTaskIn.getClaimNo());
			resultVo.setWorkStatus(wfTaskIn.getWorkStatus());
			resultVo.setRegistNo(wfTaskIn.getRegistNo());
			//??????????????????
			if(isTimeOut(wfTaskIn.getRegistNo(), wfTaskIn.getSubNodeCode(), handleStatus)){
				TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
				PrpLWfTaskVo wfTaskVo = new PrpLWfTaskVo();
				resultVo.setTimeOut("1");
				extMapUtil.addTagMap(resultVo,"timeOut");
				wfTaskVo.setBussTagMap(extMapUtil.getBussTagMap());
				String bussTag = TaskExtMapUtils.joinBussTag(resultVo.getBussTag(),wfTaskVo.getBussTagMap());
				resultVo.setBussTag(bussTag);
			}
			
			resultVoList.add(resultVo);
			
		}
		
		ResultPage<WfTaskQueryResultVo> resultPage = new ResultPage<WfTaskQueryResultVo>(start,length,page.getTotalCount(),resultVoList);
		
		return resultPage;
	}
	
	@Override
	public int countTimeOut(PrpLWfTaskQueryVo taskQueryVo){

		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		String userCode = taskQueryVo.getUserCode();
		String handleStatus = taskQueryVo.getHandleStatus();
		sqlUtil.append(" FROM PrpLWfTaskIn task, PrpLWfTaskQuery qry");
		sqlUtil.append(" WHERE task.flowId= qry.flowId");
		
		//?????????
		if("0".equals(handleStatus)){
			sqlUtil.append(" AND task.handlerStatus =? and  task.workStatus =? ");
			sqlUtil.addParamValue(HandlerStatus.INIT);
			sqlUtil.addParamValue(HandlerStatus.INIT);
			
			sqlUtil.append(" and task.nodeCode in(?,?,?,?,?,?,?) ");
			sqlUtil.addParamValue(FlowNode.Sched.name());
			sqlUtil.addParamValue(FlowNode.Check.name());
			sqlUtil.addParamValue(FlowNode.ChkRe.name());
			sqlUtil.addParamValue(FlowNode.DLoss.name());
			sqlUtil.addParamValue(FlowNode.VLoss.name());
			sqlUtil.addParamValue(FlowNode.VPrice.name());
			sqlUtil.addParamValue(FlowNode.VClaim.name());
			//???????????????
			sqlUtil.append(" and task.assignUser =? ");
			sqlUtil.addParamValue(userCode);
		//?????????
		}else if("1".equals(handleStatus)){
			sqlUtil.append(" AND task.handlerStatus =? and  task.workStatus =? ");
			sqlUtil.addParamValue(HandlerStatus.INIT);
			sqlUtil.addParamValue(HandlerStatus.INIT);
			
			String nodeStr = "";
			String subNodeStr = "";
			//??????/????????????
			if(SaaPowerUtils.hasTask(userCode,"claim.claim.cancel")){
				subNodeStr = subNodeStr+"?,?,?,";
			}
			//??????/????????????
			if(SaaPowerUtils.hasTask(userCode,"claim.claim.cancel.audit")){
				subNodeStr = subNodeStr+"?,?,?,?,";
			}
			//??????????????????
			if(SaaPowerUtils.hasTask(userCode,"claim.recase.audit")){
				subNodeStr = subNodeStr+"?,?,";
			}
			//????????????
			if(SaaPowerUtils.hasTask(userCode,"claim.bigcase.auditing")){
				nodeStr = nodeStr+"?,";
			}
			//?????????????????????
			if(SaaPowerUtils.hasTask(userCode,"claim.verifycarloss") ||
					SaaPowerUtils.hasTask(userCode,"claim.verifyloss")){
				nodeStr = nodeStr+"?,";
			}
			//????????????
			if(SaaPowerUtils.hasTask(userCode,"claim.reclaim")){
				nodeStr = nodeStr+"?,";
			}
			//?????????????????????
			if(SaaPowerUtils.hasTask(userCode,"claim.verifyprice")){
				nodeStr = nodeStr+"?,";
			}
			//????????????
			if(SaaPowerUtils.hasTask(userCode,"claim.certify")){
				nodeStr = nodeStr+"?,";
			}
			//??????
			if(SaaPowerUtils.hasTask(userCode,"claim.compensate")){
				nodeStr = nodeStr+"?,";
			}
			//?????????????????????
			if(SaaPowerUtils.hasTask(userCode,"claim.undwrt")){
				nodeStr = nodeStr+"?,";
			}
			//??????
			if(SaaPowerUtils.hasTask(userCode,"claim.prepay")){
				nodeStr = nodeStr+"?,";
			}
			//??????
			if(SaaPowerUtils.hasTask(userCode,"claim.payment")){
				nodeStr = nodeStr+"?,";
			}
			//??????
			if(SaaPowerUtils.hasTask(userCode,"claim.replevy")){
				nodeStr = nodeStr+"?,";
			}
			
			if(!"".equals(nodeStr)){
				nodeStr = nodeStr.substring(0, nodeStr.length()-1);
				sqlUtil.append(" and (task.nodeCode in("+nodeStr+") ");
				//????????????
				if(SaaPowerUtils.hasTask(userCode,"claim.bigcase.auditing")){
					sqlUtil.addParamValue(FlowNode.ChkBig.name());
				}
				//?????????????????????
				if(SaaPowerUtils.hasTask(userCode,"claim.verifycarloss") ||
						SaaPowerUtils.hasTask(userCode,"claim.verifyloss")){
					sqlUtil.addParamValue(FlowNode.VLoss.name());
				}
				//????????????
				if(SaaPowerUtils.hasTask(userCode,"claim.reclaim")){
					sqlUtil.addParamValue(FlowNode.RecLoss.name());
				}
				//?????????????????????
				if(SaaPowerUtils.hasTask(userCode,"claim.verifyprice")){
					sqlUtil.addParamValue(FlowNode.VPrice.name());
				}
				//????????????
				if(SaaPowerUtils.hasTask(userCode,"claim.certify")){
					sqlUtil.addParamValue(FlowNode.Certi.name());
				}
				//??????
				if(SaaPowerUtils.hasTask(userCode,"claim.compensate")){
					sqlUtil.addParamValue(FlowNode.Compe.name());
				}
				//?????????????????????
				if(SaaPowerUtils.hasTask(userCode,"claim.undwrt")){
					sqlUtil.addParamValue(FlowNode.VClaim.name());
				}
				//??????
				if(SaaPowerUtils.hasTask(userCode,"claim.prepay")){
					sqlUtil.addParamValue(FlowNode.PrePay.name());
				}
				//??????
				if(SaaPowerUtils.hasTask(userCode,"claim.payment")){
					sqlUtil.addParamValue(FlowNode.PadPay.name());
				}
				//??????
				if(SaaPowerUtils.hasTask(userCode,"claim.replevy")){
					sqlUtil.addParamValue(FlowNode.RecPay.name());
				}
			}
			
			if(!"".equals(subNodeStr)){
				subNodeStr = subNodeStr.substring(0, subNodeStr.length()-1);
				if(!"".equals(nodeStr)){
					sqlUtil.append(" or task.subNodeCode in("+subNodeStr+"))  ");
				}else{
					sqlUtil.append(" and task.subNodeCode in("+subNodeStr+")  ");
				}
				//??????/????????????
				if(SaaPowerUtils.hasTask(userCode,"claim.claim.cancel")){
					sqlUtil.addParamValue(FlowNode.ReCanApp.name());
					sqlUtil.addParamValue(FlowNode.CancelApp.name());
					sqlUtil.addParamValue(FlowNode.CancelAppJuPei.name());
				}
				//??????/????????????
				if(SaaPowerUtils.hasTask(userCode,"claim.claim.cancel.audit")){
					sqlUtil.addParamValue(FlowNode.ReCanVrf_LV1.name());
					sqlUtil.addParamValue(FlowNode.CancelVrf_LV1.name());
					sqlUtil.addParamValue(FlowNode.CancelLVrf_LV1.name());
					sqlUtil.addParamValue(FlowNode.ReCanLVrf_LV11.name());
				}
				//??????????????????
				if(SaaPowerUtils.hasTask(userCode,"claim.recase.audit")){
					sqlUtil.addParamValue(FlowNode.ReOpenVrf_LV1.name());
					sqlUtil.addParamValue(FlowNode.ReOpenVrf_LV2.name());
				}
			}else{
				sqlUtil.append(" ) ");
			}
			//?????????????????????????????????????????????null
			if("".equals(subNodeStr) && "".equals(nodeStr)){
				return 0;
			}
			
			sqlUtil.append(" and task.assignUser is null and task.assignCom like ? ");
			if(taskQueryVo.getComCode().startsWith("0002")){
				sqlUtil.addParamValue(taskQueryVo.getComCode().substring(0, 4)+"%");
			}else{
				sqlUtil.addParamValue(taskQueryVo.getComCode().substring(0, 2)+"%");
			}
		//?????????????????????
		}else {
			sqlUtil.append(" AND task.handlerStatus =? and  task.workStatus =? ");
			if("2".equals(handleStatus)){
				sqlUtil.addParamValue(HandlerStatus.DOING);
				sqlUtil.addParamValue(HandlerStatus.DOING);
				
				sqlUtil.append(" and task.handlerUser =? ");
				sqlUtil.addParamValue(userCode);
			}else{
				sqlUtil.addParamValue(HandlerStatus.INIT);
				sqlUtil.addParamValue(WorkStatus.BYBACK);
				
				sqlUtil.append(" and task.assignUser =? ");
				sqlUtil.addParamValue(userCode);
			}
			
			sqlUtil.append(" and (task.nodeCode in(?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
			sqlUtil.addParamValue(FlowNode.Sched.name());
			sqlUtil.addParamValue(FlowNode.Check.name());
			sqlUtil.addParamValue(FlowNode.ChkRe.name());
			sqlUtil.addParamValue(FlowNode.DLoss.name());
			sqlUtil.addParamValue(FlowNode.VLoss.name());
			sqlUtil.addParamValue(FlowNode.VPrice.name());
			sqlUtil.addParamValue(FlowNode.VClaim.name());
			sqlUtil.addParamValue(FlowNode.PrePay.name());
			sqlUtil.addParamValue(FlowNode.RecPay.name());
			sqlUtil.addParamValue(FlowNode.PadPay.name());
			sqlUtil.addParamValue(FlowNode.Certi.name());
			sqlUtil.addParamValue(FlowNode.ChkBig.name());
			sqlUtil.addParamValue(FlowNode.RecLoss.name());
			sqlUtil.addParamValue(FlowNode.Compe.name());
			
			sqlUtil.append(" or task.subNodeCode in(?,?,?,?,?,?,?,?,?)) ");
			sqlUtil.addParamValue(FlowNode.ReCanApp.name());
			sqlUtil.addParamValue(FlowNode.CancelApp.name());
			sqlUtil.addParamValue(FlowNode.CancelAppJuPei.name());
			sqlUtil.addParamValue(FlowNode.ReCanVrf_LV1.name());
			sqlUtil.addParamValue(FlowNode.CancelVrf_LV1.name());
			sqlUtil.addParamValue(FlowNode.CancelLVrf_LV1.name());
			sqlUtil.addParamValue(FlowNode.ReCanLVrf_LV11.name());
			sqlUtil.addParamValue(FlowNode.ReOpenVrf_LV1.name());
			sqlUtil.addParamValue(FlowNode.ReOpenVrf_LV2.name());
		}
		sqlUtil.append(" order by task.taskInTime desc ");
		
		int start = taskQueryVo.getStart();
		int length = taskQueryVo.getLength();
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		
		List<Object[]> list = databaseDao.findAllByHql(sql, values);
		int countTimeOut = 0;
		if(list!=null){
			for(int i = 0; i<list.size(); i++ ){
				Object[] obj = list.get(i);
				PrpLWfTaskIn prpLWfTaskin = (PrpLWfTaskIn)obj[0];
				if(isTimeOut(prpLWfTaskin.getRegistNo(), prpLWfTaskin.getSubNodeCode(), prpLWfTaskin.getHandlerStatus())){
					countTimeOut = countTimeOut+1;
				}
			}
		}
		
		return countTimeOut;
	}
	
	/**
	 * ?????????????????????????????????????????????????????????????????????
	 *  a.?????????????????????????????????24?????????
		b.?????????????????????????????????12*24?????????
		c.?????????????????????????????????????????????3*24????????? 
		????????????????????????????????????????????????????????????????????????????????????????????????????????????
	 * @param registNo
	 * @param subNodeCode
	 * @return
	 */
	public Boolean isTimeOut(String registNo,String subNodeCode,String handleStatus){
		
		Date date = new Date();
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		if(registVo != null && ("0".equals(handleStatus)||"1".equals(handleStatus))){
			if(FlowNode.Chk.name().equals(subNodeCode)){
				if(DateUtils.compareDays(date, registVo.getReportTime())>1){
					return true;
				}
			}else if(FlowNode.DLCar.name().equals(subNodeCode)||FlowNode.DLProp.name().equals(subNodeCode)){
				if(DateUtils.compareDays(date, registVo.getReportTime())>12){
					return true;
				}
			}else if(FlowNode.PLFirst.name().equals(subNodeCode)){
				if(DateUtils.compareDays(date, registVo.getReportTime())>3){
					return true;
				}
			}
			return false;
		}else{
			return false;
		}
	}

	/* 
	 * @see ins.sino.claimcar.flow.service.WfTaskQueryService#findTaskForPage(ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo)
	 * @param taskQueryVo
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResultPage<WfTaskQueryResultVo> findTaskForPage(PrpLWfTaskQueryVo taskQueryVo) throws Exception {


		String nodeCode=taskQueryVo.getNodeCode();
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		// ????????????????????????
		String handleStatus = taskQueryVo.getHandleStatus();
		// ??????????????????????????????
		if(HandlerStatus.END.equals(handleStatus)||HandlerStatus.CANCEL.equals(handleStatus)){
			sqlUtil.append(" FROM PrpLWfTaskOut task");
		} else{sqlUtil.append(" FROM PrpLWfTaskIn task");
		}
		sqlUtil.append(" , PrpLWfTaskQuery qry");
		sqlUtil.append(" WHERE task.flowId= qry.flowId");
		//??????????????????????????????
		/*if(FlowNode.VClaim.equals(nodeCode) && StringUtils.isBlank(taskQueryVo.getCompensateAmount())){
	       sqlUtil.append(" AND (task.compensateNo= comp.compensateNo  or task.compensateNo like 'D%' or task.taskInNode='CancelAppJuPei')");
		}else if(FlowNode.VClaim.equals(nodeCode)&&StringUtils.isNotBlank(taskQueryVo.getCompensateAmount())){
					if("1".equals(taskQueryVo.getCompensateAmount())){
						sqlUtil.append(" AND ((task.compensateNo= comp.compensateNo AND (nvl(abs(comp.sumAmt),0)+nvl(abs(comp.sumFee),0))>= ? and (nvl(abs(comp.sumAmt),0)+nvl(abs(comp.sumFee),0)) < ?) or task.compensateNo like 'D%' or task.taskInNode='CancelAppJuPei')  ");
						sqlUtil.addParamValue(new BigDecimal(0));
						sqlUtil.addParamValue(new BigDecimal(5000));
					}else if("2".equals(taskQueryVo.getCompensateAmount())){
						sqlUtil.append(" AND ((task.compensateNo= comp.compensateNo AND (nvl(abs(comp.sumAmt),0)+nvl(abs(comp.sumFee),0))>= ? and (nvl(abs(comp.sumAmt),0)+nvl(abs(comp.sumFee),0)) < ?) or task.compensateNo like 'D%' or task.taskInNode='CancelAppJuPei')");				
						sqlUtil.addParamValue(new BigDecimal(5000));
						sqlUtil.addParamValue(new BigDecimal(10000));
					}else if("3".equals(taskQueryVo.getCompensateAmount())){
						sqlUtil.append(" AND ((task.compensateNo= comp.compensateNo AND (nvl(abs(comp.sumAmt),0)+nvl(abs(comp.sumFee),0))>= ?) or task.compensateNo like 'D%' or task.taskInNode='CancelAppJuPei')");
						sqlUtil.addParamValue(new BigDecimal(10000));
					}else{
						
					}
						
				}*/
		
		// ????????????????????????1,2??????????????????
		if (HandlerStatus.INIT.equals(handleStatus)) {// ?????????????????????????????????
			//??????????????????
			if(FlowNode.VClaim.equals(nodeCode)){
				sqlUtil.append(" AND task.handlerStatus =? and  task.workStatus in (?,?)");
				sqlUtil.addParamValue(HandlerStatus.INIT);
				sqlUtil.addParamValue(HandlerStatus.INIT);
				sqlUtil.addParamValue(WorkStatus.BYBACK);
			}else{
				sqlUtil.append(" AND task.handlerStatus =? and  task.workStatus!=?");
				sqlUtil.addParamValue(HandlerStatus.INIT);
				sqlUtil.addParamValue(WorkStatus.BYBACK);
			}
		
		}else if(HandlerStatus.DOING.equals(handleStatus)){
			sqlUtil.append(" AND task.handlerStatus in(?,?)");
			sqlUtil.addParamValue(HandlerStatus.START);
			sqlUtil.addParamValue(handleStatus);
			if(FlowNode.Claim.equals(nodeCode) && taskQueryVo.getClaimNo() != null && !"".equals(taskQueryVo.getClaimNo())){
				sqlUtil.append(" AND task.handlerIdKey = ?");
				sqlUtil.addParamValue(taskQueryVo.getClaimNo());
			}
		}else if(WorkStatus.BYBACK.equals(handleStatus)){// ????????????????????????
			sqlUtil.append(" AND task.handlerStatus =? and  task.workStatus=?");
			sqlUtil.addParamValue(HandlerStatus.INIT);
			sqlUtil.addParamValue(WorkStatus.BYBACK);
		}else{
			sqlUtil.append(" AND task.handlerStatus = ? and task.workStatus !=?");
			sqlUtil.addParamValue(handleStatus);
			sqlUtil.addParamValue(WorkStatus.TURN);
//			if(FlowNode.Claim.equals(nodeCode) && taskQueryVo.getClaimNo() != null && !"".equals(taskQueryVo.getClaimNo().trim())){
//				sqlUtil.append(" AND task.handlerIdKey = ?");
//				sqlUtil.addParamValue(taskQueryVo.getClaimNo().trim());
//				System.out.println(taskQueryVo.getClaimNo().trim());
//			}
		}

		SaaUserPowerVo userPowerVo = saaUserPowerService.findUserPower(ServiceUserUtils.getUserCode());

		if("1".equals(taskQueryVo.getIncludeLower())&&StringUtils.isNotBlank(taskQueryVo.getSubNodeCode())){// ??????????????????
			String userMaxLevel = userPowerVo.getMaxVerifyLVMap().get(taskQueryVo.getSubNodeCode()); // ??????????????????????????????????????????????????????
			if(userMaxLevel!=null){
				sqlUtil.append("  AND task.subNodeCode <= ?  and LENGTH( task.subNodeCode) <= ?");
				sqlUtil.addParamValue(userMaxLevel);
				sqlUtil.addParamValue(userMaxLevel.length());// TODO ???????????????????????????LV10,11,12?????????, ??????????????????1x?????????????????????
			}
		}
//		else if(FlowNode.ChkBig.equals(nodeCode)||FlowNode.PLBig.equals(nodeCode)){
//			Map<String,List<String>> allMap = userPowerVo.getAllVerifyLVMap();
//			List<String> list = allMap.get(nodeCode);
//			if(list!=null&&list.size()>0){
//				String[] str = new String[list.size()];
//				for(int i = 0; i < list.size(); i++ ){
//					str[i] = list.get(i);
//				}
//				String maskStr = SqlJoinUtils.arrayToMask(str);
//				sqlUtil.append("  AND task.subNodeCode in ("+maskStr+") ");
//				for(String s : str){
//					sqlUtil.addParamValue(s);
//				}
//			}
//		}
		
		if("0".equals(taskQueryVo.getQueryRange()) || taskQueryVo.getQueryRange()==null || HandlerStatus.DOING.equals(handleStatus) ){// 0-????????????????????????
			if(FlowNode.EndCas.equals(nodeCode)){//??????
				sqlUtil.append("  AND task.comCode like  ?");
				sqlUtil.addParamValue(taskQueryVo.getAssignCode().substring(0, 2)+"%");
			}else if(FlowNode.VPrice.equals(nodeCode)||FlowNode.VLCar.equals(taskQueryVo.getSubNodeCode())||FlowNode.VLProp.equals(taskQueryVo.getSubNodeCode())||FlowNode.PLVerify.equals(taskQueryVo.getSubNodeCode())
					||FlowNode.PLCharge.equals(taskQueryVo.getSubNodeCode())||FlowNode.VClaim.equals(nodeCode)){//???????????????????????????????????????????????????????????????????????????????????????
				
				List<SaaUserPermitCompany> saaUserPermitCompanyList= findSaaUserPermitCompanyByUserCode(taskQueryVo.getUserCode());
				if(saaUserPermitCompanyList ==null || saaUserPermitCompanyList.isEmpty()){//???????????????
					if(HandlerStatus.DOING.equals(handleStatus)){
						sqlUtil.append("  AND task.handlerUser = ?");
						sqlUtil.addParamValue(taskQueryVo.getUserCode());
					}else if(HandlerStatus.END.equals(handleStatus) && !FlowNode.Claim.equals(nodeCode)){
						sqlUtil.append("  AND task.taskOutUser = ?");
						sqlUtil.addParamValue(taskQueryVo.getUserCode());
					}else if(HandlerStatus.END.equals(handleStatus) && FlowNode.Claim.equals(nodeCode)){
						sqlUtil.append("  AND task.taskOutUser in (?,?)");
						sqlUtil.addParamValue("AutoClaim");
						sqlUtil.addParamValue("ForceClaim");
					}else{
						sqlUtil.append("  AND task.assignUser = ?");
						sqlUtil.addParamValue(taskQueryVo.getUserCode());
					}
				}else{//?????????
					if(FlowNode.RecLoss.equals(nodeCode)||FlowNode.Certi.equals(nodeCode)||FlowNode.Compe.equals(nodeCode)||
							FlowNode.Compe.equals(nodeCode)||FlowNode.PrePay.equals(nodeCode)||FlowNode.PadPay.equals(nodeCode)){
						
					} else {
						//?????????????????????????????????????????? ???????????????????????????????????????00010000????????????????????????????????????????????????
						List<SaaUserPermitCompany> saaUserPermitCompanyList1= findSaaUserPermitCompanyByUserCode(taskQueryVo.getUserCode());
						if(saaUserPermitCompanyList1 ==null || saaUserPermitCompanyList1.isEmpty()){
							sqlUtil.append("  AND task.assignCom != ?");
							sqlUtil.addParamValue(CodeConstants.TOPCOM);
						}else{
							
							sqlUtil.append("  AND task.assignCom like ?");
							sqlUtil.addParamValue("0001%");
							if(FlowNode.VClaim.equals(nodeCode)){
								List<String> allVerifyLVList =  new ArrayList<String>();
								allVerifyLVList = this.findAllVerifyLVMapByUserCode(taskQueryVo.getUserCode());
								if(allVerifyLVList!=null && allVerifyLVList.size() > 0 ){
									sqlUtil.append("  and task.subNodeCode in (");
									if(allVerifyLVList.size()>1){
										for(int i =0; i < allVerifyLVList.size()-1;i++){
											sqlUtil.append("?,");
										}
									}
									sqlUtil.append("?)");
									for(String verifyLV : allVerifyLVList){
									sqlUtil.addParamValue(verifyLV);}
								}
							}else{
								String userMaxLevel = userPowerVo.getMaxVerifyLVMap().get(taskQueryVo.getSubNodeCode()); // ??????????????????????????????????????????????????????
								if(userMaxLevel!=null){
									sqlUtil.append("  AND task.subNodeCode <= ?  and LENGTH( task.subNodeCode) <= ?");
									sqlUtil.addParamValue(userMaxLevel);
									sqlUtil.addParamValue(userMaxLevel.length());// TODO ???????????????????????????LV10,11,12?????????, ??????????????????1x?????????????????????
								}
							}
							
						}
					}
				}
			}else if(FlowNode.Survey.equals(nodeCode)){
				sqlUtil.andTopComSql("task","comCode",taskQueryVo.getComCode());//???????????????2???????????????????????????????????????
			}else{
			
				if(HandlerStatus.DOING.equals(handleStatus) && !FlowNode.Regis.equals(nodeCode)){
					//????????????????????????????????????????????????????????????????????????????????????
					sqlUtil.append("  AND task.handlerUser = ?");
					sqlUtil.addParamValue(taskQueryVo.getUserCode());
				}else if(HandlerStatus.END.equals(handleStatus) && !FlowNode.Claim.equals(nodeCode)){
					sqlUtil.append("  AND task.taskOutUser = ?");
					sqlUtil.addParamValue(taskQueryVo.getUserCode());
				}else if(HandlerStatus.END.equals(handleStatus) && FlowNode.Claim.equals(nodeCode)){
					sqlUtil.append("  AND task.taskOutUser in (?,?)");
					sqlUtil.addParamValue("AutoClaim");
					sqlUtil.addParamValue("ForceClaim");
				}else{
					if(!FlowNode.Regis.equals(nodeCode)){//????????????????????????????????????????????????????????????????????????????????????
						sqlUtil.append("  AND task.assignUser = ?");
						sqlUtil.addParamValue(taskQueryVo.getUserCode());
					}
				}
			}
			
		} else {
	
			if(FlowNode.RecLoss.equals(nodeCode)||FlowNode.Certi.equals(nodeCode)||FlowNode.Compe.equals(nodeCode)||
					FlowNode.Compe.equals(nodeCode)||FlowNode.PrePay.equals(nodeCode)||FlowNode.PadPay.equals(nodeCode)){
				if(HandlerStatus.END.equals(handleStatus)){
					sqlUtil.append("  AND task.taskOutUser = ?");
					sqlUtil.addParamValue(taskQueryVo.getUserCode());
				}
			} else {
				//?????????????????????????????????????????? ???????????????????????????????????????00010000????????????????????????????????????????????????
				List<SaaUserPermitCompany> saaUserPermitCompanyList= findSaaUserPermitCompanyByUserCode(taskQueryVo.getUserCode());
				if(saaUserPermitCompanyList ==null || saaUserPermitCompanyList.isEmpty()){
					sqlUtil.append("  AND task.assignCom != ?");
					sqlUtil.addParamValue(CodeConstants.TOPCOM);
				}else{
					
					sqlUtil.append("  AND task.assignCom like ?");
					sqlUtil.addParamValue("0001%");
					String userMaxLevel = userPowerVo.getMaxVerifyLVMap().get(taskQueryVo.getSubNodeCode()); // ??????????????????????????????????????????????????????
					if(userMaxLevel!=null){
						sqlUtil.append("  AND task.subNodeCode <= ?  and LENGTH( task.subNodeCode) <= ?");
						sqlUtil.addParamValue(userMaxLevel);
						sqlUtil.addParamValue(userMaxLevel.length());// TODO ???????????????????????????LV10,11,12?????????, ??????????????????1x?????????????????????
					}
				}
				/*Map<String,List<String>> allMap = userPowerVo.getAllVerifyLVMap();
				List<String> list = allMap.get(nodeCode);
				if(list!=null&&list.size()>0){
					String[] str = new String[list.size()];
					for(int i = 0; i < list.size(); i++ ){
						str[i] = list.get(i);
					}
					String maskStr = SqlJoinUtils.arrayToMask(str);
					sqlUtil.append("  AND task.subNodeCode in ("+maskStr+") ");
					for(String s : str){
						sqlUtil.addParamValue(s);
					}
				}*/
			}
			
		}
		/*else{
			sqlUtil.append("  AND task.assignCom like  ?");
			if("0002".equals(taskQueryVo.getAssignCode())){
				sqlUtil.addParamValue(taskQueryVo.getAssignCode()+"%");
			} else {
				sqlUtil.addParamValue(taskQueryVo.getAssignCode().substring(0, 2)+"%");
			}
			
		}*/
		/*else{
			sqlUtil.append("  AND task.taskInUser IN (?)");
			sqlUtil.addParamValue(taskQueryVo.getUserCode());
		}*/
		/*if("0".equals(taskQueryVo.getQueryRange())){// 0-????????????????????????
			if(HandlerStatus.DOING.equals(handleStatus)){
				sqlUtil.append("  AND task.handlerUser = ?");
			}else if(HandlerStatus.END.equals(handleStatus)){
				sqlUtil.append("  AND task.taskOutUser = ?");
			}else{
				sqlUtil.append("  AND task.assignUser = ?");
			}
			sqlUtil.addParamValue(taskQueryVo.getUserCode());
		}*/
		String  table_query="qry";
		String  table_task="task";

		sqlUtil.andEquals(taskQueryVo,table_task,"nodeCode","taskInNode");
		sqlUtil.andComSql(table_query,"comCodePly",taskQueryVo.getComCode());

		// ????????????
		if(FlowNode.CancelApp.equals(taskQueryVo.getSubNodeCode()) || FlowNode.CancelAppJuPei.equals(taskQueryVo.getSubNodeCode())
				||FlowNode.ReCanApp.equals(taskQueryVo.getSubNodeCode())){
			sqlUtil.append(" AND exists  (select  1 from PrpLClaim prpLClaim where prpLClaim.registNo=qry.registNo ");

		/*	sqlUtil.append(" and (prpLClaim.claimTime >= ? and prpLClaim.claimTime< ?) ");
			sqlUtil.addParamValue(taskQueryVo.getTaskInTimeStart());
			sqlUtil.addParamValue(taskQueryVo.getTaskInTimeEnd());*/
			
			
			Calendar c = Calendar.getInstance();  
	        c.setTime(taskQueryVo.getTaskInTimeEnd());   //??????????????????  
	        c.add(Calendar.DATE, 1); //?????????1???  
	        Date taskInTimeEnd = new Date();
	        taskInTimeEnd = c.getTime(); 
			sqlUtil.append(" and (prpLClaim.claimTime >= ? and prpLClaim.claimTime<?) ");
			sqlUtil.addParamValue(taskQueryVo.getTaskInTimeStart());
			sqlUtil.addParamValue(taskInTimeEnd);
			
			sqlUtil.append(")");
		}else{
			sqlUtil.andDate(taskQueryVo,table_task,"taskInTime");
		}
		//sqlUtil.andDate(taskQueryVo,table_task,"taskInTime");
		sqlUtil.andDate(taskQueryVo,table_query,"reportTime","damageTime","appointmentTime");

		// ????????????????????????
		sqlUtil.andEquals(taskQueryVo,table_query,"mercyFlag","customerLevel","reporterPhone","tempRegistFlag","isOnSitReport","reconcileFlag");
		// riskCode????????????
		sqlUtil.andEquals(taskQueryVo,table_query,"riskCode");
		logger.debug("=getMercyFlaggetMercyFlag==="+taskQueryVo.getMercyFlag());
		
		// ???????????????like?????? ????????????lossPersonInfo,// ?????????????????????????????????????????????????????????
		if(StringUtils.isNotBlank(taskQueryVo.getPersonName())){
			sqlUtil.append(" AND qry.lossPersonInfo like ?");
			sqlUtil.addParamValue("%"+taskQueryVo.getPersonName()+"%");
		}
		if(StringUtils.isNotBlank(taskQueryVo.getHospitalName())){
			sqlUtil.append(" AND qry.lossPersonInfo like ?");
			sqlUtil.addParamValue("%"+taskQueryVo.getHospitalName()+"%");
		}
		
//		sqlUtil.andLike2(taskQueryVo,table_query,"lossPersonInfo","personName","hospitalName");

		// ??????in??????
		//sqlUtil.addIn(table_task,"subNodeCode",taskQueryVo.getSubNodes());

		if(FlowNode.Check.equals(nodeCode)){// ?????????
			// sqlUtil.andEquals(taskQueryVo, table_task, "subNodeCode");// ???????????????????????????like
			if(StringUtils.isNotBlank(taskQueryVo.getSubNodeCode())){
				sqlUtil.andEquals(taskQueryVo,table_task,"subNodeCode");
			}
		}else if(FlowNode.DLCar.equals(taskQueryVo.getSubNodeCode())){
			sqlUtil.andEquals(taskQueryVo, table_task, "subNodeCode");//???????????????????????????
		}else if(FlowNode.DLCars.equals(taskQueryVo.getSubNodeCode())){//???????????????????????????
			taskQueryVo.setSubNodeCode("DLCar");
			sqlUtil.andLike(taskQueryVo, table_task, "subNodeCode");
		}else if(!FlowNode.ChkBig.equals(nodeCode)&&!FlowNode.PLBig.equals(nodeCode)){
			sqlUtil.andLike(taskQueryVo,table_task,"subNodeCode");// ??????????????????????????????????????????like
		}else{
			
		}
		//??????????????????????????????
		if(FlowNode.Compe.equals(nodeCode)&&WorkStatus.BYBACK.equals(handleStatus)){
			sqlUtil.append(" AND task.assignUser = ?");
			sqlUtil.addParamValue(taskQueryVo.getUserCode());
		}
		
		if(FlowNode.VClaim.equals(nodeCode)&&StringUtils.isNotBlank(taskQueryVo.getYwTaskType())){
			sqlUtil.andEquals(taskQueryVo, table_task, "ywTaskType");//????????????????????????
		}
		//??????????????????
        if(FlowNode.VClaim.equals(nodeCode) && StringUtils.isNotBlank(taskQueryVo.getAutoType())){
        	if("0".equals(taskQueryVo.getAutoType())){
        		sqlUtil.append(" AND task.assignUser != ?");
    			sqlUtil.addParamValue("AutoVClaim");
        	}else if("1".equals(taskQueryVo.getAutoType())){
        		sqlUtil.append(" AND task.assignUser = ?");
    			sqlUtil.addParamValue("AutoVClaim");
        	}
        	
        }
		logger.debug("===taskInUser========taskInUser"+taskQueryVo.getTaskInUser());
		sqlUtil.andLike(taskQueryVo,table_task,"taskInUser");
		
//		sqlUtil.andEquals(taskQueryVo,table_task,"claimNo","compensateNo");

		if(FlowNode.Sched.equals(nodeCode)){
			String keyProperty=taskQueryVo.getKeyProperty();
			if(keyProperty!=null){
				if("insuredName".equals(keyProperty)){
					sqlUtil.andLike2(taskQueryVo,table_query,keyProperty);
				}else{
					sqlUtil.andReverse(taskQueryVo,table_query,7,keyProperty);
				}
			}
		}else{
			// ?????????????????????????????????
			sqlUtil.andReverse(taskQueryVo,table_query,7,"registNo");
			sqlUtil.andReverse(taskQueryVo,table_query,7,"frameNo");
			sqlUtil.andLike2(taskQueryVo,table_query,"insuredName","licenseNo");
			logger.debug("??????===="+taskQueryVo.getClaimNo());
			// sqlUtil.andReverse(taskQueryVo,table_task,7,"claimNo");
			sqlUtil.andLike2(taskQueryVo,table_task,"claimNo","compensateNo");
		}
		
		//????????????/???????????????????????????
		if(FlowNode.PrePay.equals(nodeCode)||FlowNode.PrePayWf.equals(nodeCode)){
			sqlUtil.andLike2(taskQueryVo,table_task,"claimNo","compensateNo");
		}
		
		if("1".equals(taskQueryVo.getPrePayType())){//????????????
			sqlUtil.append(" and task.riskCode = ? ");
			sqlUtil.addParamValue(Risk.DQZ);
		}else if("2".equals(taskQueryVo.getPrePayType())){//????????????
			sqlUtil.append(" and task.riskCode <> ? ");
			sqlUtil.addParamValue(Risk.DQZ);
		}
		
		if(FlowNode.EndCas.equals(nodeCode)){
			logger.debug("??????=22=="+taskQueryVo.getClaimNo());
			sqlUtil.andReverse(taskQueryVo,table_query,7,"registNo");
			sqlUtil.andLike2(taskQueryVo,table_task,"claimNo","comCode");
			sqlUtil.andLike2(taskQueryVo,table_query,"insuredName");
		}
		
		// ??????????????????
		// "policyNo" ?????????????????? ////
		String policyNo = taskQueryVo.getPolicyNo();
		if(StringUtils.isNotBlank(policyNo)&&policyNo.length()>2){
			String policyNoRev = StringUtils.reverse(policyNo.toString()).trim();
			sqlUtil.append("AND (qry.policyNoRev LIKE ? Or qry.policyNoLink LIKE ? ) ");
			sqlUtil.addParamValue(policyNoRev+"%");
			sqlUtil.addParamValue("%"+policyNo+"%");
		}

		// ??????????????????????????????????????????????????????
		if((FlowNode.PLoss.equals(nodeCode)||FlowNode.PLBig.equals(nodeCode))
				&&( taskQueryVo.getInHospitalDateStart()!=null
				||taskQueryVo.getInHospitalDateEnd()!=null )){
			sqlUtil.append(" AND exists  (select  1 from PrpLDlossPersHospital hsp where hsp.registNo=qry.registNo ");
			sqlUtil.andDate(taskQueryVo,"hsp","inHospitalDate");
			sqlUtil.append(")");
		}
		
		//????????????????????????????????????????????????????????? ???????????????????????????
		if ( FlowNode.PLNext.equals(taskQueryVo.getSubNodeCode()) && StringUtils.isNotBlank(taskQueryVo.getYwTaskType()) ) {
			sqlUtil.append(" AND task.taskInNode like ? ");
			sqlUtil.addParamValue(taskQueryVo.getYwTaskType() + "%");
		}

		if(FlowNode.Regis.equals(nodeCode)&&taskQueryVo.getEngineNo()!=null&&taskQueryVo.getEngineNo()!=""){
			sqlUtil.append(" AND exists  (select  1 from PrpLCItemCar ite where ite.registNo=qry.registNo AND ite.policyNo=qry.policyNo ");
			sqlUtil.andLike2(taskQueryVo,"ite","engineNo");
			sqlUtil.append(")");
		}
		//??????????????????start
		if(FlowNode.Certi.equals(nodeCode) && StringUtils.isNotBlank(taskQueryVo.getIsVLoss()) ){
		    if("1".equals(taskQueryVo.getIsVLoss())){
		        sqlUtil.append(" AND  not exists  (select  1 from PrpLWfTaskIn wfTaskIn where wfTaskIn.nodeCode in (?,?,?,?) and wfTaskIn.registNo = qry.registNo)");
		    }
		    else if("0".equals(taskQueryVo.getIsVLoss())){
	            sqlUtil.append(" AND  exists  (select  1 from PrpLWfTaskIn wfTaskIn where wfTaskIn.nodeCode in (?,?,?,?) and wfTaskIn.registNo = qry.registNo)");
		    }
		        sqlUtil.addParamValue(FlowNode.VLoss.toString());//??????
	            sqlUtil.addParamValue(FlowNode.DLoss.toString());//??????
	            sqlUtil.addParamValue(FlowNode.VPrice.toString());//??????
	            sqlUtil.addParamValue(FlowNode.PLoss.toString());//??????
		}
		//??????????????????end
		// ??????
		if(FlowNode.PLoss.equals(nodeCode)){
			if("0".equals(taskQueryVo.getSorting())){//????????????
				if("1".equals(taskQueryVo.getSortType())){
					sqlUtil.append(" ORDER BY  task.taskInTime ASC");
				} else if("0".equals(taskQueryVo.getSortType())){//??????
					sqlUtil.append(" ORDER BY  task.taskInTime DESC");
				}
			}else if("1".equals(taskQueryVo.getSorting())){//????????????
				if("1".equals(taskQueryVo.getSortType())){
					sqlUtil.append(" ORDER BY  task.money ASC");
				} else if("0".equals(taskQueryVo.getSortType())){//??????
					sqlUtil.append(" ORDER BY  task.money DESC");
				}
			}else{
				sqlUtil.append(" ORDER BY qry.mercyFlag DESC, task.taskInTime DESC,qry.appointmentTime ASC");	
			}
		}else if(FlowNode.DLCar.equals(taskQueryVo.getSubNodeCode())){
			sqlUtil.append(" order by decode(sign(trunc(sysdate-qry.reportTime)-12),1,trunc(sysdate-qry.reportTime)-12,0) desc,task.taskInTime DESC");	
		}else{
			//??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
			
			if(HandlerStatus.END.equals(handleStatus) && FlowNode.VClaim.equals(taskQueryVo.getNodeCode())){
				sqlUtil.append(" ORDER BY task.taskOutTime DESC,qry.mercyFlag DESC ");
			}else if("0".equals(taskQueryVo.getSorting())){//????????????
				if("1".equals(taskQueryVo.getSortType())){
					sqlUtil.append(" ORDER BY  task.taskInTime ASC");
				} else if("0".equals(taskQueryVo.getSortType())){//??????
					sqlUtil.append(" ORDER BY  task.taskInTime DESC");
				}
			}else if("1".equals(taskQueryVo.getSorting())){//????????????
				if("1".equals(taskQueryVo.getSortType())){
					sqlUtil.append(" ORDER BY  task.money ASC");
				} else if("0".equals(taskQueryVo.getSortType())){//??????
					sqlUtil.append(" ORDER BY  task.money DESC");
				}
			}else if("2".equals(taskQueryVo.getSorting())){
				if("1".equals(taskQueryVo.getSortType())){
					sqlUtil.append(" ORDER BY  task.taskOutTime ASC");
				} else if("0".equals(taskQueryVo.getSortType())){//??????
					sqlUtil.append(" ORDER BY  task.taskOutTime DESC");
				}
			}else{
				sqlUtil.append(" ORDER BY qry.mercyFlag DESC, task.taskInTime DESC");	
			}
			//sqlUtil.append(" ORDER BY qry.mercyFlag DESC, task.taskInTime DESC");	
		}
		
		
		//sqlUtil.append(" ORDER BY qry.appointmentTime ASC");
		// ???????????????
		int start = taskQueryVo.getStart();
		// ??????????????????
		int length = taskQueryVo.getLength();

		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		logger.debug("taskQrySql="+sql);
		logger.debug("taskQueryVo.getNodeCode()========"+taskQueryVo.getNodeCode());
		logger.debug("taskQueryVo.getNodeCode()========"+taskQueryVo.getHandleStatus());
		logger.debug("taskQueryVo.getSubNodeCode()========"+taskQueryVo.getSubNodeCode());
		logger.debug("taskQueryVo.getNodeCode()========"+taskQueryVo.getNodeCode());
		logger.debug("taskQueryVo.getSubNodeCode()========"+taskQueryVo.getSubNodeCode());
		logger.debug("ParamValues="+ArrayUtils.toString(values));
		//System.out.println("ParamValues="+ArrayUtils.toString(values));
		//System.out.println("taskQrySql="+sql);
		//System.out.println("ParamValues="+ArrayUtils.toString(values));
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		Date date = new Date();
		// ????????????
		List<WfTaskQueryResultVo> resultVoList=new ArrayList<WfTaskQueryResultVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){

			WfTaskQueryResultVo resultVo=new WfTaskQueryResultVo();
			Object[] obj = page.getResult().get(i);

			PrpLWfTaskQuery wfTaskQuery = (PrpLWfTaskQuery)obj[1];
			Beans.copy().from(wfTaskQuery).to(resultVo);
			
			//??????????????????start
	        if(FlowNode.Certi.equals(nodeCode)){
	            BigDecimal sumVeriLossFee = new BigDecimal("0");
	            List<PrpLDlossCarMainVo> dlossCarMainVos = lossCarService.findLossCarMainByRegistNo(resultVo.getRegistNo());
	            List<PrpLdlossPropMainVo> lossPropMainList = propTaskService.findPropMainListByRegistNo(resultVo.getRegistNo());
	            List<PrpLDlossPersTraceMainVo> lossPersTraceMainList = deflossHandleService.findlossPersTraceMainByRegistNo(resultVo.getRegistNo());          
	            if(dlossCarMainVos != null&&dlossCarMainVos.size() > 0){
                    for(PrpLDlossCarMainVo prpLDlossCarMainVo:dlossCarMainVos){
                        if(VeriFlag.PASS.equals(prpLDlossCarMainVo.getUnderWriteFlag())){
                            sumVeriLossFee = sumVeriLossFee.add(NullToZero(prpLDlossCarMainVo.getSumVeriLossFee()));
                        }
                    }
                }
	            if(lossPropMainList != null&&lossPropMainList.size() > 0){
                    for(PrpLdlossPropMainVo prpLdlossPropMainVo:lossPropMainList){
                        if(VeriFlag.PASS.equals(prpLdlossPropMainVo.getUnderWriteFlag())){
                            sumVeriLossFee = sumVeriLossFee.add(NullToZero(prpLdlossPropMainVo.getSumVeriLoss()));
                        }
                    }
                }
                if (lossPersTraceMainList != null&&lossPersTraceMainList.size() > 0) {
                    for (PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo : lossPersTraceMainList) {
                        if(!CodeConstants.AuditStatus.SUBMITCHARGE.equals(prpLDlossPersTraceMainVo.getAuditStatus())
                                &&!CodeConstants.AuditStatus.SUBMITVERIFY.equals(prpLDlossPersTraceMainVo.getAuditStatus())
                                ||"10".equals(prpLDlossPersTraceMainVo.getCaseProcessType())){
                            // ??????????????????????????????????????????????????????????????? ???????????????????????????????????? 
                            // ??????????????????????????????  ??????????????????????????????????????????????????? ?????????????????????
                            continue;
                        }
                        List<PrpLDlossPersTraceVo> lossPersTraceVos = prpLDlossPersTraceMainVo.getPrpLDlossPersTraces();
                        if (lossPersTraceVos != null&&lossPersTraceVos.size() > 0) {
                            for (PrpLDlossPersTraceVo prpLDlossPersTraceVo : lossPersTraceVos) {
                                if (CodeConstants.ValidFlag.INVALID.equals(prpLDlossPersTraceVo.getValidFlag())) {
                                    continue;
                                }
                                List<PrpLDlossPersTraceFeeVo> lossPersTraceFeeList = prpLDlossPersTraceVo.getPrpLDlossPersTraceFees();
                                if (lossPersTraceFeeList != null&&lossPersTraceFeeList.size() > 0) {
                                    for (PrpLDlossPersTraceFeeVo prpLDlossPersTraceFeeVo : lossPersTraceFeeList) {
                                        sumVeriLossFee = sumVeriLossFee.add(NullToZero(prpLDlossPersTraceFeeVo.getVeriDefloss()));
                                    }                   
                                }                   
                            }
                        }
                    }
                }
	            resultVo.setSumVeriLossFee(sumVeriLossFee.toString());
	            if(StringUtils.isNotBlank(taskQueryVo.getIsVLoss())){
	                if("1".equals(taskQueryVo.getIsVLoss())){
	                    resultVo.setIsVLoss("1");
	                }
	                else if("0".equals(taskQueryVo.getIsVLoss())){
	                    resultVo.setIsVLoss("0");
	                }
	            }else{//?????????????????????
	                List<String> nodeCodeList = new ArrayList<String>();
                    nodeCodeList.add(FlowNode.VLoss.toString());//??????
                    nodeCodeList.add(FlowNode.DLoss.toString());//??????
                    nodeCodeList.add(FlowNode.VPrice.toString());//??????
                    nodeCodeList.add(FlowNode.PLoss.toString());//??????
                    List<WfTaskQueryResultVo> wfTaskQueryResultVoList = findPrpLWfTaskInByRegistNo(resultVo.getRegistNo(),nodeCodeList);
                    if(wfTaskQueryResultVoList != null && wfTaskQueryResultVoList.size() > 0){
                        resultVo.setIsVLoss("0");
                    }
                    else{
                        resultVo.setIsVLoss("1");
                    }
	            }
	        }
			//??????????????????end
			if(HandlerStatus.END.equals(handleStatus)||HandlerStatus.CANCEL.equals(handleStatus)){//?????????????????????
				PrpLWfTaskOut prpLWfTaskIn = (PrpLWfTaskOut)obj[0];
				Beans.copy().from(prpLWfTaskIn).excludeNull().to(resultVo);
				//??????????????????yzy
	            if(FlowNode.VClaim.equals(nodeCode) && StringUtils.isBlank(taskQueryVo.getCompensateAmount())){
	            	
	            	if(prpLWfTaskIn!=null && StringUtils.isNotBlank(prpLWfTaskIn.getHandlerIdKey()) && !prpLWfTaskIn.getHandlerIdKey().startsWith("D")){
	            		PrpLCompensateVo compensateVo=compensateTaskService.findCompByPK(prpLWfTaskIn.getHandlerIdKey());
	            		if(compensateVo!=null){
		            		resultVo.setCompensateAmount(DataUtils.NullToZero(compensateVo.getSumAmt()).add(DataUtils.NullToZero(compensateVo.getSumFee())));
		            	}
	            	}else if(prpLWfTaskIn!=null && StringUtils.isNotBlank(prpLWfTaskIn.getHandlerIdKey()) && prpLWfTaskIn.getHandlerIdKey().startsWith("D")){
	            		PrpLPadPayMainVo padpayVo=	padPayService.findPadPayMainByCompNo(prpLWfTaskIn.getHandlerIdKey());
	            		BigDecimal payAmount=new BigDecimal("0");
	            		if(padpayVo!=null){
	            			for(PrpLPadPayPersonVo personVo:padpayVo.getPrpLPadPayPersons()){
	            				payAmount=payAmount.add(personVo.getCostSum());
	            				}
	            			resultVo.setCompensateAmount(DataUtils.NullToZero(payAmount));
	            			
	            		}
	            		
	            	}
	            		
	            	
	            }
				
			}else{
				PrpLWfTaskIn prpLWfTaskOut = (PrpLWfTaskIn)obj[0];
				Beans.copy().from(prpLWfTaskOut).excludeNull().to(resultVo);
				//??????????????????yzy
	            if(FlowNode.VClaim.equals(nodeCode) && StringUtils.isBlank(taskQueryVo.getCompensateAmount())){
	            	if(prpLWfTaskOut!=null && StringUtils.isNotBlank(prpLWfTaskOut.getHandlerIdKey()) && !prpLWfTaskOut.getHandlerIdKey().startsWith("D")){
	            		PrpLCompensateVo compensateVo=compensateTaskService.findCompByPK(prpLWfTaskOut.getHandlerIdKey());
	            		if(compensateVo!=null){
		            		resultVo.setCompensateAmount(DataUtils.NullToZero(compensateVo.getSumAmt()).add(DataUtils.NullToZero(compensateVo.getSumFee())));
		            	}
	            	}else if(prpLWfTaskOut!=null && StringUtils.isNotBlank(prpLWfTaskOut.getHandlerIdKey()) && prpLWfTaskOut.getHandlerIdKey().startsWith("D")){
	            		PrpLPadPayMainVo padpayVo=	padPayService.findPadPayMainByCompNo(prpLWfTaskOut.getHandlerIdKey());
	            		BigDecimal payAmount=new BigDecimal("0");
	            		if(padpayVo!=null){
	            			for(PrpLPadPayPersonVo personVo:padpayVo.getPrpLPadPayPersons()){
	            				payAmount=payAmount.add(personVo.getCostSum());
	            				}
	            			resultVo.setCompensateAmount(DataUtils.NullToZero(payAmount));
	            		}
	            	}
	            	
	            }
			}
			
			
			if (FlowNode.PLoss.equals(resultVo.getNodeCode())
					||FlowNode.PLBig.equals(resultVo.getNodeCode())) {// ??????????????????????????????????????????????????????
				Date currentDate = new Date();
				if(resultVo.getReportTime() != null){
					int reportDay = DateUtils.compareDays(currentDate,resultVo.getReportTime());
					resultVo.setReportDay(reportDay);
				}
				if(resultVo.getAppointmentTime() != null){
					int appointmentDay = DateUtils.compareDays(resultVo.getAppointmentTime(),currentDate);
					resultVo.setAppointmentDay(appointmentDay);
				}
			}
			//appointTaskInTime??????????????????????????????????????????????????????????????????????????????????????????
			if (FlowNode.PLoss.equals(resultVo.getNodeCode())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date nowDate = new Date();
				String nowDates = sdf.format(nowDate);
				String taskInTime = sdf.format(resultVo.getTaskInTime());
				nowDate = sdf.parse(nowDates);
				Date taskInTimes=sdf.parse(taskInTime);
				
				long appointTaskInTime = nowDate.getTime() - taskInTimes.getTime();
//				long day = appointTaskInTime/(24*60*60*1000);
				long hour = (appointTaskInTime/(60*60*1000));
				resultVo.setAppointTaskInTime(hour);
			}
		
			//?????????????????????HandlerIdKey??????????????????
			if(FlowNode.Claim.equals(resultVo.getNodeCode())){
				resultVo.setClaimNo(resultVo.getHandlerIdKey());
			}
			//??????
			if(FlowNode.RecPay.equals(resultVo.getNodeCode())){
				resultVo.setCompensateNo(resultVo.getHandlerIdKey());
			}
			//???????????????????????????taskInKey??????????????????
			if(FlowNode.ReOpenVrf.equals(resultVo.getSubNodeCode())){
				resultVo.setClaimNo(resultVo.getHandlerIdKey());
			}
			//????????????????????????
			if(FlowNode.Check.equals(resultVo.getNodeCode()) || FlowNode.PLoss.equals(resultVo.getNodeCode())){
				//?????????
				SysUserVo userVo = sysUserService.findByUserCode(taskQueryVo.getUserCode());
				if(userVo.getPhone()!=null){
					resultVo.setAssignUserPhone(userVo.getPhone());
				}else{
					resultVo.setAssignUserPhone(userVo.getMobile());
				}
				//????????????
				String overTime = "";
				if(resultVo.getTaskInTime() != null){
					overTime = String.valueOf(((date.getTime()-resultVo.getTaskInTime().getTime())/(60*1000)));
				}
				resultVo.setOverTime(overTime);
			}
			//??????????????????????????????????????????
			if(FlowNode.Check.equals(resultVo.getNodeCode())){
				List<PrpLScheduleTaskVo> scheduleTaskVoList = scheduleService.findScheduleTask(resultVo.getRegistNo(), "0", "1");
				if(scheduleTaskVoList.size()>0){
					resultVo.setSchedLastTime(DateUtils.dateToStr(scheduleTaskVoList.get(0).getScheduledTime(), DateUtils.YToSec));
					resultVo.setSchedFirstTime(DateUtils.dateToStr(scheduleTaskVoList.get(scheduleTaskVoList.size()-1).getScheduledTime(), DateUtils.YToSec));
				}
			}
			if(FlowNode.PLoss.equals(resultVo.getNodeCode())){
				List<PrpLScheduleTaskVo> scheduleTaskVoList = scheduleService.findScheduleTask(resultVo.getRegistNo(), "1", "1");
				if(scheduleTaskVoList.size()>0){
					resultVo.setSchedLastTime(DateUtils.dateToStr(scheduleTaskVoList.get(0).getScheduledTime(), DateUtils.YToSec));
					resultVo.setSchedFirstTime(DateUtils.dateToStr(scheduleTaskVoList.get(scheduleTaskVoList.size()-1).getScheduledTime(), DateUtils.YToSec));
				}
			}
			
			if(StringUtils.isBlank(taskQueryVo.getCompensateAmount())){
			// liuping ???????????????????????????????????????????????????
			resultVo.setComCode(resultVo.getComCodePly());
			resultVoList.add(resultVo);
			}else{
				if("1".equals(taskQueryVo.getCompensateAmount()) && resultVo!=null && StringUtils.isNotBlank(resultVo.getHandlerIdKey()) && !resultVo.getHandlerIdKey().startsWith("D")){
	            		PrpLCompensateVo compensateVo=compensateTaskService.findCompByPK(resultVo.getHandlerIdKey());
	            		if(compensateVo!=null){
	            			 BigDecimal a=DataUtils.NullToZero(compensateVo.getSumAmt()).abs();
	            			 BigDecimal b=DataUtils.NullToZero(compensateVo.getSumFee()).abs();
	            			 BigDecimal c=a.add(b);
	            			 if(c.doubleValue()>=0 && c.doubleValue()<5000){
	            				// liuping ???????????????????????????????????????????????????
	            				    resultVo.setCompensateAmount(DataUtils.NullToZero(compensateVo.getSumAmt()).add(DataUtils.NullToZero(compensateVo.getSumFee())));
	            					resultVo.setComCode(resultVo.getComCodePly());
	            					resultVoList.add(resultVo);
	            			 }
	            		}
	            	}else if("2".equals(taskQueryVo.getCompensateAmount()) && resultVo!=null && StringUtils.isNotBlank(resultVo.getHandlerIdKey()) && !resultVo.getHandlerIdKey().startsWith("D")){
	            		PrpLCompensateVo compensateVo=compensateTaskService.findCompByPK(resultVo.getHandlerIdKey());
	            		if(compensateVo!=null){
	            			 BigDecimal a=DataUtils.NullToZero(compensateVo.getSumAmt()).abs();
	            			 BigDecimal b=DataUtils.NullToZero(compensateVo.getSumFee()).abs();
	            			 BigDecimal c=a.add(b);
	            			 if(c.doubleValue()>=5000 && c.doubleValue()<10000){
	            				// liuping ???????????????????????????????????????????????????
	            				    resultVo.setCompensateAmount(DataUtils.NullToZero(compensateVo.getSumAmt()).add(DataUtils.NullToZero(compensateVo.getSumFee())));
	            					resultVo.setComCode(resultVo.getComCodePly());
	            					resultVoList.add(resultVo);
	            			 }
	            		}
	            	}else if("3".equals(taskQueryVo.getCompensateAmount()) && resultVo!=null && StringUtils.isNotBlank(resultVo.getHandlerIdKey()) && !resultVo.getHandlerIdKey().startsWith("D")){
	            		PrpLCompensateVo compensateVo=compensateTaskService.findCompByPK(resultVo.getHandlerIdKey());
	            		if(compensateVo!=null){
	            			 BigDecimal a=DataUtils.NullToZero(compensateVo.getSumAmt()).abs();
	            			 BigDecimal b=DataUtils.NullToZero(compensateVo.getSumFee()).abs();
	            			 BigDecimal c=a.add(b);
	            			 if(c.doubleValue()>=10000){
	            				// liuping ???????????????????????????????????????????????????
	            				    resultVo.setCompensateAmount(DataUtils.NullToZero(compensateVo.getSumAmt()).add(DataUtils.NullToZero(compensateVo.getSumFee())));
	            					resultVo.setComCode(resultVo.getComCodePly());
	            					resultVoList.add(resultVo);
	            			 }
	            		}
	            	}else if("1".equals(taskQueryVo.getCompensateAmount()) && resultVo!=null && StringUtils.isNotBlank(resultVo.getHandlerIdKey()) && resultVo.getHandlerIdKey().startsWith("D")){
	            		PrpLPadPayMainVo padpayVo=	padPayService.findPadPayMainByCompNo(resultVo.getHandlerIdKey());
	            		BigDecimal payAmount=new BigDecimal("0");
	            		if(padpayVo!=null){
	            			for(PrpLPadPayPersonVo personVo:padpayVo.getPrpLPadPayPersons()){
	            				payAmount=payAmount.add(DataUtils.NullToZero(personVo.getCostSum()));
	            				}
	            			if(payAmount.doubleValue()>=0 && payAmount.doubleValue()<5000){
	            				// liuping ???????????????????????????????????????????????????
	            				resultVo.setCompensateAmount(payAmount);
            					resultVo.setComCode(resultVo.getComCodePly());
            					resultVoList.add(resultVo);
	            			}
	            		}
	            	}else if("2".equals(taskQueryVo.getCompensateAmount()) && resultVo!=null && StringUtils.isNotBlank(resultVo.getHandlerIdKey()) && resultVo.getHandlerIdKey().startsWith("D")){
	            		PrpLPadPayMainVo padpayVo=	padPayService.findPadPayMainByCompNo(resultVo.getHandlerIdKey());
	            		BigDecimal payAmount=new BigDecimal("0");
	            		if(padpayVo!=null){
	            			for(PrpLPadPayPersonVo personVo:padpayVo.getPrpLPadPayPersons()){
	            				payAmount=payAmount.add(DataUtils.NullToZero(personVo.getCostSum()));
	            				}
	            			if(payAmount.doubleValue()>=5000 && payAmount.doubleValue()<10000){
	            				// liuping ???????????????????????????????????????????????????
	            				resultVo.setCompensateAmount(payAmount);
            					resultVo.setComCode(resultVo.getComCodePly());
            					resultVoList.add(resultVo);
	            			}
	            		}
	            	}else if("3".equals(taskQueryVo.getCompensateAmount()) && resultVo!=null && StringUtils.isNotBlank(resultVo.getHandlerIdKey()) && resultVo.getHandlerIdKey().startsWith("D")){
	            		PrpLPadPayMainVo padpayVo=	padPayService.findPadPayMainByCompNo(resultVo.getHandlerIdKey());
	            		BigDecimal payAmount=new BigDecimal("0");
	            		if(padpayVo!=null){
	            			for(PrpLPadPayPersonVo personVo:padpayVo.getPrpLPadPayPersons()){
	            				payAmount=payAmount.add(DataUtils.NullToZero(personVo.getCostSum()));
	            				}
	            			if(payAmount.doubleValue()>=10000){
	            				// liuping ???????????????????????????????????????????????????
	            				resultVo.setCompensateAmount(payAmount);
            					resultVo.setComCode(resultVo.getComCodePly());
            					resultVoList.add(resultVo);
	            			}
	            		}
	            	}
	            }
			
		}

		ResultPage<WfTaskQueryResultVo> resultPage = new ResultPage<WfTaskQueryResultVo> (start, length, page.getTotalCount(), resultVoList);
		if(resultPage != null && resultPage.getData().size() != 0 && FlowNode.EndCas.equals(resultPage.getData().get(0).getNodeCode())){
			List<WfTaskQueryResultVo>  resuVo = resultPage.getData();
			for(WfTaskQueryResultVo result : resuVo){
				String claimKind = result.getClaimNo().substring(11, 14);
				String poliyKind = result.getPolicyNo().substring(11, 14);
				if(!claimKind.equals(poliyKind)){
					result.setPolicyNo(result.getPolicyNoLink());
					if("1101".equals(claimKind)){
						result.setRiskCode("1206");
					}else{
						result.setRiskCode("1101");
					}
					
				}
			}
		}
		return resultPage;
		
	}
	
	/* 
	 * @see ins.sino.claimcar.flow.service.WfTaskQueryService#findHandoverTaskForPage(ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo)
	 * @param taskQueryVo
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResultPage<WfTaskQueryResultVo> findHandoverTaskForPage(PrpLWfTaskQueryVo taskQueryVo) throws Exception {
		logger.info("findHandoverTaskForPage start");
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" FROM PrpLWfTaskQuery qry,PrpLWfTaskIn task");
		sqlUtil.append(" WHERE task.flowId= qry.flowId  ");
		// ????????????????????????????????????????????????????????????????????????????????????
		sqlUtil.append(" AND task.handlerStatus in(?,?,?)");
		sqlUtil.addParamValue(HandlerStatus.INIT);
		sqlUtil.addParamValue(HandlerStatus.START);
		sqlUtil.addParamValue(HandlerStatus.DOING);
		//sqlUtil.addParamValue(WorkStatus.TURN);

		sqlUtil.append(" AND task.workStatus in(?,?,?,?)");
		sqlUtil.addParamValue(WorkStatus.INIT);
		sqlUtil.addParamValue(WorkStatus.DOING);
		sqlUtil.addParamValue(WorkStatus.START);
		sqlUtil.addParamValue(WorkStatus.BYBACK);
		// ?????????????????????
		sqlUtil.append(" AND task.subNodeCode in(SELECT nodeCode FROM PrpDNode node WHERE node.remark = ?)");
		sqlUtil.addParamValue("1");
		
		// ????????????
		sqlUtil.andEquals(taskQueryVo,"task","registNo","subNodeCode");
		
		if(FlowNode.Handover.name().equals(taskQueryVo.getNodeCode())){// ????????????????????????
			sqlUtil.append(" AND (task.assignUser in (?) OR task.handlerUser in (?)) ");
			sqlUtil.addParamValue(ServiceUserUtils.getUserCode());
			sqlUtil.addParamValue(ServiceUserUtils.getUserCode());
		}
		
		// ??????
		sqlUtil.append(" ORDER BY task.taskInTime DESC");
		// ???????????????
		int start = taskQueryVo.getStart();
		// ??????????????????
		int length = taskQueryVo.getLength();

		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		logger.info("taskQrySql="+sql);
		logger.info("ParamValues="+ArrayUtils.toString(values));
		
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);

		// ????????????
		List<WfTaskQueryResultVo> resultVoList=new ArrayList<WfTaskQueryResultVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){

			WfTaskQueryResultVo resultVo=new WfTaskQueryResultVo();
			Object[] obj = page.getResult().get(i);

			PrpLWfTaskQuery wfTaskQuery = (PrpLWfTaskQuery)obj[0];
			Beans.copy().from(wfTaskQuery).to(resultVo);

			PrpLWfTaskIn prpLWfTaskin = (PrpLWfTaskIn)obj[1];
			Beans.copy().from(prpLWfTaskin).excludeNull().to(resultVo);
			if(StringUtils.isBlank(resultVo.getHandoverTimes())){
				resultVo.setHandoverTimes("0");
			}
			resultVo.setComCode(resultVo.getComCodePly());
			resultVoList.add(resultVo);
		}
		logger.info("findHandoverTaskForPage end");
		ResultPage<WfTaskQueryResultVo> resultPage = new ResultPage<WfTaskQueryResultVo> (start, length, page.getTotalCount(), resultVoList);

		return resultPage;
				
	}
	
	/**
	 * ??????????????????
	 * @throws Exception 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public ResultPage<WfTaskQueryResultVo> findCancelTaskForPage(PrpLWfTaskQueryVo taskQueryVo) throws Exception{
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		String  table_query="qry";
		String comCode = taskQueryVo.getComCode();
		String subNodeCode = taskQueryVo.getSubNodeCode();
		String handleStatus = taskQueryVo.getHandleStatus();
		
		sqlUtil.append("SELECT qry.registNo,task.taskName,task.comCode,task.taskInTime,"
				+ "task.taskInUser,qry.policyNo,qry.insuredName,task.showInfoXML,task.bussTag,qry.licenseNo,"
				+ "task.taskInKey,task.handlerIdKey,task.flowId,task.taskId,task.subNodeCode"
				+ " FROM PrpLWfTaskIn task, PrpLWfTaskQuery qry WHERE task.flowId=qry.flowId ");
		sqlUtil.andReverse(taskQueryVo,table_query,7,"registNo");
		sqlUtil.andReverse(taskQueryVo,table_query,7,"frameNo");
		sqlUtil.andEquals(taskQueryVo,table_query,"mercyFlag");
		sqlUtil.andLike2(taskQueryVo,table_query,"insuredName","licenseNo");
		
		if(StringUtils.isNotBlank(comCode)){
			if(!"00000000".equals(comCode)){
				sqlUtil.append(" AND task.comCode LIKE ?  ");
				if(comCode.startsWith("00")){
					sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
				}else{
					sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
				}
			}			
		}
		if("DLCars".equals(subNodeCode)){
			sqlUtil.append(" AND task.subNodeCode IN (? , ?)  ");
			sqlUtil.addParamValue(FlowNode.DLCar.name());
			sqlUtil.addParamValue(FlowNode.DLCarMod.name());
		}else if("DLProps".equals(subNodeCode)){
			sqlUtil.append(" AND task.subNodeCode IN (? , ?)  ");
			sqlUtil.addParamValue(FlowNode.DLProp.name());
			sqlUtil.addParamValue(FlowNode.DLPropMod.name());
		}else{
			sqlUtil.append(" AND task.subNodeCode = ?  ");
			sqlUtil.addParamValue(subNodeCode);
		}
		
		if(subNodeCode.startsWith("DLCar")){
			sqlUtil.append(" AND task.showInfoXML like ?  ");
			sqlUtil.addParamValue("%"+"?????????"+"%");
		}
		
		if (WorkStatus.BYBACK.equals(handleStatus)) {
			sqlUtil.append(" AND task.handlerStatus =? and  task.workStatus =?");
			sqlUtil.addParamValue(HandlerStatus.INIT);
			sqlUtil.addParamValue(WorkStatus.BYBACK);
		}else{
			sqlUtil.append(" AND task.handlerStatus =? and  task.workStatus =?");
			sqlUtil.addParamValue(handleStatus);
			sqlUtil.addParamValue(handleStatus);
		}
		if(taskQueryVo.getTaskInTimeStart()!=null && taskQueryVo.getTaskInTimeEnd()!=null){
			sqlUtil.append(" AND task.taskInTime >= ? AND  task.taskInTime < ?  ");
			sqlUtil.addParamValue(taskQueryVo.getTaskInTimeStart());
			sqlUtil.addParamValue(DateUtils.addDays(taskQueryVo.getTaskInTimeEnd(), 1));
		}
		sqlUtil.append(" ORDER BY task.taskInTime DESC ");
		
		// ???????????????
		int start = taskQueryVo.getStart();
		// ??????????????????
		int length = taskQueryVo.getLength();
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		@SuppressWarnings("unchecked")
		Page<Object[]> page = baseDaoService.pagedSQLQuery(sql,start,length,values);
		// ????????????
		List<WfTaskQueryResultVo> resultVoList=new ArrayList<WfTaskQueryResultVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){
			WfTaskQueryResultVo resultVo=new WfTaskQueryResultVo();
			Object[] obj = (Object[])page.getResult().get(i);
			resultVo.setRegistNo((String)obj[0]);
			resultVo.setTaskName((String)obj[1]);
			resultVo.setComCode((String)obj[2]);
			resultVo.setTaskInTime((Date)obj[3]);
			resultVo.setTaskInUser((String)obj[4]);
			resultVo.setPolicyNo((String)obj[5]);
			resultVo.setInsuredName((String)obj[6]);
			resultVo.setShowInfoXML((String)obj[7]);
			resultVo.setBussTag((String)obj[8]);
			resultVo.setLicenseNo((String)obj[9]);
			resultVo.setTaskInKey((String)obj[10]);
			resultVo.setHandlerIdKey((String)obj[11]);
			resultVo.setFlowId((String)obj[12]);
			resultVo.setTaskId((BigDecimal)obj[13]);
			resultVo.setSubNodeCode((String)obj[14]);
			resultVoList.add(resultVo);
		}
		ResultPage<WfTaskQueryResultVo> resultPage = new ResultPage<WfTaskQueryResultVo> (start, length, page.getTotalCount(), resultVoList);

		return resultPage;
	}
	
	
	/* 
	 * @see ins.sino.claimcar.flow.service.WfTaskQueryService#findPLossTaskForPage(ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo)
	 * @param taskQueryVo
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResultPage<WfTaskQueryResultVo> findPLossTaskForPage(PrpLWfTaskQueryVo taskQueryVo) throws Exception {
		logger.debug("findPLossTaskForPage start");
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		List<String> paramValues = new ArrayList<String>();
		sqlUtil.append(" FROM PrpLWfTaskQuery qry,PrpLWfTaskIn task");
		sqlUtil.append(" WHERE task.flowId= qry.flowId  ");
		String handleStatus = taskQueryVo.getHandleStatus();
		// ???????????????????????????????????????????????????????????????????????????????????????
		if(handleStatus.indexOf(",")>0){
			String[] str = handleStatus.split(",");
			String maskStr = SqlJoinUtils.arrayToMask(str);
			sqlUtil.append(" AND task.handlerStatus in ("+maskStr+")");
			paramValues.addAll(SqlJoinUtils.arrayToList(str));
			/*for(int i=0; i<str.length; i++){
				if(WorkStatus.BYBACK.equals(str[i])){
					sqlUtil.append(" or  task.workStatus in (?,?)");
					paramValues.add(HandlerStatus.INIT);
					paramValues.add(WorkStatus.BYBACK);
				}
			}*/
		}else{
			sqlUtil.append(" AND  task.handlerStatus = ? ");
			paramValues.add(handleStatus);
			/*if(WorkStatus.BYBACK.equals(handleStatus)){
				sqlUtil.append(" or  task.workStatus in (?,?)");
				paramValues.add(HandlerStatus.INIT);
				paramValues.add(WorkStatus.BYBACK);
			}*/
		}
		//????????????????????????????????????
		if(("PLVerify".equals(taskQueryVo.getSubNodeCode()) || "PLCharge".equals(taskQueryVo.getSubNodeCode()))
			&&(ServiceUserUtils.getComCode().startsWith("0000")||ServiceUserUtils.getComCode().startsWith("0001"))){
			
			sqlUtil.append(" and ((task.handlerUser = null and task.assignCom like ?) ");
			paramValues.add(ServiceUserUtils.getComCode().substring(0, 4)+"%");
			sqlUtil.append(" or (task.handlerUser = ?)) ");
			paramValues.add(ServiceUserUtils.getUserCode());
		}else{
			sqlUtil.append(" AND (task.assignUser in (?) OR task.handlerUser in (?)) ");
			paramValues.add(ServiceUserUtils.getUserCode());
			paramValues.add(ServiceUserUtils.getUserCode());
		}
		
		sqlUtil.append(" AND task.subNodeCode like ?");
		paramValues.add(taskQueryVo.getSubNodeCode()+"%");
		
		// ??????
		sqlUtil.append(" ORDER BY qry.appointmentTime ASC,task.taskInTime DESC");
		// ???????????????
		int start = taskQueryVo.getStart();
		// ??????????????????
		int length = taskQueryVo.getLength();

		String sql = sqlUtil.getSql();
//		Object[] values = sqlUtil.getParamValues();
		logger.debug("taskQrySql="+sql);
		logger.debug("ParamValues="+ArrayUtils.toString(paramValues));
		
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,paramValues.toArray());

		// ????????????
		List<WfTaskQueryResultVo> resultVoList=new ArrayList<WfTaskQueryResultVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){

			WfTaskQueryResultVo resultVo=new WfTaskQueryResultVo();
			Object[] obj = page.getResult().get(i);

			PrpLWfTaskQuery wfTaskQuery = (PrpLWfTaskQuery)obj[0];
			Beans.copy().from(wfTaskQuery).to(resultVo);

			PrpLWfTaskIn prpLWfTaskin = (PrpLWfTaskIn)obj[1];
			Beans.copy().from(prpLWfTaskin).excludeNull().to(resultVo);
			resultVo.setComCode(resultVo.getComCodePly());
			
			Date currentDate = new Date();
			if(resultVo.getReportTime() != null){
				int reportDay = DateUtils.compareDays(currentDate,resultVo.getReportTime());
				resultVo.setReportDay(reportDay);
			}
			if(resultVo.getAppointmentTime() != null){
				int appointmentDay = DateUtils.compareDays(resultVo.getAppointmentTime(),currentDate);
				resultVo.setAppointmentDay(appointmentDay);
			}
			if(WorkStatus.BYBACK.equals(prpLWfTaskin.getWorkStatus())){
				resultVo.setBackFlags("1");
			}else{
				resultVo.setBackFlags("0");
			}
			if(WorkStatus.DOING.equals(prpLWfTaskin.getWorkStatus()) && FlowNode.PLNext.equals(prpLWfTaskin.getSubNodeCode())){
				String bussTag = prpLWfTaskin.getBussTag();
				if(StringUtils.isNotBlank(bussTag)){
					bussTag = bussTag.replace("{", "");
					bussTag = bussTag.replace("}", "");
					if(StringUtils.isNotBlank(bussTag)){
						bussTag = bussTag.replace("\"", "");
						String a[] =bussTag.split(",");
						if(a.length > 1){
							for(int j=0; j < a.length;j++){
								String backFlags[] = a[j].split(":");
								if(backFlags.length > 1){
									if("backFlags".equals(backFlags[0])){
										if("1".equals(backFlags[1])){
											resultVo.setBackFlags("1");
										}else{
											resultVo.setBackFlags("0");
										}
									}
								}
							}
						}
					}
				}
			}
			if(FlowNode.PLFirst.name().equals(prpLWfTaskin.getSubNodeCode())){
				if(isTimeOut(prpLWfTaskin.getRegistNo(), prpLWfTaskin.getSubNodeCode(), prpLWfTaskin.getHandlerStatus())){
					
					TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
					PrpLWfTaskVo wfTaskVo = new PrpLWfTaskVo();
					resultVo.setTimeOut("1");
					extMapUtil.addTagMap(resultVo,"timeOut");
					wfTaskVo.setBussTagMap(extMapUtil.getBussTagMap());
					String bussTag = TaskExtMapUtils.joinBussTag(resultVo.getBussTag(),wfTaskVo.getBussTagMap());
					resultVo.setBussTag(bussTag);
				}
			}
			
			resultVoList.add(resultVo);
		}
		
		logger.debug("findPLossTaskForPage end");
		ResultPage<WfTaskQueryResultVo> resultPage = new ResultPage<WfTaskQueryResultVo> (start, length, page.getTotalCount(), resultVoList);
		
		return resultPage;
				
	}
	
	@Override
	public int countLossTimeOut(PrpLWfTaskQueryVo taskQueryVo) {
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		List<String> paramValues = new ArrayList<String>();
		sqlUtil.append(" FROM PrpLWfTaskQuery qry,PrpLWfTaskIn task");
		sqlUtil.append(" WHERE task.flowId= qry.flowId  ");
		String handleStatus = taskQueryVo.getHandleStatus();
		// ???????????????????????????????????????????????????????????????????????????????????????
		if(handleStatus.indexOf(",")>0){
			String[] str = handleStatus.split(",");
			String maskStr = SqlJoinUtils.arrayToMask(str);
			sqlUtil.append(" AND task.handlerStatus in ("+maskStr+")");
			paramValues.addAll(SqlJoinUtils.arrayToList(str));
			/*for(int i=0; i<str.length; i++){
				if(WorkStatus.BYBACK.equals(str[i])){
					sqlUtil.append(" or  task.workStatus in (?,?)");
					paramValues.add(HandlerStatus.INIT);
					paramValues.add(WorkStatus.BYBACK);
				}
			}*/
		}else{
			sqlUtil.append(" AND  task.handlerStatus = ? ");
			paramValues.add(handleStatus);
			/*if(WorkStatus.BYBACK.equals(handleStatus)){
				sqlUtil.append(" or  task.workStatus in (?,?)");
				paramValues.add(HandlerStatus.INIT);
				paramValues.add(WorkStatus.BYBACK);
			}*/
		}
		//????????????????????????????????????
		if(("PLVerify".equals(taskQueryVo.getSubNodeCode()) || "PLCharge".equals(taskQueryVo.getSubNodeCode()))
			&&(ServiceUserUtils.getComCode().startsWith("0000")||ServiceUserUtils.getComCode().startsWith("0001"))){
			
			sqlUtil.append(" and ((task.handlerUser = null and task.assignCom like ?) ");
			paramValues.add(ServiceUserUtils.getComCode().substring(0, 4)+"%");
			sqlUtil.append(" or (task.handlerUser = ?)) ");
			paramValues.add(ServiceUserUtils.getUserCode());
		}else{
			sqlUtil.append(" AND (task.assignUser in (?) OR task.handlerUser in (?)) ");
			paramValues.add(ServiceUserUtils.getUserCode());
			paramValues.add(ServiceUserUtils.getUserCode());
		}
		
		sqlUtil.append(" AND task.subNodeCode like ?");
		paramValues.add(taskQueryVo.getSubNodeCode()+"%");
		
		// ??????
		sqlUtil.append(" ORDER BY qry.appointmentTime ASC,task.taskInTime DESC");
		// ???????????????
		int start = taskQueryVo.getStart();
		// ??????????????????
		int length = taskQueryVo.getLength();

		String sql = sqlUtil.getSql();
//		Object[] values = sqlUtil.getParamValues();
		logger.debug("taskQrySql="+sql);
		logger.debug("ParamValues="+ArrayUtils.toString(paramValues));
		
		List<Object[]> list = databaseDao.findAllByHql(sql, paramValues.toArray());
		int countTimeOut = 0;
		if(list!=null && list.size()>0){
			for(int i = 0; i<list.size(); i++ ){
				
				Object[] obj = list.get(i);
				PrpLWfTaskIn prpLWfTaskin = (PrpLWfTaskIn)obj[1];
				if(isTimeOut(prpLWfTaskin.getRegistNo(), prpLWfTaskin.getSubNodeCode(), prpLWfTaskin.getHandlerStatus())){
					countTimeOut = countTimeOut+1;
				}
			}
		}
		
		return countTimeOut;
	}
	
	/* 
	 * @see ins.sino.claimcar.flow.service.WfTaskQueryService#findAssessorFeeTaskQuery(ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo)
	 * @param taskQueryVo
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@Override
	public ResultPage<WfTaskQueryResultVo> findAssessorFeeTaskQuery( PrpLWfTaskQueryVo taskQueryVo
		 ) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
//	   String nodeCode = taskQueryVo.getNodeCode();
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		// ????????????????????????
		String handleStatus = taskQueryVo.getHandleStatus();
		// ??????????????????????????????
		if(HandlerStatus.END.equals(handleStatus)||HandlerStatus.CANCEL.equals(handleStatus)){
			sqlUtil.append(" FROM PrpLAssessorMain main,PrpLWfTaskOut task ");
		} else{
			sqlUtil.append(" FROM PrpLAssessorMain main,PrpLWfTaskIn task ");
		}
		
		sqlUtil.append(" where 1=1 AND main.taskId=task.registNo");
	      //???????????????????????????
		if(HandlerStatus.DOING.equals(handleStatus)){
		sqlUtil.append(" AND task.handlerStatus in(?,?)");
		sqlUtil.addParamValue(HandlerStatus.START);
		sqlUtil.addParamValue(handleStatus);
		}else if(WorkStatus.BYBACK.equals(handleStatus)){// ????????????????????????
			sqlUtil.append(" AND task.handlerStatus =? and  task.workStatus=?");
			sqlUtil.addParamValue(HandlerStatus.INIT);
			sqlUtil.addParamValue(WorkStatus.BYBACK);
		}else{
			sqlUtil.append(" AND task.handlerStatus = ?");
			sqlUtil.addParamValue(handleStatus);
			
		}

		//???????????????????????????????????????????????????????????????????????????????????????
		List<SysCodeDictVo> sysCodeList = findPermitcompanyByUserCode(taskQueryVo.getUserCode());
		if (sysCodeList != null && sysCodeList.size() > 0){
			sqlUtil.append(" AND ( ");
			for(int i=0;i<sysCodeList.size();i++){
				String comCode = sysCodeList.get(i).getCodeCode();
				if(comCode.startsWith("00")){
					comCode = comCode.substring(0, 4);
				}else{
					comCode = comCode.substring(0, 2);
				}

				if (i == sysCodeList.size()-1){
					sqlUtil.append("main.comCode like ?");
				}else {
					sqlUtil.append("main.comCode like ? OR ");
				}
				sqlUtil.addParamValue((comCode.equals("0000")?"":comCode) + "%");
			}
			sqlUtil.append(" ) ");

		}else {//??????????????????????????????????????????????????????????????????
			if(StringUtils.isNotBlank(taskQueryVo.getComCode())){
				String comCode = taskQueryVo.getComCode();
				if(comCode.startsWith("00")){
					comCode = comCode.substring(0, 4);
				}else{
					comCode = comCode.substring(0, 2);
				}
				sqlUtil.append(" AND main.comCode like ? ");
				sqlUtil.addParamValue((comCode.equals("0000")?"":comCode) + "%");
			}
		}

		if(StringUtils.isNotBlank(taskQueryVo.getIntermCode())){
			sqlUtil.append("AND main.intermId =? ");
			sqlUtil.addParamValue(Long.parseLong(taskQueryVo.getIntermCode()));
		}
		
		sqlUtil.andEquals(taskQueryVo,"task","nodeCode");
		sqlUtil.andEquals(taskQueryVo,"task","subNodeCode");
		
		
		if(StringUtils.isNotBlank(taskQueryVo.getTaskNo())){
			sqlUtil.append(" AND task.registNo like ? ");
			 sqlUtil.addParamValue("%"+taskQueryVo.getTaskNo()+"%");
		}
		
		sqlUtil.andDate(taskQueryVo,"task","taskInTime");
		
		if(StringUtils.isNotBlank(taskQueryVo.getRegistNo())){
			sqlUtil.append(" AND exists  (select  1 from main.prpLAssessorFees fees where fees.registNo like ? ) ");
			sqlUtil.addParamValue("%"+taskQueryVo.getRegistNo()+"%");
		}
		
		// ??????
		sqlUtil.append(" Order By task.taskInTime DESC ");
		// ???????????????
        int start = taskQueryVo.getStart();
	    // ??????????????????
	    int length = taskQueryVo.getLength();
	  	String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		logger.debug("taskQueryVo.getNodeCode()========"+taskQueryVo.getNodeCode());
		logger.debug("taskQueryVo.getSubNodeCode()========"+taskQueryVo.getSubNodeCode());
		logger.debug("taskQrySql="+sql);
		logger.debug("ParamValues="+ArrayUtils.toString(values));
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
        
		// ????????????
		List<WfTaskQueryResultVo> resultVoList=new ArrayList<WfTaskQueryResultVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){

			WfTaskQueryResultVo resultVo=new WfTaskQueryResultVo();
			Object[] obj = page.getResult().get(i);
			
			/*PrpLWfTaskQuery wfTaskQuery = (PrpLWfTaskQuery)obj[0];
			Beans.copy().from(wfTaskQuery).to(resultVo);*/
			
			PrpLAssessorMain prplAssessorMain = (PrpLAssessorMain)obj[0];
			resultVo.setIntermCode(prplAssessorMain.getIntermcode());

			if(HandlerStatus.END.equals(handleStatus)||HandlerStatus.CANCEL.equals(handleStatus)){
				PrpLWfTaskOut prpLWfTaskOut = (PrpLWfTaskOut)obj[1];
				Beans.copy().from(prpLWfTaskOut).excludeNull().to(resultVo);
			}else{
				PrpLWfTaskIn prpLWfTaskIn = (PrpLWfTaskIn)obj[1];
				Beans.copy().from(prpLWfTaskIn).excludeNull().to(resultVo);
			}
		resultVoList.add(resultVo);
		}

		ResultPage<WfTaskQueryResultVo> resultPage = new ResultPage<WfTaskQueryResultVo> (start, length, page.getTotalCount(), resultVoList);
		return resultPage;

	}
	
	/* 
	 * @see ins.sino.claimcar.flow.service.WfTaskQueryService#findAssessorFeeVeriTaskQuery(ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo)
	 * @param taskQueryVo
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@Override
	public ResultPage<WfTaskQueryResultVo> findAssessorFeeVeriTaskQuery( PrpLWfTaskQueryVo taskQueryVo
		 ) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
//		 String nodeCode = taskQueryVo.getNodeCode();
			SqlJoinUtils sqlUtil=new SqlJoinUtils();
			// ????????????????????????
			String handleStatus = taskQueryVo.getHandleStatus();
			// ??????????????????????????????
			if(HandlerStatus.END.equals(handleStatus)||HandlerStatus.CANCEL.equals(handleStatus)){
				sqlUtil.append(" FROM PrpLAssessorMain main,PrpLWfTaskOut task ");
			} else{
				sqlUtil.append(" FROM PrpLAssessorMain main,PrpLWfTaskIn task ");
			}
			sqlUtil.append(" where 1=1 AND main.taskId=task.registNo");
			
		      //???????????????????????????
			if (HandlerStatus.INIT.equals(handleStatus)) {// ?????????????????????????????????
				sqlUtil.append(" AND task.handlerStatus =? and  task.workStatus!=?");
				sqlUtil.addParamValue(HandlerStatus.INIT);
				sqlUtil.addParamValue(WorkStatus.BYBACK);
				}else{
					sqlUtil.append(" AND task.handlerStatus = ? ");
					sqlUtil.addParamValue(handleStatus);
				}

			//???????????????????????????????????????????????????????????????????????????????????????
			List<SysCodeDictVo> sysCodeList = findPermitcompanyByUserCode(taskQueryVo.getUserCode());
			if (sysCodeList != null && sysCodeList.size() > 0){
				sqlUtil.append(" AND ( ");
				for(int i=0;i<sysCodeList.size();i++){
					String comCode = sysCodeList.get(i).getCodeCode();
					if(comCode.startsWith("00")){
						comCode = comCode.substring(0, 4);
					}else{
						comCode = comCode.substring(0, 2);
					}

					if (i == sysCodeList.size()-1){
						sqlUtil.append("main.comCode like ?");
					}else {
						sqlUtil.append("main.comCode like ? OR ");
					}
					sqlUtil.addParamValue(comCode+"%");
				}
				sqlUtil.append(" ) ");

			}else {//??????????????????????????????????????????????????????????????????
				if(StringUtils.isNotBlank(taskQueryVo.getComCode())){
					String comCode = taskQueryVo.getComCode();
					if(comCode.startsWith("00")){
						comCode = comCode.substring(0, 4);
					}else{
						comCode = comCode.substring(0, 2);
					}
					sqlUtil.append(" AND main.comCode like ? ");
					sqlUtil.addParamValue(comCode+"%");
				}
			}

			if(StringUtils.isNotBlank(taskQueryVo.getIntermCode())){
				sqlUtil.append("AND main.intermId=? ");
				sqlUtil.addParamValue(Long.parseLong(taskQueryVo.getIntermCode()));
			}
			
			sqlUtil.andEquals(taskQueryVo,"task","nodeCode");
			sqlUtil.andEquals(taskQueryVo,"task","subNodeCode");
			
			
			if(StringUtils.isNotBlank(taskQueryVo.getTaskNo())){
				sqlUtil.append(" AND task.registNo like ? ");
				 sqlUtil.addParamValue("%"+taskQueryVo.getTaskNo()+"%");
			}
			
			sqlUtil.andDate(taskQueryVo,"task","taskInTime");
			
			if(StringUtils.isNotBlank(taskQueryVo.getRegistNo())){
				sqlUtil.append(" AND exists  (select  1 from main.prpLAssessorFees fees where fees.registNo like ? ) ");
				sqlUtil.addParamValue("%"+taskQueryVo.getRegistNo()+"%");
			}
			
			// ??????
			sqlUtil.append(" Order By task.taskInTime DESC ");
			// ???????????????
	        int start = taskQueryVo.getStart();
		    // ??????????????????
		    int length = taskQueryVo.getLength();
		  	String sql = sqlUtil.getSql();
			Object[] values = sqlUtil.getParamValues();
			logger.debug("taskQueryVo.getNodeCode()========"+taskQueryVo.getNodeCode());
			logger.debug("taskQueryVo.getSubNodeCode()========"+taskQueryVo.getSubNodeCode());
			logger.debug("taskQrySql="+sql);
			logger.debug("ParamValues="+ArrayUtils.toString(values));
			Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
	        
			// ????????????
			List<WfTaskQueryResultVo> resultVoList=new ArrayList<WfTaskQueryResultVo>();
			for(int i = 0; i<page.getResult().size(); i++ ){

				WfTaskQueryResultVo resultVo=new WfTaskQueryResultVo();
				Object[] obj = page.getResult().get(i);

				/*PrpLWfTaskQuery wfTaskQuery = (PrpLWfTaskQuery)obj[0];
				Beans.copy().from(wfTaskQuery).to(resultVo);*/
				
				PrpLAssessorMain prplAssessorMain = (PrpLAssessorMain)obj[0];
				resultVo.setIntermCode(prplAssessorMain.getIntermcode());

				if(HandlerStatus.END.equals(handleStatus)){
					
					PrpLWfTaskOut prpLWfTaskIn = (PrpLWfTaskOut)obj[1];
					Beans.copy().from(prpLWfTaskIn).excludeNull().to(resultVo);
				}else{
					PrpLWfTaskIn prpLWfTaskOut = (PrpLWfTaskIn)obj[1];
					Beans.copy().from(prpLWfTaskOut).excludeNull().to(resultVo);
				}
			resultVoList.add(resultVo);
			}

			ResultPage<WfTaskQueryResultVo> resultPage = new ResultPage<WfTaskQueryResultVo> (start, length, page.getTotalCount(), resultVoList);
			return resultPage;
	}
    
	@Override
	public ResultPage<WfTaskQueryResultVo> findCheckFeeTaskQuery(PrpLWfTaskQueryVo taskQueryVo) throws IllegalAccessException,InvocationTargetException, NoSuchMethodException {
//		   String nodeCode = taskQueryVo.getNodeCode();
			SqlJoinUtils sqlUtil=new SqlJoinUtils();
			// ????????????????????????
			String handleStatus = taskQueryVo.getHandleStatus();
			// ??????????????????????????????
			if(HandlerStatus.END.equals(handleStatus)||HandlerStatus.CANCEL.equals(handleStatus)){
				sqlUtil.append(" FROM PrpLAcheckMain main,PrpLWfTaskOut task ");
			} else{
				sqlUtil.append(" FROM PrpLAcheckMain main,PrpLWfTaskIn task ");
			}
			
			sqlUtil.append(" where 1=1 AND main.taskId=task.registNo");
		      //???????????????????????????
			if(HandlerStatus.DOING.equals(handleStatus)){
			sqlUtil.append(" AND task.handlerStatus in(?,?)");
			sqlUtil.addParamValue(HandlerStatus.START);
			sqlUtil.addParamValue(handleStatus);
			}else if(WorkStatus.BYBACK.equals(handleStatus)){// ????????????????????????
				sqlUtil.append(" AND task.handlerStatus =? and  task.workStatus=?");
				sqlUtil.addParamValue(HandlerStatus.INIT);
				sqlUtil.addParamValue(WorkStatus.BYBACK);
			}else{
				sqlUtil.append(" AND task.handlerStatus = ?");
				sqlUtil.addParamValue(handleStatus);
				
			}
				  
			if(StringUtils.isNotBlank(taskQueryVo.getComCode())){
				String comCode = taskQueryVo.getComCode();
				if(comCode.startsWith("00")){
					comCode = comCode.substring(0, 4);
				}else{
					comCode = comCode.substring(0, 2);
				}
				sqlUtil.append(" AND main.comCode like ? ");
				sqlUtil.addParamValue((comCode.equals("0000")?"":comCode) + "%");
			}
			
				
			if(StringUtils.isNotBlank(taskQueryVo.getCheckCode())){
				sqlUtil.append("AND main.checkcode =? ");
				sqlUtil.addParamValue(taskQueryVo.getCheckCode());
			}
			
			sqlUtil.andEquals(taskQueryVo,"task","nodeCode");
			sqlUtil.andEquals(taskQueryVo,"task","subNodeCode");
			
			
			if(StringUtils.isNotBlank(taskQueryVo.getTaskNo())){
				sqlUtil.append(" AND task.registNo like ? ");
				 sqlUtil.addParamValue("%"+taskQueryVo.getTaskNo()+"%");
			}
			
			sqlUtil.andDate(taskQueryVo,"task","taskInTime");
			
			if(StringUtils.isNotBlank(taskQueryVo.getRegistNo())){
				sqlUtil.append(" AND exists  (select  1 from main.prpLCheckFees fees where fees.registNo like ? ) ");
				sqlUtil.addParamValue("%"+taskQueryVo.getRegistNo()+"%");
			}
			
			// ??????
			sqlUtil.append(" Order By task.taskInTime DESC ");
			// ???????????????
	        int start = taskQueryVo.getStart();
		    // ??????????????????
		    int length = taskQueryVo.getLength();
		  	String sql = sqlUtil.getSql();
			Object[] values = sqlUtil.getParamValues();
			logger.debug("taskQueryVo.getNodeCode()========"+taskQueryVo.getNodeCode());
			logger.debug("taskQueryVo.getSubNodeCode()========"+taskQueryVo.getSubNodeCode());
			logger.debug("taskQrySql="+sql);
			logger.debug("ParamValues="+ArrayUtils.toString(values));
			Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
	        
			// ????????????
			List<WfTaskQueryResultVo> resultVoList=new ArrayList<WfTaskQueryResultVo>();
			for(int i = 0; i<page.getResult().size(); i++ ){

				WfTaskQueryResultVo resultVo=new WfTaskQueryResultVo();
				Object[] obj = page.getResult().get(i);
				
				/*PrpLWfTaskQuery wfTaskQuery = (PrpLWfTaskQuery)obj[0];
				Beans.copy().from(wfTaskQuery).to(resultVo);*/
				
				PrpLAcheckMain prpLAcheckMain = (PrpLAcheckMain)obj[0];
				resultVo.setCheckCode(prpLAcheckMain.getCheckcode());

				if(HandlerStatus.END.equals(handleStatus)||HandlerStatus.CANCEL.equals(handleStatus)){
					PrpLWfTaskOut prpLWfTaskOut = (PrpLWfTaskOut)obj[1];
					Beans.copy().from(prpLWfTaskOut).excludeNull().to(resultVo);
				}else{
					PrpLWfTaskIn prpLWfTaskIn = (PrpLWfTaskIn)obj[1];
					Beans.copy().from(prpLWfTaskIn).excludeNull().to(resultVo);
				}
			resultVoList.add(resultVo);
			}

			ResultPage<WfTaskQueryResultVo> resultPage = new ResultPage<WfTaskQueryResultVo> (start, length, page.getTotalCount(), resultVoList);
			return resultPage;
	}

	@Override
	public ResultPage<WfTaskQueryResultVo> findCheckFeeVeriTaskQuery(PrpLWfTaskQueryVo taskQueryVo) throws IllegalAccessException,InvocationTargetException, NoSuchMethodException {
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		// ????????????????????????
		String handleStatus = taskQueryVo.getHandleStatus();
		// ??????????????????????????????
		if(HandlerStatus.END.equals(handleStatus)||HandlerStatus.CANCEL.equals(handleStatus)){
			sqlUtil.append(" FROM PrpLAcheckMain main,PrpLWfTaskOut task ");
		} else{
			sqlUtil.append(" FROM PrpLAcheckMain main,PrpLWfTaskIn task ");
		}
		sqlUtil.append(" where 1=1 AND main.taskId=task.registNo");
		
	      //???????????????????????????
		if (HandlerStatus.INIT.equals(handleStatus)) {// ?????????????????????????????????
			sqlUtil.append(" AND task.handlerStatus =? and  task.workStatus!=?");
			sqlUtil.addParamValue(HandlerStatus.INIT);
			sqlUtil.addParamValue(WorkStatus.BYBACK);
			}else{
				sqlUtil.append(" AND task.handlerStatus = ? ");
				sqlUtil.addParamValue(handleStatus);
			}
		
		if(StringUtils.isNotBlank(taskQueryVo.getComCode())){
			String comCode = taskQueryVo.getComCode();
			if(comCode.startsWith("00")){
				comCode = comCode.substring(0, 4);
			}else{
				comCode = comCode.substring(0, 2);
			}
			sqlUtil.append(" AND main.comCode like ? ");
			sqlUtil.addParamValue(comCode+"%");
		}
		
		if(StringUtils.isNotBlank(taskQueryVo.getCheckCode())){
			sqlUtil.append("AND main.checkcode=? ");
			sqlUtil.addParamValue(taskQueryVo.getCheckCode());
		}
		
		sqlUtil.andEquals(taskQueryVo,"task","nodeCode");
		sqlUtil.andEquals(taskQueryVo,"task","subNodeCode");
		
		
		if(StringUtils.isNotBlank(taskQueryVo.getTaskNo())){
			sqlUtil.append(" AND task.registNo like ? ");
			 sqlUtil.addParamValue("%"+taskQueryVo.getTaskNo()+"%");
		}
		
		sqlUtil.andDate(taskQueryVo,"task","taskInTime");
		
		if(StringUtils.isNotBlank(taskQueryVo.getRegistNo())){
			sqlUtil.append(" AND exists  (select  1 from main.prpLCheckFees fees where fees.registNo like ? ) ");
			sqlUtil.addParamValue("%"+taskQueryVo.getRegistNo()+"%");
		}
		
		// ??????
		sqlUtil.append(" Order By task.taskInTime DESC ");
		// ???????????????
        int start = taskQueryVo.getStart();
	    // ??????????????????
	    int length = taskQueryVo.getLength();
	  	String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		logger.debug("taskQueryVo.getNodeCode()========"+taskQueryVo.getNodeCode());
		logger.debug("taskQueryVo.getSubNodeCode()========"+taskQueryVo.getSubNodeCode());
		logger.debug("taskQrySql="+sql);
		logger.debug("ParamValues="+ArrayUtils.toString(values));
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
        
		// ????????????
		List<WfTaskQueryResultVo> resultVoList=new ArrayList<WfTaskQueryResultVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){

			WfTaskQueryResultVo resultVo=new WfTaskQueryResultVo();
			Object[] obj = page.getResult().get(i);

			/*PrpLWfTaskQuery wfTaskQuery = (PrpLWfTaskQuery)obj[0];
			Beans.copy().from(wfTaskQuery).to(resultVo);*/
			
			PrpLAcheckMain PrpLAcheckMain = (PrpLAcheckMain)obj[0];
			resultVo.setCheckCode(PrpLAcheckMain.getCheckcode());

			if(HandlerStatus.END.equals(handleStatus)){
				
				PrpLWfTaskOut prpLWfTaskIn = (PrpLWfTaskOut)obj[1];
				Beans.copy().from(prpLWfTaskIn).excludeNull().to(resultVo);
			}else{
				PrpLWfTaskIn prpLWfTaskOut = (PrpLWfTaskIn)obj[1];
				Beans.copy().from(prpLWfTaskOut).excludeNull().to(resultVo);
			}
		resultVoList.add(resultVo);
		}

		ResultPage<WfTaskQueryResultVo> resultPage = new ResultPage<WfTaskQueryResultVo> (start, length, page.getTotalCount(), resultVoList);
		return resultPage;
	}
	
	@Override
	public List<SaaUserPermitCompany> findSaaUserPermitCompanyByUserCode(String userCode) {
		// ????????????
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("userCode",userCode);
		queryRule.addEqual("comCode", CodeConstants.TOPCOM);
		List<SaaUserPermitCompany> permitCompanyList = databaseDao.findAll(SaaUserPermitCompany.class,queryRule);
		return permitCompanyList;
	}

/*	@Override
	public ResultPage<PrpLClaimVo> findCancelAppTaskQuery(
			PrpLWfTaskQueryVo taskQueryVo) throws Exception {
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		
		sqlUtil.append(" FROM PrpLClaim prpLClaim ");
		sqlUtil.append(" where  1=1  AND prpLClaim.validFlag !=?");
		sqlUtil.addParamValue("0");
		
		if(StringUtils.isNotBlank(taskQueryVo.getClaimNo())){
			sqlUtil.append(" and (prpLClaim.claimNo = ? ) ");
			sqlUtil.addParamValue(taskQueryVo.getClaimNo());
		}
		if(StringUtils.isNotBlank(taskQueryVo.getRegistNo())){
			sqlUtil.append(" and (prpLClaim.registNo = ? ) ");
			sqlUtil.addParamValue(taskQueryVo.getRegistNo());
		}
		if(StringUtils.isNotBlank(taskQueryVo.getPolicyNo())){
			sqlUtil.append(" and (prpLClaim.policyNo = ? ) ");
			sqlUtil.addParamValue(taskQueryVo.getPolicyNo());
		}
		if(StringUtils.isNotBlank(taskQueryVo.getLicenseNo())){
			sqlUtil.append(" and (prpLClaim.licenseNo = ? ) ");
			sqlUtil.addParamValue(taskQueryVo.getLicenseNo());
		}
		sqlUtil.append("  and (((not exists(select 1 from PrpLWfTaskIn  a where a.claimNo = prpLClaim.claimNo and a.subNodeCode=? )  and "
				+ "not exists(select 1 from PrpLWfTaskOut  b where b.claimNo = prpLClaim.claimNo and b.subNodeCode=? ) ) ");
		sqlUtil.addParamValue(FlowNode.CancelApp.toString());
		sqlUtil.addParamValue(FlowNode.CancelApp.toString());
		
		
		sqlUtil.append(" or ((exists(select 1 from PrpLWfTaskOut  d ,PrpLcancelTrace c where d.claimNo = prpLClaim.claimNo and prpLClaim.claimNo=c.claimNo"
				+ " and d.subNodeCode=? and c.flags=?  ) )and (not exists(select 1 from PrpLWfTaskIn  f where f.claimNo = prpLClaim.claimNo and f.subNodeCode=? ))  )))");
		sqlUtil.addParamValue(FlowNode.ReCanApp.toString());
		sqlUtil.addParamValue("4");
		sqlUtil.addParamValue(FlowNode.CancelApp.toString());
		sqlUtil.append(" and (prpLClaim.claimTime >= ? and prpLClaim.claimTime< ?) ");
		sqlUtil.addParamValue(taskQueryVo.getTaskInTimeStart());
		sqlUtil.addParamValue(taskQueryVo.getTaskInTimeEnd());
		
		
		//sqlUtil.andDate(taskQueryVo,"task","taskInTime");
		// ???????????????
        int start = taskQueryVo.getStart();
	    // ??????????????????
	    int length = taskQueryVo.getLength();
	  	String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		logger.debug("taskQueryVo.getNodeCode()========"+taskQueryVo.getNodeCode());
		logger.debug("taskQueryVo.getSubNodeCode()========"+taskQueryVo.getSubNodeCode());
		System.out.println("taskQrySql="+sql);
		System.out.println("ParamValues="+ArrayUtils.toString(values));
		List<PrpLClaim> los = databaseDao.findAllByHql(PrpLClaim.class,sql,values);
		Page<Object[]> pageq = databaseDao.findPageByHql(sql,start/length+1,length,values);
		List<Object[]> pageq1= databaseDao.findAllByHql(sql,values);
		Page<PrpLClaim> page = databaseDao.findPageByHql(PrpLClaim.class,sql,start/length+1,length,values);
		
		// ????????????
		List<PrpLClaimVo> resultVoList=new ArrayList<PrpLClaimVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){

			PrpLClaimVo resultVo=new PrpLClaimVo();
			Object[] obj = page.getResult().get(i);

			PrpLWfTaskQuery wfTaskQuery = (PrpLWfTaskQuery)obj[0];
			Beans.copy().from(wfTaskQuery).to(resultVo);
			
			PrpLClaim prpLClaim = (PrpLClaim)obj[0];
			Beans.copy().from(page.getResult().get(i)).excludeNull().to(resultVo);
			QueryRule queryRule = QueryRule.getInstance();
			queryRule.addEqual("policyNo",page.getResult().get(i).getPolicyNo());
			queryRule.addEqual("registNo",page.getResult().get(i).getRegistNo());
			PrpLCMain prpLCMain = databaseDao.findUnique(PrpLCMain.class, queryRule);
			resultVo.setInsuredName(prpLCMain.getInsuredName());
			resultVo.setStartDate(prpLCMain.getStartDate());
			resultVo.setEndDate(prpLCMain.getEndDate());
			resultVoList.add(resultVo);
		}

		ResultPage<PrpLClaimVo> resultPage = new ResultPage<PrpLClaimVo> (start, length, page.getTotalCount(), resultVoList);
		return resultPage;

	}

	@Override
	public ResultPage<PrpLClaimVo> findRecanAppTaskQuery(
			PrpLWfTaskQueryVo taskQueryVo) throws Exception {
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		// ??????????????????????????????
		sqlUtil.append(" FROM PrpLClaim prpLClaim ");
		sqlUtil.append(" where  1=1  AND prpLClaim.validFlag =?");
		sqlUtil.addParamValue("0");
		
		if(StringUtils.isNotBlank(taskQueryVo.getClaimNo())){
			sqlUtil.append(" and (prpLClaim.claimNo = ? ) ");
			sqlUtil.addParamValue(taskQueryVo.getClaimNo());
		}
		
		sqlUtil.append("  and (((not exists(select 1 from PrpLWfTaskIn  a where a.claimNo = prpLClaim.claimNo and a.subNodeCode=? )  and "
				+ "not exists(select 1 from PrpLWfTaskOut  b where b.claimNo = prpLClaim.claimNo and b.subNodeCode=? ) ) ");
		sqlUtil.addParamValue(FlowNode.ReCanApp.toString());
		sqlUtil.addParamValue(FlowNode.ReCanApp.toString());
		
		
		sqlUtil.append(" or ((exists(select 1 from PrpLWfTaskOut  d ,PrpLcancelTrace c where d.claimNo = prpLClaim.claimNo and prpLClaim.claimNo=c.claimNo"
				+ " and d.subNodeCode=? and c.flags =?  ) )and (not exists(select 1 from PrpLWfTaskIn  f where f.claimNo = prpLClaim.claimNo and f.subNodeCode=? ))  )))");
		sqlUtil.addParamValue(FlowNode.CancelApp.toString());
		sqlUtil.addParamValue("5");
		sqlUtil.addParamValue(FlowNode.ReCanApp.toString());
		sqlUtil.append(" and (prpLClaim.claimTime >= ? and prpLClaim.claimTime< ?) ");
		sqlUtil.addParamValue(taskQueryVo.getTaskInTimeStart());
		sqlUtil.addParamValue(taskQueryVo.getTaskInTimeEnd());
		// ???????????????
        int start = taskQueryVo.getStart();
	    // ??????????????????
	    int length = taskQueryVo.getLength();
	  	String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		System.out.println("taskQrySql="+sql);
		System.out.println("ParamValues="+ArrayUtils.toString(values));
		Page<PrpLClaim> page = databaseDao.findPageByHql(PrpLClaim.class,sql,start/length+1,length,values);
		// ????????????
		List<PrpLClaimVo> resultVoList=new ArrayList<PrpLClaimVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){
			PrpLClaimVo resultVo=new PrpLClaimVo();
			Beans.copy().from(page.getResult().get(i)).excludeNull().to(resultVo);
			QueryRule queryRule = QueryRule.getInstance();
			queryRule.addEqual("policyNo",page.getResult().get(i).getPolicyNo());
			queryRule.addEqual("registNo",page.getResult().get(i).getRegistNo());
			PrpLCMain prpLCMain = databaseDao.findUnique(PrpLCMain.class, queryRule);
			resultVo.setInsuredName(prpLCMain.getInsuredName());
			resultVo.setStartDate(prpLCMain.getStartDate());
			resultVo.setEndDate(prpLCMain.getEndDate());
			resultVoList.add(resultVo);
		}

		ResultPage<PrpLClaimVo> resultPage = new ResultPage<PrpLClaimVo> (start, length, page.getTotalCount(), resultVoList);
		return resultPage;
	}*/
	private List<String> findAllVerifyLVMapByUserCode(String userCode) {

		String sql = "select upperNode,nodeCode from PrpDNode n where nodeCode  LIKE  ? AND upperNode = ?";
		sql += "AND Exists (Select 1 from  Saa_UserGrade g where g.gradeId=n.gradeId and userCode=? )";
		List<Object[]> result = databaseDao.findTopBySql(sql,20,"%_LV%","VClaim",userCode);
		List<String> allVerifyLVList = new ArrayList<String>();
		for(Object[] objs:result){
			allVerifyLVList.add(objs[1].toString());
		}
		return allVerifyLVList;
		
	}

	@Override
	public List<SysCodeDictVo> findPrpDcompanyByUserCode(String comCode,String... comlevel) {
		// TODO Auto-generated method stub
		StringBuffer querySb = new StringBuffer();
		List<String> paramValues = new ArrayList<String>();
		List<SysCodeDictVo> codeDictVoList = new ArrayList<SysCodeDictVo>();
		querySb.append("SELECT comCode,comCName,sysAreaCode FROM ywuser.PrpDcompany where 1 = 1"); 
		if(StringUtils.isNotBlank(comCode)){
			querySb.append(" and comcode like ? ");
			paramValues.add(comCode+"%");
		}
		if(comlevel!=null && comlevel.length >0){
			querySb.append(" and comlevel in (");
			for(int i = 0 ;i<comlevel.length ;i++){
				if(i<1){
					querySb.append("?");
					paramValues.add(comlevel[i]);
				}else{
					querySb.append(",?");
					paramValues.add(comlevel[i]);
				}
			}
			querySb.append(" )");
		}
		
		List<Object[]> objList = databaseDao.findRangeBySql(querySb.toString(),0,1000,paramValues.toArray());
		logger.info("sql========="+querySb.toString());
		logger.info("sql========123="+paramValues.toArray());
		for(Object[] result:objList){
			SysCodeDictVo codeDictVo = new SysCodeDictVo();
			codeDictVo.setCodeCode((String)result[0]);
			codeDictVo.setCodeName((String)result[1]);
			codeDictVo.setSysAreaCode((String)result[2]);
			codeDictVoList.add(codeDictVo);
		}
		return codeDictVoList;
	}

	@Override
	public List<SysCodeDictVo> findPermitcompanyByUserCode(String userCode) {
		// TODO Auto-generated method stub
		StringBuffer querySb = new StringBuffer();
		List<String> paramValues = new ArrayList<String>();
		List<SysCodeDictVo> codeDictVoList = new ArrayList<SysCodeDictVo>();
		querySb.append("select a.comcode,b.usercode from saauser.saa_permitcompany a,saauser.saa_usergrade b  where a.usergradeid=b.id "); 
		if(StringUtils.isNotBlank(userCode)){
			querySb.append(" and b.usercode = ? ");
			paramValues.add(userCode);
		}
		
		List<Object[]> objList = databaseDao.findRangeBySql(querySb.toString(),0,1000,paramValues.toArray());
		logger.info("sql========="+querySb.toString());
		logger.info("sql========="+paramValues.toArray());
		for(Object[] result:objList){
			SysCodeDictVo codeDictVo = new SysCodeDictVo();
			codeDictVo.setCodeCode((String)result[0]);
			codeDictVo.setCodeName((String)result[1]);
			codeDictVoList.add(codeDictVo);
		}
		return codeDictVoList;
	}

	/**
	 * 
	 * ??????????????????
	 * @param registNo
	 * @return
	 * @modified:
	 * ???zhujunde(2017???6???6??? ??????4:26:02): <br>
	 */
   public List<WfTaskQueryResultVo> findPrpLWfTaskInByRegistNo(String registNo,List<String> nodeCodeList) {
        //????????????????????????
        QueryRule queryRule = QueryRule.getInstance();
        List<PrpLWfTaskIn> prpLWfTaskInList = new ArrayList<PrpLWfTaskIn>();
        List<WfTaskQueryResultVo> resultVoList=new ArrayList<WfTaskQueryResultVo>();
        queryRule.addEqual("registNo",registNo);
        queryRule.addIn("nodeCode",nodeCodeList);
        prpLWfTaskInList = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
        for(PrpLWfTaskIn po : prpLWfTaskInList){
            WfTaskQueryResultVo resultVo = new WfTaskQueryResultVo();
            Beans.copy().from(po).excludeNull().to(resultVo);
            resultVoList.add(resultVo);
        }
        return resultVoList;
    }
   
    private static BigDecimal NullToZero(BigDecimal strNum) {
        if(strNum==null){
            return new BigDecimal("0");
        }
        return strNum;
    }
    /**
     * ????????????????????????????????????????????????????????????taskVo
     */
    @Override
    public List<PrpLWfTaskVo> checkHandlerRegistTask(String policyNo){
    	SqlJoinUtils sqlUtil=new SqlJoinUtils();
    	//select * from prplwftaskin tin ,prplwfmain mm where tin.flowid = mm.flowid and tin.nodecode = 'Regis' and (mm.policyno = ? or mm.policynolink = ?)
    	
		sqlUtil.append(" select a.flowId,a.taskId,a.registNo,a.taskInUser,a.taskInTime from PrpLWfTaskIn a ");
		sqlUtil.append(" join PrpLWfMain b on a.flowId = b.flowId ");
		sqlUtil.append(" where a.nodeCode = ? ");
		sqlUtil.addParamValue("Regis");
		sqlUtil.append(" and (b.policyNo = ? or b.policyNoLink = ? ) ");
		sqlUtil.addParamValue(policyNo);
		sqlUtil.addParamValue(policyNo);
		
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		logger.info(sql);
		
		List<Object[]> resList = new ArrayList<Object[]>();
		try{
			resList = baseDaoService.getAllBySql(sql,values);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		List<PrpLWfTaskVo> PrpLWfTaskVos = new ArrayList<PrpLWfTaskVo>();
		if(resList!=null&&resList.size()>0){
			for(Object[] obj : resList){
				SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				PrpLWfTaskVo taskVo = new PrpLWfTaskVo();
				taskVo.setFlowId(obj[0].toString());
				taskVo.setTaskId(new BigDecimal(obj[1].toString()));
				taskVo.setRegistNo(obj[2].toString());
				taskVo.setTaskInUser(obj[3].toString());
				try{
					taskVo.setTaskInTime(date.parse(obj[4].toString()));
				}catch(ParseException e){
					e.printStackTrace();
				}
				PrpLWfTaskVos.add(taskVo);
			}
		}
		return PrpLWfTaskVos;
    }
    
    /**
     * 1????????????????????????????????????????????????????????????????????????
		2???????????????????????????????????????????????????2????????????????????????????????? ??????????????????????????????????????????
		3???????????????????????????????????????????????????2???????????????????????????????????????????????????????????????????????????
		4???????????????????????????????????????????????????2??????????????????????????????????????????????????????????????????
     */
    public String getSubCheckFlag(String registNo,String assignUser,String checkAreaCode){
    	//??????????????????0-???????????????1-??????????????????2-??????????????????3-????????????
    	String subCheckFlag = "";
    	PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
    	PrpdIntermMainVo prplIntermMainVo = managerService.findIntermByUserCode(assignUser);
    	if(StringUtils.isEmpty(checkAreaCode)){
    		PrpLScheduleTaskVo scheduleTaskVo = scheduleService.findScheduleTaskByOther(registNo,"1","1");
        	if(scheduleTaskVo!=null && scheduleTaskVo.getProvinceCityAreaCode()!=null){
        		checkAreaCode = scheduleTaskVo.getProvinceCityAreaCode();
        	}else{
        		checkAreaCode = registVo.getDamageAreaCode();
        	 	if(StringUtils.isBlank(registVo.getDamageAreaCode())){//????????????????????????0??????????????????
            		subCheckFlag = CodeConstants.subCheckFlag.normalCase;
            		return subCheckFlag;
        		}
        	}
    	}
    	String insuredComCode = registVo.getComCode().startsWith("00") ? registVo.getComCode().substring(0,4):registVo.getComCode().substring(0,2);
    	List<SysCodeDictVo> codeDictVoList = findPrpDcompanyByUserCode(insuredComCode, "2");
    	if(codeDictVoList==null || codeDictVoList.size()==0){
    		insuredComCode = insuredComCode.startsWith("00") ? insuredComCode+"0000" : insuredComCode+"000000";
    		codeDictVoList = findPrpDcompanyByUserCode(insuredComCode, null);
    	}
    	if(codeDictVoList!=null && codeDictVoList.size()>0){
    		String sysAreaCode = codeDictVoList.get(0).getSysAreaCode();
    		sysAreaCode = sysAreaCode.startsWith("4403") ? sysAreaCode.substring(0,4) : sysAreaCode.substring(0,2);
    		checkAreaCode = checkAreaCode.startsWith("4403") ? checkAreaCode.substring(0,4) : checkAreaCode.substring(0,2);
    		//?????????????????????????????????
    		if(sysAreaCode.equals(checkAreaCode)){
    			subCheckFlag = "0";
    		}else{
    			//????????????????????????????????????
    			checkAreaCode = "4403".equals(checkAreaCode) ? (checkAreaCode+"00") : (checkAreaCode+"0000");
    			List<SysAreaDictVo> areaVoList = areaDictService.findAreaCode(checkAreaCode);
    			if(areaVoList!=null && areaVoList.size()>0){
    				if(areaVoList.get(0).getComCode()!=null && 
    						StringUtils.isNotEmpty(areaVoList.get(0).getComCode())){//???????????????
    					//????????????????????????
    					if(prplIntermMainVo != null){
    						subCheckFlag = "2";
    					}else{
    						subCheckFlag = "1";
    					}
    				}else if(prplIntermMainVo != null){//???????????????
    					subCheckFlag = "3";
    				}
    			}else{
    				//???????????????????????????
    			}
    		}
    	}else{
    		//???????????????????????????
    		
    	}
    	
    	return subCheckFlag;
    }
    
    @Override
    public List<PrpLWfTaskVo> findTaskBySubNode(String subNode,String registNo){
    	QueryRule qrForIn = QueryRule.getInstance();
    	QueryRule qrForOut = QueryRule.getInstance();
    	List<PrpLWfTaskVo> resultList = new ArrayList<PrpLWfTaskVo>();
    	qrForIn.addEqual("registNo", registNo);
    	qrForIn.addEqual("subNodeCode", subNode);
//    	qrForIn.addIn("workStatus", "0","1","2");
    	List<PrpLWfTaskIn> taskInList = databaseDao.findAll(PrpLWfTaskIn.class, qrForIn);
    	if(taskInList!=null && taskInList.size()>0){
    		List<PrpLWfTaskVo> taskInVoList = Beans.copyDepth().from(taskInList).toList(PrpLWfTaskVo.class);
    		for(PrpLWfTaskVo vo:taskInVoList){
    			resultList.add(vo);
    		}
    	}
    	qrForOut.addEqual("registNo", registNo);
    	qrForOut.addEqual("subNodeCode", subNode);
    	qrForOut.addEqual("workStatus", "3");
    	List<PrpLWfTaskOut> taskOutList = databaseDao.findAll(PrpLWfTaskOut.class, qrForOut);
    	if(taskOutList!=null && taskOutList.size()>0){
    		List<PrpLWfTaskVo> taskOutVoList = Beans.copyDepth().from(taskOutList).toList(PrpLWfTaskVo.class);
    		for(PrpLWfTaskVo vo:taskOutVoList){
    			resultList.add(vo);
    		}
    	}
    	return resultList;
    }
    @Override
    public void updateTaskIn(PrpLWfTaskVo taskInVo) {
        wfTaskService.updateTaskIn(taskInVo);
    }

    @Override
    public void updateTaskOut(PrpLWfTaskVo taskInVo) {
        wfTaskService.updateTaskOut(taskInVo);
    }

	@Override
	public List<SysCodeDictVo> findAllPermitcompanyByUserCode(String userCode) {
		// TODO Auto-generated method stub
		StringBuffer querySb = new StringBuffer();
		List<String> paramValues = new ArrayList<String>();
		List<SysCodeDictVo> codeDictVoList = new ArrayList<SysCodeDictVo>();
		querySb.append("select b.comcode,b.usergradeid from saauser.saa_permitcompany b where b.usergradeid in(select a.id from saauser.saa_usergrade  a where a.usercode = ?) "); 
		paramValues.add(userCode);
		
		List<Object[]> objList = databaseDao.findRangeBySql(querySb.toString(),0,1000,paramValues.toArray());
		logger.info("sql========="+querySb.toString());
		logger.info("sql========="+paramValues.toArray());
		for(Object[] result:objList){
			SysCodeDictVo codeDictVo = new SysCodeDictVo();
			codeDictVo.setCodeCode((String)result[0]);
			codeDictVoList.add(codeDictVo);
		}
		return codeDictVoList;
	}

	@Override
	public String getAllPermitcompanyByUserCode(List<SysCodeDictVo> sysCodeList,String WebUserComCode) {
		// TODO Auto-generated method stub
		StringBuffer querySb = new StringBuffer();
		if(sysCodeList != null && sysCodeList.size() >0){
			String comCode=null;
			 for (SysCodeDictVo sysCodeDictVo : sysCodeList) {
				  comCode=sysCodeDictVo.getCodeCode();
				  if (comCode.startsWith("0002")) {
					  querySb.append(comCode.substring(0, 4)).append(",");
				   } else {
					  querySb.append(comCode.substring(0, 2)).append(",");
				 }
			}
		}else{
			querySb.append(WebUserComCode.substring(0, 2)).append(","); 
		}
		return querySb.toString();
	}

	
}
