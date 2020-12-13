package ins.sino.claimcar.mobile.check.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("PROPINFO")
public class PropInfoResVo implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("PROPID")
	private String propId; //损失方
	
	@XStreamAlias("PROPSERIALNO")
	private String propSerialNo; //损失方
	
	
	
	public String getPropId() {
		return propId;
	}

	public void setPropId(String propId) {
		this.propId = propId;
	}

	public String getPropSerialNo() {
		return propSerialNo;
	}

	public void setPropSerialNo(String propSerialNo) {
		this.propSerialNo = propSerialNo;
	}


}
