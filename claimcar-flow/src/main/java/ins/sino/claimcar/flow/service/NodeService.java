package ins.sino.claimcar.flow.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.flow.po.PrpDNode;
import ins.sino.claimcar.flow.vo.PrpDNodeVo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "nodeService")
public class NodeService {
	@Autowired
	private DatabaseDao databaseDao;
	
	public List<PrpDNodeVo> findeNode(String uppernode,String currencyNode,String nextFlag) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("upperNode",uppernode);
		if(CodeConstants.AuditStatus.AUDIT.equals(nextFlag)){
			queryRule.addGreaterThan("nodeCode",currencyNode);
		}
		if(CodeConstants.AuditStatus.BACKLOWER.equals(nextFlag)){
			queryRule.addLessThan("nodeCode",currencyNode);
		}
		queryRule.addAscOrder("serialno");
		
		List<PrpDNode> prpdNodeList = databaseDao.findAll(PrpDNode.class,queryRule);
		List<PrpDNodeVo> nodeVoList = Beans.copyDepth().from(prpdNodeList).toList(PrpDNodeVo.class);
		
		return nodeVoList;
	}

}
