package ins.sino.claimcar.mobilecheck.service;

import ins.sino.claimcar.flow.vo.PrplLockListVo;

public interface LockService {

	/**
	 * 保存PrplLockList
	 * @param yWMainId
	 * @param lockType
	 */
	public abstract void savePrplLockList(Long yWMainId, String lockType);
	
	/**
	 * 查询PrplLockList
	 * @param yWMainId
	 * @param lockType
	 * @return
	 */
	public abstract PrplLockListVo findPrplLockListById(Long yWMainId, String lockType);

	/**
	 * 删除PrplLockList
	 * @param yWMainId
	 * @param lockType
	 */
	public abstract void deletePrplLockListById(Long yWMainId, String lockType);
	
}
