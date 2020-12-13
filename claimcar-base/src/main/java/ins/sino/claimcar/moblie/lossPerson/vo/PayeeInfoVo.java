
package ins.sino.claimcar.moblie.lossPerson.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 收款人信息
 * @author j2eel
 *
 */
@XStreamAlias("PAYEEINFO")
public class PayeeInfoVo implements Serializable {

	private static final long serialVersionUID = -1528493104586689963L;
    @XStreamAlias("PAYEENATURE")
    protected String payeeNature;
    @XStreamAlias("IDENTIFYTYPE")
    protected String identifyType;
    @XStreamAlias("PAYEETYPE")
    protected String payeeType;
    @XStreamAlias("NAME")
    protected String name;
    @XStreamAlias("PUBANDPRILOGO")
    protected String pubAndPrilogo;
    @XStreamAlias("IDENTIFYNUMBER")
    protected String identifyNumber;
    @XStreamAlias("ACCOUNTNO")
    protected String accountNo;
    @XStreamAlias("PROVINCECODE")
    protected String provinceCode;
    @XStreamAlias("CITYCODE")
    protected String cityCode;
    @XStreamAlias("BANKNAME")
    protected String bankName;
    @XStreamAlias("BRANCHNAME")
    protected String branchName;
    @XStreamAlias("BANKCODE")
    protected String bankCode;
    @XStreamAlias("TRANSFERMODE")
    protected String transferMode;
    @XStreamAlias("PHONE")
    protected String phone;
    @XStreamAlias("USE")
    protected String use;
    
	public String getPayeeNature() {
		return payeeNature;
	}
	public void setPayeeNature(String payeeNature) {
		this.payeeNature = payeeNature;
	}
	public String getIdentifyType() {
		return identifyType;
	}
	public void setIdentifyType(String identifyType) {
		this.identifyType = identifyType;
	}
	public String getPayeeType() {
		return payeeType;
	}
	public void setPayeeType(String payeeType) {
		this.payeeType = payeeType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPubAndPrilogo() {
		return pubAndPrilogo;
	}
	public void setPubAndPrilogo(String pubAndPrilogo) {
		this.pubAndPrilogo = pubAndPrilogo;
	}
	public String getIdentifyNumber() {
		return identifyNumber;
	}
	public void setIdentifyNumber(String identifyNumber) {
		this.identifyNumber = identifyNumber;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getTransferMode() {
		return transferMode;
	}
	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUse() {
		return use;
	}
	public void setUse(String use) {
		this.use = use;
	}


}


