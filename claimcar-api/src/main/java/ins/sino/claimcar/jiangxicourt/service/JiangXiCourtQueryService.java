package ins.sino.claimcar.jiangxicourt.service;

import ins.sino.claimcar.jiangxicourt.vo.Data;

import java.util.List;

public interface JiangXiCourtQueryService {

	/**
	 * 通过保单号查询结果
	 * @return
	 */
	public List<Data> findByPolicyNo(String taskId, String policyNo);
	
}
