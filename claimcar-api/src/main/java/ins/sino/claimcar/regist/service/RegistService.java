package ins.sino.claimcar.regist.service;

import ins.framework.common.ResultPage;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.manager.vo.PrpLInsuredFactoryVo;
import ins.sino.claimcar.regist.vo.BiCiPolicyVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistAndCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistAvgVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistRelationshipHisVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.regist.vo.PrplOldClaimRiskInfoVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo; 

import java.util.Date;
import java.util.List;
import java.util.Map;
public interface RegistService {

	public PrpLRegistVo saveOrUpdate(PrpLRegistVo prpLRegistVo);



	public PrpLRegistVo findRegistByRegistNo(String registNo);

	/**
	 * 报案保存后提交工作流，发起调度
	 * @param registNo
	 * @param submitVo
	 * @param scheduleTaskVo2
	 * @return
	 * @throws Exception
	 */
	public void submitSchedule(PrpLRegistVo registVo, WfTaskSubmitVo submitVo,
			PrpLScheduleTaskVo scheduleTaskVo,String url) throws Exception;

	// 通过报案号查询风险信息表
	public Map<String, String> findRegistRiskInfoByRegistNo(String registNo);

	/**
	 * 保单取消关联
	 * @param prpLCMainVoList
	 * @param registVo
	 * @param relationshipHisVo
	 */
	public void cancelPolicy(List<PrpLCMainVo> prpLCMainVoList,
			PrpLRegistVo registVo, PrpLRegistRelationshipHisVo relationshipHisVo);

	public PrpLRegistVo updatePrpLRegist(PrpLRegistVo registVo);

	/**
	 * 保存保单关联与取消轨迹
	 * @param relationshipHisVo
	 */
	public void saveRelationshipHis(
			PrpLRegistRelationshipHisVo relationshipHisVo);

	public List<PrpLRegistRelationshipHisVo> findRelationshipHisByRegistNo(
			String registNo);

	public void saveRiskInfos(PrpLRegistVo prpLRegistVo,
			Map<String, String> registRiskInfoMap);

	/**
	 * 更新代位求偿标志
	 * @param prpLRegistVo
	 * @param flag
	 */
	public void updateRiskInfoForSubRogation(PrpLRegistVo prpLRegistVo,
			String flag);

	public void updatePrpLRegistAvgByVos(List<PrpLRegistAvgVo> registAvgVos);

	/**
	 * 保存案件理赔条件数据
	 * @modified: ☆Luwei(2016年5月20日 上午11:31:17): <br>
	 */
	public void saveDeductCond(String registNo);

	/**
	 * 更新风险提示信息
	 * @param prpLRegistVo
	 * @param flag
	 */
	public void updateRiskInfo(PrpLRegistVo prpLRegistVo, String flag);

	// 无保转有保删除风险因子
	public void deleteByRegistNo(PrpLRegistVo registVo);

	public List<PrpLCItemKindVo> findPrpLCItemKindByOther(String BIPolicyNo,String BIRiskCode);

	public List<PrpLCItemKindVo> findPrpLCItemKindByOthers(String BIPolicyNo,
			String kindCode,String BIRiskCode);

	public void sendMsg(PrpLRegistVo prpLRegistVo);

	public PrpLRegistVo saveOrUpdatePrpLRegist(PrpLRegistVo prpLRegistVo);

	public List<PrplOldClaimRiskInfoVo> findPrploldclaimriskinfoByPolicyNo(
			String policyNo);

	public PrpLRegistAndCMainVo findPrpLCMainVoByPolicyNo(String policyNo);
	
	/**
	 * 根据报案号查询cItemKind
	 * @param policyNo
	 * @return
	 */
	public List<PrpLCItemKindVo> findCItemKindByPolicyNo(String registNo);
	
	/**
	 * 根据保单号获取历史出险次数
	 * @param registno
	 * @return
	 */
	public Map<String,String> getRegistTimesByPolicyNo(String PolicyNo);

	   /**
	 * 查询承保被保险人
	 * @param lAgentFactoryVo
	 * @param start
	 * @param length
	 * @return
	 * @modified: ☆zhujunde(2017年6月7日 下午7:34:21): <br>
	 */
    public abstract ResultPage<PrpLCMainVo> findPrpLCMainVo(
            PrpLInsuredFactoryVo prpLInsuredFactoryVo, int start, int length);



    /**
	 * 移动查勘交互更新调度任务表信息
	 * 
	 * <pre></pre>
	 * @param registVo
	 * @param scheduleTaskVo
	 * @param url
	 * @modified: ☆LinYi(2018年3月5日 下午4:27:42): <br>
	 */
    public void updateScheduleTask(PrpLRegistVo registVo,PrpLScheduleTaskVo scheduleTaskVo,String url);
    
    public List<PrpLRegistVo> findPrplregistNo(String flag,Date startDate,Date endDate);

	/**
	 * 报案送警报平台
	 * 
	 * <pre></pre>
	 * @return
	 * @modified: ☆LiYi(2018年9月27日 上午11:18:56): <br>
	 */
	public List<PrpLRegistVo> findPrpLRegistByHql(Map<String,String> map);
	/**
	 * 自助报案时，保单信息校验，是否符合报案相关条件
	 * @param cmainVo
	 * @return
	 */
	public void validateRegist(BiCiPolicyVo policyVo) throws Exception;
	/**
	 * 更新报案扩展表
	 * @param prpLRegistExtVo
	 */
	public void updatePrpLRegistExt (PrpLRegistExtVo prpLRegistExtVo);

	
	/**
	 * 判断案件是否为单车事故、无人伤、无物损（或物损无损失）
	 * @param registNo
	 * @return
	 */
	public Boolean isSelfHelpSurVey(String registNo);

	/**
	 * 从再保获取巨灾二级代码
	 * @param disasterOneCode
	 * @return
	 */
	public List<String> findLevelTwoDisasterInfo(String disasterOneCode);
	
	public List<PrpLCItemKindVo> findPrpLCItemKindByPolicyNo(String policyNo);

	/**
	 * 根据平安联盟案件号查询是否已存在
	 * @param paicReportNo
	 * @return
	 */
	public PrpLRegistVo findRegistByPaicReportNo(String paicReportNo);
}