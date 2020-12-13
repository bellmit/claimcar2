package ins.sino.claimcar.lossprop.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropFeeVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainHisVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;

import java.util.List;

public interface PropLossService {


	public abstract PrpLdlossPropMainVo findVoByRegistNo(String registNo);
	/**
	 * 根据报案号与平安业务表Id查询
	 * @param registNo
	 * @param paId
	 * @return
	 */
	public abstract PrpLdlossPropMainVo findVoByRegistNoAndPaid(String registNo,Long paId);

	/**
	 * 查询财产损失信息
	 * @modified:
	 * ☆Luwei(2016年3月30日 下午6:58:56): <br>
	 */
	public abstract List<PrpLdlossPropFeeVo> findPropVoListByRegistNo(
			String registNo);

	public abstract List<PrpLdlossPropMainVo> findPropMainByRegistNo(
			String registNo);

	public abstract void saveOrUpdatePropMain(
			PrpLdlossPropMainVo prpLdlossPropMainVo, SysUserVo sysUserVo);

	public abstract PrpLdlossPropMainVo findVoByKey(Long id);

	public abstract PrpLdlossPropMainVo findPropMainVoById(Long id);

	public abstract Long findPropFeeVoById(Long id);

	/**
	 * 更新定损主表信息
	 * 该方法只提供提交后回写数据使用（vo是查询处理的，非页面获得）
	 * <pre></pre>
	 * @param lossCarMainVo
	 * @modified:
	 * ☆yangkun(2016年2月15日 下午4:52:18): <br>
	 */
	public abstract void updatePropMain(PrpLdlossPropMainVo propMainVo);

	public abstract String dLPropModVlaid(String registNo, String lossId);

	/**
	 * 判断能否发起损余回收，返回"t","f"
	 * @param wfTaskVo
	 * @return
	 */
	public abstract String judgeRecLoss(PrpLWfTaskVo wfTaskVo);

	/**
	 * 未理算的财产定损
	 * @param registNo
	 * @param serialNo
	 * @return
	 */
	public abstract List<PrpLdlossPropMainVo> findLossPropNoComp(
			String registNo, Integer serialNo);

	/**
	 * 保存轨迹表
	 * @param lossPropMainVo
	 */
	public void savePropHis(PrpLdlossPropMainVo lossPropMainVo);
	
	/**
	 * 根据财产主表的ID查询轨迹表，取创建日期最新的一条数据
	 * @param propMainId
	 * @return
	 */
	public PrpLdlossPropMainHisVo findPropHisByPropMainId(Long propMainId);
	
	
	public PrpLdlossPropFeeVo findLossPropFeeVoById(Long id);
	
	/**
	 * 通过serialNo查询同一报案号下的财产定损
	 * @param registNo
	 * @param serialNo
	 * @return
	 */
	public List<PrpLdlossPropMainVo> findLossPropBySerialNo(String registNo, Integer serialNo);
}