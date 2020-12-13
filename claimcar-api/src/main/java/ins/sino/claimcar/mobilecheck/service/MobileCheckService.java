package ins.sino.claimcar.mobilecheck.service;

import ins.sino.claimcar.mobilecheck.vo.FixedPositionReqVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleDOrGBackReqVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleDOrGReqVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleSDBackReqVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleSDReqVo;
import ins.sino.claimcar.mobilecheck.vo.PersonnelInformationReqVo;
import ins.sino.claimcar.mobilecheck.vo.PersonnelInformationResVo;

public interface MobileCheckService {

	/**
	 * 自动调度接口
	 * @param reqVo
	 * @return
	 * @throws Exception
	 */
	public PersonnelInformationResVo getPersonnelInformation(PersonnelInformationReqVo reqVo,String url) throws Exception;

	/**
	 * 打开移动查勘地图报案调用
	 * @param fixedPositionReqVo
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public String getPositionMapUrl(FixedPositionReqVo fixedPositionReqVo,String item,String backUrl,String url) throws Exception;

	/**
	 * 获取手动调度地图的URL调度调用
	 * @param handleScheduleReqVo
	 * @return
	 * @throws Exception
	 */
	public String getHandelScheduleUrl(HandleScheduleReqVo handleScheduleReqVo,String backUrl,String url) throws Exception;

	/**
	 * 自定义获取人员
	 * @param handleScheduleReqVo
	 * @return
	 * @throws Exception
	 */
	public HandleScheduleSDBackReqVo getHandelScheduleSDUrl(HandleScheduleSDReqVo handleScheduleReqVo,String url) throws Exception;

	/**
	 * 理赔调度提交/改派提交接口（理赔请求快赔系统）
	 * @param handleScheduleReqVo
	 * @return
	 * @throws Exception
	 */
	public HandleScheduleDOrGBackReqVo getHandelScheduleDOrDUrl(HandleScheduleDOrGReqVo handleScheduleReqVo,String url) throws Exception;

}
