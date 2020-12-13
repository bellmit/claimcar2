/******************************************************************************
* CREATETIME : 2016年1月9日 上午10:12:25
******************************************************************************/
package ins.sino.claimcar.flow.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.flow.po.PrpLWfNode;
import ins.sino.claimcar.flow.service.WfNodeService;
import ins.sino.claimcar.flow.vo.PrpLWfNodeVo;
import ins.sino.claimcar.flow.vo.WfFlowNodeShowVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年1月9日
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path(value = "wfNodeService")
public class WfNodeServiceImpl implements WfNodeService {

	private static Logger logger = LoggerFactory.getLogger(WfNodeServiceImpl.class);

	@Autowired
	private DatabaseDao databaseDao;

	/* 
	 * @see ins.sino.claimcar.flow.service.WfNodeService#save(ins.sino.claimcar.flow.vo.PrpLWfNodeVo)
	 * @param wfNodeVo
	 */
	@Override
	public void save(PrpLWfNodeVo wfNodeVo) {
		PrpLWfNode po = findPrpLWfNode(wfNodeVo.getFlowId(),wfNodeVo.getNodeCode());
		if(po==null){
			po = new PrpLWfNode();
			Beans.copy().from(wfNodeVo).to(po);
			databaseDao.save(PrpLWfNode.class,po);
		}else{
			wfNodeVo.setId(null);
			Beans.copy().from(wfNodeVo).excludeNull().excludeEmpty().to(po);
			databaseDao.update(PrpLWfNode.class,po);
		}

		logger.debug("WfNodeService.po.id="+po.getId());
	}

	private PrpLWfNode findPrpLWfNode(String flowId,String nodeCode) {
		String HQL = "FROM PrpLWfNode pn WHERE  pn.flowId = ? AND pn.nodeCode = ? ";
		List<Object> paramValues = new ArrayList<Object>();
		paramValues.add(flowId);
		paramValues.add(nodeCode);
		return databaseDao.findUniqueByHql(PrpLWfNode.class,HQL,paramValues.toArray());
	}


	public void updatePrpLWfNode(PrpLWfNode prpLWfNode) {
		databaseDao.update(PrpLWfNode.class,prpLWfNode);
	}

	/* 
	 * @see ins.sino.claimcar.flow.service.WfNodeService#update(ins.sino.claimcar.flow.vo.PrpLWfNodeVo)
	 * @param schedNodeVo
	 */
	@Override
	public void update(PrpLWfNodeVo schedNodeVo) {
		String hql = "FROM PrpLWfNode pn WHERE pn.flowId=? AND  pn.nodeCode = ? ";
		PrpLWfNode po = databaseDao.findUniqueByHql(PrpLWfNode.class,hql,schedNodeVo.getFlowId(),schedNodeVo.getNodeCode());
		Beans.copy().from(schedNodeVo).excludeNull().excludeEmpty().to(po);
		databaseDao.update(PrpLWfNode.class,po);
	}
	
	/* 
	 * @see ins.sino.claimcar.flow.service.WfNodeService#findWfFlowNodeShowList(java.lang.String)
	 * @param flowId
	 * @return
	 */
	@Override
	public List<WfFlowNodeShowVo> findWfFlowNodeShowList(String flowId){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("flowId",flowId);
		queryRule.addAscOrder("nodeInTime");//id排序
		List<PrpLWfNode> prpLWfNodeList = databaseDao.findAll(PrpLWfNode.class,queryRule);
		List<WfFlowNodeShowVo> nodeList = new ArrayList<WfFlowNodeShowVo>();
		nodeList = Beans.copyDepth().from(prpLWfNodeList).toList(WfFlowNodeShowVo.class);
		return nodeList;
	}

    @Override
    public void delete(BigDecimal id) {
        databaseDao.deleteByPK(PrpLWfNode.class,id);
    }

    @Override
    public List<PrpLWfNodeVo> findBydFlowIdAndNodeCode(String flowId,String nodeCode) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("flowId",flowId);
        queryRule.addEqual("nodeCode",nodeCode);
        List<PrpLWfNode> prpLWfNodeList = databaseDao.findAll(PrpLWfNode.class,queryRule);
        List<PrpLWfNodeVo> nodeVoList = new ArrayList<PrpLWfNodeVo>();
        nodeVoList = Beans.copyDepth().from(prpLWfNodeList).toList(PrpLWfNodeVo.class);
        return nodeVoList;
    }
	
	

}
