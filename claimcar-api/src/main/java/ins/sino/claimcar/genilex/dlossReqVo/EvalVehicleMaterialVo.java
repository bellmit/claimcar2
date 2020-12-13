package ins.sino.claimcar.genilex.dlossReqVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("EvalVehicleMaterial")
public class EvalVehicleMaterialVo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("EvalID")
	private String evalID;//定核损ID
	@XStreamAlias("ItemCode")
	private String itemCode;//项目编码	
	@XStreamAlias("ItemName")
	private String itemName;//项目名称		
	@XStreamAlias("MateTypeName")
	private String mateTypeName;//辅料项目类型名称
	@XStreamAlias("MateTypeCode")
	private String mateTypeCode;//辅料项目类型编码	
	@XStreamAlias("EvalUnitPrice")
	private String evalUnitPrice;//定损单价
	@XStreamAlias("EvalMateAmount")
	private String evalMateAmount;//定损数量
	@XStreamAlias("EvalMateSum")
	private String evalMateSum;//定损金额
	@XStreamAlias("EstiUnitPrice")
	private String estiUnitPrice;//核价单价
	@XStreamAlias("EstiMateSum")
	private String estiMateSum;//核价金额	
	@XStreamAlias("ApprUnitPrice")
	private String apprUnitPrice;//核损单价	
	@XStreamAlias("ApprMateSum")
	private String apprMateSum;//核损金额
	@XStreamAlias("ReferencePrice")
	private String referencePrice;//参考价格
	@XStreamAlias("EstiMateAmount")
	private String estiMateAmount;//核价数量
	@XStreamAlias("EstiFloatRatio")
	private String estiFloatRatio;//核价费率
	@XStreamAlias("IsSubjoin")
	private String isSubjoin;//是否增补
	@XStreamAlias("CreateBy")
	private String createBy;//创建者	
	@XStreamAlias("CreateTime")
	private String createTime;//创建日期	
	@XStreamAlias("UpdateBy")
	private String updateBy;//更新者		
	@XStreamAlias("UpdateTime")
	private String updateTime;//更新日期
	public String getEvalID() {
		return evalID;
	}
	public void setEvalID(String evalID) {
		this.evalID = evalID;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getMateTypeName() {
		return mateTypeName;
	}
	public void setMateTypeName(String mateTypeName) {
		this.mateTypeName = mateTypeName;
	}
	public String getMateTypeCode() {
		return mateTypeCode;
	}
	public void setMateTypeCode(String mateTypeCode) {
		this.mateTypeCode = mateTypeCode;
	}
	public String getEvalUnitPrice() {
		return evalUnitPrice;
	}
	public void setEvalUnitPrice(String evalUnitPrice) {
		this.evalUnitPrice = evalUnitPrice;
	}
	public String getEvalMateAmount() {
		return evalMateAmount;
	}
	public void setEvalMateAmount(String evalMateAmount) {
		this.evalMateAmount = evalMateAmount;
	}
	public String getEvalMateSum() {
		return evalMateSum;
	}
	public void setEvalMateSum(String evalMateSum) {
		this.evalMateSum = evalMateSum;
	}
	public String getEstiUnitPrice() {
		return estiUnitPrice;
	}
	public void setEstiUnitPrice(String estiUnitPrice) {
		this.estiUnitPrice = estiUnitPrice;
	}
	public String getEstiMateSum() {
		return estiMateSum;
	}
	public void setEstiMateSum(String estiMateSum) {
		this.estiMateSum = estiMateSum;
	}
	public String getApprUnitPrice() {
		return apprUnitPrice;
	}
	public void setApprUnitPrice(String apprUnitPrice) {
		this.apprUnitPrice = apprUnitPrice;
	}
	public String getApprMateSum() {
		return apprMateSum;
	}
	public void setApprMateSum(String apprMateSum) {
		this.apprMateSum = apprMateSum;
	}
	public String getReferencePrice() {
		return referencePrice;
	}
	public void setReferencePrice(String referencePrice) {
		this.referencePrice = referencePrice;
	}
	public String getEstiMateAmount() {
		return estiMateAmount;
	}
	public void setEstiMateAmount(String estiMateAmount) {
		this.estiMateAmount = estiMateAmount;
	}
	public String getEstiFloatRatio() {
		return estiFloatRatio;
	}
	public void setEstiFloatRatio(String estiFloatRatio) {
		this.estiFloatRatio = estiFloatRatio;
	}
	public String getIsSubjoin() {
		return isSubjoin;
	}
	public void setIsSubjoin(String isSubjoin) {
		this.isSubjoin = isSubjoin;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
		

}
