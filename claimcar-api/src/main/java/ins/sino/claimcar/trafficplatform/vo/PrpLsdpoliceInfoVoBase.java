package ins.sino.claimcar.trafficplatform.vo;

// Generated by Hibernate Tools 3.2.5 (sinosoft version), Don't modify!
import java.util.Date;

/**
 * Vo Base Class of PO PrpLsdpoliceInfo
 */
public class PrpLsdpoliceInfoVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String registNo;
	private String claimsequenceNo;
	private String claimStage;
	private String warnMessage;
	private Date createTime;
	private Date updateTime;

	protected PrpLsdpoliceInfoVoBase() {

	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRegistNo() {
		return this.registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getClaimsequenceNo() {
		return this.claimsequenceNo;
	}

	public void setClaimsequenceNo(String claimsequenceNo) {
		this.claimsequenceNo = claimsequenceNo;
	}

	public String getClaimStage() {
		return this.claimStage;
	}

	public void setClaimStage(String claimStage) {
		this.claimStage = claimStage;
	}

	public String getWarnMessage() {
		return this.warnMessage;
	}

	public void setWarnMessage(String warnMessage) {
		this.warnMessage = warnMessage;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}