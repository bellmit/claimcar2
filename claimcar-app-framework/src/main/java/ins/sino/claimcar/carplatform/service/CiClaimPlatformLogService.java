/******************************************************************************
 * CREATETIME : 2016年6月24日 下午3:23:49
 ******************************************************************************/
package ins.sino.claimcar.carplatform.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.saa.util.CodeConstants;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.po.CiClaimPlatformLog;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年6月24日
 */
@Service(value = "ciClaimPlatformLogService")
@Transactional(noRollbackFor = RuntimeException.class)
public class CiClaimPlatformLogService {

	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private BaseDaoService baseDaoService;
	
	public CiClaimPlatformLogVo save(CiClaimPlatformLogVo platformLogVo) {
		platformLogVo.setCreateTime(new Date());
		CiClaimPlatformLog platLogPo = new CiClaimPlatformLog();
		Beans.copy().from(platformLogVo).to(platLogPo);
		databaseDao.save(CiClaimPlatformLog.class,platLogPo);
		platformLogVo.setId(platLogPo.getId());
		return platformLogVo;
	}
	
	/**
	 * 平台交互查询，唯一的记录
	 * @param logId
	 * @return CiClaimPlatformLogVo
	 */
	public CiClaimPlatformLogVo findLogById(Long logId) {
		CiClaimPlatformLogVo platLogVo = null;
		CiClaimPlatformLog platLogPo = null;
		platLogPo = databaseDao.findByPK(CiClaimPlatformLog.class,logId);
		if(platLogPo!=null){
			platLogVo = new CiClaimPlatformLogVo();
			Beans.copy().from(platLogPo).to(platLogVo);
		}
		return platLogVo;
	}
	
	/**
	 * 查询案件的上传log记录,查询报案上传成功的Log记录
	 * @param bussNo（不能为空）
	 * @param reqType（不能为空）
	 * @param comCode 保单机构代码（区别全国和上海平台）
	 * @param status=1(查询出报案上传平台成功的一条唯一数据)
	 * @return CiClaimPlatformLogVo
	 */
	public CiClaimPlatformLogVo findLogByBussNo(String reqType,String bussNo,String comCode){
		if(StringUtils.isEmpty(reqType)||StringUtils.isEmpty(bussNo))return null;
		CiClaimPlatformLogVo platLogVo = null;
		// 查询旧理赔
		// 旧理赔
		// 查询lcmain表
		SqlJoinUtils sqlUtil1 = new SqlJoinUtils();
		sqlUtil1.append("SELECT registNo from PrpLRegist WHERE 1=1 ");
		sqlUtil1.append("AND registNo=");
		sqlUtil1.append("'"+bussNo+"' ");
		sqlUtil1.append("AND flag=");
		sqlUtil1.append("'oldClaim' ");
		String sql1 = sqlUtil1.getSql();
		Object[] values1 = sqlUtil1.getParamValues();
		// 执行查询
		List<Object[]> objects1 = baseDaoService.getAllBySql(sql1,values1);
		if(objects1.size() > 0){
			// 查询lcmain表
			SqlJoinUtils sqlUtil = new SqlJoinUtils();
			sqlUtil.append("SELECT CLAIMSEQUENCENO from PrplCMain WHERE 1=1 ");
			sqlUtil.append("AND registNo=");
			sqlUtil.append("'"+bussNo+"' ");
			if(RequestType.RegistInfoCI_SH.getCode().equals(reqType) ||
RequestType.RegistInfoCI.getCode().equals(reqType)||RequestType.ClaimInfoCI_SH
					.getCode().equals(reqType)||RequestType.ClaimCI.getCode().equals(reqType)||RequestType.LossInfoCI_SH.getCode().equals(reqType)||RequestType.LossInfoCI
					.getCode().equals(reqType)||RequestType.CertifyCI_SH.getCode().equals(reqType)||RequestType.CertifyCI.getCode().equals(reqType)){// 交强
				sqlUtil.append("AND riskCode like");
				sqlUtil.append("'11%'");
			}else{
				sqlUtil.append("AND riskCode like");
				sqlUtil.append("'12%'");
			}
			String sql = sqlUtil.getSql();
			Object[] values = sqlUtil.getParamValues();
			// 执行查询
			List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
			if(objects.size() > 0){
				Object claimSeqNo = objects.get(0);
				platLogVo = new CiClaimPlatformLogVo();
				platLogVo.setClaimSeqNo((String)claimSeqNo);
			}
		}else{
			QueryRule queryRule = QueryRule.getInstance();
			queryRule.addEqual("requestType", reqType);
			queryRule.addEqual("bussNo", bussNo);
			queryRule.addEqual("comCode", comCode);
			queryRule.addEqual("status","1");
			queryRule.addDescOrder("createTime");
			
			List<CiClaimPlatformLog> logPoList = 
			databaseDao.findAll(CiClaimPlatformLog.class,queryRule);
			if(logPoList!=null&&logPoList.size()>0){
				platLogVo = new CiClaimPlatformLogVo();
				platLogVo = Beans.copyDepth().from(logPoList.get(0))
						.to(CiClaimPlatformLogVo.class);
			}
		}
		
		return platLogVo;
	}
	
	/**
	 * 根据请求类型，案件号，状态位精确查询平台日志表，flag=1则需要查询旧理赔
	 * @param reqType
	 * @param bussNo
	 * @param status
	 * @param flag
	 * @return
	 */
	public CiClaimPlatformLogVo findPlatformLog(String reqType,String bussNo,String status,String flag){
		
		if(StringUtils.isEmpty(reqType)||StringUtils.isEmpty(bussNo))return null;
		CiClaimPlatformLogVo platLogVo = null;
		
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("requestType", reqType);
		queryRule.addEqual("bussNo", bussNo);
		queryRule.addEqual("status",status);
		queryRule.addDescOrder("createTime");
		List<CiClaimPlatformLog> logPoList = databaseDao.findAll(CiClaimPlatformLog.class,queryRule);
		if(logPoList!=null&&logPoList.size()>0){
			platLogVo = new CiClaimPlatformLogVo();
			platLogVo = Beans.copyDepth().from(logPoList.get(0)).to(CiClaimPlatformLogVo.class);
		}else if("1".equals(flag)){
			SqlJoinUtils sqlUtil = new SqlJoinUtils();
			sqlUtil.append("select CLAIMCODE from ywuser.ciclaimuploadlog a  ");
			sqlUtil.append("where a.businessno=? and a.uploadtype=? and  a.flag=? ");
			sqlUtil.addParamValue(bussNo);
			sqlUtil.addParamValue(reqType);
			sqlUtil.addParamValue(status);
			String sql = sqlUtil.getSql();
			Object[] values = sqlUtil.getParamValues();
			// 执行查询
			List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
			if(objects!=null && objects.size() > 0){
				Object claimSeqNo = objects.get(0);
				platLogVo = new CiClaimPlatformLogVo();
				platLogVo.setClaimSeqNo((String)claimSeqNo);
			}
		}
		
		return platLogVo;
	}
	
	/**
	 * 查询案件的上传log记录,查询报案上传成功的Log记录
	 * @param bussNo（不能为空）
	 * @param reqType（不能为空）
	 * @param comCode 保单机构代码（区别全国和上海平台）
	 * @param status=1(查询出报案上传平台成功的一条唯一数据)
	 * @return CiClaimPlatformLogVo
	 */
	public CiClaimPlatformLogVo findLogByBussNoAndType(String reqType,String bussNo,String comCode){
		if(StringUtils.isEmpty(reqType)||StringUtils.isEmpty(bussNo))return null;
		CiClaimPlatformLogVo platLogVo = null;
		// 查询旧理赔
		// 旧理赔
		// 查询lcmain表
		SqlJoinUtils sqlUtil1 = new SqlJoinUtils();
		sqlUtil1.append("SELECT registNo from PrpLRegist WHERE 1=1 ");
		sqlUtil1.append("AND registNo=");
		sqlUtil1.append("'"+bussNo+"' ");
		sqlUtil1.append("AND flag=");
		sqlUtil1.append("'oldClaim' ");
		String sql1 = sqlUtil1.getSql();
		Object[] values1 = sqlUtil1.getParamValues();
		// 执行查询
		List<Object[]> objects1 = baseDaoService.getAllBySql(sql1,values1);
		if(objects1.size() > 0){
			// 查询lcmain表
			SqlJoinUtils sqlUtil = new SqlJoinUtils();
			sqlUtil.append("SELECT CLAIMSEQUENCENO from PrplCMain WHERE 1=1 ");
			sqlUtil.append("AND registNo=");
			sqlUtil.append("'"+bussNo+"' ");
			if(RequestType.RegistInfoCI_SH.getCode().equals(reqType) ||
RequestType.RegistInfoCI.getCode().equals(reqType)){// 交强
				sqlUtil.append("AND riskCode like");
				sqlUtil.append("'11%'");
			}else{
				sqlUtil.append("AND riskCode like");
				sqlUtil.append("'12%'");
			}
			String sql = sqlUtil.getSql();
			Object[] values = sqlUtil.getParamValues();
			// 执行查询
			List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
			if(objects.size() > 0){
				Object claimSeqNo = objects.get(0);
				platLogVo = new CiClaimPlatformLogVo();
				platLogVo.setClaimSeqNo((String)claimSeqNo);
			}
		}else{
			SqlJoinUtils sqlUtil=new SqlJoinUtils();
			sqlUtil.append(" FROM CiClaimPlatformLog c WHERE c.bussNo = ? and c.requestType= ? and comCode = ? ");
			sqlUtil.append(" AND (( c.claimSeqNo is not null and c.status != ? ) ");
			sqlUtil.append(" OR c.status = ? )");
			sqlUtil.append(" ORDER BY c.createTime DESC");
			List<CiClaimPlatformLog> logPoList = databaseDao.findAllByHql(CiClaimPlatformLog.class,sqlUtil.getSql(),bussNo.trim(),reqType,comCode,"1","1");
			if(logPoList!=null&&logPoList.size()>0){
				platLogVo = new CiClaimPlatformLogVo();
				platLogVo = Beans.copyDepth().from(logPoList.get(0))
						.to(CiClaimPlatformLogVo.class);
			}
		}
		
		return platLogVo;
	}
	
	/**
	 * <pre>
	 * 补送时，更新当前数据的状态
	 * </pre>
	 * @param logId
	 * @modified: ☆Luwei(2017年1月16日 下午5:05:59): <br>
	 */
	public void platformLogUpdate(Long logId){
		CiClaimPlatformLog platLogPo =  databaseDao.findByPK(CiClaimPlatformLog.class,logId);
		if(platLogPo != null){
			platLogPo.setStatus("2");// 已补送
			databaseDao.update(CiClaimPlatformLog.class,platLogPo);
		}
	}
	/**
	 * <pre>
	 * 补送时，更新当前数据的状态
	 * </pre>
	 * @param logId
	 * @modified: ☆Luwei(2017年1月16日 下午5:05:59): <br>
	 */
	public void platformLogUpdate(CiClaimPlatformLogVo ciClaimPlatformLogVo){
		CiClaimPlatformLog platLogPo =  databaseDao.findByPK(CiClaimPlatformLog.class,ciClaimPlatformLogVo.getId());
		if(platLogPo != null){
			platLogPo.setStatus(ciClaimPlatformLogVo.getStatus());
			platLogPo.setClaimSeqNo(ciClaimPlatformLogVo.getClaimSeqNo());
			databaseDao.update(CiClaimPlatformLog.class,platLogPo);
		}
	}
	/**
	 * 更新当前数据的状态
	 * @param taskId
	 * @param status
	 */
	public void platformLogUpdateByTaskId(Long taskId,String status){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("taskId",taskId);
		queryRule.addEqual("status",CodeConstants.CommonConst.FALSE);
		List<CiClaimPlatformLog> logPoList = databaseDao.findAll(CiClaimPlatformLog.class,queryRule);
		if(logPoList!=null && logPoList.size()>0){
			for(CiClaimPlatformLog po:logPoList){
				po.setStatus(status);
				databaseDao.update(CiClaimPlatformLog.class,po);
			}
		}
	}
	
	public List<CiClaimPlatformLogVo> findLogByBussNoAndRequestName (String requestName,String bussNo){
		if(StringUtils.isEmpty(requestName)||StringUtils.isEmpty(bussNo))return null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("bussNo",bussNo);
		queryRule.addEqual("status","1");
		queryRule.addLike("requestName", requestName);
		
		List<CiClaimPlatformLogVo> logVos=new ArrayList<CiClaimPlatformLogVo>();
		List<CiClaimPlatformLog> logPoList = databaseDao.findAll(CiClaimPlatformLog.class,queryRule);
		if(logPoList!=null&&logPoList.size()>0){
			logVos= Beans.copyDepth().from(logPoList).toList(CiClaimPlatformLogVo.class);
		}
		return logVos;
	}
	
	/**
	 * 查询案件的平台上传最新log记录
	 * @param bussNo（不能为空）
	 * @param reqType（不能为空）
	 * @param comCode 保单机构代码（区别全国和上海平台）
	 * @return CiClaimPlatformLogVo
	 */
	public CiClaimPlatformLogVo findLastestLogByReqTypeBussNoComCode(String reqType,String bussNo,String comCode) {
		if(StringUtils.isEmpty(reqType)||StringUtils.isEmpty(bussNo)) return null;
		CiClaimPlatformLogVo platLogVo = null;
		// 查询旧理赔
		// 旧理赔
		// 查询lcmain表
		SqlJoinUtils sqlUtil1 = new SqlJoinUtils();
		sqlUtil1.append("SELECT registNo from PrpLRegist WHERE 1=1 ");
		sqlUtil1.append("AND registNo=");
		sqlUtil1.append("'"+bussNo+"' ");
		sqlUtil1.append("AND flag=");
		sqlUtil1.append("'oldClaim' ");
		// 执行查询
		List<Object[]> objects1 = baseDaoService.getAllBySql(sqlUtil1.getSql(),sqlUtil1.getParamValues());
		if(objects1.size()>0){
			// 查询lcmain表
			SqlJoinUtils sqlUtil = new SqlJoinUtils();
			sqlUtil.append("SELECT CLAIMSEQUENCENO from PrplCMain WHERE 1=1 ");
			sqlUtil.append("AND registNo=");
			sqlUtil.append("'"+bussNo+"' ");
			if(RequestType.RegistInfoCI_SH.getCode().equals(reqType)||RequestType.RegistInfoCI.getCode().equals(reqType)){// 交强
				sqlUtil.append("AND riskCode like");
				sqlUtil.append("'11%'");
			}else{
				sqlUtil.append("AND riskCode like");
				sqlUtil.append("'12%'");
			}
			// 执行查询
			List<Object[]> objects = baseDaoService.getAllBySql(sqlUtil.getSql(),sqlUtil.getParamValues());
			if(objects.size()>0){
				Object claimSeqNo = objects.get(0);
				platLogVo = new CiClaimPlatformLogVo();
				platLogVo.setClaimSeqNo((String)claimSeqNo);
			}
		}else{
			QueryRule queryRule = QueryRule.getInstance();
			queryRule.addEqual("requestType",reqType);
			queryRule.addEqual("bussNo",bussNo);
			queryRule.addEqual("comCode",comCode);
			queryRule.addDescOrder("createTime");
			List<CiClaimPlatformLog> logPoList = databaseDao.findAll(CiClaimPlatformLog.class,queryRule);
			if(logPoList!=null&&logPoList.size()>0){
				platLogVo = new CiClaimPlatformLogVo();
				platLogVo = Beans.copyDepth().from(logPoList.get(0)).to(CiClaimPlatformLogVo.class);
			}
		}
		return platLogVo;
	}
}
