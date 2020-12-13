/******************************************************************************
 * CREATETIME : 2016年5月24日 下午3:05:43
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiEndCaseFraudTypeDataVo {

	@XmlElement(name = "FRAUD_TYPE", required = true)
	private String fraudType;// 欺诈类型；参见代码

	/**
	 * @return 返回 fraudType 欺诈类型；参见代码
	 */
	public String getFraudType() {
		return fraudType;
	}

	/**
	 * @param fraudType 要设置的 欺诈类型；参见代码
	 */
	public void setFraudType(String fraudType) {
		this.fraudType = fraudType;
	}

}
