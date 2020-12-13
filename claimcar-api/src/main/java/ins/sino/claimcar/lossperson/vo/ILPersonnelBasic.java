package ins.sino.claimcar.lossperson.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("personnelbasic")
public class ILPersonnelBasic implements Serializable{

	private static final long serialVersionUID = 8423652723600188374L;
	
	@XStreamAlias("treatSituation")
	private String treatSituation;
	
	@XStreamAlias("woundCode")
	private String woundCode;

	public String getTreatSituation() {
		return treatSituation;
	}

	public void setTreatSituation(String treatSituation) {
		this.treatSituation = treatSituation;
	}

	public String getWoundCode() {
		return woundCode;
	}

	public void setWoundCode(String woundCode) {
		this.woundCode = woundCode;
	}
	
}
