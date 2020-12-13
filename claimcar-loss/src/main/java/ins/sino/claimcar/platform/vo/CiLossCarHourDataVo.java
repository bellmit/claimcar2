package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 工时/辅料明细列表
 * @author j2eel
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiLossCarHourDataVo {
	
	@XmlElement(name = "MAN_HOUR_CODE")
	private String manHourCode;		//工时名称代码
	
	@XmlElement(name = "MAN_HOUR_NAME")
	private String manHourName;		//工时名称
	
	@XmlElement(name = "MAN_HOUR_PRICE")
	private String manHourPrice;	//工时费用

	@XmlElement(name = "ACCESSORIES_NAME")
	private String accessoriesName; //辅料名称

	@XmlElement(name = "ACCESSORIES_PRICE")
	private String accessoriesPrice; //辅料费用

	public String getManHourCode() {
		return manHourCode;
	}

	public void setManHourCode(String manHourCode) {
		this.manHourCode = manHourCode;
	}

	public String getManHourName() {
		return manHourName;
	}

	public void setManHourName(String manHourName) {
		this.manHourName = manHourName;
	}

	public String getManHourPrice() {
		return manHourPrice;
	}

	public void setManHourPrice(String manHourPrice) {
		this.manHourPrice = manHourPrice;
	}

	public String getAccessoriesName() {
		return accessoriesName;
	}

	public void setAccessoriesName(String accessoriesName) {
		this.accessoriesName = accessoriesName;
	}

	public String getAccessoriesPrice() {
		return accessoriesPrice;
	}

	public void setAccessoriesPrice(String accessoriesPrice) {
		this.accessoriesPrice = accessoriesPrice;
	}

	

}
