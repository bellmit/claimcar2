package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ReqInfo")
public class DlossAskReqInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("IfNewLossFlag")
	private String ifNewLossFlag;//是否包含保单、报案信息[传否-定损请求不再送报案保单信息部分-待精友确认] 0：否 1：是
	@XStreamAlias("AuditLossFlag")
	private String auditLossFlag = "0";//核损标记 0：正常定损 1：核损退回进定损 核价传0
	@XStreamAlias("ReturnURL")
	private String returnURL;//返回URL
	@XStreamAlias("RefreshURL")
	private String refreshURL;//返回刷新URL
	
	public String getIfNewLossFlag() {
		return ifNewLossFlag;
	}
	public void setIfNewLossFlag(String ifNewLossFlag) {
		this.ifNewLossFlag = ifNewLossFlag;
	}
	public String getAuditLossFlag() {
		return auditLossFlag;
	}
	public void setAuditLossFlag(String auditLossFlag) {
		this.auditLossFlag = auditLossFlag;
	}
	public String getReturnURL() {
		return returnURL;
	}
	public void setReturnURL(String returnURL) {
		this.returnURL = returnURL;
	}
	public String getRefreshURL() {
		return refreshURL;
	}
	public void setRefreshURL(String refreshURL) {
		this.refreshURL = refreshURL;
	}
}
