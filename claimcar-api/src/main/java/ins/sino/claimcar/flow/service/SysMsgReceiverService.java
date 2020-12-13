package ins.sino.claimcar.flow.service;

import freemarker.core.ParseException;
import ins.framework.common.ResultPage;
import ins.sino.claimcar.manager.vo.SysMsgContentVo;
import ins.sino.claimcar.manager.vo.SysMsgReceiverVo;
import ins.sino.claimcar.manager.vo.SysUtiBulletinVo;

import java.util.List;

public interface SysMsgReceiverService {

	/**
	 * 根据主键查询SysMsgReceiverVo
	 * 
	 * @param id
	 * @return
	 */
	public SysMsgReceiverVo findMsgByPK(Long id);

	/**
	 * 根据msgReceiver查询相关留言回复表，根据留言回复表查询出回复给该用户的留言
	 * 
	 * @param userCode
	 * @return
	 */
	public List<SysMsgContentVo> findMsgByUser(String userCode);
	/**
	 * 查询系统公告
	 * <pre></pre>
	 * @return
	 * @throws ParseException
	 * @modified:
	 * ☆WLL(2016年10月24日 下午6:03:48): <br>
	 */
	public List<SysUtiBulletinVo> findAllSysUtiBulletin();
	/**
	 * 查看更多系统公告
	 * <pre></pre>
	 * @return
	 * @modified:
	 * ☆LuLiang(2016年10月24日 下午9:01:10): <br>
	 */
	public ResultPage<SysUtiBulletinVo> findMoreSysUtiBulletin();
	
	public ResultPage<SysMsgContentVo> findMoreSysMsg(String userCode);

}
