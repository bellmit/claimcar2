package ins.sino.claimcar.claimcarYJ.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("PART")
public class PrpLDlchkInfoVo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XStreamAlias("ID")
	private Long id;
	@XStreamAlias("PARTNAME")
	private String partName;
	@XStreamAlias("PARTNO")
	private String partNo;
	@XStreamAlias("PARTID")
	private String partId;
	@XStreamAlias("ORIGINAL")
	private String original;
	@XStreamAlias("OPERATOR")
	private String operator;
	@XStreamAlias("OPERATEDATE")
	private String operateDate;
	@XStreamAlias("IDENTIFYRESULT")
	private String identifyResult;
	@XStreamAlias("IDENTIFYRESULTTYPE")
	private String identifyResultType;
	@XStreamAlias("UNITPRICE")
	private String unitPrice;
	@XStreamAlias("APPRAISALPRICE")
	private String appraisalPrice;
	@XStreamAlias("DECREASEAMOUNT")
	private String decreaseAmount;
	@XStreamAlias("REMARK")
	private String remark;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public String getPartNo() {
		return partNo;
	}
	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}
	public String getPartId() {
		return partId;
	}
	public void setPartId(String partId) {
		this.partId = partId;
	}
	public String getOriginal() {
		return original;
	}
	public void setOriginal(String original) {
		this.original = original;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getOperateDate() {
		return operateDate;
	}
	public void setOperateDate(String operateDate) {
		this.operateDate = operateDate;
	}
	public String getIdentifyResult() {
		return identifyResult;
	}
	public void setIdentifyResult(String identifyResult) {
		this.identifyResult = identifyResult;
	}
	public String getIdentifyResultType() {
		return identifyResultType;
	}
	public void setIdentifyResultType(String identifyResultType) {
		this.identifyResultType = identifyResultType;
	}
	public String getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getAppraisalPrice() {
		return appraisalPrice;
	}
	public void setAppraisalPrice(String appraisalPrice) {
		this.appraisalPrice = appraisalPrice;
	}
	public String getDecreaseAmount() {
		return decreaseAmount;
	}
	public void setDecreaseAmount(String decreaseAmount) {
		this.decreaseAmount = decreaseAmount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
