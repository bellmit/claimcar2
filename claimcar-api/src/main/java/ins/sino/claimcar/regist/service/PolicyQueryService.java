package ins.sino.claimcar.regist.service;

import ins.framework.dao.database.support.Page;
import ins.sino.claimcar.regist.vo.PolicyInfoVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.formula.ptg.StringPtg;

public interface PolicyQueryService {

	/**
	 * <pre></pre>
	 * @param prpCMainVo
	 * @param request
	 * @return
	 * @modified:
	 * ☆ZhangYu(2015年12月1日 上午10:35:14): <br>
	 */
	public Page<PolicyInfoVo> findPrpCMainForPage(PolicyInfoVo policyInfoVo,
			int start, int pageSize) throws Exception;

	/**
	 * 查找一个保单号的关联保单号
	 * @param policyNo
	 * @return
	 * @modified: ☆LiuPing(2016年1月7日 下午3:08:29): <br>
	 */
	public String getRelatedPolicyNo(String policyNo);

	public List<PrpLCMainVo> findPrpcMainByPolicyNos(List<String> policyNoList);

	/**
	 * 查询一个保单的关联保单号，不包括当前保单号
	 * @param policyInfoVo 包含了保单号，出险日期
	 * @return
	 * @modified: ☆LiuPing(2016年1月7日 下午3:05:26): <br>
	 */
	public Page<PolicyInfoVo> findRelatedPolicy(PolicyInfoVo policyInfoVo);

	public int findByPolicyNo(String policyNo);

	public List<PolicyInfoVo> findPrpCMainForPages(PolicyInfoVo policyInfo,
			int start, int pageSize) throws Exception ;
	public Page<PolicyInfoVo> findPolicyNoForPage(PolicyInfoVo policyInfoVo,int start,int pageSize);
	
	/**
	 * 根据保单号数组查询保单是否实付
	 * <pre></pre>
	 * @param plyNoArr
	 * @return
	 * @modified:
	 * ☆LiYi(2018年10月26日 下午2:53:41): <br>
	 */
	public Set<Map<String,String>> findPrpjpayrefrecBySQL(String[] plyNoArr);
	
	/**
	 * 通过保单号数组查询保单是否联共保
	 * <pre></pre>
	 * @param plyNoArr
	 * @return
	 * @modified:
	 * ☆LiYi(2018年10月26日 下午2:54:07): <br>
	 */
	public Set<Map<String,String>> findPrpCMainBySQL(String[] plyNoArr);
	
	/**
	 * 根据保单号数组查询 联共保保单所占份额信息
	 * <pre></pre>
	 * @param plyNoArr
	 * @return
	 * @modified:
	 * ☆LiYi(2018年10月26日 下午2:55:17): <br>
	 */
	public List<Map<String,String>> findPrpCCoins(String[] plyNoArr);
	
	/**
	 * 根据保单号和出险时间判断保单是否有效
	 * @param policyNo
	 * @param damageTime
	 * @return
	 */
	public boolean existPolicy(String policyNo,Date damageTime);
	
	/**
	 * 通过车牌号，保险人，身份证号，查询保单表
	 * @param insuredName
	 * @param licenseNo
	 * @param identifyNumber
	 * @return
	 */
	public List<PolicyInfoVo> findPolicyNoList(String insuredName, String licenseNo,String identifyNumber,String identifyType,String engineNo,String frameNo);
	/**
	 * 通过保单查询保单表
	 * @param policyNo
	 * @return
	 */
	public String findPrpCMian(String policyNo);

	/**
	 * <pre></pre>
	 * @param policyInfoVo
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年1月28日 下午7:07:50): <br>
	 */
	public boolean calculateResultCount(PolicyInfoVo policyInfoVo,String propertyName); 

}