package ins.sino.claimcar.regist.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 交强报案平台返回信息BasePart类
 * @author dengkk
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiRegistBasePartResVo {
	
	@XmlElement(name="CLAIM_CODE", required = true)
	private String claimCode;//理赔编号

	public String getClaimCode() {
		return claimCode;
	}

	public void setClaimCode(String claimCode) {
		this.claimCode = claimCode;
	}
	
	

}
