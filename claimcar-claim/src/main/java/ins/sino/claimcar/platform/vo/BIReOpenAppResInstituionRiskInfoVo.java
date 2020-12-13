package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class BIReOpenAppResInstituionRiskInfoVo {
	
	@XmlElement(name = "InstituionRiskType")
	private String instituionRiskType;// 机构风险类型

	public String getInstituionRiskType() {
		return instituionRiskType;
	}

	public void setInstituionRiskType(String instituionRiskType) {
		this.instituionRiskType = instituionRiskType;
	}

}
