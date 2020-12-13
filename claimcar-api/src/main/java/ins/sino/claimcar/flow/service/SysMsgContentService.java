package ins.sino.claimcar.flow.service;

import ins.sino.claimcar.manager.vo.SysMsgContentVo;

import java.util.List;

public interface SysMsgContentService {

	/**
	 * 保存案件备注信息
	 * 
	 * @param msgVo
	 */
	public SysMsgContentVo saveSysMsg(SysMsgContentVo msgVo);

	/**
	 * 查询案件备注信息
	 * 
	 * @param bussNo
	 * @return
	 */
	public List<SysMsgContentVo> findSysMsg(String bussNo);

	/**
	 * 根据主键查询案件备注消息，如果为回复消息，就将回复的原留言查出setReMsgVo
	 * 
	 * @param mId
	 * @return
	 */
	public SysMsgContentVo findSysMsgByPK(Long mId);

}
