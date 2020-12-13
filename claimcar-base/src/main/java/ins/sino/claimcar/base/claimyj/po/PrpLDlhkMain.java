package ins.sino.claimcar.base.claimyj.po;

// Generated by Hibernate Tools 3.2.5 (sinosoft version), Don't modify!
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * POJO Class PrpLDlhkMain
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPLDLCHKMAIN_PK", allocationSize = 10)
@Table(name = "PRPLDLCHKMAIN")
public class PrpLDlhkMain implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private List<PrpLDlchkInfo> prpLDlchkInfos;
	private String notificationNo;
	private String topActualId;
	private String actualId;
	private String assessNo;
	private String licenseNo;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")
	@Column(name = "ID", unique = true, nullable = false, precision=13, scale=0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "prpLDlhkMain")
	public List<PrpLDlchkInfo> getPrpLDlchkInfos() {
		return prpLDlchkInfos;
	}
	public void setPrpLDlchkInfos(List<PrpLDlchkInfo> prpLDlchkInfos) {
		this.prpLDlchkInfos = prpLDlchkInfos;
	}
	
	@Column(name = "NOTIFICATIONNO", nullable = false, length=30)
	public String getNotificationNo() {
		return notificationNo;
	}

	public void setNotificationNo(String notificationNo) {
		this.notificationNo = notificationNo;
	}
	
	@Column(name = "TOPACTUALID", nullable = false, length=50)
	public String getTopActualId() {
		return topActualId;
	}
	public void setTopActualId(String topActualId) {
		this.topActualId = topActualId;
	}
	
	@Column(name = "ACTUALID", nullable = false, length=50)
	public String getActualId() {
		return actualId;
	}
	public void setActualId(String actualId) {
		this.actualId = actualId;
	}
	
	@Column(name = "ASSESSNO", nullable = false, length=50)
	public String getAssessNo() {
		return assessNo;
	}
	public void setAssessNo(String assessNo) {
		this.assessNo = assessNo;
	}
	
	@Column(name = "LICENSENO", nullable = false, length=10)
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	
	
}