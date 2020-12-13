/******************************************************************************
 * CREATETIME : 2016年9月24日 下午3:08:43
 ******************************************************************************/
package ins.sino.claimcar.platform.service;

import ins.framework.common.ResultPage;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformTaskVo;

/**
 * 平台重新上传处理服务接口
 * @author ★Luwei
 * @CreateTime 2016年6月30日
 */
public interface PlatformReUploadService {

	/**
	 * <pre>平台补传</pre>
	 * @param logId
	 * @throws Exception 
	 * @modified:
	 * ☆Luwei(2016年9月24日 下午3:17:26): <br>
	 */
	public abstract void platformReUpload(Long logId) throws Exception;

	/**
	 * <pre>平台日志查询</pre>
	 * @param queryVo
	 * @param start
	 * @param length
	 * @return
	 * @modified:
	 * ☆Luwei(2016年9月24日 下午3:16:54): <br>
	 */
	public abstract ResultPage<CiClaimPlatformLogVo> findPaltformInfoForPage
	(CiClaimPlatformLogVo queryVo,Integer start,Integer length) throws Exception;
	
	/**
	 * 查询自动上传平台的任务表
	 * @param queryVo
	 * @param start
	 * @param length
	 * @return
	 */
	public ResultPage<CiClaimPlatformTaskVo> findPaltformTaskForPage(CiClaimPlatformTaskVo queryVo,Integer start,Integer length)  throws Exception;

	/**
	 * 平台交互查询，唯一的记录
	 * <pre></pre>
	 * @param logId
	 * @return
	 * @modified:
	 * ☆Luwei(2016年9月24日 下午3:32:50): <br>
	 */
	public abstract CiClaimPlatformLogVo findLogById(Long logId);
	
	/**
	 * 根据ciClaimPlatformTask的ID查询CiClaimPlatformLog表
	 * @param logId
	 * @return
	 */
	public CiClaimPlatformLogVo findLogByTaskId(Long taskId);
	
	public CiClaimPlatformTaskVo findPlatformTaskById(Long taskId);
	
	/**
	 *  查询案件的上传log记录,查询报案上传成功的Log记录
	 * @param bussNo（不能为空）
	 * @param reqType（不能为空）
	 * @param comCode 保单机构代码（区别全国和上海平台）
	 * @param status=1(查询出报案上传平台成功的一条唯一数据)
	 * @return CiClaimPlatformLogVo
	 * @modified:
	 * ☆Luwei(2016年9月24日 下午3:33:10): <br>
	 */
	public abstract CiClaimPlatformLogVo findLogByBussNo(String reqType,String bussNo,String comCode);
	
	/**
	 * <pre>更新日志表的状态（已补送）</pre>
	 * @param logId
	 * @modified:
	 * ☆Luwei(2017年1月16日 下午5:09:53): <br>
	 */
	public abstract void platformLogUpdate(Long logId);
	
	/**
	 * 平台补送数据 服务方法（批量补送）
	 * @param bussNoArray
	 * ☆Luwei(2017年3月9日 上午10:17:45): <br>
	 */
	public abstract String dataReloadSend(String uploadNode, String bussNoArray ,SysUserVo userVo) throws Exception;
	
	/**
	 * 根据请求类型，案件号，状态位精确查询平台日志表，flag=1则需要查询旧理赔
	 * @param reqType
	 * @param bussNo
	 * @param status
	 * @param flag
	 * @return
	 */
	public CiClaimPlatformLogVo findPlatformLog(String reqType,String bussNo,String status,String flag);
	

	/**
	 * 查询案件的最新上传平台log记录
	 * @param bussNo（不能为空）
	 * @param reqType（不能为空）
	 * @param comCode 保单机构代码（区别全国和上海平台）
	 * @return CiClaimPlatformLogVo
	 */
	public abstract CiClaimPlatformLogVo findLastestLogByReqTypeBussNoComCode(String reqType,String bussNo,String comCode);

	/**
	 * 第一次补传平台
	 * @throws Exception
	 * @modified: ☆Lundy(2018年12月19日 下午3:50:42): <br>
	 */
	public void platformFirstUpload(CiClaimPlatformLogVo logVo) throws Exception;
}
