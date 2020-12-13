package ins.sino.claimcar.mobile.check.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("PERSONINFO")
public class PersonInfoResVo implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("PERSONID")
	private String personId; //理赔人伤ID
	
	@XStreamAlias("PERSONSERIALNO")
	private String personSerialNo; //移动终端人伤序号

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPersonSerialNo() {
		return personSerialNo;
	}

	public void setPersonSerialNo(String personSerialNo) {
		this.personSerialNo = personSerialNo;
	}


	
	
}
