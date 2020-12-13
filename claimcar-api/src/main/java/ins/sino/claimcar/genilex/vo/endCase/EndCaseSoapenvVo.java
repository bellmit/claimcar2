package ins.sino.claimcar.genilex.vo.endCase;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("soapenv:Envelope")
public class EndCaseSoapenvVo implements Serializable{

	@XStreamAsAttribute
	@XStreamAlias("xmlns:soapenv")
	private String xmlnssoapenv ="http://schemas.xmlsoap.org/soap/envelope/";
	@XStreamAsAttribute
	@XStreamAlias("xmlns")
	private String xmlns="http://soap.score.fraud.message.genilex.com";
	@XStreamAlias("soapenv:Header")
	private String soapenvHeader;
	@XStreamAlias("soapenv:Body")
	private EndCaseBody body;
	
	public String getXmlnssoapenv() {
		return xmlnssoapenv;
	}
	public void setXmlnssoapenv(String xmlnssoapenv) {
		this.xmlnssoapenv = xmlnssoapenv;
	}
	public String getXmlns() {
		return xmlns;
	}
	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}
	public String getSoapenvHeader() {
		return soapenvHeader;
	}
	public void setSoapenvHeader(String soapenvHeader) {
		this.soapenvHeader = soapenvHeader;
	}
	public EndCaseBody getBody() {
		return body;
	}
	public void setBody(EndCaseBody body) {
		this.body = body;
	}
	
	
}
