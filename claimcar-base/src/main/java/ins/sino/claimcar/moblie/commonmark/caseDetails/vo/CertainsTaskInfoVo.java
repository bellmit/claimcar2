package ins.sino.claimcar.moblie.commonmark.caseDetails.vo;


import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("TASKCARINFO")
public class CertainsTaskInfoVo implements Serializable{

	private static final long serialVersionUID = 8423652723600188374L;
	
	@XStreamAlias("CERTAINSID")
	private String certainsId;  //理赔定损ID
	@XStreamAlias("ISOBJECT")
	private String isObject;  //是否标的
	@XStreamAlias("OWERN")
	private String owern;  //车主
	@XStreamAlias("ENROLLDATE")
	private String enrollDate;  //初登日期
	@XStreamAlias("CARKINDCODE")
	private String carKindCode;  //车辆种类
	@XStreamAlias("FRAMENO")
	private String frameNo;  //车架号
	@XStreamAlias("VIN")
	private String vin;  //VIN码
	@XStreamAlias("ENGINENO")
	private String engineNo;  //发动机号
	@XStreamAlias("LICENSETYPE")
	private String licenseType;  //车牌种类
	@XStreamAlias("LICENSENO")
	private String licenseNo;  //车牌号码
	@XStreamAlias("VEHICLEMODLECODE")
	private String vehicleModleCode;  //车型编码
	@XStreamAlias("VEHICLEMODLENAME")
	private String vehicleModleName;  //车型名称
	@XStreamAlias("BRANDNAME")
	private String brandName;  //厂牌型号
	@XStreamAlias("KINDCODE")
	private String kindCode;  //定损险别
	@XStreamAlias("ASSESSTYPE")
	private String assessType;  //定损方式
	@XStreamAlias("ASSESSCARTYPE")
	private String assessCarType;  //定损车种
	@XStreamAlias("REPAIRNAME")
	private String repairName;  //修理厂名称
	@XStreamAlias("REPAIRTYPE")
	private String repairType;  //修理厂类型
	@XStreamAlias("REPAIRPHONE")
	private String repairPhone;  //修理厂手机
	@XStreamAlias("DEGREEDAMAGE")
	private String degreeDamage;  //损失程度
	@XStreamAlias("EXCESSTYPE")
	private String excessType;  //是否互碰自赔
	@XStreamAlias("SUMFITSFEE")
	private String sumFitsFee;  //换件金额合计
	@XStreamAlias("ACCESSORFEE")
	private String accessorFee;  //辅料金额合计
	@XStreamAlias("SUMREPAIRFEE")
	private String sumrepairFee;  //维修金额合计
	@XStreamAlias("SUMSALVAGEFEE")
	private String sumSalvageFee;  //残值合计
	@XStreamAlias("CERAMOUNT")
	private String ceraMount;  //定损金额合计
	@XStreamAlias("ASSISTFEE")
	private String assistFee;  //施救费
	@XStreamAlias("VIRTUALVALUE")
	private String virtualValue;  //总施救财产实际价值
	@XStreamAlias("VERSUMFITSFEE")
	private String verSumFitsFee;  //核损换金额合计
	@XStreamAlias("VERACCESSORFEE")
	private String verAccessorFee;  //核损辅料金额合计
	@XStreamAlias("VERSUMREPAIRFEE")
	private String verSumRepairFee;  //核损维修金额（工时费）合计
	@XStreamAlias("VERSUMSALVAGEFEE")
	private String verSumSalvageFee;  //核损残值合计
	@XStreamAlias("SUMVERIAMOUNT")
	private String sumVeriAmount;  //核损金额合计
	@XStreamAlias("LOSSOPINION")
	private String lossOpinion;  //定损意见
	@XStreamAlias("SUMFEEAMOUNT")
	private Double sumFeeAmount;  //费用合计
	@XStreamAlias("FROCEEXCLUSIONS")
    private String froceExclusions;  //交强拒赔
	@XStreamAlias("BUSINESSEXCLUSIONS")
    private String businessExclusions;  //商业拒赔
	@XStreamAlias("EXCLUSIONSREASON")
    private String exclusionsReason;  //拒赔原因
	@XStreamAlias("DOCUMENTSCOMPLETE")
    private String documentsComplete;  //单证齐全
	@XStreamAlias("ISSPECIALOPERATION")
    private String isSpecialOperation;  //是否有特种车操作证
	@XStreamAlias("ISBUSINESSQUALIFICATION")
    private String isBusinessQuelification;  //是否有营业车资格证
	@XStreamAlias("ISNORESPONSIBILITY")
    private String isNoResponsibility;  //是否无责代赔
	@XStreamAlias("OTHEREXCREASON")
    private String otherExcreason;  //其他拒赔原因
	@XStreamAlias("STIPOLICYNO")
    private String stIpolicyNo;//交强险保单号
    @XStreamAlias("STINSURCOM")
    private String stInsurCom;//交强承保机构
    @XStreamAlias("SYPOLICYNO")
    private String syPolicyNo;//商业险保单号
    
    /*精友2代改造*/
	@XStreamAlias("CERTAINSCOMCODE")
    private String certainsComcode;  //定损机构
	@XStreamAlias("CERTAINSCOMNAME")
    private String certainsComname;  //定损机构名称
	@XStreamAlias("CERTAINSCODE")
    private String certainsCode;//定损人员编码
    @XStreamAlias("CERTAINSNAME")
    private String certainsName;//定损人员名称
    
    /*自助理赔*/
	@XStreamAlias("CASETIME")
    private String caseTime;  //定损时间
    
	@XStreamAlias("COLLISIONLIST")
	private List<CollisionInfoVo> collisionInfoVo;
	@XStreamAlias("PARTLIST")
	private List<DefLossPartInfoVo> defLossPartInfoVo;//换件信息
	@XStreamAlias("REPAIRLIST")
	private List<DefLossRepairInfoVo> defLossRepairInfoVo;//修理信息
	@XStreamAlias("OUTSIDEREPAIRTLIST")
	private List<DefLossOutsideRepairtInfoVo> defLossOutsideRepairtInfoVo;//外修信息
	@XStreamAlias("EVALREPAIRSUMLIST")
	private List<DefLossEvalRepairSumInfoVo> evalRepairSumInfoVo;//修理合计
	@XStreamAlias("ASSISTLIST")
	private List<DefLossAssistInfoVo> defLossAssistInfoVo;//辅料合计
	@XStreamAlias("FEELIST")
	private List<DefLossFeeInfoVo> defLossFeeInfoVo;//费用信息
	public String getCertainsId() {
		return certainsId;
	}
	public void setCertainsId(String certainsId) {
		this.certainsId = certainsId;
	}
	public String getIsObject() {
		return isObject;
	}
	public void setIsObject(String isObject) {
		this.isObject = isObject;
	}
	public String getOwern() {
		return owern;
	}
	public void setOwern(String owern) {
		this.owern = owern;
	}
	public String getEnrollDate() {
		return enrollDate;
	}
	public void setEnrollDate(String enrollDate) {
		this.enrollDate = enrollDate;
	}
	public String getCarKindCode() {
		return carKindCode;
	}
	public void setCarKindCode(String carKindCode) {
		this.carKindCode = carKindCode;
	}
	public String getFrameNo() {
		return frameNo;
	}
	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getLicenseType() {
		return licenseType;
	}
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getVehicleModleCode() {
		return vehicleModleCode;
	}
	public void setVehicleModleCode(String vehicleModleCode) {
		this.vehicleModleCode = vehicleModleCode;
	}
	public String getVehicleModleName() {
		return vehicleModleName;
	}
	public void setVehicleModleName(String vehicleModleName) {
		this.vehicleModleName = vehicleModleName;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getKindCode() {
		return kindCode;
	}
	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}
	public String getAssessType() {
		return assessType;
	}
	public void setAssessType(String assessType) {
		this.assessType = assessType;
	}
	public String getAssessCarType() {
		return assessCarType;
	}
	public void setAssessCarType(String assessCarType) {
		this.assessCarType = assessCarType;
	}
	public String getRepairName() {
		return repairName;
	}
	public void setRepairName(String repairName) {
		this.repairName = repairName;
	}
	public String getRepairType() {
		return repairType;
	}
	public void setRepairType(String repairType) {
		this.repairType = repairType;
	}
	public String getRepairPhone() {
		return repairPhone;
	}
	public void setRepairPhone(String repairPhone) {
		this.repairPhone = repairPhone;
	}
	public String getDegreeDamage() {
		return degreeDamage;
	}
	public void setDegreeDamage(String degreeDamage) {
		this.degreeDamage = degreeDamage;
	}
	public String getExcessType() {
		return excessType;
	}
	public void setExcessType(String excessType) {
		this.excessType = excessType;
	}
	public String getSumFitsFee() {
		return sumFitsFee;
	}
	public void setSumFitsFee(String sumFitsFee) {
		this.sumFitsFee = sumFitsFee;
	}
	public String getAccessorFee() {
		return accessorFee;
	}
	public void setAccessorFee(String accessorFee) {
		this.accessorFee = accessorFee;
	}
	public String getSumrepairFee() {
		return sumrepairFee;
	}
	public void setSumrepairFee(String sumrepairFee) {
		this.sumrepairFee = sumrepairFee;
	}
	public String getSumSalvageFee() {
		return sumSalvageFee;
	}
	public void setSumSalvageFee(String sumSalvageFee) {
		this.sumSalvageFee = sumSalvageFee;
	}
	public String getCeraMount() {
		return ceraMount;
	}
	public void setCeraMount(String ceraMount) {
		this.ceraMount = ceraMount;
	}
	public String getAssistFee() {
		return assistFee;
	}
	public void setAssistFee(String assistFee) {
		this.assistFee = assistFee;
	}
	public String getVirtualValue() {
		return virtualValue;
	}
	public void setVirtualValue(String virtualValue) {
		this.virtualValue = virtualValue;
	}
	public String getVerSumFitsFee() {
		return verSumFitsFee;
	}
	public void setVerSumFitsFee(String verSumFitsFee) {
		this.verSumFitsFee = verSumFitsFee;
	}
	public String getVerAccessorFee() {
		return verAccessorFee;
	}
	public void setVerAccessorFee(String verAccessorFee) {
		this.verAccessorFee = verAccessorFee;
	}
	public String getVerSumRepairFee() {
		return verSumRepairFee;
	}
	public void setVerSumRepairFee(String verSumRepairFee) {
		this.verSumRepairFee = verSumRepairFee;
	}
	public String getVerSumSalvageFee() {
		return verSumSalvageFee;
	}
	public void setVerSumSalvageFee(String verSumSalvageFee) {
		this.verSumSalvageFee = verSumSalvageFee;
	}
	public String getSumVeriAmount() {
		return sumVeriAmount;
	}
	public void setSumVeriAmount(String sumVeriAmount) {
		this.sumVeriAmount = sumVeriAmount;
	}
	public String getLossOpinion() {
		return lossOpinion;
	}
	public void setLossOpinion(String lossOpinion) {
		this.lossOpinion = lossOpinion;
	}
	public Double getSumFeeAmount() {
		return sumFeeAmount;
	}
	public void setSumFeeAmount(Double sumFeeAmount) {
		this.sumFeeAmount = sumFeeAmount;
	}
	public String getFroceExclusions() {
		return froceExclusions;
	}
	public void setFroceExclusions(String froceExclusions) {
		this.froceExclusions = froceExclusions;
	}
	public String getBusinessExclusions() {
		return businessExclusions;
	}
	public void setBusinessExclusions(String businessExclusions) {
		this.businessExclusions = businessExclusions;
	}
	public String getExclusionsReason() {
		return exclusionsReason;
	}
	public void setExclusionsReason(String exclusionsReason) {
		this.exclusionsReason = exclusionsReason;
	}
	public String getDocumentsComplete() {
		return documentsComplete;
	}
	public void setDocumentsComplete(String documentsComplete) {
		this.documentsComplete = documentsComplete;
	}
	public String getIsSpecialOperation() {
		return isSpecialOperation;
	}
	public void setIsSpecialOperation(String isSpecialOperation) {
		this.isSpecialOperation = isSpecialOperation;
	}
	public String getIsBusinessQuelification() {
		return isBusinessQuelification;
	}
	public void setIsBusinessQuelification(String isBusinessQuelification) {
		this.isBusinessQuelification = isBusinessQuelification;
	}
	public String getIsNoResponsibility() {
		return isNoResponsibility;
	}
	public void setIsNoResponsibility(String isNoResponsibility) {
		this.isNoResponsibility = isNoResponsibility;
	}
	public String getOtherExcreason() {
		return otherExcreason;
	}
	public void setOtherExcreason(String otherExcreason) {
		this.otherExcreason = otherExcreason;
	}
	public String getStIpolicyNo() {
		return stIpolicyNo;
	}
	public void setStIpolicyNo(String stIpolicyNo) {
		this.stIpolicyNo = stIpolicyNo;
	}
	public String getStInsurCom() {
		return stInsurCom;
	}
	public void setStInsurCom(String stInsurCom) {
		this.stInsurCom = stInsurCom;
	}
	public String getCertainsComcode() {
		return certainsComcode;
	}
	public void setCertainsComcode(String certainsComcode) {
		this.certainsComcode = certainsComcode;
	}
	public String getCertainsComname() {
		return certainsComname;
	}
	public void setCertainsComname(String certainsComname) {
		this.certainsComname = certainsComname;
	}
	public String getCertainsCode() {
		return certainsCode;
	}
	public void setCertainsCode(String certainsCode) {
		this.certainsCode = certainsCode;
	}
	public String getCertainsName() {
		return certainsName;
	}
	public void setCertainsName(String certainsName) {
		this.certainsName = certainsName;
	}
	public List<CollisionInfoVo> getCollisionInfoVo() {
		return collisionInfoVo;
	}
	public void setCollisionInfoVo(List<CollisionInfoVo> collisionInfoVo) {
		this.collisionInfoVo = collisionInfoVo;
	}
	public List<DefLossPartInfoVo> getDefLossPartInfoVo() {
		return defLossPartInfoVo;
	}
	public void setDefLossPartInfoVo(List<DefLossPartInfoVo> defLossPartInfoVo) {
		this.defLossPartInfoVo = defLossPartInfoVo;
	}
	public List<DefLossRepairInfoVo> getDefLossRepairInfoVo() {
		return defLossRepairInfoVo;
	}
	public void setDefLossRepairInfoVo(List<DefLossRepairInfoVo> defLossRepairInfoVo) {
		this.defLossRepairInfoVo = defLossRepairInfoVo;
	}
	public List<DefLossOutsideRepairtInfoVo> getDefLossOutsideRepairtInfoVo() {
		return defLossOutsideRepairtInfoVo;
	}
	public void setDefLossOutsideRepairtInfoVo(
			List<DefLossOutsideRepairtInfoVo> defLossOutsideRepairtInfoVo) {
		this.defLossOutsideRepairtInfoVo = defLossOutsideRepairtInfoVo;
	}
	public List<DefLossEvalRepairSumInfoVo> getEvalRepairSumInfoVo() {
		return evalRepairSumInfoVo;
	}
	public void setEvalRepairSumInfoVo(
			List<DefLossEvalRepairSumInfoVo> evalRepairSumInfoVo) {
		this.evalRepairSumInfoVo = evalRepairSumInfoVo;
	}
	public List<DefLossAssistInfoVo> getDefLossAssistInfoVo() {
		return defLossAssistInfoVo;
	}
	public void setDefLossAssistInfoVo(List<DefLossAssistInfoVo> defLossAssistInfoVo) {
		this.defLossAssistInfoVo = defLossAssistInfoVo;
	}
	public List<DefLossFeeInfoVo> getDefLossFeeInfoVo() {
		return defLossFeeInfoVo;
	}
	public void setDefLossFeeInfoVo(List<DefLossFeeInfoVo> defLossFeeInfoVo) {
		this.defLossFeeInfoVo = defLossFeeInfoVo;
	}
	public String getSyPolicyNo() {
		return syPolicyNo;
	}
	public void setSyPolicyNo(String syPolicyNo) {
		this.syPolicyNo = syPolicyNo;
	}
	public String getCaseTime() {
		return caseTime;
	}
	public void setCaseTime(String caseTime) {
		this.caseTime = caseTime;
	}
	
	

}
