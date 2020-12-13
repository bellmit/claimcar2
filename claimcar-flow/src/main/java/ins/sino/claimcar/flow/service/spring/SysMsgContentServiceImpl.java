package ins.sino.claimcar.flow.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.common.po.SysMsgContent;
import ins.sino.claimcar.common.po.SysMsgReceiver;
import ins.sino.claimcar.flow.service.SysMsgContentService;
import ins.sino.claimcar.manager.vo.SysMsgContentVo;
import ins.sino.claimcar.manager.vo.SysMsgReContentVo;
import ins.sino.claimcar.manager.vo.SysMsgReceiverVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * <pre></pre>
 * 
 * @author ★LiuPing
 * @CreateTime 2016年1月19日
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path(value = "sysMsgContentService")
public class SysMsgContentServiceImpl implements SysMsgContentService {

	@Autowired
	DatabaseDao databaseDao;
	
	/* 
	 * @see ins.sino.claimcar.flow.service.SysMsgContentService#saveSysMsg(ins.sino.claimcar.manager.vo.SysMsgContentVo)
	 * @param msgVo
	 * @return
	 */
	@Override
	public SysMsgContentVo saveSysMsg(SysMsgContentVo msgVo) {

		Date date = new Date();
		msgVo.setCreateDate(date);
		SysMsgContent msgPo = new SysMsgContent();
		Beans.copy().from(msgVo).to(msgPo);
		databaseDao.save(SysMsgContent.class,msgPo);
		// 如果新增的案件备注是回复某条消息，则需要往SYS_MSGRECEIVER表中存入一条数据
		if (msgPo.getReMsgId() != null) {
			SysMsgReceiverVo receVo = new SysMsgReceiverVo();// ReceiverVo
			SysMsgReceiver recePo = new SysMsgReceiver();
			SysMsgContentVo reVo = findSysMsgByPK(msgPo.getReMsgId());// 回复的原消息
			receVo.setMsgId(msgPo.getId());// 回复的消息的Id
			receVo.setMsgReceiver(reVo.getCreateUser());// 消息接收人（回复给谁
			receVo.setReadFlag("0");// 是否已读1-是 0-否
			receVo.setCtreatedate(date);
			receVo.setReceTerminalType("S");// 终端类型-系统sys
			Beans.copy().from(receVo).to(recePo);
			databaseDao.save(SysMsgReceiver.class, recePo);
			
		}

		// 将获取的承保主表信息，复制到reIntermMainVo对象中return
		Beans.copy().from(msgPo).to(msgVo);

		return msgVo;

	}

	/* 
	 * @see ins.sino.claimcar.flow.service.SysMsgContentService#findSysMsg(java.lang.String)
	 * @param bussNo
	 * @return
	 */
	@Override
	public List<SysMsgContentVo> findSysMsg(String bussNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("bussNo", bussNo);
		queryRule.addAscOrder("createDate");
		// 根据bussNo查询所有案件备注
		List<SysMsgContent> sysMsgContents = databaseDao.findAll(
				SysMsgContent.class, queryRule);
		List<SysMsgContentVo> sysMsgContentVos = new ArrayList<SysMsgContentVo>();
		SysMsgContentVo sysMsgContentVo = new SysMsgContentVo();
		Map<Long, SysMsgContentVo> idMap = new HashMap<Long, SysMsgContentVo>();
		SysMsgContent sysMsgContent = new SysMsgContent();
		for (int i = 0; i < sysMsgContents.size(); i++) {
			sysMsgContent = sysMsgContents.get(i);
			// 复制到Vo对象中
			sysMsgContentVo = Beans.copyDepth().from(sysMsgContent)
					.to(SysMsgContentVo.class);
			sysMsgContentVo.setFloorIndex(i + 1);
			idMap.put(sysMsgContentVo.getId(), sysMsgContentVo);
			// 如果为回复，则找到回复的原内容Vo，setReMsgVo
			if (sysMsgContentVo.getReMsgId() != null) {
				SysMsgContentVo msgVo=idMap.get(sysMsgContentVo.getReMsgId());
				if(msgVo!=null){
					SysMsgReContentVo reMsgVo = new SysMsgReContentVo();
					Beans.copy().from(msgVo).to(reMsgVo);
					sysMsgContentVo.setReMsgVo(reMsgVo);
				}
			}
			sysMsgContentVos.add(sysMsgContentVo);
		}
		return sysMsgContentVos;

	}

	/* 
	 * @see ins.sino.claimcar.flow.service.SysMsgContentService#findSysMsgByPK(java.lang.Long)
	 * @param mId
	 * @return
	 */
	@Override
	public SysMsgContentVo findSysMsgByPK(Long mId) {
		SysMsgContentVo MsgVo = new SysMsgContentVo();
		SysMsgContent MsgPo = new SysMsgContent();
		// 回复的原留言
		SysMsgReContentVo reMsgVo = new SysMsgReContentVo();
		SysMsgContent reMsgPo = new SysMsgContent();
		if (mId != null) {
			MsgPo = databaseDao.findByPK(SysMsgContent.class, mId);
			Beans.copy().from(MsgPo).to(MsgVo);
			// 判断是否为回复消息
			if (MsgVo.getReMsgId() != null) {
				Long reId = MsgVo.getReMsgId();
				// 根据reId查询出原留言
				reMsgPo = databaseDao.findByPK(SysMsgContent.class, reId);
				Beans.copy().from(reMsgPo).to(reMsgVo);
				MsgVo.setReMsgVo(reMsgVo);
			}
		}
		return MsgVo;
	}

}
