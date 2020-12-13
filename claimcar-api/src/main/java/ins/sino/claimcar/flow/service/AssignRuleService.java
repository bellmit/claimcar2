package ins.sino.claimcar.flow.service;

import ins.sino.claimcar.flow.vo.AssignUserVo;

import java.util.List;

/**
 * 统一分配方式类
 * @author zhouyanbin
 *
 */
public interface AssignRuleService {
	
	/**
	 * add by zhouyanbin 分配算法统一入口
	 * @param assignType  分配方式
	 * @param assignUserVolist 分配人员列表
	 * @return
	 */
	public AssignUserVo assignTask(String assignType,List<AssignUserVo> assignUserVolist);
	
	
	
}