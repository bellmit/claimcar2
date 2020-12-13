package ins.sino.claimcar.claim.service;

import ins.framework.common.ResultPage;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claim.vo.*;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;

import java.util.List;

public interface ClaimService {

	public abstract PrpLClaimVo findByClaimNo(String claimNo);

	/**
	 * 根据条件查找
	 * <pre></pre>
	 * @param registNo
	 * @param policyNo
	 * @param validFlag
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年5月13日 下午4:38:53): <br>
	 */
	public abstract List<PrpLClaimVo> findprpLClaimVoListByRegistAndPolicyNo(String registNo,String policyNo,String validFlag);

	/**
	 * 根据报案号查找立案列表
	 * 
	 * @param registNo
	 * @return
	 */
	public abstract List<PrpLClaimVo> findClaimListByRegistNo(String registNo);

	/**
	 * 查勘提交保存立案信息
	 * 
	 * @param prpLcheckVo
	 * @throws Exception
	 */
	public abstract String saveClaim(PrpLClaimVo claimMainVo);

	/**
	 * 更新立案
	 * <pre></pre>
	 * @param claimMainVo
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年5月6日 下午12:13:02): <br>
	 */
	public abstract String updateClaim(PrpLClaimVo claimMainVo);

	/**
	 * 根据报案号查找立案险别表 weilanlei
	 * 
	 * @param registNo
	 * @return
	 */
	public abstract List<PrpLClaimKindVo> findClaimKindVoListByRegistNo(String registNo);

	/**
	 * 根据报案号和保单号取立案数据 
	 * <pre></pre>
	 * @param registNo 报案号
	 * @param policyNo 保单号
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年4月7日 下午2:48:39): <br>
	 */
	public abstract PrpLClaimVo findClaimVoByRegistNoAndPolicyNo(String registNo,String policyNo);

	/**
	 * 历史出险信息 根据报案号获取立案案件信息表
	 * @param registNo
	 * @return
	 * @modified: ☆WLL(2016年6月24日 ): <br>
	 */
	public abstract List<PrpLClaimSummaryVo> findPrpLClaimSummaryByRegistNo(String registNo);

	/**
	 * 历史出险信息 根据报案号获取立案案件信息
	 * @param registNo
	 * @return
	 * @modified: ☆WLL(2016年6月24日 ): <br>
	 */
	public abstract PrpLClaimSummaryVo findPrpLClaimSummaryVoByRegistNo(String claimNo);

	/**
	 * 立案注销
	 * <pre></pre>
	 * @modified:
	 * validFlag  0-立案注销   2-立案拒赔
	 * ☆ZhouYanBin(2016年5月10日 下午4:00:05): <br>
	 */
	public abstract void cancleClaimByClaimNo(String claimNo,String validFlag,SysUserVo userVo);

	/**
	 * 
	 * <pre></pre>
	 * @param claimTextVo
	 * @param claimKindVoList
	 * @param claimKindFeeVoList
	 * @modified:
	 * ☆WLL(2016年7月20日 上午10:07:14): <br>
	 */
	public abstract void saveClaimModifity(PrpLClaimTextVo claimTextVo,List<PrpLClaimKindVo> claimKindVoList,
											List<PrpLClaimKindFeeVo> claimKindFeeVoList);

	public abstract List<PrpLClaimVo> findClaimListByRegistNo(String registNo,String validFlag);

	/**
	 * 判断案件是否结案且未重开赔案
	 * 
	 * <pre></pre>
	 * @param claimNo
	 * @return
	 * @modified: ☆WLL(2016年7月25日 下午2:51:03): <br>
	 */
	public abstract String adjustCaseHadEnd(String claimNo);
	public ResultPage<PrpLClaimVo> findCancelAppTaskQuery(PrpLWfTaskQueryVo taskQueryVo) throws Exception;
	public ResultPage<PrpLClaimVo> findRecanAppTaskQuery(PrpLWfTaskQueryVo taskQueryVo) throws Exception;
	
	
	public abstract List<PrpLClaimVo> findSumClaimByPolicyNo(String policyNo)throws Exception;
	
	public List<PrpLClaimVo> findPrpLClaimVosByPolicyNo(String policyNo);
	/**
	 * 获得人伤刷新的最新一次立案轨迹
	 * <pre></pre>
	 * @param claimNo
	 * @return
	 * @modified:
	 * ☆WLL(2017年4月7日 下午3:53:13): <br>
	 */
	List<PrpLClaimKindHisVo> findPLossKindHisNewest(String registNo);
	
	/**
	 * 根据报案号查找是否结案
	 * @param registNo
	 * @return
	 */
	public abstract Boolean findNotCaseByRegistNo(String registNo);
	
	/**
	 * 根据报案号和classCode查询
	 * @param registNo
	 * @param classCode
	 * @return
	 */
	public PrpLClaimVo findClaimVoByClassCode(String registNo,String classCode);
	/**
	 * 编码翻译
	 * @param codeType
	 * @param codeCode
	 * @return
	 */
	public PrplcodeDictVo findPrplcodeDictByCodeAndcodeType(String codeType,String codeCode);

	/**
	 * 限额表
	 * @param ciindemDuty 有责无责，1-有责 0-无责	CodeConstants.DutyType
	 * @return
	 * @modified:
	 */
	public List<PrpLDLimitVo> findPrpLDLimitList(String ciindemDuty, String registNo);
}
