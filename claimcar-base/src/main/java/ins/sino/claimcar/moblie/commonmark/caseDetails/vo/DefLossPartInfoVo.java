package ins.sino.claimcar.moblie.commonmark.caseDetails.vo;


import java.math.BigDecimal;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PARTINFO")
public class DefLossPartInfoVo {

	private static final long serialVersionUID = 8423652723600188374L;

	@XStreamAlias("PARTID")
	private String partId;  //定损明细主键
	
	@XStreamAlias("PARTCODE")
	private String partCode;  //零配件原厂编码
	
	@XStreamAlias("ITEMNAME")
	private String itemName;  //项目名称
	@XStreamAlias("SYSGUIDEPRICE")
	private Double sysguidePrice;  //系统4S店价
	@XStreamAlias("SYSMARKETPRICE")
	private Double sysmarketPrice;  //系统区域市场价
	@XStreamAlias("LOCALGUIDEPRICE")
	private Double localGuidePrice;  //本地区域原厂价
	@XStreamAlias("LOCALMARKETPRICE")
	private Double localMarketPrice;  //本地区域市场价
	@XStreamAlias("LOCALAPPLICABLEPRICE")
	private Double localApplicablePrice;  //本地区域适用价
	@XStreamAlias("LOCALPRICE")
	private Double localPrice;  //本地价
	@XStreamAlias("COUNT")
	private Integer count;  //数量
	@XStreamAlias("MATERIALFEE")
	private Double materialFee;  //定损材料费（定损单价）
	@XStreamAlias("SELFCONFIGFLAG")
	private String selfconfigFlag;  //自定义配件标记
	@XStreamAlias("ITEMCOVERCODE")
	private String itemCoverCode;  //险种代码
	@XStreamAlias("REMARK")
	private String remark;  //备注
	@XStreamAlias("CHGCOMPSETCODE")
	private String chgcompsetCode;  //参考价格类型编码
	@XStreamAlias("FITBACKFLAG")
	private String fitbackFlag;  //回收标志

	@XStreamAlias("REMAINSPRICE")
	private Double remainsPrice;  //残值
	@XStreamAlias("DETECTEDFLAG")
	private String detectedFlag;  //待检测标志
	@XStreamAlias("DIRECTSUPPLYFLAG")
	private String directSupplyFlag;  //直供测标志
	@XStreamAlias("DIRECTSUPPLIER")
	private String directSupplier;  //直供商
	@XStreamAlias("MANAGESINGLERATE")
	private BigDecimal manageSingleRate;  //管理费率
	@XStreamAlias("MANAGESINGLEFEE")
	private BigDecimal manageSingleFee;  //管理费
	@XStreamAlias("EVALPARTSUM")
	private Double evalpartSum;  //换件合计
	@XStreamAlias("RECYCLEPARTFLAG")
	private String recyclePartFlag;  //回收方式
	@XStreamAlias("SELFPAYRATE")
	private BigDecimal selfPayRate;  //自付比例
	@XStreamAlias("IFWADING")
	private String ifWading;  //涉水标志
	@XStreamAlias("LOSSFEE2")
	private Double lossFee2;  //核损价格
	@XStreamAlias("REMARK2")
	private String remark2;  //核损备注
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
	public Double getSysguidePrice() {
		return sysguidePrice;
	}
	public void setSysguidePrice(Double sysguidePrice) {
		this.sysguidePrice = sysguidePrice;
	}
	public Double getSysmarketPrice() {
		return sysmarketPrice;
	}
	public void setSysmarketPrice(Double sysmarketPrice) {
		this.sysmarketPrice = sysmarketPrice;
	}
	public Double getLocalGuidePrice() {
		return localGuidePrice;
	}
	public void setLocalGuidePrice(Double localGuidePrice) {
		this.localGuidePrice = localGuidePrice;
	}
	public Double getLocalApplicablePrice() {
		return localApplicablePrice;
	}
	public void setLocalApplicablePrice(Double localApplicablePrice) {
		this.localApplicablePrice = localApplicablePrice;
	}
	public Double getLocalPrice() {
		return localPrice;
	}
	public void setLocalPrice(Double localPrice) {
		this.localPrice = localPrice;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Double getMaterialFee() {
		return materialFee;
	}
	public void setMaterialFee(Double materialFee) {
		this.materialFee = materialFee;
	}
	public String getSelfconfigFlag() {
		return selfconfigFlag;
	}
	public void setSelfconfigFlag(String selfconfigFlag) {
		this.selfconfigFlag = selfconfigFlag;
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
	public String getChgcompsetCode() {
		return chgcompsetCode;
	}
	public void setChgcompsetCode(String chgcompsetCode) {
		this.chgcompsetCode = chgcompsetCode;
	}
	public String getFitbackFlag() {
		return fitbackFlag;
	}
	public void setFitbackFlag(String fitbackFlag) {
		this.fitbackFlag = fitbackFlag;
	}
	public Double getRemainsPrice() {
		return remainsPrice;
	}
	public void setRemainsPrice(Double remainsPrice) {
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
	public BigDecimal getManageSingleRate() {
		return manageSingleRate;
	}
	public void setManageSingleRate(BigDecimal manageSingleRate) {
		this.manageSingleRate = manageSingleRate;
	}
	public BigDecimal getManageSingleFee() {
		return manageSingleFee;
	}
	public void setManageSingleFee(BigDecimal manageSingleFee) {
		this.manageSingleFee = manageSingleFee;
	}
	public Double getEvalpartSum() {
		return evalpartSum;
	}
	public void setEvalpartSum(Double evalpartSum) {
		this.evalpartSum = evalpartSum;
	}
	public String getRecyclePartFlag() {
		return recyclePartFlag;
	}
	public void setRecyclePartFlag(String recyclePartFlag) {
		this.recyclePartFlag = recyclePartFlag;
	}
	public BigDecimal getSelfPayRate() {
		return selfPayRate;
	}
	public void setSelfPayRate(BigDecimal selfPayRate) {
		this.selfPayRate = selfPayRate;
	}
	public String getIfWading() {
		return ifWading;
	}
	public void setIfWading(String ifWading) {
		this.ifWading = ifWading;
	}
	public Double getLossFee2() {
		return lossFee2;
	}
	public void setLossFee2(Double lossFee2) {
		this.lossFee2 = lossFee2;
	}
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	public Double getLocalMarketPrice() {
		return localMarketPrice;
	}
	public void setLocalMarketPrice(Double localMarketPrice) {
		this.localMarketPrice = localMarketPrice;
	}
	
	
	
}
