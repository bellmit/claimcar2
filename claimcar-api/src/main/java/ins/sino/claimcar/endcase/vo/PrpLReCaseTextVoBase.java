package ins.sino.claimcar.endcase.vo;


//Generated by Hibernate Tools 3.2.5 (sinosoft version), Don't modify!
import java.util.Date;


public class PrpLReCaseTextVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String operatorName;
	private String comName;
	private Date inputTime;
	private String checkOpinion;
	private String checkStatus;
	private String openReasonDetail;
	
	protected PrpLReCaseTextVoBase(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public Date getInputTime() {
		return inputTime;
	}

	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}

	public String getCheckOpinion() {
		return checkOpinion;
	}

	public void setCheckOpinion(String checkOpinion) {
		this.checkOpinion = checkOpinion;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getOpenReasonDetail() {
		return openReasonDetail;
	}

	public void setOpenReasonDetail(String openReasonDetail) {
		this.openReasonDetail = openReasonDetail;
	}
	
	
}