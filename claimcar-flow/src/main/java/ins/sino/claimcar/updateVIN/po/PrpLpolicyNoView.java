package ins.sino.claimcar.updateVIN.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "VIEW_POLICYNOANDPOLICYNOLINK")
public class PrpLpolicyNoView implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String registNo;
	private String policyNo;
	private String policyNoLink;//关联保单号
	private String licenseNo;//标的车车牌号
	private String insuredname;//被保险人
	private String frameNo;//标的车车架号
	@Id
	@Column(name = "REGISTNO", unique = true, nullable = false, length = 25)
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	@Column(name = "POLICYNO", length = 25)
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	@Column(name = "POLICYNOLINK", length = 25)
	public String getPolicyNoLink() {
		return policyNoLink;
	}
	public void setPolicyNoLink(String policyNoLink) {
		this.policyNoLink = policyNoLink;
	}
	@Column(name = "LICENSENO", length = 20)
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	@Column(name = "INSUREDNAME", length = 320)
	public String getInsuredname() {
		return insuredname;
	}
	public void setInsuredname(String insuredname) {
		this.insuredname = insuredname;
	}
	@Column(name = "FRAMENO", length = 30)
	public String getFrameNo() {
		return frameNo;
	}
	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}
	
	
}
