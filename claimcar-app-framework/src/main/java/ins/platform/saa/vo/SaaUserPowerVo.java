package ins.platform.saa.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom VO class of PO SaaUserPower
 */ 
public class SaaUserPowerVo implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	
	private String userCode;
	private List<String> roleList;
	private List<String> taskList;
	private Map<String,String> roleMap = null;

	/** 允许的权限因子 */
	private Map<String,List<SaaFactorPowerVo>> permitFactorMap;
	/** 排除权限的因子 */
	private Map<String,List<SaaFactorPowerVo>> exceptFactorMap;

	private Map<String,String> maxVerifyLVMap;// 这个用户在审核权限中最高的审核等级<Key=NodeCode,Value=subNodeCode
	
	private Map<String,List<String>> allVerifyLVMap;// 该用户的所有审核权限

	/**
	 * 判断有没有岗位
	 * @param roleCode
	 * @return
	 * @modified: ☆LiuPing(2016年6月29日 ): <br>
	 */
	public Boolean hasRole(String roleCode) {
		if(roleMap==null&&roleList!=null){
			roleMap = new HashMap<String,String>();
			for(String role:roleList){
				roleMap.put(role,null);
			}
		}
		if(roleMap==null) return false;
		return roleMap.containsKey(roleCode);
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public List<String> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<String> taskList) {
		this.taskList = taskList;
	}

	public List<String> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}

	public Map<String,List<SaaFactorPowerVo>> getPermitFactorMap() {
		return permitFactorMap;
	}

	public void setPermitFactorMap(Map<String,List<SaaFactorPowerVo>> permitFactorMap) {
		this.permitFactorMap = permitFactorMap;
	}

	public Map<String,List<SaaFactorPowerVo>> getExceptFactorMap() {
		return exceptFactorMap;
	}

	public void setExceptFactorMap(Map<String,List<SaaFactorPowerVo>> exceptFactorMap) {
		this.exceptFactorMap = exceptFactorMap;
	}

	public Map<String,String> getMaxVerifyLVMap() {
		return maxVerifyLVMap;
	}

	public void setMaxVerifyLVMap(Map<String,String> maxVerifyLVMap) {
		this.maxVerifyLVMap = maxVerifyLVMap;
	}

	public Map<String,List<String>> getAllVerifyLVMap() {
		return allVerifyLVMap;
	}

	public void setAllVerifyLVMap(Map<String,List<String>> allVerifyLVMap) {
		this.allVerifyLVMap = allVerifyLVMap;
	}

	public boolean hasTask(String... taskCodes) {
		boolean hasPower=false;
		for(String task:taskCodes){
			hasPower=taskList.contains(task);
			if(hasPower) return hasPower;
		}
		return hasPower;
	}

	
	// 因子不允许权限
}
