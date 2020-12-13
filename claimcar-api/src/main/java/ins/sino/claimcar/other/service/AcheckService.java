package ins.sino.claimcar.other.service;

import ins.framework.common.ResultPage;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.other.vo.CheckQueryResultVo;
import ins.sino.claimcar.other.vo.CheckQueryVo;
import ins.sino.claimcar.other.vo.PrpLAcheckMainVo;
import ins.sino.claimcar.other.vo.PrpLAcheckVo;
import ins.sino.claimcar.other.vo.PrpLCheckFeeVo;
import ins.sino.claimcar.other.vo.PrpLCheckmAuditVo;
import ins.sino.claimcar.other.vo.PrpLPayBankVo;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AcheckService {
	public PrpLAcheckVo findAcheckVoByPk(Long id);

	/**
	 * 查勘费主表
	 * @param taskNo
	 * @return
	 * @modified:
	 * ☆yzy(2019年8月1日 上午9:42:46): <br>
	 */
	public PrpLAcheckMainVo findAcheckMainVoByTaskNo(String taskNo);
	/**
	 * 通过id查询查勘费主表
	 * @param id
	 * @return
	 */
	public PrpLAcheckMainVo findAcheckMainVoById(Long id);

	/**
	 * 保存或更新
	 * @param acheckVo
	 * @modified:
	 * ☆yzy(2019年8月1日 下午5:38:36): <br>
	 */
	public void saveOrUpdatePrpLAcheck(PrpLAcheckVo acheckVo,SysUserVo uservo);

	public PrpLAcheckVo findAcheckByLossId(String registNo,String taskType,Integer serialNo,
														String checkcode);
	public List<PrpLAcheckVo> findAcheckByRegistNoAndTaskType(String registNo,String taskType);

	public List<CheckQueryResultVo> getDatas(CheckQueryVo queryVo) throws Exception;

	public List<CheckQueryResultVo> findAcheckMainVo(String taskNo);
	
	public ResultPage<CheckQueryResultVo> findPageForACheck(CheckQueryVo queryVo) throws Exception;

	public Double applyAcheckTask(List<List<Object>> objects,SysUserVo userVo) throws Exception;

	/**
	 * 更新定损查勘费表的标志
	 * @param id
	 * @param registNo
	 * @param checkCode
	 * @param underWriteFlag
	 * @param index
	 * @throws Exception
	 * @modified:
	 * ☆yzy(2019年8月1日 上午10:44:21): <br>
	 */
	public PrpLAcheckVo updatePrpLAcheck(Long id,String registNo,String checkCode,String underWriteFlag,
														int index,String amount,SysUserVo userVo) throws Exception;

	public Long saveOrUpdatePrpLAcheckMain(PrpLAcheckMainVo mainVo,SysUserVo userVo);
	
	public List<Map<String,Object>> createExcelRecord(List<CheckQueryResultVo> results) throws Exception;
	
	public void submitCancel(Double flowTaskId,SysUserVo userVo) throws Exception;

	/**
	 * <pre>更新查勘费用金额</pre>
	 * @param feeVoList
	 * @modified:
	 * ☆yzy(2019年8月1日 下午4:28:51): <br>
	 */
	public void updateCheckFee(List<PrpLCheckFeeVo> feeVoList);
	
	/**
	 * 
	 * <pre></pre>
	 * @param mainVo
	 * @modified:
	 * ☆niuqiang(2016年10月12日 下午3:55:49): <br>
	 */
	public void updateSumAmountFee(PrpLAcheckMainVo mainVo);
	
	public void updateUnderWriteFlag(PrpLAcheckMainVo mainVo);
	
	public String queryPayAmount(String registNo,String acheckId);
	public String queryAmount(String registNo,String acheckId);
	
	public PrpLCheckFeeVo findCheckFeeVoByComp(String registNo,String compensateNo);
	
	public void saveEndCaseTimeToAcheck(String registNo,Date Date);
	
	 public PrpLCheckFeeVo findCheckFeeVoByComp(String compensateNo);
	 
	 public void updateCheckFee(PrpLCheckFeeVo feeVo);
	 
	 /**
	  * 通过计算书号查找 查勘费用主表  用于送收付 补送
	  * <pre></pre>
	  * @param compensateNo
	  * @return
	  * @modified:
	  * *yzy(2019年8月1日 上午8:48:58): <br>
	  */
	 public PrpLAcheckMainVo findAcheckMainVoByCompNo(String compensateNo);
	 /**
	  * 通过报案号查询查勘费费用表
	  * @param registNo
	  * @return
	  */
	 public List<PrpLCheckFeeVo> findPrpLCheckFeeVoByRegistNo(String registNo);



	 /**
	  * 查勘费银行账号信息查询
	  * @param prplWfTaskQueryVo
	  * @param start
	  * @param length
	  * @return
	  * @throws ParseException
	  */
	 public abstract ResultPage<PrpLPayBankVo> search(PrpLWfTaskQueryVo prplWfTaskQueryVo,int start,int length,String handleStatus) throws ParseException;
	 /**
	  * 查勘费退票审核查询
	  * @param prplWfTaskQueryVo
	  * @param start
	  * @param length
	  * @param handleStatus
	  * @return
	  * @throws ParseException
	  */
	 public abstract ResultPage<PrpLPayBankVo> checkFeeAuditSearch(PrpLWfTaskQueryVo prplWfTaskQueryVo,int start,int length,String handleStatus) throws ParseException;
	 /**
	  * 通过计算书号或者结算单号EndNo查询PrpLCheckFee表
	  * @param bussNo
	  * @return
	  */
	 public List<PrpLCheckFeeVo> findPrpLCheckFeeVoByBussNo(String bussNo);
		 

	 /**
	  * 更新查勘费的结算单号,operateType==0是销毁结算单号，operateType==1是生成结算单号
	  * @param compensateNo
	  * @param settleNo
	  * @param operateType
	  */
	 public int updateSettleNo(String compensateNo,String settleNo,String operateType);
	 /**
	  * 通过查勘费退票审核表Id查询退票审核表
	  * @param id
	  * @return
	  */
	 public PrpLCheckmAuditVo findPrpLCheckmAuditVoById(Long id);
	 /**
	  * 更新或保存查勘费退票审核表
	  * @param auditVo
	  * @throws Exception
	  */
	 public void updateOrSaveOfPrpLCheckmAudit(PrpLCheckmAuditVo auditVo)throws Exception;
	/**
	 * 回写退票表Prpdaccrollbackaccount的AuditId和IsHaveAudit,auditFlag三字段
	 * @param backId
	 * @param auditId
	 * @param isHaveAudit
	 */
	 public void updatAccrollbackaccount(Long backId,Long auditId,String isHaveAudit,String auditFlag)throws Exception;
	 /**
	  * 更新退票表Prpdaccrollbackaccount的status字段
	  * @param status
	  */
	 public void updateStatusOfPrpdaccrollbackaccount(String status,Long backId)throws Exception;
	 
	 /**
	  * 更新退票表Prpdaccrollbackaccount的infoFlag字段
	  * @param status
	  */
	 public void updateInfoFlagOfPrpdaccrollbackaccount(String infoFlag,Long backId)throws Exception;
	 /**
	  * 通过backAcountId最新退票审核数据
	  * @param backAccountId
	  * @return
	  */
	 public PrpLCheckmAuditVo findCheckFeeAuditByAccountId(Long backAccountId);
	 
	 /**
	  * 通过报案号，业务号(业务号有可能为结算单号或计算书号)，任务状态，查询查勘费退票审核表
	  * @param registNo
	  * @param bussNo
	  * @param status
	  * @return
	  */
	 public PrpLCheckmAuditVo findPrpLCheckmAuditVoByRegistNoAndBussNoAndStatus(String registNo,String bussNo,String status);
	/**
	 * 通过报案号，任务类型，核损标志，车牌号查询（车牌号可空）
	 * @param registNo
	 * @param taskType
	 * @param underWriteFlag
	 * @param licenseNo
	 * @return
	 */
	 public PrpLAcheckVo findPrpLAcheckVo(String registNo,String taskType,String underWriteFlag,String licenseNo);
	
	 /**
	  * 更新查勘表
	  * @param PrpLAcheckVo
	  */
	 public void updatePrpLAcheck(PrpLAcheckVo PrpLAcheckVo);

	/**
	 * 获取查勘费查询条件（导出条件也用这个）
	 * <pre></pre>
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆yzy(2019年8月1日 上午10:41:58): <br>
	 */
	public SqlJoinUtils getSqlJoinUtils(CheckQueryVo queryVo) throws Exception;

	 /**
	  * 根据报案号和创建人查询查勘任务
	  * @param registNo
	  * @param createUser
	  * @return
	  */
	 public List<PrpLAcheckVo> findListLAcheckVo(String registNo,String createUser);

	/**
	 * 查询查勘费补录列表
	 * @param queryVo
	 * @return
	 * @throws Exception 
	 * @modified:
	 * ☆XiaoHuYao(2019年8月13日 下午5:35:06): <br>
	 */
	public ResultPage<CheckQueryResultVo> findSupplymentPageForCheckFee(CheckQueryVo queryVo) throws Exception;

	/**
	 * 根据报案号，计算书号，审核状态查询查勘费审核记录
	 * @param registNo
	 * @param compensateNo
	 * @param true1
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年8月15日 下午3:49:35): <br>
	 */
	public PrpLCheckmAuditVo findACheckmAuditByParams(String registNo, String bussNo, String status);

	/**
	 *查询公估费任务
	 * @param prplAcheck
	 * @return 
	 * @modified:
	 * ☆XiaoHuYao(2019年8月27日 下午5:03:04): <br>
	 */
	public PrpLAcheckVo findAcheckByParams(PrpLAcheckVo prplAcheck);

	
}
