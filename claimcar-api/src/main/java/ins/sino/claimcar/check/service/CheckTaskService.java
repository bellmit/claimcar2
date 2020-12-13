/******************************************************************************
 * CREATETIME : 2015年12月8日 上午10:11:26
 ******************************************************************************/
package ins.sino.claimcar.check.service;
import ins.sino.claimcar.check.vo.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 查勘API服务
 * @author ★Luwei
 */
public interface CheckTaskService {

	/**
	 * 根据registNo查询查勘主表CheckVo
	 * @param registNo ☆Luwei(2016年2月23日 下午4:12:52): <br>
	 */
	public PrpLCheckVo findCheckVoByRegistNo(String registNo);

	/**
	 * 根据查勘车辆第 --查询查勘车辆主表ChecCarVo
	 * @param checkId ☆Luwei(2016年2月23日 下午4:13:51): <br>
	 */
	public PrpLCheckCarVo findByCheckId(Long checkId);
	
	/**
	 * 根据报案号查询车辆信息表
	 * @param registNo ☆Luwei(2016年3月17日 下午6:02:39): <br>
	 */
	public List<PrpLCheckCarVo> findCheckCarVo(String registNo);
	
	/**
	 * 根据报案号查询查勘人员信息表
	 * @param registNo
	 * ☆XMSH(2016年8月8日 下午4:36:31): <br>
	 */
	public List<PrpLCheckPersonVo> findCheckPersonVo(String registNo);
	/**
	 * 根据车辆驾驶员的主表Id查车辆驾驶员表
	 * @param id
	 * @return
	 */
	public PrpLCheckDriverVo findPrpLCheckDriverVoById(Long id);

	/**
	 * 根据ID查勘财产propVo
	 * @param id ☆Luwei(2016年2月23日 下午4:14:49): <br>
	 */
	public PrpLCheckPropVo findByCheckPropVoById(Long id);

	/**
	 * 根据报案号查询财产损失表
	 * @param registNo ☆Luwei(2016年3月17日 下午6:03:04): <br>
	 */
	public List<PrpLCheckPropVo> findCheckPropVo(String registNo);
	
	/**
	 * 根据id查勘人伤personVo
	 * @param id
	 * @return
	 * @modified:
	 * ☆XMSH(2016年8月12日 下午12:57:10): <br>
	 */
	public PrpLCheckPersonVo findCheckPersonVpById(Long id);
	
	/**
	 * 根据ID查勘CheckTaskVo
	 * @param id ☆Luwei(2016年2月23日 下午4:16:31): <br>
	 */
	public PrpLCheckTaskVo findCheckTaskVoById(Long id);

	/**
	 * 根据ID查勘CheckVo
	 * @param id ☆Luwei(2016年2月23日 下午4:17:02): <br>
	 */
	public PrpLCheckVo findCheckVoById(Long id);
	
	/**
	 * 根据ID查勘prpLCheckDutyVo
	 */
	public PrpLCheckDutyVo findprpLCheckDutyById(Long id);

	/**
	 * 根据报案号和checkCarId删除checkDuty表
	 * @param registNo checkCarId ☆Luwei(2016年2月23日 下午4:17:22): <br>
	 */
	public void deleteCheckDutyByCheckId(String registNo,Integer serialNo);
	
	/**
	 * 查询车辆责任表(PrpLCheckDuty)
	 * @param registNo itemNo ☆Luwei(2016年2月23日 下午4:18:29): <br>
	 */
	public PrpLCheckDutyVo findCheckDuty(String registNo,Integer itemNo);

	/**
	 * 根据报案号查询checkDutyList
	 * @param registNo ☆Luwei(2016年2月23日 下午4:18:53): <br>
	 */
	public List<PrpLCheckDutyVo> findCheckDutyByRegistNo(String registNo);

	/**
	 * 保存查勘车辆责任表
	 * @param checkDutyVo ☆Luwei(2016年2月23日 下午4:19:10): <br>
	 */
	public void saveCheckDuty(PrpLCheckDutyVo checkDutyVo);
	
	public void saveCheckDutyHis(String registNo,String remark);
	
	/**
	 * 根据报案号、车辆序号查询车损表
	 * @param registNo
	 * @param serialNo ☆Luwei(2016年2月23日 下午4:19:29): <br>
	 */
	public PrpLCheckCarVo findCheckCarBySerialNo(String registNo,Integer serialNo);
	
	/**
	 * 根据报案号查询查勘车辆信息
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月9日 下午6:27:03): <br>
	 */
	public List<PrpLCheckCarInfoVo> findPrpLCheckCarInfoVoListByRegistNo(String registNo);
	
	/**
	 * 根据主键查询唯一一条记录
	 * 
	 * <pre></pre>
	 * @param id
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月11日 上午11:10:11): <br>
	 */
	public PrpLCheckCarVo findPrpLCheckCarVoById(Long id);
	
	/**
	 * 根据主键查询唯一一条记录
	 * 
	 * <pre></pre>
	 * @param id
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月12日 上午9:29:07): <br>
	 */
	public PrpLCheckCarInfoVo findPrpLCheckCarInfoVoById(Long id);
	
	public Map<String,String> getCarLossParty(String registNo);
	
	public void saveCheckDutyList(List<PrpLCheckDutyVo> checkDutyList);
	
	/**
	 * 更新或者保存checkDutyList
	 * @param checkDutyList
	 */
	public void saveOrUpdateCheckDutyList(List<PrpLCheckDutyVo> checkDutyVoList);

	/**
	 * 查询巨灾信息
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified: ☆WLL(2016年7月22日 上午10:29:19): <br>
	 */
	public PrpLDisasterVo findDisasterVoByRegistNo(String registNo);
	
	/**
	 * 保存巨灾信息
	 * @param prpLDisasterVo
	 */
	public void saveDisasterVo(PrpLDisasterVo prpLDisasterVo);
	
	/**
	 * 删除巨灾数据
	 * @param id
	 */
	public void deleteDisasterVo(Long id); 
	
	/**
	 * 删除prplcheckcar表
	 * @param PrpLCheckCarVo
	 */
	public void deleteCheckCar(PrpLCheckCarVo checkCarVo);
	
	
	/**
	 * 根据报案号和id查询唯一的查勘车辆损失表
	 * <pre></pre>
	 * @param registNo
	 * @param id
	 * @return
	 * @modified:
	 * ☆WLL(2016年11月8日 下午8:13:21): <br>
	 */
	public PrpLCheckCarVo findCheckCarVoByRegistNoAndId(String registNo,Long id);

	/*
	 * 更新PrpLCheckCarVo
	 */
	public void updateCheckCar(PrpLCheckCarVo checkCarVo);
	
	public void updateCheck(PrpLCheckVo checkVo);
	
	/**
	 * 根据报案号和传入的参数排序
	 * <pre></pre>
	 * @param registNo
	 * @param serialNo
	 * @return
	 * @modified:
	 * ☆zhujunde(2017年6月28日 下午6:03:56): <br>
	 */
	public List<PrpLCheckCarVo> findCheckCarVoByRegistNoAndSerialNo(String registNo,String serialNo);

	/**
	 * 通过报案号查询
	 * @param registNo
	 * @return
	 */
	public List<PrpLCheckExtVo> findPrpLcheckExtVoByRegistNo(String registNo);

	/**
	 * 根据报案号跟车牌号查询
	 * <pre></pre>
	 * @param registNo
	 * @param licenseNo
	 * @return
	 * @modified:
	 * ☆zhujunde(2017年12月19日 上午11:30:30): <br>
	 */
    public List<PrpLCheckCarInfoVo> findPrpLCheckCarInfoVoListByOther(String registNo,String licenseNo);

    public void updatePrpLCheckDuty(String registNo);
    
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
	 * 通过用户代码查询查勘指标信息
	 * @param userCode
	 */
	PrpLIndiQuotaInfoVo getCheckIndexInfoByUserCode(String userCode) throws IOException;
	
	/**
	 * 根据报案号查车辆驾驶员表
	 * @param id
	 * @return
	 */
	public List<PrpLCheckDriverVo> findPrpLCheckDriverVoByRegistNo(String registNo);
}
