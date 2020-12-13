package ins.sino.claimcar.platform.vo;

import java.util.Date;

public class PlatformQueryVo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// 分页
	private Integer start;// 记录起始位置
	
	private Integer length;// 记录数

	// 理赔编码
	private String claimSeqNo;
	// 报案号
	private String bussNo;
	// 报案日期
	private Date reportTime;
	// 赔案校验码
	private String validNo;
	// 机构
	private String comCode;
	// 案件状态
	private String status;
	// 错误代码
	private String errorCode;
	// 错误信息
	private String errorMessage;

	// 上传节点
	private String uploadNode;
	private String registNo;

	public Integer getStart() {
		if (start == null)
			start = 0;
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLength() {
		if (length == null)
			length = 10;
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getClaimSeqNo() {
		return claimSeqNo;
	}

	public void setClaimSeqNo(String claimSeqNo) {
		this.claimSeqNo = claimSeqNo;
	}

	public String getBussNo() {
		return bussNo;
	}

	public void setBussNo(String bussNo) {
		this.bussNo = bussNo;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public String getValidNo() {
		return validNo;
	}

	public void setValidNo(String validNo) {
		this.validNo = validNo;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getUploadNode() {
		return uploadNode;
	}

	public void setUploadNode(String uploadNode) {
		this.uploadNode = uploadNode;
	}

	
	public String getRegistNo() {
		return registNo;
	}

	
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	

}
