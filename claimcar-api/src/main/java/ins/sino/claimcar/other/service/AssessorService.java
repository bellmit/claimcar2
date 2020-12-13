/******************************************************************************
 * CREATETIME : 2016年9月23日 下午3:12:24
 ******************************************************************************/
package ins.sino.claimcar.other.service;

import ins.framework.common.ResultPage;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.other.vo.AssessorQueryResultVo;
import ins.sino.claimcar.other.vo.AssessorQueryVo;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLAssessorMainVo;
import ins.sino.claimcar.other.vo.PrpLAssessorVo;
import ins.sino.claimcar.other.vo.PrpLPayBankVo;
import ins.sino.claimcar.other.vo.PrplInterrmAuditVo;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ★XMSH
 */
public interface AssessorService {

	public PrpLAssessorVo findAssessorVoByPk(Long id);

	/**
	 * 公估费主表
	 * @param taskNo
	 * @return
	 * @modified:
	 * ☆XMSH(2016年8月25日 上午9:42:46): <br>
	 */
	public PrpLAssessorMainVo findAssessorMainVoByTaskNo(String taskNo);
	/**
	 * 通过id查询公估费主表
	 * @param id
	 * @return
	 */
	public PrpLAssessorMainVo findAssessorMainVoById(Long id);

	/**
	 * 保存或更新
	 * @param assessorVo
	 * @modified:
	 * ☆XMSH(2016年8月16日 下午5:38:36): <br>
	 */
	public void saveOrUpdatePrpLAssessor(PrpLAssessorVo assessorVo,SysUserVo uservo);

	public PrpLAssessorVo findAssessorByLossId(String registNo,String taskType,Integer serialNo,
														String intermcode);

	public PrpLAssessorVo findAssessorByLossId(String registNo,String taskType, String intermcode);

	public List<AssessorQueryResultVo> getDatas(AssessorQueryVo queryVo) throws Exception;

	public List<AssessorQueryResultVo> findAssessorMainVo(String taskNo);
	
	public ResultPage<AssessorQueryResultVo> findPageForAssessor(AssessorQueryVo queryVo) throws Exception;

	public Double applyAssessorTask(List<List<Object>> objects,SysUserVo userVo) throws Exception;

	/**
	 * 更新定损公估表的标志
	 * @param id
	 * @param registNo
	 * @param intermCode
	 * @param underWriteFlag
	 * @param index
	 * @throws Exception
	 * @modified:
	 * ☆XMSH(2016年8月24日 上午10:44:21): <br>
	 */
	public PrpLAssessorVo updatePrpLAssessor(Long id,String registNo,String intermCode,String underWriteFlag,
														int index,String amount,SysUserVo userVo) throws Exception;

	public Long saveOrUpdatePrpLAssessorMain(PrpLAssessorMainVo mainVo,SysUserVo userVo);
	
	public List<Map<String,Object>> createExcelRecord(List<AssessorQueryResultVo> results) throws Exception;
	
	public void submitCancel(Double flowTaskId,SysUserVo userVo) throws Exception;

	/**
	 * <pre>更新公估费用金额</pre>
	 * @param feeVoList
	 * @modified:
	 * ☆Luwei(2016年10月9日 下午4:28:51): <br>
	 */
	public void updateAssessorFee(List<PrpLAssessorFeeVo> feeVoList);
	
	/**
	 * 
	 * <pre></pre>
	 * @param mainVo
	 * @modified:
	 * ☆niuqiang(2016年10月12日 下午3:55:49): <br>
	 */
	public void updateSumAmountFee(PrpLAssessorMainVo mainVo);
	
	public void updateUnderWriteFlag(PrpLAssessorMainVo mainVo);
	
	public String queryPayAmount(String registNo,String assessorId);
	public String queryAmount(String registNo,String assessorId);
	
	public PrpLAssessorFeeVo findAssessorFeeVoByComp(String registNo,String compensateNo);
	
	public void saveEndCaseTimeToAssessor(String registNo,Date Date);
	
	 public PrpLAssessorFeeVo findAssessorFeeVoByComp(String compensateNo);
	 
	 public void updateAssessorFee(PrpLAssessorFeeVo feeVoList);
	 
	 /**
	  * 通过计算书号查找 公估费用主表  用于送收付 补送
	  * <pre></pre>
	  * @param compensateNo
	  * @return
	  * @modified:
	  * *牛强(2017年5月12日 上午8:48:58): <br>
	  */
	 public PrpLAssessorMainVo findAsseMainVoByCompNo(String compensateNo);
	 /**
	  * 通过报案号查询公估费费用表
	  * @param registNo
	  * @return
	  */
	 public List<PrpLAssessorFeeVo> findPrpLAssessorFeeVoByRegistNo(String registNo);



	 /**
	  * 公估费银行账号信息查询
	  * @param prplWfTaskQueryVo
	  * @param start
	  * @param length
	  * @return
	  * @throws ParseException
	  */
	 public abstract ResultPage<PrpLPayBankVo> search(PrpLWfTaskQueryVo prplWfTaskQueryVo,int start,int length,String handleStatus) throws ParseException;
	 /**
	  * 公估费退票审核查询
	  * @param prplWfTaskQueryVo
	  * @param start
	  * @param length
	  * @param handleStatus
	  * @return
	  * @throws ParseException
	  */
	 public abstract ResultPage<PrpLPayBankVo> assessorAuditSearch(PrpLWfTaskQueryVo prplWfTaskQueryVo,int start,int length,String handleStatus) throws ParseException;
	 /**
	  * 通过计算书号或者结算单号EndNo查询PrpLAssessorFee表
	  * @param bussNo
	  * @return
	  */
	 public List<PrpLAssessorFeeVo> findPrpLAssessorFeeVoByCompensateNoOrEndNo(String bussNo);
		 

	 /**
	  * 更新公估费的结算单号,operateType==0是销毁结算单号，operateType==1是生成结算单号
	  * @param compensateNo
	  * @param settleNo
	  * @param operateType
	  */
	 public int updateSettleNo(String compensateNo,String settleNo,String operateType);
	 /**
	  * 通过公估费退票审核表Id查询退票审核表
	  * @param id
	  * @return
	  */
	 public PrplInterrmAuditVo findPrplInterrmAuditVoById(Long id);
	 /**
	  * 更新或保存公估费退票审核表
	  * @param auditVo
	  * @throws Exception
	  */
	 public void updateOrSaveOfPrplInterrmAudit(PrplInterrmAuditVo auditVo)throws Exception;
	/**
	 * 回写退票表Prpdaccrollbackaccount的AuditId和IsHaveAudit,auditFlag三字段
	 * @param backId
	 * @param auditId
	 * @param isHaveAudit
	 */
	 public void updateAuditIdAndIsHaveAuditAndAuditFlagOfPrpdaccrollbackaccount(Long backId,Long auditId,String isHaveAudit,String auditFlag)throws Exception;
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
	 public PrplInterrmAuditVo findPrplInterrmAuditVoByBackAccountId(Long backAccountId);
	 
	 /**
	  * 通过报案号，业务号(业务号有可能为结算单号或计算书号)，任务状态，查询公估费退票审核表
	  * @param registNo
	  * @param bussNo
	  * @param status
	  * @return
	  */
	 public PrplInterrmAuditVo findPrplInterrmAuditVoByRegistNoAndBussNoAndStatus(String registNo,String bussNo,String status);
	/**
	 * 通过报案号，任务类型，核损标志，车牌号查询（车牌号可空）
	 * @param registNo
	 * @param taskType
	 * @param underWriteFlag
	 * @param licenseNo
	 * @return
	 */
	 public PrpLAssessorVo findPrpLAssessorVo(String registNo,String taskType,String underWriteFlag,String licenseNo);
	
	 /**
	  * 更新公估表
	  * @param prpLAssessorVo
	  */
	 public void updatePrpLAssessor(PrpLAssessorVo prpLAssessorVo);

	/**
	 * 获取公估费查询条件（导出条件也用这个）
	 * <pre></pre>
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆zhujunde(2019年2月14日 上午10:41:58): <br>
	 */
	public SqlJoinUtils getSqlJoinUtils(AssessorQueryVo queryVo) throws Exception;

	 /**
	  * 根据报案号和创建人查询公估任务
	  * @param registNo
	  * @param createUser
	  * @return
	  */
	 public List<PrpLAssessorVo> findListLAssessorVo(String registNo,String createUser);

	/**
	 * <pre>查询公估费补录数据</pre>
	 * @param queryVo
	 * @return
	 * @throws Exception 
	 * @modified:
	 * ☆XiaoHuYao(2019年8月9日 下午2:34:37): <br>
	 */
	public ResultPage<AssessorQueryResultVo> findSupplymentPageForAssessor(AssessorQueryVo queryVo) throws Exception;

	/**
	 * <pre>查勘公估费记录</pre>
	 * @param prpLAssessorVo
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年8月27日 下午5:32:54): <br>
	 */
	public PrpLAssessorVo findAssessorByParams(PrpLAssessorVo prpLAssessorVo);


}

