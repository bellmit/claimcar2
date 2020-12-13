/******************************************************************************
 * CREATETIME : 2016年1月6日 上午9:05:28
 ******************************************************************************/
package ins.sino.claimcar.lossperson.service;

import java.util.List;

import ins.framework.common.ResultPage;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.lossperson.vo.ChargeAdjustQueryResultVo;
import ins.sino.claimcar.lossperson.vo.ChargeAdjustQueryVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersInjuredVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceFeeVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceHisVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;

/**
 * <pre></pre>
 * 
 * @author ★XMSH
 */
public interface PersTraceService {

	/**
	 * 按报案后查找 PrpLDlossPersTraceMainVo List
	 * @param registNo
	 * @return
	 * @modified: ☆XMSH(2016年1月11日 下午3:31:11): <br>
	 */
	public List<PrpLDlossPersTraceMainVo> findPersTraceMainVo(String registNo);

	/**
	 * 按ID查找人伤主表
	 * @param id
	 * @return
	 * @modified:
	 * ☆XMSH(2016年3月8日 下午4:34:00): <br>
	 */
	public PrpLDlossPersTraceMainVo findPersTraceMainVoById(Long id);

	/**
	 * 根据id查找跟踪人员
	 * @param id
	 * @return
	 * @modified:
	 * ☆XMSH(2016年3月8日 下午4:44:43): <br>
	 */
	public PrpLDlossPersTraceVo findPersTraceVo(Long id);

	/**
	 * 根据报案号和主表id查找所有的跟踪人员
	 * @param registNo
	 * @param persTraceMainId
	 * @return
	 * @modified:
	 * ☆XMSH(2016年3月8日 下午4:46:10): <br>
	 */
	public List<PrpLDlossPersTraceVo> findPersTraceVo(String registNo,Long persTraceMainId);
	/**
	 * 根据报案号查找所有跟踪人员
	 * @param registNo
	 * @return
	 */
	public List<PrpLDlossPersTraceVo> findPersTraceVoByRegistNo(String registNo);
	
	/**
	 * 根据身份证查询报案下的受伤人员信息
	 * @param registNo
	 * @param certiCode
	 * @return
	 * @modified:
	 * ☆XMSH(2016年7月6日 下午2:45:02): <br>
	 */
	public List<PrpLDlossPersInjuredVo> findPersInjuredVo(String registNo,String certiCode);

	/**
	 * 查找personName的所有跟踪历史轨迹
	 * @param registNo
	 * @param persTraceMainId
	 * @param personName
	 * @return
	 * @modified:
	 * ☆XMSH(2016年3月8日 下午4:46:58): <br>
	 */
	public List<PrpLDlossPersTraceHisVo> findPersTraceHisVo(String registNo,Long persTraceMainId,String personName);

	/**
	 * 费用审核修改查询
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆XMSH(2016年3月8日 下午4:48:13): <br>
	 */
	public ResultPage<ChargeAdjustQueryResultVo> findPageForChargeAdjust(ChargeAdjustQueryVo queryVo) throws Exception;

	/**
	 * 新增或修改伤亡人员
	 * @param persTraceVo
	 * @return
	 * @throws Exception 
	 * @modified:
	 * ☆XMSH(2016年3月8日 下午4:49:18): <br>
	 */
	public Long saveOrUpdatePersTrace(PrpLDlossPersTraceVo persTraceVo,SysUserVo userVo) throws Exception;

	/**
	 * 新增或保存人伤主表
	 * @param persTraceMainVo
	 * @return
	 * @modified:
	 * ☆XMSH(2016年3月8日 下午4:49:56): <br>
	 */
	public Long saveOrUpdatePersTraceMain(PrpLDlossPersTraceMainVo persTraceMainVo);

	/**
	 * 保存人伤跟踪记录轨迹
	 * @param persTraceVo
	 * @modified:
	 * ☆XMSH(2016年3月8日 下午4:50:33): <br>
	 */
	public void savePersTraceHis(PrpLDlossPersTraceVo persTraceVo);
	
	public void updatePersTraceHis(PrpLDlossPersTraceHisVo persTraceHisVo);
	
	public List<PrpLDlossPersTraceHisVo> findPersTraceHisVo(Long persTraceMainId,String flag);
	
	/**
	 * 激活或注销伤亡人员
	 * @param persTraceVo
	 * @modified:
	 * ☆XMSH(2016年3月8日 下午4:51:43): <br>
	 */
	public void AvtiveOrCanCelPersTrace(PrpLDlossPersTraceVo persTraceVo);
	
	/**
	 * 更新跟踪记录金额(不排空)
	 * @param persTraceVo
	 * @modified:
	 * ☆XMSH(2016年3月8日 下午4:52:10): <br>
	 */
	public void updatePersTraceSumAmount(PrpLDlossPersTraceVo persTraceVo);
	
	/**
	 * 更新审核跟踪记录金额(不排空)
	 * @param persTraceVo
	 * @modified:
	 * ☆XMSH(2016年3月8日 下午4:52:58): <br>
	 */
	public void updatePersTraceVeriSumAmount(PrpLDlossPersTraceVo persTraceVo);
	
	/**
	 * 更新费用赔款信息(不排空)
	 * @param feeVo
	 * @modified:
	 * ☆XMSH(2016年3月8日 下午4:53:42): <br>
	 */
	public void updatePersTraceFee(PrpLDlossPersTraceFeeVo feeVo);
	
	/**
	 * 更新审核费用赔款信息(不排空)
	 * @param feeVo
	 * @modified:
	 * ☆XMSH(2016年3月8日 下午4:54:06): <br>
	 */
	public void updatePersTraceVeriFee(PrpLDlossPersTraceFeeVo feeVo);

	/**
	 * 计算所有人伤金额统计
	 * @param persTaceMainVo
	 * @return
	 */
	public PrpLDlossPersTraceMainVo calculateSumAmt(PrpLDlossPersTraceMainVo persTaceMainVo);
	
	public Long saveOrUpdateTraceMain(PrpLDlossPersTraceMainVo persTraceMainVo);
	/**
	 * 根据id查询人伤主表，获取跟踪员
	 * @param id
	 * @return
	 */
	public PrpLDlossPersTraceMainVo findPersTraceMainVobyId(Long id);
	
	public void updateAndsaveAndDelSonsPrpLDlossPersTrace(PrpLDlossPersTraceVo prpLDlossPersTraceVo);
	public void updateAndsaveAndDelSonsPrpLDlossPersInjured(PrpLDlossPersInjuredVo prpLDlossPersInjuredVo);
	
}
