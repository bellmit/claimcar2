package ins.sino.claimcar.regist.service;

import ins.framework.common.ResultPage;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

public interface RegistTaskQueryService {

	/*	public void save(PrpLWfprpLRegistVo prpLRegistVo) {
			PrpLWfTaskQuery po = new PrpLWfTaskQuery();
			Beans.copy().from(prpLRegistVo).to(po);
			databaseDao.save(PrpLWfTaskQuery.class,po);
			logger.debug("WfTaskQueryService.po.id="+po.getFlowID());
		}*/
	//PrpLRegistVo prpLRegistVo
	/**
	 * 所有节点的任务查询
	 * @param prpLRegistVo
	 * @return
	 * @throws Exception
	 * @modified: ☆LiuPing(2016年1月10日 下午7:47:42): <br>
	 */
	public ResultPage<PrpLRegistVo> findTaskForPage(PrpLRegistVo prpLRegistVo,
			Integer start, Integer length, String keyProperty) throws Exception;

}