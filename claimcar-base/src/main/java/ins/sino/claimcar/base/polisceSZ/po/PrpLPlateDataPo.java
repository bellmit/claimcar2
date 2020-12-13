package ins.sino.claimcar.base.polisceSZ.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPLPLATEDATAPO_PK", allocationSize = 10)
@Table(name = "PRPLPLATEDATAPO")
public class PrpLPlateDataPo implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;
	private PrpLAccidentResInfoPo prpLAccidentResInfoPo;
	private String vinNo;//车架号
	private String plateType;//车牌类型
	private String plateNo;//车牌号
	private String driverName;//驾驶人姓名
	private String driverPhone;//驾驶人电话
	private String dutyProportion;//责任占比
	private String isNorDriving;//车辆是否能够行驶
	private String liabilityInsuranceName;//交强险承保公司名称
	private String liabilityInsuranceNo;//交强险保单号
	private Date createTime;
	private Date updateTime;
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")
	@Column(name = "ID", unique = true, nullable = false, scale=0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ACCIDENTID", nullable = false)
	public PrpLAccidentResInfoPo getPrpLAccidentResInfoPo() {
		return prpLAccidentResInfoPo;
	}
	public void setPrpLAccidentResInfoPo(PrpLAccidentResInfoPo prpLAccidentResInfoPo) {
		this.prpLAccidentResInfoPo = prpLAccidentResInfoPo;
	}
	@Column(name = "VINNO", length=100)
	public String getVinNo() {
		return vinNo;
	}
	public void setVinNo(String vinNo) {
		this.vinNo = vinNo;
	}
	@Column(name = "PLATETYPE", length=10)
	public String getPlateType() {
		return plateType;
	}
	public void setPlateType(String plateType) {
		this.plateType = plateType;
	}
 	@Column(name = "PLATENO", length=50)
	public String getPlateNo() {
		return plateNo;
	}
	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}
	@Column(name = "DRIVERNAME", length=50)
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	@Column(name = "DRIVERPHONE", length=20)
	public String getDriverPhone() {
		return driverPhone;
	}
	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}
 	@Column(name = "DUTYPROPORTION", length=20)
	public String getDutyProportion() {
		return dutyProportion;
	}
	public void setDutyProportion(String dutyProportion) {
		this.dutyProportion = dutyProportion;
	}
	@Column(name = "ISNORDRIVING", length=10)
	public String getIsNorDriving() {
		return isNorDriving;
	}
	public void setIsNorDriving(String isNorDriving) {
		this.isNorDriving = isNorDriving;
	}
	@Column(name = "LIABILITYINSURANCENAME", length=500)
	public String getLiabilityInsuranceName() {
		return liabilityInsuranceName;
	}
	public void setLiabilityInsuranceName(String liabilityInsuranceName) {
		this.liabilityInsuranceName = liabilityInsuranceName;
	}
	@Column(name = "LIABILITYINSURANCENO", length=100)
	public String getLiabilityInsuranceNo() {
		return liabilityInsuranceNo;
	}
	public void setLiabilityInsuranceNo(String liabilityInsuranceNo) {
		this.liabilityInsuranceNo = liabilityInsuranceNo;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME", nullable = false, length = 7)
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATETIME", nullable = false, length = 7)
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
