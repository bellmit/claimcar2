package ins.sino.claimcar.endcase.service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.support.Page;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.endcase.vo.PrpLReCaseVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.WfTaskQueryResultVo;

import java.util.List;

public interface ReOpenQueryService {

	/**
	 * 发起任务查询
	 * @param endCaseVo
	 * @param wftaskqueryvo
	 * @param start
	 * @param length
	 * @return
	 */
	public ResultPage<WfTaskQueryResultVo> findResultVo(
			PrpLEndCaseVo endCaseVo, PrpLWfTaskQueryVo wftaskqueryvo,
			int start, int length);

	public ResultPage<WfTaskQueryResultVo> search(
			PrpLEndCaseVo endCaseVo, PrpLWfTaskQueryVo wftaskqueryvo,
			int start, int length);

	public ResultPage<PrpLReCaseVo> searchProcessed(PrpLEndCaseVo endCaseVo,
			PrpLWfTaskQueryVo wftaskqueryvo, String handleStatus, int start,
			int length);

	/**
	 * 查询“已处理”案件
	 * @param endCaseVo
	 * @param wftaskqueryvo
	 * @param handleStatus
	 * @param start
	 * @param length
	 * @return
	 */
	public List<PrpLReCaseVo> findProcessed(PrpLEndCaseVo endCaseVo,
			PrpLWfTaskQueryVo wftaskqueryvo, String handleStatus, int start,
			int length);

	/**
	 * 根据立案号判断是否有重开的赔案未结案
	 * @param claimNo
	 * @return
	 */
	public boolean notExistsForReOpen(String claimNo);

	/**
	 * 根据结案号查询PrpLReCase表
	 * @param endCaseNo
	 * @return
	 */


	/**
	 * 根据立案号统计重开赔案次数
	 * @param claimNo
	 * @return
	 */
	public String countReCaseByClaimNo(String claimNo);

	/**
	 * 根据立案号查询重开赔案案件
	 * @param claimNo
	 * @return
	 */
	public List<PrpLReCaseVo> findReCaseByClaimNo(String claimNo);

	/**
	 * 查询旧理赔重开案子
	 * @param endCaseVo
	 * @param wftaskqueryvo
	 * @param start
	 * @param length
	 * @return
	 */
	public ResultPage<WfTaskQueryResultVo> searchOldClaim(PrpLEndCaseVo endCaseVo,PrpLWfTaskQueryVo wftaskqueryvo,
			int start, int length);
}