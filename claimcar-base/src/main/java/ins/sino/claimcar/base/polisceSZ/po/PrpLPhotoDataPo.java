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
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPLPHOTODATAPO_PK", allocationSize = 10)
@Table(name = "PRPLPHOTODATAPO")
public class PrpLPhotoDataPo implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;
	private PrpLAccidentResInfoPo prpLAccidentResInfoPo;
	private String photoType;//照片类型
	private String photoName;//照片名称
	private String photoUrl;//照片URL
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
	@Column(name = "PHOTOTYPE", length=50)
	public String getPhotoType() {
		return photoType;
	}
	public void setPhotoType(String photoType) {
		this.photoType = photoType;
	}
	@Column(name = "PHOTONAME", length=50)
	public String getPhotoName() {
		return photoName;
	}
	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}
	@Column(name = "PHOTOURL", length=200)
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
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
