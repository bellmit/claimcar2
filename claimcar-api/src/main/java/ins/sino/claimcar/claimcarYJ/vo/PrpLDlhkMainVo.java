package ins.sino.claimcar.claimcarYJ.vo;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("LDLHKMAIN")
public class PrpLDlhkMainVo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XStreamAlias("ID")
	private Long id;
	@XStreamAlias("PARTLIST")
	private List<PrpLDlchkInfoVo> prpLDlchkInfos;
	@XStreamAlias("NOTIFICATIONNO")
	private String notificationNo;
	@XStreamAlias("TOPACTUALID")
	private String topActualId;
	@XStreamAlias("ACTUALID")
	private String actualId;
	@XStreamAlias("ASSESSNO")
	private String assessNo;
	@XStreamAlias("LICENSENO")
	private String licenseNo;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	
	public List<PrpLDlchkInfoVo> getPrpLDlchkInfos() {
		return prpLDlchkInfos;
	}
	public void setPrpLDlchkInfos(List<PrpLDlchkInfoVo> prpLDlchkInfos) {
		this.prpLDlchkInfos = prpLDlchkInfos;
	}
	public String getNotificationNo() {
		return notificationNo;
	}
	public void setNotificationNo(String notificationNo) {
		this.notificationNo = notificationNo;
	}
	public String getTopActualId() {
		return topActualId;
	}
	public void setTopActualId(String topActualId) {
		this.topActualId = topActualId;
	}
	public String getActualId() {
		return actualId;
	}
	public void setActualId(String actualId) {
		this.actualId = actualId;
	}
	public String getAssessNo() {
		return assessNo;
	}
	public void setAssessNo(String assessNo) {
		this.assessNo = assessNo;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	
	
}
