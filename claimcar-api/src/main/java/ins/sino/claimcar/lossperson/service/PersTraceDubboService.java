/******************************************************************************
 * CREATETIME : 2016年1月6日 上午9:05:28
 ******************************************************************************/
package ins.sino.claimcar.lossperson.service;

import ins.framework.dao.database.support.QueryRule;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersInjuredVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;

import java.util.List;

/**
 * <pre></pre>
 * @author ★XMSH
 */
public interface PersTraceDubboService {

	public List<PrpLDlossPersInjuredVo> findPersInjuredByRegistNo(String registNo);
	
	public List<PrpLDlossPersTraceMainVo> findPersTraceMainVoList(String registNo);

	public PrpLDlossPersInjuredVo findPersInjuredByPK(Long id);
	
	public PrpLDlossPersTraceVo findPersTraceByPK(Long id); 
	
	public PrpLDlossPersTraceMainVo findPersTraceMainByPk(Long id);
	
	public List<PrpLDlossPersTraceVo> findPrpLDlossPersTraceVoListByRegistNo(String registNo);
	public List<PrpLDlossPersTraceMainVo> findPrpLDlossPersTraceMainVoListByRegistNoDesc(String registNo);
	
	/**	核赔-回写人伤
	 * @param DlossPersTraceMainVo
	 * @modified:
	 * ☆Luwei(2016年4月21日 下午7:50:44): <br>
	 */
	public void writeBackDLossPerson(PrpLDlossPersTraceMainVo DlossPersTraceMainVo);
	
	/**
	 * 根据条件查新信息
	 * <pre></pre>
	 * @param registNo
	 * @param lossState 理算状态
	 * @param underwriteFlag 核损标注
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年4月26日 下午5:50:08): <br>
	 */
	public PrpLDlossPersTraceMainVo findPersTraceMainVoByCondition(String registNo, List<String> lossState,String underwriteFlag);
	
	/**
	 * 根据QueryRule查找记录
	 * <pre></pre>
	 * @param queryRule
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年4月27日 下午5:03:55): <br>
	 */
	public List<PrpLDlossPersTraceVo> findPrpLDlossPersTraceVoListByRule(QueryRule queryRule);
	
	/**
	 * 该案件下人伤费用审核是否全部通过
	 * @param registNo
	 * @return
	 */
	public boolean isDlossPersonAllPassed(String registNo);
	
	/**
	 * 按ID查找人伤主表
	 * @param id
	 * @return
	 * @modified:
	 */
	public PrpLDlossPersTraceMainVo findPersTraceMainVoById(Long id);

	/**
	 * 根据报案号和主表id查找所有的跟踪人员
	 * @param registNo
	 * @param persTraceMainId
	 * @return
	 * @modified:
	 */
	public List<PrpLDlossPersTraceVo> findPersTraceVo(String registNo,Long persTraceMainId);
	
	/**
	 * 保存或更新人伤主表
	 * @param persTraceMainVo
	 * @return
	 * @modified:
	 * ☆XMSH(2016年10月8日 下午2:47:44): <br>
	 */
	public Long saveOrUpdatePersTraceMain(PrpLDlossPersTraceMainVo persTraceMainVo);
	
	public int getTraceTimes(String registNo,Long injuredId);
}
