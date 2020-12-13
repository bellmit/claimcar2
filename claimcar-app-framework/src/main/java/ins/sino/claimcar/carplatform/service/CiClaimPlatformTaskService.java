package ins.sino.claimcar.carplatform.service;

import java.util.ArrayList;
import java.util.List;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.saa.util.CodeConstants;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.carplatform.po.CiClaimPlatformLog;
import ins.sino.claimcar.carplatform.po.CiClaimPlatformTask;
import ins.sino.claimcar.carplatform.po.PrpDReUploadConfig;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformTaskVo;
import ins.sino.claimcar.carplatform.vo.PrpDReUploadConfigVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "ciClaimPlatformTaskService")
@Transactional(noRollbackFor = RuntimeException.class)
public class CiClaimPlatformTaskService {

	private static Logger logger = LoggerFactory.getLogger(CiClaimPlatformTaskService.class);
	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private BaseDaoService baseDaoService;
	
	public CiClaimPlatformTaskVo findPlatformTaskByPK(Long id){
		if(id == null)return null;
		CiClaimPlatformTaskVo resultVo = null;
		CiClaimPlatformTask po = databaseDao.findByPK(CiClaimPlatformTask.class,id);
		if(po != null){
			resultVo = new CiClaimPlatformTaskVo();
			resultVo = Beans.copyDepth().from(po).to(CiClaimPlatformTaskVo.class);
		}
		return resultVo;
	}
	
	public void savePlatformTaskList(List<CiClaimPlatformTaskVo> platformTaskList){
		if(platformTaskList!=null && platformTaskList.size()>0){
			List<CiClaimPlatformTask> poList = new ArrayList<CiClaimPlatformTask>();
			databaseDao.saveAll(CiClaimPlatformTask.class,poList);
		}
	}
	
	public void savePlatformTask(CiClaimPlatformTaskVo platformTaskVo){
		CiClaimPlatformTask po = new CiClaimPlatformTask();
		po = Beans.copyDepth().from(platformTaskVo).to(CiClaimPlatformTask.class);
		databaseDao.save(CiClaimPlatformTask.class,po);
	}
	
	public void updatePlatformTask(CiClaimPlatformTaskVo platformTaskVo){
		CiClaimPlatformTask po = databaseDao.findByPK(CiClaimPlatformTask.class, platformTaskVo.getId());
		Beans.copy().from(platformTaskVo).to(po);
		databaseDao.update(CiClaimPlatformTask.class,po);
	}
	
	
	/**
	 * 查询小于最大执行次数，上传失败，任务状态为空闲的理算编码，去重
	 * @return
	 */
	public List<String> findClaimSeqNoList(){
		List<String> claimSeqNoList = new ArrayList<String>();
		String maxTimes = SpringProperties.getProperty("PLATFORM_TIMES");	//最大执行次数
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append("SELECT DISTINCT a.claimseqno FROM  ciclaimplatformtask a WHERE 1=1 ");
		sqlUtil.append(" AND a.status in (?,?) ");
		sqlUtil.addParamValue(CodeConstants.platformStatus.Failure);
		sqlUtil.addParamValue(CodeConstants.platformStatus.None);
		sqlUtil.append(" AND a.operateStatus=? ");
		sqlUtil.append(CodeConstants.OperateStatus.OFF);
		sqlUtil.append(" AND a.redoTimes<?");
		sqlUtil.addParamValue(Integer.valueOf(maxTimes));
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
		
		for(int i = 0; i<objects.size(); i++ ){
			Object[] obj = objects.get(i);
			claimSeqNoList.add(obj[0].toString());
		}
		return claimSeqNoList;
	}
	
	/**
	 * 正在上传平台的数据的状态为置为ON
	 * @param claimSeqNoList
	 */
	/*public void taskTurnOn(List<String> claimSeqNoList){
		String maxTimes = SpringProperties.getProperty("PLATFORM_TIMES");	//最大执行次数
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("UPATE ciclaimplatformtask a SET a.operateStatus='").append(CodeConstants.OperateStatus.ON);
		sqlBuffer.append("' where a.operateStatus='").append(CodeConstants.OperateStatus.OFF);
		sqlBuffer.append("' and a.status='").append(CodeConstants.platformStatus.Failure);
		sqlBuffer.append("' and a.redoTimes<").append(maxTimes);
		sqlBuffer.append("' and a.claimSeqNo in(");
		for(int i=0;i<claimSeqNoList.size();i++){
			if((i+1)==claimSeqNoList.size()){
				sqlBuffer.append("'").append(claimSeqNoList.get(i)).append("')");
			}else{
				sqlBuffer.append("'").append(claimSeqNoList.get(i)).append("',");
			}
		}
		baseDaoService.executeSQL(sqlBuffer.toString());
	}*/
	
	/**
	 * 正在上传平台的数据的状态为置为ON
	 */
	public void updateOperateStatus(List<CiClaimPlatformTaskVo> platformTaskVoList,String operateStatus){
		if(platformTaskVoList!=null && platformTaskVoList.size()>0){
			for(CiClaimPlatformTaskVo platformTaskVo:platformTaskVoList){
				this.updateOperateStatus(platformTaskVo,operateStatus);
			}
		}
	}
	
	public void updateOperateStatus(CiClaimPlatformTaskVo platformTaskVo,String operateStatus){
		platformTaskVo.setOperateStatus(operateStatus);
		CiClaimPlatformTask platformTask = new CiClaimPlatformTask();
		Beans.copy().from(platformTaskVo).to(platformTask);
		databaseDao.update(CiClaimPlatformTask.class,platformTask);
	}
	
	/**
	 * 查询小于最大执行次数，上传失败，任务状态为空闲的平台数据
	 * @return
	 */
	public List<CiClaimPlatformTaskVo> findPlatformTaskList(){
		String maxTimes = SpringProperties.getProperty(CodeConstants.PLATFORM_TIMES);	//最大执行次数
		String count = SpringProperties.getProperty(CodeConstants.PLATFORM_COUNT);	//自动上传平台执行的最大案件数量
		List<CiClaimPlatformTaskVo> resultList = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("operateStatus",CodeConstants.OperateStatus.OFF);
		queryRule.addIn("status",CodeConstants.platformStatus.Failure,CodeConstants.platformStatus.None);
		queryRule.addLessThan("redoTimes",Integer.valueOf(maxTimes));
		queryRule.addSql(" rownum <= "+count);
//		queryRule.addAscOrder("claimSeqNo");
		queryRule.addAscOrder("taskLevel");
		queryRule.addDescOrder("id");
		
		List<CiClaimPlatformTask> platformTaskList = databaseDao.findAll(CiClaimPlatformTask.class,queryRule);
		if(platformTaskList!=null && platformTaskList.size()>0){
			resultList = new ArrayList<CiClaimPlatformTaskVo>();
			resultList = Beans.copyDepth().from(platformTaskList).toList(CiClaimPlatformTaskVo.class);
		}
		return resultList;
	}
	
	/**
	 * 根据ciClaimPlatformTask的ID和status查询CiClaimPlatformLog表
	 * @param logId
	 * @return
	 */
	public CiClaimPlatformLogVo findLogByTaskId(Long taskId){
		CiClaimPlatformLogVo platformLogVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("taskId",taskId);
		queryRule.addDescOrder("createTime");
		List<CiClaimPlatformLog> platformLogList = databaseDao.findAll(CiClaimPlatformLog.class,queryRule);
		if(platformLogList!=null && platformLogList.size()>0){
			platformLogVo = new CiClaimPlatformLogVo();
			Beans.copy().from(platformLogList.get(0)).to(platformLogVo);
		}
		
		return platformLogVo;
	}
	
	/**
	 * 根据requestType查询PrpDReUploadConfig
	 * @param requestType
	 * @return
	 */
	public PrpDReUploadConfigVo findPrpDReUploadConfig(String requestType){
		PrpDReUploadConfigVo resultVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("requestType",requestType);
		queryRule.addEqual("validStatus",CodeConstants.CommonConst.TRUE);
		List<PrpDReUploadConfig> poList = databaseDao.findAll(PrpDReUploadConfig.class,queryRule);
		if(poList!=null && poList.size()>0){
			resultVo = new PrpDReUploadConfigVo();
			Beans.copy().from(poList.get(0)).to(resultVo);
		}
		return resultVo;
	}
	
	
}
