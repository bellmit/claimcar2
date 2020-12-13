package ins.sino.claimcar.verifyclaim.service;

import ins.framework.common.ResultPage;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformTaskVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrplPayeeKindPaymentVo;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.endcase.vo.PrpLuwNotionMainVo;
import ins.sino.claimcar.endcase.vo.PrpLuwNotionVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.verifyclaim.vo.VerifyClaimPassVo;

import java.util.List;
import java.util.Map;

public interface VerifyClaimService {

	/** 获取保单信息
	 * @param registNo
	 * @param flag
	 * ☆Luwei(2016年3月14日 下午5:22:39): <br>
	 */
	public List<PrpLCMainVo> initPolicyInfo(String registNo,String policyType);

	public PrpLuwNotionMainVo queryUwNotion(String registNo,String compensateNo);

	/**
	 * 查询一条核赔意见信息表
	 * @param registNo
	 * @param compeNo
	 * @param taskType
	 * @return
	 */
	public PrpLuwNotionMainVo findUwNotion(String registNo,String compeNo,String taskType);

	/**
	 * @param wfTaskVo
	 * @return
	 */
	public PrpLuwNotionMainVo findUwNotionMainByRegistNo(PrpLWfTaskVo wfTaskVo);

	/**
	 * @param id
	 * @return
	 * @modified:
	 * ☆Luwei(2016年10月21日 上午11:44:35): <br>
	 */
	public PrpLuwNotionMainVo finduwNotionByPK(Long id);

	/** 
	 * 保存核赔审批信息
	 * @param uwNotionVo
	 * @modified:
	 * sign=0(自动核陪)，sign=1(人工核陪)
	 * ☆Luwei(2016年3月17日 下午5:22:45): <br>
	 */
	public Long saveVerifyClaim(PrpLuwNotionMainVo uwNotionMainVo,PrpLuwNotionVo uwNotionVo,PrpLWfTaskVo wfTaskVo,String comCode,String userCode,String sign);

	/**
	 * 核赔提交工作流（action : 退回，提交上级，核赔通过）
	 * @param uwId,action
	 * @modified: ☆Luwei(2016年4月14日 上午10:37:16): <br>
	 */
	public List<PrpLWfTaskVo> verifyClaimSubmit(PrpLWfTaskVo wfTaskVo,Long uwNotionMainId,String currentNode,String nextNode,String action,SysUserVo userVo,String compeWfZeroFlag) throws Exception;

	// ------------- 垫付 --------------------//
	public PrpLCMainVo getCMainInfo(String registNo);

	public PrpLCheckCarVo getCheckCarInfo(String registNo);

	public PrpLPadPayMainVo getPadPay(String registNo,String compeNo);

	public PrpLPadPayMainVo getPadPayByCompeNo(String registNo,String compeNo);
	/**
	 * 获取定损标的车信息
	 */
	public PrpLDlossCarInfoVo getDlossCarInfo(String registNo);

	/** 车辆损失信息 **/
	public List<PrpLDlossCarMainVo> getLossCarInfo(String registNo,String bcFlag);

	/**
	 * 是否无责代赔
	 */
	public String getCheckDutyFlag(String registNo);

	/**
	 * 获取不计免赔附加险
	 */
	public List<PrpLCItemKindVo> getOtherKind(String registNo,String flag);

	//5 -- 回写立案
	public void wirteBackClaim(String claimNo,String taskInNode,String userCode);

	public String setActionToStr(String action);

	/**
	 * <pre>理算核赔通过送平台</pre>
	 * @param compeVo
	 * @modified:
	 * ☆Luwei(2016年8月29日 上午11:14:23): <br>
	 */
	public void verifyToPlatform(PrpLCompensateVo compeVo,String taskInNode);

	/**
	 * <pre>核赔通过送收付、再保</pre>
	 * @modified:
	 * ☆Luwei(2017年2月14日 下午6:14:23): <br>
	 */
	public void sendCompensateToPayment(Long uwNotionMainId) throws Exception;

	public Long autoVerifyClaimEndCase(SysUserVo userVo,PrpLCompensateVo prpLCompensateVo);

	public List<PrpLWfTaskVo> autoVerifyClaimEndCaseForPingAn(SysUserVo userVo,PrpLCompensateVo prpLCompensateVo) throws Exception;

    /**
     * <pre>
     * 核赔通过清单查询
     * </pre>
     * @param verifyClaimPassVo
     * @param start
     * @param length
     * @return
     * @modified: ☆LinYi(2017年6月8日 下午4:26:24): <br>
     */
    public ResultPage<VerifyClaimPassVo> findVerifyClaimPassList(VerifyClaimPassVo verifyClaimPassVo,int start,int length);
    
    /**
     * 获取导出数据
     * <pre></pre>
     * @param verifyClaimPassVo
     * @return
     * @modified:
     * ☆LinYi(2017年6月15日 下午6:09:56): <br>
     */
    public List<VerifyClaimPassVo> getDatas(VerifyClaimPassVo verifyClaimPassVo);

    /**
     * 导出excel填充数据
     * <pre></pre>
     * @param results
     * @return
     * @modified:
     * ☆LinYi(2017年6月16日 下午2:57:59): <br>
     */
    public List<Map<String,Object>> createExcelRecord(List<VerifyClaimPassVo> results);
    /**
     * 保存核赔意见表
     * @param mainVo
     */
    public void updatePrpLuwNotionMainVo(PrpLuwNotionMainVo mainVo);
	
    /**
     * 核赔提交结案
     */
    public List<PrpLWfTaskVo> verifyToEndcasSubmit(PrpLWfTaskVo wfTaskVo,Long uwNotionMainId,String currentNode,String nextNode,String action,SysUserVo userVo,String compeWfZeroFlag);

    /**
     * 核赔提交结案
     * @param userVo
     * @param prpLCompensateVo
     * @return
     */
    public void autoVerifyClaimToFlowEndCase(SysUserVo userVo,PrpLCompensateVo prpLCompensateVo,Long uwNotionMainId);
    

	/**
	 * 核赔发送上海平台调用方法
	 */
    public void sendVClaimToSH(String compensateNo,String comCode,CiClaimPlatformTaskVo platformTaskVo);
    
    /**
	 * 全国平台--交强理算核赔
	 * @param compeNo-计算书号
	 * @throws Exception
	 */
    public void sendVClaimCIToPlatform(String compeNo,CiClaimPlatformTaskVo platformTaskVo);
    
    /**
	 * 全国平台--商业理算核赔
	 * @param compeNo-计算书号
	 * @throws Exception
	 */
    public void sendVClaimBIToPlatform(String compeNo,CiClaimPlatformTaskVo platformTaskVo);
    
    public void sendEndCaseToSHBySubmit(String endCaseNo,CiClaimPlatformTaskVo platformTaskVo);
    
    public void sendEndCaseToPlatform(String endCaseNo,CiClaimPlatformTaskVo platformTaskVo);
    
    /**
     * vat回写价税信息推送再保
     * @param registNo
     * @param riskCode
     */
    public void sendVatBackSumAmountNTToReins(String registNo,String riskCode);
    
    /**
     * vat回写价税信息推送再保
     * @param keyString
     * @param casePayeeKindPayments
     */
    public void sendVatBackSumAmountNTToReins(String keyString,List<PrplPayeeKindPaymentVo> casePayeeKindPayments);
    
    /**
     * 获取指定次数的收款人险别拆分信息
     * @param registNo
     * @param riskCode
     * @param times
     * @return
     */
    public List<PrplPayeeKindPaymentVo> findPayeeKindPaymentsByTimes(String registNo,String riskCode,int times);
    
}
