/******************************************************************************
* CREATETIME : 2016年2月26日 上午11:50:22
******************************************************************************/
package ins.sino.claimcar.flow.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.po.PrpLWfTaskIn;
import ins.sino.claimcar.flow.po.PrpLWfTaskOut;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.dubbo.config.annotation.Service;


/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年2月26日
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path(value = "wfFlowQueryService")
public class WfFlowQueryServiceSpringImpl implements WfFlowQueryService {

	@Autowired
	private DatabaseDao databaseDao;

	/* 
	 * @see ins.sino.claimcar.flow.service.WfFlowQueryService#findUnAcceptTask(java.lang.String, ins.sino.claimcar.flow.constant.FlowNode)
	 * @param registNo
	 * @param nodeCode
	 * @return
	 */
	@Override
	public List<PrpLWfTaskVo> findUnAcceptTask(String registNo,String... nodeCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		/*queryRule.addEqual("taskInNode",nodeCode.name());*/
		queryRule.addEqual("handlerStatus",HandlerStatus.INIT);
		queryRule.addIn("nodeCode",nodeCode);

		List<PrpLWfTaskIn> taskInPoList = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);

		List<PrpLWfTaskVo> prpLWfTaskInVos = Beans.copyDepth().from(taskInPoList).toList(PrpLWfTaskVo.class);
		return prpLWfTaskInVos;
	}

	@Override
	public boolean isCheckNodeEnd(String registNo) {
		boolean result = false;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("handlerStatus",HandlerStatus.END);
		queryRule.addEqual("workStatus",WorkStatus.END);
		queryRule.addEqual("nodeCode",FlowNode.Check.name());
		queryRule.addEqual("subNodeCode",FlowNode.Chk.name());

		List<PrpLWfTaskOut> taskInPoList = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);

		if(taskInPoList != null && taskInPoList.size() > 0){
			result = true;
		}
		return result;
	}

	@Override
	public List<PrpLWfTaskVo> findPrpWfTaskVo(String registNo,
			String... nodeCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addIn("nodeCode",nodeCode);
		List<PrpLWfTaskIn> taskInPoList = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);

		List<PrpLWfTaskVo> prpLWfTaskInVos = Beans.copyDepth().from(taskInPoList).toList(PrpLWfTaskVo.class);
		return prpLWfTaskInVos;
	}
//查询单证数据
	@Override
	public List<PrpLWfTaskVo> findCertiByRegistNo(String registNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("nodeCode","Certi");
		List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		if(prpLWfTaskIns==null||prpLWfTaskIns.size()==0){
			List<PrpLWfTaskOut> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
			List<PrpLWfTaskVo> prpLWfTaskVo = Beans.copyDepth().from(prpLWfTaskOuts).toList(PrpLWfTaskVo.class);
			return prpLWfTaskVo;
		}else{
		List<PrpLWfTaskVo> prpLWfTaskVo = Beans.copyDepth().from(prpLWfTaskIns).toList(PrpLWfTaskVo.class);
		return prpLWfTaskVo;
		}
	}
	
	/**
	 * workStatus 0 表示查询 PrpLWfTaskIn表  ，1 表示查询 已处理的数据 PrpLWfTaskout表 
	 * @param registNo
	 * @param subNode
	 * @param workStatus
	 * @return
	 * @modified:
	 * ☆YangKun(2016年7月15日 下午3:54:54): <br>
	 */
	@Override
	public List<PrpLWfTaskVo> findWfTaskVo(String registNo,FlowNode subNode,String workStatus) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("subNodeCode",subNode.name());
		queryRule.addDescOrder("taskId");
		
		List<PrpLWfTaskVo> wfTaskVo = new ArrayList<PrpLWfTaskVo>();
		if("0".equals(workStatus)){
			List<PrpLWfTaskIn> taskInPoList = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
			wfTaskVo = Beans.copyDepth().from(taskInPoList).toList(PrpLWfTaskVo.class);
		}else{
			List<PrpLWfTaskOut> taskInPoList = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
			wfTaskVo = Beans.copyDepth().from(taskInPoList).toList(PrpLWfTaskVo.class);
		}
		
		return wfTaskVo;
	}

	@Override
	public List<PrpLWfTaskVo> findPrpWfTaskVo(String registNo, String nodeCode,
			String subNodeCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("nodeCode",nodeCode);
		queryRule.addEqual("subNodeCode",subNodeCode);
		List<PrpLWfTaskVo> wfTaskVo = new ArrayList<PrpLWfTaskVo>();
		List<PrpLWfTaskVo> wfTaskInVo = new ArrayList<PrpLWfTaskVo>();
		List<PrpLWfTaskVo> wfTaskOutVo = new ArrayList<PrpLWfTaskVo>();
		List<PrpLWfTaskIn> taskInPoList = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		if(taskInPoList != null){
			wfTaskInVo = Beans.copyDepth().from(taskInPoList).toList(PrpLWfTaskVo.class);
		}
		queryRule.addDescOrder("taskOutTime");
		List<PrpLWfTaskOut> taskOutPoList = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
		if(taskOutPoList != null){
			wfTaskOutVo = Beans.copyDepth().from(taskOutPoList).toList(PrpLWfTaskVo.class);
		}
		wfTaskVo.addAll(wfTaskOutVo);
		wfTaskVo.addAll(wfTaskInVo);
		return wfTaskVo;
	}

	@Override
	public List<PrpLWfTaskVo> findPrpWfTaskVoForOut(String registNo,
			String... nodeCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addIn("nodeCode",nodeCode);
		queryRule.addDescOrder("taskInTime");
		List<PrpLWfTaskOut> taskOutPoList = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);

		List<PrpLWfTaskVo> prpLWfTaskInVos = Beans.copyDepth().from(taskOutPoList).toList(PrpLWfTaskVo.class);
		return prpLWfTaskInVos;
	}
	
	@Override
	public List<PrpLWfTaskVo> findTaskVoForInAndOut(String registNo,
			String nodeCode, String... taskInNode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("nodeCode",nodeCode);
		queryRule.addIn("taskInNode",taskInNode);
		List<PrpLWfTaskVo> wfTaskVo = new ArrayList<PrpLWfTaskVo>();
		List<PrpLWfTaskVo> wfTaskInVo = new ArrayList<PrpLWfTaskVo>();
		List<PrpLWfTaskVo> wfTaskOutVo = new ArrayList<PrpLWfTaskVo>();
		List<PrpLWfTaskIn> taskInPoList = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		if(taskInPoList != null){
			wfTaskInVo = Beans.copyDepth().from(taskInPoList).toList(PrpLWfTaskVo.class);
		}
		List<PrpLWfTaskOut> taskOutPoList = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
		if(taskOutPoList != null){
			wfTaskOutVo = Beans.copyDepth().from(taskOutPoList).toList(PrpLWfTaskVo.class);
		}
		wfTaskVo.addAll(wfTaskInVo);
		wfTaskVo.addAll(wfTaskOutVo);
		return wfTaskVo;
	}

	@Override
	public List<PrpLWfTaskVo> findTaskVoForOutBySubNodeCode(String registNo,
			String subNodeCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("subNodeCode",subNodeCode);
		queryRule.addDescOrder("taskOutTime");
		List<PrpLWfTaskVo> wfTaskVo = new ArrayList<PrpLWfTaskVo>();
		List<PrpLWfTaskVo> wfTaskOutVo = new ArrayList<PrpLWfTaskVo>();
		List<PrpLWfTaskOut> taskOutPoList = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
		if(taskOutPoList != null){
			wfTaskOutVo = Beans.copyDepth().from(taskOutPoList).toList(PrpLWfTaskVo.class);
		}
		wfTaskVo.addAll(wfTaskOutVo);
		return wfTaskVo;
	}

	@Override
	public List<PrpLWfTaskVo> findTaskVoForInAndOutByRegistNo(String registNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLWfTaskVo> wfTaskVo = new ArrayList<PrpLWfTaskVo>();
		List<PrpLWfTaskVo> wfTaskInVo = new ArrayList<PrpLWfTaskVo>();
		List<PrpLWfTaskVo> wfTaskOutVo = new ArrayList<PrpLWfTaskVo>();
		List<PrpLWfTaskIn> taskInPoList = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		if(taskInPoList != null){
			wfTaskInVo = Beans.copyDepth().from(taskInPoList).toList(PrpLWfTaskVo.class);
		}
		QueryRule queryRuleOut = QueryRule.getInstance();
		queryRuleOut.addEqual("registNo",registNo);
		List<PrpLWfTaskOut> taskOutPoList = databaseDao.findAll(PrpLWfTaskOut.class,queryRuleOut);
		if(taskOutPoList != null){
			wfTaskOutVo = Beans.copyDepth().from(taskOutPoList).toList(PrpLWfTaskVo.class);
		}
		wfTaskVo.addAll(wfTaskOutVo);
		wfTaskVo.addAll(wfTaskInVo);
		
		Collections.sort(wfTaskVo, new Comparator<PrpLWfTaskVo>() {
	        @Override
	        public int compare(PrpLWfTaskVo o1,PrpLWfTaskVo o2) {
	                return o1.getTaskId().compareTo(o2.getTaskId());
	            }
	        });
		return wfTaskVo;
	}

	/*@Override
	public List<PrpLWfTaskVo> findPrpWfTaskVoForOutBySub(String registNo,
			String... subNodeCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addIn("nodeCode",subNodeCode);
		queryRule.addDescOrder("taskInTime");
		List<PrpLWfTaskOut> taskOutPoList = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);

		List<PrpLWfTaskVo> prpLWfTaskOutVos = Beans.copyDepth().from(taskOutPoList).toList(PrpLWfTaskVo.class);
		
		List<PrpLWfTaskIn> taskInPoList = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);

		List<PrpLWfTaskVo> prpLWfTaskInVos = Beans.copyDepth().from(taskInPoList).toList(PrpLWfTaskVo.class);
		if(prpLWfTaskOutVos.size()>0){
			if(prpLWfTaskInVos.size()>0){
				if(prpLWfTaskOutVos.get(0).getTaskInTime() > prpLWfTaskInVos.get(0).getTaskInTime()){
					
				}
			}
		}else{
			if(prpLWfTaskInVos.size()>0){
						
			}
		}
		return prpLWfTaskInVos;
	}*/

	@Override
	public List<PrpLWfTaskVo> findCancelByRegistNo(String registNo){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addIn("nodeCode",FlowNode.Check.name(),FlowNode.DLoss.name());
		queryRule.addIn("workStatus", CodeConstants.WorkStatus.CANCEL,CodeConstants.WorkStatus.PAUSE);
		List<PrpLWfTaskOut> taskOutPoList = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
		List<PrpLWfTaskVo> wfTaskVoList = new ArrayList<PrpLWfTaskVo>();
		if(taskOutPoList != null){
			wfTaskVoList = Beans.copyDepth().from(taskOutPoList).toList(PrpLWfTaskVo.class);
		}
		return wfTaskVoList;
	}

    @Override
    public List<PrpLWfTaskVo> findTaskVoForOutByNodeCode(String registNo,String nodeCode) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        queryRule.addEqual("nodeCode",nodeCode);
        queryRule.addDescOrder("taskOutTime");
        List<PrpLWfTaskVo> wfTaskVo = new ArrayList<PrpLWfTaskVo>();
        List<PrpLWfTaskVo> wfTaskOutVo = new ArrayList<PrpLWfTaskVo>();
        List<PrpLWfTaskOut> taskOutPoList = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
        if(taskOutPoList != null){
            wfTaskOutVo = Beans.copyDepth().from(taskOutPoList).toList(PrpLWfTaskVo.class);
        }
        wfTaskVo.addAll(wfTaskOutVo);
        return wfTaskVo;
    }

    @Override
    public List<PrpLWfTaskVo> findTaskVoForInByNodeCode(String registNo,String nodeCode) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        queryRule.addEqual("nodeCode",nodeCode);
        List<PrpLWfTaskVo> wfTaskVo = new ArrayList<PrpLWfTaskVo>();
        List<PrpLWfTaskVo> wfTaskInVo = new ArrayList<PrpLWfTaskVo>();
        List<PrpLWfTaskIn> taskInPoList = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
        if(taskInPoList != null){
            wfTaskInVo = Beans.copyDepth().from(taskInPoList).toList(PrpLWfTaskVo.class);
        }
        wfTaskVo.addAll(wfTaskInVo);
        return wfTaskVo;
    }

    @Override
    public List<PrpLWfTaskVo> findPrpWfTaskVoForIn(String registNo,String nodeCode,String subNodeCode) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        queryRule.addEqual("nodeCode",nodeCode);
        queryRule.addEqual("subNodeCode",subNodeCode);
        queryRule.addDescOrder("taskInTime");
        List<PrpLWfTaskVo> wfTaskInVo = new ArrayList<PrpLWfTaskVo>();
        List<PrpLWfTaskIn> taskInPoList = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
        if(taskInPoList != null){
            wfTaskInVo = Beans.copyDepth().from(taskInPoList).toList(PrpLWfTaskVo.class);
        }
        return wfTaskInVo;
    }
    
	@Override
	public List<PrpLWfTaskVo> findTaskVoForQueryVo(PrpLWfTaskVo WfQueryVo,String inOutFlag) {
		Assert.notNull(WfQueryVo.getRegistNo(),"查询报案号不能为空");
		
		List<PrpLWfTaskVo> wfTaskVos = new ArrayList<PrpLWfTaskVo>();
		
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",WfQueryVo.getRegistNo());
		queryRule.addEqualIfExist("subNodeCode",WfQueryVo.getSubNodeCode());
		queryRule.addEqualIfExist("workStatus",WfQueryVo.getWorkStatus());
		
		if("0".equals(inOutFlag)){
			queryRule.addDescOrder("taskInTime");
			List<PrpLWfTaskIn> taskInPoList = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
			wfTaskVos = Beans.copyDepth().from(taskInPoList).toList(PrpLWfTaskVo.class);
		}else{
			queryRule.addDescOrder("taskOutTime");
			List<PrpLWfTaskOut> taskInPoList = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
			wfTaskVos = Beans.copyDepth().from(taskInPoList).toList(PrpLWfTaskVo.class);
		}
		
		return wfTaskVos;
	}

	@Override
	public List<PrpLWfTaskVo> findTaskVoForInByClaimNo(String claimNo,String nodeCode) {
		  QueryRule queryRule = QueryRule.getInstance();
	        queryRule.addEqual("claimNo",claimNo);
	        queryRule.addEqual("nodeCode",nodeCode);
	        List<PrpLWfTaskVo> wfTaskInVo = new ArrayList<PrpLWfTaskVo>();
	        List<PrpLWfTaskIn> taskInPoList = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
	        if(taskInPoList != null){
	            wfTaskInVo = Beans.copyDepth().from(taskInPoList).toList(PrpLWfTaskVo.class);
	        }
	        return wfTaskInVo;
	}

	/* 
	 * @see ins.sino.claimcar.flow.service.WfFlowQueryService#findOtherPrpWfTaskVo(java.lang.String, java.lang.String)
	 * @param registNo
	 * @param flowId
	 * @return
	 */
	@Override
	public List<PrpLWfTaskVo> findOtherPrpWfTaskVo(String registNo,Long flowTaskId) {
		  QueryRule queryRule = QueryRule.getInstance();
	        queryRule.addEqual("registNo",registNo);
	        queryRule.addNotEqual("taskId",new BigDecimal(flowTaskId));
	        List<PrpLWfTaskVo> wfTaskInVo = new ArrayList<PrpLWfTaskVo>();
	        List<PrpLWfTaskIn> taskInPoList = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
	        if(taskInPoList != null){
	            wfTaskInVo = Beans.copyDepth().from(taskInPoList).toList(PrpLWfTaskVo.class);
	        }
	        return wfTaskInVo;
	}
    
}
