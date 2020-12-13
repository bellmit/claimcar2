/******************************************************************************
 * CREATETIME : 2016年9月23日 下午4:46:46
 ******************************************************************************/
package ins.sino.claimcar.check.service;

import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.check.vo.*;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.mobile.check.vo.PhotoInfo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <pre></pre>
 * @author ★Luwei
 */
public interface CheckHandleService {

	/**
	 * 根据查勘表初始化查勘主信息
	 * @param checkActionVo
	 * @throws Exception
	 */
	public abstract CheckActionVo initCheckByCheck(Long checkId) throws Exception;

	// 保单险别
	public abstract List<PrpLCItemKindVo> findItemKind(String registNo,String calculateFlag);

	/**
	 * 状态为“未处理”时调用该方法初始化查勘显示,查询调度报案取值
	 */
	public abstract CheckActionVo initCheckBySchedule(Long scheduleTaskId,String registNo,SysUserVo userVo);

	/**
	 * 初始化标的车信息
	 * @param id
	 * @return PrpLCheckCarVo
	 */
	public abstract PrpLCheckCarVo initPrpLCheckCar(Long carId);

	public abstract Long getCheckId(String registNo);

	/**
	 * 更新查勘主信息
	 * @param prpLcheckVo
	 * @throws Exception
	 */
	public abstract void updateCheckMain(PrpLCheckVo prpLcheckVo);

	/**
	 * 首次接收任务保存
	 * @param scheduleTaskId
	 * @throws Exception
	 */
	public abstract Long saveCheckOnAccept(CheckActionVo checkActionVo,SysUserVo userVo) throws Exception;

	// 更新标的车Duty
	public abstract void updateMainCarDuty(PrpLCheckDutyVo checkDutyVo,PrpLCheckVo checkVo,String userCode);

	/**
	 * 保存新增的车损信息
	 * @param prpLCheckCarVo
	 * @throws Exception
	 */
	public abstract Long savePrpLCheckCar(PrpLCheckCarVo prpLCheckCarVo,PrpLCheckDutyVo checkDutyVo,String userCode,String isMobileCase) throws Exception;

	/**
	 * 更新车损信息
	 * @param prpLCheckCarVo
	 * @throws Exception
	 */
	public abstract Long updatePrpLCheckCar(PrpLCheckCarVo prpLCheckCarVo,PrpLCheckDutyVo checkDutyVo,String isMobileCase) throws Exception;

	/**
	 * 暂存-->保存财产/人伤 /扩展信息...
	 * @param checkVo
	 * @param checkTaskVo
	 * @throws Exception
	 */
	public abstract Long save(PrpLCheckVo prpLcheckVo,PrpLDisasterVo disasterVo,SysUserVo userVo,String saveType) throws Exception;

	// flag 节点未完成0,完成之后为1
	public abstract PrpLClaimTextVo createClaimText(Long checkId,String AuditStatus,String nodeCode,String flag,
													SysUserVo userVo) throws Exception;

	/** 获取报案车辆险别 */
	public abstract boolean getCarKind(String registNo);

	/**
	 * TODO 临时初始化查勘定损任务信息
	 * @param checkId
	 * @return
	 * @throws Exception
	 */
	public abstract List<PrpLScheduleDefLossVo> initCheckSubmitDloss(PrpLCheckVo checkVo,String url) throws Exception;

	/**
	 * 查勘提交定损数据准备
	 * @param checkId,userCode
	 * @throws Exception
	 */
	public abstract List<PrpLWfTaskVo> submitCheckToDloss(List<PrpLScheduleDefLossVo> defLossVos,Long checkId,
															Long flowTaskId,String flowId,double lossFees,
															SysUserVo userVo,String isMobileAccept,
															List<PhotoInfo> photoInfoList) throws Exception;

	// 查勘生成定损表
	public abstract Long saveScheduleDefLossTask(Long checkId,String userCode,
													Map<String,PrpLScheduleDefLossVo> carScheduleMap,
													Map<Long,PrpLScheduleDefLossVo> propScheduleMap,Long schTaskId);

	public abstract PrpLScheduleDefLossVo createDefLossFormPropVo(Long checkTaskId,PrpLCheckPropVo checkPropVo,
																	String userCode);

	// 创建定损carVo
	public abstract PrpLScheduleDefLossVo createDefLossFormCarVo(Long checkTaskId,PrpLCheckCarVo checkCarVo,
																	String userCode);

	/**
	 * 大案审核、复勘提交
	 * @param checkId,userCode
	 */
	public abstract void chkReOrBigSubmit(String codeName,Long checkId,Long flowTaskId,String chkBigUser,
											SysUserVo currentUserVo);

	// 获取下一节点名称
	public abstract FlowNode getNextNodeCode(String codeName,Long checkId);

	/**
	 * 初始化新增的三者车
	 * @param registNo
	 * @return PrpLCheckCarVo
	 */
	public abstract PrpLCheckCarVo initAddThirdCar(String registNo);

	/** 删除查勘新增的三者车 */
	public abstract void deleteThirdCar(Long thCarId) throws Exception;

	/**
	 * 更新报案出险经过
	 * @param registNo
	 * @param dangerRemark
	 */
	public abstract void updateDangerRemark(String registNo,String dangerRemark,String userCode);

	/**
	 * 保存成功，建checkDuty表，
	 * @param registNo
	 * @throws Exception
	 */
	public abstract double saveSuccess(String registNo) throws Exception;

	// 获取大案审核标志
	public abstract String getMajorFlag(Long checkId) throws Exception;

	// 获取费用总和
	public abstract double getLossFee(Long checkId) throws Exception;

	// 保存公估费
	public abstract void saveCharge(List<PrpLDlossChargeVo> lossChargeVos,Long checkId,SysUserVo userVo) throws Exception;

	// 更新免赔率
	public abstract void updateClaimDeduct(List<PrpLClaimDeductVo> claimDeductVos,String registNo);

	/**
	 * 点击互陪自赔校验
	 * @param registNo
	 * @return
	 * @modified: ☆Luwei(2016年4月22日 下午3:19:02): <br>
	 */
	public abstract String subRadioValid(String registNo);

	/**
	 * <pre>
	 * 校验是否存在有效的人伤任务
	 * </pre>
	 * @param registNo
	 * @modified: ☆Luwei(2016年8月23日 上午9:37:48): <br>
	 */
	public abstract boolean validPersonTask(String registNo);

	public abstract PrpLPayCustomVo getPayCusInfo(Long id);

	/**
	 * 获取出现保单信息 ☆Luwei(2016年2月26日 下午7:18:14): <br>
	 */
	public abstract List<PrpLCMainVo> getPolicyAllInfo(String registNo);

	// 获取收款人信息
	public abstract PrpLPayCustomVo findPayCustomVoByRegistNo(String registNo);

	// 获取公估资费标准
	public abstract Map<String,String> getIntermStanders(String userCode);

	/**
	 * 获取收款人
	 * @param registNo
	 */
	public abstract Map<String,String> getCustom(String registNo);

	/**
	 * 1,校验未处理车辆 ,flag存在空就还有没处理完的车辆 2,车辆责任比例之和 3,互碰自赔校验
	 */
	public abstract String validHandleCar(PrpLCheckVo checkVo,PrpLCheckDutyVo checkDuty,
			PrpLSubrogationMainVo subrogationMainVo);

	/**
	 * 1-互碰自赔, 2 - 标的车未承保车上货物险 3-估损金额,4-车辆损失
	 */
	public abstract String validCheckClaim(String registNo);

	/**
	 * 车辆保存校验
	 * @param licenseType
	 */
	public abstract String saveValidCar(int seriNo,String registNo,String licenseNo,String frameNo,
	       String vinNo,Long carId,String idNoType,String idNo,Date checkTime, String licenseType,String drivingLicenseNo,String carkindcode);

	/**
	 * 车辆保存校验
	 * @param <E>
	 * @param licenseType
	 */
	public abstract <E> String saveValidCars(List<E>  carList);
	
	/**
	 * 获取财产人伤的损失方
	 * @param registNo
	 * @return Map
	 */
	public abstract Map<String,String> getCarLossParty(String registNo);

	// 获取代位的三者车车牌号下拉框
	public abstract Map<String,String> getSubLicenseNo(String registNo);

	// 保存代位信息
	public abstract Long saveSubrogationMain(PrpLSubrogationMainVo subMainVo,Long checkId,String assignUser);

	/**
	 * 查勘提交时-->查勘信息送平台
	 * @param checkVo
	 * @throws Exception
	 * @modified: ☆Luwei(2016年5月3日 下午5:22:23): <br>
	 */
	public abstract void sendCheckToPlatform(String registNo) throws Exception;

	/**
	 * 通过registNo查询carVoList
	 * @param registNo
	 * @return PrpLCheckCarVo--->List
	 */
	public abstract List<PrpLCheckCarVo> findPrpLcheckCarVoByRegistNo(String registNo);


	/**
	 * 保存巨灾
	 */
	public abstract void saveDisaster(PrpLDisasterVo disasterVo);

	/**
	 * 通过主键查询查勘主表
	 * @param id
	 * @return PrpLCheckVo
	 */
	public abstract PrpLCheckVo findPrpLCheckVoById(Long id);

	public abstract PrpLCheckVo queryPrpLCheckVo(String registNo);

	public abstract PrpLCheckCarVo findCarIdBySerialNoAndRegistNo(Integer serialNo,String registNo);

	public abstract void dropPropLoss(Long id) throws Exception;

	public abstract void dropPersonLoss(Long id) throws Exception;

	public abstract PrpLCheckCarVo findCheckCarById(Long id);

	/**
	 * 根据报案号查询巨灾
	 * @param registNo
	 * @return PrpLDisasterVo
	 */
	public abstract PrpLDisasterVo findDisasterVoByRegistNo(String registNo);

	/**
	 * <pre>
	 * 查询大案审核的人员列表
	 * </pre>
	 * @param nodeCode、comCode、level
	 * @return
	 * @modified: ☆Luwei(2016年10月19日 下午6:21:24): <br>
	 */
	public abstract Map<String,String> findByChkBigAndGradeid(String nodeCode,String comCode,String level);

	/**
	 * <pre>
	 * 保存公估信息
	 * </pre>
	 * @param registNo
	 * @param userVo
	 * @modified: ☆Luwei(2016年10月9日 下午8:35:23): <br>
	 */
	public abstract void saveAssessor(String registNo,SysUserVo userVo);
	
	/**
	 * <pre>
	 * 保存查勘费信息
	 * </pre>
	 * @param registNo
	 * @param userVo
	 * @modified: ☆yzy(2019年8月3日 下午8:35:23): <br>
	 */
	public abstract void saveCheckFee(String registNo,SysUserVo userVo);
	
	/**
	 * 是否强制立案
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified: *牛强(2017年6月26日 下午12:31:48): <br>
	 */
	public boolean isClaimForce(String registNo);
	
	/**
	 * 定损照片是否审核通过送快赔系统，checkStatus：0-不通过，1-审核通过
	 */
	public void photoVerifyToHNQC(String registNo,String photoStatus,String checkId,String checkStatus,String dataType,SysUserVo userVo)throws Exception;
	
	
	public void updatePrplCheckCarInfo(PrpLCheckCarInfoVo prplCheckCarInfoVo);
	
	/**
	 * 定损注销更新单车事故字段
	 * @param registNo
	 */
	public void updateSingleAccidentFlag(String registNo);
	
	/**
	 * 根据报案号，查找prplcheckcarinfo
	 */
	public List<PrpLCheckCarInfoVo> findAllInfoByRegistNo(String registNo);
	
	/**
	 * 根据机构与人员岗位表Id查询该岗位下所含的人员信息
	 * @param comCode
	 * @param gradeid
	 * @return
	 */
	public List<SysCodeDictVo> findByComCodeAndGradeid(String comCode,Long gradeid);

	/**
	 * <pre></pre>
	 * @param checkCarVo
	 * @modified: ☆Wurh(2019年1月29日 下午5:17:44): <br>
	 */
	public void updateCheckCar(PrpLCheckCarVo checkCarVo);
	/**
	 * 公估获取险种代码
	 * @param registNo
	 * @return
	 */
	public String getCarKindCode(String registNo);
	
	/**
	 * 条件报案号registNo与flag
	 * @param registNo
	 * @param flag
	 * @return
	 */
	public List<PrpLCheckCarInfoVo> findAllInfoByRegistNoAndFlag(String registNo,String flag);
	
	/**
	 * 条件报案号registNo与flag
	 * @param registNo
	 * @param flag
	 * @return
	 */
	public List<PrpLCheckPropVo> findAllPropByRegistNoAndFlag(String registNo,String flag);
	
	/**
	 * 条件报案号registNo与flag
	 * @param registNo
	 * @param flag
	 * @return
	 */
	public List<PrpLCheckPersonVo> findAllPersonByRegistNoAndFlag(String registNo,String flag);
	/**
	 * 通过id更新
	 * @param id
	 */
	public void updatePrpLCheckCarInfo(Long id,String flag);
	/**
	 * 通过id更新
	 * @param id
	 */
	public void updatePrpLCheckProp(Long id,String flag);
	
	/**
	 * 通过id更新
	 * @param id
	 */
	public void updatePrpLCheckPerson(Long id,String flag);

	/**
	 * 添加查勘费任务
	 * @param mainVo
	 * @param userVo
	 * @modified:
	 * ☆XiaoHuYao(2019年8月14日 上午10:30:13): <br>
	 */
	public  void saveAssessors(String registNo,SysUserVo userVo);


	PrpLCheckDriverVo findPrplcheckcarinfoByRegistNoAndLicenseNo(String registNo, String licenseNo);
}
