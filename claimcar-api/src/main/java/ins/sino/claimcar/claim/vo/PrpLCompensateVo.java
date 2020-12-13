package ins.sino.claimcar.claim.vo;

import java.math.BigDecimal;

/**
 * Custom VO class of PO PrpLCompensate
 */
public class PrpLCompensateVo extends PrpLCompensateVoBase implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String insuredPhone;
	
	
	private String oppoCompensateNo;
    private  String rescueReport;
    
    private String inSideDeroFlagValue;
    
    private String inSideDeroPersonFlag;//是否人伤内部减损标志
    private BigDecimal personPisderoVerifyAmout;//人伤减损金额(审核)
	private BigDecimal carCisderoVerifyAmout;//车物减损金额(审核)
	private String fraudType;//欺诈类型
	private String impairmentType;//减损类型
	public String getOppoCompensateNo() {
		return oppoCompensateNo;
	}


	public void setOppoCompensateNo(String oppoCompensateNo) {
		this.oppoCompensateNo = oppoCompensateNo;
	}


	public String getRescueReport() {
		return rescueReport;
	}


	public void setRescueReport(String rescueReport) {
		this.rescueReport = rescueReport;
	}


    
    public String getinsuredPhone() {
        return insuredPhone;
    }


    
    public void setinsuredPhone(String insuredPhone) {
        this.insuredPhone = insuredPhone;
    }


	public String getInSideDeroFlagValue() {
		return inSideDeroFlagValue;
	}


	public void setInSideDeroFlagValue(String inSideDeroFlagValue) {
		this.inSideDeroFlagValue = inSideDeroFlagValue;
	}


	
	public String getInSideDeroPersonFlag() {
		return inSideDeroPersonFlag;
	}


	
	public void setInSideDeroPersonFlag(String inSideDeroPersonFlag) {
		this.inSideDeroPersonFlag = inSideDeroPersonFlag;
	}


	
	public BigDecimal getPersonPisderoVerifyAmout() {
		return personPisderoVerifyAmout;
	}


	
	public void setPersonPisderoVerifyAmout(BigDecimal personPisderoVerifyAmout) {
		this.personPisderoVerifyAmout = personPisderoVerifyAmout;
	}


	
	public BigDecimal getCarCisderoVerifyAmout() {
		return carCisderoVerifyAmout;
	}


	
	public void setCarCisderoVerifyAmout(BigDecimal carCisderoVerifyAmout) {
		this.carCisderoVerifyAmout = carCisderoVerifyAmout;
	}


	public String getFraudType() {
		return fraudType;
	}


	public void setFraudType(String fraudType) {
		this.fraudType = fraudType;
	}


	public String getImpairmentType() {
		return impairmentType;
	}


	public void setImpairmentType(String impairmentType) {
		this.impairmentType = impairmentType;
	}

}
