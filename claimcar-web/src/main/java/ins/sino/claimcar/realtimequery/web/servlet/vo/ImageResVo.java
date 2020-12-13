package ins.sino.claimcar.realtimequery.web.servlet.vo;

import java.io.Serializable;

public class ImageResVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String retCode;
	private String retMessage;
	private String insurerUuid;
	private ImageDateVo data;
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public String getRetMessage() {
		return retMessage;
	}
	public void setRetMessage(String retMessage) {
		this.retMessage = retMessage;
	}
	public String getInsurerUuid() {
		return insurerUuid;
	}
	public void setInsurerUuid(String insurerUuid) {
		this.insurerUuid = insurerUuid;
	}
	public ImageDateVo getData() {
		return data;
	}
	public void setData(ImageDateVo data) {
		this.data = data;
	}
	
	
	
}
