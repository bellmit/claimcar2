package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class LossFitInfoItem implements Serializable{
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "PartId")
	private String partId;//定损明细主键 要保存，这是定损系统的明细主键，理赔系统可能有自己的明细主键，靠这个对应两边的明细
	@XmlElement(name = "PartCode")
	private String partCode;//零配件原厂编码
	@XmlElement(name = "ItemName")
	private String itemName;//项目名称
	@XmlElement(name = "SysGuidePrice")
	private String sysGuidePrice;//系统4S店价
	@XmlElement(name = "SysMarketPrice")
	private String sysMarketPrice;//系统市场价
	@XmlElement(name = "LocalGuidePrice")
	private String localGuidePrice;//本地4S店价
	@XmlElement(name = "LocalMarketPrice")
	private String localMarketPrice;//本地市场原厂价
	@XmlElement(name = "LocalApplicablePrice")
	private String localApplicablePrice;//本地适用价
	@XmlElement(name = "LocalPrice")
	private String localPrice;//定损参考价
	@XmlElement(name = "Count")
	private String count;//数量
	@XmlElement(name = "MaterialFee")
	private String materialFee;//定损材料费
	@XmlElement(name = "SelfConfigFlag")
	private String selfConfigFlag;//自定义配件标记 0-系统点选 1-手工自定义 2-标准点选 
	@XmlElement(name = "ItemCoverCode")
	private String itemCoverCode;//险种代码
	@XmlElement(name = "Remark")
	private String remark;//备注
	@XmlElement(name = "ChgCompSetCode")
	private String chgCompSetCode;//参考价格类型编码1-4S价格 2-市场原厂价格 3-品牌价格 4-适用价格 5-再制造价格
	@XmlElement(name = "FitBackFlag")
	private String fitBackFlag;//回收标志0—不回收 1—回收
	@XmlElement(name = "RemainsPrice")
	private String remainsPrice;//残值
	@XmlElement(name = "DetectedFlag")
	private String detectedFlag;//待检测标志 0-无需检测 1-待检测
	@XmlElement(name = "DirectSupplyFlag")
	private String directSupplyFlag;//直供测标志 0-非直供 1-直供
	@XmlElement(name = "DirectSupplier")
	private String directSupplier;//直供商 供应商名称
	@XmlElement(name = "ManageSingleRate")
	private String manageSingleRate;//管理费率
	@XmlElement(name = "ManageSingleFee")
	private String manageSingleFee;//管理费
	@XmlElement(name = "EvalPartSum")
	private String evalPartSum;//换件合计
	@XmlElement(name = "SelfPayRate")
	private String selfPayRate;//自付比例
	@XmlElement(name = "EvalPartSumFirst")
	private String evalPartSumFirst;//首次定损金额
	@XmlElement(name = "IfWading")
	private String ifWading;//涉水标志
	
	public String getPartId() {
		return partId;
	}
	public void setPartId(String partId) {
		this.partId = partId;
	}
	public String getPartCode() {
		return partCode;
	}
	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getSysGuidePrice() {
		return sysGuidePrice;
	}
	public void setSysGuidePrice(String sysGuidePrice) {
		this.sysGuidePrice = sysGuidePrice;
	}
	public String getSysMarketPrice() {
		return sysMarketPrice;
	}
	public void setSysMarketPrice(String sysMarketPrice) {
		this.sysMarketPrice = sysMarketPrice;
	}
	public String getLocalGuidePrice() {
		return localGuidePrice;
	}
	public void setLocalGuidePrice(String localGuidePrice) {
		this.localGuidePrice = localGuidePrice;
	}
	public String getLocalMarketPrice() {
		return localMarketPrice;
	}
	public void setLocalMarketPrice(String localMarketPrice) {
		this.localMarketPrice = localMarketPrice;
	}
	public String getLocalApplicablePrice() {
		return localApplicablePrice;
	}
	public void setLocalApplicablePrice(String localApplicablePrice) {
		this.localApplicablePrice = localApplicablePrice;
	}
	public String getLocalPrice() {
		return localPrice;
	}
	public void setLocalPrice(String localPrice) {
		this.localPrice = localPrice;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getMaterialFee() {
		return materialFee;
	}
	public void setMaterialFee(String materialFee) {
		this.materialFee = materialFee;
	}
	public String getSelfConfigFlag() {
		return selfConfigFlag;
	}
	public void setSelfConfigFlag(String selfConfigFlag) {
		this.selfConfigFlag = selfConfigFlag;
	}
	public String getItemCoverCode() {
		return itemCoverCode;
	}
	public void setItemCoverCode(String itemCoverCode) {
		this.itemCoverCode = itemCoverCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getChgCompSetCode() {
		return chgCompSetCode;
	}
	public void setChgCompSetCode(String chgCompSetCode) {
		this.chgCompSetCode = chgCompSetCode;
	}
	public String getFitBackFlag() {
		return fitBackFlag;
	}
	public void setFitBackFlag(String fitBackFlag) {
		this.fitBackFlag = fitBackFlag;
	}
	public String getRemainsPrice() {
		return remainsPrice;
	}
	public void setRemainsPrice(String remainsPrice) {
		this.remainsPrice = remainsPrice;
	}
	public String getDetectedFlag() {
		return detectedFlag;
	}
	public void setDetectedFlag(String detectedFlag) {
		this.detectedFlag = detectedFlag;
	}
	public String getDirectSupplyFlag() {
		return directSupplyFlag;
	}
	public void setDirectSupplyFlag(String directSupplyFlag) {
		this.directSupplyFlag = directSupplyFlag;
	}
	public String getDirectSupplier() {
		return directSupplier;
	}
	public void setDirectSupplier(String directSupplier) {
		this.directSupplier = directSupplier;
	}
	public String getManageSingleRate() {
		return manageSingleRate;
	}
	public void setManageSingleRate(String manageSingleRate) {
		this.manageSingleRate = manageSingleRate;
	}
	public String getManageSingleFee() {
		return manageSingleFee;
	}
	public void setManageSingleFee(String manageSingleFee) {
		this.manageSingleFee = manageSingleFee;
	}
	public String getEvalPartSum() {
		return evalPartSum;
	}
	public void setEvalPartSum(String evalPartSum) {
		this.evalPartSum = evalPartSum;
	}
	public String getSelfPayRate() {
		return selfPayRate;
	}
	public void setSelfPayRate(String selfPayRate) {
		this.selfPayRate = selfPayRate;
	}
	public String getEvalPartSumFirst() {
		return evalPartSumFirst;
	}
	public void setEvalPartSumFirst(String evalPartSumFirst) {
		this.evalPartSumFirst = evalPartSumFirst;
	}
	public String getIfWading() {
		return ifWading;
	}
	public void setIfWading(String ifWading) {
		this.ifWading = ifWading;
	}
}
