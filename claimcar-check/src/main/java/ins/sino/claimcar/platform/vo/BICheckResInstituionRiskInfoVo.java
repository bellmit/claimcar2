/******************************************************************************
 * CREATETIME : 2016年4月29日 下午3:47:00
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 商业-->返回报文-->机构风险类型提示信息-->机构风险类型
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BICheckResInstituionRiskInfoVo {

	@XmlElement(name = "InstituionRiskType")
	private String instituionRiskType;// 机构风险类型

	public String getInstituionRiskType() {
		return instituionRiskType;
	}

	public void setInstituionRiskType(String instituionRiskType) {
		this.instituionRiskType = instituionRiskType;
	}

}
