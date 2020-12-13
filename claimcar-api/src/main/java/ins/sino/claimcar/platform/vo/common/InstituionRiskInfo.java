package ins.sino.claimcar.platform.vo.common;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("InstituionRiskInfo")
public class InstituionRiskInfo {

	@XStreamAlias("InstituionRiskType")
	private String instituionRiskType;

	public String getInstituionRiskType() {
		return instituionRiskType;
	}

	public void setInstituionRiskType(String instituionRiskType) {
		this.instituionRiskType = instituionRiskType;
	}

}