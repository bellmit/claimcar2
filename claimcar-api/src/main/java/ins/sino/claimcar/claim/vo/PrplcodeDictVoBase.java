package ins.sino.claimcar.claim.vo;

// Generated by Hibernate Tools 3.2.5 (sinosoft version), Don't modify!
import java.util.Date;


/**
 * Vo Base Class of PO PrplcodeDict
 */ 
public class PrplcodeDictVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String codeType;
	private String wcode;
	private String tcode;
	private String codeName;
	private String tcompanyName;
	private Date createTime;
	private String remark;

    protected PrplcodeDictVoBase() {
	
    }

    
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodeType() {
		return this.codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getWcode() {
		return this.wcode;
	}

	public void setWcode(String wcode) {
		this.wcode = wcode;
	}

	public String getTcode() {
		return this.tcode;
	}

	public void setTcode(String tcode) {
		this.tcode = tcode;
	}

	public String getCodeName() {
		return this.codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getTcompanyName() {
		return this.tcompanyName;
	}

	public void setTcompanyName(String tcompanyName) {
		this.tcompanyName = tcompanyName;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}