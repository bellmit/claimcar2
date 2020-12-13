package ins.sino.claimcar.base.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.base.po.PrpLwarnInfo;
import ins.sino.claimcar.claim.service.SdwarnService;
import ins.sino.claimcar.claim.vo.PrpLwarnInfoVo;
@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" }, timeout = 1000000)
@Path("SdwarnService")
public class SdwarnServiceImpl implements SdwarnService{
	@Autowired
	DatabaseDao databaseDao;
	
	@Override
	public void savePrplwarninfo(List<PrpLwarnInfoVo> prpLwarnInfoVos) {
		if(prpLwarnInfoVos!=null && prpLwarnInfoVos.size()>0){
		  List<PrpLwarnInfo> prpLwarnInfos=new ArrayList<PrpLwarnInfo>();
		  prpLwarnInfos=Beans.copyDepth().from(prpLwarnInfoVos).toList(PrpLwarnInfo.class);
		  databaseDao.saveAll(PrpLwarnInfo.class, prpLwarnInfos);
		}
		
	}

	@Override
	public List<PrpLwarnInfoVo> findPrpLwarnInfoVosByRegistNo(String registNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addAscOrder("createTime");
		List<PrpLwarnInfo> prpLwarnInfos=databaseDao.findAll(PrpLwarnInfo.class, queryRule);
		List<PrpLwarnInfoVo> prpLwarnInfoVos=new ArrayList<PrpLwarnInfoVo>();
		if(prpLwarnInfos!=null && prpLwarnInfos.size()>0){
			prpLwarnInfoVos=Beans.copyDepth().from(prpLwarnInfos).toList(PrpLwarnInfoVo.class);
		}
		return prpLwarnInfoVos;
	}

	@Override
	public List<PrpLwarnInfoVo> findwarnVosByclaimsequenceNo(String claimsequenceNo,String warnstageCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("claimsequenceNo",claimsequenceNo);
		queryRule.addEqual("warnstageCode",warnstageCode);
		queryRule.addAscOrder("createTime");
		List<PrpLwarnInfo> prpLwarnInfos=databaseDao.findAll(PrpLwarnInfo.class, queryRule);
		List<PrpLwarnInfoVo> prpLwarnInfoVos=new ArrayList<PrpLwarnInfoVo>();
		if(prpLwarnInfos!=null && prpLwarnInfos.size()>0){
			prpLwarnInfoVos=Beans.copyDepth().from(prpLwarnInfos).toList(PrpLwarnInfoVo.class);
		}
		return prpLwarnInfoVos;
	}

}
