package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 手动调度请求接口-数据部分（理赔请求快赔）
 * @author zy
 *
 */
@XStreamAlias("SCHEDULEWF")
public class HandleScheduleReqScheduleSD implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 报案号 */
	@XStreamAlias("REGISTNO")
	private String registNo;
	
	/** 保单号 */
	@XStreamAlias("POLICYNO")
	private String policyNo;
	
	/** 类型 */
	@XStreamAlias("TYPE")
	private String type;
	
	
	/** 业务类型 */
	@XStreamAlias("BUSINESSTYPE")
	private String businessType;
	
	/** 客户分类 */
	@XStreamAlias("CUSTOMTYPE")
	private String customType;
	
	/** 案件类型 */
	@XStreamAlias("CASETYPE")
	private String caseType;
	
	/** 是否异地案件 */
	@XStreamAlias("ISELSEWHERE")
	private String isElseWhere;
	
	/** 代理人编码 */
	@XStreamAlias("AGENTCODE")
	private String agentCode;
	
	/** 保单归属地编码 */
	@XStreamAlias("COMCODE")
	private String comCode;

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

	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getCustomType() {
		return customType;
	}

	public void setCustomType(String customType) {
		this.customType = customType;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public String getIsElseWhere() {
		return isElseWhere;
	}

	public void setIsElseWhere(String isElseWhere) {
		this.isElseWhere = isElseWhere;
	}

	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	
	
}
