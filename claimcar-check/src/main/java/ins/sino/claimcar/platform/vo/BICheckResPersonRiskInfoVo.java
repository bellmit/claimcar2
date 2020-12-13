/******************************************************************************
 * CREATETIME : 2016年4月29日 下午3:46:50
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 商业-->返回报文-->风险信息-->人员风险类型
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BICheckResPersonRiskInfoVo {

	@XmlElement(name = "PersonRiskType")
	private String personRiskType;// 人员风险类型

	public String getPersonRiskType() {
		return personRiskType;
	}

	public void setPersonRiskType(String personRiskType) {
		this.personRiskType = personRiskType;
	}

}
