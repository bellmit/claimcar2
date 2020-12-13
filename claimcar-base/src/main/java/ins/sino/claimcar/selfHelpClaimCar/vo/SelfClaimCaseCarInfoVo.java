package ins.sino.claimcar.selfHelpClaimCar.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CASECARINFO")
public class SelfClaimCaseCarInfoVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("CASECARTYPE")
	private String caseCarType;
	
	@XStreamAlias("CASECARNO")
	private String caseCarNo;

	@XStreamAlias("DUTYTYPE")
	private String dutyType;

	@XStreamAlias("CAROWNNAME")
	private String carOwnName;
	
	@XStreamAlias("CAROWNPHONE")
	private String carOwnPhone;

	@XStreamAlias("INSCOMCODE")
	private String insComCode;	//DHIC

	@XStreamAlias("INSCOMPANY")
	private String insCompany;	//鼎和财产保险股份有限公司
	
	@XStreamAlias("FRAMENO")
	private String frameNo;

	public String getCaseCarType() {
		return caseCarType;
	}

	public void setCaseCarType(String caseCarType) {
		this.caseCarType = caseCarType;
	}

	public String getCaseCarNo() {
		return caseCarNo;
	}

	public void setCaseCarNo(String caseCarNo) {
		this.caseCarNo = caseCarNo;
	}

	public String getDutyType() {
		return dutyType;
	}

	public void setDutyType(String dutyType) {
		this.dutyType = dutyType;
	}

	public String getCarOwnName() {
		return carOwnName;
	}

	public void setCarOwnName(String carOwnName) {
		this.carOwnName = carOwnName;
	}

	public String getCarOwnPhone() {
		return carOwnPhone;
	}

	public void setCarOwnPhone(String carOwnPhone) {
		this.carOwnPhone = carOwnPhone;
	}

	public String getInsComCode() {
		return insComCode;
	}

	public void setInsComCode(String insComCode) {
		this.insComCode = insComCode;
	}

	public String getInsCompany() {
		return insCompany;
	}

	public void setInsCompany(String insCompany) {
		this.insCompany = insCompany;
	}

	public String getFrameNo() {
		return frameNo;
	}

	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}
	
	
}
