package ins.sino.claimcar.ilog.rule.service;

import ins.framework.dao.database.support.Page;
import ins.sino.claimcar.ilog.rule.vo.IlogDataProcessingVo;
import ins.sino.claimcar.ilog.rule.vo.PrpLRuleBaseInfoVo;
import ins.sino.claimcar.ilog.rule.vo.PrpLRuleDetailInfoVo;
import ins.sino.claimcar.ilogFinalpowerInfo.vo.IlogFinalPowerInfoVo;

import java.util.List;


public interface IlogRuleService {

	/**
	 * 保存规则信息
	 * @param prpLRuleBaseInfoVo
	 * @param prpLRuleDetailInfoVoList
	 * @author ☆luows(2018年1月12日 上午9:00:27): <br>
	 */
	public void saveRuleInfo(PrpLRuleBaseInfoVo   prpLRuleBaseInfoVo,List<PrpLRuleDetailInfoVo> prpLRuleDetailInfoVoList);
	
	/**
	 * 查询某个节点的规则信息
	 * @param BusinessNo
	 * @param RuleNode
	 * @param OperateType
	 * @return
	 * @author ☆luows(2018年1月12日 上午9:00:38): <br>
	 */
	public IlogDataProcessingVo findRuleInfo(String businessNo,String ruleNode);
	
	/**
	 * 获取规则基本信息
	 * @param businessNo
	 * @param ruleNode
	 * @return
	 * @author ☆luows(2018年1月17日 下午3:04:38): <br>
	 */
	public PrpLRuleBaseInfoVo findRuleBaseInfo(String businessNo,String ruleNode,String riskCode,String licenseNo,String taskId);
	
	
	/**
	 * 查询规则明细数据
	 * @param businessNo
	 * @param ruleNode
	 * @return
	 * @author ☆luows(2018年1月17日 下午3:14:29): <br>
	 */
	public IlogDataProcessingVo findDetailInfoList(String businessNo,String ruleNode,String riskCode,String licenseNo,String taskId);
	
	
	/**
	 * 获取规则明细提示信息
	 * @param ruleId
	 * @param serialNo
	 * @param ruleNode
	 * @return
	 * @author ☆luows(2018年1月18日 上午10:04:50): <br>
	 */
	public String findRuleContent(String ruleId,String serialNo,String ruleNode);
	
	/* 兜底权限管理 start */
	/**
	 * ilog兜底权限查询
	 * @param ilogFinalPowerInfo
	 * @param start
	 * @param length
	 * @return
	 */
	public Page findIlogFinalPowerForPage(IlogFinalPowerInfoVo ilogFinalPowerInfo, Integer start, Integer length);
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public IlogFinalPowerInfoVo findById(Long id);
	
	/**
	 * 保存修改
	 * @param ilogfinal
	 * @return
	 * @throws Exception
	 */
	public Long ilogFinalUpdate(IlogFinalPowerInfoVo ilogfinal) throws Exception;
	
	/**
	 * 通过id删除信息
	 * @param id
	 * @throws Exception
	 */
	public void deleteilog(Long id) throws Exception;

	/**
	 * 	根据工号查询兜底人员权限信息
	 * @param userCode
	 * @return
	 */
	public IlogFinalPowerInfoVo findByUserCode(String userCode);
	
	/**
	 * 添加ilog信息
	 * @param info
	 * @throws Exception
	 */
	public void addIlog(IlogFinalPowerInfoVo info) throws Exception;
	
	/* 兜底权限管理  end  */

}
