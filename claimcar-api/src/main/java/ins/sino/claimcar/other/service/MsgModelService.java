package ins.sino.claimcar.other.service;

import ins.framework.common.ResultPage;
import ins.sino.claimcar.claim.vo.PrpsmsMessageVo;
import ins.sino.claimcar.manager.vo.PrpdAddresseeVo;
import ins.sino.claimcar.other.vo.SysMsgModelVo;

import java.math.BigDecimal;
import java.util.List;

public interface MsgModelService {

	/**
	 * 保存或更新短信模板
	 * 
	 * @param SysMsgModelVo
	 * @return sysMsgModel
	 */
	public abstract void saveOrUpdateSysMsgModel(
			SysMsgModelVo sysMsgModelVo);
	/**
	 * 保存短信表yzy
	 * @param prpsmsMessageVo
	 */
	public abstract void saveorUpdatePrpSmsMessage(PrpsmsMessageVo prpsmsMessageVo);
	
	/**
	 * 根据报案号查询所有短信
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆LinYi(2017年8月17日 下午4:00:41): <br>
	 */
	public abstract List<PrpsmsMessageVo> findPrpSmsMessageByBusinessNo(String registNo);
	
	/**
	 * 根据主键查询短信内容
	 * <pre></pre>
	 * @param misId
	 * @return
	 * @modified:
	 * ☆LinYi(2017年8月18日 上午9:25:47): <br>
	 */
	public abstract PrpsmsMessageVo findPrpSmsMessageByPrimaryKey(BigDecimal misId);

	/**
	 * 根据主键find SysMsgModelVo
	 * 
	 * @param id
	 * @return modelVo
	 */
	public abstract SysMsgModelVo findSysMsgModelVoByPK(Long id);

	public abstract ResultPage<SysMsgModelVo> findAllSysMsgModelByHql(
			SysMsgModelVo sysMsgModelVo, int start, int length);

	public abstract ResultPage<PrpsmsMessageVo> findAllSmsMessageByHql(
			PrpsmsMessageVo prpsmsMessageVo, int start, int length)throws Exception;

	
	public abstract ResultPage<PrpdAddresseeVo> searchLeaderInfo(
			PrpdAddresseeVo prpdAddresseeVo, int start, int length);
	/**
	 * 根据类型查询自助理赔短信接收人信息
	 */
	public abstract List<PrpdAddresseeVo> searchAutoClaimMsgSender(String flag);
	/**
	 * 根据主键查询自助理赔短信接收人信息
	 */
	public abstract PrpdAddresseeVo findAutoClaimMsgSenderById(Long id);

	public abstract void updateAutoClaimMsgSender(PrpdAddresseeVo prpdAddresseeVo);
	
	public abstract List<PrpdAddresseeVo> findAllLeaderInfo();
	/**
	 * 根据主键删除模板类型
	 * 
	 * @param id
	 */
	public abstract void deleteSysMsgModelByPK(Long id);
	
	

	public abstract void updateLeaderInfo(List<PrpdAddresseeVo> list,String userCode);
	
	public abstract String activOrCancel(String id,String validFlag);

	/** 通过状态查询 */
	public abstract List<PrpsmsMessageVo> findPrpSmsMessagesByStatus(List<String> status);

	List<PrpdAddresseeVo>  findLeaderInfoById(Long id);

	void addLeaderInfo(List<PrpdAddresseeVo> list, String userCode);

	boolean checkIfAddAgain(List<PrpdAddresseeVo> list);

	boolean checkIfExist(List<PrpdAddresseeVo> list);

	void updateLeaderInfo2(List<PrpdAddresseeVo> list, String userCode);

	void deleteLeaderInfoById(Long id);
    
}