/**
 * 
 */
package ins.sino.claimcar.regist.service;

import ins.framework.common.ResultPage;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.regist.vo.PrpCcarDriverVo;
import ins.sino.claimcar.regist.vo.PrpDClaimAvgVo;
import ins.sino.claimcar.regist.vo.PrpDagentVo;
import ins.sino.claimcar.regist.vo.PrpLCInsuredVo;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistPropLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.regist.vo.PrppMainVo;
import ins.sino.claimcar.regist.vo.PrppheadSubVo;
import ins.sino.claimcar.regist.vo.PrppheadVo;
import ins.sino.claimcar.regist.vo.PrpptextVo;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author zhangyu
 *
 */
public interface RegistQueryService {

   
	public PrpLRegistVo findByRegistNo(String registNo);
	public List<PrpLRegistVo> findPrpLRegistByPolicyNo(String policyNo);
	public void updatePrpLRegistExt(PrpLRegistExtVo registExt);
	
	public PrpLRegistCarLossVo findRegistCarLossById(Long id);
	
	public PrpLRegistPropLossVo findRegistPropLossById(Long id);
	
    public List<PrppheadVo> findByPolicyNo(String policyNo);
    
	public List<PrpLClaimDeductVo> findClaimDeductVoByRegistNo(String registNo);
	
	public List<PrpLClaimDeductVo> findIsCheckClaimDeducts(String registNo);
	
	public void updateClaimDeduct(List<PrpLClaimDeductVo> claimDeductVos); 
	
	public PrpLCItemCarVo findCItemCarByRegistNo(String registNo);

	/**
	 * <pre>根据报案号查询历史出险次数</pre>
	 * @param registNo
	 * @param classType-险种类型，1-交强，2-商业
	 * @return int-次数
	 * @modified:
	 * ☆Luwei(2016年8月3日 上午11:09:06): <br>
	 */
	public int findRiskInfoByRegistNo(String registNo,String classType);

	/**
	 * 根据条件查找案均值
	 * 
	 * <pre></pre>
	 * @param riskCode 险种代码
	 * @param kindCode 先别代码
	 * @param avgType 案均类型
	 * @return
	 * @modified: ☆ZhouYanBin(2016年3月21日 上午10:54:48): <br>
	 */
	public List<PrpDClaimAvgVo> findForceClaimAverageValue(String riskCode,String kindCode,String avgType,Integer year,String comcode);



	public List<PrpLCItemKindVo> findCItemKindListByRegistNo(String registNo);
	
	public ResultPage<PrpLRegistVo> findRegistPageByRegistNo(String registNo,String policyNo,String insuredName,String licenseNo,int start,int length );
	public ResultPage<PrpLCMainVo> findPrpLCMainPageByRegistNo(String registNo,String policyNo,Date taskInTimeStart,Date taskInTimeEnd,int start,int length );

	
	public PrpLRegistExtVo getPrpLRegistExtInfo(String registNo);

	
	/**
	 * 根据险种代码组织可选免赔条件
	 * <pre></pre>
	 * @param registNo 报案号
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年4月18日 上午10:31:28): <br>
	 */
	public Map<String,Double> getDeductRate(String registNo);
	
	/**
	 * 根据报案时间查询数据
	 * <pre></pre>
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年5月13日 上午10:42:56): <br>
	 */
	public List<PrpLRegistVo> findPrpLRegistVoListByReportTime(Date beginTime,Date endTime);

	public List<PrppheadSubVo> findPrppheadSubByPolicyNo(String policyNo);
	List<PrpLCInsuredVo> findPrpCinsuredNatureByPolicyNo(String policyNo);
	//根据报案号和关系人标志查询被保险人信息
	public List<PrpLCInsuredVo> findPrpLCInsuredVoByPolicyNoAndFlag(String policyNo, String insuredFlag);

	/*
	 * 查询批改内容endorseNo
	 */
	public List<PrpptextVo> findPrppTextByPolicyNo(String  endorseNo);
	public List<PrppMainVo> findprppMainByPolicyNo(String endorseNo);
	
	public List<PrpLRegistVo> findOldRegistByPolicyNo(String policyNo);
	public List<PrpLPayCustomVo> findOldPrpjlinkaccountByCertiNo(String certiNo);
	public List<PrpLPayCustomVo> findOldAccountByAccountNo(String AccountNo);
	public List<PrpLRegistVo> findOldPrpLRegistRPolicy(String registNo);
	public List<PrpLRegistCarLossVo> findOldPrpLthirdCarLoss(String registNo,String serialNo);
	public List<PrpLClaimVo> findOldPrpLClaimVo(String registNo);

	/**
	 * 根据保单号，批单类型，排序字段查询
	 * @param policyNo
	 * @param endorType
	 * @param sort
	 * @return
	 * @modified:
	 * ☆zhujunde(2017年6月19日 下午2:56:39): <br>
	 */
	public List<PrppheadVo> findByOther(String policyNo,List<String> endorType,String sort);
	
	/**
	 * 
	 * 根据保单，批单列表查询PrppheadSubVo
	 * @param policyNo
	 * @param endorseNo
	 * @return
	 * @modified:
	 * ☆zhujunde(2017年6月20日 上午9:29:17): <br>
	 */
	public List<PrppheadSubVo> findPrppheadSubVoByOther(String policyNo,List<String> endorseNo);
	
	/**
	 * 根据报案时间查询
	 * @param beginTime
	 * @return
	 */
	public List<PrpLRegistVo> findRegistByQueryReportTime(Date beginTime ,Date endTime,String subComcode);

	public List<PrpLCItemKindVo> findItemKindVo(String registNo,String kindCode);
	
	/**
	 * 根据时间分页查询
	 * <pre></pre>
	 * @param beginTime
	 * @param endTime
	 * @param start
	 * @param length
	 * @return
	 * @modified:
	 * ☆zhujunde(2018年3月27日 下午3:46:10): <br>
	 */
    public List<PrpLRegistVo> findPrpLRegistPageVoListByReportTime(Date beginTime,Date endTime,int start,int length);
    /**
     * PrpCcarDriver-车辆驾驶员关系表的列清单
     * <pre></pre>
     * @param policyNo
     * @return
     * @modified:
     * ☆zhujunde(2018年5月9日 下午5:01:19): <br>
     */
    public List<PrpCcarDriverVo> findPrpCcarDriver(String policyNo);
    
    /**
     * PRPDAGENT-代理人代码表
     * <pre></pre>
     * @param agentCode
     * @return
     * @modified:
     * ☆zhujunde(2018年5月9日 下午5:47:34): <br>
     */
    public List<PrpDagentVo> findPrpdagent(String agentCode);
	/**
	 * 根据policyNo查承保PrpCMain
	 * <pre></pre>
	 * @param policyNo
	 * @return
	 * @modified:
	 * ☆zhujunde(2017年12月11日 上午9:30:51): <br>
	 */
	public PrpLCMainVo findPrpCmainByPolicyNo(String policyNo);
	
	/**
	 * 查询PrpLCItemCarVo
	 * @param registNo
	 * @param riskCode
	 * @return
	 */
	public List<PrpLCItemCarVo> findCItemCarByOther(String registNo,String riskCode);
    
    /**
     * 根据保单号查询报案信息根据出险时间排序
     * @param policyNo
     * @return
     */
	public List<PrpLRegistVo> findPrpLRegist(String policyNo);
    /**
     * 根据保单号查询报案信息根据出险时间排序
     * @param policyNo
     * @return
     */
	public List<PrpLRegistVo> findPrpLRegistByPolicyNos(List<String> policyNos);
	
	/**
	 * 根据多个报案号查出多条数据
	 * <pre></pre>
	 * @param registNos
	 * @return
	 * @modified:
	 * ☆LiYi(2018年10月24日 上午10:47:02): <br>
	 */
	public List<PrpLRegistVo> findPrpLRegistVosByRegistNos(String[] registNos);
	
	/**
	 * 根据保单号查询出险时间48小时内已进行报案，案件状态为有效（未注销，未结案）的案件
	 * @param policies
	 * @param reportTime
	 * @return
	 */
	public List<PrpLRegistVo> findValidCase(List<String> policies,Date reportTime);
	/**
	 * 查询报案号后，48小时后还立案的案件
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<PrpLRegistVo> findPrpLRegistVosByTime(Date beginTime,Date endTime);
}
