package ins.sino.claimcar.moblie.commonmark.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("REMARKOPTIONINFO")
public class MCCommonRemarksRemarksOptionInfo implements Serializable {

	/**  */
	private static final long serialVersionUID = -8931249200262653287L;
	
	@XStreamAlias("REMARKID")
	private String remarkId; //备注ID
	
	@XStreamAlias("ROLE")
	private String role; //角色
	
	@XStreamAlias("OPERATORCODE")
	private String operateCode; //操作人员
	
	@XStreamAlias("COMCODE")
	private String comCode; //机构代码
	
	@XStreamAlias("INPUTDATE")
	private String inputDate; //发表意见时间
	
	@XStreamAlias("OPINIONDESC")
	private String optionDesc; //意见说明
	
	@XStreamAlias("AUDITSTATE")
	private String auditState; //审核状态

	public String getRemarkId() {
		return remarkId;
	}

	public void setRemarkId(String remarkId) {
		this.remarkId = remarkId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getOperateCode() {
		return operateCode;
	}

	public void setOperateCode(String operateCode) {
		this.operateCode = operateCode;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getInputDate() {
		return inputDate;
	}

	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}

	public String getOptionDesc() {
		return optionDesc;
	}

	public void setOptionDesc(String optionDesc) {
		this.optionDesc = optionDesc;
	}

	public String getAuditState() {
		return auditState;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}
	
	

}
