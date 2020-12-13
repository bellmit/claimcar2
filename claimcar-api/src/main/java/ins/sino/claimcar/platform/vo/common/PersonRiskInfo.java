package ins.sino.claimcar.platform.vo.common;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PersonRiskInfo")
public class PersonRiskInfo {

	@XStreamAlias("PersonRiskType")
	private String personRiskType;

	public String getPersonRiskType() {
		return personRiskType;
	}

	public void setPersonRiskType(String personRiskType) {
		this.personRiskType = personRiskType;
	}

}
