package ins.sino.claimcar.lossprop.vo;

import java.io.Serializable;

/**
 * 财产定损查询条件vo
 * @author ★LiuPing
 * @CreateTime 2016年2月3日
 */
public class PropQueryVo implements Serializable{
	private static final long serialVersionUID = 1L; 
	private String registNo;//报案号
	private String policyNo;//保单号
	private String licenseNo;// 车牌号
	private String frameNo;//车架号
	private String insuredName;//被保险人
	private String mercyFlag;//案件紧急程度
	private String deflossFlag;
	private String comCode;//机构

	// 分页
	private Integer start;// 记录起始位置
	private Integer length;// 记录数

	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getFrameNo() {
		return frameNo;
	}
	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	public String getMercyFlag() {
		return mercyFlag;
	}
	public void setMercyFlag(String mercyFlag) {
		this.mercyFlag = mercyFlag;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getDeflossFlag() {
		return deflossFlag;
	}
	public void setDeflossFlag(String deflossFlag) {
		this.deflossFlag = deflossFlag;
	}
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	


}
