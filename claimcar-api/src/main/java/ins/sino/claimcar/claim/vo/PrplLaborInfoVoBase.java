package ins.sino.claimcar.claim.vo;

// Generated by Hibernate Tools 3.2.5 (sinosoft version), Don't modify!
import java.util.Date;

/**
 * Vo Base Class of PO PrplLaborInfo
 */
public class PrplLaborInfoVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String laborItem;
	private String laborName;
	private String laborPrice;
	private String laborCount;
	private String labortotalPrice;
	private String cePrice;
	private String ceCount;
	private String cetotalPrice;
	private String savingPrice;
	private String savingDescs;
	private Date createTime;
	private String createUser;

	protected PrplLaborInfoVoBase() {

	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLaborItem() {
		return this.laborItem;
	}

	public void setLaborItem(String laborItem) {
		this.laborItem = laborItem;
	}

	public String getLaborName() {
		return this.laborName;
	}

	public void setLaborName(String laborName) {
		this.laborName = laborName;
	}

	public String getLaborPrice() {
		return this.laborPrice;
	}

	public void setLaborPrice(String laborPrice) {
		this.laborPrice = laborPrice;
	}

	public String getLaborCount() {
		return this.laborCount;
	}

	public void setLaborCount(String laborCount) {
		this.laborCount = laborCount;
	}

	public String getLabortotalPrice() {
		return this.labortotalPrice;
	}

	public void setLabortotalPrice(String labortotalPrice) {
		this.labortotalPrice = labortotalPrice;
	}

	public String getCePrice() {
		return this.cePrice;
	}

	public void setCePrice(String cePrice) {
		this.cePrice = cePrice;
	}

	public String getCeCount() {
		return this.ceCount;
	}

	public void setCeCount(String ceCount) {
		this.ceCount = ceCount;
	}

	public String getCetotalPrice() {
		return this.cetotalPrice;
	}

	public void setCetotalPrice(String cetotalPrice) {
		this.cetotalPrice = cetotalPrice;
	}

	public String getSavingPrice() {
		return this.savingPrice;
	}

	public void setSavingPrice(String savingPrice) {
		this.savingPrice = savingPrice;
	}

	public String getSavingDescs() {
		return this.savingDescs;
	}

	public void setSavingDescs(String savingDescs) {
		this.savingDescs = savingDescs;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {

		this.createUser = createUser; 
	}  
}