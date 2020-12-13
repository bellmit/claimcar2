package ins.sino.claimcar.realtimequery.po;

import java.io.Serializable;
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
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPLCASUALTYINFOR_PK", allocationSize = 10)
@Table(name = "PRPLCASUALTYINFOR")
public class PrpLCasualtyInfor implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String personProperty;  //人员属性	
	private String personName;  //伤亡人员姓名	
	private String csCertiType;  //伤亡证件类型	
	private String csCertiCode;  //伤亡人员证件号码	
	private String csMedicalType;  //伤亡人员医疗类型	
	private String medicalType;  //伤情类别代码	
	private List<PrpLInjuredDetails> prpLInjuredDetailsList;  //伤亡详情	
	private Long upperId;  //维护一个父级关系表id
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")
	@Column(name = "ID", unique = true, nullable = false, precision=13, scale=0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "PERSONPROPERTY", nullable = false, length=1)
	public String getPersonProperty() {
		return personProperty;
	}
	public void setPersonProperty(String personProperty) {
		this.personProperty = personProperty;
	}
	@Column(name = "PERSONNAME", nullable = false, length=256)
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	@Column(name = "CSCERTITYPE", nullable = false, length=4)
	public String getCsCertiType() {
		return csCertiType;
	}
	public void setCsCertiType(String csCertiType) {
		this.csCertiType = csCertiType;
	}
	@Column(name = "CSCERTICODE", nullable = false, length=20)
	public String getCsCertiCode() {
		return csCertiCode;
	}
	public void setCsCertiCode(String csCertiCode) {
		this.csCertiCode = csCertiCode;
	}
	@Column(name = "CSMEDICALTYPE", nullable = false, length=1)
	public String getCsMedicalType() {
		return csMedicalType;
	}
	public void setCsMedicalType(String csMedicalType) {
		this.csMedicalType = csMedicalType;
	}
	@Column(name = "MEDICALTYPE", nullable = false, length=2)
	public String getMedicalType() {
		return medicalType;
	}
	public void setMedicalType(String medicalType) {
		this.medicalType = medicalType;
	}
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="prpLCasualtyInfor")
	public List<PrpLInjuredDetails> getPrpLInjuredDetailsList() {
		return prpLInjuredDetailsList;
	}
	public void setPrpLInjuredDetailsList(
			List<PrpLInjuredDetails> prpLInjuredDetailsList) {
		this.prpLInjuredDetailsList = prpLInjuredDetailsList;
	}
	@Column(name = "UPPERID", nullable = false,precision=13, scale=0)
	public Long getUpperId() {
		return upperId;
	}
	public void setUpperId(Long upperId) {
		this.upperId = upperId;
	}
	
	
	
}
