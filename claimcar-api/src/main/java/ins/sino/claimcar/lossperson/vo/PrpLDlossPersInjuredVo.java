package ins.sino.claimcar.lossperson.vo;

/**
 * Custom VO class of PO PrpLDlossPersInjured
 */ 
public class PrpLDlossPersInjuredVo extends PrpLDlossPersInjuredVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	
	private String hospitalCode;
	
	private String hospitalCity;
	
	public String getHospitalCode() {
		return hospitalCode;
	}
	
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	
	public String getHospitalCity() {
		return hospitalCity;
	}
	
	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}

	
}
