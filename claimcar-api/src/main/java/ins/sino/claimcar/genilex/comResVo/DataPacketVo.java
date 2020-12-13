package ins.sino.claimcar.genilex.comResVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("DataPacket")
public class DataPacketVo implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XStreamAsAttribute	
	@XStreamAlias("xmlns")
	private String xmlns="http://soap.fraud.message.genilex.com";
	@XStreamAlias("PacketType")
	private String packetType="RESPONSE";
	@XStreamAlias("Requestor")
	private RequestorVo requestor;
	@XStreamAlias("ResponseSummary")
	private ResponseSummaryVo responseSummaryVo;
	@XStreamAlias("ProductResponse")
	private ProductResponseVo productResponseVo;
	public String getXmlns() {
		return xmlns;
	}
	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}
	public String getPacketType() {
		return packetType;
	}
	public void setPacketType(String packetType) {
		this.packetType = packetType;
	}
	public RequestorVo getRequestor() {
		return requestor;
	}
	public void setRequestor(RequestorVo requestor) {
		this.requestor = requestor;
	}
	public ResponseSummaryVo getResponseSummaryVo() {
		return responseSummaryVo;
	}
	public void setResponseSummaryVo(ResponseSummaryVo responseSummaryVo) {
		this.responseSummaryVo = responseSummaryVo;
	}
	public ProductResponseVo getProductResponseVo() {
		return productResponseVo;
	}
	public void setProductResponseVo(ProductResponseVo productResponseVo) {
		this.productResponseVo = productResponseVo;
	}




}
