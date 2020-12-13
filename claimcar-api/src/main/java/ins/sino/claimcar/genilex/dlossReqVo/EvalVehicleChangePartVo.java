package ins.sino.claimcar.genilex.dlossReqVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("EvalVehicleChangePart")
public class EvalVehicleChangePartVo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("EvalID")
	private String evalID;//定核损ID
	@XStreamAlias("HandAddpartFlag")
	private String handAddpartFlag;//配件平台手工添加标志	
	@XStreamAlias("ItemCode")
	private String itemCode;//精友零件编码		
	@XStreamAlias("ItemName")
	private String itemName;//配件名称
	@XStreamAlias("FactoryPartCode")
	private String factoryPartCode;//原厂零件号	
	@XStreamAlias("PartGroupCode")
	private String partGroupCode;//配件分组编码
	@XStreamAlias("PartGroupName")
	private String partGroupName;//配件分组名称
	@XStreamAlias("EvalPartPrice")
	private String EvalPartPrice;//定损单价金额
	@XStreamAlias("EvalPartAmount")
	private String evalPartAmount;//定损配件数量
	@XStreamAlias("EvalPartSum")
	private String evalPartSum;//定损金额	
	@XStreamAlias("EvalPartSumFirst")
	private String evalPartSumFirst;//首次定损金额	
	@XStreamAlias("EstiPartPrice")
	private String estiPartPrice;//核价单价金额	
	@XStreamAlias("EstiPartSum")
	private String estiPartSum;//核价金额	
	@XStreamAlias("EstiRemainsPrice")
	private String estiRemainsPrice;//核价残值
	@XStreamAlias("ApprPartPrice")
	private String apprPartPrice;//核损单价金额
	@XStreamAlias("ApprPartAmount")
	private String ApprPartAmount;//核损配件数量
	@XStreamAlias("ApprPartSum")
	private String apprPartSum;//核损金额
	@XStreamAlias("LocalPrice")
	private String localPrice;//本地价格
	@XStreamAlias("SystemPrice")
	private String systemPrice;//系统价格
	@XStreamAlias("AreaPrice")
	private String areaPrice;//区域价格
	@XStreamAlias("ResurveyFlag")
	private String resurveyFlag;//复勘标志
	@XStreamAlias("SelfPayPrice")
	private String selfPayPrice;//自付
	@XStreamAlias("RemainsPrice")
	private String RemainsPrice;//残值
	@XStreamAlias("ResurveyPrice")
	private String ResurveyPrice;//复勘价	
	@XStreamAlias("EvalFloatRatio")
	private String evalFloatRatio;//管理费率
	@XStreamAlias("FitBackFlag")
	private String fitBackFlag;//旧件回收标志	
	@XStreamAlias("RecycleType")
	private String recycleType;//回收类型
	@XStreamAlias("OutsideRadio")
	private String outsideRadio;//是否专修厂
	@XStreamAlias("OutsideRepairName")
	private String outsideRepairName;//专修厂名称
	@XStreamAlias("OutsideRepairCode")
	private String OutsideRepairCode;//专修厂代码	
	@XStreamAlias("OutsidePrice")
	private String OutsidePrice;//专修价格
	@XStreamAlias("IsSubjoin")
	private String isSubjoin;//是否增补
	@XStreamAlias("CreateBy")
	private String createBy;//创建者
	@XStreamAlias("CreateTime")
	private String createtime;//创建日期
	@XStreamAlias("UpdateBy")
	private String updateby;//更新者	
	@XStreamAlias("UpdateTime")
	private String updatetime;//更新日期	
	public String getEvalID() {
		return evalID;
	}
	public void setEvalID(String evalID) {
		this.evalID = evalID;
	}
	public String getHandAddpartFlag() {
		return handAddpartFlag;
	}
	public void setHandAddpartFlag(String handAddpartFlag) {
		this.handAddpartFlag = handAddpartFlag;
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
	public String getFactoryPartCode() {
		return factoryPartCode;
	}
	public void setFactoryPartCode(String factoryPartCode) {
		this.factoryPartCode = factoryPartCode;
	}
	public String getPartGroupCode() {
		return partGroupCode;
	}
	public void setPartGroupCode(String partGroupCode) {
		this.partGroupCode = partGroupCode;
	}
	public String getPartGroupName() {
		return partGroupName;
	}
	public void setPartGroupName(String partGroupName) {
		this.partGroupName = partGroupName;
	}
	public String getEvalPartPrice() {
		return EvalPartPrice;
	}
	public void setEvalPartPrice(String evalPartPrice) {
		EvalPartPrice = evalPartPrice;
	}
	public String getEvalPartAmount() {
		return evalPartAmount;
	}
	public void setEvalPartAmount(String evalPartAmount) {
		this.evalPartAmount = evalPartAmount;
	}
	public String getEvalPartSum() {
		return evalPartSum;
	}
	public void setEvalPartSum(String evalPartSum) {
		this.evalPartSum = evalPartSum;
	}
	public String getEvalPartSumFirst() {
		return evalPartSumFirst;
	}
	public void setEvalPartSumFirst(String evalPartSumFirst) {
		this.evalPartSumFirst = evalPartSumFirst;
	}
	public String getEstiPartPrice() {
		return estiPartPrice;
	}
	public void setEstiPartPrice(String estiPartPrice) {
		this.estiPartPrice = estiPartPrice;
	}
	public String getEstiPartSum() {
		return estiPartSum;
	}
	public void setEstiPartSum(String estiPartSum) {
		this.estiPartSum = estiPartSum;
	}
	public String getEstiRemainsPrice() {
		return estiRemainsPrice;
	}
	public void setEstiRemainsPrice(String estiRemainsPrice) {
		this.estiRemainsPrice = estiRemainsPrice;
	}
	public String getApprPartPrice() {
		return apprPartPrice;
	}
	public void setApprPartPrice(String apprPartPrice) {
		this.apprPartPrice = apprPartPrice;
	}
	public String getApprPartAmount() {
		return ApprPartAmount;
	}
	public void setApprPartAmount(String apprPartAmount) {
		ApprPartAmount = apprPartAmount;
	}
	public String getApprPartSum() {
		return apprPartSum;
	}
	public void setApprPartSum(String apprPartSum) {
		this.apprPartSum = apprPartSum;
	}
	public String getLocalPrice() {
		return localPrice;
	}
	public void setLocalPrice(String localPrice) {
		this.localPrice = localPrice;
	}
	public String getSystemPrice() {
		return systemPrice;
	}
	public void setSystemPrice(String systemPrice) {
		this.systemPrice = systemPrice;
	}
	public String getAreaPrice() {
		return areaPrice;
	}
	public void setAreaPrice(String areaPrice) {
		this.areaPrice = areaPrice;
	}
	public String getResurveyFlag() {
		return resurveyFlag;
	}
	public void setResurveyFlag(String resurveyFlag) {
		this.resurveyFlag = resurveyFlag;
	}
	public String getSelfPayPrice() {
		return selfPayPrice;
	}
	public void setSelfPayPrice(String selfPayPrice) {
		this.selfPayPrice = selfPayPrice;
	}
	public String getRemainsPrice() {
		return RemainsPrice;
	}
	public void setRemainsPrice(String remainsPrice) {
		RemainsPrice = remainsPrice;
	}
	public String getResurveyPrice() {
		return ResurveyPrice;
	}
	public void setResurveyPrice(String resurveyPrice) {
		ResurveyPrice = resurveyPrice;
	}
	public String getEvalFloatRatio() {
		return evalFloatRatio;
	}
	public void setEvalFloatRatio(String evalFloatRatio) {
		this.evalFloatRatio = evalFloatRatio;
	}
	public String getFitBackFlag() {
		return fitBackFlag;
	}
	public void setFitBackFlag(String fitBackFlag) {
		this.fitBackFlag = fitBackFlag;
	}
	public String getRecycleType() {
		return recycleType;
	}
	public void setRecycleType(String recycleType) {
		this.recycleType = recycleType;
	}
	public String getOutsideRadio() {
		return outsideRadio;
	}
	public void setOutsideRadio(String outsideRadio) {
		this.outsideRadio = outsideRadio;
	}
	public String getOutsideRepairName() {
		return outsideRepairName;
	}
	public void setOutsideRepairName(String outsideRepairName) {
		this.outsideRepairName = outsideRepairName;
	}
	public String getOutsideRepairCode() {
		return OutsideRepairCode;
	}
	public void setOutsideRepairCode(String outsideRepairCode) {
		OutsideRepairCode = outsideRepairCode;
	}
	public String getOutsidePrice() {
		return OutsidePrice;
	}
	public void setOutsidePrice(String outsidePrice) {
		OutsidePrice = outsidePrice;
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
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getUpdateby() {
		return updateby;
	}
	public void setUpdateby(String updateby) {
		this.updateby = updateby;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
		


}
