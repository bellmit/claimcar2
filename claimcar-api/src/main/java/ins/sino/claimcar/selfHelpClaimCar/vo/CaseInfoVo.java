package ins.sino.claimcar.selfHelpClaimCar.vo;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CASEINFO")
public class CaseInfoVo {
  @XStreamAlias("INSCASENO")
  private String inscaseNo;//报案号
  @XStreamAlias("NOTICETYPE")
  private String noticeType;//通知环节--1.查勘完成,2.定损金额通, 3.赔付通知
  @XStreamAlias("CLAIMCARNO")
  private String claimcarNo;//标的车牌号
  @XStreamAlias("CPSTYPE")
  private String cpsType;//理赔状态1-结案2-拒赔3-零结4-注销 5-未结案
  @XStreamAlias("CASETYPE")
  private String caseType;//案件类型 1.自助案件2.常规案件
  @XStreamAlias("CASECHECK")
  private CaseCheckVo caseCheckVo;//车辆查勘信息
  @XStreamAlias("CASECARFEELIST")
  private List<CasecarFeeVo> casecarFeeList;//车辆定损金额
  @XStreamAlias("CASECLAIMFEELIST")
  private List<CaseclaimrFeeVo> caseclaimFeeList;//车辆理赔金额
public String getInscaseNo() {
	return inscaseNo;
}
public void setInscaseNo(String inscaseNo) {
	this.inscaseNo = inscaseNo;
}
public String getNoticeType() {
	return noticeType;
}
public void setNoticeType(String noticeType) {
	this.noticeType = noticeType;
}
public CaseCheckVo getCaseCheckVo() {
	return caseCheckVo;
}
public void setCaseCheckVo(CaseCheckVo caseCheckVo) {
	this.caseCheckVo = caseCheckVo;
}
public List<CasecarFeeVo> getCasecarFeeList() {
	return casecarFeeList;
}
public void setCasecarFeeList(List<CasecarFeeVo> casecarFeeList) {
	this.casecarFeeList = casecarFeeList;
}
public List<CaseclaimrFeeVo> getCaseclaimFeeList() {
	return caseclaimFeeList;
}
public void setCaseclaimFeeList(List<CaseclaimrFeeVo> caseclaimFeeList) {
	this.caseclaimFeeList = caseclaimFeeList;
}
public String getClaimcarNo() {
	return claimcarNo;
}
public void setClaimcarNo(String claimcarNo) {
	this.claimcarNo = claimcarNo;
}
public String getCpsType() {
	return cpsType;
}
public void setCpsType(String cpsType) {
	this.cpsType = cpsType;
}
public String getCaseType() {
	return caseType;
}
public void setCaseType(String caseType) {
	this.caseType = caseType;
}
  
  
  
 

}
