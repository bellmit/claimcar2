package ins.sino.claimcar.losscar.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <pre>第三方定损系统数据传输 </pre>
 * <pre></pre>
 * @author ★yangkun
 */
public class ClaimFittingVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** 定损车辆id **/
	private String lossCarId;
	/** 报案号  **/
	private String registNo;
	/** 保单号 **/
	private String policyNo;
	/** 被保险人 **/
	private String insurant;
	/** 损失车辆 **/
	private String lossItemCode;
	/** 车牌号码 **/
	private String licenseNo;
	/** 系统价地区 **/ 
	private String systemAreaCode;
	/** 本地价地区   填写当前用户所在的分公司代码 **/
	private String localAreaCode;
	/** 车型编码 **/
	private String vehCode;
	/** 车型名称 **/
	private String vehName;
	/** 2007年6月协议后增加 **/
	private String callType;
	/** 操作员名称 **/
	private String operatorCode;
	/**返回URL**/
	private String returnURL;
	/**刷新的URL**/
	private String refreshURL;
	private String fittingsURL;
	
	/**核价标记   0：正常定损  1：核价退回进定损  **/
	private String auditFlag="0";
	/**核损标记  0：正常定损  1：核损退回进定损 **/
	private String auditLossFlag="0";
	/**复检标记  0：正常定损  1：复检  **/
	private String recheck="0";
	/**价格方案标记  001：特约维修站  002：一类修理厂  003：二类修理厂  (必须是三者一种)  **/
	private String jgfaFlag;
	/**配件折扣 **/
	private BigDecimal fitDiscount = new BigDecimal("1");
	/**工时折扣 **/
	private BigDecimal repairDiscount = new BigDecimal("1");
	/**修理厂代码**/
	private String repairCode;
	/**请求报文 **/
	private String requestXml;
	/**返回报文 **/
	private String responseXml;
	
	private PrpLDlossCarMainVo lossCarMainVo;
	private String operateType="";
	private String auditPriceRate="1.1";
	private String auditLossRate="1.1";
	/** 复检权限 **/
	private String reView;
	private String localAreaName="";
	private Date creatDate;
	
	private String nodeCode;
	
	private SendVo sendVo;
	
	private String jyUrl;
	
	public String getLossCarId() {
		return lossCarId;
	}
	
	public void setLossCarId(String lossCarId) {
		this.lossCarId = lossCarId;
	}

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
	
	public String getInsurant() {
		return insurant;
	}
	
	public void setInsurant(String insurant) {
		this.insurant = insurant;
	}
	
	public String getLossItemCode() {
		return lossItemCode;
	}
	
	public void setLossItemCode(String lossItemCode) {
		this.lossItemCode = lossItemCode;
	}
	
	public String getLicenseNo() {
		return licenseNo;
	}
	
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	
	public String getSystemAreaCode() {
		return systemAreaCode;
	}
	
	public void setSystemAreaCode(String systemAreaCode) {
		this.systemAreaCode = systemAreaCode;
	}
	
	public String getLocalAreaCode() {
		return localAreaCode;
	}
	
	public void setLocalAreaCode(String localAreaCode) {
		this.localAreaCode = localAreaCode;
	}
	
	public String getVehCode() {
		return vehCode;
	}
	
	public void setVehCode(String vehCode) {
		this.vehCode = vehCode;
	}
	
	public String getVehName() {
		return vehName;
	}
	
	public void setVehName(String vehName) {
		this.vehName = vehName;
	}
	
	public String getCallType() {
		return callType;
	}
	
	public void setCallType(String callType) {
		this.callType = callType;
	}
	
	public String getOperatorCode() {
		return operatorCode;
	}
	
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
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
	
	public String getFittingsURL() {
		return fittingsURL;
	}
	
	public void setFittingsURL(String fittingsURL) {
		this.fittingsURL = fittingsURL;
	}
	
	public String getAuditFlag() {
		return auditFlag;
	}
	
	public void setAuditFlag(String auditFlag) {
		this.auditFlag = auditFlag;
	}
	
	public String getAuditLossFlag() {
		return auditLossFlag;
	}
	
	public void setAuditLossFlag(String auditLossFlag) {
		this.auditLossFlag = auditLossFlag;
	}
	
	public PrpLDlossCarMainVo getLossCarMainVo() {
		return lossCarMainVo;
	}
	
	public void setLossCarMainVo(PrpLDlossCarMainVo lossCarMainVo) {
		this.lossCarMainVo = lossCarMainVo;
	}
	
	public String getOperateType() {
		return operateType;
	}
	
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	
	public String getAuditPriceRate() {
		return auditPriceRate;
	}
	
	public void setAuditPriceRate(String auditPriceRate) {
		this.auditPriceRate = auditPriceRate;
	}
	
	public String getAuditLossRate() {
		return auditLossRate;
	}
	
	public void setAuditLossRate(String auditLossRate) {
		this.auditLossRate = auditLossRate;
	}
	
	public String getLocalAreaName() {
		return localAreaName;
	}
	
	public void setLocalAreaName(String localAreaName) {
		this.localAreaName = localAreaName;
	}
	
	public Date getCreatDate() {
		return creatDate;
	}
	
	public void setCreatDate(Date creatDate) {
		this.creatDate = creatDate;
	}
	
	public String getRecheck() {
		return recheck;
	}

	public void setRecheck(String recheck) {
		this.recheck = recheck;
	}

	public String getJgfaFlag() {
		return jgfaFlag;
	}

	public void setJgfaFlag(String jgfaFlag) {
		this.jgfaFlag = jgfaFlag;
	}

	public BigDecimal getFitDiscount() {
		return fitDiscount;
	}

	public void setFitDiscount(BigDecimal fitDiscount) {
		this.fitDiscount = fitDiscount;
	}

	public BigDecimal getRepairDiscount() {
		return repairDiscount;
	}

	public void setRepairDiscount(BigDecimal repairDiscount) {
		this.repairDiscount = repairDiscount;
	}

	public String getRequestXml() {
		return requestXml;
	}

	public void setRequestXml(String requestXml) {
		this.requestXml = requestXml;
	}

	public String getResponseXml() {
		return responseXml;
	}

	public void setResponseXml(String responseXml) {
		this.responseXml = responseXml;
	}

	public String getNodeCode() {
		return nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	public String getRepairCode() {
		return repairCode;
	}

	public void setRepairCode(String repairCode) {
		this.repairCode = repairCode;
	}

	public String getReView() {
		return reView;
	}

	public void setReView(String reView) {
		this.reView = reView;
	}

	public SendVo getSendVo() {
		return sendVo;
	}

	public void setSendVo(SendVo sendVo) {
		this.sendVo = sendVo;
	}

	public String getJyUrl() {
		return jyUrl;
	}

	public void setJyUrl(String jyUrl) {
		this.jyUrl = jyUrl;
	}
	
}
