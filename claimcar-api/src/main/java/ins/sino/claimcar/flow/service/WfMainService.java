package ins.sino.claimcar.flow.service;

import ins.sino.claimcar.flow.vo.PrpLWfMainVo;

public interface WfMainService {

	public abstract void save(PrpLWfMainVo wfMainVo);

	/**
	 * 更新mian表数据
	 * @param wfMainVo
	 * @modified: ☆LiuPing(2016年1月11日 ): <br>
	 */
	public abstract void update(PrpLWfMainVo wfMainVo);

	/**
	 * 根据flowId查找prpLWfMainVo
	 * @param flowId
	 * @return
	 */
	public abstract PrpLWfMainVo findPrpLWfMainVoByFlowId(String flowId);
	
	/**
	 * 根据报案号查找 PrpLWfMainVo
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * *牛强(2017年6月9日 上午8:41:29): <br>
	 */
	public abstract PrpLWfMainVo  findPrpLWfMainVoByRegistNo(String registNo);
	
	/**
	 * 根据车牌号查询未完成案件
	 * <pre></pre>
	 * @param licenseNo
	 * @return
	 * @modified:
	 * ☆LinYi(2017年9月14日 上午9:15:08): <br>
	 */
	public PrpLWfMainVo findPrpLWfMainVoByLicenseNo(String licenseNo);

}
