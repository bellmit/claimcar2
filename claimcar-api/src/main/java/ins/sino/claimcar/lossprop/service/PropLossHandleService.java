package ins.sino.claimcar.lossprop.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.vo.DeflossActionVo;
import ins.sino.claimcar.lossprop.vo.PropModifyVo;
import ins.sino.claimcar.lossprop.vo.PropQueryVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.lossprop.vo.SubmitNextVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.util.List;
import java.util.Map;

public interface PropLossHandleService {

	/**
	 * 定损修改查询方法
	 */
	public List<PropModifyVo> propModfifyQuery(PropQueryVo queryVo);

	public void savePropMain(DeflossActionVo deflossVo,
			SysUserVo sysUserVo);

	public void saveOrUpdateDefloss(DeflossActionVo deflossVo,
			SysUserVo sysUserVo);

	/**
	 * 提交下一个节点
	 */
	public List<PrpLWfTaskVo> submitNextNode(
			PrpLdlossPropMainVo lossPropMainVo, SubmitNextVo nextVo,
			SysUserVo sysUserVo);

	public void saveClaimText(DeflossActionVo deflossVo,
			String nodeCode);

	/**
	 * 核损初始化
	 */
	public DeflossActionVo initPropVerify(DeflossActionVo deflossVo,
			SysUserVo userVo);

	/**
	 * 核损保存
	 */
	public void savePropVerify(DeflossActionVo deflossVo,
			SysUserVo sysUserVo);

	/**
	 * 接收定损任务
	 * 
	 */
	public PrpLdlossPropMainVo acceptDefloss(Double flowTaskId,
			DeflossActionVo deflossVo, SysUserVo sysUserVo);

	/**
	 * 财产初始化
	 */
	public DeflossActionVo initPropDefloss(DeflossActionVo deflossVo,
			double flowTaskId, SysUserVo sysUserVo);

	/**
	 * 未处理 或者 已接受未处理 调用该方法初始化
	 */
	public void getDefloss(DeflossActionVo deflossVo,
			PrpLRegistVo registVo, SysUserVo sysUserVo);

	/**
	 * 加载费用赔款信息
	 * @param chargeTypes
	 * @param registNo
	 * @param intermCode
	 * @param feeStandard
	 */
	public List<PrpLDlossChargeVo> loadChargeTr(String[] chargeTypes,
			String registNo, String intermCode);

	/** 获取报案保单险别  */
	public Map<String, String> getPolicyKindMap(String registNo);

	/** 根据查勘出险-->对应险别  */
	public String getKindCode(String registNo);

	// 获取公估资费标准
	public Map<String, String> getIntermStanders(String code);

	//获取费用名称Map
	public Map<String, String> getFeeTypeCode();

	/**
	 * 初始化费用选择类型
	 * @param registNo
	 * @param kindCodes
	 */
	public List<PrpLCItemKindVo> initSubRisks(String registNo,
			String[] kindCodes);

	public SubmitNextVo organizeNextVo(Long lossMainId,
			String flowTaskId, String currentNode, String saveType,SysUserVo userVo,String isSubmitHeadOffice);

	/** 获取收款人 
	 * @param registNo
	 */
	public Map<String, String> getPayCustomMap(String registNo);

	public Map<String, String> getReplacePayMap(Long id);

	public List<PrpLDlossPersTraceVo> findPersTraceList(String registNo);

	public DeflossActionVo deflossView(String lossId);

	/**
	 * 校验财产定损 互碰自赔
	 * @param deflossVo
	 * @return
	 * @modified:
	 * ☆YangKun(2016年7月15日 上午10:54:16): <br>
	 */
	public String validDefloss(DeflossActionVo deflossVo);

	/**
	 * 保存定损信息
	 * @param lossPropMainVo
	 * @param nextVo
	 * @param sysUserVo
	 */
	public void savePropLoss(PrpLdlossPropMainVo lossPropMainVo,SubmitNextVo nextVo,SysUserVo sysUserVo);
	
	/**
	 * 提交工作流
	 * @param lossPropMainVo
	 * @param nextVo
	 * @param sysUserVo
	 * @return
	 */
	public List<PrpLWfTaskVo> submitTask(PrpLdlossPropMainVo lossPropMainVo,SubmitNextVo nextVo,SysUserVo sysUserVo);
	
	/**
	 * 注销财损任务，underwriteflag回写为6
	 * @param id
	 */
	public void cancelProp(String id);
	/**
	 * 生成查勘费任务
	 * @param lossPropMainVo
	 * @param userVo
	 */
	public void saveAcheckFeeTask(PrpLdlossPropMainVo lossPropMainVo,SysUserVo userVo);
	
}