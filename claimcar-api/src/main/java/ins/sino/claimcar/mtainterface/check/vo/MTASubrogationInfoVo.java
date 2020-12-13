package ins.sino.claimcar.mtainterface.check.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("SUBROGATIONINFO")
public class MTASubrogationInfoVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("SUBROGATIONTYPE")
	private String subrogationType; //代位求偿类型

	@XStreamAlias("CREATEUSER")
	private String createUser; //操作人员

	@XStreamAlias("CREATETIME")
	private String createTime; //操作时间

	@XStreamAlias("SERIALNO")
	private String serialNo; //车辆序号
	
	@XStreamAlias("LINKERNAME")
	private String linkerName; //联系人姓名
	
	@XStreamAlias("LICENSENO")
	private String licenseNo; //车辆号牌号码

	@XStreamAlias("LICENSETYPE")
	private String licenseType; //车辆号牌种类

	@XStreamAlias("VINNO")
	private String vinNo; //vinNo
	
	@XStreamAlias("ENGINENO")
	private String engineNo; //车辆发动机号

	@XStreamAlias("BIINSURERCODE")
	private String biInsurerCode; //商业险承保公司

	@XStreamAlias("BIINSURERAREA")
	private String biInsurerArea; //商业险承保地区

	@XStreamAlias("CIINSURERCODE")
	private String ciInsurerCode; //交强险承保公司

	@XStreamAlias("CIINSURERAREA")
	private String ciInsurerArea; //交强险承保地区

	@XStreamAlias("NAME")
	private String name; //姓名/名称

	@XStreamAlias("UNITNAME")
	private String unitName; //单位名称

	@XStreamAlias("PHONE")
	private String phone; //联系电话

	@XStreamAlias("ADDRESS")
	private String address; //地址

	@XStreamAlias("ZIPNO")
	private String zipno; //邮政编码
	
	@XStreamAlias("IDENTIFYNUMBER")
	private String identifyNumber; //身份证号码

	@XStreamAlias("INSUREDINFO")
	private String insuredInfo; //投保保险情况

	@XStreamAlias("LAWLINKERNAME")
	private String LawLinkerName; //法定代表人姓名

	@XStreamAlias("UNITPHONE")
	private String unitPhone; //单位联系电话

	@XStreamAlias("OTHERINFO")
	private String otherInfo; //其他信息

	public String getSubrogationType() {
		return subrogationType;
	}

	public void setSubrogationType(String subrogationType) {
		this.subrogationType = subrogationType;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getLinkerName() {
		return linkerName;
	}

	public void setLinkerName(String linkerName) {
		this.linkerName = linkerName;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	public String getVinNo() {
		return vinNo;
	}

	public void setVinNo(String vinNo) {
		this.vinNo = vinNo;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getBiInsurerCode() {
		return biInsurerCode;
	}

	public void setBiInsurerCode(String biInsurerCode) {
		this.biInsurerCode = biInsurerCode;
	}

	public String getBiInsurerArea() {
		return biInsurerArea;
	}

	public void setBiInsurerArea(String biInsurerArea) {
		this.biInsurerArea = biInsurerArea;
	}

	public String getCiInsurerCode() {
		return ciInsurerCode;
	}

	public void setCiInsurerCode(String ciInsurerCode) {
		this.ciInsurerCode = ciInsurerCode;
	}

	public String getCiInsurerArea() {
		return ciInsurerArea;
	}

	public void setCiInsurerArea(String ciInsurerArea) {
		this.ciInsurerArea = ciInsurerArea;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipno() {
		return zipno;
	}

	public void setZipno(String zipno) {
		this.zipno = zipno;
	}

	public String getIdentifyNumber() {
		return identifyNumber;
	}

	public void setIdentifyNumber(String identifyNumber) {
		this.identifyNumber = identifyNumber;
	}

	public String getInsuredInfo() {
		return insuredInfo;
	}

	public void setInsuredInfo(String insuredInfo) {
		this.insuredInfo = insuredInfo;
	}

	public String getLawLinkerName() {
		return LawLinkerName;
	}

	public void setLawLinkerName(String lawLinkerName) {
		LawLinkerName = lawLinkerName;
	}

	public String getUnitPhone() {
		return unitPhone;
	}

	public void setUnitPhone(String unitPhone) {
		this.unitPhone = unitPhone;
	}

	public String getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}


}
