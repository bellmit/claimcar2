package ins.sino.claimcar.subrogation.service;

import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;

import java.util.List;

public interface PlatLockDubboService {

	/**
	 * 保存锁定信息
	 * @param platLockVo
	 * @modified:
	 * ☆YangKun(2016年3月30日 下午7:49:14): <br>
	 */
	public abstract void savePlatLock(PrpLPlatLockVo platLockVo);

	/**
	 * 调用接口保存时 
	 * @param platLockVo
	 */
	public abstract void firstSavePlatLock(PrpLPlatLockVo platLockVo);

	/**
	 * 查询 可以取消的锁定
	 * @param registNo
	 * @param recoveryCode
	 * @return
	 * @modified:
	 * ☆YangKun(2016年3月30日 下午7:48:54): <br>
	 */
	public abstract List<PrpLPlatLockVo> findLockCancelList(String registNo,
			String recoveryCode);

	/**
	 * 查询
	 * @param registNo
	 * @param recoveryCode
	 * @return
	 * @modified:
	 * ☆YangKun(2016年3月30日 下午9:35:58): <br>
	 */
	public abstract PrpLPlatLockVo findPlatLockVo(String registNo,
			String recoveryCode);

	/**
	 * 查询 锁定信息
	 * @param registNo
	 * @param recoveryCode
	 * @return
	 * @modified:
	 * ☆YangKun(2016年3月30日 下午7:48:54): <br>
	 */
	public abstract List<PrpLPlatLockVo> findPlatLockList(String registNo,
			String recoveryCode);

	public abstract PrpLPlatLockVo findPlatLockByRecoveryCode(
			String recoveryCode);

}