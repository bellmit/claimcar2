package ins.sino.claimcar.losscar.service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.support.QueryRule;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.vo.CarQueryReslutVo;
import ins.sino.claimcar.losscar.vo.CarQueryVo;
import ins.sino.claimcar.losscar.vo.ClaimFittingVo;
import ins.sino.claimcar.losscar.vo.PrpLCaseComponentVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossprop.vo.PropQueryReslutVo;

import java.sql.SQLException;
import java.util.List;

/**
 * 车辆定损对外接口
 * @author ★yangkun
 */
public interface LossCarService {

	public PrpLDlossCarMainVo findLossCarMainById(Long id);
	public PrpLDlossCarMainVo findLossCarMainByRegistNoAndPaId(String registNo,Long paId);
	public PrpLDlossCarMainVo findLossCarMainByRegistNoAndLicenseNo(String registNo,String licenseNo);
	
	
	/**
	 * 更新 定损 车辆信息表
	 * @param dlossCarMainVo
	 * @modified: ☆Luwei(2016年4月21日 上午11:15:06): <br>
	 */
	public void updateDlossCarMain(PrpLDlossCarMainVo dlossCarMainVo);
	
	/**
	 * 报案号--查询定损车辆表
	 * @param regsitNo
	 * @return List<PrpLDlossCarMainVo>
	 */
	public List<PrpLDlossCarMainVo> findLossCarMainByRegistNo(String registNo);
	
	/**
	 * 核赔通过创建该表
	 * @param caseVoLost
	 */
	public void createCaseComponent(List<PrpLCaseComponentVo> caseVoList);
	
	/**
	 * 通过报案号查询已核损任务
	 * @return
	 * @modified: ☆XMSH(2016年3月23日 下午12:03:15): <br>
	 */
	public ResultPage<PrpLDlossCarMainVo> findLossCarMainPageByRegistNo(String registNo,int start,int length);
	
	/**
	 * 根据报案号查询定损车辆信息
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月9日 下午6:24:53): <br>
	 */
	public List<PrpLDlossCarInfoVo> findPrpLDlossCarInfoVoListByRegistNo(String registNo);
	
	/**
	 * 根据逐渐查找唯一数据
	 * 
	 * <pre></pre>
	 * @param Id
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月12日 上午9:07:24): <br>
	 */
	public PrpLDlossCarInfoVo findPrpLDlossCarInfoVoById(Long id);
	
	/**
	 * 根据条件查询数据
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @param lossState
	 * @param underWriteFlag
	 * @param serialNo
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月26日 下午3:57:10): <br>
	 */
	public List<PrpLDlossCarMainVo> findPrpLDlossCarMainVoListByCondition(String registNo,List<String> lossState,List<String> underWriteFlag,String serialNo);
	
	/**
	 * 根据QueryRule查找记录
	 * 
	 * <pre></pre>
	 * @param queryRule
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月27日 下午5:03:55): <br>
	 */
	public List<PrpLDlossCarMainVo> findPrpLDlossCarMainVoListByRule(QueryRule queryRule);
	
	/**
	 * 通过报案号和车辆序号查找定损信息
	 * @param registNo
	 * @param serialNo
	 * @return
	 * @modified: ☆YangKun(2016年6月24日 上午10:49:16): <br>
	 */
	public List<PrpLDlossCarMainVo> findLossCarMainBySerialNo(String registNo,Integer serialNo);

	/**
	 * 修改定损最后一个核损任务 发起理算
	 * @param registNo
	 * @param serialNo
	 * @return
	 * @modified: ☆YangKun(2016年6月24日 上午10:49:16): <br>
	 */
	public List<PrpLWfTaskVo> modifyToSubMitComp(String registNo,SysUserVo userVo);

	// 理算注销是发起定损
	public String carModifyLaunch(Long lossId, SysUserVo deflossVo);
	
	// 查询配件
	public List<PrpLCaseComponentVo> findCaseCompList(String compCode,String frameNo);
	
	// 追加定损
	public String carAdditionLaunch(Long id,SysUserVo userVo);
	
	public String sendXMLData(ClaimFittingVo claimFittingVo,SysUserVo userVo) throws Exception;


	public ResultPage<CarQueryReslutVo> findPageForAdjust(CarQueryVo carQueryVo) throws Exception ;


	/**
	 * 精友返回接口
	 * @param operateType
	 * @param strData
	 * @return
	 * @throws Exception
	 */
	public String doTransData(String operateType, String strData)throws Exception;
	
	/**
	 * 财产定损
	 * @param queryVo
	 * @return
	 * @throws Exception
	 */
	public ResultPage<PropQueryReslutVo> findPropPageForAdjust(CarQueryVo queryVo) throws Exception;
	
	/**
	 * 根据报案号和id寻找唯一车辆定损主表
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @param id
	 * @return
	 * @modified: ☆WLL(2016年11月8日 下午7:50:28): <br>
	 */
	public PrpLDlossCarMainVo findDlossCarMainVoByRegistNoAndId(String registNo,Long id);
	
	/**
	 * 报案提交时，历次出险出现过全车盗抢、推定全损，报案提交需给出软提示
	 * @author WLL
	 */
	public Boolean checkCarAllLossHisByPolicyNo(String PolicyNo);
	
	/**
	 * 报案提交时，历次出险出现过全车盗抢返回03，出现过推定全损返回02，没有则返回0
	 * @param PolicyNo
	 * @return
	 */
	public String checkCarLossHisByPolicyNo(String PolicyNo);
	
	public PrpLDlossCarInfoVo findDefCarInfoByPk(Long id);
	
	public void updateLossCarInfo(PrpLDlossCarInfoVo lossCarInfo);
	
	/**
	 * 根据scheduleDeflossId跟registNo查询PrpLDlossCarMainVo
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @param scheduleDeflossId
	 * @return
	 * @modified: ☆zhujunde(2017年12月4日 上午9:42:36): <br>
	 */
	public List<PrpLDlossCarMainVo> findLossCarMainByScheduleDeflossId(String registNo,Long scheduleDeflossId);
	
	/**
	 * 根据报案号和underWriteFlag查询车损主表，flag=1等于underWriteFlag，flag=0不等于underWriteFlag
	 * @param registNo
	 * @param underWriteFlag
	 * @param flag
	 * @return
	 */
	public List<PrpLDlossCarMainVo> findLossCarMainByUnderWriteFlag(String registNo,String underWriteFlag,String flag);
	
	/**
	 * 根据车id查询有没跟精友交互过
	 * @param defLossMainId
	 * @return
	 */
	public boolean findJyInfoByDefLossMainId(Long defLossMainId);
	/**
	 * 查询PrpLDlossCarMain
	 * @param registNo
	 * @param serialNo
	 * @param licenseNo
	 * @return
	 */
	public List<PrpLDlossCarMainVo> findPrpLDlossCarMainVoByOther(String registNo,Integer serialNo,String licenseNo);
	
	/**
	 * 精友2代定损保存数据
	 * @param lossCarMainVo
	 * @param lossCarInfoVo
	 * @throws SQLException
	 */
	public void saveJyDeflossInfo(PrpLDlossCarMainVo lossCarMainVo, PrpLDlossCarInfoVo lossCarInfoVo) throws Exception;
	
	/**
	 * 精友核价,核损保存数据
	 * @param lossCarMainVo
	 * @param type
	 */
	public void saveByJyDeflossCheck(PrpLDlossCarMainVo lossCarMainVo,String type);

	/**
	 * 精友复检,复检更新数据
	 * 
	 * <pre></pre>
	 * @param prpLDlossCarMainVo
	 * @modified: ☆LiYi(2018年9月21日 下午8:02:32): <br>
	 */
	public void updateJyDlossCarMain(PrpLDlossCarMainVo prpLDlossCarMainVo);
	/**
	 * 验证标的车损失类型是否必传问题
	 * @param registNo
	 * @throws Exception
	 */
	public List<String> kindCodes(String registNo);
	
	/**
	 * 保存或者更新prpLDlossCarCompVos
	 * @param prpLDlossCarCompVos
	 * @param dlossCarMainVo
	 */
	public void saveOrUpdateCarComp(List<PrpLDlossCarCompVo> prpLDlossCarCompVos,PrpLDlossCarMainVo dlossCarMainVo);
	
	/**
	 * 根据id查询定核损主表，获取定损人，修理厂信息
	 * @param id
	 * @return
	 */
	public PrpLDlossCarMainVo findLossCarMainbyId(Long id);
	/**
	 * 更新定损主表与子表
	 * @param prpLDlossCarMainVo
	 */
	public void updateAndSaveAndDelSons(PrpLDlossCarMainVo prpLDlossCarMainVo);
}
