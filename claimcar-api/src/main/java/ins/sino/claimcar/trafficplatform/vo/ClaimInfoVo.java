package ins.sino.claimcar.trafficplatform.vo;

import java.util.ArrayList;
import java.util.List;

public class ClaimInfoVo {
	private String claimNo1;//立案号
    private String insuranceCode;//保险公司代码  Y
	private String claimNo;//理赔编号 Y
	private String policyNo;//保单号  Y
	private String insuranceType;//险种类型  Y
	private String accidentNo;//事故编号  Y
	private String warningNo;//报案号  Y
	private String payAmout;//赔款总金额  Y
	private List<PlateDataVo> plateData=new ArrayList<PlateDataVo>();//车牌信息  Y
	private List<PayDataVo> payData=new ArrayList<PayDataVo>();//赔款信息   Y
	public String getInsuranceCode() {
		return insuranceCode;
	}
	public void setInsuranceCode(String insuranceCode) {
		this.insuranceCode = insuranceCode;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getInsuranceType() {
		return insuranceType;
	}
	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}
	public String getAccidentNo() {
		return accidentNo;
	}
	public void setAccidentNo(String accidentNo) {
		this.accidentNo = accidentNo;
	}
	public String getWarningNo() {
		return warningNo;
	}
	public void setWarningNo(String warningNo) {
		this.warningNo = warningNo;
	}
	public String getPayAmout() {
		return payAmout;
	}
	public void setPayAmout(String payAmout) {
		this.payAmout = payAmout;
	}
	public List<PlateDataVo> getPlateData() {
		return plateData;
	}
	public void setPlateData(List<PlateDataVo> plateData) {
		this.plateData = plateData;
	}
	public List<PayDataVo> getPayData() {
		return payData;
	}
	public void setPayData(List<PayDataVo> payData) {
		this.payData = payData;
	}
	public String getClaimNo1() {
		return claimNo1;
	}
	public void setClaimNo1(String claimNo1) {
		this.claimNo1 = claimNo1;
	}
	
    
}
