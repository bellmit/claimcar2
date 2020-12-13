package ins.sino.claimcar.lossprop.service;

import ins.framework.dao.database.support.QueryRule;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;

import java.math.BigDecimal;
import java.util.List;

public interface PropTaskService {
	public List<PrpLdlossPropMainVo> findPropMainListByRegistNo(String registNo);
	
	public PrpLdlossPropMainVo findPropMainVoById(Long id);

	/**	更新财产损失信息
	 * @param dlossPropMain
	 * @return
	 * @modified:
	 * ☆Luwei(2016年4月21日 下午7:17:39): <br>
	 */
	public void updateDLossProp(PrpLdlossPropMainVo dlossPropMain);
	
	/**
	 * 根据条件查询数据
	 * <pre></pre>
	 * @param registNo
	 * @param lossState
	 * @param underWriteFlag
	 * @param serialNo
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年4月26日 下午3:57:10): <br>
	 */
	public List<PrpLdlossPropMainVo> findPrpLdlossPropMainVoListByCondition(String registNo,List<String> lossState,List<String> underWriteFlag,String serialNo);
	
	/**
	 * 根据QueryRule查找记录
	 * <pre></pre>
	 * @param queryRule
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年4月27日 下午5:03:55): <br>
	 */
	public List<PrpLdlossPropMainVo> findPrpLdlossPropMainVoListByRule(QueryRule queryRule);
	
	/**
	 * 报案号下定损是否全部核损通过true代表全部核损通过，false代表没有全部核损通过
	 * @param registNo
	 * @return
	 */
	public boolean isDLossAllPassed(String registNo);
	public String propModifyLaunch(Long lossId,SysUserVo sysUserVo);
	
	public String propAdditionLaunch(Long id,SysUserVo userVo,String licenseNo,String remarks);
	
	/**
	 * 
	 * <pre></pre>
	 * @param PrpLdlossPropMainList
	 * @modified:
	 * *牛强(2017年2月16日 上午9:38:09): <br>
	 */
	public void updateDLossPropByRegistNo(List<PrpLdlossPropMainVo> PrpLdlossPropMainList);
	
	/**
	 * 根据报案号和underWriteFlag查询物损，flag=1等于underWriteFlag，flag=0不等于underWriteFlag
	 * @param registNo
	 * @param underWriteFlag
	 * @param flag
	 * @return
	 */
	public List<PrpLdlossPropMainVo> findlossPropMainByUnderWriteFlag(String registNo,String underWriteFlag,String flag);
	
}
