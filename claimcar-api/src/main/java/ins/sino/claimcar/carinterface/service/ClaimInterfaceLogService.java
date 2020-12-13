package ins.sino.claimcar.carinterface.service;

import ins.framework.common.ResultPage;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.court.vo.PrpLCourtMessageVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ClaimInterfaceLogService {

	/**
	 * <pre>保存接口日志</pre>
	 * @param logVo
	 * @return
	 * @modified:
	 * ☆Luwei(2016年10月25日 上午9:42:36): <br>
	 */
	public ClaimInterfaceLogVo save(ClaimInterfaceLogVo logVo);
	
	/**
	 * 更新接口日志数据
	 * @param logVo 日志对象
	 * @return
	 */
	public ClaimInterfaceLogVo updateLog(ClaimInterfaceLogVo logVo);
	
	/**
	 * <pre>批量保存接口日志</pre>
	 * @param logVo
	 * @return
	 * @modified:
	 * ☆yzy(2019年8月29日 上午9:42:36): <br>
	 */
	public void saveAll(List<ClaimInterfaceLogVo> logVoList);
	
	/**
	 * <pre>根据ID查询接口日志信息</pre>
	 * @param id
	 * @return
	 * @modified:
	 * ☆Luwei(2016年10月25日 上午9:42:10): <br>
	 */
	public ClaimInterfaceLogVo findLogByPK(Long id);
	
	/**
	 * <pre>接口日志补传</pre>
	 * @param logId
	 * @modified:
	 * ☆Luwei(2016年10月25日 上午9:41:56): <br>
	 */
	public String interfaceLogReupload(Long logId,SysUserVo userVo) throws Exception;

	/**
	 * <pre>根据报案号查询日志</pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆Luwei(2016年10月25日 上午9:42:53): <br>
	 */
	public List<ClaimInterfaceLogVo> findLogByRegistNo(String registNo);

	/**
	 * <pre>接口日志分页查询</pre>
	 * @param queryVo,start,length
	 * @return
	 * @modified:
	 * ☆Luwei(2016年10月25日 上午9:43:53): <br>
	 */
	public ResultPage<ClaimInterfaceLogVo> findLogForPage(ClaimInterfaceLogVo queryVo,Integer start,Integer length) throws Exception;

	/**
	 * <pre>互补传功能需优化,补传完后 原任务不能在补传</pre>
	 * @param id
	 * @modified:
	 * ☆Luwei(2017年2月28日 下午3:01:22): <br>
	 */
	public void changeInterfaceLog(Long id);
	
	/**
	 * 查询请求时间，业务类型，状态，不等于errorCode
	 * @param requestTime
	 * @param businessType
	 * @param status
	 * @param errorCode
	 * @return
	 */
	public List<ClaimInterfaceLogVo> findLogByRequestTime(Date requestTime,String businessType,String status,String errorCode);
	
	/**
	 * 根据报案号和上传节点查询上传失败的数据
	 * @param registNo
	 * @param BusinessType
	 * @return
	 */
	public Map<String,List<ClaimInterfaceLogVo>> searchByRegistNoAndType(String registNo,List<String> businessType);
	
	/**
	 * 获取指定业务类型接口成功请求次数
	 * @param registNo
	 * @param businessType
	 * @param compensateNo
	 * @return
	 */
	public int getBusinessTypeInterfaceRequestTimes(String registNo,String businessType,String compensateNo) throws Exception;
	
	/**
	 * 获取已决送再保接口成功请求次数
	 * @param registNo
	 * @param compensateNo
	 * @return
	 */
	public int getReinsuranceInterfaceRequestTimes(String registNo,String compensateNo) throws Exception;

}
