package ins.sino.claimcar.other.vo;

import java.math.BigDecimal;
import java.util.Date;


/**
 * Custom VO class of PO PrpLAssessor
 */ 
public class PrpLAssessorVo extends PrpLAssessorVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	private Date endCaseTime;
	private String comCode;
	private BigDecimal assessorFee;
	private BigDecimal veriLoss;
	private Long intermId;
	private String intermNameDetail;
	private Integer sumImgs;
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	public Date getEndCaseTime() {
		return endCaseTime;
	}
	public void setEndCaseTime(Date endCaseTime) {
		this.endCaseTime = endCaseTime;
	}
	public BigDecimal getAssessorFee() {
		return assessorFee;
	}
	public void setAssessorFee(BigDecimal assessorFee) {
		this.assessorFee = assessorFee;
	}
	public BigDecimal getVeriLoss() {
		return veriLoss;
	}
	public void setVeriLoss(BigDecimal veriLoss) {
		this.veriLoss = veriLoss;
	}
	public Long getIntermId() {
		return intermId;
	}
	public void setIntermId(Long intermId) {
		this.intermId = intermId;
	}
	public String getIntermNameDetail() {
		return intermNameDetail;
	}
	public void setIntermNameDetail(String intermNameDetail) {
		this.intermNameDetail = intermNameDetail;
	}
	public Integer getSumImgs() {
		return sumImgs;
	}
	public void setSumImgs(Integer sumImgs) {
		this.sumImgs = sumImgs;
	}
	
}
