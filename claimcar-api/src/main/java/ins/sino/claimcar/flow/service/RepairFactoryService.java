package ins.sino.claimcar.flow.service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.support.Page;
import ins.sino.claimcar.claimjy.vo.JyResVo;
import ins.sino.claimcar.manager.vo.PrpLAgentFactoryVo;
import ins.sino.claimcar.manager.vo.PrpLInsuredFactoryVo;
import ins.sino.claimcar.manager.vo.PrpLRepairBrandVo;
import ins.sino.claimcar.manager.vo.PrpLRepairFactoryVo;
import ins.sino.claimcar.manager.vo.PrpLRepairPhoneVo;
import ins.sino.claimcar.manager.vo.PrplSysAgentfactoryVo;

import java.util.List;
import java.util.Map;


public interface RepairFactoryService {

	/*
	 * 查询所有修理厂
	 */
	public abstract Page<PrpLRepairFactoryVo> findAllFactory(
			PrpLRepairFactoryVo repairFactoryVo, int start, int length);

	/*
	 * 查询所有修理厂
	 */
	public abstract List<PrpLRepairFactoryVo> findAllFactory(
			PrpLRepairFactoryVo repairFactoryVo);

	/**
	 * 根据id查询修理厂
	 * @param repairId
	 * @modified: ☆Luwei(2016年6月22日 上午9:36:58): <br>
	 */
	public abstract PrpLRepairFactoryVo findFactoryById(String repairId);

	/*
	 * 异步查询factorycode是否在数据库中存在
	 */
	public abstract boolean findFactoryCode(String factoryCode,Long id);
	/**
	 * 通过查询factoryName来查找对应的修理厂VO
	 * @param factoryName
	 * @return
	 */
	public abstract String findByFactoryName(String factoryName);
	
	

	/** 保存修理厂信息
	 * @param repairFactoryVo
	 * @param repairBrandVos
	 * @param userCode
	 * @modified: ☆Luwei(2016年6月21日 下午7:26:35): <br>
	 */
	public abstract PrpLRepairFactoryVo savaOrUpData(PrpLRepairFactoryVo repairFactoryVo,
			List<PrpLRepairBrandVo> repairBrandVos,List<PrpLRepairPhoneVo> repairPhoneVos, String userCode);

	/**
	 * 更新修理厂
	 * @modified:
	 * ☆Luwei(2016年6月22日 上午10:25:25): <br>
	 */
	public abstract void updateRepairFactory(Long repairId, String userCode);

	/**
	 * 根据修理厂类型、修理厂代码查询修理厂信息
	 * @param factoryCode
	 * @param factoryType
	 * @return
	 * @modified: ☆Luwei(2016年6月21日 下午7:32:26): <br>
	 */
	public abstract PrpLRepairFactoryVo findFactoryByCode(String factoryCode,
			String factoryType);
	/**
	 * 根据修理厂代码查询修理厂信息
	 * @param factoryCode
	 * @return
	 */
	public abstract PrpLRepairFactoryVo findFactoryByCode(String factoryCode);

	/** 根据出险地、品牌名称查询出所有的修理厂信息
	 * @param areaCode--修理厂地址
	 * @param brandName--品牌名称
	 * @modified: ☆Luwei(2016年6月23日 下午1:11:04): <br>
	 */
	public abstract List<PrpLRepairFactoryVo> findFactoryByAreaCode(
			String areaCode, String brandName);

	/*
	 * 车辆定损页面查询修理厂
	 */
	public abstract Page<PrpLRepairFactoryVo> findRepariFactory(
			PrpLRepairFactoryVo PrpLRepairFactoryVo, int start, int length,String index);
	
	//查找代理人信息
	public Map<String, String> findAgentCode(String comCode);

	/**
	 * 根据机构和代理人查询修理厂信息，返回最新维护且修理厂电话不为空的记录
	 * @param comCode
	 * @param agentCode
	 * @return
	 */
	public abstract PrpLRepairFactoryVo findFactory(String comCode,String agentCode,String insuredCode, String userCode);

	/**
	 * 查找个性被保人 
	 * <pre>agentCode</pre>
	 * @param agentCode
	 * @return 该代理人代理的保单所对应的被保险人
	 * @modified:
	 * *牛强(2017年2月28日 上午10:54:16): <br>
	 */
	public Map<String, String> findByAgentCode(String userInfo,String agentCode);
	/**
	 * 查找代理人
	 * <pre></pre>
	 * @param id
	 * @return
	 * @modified:
	 * *牛强(2017年3月2日 上午10:29:44): <br>
	 */
	public List<PrpLAgentFactoryVo> findAgentFactoryByFactoryId(Long factoryId); 
	
	/**
	 * 保存代理人
	 * <pre></pre>
	 * @param agentFactory
	 * @modified:
	 * *牛强(2017年3月2日 上午10:32:19): <br>
	 */
	public void saveOrUpdateAgentFactory(List<PrpLAgentFactoryVo> agentFactory,PrpLRepairFactoryVo repairFactoryVo);
	
	/**
	 * 保存被保险人
	 * <pre></pre>
	 * @param insuredFactory
	 * @return
	 * @modified:
	 * *牛强(2017年3月2日 上午10:38:19): <br>
	 */
	public void saveInsuredFactory(List<PrpLInsuredFactoryVo> insuredFactory,Long agentId) throws Exception;
	
	/**
	 * 查找被保险人
	 * <pre></pre>
	 * @param agentcode
	 * @modified:
	 * *牛强(2017年3月2日 上午10:40:08): <br>
	 */
	public List<PrpLInsuredFactoryVo> findInsuredFactoryByAgentId(String agentId);


	/**
	 * 查找被保险人
	 * <pre></pre>
	 * @param agentcode
	 * @modified:
	 * *牛强(2017年3月2日 上午10:40:08): <br>
	 */
	public Map<String,String> findInsuredFactoryByAgentCode(String agentCode);
	/**
	 * 从保单表查找所有代理人信息
	 * <pre></pre>
	 * @return
	 * @modified:
	 * *牛强(2017年3月10日 上午10:19:48): <br>
	 */
	public Map<String, String> findAgentInfoFromPrpLCmain();

	 /**
	  * 
	  * 查询代理人信息
	  * @param lAgentFactoryVo
	  * @param start
	  * @param length
	  * @return
	  * @modified:
	  * ☆zhujunde(2017年6月7日 上午10:10:22): <br>
	  */
    public abstract Page<PrplSysAgentfactoryVo> findLAgentFactory(
            PrpLAgentFactoryVo lAgentFactoryVo, int start, int length);

	/**
	 * 查询可修品牌
	 *
	 * @param prpLRepairBrandVo
	 * @param start
	 * @param length
	 * @return
	 * @author huanggusheng
	 */
	ResultPage<PrpLRepairBrandVo> findRepairBrand(PrpLRepairBrandVo prpLRepairBrandVo, int start, int length) throws Exception;

    /**
     * 
     * 查询代理人信息
     * @param agentCode
     * @return
     * @modified:
     * ☆zhujunde(2017年6月7日 下午7:32:39): <br>
     */
    public abstract List<PrplSysAgentfactoryVo> findLAgentFactoryByOther(String agentCode);
    
    /**
     * 理赔系统修改修理厂信息后，需要实时同步给精友系统 （朱彬）
     * @param repairFactory
     * @param repairBrandVos
     * @param agentFactoryVos
     * @param status  用于判断 状态（status）
     * @return
     */
    public abstract JyResVo factorySyncJy(PrpLRepairFactoryVo repairFactory,List<PrpLRepairBrandVo> repairBrandVos,
    		List<PrpLAgentFactoryVo> agentFactoryVos,String status);

    /**
     * 通过推送修手机号码匹配关联的修理厂
     * @param telephoneNumber
     * @return
     */
    public abstract Long findRepairFactoryBy(String telephoneNumber);
    
    /**
     * 根据PrpLRepairPhoneVo的手机号查询
     * @param repairPhoneVos
     * @return
     */
	public abstract Map<String, String> findAllPhone(
			List<PrpLRepairPhoneVo> repairPhoneVos); 

}