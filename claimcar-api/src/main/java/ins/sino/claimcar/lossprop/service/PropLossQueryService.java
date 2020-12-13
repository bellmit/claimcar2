package ins.sino.claimcar.lossprop.service;

import ins.framework.common.ResultPage;
import ins.sino.claimcar.lossprop.vo.PropQueryReslutVo;
import ins.sino.claimcar.lossprop.vo.PropQueryVo;

public interface PropLossQueryService {

	/**
	 * 查询可以定损修改的定损任务
	 * @param queryVo
	 * @return
	 * @modified: ☆LiuPing(2016年2月3日 ): <br>
	 */
	public abstract ResultPage<PropQueryReslutVo> findPageForAdjust(
			PropQueryVo queryVo) throws Exception;

}