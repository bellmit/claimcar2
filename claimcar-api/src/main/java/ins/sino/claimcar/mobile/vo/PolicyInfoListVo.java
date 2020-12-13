package ins.sino.claimcar.mobile.vo;


import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("POLICYINFO")
public class PolicyInfoListVo  implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/*
	 * 保单号
	 */
	@XStreamAlias("POLICYNO")
	private String policyNo;
	
	/*
	 * 车牌号
	 */
	@XStreamAlias("LICENSENO")
	private String licenseNo;
	
	/*
	 * 车架号
	 */
	@XStreamAlias("FRAMENO")
	private String frameNo;
	
	/*
	 * 发动机号
	 */
	@XStreamAlias("ENGINENO")
	private String engineNo;
	
	/*
	 * 车型
	 */
	@XStreamAlias("MODELCODE")
	private String modelCode;
	
	/*
	 * 被保险人名称
	 */
	@XStreamAlias("INUREDNAME")
	private String inuredName;
	
	/*
	 * 起保日期
	 */
	@XStreamAlias("STARTDATE")
	private String startDate;
	
	/*
	 * 终保日期
	 */
	@XStreamAlias("ENDDATE")
	private String endDate;
	
	/*
	 * 保单类别
	 */
	@XStreamAlias("CLAIMTYPE")
	private String claimType;
	
	/*
	 * 是否VIP
	 */
	@XStreamAlias("ISVIP")
	private String isVip;
	/*
	 * 历次出险次数
	 */
	@XStreamAlias("DAMAGECOUNT")
	private String damageCount;
	
	/*
	 * 承保机构代码
	 */
	@XStreamAlias("CompanyCode")
	private String companyCode;
	/*
	 * 承保机构名称
	 */
	@XStreamAlias("CompanyName")
	private String companyName;
	
	/*
	 * 总保额
	 */
	@XStreamAlias("TotalInsSum")
	private String totalInsSum;
	/*
	 * 业务渠道代码
	 */
	@XStreamAlias("ChannelCode")
	private String channelCode;
	/*
	 * 业务渠道名称
	 */
	@XStreamAlias("ChannelName")
	private String channelName;
	/*
	 * 出单日期
	 */
	@XStreamAlias("BillDate")
	private String billDate;
	/*
	 * 投保日期
	 */
	@XStreamAlias("PolicyHoldDate")
	private String policyHoldDate;
	/*
	 * 车辆所有人
	 */
	@XStreamAlias("CarOwner")
	private String carOwner;
	/*
	 * 初次登记年月
	 */
	@XStreamAlias("EnrolDate")
	private String enrolDate;
	/*
	 * 标的信息
	 */
	@XStreamAlias("KINDLIST")
	private List<KindInfoListVo> kindList;
	
	/*
	 * 特别约定
	 */
	@XStreamAlias("ENGAGELIST")
	private List<EngageInfoListVo> engageList;
	
	/*
	 * 批单信息
	 */
	@XStreamAlias("ENDORLIST")
	private List<EndorInfoListVo> endorlist;
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getFrameNo() {
		return frameNo;
	}
	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	public String getInuredName() {
		return inuredName;
	}
	public void setInuredName(String inuredName) {
		this.inuredName = inuredName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	
	public String getIsVip() {
		return isVip;
	}
	public void setIsVip(String isVip) {
		this.isVip = isVip;
	}
	public String getDamageCount() {
		return damageCount;
	}
	public void setDamageCount(String damageCount) {
		this.damageCount = damageCount;
	}
	
	
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getTotalInsSum() {
		return totalInsSum;
	}
	public void setTotalInsSum(String totalInsSum) {
		this.totalInsSum = totalInsSum;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getPolicyHoldDate() {
		return policyHoldDate;
	}
	public void setPolicyHoldDate(String policyHoldDate) {
		this.policyHoldDate = policyHoldDate;
	}
	public String getCarOwner() {
		return carOwner;
	}
	public void setCarOwner(String carOwner) {
		this.carOwner = carOwner;
	}
	public String getEnrolDate() {
		return enrolDate;
	}
	public void setEnrolDate(String enrolDate) {
		this.enrolDate = enrolDate;
	}
	public List<KindInfoListVo> getKindList() {
		return kindList;
	}
	public void setKindList(List<KindInfoListVo> kindList) {
		this.kindList = kindList;
	}
	public List<EngageInfoListVo> getEngageList() {
		return engageList;
	}
	public void setEngageList(List<EngageInfoListVo> engageList) {
		this.engageList = engageList;
	}
	public List<EndorInfoListVo> getEndorlist() {
		return endorlist;
	}
	public void setEndorlist(List<EndorInfoListVo> endorlist) {
		this.endorlist = endorlist;
	}
	
}
