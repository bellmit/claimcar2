/******************************************************************************
* CREATETIME : 2015年11月26日 上午11:49:48
******************************************************************************/
package ins.sino.claimcar.check.service;

import java.util.List;
import ins.sino.claimcar.check.po.PrpLCheckTask;

/**
 * <pre>复勘服务类</pre>
 * @author ★Luwei
 */
public interface VerifyCheckService {
	
	/**  **/
	public List<PrpLCheckTask> findVerifyPrpLcheckTasks(String registNo);
		
}
