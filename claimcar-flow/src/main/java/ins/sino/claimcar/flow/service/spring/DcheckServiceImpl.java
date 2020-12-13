package ins.sino.claimcar.flow.service.spring;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.commom.vo.StringUtils;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.po.PrpLWfTaskQuery;
import ins.sino.claimcar.flow.service.DcheckService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskQueryResultVo;
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path(value = "dcheckService")
public class DcheckServiceImpl implements DcheckService{
	private static Logger logger = LoggerFactory.getLogger(DcheckServiceImpl.class);

	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private BaseDaoService baseDaoService;
	@Autowired
	private CodeTranService codeTranService;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Override
	public List<WfTaskQueryResultVo> search(PrpLWfTaskQueryVo taskQueryVo, String comCode, int start,int length)throws Exception {
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" FROM PrpLWfTaskQuery task where 1=1 ");
		//标的车发动机号
		if(StringUtils.isNotBlank(taskQueryVo.getEngineNo())){
			sqlUtil.append(" and exists(select 1 from PrpLCItemCar i where task.registNo=i.registNo and task.licenseNo=i.licenseNo ");
			sqlUtil.append(" and i.engineNo like ? ");
			sqlUtil.addParamValue("%"+taskQueryVo.getEngineNo()+"%");
			sqlUtil.append(")");
		}
		//立案号
		String claimNo = taskQueryVo.getClaimNo();
		if(StringUtils.isNotBlank(claimNo)){
			sqlUtil.append(" AND exists(select 1 FROM PrpLClaim pc WHERE pc.flowId = task.flowId AND pc.claimNo like ?)");
			sqlUtil.addParamValue("%"+claimNo+"%");
		}
		
		
		//案件类型PrpLWfTaskIn PrpLWfTaskOut
		String checkType=taskQueryVo.getCheckType();
		if(StringUtils.isNotBlank(checkType)){  
			if("0001".equals(comCode.substring(0,4))){
				if("0".equals(checkType)){
					sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=? ) and kn.subCheckFlag=? ) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and kt.subCheckFlag=? and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )))");
					sqlUtil.append(")");
					sqlUtil.addParamValue(FlowNode.Check.name());
					sqlUtil.addParamValue(FlowNode.DLoss.name());
				    sqlUtil.addParamValue(FlowNode.PLoss.name());
					sqlUtil.addParamValue("2");
					sqlUtil.addParamValue(FlowNode.Check.name());
					sqlUtil.addParamValue(FlowNode.DLoss.name());
				    sqlUtil.addParamValue(FlowNode.PLoss.name());
					sqlUtil.addParamValue("2");
					sqlUtil.addParamValue("7");
					sqlUtil.addParamValue("8");
				}else if("1".equals(checkType)){
					sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=? ) and kn.subCheckFlag=? ) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and kt.subCheckFlag=? and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )))");
					sqlUtil.append(")");
					sqlUtil.addParamValue(FlowNode.Check.name());
					sqlUtil.addParamValue(FlowNode.DLoss.name());
				    sqlUtil.addParamValue(FlowNode.PLoss.name());
					sqlUtil.addParamValue("1");
					sqlUtil.addParamValue(FlowNode.Check.name());
					sqlUtil.addParamValue(FlowNode.DLoss.name());
				    sqlUtil.addParamValue(FlowNode.PLoss.name());
					sqlUtil.addParamValue("1");
					sqlUtil.addParamValue("7");
					sqlUtil.addParamValue("8");
					
				}
			}else if("0002".equals(comCode.substring(0,4))){
				if(comCode.endsWith("0000")){
					if("0".equals(checkType)){
						   sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=? ) and kn.subCheckFlag=? and (kn.assignCom like ? or task.comCodePly like ? )) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and kt.subCheckFlag= ? and (kt.assignCom like ? or task.comCodePly like ? ) and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )) )");
						   sqlUtil.append(")");
						   sqlUtil.addParamValue(FlowNode.Check.name());
						   sqlUtil.addParamValue(FlowNode.DLoss.name());
						   sqlUtil.addParamValue(FlowNode.PLoss.name());
						   sqlUtil.addParamValue("2");
						   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
						   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
						   sqlUtil.addParamValue(FlowNode.Check.name());
						   sqlUtil.addParamValue(FlowNode.DLoss.name());
						   sqlUtil.addParamValue(FlowNode.PLoss.name());
						   sqlUtil.addParamValue("2");
						   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
						   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
						   sqlUtil.addParamValue("7");
						   sqlUtil.addParamValue("8");
					}else if("1".equals(checkType)){
						   sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=? ) and kn.subCheckFlag=? and (kn.assignCom like ? or task.comCodePly like ? )) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and kt.subCheckFlag= ? and (kt.assignCom like ? or task.comCodePly like ? ) and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )))");
						   sqlUtil.append(")");
						   sqlUtil.addParamValue(FlowNode.Check.name());
						   sqlUtil.addParamValue(FlowNode.DLoss.name());
						   sqlUtil.addParamValue(FlowNode.PLoss.name());
						   sqlUtil.addParamValue("1");
						   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
						   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
						   sqlUtil.addParamValue(FlowNode.Check.name());
						   sqlUtil.addParamValue(FlowNode.DLoss.name());
						   sqlUtil.addParamValue(FlowNode.PLoss.name());
						   sqlUtil.addParamValue("1");
						   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
						   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
						   sqlUtil.addParamValue("7");
						   sqlUtil.addParamValue("8");
					}
				}else{
					if("0".equals(checkType)){
						sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=? ) and kn.subCheckFlag=? and (kn.assignCom like ? or task.comCodePly like ? )) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and kt.subCheckFlag= ? and (kt.assignCom like ? or task.comCodePly like ? ) and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )))");
						sqlUtil.append(")");
						sqlUtil.addParamValue(FlowNode.Check.name());
						sqlUtil.addParamValue(FlowNode.DLoss.name());
					    sqlUtil.addParamValue(FlowNode.PLoss.name());
						sqlUtil.addParamValue("2");
						sqlUtil.addParamValue(comCode.substring(0, 6)+"%");
						sqlUtil.addParamValue(comCode.substring(0, 6)+"%");
						sqlUtil.addParamValue(FlowNode.Check.name());
						sqlUtil.addParamValue(FlowNode.DLoss.name());
					    sqlUtil.addParamValue(FlowNode.PLoss.name());
						sqlUtil.addParamValue("2");
						sqlUtil.addParamValue(comCode.substring(0, 6)+"%");
						sqlUtil.addParamValue(comCode.substring(0, 6)+"%");
						sqlUtil.addParamValue("7");
						sqlUtil.addParamValue("8");
					}else if("1".equals(checkType)){
						sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=? ) and kn.subCheckFlag=? and (kn.assignCom like ? or task.comCodePly like ? )) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and kt.subCheckFlag= ? and (kt.assignCom like ? or task.comCodePly like ? ) and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )) )");
						sqlUtil.append(")");
						sqlUtil.addParamValue(FlowNode.Check.name());
						sqlUtil.addParamValue(FlowNode.DLoss.name());
					    sqlUtil.addParamValue(FlowNode.PLoss.name());
					    sqlUtil.addParamValue("1");
						sqlUtil.addParamValue(comCode.substring(0, 6)+"%");
						sqlUtil.addParamValue(comCode.substring(0, 6)+"%");
						sqlUtil.addParamValue(FlowNode.Check.name());
						sqlUtil.addParamValue(FlowNode.DLoss.name());
					    sqlUtil.addParamValue(FlowNode.PLoss.name());
						sqlUtil.addParamValue("1");
						sqlUtil.addParamValue(comCode.substring(0, 6)+"%");
						sqlUtil.addParamValue(comCode.substring(0, 6)+"%");
						sqlUtil.addParamValue("7");
						sqlUtil.addParamValue("8");
					}
				}
			}else if(comCode.endsWith("000000")){
				if("0".equals(checkType)){
					sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=? ) and kn.subCheckFlag=? and (kn.assignCom like ? or task.comCodePly like ? )) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and kt.subCheckFlag= ? and (kt.assignCom like ? or task.comCodePly like ? ) and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )) )");
					sqlUtil.append(")");
					sqlUtil.addParamValue(FlowNode.Check.name());
					sqlUtil.addParamValue(FlowNode.DLoss.name());
				    sqlUtil.addParamValue(FlowNode.PLoss.name());
					sqlUtil.addParamValue("2");
					sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
					sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
					sqlUtil.addParamValue(FlowNode.Check.name());
					sqlUtil.addParamValue(FlowNode.DLoss.name());
				    sqlUtil.addParamValue(FlowNode.PLoss.name());
					sqlUtil.addParamValue("2");
					sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
					sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
					sqlUtil.addParamValue("7");
					sqlUtil.addParamValue("8");
				}else if("1".equals(checkType)){
					sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=? ) and kn.subCheckFlag=? and (kn.assignCom like ? or task.comCodePly like ? )) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and kt.subCheckFlag= ? and (kt.assignCom like ? or task.comCodePly like ? ) and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )) )");
					sqlUtil.append(")");
					sqlUtil.addParamValue(FlowNode.Check.name());
					sqlUtil.addParamValue(FlowNode.DLoss.name());
				    sqlUtil.addParamValue(FlowNode.PLoss.name());
					sqlUtil.addParamValue("1");
					sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
					sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
					sqlUtil.addParamValue(FlowNode.Check.name());
					sqlUtil.addParamValue(FlowNode.DLoss.name());
				    sqlUtil.addParamValue(FlowNode.PLoss.name());
					sqlUtil.addParamValue("1");
					sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
					sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
					sqlUtil.addParamValue("7");
					sqlUtil.addParamValue("8");
				}
			}else{
				if("0".equals(checkType)){
					sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=? ) and kn.subCheckFlag=? and (kn.assignCom like ? or task.comCodePly like ? )) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and kt.subCheckFlag= ? and (kt.assignCom like ? or task.comCodePly like ? ) and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )) )");
					sqlUtil.append(")");
					sqlUtil.addParamValue(FlowNode.Check.name());
					sqlUtil.addParamValue(FlowNode.DLoss.name());
				    sqlUtil.addParamValue(FlowNode.PLoss.name());
				    sqlUtil.addParamValue("2");
				    sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
				    sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
				    sqlUtil.addParamValue(FlowNode.Check.name());
					sqlUtil.addParamValue(FlowNode.DLoss.name());
				    sqlUtil.addParamValue(FlowNode.PLoss.name());
				    sqlUtil.addParamValue("2");
			 	    sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
				    sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
				    sqlUtil.addParamValue("7");
					sqlUtil.addParamValue("8");
				}else if("1".equals(checkType)){
					sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=? ) and kn.subCheckFlag=? and (kn.assignCom like ? or task.comCodePly like ? )) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and kt.subCheckFlag= ? and (kt.assignCom like ? or task.comCodePly like ? ) and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )))");
					sqlUtil.append(")");
					sqlUtil.addParamValue(FlowNode.Check.name());
					sqlUtil.addParamValue(FlowNode.DLoss.name());
				    sqlUtil.addParamValue(FlowNode.PLoss.name());
					sqlUtil.addParamValue("1");
					sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
					sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
					sqlUtil.addParamValue(FlowNode.Check.name());
					sqlUtil.addParamValue(FlowNode.DLoss.name());
				    sqlUtil.addParamValue(FlowNode.PLoss.name());
					sqlUtil.addParamValue("1");
					sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
					sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
					sqlUtil.addParamValue("7");
					sqlUtil.addParamValue("8");
				}
			}
			
		}else{
		 if("0001".equals(comCode.substring(0,4))){
			 sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=? ) and (kn.subCheckFlag=? or kn.subCheckFlag=?)) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and (kt.subCheckFlag=? or kt.subCheckFlag=? )  and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )))");
			 sqlUtil.append(")");
			 sqlUtil.addParamValue(FlowNode.Check.name());
			 sqlUtil.addParamValue(FlowNode.DLoss.name());
			 sqlUtil.addParamValue(FlowNode.PLoss.name());
			 sqlUtil.addParamValue("1");
			 sqlUtil.addParamValue("2");
			 sqlUtil.addParamValue(FlowNode.Check.name());
			 sqlUtil.addParamValue(FlowNode.DLoss.name());
			 sqlUtil.addParamValue(FlowNode.PLoss.name());
			 sqlUtil.addParamValue("1");
			 sqlUtil.addParamValue("2");
			 sqlUtil.addParamValue("7");
			 sqlUtil.addParamValue("8");
		   }else if("0002".equals(comCode.substring(0,4))){
			   if(comCode.endsWith("0000")){
				   sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=? ) and (kn.subCheckFlag=? or kn.subCheckFlag=?) and (kn.assignCom like ? or task.comCodePly like ? )) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and (kt.subCheckFlag=? or kt.subCheckFlag=? ) and (kt.assignCom like ? or task.comCodePly like ? ) and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )) )");
				   sqlUtil.append(")");
				   sqlUtil.addParamValue(FlowNode.Check.name());
				   sqlUtil.addParamValue(FlowNode.DLoss.name());
				   sqlUtil.addParamValue(FlowNode.PLoss.name());
				   sqlUtil.addParamValue("1");
				   sqlUtil.addParamValue("2");
				   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
				   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
				   sqlUtil.addParamValue(FlowNode.Check.name());
				   sqlUtil.addParamValue(FlowNode.DLoss.name());
				   sqlUtil.addParamValue(FlowNode.PLoss.name());
				   sqlUtil.addParamValue("1");
				   sqlUtil.addParamValue("2");
				   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
				   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
				   sqlUtil.addParamValue("7");
				   sqlUtil.addParamValue("8");
			   }else{
				   sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=? ) and (kn.subCheckFlag=? or kn.subCheckFlag=?) and (kn.assignCom like ? or task.comCodePly like ? )) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and (kt.subCheckFlag=? or kt.subCheckFlag=? ) and (kt.assignCom like ? or task.comCodePly like ? ) and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )) )");
				   sqlUtil.append(")");
				   sqlUtil.addParamValue(FlowNode.Check.name());
				   sqlUtil.addParamValue(FlowNode.DLoss.name());
				   sqlUtil.addParamValue(FlowNode.PLoss.name());
				   sqlUtil.addParamValue("1");
				   sqlUtil.addParamValue("2");
				   sqlUtil.addParamValue(comCode.substring(0, 6)+"%");
				   sqlUtil.addParamValue(comCode.substring(0, 6)+"%");
				   sqlUtil.addParamValue("Check");
				   sqlUtil.addParamValue("DLoss");
				   sqlUtil.addParamValue("PLoss");
				   sqlUtil.addParamValue("1");
				   sqlUtil.addParamValue("2");
				   sqlUtil.addParamValue(comCode.substring(0, 6)+"%");
				   sqlUtil.addParamValue(comCode.substring(0, 6)+"%");
				   sqlUtil.addParamValue("7");
				   sqlUtil.addParamValue("8");
			   }
		   }else if(comCode.endsWith("000000")){
			   sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=? ) and (kn.subCheckFlag=? or kn.subCheckFlag=?) and (kn.assignCom like ? or task.comCodePly like ? )) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and (kt.subCheckFlag=? or kt.subCheckFlag=? ) and (kt.assignCom like ? or task.comCodePly like ? ) and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )))");
			   sqlUtil.append(")");
			   sqlUtil.addParamValue(FlowNode.Check.name());
			   sqlUtil.addParamValue(FlowNode.DLoss.name());
			   sqlUtil.addParamValue(FlowNode.PLoss.name());
			   sqlUtil.addParamValue("1");
			   sqlUtil.addParamValue("2");
			   sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
			   sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
			   sqlUtil.addParamValue(FlowNode.Check.name());
			   sqlUtil.addParamValue(FlowNode.DLoss.name());
			   sqlUtil.addParamValue(FlowNode.PLoss.name());
			   sqlUtil.addParamValue("1");
			   sqlUtil.addParamValue("2");
			   sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
			   sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
			   sqlUtil.addParamValue("7");
			   sqlUtil.addParamValue("8");
		   }else{
			   sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=? ) and (kn.subCheckFlag=? or kn.subCheckFlag=?) and (kn.assignCom like ? or task.comCodePly like ? )) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and (kt.subCheckFlag=? or kt.subCheckFlag=? ) and (kt.assignCom like ? or task.comCodePly like ? )  and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )) )");
			   sqlUtil.append(")");
			   sqlUtil.addParamValue(FlowNode.Check.name());
			   sqlUtil.addParamValue(FlowNode.DLoss.name());
			   sqlUtil.addParamValue(FlowNode.PLoss.name());
			   sqlUtil.addParamValue("1");
			   sqlUtil.addParamValue("2");
			   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
			   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
			   sqlUtil.addParamValue(FlowNode.Check.name());
			   sqlUtil.addParamValue(FlowNode.DLoss.name());
			   sqlUtil.addParamValue(FlowNode.PLoss.name());
			   sqlUtil.addParamValue("1");
			   sqlUtil.addParamValue("2");
			   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
			   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
			   sqlUtil.addParamValue("7");
			   sqlUtil.addParamValue("8");
		   }
		}
		
		
		//保单号
		// "policyNo" 特殊条件控制
		String policyNo = taskQueryVo.getPolicyNo();
		if(StringUtils.isNotBlank(policyNo) && policyNo.length()>2){
			String policyNoRev = StringUtils.reverse(policyNo.toString()).trim();
			sqlUtil.append(" AND (task.policyNoRev LIKE ? Or task.policyNoLink LIKE ? ) ");
			sqlUtil.addParamValue(policyNoRev+"%");
			sqlUtil.addParamValue("%"+policyNo+"%");
		}
		String  table_query="task";
		sqlUtil.andReverse(taskQueryVo,table_query,7,"registNo");
		sqlUtil.andReverse(taskQueryVo,table_query,6,"frameNo");
		sqlUtil.andLike2(taskQueryVo,table_query,"insuredName","licenseNo");
		//日期条件
		sqlUtil.andDate(taskQueryVo,table_query,"reportTime","damageTime");
		//增加归属机构查询
		if(StringUtils.isNotBlank(taskQueryVo.getComCode())){
			if("0002".equals(taskQueryVo.getComCode().substring(0, 4))){//深圳
				if(taskQueryVo.getComCode().endsWith("0000")){//分公司
					sqlUtil.append(" AND task.comCodePly LIKE ?");
					sqlUtil.addParamValue(taskQueryVo.getComCode().substring(0, 4)+"%");
				}else{//支公司
					sqlUtil.append(" AND task.comCodePly LIKE ?");
					sqlUtil.addParamValue(taskQueryVo.getComCode().substring(0, 6)+"%");
				}
			}else{
				sqlUtil.andComSql(table_query,"comCodePly",taskQueryVo.getComCode());
			}
		}
		//排序
		sqlUtil.append(" ORDER BY task.reportTime DESC");
		String sql=sqlUtil.getSql();
		logger.info("查询SQL="+sql);
//		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,sqlUtil.getParamValues());
//		
//		//对象转换s
//		List<WfTaskQueryResultVo> resultVoList=new ArrayList<WfTaskQueryResultVo>();
//		for(int i=0;i<page.getResult().size();i++){
//			WfTaskQueryResultVo resultVo=new WfTaskQueryResultVo();
//			Object obj=page.getResult().get(i);
//			PrpLWfTaskQuery taskQuery = (PrpLWfTaskQuery)obj;
//			taskQuery=(PrpLWfTaskQuery)obj;
//			Beans.copy().from(taskQuery).to(resultVo);
//			resultVoList.add(resultVo);
//		}
//		ResultPage<WfTaskQueryResultVo> resultPage = new ResultPage<WfTaskQueryResultVo> (start, length, page.getTotalCount(), resultVoList);
//		return resultPage;
		
		List<PrpLWfTaskQuery> resultVoList = databaseDao.findAllByHql(PrpLWfTaskQuery.class, sql, sqlUtil.getParamValues());
		List<WfTaskQueryResultVo> queryResultVoList = new ArrayList<WfTaskQueryResultVo>();
		if(resultVoList != null && resultVoList.size() > 0){
            for(PrpLWfTaskQuery obj : resultVoList){
            	WfTaskQueryResultVo resultVo=new WfTaskQueryResultVo();
            	Beans.copy().from(obj).to(resultVo);
            	queryResultVoList.add(resultVo);
            }
			
			
		}
		return queryResultVoList;
	}
	
	
	@Override
	public List<WfTaskQueryResultVo> dcheckDatas(PrpLWfTaskQueryVo taskQueryVo, String comCode)throws Exception {
			SqlJoinUtils sqlUtil=new SqlJoinUtils();
	sqlUtil.append("select * FROM PrpLWfTaskQuery task where 1=1 ");
	//标的车发动机号
	if(StringUtils.isNotBlank(taskQueryVo.getEngineNo())){
		sqlUtil.append(" and exists(select 1 from PrpLCItemCar i where task.registNo=i.registNo and task.licenseNo=i.licenseNo ");
		sqlUtil.append(" and i.engineNo like ? ");
		sqlUtil.addParamValue("%"+taskQueryVo.getEngineNo()+"%");
		sqlUtil.append(")");
	}
	//立案号
	String claimNo = taskQueryVo.getClaimNo();
	if(StringUtils.isNotBlank(claimNo)){
		sqlUtil.append(" AND exists(select 1 FROM PrpLClaim pc WHERE pc.flowId = task.flowId AND pc.claimNo like ?)");
		sqlUtil.addParamValue("%"+claimNo+"%");
	}
	
	
	//案件类型PrpLWfTaskIn PrpLWfTaskOut
	String checkType=taskQueryVo.getCheckType();
	if(StringUtils.isNotBlank(checkType)){  
		if("0001".equals(comCode.substring(0,4))){
			if("0".equals(checkType)){
				sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=?) and kn.subCheckFlag=? ) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and kt.subCheckFlag=? and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )))");
				sqlUtil.append(")");
				sqlUtil.addParamValue(FlowNode.Check.name());
				sqlUtil.addParamValue(FlowNode.DLoss.name());
			    sqlUtil.addParamValue(FlowNode.PLoss.name());
				sqlUtil.addParamValue("2");
				sqlUtil.addParamValue(FlowNode.Check.name());
				sqlUtil.addParamValue(FlowNode.DLoss.name());
			    sqlUtil.addParamValue(FlowNode.PLoss.name());
				sqlUtil.addParamValue("2");
				sqlUtil.addParamValue("7");
				sqlUtil.addParamValue("8");
			}else if("1".equals(checkType)){
				sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=?) and kn.subCheckFlag=? ) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and kt.subCheckFlag=? and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )))");
				sqlUtil.append(")");
				sqlUtil.addParamValue(FlowNode.Check.name());
				sqlUtil.addParamValue(FlowNode.DLoss.name());
			    sqlUtil.addParamValue(FlowNode.PLoss.name());
				sqlUtil.addParamValue("1");
				sqlUtil.addParamValue(FlowNode.Check.name());
				sqlUtil.addParamValue(FlowNode.DLoss.name());
			    sqlUtil.addParamValue(FlowNode.PLoss.name());
				sqlUtil.addParamValue("1");
				sqlUtil.addParamValue("7");
				sqlUtil.addParamValue("8");
				
			}
		}else if("0002".equals(comCode.substring(0,4))){
			if(comCode.endsWith("0000")){
				if("0".equals(checkType)){
					   sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=? ) and kn.subCheckFlag=? and (kn.assignCom like ? or task.comCodePly like ? )) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and kt.subCheckFlag= ? and (kt.assignCom like ? or task.comCodePly like ? ) and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )) )");
					   sqlUtil.append(")");
					   sqlUtil.addParamValue(FlowNode.Check.name());
					   sqlUtil.addParamValue(FlowNode.DLoss.name());
					   sqlUtil.addParamValue(FlowNode.PLoss.name());
					   sqlUtil.addParamValue("2");
					   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
					   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
					   sqlUtil.addParamValue(FlowNode.Check.name());
					   sqlUtil.addParamValue(FlowNode.DLoss.name());
					   sqlUtil.addParamValue(FlowNode.PLoss.name());
					   sqlUtil.addParamValue("2");
					   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
					   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
					   sqlUtil.addParamValue("7");
					   sqlUtil.addParamValue("8");
				}else if("1".equals(checkType)){
					   sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=? ) and kn.subCheckFlag=? and (kn.assignCom like ? or task.comCodePly like ? )) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and kt.subCheckFlag= ? and (kt.assignCom like ? or task.comCodePly like ? ) and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )))");
					   sqlUtil.append(")");
					   sqlUtil.addParamValue(FlowNode.Check.name());
					   sqlUtil.addParamValue(FlowNode.DLoss.name());
					   sqlUtil.addParamValue(FlowNode.PLoss.name());
					   sqlUtil.addParamValue("1");
					   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
					   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
					   sqlUtil.addParamValue(FlowNode.Check.name());
					   sqlUtil.addParamValue(FlowNode.DLoss.name());
					   sqlUtil.addParamValue(FlowNode.PLoss.name());
					   sqlUtil.addParamValue("1");
					   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
					   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
					   sqlUtil.addParamValue("7");
					   sqlUtil.addParamValue("8");
				}
			}else{
				if("0".equals(checkType)){
					sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=?) and kn.subCheckFlag=? and (kn.assignCom like ? or task.comCodePly like ? )) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and kt.subCheckFlag= ? and (kt.assignCom like ? or task.comCodePly like ? ) and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )))");
					sqlUtil.append(")");
					sqlUtil.addParamValue(FlowNode.Check.name());
					sqlUtil.addParamValue(FlowNode.DLoss.name());
				    sqlUtil.addParamValue(FlowNode.PLoss.name());
					sqlUtil.addParamValue("2");
					sqlUtil.addParamValue(comCode.substring(0, 6)+"%");
					sqlUtil.addParamValue(comCode.substring(0, 6)+"%");
					sqlUtil.addParamValue(FlowNode.Check.name());
					sqlUtil.addParamValue(FlowNode.DLoss.name());
				    sqlUtil.addParamValue(FlowNode.PLoss.name());
					sqlUtil.addParamValue("2");
					sqlUtil.addParamValue(comCode.substring(0, 6)+"%");
					sqlUtil.addParamValue(comCode.substring(0, 6)+"%");
					sqlUtil.addParamValue("7");
					sqlUtil.addParamValue("8");
				}else if("1".equals(checkType)){
					sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=?) and kn.subCheckFlag=? and (kn.assignCom like ? or task.comCodePly like ? )) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and kt.subCheckFlag= ? and (kt.assignCom like ? or task.comCodePly like ? ) and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )) )");
					sqlUtil.append(")");
					sqlUtil.addParamValue(FlowNode.Check.name());
					sqlUtil.addParamValue(FlowNode.DLoss.name());
				    sqlUtil.addParamValue(FlowNode.PLoss.name());
				    sqlUtil.addParamValue("1");
					sqlUtil.addParamValue(comCode.substring(0, 6)+"%");
					sqlUtil.addParamValue(comCode.substring(0, 6)+"%");
					sqlUtil.addParamValue(FlowNode.Check.name());
					sqlUtil.addParamValue(FlowNode.DLoss.name());
				    sqlUtil.addParamValue(FlowNode.PLoss.name());
					sqlUtil.addParamValue("1");
					sqlUtil.addParamValue(comCode.substring(0, 6)+"%");
					sqlUtil.addParamValue(comCode.substring(0, 6)+"%");
					sqlUtil.addParamValue("7");
					sqlUtil.addParamValue("8");
				}
			}
		}else if(comCode.endsWith("000000")){
			if("0".equals(checkType)){
				sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=?) and kn.subCheckFlag=? and (kn.assignCom like ? or task.comCodePly like ? )) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and kt.subCheckFlag= ? and (kt.assignCom like ? or task.comCodePly like ? ) and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )) )");
				sqlUtil.append(")");
				sqlUtil.addParamValue(FlowNode.Check.name());
				sqlUtil.addParamValue(FlowNode.DLoss.name());
			    sqlUtil.addParamValue(FlowNode.PLoss.name());
				sqlUtil.addParamValue("2");
				sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
				sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
				sqlUtil.addParamValue(FlowNode.Check.name());
				sqlUtil.addParamValue(FlowNode.DLoss.name());
			    sqlUtil.addParamValue(FlowNode.PLoss.name());
				sqlUtil.addParamValue("2");
				sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
				sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
				sqlUtil.addParamValue("7");
				sqlUtil.addParamValue("8");
			}else if("1".equals(checkType)){
				sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=?) and kn.subCheckFlag=? and (kn.assignCom like ? or task.comCodePly like ? )) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and kt.subCheckFlag= ? and (kt.assignCom like ? or task.comCodePly like ? ) and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )) )");
				sqlUtil.append(")");
				sqlUtil.addParamValue(FlowNode.Check.name());
				sqlUtil.addParamValue(FlowNode.DLoss.name());
			    sqlUtil.addParamValue(FlowNode.PLoss.name());
				sqlUtil.addParamValue("1");
				sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
				sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
				sqlUtil.addParamValue(FlowNode.Check.name());
				sqlUtil.addParamValue(FlowNode.DLoss.name());
			    sqlUtil.addParamValue(FlowNode.PLoss.name());
				sqlUtil.addParamValue("1");
				sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
				sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
				sqlUtil.addParamValue("7");
				sqlUtil.addParamValue("8");
			}
		}else{
			if("0".equals(checkType)){
				sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=?) and kn.subCheckFlag=? and (kn.assignCom like ? or task.comCodePly like ? )) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and kt.subCheckFlag= ? and (kt.assignCom like ? or task.comCodePly like ? ) and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )) )");
				sqlUtil.append(")");
				sqlUtil.addParamValue(FlowNode.Check.name());
				sqlUtil.addParamValue(FlowNode.DLoss.name());
			    sqlUtil.addParamValue(FlowNode.PLoss.name());
			    sqlUtil.addParamValue("2");
			    sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
			    sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
			    sqlUtil.addParamValue(FlowNode.Check.name());
				sqlUtil.addParamValue(FlowNode.DLoss.name());
			    sqlUtil.addParamValue(FlowNode.PLoss.name());
			    sqlUtil.addParamValue("2");
		 	    sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
			    sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
			    sqlUtil.addParamValue("7");
				sqlUtil.addParamValue("8");
			}else if("1".equals(checkType)){
				sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=? ) and kn.subCheckFlag=? and (kn.assignCom like ? or task.comCodePly like ? )) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and kt.subCheckFlag= ? and (kt.assignCom like ? or task.comCodePly like ? ) and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )))");
				sqlUtil.append(")");
				sqlUtil.addParamValue(FlowNode.Check.name());
				sqlUtil.addParamValue(FlowNode.DLoss.name());
			    sqlUtil.addParamValue(FlowNode.PLoss.name());
				sqlUtil.addParamValue("1");
				sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
				sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
				sqlUtil.addParamValue(FlowNode.Check.name());
				sqlUtil.addParamValue(FlowNode.DLoss.name());
			    sqlUtil.addParamValue(FlowNode.PLoss.name());
				sqlUtil.addParamValue("1");
				sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
				sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
				sqlUtil.addParamValue("7");
				sqlUtil.addParamValue("8");
			}
		}
		
	}else{
	 if("0001".equals(comCode.substring(0,4))){
		 sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=? ) and (kn.subCheckFlag=? or kn.subCheckFlag=?)) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and (kt.subCheckFlag=? or kt.subCheckFlag=? )  and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )))");
		 sqlUtil.append(")");
		 sqlUtil.addParamValue(FlowNode.Check.name());
		 sqlUtil.addParamValue(FlowNode.DLoss.name());
		 sqlUtil.addParamValue(FlowNode.PLoss.name());
		 sqlUtil.addParamValue("1");
		 sqlUtil.addParamValue("2");
		 sqlUtil.addParamValue(FlowNode.Check.name());
		 sqlUtil.addParamValue(FlowNode.DLoss.name());
		 sqlUtil.addParamValue(FlowNode.PLoss.name());
		 sqlUtil.addParamValue("1");
		 sqlUtil.addParamValue("2");
		 sqlUtil.addParamValue("7");
		 sqlUtil.addParamValue("8");
	   }else if("0002".equals(comCode.substring(0,4))){
		   if(comCode.endsWith("0000")){
			   sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=? ) and (kn.subCheckFlag=? or kn.subCheckFlag=?) and (kn.assignCom like ? or task.comCodePly like ? )) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and (kt.subCheckFlag=? or kt.subCheckFlag=? ) and (kt.assignCom like ? or task.comCodePly like ? ) and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )) )");
			   sqlUtil.append(")");
			   sqlUtil.addParamValue(FlowNode.Check.name());
			   sqlUtil.addParamValue(FlowNode.DLoss.name());
			   sqlUtil.addParamValue(FlowNode.PLoss.name());
			   sqlUtil.addParamValue("1");
			   sqlUtil.addParamValue("2");
			   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
			   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
			   sqlUtil.addParamValue(FlowNode.Check.name());
			   sqlUtil.addParamValue(FlowNode.DLoss.name());
			   sqlUtil.addParamValue(FlowNode.PLoss.name());
			   sqlUtil.addParamValue("1");
			   sqlUtil.addParamValue("2");
			   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
			   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
			   sqlUtil.addParamValue("7");
			   sqlUtil.addParamValue("8");
		   }else{
			   sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=? ) and (kn.subCheckFlag=? or kn.subCheckFlag=?) and (kn.assignCom like ? or task.comCodePly like ? )) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and (kt.subCheckFlag=? or kt.subCheckFlag=? ) and (kt.assignCom like ? or task.comCodePly like ? ) and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )) )");
			   sqlUtil.append(")");
			   sqlUtil.addParamValue(FlowNode.Check.name());
			   sqlUtil.addParamValue(FlowNode.DLoss.name());
			   sqlUtil.addParamValue(FlowNode.PLoss.name());
			   sqlUtil.addParamValue("1");
			   sqlUtil.addParamValue("2");
			   sqlUtil.addParamValue(comCode.substring(0, 6)+"%");
			   sqlUtil.addParamValue(comCode.substring(0, 6)+"%");
			   sqlUtil.addParamValue("Check");
			   sqlUtil.addParamValue("DLoss");
			   sqlUtil.addParamValue("PLoss");
			   sqlUtil.addParamValue("1");
			   sqlUtil.addParamValue("2");
			   sqlUtil.addParamValue(comCode.substring(0, 6)+"%");
			   sqlUtil.addParamValue(comCode.substring(0, 6)+"%");
			   sqlUtil.addParamValue("7");
			   sqlUtil.addParamValue("8");
		   }
	   }else if(comCode.endsWith("000000")){
		   sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=? ) and (kn.subCheckFlag=? or kn.subCheckFlag=?) and (kn.assignCom like ? or task.comCodePly like ? )) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and (kt.subCheckFlag=? or kt.subCheckFlag=? ) and (kt.assignCom like ? or task.comCodePly like ? ) and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )))");
		   sqlUtil.append(")");
		   sqlUtil.addParamValue(FlowNode.Check.name());
		   sqlUtil.addParamValue(FlowNode.DLoss.name());
		   sqlUtil.addParamValue(FlowNode.PLoss.name());
		   sqlUtil.addParamValue("1");
		   sqlUtil.addParamValue("2");
		   sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
		   sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
		   sqlUtil.addParamValue(FlowNode.Check.name());
		   sqlUtil.addParamValue(FlowNode.DLoss.name());
		   sqlUtil.addParamValue(FlowNode.PLoss.name());
		   sqlUtil.addParamValue("1");
		   sqlUtil.addParamValue("2");
		   sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
		   sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
		   sqlUtil.addParamValue("7");
		   sqlUtil.addParamValue("8");
	   }else{
		   sqlUtil.append(" AND (exists(select 1 FROM PrpLWfTaskIn kn where kn.flowId=task.flowId and (kn.nodeCode=? or kn.nodeCode=? or kn.nodeCode=? ) and (kn.subCheckFlag=? or kn.subCheckFlag=?) and (kn.assignCom like ? or task.comCodePly like ? )) or exists(select 1 FROM PrpLWfTaskOut kt where kt.flowId=task.flowId and (kt.nodeCode=? or kt.nodeCode=? or kt.nodeCode=?) and (kt.subCheckFlag=? or kt.subCheckFlag=? ) and (kt.assignCom like ? or task.comCodePly like ? )  and not exists(select 1 from PrpLWfTaskOut out where out.taskId=kt.taskId and (out.workStatus=? or out.workStatus=? )) )");
		   sqlUtil.append(")");
		   sqlUtil.addParamValue(FlowNode.Check.name());
		   sqlUtil.addParamValue(FlowNode.DLoss.name());
		   sqlUtil.addParamValue(FlowNode.PLoss.name());
		   sqlUtil.addParamValue("1");
		   sqlUtil.addParamValue("2");
		   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
		   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
		   sqlUtil.addParamValue(FlowNode.Check.name());
		   sqlUtil.addParamValue(FlowNode.DLoss.name());
		   sqlUtil.addParamValue(FlowNode.PLoss.name());
		   sqlUtil.addParamValue("1");
		   sqlUtil.addParamValue("2");
		   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
		   sqlUtil.addParamValue(comCode.substring(0, 4)+"%");
		   sqlUtil.addParamValue("7");
		   sqlUtil.addParamValue("8");
	   }
	}
	//保单号
	// "policyNo" 特殊条件控制
	String policyNo = taskQueryVo.getPolicyNo();
	if(StringUtils.isNotBlank(policyNo) && policyNo.length()>2){
		String policyNoRev = StringUtils.reverse(policyNo.toString()).trim();
		sqlUtil.append(" AND (task.policyNoRev LIKE ? Or task.policyNoLink LIKE ? ) ");
		sqlUtil.addParamValue(policyNoRev+"%");
		sqlUtil.addParamValue("%"+policyNo+"%");
	}
	String  table_query="task";
	sqlUtil.andReverse(taskQueryVo,table_query,7,"registNo");
	sqlUtil.andReverse(taskQueryVo,table_query,6,"frameNo");
	sqlUtil.andLike2(taskQueryVo,table_query,"insuredName","licenseNo");
	//日期条件
	sqlUtil.andDate(taskQueryVo,table_query,"reportTime","damageTime");
	//增加归属机构查询
	if(StringUtils.isNotBlank(taskQueryVo.getComCode())){
		if("0002".equals(taskQueryVo.getComCode().substring(0, 4))){//深圳
			if(taskQueryVo.getComCode().endsWith("0000")){//分公司
				sqlUtil.append(" AND task.comCodePly LIKE ?");
				sqlUtil.addParamValue(taskQueryVo.getComCode().substring(0, 4)+"%");
			}else{//支公司
				sqlUtil.append(" AND task.comCodePly LIKE ?");
				sqlUtil.addParamValue(taskQueryVo.getComCode().substring(0, 6)+"%");
			}
		}else{
			sqlUtil.andComSql(table_query,"comCodePly",taskQueryVo.getComCode());
		}
	}
	//排序
	sqlUtil.append(" ORDER BY task.reportTime DESC");
	String sql=sqlUtil.getSql();
	Object[] values=sqlUtil.getParamValues();
	logger.info("查询SQL="+sql);
	List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
	
	
	//对象转换s
	List<WfTaskQueryResultVo> resultVoList=new ArrayList<WfTaskQueryResultVo>();
	for(int i=0;i<objects.size();i++){
		Object[] obj = objects.get(i);
		WfTaskQueryResultVo resultVo=new WfTaskQueryResultVo();
		resultVo.setFlowId(obj[0]==null ? "" : obj[0].toString());
		resultVo.setRegistNo(obj[1]==null ? "" : obj[1].toString());
		resultVo.setPolicyNo(obj[3]==null ? "" : obj[3].toString());
		resultVo.setPolicyNoLink(obj[4]==null ? "" : obj[4].toString());
		resultVo.setInsuredName(obj[13]==null ? "" : obj[13].toString());
		resultVo.setLicenseNo(obj[9]==null ? "" : obj[9].toString());
		resultVo.setDamageTime(obj[19]==null ? null : (Date)obj[19]);
		resultVo.setReportTime(obj[17]==null ? null : (Date)obj[17]);
		resultVo.setDamageAddress(obj[28]==null ? "" : obj[28].toString());
		resultVo.setComCodePly(obj[25]==null ? "" : obj[25].toString());
		resultVoList.add(resultVo);
	}
	
	return resultVoList;
	}
	@Override
	public List<Map<String, Object>> createExcelRecord(List<WfTaskQueryResultVo> resultList) throws Exception{
		List<Map<String, Object>> mapList=new ArrayList<Map<String, Object>>();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("sheetName","sheet1");
		mapList.add(map);
		if(resultList!=null && resultList.size()>0){
			for(WfTaskQueryResultVo resultVo: resultList){
				Map<String,Object> mapvalue=new HashMap<String,Object>();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		        String reportdate = format.format(resultVo.getReportTime());
		        String damagedate = format.format(resultVo.getDamageTime());
				mapvalue.put("registNo",resultVo.getRegistNo());
				mapvalue.put("policyNo",resultVo.getPolicyNo()+(StringUtils.isNotBlank(resultVo.getPolicyNoLink())? ","+resultVo.getPolicyNoLink():""));
				mapvalue.put("insuredName",resultVo.getInsuredName());
				mapvalue.put("licenseNo",resultVo.getLicenseNo());
				mapvalue.put("damageTime",damagedate);
				mapvalue.put("reportTime",reportdate);
				mapvalue.put("damageAddress",resultVo.getDamageAddress());
				mapvalue.put("comCodePly",codeTranService.transCode("ComCode",resultVo.getComCodePly()));
				mapvalue.put("handlerCom",translateComCode(resultVo.getFlowId()));
				mapList.add(mapvalue);
			}
		}
		
		return mapList;
	}
	
	/**
	 * 翻译代查勘的
	 * @param flowId
	 * @return
	 */
	private String translateComCode(String flowId){
		List<String> nodeCodes=new ArrayList<String>();
		nodeCodes.add(FlowNode.Check.name());
		nodeCodes.add(FlowNode.DLoss.name());
		nodeCodes.add(FlowNode.PLoss.name());
		//查询in表
		List<PrpLWfTaskVo> taskInVos=wfTaskHandleService.findPrpLWfTaskByFlowIdAndnodeCode(flowId,"1",nodeCodes);
		//查询out表
		List<PrpLWfTaskVo> taskOutVos=wfTaskHandleService.findPrpLWfTaskByFlowIdAndnodeCode(flowId,"2",nodeCodes);
		List<PrpLWfTaskVo> taskOutVos2=new ArrayList<PrpLWfTaskVo>();
		if(taskOutVos!=null && taskOutVos.size()>0){
			for(PrpLWfTaskVo vo:taskOutVos){
				if(!"8".equals(vo.getWorkStatus()) && !"7".equals(vo.getWorkStatus())){
					taskOutVos2.add(vo);
				}
			}
		}
		List<String> comCodeList=new ArrayList<String>();
		if(taskInVos!=null && taskInVos.size()>0){
			for(PrpLWfTaskVo inVo:taskInVos){
				if(StringUtils.isNotBlank(inVo.getHandlerCom())){
					comCodeList.add(inVo.getHandlerCom().substring(0,6)+"00");
				}
			}
		}
		
		if(taskOutVos2!=null && taskOutVos2.size()>0){
			for(PrpLWfTaskVo outVo:taskOutVos2){
				if(StringUtils.isNotBlank(outVo.getHandlerCom())){
					comCodeList.add(outVo.getHandlerCom().substring(0,6)+"00");
				}
			}
		}
		String comCodeStr="";
		int index=0;
		Set<String> comset=new HashSet<String>(comCodeList);
		if(comset!=null && comset.size()>0){
			for(String str:comset){
				index++;//为了去掉最后一个逗号
				if(index==comset.size()){
					comCodeStr=comCodeStr+codeTranService.transCode("ComCode",str);
				}else{
					comCodeStr=comCodeStr+codeTranService.transCode("ComCode",str)+",";
				}
				
			}
		}
		return comCodeStr;
	}

}
