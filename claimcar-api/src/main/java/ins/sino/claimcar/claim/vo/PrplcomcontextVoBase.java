package ins.sino.claimcar.claim.vo;

// Generated by Hibernate Tools 3.2.5 (sinosoft version), Don't modify!
import java.util.Date;


/**
 * Vo Base Class of PO Prplcomcontext
 */ 
public class PrplcomcontextVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String compensateNo;
	private String flag;
	private String flagContext;
	private String causes;
	private String nodeSign;
	private String createUser;
	private Date createTime;

	protected PrplcomcontextVoBase() {

	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompensateNo() {
		return this.compensateNo;
	}

	public void setCompensateNo(String compensateNo) {
		this.compensateNo = compensateNo;
	}

	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getFlagContext() {
		return this.flagContext;
	}

	public void setFlagContext(String flagContext) {
		this.flagContext = flagContext;
	}

	public String getCauses() {
		return this.causes;
	}

	public void setCauses(String causes) {
		this.causes = causes;
	}

	public String getNodeSign() {
		return this.nodeSign;
	}

	public void setNodeSign(String nodeSign) {
		this.nodeSign = nodeSign;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}