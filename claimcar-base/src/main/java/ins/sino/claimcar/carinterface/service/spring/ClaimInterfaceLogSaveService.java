/******************************************************************************
* CREATETIME : 2017年11月17日 上午1:57:58
******************************************************************************/
package ins.sino.claimcar.carinterface.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.sino.claimcar.carinterface.po.ClaimInterfaceLog;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <pre></pre>
 * @author ★WeiLanlei
 */
@Service("claimInterfaceLogSaveService")
public class ClaimInterfaceLogSaveService {
	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private BaseDaoService baseDaoService;
	public ClaimInterfaceLogVo commitSave(ClaimInterfaceLogVo logVo) {
		logVo.setCreateTime(new Date());
		ClaimInterfaceLog platLogPo = new ClaimInterfaceLog();
		Beans.copy().from(logVo).to(platLogPo);
        databaseDao.save(ClaimInterfaceLog.class,platLogPo);
		logVo.setId(platLogPo.getId());
		return logVo;
	}
	
	/**
	 * 更新日志记录数据
	 * @param logVo
	 * @return
	 */
	public ClaimInterfaceLogVo updateLog(ClaimInterfaceLogVo logVo) {
		logVo.setCreateTime(new Date());
		ClaimInterfaceLog platLogPo = new ClaimInterfaceLog();
		Beans.copy().from(logVo).to(platLogPo);
        databaseDao.update(ClaimInterfaceLog.class, platLogPo);
		return logVo;
	}
	
	public void commitSaveAll(List<ClaimInterfaceLogVo> logVoList) {
		if(logVoList!=null && logVoList.size()>0){
			for(ClaimInterfaceLogVo logVo:logVoList){
				logVo.setCreateTime(new Date());
			}
		}
		
		if(logVoList!=null && logVoList.size()>0){
			List<ClaimInterfaceLog> logpoList=Beans.copyDepth().from(logVoList).toList(ClaimInterfaceLog.class);
	        databaseDao.saveAll(ClaimInterfaceLog.class,logpoList);
		}
		
	}
	/**
	 * 更新日志表补送状态
	 * <pre></pre>
	 * @param logVo
	 * @modified:
	 * ☆WLL(2017年11月29日 下午2:30:00): <br>
	 */
	public void commitUpdate(long id) {
		String sql = "UPDATE CLAIMINTERFACELOG SET STATUS='2' WHERE ID="+id;
		 baseDaoService.executeSQL(sql);
	}
}
