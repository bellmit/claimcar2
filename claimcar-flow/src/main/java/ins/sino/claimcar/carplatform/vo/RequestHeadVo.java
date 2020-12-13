/******************************************************************************
* CREATETIME : 2016年3月14日 上午10:33:11
******************************************************************************/
package ins.sino.claimcar.carplatform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 请求车险平台头部
 * @author ★LiuPing
 * @CreateTime 2016年3月14日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HEAD")
public class RequestHeadVo {
	
	@XmlElement(name="REQUEST_TYPE")
	private String requestType;
	
	@XmlElement(name="USER")
	private String user;
	
	@XmlElement(name="PASSWORD")
	private String password;

	
	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
