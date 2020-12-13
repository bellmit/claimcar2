/******************************************************************************
* CREATETIME : 2016年1月13日 下午3:34:33
******************************************************************************/
package ins.sino.claimcar.dloss.lossindex.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.common.lossindex.service.LossIndexService;
import ins.sino.claimcar.common.lossindex.vo.PrpLDlossIndexVo;
import ins.sino.claimcar.dloss.lossindex.po.PrpLDlossIndex;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;


/**
 * <pre></pre>
 * @author ★yangkun
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("lossIndexService")
public class LossIndexServiceImpl implements LossIndexService {
	
	@Autowired
	DatabaseDao databaseDao;
	
	public void saveOrUpdte(PrpLDlossIndexVo lossIndexVo){
		PrpLDlossIndex lossIndex = this.findLossIndexPo(lossIndexVo.getBussTaskId(),lossIndexVo.getNodeCode());
		if(lossIndex == null){
			lossIndex = new PrpLDlossIndex();
		}
		
		Beans.copy().from(lossIndexVo).to(lossIndex);
		
		databaseDao.save(PrpLDlossIndex.class,lossIndex);
		
	}
	
	private PrpLDlossIndex findLossIndexPo(Long bussTaskId,String nodeCode){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("bussTaskId",bussTaskId);
		queryRule.addEqual("nodeCode",nodeCode);
		
		PrpLDlossIndex lossIndex = databaseDao.findUnique(PrpLDlossIndex.class,queryRule);
		
		return lossIndex;
	}
	
	public PrpLDlossIndexVo findLossIndex(Long bussTaskId,String nodeCode){
		PrpLDlossIndex lossIndex = this.findLossIndexPo(bussTaskId,nodeCode);
		if(lossIndex == null){
			return null;
		}
		PrpLDlossIndexVo lossIndexVo = new PrpLDlossIndexVo(); 
		Beans.copy().from(lossIndex).to(lossIndexVo);
		
		return lossIndexVo;
	}
}
