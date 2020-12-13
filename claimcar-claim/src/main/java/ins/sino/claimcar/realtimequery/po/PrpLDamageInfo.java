package ins.sino.claimcar.realtimequery.po;

import java.io.Serializable;

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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPLDAMAGEINFO_PK", allocationSize = 10)
@Table(name = "PRPLDAMAGEINFO")
public class PrpLDamageInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String lossPartCode;
	private PrpLVehicleInfo prpLVehicleInfo;
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
	@Column(name = "LOSSPARTCODE", nullable = false, length=200)
	public String getLossPartCode() {
		return lossPartCode;
	}
	public void setLossPartCode(String lossPartCode) {
		this.lossPartCode = lossPartCode;
	}
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VEHICLEINFOID", nullable = false)
	public PrpLVehicleInfo getPrpLVehicleInfo() {
		return prpLVehicleInfo;
	}
	public void setPrpLVehicleInfo(PrpLVehicleInfo prpLVehicleInfo) {
		this.prpLVehicleInfo = prpLVehicleInfo;
	}
	@Column(name = "UPPERID", nullable = false, length=13)
	public Long getUpperId() {
		return upperId;
	}
	public void setUpperId(Long upperId) {
		this.upperId = upperId;
	}
	
	

}
