package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "PACKET")
@XmlAccessorType(XmlAccessType.FIELD)
public class DlossReceiveReqVo implements Serializable{	
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "HEAD")
	private DlossReqHeadVo dlossReqHeadVo;
	@XmlElement(name = "BODY")
	private DlossReceiveBodyVo dlossReceiveBodyVo;
	public DlossReqHeadVo getDlossReqHeadVo() {
		return dlossReqHeadVo;
	}
	public void setDlossReqHeadVo(DlossReqHeadVo dlossReqHeadVo) {
		this.dlossReqHeadVo = dlossReqHeadVo;
	}
	public DlossReceiveBodyVo getDlossReceiveBodyVo() {
		return dlossReceiveBodyVo;
	}
	public void setDlossReceiveBodyVo(DlossReceiveBodyVo dlossReceiveBodyVo) {
		this.dlossReceiveBodyVo = dlossReceiveBodyVo;
	}
}
