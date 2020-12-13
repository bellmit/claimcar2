package ins.sino.claimcar.claimjy.service;




import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claimjy.vo.JyResVo;



/**
 * 
 * @author zhujunde
 *
 */
public interface JyTaskService {

	/**
	 * 任务状态同步接口（从理赔系统请求定损系统）
	 * @param registNo
	 * @param dmgVhclId
	 * @param operationLink
	 * @param operationResults
	 * @param subNodeCode
	 * @param userVo
	 * @return
	 */
	public JyResVo sendTaskInfoService(String registNo,String dmgVhclId,String operationLink,String operationResults,String subNodeCode,SysUserVo userVo);
}
