package ins.sino.claimcar.base.claimyj.service;

import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.base.claimyj.po.PrpLDlchkInfo;
import ins.sino.claimcar.base.claimyj.po.PrpLDlhkMain;
import ins.sino.claimcar.claimcarYJ.vo.PrpLDlhkMainVo;
import ins.sino.claimcar.claimyj.service.YjPrpLDlhkMainService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;

/**
 * 阳杰接收接口
 */

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path(value = "yjPrpLDlhkMainService")
public class YjPrpLDlhkMainServiceImpl  implements YjPrpLDlhkMainService {

	@Autowired
	DatabaseDao databaseDao;
	@Override
	public void savePrpLDlhkMain(PrpLDlhkMainVo prpLDlhkMainVo) {
		PrpLDlhkMain mainPo = Beans.copyDepth().from(prpLDlhkMainVo).to(PrpLDlhkMain.class);
		if(mainPo.getPrpLDlchkInfos() != null && mainPo.getPrpLDlchkInfos().size() > 0){
			for(PrpLDlchkInfo po : mainPo.getPrpLDlchkInfos()){
				po.setPrpLDlhkMain(mainPo);
			}
		}
		databaseDao.save(PrpLDlhkMain.class, mainPo);
	}
	@Override
	public List<PrpLDlhkMainVo> findPrpLDlhkMains(String registNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("notificationNo",registNo);
		List<PrpLDlhkMain> prpLDlhkMains = databaseDao.findAll
				(PrpLDlhkMain.class,queryRule);
		List<PrpLDlhkMainVo> prpLDlhkMainVoList = null;
		if(prpLDlhkMains != null && prpLDlhkMains.size() > 0){
			prpLDlhkMainVoList = Beans.copyDepth().from(prpLDlhkMains).toList(PrpLDlhkMainVo.class);
		}
		return prpLDlhkMainVoList;
	}
	@Override
	public List<PrpLDlhkMainVo> findPrpLDlhkMainsBytopActualId(
			String topActualId) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("topActualId",topActualId);
		List<PrpLDlhkMain> prpLDlhkMains = databaseDao.findAll
				(PrpLDlhkMain.class,queryRule);
		List<PrpLDlhkMainVo> prpLDlhkMainVoList = null;
		if(prpLDlhkMains != null && prpLDlhkMains.size() > 0){
			prpLDlhkMainVoList = Beans.copyDepth().from(prpLDlhkMains).toList(PrpLDlhkMainVo.class);
		}
		return prpLDlhkMainVoList;
	}

}
