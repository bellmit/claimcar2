package ins.sino.claimcar.ilog.certify;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("accidentResponsibility")
public class AccidentResponsibilityVo {
	@XStreamAlias("accidentVehicleNo")
    private String accidentVehicleNo;//事故车辆车牌号
	@XStreamAlias("accidentVehicleType")
    private String accidentVehicleType;//事故车辆类型
	@XStreamAlias("indemnityDuty")
    private String indemnityDuty;//事故责任类型
	@XStreamAlias("indemnityDutyRate")
    private String indemnityDutyRate;//事故责任比例
	@XStreamAlias("ciPolicyNo")
    private String ciPolicyNo;//交强险保单号
	@XStreamAlias("ciInsuredMechanism")
    private String ciInsuredMechanism;//交强险保险公司
	@XStreamAlias("noDutyPayFlag")
    private String noDutyPayFlag;//是否无责代赔
	public String getAccidentVehicleNo() {
		return accidentVehicleNo;
	}
	public void setAccidentVehicleNo(String accidentVehicleNo) {
		this.accidentVehicleNo = accidentVehicleNo;
	}
	public String getAccidentVehicleType() {
		return accidentVehicleType;
	}
	public void setAccidentVehicleType(String accidentVehicleType) {
		this.accidentVehicleType = accidentVehicleType;
	}
	public String getIndemnityDuty() {
		return indemnityDuty;
	}
	public void setIndemnityDuty(String indemnityDuty) {
		this.indemnityDuty = indemnityDuty;
	}
	public String getIndemnityDutyRate() {
		return indemnityDutyRate;
	}
	public void setIndemnityDutyRate(String indemnityDutyRate) {
		this.indemnityDutyRate = indemnityDutyRate;
	}
	public String getCiPolicyNo() {
		return ciPolicyNo;
	}
	public void setCiPolicyNo(String ciPolicyNo) {
		this.ciPolicyNo = ciPolicyNo;
	}
	public String getCiInsuredMechanism() {
		return ciInsuredMechanism;
	}
	public void setCiInsuredMechanism(String ciInsuredMechanism) {
		this.ciInsuredMechanism = ciInsuredMechanism;
	}
	public String getNoDutyPayFlag() {
		return noDutyPayFlag;
	}
	public void setNoDutyPayFlag(String noDutyPayFlag) {
		this.noDutyPayFlag = noDutyPayFlag;
	}
	
	
}
