package ins.sino.claimcar.moblie.loss.vo;


import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PARTINFO")
public class DefLossPartInfoVo implements Serializable{
	private static final long serialVersionUID = 8423652723600188374L;
	@XStreamAlias("PARTID")
	private String partId;//定损明细主键 要保存，这是定损系统的明细主键，理赔系统可能有自己的明细主键，靠这个对应两边的明细
	@XStreamAlias("PARTCODE")
	private String partCode;//零配件原厂编码
	@XStreamAlias("ITEMNAME")
	private String itemName;//项目名称
	@XStreamAlias("SYSGUIDEPRICE")
	private String sysGuidePrice;//系统4S店价
	@XStreamAlias("SYSMARKETPRICE")
	private String sysMarketPrice;//系统市场价
	@XStreamAlias("LOCALGUIDEPRICE")
	private String localGuidePrice;//本地4S店价
	@XStreamAlias("LOCALMARKETPRICE")
	private String localMarketPrice;//本地市场原厂价
	@XStreamAlias("LOCALAPPLICABLEPRICE")
	private String localApplicablePrice;//本地适用价
	@XStreamAlias("LOCALPRICE")
	private String localPrice;//定损参考价
	@XStreamAlias("COUNT")
	private String count;//数量
	@XStreamAlias("MATERIALFEE")
	private String materialFee;//定损材料费
	@XStreamAlias("SELFCONFIGFLAG")
	private String selfConfigFlag;//自定义配件标记 0-系统点选 1-手工自定义 2-标准点选 
	@XStreamAlias("ITEMCOVERCODE")
	private String itemCoverCode;//险种代码
	@XStreamAlias("REMARK")
	private String remark;//备注
	@XStreamAlias("CHGCOMPSETCODE")
	private String chgCompSetCode;//参考价格类型编码1-4S价格 2-市场原厂价格 3-品牌价格 4-适用价格 5-再制造价格
	@XStreamAlias("FITBACKFLAG")
	private String fitBackFlag;//回收标志0—不回收 1—回收
	@XStreamAlias("REMAINSPRICE")
	private String remainsPrice;//残值
	@XStreamAlias("DETECTEDFLAG")
	private String detectedFlag;//待检测标志 0-无需检测 1-待检测
	@XStreamAlias("DIRECTSUPPLYFLAG")
	private String directSupplyFlag;//直供测标志 0-非直供 1-直供
	@XStreamAlias("DIRECTSUPPLIER")
	private String directSupplier;//直供商 供应商名称
	@XStreamAlias("MANAGESINGLERATE")
	private String manageSingleRate;//管理费率
	@XStreamAlias("MANAGESINGLEFEE")
	private String manageSingleFee;//管理费
	@XStreamAlias("EVALPARTSUM")
	private String evalPartSum;//换件合计
	@XStreamAlias("RECYCLEPARTFLAG")
	private String recyclePartFlag;//回收方式
	@XStreamAlias("SELFPAYRATE")
	private String selfPayRate;//自付比例
	@XStreamAlias("IFWADING")
	private String ifWading;//涉水标志
	@XStreamAlias("LOSSFEE2")
	private String lossFee2;//核损价格
	@XStreamAlias("REMARK2")
	private String remark2;//核损备注
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
	public String getRecyclePartFlag() {
		return recyclePartFlag;
	}
	public void setRecyclePartFlag(String recyclePartFlag) {
		this.recyclePartFlag = recyclePartFlag;
	}
	public String getSelfPayRate() {
		return selfPayRate;
	}
	public void setSelfPayRate(String selfPayRate) {
		this.selfPayRate = selfPayRate;
	}
	public String getIfWading() {
		return ifWading;
	}
	public void setIfWading(String ifWading) {
		this.ifWading = ifWading;
	}
	public String getLossFee2() {
		return lossFee2;
	}
	public void setLossFee2(String lossFee2) {
		this.lossFee2 = lossFee2;
	}
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
}
