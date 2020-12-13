package ins.sino.claimcar.flow.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.vo.AssignUserVo;

import java.util.List;

/**
 * 统一分配方式类
 * @author zhouyanbin
 */
public interface AssignService {
	
	/**
	 * 组权限统一分配入口
	 * @param nodeCode 当前节点ID
	 * @param comCodes 机构代码
	 * @param userVo 当前处理人 用于不轮询时的判断
	 * @param isExcludeCurrentUser  是否排除当前处理人 0 排除
	 * @return
	 */
	public SysUserVo execute(FlowNode nodeCode,String comCode,SysUserVo userVo, String isExcludeCurrentUser);
	
	/**
	 * 更新PrpLAssignCount表中被分配任务用户的数据
	 * <pre></pre>
	 * @param userVo
	 * @param comCode
	 * @param gradeId
	 * @modified:
	 * ☆WLL(2016年8月12日 上午10:24:29): <br>
	 */
	public void updatePrpLAssignCountAfterPolling(SysUserVo userVo,String comCode,FlowNode nodeCode);
	
	public boolean existsGradeUser(FlowNode nodeCode,String comCode);
	
	
	
	public void clearRule();
	
	/**
	 * 根据岗位和机构返回人员
	 * @param nodeCode
	 * @param comCode
	 * @return
	 */
	public List<AssignUserVo> returnUserVoByGrade(FlowNode nodeCode,String comCode);
	
	/**
	 * 根据员工code查询存在有效休假的记录
	 * @param userCode
	 * @return true-没有休假，false-正在休假
	 */
	public boolean findHolidayByUserCode(String userCode);
	
	/**
	 * 判断有效的人员（SysUserVo有效且没有休假）
	 * @param userCode
	 * @return
	 */
	public boolean validUserCode(String userCode);

	/**
	 * 判断该工号是否可以处理当前任务的下一节点任务
	 * @param nodeCode 节点信息
	 * @param comCode 处理机构
	 * @param userCode 关联任务的处理工号
	 * @return 返回true or false
	 */
	public boolean isUserCouldHandle(FlowNode nodeCode,String comCode,String userCode);
}