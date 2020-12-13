package ins.sino.claimcar.flow.service.spring;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.common.po.SysMsgReceiver;
import ins.sino.claimcar.common.po.SysUtiBulletin;
import ins.sino.claimcar.flow.service.SysMsgContentService;
import ins.sino.claimcar.flow.service.SysMsgReceiverService;
import ins.sino.claimcar.manager.vo.SysMsgContentVo;
import ins.sino.claimcar.manager.vo.SysMsgReceiverVo;
import ins.sino.claimcar.manager.vo.SysUtiBulletinVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path(value = "sysMsgReceiverService")
public class SysMsgReceiverServiceImpl implements SysMsgReceiverService {
	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	SysMsgContentService sysMsgContentService;

	/* 
	 * @see ins.sino.claimcar.flow.service.SysMsgReceiverService#findMsgByPK(java.lang.Long)
	 * @param id
	 * @return
	 */
	@Override
	public SysMsgReceiverVo findMsgByPK(Long id) {
		SysMsgReceiverVo receVo = new SysMsgReceiverVo();
		SysMsgReceiver recePo = new SysMsgReceiver();
		if (id != null) {
			recePo = databaseDao.findByPK(SysMsgReceiver.class, id);
			Beans.copy().from(recePo).to(receVo);
		}
		return receVo;

	}

	/* 
	 * @see ins.sino.claimcar.flow.service.SysMsgReceiverService#findMsgByUser(java.lang.String)
	 * @param userCode
	 * @return
	 */
	@Override
	public List<SysMsgContentVo> findMsgByUser(String userCode) {
		List<SysMsgReceiver> poList = new ArrayList<SysMsgReceiver>();
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("msgReceiver", userCode);
		qr.addDescOrder("ctreatedate");// 时间最新在最前
		poList = databaseDao.findAll(SysMsgReceiver.class, qr);

		// 根据查询到的该用户被回复留言信息查询出回复的留言
		List<SysMsgContentVo> msgConList = new ArrayList<SysMsgContentVo>();
		for (SysMsgReceiver recePo : poList) {
			Long mId = recePo.getMsgId();
			if ("0".equals(recePo.getReadFlag())) {// 判断是否为已读
				SysMsgContentVo msgCon = sysMsgContentService
						.findSysMsgByPK(mId);
				msgConList.add(msgCon);
			}
		}

		return msgConList;
	}
	
	@Override
	public List<SysUtiBulletinVo> findAllSysUtiBulletin() {
		List<SysUtiBulletinVo> SysUtiBulletinVoList = new ArrayList<SysUtiBulletinVo>();
		QueryRule qr = QueryRule.getInstance();
		qr.addLikeIfExist("systemCode","%claim%");
		qr.addGreaterEqualIfExist("endTime",new Date());
		List<SysUtiBulletin> SysUtiBulletinPoList = new ArrayList<SysUtiBulletin>();
		SysUtiBulletinPoList = databaseDao.findAll(SysUtiBulletin.class, qr);
		if(SysUtiBulletinPoList!=null&&!SysUtiBulletinPoList.isEmpty()){
			SysUtiBulletinVoList = Beans.copyDepth().from(SysUtiBulletinPoList).toList(SysUtiBulletinVo.class);
		}

		return SysUtiBulletinVoList;
	}
	
	
	@Override
	public ResultPage<SysUtiBulletinVo> findMoreSysUtiBulletin() {
		List<SysUtiBulletinVo> SysUtiBulletinVoList = this.findAllSysUtiBulletin();
		ResultPage<SysUtiBulletinVo> resultPage = new ResultPage<SysUtiBulletinVo>(0, 10, SysUtiBulletinVoList.size(), SysUtiBulletinVoList);
		return resultPage;
	}
	
	@Override
	public ResultPage<SysMsgContentVo> findMoreSysMsg(String userCode) {
		List<SysMsgContentVo> SysMsgVoList = this.findMsgByUser(userCode);
		ResultPage<SysMsgContentVo> resultPage = new ResultPage<SysMsgContentVo>(0, 10, SysMsgVoList.size(), SysMsgVoList);
		return resultPage;
	}

}
