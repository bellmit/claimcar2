/******************************************************************************
* CREATETIME : 2016年6月27日 下午10:40:43
******************************************************************************/
package test.flow;

import ins.framework.common.ResultPage;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.WfTaskQueryResultVo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import base.BaseTestTrueRollback;

/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年6月27日
 */
public class WfTaskQueryServiceTest extends BaseTestTrueRollback {

	@Autowired
	private WfTaskQueryService wfTaskQueryService;

	@Test
	public void findTaskForPage() throws Exception {
		PrpLWfTaskQueryVo taskQueryVo = new PrpLWfTaskQueryVo();
		taskQueryVo.setHandleStatus(HandlerStatus.INIT);
		taskQueryVo.setNodeCode(FlowNode.Claim.name());
		ResultPage<WfTaskQueryResultVo> resultPage = wfTaskQueryService.findTaskForPage(taskQueryVo);
		System.out.println("===result==="+resultPage.getTotalCount());
	}
}
