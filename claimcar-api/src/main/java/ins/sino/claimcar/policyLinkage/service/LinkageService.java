package ins.sino.claimcar.policyLinkage.service;

import ins.framework.common.ResultPage;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.policyLinkage.vo.CaseDetailResultVo;
import ins.sino.claimcar.policyLinkage.vo.CaseInfosDataListVo;
import ins.sino.claimcar.policyLinkage.vo.CaseInfosResultVo;
import ins.sino.claimcar.policyLinkage.vo.CurrentVersionVo;
import ins.sino.claimcar.policyLinkage.vo.PolicyLinkQueryVo;
import ins.sino.claimcar.policyLinkage.vo.PolicyLinkResultVo;
import ins.sino.claimcar.policyLinkage.vo.PrpLLinkCaseCarVo;
import ins.sino.claimcar.policyLinkage.vo.PrpLLinkCaseImgVo;
import ins.sino.claimcar.policyLinkage.vo.PrpLLinkCaseInfoVo;
import ins.sino.claimcar.policyLinkage.vo.PrpLLinkCaseInjuredVo;
import ins.sino.claimcar.policyLinkage.vo.PrpLLinkCaseMainVo;
import ins.sino.claimcar.policyLinkage.vo.PrpLLinkCaseVo;
import ins.sino.claimcar.policyLinkage.vo.PrpLLinkVersionVo;

import java.util.List;


public interface LinkageService {

	/* 
	 * @see ins.sino.claimcar.linkage.service.LinkageService#getCurrentVersion()
	 * @throws Exception
	 */
	public CurrentVersionVo searchCurrentVersion() throws Exception;

	/**
	 * 
	 * <pre></pre>
	 * @param startTime 案件开始查询时间(实际查询的是出险时间)，精确到时分秒（距1970年1月1日的毫秒数）。
	 * @param endTime   案件结束查询时间(实际查询的是出险时间) ，精确到时分秒（距1970年1月1日的毫秒数）。
	 * @param childAppid 这个参数非必填，主要是业务方决定调用哪个保险公司的案件
	 * @throws Exception
	 * @modified:
	 * *牛强(2016年11月21日 上午11:35:08): <br>
	 */

	/* 
	 * @see ins.sino.claimcar.linkage.service.LinkageService#savePolicyLinkAgeInfo(java.lang.String, java.lang.String, java.lang.String)
	 * @param businessType
	 * @param caseCode
	 * @param xml
	 */

	public void savePolicyLinkAgeInfos(ClaimInterfaceLogVo logVo);

	/* 
	 * @see ins.sino.claimcar.linkage.service.LinkageService#getCaseInfos(java.lang.Long, java.lang.Long, java.lang.String)
	 * @param startTime
	 * @param endTime
	 * @param childAppid
	 * @throws Exception
	 */
	public CaseInfosResultVo searchCaseInfos(Long startTime, Long endTime, String childAppid,int pageSize)
			throws Exception;

	/* 
	 * @see ins.sino.claimcar.linkage.service.LinkageService#getCaseDetail(java.lang.String, java.lang.String, int)
	 * @param caseNumber
	 * @param childappid
	 * @param imageType
	 * @return
	 * @throws Exception
	 */
	public CaseDetailResultVo searchCaseDetail(String caseNumber,
			String childappid, int imageType) throws Exception;
	
	public void autoSaveLinkInfo();
	
	public PrpLLinkVersionVo findVersionInfo(CurrentVersionVo versionVo) throws Exception;
	
	public PrpLLinkVersionVo saveVersionInfo(CurrentVersionVo versionVo) throws Exception;
	
	public void saveLinkcase(CaseInfosResultVo caseInfos,String versionNumber) throws Exception;
	public void saveCaseListInfo(CaseInfosResultVo caseInfos,String versionNumber) throws Exception;
	
	public void saveCaseList(List<CaseInfosDataListVo> caseList,String versionNumber) throws Exception;
	  
	public void saveCaseDatail(CaseDetailResultVo caseDetail,String versionNumber) throws Exception;
	
	public ResultPage<PolicyLinkResultVo> findCaseList(PolicyLinkQueryVo queryVo) throws Exception;
	
	public PrpLLinkCaseMainVo findMainVo(String caseId) throws Exception;
	public List<PrpLLinkCaseCarVo> findCarList(String caseId) throws Exception;
	public List<PrpLLinkCaseImgVo> findImgList(String caseId) throws Exception;
	public List<PrpLLinkCaseInjuredVo> findInjuredList(String caseId) throws Exception;
	
	public List<PrpLLinkCaseVo> findLinkCase(String versionNumber) throws Exception;
	public List<PrpLLinkCaseMainVo> findLinkCarMain(CaseDetailResultVo caseDetailVo) throws Exception;
	
	public List<PrpLLinkCaseInfoVo>  findLinkCaseInfo(String caseNumber) throws Exception;
	
	public void testQuartz();
	
	public void updateSmallPic();

}