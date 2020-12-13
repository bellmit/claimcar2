package ins.sino.claimcar.interf.vo;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CECheckResult")
public class CECheckResultVo {
	/*报案号*/
	@XStreamAlias("ClaimNumber")
	private String claimNumber;
	/*RequestID*/
	@XStreamAlias("RequestID")
	private String requestID;
	/*提取码*/
	@XStreamAlias("ExtractionCode")
	private String extractionCode;
	/*车辆检测的数量*/
	@XStreamAlias("ClaimCount")
	private String claimCount;
	/*错误码*/
	@XStreamAlias("ErrCode")
	private String errCode;
	/*错误提示信息*/
	@XStreamAlias("ErrMsg")
	private String errMsg;
	/*YA：关联ID*/
	@XStreamAlias("ICClaimRelationID")
	private String iCClaimRelationID;
	/*案件中车辆检测结果列表*/
	@XStreamAlias("Claims")
	private List<ClaimVo> claims;
	public String getClaimNumber() {
		return claimNumber;
	}
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	public String getRequestID() {
		return requestID;
	}
	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}
	public String getExtractionCode() {
		return extractionCode;
	}
	public void setExtractionCode(String extractionCode) {
		this.extractionCode = extractionCode;
	}
	public String getClaimCount() {
		return claimCount;
	}
	public void setClaimCount(String claimCount) {
		this.claimCount = claimCount;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public String getiCClaimRelationID() {
		return iCClaimRelationID;
	}
	public void setiCClaimRelationID(String iCClaimRelationID) {
		this.iCClaimRelationID = iCClaimRelationID;
	}
	public List<ClaimVo> getClaims() {
		return claims;
	}
	public void setClaims(List<ClaimVo> claims) {
		this.claims = claims;
	}
	

}
