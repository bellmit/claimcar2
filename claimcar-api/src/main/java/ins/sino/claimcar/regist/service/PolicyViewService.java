/******************************************************************************
 * CREATETIME : 2015年11月18日 下午3:30:59
 ******************************************************************************/
package ins.sino.claimcar.regist.service;

import ins.sino.claimcar.regist.vo.PolicyEndorseInfoVo;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;

import java.util.List;
import java.util.Map;


/**
 * 报案时保单查看方法，多个节点都会调用保单查看方法，实现类在regist中
 * @author ★LiuPing
 * @CreateTime 2015年11月18日
 */
public interface PolicyViewService {

	
	/**
	 * 查看报案时保单信息
	 * @param registNo
	 * @return
	 * @add by dengkk
	 */
	public List<PrpLCMainVo> getPolicyAllInfo(String registNo); 
	
	/**
	 * 根据保单号查询保单信息
	 * @param policyNo
	 * @return
	 * @modified:
	 * ☆XMSH(2016年3月24日 下午3:19:46): <br>
	 */
	public PrpLCMainVo getPolicyInfo(String registNo,String policyNo);
	
	/**
	 * 保单取消关联，平台查询取消保单信息
	 * @param policyNo
	 * @return
	 * @modified:
	 * ☆Luwei(2016年3月24日 下午3:19:46): <br>
	 */
	public PrpLCMainVo findPolicyInfoByPaltform(String registNo,String policyNo);
	
	/**
	 * 查看案件的险别信息
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆yangkun(2015年12月18日 下午3:40:53): <br>
	 */
	public List<PrpLCItemKindVo> findItemKinds(String registNo,String calculateFlag);
	/**
	 * 查询保单 车上货物责任险 保额
	 */
	public PrpLCItemKindVo findItemKindByKindCode(String registNo,String calculateFlag);
	
	/**
	 * 通过保单,报案号，险别查询冲减保单表
	 * @param policyNo
	 * @param registNo
	 * @param kindcode
	 * @return
	 */
     public List<PrpLCItemKindVo> findItemKindVos(String policyNo,String registNo,String kindcode);
	
	/**
	 * 根据报案号和保单号取唯一数据
	 * 
	 * <pre></pre>
	 * @param registNo 报案号
	 * @param policyNo 保单号
	 * @return
	 * @modified: ☆ZhouYanBin(2016年3月17日 上午11:54:38): <br>
	 */
	public PrpLCMainVo getPrpLCMainByRegistNoAndPolicyNo(String registNo,String policyNo);

	/**
	 * 根据报案号和保单号取险别信息
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @param policyNo
	 * @return
	 * @modified: ☆ZhouYanBin(2016年3月23日 下午3:32:44): <br>
	 */
	public List<PrpLCItemKindVo> findItemKindVoListByRegistNoAndPolicyNo(String registNo,String policyNo);
	
	
	/**
	 * 根据报案号查询报案时保单信息（浅查询）
	 * @param registNo
	 * @return
	 */
	public List<PrpLCMainVo> findPrpLCMainVoListByRegistNo(String registNo); 

	
	//根据立案号跟报案号查询报案信息
	public PrpLCMainVo getRegistNoInfo(String registNo);

	
	/**
	 * 判断保单是否包含此险别代码
	 * <pre></pre>
	 * @param prpLCMainVo 
	 * @param kindCode 险别代码
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年4月14日 上午9:13:07): <br>
	 */
	public boolean isInsuredKindCode(PrpLCMainVo prpLCMainVo,String kindCode);
	
	/**
	 * 该险别是否承保了不计免赔险
	 * <pre></pre>
	 * @param registNo 案件号
	 * @param kindCode 险别代码
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年4月14日 下午6:04:11): <br>
	 */
	public boolean isMKindCode(String registNo,String kindCode);
	
	/**
	 * 查询承保车辆信息
	 * <pre></pre>
	 * @param registNo
	 * @param policyNo
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年4月18日 下午3:10:51): <br>
	 */
	public PrpLCItemCarVo findItemCarByRegistNoAndPolicyNo(String registNo,String policyNo);
	

	/**
	 * 判断案件是否承保费改以后的保单
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年4月29日 上午9:10:43): <br>
	 */
	public boolean isNewClauseCode(String registNo);

	/**
	 * 判断案件是否承保2020费改以后的保单
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年4月29日 上午9:10:43): <br>
	 */
	public boolean isNew2020ClauseCode(String registNo);

	//报案时临时保单信息主表
	List<PrpLCMainVo> getPolicyForPrpLTmpCMain(String registNo);

	/**
	 * 查询投保确认码
	 * @param registNo-报案号
	 * @param classType-险类代码（交钱还是商业保单的投保来确认码）-->11-交强，12-商业
	 * @return
	 */
	public String findValidNo(String registNo,String classType);
	
	/**
	 * 查询保单的机构代码
	 * @param registNo-报案号
	 * @param policyType-保单类型（11-交强,12-商业）
	 * @return
	 */
	public String findPolicyComCode(String registNo,String policyType);
	
	/**
	 * 查询保单的comcode(双保单取商业险)
	 * @param registNo
	 * @return
	 */
	public String getPolicyComCode(String registNo);
	
	/**
	 * <pre>查询批单纪录</pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆Luwei(2016年9月6日 下午7:24:20): <br>
	 */
	public List<PolicyEndorseInfoVo> findPolicyEndorseInfo(String registNo);
	
	/**
	 * <pre>查询关联的保单号</pre>
	 * @param policyNo
	 * @return
	 * @modified:
	 * ☆Luwei(2016年9月12日 下午4:02:55): <br>
	 */
	public int findRelatedPolicyNo(String policyNo);
	
	//根据立案号跟报案号查询报案信息
	public PrpLCMainVo getRegistNoAndRiskCodeInfo(String registNo,String riskCode);
	/**
	 * 查询指定的抄单表(1-交强，2-商业)
	 * @param registNo
	 * @param type
	 * @return
	 */
	public PrpLCMainVo getPolicyInfoByType(String registNo,String type);
	
	//根据保单号查询prpPhead和PrpPmain表批单数据
	public List<PolicyEndorseInfoVo> findPrpPheadAndPrpPmainInfo(String policyNo); 
	
	/**
	 * 根据车三项查询承保数据
	 * @param licenseNo
	 * @param engineNo
	 * @param vin
	 * @return
	 */
	public List<PrpLCItemCarVo> findPrpcItemcar(String licenseNo,String engineNo,String vin);
	/**
	 * 根据报案号查询承保数据
	 * @param licenseNo
	 * @param engineNo
	 * @param vin
	 * @return
	 */
	public List<PrpLCItemCarVo> findPrpcItemcarByRegistNo(String registNo);
	
	public String findCoinsCode(String policyNo);

	public List<Map<String,String>> getCaseInfo(String registNo);
}
