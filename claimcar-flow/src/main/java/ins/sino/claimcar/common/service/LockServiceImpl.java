package ins.sino.claimcar.common.service;

import java.util.Date;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.common.po.PrplLockList;
import ins.sino.claimcar.flow.vo.PrplLockListVo;
import ins.sino.claimcar.mobilecheck.service.LockService;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 移动查勘接口
 * @author zhujunde
 *
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("lockService")
public class LockServiceImpl implements LockService {

	@Autowired
	DatabaseDao databaseDao;
	
	@Override
	public void savePrplLockList(Long yWMainId, String lockType) {
		PrplLockListVo prplLockListVo = new PrplLockListVo();
		PrplLockList prplLockList = new PrplLockList();
		prplLockListVo.setCreateTime(new Date());
		prplLockListVo.setUpdateTime(new Date());
		prplLockListVo.setLockType(lockType);
		prplLockListVo.setyWMainId(yWMainId);
		prplLockList = Beans.copyDepth().from(prplLockListVo).to(PrplLockList.class);
		databaseDao.save(PrplLockList.class,prplLockList);
	}

	@Override
	public PrplLockListVo findPrplLockListById(Long yWMainId, String lockType) {
		PrplLockListVo prplLockListVo = new PrplLockListVo();
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("yWMainId", yWMainId);
		queryRule.addEqual("lockType", lockType);
		PrplLockList prplLockList = databaseDao.findUnique(PrplLockList.class,queryRule);
		prplLockListVo = Beans.copyDepth().from(prplLockList).to(PrplLockListVo.class);
		return prplLockListVo;
	}

	@Override
	public void deletePrplLockListById(Long yWMainId, String lockType) {
		PrplLockListVo prplLockListVo = this.findPrplLockListById(yWMainId, lockType);
		if(prplLockListVo != null && prplLockListVo.getId() != null){
			databaseDao.deleteByPK(PrplLockList.class,prplLockListVo.getId());
		}
	}
	
	
}
