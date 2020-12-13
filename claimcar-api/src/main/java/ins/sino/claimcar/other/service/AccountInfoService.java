package ins.sino.claimcar.other.service;

import java.math.BigDecimal;
import java.util.List;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.manager.vo.PrpdIntermBankVo;
import ins.sino.claimcar.manager.vo.PrpdcheckBankVo;
import ins.sino.claimcar.other.vo.PrpDAccRollBackAccountVo;
import ins.sino.claimcar.other.vo.PrpLCheckmAuditVo;
import ins.sino.claimcar.other.vo.PrpLPayBankHisVo;
import ins.sino.claimcar.other.vo.PrpLPayBankVo;
import ins.sino.claimcar.other.vo.PrplInterrmAuditVo;

/**
 * <pre>账户信息修改处理service</pre>
 * @author ★wu
 */
public interface AccountInfoService {

	/**
	 * 根据计算书号组装payBankVo
	 * @param compensateNo
	 * @return
	 */
	public abstract PrpLPayBankVo getPayBankVo(String compensateNo,PrpLPayCustomVo paycustomVo,String registNo,String payType);

	public abstract void savePayBankHis(PrpLPayCustomVo paycustomVo);

	public abstract void saveAcc(PrpLPayBankVo payBankVo, SysUserVo userVo,String originalAccountNo,Long oldPayeeId) throws Exception;

	public abstract PrpLPayBankHisVo findPayBankHisVo(Long payeeId);

	public abstract PrpDAccRollBackAccountVo findRollBackAccount(String certiNo,String accountId,String payType);
	
	public abstract PrpDAccRollBackAccountVo findRollBackByOldAccountId(String certiNo,String oldAccountId,String payType);

	public PrpDAccRollBackAccountVo findRollBackBySerialNo(String certiNo, String serialNo, String payType);

	public abstract void saveAccRollBackAccount(PrpDAccRollBackAccountVo accRollBaccVo);
	
	public abstract PrpLPayCustomVo findPayCustomVo(String registNo,String accountId);
	
	/**
	 * 如果退票只修改账号，银行支行，手机号则自动审核通过；,
	 * 如果退票信息修改修改了收款人类型、收款人账户名，则必须通过审核。
	 * @param compensateNo
	 * @param payCustomVo
	 * @param payBankVo
	 * @param originalAccountNo
	 * @param userVo
	 * @throws Exception
	 * @modified:
	 * ☆Luwei(2016年12月27日 下午5:58:42): <br>
	 */
	public String saveAccountInfo(String compensateNo,PrpLPayCustomVo payCustomVo,
	            	            PrpLPayBankVo payBankVo,String originalAccountNo,SysUserVo userVo) throws Exception;


	public String saveRollbackAccountInfo(String compensateNo, PrpLPayCustomVo payCustomVo, PrpLPayBankVo payBankVo, SysUserVo userVo) throws Exception;
	
	/**
	 * <pre>账户信息修改审核提交</pre>
	 * @param payBankVo
	 * @param userVo
	 * @throws Exception
	 * @modified:
	 * ☆Luwei(2016年12月28日 上午9:52:10): <br>
	 */
	public String accountSubmit(PrpLPayBankVo payBankVo,SysUserVo userVo,String originalAccountNo) throws Exception;
	
	/**
	 * 发送报文至收付系统
	 * @param payCustomVo
	 * @param userVo
	 * @throws Exception
	 */
	public String sendAccountToPayment(PrpLPayCustomVo payCustomVo,SysUserVo userVo,String compensateNo,PrpLPayBankVo payBankVo)
			throws Exception;
	/**
	 * 公估费退票发送报文至收付系统
	 * @param bankVo
	 * @param userVo
	 * @param accuntVo
	 * @return
	 * @throws Exception
	 */
	public String sendAssessFeeAccountToPayment(PrpdIntermBankVo bankVo,SysUserVo userVo,String registNo,PrpDAccRollBackAccountVo accuntVo,String bankId,String intermCode,PrplInterrmAuditVo auditVo)
			throws Exception;


	/**
	 * 公估费退票推送至新收付
	 * @param bankVo
	 * @param userVo
	 * @param registNo
	 * @param accuntVo
	 * @param bankId
	 * @param intermCode
	 * @param auditVo
	 * @return
	 * @throws Exception
	 */
	public String sendAssessFeeAccountToNewPayment(PrpdIntermBankVo bankVo,SysUserVo userVo,String registNo,PrpDAccRollBackAccountVo accuntVo,String bankId,String intermCode,PrplInterrmAuditVo auditVo)
			throws Exception;
	
	/**
	 * 账号信息修改接口补传
	 * @param compensateNo
	 * @param xml
	 */
	public String reUpload(String compensateNo,String xml,SysUserVo userVo) throws Exception;
	/**
	 * 账号信息修改接口补传
	 * @param compensateNo
	 * @param xml
	 */
	public String reUploadNewPayment(String compensateNo,String xml,SysUserVo userVo,String payeeId) throws Exception;
	/**
	 * 公估费退票接口补传
	 * @param compensateNo
	 * @param registNo
	 * @param userVo
	 * @return
	 * @throws Exception
	 */
	public String reUploadOfAssessorFeeBackTickit(String compensateNo,String registNo,SysUserVo userVo)throws Exception;
	/**
	 * 查询已存在PrpLPayBankVo数据
	 * @param registNo
	 * @param compensateNo
	 * @return PrpLPayBankVo
	 * @modified:
	 * ☆Luwei(2016年12月28日 下午6:59:27): <br>
	 */
	public PrpLPayBankVo findOldPayBank(String registNo,String compensateNo,String payeeId,String payType);


	
	
	
	
	public List<PrpLPayBankVo> organizePayBankVo(PrpDAccRollBackAccountVo rollBackAccountVo);

	public void writeAccRollBackAccount(PrpLPayBankVo payBankVo,String originalAccountNo) throws Exception;

	public void updateAccRollBackAccountStatus(PrpLPayBankVo payBankVo) throws Exception;

	public void updatePayBank(BigDecimal id);

	/**
	 * <pre>查勘费退票送收付</pre>
	 * @param checkbankVo
	 * @param userVo
	 * @param registNo
	 * @param backAccountVo
	 * @param bankId
	 * @param checkCode
	 * @param auditVo
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年8月5日 下午2:57:34): <br>
	 */
	public String  sendCheckFeeAccountToPayment(PrpdcheckBankVo checkbankVo,SysUserVo userVo,String registNo,
														PrpDAccRollBackAccountVo backAccountVo,String bankId,String checkCode,
														PrpLCheckmAuditVo auditVo) throws Exception;

	/**
	 * 查勘费退票推送至新收付
	 * @param checkbankVo
	 * @param userVo
	 * @param registNo
	 * @param backAccountVo
	 * @param bankId
	 * @param checkCode
	 * @param auditVo
	 * @return
	 * @throws Exception
	 */
	public String  sendCheckFeeAccountToNewPayment(PrpdcheckBankVo checkbankVo,SysUserVo userVo,String registNo,
												PrpDAccRollBackAccountVo backAccountVo,String bankId,String checkCode,
												PrpLCheckmAuditVo auditVo) throws Exception;

	/**
	 * 查勘费退票修改补送收付
	 * @param compensateNo
	 * @param registNo
	 * @param userVo
	 * @return
	 * @throws Exception 
	 * @modified:
	 * ☆XiaoHuYao(2019年8月15日 下午3:43:50): <br>
	 */
	public String reUploadOfCheckFeeBackTickit(String compensateNo,String registNo,SysUserVo userVo) throws Exception;
	/**
	 * 平安保存或更新退票银行信息，更新退票信息
	 * @param payBankVo 退票银行信息
	 * @throws Exception 异常信息
	 */
	public  void saveAccRbackAccountPingAn(PrpLPayBankVo payBankVo) throws Exception;
}