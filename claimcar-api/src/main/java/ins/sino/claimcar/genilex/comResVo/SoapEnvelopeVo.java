package ins.sino.claimcar.genilex.comResVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("soap:Envelope")
public class SoapEnvelopeVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XStreamAsAttribute
	@XStreamAlias("xmlns:soap")
	private String xmlnssoap="http://schemas.xmlsoap.org/soap/envelope/";
	@XStreamAlias("soap:Body")
	private ResBodyVo bodyVo;
	public String getXmlnssoap() {
		return xmlnssoap;
	}
	public void setXmlnssoap(String xmlnssoap) {
		this.xmlnssoap = xmlnssoap;
	}
	public ResBodyVo getBodyVo() {
		return bodyVo;
	}
	public void setBodyVo(ResBodyVo bodyVo) {
		this.bodyVo = bodyVo;
	}

}
