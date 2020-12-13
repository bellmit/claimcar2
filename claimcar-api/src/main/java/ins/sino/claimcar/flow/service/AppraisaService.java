package ins.sino.claimcar.flow.service;





import ins.framework.dao.database.support.Page;



import ins.sino.claimcar.manager.vo.PrpdAppraisaVo;

public interface AppraisaService {

	/*
	 * 查询所有伤残鉴定机构
	 */
	public abstract Page<PrpdAppraisaVo> findAllAppraisa(
			PrpdAppraisaVo prpdAppraisaVo, int start, int length);
	
	public abstract PrpdAppraisaVo findAppraisaById(long id);
	
	/*
	 * 异步查询appraisaCode是否在数据库中存在
	 */
	
	public abstract boolean findAppraisaCode(String appraisaCode);
	
	/**
	 * 保存或更新伤残鉴定机构
	 * @param prpdAppraisaVo
	 * @param userCode
	 */
	public abstract void savaOrUpDate(PrpdAppraisaVo prpdAppraisaVo,
			 String userCode);
	public abstract String findAppraisaName(String appraisaCode);
		
}
