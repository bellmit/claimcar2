package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CarInfoHistoryResKsBasePartDataVo {
	
	
	@XmlElement(name = "ErrorAreaCode", required = true)
	private String errorAreaCode;// 跨省查询失败的地区代码；参见代码
	
	
	
	/**
	 * @return the errorAreaCode
	 */
	public String getErrorAreaCode() {
		return errorAreaCode;
	}

	/**
	 * @param errorAreaCode
	 *            the errorAreaCode to set
	 */
	public void setErrorAreaCode(String errorAreaCode) {
		this.errorAreaCode = errorAreaCode;
	}

	
}
