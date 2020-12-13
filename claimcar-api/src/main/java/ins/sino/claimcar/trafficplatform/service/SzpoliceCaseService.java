package ins.sino.claimcar.trafficplatform.service;

import ins.framework.dao.database.support.Page;
import ins.sino.claimcar.trafficplatform.vo.AccidentResInfo;
import ins.sino.claimcar.trafficplatform.vo.DispatchInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SzpoliceCaseService {
	
	/**
	 * 通过报案号深圳警保信息
	 * @param registNo
	 * @return
	 */
	public AccidentResInfo findAccidentResInfoByRegistNo(String registNo);

	/**
	 * 保存事故信息PrpLAccidentResInfoPo
	 * @param infoVoList
	 * @throws Exception
	 */
	public void savePrpLAccidentResInfoPo(List<AccidentResInfo> infoVoList);
	
	/**
	 * 
	 * @param plateNo
	 * @param accidentTime
	 * @param types
	 * @return
	 */
	public String findAccidentInfoByPlateNoAndTime(String plateNo,String accidentTime,String comCode,String types);
	
	/**
	 * 根据accidentNo查询AccidentResInfo
	 * @param accidentNo
	 * @return
	 */
	public AccidentResInfo findAccidentResInfoByAccidentNo(String accidentNo);
	
	/**
	 * 更新或者保存AccidentResInfo
	 * @param infoVo
	 */
	public void updateAccidentResInfo(AccidentResInfo infoVo);
	
	/**
	 * 事故信息下载请求接口
	 */
	public void accidentInfoDownLoad();
	
	/**
	 * 查询一天中深圳警保理赔信息上传失败的日志数据，并返回对应的立案号
	 * @return
	 */
	public Map<String,Long> findClaimInterfaceLog();
	/**
	 * 深圳警保事故信息查询
	 * @param accidentInformation
	 * @param start
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public Page findPolicyAccidentForPage(AccidentResInfo accidentResInfo,Integer start,Integer length);

	/**
	 * 	调度信息下载后自动生成查勘任务
	 * 	@author maofengning
	 * @throws Exception 
	 * 	@date 2019年4月22日 16点17分
	 */
	public void autoGenerateSurveyTasks(DispatchInfo dispatchInfo) throws Exception;

	/**
	 * 调度信息下载接口
	 * 
	 * <pre></pre>
	 * @modified: ☆Wurh(2019年3月27日 上午11:15:18): <br>
	 */
	public void dispatchInfoDownload();
	
	/**深圳警情信息下载
	 * warningInstanceDownload
	 * <pre></pre>
	 * @modified:
	 * ☆zhujunde(2019年4月16日 下午4:51:34): <br>
	 */
	public void warningInstanceDownload();

	public void dowmloadAcciPhoto(List<AccidentResInfo> infoVoList,HashMap<String,String> registNoAccidentNoMap);
}
