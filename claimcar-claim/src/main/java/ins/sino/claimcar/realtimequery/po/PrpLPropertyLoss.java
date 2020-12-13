package ins.sino.claimcar.realtimequery.po;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPLPROPERTYLOSS_PK", allocationSize = 10)
@Table(name = "PRPLPROPERTYLOSS")
public class PrpLPropertyLoss implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String protectName;  //损失财产名称
	private String protectProperty;  //财产属性
	private BigDecimal underDefLoss;  //核损金额（财产）
	private Long upperId;  //维护一个父级关系表id
	private String reportPhoneNo; //电话号码
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")
	@Column(name = "ID", unique = true, nullable = false, precision=13, scale=0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "PROTECTNAME", nullable = false, length=100)
	public String getProtectName() {
		return protectName;
	}
	public void setProtectName(String protectName) {
		this.protectName = protectName;
	}
	@Column(name = "PROTECTPROPERTY", nullable = false, length=2)
	public String getProtectProperty() {
		return protectProperty;
	}
	public void setProtectProperty(String protectProperty) {
		this.protectProperty = protectProperty;
	}
	@Column(name = "UNDERDEFLOSS", nullable = false, length=14)
	public BigDecimal getUnderDefLoss() {
		return underDefLoss;
	}
	public void setUnderDefLoss(BigDecimal underDefLoss) {
		this.underDefLoss = underDefLoss;
	}
	
	@Column(name = "UPPERID", nullable = false, length=13)
	public Long getUpperId() {
		return upperId;
	}
	
	public void setUpperId(Long upperId) {
		this.upperId = upperId;
	}
	@Column(name = "REPORTPHONENO", nullable = false, length=20)
	public String getReportPhoneNo() {
		return reportPhoneNo;
	}
	public void setReportPhoneNo(String reportPhoneNo) {
		this.reportPhoneNo = reportPhoneNo;
	}
	
	
	
}
