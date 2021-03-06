package ins.sino.claimcar.losscar.vo;

// Generated by Hibernate Tools 3.2.5 (sinosoft version), Don't modify!
import java.math.BigDecimal;
import java.util.Date;


/**
 * Vo Base Class of PO PrpLDlossCarRepair
 */ 
public class PrpLDlossCarRepairVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String repairFlag;
	private String claimNo;
	private String riskCode;
	private String policyNo;
	private Integer serialNo;
	private String registNo;
	private Long itemKindNo;
	private String kindCode;
    private String kindName;
	private String licenseNo;
	private String licenseColorCode;
	private String carKindCode;
	private String repairFactoryCode;
	private String repairFactoryName;
	private String handlerCode;
	private Date repairStartDate;
	private Date repairEndDate;
	private String sanctioner;
	private String approverCode;
	private String operatorCode;
	private String compCode;
	private String compName;
	private BigDecimal manHour;
	private BigDecimal manHourUnitPrice;
	private BigDecimal manHourFee;
	private BigDecimal materialFee;
	private BigDecimal lossRate;
	private String currency;
	private BigDecimal sumDefLoss;
	private String remark;
	private String flag;
	private BigDecimal veriManHour;
	private BigDecimal veriManUnitPrice;
	private BigDecimal veriManHourFee;
	private BigDecimal veriMaterQuantity;
	private BigDecimal veriMaterUnitPrice;
	private BigDecimal veriMaterialFee;
	private BigDecimal veriLossRate;
	private BigDecimal sumVeriLoss;
	private String veriRemark;
	private BigDecimal materialQuantity;
	private BigDecimal materialUnitPrice;
	private BigDecimal sumCheckLoss;
	private String backCheckRemark;
	private String lossItemCode;
	private String partCode;
	private String partName;
	private String repairType;
	private BigDecimal firstSumDefLoss;
	private String compensateBackFlag;
	private BigDecimal veripManunitPrice;
	private String veripRemark;
	private String repairId;
	private String repairCode;
	private String repairName;
	private Integer levelCode;
	private String levelName;
	private String selfConfigFlag;
	private BigDecimal veriDerogationPrice;	//??????????????????
	private BigDecimal derogationPrice;//????????????????????????
	private PrpLDlossCarMainVo prpLDlossCarMainVo;
	private BigDecimal dLChkAuditManpowerFee;// ???????????????
	private BigDecimal dLChkAuditMaterialFee;// ???????????????
	private BigDecimal dLChkApprHour;// ???????????????
	private BigDecimal dLChkApprRemainsPrice;// ????????????
	private BigDecimal dLChkApprRepairSum;// ????????????
	private String dLChkRemark;// ????????????



	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	

	public BigDecimal getVeriDerogationPrice() {
		return veriDerogationPrice;
	}

	public void setVeriDerogationPrice(BigDecimal veriDerogationPrice) {
		this.veriDerogationPrice = veriDerogationPrice;
	}

	public String getRepairFlag() {
		return this.repairFlag;
	}

	public void setRepairFlag(String repairFlag) {
		this.repairFlag = repairFlag;
	}

	public String getClaimNo() {
		return this.claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getRiskCode() {
		return this.riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public String getPolicyNo() {
		return this.policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public Integer getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}

	public String getRegistNo() {
		return this.registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public Long getItemKindNo() {
		return this.itemKindNo;
	}

	public void setItemKindNo(Long itemKindNo) {
		this.itemKindNo = itemKindNo;
	}

	public String getKindCode() {
		return this.kindCode;
	}

	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}
	
	public String getKindName() {
		return kindName;
	}
	
	public void setKindName(String kindName) {
		this.kindName = kindName;
	}

	public String getLicenseNo() {
		return this.licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getLicenseColorCode() {
		return this.licenseColorCode;
	}

	public void setLicenseColorCode(String licenseColorCode) {
		this.licenseColorCode = licenseColorCode;
	}

	public String getCarKindCode() {
		return this.carKindCode;
	}

	public void setCarKindCode(String carKindCode) {
		this.carKindCode = carKindCode;
	}

	public String getRepairFactoryCode() {
		return this.repairFactoryCode;
	}

	public void setRepairFactoryCode(String repairFactoryCode) {
		this.repairFactoryCode = repairFactoryCode;
	}

	public String getRepairFactoryName() {
		return this.repairFactoryName;
	}

	public void setRepairFactoryName(String repairFactoryName) {
		this.repairFactoryName = repairFactoryName;
	}

	public String getHandlerCode() {
		return this.handlerCode;
	}

	public void setHandlerCode(String handlerCode) {
		this.handlerCode = handlerCode;
	}

	public Date getRepairStartDate() {
		return this.repairStartDate;
	}

	public void setRepairStartDate(Date repairStartDate) {
		this.repairStartDate = repairStartDate;
	}

	public Date getRepairEndDate() {
		return this.repairEndDate;
	}

	public void setRepairEndDate(Date repairEndDate) {
		this.repairEndDate = repairEndDate;
	}

	public String getSanctioner() {
		return this.sanctioner;
	}

	public void setSanctioner(String sanctioner) {
		this.sanctioner = sanctioner;
	}

	public String getApproverCode() {
		return this.approverCode;
	}

	public void setApproverCode(String approverCode) {
		this.approverCode = approverCode;
	}

	public String getOperatorCode() {
		return this.operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public String getCompCode() {
		return this.compCode;
	}

	public void setCompCode(String compCode) {
		this.compCode = compCode;
	}

	public String getCompName() {
		return this.compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public BigDecimal getManHour() {
		return this.manHour;
	}

	public void setManHour(BigDecimal manHour) {
		this.manHour = manHour;
	}

	public BigDecimal getManHourUnitPrice() {
		return this.manHourUnitPrice;
	}

	public void setManHourUnitPrice(BigDecimal manHourUnitPrice) {
		this.manHourUnitPrice = manHourUnitPrice;
	}

	public BigDecimal getManHourFee() {
		return this.manHourFee;
	}

	public void setManHourFee(BigDecimal manHourFee) {
		this.manHourFee = manHourFee;
	}

	public BigDecimal getMaterialFee() {
		return this.materialFee;
	}

	public void setMaterialFee(BigDecimal materialFee) {
		this.materialFee = materialFee;
	}

	public BigDecimal getLossRate() {
		return this.lossRate;
	}

	public void setLossRate(BigDecimal lossRate) {
		this.lossRate = lossRate;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getSumDefLoss() {
		return this.sumDefLoss;
	}

	public void setSumDefLoss(BigDecimal sumDefLoss) {
		this.sumDefLoss = sumDefLoss;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public BigDecimal getVeriManHour() {
		return this.veriManHour;
	}

	public void setVeriManHour(BigDecimal veriManHour) {
		this.veriManHour = veriManHour;
	}

	public BigDecimal getVeriManUnitPrice() {
		return this.veriManUnitPrice;
	}

	public void setVeriManUnitPrice(BigDecimal veriManUnitPrice) {
		this.veriManUnitPrice = veriManUnitPrice;
	}

	public BigDecimal getVeriManHourFee() {
		return this.veriManHourFee;
	}

	public void setVeriManHourFee(BigDecimal veriManHourFee) {
		this.veriManHourFee = veriManHourFee;
	}

	public BigDecimal getVeriMaterQuantity() {
		return this.veriMaterQuantity;
	}

	public void setVeriMaterQuantity(BigDecimal veriMaterQuantity) {
		this.veriMaterQuantity = veriMaterQuantity;
	}

	public BigDecimal getVeriMaterUnitPrice() {
		return this.veriMaterUnitPrice;
	}

	public void setVeriMaterUnitPrice(BigDecimal veriMaterUnitPrice) {
		this.veriMaterUnitPrice = veriMaterUnitPrice;
	}

	public BigDecimal getVeriMaterialFee() {
		return this.veriMaterialFee;
	}

	public void setVeriMaterialFee(BigDecimal veriMaterialFee) {
		this.veriMaterialFee = veriMaterialFee;
	}

	public BigDecimal getVeriLossRate() {
		return this.veriLossRate;
	}

	public void setVeriLossRate(BigDecimal veriLossRate) {
		this.veriLossRate = veriLossRate;
	}
	
	public BigDecimal getSumVeriLoss() {
		return sumVeriLoss;
	}
	
	public void setSumVeriLoss(BigDecimal sumVeriLoss) {
		this.sumVeriLoss = sumVeriLoss;
	}

	public String getVeriRemark() {
		return this.veriRemark;
	}

	public void setVeriRemark(String veriRemark) {
		this.veriRemark = veriRemark;
	}

	public BigDecimal getMaterialQuantity() {
		return this.materialQuantity;
	}

	public void setMaterialQuantity(BigDecimal materialQuantity) {
		this.materialQuantity = materialQuantity;
	}

	public BigDecimal getMaterialUnitPrice() {
		return this.materialUnitPrice;
	}

	public void setMaterialUnitPrice(BigDecimal materialUnitPrice) {
		this.materialUnitPrice = materialUnitPrice;
	}

	public BigDecimal getSumCheckLoss() {
		return this.sumCheckLoss;
	}

	public void setSumCheckLoss(BigDecimal sumCheckLoss) {
		this.sumCheckLoss = sumCheckLoss;
	}

	public String getBackCheckRemark() {
		return this.backCheckRemark;
	}

	public void setBackCheckRemark(String backCheckRemark) {
		this.backCheckRemark = backCheckRemark;
	}

	public String getLossItemCode() {
		return this.lossItemCode;
	}

	public void setLossItemCode(String lossItemCode) {
		this.lossItemCode = lossItemCode;
	}

	public String getPartCode() {
		return this.partCode;
	}

	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}

	public String getPartName() {
		return this.partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getRepairType() {
		return this.repairType;
	}

	public void setRepairType(String repairType) {
		this.repairType = repairType;
	}

	public BigDecimal getFirstSumDefLoss() {
		return this.firstSumDefLoss;
	}

	public void setFirstSumDefLoss(BigDecimal firstSumDefLoss) {
		this.firstSumDefLoss = firstSumDefLoss;
	}

	public String getCompensateBackFlag() {
		return this.compensateBackFlag;
	}

	public void setCompensateBackFlag(String compensateBackFlag) {
		this.compensateBackFlag = compensateBackFlag;
	}

	public BigDecimal getVeripManunitPrice() {
		return this.veripManunitPrice;
	}

	public void setVeripManunitPrice(BigDecimal veripManunitPrice) {
		this.veripManunitPrice = veripManunitPrice;
	}

	public String getVeripRemark() {
		return this.veripRemark;
	}

	public void setVeripRemark(String veripRemark) {
		this.veripRemark = veripRemark;
	}

	public String getRepairId() {
		return this.repairId;
	}

	public void setRepairId(String repairId) {
		this.repairId = repairId;
	}

	public String getRepairCode() {
		return this.repairCode;
	}

	public void setRepairCode(String repairCode) {
		this.repairCode = repairCode;
	}

	public String getRepairName() {
		return this.repairName;
	}

	public void setRepairName(String repairName) {
		this.repairName = repairName;
	}

	public Integer getLevelCode() {
		return this.levelCode;
	}

	public void setLevelCode(Integer levelCode) {
		this.levelCode = levelCode;
	}

	public String getLevelName() {
		return this.levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getSelfConfigFlag() {
		return this.selfConfigFlag;
	}

	public void setSelfConfigFlag(String selfConfigFlag) {
		this.selfConfigFlag = selfConfigFlag;
	}

	public BigDecimal getDerogationPrice() {
		return derogationPrice;
	}

	public void setDerogationPrice(BigDecimal derogationPrice) {
		this.derogationPrice = derogationPrice;
	}

	public PrpLDlossCarMainVo getPrpLDlossCarMainVo() {
		return prpLDlossCarMainVo;
	}

	public void setPrpLDlossCarMainVo(PrpLDlossCarMainVo prpLDlossCarMainVo) {
		this.prpLDlossCarMainVo = prpLDlossCarMainVo;
	}

	public BigDecimal getdLChkAuditManpowerFee() {
		return dLChkAuditManpowerFee;
	}

	public void setdLChkAuditManpowerFee(BigDecimal dLChkAuditManpowerFee) {
		this.dLChkAuditManpowerFee = dLChkAuditManpowerFee;
	}

	public BigDecimal getdLChkAuditMaterialFee() {
		return dLChkAuditMaterialFee;
	}

	public void setdLChkAuditMaterialFee(BigDecimal dLChkAuditMaterialFee) {
		this.dLChkAuditMaterialFee = dLChkAuditMaterialFee;
	}

	public BigDecimal getdLChkApprHour() {
		return dLChkApprHour;
	}

	public void setdLChkApprHour(BigDecimal dLChkApprHour) {
		this.dLChkApprHour = dLChkApprHour;
	}

	public BigDecimal getdLChkApprRemainsPrice() {
		return dLChkApprRemainsPrice;
	}

	public void setdLChkApprRemainsPrice(BigDecimal dLChkApprRemainsPrice) {
		this.dLChkApprRemainsPrice = dLChkApprRemainsPrice;
	}

	public BigDecimal getdLChkApprRepairSum() {
		return dLChkApprRepairSum;
	}

	public void setdLChkApprRepairSum(BigDecimal dLChkApprRepairSum) {
		this.dLChkApprRepairSum = dLChkApprRepairSum;
	}

	public String getdLChkRemark() {
		return dLChkRemark;
	}

	public void setdLChkRemark(String dLChkRemark) {
		this.dLChkRemark = dLChkRemark;
	}
}
