package ins.sino.claimcar.subrogation.service;

import java.util.List;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.subrogation.vo.PrpLPlatCheckVo;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;
import ins.sino.claimcar.subrogation.vo.SubrogationCheckVo;

public interface SubrogationHandleService {

	/**
	 * 展示追偿信息
	 * 1 调用结算接口，获取最新的结算金额,更新prplplatlock的sumRecoveryAmount
	 * 2 查询PrpLrecoveryOrPay 表获得追偿金额
	 * @param registNo
	 * @param recoveryCode
	 * @modified:
	 * ☆YangKun(2016年3月31日 下午9:24:21): <br>
	 */
	public abstract PrpLPlatLockVo sendBeforeRevoeryData(String registNo,
			String recoveryCode) throws Exception;

	/**
	 * TODO  追回金额是否可手动修改
	 * @param registNo
	 * @param recoveryCode
	 * @param realReOrPayAmount
	 * @throws Exception
	 * @modified:
	 * ☆YangKun(2016年4月9日 下午4:55:16): <br>
	 */
	public abstract void recoveryConfirm(String registNo, String recoveryCode,
			Double realReOrPayAmount, SysUserVo userVo) throws Exception;

	/**
	 * 清算确认功能
	 * 更新platLock 清算状态字段 
	 * 发送收付接口
	 * @param registNo
	 * @param recoveryCode
	 * @param realAmount
	 * @throws Exception 
	 * @modified:
	 * ☆YangKun(2016年4月11日 下午4:01:22): <br>
	 */
	public abstract void qsConfirm(String recoveryCode, Double realAmount,
			SysUserVo userVo) throws Exception;

	/**
	 * 追偿送收付  P6C	追偿赔款
	 * @param platLockVo
	 * @throws Exception
	 */
	public abstract void reCoveryPayToPayment(PrpLPlatLockVo platLockVo,
			PrpLRegistVo registVo, SysUserVo userVo) throws Exception;

	/**
	 * 追偿送收付  P6C	追偿赔款
	 * @param platLockVo
	 * @throws Exception
	 */
	public abstract void qsConfirmToPayment(PrpLPlatLockVo platLockVo,
			SysUserVo userVo) throws Exception;

	public abstract List<PrpLPlatCheckVo> findByOther(SubrogationCheckVo subrogationQuery);

}